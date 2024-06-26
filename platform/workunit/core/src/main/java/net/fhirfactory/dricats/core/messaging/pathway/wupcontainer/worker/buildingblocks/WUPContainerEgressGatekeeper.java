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

package net.fhirfactory.dricats.core.messaging.pathway.wupcontainer.worker.platform;

import net.fhirfactory.dricats.core.model.dataparcel.DataParcelManifest;
import net.fhirfactory.dricats.core.model.dataparcel.DataParcelTypeDescriptor;
import net.fhirfactory.dricats.core.model.dataparcel.valuesets.DataParcelNormalisationStatusEnum;
import net.fhirfactory.dricats.core.model.dataparcel.valuesets.DataParcelValidationStatusEnum;
import net.fhirfactory.dricats.core.model.dataparcel.valuesets.PolicyEnforcementPointApprovalStatusEnum;
import net.fhirfactory.dricats.core.model.task.datatypes.status.valuesets.TaskOutcomeStatusEnum;
import net.fhirfactory.dricats.core.model.task.datatypes.status.valuesets.TaskOutcomeStatusReasonEnum;
import net.fhirfactory.dricats.core.model.uow.UoWPayload;
import net.fhirfactory.dricats.petasos.audit.brokers.PetasosFulfillmentTaskAuditServicesBroker;
import net.fhirfactory.dricats.core.tasks.accessors.PetasosFulfillmentTaskSharedInstance;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;

/**
 * @author Mark A. Hunter
 * @since 2020-07-01
 */
@ApplicationScoped
public class WUPContainerEgressGatekeeper {
    private static final Logger LOG = LoggerFactory.getLogger(WUPContainerEgressGatekeeper.class);
    @Inject
    private PetasosFulfillmentTaskAuditServicesBroker auditServicesBroker;

    protected Logger getLogger() {
        return (LOG);
    }

    /**
     * This class/method checks the status of the WUPJobCard for the parcel, and ascertains if it is to be
     * discarded (because of some processing error or due to the fact that the processing has occurred already
     * within another WUP). At the moment, it reaches the "discard" decisions purely by checking the
     * WUPJobCard.isToBeDiscarded boolean.
     *
     * @param fulfillmentTask The WorkUnitTransportPacket that is to be forwarded to the Intersection (if all is OK)
     * @param camelExchange   The Apache Camel Exchange object, used to store a Semaphore as we iterate through Dynamic Route options
     * @return Returns a PetasosFulfillmentTask with the egress payload containing the DiscardedTask value set
     */

    public PetasosFulfillmentTaskSharedInstance egressGatekeeper(PetasosFulfillmentTaskSharedInstance fulfillmentTask, Exchange camelExchange) {
        if (getLogger().isDebugEnabled()) {
            getLogger().debug(".egressGatekeeper(): Entry, fulfillmentTaskId/ActionableTaskId->{}/{}", fulfillmentTask.getTaskId(), fulfillmentTask.getActionableTaskId());
        }
        getLogger().trace(".egressGatekeeper(): Enter, fulfillmentTask ->{}", fulfillmentTask);
        ArrayList<String> targetList = new ArrayList<String>();
        if (fulfillmentTask.getTaskFulfillment().isToBeDiscarded()) {
            DataParcelManifest discardedParcelManifest = new DataParcelManifest();
            DataParcelTypeDescriptor discardedParcelDescriptor = new DataParcelTypeDescriptor();
            discardedParcelDescriptor.setDataParcelDefiner("FHIRFactory");
            discardedParcelDescriptor.setDataParcelCategory("Petasos");
            discardedParcelDescriptor.setDataParcelSubCategory("Tasking");
            discardedParcelManifest.setContentDescriptor(discardedParcelDescriptor);
            discardedParcelManifest.setDataParcelFlowDirection(fulfillmentTask.getTaskWorkItem().getIngresContent().getPayloadManifest().getDataParcelFlowDirection());
            discardedParcelManifest.setEnforcementPointApprovalStatus(PolicyEnforcementPointApprovalStatusEnum.POLICY_ENFORCEMENT_POINT_APPROVAL_POSITIVE);
            discardedParcelManifest.setInterSubsystemDistributable(false);
            discardedParcelManifest.setNormalisationStatus(DataParcelNormalisationStatusEnum.DATA_PARCEL_CONTENT_NORMALISATION_TRUE);
            discardedParcelManifest.setValidationStatus(DataParcelValidationStatusEnum.DATA_PARCEL_CONTENT_VALIDATED_TRUE);
            if (fulfillmentTask.getTaskWorkItem().getIngresContent().getPayloadManifest().getSourceSystem() != null) {
                discardedParcelManifest.setSourceSystem(fulfillmentTask.getTaskWorkItem().getIngresContent().getPayloadManifest().getSourceSystem());
            }
            UoWPayload discardedItemPayload = new UoWPayload();

            switch (fulfillmentTask.getTaskWorkItem().getProcessingOutcome()) {
                case UOW_OUTCOME_NO_PROCESSING_REQUIRED:
                    discardedParcelDescriptor.setDataParcelResource("OAM-NoProcessingRequired");
                    discardedItemPayload.setPayload("Task was a No-Operation");
                    break;
                case UOW_OUTCOME_FILTERED:
                    discardedParcelDescriptor.setDataParcelResource("OAM-Filtered");
                    discardedItemPayload.setPayload("Task was filtered");
                    fulfillmentTask.getTaskOutcomeStatus().setOutcomeStatus(TaskOutcomeStatusEnum.PETASOS_TASK_OUTCOME_STATUS_FINISHED);
                    fulfillmentTask.getTaskOutcomeStatus().setOutcomeStatusReason(TaskOutcomeStatusReasonEnum.PETASOS_TASK_OUTCOME_REASON_FILTERED_BY_CODE);
                    break;
                case UOW_OUTCOME_DISCARD:
                    discardedParcelDescriptor.setDataParcelResource("OAM-Discard");
                    discardedItemPayload.setPayload("Task is to be discarded");
                    break;
                default:
                    discardedParcelDescriptor.setDataParcelResource("OAM-Failure");
                    discardedItemPayload.setPayload("Failed");
            }
            discardedItemPayload.setPayloadManifest(discardedParcelManifest);
            fulfillmentTask.getTaskWorkItem().getEgressContent().getPayloadElements().clear();
            fulfillmentTask.getTaskWorkItem().getEgressContent().addPayloadElement(discardedItemPayload);
            fulfillmentTask.update();
        }
        auditServicesBroker.logActivity(fulfillmentTask.getInstance());

        if (getLogger().isDebugEnabled()) {
            getLogger().debug(".egressGatekeeper(): Exit, fulfillmentTaskId/ActionableTaskId->{}/{}: Status->{}", fulfillmentTask.getTaskId(), fulfillmentTask.getActionableTaskId(), fulfillmentTask.getTaskFulfillment().getStatus());
        }

        getLogger().trace(".egressGatekeeper(): Exit, fulfillmentTask ->{}", fulfillmentTask);
        return (fulfillmentTask);
    }
}
