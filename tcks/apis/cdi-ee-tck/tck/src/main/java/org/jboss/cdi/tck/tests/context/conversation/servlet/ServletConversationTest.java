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
package org.jboss.cdi.tck.tests.context.conversation.servlet;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.CONVERSATION_CONTEXT_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class ServletConversationTest extends AbstractTest {

    private final String CONVERSATION_CREATED = "conversationCreated";
    private final String CONVERSATION_DESTROYED = "conversationDestroyed";
    private final String BEFORE_INVALIDATE = "beforeInvalidate";
    private final String AFTER_INVALIDATE = "afterInvalidate";

    @ArquillianResource
    private URL url;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ServletConversationTest.class)
                .withWebResource("message.html", "message.html").build();
    }

    @Test
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "aa")
    public void testTransientConversation() throws Exception {
        WebClient client = new WebClient();
        TextPage page = client.getPage(getPath("/display", null));
        assertTrue(page.getContent().contains("message: Hello"));
        assertTrue(page.getContent().contains("cid: [null]"));
        assertTrue(page.getContent().contains("transient: true"));
    }

    @Test
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "aa")
    public void testLongRunningConversation() throws Exception {
        WebClient client = new WebClient();

        // begin conversation
        TextPage initialPage = client.getPage(getPath("/begin", null));
        String content = initialPage.getContent();
        assertTrue(content.contains("message: Hello"));
        assertTrue(content.contains("transient: false"));

        String cid = getCid(content);

        // verify conversation is not transient
        {
            TextPage page = client.getPage(getPath("/display", cid));
            assertTrue(page.getContent().contains("message: Hello"));
            assertTrue(page.getContent().contains("cid: [" + cid + "]"));
            assertTrue(page.getContent().contains("transient: false"));
        }

        // modify conversation state
        {
            TextPage page = client.getPage(getPath("/set", cid) + "&message=Hi");
            assertTrue(page.getContent().contains("message: Hi"));
            assertTrue(page.getContent().contains("cid: [" + cid + "]"));
            assertTrue(page.getContent().contains("transient: false"));
        }

        // verify conversation state
        {
            TextPage page = client.getPage(getPath("/display", cid));
            assertTrue(page.getContent().contains("message: Hi"));
            assertTrue(page.getContent().contains("cid: [" + cid + "]"));
            assertTrue(page.getContent().contains("transient: false"));
        }

        // end conversation
        {
            TextPage page = client.getPage(getPath("/end", cid));
            assertTrue(page.getContent().contains("message: Hi"));
            assertTrue(page.getContent().contains("transient: true"));
        }

        // verify that the conversation can no longer be restored
        try {
            Page page = client.getPage(getPath("/display", cid));
            fail("Access to /display should have failed");
        } catch (FailingHttpStatusCodeException e) {
            assertEquals(e.getStatusCode(), 500);
        }
    }

    @Test
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "aa")
    public void testPost() throws Exception {
        WebClient client = new WebClient();

        // begin conversation
        TextPage initialPage = client.getPage(getPath("/begin", null));
        String content = initialPage.getContent();
        assertTrue(content.contains("message: Hello"));
        assertTrue(content.contains("transient: false"));

        String cid = getCid(content);

        // submit a form
        {
            HtmlPage form = client.getPage(url.toString() + "message.html");
            getFirstMatchingElement(form, HtmlTextInput.class, "message").setValueAttribute("Hola!");
            getFirstMatchingElement(form, HtmlTextInput.class, "cid").setValueAttribute(cid);
            TextPage page = getFirstMatchingElement(form, HtmlSubmitInput.class, "submit").click();

            assertTrue(page.getContent().contains("message: Hola!"));
            assertTrue(page.getContent().contains("cid: [" + cid + "]"));
            assertTrue(page.getContent().contains("transient: false"));
        }

        // verify conversation state
        {
            TextPage page = client.getPage(getPath("/display", cid));
            assertTrue(page.getContent().contains("message: Hola!"));
            assertTrue(page.getContent().contains("cid: [" + cid + "]"));
            assertTrue(page.getContent().contains("transient: false"));
        }
    }

    @Test
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "qa")
    public void testInvalidatingSessionDestroysConversation() throws Exception {
        WebClient client = new WebClient();

        // reset ActionSequence and begin conversation
        String cid = beginConversation(client, true);

        // Invalidate the session
        {
            client.getPage(getPath("/invalidateSession", cid));
        }
        
        TextPage sequence = client.getPage(getPath("/getSequence", null));
        String result = sequence.getContent().trim();
        
        // Construct expected result locally
        ActionSequence.reset();
        ActionSequence.addAction(CONVERSATION_CREATED);
        ActionSequence.addAction(BEFORE_INVALIDATE);
        ActionSequence.addAction(AFTER_INVALIDATE);
        ActionSequence.addAction(CONVERSATION_DESTROYED);
            
        // Verify that the action sequence fetched from server is equal to the expected sequence
        assertEquals(result, ActionSequence.getSequence().dataToCsv());
        
        // Additional verification that the conversation cannot be associated
        try {
            Page page = client.getPage(getPath("/display", cid));
            fail("Access to /display should have failed");
        } catch (FailingHttpStatusCodeException e) {
            assertEquals(e.getStatusCode(), 500);
        }
    }

    @Test
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "qa")
    public void testInvalidatingSessionDestroysAllLongRunningConversations() throws Exception {
        WebClient client = new WebClient();

        // reset ActionSequence and begin two conversations
        String firstCid = beginConversation(client, true);
        String secondCid = beginConversation(client, false);
        assertFalse(firstCid.equals(secondCid));
        
        // Invalidate the session with one cid
        client.getPage(getPath("/invalidateSession", secondCid));
        
        TextPage sequence = client.getPage(getPath("/getSequence", null));
        String result = sequence.getContent().trim();
        
        // Construct expected result locally
        // Two conv. should be created and both destroyed after invalidation
        ActionSequence.reset();
        ActionSequence.addAction(CONVERSATION_CREATED);
        ActionSequence.addAction(CONVERSATION_CREATED);
        ActionSequence.addAction(BEFORE_INVALIDATE);
        ActionSequence.addAction(AFTER_INVALIDATE);
        ActionSequence.addAction(CONVERSATION_DESTROYED);
        ActionSequence.addAction(CONVERSATION_DESTROYED);
            
        // Verify that the action sequence fetched from server is equal to the expected sequence
        assertEquals(result, ActionSequence.getSequence().dataToCsv());
    }
    
    protected String getCid(String content) {
        return content.substring(content.indexOf("cid: [") + 6, content.indexOf("]"));
    }

    protected String getPath(String viewId, String cid) {
        StringBuilder builder = new StringBuilder(url.toString());
        builder.append("servlet");
        builder.append(viewId);
        if (cid != null) {
            builder.append("?");
            builder.append("cid");
            builder.append("=");
            builder.append(cid);
        }
        return builder.toString();
    }

    protected <T extends HtmlElement> T getFirstMatchingElement(HtmlPage page, Class<T> elementClass, String id) {

        Set<T> inputs = getElements(page.getBody(), elementClass);
        for (T input : inputs) {
            if (input.getId().contains(id)) {
                return input;
            }
        }
        return null;
    }

    protected <T> Set<T> getElements(HtmlElement rootElement, Class<T> elementClass) {
        Set<T> result = new HashSet<T>();

        for (HtmlElement element : rootElement.getHtmlElementDescendants()) {
            result.addAll(getElements(element, elementClass));
        }

        if (elementClass.isInstance(rootElement)) {
            result.add(elementClass.cast(rootElement));
        }
        return result;

    }
    
    protected String beginConversation(WebClient client, boolean resetSequence) throws IOException {
        if (resetSequence) {
            client.getPage(getPath("/resetSequence", null));
        }
        TextPage initialPage = client.getPage(getPath("/begin", null));
        String content = initialPage.getContent();
        assertTrue(content.contains("message: Hello"));
        assertTrue(content.contains("transient: false"));
        return getCid(content);
    }
    
}
