
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
import net.fhirfactory.dricats.core.model.task.datatypes.identity.datatypes.TaskIdType;
import net.fhirfactory.dricats.core.model.task.datatypes.tasktype.valuesets.TaskTypeTypeEnum;
import net.fhirfactory.dricats.internals.fhir.r4.codesystems.PegacornIdentifierCodeEnum;
import net.fhirfactory.dricats.internals.fhir.r4.resources.identifier.PegacornIdentifierFactory;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.util.Date;

@ApplicationScoped
public class TaskIdentifierFactory {
    private static final Logger LOG = LoggerFactory.getLogger(TaskIdentifierFactory.class);

    private static final String DRICATS_TASK_IDENTIFIER_SEQUENCE_NUMBER_MAJOR_ELEMENT = "/extension/task-identifier/sequence-number-major-element";
    private static final String DRICATS_TASK_IDENTIFIER_SEQUENCE_NUMBER_MINOR_ELEMENT = "/extension/task-identifier/sequence-number-minor-element";

    @Inject
    private PegacornIdentifierFactory generalIdentifierFactory;

    @Inject
    private PegacornReferenceProperties systemWideProperties;

    //
    // Constructor(s)
    //

    //
    // Business Methods
    //

    public Identifier newTaskIdentifier(TaskTypeTypeEnum taskType, TaskIdType taskId, Instant startInstant, Instant endInstant) {
        getLogger().debug(".newActionableTaskIdentifier(): Entry, taskId->{}", taskId);
        Identifier fhirIdentifier = new Identifier();
        Period identifierPeriod = new Period();
        if (startInstant != null) {
            identifierPeriod.setStart(Date.from(startInstant));
        } else {
            identifierPeriod.setStart(Date.from(Instant.now()));
        }
        if (endInstant != null) {
            identifierPeriod.setEnd(Date.from(endInstant));
        }
        PegacornIdentifierCodeEnum identifierCode = null;
        switch (taskType) {
            case PETASOS_BASE_TASK_TYPE:
            case PETASOS_ACTIONABLE_TASK_TYPE:
                identifierCode = PegacornIdentifierCodeEnum.IDENTIFIER_CODE_ACTIONABLE_TASK;
                break;
            case PETASOS_FULFILLMENT_TASK_TYPE:
                identifierCode = PegacornIdentifierCodeEnum.IDENTIFIER_CODE_FULFILLMENT_TASK;
                break;
            case PETASOS_AGGREGATE_TASK_TYPE:
                identifierCode = PegacornIdentifierCodeEnum.IDENTIFIER_CODE_AGGREGATE_TASK;
                break;
        }
        fhirIdentifier = generalIdentifierFactory.newIdentifier(identifierCode, taskId.getLocalId(), identifierPeriod);

        getLogger().trace(".newActionableTaskIdentifier(): [Add Sequence Number] Start");
        if (taskId.getTaskSequenceNumber() != null) {
            getLogger().trace(".newActionableTaskIdentifier(): [Add Sequence Number][Add Major Sequence Number Element] Start");
            Extension sequenceNumberMajorElementExtension = new Extension();
            sequenceNumberMajorElementExtension.setUrl(getTaskSequenceNumberMajorElementISystemURL());
            IntegerType majorSequenceNumber = new IntegerType(taskId.getTaskSequenceNumber().getMajorSequenceNumber());
            sequenceNumberMajorElementExtension.setValue(majorSequenceNumber);
            fhirIdentifier.addExtension(sequenceNumberMajorElementExtension);
            getLogger().trace(".newActionableTaskIdentifier(): [Add Sequence Number][Add Major Sequence Number Element] Finish");
            getLogger().trace(".newActionableTaskIdentifier(): [Add Sequence Number][Add Minor Sequence Number Element] Start");
            Extension sequenceNumberMinorElementExtension = new Extension();
            sequenceNumberMinorElementExtension.setUrl(getTaskSequenceNumberMinorElementISystemURL());
            IntegerType minorSequenceNumber = new IntegerType(taskId.getTaskSequenceNumber().getMinorSequenceNumber());
            sequenceNumberMinorElementExtension.setValue(minorSequenceNumber);
            fhirIdentifier.addExtension(sequenceNumberMinorElementExtension);
            getLogger().trace(".newActionableTaskIdentifier(): [Add Sequence Number][Add Minor Sequence Number Element] Finish");
        }
        getLogger().trace(".newActionableTaskIdentifier(): [Add Sequence Number] Finish");

        getLogger().debug(".newActionableTaskIdentifier(): Exit, fhirIdentifier->{}", fhirIdentifier);
        return (fhirIdentifier);
    }

    public Identifier newTaskIdentifier(TaskTypeTypeEnum taskType, TaskIdType taskId, Instant endInstant) {
        getLogger().debug(".newActionableTaskIdentifier(): Entry, taskId->{}", taskId);
        Identifier fhirIdentifier = fhirIdentifier = newTaskIdentifier(taskType, taskId, null, endInstant);
        getLogger().debug(".newActionableTaskIdentifier(): Exit, fhirIdentifier->{}", fhirIdentifier);
        return (fhirIdentifier);
    }

    public Identifier newTaskIdentifier(TaskTypeTypeEnum taskType, TaskIdType taskId) {
        getLogger().debug(".newActionableTaskIdentifier(): Entry, taskId->{}", taskId);
        Identifier fhirIdentifier = fhirIdentifier = newTaskIdentifier(taskType, taskId, null, null);
        getLogger().debug(".newActionableTaskIdentifier(): Exit, fhirIdentifier->{}", fhirIdentifier);
        return (fhirIdentifier);
    }

    public String getTaskSequenceNumberMajorElementISystemURL() {
        String system = systemWideProperties.getPegacornCodeSystemSite() + DRICATS_TASK_IDENTIFIER_SEQUENCE_NUMBER_MAJOR_ELEMENT;
        return (system);
    }

    public String getTaskSequenceNumberMinorElementISystemURL() {
        String system = systemWideProperties.getPegacornCodeSystemSite() + DRICATS_TASK_IDENTIFIER_SEQUENCE_NUMBER_MINOR_ELEMENT;
        return (system);
    }

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (LOG);
    }
}
