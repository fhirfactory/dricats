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
package net.fhirfactory.dricats.internals.fhir.r4.resources.device.factories;

import net.fhirfactory.dricats.core.constants.systemwide.PegacornReferenceProperties;
import net.fhirfactory.dricats.core.model.componentid.PegacornSystemComponentTypeTypeEnum;
import net.fhirfactory.dricats.core.model.datagrid.datatypes.DataGridResourceIdType;
import net.fhirfactory.dricats.core.model.participant.PetasosParticipant;
import net.fhirfactory.dricats.core.model.participant.PetasosParticipantControlStatusEnum;
import net.fhirfactory.dricats.core.model.participant.PetasosParticipantStatusEnum;
import net.fhirfactory.dricats.core.model.task.datatypes.work.datatypes.TaskWorkItemSubscriptionType;
import net.fhirfactory.dricats.internals.fhir.r4.codesystems.PegacornIdentifierCodeEnum;
import net.fhirfactory.dricats.internals.fhir.r4.resources.endpoint.factories.EndpointFactory;
import net.fhirfactory.dricats.internals.fhir.r4.resources.identifier.PegacornIdentifierFactory;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class ParticipantDeviceFactory {
    private static final Logger LOG = LoggerFactory.getLogger(ParticipantDeviceFactory.class);
    private static final String PEGACORN_SOFTWARE_COMPONENT_DEVICE_TYPE_SYSTEM = "/device-type";
    private static final String DRICATS_PETASOS_PARTICIPANT_DEVICE_TYPE_NAME = "PetasosParticipant";
    private static final String DRICATS_SUBSYSTEM_PETASOS_PARTICIPANT_DEVICE_TYPE = "ProcessingPlant";
    private static final String DRICATS_WUP_PETASOS_PARTICIPANT_DEVICE_TYPE = "WorkUnitProcessor";
    @Inject
    private DeviceMetaTagFactory metaTagFactory;
    @Inject
    private DevicePropertyFactory propertyFactory;
    @Inject
    private PegacornIdentifierFactory identifierFactory;
    @Inject
    private PegacornReferenceProperties systemWideProperties;
    @Inject
    private DeviceSpecialisationFactory specialisationFactory;
    @Inject
    private EndpointFactory endpointFactory;
    @Inject
    private DeviceStatusFactory deviceStatusFactory;
    @Inject
    private DeviceSubscriptionFactory deviceSubscriptionFactory;

    //
    // Business Methods (to FHIR)
    //

    public Device mapPetasosParticipantToFHIRDevice(PetasosParticipant participant) {
        getLogger().debug(".mapPetasosParticipantToDevice(): Entry");
        if (participant == null) {
            getLogger().debug(".mapPetasosParticipantToDevice(): Exit, participant is null/empty");
            return (null);
        }
        try {
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] Start");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [Create Empty Device] Start");
            Device participantDevice = new Device();
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [Create Empty Device] Finish");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [Created and Set Primary Device Identifier] Start");
            String participantName = participant.getParticipantId().getName();
            Identifier deviceIdentifier = getIdentifierFactory().newIdentifier(PegacornIdentifierCodeEnum.IDENTIFIER_CODE_SOFTWARE_PARTICIPANT, participantName, null);
            participantDevice.addIdentifier(deviceIdentifier);
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [Created and Set Primary Device Identifier] Finish");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [Set DistinctIdentifier] Start");
            String participantFullName = participant.getParticipantId().getFullName();
            participantDevice.setDistinctIdentifier(participantFullName);
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [Set DistinctIdentifier] Finish");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [If WUP, Create Reference to ProcessingPlant Device] Start");
            String subsystemName = participant.getParticipantId().getSubsystemName();
            if (!subsystemName.contentEquals(participantName)) {
                Reference subsystemReference = mapPetasosSubsystemToDeviceReference(subsystemName);
                participantDevice.setParent(subsystemReference);
            }
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [If WUP, Create Reference to ProcessingPlant Device] Finish");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [Set the Device Status] Start");
            Device.FHIRDeviceStatus deviceStatus = deviceStatusFactory.mapParticipantStatusToDeviceStatus(participant.getParticipantStatus());
            participantDevice.setStatus(deviceStatus);
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [Set the Device Status] Finish");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [Set the Device Status Reason (Device Status)] Start");
            CodeableConcept deviceStatusReason = deviceStatusFactory.mapParticipantStatusToDeviceStatusReason(participant.getParticipantStatus());
            participantDevice.addStatusReason(deviceStatusReason);
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [Set the Device Status Reason (Device Status)] Finish");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [Set the Device Status Reason (Device Control Status)] Start");
            CodeableConcept deviceControlStatusReason = deviceStatusFactory.mapParticipantControlStatusToDeviceStatusReason(participant.getParticipantControlStatus());
            participantDevice.addStatusReason(deviceControlStatusReason);
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [Set the Device Status Reason (Device Control Status)] Finish");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [Set the Device Component Name] Start");
            Device.DeviceDeviceNameComponent displayNameComponent = new Device.DeviceDeviceNameComponent();
            displayNameComponent.setName(participant.getParticipantId().getDisplayName());
            displayNameComponent.setType(Device.DeviceNameType.USERFRIENDLYNAME);
            participantDevice.addDeviceName(displayNameComponent);
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [Set the Device Component Name] Finish");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [Set the Device Type] Start");
            CodeableConcept deviceType = new CodeableConcept();
            Coding deviceTypeCoding = new Coding();
            deviceTypeCoding.setSystem(getDRICATSDeviceTypeSystem());
            if (participant.getParticipantId().getName().contentEquals(participant.getParticipantId().getSubsystemName())) {
                deviceTypeCoding.setCode(PegacornSystemComponentTypeTypeEnum.PROCESSING_PLANT.getToken());
                deviceTypeCoding.setDisplay(PegacornSystemComponentTypeTypeEnum.PROCESSING_PLANT.getDisplayName());
            } else {
                deviceTypeCoding.setCode(PegacornSystemComponentTypeTypeEnum.WUP.getToken());
                deviceTypeCoding.setDisplay(PegacornSystemComponentTypeTypeEnum.WUP.getDisplayName());
            }
            deviceType.addCoding(deviceTypeCoding);
            deviceType.setText("A participant in the distributed, resilient task processing framework");
            participantDevice.setType(deviceType);
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [Set the Device Type] Finish");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [Set the Device Model] Start");
            participantDevice.setModelNumber(participant.getParticipantId().getName());
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [Set the Device Model] Finish");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [Add the Subscription Extensions] Start");
            List<Extension> subscriptionExtensionList = deviceSubscriptionFactory.mapSubscriptionToExtensionList(participant.getSubscriptions());
            if (subscriptionExtensionList != null && !subscriptionExtensionList.isEmpty()) {
                participantDevice.getExtension().addAll(subscriptionExtensionList);
            }
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] [Add the Subscription Extensions] Finish");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Participant->Device] Finish");

            getLogger().debug(".mapPetasosParticipantToDevice(): Exit, participantDevice->{}", participantDevice);
            return (participantDevice);
        } catch (Exception ex) {

        }
        getLogger().debug(".mapPetasosParticipantToDevice(): Exit, returning -null-");
        return (null);
    }

    public Reference mapPetasosSubsystemToDeviceReference(String subsystemName) {
        getLogger().debug(".mapPetasosSubsystemToDeviceReference(): Entry, subsystemName->{}", subsystemName);
        if (StringUtils.isEmpty(subsystemName)) {
            getLogger().debug(".mapPetasosSubsystemToDeviceReference(): Exit, subsystem name is empty, returning -null-");
            return (null);
        }
        Reference subsystemReference = new Reference();
        Identifier subsystemIdentifier = getIdentifierFactory().newIdentifier(PegacornIdentifierCodeEnum.IDENTIFIER_CODE_SOFTWARE_PARTICIPANT, subsystemName, null);
        subsystemReference.setIdentifier(subsystemIdentifier);
        subsystemReference.setType(ResourceType.Device.name());
        getLogger().debug(".mapPetasosSubsystemToDeviceReference(): Exit, subsystemReference->{}", subsystemReference);
        return (subsystemReference);
    }


    //
    // Business Methods (from FHIR)
    //

    public PetasosParticipant mapFHIRDeviceToPetasosParticipant(Device device) {
        getLogger().debug(".mapFHIRDeviceToPetasosParticipant(): Entry, device->{}", device);
        try {
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Device->Participant] Start");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Device->Participant] [Create Empty PetasosParticipant] Start");
            PetasosParticipant participant = new PetasosParticipant();
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Device->Participant] [Create Empty PetasosParticipant] Start");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Device->Participant] [Create ParticipantId.setName()] Start");
            Identifier primaryIdentifier = getPrimaryParticipantIdentifier(device.getIdentifier());
            participant.setParticipantName(primaryIdentifier.getValue());
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Device->Participant] [Create ParticipantId.setName()] finish");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Device->Participant] [Set Resource Id] Start");
            DataGridResourceIdType dataGridResourceId = new DataGridResourceIdType();
            dataGridResourceId.setResourceId(device.getIdElement());
            dataGridResourceId.setPrimaryBusinessIdentifier(primaryIdentifier);
            participant.setDataGridId(dataGridResourceId);
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Device->Participant] [Set Resource Id] Start");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Device->Participant] [Create ParticipantId.setDisplayName()] Start");
            String displayName = getDisplayNameFromDeviceNameType(device.getDeviceName());
            participant.setParticipantDisplayName(displayName);
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Device->Participant] [Create ParticipantId.setDisplayName()] Finish");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Device->Participant] [Create ParticipantId.setSubsystemName()] Start");
            if (device.hasParent()) {
                String subsystemName = mapDeviceParentReferenceToSubsystemName(device.getParent());
                participant.setSubsystemParticipantName(subsystemName);
            } else {
                participant.setSubsystemParticipantName(participant.getParticipantName());
            }
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Device->Participant] [Create ParticipantId.setSubsystemName()] Finish");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Device->Participant] [Set participant.setParticipantStatus()] Start");
            PetasosParticipantStatusEnum petasosParticipantStatusEnum = deviceStatusFactory.mapDeviceStatusReasonToParticipantStatus(device.getStatusReason());
            if (petasosParticipantStatusEnum != null) {
                participant.setParticipantStatus(petasosParticipantStatusEnum);
            }
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Device->Participant] [Set participant.setParticipantStatus()] Finish");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Device->Participant] [Set Control Status] Start");
            PetasosParticipantControlStatusEnum controlStatus = deviceStatusFactory.mapDeviceStatusReasonToParticipantControlStatus(device.getStatusReason());
            if (controlStatus != null) {
                participant.setParticipantControlStatus(controlStatus);
            } else {
                participant.setParticipantControlStatus(PetasosParticipantControlStatusEnum.PARTICIPANT_IS_SUSPENDED);
            }
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Device->Participant] [Set Control Status] Finish");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Device->Participant] [Set Subscriptions] Start");
            Set<TaskWorkItemSubscriptionType> subscriptionSet = deviceSubscriptionFactory.mapSubscriptionExtensionsToTaskWorkItemSubscriptions(device.getExtension());
            if (participant.getSubscriptions() == null) {
                participant.setSubscriptions(new HashSet<>());
            }
            if (subscriptionSet != null && !subscriptionSet.isEmpty()) {
                participant.getSubscriptions().addAll(subscriptionSet);
            }
            getLogger().trace(".mapPetasosParticipantToDevice():[Map Device->Participant] [Set Subscriptions] Finish");

            getLogger().trace(".mapPetasosParticipantToDevice():[Map Device->Participant] Finish");

            getLogger().warn(".mapPetasosParticipantToDevice():Exit, participant->{}", participant);
            return (participant);
        } catch (Exception ex) {
            getLogger().warn(".mapFHIRDeviceToPetasosParticipant(): Problem ->", ex);
        }

        getLogger().debug(".mapFHIRDeviceToPetasosParticipant(): Exit, participant -> null");
        return (null);
    }

    public String getDisplayNameFromDeviceNameType(List<Device.DeviceDeviceNameComponent> deviceNameList) {
        if (deviceNameList != null) {
            for (Device.DeviceDeviceNameComponent currentDeviceNameType : deviceNameList) {
                if (currentDeviceNameType.getType().equals(Device.DeviceNameType.USERFRIENDLYNAME)) {
                    String displayName = currentDeviceNameType.getName();
                    return (displayName);
                }
            }
        }
        return (null);
    }

    public Identifier getPrimaryParticipantIdentifier(List<Identifier> identifierList) {
        if (identifierList != null) {
            for (Identifier currentIdentifier : identifierList) {
                CodeableConcept currentIdentifierCC = currentIdentifier.getType();
                for (Coding currentIdentifierCoding : currentIdentifierCC.getCoding()) {
                    if (currentIdentifierCoding.getCode().contentEquals(PegacornIdentifierCodeEnum.IDENTIFIER_CODE_SOFTWARE_PARTICIPANT.getToken())) {
                        return (currentIdentifier);
                    }
                }
            }
        }
        return (null);
    }

    public String mapDeviceParentReferenceToSubsystemName(Reference deviceParent) {
        getLogger().debug(".mapDeviceParentReferenceToSubsystemName(): Entry, deviceParent->{}", deviceParent);
        if (deviceParent == null) {
            getLogger().debug(".mapDeviceParentReferenceToSubsystemName(): Exit, deviceParent is empty, returning -null-");
            return (null);
        }
        Reference subsystemReference = new Reference();
        Identifier subsystemIdentifier = deviceParent.getIdentifier();
        String subsystemName = subsystemIdentifier.getValue();
        getLogger().debug(".mapDeviceParentReferenceToSubsystemName(): Exit, subsystemName->{}", subsystemName);
        return (subsystemName);
    }

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (LOG);
    }

    protected DeviceMetaTagFactory getMetaTagFactory() {
        return (metaTagFactory);
    }

    protected DevicePropertyFactory getPropertyFactory() {
        return (propertyFactory);
    }

    protected PegacornIdentifierFactory getIdentifierFactory() {
        return (identifierFactory);
    }

    protected String getDRICATSDeviceTypeSystem() {
        String system = systemWideProperties.getPegacornCodeSystemSite() + PEGACORN_SOFTWARE_COMPONENT_DEVICE_TYPE_SYSTEM;
        return (system);
    }
}
