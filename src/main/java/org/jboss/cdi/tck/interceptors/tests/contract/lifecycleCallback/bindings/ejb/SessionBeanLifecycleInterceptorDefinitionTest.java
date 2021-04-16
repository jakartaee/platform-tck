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
package org.jboss.cdi.tck.interceptors.tests.contract.lifecycleCallback.bindings.ejb;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INT_METHODS_FOR_LIFECYCLE_EVENT_CALLBACKS;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INT_ORDERING_RULES;
import static org.testng.Assert.assertEquals;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "interceptors", version = "1.2")
public class SessionBeanLifecycleInterceptorDefinitionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(SessionBeanLifecycleInterceptorDefinitionTest.class)
                .withBeansXml(new BeansXml().interceptors(AirborneInterceptor.class, DestructionInterceptor.class))
                .build();
    }

    @Test(groups = { INTEGRATION })
    @SpecAssertion(section = INT_METHODS_FOR_LIFECYCLE_EVENT_CALLBACKS, id = "b")
    @SpecAssertion(section = INT_METHODS_FOR_LIFECYCLE_EVENT_CALLBACKS, id = "c")
    public void testLifecycleInterception() {

        ActionSequence.reset();

        Bean<Missile> bean = getUniqueBean(Missile.class);
        CreationalContext<Missile> ctx = getCurrentManager().createCreationalContext(bean);
        Missile missile = bean.create(ctx);
        missile.fire();
        bean.destroy(missile, ctx);

        assertEquals(ActionSequence.getSequenceSize("postConstruct"), 1);
        assertEquals(ActionSequence.getSequenceData("postConstruct").get(0), AirborneInterceptor.class.getSimpleName());
        assertEquals(ActionSequence.getSequenceSize("preDestroy"), 1);
        assertEquals(ActionSequence.getSequenceData("preDestroy").get(0), AirborneInterceptor.class.getSimpleName());
    }

    @Test(groups = { INTEGRATION })
    @SpecAssertion(section = INT_METHODS_FOR_LIFECYCLE_EVENT_CALLBACKS, id = "ea")
    @SpecAssertion(section = INT_METHODS_FOR_LIFECYCLE_EVENT_CALLBACKS, id = "eb")
    @SpecAssertion(section = INT_METHODS_FOR_LIFECYCLE_EVENT_CALLBACKS, id = "i")
    @SpecAssertion(section = INT_ORDERING_RULES, id = "i")
    public void testMultipleLifecycleInterceptors() {

        ActionSequence.reset();

        Bean<Rocket> bean = getUniqueBean(Rocket.class);
        CreationalContext<Rocket> ctx = getCurrentManager().createCreationalContext(bean);
        Rocket rocket = bean.create(ctx);
        rocket.fire();
        bean.destroy(rocket, ctx);

        ActionSequence postConstruct = ActionSequence.getSequence("postConstruct");
        postConstruct.assertDataEquals(AirborneInterceptor.class, SuperDestructionInterceptor.class,
                DestructionInterceptor.class, Weapon.class, Rocket.class);

        ActionSequence preDestroy = ActionSequence.getSequence("preDestroy");
        preDestroy.assertDataEquals(AirborneInterceptor.class, SuperDestructionInterceptor.class, DestructionInterceptor.class,
                Weapon.class, Rocket.class);

        ActionSequence aroundConstruct = ActionSequence.getSequence("aroundConstruct");
        aroundConstruct.assertDataEquals(AirborneInterceptor.class, SuperDestructionInterceptor.class,
                DestructionInterceptor.class);
    }
}
