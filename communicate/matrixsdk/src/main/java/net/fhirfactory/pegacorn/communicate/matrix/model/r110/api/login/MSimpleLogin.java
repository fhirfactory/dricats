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
package net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.login;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.login.datatypes.MUserIdentifierType;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MSimpleLogin implements Serializable {
    private String type;
    private String address;
    private String deviceId;
    private String initialDeviceDisplayName;
    private String medium;
    private String token;
    private String user;
    private MUserIdentifierType identifier;
    private String password;

    //
    // Getters and Setters
    //

    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @JsonProperty("device_id")
    public String getDeviceId() {
        return deviceId;
    }

    @JsonProperty("device_id")
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @JsonProperty("initial_device_display_name")
    public String getInitialDeviceDisplayName() {
        return initialDeviceDisplayName;
    }

    @JsonProperty("initial_device_display_name")
    public void setInitialDeviceDisplayName(String initialDeviceDisplayName) {
        this.initialDeviceDisplayName = initialDeviceDisplayName;
    }

    @JsonProperty("medium")
    public String getMedium() {
        return medium;
    }

    @JsonProperty("medium")
    public void setMedium(String medium) {
        this.medium = medium;
    }

    @JsonProperty("token")
    public String getToken() {
        return token;
    }

    @JsonProperty("token")
    public void setToken(String token) {
        this.token = token;
    }

    @JsonProperty("user")
    public String getUser() {
        return user;
    }

    @JsonProperty("user")
    public void setUser(String user) {
        this.user = user;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("identifier")
    public MUserIdentifierType getIdentifier() {
        return identifier;
    }

    @JsonProperty("identifier")
    public void setIdentifier(MUserIdentifierType identifier) {
        this.identifier = identifier;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "MSimpleLogin{" +
                "type='" + type + '\'' +
                ", address='" + address + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", initialDeviceDisplayName='" + initialDeviceDisplayName + '\'' +
                ", medium='" + medium + '\'' +
                ", token='" + token + '\'' +
                ", user='" + user + '\'' +
                ", identifier=" + identifier +
                ", password='" + password + '\'' +
                '}';
    }
}
