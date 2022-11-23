package com.redhat.hacbs.container.verifier.asm;

import org.objectweb.asm.Attribute;

public class AttributeInfo {
    private final String type;

    public AttributeInfo(Attribute attribute) {
        this.type = attribute.type;
    }


}
