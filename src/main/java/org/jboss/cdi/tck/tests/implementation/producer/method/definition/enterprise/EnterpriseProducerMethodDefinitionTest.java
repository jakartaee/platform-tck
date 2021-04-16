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
package org.jboss.cdi.tck.tests.implementation.producer.method.definition.enterprise;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.MEMBER_LEVEL_INHERITANCE_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import jakarta.enterprise.inject.UnsatisfiedResolutionException;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseProducerMethodDefinitionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EnterpriseProducerMethodDefinitionTest.class).build();
    }

    @Test(groups = INTEGRATION, expectedExceptions = UnsatisfiedResolutionException.class)
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE_EE, id = "dd")
    public void testNonStaticProducerMethodNotInheritedBySpecializingSubclass() {
        assertEquals(getBeans(Egg.class, new AnnotationLiteral<Yummy>() {
        }).size(), 0);
        getContextualReference(Egg.class, new AnnotationLiteral<Yummy>() {
        }).getMother();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE_EE, id = "dd")
    public void testNonStaticProducerMethodNotInherited() {
        assertEquals(getBeans(Apple.class, new AnnotationLiteral<Yummy>() {
        }).size(), 1);
        assertTrue(getContextualReference(Apple.class, new AnnotationLiteral<Yummy>() {
        }).getTree() instanceof AppleTree);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE_EE, id = "dj")
    public void testNonStaticProducerMethodNotIndirectlyInherited() {
        Set<Bean<Pear>> beans = getBeans(Pear.class, new AnnotationLiteral<Yummy>() {
        });
        assertEquals(beans.size(), 2);
    }
}
