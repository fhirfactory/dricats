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
package net.fhirfactory.dricats.internals.fhir.r4.resources.task.factories;

import net.fhirfactory.dricats.core.constants.systemwide.PegacornReferenceProperties;
import net.fhirfactory.dricats.core.model.task.datatypes.status.valuesets.TaskOutcomeStatusReasonEnum;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.*;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.Date;

@ApplicationScoped
public class TaskStatusReasonFactory {

    private static final String DRICATS_TASK_STATUS_REASON_SYSTEM = "/task-status-reason";
    private static final String DRICATS_TASK_STATUS_REASON_DESCRIPTION_EXTENSION_SYSTEM = "/extension/task-status-reason/reason-description";
    private static final String DRICATS_TASK_STATUS_REASON_ENTRY_INSTANT_EXTENSION_SYSTEM = "/extension/task-status-reason/reason-entry-instant";
    private static final String DRICATS_TASK_IS_FINALISED_CODE = "dricats.task.status.reason.finalised";
    private static final String DRICATS_TASK_IS_NOT_FINALISED_CODE = "dricats.task.status.reason.not-finalised";
    @Inject
    private PegacornReferenceProperties systemWideProperties;
    //
    // Business Methods
    //

    public String getTaskIsFinalisedCode() {
        return (DRICATS_TASK_IS_FINALISED_CODE);
    }

    public String getTaskIsNotFinalisedCode() {
        return (DRICATS_TASK_IS_NOT_FINALISED_CODE);
    }

    public String getTaskStatusReasonSystem() {
        String codeSystem = systemWideProperties.getPegacornCodeSystemSite() + DRICATS_TASK_STATUS_REASON_SYSTEM;
        return (codeSystem);
    }

    public String getDricatsTaskStatusReasonDescriptionExtensionSystem() {
        String system = systemWideProperties.getPegacornCodeSystemSite() + DRICATS_TASK_STATUS_REASON_DESCRIPTION_EXTENSION_SYSTEM;
        return (system);
    }

    public String getDricatsTaskStatusReasonEntryInstantExtensionSystem() {
        String system = systemWideProperties.getPegacornCodeSystemSite() + DRICATS_TASK_STATUS_REASON_ENTRY_INSTANT_EXTENSION_SYSTEM;
        return (system);
    }

    public CodeableConcept newTaskStatusReason(TaskOutcomeStatusReasonEnum taskStatusReason, String statusReasonDescription, Instant entryInstant) {
        CodeableConcept taskReasonCC = new CodeableConcept();
        Coding taskReasonCoding = new Coding();
        taskReasonCoding.setSystem(getTaskStatusReasonSystem());
        taskReasonCoding.setCode(taskStatusReason.getCode());
        taskReasonCoding.setDisplay(taskStatusReason.getName());
        taskReasonCC.setText(taskStatusReason.getDisplayText());
        taskReasonCC.addCoding(taskReasonCoding);

        // Add description extension if present
        if (StringUtils.isNotEmpty(statusReasonDescription)) {
            Extension statusReasonExtension = new Extension();
            statusReasonExtension.setUrl(getDricatsTaskStatusReasonDescriptionExtensionSystem());
            StringType statusReasonDescriptionStringType = new StringType(statusReasonDescription);
            statusReasonExtension.setValue(statusReasonDescriptionStringType);
            taskReasonCC.addExtension(statusReasonExtension);
        }

        // Add entryInstant extension if present
        if (entryInstant != null) {
            try {
                Extension statusReasonEntryInstantExtension = new Extension();
                statusReasonEntryInstantExtension.setUrl(getDricatsTaskStatusReasonEntryInstantExtensionSystem());
                InstantType entryInstantType = new InstantType(Date.from(entryInstant));
                statusReasonEntryInstantExtension.setValue(entryInstantType);
                taskReasonCC.addExtension(statusReasonEntryInstantExtension);
            } catch (Exception ex) {
                // do nothing...
            }
        }
        return (taskReasonCC);
    }


}
