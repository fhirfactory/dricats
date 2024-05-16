/*
 * Copyright (c) 2021 Mark A. Hunter (ACT Health)
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
package net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.archetypes;

import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.http.ClusteredHTTPServerPortSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.http.HTTPClientPortSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.jgroups.JGroupsInterZoneRepeaterClientPortSegment;

public class PetasosEnabledSubsystemPropertyFile extends ClusterServiceDeliverySubsystemPropertyFile {
    private JGroupsInterZoneRepeaterClientPortSegment petasosIPCMessagingEndpoint;
    private JGroupsInterZoneRepeaterClientPortSegment petasosTopologyDiscoveryEndpoint;
    private JGroupsInterZoneRepeaterClientPortSegment petasosSubscriptionsEndpoint;
    private JGroupsInterZoneRepeaterClientPortSegment petasosAuditServicesEndpoint;
    private JGroupsInterZoneRepeaterClientPortSegment petasosInterceptionEndpoint;
    private JGroupsInterZoneRepeaterClientPortSegment petasosTaskServicesEndpoint;
    private JGroupsInterZoneRepeaterClientPortSegment petasosMetricsEndpoint;
    private JGroupsInterZoneRepeaterClientPortSegment multiuseInfinispanEndpoint;
    private ClusteredHTTPServerPortSegment edgeAnswer;
    private HTTPClientPortSegment edgeAsk;

    //
    // Constructor(s)
    //

    public PetasosEnabledSubsystemPropertyFile() {
        super();
        edgeAnswer = new ClusteredHTTPServerPortSegment();
        edgeAsk = new HTTPClientPortSegment();
        petasosIPCMessagingEndpoint = new JGroupsInterZoneRepeaterClientPortSegment();
        petasosTopologyDiscoveryEndpoint = new JGroupsInterZoneRepeaterClientPortSegment();
        petasosSubscriptionsEndpoint = new JGroupsInterZoneRepeaterClientPortSegment();
        petasosAuditServicesEndpoint = new JGroupsInterZoneRepeaterClientPortSegment();
        petasosInterceptionEndpoint = new JGroupsInterZoneRepeaterClientPortSegment();
        petasosTaskServicesEndpoint = new JGroupsInterZoneRepeaterClientPortSegment();
        multiuseInfinispanEndpoint = new JGroupsInterZoneRepeaterClientPortSegment();
        petasosMetricsEndpoint = new JGroupsInterZoneRepeaterClientPortSegment();
    }

    //
    // Getters and Setters
    //

    public JGroupsInterZoneRepeaterClientPortSegment getPetasosSubscriptionsEndpoint() {
        return petasosSubscriptionsEndpoint;
    }

    public void setPetasosSubscriptionsEndpoint(JGroupsInterZoneRepeaterClientPortSegment petasosSubscriptionsEndpoint) {
        this.petasosSubscriptionsEndpoint = petasosSubscriptionsEndpoint;
    }

    public HTTPClientPortSegment getEdgeAsk() {
        return edgeAsk;
    }

    public void setEdgeAsk(HTTPClientPortSegment edgeAsk) {
        this.edgeAsk = edgeAsk;
    }

    public JGroupsInterZoneRepeaterClientPortSegment getPetasosAuditServicesEndpoint() {
        return petasosAuditServicesEndpoint;
    }

    public void setPetasosAuditServicesEndpoint(JGroupsInterZoneRepeaterClientPortSegment petasosAuditServicesEndpoint) {
        this.petasosAuditServicesEndpoint = petasosAuditServicesEndpoint;
    }

    public JGroupsInterZoneRepeaterClientPortSegment getPetasosInterceptionEndpoint() {
        return petasosInterceptionEndpoint;
    }

    public void setPetasosInterceptionEndpoint(JGroupsInterZoneRepeaterClientPortSegment petasosInterceptionEndpoint) {
        this.petasosInterceptionEndpoint = petasosInterceptionEndpoint;
    }

    public JGroupsInterZoneRepeaterClientPortSegment getPetasosTaskServicesEndpoint() {
        return petasosTaskServicesEndpoint;
    }

    public void setPetasosTaskServicesEndpoint(JGroupsInterZoneRepeaterClientPortSegment petasosTaskServicesEndpoint) {
        this.petasosTaskServicesEndpoint = petasosTaskServicesEndpoint;
    }

    public JGroupsInterZoneRepeaterClientPortSegment getMultiuseInfinispanEndpoint() {
        return multiuseInfinispanEndpoint;
    }

    public void setMultiuseInfinispanEndpoint(JGroupsInterZoneRepeaterClientPortSegment multiuseInfinispanEndpoint) {
        this.multiuseInfinispanEndpoint = multiuseInfinispanEndpoint;
    }

    public JGroupsInterZoneRepeaterClientPortSegment getPetasosMetricsEndpoint() {
        return petasosMetricsEndpoint;
    }

    public void setPetasosMetricsEndpoint(JGroupsInterZoneRepeaterClientPortSegment petasosMetricsEndpoint) {
        this.petasosMetricsEndpoint = petasosMetricsEndpoint;
    }

    public JGroupsInterZoneRepeaterClientPortSegment getPetasosIPCMessagingEndpoint() {
        return petasosIPCMessagingEndpoint;
    }

    public void setPetasosIPCMessagingEndpoint(JGroupsInterZoneRepeaterClientPortSegment petasosIPCMessagingEndpoint) {
        this.petasosIPCMessagingEndpoint = petasosIPCMessagingEndpoint;
    }

    public ClusteredHTTPServerPortSegment getEdgeAnswer() {
        return edgeAnswer;
    }

    public void setEdgeAnswer(ClusteredHTTPServerPortSegment edgeAnswer) {
        this.edgeAnswer = edgeAnswer;
    }

    public JGroupsInterZoneRepeaterClientPortSegment getPetasosTopologyDiscoveryEndpoint() {
        return petasosTopologyDiscoveryEndpoint;
    }

    public void setPetasosTopologyDiscoveryEndpoint(JGroupsInterZoneRepeaterClientPortSegment petasosTopologyDiscoveryEndpoint) {
        this.petasosTopologyDiscoveryEndpoint = petasosTopologyDiscoveryEndpoint;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "PetasosEnabledSubsystemPropertyFile{" +
                "kubeReadinessProbe=" + getKubeReadinessProbe() +
                ", kubeLivelinessProbe=" + getKubeLivelinessProbe() +
                ", prometheusPort=" + getPrometheusPort() +
                ", jolokiaPort=" + getJolokiaPort() +
                ", subsystemInstant=" + getSubsystemInstant() +
                ", deploymentMode=" + getDeploymentMode() +
                ", deploymentSites=" + getDeploymentSites() +
                ", subsystemImageProperties=" + getSubsystemImageProperties() +
                ", trustStorePassword=" + getTrustStorePassword() +
                ", keyPassword=" + getKeyPassword() +
                ", deploymentZone=" + getDeploymentZone() +
                ", defaultServicePortLowerBound=" + getDefaultServicePortLowerBound() +
                ", loadBalancer=" + getLoadBalancer() +
                ", volumeMounts=" + getVolumeMounts() +
                ", debugProperties=" + getDebugProperties() +
                ", hapiAPIKey=" + getHapiAPIKey() +
                ", javaDeploymentParameters=" + getJavaDeploymentParameters() +
                ", petasosIPCMessagingEndpoint=" + petasosIPCMessagingEndpoint +
                ", petasosTopologyDiscoveryEndpoint=" + petasosTopologyDiscoveryEndpoint +
                ", petasosSubscriptionsEndpoint=" + petasosSubscriptionsEndpoint +
                ", petasosAuditServicesEndpoint=" + petasosAuditServicesEndpoint +
                ", petasosInterceptionEndpoint=" + petasosInterceptionEndpoint +
                ", petasosTaskServicesEndpoint=" + petasosTaskServicesEndpoint +
                ", multiuseInfinispanEndpoint=" + multiuseInfinispanEndpoint +
                ", petasosMetricsEndpoint=" + petasosMetricsEndpoint +
                ", edgeAnswer=" + edgeAnswer +
                ", edgeAsk=" + edgeAsk +
                '}';
    }
}
