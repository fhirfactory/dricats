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
package net.fhirfactory.pegacorn.communicate.matrix.model.r110.api.datatypes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MStateEvent implements Serializable {
    private String type;
    private String stateKey;
    private Object content;

    //
    // Constructor(s)
    //

    public MStateEvent() {
        this.type = null;
        this.stateKey = null;
        this.content = null;
    }

    //
    // Getters and Setters
    //

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("state_key")
    public String getStateKey() {
        return stateKey;
    }

    @JsonProperty("state_key")
    public void setStateKey(String stateKey) {
        this.stateKey = stateKey;
    }

    @JsonProperty("content")
    public Object getContent() {
        return content;
    }

    @JsonProperty("content")
    public void setContent(Object content) {
        this.content = content;
    }

    //
    // to String
    //

    @Override
    public String toString() {
        return "MStateEvent{" +
                "type='" + type + '\'' +
                ", stateKey='" + stateKey + '\'' +
                ", content=" + content +
                '}';
    }
}
