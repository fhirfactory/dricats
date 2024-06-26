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
package net.fhirfactory.dricats.itops.im.datatypes;

import net.fhirfactory.pegacorn.communicate.matrix.model.core.MatrixRoom;

import java.io.Serializable;

public class ProcessingPlantSpaceDetail implements Serializable {
    MatrixRoom processingPlantSpace;
    MatrixRoom processingPlantComponentSpace;


    //
    // Constructor(s)
    //

    public ProcessingPlantSpaceDetail() {
        this.processingPlantComponentSpace = null;
        this.processingPlantSpace = null;
    }

    //
    // Getters and Setters
    //

    public MatrixRoom getProcessingPlantSpace() {
        return processingPlantSpace;
    }

    public void setProcessingPlantSpace(MatrixRoom processingPlantSpace) {
        this.processingPlantSpace = processingPlantSpace;
    }

    public MatrixRoom getProcessingPlantComponentSpace() {
        return processingPlantComponentSpace;
    }

    public void setProcessingPlantComponentSpace(MatrixRoom processingPlantComponentSpace) {
        this.processingPlantComponentSpace = processingPlantComponentSpace;
    }

    //
    // To String
    //

    @Override
    public String toString() {
        return "ProcessingPlantSpaceDetail{" +
                "processingPlantSpace=" + processingPlantSpace +
                ", processingPlantComponentSpace=" + processingPlantComponentSpace +
                '}';
    }
}
