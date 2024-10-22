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
package org.jboss.cdi.tck.tests.definition.scope.enterprise;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.TYPE_LEVEL_INHERITANCE_EE;

import jakarta.enterprise.context.RequestScoped;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseScopeDefinitionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EnterpriseScopeDefinitionTest.class).build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE_EE, id = "be")
    public void testScopeTypeDeclaredInheritedIsInherited() throws Exception {
        assert getBeans(BorderCollieLocal.class).iterator().next().getScope().equals(RequestScoped.class);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE_EE, id = "bea")
    public void testScopeTypeNotDeclaredInheritedIsNotInherited() throws Exception {
        assert !getBeans(SiameseLocal.class).iterator().next().getScope().equals(FooScoped.class);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE_EE, id = "bh")
    public void testScopeTypeDeclaredInheritedIsIndirectlyInherited() {
        assert getBeans(EnglishBorderCollieLocal.class).iterator().next().getScope().equals(RequestScoped.class);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE_EE, id = "bha")
    public void testScopeTypeNotDeclaredInheritedIsNotIndirectlyInherited() {
        assert !getBeans(BengalTigerLocal.class).iterator().next().getScope().equals(FooScoped.class);
    }
}
