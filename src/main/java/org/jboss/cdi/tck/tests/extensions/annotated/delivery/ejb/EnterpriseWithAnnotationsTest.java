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

package org.jboss.cdi.tck.tests.extensions.annotated.delivery.ejb;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_ANNOTATED_TYPE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.full.extensions.annotated.delivery.Baby;
import org.jboss.cdi.tck.tests.full.extensions.annotated.delivery.Chicken;
import org.jboss.cdi.tck.tests.full.extensions.annotated.delivery.Desired;
import org.jboss.cdi.tck.tests.full.extensions.annotated.delivery.MetaAnnotation;
import org.jboss.cdi.tck.tests.full.extensions.annotated.delivery.ProcessAnnotatedTypeObserver;
import org.jboss.cdi.tck.tests.full.extensions.annotated.delivery.Wanted;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 * 
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseWithAnnotationsTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(EnterpriseWithAnnotationsTest.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .withClasses(Baby.class, Desired.class, ProcessAnnotatedTypeObserver.class, Hawk.class, Wanted.class,
                             Chicken.class, MetaAnnotation.class).withExtensions(ProcessAnnotatedTypeObserver.class).build();
    }

    @Inject
    ProcessAnnotatedTypeObserver processAnnotatedTypeObserver;

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "fc") })
    public void testDelivery() {

        List<Class<?>> processedDesiredAnWantedTypes = processAnnotatedTypeObserver.getProcessedDesiredAndWantedTypes();
        assertFalse(processedDesiredAnWantedTypes.isEmpty());
        assertEquals(processedDesiredAnWantedTypes.size(), 1);
        assertTrue(processedDesiredAnWantedTypes.contains(Hawk.class));

        List<Class<?>> processedDesiredTypes = processAnnotatedTypeObserver.getProcessedDesiredTypes();
        assertFalse(processedDesiredTypes.isEmpty());
        assertEquals(processedDesiredTypes.size(), 1);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "fc"), @SpecAssertion(section = PROCESS_ANNOTATED_TYPE, id = "g") })
    public void testDeliveryMetaAnnotation() {
        List<Class<?>> processedTypes = processAnnotatedTypeObserver.getProcessedMetaAnnotationTypes();
        assertFalse(processedTypes.isEmpty());
        assertEquals(processedTypes.size(), 2);
        assertTrue(processedTypes.contains(Hawk.class));
        assertTrue(processedTypes.contains(Chicken.class));
    }

}
