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
package net.fhirfactory.dricats.core.model.task.datatypes.reporting.datatypes;

import net.fhirfactory.dricats.core.model.task.datatypes.reporting.valuesets.TaskOutcomeReportingEnum;
import net.fhirfactory.dricats.core.model.task.datatypes.common.TaskInstantDetailSegmentBase;

public class TaskReportingType extends TaskInstantDetailSegmentBase {
    private TaskOutcomeReportingEnum outcome;

    //
    // Constructor(s)
    //

    public TaskReportingType() {
        super();
    }

    public TaskReportingType(TaskReportingType ori) {
        super(ori);
    }

    //
    // Getters and Setters (Bean Methods)
    //

    public boolean hasOutcome() {
        boolean hasValue = this.outcome != null;
        return (hasValue);
    }

    public TaskOutcomeReportingEnum getOutcome() {
        return outcome;
    }

    public void setOutcome(TaskOutcomeReportingEnum outcome) {
        this.outcome = outcome;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "ReportingSegment{" +
                "registrationInstant=" + getRegistrationInstant() +
                ", readyInstant=" + getReadyInstant() +
                ", startInstant=" + getStartInstant() +
                ", finishInstant=" + getFinishInstant() +
                ", finalisationInstant=" + getFinalisationInstant() +
                ", lastCheckedInstant=" + getLastCheckedInstant() +
                ", outcome=" + outcome +
                '}';
    }
}
