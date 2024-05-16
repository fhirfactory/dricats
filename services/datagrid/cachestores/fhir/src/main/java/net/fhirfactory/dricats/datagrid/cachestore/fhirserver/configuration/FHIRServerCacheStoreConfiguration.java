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

import net.fhirfactory.dricats.datagrid.cachestore.fhirserver.FHIRServerCacheStore;
import net.fhirfactory.dricats.datagrid.cachestore.fhirserver.common.datatypes.ResourceTypeSet;
import net.fhirfactory.dricats.datagrid.cachestore.fhirserver.common.valuesets.ConfigurationAttributeEnum;
import org.infinispan.commons.configuration.BuiltBy;
import org.infinispan.commons.configuration.ConfigurationFor;
import org.infinispan.commons.configuration.ConfigurationInfo;
import org.infinispan.commons.configuration.attributes.AttributeDefinition;
import org.infinispan.commons.configuration.attributes.AttributeSet;
import org.infinispan.commons.configuration.elements.DefaultElementDefinition;
import org.infinispan.commons.configuration.elements.ElementDefinition;
import org.infinispan.configuration.cache.AbstractStoreConfiguration;
import org.infinispan.configuration.cache.AsyncStoreConfiguration;

import java.util.ArrayList;
import java.util.List;

import static net.fhirfactory.dricats.datagrid.cachestore.fhirserver.common.valuesets.ConfigurationElementEnum.FHIR_SERVER_CACHE_STORE;

@ConfigurationFor(FHIRServerCacheStore.class)
@BuiltBy(FHIRServerCacheStoreConfigurationBuilder.class)
public class FHIRServerCacheStoreConfiguration extends AbstractStoreConfiguration implements ConfigurationInfo {

    public static final AttributeDefinition<String> HOST = AttributeDefinition.builder(ConfigurationAttributeEnum.HOST.getAttributeName(), null, String.class).immutable().build();
    public static final AttributeDefinition<Integer> PORT = AttributeDefinition.builder(ConfigurationAttributeEnum.PORT.getAttributeName(), 8080, Integer.class).immutable().build();
    public static final AttributeDefinition<Integer> PORT_TIMEOUT = AttributeDefinition.builder(ConfigurationAttributeEnum.PORT_TIMEOUT.getAttributeName(), 30, Integer.class).immutable().build();
    public static final AttributeDefinition<String> CONTEXT_PATH = AttributeDefinition.builder(ConfigurationAttributeEnum.CONTEXT_PATH.getAttributeName(), "/", String.class).immutable().build();
    public static final AttributeDefinition<ResourceTypeSet> PERSISTED_RESOURCE_SET = AttributeDefinition.builder(ConfigurationAttributeEnum.PERSISTED_RESOURCE_SET.getAttributeName(), null, ResourceTypeSet.class).immutable().build();
    public static final AttributeDefinition<Boolean> REQUIRES_ENCRYPTION = AttributeDefinition.<Boolean>builder(ConfigurationAttributeEnum.REQUIRES_ENCRYPTION.getAttributeName(), false, Boolean.class).immutable().build();
    public static final ElementDefinition ELEMENT_DEFINITION = new DefaultElementDefinition(FHIR_SERVER_CACHE_STORE.getElementName(), true, false);
    private final List<ConfigurationInfo> subElements;

    //
    // Constructor
    //

    public FHIRServerCacheStoreConfiguration(AttributeSet attributes, AsyncStoreConfiguration async) {
        super(attributes, async);
        this.subElements = new ArrayList<>(super.subElements());
    }

    //
    // Overrides
    //

    public static AttributeSet attributeDefinitionSet() {
        return new AttributeSet(FHIRServerCacheStoreConfiguration.class, AbstractStoreConfiguration.attributeDefinitionSet(), HOST, PORT, PORT_TIMEOUT, CONTEXT_PATH, PERSISTED_RESOURCE_SET, REQUIRES_ENCRYPTION);
    }

    //
    // Business Methods
    //

    @Override
    public ElementDefinition getElementDefinition() {
        return (ELEMENT_DEFINITION);
    }

    public AttributeSet getAttributeDefinitionSet() {
        return (attributes);
    }

    public AttributeDefinition<String> getHostAttributeDefinition() {
        return (HOST);
    }

    public AttributeDefinition<Integer> getPortAttributeDefinition() {
        return (PORT);
    }

    public AttributeDefinition<Integer> getPortTimeoutAttributeDefinition() {
        return (PORT_TIMEOUT);
    }

    public AttributeDefinition<String> getContextPathAttributeDefinition() {
        return (CONTEXT_PATH);
    }

    public AttributeDefinition<ResourceTypeSet> getPersistedResourceSetAttributeDefinition() {
        return (PERSISTED_RESOURCE_SET);
    }

    public AttributeDefinition<Boolean> getRequiresEncryptionAttributeDefinition() {
        return (REQUIRES_ENCRYPTION);
    }


    public AttributeSet attributes() {
        return attributes;
    }

    public String host() {
        return attributes.attribute(HOST).get();
    }

    public int port() {
        return attributes.attribute(PORT).get();
    }

    public int portTimeout() {
        return (attributes.attribute(PORT_TIMEOUT).get());
    }

    public String contextPath() {
        return attributes.attribute(CONTEXT_PATH).get();
    }

    public ResourceTypeSet persistedResources() {
        return attributes.attribute(PERSISTED_RESOURCE_SET).get();
    }

    public Boolean requiresEncryption() {
        return (attributes.attribute(REQUIRES_ENCRYPTION).get());
    }

    //
    // toString
    //


}
