package com.redhat.hacbs.container.verifier.asm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.objectweb.asm.tree.MethodNode;

/**
 * Method info.
 */
public class MethodInfo implements Diffable<MethodInfo> {
    private final int access;

    private final Object annotationDefault;

    private final List<AttributeInfo> attrs;

    private final String desc;

    private final List<String> exceptions;

    private final List<InsnInfo> instructions;

    private final int invisibleAnnotableParameterCount;

    private final Map<String, AnnotationInfo> invisibleAnnotations;

    private final Map<String, LocalVariableAnnotationInfo> invisibleLocalVariableAnnotations;

    private final List<List<AnnotationInfo>> invisibleParameterAnnotations;

    private final Map<String, TypeAnnotationInfo> invisibleTypeAnnotations;

    private final Map<String, LocalVariableInfo> localVariables;

    private final String name;

    private final int visibleAnnotableParameterCount;

    private final Map<String, AnnotationInfo> visibleAnnotations;

    private final List<List<AnnotationInfo>> visibleParameterAnnotations;

    private final Map<String, TypeAnnotationInfo> visibleTypeAnnotations;

    private final String signature;

    /**
     * Instantiates a new Method info.
     *
     * @param node the node
     */
    public MethodInfo(MethodNode node) {
        this.access = node.access;
        this.annotationDefault = node.annotationDefault;

        if (node.attrs != null) {
            this.attrs = new ArrayList<>(node.attrs.size());
            node.attrs.stream().map(AttributeInfo::new).forEach(this.attrs::add);
        } else {
            this.attrs = null;
        }

        this.desc = node.desc;
        this.exceptions = node.exceptions;
        this.instructions = new ArrayList<>();
        StreamSupport.stream(node.instructions.spliterator(), false)
                .forEach(instruction -> this.instructions.add(new InsnInfo(instruction)));
        this.invisibleAnnotableParameterCount = node.invisibleAnnotableParameterCount;

        if (node.invisibleAnnotations != null) {
            this.invisibleAnnotations = new LinkedHashMap<>();
            node.invisibleAnnotations.forEach(invisibleAnnotation -> this.invisibleAnnotations.put(invisibleAnnotation.desc,
                    new AnnotationInfo(invisibleAnnotation)));
        } else {
            this.invisibleAnnotations = null;
        }

        if (node.invisibleLocalVariableAnnotations != null) {
            this.invisibleLocalVariableAnnotations = new LinkedHashMap<>();
            node.invisibleLocalVariableAnnotations
                    .forEach(invisibleLocalVariableAnnotation -> this.invisibleLocalVariableAnnotations.put(
                            invisibleLocalVariableAnnotation.desc,
                            new LocalVariableAnnotationInfo(invisibleLocalVariableAnnotation)));
        } else {
            this.invisibleLocalVariableAnnotations = null;
        }

        if (node.invisibleParameterAnnotations != null) {
            this.invisibleParameterAnnotations = new ArrayList<>(node.invisibleParameterAnnotations.length);
            Arrays.stream(node.invisibleParameterAnnotations)
                    .forEach(invisibleParameterAnnotation -> this.invisibleParameterAnnotations
                            .add(invisibleParameterAnnotation.stream().map(AnnotationInfo::new).collect(Collectors.toList())));
        } else {
            this.invisibleParameterAnnotations = null;
        }

        if (node.invisibleTypeAnnotations != null) {
            this.invisibleTypeAnnotations = new LinkedHashMap<>();
            node.invisibleTypeAnnotations
                    .forEach(invisibleTypeAnnotation -> this.invisibleTypeAnnotations.put(invisibleTypeAnnotation.desc,
                            new TypeAnnotationInfo(invisibleTypeAnnotation)));
        } else {
            this.invisibleTypeAnnotations = null;
        }

        if (node.localVariables != null) {
            this.localVariables = new LinkedHashMap<>();
            node.localVariables.forEach(
                    localVariable -> this.localVariables.put(localVariable.name, new LocalVariableInfo(localVariable)));
        } else {
            this.localVariables = null;
        }

        this.name = node.name;
        this.signature = node.signature;
        this.visibleAnnotableParameterCount = node.visibleAnnotableParameterCount;

        if (node.visibleAnnotations != null) {
            this.visibleAnnotations = new LinkedHashMap<>();
            node.visibleAnnotations.forEach(visibleAnnotation -> this.visibleAnnotations.put(visibleAnnotation.desc,
                    new AnnotationInfo(visibleAnnotation)));
        } else {
            this.visibleAnnotations = null;
        }

        if (node.visibleParameterAnnotations != null) {
            this.visibleParameterAnnotations = new ArrayList<>(node.visibleParameterAnnotations.length);
            Arrays.stream(node.visibleParameterAnnotations)
                    .forEach(visibleParameterAnnotation -> this.visibleParameterAnnotations
                            .add(visibleParameterAnnotation.stream().map(AnnotationInfo::new).collect(Collectors.toList())));
        } else {
            this.visibleParameterAnnotations = null;
        }

        if (node.visibleTypeAnnotations != null) {
            this.visibleTypeAnnotations = new LinkedHashMap<>();
            node.visibleTypeAnnotations.forEach(visibleTypeAnnotation -> this.visibleTypeAnnotations
                    .put(visibleTypeAnnotation.desc, new TypeAnnotationInfo(visibleTypeAnnotation)));
        } else {
            this.visibleTypeAnnotations = null;
        }
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
     * Gets the annotation default.
     *
     * @return the annotation default
     */
    public Object getAnnotationDefault() {
        return annotationDefault;
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
     * Gets the desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Gets the exceptions.
     *
     * @return the exceptions
     */
    public List<String> getExceptions() {
        return Collections.unmodifiableList(exceptions);
    }

    /**
     * Gets the instructions.
     *
     * @return the instructions
     */
    public List<InsnInfo> getInstructions() {
        return Collections.unmodifiableList(instructions);
    }

    /**
     * Gets the invisible annotable parameter count.
     *
     * @return the invisible annotable parameter count
     */
    public int getInvisibleAnnotableParameterCount() {
        return invisibleAnnotableParameterCount;
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
     * Gets the invisible local variable annotations.
     *
     * @return the invisible local variable annotations
     */
    public Map<String, LocalVariableAnnotationInfo> getInvisibleLocalVariableAnnotations() {
        return Collections.unmodifiableMap(invisibleLocalVariableAnnotations);
    }

    /**
     * Gets the invisible parameter annotations.
     *
     * @return the invisible parameter annotations
     */
    public List<List<AnnotationInfo>> getInvisibleParameterAnnotations() {
        return Collections.unmodifiableList(invisibleParameterAnnotations);
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
     * Gets the local variables.
     *
     * @return the local variables
     */
    public Map<String, LocalVariableInfo> getLocalVariables() {
        return Collections.unmodifiableMap(localVariables);
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
     * Gets the visible annotable parameter count.
     *
     * @return the visible annotable parameter count
     */
    public int getVisibleAnnotableParameterCount() {
        return visibleAnnotableParameterCount;
    }

    /**
     * Gets the visible annotations.
     *
     * @return the visible annotations
     */
    public Map<String, AnnotationInfo> getVisibleAnnotations() {
        return Collections.unmodifiableMap(visibleAnnotations);
    }

    /**
     * Gets the visible parameter annotations.
     *
     * @return the visible parameter annotations
     */
    public List<List<AnnotationInfo>> getVisibleParameterAnnotations() {
        return Collections.unmodifiableList(visibleParameterAnnotations);
    }

    /**
     * Gets the visible type annotations.
     *
     * @return the visible type annotations
     */
    public Map<String, TypeAnnotationInfo> getVisibleTypeAnnotations() {
        return Collections.unmodifiableMap(visibleTypeAnnotations);
    }

    /**
     * Gets signature.
     *
     * @return the signature
     */
    public String getSignature() {
        return signature;
    }

    @Override
    public DiffResult<MethodInfo> diff(MethodInfo obj) {
        return new DiffBuilder<>(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("access", this.access, obj.access)
                .append("annotationDefault", this.annotationDefault, obj.annotationDefault)
                .append("attrs", this.attrs, obj.attrs)
                .append("desc", this.desc, obj.desc)
                .append("exceptions", this.exceptions, obj.exceptions)
                .append("instructions", this.instructions, obj.instructions)
                .append("invisibleAnnotableParameterCount", this.invisibleAnnotableParameterCount,
                        obj.invisibleAnnotableParameterCount)
                .append("invisibleAnnotations", this.invisibleAnnotations, obj.invisibleAnnotations)
                .append("invisibleLocalVariableAnnotations", this.invisibleLocalVariableAnnotations,
                        obj.invisibleLocalVariableAnnotations)
                .append("invisibleParameterAnnotations", this.invisibleParameterAnnotations, obj.invisibleParameterAnnotations)
                .append("invisibleTypeAnnotations", this.invisibleTypeAnnotations, obj.invisibleTypeAnnotations)
                .append("localVariables", this.localVariables, obj.localVariables)
                .append("name", this.name, obj.name)
                .append("visibleAnnotableParameterCount", this.visibleAnnotableParameterCount,
                        obj.visibleAnnotableParameterCount)
                .append("visibleAnnotations", this.visibleAnnotations, obj.visibleAnnotations)
                .append("visibleParameterAnnotations", this.visibleParameterAnnotations, obj.visibleParameterAnnotations)
                .append("visibleTypeAnnotations", this.visibleTypeAnnotations, obj.visibleTypeAnnotations)
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

        var that = (MethodInfo) o;
        return access == that.access && invisibleAnnotableParameterCount == that.invisibleAnnotableParameterCount
                && visibleAnnotableParameterCount == that.visibleAnnotableParameterCount
                && Objects.equals(annotationDefault, that.annotationDefault) && Objects.equals(attrs, that.attrs)
                && desc.equals(that.desc) && exceptions.equals(that.exceptions) && instructions.equals(that.instructions)
                && Objects.equals(invisibleAnnotations, that.invisibleAnnotations)
                && Objects.equals(invisibleLocalVariableAnnotations, that.invisibleLocalVariableAnnotations)
                && Objects.equals(invisibleParameterAnnotations, that.invisibleParameterAnnotations)
                && Objects.equals(invisibleTypeAnnotations, that.invisibleTypeAnnotations)
                && Objects.equals(localVariables, that.localVariables) && name.equals(that.name)
                && Objects.equals(visibleAnnotations, that.visibleAnnotations)
                && Objects.equals(visibleParameterAnnotations, that.visibleParameterAnnotations)
                && Objects.equals(visibleTypeAnnotations, that.visibleTypeAnnotations)
                && Objects.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(access, annotationDefault, attrs, desc, exceptions, instructions, invisibleAnnotableParameterCount,
                invisibleAnnotations, invisibleLocalVariableAnnotations, invisibleParameterAnnotations,
                invisibleTypeAnnotations, localVariables, name, visibleAnnotableParameterCount, visibleAnnotations,
                visibleParameterAnnotations, visibleTypeAnnotations, signature);
    }

    @Override
    public String toString() {
        return "MethodInfo{" +
                "access=" + access +
                ", annotationDefault=" + annotationDefault +
                ", attrs=" + attrs +
                ", desc='" + desc + '\'' +
                ", exceptions=" + exceptions +
                ", instructions=" + instructions +
                ", invisibleAnnotableParameterCount=" + invisibleAnnotableParameterCount +
                ", invisibleAnnotations=" + invisibleAnnotations +
                ", invisibleLocalVariableAnnotations=" + invisibleLocalVariableAnnotations +
                ", invisibleParameterAnnotations=" + invisibleParameterAnnotations +
                ", invisibleTypeAnnotations=" + invisibleTypeAnnotations +
                ", localVariables=" + localVariables +
                ", name='" + name + '\'' +
                ", visibleAnnotableParameterCount=" + visibleAnnotableParameterCount +
                ", visibleAnnotations=" + visibleAnnotations +
                ", visibleParameterAnnotations=" + visibleParameterAnnotations +
                ", visibleTypeAnnotations=" + visibleTypeAnnotations +
                ", signature='" + signature + '\'' +
                '}';
    }
}
