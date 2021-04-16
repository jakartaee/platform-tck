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

package org.jboss.cdi.tck.tests.deployment.packaging.war.modules;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE_EE;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE;
import static org.jboss.cdi.tck.cdi.Sections.DECORATOR_RESOLUTION;
import static org.jboss.cdi.tck.cdi.Sections.ENABLED_DECORATORS_BEAN_ARCHIVE;
import static org.jboss.cdi.tck.cdi.Sections.INIT_EVENTS;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_RESOLUTION;
import static org.jboss.cdi.tck.cdi.Sections.PERFORMING_TYPESAFE_RESOLUTION;
import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_METHOD;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.util.Set;

/**
 * This test is aimed to verify packaging-related issues in a little bit more complex deployment scenario. The assertions are
 * rather informative and redundant as they're already verified in elementary tests.
 *
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class WebArchiveModulesTest extends AbstractTest {

    /**
     * Modules:
     * <ul>
     * <li>A - WEB-INF/classes BDA: Foo, Secured, SecurityInterceptor, Business, BusinessOperationEvent</li>
     * <li>B - WEB-INF/lib BDA: Bar, AlternativeBar, BarInspector</li>
     * <li>C - WEB-INF/lib BDA: Baz, LoggingDecorator, Bazinga</li>
     * <li>D - WEB-INF/lib BDA: Qux, ContainerEventsObserver, LegacyServiceProducer</li>
     * <li>E - WEB-INF/lib non-BDA: LegacyService</li>
     * </ul>
     *
     * @return test archive
     */
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(WebArchiveModulesTest.class)
                // A
                .withClasses(Foo.class, Secured.class, SecurityInterceptor.class, Business.class, BusinessOperationEvent.class)
                .withBeansXml(new BeansXml().alternatives(AlternativeBar.class))
                // B
                .withBeanLibrary(new BeansXml().interceptors(SecurityInterceptor.class), Bar.class, AlternativeBar.class,
                        BarInspector.class)
                // C
                .withBeanLibrary(new BeansXml().decorators(LoggingDecorator.class), Baz.class, LoggingDecorator.class, Bazinga.class)
                // D
                .withBeanLibrary(Qux.class, ContainerEventsObserver.class, LegacyServiceProducer.class)
                // E
                .withLibrary(LegacyService.class).build();
    }

    @Inject
    Foo foo;

    @Test
    @SpecAssertions({ @SpecAssertion(section = INIT_EVENTS, id = "bb") })
    public void testExtensionAndContainerEvents() throws Exception {
        // Test extension registration and container lifecycle events
        assertTrue(ContainerEventsObserver.allEventsOk());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE_EE, id = "jg"), @SpecAssertion(section = BEAN_ARCHIVE_EE, id = "jh"),
            @SpecAssertion(section = PERFORMING_TYPESAFE_RESOLUTION, id = "n") })
    public void testInjectionChainVisibilityAndInterceptorEnablement() {
        // Test injection chain, visibility, SecurityInterceptor is enabled in B only
        SecurityInterceptor.reset();
        Bar bar = foo.getBar();
        assertTrue(bar.isAlternative());
        assertTrue(bar.getBaz().getId().equals(bar.getQux().getBaz().getId()));
        assertEquals(SecurityInterceptor.getNumberOfInterceptions(), 3);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECORATOR_RESOLUTION, id = "aa"), @SpecAssertion(section = ENABLED_DECORATORS_BEAN_ARCHIVE, id = "a"),
            @SpecAssertion(section = OBSERVER_RESOLUTION, id = "c") })
    public void testDecoratorAndCrossModuleEventObserver() throws Exception {
        // Test LoggingDecorator is enabled in C only; bean from D observes event from A
        foo.getBar().getBaz().businessOperation1();
        foo.businessOperation1();
        assertEquals(LoggingDecorator.getNumberOfDecorationsPerformed(), 1);
        assertEquals(Qux.getEventsObserved(), 1);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_METHOD, id = "aa"), @SpecAssertion(section = PRODUCER_METHOD, id = "c"),
            @SpecAssertion(section = OBSERVER_RESOLUTION, id = "c") })
    public void testProducerAndEventDuringDisposal() throws Exception {
        // Test legacy service producer in D, bean from A is observing event fired during legacy service disposal in D
        Bean<LegacyService> bean = getUniqueBean(LegacyService.class);
        CreationalContext<LegacyService> ctx = getCurrentManager().createCreationalContext(bean);
        LegacyService instance = bean.create(ctx);
        bean.destroy(instance, ctx);
        assertTrue(Foo.legacyObserved);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE, id = "i") })
    public void testAlternatives(BarInspector barInspector) throws Exception {

        Set<Bean<?>> beans = getCurrentManager().getBeans(AlternativeBar.class);
        assertEquals(beans.size(), 1);
        assertEquals(beans.iterator().next().getBeanClass(), AlternativeBar.class);

        // Bar alternative is enabled in WEB-INF/classes only
        assertFalse(barInspector.getBar().isAlternative());
    }
}
