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
package com.sun.ts.tests.jms.core.bytesMsgTopic;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.BytesMessage;
import jakarta.jms.MessageNotWriteableException;

public class BytesMsgTopicTestsIT {
	private static final String testName = "com.sun.ts.tests.jms.core.bytesMsgTopic.BytesMsgTopicTestsIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(BytesMsgTopicTestsIT.class.getName());

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
				throw new Exception("'user' is null");
			}
			if (password == null) {
				throw new Exception("'password' is null");
			}
			if (mode == null) {
				throw new Exception("'platform.mode' is null");
			}

			// get ready for new test
			logger.log(Logger.Level.INFO, "Getting Administrator and deleting any leftover destinations.");
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
	 * @testName: bytesMsgNullStreamTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:86.1; JMS:JAVADOC:714;
	 * 
	 * @test_Strategy: create a byte message. Use writeObject to write a null.
	 * verify a java.lang.NullPointerException is thrown.
	 */
	@Test
	public void bytesMsgNullStreamTopicTest() throws Exception {
		boolean pass = true;
		int nInt = 1000;

		try {
			BytesMessage messageSentBytesMessage = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
			tool.getDefaultTopicConnection().start();

			// BytesMessage
			try {
				logger.log(Logger.Level.INFO,
						"Writing a null stream to byte message should throw a NullPointerException");
				messageSentBytesMessage = tool.getDefaultTopicSession().createBytesMessage();
				messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "bytesMsgNullStreamTopicTest");

				// write a null to the message
				messageSentBytesMessage.writeObject(null);
				logger.log(Logger.Level.INFO, "Fail: message did not throw NullPointerException exception as expected");
				pass = false;
			} catch (java.lang.NullPointerException np) {
				logger.log(Logger.Level.TRACE, "Pass: NullPointerException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.INFO, "Error: " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: bytesMsgNullStreamTopicTest test failure");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("bytesMsgNullStreamTopicTest");
		}
	}

	/*
	 * @testName: bytesMessageTopicTestsFullMsg
	 * 
	 * @assertion_ids: JMS:JAVADOC:560; JMS:JAVADOC:562; JMS:JAVADOC:564;
	 * JMS:JAVADOC:566; JMS:JAVADOC:568; JMS:JAVADOC:570; JMS:JAVADOC:572;
	 * JMS:JAVADOC:574; JMS:JAVADOC:576; JMS:JAVADOC:578; JMS:JAVADOC:580;
	 * JMS:JAVADOC:582; JMS:JAVADOC:534; JMS:JAVADOC:536; JMS:JAVADOC:540;
	 * JMS:JAVADOC:544; JMS:JAVADOC:546; JMS:JAVADOC:548; JMS:JAVADOC:550;
	 * JMS:JAVADOC:552; JMS:JAVADOC:554; JMS:JAVADOC:556; JMS:JAVADOC:558;
	 * 
	 * @test_Strategy: Create a BytesMessage -. write to the message using each type
	 * of method and as an object. Send the message. Verify the data received was as
	 * sent.
	 * 
	 */
	@Test
	public void bytesMessageTopicTestsFullMsg() throws Exception {
		try {
			BytesMessage messageSent = null;
			BytesMessage messageReceived = null;
			boolean pass = true;
			boolean booleanValue = false;
			byte byteValue = 127;
			byte[] bytesValue = { 127, -127, 1, 0 };
			byte[] bytesValueRecvd = { 0, 0, 0, 0 };
			char charValue = 'Z';
			double doubleValue = 6.02e23;
			float floatValue = 6.02e23f;
			int intValue = 2147483647;
			long longValue = 9223372036854775807L;
			Integer nInteger = new Integer(-2147483648);
			short shortValue = -32768;
			String utfValue = "what";

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
			tool.getDefaultTopicConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultTopicSession().createBytesMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "bytesMessageTopicTestsFullMsg");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.INFO, "Writing one of each primitive type to the message");

			// -----------------------------------------------------------------------------
			messageSent.writeBoolean(booleanValue);
			messageSent.writeByte(byteValue);
			messageSent.writeChar(charValue);
			messageSent.writeDouble(doubleValue);
			messageSent.writeFloat(floatValue);
			messageSent.writeInt(intValue);
			messageSent.writeLong(longValue);
			messageSent.writeObject(nInteger);
			messageSent.writeShort(shortValue);
			messageSent.writeUTF(utfValue);
			messageSent.writeBytes(bytesValue);
			messageSent.writeBytes(bytesValue, 0, 1);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultTopicPublisher().publish(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (BytesMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			try {
				if (messageReceived.readBoolean() == booleanValue) {
					logger.log(Logger.Level.TRACE, "Pass: boolean returned ok");
				} else {
					logger.log(Logger.Level.INFO, "Fail: boolean not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.INFO, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readByte() == byteValue) {
					logger.log(Logger.Level.TRACE, "Pass: Byte returned ok");
				} else {
					logger.log(Logger.Level.INFO, "Fail: Byte not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.INFO, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readChar() == charValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct char");
				} else {
					logger.log(Logger.Level.INFO, "Fail: char not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.INFO, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readDouble() == doubleValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct double");
				} else {
					logger.log(Logger.Level.INFO, "Fail: double not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.INFO, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readFloat() == floatValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct float");
				} else {
					logger.log(Logger.Level.INFO, "Fail: float not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.INFO, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readInt() == intValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct int");
				} else {
					logger.log(Logger.Level.INFO, "Fail: int not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.INFO, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readLong() == longValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct long");
				} else {
					logger.log(Logger.Level.INFO, "Fail: long not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.INFO, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readInt() == nInteger.intValue()) {
					logger.log(Logger.Level.TRACE, "Pass: correct Integer returned");
				} else {
					logger.log(Logger.Level.INFO, "Fail: Integer not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.INFO, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readShort() == shortValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct short");
				} else {
					logger.log(Logger.Level.INFO, "Fail: short not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.INFO, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readUTF().equals(utfValue)) {
					logger.log(Logger.Level.TRACE, "Pass: correct UTF");
				} else {
					logger.log(Logger.Level.INFO, "Fail: UTF not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.INFO, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				int nCount = messageReceived.readBytes(bytesValueRecvd);

				for (int i = 0; i < nCount; i++) {
					if (bytesValueRecvd[i] != bytesValue[i]) {
						logger.log(Logger.Level.INFO, "Fail: bytes value incorrect");
						pass = false;
					} else {
						logger.log(Logger.Level.TRACE, "Pass: byte value " + i + " ok");
					}
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.INFO, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				int nCount = messageReceived.readBytes(bytesValueRecvd);

				logger.log(Logger.Level.TRACE, "count returned " + nCount);
				if (bytesValueRecvd[0] != bytesValue[0]) {
					logger.log(Logger.Level.INFO, "Fail: bytes value incorrect");
					pass = false;
				} else {
					logger.log(Logger.Level.TRACE, "Pass: byte value ok");
				}
				if (nCount == 1) {
					logger.log(Logger.Level.TRACE, "Pass: correct count");
				} else {
					logger.log(Logger.Level.INFO, "Fail: count not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.INFO, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("bytesMessageTopicTestsFullMsg", e);
		}
	}

	/*
	 * @testName: bytesMessageTNotWriteable
	 *
	 * @assertion_ids: JMS:SPEC:73; JMS:JAVADOC:701; JMS:JAVADOC:702;
	 * JMS:JAVADOC:703; JMS:JAVADOC:704; JMS:JAVADOC:705; JMS:JAVADOC:706;
	 * JMS:JAVADOC:707; JMS:JAVADOC:708; JMS:JAVADOC:709; JMS:JAVADOC:710;
	 * JMS:JAVADOC:711; JMS:JAVADOC:713;
	 *
	 * @test_Strategy: Create a BytesMessage - send it to a Topic. Write to the
	 * received message using each type of method and as an object. Verify
	 * MessageNotWriteableException thrown
	 */
	@Test
	public void bytesMessageTNotWriteable() throws Exception {
		try {
			BytesMessage messageSent = null;
			BytesMessage messageReceived = null;
			boolean pass = true;
			byte bValue = 127;
			byte[] bbValue = { 127, -127, 1, 0 };
			char cValue = 'Z';
			double dValue = 6.02e23;
			float fValue = 6.02e23f;
			int iValue = 2147483647;
			long lValue = 9223372036854775807L;
			short sValue = -32768;
			String ssValue = "what";

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
			tool.getDefaultTopicConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultTopicSession().createBytesMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "bytesMessageTNotWriteable");

			messageSent.writeBytes(bbValue);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultTopicPublisher().publish(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (BytesMessage) tool.getDefaultTopicSubscriber().receive(timeout);

			logger.log(Logger.Level.INFO, "Writing a boolean ... ");
			try {
				messageReceived.writeBoolean(pass);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeBoolean");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeBoolean");
			}

			logger.log(Logger.Level.INFO, "Writing a byte ... ");
			try {
				messageReceived.writeByte(bValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeByte");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeByte");
			}

			logger.log(Logger.Level.INFO, "Writing a short ... ");
			try {
				messageReceived.writeShort(sValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeShort");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeShort");
			}

			logger.log(Logger.Level.INFO, "Writing a char ... ");
			try {
				messageReceived.writeChar(cValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeChar");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeChar");
			}

			logger.log(Logger.Level.INFO, "Writing a int ... ");
			try {
				messageReceived.writeInt(iValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeInt");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeInt");
			}

			logger.log(Logger.Level.INFO, "Writing a long ... ");
			try {
				messageReceived.writeLong(lValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeLong");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeLong");
			}

			logger.log(Logger.Level.INFO, "Writing a float ... ");
			try {
				messageReceived.writeFloat(fValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeFloat");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeFloat");
			}

			logger.log(Logger.Level.INFO, "Writing a double ... ");
			try {
				messageReceived.writeDouble(dValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeDouble");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeDouble");
			}

			logger.log(Logger.Level.INFO, "Writing a bytes... ");
			try {
				messageReceived.writeBytes(bbValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeBytes");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeBytes");
			}

			logger.log(Logger.Level.INFO, "Writing a bytes... ");
			try {
				messageReceived.writeBytes(bbValue, 0, 2);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeBytes");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeBytes");
			}

			logger.log(Logger.Level.INFO, "Writing a UTF ... ");
			try {
				messageReceived.writeUTF(ssValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeUTF");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeUTF");
			}

			logger.log(Logger.Level.INFO, "Writing a object ... ");
			try {
				messageReceived.writeObject(new Integer(iValue));
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeObject");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeObject");
			}

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("BytesMessageQueueNotWriteable", e);
		}
	}
}
