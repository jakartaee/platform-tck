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
package org.jboss.cdi.tck.tests.deployment.discovery.enterprise.annotated;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.DEFAULT_BEAN_DISCOVERY_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.Set;

import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.ObserverMethod;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeansXmlVersion;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@Test(groups = { JAVAEE_FULL, INTEGRATION })
@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseDefaultBeanDiscoveryModeTest extends AbstractTest {

    @Inject
    TestExtension extension;

    @Inject
    BeanManager beanManager;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EnterpriseDefaultBeanDiscoveryModeTest.class)
                .withBeansXml(new BeansXml()
                    .setBeansXmlVersion(BeansXmlVersion.v11)
                    .setBeanDiscoveryMode(BeanDiscoveryMode.ANNOTATED)
                    )
                .withExtension(TestExtension.class).build();
    }

    @Test
    @SpecAssertion(section = DEFAULT_BEAN_DISCOVERY_EE, id = "b")
    public void testProducerMethodDiscovered() {
        assertNotNull(extension.getProducerMethod());
    }

    @Test
    @SpecAssertion(section = DEFAULT_BEAN_DISCOVERY_EE, id = "c")
    public void testProducerFieldDiscovered() {
        assertNotNull(extension.getProducerField());
    }

    @Test
    @SpecAssertion(section = DEFAULT_BEAN_DISCOVERY_EE, id = "d")
    public void testDisposerMethodDiscovered() {
        assertNotNull(extension.getDisposesParam());
    }

    @Test
    @SpecAssertion(section = DEFAULT_BEAN_DISCOVERY_EE, id = "e")
    public void testObserverMethodDiscovered() {
        Set<ObserverMethod<? super Apple>> observers = beanManager.resolveObserverMethods(new Apple());
        assertEquals(observers.size(), 1);
        assertEquals(observers.iterator().next().getBeanClass(), AppleTree.class);
    }

}
