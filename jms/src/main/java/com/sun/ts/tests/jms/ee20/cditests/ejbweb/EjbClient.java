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

package com.sun.ts.tests.jms.ee20.cditests.ejbweb;

import java.lang.System.Logger;
import java.util.Properties;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.EJBException;
import jakarta.ejb.Remote;
import jakarta.ejb.Stateful;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.IllegalStateRuntimeException;
import jakarta.jms.JMSConnectionFactory;
import jakarta.jms.JMSConsumer;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSPasswordCredential;
import jakarta.jms.JMSSessionMode;
import jakarta.jms.Queue;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;
import jakarta.jms.TopicConnectionFactory;
import jakarta.transaction.UserTransaction;

@Stateful(name = "CDITestsEjbWebClntBean")
@Remote({ EjbClientIF.class })
public class EjbClient implements EjbClientIF {

	private static final boolean debug = false;

	private static final long serialVersionUID = 1L;

	long timeout;

	private static final Logger logger = (Logger) System.getLogger(EjbClient.class.getName());

	private static int testsExecuted = 0;

	// JMSContext CDI injection specifying QueueConnectionFactory
	@Inject
	@JMSConnectionFactory("jms/QueueConnectionFactory")
	transient JMSContext context1;

	// JMSContext CDI injection specifying TopicConnectionFactory
	@Inject
	@JMSConnectionFactory("jms/TopicConnectionFactory")
	transient JMSContext context2;

	// JMSContext CDI injection specifying ConnectionFactory,
	// Password Credentials, and Session Mode
	@Inject
	@JMSConnectionFactory("jms/ConnectionFactory")
	@JMSPasswordCredential(userName = "j2ee", password = "j2ee")
	@JMSSessionMode(JMSContext.DUPS_OK_ACKNOWLEDGE)
	transient JMSContext context3;

	// JMSContext CDI injection for default Connection Factory
	@Inject
	transient JMSContext context4;

	@Resource(name = "jms/MyConnectionFactory")
	private transient ConnectionFactory cfactory;

	@Resource(name = "jms/MyQueueConnectionFactory")
	private transient QueueConnectionFactory qcfactory;

	@Resource(name = "jms/MyTopicConnectionFactory")
	private transient TopicConnectionFactory tcfactory;

	@Resource(name = "jms/MY_QUEUE")
	private transient Queue queue;

	@Resource(name = "jms/MY_TOPIC")
	private transient Topic topic;

	/*
	 * Utility method to return the session mode as a String
	 */
	private String printSessionMode(int sessionMode) {
		switch (sessionMode) {
		case JMSContext.SESSION_TRANSACTED:
			return "SESSION_TRANSACTED";
		case JMSContext.AUTO_ACKNOWLEDGE:
			return "AUTO_ACKNOWLEDGE";
		case JMSContext.CLIENT_ACKNOWLEDGE:
			return "CLIENT_ACKNOWLEDGE";
		case JMSContext.DUPS_OK_ACKNOWLEDGE:
			return "DUPS_OK_ACKNOWLEDGE";
		default:
			return "UNEXPECTED_SESSIONMODE";
		}
	}

	private void cleanup() {
		logger.log(Logger.Level.INFO, "cleanup");
	}

	@PostConstruct
	public void postConstruct() {
		System.out.println("EjbClient:postConstruct()");
		System.out.println("queue=" + queue);
		System.out.println("topic=" + topic);
		System.out.println("cfactory=" + cfactory);
		System.out.println("qcfactory=" + qcfactory);
		System.out.println("tcfactory=" + tcfactory);
		if (queue == null || topic == null || context1 == null || context2 == null || context3 == null
				|| context4 == null || cfactory == null || qcfactory == null || tcfactory == null) {
			throw new EJBException("postConstruct failed: injection failure");
		}
	}

	public void init(Properties p) {
		try {
			TestUtil.init(p);
			timeout = Long.parseLong(System.getProperty("jms_timeout"));
		} catch (RemoteLoggingInitException e) {
			TestUtil.printStackTrace(e);
			throw new EJBException("init: failed");
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new EJBException("init: failed");
		}
	}

	public boolean echo(String testName) {
		boolean pass = false;

		if (testName.equals("sendRecvQueueTestUsingCDIFromEjb"))
			pass = sendRecvQueueTestUsingCDIFromEjb();
		else if (testName.equals("sendRecvTopicTestUsingCDIFromEjb"))
			pass = sendRecvTopicTestUsingCDIFromEjb();
		else if (testName.equals("sendRecvUsingCDIDefaultFactoryFromEjb"))
			pass = sendRecvUsingCDIDefaultFactoryFromEjb();
		else if (testName.equals("verifySessionModeOnCDIJMSContextFromEjb"))
			pass = verifySessionModeOnCDIJMSContextFromEjb();
		else if (testName.equals("testRestrictionsOnCDIJMSContextFromEjb"))
			pass = testRestrictionsOnCDIJMSContextFromEjb();
		else if (testName.equals("testActiveJTAUsingCDICallMethod1FromEjb"))
			pass = testActiveJTAUsingCDICallMethod1FromEjb();
		else if (testName.equals("testActiveJTAUsingCDICallMethod2FromEjb"))
			pass = testActiveJTAUsingCDICallMethod2FromEjb();
		cleanup();
		return pass;
	}

	public boolean sendRecvQueueTestUsingCDIFromEjb() {
		boolean pass = true;
		JMSConsumer consumer = null;
		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;

			logger.log(Logger.Level.INFO, "Using CDI injected context1 specifying QueueConnectionFactory");

			// Create JMSConsumer from JMSContext
			consumer = context1.createConsumer(queue);

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			messageSent = context1.createTextMessage();
			messageSent.setText("just a test");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "sendRecvQueueTestUsingCDIFromEjb");
			logger.log(Logger.Level.INFO, "Sending TextMessage");
			context1.createProducer().send(queue, messageSent);
			logger.log(Logger.Level.INFO, "Receiving TextMessage");
			messageReceived = (TextMessage) consumer.receive(timeout);

			// Check to see if correct message received
			if (messageReceived == null) {
				logger.log(Logger.Level.ERROR, "No message was received");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Message Sent: \"" + messageSent.getText() + "\"");
				logger.log(Logger.Level.INFO, "Message Received: \"" + messageReceived.getText() + "\"");
				if (messageReceived.getText().equals(messageSent.getText())) {
					logger.log(Logger.Level.INFO, "Received correct message");
				} else {
					logger.log(Logger.Level.ERROR, "Received incorrect message");
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			pass = false;
		} finally {
			try {
				consumer.receive(timeout);
				while (consumer.receiveNoWait() != null)
					;
				consumer.close();
			} catch (Exception e) {
			}
		}
		return pass;
	}

	public boolean sendRecvTopicTestUsingCDIFromEjb() {
		boolean pass = true;
		JMSConsumer consumer = null;
		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;

			logger.log(Logger.Level.INFO, "Using CDI injected context2 specifying TopicConnectionFactory");

			// Create JMSConsumer from JMSContext
			consumer = context2.createConsumer(topic);

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			messageSent = context2.createTextMessage();
			messageSent.setText("just a test");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "sendRecvTopicTestUsingCDIFromEjb");
			logger.log(Logger.Level.INFO, "Sending TextMessage");
			context2.createProducer().send(topic, messageSent);
			logger.log(Logger.Level.INFO, "Receiving TextMessage");
			messageReceived = (TextMessage) consumer.receive(timeout);

			// Check to see if correct message received
			if (messageReceived == null) {
				logger.log(Logger.Level.ERROR, "No message was received");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Message Sent: \"" + messageSent.getText() + "\"");
				logger.log(Logger.Level.INFO, "Message Received: \"" + messageReceived.getText() + "\"");
				if (messageReceived.getText().equals(messageSent.getText())) {
					logger.log(Logger.Level.INFO, "Received correct message");
				} else {
					logger.log(Logger.Level.ERROR, "Received incorrect message");
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			pass = false;
		} finally {
			try {
				consumer.close();
			} catch (Exception e) {
			}
		}
		return pass;
	}

	public boolean sendRecvUsingCDIDefaultFactoryFromEjb() {
		boolean pass = true;
		JMSConsumer consumer = null;
		JMSConsumer consumer2 = null;
		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			TextMessage messageReceived2 = null;

			logger.log(Logger.Level.INFO, "Using CDI injected context4 using default system connection factory");

			// Create JMSConsumer from JMSContext for Queue
			logger.log(Logger.Level.INFO, "Creating Consumer for Queue");
			consumer = context4.createConsumer(queue);

			// Create JMSConsumer from JMSContext for Topic
			logger.log(Logger.Level.INFO, "Creating Consumer for Topic");
			consumer2 = context4.createConsumer(topic);

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			messageSent = context4.createTextMessage();
			messageSent.setText("just a test");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "sendRecvUsingCDIDefaultFactoryFromEjb");
			logger.log(Logger.Level.INFO, "Sending TextMessage to Queue");
			context4.createProducer().send(queue, messageSent);
			logger.log(Logger.Level.INFO, "Sending TextMessage to Topic");
			context4.createProducer().send(topic, messageSent);
			logger.log(Logger.Level.INFO, "Receiving TextMessage from Queue consumer");
			messageReceived = (TextMessage) consumer.receive(timeout);
			logger.log(Logger.Level.INFO, "Receiving TextMessage from Topic consumer");
			messageReceived2 = (TextMessage) consumer2.receive(timeout);

			// Check to see if correct message received from Queue consumer
			logger.log(Logger.Level.INFO, "Check received message from Queue consumer");
			if (messageReceived == null) {
				logger.log(Logger.Level.ERROR, "No message was received");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Message Sent: \"" + messageSent.getText() + "\"");
				logger.log(Logger.Level.INFO, "Message Received: \"" + messageReceived.getText() + "\"");
				if (messageReceived.getText().equals(messageSent.getText())) {
					logger.log(Logger.Level.INFO, "Received correct message");
				} else {
					logger.log(Logger.Level.ERROR, "Received incorrect message");
					pass = false;
				}
			}

			// Check to see if correct message received from Queue consumer
			logger.log(Logger.Level.INFO, "Check received message from Topic consumer");
			if (messageReceived2 == null) {
				logger.log(Logger.Level.ERROR, "No message was received");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Message Sent: \"" + messageSent.getText() + "\"");
				logger.log(Logger.Level.INFO, "Message Received: \"" + messageReceived2.getText() + "\"");
				if (messageReceived2.getText().equals(messageSent.getText())) {
					logger.log(Logger.Level.INFO, "Received correct message");
				} else {
					logger.log(Logger.Level.ERROR, "Received incorrect message");
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			pass = false;
		} finally {
			try {
				consumer.receive(timeout);
				while (consumer.receiveNoWait() != null)
					;
				consumer.close();
				consumer2.receive(timeout);
				while (consumer2.receiveNoWait() != null)
					;
				consumer2.close();
			} catch (Exception e) {
			}
		}
		return pass;
	}

	public boolean verifySessionModeOnCDIJMSContextFromEjb() {
		boolean pass = true;
		System.out.println("verifySessionModeOnCDIJMSContextFromEjb");
		try {
			logger.log(Logger.Level.INFO,
					"Checking session mode of context3 should be " + printSessionMode(JMSContext.DUPS_OK_ACKNOWLEDGE));
			if (context3.getSessionMode() != JMSContext.DUPS_OK_ACKNOWLEDGE) {
				logger.log(Logger.Level.ERROR,
						"Incorrect session mode returned: " + printSessionMode(context3.getSessionMode())
								+ "  expected: " + printSessionMode(JMSContext.DUPS_OK_ACKNOWLEDGE));
				pass = false;
			} else {
				logger.log(Logger.Level.INFO,
						"Returned correct session mode: " + printSessionMode(JMSContext.DUPS_OK_ACKNOWLEDGE));
			}

			logger.log(Logger.Level.INFO,
					"Checking session mode of context2 should be " + printSessionMode(JMSContext.AUTO_ACKNOWLEDGE));
			if (context2.getSessionMode() != JMSContext.AUTO_ACKNOWLEDGE) {
				logger.log(Logger.Level.ERROR,
						"Incorrect session mode returned: " + printSessionMode(context2.getSessionMode())
								+ "  expected: " + printSessionMode(JMSContext.AUTO_ACKNOWLEDGE));
				pass = false;
			} else {
				logger.log(Logger.Level.INFO,
						"Returned correct session mode: " + printSessionMode(JMSContext.AUTO_ACKNOWLEDGE));
			}

			logger.log(Logger.Level.INFO,
					"Checking session mode of context1 should be " + printSessionMode(JMSContext.AUTO_ACKNOWLEDGE));
			if (context1.getSessionMode() != JMSContext.AUTO_ACKNOWLEDGE) {
				logger.log(Logger.Level.ERROR,
						"Incorrect session mode returned: " + printSessionMode(context1.getSessionMode())
								+ "  expected: " + printSessionMode(JMSContext.AUTO_ACKNOWLEDGE));
				pass = false;
			} else {
				logger.log(Logger.Level.INFO,
						"Returned correct session mode: " + printSessionMode(JMSContext.AUTO_ACKNOWLEDGE));
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			pass = false;
		}
		return pass;
	}

	public boolean testRestrictionsOnCDIJMSContextFromEjb() {
		boolean pass = true;
		System.out.println("testRestrictionsOnCDIJMSContextFromEjb");
		try {
			logger.log(Logger.Level.INFO, "Calling JMSContext.acknowledge() MUST throw IllegalStateRuntimeException");
			try {
				context1.acknowledge();
			} catch (IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalStateRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected Exception: " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO,
					"Calling JMSContext.setClientID(String) MUST throw IllegalStateRuntimeException");
			try {
				context1.setClientID("test");
			} catch (IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalStateRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected Exception: " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO,
					"Calling JMSContext.setExceptionListener(ExceptionListener) MUST throw IllegalStateRuntimeException");
			try {
				context1.setExceptionListener(null);
			} catch (IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalStateRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected Exception: " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Calling JMSContext.start() MUST throw IllegalStateRuntimeException");
			try {
				context1.start();
			} catch (IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalStateRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected Exception: " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Calling JMSContext.stop() MUST throw IllegalStateRuntimeException");
			try {
				context1.stop();
			} catch (IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalStateRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected Exception: " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Calling JMSContext.commit() MUST throw IllegalStateRuntimeException");
			try {
				context1.commit();
			} catch (IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalStateRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected Exception: " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Calling JMSContext.rollback() MUST throw IllegalStateRuntimeException");
			try {
				context1.rollback();
			} catch (IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalStateRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected Exception: " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Calling JMSContext.recover() MUST throw IllegalStateRuntimeException");
			try {
				context1.recover();
			} catch (IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalStateRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected Exception: " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO,
					"Calling JMSContext.setAutoStart(boolean) MUST throw IllegalStateRuntimeException");
			try {
				context1.setAutoStart(true);
			} catch (IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalStateRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected Exception: " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Calling JMSContext.close() MUST throw IllegalStateRuntimeException");
			try {
				context1.close();
			} catch (IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalStateRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected Exception: " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			pass = false;
		}
		return pass;
	}

	public boolean testActiveJTAUsingCDICallMethod1FromEjb() {
		boolean pass = true;
		JMSConsumer consumer = null;

		try {
			logger.log(Logger.Level.INFO, "Enter testActiveJTAUsingCDICallMethod1FromEjb");
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			TSNamingContext nctx = new TSNamingContext();
			UserTransaction ut = (UserTransaction) nctx.lookup("java:comp/UserTransaction");

			logger.log(Logger.Level.INFO, "Start a JTA transaction");
			ut.begin();
			logger.log(Logger.Level.INFO, "Create TextMessage");
			messageSent = context4.createTextMessage();
			messageSent.setText("just a test");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "testActiveJTAUsingCDICallMethod1FromEjb");
			logger.log(Logger.Level.INFO, "Send TextMessage");
			context4.createProducer().send(queue, messageSent);
			logger.log(Logger.Level.INFO, "Exit method and let the next method call complete the JTA transaction");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			pass = false;
		}
		return pass;
	}

	public boolean testActiveJTAUsingCDICallMethod2FromEjb() {
		boolean pass = true;
		JMSConsumer consumer = null;

		try {
			logger.log(Logger.Level.INFO, "Enter testActiveJTAUsingCDICallMethod2FromEjb");
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			TSNamingContext nctx = new TSNamingContext();
			UserTransaction ut = (UserTransaction) nctx.lookup("java:comp/UserTransaction");

			logger.log(Logger.Level.INFO, "Commit the JTA transaction from previous method");
			ut.commit();

			// Create JMSConsumer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSConsumer to consume TextMessage sent in previous method");
			consumer = context4.createConsumer(queue);

			messageSent = context4.createTextMessage();
			messageSent.setText("just a test");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "testActiveJTAUsingCDICallMethod2FromEjb");
			logger.log(Logger.Level.INFO, "Receiving TextMessage sent in previous method");
			messageReceived = (TextMessage) consumer.receive(timeout);

			// Check to see if correct message received
			if (messageReceived == null) {
				logger.log(Logger.Level.ERROR, "No message was received");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Message Sent: \"" + messageSent.getText() + "\"");
				logger.log(Logger.Level.INFO, "Message Received: \"" + messageReceived.getText() + "\"");
				if (messageReceived.getText().equals(messageSent.getText())) {
					logger.log(Logger.Level.INFO, "Received correct message");
				} else {
					logger.log(Logger.Level.ERROR, "Received incorrect message");
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			pass = false;
		} finally {
			try {
				consumer.receive(timeout);
				while (consumer.receiveNoWait() != null)
					;
				consumer.close();
			} catch (Exception e) {
			}
		}
		return pass;
	}
}
