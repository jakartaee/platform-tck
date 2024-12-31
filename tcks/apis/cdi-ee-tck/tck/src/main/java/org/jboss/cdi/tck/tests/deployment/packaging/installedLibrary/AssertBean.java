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

package org.jboss.cdi.tck.tests.deployment.packaging.installedLibrary;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.lang.annotation.Annotation;

import jakarta.enterprise.inject.AmbiguousResolutionException;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

public abstract class AssertBean {

    @Inject
    @Any
    Instance<Object> instance;

    @Inject
    BeanManager beanManager;

    /**
     * Asserts that typesafe resolution fails - multiple beans are eligible for injection.
     *
     * Don't use this method until CDI-331 is resolved.
     *
     * @param beanType
     * @param qualifiers
     */
    public <T> void assertUnsatisfied(Class<T> beanType, Annotation... qualifiers) {

        assertEquals(beanManager.getBeans(beanType, qualifiers).size(), 0);

        Instance<T> subtypeInstance = instance.select(beanType, qualifiers);
        assertTrue(subtypeInstance.isUnsatisfied());
    }

    /**
     * Asserts that typesafe resolution fails - no bean is eligible for injection.
     *
     * Don't use this method until CDI-331 is resolved.
     *
     * @param beanType
     * @param qualifiers
     */
    public <T> void assertAmbiguous(Class<T> beanType, Annotation... qualifiers) {

        try {
            beanManager.resolve(beanManager.getBeans(beanType, qualifiers));
            fail("AmbiguousResolutionException not thrown");
        } catch (AmbiguousResolutionException e) {
            // Expected
        }

        Instance<T> subtypeInstance = instance.select(beanType, qualifiers);
        assertTrue(subtypeInstance.isAmbiguous());
    }

    /**
     * Assert that a bean with given type and qualifiers is available for injection.
     *
     * @param beanType
     * @param qualifiers
     * @return the bean instance
     */
    public <T> T assertAvailable(Class<T> beanType, Annotation... qualifiers) {

        assertNotNull(beanManager.resolve(beanManager.getBeans(beanType, qualifiers)));

        Instance<T> beanTypeInstance = instance.select(beanType, qualifiers);
        T beanInstance = beanTypeInstance.get();
        assertNotNull(beanInstance);

        return beanInstance;
    }

}
