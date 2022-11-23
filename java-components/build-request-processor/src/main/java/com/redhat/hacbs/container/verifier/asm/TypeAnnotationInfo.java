package com.redhat.hacbs.container.verifier.asm;

import org.objectweb.asm.TypePath;
import org.objectweb.asm.tree.TypeAnnotationNode;

public class TypeAnnotationInfo extends AnnotationInfo {
    private final int typeRef;

    private final TypePath typePath;

    public TypeAnnotationInfo(TypeAnnotationNode node) {
        super(node);
        this.typePath = node.typePath;
        this.typeRef = node.typeRef;
    }
}
