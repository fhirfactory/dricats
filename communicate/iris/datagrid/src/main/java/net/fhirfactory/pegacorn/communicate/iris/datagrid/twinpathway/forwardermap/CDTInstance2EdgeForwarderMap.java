package net.fhirfactory.pegacorn.communicate.iris.datagrid.twinpathway.forwardermap;


import net.fhirfactory.pegacorn.internals.communicate.workflow.model.CDTIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class CDTInstance2EdgeForwarderMap {
    private static final Logger LOG = LoggerFactory.getLogger(CDTInstance2EdgeForwarderMap.class);

    ConcurrentHashMap<CDTIdentifier, HashSet<String>> forwarderMap;

    public CDTInstance2EdgeForwarderMap() {
        forwarderMap = new ConcurrentHashMap<>();
    }

    public void addForwarderAssociation2DigitalTwin(String forwarderName, CDTIdentifier twinIdentifier) {
        LOG.debug(".addForwarder2DigitalTwin(): Entry forwarderName --> {}, twinIdentifier --> {}", forwarderName, twinIdentifier);
        if (forwarderName == null || twinIdentifier == null) {
            LOG.error(".addForwarder2DigitalTwin(): Error, forwarderName or twinIdentifier is null");
        }
        if (!forwarderMap.containsKey(twinIdentifier)) {
            HashSet<String> forwarderList = new HashSet<String>();
            forwarderMap.put(twinIdentifier, forwarderList);
            LOG.trace(".addForwarder2DigitalTwin(): Map Entry for Digital Twin created");
        }
        HashSet<String> forwarderList = forwarderMap.get(twinIdentifier);
        if (!forwarderList.contains(forwarderName)) {
            forwarderList.add(forwarderName);
            LOG.trace(".addForwarder2DigitalTwin(): Forwarder added!");
        }
        LOG.debug(".addForwarder2DigitalTwin(): Exit");
    }

    public void removeForwarderAssociation2DigitalTwin(String forwarderName, CDTIdentifier twinIdentifier) {
        LOG.debug(".removeForwarderAssociation2DigitalTwin(): Entry forwarderName --> {}, twinIdentifier --> {}", forwarderName, twinIdentifier);
        if (forwarderName == null || twinIdentifier == null) {
            LOG.error(".removeForwarderAssociation2DigitalTwin(): Error, forwarderName or twinIdentifier is null");
        }
        if (!forwarderMap.containsKey(twinIdentifier)) {
            LOG.debug(".removeForwarderAssociation2DigitalTwin(): Exit, Digital Twin is not in Map");
            return;
        }
        HashSet<String> forwarderList = forwarderMap.get(twinIdentifier);
        if (forwarderList.contains(forwarderName)) {
            forwarderList.remove(forwarderName);
            LOG.trace(".addForwarder2DigitalTwin(): Forwarder removed!");
        }
        if (forwarderList.isEmpty()) {
            forwarderMap.remove(twinIdentifier);
            LOG.trace(".addForwarder2DigitalTwin(): Digital Twin has been removed from the Map!");
        }
        LOG.debug(".removeForwarderAssociation2DigitalTwin(): Exit");
    }

    public Set<String> getForwarderAssociation2DigitalTwin(CDTIdentifier twinIdentifier) {
        LOG.debug(".getForwarderAssociation2DigitalTwin(): Entry twinIdentifier --> {}", twinIdentifier);
        if (twinIdentifier == null) {
            LOG.error(".getForwarderAssociation2DigitalTwin(): Error, forwarderName or twinIdentifier is null");
        }
        HashSet<String> forwarderSet = new HashSet<String>();
        if (!forwarderMap.containsKey(twinIdentifier)) {
            LOG.debug(".getForwarderAssociation2DigitalTwin(): Exit, Digital Twin is not in Map");
            return (forwarderSet);
        }
        forwarderSet.addAll(forwarderMap.get(twinIdentifier));
        LOG.debug(".getForwarderAssociation2DigitalTwin(): Exit");
        return (forwarderSet);
    }
}
