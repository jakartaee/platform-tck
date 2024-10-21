/*
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
package org.jboss.cdi.tck.tests.inheritance.specialization.enterprise;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.DIRECT_AND_INDIRECT_SPECIALIZATION_EE;
import static org.jboss.cdi.tck.cdi.Sections.SPECIALIZE_SESSION_BEAN;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Named;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseBeanSpecializationTest extends AbstractTest {

    @SuppressWarnings("serial")
    private static Annotation LANDOWNER_LITERAL = new Landowner.Literal();

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EnterpriseBeanSpecializationTest.class).build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = DIRECT_AND_INDIRECT_SPECIALIZATION_EE, id = "ia"), @SpecAssertion(section = SPECIALIZE_SESSION_BEAN, id = "aa") })
    public void testDirectSpecialization() {

        Set<Bean<LazyFarmerLocal>> farmerBeans = getBeans(LazyFarmerLocal.class, LANDOWNER_LITERAL);
        assertEquals(farmerBeans.size(), 1);
        Bean<LazyFarmerLocal> lazyFarmerBean = farmerBeans.iterator().next();
        assertEquals(lazyFarmerBean.getBeanClass(), LazyFarmer.class);

        // Types of specializing bean
        Set<Type> lazyFarmerBeanTypes = lazyFarmerBean.getTypes();
        assertEquals(lazyFarmerBeanTypes.size(), 3);
        assertTrue(typeSetMatches(lazyFarmerBeanTypes, Object.class, FarmerLocal.class, LazyFarmerLocal.class));
    }

    @SuppressWarnings("unchecked")
    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = DIRECT_AND_INDIRECT_SPECIALIZATION_EE, id = "j") })
    public void testSpecializingBeanHasBindingsOfSpecializedAndSpecializingBean() {
        Set<Bean<LazyFarmerLocal>> farmerBeans = getBeans(LazyFarmerLocal.class, LANDOWNER_LITERAL);
        assertEquals(farmerBeans.size(), 1);
        Bean<LazyFarmerLocal> lazyFarmerBean = farmerBeans.iterator().next();
        assertEquals(lazyFarmerBean.getQualifiers().size(), 4);
        // LazyFarmer inherits LandOwner and Named from Farmer
        assertTrue(annotationSetMatches(lazyFarmerBean.getQualifiers(), Landowner.class, Lazy.class, Any.class, Named.class));
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = DIRECT_AND_INDIRECT_SPECIALIZATION_EE, id = "k") })
    public void testSpecializingBeanHasNameOfSpecializedBean() {
        String expectedName = "farmer";
        Set<Bean<?>> beans = getCurrentManager().getBeans(expectedName);
        assertEquals(beans.size(), 1);
        Bean<?> farmerBean = beans.iterator().next();
        assertEquals(farmerBean.getName(), expectedName);
        assertEquals(farmerBean.getBeanClass(), LazyFarmer.class);
    }

    @Test(groups = { INTEGRATION }, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = SPECIALIZE_SESSION_BEAN, id = "ca") })
    public void testSpecializedBeanNotInstantiated(@Landowner FarmerLocal farmer) throws Exception {
        assertEquals(farmer.getClassName(), LazyFarmer.class.getName());
    }

}
