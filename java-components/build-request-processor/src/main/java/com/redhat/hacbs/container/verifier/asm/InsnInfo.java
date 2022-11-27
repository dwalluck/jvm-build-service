package com.redhat.hacbs.container.verifier.asm;

import java.util.Objects;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.objectweb.asm.tree.AbstractInsnNode;

/**
 * Bytecode instruction info.
 */
public class InsnInfo implements Diffable<InsnInfo> {
    private final int opcode;

    private final int type;

    /**
     * Creates a new bytecode instruction info from the given bytecode instruction node.
     *
     * @param node the bytecode instruction node
     */
    public InsnInfo(AbstractInsnNode node) {
        this.opcode = node.getOpcode();
        this.type = node.getType();
    }

    /**
     * Gets the opcode.
     *
     * @return the opcode
     */
    public int getOpcode() {
        return opcode;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public int getType() {
        return type;
    }

    @Override
    public DiffResult<InsnInfo> diff(InsnInfo obj) {
        return new DiffBuilder<>(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("opcode", this.opcode, obj.opcode)
                .append("type", this.type, obj.type)
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

        var insnInfo = (InsnInfo) o;
        return opcode == insnInfo.opcode && type == insnInfo.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(opcode, type);
    }

    @Override
    public String toString() {
        return "InsnInfo{" +
                "opcode=" + opcode +
                ", type=" + type +
                '}';
    }
}
