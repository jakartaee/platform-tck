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
package org.jboss.cdi.tck.tests.implementation.simple.resource.persistenceContext;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.TestGroups.PERSISTENCE;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_RESOURCE;
import static org.jboss.cdi.tck.cdi.Sections.RESOURCE_LIFECYCLE;
import static org.jboss.cdi.tck.cdi.Sections.RESOURCE_TYPES;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Injection of persistence related objects.
 *
 * @author David Allen
 * @author Martin Kouba
 */
@Test(groups = { INTEGRATION, PERSISTENCE })
@SpecVersion(spec = "cdi", version = "2.0")
public class PersistenceContextInjectionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(PersistenceContextInjectionTest.class).withBeansXml("beans.xml")
                .withDefaultPersistenceXml().build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_RESOURCE, id = "cc"), @SpecAssertion(section = RESOURCE_LIFECYCLE, id = "lb"),
            @SpecAssertion(section = RESOURCE_LIFECYCLE, id = "mc") })
    public void testInjectionOfPersistenceContext() {
        ServiceBean serviceBean = getContextualReference(ServiceBean.class);
        ManagedBean managedBean = serviceBean.getManagedBean();
        assert managedBean.getPersistenceContext() != null : "Persistence context was not injected into bean";
        assert serviceBean.validateEntityManager();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_RESOURCE, id = "dd"), @SpecAssertion(section = RESOURCE_LIFECYCLE, id = "lc"),
            @SpecAssertion(section = RESOURCE_LIFECYCLE, id = "me") })
    public void testInjectionOfPersistenceUnit() {
        Bean<ManagedBean> managedBeanBean = getBeans(ManagedBean.class).iterator().next();
        CreationalContext<ManagedBean> managedBeanCc = getCurrentManager().createCreationalContext(managedBeanBean);
        ManagedBean managedBean = managedBeanBean.create(managedBeanCc);
        assert managedBean.getPersistenceUnit() != null : "Persistence unit was not injected into bean";
        assert managedBean.getPersistenceUnit().isOpen() : "Persistence unit not open injected into bean";
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = RESOURCE_LIFECYCLE, id = "md") })
    public void testPassivationOfPersistenceContext() throws Exception {
        Bean<ManagedBean> managedBeanBean = getBeans(ManagedBean.class).iterator().next();
        CreationalContext<ManagedBean> managedBeanCc = getCurrentManager().createCreationalContext(managedBeanBean);
        ManagedBean managedBean = managedBeanBean.create(managedBeanCc);
        managedBean = (ManagedBean) activate(passivate(managedBean));
        assert managedBean.getPersistenceContext() != null : "Persistence context was not injected into bean";
        assert managedBean.getPersistenceContext().getDelegate() != null : "Persistence context not deserialized correctly";
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = RESOURCE_LIFECYCLE, id = "lc"), @SpecAssertion(section = RESOURCE_LIFECYCLE, id = "mf") })
    public void testPassivationOfPersistenceUnit() throws Exception {
        Bean<ManagedBean> managedBeanBean = getBeans(ManagedBean.class).iterator().next();
        CreationalContext<ManagedBean> managedBeanCc = getCurrentManager().createCreationalContext(managedBeanBean);
        ManagedBean managedBean = managedBeanBean.create(managedBeanCc);
        managedBean = (ManagedBean) activate(passivate(managedBean));
        assert managedBean.getPersistenceUnit() != null : "Persistence unit was not injected into bean";
        assert managedBean.getPersistenceUnit().isOpen() : "Persistence unit not open injected into bean";
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_RESOURCE, id = "hh"), @SpecAssertion(section = RESOURCE_TYPES, id = "ab") })
    public void testBeanTypesAndBindingTypesOfPersistenceContext() {
        Bean<EntityManager> manager = getBeans(EntityManager.class, new Database.Literal()).iterator().next();
        assert manager.getTypes().size() == 3;
        assert typeSetMatches(manager.getTypes(), EntityManager.class, Object.class, AutoCloseable.class);
        assert manager.getQualifiers().size() == 2;
        assert annotationSetMatches(manager.getQualifiers(), Any.class, Database.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = RESOURCE_TYPES, id = "ac") })
    public void testBeanTypesOfPersistenceUnit() {
        Bean<EntityManagerFactory> factory = getBeans(EntityManagerFactory.class, new Database.Literal()).iterator().next();
        assert factory.getTypes().size() == 3;
        assert typeSetMatches(factory.getTypes(), EntityManagerFactory.class, Object.class, AutoCloseable.class);
    }
}
