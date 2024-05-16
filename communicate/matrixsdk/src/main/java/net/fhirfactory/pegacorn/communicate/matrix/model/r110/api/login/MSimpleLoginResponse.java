package net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.login;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.datatypes.MDiscoveryInformationType;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MSimpleLoginResponse implements Serializable {
    private String userId;
    private String accessToken;
    private String homeServer;
    private String deviceId;
    private MDiscoveryInformationType wellKnown;

    //
    // Getters and Setters
    //

    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    @JsonProperty("access_token")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("home_server")
    public String getHomeServer() {
        return homeServer;
    }

    @JsonProperty("home_server")
    public void setHomeServer(String homeServer) {
        this.homeServer = homeServer;
    }

    @JsonProperty("device_id")
    public String getDeviceId() {
        return deviceId;
    }

    @JsonProperty("device_id")
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @JsonProperty("well_known")
    public MDiscoveryInformationType getWellKnown() {
        return wellKnown;
    }

    @JsonProperty("well_known")
    public void setWellKnown(MDiscoveryInformationType wellKnown) {
        this.wellKnown = wellKnown;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "MSimpleLoginResponse{" +
                "userId='" + userId + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", homeServer='" + homeServer + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", wellKnown=" + wellKnown +
                '}';
    }
}
