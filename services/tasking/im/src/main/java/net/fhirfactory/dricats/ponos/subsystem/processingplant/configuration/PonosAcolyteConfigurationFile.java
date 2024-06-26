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
package net.fhirfactory.dricats.ponos.subsystem.processingplant.configuration;

import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.archetypes.PetasosEnabledSubsystemPropertyFile;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.http.HTTPClientPortSegment;

public class PonosAcolyteConfigurationFile extends PetasosEnabledSubsystemPropertyFile {

    private HTTPClientPortSegment taskLoggerDM;
    private HTTPClientPortSegment taskRouterDM;
    private HTTPClientPortSegment taskReportDM;

    //
    // Constructor(s)
    //

    public PonosAcolyteConfigurationFile() {
        super();
    }

    //
    // Getters and Setters
    //

    public HTTPClientPortSegment getTaskLoggerDM() {
        return taskLoggerDM;
    }

    public void setTaskLoggerDM(HTTPClientPortSegment taskLoggerDM) {
        this.taskLoggerDM = taskLoggerDM;
    }

    public HTTPClientPortSegment getTaskRouterDM() {
        return taskRouterDM;
    }

    public void setTaskRouterDM(HTTPClientPortSegment taskRouterDM) {
        this.taskRouterDM = taskRouterDM;
    }

    public HTTPClientPortSegment getTaskReportDM() {
        return taskReportDM;
    }

    public void setTaskReportDM(HTTPClientPortSegment taskReportDM) {
        this.taskReportDM = taskReportDM;
    }


    //
    // To Stirng
    //

    @Override
    public String toString() {
        return "PonosControllerConfigurationFile{" +
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
                ", taskLoggerDM=" + taskLoggerDM +
                ", taskReportDM=" + taskReportDM +
                ", taskRouterDM=" + taskRouterDM +
                '}';
    }
}
