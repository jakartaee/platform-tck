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
package org.jboss.cdi.tck.tests.lookup.injection;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.FIELDS_INITIALIZER_METHODS;
import static org.jboss.cdi.tck.cdi.Sections.FIELDS_INITIALIZER_METHODS_EE;
import static org.jboss.cdi.tck.cdi.Sections.INJECTION;
import static org.jboss.cdi.tck.cdi.Sections.MEMBER_LEVEL_INHERITANCE;
import static org.jboss.cdi.tck.cdi.Sections.PERFORMING_TYPESAFE_RESOLUTION;
import static org.jboss.cdi.tck.cdi.Sections.PRIMITIVE_TYPES_AND_NULL_VALUES;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.lookup.injection.ejb.DeluxeHenHouse;
import org.jboss.cdi.tck.tests.lookup.injection.ejb.HenHouse;
import org.jboss.cdi.tck.tests.lookup.injection.ejb.SessionBean;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class InjectionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(InjectionTest.class)
                .withClasses(DeluxeHenHouse.class, HenHouse.class, SessionBean.class)
                .withWebXml(
                        Descriptors.create(WebAppDescriptor.class).createEnvEntry().envEntryName("greeting")
                                .envEntryType("java.lang.String").envEntryValue("Hello").up().createEnvEntry()
                                .envEntryName("game").envEntryType("java.lang.String").envEntryValue("poker").up())
                .build();
    }

    @Test
    @SpecAssertion(section = PRIMITIVE_TYPES_AND_NULL_VALUES, id = "aa")
    public void testInjectionPerformsBoxingIfNecessary() throws Exception {
        assert getBeans(SpiderNest.class).size() == 1;
        SpiderNest spiderNest = getContextualReference(SpiderNest.class);
        assert spiderNest.numberOfSpiders != null;
        assert spiderNest.numberOfSpiders.equals(4);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PERFORMING_TYPESAFE_RESOLUTION, id = "kc"),
            @SpecAssertion(section = INJECTION, id = "b"), @SpecAssertion(section = FIELDS_INITIALIZER_METHODS, id = "ac") })
    public void testInjectionOfNamedBean() {
        WolfPack wolfPack = getContextualReference(WolfPack.class);
        assert wolfPack.getAlphaWolf() != null;
    }

    @Test(groups = INTEGRATION, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "aa"),
            @SpecAssertion(section = FIELDS_INITIALIZER_METHODS, id = "bg"),
            @SpecAssertion(section = FIELDS_INITIALIZER_METHODS, id = "bh"),
            @SpecAssertion(section = FIELDS_INITIALIZER_METHODS, id = "bk"),
            @SpecAssertion(section = FIELDS_INITIALIZER_METHODS, id = "bl"),
            @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "bi"),
            @SpecAssertion(section = FIELDS_INITIALIZER_METHODS_EE, id = "bj") })
    public void testInjectionFieldsAndInitializerMethods(DeluxeHenHouse henHouse) throws Exception {
        assertNotNull(henHouse.fox);
        assertEquals(henHouse.fox.getName(), "gavin");
        assertTrue(henHouse.initializerCalledAfterInjectionPointsInit);
        assertTrue(henHouse.postConstructCalledAfterInitializers);
    }

    @Test
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "ac")
    public void testFieldDeclaredInIndirectSuperclassInjected() throws Exception {
        MegaPoorHenHouse henHouse = getContextualReference(MegaPoorHenHouse.class);
        assert henHouse.fox != null;
        assert henHouse.fox.getName().equals("gavin");
    }

}
