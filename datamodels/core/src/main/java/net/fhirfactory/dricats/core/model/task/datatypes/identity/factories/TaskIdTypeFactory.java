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
package net.fhirfactory.dricats.core.model.task.datatypes.identity.factories;

import net.fhirfactory.dricats.core.model.dataparcel.DataParcelTypeDescriptor;
import net.fhirfactory.dricats.core.model.task.datatypes.identity.datatypes.TaskIdType;
import net.fhirfactory.dricats.core.model.task.datatypes.reason.valuesets.TaskReasonTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class TaskIdTypeFactory {

    public TaskIdType newTaskId(TaskReasonTypeEnum taskReason, DataParcelTypeDescriptor contentDescriptor) {
        StringBuilder idBuilder = new StringBuilder();
        idBuilder.append(taskReason.getTaskReasonDisplayName());
        idBuilder.append("-");
        if (contentDescriptor.hasDataParcelDefiner()) {
            String definer = contentDescriptor.getDataParcelDefiner();
            String definerValue = definer.replaceAll(" ", "");
            idBuilder.append(definerValue);
        }
        if (contentDescriptor.hasDataParcelCategory()) {
            String category = contentDescriptor.getDataParcelCategory();
            idBuilder.append("-" + category);
        }
        if (contentDescriptor.hasDataParcelSubCategory()) {
            String subCategory = contentDescriptor.getDataParcelSubCategory();
            idBuilder.append("-" + subCategory);
        }
        if (contentDescriptor.hasDataParcelResource()) {
            String resource = contentDescriptor.getDataParcelResource();
            idBuilder.append("-" + resource);
        }
        if (contentDescriptor.hasDataParcelSegment()) {
            String segment = contentDescriptor.getDataParcelSegment();
            idBuilder.append("-" + segment);
        }
        if (contentDescriptor.hasDataParcelAttribute()) {
            String attribute = contentDescriptor.getDataParcelAttribute();
            idBuilder.append("-" + attribute);
        }
        if (contentDescriptor.hasDataParcelDiscriminatorType()) {
            String descType = contentDescriptor.getDataParcelDiscriminatorType();
            idBuilder.append("-" + descType);
        }
        if (contentDescriptor.hasDataParcelDiscriminatorValue()) {
            String descValue = contentDescriptor.getDataParcelDiscriminatorValue();
            idBuilder.append("-" + descValue);
        }
        idBuilder.append("-");
        long leastSignificantBits = UUID.randomUUID().getLeastSignificantBits();
        String hexString = Long.toHexString(leastSignificantBits);
        idBuilder.append("-");
        idBuilder.append(hexString);
        TaskIdType id = new TaskIdType();
        id.setLocalId(idBuilder.toString());
        return (id);
    }

    public TaskIdType newTaskId(String participantName, TaskReasonTypeEnum taskReason, DataParcelTypeDescriptor contentDescriptor) {
        StringBuilder idBuilder = new StringBuilder();
        idBuilder.append(participantName);
        idBuilder.append("-");
        long leastSignificantBits = UUID.randomUUID().getLeastSignificantBits();
        String hexString = Long.toHexString(leastSignificantBits);
        idBuilder.append("-");
        idBuilder.append(hexString);
        TaskIdType id = new TaskIdType();
        id.setLocalId(idBuilder.toString());
        return (id);
    }

    public TaskIdType newTaskId() {
        TaskIdType taskId = new TaskIdType();
        return (taskId);
    }

    public TaskIdType newTaskId(IdType id) {
        TaskIdType taskId = new TaskIdType();
        taskId.setResourceId(id);
        UUID uuid = UUID.randomUUID();
        String localId = Long.toHexString(uuid.getMostSignificantBits()) + Long.toHexString(uuid.getLeastSignificantBits());
        taskId.setLocalId(localId);
        return (taskId);
    }

    public TaskIdType newTaskId(Identifier identifier) {
        TaskIdType taskId = new TaskIdType();
        taskId.setPrimaryBusinessIdentifier(identifier);
        UUID uuid = UUID.randomUUID();
        String id = Long.toHexString(uuid.getMostSignificantBits()) + Long.toHexString(uuid.getLeastSignificantBits());
        taskId.setLocalId(id);
        return (taskId);
    }

    public TaskIdType newRoutingTaskId(String participantName, TaskIdType oldId) {
        if (oldId == null) {
            return (null);
        }
        if (StringUtils.isEmpty(oldId.getId())) {
            return (null);
        }
        int startingPoint = oldId.getId().indexOf('(');
        int endingPoint = oldId.getId().indexOf(')');

        if (startingPoint <= 0 && endingPoint <= 0) {
            return (null);
        }
        String dataType = oldId.getId().substring(startingPoint, endingPoint);
        if (StringUtils.isEmpty(dataType)) {
            return (null);
        }
        StringBuilder newID = new StringBuilder();
        newID.append(participantName);
        newID.append("::");
        newID.append(TaskReasonTypeEnum.TASK_REASON_TASK_ROUTING.getTaskReasonDisplayName());
        newID.append(dataType);
        long leastSignificantBits = UUID.randomUUID().getLeastSignificantBits();
        String hexString = Long.toHexString(leastSignificantBits);
        newID.append("::");
        newID.append(hexString);
        TaskIdType id = new TaskIdType();
        id.setLocalId(newID.toString());
        return (id);
    }
}
