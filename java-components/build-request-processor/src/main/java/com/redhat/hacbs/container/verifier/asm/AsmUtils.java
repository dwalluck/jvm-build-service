package com.redhat.hacbs.container.verifier.asm;

import static org.objectweb.asm.Opcodes.ACC_BRIDGE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_SYNTHETIC;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.Type;
import org.objectweb.asm.signature.SignatureReader;

public final class AsmUtils {

    public static final String INIT = "<init>";

    private AsmUtils() {

    }

    private static int ordinal(int i) {
        return Integer.numberOfTrailingZeros(i);
    }

    public static <E extends Enum<E> & Access> Set<E> accessToSet(int access, Class<E> clazz) {
        return Arrays.stream(clazz.getEnumConstants()).filter(constant -> (access & (1 << ordinal(constant.getAccess()))) != 0)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(clazz)));
    }

    public static <E extends Enum<E> & Access> int setToAccess(Set<E> set) {
        return set.stream().mapToInt(constant -> 1 << ordinal(constant.getAccess())).reduce(0, (x, y) -> x | y);
    }

    public static <E extends Enum<E> & Access> String accessSetToString(Set<E> set) {
        var s = Objects.toString(set, "");

        if (s.length() >= 2) {
            return s.substring(1, s.length() - 1).replace(",", "");
        }

        return s;
    }

    public static boolean isPublic(int access) {
        return ((access & ACC_PUBLIC) != 0);
    }

    public static boolean isSyntheticBridge(int access) {
        var b = ACC_BRIDGE | ACC_SYNTHETIC;
        return ((access & b) == b);
    }

    public static String fixName(String name) {
        return !name.isEmpty() ? StringUtils.removeStart(name, "java/lang/").replace('/', '.') : "?";
    }

    public static String signatureToJavaCode(String name, String desc, String signature, List<String> exceptions) {
        if (signature == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        SignatureReader reader = new SignatureReader(signature);
        JavaCodeSignatureWriter writer = new JavaCodeSignatureWriter();
        reader.accept(writer);

        if (!signature.contains("(") && !signature.contains("<")) {
            System.out.println("desc: " + desc + ", signature: " + signature);
            SignatureReader reader1 = new SignatureReader(signature);
            JavaCodeSignatureWriter writer1 = new JavaCodeSignatureWriter();
            reader1.acceptType(writer1);
            sb.append(writer1);
            return sb.toString();
        } else {
            if (desc != null && !desc.contains("(")) {
                System.out.println("desc: " + desc);

                if (!sb.isEmpty()) {
                    sb.append(' ');
                }

                sb.append(fixName(Type.getType(desc).getClassName()));
            }
        }

        if (writer.hasFormals()) {
            sb.append(' ').append(writer.getFormals());
        }

        if (name != null && !isConstructor(name)) {
            if (writer.getReturnType() == null) {
                if (desc != null) {
                    sb.append(fixName(Type.getReturnType(desc).getClassName())).append(' ');
                }
            } else {
                String returnType = writer.getReturnType().toString();
                System.out.println("returnType: " + returnType);
                sb.append(returnType).append(' ');
            }
        }

        if (name != null) {
            if (isConstructor(name)) {
                sb.append('(');
            } else {
                sb.append(name).append('(');
            }
        }

        if (!writer.getParameters().isEmpty()) {
            String s = writer.getParameters().stream().map(JavaCodeSignatureWriter::toString).collect(Collectors.joining(", "));
            sb.append(s).append(')');
        } else {
            sb.append(')');
        }

        if (exceptions != null && !exceptions.isEmpty()) {
            sb.append(" throws ");
            String s = exceptions.stream().map(AsmUtils::fixName).collect(Collectors.joining(", "));
            sb.append(s);
        }

        System.out.println("Java code signature: " + sb);

        return sb.toString();
    }

    private static boolean isConstructor(String name) {
        return INIT.equals(name);
    }
}
