package net.fhirfactory.pegacorn.communicate.matrix.model.r110.events.common.contenttypes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MStrippedStateType implements Serializable {
    private MEventContentType content;
    private String stateKey;
    private String type;
    private String sender;

    public MEventContentType getContent() {
        return content;
    }

    public void setContent(MEventContentType content) {
        this.content = content;
    }

    @JsonProperty("state_key")
    public String getStateKey() {
        return stateKey;
    }

    @JsonProperty("state_key")
    public void setStateKey(String stateKey) {
        this.stateKey = stateKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
