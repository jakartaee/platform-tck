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
package org.jboss.cdi.tck.tests.extensions.registration;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE;
import static org.jboss.cdi.tck.cdi.Sections.BEFORE_BEAN_DISCOVERY;
import static org.jboss.cdi.tck.cdi.Sections.INIT_EVENTS;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Validates that a bean can be registered by an extension that resides in a non-bean archive.
 *
 * <p>
 * This test was originally part of Seam Compatibility project.
 * <p>
 *
 * @author <a href="http://community.jboss.org/people/LightGuard">Jason Porter</a>
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 * @author Martin Kouba
 * @see <a href="http://java.net/jira/browse/GLASSFISH-14808">GLASSFISH-14808</a> (resolved)
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = INTEGRATION)
public class BeanRegistrationByExtensionInNonBeanArchiveTest extends AbstractTest {

    @Inject
    BeanClassToRegister bean;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(BeanRegistrationByExtensionInNonBeanArchiveTest.class)
                .withLibrary(BeanClassToRegister.class, ManualBeanRegistrationExtension.class).build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = INIT_EVENTS, id = "b"), @SpecAssertion(section = INIT_EVENTS, id = "bb"),
            @SpecAssertion(section = BEFORE_BEAN_DISCOVERY, id = "af"), @SpecAssertion(section = BEAN_ARCHIVE, id = "n") })
    public void shouldFindBeanReference() {
        Assert.assertNotNull(bean);
        Assert.assertTrue(bean.isInvokable());
    }
}
