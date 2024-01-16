/*
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
package org.jboss.cdi.tck.tests.deployment.packaging.visibility;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.INIT_EVENTS;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Tests whether an extension that observes annotated types, when put in a library with a higher order name (alpha.jar), is
 * notified of annotated types in a library with a lower order name (bravo.jar).
 * 
 * <p>
 * This test was originally part of Seam Compatibility project.
 * <p>
 * 
 * @author <a href="http://community.jboss.org/people/dan.j.allen">Dan Allen</a>
 * @author Martin Kouba
 * @see <a href="http://java.net/jira/browse/GLASSFISH-15735">GLASSFISH-15735</a>
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class VisibilityOfAnnotatedTypesFromExtensionInAlphaBeanArchiveTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(VisibilityOfAnnotatedTypesFromExtensionInAlphaBeanArchiveTest.class)
                .withBeanLibrary("bravo.jar", Soda.class)
                .withBeanLibrary("alpha.jar", Beer.class, AnnotatedTypeObserverExtension.class).build();
    }

    @Inject
    AnnotatedTypeObserverExtension observer;

    @Test
    @SpecAssertion(section = INIT_EVENTS, id = "b")
    public void shouldObserveExpectedAnnotatedTypes() {
        Assert.assertTrue(observer.observed(Beer.class));
        Assert.assertTrue(observer.observed(Soda.class));
    }
}
