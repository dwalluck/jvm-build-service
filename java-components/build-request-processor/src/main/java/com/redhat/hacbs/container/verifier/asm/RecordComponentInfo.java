package com.redhat.hacbs.container.verifier.asm;

import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.objectweb.asm.tree.RecordComponentNode;

public class RecordComponentInfo implements Diffable<RecordComponentInfo> {
    // TODO
    public RecordComponentInfo(RecordComponentNode node) {
        //        node.attrs
        //            node.descriptor
        //                node.invisibleAnnotations
        //                    node.invisibleTypeAnnotations
        //                        node.name
        //                            node.signature
    }

    @Override
    public DiffResult<RecordComponentInfo> diff(RecordComponentInfo obj) {
        return null;
    }
}
