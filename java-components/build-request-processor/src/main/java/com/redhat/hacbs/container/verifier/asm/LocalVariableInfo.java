package com.redhat.hacbs.container.verifier.asm;

import java.util.Objects;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.objectweb.asm.tree.LocalVariableNode;

/**
 * Local variable info.
 */
public class LocalVariableInfo implements Diffable<LocalVariableInfo> {
    private final String desc;

    private final LabelInfo end;

    private final int index;

    private final String name;

    private final String signature;

    private final LabelInfo start;

    /**
     * Creates a new local variable info from the given local variable node.
     *
     * @param node the local variable node
     */
    public LocalVariableInfo(LocalVariableNode node) {
        this.desc = node.desc;
        this.end = new LabelInfo(node.end);
        this.index = node.index;
        this.name = node.name;
        this.signature = node.signature;
        this.start = new LabelInfo(node.start);
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
     * Gets the end.
     *
     * @return the end
     */
    public LabelInfo getEnd() {
        return end;
    }

    /**
     * Gets the index.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
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
     * Gets the start.
     *
     * @return the start
     */
    public LabelInfo getStart() {
        return start;
    }

    @Override
    public DiffResult<LocalVariableInfo> diff(LocalVariableInfo obj) {
        return new DiffBuilder<>(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("desc", this.desc, obj.desc)
                .append("end", this.end, obj.end)
                .append("index", this.index, obj.index)
                .append("name", this.name, obj.name)
                .append("signature", this.signature, obj.signature)
                .append("start", this.start, obj.start)
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

        var that = (LocalVariableInfo) o;
        return index == that.index && desc.equals(that.desc) && end.equals(that.end) && name.equals(that.name)
                && Objects.equals(signature, that.signature) && start.equals(that.start);
    }

    @Override
    public int hashCode() {
        return Objects.hash(desc, end, index, name, signature, start);
    }

    @Override
    public String toString() {
        return "LocalVariableInfo{" +
                "desc='" + desc + '\'' +
                ", end=" + end +
                ", index=" + index +
                ", name='" + name + '\'' +
                ", signature='" + signature + '\'' +
                ", start=" + start +
                '}';
    }
}
