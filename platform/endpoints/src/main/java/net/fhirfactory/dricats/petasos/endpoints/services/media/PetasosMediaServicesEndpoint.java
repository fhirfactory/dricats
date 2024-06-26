/*
 * Copyright (c) 2021 Mark A. Hunter (ACT Health)
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
package net.fhirfactory.dricats.petasos.endpoints.services.media;

import ca.uhn.fhir.parser.IParser;
import net.fhirfactory.dricats.core.model.capabilities.base.CapabilityUtilisationRequest;
import net.fhirfactory.dricats.core.model.capabilities.base.CapabilityUtilisationResponse;
import net.fhirfactory.dricats.core.model.endpoint.valuesets.PetasosEndpointFunctionTypeEnum;
import net.fhirfactory.dricats.core.model.endpoint.valuesets.PetasosEndpointTopologyTypeEnum;
import net.fhirfactory.dricats.core.model.topology.endpoints.edge.jgroups.JGroupsIntegrationPointSummary;
import net.fhirfactory.dricats.internals.fhir.r4.resources.endpoint.valuesets.EndpointPayloadTypeEnum;
import net.fhirfactory.dricats.petasos.endpoints.technologies.datatypes.PetasosAdapterAddress;
import net.fhirfactory.dricats.petasos.endpoints.technologies.jgroups.JGroupsIntegrationPointBase;
import net.fhirfactory.dricats.util.FHIRContextUtility;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.jgroups.Address;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public abstract class PetasosMediaServicesEndpoint extends JGroupsIntegrationPointBase {
    private static final Logger LOG = LoggerFactory.getLogger(PetasosMediaServicesEndpoint.class);

    private IParser fhirJSONParser;

    @Produce
    private ProducerTemplate camelProducer;

    @Inject
    private FHIRContextUtility fhirContextUtility;

    //
    // Constructor(s)
    //

    public PetasosMediaServicesEndpoint() {
        super();
    }

    //
    // PostConstruct Activities
    //

    @Override
    protected void executePostConstructActivities() {
        fhirJSONParser = fhirContextUtility.getJsonParser().setPrettyPrint(true);
        executePostConstructInstanceActivities();
    }

    //
    // Abstract Methods
    //

    abstract protected void executePostConstructInstanceActivities();

    //
    // Getters (and Setters)
    //

    protected IParser getFHIRJSONParser() {
        return (fhirJSONParser);
    }

    @Override
    protected Logger specifyLogger() {
        return (LOG);
    }

    public ProducerTemplate getCamelProducer() {
        return camelProducer;
    }

    //
    // Endpoint Specification
    //

    @Override
    protected PetasosEndpointTopologyTypeEnum specifyIPCType() {
        return (PetasosEndpointTopologyTypeEnum.JGROUPS_INTEGRATION_POINT);
    }

    @Override
    protected String specifyJGroupsStackFileName() {
        return (getProcessingPlant().getMeAsASoftwareComponent().getPetasosMediaStackConfigFile());
    }

    @Override
    protected String specifySubsystemParticipantName() {
        return (getProcessingPlant().getSubsystemParticipantName());
    }

    @Override
    protected PetasosEndpointFunctionTypeEnum specifyPetasosEndpointFunctionType() {
        return (PetasosEndpointFunctionTypeEnum.PETASOS_MEDIA_ENDPOINT);
    }

    @Override
    protected EndpointPayloadTypeEnum specifyPetasosEndpointPayloadType() {
        return (EndpointPayloadTypeEnum.ENDPOINT_PAYLOAD_INTERNAL_MEDIA);
    }

    //
    // Processing Plant check triggered by JGroups Cluster membership change
    //

    @Override
    protected void doIntegrationPointBusinessFunctionCheck(JGroupsIntegrationPointSummary integrationPointSummary, boolean isRemoved, boolean isAdded) {

    }

    //
    // Business Methods
    //

    public List<Address> getMediaServerTargetAddressSet(String endpointServiceName) {
        getLogger().debug(".getMediaServerTargetAddressSet(): Entry, endpointServiceName->{}", endpointServiceName);
        List<Address> endpointAddressSet = new ArrayList<>();
        if (StringUtils.isEmpty(endpointServiceName)) {
            getLogger().debug(".getMediaServerTargetAddressSet(): Exit, endpointServiceName is empty");
            return (endpointAddressSet);
        }
        List<PetasosAdapterAddress> memberAdapterSetForService = getTargetMemberAdapterSetForSubsystemName(endpointServiceName);
        for (PetasosAdapterAddress currentMember : memberAdapterSetForService) {
            Address currentMemberAddress = currentMember.getJGroupsAddress();
            if (currentMemberAddress != null) {
                endpointAddressSet.add(currentMemberAddress);
            }
        }
        getLogger().debug(".getMediaServerTargetAddressSet(): Exit, endpointAddressSet->{}", endpointAddressSet);
        return (endpointAddressSet);
    }

    public Address getCandidateMediaServerTargetAddress(String endpointServiceName) {
        getLogger().debug(".getCandidateMediaServerTargetAddress(): Entry, endpointServiceName->{}", endpointServiceName);
        if (StringUtils.isEmpty(endpointServiceName)) {
            getLogger().debug(".getCandidateMediaServerTargetAddress(): Exit, endpointServiceName is empty");
            return (null);
        }
        List<Address> endpointAddressSet = getMediaServerTargetAddressSet(endpointServiceName);
        if (endpointAddressSet.isEmpty()) {
            getLogger().debug(".getCandidateMediaServerTargetAddress(): Exit, endpointAddressSet is empty");
            return (null);
        }
        Address endpointJGroupsAddress = endpointAddressSet.get(0);
        getLogger().debug(".getCandidateMediaServerTargetAddress(): Exit, selected address->{}", endpointJGroupsAddress);
        return (endpointJGroupsAddress);
    }

    //
    // ****Tactical****
    // Task Execution / Capability Utilisation Services
    //

    public CapabilityUtilisationResponse executeTask(String capabilityProviderName, CapabilityUtilisationRequest task) {
        getLogger().trace(".executeTask(): Entry, capabilityProviderName->{}, task->{}", capabilityProviderName, task);
        Address targetAddress = getCandidateMediaServerTargetAddress(capabilityProviderName);
        try {
            Object objectSet[] = new Object[1];
            Class classSet[] = new Class[1];
            objectSet[0] = task;
            classSet[0] = CapabilityUtilisationRequest.class;
            RequestOptions requestOptions = new RequestOptions(ResponseMode.GET_FIRST, getRPCUnicastTimeout());
            CapabilityUtilisationResponse response = null;
            synchronized (getIPCChannelLock()) {
                response = getRPCDispatcher().callRemoteMethod(targetAddress, "executeTaskHandler", objectSet, classSet, requestOptions);
            }
            getMetricsAgent().incrementRemoteProcedureCallCount();
            getLogger().debug(".executeTask(): Exit, response->{}", response);
            return (response);
        } catch (NoSuchMethodException e) {
            getMetricsAgent().incrementRemoteProcedureCallFailureCount();
            getLogger().error(".executeTask(): Error (NoSuchMethodException) ->{}", e.getMessage());
            CapabilityUtilisationResponse response = new CapabilityUtilisationResponse();
            response.setAssociatedRequestID(task.getRequestID());
            response.setSuccessful(false);
            return (response);
        } catch (Exception e) {
            getMetricsAgent().incrementRemoteProcedureCallFailureCount();
            e.printStackTrace();
            getLogger().error(".executeTask: Error (GeneralException) ->{}", e.getMessage());
            CapabilityUtilisationResponse response = new CapabilityUtilisationResponse();
            response.setAssociatedRequestID(task.getRequestID());
            response.setSuccessful(false);
            return (response);
        }
    }


    @Override
    protected String specifyJGroupsClusterName() {
        return (getComponentNameUtilities().getPetasosMediaServicesGroupName());
    }

    @Override
    protected void addIntegrationPointToJGroupsIntegrationPointSet() {
        getJgroupsIPSet().setPetasosMediaServicesEndpoint(getJGroupsIntegrationPoint());
    }
}
