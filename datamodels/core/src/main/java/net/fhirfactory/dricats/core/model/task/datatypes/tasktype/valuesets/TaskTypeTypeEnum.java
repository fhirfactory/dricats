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
package net.fhirfactory.dricats.core.model.task.datatypes.tasktype.valuesets;

import org.apache.commons.lang3.StringUtils;

public enum TaskTypeTypeEnum {
    PETASOS_BASE_TASK_TYPE("Task", "petasos.task_type.base_task"),
    PETASOS_ACTIONABLE_TASK_TYPE("ActionableTask", "petasos.task_type.actionable_task"),
    PETASOS_FULFILLMENT_TASK_TYPE("FulfillmentTask", "petasos.task_type.fulfillment_task"),
    PETASOS_AGGREGATE_TASK_TYPE("OversightTask", "petasos.task_type.oversight_task");

    private String displayName;
    private String token;

    private TaskTypeTypeEnum(String name, String token) {
        this.displayName = name;
        this.token = token;
    }

    public static TaskTypeTypeEnum fromTokenValue(String tokenValue) {
        if (StringUtils.isEmpty(tokenValue)) {
            return (null);
        }
        for (TaskTypeTypeEnum currentType : TaskTypeTypeEnum.values()) {
            if (tokenValue.contentEquals(currentType.token)) {
                return (currentType);
            }
        }
        return (null);
    }

    public String getTaskTypeDisplayName() {
        return (displayName);
    }

    public String getTaskTypeToken() {
        return (token);
    }
}
