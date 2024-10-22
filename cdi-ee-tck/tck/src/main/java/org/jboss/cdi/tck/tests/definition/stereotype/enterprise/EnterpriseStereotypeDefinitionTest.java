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
package org.jboss.cdi.tck.tests.definition.stereotype.enterprise;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.TYPE_LEVEL_INHERITANCE_EE;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseStereotypeDefinitionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EnterpriseStereotypeDefinitionTest.class).build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE_EE, id = "am")
    public void testStereotypeDeclaredInheritedIsInherited() throws Exception {
        assert getBeans(BorderCollieLocal.class).iterator().next().getScope().equals(RequestScoped.class);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE_EE, id = "ama")
    public void testStereotypeNotDeclaredInheritedIsNotInherited() throws Exception {
        assert !getBeans(BarracudaLocal.class).iterator().next().getScope().equals(RequestScoped.class);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE_EE, id = "aq")
    public void testStereotypeDeclaredInheritedIsIndirectlyInherited() {
        assert getBeans(EnglishBorderCollieLocal.class).iterator().next().getScope().equals(RequestScoped.class);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE_EE, id = "aqa")
    public void testStereotypeNotDeclaredInheritedIsNotIndirectlyInherited() {
        assert !getBeans(TameBarracudaLocal.class).iterator().next().getScope().equals(RequestScoped.class);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE_EE, id = "hhj")
    public void testStereotypeScopeIsOverriddenByInheritedScope() {
        assert getBeans(ChihuahuaLocal.class).iterator().next().getScope().equals(SessionScoped.class);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE_EE, id = "hhk")
    public void testStereotypeScopeIsOverriddenByIndirectlyInheritedScope() {
        assert getBeans(MexicanChihuahuaLocal.class).iterator().next().getScope().equals(SessionScoped.class);
    }
}
