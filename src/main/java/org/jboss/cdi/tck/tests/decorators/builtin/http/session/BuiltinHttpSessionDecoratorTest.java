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

package org.jboss.cdi.tck.tests.decorators.builtin.http.session;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.DECORATOR_BEAN_EE;
import static org.jboss.cdi.tck.cdi.Sections.DECORATOR_RESOLUTION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.inject.spi.Decorator;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.decorators.AbstractDecoratorTest;
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
 * @author Martin Kouba
 *
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class BuiltinHttpSessionDecoratorTest extends AbstractDecoratorTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(BuiltinHttpSessionDecoratorTest.class)
                .withClass(AbstractDecoratorTest.class)
                .withBeansXml(
                        new BeansXml().decorators(HttpSessionDecorator1.class, HttpSessionDecorator2.class)).build();
    }

    @Inject
    HttpSession httpSession;

    @Inject
    HttpSessionObserver httpSessionObserver;

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = DECORATOR_BEAN_EE, id = "acj"), @SpecAssertion(section = DECORATOR_RESOLUTION, id = "aa") })
    public void testDecoratorIsResolved() {
        List<Decorator<?>> decorators = getCurrentManager().resolveDecorators(Collections.<Type> singleton(HttpSession.class));
        assertEquals(2, decorators.size());
        for (Decorator<?> decorator : decorators) {
            assertEquals(decorator.getDecoratedTypes(), Collections.<Type> singleton(HttpSession.class));
            assertEquals(decorator.getDelegateType(), HttpSession.class);
        }
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = DECORATOR_BEAN_EE, id = "acj") })
    public void testDecoratorIsInvoked() {
        httpSession.invalidate();
        assertTrue(httpSessionObserver.isDestroyed());
        assertTrue(httpSessionObserver.isDecorated());
        assertEquals(3, httpSession.getLastAccessedTime());
        assertEquals("bar", httpSession.getAttribute("foo"));
    }
}
