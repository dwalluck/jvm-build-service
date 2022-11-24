package com.redhat.hacbs.container.verifier.asm;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.objectweb.asm.tree.FieldNode;

/**
 * Field info.
 */
public class FieldInfo implements Diffable<FieldInfo> {
    private final int access;

    private final String desc;

    private final Map<String, AnnotationInfo> invisibleAnnotations;

    private final Map<String, TypeAnnotationInfo> invisibleTypeAnnotations;

    private final String name;

    private final String signature;

    private final Object value;

    /**
     * Creates a new field info from the given field node.
     *
     * @param node the field node
     */
    public FieldInfo(FieldNode node) {
        this.access = node.access;
        this.desc = node.desc;
        this.invisibleAnnotations = new LinkedHashMap<>();

        if (node.invisibleAnnotations != null) {
            for (var invisibleAnnotation : node.invisibleAnnotations) {
                this.invisibleAnnotations.put(invisibleAnnotation.desc, new AnnotationInfo(invisibleAnnotation));
            }
        }

        this.invisibleTypeAnnotations = new LinkedHashMap<>();

        if (node.invisibleTypeAnnotations != null) {
            for (var invisibleTypeAnnotation : node.invisibleTypeAnnotations) {
                this.invisibleTypeAnnotations.put(invisibleTypeAnnotation.desc,
                        new TypeAnnotationInfo(invisibleTypeAnnotation));
            }
        }

        this.name = node.name;
        this.signature = node.signature;
        this.value = node.value;
    }

    @Override
    public DiffResult<FieldInfo> diff(FieldInfo obj) {
        return new DiffBuilder<>(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("access", this.access, obj.access)
                .append("desc", this.desc, obj.desc)
                .append("invisibleAnnotations", this.invisibleAnnotations, obj.invisibleAnnotations)
                .append("invisibleTypeAnnotations", this.invisibleTypeAnnotations, obj.invisibleTypeAnnotations)
                .append("name", this.name, obj.name)
                .append("signature", this.signature, obj.signature)
                .append("value", this.value, obj.value)
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

        FieldInfo fieldInfo = (FieldInfo) o;
        return access == fieldInfo.access && desc.equals(fieldInfo.desc)
                && Objects.equals(invisibleAnnotations, fieldInfo.invisibleAnnotations)
                && Objects.equals(invisibleTypeAnnotations, fieldInfo.invisibleTypeAnnotations) && name.equals(fieldInfo.name)
                && Objects.equals(signature, fieldInfo.signature) && Objects.equals(value, fieldInfo.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(access, desc, invisibleAnnotations, invisibleTypeAnnotations, name, signature, value);
    }

    @Override
    public String toString() {
        return name + desc;
    }
}
