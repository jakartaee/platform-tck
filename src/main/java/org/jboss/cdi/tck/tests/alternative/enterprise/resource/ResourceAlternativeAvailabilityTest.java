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
package org.jboss.cdi.tck.tests.alternative.enterprise.resource;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.TestGroups.PERSISTENCE;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class ResourceAlternativeAvailabilityTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(ResourceAlternativeAvailabilityTest.class)
                .withBeansXml(new BeansXml().alternatives(EnabledResourceProducer.class))
                .withDefaultPersistenceXml().build();
    }

    @Inject
    @DatabaseTest
    private EntityManager entityManager;

    @SuppressWarnings("serial")
    @Test(groups = PERSISTENCE)
    @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE, id = "cc")
    public void testResourceAvailability() {

        AnnotationLiteral<DatabaseTest> testLiteral = new AnnotationLiteral<DatabaseTest>() {
        };
        AnnotationLiteral<DatabaseProduction> productionLiteral = new AnnotationLiteral<DatabaseProduction>() {
        };
        Set<Bean<EntityManager>> beans = getBeans(EntityManager.class, testLiteral);
        assertEquals(beans.size(), 1);
        beans = getBeans(EntityManager.class, productionLiteral);
        assertEquals(beans.size(), 0);

        assertNotNull(entityManager);
        assertNotNull(entityManager.getDelegate());
    }

}
