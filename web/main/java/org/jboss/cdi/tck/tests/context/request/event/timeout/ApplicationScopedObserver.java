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
package org.jboss.cdi.tck.tests.context.request.event.timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class ApplicationScopedObserver {

    @Inject
    private RequestScopedObserver observer;

    private final CountDownLatch latch = new CountDownLatch(1);
    private final AtomicBoolean destroyedCalled = new AtomicBoolean();

    void observeRequestDestroyed(@Observes @Destroyed(RequestScoped.class) Object event) {
        destroyedCalled.set(true);
    }

    void reset() {
        destroyedCalled.set(false);
    }

    boolean isDestroyedCalled() {
        return destroyedCalled.get();
    }

    public void countDown() {
        if (observer.isInitializedObserved()) {
            latch.countDown();
        }
    }

    public Boolean await(long timeout, TimeUnit timeUnit) {
        Boolean result = new Boolean(false);
        try {
            result = latch.await(timeout, timeUnit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
