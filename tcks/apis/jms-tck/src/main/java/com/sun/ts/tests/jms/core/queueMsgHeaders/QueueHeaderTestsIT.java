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

package com.sun.ts.tests.jms.core.queueMsgHeaders;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.BytesMessage;
import jakarta.jms.DeliveryMode;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.StreamMessage;
import jakarta.jms.TextMessage;


public class QueueHeaderTestsIT {
	private static final String testName = "com.sun.ts.tests.jms.core.queueMsgHeaders.QueueHeaderTestsIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(QueueHeaderTestsIT.class.getName());

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
				throw new Exception("'jms_timeout' (milliseconds) in must be > 0");
			}
			if (user == null) {
				throw new Exception("'user' is null");
			}
			if (password == null) {
				throw new Exception("'password' is null");
			}
			if (mode == null) {
				throw new Exception("'mode' is null");
			}
			queues = new ArrayList(2);

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
				logger.log(Logger.Level.INFO, "Cleanup: Closing Queue and Topic Connections");
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
	 * helper method for msgHdrIDQTest and msgHdrIDTopicTest verifies that the
	 * JMSMessageID starts with ID:
	 * 
	 * 
	 * @param String returned from getJMSMessageID
	 * 
	 * @return boolean true if id correctly starts with ID:
	 */
	@Test
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
		logger.log(Logger.Level.TRACE, "Results: " + status[index]);
		return retcode;
	}

	/*
	 * @testName: msgHdrIDQTest
	 * 
	 * @assertion_ids: JMS:SPEC:4; JMS:JAVADOC:343;
	 * 
	 * @test_Strategy: Send to a Queue and receive Text, Map, Bytes, Stream, and
	 * object message. call getJMSMessageID and verify that it starts with ID:
	 */
	@Test
	public void msgHdrIDQTest() throws Exception {
		boolean pass = true;
		byte bValue = 127;
		String id = null;

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			StreamMessage messageSentS = null;
			StreamMessage messageReceivedS = null;
			BytesMessage messageSentB = null;
			BytesMessage messageReceivedB = null;
			MapMessage messageReceivedM = null;
			MapMessage messageSentM = null;
			ObjectMessage messageSentO = null;
			ObjectMessage messageReceivedO = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();

			// send and receive Object message to Queue
			logger.log(Logger.Level.TRACE, "Send ObjectMessage to Queue.");
			messageSentO = tool.getDefaultQueueSession().createObjectMessage();
			messageSentO.setObject("msgHdrIDQTest for ObjectMessage");
			messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrIDQTest");
			tool.getDefaultQueueSender().send(messageSentO);
			messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "getJMSMessageID ");
			logger.log(Logger.Level.TRACE, " " + messageReceivedO.getJMSMessageID());
			id = messageReceivedO.getJMSMessageID();
			if (!chkMessageID(id)) {
				logger.log(Logger.Level.INFO, "ObjectMessage error: JMSMessageID does not start with ID:");
				pass = false;
			}
			// send and receive map message to Queue
			logger.log(Logger.Level.TRACE, "Send MapMessage to Queue.");
			messageSentM = tool.getDefaultQueueSession().createMapMessage();
			messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrIDQTest");
			messageSentM.setString("aString", "value");
			tool.getDefaultQueueSender().send(messageSentM);
			messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "getJMSMessageID ");
			logger.log(Logger.Level.TRACE, " " + messageReceivedM.getJMSMessageID());
			id = messageReceivedM.getJMSMessageID();
			if (!chkMessageID(id)) {
				logger.log(Logger.Level.INFO, "MapMessage error: JMSMessageID does not start with ID:");
				pass = false;
			}

			// send and receive bytes message to Queue
			logger.log(Logger.Level.TRACE, "Send BytesMessage to Queue.");
			messageSentB = tool.getDefaultQueueSession().createBytesMessage();
			messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrIDQTest");
			messageSentB.writeByte(bValue);
			tool.getDefaultQueueSender().send(messageSentB);
			messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "getJMSMessageID ");
			logger.log(Logger.Level.TRACE, " " + messageReceivedB.getJMSMessageID());
			id = messageReceivedB.getJMSMessageID();
			if (!chkMessageID(id)) {
				logger.log(Logger.Level.INFO, "BytesMessage error: JMSMessageID does not start with ID:");
				pass = false;
			}

			// Send and receive a StreamMessage
			logger.log(Logger.Level.TRACE, "sending a StreamMessage");
			messageSentS = tool.getDefaultQueueSession().createStreamMessage();
			messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrIDQTest");
			messageSentS.writeString("Testing...");
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSentS);
			messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "getJMSMessageID ");
			logger.log(Logger.Level.TRACE, " " + messageReceivedS.getJMSMessageID());
			id = messageReceivedS.getJMSMessageID();
			if (!chkMessageID(id)) {
				logger.log(Logger.Level.INFO, "StreamMessage error: JMSMessageID does not start with ID:");
				pass = false;
			}

			// TextMessage
			messageSent = tool.getDefaultQueueSession().createTextMessage();
			messageSent.setText("sending a TextMessage");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrIDQTest");
			logger.log(Logger.Level.TRACE, "sending a TextMessage");
			tool.getDefaultQueueSender().send(messageSent);
			messageReceived = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "getJMSMessageID ");
			logger.log(Logger.Level.TRACE, " " + messageReceived.getJMSMessageID());
			id = messageReceived.getJMSMessageID();
			if (!chkMessageID(id)) {
				logger.log(Logger.Level.INFO, "TextMessage error: JMSMessageID does not start with ID:");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: invalid JMSMessageID returned from JMSMessageID");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("msgHdrIDQTest");
		}
	}

	/*
	 * @testName: msgHdrTimeStampQTest
	 * 
	 * @assertion_ids: JMS:SPEC:7; JMS:JAVADOC:347;
	 * 
	 * @test_Strategy: With a queue destination Send a single Text, map, bytes,
	 * stream, and object message check time of send against time send returns
	 * JMSTimeStamp should be between these two
	 */
	@Test
	public void msgHdrTimeStampQTest() throws Exception {
		boolean pass = true;
		long timeBeforeSend;
		long timeAfterSend;
		byte bValue = 127;

		try {
			TextMessage messageSent = null;
			StreamMessage messageSentS = null;
			BytesMessage messageSentB = null;
			MapMessage messageSentM = null;
			ObjectMessage messageSentO = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();

			// send and receive Object message to Queue
			logger.log(Logger.Level.TRACE, "Send ObjectMessage to Queue.");
			messageSentO = tool.getDefaultQueueSession().createObjectMessage();
			messageSentO.setObject("msgHdrTimeStampQTest for ObjectMessage");
			messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrTimeStampQTest");

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			tool.getDefaultQueueSender().send(messageSentO);

			// message has been sent
			timeAfterSend = System.currentTimeMillis();
			logger.log(Logger.Level.TRACE, " getJMSTimestamp");
			logger.log(Logger.Level.TRACE, " " + messageSentO.getJMSTimestamp());
			logger.log(Logger.Level.TRACE, "Time at send is: " + timeBeforeSend);
			logger.log(Logger.Level.TRACE, "Time after return fromsend is:" + timeAfterSend);
			if ((timeBeforeSend <= messageSentO.getJMSTimestamp())
					&& (timeAfterSend >= messageSentO.getJMSTimestamp())) {
				logger.log(Logger.Level.TRACE, "ObjectMessage TimeStamp pass");
			} else {
				logger.log(Logger.Level.INFO, "Error: invalid timestamp from ObjectMessage");
				pass = false;
			}

			// send map message to Queue
			logger.log(Logger.Level.TRACE, "Send MapMessage to Queue.");
			messageSentM = tool.getDefaultQueueSession().createMapMessage();
			messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrTimeStampQTest");
			messageSentM.setString("aString", "value");

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			tool.getDefaultQueueSender().send(messageSentM);

			// message has been sent
			timeAfterSend = System.currentTimeMillis();
			logger.log(Logger.Level.TRACE, " getJMSTimestamp");
			logger.log(Logger.Level.TRACE, " " + messageSentM.getJMSTimestamp());
			logger.log(Logger.Level.TRACE, "Time at send is: " + timeBeforeSend);
			logger.log(Logger.Level.TRACE, "Time after return fromsend is:" + timeAfterSend);
			if (!(timeBeforeSend <= messageSentM.getJMSTimestamp())
					&& (timeAfterSend >= messageSentM.getJMSTimestamp())) {
				logger.log(Logger.Level.ERROR, "Error: invalid timestamp from MapMessage");
				pass = false;
			}

			// send and receive bytes message to Queue
			logger.log(Logger.Level.TRACE, "Send BytesMessage to Queue.");
			messageSentB = tool.getDefaultQueueSession().createBytesMessage();
			messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrTimeStampQTest");
			messageSentB.writeByte(bValue);

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			tool.getDefaultQueueSender().send(messageSentB);

			// message has been sent
			timeAfterSend = System.currentTimeMillis();
			logger.log(Logger.Level.TRACE, " getJMSTimestamp");
			logger.log(Logger.Level.TRACE, " " + messageSentB.getJMSTimestamp());
			logger.log(Logger.Level.TRACE, "Time at send is: " + timeBeforeSend);
			logger.log(Logger.Level.TRACE, "Time after return fromsend is:" + timeAfterSend);
			if ((timeBeforeSend <= messageSentB.getJMSTimestamp())
					&& (timeAfterSend >= messageSentB.getJMSTimestamp())) {
				logger.log(Logger.Level.TRACE, "BytesMessage TimeStamp pass");
			} else {
				logger.log(Logger.Level.INFO, "Error: invalid timestamp from BytesMessage");
				pass = false;
			}

			// Send and receive a StreamMessage
			logger.log(Logger.Level.TRACE, "sending a StreamMessage");
			messageSentS = tool.getDefaultQueueSession().createStreamMessage();
			messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrTimeStampQTest");
			messageSentS.writeString("Testing...");
			logger.log(Logger.Level.TRACE, "Sending message");

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			tool.getDefaultQueueSender().send(messageSentS);

			// message has been sent
			timeAfterSend = System.currentTimeMillis();
			logger.log(Logger.Level.TRACE, " getJMSTimestamp");
			logger.log(Logger.Level.TRACE, " " + messageSentS.getJMSTimestamp());
			logger.log(Logger.Level.TRACE, "Time at send is: " + timeBeforeSend);
			logger.log(Logger.Level.TRACE, "Time after return fromsend is:" + timeAfterSend);
			if ((timeBeforeSend <= messageSentS.getJMSTimestamp())
					&& (timeAfterSend >= messageSentS.getJMSTimestamp())) {
				logger.log(Logger.Level.TRACE, "StreamMessage TimeStamp pass");
			} else {
				logger.log(Logger.Level.INFO, "Error: invalid timestamp from StreamMessage");
				pass = false;
			}

			// TextMessage
			messageSent = tool.getDefaultQueueSession().createTextMessage();
			messageSent.setText("sending a TextMessage");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrTimeStampQTest");
			logger.log(Logger.Level.TRACE, "sending a TextMessage");

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			tool.getDefaultQueueSender().send(messageSent);

			// message has been sent
			timeAfterSend = System.currentTimeMillis();
			logger.log(Logger.Level.TRACE, " getJMSTimestamp");
			logger.log(Logger.Level.TRACE, " " + messageSent.getJMSTimestamp());
			logger.log(Logger.Level.TRACE, "Time at send is: " + timeBeforeSend);
			logger.log(Logger.Level.TRACE, "Time after return fromsend is:" + timeAfterSend);
			if (!(timeBeforeSend <= messageSent.getJMSTimestamp())
					&& (timeAfterSend >= messageSent.getJMSTimestamp())) {
				logger.log(Logger.Level.ERROR, "Error: invalid timestamp from TextMessage");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: invalid TimeStamp returned from JMSTimeStamp");
			}

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("msgHdrTimeStampQTest");
		}
	}

	/*
	 * @testName: msgHdrCorlIdQTest
	 * 
	 * @assertion_ids: JMS:SPEC:246.7; JMS:JAVADOC:355; JMS:JAVADOC:357;
	 * 
	 * @test_Strategy: Send a message to a Queue with CorrelationID set. Receive msg
	 * and verify the correlationid is as set by client
	 */
	@Test
	public void msgHdrCorlIdQTest() throws Exception {
		boolean pass = true;
		byte bValue = 127;
		String jmsCorrelationID = "testCorrelationid";
		String testName = "msgHdrCorlIdQTest";

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			StreamMessage messageSentS = null;
			StreamMessage messageReceivedS = null;
			BytesMessage messageSentB = null;
			BytesMessage messageReceivedB = null;
			MapMessage messageReceivedM = null;
			MapMessage messageSentM = null;
			ObjectMessage messageSentO = null;
			ObjectMessage messageReceivedO = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();

			messageSent = tool.getDefaultQueueSession().createTextMessage();
			messageSent.setText("sending a message");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			logger.log(Logger.Level.TRACE, "Send TextMessage to Queue.");
			messageSent.setJMSCorrelationID(jmsCorrelationID);
			try {
				tool.getDefaultQueueSender().send(messageSent);
				messageReceived = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);
				if (messageReceived == null) {
					logger.log(Logger.Level.ERROR, "messageReceived is null");
					pass = false;
				} else if (messageReceived.getJMSCorrelationID() == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "TextMessage Error: JMSCorrelationID returned a  null");
				} else if (!messageReceived.getJMSCorrelationID().equals(jmsCorrelationID)) {
					pass = false;
					logger.log(Logger.Level.ERROR, "TextMessage Error: JMSCorrelationID is incorrect");
				}
			} catch (java.lang.Exception e) {
				pass = false;
				logger.log(Logger.Level.ERROR, "Unexpected Exception: ", e);
			}

			// send and receive map message to Queue
			logger.log(Logger.Level.TRACE, "Send MapMessage to Queue.");
			messageSentM = tool.getDefaultQueueSession().createMapMessage();
			messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentM.setJMSCorrelationID(jmsCorrelationID);
			messageSentM.setString("aString", "value");
			tool.getDefaultQueueSender().send(messageSentM);

			messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedM.getJMSCorrelationID() == null) {
				pass = false;
				logger.log(Logger.Level.ERROR, "MapMessage Error: JMSCorrelationID returned a  null");
			} else if (!messageReceivedM.getJMSCorrelationID().equals(jmsCorrelationID)) {
				pass = false;
				logger.log(Logger.Level.ERROR, "MapMessage Error: JMSCorrelationID is incorrect");
			}

			// send and receive bytes message to Queue
			logger.log(Logger.Level.TRACE, "Send BytesMessage to Queue.");
			messageSentB = tool.getDefaultQueueSession().createBytesMessage();
			messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentB.setJMSCorrelationID(jmsCorrelationID);
			messageSentB.writeByte(bValue);
			tool.getDefaultQueueSender().send(messageSentB);

			messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedB.getJMSCorrelationID() == null) {
				pass = false;
				logger.log(Logger.Level.INFO, "BytesMessage Error: JMSCorrelationID returned a  null");
			} else if (!messageReceivedB.getJMSCorrelationID().equals(jmsCorrelationID)) {
				pass = false;
				logger.log(Logger.Level.INFO, "Byte Message Error: JMSCorrelationID is incorrect");
			}

			// Send and receive a StreamMessage
			logger.log(Logger.Level.TRACE, "sending a StreamMessage");
			messageSentS = tool.getDefaultQueueSession().createStreamMessage();
			messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentS.setJMSCorrelationID(jmsCorrelationID);
			messageSentS.writeString("Testing...");
			tool.getDefaultQueueSender().send(messageSentS);

			messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedS.getJMSCorrelationID() == null) {
				pass = false;
				logger.log(Logger.Level.ERROR, "StreamMessage Error: JMSCorrelationID returned a  null");
			} else if (!messageReceivedS.getJMSCorrelationID().equals(jmsCorrelationID)) {
				pass = false;
				logger.log(Logger.Level.ERROR, "StreamMessage Error: JMSCorrelationID is incorrect");
			}

			// send and receive Object message to Queue
			logger.log(Logger.Level.TRACE, "Send ObjectMessage to Queue.");
			messageSentO = tool.getDefaultQueueSession().createObjectMessage();
			messageSentO.setObject("msgHdrIDQTest for ObjectMessage");
			messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentO.setJMSCorrelationID(jmsCorrelationID);
			tool.getDefaultQueueSender().send(messageSentO);

			messageReceivedO = (ObjectMessage) (Message) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedO.getJMSCorrelationID() == null) {
				pass = false;
				logger.log(Logger.Level.INFO, "ObjectMessage Error: JMSCorrelationID returned a  null");
			} else if (!messageReceivedO.getJMSCorrelationID().equals(jmsCorrelationID)) {
				pass = false;
				logger.log(Logger.Level.INFO, "ObjectMessage Error: JMSCorrelationID is incorrect");
			}

			if (!pass) {
				throw new Exception("Error: invalid JMSCorrelationID returned from JMS Header");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception(testName);
		}
	}

	/*
	 * @testName: msgHdrReplyToQTest
	 * 
	 * @assertion_ids: JMS:SPEC:12; JMS:SPEC:246.8; JMS:JAVADOC:359;
	 * JMS:JAVADOC:361; JMS:JAVADOC:286; JMS:JAVADOC:289; JMS:JAVADOC:562;
	 * JMS:JAVADOC:166;
	 * 
	 * @test_Strategy: Send a message to a Queue with ReplyTo set to null and then
	 * set to a destination test with Text, map, object, byte, and stream messages
	 * verify on receive.
	 * 
	 */
	@Test
	public void msgHdrReplyToQTest() throws Exception {
		boolean pass = true;
		Queue replyQueue = null;
		byte bValue = 127;

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			StreamMessage messageSentS = null;
			StreamMessage messageReceivedS = null;
			BytesMessage messageSentB = null;
			BytesMessage messageReceivedB = null;
			MapMessage messageReceivedM = null;
			MapMessage messageSentM = null;
			ObjectMessage messageSentO = null;
			ObjectMessage messageReceivedO = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			messageSent = tool.getDefaultQueueSession().createTextMessage();
			messageSent.setText("sending a message");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrReplyToQTest");
			logger.log(Logger.Level.TRACE, "Send Text message");

			// messageSent.setJMSReplyTo(tool.getDefaultQueue());
			tool.getDefaultQueueSender().send(messageSent);
			messageReceived = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived.getJMSReplyTo() == null) {
				logger.log(Logger.Level.TRACE, " as expected replyto field is null");
			} else {
				logger.log(Logger.Level.INFO, "ERROR: expected replyto field should have been null for this case");
				pass = false;
			}
			logger.log(Logger.Level.TRACE, "Set ReplyTo and resend msg");
			messageSent.setJMSReplyTo(tool.getDefaultQueue());
			tool.getDefaultQueueSender().send(messageSent);
			messageReceived = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);
			replyQueue = (Queue) messageReceived.getJMSReplyTo();
			logger.log(Logger.Level.TRACE, "Queue name is " + replyQueue.getQueueName());
			if (replyQueue.getQueueName().equals(tool.getDefaultQueue().getQueueName())) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "TextMessage Failed");
				pass = false;
			}

			// send and receive Object message to Queue
			logger.log(Logger.Level.TRACE, "Send ObjectMessage to Queue.");
			messageSentO = tool.getDefaultQueueSession().createObjectMessage();
			messageSentO.setObject("msgHdrReplyToQTest for ObjectMessage");
			messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrReplyToQTest");
			tool.getDefaultQueueSender().send(messageSentO);
			messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedO.getJMSReplyTo() == null) {
				logger.log(Logger.Level.TRACE, " as expected replyto field is null");
			} else {
				replyQueue = (Queue) messageReceivedO.getJMSReplyTo();
				logger.log(Logger.Level.INFO, "ReplyTo is: " + replyQueue.toString());
				logger.log(Logger.Level.INFO, "ERROR: expected replyto field to be null in this case");
				pass = false;
			}
			logger.log(Logger.Level.TRACE, "Set ReplyTo and resend msg");
			messageSentO.setJMSReplyTo(tool.getDefaultQueue());
			tool.getDefaultQueueSender().send(messageSentO);
			messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver().receive(timeout);
			replyQueue = (Queue) messageReceivedO.getJMSReplyTo();
			logger.log(Logger.Level.TRACE, "Queue name is " + replyQueue.getQueueName());
			if (replyQueue.getQueueName().equals(tool.getDefaultQueue().getQueueName())) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "ObjectMessage ReplyTo Failed");
				pass = false;
			}

			// send and receive map message to Queue
			logger.log(Logger.Level.TRACE, "Send MapMessage to Queue.");
			messageSentM = tool.getDefaultQueueSession().createMapMessage();
			messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrReplyToQTest");
			messageSentM.setString("aString", "value");
			tool.getDefaultQueueSender().send(messageSentM);
			messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedM.getJMSReplyTo() == null) {
				logger.log(Logger.Level.TRACE, " as expected replyto field is null");
			} else {
				logger.log(Logger.Level.INFO, "ERROR: expected replyto field to be null in this case");
				pass = false;
			}
			logger.log(Logger.Level.TRACE, "Set ReplyTo and resend msg");
			messageSentM.setJMSReplyTo(tool.getDefaultQueue());
			tool.getDefaultQueueSender().send(messageSentM);
			messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "Received Map message ");
			replyQueue = (Queue) messageReceivedM.getJMSReplyTo();
			logger.log(Logger.Level.TRACE, "Queue name is " + replyQueue.getQueueName());
			if (replyQueue.getQueueName().equals(tool.getDefaultQueue().getQueueName())) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "MapMessage ReplyTo Failed");
				pass = false;
			}

			// send and receive bytes message to Queue
			logger.log(Logger.Level.TRACE, "Send BytesMessage to Queue.");
			messageSentB = tool.getDefaultQueueSession().createBytesMessage();
			messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrReplyToQTest");
			messageSentB.writeByte(bValue);
			tool.getDefaultQueueSender().send(messageSentB);
			messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedB.getJMSReplyTo() == null) {
				logger.log(Logger.Level.TRACE, " as expected replyto field is null");
			} else {
				logger.log(Logger.Level.INFO, "ERROR: expected replyto field to be null in this case");
				pass = false;
			}
			logger.log(Logger.Level.TRACE, "Set ReplyTo and resend msg");
			messageSentB.setJMSReplyTo(tool.getDefaultQueue());
			tool.getDefaultQueueSender().send(messageSentB);
			messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "Received Bytes message ");
			replyQueue = (Queue) messageReceivedB.getJMSReplyTo();
			logger.log(Logger.Level.TRACE, "Queue name is " + replyQueue.getQueueName());
			if (replyQueue.getQueueName().equals(tool.getDefaultQueue().getQueueName())) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "BytesMessage ReplyTo Failed");
				pass = false;
			}

			// Send and receive a StreamMessage
			messageSentS = tool.getDefaultQueueSession().createStreamMessage();
			messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrReplyToQTest");
			messageSentS.writeString("Testing...");
			logger.log(Logger.Level.TRACE, "Sending StreamMessage");
			tool.getDefaultQueueSender().send(messageSentS);
			messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedS.getJMSReplyTo() == null) {
				logger.log(Logger.Level.TRACE, " as expected replyto field is null");
			} else {
				replyQueue = (Queue) messageReceivedS.getJMSReplyTo();
				logger.log(Logger.Level.INFO, "ERROR: expected replyto field to be null in this case");
				pass = false;
			}
			logger.log(Logger.Level.TRACE, "Set ReplyTo and resend msg");
			messageSentS.setJMSReplyTo(tool.getDefaultQueue());
			tool.getDefaultQueueSender().send(messageSentS);
			messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "Received Stream message ");
			replyQueue = (Queue) messageReceivedS.getJMSReplyTo();
			logger.log(Logger.Level.TRACE, "Queue name is " + replyQueue.getQueueName());
			if (replyQueue.getQueueName().equals(tool.getDefaultQueue().getQueueName())) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "StreamMessage ReplyTo Failed");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: invalid Replyto returned from JMS Header");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("msgHdrReplyToQTest");
		}
	}

	/*
	 * @testName: msgHdrJMSTypeQTest
	 * 
	 * @assertion_ids: JMS:SPEC:246.9; JMS:JAVADOC:375; JMS:JAVADOC:377;
	 * 
	 * @test_Strategy: Send a message to a Queue with JMSType set to TESTMSG test
	 * with Text, map, object, byte, and stream messages verify on receive.
	 * 
	 */
	@Test
	public void msgHdrJMSTypeQTest() throws Exception {
		boolean pass = true;
		byte bValue = 127;
		String type = "TESTMSG";

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			StreamMessage messageSentS = null;
			StreamMessage messageReceivedS = null;
			BytesMessage messageSentB = null;
			BytesMessage messageReceivedB = null;
			MapMessage messageReceivedM = null;
			MapMessage messageSentM = null;
			ObjectMessage messageSentO = null;
			ObjectMessage messageReceivedO = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			messageSent = tool.getDefaultQueueSession().createTextMessage();
			messageSent.setText("sending a message");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSTypeQTest");
			logger.log(Logger.Level.TRACE, "JMSType test - Send a TextMessage");
			messageSent.setJMSType(type);
			tool.getDefaultQueueSender().send(messageSent);
			messageReceived = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSType is " + (String) messageReceived.getJMSType());
			if (messageReceived.getJMSType().equals(type)) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "TextMessage Failed");
				pass = false;
			}

			// send and receive Object message to Queue
			logger.log(Logger.Level.TRACE, "JMSType test - Send ObjectMessage to Queue.");
			messageSentO = tool.getDefaultQueueSession().createObjectMessage();
			messageSentO.setObject("msgHdrJMSTypeQTest for ObjectMessage");
			messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSTypeQTest");
			messageSentO.setJMSType(type);
			tool.getDefaultQueueSender().send(messageSentO);
			messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSType is " + (String) messageReceivedO.getJMSType());
			if (messageReceivedO.getJMSType().equals(type)) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "ObjectMessage JMSType Failed");
				pass = false;
			}

			// send and receive map message to Queue
			logger.log(Logger.Level.TRACE, "JMSType test - Send MapMessage to Queue.");
			messageSentM = tool.getDefaultQueueSession().createMapMessage();
			messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSTypeQTest");
			messageSentM.setString("aString", "value");
			messageSentM.setJMSType(type);
			tool.getDefaultQueueSender().send(messageSentM);
			messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSType is " + (String) messageReceivedM.getJMSType());
			if (messageReceivedM.getJMSType().equals(type)) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "MapMessage JMSType Failed");
				pass = false;
			}

			// send and receive bytes message to Queue
			logger.log(Logger.Level.TRACE, "JMSType test - Send BytesMessage to Queue.");
			messageSentB = tool.getDefaultQueueSession().createBytesMessage();
			messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSTypeQTest");
			messageSentB.writeByte(bValue);
			messageSentB.setJMSType(type);
			tool.getDefaultQueueSender().send(messageSentB);
			messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSType is " + (String) messageReceivedB.getJMSType());
			if (messageReceivedB.getJMSType().equals(type)) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "BytesMessage JMSType Failed");
				pass = false;
			}

			// Send and receive a StreamMessage
			logger.log(Logger.Level.TRACE, "JMSType test - sending a StreamMessage");
			messageSentS = tool.getDefaultQueueSession().createStreamMessage();
			messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSTypeQTest");
			messageSentS.writeString("Testing...");
			messageSentS.setJMSType(type);
			tool.getDefaultQueueSender().send(messageSentS);
			messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSType is " + (String) messageReceivedS.getJMSType());
			if (messageReceivedS.getJMSType().equals(type)) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "StreamMessage JMSType Failed");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: invalid JMSType returned from JMS Header");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("msgHdrJMSTypeQTest");
		}
	}

	/*
	 * @testName: msgHdrJMSPriorityQTest
	 * 
	 * @assertion_ids: JMS:SPEC:16; JMS:SPEC:18; JMS:SPEC:140; JMS:JAVADOC:305;
	 * JMS:JAVADOC:383;
	 * 
	 * @test_Strategy: Send a message to a Queue with JMSPriority set to 2 test with
	 * Text, map, object, byte, and stream messages verify on receive.
	 * 
	 */
	@Test
	public void msgHdrJMSPriorityQTest() throws Exception {
		boolean pass = true;
		byte bValue = 127;
		int priority = 2;

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			StreamMessage messageSentS = null;
			StreamMessage messageReceivedS = null;
			BytesMessage messageSentB = null;
			BytesMessage messageReceivedB = null;
			MapMessage messageReceivedM = null;
			MapMessage messageSentM = null;
			ObjectMessage messageSentO = null;
			ObjectMessage messageReceivedO = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			messageSent = tool.getDefaultQueueSession().createTextMessage();
			messageSent.setText("sending a message");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSPriorityQTest");
			logger.log(Logger.Level.TRACE, "JMSPriority test - Send a TextMessage");
			tool.getDefaultQueueSender().setPriority(priority);
			tool.getDefaultQueueSender().send(messageSent);
			messageReceived = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSPriority is " + messageReceived.getJMSPriority());
			if (messageReceived.getJMSPriority() == priority) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "TextMessage Failed");
				pass = false;
			}

			// send and receive Object message to Queue
			logger.log(Logger.Level.TRACE, "JMSPriority test - Send ObjectMessage to Queue.");
			messageSentO = tool.getDefaultQueueSession().createObjectMessage();
			messageSentO.setObject("msgHdrJMSPriorityQTest for ObjectMessage");
			messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSPriorityQTest");
			tool.getDefaultQueueSender().setPriority(priority);
			tool.getDefaultQueueSender().send(messageSentO);
			messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSPriority is " + messageReceivedO.getJMSPriority());
			if (messageReceivedO.getJMSPriority() == priority) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "ObjectMessage JMSPriority Failed");
				pass = false;
			}

			// send and receive map message to Queue
			logger.log(Logger.Level.TRACE, "JMSPriority test - Send MapMessage to Queue.");
			messageSentM = tool.getDefaultQueueSession().createMapMessage();
			messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSPriorityQTest");
			messageSentM.setString("aString", "value");
			tool.getDefaultQueueSender().setPriority(priority);
			tool.getDefaultQueueSender().send(messageSentM);
			messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSPriority is " + messageReceivedM.getJMSPriority());
			if (messageReceivedM.getJMSPriority() == priority) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "MapMessage JMSPriority Failed");
				pass = false;
			}

			// send and receive bytes message to Queue
			logger.log(Logger.Level.TRACE, "JMSPriority test - Send BytesMessage to Queue.");
			messageSentB = tool.getDefaultQueueSession().createBytesMessage();
			messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSPriorityQTest");
			messageSentB.writeByte(bValue);
			tool.getDefaultQueueSender().setPriority(priority);
			tool.getDefaultQueueSender().send(messageSentB);
			messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSPriority is " + messageReceivedB.getJMSPriority());
			if (messageReceivedB.getJMSPriority() == priority) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "BytesMessage JMSPriority Failed");
				pass = false;
			}

			// Send and receive a StreamMessage
			logger.log(Logger.Level.TRACE, "JMSPriority test - sending a StreamMessage");
			messageSentS = tool.getDefaultQueueSession().createStreamMessage();
			messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSPriorityQTest");
			messageSentS.writeString("Testing...");
			tool.getDefaultQueueSender().setPriority(priority);
			tool.getDefaultQueueSender().send(messageSentS);
			messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSPriority is " + messageReceivedS.getJMSPriority());
			if (messageReceivedS.getJMSPriority() == priority) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "StreamMessage JMSPriority Failed");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: invalid JMSPriority returned from JMS Header");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("msgHdrJMSPriorityQTest");
		}
	}

	/*
	 * @testName: msgHdrJMSExpirationQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:15.1; JMS:SPEC:15.2; JMS:SPEC:15.3; JMS:SPEC:140;
	 * JMS:JAVADOC:309; JMS:JAVADOC:379;
	 * 
	 * @test_Strategy: 1. Send a message to a Queue with time to live set to 0.
	 * Verify on receive that JMSExpiration gets set to 0. Test with Text, Map,
	 * Object, Bytes, and Stream messages. 2. Send a message to a Queue with time to
	 * live set to non-0; Verify on receive that JMSExpiration gets set correctly.
	 */
	@Test
	public void msgHdrJMSExpirationQueueTest() throws Exception {
		boolean pass = true;
		byte bValue = 127;
		long forever = 0L;
		long timeToLive = timeout;
		String testName = "msgHdrJMSExpirationQueueTest";
		long timeBeforeSend = 0L;
		long timeAfterSend = 0L;

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			StreamMessage messageSentS = null;
			StreamMessage messageReceivedS = null;
			BytesMessage messageSentB = null;
			BytesMessage messageReceivedB = null;
			MapMessage messageReceivedM = null;
			MapMessage messageSentM = null;
			ObjectMessage messageSentO = null;
			ObjectMessage messageReceivedO = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();

			logger.log(Logger.Level.TRACE, "JMSExpiration test - Send a TextMessage");
			messageSent = tool.getDefaultQueueSession().createTextMessage();
			messageSent.setText("sending a TextMessage");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			tool.getDefaultQueueSender().setTimeToLive(forever);
			tool.getDefaultQueueSender().send(messageSent);

			messageReceived = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived.getJMSExpiration() != forever) {
				logger.log(Logger.Level.ERROR, "TextMessage Failed");
				pass = false;
			}

			logger.log(Logger.Level.TRACE, "JMSExpiration test - Send a TextMessage");
			tool.getDefaultQueueSender().setTimeToLive(timeToLive);
			timeBeforeSend = System.currentTimeMillis();
			tool.getDefaultQueueSender().send(messageSent);
			timeAfterSend = System.currentTimeMillis();

			long exp = messageSent.getJMSExpiration();
			logger.log(Logger.Level.TRACE, "JMSExpiration is set to=" + exp);
			logger.log(Logger.Level.TRACE, "Time before send=" + timeBeforeSend);
			logger.log(Logger.Level.TRACE, "Time after send=" + timeAfterSend);
			logger.log(Logger.Level.TRACE, "Time to Live =" + timeToLive);

			messageReceived = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived.getJMSExpiration() != exp) {
				logger.log(Logger.Level.ERROR, "TextMessage Failed: JMSExpiration didnot set correctly = "
						+ messageReceived.getJMSExpiration());
				logger.log(Logger.Level.ERROR, "JMSExpiration was set to=" + exp);
				pass = false;
			}

			// send and receive Object message to Queue
			logger.log(Logger.Level.TRACE, "JMSExpiration test - Send ObjectMessage to Queue.");
			messageSentO = tool.getDefaultQueueSession().createObjectMessage();
			messageSentO.setObject("msgHdrJMSExpirationQueueTest for ObjectMessage");
			messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			tool.getDefaultQueueSender().setTimeToLive(forever);
			tool.getDefaultQueueSender().send(messageSentO);
			messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedO.getJMSExpiration() != forever) {
				logger.log(Logger.Level.INFO, "ObjectMessage JMSExpiration Failed");
				pass = false;
			}

			logger.log(Logger.Level.TRACE, "JMSExpiration test - Send an ObjectMessage");
			tool.getDefaultQueueSender().setTimeToLive(timeToLive);
			timeBeforeSend = System.currentTimeMillis();
			tool.getDefaultQueueSender().send(messageSentO);
			timeAfterSend = System.currentTimeMillis();

			exp = messageSentO.getJMSExpiration();
			logger.log(Logger.Level.TRACE, "JMSExpiration is set to=" + exp);
			logger.log(Logger.Level.TRACE, "Time before send=" + timeBeforeSend);
			logger.log(Logger.Level.TRACE, "Time after send=" + timeAfterSend);
			logger.log(Logger.Level.TRACE, "Time to Live =" + timeToLive);

			messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedO.getJMSExpiration() != exp) {
				logger.log(Logger.Level.ERROR, "ObjectMessage Failed: JMSExpiration didnot set correctly = "
						+ messageReceivedO.getJMSExpiration());
				logger.log(Logger.Level.ERROR, "JMSExpiration was set to=" + exp);
				pass = false;
			}

			// send and receive map message to Queue
			logger.log(Logger.Level.TRACE, "JMSExpiration test - Send MapMessage to Queue.");
			messageSentM = tool.getDefaultQueueSession().createMapMessage();
			messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentM.setString("aString", "value");
			tool.getDefaultQueueSender().setTimeToLive(forever);
			tool.getDefaultQueueSender().send(messageSentM);
			messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedM.getJMSExpiration() != forever) {
				logger.log(Logger.Level.INFO, "MapMessage JMSExpiration Failed");
				pass = false;
			}

			logger.log(Logger.Level.TRACE, "JMSExpiration test - Send a MapMessage");
			tool.getDefaultQueueSender().setTimeToLive(timeToLive);
			timeBeforeSend = System.currentTimeMillis();
			tool.getDefaultQueueSender().send(messageSentM);
			timeAfterSend = System.currentTimeMillis();

			exp = messageSentM.getJMSExpiration();
			logger.log(Logger.Level.TRACE, "JMSExpiration is set to=" + exp);
			logger.log(Logger.Level.TRACE, "Time before send=" + timeBeforeSend);
			logger.log(Logger.Level.TRACE, "Time after send=" + timeAfterSend);
			logger.log(Logger.Level.TRACE, "Time to Live =" + timeToLive);

			messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedM.getJMSExpiration() != exp) {
				logger.log(Logger.Level.ERROR, "MapMessage Failed: JMSExpiration didnot set correctly = "
						+ messageReceivedM.getJMSExpiration());
				logger.log(Logger.Level.ERROR, "JMSExpiration was set to=" + exp);
				pass = false;
			}

			// send and receive bytes message to Queue
			logger.log(Logger.Level.TRACE, "JMSExpiration test - Send BytesMessage to Queue.");
			messageSentB = tool.getDefaultQueueSession().createBytesMessage();
			messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentB.writeByte(bValue);
			tool.getDefaultQueueSender().setTimeToLive(forever);
			tool.getDefaultQueueSender().send(messageSentB);
			messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedB.getJMSExpiration() != forever) {
				logger.log(Logger.Level.INFO, "BytesMessage JMSExpiration Failed");
				pass = false;
			}

			logger.log(Logger.Level.TRACE, "JMSExpiration test - Send a BytesMessage");
			tool.getDefaultQueueSender().setTimeToLive(timeToLive);
			timeBeforeSend = System.currentTimeMillis();
			tool.getDefaultQueueSender().send(messageSentB);
			timeAfterSend = System.currentTimeMillis();

			exp = messageSentB.getJMSExpiration();
			logger.log(Logger.Level.TRACE, "JMSExpiration is set to=" + exp);
			logger.log(Logger.Level.TRACE, "Time before send=" + timeBeforeSend);
			logger.log(Logger.Level.TRACE, "Time after send=" + timeAfterSend);
			logger.log(Logger.Level.TRACE, "Time to Live =" + timeToLive);

			messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedB.getJMSExpiration() != exp) {
				logger.log(Logger.Level.ERROR, "BytesMessage Failed: JMSExpiration didnot set correctly = "
						+ messageReceivedB.getJMSExpiration());
				logger.log(Logger.Level.ERROR, "JMSExpiration was set to=" + exp);
				pass = false;
			}

			// Send and receive a StreamMessage
			logger.log(Logger.Level.TRACE, "JMSExpiration test - sending a StreamMessage");
			messageSentS = tool.getDefaultQueueSession().createStreamMessage();
			messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentS.writeString("Testing...");
			tool.getDefaultQueueSender().setTimeToLive(forever);
			tool.getDefaultQueueSender().send(messageSentS);
			messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedS.getJMSExpiration() != forever) {
				logger.log(Logger.Level.INFO, "StreamMessage JMSExpiration Failed");
				pass = false;
			}

			logger.log(Logger.Level.TRACE, "JMSExpiration test - Send a StreamMessage");
			tool.getDefaultQueueSender().setTimeToLive(timeToLive);
			timeBeforeSend = System.currentTimeMillis();
			tool.getDefaultQueueSender().send(messageSentS);
			timeAfterSend = System.currentTimeMillis();

			exp = messageSentS.getJMSExpiration();
			logger.log(Logger.Level.TRACE, "JMSExpiration is set to=" + exp);
			logger.log(Logger.Level.TRACE, "Time before send=" + timeBeforeSend);
			logger.log(Logger.Level.TRACE, "Time after send=" + timeAfterSend);
			logger.log(Logger.Level.TRACE, "Time to Live =" + timeToLive);

			messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedS.getJMSExpiration() != exp) {
				logger.log(Logger.Level.ERROR, "StreamMessage Failed: JMSExpiration didnot set correctly = "
						+ messageReceivedS.getJMSExpiration());
				logger.log(Logger.Level.ERROR, "JMSExpiration was set to=" + exp);
				pass = false;
			}

			if (!pass) {
				throw new Exception("Error: invalid JMSExpiration returned from JMS Header");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception(testName);
		}
	}

	/*
	 * @testName: msgHdrJMSDestinationQTest
	 * 
	 * @assertion_ids: JMS:SPEC:2; JMS:JAVADOC:363; JMS:JAVADOC:286;
	 * 
	 * @test_Strategy: Create and send a message to the default Queue. Receive msg
	 * and verify that JMSDestination is set to the default Queue test with Text,
	 * map, object, byte, and stream messages
	 */
	@Test
	public void msgHdrJMSDestinationQTest() throws Exception {
		boolean pass = true;
		byte bValue = 127;
		Queue replyDestination = null;

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			StreamMessage messageSentS = null;
			StreamMessage messageReceivedS = null;
			BytesMessage messageSentB = null;
			BytesMessage messageReceivedB = null;
			MapMessage messageReceivedM = null;
			MapMessage messageSentM = null;
			ObjectMessage messageSentO = null;
			ObjectMessage messageReceivedO = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			messageSent = tool.getDefaultQueueSession().createTextMessage();
			messageSent.setText("sending a message");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDestinationQTest");
			logger.log(Logger.Level.TRACE, "Send TextMessage to Queue.");
			tool.getDefaultQueueSender().send(messageSent);
			messageReceived = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSDestination:  " + messageReceived.getJMSDestination());
			replyDestination = (Queue) messageReceived.getJMSDestination();
			if (replyDestination == null) {
				pass = false;
				logger.log(Logger.Level.INFO, "TextMessage Error: JMSDestination returned a  null");
			} else {
				logger.log(Logger.Level.TRACE, "Queue name is " + replyDestination.getQueueName());
				if (replyDestination.getQueueName().equals(tool.getDefaultQueue().getQueueName())) {
					logger.log(Logger.Level.TRACE, "Pass ");
				} else {
					logger.log(Logger.Level.INFO, "TextMessage Failed");
					pass = false;
				}
			}

			// send and receive Object message to Queue
			logger.log(Logger.Level.TRACE, "Send ObjectMessage to Queue.");
			messageSentO = tool.getDefaultQueueSession().createObjectMessage();
			messageSentO.setObject("msgHdrJMSDestinationQTest for ObjectMessage");
			messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDestinationQTest");
			tool.getDefaultQueueSender().send(messageSentO);
			messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSDestination:  " + messageReceivedO.getJMSDestination());
			replyDestination = (Queue) messageReceived.getJMSDestination();
			if (replyDestination == null) {
				pass = false;
				logger.log(Logger.Level.INFO, "ObjectMessage Error: JMSDestination returned a  null");
			} else {
				logger.log(Logger.Level.TRACE, "Queue name is " + replyDestination.getQueueName());
				if (replyDestination.getQueueName().equals(tool.getDefaultQueue().getQueueName())) {
					logger.log(Logger.Level.TRACE, "Pass ");
				} else {
					logger.log(Logger.Level.INFO, "ObjectMessage Failed");
					pass = false;
				}
			}

			// send and receive map message to Queue
			logger.log(Logger.Level.TRACE, "Send MapMessage to Queue.");
			messageSentM = tool.getDefaultQueueSession().createMapMessage();
			messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDestinationQTest");
			messageSentM.setString("aString", "value");
			tool.getDefaultQueueSender().send(messageSentM);
			messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSDestination:  " + messageReceivedM.getJMSDestination());
			replyDestination = (Queue) messageReceived.getJMSDestination();
			if (replyDestination == null) {
				pass = false;
				logger.log(Logger.Level.INFO, "MapMessage Error: JMSDestination returned a  null");
			} else {
				logger.log(Logger.Level.TRACE, "Queue name is " + replyDestination.getQueueName());
				if (replyDestination.getQueueName().equals(tool.getDefaultQueue().getQueueName())) {
					logger.log(Logger.Level.TRACE, "Pass ");
				} else {
					logger.log(Logger.Level.INFO, "MapMessage Failed");
					pass = false;
				}
			}

			// send and receive bytes message to Queue
			logger.log(Logger.Level.TRACE, "Send BytesMessage to Queue.");
			messageSentB = tool.getDefaultQueueSession().createBytesMessage();
			messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDestinationQTest");
			messageSentB.writeByte(bValue);
			tool.getDefaultQueueSender().send(messageSentB);
			messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSDestination:  " + messageReceivedB.getJMSDestination());
			replyDestination = (Queue) messageReceived.getJMSDestination();
			if (replyDestination == null) {
				pass = false;
				logger.log(Logger.Level.INFO, "BytesMessage Error: JMSDestination returned a  null");
			} else {
				logger.log(Logger.Level.TRACE, "Queue name is " + replyDestination.getQueueName());
				if (replyDestination.getQueueName().equals(tool.getDefaultQueue().getQueueName())) {
					logger.log(Logger.Level.TRACE, "Pass ");
				} else {
					logger.log(Logger.Level.INFO, "BytesMessage Failed");
					pass = false;
				}
			}

			// Send and receive a StreamMessage
			logger.log(Logger.Level.TRACE, "sending a StreamMessage");
			messageSentS = tool.getDefaultQueueSession().createStreamMessage();
			messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDestinationQTest");
			messageSentS.writeString("Testing...");
			logger.log(Logger.Level.TRACE, "Sending StreamMessage");
			tool.getDefaultQueueSender().send(messageSentS);
			messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSDestination:  " + messageReceivedS.getJMSDestination());
			replyDestination = (Queue) messageReceived.getJMSDestination();
			if (replyDestination == null) {
				pass = false;
				logger.log(Logger.Level.INFO, "StreamMessage Error: JMSDestination returned a  null");
			} else {
				logger.log(Logger.Level.TRACE, "Queue name is " + replyDestination.getQueueName());
				if (replyDestination.getQueueName().equals(tool.getDefaultQueue().getQueueName())) {
					logger.log(Logger.Level.TRACE, "Pass ");
				} else {
					logger.log(Logger.Level.INFO, "StreamMessage Failed");
					pass = false;
				}
			}
			if (!pass) {
				throw new Exception("Error: invalid JMSDestination returned from JMS Header");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("msgHdrJMSDestinationQTest");
		}
	}

	/*
	 * @testName: msgHdrJMSDeliveryModeQTest
	 * 
	 * @assertion_ids: JMS:SPEC:3; JMS:SPEC:140; JMS:SPEC:246.2; JMS:JAVADOC:301;
	 * JMS:JAVADOC:367; JMS:JAVADOC:369;
	 * 
	 * @test_Strategy: 1. Create and send a message to the default Queue. Receive
	 * the msg and verify that JMSDeliveryMode is set the default delivery mode of
	 * persistent. 2. Create and test another message with a nonpersistent delivery
	 * mode. Test with Text, map, object, byte, and stream messages 3. Set
	 * JMSDeliveryMode to Message after receive. Verify that JMSDeliveryMode is set
	 * correctly.
	 */
	@Test
	public void msgHdrJMSDeliveryModeQTest() throws Exception {
		boolean pass = true;
		byte bValue = 127;
		String testName = "msgHdrJMSDeliveryModeQTest";

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			StreamMessage messageSentS = null;
			StreamMessage messageReceivedS = null;
			BytesMessage messageSentB = null;
			BytesMessage messageReceivedB = null;
			MapMessage messageReceivedM = null;
			MapMessage messageSentM = null;
			ObjectMessage messageSentO = null;
			ObjectMessage messageReceivedO = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();

			logger.log(Logger.Level.TRACE, "send TextMessage to Queue.");
			messageSent = tool.getDefaultQueueSession().createTextMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			tool.getDefaultQueueSender().send(messageSent);

			messageReceived = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR,
						"TextMessage Error: JMSDeliveryMode should be set to persistent as default");
			}

			messageReceived.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
			if (messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR,
						"TextMessage Error: JMSDeliveryMode not set correctly by setJMSDeliveryMode");
			}

			// send and receive Object message to Queue
			logger.log(Logger.Level.TRACE, "send ObjectMessage to Queue.");
			messageSentO = tool.getDefaultQueueSession().createObjectMessage();
			messageSentO.setObject("Test for ObjectMessage");
			messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			tool.getDefaultQueueSender().send(messageSentO);

			messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedO.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.INFO,
						"ObjectMessage Error: JMSDeliveryMode should be set to persistent as default");
			}

			messageReceivedO.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
			if (messageReceivedO.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR,
						"ObjectMessage Error: JMSDeliveryMode not set correctly by setJMSDeliveryMode");
			}

			// send and receive map message to Queue
			logger.log(Logger.Level.TRACE, "send MapMessage to Queue.");
			messageSentM = tool.getDefaultQueueSession().createMapMessage();
			messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentM.setString("aString", "value");
			tool.getDefaultQueueSender().send(messageSentM);

			messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedM.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.INFO,
						"MapMessage Error: JMSDeliveryMode should be set to persistent as default");
			}

			messageReceivedM.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
			if (messageReceivedM.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR,
						"MapMessage Error: JMSDeliveryMode not set correctly by setJMSDeliveryMode");
			}

			// send and receive bytes message to Queue
			logger.log(Logger.Level.TRACE, "send BytesMessage to Queue.");
			messageSentB = tool.getDefaultQueueSession().createBytesMessage();
			messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentB.writeByte(bValue);
			tool.getDefaultQueueSender().send(messageSentB);

			messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedB.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR,
						"BytesMessage Error: JMSDeliveryMode should be set to persistent as default");
			}

			messageReceivedB.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
			if (messageReceivedB.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR,
						"BytesMessage Error: JMSDeliveryMode not set correctly by setJMSDeliveryMode");
			}

			// send and receive a StreamMessage
			logger.log(Logger.Level.TRACE, "sending a StreamMessage");
			messageSentS = tool.getDefaultQueueSession().createStreamMessage();
			messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentS.writeString("Testing...");
			tool.getDefaultQueueSender().send(messageSentS);

			messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedS.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR,
						"StreamMessage Error: JMSDeliveryMode should be set to persistent as default");
			}

			messageReceivedS.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
			if (messageReceivedS.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR,
						"StreamMessage Error: JMSDeliveryMode not set correctly by setJMSDeliveryMode");
			}

			// Test again - this time set delivery mode to persistent
			logger.log(Logger.Level.TRACE, "send TextMessage to Queue.");
			tool.getDefaultQueueSender().setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			messageSent = tool.getDefaultQueueSession().createTextMessage();
			messageSent.setText("sending a message");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			tool.getDefaultQueueSender().send(messageSent);

			messageReceived = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR, "TextMessage Error: JMSDeliveryMode should be set to NON_PERSISTENT");
			}

			messageReceived.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
			if (messageReceived.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR,
						"TextMessage Error: JMSDeliveryMode not set correctly by setJMSDeliveryMode");
			}

			// send and receive Object message to Queue
			logger.log(Logger.Level.TRACE, "send ObjectMessage to Queue.");
			messageSentO = tool.getDefaultQueueSession().createObjectMessage();
			messageSentO.setObject("msgHdrJMSDeliveryModeQTest for ObjectMessage");
			messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			tool.getDefaultQueueSender().send(messageSentO);

			messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedO.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR, "ObjectMessage Error: JMSDeliveryMode should be set to NON_PERSISTENT");
			}

			messageReceivedO.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
			if (messageReceivedO.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR,
						"ObjectMessage Error: JMSDeliveryMode not set correctly by setJMSDeliveryMode");
			}

			// send and receive map message to Queue
			logger.log(Logger.Level.TRACE, "send MapMessage to Queue.");
			messageSentM = tool.getDefaultQueueSession().createMapMessage();
			messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentM.setString("aString", "value");
			tool.getDefaultQueueSender().send(messageSentM);

			messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedM.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR, "MapMessage Error: JMSDeliveryMode should be set to NON_PERSISTENT");
			}

			messageReceivedM.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
			if (messageReceivedM.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR,
						"MapMessage Error: JMSDeliveryMode not set correctly by setJMSDeliveryMode");
			}

			// send and receive bytes message to Queue
			logger.log(Logger.Level.TRACE, "send BytesMessage to Queue.");
			messageSentB = tool.getDefaultQueueSession().createBytesMessage();
			messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentB.writeByte(bValue);
			tool.getDefaultQueueSender().send(messageSentB);

			messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedB.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR, "BytesMessage Error: JMSDeliveryMode should be set to NON_PERSISTENT");
			}

			messageReceivedB.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
			if (messageReceivedB.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR,
						"BytesMessage Error: JMSDeliveryMode not set correctly by setJMSDeliveryMode");
			}

			// send and receive a StreamMessage
			logger.log(Logger.Level.TRACE, "sending a StreamMessage");
			messageSentS = tool.getDefaultQueueSession().createStreamMessage();
			messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentS.writeString("Testing...");
			tool.getDefaultQueueSender().send(messageSentS);

			messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedS.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR, "StreamMessage Error: JMSDeliveryMode should be set to NON_PERSISTENT");
			}

			messageReceivedS.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
			if (messageReceivedS.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR,
						"StreamMessage Error: JMSDeliveryMode not set correctly by setJMSDeliveryMode");
			}

			if (!pass) {
				throw new Exception("Error: invalid JMSDeliveryMode returned from JMS Header");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception(testName);
		}
	}

	/*
	 * @testName: msgHdrJMSRedeliveredTest
	 * 
	 * @assertion_ids: JMS:JAVADOC:371; JMS:JAVADOC:373;
	 * 
	 * @test_Strategy: 1. Create and send a message to the default Queue. Verify at
	 * the receive that JMSRedelivered is false; 3. Set JMSRedelivered to true after
	 * receive. Verify that JMSRedelivered is set correctly. Test with Text, Map,
	 * Object, Bytes, and Stream messages
	 */
	@Test
	public void msgHdrJMSRedeliveredTest() throws Exception {
		boolean pass = true;
		byte bValue = 127;
		String testName = "msgHdrJMSRedeliveredTest";

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			StreamMessage messageSentS = null;
			StreamMessage messageReceivedS = null;
			BytesMessage messageSentB = null;
			BytesMessage messageReceivedB = null;
			MapMessage messageReceivedM = null;
			MapMessage messageSentM = null;
			ObjectMessage messageSentO = null;
			ObjectMessage messageReceivedO = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();

			logger.log(Logger.Level.TRACE, "send TextMessage to Queue.");
			messageSent = tool.getDefaultQueueSession().createTextMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			tool.getDefaultQueueSender().send(messageSent);

			messageReceived = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived.getJMSRedelivered() != false) {
				pass = false;
				logger.log(Logger.Level.ERROR, "TextMessage Error: JMSRedelivered should be false");
			}

			messageReceived.setJMSRedelivered(true);
			if (messageReceived.getJMSRedelivered() != true) {
				pass = false;
				logger.log(Logger.Level.ERROR,
						"TextMessage Error: JMSRedelivered not set correctly by setJMSRedelivered");
			}

			// send and receive Object message to Queue
			logger.log(Logger.Level.TRACE, "send ObjectMessage to Queue.");
			messageSentO = tool.getDefaultQueueSession().createObjectMessage();
			messageSentO.setObject("Test for ObjectMessage");
			messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			tool.getDefaultQueueSender().send(messageSentO);

			messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedO.getJMSRedelivered() != false) {
				pass = false;
				logger.log(Logger.Level.ERROR, "ObjectMessage Error: JMSRedelivered should be false");
			}

			messageReceivedO.setJMSRedelivered(true);
			if (messageReceivedO.getJMSRedelivered() != true) {
				pass = false;
				logger.log(Logger.Level.ERROR,
						"ObjectMessage Error: JMSRedelivered not set correctly by setJMSRedelivered");
			}

			// send and receive map message to Queue
			logger.log(Logger.Level.TRACE, "send MapMessage to Queue.");
			messageSentM = tool.getDefaultQueueSession().createMapMessage();
			messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentM.setString("aString", "value");
			tool.getDefaultQueueSender().send(messageSentM);

			messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedM.getJMSRedelivered() != false) {
				pass = false;
				logger.log(Logger.Level.ERROR, "MapMessage Error: JMSRedelivered should be false");
			}

			messageReceivedM.setJMSRedelivered(true);
			if (messageReceivedM.getJMSRedelivered() != true) {
				pass = false;
				logger.log(Logger.Level.ERROR,
						"MapMessage Error: JMSRedelivered not set correctly by setJMSRedelivered");
			}

			// send and receive bytes message to Queue
			logger.log(Logger.Level.TRACE, "send BytesMessage to Queue.");
			messageSentB = tool.getDefaultQueueSession().createBytesMessage();
			messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentB.writeByte(bValue);
			tool.getDefaultQueueSender().send(messageSentB);

			messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedB.getJMSRedelivered() != false) {
				pass = false;
				logger.log(Logger.Level.ERROR, "BytesMessage Error: JMSRedelivered should be false");
			}

			messageReceivedB.setJMSRedelivered(true);
			if (messageReceivedB.getJMSRedelivered() != true) {
				pass = false;
				logger.log(Logger.Level.ERROR,
						"BytesMessage Error: JMSRedelivered not set correctly by setJMSRedelivered");
			}

			// send and receive a StreamMessage
			logger.log(Logger.Level.TRACE, "sending a StreamMessage");
			messageSentS = tool.getDefaultQueueSession().createStreamMessage();
			messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentS.writeString("Testing...");
			tool.getDefaultQueueSender().send(messageSentS);

			messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceivedS.getJMSRedelivered() != false) {
				pass = false;
				logger.log(Logger.Level.ERROR, "StreamMessage Error: JMSRedelivered should be false");
			}

			messageReceivedS.setJMSRedelivered(true);
			if (messageReceivedS.getJMSRedelivered() != true) {
				pass = false;
				logger.log(Logger.Level.ERROR,
						"StreamMessage Error: JMSRedelivered not set correctly by setJMSRedelivered");
			}

			if (!pass) {
				throw new Exception("Error: invalid JMSDeliveryMode returned from JMS Header");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception(testName);
		}
	}
}
