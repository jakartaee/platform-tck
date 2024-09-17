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
package org.jboss.cdi.tck.tests.extensions.container.event;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeforeShutdown;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessManagedBean;
import jakarta.enterprise.inject.spi.ProcessSessionBean;
import jakarta.enterprise.inject.spi.SessionBeanType;

public class ProcessBeanObserver implements Extension {
    private static AnnotatedType<Farm> processManagedBeanType = null;
    private static AnnotatedType<Object> processStatelessSessionBeanAnnotatedType = null;
    private static String processStatelessSessionBeanName = null;
    private static SessionBeanType processStatelessSessionBeanType = null;
    private static AnnotatedType<Object> processStatefulSessionBeanAnnotatedType = null;
    private static String processStatefulSessionBeanName = null;
    private static SessionBeanType processStatefulSessionBeanType = null;

    public void cleanup(@Observes BeforeShutdown shutdown) {
        processManagedBeanType = null;
        processStatelessSessionBeanAnnotatedType = null;
        processStatelessSessionBeanName = null;
        processStatelessSessionBeanType = null;
        processStatefulSessionBeanAnnotatedType = null;
        processStatefulSessionBeanName = null;
        processStatefulSessionBeanType = null;
    }

    public void observeProcessManagedBean(@Observes ProcessManagedBean<Farm> event) {
        processManagedBeanType = event.getAnnotatedBeanClass();
    }

    public void observeProcessStatelessSessionBean(@Observes ProcessSessionBean<Sheep> event) {
        processStatelessSessionBeanAnnotatedType = event.getAnnotatedBeanClass();
        processStatelessSessionBeanName = event.getEjbName();
        processStatelessSessionBeanType = event.getSessionBeanType();
    }

    public void observeProcessStatefulSessionBean(@Observes ProcessSessionBean<Cow> event) {
        processStatefulSessionBeanAnnotatedType = event.getAnnotatedBeanClass();
        processStatefulSessionBeanName = event.getEjbName();
        processStatefulSessionBeanType = event.getSessionBeanType();
    }

    public static AnnotatedType<Farm> getProcessManagedBeanType() {
        return processManagedBeanType;
    }

    public static AnnotatedType<Object> getProcessStatelessSessionBeanAnnotatedType() {
        return processStatelessSessionBeanAnnotatedType;
    }

    public static String getProcessStatelessSessionBeanName() {
        return processStatelessSessionBeanName;
    }

    public static SessionBeanType getProcessStatelessSessionBeanType() {
        return processStatelessSessionBeanType;
    }

    public static AnnotatedType<Object> getProcessStatefulSessionBeanAnnotatedType() {
        return processStatefulSessionBeanAnnotatedType;
    }

    public static String getProcessStatefulSessionBeanName() {
        return processStatefulSessionBeanName;
    }

    public static SessionBeanType getProcessStatefulSessionBeanType() {
        return processStatefulSessionBeanType;
    }
}
