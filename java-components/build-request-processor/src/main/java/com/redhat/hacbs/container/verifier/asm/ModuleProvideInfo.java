package com.redhat.hacbs.container.verifier.asm;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.objectweb.asm.tree.ModuleProvideNode;

/**
 * Module provide info.
 */
public class ModuleProvideInfo implements Diffable<ModuleProvideInfo> {
    private final List<String> providers;

    private final String service;

    /**
     * Creates a new module provide info from the given module provide node.
     *
     * @param node the module provide node
     */
    public ModuleProvideInfo(ModuleProvideNode node) {
        this.providers = node.providers;
        this.service = node.service;
    }

    /**
     * Gets the providers.
     *
     * @return the providers
     */
    public List<String> getProviders() {
        return Collections.unmodifiableList(providers);
    }

    /**
     * Gets the service.
     *
     * @return the service
     */
    public String getService() {
        return service;
    }

    @Override
    public DiffResult<ModuleProvideInfo> diff(ModuleProvideInfo obj) {
        return new DiffBuilder<>(this, obj, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("providers", this.providers, obj.providers)
                .append("service", this.service, obj.service)
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

        var that = (ModuleProvideInfo) o;
        return providers.equals(that.providers) && service.equals(that.service);
    }

    @Override
    public int hashCode() {
        return Objects.hash(providers, service);
    }

    @Override
    public String toString() {
        return "ModuleProvideInfo{" +
                "providers=" + providers +
                ", service='" + service + '\'' +
                '}';
    }
}
