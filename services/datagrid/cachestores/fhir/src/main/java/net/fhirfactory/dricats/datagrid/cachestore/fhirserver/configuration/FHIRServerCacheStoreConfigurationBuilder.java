/*
 * Copyright (c) 2022 Mark A. Hunter (ACT Health)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.fhirfactory.dricats.datagrid.cachestore.fhirserver.configuration;

import net.fhirfactory.dricats.datagrid.cachestore.fhirserver.common.datatypes.ResourceTypeSet;
import org.infinispan.commons.configuration.ConfigurationBuilderInfo;
import org.infinispan.commons.configuration.attributes.AttributeSet;
import org.infinispan.commons.configuration.elements.ElementDefinition;
import org.infinispan.configuration.cache.AbstractStoreConfigurationBuilder;
import org.infinispan.configuration.cache.PersistenceConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FHIRServerCacheStoreConfigurationBuilder extends AbstractStoreConfigurationBuilder<FHIRServerCacheStoreConfiguration, FHIRServerCacheStoreConfigurationBuilder> implements ConfigurationBuilderInfo {
    private static final Logger LOG = LoggerFactory.getLogger(FHIRServerCacheStoreConfigurationBuilder.class);

    //
    // Constructor
    //

    public FHIRServerCacheStoreConfigurationBuilder(PersistenceConfigurationBuilder builder) {
        super(builder, FHIRServerCacheStoreConfiguration.attributeDefinitionSet());
    }

    //
    // Getters and Setters
    //

    protected Logger getLogger() {
        return (LOG);
    }

    @Override
    public AttributeSet attributes() {
        return attributes;
    }

    @Override
    public ElementDefinition getElementDefinition() {
        return (FHIRServerCacheStoreConfiguration.ELEMENT_DEFINITION);
    }

    //
    // Business Methods
    //

    @Override
    public FHIRServerCacheStoreConfiguration create() {
        return null;
    }

    @Override
    public FHIRServerCacheStoreConfigurationBuilder self() {
        return this;
    }

    public FHIRServerCacheStoreConfigurationBuilder host(String host) {
        this.attributes.attribute(FHIRServerCacheStoreConfiguration.HOST).set(host);
        return this;
    }

    public FHIRServerCacheStoreConfigurationBuilder port(int port) {
        this.attributes.attribute(FHIRServerCacheStoreConfiguration.PORT).set(port);
        return this;
    }

    public FHIRServerCacheStoreConfigurationBuilder contextPath(String path) {
        this.attributes.attribute(FHIRServerCacheStoreConfiguration.CONTEXT_PATH).set(path);
        return this;
    }

    public FHIRServerCacheStoreConfigurationBuilder persistedResourceSet(ResourceTypeSet resourceSet) {
        this.attributes.attribute(FHIRServerCacheStoreConfiguration.PERSISTED_RESOURCE_SET).set(resourceSet);
        return this;
    }

    public FHIRServerCacheStoreConfigurationBuilder requiresEncryption(Boolean encrypted) {
        this.attributes.attribute(FHIRServerCacheStoreConfiguration.REQUIRES_ENCRYPTION).set(encrypted);
        return this;
    }
}
