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
package org.jboss.cdi.tck.tests.decorators.ordering.global;

import static org.jboss.cdi.tck.cdi.Sections.ENABLED_DECORATORS;
import static org.jboss.cdi.tck.cdi.Sections.ENABLED_DECORATORS_BEAN_ARCHIVE;
import static org.jboss.cdi.tck.cdi.Sections.ENABLED_DECORATORS_PRIORITY;
import static org.testng.Assert.assertEquals;

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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This test was originally part of the Weld test suite.
 * <p>
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class GlobalDecoratorOrderingTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {

        return new WebArchiveBuilder()
                .withTestClass(GlobalDecoratorOrderingTest.class)
                .withClasses(DecoratedImpl.class, LegacyDecorator1.class, LegacyDecorator2.class, LegacyDecorator3.class,
                        WebApplicationGlobalDecorator.class)
                .withBeansXml(new BeansXml().decorators(LegacyDecorator1.class, LegacyDecorator2.class, LegacyDecorator3.class))
                .withBeanLibrary(AbstractDecorator.class, Decorated.class, GloballyEnabledDecorator1.class,
                        GloballyEnabledDecorator2.class, GloballyEnabledDecorator3.class, GloballyEnabledDecorator4.class,
                        GloballyEnabledDecorator5.class).build();
    }

    @Inject
    private Decorated decorated;

    @Test
    @SpecAssertions({ @SpecAssertion(section = ENABLED_DECORATORS_PRIORITY, id = "a"),
            @SpecAssertion(section = ENABLED_DECORATORS_BEAN_ARCHIVE, id = "a"),
            @SpecAssertion(section = ENABLED_DECORATORS_PRIORITY, id = "b"),
            @SpecAssertion(section = ENABLED_DECORATORS, id = "c")})
    public void testDecoratorsInWebInfClasses() {

        List<String> expected = new ArrayList<String>();
        // 800
        expected.add(GloballyEnabledDecorator5.class.getSimpleName());
        // 995
        expected.add(GloballyEnabledDecorator1.class.getSimpleName());
        // 1005
        expected.add(GloballyEnabledDecorator2.class.getSimpleName());
        // 1008
        expected.add(WebApplicationGlobalDecorator.class.getSimpleName());
        // 1015
        expected.add(GloballyEnabledDecorator3.class.getSimpleName());
        // 1025
        expected.add(GloballyEnabledDecorator4.class.getSimpleName());
        // Decorators enabled using beans.xml
        expected.add(LegacyDecorator1.class.getSimpleName());
        expected.add(LegacyDecorator2.class.getSimpleName());
        expected.add(LegacyDecorator3.class.getSimpleName());
        // Bean itself
        expected.add(DecoratedImpl.class.getSimpleName());

        List<String> actual = new ArrayList<String>();
        decorated.getSequence(actual);
        assertEquals(actual, expected);
    }
}
