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

package org.jboss.cdi.tck.tests.implementation.builtin.broken.transaction;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.ADDITIONAL_BUILTIN_BEANS;

import jakarta.enterprise.inject.spi.DefinitionException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test Java EE component class that has an injection point of type UserTransaction and qualifier Default, and may not validly
 * make use of the JTA UserTransaction according to the specification.
 * 
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class UserTransactionInvalidInjectionTest extends AbstractTest {

    @ShouldThrowException(DefinitionException.class)
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(UserTransactionInvalidInjectionTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ADDITIONAL_BUILTIN_BEANS, id = "e") })
    public void testUserTransactionInjection() throws Exception {
    }

}
