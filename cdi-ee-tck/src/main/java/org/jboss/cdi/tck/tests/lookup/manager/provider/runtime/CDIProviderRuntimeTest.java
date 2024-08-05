/*
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

package org.jboss.cdi.tck.tests.lookup.manager.provider.runtime;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.PROVIDER;
import static org.jboss.cdi.tck.cdi.Sections.PROVIDER_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test {@link CDI} after the application initialization is completed until the application shutdown starts.
 *
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class CDIProviderRuntimeTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(CDIProviderRuntimeTest.class)
                // BDA01
                .withClasses(Alpha.class, Powerful.class, PowerfulLiteral.class, AlphaLocator.class)
                // BDA01
                .withBeanLibrary(
                        new BeansXml().alternatives(Bravo.class),
                        Bravo.class, BravoLocator.class, BravoMarker.class).build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = PROVIDER, id = "ab") })
    public void testGetBeanManager(BravoMarker bravoMarker) {

        // Test BDA01
        BeanManager alphaManager = AlphaLocator.getBeanManager();
        assertNotNull(alphaManager);
        assertEquals(alphaManager.getBeans(Alpha.class, PowerfulLiteral.INSTANCE).size(), 1);
        // Bravo is an alternative enabled in BDA02 only
        assertEquals(alphaManager.getBeans(Bravo.class).size(), 0);

        // Test BDA02
        assertNotNull(bravoMarker);
        BeanManager bravoManager = bravoMarker.getBeanManager();
        assertNotNull(bravoManager);
        assertEquals(bravoManager.getBeans(Alpha.class, PowerfulLiteral.INSTANCE).size(), 1);
        assertEquals(bravoManager.getBeans(Bravo.class).size(), 1);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = PROVIDER, id = "ac") })
    public void testLookup(BravoMarker bravoMarker) {

        // Test BDA01
        Alpha alpha = AlphaLocator.lookupAlpha();
        assertNotNull(alpha);
        assertTrue(alpha.ping());

        // Test BDA02
        assertNotNull(bravoMarker);
        Bravo bravo = bravoMarker.lookupBravo();
        assertNotNull(bravo);
        assertTrue(bravo.ping());
    }

    //    @Test(expectedExceptions = UnsupportedOperationException.class)
    @SpecAssertion(section = PROVIDER_EE, id = "e")
    public void testCDIProviderInitializeThrowUnsupportedOperationException() {
        // FIXME
        // CDI.getCDIProvider().initialize();
    }

}
