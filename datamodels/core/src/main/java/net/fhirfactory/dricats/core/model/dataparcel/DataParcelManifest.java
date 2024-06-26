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
package net.fhirfactory.dricats.core.model.dataparcel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.fhirfactory.dricats.core.model.dataparcel.valuesets.*;
import net.fhirfactory.dricats.core.model.participant.id.PetasosParticipantId;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.Objects;

public class DataParcelManifest implements Serializable {

    public static String WILDCARD_CHARACTER = "*";

    private DataParcelTypeDescriptor contentDescriptor;
    private DataParcelTypeDescriptor containerDescriptor;
    private DataParcelQualityStatement payloadQuality;
    private DataParcelNormalisationStatusEnum normalisationStatus;
    private DataParcelValidationStatusEnum validationStatus;
    private DataParcelTypeEnum dataParcelType;
    private String sourceSystem;
    private String intendedTargetSystem;
    private PolicyEnforcementPointApprovalStatusEnum enforcementPointApprovalStatus;
    private boolean interSubsystemDistributable;
    private DataParcelDirectionEnum dataParcelFlowDirection;
    private PetasosParticipantId previousParticipant;
    private PetasosParticipantId originParticipant;
    private PetasosParticipantId destinationParticipant;

    public DataParcelManifest() {
        this.contentDescriptor = null;
        this.containerDescriptor = null;
        this.sourceSystem = null;
        this.intendedTargetSystem = null;
        this.interSubsystemDistributable = false;
        this.dataParcelFlowDirection = null;
        this.originParticipant = null;
        this.destinationParticipant = null;
        this.previousParticipant = null;
        this.enforcementPointApprovalStatus = PolicyEnforcementPointApprovalStatusEnum.POLICY_ENFORCEMENT_POINT_APPROVAL_NEGATIVE;
        this.normalisationStatus = DataParcelNormalisationStatusEnum.DATA_PARCEL_CONTENT_NORMALISATION_ANY;
        this.validationStatus = DataParcelValidationStatusEnum.DATA_PARCEL_CONTENT_VALIDATION_ANY;
        this.dataParcelType = DataParcelTypeEnum.GENERAL_DATA_PARCEL_TYPE;
    }

    public DataParcelManifest(DataParcelTypeDescriptor contentDescriptor) {
        this.containerDescriptor = null;
        this.contentDescriptor = (DataParcelTypeDescriptor) SerializationUtils.clone(contentDescriptor);
        this.sourceSystem = null;
        this.intendedTargetSystem = null;
        this.interSubsystemDistributable = false;
        this.dataParcelFlowDirection = null;
        this.originParticipant = null;
        this.destinationParticipant = null;
        this.previousParticipant = null;
        this.enforcementPointApprovalStatus = PolicyEnforcementPointApprovalStatusEnum.POLICY_ENFORCEMENT_POINT_APPROVAL_NEGATIVE;
        this.normalisationStatus = DataParcelNormalisationStatusEnum.DATA_PARCEL_CONTENT_NORMALISATION_ANY;
        this.validationStatus = DataParcelValidationStatusEnum.DATA_PARCEL_CONTENT_VALIDATION_ANY;
        this.dataParcelType = DataParcelTypeEnum.GENERAL_DATA_PARCEL_TYPE;
    }

    public DataParcelManifest(DataParcelManifest ori) {
        if (ori.hasDestinationParticipant()) {
            this.setDestinationParticipant(ori.getDestinationParticipant());
        } else {
            this.setDestinationParticipant(null);
        }
        if (ori.hasOriginParticipant()) {
            this.setOriginParticipant(ori.getOriginParticipant());
        } else {
            this.setOriginParticipant(null);
        }
        if (ori.hasPreviousParticipant()) {
            this.setPreviousParticipant(ori.getPreviousParticipant());
        } else {
            this.setPreviousParticipant(null);
        }
        if (ori.hasContainerDescriptor()) {
            this.containerDescriptor = (DataParcelTypeDescriptor) SerializationUtils.clone(ori.getContainerDescriptor());
        } else {
            this.containerDescriptor = null;
        }
        if (ori.hasContentDescriptor()) {
            this.contentDescriptor = (DataParcelTypeDescriptor) SerializationUtils.clone(ori.getContentDescriptor());
        } else {
            this.contentDescriptor = null;
        }
        if (ori.hasSourceSystem()) {
            this.setSourceSystem(ori.getSourceSystem());
        } else {
            this.setSourceSystem(null);
        }
        if (ori.hasIntendedTargetSystem()) {
            this.setIntendedTargetSystem(ori.getIntendedTargetSystem());
        } else {
            this.setIntendedTargetSystem(null);
        }
        this.setInterSubsystemDistributable(ori.isInterSubsystemDistributable());
        if (ori.hasDataParcelFlowDirection()) {
            this.setDataParcelFlowDirection(ori.getDataParcelFlowDirection());
        } else {
            this.setDataParcelFlowDirection(null);
        }
        if (ori.hasEnforcementPointApprovalStatus()) {
            this.setEnforcementPointApprovalStatus(ori.getEnforcementPointApprovalStatus());
        } else {
            this.enforcementPointApprovalStatus = PolicyEnforcementPointApprovalStatusEnum.POLICY_ENFORCEMENT_POINT_APPROVAL_NEGATIVE;
        }
        if (ori.hasNormalisationStatus()) {
            this.setNormalisationStatus(ori.getNormalisationStatus());
        } else {
            this.normalisationStatus = DataParcelNormalisationStatusEnum.DATA_PARCEL_CONTENT_NORMALISATION_ANY;
        }
        if (ori.hasValidationStatus()) {
            this.setValidationStatus(ori.getValidationStatus());
        } else {
            this.validationStatus = DataParcelValidationStatusEnum.DATA_PARCEL_CONTENT_VALIDATION_ANY;
        }
        if (ori.hasDataParcelType()) {
            this.setDataParcelType(ori.getDataParcelType());
        } else {
            this.dataParcelType = DataParcelTypeEnum.GENERAL_DATA_PARCEL_TYPE;
        }
    }

    //
    // Getters and Setters (and Has's)
    //

    @JsonIgnore
    public boolean hasPreviousParticipant() {
        boolean hasValue = this.previousParticipant != null;
        return (hasValue);
    }

    public PetasosParticipantId getPreviousParticipant() {
        return previousParticipant;
    }

    public void setPreviousParticipant(PetasosParticipantId previousParticipant) {
        this.previousParticipant = previousParticipant;
    }

    @JsonIgnore
    public boolean hasDestinationParticipant() {
        boolean hasValue = this.destinationParticipant != null;
        return (hasValue);
    }

    public PetasosParticipantId getDestinationParticipant() {
        return destinationParticipant;
    }

    public void setDestinationParticipant(PetasosParticipantId destinationParticipant) {
        this.destinationParticipant = destinationParticipant;
    }

    @JsonIgnore
    public boolean hasOriginParticipant() {
        boolean hasValue = this.originParticipant != null;
        return (hasValue);
    }

    public PetasosParticipantId getOriginParticipant() {
        return originParticipant;
    }

    public void setOriginParticipant(PetasosParticipantId taskProducerProcessingPlantParticipantName) {
        this.originParticipant = taskProducerProcessingPlantParticipantName;
    }

    @JsonIgnore
    public boolean hasDataParcelType() {
        boolean has = this.dataParcelType != null;
        return (has);
    }

    @JsonIgnore
    public boolean hasValidationStatus() {
        boolean has = this.validationStatus != null;
        return (has);
    }

    @JsonIgnore
    public boolean hasNormalisationStatus() {
        boolean has = this.normalisationStatus != null;
        return (has);
    }

    @JsonIgnore
    public boolean hasEnforcementPointApprovalStatus() {
        boolean has = this.enforcementPointApprovalStatus != null;
        return (has);
    }

    @JsonIgnore
    public boolean hasDataParcelFlowDirection() {
        boolean has = this.dataParcelFlowDirection != null;
        return (has);
    }

    @JsonIgnore
    public boolean hasContentDescriptor() {
        boolean hasCT = this.contentDescriptor != null;
        return (hasCT);
    }

    @JsonIgnore
    public boolean hasContainerDescriptor() {
        boolean hasCT = this.containerDescriptor != null;
        return (hasCT);
    }

    @JsonIgnore
    public boolean hasSourceSystem() {
        boolean hasSS = this.sourceSystem != null;
        return (hasSS);
    }

    @JsonIgnore
    public boolean hasIntendedTargetSystem() {
        boolean hasITS = this.intendedTargetSystem != null;
        return (hasITS);
    }

    @JsonIgnore
    public boolean hasDataParcelQualityStatement() {
        boolean hasDPQS = this.payloadQuality != null;
        return (hasDPQS);
    }

    public DataParcelTypeDescriptor getContentDescriptor() {
        return contentDescriptor;
    }

    public void setContentDescriptor(DataParcelTypeDescriptor contentDescriptor) {
        this.contentDescriptor = (DataParcelTypeDescriptor) SerializationUtils.clone(contentDescriptor);
    }

    public DataParcelTypeDescriptor getContainerDescriptor() {
        return containerDescriptor;
    }

    public void setContainerDescriptor(DataParcelTypeDescriptor containerDescriptor) {
        this.containerDescriptor = (DataParcelTypeDescriptor) SerializationUtils.clone(containerDescriptor);
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = (String) SerializationUtils.clone(sourceSystem);
    }

    public String getIntendedTargetSystem() {
        return intendedTargetSystem;
    }

    public void setIntendedTargetSystem(String intendedTargetSystem) {
        this.intendedTargetSystem = (String) SerializationUtils.clone(intendedTargetSystem);
    }

    public boolean isInterSubsystemDistributable() {
        return interSubsystemDistributable;
    }

    public void setInterSubsystemDistributable(boolean interSubsystemDistributable) {
        this.interSubsystemDistributable = interSubsystemDistributable;
    }

    public DataParcelQualityStatement getPayloadQuality() {
        return payloadQuality;
    }

    public void setPayloadQuality(DataParcelQualityStatement payloadQuality) {
        this.payloadQuality = (DataParcelQualityStatement) SerializationUtils.clone(payloadQuality);
    }

    public DataParcelNormalisationStatusEnum getNormalisationStatus() {
        return normalisationStatus;
    }

    public void setNormalisationStatus(DataParcelNormalisationStatusEnum normalisationStatus) {
        this.normalisationStatus = normalisationStatus;
    }

    public DataParcelValidationStatusEnum getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(DataParcelValidationStatusEnum validationStatus) {
        this.validationStatus = validationStatus;
    }

    public DataParcelTypeEnum getDataParcelType() {
        return dataParcelType;
    }

    public void setDataParcelType(DataParcelTypeEnum dataParcelType) {
        this.dataParcelType = dataParcelType;
    }

    public PolicyEnforcementPointApprovalStatusEnum getEnforcementPointApprovalStatus() {
        return enforcementPointApprovalStatus;
    }

    public void setEnforcementPointApprovalStatus(PolicyEnforcementPointApprovalStatusEnum enforcementPointApprovalStatus) {
        this.enforcementPointApprovalStatus = enforcementPointApprovalStatus;
    }

    public DataParcelDirectionEnum getDataParcelFlowDirection() {
        return dataParcelFlowDirection;
    }

    public void setDataParcelFlowDirection(DataParcelDirectionEnum dataParcelFlowDirection) {
        this.dataParcelFlowDirection = dataParcelFlowDirection;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "DataParcelManifest{" +
                "contentDescriptor=" + contentDescriptor +
                ", containerDescriptor=" + containerDescriptor +
                ", payloadQuality=" + payloadQuality +
                ", normalisationStatus=" + normalisationStatus +
                ", validationStatus=" + validationStatus +
                ", dataParcelType=" + dataParcelType +
                ", sourceSystem='" + sourceSystem + '\'' +
                ", intendedTargetSystem='" + intendedTargetSystem + '\'' +
                ", enforcementPointApprovalStatus=" + enforcementPointApprovalStatus +
                ", interSubsystemDistributable=" + interSubsystemDistributable +
                ", dataParcelFlowDirection=" + dataParcelFlowDirection +
                ", previousParticipant=" + previousParticipant +
                ", originParticipant=" + originParticipant +
                ", destinationParticipant=" + destinationParticipant +
                '}';
    }

    //
    // Hash and Equals
    //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataParcelManifest that = (DataParcelManifest) o;
        return isInterSubsystemDistributable() == that.isInterSubsystemDistributable() && Objects.equals(getContentDescriptor(), that.getContentDescriptor()) && Objects.equals(getContainerDescriptor(), that.getContainerDescriptor()) && Objects.equals(getPayloadQuality(), that.getPayloadQuality()) && getNormalisationStatus() == that.getNormalisationStatus() && getValidationStatus() == that.getValidationStatus() && getDataParcelType() == that.getDataParcelType() && Objects.equals(getSourceSystem(), that.getSourceSystem()) && Objects.equals(getIntendedTargetSystem(), that.getIntendedTargetSystem()) && getEnforcementPointApprovalStatus() == that.getEnforcementPointApprovalStatus() && getDataParcelFlowDirection() == that.getDataParcelFlowDirection() && Objects.equals(getOriginParticipant(), that.getOriginParticipant()) && Objects.equals(getDestinationParticipant(), that.getDestinationParticipant());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContentDescriptor(), getContainerDescriptor(), getPayloadQuality(), getNormalisationStatus(), getValidationStatus(), getDataParcelType(), getSourceSystem(), getIntendedTargetSystem(), getEnforcementPointApprovalStatus(), isInterSubsystemDistributable(), getDataParcelFlowDirection(), getOriginParticipant(), getDestinationParticipant());
    }
}
