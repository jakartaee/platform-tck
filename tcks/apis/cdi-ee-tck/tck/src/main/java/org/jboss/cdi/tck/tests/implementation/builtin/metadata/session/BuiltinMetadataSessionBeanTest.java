/*
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.implementation.builtin.metadata.session;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_METADATA;
import static org.testng.Assert.assertEquals;

import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.Decorator;
import jakarta.enterprise.inject.spi.InterceptionType;
import jakarta.enterprise.inject.spi.Interceptor;
import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.lang.reflect.Type;
import java.util.Collections;

/**
 * @author Tomas Remes
 */
@Test(groups = JAVAEE_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class BuiltinMetadataSessionBeanTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(BuiltinMetadataSessionBeanTest.class)
                .withClasses(YoghurtInterceptor.class, Frozen.class)
                .withBeansXml(
                        new BeansXml().interceptors(YoghurtInterceptor.class).decorators(BakeryProductDecorator.class)).build();
    }

    @Inject
    private Yoghurt yoghurt;

    @Inject
    private BakeryProduct bakery;

    @Test
    @SpecAssertions({ @SpecAssertion(section = BEAN_METADATA, id = "b"), @SpecAssertion(section = BEAN_METADATA, id = "d"),
            @SpecAssertion(section = BEAN_METADATA, id = "f") })
    public void testInterceptorMetadata() {

        Interceptor<?> interceptor = getCurrentManager()
                .resolveInterceptors(InterceptionType.AROUND_INVOKE, new Frozen.Literal()).iterator().next();
        Bean<?> sessionBean = getUniqueBean(Yoghurt.class);
        YoghurtInterceptor fatYoghurtInterceptor = yoghurt.getInterceptorInstance();
        assertEquals(interceptor, fatYoghurtInterceptor.getBean());
        assertEquals(interceptor, fatYoghurtInterceptor.getInterceptor());
        assertEquals(sessionBean, fatYoghurtInterceptor.getInterceptedBean());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BEAN_METADATA, id = "c"), @SpecAssertion(section = BEAN_METADATA, id = "e"),
            @SpecAssertion(section = BEAN_METADATA, id = "f") })
    public void testDecoratorMetadata() {
        Bean<?> sessionBean = getUniqueBean(BakeryProduct.class);
        Decorator<?> decoratorInstance = getCurrentManager().resolveDecorators(Collections.<Type>singleton(BakeryProduct.class))
                .iterator().next();
        BakeryProductDecorator bakeryDecorator = bakery.getDecoratorInstance();
        assertEquals(decoratorInstance, bakeryDecorator.getBean());
        assertEquals(decoratorInstance, bakeryDecorator.getDecorator());
        assertEquals(sessionBean, bakeryDecorator.getDecoratedBean());
    }

}
