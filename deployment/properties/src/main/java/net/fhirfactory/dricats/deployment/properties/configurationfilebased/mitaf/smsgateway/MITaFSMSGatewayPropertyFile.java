package net.fhirfactory.dricats.deployment.properties.configurationfilebased.mitaf.smsgateway;

import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.base.StandardServerPortSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.interact.InteractServerPortSegment;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.mitaf.MITaFSubsystemPropertyFile;

public class MITaFSMSGatewayPropertyFile extends MITaFSubsystemPropertyFile {

    private InteractServerPortSegment smsGateway;

    private StandardServerPortSegment edgeReceiveCommunication;

    public InteractServerPortSegment getSmsGateway() {
        return smsGateway;
    }

    public void setSmsGateway(InteractServerPortSegment smsGateway) {
        this.smsGateway = smsGateway;
    }

    public StandardServerPortSegment getEdgeReceiveCommunication() {
        return edgeReceiveCommunication;
    }

    public void setEdgeReceiveCommunication(StandardServerPortSegment edgeReceiveCommunication) {
        this.edgeReceiveCommunication = edgeReceiveCommunication;
    }
}
