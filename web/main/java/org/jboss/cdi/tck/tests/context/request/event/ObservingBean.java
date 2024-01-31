/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.context.request.event;

import java.util.concurrent.atomic.AtomicInteger;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.BeforeDestroyed;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.servlet.ServletRequest;

@ApplicationScoped
public class ObservingBean {

    private final AtomicInteger initializedRequestCount = new AtomicInteger();
    private final AtomicInteger beforeDestroyedRequestCount = new AtomicInteger();
    private final AtomicInteger destroyedRequestCount = new AtomicInteger();

    public void observeRequestInitialized(@Observes @Initialized(RequestScoped.class) ServletRequest event) {
        if (!"bar".equals(event.getParameter("foo"))) {
            throw new IllegalArgumentException("Unknown request, parameter foo not set.");
        }
        initializedRequestCount.incrementAndGet();
    }

    public void observeBeforeDestroyed(@Observes @BeforeDestroyed(RequestScoped.class) ServletRequest event){
         beforeDestroyedRequestCount.incrementAndGet();
    }

    public void observeRequestDestroyed(@Observes @Destroyed(RequestScoped.class) ServletRequest event) {
        destroyedRequestCount.incrementAndGet();
    }

    public AtomicInteger getInitializedRequestCount() {
        return initializedRequestCount;
    }

    public AtomicInteger getDestroyedRequestCount() {
        return destroyedRequestCount;
    }

    public AtomicInteger getBeforeDestroyedRequestCount() {
        return beforeDestroyedRequestCount;
    }
}
