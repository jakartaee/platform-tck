/*
 * JBoss, Home of Professional Open Source
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

package org.jboss.cdi.tck.tests.deployment.shutdown;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.APPLICATION_CONTEXT_EE;
import static org.jboss.cdi.tck.cdi.Sections.BEFORE_SHUTDOWN;
import static org.jboss.cdi.tck.cdi.Sections.REQUEST_CONTEXT_EE;
import static org.jboss.cdi.tck.cdi.Sections.SHUTDOWN;
import static org.testng.Assert.assertTrue;

import java.net.URL;
import java.net.URLEncoder;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.spi.BeforeShutdown;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.api.InSequence;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.cdi.tck.util.SimpleLogger;
import org.jboss.cdi.tck.util.TransformationUtils;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test application shutdown lifecycle.
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = INTEGRATION)
public class ApplicationShutdownLifecycleTest extends AbstractTest {

    private static final String FOO = "foo";

    private static final String INFO = "info";

    @Deployment(name = FOO, managed = false, testable = false)
    public static WebArchive createFooTestArchive() {
        return new WebArchiveBuilder()
                .notTestArchive()
                .withClasses(ContextDestructionObserver.class, LifecycleMonitoringExtension.class, SimpleLogger.class,
                        InitServlet.class, InfoClient.class, Foo.class, Bar.class, Baz.class, Qux.class)
                .withExtension(LifecycleMonitoringExtension.class).build();
    }

    @Deployment(name = INFO, managed = false, testable = false)
    public static WebArchive createBarTestArchive() {
        return new WebArchiveBuilder().notTestArchive()
                .withClasses(InfoServlet.class, ActionSequence.class, TransformationUtils.class, TransformationUtils.Function.class).build();
    }

    @ArquillianResource
    Deployer deployer;

    /**
     * This is not a real test method.
     *
     * @see #testShutdown(URL, URL)
     */
    @Test
    @InSequence(1)
    public void deployArchives() {
        // In order to use @ArquillianResource URLs we need to deploy both test archives first
        deployer.deploy(FOO);
        deployer.deploy(INFO);
    }

    /**
     * Note that this test method depends on (must be run after)
     *
     * @throws Exception
     */
    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = SHUTDOWN, id = "a"), @SpecAssertion(section = SHUTDOWN, id = "b"),
            @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "ja"), @SpecAssertion(section = APPLICATION_CONTEXT_EE, id = "ga"),
            @SpecAssertion(section = BEFORE_SHUTDOWN, id = "a") })
    public void testShutdown(@ArquillianResource @OperateOnDeployment(FOO) URL fooContext,
            @ArquillianResource @OperateOnDeployment(INFO) URL infoContext) throws Exception {

        // Init foo - set info archive deployment url
        WebClient webClient = new WebClient();
        webClient.getPage(fooContext + "init?url=" + URLEncoder.encode(infoContext.toExternalForm(), "UTF-8"));

        // Undeploy foo
        deployer.undeploy(FOO);

        // 1. Destroy contexts (the order is not set)
        // 2. BeforeShutdown event
        TextPage info = webClient.getPage(infoContext + "info?action=get");
        ActionSequence actual = ActionSequence.buildFromCsvData(info.getContent());
        assertTrue(actual.endsWith(BeforeShutdown.class.getName()));
        actual.assertDataContainsAll(RequestScoped.class.getName(), SessionScoped.class.getName(),
                ApplicationScoped.class.getName(),
                ConversationScoped.class.getName(), Foo.class.getName(), Bar.class.getName(), Baz.class.getName(), Qux.class.getName());

        // Undeploy info
        deployer.undeploy(INFO);
    }

}
