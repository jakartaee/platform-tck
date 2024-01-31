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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processInjectionTarget;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY_STEPS_EE;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_INJECTION_TARGET_EE;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * This test verifies that ProcessInjectionTarget event is fired for various Java EE components.
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class ContainerEventTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ContainerEventTest.class)
                .withExtension(ProcessInjectionTargetObserver.class)
                .withWebResource("faces-config.xml", "/WEB-INF/faces-config.xml")
                .withWebResource("TestLibrary.tld", "WEB-INF/TestLibrary.tld").build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "aac"), @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "abc"),
            @SpecAssertion(section = BEAN_DISCOVERY_STEPS_EE, id = "bd") })
    public void testProcessInjectionTargetEventFiredForJsfManagedBean() {
        assertNotNull(ProcessInjectionTargetObserver.getJsfManagedBeanEvent());
    }
    
    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "aac"), @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "abc"),
            @SpecAssertion(section = BEAN_DISCOVERY_STEPS_EE, id = "be") })
    public void testProcessInjectionTargetEventFiredForServletListener() {
        assertNotNull(ProcessInjectionTargetObserver.getListenerEvent());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "aad"), @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "abd"),
            @SpecAssertion(section = BEAN_DISCOVERY_STEPS_EE, id = "bf") })
    public void testProcessInjectionTargetEventFiredForTagHandler() {
        assertNotNull(ProcessInjectionTargetObserver.getTagHandlerEvent());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "aae"), @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "abe"),
            @SpecAssertion(section = BEAN_DISCOVERY_STEPS_EE, id = "bg") })
    public void testProcessInjectionTargetEventFiredForTagLibraryListener() {
        assertNotNull(ProcessInjectionTargetObserver.getTagLibraryListenerEvent());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "aah"), @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "abh"),
            @SpecAssertion(section = BEAN_DISCOVERY_STEPS_EE, id = "bj") })
    public void testProcessInjectionTargetEventFiredForServlet() {
        assertNotNull(ProcessInjectionTargetObserver.getServletEvent());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "aai"), @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "abi"),
            @SpecAssertion(section = BEAN_DISCOVERY_STEPS_EE, id = "bk") })
    public void testProcessInjectionTargetEventFiredForFilter() {
        assertNotNull(ProcessInjectionTargetObserver.getFilterEvent());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "aas"), @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "aao"),
            @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "aan") })
    public void testTypeOfProcessInjectionTargetParameter() {
        assertFalse(ProcessInjectionTargetObserver.isStringObserved());
        assertTrue(ProcessInjectionTargetObserver.isTagHandlerSubTypeObserved());
        assertFalse(ProcessInjectionTargetObserver.isTagHandlerSuperTypeObserved());
        assertFalse(ProcessInjectionTargetObserver.isServletSuperTypeObserved());
        assertTrue(ProcessInjectionTargetObserver.isServletSubTypeObserved());
        assertFalse(ProcessInjectionTargetObserver.isListenerSuperTypeObserved());
    }

}
