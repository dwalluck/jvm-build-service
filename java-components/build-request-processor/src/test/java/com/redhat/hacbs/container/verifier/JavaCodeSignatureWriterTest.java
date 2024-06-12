package com.redhat.hacbs.container.verifier;

import static com.redhat.hacbs.container.verifier.asm.AsmUtils.signatureToJavaCode;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

class JavaCodeSignatureWriterTest {
    @Test
    void testSignature() {
        String name = "func";
        String desc = null;
        List<String> exceptions = List.of("java.io.IOException");
        String signature = "(ILjava/lang/String;[I)J";
        String expectedExceptions = " throws " + String.join(", ", exceptions);
        String expected = "long " + name + "(int, String, int[])" + expectedExceptions;
        assertThat(signatureToJavaCode(name, desc, signature, exceptions)).isEqualTo(expected);
    }

    @Test
    void testSignature2() {
        assertThat(signatureToJavaCode(null, null, "Ljava/lang/Object;", null)).isEqualTo("Object");
    }
}
