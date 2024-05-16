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
package net.fhirfactory.dricats.internals.fhir.r4.resources.messageheader.factories;

import net.fhirfactory.dricats.core.constants.systemwide.PegacornReferenceProperties;
import org.hl7.fhir.r4.model.Coding;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MessageHeaderEventFactory {

    private static final String DRICATS_MESSAGE_HEADER_EVENT_TYPE = "/message-header/hl7v2x-event-type";
    private static final String DRICATS_HL7_V2_MESSAGE_EVENT_TYPE = "HL7v2x";

    @Inject
    private PegacornReferenceProperties systemWideProperties;

    //
    // Getters (and Setters)
    //

    public String getDricatsMessageHeaderEventType() {
        String codeSystem = systemWideProperties.getPegacornCodeSystemSite() + DRICATS_MESSAGE_HEADER_EVENT_TYPE;
        return (codeSystem);
    }


    //
    // Business Methods
    //

    public Coding createHL7v2MessageEvent() {
        Coding messageHeaderEvent = new Coding();
        messageHeaderEvent.setSystem(getDricatsMessageHeaderEventType());
        messageHeaderEvent.setCode(DRICATS_HL7_V2_MESSAGE_EVENT_TYPE);
        messageHeaderEvent.setDisplay("HL7 v2.x Trigger Event");
        return (messageHeaderEvent);
    }

    public Coding createHL7v2MessageEvent(String eventType) {
        Coding messageHeaderEvent = new Coding();
        messageHeaderEvent.setSystem(getDricatsMessageHeaderEventType());
        messageHeaderEvent.setCode(eventType);
        messageHeaderEvent.setDisplay(eventType);
        return (messageHeaderEvent);
    }

    public Coding createHL7v2MessageEvent(String eventType, String eventTrigger) {
        Coding messageHeaderEvent = new Coding();
        messageHeaderEvent.setSystem(getDricatsMessageHeaderEventType());
        messageHeaderEvent.setCode(eventType);
        messageHeaderEvent.setDisplay(eventTrigger);
        return (messageHeaderEvent);
    }
}
