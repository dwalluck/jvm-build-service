package com.redhat.hacbs.container.verifier;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.eclipse.aether.resolution.ArtifactResolutionException;

import com.redhat.hacbs.container.build.preprocessor.AbstractPreprocessor;
import com.redhat.hacbs.container.verifier.asm.JarInfo;

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
        System.out.printf("Using deployPath %s%n", deployPath);

        try {
            Files.walkFileTree(deployPath, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    var fileName = file.getFileName().toString();
                    System.out.printf("Handling file %s%n", fileName);

                    if (fileName.endsWith(".jar")) {
                        try {
                            handleJar(file);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    return CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleJar(Path file) {
        var coords = MavenUtils.pathToCoords(deployPath.relativize(file));
        System.out.printf("Handle jar %s%n", deployPath.relativize(file));
        System.out.printf("Coordinates %s%n", coords);
        var remoteFile = MavenUtils.resolveArtifact(localRepository, coords);
        var left = new JarInfo(remoteFile);
        var right = new JarInfo(file);
        var fail = diff(left, right);
        System.out.printf("Verification %s%n", fail ? "failed" : "passed");
    }

    private static boolean isFailure(DiffUtils.DiffResults results) {
        return !results.diffResults().isEmpty() || !results.added().isEmpty() || !results.deleted().isEmpty();
    }

    private static boolean diff(JarInfo left, JarInfo right) {
        System.out.printf("Diffing jars %s and %s%n", left.file.toAbsolutePath(), right.file.toAbsolutePath());
        var fail = false;
        var results = DiffUtils.diff(left.getClasses(), right.getClasses());
        fail = isFailure(results);
        var sharedClasses = results.shared();
        System.out.printf("Diffing %d shared classes%n", sharedClasses.size());

        for (var sharedClass : sharedClasses) {
            var leftClass = left.getClass(sharedClass);
            var rightClass = right.getClass(sharedClass);
            var leftFields = leftClass.fields;
            var rightFields = rightClass.fields;
            var fieldsDiff = DiffUtils.diff(leftFields, rightFields);
            fail |= isFailure(fieldsDiff);
            var sharedFields = fieldsDiff.shared();
            var leftMethods = leftClass.methods;
            var rightMethods = rightClass.methods;
            var methodsDiff = DiffUtils.diff(leftMethods, rightMethods);
            fail |= isFailure(methodsDiff);
        }

        return fail;
    }

    public static void main(String[] args) throws ArtifactResolutionException, IOException {
        new VerifyBuiltArtifactCommand().run();
    }
}
