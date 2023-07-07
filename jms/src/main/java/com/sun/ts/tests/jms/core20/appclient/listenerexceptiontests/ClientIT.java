/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */
package com.sun.ts.tests.jms.core20.appclient.listenerexceptiontests;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;
import com.sun.ts.tests.jms.common.TextMessageTestImpl;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import jakarta.jms.JMSConsumer;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSProducer;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

public class ClientIT {
	private static final String testName = "com.sun.ts.tests.jms.core20.appclient.listenerexceptiontests.ClientIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(ClientIT.class.getName());

	// JMS tool which creates and/or looks up the JMS administered objects
	private transient JmsTool tool = null;

	// JMS objects
	private transient ConnectionFactory cf = null;

	private transient Queue queue = null;

	private transient Session session = null;

	private transient Destination destination = null;

	private transient Connection connection = null;

	private transient MessageConsumer consumer = null;

	private transient MessageProducer producer = null;

	private transient JMSContext context = null;

	private transient JMSContext contextToSendMsg = null;

	private transient JMSContext contextToCreateMsg = null;

	private transient JMSConsumer jmsconsumer = null;

	private transient JMSProducer jmsproducer = null;

	// Harness req's
	private Properties props = null;

	// properties read
	long timeout;

	String user;

	String password;

	String mode;

	// used for tests
	ArrayList queues = null;

	ArrayList connections = null;

	boolean jmscontextTest = false;

	/*
	 * Utility methods
	 */
	private void setupForQueue2() throws Exception {
		try {
			// set up JmsTool for COMMON_Q setup
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON_Q setup");
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			cf = tool.getConnectionFactory();
			destination = tool.getDefaultDestination();
			queue = (Queue) destination;
			tool.getDefaultConnection().close();
			logger.log(Logger.Level.INFO, "Create JMSContext with AUTO_ACKNOWLEDGE");
			context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			contextToSendMsg = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			contextToCreateMsg = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			jmsconsumer = context.createConsumer(destination);
			jmsproducer = contextToSendMsg.createProducer();
			jmscontextTest = true;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("setupForQueue2 failed!", e);
		}
	}

	private void setupForQueue() throws Exception {
		try {
			// set up JmsTool for COMMON_Q setup
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON_Q setup");
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			cf = tool.getConnectionFactory();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			consumer = tool.getDefaultConsumer();
			producer = tool.getDefaultProducer();
			destination = tool.getDefaultDestination();
			queue = (Queue) destination;
			connection.start();
			jmscontextTest = false;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("setupForQueue failed!", e);
		}
	}

	private void setupForQueueWithMultipleSessions() throws Exception {
		Connection newConnection = null;
		Session newSession = null;
		boolean transacted = false;
		try {
			// set up JmsTool for COMMON_Q setup
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON_Q setup");
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			cf = tool.getConnectionFactory();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			producer = tool.getDefaultProducer();
			destination = tool.getDefaultDestination();
			queue = (Queue) destination;

			// Create a consumer that uses new connection and new session
			newConnection = tool.getNewConnection(JmsTool.QUEUE, user, password);
			newSession = newConnection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);
			consumer = newSession.createConsumer(destination);
			connection.start();
			jmscontextTest = false;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("setupForQueue failed!", e);
		}
	}

	/* Test setup: */

	/*
	 * setup() is called before each test
	 * 
	 * @class.setup_props: jms_timeout; user; password; platform.mode;
	 * 
	 * @exception Fault
	 */
	@BeforeEach
	public void setup() throws Exception {
		try {
			// get props
			timeout = Long.parseLong(System.getProperty("jms_timeout"));
			user = System.getProperty("user");
			password = System.getProperty("password");
			mode = System.getProperty("platform.mode");

			// check props for errors
			if (timeout < 1) {
				throw new Exception("'jms_timeout' (milliseconds) in must be > 0");
			}
			if (user == null) {
				throw new Exception("'user' is null ");
			}
			if (password == null) {
				throw new Exception("'password' is null ");
			}
			if (mode == null) {
				throw new Exception("'platform.mode' is null");
			}
			queues = new ArrayList(3);
			connections = new ArrayList(5);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("setup failed!", e);
		}
	}

	/* cleanup */

	/*
	 * cleanup() is called after each test
	 * 
	 * @exception Fault
	 */
	@AfterEach
	public void cleanup() throws Exception {
		try {
			logger.log(Logger.Level.INFO, "Close Consumer objects");
			if (jmscontextTest) {
				if (jmsconsumer != null) {
					jmsconsumer.close();
					jmsconsumer = null;
				}
				logger.log(Logger.Level.INFO, "Close JMSContext objects");
				if (context != null) {
					context.close();
					context = null;
				}
				if (contextToSendMsg != null) {
					contextToSendMsg.close();
					contextToSendMsg = null;
				}
				if (contextToCreateMsg != null) {
					contextToCreateMsg.close();
					contextToCreateMsg = null;
				}
			} else {
				if (consumer != null) {
					consumer.close();
					consumer = null;
				}
			}
			logger.log(Logger.Level.INFO, "Flush any messages left on Queue");
			tool.flushDestination();
			tool.closeAllResources();
			tool.getDefaultConnection().close();
			producer = null;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("cleanup failed!", e);
		}
	}

	/*
	 * @testName: illegalStateExceptionTest1
	 * 
	 * @assertion_ids: JMS:JAVADOC:1351;
	 * 
	 * @test_Strategy: Calling Connection.close() from a CompletionListener MUST
	 * throw IllegalStateException.
	 */
	@Test
	public void illegalStateExceptionTest1() throws Exception {
		boolean pass = true;
		try {

			logger.log(Logger.Level.INFO,
					"Testing Connection.close() from CompletionListener (expect IllegalStateException)");
			try {
				// Setup for QUEUE
				setupForQueue();

				// Create CompetionListener
				MyCompletionListener listener = new MyCompletionListener(connection);

				// Create TextMessage
				logger.log(Logger.Level.INFO, "Creating TextMessage");
				TextMessage expTextMessage = session.createTextMessage("Call connection close method");
				logger.log(Logger.Level.INFO, "Set some values in TextMessage");
				expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "illegalStateExceptionTest1");

				logger.log(Logger.Level.INFO,
						"Send async message specifying CompletionListener to recieve async message");
				logger.log(Logger.Level.INFO,
						"CompletionListener will call Connection.close() (expect IllegalStateException)");
				producer.send(expTextMessage, listener);
				TextMessage actTextMessage = null;
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				for (int i = 0; i < 30; i++) {
					if (listener.isComplete()) {
						listener.setComplete(false);
						break;
					} else {
						TestUtil.sleepSec(2);
					}
				}
				logger.log(Logger.Level.INFO, "Check if we got correct exception from Connection.close()");
				if (listener.gotException()) {
					if (listener.gotCorrectException()) {
						logger.log(Logger.Level.INFO, "Got correct IllegalStateException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected IllegalStateException, received: " + listener.getException());
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected IllegalStateException, got no exception");
					pass = false;
				}
			} catch (jakarta.jms.IllegalStateException e) {
				logger.log(Logger.Level.INFO, "Caught IllegalStateException as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected IllegalStateException, received " + e);
				TestUtil.printStackTrace(e);
				pass = false;
			}

			try {
				cleanup();
			} catch (Exception e) {
				logger.log(Logger.Level.INFO, "Exception during cleanup: " + e);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("illegalStateExceptionTest1", e);
		}

		if (!pass) {
			throw new Exception("illegalStateExceptionTest1 failed");
		}
	}

	/*
	 * @testName: illegalStateExceptionTest2
	 * 
	 * @assertion_ids: JMS:JAVADOC:1351; JMS:JAVADOC:1352;
	 * 
	 * @test_Strategy: Calling Connection.close() or Connection.stop() from a
	 * MessageListener MUST throw IllegalStateException.
	 */
	@Test
	public void illegalStateExceptionTest2() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO,
					"Testing Connection.close() from MessageListener (expect IllegalStateException)");
			try {
				// Setup for QUEUE
				setupForQueue();

				// Create MessageListener
				MyMessageListener listener = new MyMessageListener(connection);

				// Create TextMessage
				logger.log(Logger.Level.INFO, "Creating TextMessage");
				TextMessage expTextMessage = session.createTextMessage("Call connection close method");
				logger.log(Logger.Level.INFO, "Set some values in TextMessage");
				expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "illegalStateExceptionTest2");

				logger.log(Logger.Level.INFO, "Set MessageListener to receive async message");
				consumer.setMessageListener(listener);

				logger.log(Logger.Level.INFO, "Send async message to MessageListener");
				logger.log(Logger.Level.INFO,
						"MessageListener will call Connection.close() (expect IllegalStateException)");
				producer.send(expTextMessage);
				TextMessage actTextMessage = null;
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				for (int i = 0; i < 30; i++) {
					if (listener.isComplete()) {
						listener.setComplete(false);
						break;
					} else {
						TestUtil.sleepSec(2);
					}
				}
				logger.log(Logger.Level.INFO, "Check if we got correct exception from Connection.close()");
				if (listener.gotException()) {
					if (listener.gotCorrectException()) {
						logger.log(Logger.Level.INFO, "Got correct IllegalStateException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected IllegalStateException, received: " + listener.getException());
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected IllegalStateException, got no exception");
					pass = false;
				}
			} catch (jakarta.jms.IllegalStateException e) {
				logger.log(Logger.Level.INFO, "Caught IllegalStateException as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected IllegalStateException, received " + e);
				TestUtil.printStackTrace(e);
				pass = false;
			}

			try {
				cleanup();
			} catch (Exception e) {
				logger.log(Logger.Level.INFO, "Exception during cleanup: " + e);
			}

			logger.log(Logger.Level.INFO,
					"Testing Connection.stop() from MessageListener (expect IllegalStateException)");
			try {
				// Setup for QUEUE
				setupForQueue();

				// Create MessageListener
				MyMessageListener listener = new MyMessageListener(connection);

				// Create TextMessage
				logger.log(Logger.Level.INFO, "Creating TextMessage");
				TextMessage expTextMessage = session.createTextMessage("Call connection stop method");
				logger.log(Logger.Level.INFO, "Set some values in TextMessage");
				expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "illegalStateExceptionTest2");

				logger.log(Logger.Level.INFO, "Set MessageListener to receive async message");
				consumer.setMessageListener(listener);

				logger.log(Logger.Level.INFO, "Send async message to MessageListener");
				logger.log(Logger.Level.INFO,
						"MessageListener will call Connection.stop() (expect IllegalStateException)");
				producer.send(expTextMessage);
				TextMessage actTextMessage = null;
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				for (int i = 0; i < 30; i++) {
					if (listener.isComplete()) {
						listener.setComplete(false);
						break;
					} else {
						TestUtil.sleepSec(2);
					}
				}
				logger.log(Logger.Level.INFO, "Check if we got correct exception from Connection.stop()");
				if (listener.gotException()) {
					if (listener.gotCorrectException()) {
						logger.log(Logger.Level.INFO, "Got correct IllegalStateException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected IllegalStateException, received: " + listener.getException());
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected IllegalStateException, got no exception");
					pass = false;
				}
			} catch (jakarta.jms.IllegalStateException e) {
				logger.log(Logger.Level.INFO, "Caught IllegalStateException as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected IllegalStateException, received " + e);
				TestUtil.printStackTrace(e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("illegalStateExceptionTest2", e);
		}

		if (!pass) {
			throw new Exception("illegalStateExceptionTest2 failed");
		}
	}

	/*
	 * @testName: illegalStateExceptionTest3
	 * 
	 * @assertion_ids: JMS:JAVADOC:1356;
	 * 
	 * @test_Strategy: Calling Session.close() from a CompletionListener or
	 * MessageListener MUST throw IllegalStateException.
	 */
	@Test
	public void illegalStateExceptionTest3() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO,
					"Testing Session.close() from CompletionListener (expect IllegalStateException)");
			try {
				// Setup for QUEUE
				setupForQueue();

				// Create CompetionListener
				MyCompletionListener listener = new MyCompletionListener(session);

				// Create TextMessage
				logger.log(Logger.Level.INFO, "Creating TextMessage");
				TextMessage expTextMessage = session.createTextMessage("Call session close method");
				logger.log(Logger.Level.INFO, "Set some values in TextMessage");
				expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "illegalStateExceptionTest3");

				logger.log(Logger.Level.INFO, "Send message specifying CompletionListener");
				logger.log(Logger.Level.INFO,
						"CompletionListener will call Session.close() (expect IllegalStateException)");
				producer.send(expTextMessage, listener);
				TextMessage actTextMessage = null;
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				for (int i = 0; i < 30; i++) {
					if (listener.isComplete()) {
						listener.setComplete(false);
						break;
					} else {
						TestUtil.sleepSec(2);
					}
				}
				logger.log(Logger.Level.INFO, "Check if we got correct exception from Session.close()");
				if (listener.gotException()) {
					if (listener.gotCorrectException()) {
						logger.log(Logger.Level.INFO, "Got correct IllegalStateException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected IllegalStateException, received: " + listener.getException());
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected IllegalStateException, got no exception");
					pass = false;
				}
			} catch (jakarta.jms.IllegalStateException e) {
				logger.log(Logger.Level.INFO, "Caught IllegalStateException as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected IllegalStateException, received " + e);
				TestUtil.printStackTrace(e);
				pass = false;
			}

			try {
				cleanup();
			} catch (Exception e) {
				logger.log(Logger.Level.INFO, "Exception during cleanup: " + e);
			}

			logger.log(Logger.Level.INFO,
					"Testing Session.close() from MessageListener (expect IllegalStateException)");
			try {
				// Setup for QUEUE
				setupForQueue();

				// Create MessageListener
				MyMessageListener listener = new MyMessageListener(session);

				// Create TextMessage
				logger.log(Logger.Level.INFO, "Creating TextMessage");
				TextMessage expTextMessage = session.createTextMessage("Call session close method");
				logger.log(Logger.Level.INFO, "Set some values in TextMessage");
				expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "illegalStateExceptionTest6");

				logger.log(Logger.Level.INFO, "Set MessageListener to receive async message");
				consumer.setMessageListener(listener);

				logger.log(Logger.Level.INFO, "Send async message to MessageListener");
				logger.log(Logger.Level.INFO,
						"MessageListener will call Session.close() (expect IllegalStateException)");
				producer.send(expTextMessage);
				TextMessage actTextMessage = null;
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				for (int i = 0; i < 30; i++) {
					if (listener.isComplete()) {
						listener.setComplete(false);
						break;
					} else {
						TestUtil.sleepSec(2);
					}
				}
				logger.log(Logger.Level.INFO, "Check if we got correct exception from Session.close()");
				if (listener.gotException()) {
					if (listener.gotCorrectException()) {
						logger.log(Logger.Level.INFO, "Got correct IllegalStateException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected IllegalStateException, received: " + listener.getException());
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected IllegalStateException, got no exception");
					pass = false;
				}
			} catch (jakarta.jms.IllegalStateException e) {
				logger.log(Logger.Level.INFO, "Caught IllegalStateException as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected IllegalStateException, received " + e);
				TestUtil.printStackTrace(e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("illegalStateExceptionTest3", e);
		}

		if (!pass) {
			throw new Exception("illegalStateExceptionTest3 failed");
		}
	}

	/*
	 * @testName: callingMessageConsumerCloseIsAllowed
	 * 
	 * @assertion_ids: JMS:JAVADOC:338;
	 * 
	 * @test_Strategy: Calling MessageConsumer.close() from a MessageListener is
	 * allowed.
	 */
	@Test
	public void callingMessageConsumerCloseIsAllowed() throws Exception {
		boolean pass = true;
		try {

			try {
				// Setup for QUEUE
				setupForQueueWithMultipleSessions();

				// Create MessageListener
				MyMessageListener listener = new MyMessageListener(consumer);

				// Create TextMessage
				logger.log(Logger.Level.INFO, "Creating TextMessage");
				// TextMessage expTextMessage = session
				// .createTextMessage("Call MessageConsumer close method");
				TextMessage expTextMessage = new TextMessageTestImpl();
				expTextMessage.setText("Call MessageConsumer close method");

				logger.log(Logger.Level.INFO, "Set some values in TextMessage");
				expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "callingMessageConsumerCloseIsAllowed");

				logger.log(Logger.Level.INFO, "Set MessageListener to receive async message");
				consumer.setMessageListener(listener);

				logger.log(Logger.Level.INFO, "Send async message to MessageListener");
				logger.log(Logger.Level.INFO, "MessageListener will call MessageConsumer.close() which is allowed");
				producer.send(expTextMessage);
				TextMessage actTextMessage = null;
				logger.log(Logger.Level.INFO, "Poll listener until complete");
				for (int i = 0; i < 30; i++) {
					if (listener.isComplete()) {
						listener.setComplete(false);
						break;
					} else {
						TestUtil.sleepSec(2);
					}
				}
				logger.log(Logger.Level.INFO, "Make sure MessageConsumer.close() was allowed");
				if (!listener.gotException()) {
					logger.log(Logger.Level.INFO, "MessageConsumer.close() was allowed");
				} else {
					logger.log(Logger.Level.ERROR, "MessageConsumer.close() through an exception");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				TestUtil.printStackTrace(e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("callingMessageConsumerCloseIsAllowed", e);
		}

		if (!pass) {
			throw new Exception("callingMessageConsumerCloseIsAllowed failed");
		}
	}

	/*
	 * @testName: callingJMSConsumerCloseIsAllowed
	 * 
	 * @assertion_ids: JMS:JAVADOC:1098;
	 * 
	 * @test_Strategy: Calling JMSConsumer.close() from a MessageListsner is
	 * allowed.
	 */
	@Test
	public void callingJMSConsumerCloseIsAllowed() throws Exception {
		boolean pass = true;
		try {

			try {
				// Setup for QUEUE
				setupForQueue2();

				// Create MessageListener
				MyMessageListener listener = new MyMessageListener(jmsconsumer);

				// Create TextMessage
				logger.log(Logger.Level.INFO, "Creating TextMessage");
				TextMessage expTextMessage = contextToCreateMsg.createTextMessage("Call JMSConsumer close method");
				logger.log(Logger.Level.INFO, "Set some values in TextMessage");
				expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "callingJMSConsumerCloseIsAllowed");

				logger.log(Logger.Level.INFO, "Set MessageListener to receive async message");
				jmsconsumer.setMessageListener(listener);

				logger.log(Logger.Level.INFO, "Send async message to MessageListener");
				logger.log(Logger.Level.INFO, "MessageListener will call JMSConsumer.close() which is allowed");
				jmsproducer.send(destination, expTextMessage);
				TextMessage actTextMessage = null;
				logger.log(Logger.Level.INFO, "Poll listener until complete");
				for (int i = 0; i < 30; i++) {
					if (listener.isComplete()) {
						listener.setComplete(false);
						break;
					} else {
						TestUtil.sleepSec(2);
					}
				}
				logger.log(Logger.Level.INFO, "Make sure JMSConsumer.close() was allowed");
				if (!listener.gotException()) {
					logger.log(Logger.Level.INFO, "JMSConsumer.close() was allowed");
				} else {
					logger.log(Logger.Level.ERROR, "JMSConsumer.close() through an exception");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				TestUtil.printStackTrace(e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("callingJMSConsumerCloseIsAllowed", e);
		}

		if (!pass) {
			throw new Exception("callingJMSConsumerCloseIsAllowed failed");
		}
	}
}
