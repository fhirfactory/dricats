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
package net.fhirfactory.dricats.internals.communicate.entities.message.datatypes;

import java.util.Date;

public class CommunicateMessageIdentifier {
    private String messageID;
    private String sourceRoomID;
    private String sourceUserID;
    private Date messageDate;

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getSourceRoomID() {
        return sourceRoomID;
    }

    public void setSourceRoomID(String sourceRoomID) {
        this.sourceRoomID = sourceRoomID;
    }

    public String getSourceUserID() {
        return sourceUserID;
    }

    public void setSourceUserID(String sourceUserID) {
        this.sourceUserID = sourceUserID;
    }

    public Date getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Date messageDate) {
        this.messageDate = messageDate;
    }

    @Override
    public String toString() {
        return "CommunicateMessageIdentifier{" +
                "messageID=" + messageID +
                ", sourceRoomID=" + sourceRoomID +
                ", sourceUserID=" + sourceUserID +
                ", messageDate=" + messageDate +
                "}";
    }
}