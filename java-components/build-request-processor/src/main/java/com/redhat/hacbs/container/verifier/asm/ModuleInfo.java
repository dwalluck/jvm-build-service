package com.redhat.hacbs.container.verifier.asm;

import org.objectweb.asm.tree.ModuleNode;

public class ModuleInfo {
    private final int access;

    public ModuleInfo(ModuleNode node) {
        this.access = node.access;
    }
}
