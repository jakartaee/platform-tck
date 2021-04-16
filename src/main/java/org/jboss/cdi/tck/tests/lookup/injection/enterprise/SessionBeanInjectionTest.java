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
package org.jboss.cdi.tck.tests.lookup.injection.enterprise;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.FIELDS_INITIALIZER_METHODS_EE;
import static org.jboss.cdi.tck.cdi.Sections.INJECTION_EE;
import static org.jboss.cdi.tck.cdi.Sections.MEMBER_LEVEL_INHERITANCE_EE;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class SessionBeanInjectionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(SessionBeanInjectionTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTION_EE, id = "a"), @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "aa"),
            @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "ab"), @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "ba"),
            @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "be") })
    public void testInjectionOnContextualSessionBean() {
        assert getContextualReference(FarmLocal.class).isInjectionPerformedCorrectly();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTION_EE, id = "c"), @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "ak"),
            @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "al") })
    public void testInjectionOnNonContextualSessionBean() {
        assert getContextualReference(InjectedSessionBeanLocal.class).getFarm().isInjectionPerformedCorrectly();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INJECTION_EE, id = "ed"), @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "ao"),
            @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "ap") })
    public void testInjectionOnEJBInterceptor() {
        // Test interceptor that intercepts contextual Session Bean
        assert getContextualReference(FarmLocal.class).getAnimalCount() == 2;
        // Test interceptor that intercepts non-contextual Session Bean
        assert getContextualReference(InjectedSessionBeanLocal.class).getFarm().getAnimalCount() == 2;
    }

    @Test
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE_EE, id = "ab")
    public void testFieldDeclaredInSuperclassInjected() {
        DeluxeHenHouseLocal henHouse = getContextualReference(DeluxeHenHouseLocal.class);
        assert henHouse.getFox() != null;
        assert henHouse.getFox().getName().equals("gavin");
    }

    @Test
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE_EE, id = "ad")
    public void testFieldDeclaredInSuperclassIndirectlyInjected() {
        MegaPoorHenHouseLocal henHouse = getContextualReference(MegaPoorHenHouseLocal.class);
        assert henHouse.getFox() != null;
        assert henHouse.getFox().getName().equals("gavin");
    }

}
