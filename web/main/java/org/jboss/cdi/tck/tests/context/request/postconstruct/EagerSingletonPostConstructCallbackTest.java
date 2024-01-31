/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.context.request.postconstruct;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.REQUEST_CONTEXT;
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
 * Test the request context is active during @PostConstruct callback of an eager singleton.
 * 
 * @author Martin Kouba
 * 
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class EagerSingletonPostConstructCallbackTest extends AbstractTest {

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(EagerSingletonPostConstructCallbackTest.class)
                .withClasses(EagerSingleton.class, Action.class, RequestContextObserver.class, EagerSingletonInfoServlet.class)
                .build();
    }

    @ArquillianResource
    private URL contextPath;

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = REQUEST_CONTEXT, id = "db")
    @SpecAssertion(section = REQUEST_CONTEXT, id = "eb")
    public void testEagerSingletonPostConstructCallback() throws Exception {

        WebClient client = new WebClient();

        TextPage page = client.getPage(contextPath + "eager");
        // Context was active
        assertTrue(page.getContent().contains("Active:true"));
        // Only singleton callback, current request not destroyed yet
        assertTrue(page.getContent().contains("Destroyed requests:1"));
    }

}
