/*
 * Copyright 2017, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.deployment.trimmed.enteprise;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.TestGroups;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Tomas Remes
 */
@Test(groups = TestGroups.INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseTrimmedBeanArchiveTest extends AbstractTest {

    @Inject
    TestExtension extension;

    @Inject
    Instance<Bike> bikeInstance;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EnterpriseTrimmedBeanArchiveTest.class)
                .withExtension(TestExtension.class)
                .withBeansXml("beans.xml").build();
    }

    @Test
    @SpecAssertion(section = Sections.TRIMMED_BEAN_ARCHIVE_EE, id = "a")
    public void testDiscoveredBean() {
        assertEquals(extension.getVehiclePBAinvocations().get(), 1);
        Bean<MotorizedVehicle> vehicleBean = getUniqueBean(MotorizedVehicle.class);
        CreationalContext<MotorizedVehicle> cc = getCurrentManager().createCreationalContext(vehicleBean);
        MotorizedVehicle vehicle = (MotorizedVehicle) getCurrentManager().getReference(vehicleBean, MotorizedVehicle.class, cc);
        assertEquals(vehicle.start(), "car started");

        assertEquals(extension.getBikePBAinvocations().get(), 1);
        assertTrue(bikeInstance.isResolvable());
        assertTrue(bikeInstance.get().ping());
    }

}
