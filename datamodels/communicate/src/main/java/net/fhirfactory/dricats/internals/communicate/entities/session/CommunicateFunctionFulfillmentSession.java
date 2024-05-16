package net.fhirfactory.dricats.internals.communicate.entities.session;

import net.fhirfactory.dricats.internals.communicate.entities.practitioner.CommunicatePractitioner;
import net.fhirfactory.dricats.internals.communicate.entities.rooms.CommunicatePractitionerRoleFulfilmentRoom;
import net.fhirfactory.dricats.internals.communicate.entities.rooms.datatypes.CommunicateRoomID;
import net.fhirfactory.dricats.internals.communicate.entities.session.valuesets.CommunicateSessionFulfillmentStatusEnum;
import net.fhirfactory.dricats.internals.communicate.entities.user.datatypes.CommunicateUserID;

import java.util.concurrent.ConcurrentHashMap;

public class CommunicateFunctionFulfillmentSession extends CommunicateSession {
    private ConcurrentHashMap<CommunicateUserID, CommunicatePractitioner> candidateFulfillerPractitioners;
    private ConcurrentHashMap<CommunicateRoomID, CommunicatePractitionerRoleFulfilmentRoom> candidateFulfillerRooms;
    private CommunicateSessionFulfillmentStatusEnum connectionStatus;
}
