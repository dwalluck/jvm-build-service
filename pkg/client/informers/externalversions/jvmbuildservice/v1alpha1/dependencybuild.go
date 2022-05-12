/*
Copyright 2021-2022 Red Hat, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
// Code generated by informer-gen. DO NOT EDIT.

package v1alpha1

import (
	"context"
	time "time"

	jvmbuildservicev1alpha1 "github.com/redhat-appstudio/jvm-build-service/pkg/apis/jvmbuildservice/v1alpha1"
	versioned "github.com/redhat-appstudio/jvm-build-service/pkg/client/clientset/versioned"
	internalinterfaces "github.com/redhat-appstudio/jvm-build-service/pkg/client/informers/externalversions/internalinterfaces"
	v1alpha1 "github.com/redhat-appstudio/jvm-build-service/pkg/client/listers/jvmbuildservice/v1alpha1"
	v1 "k8s.io/apimachinery/pkg/apis/meta/v1"
	runtime "k8s.io/apimachinery/pkg/runtime"
	watch "k8s.io/apimachinery/pkg/watch"
	cache "k8s.io/client-go/tools/cache"
)

// DependencyBuildInformer provides access to a shared informer and lister for
// DependencyBuilds.
type DependencyBuildInformer interface {
	Informer() cache.SharedIndexInformer
	Lister() v1alpha1.DependencyBuildLister
}

type dependencyBuildInformer struct {
	factory          internalinterfaces.SharedInformerFactory
	tweakListOptions internalinterfaces.TweakListOptionsFunc
	namespace        string
}

// NewDependencyBuildInformer constructs a new informer for DependencyBuild type.
// Always prefer using an informer factory to get a shared informer instead of getting an independent
// one. This reduces memory footprint and number of connections to the server.
func NewDependencyBuildInformer(client versioned.Interface, namespace string, resyncPeriod time.Duration, indexers cache.Indexers) cache.SharedIndexInformer {
	return NewFilteredDependencyBuildInformer(client, namespace, resyncPeriod, indexers, nil)
}

// NewFilteredDependencyBuildInformer constructs a new informer for DependencyBuild type.
// Always prefer using an informer factory to get a shared informer instead of getting an independent
// one. This reduces memory footprint and number of connections to the server.
func NewFilteredDependencyBuildInformer(client versioned.Interface, namespace string, resyncPeriod time.Duration, indexers cache.Indexers, tweakListOptions internalinterfaces.TweakListOptionsFunc) cache.SharedIndexInformer {
	return cache.NewSharedIndexInformer(
		&cache.ListWatch{
			ListFunc: func(options v1.ListOptions) (runtime.Object, error) {
				if tweakListOptions != nil {
					tweakListOptions(&options)
				}
				return client.JvmbuildserviceV1alpha1().DependencyBuilds(namespace).List(context.TODO(), options)
			},
			WatchFunc: func(options v1.ListOptions) (watch.Interface, error) {
				if tweakListOptions != nil {
					tweakListOptions(&options)
				}
				return client.JvmbuildserviceV1alpha1().DependencyBuilds(namespace).Watch(context.TODO(), options)
			},
		},
		&jvmbuildservicev1alpha1.DependencyBuild{},
		resyncPeriod,
		indexers,
	)
}

func (f *dependencyBuildInformer) defaultInformer(client versioned.Interface, resyncPeriod time.Duration) cache.SharedIndexInformer {
	return NewFilteredDependencyBuildInformer(client, f.namespace, resyncPeriod, cache.Indexers{cache.NamespaceIndex: cache.MetaNamespaceIndexFunc}, f.tweakListOptions)
}

func (f *dependencyBuildInformer) Informer() cache.SharedIndexInformer {
	return f.factory.InformerFor(&jvmbuildservicev1alpha1.DependencyBuild{}, f.defaultInformer)
}

func (f *dependencyBuildInformer) Lister() v1alpha1.DependencyBuildLister {
	return v1alpha1.NewDependencyBuildLister(f.Informer().GetIndexer())
}