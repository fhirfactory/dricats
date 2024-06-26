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

import org.apache.commons.lang3.StringUtils;

public enum PetasosParticipantControlStatusEnum {
    PARTICIPANT_IS_IN_ERROR("InError", "Participant-is-In-Error", "dricats.petasos.participant.operational_status.error"),
    PARTICIPANT_IS_ENABLED("Enabled", "Participant-is-Enabled", "dricats.petasos.participant.operational_status.enabled"),
    PARTICIPANT_IS_SUSPENDED("Suspended", "Participant-is-Suspended", "dricats.petasos.participant.operational_status.suspended"),
    PARTICIPANT_IS_DISABLED("Disabled", "Participant-is-Disabled", "dricats.petasos.participant.operational_status.disabled");

    private String name;
    private String displayText;
    private String token;

    private PetasosParticipantControlStatusEnum(String name, String displayText, String token) {
        this.name = name;
        this.displayText = displayText;
        this.token = token;
    }

    public static PetasosParticipantControlStatusEnum fromCode(String code) {
        if (StringUtils.isNotEmpty(code)) {
            for (PetasosParticipantControlStatusEnum currentControlStatus : PetasosParticipantControlStatusEnum.values()) {
                if (currentControlStatus.getToken().contentEquals(code)) {
                    return (currentControlStatus);
                }
            }
        }
        return (null);
    }

    public String getName() {
        return name;
    }

    public String getDisplayText() {
        return displayText;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return "PetasosParticipantControlStatusEnum{" +
                "name=" + name +
                '}';
    }
}