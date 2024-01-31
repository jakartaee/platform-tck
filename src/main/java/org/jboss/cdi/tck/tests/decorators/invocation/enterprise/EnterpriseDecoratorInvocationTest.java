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

package org.jboss.cdi.tck.tests.decorators.invocation.enterprise;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BIZ_METHOD_EE;
import static org.jboss.cdi.tck.cdi.Sections.DECORATORS_EE;
import static org.jboss.cdi.tck.cdi.Sections.DECORATOR_BEAN_EE;
import static org.jboss.cdi.tck.cdi.Sections.DELEGATE_ATTRIBUTE;
import static org.testng.Assert.assertEquals;

import jakarta.enterprise.inject.spi.Decorator;
import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseDecoratorInvocationTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(EnterpriseDecoratorInvocationTest.class)
                .withBeansXml(new BeansXml()
                        .interceptors(FooInterceptor.class)
                        .decorators(FooBusinessDecorator1.class, FooBusinessDecorator2.class, BarBusinessDecorator.class))
                .build();
    }

    @Inject
    FooBusiness foo;

    @Test
    @SpecAssertions({ @SpecAssertion(section = BIZ_METHOD_EE, id = "d"), @SpecAssertion(section = DECORATOR_BEAN_EE, id = "d"),
            @SpecAssertion(section = DECORATORS_EE, id = "e"), @SpecAssertion(section = DELEGATE_ATTRIBUTE, id = "cc"),
            @SpecAssertion(section = DECORATORS_EE, id = "d"), @SpecAssertion(section = DECORATORS_EE, id = "g"),
            @SpecAssertion(section = DECORATORS_EE, id = "f") })
    public void testContextualDecorated() throws Exception {

        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(Collections.<Type> singleton(FooBusiness.class));
        assertEquals(decorators.size(), 2);

        ActionSequence.reset();

        // Test actual decoration
        assertEquals(foo.businessOperation1(), Foo.class.getName() + FooBusinessDecorator2.class.getName()
                + FooBusinessDecorator1.class.getName());

        // Decorators are called after interceptors and decorator that occures earlier in the list is called first
        List<String> sequence = ActionSequence.getSequenceData();
        assertEquals(sequence.size(), 3);
        assertEquals(sequence.get(0), FooInterceptor.class.getName());
        assertEquals(sequence.get(1), FooBusinessDecorator1.class.getName());
        assertEquals(sequence.get(2), FooBusinessDecorator2.class.getName());

        // Only businessOperation1() is decorated
        assertEquals(foo.businessOperation2(), Foo.class.getName());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BIZ_METHOD_EE, id = "i"), @SpecAssertion(section = DECORATOR_BEAN_EE, id = "d"),
            @SpecAssertion(section = DECORATORS_EE, id = "e") })
    public void testNonContextualDecorated() throws Exception {

        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(Collections.<Type> singleton(BarBusiness.class));
        assertEquals(decorators.size(), 1);
        assertEquals(decorators.iterator().next().getDecoratedTypes().size(), 1);

        ActionSequence.reset();

        // Test actual decoration
        assertEquals(foo.invokeBarBusinessOperation1(), Bar.class.getName() + BarBusinessDecorator.class.getName());

        List<String> sequence = ActionSequence.getSequenceData();
        assertEquals(sequence.size(), 2);
        // invokeBarBusinessOperation1() is intercepted
        assertEquals(sequence.get(0), FooInterceptor.class.getName());
        assertEquals(sequence.get(1), BarBusinessDecorator.class.getName());
    }
}
