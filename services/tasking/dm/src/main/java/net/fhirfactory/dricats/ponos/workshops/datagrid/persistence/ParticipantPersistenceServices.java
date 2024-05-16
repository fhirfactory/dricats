/*
 * Copyright (c) 2022 Mark A. Hunter
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
package net.fhirfactory.dricats.ponos.workshops.datagrid.persistence;

import ca.uhn.fhir.rest.api.MethodOutcome;
import net.fhirfactory.dricats.core.model.componentid.PegacornSystemComponentTypeTypeEnum;
import net.fhirfactory.dricats.core.model.datagrid.datatypes.DataGridResourceIdType;
import net.fhirfactory.dricats.core.model.participant.PetasosParticipant;
import net.fhirfactory.dricats.core.model.participant.id.PetasosParticipantId;
import net.fhirfactory.dricats.internals.fhir.r4.resources.device.factories.ParticipantDeviceFactory;
import net.fhirfactory.dricats.ponos.workshops.datagrid.persistence.adapters.taskrouterstore.TaskRouterDeviceDMClient;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.IdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ParticipantPersistenceServices {
    private static final Logger LOG = LoggerFactory.getLogger(ParticipantPersistenceServices.class);

    @Inject
    private TaskRouterDeviceDMClient deviceFHIRClient;

    @Inject
    private ParticipantDeviceFactory deviceFactory;

    //
    // Constructor(s)
    //

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (LOG);
    }

    protected ParticipantDeviceFactory getDeviceFactory() {
        return (deviceFactory);
    }

    protected TaskRouterDeviceDMClient getDeviceFHIRClient() {
        return (deviceFHIRClient);
    }

    //
    // Business Methods
    //

    public Boolean hasPendingActionableTasks(PetasosParticipantId participantId) {

        return (false);
    }

    public IIdType getParticipantDeviceId(PetasosParticipantId participantId) {
        return (null);
    }

    public PetasosParticipant loadParticipant(PetasosParticipantId participantId) {

        PetasosParticipant component = null;

        return (component);
    }

    public boolean saveParticipant(PetasosParticipant participant) {

        Device participantDevice = getDeviceFactory().mapPetasosParticipantToFHIRDevice(participant);
        boolean alreadyExists = false;

        if (participant.hasDataGridId()) {
            if (participant.getDataGridId().getResourceId() != null) {
                participantDevice.setId(participant.getDataGridId().getResourceId());
                alreadyExists = true;
            }
        }

        if (alreadyExists) {
            MethodOutcome methodOutcome = getDeviceFHIRClient().updateResource(participantDevice);
        } else {
            MethodOutcome methodOutcome = getDeviceFHIRClient().createDevice(participantDevice);
            if (methodOutcome.getCreated()) {
                if (!participant.hasDataGridId()) {
                    participant.setDataGridId(new DataGridResourceIdType());
                }
                IdType id = new IdType();
                id.setParts(methodOutcome.getId().getBaseUrl(), methodOutcome.getId().getResourceType(), methodOutcome.getId().getIdPart(), methodOutcome.getId().getVersionIdPart());
                participant.getDataGridId().setResourceId(id);
                if (participant.getDataGridId().getPrimaryBusinessIdentifier() == null) {
                    participant.getDataGridId().setPrimaryBusinessIdentifier(participantDevice.getIdentifierFirstRep());
                }
            }
        }
        return (true);
    }

    public List<PetasosParticipant> loadAllParticipants() {
        getLogger().debug(".loadAllParticipants(): Entry");

        List<PetasosParticipant> participantList = new ArrayList<>();

        List<Device> processingPlantDevices = getDeviceFHIRClient().searchForDevicesWithType(PegacornSystemComponentTypeTypeEnum.PROCESSING_PLANT.getToken());
        if (processingPlantDevices != null && !processingPlantDevices.isEmpty()) {
            for (Device currentDevice : processingPlantDevices) {
                PetasosParticipant currentParticipant = getDeviceFactory().mapFHIRDeviceToPetasosParticipant(currentDevice);
                if (currentParticipant != null) {
                    participantList.add(currentParticipant);
                }
            }
        }

        List<Device> wupDevices = getDeviceFHIRClient().searchForDevicesWithType(PegacornSystemComponentTypeTypeEnum.WUP.getToken());
        if (wupDevices != null && !wupDevices.isEmpty()) {
            for (Device currentDevice : wupDevices) {
                PetasosParticipant currentParticipant = getDeviceFactory().mapFHIRDeviceToPetasosParticipant(currentDevice);
                if (currentParticipant != null) {
                    participantList.add(currentParticipant);
                }
            }
        }

        getLogger().warn(".loadAllParticipants(): Exit, participantList.size()->{}", participantList.size());
        return (participantList);
    }
}
