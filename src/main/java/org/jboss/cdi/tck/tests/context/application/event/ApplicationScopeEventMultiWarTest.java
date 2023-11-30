/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.context.application.event;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.APPLICATION_CONTEXT_EE;

import java.io.IOException;
import java.net.URL;

import jakarta.servlet.ServletContext;

import com.gargoylesoftware.htmlunit.TextPage;
import com.gargoylesoftware.htmlunit.WebClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.Testable;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.application6.ApplicationDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * <p>
 * Verifies that an observer is not notified of a non-visible {@link ServletContext}.
 * </p>
 * <p>
 * Note that this test has to run in as-client mode since arquillian cannot work with such archive (doesn't know which WAR to
 * enrich).
 * </p>
 * <p>
 * This test was originally part of Weld test suite.
 * </p>
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = JAVAEE_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class ApplicationScopeEventMultiWarTest extends AbstractTest {

    @ArquillianResource
    URL context;

    private static final String TEST1_ARCHIVE_NAME = "test1";
    private static final String TEST2_ARCHIVE_NAME = "test2";

    @Deployment(testable = false)
    public static EnterpriseArchive createTestArchive() {

        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder()
                .notTestArchive()
                .withClasses(Collector.class, ObserverNames.class, Helper.class).noDefaultWebModule().build();
        StringAsset applicationXml = new StringAsset(Descriptors.create(ApplicationDescriptor.class)
                .version(EnterpriseArchiveBuilder.DEFAULT_APP_VERSION).applicationName("Test").createModule()
                .ejb(EnterpriseArchiveBuilder.DEFAULT_EJB_MODULE_NAME).up().createModule().getOrCreateWeb().webUri(TEST1_ARCHIVE_NAME+".war")
                .contextRoot("/"+TEST1_ARCHIVE_NAME).up().up().createModule().getOrCreateWeb().webUri(TEST2_ARCHIVE_NAME+".war").contextRoot("/"+TEST2_ARCHIVE_NAME).up()
                .up().exportAsString());
        enterpriseArchive.setApplicationXML(applicationXml);

        WebArchive fooArchive = new WebArchiveBuilder().notTestArchive().withName(TEST1_ARCHIVE_NAME+".war")
                .withClasses(Observer2.class, PingServlet.class).withDefaultEjbModuleDependency().build();
        enterpriseArchive.addAsModule(Testable.archiveToTest(fooArchive));

        WebArchive barArchive = new WebArchiveBuilder().notTestArchive().withName(TEST2_ARCHIVE_NAME+".war").withClasses(Observer3.class, PingServlet.class)
                .withDefaultEjbModuleDependency().build();
        enterpriseArchive.addAsModule(barArchive);

        return enterpriseArchive;
    }

    @Test
    @SpecAssertion(section = APPLICATION_CONTEXT_EE, id = "ga")
    public void testInitializedApplicationScopedEventObserved() throws IOException {

        WebClient client = new WebClient();
        TextPage page1 = client.getPage(context + "/" + TEST1_ARCHIVE_NAME + "/ping");
        TextPage page2 = client.getPage(context + "/" + TEST2_ARCHIVE_NAME + "/ping");
        Assert.assertTrue(page1.getContent().toString().contains(ObserverNames.OBSERVER2_NAME));
        Assert.assertTrue(page2.getContent().toString().contains(ObserverNames.OBSERVER3_NAME));

    }
}
