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
package net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.rooms;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.datatypes.MCreationContent;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.datatypes.MInvite3PID;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.datatypes.MStateEvent;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.events.room.contenttypes.MRoomPowerLevelsContentType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MRoomCreation implements Serializable {
    private String visibility;
    private String roomAliasName;
    private String name;
    private String topic;
    private List<String> invite;
    private List<MInvite3PID> invite3PID;
    private String roomVersion;
    private MCreationContent creationContent;
    private List<MStateEvent> initialState;
    private String preset;
    private boolean direct;
    private MRoomPowerLevelsContentType powerLevelContentOverride;

    //
    // Constructor(s)
    //

    public MRoomCreation() {
        this.visibility = null;
        this.roomAliasName = null;
        this.name = null;
        this.topic = null;
        this.invite = new ArrayList<>();
        this.invite3PID = new ArrayList<>();
        this.roomVersion = null;
        this.creationContent = null;
        this.initialState = new ArrayList<>();
        this.preset = null;
        this.direct = false;
        this.powerLevelContentOverride = null;
    }

    //
    // Getters and Setters
    //

    @JsonProperty("visibility")
    public String getVisibility() {
        return visibility;
    }

    @JsonProperty("visibility")
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    @JsonProperty("room_alias_name")
    public String getRoomAliasName() {
        return roomAliasName;
    }

    @JsonProperty("room_alias_name")
    public void setRoomAliasName(String roomAliasName) {
        this.roomAliasName = roomAliasName;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("topic")
    public String getTopic() {
        return topic;
    }

    @JsonProperty("topic")
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @JsonProperty("invite")
    public List<String> getInvite() {
        return invite;
    }

    @JsonProperty("invite")
    public void setInvite(List<String> invite) {
        this.invite = invite;
    }

    @JsonProperty("invite_3pid")
    public List<MInvite3PID> getInvite3PID() {
        return invite3PID;
    }

    @JsonProperty("invite_3pid")
    public void setInvite3PID(List<MInvite3PID> invite3PID) {
        this.invite3PID = invite3PID;
    }

    @JsonProperty("room_version")
    public String getRoomVersion() {
        return roomVersion;
    }

    @JsonProperty("room_version")
    public void setRoomVersion(String roomVersion) {
        this.roomVersion = roomVersion;
    }

    @JsonProperty("creation_content")
    public MCreationContent getCreationContent() {
        return creationContent;
    }

    @JsonProperty("creation_content")
    public void setCreationContent(MCreationContent creationContent) {
        this.creationContent = creationContent;
    }

    @JsonProperty("initial_state")
    public List<MStateEvent> getInitialState() {
        return initialState;
    }

    @JsonProperty("initial_state")
    public void setInitialState(List<MStateEvent> initialState) {
        this.initialState = initialState;
    }

    @JsonProperty("preset")
    public String getPreset() {
        return preset;
    }

    @JsonProperty("preset")
    public void setPreset(String preset) {
        this.preset = preset;
    }

    @JsonProperty("is_direct")
    public boolean isDirect() {
        return direct;
    }

    @JsonProperty("is_direct")
    public void setDirect(boolean direct) {
        this.direct = direct;
    }

    @JsonProperty("power_level_content_override")
    public MRoomPowerLevelsContentType getPowerLevelContentOverride() {
        return powerLevelContentOverride;
    }

    @JsonProperty("power_level_content_override")
    public void setPowerLevelContentOverride(MRoomPowerLevelsContentType powerLevelContentOverride) {
        this.powerLevelContentOverride = powerLevelContentOverride;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "MRoomCreation{" +
                "visibility='" + visibility + '\'' +
                ", roomAliasName='" + roomAliasName + '\'' +
                ", name='" + name + '\'' +
                ", topic='" + topic + '\'' +
                ", invite=" + invite +
                ", invite3PID=" + invite3PID +
                ", roomVersion='" + roomVersion + '\'' +
                ", creationContent=" + creationContent +
                ", initialState=" + initialState +
                ", preset='" + preset + '\'' +
                ", direct=" + direct +
                ", powerLevelContentOverride=" + powerLevelContentOverride +
                '}';
    }
}
