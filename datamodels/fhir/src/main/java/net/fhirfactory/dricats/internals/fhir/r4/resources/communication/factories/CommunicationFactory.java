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
package net.fhirfactory.dricats.internals.fhir.r4.resources.communication.factories;

import net.fhirfactory.dricats.core.constants.petasos.PetasosPropertyConstants;
import net.fhirfactory.dricats.core.model.task.datatypes.context.datatypes.HL7v2MessageHeader;
import net.fhirfactory.dricats.core.model.task.datatypes.context.datatypes.MLLPMessageActivityParcel;
import net.fhirfactory.dricats.internals.fhir.r4.codesystems.PegacornIdentifierCodeEnum;
import net.fhirfactory.dricats.internals.fhir.r4.resources.identifier.PegacornIdentifierFactory;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@ApplicationScoped
public class CommunicationFactory {
    private static final Logger LOG = LoggerFactory.getLogger(CommunicationFactory.class);

    private DateTimeFormatter dateTimeFormatter;

    @Inject
    private PegacornIdentifierFactory identifierFactory;

    //
    // Constructor(s)
    //

    public CommunicationFactory() {
        dateTimeFormatter = new DateTimeFormatterBuilder()
                .appendPattern("YYYY[MM[DD[HHMM[SS[.S[S[S[S]]]]]]]][+/-ZZZZ]")
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .parseDefaulting(ChronoField.SECOND_OF_DAY, 0)
                .toFormatter(Locale.ENGLISH)
                .withZone(ZoneId.of(PetasosPropertyConstants.DEFAULT_TIMEZONE));
    }

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (LOG);
    }

    protected DateTimeFormatter getDateTimeFormatter() {
        return (dateTimeFormatter);
    }

    //
    // Business Methods
    //

    public Communication newCommunicationResource(String idValue, Date date) {
        LOG.debug(".newCommunicationResource(): Entry, idValue->{}, date->{}", idValue, date);
        Communication communicationResource = new Communication();
        Period newPeriod = new Period();
        newPeriod.setStart(date);
        Identifier baseIdentifier = identifierFactory.newIdentifier(PegacornIdentifierCodeEnum.IDENTIFIER_CODE_HL7V2_COMMUNICATION_CONTAINER, idValue, newPeriod);
        communicationResource.addIdentifier(baseIdentifier);

        LOG.debug(".newCommunicationResource(): Exit, communicationResource->{}", communicationResource);
        return (communicationResource);
    }


    public Communication mapHL7v2MessageToFHIRCommunication(MLLPMessageActivityParcel mllpMessage) {
        getLogger().debug(".mapHL7v2MessageToFHIRCommunication(): Entry, mllpMessage->{}", mllpMessage);

        getLogger().trace(".mapHL7v2MessageToFHIRCommunication(): [Create Communication] [Create Empty Communication] Start");
        Communication receivedTriggerEvent = new Communication();
        getLogger().trace(".mapHL7v2MessageToFHIRCommunication(): [Create Communication] [Create Empty Communication] Finish");

        getLogger().trace(".mapHL7v2MessageToFHIRCommunication(): [Create Communication] [Create and Insert Communication.Identifier] Start");
        String triggerEventId = mllpMessage.getMllpMessageControlId();
        if (StringUtils.isEmpty(triggerEventId)) {
            triggerEventId = mllpMessage.getMllpProcessingId();
        }
        if (StringUtils.isEmpty(triggerEventId)) {
            triggerEventId = UUID.randomUUID().toString();
        }
        Identifier identifier = identifierFactory.newIdentifier(PegacornIdentifierCodeEnum.IDENTIFIER_CODE_HL7V2_TRIGGER_EVENT_COMMUNICATION, triggerEventId, null);
        receivedTriggerEvent.addIdentifier(identifier);
        getLogger().trace(".mapHL7v2MessageToFHIRCommunication(): [Create Communication] [Create and Insert Communication.Identifier] Finish");

        getLogger().trace(".mapHL7v2MessageToFHIRCommunication(): [Create Communication] [Create and Insert Communication.received] Start");
        String dateString = mllpMessage.getMllpTimestamp();
        TemporalAccessor parsedTime = getDateTimeFormatter().parse(dateString);
        Instant messageInstant = Instant.from(parsedTime);
        Date messageDate = Date.from(messageInstant);
        receivedTriggerEvent.setReceived(messageDate);
        getLogger().trace(".mapHL7v2MessageToFHIRCommunication(): [Create Communication] [Create and Insert Communication.received] Start");

        getLogger().trace(".mapHL7v2MessageToFHIRCommunication(): [Create Communication] [Create and Insert Communication.payload] Start");
        Communication.CommunicationPayloadComponent payload = new Communication.CommunicationPayloadComponent();
        boolean payloadIsPopulated = false;
        try {
            if (StringUtils.isNotEmpty(mllpMessage.getUow().getIngresContent().getPayload())) {
                StringType payloadStringType = new StringType(mllpMessage.getUow().getIngresContent().getPayload());
                payload.setContent(payloadStringType);
                payloadIsPopulated = true;
            }
        } catch (Exception ex) {
            getLogger().warn(".mapHL7v2MessageToFHIRCommunication(): Couldn't load payload from UoW, problem -->", ex);
        }
        if (!payloadIsPopulated) {
            StringType payloadStringType = new StringType("Not Available");
            payload.setContent(payloadStringType);
        }
        getLogger().trace(".mapHL7v2MessageToFHIRCommunication(): [Create Communication] [Create and Insert Communication.payload] Finish");

        getLogger().debug(".mapHL7v2MessageToFHIRCommunication(): Exit, receivedTriggerEvent->{}", receivedTriggerEvent);
        return (receivedTriggerEvent);
    }

    //
    // Helpers

    public MessageHeader.MessageSourceComponent mapMessageHeaderToFHIRMessageSourceComponent(HL7v2MessageHeader messageHeader) {
        if (messageHeader == null) {
            return (null);
        }
        MessageHeader.MessageSourceComponent sourceComponent = new MessageHeader.MessageSourceComponent();
        if (StringUtils.isNotEmpty(messageHeader.getMllpSendingApplication())) {
            sourceComponent.setName(messageHeader.getMllpSendingApplication());
        }
        String localAddress = messageHeader.getMllpConnectionLocalAddress();
        String url = "mllp://" + localAddress;
        sourceComponent.setEndpoint(url);
        return (sourceComponent);
    }
}
