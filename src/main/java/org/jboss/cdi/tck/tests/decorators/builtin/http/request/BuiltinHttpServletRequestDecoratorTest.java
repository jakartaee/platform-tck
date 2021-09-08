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

package org.jboss.cdi.tck.tests.decorators.builtin.http.request;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.DECORATOR_BEAN_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;

import jakarta.enterprise.inject.spi.Decorator;
import jakarta.inject.Inject;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.full.decorators.AbstractDecoratorTest;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * @author Martin Kouba
 * 
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class BuiltinHttpServletRequestDecoratorTest extends AbstractDecoratorTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(BuiltinHttpServletRequestDecoratorTest.class)
                .withClass(AbstractDecoratorTest.class)
                .withBeansXml(
                        new BeansXml().decorators(HttpServletRequestDecorator1.class, HttpServletRequestDecorator2.class)).build();
    }

    @Inject
    HttpServletRequest httpServletRequest;

    @SuppressWarnings("unchecked")
    @Test
    @SpecAssertions({ @SpecAssertion(section = DECORATOR_BEAN_EE, id = "aci") })
    public void testDecoratorIsResolved() {
        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(
                Collections.<Type> singleton(HttpServletRequest.class));
        assertEquals(2, decorators.size());
        for (Decorator<?> decorator : decorators) {
            assertEquals(decorator.getDecoratedTypes(),
                    new HashSet<Type>(Arrays.asList(HttpServletRequest.class, ServletRequest.class)));
            assertEquals(decorator.getDelegateType(), HttpServletRequest.class);
        }
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECORATOR_BEAN_EE, id = "aci") })
    public void testDecoratorIsInvoked() {
        assertNull(httpServletRequest.getSession());
        assertEquals(httpServletRequest.getLocale(), Locale.SIMPLIFIED_CHINESE);
        // Decorator chain
        assertNull(httpServletRequest.getCookies());
        assertFalse(httpServletRequest.isUserInRole("Blabla"));
    }
}
