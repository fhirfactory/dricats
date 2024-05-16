package net.fhirfactory.pegacorn.communicate.synapse.model.datatypes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SynapseRoomDelete implements Serializable {
    private String message;
    private boolean block;
    private boolean purge;

    //
    // Getters and Setters
    //

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("block")
    public boolean isBlock() {
        return block;
    }

    @JsonProperty("block")
    public void setBlock(boolean block) {
        this.block = block;
    }

    @JsonProperty("purge")
    public boolean isPurge() {
        return purge;
    }

    @JsonProperty("purge")
    public void setPurge(boolean purge) {
        this.purge = purge;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "SynapseRoomDelete{" +
                "message='" + message + '\'' +
                ", block=" + block +
                ", purge=" + purge +
                '}';
    }
}
