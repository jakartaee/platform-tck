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
package org.jboss.cdi.tck.tests.extensions.container.event.jms;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.TestGroups.JMS;
import static org.jboss.cdi.tck.cdi.Sections.PROCESS_INJECTION_TARGET_EE;
import static org.jboss.cdi.tck.shrinkwrap.descriptors.ejb.EjbJarDescriptorBuilder.MessageDriven.newMessageDriven;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.impl.ConfigurationFactory;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.descriptors.ejb.EjbJarDescriptorBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.ejbjar31.EjbJarDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * This test verifies that ProcessInjectionTarget event is fired for message driven bean.
 *
 * Note that basic JMS configuration is required for this test.
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class ContainerEventTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {

        EjbJarDescriptor ejbJarDescriptor = new EjbJarDescriptorBuilder().messageDrivenBeans(
                newMessageDriven("TestQueue", QueueMessageDrivenBean.class.getName())
                        .addActivationConfigProperty("acknowledgeMode", "Auto-acknowledge")
                        .addActivationConfigProperty("destinationType", "jakarta.jms.Queue")
                        .addActivationConfigProperty("destinationLookup", ConfigurationFactory.get().getTestJmsQueue())).build();

        return new WebArchiveBuilder().withTestClassPackage(ContainerEventTest.class)
                .withEjbJarXml(ejbJarDescriptor)
                .withExtension(ProcessInjectionTargetObserver.class).build();
    }

    @Test(groups = { JAVAEE_FULL, JMS })
    @SpecAssertions({ @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "aaba"), @SpecAssertion(section = PROCESS_INJECTION_TARGET_EE, id = "abba") })
    public void testProcessInjectionTargetEventFiredForMessageDrivenBean() {

        AnnotatedType<QueueMessageDrivenBean> annotatedType = ProcessInjectionTargetObserver.getMdbType();

        assertNotNull(annotatedType);
        assertEquals(annotatedType.getBaseType(), QueueMessageDrivenBean.class);

        // Methods initialize() and onMessage()
        assertEquals(annotatedType.getMethods().size(), 2);
        for (AnnotatedMethod<? super QueueMessageDrivenBean> method : annotatedType.getMethods()) {
            if ("initialize".equals(method.getJavaMember().getName())) {
                assertTrue(method.isAnnotationPresent(Inject.class));
                assertEquals(method.getParameters().size(), 1);
                assertEquals(method.getDeclaringType().getJavaClass(), QueueMessageDrivenBean.class);
            } else if ("onMessage".equals(method.getJavaMember().getName())) {
                assertEquals(method.getParameters().size(), 1);
                assertEquals(method.getDeclaringType().getJavaClass(), QueueMessageDrivenBean.class);
            } else {
                fail();
            }
        }

        // Fields sheep and initializerCalled
        assertEquals(annotatedType.getFields().size(), 2);
        for (AnnotatedField<? super QueueMessageDrivenBean> field : annotatedType.getFields()) {
            if ("sheep".equals(field.getJavaMember().getName())) {
                assertTrue(field.isAnnotationPresent(Inject.class));
                assertEquals(field.getDeclaringType().getJavaClass(), QueueMessageDrivenBean.class);
            } else if ("initializerCalled".equals(field.getJavaMember().getName())) {
                assertEquals(field.getDeclaringType().getJavaClass(), QueueMessageDrivenBean.class);
            } else {
                fail();
            }
        }
    }
}
