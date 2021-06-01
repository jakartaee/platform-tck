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
package org.jboss.cdi.tck.tests.context.conversation.filter;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.CONVERSATION_CONTEXT_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.util.Timer;
import org.jboss.cdi.tck.util.Timer.StopCondition;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;

/**
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class ConversationFilterTest extends AbstractTest {
    
    private static final String JSESSIONID = "JSESSIONID";

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(ConversationFilterTest.class)
                .withWebXml(
                        Descriptors.create(WebAppDescriptor.class).createFilter().filterName(OuterFilter.class.getSimpleName())
                                .filterClass(OuterFilter.class.getName()).up().createFilterMapping()
                                .filterName(OuterFilter.class.getSimpleName()).urlPattern("/*").up().createFilterMapping()
                                .filterName("CDI Conversation Filter").urlPattern("/*").up()).build();
    }

    @ArquillianResource
    protected URL contextPath;

    /**
     * The conversation context should not be active before the conversation is associated with the servlet request.
     *
     * @throws Exception
     */
    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "d")
    public void testConversationActivation() throws Exception {

        WebClient client = new WebClient();

        // Init the long-running conversation
        TextPage initPage = client.getPage(contextPath + "introspect?mode=" + IntrospectServlet.MODE_INIT);
        String cid = extractCid(initPage.getContent());
        assertNotNull(cid);
        assertFalse(cid.isEmpty());

        TextPage resultPage = client.getPage(contextPath + "introspect?mode=" + IntrospectServlet.MODE_INSPECT + "&cid=" + cid);
        assertFalse(Boolean.valueOf(resultPage.getContent()));
    }

    /**
     * "The container ensures that a long-running conversation may be associated with at most one request at a time, by blocking or rejecting concurrent requests."
     *
     * In fact the spec doesn't require the container to reject the concurrent requests, but we don't expect it will block them
     * forever.
     *
     * @throws Exception
     */
    @Test(groups = INTEGRATION)
    @SpecAssertion(section = CONVERSATION_CONTEXT_EE, id = "ua")
    public void testConversationBusy() throws Exception {

        // Init the long-running conversation
        WebClient client = new WebClient();

        TextPage initPage = client.getPage(contextPath + "introspect?mode=" + IntrospectServlet.MODE_INIT);
        String cid = extractCid(initPage.getContent());
        assertNotNull(cid);
        assertFalse(cid.isEmpty());
        String jsessionid = client.getCookieManager().getCookie(JSESSIONID).getValue();
        assertNotNull(jsessionid);
        assertFalse(jsessionid.isEmpty());

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        WebRequest longTask = new WebRequest(IntrospectServlet.MODE_LONG_TASK, contextPath, cid, jsessionid);
        WebRequest busyRequest = new WebRequest(IntrospectServlet.MODE_BUSY_REQUEST, contextPath, cid, jsessionid);

        final Future<String> longTaskFuture = executorService.submit(longTask);
        Timer timer = Timer.startNew(100l);
        final Future<String> busyRequestFuture = executorService.submit(busyRequest);
        timer.setSleepInterval(100l).setDelay(10, TimeUnit.SECONDS).addStopCondition(new StopCondition() {
            @Override
            public boolean isSatisfied() {
                return longTaskFuture.isDone() || busyRequestFuture.isDone();
            }
        }).start();

        assertEquals(longTaskFuture.get(), "OK");
        assertEquals(busyRequestFuture.get(), "BusyConversationException");
        executorService.shutdown();
    }

    /**
     * Note - htmlunit WebClient instance is not thread-safe.
     */
    private class WebRequest implements Callable<String> {

        private String mode;

        private URL contextPath;

        private String cid;

        private String jsessionid;

        public WebRequest(String mode, URL contextPath, String cid, String jsessionid) {
            super();
            this.mode = mode;
            this.contextPath = contextPath;
            this.cid = cid;
            this.jsessionid = jsessionid;
        }

        @Override
        public String call() throws Exception {

            WebClient client = new WebClient();
            client.getCookieManager().addCookie(new Cookie(contextPath.getHost(), JSESSIONID, jsessionid));

            Page page = client.getPage(contextPath + "introspect?mode=" + mode + "&cid=" + cid);

            if (!(page instanceof TextPage)) {
                return "" + page.getWebResponse().getStatusCode();
            }
            TextPage textPage = (TextPage) page;
            return textPage.getContent();
        }

    }

    private String extractCid(String content) {
        String[] tokens = content.split("::");
        if (tokens.length != 2) {
            throw new IllegalArgumentException();
        }
        return tokens[0];
    }

}
