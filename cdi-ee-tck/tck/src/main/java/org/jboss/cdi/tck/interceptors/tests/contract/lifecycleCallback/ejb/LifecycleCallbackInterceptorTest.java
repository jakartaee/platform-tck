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
package org.jboss.cdi.tck.interceptors.tests.contract.lifecycleCallback.ejb;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INT_METHODS_FOR_LIFECYCLE_EVENT_CALLBACKS;
import static org.testng.Assert.assertTrue;

@SpecVersion(spec = "interceptors", version = "1.2")
public class LifecycleCallbackInterceptorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(LifecycleCallbackInterceptorTest.class)
                .build();
    }

    @SuppressWarnings("unchecked")
    private <T extends Animal> void createAndDestroyInstance(Class<T> clazz) {
        Bean<T> bean = getUniqueBean(clazz);
        CreationalContext<T> ctx = getCurrentManager().createCreationalContext(bean);
        T instance = (T) getCurrentManager().getReference(bean, clazz, ctx);
        instance.foo(); // invoke method so that the instance is actually created
        // destroy the instance
        bean.destroy(instance, ctx);
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertion(section = INT_METHODS_FOR_LIFECYCLE_EVENT_CALLBACKS, id = "h")
    public void testLifecycleCallbackInterceptorTransactionContext() {
        createAndDestroyInstance(Cat.class);
        // checks are done in CatInterceptor
        assertTrue(CatInterceptor.pcCalled);
        assertTrue(CatInterceptor.pdCalled);
    }
}
