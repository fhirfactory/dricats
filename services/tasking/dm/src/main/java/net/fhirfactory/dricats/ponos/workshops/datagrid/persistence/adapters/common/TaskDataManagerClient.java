/*
 * Copyright (c) 2020 Mark A. Hunter (ACT Health)
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
package net.fhirfactory.dricats.ponos.workshops.datagrid.persistence.adapters.common;

import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.gclient.ICriterion;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.util.BundleUtil;
import net.fhirfactory.dricats.internals.fhir.r4.resources.task.factories.TaskStatusReasonFactory;
import net.fhirfactory.dricats.ponos.common.PonosNames;
import net.fhirfactory.dricats.ponos.workshops.datagrid.persistence.adapters.common.base.ResourceDataManagerClient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public abstract class TaskDataManagerClient extends ResourceDataManagerClient {
    private static final Logger LOG = LoggerFactory.getLogger(TaskDataManagerClient.class);

    @Inject
    private TaskStatusReasonFactory businessStatusFactory;

    @Inject
    private PonosNames ponosNames;

    //
    // Constructor(s)
    //

    public TaskDataManagerClient() {
        super();
    }

    //
    // Getters (and Setters)
    //

    protected TaskStatusReasonFactory getBusinessStatusFactory() {
        return (businessStatusFactory);
    }

    //
    // Business Methods
    //

    public MethodOutcome createTask(String taskJSONString) {
        getLogger().debug(".postTask(): Entry, taskJSONString->{}", taskJSONString);
        Task task = getFHIRParser().parseResource(Task.class, taskJSONString);
        MethodOutcome outcome = createResource(task);
        getLogger().debug(".postTask(): Exit, outcome->{}", outcome);
        return (outcome);
    }

    public MethodOutcome createTask(Task task) {
        getLogger().debug(".postTask(): Entry, task->{}", task);
        MethodOutcome outcome = createResource(task);
        getLogger().debug(".postTask(): Exit, outcome->{}", outcome);
        return (outcome);
    }

    public MethodOutcome updateTask(Task task) {
        getLogger().debug(".updateTask(): Entry, task->{}", task);
        MethodOutcome outcome = updateResource(task);
        getLogger().debug(".updateTask(): Exit, outcome->{}", outcome);
        return (outcome);
    }

    //
    // Searches
    //

    public Bundle getWaitingTasks(Integer count, Integer offSet) {
        getLogger().debug(".getWaitingTasks(): count->{}, offSet->{}", count, offSet);
        TokenClientParam waitingTaskParam = new TokenClientParam("status");
        ICriterion<TokenClientParam> statusIsOnHold = waitingTaskParam.exactly().code(Task.TaskStatus.ONHOLD.toCode());
        LOG.trace(".getWaitingTasks(): Search statusIsOnHold->{}", statusIsOnHold);
        Bundle response = getClient()
                .search()
                .forResource(Task.class)
                .where(statusIsOnHold)
                .count(count)
                .offset(offSet)
                .returnBundle(Bundle.class)
                .prettyPrint()
                .execute();
        getLogger().debug(".getWaitingTasks(): Exit, response->{}", response);
        return (response);
    }

    public Bundle getTaskByStatus(CodeableConcept taskPerformer, List<Task.TaskStatus> taskStatusSet, Integer count, Integer offSet) {
        getLogger().debug(".getTaskByStatus(): count->{}, offSet->{}", count, offSet);

        List<String> statusCodeList = new ArrayList<>();
        if (taskStatusSet != null && !taskStatusSet.isEmpty()) {
            for (Task.TaskStatus currentStatus : taskStatusSet) {
                statusCodeList.add(currentStatus.toCode());
            }
        }

        Bundle response = getClient()
                .search()
                .forResource(Task.class)
                .where(Task.PERFORMER.exactly().code(taskPerformer.getCodingFirstRep().getCode()))
                .and(Task.STATUS.exactly().codes(statusCodeList))
                .count(count)
                .offset(offSet)
                .returnBundle(Bundle.class)
                .prettyPrint()
                .execute();
        getLogger().debug(".getTaskByStatus(): Exit, response->{}", response);
        return (response);
    }


    public Resource findResourceByIdentifier(Identifier identifier) {
        getLogger().debug(".findResourceByIdentifier(): Entry, identifier->{}", identifier);

        Bundle response = getClient()
                .search()
                .forResource(Task.class)
                .where(Task.IDENTIFIER.exactly().systemAndValues(identifier.getSystem(), identifier.getValue()))
                .returnBundle(Bundle.class)
                .execute();

        List<IBaseResource> tasks = new ArrayList<>(BundleUtil.toListOfResources(getFHIRContextUtility().getFhirContext(), response));

        IParser r4Parser = getFHIRParser().setPrettyPrint(true);
        if (getLogger().isWarnEnabled()) {
            if (response != null) {
                getLogger().warn(".findResourceByIdentifier(): Retrieved Bundle --> {}", r4Parser.encodeResourceToString(response));
            }
        }

        Task resource = null;
        if (tasks.size() > 0) {
            resource = (Task) tasks.get(0);
        }
        getLogger().debug(".findResourceByIdentifier(): Exit, Resource->{}", resource);
        return (resource);
    }
}
