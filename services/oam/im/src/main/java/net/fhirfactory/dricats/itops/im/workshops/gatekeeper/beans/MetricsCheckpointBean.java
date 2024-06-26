package net.fhirfactory.dricats.itops.im.workshops.gatekeeper.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.dricats.core.model.dataparcel.valuesets.PolicyEnforcementPointApprovalStatusEnum;
import net.fhirfactory.dricats.core.model.uow.UoW;
import net.fhirfactory.dricats.core.model.uow.UoWPayload;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MetricsCheckpointBean {
    private static final Logger LOG = LoggerFactory.getLogger(MetricsCheckpointBean.class);
    ObjectMapper jsonMapper;

    public MetricsCheckpointBean() {
        jsonMapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        this.jsonMapper.registerModule(module);
        this.jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    protected Logger getLogger() {
        return (LOG);
    }

    public UoW enforceInboundPolicy(UoW uow, Exchange camelExchange) {
        getLogger().debug(".enforceIngresPolicy(): Entry, uow->{}", uow);
        if (uow == null) {
            return (null);
        }
        if (!uow.hasIngresContent()) {
            return (uow);
        }
        UoWPayload ingresPayload = SerializationUtils.clone(uow.getIngresContent());
        ingresPayload.getPayloadManifest().setEnforcementPointApprovalStatus(PolicyEnforcementPointApprovalStatusEnum.POLICY_ENFORCEMENT_POINT_APPROVAL_POSITIVE);
        uow.getEgressContent().addPayloadElement(ingresPayload);
        return (uow);
    }
}
