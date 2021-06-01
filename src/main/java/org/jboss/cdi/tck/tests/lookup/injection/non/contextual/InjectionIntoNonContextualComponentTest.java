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
package org.jboss.cdi.tck.tests.lookup.injection.non.contextual;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.TestGroups.PERSISTENCE;
import static org.jboss.cdi.tck.cdi.Sections.FIELDS_INITIALIZER_METHODS_EE;
import static org.jboss.cdi.tck.cdi.Sections.INITIALIZER_METHODS_EE;
import static org.jboss.cdi.tck.cdi.Sections.INJECTED_FIELDS_EE;
import static org.jboss.cdi.tck.cdi.Sections.INJECTION;
import static org.jboss.cdi.tck.cdi.Sections.INJECTION_EE;

import java.net.URL;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class InjectionIntoNonContextualComponentTest extends AbstractTest {

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(InjectionIntoNonContextualComponentTest.class)
                .withClasses(Farm.class, ProcessAnnotatedTypeObserver.class, SessionBean.class, Sheep.class, TagLibraryListener.class,
                        TestFilter.class, TestListener.class, TestServlet.class, TestTagHandler.class)
                .withWebXml("web2.xml")
                .withWebResource("ManagedBeanTestPage.jsp", "ManagedBeanTestPage.jsp")
                .withWebResource("TagPage.jsp", "TagPage.jsp").withWebResource("faces-config.xml", "/WEB-INF/faces-config.xml")
                .withWebResource("TestLibrary.tld", "WEB-INF/TestLibrary.tld")
                .withDefaultPersistenceXml().build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = INJECTION_EE, id = "ef"), @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "ae"),
            @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "bn"), @SpecAssertion(section = INJECTED_FIELDS_EE, id = "b") })
    public void testInjectionIntoServlet() throws Exception {
        WebClient webClient = new WebClient();
        webClient.getPage(contextPath + "Test/Servlet?test=injection");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "af"), @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "bm"),
            @SpecAssertion(section = INITIALIZER_METHODS_EE, id = "b") })
    public void testServletInitializerMethodCalled() throws Exception {
        WebClient webClient = new WebClient();
        webClient.getPage(contextPath + "Test/Servlet?test=initializer");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "bo")
    public void testServletInitCalledAfterResourceInjection() throws Exception {
        WebClient webClient = new WebClient();
        webClient.getPage(contextPath + "Test/Servlet?test=resource");
        webClient.getPage(contextPath + "Test/Servlet?test=ejb");
    }

    @Test(groups = { INTEGRATION, PERSISTENCE })
    @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "bo")
    public void testServletInitCalledAfterPersistenceResourceInjection() throws Exception {
        WebClient webClient = new WebClient();
        webClient.getPage(contextPath + "Test/Servlet?test=persistence");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = INJECTION_EE, id = "eg"), @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "ag"),
            @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "bq"), @SpecAssertion(section = INJECTED_FIELDS_EE, id = "b") })
    public void testInjectionIntoFilter() throws Exception {
        WebClient webClient = new WebClient();
        webClient.getPage(contextPath + "TestFilter?test=injection");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "ah"), @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "bp"),
            @SpecAssertion(section = INITIALIZER_METHODS_EE, id = "b") })
    public void testFilterInitializerMethodCalled() throws Exception {
        WebClient webClient = new WebClient();
        webClient.getPage(contextPath + "TestFilter?test=initializer");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "br")
    public void testFilterInitCalledAfterResourceInjection() throws Exception {
        WebClient webClient = new WebClient();
        webClient.getPage(contextPath + "TestFilter?test=resource");
        webClient.getPage(contextPath + "TestFilter?test=ejb");
    }

    @Test(groups = { INTEGRATION, PERSISTENCE })
    @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "br")
    public void testFilterInitCalledAfterPersistenceResourceInjection() throws Exception {
        WebClient webClient = new WebClient();
        webClient.getPage(contextPath + "TestFilter?test=persistence");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = INJECTION_EE, id = "ea"), @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "ai"),
            @SpecAssertion(section = INJECTED_FIELDS_EE, id = "b") })
    public void testInjectionIntoServletListener() throws Exception {
        WebClient webClient = new WebClient();
        webClient.getPage(contextPath + "Test/ServletListener?test=injection");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "aj"), @SpecAssertion(section = INITIALIZER_METHODS_EE, id = "b") })
    public void testServletListenerInitializerMethodCalled() throws Exception {
        WebClient webClient = new WebClient();
        webClient.getPage(contextPath + "Test/ServletListener?test=initializer");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = INJECTION_EE, id = "eb"), @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "am"),
            @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "an"), @SpecAssertion(section = INJECTED_FIELDS_EE, id = "b") })
    public void testInjectionIntoTagHandler() throws Exception {
        WebClient webClient = new WebClient();
        WebResponse response = webClient.getPage(contextPath + "TagPage.jsp").getWebResponse();
        assert response.getStatusCode() == 200;
        assert response.getContentAsString().contains(TestTagHandler.INJECTION_SUCCESS);
        assert response.getContentAsString().contains(TestTagHandler.INITIALIZER_SUCCESS);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = INJECTION_EE, id = "ec"), @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "as"),
            @SpecAssertion(section = INJECTED_FIELDS_EE, id = "b") })
    public void testInjectionIntoTagLibraryListener() throws Exception {
        WebClient webClient = new WebClient();
        webClient.getPage(contextPath + "Test/TagLibraryListener?test=injection");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "at"), @SpecAssertion(section = INITIALIZER_METHODS_EE, id = "b") })
    public void testTagLibraryListenerInitializerMethodCalled() throws Exception {
        WebClient webClient = new WebClient();
        webClient.getPage(contextPath + "Test/TagLibraryListener?test=initializer");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = INJECTION, id = "d"), @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "au"),
            @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "av"), @SpecAssertion(section = INJECTED_FIELDS_EE, id = "b") })
    public void testInjectionIntoJSFManagedBean() throws Exception {
        WebClient webclient = new WebClient();
        String content = webclient.getPage(contextPath + "ManagedBeanTestPage.jsf").getWebResponse().getContentAsString();
        assert content.contains("Injection works");
        assert content.contains("Initializer works");
    }
}
