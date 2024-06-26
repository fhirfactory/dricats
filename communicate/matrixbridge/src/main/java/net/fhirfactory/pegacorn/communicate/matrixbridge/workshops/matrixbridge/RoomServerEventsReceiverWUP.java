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
package net.fhirfactory.pegacorn.communicate.matrixbridge.workshops.matrixbridge;

import net.fhirfactory.dricats.core.model.dataparcel.DataParcelManifest;
import net.fhirfactory.pegacorn.communicate.matrix.issi.receiver.MatrixApplicationServicesEventsReceiverWUP;
import net.fhirfactory.pegacorn.communicate.matrixbridge.common.MatrixBridgeNames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RoomServerEventsReceiverWUP extends MatrixApplicationServicesEventsReceiverWUP {
    private static final Logger LOG = LoggerFactory.getLogger(RoomServerEventsReceiverWUP.class);

    @Inject
    private MatrixBridgeNames names;

    @Override
    protected String specifyIngresTopologyEndpointName() {
        return (names.getInteractIngressMatrixEventsName());
    }

    @Override
    public String specifyWUPInstanceVersion() {
        return ("0.0.1");
    }

    @Override
    protected Logger specifyLogger() {
        return (LOG);
    }

    @Override
    protected List<DataParcelManifest> declarePublishedTopics() {
        return (new ArrayList<>());
    }

    @Override
    protected String specifyWUPInstanceName() {
        return (getClass().getSimpleName());
    }

    @Override
    protected String specifyEndpointParticipantName() {
        return (getClass().getSimpleName());
    }
}
