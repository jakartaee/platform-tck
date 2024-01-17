/*
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.lookup.typesafe.resolution.interceptor.ejb;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_TYPES_EE;

import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;
import org.jboss.cdi.tck.tests.lookup.typesafe.resolution.Bird;

@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseResolutionByTypeTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(EnterpriseResolutionByTypeTest.class)
                .withClasses(Bird.class)
                .build();
    }

    @Test(groups = { INTEGRATION })
    @SpecAssertion(section = BEAN_TYPES_EE, id = "a")
    public void testBeanTypesOnSessionBean() {
        assert getBeans(CapercaillieLocal.class).size() == 1;
        assert getBeans(ScottishBirdLocal.class).isEmpty();
        Bean<CapercaillieLocal> bean = getUniqueBean(CapercaillieLocal.class);
        assert typeSetMatches(bean.getTypes(), CapercaillieLocal.class, Object.class);
    }

}
