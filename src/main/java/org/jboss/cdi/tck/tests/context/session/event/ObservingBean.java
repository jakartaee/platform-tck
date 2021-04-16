/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.cdi.tck.tests.context.session.event;

import java.util.concurrent.atomic.AtomicInteger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.BeforeDestroyed;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.event.Observes;
import jakarta.servlet.http.HttpSession;

@ApplicationScoped
public class ObservingBean {

    private final AtomicInteger initializedSessionCount = new AtomicInteger();
    private final AtomicInteger beforedDestroyedSessionCount = new AtomicInteger();
    private final AtomicInteger destroyedSessionCount = new AtomicInteger();

    public void observeSessionInitialized(@Observes @Initialized(SessionScoped.class) HttpSession event) {
        initializedSessionCount.incrementAndGet();
    }

    public void observeSessionBeforeDestroyed(@Observes @BeforeDestroyed(SessionScoped.class) HttpSession event) {
        beforedDestroyedSessionCount.incrementAndGet();
    }

    public void observeSessionDestroyed(@Observes @Destroyed(SessionScoped.class) HttpSession event) {
        destroyedSessionCount.incrementAndGet();
    }

    public AtomicInteger getInitializedSessionCount() {
        return initializedSessionCount;
    }

    public AtomicInteger getBeforedDestroyedSessionCount() {
        return beforedDestroyedSessionCount;
    }

    public AtomicInteger getDestroyedSessionCount() {
        return destroyedSessionCount;
    }
}
