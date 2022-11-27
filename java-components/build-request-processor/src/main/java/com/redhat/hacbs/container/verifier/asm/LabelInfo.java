package com.redhat.hacbs.container.verifier.asm;

import java.util.Objects;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.objectweb.asm.tree.LabelNode;

/**
 * Label info.
 */
public class LabelInfo implements Diffable<LabelInfo> {
    private final String value;

    /**
     * Creates a new label info from the given label node.
     *
     * @param node the label node
     */
    public LabelInfo(LabelNode node) {
        this.value = node.getLabel().toString();
    }

    /**
     * Gets the value of this label.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    @Override
    public DiffResult<LabelInfo> diff(LabelInfo obj) {
        return new DiffBuilder<>(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("value", this.value, obj.value)
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

        var that = (LabelInfo) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "LabelInfo{" +
                "value='" + value + '\'' +
                '}';
    }
}
