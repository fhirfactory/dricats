/*
 * Copyright (c) 2022 Mark A. Hunter
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
package net.fhirfactory.dricats.itops.im.workshops.workflow.beans;

import net.fhirfactory.dricats.core.constants.petasos.PetasosPropertyConstants;
import net.fhirfactory.dricats.core.model.dataparcel.DataParcelManifest;
import net.fhirfactory.dricats.core.model.dataparcel.DataParcelTypeDescriptor;
import net.fhirfactory.dricats.core.model.dataparcel.valuesets.DataParcelDirectionEnum;
import net.fhirfactory.dricats.core.model.dataparcel.valuesets.DataParcelNormalisationStatusEnum;
import net.fhirfactory.dricats.core.model.dataparcel.valuesets.DataParcelValidationStatusEnum;
import net.fhirfactory.dricats.core.model.oam.notifications.PetasosComponentITOpsNotification;
import net.fhirfactory.dricats.core.model.oam.notifications.valuesets.PetasosComponentITOpsNotificationTypeEnum;
import net.fhirfactory.dricats.core.model.participant.id.PetasosParticipantId;
import net.fhirfactory.dricats.core.model.topology.nodes.WorkUnitProcessorSoftwareComponent;
import net.fhirfactory.dricats.core.model.uow.UoW;
import net.fhirfactory.dricats.core.model.uow.UoWPayload;
import net.fhirfactory.dricats.core.model.uow.UoWProcessingOutcomeEnum;
import net.fhirfactory.dricats.internals.communicate.entities.message.CommunicateEmailMessage;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class ITOpsNotificationToCommunicateEmailMessage extends ITOpsNotificationToCommunicateMessage {
    private static final Logger LOG = LoggerFactory.getLogger(ITOpsNotificationToCommunicateEmailMessage.class);
    private static final String ITOPS_EMAIL_TARGET_ADDRESS = "ERROR_EVENT_EMAIL_TARGET_ADDRESS";
    private static final String ITOPS_EMAIL_SOURCE_ADDRESS = "ERROR_EVENT_EMAIL_SOURCE_ADDRESS";
    private static final String ITOPS_EMAIL_SUBJECT_PREFIX = "ERROR_EVENT_EMAIL_SUBJECT_PREFIX";
    private static final String UNDEFINED_ADDRESS = "Undefined Address";
    private String sourceEmailAddress;
    private String targetEmailAddress;
    private boolean resolvedTargetEmailAddress;
    private boolean resolvedSourceEmailAddress;

    //
    // Constructor(s)
    //

    public ITOpsNotificationToCommunicateEmailMessage() {
        super();
        resolvedTargetEmailAddress = false;
        resolvedSourceEmailAddress = false;
    }

    //
    // Implemented Abstract Methods
    //

    @Override
    protected Logger getLogger() {
        return (LOG);
    }

    //
    // Business Methods
    //

    private String getSourceEmailAddress() {
        if (!this.resolvedSourceEmailAddress) {
            sourceEmailAddress = getProcessingPlant().getMeAsASoftwareComponent().getOtherConfigurationParameter(ITOPS_EMAIL_SOURCE_ADDRESS);
            if (StringUtils.isEmpty(sourceEmailAddress)) {
                getLogger().error(".getSourceEmailAddress(): Cannot Resolve Source Address for ITOps Email Notifications");
                this.sourceEmailAddress = UNDEFINED_ADDRESS;
            } else {
                this.resolvedSourceEmailAddress = true;
            }
        }
        return (this.sourceEmailAddress);
    }

    private String getTargetEmailAddress() {
        if (!this.resolvedTargetEmailAddress) {
            targetEmailAddress = getProcessingPlant().getMeAsASoftwareComponent().getOtherConfigurationParameter(ITOPS_EMAIL_TARGET_ADDRESS);
            if (StringUtils.isEmpty(targetEmailAddress)) {
                getLogger().error(".getSourceEmailAddress(): Cannot Resolve Source Address for ITOps Email Notifications");
                this.targetEmailAddress = UNDEFINED_ADDRESS;
            } else {
                this.resolvedTargetEmailAddress = true;
            }
        }
        return (this.targetEmailAddress);
    }

    public UoW transformNotificationIntoCommunicateEmail(UoW uow, Exchange camelExchange) {
        getLogger().info(".transformNotificationIntoCommunicateEmail(): Entry, uow->{}", uow);

        String emailSource = getSourceEmailAddress();
        String emailTarget = getTargetEmailAddress();

        PetasosComponentITOpsNotification notification = camelExchange.getProperty(getLocalItopsNotificationMessagePropertyName(), PetasosComponentITOpsNotification.class);

        if (uow == null) {
            uow = new UoW();
            uow.setProcessingOutcome(UoWProcessingOutcomeEnum.UOW_OUTCOME_FAILED);
            uow.setFailureDescription("No UoW Maintained in camelExchange");
        }

        if (!uow.getProcessingOutcome().equals(UoWProcessingOutcomeEnum.UOW_OUTCOME_FAILED)) {
            if (emailSource.contentEquals(UNDEFINED_ADDRESS) || (emailTarget.contentEquals(UNDEFINED_ADDRESS))) {
                uow.setProcessingOutcome(UoWProcessingOutcomeEnum.UOW_OUTCOME_FAILED);
                StringBuilder errorMessageBuilder = new StringBuilder();
                if (emailSource.contentEquals(UNDEFINED_ADDRESS)) {
                    errorMessageBuilder.append("Error -> Email Source is Undefined \n");
                }
                if (emailTarget.contentEquals(UNDEFINED_ADDRESS)) {
                    errorMessageBuilder.append("Error -> Email Target is Undefined \n");
                }
                uow.setFailureDescription(errorMessageBuilder.toString());
            } else {
                CommunicateEmailMessage emailMessage = new CommunicateEmailMessage();
                emailMessage.setFrom(getSourceEmailAddress());
                emailMessage.getTo().add(getTargetEmailAddress());
                String subject = "";
                String subjectPrefix = getProcessingPlant().getMeAsASoftwareComponent().getOtherConfigurationParameter(ITOPS_EMAIL_SUBJECT_PREFIX);
                if (StringUtils.isNotEmpty(subjectPrefix)) {
                    subject += subjectPrefix; // no space after
                }
                if (notification.getNotificationType().equals(PetasosComponentITOpsNotificationTypeEnum.FAILURE_NOTIFICATION_TYPE)) {
                    subject += "Component Error (" + notification.getParticipantName() + ")";
                } else {
                    if (StringUtils.isNotEmpty(notification.getContentHeading())) {
                        subject += notification.getContentHeading();
                    } else {
                        subject += "Component Status (" + notification.getParticipantName() + ")";

                    }

                }
                emailMessage.setSubject(subject);
                StringBuilder emailMessageContentBuilder = new StringBuilder();
                if (StringUtils.isNotEmpty(notification.getContentHeading())) {
                    emailMessageContentBuilder.append(notification.getContentHeading() + "\n");
                }
                if (StringUtils.isNotEmpty(notification.getContent())) {
                    emailMessageContentBuilder.append(notification.getContent());
                }
                emailMessage.setContent(emailMessageContentBuilder.toString());
                emailMessage.setSimplifiedID("CommunicateEmailMessage:" + UUID.randomUUID().toString());
                emailMessage.setDescription("CommunicateEmailMessage: From(" + getProcessingPlant().getSubsystemParticipantName() + "), on behalf of (" + notification.getParticipantName() + ")");
                emailMessage.setDisplayName(getProcessingPlant().getSubsystemParticipantName() + "(" + emailMessage.getSimplifiedID() + ")");
                emailMessage.setContent(emailMessageContentBuilder.toString());

                UoWPayload egressPayload = new UoWPayload();
                try {
                    String egressPayloadString = getJSONMapper().writeValueAsString(emailMessage);
                    egressPayload.setPayload(egressPayloadString);
                    DataParcelManifest egressPayloadManifest = new DataParcelManifest();
                    DataParcelTypeDescriptor emailMessageDescriptor = getMessageTopicFactory().createEmailTypeDescriptor();
                    egressPayloadManifest.setContentDescriptor(emailMessageDescriptor);
                    WorkUnitProcessorSoftwareComponent wup = camelExchange.getProperty(PetasosPropertyConstants.WUP_TOPOLOGY_NODE_EXCHANGE_PROPERTY_NAME, WorkUnitProcessorSoftwareComponent.class);
                    PetasosParticipantId participantId = new PetasosParticipantId();
                    if (wup == null) {
                        participantId.setSubsystemName(getProcessingPlant().getSubsystemParticipantName());
                    } else {
                        participantId.setSubsystemName(getProcessingPlant().getSubsystemParticipantName());
                        participantId.setDisplayName(wup.getParticipantDisplayName());
                        participantId.setName(wup.getParticipantName());
                    }
                    egressPayloadManifest.setPreviousParticipant(participantId);
                    if (uow.getIngresContent().getPayloadManifest().hasOriginParticipant()) {
                        egressPayloadManifest.setOriginParticipant(uow.getIngresContent().getPayloadManifest().getOriginParticipant());
                    }
                    egressPayloadManifest.setNormalisationStatus(DataParcelNormalisationStatusEnum.DATA_PARCEL_CONTENT_NORMALISATION_TRUE);
                    egressPayloadManifest.setValidationStatus(DataParcelValidationStatusEnum.DATA_PARCEL_CONTENT_VALIDATED_TRUE);
                    egressPayloadManifest.setDataParcelFlowDirection(DataParcelDirectionEnum.INFORMATION_FLOW_WORKFLOW_OUTPUT);
                    egressPayloadManifest.setInterSubsystemDistributable(true);
                    egressPayload.setPayloadManifest(egressPayloadManifest);
                    uow.getEgressContent().addPayloadElement(egressPayload);
                    uow.setProcessingOutcome(UoWProcessingOutcomeEnum.UOW_OUTCOME_SUCCESS);
                } catch (Exception ex) {
                    uow.setProcessingOutcome(UoWProcessingOutcomeEnum.UOW_OUTCOME_FAILED);
                    uow.setFailureDescription("Could not convert CommunicateEmailMessage to JSON String, error->" + ExceptionUtils.getMessage(ex));
                    getLogger().warn(".transformNotificationIntoCommunicateEmail(): Could not convert CommunicateEmailMessage to JSON String, error->{}" + ExceptionUtils.getMessage(ex));
                }
            }
        }

        getLogger().warn(".transformNotificationIntoCommunicateEmail(): Exit, uow->{}", uow);
        return (uow);
    }
}
