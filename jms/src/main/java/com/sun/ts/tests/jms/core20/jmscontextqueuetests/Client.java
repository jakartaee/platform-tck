/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jms.core20.jmscontextqueuetests;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.ConnectionMetaData;
import jakarta.jms.Destination;
import jakarta.jms.InvalidDestinationRuntimeException;
import jakarta.jms.InvalidSelectorRuntimeException;
import jakarta.jms.JMSConsumer;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.JMSProducer;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;
import jakarta.jms.TemporaryQueue;
import jakarta.jms.TextMessage;

public class Client {
	private static final String testName = "com.sun.ts.tests.jms.core20.jmscontextqueuetests.Client";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(Client.class.getName());

	// JMS tool which creates and/or looks up the JMS administered objects
	private transient JmsTool tool = null;

	// JMS objects
	private transient ConnectionFactory cf = null;

	private transient Queue queue = null;

	private transient Destination destination = null;

	private transient JMSContext context = null;

	private transient JMSContext contextToSendMsg = null;

	private transient JMSProducer producer = null;

	private transient JMSConsumer consumer = null;

	// Harness req's
	private Properties props = null;

	// properties read
	long timeout;

	String user;

	String password;

	String mode;

	String vehicle = null;

	// used for tests
	private static final int numMessages = 3;

	private static final int iterations = 5;

	ArrayList queues = null;

	ArrayList connections = null;

	/* Utility methods for tests */

	/*
	 * helper method verifies that the ConnectionMetaData
	 * 
	 * @param ConnectionMetaData returned from getJMSMessageID
	 * 
	 * @return boolean true if ConnectionMetaData is as expected
	 */
	private boolean verifyMetaData(ConnectionMetaData data) {
		boolean pass = true;

		try {
			String tmp = data.getJMSVersion();
			logger.log(Logger.Level.INFO, "JMSVersion=" + tmp);

			if (!tmp.equals(JmsTool.JMS_VERSION)) {
				logger.log(Logger.Level.ERROR, "Error: incorrect JMSVersion=" + tmp);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error: incorrect type returned for JMSVersion: ", e);
			pass = false;
		}

		try {
			int tmp = data.getJMSMajorVersion();
			logger.log(Logger.Level.INFO, "JMSMajorVersion=" + tmp);

			if (tmp != JmsTool.JMS_MAJOR_VERSION) {
				logger.log(Logger.Level.ERROR, "Error: incorrect JMSMajorVersion=" + tmp);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error: incorrect type returned for JMSMajorVersion: ", e);
			pass = false;
		}

		try {
			int tmp = data.getJMSMinorVersion();
			logger.log(Logger.Level.INFO, "JMSMinorVersion=" + tmp);

			if (tmp != JmsTool.JMS_MINOR_VERSION) {
				logger.log(Logger.Level.ERROR, "Error: incorrect JMSMinorVersion=" + tmp);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error: incorrect type returned for JMSMinorVersion: ", e);
			pass = false;
		}

		try {
			String tmp = data.getJMSProviderName();
			logger.log(Logger.Level.INFO, "JMSProviderName=" + tmp);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error: incorrect type returned for JMSProviderName: ", e);
			pass = false;
		}

		try {
			String tmp = data.getProviderVersion();
			logger.log(Logger.Level.INFO, "JMSProviderVersion=" + tmp);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error: incorrect type returned for ProviderVersion: ", e);
			pass = false;
		}

		try {
			int tmp = data.getProviderMajorVersion();
			logger.log(Logger.Level.INFO, "ProviderMajorVersion=" + tmp);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error: incorrect type returned for ProviderMajorVersion: ", e);
			pass = false;
		}

		try {
			int tmp = data.getProviderMinorVersion();
			logger.log(Logger.Level.INFO, "ProviderMinorVersion=" + tmp);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error: incorrect type returned for ProviderMinorVersion: ", e);
			pass = false;
		}
		return pass;
	}

	/*
	 * helper method verifies that the JMSMessageID starts with ID:
	 * 
	 * @param String returned from getJMSMessageID
	 * 
	 * @return boolean true if id correctly starts with ID:
	 */
	private boolean chkMessageID(String id) {
		String status[] = { "Pass", "Fail" };
		boolean retcode = true;

		// message id must start with ID: - unless it is null
		int index = 0;

		if (id == null) {
			;
		} else if (id.startsWith("ID:")) {
			;
		} else {
			index = 1;
			retcode = false;
		}
		logger.log(Logger.Level.INFO, "Results: " + status[index]);
		return retcode;
	}

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
			vehicle = System.getProperty("vehicle");

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
			queues = new ArrayList(3);
			connections = new ArrayList(5);

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
			producer = contextToSendMsg.createProducer();
			consumer = context.createConsumer(destination);
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
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "Close JMSContext Objects");
			if (context != null)
				context.close();
			context = null;
			if (contextToSendMsg != null)
				contextToSendMsg.close();
			contextToSendMsg = null;
			producer = null;
			logger.log(Logger.Level.INFO, "Flush any messages left on Queue");
			tool.flushDestination();
			logger.log(Logger.Level.INFO, "Close JMSConsumer objects");
			if (consumer != null)
				consumer.close();
			consumer = null;
			tool.closeAllResources();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("cleanup failed!", e);
		}

		if (!pass)
			throw new Exception("cleanup failed!");
	}

	/*
	 * @testName: createTemporayQueueTest
	 *
	 * @assertion_ids: JMS:JAVADOC:960;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * JMSContext.createTemporaryQueue()
	 *
	 * Send and receive a message to temporary queue. Compare send and recv message
	 * for equality.
	 */
	@Test
	public void createTemporayQueueTest() throws Exception {
		boolean pass = true;
		try {

			String sendMessage = "a text message";

			// create a TemporaryQueue
			logger.log(Logger.Level.INFO, "Creating TemporaryQueue");
			TemporaryQueue tempQueue = context.createTemporaryQueue();

			// Create a JMSConsumer for this Temporary Queue
			logger.log(Logger.Level.INFO, "Create JMSConsumer");
			JMSConsumer smc = context.createConsumer(tempQueue);

			// Send message to temporary queue
			logger.log(Logger.Level.INFO, "Send message to temporaty queue using JMSProducer");
			producer.send(tempQueue, sendMessage);

			// Receive message from temporary queue
			logger.log(Logger.Level.INFO, "Receive message from temporaty queue");
			String recvMessage = smc.receiveBody(String.class, timeout);

			logger.log(Logger.Level.INFO, "Checking received message");
			if (recvMessage == null) {
				throw new Exception("Did not receive Message");
			}

			if (!recvMessage.equals(sendMessage)) {
				logger.log(Logger.Level.ERROR,
						"Unexpected message: received " + recvMessage + " , expected " + sendMessage);
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "received correct message: " + recvMessage + " as expected");
			}

			logger.log(Logger.Level.INFO,
					"Attempting to delete temporary queue with an open consumer should not be allowed");
			try {
				tempQueue.delete();
				pass = false;
				logger.log(Logger.Level.ERROR, "TemporaryQueue.delete() didn't throw expected Exception");
			} catch (JMSException em) {
				logger.log(Logger.Level.INFO, "Received expected JMSException: ");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO, "Close JMSConsumer");
			smc.close();

			logger.log(Logger.Level.INFO,
					"Attempting to delete temporary queue with no open consumer should be allowed");
			try {
				tempQueue.delete();
			} catch (Exception e) {
				pass = false;
				logger.log(Logger.Level.ERROR, "Received unexpected Exception: ", e);
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("createTemporayQueueTest");
		}

		if (!pass) {
			throw new Exception("createTemporayQueueTest failed");
		}
	}

	/*
	 * @testName: createQueueBrowserTest
	 *
	 * @assertion_ids: JMS:JAVADOC:918; JMS:JAVADOC:921;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * JMSContext.createBrowser(Queue) JMSContext.createBrowser(Queue, String)
	 * 
	 * 1. Send x text messages to a Queue. 2. Create a QueueBrowser with selector to
	 * browse just the last message in the Queue. 3. Create a QueueBrowser again to
	 * browse all the messages in the queue.
	 */
	@Test
	public void createQueueBrowserTest() throws Exception {
		boolean pass = true;
		try {
			TextMessage tempMsg = null;
			QueueBrowser qBrowser = null;
			Enumeration msgs = null;

			// Close consumer created in setup() method
			consumer.close();
			consumer = null;

			// send "numMessages" messages to Queue plus end of stream message
			logger.log(Logger.Level.INFO, "Send " + numMessages + " messages to Queue");
			for (int i = 1; i <= numMessages; i++) {
				tempMsg = context.createTextMessage("Message " + i);
				tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "createQueueBrowserTest" + i);
				String tmp = null;
				if (i == numMessages) {
					tempMsg.setBooleanProperty("lastMessage", true);
					tmp = "with boolean property lastMessage=true";
				} else {
					tempMsg.setBooleanProperty("lastMessage", false);
					tmp = "with boolean property lastMessage=false";
				}
				producer.send(destination, tempMsg);
				logger.log(Logger.Level.INFO, "Message " + i + " sent " + tmp);
			}

			// create QueueBrowser to peek at last message in Queue using message
			// selector
			logger.log(Logger.Level.INFO,
					"Create QueueBrowser to peek at last message in Queue using message selector (lastMessage = TRUE)");
			qBrowser = context.createBrowser(queue, "lastMessage = TRUE");

			// check that browser just has the last message in the Queue
			logger.log(Logger.Level.INFO, "Check that browser has just the last message");
			int msgCount = 0;
			msgs = qBrowser.getEnumeration();
			while (msgs.hasMoreElements()) {
				tempMsg = (TextMessage) msgs.nextElement();
				logger.log(Logger.Level.INFO,
						"Text=" + tempMsg.getText() + ", lastMessage=" + tempMsg.getBooleanProperty("lastMessage"));
				if (!tempMsg.getText().equals("Message " + numMessages)) {
					logger.log(Logger.Level.ERROR,
							"Found [" + tempMsg.getText() + "] in browser expected only [Message 3]");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Found [Message " + numMessages + "] in browser");
				}
				msgCount++;
			}
			if (msgCount != 1) {
				logger.log(Logger.Level.ERROR, "Found " + msgCount + " messages in browser expected 1");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Found 1 message in browser (correct)");
			}
			qBrowser.close();

			// create QueueBrowser to peek at all messages in the Queue
			logger.log(Logger.Level.INFO, "Create QueueBrowser to browse all messages in the Queue");
			qBrowser = context.createBrowser(queue);

			// check for messages
			logger.log(Logger.Level.INFO, "Check that browser contains all " + numMessages + " messages");
			msgCount = 0;
			int msgIndex = 1;
			msgs = qBrowser.getEnumeration();
			while (msgs.hasMoreElements()) {
				tempMsg = (TextMessage) msgs.nextElement();
				if (!tempMsg.getText().equals("Message " + msgIndex)) {
					logger.log(Logger.Level.ERROR,
							"Found [" + tempMsg.getText() + "] in browser expected [Message " + msgIndex + "]");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Found [Message " + msgIndex + "] in browser (correct)");
				}
				msgCount++;
				msgIndex++;
			}
			if (msgCount != numMessages) {
				logger.log(Logger.Level.ERROR, "Found " + msgCount + " messages in browser expected " + numMessages);
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Found " + numMessages + " messages in browser (correct)");
			}
			qBrowser.close();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("createQueueBrowserTest");
		}

		if (!pass) {
			throw new Exception("createQueueBrowserTest failed");
		}
	}

	/*
	 * @testName: createConsumerTest
	 *
	 * @assertion_ids: JMS:JAVADOC:942; JMS:JAVADOC:945; JMS:JAVADOC:949;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * JMSContext.createConsumer(Destination) JMSContext.createConsumer(Destination,
	 * String) JMSContext.createConsumer(Destination, String, boolean)
	 * 
	 * 1. Send x text messages to a Queue. 2. Create a JMSConsumer with selector to
	 * consume just the last message in the Queue. 3. Create a JMSConsumer again to
	 * consume the rest of the messages in the Queue.
	 */
	@Test
	public void createConsumerTest() throws Exception {
		boolean pass = true;
		JMSConsumer consumerSelect = null;
		try {
			TextMessage tempMsg = null;
			Enumeration msgs = null;

			// Close normal JMSConsumer created in setup() method
			consumer.close();
			consumer = null;

			// Create selective JMSConsumer with message selector (lastMessage=TRUE)
			logger.log(Logger.Level.INFO, "Create selective JMSConsumer with selector [\"lastMessage=TRUE\"]");
			consumerSelect = context.createConsumer(destination, "lastMessage=TRUE");

			// send "numMessages" messages to Queue plus end of stream message
			logger.log(Logger.Level.INFO, "Send " + numMessages + " messages to Queue");
			for (int i = 1; i <= numMessages; i++) {
				tempMsg = context.createTextMessage("Message " + i);
				tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "createConsumerTest" + i);
				String tmp = null;
				if (i == numMessages) {
					tempMsg.setBooleanProperty("lastMessage", true);
					tmp = "with boolean property lastMessage=true";
				} else {
					tempMsg.setBooleanProperty("lastMessage", false);
					tmp = "with boolean property lastMessage=false";
				}
				producer.send(destination, tempMsg);
				logger.log(Logger.Level.INFO, "Message " + i + " sent " + tmp);
			}

			// Receive last message with selective consumer
			logger.log(Logger.Level.INFO,
					"Receive last message with selective JMSConsumer and boolean property (lastMessage=TRUE)");
			tempMsg = (TextMessage) consumerSelect.receive(timeout);
			if (tempMsg == null) {
				logger.log(Logger.Level.ERROR, "JMSConsumer.receive() returned NULL");
				logger.log(Logger.Level.ERROR, "Message " + numMessages + " missing from Queue");
				pass = false;
			} else if (!tempMsg.getText().equals("Message " + numMessages)) {
				logger.log(Logger.Level.ERROR,
						"Received [" + tempMsg.getText() + "] expected [Message " + numMessages + "]");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Received expected message: " + tempMsg.getText());
			}

			// Try to receive one more message (should return null)
			logger.log(Logger.Level.INFO, "Try receiving one more message (should get none)");
			tempMsg = (TextMessage) consumerSelect.receive(timeout);
			if (tempMsg != null) {
				logger.log(Logger.Level.ERROR, "JMSConsumer.receive() returned a message [" + tempMsg.getText() + "]");
				logger.log(Logger.Level.ERROR, "JMSConsumer with selector should have returned just 1 message");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Received no message (CORRECT)");
			}

			// Close selective JMSConsumer
			logger.log(Logger.Level.INFO, "Close selective JMSConsumer");
			consumerSelect.close();
			consumerSelect = null;

			// Create normal JMSConsumer
			logger.log(Logger.Level.INFO, "Create normal JMSConsumer");
			consumer = context.createConsumer(destination);

			// Receive rest of messages with normal JMSConsumer
			logger.log(Logger.Level.INFO, "Receive rest of messages with normal JMSConsumer");
			for (int msgCount = 1; msgCount < numMessages; msgCount++) {
				tempMsg = (TextMessage) consumer.receive(timeout);
				if (tempMsg == null) {
					logger.log(Logger.Level.ERROR, "JMSConsumer.receive() returned NULL");
					logger.log(Logger.Level.ERROR, "Message " + msgCount + " missing from Queue");
					pass = false;
				} else if (!tempMsg.getText().equals("Message " + msgCount)) {
					logger.log(Logger.Level.ERROR,
							"Received [" + tempMsg.getText() + "] expected [Message " + msgCount + "]");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Received message: " + tempMsg.getText());
				}
			}

			// Try to receive one more message (should return null)
			logger.log(Logger.Level.INFO, "Try receiving one more message (should get none)");
			tempMsg = (TextMessage) consumer.receive(timeout);
			if (tempMsg != null) {
				logger.log(Logger.Level.ERROR,
						"JMSConsumer returned message " + tempMsg.getText() + " (expected None)");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Received no message (CORRECT)");
			}

			// Close normal consumer
			logger.log(Logger.Level.INFO, "Close normal JMSConsumer");
			consumer.close();
			consumer = null;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("createConsumerTest");
		} finally {
			try {
				if (consumerSelect != null)
					consumerSelect.close();
				if (consumer != null)
					consumer.close();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			}
		}

		if (!pass) {
			throw new Exception("createConsumerTest failed");
		}
	}

	/*
	 * @testName: getMetaDataTest
	 *
	 * @assertion_ids: JMS:JAVADOC:982;
	 *
	 * @test_Strategy: Call JMSContext.getMetaData() to retrieve the
	 * ConnectionMetaData and then verify the ConnectionMetaData for correctness.
	 *
	 */
	@Test
	public void getMetaDataTest() throws Exception {
		boolean pass = true;
		ConnectionMetaData data = null;

		try {
			data = context.getMetaData();

			if (!verifyMetaData(data))
				pass = false;

		} catch (Exception e) {
			logger.log(Logger.Level.INFO, "Caught unexpected exception: " + e);
			pass = false;
		}

		if (!pass) {
			throw new Exception("getMetaDataTest failed");
		}
	}

	/*
	 * @testName: getSessionModeTest
	 *
	 * @assertion_ids: JMS:JAVADOC:986;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * JMSContext.getSessionMode().
	 *
	 * Cycle through all session modes to create each JMSContext with each mode and
	 * verify that each session mode is set correctly.
	 */
	@Test
	public void getSessionModeTest() throws Exception {
		boolean pass = true;
		JMSContext context = null;

		// Test default case
		try {
			context = cf.createContext(user, password);
			int expSessionMode = JMSContext.AUTO_ACKNOWLEDGE;
			logger.log(Logger.Level.INFO, "Calling getSessionMode and expect " + expSessionMode + " to be returned");
			int actSessionMode = context.getSessionMode();
			if (actSessionMode != expSessionMode) {
				logger.log(Logger.Level.ERROR,
						"getSessionMode() returned " + actSessionMode + ", expected " + expSessionMode);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("getSessionModeTest");
		} finally {
			try {
				if (context != null)
					context.close();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			}
		}

		// Test non-default case
		int expSessionMode[] = { JMSContext.SESSION_TRANSACTED, JMSContext.AUTO_ACKNOWLEDGE,
				JMSContext.CLIENT_ACKNOWLEDGE, JMSContext.DUPS_OK_ACKNOWLEDGE, };

		// Cycle through all session modes
		for (int i = 0; i < expSessionMode.length; i++) {
			if ((vehicle.equals("ejb") || vehicle.equals("jsp") || vehicle.equals("servlet"))) {
				if (expSessionMode[i] == JMSContext.SESSION_TRANSACTED
						|| expSessionMode[i] == JMSContext.CLIENT_ACKNOWLEDGE)
					continue;
			}
			try {
				logger.log(Logger.Level.INFO, "Creating context with session mode (" + expSessionMode[i] + ")");
				context = cf.createContext(user, password, expSessionMode[i]);
				logger.log(Logger.Level.INFO,
						"Calling getSessionMode and expect " + expSessionMode[i] + " to be returned");
				int actSessionMode = context.getSessionMode();
				if (actSessionMode != expSessionMode[i]) {
					logger.log(Logger.Level.ERROR,
							"getSessionMode() returned " + actSessionMode + ", expected " + expSessionMode[i]);
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught exception: " + e);
				throw new Exception("getSessionModeTest");
			} finally {
				try {
					if (context != null)
						context.close();
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				}
			}
		}

		if (!pass) {
			throw new Exception("getSessionModeTest failed");
		}
	}

	/*
	 * @testName: getTransactedTest
	 *
	 * @assertion_ids: JMS:JAVADOC:990;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * JMSContext.getTransacted().
	 *
	 * Create a JMSContext with JMSContext.AUTO_ACKNOWLEDGE and verify that
	 * JMSContext.getTransacted() returns false.
	 *
	 * Create a JMSContext with JMSContext.SESSION_TRANSACTED and verify that
	 * JMSContext.getTransacted() returns true.
	 */
	@Test
	public void getTransactedTest() throws Exception {
		boolean pass = true;
		JMSContext context = null;

		// Test for transacted mode false
		try {
			context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			logger.log(Logger.Level.INFO, "Calling getTransacted and expect false to be returned");
			boolean transacted = context.getTransacted();
			if (transacted) {
				logger.log(Logger.Level.ERROR, "getTransacted() returned true expected false");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			pass = false;
		} finally {
			try {
				if (context != null)
					context.close();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			}
		}

		// Test for transacted mode true
		if ((vehicle.equals("appclient") || vehicle.equals("standalone"))) {
			try {
				context = cf.createContext(user, password, JMSContext.SESSION_TRANSACTED);
				logger.log(Logger.Level.INFO, "Calling getTransacted and expect true to be returned");
				boolean transacted = context.getTransacted();
				if (!transacted) {
					logger.log(Logger.Level.ERROR, "getTransacted() returned false expected true");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught exception: " + e);
				throw new Exception("getTransactedTest");
			} finally {
				try {
					if (context != null)
						context.close();
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				}
			}
		}

		if (!pass) {
			throw new Exception("getTransactedTest failed");
		}
	}

	/*
	 * @testName: getClientIDTest
	 *
	 * @assertion_ids: JMS:JAVADOC:970;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * JMSContext.getClientID()
	 */
	@Test
	public void getClientIDTest() throws Exception {
		boolean pass = true;
		try {
			String cid = context.getClientID();
			logger.log(Logger.Level.ERROR, "getClientID() returned " + cid + ", expected clientid");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("getClientIDTest");
		}

		if (!pass) {
			throw new Exception("getClientIDTest failed");
		}
	}

	/*
	 * @testName: setGetAutoStartTest
	 *
	 * @assertion_ids: JMS:JAVADOC:1138; JMS:JAVADOC:1129;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * JMSContext.setAutoStart(boolean). JMSContext.getAutoStart().
	 */
	@Test
	public void setGetAutoStartTest() throws Exception {
		boolean pass = true;
		// Test default case
		try {
			boolean expAutoStart = true;
			logger.log(Logger.Level.INFO, "Calling getAutoStart and expect " + expAutoStart + " to be returned");
			boolean actAutoStart = context.getAutoStart();
			if (actAutoStart != expAutoStart) {
				logger.log(Logger.Level.ERROR,
						"getAutoStart() returned " + actAutoStart + ", expected " + expAutoStart);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			pass = false;
		}

		// Test non-default case
		try {
			boolean expAutoStart = false;
			logger.log(Logger.Level.INFO, "Calling setAutoStart(" + expAutoStart + ")");
			context.setAutoStart(expAutoStart);
			logger.log(Logger.Level.INFO, "Calling getAutoStart and expect " + expAutoStart + " to be returned");
			boolean actAutoStart = context.getAutoStart();
			if (actAutoStart != expAutoStart) {
				logger.log(Logger.Level.ERROR,
						"getAutoStart() returned " + actAutoStart + ", expected " + expAutoStart);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("setGetAutoStartTest");
		}

		if (!pass) {
			throw new Exception("setGetAutoStartTest failed");
		}
	}

	/*
	 * @testName: multipleCloseContextTest
	 * 
	 * @assertion_ids: JMS:JAVADOC:912; JMS:SPEC:108;
	 * 
	 * @test_Strategy: Call close() twice on a JMSContext. This MUST NOT throw an
	 * exception.
	 */
	@Test
	public void multipleCloseContextTest() throws Exception {
		try {
			logger.log(Logger.Level.INFO, "Call close on JMSContext created in setup.");
			context.close();
			logger.log(Logger.Level.INFO, "Call close on a JMSContext a second time");
			context.close();
		} catch (Exception e) {
			logger.log(Logger.Level.INFO, "Caught unexpected exception: " + e);
			throw new Exception("multipleCloseContextTest");
		}
	}

	/*
	 * @testName: invalidDestinationRuntimeExceptionTests
	 *
	 * @assertion_ids: JMS:JAVADOC:944; JMS:JAVADOC:947; JMS:JAVADOC:951;
	 * JMS:JAVADOC:920; JMS:JAVADOC:923; JMS:JAVADOC:1254; JMS:JAVADOC:1237;
	 * JMS:JAVADOC:1242; JMS:JAVADOC:1246; JMS:JAVADOC:1250;
	 *
	 * @test_Strategy: Test InvalidDestinationRuntimeException conditions from
	 * various API methods.
	 *
	 * JMSProducer.send(Destination, Message) JMSProducer.send(Destination, String)
	 * JMSProducer.send(Destination, Serializable) JMSProducer.send(Destination,
	 * byte[]) JMSProducer.send(Destination, Map)
	 * JMSContext.createConsumer(Destination) JMSContext.createConsumer(Destination,
	 * String) JMSContext.createConsumer(Destination, String, boolean)
	 * JMSContext.createBrowser(Queue, String) JMSContext.createBrowser(Queue)
	 */
	@Test
	public void invalidDestinationRuntimeExceptionTests() throws Exception {
		boolean pass = true;
		Destination invalidDestination = null;
		Queue invalidQueue = null;
		String message = "Where are you!";
		byte[] bytesMsgSend = message.getBytes();
		Map<String, Object> mapMsgSend = new HashMap<String, Object>();
		mapMsgSend.put("StringValue", "sendAndRecvTest7");
		mapMsgSend.put("BooleanValue", true);
		mapMsgSend.put("IntValue", (int) 10);
		try {
			// send to an invalid destination
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "invalidDestinationRuntimeExceptionTests");

			logger.log(Logger.Level.INFO,
					"Testing JMSProducer.send(Destination, Message) for InvalidDestinationRuntimeException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling send(Destination, Message) -> expect InvalidDestinationRuntimeException");
				producer.send(invalidDestination, expTextMessage);
			} catch (InvalidDestinationRuntimeException e) {
				logger.log(Logger.Level.INFO, "Got InvalidDestinationRuntimeException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationRuntimeException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing JMSProducer.send(Destination, String) for InvalidDestinationRuntimeException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling send(Destination, String) -> expect InvalidDestinationRuntimeException");
				producer.send(invalidDestination, message);
			} catch (InvalidDestinationRuntimeException e) {
				logger.log(Logger.Level.INFO, "Got InvalidDestinationRuntimeException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationRuntimeException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing JMSProducer.send(Destination, Serializable) for InvalidDestinationRuntimeException");
			logger.log(Logger.Level.INFO, "Send ObjectMessage");
			logger.log(Logger.Level.INFO, "Set some values in ObjectMessage");
			ObjectMessage om = context.createObjectMessage();
			StringBuffer sb = new StringBuffer(message);
			om.setObject(sb);
			om.setStringProperty("COM_SUN_JMS_TESTNAME", "invalidDestinationRuntimeExceptionTests");
			try {
				logger.log(Logger.Level.INFO,
						"Calling send(Destination, Serializable) -> expect InvalidDestinationRuntimeException");
				producer.send(invalidDestination, om);
			} catch (InvalidDestinationRuntimeException e) {
				logger.log(Logger.Level.INFO, "Got InvalidDestinationRuntimeException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationRuntimeException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing JMSProducer.send(Destination, byte[]) for InvalidDestinationRuntimeException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling send(Destination, byte[]) -> expect InvalidDestinationRuntimeException");
				producer.send(invalidDestination, bytesMsgSend);
			} catch (InvalidDestinationRuntimeException e) {
				logger.log(Logger.Level.INFO, "Got InvalidDestinationRuntimeException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationRuntimeException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing JMSProducer.send(Destination, Map) for InvalidDestinationRuntimeException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling send(Destination, Map) -> expect InvalidDestinationRuntimeException");
				producer.send(invalidDestination, mapMsgSend);
			} catch (InvalidDestinationRuntimeException e) {
				logger.log(Logger.Level.INFO, "Got InvalidDestinationRuntimeException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationRuntimeException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing JMSContext.createConsumer(Destination) for InvalidDestinationRuntimeException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling JMSContext.createConsumer(Destination) -> expect InvalidDestinationRuntimeException");
				context.createConsumer(invalidDestination);
			} catch (InvalidDestinationRuntimeException e) {
				logger.log(Logger.Level.INFO, "Got InvalidDestinationRuntimeException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationRuntimeException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing JMSContext.createConsumer(Destination, String) for InvalidDestinationRuntimeException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling JMSContext.createConsumer(Destination, String) -> expect InvalidDestinationRuntimeException");
				context.createConsumer(invalidDestination, "lastMessage = TRUE");
			} catch (InvalidDestinationRuntimeException e) {
				logger.log(Logger.Level.INFO, "Got InvalidDestinationRuntimeException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationRuntimeException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing JMSContext.createConsumer(Destination, String, boolean) for InvalidDestinationRuntimeException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling JMSContext.createConsumer(Destination, String, boolean) -> expect InvalidDestinationRuntimeException");
				context.createConsumer(invalidDestination, "lastMessage = TRUE", false);
			} catch (InvalidDestinationRuntimeException e) {
				logger.log(Logger.Level.INFO, "Got InvalidDestinationRuntimeException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationRuntimeException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing JMSContext.createBrowser(Queue, String) for InvalidDestinationRuntimeException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling JMSContext.createBrowser(Queue, String) -> expect InvalidDestinationRuntimeException");
				context.createBrowser(invalidQueue, "lastMessage = TRUE");
			} catch (InvalidDestinationRuntimeException e) {
				logger.log(Logger.Level.INFO, "Got InvalidDestinationRuntimeException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationRuntimeException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing JMSContext.createBrowser(Queue) for InvalidDestinationRuntimeException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling JMSContext.createBrowser(Queue) -> expect InvalidDestinationRuntimeException");
				context.createBrowser(invalidQueue);
			} catch (InvalidDestinationRuntimeException e) {
				logger.log(Logger.Level.INFO, "Got InvalidDestinationRuntimeException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationRuntimeException, received " + e);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("invalidDestinationRuntimeExceptionTests", e);
		}

		if (!pass) {
			throw new Exception("invalidDestinationRuntimeExceptionTests failed");
		}
	}

	/*
	 * @testName: invalidSelectorRuntimeExceptionTests
	 *
	 * @assertion_ids: JMS:JAVADOC:948; JMS:JAVADOC:952; JMS:JAVADOC:924;
	 *
	 * @test_Strategy: Test InvalidSelectorRuntimeException conditions from various
	 * API methods.
	 *
	 * JMSContext.createConsumer(Destination, String)
	 * JMSContext.createConsumer(Destination, String, boolean)
	 * JMSContext.createQueue(Queue, String)
	 */
	@Test
	public void invalidSelectorRuntimeExceptionTests() throws Exception {
		boolean pass = true;
		String invalidMessageSelector = "=TEST 'test'";
		try {

			logger.log(Logger.Level.INFO,
					"Testing JMSContext.createConsumer(Destination, String) for InvalidSelectorRuntimeException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling JMSContext.createConsumer(Destination, String) -> expect InvalidSelectorRuntimeException");
				context.createConsumer(destination, invalidMessageSelector);
			} catch (InvalidSelectorRuntimeException e) {
				logger.log(Logger.Level.INFO, "Got InvalidSelectorRuntimeException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidSelectorRuntimeException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing JMSContext.createConsumer(Destination, String, boolean) for InvalidSelectorRuntimeException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling JMSContext.createConsumer(Destination, String, boolean) -> expect InvalidSelectorRuntimeException");
				context.createConsumer(destination, invalidMessageSelector, false);
			} catch (InvalidSelectorRuntimeException e) {
				logger.log(Logger.Level.INFO, "Got InvalidSelectorRuntimeException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidSelectorRuntimeException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing JMSContext.createBrowser(Queue, String) for InvalidSelectorRuntimeException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling JMSContext.createBrowser(Queue, String) -> expect InvalidSelectorRuntimeException");
				context.createBrowser(queue, invalidMessageSelector);
			} catch (InvalidSelectorRuntimeException e) {
				logger.log(Logger.Level.INFO, "Got InvalidSelectorRuntimeException, as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidSelectorRuntimeException,, received " + e);
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("invalidSelectorRuntimeExceptionTests", e);
		}

		if (!pass) {
			throw new Exception("invalidSelectorRuntimeExceptionTests failed");
		}
	}

	/*
	 * @testName: illegalStateRuntimeExceptionTest
	 *
	 * @assertion_ids: JMS:JAVADOC:917; JMS:JAVADOC:994; JMS:JAVADOC:997;
	 * JMS:JAVADOC:1340;
	 *
	 * @test_Strategy: 1. Create a TextMessages and send to Queue 2. Then invoke
	 * JMSContext.commit() on a non-transacted session Verify that
	 * IllegalStateRuntimeException is thrown 3. Then test invoke
	 * JMSContext.rollback() on a non-transacted session Verify that
	 * IllegalStateRuntimeException is thrown 3. Then test invoke
	 * JMSContext.recover() on a transacted session Verify that
	 * IllegalStateRuntimeException is thrown 4. Create JMSContext with
	 * CLIENT_ACKNOWLEDGE then close JMSContext. Then test invoke
	 * JMSContext.acknowledge() on the JMSContext. Verify that
	 * IllegalStateRuntimeException is thrown
	 *
	 * JMSContext.commit(); JMSContext.rollback(); JMSContext.recover();
	 * JMSContext.acknowledge();
	 */
	@Test
	public void illegalStateRuntimeExceptionTest() throws Exception {
		boolean pass = true;
		String message = "Where are you!";

		try {
			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "illegalStateRuntimeExceptionTest");
			// send the message to the Queue
			logger.log(Logger.Level.INFO, "Sending message to the Queue");
			logger.log(Logger.Level.INFO, "Sending TextMessage via JMSProducer.send(Destination, Message)");
			producer.send(destination, expTextMessage);

			try {
				logger.log(Logger.Level.INFO,
						"JMSContext.commit() on non-transacted session must throw IllegalStateRuntimeException");
				context.commit();
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: JMSContext.commit() didn't throw expected IllegalStateRuntimeException");
			} catch (jakarta.jms.IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Got expected IllegalStateRuntimeException from JMSContext.commit()");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "illegalStateRuntimeExceptionTest");
			// send the message to the Queue
			logger.log(Logger.Level.INFO, "Sending message to the Queue");
			logger.log(Logger.Level.INFO, "Sending TextMessage via JMSProducer.send(Destination, Message)");
			producer.send(destination, expTextMessage);

			try {
				logger.log(Logger.Level.INFO,
						"JMSContext.rollback() on non-transacted session must throw IllegalStateRuntimeException");
				context.rollback();
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: JMSContext.rollback() didn't throw expected IllegalStateRuntimeException");
			} catch (jakarta.jms.IllegalStateRuntimeException e) {
				logger.log(Logger.Level.INFO, "Got expected IllegalStateRuntimeException from JMSContext.rollback()");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			if ((vehicle.equals("appclient") || vehicle.equals("standalone"))) {
				// create JMSContext with SESSION_TRANSACTED then create producer
				JMSContext contextTX = cf.createContext(user, password, JMSContext.SESSION_TRANSACTED);
				JMSProducer producerTX = contextTX.createProducer();

				// send and receive TextMessage
				logger.log(Logger.Level.INFO, "Creating TextMessage");
				expTextMessage = contextTX.createTextMessage(message);
				logger.log(Logger.Level.INFO, "Set some values in TextMessage");
				expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "illegalStateRuntimeExceptionTest");
				// send the message to the Queue
				logger.log(Logger.Level.INFO, "Sending message to the Queue");
				logger.log(Logger.Level.INFO, "Sending TextMessage via JMSProducer.send(Destination, Message)");
				producerTX.send(destination, expTextMessage);

				try {
					logger.log(Logger.Level.INFO,
							"JMSContext.recover() on a transacted session must throw IllegalStateRuntimeException");
					contextTX.recover();
					pass = false;
					logger.log(Logger.Level.ERROR,
							"Error: JMSContext.recover() didn't throw expected IllegalStateRuntimeException");
				} catch (jakarta.jms.IllegalStateRuntimeException e) {
					logger.log(Logger.Level.INFO,
							"Got expected IllegalStateRuntimeException from JMSContext.recover()");
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
					pass = false;
				}
				contextTX.close();

				try {
					logger.log(Logger.Level.INFO, "Create JMSContext with CLIENT_ACKNOWLEDGE");
					JMSContext msgcontext = cf.createContext(user, password, JMSContext.CLIENT_ACKNOWLEDGE);
					logger.log(Logger.Level.INFO, "Close JMSContext");
					msgcontext.close();
					logger.log(Logger.Level.INFO, "Call JMSContext.acknowledge() on a closed session which is illegal");
					msgcontext.acknowledge();
					logger.log(Logger.Level.ERROR, "Didn't throw IllegalStateRuntimeException");
					pass = false;
				} catch (jakarta.jms.IllegalStateRuntimeException e) {
					logger.log(Logger.Level.INFO,
							"Got expected IllegalStateRuntimeException from JMSContext.acknowledge()");
				} catch (Exception e) {
					logger.log(Logger.Level.INFO, "Caught expected exception" + e);
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.INFO, "Caught unexpected exception: " + e);
			throw new Exception("illegalStateRuntimeExceptionTest");
		}

		if (!pass) {
			throw new Exception("illegalStateRuntimeExceptionTest");
		}
	}
}
