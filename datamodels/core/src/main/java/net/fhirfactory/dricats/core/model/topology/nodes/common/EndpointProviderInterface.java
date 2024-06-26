package net.fhirfactory.dricats.core.model.topology.nodes.common;

import net.fhirfactory.dricats.core.model.componentid.*;
import net.fhirfactory.dricats.core.model.topology.mode.ConcurrencyModeEnum;
import net.fhirfactory.dricats.core.model.topology.mode.ResilienceModeEnum;

public interface EndpointProviderInterface {
    public void addEndpoint(TopologyNodeFDN endpointFDN);

    public TopologyNodeRDN getComponentRDN();

    public TopologyNodeFDN getComponentFDN();

    public ComponentIdType getComponentID();

    public TopologyNodeFunctionFDN getNodeFunctionFDN();

    public PegacornSystemComponentTypeTypeEnum getComponentType();

    public ResilienceModeEnum getResilienceMode();

    public ConcurrencyModeEnum getConcurrencyMode();

    public String getParticipantName();
}
