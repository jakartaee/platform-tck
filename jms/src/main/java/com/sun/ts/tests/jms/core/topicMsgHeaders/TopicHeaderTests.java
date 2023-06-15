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
package com.sun.ts.tests.jms.core.topicMsgHeaders;

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
import jakarta.jms.StreamMessage;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;

public class TopicHeaderTests {
	private static final String testName = "com.sun.ts.tests.jms.core.topicMsgHeaders.TopicHeaderTests";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(TopicHeaderTests.class.getName());

	// JMS objects
	private transient JmsTool tool = null;

	// Harness req's
	private Properties props = null;

	// properties read
	long timeout;

	String user;

	String password;

	String mode;

	ArrayList connections = null;

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
	 * Creates Administrator object and deletes all previous Destinations.
	 * Individual tests create the TestTools object with one default Queue and/or
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
				throw new Exception("'user' in must be null");
			}
			if (password == null) {
				throw new Exception("'password' in must be null");
			}
			if (mode == null) {
				throw new Exception("'mode' in must be null");
			}

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
				tool.closeAllConnections(connections);
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
	 * @testName: msgHdrIDTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:4; JMS:JAVADOC:343;
	 * 
	 * @test_Strategy: With a topic destination Send and receive single Text, map,
	 * bytes, stream, and object message call getJMSMessageID and verify that it
	 * starts with ID:
	 */
	@Test
	public void msgHdrIDTopicTest() throws Exception {
		boolean pass = true;
		byte bValue = 127;
		String id = null;

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			StreamMessage messageSentStreamMessage = null;
			StreamMessage messageReceivedStreamMessage = null;
			BytesMessage messageSentBytesMessage = null;
			BytesMessage messageReceivedBytesMessage = null;
			MapMessage messageReceivedMapMessage = null;
			MapMessage messageSentMapMessage = null;
			ObjectMessage messageSentObjectMsg = null;
			ObjectMessage messageReceivedObjectMsg = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
			tool.getDefaultTopicConnection().start();

			// send and receive Object message to Topic
			logger.log(Logger.Level.TRACE, "Send ObjectMessage to Topic.");
			messageSentObjectMsg = tool.getDefaultTopicSession().createObjectMessage();
			messageSentObjectMsg.setObject("msgHdrIDTopicTest for Object Message");
			messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrIDToopicTest");
			tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
			messageReceivedObjectMsg = (ObjectMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "getJMSMessageID ");
			logger.log(Logger.Level.TRACE, " " + messageReceivedObjectMsg.getJMSMessageID());
			id = messageReceivedObjectMsg.getJMSMessageID();
			if (!chkMessageID(id)) {
				logger.log(Logger.Level.INFO, "ObjectMessage error: JMSMessageID does not start with ID:");
				pass = false;
			}

			// send and receive map message to Topic
			logger.log(Logger.Level.TRACE, "Send MapMessage to Topic.");
			messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
			messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrIDTopicTest");
			messageSentMapMessage.setString("aString", "value");
			tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
			messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "getJMSMessageID ");
			logger.log(Logger.Level.TRACE, " " + messageReceivedMapMessage.getJMSMessageID());
			id = messageReceivedMapMessage.getJMSMessageID();
			if (!chkMessageID(id)) {
				logger.log(Logger.Level.INFO, "MapMessage error: JMSMessageID does not start with ID:");
				pass = false;
			}

			// send and receive bytes message to Topic
			logger.log(Logger.Level.TRACE, "Send BytesMessage to Topic.");
			messageSentBytesMessage = tool.getDefaultTopicSession().createBytesMessage();
			messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrIDTopicTest");
			messageSentBytesMessage.writeByte(bValue);
			tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
			messageReceivedBytesMessage = (BytesMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "getJMSMessageID ");
			logger.log(Logger.Level.TRACE, " " + messageReceivedBytesMessage.getJMSMessageID());
			id = messageReceivedBytesMessage.getJMSMessageID();
			if (!chkMessageID(id)) {
				logger.log(Logger.Level.INFO, "BytesMessage error: JMSMessageID does not start with ID:");
				pass = false;
			}

			// Send and receive a StreamMessage
			logger.log(Logger.Level.TRACE, "sending a Stream message");
			messageSentStreamMessage = tool.getDefaultTopicSession().createStreamMessage();
			messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrIDTopicTest");
			messageSentStreamMessage.writeString("Testing...");
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
			messageReceivedStreamMessage = (StreamMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "getJMSMessageID ");
			logger.log(Logger.Level.TRACE, " " + messageReceivedStreamMessage.getJMSMessageID());
			id = messageReceivedStreamMessage.getJMSMessageID();
			if (!chkMessageID(id)) {
				logger.log(Logger.Level.INFO, "StreamMessage error: JMSMessageID does not start with ID:");
				pass = false;
			}

			// Text Message
			messageSent = tool.getDefaultTopicSession().createTextMessage();
			messageSent.setText("sending a Text message");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrIDTopicTest");
			logger.log(Logger.Level.TRACE, "sending a Text message");
			tool.getDefaultTopicPublisher().publish(messageSent);
			messageReceived = (TextMessage) tool.getDefaultTopicSubscriber().receive(timeout);
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
			throw new Exception("msgHdrIDTopicTest");
		}
	}

	/*
	 * @testName: msgHdrTimeStampTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:7; JMS:JAVADOC:347;
	 * 
	 * @test_Strategy: With a Topic destination Send a single Text, map, bytes,
	 * stream, and object message check time of send against time send returns
	 * JMSTimeStamp should be between these two
	 */
	@Test
	public void msgHdrTimeStampTopicTest() throws Exception {
		boolean pass = true;
		long timeBeforeSend;
		long timeAfterSend;
		byte bValue = 127;
		String id = null;

		try {
			TextMessage messageSent = null;
			StreamMessage messageSentStreamMessage = null;
			BytesMessage messageSentBytesMessage = null;
			MapMessage messageSentMapMessage = null;
			ObjectMessage messageSentObjectMsg = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
			tool.getDefaultTopicConnection().start();

			// send an Object message to Topic
			logger.log(Logger.Level.TRACE, "Send ObjectMessage to TOPIC.");
			messageSentObjectMsg = tool.getDefaultTopicSession().createObjectMessage();
			messageSentObjectMsg.setObject("msgHdrTimeStampTopicTest for Object Message");
			messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrTimeStampTopicTest");

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);

			// message has been sent
			timeAfterSend = System.currentTimeMillis();
			logger.log(Logger.Level.TRACE, " getJMSTimestamp");
			logger.log(Logger.Level.TRACE, " " + messageSentObjectMsg.getJMSTimestamp());
			logger.log(Logger.Level.TRACE, "Time at send is: " + timeBeforeSend);
			logger.log(Logger.Level.TRACE, "Time after return fromsend is:" + timeAfterSend);
			if ((timeBeforeSend <= messageSentObjectMsg.getJMSTimestamp())
					&& (timeAfterSend >= messageSentObjectMsg.getJMSTimestamp())) {
				logger.log(Logger.Level.TRACE, "Object Message TimeStamp pass");
			} else {
				logger.log(Logger.Level.INFO, "Error: invalid timestamp from ObjectMessage");
				pass = false;
			}

			// send map message to Topic
			logger.log(Logger.Level.TRACE, "Send MapMessage to Topic.");
			messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
			messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrTimeStampTopicTest");
			messageSentMapMessage.setString("aString", "value");

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			tool.getDefaultTopicPublisher().publish(messageSentMapMessage);

			// message has been sent
			timeAfterSend = System.currentTimeMillis();
			logger.log(Logger.Level.TRACE, " getJMSTimestamp");
			logger.log(Logger.Level.TRACE, " " + messageSentMapMessage.getJMSTimestamp());
			logger.log(Logger.Level.TRACE, "Time at send is: " + timeBeforeSend);
			logger.log(Logger.Level.TRACE, "Time after return fromsend is:" + timeAfterSend);
			if ((timeBeforeSend <= messageSentMapMessage.getJMSTimestamp())
					&& (timeAfterSend >= messageSentMapMessage.getJMSTimestamp())) {
				logger.log(Logger.Level.TRACE, "MapMessage TimeStamp pass");
			} else {
				logger.log(Logger.Level.INFO, "Error: invalid timestamp from MapMessage");
				pass = false;
			}

			// send and receive bytes message to Topic
			logger.log(Logger.Level.TRACE, "Send BytesMessage to Topic.");
			messageSentBytesMessage = tool.getDefaultTopicSession().createBytesMessage();
			messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrTimeStampTopicTest");
			messageSentBytesMessage.writeByte(bValue);

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);

			// message has been sent
			timeAfterSend = System.currentTimeMillis();
			logger.log(Logger.Level.TRACE, " getJMSTimestamp");
			logger.log(Logger.Level.TRACE, " " + messageSentBytesMessage.getJMSTimestamp());
			logger.log(Logger.Level.TRACE, "Time at send is: " + timeBeforeSend);
			logger.log(Logger.Level.TRACE, "Time after return fromsend is:" + timeAfterSend);
			if ((timeBeforeSend <= messageSentBytesMessage.getJMSTimestamp())
					&& (timeAfterSend >= messageSentBytesMessage.getJMSTimestamp())) {
				logger.log(Logger.Level.TRACE, "BytesMessage TimeStamp pass");
			} else {
				logger.log(Logger.Level.INFO, "Error: invalid timestamp from BytesMessage");
				pass = false;
			}

			// Send and receive a StreamMessage
			logger.log(Logger.Level.TRACE, "sending a Stream message");
			messageSentStreamMessage = tool.getDefaultTopicSession().createStreamMessage();
			messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrTimeStampTopicTest");
			messageSentStreamMessage.writeString("Testing...");
			logger.log(Logger.Level.TRACE, "Sending message");

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);

			// message has been sent
			timeAfterSend = System.currentTimeMillis();
			logger.log(Logger.Level.TRACE, " getJMSTimestamp");
			logger.log(Logger.Level.TRACE, " " + messageSentStreamMessage.getJMSTimestamp());
			logger.log(Logger.Level.TRACE, "Time at send is: " + timeBeforeSend);
			logger.log(Logger.Level.TRACE, "Time after return fromsend is:" + timeAfterSend);
			if ((timeBeforeSend <= messageSentStreamMessage.getJMSTimestamp())
					&& (timeAfterSend >= messageSentStreamMessage.getJMSTimestamp())) {
				logger.log(Logger.Level.TRACE, "StreamMessage TimeStamp pass");
			} else {
				logger.log(Logger.Level.INFO, "Error: invalid timestamp from StreamMessage");
				pass = false;
			}

			// Text Message
			messageSent = tool.getDefaultTopicSession().createTextMessage();
			messageSent.setText("sending a Text message");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrTimeStampTopicTest");
			logger.log(Logger.Level.TRACE, "sending a Text message");

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			tool.getDefaultTopicPublisher().publish(messageSent);

			// message has been sent
			timeAfterSend = System.currentTimeMillis();
			logger.log(Logger.Level.TRACE, " getJMSTimestamp");
			logger.log(Logger.Level.TRACE, " " + messageSent.getJMSTimestamp());
			logger.log(Logger.Level.TRACE, "Time at send is: " + timeBeforeSend);
			logger.log(Logger.Level.TRACE, "Time after return fromsend is:" + timeAfterSend);
			if ((timeBeforeSend <= messageSent.getJMSTimestamp()) && (timeAfterSend >= messageSent.getJMSTimestamp())) {
				logger.log(Logger.Level.TRACE, "TextMessage TimeStamp pass");
			} else {
				logger.log(Logger.Level.INFO, "Error: invalid timestamp from TextMessage");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: invalid TimeStamp returned from JMSTimeStamp");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("msgHdrTimeStampTopicTest");
		}
	}

	/*
	 * @testName: msgHdrCorlIdTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:246.7; JMS:JAVADOC:355; JMS:JAVADOC:357;
	 * 
	 * @test_Strategy: Send a message to a Topic with CorrelationID set. Receive msg
	 * and verify the correlationid is as set by client
	 * 
	 */
	@Test
	public void msgHdrCorlIdTopicTest() throws Exception {
		boolean pass = true;
		byte bValue = 127;
		String jmsCorrelationID = "test Correlation id";

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			StreamMessage messageSentStreamMessage = null;
			StreamMessage messageReceivedStreamMessage = null;
			BytesMessage messageSentBytesMessage = null;
			BytesMessage messageReceivedBytesMessage = null;
			MapMessage messageReceivedMapMessage = null;
			MapMessage messageSentMapMessage = null;
			ObjectMessage messageSentObjectMsg = null;
			ObjectMessage messageReceivedObjectMsg = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
			tool.getDefaultTopicConnection().start();
			messageSent = tool.getDefaultTopicSession().createTextMessage();
			messageSent.setText("sending a message");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrCorlIdTopicTest");
			logger.log(Logger.Level.TRACE, "Sending Text message to Topic ");
			messageSent.setJMSCorrelationID(jmsCorrelationID);
			tool.getDefaultTopicPublisher().publish(messageSent);
			messageReceived = (TextMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "jmsCorrelationID:  " + messageReceived.getJMSCorrelationID());
			if (messageReceived.getJMSCorrelationID() == null) {
				pass = false;
				logger.log(Logger.Level.INFO, "Text Message Error: JMSCorrelationID returned a  null");
			} else if (messageReceived.getJMSCorrelationID().equals(jmsCorrelationID)) {
				logger.log(Logger.Level.TRACE, "pass");
			} else {
				pass = false;
				logger.log(Logger.Level.INFO, "Text Message Error: JMSCorrelationID is incorrect");
			}

			// send and receive Object message to Topic
			logger.log(Logger.Level.TRACE, "Send ObjectMessage to Topic.");
			messageSentObjectMsg = tool.getDefaultTopicSession().createObjectMessage();
			messageSentObjectMsg.setObject("msgHdrCorlIdTopicTest for Object Message");
			messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrCorlIdTopicTest");
			messageSentObjectMsg.setJMSCorrelationID(jmsCorrelationID);
			tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
			messageReceivedObjectMsg = (ObjectMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "jmsCorrelationID:  " + messageReceivedObjectMsg.getJMSCorrelationID());
			if (messageReceivedObjectMsg.getJMSCorrelationID() == null) {
				pass = false;
				logger.log(Logger.Level.INFO, "Object Message Error: JMSCorrelationID returned a  null");
			} else if (messageReceivedObjectMsg.getJMSCorrelationID().equals(jmsCorrelationID)) {
				logger.log(Logger.Level.TRACE, "pass");
			} else {
				pass = false;
				logger.log(Logger.Level.INFO, "Object Message Error: JMSCorrelationID is incorrect");
			}

			// send and receive map message to Topic
			logger.log(Logger.Level.TRACE, "Send MapMessage to Topic.");
			messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
			messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrCorlIdTopicTest");
			messageSentMapMessage.setJMSCorrelationID(jmsCorrelationID);
			messageSentMapMessage.setString("aString", "value");
			tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
			messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "jmsCorrelationID:  " + messageReceivedMapMessage.getJMSCorrelationID());
			if (messageReceivedMapMessage.getJMSCorrelationID() == null) {
				pass = false;
				logger.log(Logger.Level.INFO, "Map Message Error: JMSCorrelationID returned a  null");
			} else if (messageReceivedMapMessage.getJMSCorrelationID().equals(jmsCorrelationID)) {
				logger.log(Logger.Level.TRACE, "pass");
			} else {
				pass = false;
				logger.log(Logger.Level.INFO, "Map Message Error: JMSCorrelationID is incorrect");
			}

			// send and receive bytes message to Topic
			logger.log(Logger.Level.TRACE, "Send BytesMessage to Topic.");
			messageSentBytesMessage = tool.getDefaultTopicSession().createBytesMessage();
			messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrCorlIdTopicTest");
			messageSentBytesMessage.setJMSCorrelationID(jmsCorrelationID);
			messageSentBytesMessage.writeByte(bValue);
			tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
			messageReceivedBytesMessage = (BytesMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "jmsCorrelationID:  " + messageReceivedBytesMessage.getJMSCorrelationID());
			if (messageReceivedBytesMessage.getJMSCorrelationID() == null) {
				pass = false;
				logger.log(Logger.Level.INFO, "Bytes Message Error: JMSCorrelationID returned a  null");
			} else if (messageReceivedBytesMessage.getJMSCorrelationID().equals(jmsCorrelationID)) {
				logger.log(Logger.Level.TRACE, "pass");
			} else {
				pass = false;
				logger.log(Logger.Level.INFO, "Byte Message Error: JMSCorrelationID is incorrect");
			}

			// Send and receive a StreamMessage
			logger.log(Logger.Level.TRACE, "sending a Stream message");
			messageSentStreamMessage = tool.getDefaultTopicSession().createStreamMessage();
			messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrCorlIdTopicTest");
			messageSentStreamMessage.setJMSCorrelationID(jmsCorrelationID);
			messageSentStreamMessage.writeString("Testing...");
			logger.log(Logger.Level.TRACE, "Sending Stream message");
			tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
			messageReceivedStreamMessage = (StreamMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "jmsCorrelationID:  " + messageReceivedStreamMessage.getJMSCorrelationID());
			if (messageReceivedStreamMessage.getJMSCorrelationID() == null) {
				pass = false;
				logger.log(Logger.Level.INFO, "Stream Message Error: JMSCorrelationID returned a  null");
			} else if (messageReceivedStreamMessage.getJMSCorrelationID().equals(jmsCorrelationID)) {
				logger.log(Logger.Level.TRACE, "pass");
			} else {
				pass = false;
				logger.log(Logger.Level.INFO, "Stream Message Error: JMSCorrelationID is incorrect");
			}
			if (!pass) {
				throw new Exception("Error: invalid JMSCorrelationID returned from JMS Header");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("msgHdrCorlIdTopicTest");
		}
	}

	/*
	 * @testName: msgHdrReplyToTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:12; JMS:JAVADOC:359; JMS:JAVADOC:361;
	 * JMS:JAVADOC:117; JMS:SPEC:246.8; JMS:JAVADOC:289; JMS:JAVADOC:562;
	 * JMS:JAVADOC:166;
	 * 
	 * @test_Strategy: Send a message to a Topic with ReplyTo set to null and then
	 * set to a destination test with Text, map, object, byte, and stream messages
	 * verify on receive.
	 * 
	 */
	@Test
	public void msgHdrReplyToTopicTest() throws Exception {
		boolean pass = true;
		Topic replyTopic = null;
		byte bValue = 127;

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			StreamMessage messageSentStreamMessage = null;
			StreamMessage messageReceivedStreamMessage = null;
			BytesMessage messageSentBytesMessage = null;
			BytesMessage messageReceivedBytesMessage = null;
			MapMessage messageReceivedMapMessage = null;
			MapMessage messageSentMapMessage = null;
			ObjectMessage messageSentObjectMsg = null;
			ObjectMessage messageReceivedObjectMsg = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
			tool.getDefaultTopicConnection().start();
			messageSent = tool.getDefaultTopicSession().createTextMessage();
			messageSent.setText("sending a message");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrReplyToTopicTest");
			logger.log(Logger.Level.TRACE, "Send Text message");

			// messageSent.setJMSReplyTo(tool.getDefaultTopic());
			tool.getDefaultTopicPublisher().publish(messageSent);
			messageReceived = (TextMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			if (messageReceived.getJMSReplyTo() == null) {
				logger.log(Logger.Level.TRACE, " as expected replyto field is null");
			} else {
				logger.log(Logger.Level.INFO, "ERROR: expected replyto field should have been null for this case");
				pass = false;
			}
			logger.log(Logger.Level.TRACE, "Set ReplyTo and resend msg");
			messageSent.setJMSReplyTo(tool.getDefaultTopic());
			tool.getDefaultTopicPublisher().publish(messageSent);
			messageReceived = (TextMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			replyTopic = (Topic) messageReceived.getJMSReplyTo();
			logger.log(Logger.Level.TRACE, "Topic name is " + replyTopic.getTopicName());
			if (replyTopic.getTopicName().equals(tool.getDefaultTopic().getTopicName())) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Text Message Failed");
				pass = false;
			}

			// send and receive Object message to Topic
			logger.log(Logger.Level.TRACE, "Send ObjectMessage to Topic.");
			messageSentObjectMsg = tool.getDefaultTopicSession().createObjectMessage();
			messageSentObjectMsg.setObject("msgHdrReplyToTopicTest for Object Message");
			messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrReplyToTopicTest");
			tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
			messageReceivedObjectMsg = (ObjectMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			if (messageReceivedObjectMsg.getJMSReplyTo() == null) {
				logger.log(Logger.Level.TRACE, " as expected replyto field is null");
			} else {
				logger.log(Logger.Level.INFO, "ERROR: expected replyto field to be null in this case");
				pass = false;
			}
			logger.log(Logger.Level.TRACE, "Set ReplyTo and resend msg");
			messageSentObjectMsg.setJMSReplyTo(tool.getDefaultTopic());
			tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
			messageReceivedObjectMsg = (ObjectMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			replyTopic = (Topic) messageReceivedObjectMsg.getJMSReplyTo();
			logger.log(Logger.Level.TRACE, "Topic name is " + replyTopic.getTopicName());
			if (replyTopic.getTopicName().equals(tool.getDefaultTopic().getTopicName())) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Object Message ReplyTo Failed");
				pass = false;
			}

			// send and receive map message to Topic
			logger.log(Logger.Level.TRACE, "Send MapMessage to Topic.");
			messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
			messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrReplyToTopicTest");
			messageSentMapMessage.setString("aString", "value");
			tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
			messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			if (messageReceivedMapMessage.getJMSReplyTo() == null) {
				logger.log(Logger.Level.TRACE, " as expected replyto field is null");
			} else {
				logger.log(Logger.Level.INFO, "ERROR: expected replyto field to be null in this case");
				pass = false;
			}
			logger.log(Logger.Level.TRACE, "Set ReplyTo and resend msg");
			messageSentMapMessage.setJMSReplyTo(tool.getDefaultTopic());
			tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
			messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "Received Map message ");
			replyTopic = (Topic) messageReceivedMapMessage.getJMSReplyTo();
			logger.log(Logger.Level.TRACE, "Topic name is " + replyTopic.getTopicName());
			if (replyTopic.getTopicName().equals(tool.getDefaultTopic().getTopicName())) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Map Message ReplyTo Failed");
				pass = false;
			}

			// send and receive bytes message to Topic
			logger.log(Logger.Level.TRACE, "Send BytesMessage to Topic.");
			messageSentBytesMessage = tool.getDefaultTopicSession().createBytesMessage();
			messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrReplyToTopicTest");
			messageSentBytesMessage.writeByte(bValue);
			tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
			messageReceivedBytesMessage = (BytesMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			if (messageReceivedBytesMessage.getJMSReplyTo() == null) {
				logger.log(Logger.Level.TRACE, " as expected replyto field is null");
			} else {
				logger.log(Logger.Level.INFO, "ERROR: expected replyto field to be null in this case");
				pass = false;
			}
			logger.log(Logger.Level.TRACE, "Set ReplyTo and resend msg");
			messageSentBytesMessage.setJMSReplyTo(tool.getDefaultTopic());
			tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
			messageReceivedBytesMessage = (BytesMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "Received Bytes message ");
			replyTopic = (Topic) messageReceivedBytesMessage.getJMSReplyTo();
			logger.log(Logger.Level.TRACE, "Topic name is " + replyTopic.getTopicName());
			if (replyTopic.getTopicName().equals(tool.getDefaultTopic().getTopicName())) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Bytes Message ReplyTo Failed");
				pass = false;
			}

			// Send and receive a StreamMessage
			logger.log(Logger.Level.TRACE, "sending a Stream message");
			messageSentStreamMessage = tool.getDefaultTopicSession().createStreamMessage();
			messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrReplyToTopicTest");
			messageSentStreamMessage.writeString("Testing...");
			logger.log(Logger.Level.TRACE, "Sending Stream message");
			tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
			messageReceivedStreamMessage = (StreamMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			if (messageReceivedStreamMessage.getJMSReplyTo() == null) {
				logger.log(Logger.Level.TRACE, " as expected replyto field is null");
			} else {
				logger.log(Logger.Level.INFO, "ERROR: expected replyto field to be null in this case");
				pass = false;
			}
			logger.log(Logger.Level.TRACE, "Set ReplyTo and resend msg");
			messageSentStreamMessage.setJMSReplyTo(tool.getDefaultTopic());
			tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
			messageReceivedStreamMessage = (StreamMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "Received Stream message ");
			replyTopic = (Topic) messageReceivedStreamMessage.getJMSReplyTo();
			logger.log(Logger.Level.TRACE, "Topic name is " + replyTopic.getTopicName());
			if (replyTopic.getTopicName().equals(tool.getDefaultTopic().getTopicName())) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Stream Message ReplyTo Failed");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: invalid Replyto returned from JMS Header");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("msgHdrReplyToTopicTest");
		}
	}

	/*
	 * @testName: msgHdrJMSTypeTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:246.9; JMS:JAVADOC:375; JMS:JAVADOC:377;
	 * 
	 * @test_Strategy: Send a message to a Topic with JMSType set to TESTMSG test
	 * with Text, map, object, byte, and stream messages verify on receive.
	 * 
	 */
	@Test
	public void msgHdrJMSTypeTopicTest() throws Exception {
		boolean pass = true;
		byte bValue = 127;
		String type = "TESTMSG";

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			StreamMessage messageSentStreamMessage = null;
			StreamMessage messageReceivedStreamMessage = null;
			BytesMessage messageSentBytesMessage = null;
			BytesMessage messageReceivedBytesMessage = null;
			MapMessage messageReceivedMapMessage = null;
			MapMessage messageSentMapMessage = null;
			ObjectMessage messageSentObjectMsg = null;
			ObjectMessage messageReceivedObjectMsg = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
			tool.getDefaultTopicConnection().start();
			messageSent = tool.getDefaultTopicSession().createTextMessage();
			messageSent.setText("sending a message");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSTypeTopicTest");
			logger.log(Logger.Level.TRACE, "JMSType test - Send a Text message");
			messageSent.setJMSType(type);
			tool.getDefaultTopicPublisher().publish(messageSent);
			messageReceived = (TextMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSType is " + (String) messageReceived.getJMSType());
			if (messageReceived.getJMSType().equals(type)) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Text Message Failed");
				pass = false;
			}

			// send and receive Object message to Topic
			logger.log(Logger.Level.TRACE, "JMSType test - Send ObjectMessage to Topic.");
			messageSentObjectMsg = tool.getDefaultTopicSession().createObjectMessage();
			messageSentObjectMsg.setObject("msgHdrJMSTypeTopicTest for Object Message");
			messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSTypeTopicTest");
			messageSentObjectMsg.setJMSType(type);
			tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
			messageReceivedObjectMsg = (ObjectMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSType is " + (String) messageReceivedObjectMsg.getJMSType());
			if (messageReceivedObjectMsg.getJMSType().equals(type)) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Object Message JMSType Failed");
				pass = false;
			}

			// send and receive map message to Topic
			logger.log(Logger.Level.TRACE, "JMSType test - Send MapMessage to Topic.");
			messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
			messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSTypeTopicTest");
			messageSentMapMessage.setString("aString", "value");
			messageSentMapMessage.setJMSType(type);
			tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
			messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSType is " + (String) messageReceivedMapMessage.getJMSType());
			if (messageReceivedMapMessage.getJMSType().equals(type)) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Map Message JMSType Failed");
				pass = false;
			}

			// send and receive bytes message to Topic
			logger.log(Logger.Level.TRACE, "JMSType test - Send BytesMessage to Topic.");
			messageSentBytesMessage = tool.getDefaultTopicSession().createBytesMessage();
			messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSTypeTopicTest");
			messageSentBytesMessage.writeByte(bValue);
			messageSentBytesMessage.setJMSType(type);
			tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
			messageReceivedBytesMessage = (BytesMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSType is " + (String) messageReceivedBytesMessage.getJMSType());
			if (messageReceivedBytesMessage.getJMSType().equals(type)) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Bytes Message JMSType Failed");
				pass = false;
			}

			// Send and receive a StreamMessage
			logger.log(Logger.Level.TRACE, "JMSType test - sending a Stream message");
			messageSentStreamMessage = tool.getDefaultTopicSession().createStreamMessage();
			messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSTypeTopicTest");
			messageSentStreamMessage.writeString("Testing...");
			messageSentStreamMessage.setJMSType(type);
			tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
			messageReceivedStreamMessage = (StreamMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSType is " + (String) messageReceivedStreamMessage.getJMSType());
			if (messageReceivedStreamMessage.getJMSType().equals(type)) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Stream Message JMSType Failed");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: invalid JMSType returned from JMS Header");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("msgHdrJMSTypeTopicTest");
		}
	}

	/*
	 * @testName: msgHdrJMSPriorityTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:16; JMS:SPEC:18; JMS:SPEC:140; JMS:JAVADOC:305;
	 * JMS:JAVADOC:383;
	 * 
	 * @test_Strategy: Send a message to a Topic with JMSPriority set to 2 test with
	 * Text, map, object, byte, and stream messages verify on receive.
	 * 
	 */
	@Test
	public void msgHdrJMSPriorityTopicTest() throws Exception {
		boolean pass = true;
		byte bValue = 127;
		int priority = 2;

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			StreamMessage messageSentStreamMessage = null;
			StreamMessage messageReceivedStreamMessage = null;
			BytesMessage messageSentBytesMessage = null;
			BytesMessage messageReceivedBytesMessage = null;
			MapMessage messageReceivedMapMessage = null;
			MapMessage messageSentMapMessage = null;
			ObjectMessage messageSentObjectMsg = null;
			ObjectMessage messageReceivedObjectMsg = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
			tool.getDefaultTopicConnection().start();
			messageSent = tool.getDefaultTopicSession().createTextMessage();
			messageSent.setText("sending a message");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSPriorityTopicTest");
			logger.log(Logger.Level.TRACE, "JMSPriority test - Send a Text message");
			tool.getDefaultTopicPublisher().setPriority(priority);
			tool.getDefaultTopicPublisher().publish(messageSent);
			messageReceived = (TextMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSPriority is " + messageReceived.getJMSPriority());
			if (messageReceived.getJMSPriority() == priority) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Text Message Failed");
				pass = false;
			}

			// send and receive Object message to Topic
			logger.log(Logger.Level.TRACE, "JMSPriority test - Send ObjectMessage to Topic.");
			messageSentObjectMsg = tool.getDefaultTopicSession().createObjectMessage();
			messageSentObjectMsg.setObject("msgHdrJMSPriorityTopicTest for Object Message");
			messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSPriorityTopicTest");
			tool.getDefaultTopicPublisher().setPriority(priority);
			tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
			messageReceivedObjectMsg = (ObjectMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSPriority is " + messageReceivedObjectMsg.getJMSPriority());
			if (messageReceivedObjectMsg.getJMSPriority() == priority) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Object Message JMSPriority Failed");
				pass = false;
			}

			// send and receive map message to Topic
			logger.log(Logger.Level.TRACE, "JMSPriority test - Send MapMessage to Topic.");
			messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
			messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSPriorityTopicTest");
			messageSentMapMessage.setString("aString", "value");
			tool.getDefaultTopicPublisher().setPriority(priority);
			tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
			messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSPriority is " + messageReceivedMapMessage.getJMSPriority());
			if (messageReceivedMapMessage.getJMSPriority() == priority) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Map Message JMSPriority Failed");
				pass = false;
			}

			// send and receive bytes message to Topic
			logger.log(Logger.Level.TRACE, "JMSPriority test - Send BytesMessage to Topic.");
			messageSentBytesMessage = tool.getDefaultTopicSession().createBytesMessage();
			messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSPriorityTopicTest");
			messageSentBytesMessage.writeByte(bValue);
			tool.getDefaultTopicPublisher().setPriority(priority);
			tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
			messageReceivedBytesMessage = (BytesMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSPriority is " + messageReceivedBytesMessage.getJMSPriority());
			if (messageReceivedBytesMessage.getJMSPriority() == priority) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Bytes Message JMSPriority Failed");
				pass = false;
			}

			// Send and receive a StreamMessage
			logger.log(Logger.Level.TRACE, "JMSPriority test - sending a Stream message");
			messageSentStreamMessage = tool.getDefaultTopicSession().createStreamMessage();
			messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSPriorityTopicTest");
			messageSentStreamMessage.writeString("Testing...");
			tool.getDefaultTopicPublisher().setPriority(priority);
			tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
			messageReceivedStreamMessage = (StreamMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSPriority is " + messageReceivedStreamMessage.getJMSPriority());
			if (messageReceivedStreamMessage.getJMSPriority() == priority) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Stream Message JMSPriority Failed");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: invalid JMSPriority returned from JMS Header");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("msgHdrJMSPriorityTopicTest");
		}
	}

	/*
	 * @testName: msgHdrJMSExpirationTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:15; JMS:SPEC:15.2; JMS:SPEC:15.3; JMS:SPEC:140;
	 * JMS:JAVADOC:309; JMS:JAVADOC:379;
	 * 
	 * @test_Strategy: Send a message to a Topic with time to live set to 0 Verify
	 * that JMSExpiration gets set to 0 test with Text, map, object, byte, and
	 * stream messages verify on receive.
	 * 
	 */
	@Test
	public void msgHdrJMSExpirationTopicTest() throws Exception {
		boolean pass = true;
		byte bValue = 127;
		long forever = 0;

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			StreamMessage messageSentStreamMessage = null;
			StreamMessage messageReceivedStreamMessage = null;
			BytesMessage messageSentBytesMessage = null;
			BytesMessage messageReceivedBytesMessage = null;
			MapMessage messageReceivedMapMessage = null;
			MapMessage messageSentMapMessage = null;
			ObjectMessage messageSentObjectMsg = null;
			ObjectMessage messageReceivedObjectMsg = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
			tool.getDefaultTopicConnection().start();
			messageSent = tool.getDefaultTopicSession().createTextMessage();
			messageSent.setText("sending a message");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSExpirationTopicTest");
			logger.log(Logger.Level.INFO, "JMSExpiration test (set timetoLive=0) - Send TextMessage to Topic");
			tool.getDefaultTopicPublisher().setTimeToLive(forever);
			tool.getDefaultTopicPublisher().publish(messageSent);
			messageReceived = (TextMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.INFO, "JMSExpiration is " + messageReceived.getJMSExpiration());
			if (messageReceived.getJMSExpiration() == forever) {
				logger.log(Logger.Level.INFO, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "TextMessage JMSExpiration Failed");
				logger.log(Logger.Level.INFO, "TextMessage.getJMSExpiration() returned  "
						+ messageReceived.getJMSExpiration() + ", expected 0");
				pass = false;
			}
			logger.log(Logger.Level.INFO, "JMSExpiration test (set timetoLive=60000) - Send TextMessage to Topic");
			tool.getDefaultTopicPublisher().setTimeToLive(60000);
			tool.getDefaultTopicPublisher().publish(messageSent);
			messageReceived = (TextMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			long currentTime = System.currentTimeMillis();
			logger.log(Logger.Level.INFO, "JMSExpiration is " + messageReceived.getJMSExpiration());
			long timeLeftToExpiration = messageReceived.getJMSExpiration() - currentTime;
			logger.log(Logger.Level.INFO, "TimeLeftToExpiration is " + timeLeftToExpiration);
			if (timeLeftToExpiration <= 60000) {
				logger.log(Logger.Level.INFO, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "TextMessage JMSExpiration Failed");
				logger.log(Logger.Level.INFO, "TextMessage JMSExpiration timeLeftToExpiration=" + timeLeftToExpiration
						+ ", expected <=60000");
				pass = false;
			}

			// send and receive Object message to Topic
			logger.log(Logger.Level.INFO, "JMSExpiration test (set timeToLive=0) - Send ObjectMessage to Topic.");
			messageSentObjectMsg = tool.getDefaultTopicSession().createObjectMessage();
			messageSentObjectMsg.setObject("msgHdrJMSExpirationTopicTest for Object Message");
			messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSExpirationTopicTest");
			tool.getDefaultTopicPublisher().setTimeToLive(forever);
			tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
			messageReceivedObjectMsg = (ObjectMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.INFO, "JMSExpiration is " + messageReceivedObjectMsg.getJMSExpiration());
			if (messageReceivedObjectMsg.getJMSExpiration() == forever) {
				logger.log(Logger.Level.INFO, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "ObjectMessage JMSExpiration Failed");
				logger.log(Logger.Level.INFO, "ObjectMessage.getJMSExpiration() returned  "
						+ messageReceivedObjectMsg.getJMSExpiration() + ", expected 0");
				pass = false;
			}
			logger.log(Logger.Level.INFO, "JMSExpiration test (set timetoLive=60000) - Send ObjectMessage to Topic.");
			tool.getDefaultTopicPublisher().setTimeToLive(60000);
			tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
			messageReceivedObjectMsg = (ObjectMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			currentTime = System.currentTimeMillis();
			logger.log(Logger.Level.INFO, "JMSExpiration is " + messageReceivedObjectMsg.getJMSExpiration());
			timeLeftToExpiration = messageReceivedObjectMsg.getJMSExpiration() - currentTime;
			logger.log(Logger.Level.INFO, "TimeLeftToExpiration is " + timeLeftToExpiration);
			if (timeLeftToExpiration <= 60000) {
				logger.log(Logger.Level.INFO, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "ObjectMessage JMSExpiration Failed");
				logger.log(Logger.Level.INFO, "ObjectMessage JMSExpiration timeLeftToExpiration=" + timeLeftToExpiration
						+ ", expected <=60000");
				pass = false;
			}

			// send and receive map message to Topic
			logger.log(Logger.Level.INFO, "JMSExpiration test (set timeToLive=0) - Send MapMessage to Topic.");
			messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
			messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSExpirationTopicTest");
			messageSentMapMessage.setString("aString", "value");
			tool.getDefaultTopicPublisher().setTimeToLive(forever);
			tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
			messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.INFO, "JMSExpiration is " + messageReceivedMapMessage.getJMSExpiration());
			if (messageReceivedMapMessage.getJMSExpiration() == forever) {
				logger.log(Logger.Level.INFO, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "MapMessage JMSExpiration Failed");
				logger.log(Logger.Level.INFO, "MapMessage.getJMSExpiration() returned  "
						+ messageReceivedMapMessage.getJMSExpiration() + ", expected 0");
				pass = false;
			}
			logger.log(Logger.Level.INFO, "JMSExpiration test (set timetoLive=60000) - Send MapMessage to Topic.");
			tool.getDefaultTopicPublisher().setTimeToLive(60000);
			tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
			messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			currentTime = System.currentTimeMillis();
			logger.log(Logger.Level.INFO, "JMSExpiration is " + messageReceivedMapMessage.getJMSExpiration());
			timeLeftToExpiration = messageReceivedMapMessage.getJMSExpiration() - currentTime;
			logger.log(Logger.Level.INFO, "TimeLeftToExpiration is " + timeLeftToExpiration);
			if (timeLeftToExpiration <= 60000) {
				logger.log(Logger.Level.INFO, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "MapMessage JMSExpiration Failed");
				logger.log(Logger.Level.INFO,
						"MapMessage JMSExpiration timeLeftToExpiration=" + timeLeftToExpiration + ", expected <=60000");
				pass = false;
			}

			// send and receive bytes message to Topic
			logger.log(Logger.Level.INFO, "JMSExpiration test (set timeToLive=0) - Send BytesMessage to Topic.");
			messageSentBytesMessage = tool.getDefaultTopicSession().createBytesMessage();
			messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSExpirationTopicTest");
			messageSentBytesMessage.writeByte(bValue);
			tool.getDefaultTopicPublisher().setTimeToLive(forever);
			tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
			messageReceivedBytesMessage = (BytesMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.INFO, "JMSExpiration is " + messageReceivedBytesMessage.getJMSExpiration());
			if (messageReceivedBytesMessage.getJMSExpiration() == forever) {
				logger.log(Logger.Level.INFO, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "BytesMessage JMSExpiration Failed");
				logger.log(Logger.Level.INFO, "BytesMessage.getJMSExpiration() returned  "
						+ messageReceivedBytesMessage.getJMSExpiration() + ", expected 0");
				pass = false;
			}
			logger.log(Logger.Level.INFO, "JMSExpiration test (set timetoLive=60000) - Send BytesMessage to Topic.");
			tool.getDefaultTopicPublisher().setTimeToLive(60000);
			tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
			messageReceivedBytesMessage = (BytesMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			currentTime = System.currentTimeMillis();
			logger.log(Logger.Level.INFO, "JMSExpiration is " + messageReceivedBytesMessage.getJMSExpiration());
			timeLeftToExpiration = messageReceivedBytesMessage.getJMSExpiration() - currentTime;
			logger.log(Logger.Level.INFO, "TimeLeftToExpiration is " + timeLeftToExpiration);
			if (timeLeftToExpiration <= 60000) {
				logger.log(Logger.Level.INFO, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "BytesMessage JMSExpiration Failed");
				logger.log(Logger.Level.INFO, "BytesMessage JMSExpiration timeLeftToExpiration=" + timeLeftToExpiration
						+ ", expected <=60000");
				pass = false;
			}

			// Send and receive a StreamMessage
			logger.log(Logger.Level.INFO, "JMSExpiration test (set timeToLive=0) - sending a Stream message");
			messageSentStreamMessage = tool.getDefaultTopicSession().createStreamMessage();
			messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSExpirationTopicTest");
			messageSentStreamMessage.writeString("Testing...");
			tool.getDefaultTopicPublisher().setTimeToLive(forever);
			tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
			messageReceivedStreamMessage = (StreamMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.INFO, "JMSExpiration is " + messageReceivedStreamMessage.getJMSExpiration());
			if (messageReceivedStreamMessage.getJMSExpiration() == forever) {
				logger.log(Logger.Level.INFO, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "StreamMessage JMSExpiration Failed");
				logger.log(Logger.Level.INFO, "StreamMessage.getJMSExpiration() returned  "
						+ messageReceivedStreamMessage.getJMSExpiration() + ", expected 0");
				pass = false;
			}
			logger.log(Logger.Level.INFO, "JMSExpiration test (set timetoLive=60000) - Send StreamMessage to Topic.");
			tool.getDefaultTopicPublisher().setTimeToLive(60000);
			tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
			messageReceivedStreamMessage = (StreamMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			currentTime = System.currentTimeMillis();
			logger.log(Logger.Level.INFO, "JMSExpiration is " + messageReceivedStreamMessage.getJMSExpiration());
			timeLeftToExpiration = messageReceivedStreamMessage.getJMSExpiration() - currentTime;
			logger.log(Logger.Level.INFO, "TimeLeftToExpiration is " + timeLeftToExpiration);
			if (timeLeftToExpiration <= 60000) {
				logger.log(Logger.Level.INFO, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "StreamMessage JMSExpiration Failed");
				logger.log(Logger.Level.INFO, "StreamMessage JMSExpiration timeLeftToExpiration=" + timeLeftToExpiration
						+ ", expected <=60000");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: invalid JMSExpiration returned from JMS Header");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("msgHdrJMSExpirationTopicTest");
		}
	}

	/*
	 * @testName: msgHdrJMSDestinationTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:2; JMS:JAVADOC:363; JMS:JAVADOC:117;
	 * 
	 * @test_Strategy: Create and publish a message to the default Topic. Receive
	 * the msg and verify that JMSDestination is set to the default Topic test with
	 * Text, map, object, byte, and stream messages
	 * 
	 */
	@Test
	public void msgHdrJMSDestinationTopicTest() throws Exception {
		boolean pass = true;
		byte bValue = 127;
		Topic replyDestination = null;

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			StreamMessage messageSentStreamMessage = null;
			StreamMessage messageReceivedStreamMessage = null;
			BytesMessage messageSentBytesMessage = null;
			BytesMessage messageReceivedBytesMessage = null;
			MapMessage messageReceivedMapMessage = null;
			MapMessage messageSentMapMessage = null;
			ObjectMessage messageSentObjectMsg = null;
			ObjectMessage messageReceivedObjectMsg = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
			tool.getDefaultTopicConnection().start();
			messageSent = tool.getDefaultTopicSession().createTextMessage();
			messageSent.setText("publishing a message");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDestinationTopicTest");
			logger.log(Logger.Level.TRACE, "publish Text Message to Topic.");
			tool.getDefaultTopicPublisher().publish(messageSent);
			messageReceived = (TextMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSDestination:  " + messageReceived.getJMSDestination());
			replyDestination = (Topic) messageReceived.getJMSDestination();
			if (replyDestination != null)
				logger.log(Logger.Level.TRACE, "Topic name is " + replyDestination.getTopicName());
			if (replyDestination == null) {
				pass = false;
				logger.log(Logger.Level.INFO, "Text Message Error: JMSDestination returned a  null");
			} else if (replyDestination.getTopicName().equals(tool.getDefaultTopic().getTopicName())) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Text Message Failed");
				pass = false;
			}

			// publish and receive Object message to Topic
			logger.log(Logger.Level.TRACE, "publish ObjectMessage to Topic.");
			messageSentObjectMsg = tool.getDefaultTopicSession().createObjectMessage();
			messageSentObjectMsg.setObject("msgHdrIDQTest for Object Message");
			messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDestinationTopicTest");
			tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
			messageReceivedObjectMsg = (ObjectMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSDestination:  " + messageReceivedObjectMsg.getJMSDestination());
			replyDestination = (Topic) messageReceived.getJMSDestination();
			if (replyDestination != null)
				logger.log(Logger.Level.TRACE, "Topic name is " + replyDestination.getTopicName());
			if (replyDestination == null) {
				pass = false;
				logger.log(Logger.Level.INFO, "Object Message Error: JMSDestination returned a  null");
			} else if (replyDestination.getTopicName().equals(tool.getDefaultTopic().getTopicName())) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Object Message Failed");
				pass = false;
			}

			// publish and receive map message to Topic
			logger.log(Logger.Level.TRACE, "publish MapMessage to Topic.");
			messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
			messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDestinationTopicTest");
			messageSentMapMessage.setString("aString", "value");
			tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
			messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSDestination:  " + messageReceivedMapMessage.getJMSDestination());
			replyDestination = (Topic) messageReceived.getJMSDestination();
			if (replyDestination != null)
				logger.log(Logger.Level.TRACE, "Topic name is " + replyDestination.getTopicName());
			if (replyDestination == null) {
				pass = false;
				logger.log(Logger.Level.INFO, "Map Message Error: JMSDestination returned a  null");
			} else if (replyDestination.getTopicName().equals(tool.getDefaultTopic().getTopicName())) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Map Message Failed");
				pass = false;
			}

			// publish and receive bytes message to Topic
			logger.log(Logger.Level.TRACE, "publish BytesMessage to Topic.");
			messageSentBytesMessage = tool.getDefaultTopicSession().createBytesMessage();
			messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDestinationTopicTest");
			messageSentBytesMessage.writeByte(bValue);
			tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
			messageReceivedBytesMessage = (BytesMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSDestination:  " + messageReceivedBytesMessage.getJMSDestination());
			replyDestination = (Topic) messageReceived.getJMSDestination();
			if (replyDestination != null)
				logger.log(Logger.Level.TRACE, "Topic name is " + replyDestination.getTopicName());
			if (replyDestination == null) {
				pass = false;
				logger.log(Logger.Level.INFO, "Bytes Message Error: JMSDestination returned a  null");
			} else if (replyDestination.getTopicName().equals(tool.getDefaultTopic().getTopicName())) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Bytes Message Failed");
				pass = false;
			}

			// publish and receive a StreamMessage
			logger.log(Logger.Level.TRACE, "publishing a Stream message");
			messageSentStreamMessage = tool.getDefaultTopicSession().createStreamMessage();
			messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDestinationTopicTest");
			messageSentStreamMessage.writeString("Testing...");
			logger.log(Logger.Level.TRACE, "publishing Stream message");
			tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
			messageReceivedStreamMessage = (StreamMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSDestination:  " + messageReceivedStreamMessage.getJMSDestination());
			replyDestination = (Topic) messageReceived.getJMSDestination();
			if (replyDestination != null)
				logger.log(Logger.Level.TRACE, "Topic name is " + replyDestination.getTopicName());
			if (replyDestination == null) {
				pass = false;
				logger.log(Logger.Level.INFO, "Stream Message Error: JMSDestination returned a  null");
			} else if (replyDestination.getTopicName().equals(tool.getDefaultTopic().getTopicName())) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Stream Message Failed");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: invalid JMSDestination returned from JMS Header");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("msgHdrJMSDestinationTopicTest");
		}
	}

	/*
	 * @testName: msgHdrJMSDeliveryModeTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:3; JMS:SPEC:140; JMS:JAVADOC:367; JMS:SPEC:246.2;
	 * JMS:JAVADOC:301;
	 * 
	 * @test_Strategy: Create and publish a message to the default Topic. Receive
	 * the msg and verify that JMSDeliveryMode is set the default delivery mode of
	 * persistent. Create and test another message with a nonpersistent delivery
	 * mode. test with Text, map, object, byte, and stream messages
	 */
	@Test
	public void msgHdrJMSDeliveryModeTopicTest() throws Exception {
		boolean pass = true;
		byte bValue = 127;

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			StreamMessage messageSentStreamMessage = null;
			StreamMessage messageReceivedStreamMessage = null;
			BytesMessage messageSentBytesMessage = null;
			BytesMessage messageReceivedBytesMessage = null;
			MapMessage messageReceivedMapMessage = null;
			MapMessage messageSentMapMessage = null;
			ObjectMessage messageSentObjectMsg = null;
			ObjectMessage messageReceivedObjectMsg = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
			tool.getDefaultTopicConnection().start();
			logger.log(Logger.Level.INFO, "default delivery mode is " + Message.DEFAULT_DELIVERY_MODE);
			logger.log(Logger.Level.INFO, "persistent is: " + DeliveryMode.PERSISTENT);
			if (Message.DEFAULT_DELIVERY_MODE != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.INFO, "Error: default delivery mode was " + Message.DEFAULT_DELIVERY_MODE);
				logger.log(Logger.Level.INFO,
						"The default delivery mode should be persistent: " + DeliveryMode.PERSISTENT);
			}
			messageSent = tool.getDefaultTopicSession().createTextMessage();
			messageSent.setText("publishing a message");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDeliveryModeTopicTest");
			logger.log(Logger.Level.TRACE, "publish Text Message to Topic.");
			tool.getDefaultTopicPublisher().publish(messageSent);
			messageReceived = (TextMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
			if (messageReceived.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.INFO,
						"Text Message Error: JMSDeliveryMode should be set to persistent as default");
			} else {
				logger.log(Logger.Level.TRACE, "Text Message Pass ");
			}

			// publish and receive Object message to Topic
			logger.log(Logger.Level.TRACE, "publish ObjectMessage to Topic.");
			messageSentObjectMsg = tool.getDefaultTopicSession().createObjectMessage();
			messageSentObjectMsg.setObject("Test for Object Message");
			messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDeliveryModeTopicTest");
			tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
			messageReceivedObjectMsg = (ObjectMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
			if (messageReceived.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.INFO,
						"Object Message Error: JMSDeliveryMode should be set to persistent as default");
			} else {
				logger.log(Logger.Level.TRACE, "Object Message Pass ");
			}

			// publish and receive map message to Topic
			logger.log(Logger.Level.TRACE, "publish MapMessage to Topic.");
			messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
			messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDeliveryModeTopicTest");
			messageSentMapMessage.setString("aString", "value");
			tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
			messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
			if (messageReceived.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.INFO,
						"Map Message Error: JMSDeliveryMode should be set to persistent as default");
			} else {
				logger.log(Logger.Level.TRACE, "Map Message Pass ");
			}

			// publish and receive bytes message to Topic
			logger.log(Logger.Level.TRACE, "publish BytesMessage to Topic.");
			messageSentBytesMessage = tool.getDefaultTopicSession().createBytesMessage();
			messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDeliveryModeTopicTest");
			messageSentBytesMessage.writeByte(bValue);
			tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
			messageReceivedBytesMessage = (BytesMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
			if (messageReceived.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.INFO,
						"Bytes Message Error: JMSDeliveryMode should be set to persistent as default");
			} else {
				logger.log(Logger.Level.TRACE, "Bytes Message Pass ");
			}

			// publish and receive a StreamMessage
			logger.log(Logger.Level.TRACE, "publishing a Stream message");
			messageSentStreamMessage = tool.getDefaultTopicSession().createStreamMessage();
			messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDeliveryModeTopicTest");
			messageSentStreamMessage.writeString("Testing...");
			logger.log(Logger.Level.TRACE, "publishing Stream message");
			tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
			messageReceivedStreamMessage = (StreamMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
			if (messageReceived.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.INFO,
						"Stream Message Error: JMSDeliveryMode should be set to persistent as default");
			} else {
				logger.log(Logger.Level.TRACE, "Stream Message Pass ");
			}

			// Test again - this time set delivery mode to persistent
			tool.getDefaultTopicPublisher().setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			messageSent = tool.getDefaultTopicSession().createTextMessage();
			messageSent.setText("publishing a message");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDeliveryModeTopicTest");
			logger.log(Logger.Level.TRACE, "publish Text Message to Topic.");
			tool.getDefaultTopicPublisher().publish(messageSent);
			messageReceived = (TextMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
			if (messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.INFO, "Text Message Error: JMSDeliveryMode should be set to NON_PERSISTENT");
			} else {
				logger.log(Logger.Level.TRACE, "Text Message Pass ");
			}

			// publish and receive Object message to Topic
			logger.log(Logger.Level.TRACE, "publish ObjectMessage to Topic.");
			messageSentObjectMsg = tool.getDefaultTopicSession().createObjectMessage();
			messageSentObjectMsg.setObject("msgHdrJMSDeliveryModeTopicTest for Object Message");
			messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDeliveryModeTopicTest");
			tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
			messageReceivedObjectMsg = (ObjectMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			if (messageReceivedObjectMsg != null)
				logger.log(Logger.Level.TRACE, "messageReceivedObjectMsg=" + messageReceivedObjectMsg);
			logger.log(Logger.Level.TRACE, "JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
			if (messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.INFO, "Object Message Error: JMSDeliveryMode should be set to NON_PERSISTENT");
			} else {
				logger.log(Logger.Level.TRACE, "Object Message Pass ");
			}

			// publish and receive map message to Topic
			logger.log(Logger.Level.TRACE, "publish MapMessage to Topic.");
			messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
			messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDeliveryModeTopicTest");
			messageSentMapMessage.setString("aString", "value");
			tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
			messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			if (messageReceivedMapMessage != null)
				logger.log(Logger.Level.TRACE, "messageReceivedMapMessage=" + messageReceivedMapMessage);
			logger.log(Logger.Level.TRACE, "JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
			if (messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.INFO, "Map Message Error: JMSDeliveryMode should be set to NON_PERSISTENT");
			} else {
				logger.log(Logger.Level.TRACE, "Map Message Pass ");
			}

			// publish and receive bytes message to Topic
			logger.log(Logger.Level.TRACE, "publish BytesMessage to Topic.");
			messageSentBytesMessage = tool.getDefaultTopicSession().createBytesMessage();
			messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDeliveryModeTopicTest");
			messageSentBytesMessage.writeByte(bValue);
			tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
			messageReceivedBytesMessage = (BytesMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			if (messageReceivedBytesMessage != null)
				logger.log(Logger.Level.TRACE, "messageReceivedBytesMessage=" + messageReceivedBytesMessage);
			logger.log(Logger.Level.TRACE, "JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
			if (messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.INFO, "Bytes Message Error: JMSDeliveryMode should be set to NON_PERSISTENT");
			} else {
				logger.log(Logger.Level.TRACE, "Bytes Message Pass ");
			}

			// publish and receive a StreamMessage
			logger.log(Logger.Level.TRACE, "publishing a Stream message");
			messageSentStreamMessage = tool.getDefaultTopicSession().createStreamMessage();
			messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDeliveryModeTopicTest");
			messageSentStreamMessage.writeString("Testing...");
			logger.log(Logger.Level.TRACE, "publishing Stream message");
			tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
			messageReceivedStreamMessage = (StreamMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			if (messageReceivedStreamMessage != null)
				logger.log(Logger.Level.TRACE, "messageReceivedStreamMessage=" + messageReceivedStreamMessage);
			logger.log(Logger.Level.TRACE, "JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
			if (messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.INFO, "Stream Message Error: JMSDeliveryMode should be set to NON_PERSISTENT");
			} else {
				logger.log(Logger.Level.TRACE, "Stream Message Pass ");
			}
			if (!pass) {
				throw new Exception("Error: invalid JMSDeliveryMode returned from JMS Header");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("msgHdrJMSDeliveryModeTopicTest");
		}
	}

}
