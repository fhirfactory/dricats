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
package net.fhirfactory.pegacorn.communicate.matrix.model.r110.events.room.message.contenttypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.fhirfactory.dricats.core.model.dataparcel.DataParcelTypeDescriptor;
import net.fhirfactory.pegacorn.communicate.matrix.model.constants.MatrixClientServiceAPIConstants;

public enum MRoomMessageTypeEnum {
    VIDEO("m.video"),
    LOCATION("m.location"),
    AUDIO("m.audio"),
    FILE("m.file"),
    IMAGE("m.image"),
    NOTICE("m.notice"),
    EMOTE("m.emote"),
    TEXT("m.text"),
    SERVER_NOTICE("m.server_notice");

    private String msgtype;
    private DataParcelTypeDescriptor messageTypeDescriptor;

    private MRoomMessageTypeEnum(String msgtype) {
        this.msgtype = msgtype;
        setMessageTypeDescriptor(msgtype);
    }

    public static MRoomMessageTypeEnum fromString(String text) {
        for (MRoomMessageTypeEnum evType : MRoomMessageTypeEnum.values()) {
            if (evType.msgtype.equalsIgnoreCase(text)) {
                return evType;
            }
        }
        return null;
    }

    public String getMsgtype() {
        return (msgtype);
    }

    public DataParcelTypeDescriptor getMessageTypeDescriptor() {
        return messageTypeDescriptor;
    }

    @JsonIgnore
    private void setMessageTypeDescriptor(String msgtype) {
        DataParcelTypeDescriptor descriptor = new DataParcelTypeDescriptor();
        descriptor.setDataParcelDefiner("Matrix");
        descriptor.setDataParcelCategory("ClientServerAPI");
        descriptor.setDataParcelSubCategory("RoomEvents");
        descriptor.setDataParcelCategory("m.room.message");
        switch (msgtype) {
            case "m.video":
            case "m.location":
            case "m.audio":
            case "m.file":
            case "m.image":
            case "m.notice":
            case "m.emote":
            case "m.text":
            case "m.server_notice":
                descriptor.setDataParcelSegment(msgtype);
                break;
            default:
                descriptor.setDataParcelSegment("unknown");
        }
        descriptor.setVersion(MatrixClientServiceAPIConstants.MATRIX_CLIENT_SERVICES_API_RELEASE);
        this.messageTypeDescriptor = descriptor;
    }

    @Override
    public String toString() {
        return (this.msgtype);
    }
}
