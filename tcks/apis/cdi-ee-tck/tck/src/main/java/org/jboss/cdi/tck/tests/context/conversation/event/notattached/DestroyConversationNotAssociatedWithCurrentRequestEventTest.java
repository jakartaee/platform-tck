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

package org.jboss.cdi.tck.tests.context.conversation.event.notattached;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.CONVERSATION_CONTEXT_EE;
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
 * @author Martin Kouba
 * 
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class DestroyConversationNotAssociatedWithCurrentRequestEventTest extends AbstractTest {

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DestroyConversationNotAssociatedWithCurrentRequestEventTest.class).build();
    }

    @Test
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "bd")
    public void testLifecycleEventFired() throws Exception {

        WebClient webClient = new WebClient();

        // Begin new long-running conversation
        TextPage page = webClient.getPage(contextPath + "test?action=begin");
        assertTrue(page.getContent().contains("cid:" + ConversationScopedBean.CID));

        // Invalidate HTTP session - destroy non-attached long-running conversation
        page = webClient.getPage(contextPath + "test?action=invalidate");

        // Get the info
        page = webClient.getPage(contextPath + "test?action=info");
        assertTrue(page.getContent().contains("destroyed cid:" + ConversationScopedBean.CID));
    }

}
