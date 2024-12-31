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

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_ALTERNATIVES_APPLICATION_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.tests.alternative.selection.Alpha;
import org.jboss.cdi.tck.tests.alternative.selection.Bravo;
import org.jboss.cdi.tck.tests.alternative.selection.Charlie;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 
 * EAR deployment with 1 library and 1 war:
 * <ul>
 * <li>ear lib - contains {@link Service} and a simple service implementation {@link PojoService}</li>
 * <li>war - contains {@link EnterpriseService} alternative with priority 1000, should be visible for the war only</li>
 * </ul>
 * 
 * Expected results:
 * <ul>
 * <li>{@link EnterpriseService} is available for injection in beans in war only</li>
 * </ul>
 * 
 * @author Matej Briskar
 * 
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseSelectedAlternative02Test extends AbstractTest {

    @Deployment
    public static EnterpriseArchive createTestArchive() {

        EnterpriseArchive enterpriseArchive = SelectedAlternativeTestUtil.createEnterpriseBuilderBase()
        // A - default EJB jar
                .withTestClassDefinition(EnterpriseSelectedAlternative02Test.class)
                // C - lib visible to all
                .withBeanLibrary(Bravo.class, Service.class, PojoService.class).noDefaultWebModule().build();

        // E - not visible for AC
        WebArchive bazWebArchive = SelectedAlternativeTestUtil.createBuilderBase().notTestArchive()
                .withClasses(Charlie.class, EnterpriseService.class, EnterpriseSelectedAlternative02Test.class).withBeanLibrary(Alpha.class).build();
        enterpriseArchive.addAsModule(bazWebArchive);

        return enterpriseArchive;
    }

    @Inject
    Alpha alpha;

    @Inject
    Bravo bravo;

    @Inject
    Charlie charlie;

    @Test(groups = JAVAEE_FULL)
    @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_APPLICATION_EE, id = "ab")
    public void testAlternativeSessionBeanSelected() {
        assertNotNull(alpha);
        assertNotNull(bravo);
        assertNotNull(charlie);

        assertEquals(alpha.assertAvailable(Service.class).getId(), EnterpriseService.class.getName());
        // EnterpriseService is not visible to bravo
        assertEquals(bravo.assertAvailable(Service.class).getId(), PojoService.class.getName());
        assertEquals(charlie.assertAvailable(Service.class).getId(), EnterpriseService.class.getName());

    }

}
