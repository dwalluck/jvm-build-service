package com.redhat.hacbs.container.verifier;

import static com.redhat.hacbs.container.verifier.MavenUtils.CENTRAL_URL;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import picocli.CommandLine;

public class VerifyBuiltArtifactCommandTest {
    @Test
    void testCommand(@TempDir Path temp) throws IOException, URISyntaxException {
        URI uri = VerifyBuiltArtifactCommandTest.class.getResource("/verifier/info.txt").toURI();
        Path file = Path.of(uri);
        var args = new String[] { "-r", CENTRAL_URL, "--deploy-path", file.getParent().toString(), temp.toString() };
        int exitCode = new CommandLine(new VerifyBuiltArtifactCommand()).execute(args);
    }
}
