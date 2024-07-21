/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jms.core.appclient.closedQueueConnection;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.ExceptionListener;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.QueueReceiver;
import jakarta.jms.QueueSender;
import jakarta.jms.QueueSession;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

/**
 * JMS TS tests. Testing method calls on closed QueueConnection objects.
 */

public class ClosedQueueConnectionTestsIT {
	private static final String TestName = "com.sun.ts.tests.jms.core.appclient.closedQueueConnection.ClosedQueueConnectionTestsIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(ClosedQueueConnectionTestsIT.class.getName());

	// JMS objects
	private transient JmsTool tool = null;

	// Harness req's
	private Properties props = null;

	// properties read
	long timeout;

	String user;

	String password;

	String mode;

	ArrayList queues = null;

	ArrayList connections = null;

	/* Utility methods for tests */

	/**
	 * Used by tests that need a closed connection for testing. Passes any
	 * exceptions up to caller.
	 * 
	 * @param int The type of session that needs to be created and closed
	 */
	private void createAndCloseConnection(int type) throws Exception {
		if ((type == JmsTool.QUEUE) || (type == JmsTool.TX_QUEUE)) {
			tool = new JmsTool(type, user, password, mode);
			tool.getDefaultQueueConnection().start();

			logger.log(Logger.Level.TRACE, "Closing queue Connection");
			tool.getDefaultQueueConnection().close();
		}
		logger.log(Logger.Level.TRACE, "Connection closed");
	}

	/* Test setup: */

	/*
	 * setup() is called before each test
	 * 
	 * Creates Administrator object and deletes all previous Destinations.
	 * Individual tests create the JmsTool object with one default Queue and/or
	 * Topic Connection, as well as a default Queue and Topic. Tests that require
	 * multiple Destinations create the extras within the test
	 * 
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
				throw new Exception("'timeout' (milliseconds) must be > 0");
			}
			if (user == null) {
				throw new Exception("'users' is null");
			}
			if (password == null) {
				throw new Exception("'password' is null");
			}
			if (mode == null) {
				throw new Exception("'mode' is null");
			}
			queues = new ArrayList(2);
			// get ready for new test
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("Setup failed!", e);
		}
	}

	/* cleanup */

	/*
	 * cleanup() is called after each test
	 * 
	 * Closes the default connections that are created by setup(). Any separate
	 * connections made by individual tests should be closed by that test.
	 * 
	 * @exception Fault
	 */
	@AfterEach
	public void cleanup() throws Exception {
		try {
			if (tool != null) {
				tool.doClientQueueTestCleanup(connections, queues);
			}

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			logger.log(Logger.Level.ERROR, "An error occurred while cleaning");
			throw new Exception("Cleanup failed!", e);
		}
	}

	/* Tests */

	/*
	 * @testName: closedQueueConnectionSetClientIDTest
	 *
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:270; JMS:JAVADOC:526;
	 * JMS:JAVADOC:514;
	 *
	 * @test_Strategy: Close default Connection and call setClientID() method on it.
	 * Check for IllegalStateException.
	 */
	@Test
	public void closedQueueConnectionSetClientIDTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseConnection(JmsTool.QUEUE);
			logger.log(Logger.Level.TRACE, "Try to call setClientID");
			try {
				tool.getDefaultQueueConnection().setClientID("foo");
				logger.log(Logger.Level.TRACE, "Fail: Exception was not thrown!");
			} catch (jakarta.jms.IllegalStateException ise) {
				logger.log(Logger.Level.TRACE, "Pass: threw expected error");
				passed = true;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Fail: wrong exception: " + e.getClass().getName() + " was returned");
			}
			if (!passed) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			throw new Exception("closedQueueConnectionSetClientIDTest", e);
		}
	}

	/*
	 * @testName: closedQueueConnectionSetExceptionListenerTest
	 *
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:270; JMS:JAVADOC:526;
	 * JMS:JAVADOC:520; JMS:JAVADOC:483;
	 *
	 * @test_Strategy: Close default Connection and call the setExceptionListener
	 * method on it. Check for IllegalStateException.
	 */
	@Test
	public void closedQueueConnectionSetExceptionListenerTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseConnection(JmsTool.QUEUE);
			logger.log(Logger.Level.TRACE, "Try to call setExceptionListener");
			try {
				ExceptionListener foo = new ExceptionListener() {

					public void onException(JMSException jmsE) {
					}

				};

				tool.getDefaultQueueConnection().setExceptionListener(foo);
				logger.log(Logger.Level.TRACE, "Fail: Exception was not thrown!");
			} catch (jakarta.jms.IllegalStateException ise) {
				logger.log(Logger.Level.TRACE, "Pass: threw expected error");
				passed = true;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Fail: wrong exception: " + e.getClass().getName() + " was returned");
			}
			if (!passed) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			throw new Exception("closedQueueConnectionSetExceptionListenerTest", e);
		}
	}

	/*
	 * @testName: closedQueueConnectionGetMessageListenerTest
	 *
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:270; JMS:JAVADOC:526;
	 * JMS:JAVADOC:328;
	 * 
	 * @test_Strategy: Close default receiver and call the getMessageListener()
	 * method on the QueueReceiver associated with it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedQueueConnectionGetMessageListenerTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseConnection(JmsTool.QUEUE);
			logger.log(Logger.Level.TRACE, "Try to call getMessageListener");
			try {
				MessageListener foo = tool.getDefaultQueueReceiver().getMessageListener();

				logger.log(Logger.Level.TRACE, "Fail: Exception was not thrown!");
			} catch (jakarta.jms.IllegalStateException ise) {
				logger.log(Logger.Level.TRACE, "Pass: threw expected error");
				passed = true;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Fail: wrong exception: " + e.getClass().getName() + " was returned");
			}
			if (!passed) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			throw new Exception("closedQueueConnectionGetMessageListenerTest", e);
		}
	}

	/*
	 * @testName: closedQueueConnectionSetMessageListenerTest
	 *
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:270; JMS:JAVADOC:526;
	 * JMS:JAVADOC:330; JMS:JAVADOC:325;
	 *
	 * @test_Strategy: Close default receiver and call the setMessageListener method
	 * on the QueueReceiver associated with it. Check for IllegalStateException.
	 */
	@Test
	public void closedQueueConnectionSetMessageListenerTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseConnection(JmsTool.QUEUE);
			logger.log(Logger.Level.TRACE, "Try to call setMessageListener");
			try {
				MessageListener foo = new MessageListener() {

					public void onMessage(Message m) {
					}

				};

				tool.getDefaultQueueReceiver().setMessageListener(foo);
				logger.log(Logger.Level.TRACE, "Fail: Exception was not thrown!");
			} catch (jakarta.jms.IllegalStateException ise) {
				logger.log(Logger.Level.TRACE, "Pass: threw expected error");
				passed = true;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Fail: wrong exception: " + e.getClass().getName() + " was returned");
			}
			if (!passed) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			throw new Exception("closedQueueConnectionSetMessageListenerTest", e);
		}
	}

	/*
	 * @testName: closedQueueConnectionGetExceptionListenerTest
	 *
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:270; JMS:JAVADOC:526;
	 * JMS:JAVADOC:518;
	 *
	 * @test_Strategy: Close default Connection and call the getExceptionListener()
	 * method on it. Check for IllegalStateException.
	 */
	@Test
	public void closedQueueConnectionGetExceptionListenerTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseConnection(JmsTool.QUEUE);
			logger.log(Logger.Level.TRACE, "Try to call getExceptionListener");
			try {
				ExceptionListener foo = tool.getDefaultQueueConnection().getExceptionListener();

				logger.log(Logger.Level.TRACE, "Fail: Exception was not thrown!");
			} catch (jakarta.jms.IllegalStateException ise) {
				logger.log(Logger.Level.TRACE, "Pass: threw expected error");
				passed = true;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Fail: wrong exception: " + e.getClass().getName() + " was returned");
			}
			if (!passed) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			throw new Exception("closedQueueConnectionGetExceptionListenerTest", e);
		}
	}

	/*
	 * @testName: closedQueueConnectionStopTest
	 *
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:270; JMS:JAVADOC:526;
	 * JMS:JAVADOC:524;
	 *
	 * @test_Strategy: Close default Connection and call the stop method on it.
	 * Check for IllegalStateException.
	 */
	@Test
	public void closedQueueConnectionStopTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseConnection(JmsTool.QUEUE);
			logger.log(Logger.Level.TRACE, "Try to call stop");
			try {
				tool.getDefaultQueueConnection().stop();
				logger.log(Logger.Level.TRACE, "Fail: Exception was not thrown!");
			} catch (jakarta.jms.IllegalStateException ise) {
				logger.log(Logger.Level.TRACE, "Pass: threw expected error");
				passed = true;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Fail: wrong exception: " + e.getClass().getName() + " was returned");
			}
			if (!passed) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			throw new Exception("closedQueueConnectionStopTest", e);
		}
	}

	/*
	 * @testName: closedQueueConnectionAckTest
	 *
	 * @assertion_ids: JMS:JAVADOC:272; JMS:SPEC:106; JMS:JAVADOC:794;
	 *
	 * @test_Strategy: Send and receive single message. Close the queue connection,
	 * call acknowledge, then verify that IllegalStateException is thrown.
	 */
	@Test
	public void closedQueueConnectionAckTest() throws Exception {
		boolean pass = true;

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			QueueSession qSession = null;
			QueueReceiver qReceiver = null;
			QueueSender qSender = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueReceiver().close();
			tool.getDefaultQueueSession().close();

			logger.log(Logger.Level.TRACE, "Creating new session");
			qSession = tool.getDefaultQueueConnection().createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
			qReceiver = qSession.createReceiver(tool.getDefaultQueue());
			qSender = qSession.createSender(tool.getDefaultQueue());
			tool.getDefaultQueueConnection().start();

			logger.log(Logger.Level.INFO, "Creating 1 TextMessage");
			messageSent = qSession.createTextMessage();
			messageSent.setText("Message from closedQueueConnectionAckTest");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "closedQueueConnectionAckTest");

			logger.log(Logger.Level.INFO, "Sending a TextMessage");
			qSender.send(messageSent);

			logger.log(Logger.Level.INFO, "Receiving TextMessage");
			messageReceived = (TextMessage) qReceiver.receive(timeout);
			logger.log(Logger.Level.INFO, "Closing DefaultQueueConnection");
			qReceiver.close();
			qSender.close();
			qSession.close();
			tool.getDefaultQueueConnection().close();

			try {
				if (messageReceived == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didnot receive any message!!");
				} else {
					messageReceived.acknowledge();
					pass = false;
					logger.log(Logger.Level.ERROR, "Should not be here!");
				}
			} catch (jakarta.jms.IllegalStateException is) {
				logger.log(Logger.Level.INFO, "Pass: IllegalStateException thrown by acknowledge as expected");
			} catch (Exception e) {
				pass = false;
				logger.log(Logger.Level.ERROR, "Expected IllegalStateException, got", e);
			}

			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("closedQueueConnectionAckTest");
		}
	}
}
