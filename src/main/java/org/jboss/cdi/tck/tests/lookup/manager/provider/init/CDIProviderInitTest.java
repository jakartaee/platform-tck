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
package org.jboss.cdi.tck.tests.lookup.manager.provider.init;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE;
import static org.jboss.cdi.tck.cdi.Sections.PROVIDER;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.full.extensions.alternative.deployment.Bar;
import org.jboss.cdi.tck.tests.full.extensions.alternative.deployment.Foo;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test {@link CDI} after the container fires the BeforeBeanDiscovery container lifecycle event until the application
 * initialization is completed.
 *
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class CDIProviderInitTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(CDIProviderInitTest.class)
                .withClasses(Alpha.class, MarkerObtainerWar.class, Foo.class, Marker.class,
                        AfterDeploymentValidationObserver.class)
                .withExtension(AfterDeploymentValidationObserver.class)
                .withBeansXml(new BeansXml().alternatives(Alpha.class))
                .withBeanLibrary(
                        new BeansXml().alternatives(Bravo.class),
                        Bravo.class, MarkerObtainerBda1.class, Bar.class)
                .withBeanLibrary(
                        new BeansXml().alternatives(Charlie.class),
                        Charlie.class, MarkerObtainerBda2.class, Baz.class)
                .withLibrary(MarkerObtainerNonBda.class, NonBdaAfterDeploymentValidationObserver.class).build();
    }

    @Inject
    AfterDeploymentValidationObserver bdaExtension;

    @Inject
    NonBdaAfterDeploymentValidationObserver nonbdaExtension;

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PROVIDER, id = "aa"), @SpecAssertion(section = BEAN_ARCHIVE, id = "n") })
    public void testAccessingBeanManager() {

        // War itself
        assertNotNull(bdaExtension.getBeanManager());
        assertEquals(bdaExtension.getBeanManager(), getCurrentManager());

        assertEquals(MarkerObtainerWar.getBeans(Marker.class).size(), 1);
        assertEquals(MarkerObtainerWar.getBeans(Marker.class).iterator().next().getBeanClass(), Alpha.class);
        assertEquals(MarkerObtainerWar.getBeans(Foo.class).size(), 1);
        assertEquals(MarkerObtainerWar.getBeans(Bar.class).size(), 1);
        // BDA 1
        assertEquals(MarkerObtainerBda1.getBeans(Marker.class).size(), 1);
        assertEquals(MarkerObtainerBda1.getBeans(Marker.class).iterator().next().getBeanClass(), Bravo.class);
        assertEquals(MarkerObtainerBda1.getBeans(Foo.class).size(), 1);
        assertEquals(MarkerObtainerBda1.getBeans(Bar.class).size(), 1);
        // BDA 2
        assertEquals(MarkerObtainerBda2.getBeans(Marker.class).size(), 1);
        assertEquals(MarkerObtainerBda2.getBeans(Marker.class).iterator().next().getBeanClass(), Charlie.class);
        assertEquals(MarkerObtainerBda2.getBeans(Baz.class).size(), 1);
        assertEquals(MarkerObtainerBda2.getBeans(Bar.class).size(), 1);

        // non-bda
        assertNotNull(nonbdaExtension.getBeanManager());
        assertEquals(nonbdaExtension.getBeanManager().getBeans(Marker.class).size(), 0);
        assertEquals(MarkerObtainerNonBda.getBeans(Marker.class).size(), 0);
        assertEquals(MarkerObtainerNonBda.getBeans(Foo.class).size(), 1);
        assertEquals(MarkerObtainerNonBda.getBeans(Bar.class).size(), 1);
        assertEquals(MarkerObtainerNonBda.getBeans(Baz.class).size(), 1);
    }

}
