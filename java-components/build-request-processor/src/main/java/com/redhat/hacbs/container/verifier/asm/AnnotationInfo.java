package com.redhat.hacbs.container.verifier.asm;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.objectweb.asm.tree.AnnotationNode;

/**
 * Annotation info.
 */
public class AnnotationInfo implements Diffable<AnnotationInfo> {
    private final String desc;

    private final List<Object> values;

    /**
     * Creates a new annotation info from the given annotation node.
     *
     * @param node the annotation node
     */
    public AnnotationInfo(AnnotationNode node) {
        this.desc = node.desc;
        this.values = node.values;
    }

    /**
     * Gets the class descriptor of the annotation class.
     *
     * @return the class descriptor
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Gets the name value pairs of this annotation.
     *
     * @return the name value pairs
     */
    public List<Object> getValues() {
        return Collections.unmodifiableList(values);
    }

    @Override
    public DiffResult<AnnotationInfo> diff(AnnotationInfo obj) {
        return new DiffBuilder<>(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("desc", this.desc, obj.desc)
                .append("values", this.values, obj.values)
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

        var that = (AnnotationInfo) o;
        return desc.equals(that.desc) && Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(desc, values);
    }

    @Override
    public String toString() {
        return "AnnotationInfo{" +
                "desc='" + desc + '\'' +
                ", values=" + values +
                '}';
    }
}
