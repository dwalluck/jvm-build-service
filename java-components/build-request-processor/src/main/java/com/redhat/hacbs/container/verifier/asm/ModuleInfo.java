package com.redhat.hacbs.container.verifier.asm;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.objectweb.asm.tree.ModuleNode;

public class ModuleInfo implements Diffable<ModuleInfo> {
    private final int access;

    // TODO
    public ModuleInfo(ModuleNode node) {
        this.access = node.access;
    }

    @Override
    public DiffResult<ModuleInfo> diff(ModuleInfo obj) {
        return new DiffBuilder<>(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("access", this.access, obj.access)
                .build();
    }
}
