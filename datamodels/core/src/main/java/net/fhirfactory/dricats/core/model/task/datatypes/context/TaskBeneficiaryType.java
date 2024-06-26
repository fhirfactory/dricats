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
package net.fhirfactory.dricats.core.model.task.datatypes.context;

import net.fhirfactory.dricats.core.model.task.datatypes.context.valuesets.TaskBeneficiaryTypeTypeEnum;
import org.hl7.fhir.r4.model.Reference;

public class TaskBeneficiaryType extends Reference {
    private TaskBeneficiaryTypeTypeEnum beneficiaryType;

    //
    // Constructor(s)
    //

    public TaskBeneficiaryType() {
        super();
    }

    public TaskBeneficiaryType(Reference reference) {
        setIdentifier(reference.getIdentifier());
        setType(reference.getType());
        setBeneficiaryType(TaskBeneficiaryTypeTypeEnum.TASK_BENEFICIARY_PATIENT);
    }

    public TaskBeneficiaryType(TaskBeneficiaryType ori) {
        setIdentifier(ori.getIdentifier());
        setType(ori.getType());
        setBeneficiaryType(ori.getBeneficiaryType());
    }

    //
    // Getters and Setters
    //

    public TaskBeneficiaryTypeTypeEnum getBeneficiaryType() {
        return beneficiaryType;
    }

    public void setBeneficiaryType(TaskBeneficiaryTypeTypeEnum beneficiaryType) {
        this.beneficiaryType = beneficiaryType;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "TaskBeneficiaryType{" +
                "beneficiaryType=" + beneficiaryType +
                ", reference=" + getReference() +
                ", typeElement=" + getTypeElement() +
                ", displayElement=" + getDisplayElement() +
                ", display=" + getDisplay() +
                '}';
    }
}
