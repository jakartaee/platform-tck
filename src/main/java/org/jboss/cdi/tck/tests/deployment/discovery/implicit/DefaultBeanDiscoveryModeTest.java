/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.deployment.discovery.implicit;

import static org.jboss.cdi.tck.cdi.Sections.DEFAULT_BEAN_DISCOVERY;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;

import java.util.Set;

import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.ObserverMethod;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.TestGroups;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@SpecVersion(spec = "cdi", version = "2.0")
@Test(groups = TestGroups.INTEGRATION)
public class DefaultBeanDiscoveryModeTest extends AbstractTest {

    @Inject
    BeanManager beanManager;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
            .withBeansXml(new BeansXml(BeanDiscoveryMode.ANNOTATED))
            .withTestClass(DefaultBeanDiscoveryModeTest.class)
            .withExtension(TestExtension.class)
            .withPackage(DefaultBeanDiscoveryModeTest.class.getPackage()).build();
    }

    @Test
    @SpecAssertion(id = "a", section = DEFAULT_BEAN_DISCOVERY)
    public void beanClassesNotDiscoveredTest() {
        Set<Bean<?>> beans = beanManager.getBeans(NotDiscoveredBean.class);
        assertEquals(beans.size(), 0);
    }

    @Test
    @SpecAssertion(id = "b", section = DEFAULT_BEAN_DISCOVERY)
    public void producerMethodNotDiscovered() {
        assertEquals(TestExtension.processProducerMethodCounter.get(), 0);
    }

    @Test
    @SpecAssertion(id = "c", section = DEFAULT_BEAN_DISCOVERY)
    public void producerFieldNotDiscovered() {
        assertEquals(TestExtension.processProducerFieldCounter.get(), 0);
    }

    @Test
    @SpecAssertion(id = "d", section = DEFAULT_BEAN_DISCOVERY)
    public void disposerMethodNotDiscovered() {
        assertNull(TestExtension.disposerParam);
        assertFalse(NotDiscoveredBean.disposerCalled);
    }

    @Test
    @SpecAssertion(id = "e", section = DEFAULT_BEAN_DISCOVERY)
    public void observerMethodNotDiscovered() {
        Set<ObserverMethod<? super ProducedBean>> observerMethods = beanManager.resolveObserverMethods(new ProducedBean());
        assertEquals(observerMethods.size(), 0);
        assertEquals(TestExtension.processObserverCounter.get(), 0);
    }
}
