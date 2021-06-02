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
package org.jboss.cdi.tck.tests.lookup.el.integration;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.EL;
import static org.jboss.cdi.tck.cdi.Sections.NAMES_EE;
import static org.jboss.cdi.tck.cdi.Sections.NAME_RESOLUTION;
import static org.jboss.cdi.tck.cdi.Sections.NAME_RESOLUTION_EE;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.WebClient;

@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class IntegrationWithUnifiedELTest extends AbstractTest {

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(IntegrationWithUnifiedELTest.class).withWebXml("web.xml")
                .withWebResource("JSFTestPage.jsp", "JSFTestPage.jsp").withWebResource("JSPTestPage.jsp", "JSPTestPage.jsp")
                .withWebResource("faces-config.xml", "/WEB-INF/faces-config.xml").build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = EL, id = "a"), @SpecAssertion(section = NAME_RESOLUTION, id = "d"),
            @SpecAssertion(section = NAME_RESOLUTION_EE, id = "a"), @SpecAssertion(section = NAMES_EE, id = "a") })
    public void testELResolverRegisteredWithJsf() throws Exception {
        WebClient webclient = new WebClient();
        String content = webclient.getPage(contextPath + "JSFTestPage.jsf").getWebResponse().getContentAsString();
        assert content.contains("Dolly");
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = EL, id = "a"), @SpecAssertion(section = NAME_RESOLUTION, id = "d"),
            @SpecAssertion(section = NAME_RESOLUTION_EE, id = "a"), @SpecAssertion(section = NAMES_EE, id = "a") })
    public void testELResolverRegisteredWithServletContainer() throws Exception {
        WebClient webclient = new WebClient();
        String content = webclient.getPage(contextPath + "JSPTestPage.jsp").getWebResponse().getContentAsString();
        assert content.contains("Dolly");
    }
}
