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
package org.jboss.cdi.tck.tests.decorators.definition.lifecycle;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.DECORATORS;
import static org.testng.Assert.assertTrue;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.net.URL;

/**
 * 
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class DecoratorInstanceIsDependentObjectTest extends AbstractTest {

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(DecoratorInstanceIsDependentObjectTest.class)
                .withBeansXml(
                        new BeansXml().decorators(ChargeDecorator.class)).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECORATORS, id = "c") })
    public void testDecoratorInstanceIsDependentObject() throws Exception {

        WebClient webClient = new WebClient();

        TextPage resp01 = webClient.getPage(contextPath + "bank?action=deposit&amount=10");
        assertTrue(resp01.getContent().contains("ShortTermBalance:10"));
        assertTrue(resp01.getContent().contains("DurableBalance:10"));
        assertTrue(resp01.getContent().contains("PostConstructCallers:2"));
        // Note that short-term account is request scoped and thus destroyed after request - however this info is not available
        // during the request - we have to check after the next request
        assertTrue(resp01.getContent().contains("PreDestroyCallers:0"));

        TextPage resp02 = webClient.getPage(contextPath + "bank?action=deposit&amount=10");
        assertTrue(resp02.getContent().contains("ShortTermBalance:10"));
        assertTrue(resp02.getContent().contains("DurableBalance:20"));
        assertTrue(resp02.getContent().contains("PostConstructCallers:3"));
        assertTrue(resp02.getContent().contains("PreDestroyCallers:1"));

        // And finally check decorator is applied
        TextPage resp03 = webClient.getPage(contextPath + "bank?action=withdraw&amount=5");
        assertTrue(resp03.getContent().contains("ShortTermBalance:-10"));
        assertTrue(resp03.getContent().contains("DurableBalance:10"));
        assertTrue(resp03.getContent().contains("PostConstructCallers:4"));
        assertTrue(resp03.getContent().contains("PreDestroyCallers:2"));
    }

}
