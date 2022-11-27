package com.redhat.hacbs.container.verifier.asm;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.objectweb.asm.tree.ModuleExportNode;

/**
 * Module export info.
 */
public class ModuleExportInfo implements Diffable<ModuleExportInfo> {
    private final int access;

    private final List<String> modules;

    private final String packaze;

    /**
     * Creates a new module export info from the given module export node.
     *
     * @param node the module export node
     */
    public ModuleExportInfo(ModuleExportNode node) {
        this.access = node.access;
        this.modules = node.modules;
        this.packaze = node.packaze;
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
     * Gets the modules.
     *
     * @return the modules
     */
    public List<String> getModules() {
        return modules;
    }

    /**
     * Gets the package.
     *
     * @return the package
     */
    public String getPackage() {
        return packaze;
    }

    @Override
    public DiffResult<ModuleExportInfo> diff(ModuleExportInfo obj) {
        return new DiffBuilder<>(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("access", this.access, obj.access)
                .append("modules", this.modules, obj.modules)
                .append("packaze", this.packaze, obj.packaze)
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

        var that = (ModuleExportInfo) o;
        return access == that.access && Objects.equals(modules, that.modules) && packaze.equals(that.packaze);
    }

    @Override
    public int hashCode() {
        return Objects.hash(access, modules, packaze);
    }

    @Override
    public String toString() {
        return "ModuleExportInfo{" +
                "access=" + access +
                ", modules=" + modules +
                ", packaze='" + packaze + '\'' +
                '}';
    }
}
