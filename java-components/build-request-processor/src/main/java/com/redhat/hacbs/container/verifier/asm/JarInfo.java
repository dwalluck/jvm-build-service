package com.redhat.hacbs.container.verifier.asm;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Jar info.
 */
public class JarInfo {
    public transient Path path;

    public Map<String, ClassInfo> classes = new LinkedHashMap<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JarInfo jarInfo = (JarInfo) o;
        return Objects.equals(classes, jarInfo.classes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classes);
    }

    @Override
    public String toString() {
        return classes.toString();
    }
}
