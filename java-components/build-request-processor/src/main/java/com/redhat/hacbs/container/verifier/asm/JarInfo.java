package com.redhat.hacbs.container.verifier.asm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

/**
 * Jar info.
 */
public class JarInfo {
    private final Path file;

    private final Map<String, ClassInfo> classes;

    /**
     * Creates a new jar info from the given jar file.
     *
     * @param file the jar file
     */
    public JarInfo(Path file) {
        this.file = file;

        try (var in = new JarInputStream(Files.newInputStream(this.file))) {
            JarEntry entry;
            this.classes = new LinkedHashMap<>();

            while ((entry = in.getNextJarEntry()) != null) {
                var name = entry.getName();

                // XXX: Also handle other files?
                if (!name.endsWith(".class")) {
                    continue;
                }

                // XXX: Skipping lambda for now
                if (name.contains("$$Lambda$")) {
                    System.out.printf("Skipping %s%n", name);
                }

                var reader = new ClassReader(in);
                var node = new ClassNode();
                reader.accept(node, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                var classInfo = new ClassInfo(node);
                this.classes.put(classInfo.getName(), classInfo);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the file.
     *
     * @return the file
     */
    public Path getFile() {
        return file;
    }

    /**
     * Gets the class with the give name from this jar, if any.
     *
     * @param name the name of the class
     * @return the class, if any
     */
    public ClassInfo getClass(String name) {
        return classes.get(name);
    }

    /**
     * Gets the classes in this jar.
     *
     * @return the classes
     */
    public Map<String, ClassInfo> getClasses() {
        return Collections.unmodifiableMap(classes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var that = (JarInfo) o;
        return file.equals(that.file) && classes.equals(that.classes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(file, classes);
    }

    @Override
    public String toString() {
        return "JarInfo{" +
                "file=" + file +
                ", classes=" + classes +
                '}';
    }
}
