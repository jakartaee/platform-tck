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
package org.jboss.cdi.tck.tests.context.jms;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.TestGroups.JMS;
import static org.jboss.cdi.tck.cdi.Sections.APPLICATION_CONTEXT_EE;
import static org.jboss.cdi.tck.cdi.Sections.REQUEST_CONTEXT_EE;
import static org.jboss.cdi.tck.shrinkwrap.descriptors.ejb.EjbJarDescriptorBuilder.MessageDriven.newMessageDriven;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import java.util.List;
import java.util.concurrent.TimeUnit;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.impl.ConfigurationFactory;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.descriptors.ejb.EjbJarDescriptorBuilder;
import org.jboss.cdi.tck.tests.context.jms.LogStore.LogMessage;
import org.jboss.cdi.tck.util.Timer;
import org.jboss.cdi.tck.util.Timer.StopCondition;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.ejbjar31.EjbJarDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test that buil-in request and application scopes are active during message delivery to EJB message-driven bean.
 *
 * Note that basic JMS configuration is required for this test.
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class MessageDrivenBeanContextTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {

        EjbJarDescriptor ejbJarDescriptor = new EjbJarDescriptorBuilder().messageDrivenBeans(
                newMessageDriven("TestQueue", QueueMessageDrivenBean.class.getName())
                        .addActivationConfigProperty("acknowledgeMode", "Auto-acknowledge")
                        .addActivationConfigProperty("destinationType", "jakarta.jms.Queue")
                        .addActivationConfigProperty("destinationLookup", ConfigurationFactory.get().getTestJmsQueue()),
                newMessageDriven("TestTopic", TopicMessageDrivenBean.class.getName())
                        .addActivationConfigProperty("acknowledgeMode", "Auto-acknowledge")
                        .addActivationConfigProperty("destinationType", "jakarta.jms.Topic")
                        .addActivationConfigProperty("destinationLookup", ConfigurationFactory.get().getTestJmsTopic())).build();

        return new WebArchiveBuilder().withTestClassPackage(MessageDrivenBeanContextTest.class).withEjbJarXml(ejbJarDescriptor)
                .build();
    }

    @Inject
    SimpleMessageProducer producer;

    @Inject
    LogStore store;

    @Test(groups = { JAVAEE_FULL, JMS })
    @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "gd")
    @SpecAssertion(section = REQUEST_CONTEXT_EE, id = "hd")
    @SpecAssertion(section = APPLICATION_CONTEXT_EE, id = "dd")
    public void testRequestScopeActiveDuringMessageDelivery() throws Exception {

        new Timer().setDelay(5, TimeUnit.SECONDS).addStopCondition(new StopCondition() {
            public boolean isSatisfied() {
                return AbstractMessageListener.isInitialized();
            }
        }).start();

        AbstractMessageListener.resetProcessedMessages();

        producer.sendQueueMessage();
        producer.sendTopicMessage();

        // Wait for async processing
        new Timer().setDelay(5, TimeUnit.SECONDS).addStopCondition(new StopCondition() {
            public boolean isSatisfied() {
                return AbstractMessageListener.processedMessages.get() >= 2;
            }
        }).start();

        List<LogMessage> logMessages = store.getLogMessages();
        assertEquals(logMessages.size(), 2);
        LogMessage msg1 = logMessages.get(0);
        assertEquals(msg1.getText(), SimpleMessageProducer.class.getName());
        LogMessage msg2 = logMessages.get(1);
        assertEquals(msg2.getText(), SimpleMessageProducer.class.getName());
        // Test that each message delivery has its own request context
        assertFalse(msg1.getServiceId().equals(msg2.getServiceId()));
    }

}
