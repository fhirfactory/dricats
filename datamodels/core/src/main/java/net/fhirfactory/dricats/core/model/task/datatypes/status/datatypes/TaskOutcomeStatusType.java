/*
 * Copyright (c) 2020 Mark A. Hunter (ACT Health)
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
package net.fhirfactory.dricats.core.model.task.datatypes.status.datatypes;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.fhirfactory.dricats.core.constants.petasos.PetasosPropertyConstants;
import net.fhirfactory.dricats.core.model.task.datatypes.status.valuesets.TaskOutcomeStatusEnum;
import net.fhirfactory.dricats.core.model.task.datatypes.status.valuesets.TaskOutcomeStatusReasonEnum;

import java.io.Serializable;
import java.time.Instant;

public class TaskOutcomeStatusType implements Serializable {

    private TaskOutcomeStatusEnum outcomeStatus;
    private TaskOutcomeStatusReasonEnum outcomeStatusReason;
    private String outcomeReasonDescription;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSXXX", timezone = PetasosPropertyConstants.DEFAULT_TIMEZONE)
    private Instant entryInstant;

    //
    // Constructor(s)
    //

    public TaskOutcomeStatusType() {
        super();
        entryInstant = Instant.now();
        this.outcomeStatus = TaskOutcomeStatusEnum.PETASOS_TASK_OUTCOME_STATUS_UNKNOWN;
        this.outcomeStatusReason = null;
        this.outcomeReasonDescription = null;
    }

    //
    // Getters and Setters (Bean Methods)
    //

    public TaskOutcomeStatusReasonEnum getOutcomeStatusReason() {
        return outcomeStatusReason;
    }

    public void setOutcomeStatusReason(TaskOutcomeStatusReasonEnum outcomeStatusReason) {
        this.outcomeStatusReason = outcomeStatusReason;
    }

    public String getOutcomeReasonDescription() {
        return outcomeReasonDescription;
    }

    public void setOutcomeReasonDescription(String outcomeReasonDescription) {
        this.outcomeReasonDescription = outcomeReasonDescription;
    }

    public Instant getEntryInstant() {
        return entryInstant;
    }

    public void setEntryInstant(Instant entryInstant) {
        this.entryInstant = entryInstant;
    }

    public TaskOutcomeStatusEnum getOutcomeStatus() {
        return outcomeStatus;
    }

    public void setOutcomeStatus(TaskOutcomeStatusEnum outcomeStatus) {
        this.outcomeStatus = outcomeStatus;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TaskOutcomeStatusType{");
        sb.append("outcomeStatus=").append(outcomeStatus);
        sb.append(", outcomeStatusReason=").append(outcomeStatusReason);
        sb.append(", outcomeReasonDescription='").append(outcomeReasonDescription).append('\'');
        sb.append(", entryInstant=").append(entryInstant);
        sb.append('}');
        return sb.toString();
    }
}
