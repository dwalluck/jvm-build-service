package com.redhat.hacbs.container.verifier.asm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.objectweb.asm.tree.LocalVariableAnnotationNode;

/**
 * Local variable annotation info.
 */
public class LocalVariableAnnotationInfo implements Diffable<LocalVariableAnnotationInfo> {
    private final List<LabelInfo> end;

    private final List<Integer> index;

    private final List<LabelInfo> start;

    /**
     * Creates a new local variable annotation info from the given local variable annotation node.
     *
     * @param node the local variable annotation node
     */
    public LocalVariableAnnotationInfo(LocalVariableAnnotationNode node) {
        this.end = new ArrayList<>(node.end.size());
        node.end.stream().map(LabelInfo::new).forEach(this.end::add);
        this.index = node.index;
        this.start = new ArrayList<>(node.start.size());
        node.start.stream().map(LabelInfo::new).forEach(this.start::add);
    }

    /**
     * Gets the end.
     *
     * @return the end
     */
    public List<LabelInfo> getEnd() {
        return Collections.unmodifiableList(end);
    }

    /**
     * Gets the index.
     *
     * @return the index
     */
    public List<Integer> getIndex() {
        return Collections.unmodifiableList(index);
    }

    /**
     * Gets the start.
     *
     * @return the start
     */
    public List<LabelInfo> getStart() {
        return Collections.unmodifiableList(start);
    }

    @Override
    public DiffResult<LocalVariableAnnotationInfo> diff(LocalVariableAnnotationInfo obj) {
        return new DiffBuilder<>(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("end", this.end, obj.end)
                .append("index", this.index, obj.index)
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

        var that = (LocalVariableAnnotationInfo) o;
        return end.equals(that.end) && index.equals(that.index) && start.equals(that.start);
    }

    @Override
    public int hashCode() {
        return Objects.hash(end, index, start);
    }

    @Override
    public String toString() {
        return "LocalVariableAnnotationInfo{" +
                "end=" + end +
                ", index=" + index +
                ", start=" + start +
                '}';
    }
}
