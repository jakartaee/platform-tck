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
package org.jboss.cdi.tck.tests.context.conversation;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.CONVERSATION;
import static org.jboss.cdi.tck.cdi.Sections.CONVERSATION_CONTEXT_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

/**
 * @author Nicklas Karlsson
 * @author Dan Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class ClientConversationContextTest extends AbstractConversationTest {

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassDefinition(ClientConversationContextTest.class)
                .withClasses(Storm.class, ConversationTestPhaseListener.class, ConversationStatusServlet.class, Cloud.class,
                        CloudController.class, OutermostFilter.class, Cumulus.class, BuiltInConversation.class,
                        ConversationContextObserver.class).withWebResource("home.xhtml").withWebResource("cloud.xhtml")
                .withWebResource("clouds.xhtml").withWebResource("cumulus.xhtml").withWebResource("builtin.xhtml")
                .withWebResource("error.xhtml").withWebResource("storm.xhtml").withWebResource("rain.xhtml")
                .withWebResource("faces-config.xml", "/WEB-INF/faces-config.xml").withWebXml("web.xml").build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "hb")
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "o")
    public void testConversationIdSetByContainerIsUnique() throws Exception {
        WebClient client = new WebClient();
        HtmlPage storm = client.getPage(getPath("storm.jsf"));
        HtmlSubmitInput beginConversationButton = getFirstMatchingElement(storm, HtmlSubmitInput.class,
                "beginConversationButton");
        storm = beginConversationButton.click();

        String c1 = getCid(storm);

        storm = client.getPage(getPath("storm.jsf"));
        beginConversationButton = getFirstMatchingElement(storm, HtmlSubmitInput.class, "beginConversationButton");
        storm = beginConversationButton.click();

        String c2 = getCid(storm);

        assertNotEquals(c1, c2);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "j")
    public void testTransientConversationInstancesDestroyedAtRequestEnd() throws Exception {
        WebClient client = new WebClient();

        resetCloud(client);
        resetConversationContextObserver(client);

        HtmlPage page = client.getPage(getPath("cloud.jsf"));

        assertFalse(isLongRunning(page));
        assertTrue(isCloudDestroyed(client));
        assertTrue(isConversationContextDestroyed(client));
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "k")
    public void testLongRunningConversationInstancesNotDestroyedAtRequestEnd() throws Exception {
        WebClient client = new WebClient();
        HtmlPage storm = client.getPage(getPath("storm.jsf"));
        HtmlSubmitInput beginConversationButton = getFirstMatchingElement(storm, HtmlSubmitInput.class,
                "beginConversationButton");
        storm = beginConversationButton.click();

        resetCloud(client);

        client.getPage(getPath("cloud.jsf", getCid(storm)));
        assert !isCloudDestroyed(client);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "p")
    public void testConversationsDontCrossSessionBoundary1() throws Exception {
        WebClient client = new WebClient();
        // Load the page
        HtmlPage rain = client.getPage(getPath("rain.jsf"));

        // begin a conversation
        HtmlSubmitInput beginConversationButton = getFirstMatchingElement(rain, HtmlSubmitInput.class,
                "beginConversationButton");
        rain = beginConversationButton.click();
        String cid = getCid(rain);

        // Cause rain
        HtmlSubmitInput rainButton = getFirstMatchingElement(rain, HtmlSubmitInput.class, "rain");
        rain = rainButton.click();

        // Re-request the page, inside the conversation and check it has rained
        rain = client.getPage(getPath("rain.jsf", cid));
        assertTrue(hasRained(rain));

        // Invalidate the session, invalidate the conversation-scoped cloud
        invalidateSession(client);

        // Re-request the page, check NonexistentConversationException is thrown
        verifyNonexistentConversationExceptionThrown(client.getPage(getPath("rain.jsf", cid)));
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "p")
    public void testConversationsDontCrossSessionBoundary2() throws Exception {
        WebClient client = new WebClient();

        // Load the page
        HtmlPage rain = client.getPage(getPath("rain.jsf"));

        // begin a conversation
        HtmlSubmitInput beginConversationButton = getFirstMatchingElement(rain, HtmlSubmitInput.class,
                "beginConversationButton");
        rain = beginConversationButton.click();
        String cid = getCid(rain);

        // Cause rain
        HtmlSubmitInput rainButton = getFirstMatchingElement(rain, HtmlSubmitInput.class, "rain");
        rain = rainButton.click();

        // Re-request the page, inside the conversation and check it has rained
        rain = client.getPage(getPath("rain.jsf", cid));
        assertTrue(hasRained(rain));

        // Create a new web client and load the page
        WebClient client2 = new WebClient();
        verifyNonexistentConversationExceptionThrown(client2.getPage(getPath("rain.jsf", cid)));
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "a")
    public void testConversationActiveDuringNonFacesRequest() throws Exception {
        WebClient client = new WebClient();
        HtmlPage page = client.getPage(getPath("cloud.jsf"));
        HtmlSpan span = getFirstMatchingElement(page, HtmlSpan.class, "cloudName");
        assert span.getTextContent().equals(Cloud.NAME);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "f")
    public void testConversationBeginMakesConversationLongRunning() throws Exception {
        WebClient client = new WebClient();
        HtmlPage page = client.getPage(getPath("cumulus.jsf"));
        assert !isLongRunning(page);

        // begin a conversation
        HtmlSubmitInput beginConversationButton = getFirstMatchingElement(page, HtmlSubmitInput.class,
                "beginConversationButton");
        page = beginConversationButton.click();
        assert isLongRunning(page);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION, id = "r")
    public void testBeginAlreadyLongRunningConversationThrowsException() throws Exception {
        WebClient client = new WebClient();
        HtmlPage page = client.getPage(getPath("cumulus.jsf"));
        assert !isLongRunning(page);

        // begin a conversation
        HtmlSubmitInput beginConversationButton = getFirstMatchingElement(page, HtmlSubmitInput.class,
                "beginConversationButton");
        page = beginConversationButton.click();
        assert isLongRunning(page);

        // begin a conversation again and check that IllegalStateException is thrown
        HtmlSubmitInput beginConversationButton2 = getFirstMatchingElement(page, HtmlSubmitInput.class,
                "beginConversationAndSwallowException");
        page = beginConversationButton2.click();
        assert page.getBody().getTextContent().contains("Hello world!");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION, id = "s")
    public void testBeginConversationWithExplicitIdAlreadyUsedByDifferentConversation() throws Exception {

        WebClient client = new WebClient();
        HtmlPage page = client.getPage(getPath("cumulus.jsf"));
        assertFalse(isLongRunning(page));

        // Begin a conversation with explicit id
        HtmlSubmitInput beginConversationButton = (HtmlSubmitInput) page
                .getElementById("form:beginNextConversationIdentifiedByCustomIdentifier");
        page = beginConversationButton.click();
        assertTrue(isLongRunning(page));

        // Try it again with the same id
        page = client.getPage(getPath("cumulus.jsf"));
        beginConversationButton = (HtmlSubmitInput) page
                .getElementById("form:beginConversationIdentifiedByCustomIdentifierAndSwallowException");
        page = beginConversationButton.click();
        // Exception is swallowed and the browser is redirected to home
        assertTrue(page.getBody().getTextContent().contains("Hello world!"));
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "g")
    @SpecAssertion(section = CONVERSATION, id = "k")
    @SpecAssertion(section = CONVERSATION, id = "o")
    public void testConversationEndMakesConversationTransient() throws Exception {
        WebClient client = new WebClient();
        HtmlPage page = client.getPage(getPath("cumulus.jsf"));
        assert !isLongRunning(page);

        // begin a conversation
        HtmlSubmitInput beginConversationButton = getFirstMatchingElement(page, HtmlSubmitInput.class,
                "beginConversationButton");
        page = beginConversationButton.click();
        assert isLongRunning(page);

        // end a conversation
        HtmlSubmitInput endConversationButton = getFirstMatchingElement(page, HtmlSubmitInput.class, "endConversationButton");
        page = endConversationButton.click();
        assert !isLongRunning(page);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION, id = "q")
    public void testEndTransientConversationThrowsException() throws Exception {
        WebClient client = new WebClient();
        HtmlPage page = client.getPage(getPath("cumulus.jsf"));
        assert !isLongRunning(page);

        // try ending a transient conversation
        HtmlSubmitInput endConversationButton = getFirstMatchingElement(page, HtmlSubmitInput.class,
                "endConversationAndSwallowException");
        page = endConversationButton.click();
        assert page.getBody().getTextContent().contains("Hello world!");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION, id = "ib")
    @SpecAssertion(section = CONVERSATION, id = "iaa")
    public void testBeanWithRequestScope() throws Exception {
        WebClient client = new WebClient();
        HtmlPage page = client.getPage(getPath("builtin.jsf"));
        assert page.getBody().getTextContent().contains("Correct scope: true");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION, id = "id")
    public void testBeanWithDefaultQualifier() throws Exception {
        WebClient client = new WebClient();
        HtmlPage page = client.getPage(getPath("builtin.jsf"));
        assert page.getBody().getTextContent().contains("Correct qualifier: true");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION, id = "ie")
    public void testBeanWithNameJavaxEnterpriseContextConversation() throws Exception {
        WebClient client = new WebClient();
        HtmlPage page = client.getPage(getPath("builtin.jsf"));
        assert page.getBody().getTextContent().contains("Correct name: true");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION, id = "l")
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "e")
    public void testTransientConversationHasNullId() throws Exception {
        WebClient client = new WebClient();
        HtmlPage page = client.getPage(getPath("builtin.jsf"));
        assert page.getBody().getTextContent().contains("Default conversation has null id: true");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "ha")
    @SpecAssertion(section = CONVERSATION, id = "j")
    public void testConversationIdMayBeSetByApplication() throws Exception {
        WebClient client = new WebClient();
        HtmlPage page = client.getPage(getPath("cumulus.jsf"));
        assertFalse(isLongRunning(page));

        // begin a conversation
        HtmlSubmitInput beginConversationButton = (HtmlSubmitInput) page
                .getElementById("form:beginConversationIdentifiedByCustomIdentifier");
        page = beginConversationButton.click();
        assertTrue(isLongRunning(page));
        assertEquals(getCid(page), "humilis");
        assertTrue(page.getBody().getTextContent().contains("Cumulus humilis"));

        HtmlPage nextPage = client.getPage(getPath("cumulus.jsf?cid=humilis"));
        assertTrue(isLongRunning(nextPage));
        assertEquals(getCid(page), "humilis");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "hb")
    @SpecAssertion(section = CONVERSATION, id = "j")
    public void testConversationIdMayBeSetByContainer() throws Exception {
        WebClient client = new WebClient();
        HtmlPage page = client.getPage(getPath("cumulus.jsf"));
        assert !isLongRunning(page);

        // begin a conversation
        HtmlSubmitInput beginConversationButton = getFirstMatchingElement(page, HtmlSubmitInput.class,
                "beginConversationButton");
        page = beginConversationButton.click();
        assert isLongRunning(page);
        assert getCid(page) != null;
        assert page.getBody().getTextContent().contains("Cumulus congestus");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "tb")
    public void testNonexistentConversationExceptionThrown() throws Exception {
        WebClient client = new WebClient();
        verifyNonexistentConversationExceptionThrown(client.getPage(getPath("cumulus.jsf?cid=foo")));
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION, id = "m")
    @SpecAssertion(section = CONVERSATION, id = "n")
    public void testSetConversationTimeoutOverride() throws Exception {
        WebClient client = new WebClient();
        HtmlPage page = client.getPage(getPath("cumulus.jsf"));
        assert !isLongRunning(page);

        // begin a conversation and set a custom timeout
        HtmlSubmitInput beginConversationButton = getFirstMatchingElement(page, HtmlSubmitInput.class,
                "beginConversationAndSetTimeout");
        page = beginConversationButton.click();
        assert page.getBody().getTextContent().contains("Cumulonimbus");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION, id = "m")
    public void testConversationHasDefaultTimeout() throws Exception {
        WebClient client = new WebClient();
        HtmlPage page = client.getPage(getPath("cumulus.jsf"));
        assert !isLongRunning(page);

        // begin a conversation
        HtmlSubmitInput beginConversationButton = getFirstMatchingElement(page, HtmlSubmitInput.class,
                "beginConversationButton");
        page = beginConversationButton.click();
        assert page.getBody().getTextContent().contains("Stratocumulus");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "oa")
    public void testSuppressedConversationPropagation() throws Exception {
        WebClient client = new WebClient();

        // Access the start page
        HtmlPage cloud = client.getPage(getPath("cloud.jsf"));
        assertEquals(getFirstMatchingElement(cloud, HtmlSpan.class, "cloudName").getTextContent(), Cloud.NAME);

        // Now start a conversation and check the cloud name changes
        HtmlPage page1 = getFirstMatchingElement(cloud, HtmlSubmitInput.class, Cloud.CUMULUS).click();
        assertEquals(getFirstMatchingElement(page1, HtmlSpan.class, "cloudName").getTextContent(), Cloud.CUMULUS);
        String cid = getCid(page1);

        // Activate the conversation from a GET request
        HtmlPage page2 = client.getPage(getPath("cloud.jsf", cid));
        assertEquals(getFirstMatchingElement(page2, HtmlSpan.class, "cloudName").getTextContent(), Cloud.CUMULUS);

        // Send a GET request with the "cid" parameter and suppressed conversation propagation (using
        // conversationPropagation=none)
        HtmlPage page3 = client.getPage(getPath("cloud.jsf", cid) + "&conversationPropagation=none");
        assertEquals(getFirstMatchingElement(page3, HtmlSpan.class, "cloudName").getTextContent(), Cloud.NAME);
    }


    private void verifyNonexistentConversationExceptionThrown(Page page) {
        if(page instanceof TextPage) {
            TextPage textPage = (TextPage) page;
            assertTrue(textPage.getContent().contains("NonexistentConversationException thrown properly"));
            assertTrue(textPage.getContent().contains("Conversation.isTransient: true"));
            assertTrue(textPage.getContent().contains("Cloud: " + Cloud.NAME));
        } else {
            fail("Unexpected response type: "+page.getClass().getName());
        }
    }
}
