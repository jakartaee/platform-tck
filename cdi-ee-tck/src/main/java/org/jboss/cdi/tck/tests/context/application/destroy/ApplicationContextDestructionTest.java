/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.context.application.destroy;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.APPLICATION_CONTEXT_EE;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.net.URLEncoder;

import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.api.InSequence;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.util.SimpleLogger;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Test that the application context is destroyed when the application is shut down.
 * 
 * @author Martin Kouba
 */
@RunAsClient
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class ApplicationContextDestructionTest extends AbstractTest {

    private static final String FOO = "foo";

    private static final String BAR = "bar";

    @Deployment(name = FOO, managed = false, testable = false)
    public static WebArchive createFooTestArchive() {
        return new WebArchiveBuilder().notTestArchive().withClasses(Foo.class, FooInitServlet.class, SimpleLogger.class)
                .build();
    }

    @Deployment(name = BAR, managed = false, testable = false)
    public static WebArchive createBarTestArchive() {
        return new WebArchiveBuilder().notTestArchive().withClasses(Bar.class, BarInfoServlet.class, SimpleLogger.class)
                .build();
    }

    @ArquillianResource
    Deployer deployer;

    /**
     * This is not a real test method.
     * 
     * @see #testApplicationContextDestroyed(URL, URL)
     */
    @Test
    @InSequence(1)
    public void deployArchives() {
        // In order to use @ArquillianResource URLs we need to deploy both test archives first
        deployer.deploy(FOO);
        deployer.deploy(BAR);
    }

    /**
     * Note that this test method depends on (must be run after) {@link #deployArchives()}.
     * 
     * @param fooContext
     * @param barContext
     * @throws Exception
     */
    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = APPLICATION_CONTEXT_EE, id = "f")
    public void testApplicationContextDestroyed(@ArquillianResource @OperateOnDeployment(FOO) URL fooContext,
            @ArquillianResource @OperateOnDeployment(BAR) URL barContext) throws Exception {

        // Init foo - set bar archive deployment url
        WebClient webClient = new WebClient();
        webClient.getPage(fooContext + "init?url=" + URLEncoder.encode(barContext.toExternalForm(), "UTF-8"));

        // Undeploy foo
        deployer.undeploy(FOO);

        // Get bar info
        TextPage info = webClient.getPage(barContext + "info?action=get");
        assertEquals(info.getContent(), "true");

        // Undeploy bar
        deployer.undeploy(BAR);
    }

}
