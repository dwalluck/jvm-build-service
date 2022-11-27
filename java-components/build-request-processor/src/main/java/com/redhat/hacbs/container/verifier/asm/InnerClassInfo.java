package com.redhat.hacbs.container.verifier.asm;

import java.util.Objects;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.objectweb.asm.tree.InnerClassNode;

/**
 * Inner class info.
 */
public class InnerClassInfo implements Diffable<InnerClassInfo> {
    private final int access;

    private final String innerName;

    private final String name;

    private final String outerName;

    /**
     * Creates a class info from the given class node.
     *
     * @param node the class node
     */
    public InnerClassInfo(InnerClassNode node) {
        this.access = node.access;
        this.innerName = node.innerName;
        this.name = node.name;
        this.outerName = node.outerName;
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
     * Gets the inner name.
     *
     * @return the inner name
     */
    public String getInnerName() {
        return innerName;
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
     * Gets the outer name.
     *
     * @return the outer name
     */
    public String getOuterName() {
        return outerName;
    }

    @Override
    public DiffResult<InnerClassInfo> diff(InnerClassInfo obj) {
        return new DiffBuilder<>(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("access", this.access, obj.access)
                .append("innerName", this.innerName, obj.innerName)
                .append("name", this.name, obj.name)
                .append("outerName", this.outerName, obj.outerName)
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

        var that = (InnerClassInfo) o;
        return access == that.access && Objects.equals(innerName, that.innerName) && name.equals(that.name)
                && Objects.equals(outerName, that.outerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(access, innerName, name, outerName);
    }

    @Override
    public String toString() {
        return "InnerClassInfo{" +
                "access=" + access +
                ", innerName='" + innerName + '\'' +
                ", name='" + name + '\'' +
                ", outerName='" + outerName + '\'' +
                '}';
    }
}
