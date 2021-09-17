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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processBeanAttributes.ejb;

import java.lang.annotation.Annotation;

import jakarta.decorator.Decorator;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.Annotated;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.enterprise.inject.spi.BeanAttributes;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.processBeanAttributes.Alpha;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.processBeanAttributes.Bravo;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.processBeanAttributes.BravoDecorator;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.processBeanAttributes.BravoInterceptor;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.processBeanAttributes.BravoInterface;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.processBeanAttributes.BravoProducer;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.processBeanAttributes.BravoQualifier;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.processBeanAttributes.CharlieInterface;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.processBeanAttributes.CharlieProducer;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.jboss.cdi.tck.cdi.Sections.PROCESS_BEAN_ATTRIBUTES;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_BEAN_ATTRIBUTES_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * <p/>
 * This test was originally part of Weld test suite.
 * <p/>
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test
@SpecVersion(spec = "cdi", version = "2.0")
public class VerifyValuesTest extends AbstractTest {

    @Inject
    private VerifyingExtension extension;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(VerifyValuesTest.class)
                .withClasses(Alpha.class, Bravo.class, BravoDecorator.class, BravoInterceptor.class, BravoInterface.class,
                             BravoProducer.class, BravoQualifier.class, CharlieInterface.class, CharlieProducer.class)
                .withBeansXml(new BeansXml()
                              .alternatives(Alpha.class, BravoProducer.class, CharlieProducer.class)
                              .interceptors(BravoInterceptor.class)
                              .decorators(BravoDecorator.class)
                )
                .withExtension(VerifyingExtension.class).build();
    }


    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES_EE, id = "bab") })
    public void testSessionBeanAnnotated() {
        Annotated deltaAnnotated = extension.getAnnotatedMap().get(Delta.class);
        assertNotNull(deltaAnnotated);
        assertTrue(deltaAnnotated instanceof AnnotatedType);
        @SuppressWarnings("unchecked")
        AnnotatedType<Delta> deltaAnnotatedType = (AnnotatedType<Delta>) deltaAnnotated;
        assertEquals(deltaAnnotatedType.getJavaClass(), Delta.class);
        assertEquals(deltaAnnotatedType.getMethods().size(), 1);
        assertEquals(deltaAnnotatedType.getMethods().iterator().next().getJavaMember().getName(), "foo");
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "aa"), @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES_EE, id = "bab"),
            @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "bb") })
    public void testSessionBeanAttributes() {
        BeanAttributes<Delta> deltaAttributes = extension.getDeltaAttributes();
        assertNotNull(deltaAttributes);
        assertEquals(deltaAttributes.getScope(), Dependent.class);
        verifyName(deltaAttributes, "delta");
        assertFalse(deltaAttributes.isAlternative());

        assertTrue(typeSetMatches(deltaAttributes.getTypes(), Object.class, Delta.class));
        assertTrue(deltaAttributes.getStereotypes().isEmpty());
        assertTrue(annotationSetMatches(deltaAttributes.getQualifiers(), Named.class, Any.class, Default.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "bac") })
    public void testProducerMethodAnnotated() {
        Annotated bravoAnnotated = extension.getAnnotatedMap().get(Bravo.class);
        assertNotNull(bravoAnnotated);
        assertTrue(bravoAnnotated instanceof AnnotatedMethod);
        @SuppressWarnings("unchecked")
        AnnotatedMethod<Bravo> bravoAnnotatedMethod = (AnnotatedMethod<Bravo>) bravoAnnotated;
        assertEquals(bravoAnnotatedMethod.getJavaMember().getName(), "createBravo");
    }


    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "ab"), @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "bb") })
    public void testInterceptorBeanAttributes() {
        BeanAttributes<BravoInterceptor> attributes = extension.getBravoInterceptorAttributes();
        assertNotNull(attributes);
        assertEquals(Dependent.class, attributes.getScope());
        assertFalse(attributes.isAlternative());

        assertTrue(typeSetMatches(attributes.getTypes(), Object.class, BravoInterceptor.class));
        assertTrue(attributes.getStereotypes().isEmpty());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "ac"), @SpecAssertion(section = PROCESS_BEAN_ATTRIBUTES, id = "bb") })
    public void testDecoratorBeanAttributes() {
        BeanAttributes<BravoDecorator> attributes = extension.getBravoDecoratorAttributes();
        assertNotNull(attributes);
        assertEquals(Dependent.class, attributes.getScope());
        assertFalse(attributes.isAlternative());

        assertTrue(typeSetMatches(attributes.getTypes(), Object.class, BravoDecorator.class, BravoInterface.class));
        assertTrue(attributes.getStereotypes().size() == 1);
        assertTrue(attributes.getStereotypes().iterator().next().equals(Decorator.class));
    }

    private void verifyName(BeanAttributes<?> attributes, String name) {
        assertEquals(name, attributes.getName());
        for (Annotation qualifier : attributes.getQualifiers()) {
            if (Named.class.equals(qualifier.annotationType())) {
                assertEquals(name, ((Named) qualifier).value());
                return;
            }
        }
        fail("@Named qualifier not found.");
    }
}
