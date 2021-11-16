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
package org.jboss.cdi.tck.tests.extensions.container.event;

import static jakarta.enterprise.inject.spi.SessionBeanType.STATEFUL;
import static jakarta.enterprise.inject.spi.SessionBeanType.STATELESS;
import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY_STEPS;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY_STEPS_EE;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_BEAN;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_BEAN_EE;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_INJECTION_TARGET;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_INJECTION_TARGET_EE;
import static org.jboss.cdi.tck.cdi.Sections.TYPE_BEAN_DISCOVERY_EE;
import static org.jboss.cdi.tck.cdi.Sections.TYPE_DISCOVERY_STEPS;

import jakarta.ejb.Stateless;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.Annotated;
import jakarta.enterprise.inject.spi.AnnotatedType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for ProcessAnnotatedType, ProcessBean and ProcessInjectionTarget events.
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = JAVAEE_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class ContainerEventTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Deployment
    public static EnterpriseArchive createTestArchive() {
        return new EnterpriseArchiveBuilder()
                .withTestClassPackage(ContainerEventTest.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .withExtensions(ProcessBeanObserver.class, ProcessInjectionTargetObserver.class,
                        ProcessAnnotatedTypeObserver.class).withEjbJarXml("ejb-jar.xml").build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BEAN_DISCOVERY_STEPS, id = "f"), @SpecAssertion(section = PROCESS_INJECTION_TARGET, id = "aaa") })
    public void testProcessInjectionTargetFiredForManagedBean() {
        assert ProcessInjectionTargetObserver.getManagedBeanType() != null;
        validateManagedBean(ProcessInjectionTargetObserver.getManagedBeanType());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "aab"), @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "abb"),
            @SpecAssertion(section = BEAN_DISCOVERY_STEPS_EE, id = "bb") })
    public void testProcessInjectionTargetFiredForSessionBean() {
        assert ProcessInjectionTargetObserver.getStatelessSessionBeanType() != null;
        assert ProcessInjectionTargetObserver.getStatefulSessionBeanType() != null;
        validateStatelessSessionBean(ProcessInjectionTargetObserver.getStatelessSessionBeanType());
        validateStatefulSessionBean(ProcessInjectionTargetObserver.getStatefulSessionBeanType());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "aaf"), @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "abf"),
            @SpecAssertion(section = BEAN_DISCOVERY_STEPS_EE, id = "bh") })
    public void testProcessInjectionTargetFiredForSessionBeanInterceptor() {
        assert ProcessInjectionTargetObserver.getSessionBeanInterceptorType() != null;
        validateSessionBeanInterceptor(ProcessInjectionTargetObserver.getSessionBeanInterceptorType());
    }

    @Test
    @SpecAssertion(section = TYPE_DISCOVERY_STEPS, id = "c")
    public void testProcessAnnotatedTypeFiredForManagedBean() {
        assert ProcessAnnotatedTypeObserver.getManagedBeanType() != null;
        validateManagedBean(ProcessAnnotatedTypeObserver.getManagedBeanType());
    }

    @Test
    @SpecAssertion(section = TYPE_BEAN_DISCOVERY_EE, id = "bb")
    public void testProcessAnnotatedTypeFiredForSessionBean() {
        assert ProcessAnnotatedTypeObserver.getStatelessSessionBeanType() != null;
        assert ProcessAnnotatedTypeObserver.getStatefulSessionBeanType() != null;
        validateStatelessSessionBean(ProcessAnnotatedTypeObserver.getStatelessSessionBeanType());
        validateStatefulSessionBean(ProcessAnnotatedTypeObserver.getStatefulSessionBeanType());
    }

    @Test
    @SpecAssertion(section = TYPE_BEAN_DISCOVERY_EE, id = "bh")
    public void testProcessAnnotatedTypeFiredForSessionBeanInterceptor() {
        assert ProcessAnnotatedTypeObserver.getSessionBeanInterceptorType() != null;
        validateSessionBeanInterceptor(ProcessAnnotatedTypeObserver.getSessionBeanInterceptorType());
    }

    @Test
    @SpecAssertion(section = PROCESS_BEAN, id = "ba")
    public void testProcessManagedBeanFired() {
        assert ProcessBeanObserver.getProcessManagedBeanType() != null;
        validateManagedBean(ProcessBeanObserver.getProcessManagedBeanType());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN_EE, id = "ca"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS_EE, id = "fb") })
    public void testProcessSessionBeanFiredForStatelessSessionBean() {
        assert ProcessBeanObserver.getProcessStatelessSessionBeanAnnotatedType() != null;
        validateStatelessSessionBean(ProcessBeanObserver.getProcessStatelessSessionBeanAnnotatedType());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN_EE, id = "ca"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS_EE, id = "fb") })
    public void testProcessSessionBeanFiredForStatefulSessionBean() {
        assert ProcessBeanObserver.getProcessStatefulSessionBeanAnnotatedType() != null;
        validateStatefulSessionBean(ProcessBeanObserver.getProcessStatefulSessionBeanAnnotatedType());
    }

    @Test
    @SpecAssertion(section = PROCESS_BEAN_EE, id = "hb")
    public void testGetEJBName() {
        assert ProcessBeanObserver.getProcessStatelessSessionBeanName().equals("sheep");
        assert ProcessBeanObserver.getProcessStatefulSessionBeanName().equals("cow");
    }

    @Test
    @SpecAssertion(section = PROCESS_BEAN_EE, id = "hc")
    public void testGetSessionBeanType() {
        assert ProcessBeanObserver.getProcessStatelessSessionBeanType().equals(STATELESS);
        assert ProcessBeanObserver.getProcessStatefulSessionBeanType().equals(STATEFUL);
    }

    private void validateStatelessSessionBean(Annotated type) {
        assert type.getBaseType().equals(Sheep.class);
        assert typeSetMatches(type.getTypeClosure(), Sheep.class, SheepLocal.class, Object.class);
        assert type.getAnnotations().size() == 2;
        assert annotationSetMatches(type.getAnnotations(), Tame.class, Stateless.class);
    }

    private void validateStatefulSessionBean(Annotated type) {
        assert type.getBaseType().equals(Cow.class);
        assert typeSetMatches(type.getTypeClosure(), Cow.class, CowLocal.class, Object.class);
        assert type.getAnnotations().size() == 0;
    }

    private void validateSessionBeanInterceptor(AnnotatedType<SheepInterceptor> type) {
        assert type.getBaseType().equals(SheepInterceptor.class);
        assert typeSetMatches(type.getTypeClosure(), SheepInterceptor.class, Object.class);
        assert type.getAnnotations().size() == 0;
        assert type.getFields().size() == 0;
        assert type.getMethods().size() == 1;
    }

    private void validateManagedBean(AnnotatedType<Farm> type) {
        assert type.getBaseType().equals(Farm.class);
        assert typeSetMatches(type.getTypeClosure(), Farm.class, Object.class);
        assert type.getFields().size() == 1;
        assert type.getFields().iterator().next().isAnnotationPresent(Produces.class);
        assert type.getMethods().size() == 1;
        assert type.getMethods().iterator().next().isAnnotationPresent(Produces.class);
    }

}
