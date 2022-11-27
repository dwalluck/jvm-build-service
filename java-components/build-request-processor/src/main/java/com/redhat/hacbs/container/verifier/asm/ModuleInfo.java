package com.redhat.hacbs.container.verifier.asm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.objectweb.asm.tree.ModuleNode;
import org.objectweb.asm.tree.ModuleProvideNode;

/**
 * Module info.
 */
public class ModuleInfo implements Diffable<ModuleInfo> {
    private final int access;

    private final List<ModuleExportInfo> exports;

    private final String mainClass;

    private final String name;

    private final List<ModuleOpenInfo> opens;

    private final List<String> packages;

    private final List<ModuleProvideInfo> provides;

    private final List<ModuleRequireInfo> requires;

    private final List<String> uses;

    private final String version;

    /**
     * Creates a new module info from the given module node.
     *
     * @param node the module node
     */
    public ModuleInfo(ModuleNode node) {
        this.access = node.access;

        if (node.exports != null) {
            this.exports = new ArrayList<>();
            node.exports.stream().map(ModuleExportInfo::new).forEach(this.exports::add);
        } else {
            this.exports = null;
        }

        this.mainClass = node.mainClass;
        this.name = node.name;

        if (node.opens != null) {
            this.opens = new ArrayList<>();
            node.opens.stream().map(ModuleOpenInfo::new).forEach(this.opens::add);
        } else {
            this.opens = null;
        }

        this.packages = node.packages;

        if (node.provides != null) {
            this.provides = new ArrayList<>();
            List<ModuleProvideNode> moduleProvideNodes = node.provides;
            moduleProvideNodes.stream().map(ModuleProvideInfo::new).forEach(this.provides::add);
        } else {
            this.provides = null;
        }

        if (node.requires != null) {
            this.requires = new ArrayList<>();
            node.requires.stream().map(ModuleRequireInfo::new).forEach(this.requires::add);
        } else {
            this.requires = null;
        }

        this.uses = node.uses;
        this.version = node.version;
    }

    @Override
    public DiffResult<ModuleInfo> diff(ModuleInfo obj) {
        return new DiffBuilder<>(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("access", this.access, obj.access)
                .append("exports", this.exports, obj.exports)
                .append("mainClass", this.mainClass, obj.mainClass)
                .append("name", this.name, obj.name)
                .append("opens", this.opens, obj.opens)
                .append("packages", this.packages, obj.packages)
                .append("provides", this.provides, obj.provides)
                .append("requires", this.requires, obj.requires)
                .append("uses", this.uses, obj.uses)
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

        var that = (ModuleInfo) o;
        return access == that.access && Objects.equals(exports, that.exports) && Objects.equals(mainClass, that.mainClass)
                && name.equals(that.name) && Objects.equals(opens, that.opens) && Objects.equals(packages, that.packages)
                && Objects.equals(provides, that.provides) && Objects.equals(requires, that.requires)
                && Objects.equals(uses, that.uses) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(access, exports, mainClass, name, opens, packages, provides, requires, uses, version);
    }

    @Override
    public String toString() {
        return name;
    }
}
