/*
 * The MIT License
 *
 * Copyright 2020 ACT Health.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.fhirfactory.pegacorn.communicate.matrix.issi.receiver.beans;

import net.fhirfactory.dricats.core.constants.petasos.PetasosPropertyConstants;
import net.fhirfactory.dricats.core.model.dataparcel.DataParcelManifest;
import net.fhirfactory.dricats.core.model.dataparcel.DataParcelTypeDescriptor;
import net.fhirfactory.dricats.core.model.dataparcel.valuesets.DataParcelDirectionEnum;
import net.fhirfactory.dricats.core.model.dataparcel.valuesets.DataParcelNormalisationStatusEnum;
import net.fhirfactory.dricats.core.model.dataparcel.valuesets.DataParcelValidationStatusEnum;
import net.fhirfactory.dricats.core.model.dataparcel.valuesets.PolicyEnforcementPointApprovalStatusEnum;
import net.fhirfactory.dricats.core.model.topology.nodes.WorkUnitProcessorSoftwareComponent;
import net.fhirfactory.dricats.core.model.uow.UoW;
import net.fhirfactory.dricats.core.model.uow.UoWPayload;
import net.fhirfactory.dricats.core.model.uow.UoWProcessingOutcomeEnum;
import net.fhirfactory.pegacorn.communicate.matrix.model.constants.MatrixClientServiceAPIConstants;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author ACT Health
 */
@ApplicationScoped
public class IncomingMatrixEventSet2UoW {
    private static final Logger LOG = LoggerFactory.getLogger(IncomingMatrixEventSet2UoW.class);

    @Inject
    IncomingMatrixMessageSplitter messageSplitter;

    public UoW encapsulateMatrixMessage(String matrixMessage, Exchange camelExchange) {
        LOG.debug(".encapsulateMatrixMessage(): Entry, Matrix Message --> {}", matrixMessage);
        WorkUnitProcessorSoftwareComponent wupTopologyNode = camelExchange.getProperty(PetasosPropertyConstants.WUP_TOPOLOGY_NODE_EXCHANGE_PROPERTY_NAME, WorkUnitProcessorSoftwareComponent.class);
        LOG.trace(".encapsulateMatrixMessage(): Creating new Payload element, first the Payload TopicToken");
        DataParcelTypeDescriptor payloadTopic = new DataParcelTypeDescriptor();
        payloadTopic.setDataParcelDefiner("Matrix");
        payloadTopic.setDataParcelCategory("ClientServerAPI");
        payloadTopic.setDataParcelSubCategory("General");
        payloadTopic.setDataParcelResource("RawEventSet");
        payloadTopic.setVersion(MatrixClientServiceAPIConstants.MATRIX_CLIENT_SERVICES_API_RELEASE);
        DataParcelManifest manifest = new DataParcelManifest();
        manifest.setContentDescriptor(payloadTopic);
        manifest.setValidationStatus(DataParcelValidationStatusEnum.DATA_PARCEL_CONTENT_VALIDATED_TRUE);
        manifest.setNormalisationStatus(DataParcelNormalisationStatusEnum.DATA_PARCEL_CONTENT_NORMALISATION_FALSE);
        manifest.setDataParcelFlowDirection(DataParcelDirectionEnum.INFORMATION_FLOW_INBOUND_DATA_PARCEL);
        manifest.setEnforcementPointApprovalStatus(PolicyEnforcementPointApprovalStatusEnum.POLICY_ENFORCEMENT_POINT_APPROVAL_NEGATIVE);
        LOG.trace(".encapsulateMatrixMessage(): Creating new Payload element, now the Payload itself");
        UoWPayload contentPayload = new UoWPayload();
        contentPayload.setPayloadManifest(manifest);
        contentPayload.setPayload(matrixMessage);
        UoW newUoW = new UoW(contentPayload);
        newUoW.getEgressContent().addPayloadElement(contentPayload);
        newUoW.setProcessingOutcome(UoWProcessingOutcomeEnum.UOW_OUTCOME_SUCCESS);
        LOG.debug("encapsulateMatrixMessage(): Exit, UoW created, newUoW --> {}", newUoW);
        return (newUoW);
    }
}
