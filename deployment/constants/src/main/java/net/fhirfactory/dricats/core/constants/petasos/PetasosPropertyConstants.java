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
package net.fhirfactory.dricats.core.constants.petasos;

/**
 * @author Mark A. Hunter
 */

public final class PetasosPropertyConstants {

    public static final Long TASK_DATA_GRID_TASK_QUEUEING_RETRY_DELAY = 2500L;
    public static final Integer TASK_DATA_GRID_TASK_QUEUEING_MAX_RETRY = 10;


    public static final Long PONOS_TASK_FORWARDER_RETRY_DELAY = 60000L;
    public static final Long PONOS_TASK_FORWARDER_DAEMON_STARTUP_DELAY = 60000L;
    public static final Long PONOS_TASK_FORWARDER_DAEMON_PERIOD = 10000L;
    public static final Long PONOS_TASK_FORWARDER_DAEMON_RESET_PERIOD = 300000L;

    // Each participant within any Petasos enabled module has a Task Queue
    // managed by the TaskQueueManager. This value dictates the default "maximum"
    // size of the queue before it will shunt (offload) the tasks to Ponos.
    public static final Integer PARTICIPANT_QUEUE_SIZE = 100;
    // This value is the size of the queue that the TaskQueueManager will "shrink to"
    // in the event that the queue exceeds its maximum size.
    public static final Integer PARTICIPANT_QUEUE_OFFLOAD_THRESHOLD = 60;
    // This value is the size of the queue that the TaskQueueManager will "shrink to"
    // before it begins to "onload" tasks back into the local queue.
    public static final Integer PARTICIPANT_QUEUE_ONLOAD_THRESHOLD = 20;
    // This value is the nominal size of a batch of tasks "offloaded" or "onloaded"
    // by the local TaskQueueManager.
    public static final Integer PARTICIPANT_QUEUE_TASK_BATCH_SIZE = 20;

    // For assisting in monitoring expected end time in parcels, this period in milliseconds
    // can be used when monitoring a UoW. For example it can be added to the expected completion
    // time to provide an extra buffer to avoid classing a UoW as failed too early
    public final static long EXPECTED_COMPLETION_TIME_BUFFER_MILLIS = 200;

    // How often to send a heartbeat between Nodes
    public final static long HEARTBEAT_FREQUENCY_MILLIS = 100;

    // Every N heartbeats, include a status update with the heartbeat message
    public final static long HEARTBEAT_STATUS_UPDATE_FREQUENCY = 10;

    // If a heartbeat fails, try to reconnect N times before failing and marking
    // the Node as unavailable
    public final static long HEARTBEAT_NUM_RETRIES = 3;

    // If the cache is full, this is the location where the overflow is persisted
    public final static String CACHE_OVERFLOW_DIRECTORY = "/tmp";

    // Size of the cache in bytes, once exceeded entries will be written to the overflow
    // directory
    public final static long CACHE_SIZE_IN_BYTES = 1000000000;

    // The nominal period (in seconds) any single processing plant should wait before assuming
    // that a particular cache item in an infinity span cache is synchronised.
    // This is a very pessimistic number...
    public final static long PETASOS_DISTRIBUTED_CACHE_SYNCHRONISATION_WAIT = 5;

    // How long should completed content be kept within the caches?
    public final static long CACHE_ENTRY_RETENTION_PERIOD_SECONDS = 60;

    // How long should a WUP take to complete a task - worst case?
    public final static long WUP_ACTIVITY_DURATION_SECONDS = 10;

    // How long should a WUP sleep between scans for activity?
    public final static long WUP_SLEEP_INTERVAL_MILLISECONDS = 2000;

    // General Workflow Exchange Objects
    public final static String WUP_TOPOLOGY_NODE_EXCHANGE_PROPERTY_NAME = "WUPTopologyNode";
    public final static String WUP_METRICS_AGENT_EXCHANGE_PROPERTY = "WUPMetricsAgent";
    public final static String WUP_PETASOS_FULFILLMENT_TASK_EXCHANGE_PROPERTY = "WUPPetasosParcel";

    @Deprecated
    public final static String WUP_JOB_CARD_EXCHANGE_PROPERTY_NAME = "WUPJobCard";
    @Deprecated
    public final static String WUP_CURRENT_UOW_EXCHANGE_PROPERTY_NAME = "WUPCurrentUnitOfWork";


    public final static String ENDPOINT_TOPOLOGY_NODE_EXCHANGE_PROPERTY = "EndpointTopologyNode";
    public final static String ENDPOINT_METRICS_AGENT_EXCHANGE_PROPERTY = "EndpointMetricsAgent";
    public final static String ENDPOINT_TASK_REPORT_AGENT_EXCHANGE_PROPERTY = "EndpointTaskReportAgent";
    public final static String ENDPOINT_PORT_VALUE = "EndpointPortValue";
    public final static String ENDPOINT_HOSTNAME = "EndpointPortName";

    public final static String WUP_INTERACT_INGRES_SOURCE_SYSTEM_NAME = "WUPInteractIngresSourceSystemName";
    public final static String WUP_INTERACT_EGRESS_SOURCE_SYSTEM_NAME = "WUPInteractEgressSourceSystemName";

    public final static String WUP_INTERACT_PORT_TYPE = "WUPInteractIngresPortType";

    public final static String DEFAULT_TIMEZONE = "Australia/Sydney";

    //
    // MLLP Metrics Capture Parameters
    //

    public static final String INCLUDE_FULL_HL7_MESSAGE_IN_LOG = "INCLUDE_FULL_HL7_MESSAGE_IN_LOG";
    public static final String MAXIMUM_HL7_MESSAGE_SIZE_IN_LOG = "MAXIMUM_HL7_MESSAGE_SIZE_IN_LOG";

    //
    // HL7v2 ZDE Inclusion Flag
    //

    public static final String FORWARD_ZDE_SEGMENT = "FORWARD_ZDE_SEGMENT";

    //
    // Task Distribution and Outcome Collection Queues
    //

    public final static String TASK_DISTRIBUTION_QUEUE = "seda:task_distribution_queue";
    public final static String TASK_OUTCOME_COLLECTION_QUEUE = "seda:task_outcome_collection_queue";

    //
    // Inter-ProcessingPlant Task Forwarder Queue
    //

    public final static String TASK_IPC_FORWARDER_NAME = "PetasosTaskForwarderWUP";
    public final static String TASK_IPC_RECEIVER_NAME = "PetasosTaskReceiverWUP";

    public final static String OUTBOUND_CHECKPOINT_WUP_NAME = "OutboundMessageCheckpoint";
    public final static String INBOUND_CHECKPOINT_WUP_NAME = "InboundMessageCheckpoint";

    //
    // Processing Plant
    //

    public final static String AUDIT_LEVEL_PARAMETER_NAME = "AUDIT_LEVEL";
    public final static String SEDA_QUEUE_SIZE_PARAMETER_NAME = "SEDA_QUEUE_DEFAULT_SIZE";
    public final static String SEDA_QUEUE_BLOCK_ON_FULL_PARAMETER_NAME = "SEDA_QUEUE_BLOCK_ON_FULL";


    //
    // EndPoint Configurations
    //

    //
    // MLLP

    public final static String CAMEL_MLLP_CONNECTION_TIMEOUT_PARAMETER_NAME = "CAMEL_MLLP_CONNECTION_TIMEOUT";
    public final static Integer DEFAULT_CAMEL_MLLP_CONNECTION_TIMEOUT = 30;
    public final static String CAMEL_MLLP_ACCEPT_TIMEOUT_PARAMETER_NAME = "CAMEL_MLLP_ACCEPT_TIMEOUT";
    public final static String CAMEL_MLLP_BIND_TIMEOUT_PARAMETER_NAME = "CAMEL_MLLP_BIND_TIMEOUT";
    public final static String CAMEL_MLLP_MAXIMUM_CONSUMERS_PARAMETER_NAME = "CAMEL_MLLP_MAX_CONCURRENT_SESSIONS";
    public final static String CAMEL_MLLP_KEEP_ALIVE_PARAMETER_NAME = "CAMEL_MLLP_KEEPALIVE";
    public final static String DRICATS_HL7_MESSAGE_DETAIL_PARAMETER_NAME = "DRICATS_HL7_MESSAGE_DETAIL";

    public final static String CAMEL_MLLP_VALIDATE_PAYLOAD_PARAMETER_NAME = "CAMEL_MLLP_PAYLOAD_VALIDATION";
    public final static String HL7_TRIGGER_EVENT_MINIMAL_CONFORMANCE_ENFORCEMENT_PARAMETER_NAME = "TRIGGER_EVENT_MINIMAL_CONFORMANCE_ENFORCEMENT";
    public final static String CAMEL_MLLP_STRING_PAYLOAD_PARAMETER_NAME = "CAMEL_MLLP_STRING_PAYLOAD";

}
