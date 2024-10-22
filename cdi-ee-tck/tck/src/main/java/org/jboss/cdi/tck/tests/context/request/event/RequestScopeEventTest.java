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
package org.jboss.cdi.tck.tests.context.request.event;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.REQUEST_CONTEXT;
import static org.jboss.cdi.tck.cdi.Sections.REQUEST_CONTEXT_EE;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class RequestScopeEventTest extends AbstractTest {

    @ArquillianResource(Servlet.class)
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(RequestScopeEventTest.class).build();
    }

    @Test
    @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "ja")
    @SpecAssertion(section = REQUEST_CONTEXT, id = "a")
    @SpecAssertion(section = REQUEST_CONTEXT, id = "b")
    @SpecAssertion(section = REQUEST_CONTEXT, id = "c")
    public void test() throws Exception {
        WebClient client = new WebClient();

        TextPage page1 = client.getPage(contextPath + "?foo=bar");
        assertTrue(page1.getContent().contains("Initialized requests:1")); // the current request
        assertTrue(page1.getContent().contains("Before destroyed requests:0"));
        assertTrue(page1.getContent().contains("Destroyed requests:0")); // not destroyed yet

        TextPage page2 = client.getPage(contextPath + "?foo=bar");
        assertTrue(page2.getContent().contains("Initialized requests:2"));
        assertTrue(page2.getContent().contains("Before destroyed requests:1"));
        assertTrue(page2.getContent().contains("Destroyed requests:1"));
    }
}
