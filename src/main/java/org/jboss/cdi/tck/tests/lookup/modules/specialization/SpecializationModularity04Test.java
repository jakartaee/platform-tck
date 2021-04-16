/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.lookup.modules.specialization;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.DIRECT_AND_INDIRECT_SPECIALIZATION;
import static org.jboss.cdi.tck.cdi.Sections.SELECTION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * EAR deployment:
 * <ul>
 * <li>1 WAR</li>
 * <li>2 shared libraries (can see each other)</li>
 * </ul>
 * 
 * Shared library 1 defines {@link Handler}. Shared library 2 defines {@link UppercaseHandler} which specializes {@link Handler}
 * . Bean {@link Alpha} packaged in WEB-INF/classes has an injection point of type {@link Handler}. Bean {@link Bravo} packaged
 * in the shared library 1 has an injection point of type {@link Handler}.
 * 
 * Expected result: {@link UppercaseHandler} injected into {@link Alpha} and {@link Bravo}
 * 
 * @author Martin Kouba
 * 
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class SpecializationModularity04Test extends AbstractTest {

    @Deployment
    public static EnterpriseArchive createTestArchive() {

        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder().noDefaultWebModule()
                .withTestClassDefinition(SpecializationModularity04Test.class).withBeanLibrary(Handler.class, Bravo.class)
                .withBeanLibrary(UppercaseHandler.class).withLibrary(Connector.class).build();

        enterpriseArchive.addAsModule(new WebArchiveBuilder().notTestArchive().withDefaultEjbModuleDependency()
                .withClasses(SpecializationModularity04Test.class, Alpha.class).build());

        return enterpriseArchive;
    }

    @Inject
    Alpha alpha;

    @Inject
    Bravo bravo;

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = SELECTION, id = "aa"), @SpecAssertion(section = DIRECT_AND_INDIRECT_SPECIALIZATION, id = "ia") })
    public void testSpecialization() {
        assertNotNull(alpha);
        assertEquals(alpha.hello(), "HELLO");
        assertNotNull(bravo);
        assertEquals(bravo.hello(), "HELLO");
    }

}
