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
package org.jboss.cdi.tck.tests.implementation.simple.ee.components;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.JAVAEE_COMPONENTS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.net.URL;
import java.util.Arrays;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Right now only Servlet Java EE component is tested.
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = INTEGRATION)
public class JavaEEComponentsTest extends AbstractTest {

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(JavaEEComponentsTest.class).build();
    }

    @ArquillianResource
    private URL contextPath;

    @Test
    @SpecAssertions({ @SpecAssertion(section = JAVAEE_COMPONENTS, id = "a") })
    public void testComponentInject() throws Exception {
        assertComponentInstancesNotEqual(IntrospectServlet.MODE_INJECT);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = JAVAEE_COMPONENTS, id = "b") })
    public void testComponentProducerMethod() throws Exception {
        assertComponentInstancesNotEqual(IntrospectServlet.MODE_PRODUCER_METHOD);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = JAVAEE_COMPONENTS, id = "c") })
    public void testComponentProducerField() throws Exception {
        assertComponentInstancesNotEqual(IntrospectServlet.MODE_PRODUCER_FIELD);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = JAVAEE_COMPONENTS, id = "d") })
    public void testComponentObserverMethod() throws Exception {
        assertComponentInstancesNotEqual(IntrospectServlet.MODE_OBSERVER_METHOD);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = JAVAEE_COMPONENTS, id = "e") })
    public void testComponentDisposerMethod() throws Exception {
        assertComponentInstancesNotEqual(IntrospectServlet.MODE_DISPOSER_METHOD);
    }

    private void assertComponentInstancesNotEqual(String mode) throws Exception {
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        TextPage page = webClient.getPage(contextPath + "test?mode=" + mode);
        String[] ids = page.getContent().split(":");
        System.out.println(Arrays.toString(ids));
        assertEquals(ids.length, 2);
        assertNotEquals(ids[0], ids[1]);
    }

}
