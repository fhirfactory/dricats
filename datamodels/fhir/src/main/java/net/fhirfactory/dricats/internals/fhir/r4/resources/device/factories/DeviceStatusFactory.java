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
package net.fhirfactory.dricats.internals.fhir.r4.resources.device.factories;

import net.fhirfactory.dricats.core.constants.systemwide.PegacornReferenceProperties;
import net.fhirfactory.dricats.core.model.participant.PetasosParticipantControlStatusEnum;
import net.fhirfactory.dricats.core.model.participant.PetasosParticipantStatusEnum;
import net.fhirfactory.dricats.internals.fhir.r4.resources.device.valuesets.DeviceStatusReasonEnum;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class DeviceStatusFactory {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceStatusFactory.class);

    private static final String DRICATS_TASK_REASON_SYSTEM = "/device-control-status";

    @Inject
    private PegacornReferenceProperties systemWideProperties;

    //
    // Constructor(s)
    //

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (LOG);
    }

    public String getDeviceControlStatusReasonSystemURL() {
        String system = systemWideProperties.getPegacornCodeSystemSite() + DRICATS_TASK_REASON_SYSTEM;
        return (system);
    }

    //
    // Business Methods
    //

    public Device.FHIRDeviceStatus mapParticipantStatusToDeviceStatus(PetasosParticipantStatusEnum participantStatusEnum) {
        if (participantStatusEnum == null) {
            return (null);
        }
        Device.FHIRDeviceStatus deviceStatus = null;
        switch (participantStatusEnum) {
            case PETASOS_PARTICIPANT_IDLE:
            case PETASOS_PARTICIPANT_STARTING:
            case PETASOS_PARTICIPANT_STOPPING:
            case PETASOS_PARTICIPANT_ACTIVE:
                deviceStatus = Device.FHIRDeviceStatus.ACTIVE;
                break;
            case PETASOS_PARTICIPANT_FAILED:
                deviceStatus = Device.FHIRDeviceStatus.INACTIVE;
                break;
        }
        return (deviceStatus);
    }

    public Coding createDeviceStatusCoding(Device.FHIRDeviceStatus status) {
        Coding deviceStatusCoding = new Coding();
        deviceStatusCoding.setCode(status.toCode());
        deviceStatusCoding.setSystem(status.getSystem());
        deviceStatusCoding.setDisplay(status.getDisplay());
        return (deviceStatusCoding);
    }

    public CodeableConcept mapParticipantStatusToDeviceStatusReason(PetasosParticipantStatusEnum participantStatusEnum) {
        if (participantStatusEnum == null) {
            return (null);
        }
        CodeableConcept deviceStatusReason = null;
        switch (participantStatusEnum) {
            case PETASOS_PARTICIPANT_IDLE:
                deviceStatusReason = DeviceStatusReasonEnum.DEVICE_STATUS_STANDBY.toCodeableConcept();
                break;
            case PETASOS_PARTICIPANT_STARTING:
            case PETASOS_PARTICIPANT_STOPPING:
                deviceStatusReason = DeviceStatusReasonEnum.DEVICE_STATUS_NOT_READY.toCodeableConcept();
                break;
            case PETASOS_PARTICIPANT_ACTIVE:
                deviceStatusReason = DeviceStatusReasonEnum.DEVICE_STATUS_ONLINE.toCodeableConcept();
                break;
            case PETASOS_PARTICIPANT_FAILED:
                deviceStatusReason = DeviceStatusReasonEnum.DEVICE_STATUS_OFFLINE.toCodeableConcept();
                break;
        }
        return (deviceStatusReason);
    }

    public CodeableConcept mapParticipantControlStatusToDeviceStatusReason(PetasosParticipantControlStatusEnum controlStatusEnum) {
        if (controlStatusEnum == null) {
            return (null);
        }
        CodeableConcept deviceControlStatusReason = new CodeableConcept();
        Coding deviceControlStatusReasonCoding = new Coding();
        deviceControlStatusReasonCoding.setSystem(getDeviceControlStatusReasonSystemURL());
        deviceControlStatusReasonCoding.setCode(controlStatusEnum.getToken());
        deviceControlStatusReasonCoding.setDisplay(controlStatusEnum.getName());
        deviceControlStatusReason.addCoding(deviceControlStatusReasonCoding);
        deviceControlStatusReason.setText(controlStatusEnum.getDisplayText());
        return (deviceControlStatusReason);
    }

    public PetasosParticipantControlStatusEnum mapDeviceStatusReasonToParticipantControlStatus(List<CodeableConcept> deviceStatusReasonList) {
        if (deviceStatusReasonList == null || deviceStatusReasonList.isEmpty()) {
            return (null);
        }
        for (CodeableConcept currentCC : deviceStatusReasonList) {
            for (Coding currentCoding : currentCC.getCoding()) {
                if (currentCoding.getSystem().contentEquals(getDeviceControlStatusReasonSystemURL())) {
                    String controlStatusValue = currentCoding.getCode();
                    PetasosParticipantControlStatusEnum controlStatusEnum = PetasosParticipantControlStatusEnum.fromCode(controlStatusValue);
                    return (controlStatusEnum);
                }
            }
        }
        return (null);
    }

    public PetasosParticipantStatusEnum mapDeviceStatusReasonToParticipantStatus(List<CodeableConcept> deviceStatusReasonList) {
        if (deviceStatusReasonList != null && !deviceStatusReasonList.isEmpty()) {
            for (CodeableConcept currentReason : deviceStatusReasonList) {
                for (Coding currentReasonCoding : currentReason.getCoding()) {
                    if (currentReasonCoding.getSystem().contentEquals(DeviceStatusReasonEnum.SYSTEM)) {
                        DeviceStatusReasonEnum deviceStatusReasonEnum = DeviceStatusReasonEnum.fromCode(currentReasonCoding.getCode());
                        switch (deviceStatusReasonEnum) {
                            case DEVICE_STATUS_ONLINE:
                                return (PetasosParticipantStatusEnum.PETASOS_PARTICIPANT_ACTIVE);
                            case DEVICE_STATUS_PAUSED:
                                break;
                            case DEVICE_STATUS_STANDBY:
                                return (PetasosParticipantStatusEnum.PETASOS_PARTICIPANT_IDLE);
                            case DEVICE_STATUS_OFFLINE:
                                return (PetasosParticipantStatusEnum.PETASOS_PARTICIPANT_FAILED);
                            case DEVICE_STATUS_NOT_READY:
                                return (PetasosParticipantStatusEnum.PETASOS_PARTICIPANT_STARTING);
                            case DEVICE_STATUS_OFF:
                                break;
                        }
                    }
                }
            }
        }
        return (null);
    }
}
