package net.fhirfactory.pegacorn.communicate.matrix.model.r110.events.room;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.events.room.common.MRoomEventBase;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.events.room.contenttypes.MRoomJoinRulesContentType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MRoomJoinRulesEvent extends MRoomEventBase {
    private MRoomJoinRulesContentType content;

    @JsonProperty("content")
    public MRoomJoinRulesContentType getContent() {
        return content;
    }

    @JsonProperty("content")
    public void setContent(MRoomJoinRulesContentType content) {
        this.content = content;
    }
}
