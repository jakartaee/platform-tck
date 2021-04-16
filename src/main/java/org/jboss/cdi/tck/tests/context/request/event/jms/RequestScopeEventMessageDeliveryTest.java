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
package org.jboss.cdi.tck.tests.context.request.event.jms;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.TestGroups.JMS;
import static org.jboss.cdi.tck.cdi.Sections.REQUEST_CONTEXT;
import static org.jboss.cdi.tck.cdi.Sections.REQUEST_CONTEXT_EE;
import static org.jboss.cdi.tck.shrinkwrap.descriptors.ejb.EjbJarDescriptorBuilder.MessageDriven.newMessageDriven;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.TimeUnit;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.impl.ConfigurationFactory;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.descriptors.ejb.EjbJarDescriptorBuilder;
import org.jboss.cdi.tck.util.Timer;
import org.jboss.cdi.tck.util.Timer.StopCondition;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.ejbjar31.EjbJarDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class RequestScopeEventMessageDeliveryTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {

        EjbJarDescriptor ejbJarDescriptor = new EjbJarDescriptorBuilder().messageDrivenBeans(
                newMessageDriven("TestTopic", TopicMessageDrivenBean.class.getName())
                        .addActivationConfigProperty("acknowledgeMode", "Auto-acknowledge")
                        .addActivationConfigProperty("destinationType", "jakarta.jms.Topic")
                        .addActivationConfigProperty("destinationLookup", ConfigurationFactory.get().getTestJmsTopic()))
                .build();

        return new WebArchiveBuilder().withTestClassPackage(RequestScopeEventMessageDeliveryTest.class)
                .withEjbJarXml(ejbJarDescriptor).build();
    }

    @Inject
    private SimpleMessageProducer producer;

    @Inject
    private ApplicationScopedObserver observer;

    @Test(groups = { JAVAEE_FULL, JMS })
    @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "jf")
    @SpecAssertion(section = REQUEST_CONTEXT, id = "a")
    @SpecAssertion(section = REQUEST_CONTEXT, id = "c")
    public void testEventsFired() throws Exception {

        new Timer().setDelay(5, TimeUnit.SECONDS).addStopCondition(new StopCondition() {
            public boolean isSatisfied() {
                return AbstractMessageListener.isInitialized();
            }
        }).start();

        AbstractMessageListener.reset();
        observer.reset();

        producer.sendTopicMessage();

        new Timer().setDelay(5, TimeUnit.SECONDS).addStopCondition(new StopCondition() {
            public boolean isSatisfied() {
                return AbstractMessageListener.isInitializedEventObserver() && AbstractMessageListener.getProcessedMessages() >= 1;
            }
        }).start();

        assertEquals(1, AbstractMessageListener.getProcessedMessages());
        assertTrue(AbstractMessageListener.isInitializedEventObserver());

        // wait for the request scope for the message delivery to be destroyed and verify that the event was delivered
        new Timer().setDelay(5, TimeUnit.SECONDS).addStopCondition(new StopCondition() {
            public boolean isSatisfied() {
                return observer.isDestroyedCalled();
            }
        }).start();

        assertTrue(observer.isDestroyedCalled());
    }
}
