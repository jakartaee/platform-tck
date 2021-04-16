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
package org.jboss.cdi.tck.tests.context.passivating.broken.producer.method.enterprise;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_VALIDATION_EE;

import java.lang.annotation.Annotation;

import jakarta.enterprise.inject.IllegalProductException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseBeanWithIllegalDependencyTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EnterpriseBeanWithIllegalDependencyTest.class).build();
    }

    @Test
    @SpecAssertion(section = PASSIVATION_VALIDATION_EE, id = "fab")
    public void testFieldInjectionPointRequiringPassivationCapableDependency() {
        verify(FieldInjectionCorralBroken.class);
    }

    @Test
    @SpecAssertion(section = PASSIVATION_VALIDATION_EE, id = "fab")
    public void testSetterInjectionPointRequiringPassivationCapableDependency() {
        verify(SetterInjectionCorralBroken.class);
    }

    @Test
    @SpecAssertion(section = PASSIVATION_VALIDATION_EE, id = "fab")
    public void testConstructorInjectionPointRequiringPassivationCapableDependency() {
        verify(ConstructorInjectionCorralBroken.class);
    }

    @Test
    @SpecAssertion(section = PASSIVATION_VALIDATION_EE, id = "fab")
    public void testProducerMethodParamInjectionPointRequiringPassivationCapableDependency() {
        verify(Herd.class, British.BritishLiteral.INSTANCE);
    }

    private void verify(Class<? extends Ranch> clazz, Annotation... annotations) {
        try {
            getContextualReference(clazz, annotations).ping();
        } catch (Throwable t) {
            Assert.assertTrue(isThrowablePresent(IllegalProductException.class, t));
            return;
        }
        Assert.fail();

    }
}
