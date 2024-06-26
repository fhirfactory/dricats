package net.fhirfactory.dricats.core.tasks.management.daemon;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

public abstract class DaemonBase extends RouteBuilder {

    //
    // Constructor(s)
    //

    public DaemonBase() {
        super();
    }

    //
    // Class Kickstarter
    //

    @Override
    public void configure() throws Exception {
        String name = getClass().getSimpleName();

        from("timer://" + name + "?delay=1000&repeatCount=1")
                .routeId(getClass().getName())
                .log(LoggingLevel.DEBUG, "Starting....");
    }
}
