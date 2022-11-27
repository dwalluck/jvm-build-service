package com.redhat.hacbs.container.verifier.asm;

import java.util.Objects;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.objectweb.asm.Attribute;

/**
 * Attribute info.
 */
public class AttributeInfo implements Diffable<AttributeInfo> {
    private final String type;

    /**
     * Creates a new attribute info from the given attribute.
     *
     * @param attribute the attribute
     */
    public AttributeInfo(Attribute attribute) {
        // XXX: Not sure if we can access content
        this.type = attribute.type;
    }

    /**
     * Gets the type of this attribute.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    @Override
    public DiffResult<AttributeInfo> diff(AttributeInfo obj) {
        return new DiffBuilder<>(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
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

        var that = (AttributeInfo) o;
        return type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }

    @Override
    public String toString() {
        return "AttributeInfo{" +
                "type='" + type + '\'' +
                '}';
    }
}
