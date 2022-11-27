package com.redhat.hacbs.container.verifier.asm;

import static org.objectweb.asm.Opcodes.V10;
import static org.objectweb.asm.Opcodes.V11;
import static org.objectweb.asm.Opcodes.V12;
import static org.objectweb.asm.Opcodes.V13;
import static org.objectweb.asm.Opcodes.V14;
import static org.objectweb.asm.Opcodes.V15;
import static org.objectweb.asm.Opcodes.V16;
import static org.objectweb.asm.Opcodes.V17;
import static org.objectweb.asm.Opcodes.V18;
import static org.objectweb.asm.Opcodes.V19;
import static org.objectweb.asm.Opcodes.V1_1;
import static org.objectweb.asm.Opcodes.V1_2;
import static org.objectweb.asm.Opcodes.V1_3;
import static org.objectweb.asm.Opcodes.V1_4;
import static org.objectweb.asm.Opcodes.V1_5;
import static org.objectweb.asm.Opcodes.V1_6;
import static org.objectweb.asm.Opcodes.V1_7;
import static org.objectweb.asm.Opcodes.V1_8;
import static org.objectweb.asm.Opcodes.V9;

import java.util.Objects;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Class version.
 */
class ClassVersion implements Diffable<ClassVersion> {
    private final int majorVersion;

    private final int minorVersion;

    /**
     * Creates a class version from the given integer version.
     *
     * @param version the version
     */
    public ClassVersion(int version) {
        majorVersion = version & 0xFFFF;
        minorVersion = version >>> 16;
    }

    /**
     * Gets the major version.
     *
     * @return the major version
     */
    public int getMajorVersion() {
        return majorVersion;
    }

    /**
     * Gets the minor version.
     *
     * @return the minor version
     */
    public int getMinorVersion() {
        return minorVersion;
    }

    /**
     * Returns the Java version corresponding to this class version.
     *
     * @return the Java version
     */
    public String classVersionToJavaVersion() {
        int version = minorVersion << 16 | majorVersion;
        return switch (version) {
            case V1_1 -> "1.1";
            case V1_2 -> "1.2";
            case V1_3 -> "1.3";
            case V1_4 -> "1.4";
            case V1_5 -> "1.5";
            case V1_6 -> "1.6";
            case V1_7 -> "1.7";
            case V1_8 -> "8";
            case V9 -> "9";
            case V10 -> "10";
            case V11 -> "11";
            case V12 -> "12";
            case V13 -> "13";
            case V14 -> "14";
            case V15 -> "15";
            case V16 -> "16";
            case V17 -> "17";
            case V18 -> "18";
            case V19 -> "19";
            default -> throw new IllegalArgumentException("Unknown class version: " + version);
        };
    }

    @Override
    public DiffResult<ClassVersion> diff(ClassVersion obj) {
        return new DiffBuilder<>(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("majorVersion", this.majorVersion, obj.majorVersion)
                .append("minorVersion", this.minorVersion, obj.minorVersion)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        var that = (ClassVersion) o;
        return majorVersion == that.majorVersion && minorVersion == that.minorVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(majorVersion, minorVersion);
    }

    @Override
    public String toString() {
        return "version " + majorVersion + "." + minorVersion + " (Java " + classVersionToJavaVersion() + ")";
    }
}
