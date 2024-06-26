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
package net.fhirfactory.pegacorn.communicate.iris.datagrid.entityplane.matrixdomain;

import net.fhirfactory.pegacorn.communicate.iris.datagrid.common.api.CommunicatePractitionerDomainInterface;
import net.fhirfactory.pegacorn.communicate.matrixbridge.workshops.transform.api.CommunicatePractitionerMatrixAPI;
import net.fhirfactory.pegacorn.internals.communicate.entities.practitioner.CommunicatePractitioner;
import net.fhirfactory.pegacorn.internals.communicate.entities.rooms.CommunicatePractitionerMyCallsRoom;
import net.fhirfactory.pegacorn.internals.communicate.entities.rooms.CommunicatePractitionerMyMediaRoom;
import net.fhirfactory.pegacorn.internals.communicate.entities.rooms.CommunicateRoom;
import net.fhirfactory.pegacorn.core.model.ui.resources.simple.valuesets.IdentifierESDTTypesEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class PractitionerMatrixDomainServices implements CommunicatePractitionerDomainInterface {
    private static final Logger LOG = LoggerFactory.getLogger(PractitionerMatrixDomainServices.class);

    @Inject
    private CommunicatePractitionerMatrixAPI practitionerMatrixAPI;


    @Inject
    private IdentifierESDTTypesEnum identifierESDTTypesEnum;

    //
    // Practitioner Management
    //

    @Override
    public CommunicatePractitioner activatePractitioner(CommunicatePractitioner practitioner) {

        return (practitioner);
    }

    @Override
    public CommunicatePractitioner suspendPractitioner(CommunicatePractitioner practitioner) {

        return (practitioner);
    }

    @Override
    public CommunicatePractitioner retirePractitioner(CommunicatePractitioner practitioner) {

        return (practitioner);
    }

    //
    // Practitioner Centric Rooms
    //


    @Override
    public List<CommunicateRoom> validatePractitionerSystemRooms(CommunicatePractitioner practitioner) {
        return null;
    }

    @Override
    public CommunicatePractitionerMyCallsRoom synchronisePractitionerMyCallsRoom(CommunicatePractitioner practitioner) {
        return null;
    }

    @Override
    public CommunicatePractitionerMyMediaRoom synchronisePractitionerMyMediaRoom(CommunicatePractitioner practitioner) {
        return null;
    }

    @Override
    public CommunicateRoom synchronisePractitionerGeneralNotificationRoom(CommunicatePractitioner practitioner) {
        return null;
    }
}
