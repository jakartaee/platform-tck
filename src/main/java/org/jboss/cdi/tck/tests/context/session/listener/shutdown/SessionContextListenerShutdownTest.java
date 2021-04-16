/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.context.session.listener.shutdown;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.SESSION_CONTEXT_EE;
import static org.testng.Assert.assertEquals;

import java.net.URL;
import java.net.URLEncoder;

import jakarta.servlet.http.HttpSessionListener;

import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.api.InSequence;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.util.SimpleLogger;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Test the session context is active during {@link HttpSessionListener#sessionDestroyed(jakarta.servlet.http.HttpSessionEvent)}
 * invocation when the application is undeployed.
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class SessionContextListenerShutdownTest extends AbstractTest {

    private static final String ALPHA_DEPLOYMENT_NAME = "alpha";

    private static final String BRAVO_ARCHIVE_NAME = "bravo";

    @Deployment(name = ALPHA_DEPLOYMENT_NAME, managed = false, testable = false)
    public static WebArchive createFooTestArchive() {
        return new WebArchiveBuilder()
                .notTestArchive()
                .withClasses(SessionScopedTestFlagClient.class, InitServlet.class, TestHttpSessionListener.class,
                        SimpleLogger.class)
                .withWebXml(Descriptors.create(WebAppDescriptor.class).createSessionConfig().sessionTimeout(10).up()).build();
    }

    @Deployment(name = BRAVO_ARCHIVE_NAME, managed = false, testable = false)
    public static WebArchive createBarTestArchive() {
        return new WebArchiveBuilder().notTestArchive().withClasses(TestFlag.class, InfoServlet.class, SimpleLogger.class)
                .build();
    }

    @ArquillianResource
    Deployer deployer;

    /**
     * This is not a real test method.
     *
     * @see #testApplicationContextDestroyed(URL, URL)
     */
    @Test(groups = INTEGRATION)
    @InSequence(1)
    public void deployArchives() {
        // In order to use @ArquillianResource URLs we need to deploy both test archives first
        deployer.deploy(ALPHA_DEPLOYMENT_NAME);
        deployer.deploy(BRAVO_ARCHIVE_NAME);
    }

    /**
     * Note that this test method depends on (must be run after) {@link #deployArchives()}.
     *
     * @param alphaContext
     * @param bravoContext
     * @throws Exception
     */
    @Test(groups = INTEGRATION, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = SESSION_CONTEXT_EE, id = "ac")
    public void testApplicationContextDestroyed(@ArquillianResource @OperateOnDeployment(ALPHA_DEPLOYMENT_NAME) URL alphaContext,
            @ArquillianResource @OperateOnDeployment(BRAVO_ARCHIVE_NAME) URL bravoContext) throws Exception {

        // Init SessionScopedTestFlagClient - set bravo archive deployment url
        WebClient webClient = new WebClient();
        webClient.setThrowExceptionOnFailingStatusCode(true);
        webClient.getPage(alphaContext + "init?url=" + URLEncoder.encode(bravoContext.toExternalForm(), "UTF-8"));

        deployer.undeploy(ALPHA_DEPLOYMENT_NAME);

        TextPage info = webClient.getPage(bravoContext + "info?action=get");
        assertEquals(info.getContent(), "true");

        deployer.undeploy(BRAVO_ARCHIVE_NAME);
    }

}
