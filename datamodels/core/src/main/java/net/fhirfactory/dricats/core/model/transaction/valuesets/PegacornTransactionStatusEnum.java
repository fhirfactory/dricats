/*
 * Copyright (c) 2020 Mark A. Hunter (ACT Health)
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
package net.fhirfactory.dricats.core.model.transaction.valuesets;

public enum PegacornTransactionStatusEnum {
    CREATION_START,
    CREATION_IN_PROGRESS,
    CREATION_FINISH,
    CREATION_FAILURE,
    CREATION_NOT_REQUIRED,
    CREATED_PAUSED,
    UPDATE_START,
    UPDATE_IN_PROGRESS,
    UPDATE_FINISH,
    UPDATE_FAILURE,
    UPDATE_PAUSED,
    UPDATE_NOT_REQUIRED,
    DELETE_START,
    DELETE_IN_PROGRESS,
    DELETE_FINISH,
    DELETE_FAILURE,
    DELETE_PAUSED,
    REVIEW_START,
    REVIEW_IN_PROGRESS,
    REVIEW_FINISH,
    REVIEW_RESOURCE_NOT_IN_CACHE,
    REVIEW_FAILURE,
    REVIEW_PAUSED,
    SYNCHRONISING,
    LOADING_START,
    LOADING_IN_PROGRESS,
    LOADING_FINISH,
    LOADING_FAILURE,
    SEARCH_FINISHED,
    SEARCH_FAILURE,
    SYNC_FINISHED,
    SYNC_FAILURE,
    INDETERMINANT

}
