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

package org.jboss.cdi.tck.tests.lookup.injectionpoint.non.contextual;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.INJECTION_POINT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;

import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.inject.Inject;
import javax.naming.InitialContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class NonContextualInjectionPointTest extends AbstractTest {

    @Inject
    private Instance<Baz> baz;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(NonContextualInjectionPointTest.class).build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = INJECTION_POINT, id = "aaa")
    public void testNonContextualEjbInjectionPointGetBean() throws Exception {
        Bar bar = (Bar) new InitialContext().lookup("java:module/Bar");
        Bean<?> bean = bar.getFoo().getInjectionPoint().getBean();
        assertNull(bean);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = INJECTION_POINT, id = "aa")
    public void testContextualEjbInjectionPointGetBean() throws Exception {
        Bean<?> bean = baz.get().getFoo().getInjectionPoint().getBean();
        assertNotNull(bean);
        assertEquals(bean.getBeanClass(), Baz.class);
    }

}
