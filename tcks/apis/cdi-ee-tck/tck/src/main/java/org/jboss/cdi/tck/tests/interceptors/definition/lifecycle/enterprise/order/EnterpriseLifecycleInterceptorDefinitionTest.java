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
package org.jboss.cdi.tck.tests.interceptors.definition.lifecycle.enterprise.order;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.ENABLED_INTERCEPTORS;
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
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.util.List;

@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseLifecycleInterceptorDefinitionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(EnterpriseLifecycleInterceptorDefinitionTest.class)
                .withBeansXml(
                        new BeansXml().interceptors(MissileInterceptor.class))
                .build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "g"),
        @SpecAssertion(section = ENABLED_INTERCEPTORS, id = "h") })
    public void testLifecycleInterception() {

        ActionSequence.reset();

        Bean<Missile> bean = getUniqueBean(Missile.class);
        CreationalContext<Missile> ctx = getCurrentManager().createCreationalContext(bean);
        Missile missile = bean.create(ctx);
        missile.fire();
        bean.destroy(missile, ctx);

        List<String> postConstruct = ActionSequence.getSequenceData("postConstruct");
        assertEquals(postConstruct.size(), 4);
        assertEquals(postConstruct.get(0), AnotherInterceptor.class.getName());
        assertEquals(postConstruct.get(1), MissileInterceptor.class.getName());
        assertEquals(postConstruct.get(2), Weapon.class.getName());
        assertEquals(postConstruct.get(3), Missile.class.getName());

        List<String> preDestroy = ActionSequence.getSequenceData("preDestroy");
        assertEquals(preDestroy.size(), 4);
        assertEquals(postConstruct.get(0), AnotherInterceptor.class.getName());
        assertEquals(postConstruct.get(1), MissileInterceptor.class.getName());
        assertEquals(postConstruct.get(2), Weapon.class.getName());
        assertEquals(postConstruct.get(3), Missile.class.getName());
    }
}
