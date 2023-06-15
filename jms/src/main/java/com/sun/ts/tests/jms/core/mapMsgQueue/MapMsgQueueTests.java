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
package com.sun.ts.tests.jms.core.mapMsgQueue;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.MapMessage;
import jakarta.jms.MessageFormatException;
import jakarta.jms.MessageNotWriteableException;

public class MapMsgQueueTests {
	private static final String testName = "com.sun.ts.tests.jms.core.mapMsgQueue.MapMsgQueueTests";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(MapMsgQueueTests.class.getName());

	// JMS objects
	private transient JmsTool tool = null;

	// Harness req's
	private Properties props = null;

	// properties read
	long timeout;

	String password;

	String mode;

	String user;

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
				throw new Exception("'user' in must not be null");
			}
			if (password == null) {
				throw new Exception("'password' in must not be null");
			}
			if (mode == null) {
				throw new Exception("'platform.mode' in must not be null");
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
			logger.log(Logger.Level.ERROR, "An error occurred while cleaning: ", e);
			throw new Exception("Cleanup failed!", e);
		}
	}

	/* Tests */

	/*
	 * @testName: mapMessageFullMsgQTest
	 *
	 * @assertion_ids: JMS:SPEC:74; JMS:SPEC:80; JMS:JAVADOC:211; JMS:JAVADOC:457;
	 * JMS:JAVADOC:459; JMS:JAVADOC:475; JMS:JAVADOC:477; JMS:JAVADOC:479;
	 * JMS:JAVADOC:461; JMS:JAVADOC:463; JMS:JAVADOC:465; JMS:JAVADOC:467;
	 * JMS:JAVADOC:469; JMS:JAVADOC:471; JMS:JAVADOC:473; JMS:JAVADOC:433;
	 * JMS:JAVADOC:435; JMS:JAVADOC:437; JMS:JAVADOC:439; JMS:JAVADOC:441;
	 * JMS:JAVADOC:443; JMS:JAVADOC:445; JMS:JAVADOC:447; JMS:JAVADOC:449;
	 * JMS:JAVADOC:451; JMS:JAVADOC:453; JMS:JAVADOC:455; JMS:JAVADOC:481;
	 *
	 * @test_Strategy: Create a MapMessage write to the message using each type of
	 * methods: setBoolean/Byte/Bytes/Bytes/Char/Double/Float and
	 * setInt/Long/Object/Short/String/String. Send the message. Verify on receive
	 * tha all data was received correctly using: 1. getMapNames that all fields are
	 * intact; 2. ItemExists that all fields are intact; 3.
	 * getBoolean/Byte/Bytes/Bytes/Char/Double/Float and
	 * getInt/Long/Object/Short/String/String that that all fields contain correct
	 * values; 4. an unset field is treated as null field.
	 */
	@Test
	public void mapMessageFullMsgQTest() throws Exception {
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
		short shortValue = 32767;
		String stringValue = "Map Message Test";
		Integer integerValue = Integer.valueOf(100);
		String initial = "spring is here!";
		Enumeration list;

		try {
			MapMessage messageSentMapMessage = null;
			MapMessage messageReceivedMapMessage = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();

			// send and receive map message to Queue
			logger.log(Logger.Level.TRACE, "Send MapMessage to Queue.");
			messageSentMapMessage = tool.getDefaultQueueSession().createMapMessage();
			messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "mapMessageFullMsgQTest");
			messageSentMapMessage.setBoolean("booleanValue", booleanValue);
			messageSentMapMessage.setByte("byteValue", byteValue);
			messageSentMapMessage.setBytes("bytesValue", bytesValue);
			messageSentMapMessage.setBytes("bytesValue2", bytesValue, 0, 1);
			messageSentMapMessage.setChar("charValue", charValue);
			messageSentMapMessage.setDouble("doubleValue", doubleValue);
			messageSentMapMessage.setFloat("floatValue", floatValue);
			messageSentMapMessage.setInt("intValue", intValue);
			messageSentMapMessage.setLong("longValue", longValue);
			messageSentMapMessage.setObject("integerValue", integerValue);
			messageSentMapMessage.setShort("shortValue", shortValue);
			messageSentMapMessage.setString("stringValue", stringValue);
			messageSentMapMessage.setString("nullTest", null);
			tool.getDefaultQueueSender().send(messageSentMapMessage);
			messageReceivedMapMessage = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);

			list = messageReceivedMapMessage.getMapNames();
			String name = null;
			int it = 0;
			int j = 0;
			while (list.hasMoreElements()) {
				name = (String) list.nextElement();
				logger.log(Logger.Level.TRACE, "Object with name: " + name);
				it++;
				if (name.equals("booleanValue"))
					j++;
				else if (name.equals("byteValue"))
					j++;
				else if (name.equals("bytesValue"))
					j++;
				else if (name.equals("bytesValue2"))
					j++;
				else if (name.equals("charValue"))
					j++;
				else if (name.equals("doubleValue"))
					j++;
				else if (name.equals("floatValue"))
					j++;
				else if (name.equals("intValue"))
					j++;
				else if (name.equals("longValue"))
					j++;
				else if (name.equals("integerValue"))
					j++;
				else if (name.equals("shortValue"))
					j++;
				else if (name.equals("stringValue"))
					j++;
				else if (name.equals("nullTest"))
					j++;
				else
					logger.log(Logger.Level.TRACE, "Object not created by the test with name: " + name);
			}

			if ((it < 13) || (j < 13))
				logger.log(Logger.Level.ERROR, "Incorrect number of of Objects." + " Total number = " + it
						+ " Total created by the test = " + j);

			try {
				if (messageReceivedMapMessage.itemExists("booleanValue")) {
					if (messageReceivedMapMessage.getBoolean("booleanValue") != booleanValue) {
						logger.log(Logger.Level.ERROR, "Error: Invalid boolean returned="
								+ messageReceivedMapMessage.getBoolean("booleanValue"));
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: No Object with name booleanValue exists");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: unexpected exception was returned: ", e);
				pass = false;
			}

			try {
				if (messageReceivedMapMessage.itemExists("byteValue")) {
					if (messageReceivedMapMessage.getByte("byteValue") != byteValue) {
						logger.log(Logger.Level.ERROR, "Fail: invalid byte returned");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: No Object with name byteValue exists");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: unexpected exception was returned: ", e);
				pass = false;
			}

			try {
				if (messageReceivedMapMessage.itemExists("bytesValue")) {
					byte[] b = messageReceivedMapMessage.getBytes("bytesValue");

					for (int i = 0; i < b.length; i++) {
						if (b[i] != bytesValue[i]) {
							logger.log(Logger.Level.ERROR, "Fail: byte array " + i + " not valid");
							pass = false;
						}
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: No Object with name bytesValue exists");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: unexpected exception: ", e);
				pass = false;
			}

			try {
				if (messageReceivedMapMessage.itemExists("bytesValue2")) {
					byte[] b = messageReceivedMapMessage.getBytes("bytesValue2");
					if (b[0] != bytesValue[0]) {
						logger.log(Logger.Level.ERROR, "Fail: byte array not valid");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: No Object with name bytesValue2 exists");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: unexpected exception: ", e);
				pass = false;
			}
			try {
				if (messageReceivedMapMessage.itemExists("charValue")) {
					if (messageReceivedMapMessage.getChar("charValue") != charValue) {
						logger.log(Logger.Level.ERROR, "Fail: invalid char returned");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: No Object with name charValue exists");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: unexpected exception: ", e);
				pass = false;
			}

			try {
				if (messageReceivedMapMessage.itemExists("doubleValue")) {
					if (messageReceivedMapMessage.getDouble("doubleValue") != doubleValue) {
						logger.log(Logger.Level.ERROR, "Fail: invalid double returned");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: No Object with name doubleValue exists");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: unexpected exception: ", e);
				pass = false;
			}

			try {
				if (messageReceivedMapMessage.itemExists("floatValue")) {
					if (messageReceivedMapMessage.getFloat("floatValue") != floatValue) {
						logger.log(Logger.Level.ERROR, "Fail: invalid float returned");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: No Object with name floatValue exists");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: unexpected exception", e);
				pass = false;
			}
			try {
				if (messageReceivedMapMessage.itemExists("intValue")) {
					if (messageReceivedMapMessage.getInt("intValue") != intValue) {
						logger.log(Logger.Level.ERROR, "Fail: invalid int returned");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: No Object with name intValue exists");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: unexpected exception", e);
				pass = false;
			}

			try {
				if (messageReceivedMapMessage.itemExists("longValue")) {
					if (messageReceivedMapMessage.getLong("longValue") != longValue) {
						logger.log(Logger.Level.ERROR, "Fail: invalid long returned");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: No Object with name longValue exists");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: unexpected exception", e);
				pass = false;
			}

			try {
				if (messageReceivedMapMessage.itemExists("integerValue")) {
					if (!messageReceivedMapMessage.getObject("integerValue").toString()
							.equals(integerValue.toString())) {
						logger.log(Logger.Level.ERROR, "Fail: invalid object returned");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: No Object with name integerValue exists");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: unexpected exception", e);
				pass = false;
			}

			try {
				if (messageReceivedMapMessage.itemExists("shortValue")) {
					if (messageReceivedMapMessage.getShort("shortValue") != shortValue) {
						logger.log(Logger.Level.ERROR, "Fail: invalid short returned");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: No Object with name shortValue exists");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: unexpected exception", e);
				pass = false;
			}
			try {
				if (messageReceivedMapMessage.itemExists("stringValue")) {
					if (!messageReceivedMapMessage.getString("stringValue").equals(stringValue)) {
						logger.log(Logger.Level.ERROR, "Fail: invalid string returned");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: No Object with name stringValue exists");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: unexpected exception", e);
				pass = false;
			}

			try {
				if (messageReceivedMapMessage.itemExists("nullTest")) {
					if (messageReceivedMapMessage.getString("nullTest") != null) {
						logger.log(Logger.Level.ERROR, "Fail:  null not returned from getString");
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Error: No Object with name nullTest exists");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: unexpected exception", e);
				pass = false;
			}

			try {
				if (messageReceivedMapMessage.itemExists("nullTestxyz")) {
					logger.log(Logger.Level.ERROR, "Fail:  field with name nullTestxyz is unset");
					pass = false;
				} else if (messageReceivedMapMessage.getObject("nullTestxyz") != null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Error: field with name nullTestxyz  should be null");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: unexpected exception", e);
				pass = false;
			}

			if (!pass) {
				throw new Exception("Error: mapMessageFullMsgQTest test failure");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("mapMessageFullMsgQTest");
		}
	}

	/*
	 * @testName: MapMessageConversionQTestsBoolean
	 * 
	 * @assertion_ids: JMS:SPEC:75.1; JMS:SPEC:75.2; JMS:JAVADOC:457;
	 * JMS:JAVADOC:433; JMS:JAVADOC:449; JMS:JAVADOC:796; JMS:JAVADOC:797;
	 * JMS:JAVADOC:798; JMS:JAVADOC:799; JMS:JAVADOC:800; JMS:JAVADOC:801;
	 * JMS:JAVADOC:802; JMS:JAVADOC:804;
	 * 
	 * @test_Strategy: Create a MapMessage -. use MapMessage method setBoolean to
	 * write a boolean to the message. Verify the proper conversion support as in
	 * 3.11.3
	 */
	@Test
	public void MapMessageConversionQTestsBoolean() throws Exception {
		try {
			MapMessage messageSent = null;
			MapMessage messageReceived = null;
			boolean booleanValue = true;
			boolean pass = true;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createMapMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "MapMessageConversionQTestsBoolean");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for boolean primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.setBoolean("booleanValue", booleanValue);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "Msg recvd: " + messageReceived.getStringProperty("COM_SUN_JMS_TESTNAME"));

			// now test conversions for boolean
			// -----------------------------------------------
			// boolean to boolean - valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBoolean to read a boolean");
			try {
				if (messageReceived.getBoolean("booleanValue") == booleanValue) {
					logger.log(Logger.Level.TRACE, "Pass: boolean to boolean - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// boolean to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readString to read a boolean");
			try {
				String tmp = messageReceived.getString("booleanValue");
				if (Boolean.valueOf(booleanValue).toString().equals(tmp)) {
					logger.log(Logger.Level.TRACE, "Pass: boolean to string - valid");
				} else {
					logger.log(Logger.Level.ERROR, "Fail: wrong value returned=" + tmp + ".");
					logger.log(Logger.Level.ERROR, "Expecting " + Boolean.valueOf(booleanValue) + ".");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// boolean to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBytes[] to read a boolean - expect MessageFormatException");
			int nCount = 0;

			try {
				byte[] b = messageReceived.getBytes("booleanValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Count returned from readBytes is : " + nCount);

			// -----------------------------------------------
			// boolean to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readByte to read a boolean - expect MessageFormatException");
			try {
				byte b = messageReceived.getByte("booleanValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// boolean to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readShort to read a boolean - expect MessageFormatException");
			try {
				short s = messageReceived.getShort("booleanValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// boolean to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readChar to read a boolean - expect MessageFormatException");
			try {
				char c = messageReceived.getChar("booleanValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// boolean to int invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readInt to read a boolean - expect MessageFormatException");
			try {
				int i = messageReceived.getInt("booleanValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// boolean to long invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readLong to read a boolean - expect MessageFormatException");
			try {
				long l = messageReceived.getLong("booleanValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// boolean to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readFloat to read a boolean - expect MessageFormatException");
			try {
				float f = messageReceived.getFloat("booleanValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// boolean to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readDouble to read a boolean - expect MessageFormatException");
			try {
				double d = messageReceived.getDouble("booleanValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("MapMessageConversionQTestsBoolean", e);
		}
	}

	/*
	 * @testName: MapMessageConversionQTestsByte
	 * 
	 * @assertion_ids: JMS:SPEC:75.3; JMS:SPEC:75.4; JMS:JAVADOC:459;
	 * JMS:JAVADOC:435; JMS:JAVADOC:437; JMS:JAVADOC:441; JMS:JAVADOC:443;
	 * JMS:JAVADOC:449; JMS:JAVADOC:795; JMS:JAVADOC:798; JMS:JAVADOC:801;
	 * JMS:JAVADOC:802; JMS:JAVADOC:804;
	 * 
	 * @test_Strategy: Create a MapMessage -. use MapMessage method setByte to write
	 * a byte. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void MapMessageConversionQTestsByte() throws Exception {
		MapMessage messageSent = null;
		MapMessage messageReceived = null;
		byte byteValue = 127;
		boolean pass = true;

		try {

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createMapMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "MapMessageConversionQTestsByte");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.setByte("byteValue", byteValue);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "Msg recvd: " + messageReceived.getStringProperty("COM_SUN_JMS_TESTNAME"));

			// now test conversions for byte
			// -----------------------------------------------
			// byte to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getBoolean to read a byte - this is not valid");
			try {
				boolean b = messageReceived.getBoolean("byteValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// byte to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getString to read a byte");
			try {
				if (messageReceived.getString("byteValue").equals(Byte.toString(byteValue))) {
					logger.log(Logger.Level.TRACE, "Pass: byte to string - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// byte to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getBytes[] to read a byte - expect MessageFormatException");
			int nCount = 0;

			try {
				byte[] b = messageReceived.getBytes("byteValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// byte to byte valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getByte to read a byte");
			try {
				if (messageReceived.getByte("byteValue") == byteValue) {
					logger.log(Logger.Level.TRACE, "Pass: byte to byte - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// byte to short valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getShort to read a byte");
			try {
				if (messageReceived.getShort("byteValue") == byteValue) {
					logger.log(Logger.Level.TRACE, "Pass: byte to short - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// byte to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getChar to read a boolean - this is not valid");
			try {
				char c = messageReceived.getChar("byteValue");

				pass = false;
				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// byte to int valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getInt to read a byte");
			try {
				if (messageReceived.getInt("byteValue") == byteValue) {
					logger.log(Logger.Level.TRACE, "Pass: byte to int - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// byte to long valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getLong to read a byte");
			try {
				if (messageReceived.getLong("byteValue") == byteValue) {
					logger.log(Logger.Level.TRACE, "Pass: byte to long - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// byte to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getFloat to read a boolean - this is not valid");
			try {
				float f = messageReceived.getFloat("byteValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// byte to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getDouble to read a boolean - this is not valid");
			try {
				double d = messageReceived.getDouble("byteValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("MapMessageConversionQTestsByte", e);
		}
	}

	/*
	 * @testName: MapMessageConversionQTestsShort
	 * 
	 * @assertion_ids: JMS:SPEC:75.5; JMS:SPEC:75.6; JMS:JAVADOC:461;
	 * JMS:JAVADOC:437; JMS:JAVADOC:441; JMS:JAVADOC:443; JMS:JAVADOC:449;
	 * JMS:JAVADOC:795; JMS:JAVADOC:796; JMS:JAVADOC:798; JMS:JAVADOC:801;
	 * JMS:JAVADOC:802; JMS:JAVADOC:804;
	 * 
	 * @test_Strategy: Create a MapMessage -. use MapMessage method setShort to
	 * write a short. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void MapMessageConversionQTestsShort() throws Exception {
		try {
			MapMessage messageSent = null;
			MapMessage messageReceived = null;
			short shortValue = 1;
			boolean pass = true;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createMapMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "MapMessageConversionQTestsShort");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.setShort("shortValue", shortValue);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "Msg recvd: " + messageReceived.getStringProperty("COM_SUN_JMS_TESTNAME"));

			// now test conversions for byte
			// -----------------------------------------------
			// short to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getBoolean to read a short - this is not valid");
			try {
				boolean b = messageReceived.getBoolean("shortValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// short to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getString to read a short");
			try {
				if (messageReceived.getString("shortValue").equals(Short.toString(shortValue))) {
					logger.log(Logger.Level.TRACE, "Pass: short to string - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// short to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getBytes[] to read a short - expect MessageFormatException");
			try {
				byte[] b = messageReceived.getBytes("shortValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// short to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getByte to read a short - this is not valid");
			try {
				byte b = messageReceived.getByte("shortValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// short to short valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getShort to read a short");
			try {
				if (messageReceived.getShort("shortValue") == shortValue) {
					logger.log(Logger.Level.TRACE, "Pass: short to short - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// short to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getChar to read a short - this is not valid");
			try {
				char c = messageReceived.getChar("shortValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// short to int valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getInt to read a short");
			try {
				if (messageReceived.getInt("shortValue") == shortValue) {
					logger.log(Logger.Level.TRACE, "Pass: short to int - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// short to long valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getLong to read a short");
			try {
				if (messageReceived.getLong("shortValue") == shortValue) {
					logger.log(Logger.Level.TRACE, "Pass: short to long - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// short to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getFloat to read a short - this is not valid");
			try {
				float f = messageReceived.getFloat("shortValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// short to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getDouble to read a short - this is not valid");
			try {
				double d = messageReceived.getDouble("shortValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("MapMessageConversionQTestsShort", e);
		}
	}

	/*
	 * @testName: MapMessageConversionQTestsChar
	 * 
	 * @assertion_ids: JMS:SPEC:75.7; JMS:SPEC:75.8; JMS:JAVADOC:463;
	 * JMS:JAVADOC:439; JMS:JAVADOC:449; JMS:JAVADOC:795; JMS:JAVADOC:796;
	 * JMS:JAVADOC:797; JMS:JAVADOC:799; JMS:JAVADOC:800; JMS:JAVADOC:801;
	 * JMS:JAVADOC:802; JMS:JAVADOC:804;
	 * 
	 * @test_Strategy: Create a MapMessage -. use MapMessage method setChar to write
	 * a char. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void MapMessageConversionQTestsChar() throws Exception {
		try {
			MapMessage messageSent = null;
			MapMessage messageReceived = null;
			char charValue = 'a';
			boolean pass = true;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createMapMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "MapMessageConversionQTestsChar");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.setChar("charValue", charValue);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "Msg recvd: " + messageReceived.getStringProperty("COM_SUN_JMS_TESTNAME"));

			// now test conversions for byte
			// -----------------------------------------------
			// char to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getBoolean to read a char - this is not valid");
			try {
				boolean b = messageReceived.getBoolean("charValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// char to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getString to read a char");
			try {
				if (messageReceived.getString("charValue").equals(Character.valueOf(charValue).toString())) {
					logger.log(Logger.Level.TRACE, "Pass: char to string - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// char to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getBytes[] to read a char - expect MessageFormatException");
			try {
				byte[] b = messageReceived.getBytes("charValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// char to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getByte to read a char - this is not valid");
			try {
				byte b = messageReceived.getByte("charValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// char to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getShort to read a char");
			try {
				short s = messageReceived.getShort("charValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// char to char valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getChar to read a char ");
			try {
				if (messageReceived.getChar("charValue") == charValue) {
					logger.log(Logger.Level.TRACE, "Pass: char to char - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// char to int invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getInt to read a char ");
			try {
				int i = messageReceived.getInt("charValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// char to long invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getLong to read a char");
			try {
				long l = messageReceived.getLong("charValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// char to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getFloat to read a char - this is not valid");
			try {
				float f = messageReceived.getFloat("charValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// char to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getDouble to read a char - this is not valid");
			try {
				double d = messageReceived.getDouble("charValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("MapMessageConversionQTestsChar", e);
		}
	}

	/*
	 * @testName: MapMessageConversionQTestsInt
	 * 
	 * @assertion_ids: JMS:SPEC:75.9; JMS:SPEC:75.10; JMS:JAVADOC:465;
	 * JMS:JAVADOC:441; JMS:JAVADOC:443; JMS:JAVADOC:449; JMS:JAVADOC:795;
	 * JMS:JAVADOC:796; JMS:JAVADOC:797; JMS:JAVADOC:798; JMS:JAVADOC:801;
	 * JMS:JAVADOC:802; JMS:JAVADOC:804;
	 * 
	 * @test_Strategy: Create a MapMessage -. use MapMessage method setInt to write
	 * an int. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void MapMessageConversionQTestsInt() throws Exception {
		try {
			MapMessage messageSent = null;
			MapMessage messageReceived = null;
			int intValue = 6;
			boolean pass = true;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createMapMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "MapMessageConversionQTestsInt");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.setInt("intValue", intValue);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "Msg recvd: " + messageReceived.getStringProperty("COM_SUN_JMS_TESTNAME"));

			// now test conversions for byte
			// -----------------------------------------------
			// int to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getBoolean to read an int - this is not valid");
			try {
				boolean b = messageReceived.getBoolean("intValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// int to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getString to read an int");
			try {
				if (messageReceived.getString("intValue").equals(Integer.toString(intValue))) {
					logger.log(Logger.Level.TRACE, "Pass: int to string - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// int to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getBytes[] to read an int - expect MessageFormatException");
			int nCount = 0;

			try {
				byte[] b = messageReceived.getBytes("intValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// int to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getByte to read an int - this is not valid");
			try {
				byte b = messageReceived.getByte("intValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// int to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getShort to read an int");
			try {
				short s = messageReceived.getShort("intValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// int to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getChar to read an int - this is not valid");
			try {
				char c = messageReceived.getChar("intValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// int to int valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getInt to read an int");
			try {
				if (messageReceived.getInt("intValue") == intValue) {
					logger.log(Logger.Level.TRACE, "Pass: int to int - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// int to long valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getLong to read an int");
			try {
				if (messageReceived.getLong("intValue") == intValue) {
					logger.log(Logger.Level.TRACE, "Pass: int to long - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// int to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getFloat to read an int - this is not valid");
			try {
				float f = messageReceived.getFloat("intValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// int to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getDouble to read an int - this is not valid");
			try {
				double d = messageReceived.getDouble("intValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("MapMessageConversionQTestsInt", e);
		}
	}

	/*
	 * @testName: MapMessageConversionQTestsLong
	 * 
	 * @assertion_ids: JMS:SPEC:75.11; JMS:SPEC:75.12; JMS:JAVADOC:467;
	 * JMS:JAVADOC:443; JMS:JAVADOC:449; JMS:JAVADOC:795; JMS:JAVADOC:796;
	 * JMS:JAVADOC:797; JMS:JAVADOC:798; JMS:JAVADOC:799; JMS:JAVADOC:801;
	 * JMS:JAVADOC:802; JMS:JAVADOC:804;
	 * 
	 * @test_Strategy: Create a MapMessage -. use MapMessage method setLong to write
	 * a long. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void MapMessageConversionQTestsLong() throws Exception {
		try {
			MapMessage messageSent = null;
			MapMessage messageReceived = null;
			long longValue = 2;
			boolean pass = true;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createMapMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "MapMessageConversionQTestsLong");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.setLong("longValue", longValue);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "Msg recvd: " + messageReceived.getStringProperty("COM_SUN_JMS_TESTNAME"));

			// now test conversions for byte
			// -----------------------------------------------
			// long to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getBoolean to read a long - this is not valid");
			try {
				boolean b = messageReceived.getBoolean("longValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// long to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getString to read a long");
			try {
				if (messageReceived.getString("longValue").equals(Long.toString(longValue))) {
					logger.log(Logger.Level.TRACE, "Pass: long to string - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// long to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getBytes[] to read  a long - expect MessageFormatException");
			try {
				byte[] b = messageReceived.getBytes("longValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// long to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getByte to read an long - this is not valid");
			try {
				byte b = messageReceived.getByte("longValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// long to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getShort to read a long");
			try {
				short s = messageReceived.getShort("longValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// long to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getChar to read a long - this is not valid");
			try {
				char c = messageReceived.getChar("longValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// long to int invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getInt to read a long");
			try {
				int i = messageReceived.getInt("longValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// long to long valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getLong to read a long");
			try {
				if (messageReceived.getLong("longValue") == longValue) {
					logger.log(Logger.Level.TRACE, "Pass: int to long - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// long to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getFloat to read a long - this is not valid");
			try {
				float f = messageReceived.getFloat("longValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// long to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getDouble to read a long ");
			try {
				double d = messageReceived.getDouble("longValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("MapMessageConversionQTestsLong", e);
		}
	}

	/*
	 * @testName: MapMessageConversionQTestsFloat
	 * 
	 * @assertion_ids: JMS:SPEC:75.13; JMS:SPEC:75.14; JMS:JAVADOC:469;
	 * JMS:JAVADOC:445; JMS:JAVADOC:449; JMS:JAVADOC:795; JMS:JAVADOC:796;
	 * JMS:JAVADOC:797; JMS:JAVADOC:798; JMS:JAVADOC:799; JMS:JAVADOC:800;
	 * JMS:JAVADOC:802; JMS:JAVADOC:804;
	 * 
	 * @test_Strategy: Create a MapMessage -. use MapMessage method setFloat to
	 * write a float. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void MapMessageConversionQTestsFloat() throws Exception {
		try {
			MapMessage messageSent = null;
			MapMessage messageReceived = null;
			float floatValue = 5;
			boolean pass = true;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createMapMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "MapMessageConversionQTestsFloat");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.setFloat("floatValue", floatValue);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "Msg recvd: " + messageReceived.getStringProperty("COM_SUN_JMS_TESTNAME"));

			// now test conversions for byte
			// -----------------------------------------------
			// float to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getBoolean to read a float  ");
			try {
				boolean b = messageReceived.getBoolean("floatValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// float to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getString to read a float");
			try {
				if (messageReceived.getString("floatValue").equals(Float.toString(floatValue))) {
					logger.log(Logger.Level.TRACE, "Pass: float to string - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// float to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getBytes[] to read  a float ");
			try {
				byte[] b = messageReceived.getBytes("floatValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// float to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getByte to read a float  ");
			try {
				byte b = messageReceived.getByte("floatValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// float to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getShort to read a float");
			try {
				short s = messageReceived.getShort("floatValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// float to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getChar to read a long  ");
			try {
				char c = messageReceived.getChar("floatValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// float to int invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getInt to read a float");
			try {
				int i = messageReceived.getInt("floatValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// float to long invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getLong to read a long");
			try {
				long l = messageReceived.getLong("floatValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// float to float valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getFloat to read a float  ");
			try {
				if (messageReceived.getFloat("floatValue") == floatValue) {
					logger.log(Logger.Level.TRACE, "Pass: float to float - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// float to double valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getDouble to read a float  ");
			try {
				if (messageReceived.getDouble("floatValue") == floatValue) {
					logger.log(Logger.Level.TRACE, "Pass: float to double - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("MapMessageConversionQTestsFloat", e);
		}
	}

	/*
	 * @testName: MapMessageConversionQTestsDouble
	 * 
	 * @assertion_ids: JMS:SPEC:75.15; JMS:SPEC:75.16; JMS:JAVADOC:471;
	 * JMS:JAVADOC:447; JMS:JAVADOC:449; JMS:JAVADOC:795; JMS:JAVADOC:796;
	 * JMS:JAVADOC:797; JMS:JAVADOC:798; JMS:JAVADOC:799; JMS:JAVADOC:800;
	 * JMS:JAVADOC:801; JMS:JAVADOC:804;
	 * 
	 * @test_Strategy: Create a MapMessage -. use MapMessage method setDouble to
	 * write a double. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void MapMessageConversionQTestsDouble() throws Exception {
		try {
			MapMessage messageSent = null;
			MapMessage messageReceived = null;
			double doubleValue = 3;
			boolean pass = true;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createMapMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "MapMessageConversionQTestsDouble");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.setDouble("doubleValue", doubleValue);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "Msg recvd: " + messageReceived.getStringProperty("COM_SUN_JMS_TESTNAME"));

			// now test conversions for byte
			// -----------------------------------------------
			// double to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getBoolean to read a double  ");
			try {
				boolean b = messageReceived.getBoolean("doubleValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// double to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getString to read a double");
			try {
				if (messageReceived.getString("doubleValue").equals(Double.toString(doubleValue))) {
					logger.log(Logger.Level.TRACE, "Pass: double to string");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// double to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getBytes[] to read  a double ");
			try {
				byte[] b = messageReceived.getBytes("doubleValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// double to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getByte to read a double  ");
			try {
				byte b = messageReceived.getByte("doubleValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// double to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getShort to read a double");
			try {
				short s = messageReceived.getShort("doubleValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// double to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getChar to read a double  ");
			try {
				char c = messageReceived.getChar("doubleValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// double to int invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getInt to read a double");
			try {
				int i = messageReceived.getInt("doubleValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// double to long invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getLong to read a double");
			try {
				long l = messageReceived.getLong("doubleValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// double to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getFloat to read a double  ");
			try {
				float f = messageReceived.getFloat("doubleValue");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// double to double valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getDouble to read an float  ");
			try {
				if (messageReceived.getDouble("doubleValue") == doubleValue) {
					logger.log(Logger.Level.TRACE, "Pass: double to double ");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("MapMessageConversionQTestsDouble", e);
		}
	}

	/*
	 * @testName: MapMessageConversionQTestsString
	 * 
	 * @assertion_ids: JMS:SPEC:75.17; JMS:SPEC:75.18; JMS:JAVADOC:473;
	 * JMS:JAVADOC:433; JMS:JAVADOC:435; JMS:JAVADOC:437; JMS:JAVADOC:441;
	 * JMS:JAVADOC:443; JMS:JAVADOC:445; JMS:JAVADOC:447; JMS:JAVADOC:449;
	 * JMS:JAVADOC:798; JMS:JAVADOC:804;
	 * 
	 * @test_Strategy: Create a MapMessage -. use MapMessage method setString to
	 * write a string. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void MapMessageConversionQTestsString() throws Exception {
		try {
			MapMessage messageSent = null;
			MapMessage messageReceived = null;
			boolean pass = true;
			String myString = "10";
			String myString2 = "true";

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createMapMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "MapMessageConversionQTestsString");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.setString("myString", myString);
			messageSent.setString("myString2", myString2);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "Msg recvd: " + messageReceived.getStringProperty("COM_SUN_JMS_TESTNAME"));

			// now test conversions for String
			// -----------------------------------------------
			// string to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getString to read a String");
			try {
				if (messageReceived.getString("myString").equals(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: string to string - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// string to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getBytes[] to read a String");
			try {
				byte[] b = messageReceived.getBytes("myString");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// String to byte valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getByte to read a String");
			try {
				if (messageReceived.getByte("myString") == Byte.parseByte(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to byte ");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// string to short valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getShort to read a string");
			try {
				if (messageReceived.getShort("myString") == Short.parseShort(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to short ");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// String to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getChar to read a String ");
			try {
				char c = messageReceived.getChar("myString");

				logger.log(Logger.Level.TRACE, "getChar returned " + c);
				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// string to int valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getInt to read a String");
			try {
				if (messageReceived.getInt("myString") == Integer.parseInt(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to int ");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// string to long valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getLong to read a String");
			try {
				if (messageReceived.getLong("myString") == Long.parseLong(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to long ");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// String to float valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getFloat to read a String");
			try {
				if (messageReceived.getFloat("myString") == Float.parseFloat(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to float ");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// String to double valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getDouble to read a String");
			try {
				if (messageReceived.getDouble("myString") == Double.parseDouble(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to double ");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// String to boolean
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getBoolean to read a string ");
			try {
				if (messageReceived.getBoolean("myString2") == Boolean.valueOf(myString2).booleanValue()) {
					logger.log(Logger.Level.TRACE, "Pass: String to boolean ");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// String to boolean
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getBoolean to read a string that is not true");
			try {
				boolean b = messageReceived.getBoolean("myString");

				if (b != false) {
					logger.log(Logger.Level.INFO, "Fail: !true should have returned false");
					pass = false;
				} else {
					logger.log(Logger.Level.TRACE, "Pass: !true returned false");
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("MapMessageConversionQTestsString", e);
		}
	}

	/*
	 * @testName: MapMessageConversionQTestsBytes
	 * 
	 * @assertion_ids: JMS:SPEC:75.19; JMS:SPEC:75.20; JMS:JAVADOC:475;
	 * JMS:JAVADOC:451; JMS:JAVADOC:795; JMS:JAVADOC:796; JMS:JAVADOC:797;
	 * JMS:JAVADOC:798; JMS:JAVADOC:799; JMS:JAVADOC:800; JMS:JAVADOC:801;
	 * JMS:JAVADOC:802; JMS:JAVADOC:803;
	 * 
	 * @test_Strategy: Create a MapMessage -. use MapMessage method setBytes to
	 * write a byte[] to the message. Verify the proper conversion support as in
	 * 3.11.3
	 */
	@Test
	public void MapMessageConversionQTestsBytes() throws Exception {
		try {
			MapMessage messageSent = null;
			MapMessage messageReceived = null;
			byte[] byteValues = { 1, 2, 3 };
			boolean pass = true;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createMapMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "MapMessageConversionQTestsBytes");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte[] primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.setBytes("byteValues", byteValues);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "Msg recvd: " + messageReceived.getStringProperty("COM_SUN_JMS_TESTNAME"));

			// now test conversions for boolean
			// -----------------------------------------------
			// byte[] to byte[] - valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getBytes[] to read a byte[] ");
			try {
				byte[] b = messageReceived.getBytes("byteValues");

				for (int i = 0; i < b.length; i++) {
					if (b[i] != byteValues[i]) {
						logger.log(Logger.Level.INFO, "Fail: byte[] value returned is invalid");
						pass = false;
					} else {
						logger.log(Logger.Level.TRACE, "Pass: byte[] returned is valid");
					}
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// byte[] to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getBoolean to read a byte[]");
			try {
				boolean b = messageReceived.getBoolean("byteValues");

				logger.log(Logger.Level.INFO,
						"Fail: byte[] to boolean conversion should have thrown MessageFormatException");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// byte[] to string invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getString to read a byte[]");
			try {
				String s = messageReceived.getString("byteValues");

				logger.log(Logger.Level.INFO,
						"Fail: byte[] to boolean conversion should have thrown MessageFormatException");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// byte[] to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getByte to read a byte[] - expect MessageFormatException");
			try {
				byte b = messageReceived.getByte("byteValues");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// byte[] to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getShort to read a byte[] - expect MessageFormatException");
			try {
				short s = messageReceived.getShort("byteValues");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// byte[] to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getChar to read a byte[] - expect MessageFormatException");
			try {
				char c = messageReceived.getChar("byteValues");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// byte[] to int invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getInt to read a byte[] - expect MessageFormatException");
			try {
				int i = messageReceived.getInt("byteValues");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// byte[] to long invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getLong to read a byte[] - expect MessageFormatException");
			try {
				long l = messageReceived.getLong("byteValues");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// byte[] to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getFloat to read a byte[] - expect MessageFormatException");
			try {
				float f = messageReceived.getFloat("byteValues");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// byte[] to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getDouble to read a byte[] - expect MessageFormatException");
			try {
				double d = messageReceived.getDouble("byteValues");

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("MapMessageConversionQTestsBytes", e);
		}
	}

	/*
	 * @testName: MapMessageConversionQTestsInvFormatString
	 * 
	 * @assertion_ids: JMS:SPEC:76;
	 * 
	 * @test_Strategy: Create a MapMessage -. use MapMessage method setString to
	 * write a text string of "mytest string". Verify NumberFormatException is
	 * thrown
	 * 
	 */
	@Test
	public void MapMessageConversionQTestsInvFormatString() throws Exception {
		try {
			MapMessage messageSent = null;
			MapMessage messageReceived = null;
			boolean pass = true;
			String myString = "mytest string";

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createMapMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "MapMessageConversionQTestsInvFormatString");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.setString("myString", myString);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);
			logger.log(Logger.Level.TRACE, "Msg recvd: " + messageReceived.getStringProperty("COM_SUN_JMS_TESTNAME"));

			// -----------------------------------------------
			// String to byte
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getByte to read a String that is not valid ");
			try {
				byte b = messageReceived.getByte("myString");

				logger.log(Logger.Level.INFO, "Fail: java.lang.NumberFormatException expected");
				pass = false;
			} catch (NumberFormatException nf) {
				logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// string to short
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getShort to read a string that is not valid ");
			try {
				short s = messageReceived.getShort("myString");

				logger.log(Logger.Level.INFO, "Fail: NumberFormatException was expected");
				pass = false;
			} catch (NumberFormatException nf) {
				logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// string to int
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getInt to read a String that is not valid ");
			try {
				int i = messageReceived.getInt("myString");

				logger.log(Logger.Level.INFO, "Fail: NumberFormatException was expected");
				pass = false;
			} catch (NumberFormatException nf) {
				logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// string to long
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getLong to read a String that is not valid ");
			try {
				long l = messageReceived.getLong("myString");

				logger.log(Logger.Level.INFO, "Fail: NumberFormatException was expected");
				pass = false;
			} catch (NumberFormatException nf) {
				logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// String to float
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getFloat to read a String that is not valid ");
			try {
				float f = messageReceived.getFloat("myString");

				logger.log(Logger.Level.INFO, "Fail: NumberFormatException was expected");
				pass = false;
			} catch (NumberFormatException nf) {
				logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}

			// -----------------------------------------------
			// String to double
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use getDouble to read a String that is not valid ");
			try {
				double d = messageReceived.getDouble("myString");

				logger.log(Logger.Level.INFO, "Fail: NumberFormatException was expected");
				pass = false;
			} catch (NumberFormatException nf) {
				logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("MapMessageConversionQTestsInvFormatString", e);
		}
	}

	/*
	 * @testName: mapMessageQNotWritable
	 *
	 * @assertion_ids: JMS:SPEC:73; JMS:JAVADOC:806; JMS:JAVADOC:808;
	 * JMS:JAVADOC:810; JMS:JAVADOC:812; JMS:JAVADOC:814; JMS:JAVADOC:816;
	 * JMS:JAVADOC:818; JMS:JAVADOC:820; JMS:JAVADOC:822; JMS:JAVADOC:824;
	 * JMS:JAVADOC:826; JMS:JAVADOC:829;
	 *
	 * @test_Strategy: Create a MapMessage, send it to a Queue. Receive it and try
	 * to write to the received Message's body, MessageNotWritableException should
	 * be thrown.
	 */
	@Test
	public void mapMessageQNotWritable() throws Exception {
		try {
			MapMessage messageSent = null;
			MapMessage messageReceived = null;
			boolean pass = true;
			byte bValue = 127;
			short sValue = 32767;
			char cValue = '\uFFFF';
			int iValue = 2147483647;
			long lValue = 9223372036854775807L;
			float fValue = 0.0f;
			double dValue = -0.0;
			String ssValue = "abc";
			byte[] bbValue = { 0, 88, 127 };

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			messageSent = tool.getDefaultQueueSession().createMapMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "mapMessageQNotWritable");

			// -----------------------------------------------------------------------------
			try {
				messageSent.setString("ssValue", ssValue);
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception was thrown: ", e);
				throw new Exception("Error: failed to setString", e);
			}

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (MapMessage) tool.getDefaultQueueReceiver().receive(timeout);

			logger.log(Logger.Level.INFO, "Writing a boolean ... ");
			try {
				messageReceived.setBoolean("pass", pass);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setBoolean");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with setBoolean");
			}

			logger.log(Logger.Level.INFO, "Writing a byte ... ");
			try {
				messageReceived.setByte("bValue", bValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setByte");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with setByte");
			}

			logger.log(Logger.Level.INFO, "Writing a short ... ");
			try {
				messageReceived.setShort("sValue", sValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setShort");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with setShort");
			}

			logger.log(Logger.Level.INFO, "Writing a char ... ");
			try {
				messageReceived.setChar("cValue", cValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setChar");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with setChar");
			}

			logger.log(Logger.Level.INFO, "Writing a int ... ");
			try {
				messageReceived.setInt("iValue", iValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setInt");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with setInt");
			}

			logger.log(Logger.Level.INFO, "Writing a long ... ");
			try {
				messageReceived.setLong("lValue", lValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setLong");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with setLong");
			}

			logger.log(Logger.Level.INFO, "Writing a float ... ");
			try {
				messageReceived.setFloat("fValue", fValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setFloat");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with setFloat");
			}

			logger.log(Logger.Level.INFO, "Writing a double ... ");
			try {
				messageReceived.setDouble("dValue", dValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setDouble");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with setDouble");
			}

			logger.log(Logger.Level.INFO, "Writing a bytes... ");
			try {
				messageReceived.setBytes("bbValue", bbValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setBytes");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with setBytes");
			}

			logger.log(Logger.Level.INFO, "Writing a bytes... ");
			try {
				messageReceived.setBytes("bbValue", bbValue, 0, 1);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setBytes");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with setBytes");
			}

			logger.log(Logger.Level.INFO, "Writing a string ... ");
			try {
				messageReceived.setString("ssValue", ssValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setString");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with setString");
			}

			logger.log(Logger.Level.INFO, "Writing a object ... ");
			try {
				messageReceived.setObject("oValue", new Integer(iValue));
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setObject");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with setObject");
			}

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("mapMessageQNotWritable", e);
		}
	}

	/*
	 * @testName: mapMessageQIllegalarg
	 *
	 * @assertion_ids: JMS:JAVADOC:805; JMS:JAVADOC:807; JMS:JAVADOC:809;
	 * JMS:JAVADOC:811; JMS:JAVADOC:813; JMS:JAVADOC:815; JMS:JAVADOC:817;
	 * JMS:JAVADOC:819; JMS:JAVADOC:821; JMS:JAVADOC:823; JMS:JAVADOC:825;
	 * JMS:JAVADOC:827;
	 * 
	 * @test_Strategy: Create a MapMessage. Write to the message using each type of
	 * set method and as an object with null String as name. Verify that
	 * IllegalArgumentException thrown.
	 */
	@Test
	public void mapMessageQIllegalarg() throws Exception {
		try {
			MapMessage messageSent = null;
			boolean pass = true;
			byte bValue = 127;
			short sValue = 32767;
			char cValue = '\uFFFF';
			int iValue = 2147483647;
			long lValue = 9223372036854775807L;
			float fValue = 0.0f;
			double dValue = -0.0;
			String ssValue = "abc";
			byte[] bbValue = { 0, 88, 127 };

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			messageSent = tool.getDefaultQueueSession().createMapMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "mapMessageQIllegalarg");

			// -----------------------------------------------------------------------------

			logger.log(Logger.Level.INFO, "Writing a boolean ... ");
			try {
				messageSent.setBoolean("", pass);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setBoolean");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setBoolean");
			}

			logger.log(Logger.Level.INFO, "Writing a byte ... ");
			try {
				messageSent.setByte("", bValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setByte");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setByte");
			}

			logger.log(Logger.Level.INFO, "Writing a short ... ");
			try {
				messageSent.setShort("", sValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setShort");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setShort");
			}

			logger.log(Logger.Level.INFO, "Writing a char ... ");
			try {
				messageSent.setChar("", cValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setChar");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setChar");
			}

			logger.log(Logger.Level.INFO, "Writing a int ... ");
			try {
				messageSent.setInt("", iValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setInt");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setInt");
			}

			logger.log(Logger.Level.INFO, "Writing a long ... ");
			try {
				messageSent.setLong("", lValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setLong");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setLong");
			}

			logger.log(Logger.Level.INFO, "Writing a float ... ");
			try {
				messageSent.setFloat("", fValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setFloat");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setFloat");
			}

			logger.log(Logger.Level.INFO, "Writing a double ... ");
			try {
				messageSent.setDouble("", dValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setDouble");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setDouble");
			}

			logger.log(Logger.Level.INFO, "Writing a bytes... ");
			try {
				messageSent.setBytes("", bbValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setBytes");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setBytes");
			}

			logger.log(Logger.Level.INFO, "Writing a bytes... ");
			try {
				messageSent.setBytes("", bbValue, 0, 1);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setBytes");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setBytes");
			}

			logger.log(Logger.Level.INFO, "Writing a string ... ");
			try {
				messageSent.setString("", ssValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setString");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setString");
			}

			logger.log(Logger.Level.INFO, "Writing a object ... ");
			try {
				messageSent.setObject("", new Integer(iValue));
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to setObject");
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Got Expected IllegalArgumentException with setObject");
			}

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("mapMessageQIllegalarg", e);
		}
	}

}
