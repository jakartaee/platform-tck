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
package com.sun.ts.tests.jms.core.topicMsgProperties;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.ConnectionMetaData;
import jakarta.jms.MessageFormatException;
import jakarta.jms.MessageNotWriteableException;
import jakarta.jms.TextMessage;


public class TopicPropertyTestsIT {
	private static final String testName = "com.sun.ts.tests.jms.core.topicMsgProperties.TopicPropertyTestsIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(TopicPropertyTestsIT.class.getName());

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
	 * Individual tests create the JmsTool object with one default TopicConnection,
	 * as well as a default Topic. Tests that require multiple Destinations create
	 * the extras within the test
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
				logger.log(Logger.Level.INFO, "Cleanup: Closing TopicConnection");
				tool.closeAllConnections(connections);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "An error occurred while cleaning: ", e);
			throw new Exception("Cleanup failed!", e);
		}
	}

	/* Tests */

	/*
	 * @testName: msgPropertiesTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:20.1; JMS:SPEC:20.2; JMS:SPEC:20.3; JMS:SPEC:20.4;
	 * JMS:SPEC:20.5; JMS:SPEC:20.6; JMS:SPEC:20.7; JMS:SPEC:20.8; JMS:SPEC:21;
	 * JMS:SPEC:23; JMS:SPEC:24; JMS:SPEC:25; JMS:SPEC:26; JMS:SPEC:10; JMS:SPEC:27;
	 * JMS:SPEC:28; JMS:SPEC:29; JMS:SPEC:31; JMS:SPEC:32; JMS:SPEC:19; JMS:SPEC:70;
	 * JMS:SPEC:71; JMS:JAVADOC:411; JMS:JAVADOC:413; JMS:JAVADOC:415;
	 * JMS:JAVADOC:417; JMS:JAVADOC:419; JMS:JAVADOC:421; JMS:JAVADOC:423;
	 * JMS:JAVADOC:425; JMS:JAVADOC:427; JMS:JAVADOC:409; JMS:JAVADOC:391;
	 * JMS:JAVADOC:393; JMS:JAVADOC:395; JMS:JAVADOC:397; JMS:JAVADOC:399;
	 * JMS:JAVADOC:401; JMS:JAVADOC:403; JMS:JAVADOC:405; JMS:JAVADOC:407;
	 * JMS:JAVADOC:500; JMS:JAVADOC:516; JMS:JAVADOC:387; JMS:JAVADOC:792;
	 * JMS:JAVADOC:776; JMS:JAVADOC:778; JMS:JAVADOC:780; JMS:JAVADOC:782;
	 * JMS:JAVADOC:784; JMS:JAVADOC:786; JMS:JAVADOC:788; JMS:JAVADOC:790;
	 * JMS:JAVADOC:793; JMS:SPEC:34.4; JMS:SPEC:34.5;
	 *
	 * @test_Strategy: set and read properties for boolean, byte, short, int, long,
	 * float, double, and String. Verify expected results set and read properties
	 * for Boolean, Byte, Short, Int, Long, Float, Double, and String. Verify
	 * expected results.
	 * 
	 * When a client receives a message it is in read-only mode. Send a message and
	 * have the client attempt modify the properties. Verify that a
	 * MessageNotWriteableException is thrown. Call setObject property with an
	 * invalid object and verify that a MessageFormatException is thrown
	 * 
	 * call property get methods( other than getStringProperty and
	 * getObjectProperty) for non-existent properties and verify that a null pointer
	 * exception is returned. call getStringProperty and getObjectProperty for
	 * non-existent properties and verify that a null is returned.
	 * 
	 * set object properties and verify the correct value is returned with the
	 * getObjectProperty method.
	 * 
	 * call the clearProperties method on the received message and verify that the
	 * messages properties were deleted. Test that getObjectProperty returns a null
	 * and the getShortProperty throws a null pointer exception.
	 * 
	 * After clearing the message properties, call getText and verify that the
	 * message body has not been cleared.
	 * 
	 * Call ConnectionMetaData.getJMSXPropertyNames() and verify that the names of
	 * the required JMSX properties for JMSXGroupID and JMSXGroupSeq are returned.
	 */
	@Test
	public void msgPropertiesTopicTest() throws Exception {
		boolean pass = true;
		boolean bool = true;
		byte bValue = 127;
		short nShort = 10;
		int nInt = 5;
		long nLong = 333;
		float nFloat = 1;
		double nDouble = 100;
		String testString = "test";
		Enumeration propertyNames = null;
		Enumeration jmsxDefined = null;
		int numPropertyNames = 18;
		String testMessageBody = "Testing...";

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
			tool.getDefaultTopicConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultTopicSession().createTextMessage();
			messageSent.setText(testMessageBody);

			// -------------------------------------------------------------
			// set properties for boolean, byte, short, int, long, float, double, and
			// String.
			// -------------------------------------------------------------
			messageSent.setBooleanProperty("TESTBOOLEAN", bool);
			messageSent.setByteProperty("TESTBYTE", bValue);
			messageSent.setShortProperty("TESTSHORT", nShort);
			messageSent.setIntProperty("TESTINT", nInt);
			messageSent.setFloatProperty("TESTFLOAT", nFloat);
			messageSent.setDoubleProperty("TESTDOUBLE", nDouble);
			messageSent.setStringProperty("TESTSTRING", "test");
			messageSent.setLongProperty("TESTLONG", nLong);

			// --------------------------------------------------------------
			// set properties for Boolean, Byte, Short, Int, Long, Float, Double, and
			// String.
			// --------------------------------------------------------------
			messageSent.setObjectProperty("OBJTESTBOOLEAN", Boolean.valueOf(bool));
			messageSent.setObjectProperty("OBJTESTBYTE", Byte.valueOf(bValue));
			messageSent.setObjectProperty("OBJTESTSHORT", Short.valueOf(nShort));
			messageSent.setObjectProperty("OBJTESTINT", Integer.valueOf(nInt));
			messageSent.setObjectProperty("OBJTESTFLOAT", Float.valueOf(nFloat));
			messageSent.setObjectProperty("OBJTESTDOUBLE", Double.valueOf(nDouble));
			messageSent.setObjectProperty("OBJTESTSTRING", "test");
			messageSent.setObjectProperty("OBJTESTLONG", Long.valueOf(nLong));
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgPropertiesTopicTest");
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultTopicPublisher().publish(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (TextMessage) tool.getDefaultTopicSubscriber().receive(timeout);

			// ------------------------------------------------------------------------------
			// An attempt to use any other class than Boolean, Byte, Short, Int, Long,
			// Float,
			// Double, and Stringmust throw a JMS MessageFormatException.
			// ------------------------------------------------------------------------------
			try {
				messageSent.setObjectProperty("OBJTESTLONG", new Object());
				logger.log(Logger.Level.INFO, "Error: expected MessageFormatException from invalid ");
				logger.log(Logger.Level.INFO, "call to setObjectProperty did not occur!");
				pass = false;
			} catch (MessageFormatException fe) {
				logger.log(Logger.Level.TRACE, "Pass: ");
				logger.log(Logger.Level.TRACE,
						"      MessageFormatException as expected from invalid setObjectProperty call");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR, "from call to setObjectProperty!");
				pass = false;
			}

			// -------------------------------------------------------------------------
			// Received message should be read-only - verify proper exception is
			// thrown
			// when trying to modify msg properties.
			// ------------------------------------------------------------------------
			try {
				messageReceived.setBooleanProperty("TESTBOOLEAN", bool);
				logger.log(Logger.Level.INFO, "Error: exception should have occurred for setBooleanProperty");
				pass = false;
			} catch (MessageNotWriteableException aBool) {
				logger.log(Logger.Level.TRACE, "Pass: exception as expected for setBooleanProperty ");
			}
			try {
				messageReceived.setByteProperty("TESTBYTE", bValue);
				logger.log(Logger.Level.INFO, "Error: exception should have occurred for setByteProperty");
				pass = false;
			} catch (MessageNotWriteableException aBool) {
				logger.log(Logger.Level.TRACE, "Pass: exception as expected for setByteProperty ");
			}
			try {
				messageReceived.setShortProperty("TESTSHORT", nShort);
				pass = false;
				logger.log(Logger.Level.INFO, "Error: exception should have occurred for setShortProperty");
			} catch (MessageNotWriteableException aBool) {
				logger.log(Logger.Level.TRACE, "Pass: exception as expected for setShortProperty ");
			}
			try {
				messageReceived.setIntProperty("TESTINT", nInt);
				logger.log(Logger.Level.INFO, "Error: exception should have occurred for setIntProperty");
				pass = false;
			} catch (MessageNotWriteableException aBool) {
				logger.log(Logger.Level.TRACE, "Pass: exception as expected for setIntProperty ");
			}
			try {
				messageReceived.setFloatProperty("TESTFLOAT", nFloat);
				logger.log(Logger.Level.INFO, "Error: exception should have occurred for setFloatProperty");
				pass = false;
			} catch (MessageNotWriteableException aBool) {
				logger.log(Logger.Level.TRACE, "Pass: exception as expected for setFloatProperty ");
			}
			try {
				messageReceived.setDoubleProperty("TESTDOUBLE", nDouble);
				logger.log(Logger.Level.INFO, "Error: exception should have occurred for setDoubleProperty");
				pass = false;
			} catch (MessageNotWriteableException aBool) {
				logger.log(Logger.Level.TRACE, "Pass: exception as expected for setDoubleProperty ");
			}
			try {
				messageReceived.setStringProperty("TESTSTRING", testString);
				logger.log(Logger.Level.INFO, "Error: exception should have occurred for setStringProperty");
				pass = false;
			} catch (MessageNotWriteableException aBool) {
				logger.log(Logger.Level.TRACE, "Pass: exception as expected for setStringProperty ");
			}
			try {
				messageReceived.setLongProperty("TESTLONG", nLong);
				logger.log(Logger.Level.INFO, "Error: exception should have occurred for setLongProperty");
				pass = false;
			} catch (MessageNotWriteableException aBool) {
				logger.log(Logger.Level.TRACE, "Pass: exception as expected for setLongProperty ");
			}
			try {
				messageReceived.setObjectProperty("OBJTESTBOOLEAN", Boolean.valueOf(bool));
				logger.log(Logger.Level.INFO, "Error: exception should have occurred for setObjectProperty");
				pass = false;
			} catch (MessageNotWriteableException aBool) {
				logger.log(Logger.Level.TRACE, "Pass: exception as expected for setObjectProperty ");
			}

			// iterate thru the property names
			int i = 0;
			propertyNames = messageReceived.getPropertyNames();
			do {
				String tmp = (String) propertyNames.nextElement();
				logger.log(Logger.Level.TRACE, "+++++++   Property Name is: " + tmp);
				if (tmp.indexOf("JMS") != 0)
					i++;
				else if (tmp.equals("JMSXDeliveryCount"))
					i++;
			} while (propertyNames.hasMoreElements());

			if (i == numPropertyNames) {
				logger.log(Logger.Level.TRACE, "Pass: # of properties is " + numPropertyNames + " as expected");
			} else {
				logger.log(Logger.Level.INFO, "Error: expected " + numPropertyNames + " property names, but got " + i);
				pass = false;
			}

			// --------------------------------------------------------------------------------
			// read and verify the property values for primitive types in the received
			// message
			// --------------------------------------------------------------------------------
			if (messageReceived.getBooleanProperty("TESTBOOLEAN") == bool) {
				logger.log(Logger.Level.TRACE, "Pass: getBooleanProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Error: incorrect value returned from getBooleanProperty");
				pass = false;
			}
			if (messageReceived.getByteProperty("TESTBYTE") == bValue) {
				logger.log(Logger.Level.TRACE, "Pass: getByteProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Error: incorrect value returned from getByteProperty");
				pass = false;
			}
			if (messageReceived.getLongProperty("TESTLONG") == nLong) {
				logger.log(Logger.Level.TRACE, "Pass: getLongProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Error: incorrect value returned from getLongProperty");
				pass = false;
			}
			if (messageReceived.getStringProperty("TESTSTRING").equals(testString)) {
				logger.log(Logger.Level.TRACE, "Pass: getStringProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Error: incorrect value returned from getStringProperty");
				pass = false;
			}
			if (messageReceived.getDoubleProperty("TESTDOUBLE") == nDouble) {
				logger.log(Logger.Level.TRACE, "Pass: getDoubleProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Error: incorrect value returned from getDoubleProperty");
				pass = false;
			}
			if (messageReceived.getFloatProperty("TESTFLOAT") == nFloat) {
				logger.log(Logger.Level.TRACE, "Pass: getFloatProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Error: incorrect value returned from getFloatProperty");
				pass = false;
			}
			if (messageReceived.getIntProperty("TESTINT") == nInt) {
				logger.log(Logger.Level.TRACE, "Pass: getIntProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Error: incorrect value returned from getIntProperty");
				pass = false;
			}
			if (messageReceived.getShortProperty("TESTSHORT") == nShort) {
				logger.log(Logger.Level.TRACE, "Pass: getShortProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Error: incorrect value returned from getShortProperty");
				pass = false;
			}

			// -----------------------------------------------------------
			// The other property get methods ( other than getStringProperty and
			// getObjectProperty) must behave as if the property exists with a
			// null value
			// -----------------------------------------------------------
			// Getting a property value for a name which has not been set
			// returns a null value.
			// Only the getStringProperty and getObjectProperty methods can return a
			// null
			// value. The other property get methods must throw a
			// java.lang.NullPointerException if they are used to get a non-existent
			// property.
			try {
				boolean b = messageReceived.getBooleanProperty("TESTDUMMY");
				if (b != false) {
					logger.log(Logger.Level.INFO, "Error: should havereceived false for getBooleanProperty");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Unexpected Exception: ", e);
				pass = false;
			}

			try {
				byte value = messageReceived.getByteProperty("TESTDUMMY");

				logger.log(Logger.Level.INFO, "Error: NumberFormatException should have occurred for getByteProperty");
				pass = false;
			} catch (java.lang.NumberFormatException e) {
				logger.log(Logger.Level.TRACE, "Pass: NumberFormatException as expected ");
			}
			try {
				short value = messageReceived.getShortProperty("TESTDUMMY");

				logger.log(Logger.Level.INFO, "Error: NumberFormatException should have occurred for getShortProperty");
				pass = false;
			} catch (java.lang.NumberFormatException np) {
				logger.log(Logger.Level.TRACE, "Pass: NumberFormatException as expected ");
			}
			try {
				int value = messageReceived.getIntProperty("TESTDUMMY");

				logger.log(Logger.Level.INFO, "Error: NumberFormatException should have occurred for getIntProperty");
				pass = false;
			} catch (java.lang.NumberFormatException np) {
				logger.log(Logger.Level.TRACE, "Pass:NumberFormatException as expected ");
			}
			try {
				long value = messageReceived.getLongProperty("TESTDUMMY");

				logger.log(Logger.Level.INFO, "Error: NumberFormatException should have occurred for getLongProperty");
				pass = false;
			} catch (java.lang.NumberFormatException np) {
				logger.log(Logger.Level.TRACE, "Pass: NumberFormatException as expected ");
			}
			try {
				float value = messageReceived.getFloatProperty("TESTDUMMY");

				logger.log(Logger.Level.INFO, "Error: NullPointerException should have occurred for getFloatProperty");
				pass = false;
			} catch (java.lang.NullPointerException np) {
				logger.log(Logger.Level.TRACE, "Pass: NullPointerException as expected ");
			}
			try {
				double value = messageReceived.getDoubleProperty("TESTDUMMY");

				logger.log(Logger.Level.INFO, "Error: NullPointerException should have occurred for getDoubleProperty");
				pass = false;
			} catch (java.lang.NullPointerException np) {
				logger.log(Logger.Level.TRACE, "Pass: NullPointerException as expected ");
			}

			// --------------------------------------------------------------------------------
			// Getting a property value for a name which has not been set returns a
			// null value.
			// (For getStringProperty and getObjectProperty)
			// --------------------------------------------------------------------------------
			String value = messageReceived.getStringProperty("TESTDUMMY");

			if (value == null) {
				logger.log(Logger.Level.TRACE, "Pass: getStringProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO,
						"Error: expected a null return from getStringProperty for invalid property");
				pass = false;
			}
			Boolean aBool = (Boolean) messageReceived.getObjectProperty("TESTDUMMY");

			if (aBool == null) {
				logger.log(Logger.Level.TRACE, "Pass: getObjectProperty returned correct value for Boolean");
			} else {
				logger.log(Logger.Level.INFO,
						"Error: expected a null return from getObjectProperty for invalid property");
				pass = false;
			}

			// --------------------------------------------------------------------------------
			// read and verify the property values for getObject in the received
			// message
			// --------------------------------------------------------------------------------
			Boolean boolValue = (Boolean) messageReceived.getObjectProperty("OBJTESTBOOLEAN");

			if (boolValue.booleanValue() == bool) {
				logger.log(Logger.Level.TRACE, "Pass: getObjectProperty returned correct value for Boolean");
			} else {
				logger.log(Logger.Level.INFO, "Error: incorrect value returned for Boolean");
				pass = false;
			}
			Byte byteValue = (Byte) messageReceived.getObjectProperty("OBJTESTBYTE");

			if (byteValue.byteValue() == bValue) {
				logger.log(Logger.Level.TRACE, "Pass: getObjectProperty returned correct Byte value");
			} else {
				logger.log(Logger.Level.INFO, "Error: incorrect value returned from Byte");
				pass = false;
			}
			Long lValue = (Long) messageReceived.getObjectProperty("OBJTESTLONG");

			if (lValue.longValue() == nLong) {
				logger.log(Logger.Level.TRACE, "Pass: getObjectProperty returned correct value for Long");
			} else {
				logger.log(Logger.Level.INFO, "Error: getObjectProperty returned incorrect value returned for Long");
				pass = false;
			}

			// String value =
			// (String)messageReceived.getObjectProperty("OBJTESTSTRING");
			if (messageReceived.getObjectProperty("OBJTESTSTRING").equals(testString)) {
				logger.log(Logger.Level.TRACE, "Pass: getObjectProperty returned correct value for String");
			} else {
				logger.log(Logger.Level.INFO, "Error: getObjectProperty returned incorrect value for String");
				pass = false;
			}
			Double dValue = (Double) messageReceived.getObjectProperty("OBJTESTDOUBLE");

			if (dValue.doubleValue() == nDouble) {
				logger.log(Logger.Level.TRACE, "Pass: getObjectProperty returned correct value for Double");
			} else {
				logger.log(Logger.Level.INFO, "Error: getObjectProperty returned incorrect value for Double");
				pass = false;
			}
			Float fValue = (Float) messageReceived.getObjectProperty("OBJTESTFLOAT");

			if (fValue.floatValue() == nFloat) {
				logger.log(Logger.Level.TRACE, "Pass: getObjectProperty returned correct value for Float");
			} else {
				logger.log(Logger.Level.INFO, "Error: getObjectProperty returned incorrect value for Float");
				pass = false;
			}
			Integer iValue = (Integer) messageReceived.getObjectProperty("OBJTESTINT");

			if (iValue.intValue() == nInt) {
				logger.log(Logger.Level.TRACE, "Pass: getObjectProperty returned correct value for Integer");
			} else {
				logger.log(Logger.Level.INFO, "Error: getObjectProperty returned incorrect value for Integer");
				pass = false;
			}
			Short sValue = (Short) messageReceived.getObjectProperty("OBJTESTSHORT");

			if (sValue.shortValue() == nShort) {
				logger.log(Logger.Level.TRACE, "Pass: getObjectProperty returned correct value for Short");
			} else {
				logger.log(Logger.Level.INFO, "Error: getObjectProperty returned incorrect value for Short");
				pass = false;
			}

			// clear message properties
			messageReceived.clearProperties();

			// -------------------------------------------------------------------
			// A message?s properties are deleted by the clearProperties method.
			// This leaves the message with an empty set of properties.
			// -------------------------------------------------------------------
			Long aLong = (Long) messageReceived.getObjectProperty("OBJTESTLONG");

			if (aLong == null) {
				logger.log(Logger.Level.TRACE, "Pass: property was cleared");
			} else {
				logger.log(Logger.Level.INFO,
						"Error: getObjectProperty should have returned null for cleared property");
				pass = false;
			}
			try {
				short aShort = messageReceived.getShortProperty("TESTSHORT");

				logger.log(Logger.Level.INFO, "Error: NumberFormatException should have occurred for getShortProperty");
				logger.log(Logger.Level.INFO, "Properties have been cleared!");
				pass = false;
			} catch (java.lang.NumberFormatException np) {
				logger.log(Logger.Level.TRACE, "Pass: NumberFormatException as expected ");
			}

			// -------------------------------------------------------------------
			// A message?s properties are deleted by the clearProperties method.
			// This leaves the message with an empty set of properties.
			// -------------------------------------------------------------------
			aLong = (Long) messageReceived.getObjectProperty("OBJTESTLONG");

			if (aLong == null) {
				logger.log(Logger.Level.TRACE, "Pass: property was cleared");
			} else {
				logger.log(Logger.Level.INFO,
						"Error: getObjectProperty should have returned null for cleared property");
				pass = false;
			}
			try {
				short aShort = messageReceived.getShortProperty("TESTSHORT");

				logger.log(Logger.Level.INFO, "Error: NumberFormatException should have occurred for getShortProperty");
				logger.log(Logger.Level.INFO, "Properties have been cleared!");
				pass = false;
			} catch (java.lang.NumberFormatException np) {
				logger.log(Logger.Level.TRACE, "Pass: NumberFormatException as expected ");
			}

			// -------------------------------------------------------------------
			// clearing the message property should not effect the message body.
			// -------------------------------------------------------------------
			logger.log(Logger.Level.INFO, "Message body is : " + messageReceived.getText());
			if (messageReceived.getText().equals(testMessageBody)) {
				logger.log(Logger.Level.TRACE, "Pass: able to read message body after clearProperties");
			} else {
				logger.log(Logger.Level.INFO, "Error: unable to read message body after clearProperties");
				pass = false;
			}

			// -------------------------------------------------------------------
			// ConnectionMetaData.getJMSXPropertyNames() method returns the
			// names of the JMSX properties supported by a connection.
			// -------------------------------------------------------------------
			try {
				ConnectionMetaData data = tool.getDefaultTopicConnection().getMetaData();
				Enumeration cmd = data.getJMSXPropertyNames();
				String propName;

				if (cmd == null) {
					logger.log(Logger.Level.INFO, "Error: no JMSX property names were returned!");
					logger.log(Logger.Level.INFO,
							"expected JMSXGroupID, JMSXGroupSeq, JMSXDeliveryCount at a miniumum");
					pass = false;
				} else {
					int iCount = 0;

					do {
						propName = (String) cmd.nextElement();
						logger.log(Logger.Level.TRACE, propName);
						if (propName.equals("JMSXGroupID") || propName.equals("JMSXGroupSeq")
								|| propName.equals("JMSXDeliveryCount")) {
							iCount++;
						}
					} while (cmd.hasMoreElements());
					if (iCount > 1) {
						logger.log(Logger.Level.TRACE, "Pass:");
					} else {
						logger.log(Logger.Level.INFO, "Error: Expected property names not returned");
						pass = false;
					}
				}
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception: ", ee);
				logger.log(Logger.Level.ERROR, "attempting to read JMSX property names.");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during property tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("msgPropertiesTopicTest");
		}
	}

	/*
	 * @testName: msgPropertiesConversionTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:22.1; JMS:SPEC:22.2; JMS:SPEC:22.3; JMS:SPEC:22.4;
	 * JMS:SPEC:22.5; JMS:SPEC:22.6; JMS:SPEC:22.7; JMS:SPEC:22.8; JMS:SPEC:22.9;
	 * JMS:SPEC:22.10; JMS:SPEC:22.11; JMS:SPEC:22.12; JMS:SPEC:22.13;
	 * JMS:SPEC:22.14; JMS:SPEC:22.15; JMS:SPEC:22.16; JMS:JAVADOC:391;
	 * JMS:JAVADOC:393; JMS:JAVADOC:395; JMS:JAVADOC:397; JMS:JAVADOC:399;
	 * JMS:JAVADOC:401; JMS:JAVADOC:403; JMS:JAVADOC:405; JMS:JAVADOC:407;
	 * JMS:JAVADOC:767; JMS:JAVADOC:768; JMS:JAVADOC:769; JMS:JAVADOC:770;
	 * JMS:JAVADOC:771; JMS:JAVADOC:772; JMS:JAVADOC:773; JMS:JAVADOC:774;
	 * 
	 * 
	 * 
	 * @test_Strategy: create a message, set properties for all of the primitive
	 * types verify the conversion by getting the properties.
	 */
	@Test
	public void msgPropertiesConversionTopicTest() throws Exception {
		boolean pass = true;
		boolean bool = true;
		byte bValue = 127;
		short nShort = 10;
		int nInt = 5;
		long nLong = 333;
		float nFloat = 1;
		double nDouble = 100;
		String testString = "test";
		String testMessageBody = "Testing...";
		int ntest = 0;

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
			tool.getDefaultTopicConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultTopicSession().createTextMessage();
			messageSent.setText(testMessageBody);

			// ------------------------------------------------------------------------------
			// set properties for boolean, byte, short, int, long, float, double, and
			// String.
			// ------------------------------------------------------------------------------
			messageSent.setBooleanProperty("TESTBOOLEAN", bool);
			messageSent.setByteProperty("TESTBYTE", bValue);
			messageSent.setShortProperty("TESTSHORT", nShort);
			messageSent.setIntProperty("TESTINT", nInt);
			messageSent.setFloatProperty("TESTFLOAT", nFloat);
			messageSent.setDoubleProperty("TESTDOUBLE", nDouble);
			messageSent.setStringProperty("TESTSTRING", "test");
			messageSent.setLongProperty("TESTLONG", nLong);
			messageSent.setStringProperty("TESTSTRINGTRUE", "true");
			messageSent.setStringProperty("TESTSTRINGFALSE", "false");
			messageSent.setStringProperty("TESTSTRING1", "1");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgPropertiesConversionTopicTest");
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultTopicPublisher().publish(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (TextMessage) tool.getDefaultTopicSubscriber().receive(timeout);

			// -------------------------------------------------------------------
			// test conversions for property values
			// -------------------------------------------------------------------
			// property set as boolean can be read only as string or boolean
			// -------------------------------------------------------------------
			// valid - boolean to string
			String myBool = messageReceived.getStringProperty("TESTBOOLEAN");

			if (Boolean.valueOf(myBool).booleanValue() == bool) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from boolean to string - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from boolean to string failed");
				pass = false;
			}

			// invalid - boolean to byte
			try {
				messageReceived.getByteProperty("TESTBOOLEAN");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: boolean to byte ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException -- boolean to byte");
				pass = false;
			}

			// invalid - boolean to short
			try {
				messageReceived.getShortProperty("TESTBOOLEAN");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: boolean to short ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException -- boolean to short");
				pass = false;
			}

			// invalid - boolean to int
			try {
				messageReceived.getIntProperty("TESTBOOLEAN");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: boolean to int ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException --boolean to int ");
				pass = false;
			}

			// invalid - boolean to long
			try {
				messageReceived.getLongProperty("TESTBOOLEAN");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: boolean to long ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException -- boolean to long");
				pass = false;
			}

			// invalid - boolean to float
			try {
				messageReceived.getFloatProperty("TESTBOOLEAN");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: boolean to float ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException -- boolean to float");
				pass = false;
			}

			// invalid - boolean to double
			try {
				messageReceived.getDoubleProperty("TESTBOOLEAN");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: boolean to double ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException -- boolean to double");
				pass = false;
			}

			// -------------------------------------------------------------------
			// property set as byte can be read as a byte,short,int,long or string
			// valid - byte to string
			String myByte = messageReceived.getStringProperty("TESTBYTE");

			if (Byte.valueOf(myByte).byteValue() == bValue) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from byte to string - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from byte to string failed");
				pass = false;
			}

			// valid - byte to short
			if (messageReceived.getShortProperty("TESTBYTE") == bValue) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from byte to short - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from byte to short failed");
				pass = false;
			}

			// valid - byte to int
			if (messageReceived.getIntProperty("TESTBYTE") == bValue) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from byte to int - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from byte to int failed");
				pass = false;
			}

			// valid - byte to long
			if (messageReceived.getLongProperty("TESTBYTE") == bValue) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from byte to long - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from byte to long failed");
				pass = false;
			}

			// invalid - byte to boolean
			try {
				messageReceived.getBooleanProperty("TESTBYTE");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: byte to boolean ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException -- byte to boolean");
				pass = false;
			}

			// invalid - byte to float
			try {
				messageReceived.getFloatProperty("TESTBYTE");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: byte to float ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR, "Error: did not catch expected MessageFormatException --byte to float ");
				pass = false;
			}

			// invalid - byte to double
			try {
				messageReceived.getDoubleProperty("TESTBYTE");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: byte to double ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException -- byte to double");
				pass = false;
			}

			// -------------------------------------------------
			// property set as short can be read as short,int,long or string
			// valid - short to string
			String myshort = messageReceived.getStringProperty("TESTSHORT");

			if (Short.valueOf(myshort).shortValue() == nShort) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from short to string - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from short to string failed");
				pass = false;
			}

			// valid - short to int
			if (messageReceived.getIntProperty("TESTSHORT") == nShort) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from short to int - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from short to int failed");
				pass = false;
			}

			// valid - short to long
			if (messageReceived.getLongProperty("TESTSHORT") == nShort) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from short to long - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from short to long failed");
				pass = false;
			}

			// invalid - short to boolean
			try {
				messageReceived.getBooleanProperty("TESTSHORT");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: short to boolean ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException -- short to boolean");
				pass = false;
			}

			// invalid - short to byte
			try {
				messageReceived.getByteProperty("TESTSHORT");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: short to byte ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR, "Error: did not catch expected MessageFormatException -- short to byte");
				pass = false;
			}

			// invalid - short to float
			try {
				messageReceived.getFloatProperty("TESTSHORT");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: short to float ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException -- short to float");
				pass = false;
			}

			// invalid - short to double
			try {
				messageReceived.getDoubleProperty("TESTSHORT");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: short to double ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException -- short to double");
				pass = false;
			}

			// -------------------------------------------------
			// property set as int can be read only as int, long or string
			// valid - int to string
			if (Integer.valueOf(messageReceived.getStringProperty("TESTINT")).intValue() == nInt) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from int to string - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from int to string failed");
				pass = false;
			}

			// valid - int to long
			if (messageReceived.getLongProperty("TESTINT") == nInt) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from int to long - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from int to long failed");
				pass = false;
			}

			// invalid - int to boolean
			try {
				messageReceived.getBooleanProperty("TESTINT");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: int to boolean ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException -- int to boolean");
				pass = false;
			}

			// invalid - int to byte
			try {
				messageReceived.getByteProperty("TESTINT");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: int to byte ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR, "Error: did not catch expected MessageFormatException --  int to byte");
				pass = false;
			}

			// invalid - int to short
			try {
				messageReceived.getShortProperty("TESTINT");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: int to short ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected -- int to short ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR, "Error: did not catch expected MessageFormatException ");
				pass = false;
			}

			// invalid - int to float
			try {
				messageReceived.getFloatProperty("TESTINT");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: int to float ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR, "Error: did not catch expected MessageFormatException -- int to float");
				pass = false;
			}

			// invalid - int to double
			try {
				messageReceived.getDoubleProperty("TESTINT");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: int to double ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR, "Error: did not catch expected MessageFormatException -- int to double");
				pass = false;
			}

			// -------------------------------------------------------------------
			// property set as long can be read only as long,or a string
			// valid - long to string
			if (Long.valueOf(messageReceived.getStringProperty("TESTLONG")).longValue() == nLong) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from long to string - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from long to string failed");
				pass = false;
			}

			// invalid - long to boolean
			try {
				messageReceived.getBooleanProperty("TESTLONG");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: long to boolean ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException -- long to boolean");
				pass = false;
			}

			// invalid - long to byte
			try {
				messageReceived.getByteProperty("TESTLONG");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: long to byte ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR, "Error: did not catch expected MessageFormatException -- long to byte");
				pass = false;
			}

			// invalid - long to short
			try {
				messageReceived.getShortProperty("TESTLONG");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: long to short ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException -- long to short ");
				pass = false;
			}

			// invalid - long to int
			try {
				messageReceived.getIntProperty("TESTLONG");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: long to int ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR, "Error: did not catch expected MessageFormatException -- long to int");
				pass = false;
			}

			// invalid - long to float
			try {
				messageReceived.getFloatProperty("TESTLONG");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: long to float ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR, "Error: did not catch expected MessageFormatException -- long to float");
				pass = false;
			}

			// invalid - long to double
			try {
				messageReceived.getDoubleProperty("TESTLONG");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: long to double ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR, "Error: did not catch expected Exception -- long to double");
				pass = false;
			}

			// -------------------------------------------------------------------
			// property set as float can be read only as float,double or a string
			// valid - float to string
			if (Float.valueOf(messageReceived.getStringProperty("TESTFLOAT")).floatValue() == nFloat) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from float to string - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from float to string failed");
				pass = false;
			}

			// valid - float to double
			if (messageReceived.getDoubleProperty("TESTFLOAT") == nFloat) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from long to double - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from long to double failed");
				pass = false;
			}

			// invalid - float to boolean
			try {
				messageReceived.getBooleanProperty("TESTFLOAT");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: float to boolean ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException -- float to boolean ");
				pass = false;
			}

			// invalid - float to byte
			try {
				messageReceived.getByteProperty("TESTFLOAT");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: float to byte ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR, "Error: did not catch expected MessageFormatException -- float to byte");
				pass = false;
			}

			// invalid - float to short
			try {
				messageReceived.getShortProperty("TESTFLOAT");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: float to short ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException - float to short ");
				pass = false;
			}

			// invalid - float to int
			try {
				messageReceived.getIntProperty("TESTFLOAT");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: float to int ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR, "Error: did not catch expected MessageFormatException --- float to int");
				pass = false;
			}

			// invalid - float to long
			try {
				messageReceived.getLongProperty("TESTFLOAT");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: float to long ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException --  float to long");
				pass = false;
			}

			// -------------------------------------------------------------------
			// property set as double can be read only as double or string
			// valid - double to string
			if (Double.valueOf(messageReceived.getStringProperty("TESTDOUBLE")).doubleValue() == nDouble) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from double to string - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from double to string failed");
				pass = false;
			}

			// invalid - double to boolean
			try {
				messageReceived.getBooleanProperty("TESTDOUBLE");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: double to boolean ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException -- double to boolean ");
				pass = false;
			}

			// invalid - double to byte
			try {
				messageReceived.getByteProperty("TESTDOUBLE");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: double to byte ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException -- double to byte ");
				pass = false;
			}

			// invalid - double to short
			try {
				messageReceived.getShortProperty("TESTDOUBLE");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: double to short ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException -- double to short");
				pass = false;
			}

			// invalid - double to int
			try {
				messageReceived.getIntProperty("TESTDOUBLE");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: double to int ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException --- double to int ");
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				pass = false;
			}

			// invalid - double to long
			try {
				messageReceived.getLongProperty("TESTDOUBLE");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: double to long ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException -- double to long");
				pass = false;
			}

			// invalid - double to float
			try {
				messageReceived.getFloatProperty("TESTDOUBLE");
				logger.log(Logger.Level.INFO,
						"Error: MessageFormatException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "unsupported conversion: double to float ");
				pass = false;
			} catch (MessageFormatException me) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected error ", ee);
				logger.log(Logger.Level.ERROR,
						"Error: did not catch expected MessageFormatException -- double to float");
				pass = false;
			}

			// -------------------------------------------------------------------
			// property set as string can be read as boolean, byte, short,
			// int, long, float, double, and String.
			// valid - string to boolean
			if ((messageReceived.getBooleanProperty("TESTSTRINGTRUE")) == true) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from string to boolean - expect true - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from string to boolean - expect true  - failed");
				pass = false;
			}
			if ((messageReceived.getBooleanProperty("TESTSTRINGFALSE")) == false) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from string to boolean expect false - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from string to boolean expect false - failed");
				pass = false;
			}

			// valid - string to byte
			if (messageReceived.getByteProperty("TESTSTRING1") == 1) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from string to byte - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from string to byte failed");
				pass = false;
			}

			// valid - string to short
			if (messageReceived.getShortProperty("TESTSTRING1") == 1) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from string to short - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from string to short failed");
				pass = false;
			}

			// valid - string to int
			if (messageReceived.getIntProperty("TESTSTRING1") == 1) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from string to int - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from string to int failed");
				pass = false;
			}

			// valid - string to long
			if (messageReceived.getLongProperty("TESTSTRING1") == 1) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from string to long - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from string to long failed");
				pass = false;
			}

			// valid - string to float
			if (messageReceived.getFloatProperty("TESTSTRING1") == 1) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from string to float - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from string to float failed");
				pass = false;
			}

			// valid - string to double
			if (messageReceived.getDoubleProperty("TESTSTRING1") == 1) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from string to double - ok");
			} else {
				logger.log(Logger.Level.ERROR, "Error: conversion from string to double failed");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during property conversion tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("msgPropertiesConversionTopicTest");
		}
	}

	/*
	 * @testName: msgJMSXPropertiesTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:34; JMS:SPEC:34.4; JMS:SPEC:34.5;
	 *
	 * @test_Strategy: Set and read properties JMSXGroupID and JMSXGroupSeq. Verify
	 * the value of the properties JMSXGroupID and JMSXGroupSeq are the same as set
	 * by client.
	 */
	@Test
	public void msgJMSXPropertiesTopicTest() throws Exception {
		boolean pass = true;
		String testMessageBody = "Testing msgJMSXProperties";
		int seq = 123450;
		String id = "TestmsgJMSXProperties";

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			tool.getDefaultConnection().start();

			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultSession().createTextMessage();
			messageSent.setText(testMessageBody);

			messageSent.setStringProperty("JMSXGroupID", id);
			messageSent.setIntProperty("JMSXGroupSeq", seq);

			tool.getDefaultProducer().send(messageSent);

			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (TextMessage) tool.getDefaultConsumer().receive(timeout);
			// -------------------------------------------------------------------
			// ConnectionMetaData.getJMSXPropertyNames() method returns the
			// names of the JMSX properties supported by a connection.
			// -------------------------------------------------------------------
			try {
				ConnectionMetaData data = tool.getDefaultConnection().getMetaData();
				Enumeration cmd = data.getJMSXPropertyNames();
				String propName;

				if (cmd == null) {
					logger.log(Logger.Level.ERROR, "Error: no JMSX property names were returned!");
					pass = false;
				} else {
					int iCount = 0;

					do {
						propName = (String) cmd.nextElement();
						logger.log(Logger.Level.TRACE, propName);
						if (propName.equals("JMSXGroupID") || propName.equals("JMSXGroupSeq")) {
							iCount++;
						}
					} while (cmd.hasMoreElements());

					if (iCount > 1) {
						logger.log(Logger.Level.TRACE, "Pass:");
					} else {
						logger.log(Logger.Level.INFO, "Error: Expected property names not returned");
						pass = false;
					}
				}
			} catch (Exception ee) {
				logger.log(Logger.Level.ERROR, "Error: unexpected exception: ", ee);
				logger.log(Logger.Level.ERROR, "attempting to read JMSX property names.");
				pass = false;
			}

			if (messageReceived.getIntProperty("JMSXGroupSeq") != seq) {
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: incorrect JMSXGroupSeq value returned");
			}

			if (!messageReceived.getStringProperty("JMSXGroupID").equals(id)) {
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: incorrect JMSXGroupID value returned");
			}

			if (!pass) {
				throw new Exception("Error: failures occurred during property tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("msgJMSXPropertiesTopicTest");
		}
	}
}
