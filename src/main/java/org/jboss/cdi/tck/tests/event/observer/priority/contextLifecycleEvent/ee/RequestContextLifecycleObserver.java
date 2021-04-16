/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.event.observer.priority.contextLifecycleEvent.ee;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.interceptor.Interceptor;
import jakarta.servlet.ServletRequest;

@ApplicationScoped
public class RequestContextLifecycleObserver {

    private List<String> initializedEvents = new CopyOnWriteArrayList<>();
    private List<String> destroyedEvents = new CopyOnWriteArrayList<>();

    public void firstInit(@Observes @Initialized(RequestScoped.class) @Priority(Interceptor.Priority.APPLICATION - 100) ServletRequest servletRequest) {
        initializedEvents.add("A");
    }

    public void secondInit(@Observes @Initialized(RequestScoped.class) ServletRequest servletRequest) {
        initializedEvents.add("B");
        ;
    }

    public void thirdInit(@Observes @Initialized(RequestScoped.class) @Priority(Interceptor.Priority.APPLICATION + 501) ServletRequest servletRequest) {
        initializedEvents.add("C");
    }

    public void firstDestroy(@Observes @Destroyed(RequestScoped.class) @Priority(Interceptor.Priority.APPLICATION) ServletRequest servletRequest) {
        destroyedEvents.add("A");
    }

    public void secondDestroy(@Observes @Destroyed(RequestScoped.class) ServletRequest servletRequest) {
        destroyedEvents.add("B");
    }

    public void thirdDestroy(@Observes @Destroyed(RequestScoped.class) @Priority(Interceptor.Priority.APPLICATION + 501) ServletRequest servletRequest) {
        destroyedEvents.add("C");
    }

    public List<String> getDestroyedEvents() {
        return destroyedEvents;
    }

    public List<String> getInitializedEvents() {
        return initializedEvents;
    }
}
