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
package net.fhirfactory.dricats.deployment.properties.configurationfilebased.ladon.datagrid;

import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.archetypes.PetasosEnabledSubsystemPropertyFile;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.jgroups.JGroupsKubernetesPodPortSegment;

public abstract class LadonDatagridPropertyFile extends PetasosEnabledSubsystemPropertyFile {

    private JGroupsKubernetesPodPortSegment multizoneDatagrid;

    //
    // Constructor(s)
    //

    public LadonDatagridPropertyFile() {
        super();
        this.multizoneDatagrid = null;
    }

    //
    // Getters and Setters
    //

    public JGroupsKubernetesPodPortSegment getMultizoneDatagrid() {
        return multizoneDatagrid;
    }

    public void setMultizoneDatagrid(JGroupsKubernetesPodPortSegment multizoneDatagrid) {
        this.multizoneDatagrid = multizoneDatagrid;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "LadonDatagridPropertyFile{" +
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
                ", getPetasosSubscriptionsEndpoint=" + getPetasosSubscriptionsEndpoint() +
                ", getPetasosAuditServicesEndpoint=" + getPetasosAuditServicesEndpoint() +
                ", getPetasosInterceptionEndpoint=" + getPetasosInterceptionEndpoint() +
                ", getPetasosTaskServicesEndpoint=" + getPetasosTaskServicesEndpoint() +
                ", getMultiuseInfinispanEndpoint=" + getMultiuseInfinispanEndpoint() +
                ", getPetasosMetricsEndpoint=" + getPetasosMetricsEndpoint() +
                ", getPetasosIPCMessagingEndpoint=" + getPetasosIPCMessagingEndpoint() +
                ", getPetasosTopologyDiscoveryEndpoint=" + getPetasosTopologyDiscoveryEndpoint() +
                ", edgeAnswer=" + getEdgeAnswer() +
                ", edgeAsk=" + getEdgeAsk() +
                ", multizoneDatagrid=" + multizoneDatagrid +
                '}';
    }
}
