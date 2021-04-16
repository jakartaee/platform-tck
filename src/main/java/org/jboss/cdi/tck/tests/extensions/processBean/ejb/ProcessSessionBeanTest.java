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

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY_STEPS_EE;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_BEAN_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.ProcessBeanAttributes;
import jakarta.enterprise.inject.spi.ProcessSessionBean;
import jakarta.enterprise.inject.spi.SessionBeanType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class ProcessSessionBeanTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(ProcessSessionBeanTest.class)
                .withClasses(Elephant.class, ElephantLocal.class, ProcessSessionBeanObserver.class)
                .withExtension(ProcessSessionBeanObserver.class).build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN_EE, id = "ca"), @SpecAssertion(section = PROCESS_BEAN_EE, id = "cb"),
            @SpecAssertion(section = PROCESS_BEAN_EE, id = "edb"), @SpecAssertion(section = PROCESS_BEAN_EE, id = "efb"),
            @SpecAssertion(section = PROCESS_BEAN_EE, id = "fb"), @SpecAssertion(section = PROCESS_BEAN_EE, id = "hb"),
            @SpecAssertion(section = PROCESS_BEAN_EE, id = "hc"), @SpecAssertion(section = PROCESS_BEAN_EE, id = "m"),
            @SpecAssertion(section = PROCESS_BEAN_EE, id = "k"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS_EE, id = "fb") })
    public void testProcessSessionBeanEvent() {

        assertEquals(ProcessSessionBeanObserver.getElephantBean().getBeanClass(), Elephant.class);
        assertEquals(ProcessSessionBeanObserver.getElephantProcessBeanCount(), 0);
        assertEquals(ProcessSessionBeanObserver.getElephantName(), "Rosie");
        assertEquals(ProcessSessionBeanObserver.getElephantType(), SessionBeanType.STATELESS);
        assertTrue(ProcessSessionBeanObserver.getElephantAnnotated() instanceof AnnotatedType<?>);
        assertEquals(ProcessSessionBeanObserver.getElephantAnnotatedType().getBaseType(), Elephant.class);

        assertEquals(ProcessSessionBeanObserver.getElephantActionSeq().getData(),
                Arrays.asList(ProcessBeanAttributes.class.getName(), ProcessSessionBean.class.getName()));
    }

}
