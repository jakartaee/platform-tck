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
package org.jboss.cdi.tck.tests.context.session.event;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.SESSION_CONTEXT_EE;
import static org.testng.Assert.assertTrue;

import java.net.URL;

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
 * <p>
 * This test was originally part of Weld test suite.
 * </p>
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class SessionScopeEventTest extends AbstractTest {

    @ArquillianResource(Servlet.class)
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(SessionScopeEventTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SESSION_CONTEXT_EE, id = "da"), @SpecAssertion(section = SESSION_CONTEXT_EE, id = "db"),
            @SpecAssertion(section = SESSION_CONTEXT_EE, id = "dc"),
            @SpecAssertion(section = SESSION_CONTEXT_EE, id = "ca") })
    public void test() throws Exception {
        WebClient client = new WebClient();

        TextPage page = client.getPage(contextPath);
        assertTrue(page.getContent().contains("Initialized sessions:1")); // the current session
        assertTrue(page.getContent().contains("Before Destroyed sessions:0"));
        assertTrue(page.getContent().contains("Destroyed sessions:0")); // not destroyed yet

        // nothing should change
        page = client.getPage(contextPath);
        assertTrue(page.getContent().contains("Initialized sessions:1"));
        assertTrue(page.getContent().contains("Before Destroyed sessions:0"));
        assertTrue(page.getContent().contains("Destroyed sessions:0"));

        // invalidate the session
        page = client.getPage(contextPath + "/invalidate");
        assertTrue(page.getContent().contains("Initialized sessions:1"));
        assertTrue(page.getContent().contains("Before Destroyed sessions:0"));
        // the context is destroyed after the response is sent
        // verify in the next request
        assertTrue(page.getContent().contains("Destroyed sessions:0"));

        page = client.getPage(contextPath);
        // new session context was initialized
        assertTrue(page.getContent().contains("Initialized sessions:2"));
        assertTrue(page.getContent().contains("Before Destroyed sessions:1"));
        // the previous one was destroyed
        assertTrue(page.getContent().contains("Destroyed sessions:1"));
    }
}
