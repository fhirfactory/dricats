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
package net.fhirfactory.dricats.core.tasks.factories.metadata;

import net.fhirfactory.dricats.core.constants.petasos.PetasosPropertyConstants;
import net.fhirfactory.dricats.core.model.uow.UoWPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class GeneralTaskMetadataExtractor {
    private static final Logger LOG = LoggerFactory.getLogger(GeneralTaskMetadataExtractor.class);

    private DateTimeFormatter timeFormatter;

    //
    // Constructor(s)
    //

    public GeneralTaskMetadataExtractor() {
        timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss.SSS").withZone(ZoneId.of(PetasosPropertyConstants.DEFAULT_TIMEZONE));
    }

    //
    // Business Methods
    //

    public List<String> getGeneralHeaderDetail(UoWPayload payload) {
        List<String> headerInfo = new ArrayList<>();
        if (payload.getPayloadManifest().hasContainerDescriptor()) {
            StringBuilder containerBuilder = new StringBuilder();
            String containerMessageDefiner = payload.getPayloadManifest().getContainerDescriptor().getDataParcelDefiner();
            String containerMessageCategory = payload.getPayloadManifest().getContainerDescriptor().getDataParcelCategory();
            String containerMessageSubcategory = payload.getPayloadManifest().getContainerDescriptor().getDataParcelSubCategory();
            String containerMessageResource = payload.getPayloadManifest().getContainerDescriptor().getDataParcelResource();
            String containerMessageVersion = payload.getPayloadManifest().getContainerDescriptor().getVersion();
            containerBuilder.append("Container Payload: " + containerMessageDefiner + "::" + containerMessageCategory + "::" + containerMessageSubcategory + "::" + containerMessageResource + "(" + containerMessageVersion + ")");
            headerInfo.add(containerBuilder.toString());
        }
        StringBuilder contentBuilder = new StringBuilder();
        String contentDescriptor = payload.getPayloadManifest().getContentDescriptor().getDataParcelDefiner();
        String contentMessageCategory = payload.getPayloadManifest().getContentDescriptor().getDataParcelCategory();
        String contentMessageSubcategory = payload.getPayloadManifest().getContentDescriptor().getDataParcelSubCategory();
        String contentMessageResource = payload.getPayloadManifest().getContentDescriptor().getDataParcelResource();
        String contentMessageVersion = payload.getPayloadManifest().getContentDescriptor().getVersion();
        contentBuilder.append("Content Payload: " + contentDescriptor + "::" + contentMessageCategory + "::" + contentMessageSubcategory + "::" + contentMessageResource + "(" + contentMessageVersion + ")");
        headerInfo.add(contentBuilder.toString());
        return (headerInfo);
    }
}
