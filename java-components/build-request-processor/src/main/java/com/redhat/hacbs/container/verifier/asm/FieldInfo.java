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

        if (node.invisibleAnnotations != null) {
            this.invisibleAnnotations = new LinkedHashMap<>();
            node.invisibleAnnotations.forEach(invisibleAnnotation -> this.invisibleAnnotations.put(invisibleAnnotation.desc,
                    new AnnotationInfo(invisibleAnnotation)));
        } else {
            this.invisibleAnnotations = null;
        }

        if (node.invisibleTypeAnnotations != null) {
            this.invisibleTypeAnnotations = new LinkedHashMap<>();
            node.invisibleTypeAnnotations
                    .forEach(invisibleTypeAnnotation -> this.invisibleTypeAnnotations.put(invisibleTypeAnnotation.desc,
                            new TypeAnnotationInfo(invisibleTypeAnnotation)));
        } else {
            this.invisibleTypeAnnotations = null;
        }

        this.name = node.name;
        this.signature = node.signature;
        this.value = node.value;
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
     * Gets the desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Gets the invisible annotations.
     *
     * @return the invisible annotations
     */
    public Map<String, AnnotationInfo> getInvisibleAnnotations() {
        return invisibleAnnotations;
    }

    /**
     * Gets the invisible type annotations.
     *
     * @return the invisible type annotations
     */
    public Map<String, TypeAnnotationInfo> getInvisibleTypeAnnotations() {
        return invisibleTypeAnnotations;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the signature.
     *
     * @return the signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
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

        var that = (FieldInfo) o;
        return access == that.access && desc.equals(that.desc)
                && Objects.equals(invisibleAnnotations, that.invisibleAnnotations)
                && Objects.equals(invisibleTypeAnnotations, that.invisibleTypeAnnotations) && name.equals(that.name)
                && Objects.equals(signature, that.signature) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(access, desc, invisibleAnnotations, invisibleTypeAnnotations, name, signature, value);
    }

    @Override
    public String toString() {
        return "FieldInfo{" +
                "access=" + access +
                ", desc='" + desc + '\'' +
                ", invisibleAnnotations=" + invisibleAnnotations +
                ", invisibleTypeAnnotations=" + invisibleTypeAnnotations +
                ", name='" + name + '\'' +
                ", signature='" + signature + '\'' +
                ", value=" + value +
                '}';
    }
}
