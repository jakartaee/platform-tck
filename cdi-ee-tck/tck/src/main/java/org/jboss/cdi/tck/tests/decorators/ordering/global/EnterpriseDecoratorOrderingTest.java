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
package org.jboss.cdi.tck.tests.decorators.ordering.global;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.DECORATORS_EE;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.jboss.shrinkwrap.descriptor.api.spec.se.manifest.ManifestDescriptor;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author Matus Abaffy
 */
@SpecVersion(spec = "cdi", version = "2.0")
    public class EnterpriseDecoratorOrderingTest extends AbstractTest {

    /**
     * Modules:
     * <ul>
     * <li>A - EJB jar: LegacyDecorator2, DecoratedImpl</li>
     * <li>B - lib: Decorated, GloballyEnabledDecorator3</li>
     * <li>C - lib: AbstractDecorator</li>
     * <li>D - EJB jar: GloballyEnabledDecorator2, GloballyEnabledDecorator5, LegacyDecorator3</li>
     * <li>E - web archive
     * <ul>
     * <li>F - WEB-INF/classes: GloballyEnabledDecorator1, LegacyDecorator1</li>
     * <li>G - WEB-INF/lib: GloballyEnabledDecorator4</li>
     * </ul>
     * </li>
     * </ul>
     *
     * @return test archive
     */
    @Deployment
    public static EnterpriseArchive createTestArchive() {
        final String ejbJar = "ejb.jar";

        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder().noDefaultWebModule()
                //A - default test-ejb.jar
                .withTestClassDefinition(EnterpriseDecoratorOrderingTest.class)
                .withClasses(DecoratedImpl.class, LegacyDecorator2.class)
                .withBeansXml(new BeansXml().decorators(LegacyDecorator2.class))
                //B
                .withBeanLibrary(Decorated.class, GloballyEnabledDecorator3.class)
                //C
                .withLibrary(AbstractDecorator.class)
                .build();

        //D
        JavaArchive ejbArchive = ShrinkWrap
                .create(JavaArchive.class, ejbJar)
                .addClasses(DummyDao.class, GloballyEnabledDecorator2.class, GloballyEnabledDecorator5.class, LegacyDecorator3.class)
                .addAsManifestResource(new BeansXml().decorators(LegacyDecorator3.class), "beans.xml")
                .setManifest(
                        new StringAsset(Descriptors.create(ManifestDescriptor.class)
                                .addToClassPath(EnterpriseArchiveBuilder.DEFAULT_EJB_MODULE_NAME)
                                .exportAsString()));
        enterpriseArchive.addAsModule(ejbArchive);

        //E
        WebArchive webArchive = new WebArchiveBuilder()
                .notTestArchive()
                //F
                .withClasses(EnterpriseDecoratorOrderingTest.class, GloballyEnabledDecorator1.class, LegacyDecorator1.class)
                .withBeansXml(new BeansXml().decorators(LegacyDecorator1.class))
                //G
                .withBeanLibrary(GloballyEnabledDecorator4.class)
                .build()
                .setManifest(
                        new StringAsset(Descriptors.create(ManifestDescriptor.class)
                                .addToClassPath(EnterpriseArchiveBuilder.DEFAULT_EJB_MODULE_NAME)
                                .addToClassPath(ejbJar)
                                .exportAsString()));
        enterpriseArchive.addAsModule(webArchive);

        return enterpriseArchive;
    }

    @Inject
    private Decorated decorated;

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = DECORATORS_EE, id = "b"),
        @SpecAssertion(section = DECORATORS_EE, id = "c"),
        @SpecAssertion(section = DECORATORS_EE, id = "d")})
    public void testDecoratorsInWebInfClasses() {

        List<String> expected = new ArrayList<String>();
        // 800
        expected.add(GloballyEnabledDecorator5.class.getSimpleName());
        // 995
        expected.add(GloballyEnabledDecorator1.class.getSimpleName());
        // 1005
        expected.add(GloballyEnabledDecorator2.class.getSimpleName());
        // 1015
        expected.add(GloballyEnabledDecorator3.class.getSimpleName());
        // 1025
        expected.add(GloballyEnabledDecorator4.class.getSimpleName());
        // Decorators enabled using beans.xml
        //LegacyDecorator1 enabled only in F
        expected.add(LegacyDecorator2.class.getSimpleName());
        //LegacyDecorator3 enabled only in D
        // Bean itself
        expected.add(DecoratedImpl.class.getSimpleName());

        List<String> actual = new ArrayList<String>();
        decorated.getSequence(actual);
        assertEquals(actual, expected);
    }
}
