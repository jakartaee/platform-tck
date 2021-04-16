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
package org.jboss.cdi.tck.tests.interceptors.definition.broken.finalClassInterceptor.ee;

import static org.jboss.cdi.tck.cdi.Sections.BINDING_INTERCEPTOR_TO_BEAN;

import jakarta.enterprise.inject.spi.DeploymentException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.interceptors.definition.broken.finalClassInterceptor.FinalClassMethodLevelIPBean;
import org.jboss.cdi.tck.tests.interceptors.definition.broken.finalClassInterceptor.FinalClassMethodLevelMissile;
import org.jboss.cdi.tck.tests.interceptors.definition.broken.finalClassInterceptor.FooBinding;
import org.jboss.cdi.tck.tests.interceptors.definition.broken.finalClassInterceptor.MissileInterceptor;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author Ondrej Skutka
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class FinalClassMethodLevelInterceptorTest extends AbstractTest {

    @ShouldThrowException(DeploymentException.class)
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassDefinition(FinalClassMethodLevelInterceptorTest.class)
                .withClasses(FooBinding.class, MissileInterceptor.class, FinalClassMethodLevelMissile.class, FinalClassMethodLevelIPBean.class)
                .withBeansXml(new BeansXml().interceptors(MissileInterceptor.class)).build();
    }

    @Test
    @SpecAssertion(section = BINDING_INTERCEPTOR_TO_BEAN, id = "d")
    public void testFinalClassWithMethodLevelInterceptor() throws Exception {
    }
}
