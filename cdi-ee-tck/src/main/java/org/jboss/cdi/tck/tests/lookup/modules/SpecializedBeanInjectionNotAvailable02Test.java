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
package org.jboss.cdi.tck.tests.lookup.modules;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.INTER_MODULE_INJECTION;
import static org.jboss.cdi.tck.cdi.Sections.SELECTION_EE;
import static org.testng.Assert.assertEquals;

import java.util.Set;

import jakarta.enterprise.inject.spi.Bean;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class SpecializedBeanInjectionNotAvailable02Test extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        WebArchive webArchive = new WebArchiveBuilder()
                .withTestClass(SpecializedBeanInjectionNotAvailable02Test.class)
                .withClasses(PaymentFoo.class, CashFoo.class, PaymentEjbFoo.class, CashEjbFoo.class)
                .withBeanLibrary(Foo.class, Bar.class, Enterprise.class, Standard.class)
                .withBeanLibrary(WebPaymentBar.class, WebPaymentEjbBar.class)
                .build();
        return webArchive;
    }

    @Inject
    @Standard
    Bar bar;

    @Inject
    @Enterprise
    Bar enterpriseBar;

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = INTER_MODULE_INJECTION, id = "l")
    public void testManagedBeanInjection() throws Exception {
        assertEquals(bar.ping(), 0);
        Set<Bean<PaymentFoo>> beans = getBeans(PaymentFoo.class);
        assertEquals(beans.size(), 1);
        assertEquals(beans.iterator().next().getBeanClass(), CashFoo.class);
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertion(section = SELECTION_EE, id = "k")
    public void testSessionBeanInjection() throws Exception {
        assertEquals(enterpriseBar.ping(), 0);
        Set<Bean<PaymentEjbFoo>> beans = getBeans(PaymentEjbFoo.class);
        assertEquals(beans.size(), 1);
        assertEquals(beans.iterator().next().getBeanClass(), CashEjbFoo.class);
    }

}