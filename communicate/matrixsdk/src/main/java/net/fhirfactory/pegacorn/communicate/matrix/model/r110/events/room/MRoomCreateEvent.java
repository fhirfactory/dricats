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
package net.fhirfactory.pegacorn.communicate.matrix.model.r110.events.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.events.room.common.MRoomEventBase;
import net.fhirfactory.pegacorn.communicate.matrix.model.r110.events.room.contenttypes.MRoomCreateContentType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MRoomCreateEvent extends MRoomEventBase {
    private MRoomCreateContentType content;

    public MRoomCreateEvent() {
        super();
        this.content = null;
    }

    @JsonProperty("content")
    public MRoomCreateContentType getContent() {
        return content;
    }

    @JsonProperty("content")
    public void setContent(MRoomCreateContentType content) {
        this.content = content;
    }

    @JsonIgnore
    public boolean hasContent() {
        if (this.content == null) {
            return (false);
        } else {
            return (true);
        }
    }
}
