/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.cdi.tck.tests.event.observer.priority.contextLifecycleEvent.ee;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_ORDERING;

import java.io.IOException;
import java.net.URL;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class RequestContextLifecycleEventObserverOrderingTest extends AbstractTest {

    @ArquillianResource
    private URL contextPath;

    private static final String seq = "ABC";

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withClasses(RequestContextLifecycleObserver.class, InfoServlet.class)
                .withTestClass(RequestContextLifecycleEventObserverOrderingTest.class).build();
    }

    @Test
    @SpecAssertion(section = OBSERVER_ORDERING, id = "b")
    public void testContextLifecycleEventOrdering() throws IOException {

        WebClient client = new WebClient();

        TextPage page = client.getPage(contextPath);
        Assert.assertTrue(page.getContent().toString().contains("Initialized observer sequence: " + seq));
        TextPage page2 = client.getPage(contextPath);
        Assert.assertTrue(page2.getContent().toString().contains("Initialized observer sequence: " + seq + seq));
        Assert.assertTrue(page2.getContent().toString().contains("Destroyed observer sequence: " + seq));
    }

}
