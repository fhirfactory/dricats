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
package net.fhirfactory.dricats.datagrid.cachestore.fhirserver;

import net.fhirfactory.dricats.datagrid.cachestore.fhirserver.client.base.BaseFHIRClientService;
import net.fhirfactory.dricats.datagrid.cachestore.fhirserver.configuration.FHIRServerCacheStoreConfiguration;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.infinispan.commons.configuration.ConfiguredBy;
import org.infinispan.commons.io.ByteBufferFactory;
import org.infinispan.commons.marshall.Marshaller;
import org.infinispan.commons.util.IntSet;
import org.infinispan.persistence.spi.InitializationContext;
import org.infinispan.persistence.spi.MarshallableEntry;
import org.infinispan.persistence.spi.MarshallableEntryFactory;
import org.infinispan.persistence.spi.NonBlockingStore;
import org.infinispan.util.concurrent.BlockingManager;
import org.infinispan.util.concurrent.CompletableFutures;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@ConfiguredBy(FHIRServerCacheStoreConfiguration.class)
public class FHIRServerCacheStore<K, V extends Resource> extends Object implements NonBlockingStore<K, V> {
    private static final Logger LOG = LoggerFactory.getLogger(FHIRServerCacheStore.class);
    protected InitializationContext ctx;
    private FHIRServerCacheStoreConfiguration configuration;
    private Marshaller marshaller;
    private ByteBufferFactory byteBufferFactory;
    private MarshallableEntryFactory<K, V> marshallableEntryFactory;
    private BlockingManager blockingManager;

    private ConcurrentHashMap<ResourceType, BaseFHIRClientService> fhirClients;

    //
    // Constructor
    //
    private Supplier<Boolean> initialiseClients = () -> {

        if (configuration == null) {
            getLogger().error(".initialiseClients(): Error, no FHIRServerClientConfiguration available!");
            throw (new RuntimeException("Error, no FHIRServerClientConfiguration available!"));
        }

        return (true);
    };

    //
    // Getters (and Setters)
    //

    public FHIRServerCacheStore() {
        this.fhirClients = new ConcurrentHashMap<>();
    }

    protected Logger getLogger() {
        return (LOG);
    }

    protected Marshaller getMarshaller() {
        return (marshaller);
    }

    protected ByteBufferFactory getByteBufferFactory() {
        return (this.byteBufferFactory);
    }

    protected MarshallableEntryFactory<K, V> getMarshallableEntryFactory() {
        return (this.marshallableEntryFactory);
    }

    protected BlockingManager getBlockingManager() {
        return (this.blockingManager);
    }

    protected InitializationContext getInitContext() {
        return (this.ctx);
    }

    protected FHIRServerCacheStoreConfiguration getConfiguration() {
        return (this.configuration);
    }

    //
    // Business Logic :: Initialisation Activities
    //

    protected void setConfiguration(FHIRServerCacheStoreConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public CompletionStage<Void> start(InitializationContext ctx) {
        this.ctx = ctx;
        this.configuration = getInitContext().getConfiguration();
        this.marshallableEntryFactory = getInitContext().getMarshallableEntryFactory();
        this.blockingManager = getInitContext().getBlockingManager();
        this.marshaller = getInitContext().getPersistenceMarshaller();

        CompletionStage<Boolean> clientInitialisationOutcome = getBlockingManager().supplyBlocking(initialiseClients, "initialiseClients");
        return null;
    }

    //
    // Business Logic :: Finalisation Activities
    //

    @Override
    public CompletionStage<Void> stop() {
        return null;
    }

    //
    // Business Logic :: Entry Load
    //

    @Override
    public CompletionStage<MarshallableEntry<K, V>> load(int segment, Object key) {
        return null;
    }

    //
    // Business Logic :: Entry Save
    //

    @Override
    public CompletionStage<Void> write(int segment, MarshallableEntry<? extends K, ? extends V> entry) {
        return null;
    }

    //
    // Business Logic :: Entry Delete
    //

    @Override
    public CompletionStage<Boolean> delete(int segment, Object key) {
        return null;
    }

    //
    // Business Logic :: Clear
    //

    @Override
    public CompletionStage<Void> clear() {
        return null;
    }

    //
    // Business Logic :: Store Capability Definition
    //

    @Override
    public Set<Characteristic> characteristics() {
        return EnumSet.of(Characteristic.BULK_READ, Characteristic.SEGMENTABLE, Characteristic.EXPIRATION);
    }

    //
    // Business Logic :: Segment Management
    //

    @Override
    public CompletionStage<Void> addSegments(IntSet segments) {
        // TODO add segmentation service(s)
        return CompletableFutures.completedNull();
    }

    @Override
    public CompletionStage<Void> removeSegments(IntSet segments) {
        // TODO add segmentation service(s)
        return CompletableFutures.completedNull();
    }

    //
    // Business Logic :: Operational Capability Reporting
    //

    @Override
    public CompletionStage<Boolean> isAvailable() {
        // TODO: does this block?
        return CompletableFuture.completedFuture(true);
    }
}
