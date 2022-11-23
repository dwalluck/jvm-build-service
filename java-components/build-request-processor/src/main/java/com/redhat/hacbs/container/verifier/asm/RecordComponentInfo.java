package com.redhat.hacbs.container.verifier.asm;

import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.objectweb.asm.tree.RecordComponentNode;

public class RecordComponentInfo implements Diffable<RecordComponentInfo> {
    public RecordComponentInfo(RecordComponentNode node) {
//        node.attrs
//            node.descriptor
//                node.invisibleAnnotations
//                    node.invisibleTypeAnnotations
//                        node.name
//                            node.signature
    }

    /**
     * <p>Retrieves a list of the differences between
     * this object and the supplied object.</p>
     *
     * @param obj the object to diff against, can be {@code null}
     * @return a list of differences
     * @throws NullPointerException if the specified object is {@code null}
     */
    @Override
    public DiffResult<RecordComponentInfo> diff(RecordComponentInfo obj) {
        return null;
    }
}
