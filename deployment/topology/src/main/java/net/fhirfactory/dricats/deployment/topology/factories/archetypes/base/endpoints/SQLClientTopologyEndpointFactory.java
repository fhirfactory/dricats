/*
 * Copyright (c) 2021 Mark A. Hunter (ACT Health)
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
package net.fhirfactory.dricats.deployment.topology.factories.archetypes.base.endpoints;

import net.fhirfactory.dricats.core.model.componentid.PegacornSystemComponentTypeTypeEnum;
import net.fhirfactory.dricats.core.model.componentid.TopologyNodeRDN;
import net.fhirfactory.dricats.core.model.endpoint.valuesets.PetasosEndpointTopologyTypeEnum;
import net.fhirfactory.dricats.core.model.ipc.PegacornCommonInterfaceNames;
import net.fhirfactory.dricats.core.model.topology.endpoints.sql.SQLClientTopologyEndpoint;
import net.fhirfactory.dricats.core.model.topology.nodes.common.EndpointProviderInterface;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.archetypes.BaseSubsystemPropertyFile;
import net.fhirfactory.dricats.deployment.properties.configurationfilebased.common.segments.ports.interact.InteractSQLClientPortSegment;
import net.fhirfactory.dricats.deployment.topology.factories.archetypes.base.common.TopologyFactoryHelpersBase;
import net.fhirfactory.dricats.deployment.topology.manager.TopologyIM;
import net.fhirfactory.dricats.util.PegacornProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SQLClientTopologyEndpointFactory extends TopologyFactoryHelpersBase {
    private static final Logger LOG = LoggerFactory.getLogger(SQLClientTopologyEndpointFactory.class);

    @Inject
    private PegacornProperties pegacornProperties;

    @Inject
    private PegacornCommonInterfaceNames interfaceNames;

    @Inject
    private TopologyIM topologyIM;

    //
    // Constructor(s)
    //

    //
    // Getters and Setters
    //

    @Override
    protected Logger getLogger() {
        return (LOG);
    }

    protected PegacornCommonInterfaceNames getInterfaceNames() {
        return (interfaceNames);
    }

    protected TopologyIM getTopologyIM() {
        return (topologyIM);
    }

    //
    // Business Methods
    //


    // Build a SQL Client Endpoint (Helper Method)
    //

    public SQLClientTopologyEndpoint newSQLClientTopologyEndpoint(BaseSubsystemPropertyFile propertyFile, EndpointProviderInterface endpointProvider, String endpointFunctionName, InteractSQLClientPortSegment sqlClientPortConfigurationSegment) {
        getLogger().debug(".newSQLClientTopologyEndpoint(): Entry, endpointProvider->{}, httpClientPortConfigurationSegment->{}", endpointProvider, sqlClientPortConfigurationSegment);
        SQLClientTopologyEndpoint sqlClientEndpoint = new SQLClientTopologyEndpoint();
        if (sqlClientPortConfigurationSegment == null) {
            getLogger().debug(".newSQLClientTopologyEndpoint(): Exit, no port to add");
            return (null);
        }
        String name = getInterfaceNames().getEndpointServerName(endpointFunctionName);
        TopologyNodeRDN nodeRDN = createSimpleNodeRDN(name, endpointProvider.getComponentRDN().getNodeVersion(), PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        sqlClientEndpoint.setComponentRDN(nodeRDN);
        sqlClientEndpoint.setEndpointConfigurationName(sqlClientPortConfigurationSegment.getName());
        sqlClientEndpoint.constructFDN(endpointProvider.getComponentFDN(), nodeRDN);
        sqlClientEndpoint.setEndpointType(PetasosEndpointTopologyTypeEnum.SQL_CLIENT);
        sqlClientEndpoint.setComponentType(PegacornSystemComponentTypeTypeEnum.ENDPOINT);
        sqlClientEndpoint.constructFunctionFDN(endpointProvider.getNodeFunctionFDN(), nodeRDN);
        sqlClientEndpoint.setComponentRDN(nodeRDN);
        sqlClientEndpoint.setParticipantName(endpointFunctionName);
        sqlClientEndpoint.setContainingNodeFDN(endpointProvider.getComponentFDN());
        sqlClientEndpoint.setDataSourceName(sqlClientPortConfigurationSegment.getDataSourceName());
        sqlClientEndpoint.setDriverClassName(sqlClientPortConfigurationSegment.getDriverClassName());
        sqlClientEndpoint.setConnectionURL(sqlClientPortConfigurationSegment.getConnectionURL());
        sqlClientEndpoint.setQueryTemplate(sqlClientPortConfigurationSegment.getQueryTemplate());


        //
        // Add the endpoint to the provider
        endpointProvider.addEndpoint(sqlClientEndpoint.getComponentFDN());
        //
        // Add the endpoint to the topology cache
        getLogger().trace(".newSQLClientTopologyEndpoint(): Add the httpClient Port to the Topology Cache");
        getTopologyIM().addTopologyNode(endpointProvider.getComponentFDN(), sqlClientEndpoint);
        //
        // We're done
        getLogger().debug(".newSQLClientTopologyEndpoint(): Exit, endpoint added");
        return (sqlClientEndpoint);
    }
}
