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
package org.jboss.cdi.tck.tests.lookup.injection.non.contextual;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeforeShutdown;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;

public class ProcessAnnotatedTypeObserver implements Extension {
    private static AnnotatedType<TestServlet> servletEvent = null;
    private static AnnotatedType<TestListener> listenerEvent = null;
    private static AnnotatedType<TestFilter> filterEvent = null;
    private static AnnotatedType<TagLibraryListener> tagLibraryListenerEvent = null;
    private static AnnotatedType<TestTagHandler> tagHandlerEvent = null;
    private static AnnotatedType<Farm> jsfManagedBeanEvent = null;
    private static AnnotatedType<SessionBean> sessionBeanEvent = null;

    public void cleanup(@Observes BeforeShutdown shutdown) {
        servletEvent = null;
        listenerEvent = null;
        filterEvent = null;
        tagLibraryListenerEvent = null;
        tagHandlerEvent = null;
        jsfManagedBeanEvent = null;
        sessionBeanEvent = null;
    }

    public void observeServlet(@Observes ProcessAnnotatedType<TestServlet> event) {
        servletEvent = event.getAnnotatedType();
    }

    public void observeSessionBean(@Observes ProcessAnnotatedType<SessionBean> event) {
        sessionBeanEvent = event.getAnnotatedType();
    }

    public void observeFilter(@Observes ProcessAnnotatedType<TestFilter> event) {
        filterEvent = event.getAnnotatedType();
    }

    public void observeListener(@Observes ProcessAnnotatedType<TestListener> event) {
        listenerEvent = event.getAnnotatedType();
    }

    public void observeTagHandler(@Observes ProcessAnnotatedType<TestTagHandler> event) {
        tagHandlerEvent = event.getAnnotatedType();
    }

    public void observeTagLibraryListener(@Observes ProcessAnnotatedType<TagLibraryListener> event) {
        tagLibraryListenerEvent = event.getAnnotatedType();
    }

    public void observeJsfManagedBean(@Observes ProcessAnnotatedType<Farm> event) {
        jsfManagedBeanEvent = event.getAnnotatedType();
    }

    public static AnnotatedType<TestServlet> getServletEvent() {
        return servletEvent;
    }

    public static AnnotatedType<TestListener> getListenerEvent() {
        return listenerEvent;
    }

    public static AnnotatedType<TestFilter> getFilterEvent() {
        return filterEvent;
    }

    public static AnnotatedType<TagLibraryListener> getTagLibraryListenerEvent() {
        return tagLibraryListenerEvent;
    }

    public static AnnotatedType<TestTagHandler> getTagHandlerEvent() {
        return tagHandlerEvent;
    }

    public static AnnotatedType<Farm> getJsfManagedBeanEvent() {
        return jsfManagedBeanEvent;
    }

    public static AnnotatedType<SessionBean> getSessionBeanEvent() {
        return sessionBeanEvent;
    }

}
