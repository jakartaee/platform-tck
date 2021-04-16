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

import static org.jboss.cdi.tck.cdi.Sections.DIRECT_AND_INDIRECT_SPECIALIZATION;
import static org.jboss.cdi.tck.cdi.Sections.SELECTION;
import static org.testng.Assert.assertEquals;
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
 * Simple WAR with multiple libraries.
 * 
 * Library 1 defines bean {@link Handler}. Library 2 defines bean {@link UppercaseHandler} which specializes {@link Handler}.
 * Bean {@link Alpha} packaged in WEB-INF/classes has an injection point of type {@link Handler}. Bean {@link Bravo} packaged in
 * library 1 has an injection point of type {@link Handler}. Bean {@link Charlie} packaged in library 1 has an injection point
 * of type {@link Handler}.
 * 
 * 
 * Expected result: {@link UppercaseHandler} is injected into beans {@link Alpha}, {@link Bravo} and {@link Charlie}
 * 
 * @author Martin Kouba
 * 
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class SpecializationModularity01Test extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(SpecializationModularity01Test.class).withLibrary(Connector.class)
                .withBeanLibrary(Handler.class, Charlie.class).withBeanLibrary(UppercaseHandler.class, Bravo.class)
                .withClasses(Alpha.class).build();
    }

    @Inject
    Alpha alpha;

    @Inject
    Bravo bravo;

    @Inject
    Charlie charlie;

    @Test
    @SpecAssertions({ @SpecAssertion(section = SELECTION, id = "aa"), @SpecAssertion(section = DIRECT_AND_INDIRECT_SPECIALIZATION, id = "ia") })
    public void testSpecialization() {
        assertNotNull(alpha);
        assertEquals(alpha.hello(), "HELLO");
        assertNotNull(bravo);
        assertEquals(bravo.hello(), "HELLO");
        assertNotNull(charlie);
        assertEquals(charlie.hello(), "HELLO");
    }

}
