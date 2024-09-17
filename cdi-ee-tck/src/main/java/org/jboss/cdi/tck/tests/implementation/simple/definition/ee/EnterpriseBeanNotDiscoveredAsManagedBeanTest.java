/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.implementation.simple.definition.ee;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.WHAT_CLASSES_ARE_BEANS_EE;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.implementation.simple.definition.ClovenHoved;
import org.jboss.cdi.tck.tests.implementation.simple.definition.Sheep;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseBeanNotDiscoveredAsManagedBeanTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(EnterpriseBeanNotDiscoveredAsManagedBeanTest.class)
                .withClasses(EnterpriseBeanObserver.class, Sheep.class, MockEnterpriseBean.class, ClovenHoved.class)
                .withExtension(EnterpriseBeanObserver.class).build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = WHAT_CLASSES_ARE_BEANS_EE, id = "a")
    public void testClassesImplementingEnterpriseBeanInterfaceNotDiscoveredAsSimpleBean() {
        assert !EnterpriseBeanObserver.observedEnterpriseBean;
        assert EnterpriseBeanObserver.observedAnotherBean;
    }

}
