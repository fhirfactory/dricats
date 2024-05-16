/*
 * Copyright (c) 2022 Mark A. Hunter
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
package net.fhirfactory.dricats.internals.fhir.r4.resources.device.factories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.dricats.core.constants.systemwide.PegacornReferenceProperties;
import net.fhirfactory.dricats.core.model.task.datatypes.work.datatypes.TaskWorkItemSubscriptionType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class DeviceSubscriptionFactory {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceSubscriptionFactory.class);

    private static final String DRICATS_DEVICE_CONFIGURATION_SUBSCRIPTION_TOPIC = "/device-configuration-subscription-topic";

    private ObjectMapper jsonMapper;

    @Inject
    private PegacornReferenceProperties systemWideProperties;

    //
    // Constructor(s)
    //

    public DeviceSubscriptionFactory() {
        jsonMapper = new ObjectMapper();
        JavaTimeModule module = new JavaTimeModule();
        this.jsonMapper.registerModule(module);
        this.jsonMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    //
    // Getters (and Setters)
    //

    protected Logger getLogger() {
        return (LOG);
    }

    public String getDeviceExtensionSubscriptionSystemURL() {
        String codeSystem = systemWideProperties.getPegacornCodeSystemSite() + DRICATS_DEVICE_CONFIGURATION_SUBSCRIPTION_TOPIC;
        return (codeSystem);
    }

    //
    // Business Methods
    //

    public List<Extension> mapSubscriptionToExtensionList(Set<TaskWorkItemSubscriptionType> subscriptions) {
        getLogger().debug(".mapSubscriptionToPropertyComponent(): Entry->{}", subscriptions);
        if (subscriptions == null) {
            return (null);
        }
        if (subscriptions.isEmpty()) {
            return (null);
        }
        try {
            List<Extension> extensionList = new ArrayList<>();

            for (TaskWorkItemSubscriptionType currentSubscription : subscriptions) {
                Extension currentSubscriptionExtension = new Extension();
                currentSubscriptionExtension.setUrl(getDeviceExtensionSubscriptionSystemURL());
                String subscriptionAsString = jsonMapper.writeValueAsString(currentSubscription);
                getLogger().warn(".mapSubscriptionToPropertyComponent(): subscriptionAsString->{}", subscriptionAsString);
                StringType subscriptionAsStringValueType = new StringType(subscriptionAsString);
                currentSubscriptionExtension.setValue(subscriptionAsStringValueType);
                extensionList.add(currentSubscriptionExtension);
            }

            return (extensionList);

        } catch (Exception ex) {
            getLogger().warn(".mapSubscriptionToPropertyComponent(): Failed to encode subscription, problem->", ex);
        }
        return (null);
    }

    public Set<TaskWorkItemSubscriptionType> mapSubscriptionExtensionsToTaskWorkItemSubscriptions(List<Extension> deviceExtensionList) {
        if (deviceExtensionList == null || deviceExtensionList.isEmpty()) {
            return (new HashSet<>());
        }
        Set<TaskWorkItemSubscriptionType> subscriptionSet = new HashSet<>();
        for (Extension currentExtension : deviceExtensionList) {
            if (currentExtension.getUrl().contentEquals(getDeviceExtensionSubscriptionSystemURL())) {
                try {
                    StringType subscriptionAsStringType = (StringType) currentExtension.getValue();
                    String subscriptionAsString = subscriptionAsStringType.getValue();
                    TaskWorkItemSubscriptionType workItemSubscription = jsonMapper.readValue(subscriptionAsString, TaskWorkItemSubscriptionType.class);
                    if (workItemSubscription != null) {
                        subscriptionSet.add(workItemSubscription);
                    }
                } catch (Exception ex) {
                    getLogger().warn(".mapSubscriptionExtensionsToTaskWorkItemSubscriptions(): problem ->", ex);
                }
            }
        }
        return (subscriptionSet);
    }
}
