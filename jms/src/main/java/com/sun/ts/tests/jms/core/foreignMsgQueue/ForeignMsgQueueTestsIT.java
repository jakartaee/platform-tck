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
package com.sun.ts.tests.jms.core.foreignMsgQueue;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.BytesMessageTestImpl;
import com.sun.ts.tests.jms.common.JmsTool;
import com.sun.ts.tests.jms.common.MapMessageTestImpl;
import com.sun.ts.tests.jms.common.MessageTestImpl;
import com.sun.ts.tests.jms.common.ObjectMessageTestImpl;
import com.sun.ts.tests.jms.common.StreamMessageTestImpl;
import com.sun.ts.tests.jms.common.TextMessageTestImpl;

import jakarta.jms.BytesMessage;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.StreamMessage;
import jakarta.jms.TextMessage;


public class ForeignMsgQueueTestsIT {
	private static final String testName = "com.sun.ts.tests.jms.core.foreignMsgQueue.ForeignMsgQueueTests";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(ForeignMsgQueueTestsIT.class.getName());

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

	// values to pass into/read from messages
	boolean testBoolean = true;

	byte testByte = 100;

	char testChar = 'a';

	int testInt = 10;

	Object testObject = new Double(3.141);

	String testString = "java";

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
				throw new Exception("'timeout' (milliseconds) in must be > 0");
			}
			if (user == null) {
				throw new Exception("'user' is null");
			}
			if (password == null) {
				throw new Exception("'password' is null");
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
	 * @testName: sendReceiveBytesMsgQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88;
	 * 
	 * @test_Strategy: Send message with appropriate data Receive message and check
	 * data
	 */
	@Test
	public void sendReceiveBytesMsgQueueTest() throws Exception {
		boolean pass = true;

		try {
			BytesMessage messageSent = null;
			BytesMessage messageReceived = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = new BytesMessageTestImpl();
			logger.log(Logger.Level.TRACE, "Setting test values in MessageTestImpl.javaessage");
			long bodyLength = 22L;
			BytesMessageTestImpl messageSentImpl = (BytesMessageTestImpl) messageSent;
			messageSentImpl.setBodyLength(bodyLength);
			messageSent.writeBoolean(testBoolean);
			messageSent.writeByte(testByte);
			messageSent.writeChar(testChar);
			messageSent.writeInt(testInt);
			messageSent.writeObject(testObject);
			messageSent.writeUTF(testString);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "sendReceiveBytesMsgQueueTest");
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (BytesMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("Did not receive message");
			}
			logger.log(Logger.Level.TRACE, "Check received message");
			if (messageReceived.readBoolean() == testBoolean) {
				logger.log(Logger.Level.TRACE, "Received correct boolean value");
			} else {
				logger.log(Logger.Level.INFO, "incorrect boolean value -- BAD");
				pass = false;
			}
			if (messageReceived.readByte() == testByte) {
				logger.log(Logger.Level.TRACE, "Received correct byte value");
			} else {
				logger.log(Logger.Level.INFO, "incorrect byte value -- BAD");
				pass = false;
			}
			if (messageReceived.readChar() == testChar) {
				logger.log(Logger.Level.TRACE, "Received correct char value");
			} else {
				logger.log(Logger.Level.INFO, "incorrect char value -- BAD");
				pass = false;
			}
			if (messageReceived.readInt() == testInt) {
				logger.log(Logger.Level.TRACE, "Received correct int value");
			} else {
				logger.log(Logger.Level.INFO, "incorrect int value -- BAD");
				pass = false;
			}
			if (messageReceived.readDouble() == ((Double) testObject).doubleValue()) {
				logger.log(Logger.Level.TRACE, "Received correct object");
			} else {
				logger.log(Logger.Level.INFO, "incorrect object -- BAD");
				pass = false;
			}
			if (messageReceived.readUTF().equals(testString)) {
				logger.log(Logger.Level.TRACE, "Received correct String");
			} else {
				logger.log(Logger.Level.INFO, "incorrect string -- BAD");
				pass = false;
			}
			if (pass == false) {
				logger.log(Logger.Level.INFO, "Test failed -- see above");
				throw new Exception();
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("sendReceiveBytesMsgQueueTest");
		}
	}

	/*
	 * @testName: sendReceiveMsgQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88;
	 * 
	 * @test_Strategy: Send message with appropriate data Receive message and check
	 * data
	 */
	@Test
	public void sendReceiveMsgQueueTest() throws Exception {
		try {
			Message messageSent = null;
			Message messageReceived = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.INFO, "Creating 1 message");
			messageSent = new MessageTestImpl();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "sendReceiveMsgQueueTest");
			logger.log(Logger.Level.INFO, "sending: " + messageSent);
			logger.log(Logger.Level.INFO, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.INFO, "Receiving message");
			messageReceived = tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.INFO, "received: " + messageReceived);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("sendReceiveMsgQueueTest");
		}
	}

	/*
	 * @testName: sendReceiveMapMsgQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88;
	 * 
	 * @test_Strategy: Send message with appropriate data Receive message and check
	 * data
	 */
	@Test
	public void sendReceiveMapMsgQueueTest() throws Exception {
		boolean pass = true;

		try {
			MapMessage messageSent = null;
			MapMessage messageReceived = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = new MapMessageTestImpl();
			logger.log(Logger.Level.TRACE, "Setting test values in message");
			messageSent.setBoolean("TestBoolean", testBoolean);
			messageSent.setByte("TestByte", testByte);
			messageSent.setChar("TestChar", testChar);
			messageSent.setInt("TestInt", testInt);
			messageSent.setObject("TestDouble", testObject);
			messageSent.setString("TestString", testString);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "sendReceiveMapMsgQueueTest");
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("Did not receive message");
			}
			logger.log(Logger.Level.TRACE, "Check received message");
			if (messageReceived.getBoolean("TestBoolean") == testBoolean) {
				logger.log(Logger.Level.TRACE, "Received correct boolean value");
			} else {
				logger.log(Logger.Level.INFO, "incorrect boolean value -- BAD");
				pass = false;
			}
			if (messageReceived.getByte("TestByte") == testByte) {
				logger.log(Logger.Level.TRACE, "Received correct byte value");
			} else {
				logger.log(Logger.Level.INFO, "incorrect byte value -- BAD");
				pass = false;
			}
			if (messageReceived.getChar("TestChar") == testChar) {
				logger.log(Logger.Level.TRACE, "Received correct char value");
			} else {
				logger.log(Logger.Level.INFO, "incorrect char value -- BAD");
				pass = false;
			}
			if (messageReceived.getInt("TestInt") == testInt) {
				logger.log(Logger.Level.TRACE, "Received correct int value");
			} else {
				logger.log(Logger.Level.INFO, "incorrect int value -- BAD");
				pass = false;
			}
			if (messageReceived.getDouble("TestDouble") == ((Double) testObject).doubleValue()) {
				logger.log(Logger.Level.TRACE, "Received correct object");
			} else {
				logger.log(Logger.Level.INFO, "incorrect object -- BAD");
				pass = false;
			}
			if (messageReceived.getString("TestString").equals(testString)) {
				logger.log(Logger.Level.TRACE, "Received correct String");
			} else {
				logger.log(Logger.Level.INFO, "incorrect string -- BAD");
				pass = false;
			}
			if (pass == false) {
				logger.log(Logger.Level.INFO, "Test failed -- see above");
				throw new Exception();
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("sendReceiveMapMsgQueueTest");
		}
	}

	/*
	 * @testName: sendReceiveObjectMsgQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88;
	 * 
	 * @test_Strategy: Send message with appropriate data Receive message and check
	 * data
	 */
	@Test
	public void sendReceiveObjectMsgQueueTest() throws Exception {
		try {
			ObjectMessage messageSent = null;
			ObjectMessage messageReceived = null;
			String text = "test";

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.INFO, "Creating 1 message");
			messageSent = new ObjectMessageTestImpl();
			messageSent.setObject(text);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "sendReceiveObjectMsgQueueTest");
			logger.log(Logger.Level.INFO, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.INFO, "Receiving message");
			messageReceived = (ObjectMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("Did not receive message");
			}
			if (((String) messageReceived.getObject()).equals(text)) {
				logger.log(Logger.Level.INFO, "Received correct object");
			} else {
				throw new Exception("Did not receive correct message");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("sendReceiveObjectMsgQueueTest");
		}
	}

	/*
	 * @testName: sendReceiveStreamMsgQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88;
	 * 
	 * @test_Strategy: Send message with appropriate data Receive message and check
	 * data
	 */
	@Test
	public void sendReceiveStreamMsgQueueTest() throws Exception {
		boolean pass = true;

		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.INFO, "Creating 1 message");
			messageSent = new StreamMessageTestImpl();
			logger.log(Logger.Level.TRACE, "Setting test values in message");
			messageSent.writeBoolean(testBoolean);
			messageSent.writeByte(testByte);
			messageSent.writeChar(testChar);
			messageSent.writeInt(testInt);
			messageSent.writeObject(testObject);
			messageSent.writeString(testString);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "sendReceiveStreamMsgQueueTest");
			logger.log(Logger.Level.INFO, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.INFO, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("Did not receive message");
			}
			logger.log(Logger.Level.TRACE, "Check received message");
			if (messageReceived.readBoolean() == testBoolean) {
				logger.log(Logger.Level.TRACE, "Received correct boolean value");
			} else {
				logger.log(Logger.Level.INFO, "incorrect boolean value -- BAD");
				pass = false;
			}
			if (messageReceived.readByte() == testByte) {
				logger.log(Logger.Level.TRACE, "Received correct byte value");
			} else {
				logger.log(Logger.Level.INFO, "incorrect byte value -- BAD");
				pass = false;
			}
			if (messageReceived.readChar() == testChar) {
				logger.log(Logger.Level.TRACE, "Received correct char value");
			} else {
				logger.log(Logger.Level.INFO, "incorrect char value -- BAD");
				pass = false;
			}
			if (messageReceived.readInt() == testInt) {
				logger.log(Logger.Level.TRACE, "Received correct int value");
			} else {
				logger.log(Logger.Level.INFO, "incorrect int value -- BAD");
				pass = false;
			}
			if (messageReceived.readDouble() == ((Double) testObject).doubleValue()) {
				logger.log(Logger.Level.TRACE, "Received correct object");
			} else {
				logger.log(Logger.Level.INFO, "incorrect object -- BAD");
				pass = false;
			}
			if (messageReceived.readString().equals(testString)) {
				logger.log(Logger.Level.TRACE, "Received correct String");
			} else {
				logger.log(Logger.Level.INFO, "incorrect string -- BAD");
				pass = false;
			}
			if (pass == false) {
				logger.log(Logger.Level.INFO, "Test failed -- see above");
				throw new Exception();
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("sendReceiveStreamMsgQueueTest");
		}
	}

	/*
	 * @testName: sendReceiveTextMsgQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88; JMS:SPEC:77;
	 * 
	 * @test_Strategy: Send message with appropriate data Receive message and check
	 * data
	 */
	@Test
	public void sendReceiveTextMsgQueueTest() throws Exception {
		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			String text = "test";

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.INFO, "Creating 1 message");
			messageSent = new TextMessageTestImpl();
			messageSent.setText(text);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "sendReceiveTextMsgQueueTest");
			logger.log(Logger.Level.INFO, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.INFO, "Receiving message");
			messageReceived = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("Did not receive message");
			}
			if (messageReceived.getText().equals(text)) {
				logger.log(Logger.Level.INFO, "Received correct text");
			} else {
				throw new Exception("Did not receive correct message");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("sendReceiveTextMsgQueueTest");
		}
	}

	/*
	 * @testName: sendSetsJMSDestinationQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88; JMS:SPEC:246.1; JMS:SPEC:246;
	 * JMS:JAVADOC:365; JMS:JAVADOC:363;
	 * 
	 * @test_Strategy: Send message verify that JMSDestination was set
	 */
	@Test
	public void sendSetsJMSDestinationQueueTest() throws Exception {
		try {
			Message message;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			logger.log(Logger.Level.INFO, "Creating 1 message");
			message = new MessageTestImpl();
			message.setStringProperty("COM_SUN_JMS_TESTNAME", "sendSetsJMSDestinationQueueTest");

			// set header value
			message.setJMSDestination(null);
			logger.log(Logger.Level.INFO, "Sending message");
			tool.getDefaultQueueSender().send(message);

			// check again
			logger.log(Logger.Level.TRACE, "Check header value");
			if (!((Queue) message.getJMSDestination()).getQueueName().equals(tool.getDefaultQueue().getQueueName())) {
				throw new Exception("Header not set correctly");
			} else {
				logger.log(Logger.Level.TRACE, "Header set correctly");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("sendSetsJMSDestinationQueueTest");
		}
	}

	/*
	 * @testName: sendSetsJMSExpirationQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88; JMS:SPEC:246.3; JMS:SPEC:246;
	 * JMS:JAVADOC:381; JMS:JAVADOC:379;
	 * 
	 * @test_Strategy: Send message verify that JMSExpiration was set
	 */
	@Test
	public void sendSetsJMSExpirationQueueTest() throws Exception {
		try {
			Message message;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			logger.log(Logger.Level.INFO, "Creating 1 message");
			message = new MessageTestImpl();
			message.setStringProperty("COM_SUN_JMS_TESTNAME", "sendSetsJMSExpirationQueueTest");

			// set header value
			logger.log(Logger.Level.TRACE, "Set JMSExpiration to 9999");
			message.setJMSExpiration(9999);
			logger.log(Logger.Level.INFO, "Sending message");
			tool.getDefaultQueueSender().send(message);

			// check header
			long mode = message.getJMSExpiration();

			logger.log(Logger.Level.TRACE, "Check header value");
			if (mode == 9999) {
				logger.log(Logger.Level.TRACE, "JMSExpiration for message is " + mode);
				throw new Exception("Header not set correctly");
			} else {
				logger.log(Logger.Level.TRACE, "Header set correctly");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("sendSetsJMSExpirationQueueTest");
		}
	}

	/*
	 * @testName: sendSetsJMSPriorityQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88; JMS:SPEC:246.4; JMS:SPEC:246;
	 * JMS:JAVADOC:385; JMS:JAVADOC:383;
	 * 
	 * @test_Strategy: Send message verify that JMSPriority was set
	 */
	@Test
	public void sendSetsJMSPriorityQueueTest() throws Exception {
		try {
			Message message;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			logger.log(Logger.Level.INFO, "Creating 1 message");
			message = new MessageTestImpl();
			message.setStringProperty("COM_SUN_JMS_TESTNAME", "sendSetsJMSPriorityQueueTest");

			// set header value
			logger.log(Logger.Level.TRACE, "Set JMSPriority to 9999");
			message.setJMSPriority(9999);
			logger.log(Logger.Level.INFO, "Sending message");
			tool.getDefaultQueueSender().send(message);

			// check header value
			int mode = message.getJMSPriority();

			logger.log(Logger.Level.TRACE, "Check header value");
			if (mode == 9999) {
				logger.log(Logger.Level.TRACE, "JMSPriority for message is " + mode);
				throw new Exception("Header not set correctly");
			} else {
				logger.log(Logger.Level.TRACE, "Header set correctly: " + mode);
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("sendSetsJMSPriorityQueueTest");
		}
	}

	/*
	 * @testName: sendSetsJMSMessageIDQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88; JMS:SPEC:246.5; JMS:SPEC:246;
	 * JMS:JAVADOC:345; JMS:JAVADOC:343;
	 * 
	 * @test_Strategy: Send message verify that JMSMessageID was set
	 */
	@Test
	public void sendSetsJMSMessageIDQueueTest() throws Exception {
		try {
			Message message;
			String id0 = "foo";

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			logger.log(Logger.Level.INFO, "Creating 1 message");
			message = new MessageTestImpl();
			message.setStringProperty("COM_SUN_JMS_TESTNAME", "sendSetsJMSMessageIDQueueTest");

			// set header value
			logger.log(Logger.Level.TRACE, "Set JMSMessageID to \"" + id0 + "\"");
			message.setJMSMessageID(id0);
			logger.log(Logger.Level.INFO, "Sending message");
			tool.getDefaultQueueSender().send(message);

			// check header value
			String id1 = message.getJMSMessageID();

			logger.log(Logger.Level.TRACE, "Check header value");
			if (id1.equals(id0)) {
				logger.log(Logger.Level.TRACE, "JMSMessageID for message is " + id1);
				throw new Exception("Header not set correctly");
			} else {
				logger.log(Logger.Level.TRACE, "Header set correctly: " + id1);
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("sendSetsJMSMessageIDQueueTest");
		}
	}

	/*
	 * @testName: sendSetsJMSTimestampQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:84; JMS:SPEC:88; JMS:SPEC:246.6; JMS:SPEC:246;
	 * JMS:JAVADOC:349; JMS:JAVADOC:347;
	 * 
	 * @test_Strategy: Send message verify that JMSTimestamp was set
	 */
	@Test
	public void sendSetsJMSTimestampQueueTest() throws Exception {
		try {
			Message message;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			logger.log(Logger.Level.INFO, "Creating 1 message");
			message = new MessageTestImpl();
			message.setStringProperty("COM_SUN_JMS_TESTNAME", "sendSetsJMSTimestampQueueTest");

			// set header value
			logger.log(Logger.Level.TRACE, "Set JMSTimestamp to 9999");
			message.setJMSTimestamp(9999);
			logger.log(Logger.Level.INFO, "Sending message");
			tool.getDefaultQueueSender().send(message);

			// check header value
			long mode = message.getJMSTimestamp();

			logger.log(Logger.Level.TRACE, "Check header value");
			if (mode == 9999) {
				logger.log(Logger.Level.TRACE, "JMSTimestamp for message is " + mode);
				throw new Exception("Header not set correctly");
			} else {
				logger.log(Logger.Level.TRACE, "Header set correctly: " + mode);
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("sendSetsJMSTimestampQueueTest");
		}
	}

}
