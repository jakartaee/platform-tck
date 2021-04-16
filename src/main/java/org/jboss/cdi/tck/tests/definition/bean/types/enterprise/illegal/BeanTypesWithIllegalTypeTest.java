/*
 * JBoss, Home of Professional Open Source
 * Copyright 2017, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.definition.bean.types.enterprise.illegal;

import static org.jboss.cdi.tck.cdi.Sections.SESSION_BEAN_TYPES;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.TestGroups;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@Test(groups = TestGroups.INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class BeanTypesWithIllegalTypeTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(BeanTypesWithIllegalTypeTest.class)
                .build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SESSION_BEAN_TYPES, id = "e") })
    public void sessionBeanTypesContainsOnlyLegalTypes() {

        Bean<Bird> birdBean = getUniqueBean(Bird.class);
        for (Type type : birdBean.getTypes()) {
            if (type instanceof ParameterizedType) {
                assertNotEquals(((ParameterizedType) type).getRawType(), AnimalHolder.class);
            }
        }
        assertEquals(birdBean.getTypes().size(), 2);
    }

    @Test(enabled = false)
    // disabled due to CDITCK-575 and marked as testable false
    // @SpecAssertions({ @SpecAssertion(section = RESOURCE_TYPES, id = "b") })
    public void resourceBeanTypesContainsOnlyLegalTypes() {

        Bean<Dog> dogBean = getUniqueBean(Dog.class, Produced.ProducedLiteral.INSTANCE);
        for (Type type : dogBean.getTypes()) {
            if (type instanceof ParameterizedType) {
                assertNotEquals(((ParameterizedType) type).getRawType(), AnimalHolder.class);
            }
        }
        assertEquals(dogBean.getTypes().size(), 2);
    }

}
