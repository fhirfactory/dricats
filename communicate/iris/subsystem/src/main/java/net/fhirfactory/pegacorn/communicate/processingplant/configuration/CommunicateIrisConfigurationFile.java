/*
 * Copyright (c) 2021 Mark A. Hunter
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
package net.fhirfactory.pegacorn.communicate.processingplant.configuration;

import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.archetypes.PetasosEnabledSubsystemPropertyFile;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.http.ClusteredHTTPServerPortSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.http.HTTPClientPortSegment;

public class CommunicateIrisConfigurationFile extends PetasosEnabledSubsystemPropertyFile {
    private ClusteredHTTPServerPortSegment interactIngressMatrixApplicationServices;
    private HTTPClientPortSegment interactEgressMatrixApplicationServices;
    private HTTPClientPortSegment interactEgressSynapseAPI;

    //
    // Constructor(s)
    //

    public CommunicateIrisConfigurationFile() {
        super();
    }

    //
    // Getters and Setters
    //

    public ClusteredHTTPServerPortSegment getInteractIngressMatrixApplicationServices() {
        return interactIngressMatrixApplicationServices;
    }

    public void setInteractIngressMatrixApplicationServices(ClusteredHTTPServerPortSegment interactIngressMatrixApplicationServices) {
        this.interactIngressMatrixApplicationServices = interactIngressMatrixApplicationServices;
    }

    public HTTPClientPortSegment getInteractEgressMatrixApplicationServices() {
        return interactEgressMatrixApplicationServices;
    }

    public void setInteractEgressMatrixApplicationServices(HTTPClientPortSegment interactEgressMatrixApplicationServices) {
        this.interactEgressMatrixApplicationServices = interactEgressMatrixApplicationServices;
    }

    public HTTPClientPortSegment getInteractEgressSynapseAPI() {
        return interactEgressSynapseAPI;
    }

    public void setInteractEgressSynapseAPI(HTTPClientPortSegment interactEgressSynapseAPI) {
        this.interactEgressSynapseAPI = interactEgressSynapseAPI;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "CommunicateIrisConfigurationFile{" +
                "interactIngressMatrixApplicationServices=" + interactIngressMatrixApplicationServices +
                ", interactEgressMatrixApplicationServices=" + interactEgressMatrixApplicationServices +
                ", interactEgressSynapseAPI=" + interactEgressSynapseAPI +
                ", kubeReadinessProbe=" + getKubeReadinessProbe() +
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
                ", petasosSubscriptionsEndpoint=" + getPetasosSubscriptionsEndpoint() +
                ", edgeAsk=" + getEdgeAsk() +
                ", petasosAuditServicesEndpoint=" + getPetasosAuditServicesEndpoint() +
                ", petasosInterceptionEndpoint=" + getPetasosInterceptionEndpoint() +
                ", petasosTaskServicesEndpoint=" + getPetasosTaskServicesEndpoint() +
                ", multiuseInfinispanEndpoint=" + getMultiuseInfinispanEndpoint() +
                ", petasosMetricsEndpoint=" + getPetasosMetricsEndpoint() +
                ", petasosIPCMessagingEndpoint=" + getPetasosIPCMessagingEndpoint() +
                ", edgeAnswer=" + getEdgeAnswer() +
                ", petasosTopologyDiscoveryEndpoint=" + getPetasosTopologyDiscoveryEndpoint() +
                '}';
    }
}
