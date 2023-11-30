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
package org.jboss.cdi.tck.tests.context.conversation.event;

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
 * <p>
 * This test was originally part of Weld test suite.
 * </p>
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class LongRunningConversationLifecycleEventTest extends AbstractTest {

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassDefinition(LongRunningConversationLifecycleEventTest.class)
                .withClasses(Servlet.class, ApplicationScopedObserver.class, ConversationScopedObserver.class,
                        ConversationScopedBean.class).build();
    }

    @Test
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "ba")
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "bb")
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "bc")
    public void testLifecycleEventFiredForLongRunningConversation() throws Exception {

        WebClient client = new WebClient();

        // The current transient conversation is promoted to long-running
        TextPage page = client.getPage(contextPath + "/begin");
        assertTrue(page.getContent().contains("Initialized:true"));
        assertTrue(page.getContent().contains("Before Destroyed:false"));
        assertTrue(page.getContent().contains("Destroyed:false"));
        assertTrue(page.getContent().contains("cid:" + ConversationScopedBean.CID));

        // The long-running conversation is reassociated
        page = client.getPage(contextPath + "/display?cid=" + ConversationScopedBean.CID);
        assertTrue(page.getContent().contains("Initialized:true"));
        assertTrue(page.getContent().contains("Before Destroyed:false"));
        assertTrue(page.getContent().contains("Destroyed:false"));
        assertTrue(page.getContent().contains("cid:" + ConversationScopedBean.CID));

        page = client.getPage(contextPath + "/display?cid=" + ConversationScopedBean.CID);
        assertTrue(page.getContent().contains("Initialized:true"));
        assertTrue(page.getContent().contains("Before Destroyed:false"));
        assertTrue(page.getContent().contains("Destroyed:false"));
        assertTrue(page.getContent().contains("cid:" + ConversationScopedBean.CID));

        // The long-running conversation is marked transient
        page = client.getPage(contextPath + "/end?cid=" + ConversationScopedBean.CID);
        assertTrue(page.getContent().contains("Initialized:true"));
        assertTrue(page.getContent().contains("Before Destroyed:false"));
        assertTrue(page.getContent().contains("Destroyed:false"));
        assertTrue(page.getContent().contains("cid:null"));

        // The current transient conversation is initialized
        // The last long-running conversation was destroyed after the previous servlet request service() method completed
        page = client.getPage(contextPath + "/display");
        assertTrue(page.getContent().contains("Initialized:true"));
        assertTrue(page.getContent().contains("Before Destroyed:true"));
        assertTrue(page.getContent().contains("Destroyed:true"));
        assertTrue(page.getContent().contains("cid:null"));
    }

}
