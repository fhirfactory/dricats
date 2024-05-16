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
package net.fhirfactory.pegacorn.communicate.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.fhirfactory.dricats.util.PegacornEnvironmentProperties;
import net.fhirfactory.pegacorn.communicate.synapse.credentials.SynapseAdminAccessToken;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;


public abstract class APIAccessToken implements Serializable {
    public static String SYNAPSE_ADMIN_USER_PASSWORD_PROPERTY = "SYNAPSE_ADMIN_USER_PASSWORD";
    public static String SYNAPSE_ACCESS_TOKEN_PROPERTY = "SYNAPSE_ACCESS_TOKEN";
    public static String SYNAPSE_ADMIN_USER_NAME_PROPERTY = "SYNAPSE_ADMIN_USER_NAME";
    public static String MATRIX_BRIDGE_TOKEN_PROPERTY = "MATRIX_BRIDGE_ACCESS_TOKEN";
    private String userName;
    private String userId;
    private String remoteAccessToken;
    private String localAccessToken;
    private String sessionAccessToken;
    private Instant lastAccessTime;
    private Object accessLock;
    private String userPassword;
    private String homeServer;
    private boolean initialised;
    @Inject
    private PegacornEnvironmentProperties environmentProperties;

    //
    // Constructor(s)
    //

    public APIAccessToken() {
        this.accessLock = new Object();
        this.lastAccessTime = Instant.now();
        this.sessionAccessToken = null;
        this.initialised = false;
        this.userId = null;
        this.homeServer = null;
    }


    //
    // Post Construct
    //
    @PostConstruct
    public void initialise() {
        if (initialised) {
            // do nothing
        } else {
            // Get the AccessToken and assign it
            String synapseAdminUserName = environmentProperties.getMandatoryProperty(SynapseAdminAccessToken.SYNAPSE_ADMIN_USER_NAME_PROPERTY);
            String synapseAdminUserPassword = environmentProperties.getMandatoryProperty(SynapseAdminAccessToken.SYNAPSE_ADMIN_USER_PASSWORD_PROPERTY);
            String synapseAccessToken = environmentProperties.getMandatoryProperty(SynapseAdminAccessToken.SYNAPSE_ACCESS_TOKEN_PROPERTY);

            if (StringUtils.isBlank(synapseAdminUserName) || StringUtils.isBlank(synapseAdminUserPassword)) {
                throw (new RuntimeException("SynapseAdminUserName or SynapseAdminUserPassword is blank"));
            }

            setRemoteAccessToken(synapseAccessToken);
            setUserName(synapseAdminUserName);
            setUserPassword(synapseAdminUserPassword);
        }
    }

    //
    // Getters and Setters
    //


    public String getHomeServer() {
        return homeServer;
    }

    public void setHomeServer(String homeServer) {
        this.homeServer = homeServer;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionAccessToken() {
        return sessionAccessToken;
    }

    public void setSessionAccessToken(String sessionAccessToken) {
        this.sessionAccessToken = sessionAccessToken;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRemoteAccessToken() {
        return remoteAccessToken;
    }

    public void setRemoteAccessToken(String remoteAccessToken) {
        this.remoteAccessToken = remoteAccessToken;
    }

    public String getLocalAccessToken() {
        return localAccessToken;
    }

    public void setLocalAccessToken(String localAccessToken) {
        this.localAccessToken = localAccessToken;
    }

    public Instant getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Instant lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    @JsonIgnore
    public void touchAccessTime() {
        this.lastAccessTime = Instant.now();
    }

    public Object getAccessLock() {
        return accessLock;
    }

    public void setAccessLock(Object accessLock) {
        this.accessLock = accessLock;
    }

    //
    // Equals and Hashcode
    //

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        APIAccessToken that = (APIAccessToken) o;
        return Objects.equals(getUserName(), that.getUserName()) && Objects.equals(getRemoteAccessToken(), that.getRemoteAccessToken()) && Objects.equals(getLocalAccessToken(), that.getLocalAccessToken()) && Objects.equals(getSessionAccessToken(), that.getSessionAccessToken()) && Objects.equals(getLastAccessTime(), that.getLastAccessTime()) && Objects.equals(getAccessLock(), that.getAccessLock()) && Objects.equals(getUserPassword(), that.getUserPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserName(), getRemoteAccessToken(), getLocalAccessToken(), getSessionAccessToken(), getLastAccessTime(), getAccessLock(), getUserPassword());
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "APIAccessToken{" +
                "userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", remoteAccessToken='" + remoteAccessToken + '\'' +
                ", localAccessToken='" + localAccessToken + '\'' +
                ", sessionAccessToken='" + sessionAccessToken + '\'' +
                ", lastAccessTime=" + lastAccessTime +
                ", accessLock=" + accessLock +
                ", userPassword='" + userPassword + '\'' +
                ", homeServer='" + homeServer + '\'' +
                ", initialised=" + initialised +
                ", environmentProperties=" + environmentProperties +
                '}';
    }
}
