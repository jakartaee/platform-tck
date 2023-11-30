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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processInjectionPoint.ee;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.processInjectionPoint.Alpha;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.processInjectionPoint.Bravo;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.processInjectionPoint.BravoObserver;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.processInjectionPoint.Charlie;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.processInjectionPoint.Delta;
import org.jboss.cdi.tck.tests.full.extensions.lifecycle.processInjectionPoint.InjectingBean;
import org.jboss.cdi.tck.tests.extensions.lifecycle.processInjectionTarget.Farm;
import org.jboss.cdi.tck.util.HierarchyDiscovery;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_INJECTION_POINT_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * This test was originally part of Weld test suite.
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class ProcessInjectionPointFiredTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ProcessInjectionPointFiredTest.class)
                .withClasses(Alpha.class, Bravo.class, BravoObserver.class, Charlie.class, Delta.class, InjectingBean.class, Farm.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .withWebResource("faces-config.xml", "/WEB-INF/faces-config.xml")
                .withExtension(VerifyingExtension.class).build();
    }

    @Inject
    private VerifyingExtension extension;



    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_POINT_EE, id = "a") })
    public void testJavaEEComponentInjectionPoint() {
        InjectionPoint servletIp = extension.getServletCharlie();
        assertNotNull(servletIp);
        verifyType(servletIp, Charlie.class);
        InjectionPoint filterIp = extension.getFilterCharlie();
        assertNotNull(filterIp);
        verifyType(filterIp, Charlie.class);
        InjectionPoint listenerIp = extension.getListenerCharlie();
        assertNotNull(listenerIp);
        verifyType(listenerIp, Charlie.class);
    }


    private static void verifyType(InjectionPoint ip, Class<?> rawType, Class<?>... typeParameters) {
        assertEquals(getRawType(ip.getType()), rawType);
        if (typeParameters.length > 0) {
            assertTrue(ip.getType() instanceof ParameterizedType);
            assertTrue(Arrays.equals(typeParameters, getActualTypeArguments(ip.getType())));
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> Class<T> getRawType(Type type) {
        if (type instanceof Class<?>) {
            return (Class<T>) type;
        } else if (type instanceof ParameterizedType) {
            if (((ParameterizedType) type).getRawType() instanceof Class<?>) {
                return (Class<T>) ((ParameterizedType) type).getRawType();
            }
        }
        return null;
    }

    private static Type[] getActualTypeArguments(Type type) {
        Type resolvedType = new HierarchyDiscovery(type).getResolvedType();
        if (resolvedType instanceof ParameterizedType) {
            return ((ParameterizedType) resolvedType).getActualTypeArguments();
        } else {
            return new Type[] {};
        }
    }
}
