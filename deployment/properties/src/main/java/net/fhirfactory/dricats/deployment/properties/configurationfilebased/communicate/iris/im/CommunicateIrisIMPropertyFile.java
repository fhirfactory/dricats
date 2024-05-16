package net.fhirfactory.dricats.deployment.properties.configurationfilebased.communicate.iris.im;

import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.SecurityCredentialSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.http.ClusteredHTTPServerPortSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.http.HTTPClientPortSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.communicate.common.CommunicateSubsystemPropertyFile;

public class CommunicateIrisIMPropertyFile extends CommunicateSubsystemPropertyFile {

    private HTTPClientPortSegment interactMatrixClientServicesClient;
    private ClusteredHTTPServerPortSegment interactMatrixApplicationServicesServer;
    private ClusteredHTTPServerPortSegment interactPegacornVoIPServicesServer;
    private SecurityCredentialSegment wildflyUser;

    //
    // Constructor(s)
    //

    public CommunicateIrisIMPropertyFile() {
        super();
        this.wildflyUser = new SecurityCredentialSegment();
        this.interactMatrixClientServicesClient = new HTTPClientPortSegment();
        this.interactMatrixApplicationServicesServer = new ClusteredHTTPServerPortSegment();
        this.interactPegacornVoIPServicesServer = new ClusteredHTTPServerPortSegment();
    }

    //
    // Getters and Setters
    //

    public HTTPClientPortSegment getInteractMatrixClientServicesClient() {
        return interactMatrixClientServicesClient;
    }

    public void setInteractMatrixClientServicesClient(HTTPClientPortSegment interactMatrixClientServicesClient) {
        this.interactMatrixClientServicesClient = interactMatrixClientServicesClient;
    }

    public ClusteredHTTPServerPortSegment getInteractMatrixApplicationServicesServer() {
        return interactMatrixApplicationServicesServer;
    }

    public void setInteractMatrixApplicationServicesServer(ClusteredHTTPServerPortSegment interactMatrixApplicationServicesServer) {
        this.interactMatrixApplicationServicesServer = interactMatrixApplicationServicesServer;
    }

    public ClusteredHTTPServerPortSegment getInteractPegacornVoIPServicesServer() {
        return interactPegacornVoIPServicesServer;
    }

    public void setInteractPegacornVoIPServicesServer(ClusteredHTTPServerPortSegment interactPegacornVoIPServicesServer) {
        this.interactPegacornVoIPServicesServer = interactPegacornVoIPServicesServer;
    }

    public SecurityCredentialSegment getWildflyUser() {
        return wildflyUser;
    }

    public void setWildflyUser(SecurityCredentialSegment wildflyUser) {
        this.wildflyUser = wildflyUser;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "CommunicateIrisIMPropertyFile{" +
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
                ", interactMatrixClientServicesClient=" + interactMatrixClientServicesClient +
                ", interactMatrixApplicationServicesServer=" + interactMatrixApplicationServicesServer +
                ", interactPegacornVoIPServicesServer=" + interactPegacornVoIPServicesServer +
                ", wildflyUser=" + wildflyUser +
                '}';
    }
}
