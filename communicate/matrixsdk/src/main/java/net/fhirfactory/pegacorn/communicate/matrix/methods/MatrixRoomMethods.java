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
package net.fhirfactory.pegacorn.communicate.matrix.methods;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.netty.handler.codec.http.HttpMethod;
import net.fhirfactory.pegacorn.communicate.matrix.methods.common.MatrixQuery;
import net.fhirfactory.pegacorn.communicate.matrix.model.core.MatrixRoom;
import net.fhirfactory.pegacorn.communicate.matrix.model.interfaces.MatrixAdminProxyInterface;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.common.MAPIResponse;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.datatypes.MCreationContentResponse;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.datatypes.MStateEvent;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.rooms.MRoomCreation;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.rooms.MRoomVisibilityEnum;
import net.fhirfactory.pegacorn.communicate.synapse.methods.SynapseRoomMethods;
import net.fhirfactory.pegacorn.communicate.synapse.model.SynapseRoom;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;

@ApplicationScoped
public class MatrixRoomMethods {
    private static final Logger LOG = LoggerFactory.getLogger(MatrixSpaceMethods.class);

    private ObjectMapper jsonMapper;

    @Inject
    private SynapseRoomMethods synapseRoomAPI;

    @Inject
    private MatrixAdminProxyInterface matrixAdminAPI;

    //
    // Constructor(s)
    //

    public MatrixRoomMethods() {
        jsonMapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        jsonMapper.registerModule(module);
        jsonMapper.configure(JsonParser.Feature.ALLOW_MISSING_VALUES, true);
    }

    //
    // Business Methods
    //

    public MAPIResponse getRoom(String roomID) {
        MAPIResponse taskResponse = new MAPIResponse();

        return (taskResponse);
    }

    /**
     * Lists the public rooms on the server.
     * <p>
     * This API returns paginated responses. The rooms are ordered by the number of joined members,
     * with the largest rooms first.
     *
     * @return
     */
    public MAPIResponse getPublicRooms() {
        List<SynapseRoom> roomList = synapseRoomAPI.getRooms("");

        MAPIResponse mapiResponse = new MAPIResponse();

        return (mapiResponse);
    }

    /**
     * Gets the visibility of a given room on the server's public room directory.
     *
     * @param roomID
     * @return
     */
    public MAPIResponse getRoomVisibility(String practitionerMatrixUserID, String roomID) {
        MAPIResponse taskResponse = new MAPIResponse();

        return (taskResponse);
    }

    /**
     * Sets the visibility of a given room in the server's public room directory.
     *
     * @param roomID
     * @param roomVisibility
     * @return
     */
    public MAPIResponse setRoomVisibility(String practitionerMatrixUserID, String roomID, MRoomVisibilityEnum roomVisibility) {
        MAPIResponse taskResponse = new MAPIResponse();

        return (taskResponse);
    }

    //
    // Room Status Detail
    //

    /**
     * Get the state events for the current state of a room.
     *
     * @param roomID
     * @return
     */
    public MAPIResponse getRoomState(String practitionerMatrixUserID, String roomID) {
        MAPIResponse response = new MAPIResponse();

        return (response);
    }

    /**
     * Get the list of members for this room.
     *
     * @param roomID
     * @return
     */
    public MAPIResponse getRoomMembers(String practitionerMatrixUserID, String roomID) {
        MAPIResponse response = new MAPIResponse();

        return (response);
    }

    /**
     * This API returns a map of MXIDs to member info objects for members of the room. The current user must be in the
     * room for it to work, unless it is an Application Service in which case any of the AS's users must be in the room.
     * This API is primarily for Application Services and should be faster to respond than /members as it can be
     * implemented more efficiently on the server.
     *
     * @param roomID
     * @return
     */
    public MAPIResponse getRoomJoinedMembers(String practitionerMatrixUserID, String roomID) {
        MAPIResponse response = new MAPIResponse();

        return (response);
    }

    public MAPIResponse getRoomMessages(String practitionerMatrixUserID, String roomID) {
        MAPIResponse response = new MAPIResponse();

        return (response);
    }

    /**
     * Strips all information out of an event which isn't critical to the integrity of the server-side representation
     * of the room.
     * <p>
     * This cannot be undone.
     *
     * @param roomID
     * @param eventID
     * @param transactionID
     * @return
     */
    public MAPIResponse redactRoomEvent(String practitionerMatrixUserID, String roomID, String eventID, String transactionID) {
        MAPIResponse response = new MAPIResponse();

        return (response);
    }

    //
    // Room Creation/Deletion
    //

    /**
     * Create a new room with various configuration options.
     *
     * @param roomCreation
     * @return
     */
    public SynapseRoom createRoom(String matrixUserID, MRoomCreation roomCreation) {
        getLogger().debug(".createSpace(): Entry, matrixUserID->{}, roomCreation->{}", matrixUserID, roomCreation);
        MatrixQuery query = new MatrixQuery();

        //
        // Create the Query
        query.setHttpPath("_matrix/client/r0/createRoom");
        query.setHttpMethod(HttpMethod.POST.name());

        JSONObject creationRequest = new JSONObject();
        if (StringUtils.isNotEmpty(roomCreation.getVisibility())) {
            creationRequest.put("visibility", roomCreation.getVisibility());
        }
        if (StringUtils.isNotEmpty(roomCreation.getRoomAliasName())) {
            creationRequest.put("room_alias_name", roomCreation.getRoomAliasName());
        }
        if (StringUtils.isNotEmpty(roomCreation.getName())) {
            creationRequest.put("name", roomCreation.getName());
        }
        if (StringUtils.isNotEmpty(roomCreation.getTopic())) {
            creationRequest.put("topic", roomCreation.getTopic());
        }
        if (!roomCreation.getInvite3PID().isEmpty()) {
            // TODO fix
        }
        if (StringUtils.isNotEmpty(roomCreation.getRoomVersion())) {
            creationRequest.put("room_version", roomCreation.getRoomVersion());
        }
        if (!roomCreation.getInitialState().isEmpty()) {
            JSONArray stateEventArray = new JSONArray();
            int counter = 0;
            for (MStateEvent currentStateEvent : roomCreation.getInitialState()) {
                JSONObject currentStateEventObject = new JSONObject();
                if (StringUtils.isNotEmpty(currentStateEvent.getType())) {
                    currentStateEventObject.put("type", currentStateEvent.getType());
                }
                if (StringUtils.isNotEmpty(currentStateEvent.getStateKey())) {
                    currentStateEventObject.put("state_key", currentStateEvent.getStateKey());
                }
                if (currentStateEvent.getContent() != null) {
                    if (currentStateEvent.getContent() instanceof JSONObject) {
                        JSONObject contentObject = (JSONObject) currentStateEvent.getContent();
                        currentStateEventObject.put("content", contentObject);
                    }
                }
                stateEventArray.put(counter, currentStateEventObject);
                counter += 1;
            }
            creationRequest.put("initial_state", stateEventArray);
        }
        if (StringUtils.isNotEmpty(roomCreation.getPreset())) {
            creationRequest.put("preset", roomCreation.getPreset());
        }
        creationRequest.put("is_direct", roomCreation.isDirect());
        if (roomCreation.getCreationContent() != null) {
            JSONObject creationContentObject = new JSONObject();
            creationContentObject.put("m.federate", roomCreation.getCreationContent().isFederate());
            if (StringUtils.isNotEmpty(roomCreation.getCreationContent().getType())) {
                creationContentObject.put("type", roomCreation.getCreationContent().getType());
            }
            creationRequest.put("creation_content", creationContentObject);
        }
        if (roomCreation.getPowerLevelContentOverride() != null) {
            JSONObject powerlevelOverrideObject = new JSONObject();
            powerlevelOverrideObject.put("state_default", roomCreation.getPowerLevelContentOverride().getStateDefault());
            powerlevelOverrideObject.put("ban", roomCreation.getPowerLevelContentOverride().getBan());
            powerlevelOverrideObject.put("events_default", roomCreation.getPowerLevelContentOverride().getEventDefaultPowerLevel());
            powerlevelOverrideObject.put("invite", roomCreation.getPowerLevelContentOverride().getInvite());
            powerlevelOverrideObject.put("kick", roomCreation.getPowerLevelContentOverride().getKick());
            powerlevelOverrideObject.put("redact", roomCreation.getPowerLevelContentOverride().getRedact());
            powerlevelOverrideObject.put("users_default", roomCreation.getPowerLevelContentOverride().getUserDefaultPowerLevel());
            // TODO fix the various array values
            creationRequest.put("power_level_content_override", powerlevelOverrideObject);
        }

        /*
        try {
            String roomCreationAsString = jsonMapper.writeValueAsString(roomCreation);
            query.setBody(roomCreationAsString);
        } catch (JsonProcessingException e) {
            getLogger().error(".createSpace(): Error, could convert MRoomCreation to String, stack->{}", ExceptionUtils.getStackTrace(e));
        }
         */

        query.setBody(creationRequest.toString());

        MAPIResponse mapiResponse = matrixAdminAPI.executeRoomAction(query);

        MCreationContentResponse response = new MCreationContentResponse();
        MatrixRoom matrixRoom = null;

        if (mapiResponse.getResponseCode() == 200) {
            JSONObject responseObject = new JSONObject(mapiResponse.getResponseContent());
            String roomId = responseObject.getString("room_id");
            String roomAlias = responseObject.getString("room_alias");
            response.setRoomId(roomId);
            response.setRoomAlias(roomAlias);

            SynapseRoom synapseRoom = synapseRoomAPI.getRoomDetail(response.getRoomId());

            if (synapseRoom != null) {
                matrixRoom = new MatrixRoom(synapseRoom);
            }
        }

        getLogger().debug(".createSpace(): Response to creation matrixRoom->{}", matrixRoom);
        return (matrixRoom);
    }

    /**
     * Create a new mapping from room alias to room ID.
     *
     * @param roomID
     * @param roomAlias
     * @return
     */
    public MAPIResponse setRoomAlias(String practitionerMatrixUserID, String roomID, String roomAlias) {
        MAPIResponse response = new MAPIResponse();

        return (response);
    }

    /**
     * Requests that the server resolve a room alias to a room ID.
     * <p>
     * The server will use the federation API to resolve the alias if the domain part of the alias does not correspond
     * to the server's own domain.
     *
     * @param roomAlias
     * @return
     */
    public MAPIResponse getRoomFromAlias(String practitionerMatrixUserID, String roomAlias) {
        MAPIResponse response = new MAPIResponse();

        return (response);
    }

    //
    // Auxillary MicroTasks
    //

    public boolean doesRoomExistForUser(String practitionerMatrixUserID, String roomName) {
        return (false);
    }

    //
    // Getters and Setters
    //

    protected Logger getLogger() {
        return (LOG);
    }
}
