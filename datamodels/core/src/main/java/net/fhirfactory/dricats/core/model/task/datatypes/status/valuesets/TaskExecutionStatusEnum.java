/*
 * Copyright (c) 2020 MAHun
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

/**
 * @author ACT Health (Mark A. Hunter)
 */
public enum TaskExecutionStatusEnum {
    PETASOS_TASK_EXECUTION_STATUS_QUEUED("Queued", "dricats.petasos.task.execution_status.queued", "Task is Queued"),
    PETASOS_TASK_EXECUTION_STATUS_ASSIGNED("Assigned", "dricats.petasos.task.execution_status.assigned", "Task is Assigned to a WorkUnitProcessor"),
    PETASOS_TASK_EXECUTION_STATUS_CANCELLED("Cancelled", "dricats.pegasos.task.execution_status.cancelled", "Task is Cancelled"),
    PETASOS_TASK_EXECUTION_STATUS_FAILED("Failed", "dricats.pegasos.task.execution_status.failed", "Task has Failed"),
    PETASOS_TASK_EXECUTION_STATUS_FINISHED("Finished", "dricats.pegasos.task.execution_status.finished", "Task has Finished"),
    PETASOS_TASK_EXECUTION_STATUS_EXECUTING("Executing", "dricats.petasos.task.execution_status.executing", "Task is being actively processed"),
    PETASOS_TASK_EXECUTION_STATUS_FINALISED("Finalised", "dricats.petasos.task.execution_status.finalised", "Task has been Finalised");

    private String code;
    private String name;
    private String displayText;

    private TaskExecutionStatusEnum(String name, String code, String displayText) {
        this.name = name;
        this.code = code;
        this.displayText = displayText;
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
