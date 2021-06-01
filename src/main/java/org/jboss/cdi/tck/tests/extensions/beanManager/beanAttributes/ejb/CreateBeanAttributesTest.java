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
package org.jboss.cdi.tck.tests.extensions.beanManager.beanAttributes.ejb;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.AnnotatedConstructor;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeanAttributes;
import jakarta.inject.Named;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.extensions.beanManager.beanAttributes.Animal;
import org.jboss.cdi.tck.tests.extensions.beanManager.beanAttributes.Fish;
import org.jboss.cdi.tck.tests.extensions.beanManager.beanAttributes.InvalidBeanType;
import org.jboss.cdi.tck.tests.extensions.beanManager.beanAttributes.Landmark;
import org.jboss.cdi.tck.tests.extensions.beanManager.beanAttributes.Natural;
import org.jboss.cdi.tck.tests.extensions.beanManager.beanAttributes.TundraStereotype;
import org.jboss.cdi.tck.tests.extensions.beanManager.beanAttributes.WaterBody;
import org.jboss.cdi.tck.tests.extensions.beanManager.beanAttributes.Wild;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BM_OBTAIN_BEANATTRIBUTES;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class CreateBeanAttributesTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(CreateBeanAttributesTest.class)
                .withClasses(Animal.class, Fish.class, InvalidBeanType.class, Landmark.class, Natural.class,
                             TundraStereotype.class, WaterBody.class,Wild.class)
                .build();
    }


    @SuppressWarnings("unchecked")
    @Test(groups = INTEGRATION)
    @SpecAssertion(section = BM_OBTAIN_BEANATTRIBUTES, id = "a")
    public void testBeanAttributesForSessionBean() {
        AnnotatedType<Lake> type = getCurrentManager().createAnnotatedType(Lake.class);
        BeanAttributes<Lake> attributes = getCurrentManager().createBeanAttributes(type);

        assertTrue(typeSetMatches(attributes.getTypes(), LakeLocal.class, WaterBody.class, Landmark.class, Object.class));
        assertTrue(typeSetMatches(attributes.getStereotypes(), TundraStereotype.class));
        assertTrue(annotationSetMatches(attributes.getQualifiers(), Natural.class, Any.class));
        assertEquals(attributes.getScope(), Dependent.class);
        assertEquals(attributes.getName(), "lake");
        assertTrue(attributes.isAlternative());
    }

    @Test
    @SpecAssertion(section = BM_OBTAIN_BEANATTRIBUTES, id = "b")
    public void testBeanAttributesForMethod() {
        AnnotatedType<Dam> type = getCurrentManager().createAnnotatedType(Dam.class);

        AnnotatedMethod<?> lakeFishMethod = null;
        AnnotatedMethod<?> damFishMethod = null;
        AnnotatedMethod<?> volumeMethod = null;

        for (AnnotatedMethod<?> method : type.getMethods()) {
            if (method.getJavaMember().getName().equals("getFish")
                    && method.getJavaMember().getDeclaringClass().equals(Dam.class)) {
                damFishMethod = method;
            }
            if (method.getJavaMember().getName().equals("getFish")
                    && method.getJavaMember().getDeclaringClass().equals(Lake.class)) {
                lakeFishMethod = method;
            }
            if (method.getJavaMember().getName().equals("getVolume")
                    && method.getJavaMember().getDeclaringClass().equals(Lake.class)) {
                volumeMethod = method;
            }
        }
        assertNotNull(lakeFishMethod);
        assertNotNull(damFishMethod);
        assertNotNull(volumeMethod);

        verifyLakeFish(getCurrentManager().createBeanAttributes(lakeFishMethod));
        verifyDamFish(getCurrentManager().createBeanAttributes(damFishMethod));
        verifyVolume(getCurrentManager().createBeanAttributes(volumeMethod));
    }

    @Test
    @SpecAssertion(section = BM_OBTAIN_BEANATTRIBUTES, id = "b")
    public void testBeanAttributesForField() {
        AnnotatedType<Dam> type = getCurrentManager().createAnnotatedType(Dam.class);

        AnnotatedField<?> lakeFishField = null;
        AnnotatedField<?> damFishField = null;
        AnnotatedField<?> volumeField = null;

        for (AnnotatedField<?> field : type.getFields()) {
            if (field.getJavaMember().getName().equals("fish") && field.getJavaMember().getDeclaringClass().equals(Dam.class)) {
                damFishField = field;
            }
            if (field.getJavaMember().getName().equals("fish") && field.getJavaMember().getDeclaringClass().equals(Lake.class)) {
                lakeFishField = field;
            }
            if (field.getJavaMember().getName().equals("volume")
                    && field.getJavaMember().getDeclaringClass().equals(Lake.class)) {
                volumeField = field;
            }
        }
        assertNotNull(lakeFishField);
        assertNotNull(damFishField);
        assertNotNull(volumeField);

        verifyLakeFish(getCurrentManager().createBeanAttributes(lakeFishField));
        verifyDamFish(getCurrentManager().createBeanAttributes(damFishField));
        verifyVolume(getCurrentManager().createBeanAttributes(volumeField));
    }

    @SuppressWarnings("unchecked")
    private void verifyLakeFish(BeanAttributes<?> attributes) {
        assertTrue(typeSetMatches(attributes.getTypes(), Fish.class, Object.class));
        assertTrue(typeSetMatches(attributes.getStereotypes(), TundraStereotype.class));
        assertTrue(annotationSetMatches(attributes.getQualifiers(), Natural.class, Any.class, Named.class));
        assertEquals(attributes.getScope(), ApplicationScoped.class);
        assertEquals(attributes.getName(), "fish");
        assertTrue(attributes.isAlternative());
    }

    @SuppressWarnings("unchecked")
    private void verifyDamFish(BeanAttributes<?> attributes) {
        assertTrue(typeSetMatches(attributes.getTypes(), Fish.class, Animal.class, Object.class));
        assertTrue(annotationSetMatches(attributes.getQualifiers(), Wild.class, Any.class));
        assertTrue(attributes.getStereotypes().isEmpty());
        assertEquals(attributes.getScope(), Dependent.class);
        assertNull(attributes.getName());
        assertFalse(attributes.isAlternative());
    }

    @SuppressWarnings("unchecked")
    private void verifyVolume(BeanAttributes<?> attributes) {
        assertTrue(typeSetMatches(attributes.getTypes(), long.class, Object.class));
        assertTrue(annotationSetMatches(attributes.getQualifiers(), Any.class, Default.class, Named.class));
        assertTrue(attributes.getStereotypes().isEmpty());
        assertEquals(attributes.getScope(), Dependent.class);
        assertEquals(attributes.getName(), "volume");
        assertFalse(attributes.isAlternative());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    @SpecAssertion(section = BM_OBTAIN_BEANATTRIBUTES, id = "c")
    public void testInvalidMember() {
        AnnotatedConstructor<?> constructor = getCurrentManager().createAnnotatedType(InvalidBeanType.class).getConstructors()
                .iterator().next();
        getCurrentManager().createBeanAttributes(constructor);
    }
}
