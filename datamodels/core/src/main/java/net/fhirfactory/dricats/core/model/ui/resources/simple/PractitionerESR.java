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
package net.fhirfactory.dricats.core.model.ui.resources.simple;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.fhirfactory.dricats.core.model.ui.resources.simple.common.ExtremelySimplifiedResource;
import net.fhirfactory.dricats.core.model.ui.resources.simple.datatypes.EmailAddress;
import net.fhirfactory.dricats.core.model.ui.resources.simple.datatypes.FavouriteListESDT;
import net.fhirfactory.dricats.core.model.ui.resources.simple.datatypes.IdentifierESDT;
import net.fhirfactory.dricats.core.model.ui.resources.simple.datatypes.PractitionerStatusESDT;
import net.fhirfactory.dricats.core.model.ui.resources.simple.valuesets.ExtremelySimplifiedResourceTypeEnum;
import net.fhirfactory.dricats.core.model.ui.resources.simple.valuesets.IdentifierESDTUseEnum;
import org.hl7.fhir.r4.model.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class PractitionerESR extends PersonESR {
    private static final Logger LOG = LoggerFactory.getLogger(PractitionerESR.class);
    public static Comparator<ExtremelySimplifiedResource> identifierEmailAddressBasedComparator = new Comparator<ExtremelySimplifiedResource>() {
        @Override
        public int compare(ExtremelySimplifiedResource o1, ExtremelySimplifiedResource o2) {
            if (o1 == null && o2 == null) {
                return (0);
            }
            if (o1 == null && o2 != null) {
                return (1);
            }
            if (o1 != null && o2 == null) {
                return (-1);
            }
            if (o1.getIdentifiers() == null && o2.getIdentifiers() == null) {
                return (0);
            }
            if (o1.getIdentifiers().isEmpty() && o2.getIdentifiers().isEmpty()) {
                return (0);
            }
            if (o1.getIdentifiers().isEmpty() && !o2.getIdentifiers().isEmpty()) {
                return (1);
            }
            if (!o1.getIdentifiers().isEmpty() && o2.getIdentifiers().isEmpty()) {
                return (-1);
            }
            IdentifierESDT practitionerRole1Identifier = o1.getIdentifierWithType("EmailAddress");
            IdentifierESDT practitionerRole2Identifier = o2.getIdentifierWithType("EmailAddress");
            if (practitionerRole1Identifier == null && practitionerRole2Identifier == null) {
                return (0);
            }
            if (practitionerRole1Identifier == null && practitionerRole2Identifier != null) {
                return (1);
            }
            if (practitionerRole1Identifier != null && practitionerRole2Identifier == null) {
                return (-1);
            }
            String testValue1 = practitionerRole1Identifier.getValue();
            String testValue2 = practitionerRole2Identifier.getValue();
            int comparison = testValue1.compareTo(testValue2);
            return (comparison);
        }
    };
    public static Comparator<ExtremelySimplifiedResource> identifierMatrixUserIDBasedComparator = new Comparator<ExtremelySimplifiedResource>() {
        @Override
        public int compare(ExtremelySimplifiedResource o1, ExtremelySimplifiedResource o2) {
            if (o1 == null && o2 == null) {
                return (0);
            }
            if (o1 == null && o2 != null) {
                return (1);
            }
            if (o1 != null && o2 == null) {
                return (-1);
            }
            if (o1.getIdentifiers() == null && o2.getIdentifiers() == null) {
                return (0);
            }
            if (o1.getIdentifiers().isEmpty() && o2.getIdentifiers().isEmpty()) {
                return (0);
            }
            if (o1.getIdentifiers().isEmpty() && !o2.getIdentifiers().isEmpty()) {
                return (1);
            }
            if (!o1.getIdentifiers().isEmpty() && o2.getIdentifiers().isEmpty()) {
                return (-1);
            }
            IdentifierESDT practitionerRole1Identifier = o1.getIdentifierWithType("user_id");
            IdentifierESDT practitionerRole2Identifier = o2.getIdentifierWithType("user_id");
            if (practitionerRole1Identifier == null && practitionerRole2Identifier == null) {
                return (0);
            }
            if (practitionerRole1Identifier == null && practitionerRole2Identifier != null) {
                return (1);
            }
            if (practitionerRole1Identifier != null && practitionerRole2Identifier == null) {
                return (-1);
            }
            String testValue1 = practitionerRole1Identifier.getValue();
            String testValue2 = practitionerRole2Identifier.getValue();
            int comparison = testValue1.compareTo(testValue2);
            return (comparison);
        }
    };
    private ArrayList<String> currentPractitionerRoles;
    private HashMap<String, IdentifierESDT> organizationMembership;
    private FavouriteListESDT practitionerRoleFavourites;
    private FavouriteListESDT healthcareServiceFavourites;
    private FavouriteListESDT practitionerFavourites;
    private PractitionerStatusESDT practitionerStatus;

    public PractitionerESR() {
        super();
        this.organizationMembership = new HashMap<>();
        this.currentPractitionerRoles = new ArrayList<>();
        this.practitionerStatus = new PractitionerStatusESDT();
        this.practitionerFavourites = new FavouriteListESDT();
        this.healthcareServiceFavourites = new FavouriteListESDT();
        this.practitionerRoleFavourites = new FavouriteListESDT();
        this.setResourceESRType(ExtremelySimplifiedResourceTypeEnum.ESR_PRACTITIONER);
    }

    @Override
    protected Logger getLogger() {
        return (LOG);
    }

    public ArrayList<String> getCurrentPractitionerRoles() {
        return currentPractitionerRoles;
    }

    public void setCurrentPractitionerRoles(ArrayList<String> currentPractitionerRoles) {
        this.currentPractitionerRoles = currentPractitionerRoles;
    }

    public HashMap<String, IdentifierESDT> getOrganizationMembership() {
        return organizationMembership;
    }

    public void setOrganizationMembership(HashMap<String, IdentifierESDT> organizationMembership) {
        this.organizationMembership = organizationMembership;
    }

    public FavouriteListESDT getPractitionerRoleFavourites() {
        return practitionerRoleFavourites;
    }

    public void setPractitionerRoleFavourites(FavouriteListESDT practitionerRoleFavourites) {
        this.practitionerRoleFavourites = practitionerRoleFavourites;
    }

    public FavouriteListESDT getHealthcareServiceFavourites() {
        return healthcareServiceFavourites;
    }

    public void setHealthcareServiceFavourites(FavouriteListESDT healthcareServiceFavourites) {
        this.healthcareServiceFavourites = healthcareServiceFavourites;
    }

    public FavouriteListESDT getPractitionerFavourites() {
        return practitionerFavourites;
    }

    public void setPractitionerFavourites(FavouriteListESDT practitionerFavourites) {
        this.practitionerFavourites = practitionerFavourites;
    }

    public PractitionerStatusESDT getPractitionerStatus() {
        return practitionerStatus;
    }

    public void setPractitionerStatus(PractitionerStatusESDT practitionerStatus) {
        this.practitionerStatus = practitionerStatus;
    }

    @JsonIgnore
    public EmailAddress getEmailAddress() {
        IdentifierESDT foundIdentifier = getIdentifierWithType("EmailAddress");
        if (foundIdentifier == null) {
            return (null);
        }
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.setValue(foundIdentifier.getValue());
        return (emailAddress);
    }

    //
    // Identifier Type based Comparator
    //

    @JsonIgnore
    public void setEmailAddress(String email) {
        IdentifierESDT identifier = new IdentifierESDT();
        identifier.setValue(email);
        identifier.setType("EmailAddress");
        identifier.setUse(IdentifierESDTUseEnum.USUAL);
        addIdentifier(identifier);
    }

    //
    // Identifier Type based Comparator
    //

    @JsonIgnore
    public String getEmailAddressUserNamePart() {
        IdentifierESDT foundIdentifier = getIdentifierWithType("EmailAddress");
        if (foundIdentifier == null) {
            return (null);
        }
        String[] emailAddressParts = foundIdentifier.getValue().split("@");
        String emailAddressUserNamePart = emailAddressParts[0];
        return (emailAddressUserNamePart);
    }

    @Override
    protected ResourceType specifyResourceType() {
        return (ResourceType.Practitioner);
    }
}
