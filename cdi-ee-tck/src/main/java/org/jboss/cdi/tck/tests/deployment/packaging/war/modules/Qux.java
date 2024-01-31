/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.cdi.tck.tests.deployment.packaging.war.modules;

import java.util.concurrent.atomic.AtomicInteger;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

/**
 * @author Martin Kouba
 */
@Dependent
public class Qux {

    private static AtomicInteger eventsObserved = new AtomicInteger(0);

    private Baz baz;

    public Baz getBaz() {
        return baz;
    }

    @Inject
    public void setBaz(Baz baz) {
        this.baz = baz;
    }

    public void observeBusinessOperations(@Observes BusinessOperationEvent event) {
        eventsObserved.incrementAndGet();
    }

    public static int getEventsObserved() {
        return eventsObserved.get();
    }
}
