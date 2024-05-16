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
package net.fhirfactory.pegacorn.communicate.matrix.model.r110.events.common.contenttypes;

import net.fhirfactory.dricats.core.model.dataparcel.DataParcelTypeDescriptor;
import net.fhirfactory.pegacorn.communicate.matrix.model.constants.MatrixClientServiceAPIConstants;

public enum MEventTypeEnum {
    M_CALL_ANSWER("m.call.answer"),
    M_CALL_CANDIDATES("m.call.candidates"),
    M_CALL_HANGUP("m.call.hangup"),
    M_CALL_INVITE("m.call.invite"),
    M_DIRECT("m.direct"),
    M_FULLY_READ("m.fully_read"),
    M_IGNORED_USER_LIST("m.ignored_user_list"),
    M_PRESENCE("m.presence"),
    M_RECEIPT("m.receipt"),
    M_TAG("m.tag"),
    M_TYPING("m.typing"),
    M_POLICY_RULE_ROOM("m.policy.rule.room"),
    M_POLICY_RULE_SERVER("m.policy.rule.server"),
    M_POLICY_RULE_USER("m.policy.rule.user"),
    M_ROOM_ENCRYPTED("m.room.encrypted"),
    M_ROOM_ENCRYPTION("m.room.encryption"),
    M_ROOM_KEY("m.room_key"),
    M_ROOM_KEY_REQUEST("m.room_key_request"),
    M_FORWARDED_ROOM_KEY("m.forwarded_room_key"),
    M_DUMMY("m.dummy"),
    M_ROOM_CANONICAL_ALIAS("m.room.canonical_alias"),
    M_ROOM_CREATE("m.room.create"),
    M_ROOM_GUEST_ACCESS("m.room.guest_access"),
    M_ROOM_HISTORY_VISIBILITY("m.room.history_visibility"),
    M_ROOM_JOIN_RULES("m.room.join_rules"),
    M_ROOM_MEMBER("m.room.member"),
    M_ROOM_MESSAGE("m.room.message"),
    M_ROOM_MESSAGE_FEEDBACK("m.room.message.feedback"),
    M_ROOM_NAME("m.room.name"),
    M_ROOM_POWER_LEVELS("m.room.power_levels"),
    M_ROOM_REDACTION("m.room.redaction"),
    M_ROOM_SERVER_ACL("m.room.server_acl"),
    M_ROOM_THIRD_PARTY_INVITE("m.room.third_party_invite"),
    M_ROOM_TOMBSTONE("m.room.tombstone"),
    M_ROOM_TOPIC("m.room.topic"),
    M_SPACE_CHILD("m.space.child"),
    M_SPACE_PARENT("m.space.parent"),
    M_KEY_VERIFICATION_REQUEST("m.key.verification.request"),
    M_KEY_VERIFICATION_START("m.key.verification.start"),
    M_KEY_VERIFICATION_CANCEL("m.key.verification.cancel"),
    M_KEY_VERIFICATION_ACCEPT("m.key.verification.accept"),
    M_KEY_VERIFICATION_KEY("m.key.verification.key"),
    M_KEY_VERIFICATION_MAC("m.key.verification.mac");

    private String eventType;
    private DataParcelTypeDescriptor typeDescriptor;

    private MEventTypeEnum(String eventType) {
        this.eventType = eventType;
        setEventTypeDescriptory(eventType);
    }

    public static MEventTypeEnum fromString(String text) {
        for (MEventTypeEnum evType : MEventTypeEnum.values()) {
            if (evType.eventType.equalsIgnoreCase(text)) {
                return evType;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return (this.eventType);
    }

    public String getEventType() {
        return eventType;
    }

    public DataParcelTypeDescriptor getTypeDescriptor() {
        return (this.typeDescriptor);
    }

    private void setEventTypeDescriptory(String eventType) {
        DataParcelTypeDescriptor parcelTypeDescriptor = new DataParcelTypeDescriptor();
        parcelTypeDescriptor.setDataParcelDefiner("Matrix");
        parcelTypeDescriptor.setDataParcelCategory("ClientServerAPI");
        parcelTypeDescriptor.setDataParcelCategory(eventType);
        switch (eventType) {
            case "m.call.answer":
            case "m.call.candidates":
            case "m.call.hangup":
            case "m.call.invite":
                parcelTypeDescriptor.setDataParcelSubCategory("CallEvents");
                break;
            case "m.direct":
            case "m.fully_read":
            case "m.ignored_user_list":
            case "m.presence":
            case "m.receipt":
            case "m.tag":
            case "m.typing":
                parcelTypeDescriptor.setDataParcelSubCategory("UserEvents");
                break;
            case "m.policy.rule.room":
            case "m.policy.rule.server":
            case "m.policy.rule.user":
            case "m.room.canonical_alias":
                parcelTypeDescriptor.setDataParcelSubCategory("PolicyEvents");
                break;
            case "m.room.create":
            case "m.room.guest_access":
            case "m.room.history_visibility":
            case "m.room.join_rules":
            case "m.room.member":
            case "m.room.message":
            case "m.room.message.feedback":
            case "m.room.name":
            case "m.room.power_levels":
            case "m.room.redaction":
            case "m.room.server_acl":
            case "m.room.third_party_invite":
            case "m.room.tombstone":
            case "m.room.topic":
            case "m.room.avatar":
            case "m.room.pinned_events":
                parcelTypeDescriptor.setDataParcelSubCategory("RoomEvents");
                break;
            case "m.key.verification.request":
            case "m.key.verification.start":
            case "m.key.verification.cancel":
            case "m.key.verification.accept":
            case "m.key.verification.key":
            case "m.key.verification.mac":
                parcelTypeDescriptor.setDataParcelSubCategory("DeviceVerification");
                break;
            case "m.room.encryption":
            case "m.room.encrypted":
            case "m.room_key":
            case "m.room_key_request":
            case "m.forwarded_room_key":
            case "m.dummy":
                parcelTypeDescriptor.setDataParcelSubCategory("ProtocolDefinitions");
                break;
            case "m.space.paren":
            case "m.space.child":
                parcelTypeDescriptor.setDataParcelSubCategory("SpaceEvents");
            default:
                parcelTypeDescriptor.setDataParcelSubCategory("General");
        }
        parcelTypeDescriptor.setVersion(MatrixClientServiceAPIConstants.MATRIX_CLIENT_SERVICES_API_RELEASE);
        this.typeDescriptor = parcelTypeDescriptor;
    }
}
