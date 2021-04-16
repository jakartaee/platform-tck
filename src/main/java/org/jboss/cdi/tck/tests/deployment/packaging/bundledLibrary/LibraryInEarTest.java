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

package org.jboss.cdi.tck.tests.deployment.packaging.bundledLibrary;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE_EE;
import static org.testng.Assert.assertEquals;

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
 * Tests related to the final deployment phase of the lifecycle.
 *
 * Note that we DO NOT include test class in EJB module since we wouldn't be able to inject bean from web module (Java EE
 * classloading requirements)!
 *
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class LibraryInEarTest extends AbstractTest {

    @Deployment
    public static EnterpriseArchive createTestArchive() {

        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder().noDefaultWebModule()
                .withTestClassDefinition(LibraryInEarTest.class).withClasses(Baz.class).withBeanLibrary(Bar.class).build();

        WebArchive webArchive = new WebArchiveBuilder().notTestArchive().withName("test.war")
                .withClasses(LibraryInEarTest.class, Foo.class).withDefaultEjbModuleDependency().build();
        enterpriseArchive.addAsModule(webArchive);

        return enterpriseArchive;
    }

    @Inject
    Baz baz;

    @Inject
    Foo foo;

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE_EE, id = "jb") })
    public void test() {

        assertEquals(getCurrentManager().getBeans(Foo.class).size(), 1);
        assertEquals(getCurrentManager().getBeans(Bar.class).size(), 1);
        assertEquals(getCurrentManager().getBeans(Baz.class).size(), 1);

        // Bean in EJB module can inject bean from EAR library
        assertEquals(baz.ping(), 1);
        // Bean in WAR classes can inject bean from EAR library
        assertEquals(foo.ping(), 1);
    }
}
