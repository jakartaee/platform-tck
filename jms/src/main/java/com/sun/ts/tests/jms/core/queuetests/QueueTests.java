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
package com.sun.ts.tests.jms.core.queuetests;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.BytesMessage;
import jakarta.jms.Connection;
import jakarta.jms.DeliveryMode;
import jakarta.jms.JMSException;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageProducer;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;
import jakarta.jms.QueueConnection;
import jakarta.jms.QueueReceiver;
import jakarta.jms.QueueSender;
import jakarta.jms.QueueSession;
import jakarta.jms.Session;
import jakarta.jms.StreamMessage;
import jakarta.jms.TemporaryQueue;
import jakarta.jms.TextMessage;

public class QueueTests {
	private static final String testName = "com.sun.ts.tests.jms.core.queuetests.QueueTests";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(QueueTests.class.getName());

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

	// used for tests
	private static final int numMessages = 3;

	private static final int iterations = 5;

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
			throw new Exception("Didn't catch expected exception");
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
				throw new Exception("'user' in must not be null ");
			}
			if (password == null) {
				throw new Exception("'password' in must not be null ");
			}
			if (mode == null) {
				throw new Exception("'platform.mode' in must not be null");
			}
			queues = new ArrayList(10);
			connections = new ArrayList(5);

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("Setup failed!", e);
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
			logger.log(Logger.Level.INFO, "Cleanup: Closing Queue Connections");
			tool.doClientQueueTestCleanup(connections, queues);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "An error occurred while cleaning", e);
			throw new Exception("Cleanup failed!", e);
		}
	}

	/*
	 * @testName: emptyMsgsQueueTest
	 *
	 * @assertion_ids: JMS:SPEC:85; JMS:SPEC:88; JMS:JAVADOC:198; JMS:JAVADOC:334;
	 * JMS:JAVADOC:301; JMS:JAVADOC:209; JMS:JAVADOC:211; JMS:JAVADOC:213;
	 * JMS:JAVADOC:215; JMS:JAVADOC:217; JMS:JAVADOC:219; JMS:JAVADOC:221;
	 * JMS:JAVADOC:223;
	 *
	 * @test_Strategy: Send and receive empty messages of each type.
	 *
	 */
	@Test
	public void emptyMsgsQueueTest() throws Exception {
		try {

			// create Queue Connection
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueSender().setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			tool.getDefaultQueueConnection().start();

			// send and receive simple message to Queue
			logger.log(Logger.Level.INFO, "Send generic Message to Queue.");
			Message msg = tool.getDefaultQueueSession().createMessage();

			msg.setStringProperty("COM_SUN_JMS_TESTNAME", "emptyMsgsQueueTest");
			tool.getDefaultQueueSender().send(msg);
			if (tool.getDefaultQueueReceiver().receive(timeout) == null) {
				throw new Exception("Did not receive message");
			}

			// send and receive bytes message to Queue
			logger.log(Logger.Level.INFO, "Send BytesMessage to Queue.");
			BytesMessage bMsg = tool.getDefaultQueueSession().createBytesMessage();

			bMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "emptyMsgsQueueTest");
			tool.getDefaultQueueSender().send(bMsg);
			if (tool.getDefaultQueueReceiver().receive(timeout) == null) {
				throw new Exception("Did not receive message");
			}

			// send and receive map message to Queue
			logger.log(Logger.Level.INFO, "Send MapMessage to Queue.");
			MapMessage mMsg = tool.getDefaultQueueSession().createMapMessage();

			mMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "emptyMsgsQueueTest");
			tool.getDefaultQueueSender().send(mMsg);
			if (tool.getDefaultQueueReceiver().receive(timeout) == null) {
				throw new Exception("Did not receive message");
			}

			// send and receive object message to Queue
			logger.log(Logger.Level.INFO, "Send ObjectMessage to Queue.");
			ObjectMessage oMsg = tool.getDefaultQueueSession().createObjectMessage();

			oMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "emptyMsgsQueueTest");
			tool.getDefaultQueueSender().send(oMsg);
			if (tool.getDefaultQueueReceiver().receive(timeout) == null) {
				throw new Exception("Did not receive message");
			}

			// send and receive stream message to Queue
			logger.log(Logger.Level.INFO, "Send SreamMessage to Queue.");
			StreamMessage sMsg = tool.getDefaultQueueSession().createStreamMessage();

			sMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "emptyMsgsQueueTest");
			tool.getDefaultQueueSender().send(sMsg);
			if (tool.getDefaultQueueReceiver().receive(timeout) == null) {
				throw new Exception("Did not receive message");
			}

			// send and receive text message to Queue
			logger.log(Logger.Level.INFO, "Send TextMessage to Queue.");
			TextMessage tMsg = tool.getDefaultQueueSession().createTextMessage();

			tMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "emptyMsgsQueueTest");
			tool.getDefaultQueueSender().send(tMsg);
			if (tool.getDefaultQueueReceiver().receive(timeout) == null) {
				throw new Exception("Did not receive message");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("emptyMsgsQueueTest failed");
		}
	}

	/*
	 * @testName: autoAckQueueTest
	 *
	 * @assertion_ids: JMS:SPEC:132; JMS:JAVADOC:198; JMS:JAVADOC:334;
	 * JMS:JAVADOC:235;
	 *
	 * @test_Strategy: Send two messages to a queue. Receive the first message and
	 * call session.recover(). Attempt to receive the second message.
	 */
	@Test
	public void autoAckQueueTest() throws Exception {
		try {
			Message messageSent = null;
			Message messageReceived = null;

			// create queue setup
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();

			// create and send messages
			messageSent = tool.getDefaultQueueSession().createMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "autoAckQueueTest");

			logger.log(Logger.Level.TRACE, "send two messages");
			messageSent.setBooleanProperty("lastMessage", false);
			tool.getDefaultQueueSender().send(messageSent);
			messageSent.setBooleanProperty("lastMessage", true);
			tool.getDefaultQueueSender().send(messageSent);

			// receive message and call recover
			messageReceived = tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("Did not receive message");
			} else if (messageReceived.getBooleanProperty("lastMessage") == true) {
				throw new Exception("Error: received second message first");
			}
			logger.log(Logger.Level.TRACE, "Message received. Call recover.");
			tool.getDefaultQueueSession().recover();

			// attempt to receive second message
			messageReceived = tool.getDefaultQueueReceiver().receive(timeout);

			// check message
			if (messageReceived == null) {
				throw new Exception("Did not receive second message as expected");
			} else if (messageReceived.getBooleanProperty("lastMessage") == false) {
				throw new Exception("Received original message again");
			} else {
				logger.log(Logger.Level.INFO, "Did not receive message again - GOOD");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("autoAckQueueTest");
		}
	}

	/*
	 * @testName: simpleSendReceiveQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:138; JMS:JAVADOC:198; JMS:JAVADOC:334;
	 * JMS:JAVADOC:122;
	 * 
	 * @test_Strategy: Send and receive single message to verify that Queues are
	 * working.
	 */
	@Test
	public void simpleSendReceiveQueueTest() throws Exception {
		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.INFO, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createTextMessage();
			messageSent.setText("just a test");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "simpleSendReceiveQueueTest");
			logger.log(Logger.Level.INFO, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.INFO, "Receiving message");
			messageReceived = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);

			// Check to see if correct message received
			if (messageReceived.getText().equals(messageSent.getText())) {
				logger.log(Logger.Level.INFO, "Message text: \"" + messageReceived.getText() + "\"");
				logger.log(Logger.Level.INFO, "Received correct message");
			} else {
				throw new Exception("didn't get the right message");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("simpleSendReceiveQueueTest failed");
		}
	}

	/*
	 * @testName: messageOrderQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:146; JMS:SPEC:147; JMS:JAVADOC:198; JMS:JAVADOC:122;
	 * JMS:JAVADOC:334;
	 * 
	 * @test_Strategy: Send messages to a queue and receive them verify that the
	 * text of each matches the order of the text in the sent messages.
	 */
	@Test
	public void messageOrderQueueTest() throws Exception {
		try {
			TextMessage tempMsg;
			String text[] = new String[numMessages];

			// set up tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();

			// create and send messages to queue
			for (int i = 0; i < numMessages; i++) {
				text[i] = "message order test " + i;
				tempMsg = tool.getDefaultQueueSession().createTextMessage();
				tempMsg.setText(text[i]);
				tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "messageOrderQueueTest" + i);
				tool.getDefaultQueueSender().send(tempMsg);
				logger.log(Logger.Level.TRACE, "Sent message: " + tempMsg.getText());
			}

			// receive messages and verify order
			for (int i = 0; i < numMessages; i++) {
				tempMsg = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);
				if (tempMsg == null) {
					throw new Exception("cannot receive message");
				}
				logger.log(Logger.Level.TRACE, "Received message: " + tempMsg.getText());
				if (!tempMsg.getText().equals(text[i])) {
					logger.log(Logger.Level.INFO, "Received message: " + tempMsg.getText());
					logger.log(Logger.Level.INFO, "Should have: " + text[i]);
					throw new Exception("received wrong message");
				}
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("messageOrderQueueTest failed");
		}
	}

	/*
	 * @testName: temporaryQueueNotConsumableTest
	 * 
	 * @assertion_ids: JMS:SPEC:144; JMS:JAVADOC:194;
	 * 
	 * @test_Strategy: Create temporary queue and a separate QueueSession. Try to
	 * create a receiver for the temporary queue from the new session, which should
	 * throw a JMSException. Also sends a blank message to verify that the temporary
	 * queue is working.
	 */
	@Test
	public void temporaryQueueNotConsumableTest() throws Exception {
		boolean passed = false;

		try {

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();

			// create the TemporaryQueue
			logger.log(Logger.Level.INFO, "Creating TemporaryQueue");
			TemporaryQueue tempQ = tool.getDefaultQueueSession().createTemporaryQueue();

			// open a new connection, create Session and Sender
			logger.log(Logger.Level.INFO, "Creating new Connection");
			QueueConnection newQConn = (QueueConnection) tool.getNewConnection(JmsTool.QUEUE, user, password);
			connections.add(newQConn);

			logger.log(Logger.Level.INFO, "Create new Session");
			QueueSession newQSess = newQConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

			logger.log(Logger.Level.INFO, "Create new sender for TemporaryQueue");
			QueueSender newQSender = newQSess.createSender(tempQ);

			// send message to verify TemporaryQueue
			logger.log(Logger.Level.INFO, "Send message to TemporaryQueue");
			Message msg = tool.getDefaultQueueSession().createMessage();

			msg.setStringProperty("COM_SUN_JMS_TESTNAME", "temporaryQueueNotConsumableTest");
			newQSender.send(msg);

			// try to create receiver for the TemporaryQueue
			logger.log(Logger.Level.INFO, "Attempt to create receiver for TemporaryQueue from another Session");
			try {
				QueueReceiver newQReceiver = newQSess.createReceiver(tempQ);
				if (newQReceiver != null)
					logger.log(Logger.Level.TRACE, "newQReceiver=" + newQReceiver);
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Received expected JMSException -- GOOD");
				passed = true;
			}

			// close new connection
			logger.log(Logger.Level.TRACE, "Closing new QueueConnection");
			newQConn.close();

			// throw exception if test failed
			checkExceptionPass(passed);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("temporaryQueueNotConsumableTest failed");
		}
	}

	/*
	 * @testName: messageSelectorMsgRemainsOnQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:146; JMS:SPEC:147; JMS:JAVADOC:186;
	 * 
	 * @test_Strategy: Send two messages to a queue and receive the second one using
	 * a message selector. Verify that the first message is still in the queue by
	 * receiving with a different receiver.
	 */
	@Test
	public void messageSelectorMsgRemainsOnQueueTest() throws Exception {
		boolean pass = true;

		try {
			Message messageSent = null;
			Message messageReceived = null;

			// create Queue Connection
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);

			// create receiver with message selector (close default receiver)
			logger.log(Logger.Level.INFO, "Creating receiver with message selector");
			tool.getDefaultQueueReceiver().close();
			QueueReceiver qSelectiveReceiver = tool.getDefaultQueueSession().createReceiver(tool.getDefaultQueue(),
					"TEST = 'test'");

			// start connection
			tool.getDefaultQueueConnection().start();

			// send two messages
			logger.log(Logger.Level.TRACE, "Sending two Messages");
			messageSent = tool.getDefaultQueueSession().createMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "messageSelectorMsgRemainsOnQueueTest");

			messageSent.setBooleanProperty("lastMessage", false);
			tool.getDefaultQueueSender().send(messageSent);
			messageSent.setStringProperty("TEST", "test");
			messageSent.setBooleanProperty("lastMessage", true);
			tool.getDefaultQueueSender().send(messageSent);

			// check selective receiver
			logger.log(Logger.Level.TRACE, "Receiving message with selective receiver");
			messageReceived = qSelectiveReceiver.receive(timeout);
			if (messageReceived == null) {
				pass = false;
				logger.log(Logger.Level.ERROR, "Did not receive any message");
			} else if (messageReceived.getBooleanProperty("lastMessage") == false) {
				pass = false;
				logger.log(Logger.Level.ERROR, "Received incorrect message");
			} else {
				logger.log(Logger.Level.INFO, "Selective Receiver received correct message");
			}

			// receive original message with normal receiver
			qSelectiveReceiver.close();
			QueueReceiver qRec = tool.getDefaultQueueSession().createReceiver(tool.getDefaultQueue());
			messageReceived = qRec.receive(timeout);
			if (messageReceived == null) {
				pass = false;
				logger.log(Logger.Level.ERROR, "Un-received message did not remain on queue");
			} else if (messageReceived.getBooleanProperty("lastMessage") == true) {
				pass = false;
				logger.log(Logger.Level.ERROR, "received incorrect message");
			} else {
				logger.log(Logger.Level.INFO, "Corrected Message left on Queue is received");
			}

			if (!pass)
				throw new Exception("messageSelectorMsgRemainsOnQueueTest Failed!!");
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("messageSelectorMsgRemainsOnQueueTest failed");
		}
	}

	/*
	 * @testName: msgSelectorMsgHeaderQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:38; JMS:JAVADOC:186;
	 * 
	 * @test_Strategy: Create receiver with a message selector that uses a message
	 * header. Send two messages, one that has the proper header and one that
	 * doesn't, and try to receive only matching message.
	 */
	@Test
	public void msgSelectorMsgHeaderQueueTest() throws Exception {
		boolean pass = true;

		try {

			// create Queue Connection
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);

			// create receiver with message selector (close default receiver)
			logger.log(Logger.Level.INFO, "Creating receiver with message selector");
			tool.getDefaultQueueReceiver().close();
			QueueReceiver qSelectiveReceiver = tool.getDefaultQueueSession().createReceiver(tool.getDefaultQueue(),
					"JMSType = 'test_message'");

			// start connection
			tool.getDefaultQueueConnection().start();

			// send messages
			Message m = tool.getDefaultQueueSession().createMessage();
			m.setStringProperty("COM_SUN_JMS_TESTNAME", "msgSelectorMsgHeaderQueueTest");

			logger.log(Logger.Level.TRACE, "Sending message not matching selector");
			m.setJMSType("foo");
			m.setBooleanProperty("lastMessage", false);
			tool.getDefaultQueueSender().send(m);

			logger.log(Logger.Level.TRACE, "Sending test message with header property JMSType");
			m.setJMSType("test_message");
			m.setBooleanProperty("lastMessage", true);
			tool.getDefaultQueueSender().send(m);

			// attempt to receive correct message
			logger.log(Logger.Level.INFO, "Attempt to receive 'good' message");
			Message msg1 = qSelectiveReceiver.receive(timeout);
			qSelectiveReceiver.close();
			if (msg1 == null) {
				pass = false;
				logger.log(Logger.Level.ERROR, "Did not receive any message");
			} else if (msg1.getBooleanProperty("lastMessage") == true) {
				logger.log(Logger.Level.INFO, "Received correct message -- GOOD");
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "Received message not matching header");
			}

			// remove remaining message from queue
			QueueReceiver qRec = tool.getDefaultQueueSession().createReceiver(tool.getDefaultQueue());
			msg1 = qRec.receive(timeout);
			if (msg1 == null) {
				pass = false;
				logger.log(Logger.Level.ERROR, "No message received.");
			} else if (msg1.getBooleanProperty("lastMessage") == false) {
				logger.log(Logger.Level.INFO, "Received correct message left");
			} else {
				pass = false;
				logger.log(Logger.Level.ERROR, "Received incorrect message");
			}

			if (!pass)
				throw new Exception("msgSelectorMsgHeaderQueueTest failed!!");

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("msgSelectorMsgHeaderQueueTest failed");
		}
	}

	/*
	 * @testName: queueBrowserMsgsRemainOnQueueTest
	 *
	 * @assertion_ids: JMS:JAVADOC:186; JMS:JAVADOC:221; JMS:JAVADOC:411;
	 * JMS:JAVADOC:425; JMS:JAVADOC:120; JMS:JAVADOC:190; JMS:JAVADOC:282;
	 * JMS:JAVADOC:284; JMS:SPEC:148; JMS:SPEC:149; JMS:JAVADOC:122;
	 *
	 * @test_Strategy: 1. Create a Queue and send x messages to it. 2. Create a
	 * QueueReceiver with a message selector so that only last message received. 3.
	 * Create a QueueBrowser to browse the queue. 4. Then create another
	 * QueueReceiver to verify all x messages can be received from the Queue.
	 */
	@Test
	public void queueBrowserMsgsRemainOnQueueTest() throws Exception {
		try {
			TextMessage tempMsg = null;
			QueueReceiver lastMessageReceiver = null;
			QueueReceiver qRec = null;
			QueueBrowser qBrowser = null;
			Enumeration msgs = null;

			// create QueueConnection
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueReceiver().close();
			lastMessageReceiver = tool.getDefaultQueueSession().createReceiver(tool.getDefaultQueue(),
					"lastMessage = TRUE");
			tool.getDefaultQueueConnection().start();

			// send "numMessages" messages to queue plus end of stream message
			for (int i = 0; i <= numMessages; i++) {
				tempMsg = tool.getDefaultQueueSession().createTextMessage();
				if (i == numMessages) {
					tempMsg.setBooleanProperty("lastMessage", true);
				} else {
					tempMsg.setBooleanProperty("lastMessage", false);
				}
				tempMsg.setText("message" + i);
				tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "queueBrowserMsgsRemainOnQueueTest" + i);
				tool.getDefaultQueueSender().send(tempMsg);
				logger.log(Logger.Level.TRACE, "message " + i + " sent");
			}

			// receive final message, all messages now processed
			tempMsg = (TextMessage) lastMessageReceiver.receive(timeout);
			if (tempMsg == null) {
				throw new Exception("Did not receive expected message");
			} else {
				logger.log(Logger.Level.TRACE,
						"Received last sent message, lastMessage=" + tempMsg.getBooleanProperty("lastMessage"));
			}
			lastMessageReceiver.close();

			// create QueueBrowser
			logger.log(Logger.Level.TRACE, "Creating QueueBrowser");
			qBrowser = tool.getDefaultQueueSession().createBrowser(tool.getDefaultQueue());

			// check for messages
			logger.log(Logger.Level.INFO, "Checking for " + numMessages + " messages with QueueBrowser");
			int msgCount = 0;
			msgs = qBrowser.getEnumeration();
			while (msgs.hasMoreElements()) {
				tempMsg = (TextMessage) msgs.nextElement();
				msgCount++;
			}
			logger.log(Logger.Level.TRACE, "found " + msgCount + " messages total in browser");

			// check to see if all messages found
			if (msgCount != numMessages) {

				// not the test assertion, test can still pass
				logger.log(Logger.Level.INFO, "Warning: browser did not find all messages");
			}
			qBrowser.close();

			// continue and try to receive all messages
			qRec = tool.getDefaultQueueSession().createReceiver(tool.getDefaultQueue());
			logger.log(Logger.Level.INFO, "Receive all remaining messages to verify still in queue");
			for (msgCount = 0; msgCount < numMessages; msgCount++) {
				tempMsg = (TextMessage) qRec.receive(timeout);
				if (tempMsg == null) {
					throw new Exception("Message " + msgCount + " missing from queue");
				} else {
					logger.log(Logger.Level.TRACE, "received message: " + tempMsg.getText());
				}
			}

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("queueBrowserMsgsRemainOnQueueTest failed");
		}
	}

	/*
	 * @testName: inactiveClientReceiveQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:150;
	 * 
	 * @test_Strategy: Create a queue with no receiver and send messages to it.
	 * Create a new session with a receiver for the queue and verify that the
	 * messages can still be received.
	 */
	@Test
	public void inactiveClientReceiveQueueTest() throws Exception {
		try {
			TextMessage tempMsg;
			QueueSession qSession;
			QueueReceiver qReceiver;

			// create queue with no receiver
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueReceiver().close();
			tool.getDefaultQueueConnection().start();

			// create and send messages
			for (int i = 0; i < numMessages; i++) {
				tempMsg = tool.getDefaultQueueSession().createTextMessage();
				tempMsg.setText("message" + i);
				tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "inactiveClientReceiveQueueTest" + i);
				tool.getDefaultQueueSender().send(tempMsg);
				logger.log(Logger.Level.TRACE, "message " + i + " sent");
			}

			tool.getDefaultQueueSession().close();

			// create session with a receiver for the queue
			qSession = tool.getDefaultQueueConnection().createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			qReceiver = qSession.createReceiver(tool.getDefaultQueue());

			// attempt to receive all messages
			for (int i = 0; i < numMessages; i++) {
				tempMsg = (TextMessage) qReceiver.receive(timeout);
				if (tempMsg == null) {
					throw new Exception("Did not receive all messages");
				}
				logger.log(Logger.Level.TRACE, "received message " + i);
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("inactiveClientReceiveQueueTest failed");
		}
	}

	/*
	 * @testName: msgProducerNullDestinationQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:139; JMS:JAVADOC:188; JMS:JAVADOC:186;
	 * JMS:JAVADOC:198;
	 * 
	 * @test_Strategy: Create a q sender, specifying null for the destination send
	 * the message to the default destination and then verify receiving it
	 */
	@Test
	public void msgProducerNullDestinationQueueTest() throws Exception {
		boolean pass = true;
		QueueSender qSender = null;
		Queue nullQ = null;
		TextMessage messageSent = null;
		TextMessage messageReceived = null;

		try {
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating  1 message");
			messageSent = tool.getDefaultQueueSession().createTextMessage();
			messageSent.setText("test creating a producer without specifying the destination");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgProducerNullDestinationQueueTest");
			logger.log(Logger.Level.INFO, "Test createSender(null) - This is valid");
			try {
				qSender = tool.getDefaultQueueSession().createSender(nullQ);
				logger.log(Logger.Level.TRACE, "PASS: null allowed for unidentified producer");
			} catch (Exception ee) {
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				pass = false;
			}

			// publish to a queue and then get the message.
			logger.log(Logger.Level.TRACE, "Send a message");
			qSender.send(tool.getDefaultQueue(), messageSent);
			logger.log(Logger.Level.TRACE, "Receive a message");
			messageReceived = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived == null) {
				pass = false;
			} else {
				logger.log(Logger.Level.TRACE, "Got message - OK");
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during msgProducerNullDestinationQueueTest tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("msgProducerNullDestinationQueueTest failed");
		}
	}

	/*
	 * @testName: multipleCloseQueueConnectionTest
	 * 
	 * @assertion_ids: JMS:SPEC:108;
	 * 
	 * @test_Strategy: Call close() twice on a connection and catch any exception.
	 */
	@Test
	public void multipleCloseQueueConnectionTest() throws Exception {
		try {

			// create Queue Connection
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Call close on a connection ");
			tool.getDefaultQueueConnection().close();
			logger.log(Logger.Level.TRACE, "Call close on a connection a second time");
			tool.getDefaultQueueConnection().close();
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("multipleCloseQueueConnectionTest failed");
		}
	}

	/*
	 * @testName: messageOrderDeliveryModeQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:127; JMS:SPEC:128; JMS:JAVADOC:122;
	 * 
	 * @test_Strategy: Send non persistent messages to a queue and receive them.
	 * Verify that the text of each matches the order of the text in the sent
	 * messages.
	 */
	@Test
	public void messageOrderDeliveryModeQueueTest() throws Exception {
		try {
			TextMessage tempMsg;
			String text[] = new String[numMessages];

			// set up tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();

			// Persistent delivery is the default
			tool.getDefaultQueueSender().setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			// create and send messages to queue
			for (int i = 0; i < numMessages; i++) {
				text[i] = "message order test " + i;
				tempMsg = tool.getDefaultQueueSession().createTextMessage();
				tempMsg.setText(text[i]);
				tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "messageOrderDeliveryModeQueueTest" + i);
				tool.getDefaultQueueSender().send(tempMsg);
				logger.log(Logger.Level.TRACE, "Sent message: " + tempMsg.getText());
			}

			// receive messages and verify order
			for (int i = 0; i < numMessages; i++) {
				tempMsg = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);
				if (tempMsg == null) {
					throw new Exception("cannot receive message");
				}
				logger.log(Logger.Level.TRACE, "Received message: " + tempMsg.getText());
				if (!tempMsg.getText().equals(text[i])) {
					logger.log(Logger.Level.INFO, "Received message: " + tempMsg.getText());
					logger.log(Logger.Level.INFO, "Should have: " + text[i]);
					throw new Exception("received wrong message");
				}
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("messageOrderDeliveryModeQueueTest failed");
		}
	}

	/*
	 * @testName: tempQueueTests
	 *
	 * @assertion_ids: JMS:SPEC:144; JMS:JAVADOC:262; JMS:JAVADOC:126;
	 *
	 * @test_Strategy: 1. Create a TemporaryQueue from a Session. Send a TextMessage
	 * and Receive it using the TemporaryQueue. Verify the Message received
	 * correctly. 2. Try to delete the TemporaryQueue without closing
	 * MessageConsumer, verify that JMSException is thrown. 3. Close the
	 * MessageConsumer, verify that the TemporaryQueue can be deleted. 4. Try to
	 * create a MessageConsumer using Session from a different Connection, verify
	 * that JMSException is thrown.
	 */
	@Test
	public void tempQueueTests() throws Exception {
		boolean pass = true;
		TextMessage msgSent;
		TextMessage msgReceived;
		String testName = "tempQueueTests";
		String message = "Just a test from tempQueueTests";
		TemporaryQueue tempQ = null;
		Connection newConn = null;

		try {
			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			tool.getDefaultProducer().close();
			tool.getDefaultConsumer().close();
			tool.getDefaultConnection().start();

			// create the TemporaryQueue
			logger.log(Logger.Level.INFO, "Creating TemporaryQueue");
			tempQ = tool.getDefaultSession().createTemporaryQueue();

			// open a new connection, create Session and Sender
			logger.log(Logger.Level.INFO, "Create new sender for TemporaryQueue");
			MessageProducer sender = tool.getDefaultSession().createProducer(tempQ);
			MessageConsumer receiver = tool.getDefaultSession().createConsumer(tempQ);

			// send message to verify TemporaryQueue
			logger.log(Logger.Level.INFO, "Send message to TemporaryQueue");
			msgSent = tool.getDefaultSession().createTextMessage();
			msgSent.setText(message);
			msgSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			sender.send(msgSent);

			// try to create receiver for the TemporaryQueue
			msgReceived = (TextMessage) receiver.receive(timeout);

			if (msgReceived == null) {
				pass = false;
				logger.log(Logger.Level.ERROR, "didnot receive message");
			} else if (!msgReceived.getText().equals(message)) {
				pass = false;
				logger.log(Logger.Level.ERROR, "Received wrong message=" + msgReceived.getText());
				logger.log(Logger.Level.ERROR, "Should have: " + message);
			}

			try {
				tempQ.delete();
				pass = false;
				logger.log(Logger.Level.ERROR, "Didn't throw expected JMSException calling TemporaryQueue.delete()");
			} catch (JMSException em) {
				logger.log(Logger.Level.TRACE, "Received expected JMSException: ");
			}
			receiver.close();

			try {
				tempQ.delete();
				logger.log(Logger.Level.TRACE, "Succesfully calling TemporaryQueue.delete() after closing Receiver");
			} catch (Exception e) {
				pass = false;
				logger.log(Logger.Level.ERROR, "Received unexpected Exception: ", e);
			}

			tempQ = tool.getDefaultSession().createTemporaryQueue();
			newConn = (Connection) tool.getNewConnection(JmsTool.COMMON_Q, user, password);
			connections.add(newConn);
			Session newSess = newConn.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// try to create receiver for the TemporaryQueue
			logger.log(Logger.Level.INFO,
					"Attempt to create MessageConsumer for TemporaryQueue from another Connection");
			try {
				MessageConsumer newReceiver = newSess.createConsumer(tempQ);
				if (newReceiver != null)
					logger.log(Logger.Level.TRACE, "newReceiver=" + newReceiver);
				logger.log(Logger.Level.TRACE, "FAIL: expected IllegalStateException");
				logger.log(Logger.Level.ERROR,
						"Didn't throw expected JMSException calling Session.createConsumer(TemporaryQueue)");
			} catch (JMSException e) {
				logger.log(Logger.Level.TRACE, "Received expected JMSException from createConsumer.");
			}

			if (!pass)
				throw new Exception(testName + " failed");

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception(testName);
		}
	}
}
