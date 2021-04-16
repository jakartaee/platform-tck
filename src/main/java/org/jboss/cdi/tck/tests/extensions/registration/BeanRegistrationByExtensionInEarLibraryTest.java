/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.cdi.tck.tests.extensions.registration;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

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
 * @author Tomas Remes
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class BeanRegistrationByExtensionInEarLibraryTest extends AbstractTest {

    @Inject
    BeanClassToRegister bean;

    @Deployment
    public static EnterpriseArchive createTestArchive() {
        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder()
                .withTestClass(BeanRegistrationByExtensionInEarLibraryTest.class)
                .withClasses(DummyObserverExtension.class, EarExtensionsCheck.class).withExtension(DummyObserverExtension.class)
                .withLibrary(BeanClassToRegister.class, ManualBeanRegistrationExtension.class).noDefaultWebModule().build();

        WebArchive webArchive = new WebArchiveBuilder().withExtension(WarDummyObserverExtension.class).notTestArchive()
                .withClasses(WarDummyObserverExtension.class)
                .withDefaultEjbModuleDependency()
                .build();
        enterpriseArchive.addAsModule(webArchive);

        return enterpriseArchive;
    }

    @Test(groups = { INTEGRATION, JAVAEE_FULL })
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "n") })
    public void shouldFindBeanReference() {
        assertNotNull(bean);
    }

    @Test(groups = { INTEGRATION, JAVAEE_FULL })
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "n") })
    public void shouldRegisterExtensions() {
        assertTrue(EarExtensionsCheck.extensionInEjbJarRegistered);
        assertTrue(EarExtensionsCheck.extensionInWarRegistered);
    }

}
