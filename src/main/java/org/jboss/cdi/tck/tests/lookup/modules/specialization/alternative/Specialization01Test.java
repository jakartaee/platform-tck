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
package org.jboss.cdi.tck.tests.lookup.modules.specialization.alternative;

import static org.jboss.cdi.tck.cdi.Sections.DECLARING_ALTERNATIVE;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVES;
import static org.jboss.cdi.tck.cdi.Sections.SPECIALIZE_MANAGED_BEAN;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.event.Event;
import jakarta.enterprise.inject.Alternative;
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
 * Originated from weld test suite. Test for specializing {@link Alternative}. Verifies that a bean is not specialized unless the specializing alternative is
 * enabled.
 * 
 * @author Jozef Hartinger
 * @author Matej Briskar
 * 
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class Specialization01Test extends AbstractTest {

    public static String TEST_JAR = "test.jar";

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(Specialization01Test.class).withBeanLibrary(InjectedBean1.class)
                .withBeanLibrary(Factory.class, AlternativeSpecializedFactory.class, Product.class, InjectedBean2.class, FactoryEvent.class).build();
    }

    @Inject
    InjectedBean1 bean1;

    @Inject
    InjectedBean2 bean2;

    @Inject
    private Event<FactoryEvent> event;

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_ALTERNATIVE, id = "aa"), @SpecAssertion(section = SPECIALIZE_MANAGED_BEAN, id = "ac") })
    public void testNotEnabledAlternativeDoesNotSpecialize() {
        assertFalse(bean1.getFactory() instanceof AlternativeSpecializedFactory);
        assertFalse(bean1.getProduct().isUnsatisfied());
        assertFalse(bean1.getProduct().isAmbiguous());
        assertNotNull(bean1.getProduct().get());

        assertFalse(bean2.getFactory() instanceof AlternativeSpecializedFactory);
        assertFalse(bean2.getProduct().isUnsatisfied());
        assertFalse(bean2.getProduct().isAmbiguous());
        assertNotNull(bean2.getProduct().get());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVES, id = "a") })
    public void testEvent() {
        Factory.reset();
        event.fire(new FactoryEvent());
        assertTrue(Factory.isEventDelivered());
    }
}
