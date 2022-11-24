package com.redhat.hacbs.container.verifier.asm;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.tree.TypeAnnotationNode;

public class TypeAnnotationInfo implements Diffable<TypeAnnotationInfo> {
    private final String desc;

    private final List<Object> values;

    private final int typeRef;

    private final TypePath typePath;

    public TypeAnnotationInfo(TypeAnnotationNode node) {
        this.desc = node.desc;
        this.values = node.values;
        this.typeRef = node.typeRef;
        this.typePath = node.typePath;
    }

    @Override
    public DiffResult<TypeAnnotationInfo> diff(TypeAnnotationInfo obj) {
        return new DiffBuilder<>(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("desc", this.desc, obj.desc)
                .append("values", this.values, obj.values)
                .append("typeRef", this.typeRef, obj.typeRef)
                .append("typePath", this.typePath, obj.typePath)
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

        var that = (TypeAnnotationInfo) o;
        return desc.equals(that.desc) && Objects.equals(values, that.values) && typeRef == that.typeRef
                && Objects.equals(typePath, that.typePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(desc, values, typeRef, typePath);
    }

    @Override
    public String toString() {
        return desc;
    }
}
