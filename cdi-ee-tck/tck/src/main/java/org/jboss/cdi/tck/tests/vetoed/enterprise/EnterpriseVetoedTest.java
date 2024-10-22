/*
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
package org.jboss.cdi.tck.tests.vetoed.enterprise;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_ANNOTATED_TYPE;
import static org.jboss.cdi.tck.cdi.Sections.SESSION_BEANS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.vetoed.enterprise.aquarium.Piranha;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of the Weld test suite.
 * <p>
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseVetoedTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(EnterpriseVetoedTest.class)
                .withClasses(ElephantLocal.class, Elephant.class, ModifyingExtension.class, VerifyingExtension.class)
                .withPackage(Piranha.class.getPackage())
                .withExtensions(ModifyingExtension.class, VerifyingExtension.class)
                .withLibrary(Gecko.class).build();
    }

    @Inject
    VerifyingExtension verifyingExtension;

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = SESSION_BEANS, id = "aa"),
            @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "ia") })
    public void testClassLevelVeto() {
        assertFalse(verifyingExtension.getClasses().contains(Elephant.class));
        assertEquals(getCurrentManager().getBeans(Elephant.class, Any.Literal.INSTANCE).size(), 0);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "ie") })
    public void testAnnotatedTypeAddedByExtension() {
        assertFalse(verifyingExtension.getClasses().contains(Gecko.class));
        assertEquals(getCurrentManager().getBeans(Gecko.class, Any.Literal.INSTANCE).size(), 0);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = SESSION_BEANS, id = "aa"),
            @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "ii") })
    public void testPackageLevelVeto() {
        assertFalse(verifyingExtension.getClasses().contains(Piranha.class));
        assertEquals(getCurrentManager().getBeans(Piranha.class, Any.Literal.INSTANCE).size(), 0);
    }
}
