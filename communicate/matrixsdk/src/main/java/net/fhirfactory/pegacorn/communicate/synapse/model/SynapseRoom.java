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
package net.fhirfactory.pegacorn.communicate.synapse.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class SynapseRoom implements Serializable {
    private String roomID;
    private String name;
    private String avatar;
    private String topic;
    private String canonicalAlias;
    private int joinedMembers;
    private int joinedLocalMembers;
    private String version;
    private String creator;
    private String encryption;
    private boolean federatable;
    private boolean publicRoom;
    private String joinRules;
    private String guestAccess;
    private String historyVisibility;
    private int stateEventsCount;
    private int joinedLocalDevices;

    //
    // Constructor(s)
    //

    public SynapseRoom() {
        this.roomID = null;
        this.name = null;
        this.avatar = null;
        this.topic = null;
        this.canonicalAlias = null;
        this.joinedMembers = 0;
        this.joinedLocalMembers = 0;
        this.joinedLocalDevices = 0;
        this.version = null;
        this.creator = null;
        this.encryption = null;
        this.federatable = false;
        this.publicRoom = false;
        this.joinRules = null;
        this.guestAccess = null;
        this.historyVisibility = null;
        this.stateEventsCount = 0;
    }

    public SynapseRoom(SynapseRoom ori) {
        this.roomID = null;
        this.name = null;
        this.avatar = null;
        this.topic = null;
        this.canonicalAlias = null;
        this.joinedMembers = 0;
        this.joinedLocalMembers = 0;
        this.joinedLocalDevices = 0;
        this.version = null;
        this.creator = null;
        this.encryption = null;
        this.federatable = false;
        this.publicRoom = false;
        this.joinRules = null;
        this.guestAccess = null;
        this.historyVisibility = null;
        this.stateEventsCount = 0;


        setRoomID(ori.getRoomID());
        setName(ori.getName());
        setAvatar(ori.getAvatar());
        setTopic(ori.getTopic());
        setCanonicalAlias(ori.getCanonicalAlias());
        setJoinedMembers(ori.getJoinedMembers());
        setJoinedLocalMembers(ori.getJoinedLocalMembers());
        setJoinedLocalDevices(ori.getJoinedLocalDevices());
        setVersion(ori.getVersion());
        setCreator(ori.getCreator());
        setFederatable(ori.isFederatable());
        setPublicRoom(ori.isPublicRoom());
        setJoinRules(ori.getJoinRules());
        setGuestAccess(ori.getGuestAccess());
        setHistoryVisibility(ori.getHistoryVisibility());
        setStateEventsCount(ori.getStateEventsCount());
    }

    //
    // Getters and Setters
    //

    @JsonProperty("joined_local_devices")
    public int getJoinedLocalDevices() {
        return joinedLocalDevices;
    }

    @JsonProperty("joined_local_devices")
    public void setJoinedLocalDevices(int joinedLocalDevices) {
        this.joinedLocalDevices = joinedLocalDevices;
    }

    @JsonProperty("topic")
    public String getTopic() {
        return topic;
    }

    @JsonProperty("topic")
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @JsonProperty("avatar")
    public String getAvatar() {
        return avatar;
    }

    @JsonProperty("avatar")
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @JsonProperty("room_id")
    public String getRoomID() {
        return roomID;
    }

    @JsonProperty("room_id")
    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("canonical_alias")
    public String getCanonicalAlias() {
        return canonicalAlias;
    }

    @JsonProperty("canonical_alias")
    public void setCanonicalAlias(String canonicalAlias) {
        this.canonicalAlias = canonicalAlias;
    }

    @JsonProperty("joined_members")
    public int getJoinedMembers() {
        return joinedMembers;
    }

    @JsonProperty("joined_members")
    public void setJoinedMembers(int joinedMembers) {
        this.joinedMembers = joinedMembers;
    }

    @JsonProperty("joined_local_members")
    public int getJoinedLocalMembers() {
        return joinedLocalMembers;
    }

    @JsonProperty("joined_local_members")
    public void setJoinedLocalMembers(int joinedLocalMembers) {
        this.joinedLocalMembers = joinedLocalMembers;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("creator")
    public String getCreator() {
        return creator;
    }

    @JsonProperty("creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @JsonProperty("encryption")
    public String getEncryption() {
        return encryption;
    }

    @JsonProperty("encryption")
    public void setEncryption(String encryption) {
        this.encryption = encryption;
    }

    @JsonProperty("federatable")
    public boolean isFederatable() {
        return federatable;
    }

    @JsonProperty("federatable")
    public void setFederatable(boolean federatable) {
        this.federatable = federatable;
    }

    @JsonProperty("public")
    public boolean isPublicRoom() {
        return publicRoom;
    }

    @JsonProperty("public")
    public void setPublicRoom(boolean publicRoom) {
        this.publicRoom = publicRoom;
    }

    @JsonProperty("join_rules")
    public String getJoinRules() {
        return joinRules;
    }

    @JsonProperty("join_rules")
    public void setJoinRules(String join_rules) {
        this.joinRules = join_rules;
    }

    @JsonProperty("guest_access")
    public String getGuestAccess() {
        return guestAccess;
    }

    @JsonProperty("guest_access")
    public void setGuestAccess(String guestAccess) {
        this.guestAccess = guestAccess;
    }

    @JsonProperty("history_visibility")
    public String getHistoryVisibility() {
        return historyVisibility;
    }

    @JsonProperty("history_visibility")
    public void setHistoryVisibility(String historyVisibility) {
        this.historyVisibility = historyVisibility;
    }

    @JsonProperty("state_events")
    public int getStateEventsCount() {
        return stateEventsCount;
    }

    @JsonProperty("state_events")
    public void setStateEventsCount(int stateEventsCount) {
        this.stateEventsCount = stateEventsCount;
    }

    @Override
    public String toString() {
        return "SynapseRoom{" +
                "roomID='" + roomID + '\'' +
                ", name='" + name + '\'' +
                ", canonicalAlias='" + canonicalAlias + '\'' +
                ", joinedMembers=" + joinedMembers +
                ", joinedLocalMembers=" + joinedLocalMembers +
                ", version='" + version + '\'' +
                ", creator='" + creator + '\'' +
                ", encryption='" + encryption + '\'' +
                ", federatable=" + federatable +
                ", publicRoom=" + publicRoom +
                ", joinRules='" + joinRules + '\'' +
                ", guestAccess='" + guestAccess + '\'' +
                ", historyVisibility='" + historyVisibility + '\'' +
                ", stateEventsCount=" + stateEventsCount +
                ", avatar=" + avatar +
                '}';
    }
}
