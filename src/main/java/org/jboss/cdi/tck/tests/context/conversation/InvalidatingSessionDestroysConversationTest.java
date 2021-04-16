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
package org.jboss.cdi.tck.tests.context.conversation;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.CONVERSATION_CONTEXT_EE;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.WebClient;

/**
 * @author Nicklas Karlsson
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class InvalidatingSessionDestroysConversationTest extends AbstractConversationTest {

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassDefinition(InvalidatingSessionDestroysConversationTest.class)
                .withClasses(Storm.class, ConversationTestPhaseListener.class, ConversationStatusServlet.class, Cloud.class,
                        CloudController.class, OutermostFilter.class).withWebResource("cloud.xhtml")
                .withWebResource("clouds.xhtml").withWebResource("faces-config.xml", "/WEB-INF/faces-config.xml")
                .withWebXml("web.xml").build();
    }

    @Test
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "qa")
    // Doesn't precisely probe the boundaries of the service() method, rather tests that the conversation
    // is eventually destroyed.
    public void testInvalidatingSessionDestroysConversation() throws Exception {

        WebClient webClient = new WebClient();

        resetCloud(webClient);
        webClient.getPage(getPath("clouds.jsf"));

        assertFalse(isCloudDestroyed(webClient));
        invalidateSession(webClient);
        assertTrue(isCloudDestroyed(webClient));
    }

}
