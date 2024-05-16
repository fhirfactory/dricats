/*
 * Copyright (c) 2022 Mark A. Hunter (ACT Health)
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
package net.fhirfactory.dricats.datagrid.poc;

import net.fhirfactory.dricats.datagrid.cachestore.fhirserver.configuration.FHIRServerCacheStoreConfigurationBuilder;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.Resource;
import org.infinispan.Cache;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.PersistenceConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.Locale;

@ApplicationScoped
public class TestTaskCache {
    private static final Logger LOG = LoggerFactory.getLogger(TestTaskCache.class);

    private boolean initialised;

    private Cache<String, Resource> taskCache;
    private Object taskCacheLock;

    //
    // Constructor
    //

    public TestTaskCache() {
        super();
        this.initialised = false;
        this.taskCacheLock = new Object();
    }

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (LOG);
    }

    //
    // Post Construct
    //

    @PostConstruct
    public void initialise() {
        getLogger().debug(".initialise(): Entry");
        if (!initialised) {
            getLogger().info(".initialise(): Initialisation Start");

            getLogger().info(".initialise(): [Initialising Caches] Start");
            if (!initialised) {
                getLogger().info(".initialise(): Initialisation Start");
                //
                // Get the Infinispan-Task-JGroups-Configuration-File
                getLogger().info(".initialise(): [Retrieve JGroups Configuration Detail] Start");
                String jgroupsConfigFileName = getEnvironmentVariable("JGROUPS_CONFIG_FILE_NAME");
                String jgroupsClusterName = getEnvironmentVariable("JGROUPS_CLUSTER_NAME");
                getLogger().info(".initialise(): [Retrieve JGroups Configuration Detail] jgroupsConfigFileName->{}", jgroupsConfigFileName);
                getLogger().info(".initialise(): [Retrieve JGroups Configuration Detail] jgroupsClusterName->{}", jgroupsClusterName);
                getLogger().info(".initialise(): [Retrieve JGroups Configuration Detail] End");

                getLogger().info(".initialise(): [Initialising Infinispan Cache Manager] Start");
                GlobalConfiguration globalConfig = new GlobalConfigurationBuilder()
                        .transport()
                        .defaultTransport()
                        .clusterName(jgroupsClusterName)
                        .addProperty("configurationFile", jgroupsConfigFileName)
                        .build();
                DefaultCacheManager cacheManager = new DefaultCacheManager(globalConfig);
                // Create a distributed cache with synchronous replication.


                getLogger().info(".initialise(): [Retrieve FHIR Server Configuration Detail] Start");
                String fhirServerHost = getEnvironmentVariable("FHIR_SERVER_HOST");
                getLogger().info(".initialise(): [Retrieve FHIR Server Configuration Detail] Host->{}", fhirServerHost);
                String fhirServerPort = getEnvironmentVariable("FHIR_SERVER_PORT");
                getLogger().info(".initialise(): [Retrieve FHIR Server Configuration Detail] Port->{}", fhirServerPort);
                String fhirServerContextPath = getEnvironmentVariable("FHIR_SERVER_CONTEXT_PATH");
                getLogger().info(".initialise(): [Retrieve FHIR Server Configuration Detail] contextPath->{}", fhirServerContextPath);
                String fhirServerPortTimeout = getEnvironmentVariable("FHIR_SERVER_PORT_TIMEOUT");
                getLogger().info(".initialise(): [Retrieve FHIR Server Configuration Detail] portTimeout->{}", fhirServerPortTimeout);
                String fhirServerRequiresEncryption = getEnvironmentVariable("FHIR_SERVER_REQUIRES_ENCRYPTION");
                getLogger().info(".initialise(): [Retrieve FHIR Server Configuration Detail] requiresEncryption->{}", fhirServerRequiresEncryption);
                getLogger().info(".initialise(): [Retrieve FHIR Server Configuration Detail] End");

                PersistenceConfigurationBuilder persistenceBuilder = new ConfigurationBuilder().persistence();
                Configuration cacheStoreConfiguration = new FHIRServerCacheStoreConfigurationBuilder(persistenceBuilder)
                        .host(fhirServerHost)
                        .port(Integer.parseInt(fhirServerPort))
                        .contextPath(fhirServerContextPath)
                        .requiresEncryption(Boolean.parseBoolean(fhirServerRequiresEncryption))
                        .clustering().cacheMode(CacheMode.REPL_SYNC)
                        .build();
                getLogger().info(".initialise(): [Initialising Infinispan Cache Manager] End");

                taskCache = cacheManager.createCache("TaskCache", cacheStoreConfiguration);
                getLogger().info(".initialise(): Initialisation Finished...");
                this.initialised = true;
            }
        }
    }


    //
    // Helper Methods
    //

    protected String getEnvironmentVariable(String variableName) {
        if (StringUtils.isEmpty(variableName)) {
            getLogger().info(".getEnvironmentVariable(): variableName is empty, returning -null-");
            return (null);
        }

        String env = variableName.toUpperCase(Locale.US);
        env = env.replace(".", "_");
        env = env.replace("-", "_");

        String logMsg = null;

        String value = System.getenv(env);
        if (StringUtils.isNotBlank(value)) {
            getLogger().warn(".getEnvironmentVariable(): Found value in environment variable");
            return (value);
        }
        getLogger().info(".getEnvironmentVariable(): Value not found in environment variables");
        return (null);
    }
}

