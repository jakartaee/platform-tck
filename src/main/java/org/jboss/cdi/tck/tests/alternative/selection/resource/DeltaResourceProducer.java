package org.jboss.cdi.tck.tests.alternative.selection.resource;

import jakarta.annotation.Priority;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;

@Dependent
public class DeltaResourceProducer {

    @Produces
    @ProductionReady
    @Resource(name = "test1")
    @Alternative
    @Priority(1)
    String test1;
}
