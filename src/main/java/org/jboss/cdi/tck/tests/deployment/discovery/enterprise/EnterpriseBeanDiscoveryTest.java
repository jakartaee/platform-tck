/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.deployment.discovery.enterprise;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE_EE;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_DEFINING_ANNOTATIONS;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_DISCOVERY_STEPS_EE;
import static org.jboss.cdi.tck.cdi.Sections.DEFAULT_BEAN_DISCOVERY_EE;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.spi.Extension;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeanDiscoveryMode;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.jboss.shrinkwrap.descriptor.api.spec.se.manifest.ManifestDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseBeanDiscoveryTest extends AbstractTest {

    private static final String ALPHA_JAR = "alpha.jar";
    private static final String BRAVO_JAR = "bravo.jar";
    private static final String CHARLIE_JAR = "charlie.jar";
    private static final String DELTA_JAR = "delta.jar";
    private static final String ECHO_JAR = "echo.jar";
    private static final String FOXTROT_JAR = "foxtrot.jar";
    private static final String LEGACY_JAR = "legacy.jar";

    @Deployment
    public static EnterpriseArchive createTestArchive() {

        // 1.1 version beans.xml with bean-discovery-mode of all
        JavaArchive alpha = ShrinkWrap
                .create(JavaArchive.class, ALPHA_JAR)
                .addClasses(Alpha.class, AlphaLocal.class)
                .addAsManifestResource(
                        new StringAsset(Descriptors.create(BeansDescriptor.class).beanDiscoveryMode(BeanDiscoveryMode._ALL.toString()).exportAsString()),
                        "beans.xml");
        // Empty beans.xml
        JavaArchive bravo = ShrinkWrap.create(JavaArchive.class, BRAVO_JAR).addClasses(Bravo.class, BravoLocal.class)
                .addAsManifestResource(new StringAsset(""), "beans.xml");
        // No version beans.xml
        JavaArchive charlie = ShrinkWrap
                .create(JavaArchive.class, CHARLIE_JAR)
                .addClasses(Charlie.class, CharlieLocal.class)
                .addAsManifestResource(new StringAsset(Descriptors.create(BeansDescriptor.class).exportAsString()), "beans.xml");
        // Session bean and no beans.xml
        JavaArchive delta = ShrinkWrap.create(JavaArchive.class, DELTA_JAR).addClasses(Delta.class, DeltaLocal.class);
        // Session bean and 1.1 version beans.xml with bean-discovery-mode of annotated
        JavaArchive echo = ShrinkWrap
                .create(JavaArchive.class, ECHO_JAR)
                .addClasses(Echo.class, EchoLocal.class)
                .addAsManifestResource(
                        new StringAsset(Descriptors.create(BeansDescriptor.class).beanDiscoveryMode(BeanDiscoveryMode._ANNOTATED.toString()).exportAsString()),
                        "beans.xml");
        // Session bean and 1.1 version beans.xml with bean-discovery-mode of none
        JavaArchive foxtrot = ShrinkWrap
                .create(JavaArchive.class, FOXTROT_JAR)
                .addClasses(Foxtrot.class, FoxtrotLocal.class)
                .addAsManifestResource(
                        new StringAsset(Descriptors.create(BeansDescriptor.class).beanDiscoveryMode(BeanDiscoveryMode._NONE.toString()).exportAsString()),
                        "beans.xml");

        // Archive which contains an extension and no beans.xml file - not a bean archive
        JavaArchive legacy = ShrinkWrap.create(JavaArchive.class, LEGACY_JAR)
                .addClasses(LegacyExtension.class, LegacyBean.class)
                .addAsServiceProvider(Extension.class, LegacyExtension.class);

        WebArchive webArchive = new WebArchiveBuilder()
                .withClasses(EnterpriseBeanDiscoveryTest.class)
                .notTestArchive()
                .build()
                .setManifest(
                        new StringAsset(Descriptors.create(ManifestDescriptor.class)
                                .addToClassPath(EnterpriseArchiveBuilder.DEFAULT_EJB_MODULE_NAME).addToClassPath(ALPHA_JAR)
                                .addToClassPath(BRAVO_JAR).addToClassPath(CHARLIE_JAR).addToClassPath(DELTA_JAR)
                                .addToClassPath(ECHO_JAR).addToClassPath(FOXTROT_JAR).addToClassPath(LEGACY_JAR)
                                .exportAsString()));

        return new EnterpriseArchiveBuilder().noDefaultWebModule().withTestClassDefinition(EnterpriseBeanDiscoveryTest.class)
                .withClasses(VerifyingExtension.class).withExtension(VerifyingExtension.class).withLibrary(Ping.class).build()
                .addAsModules(webArchive, alpha, bravo, charlie, delta, echo, foxtrot, legacy);
    }

    @Inject
    VerifyingExtension extension;

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE_EE, id = "ba"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS_EE, id = "tc") })
    public void testExplicitBeanArchiveModeAll() {
        assertDiscoveredAndAvailable(AlphaLocal.class, Alpha.class);
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE_EE, id = "bb"), @SpecAssertion(section = BEAN_ARCHIVE_EE, id = "bc"),
            @SpecAssertion(section = BEAN_DISCOVERY_STEPS_EE, id = "tc") })
    public void testExplicitBeanArchiveEmptyDescriptor() {
        assertDiscoveredAndAvailable(BravoLocal.class, Bravo.class);
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE_EE, id = "bc"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS_EE, id = "tc") })
    public void testExplicitBeanArchiveLegacyDescriptor() {
        assertDiscoveredAndAvailable(CharlieLocal.class, Charlie.class);
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = DEFAULT_BEAN_DISCOVERY_EE, id = "a"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS_EE, id = "tc"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "ba") })
    public void testImplicitBeanArchiveNoDescriptor() {
        assertDiscoveredAndAvailable(DeltaLocal.class, Delta.class);
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = DEFAULT_BEAN_DISCOVERY_EE, id = "a"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS_EE, id = "tc"),
            @SpecAssertion(section = BEAN_DEFINING_ANNOTATIONS, id = "ba") })
    public void testImplicitBeanArchiveModeAnnotated() {
        assertDiscoveredAndAvailable(EchoLocal.class, Echo.class);
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE_EE, id = "oa"), @SpecAssertion(section = BEAN_DISCOVERY_STEPS_EE, id = "tc") })
    public void testNoBeanArchiveModeNone() {
        assertNotDiscoveredAndNotAvailable(FoxtrotLocal.class, Foxtrot.class);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE_EE, id = "ob") })
    public void testNotBeanArchiveExtension() {
        assertNotDiscoveredAndNotAvailable(LegacyBean.class, LegacyBean.class);
    }

    private <T extends Ping, B extends Ping> void assertDiscoveredAndAvailable(Class<T> beanType, Class<B> beanClazz) {
        T instance = getContextualReference(beanType);
        assertNotNull(instance);
        assertTrue(extension.getObservedAnnotatedTypes().contains(beanClazz));
        instance.pong();
    }

    private <T extends Ping, B extends Ping> void assertNotDiscoveredAndNotAvailable(Class<T> beanType, Class<B> beanClazz) {
        assertFalse(extension.getObservedAnnotatedTypes().contains(beanClazz));
        assertTrue(getBeans(beanType).isEmpty());
    }

}
