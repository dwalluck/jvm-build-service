package com.redhat.hacbs.container.verifier.asm;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;





public class MethodInfo implements Diffable<MethodInfo> {
    private List<AttributeInfo> attrs;

    private final List<String> exceptions;

    private final Map<String, AnnotationInfo> invisibleAnnotations;

    private final Map<String, TypeAnnotationInfo> invisibleTypeAnnotations;

    private final Map<String, TypeAnnotationInfo> visibleTypeAnnotations;

    private final Object annotationDefault;

    private final String desc;

    private final String name;

    private final String signature;

    private final int access;

    private final int invisibleAnnotableParameterCount;

    private final int visibleAnnotableParameterCount;

    public MethodInfo(MethodNode node) {
        this.access = node.access;
        this.annotationDefault = node.annotationDefault;

        if (node.attrs != null) {
            this.attrs = new ArrayList<>(node.attrs.size());

            for (var attribute : node.attrs) {
                this.attrs.add(new AttributeInfo(attribute));
            }
        }

        this.desc = node.desc;
        this.exceptions = node.exceptions;
        //node.instructions
        this.invisibleAnnotableParameterCount = node.invisibleAnnotableParameterCount;
        this.invisibleAnnotations = new LinkedHashMap<>();

        if (node.invisibleAnnotations != null) {
            for (var invisibleAnnotation : node.invisibleAnnotations) {
                this.invisibleAnnotations.put(invisibleAnnotation.desc, new AnnotationInfo(invisibleAnnotation));
            }
        }
//            node.invisibleLocalVariableAnnotations
//                node.invisibleParameterAnnotations
        this.invisibleTypeAnnotations = new LinkedHashMap<>();

        if (node.invisibleTypeAnnotations != null) {
            for (var invisibleTypeAnnotation : node.invisibleTypeAnnotations) {
                this.invisibleAnnotations.put(invisibleTypeAnnotation.desc, new TypeAnnotationInfo(invisibleTypeAnnotation));
            }
        }

//                        node.localVariables
        this.name = node.name;
        this.signature = node.signature;
        this.visibleAnnotableParameterCount = node.visibleAnnotableParameterCount;

        if (node.visibleAnnotations != null) {
            for (var visibleAnnotation : node.visibleAnnotations) {
                this.invisibleAnnotations.put(visibleAnnotation.desc, new AnnotationInfo(visibleAnnotation));
            }
        }

//                                        node.visibleLocalVariableAnnotations
//                                                node.visibleParameterAnnotations
        this.visibleTypeAnnotations = new LinkedHashMap<>();

        if (node.visibleTypeAnnotations != null) {
            for (var visibleTypeAnnotation : node.visibleTypeAnnotations) {
                this.invisibleAnnotations.put(visibleTypeAnnotation.desc, new TypeAnnotationInfo(visibleTypeAnnotation));
            }
        }
    }

    /**
     * <p>Retrieves a list of the differences between
     * this object and the supplied object.</p>
     *
     * @param obj the object to diff against, can be {@code null}
     * @return a list of differences
     * @throws NullPointerException if the specified object is {@code null}
     */
    @Override
    public DiffResult<MethodInfo> diff(MethodInfo obj) {
        return new DiffBuilder<>(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("access", this.access, obj.access)
            .append("annotationDefault", this.annotationDefault, obj.annotationDefault)
            .append("attrs", this.attrs, obj.attrs)
            .append("desc", this.desc, obj.desc)
            .append("exceptions", this.exceptions, obj.exceptions)
            .append("name", this.name, obj.name)
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

        MethodInfo that = (MethodInfo) o;
        return access == that.access && invisibleAnnotableParameterCount == that.invisibleAnnotableParameterCount && visibleAnnotableParameterCount == that.visibleAnnotableParameterCount && Objects.equals(attrs, that.attrs) && exceptions.equals(that.exceptions) && Objects.equals(invisibleAnnotations, that.invisibleAnnotations) && Objects.equals(invisibleTypeAnnotations, that.invisibleTypeAnnotations) && Objects.equals(visibleTypeAnnotations, that.visibleTypeAnnotations) && Objects.equals(this.annotationDefault, that.annotationDefault) && desc.equals(that.desc) && name.equals(that.name) && Objects.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attrs, exceptions, invisibleAnnotations, invisibleTypeAnnotations, visibleTypeAnnotations, annotationDefault, desc, name, signature, access, invisibleAnnotableParameterCount, visibleAnnotableParameterCount);
    }

    @Override
    public String toString() {
        return "MethodInfo{" +
            "attrs=" + attrs +
            ", exceptions=" + exceptions +
            ", invisibleAnnotations=" + invisibleAnnotations +
            ", invisibleTypeAnnotations=" + invisibleTypeAnnotations +
            ", visibleTypeAnnotations=" + visibleTypeAnnotations +
            ", annotationDefault=" + annotationDefault +
            ", desc='" + desc + '\'' +
            ", name='" + name + '\'' +
            ", signature='" + signature + '\'' +
            ", access=" + access +
            ", invisibleAnnotableParameterCount=" + invisibleAnnotableParameterCount +
            ", visibleAnnotableParameterCount=" + visibleAnnotableParameterCount +
            '}';
    }
}
