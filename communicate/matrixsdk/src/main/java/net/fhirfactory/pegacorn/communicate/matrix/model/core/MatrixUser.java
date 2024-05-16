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
package net.fhirfactory.pegacorn.communicate.matrix.model.core;

import net.fhirfactory.pegacorn.communicate.synapse.model.SynapseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatrixUser extends SynapseUser {
    private List<String> roomMembershipIds;
    private Map<String, MatrixRoom> roomMemberships;

    //
    // Constructor(s)
    //

    public MatrixUser() {
        super();
        this.roomMembershipIds = new ArrayList<>();
        this.roomMemberships = new HashMap<>();
    }

    public MatrixUser(SynapseUser ori) {
        super(ori);
        this.roomMembershipIds = new ArrayList<>();
        this.roomMemberships = new HashMap<>();
    }

    public MatrixUser(MatrixUser ori) {
        super(ori);
        this.roomMembershipIds = new ArrayList<>();
        if (!ori.getRoomMembershipIds().isEmpty()) {
            getRoomMembershipIds().addAll(ori.getRoomMembershipIds());
        }
        this.roomMemberships = new HashMap<>();
        if (!ori.getRoomMemberships().isEmpty()) {
            for (MatrixRoom currentMembershipRoom : ori.getRoomMemberships().values()) {
                getRoomMemberships().put(currentMembershipRoom.getRoomID(), currentMembershipRoom);
            }
        }
    }

    //
    // Getters and Setters
    //

    public List<String> getRoomMembershipIds() {
        return roomMembershipIds;
    }

    public void setRoomMembershipIds(List<String> roomMembershipIds) {
        this.roomMembershipIds = roomMembershipIds;
    }

    public Map<String, MatrixRoom> getRoomMemberships() {
        return roomMemberships;
    }

    public void setRoomMemberships(Map<String, MatrixRoom> roomMemberships) {
        this.roomMemberships = roomMemberships;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "MatrixUser{" +
                "roomMembershipIds=" + roomMembershipIds +
                ", roomMemberships=" + roomMemberships +
                ", name='" + getName() + '\'' +
                ", guest=" + isGuest() +
                ", admin=" + isAdmin() +
                ", userType='" + getUserType() + '\'' +
                ", deactivated=" + isDeactivated() +
                ", shadowBanned=" + isShadowBanned() +
                ", displayName='" + getDisplayName() + '\'' +
                ", avatarURL='" + getAvatarURL() + '\'' +
                ", creationTimestamp=" + getCreationTimestamp() +
                '}';
    }
}
