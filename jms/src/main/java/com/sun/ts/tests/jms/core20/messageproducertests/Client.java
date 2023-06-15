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
package com.sun.ts.tests.jms.core20.messageproducertests;

import java.lang.System.Logger;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.CompletionListener;
import jakarta.jms.Connection;
import jakarta.jms.DeliveryMode;
import jakarta.jms.Destination;
import jakarta.jms.InvalidDestinationException;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;

public class Client {
	private static final String testName = "com.sun.ts.tests.jms.core20.messageproducertests.Client";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	// JMS tool which creates and/or looks up the JMS administered objects
	private transient JmsTool tool = null;

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
				throw new Exception("'user' in must not be null ");
			}
			if (password == null) {
				throw new Exception("'password' in must not be null ");
			}
			if (mode == null) {
				throw new Exception("'platform.mode' in must not be null");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
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
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("cleanup failed!", e);
		}
	}

	/*
	 * @testName: queueSendAndRecvTest1
	 *
	 * @assertion_ids: JMS:JAVADOC:321; JMS:JAVADOC:334;
	 *
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 *
	 * MessageProducer.send(Destination, Message) MessageConsumer.receive(timeout)
	 */
	@Test
	public void queueSendAndRecvTest1() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Queue) null);
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = true;

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueSendAndRecvTest1");
			logger.log(Logger.Level.INFO, "Sending TextMessage");
			logger.log(Logger.Level.INFO, "Calling MessageProducer.send(Destination, Message)");
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Receive TextMessage");
			TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the value in TextMessage");
			if (actTextMessage.getText().equals(expTextMessage.getText())) {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
						+ ", received " + actTextMessage.getText());
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("queueSendAndRecvTest1", e);
		} finally {
			try {
				producer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("queueSendAndRecvTest1 failed");
		}
	}

	/*
	 * @testName: queueSendAndRecvTest2
	 *
	 * @assertion_ids: JMS:JAVADOC:323; JMS:JAVADOC:334;
	 *
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 *
	 * MessageProducer.send(Destination, Message, int, int, long)
	 * MessageConsumer.receive(timeout)
	 */
	@Test
	public void queueSendAndRecvTest2() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Queue) null);
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = true;

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueSendAndRecvTest2");
			logger.log(Logger.Level.INFO, "Sending TextMessage");
			logger.log(Logger.Level.INFO, "Calling MessageProducer.send(Destination, Message, int, int, long)");
			producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY - 1, 0L);
			logger.log(Logger.Level.INFO, "Receive TextMessage");
			TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the values in TextMessage, deliverymode, priority, time to live");
			if (!actTextMessage.getText().equals(expTextMessage.getText())
					|| actTextMessage.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT
					|| actTextMessage.getJMSPriority() != (Message.DEFAULT_PRIORITY - 1)
					|| actTextMessage.getJMSExpiration() != 0L) {
				logger.log(Logger.Level.ERROR, "Didn't get the right message.");
				logger.log(Logger.Level.ERROR,
						"text=" + actTextMessage.getText() + ", expected " + expTextMessage.getText());
				logger.log(Logger.Level.ERROR, "DeliveryMode=" + actTextMessage.getJMSDeliveryMode() + ", expected "
						+ expTextMessage.getJMSDeliveryMode());
				logger.log(Logger.Level.ERROR, "Priority=" + actTextMessage.getJMSPriority() + ", expected "
						+ expTextMessage.getJMSPriority());
				logger.log(Logger.Level.ERROR, "TimeToLive=" + actTextMessage.getJMSExpiration() + ", expected "
						+ expTextMessage.getJMSExpiration());
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("queueSendAndRecvTest2", e);
		} finally {
			try {
				producer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("queueSendAndRecvTest2 failed");
		}
	}

	/*
	 * @testName: queueSendAndRecvTest3
	 *
	 * @assertion_ids: JMS:JAVADOC:317; JMS:JAVADOC:334;
	 *
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 *
	 * MessageProducer.send(Message) MessageConsumer.receive(timeout)
	 */
	@Test
	public void queueSendAndRecvTest3() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			producer = tool.getDefaultProducer();
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = true;

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueSendAndRecvTest3");
			logger.log(Logger.Level.INFO, "Sending TextMessage");
			logger.log(Logger.Level.INFO, "Calling MessageProducer.send(Message)");
			producer.send(expTextMessage);
			logger.log(Logger.Level.INFO, "Receive TextMessage");
			TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the value in TextMessage");
			if (actTextMessage.getText().equals(expTextMessage.getText())) {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
						+ ", received " + actTextMessage.getText());
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("queueSendAndRecvTest3", e);
		}

		if (!pass) {
			throw new Exception("queueSendAndRecvTest3 failed");
		}
	}

	/*
	 * @testName: queueSendAndRecvTest4
	 *
	 * @assertion_ids: JMS:JAVADOC:319; JMS:JAVADOC:334;
	 *
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 *
	 * MessageProducer.send(Message, int, int, long)
	 * MessageConsumer.receive(timeout)
	 */
	@Test
	public void queueSendAndRecvTest4() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			producer = tool.getDefaultProducer();
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = true;

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueSendAndRecvTest4");
			logger.log(Logger.Level.INFO, "Sending TextMessage");
			logger.log(Logger.Level.INFO, "Calling MessageProducer.send(Message, int, int, long)");
			producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY - 1, 0L);
			logger.log(Logger.Level.INFO, "Receive TextMessage");
			TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the values in TextMessage, deliverymode, priority, time to live");
			if (!actTextMessage.getText().equals(expTextMessage.getText())
					|| actTextMessage.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT
					|| actTextMessage.getJMSPriority() != (Message.DEFAULT_PRIORITY - 1)
					|| actTextMessage.getJMSExpiration() != 0L) {
				logger.log(Logger.Level.ERROR, "Didn't get the right message.");
				logger.log(Logger.Level.ERROR,
						"text=" + actTextMessage.getText() + ", expected " + expTextMessage.getText());
				logger.log(Logger.Level.ERROR, "DeliveryMode=" + actTextMessage.getJMSDeliveryMode() + ", expected "
						+ expTextMessage.getJMSDeliveryMode());
				logger.log(Logger.Level.ERROR, "Priority=" + actTextMessage.getJMSPriority() + ", expected "
						+ expTextMessage.getJMSPriority());
				logger.log(Logger.Level.ERROR, "TimeToLive=" + actTextMessage.getJMSExpiration() + ", expected "
						+ expTextMessage.getJMSExpiration());
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("queueSendAndRecvTest4", e);
		}

		if (!pass) {
			throw new Exception("queueSendAndRecvTest4 failed");
		}
	}

	/*
	 * @testName: queueSetGetDeliveryModeTest
	 *
	 * @assertion_ids: JMS:JAVADOC:301; JMS:JAVADOC:303;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * MessageProducer.setDeliveryMode(int). MessageProducer.getDeliveryMode().
	 */
	@Test
	public void queueSetGetDeliveryModeTest() throws Exception {
		boolean pass = true;

		// Test default case
		try {
			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			producer = tool.getDefaultProducer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			queueTest = true;

			long expDeliveryMode = DeliveryMode.PERSISTENT;
			logger.log(Logger.Level.INFO, "Calling getDeliveryMode and expect " + expDeliveryMode + " to be returned");
			long actDeliveryMode = producer.getDeliveryMode();
			if (actDeliveryMode != expDeliveryMode) {
				logger.log(Logger.Level.ERROR,
						"getDeliveryMode() returned " + actDeliveryMode + ", expected " + expDeliveryMode);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("queueSetGetDeliveryModeTest");
		}

		// Test non-default case
		try {
			int expDeliveryMode = DeliveryMode.NON_PERSISTENT;
			logger.log(Logger.Level.INFO, "Calling setDeliveryMode(" + expDeliveryMode + ")");
			producer.setDeliveryMode(expDeliveryMode);
			logger.log(Logger.Level.INFO, "Calling getDeliveryMode and expect " + expDeliveryMode + " to be returned");
			int actDeliveryMode = producer.getDeliveryMode();
			if (actDeliveryMode != expDeliveryMode) {
				logger.log(Logger.Level.ERROR,
						"getDeliveryMode() returned " + actDeliveryMode + ", expected " + expDeliveryMode);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("queueSetGetDeliveryModeTest");
		}

		if (!pass) {
			throw new Exception("queueSetGetDeliveryModeTest failed");
		}
	}

	/*
	 * @testName: queueSetGetDeliveryDelayTest
	 *
	 * @assertion_ids: JMS:JAVADOC:907; JMS:JAVADOC:886; JMS:SPEC:261;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * MessageProducer.setDeliveryDelay(long). MessageProducer.getDeliveryDelay().
	 */
	@Test
	public void queueSetGetDeliveryDelayTest() throws Exception {
		boolean pass = true;

		// Test default case
		try {
			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			producer = tool.getDefaultProducer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			queueTest = true;

			long expDeliveryDelay = 0L;
			logger.log(Logger.Level.INFO,
					"Calling getDeliveryDelay and expect " + expDeliveryDelay + " to be returned");
			long actDeliveryDelay = producer.getDeliveryDelay();
			if (actDeliveryDelay != expDeliveryDelay) {
				logger.log(Logger.Level.ERROR,
						"getDeliveryDelay() returned " + actDeliveryDelay + ", expected " + expDeliveryDelay);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("queueSetGetDeliveryDelayTest");
		}

		// Test non-default case
		try {
			long expDeliveryDelay = 1L;
			logger.log(Logger.Level.INFO, "Calling setDeliveryDelay(" + expDeliveryDelay + ")");
			producer.setDeliveryDelay(expDeliveryDelay);
			logger.log(Logger.Level.INFO,
					"Calling getDeliveryDelay and expect " + expDeliveryDelay + " to be returned");
			long actDeliveryDelay = producer.getDeliveryDelay();
			if (actDeliveryDelay != expDeliveryDelay) {
				logger.log(Logger.Level.ERROR,
						"getDeliveryDelay() returned " + actDeliveryDelay + ", expected " + expDeliveryDelay);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("queueSetGetDeliveryDelayTest");
		}

		if (!pass) {
			throw new Exception("queueSetGetDeliveryDelayTest failed");
		}
	}

	/*
	 * @testName: queueSetGetDisableMessageIDTest
	 *
	 * @assertion_ids: JMS:JAVADOC:293; JMS:JAVADOC:295;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * MessageProducer.setDisableMessageID(int).
	 * MessageProducer.getDisableMessageID().
	 */
	@Test
	public void queueSetGetDisableMessageIDTest() throws Exception {
		boolean pass = true;
		// Test true case
		try {
			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			producer = tool.getDefaultProducer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			queueTest = true;

			boolean expDisableMessageID = true;
			logger.log(Logger.Level.INFO, "Calling setDisableMessageID(" + expDisableMessageID + ")");
			producer.setDisableMessageID(expDisableMessageID);
			logger.log(Logger.Level.INFO,
					"Calling getDisableMessageID and expect " + expDisableMessageID + " to be returned");
			boolean actDisableMessageID = producer.getDisableMessageID();
			if (actDisableMessageID != expDisableMessageID) {
				logger.log(Logger.Level.ERROR,
						"getDisableMessageID() returned " + actDisableMessageID + ", expected " + expDisableMessageID);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("queueSetGetDisableMessageIDTest");
		}

		// Test false case
		try {
			boolean expDisableMessageID = false;
			logger.log(Logger.Level.INFO, "Calling setDisableMessageID(" + expDisableMessageID + ")");
			producer.setDisableMessageID(expDisableMessageID);
			logger.log(Logger.Level.INFO,
					"Calling getDisableMessageID and expect " + expDisableMessageID + " to be returned");
			boolean actDisableMessageID = producer.getDisableMessageID();
			if (actDisableMessageID != expDisableMessageID) {
				logger.log(Logger.Level.ERROR,
						"getDisableMessageID() returned " + actDisableMessageID + ", expected " + expDisableMessageID);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("queueSetGetDisableMessageIDTest");
		}

		if (!pass) {
			throw new Exception("queueSetGetDisableMessageIDTest failed");
		}
	}

	/*
	 * @testName: queueSetGetDisableMessageTimestampTest
	 *
	 * @assertion_ids: JMS:JAVADOC:297; JMS:JAVADOC:299;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * MessageProducer.setDisableMessageTimestamp(int).
	 * MessageProducer.getDisableMessageTimestamp().
	 */
	@Test
	public void queueSetGetDisableMessageTimestampTest() throws Exception {
		boolean pass = true;
		// Test true case
		try {
			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			producer = tool.getDefaultProducer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			queueTest = true;

			boolean expDisableMessageTimestamp = true;
			logger.log(Logger.Level.INFO, "Calling setDisableMessageTimestamp(" + expDisableMessageTimestamp + ")");
			producer.setDisableMessageTimestamp(expDisableMessageTimestamp);
			logger.log(Logger.Level.INFO,
					"Calling getDisableMessageTimestamp and expect " + expDisableMessageTimestamp + " to be returned");
			boolean actDisableMessageTimestamp = producer.getDisableMessageTimestamp();
			if (actDisableMessageTimestamp != expDisableMessageTimestamp) {
				logger.log(Logger.Level.ERROR, "getDisableMessageTimestamp() returned " + actDisableMessageTimestamp
						+ ", expected " + expDisableMessageTimestamp);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("queueSetGetDisableMessageTimestampTest");
		}

		// Test false case
		try {
			boolean expDisableMessageTimestamp = false;
			logger.log(Logger.Level.INFO, "Calling setDisableMessageTimestamp(" + expDisableMessageTimestamp + ")");
			producer.setDisableMessageTimestamp(expDisableMessageTimestamp);
			logger.log(Logger.Level.INFO,
					"Calling getDisableMessageTimestamp and expect " + expDisableMessageTimestamp + " to be returned");
			boolean actDisableMessageTimestamp = producer.getDisableMessageTimestamp();
			if (actDisableMessageTimestamp != expDisableMessageTimestamp) {
				logger.log(Logger.Level.ERROR, "getDisableMessageTimestamp() returned " + actDisableMessageTimestamp
						+ ", expected " + expDisableMessageTimestamp);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("queueSetGetDisableMessageTimestampTest");
		}

		if (!pass) {
			throw new Exception("queueSetGetDisableMessageTimestampTest failed");
		}
	}

	/*
	 * @testName: queueSetGetPriorityTest
	 *
	 * @assertion_ids: JMS:JAVADOC:305; JMS:JAVADOC:307;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * MessageProducer.setPriority(int). MessageProducer.getPriority().
	 */
	@Test
	public void queueSetGetPriorityTest() throws Exception {
		boolean pass = true;
		try {
			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			producer = tool.getDefaultProducer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			queueTest = true;

			// Test default
			int expPriority = 4;
			logger.log(Logger.Level.INFO, "Calling getPriority and expect " + expPriority + " to be returned");
			int actPriority = producer.getPriority();
			if (actPriority != expPriority) {
				logger.log(Logger.Level.ERROR, "getPriority() returned " + actPriority + ", expected " + expPriority);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("queueSetGetPriorityTest");
		}

		// Test non-default
		int expPriority[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

		// Cycle through all priorties
		for (int i = 0; i < expPriority.length; i++) {
			try {
				logger.log(Logger.Level.INFO, "Calling setPriority(" + expPriority[i] + ")");
				producer.setPriority(expPriority[i]);
				logger.log(Logger.Level.INFO, "Calling getPriority and expect " + expPriority[i] + " to be returned");
				int actPriority = producer.getPriority();
				if (actPriority != expPriority[i]) {
					logger.log(Logger.Level.ERROR,
							"getPriority() returned " + actPriority + ", expected " + expPriority[i]);
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
				throw new Exception("queueSetGetPriorityTest");
			}
		}

		if (!pass) {
			throw new Exception("queueSetGetPriorityTest failed");
		}
	}

	/*
	 * @testName: queueSetGetTimeToLiveTest
	 *
	 * @assertion_ids: JMS:JAVADOC:309; JMS:JAVADOC:311;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * MessageProducer.setTimeToLive(long). MessageProducer.getTimeToLive()
	 */
	@Test
	public void queueSetGetTimeToLiveTest() throws Exception {
		boolean pass = true;

		try {
			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			producer = tool.getDefaultProducer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			queueTest = true;

			// Test default
			long expTimeToLive = 0;
			logger.log(Logger.Level.INFO, "Calling getTimeToLive and expect " + expTimeToLive + " to be returned");
			long actTimeToLive = producer.getTimeToLive();
			if (actTimeToLive != expTimeToLive) {
				logger.log(Logger.Level.ERROR,
						"getTimeToLive() returned " + actTimeToLive + ", expected " + expTimeToLive);
				pass = false;
			}

			// Test non-default
			expTimeToLive = 1000;
			logger.log(Logger.Level.INFO, "Calling setTimeToLive(" + expTimeToLive + ")");
			producer.setTimeToLive(expTimeToLive);
			logger.log(Logger.Level.INFO, "Calling getTimeToLive and expect " + expTimeToLive + " to be returned");
			actTimeToLive = producer.getTimeToLive();
			if (actTimeToLive != expTimeToLive) {
				logger.log(Logger.Level.ERROR,
						"getTimeToLive() returned " + actTimeToLive + ", expected " + expTimeToLive);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("queueSetGetTimeToLiveTest");
		}

		if (!pass) {
			throw new Exception("queueSetGetTimeToLiveTest failed");
		}
	}

	/*
	 * @testName: queueInvalidDestinationExceptionTests
	 *
	 * @assertion_ids: JMS:JAVADOC:598; JMS:JAVADOC:601; JMS:JAVADOC:604;
	 * JMS:JAVADOC:607;
	 *
	 * @test_Strategy: Test for InvalidDestinationException from MessageProducer
	 * API's.
	 *
	 * MessageProducer.send(Destination, Message) MessageProducer.send(Destination,
	 * Message, init, int, int)
	 */
	@Test
	public void queueInvalidDestinationExceptionTests() throws Exception {
		boolean pass = true;
		TextMessage tempMsg = null;
		String message = "Where are you!";
		try {
			// set up test tool for Queue
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON QUEUE");
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Queue) null);
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = null;
			connection.start();
			queueTest = true;

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueInvalidDestinationExceptionTests");

			try {
				logger.log(Logger.Level.INFO, "Send message with invalid destination");
				producer.send(destination, expTextMessage);
				logger.log(Logger.Level.ERROR, "Didn't throw InvalidDestinationException");
			} catch (InvalidDestinationException e) {
				logger.log(Logger.Level.INFO, "Caught expected InvalidDestinationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Send message with invalid destination");
				producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 0L);
				logger.log(Logger.Level.ERROR, "Didn't throw InvalidDestinationException");
			} catch (InvalidDestinationException e) {
				logger.log(Logger.Level.INFO, "Caught expected InvalidDestinationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationException, received " + e.getMessage());
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("queueInvalidDestinationExceptionTests", e);
		} finally {
			try {
				producer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("queueInvalidDestinationExceptionTests failed");
		}
	}

	/*
	 * @testName: queueUnsupportedOperationExceptionTests
	 *
	 * @assertion_ids: JMS:JAVADOC:599; JMS:JAVADOC:602; JMS:JAVADOC:605;
	 * JMS:JAVADOC:1318;
	 *
	 * @test_Strategy: Test for UnsupportedOperationException from MessageProducer
	 * API's.
	 * 
	 * MessageProducer.send(Destination, Message) MessageProducer.send(Destination,
	 * Message, init, int, int) MessageProducer.send(Message)
	 * MessageProducer.send(Message, init, int, int)
	 */
	@Test
	public void queueUnsupportedOperationExceptionTests() throws Exception {
		boolean pass = true;
		TextMessage tempMsg = null;
		String message = "Where are you!";
		try {
			// set up test tool for Queue
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON QUEUE");
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			producer = tool.getDefaultProducer();
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = true;

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueUnsupportedOperationExceptionTests");

			try {
				logger.log(Logger.Level.INFO, "Send message with destination specified at creation time");
				producer.send(destination, expTextMessage);
				logger.log(Logger.Level.ERROR, "Didn't throw UnsupportedOperationException");
			} catch (UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected UnsupportedOperationException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Send message with destination specified at creation time");
				producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 0L);
				logger.log(Logger.Level.ERROR, "Didn't throw UnsupportedOperationException");
			} catch (UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected UnsupportedOperationException, received " + e.getMessage());
				pass = false;
			}

			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Queue) null);

			try {
				logger.log(Logger.Level.INFO, "Send message with destination not specified at creation time");
				producer.send(expTextMessage);
				logger.log(Logger.Level.ERROR, "Didn't throw UnsupportedOperationException");
			} catch (UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected UnsupportedOperationException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Send message with destination not specified at creation time");
				producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 0L);
				logger.log(Logger.Level.ERROR, "Didn't throw UnsupportedOperationException");
			} catch (UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected UnsupportedOperationException, received " + e.getMessage());
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("queueUnsupportedOperationExceptionTests", e);
		} finally {
			try {
				producer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("queueUnsupportedOperationExceptionTests failed");
		}
	}

	/*
	 * @testName: queueDeliveryDelayTest
	 * 
	 * @assertion_ids: JMS:SPEC:261; JMS:JAVADOC:907;
	 * 
	 * @test_Strategy: Send message and verify that message is not delivered until
	 * the DeliveryDelay of 30 seconds is reached. Test DeliveryMode.PERSISTENT and
	 * DeliveryMode.NON_PERSISTENT.
	 *
	 * MessageProducer.setDeliveryDelay(30000) MessageProducer.send(Destination,
	 * Message, int, int, long)
	 */
	@Test
	public void queueDeliveryDelayTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "-----------------------------------------------------------");
			logger.log(Logger.Level.INFO, "BEGIN TEST queueDeliveryDelayTest with DeliveryDelay=30Secs");
			logger.log(Logger.Level.INFO, "-----------------------------------------------------------");
			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Queue) null);
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = true;

			producer.setDeliveryDelay(30000);

			// Send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage message = session.createTextMessage("This is a test!");

			logger.log(Logger.Level.INFO, "Set StringProperty COM_SUN_JMS_TESTNAME");
			message.setStringProperty("COM_SUN_JMS_TESTNAME", "queueDeliveryDelayTest");

			logger.log(Logger.Level.INFO, "Sending message with DeliveryMode.PERSISTENT and DeliveryDelay=30Secs");
			producer.send(destination, message, DeliveryMode.PERSISTENT, Message.DEFAULT_PRIORITY, 0L);

			logger.log(Logger.Level.INFO, "Waiting 15 seconds to receive message");
			message = (TextMessage) consumer.receive(15000);
			if (message != null) {
				logger.log(Logger.Level.ERROR, "FAILED: Message received before delivery delay of 30 secs elapsed");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Message not available after 15 seconds (CORRECT)");
				logger.log(Logger.Level.INFO, "Sleeping 15 more seconds before receiving message");
				Thread.sleep(15000);
				logger.log(Logger.Level.INFO, "Waiting 15 seconds to receive message");
				message = (TextMessage) consumer.receive(15000);
				if (message == null) {
					logger.log(Logger.Level.ERROR,
							"FAILED: Message was not received after delivery delay of 30 secs elapsed");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Message received after 30 seconds expired (CORRECT)");
				}
			}

			logger.log(Logger.Level.INFO, "Sending message with DeliveryMode.NON_PERSISTENT and DeliveryDelay=30Secs");
			producer.send(destination, message, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 0L);

			logger.log(Logger.Level.INFO, "Waiting 15 seconds to receive message");
			message = (TextMessage) consumer.receive(15000);
			if (message != null) {
				logger.log(Logger.Level.ERROR, "FAILED: Message received before delivery delay of 30 secs elapsed");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Message not available after 15 seconds (CORRECT)");
				logger.log(Logger.Level.INFO, "Sleeping 15 more seconds before receiving message");
				Thread.sleep(15000);
				logger.log(Logger.Level.INFO, "Waiting 15 seconds to receive message");
				message = (TextMessage) consumer.receive(15000);
				if (message == null) {
					logger.log(Logger.Level.ERROR,
							"FAILED: Message was not received after delivery delay of 30 secs elapsed");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Message received after 30 seconds expired (CORRECT)");
				}
			}
			logger.log(Logger.Level.INFO, "---------------------------------------------------------");
			logger.log(Logger.Level.INFO, "END TEST queueDeliveryDelayTest with DeliveryDelay=30Secs");
			logger.log(Logger.Level.INFO, "---------------------------------------------------------");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("queueDeliveryDelayTest", e);
		}

		if (!pass) {
			throw new Exception("queueDeliveryDelayTest failed");
		}
	}

	/*
	 * @testName: topicSendAndRecvTest1
	 *
	 * @assertion_ids: JMS:JAVADOC:321; JMS:JAVADOC:334;
	 *
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 *
	 * MessageProducer.send(Destination, Message) MessageConsumer.receive(timeout)
	 */
	@Test
	public void topicSendAndRecvTest1() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Topic) null);
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = false;

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicSendAndRecvTest1");
			logger.log(Logger.Level.INFO, "Sending TextMessage");
			logger.log(Logger.Level.INFO, "Calling MessageProducer.send(Destination, Message)");
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Receive TextMessage");
			TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the value in TextMessage");
			if (actTextMessage.getText().equals(expTextMessage.getText())) {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
						+ ", received " + actTextMessage.getText());
				pass = false;
			}
			consumer.close();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("topicSendAndRecvTest1", e);
		} finally {
			try {
				producer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("topicSendAndRecvTest1 failed");
		}
	}

	/*
	 * @testName: topicSendAndRecvTest2
	 *
	 * @assertion_ids: JMS:JAVADOC:323; JMS:JAVADOC:334;
	 *
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 *
	 * MessageProducer.send(Destination, Message, int, int, long)
	 * MessageConsumer.receive(timeout)
	 */
	@Test
	public void topicSendAndRecvTest2() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Topic) null);
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = false;

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicSendAndRecvTest2");
			logger.log(Logger.Level.INFO, "Sending TextMessage");
			logger.log(Logger.Level.INFO, "Calling MessageProducer.send(Destination, Message, int, int, long)");
			producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY - 1, 0L);
			logger.log(Logger.Level.INFO, "Receive TextMessage");
			TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the values in TextMessage, deliverymode, priority, time to live");
			if (!actTextMessage.getText().equals(expTextMessage.getText())
					|| actTextMessage.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT
					|| actTextMessage.getJMSPriority() != (Message.DEFAULT_PRIORITY - 1)
					|| actTextMessage.getJMSExpiration() != 0L) {
				logger.log(Logger.Level.ERROR, "Didn't get the right message.");
				logger.log(Logger.Level.ERROR,
						"text=" + actTextMessage.getText() + ", expected " + expTextMessage.getText());
				logger.log(Logger.Level.ERROR, "DeliveryMode=" + actTextMessage.getJMSDeliveryMode() + ", expected "
						+ expTextMessage.getJMSDeliveryMode());
				logger.log(Logger.Level.ERROR, "Priority=" + actTextMessage.getJMSPriority() + ", expected "
						+ expTextMessage.getJMSPriority());
				logger.log(Logger.Level.ERROR, "TimeToLive=" + actTextMessage.getJMSExpiration() + ", expected "
						+ expTextMessage.getJMSExpiration());
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("topicSendAndRecvTest2", e);
		} finally {
			try {
				producer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("topicSendAndRecvTest2 failed");
		}
	}

	/*
	 * @testName: topicSendAndRecvTest3
	 *
	 * @assertion_ids: JMS:JAVADOC:317; JMS:JAVADOC:334;
	 *
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 *
	 * MessageProducer.send(Message) MessageConsumer.receive(timeout)
	 */
	@Test
	public void topicSendAndRecvTest3() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			producer = tool.getDefaultProducer();
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = false;

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicSendAndRecvTest3");
			logger.log(Logger.Level.INFO, "Sending TextMessage");
			logger.log(Logger.Level.INFO, "Calling MessageProducer.send(Message)");
			producer.send(expTextMessage);
			logger.log(Logger.Level.INFO, "Receive TextMessage");
			TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the value in TextMessage");
			if (actTextMessage.getText().equals(expTextMessage.getText())) {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
						+ ", received " + actTextMessage.getText());
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("topicSendAndRecvTest3", e);
		}

		if (!pass) {
			throw new Exception("topicSendAndRecvTest3 failed");
		}
	}

	/*
	 * @testName: topicSendAndRecvTest4
	 *
	 * @assertion_ids: JMS:JAVADOC:319; JMS:JAVADOC:334;
	 *
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 *
	 * MessageProducer.send(Message, int, int, long)
	 * MessageConsumer.receive(timeout)
	 */
	@Test
	public void topicSendAndRecvTest4() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			producer = tool.getDefaultProducer();
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = false;

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicSendAndRecvTest4");
			logger.log(Logger.Level.INFO, "Sending TextMessage");
			logger.log(Logger.Level.INFO, "Calling MessageProducer.send(Message, int, int, long)");
			producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY - 1, 0L);
			logger.log(Logger.Level.INFO, "Receive TextMessage");
			TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the values in TextMessage, deliverymode, priority, time to live");
			if (!actTextMessage.getText().equals(expTextMessage.getText())
					|| actTextMessage.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT
					|| actTextMessage.getJMSPriority() != (Message.DEFAULT_PRIORITY - 1)
					|| actTextMessage.getJMSExpiration() != 0L) {
				logger.log(Logger.Level.ERROR, "Didn't get the right message.");
				logger.log(Logger.Level.ERROR,
						"text=" + actTextMessage.getText() + ", expected " + expTextMessage.getText());
				logger.log(Logger.Level.ERROR, "DeliveryMode=" + actTextMessage.getJMSDeliveryMode() + ", expected "
						+ expTextMessage.getJMSDeliveryMode());
				logger.log(Logger.Level.ERROR, "Priority=" + actTextMessage.getJMSPriority() + ", expected "
						+ expTextMessage.getJMSPriority());
				logger.log(Logger.Level.ERROR, "TimeToLive=" + actTextMessage.getJMSExpiration() + ", expected "
						+ expTextMessage.getJMSExpiration());
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("topicSendAndRecvTest4", e);
		}

		if (!pass) {
			throw new Exception("topicSendAndRecvTest4 failed");
		}
	}

	/*
	 * @testName: topicSetGetDeliveryModeTest
	 *
	 * @assertion_ids: JMS:JAVADOC:301; JMS:JAVADOC:303;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * MessageProducer.setDeliveryMode(int). MessageProducer.getDeliveryMode().
	 */
	@Test
	public void topicSetGetDeliveryModeTest() throws Exception {
		boolean pass = true;

		// Test default case
		try {
			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			producer = tool.getDefaultProducer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			queueTest = false;

			int expDeliveryMode = DeliveryMode.PERSISTENT;
			logger.log(Logger.Level.INFO, "Calling getDeliveryMode and expect " + expDeliveryMode + " to be returned");
			int actDeliveryMode = producer.getDeliveryMode();
			if (actDeliveryMode != expDeliveryMode) {
				logger.log(Logger.Level.ERROR,
						"getDeliveryMode() returned " + actDeliveryMode + ", expected " + expDeliveryMode);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("topicSetGetDeliveryModeTest");
		}

		// Test non-default case
		try {
			int expDeliveryMode = DeliveryMode.NON_PERSISTENT;
			logger.log(Logger.Level.INFO, "Calling setDeliveryMode(" + expDeliveryMode + ")");
			producer.setDeliveryMode(expDeliveryMode);
			logger.log(Logger.Level.INFO, "Calling getDeliveryMode and expect " + expDeliveryMode + " to be returned");
			int actDeliveryMode = producer.getDeliveryMode();
			if (actDeliveryMode != expDeliveryMode) {
				logger.log(Logger.Level.ERROR,
						"getDeliveryMode() returned " + actDeliveryMode + ", expected " + expDeliveryMode);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("topicSetGetDeliveryModeTest");
		}

		if (!pass) {
			throw new Exception("topicSetGetDeliveryModeTest failed");
		}
	}

	/*
	 * @testName: topicSetGetDeliveryDelayTest
	 *
	 * @assertion_ids: JMS:JAVADOC:907; JMS:JAVADOC:886; JMS:SPEC:261;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * MessageProducer.setDeliveryDelay(long). MessageProducer.getDeliveryDelay().
	 */
	@Test
	public void topicSetGetDeliveryDelayTest() throws Exception {
		boolean pass = true;

		// Test default case
		try {
			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			producer = tool.getDefaultProducer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			queueTest = false;

			long expDeliveryDelay = 0L;
			logger.log(Logger.Level.INFO,
					"Calling getDeliveryDelay and expect " + expDeliveryDelay + " to be returned");
			long actDeliveryDelay = producer.getDeliveryDelay();
			if (actDeliveryDelay != expDeliveryDelay) {
				logger.log(Logger.Level.ERROR,
						"getDeliveryDelay() returned " + actDeliveryDelay + ", expected " + expDeliveryDelay);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("topicSetGetDeliveryDelayTest");
		}

		// Test non-default case
		try {
			long expDeliveryDelay = 1L;
			logger.log(Logger.Level.INFO, "Calling setDeliveryDelay(" + expDeliveryDelay + ")");
			producer.setDeliveryDelay(expDeliveryDelay);
			logger.log(Logger.Level.INFO,
					"Calling getDeliveryDelay and expect " + expDeliveryDelay + " to be returned");
			long actDeliveryDelay = producer.getDeliveryDelay();
			if (actDeliveryDelay != expDeliveryDelay) {
				logger.log(Logger.Level.ERROR,
						"getDeliveryDelay() returned " + actDeliveryDelay + ", expected " + expDeliveryDelay);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("topicSetGetDeliveryDelayTest");
		}

		if (!pass) {
			throw new Exception("topicSetGetDeliveryDelayTest failed");
		}
	}

	/*
	 * @testName: topicSetGetDisableMessageIDTest
	 *
	 * @assertion_ids: JMS:JAVADOC:293; JMS:JAVADOC:295;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * MessageProducer.setDisableMessageID(int).
	 * MessageProducer.getDisableMessageID().
	 */
	@Test
	public void topicSetGetDisableMessageIDTest() throws Exception {
		boolean pass = true;
		// Test true case
		try {
			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			producer = tool.getDefaultProducer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			queueTest = false;

			boolean expDisableMessageID = true;
			logger.log(Logger.Level.INFO, "Calling setDisableMessageID(" + expDisableMessageID + ")");
			producer.setDisableMessageID(expDisableMessageID);
			logger.log(Logger.Level.INFO,
					"Calling getDisableMessageID and expect " + expDisableMessageID + " to be returned");
			boolean actDisableMessageID = producer.getDisableMessageID();
			if (actDisableMessageID != expDisableMessageID) {
				logger.log(Logger.Level.ERROR,
						"getDisableMessageID() returned " + actDisableMessageID + ", expected " + expDisableMessageID);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("topicSetGetDisableMessageIDTest");
		}

		// Test false case
		try {
			boolean expDisableMessageID = false;
			logger.log(Logger.Level.INFO, "Calling setDisableMessageID(" + expDisableMessageID + ")");
			producer.setDisableMessageID(expDisableMessageID);
			logger.log(Logger.Level.INFO,
					"Calling getDisableMessageID and expect " + expDisableMessageID + " to be returned");
			boolean actDisableMessageID = producer.getDisableMessageID();
			if (actDisableMessageID != expDisableMessageID) {
				logger.log(Logger.Level.ERROR,
						"getDisableMessageID() returned " + actDisableMessageID + ", expected " + expDisableMessageID);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("topicSetGetDisableMessageIDTest");
		}

		if (!pass) {
			throw new Exception("topicSetGetDisableMessageIDTest failed");
		}
	}

	/*
	 * @testName: topicSetGetDisableMessageTimestampTest
	 *
	 * @assertion_ids: JMS:JAVADOC:297; JMS:JAVADOC:299;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * MessageProducer.setDisableMessageTimestamp(int).
	 * MessageProducer.getDisableMessageTimestamp().
	 */
	@Test
	public void topicSetGetDisableMessageTimestampTest() throws Exception {
		boolean pass = true;
		// Test true case
		try {
			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			producer = tool.getDefaultProducer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			queueTest = false;

			boolean expDisableMessageTimestamp = true;
			logger.log(Logger.Level.INFO, "Calling setDisableMessageTimestamp(" + expDisableMessageTimestamp + ")");
			producer.setDisableMessageTimestamp(expDisableMessageTimestamp);
			logger.log(Logger.Level.INFO,
					"Calling getDisableMessageTimestamp and expect " + expDisableMessageTimestamp + " to be returned");
			boolean actDisableMessageTimestamp = producer.getDisableMessageTimestamp();
			if (actDisableMessageTimestamp != expDisableMessageTimestamp) {
				logger.log(Logger.Level.ERROR, "getDisableMessageTimestamp() returned " + actDisableMessageTimestamp
						+ ", expected " + expDisableMessageTimestamp);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("topicSetGetDisableMessageTimestampTest");
		}

		// Test false case
		try {
			boolean expDisableMessageTimestamp = false;
			logger.log(Logger.Level.INFO, "Calling setDisableMessageTimestamp(" + expDisableMessageTimestamp + ")");
			producer.setDisableMessageTimestamp(expDisableMessageTimestamp);
			logger.log(Logger.Level.INFO,
					"Calling getDisableMessageTimestamp and expect " + expDisableMessageTimestamp + " to be returned");
			boolean actDisableMessageTimestamp = producer.getDisableMessageTimestamp();
			if (actDisableMessageTimestamp != expDisableMessageTimestamp) {
				logger.log(Logger.Level.ERROR, "getDisableMessageTimestamp() returned " + actDisableMessageTimestamp
						+ ", expected " + expDisableMessageTimestamp);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("topicSetGetDisableMessageTimestampTest");
		}

		if (!pass) {
			throw new Exception("topicSetGetDisableMessageTimestampTest failed");
		}
	}

	/*
	 * @testName: topicSetGetPriorityTest
	 *
	 * @assertion_ids: JMS:JAVADOC:305; JMS:JAVADOC:307;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * MessageProducer.setPriority(int). MessageProducer.getPriority().
	 */
	@Test
	public void topicSetGetPriorityTest() throws Exception {
		boolean pass = true;
		try {
			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			producer = tool.getDefaultProducer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			queueTest = false;

			// Test default
			int expPriority = 4;
			logger.log(Logger.Level.INFO, "Calling getPriority and expect " + expPriority + " to be returned");
			int actPriority = producer.getPriority();
			if (actPriority != expPriority) {
				logger.log(Logger.Level.ERROR, "getPriority() returned " + actPriority + ", expected " + expPriority);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("topicSetGetPriorityTest");
		}

		// Test non-default
		int expPriority[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

		// Cycle through all priorties
		for (int i = 0; i < expPriority.length; i++) {
			try {
				logger.log(Logger.Level.INFO, "Calling setPriority(" + expPriority[i] + ")");
				producer.setPriority(expPriority[i]);
				logger.log(Logger.Level.INFO, "Calling getPriority and expect " + expPriority[i] + " to be returned");
				int actPriority = producer.getPriority();
				if (actPriority != expPriority[i]) {
					logger.log(Logger.Level.ERROR,
							"getPriority() returned " + actPriority + ", expected " + expPriority[i]);
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
				throw new Exception("topicSetGetPriorityTest");
			}
		}

		if (!pass) {
			throw new Exception("topicSetGetPriorityTest failed");
		}
	}

	/*
	 * @testName: topicSetGetTimeToLiveTest
	 *
	 * @assertion_ids: JMS:JAVADOC:309; JMS:JAVADOC:311;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * MessageProducer.setTimeToLive(long). MessageProducer.getTimeToLive()
	 * 
	 * public void topicSetGetTimeToLiveTest() throws Exception { boolean pass =
	 * true;
	 * 
	 * try { // set up test tool for Topic tool = new JmsTool(JmsTool.COMMON_T,
	 * user, password, mode); producer = tool.getDefaultProducer(); connection =
	 * tool.getDefaultConnection(); session = tool.getDefaultSession(); destination
	 * = tool.getDefaultDestination(); queueTest = false;
	 * 
	 * // Test default long expTimeToLive = 0;
	 * logger.log(Logger.Level.INFO,"Calling getTimeToLive and expect " +
	 * expTimeToLive + " to be returned"); long actTimeToLive =
	 * producer.getTimeToLive(); if(actTimeToLive != expTimeToLive) {
	 * logger.log(Logger.Level.ERROR,"getTimeToLive() returned "+ actTimeToLive +
	 * ", expected " + expTimeToLive); pass = false; }
	 * 
	 * // Test non-default long expTimeToLive = 1000;
	 * logger.log(Logger.Level.INFO,"Calling setTimeToLive("+expTimeToLive+")");
	 * producer.setTimeToLive(expTimeToLive);
	 * logger.log(Logger.Level.INFO,"Calling getTimeToLive and expect " +
	 * expTimeToLive + " to be returned"); long actTimeToLive =
	 * producer.getTimeToLive(); if(actTimeToLive != expTimeToLive) {
	 * logger.log(Logger.Level.ERROR,"getTimeToLive() returned "+ actTimeToLive +
	 * ", expected " + expTimeToLive); pass = false; } } catch (Exception e) {
	 * logger.log(Logger.Level.ERROR,"Caught exception: " + e.getMessage()); throw
	 * new Fault("topicSetGetTimeToLiveTest"); }
	 * 
	 * if (!pass) { throw new Exception("topicSetGetTimeToLiveTest failed"); } }
	 * 
	 * /*
	 * 
	 * @testName: topicInvalidDestinationExceptionTests
	 *
	 * @assertion_ids: JMS:JAVADOC:598; JMS:JAVADOC:601; JMS:JAVADOC:604;
	 * JMS:JAVADOC:607;
	 *
	 * @test_Strategy: Test for InvalidDestinationException from MessageProducer
	 * API's.
	 * 
	 * MessageProducer.send(Destination, Message) MessageProducer.send(Destination,
	 * Message, init, int, int)
	 */
	@Test
	public void topicInvalidDestinationExceptionTests() throws Exception {
		boolean pass = true;
		TextMessage tempMsg = null;
		String message = "Where are you!";
		try {
			// set up test tool for Topic
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON TOPIC");
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Queue) null);
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = null;
			connection.start();
			queueTest = false;

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicInvalidDestinationExceptionTests");

			try {
				logger.log(Logger.Level.INFO, "Send message with invalid destination");
				producer.send(destination, expTextMessage);
				logger.log(Logger.Level.ERROR, "Didn't throw InvalidDestinationException");
			} catch (InvalidDestinationException e) {
				logger.log(Logger.Level.INFO, "Caught expected InvalidDestinationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Send message with invalid destination");
				producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 0L);
				logger.log(Logger.Level.ERROR, "Didn't throw InvalidDestinationException");
			} catch (InvalidDestinationException e) {
				logger.log(Logger.Level.INFO, "Caught expected InvalidDestinationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationException, received " + e.getMessage());
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("queueInvalidDestinationExceptionTests", e);
		} finally {
			try {
				producer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("topicInvalidDestinationExceptionTests failed");
		}
	}

	/*
	 * @testName: topicUnsupportedOperationExceptionTests
	 *
	 * @assertion_ids: JMS:JAVADOC:599; JMS:JAVADOC:602; JMS:JAVADOC:605;
	 * JMS:JAVADOC:1318;
	 *
	 * @test_Strategy: Test for UnsupportedOperationException from MessageProducer
	 * API's.
	 *
	 * MessageProducer.send(Destination, Message) MessageProducer.send(Destination,
	 * Message, init, int, int) MessageProducer.send(Message)
	 * MessageProducer.send(Message, init, int, int)
	 * 
	 */
	@Test
	public void topicUnsupportedOperationExceptionTests() throws Exception {
		boolean pass = true;
		TextMessage tempMsg = null;
		String message = "Where are you!";
		try {
			// set up test tool for Topic
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON TOPIC");
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			producer = tool.getDefaultProducer();
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = false;

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicUnsupportedOperationExceptionTests");

			try {
				logger.log(Logger.Level.INFO, "Send message with destination specified at creation time");
				producer.send(destination, expTextMessage);
				logger.log(Logger.Level.ERROR, "Didn't throw UnsupportedOperationException");
			} catch (UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected UnsupportedOperationException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Send message with destination specified at creation time");
				producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 0L);
				logger.log(Logger.Level.ERROR, "Didn't throw UnsupportedOperationException");
			} catch (UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected UnsupportedOperationException, received " + e.getMessage());
				pass = false;
			}

			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Queue) null);

			try {
				logger.log(Logger.Level.INFO, "Send message with destination not specified at creation time");
				producer.send(expTextMessage);
				logger.log(Logger.Level.ERROR, "Didn't throw UnsupportedOperationException");
			} catch (UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected UnsupportedOperationException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Send message with destination not specified at creation time");
				producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 0L);
				logger.log(Logger.Level.ERROR, "Didn't throw UnsupportedOperationException");
			} catch (UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected UnsupportedOperationException, received " + e.getMessage());
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("topicUnsupportedOperationExceptionTests", e);
		} finally {
			try {
				producer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("topicUnsupportedOperationExceptionTests failed");
		}
	}

	/*
	 * @testName: topicDeliveryDelayTest
	 * 
	 * @assertion_ids: JMS:SPEC:261; JMS:JAVADOC:907;
	 * 
	 * @test_Strategy: Send message and verify that message is not delivered until
	 * the DeliveryDelay of 30 seconds is reached. Test DeliveryMode.PERSISTENT and
	 * DeliveryMode.NON_PERSISTENT.
	 * 
	 * MessageProducer.setDeliveryDelay(30000) MessageProducer.send(Destination,
	 * Message, int, int, long)
	 */
	@Test
	public void topicDeliveryDelayTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "-----------------------------------------------------------");
			logger.log(Logger.Level.INFO, "BEGIN TEST topicDeliveryDelayTest with DeliveryDelay=30Secs");
			logger.log(Logger.Level.INFO, "-----------------------------------------------------------");
			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Queue) null);
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = false;

			producer.setDeliveryDelay(30000);

			// Send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage message = session.createTextMessage("This is a test!");

			logger.log(Logger.Level.INFO, "Set StringProperty COM_SUN_JMS_TESTNAME");
			message.setStringProperty("COM_SUN_JMS_TESTNAME", "topicDeliveryDelayTest");

			logger.log(Logger.Level.INFO, "Sending message with DeliveryMode.PERSISTENT and DeliveryDelay=30Secs");
			producer.send(destination, message, DeliveryMode.PERSISTENT, Message.DEFAULT_PRIORITY, 0L);

			logger.log(Logger.Level.INFO, "Waiting 15 seconds to receive message");
			message = (TextMessage) consumer.receive(15000);
			if (message != null) {
				logger.log(Logger.Level.ERROR, "FAILED: Message received before delivery delay of 30 secs elapsed");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Message not available after 15 seconds (CORRECT)");
				logger.log(Logger.Level.INFO, "Sleeping 15 more seconds before receiving message");
				Thread.sleep(15000);
				logger.log(Logger.Level.INFO, "Waiting 15 seconds to receive message");
				message = (TextMessage) consumer.receive(15000);
				if (message == null) {
					logger.log(Logger.Level.ERROR,
							"FAILED: Message was not received after delivery delay of 30 secs elapsed");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Message received after 30 seconds expired (CORRECT)");
				}
			}

			logger.log(Logger.Level.INFO, "Sending message with DeliveryMode.NON_PERSISTENT and DeliveryDelay=30Secs");
			producer.send(destination, message, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 0L);

			logger.log(Logger.Level.INFO, "Waiting 15 seconds to receive message");
			message = (TextMessage) consumer.receive(15000);
			if (message != null) {
				logger.log(Logger.Level.ERROR, "FAILED: Message received before delivery delay of 30 secs elapsed");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Message not available after 15 seconds (CORRECT)");
				logger.log(Logger.Level.INFO, "Sleeping 15 more seconds before receiving message");
				Thread.sleep(15000);
				logger.log(Logger.Level.INFO, "Waiting 15 seconds to receive message");
				message = (TextMessage) consumer.receive(15000);
				if (message == null) {
					logger.log(Logger.Level.ERROR,
							"FAILED: Message was not received after delivery delay of 30 secs elapsed");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Message received after 30 seconds expired (CORRECT)");
				}
			}
			logger.log(Logger.Level.INFO, "---------------------------------------------------------");
			logger.log(Logger.Level.INFO, "END TEST topicDeliveryDelayTest with DeliveryDelay=30Secs");
			logger.log(Logger.Level.INFO, "---------------------------------------------------------");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			e.printStackTrace();
			throw new Exception("topicDeliveryDelayTest", e);
		}

		if (!pass) {
			throw new Exception("topicDeliveryDelayTest failed");
		}
	}

	/*
	 * @testName: JMSExceptionTests
	 *
	 * @assertion_ids: JMS:JAVADOC:302; JMS:JAVADOC:306; JMS:JAVADOC:908;
	 * JMS:JAVADOC:310; JMS:JAVADOC:320;
	 *
	 * @test_Strategy: Test for JMSException from MessageProducer API's.
	 * 
	 * MessageProducer.setPriority(int) MessageProducer.setDeliveryMode(int)
	 * MessageProducer.send(Message, int, int, long) MessageProducer.send(Message,
	 * int, int, long, CompletionListener) MessageProducer.send(Destination,
	 * Message, int, int, long) MessageProducer.send(Destination, Message, int, int,
	 * long, CompletionListener)
	 */
	@Test
	public void JMSExceptionTests() throws Exception {
		boolean pass = true;
		TextMessage tempMsg = null;
		String message = "Where are you!";
		try {
			// set up test tool for Queue
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON QUEUE");
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			producer = tool.getDefaultProducer();
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = true;

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "JMSExceptionTests");

			try {
				logger.log(Logger.Level.INFO, "Try and set an invalid priority of -1");
				logger.log(Logger.Level.INFO, "Calling MessageProducer.setPriorty(-1)");
				producer.setPriority(-1);
				logger.log(Logger.Level.ERROR, "Didn't throw JMSException");
				pass = false;
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Try and set an invalid delivery mode of -1");
				logger.log(Logger.Level.INFO, "Calling MessageProducer.setDeliveryMode(-1)");
				producer.setDeliveryMode(-1);
				logger.log(Logger.Level.ERROR, "Didn't throw JMSException");
				pass = false;
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Try and send message with delivery mode of -1");
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Message, -1, Message.DEFAULT_PRIORITY, 0L)");
				producer.send(expTextMessage, -1, Message.DEFAULT_PRIORITY, 0L);
				logger.log(Logger.Level.ERROR, "Didn't throw JMSException");
				pass = false;
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Try and send message with priority of -1");
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Message, DeliveryMode.NON_PERSISTENT, -1, 0L)");
				producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT, -1, 0L);
				logger.log(Logger.Level.ERROR, "Didn't throw JMSException");
				pass = false;
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Try and send message with delivery mode of -1");
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Message, -1, Message.DEFAULT_PRIORITY, 0L, CompletionListener");
				producer.send(expTextMessage, -1, Message.DEFAULT_PRIORITY, 0L, new MyCompletionListener());
				logger.log(Logger.Level.ERROR, "Didn't throw JMSException");
				pass = false;
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Try and send message with priority of -1");
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Message, DeliveryMode.NON_PERSISTENT, -1, 0L, CompletionListener");
				producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT, -1, 0L, new MyCompletionListener());
				logger.log(Logger.Level.ERROR, "Didn't throw JMSException");
				pass = false;
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e.getMessage());
				pass = false;
			}

			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Queue) null);

			try {
				logger.log(Logger.Level.INFO, "Try and send message with delivery mode of -1");
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Destination, Message, -1, Message.DEFAULT_PRIORITY, 0L)");
				producer.send(destination, expTextMessage, -1, Message.DEFAULT_PRIORITY, 0L);
				logger.log(Logger.Level.ERROR, "Didn't throw JMSException");
				pass = false;
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Try and send message with priority of -1");
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Destination, Message, DeliveryMode.NON_PERSISTENT, -1, 0L)");
				producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT, -1, 0L);
				logger.log(Logger.Level.ERROR, "Didn't throw JMSException");
				pass = false;
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Try and send message with delivery mode of -1");
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Destination, Message, -1, Message.DEFAULT_PRIORITY, 0L, CompletionListener)");
				producer.send(destination, expTextMessage, -1, Message.DEFAULT_PRIORITY, 0L,
						new MyCompletionListener());
				logger.log(Logger.Level.ERROR, "Didn't throw JMSException");
				pass = false;
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Try and send message with priority of -1");
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Destination, Message, DeliveryMode.NON_PERSISTENT, -1, 0L, CompletionListener)");
				producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT, -1, 0L,
						new MyCompletionListener());
				logger.log(Logger.Level.ERROR, "Didn't throw JMSException");
				pass = false;
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e.getMessage());
				pass = false;
			}

			cleanup();

			// set up test tool for Topic
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON TOPIC");
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			producer = tool.getDefaultProducer();
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = false;

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "JMSExceptionTests");

			try {
				logger.log(Logger.Level.INFO, "Try and set an invalid priority of -1");
				logger.log(Logger.Level.INFO, "Calling MessageProducer.setPriorty(-1)");
				producer.setPriority(-1);
				logger.log(Logger.Level.ERROR, "Didn't throw JMSException");
				pass = false;
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Try and set an invalid delivery mode of -1");
				logger.log(Logger.Level.INFO, "Calling MessageProducer.setDeliveryMode(-1)");
				producer.setDeliveryMode(-1);
				logger.log(Logger.Level.ERROR, "Didn't throw JMSException");
				pass = false;
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Try and send message with delivery mode of -1");
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Message, -1, Message.DEFAULT_PRIORITY, 0L)");
				producer.send(expTextMessage, -1, Message.DEFAULT_PRIORITY, 0L);
				logger.log(Logger.Level.ERROR, "Didn't throw JMSException");
				pass = false;
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Try and send message with priority of -1");
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Message, DeliveryMode.NON_PERSISTENT, -1, 0L)");
				producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT, -1, 0L);
				logger.log(Logger.Level.ERROR, "Didn't throw JMSException");
				pass = false;
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Try and send message with delivery mode of -1");
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Message, -1, Message.DEFAULT_PRIORITY, 0L, CompletionListener");
				producer.send(expTextMessage, -1, Message.DEFAULT_PRIORITY, 0L, new MyCompletionListener());
				logger.log(Logger.Level.ERROR, "Didn't throw JMSException");
				pass = false;
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Try and send message with priority of -1");
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Message, DeliveryMode.NON_PERSISTENT, -1, 0L, CompletionListener");
				producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT, -1, 0L, new MyCompletionListener());
				logger.log(Logger.Level.ERROR, "Didn't throw JMSException");
				pass = false;
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e.getMessage());
				pass = false;
			}

			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Topic) null);

			try {
				logger.log(Logger.Level.INFO, "Try and send message with delivery mode of -1");
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Destination, Message, -1, Message.DEFAULT_PRIORITY, 0L)");
				producer.send(destination, expTextMessage, -1, Message.DEFAULT_PRIORITY, 0L);
				logger.log(Logger.Level.ERROR, "Didn't throw JMSException");
				pass = false;
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Try and send message with priority of -1");
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Destination, Message, DeliveryMode.NON_PERSISTENT, -1, 0L)");
				producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT, -1, 0L);
				logger.log(Logger.Level.ERROR, "Didn't throw JMSException");
				pass = false;
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Try and send message with delivery mode of -1");
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Destination, Message, -1, Message.DEFAULT_PRIORITY, 0L, CompletionListener)");
				producer.send(destination, expTextMessage, -1, Message.DEFAULT_PRIORITY, 0L,
						new MyCompletionListener());
				logger.log(Logger.Level.ERROR, "Didn't throw JMSException");
				pass = false;
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e.getMessage());
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Try and send message with priority of -1");
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Destination, Message, DeliveryMode.NON_PERSISTENT, -1, 0L, CompletionListener)");
				producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT, -1, 0L,
						new MyCompletionListener());
				logger.log(Logger.Level.ERROR, "Didn't throw JMSException");
				pass = false;
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e.getMessage());
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e.getMessage());
			throw new Exception("JMSExceptionTests", e);
		}

		if (!pass) {
			throw new Exception("JMSExceptionTests failed");
		}
	}

	private static class MyCompletionListener implements CompletionListener {

		public MyCompletionListener() {
			logger.log(Logger.Level.INFO, "MyCompletionListener()");
		}

		public void onCompletion(Message message) {
			logger.log(Logger.Level.INFO, "onCompletion()");
		}

		public void onException(Message message, Exception exception) {
			logger.log(Logger.Level.INFO, "onException()");
		}
	}
}
