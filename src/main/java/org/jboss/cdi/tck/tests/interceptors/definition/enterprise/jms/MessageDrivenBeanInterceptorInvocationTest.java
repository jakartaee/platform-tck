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
package org.jboss.cdi.tck.tests.interceptors.definition.enterprise.jms;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.TestGroups.JMS;
import static org.jboss.cdi.tck.cdi.Sections.BIZ_METHOD_EE;
import static org.jboss.cdi.tck.shrinkwrap.descriptors.ejb.EjbJarDescriptorBuilder.MessageDriven.newMessageDriven;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.impl.ConfigurationFactory;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.descriptors.ejb.EjbJarDescriptorBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.ejbjar31.EjbJarDescriptor;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

/**
 * Test that invocations of message listener methods of message-driven beans during message delivery are business method
 * invocations.
 *
 * Note that basic JMS configuration is required for this test.
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class MessageDrivenBeanInterceptorInvocationTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {

        EjbJarDescriptor ejbJarDescriptor = new EjbJarDescriptorBuilder().messageDrivenBeans(
                newMessageDriven("TestQueue", MessageDrivenMissile.class.getName())
                        .addActivationConfigProperty("acknowledgeMode", "Auto-acknowledge")
                        .addActivationConfigProperty("destinationType", "jakarta.jms.Queue")
                        .addActivationConfigProperty("destinationLookup", ConfigurationFactory.get().getTestJmsQueue())).build();

        return new WebArchiveBuilder()
                .withTestClassPackage(MessageDrivenBeanInterceptorInvocationTest.class)
                .withEjbJarXml(ejbJarDescriptor)
                .withBeansXml(new BeansXml().interceptors(MissileInterceptor.class)).build();
    }

    @Inject
    SimpleMessageProducer producer;

    @Test(groups = { JAVAEE_FULL, JMS })
    @SpecAssertions(@SpecAssertion(section = BIZ_METHOD_EE, id = "la"))
    public void testMessageDrivenBeanMethodIntercepted() throws Exception {

        MissileInterceptor.reset();

        producer.sendQueueMessage();

        assertEquals(MessageDrivenMissile.class.getName(), MessageDrivenMissile.MESSAGES.poll(5, TimeUnit.SECONDS));
        assertTrue(MissileInterceptor.METHOD_INTERCEPTED.get());
        assertTrue(MissileInterceptor.lifecycleCallbackIntercepted);
        assertTrue(MissileInterceptor.aroundConstructInterceptorCalled);
    }

}
