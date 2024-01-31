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
package org.jboss.cdi.tck.tests.implementation.builtin.servlet;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.ADDITIONAL_BUILTIN_BEANS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.net.URL;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Test that servlet container built-in beans are available for injection.
 * 
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class ServletContainerBuiltinBeanTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ServletContainerBuiltinBeanTest.class).build();
    }

    @Inject
    LowercaseConverter lowercaseConverter;

    @Test
    @SpecAssertions({ @SpecAssertion(section = ADDITIONAL_BUILTIN_BEANS, id = "da"), @SpecAssertion(section = ADDITIONAL_BUILTIN_BEANS, id = "db"),
            @SpecAssertion(section = ADDITIONAL_BUILTIN_BEANS, id = "dc") })
    public void testBuiltinBeansAvailableForInjection() {

        String result = lowercaseConverter.convert("Foo");
        assertEquals(result, "foo");

        assertNotNull(lowercaseConverter.getHttpServletRequest());
        assertNotNull(lowercaseConverter.getHttpServletRequest().getRequestURL());
        assertNotNull(lowercaseConverter.getHttpSession());
        assertNotNull(lowercaseConverter.getHttpSession().getId());
        assertNotNull(lowercaseConverter.getServletContext());
        assertTrue(lowercaseConverter.getServletContext().getMajorVersion() >= 2);
    }

    @RunAsClient
    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = ADDITIONAL_BUILTIN_BEANS, id = "da"), @SpecAssertion(section = ADDITIONAL_BUILTIN_BEANS, id = "db"),
            @SpecAssertion(section = ADDITIONAL_BUILTIN_BEANS, id = "dc") })
    public void testBuiltinBeansFromClient(@ArquillianResource URL contextPath) throws Exception {

        WebClient client = new WebClient();

        TextPage requestPage = client.getPage(contextPath + "/convert-request?text=BaR");
        assertEquals(requestPage.getContent(), "bar");

        TextPage sessionPage = client.getPage(contextPath + "/convert-session");
        assertEquals(sessionPage.getContent(), "session");

        TextPage contextPage = client.getPage(contextPath + "/convert-context");
        assertEquals(contextPage.getContent(), "servletcontext");
    }

}
