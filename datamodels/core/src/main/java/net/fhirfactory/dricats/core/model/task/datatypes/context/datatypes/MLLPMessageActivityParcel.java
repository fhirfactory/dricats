/*
 * Copyright (c) 2022 Mark A. Hunter
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
package net.fhirfactory.dricats.core.model.task.datatypes.context.datatypes;

import net.fhirfactory.dricats.core.model.uow.UoW;

import java.io.Serializable;

public class MLLPMessageActivityParcel extends HL7v2MessageHeader implements Serializable {
    private UoW uow;

    //
    // Constructor(s)
    //

    public MLLPMessageActivityParcel() {
        super();
        this.uow = null;
    }

    //
    // Getters and Setters
    //

    public UoW getUow() {
        return uow;
    }

    public void setUow(UoW uow) {
        this.uow = uow;
    }

    //
    // toString
    //


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MLLPMessageActivityParcel{");
        sb.append("uow=").append(uow);
        sb.append(", mllpSendingFacility='").append(getMllpSendingFacility()).append('\'');
        sb.append(", mllpSendingApplication='").append(getMllpSendingApplication()).append('\'');
        sb.append(", mllpConnectionLocalAddress='").append(getMllpConnectionLocalAddress()).append('\'');
        sb.append(", mllpConnectionRemoteAddress='").append(getMllpConnectionRemoteAddress()).append('\'');
        sb.append(", mllpAcknowledgement='").append(getMllpAcknowledgement()).append('\'');
        sb.append(", mllpAcknowledgementType='").append(getMllpAcknowledgementType()).append('\'');
        sb.append(", mllpReceivingFacility='").append(getMllpReceivingFacility()).append('\'');
        sb.append(", mllpReceivingApplication='").append(getMllpReceivingApplication()).append('\'');
        sb.append(", mllpTimestamp='").append(getMllpTimestamp()).append('\'');
        sb.append(", mllpSecurity='").append(getMllpSecurity()).append('\'');
        sb.append(", mllpMessageType='").append(getMllpMessageType()).append('\'');
        sb.append(", mllpEventType='").append(getMllpEventType()).append('\'');
        sb.append(", mllpTriggerEvent='").append(getMllpTriggerEvent()).append('\'');
        sb.append(", mllpMessageControlId='").append(getMllpMessageControlId()).append('\'');
        sb.append(", mllpProcessingId='").append(getMllpProcessingId()).append('\'');
        sb.append(", mllpVersionId='").append(getMllpVersionId()).append('\'');
        sb.append(", mllpCharSet='").append(getMllpCharSet()).append('\'');
        sb.append(", notes=").append(getNotes());
        sb.append('}');
        return sb.toString();
    }
}
