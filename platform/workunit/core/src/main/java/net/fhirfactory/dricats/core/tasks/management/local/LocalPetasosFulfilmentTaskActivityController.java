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

package net.fhirfactory.dricats.core.tasks.management.local;

import net.fhirfactory.dricats.core.interfaces.topology.ProcessingPlantInterface;
import net.fhirfactory.dricats.core.tasks.caches.processingplant.LocalFulfillmentTaskCache;
import net.fhirfactory.dricats.core.tasks.caches.shared.ParticipantSharedTaskJobCardCache;
import net.fhirfactory.dricats.core.tasks.management.local.router.LocalTaskRouter;
import net.fhirfactory.dricats.petasos.audit.brokers.PetasosFulfillmentTaskAuditServicesBroker;
import net.fhirfactory.dricats.core.tasks.accessors.PetasosFulfillmentTaskSharedInstance;
import net.fhirfactory.dricats.core.tasks.accessors.PetasosFulfillmentTaskSharedInstanceAccessorFactory;
import net.fhirfactory.dricats.core.model.task.PetasosFulfillmentTask;
import net.fhirfactory.dricats.core.model.task.datatypes.identity.datatypes.TaskIdType;
import net.fhirfactory.dricats.core.model.wup.PetasosTaskJobCard;
import net.fhirfactory.dricats.core.model.task.datatypes.status.valuesets.TaskExecutionStatusEnum;
import net.fhirfactory.dricats.core.tasks.factories.PetasosTaskJobCardFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author Mark A. Hunter
 */
@ApplicationScoped
public class LocalPetasosFulfilmentTaskActivityController {
    private static final Logger LOG = LoggerFactory.getLogger(LocalPetasosFulfilmentTaskActivityController.class);

    @Inject
    private LocalFulfillmentTaskCache fulfillmentTaskDM;

    @Inject
    private ParticipantSharedTaskJobCardCache sharedTaskJobCardDM;

    @Inject
    private PetasosFulfillmentTaskAuditServicesBroker auditServicesBroker;

    @Inject
    private ProcessingPlantInterface processingPlant;

    @Inject
    private LocalTaskRouter taskExecutionController;

    @Inject
    private PetasosTaskJobCardFactory jobCardFactory;

    @Inject
    private PetasosFulfillmentTaskSharedInstanceAccessorFactory fulfillmentTaskSharedInstanceAccessorFactory;

    //
    // PetasosFulfillmentTask Registration
    //

    public PetasosFulfillmentTaskSharedInstance registerFulfillmentTask(PetasosFulfillmentTask task, boolean synchronousWriteToAudit) {
        getLogger().debug(".registerFulfillmentTask(): Entry, fulfillmentTask->{}", task);
        PetasosTaskJobCard petasosTaskJobCard = null;
        if (task.getTaskJobCard() != null) {
            petasosTaskJobCard = task.getTaskJobCard();
        } else {
            petasosTaskJobCard = jobCardFactory.newPetasosTaskJobCard(task);
            petasosTaskJobCard.setCurrentStatus(TaskExecutionStatusEnum.PETASOS_TASK_EXECUTION_STATUS_ASSIGNED);
            petasosTaskJobCard.setGrantedStatus(TaskExecutionStatusEnum.PETASOS_TASK_EXECUTION_STATUS_ASSIGNED);
            petasosTaskJobCard.setExecutingFulfillmentTaskId(null);
            petasosTaskJobCard.setLastRequestedStatus(TaskExecutionStatusEnum.PETASOS_TASK_EXECUTION_STATUS_ASSIGNED);
            petasosTaskJobCard.setExecutingFulfillmentTaskIdAssignmentInstant(null);
            task.setTaskJobCard(petasosTaskJobCard);
        }
        //
        // Register and Create a SharedInstance Accessor for the Fulfillment Task
        PetasosFulfillmentTaskSharedInstance fulfillmentTaskSharedInstance = getFulfillmentTaskSharedInstanceAccessorFactory().newFulfillmentTaskSharedAccessor(task);
        //
        // Create an audit event
        auditServicesBroker.logActivity(fulfillmentTaskSharedInstance.getInstance(), false);
        //
        // We're done
        getLogger().debug(".registerFulfillmentTask(): Exit, fulfillmentTaskSharedInstance->{}", fulfillmentTaskSharedInstance);
        return (fulfillmentTaskSharedInstance);
    }

    public void deregisterFulfillmentTask(PetasosFulfillmentTask task) {
        getLogger().debug(".deregisterFulfillmentTask(): Entry, task->{}", task);
        fulfillmentTaskDM.removeTask(task.getTaskId());
        getLogger().debug(".deregisterFulfillmentTask(): Exit");
    }

    public void deregisterFulfillmentTask(TaskIdType taskId) {
        fulfillmentTaskDM.removeTask(taskId);
    }


    //
    // Getters and Setters
    //

    protected Logger getLogger() {
        return (LOG);
    }

    protected PetasosFulfillmentTaskSharedInstanceAccessorFactory getFulfillmentTaskSharedInstanceAccessorFactory() {
        return (this.fulfillmentTaskSharedInstanceAccessorFactory);
    }
}
