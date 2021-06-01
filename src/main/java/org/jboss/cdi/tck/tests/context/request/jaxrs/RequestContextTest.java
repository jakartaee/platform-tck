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
package org.jboss.cdi.tck.tests.context.request.jaxrs;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.TestGroups.JAX_RS;
import static org.jboss.cdi.tck.cdi.Sections.REQUEST_CONTEXT_EE;
import static org.testng.Assert.assertNotEquals;
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
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class RequestContextTest extends AbstractTest {

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(RequestContextTest.class).withClass(JaxRsActivator.class).build();
    }

    @Test(groups = { INTEGRATION, JAX_RS})
    @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "b")
    @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "d")
    @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "jb")
    public void testRequestScopeActiveDuringWebServiceInvocation() throws Exception {

        WebClient webClient = new WebClient();

        // New instance of Foo is created for each request
        TextPage resource01 = webClient.getPage(contextPath + "rest/foo");
        Long id01 = Long.valueOf(resource01.getContent());
        TextPage resource02 = webClient.getPage(contextPath + "rest/foo");
        Long id02 = Long.valueOf(resource02.getContent());
        assertNotEquals(id01, id02);

        // At the time the info servlet is generating response, 3 requests were initialized and 2 destroyed
        TextPage resource03 = webClient.getPage(contextPath + "info");
        assertTrue(resource03.getContent().contains("Initialized requests:3"));
        assertTrue(resource03.getContent().contains("Destroyed requests:2"));

        assertTrue(resource03.getContent().contains("Foo destroyed:2"));
    }

}
