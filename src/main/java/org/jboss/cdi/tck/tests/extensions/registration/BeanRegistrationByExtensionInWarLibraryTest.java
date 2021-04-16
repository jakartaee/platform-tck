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
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE;
import static org.testng.Assert.assertNotNull;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 
 * @author Tomas Remes
 * 
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class BeanRegistrationByExtensionInWarLibraryTest extends AbstractTest {

    @Inject
    BeanClassToRegister bean;

    @Inject
    AnotherBeanClassToRegister anotherBean;

    @Deployment
    public static WebArchive createTestArchive() {
        WebArchive webArchive = new WebArchiveBuilder()
                .withTestClass(BeanRegistrationByExtensionInWarLibraryTest.class)
                .withLibrary(BeanClassToRegister.class, ManualBeanRegistrationExtension.class,
                        AnotherBeanClassToRegister.class, AnotherManualBeanRegistrationExtension.class).build();

        return webArchive;
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE, id = "n") })
    public void shouldFindBeanReference() {
        assertNotNull(bean);
        assertNotNull(anotherBean);
    }

}
