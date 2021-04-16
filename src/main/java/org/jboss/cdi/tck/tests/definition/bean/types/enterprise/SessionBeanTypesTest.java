/*
 * JBoss, Home of Professional Open Source
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

package org.jboss.cdi.tck.tests.definition.bean.types.enterprise;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.SESSION_BEAN_TYPES;
import static org.jboss.cdi.tck.util.Assert.assertTypeSetMatches;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.enterprise.util.TypeLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 * @author Matus Abaffy
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class SessionBeanTypesTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(SessionBeanTypesTest.class).build();
    }

    @SuppressWarnings("serial")
    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = SESSION_BEAN_TYPES, id = "aa"),
            @SpecAssertion(section = SESSION_BEAN_TYPES, id = "ba"), @SpecAssertion(section = SESSION_BEAN_TYPES, id = "c") })
    public void testGenericHierarchyBeanTypes() {

        // Generic class inheritance with abstract class and interface
        Bean<Vulture> vultureBean = getUniqueBean(Vulture.class);
        assertNotNull(vultureBean);
        // Object, Bird<Integer>, Vulture
        assertEquals(vultureBean.getTypes().size(), 3);
        assertTypeSetMatches(vultureBean.getTypes(), Object.class, Vulture.class, new TypeLiteral<Bird<Integer>>() {
        }.getType());

        // Generic class inheritance with two interfaces
        TypeLiteral<Mammal<String>> mammalLiteral = new TypeLiteral<Mammal<String>>() {
        };
        Bean<Mammal<String>> tigerBean = getUniqueBean(mammalLiteral);
        assertNotNull(tigerBean);
        // Object, Animal<String>, Mammal<String>
        assertEquals(tigerBean.getTypes().size(), 3);
        assertTypeSetMatches(tigerBean.getTypes(), Object.class, new TypeLiteral<Animal<String>>() {
        }.getType(), mammalLiteral.getType());

        // session bean with both local business interface and no-interface view
        Bean<LegendaryCreature> creatureBean = getUniqueBean(LegendaryCreature.class);
        assertNotNull(creatureBean);
        // Object, LegendaryCreature, LegendaryLocal, Creature
        assertEquals(creatureBean.getTypes().size(), 4);
        assertTypeSetMatches(creatureBean.getTypes(), Object.class, LegendaryCreature.class, LegendaryLocal.class,
                Creature.class);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = SESSION_BEAN_TYPES, id = "aa"),
            @SpecAssertion(section = SESSION_BEAN_TYPES, id = "ba"), @SpecAssertion(section = SESSION_BEAN_TYPES, id = "c") })
    public void testSessionBeanWithBothLocalInterfaceAndNoInterfaceView() {
        // session bean with both local business interface and no-interface view
        Bean<LegendaryCreature> creatureBean = getUniqueBean(LegendaryCreature.class);
        assertNotNull(creatureBean);
        // Object, LegendaryCreature, LegendaryLocal, Creature
        assertEquals(creatureBean.getTypes().size(), 4);
        assertTypeSetMatches(creatureBean.getTypes(), Object.class, LegendaryCreature.class, LegendaryLocal.class,
                Creature.class);
    }

    @SuppressWarnings("serial")
    @Test(groups = INTEGRATION)
    @SpecAssertion(section = SESSION_BEAN_TYPES, id = "ba")
    public void testSessionBeanExtendingSessionBeanWithLocalClientView() {
        // no-interface view
        Bean<MockLoginActionBean> loginBean = getUniqueBean(MockLoginActionBean.class, new AnnotationLiteral<Mock>() {
        });
        assertNotNull(loginBean);
        // MockLoginActionBean, LoginActionBean, Object
        assertEquals(loginBean.getTypes().size(), 3);
        assertTypeSetMatches(loginBean.getTypes(), Object.class, LoginActionBean.class, MockLoginActionBean.class);
    }
    
    @Test(groups = INTEGRATION)
    @SpecAssertion(section = SESSION_BEAN_TYPES, id = "ba")
    public void testSessionBeanWithNoInterfaceView(){
        Bean<Cobra> cobraBean = getUniqueBean(Cobra.class);
        assertNotNull(cobraBean);
        assertEquals(cobraBean.getTypes().size(), 3);
        assertTypeSetMatches(cobraBean.getTypes(), Object.class, Cobra.class, Snake.class);
    }
}
