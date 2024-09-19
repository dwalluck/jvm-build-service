package jvmimagescan

import (
	"context"
	_ "embed"
	"fmt"
	corev1 "k8s.io/api/core/v1"
	"k8s.io/apimachinery/pkg/api/errors"
	"k8s.io/apimachinery/pkg/api/resource"
	"k8s.io/apimachinery/pkg/runtime"
	"k8s.io/apimachinery/pkg/types"
	"k8s.io/client-go/tools/record"
	ctrl "sigs.k8s.io/controller-runtime"
	"sigs.k8s.io/controller-runtime/pkg/client"
	"sigs.k8s.io/controller-runtime/pkg/controller/controllerutil"
	"sigs.k8s.io/controller-runtime/pkg/reconcile"
	"strings"
	"time"

	"github.com/go-logr/logr"
	"github.com/redhat-appstudio/jvm-build-service/pkg/apis/jvmbuildservice/v1alpha1"
	"github.com/redhat-appstudio/jvm-build-service/pkg/reconciler/util"
	tektonpipeline "github.com/tektoncd/pipeline/pkg/apis/pipeline/v1"
)

const (
	//TODO eventually we'll need to decide if we want to make this tuneable
	contextTimeout            = 300 * time.Second
	ImageScanFinalizer        = "jvmbuildservice.io/image-analysis-finalizer"
	ImageScanPipelineRunLabel = "jvmbuildservice.io/image-analysis-pipelinerun"
	JvmDependenciesResult     = "JVM_DEPENDENCIES"
	ImageDigestResult         = "IMAGE_DIGEST"
)

type ReconcileImageScan struct {
	client        client.Client
	scheme        *runtime.Scheme
	eventRecorder record.EventRecorder
}

func newReconciler(mgr ctrl.Manager) reconcile.Reconciler {
	return &ReconcileImageScan{
		client:        mgr.GetClient(),
		scheme:        mgr.GetScheme(),
		eventRecorder: mgr.GetEventRecorderFor("JvmImageScan"),
	}
}

func (r *ReconcileImageScan) Reconcile(ctx context.Context, request reconcile.Request) (reconcile.Result, error) {
	// Set the ctx to be Background, as the top-level context for incoming requests.
	var cancel context.CancelFunc
	ctx, cancel = context.WithTimeout(ctx, contextTimeout)
	defer cancel()
	log := ctrl.Log.WithName("jvmimagescan").WithValues("namespace", request.NamespacedName.Namespace, "resource", request.Name)

	ctx = logr.NewContext(ctx, log)
	ia := v1alpha1.JvmImageScan{}
	iaerr := r.client.Get(ctx, request.NamespacedName, &ia)
	if iaerr != nil {
		if !errors.IsNotFound(iaerr) {
			log.Error(iaerr, "Reconcile key %s as jvmimagescan unexpected error", request.NamespacedName.String())
			return ctrl.Result{}, iaerr
		}
	}

	pr := tektonpipeline.PipelineRun{}
	prerr := r.client.Get(ctx, request.NamespacedName, &pr)
	if prerr != nil {
		if !errors.IsNotFound(prerr) {
			log.Error(prerr, "Reconcile key %s as pipelinerun unexpected error", request.NamespacedName.String())
			return ctrl.Result{}, prerr
		}
	}

	if prerr != nil && iaerr != nil {
		log.Info(fmt.Sprintf("Reconcile key %s received not found errors for pipelineruns and jvmimagescan (probably deleted)", request.NamespacedName.String()))
		return ctrl.Result{}, nil
	}

	switch {
	case prerr == nil:
		log = log.WithValues("kind", "PipelineRun")
		ctx = logr.NewContext(ctx, log)
		return r.handlePipelineRunReceived(ctx, &pr)

	case iaerr == nil:
		result, err := r.handleImageScan(ctx, ia)
		if err != nil {
			log.Error(err, "failure reconciling JvmImageScan")
		}
		return result, err
	}

	return reconcile.Result{}, nil
}

func (r *ReconcileImageScan) handleImageScan(ctx context.Context, ia v1alpha1.JvmImageScan) (reconcile.Result, error) {

	switch ia.Status.State {
	case v1alpha1.JvmImageScanStateNew, "":
		return r.handleStateNew(ctx, &ia)
	}
	return reconcile.Result{}, nil
}

func (r *ReconcileImageScan) handlePipelineRunReceived(ctx context.Context, pr *tektonpipeline.PipelineRun) (reconcile.Result, error) {

	if pr.DeletionTimestamp != nil {
		//always remove the finalizer if it is deleted
		//but continue with the method
		//if the PR is deleted while it is running then we want to allow that
		result, err2 := removePipelineFinalizer(ctx, pr, r.client)
		if err2 != nil {
			return result, err2
		}
	} else if pr.Status.CompletionTime == nil {
		//not finished, add the finalizer if needed
		//these PRs can be aggressively pruned, we need the finalizer to
		//make sure we see the results
		if controllerutil.AddFinalizer(pr, ImageScanFinalizer) {
			return reconcile.Result{}, r.client.Update(ctx, pr)
		}
		return reconcile.Result{}, nil
	}

	ownerRefs := pr.GetOwnerReferences()
	ownerName := ""
	for _, ownerRef := range ownerRefs {
		if strings.EqualFold(ownerRef.Kind, "jvmimagescan") || strings.EqualFold(ownerRef.Kind, "jvmimagescans") {
			ownerName = ownerRef.Name
			break
		}
	}
	if len(ownerName) == 0 {
		msg := "pipelinerun missing jvmimagescan ownerrefs %s:%s"
		r.eventRecorder.Eventf(pr, corev1.EventTypeWarning, "MissingOwner", msg, pr.Namespace, pr.Name)
		log, _ := logr.FromContext(ctx)
		log.Info(fmt.Sprintf(msg, pr.Namespace, pr.Name))

		return reconcile.Result{}, nil
	}

	key := types.NamespacedName{Namespace: pr.Namespace, Name: ownerName}
	ia := v1alpha1.JvmImageScan{}
	err := r.client.Get(ctx, key, &ia)
	if err != nil {
		return reconcile.Result{}, err
	}
	deps := ""
	if pr.Status.Results != nil {
		for _, prRes := range pr.Status.Results {
			if prRes.Name == JvmDependenciesResult {
				deps = prRes.Value.StringVal
			} else if prRes.Name == ImageDigestResult {
				ia.Status.Digest = prRes.Value.StringVal
			}
		}
	}
	if deps != "" {
		return reconcile.Result{}, r.handleJavaDependencies(ctx, strings.Split(deps, ","), &ia)
	}
	ia.Status.State = v1alpha1.JvmImageScanStateComplete
	return reconcile.Result{}, r.client.Status().Update(ctx, &ia)
}

func removePipelineFinalizer(ctx context.Context, pr *tektonpipeline.PipelineRun, client client.Client) (reconcile.Result, error) {
	//remove the finalizer
	if controllerutil.RemoveFinalizer(pr, ImageScanFinalizer) {
		return reconcile.Result{}, client.Update(ctx, pr)
	}
	return reconcile.Result{}, nil
}

func (r *ReconcileImageScan) handleStateNew(ctx context.Context, ia *v1alpha1.JvmImageScan) (reconcile.Result, error) {
	//guard against " in image name, to prevent script injection
	if strings.Contains(ia.Spec.Image, "\"") {
		ia.Status.State = v1alpha1.JvmImageScanStateFailed
		ia.Status.Message = "invalid image name " + ia.Spec.Image
		return reconcile.Result{}, r.client.Status().Update(ctx, ia)
	}
	spec, err := r.createLookupPipeline(ctx, ia.Spec.Image)
	if err != nil {
		log, _ := logr.FromContext(ctx)
		log.Info(fmt.Sprintf("Create lookup pipeline failed for %s", ia.Spec.Image))
		ia.Status.State = v1alpha1.JvmImageScanStateFailed
		ia.Status.Message = err.Error()
		return reconcile.Result{}, r.client.Status().Update(ctx, ia)
	}

	pr := tektonpipeline.PipelineRun{}
	pr.Finalizers = []string{ImageScanFinalizer}
	pr.Spec.PipelineSpec = spec
	pr.Namespace = ia.Namespace
	pr.GenerateName = ia.Name + "-image-discovery-"
	pr.Labels = map[string]string{ImageScanPipelineRunLabel: ""}
	if err := controllerutil.SetOwnerReference(ia, &pr, r.scheme); err != nil {
		return reconcile.Result{}, err
	}
	ia.Status.State = v1alpha1.JvmImageScanStateDiscovering
	if err := r.client.Status().Update(ctx, ia); err != nil {
		return reconcile.Result{}, err
	}
	if err := r.client.Create(ctx, &pr); err != nil {
		return reconcile.Result{}, nil
	}
	return reconcile.Result{}, nil
}

func (r *ReconcileImageScan) handleJavaDependencies(ctx context.Context, deps []string, ia *v1alpha1.JvmImageScan) error {
	results := []v1alpha1.JavaDependency{}
	for _, dep := range deps {
		if len(dep) == 0 {
			continue
		}
		split := strings.Split(dep, ";")
		if len(split) < 2 {
			continue
		}
		source := split[1]
		if source == "null" {
			source = "unknown"
		}
		attrs := map[string]string{}
		for i := 2; i < len(split); i++ {
			val := split[i]
			idx := strings.Index(val, "=")
			if idx > 0 {
				attrs[val[0:idx]] = val[idx+1:]
			}
		}
		results = append(results, v1alpha1.JavaDependency{GAV: split[0], Source: source, Attributes: attrs})
	}
	ia.Status.State = v1alpha1.JvmImageScanStateComplete
	ia.Status.Results = results

	return r.client.Status().Update(ctx, ia)
}

func (r *ReconcileImageScan) createLookupPipeline(ctx context.Context, image string) (*tektonpipeline.PipelineSpec, error) {

	buildReqProcessorImages, err := util.GetImageName(ctx, r.client, "build-request-processor", "JVM_BUILD_SERVICE_REQPROCESSOR_IMAGE")
	if err != nil {
		return nil, err
	}
	zero := int64(0)

	args := []string{
		"analyse-dependencies",
		"image",
		"--task-run-name=$(context.taskRun.name)",
		"-s",
		"/data/syft.json",
		"--output-all-dependencies",
		image,
	}

	pullPolicy := corev1.PullIfNotPresent
	if strings.HasSuffix(image, "dev") {
		pullPolicy = corev1.PullAlways
	}
	envVars := []corev1.EnvVar{
		{Name: "JAVA_OPTS", Value: "-XX:+CrashOnOutOfMemoryError"},
	}
	//TODO: this pulls twice

	return &tektonpipeline.PipelineSpec{
		Results: []tektonpipeline.PipelineResult{
			{Name: JvmDependenciesResult, Value: tektonpipeline.ResultValue{Type: tektonpipeline.ParamTypeString, StringVal: "$(tasks.task.results." + JvmDependenciesResult + ")"}},
			{Name: ImageDigestResult, Value: tektonpipeline.ResultValue{Type: tektonpipeline.ParamTypeString, StringVal: "$(tasks.task.results." + ImageDigestResult + ")"}},
		},
		Tasks: []tektonpipeline.PipelineTask{
			{
				Name: "task",
				TaskSpec: &tektonpipeline.EmbeddedTask{
					TaskSpec: tektonpipeline.TaskSpec{
						Volumes: []corev1.Volume{{Name: "data", VolumeSource: corev1.VolumeSource{EmptyDir: &corev1.EmptyDirVolumeSource{}}}},
						Results: []tektonpipeline.TaskResult{{Name: JvmDependenciesResult}, {Name: ImageDigestResult}},
						Steps: []tektonpipeline.Step{
							{
								Name:            "run-syft",
								Image:           "quay.io/redhat-appstudio/syft:v0.95.0", //TODO: hard coded
								ImagePullPolicy: pullPolicy,
								SecurityContext: &corev1.SecurityContext{RunAsUser: &zero},
								Script:          "syft \"" + image + "\" --output cyclonedx-json=/data/syft.json",
								ComputeResources: corev1.ResourceRequirements{
									//TODO: make configurable
									Requests: corev1.ResourceList{"memory": resource.MustParse("1024Mi")},
									Limits:   corev1.ResourceList{"memory": resource.MustParse("1024Mi")},
								},
								Env:          envVars,
								VolumeMounts: []corev1.VolumeMount{{Name: "data", MountPath: "/data"}},
							},
							{
								Name:            "analyze-dependencies",
								Image:           buildReqProcessorImages,
								ImagePullPolicy: pullPolicy,
								SecurityContext: &corev1.SecurityContext{RunAsUser: &zero},
								Args:            args,
								ComputeResources: corev1.ResourceRequirements{
									//TODO: make configurable
									Requests: corev1.ResourceList{"memory": resource.MustParse("512Mi")},
									Limits:   corev1.ResourceList{"memory": resource.MustParse("512Mi")},
								},
								Env:          envVars,
								VolumeMounts: []corev1.VolumeMount{{Name: "data", MountPath: "/data"}},
							},
						},
					},
				},
			},
		},
	}, nil
}
