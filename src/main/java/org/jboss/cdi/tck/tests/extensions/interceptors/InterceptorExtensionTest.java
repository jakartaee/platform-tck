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
package org.jboss.cdi.tck.tests.extensions.interceptors;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BEFORE_BEAN_DISCOVERY;
import static org.jboss.cdi.tck.cdi.Sections.INIT_EVENTS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests that interceptors registered via the SPI work correctly.
 * 
 * <p>
 * This test was originally part of Weld test suite.
 * <p>
 * 
 * @author Stuart Douglas stuart@baileyroberts.com.au
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class InterceptorExtensionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {

        return new WebArchiveBuilder()
                .withTestClass(InterceptorExtensionTest.class)
                .withClasses(Marathon.class, NumberSource.class, WordSource.class)
                .withLibrary(SuffixingInterceptor.class, IncrementingInterceptor.class, LifecycleInterceptor.class, Suffixed.class, FullMarathon.class, Incremented.class,
                        InterceptorExtension.class)
                .withBeansXml(
                        new BeansXml().interceptors(SuffixingInterceptor.class, IncrementingInterceptor.class, LifecycleInterceptor.class))
                .build();
    }

    @Inject
    private BeanManager beanManager;

    @Inject
    private NumberSource numberSource;
    
    @Inject
    private WordSource wordSource;

    @Test
    @SpecAssertions({ @SpecAssertion(section = INIT_EVENTS, id = "b"), @SpecAssertion(section = INIT_EVENTS, id = "bb"),
            @SpecAssertion(section = BEFORE_BEAN_DISCOVERY, id = "ae") })
    public void testInterceptorAddedByClass() {
        assertEquals(numberSource.value(), 2);
        assertTrue(IncrementingInterceptor.isDoAroundCalled());
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INIT_EVENTS, id = "b"), @SpecAssertion(section = INIT_EVENTS, id = "bb"),
            @SpecAssertion(section = BEFORE_BEAN_DISCOVERY, id = "ae") })
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void testLifecycleInterceptor() {

        Bean bean = beanManager.getBeans(Marathon.class).iterator().next();
        CreationalContext creationalContext = beanManager.createCreationalContext(bean);
        Marathon marathon = (Marathon) bean.create(creationalContext);

        assertTrue(LifecycleInterceptor.isPostConstructCalled());
        assertEquals(marathon.getLength(), 42);
        bean.destroy(marathon, creationalContext);
        assertTrue(LifecycleInterceptor.isPreDestroyCalled());
    }
    
    @Test
    @SpecAssertions({ @SpecAssertion(section = INIT_EVENTS, id = "b"), @SpecAssertion(section = INIT_EVENTS, id = "bb"),
            @SpecAssertion(section = BEFORE_BEAN_DISCOVERY, id = "aea") })
    public void testInterceptorAddedByAnnotatedType() {
        assertEquals(wordSource.getWord(), WordSource.word);
        assertEquals(wordSource.getWordWithSuffix(), WordSource.wordWithSuffix);
        assertTrue(SuffixingInterceptor.isDoAroundCalled());
    }
    
}
