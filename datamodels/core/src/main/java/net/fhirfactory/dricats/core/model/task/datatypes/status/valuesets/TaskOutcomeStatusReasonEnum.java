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
package net.fhirfactory.dricats.core.model.task.datatypes.status.valuesets;

import org.apache.commons.lang3.StringUtils;

public enum TaskOutcomeStatusReasonEnum {
    PETASOS_TASK_OUTCOME_REASON_FAILURE_GENERAL("Unspecified Failure", "dricats.petasos.task.outcome_status_reason.unspecified_failure", "Unspecified Failure Condition"),
    PETASOS_TASK_OUTCOME_REASON_FAILURE_NETWORK_CONNECT_ERROR("Network Connection Error", "dricats.petasos.task.outcome_status_reason.network_connection_error", "Unable to connect to remote system"),
    PETASOS_TASK_OUTCOME_REASON_FAILURE_NEGATIVE_ACKNOWLEDGEMENT("Negative Acknowledgement Received", "dricats.petasos.task.outcome_status_reason.negative_acknowledgment", "Negative Acknowledgement received on Transaction"),
    PETASOS_TASK_OUTCOME_REASON_FAILURE_FILE_IO("File I/O Error", "dricats.petasos.task.outcome_status_reason.file_io_error", "File Reading/Writing Error"),

    PETASOS_TASK_OUTCOME_REASON_FILTERED_BY_CODE("Filtered", "dricats.petasos.task.outcome_status_reason.filtered", "Task is Filtered/Suppressed via Transformation Code"),

    PETASOS_TASK_OUTCOME_REASON_CANCELLATION_GENERAL("Cancelled for Unknown Reason", "dricats.petasos.task.outcome_status_reason.unspecified_cancellation", "Unspecified Cancellation Condition"),
    PETASOS_TASK_OUTCOME_REASON_CANCELLATION_BY_USER("User Cancelled Task", "dricats.petasos.task.outcome_status_reason.administrator_cancelled", "A User/Administrator Cancelled the Task"),
    PETASOS_TASK_OUTCOME_REASON_CANCELLATION_DUE_TO_PARTICIPANT_DISABLED("Pod/Participant Disabled", "dricats.petasos.task.outcome_status_reason.participant_disabled", "The Pod/Participant is Disabled");

    private String code;
    private String name;
    private String displayText;

    private TaskOutcomeStatusReasonEnum(String name, String code, String displayText) {
        this.name = name;
        this.code = code;
        this.displayText = displayText;
    }

    public static TaskOutcomeStatusReasonEnum fromCode(String code) {
        if (StringUtils.isNotEmpty(code)) {
            for (TaskOutcomeStatusReasonEnum currentStatusReason : TaskOutcomeStatusReasonEnum.values()) {
                if (currentStatusReason.getCode().contentEquals(code)) {
                    return (currentStatusReason);
                }
            }
        }
        return (null);
    }

    public String getCode() {
        return (this.code);
    }

    public String getName() {
        return (this.name);
    }

    public String getDisplayText() {
        return (this.displayText);
    }
}
