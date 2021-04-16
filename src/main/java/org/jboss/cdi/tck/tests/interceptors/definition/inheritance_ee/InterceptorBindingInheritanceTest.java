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
package org.jboss.cdi.tck.tests.interceptors.definition.inheritance_ee;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.MEMBER_LEVEL_INHERITANCE;
import static org.jboss.cdi.tck.cdi.Sections.MEMBER_LEVEL_INHERITANCE_EE;
import static org.jboss.cdi.tck.cdi.Sections.TYPE_LEVEL_INHERITANCE;
import static org.jboss.cdi.tck.cdi.Sections.TYPE_LEVEL_INHERITANCE_EE;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Test interceptor binding inheritance.
 * 
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class InterceptorBindingInheritanceTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(InterceptorBindingInheritanceTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).getOrCreateInterceptors()
                                .clazz(SquirrelInterceptor.class.getName(), WoodpeckerInterceptor.class.getName()).up())
                .build();
    }

    private String squirrel = SquirrelInterceptor.class.getName();
    private String woodpecker = WoodpeckerInterceptor.class.getName();


    @Test(groups = INTEGRATION, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = TYPE_LEVEL_INHERITANCE_EE, id = "an"), @SpecAssertion(section = TYPE_LEVEL_INHERITANCE_EE, id = "ana") })
    public void testInterceptorBindingDirectlyInheritedFromSessionBean(ForgetMeNot forgetMeNot) throws Exception {
        forgetMeNot.pong();
        assertTrue(forgetMeNot.inspectedBy(squirrel));
        assertFalse(forgetMeNot.inspectedBy(woodpecker));
    }

    @Test(groups = INTEGRATION, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = TYPE_LEVEL_INHERITANCE_EE, id = "ar"), @SpecAssertion(section = TYPE_LEVEL_INHERITANCE_EE, id = "ara") })
    public void testInterceptorBindingIndirectlyInheritedFromSessionBean(@European ForgetMeNot woodForgetMeNot)
            throws Exception {
        woodForgetMeNot.pong();
        assertTrue(woodForgetMeNot instanceof WoodForgetMeNot);
        assertTrue(woodForgetMeNot.inspectedBy(squirrel));
        assertFalse(woodForgetMeNot.inspectedBy(woodpecker));
    }

    @Test(groups = INTEGRATION, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE_EE, id = "kb")
    public void testMethodInterceptorBindingDirectlyInheritedFromSessionBean(Grass grass) {
        grass.pong();
        assertTrue(grass.inspectedBy(squirrel));
    }

    @Test(groups = INTEGRATION, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE_EE, id = "kd")
    public void testMethodInterceptorBindingIndirectlyInheritedFromSessionBean(@Culinary Grass waterChestnut) {
        waterChestnut.pong();
        assertTrue(waterChestnut instanceof WaterChestnut);
        assertTrue(waterChestnut.inspectedBy(squirrel));
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE, id = "ka")
    public void testMethodInterceptorBindingDirectlyNotInheritedFromManagedBean(Shrub shrub) {
        shrub.pong();
        assertFalse(shrub.inspectedBy(squirrel));
    }

    @Test(groups = INTEGRATION, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE_EE, id = "kb")
    public void testMethodInterceptorBindingDirectlyNotInheritedFromSessionBean(Cactus cactus) {
        cactus.pong();
        assertFalse(cactus.inspectedBy(squirrel));
    }

    @Test(groups = INTEGRATION, dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE_EE, id = "kd")
    public void testMethodInterceptorBindingIndirectlyNotInheritedFromSessionBean(@Culinary Cactus opuncia) {
        opuncia.pong();
        assertTrue(opuncia instanceof Opuncia);
        assertFalse(opuncia.inspectedBy(squirrel));
    }
}
