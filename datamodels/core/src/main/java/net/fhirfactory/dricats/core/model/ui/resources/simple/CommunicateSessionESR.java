package net.fhirfactory.dricats.core.model.ui.resources.simple;

import net.fhirfactory.dricats.core.model.ui.resources.simple.common.ExtremelySimplifiedResource;
import org.hl7.fhir.r4.model.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommunicateSessionESR extends ExtremelySimplifiedResource {
    private static final Logger LOG = LoggerFactory.getLogger(CommunicateSessionESR.class);

    @Override
    protected Logger getLogger() {
        return (LOG);
    }

    @Override
    protected ResourceType specifyResourceType() {
        return (ResourceType.Communication);
    }
}
