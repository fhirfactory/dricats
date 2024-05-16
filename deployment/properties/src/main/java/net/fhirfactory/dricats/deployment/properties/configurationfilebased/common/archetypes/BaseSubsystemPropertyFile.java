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

import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.*;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.http.HTTPServerPortSegment;

public class BaseSubsystemPropertyFile {

    private SubsystemInstanceSegment subsystemInstant;
    private DeploymentModeSegment deploymentMode;
    private DeploymentSiteSegment deploymentSites;
    private DeploymentZoneSegment deploymentZone;
    private HTTPServerPortSegment kubeReadinessProbe;
    private HTTPServerPortSegment kubeLivelinessProbe;
    private HTTPServerPortSegment prometheusPort;
    private HTTPServerPortSegment jolokiaPort;
    private LoadBalancerSegment loadBalancer;
    private PetasosNetworkingSegment networking;
    private SubsystemImageSegment subsystemImageProperties;
    private SecurityCredentialSegment trustStorePassword;
    private SecurityCredentialSegment keyPassword;

    //
    // Constructor(s)
    //

    public BaseSubsystemPropertyFile() {
        loadBalancer = new LoadBalancerSegment();
        subsystemInstant = new SubsystemInstanceSegment();
        deploymentMode = new DeploymentModeSegment();
        deploymentSites = new DeploymentSiteSegment();
        kubeLivelinessProbe = new HTTPServerPortSegment();
        kubeReadinessProbe = new HTTPServerPortSegment();
        subsystemImageProperties = new SubsystemImageSegment();
        trustStorePassword = new SecurityCredentialSegment();
        keyPassword = new SecurityCredentialSegment();
        jolokiaPort = new HTTPServerPortSegment();
        prometheusPort = new HTTPServerPortSegment();
        deploymentZone = new DeploymentZoneSegment();
        networking = new PetasosNetworkingSegment();
    }

    //
    // Getters And Setters
    //


    public PetasosNetworkingSegment getNetworking() {
        return networking;
    }

    public void setNetworking(PetasosNetworkingSegment networking) {
        this.networking = networking;
    }

    public LoadBalancerSegment getLoadBalancer() {
        return loadBalancer;
    }

    public void setLoadBalancer(LoadBalancerSegment loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    public HTTPServerPortSegment getKubeReadinessProbe() {
        return kubeReadinessProbe;
    }

    public void setKubeReadinessProbe(HTTPServerPortSegment kubeReadinessProbe) {
        this.kubeReadinessProbe = kubeReadinessProbe;
    }

    public HTTPServerPortSegment getKubeLivelinessProbe() {
        return kubeLivelinessProbe;
    }

    public void setKubeLivelinessProbe(HTTPServerPortSegment kubeLivelinessProbe) {
        this.kubeLivelinessProbe = kubeLivelinessProbe;
    }

    public HTTPServerPortSegment getPrometheusPort() {
        return prometheusPort;
    }

    public void setPrometheusPort(HTTPServerPortSegment prometheusPort) {
        this.prometheusPort = prometheusPort;
    }

    public HTTPServerPortSegment getJolokiaPort() {
        return jolokiaPort;
    }

    public void setJolokiaPort(HTTPServerPortSegment jolokiaPort) {
        this.jolokiaPort = jolokiaPort;
    }

    public SubsystemInstanceSegment getSubsystemInstant() {
        return subsystemInstant;
    }

    public void setSubsystemInstant(SubsystemInstanceSegment subsystemInstant) {
        this.subsystemInstant = subsystemInstant;
    }

    public DeploymentModeSegment getDeploymentMode() {
        return deploymentMode;
    }

    public void setDeploymentMode(DeploymentModeSegment deploymentMode) {
        this.deploymentMode = deploymentMode;
    }

    public DeploymentSiteSegment getDeploymentSites() {
        return deploymentSites;
    }

    public void setDeploymentSites(DeploymentSiteSegment deploymentSites) {
        this.deploymentSites = deploymentSites;
    }


    public SubsystemImageSegment getSubsystemImageProperties() {
        return subsystemImageProperties;
    }

    public void setSubsystemImageProperties(SubsystemImageSegment subsystemImageProperties) {
        this.subsystemImageProperties = subsystemImageProperties;
    }

    public SecurityCredentialSegment getTrustStorePassword() {
        return trustStorePassword;
    }

    public void setTrustStorePassword(SecurityCredentialSegment trustStorePassword) {
        this.trustStorePassword = trustStorePassword;
    }

    public SecurityCredentialSegment getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(SecurityCredentialSegment keyPassword) {
        this.keyPassword = keyPassword;
    }

    public DeploymentZoneSegment getDeploymentZone() {
        return deploymentZone;
    }

    public void setDeploymentZone(DeploymentZoneSegment deploymentZone) {
        this.deploymentZone = deploymentZone;
    }

    @Override
    public String toString() {
        return "BaseSubsystemPropertyFile{" +
                "subsystemInstant=" + subsystemInstant +
                ", deploymentMode=" + deploymentMode +
                ", deploymentSites=" + deploymentSites +
                ", kubeReadinessProbe=" + kubeReadinessProbe +
                ", kubeLivelinessProbe=" + kubeLivelinessProbe +
                ", prometheusPort=" + prometheusPort +
                ", jolokiaPort=" + jolokiaPort +
                ", subsystemImageProperties=" + subsystemImageProperties +
                ", trustStorePassword=" + trustStorePassword +
                ", keyPassword=" + keyPassword +
                ", deploymentZone=" + deploymentZone +
                ", networking=" + networking +
                "," + super.toString() +
                '}';
    }
}
