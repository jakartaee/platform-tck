/*
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

package org.jboss.cdi.tck.tests.context.passivating.dependency.resource.persistence;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.TestGroups.PERSISTENCE;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_CAPABLE_DEPENDENCY_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class ResourcePassivationDependencyTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(ResourcePassivationDependencyTest.class)
                .withClasses(ResourceProducer.class, Profile.class, Another.class).withDefaultPersistenceXml().build();
    }

    @Test(groups = { PERSISTENCE, INTEGRATION })
    @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY_EE, id = "db")
    @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY_EE, id = "dc")
    public void testPersistenceUnitAndContext() throws IOException, ClassNotFoundException {

        Profile profile = getContextualReference(Profile.class);

        assertNotNull(profile);
        assertNotNull(profile.getEntityManager());
        assertTrue(profile.getEntityManager().isOpen());
        assertNotNull(profile.getEntityManagerFactory());
        assertTrue(profile.getEntityManagerFactory().isOpen());

        String profileId = profile.getId();

        byte[] serializedProfile = passivate(profile);

        Profile profileCopy = (Profile) activate(serializedProfile);

        assertEquals(profileCopy.getId(), profileId);
        assertNotNull(profileCopy.getEntityManager());
        assertTrue(profileCopy.getEntityManager().isOpen());
        assertNotNull(profileCopy.getEntityManagerFactory());
        assertTrue(profileCopy.getEntityManagerFactory().isOpen());
    }

}
