package net.fhirfactory.dricats.deployment.properties.configurationfilebased.fhirbreak.emailgateway;

import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.interact.InteractClusteredServerPortSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.base.StandardServerPortSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.fhirbreak.common.FHIRBreakSubsystemPropertyFile;

public class FHIRBreakEmailGatewayPropertyFile extends FHIRBreakSubsystemPropertyFile {

    private InteractClusteredServerPortSegment smtpGateway;
    private InteractClusteredServerPortSegment imapGateway;

    private StandardServerPortSegment edgeReceiveCommunication;

    public InteractClusteredServerPortSegment getSmtpGateway() {
        return smtpGateway;
    }

    public void setSmtpGateway(InteractClusteredServerPortSegment smtpGateway) {
        this.smtpGateway = smtpGateway;
    }

    public InteractClusteredServerPortSegment getImapGateway() {
        return imapGateway;
    }

    public void setImapGateway(InteractClusteredServerPortSegment imapGateway) {
        this.imapGateway = imapGateway;
    }

    public StandardServerPortSegment getEdgeReceiveCommunication() {
        return edgeReceiveCommunication;
    }

    public void setEdgeReceiveCommunication(StandardServerPortSegment edgeReceiveCommunication) {
        this.edgeReceiveCommunication = edgeReceiveCommunication;
    }
}
