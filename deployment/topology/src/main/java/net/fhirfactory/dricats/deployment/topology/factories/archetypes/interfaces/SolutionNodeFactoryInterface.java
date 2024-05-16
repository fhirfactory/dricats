package net.fhirfactory.dricats.deployment.topology.factories.archetypes.interfaces;

import net.fhirfactory.dricats.core.model.topology.nodes.SolutionTopologyNode;

public interface SolutionNodeFactoryInterface {
    public void initialise();

    public SolutionTopologyNode getSolutionTopologyNode();
}
