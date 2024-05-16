package net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.archetypes.fhirpersistence.im;

import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.archetypes.ClusterServiceDeliverySubsystemPropertyFile;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.http.ClusteredHTTPServerPortSegment;

public abstract class FHIRInformationManagerPropertyFile extends ClusterServiceDeliverySubsystemPropertyFile {

    ClusteredHTTPServerPortSegment edgeAnswerConformance;
    ClusteredHTTPServerPortSegment edgeAnswerTerminology;
    ClusteredHTTPServerPortSegment edgeAnswerSecurity;
    ClusteredHTTPServerPortSegment edgeAnswerDocuments;
    ClusteredHTTPServerPortSegment edgeAnswerOther;
    ClusteredHTTPServerPortSegment edgeAnswerIndividuals;
    ClusteredHTTPServerPortSegment edgeAnswerEntities;
    ClusteredHTTPServerPortSegment edgeAnswerWorkflow;
    ClusteredHTTPServerPortSegment edgeAnswerManagement;
    ClusteredHTTPServerPortSegment edgeAnswerSummary;
    ClusteredHTTPServerPortSegment edgeAnswerDiagnostics;
    ClusteredHTTPServerPortSegment edgeAnswerMedications;
    ClusteredHTTPServerPortSegment edgeAnswerCareProvision;
    ClusteredHTTPServerPortSegment edgeAnswerRequestAndResponse;
    ClusteredHTTPServerPortSegment edgeAnswerSupport;
    ClusteredHTTPServerPortSegment edgeAnswerBilling;
    ClusteredHTTPServerPortSegment edgeAnswerPayment;
    ClusteredHTTPServerPortSegment edgeAnswerGeneral;

    public ClusteredHTTPServerPortSegment getEdgeAnswerConformance() {
        return edgeAnswerConformance;
    }

    public void setEdgeAnswerConformance(ClusteredHTTPServerPortSegment edgeAnswerConformance) {
        this.edgeAnswerConformance = edgeAnswerConformance;
    }

    public ClusteredHTTPServerPortSegment getEdgeAnswerTerminology() {
        return edgeAnswerTerminology;
    }

    public void setEdgeAnswerTerminology(ClusteredHTTPServerPortSegment edgeAnswerTerminology) {
        this.edgeAnswerTerminology = edgeAnswerTerminology;
    }

    public ClusteredHTTPServerPortSegment getEdgeAnswerSecurity() {
        return edgeAnswerSecurity;
    }

    public void setEdgeAnswerSecurity(ClusteredHTTPServerPortSegment edgeAnswerSecurity) {
        this.edgeAnswerSecurity = edgeAnswerSecurity;
    }

    public ClusteredHTTPServerPortSegment getEdgeAnswerDocuments() {
        return edgeAnswerDocuments;
    }

    public void setEdgeAnswerDocuments(ClusteredHTTPServerPortSegment edgeAnswerDocuments) {
        this.edgeAnswerDocuments = edgeAnswerDocuments;
    }

    public ClusteredHTTPServerPortSegment getEdgeAnswerOther() {
        return edgeAnswerOther;
    }

    public void setEdgeAnswerOther(ClusteredHTTPServerPortSegment edgeAnswerOther) {
        this.edgeAnswerOther = edgeAnswerOther;
    }

    public ClusteredHTTPServerPortSegment getEdgeAnswerIndividuals() {
        return edgeAnswerIndividuals;
    }

    public void setEdgeAnswerIndividuals(ClusteredHTTPServerPortSegment edgeAnswerIndividuals) {
        this.edgeAnswerIndividuals = edgeAnswerIndividuals;
    }

    public ClusteredHTTPServerPortSegment getEdgeAnswerEntities() {
        return edgeAnswerEntities;
    }

    public void setEdgeAnswerEntities(ClusteredHTTPServerPortSegment edgeAnswerEntities) {
        this.edgeAnswerEntities = edgeAnswerEntities;
    }

    public ClusteredHTTPServerPortSegment getEdgeAnswerWorkflow() {
        return edgeAnswerWorkflow;
    }

    public void setEdgeAnswerWorkflow(ClusteredHTTPServerPortSegment edgeAnswerWorkflow) {
        this.edgeAnswerWorkflow = edgeAnswerWorkflow;
    }

    public ClusteredHTTPServerPortSegment getEdgeAnswerManagement() {
        return edgeAnswerManagement;
    }

    public void setEdgeAnswerManagement(ClusteredHTTPServerPortSegment edgeAnswerManagement) {
        this.edgeAnswerManagement = edgeAnswerManagement;
    }

    public ClusteredHTTPServerPortSegment getEdgeAnswerSummary() {
        return edgeAnswerSummary;
    }

    public void setEdgeAnswerSummary(ClusteredHTTPServerPortSegment edgeAnswerSummary) {
        this.edgeAnswerSummary = edgeAnswerSummary;
    }

    public ClusteredHTTPServerPortSegment getEdgeAnswerDiagnostics() {
        return edgeAnswerDiagnostics;
    }

    public void setEdgeAnswerDiagnostics(ClusteredHTTPServerPortSegment edgeAnswerDiagnostics) {
        this.edgeAnswerDiagnostics = edgeAnswerDiagnostics;
    }

    public ClusteredHTTPServerPortSegment getEdgeAnswerMedications() {
        return edgeAnswerMedications;
    }

    public void setEdgeAnswerMedications(ClusteredHTTPServerPortSegment edgeAnswerMedications) {
        this.edgeAnswerMedications = edgeAnswerMedications;
    }

    public ClusteredHTTPServerPortSegment getEdgeAnswerCareProvision() {
        return edgeAnswerCareProvision;
    }

    public void setEdgeAnswerCareProvision(ClusteredHTTPServerPortSegment edgeAnswerCareProvision) {
        this.edgeAnswerCareProvision = edgeAnswerCareProvision;
    }

    public ClusteredHTTPServerPortSegment getEdgeAnswerRequestAndResponse() {
        return edgeAnswerRequestAndResponse;
    }

    public void setEdgeAnswerRequestAndResponse(ClusteredHTTPServerPortSegment edgeAnswerRequestAndResponse) {
        this.edgeAnswerRequestAndResponse = edgeAnswerRequestAndResponse;
    }

    public ClusteredHTTPServerPortSegment getEdgeAnswerSupport() {
        return edgeAnswerSupport;
    }

    public void setEdgeAnswerSupport(ClusteredHTTPServerPortSegment edgeAnswerSupport) {
        this.edgeAnswerSupport = edgeAnswerSupport;
    }

    public ClusteredHTTPServerPortSegment getEdgeAnswerBilling() {
        return edgeAnswerBilling;
    }

    public void setEdgeAnswerBilling(ClusteredHTTPServerPortSegment edgeAnswerBilling) {
        this.edgeAnswerBilling = edgeAnswerBilling;
    }

    public ClusteredHTTPServerPortSegment getEdgeAnswerPayment() {
        return edgeAnswerPayment;
    }

    public void setEdgeAnswerPayment(ClusteredHTTPServerPortSegment edgeAnswerPayment) {
        this.edgeAnswerPayment = edgeAnswerPayment;
    }

    public ClusteredHTTPServerPortSegment getEdgeAnswerGeneral() {
        return edgeAnswerGeneral;
    }

    public void setEdgeAnswerGeneral(ClusteredHTTPServerPortSegment edgeAnswerGeneral) {
        this.edgeAnswerGeneral = edgeAnswerGeneral;
    }
}
