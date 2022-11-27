package com.redhat.hacbs.container.verifier.asm;

import java.util.Objects;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.objectweb.asm.tree.ModuleRequireNode;

/**
 * Module require info.
 */
public class ModuleRequireInfo implements Diffable<ModuleRequireInfo> {
    private final int access;

    private final String module;

    private final String version;

    /**
     * Creates a new module require info from the given module require node.
     *
     * @param node the module require node
     */
    public ModuleRequireInfo(ModuleRequireNode node) {
        this.access = node.access;
        this.module = node.module;
        this.version = node.version;
    }

    /**
     * Gets the access.
     *
     * @return the access
     */
    public int getAccess() {
        return access;
    }

    /**
     * Gets the module.
     *
     * @return the module
     */
    public String getModule() {
        return module;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    @Override
    public DiffResult<ModuleRequireInfo> diff(ModuleRequireInfo obj) {
        return new DiffBuilder<>(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("access", this.access, obj.access)
                .append("module", this.module, obj.module)
                .append("version", this.version, obj.version)
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

        var that = (ModuleRequireInfo) o;
        return access == that.access && module.equals(that.module) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(access, module, version);
    }

    @Override
    public String toString() {
        return "ModuleRequireInfo{" +
                "access=" + access +
                ", module='" + module + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
