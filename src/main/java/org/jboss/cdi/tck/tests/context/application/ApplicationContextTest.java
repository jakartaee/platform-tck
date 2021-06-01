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
package org.jboss.cdi.tck.tests.context.application;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.TestGroups.JAX_RS;
import static org.jboss.cdi.tck.cdi.Sections.APPLICATION_CONTEXT_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.jaxrs.JaxRsActivator;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * @author David Allen
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class ApplicationContextTest extends AbstractTest {

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ApplicationContextTest.class)
                .withClass(JaxRsActivator.class)
                .withWebXml("web.xml")
                .withWebResource("SimplePage.html").build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = APPLICATION_CONTEXT_EE, id = "aa")
    public void testApplicationScopeActiveDuringServiceMethod() throws Exception {
        WebClient webClient = new WebClient();
        webClient.getPage(contextPath + "TestServlet?test=servlet");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = APPLICATION_CONTEXT_EE, id = "ab")
    public void testApplicationScopeActiveDuringDoFilterMethod() throws Exception {
        WebClient webClient = new WebClient();
        webClient.getPage(contextPath + "SimplePage.html");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = APPLICATION_CONTEXT_EE, id = "ac")
    public void testApplicationScopeActiveDuringServletContextListenerInvocation() throws Exception {
        WebClient webClient = new WebClient();
        webClient.getPage(contextPath + "TestServlet?test=servletContextListener");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = APPLICATION_CONTEXT_EE, id = "ad")
    public void testApplicationScopeActiveDuringHttpSessionListenerInvocation() throws Exception {
        WebClient webClient = new WebClient();
        webClient.getPage(contextPath + "TestServlet?test=httpSessionListener");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = APPLICATION_CONTEXT_EE, id = "af")
    public void testApplicationScopeActiveDuringServletRequestListenerInvocation() throws Exception {
        WebClient webClient = new WebClient();
        webClient.getPage(contextPath + "TestServlet?test=servletRequestListener");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = APPLICATION_CONTEXT_EE, id = "e")
    public void testApplicationContextSharedBetweenServletRequests() throws Exception {
        WebClient webClient = new WebClient();
        TextPage firstRequestResult = webClient.getPage(contextPath + "IntrospectApplication");
        assertNotNull(firstRequestResult.getContent());
        assertTrue(Double.parseDouble(firstRequestResult.getContent()) != 0);
        // Make a second request and make sure the same context is used
        TextPage secondRequestResult = webClient.getPage(contextPath + "IntrospectApplication");
        assertNotNull(secondRequestResult.getContent());
        // should be same random number
        assertEquals(Double.parseDouble(secondRequestResult.getContent()), Double.parseDouble(firstRequestResult.getContent()));
    }

    /**
     * Related to CDITCK-96.
     *
     * @throws Exception
     */
    @Test(groups = { INTEGRATION, JAX_RS })
    @SpecAssertion(section = APPLICATION_CONTEXT_EE, id = "e")
    public void testApplicationContextSharedBetweenJaxRsRequests() throws Exception {
        WebClient webClient = new WebClient();
        TextPage firstRequestResult = webClient.getPage(contextPath + "rest/application-id");
        assertNotNull(firstRequestResult.getContent());
        assertTrue(Double.parseDouble(firstRequestResult.getContent()) != 0);
        // Make a second request and make sure the same context is used
        TextPage secondRequestResult = webClient.getPage(contextPath + "rest/application-id");
        assertNotNull(secondRequestResult.getContent());
        // should be same random number
        assertEquals(Double.parseDouble(secondRequestResult.getContent()), Double.parseDouble(firstRequestResult.getContent()));
    }

}
