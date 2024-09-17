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

package org.jboss.cdi.tck.tests.alternative.selection.resource;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_ALTERNATIVES_APPLICATION;
import static org.jboss.cdi.tck.tests.alternative.selection.SelectedAlternativeTestUtil.createBuilderBase;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.tests.alternative.selection.Alpha;
import org.jboss.cdi.tck.tests.alternative.selection.Bravo;
import org.jboss.cdi.tck.tests.alternative.selection.Charlie;
import org.jboss.cdi.tck.tests.alternative.selection.resource.ProductionReady.ProductionReadyLiteral;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * The simplest possible scenario - test alternative resource is selected for the entire application, no deselection, no
 * priority ordering during resolution.
 *
 * WAR deployment with 2 libraries:
 * <ul>
 * <li>WEB-INF/classes - alpha - does not declare any alternative</li>
 * <li>lib 1 - bravo - declares {@link BravoResourceProducer} alternative selected for the app with priority 1000</li>
 * <li>lib 2 - charlie - does not declare any alternative</li>
 * </ul>
 *
 * Expected result: {@link BravoResourceProducer} resource is available for injection in all bean archives
 *
 * @author Martin Kouba
 *
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class ResourceAlternative01Test extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return createBuilderBase()
                .withTestClass(ResourceAlternative01Test.class)
                .withLibrary(ProductionReady.class)
                .withClasses(Alpha.class)
                .withBeanLibrary(Bravo.class, BravoResourceProducer.class)
                .withBeanLibrary(Charlie.class)
                .withWebXml(
                        Descriptors.create(WebAppDescriptor.class).createEnvEntry().envEntryName("test1")
                                .envEntryType("java.lang.String").envEntryValue("hello").up()).build();
    }

    @Inject
    Alpha alpha;

    @Inject
    Bravo bravo;

    @Inject
    Charlie charlie;

    @Test(groups = { INTEGRATION })
    @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_APPLICATION, id = "bc")
    public void testAlternativeResourceSelected() {
        alpha.assertAvailable(String.class, ProductionReadyLiteral.INSTANCE);
        bravo.assertAvailable(String.class, ProductionReadyLiteral.INSTANCE);
        charlie.assertAvailable(String.class, ProductionReadyLiteral.INSTANCE);
    }

}
