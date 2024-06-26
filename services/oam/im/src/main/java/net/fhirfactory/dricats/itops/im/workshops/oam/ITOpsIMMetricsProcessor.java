/*
 * Copyright (c) 2022 Mark A. Hunter
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
package net.fhirfactory.dricats.itops.im.workshops.oam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.dricats.core.interfaces.topology.ProcessingPlantInterface;
import net.fhirfactory.dricats.core.model.componentid.ComponentIdType;
import net.fhirfactory.dricats.core.model.oam.metrics.component.EndpointMetricsData;
import net.fhirfactory.dricats.core.model.oam.metrics.component.ProcessingPlantMetricsData;
import net.fhirfactory.dricats.core.model.oam.metrics.component.WorkUnitProcessorMetricsData;
import net.fhirfactory.dricats.core.model.oam.metrics.component.common.CommonComponentMetricsData;
import net.fhirfactory.dricats.core.model.oam.metrics.reporting.PetasosComponentMetricSet;
import net.fhirfactory.dricats.core.model.oam.metrics.reporting.factories.PetasosComponentMetricSetFactory;
import net.fhirfactory.dricats.core.oam.metrics.cache.PetasosLocalMetricsDM;
import net.fhirfactory.dricats.internals.SerializableObject;
import net.fhirfactory.dricats.itops.im.workshops.datagrid.ITOpsSystemWideMetricsDM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class ITOpsIMMetricsProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(ITOpsIMMetricsProcessor.class);

    private ConcurrentHashMap<ComponentIdType, PetasosComponentMetricSet> metricsQueue;
    private ConcurrentHashMap<ComponentIdType, PetasosComponentMetricSet> publishedMetricQueue;

    private SerializableObject metricQueueLock = new SerializableObject();

    private ObjectMapper jsonMapper;
    private boolean initialised;

    @Inject
    private ProcessingPlantInterface processingPlant;

    @Inject
    private PetasosLocalMetricsDM metricsDM;

    @Inject
    private PetasosComponentMetricSetFactory componentMetricSetFactory;

    @Inject
    private ITOpsSystemWideMetricsDM systemWideMetricsDM;

    //
    // Constructor(s)
    //

    public ITOpsIMMetricsProcessor() {
        this.initialised = false;
        this.metricsQueue = new ConcurrentHashMap<>();
        this.publishedMetricQueue = new ConcurrentHashMap<>();
        this.jsonMapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        this.jsonMapper.registerModule(module);
        this.jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    //
    // Post Construct
    //

    //
    // Actual Daemon Tasks
    //

    public void captureLocalMetrics() {
        getLogger().debug(".captureLocalMetrics(): Entry");
        //
        // Get Metrics from the Cache
        List<CommonComponentMetricsData> allLocalMetricsSets = getMetricsDM().getAllMetricsSets();
        getLogger().trace(".captureLocalMetrics(): Iterating through Metrics retrieved from local cache");
        getLogger().trace(".captureLocalMetrics(): Iterating through Metrics retrieved from local cache, set size -> {}", allLocalMetricsSets.size());
        for (CommonComponentMetricsData currentMetrics : allLocalMetricsSets) {
            PetasosComponentMetricSet metricSet = null;
            ComponentIdType componentId = currentMetrics.getComponentID();
            getLogger().trace(".captureLocalMetrics(): Iterating through Metrics (Processing->{})", componentId);
            if (currentMetrics instanceof ProcessingPlantMetricsData) {
                getLogger().trace(".captureLocalMetrics(): Iterating through Metrics (Is a ProcessingPlant)");
                ProcessingPlantMetricsData plantMetricsData = (ProcessingPlantMetricsData) currentMetrics;
                metricSet = getComponentMetricSetFactory().convertProcessingPlantMetricsData(plantMetricsData);
            }
            if (currentMetrics instanceof WorkUnitProcessorMetricsData) {
                getLogger().trace(".captureLocalMetrics(): Iterating through Metrics (Is a WorkUnitProcessor)");
                WorkUnitProcessorMetricsData wupMetricsData = (WorkUnitProcessorMetricsData) currentMetrics;
                metricSet = getComponentMetricSetFactory().convertWorkUnitProcessorMetricsData(wupMetricsData);
            }
            if (currentMetrics instanceof EndpointMetricsData) {
                getLogger().trace(".captureLocalMetrics(): Iterating through Metrics (Is a WorkUnitProcessor)");
                EndpointMetricsData endpointMetricsData = (EndpointMetricsData) currentMetrics;
                metricSet = getComponentMetricSetFactory().convertEndpointMetricsData(endpointMetricsData);
            }
            if (metricSet != null) {
                synchronized (getMetricQueueLock()) {
                    if (getMetricsQueue().containsKey(componentId)) {
                        getMetricsQueue().remove(componentId);
                    }
                    getMetricsQueue().put(componentId, metricSet);
                }
            }
        }
        getLogger().debug(".captureLocalMetrics(): Exit");
    }

    public void forwardLocalMetricsToServer() {
        LOG.debug(".forwardLocalMetricsToServer(): Entry");


        LOG.trace(".forwardLocalMetricsToServer(): Number of MetricSets to processing->{}", metricsQueue.size());

        List<PetasosComponentMetricSet> updatedMetrics = new ArrayList<>();
        synchronized (metricQueueLock) {
            Enumeration<ComponentIdType> keys = metricsQueue.keys();
            while (keys.hasMoreElements()) {
                ComponentIdType currentComponentId = keys.nextElement();
                PetasosComponentMetricSet metricSetInQueue = metricsQueue.get(currentComponentId);
                PetasosComponentMetricSet publishedMetricSet = publishedMetricQueue.get(currentComponentId);
                boolean publishCurrentMetricSet = false;
                if (publishedMetricSet == null) {
                    publishCurrentMetricSet = true;
                } else {
                    if (!publishedMetricSet.businessEquals(metricSetInQueue)) {
                        publishCurrentMetricSet = true;
                    }
                }

                if (publishCurrentMetricSet) {
                    updatedMetrics.add(metricSetInQueue);
                    if (publishedMetricQueue.contains(currentComponentId)) {
                        publishedMetricQueue.remove(currentComponentId);
                    }
                    publishedMetricQueue.put(currentComponentId, metricSetInQueue);
                }
            }

            LOG.trace(".forwardLocalMetricsToServer(): new unique metrics to send to server-> {}", updatedMetrics.size());
        }

        LOG.trace(".forwardLocalMetricsToServer(): Loaded metrics form local cache, forwarding");
        boolean metricsUpdateFailed = false;
        for (PetasosComponentMetricSet currentMetric : updatedMetrics) {
            LOG.debug(".forwardLocalMetricsToServer(): Sending metrics for component->{}", currentMetric.getMetricSourceComponentId());
            systemWideMetricsDM.addComponentMetricSet(processingPlant.getSubsystemParticipantName(), currentMetric);
            synchronized (metricQueueLock) {
                if (!metricsQueue.containsKey(currentMetric.getMetricSourceComponentId())) {
                    metricsQueue.put(currentMetric.getMetricSourceComponentId(), currentMetric);
                }
            }
        }
        if (metricsUpdateFailed) {
            LOG.debug(".forwardLocalMetricsToServer(): Exit, failed to update");
        } else {
            LOG.debug(".forwardLocalMetricsToServer(): Exit, Update successful");
        }
    }

    //
    // Getters and Setters
    //

    protected Logger getLogger() {
        return (LOG);
    }

    protected ObjectMapper getJsonMapper() {
        return jsonMapper;
    }

    protected ProcessingPlantInterface getProcessingPlant() {
        return processingPlant;
    }

    protected PetasosLocalMetricsDM getMetricsDM() {
        return metricsDM;
    }

    protected PetasosComponentMetricSetFactory getComponentMetricSetFactory() {
        return componentMetricSetFactory;
    }

    protected SerializableObject getMetricQueueLock() {
        return metricQueueLock;
    }

    protected ConcurrentHashMap<ComponentIdType, PetasosComponentMetricSet> getMetricsQueue() {
        return metricsQueue;
    }
}
