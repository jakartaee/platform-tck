/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.cdi.tck.tests.deployment.packaging.ear;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE_EE;
import static org.testng.Assert.assertNotNull;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.Testable;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.util.SimpleLogger;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.application6.ApplicationDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * This test verifies that multiple CDI-enabled web applications can be bundled inside of an enterprise archive (.ear).
 * <p>
 * This test was originally part of Seam Compatibility project.
 * </p>
 *
 * @author <a href="http://community.jboss.org/people/jharting">Jozef Hartinger</a>
 * @author Martin Kouba
 * @see <a href="http://java.net/jira/browse/GLASSFISH-16303">GLASSFISH-16303</a>
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class MultiWebModuleWithExtensionTest extends AbstractTest {

    @Deployment
    public static EnterpriseArchive createTestArchive() {

        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder()
                .withTestClassDefinition(MultiWebModuleWithExtensionTest.class).withClasses(SimpleLogger.class)
                .noDefaultWebModule().build();
        StringAsset applicationXml = new StringAsset(Descriptors.create(ApplicationDescriptor.class)
                .version(EnterpriseArchiveBuilder.DEFAULT_APP_VERSION).applicationName("Test").createModule()
                .ejb(EnterpriseArchiveBuilder.DEFAULT_EJB_MODULE_NAME).up().createModule().getOrCreateWeb().webUri("foo.war")
                .contextRoot("/foo").up().up().createModule().getOrCreateWeb().webUri("bar.war").contextRoot("/bar").up().up()
                .exportAsString());
        enterpriseArchive.setApplicationXML(applicationXml);

        WebArchive fooArchive = Testable.archiveToTest(new WebArchiveBuilder().notTestArchive().withName("foo.war")
                .withClasses(MultiWebModuleWithExtensionTest.class, FooWebBean.class)
                .withBeanLibrary("foo.jar", FooExtension.class, FooBean.class).withDefaultEjbModuleDependency().build());
        enterpriseArchive.addAsModule(fooArchive);

        WebArchive barArchive = new WebArchiveBuilder().notTestArchive().withName("bar.war").withClasses(BarWebBean.class)
                .withBeanLibrary("bar.jar", BarExtension.class, BarBean.class).withDefaultEjbModuleDependency().build();
        enterpriseArchive.addAsModule(barArchive);

        return enterpriseArchive;
    }

    @Test(groups = JAVAEE_FULL, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE_EE, id = "jc"), @SpecAssertion(section = BEAN_ARCHIVE_EE, id = "je") })
    public void testMultipleWebModulesWithExtension(FooBean fooBean) {
        assertNotNull(fooBean);
        assertNotNull(fooBean.getExtension());
    }

}
