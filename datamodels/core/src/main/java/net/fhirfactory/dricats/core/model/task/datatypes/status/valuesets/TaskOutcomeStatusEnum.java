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

/**
 * @author ACT Health (Mark A. Hunter)
 */
public enum TaskOutcomeStatusEnum {
    PETASOS_TASK_OUTCOME_STATUS_UNKNOWN("Outcome Unknown", "petasos.task.outcome_status.unknown"),
    PETASOS_TASK_OUTCOME_STATUS_WAITING("Waiting", "petasos.task.outcome_status.waiting"),
    PETASOS_TASK_OUTCOME_STATUS_CANCELLED("Cancelled", "petasos.task.outcome_status.cancelled"),
    PETASOS_TASK_OUTCOME_STATUS_ACTIVE("Active", "petasos.task.outcome_status.active"),
    PETASOS_TASK_OUTCOME_STATUS_FINISHED("Finished", "petasos.task.outcome_status.finished"),
    PETASOS_TASK_OUTCOME_STATUS_FINALISED("Finalised", "petasos.task.outcome_status.finalised"),
    PETASOS_TASK_OUTCOME_STATUS_FAILED("Failed", "petasos.task.outcome_status.failed");

    private String code;
    private String name;

    private TaskOutcomeStatusEnum(String name, String executionStatus) {
        this.code = executionStatus;
        this.name = name;
    }

    public static TaskOutcomeStatusEnum fromCode(String code) {
        if (StringUtils.isNotEmpty(code)) {
            for (TaskOutcomeStatusEnum currentOutcomeStatus : TaskOutcomeStatusEnum.values()) {
                if (currentOutcomeStatus.getCode().contentEquals(code)) {
                    return (currentOutcomeStatus);
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
}
