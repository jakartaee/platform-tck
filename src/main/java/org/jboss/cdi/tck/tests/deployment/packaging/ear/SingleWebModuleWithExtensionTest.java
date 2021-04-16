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

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * This test verifies that a CDI extension can be used with a web application bundled inside of an enterprise archive (.ear)
 *
 * Note that we DO NOT include test class in EJB module since we wouldn't be able to inject bean from web module (Java EE
 * classloading requirements)!
 *
 * <p>
 * This test was originally part of Seam Compatibility project.
 * <p>
 *
 * @author <a href="http://community.jboss.org/people/jharting">Jozef Hartinger</a>
 * @author Martin Kouba
 * @see https://issues.jboss.org/browse/JBAS-8683
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class SingleWebModuleWithExtensionTest extends AbstractTest {

    @Deployment
    public static EnterpriseArchive createTestArchive() {

        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder().noDefaultWebModule()
                .withTestClassDefinition(SingleWebModuleWithExtensionTest.class).build();
        // StringAsset applicationXml = new StringAsset(Descriptors.create(ApplicationDescriptor.class).applicationName("Test")
        // .createModule().getOrCreateWeb().webUri("test.war").contextRoot("/test").up().up().exportAsString());
        // enterpriseArchive.setApplicationXML(applicationXml);

        WebArchive webArchive = new WebArchiveBuilder().notTestArchive().withName("test.war")
                .withClasses(SingleWebModuleWithExtensionTest.class, FooWebBean.class)
                .withBeanLibrary("test.jar", FooExtension.class, FooBean.class).withDefaultEjbModuleDependency().build();
        enterpriseArchive.addAsModule(webArchive);
        return enterpriseArchive;
    }

    @Inject
    FooWebBean fooWebBean;

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE_EE, id = "jc"), @SpecAssertion(section = BEAN_ARCHIVE_EE, id = "je") })
    public void testSingleWebModuleWithExtension() {
        fooWebBean.ping();
    }
}
