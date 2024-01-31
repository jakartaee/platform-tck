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
package org.jboss.cdi.tck.tests.lookup.clientProxy.incontainer;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.CLIENT_PROXIES;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.WebClient;

@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class ClientProxyTest extends AbstractTest {

    @ArquillianResource
    private URL contextPath;

    @Deployment(testable = false)
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ClientProxyTest.class).build();
    }

    @Test
    @SpecAssertion(section = CLIENT_PROXIES, id = "d")
    public void testInvocationIsProcessedOnCurrentInstance() throws Exception {
        WebClient webClient = new WebClient();
        String response;
        response = webClient.getPage(contextPath + "Test/Garage?make=Honda").getWebResponse().getContentAsString();
        assert response.contains("Honda");
        response = webClient.getPage(contextPath + "Test/Garage?make=Toyota").getWebResponse().getContentAsString();
        assert response.contains("Toyota");
    }
}
