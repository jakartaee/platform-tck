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

package org.jboss.cdi.tck.tests.context.request.async;

import static org.jboss.cdi.tck.TestGroups.ASYNC_SERVLET;
import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.REQUEST_CONTEXT_EE;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertTrue;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.context.application.async.AsyncServlet;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author Martin Kouba
 * @author Tomas Remes
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class RequestContextAsyncListenerTest extends AbstractTest {

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(RequestContextAsyncListenerTest.class).build();
    }

    @Test(groups = {INTEGRATION, ASYNC_SERVLET})
    @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "ad")
    @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "bd")
    public void testRequestContextActiveOnComplete() throws Exception {

        WebClient webClient = new WebClient();
        webClient.getPage(getPath(AsyncServlet.TEST_COMPLETE));

        // check status servlet for results
        TextPage results = webClient.getPage(contextPath + "Status");
        assertTrue(results.getContent().contains("onComplete: true"));

        // Indirectly test request context is destroyed after onComplete()
        webClient.getPage(getPath(AsyncServlet.TEST_COMPLETE));
        TextPage results2 = webClient.getPage(contextPath + "Status");
        assertNotEquals(extractSimpleRequestBeanIdString(results.getContent()),
                extractSimpleRequestBeanIdString(results2.getContent()));
    }

    @Test(groups = {INTEGRATION, ASYNC_SERVLET})
    @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "ad")
    public void testRequestContextActiveOnTimeout() throws Exception {
        WebClient webClient = new WebClient();
        webClient.getPage(getPath(AsyncServlet.TEST_TIMEOUT));

        // check status servlet for results
        TextPage results = webClient.getPage(contextPath + "Status");
        assertTrue(results.getContent().contains("onTimeout: true"));
    }

    @Test(groups = {INTEGRATION, ASYNC_SERVLET})
    @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "ad")
    public void testRequestContextActiveOnError() throws Exception {
        WebClient webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getPage(getPath(AsyncServlet.TEST_ERROR));
        TextPage results = webClient.getPage(contextPath + "Status");
        assertTrue(results.getContent().contains("onError: true"));
    }

    @Test(groups = {INTEGRATION, ASYNC_SERVLET})
    @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "ad")
    public void testRequestContextActiveOnStartAsync() throws Exception {
        WebClient webClient = new WebClient();
        webClient.getPage(getPath(AsyncServlet.TEST_LOOP));
        TextPage results = webClient.getPage(contextPath + "Status");
        assertTrue(results.getContent().contains("onStartAsync: true"));
    }

    private String getPath(String test) {
        return contextPath + "AsyncServlet?test=" + test;
    }

    private String extractSimpleRequestBeanIdString(String content) {
        String[] tokens = content.split(",");
        // See SimpleAsyncListener#getInfo()
        return tokens[4];
    }
}
