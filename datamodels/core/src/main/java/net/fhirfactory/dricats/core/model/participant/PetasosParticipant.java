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
package net.fhirfactory.dricats.core.model.participant;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import net.fhirfactory.dricats.core.constants.petasos.PetasosPropertyConstants;
import net.fhirfactory.dricats.core.model.component.SoftwareComponent;
import net.fhirfactory.dricats.core.model.participant.id.PetasosParticipantId;
import net.fhirfactory.dricats.core.model.task.datatypes.work.datatypes.TaskWorkItemManifestType;
import net.fhirfactory.dricats.core.model.task.datatypes.work.datatypes.TaskWorkItemSubscriptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PetasosParticipant extends SoftwareComponent implements Serializable {
    private static final Logger LOG = LoggerFactory.getLogger(PetasosParticipant.class);

    private PetasosParticipantStatusEnum participantStatus;
    private Set<TaskWorkItemSubscriptionType> subscriptions;
    private Set<TaskWorkItemManifestType> publishedWorkItemManifests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSXXX", timezone = PetasosPropertyConstants.DEFAULT_TIMEZONE)
    private Instant utilisationUpdateInstant;
    private PetasosParticipantFulfillment fulfillmentState;
    private PetasosParticipantControlStatusEnum participantControlStatus;

    //
    // Constructor(s)
    //

    public PetasosParticipant() {
        super();
        this.participantStatus = PetasosParticipantStatusEnum.PETASOS_PARTICIPANT_STARTING;
        this.utilisationUpdateInstant = null;
        this.subscriptions = new HashSet<>();
        this.publishedWorkItemManifests = new HashSet<>();
        this.fulfillmentState = null;
        this.participantControlStatus = PetasosParticipantControlStatusEnum.PARTICIPANT_IS_ENABLED;
    }

    public PetasosParticipant(PetasosParticipant ori) {
        super(ori);

        this.participantStatus = PetasosParticipantStatusEnum.PETASOS_PARTICIPANT_STARTING;
        this.participantControlStatus = PetasosParticipantControlStatusEnum.PARTICIPANT_IS_ENABLED;
        this.utilisationUpdateInstant = null;
        this.subscriptions = new HashSet<>();
        this.publishedWorkItemManifests = new HashSet<>();

        if (ori.hasParticipantStatus()) {
            this.setParticipantStatus(ori.getParticipantStatus());
        }
        if (ori.hasUtilisationUpdateInstant()) {
            this.setUtilisationUpdateInstant(ori.getUtilisationUpdateInstant());
        }
        if (!ori.getSubscriptions().isEmpty()) {
            this.getSubscriptions().addAll(ori.getSubscriptions());
        }
        if (!ori.getPublishedWorkItemManifests().isEmpty()) {
            this.getPublishedWorkItemManifests().addAll(ori.getPublishedWorkItemManifests());
        }
        if (ori.hasFulfillmentState()) {
            this.setFulfillmentState(ori.getFulfillmentState());
        }
        if (ori.hasParticipantControlStatus()) {
            this.setParticipantControlStatus(ori.participantControlStatus);
        }
    }

    public PetasosParticipant(String participantName, SoftwareComponent ori) {
        super(ori);
        this.participantStatus = PetasosParticipantStatusEnum.PETASOS_PARTICIPANT_STARTING;
        this.participantControlStatus = PetasosParticipantControlStatusEnum.PARTICIPANT_IS_ENABLED;
        this.setParticipantName(participantName);
        this.utilisationUpdateInstant = null;
        this.subscriptions = new HashSet<>();
        this.publishedWorkItemManifests = new HashSet<>();
    }

    public PetasosParticipant(SoftwareComponent ori) {
        super(ori);
        this.participantControlStatus = PetasosParticipantControlStatusEnum.PARTICIPANT_IS_ENABLED;
        this.participantStatus = PetasosParticipantStatusEnum.PETASOS_PARTICIPANT_STARTING;
        ;
        this.utilisationUpdateInstant = null;
        this.subscriptions = new HashSet<>();
        this.publishedWorkItemManifests = new HashSet<>();
    }

    //
    // Getters (and Setters)
    //

    @JsonIgnore
    public PetasosParticipantId getParticipantId() {
        PetasosParticipantId participantId = new PetasosParticipantId();
        participantId.setName(getParticipantName());
        participantId.setSubsystemName(getSubsystemParticipantName());
        participantId.setFullName(getParticipantName());
        participantId.setDisplayName(getParticipantDisplayName());
        return (participantId);
    }

    @JsonIgnore
    public boolean hasParticipantControlStatus() {
        boolean hasValue = this.participantControlStatus != null;
        return (hasValue);
    }

    public PetasosParticipantControlStatusEnum getParticipantControlStatus() {
        return participantControlStatus;
    }

    public void setParticipantControlStatus(PetasosParticipantControlStatusEnum participantControlStatus) {
        this.participantControlStatus = participantControlStatus;
    }

    public boolean hasFulfillmentState() {
        boolean hasValue = this.fulfillmentState != null;
        return (hasValue);
    }

    public PetasosParticipantFulfillment getFulfillmentState() {
        return fulfillmentState;
    }

    public void setFulfillmentState(PetasosParticipantFulfillment fulfillmentState) {
        this.fulfillmentState = fulfillmentState;
    }

    @Override
    protected Logger getLogger() {
        return (LOG);
    }

    @JsonIgnore
    public boolean hasParticipantStatus() {
        boolean hasValue = this.participantStatus != null;
        return (hasValue);
    }

    public PetasosParticipantStatusEnum getParticipantStatus() {
        return participantStatus;
    }

    public void setParticipantStatus(PetasosParticipantStatusEnum connectionStatus) {
        this.participantStatus = connectionStatus;
    }

    @JsonIgnore
    public boolean hasUtilisationUpdateInstant() {
        boolean hasValue = this.utilisationUpdateInstant != null;
        return (hasValue);
    }

    public Instant getUtilisationUpdateInstant() {
        return utilisationUpdateInstant;
    }

    public void setUtilisationUpdateInstant(Instant utilisationUpdateInstant) {
        this.utilisationUpdateInstant = utilisationUpdateInstant;
    }

    public Set<TaskWorkItemSubscriptionType> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<TaskWorkItemSubscriptionType> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Set<TaskWorkItemManifestType> getPublishedWorkItemManifests() {
        return publishedWorkItemManifests;
    }

    public void setPublishedWorkItemManifests(Set<TaskWorkItemManifestType> publishedWorkItemManifests) {
        this.publishedWorkItemManifests = publishedWorkItemManifests;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "PetasosParticipant{" +
                "lastActivityInstant=" + getLastActivityInstant() +
                ", lastReportingInstant=" + getLastReportingInstant() +
                ", subsystemParticipantName='" + getSubsystemParticipantName() + '\'' +
                ", componentFDN=" + getComponentFDN() +
                ", otherConfigurationParameters=" + getOtherConfigurationParameters() +
                ", concurrencyMode=" + getConcurrencyMode() +
                ", resilienceMode=" + getResilienceMode() +
                ", securityZone=" + getSecurityZone() +
                ", componentID=" + getComponentID() +
                ", nodeFunctionFDN=" + getNodeFunctionFDN() +
                ", componentType=" + getComponentType() +
                ", containingNodeFDN=" + getContainingNodeFDN() +
                ", componentRDN=" + getComponentRDN() +
                ", metrics=" + getMetrics() +
                ", componentSystemRole=" + getComponentSystemRole() +
                ", componentStatus=" + getComponentStatus() +
                ", utilisationStatus=" + participantStatus +
                ", subscribedWorkItemManifests=" + subscriptions +
                ", publishedWorkItemManifests=" + publishedWorkItemManifests +
                ", utilisationUpdateInstant=" + utilisationUpdateInstant +
                ", fulfillmentState=" + fulfillmentState +
                ", participantControlStatus=" + participantControlStatus +
                ", dataGridId=" + getDataGridId() +
                '}';
    }

    //
    // Hash and Equals
    //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetasosParticipant)) return false;
        if (!super.equals(o)) return false;
        PetasosParticipant that = (PetasosParticipant) o;
        return getParticipantStatus() == that.getParticipantStatus() && Objects.equals(getSubscriptions(), that.getSubscriptions()) && Objects.equals(getPublishedWorkItemManifests(), that.getPublishedWorkItemManifests()) && Objects.equals(getUtilisationUpdateInstant(), that.getUtilisationUpdateInstant());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getParticipantStatus(), getSubscriptions(), getPublishedWorkItemManifests(), getUtilisationUpdateInstant());
    }
}
