package com.redhat.hacbs.container.verifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import com.redhat.hacbs.container.verifier.asm.ClassInfo;
import com.redhat.hacbs.container.verifier.asm.DiffUtils;
import com.redhat.hacbs.container.verifier.asm.FieldInfo;
import com.redhat.hacbs.container.verifier.asm.JarInfo;
import com.redhat.hacbs.container.verifier.asm.MethodInfo;
import com.redhat.hacbs.container.verifier.maven.MavenUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

import com.redhat.hacbs.container.build.preprocessor.AbstractPreprocessor;

import picocli.CommandLine;

/**
 * Command to verify a built artifact.
 */
@CommandLine.Command(name = "verify-built-artifact")
public class VerifyBuiltArtifactCommand extends AbstractPreprocessor {
    private final Path localRepository;

    @CommandLine.Option(required = true, names = "--deploy-path")
    Path deployPath;

    /**
     * Initializes this command by creating a temporary directory to store the downloaded artifacts. This prevents the
     * locally built version from accidentally being used.
     *
     * @throws IOException if an error occurs creating the temporary directory
     */
    public VerifyBuiltArtifactCommand() throws IOException {
        localRepository = Files.createTempDirectory("hacbs-verify-built-artifact-");
    }

    /**
     * Runs this command.
     */
    @Override
    public void run() {
        // FIXME: Remove
        if (deployPath == null) {
            deployPath = Path.of("/home/dwalluck/IdeaProjects/isorelax-20090621/hacbs-jvm-deployment-repo");
        }

        System.out.printf("Using deployPath %s%n", deployPath);

            /*Files.walkFileTree(deployPath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    var fileName = file.getFileName().toString();
                    System.out.printf("Handling file %s%n", fileName);

                    // XXX: Other extensions?
                    if (fileName.endsWith(".jar")) {
                        try {
                            handleJar(file);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }

            });*/
        handleJar(null);
    }

    private void handleJar(Path file) {
        //System.out.println("Handle jar " + deployPath.relativize(file));
        //var coords = MavenUtils.pathToCoords(deployPath.relativize(file));
        //System.out.println(coords);
        //var newInfo = newJarInfo(file);
        //var origInfo = newJarInfo(MavenUtils.resolveArtifact(localRepository, coords));
        //var origInfo = newJarInfo(MavenUtils.resolveArtifact(localRepository, "msv:isorelax:20050913"));
        JarInfo left = newJarInfo(MavenUtils.resolveArtifact(localRepository, "junit:junit:3.8.2"));
        JarInfo right = newJarInfo(MavenUtils.resolveArtifact(localRepository, "junit:junit:4.13.2"));
        diff(left, right);
    }

    // asm class visitor
    // $0 -> OK to skip class (lambda)
    // Add it in https://github.com/redhat-appstudio/jvm-build-service/blob/82fa864c52c818359c3ecd31f6b2164f96f6b9b0/pkg/reconciler/dependencybuild/buildrecipeyaml.go#L165

    private static JarInfo newJarInfo(Path path) {
        var jarInfo = new JarInfo();
        jarInfo.path = path;
        try (var in = new JarInputStream(Files.newInputStream(path))) {
            JarEntry entry;
            while ((entry = in.getNextJarEntry()) != null) {
                var name = entry.getName();
                //classInfo.name = name;
                // $$Lambda$x/NNNNNNNNNNNN
                if (name.endsWith(".class")) {
                    var reader = new ClassReader(in);
                    var node = new ClassNode();
                    reader.accept(node, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                    var classInfo = new ClassInfo(node);
                    jarInfo.classes.put(classInfo.name, classInfo);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return jarInfo;
    }

    private static void diff(JarInfo left, JarInfo right) {
        System.out.printf("Diffing jars %s and %s%n", left.path.toAbsolutePath(), right.path.toAbsolutePath());
        Triple<Set<String>, Set<String>, Set<String>> results = DiffUtils.diff(left.classes, right.classes);
        Set<String> sharedClasses = results.getLeft();

        for (var sharedClass : sharedClasses) {
            var leftClass = left.classes.get(sharedClass);
            var rightClass = right.classes.get(sharedClass);
            Map<String, FieldInfo> leftFields = leftClass.fields;
            Map<String, FieldInfo> rightFields = rightClass.fields;
            System.out.printf("Diffing fields of %s%n", leftClass.name);
            Set<String> sharedFields = DiffUtils.diff(leftFields, rightFields).getLeft();
            Map<String, MethodInfo> leftMethods = leftClass.methods;
            Map<String, MethodInfo> rightMethods = rightClass.methods;
            System.out.printf("Diffing methods of %s%n", leftClass.name);
            Set<String> sharedMethods = DiffUtils.diff(leftMethods, rightMethods).getLeft();
        }
    }

    public static void main(String[] args) throws ArtifactResolutionException, IOException {
        new VerifyBuiltArtifactCommand().run();
    }
}
