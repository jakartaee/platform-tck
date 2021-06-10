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
package org.jboss.cdi.tck.tests.implementation.simple.resource.resource;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_RESOURCE;
import static org.jboss.cdi.tck.cdi.Sections.RESOURCE_LIFECYCLE;
import static org.jboss.cdi.tck.cdi.Sections.RESOURCE_TYPES;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanContainer;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class InjectionOfResourceTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(InjectionOfResourceTest.class).withBeansXml("beans.xml").build();
    }

    @Test
    @SpecAssertion(section = DECLARING_RESOURCE, id = "bb")
    public void testInjectionOfResource() {
        Bean<ManagedBean> managedBeanBean = getBeans(ManagedBean.class).iterator().next();
        CreationalContext<ManagedBean> managedBeanCc = getCurrentManager().createCreationalContext(managedBeanBean);
        ManagedBean managedBean = managedBeanBean.create(managedBeanCc);
        assert managedBean.getBeanManager() != null : "@Another Manager not found";
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = RESOURCE_LIFECYCLE, id = "la"), @SpecAssertion(section = RESOURCE_LIFECYCLE, id = "ma"),
            @SpecAssertion(section = RESOURCE_LIFECYCLE, id = "o") })
    public void testProduceResourceProxy() {
        Bean<BeanManager> beanManagerBean = getBeans(BeanManager.class, new AnnotationLiteral<Another>() {
        }).iterator().next();
        CreationalContext<BeanManager> beanManagerCc = getCurrentManager().createCreationalContext(beanManagerBean);
        BeanManager beanManager = beanManagerBean.create(beanManagerCc);
        assert beanManager != null;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = RESOURCE_LIFECYCLE, id = "mb") })
    public void testPassivatingResource() throws Exception {
        Bean<ManagedBean> managedBeanBean = getBeans(ManagedBean.class).iterator().next();
        CreationalContext<ManagedBean> managedBeanCc = getCurrentManager().createCreationalContext(managedBeanBean);
        ManagedBean managedBean = managedBeanBean.create(managedBeanCc);
        managedBean = (ManagedBean) activate(passivate(managedBean));
        assert managedBean.getBeanManager() != null;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = RESOURCE_TYPES, id = "aa") })
    public void testResourceBeanTypes() {
        Bean<BeanManager> beanRemote = getBeans(BeanManager.class, new AnnotationLiteral<Another>() {
        }).iterator().next();
        assert beanRemote.getTypes().size() == 3;
        assert beanRemote.getTypes().contains(BeanManager.class);
        assert beanRemote.getTypes().contains(BeanContainer.class);
        assert beanRemote.getTypes().contains(Object.class);
    }
}
