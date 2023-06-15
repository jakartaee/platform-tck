/*
 * Copyright (c) 2013, 2018, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.ts.tests.jms.ee20.cditests.usecases;

import java.lang.System.Logger;
import java.util.Properties;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TestUtil;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSConnectionFactory;
import jakarta.jms.JMSConsumer;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSProducer;
import jakarta.jms.Queue;
import jakarta.jms.Topic;
import jakarta.transaction.UserTransaction;

@TransactionManagement(TransactionManagementType.BEAN)
@Stateless(name = "CDIUseCasesBMBEAN1")
@Remote({ BMBean1IF.class })
public class BMBean1 implements BMBean1IF {

	private static final long serialVersionUID = 1L;

	long timeout;

	private static final Logger logger = (Logger) System.getLogger(BMBean1.class.getName());

	// JMSContext CDI injection specifying ConnectionFactory
	@Inject
	@JMSConnectionFactory("jms/ConnectionFactory")
	JMSContext context;

	@Resource(name = "jms/MyConnectionFactory")
	ConnectionFactory cfactory;

	@Resource(name = "jms/MY_QUEUE")
	Queue queue;

	@Resource(name = "jms/MY_TOPIC")
	Topic topic;

	@EJB(name = "ejb/CDIUseCasesBMBEAN2")
	BMBean2IF bmbean2;

	@Inject
	UserTransaction ut;

	@PostConstruct
	public void postConstruct() {
		System.out.println("BMBean1:postConstruct()");
		System.out.println("queue=" + queue);
		System.out.println("topic=" + topic);
		System.out.println("cfactory=" + cfactory);
		System.out.println("bmbean2=" + bmbean2);
		System.out.println("ut=" + ut);
		if (queue == null || topic == null || context == null || cfactory == null || bmbean2 == null || ut == null) {
			throw new EJBException("postConstruct failed: injection failure");
		}
	}

	public void init(Properties p) {
		logger.log(Logger.Level.INFO, "BMBean1.init()");
		try {
			TestUtil.init(p);
			timeout = Long.parseLong(System.getProperty("jms_timeout"));
		} catch (RemoteLoggingInitException e) {
			TestUtil.printStackTrace(e);
			throw new EJBException("BMBean1.init: failed");
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new EJBException("BMBean1.init: failed");
		}
	}

	public boolean cleanupQueue(int numOfMsgs) {
		int count = 0;
		String message = null;
		logger.log(Logger.Level.INFO, "BMBean1.cleanupQueue()");
		try {
			JMSConsumer consumer = context.createConsumer(queue);
			for (int i = 0; i < numOfMsgs; i++) {
				message = consumer.receiveBody(String.class, timeout);
				if (message != null) {
					logger.log(Logger.Level.INFO, "Cleanup message: [" + message + "]");
					count++;
				}
			}
			while ((message = consumer.receiveBody(String.class, timeout)) != null) {
				logger.log(Logger.Level.INFO, "Cleanup message: [" + message + "]");
				count++;
			}
			consumer.close();
			logger.log(Logger.Level.INFO, "Cleaned up " + count + " messages from Queue (numOfMsgs=" + numOfMsgs + ")");
			if (count == numOfMsgs)
				return true;
			else
				return false;
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new EJBException("CMBean1.cleanupQueue: failed");
		}
	}

	public void method1() {
		logger.log(Logger.Level.INFO, "BMBean1.method1(): JMSContext context=" + context);
		JMSProducer producer = context.createProducer();
		logger.log(Logger.Level.INFO, "Sending message [Message 1]");
		producer.send(queue, "Message 1");
		logger.log(Logger.Level.INFO, "Sending message [Message 2]");
		producer.send(queue, "Message 2");
	}

	public void method2() {
		logger.log(Logger.Level.INFO, "BMBean1.method2(): JMSContext context=" + context);
		logger.log(Logger.Level.INFO, "Sending message [Message 1]");
		context.createProducer().send(queue, "Message 1");
		logger.log(Logger.Level.INFO, "Calling BMBean2.method2()");
		bmbean2.method2();
	}

	public void method3() {
		try {
			logger.log(Logger.Level.INFO, "BMBean1.method3()");
			logger.log(Logger.Level.INFO, "Begin First User Transaction");
			ut.begin();
			logger.log(Logger.Level.INFO, "JMSContext context=" + context);
			JMSProducer producer = context.createProducer();
			logger.log(Logger.Level.INFO, "Sending message [Message 1]");
			producer.send(queue, "Message 1");
			logger.log(Logger.Level.INFO, "Sending message [Message 2]");
			producer.send(queue, "Message 2");
			logger.log(Logger.Level.INFO, "Commit First User Transaction");
			ut.commit();
			logger.log(Logger.Level.INFO, "Begin Second User Transaction");
			ut.begin();
			producer = context.createProducer();
			logger.log(Logger.Level.INFO, "Sending message [Message 3]");
			producer.send(queue, "Message 3");
			logger.log(Logger.Level.INFO, "Sending message [Message 4]");
			producer.send(queue, "Message 4");
			logger.log(Logger.Level.INFO, "Commit Second User Transaction");
			ut.commit();
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}

	public void method4() {
		logger.log(Logger.Level.INFO, "BMBean1.method4(): JMSContext context=" + context);
		try {
			JMSProducer producer = context.createProducer();
			logger.log(Logger.Level.INFO, "Sending message [Message 1]");
			producer.send(queue, "Message 1");
			logger.log(Logger.Level.INFO, "Sending message [Message 2]");
			producer.send(queue, "Message 2");
			logger.log(Logger.Level.INFO, "Begin User Transaction");
			ut.begin();
			producer = context.createProducer();
			logger.log(Logger.Level.INFO, "Sending message [Message 3]");
			producer.send(queue, "Message 3");
			logger.log(Logger.Level.INFO, "Commit User Transaction");
			ut.commit();
			producer = context.createProducer();
			logger.log(Logger.Level.INFO, "Sending message [Message 4]");
			producer.send(queue, "Message 4");
			logger.log(Logger.Level.INFO, "Sending message [Message 5]");
			producer.send(queue, "Message 5");
		} catch (Exception e) {
			throw new EJBException(e);
		}
	}
}
