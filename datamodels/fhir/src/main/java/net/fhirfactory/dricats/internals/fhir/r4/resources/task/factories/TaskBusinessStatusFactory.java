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
import net.fhirfactory.dricats.core.model.task.datatypes.fulfillment.valuesets.FulfillmentExecutionStatusEnum;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class TaskBusinessStatusFactory {

    private static final String DRICATS_TASK_BUSINESS_STATUS_SYSTEM = "/task-business-status";
    @Inject
    private PegacornReferenceProperties systemWideProperties;

    //
    // Business Methods
    //

    public String getPegacornTaskBusinessStatusSystem() {
        String codeSystem = systemWideProperties.getPegacornCodeSystemSite() + DRICATS_TASK_BUSINESS_STATUS_SYSTEM;
        return (codeSystem);
    }

    // TODO This doesn't seem right at all... It should contain more info
    public CodeableConcept newBusinessStatus(FulfillmentExecutionStatusEnum outcomeStatus) {
        CodeableConcept businessStatusCC = new CodeableConcept();
        Coding taskBusinessStatusCoding = new Coding();
        taskBusinessStatusCoding.setSystem(getPegacornTaskBusinessStatusSystem());
        taskBusinessStatusCoding.setCode(outcomeStatus.getToken());
        taskBusinessStatusCoding.setDisplay(outcomeStatus.getDisplayName());
        businessStatusCC.setText(outcomeStatus.getDisplayName());
        businessStatusCC.addCoding(taskBusinessStatusCoding);

        return (businessStatusCC);
    }
}
