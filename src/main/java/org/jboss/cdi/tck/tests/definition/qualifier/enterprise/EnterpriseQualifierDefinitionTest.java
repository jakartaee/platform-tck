/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.cdi.tck.tests.definition.qualifier.enterprise;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.TYPE_LEVEL_INHERITANCE_EE;

import java.lang.annotation.Annotation;
import java.util.Set;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseQualifierDefinitionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EnterpriseQualifierDefinitionTest.class).build();
    }

    @SuppressWarnings("unchecked")
    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE_EE, id = "al")
    public void testQualifierDeclaredInheritedIsInherited() throws Exception {
        Set<? extends Annotation> qualifiers = getBeans(BorderCollieLocal.class, new HairyQualifier(false)).iterator().next()
                .getQualifiers();
        assert annotationSetMatches(qualifiers, Any.class, Hairy.class);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE_EE, id = "ala")
    public void testQualifierNotDeclaredInheritedIsNotInherited() throws Exception {
        assert getBeans(TameSkinnyHairlessCatLocal.class, new SkinnyQualifier()).size() == 0;
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE_EE, id = "ap")
    public void testQualifierDeclaredInheritedIsIndirectlyInherited() {
        Set<? extends Annotation> qualifiers = getBeans(EnglishBorderCollieLocal.class, new HairyQualifier(false)).iterator()
                .next().getQualifiers();
        assert annotationSetMatches(qualifiers, Any.class, Hairy.class);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_LEVEL_INHERITANCE_EE, id = "apa")
    public void testQualifierNotDeclaredInheritedIsNotIndirectlyInherited() {
        Set<Bean<FamousCatLocal>> beans = getBeans(FamousCatLocal.class, new SkinnyQualifier());
        assert beans.size() == 0;
    }
}
