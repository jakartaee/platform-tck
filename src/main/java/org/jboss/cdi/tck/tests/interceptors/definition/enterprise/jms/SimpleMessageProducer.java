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

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Dependent;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;

import org.jboss.cdi.tck.impl.ConfigurationFactory;
import org.jboss.cdi.tck.util.JndiLookupUtils;
import org.jboss.cdi.tck.util.SimpleLogger;

@Dependent
public class SimpleMessageProducer {

    private static final SimpleLogger simpleLogger = new SimpleLogger(SimpleMessageProducer.class);

    private ConnectionFactory connectionFactory;

    private Queue queue;

    private Topic topic;

    @PostConstruct
    public void init() {
        this.connectionFactory = (ConnectionFactory) JndiLookupUtils.lookup(ConfigurationFactory.get().getTestJmsConnectionFactory());
        this.queue = (Queue) JndiLookupUtils.lookup(ConfigurationFactory.get().getTestJmsQueue());
        this.topic = (Topic) JndiLookupUtils.lookup(ConfigurationFactory.get().getTestJmsTopic());
    }

    public void sendQueueMessage() {

        Connection connection = null;
        Session session = null;
        MessageProducer messageProducer = null;
        TextMessage message = null;

        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            messageProducer = session.createProducer(queue);
            message = session.createTextMessage();
            message.setText(SimpleMessageProducer.class.getName());
            messageProducer.send(message);

        } catch (JMSException e) {
            throw new RuntimeException("Cannot send message", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    simpleLogger.log(e);
                }
            }
        }
    }

    public void sendTopicMessage() {

        Connection connection = null;
        Session session = null;
        MessageProducer messageProducer = null;
        TextMessage message = null;

        try {
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            messageProducer = session.createProducer(topic);
            message = session.createTextMessage();
            message.setText(SimpleMessageProducer.class.getName());
            messageProducer.send(message);

        } catch (JMSException e) {
            throw new RuntimeException("Cannot send message", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    simpleLogger.log(e);
                }
            }
        }
    }

}
