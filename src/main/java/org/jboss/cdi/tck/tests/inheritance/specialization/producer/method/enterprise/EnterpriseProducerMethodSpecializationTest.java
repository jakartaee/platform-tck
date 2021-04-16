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
package org.jboss.cdi.tck.tests.inheritance.specialization.producer.method.enterprise;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_PRODUCER_METHOD_EE;
import static org.jboss.cdi.tck.cdi.Sections.DIRECT_AND_INDIRECT_SPECIALIZATION_EE;
import static org.jboss.cdi.tck.cdi.Sections.PRODUCER_METHOD_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Set;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
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

@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseProducerMethodSpecializationTest extends AbstractTest {

    @SuppressWarnings("serial")
    private static Annotation EXPENSIVE_LITERAL = new AnnotationLiteral<Expensive>() {
    };
    @SuppressWarnings("serial")
    private static Annotation SPARKLY_LITERAL = new AnnotationLiteral<Sparkly>() {
    };

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EnterpriseProducerMethodSpecializationTest.class).build();
    }

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = DIRECT_AND_INDIRECT_SPECIALIZATION_EE, id = "ia"), @SpecAssertion(section = DECLARING_PRODUCER_METHOD_EE, id = "c"),
            @SpecAssertion(section = PRODUCER_METHOD_EE, id = "aa") })
    public void testSpecializingProducerMethod() {

        Set<Bean<Necklace>> expensiveNecklaceBeans = getBeans(Necklace.class, EXPENSIVE_LITERAL);
        // There is only one bean for type Necklace and qualifier Expensive
        assertEquals(expensiveNecklaceBeans.size(), 1);

        Bean<Necklace> expensiveNecklaceBean = expensiveNecklaceBeans.iterator().next();

        // Check types of specializing bean
        Set<Type> expensiveNecklaceBeanTypes = expensiveNecklaceBean.getTypes();
        assertEquals(expensiveNecklaceBeanTypes.size(), 3);
        assertTrue(typeSetMatches(expensiveNecklaceBeanTypes, Object.class, Product.class, Necklace.class));

        // Check qualifiers of specializing bean
        Set<Annotation> expensiveNecklaceQualifiers = expensiveNecklaceBean.getQualifiers();
        assertEquals(expensiveNecklaceQualifiers.size(), 4);
        assertTrue(annotationSetMatches(expensiveNecklaceQualifiers, Expensive.class, Sparkly.class, Any.class, Named.class));

        // There is only one bean for type Necklace and qualifier Sparkly
        Set<Bean<Necklace>> sparklyNecklaceBeans = getBeans(Necklace.class, SPARKLY_LITERAL);
        assertEquals(sparklyNecklaceBeans.size(), 1);
        Bean<Necklace> sparklyBean = sparklyNecklaceBeans.iterator().next();
        // Check EL name of specializing bean
        assertEquals(sparklyBean.getName(), "expensiveGift");
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = DIRECT_AND_INDIRECT_SPECIALIZATION_EE, id = "ia") })
    public void testSpecializingBeanInjection(@Expensive Product expensiveProduct, @Default Product plainProduct) {
        assertTrue(expensiveProduct instanceof Necklace);
        assertEquals(expensiveProduct.getPrice(), 10);
        assertEquals(plainProduct.getPrice(), 0);
    }

}
