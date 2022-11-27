package com.redhat.hacbs.container.verifier.asm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.objectweb.asm.tree.RecordComponentNode;

/**
 * Record component info.
 */
public class RecordComponentInfo implements Diffable<RecordComponentInfo> {
    private final List<AttributeInfo> attrs;

    private final String descriptor;

    private final Map<String, AnnotationInfo> invisibleAnnotations;

    private final Map<String, TypeAnnotationInfo> invisibleTypeAnnotations;

    private final String name;

    private final String signature;

    /**
     * Creates a record component info from the given record component node.
     *
     * @param node the record component node
     */
    public RecordComponentInfo(RecordComponentNode node) {
        if (node.attrs != null) {
            this.attrs = new ArrayList<>(node.attrs.size());
            node.attrs.stream().map(AttributeInfo::new).forEach(this.attrs::add);
        } else {
            this.attrs = null;
        }

        this.descriptor = node.descriptor;

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
    }

    /**
     * Gets the attrs.
     *
     * @return the attrs
     */
    public List<AttributeInfo> getAttrs() {
        return Collections.unmodifiableList(attrs);
    }

    /**
     * Gets the descriptor.
     *
     * @return the descriptor
     */
    public String getDescriptor() {
        return descriptor;
    }

    /**
     * Gets the invisible annotations.
     *
     * @return the invisible annotations
     */
    public Map<String, AnnotationInfo> getInvisibleAnnotations() {
        return Collections.unmodifiableMap(invisibleAnnotations);
    }

    /**
     * Gets the invisible type annotations.
     *
     * @return the invisible type annotations
     */
    public Map<String, TypeAnnotationInfo> getInvisibleTypeAnnotations() {
        return Collections.unmodifiableMap(invisibleTypeAnnotations);
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

    @Override
    public DiffResult<RecordComponentInfo> diff(RecordComponentInfo obj) {
        return new DiffBuilder<>(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("attrs", this.attrs, obj.attrs)
                .append("descriptor", this.descriptor, obj.descriptor)
                .append("invisibleAnnotations", this.invisibleAnnotations, obj.invisibleAnnotations)
                .append("invisibleTypeAnnotations", this.invisibleTypeAnnotations, obj.invisibleTypeAnnotations)
                .append("name", this.name, obj.name)
                .append("signature", this.signature, obj.signature)
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

        var that = (RecordComponentInfo) o;
        return Objects.equals(attrs, that.attrs) && descriptor.equals(that.descriptor)
                && Objects.equals(invisibleAnnotations, that.invisibleAnnotations)
                && Objects.equals(invisibleTypeAnnotations, that.invisibleTypeAnnotations) && name.equals(that.name)
                && Objects.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attrs, descriptor, invisibleAnnotations, invisibleTypeAnnotations, name, signature);
    }

    @Override
    public String toString() {
        return "RecordComponentInfo{" +
                "attrs=" + attrs +
                ", descriptor='" + descriptor + '\'' +
                ", invisibleAnnotations=" + invisibleAnnotations +
                ", invisibleTypeAnnotations=" + invisibleTypeAnnotations +
                ", name='" + name + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
