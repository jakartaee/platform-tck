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

package org.jboss.cdi.tck.tests.implementation.simple.resource.ejb;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_RESOURCE;
import static org.jboss.cdi.tck.cdi.Sections.RESOURCE_LIFECYCLE;
import static org.jboss.cdi.tck.cdi.Sections.RESOURCE_TYPES;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * EJB injection tests for simple beans.
 *
 * @author David Allen
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class EjbInjectionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EjbInjectionTest.class).build();
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = DECLARING_RESOURCE, id = "ee"), @SpecAssertion(section = RESOURCE_LIFECYCLE, id = "ld"),
            @SpecAssertion(section = RESOURCE_LIFECYCLE, id = "mg") })
    public void testInjectionOfEjbs() {
        Bean<ManagedBean> managedBean = getBeans(ManagedBean.class).iterator().next();
        CreationalContext<ManagedBean> creationalContext = getCurrentManager().createCreationalContext(managedBean);
        ManagedBean instance = managedBean.create(creationalContext);
        assert instance.getMyEjb() != null : "EJB reference was not produced and injected into bean";
        assert instance.getMyEjb().knockKnock().equals("We're home");
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = RESOURCE_LIFECYCLE, id = "mh") })
    public void testPassivationOfEjbs() throws Exception {
        Bean<ManagedBean> managedBean = getBeans(ManagedBean.class).iterator().next();
        CreationalContext<ManagedBean> creationalContext = getCurrentManager().createCreationalContext(managedBean);
        ManagedBean instance = managedBean.create(creationalContext);
        instance = (ManagedBean) activate(passivate(instance));
        assert instance.getMyEjb() != null : "EJB reference was not produced and injected into bean";
        assert instance.getMyEjb().knockKnock().equals("We're home");
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = RESOURCE_TYPES, id = "ad") })
    public void testResourceBeanTypes() {
        @SuppressWarnings("serial")
        Bean<BeanRemote> beanRemote = getBeans(BeanRemote.class, new AnnotationLiteral<Lazy>() {
        }).iterator().next();
        assert beanRemote.getTypes().size() == 3;
        assert typeSetMatches(beanRemote.getTypes(), BeanRemote.class, Object.class, AnotherInterface.class);
    }

}
