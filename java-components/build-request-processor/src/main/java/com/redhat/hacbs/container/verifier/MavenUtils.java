package com.redhat.hacbs.container.verifier;

import java.nio.file.Path;
import java.util.List;

import org.apache.maven.index.artifact.Gav;
import org.apache.maven.index.artifact.M2GavCalculator;
import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.apache.maven.settings.Settings;
import org.apache.maven.settings.building.DefaultSettingsBuilderFactory;
import org.apache.maven.settings.building.DefaultSettingsBuildingRequest;
import org.apache.maven.settings.building.SettingsBuildingException;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.connector.basic.BasicRepositoryConnectorFactory;
import org.eclipse.aether.repository.AuthenticationSelector;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.MirrorSelector;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.spi.connector.RepositoryConnectorFactory;
import org.eclipse.aether.spi.connector.transport.TransporterFactory;
import org.eclipse.aether.transport.file.FileTransporterFactory;
import org.eclipse.aether.transport.http.HttpTransporterFactory;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.eclipse.aether.util.repository.DefaultAuthenticationSelector;
import org.eclipse.aether.util.repository.DefaultMirrorSelector;

/**
 * Maven utilities.
 */
public class MavenUtils {
    public static final String CENTRAL_ID = "central";

    public static final String CENTRAL_TYPE = "default";

    public static final String CENTRAL_URL = "https://repo1.maven.org/maven2";

    /**
     * Creates a new repository system.
     *
     * @return the new repository system
     */
    public static RepositorySystem newRepositorySystem() {
        var locator = MavenRepositorySystemUtils.newServiceLocator();
        locator.addService(RepositoryConnectorFactory.class, BasicRepositoryConnectorFactory.class);
        locator.addService(TransporterFactory.class, FileTransporterFactory.class);
        locator.addService(TransporterFactory.class, HttpTransporterFactory.class);
        return locator.getService(RepositorySystem.class);
    }

    /**
     * Creates a new settings.
     *
     * @return the new settings
     */
    public static Settings newSettings() {
        try {
            var builder = new DefaultSettingsBuilderFactory().newInstance();
            var request = new DefaultSettingsBuildingRequest();
            var result = builder.build(request);
            return result.getEffectiveSettings();
        } catch (SettingsBuildingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new mirror selector from the given settings.
     *
     * @param settings the settings
     * @return the new mirror selector
     */
    public static MirrorSelector newMirrorSelector(Settings settings) {
        var mirrors = settings.getMirrors();
        var selector = new DefaultMirrorSelector();
        mirrors.forEach(mirror -> {
            var id = mirror.getId();
            var url = mirror.getUrl();
            var layout = mirror.getLayout();
            var blocked = mirror.isBlocked();
            var mirrorOf = mirror.getMirrorOf();
            var mirrorOfLayouts = mirror.getMirrorOfLayouts();
            selector.add(id, url, layout, false, blocked, mirrorOf, mirrorOfLayouts);
        });
        return selector;
    }

    /**
     * Creates a new authentication selector from the given settings.
     *
     * @param settings the settings
     * @return the new authentication selector
     */
    public static AuthenticationSelector newAuthenticationSelector(Settings settings) {
        var selector = new DefaultAuthenticationSelector();
        var servers = settings.getServers();
        servers.forEach(server -> {
            var id = server.getId();
            var username = server.getUsername();
            var password = server.getPassword();
            var privateKey = server.getPrivateKey();
            var passphrase = server.getPassphrase();
            var builder = new AuthenticationBuilder();
            builder.addUsername(username).addPassword(password).addPrivateKey(privateKey, passphrase);
            var authentication = builder.build();
            selector.add(id, authentication);
        });

        return selector;
    }

    /**
     * Resolves an artifact in the given base directory with the given coordinates to a file.
     *
     * @param basedir the base directory
     * @param coords the coordinates
     * @return the file
     */
    public static Path resolveArtifact(Path basedir, String coords) {
        var session = MavenRepositorySystemUtils.newSession();
        var settings = newSettings();
        session.setMirrorSelector(newMirrorSelector(settings));
        session.setAuthenticationSelector(newAuthenticationSelector(settings));
        var system = newRepositorySystem();
        var localRepository = new LocalRepository(basedir.toFile());
        var manager = system.newLocalRepositoryManager(session, localRepository);
        session.setLocalRepositoryManager(manager);
        var remoteRepository = new RemoteRepository.Builder(CENTRAL_ID, CENTRAL_TYPE, CENTRAL_URL).build();
        var remoteRepositories = List.of(remoteRepository);
        var request = new ArtifactRequest(new DefaultArtifact(coords), remoteRepositories, null);

        try {
            var result = system.resolveArtifact(session, request);
            var repository = result.getRepository();
            var artifact = result.getArtifact();
            var file = artifact.getFile();
            System.out.printf("Resolved artifact %s to file %s from repository %s%n", artifact, file, repository);
            return file.toPath();
        } catch (ArtifactResolutionException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts the given {@link Gav Gav} to its coordinates.
     *
     * @param gav the Gav
     * @return the coordinates
     */
    public static String gavToCoords(Gav gav) {
        var groupId = gav.getGroupId();
        var artifactId = gav.getArtifactId();
        var extension = gav.getExtension() != null ? ":" + gav.getExtension() : "";
        var classifier = gav.getClassifier() != null ? ":" + gav.getClassifier() : "";
        var version = gav.getVersion();
        return groupId + ":" + artifactId + extension + classifier + ":" + version;
    }

    /**
     * Converts the given file to its string coordinates.
     *
     * @param file the path to the file
     * @return the coordinates
     */
    public static String pathToCoords(Path file) {
        var gav = new M2GavCalculator().pathToGav(file.toString());
        return gavToCoords(gav);
    }
}
