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
package net.fhirfactory.pegacorn.communicate.synapse.methods.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.util.Objects;

public class SynapseAPIResponse implements Serializable {
    private int responseCode;
    private String responseContent;
    private String errorContent;

    public SynapseAPIResponse() {
        this.responseCode = 0;
        this.responseContent = null;
        this.errorContent = null;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public String getErrorContent() {
        return errorContent;
    }

    public void setErrorContent(String errorContent) {
        this.errorContent = errorContent;
    }

    @JsonIgnore
    public boolean isSuccessful() {
        switch (getResponseCode()) {
            case HttpURLConnection.HTTP_CREATED:
            case HttpURLConnection.HTTP_ACCEPTED:
            case HttpURLConnection.HTTP_OK:
                return (true);
            default:
                return (false);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SynapseAPIResponse)) return false;
        SynapseAPIResponse that = (SynapseAPIResponse) o;
        return getResponseCode() == that.getResponseCode() && Objects.equals(getResponseContent(), that.getResponseContent()) && Objects.equals(getErrorContent(), that.getErrorContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getResponseCode(), getResponseContent(), getErrorContent());
    }

    @Override
    public String toString() {
        return "SynapseAPIResponse{" +
                "responseCode=" + responseCode +
                ", responseContent='" + responseContent + '\'' +
                ", errorContent='" + errorContent + '\'' +
                '}';
    }
}
