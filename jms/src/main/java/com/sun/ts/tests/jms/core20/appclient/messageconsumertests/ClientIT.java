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
package com.sun.ts.tests.jms.core20.appclient.messageconsumertests;

import java.lang.System.Logger;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;
import com.sun.ts.tests.jms.common.TextMessageTestImpl;

import jakarta.jms.Connection;
import jakarta.jms.Destination;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageListener;
import jakarta.jms.MessageProducer;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

public class ClientIT {
	private static final String testName = "com.sun.ts.tests.jms.core20.appclient.messageconsumertests.ClientIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(ClientIT.class.getName());

	// JMS tool which creates and/or looks up the JMS administered objects
	private transient JmsTool tool = null;

	// JMS tool which creates and/or looks up the JMS administered objects
	private transient JmsTool toolForProducer = null;

	// JMS objects
	transient MessageProducer producer = null;

	transient MessageConsumer consumer = null;

	transient Connection connection = null;

	transient Session session = null;

	transient Destination destination = null;

	// Harness req's
	private Properties props = null;

	// properties read
	long timeout;

	String user;

	String password;

	String mode;

	// used for tests
	private static final int numMessages = 3;

	private static final int iterations = 5;

	boolean queueTest = false;

	/* Utility methods for tests */

	/*
	 * Checks passed flag for negative tests and throws exception back to caller
	 * which passes ot to harness.
	 * 
	 * @param boolean Pass/Fail flag
	 */
	private void checkExceptionPass(boolean passed) throws Exception {
		if (passed == false) {
			logger.log(Logger.Level.INFO, "Didn't get expected exception");
			throw new Exception("Didn't get expected exception");
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
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
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
			logger.log(Logger.Level.INFO, "Closing default Connection");
			tool.getDefaultConnection().close();
			if (queueTest) {
				logger.log(Logger.Level.INFO, "Flush any messages left on Queue");
				tool.flushDestination();
			}
			tool.closeAllResources();

			if (toolForProducer != null) {
				toolForProducer.getDefaultConnection().close();
				if (queueTest) {
					logger.log(Logger.Level.INFO, "Flush any messages left on Queue");
					toolForProducer.flushDestination();
				}
				toolForProducer.closeAllResources();
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("cleanup failed!", e);
		}
	}

	/*
	 * @testName: queueSendRecvMessageListenerTest
	 * 
	 * @assertion_ids: JMS:JAVADOC:317; JMS:JAVADOC:328; JMS:JAVADOC:330;
	 * JMS:SPEC:264.4; JMS:SPEC:264; JMS:SPEC:137;
	 * 
	 * @test_Strategy: Creates a new consumer on the specified destination that will
	 * deliver messages to the specified MessageListener. Tests the following API
	 * method:
	 * 
	 * MessageProducer.send(Message)
	 * MessageConsumer.setMessageListener(MessageListener)
	 * MessageConsumer.getMessageListener()
	 * 
	 * 1 Setup MessageListener for the specified destination 2 Send a message to the
	 * destination 3 Verify message received by listener
	 */
	@Test
	public void queueSendRecvMessageListenerTest() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up test tool for Queue
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON QUEUE");
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			toolForProducer = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			logger.log(Logger.Level.INFO, "Initialize variables after JmsTool setup");
			producer = toolForProducer.getDefaultProducer();
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = true;

			// Creates a new consumer on the specified destination that
			// will deliver messages to the specified MessageListener.
			logger.log(Logger.Level.INFO, "Create message listener MyMessageListener");
			MyMessageListener listener = new MyMessageListener();
			logger.log(Logger.Level.INFO, "Set message listener MyMessageListener on this MessageConsumer");
			consumer.setMessageListener(listener);

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			// TextMessage expTextMessage = session.createTextMessage(message);
			TextMessage expTextMessage = new TextMessageTestImpl();
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setText(message);
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueSendRecvMessageListenerTest");
			logger.log(Logger.Level.INFO, "Calling MessageProducer.send(Message)");
			producer.send(expTextMessage);

			logger.log(Logger.Level.INFO, "Poll listener waiting for TestMessage to arrive");
			TextMessage actTextMessage = null;
			for (int i = 0; !listener.isComplete() && i < 60; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for messages to arrive at listener");
				TestUtil.sleepSec(2);
			}
			if (listener.isComplete())
				actTextMessage = (TextMessage) listener.getMessage();

			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage (actTextMessage=NULL)");
			}

			logger.log(Logger.Level.INFO, "Check value of message returned");
			if (!actTextMessage.getText().equals(expTextMessage.getText())) {
				logger.log(Logger.Level.ERROR,
						"Received [" + actTextMessage.getText() + "] expected [" + expTextMessage.getText() + "]");
				pass = false;
			}

			logger.log(Logger.Level.INFO, "Retreive MessageListener by calling MessageConsumer.getMessageListener()");
			MessageListener messageListener = consumer.getMessageListener();
			if (messageListener == null) {
				logger.log(Logger.Level.ERROR, "getMessageListener() returned NULL");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("queueSendRecvMessageListenerTest", e);
		} finally {
			try {
				producer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("queueSendRecvMessageListenerTest failed");
		}
	}

	/*
	 * @testName: topicSendRecvMessageListenerTest
	 * 
	 * @assertion_ids: JMS:JAVADOC:317; JMS:JAVADOC:328; JMS:JAVADOC:330;
	 * JMS:SPEC:264.4; JMS:SPEC:264;
	 * 
	 * @test_Strategy: Creates a new consumer on the specified destination that will
	 * deliver messages to the specified MessageListener. Tests the following API
	 * method:
	 * 
	 * MessageProducer.send(Message)
	 * MessageConsumer.setMessageListener(MessageListener)
	 * MessageConsumer.getMessageListener()
	 * 
	 * 1 Setup MessageListener for the specified destination 2 Send a message to the
	 * destination 3 Verify message received by listener
	 */
	@Test
	public void topicSendRecvMessageListenerTest() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up test tool for Topic
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON TOPIC");
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			toolForProducer = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			logger.log(Logger.Level.INFO, "Initialize variables after JmsTool setup");
			producer = toolForProducer.getDefaultProducer();
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = false;

			// Creates a new consumer on the specified destination that
			// will deliver messages to the specified MessageListener.
			logger.log(Logger.Level.INFO, "Create message listener MyMessageListener");
			MyMessageListener listener = new MyMessageListener();
			logger.log(Logger.Level.INFO, "Set message listener MyMessageListener on this MessageConsumer");
			consumer.setMessageListener(listener);

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			// TextMessage expTextMessage = session.createTextMessage(message);
			TextMessage expTextMessage = new TextMessageTestImpl();
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setText(message);
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicSendRecvMessageListenerTest");
			logger.log(Logger.Level.INFO, "Calling MessageProducer.send(Message)");
			producer.send(expTextMessage);

			logger.log(Logger.Level.INFO, "Poll listener waiting for TestMessage to arrive");
			TextMessage actTextMessage = null;
			for (int i = 0; !listener.isComplete() && i < 60; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for messages to arrive at listener");
				TestUtil.sleepSec(2);
			}
			if (listener.isComplete())
				actTextMessage = (TextMessage) listener.getMessage();

			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage (actTextMessage=NULL)");
			}

			logger.log(Logger.Level.INFO, "Check value of message returned");
			if (!actTextMessage.getText().equals(expTextMessage.getText())) {
				logger.log(Logger.Level.ERROR,
						"Received [" + actTextMessage.getText() + "] expected [" + expTextMessage.getText() + "]");
				pass = false;
			}

			logger.log(Logger.Level.INFO, "Retreive MessageListener by calling MessageConsumer.getMessageListener()");
			MessageListener messageListener = consumer.getMessageListener();
			if (messageListener == null) {
				logger.log(Logger.Level.ERROR, "getMessageListener() returned NULL");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("topicSendRecvMessageListenerTest", e);
		} finally {
			try {
				producer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("topicSendRecvMessageListenerTest failed");
		}
	}
}
