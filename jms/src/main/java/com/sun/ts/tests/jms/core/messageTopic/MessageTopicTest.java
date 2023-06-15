/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jms.core.messageTopic;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.BytesMessage;
import jakarta.jms.MapMessage;
import jakarta.jms.MessageNotWriteableException;
import jakarta.jms.ObjectMessage;
import jakarta.jms.StreamMessage;
import jakarta.jms.TextMessage;

public class MessageTopicTest {
	private static final String testName = "com.sun.ts.tests.jms.core.messageTopic.MessageTopicTest";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(MessageTopicTest.class.getName());

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
				throw new Exception("'user' in must not be null");
			}
			if (password == null) {
				throw new Exception("'password' in must not be null");
			}
			if (mode == null) {
				throw new Exception("'platform.mode' in must not be null");
			}

		} catch (Exception e) {
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
			logger.log(Logger.Level.ERROR, "An error occurred while cleaning");
			throw new Exception("Cleanup failed!", e);
		}
	}

	/* Tests */

	/*
	 * @testName: msgClearBodyTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:71; JMS:SPEC:72; JMS:JAVADOC:431; JMS:JAVADOC:473;
	 * JMS:JAVADOC:449; JMS:SPEC:178; JMS:JAVADOC:291;
	 * 
	 * @test_Strategy: For each type of message, create and send a message Send and
	 * receive single Text, map, bytes, stream, and object message call clearBody,
	 * verify body is empty after clearBody. verify properties are not effected by
	 * clearBody. Write to the message again 3.11
	 */
	@Test
	public void msgClearBodyTopicTest() throws Exception {
		boolean pass = true;
		byte bValue = 127;
		byte bValue2 = 22;
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
			messageSentObjectMsg.setObject("Initial message");
			messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "msgClearBodyTopicTest");
			tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
			messageReceivedObjectMsg = (ObjectMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			try {
				logger.log(Logger.Level.TRACE, "Testing Object message");
				logger.log(Logger.Level.TRACE, "read 1st contents");
				logger.log(Logger.Level.TRACE, "  " + messageReceivedObjectMsg.getObject());
				logger.log(Logger.Level.TRACE, "Call to clearBody !!!!!!!!!!!!!!!");
				messageReceivedObjectMsg.clearBody();

				// message body should now be empty
				if (messageReceivedObjectMsg.getObject() == null) {
					logger.log(Logger.Level.TRACE, "Empty body after clearBody as expected: null");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: message body was not empty");
					pass = false;
				}

				// properties should not have been deleted by the clearBody method.
				if (messageSentObjectMsg.getStringProperty("COM_SUN_JMS_TESTNAME").equals("msgClearBodyTopicTest")) {
					logger.log(Logger.Level.TRACE, "Pass: Object properties read ok after clearBody called");
				} else {
					logger.log(Logger.Level.ERROR, "Fail: Object properties cleared after clearBody called");
					pass = false;
				}
				logger.log(Logger.Level.TRACE, "write 2nd contents");
				messageReceivedObjectMsg.setObject("new stuff here!!!!!!");
				logger.log(Logger.Level.TRACE, "read 2nd contents");
				if (messageReceivedObjectMsg.getObject().equals("new stuff here!!!!!!")) {
					logger.log(Logger.Level.TRACE, "Pass:");
				} else {
					logger.log(Logger.Level.ERROR, "Fail: ");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception: ", e);
				pass = false;
			}

			// send and receive map message to Topic
			logger.log(Logger.Level.TRACE, "Send MapMessage to Topic.");
			messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
			messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgClearBodyTopicTest");
			messageSentMapMessage.setString("aString", "Initial message");
			tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
			messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			try {
				logger.log(Logger.Level.TRACE, "Test for MapMessage ");
				logger.log(Logger.Level.TRACE, "read 1st contents");
				logger.log(Logger.Level.TRACE, "  " + messageReceivedMapMessage.getString("aString"));
				logger.log(Logger.Level.TRACE, "Call to clearBody !!!!!!!!!!!!!!!");
				messageReceivedMapMessage.clearBody();

				// message body should now be empty
				if (messageReceivedMapMessage.getString("aString") == null) {
					logger.log(Logger.Level.TRACE, "Empty body after clearBody as expected: null");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: message body was not empty");
					pass = false;
				}

				// properties should not have been deleted by the clearBody method.
				if (messageReceivedMapMessage.getStringProperty("COM_SUN_JMS_TESTNAME")
						.equals("msgClearBodyTopicTest")) {
					logger.log(Logger.Level.TRACE, "Pass: Map properties read ok after clearBody called");
				} else {
					logger.log(Logger.Level.ERROR, "Fail: Map properties cleared after clearBody called");
					pass = false;
				}
				logger.log(Logger.Level.TRACE, "write 2nd contents");
				messageReceivedMapMessage.setString("yes", "new stuff !!!!!");
				logger.log(Logger.Level.TRACE, "read 2nd contents");
				if (messageReceivedMapMessage.getString("yes").equals("new stuff !!!!!")) {
					logger.log(Logger.Level.TRACE, "PASS:");
				} else {
					logger.log(Logger.Level.ERROR, "FAIL:");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception: ", e);
				pass = false;
			}

			// send and receive bytes message to Topic
			logger.log(Logger.Level.TRACE, "Send BytesMessage to Topic.");
			messageSentBytesMessage = tool.getDefaultTopicSession().createBytesMessage();
			messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgClearBodyTopicTest");
			messageSentBytesMessage.writeByte(bValue);
			tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
			messageReceivedBytesMessage = (BytesMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			try {
				logger.log(Logger.Level.TRACE, "Test BytesMessage ");
				logger.log(Logger.Level.TRACE, "read 1st contents");
				logger.log(Logger.Level.TRACE, "  " + messageReceivedBytesMessage.readByte());
				logger.log(Logger.Level.TRACE, "Call to clearBody !!!!!!!!!!!!!!!");
				messageReceivedBytesMessage.clearBody();
				logger.log(Logger.Level.TRACE, "Bytes message body should now be empty and in writeonly mode");
				try {
					byte b = messageReceivedBytesMessage.readByte();

					logger.log(Logger.Level.ERROR, "Fail: MessageNotReadableException not thrown as expected");
					pass = false;
				} catch (jakarta.jms.MessageNotReadableException e) {
					logger.log(Logger.Level.TRACE, "Pass: MessageNotReadableException thrown as expected");
				} catch (Exception ee) {
					logger.log(Logger.Level.ERROR, "Error: Unexpected exception: ", ee);
					pass = false;
				}

				// properties should not have been deleted by the clearBody method.
				if (messageReceivedBytesMessage.getStringProperty("COM_SUN_JMS_TESTNAME")
						.equals("msgClearBodyTopicTest")) {
					logger.log(Logger.Level.TRACE, "Pass: Bytes msg properties read ok after clearBody called");
				} else {
					logger.log(Logger.Level.ERROR, "Fail: Bytes msg properties cleared after clearBody called");
					pass = false;
				}
				logger.log(Logger.Level.TRACE, "write 2nd contents");
				messageReceivedBytesMessage.writeByte(bValue2);
				logger.log(Logger.Level.TRACE, "read 2nd contents");
				messageReceivedBytesMessage.reset();
				if (messageReceivedBytesMessage.readByte() == bValue2) {
					logger.log(Logger.Level.TRACE, "Pass:");
				} else {
					logger.log(Logger.Level.ERROR, "Fail:");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception: ", e);
				pass = false;
			}

			// Send and receive a StreamMessage
			logger.log(Logger.Level.TRACE, "sending a Stream message");
			messageSentStreamMessage = tool.getDefaultTopicSession().createStreamMessage();
			messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgClearBodyTopicTest");
			messageSentStreamMessage.writeString("Testing...");
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
			messageReceivedStreamMessage = (StreamMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			try {
				logger.log(Logger.Level.TRACE, "Test StreamMessage ");
				logger.log(Logger.Level.TRACE, "read 1st contents");
				logger.log(Logger.Level.TRACE, "  " + messageReceivedStreamMessage.readString());
				logger.log(Logger.Level.TRACE, "Call to clearBody !!!!!!!!!!!!!!!");
				messageReceivedStreamMessage.clearBody();
				logger.log(Logger.Level.TRACE, "Stream message body should now be empty and in writeonly mode");
				try {
					String s = messageReceivedStreamMessage.readString();

					logger.log(Logger.Level.ERROR, "Fail: MessageNotReadableException should have been thrown");
					pass = false;
				} catch (jakarta.jms.MessageNotReadableException e) {
					logger.log(Logger.Level.TRACE, "Pass: MessageNotReadableException thrown as expected");
				} catch (Exception ee) {
					logger.log(Logger.Level.ERROR, "Error: Unexpected exception: ", ee);
					pass = false;
				}

				// properties should not have been deleted by the clearBody method.
				if (messageReceivedStreamMessage.getStringProperty("COM_SUN_JMS_TESTNAME")
						.equals("msgClearBodyTopicTest")) {
					logger.log(Logger.Level.TRACE, "Pass: Stream msg properties read ok after clearBody called");
				} else {
					logger.log(Logger.Level.ERROR, "Fail: Stream msg properties cleared after clearBody called");
					pass = false;
				}
				logger.log(Logger.Level.TRACE, "write 2nd contents");
				messageReceivedStreamMessage.writeString("new data");
				logger.log(Logger.Level.TRACE, "read 2nd contents");
				messageReceivedStreamMessage.reset();
				if (messageReceivedStreamMessage.readString().equals("new data")) {
					logger.log(Logger.Level.TRACE, "Pass:");
				} else {
					logger.log(Logger.Level.ERROR, "Fail:");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: ", e);
				pass = false;
			}

			// Text Message
			messageSent = tool.getDefaultTopicSession().createTextMessage();
			messageSent.setText("sending a Text message");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgClearBodyTopicTest");
			logger.log(Logger.Level.TRACE, "sending a Text message");
			tool.getDefaultTopicPublisher().publish(messageSent);
			messageReceived = (TextMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			try {
				logger.log(Logger.Level.TRACE, "Test TextMessage ");
				logger.log(Logger.Level.TRACE, "read 1st contents");
				logger.log(Logger.Level.TRACE, "  " + messageReceived.getText());
				logger.log(Logger.Level.TRACE, "Call to clearBody !!!!!!!!!!!!!!!");
				messageReceived.clearBody();

				// message body should now be empty
				if (messageReceived.getText() == null) {
					logger.log(Logger.Level.TRACE, "Empty body after clearBody as expected: null");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: message body was not empty");
					pass = false;
				}

				// properties should not have been deleted by the clearBody method.
				if (messageReceived.getStringProperty("COM_SUN_JMS_TESTNAME").equals("msgClearBodyTopicTest")) {
					logger.log(Logger.Level.TRACE, "Pass: Text properties read ok after clearBody called");
				} else {
					logger.log(Logger.Level.ERROR, "Fail: Text properties cleared after clearBody called");
					pass = false;
				}
				logger.log(Logger.Level.TRACE, "write and read 2nd contents");
				messageReceived.setText("new data");
				if (messageReceived.getText().equals("new data")) {
					logger.log(Logger.Level.TRACE, "Pass:");
				} else {
					logger.log(Logger.Level.ERROR, "Fail:");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: ", e);
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: clearBody test failure");
			}
		} catch (Exception e) {
			throw new Exception("msgClearBodyTopicTest", e);
		}
	}

	/*
	 * @testName: msgResetTopicTest
	 * 
	 * @assertion_ids: JMS:JAVADOC:174; JMS:JAVADOC:584;
	 *
	 * @test_Strategy: create a stream message and a byte message. write to the
	 * message body, call the reset method, try to write to the body expect a
	 * MessageNotWriteableException to be thrown.
	 */
	@Test
	public void msgResetTopicTest() throws Exception {
		boolean pass = true;
		int nInt = 1000;

		try {
			StreamMessage messageSentStreamMessage = null;
			BytesMessage messageSentBytesMessage = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
			tool.getDefaultTopicConnection().start();

			// StreamMessage
			try {
				logger.log(Logger.Level.TRACE, "creating a Stream message");
				messageSentStreamMessage = tool.getDefaultTopicSession().createStreamMessage();
				messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgResetTopicTest");

				// write to the message
				messageSentStreamMessage.writeString("Testing...");
				logger.log(Logger.Level.INFO, "reset stream message -  now  should be in readonly mode");
				messageSentStreamMessage.reset();
				messageSentStreamMessage.writeString("new data");
				logger.log(Logger.Level.ERROR, "Fail: message did not throw MessageNotWriteable exception as expected");
				pass = false;
			} catch (MessageNotWriteableException nw) {
				logger.log(Logger.Level.TRACE, "Pass: MessageNotWriteable thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: ", e);
				pass = false;
			}

			// BytesMessage
			try {
				logger.log(Logger.Level.TRACE, "creating a Byte message");
				messageSentBytesMessage = tool.getDefaultTopicSession().createBytesMessage();
				messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgResetTopicTest");

				// write to the message
				messageSentBytesMessage.writeInt(nInt);
				logger.log(Logger.Level.INFO, "reset Byte message -  now  should be in readonly mode");
				messageSentBytesMessage.reset();
				messageSentBytesMessage.writeInt(nInt);
				logger.log(Logger.Level.ERROR, "Fail: message did not throw MessageNotWriteable exception as expected");
				pass = false;
			} catch (MessageNotWriteableException nw) {
				logger.log(Logger.Level.TRACE, "Pass: MessageNotWriteable thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception: ", e);
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: msgResetTopicTest test failure");
			}
		} catch (Exception e) {
			throw new Exception("msgResetTopicTest", e);
		}
	}

	/*
	 * @testName: readNullCharNotValidTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:79; JMS:JAVADOC:134; JMS:JAVADOC:439;
	 * 
	 * @test_Strategy: Write a null string to a MapMessage and then a StreamMessage.
	 * Attempt to read the null value as a char. Verify that a NullPointerException
	 * is thrown.
	 * 
	 */
	@Test
	public void readNullCharNotValidTopicTest() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			MapMessage mapSent = null;
			MapMessage mapReceived = null;
			char c;
			boolean pass = true;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
			tool.getDefaultTopicConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultTopicSession().createStreamMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "readNullCharNotValidTopicTest");

			// -----------------------------------------------------------------------------
			// Stream Message
			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE,
					"Write a null string to the stream message object with StreamMessage.writeString");
			messageSent.writeString(null);
			logger.log(Logger.Level.TRACE, " Send the message");
			tool.getDefaultTopicPublisher().publish(messageSent);
			messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "Use readChar to read a null  ");
			try {
				messageReceived.readChar();
				logger.log(Logger.Level.ERROR, "Fail: NullPointerException was not thrown");
				pass = false;
			} catch (java.lang.NullPointerException e) {
				logger.log(Logger.Level.TRACE, "Pass: NullPointerException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: Unexpected exception: ", e);
				pass = false;
			}

			// -----------------------------------------------------------------------------
			// Map Message
			// -----------------------------------------------------------------------------
			mapSent = tool.getDefaultTopicSession().createMapMessage();
			mapSent.setStringProperty("COM_SUN_JMS_TESTNAME", "readNullCharNotValidTopicTest");
			logger.log(Logger.Level.TRACE, "Write a null string to the map message object with mapMessage.setString");
			mapSent.setString("WriteANull", null);
			logger.log(Logger.Level.TRACE, " Send the message");
			tool.getDefaultTopicPublisher().publish(mapSent);
			mapReceived = (MapMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.TRACE, "Use readChar to read a null  ");
			try {
				mapReceived.getChar("WriteANull");
				logger.log(Logger.Level.ERROR, "Fail: NullPointerException was not thrown");
				pass = false;
			} catch (java.lang.NullPointerException e) {
				logger.log(Logger.Level.TRACE, "Pass: NullPointerException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: Unexpected exception: ", e);
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			throw new Exception("readNullCharNotValidTopicTest", e);
		}
	}

	/*
	 * @testName: messageTIllegalarg
	 *
	 * @assertion_ids: JMS:JAVADOC:775; JMS:JAVADOC:777; JMS:JAVADOC:779;
	 * JMS:JAVADOC:781; JMS:JAVADOC:783; JMS:JAVADOC:785; JMS:JAVADOC:787;
	 * JMS:JAVADOC:789; JMS:JAVADOC:791;
	 *
	 * @test_Strategy: Create a TextMessage. Write to the message using each type of
	 * setProperty method and as an object with null String as name. Verify that
	 * IllegalArgumentException thrown.
	 */
	@Test
	public void messageTIllegalarg() throws Exception {
		try {
			TextMessage messageSent = null;
			boolean pass = true;
			byte bValue = 127;
			short sValue = 32767;
			char cValue = '\uFFFF';
			int iValue = 2147483647;
			long lValue = 9223372036854775807L;
			float fValue = 0.0f;
			double dValue = -0.0;
			String ssValue = "abc";

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
			tool.getDefaultTopicConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultTopicSession().createTextMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "messageTIllegalarg");

			// -----------------------------------------------------------------------------

			logger.log(Logger.Level.INFO, "Writing a boolean property ... ");
			try {
				messageSent.setBooleanProperty("", pass);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setBooleanProperty");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setBooleanProperty");
			}

			logger.log(Logger.Level.INFO, "Writing a byte Property ... ");
			try {
				messageSent.setByteProperty("", bValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setByteProperty");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setByteProperty");
			}

			logger.log(Logger.Level.INFO, "Writing a short Property... ");
			try {
				messageSent.setShortProperty("", sValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setShortProperty");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setShortProperty");
			}

			logger.log(Logger.Level.INFO, "Writing a int Property ... ");
			try {
				messageSent.setIntProperty("", iValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setIntProperty");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setIntProperty");
			}

			logger.log(Logger.Level.INFO, "Writing a long Property ... ");
			try {
				messageSent.setLongProperty("", lValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setLongProperty");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setLongProperty");
			}

			logger.log(Logger.Level.INFO, "Writing a float Property ... ");
			try {
				messageSent.setFloatProperty("", fValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setFloatProperty");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setFloatProperty");
			}

			logger.log(Logger.Level.INFO, "Writing a double Property ... ");
			try {
				messageSent.setDoubleProperty("", dValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setDoubleProperty");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setDoubleProperty");
			}

			logger.log(Logger.Level.INFO, "Writing a string Property ... ");
			try {
				messageSent.setStringProperty("", ssValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setStringProperty");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setStringProperty");
			}

			logger.log(Logger.Level.INFO, "Writing a object Property ... ");
			try {
				messageSent.setObjectProperty("", new Integer(iValue));
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setObjectProperty");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setObjectProperty");
			}
		} catch (Exception e) {
			throw new Exception("messageTIllegalarg", e);
		}
	}

}
