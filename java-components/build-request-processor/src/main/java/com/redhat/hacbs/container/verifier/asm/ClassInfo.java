package com.redhat.hacbs.container.verifier.asm;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.objectweb.asm.tree.ClassNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ClassInfo implements Diffable<ClassInfo> {
    private final int access;

    private final ModuleInfo module;

    public final Map<String, MethodInfo> methods;

    private final Map<String, InnerClassInfo> innerClasses;

    private final List<String> interfaces;

    private final Map<String, AnnotationInfo> invisibleAnnotations;

    private final Map<String, TypeAnnotationInfo> invisibleTypeAnnotations;

    private final String superName;
    private final ClassVersion version;
    private final String nestHostClass;
    private final List<String> nestMembers;
    private final String outerClass;
    private final String outerMethod;
    private final List<String> permittedSubclasses;
    private final String signature;
    private final String sourceDebug;
    private final String sourceFile;
    private final Map<String, AnnotationInfo> visibleAnnotations;
    private final Map<String, TypeAnnotationInfo> visibleTypeAnnotations;
    private final Map<String, RecordComponentInfo> recordComponents;

    public String name;

    ClassVersion classVersion;

    private List<AttributeInfo> attrs;

    public Map<String, FieldInfo> fields;
    private final String outerMethodDesc;

    public ClassInfo(ClassNode node) {
        this.access = node.access;

        if (node.attrs != null) {
            this.attrs = new ArrayList<>(node.attrs.size());

            for (var attribute : node.attrs) {
                this.attrs.add(new AttributeInfo(attribute));
            }
        }

        this.fields = new LinkedHashMap<>();

        for (var field : node.fields) {
            this.fields.put(field.name, new FieldInfo(field));
        }

        this.innerClasses = new LinkedHashMap<>();

        for (var innerClass : node.innerClasses) {
            this.innerClasses.put(innerClass.name, new InnerClassInfo(innerClass));
        }

        this.interfaces = node.interfaces;
        this.invisibleAnnotations = new LinkedHashMap<>();

        if (node.invisibleAnnotations != null) {
            for (var invisibleAnnotation : node.invisibleAnnotations) {
                this.invisibleAnnotations.put(invisibleAnnotation.desc, new AnnotationInfo(invisibleAnnotation));
            }
        }

        this.invisibleTypeAnnotations = new LinkedHashMap<>();

        if (node.invisibleTypeAnnotations != null) {
            for (var invisibleTypeAnnotation : node.invisibleTypeAnnotations) {
                this.invisibleAnnotations.put(invisibleTypeAnnotation.desc, new TypeAnnotationInfo(invisibleTypeAnnotation));
            }
        }

        this.methods = new LinkedHashMap<>();

        for (var method : node.methods) {
            this.methods.put(method.name + method.desc, new MethodInfo(method));
        }

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
        this.recordComponents = new LinkedHashMap<>();

        if (node.recordComponents != null) {
            for (var recordComponent : node.recordComponents) {
                this.recordComponents.put(recordComponent.name + recordComponent.descriptor, new RecordComponentInfo(recordComponent));
            }
        }

        this.signature = node.signature;
        this.sourceDebug = node.sourceDebug;
        this.sourceFile = node.sourceFile;
        this.superName = node.superName;
        this.version = new ClassVersion(node.version);
        this.visibleAnnotations = new LinkedHashMap<>();

        if (node.visibleAnnotations != null) {
            for (var visibleAnnotation : node.visibleAnnotations) {
                this.invisibleAnnotations.put(visibleAnnotation.desc, new AnnotationInfo(visibleAnnotation));
            }
        }

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassInfo classInfo = (ClassInfo) o;
        return access == classInfo.access && version == classInfo.version && Objects.equals(module, classInfo.module) && methods.equals(classInfo.methods) && innerClasses.equals(classInfo.innerClasses) && interfaces.equals(classInfo.interfaces) && Objects.equals(invisibleAnnotations, classInfo.invisibleAnnotations) && Objects.equals(invisibleTypeAnnotations, classInfo.invisibleTypeAnnotations) && superName.equals(classInfo.superName) && nestHostClass.equals(classInfo.nestHostClass) && nestMembers.equals(classInfo.nestMembers) && outerClass.equals(classInfo.outerClass) && outerMethod.equals(classInfo.outerMethod) && permittedSubclasses.equals(classInfo.permittedSubclasses) && signature.equals(classInfo.signature) && Objects.equals(sourceDebug, classInfo.sourceDebug) && Objects.equals(sourceFile, classInfo.sourceFile) && Objects.equals(visibleAnnotations, classInfo.visibleAnnotations) && Objects.equals(visibleTypeAnnotations, classInfo.visibleTypeAnnotations) && Objects.equals(recordComponents, classInfo.recordComponents) && name.equals(classInfo.name) && Objects.equals(classVersion, classInfo.classVersion) && Objects.equals(attrs, classInfo.attrs) && fields.equals(classInfo.fields) && outerMethodDesc.equals(classInfo.outerMethodDesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(access, module, methods, innerClasses, interfaces, invisibleAnnotations, invisibleTypeAnnotations, superName, version, nestHostClass, nestMembers, outerClass, outerMethod, permittedSubclasses, signature, sourceDebug, sourceFile, visibleAnnotations, visibleTypeAnnotations, recordComponents, name, classVersion, attrs, fields, outerMethodDesc);
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
            "access=" + access +
            ", module=" + module +
            ", methods=" + methods +
            ", innerClasses=" + innerClasses +
            ", interfaces=" + interfaces +
            ", invisibleAnnotations=" + invisibleAnnotations +
            ", invisibleTypeAnnotations=" + invisibleTypeAnnotations +
            ", superName='" + superName + '\'' +
            ", version=" + version +
            ", nestHostClass='" + nestHostClass + '\'' +
            ", nestMembers=" + nestMembers +
            ", outerClass='" + outerClass + '\'' +
            ", outerMethod='" + outerMethod + '\'' +
            ", permittedSubclasses=" + permittedSubclasses +
            ", signature='" + signature + '\'' +
            ", sourceDebug='" + sourceDebug + '\'' +
            ", sourceFile='" + sourceFile + '\'' +
            ", visibleAnnotations=" + visibleAnnotations +
            ", visibleTypeAnnotations=" + visibleTypeAnnotations +
            ", recordComponents=" + recordComponents +
            ", name='" + name + '\'' +
            ", classVersion=" + classVersion +
            ", attrs=" + attrs +
            ", fields=" + fields +
            ", outerMethodDesc='" + outerMethodDesc + '\'' +
            '}';
    }
}
