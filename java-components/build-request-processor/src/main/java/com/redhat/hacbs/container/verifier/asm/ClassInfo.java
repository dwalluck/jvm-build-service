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
import org.objectweb.asm.tree.ClassNode;

/**
 * Class info.
 */
public class ClassInfo implements Diffable<ClassInfo> {
    private final int access;

    private final List<AttributeInfo> attrs;

    private final Map<String, FieldInfo> fields;

    private final Map<String, InnerClassInfo> innerClasses;

    private final List<String> interfaces;

    private final Map<String, AnnotationInfo> invisibleAnnotations;

    private final Map<String, TypeAnnotationInfo> invisibleTypeAnnotations;

    private final ModuleInfo module;

    private final Map<String, MethodInfo> methods;

    private final String name;

    private final String nestHostClass;

    private final List<String> nestMembers;

    private final String outerClass;

    private final String outerMethod;

    private final String outerMethodDesc;

    private final List<String> permittedSubclasses;

    private final Map<String, RecordComponentInfo> recordComponents;

    private final String superName;

    private final String signature;

    private final String sourceDebug;

    private final String sourceFile;

    private final Map<String, AnnotationInfo> visibleAnnotations;

    private final Map<String, TypeAnnotationInfo> visibleTypeAnnotations;

    private final ClassVersion version;

    /**
     * Instantiates a new Class info.
     *
     * @param node the node
     */
    public ClassInfo(ClassNode node) {
        this.access = node.access;

        if (node.attrs != null) {
            this.attrs = new ArrayList<>();
            node.attrs.stream().map(AttributeInfo::new).forEach(this.attrs::add);
        } else {
            this.attrs = null;
        }

        this.fields = new LinkedHashMap<>();
        node.fields.forEach(field -> this.fields.put(field.name, new FieldInfo(field)));
        this.innerClasses = new LinkedHashMap<>();
        node.innerClasses.forEach(innerClass -> this.innerClasses.put(innerClass.name, new InnerClassInfo(innerClass)));
        this.interfaces = node.interfaces;

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

        this.methods = new LinkedHashMap<>();
        node.methods.forEach(method -> this.methods.put(method.name + method.desc, new MethodInfo(method)));

        if (node.module != null) {
            this.module = new ModuleInfo(node.module);
        } else {
            this.module = null;
        }

        this.name = node.name;
        this.nestHostClass = node.nestHostClass;
        this.nestMembers = node.nestMembers;
        this.outerClass = node.outerClass;
        this.outerMethod = node.outerMethod;
        this.outerMethodDesc = node.outerMethodDesc;
        this.permittedSubclasses = node.permittedSubclasses;

        if (node.recordComponents != null) {
            this.recordComponents = new LinkedHashMap<>();
            node.recordComponents
                    .forEach(recordComponent -> this.recordComponents.put(recordComponent.name + recordComponent.descriptor,
                            new RecordComponentInfo(recordComponent)));
        } else {
            this.recordComponents = null;
        }

        this.signature = node.signature;
        this.sourceDebug = node.sourceDebug;
        this.sourceFile = node.sourceFile;
        this.superName = node.superName;
        this.version = new ClassVersion(node.version);

        if (node.visibleAnnotations != null) {
            this.visibleAnnotations = new LinkedHashMap<>();
            node.visibleAnnotations.forEach(visibleAnnotation -> this.visibleAnnotations.put(visibleAnnotation.desc,
                    new AnnotationInfo(visibleAnnotation)));
        } else {
            this.visibleAnnotations = null;
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
     * Gets the attrs.
     *
     * @return the attrs
     */
    public List<AttributeInfo> getAttrs() {
        return Collections.unmodifiableList(attrs);
    }

    /**
     * Gets the fields.
     *
     * @return the fields
     */
    public Map<String, FieldInfo> getFields() {
        return Collections.unmodifiableMap(fields);
    }

    /**
     * Gets the inner classes.
     *
     * @return the inner classes
     */
    public Map<String, InnerClassInfo> getInnerClasses() {
        return Collections.unmodifiableMap(innerClasses);
    }

    /**
     * Gets the interfaces.
     *
     * @return the interfaces
     */
    public List<String> getInterfaces() {
        return Collections.unmodifiableList(interfaces);
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
     * Gets the module.
     *
     * @return the module
     */
    public ModuleInfo getModule() {
        return module;
    }

    /**
     * Gets the methods.
     *
     * @return the methods
     */
    public Map<String, MethodInfo> getMethods() {
        return Collections.unmodifiableMap(methods);
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
     * Gets the nest host class.
     *
     * @return the nest host class
     */
    public String getNestHostClass() {
        return nestHostClass;
    }

    /**
     * Gets the nest members.
     *
     * @return the nest members
     */
    public List<String> getNestMembers() {
        return Collections.unmodifiableList(nestMembers);
    }

    /**
     * Gets the outer class.
     *
     * @return the outer class
     */
    public String getOuterClass() {
        return outerClass;
    }

    /**
     * Gets the outer method.
     *
     * @return the outer method
     */
    public String getOuterMethod() {
        return outerMethod;
    }

    /**
     * Gets the outer method desc.
     *
     * @return the outer method desc
     */
    public String getOuterMethodDesc() {
        return outerMethodDesc;
    }

    /**
     * Gets the permitted subclasses.
     *
     * @return the permitted subclasses
     */
    public List<String> getPermittedSubclasses() {
        return Collections.unmodifiableList(permittedSubclasses);
    }

    /**
     * Gets the record components.
     *
     * @return the record components
     */
    public Map<String, RecordComponentInfo> getRecordComponents() {
        return Collections.unmodifiableMap(recordComponents);
    }

    /**
     * Gets the super name.
     *
     * @return the super name
     */
    public String getSuperName() {
        return superName;
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
     * Gets the source debug.
     *
     * @return the source debug
     */
    public String getSourceDebug() {
        return sourceDebug;
    }

    /**
     * Gets the source file.
     *
     * @return the source file
     */
    public String getSourceFile() {
        return sourceFile;
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
     * Gets the visible type annotations.
     *
     * @return the visible type annotations
     */
    public Map<String, TypeAnnotationInfo> getVisibleTypeAnnotations() {
        return Collections.unmodifiableMap(visibleTypeAnnotations);
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public ClassVersion getVersion() {
        return version;
    }

    @Override
    public DiffResult<ClassInfo> diff(ClassInfo obj) {
        return new DiffBuilder<>(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("access", this.access, obj.access)
                .append("attrs", this.attrs, obj.attrs)
                .append("fields", this.fields, obj.fields)
                .append("innerClasses", this.innerClasses, obj.innerClasses)
                .append("interfaces", this.interfaces, obj.interfaces)
                .append("invisibleAnnotations", this.invisibleAnnotations, obj.invisibleAnnotations)
                .append("invisibleTypeAnnotations", this.invisibleTypeAnnotations, obj.invisibleTypeAnnotations)
                .append("methods", this.methods, obj.methods)
                .append("module", this.module, obj.module)
                .append("name", this.name, obj.name)
                .append("nestHostClass", this.nestHostClass, obj.nestHostClass)
                .append("nestMembers", this.nestMembers, obj.nestMembers)
                .append("outerClass", this.outerClass, obj.outerClass)
                .append("outerMethod", this.outerMethod, obj.outerMethod)
                .append("outerMethodDesc", this.outerMethodDesc, obj.outerMethodDesc)
                .append("permittedSubclasses", this.permittedSubclasses, obj.permittedSubclasses)
                .append("recordComponent", this.recordComponents, obj.recordComponents)
                .append("signature", this.signature, obj.signature)
                .append("sourceDebug", this.sourceDebug, obj.sourceDebug)
                .append("sourceFile", this.sourceFile, obj.sourceFile)
                .append("superName", this.superName, obj.superName)
                .append("version", this.version, obj.version)
                .append("visibleAnnotations", this.visibleAnnotations, obj.visibleTypeAnnotations)
                .append("visibleTypeAnnotations", this.visibleTypeAnnotations, obj.visibleTypeAnnotations)
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

        var that = (ClassInfo) o;
        return access == that.access && Objects.equals(attrs, that.attrs) && fields.equals(that.fields)
                && innerClasses.equals(that.innerClasses) && interfaces.equals(that.interfaces)
                && Objects.equals(invisibleAnnotations, that.invisibleAnnotations)
                && Objects.equals(invisibleTypeAnnotations, that.invisibleTypeAnnotations)
                && Objects.equals(module, that.module) && methods.equals(that.methods) && name.equals(that.name)
                && Objects.equals(nestHostClass, that.nestHostClass) && Objects.equals(nestMembers, that.nestMembers)
                && Objects.equals(outerClass, that.outerClass) && Objects.equals(outerMethod, that.outerMethod)
                && Objects.equals(outerMethodDesc, that.outerMethodDesc)
                && Objects.equals(permittedSubclasses, that.permittedSubclasses)
                && Objects.equals(recordComponents, that.recordComponents) && superName.equals(that.superName)
                && Objects.equals(signature, that.signature) && Objects.equals(sourceDebug, that.sourceDebug)
                && Objects.equals(sourceFile, that.sourceFile) && Objects.equals(visibleAnnotations, that.visibleAnnotations)
                && Objects.equals(visibleTypeAnnotations, that.visibleTypeAnnotations) && version.equals(that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(access, attrs, fields, innerClasses, interfaces, invisibleAnnotations, invisibleTypeAnnotations,
                module, methods, name, nestHostClass, nestMembers, outerClass, outerMethod, outerMethodDesc,
                permittedSubclasses, recordComponents, superName, signature, sourceDebug, sourceFile, visibleAnnotations,
                visibleTypeAnnotations, version);
    }

    @Override
    public String toString() {
        return name + ": " + version;
    }
}
