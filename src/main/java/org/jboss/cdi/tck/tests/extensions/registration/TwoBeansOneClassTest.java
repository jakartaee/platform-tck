/*
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.cdi.tck.tests.extensions.registration;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE;
import static org.jboss.cdi.tck.cdi.Sections.BEFORE_BEAN_DISCOVERY;
import static org.jboss.cdi.tck.cdi.Sections.INIT_EVENTS;
import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class TwoBeansOneClassTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(TwoBeansOneClassTest.class)
                .withLibrary(TwoBeansOneClassExtension.class, Beanie.class, BeanieType.class, BeanieTypeLiteral.class).build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({
        @SpecAssertion(section = INIT_EVENTS, id = "b"),
        @SpecAssertion(section = INIT_EVENTS, id = "bb"),
        @SpecAssertion(section = BEAN_ARCHIVE, id = "n"),
        @SpecAssertion(section = BEFORE_BEAN_DISCOVERY, id = "af"),
        @SpecAssertion(section = BEFORE_BEAN_DISCOVERY, id = "afa")})
    public void testTwoBeansWithOneBaseClass() {
        assertEquals(beanManager.getBeans(Beanie.class).size(), 0);
        assertEquals(beanManager.getBeans(Beanie.class, new BeanieTypeLiteral() {
            @Override
            public String value() {
                return "basic";
            }
        }).size(), 1);

        assertEquals(beanManager.getBeans(Beanie.class, new BeanieTypeLiteral() {
            @Override
            public String value() {
                return "propeller";
            }
        }).size(), 1);
    }
}
