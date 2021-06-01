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

package org.jboss.cdi.tck.tests.deployment.packaging.ear.modules;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE_EE;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE_EE;
import static org.jboss.cdi.tck.cdi.Sections.DECORATORS_EE;
import static org.jboss.cdi.tck.cdi.Sections.ENABLED_DECORATORS_BEAN_ARCHIVE;
import static org.jboss.cdi.tck.cdi.Sections.INIT_EVENTS;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_RESOLUTION;
import static org.jboss.cdi.tck.cdi.Sections.PERFORMING_TYPESAFE_RESOLUTION;
import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_METHOD;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.util.Versions;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.BeansXmlVersion;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.se.manifest.ManifestDescriptor;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * This test is aimed to verify packaging-related issues in a little bit more complex deployment scenario. The assertions are
 * rather informative and redundant as they're already verified in elementary tests.
 *
 * Note that we DO NOT include test class in EJB module since we wouldn't be able to inject bean from web module (Java EE
 * classloading requirements)!
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseArchiveModulesTest extends AbstractTest {

    /**
     * Modules:
     * <ul>
     * <li>A - EJB jar BDA: Foo, BusinessOperationEventInspector</li>
     * <li>B - EJB jar BDA: Bar, BarInspector, ContainerEventsObserver, LegacyServiceProducer, DummyBar</li>
     * <li>C - lib BDA: AlternativeBar, Util, Business, BusinessOperationEvent, BusinessOperationObservedEvent, NonEnterprise,
     * Secured, SecurityInterceptor, LoggingDecorator</li>
     * <li>D - lib non-BDA: LegacyService</li>
     * <li>E - web archive
     * <ul>
     * <li>F - WEB-INF/classes BDA: Baz</li>
     * <li>G - WEB-INF/lib BDA: Qux</li>
     * </ul>
     * </li>
     * </ul>
     *
     * @return test archive
     */
    @Deployment
    public static EnterpriseArchive createTestArchive() {

        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder()
                // A - default EJB jar
                .withTestClassDefinition(EnterpriseArchiveModulesTest.class)
                .withClasses(Foo.class, BusinessOperationEventInspector.class)
                // C - lib visible to all
                .withBeanLibrary(Util.class, Business.class, BusinessOperationEvent.class,
                        BusinessOperationObservedEvent.class, NonEnterprise.class, Secured.class, SecurityInterceptor.class,
                        LoggingDecorator.class)
                // D - lib visible to all
                .withLibrary(LegacyService.class).noDefaultWebModule().build();

        // B - not visible for ACD
        JavaArchive barArchive = ShrinkWrap
                .create(JavaArchive.class, "bar-ejb.jar")
                .addClasses(Bar.class, AlternativeBar.class, BarInspector.class, ContainerEventsObserver.class, LegacyServiceProducer.class)
                .addAsServiceProvider(Extension.class, ContainerEventsObserver.class)
                .addAsManifestResource(new BeansXml().setBeansXmlVersion(BeansXmlVersion.v11)
                                .setBeanDiscoveryMode(BeanDiscoveryMode.ALL)
                                .interceptors(SecurityInterceptor.class)
                                , "beans.xml")
                // Make A visible in a portable way
                .setManifest(
                        new StringAsset(Descriptors.create(ManifestDescriptor.class)
                                .addToClassPath(EnterpriseArchiveBuilder.DEFAULT_EJB_MODULE_NAME).exportAsString()));
        enterpriseArchive.addAsModule(barArchive);

        // E - not visible for ABCD
        WebArchive bazWebArchive = new WebArchiveBuilder()
                .notTestArchive()
                // F - with enabled decorator
                .withClasses(Baz.class, EnterpriseArchiveModulesTest.class)
                .withBeansXml(new BeansXml().decorators(LoggingDecorator.class)
                        .alternatives(AlternativeBar.class)
                )
                // G
                .withBeanLibrary(Qux.class)
                .build()
                // Make A and B visible in a portable way
                .setManifest(
                        new StringAsset(Descriptors.create(ManifestDescriptor.class)
                                .addToClassPath(EnterpriseArchiveBuilder.DEFAULT_EJB_MODULE_NAME).addToClassPath("bar-ejb.jar")
                                .exportAsString()));
        enterpriseArchive.addAsModule(bazWebArchive);

        return enterpriseArchive;
    }

    @Inject
    Business foo;

    @Inject
    BusinessOperationEventInspector inspector;

    @Inject
    BarInspector barInspector;

    @NonEnterprise
    @Inject
    Business baz;

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = INIT_EVENTS, id = "bb") })
    public void testExtensionAndContainerEvents() throws Exception {
        // Test extension registration and container lifecycle events
        ContainerEventsObserver.assertAllEventsOk();
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE_EE, id = "ja"), @SpecAssertion(section = BEAN_ARCHIVE_EE, id = "jb"),
            @SpecAssertion(section = BEAN_ARCHIVE_EE, id = "jc"), @SpecAssertion(section = BEAN_ARCHIVE_EE, id = "je"),
            @SpecAssertion(section = PERFORMING_TYPESAFE_RESOLUTION, id = "n") })
    public void testVisibilityAndInterceptorEnablement() throws Exception {
        SecurityInterceptor.reset();
        inspector.reset();
        foo.businessOperation1();
        foo.businessOperation2();
        // BusinessOperationEvent observers: Bar, Qux
        assertEquals(inspector.getBusinessOperationObservations(), 2);
        // Interceptor is enabled in B only; observed method intercepted
        assertEquals(SecurityInterceptor.getNumberOfInterceptions(), 1);
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = DECORATORS_EE, id = "a"),
            @SpecAssertion(section = ENABLED_DECORATORS_BEAN_ARCHIVE, id = "a") })
    public void testDecoratorEnablement() throws Exception {
        // Test LoggingDecorator is enabled in F only
        LoggingDecorator.reset();
        baz.businessOperation1();
        foo.businessOperation1();
        assertEquals(LoggingDecorator.getNumberOfDecorationsPerformed(), 1);
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = PRODUCER_METHOD, id = "aa"),
            @SpecAssertion(section = PRODUCER_METHOD, id = "c"), @SpecAssertion(section = OBSERVER_RESOLUTION, id = "c") })
    public void testProducerAndEventDuringDisposal() throws Exception {
        // Test legacy service producer in B, bean from F is observing event fired during legacy service disposal
        Bean<LegacyService> bean = getUniqueBean(LegacyService.class);
        CreationalContext<LegacyService> ctx = getCurrentManager().createCreationalContext(bean);
        LegacyService instance = bean.create(ctx);
        bean.destroy(instance, ctx);
        assertTrue(LegacyService.cleanupPerformed);
        assertTrue(LegacyService.disposalObserved);
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_BEAN_ARCHIVE_EE, id = "bb") })
    public void testAlternatives() throws Exception {
        Set<Bean<?>> beans = getCurrentManager().getBeans(AlternativeBar.class);
        assertEquals(beans.size(), 1);
        assertEquals(beans.iterator().next().getBeanClass(), AlternativeBar.class);
        // Bar alternative is enabled in F only
        assertFalse(barInspector.getBar().isAlternative());
    }

}
