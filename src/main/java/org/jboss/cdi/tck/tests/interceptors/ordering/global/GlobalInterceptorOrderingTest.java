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

package org.jboss.cdi.tck.tests.interceptors.ordering.global;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.ENABLED_INTERCEPTORS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Test interceptor enablement and ordering.
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class GlobalInterceptorOrderingTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(GlobalInterceptorOrderingTest.class)
                // WEB-INF/classes
                .withClasses(Dao.class, LegacyInterceptor1.class, LegacyInterceptor2.class, LegacyInterceptor3.class,
                        WebApplicationGlobalInterceptor1.class)
                .withBeansXml(new BeansXml().interceptors(LegacyInterceptor1.class, LegacyInterceptor2.class, LegacyInterceptor3.class))
                .withBeanLibrary(Transactional.class, AbstractInterceptor.class, Service.class,
                        GloballyEnabledInterceptor1.class, GloballyEnabledInterceptor3.class,
                        GloballyEnabledInterceptor4.class, GloballyEnabledInterceptor5.class)
                .withBeanLibrary(GloballyEnabledInterceptor2.class).build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "f") })
    public void testOrderingInWebInfClasses(Dao dao) {

        assertNotNull(dao);
        ActionSequence.reset();
        dao.ping();

        List<String> sequence = new ArrayList<String>();
        sequence.add(GloballyEnabledInterceptor5.class.getName());
        sequence.add(GloballyEnabledInterceptor1.class.getName());
        sequence.add(GloballyEnabledInterceptor2.class.getName());
        sequence.add(WebApplicationGlobalInterceptor1.class.getName());
        sequence.add(GloballyEnabledInterceptor3.class.getName());
        sequence.add(GloballyEnabledInterceptor4.class.getName());
        // Interceptors enabled using beans.xml
        sequence.add(LegacyInterceptor1.class.getName());
        sequence.add(LegacyInterceptor2.class.getName());
        sequence.add(LegacyInterceptor3.class.getName());

        assertEquals(ActionSequence.getSequenceData(), sequence);
    }

    @Test(groups = INTEGRATION, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "i") })
    public void testOrderingInLib(Service service) {

        assertNotNull(service);
        ActionSequence.reset();
        service.ping();

        List<String> sequence = new ArrayList<String>();
        sequence.add(GloballyEnabledInterceptor5.class.getName());
        sequence.add(GloballyEnabledInterceptor1.class.getName());
        sequence.add(GloballyEnabledInterceptor2.class.getName());
        sequence.add(WebApplicationGlobalInterceptor1.class.getName());
        sequence.add(GloballyEnabledInterceptor3.class.getName());
        sequence.add(GloballyEnabledInterceptor4.class.getName());

        assertEquals(ActionSequence.getSequenceData(), sequence);
    }
}
