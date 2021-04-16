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
package org.jboss.cdi.tck.tests.extensions.processBean.ejb;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Annotated;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessBean;
import jakarta.enterprise.inject.spi.ProcessBeanAttributes;
import jakarta.enterprise.inject.spi.ProcessSessionBean;
import jakarta.enterprise.inject.spi.SessionBeanType;

import org.jboss.cdi.tck.util.ActionSequence;

public class ProcessSessionBeanObserver implements Extension {

    private static Bean<Object> elephantBean;
    private static AnnotatedType<Object> elephantAnnotatedType;
    private static Annotated elephantAnnotated;
    private static String elephantName;
    private static SessionBeanType elephantType;

    private static int elephantProcessBeanCount;

    private static ActionSequence elephantActionSeq = new ActionSequence();

    public void observeElephantSessionBean(@Observes ProcessSessionBean<Elephant> event) {
        elephantBean = event.getBean();
        elephantAnnotatedType = event.getAnnotatedBeanClass();
        elephantAnnotated = event.getAnnotated();
        elephantName = event.getEjbName();
        elephantType = event.getSessionBeanType();
        elephantActionSeq.add(ProcessSessionBean.class.getName());
    }

    public void observeElephantBean(@Observes ProcessBean<Elephant> event) {
        ProcessSessionBeanObserver.elephantProcessBeanCount++;
    }

    public void observeElephantBeanAttributes(@Observes ProcessBeanAttributes<Elephant> event) {
        elephantActionSeq.add(ProcessBeanAttributes.class.getName());
    }

    public static int getElephantProcessBeanCount() {
        return elephantProcessBeanCount;
    }

    public static ActionSequence getElephantActionSeq() {
        return elephantActionSeq;
    }

    public static Bean<Object> getElephantBean() {
        return elephantBean;
    }

    public static AnnotatedType<Object> getElephantAnnotatedType() {
        return elephantAnnotatedType;
    }

    public static Annotated getElephantAnnotated() {
        return elephantAnnotated;
    }

    public static String getElephantName() {
        return elephantName;
    }

    public static SessionBeanType getElephantType() {
        return elephantType;
    }

}
