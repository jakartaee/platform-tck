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

package org.jboss.cdi.tck.tests.lookup.injection.enterprise.chain;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.INJECTION_AND_RESOLUTION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 
 * @author Martin Kouba
 * @see <a>CDITCK-15</a>
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class SessionBeanInjectionChainTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(SessionBeanInjectionChainTest.class).build();
    }

    @Inject
    Qux qux;

    @Test
    @SpecAssertion(section = INJECTION_AND_RESOLUTION, id = "b")
    public void testChainOfSessionBeans() {
        assertNotNull(qux);
        assertEquals(qux.ping(0), 4);
        assertEquals(qux.getBaz().ping(0), 3);
        Foo foo = qux.getBaz().getBar().getFoo();
        assertNotNull(foo);
        assertEquals(foo.getBaz().ping(0), 3);
        assertEquals(foo.getBaz().getBar().ping(0), 2);
        assertEquals(foo.getBaz().getQux().ping(0), 4);
    }

}
