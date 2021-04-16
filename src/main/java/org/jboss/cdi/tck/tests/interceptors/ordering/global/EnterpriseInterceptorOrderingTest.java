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
package org.jboss.cdi.tck.tests.interceptors.ordering.global;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.ENABLED_INTERCEPTORS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
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
public class EnterpriseInterceptorOrderingTest extends AbstractTest {

    /**
     * Modules:
     * <ul>
     * <li>A - EJB jar: Dao, LegacyInterceptor2</li>
     * <li>B - lib: Transactional, GloballyEnabledInterceptor3</li>
     * <li>C - lib: AbstractDecorator</li>
     * <li>D - EJB jar: GloballyEnabledInterceptor2, GloballyEnabledInterceptor5, LegacyInterceptor3</li>
     * <li>E - web archive
     * <ul>
     * <li>F - WEB-INF/classes: GloballyEnabledInterceptor1, LegacyInterceptor1</li>
     * <li>G - WEB-INF/lib: GloballyEnabledInterceptor4</li>
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
                .withTestClassDefinition(EnterpriseInterceptorOrderingTest.class)
                .withClasses(Dao.class, LegacyInterceptor2.class)
                .withBeansXml(new BeansXml().interceptors(LegacyInterceptor2.class))
                //B
                .withBeanLibrary(Transactional.class, GloballyEnabledInterceptor3.class)
                //C
                .withLibrary(AbstractInterceptor.class)
                .build();

        //D
        JavaArchive ejbArchive = ShrinkWrap
                .create(JavaArchive.class, ejbJar)
                .addClasses(DummyDao.class, GloballyEnabledInterceptor2.class, GloballyEnabledInterceptor5.class, LegacyInterceptor3.class)
                .addAsManifestResource(new BeansXml().interceptors(LegacyInterceptor3.class), "beans.xml")
                .setManifest(
                        new StringAsset(Descriptors.create(ManifestDescriptor.class)
                                .addToClassPath(EnterpriseArchiveBuilder.DEFAULT_EJB_MODULE_NAME)
                                .exportAsString()));
        enterpriseArchive.addAsModule(ejbArchive);

        //E
        WebArchive webArchive = new WebArchiveBuilder()
                .notTestArchive()
                //F
                .withClasses(EnterpriseInterceptorOrderingTest.class, GloballyEnabledInterceptor1.class, LegacyInterceptor1.class)
                .withBeansXml(new BeansXml().interceptors(LegacyInterceptor1.class))
                //G
                .withBeanLibrary(GloballyEnabledInterceptor4.class)
                .build()
                .setManifest(
                        new StringAsset(Descriptors.create(ManifestDescriptor.class)
                                .addToClassPath(EnterpriseArchiveBuilder.DEFAULT_EJB_MODULE_NAME)
                                .addToClassPath(ejbJar)
                                .exportAsString()));
        enterpriseArchive.addAsModule(webArchive);

        return enterpriseArchive;
    }

    @Test(groups = JAVAEE_FULL, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "f"),
        @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "i") })
    public void testDecoratorsInWebInfClasses(Dao dao) {

        assertNotNull(dao);
        ActionSequence.reset();
        dao.ping();

        List<String> expected = new ArrayList<String>();
        // 800
        expected.add(GloballyEnabledInterceptor5.class.getName());
        // 995
        expected.add(GloballyEnabledInterceptor1.class.getName());
        // 1005
        expected.add(GloballyEnabledInterceptor2.class.getName());
        // 1015
        expected.add(GloballyEnabledInterceptor3.class.getName());
        // 1025
        expected.add(GloballyEnabledInterceptor4.class.getName());
        // Interceptors enabled using beans.xml
        //LegacyInterceptor1 enabled only in F
        expected.add(LegacyInterceptor2.class.getName());
        //LegacyInterceptor3 enabled only in D
        // Bean itself

        assertEquals(ActionSequence.getSequenceData(), expected);
    }
}
