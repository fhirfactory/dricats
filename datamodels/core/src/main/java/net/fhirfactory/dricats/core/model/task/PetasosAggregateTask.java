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
package net.fhirfactory.dricats.core.model.task;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import lombok.extern.slf4j.Slf4j;
import net.fhirfactory.dricats.core.model.task.datatypes.identity.datatypes.TaskIdType;
import net.fhirfactory.dricats.core.model.task.datatypes.reporting.datatypes.TaskReportingType;
import net.fhirfactory.dricats.core.model.task.datatypes.status.datatypes.AggregateTaskStatusType;
import net.fhirfactory.dricats.core.model.task.datatypes.tasktype.TaskTypeType;
import net.fhirfactory.dricats.core.model.task.datatypes.tasktype.valuesets.TaskTypeTypeEnum;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Slf4j
@Jacksonized
public class PetasosAggregateTask extends PetasosTask {

    private TaskReportingType taskReporting;

    private AggregateTaskStatusType aggregateTaskStatus;

    private TaskIdType actionableTaskId;

    private Set<TaskIdType> subTasks;

    //
    // Constructor(s)
    //

    public PetasosAggregateTask() {
        super();
        this.taskReporting = null;
        this.aggregateTaskStatus = null;
        this.actionableTaskId = null;
        this.subTasks = new HashSet<>();
        setTaskType(new TaskTypeType(TaskTypeTypeEnum.PETASOS_AGGREGATE_TASK_TYPE));
    }

    //
    // has Methods
    //

    public boolean hasTaskReporting() {
        return taskReporting != null;
    }

    public boolean hasAggregateTaskStatus() {
        return aggregateTaskStatus != null;
    }

    public boolean hasActionableTaskId() {
        return actionableTaskId != null;
    }

    public boolean hasSubTasks() {
        boolean hasValue = subTasks != null;
        if(hasValue){
            if(subTasks.size() > 0){
                return(true);
            }
        }
        return(hasValue);
    }
}
