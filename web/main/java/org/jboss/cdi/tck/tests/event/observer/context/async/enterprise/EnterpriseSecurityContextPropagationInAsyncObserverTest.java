/*
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
package org.jboss.cdi.tck.tests.event.observer.context.async.enterprise;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_METHOD_INVOCATION_CONTEXT_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.ExecutionException;

import jakarta.ejb.EJBAccessException;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@Test(groups = { INTEGRATION, JAVAEE_FULL })
@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseSecurityContextPropagationInAsyncObserverTest extends AbstractTest {

    @Inject
    Student student;

    @Inject
    Teacher teacher;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EnterpriseSecurityContextPropagationInAsyncObserverTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_METHOD_INVOCATION_CONTEXT_EE, id = "a"),
            @SpecAssertion(section = OBSERVER_METHOD_INVOCATION_CONTEXT_EE, id = "b") })
    public void testSecurityContextPropagation() throws InterruptedException, ExecutionException {
        assertEquals(Student.STUDENT_MESSAGE, student.print().getContent());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_METHOD_INVOCATION_CONTEXT_EE, id = "a"),
            @SpecAssertion(section = OBSERVER_METHOD_INVOCATION_CONTEXT_EE, id = "b") })
    public void testSecurityContextNotPropagated() throws InterruptedException {
        Throwable expectedException = teacher.print();
        assertNotNull(expectedException);
        assertTrue(expectedException.getCause() instanceof EJBAccessException);
    }
}
