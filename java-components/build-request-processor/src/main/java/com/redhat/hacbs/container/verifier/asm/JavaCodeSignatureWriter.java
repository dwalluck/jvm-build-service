package com.redhat.hacbs.container.verifier.asm;

import static com.redhat.hacbs.container.verifier.asm.AsmUtils.fixName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.signature.SignatureWriter;

public class JavaCodeSignatureWriter extends SignatureWriter {
    private final StringBuilder stringBuilder;

    private final StringBuilder formalsStringBuilder = new StringBuilder();

    private final List<JavaCodeSignatureWriter> parameters = new ArrayList<>();

    private boolean hasFormals;

    private boolean hasArray;

    private int argumentStack = 1;

    private JavaCodeSignatureWriter returnType;

    private JavaCodeSignatureWriter superClass;

    public JavaCodeSignatureWriter() {
        this(new StringBuilder());
    }

    private JavaCodeSignatureWriter(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    @Override
    public void visitFormalTypeParameter(String name) {
        if (!hasFormals) {
            hasFormals = true;
            formalsStringBuilder.append('<');
        } else {
            formalsStringBuilder.append(", ");
        }

        formalsStringBuilder.append(name);
        formalsStringBuilder.append(" extends ");
    }

    @Override
    public SignatureVisitor visitInterfaceBound() {
        return this;
    }

    @Override
    public SignatureVisitor visitSuperclass() {
        endFormals();
        return (superClass = new JavaCodeSignatureWriter());
    }

    @Override
    public SignatureVisitor visitParameterType() {
        endFormals();
        JavaCodeSignatureWriter writer = new JavaCodeSignatureWriter();
        parameters.add(writer);
        return writer;
    }

    @Override
    public SignatureVisitor visitReturnType() {
        endFormals();
        return (returnType = new JavaCodeSignatureWriter());
    }

    @Override
    public SignatureVisitor visitExceptionType() {
        return this;
    }

    @Override
    public void visitBaseType(char descriptor) {
        String typeDescriptor = String.valueOf(descriptor);
        Type type = Type.getType(typeDescriptor);
        String className = type.getClassName();
        (hasFormals ? formalsStringBuilder : stringBuilder).append(fixName(className));

        if (hasArray) {
            hasArray = false;
            (hasFormals ? formalsStringBuilder : stringBuilder).append("[]");
        }
    }

    @Override
    public void visitTypeVariable(String name) {
        (hasFormals ? formalsStringBuilder : stringBuilder).append(fixName(name));
    }

    @Override
    public SignatureVisitor visitArrayType() {
        hasArray = true;
        return this;
    }

    @Override
    public void visitClassType(String name) {
        (hasFormals ? formalsStringBuilder : stringBuilder).append(fixName(name));
        argumentStack <<= 1;
    }

    @Override
    public void visitInnerClassType(String name) {
        endArguments();
        (hasFormals ? formalsStringBuilder : stringBuilder).append('.').append(fixName(name));
        argumentStack <<= 1;
    }

    @Override
    public void visitTypeArgument() {
        if ((argumentStack & 1) == 0) {
            argumentStack |= 1;
            (hasFormals ? formalsStringBuilder : stringBuilder).append('<');
        } else {
            (hasFormals ? formalsStringBuilder : stringBuilder).append(", ");
        }
    }

    @Override
    public SignatureVisitor visitTypeArgument(char wildcard) {
        visitTypeArgument();

        if (wildcard == '+') {
            (hasFormals ? formalsStringBuilder : stringBuilder).append("? extends ");
        } else if (wildcard == '-') {
            (hasFormals ? formalsStringBuilder : stringBuilder).append("? super ");
        } else if (wildcard != '=') {
            (hasFormals ? formalsStringBuilder : stringBuilder).append(wildcard);
        }

       return (argumentStack & (1 << 31)) == 0 ? this : new JavaCodeSignatureWriter(stringBuilder);
    }

    @Override
    public void visitEnd() {
        endArguments();
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

    private void endFormals() {
        if (hasFormals) {
            hasFormals = false;
            stringBuilder.append('>');
        }
    }

    private void endArguments() {
        if ((argumentStack & 1) == 1) {
            (hasFormals ? formalsStringBuilder : stringBuilder).append('>');
        }

        argumentStack >>>= 1;
    }

    public JavaCodeSignatureWriter getReturnType() {
        return returnType;
    }

    public List<JavaCodeSignatureWriter> getParameters() {
        return Collections.unmodifiableList(parameters);
    }

    public boolean hasFormals() {
        return !formalsStringBuilder.isEmpty();
    }

    public String getFormals() {
        return formalsStringBuilder.toString();
    }

    public JavaCodeSignatureWriter getSuperClass() {
        return superClass;
    }
}
