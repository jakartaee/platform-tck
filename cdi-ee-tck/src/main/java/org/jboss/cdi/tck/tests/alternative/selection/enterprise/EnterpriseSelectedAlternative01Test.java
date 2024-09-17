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

package org.jboss.cdi.tck.tests.alternative.selection.enterprise;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_ALTERNATIVES_APPLICATION_EE;
import static org.jboss.cdi.tck.tests.alternative.selection.SelectedAlternativeTestUtil.createBuilderBase;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.tests.alternative.selection.Alpha;
import org.jboss.cdi.tck.tests.alternative.selection.Bravo;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * The simplest possible scenario - test session bean alternative selected for the entire application, no priority ordering during resolution.
 * 
 * WAR deployment with 2 libraries:
 * <ul>
 * <li>WEB-INF/classes - alpha - contains {@link EnterpriseService} alternative with priority 1000</li>
 * <li>lib 1 - bravo - contains {@link PojoService}</li>
 * </ul>
 * 
 * Expected results:
 * <ul>
 * <li>{@link EnterpriseService} is available for injection in all bean archives</li>
 * </ul>
 * 
 * @author Martin Kouba
 * 
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseSelectedAlternative01Test extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return createBuilderBase().withTestClass(EnterpriseSelectedAlternative01Test.class).withClasses(Service.class, EnterpriseService.class, Alpha.class)
                .withBeanLibrary(PojoService.class, Bravo.class).build();
    }

    @Inject
    Alpha alpha;

    @Inject
    Bravo bravo;

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_APPLICATION_EE, id = "ab")
    public void testAlternativeSessionBeanSelected() {
        assertNotNull(alpha);
        assertNotNull(bravo);

        assertEquals(alpha.assertAvailable(Service.class).getId(), EnterpriseService.class.getName());
        assertEquals(bravo.assertAvailable(Service.class).getId(), EnterpriseService.class.getName());
    }

}
