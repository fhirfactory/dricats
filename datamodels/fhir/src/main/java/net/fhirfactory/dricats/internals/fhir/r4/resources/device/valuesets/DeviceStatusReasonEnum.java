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
package net.fhirfactory.dricats.internals.fhir.r4.resources.device.valuesets;

import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;

public enum DeviceStatusReasonEnum {
    DEVICE_STATUS_ONLINE("online", "Online", "The device is online."),
    DEVICE_STATUS_PAUSED("paused", "Paused", "The device is paused."),
    DEVICE_STATUS_STANDBY("standby", "Standby", "The device is ready but not actively operating."),
    DEVICE_STATUS_OFFLINE("offline", "Offline", "The device is offline."),
    DEVICE_STATUS_NOT_READY("not-ready", "Not Ready", "The device is not ready."),
    DEVICE_STATUS_OFF("off", "Off", "The device is off.");

    public static final String SYSTEM = "http://hl7.org/fhir/codesystem-device-status-reason.html";
    private String code;
    private String display;
    private String definition;

    private DeviceStatusReasonEnum(String code, String display, String definition) {
        this.code = code;
        this.display = display;
        this.definition = definition;
    }

    public static DeviceStatusReasonEnum fromCode(String token) {
        if (StringUtils.isNotEmpty(token)) {
            for (DeviceStatusReasonEnum currentReason : DeviceStatusReasonEnum.values()) {
                if (currentReason.getCode().contentEquals(token)) {
                    return (currentReason);
                }
            }
        }
        return (null);
    }

    public String getCode() {
        return code;
    }

    public String getDisplay() {
        return display;
    }

    public String getDefinition() {
        return definition;
    }

    public CodeableConcept toCodeableConcept() {
        CodeableConcept statusReason = new CodeableConcept();
        Coding statusReasonCoding = new Coding();

        statusReasonCoding.setSystem(SYSTEM);
        statusReasonCoding.setDisplay(display);
        statusReasonCoding.setCode(code);
        statusReason.addCoding(statusReasonCoding);
        statusReason.setText(definition);

        return (statusReason);
    }
}
