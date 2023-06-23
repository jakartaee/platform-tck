/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processBeanAttributes.builtin;

import jakarta.enterprise.context.Conversation;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessBeanAttributes;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

public class ProcessBeanAttributesObserver implements Extension {

    private List<ProcessBeanAttributes<?>> observedBeanAttributes = new ArrayList<ProcessBeanAttributes<?>>();

    public void observeHttpServletRequestBeanAttributes(@Observes ProcessBeanAttributes<HttpServletRequest> event) {
        if (event.getBeanAttributes().getQualifiers().contains(Default.Literal.INSTANCE)) {
            observedBeanAttributes.add(event);
        }
    }

    @SuppressWarnings("rawtypes")
    public void observeEventBeanAttributes(@Observes ProcessBeanAttributes<Event> event) {
        if (event.getBeanAttributes().getQualifiers().contains(Default.Literal.INSTANCE)) {
            observedBeanAttributes.add(event);
        }
    }

    @SuppressWarnings("rawtypes")
    public void observeInstanceBeanAttributes(@Observes ProcessBeanAttributes<Instance> event) {
        if (event.getBeanAttributes().getQualifiers().contains(Default.Literal.INSTANCE)) {
            observedBeanAttributes.add(event);
        }
    }

    public void observeConversationBeanAttributes(@Observes ProcessBeanAttributes<Conversation> event) {
        if (event.getBeanAttributes().getQualifiers().contains(Default.Literal.INSTANCE)) {
            observedBeanAttributes.add(event);
        }
    }

    public void observeBeanManagerBeanAttributes(@Observes ProcessBeanAttributes<BeanManager> event) {
        if (event.getBeanAttributes().getQualifiers().contains(Default.Literal.INSTANCE)) {
            observedBeanAttributes.add(event);
        }
    }

    public List<ProcessBeanAttributes<?>> getObservedBeanAttributes() {
        return observedBeanAttributes;
    }

}