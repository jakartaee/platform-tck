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
package org.jboss.cdi.tck.tests.lookup.modules.interceptors;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.SELECTION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.Instance;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.Testable;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.DummySessionBean;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.se.manifest.ManifestDescriptor;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test that interceptor class's injection and injection point validation are done with respect to the module containing the
 * interceptor class and not the intercepted class, i.e. test that an interceptor of a bean uses the interceptor's bean manager
 * and not the bean's bean manager (for injection and injection point validation).
 *
 * @author Matus Abaffy
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class InterceptorModularityTest extends AbstractTest {

    private static final String TEST1_JAR = "test1.jar";

    @Deployment
    public static EnterpriseArchive createTestArchive() {

        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder()
                .withTestClassDefinition(InterceptorModularityTest.class)
                .withClasses(Checker.class, BarBinding.class, Animal.class, Cow.class, BarSuperInterceptor.class)
                .withBeansXml(new BeansXml().alternatives(Cow.class))
                .noDefaultWebModule().build();

        JavaArchive fooArchive = ShrinkWrap.create(JavaArchive.class, TEST1_JAR)
                .addClasses(BarInterceptor.class, Dog.class, DummySessionBean.class)
                .setManifest(new StringAsset(Descriptors.create(ManifestDescriptor.class)
                                .addToClassPath(EnterpriseArchiveBuilder.DEFAULT_EJB_MODULE_NAME)
                                .exportAsString()))
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        enterpriseArchive.addAsModule(fooArchive);

        WebArchive barArchive = Testable.archiveToTest(new WebArchiveBuilder().notTestArchive().withName("test2.war")
                .withClasses(InterceptorModularityTest.class, Bar.class, Cat.class)
                .build()
                .setManifest(
                        new StringAsset(Descriptors.create(ManifestDescriptor.class)
                                .addToClassPath(EnterpriseArchiveBuilder.DEFAULT_EJB_MODULE_NAME)
                                .addToClassPath(TEST1_JAR)
                                .exportAsString())));
        enterpriseArchive.addAsModule(barArchive);

        return enterpriseArchive;
    }

    @Test(groups = JAVAEE_FULL, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = SELECTION, id = "aa")
    public void testInterceptorEnvironment(Instance<Bar> barInstance, Instance<BarSuperInterceptor> barSIInstance,
            Checker checker) {
        checker.reset();
        barInstance.get().ping();

        assertTrue(checker.isBarInterceptorCalled());
        assertNotNull(checker.getAnimal());
        assertEquals(checker.getAnimal().getName(), "Dog");
        assertEquals(checker.getSuperAnimal().getName(), "Dog");

        barSIInstance.get();
        assertEquals(checker.getSuperAnimal().getName(), "Cow");
    }
}
