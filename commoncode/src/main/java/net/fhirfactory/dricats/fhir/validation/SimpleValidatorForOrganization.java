/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package net.fhirfactory.dricats.fhir.validation;

import org.hl7.fhir.r4.model.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mark A. Hunter (ACT Health)
 */
public class SimpleValidatorForOrganization extends SimpleValidatorBase {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleValidatorForOrganization.class);

    protected Logger getLogger() {
        return (LOG);
    }

    public boolean isSimplyValid(Organization pOrganization) {
        getLogger().debug("isSimplyValid(Organization p): Entry");

        return (false);
    }

}
