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

package com.sun.ts.tests.jms.ee.mdb.mdb_msgPropsQ;

import java.lang.System.Logger;
import java.util.Enumeration;
import java.util.Properties;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsUtil;

import jakarta.ejb.EJBException;
import jakarta.ejb.MessageDrivenBean;
import jakarta.ejb.MessageDrivenContext;
import jakarta.jms.ConnectionMetaData;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageFormatException;
import jakarta.jms.MessageListener;
import jakarta.jms.MessageNotWriteableException;
import jakarta.jms.Queue;
import jakarta.jms.QueueConnection;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.QueueSender;
import jakarta.jms.QueueSession;
import jakarta.jms.TextMessage;

public class MsgBeanMsgTestPropsQ implements MessageDrivenBean, MessageListener {

	// properties object needed for logging, get this from the message object
	// passed into
	// the onMessage method.
	private java.util.Properties p = null;

	private TSNamingContext context = null;

	private MessageDrivenContext mdc = null;

	// JMS
	private QueueConnectionFactory qFactory = null;

	private QueueConnection qConnection = null;

	private Queue queueR = null;

	private Queue queue = null;

	private QueueSender mSender = null;

	private QueueSession qSession = null;

	private TextMessage messageSent = null;

	private static final Logger logger = (Logger) System.getLogger(MsgBeanMsgTestPropsQ.class.getName());

	public MsgBeanMsgTestPropsQ() {
		System.out.println("@MsgBeanMsgTestPropsQ()!");
	};

	public void ejbCreate() {
		System.out.println("jms.ee.mdb.mdb_msgPropsQ  - @MsgBeanMsgTestPropsQ-ejbCreate() !!");
		try {
			context = new TSNamingContext();
			qFactory = (QueueConnectionFactory) context.lookup("java:comp/env/jms/MyQueueConnectionFactory");
			if (qFactory == null) {
				System.out.println("qFactory error");
			}
			System.out.println("got a qFactory !!");

			queueR = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
			if (queueR == null) {
				System.out.println("queueR error");
			}
			queue = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE");

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new EJBException("MDB ejbCreate Error!", e);
		}

		// Properties
		p = new Properties();

	}

	public void onMessage(Message msg) {
		System.out.println("from jms.ee.mdb.mdb_msgPropsQ @onMessage!" + msg);

		try {
			JmsUtil.initHarnessProps(msg, p);

			System.out.println("onMessage will run TestCase: " + msg.getStringProperty("TestCase"));
			qConnection = qFactory.createQueueConnection();
			if (qConnection == null) {
				System.out.println("connection error");
			} else {
				qConnection.start();
				qSession = qConnection.createQueueSession(true, 0);
			}

			messageSent = qSession.createTextMessage();
			JmsUtil.addPropsToMessage(messageSent, p);

			if (msg.getStringProperty("TestCase").equals("msgPropertiesQTestCreate")) {
				System.out.println("@onMessage - running msgPropertiesQTestCreate - create the message");
				msgPropertiesQTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgPropertiesQTest")) {
				System.out.println("@onMessage - msgPropertiesQTest!");
				msgPropertiesQTest(msg);
			} else if (msg.getStringProperty("TestCase").equals("msgPropertiesConversionQTestCreate")) {
				System.out.println("@onMessage - msgPropertiesConversionQTestCreate!");
				msgPropertiesConversionQTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgPropertiesConversionQTest")) {
				System.out.println("@onMessage - msgPropertiesConversionQTest!");
				msgPropertiesConversionQTest(msg);
			}

			else {
				System.out.println("@onMessage - invalid message type found in StringProperty");
				System.out.println("Do not have a method for this testcase: " + msg.getStringProperty("TestCase"));
			}

			System.out.println("@onMessage - Finished for this test!");
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		} finally {
			if (qConnection != null) {
				try {
					qConnection.close();
				} catch (Exception e) {
					TestUtil.printStackTrace(e);
				}
			}
		}

	}

	/*
	 *
	 */

	private void msgPropertiesQTestCreate() {
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

		int NUMPROPS = 0;
		String testMessageBody = "Testing...";

		JmsUtil.addPropsToMessage(messageSent, p);
		try {

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

			// ------------------------------------------------------------------------------
			// set properties for Boolean, Byte, Short, Int, Long, Float, Double, and
			// String.
			// ------------------------------------------------------------------------------

			messageSent.setObjectProperty("OBJTESTBOOLEAN", Boolean.valueOf(bool));
			messageSent.setObjectProperty("OBJTESTBYTE", Byte.valueOf(bValue));
			messageSent.setObjectProperty("OBJTESTSHORT", Short.valueOf(nShort));
			messageSent.setObjectProperty("OBJTESTINT", Integer.valueOf(nInt));
			messageSent.setObjectProperty("OBJTESTFLOAT", new Float(nFloat));
			messageSent.setObjectProperty("OBJTESTDOUBLE", new Double(nDouble));
			messageSent.setObjectProperty("OBJTESTSTRING", "test");
			messageSent.setObjectProperty("OBJTESTLONG", new Long(nLong));

			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgPropertiesQTest");
			messageSent.setStringProperty("TestCase", "msgPropertiesQTest");

			// count total properties in messageSent

			Enumeration e = messageSent.getPropertyNames();
			String key = null;
			int i = 0;
			while (e.hasMoreElements()) {
				key = (String) e.nextElement();
				logger.log(Logger.Level.TRACE, "+++++++   Property Name is: " + key);
				if (key.indexOf("JMS") != 0)
					i++;
			}

			// set Count for properties to pass + 1 for count itself
			i++;
			messageSent.setIntProperty("NUMPROPS", i);

			logger.log(Logger.Level.TRACE, "Sending message");
			mSender = qSession.createSender(queue);
			mSender.send(messageSent);
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "from call to setObjectProperty!");
				logger.log(Logger.Level.INFO, ee.getMessage());
				pass = false;
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	private void msgPropertiesQTest(jakarta.jms.Message msg) {

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

		String testCase = "msgPropertiesQTest";
		String testMessageBody = "Testing...";
		TextMessage messageReceived = (TextMessage) msg;
		try {
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

			int numPropertyNames = (int) messageReceived.getIntProperty("NUMPROPS");
			// iterate thru the property names
			int i = 0;
			propertyNames = messageReceived.getPropertyNames();
			do {
				String tmp = (String) propertyNames.nextElement();
				logger.log(Logger.Level.TRACE, "+++++++   Property Name is: " + tmp);
				if (tmp.indexOf("JMS") != 0)
					i++;
			} while (propertyNames.hasMoreElements());

			if (i == numPropertyNames) {
				logger.log(Logger.Level.TRACE, "Pass: # of properties is " + numPropertyNames + " as expected");
			} else {
				logger.log(Logger.Level.INFO, "Error: expected " + numPropertyNames + "property names");
				logger.log(Logger.Level.INFO, "       But " + i + " returned");
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
			// --------------------------------------------------------------------------------
			// The other property get methods ( other than getStringProperty and
			// getObjectProperty) must throw a java.lang.NullPointerException if
			// they are used to get a non-existent property.
			// --------------------------------------------------------------------------------
			/*
			 * ifc try { boolean value = messageReceived.getBooleanProperty("TESTDUMMY");
			 * TestUtil. logger.log(Logger.Level.
			 * INFO,"Error: NullPointerException should have occurred for getBooleanProperty"
			 * ); pass = false; } catch (java.lang.NullPointerException np) {
			 * logger.log(Logger.Level.TRACE,"Pass: NullPointerException as expected ");}
			 * 
			 * try { byte value = messageReceived.getByteProperty("TESTDUMMY"); TestUtil.
			 * logger.log(Logger.Level.
			 * INFO,"Error: NullPointerException should have occurred for getByteProperty"
			 * ); pass = false; } catch (java.lang.NullPointerException np) {
			 * logger.log(Logger.Level.TRACE,"Pass: NullPointerException as expected ");}
			 * 
			 * try { short value = messageReceived.getShortProperty("TESTDUMMY"); TestUtil.
			 * logger.log(Logger.Level.
			 * INFO,"Error: NullPointerException should have occurred for getShortProperty"
			 * ); pass = false; } catch (java.lang.NullPointerException np) {
			 * logger.log(Logger.Level.TRACE,"Pass: NullPointerException as expected ");}
			 * 
			 * try { int value = messageReceived.getIntProperty("TESTDUMMY"); TestUtil.
			 * logger.log(Logger.Level.
			 * INFO,"Error: NullPointerException should have occurred for getIntProperty" );
			 * pass = false; } catch (java.lang.NullPointerException np) {
			 * logger.log(Logger.Level.TRACE,"Pass: NullPointerException as expected ");}
			 * 
			 * try { long value = messageReceived.getLongProperty("TESTDUMMY"); TestUtil.
			 * logger.log(Logger.Level.
			 * INFO,"Error: NullPointerException should have occurred for getLongProperty"
			 * ); pass = false; } catch (java.lang.NullPointerException np) {
			 * logger.log(Logger.Level.TRACE,"Pass: NullPointerException as expected ");}
			 * 
			 * try { float value = messageReceived.getFloatProperty("TESTDUMMY"); TestUtil.
			 * logger.log(Logger.Level.
			 * INFO,"Error: NullPointerException should have occurred for getFloatProperty"
			 * ); pass = false; } catch (java.lang.NullPointerException np) {
			 * logger.log(Logger.Level.TRACE,"Pass: NullPointerException as expected ");}
			 * 
			 * try { double value = messageReceived.getDoubleProperty("TESTDUMMY");
			 * TestUtil. logger.log(Logger.Level.
			 * INFO,"Error: NullPointerException should have occurred for getDoubleProperty"
			 * ); pass = false; } catch (java.lang.NullPointerException np) {
			 * logger.log(Logger.Level.TRACE,"Pass: NullPointerException as expected ");}
			 */

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
				logger.log(Logger.Level.INFO, "Error: NullPointerException should have occurred for getShortProperty");
				logger.log(Logger.Level.INFO, "Properties have been cleared!");
				pass = false;
			} catch (java.lang.NullPointerException np) {
				TestUtil.printStackTrace(np);
				logger.log(Logger.Level.TRACE, "Pass: NullPointerException");
			} catch (java.lang.NumberFormatException nf) {
				logger.log(Logger.Level.TRACE, "Pass: NumberFormatException as expected ");
			}

			// -------------------------------------------------------------------
			// A message's properties are deleted by the clearProperties method.
			// This leaves the message with an empty set of properties.
			// New property entries can then be both created and read.
			// -------------------------------------------------------------------
			try {
				String newEntry = "new entry";
				logger.log(Logger.Level.TRACE, "Verify that you can set a property after clearProperty");
				messageReceived.setStringProperty("TESTSTRING", "new entry");
				if (messageReceived.getStringProperty("TESTSTRING").equals(newEntry)) {
					logger.log(Logger.Level.TRACE, "Pass: able to set a cleared property");
				} else {
					logger.log(Logger.Level.TRACE, "Error: was not able to new property setting after clearProperty");
					pass = false;
				}
			} catch (Exception ex) {
				logger.log(Logger.Level.TRACE, "Unexpected Exception caught while testing set after clearProperty");
				pass = false;
				TestUtil.printStackTrace(ex);
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
				ConnectionMetaData data = qConnection.getMetaData();
				Enumeration cmd = data.getJMSXPropertyNames();
				String propName;
				if (cmd == null) {
					logger.log(Logger.Level.INFO, "Error: no JMSX property names were returned!");
					logger.log(Logger.Level.INFO, "expected JMSXGroupID and JMSXGroupSeq at a miniumum");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, "attempting to read JMSX property names.");
				logger.log(Logger.Level.INFO,
						"Error: unexpected exception: " + ee.getClass().getName() + " was thrown");
				pass = false;
			}

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			pass = false;
		} finally {
			sendTestResults(testCase, pass);
		}
	}

	private void msgPropertiesConversionQTestCreate() {
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

			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgPropertiesConversionQTest");
			messageSent.setStringProperty("TestCase", "msgPropertiesConversionQTest");
			logger.log(Logger.Level.TRACE, "Sending message");

			mSender = qSession.createSender(queue);
			mSender.send(messageSent);
			// ------------------------------------------------------------------------------
			// An attempt to use any other class than Boolean, Byte, Short, Int, Long,
			// Float,
			// Double, and Stringmust throw a JMS MessageFormatException.
			// ------------------------------------------------------------------------------
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	private void msgPropertiesConversionQTest(jakarta.jms.Message messageReceived) {
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
		String testCase = "msgPropertiesConversionQTest";
		try {

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
				logger.log(Logger.Level.INFO, "Error: conversion from boolean to string failed");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				// logger.log(Logger.Level.INFO,"Error: unexpected error " +
				// ee.getClass().getName()
				// + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- boolean to byte");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- boolean to short");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception --boolean to int ");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- boolean to long");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- boolean to float");
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
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- boolean to double");
				pass = false;
			}

			// -------------------------------------------------------------------
			// property set as byte can be read as a byte,short,int,long or string

			// valid - byte to string
			String myByte = messageReceived.getStringProperty("TESTBYTE");
			if (Byte.valueOf(myByte).byteValue() == bValue) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from byte to string - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from byte to string failed");
				pass = false;
			}
			// valid - byte to short
			if (messageReceived.getShortProperty("TESTBYTE") == bValue) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from byte to short - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from byte to short failed");
				pass = false;
			}
			// valid - byte to int
			if (messageReceived.getIntProperty("TESTBYTE") == bValue) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from byte to int - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from byte to int failed");
				pass = false;
			}
			// valid - byte to long

			if (messageReceived.getLongProperty("TESTBYTE") == bValue) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from byte to long - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from byte to long failed");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- byte to boolean");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception --byte to float ");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- byte to double");
				pass = false;
			}

			// -------------------------------------------------
			// property set as short can be read as short,int,long or string

			// valid - short to string
			String myshort = messageReceived.getStringProperty("TESTSHORT");
			if (Short.valueOf(myshort).shortValue() == nShort) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from short to string - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from short to string failed");
				pass = false;
			}

			// valid - short to int
			if (messageReceived.getIntProperty("TESTSHORT") == nShort) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from short to int - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from short to int failed");
				pass = false;
			}
			// valid - short to long
			if (messageReceived.getLongProperty("TESTSHORT") == nShort) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from short to long - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from short to long failed");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- short to boolean");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- short to byte");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- short to float");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- short to double");
				pass = false;
			}

			// -------------------------------------------------
			// property set as int can be read only as int, long or string

			// valid - int to string
			if (Integer.valueOf(messageReceived.getStringProperty("TESTINT")).intValue() == nInt) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from int to string - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from int to string failed");
				pass = false;
			}

			// valid - int to long
			if (messageReceived.getLongProperty("TESTINT") == nInt) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from int to long - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from int to long failed");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- int to boolean");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception --  int to byte");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception ");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- int to float");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- int to double");
				pass = false;
			}

			// -------------------------------------------------------------------
			// property set as long can be read only as long,or a string

			// valid - long to string
			if (Long.valueOf(messageReceived.getStringProperty("TESTLONG")).longValue() == nLong) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from long to string - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from long to string failed");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- long to boolean");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- long to byte");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- long to short ");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- long to int");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- long to float");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- long to double");
				pass = false;
			}

			// -------------------------------------------------------------------
			// property set as float can be read only as float,double or a string

			// valid - float to string
			if (Float.valueOf(messageReceived.getStringProperty("TESTFLOAT")).floatValue() == nFloat) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from float to string - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from float to string failed");
				pass = false;
			}
			// valid - float to double
			if (messageReceived.getDoubleProperty("TESTFLOAT") == nFloat) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from long to double - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from long to double failed");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- float to boolean ");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- float to byte");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception - float to short ");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception --- float to int");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception --  float to long");
				pass = false;
			}

			// -------------------------------------------------------------------
			// property set as double can be read only as double or string

			// valid - double to string
			if (Double.valueOf(messageReceived.getStringProperty("TESTDOUBLE")).doubleValue() == nDouble) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from double to string - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from double to string failed");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- double to boolean ");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- double to byte ");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- double to short");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception --- double to int ");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- double to long");
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
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.INFO, ee.getMessage());
				logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				logger.log(Logger.Level.INFO, "Error: did not catch expected Exception -- double to float");
				pass = false;
			}
			// -------------------------------------------------------------------
			// property set as string can be read as boolean, byte, short,
			// int, long, float, double, and String.

			// valid - string to boolean
			if ((messageReceived.getBooleanProperty("TESTSTRINGTRUE")) == true) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from string to boolean - expect true - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from string to boolean - expect true  - failed");
				pass = false;
			}

			if ((messageReceived.getBooleanProperty("TESTSTRINGFALSE")) == false) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from string to boolean expect false - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from string to boolean expect false - failed");
				pass = false;
			}

			// valid - string to byte
			if (messageReceived.getByteProperty("TESTSTRING1") == 1) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from string to byte - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from string to byte failed");
				pass = false;
			}
			// valid - string to short
			if (messageReceived.getShortProperty("TESTSTRING1") == 1) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from string to short - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from string to short failed");
				pass = false;
			}
			// valid - string to int
			if (messageReceived.getIntProperty("TESTSTRING1") == 1) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from string to int - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from string to int failed");
				pass = false;
			}
			// valid - string to long
			if (messageReceived.getLongProperty("TESTSTRING1") == 1) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from string to long - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from string to long failed");
				pass = false;
			}

			// valid - string to float
			if (messageReceived.getFloatProperty("TESTSTRING1") == 1) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from string to float - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from string to float failed");
				pass = false;
			}
			// valid - string to double
			if (messageReceived.getDoubleProperty("TESTSTRING1") == 1) {
				logger.log(Logger.Level.TRACE, "Pass: conversion from string to double - ok");
			} else {
				logger.log(Logger.Level.INFO, "Error: conversion from string to double failed");
				pass = false;
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		} finally {
			sendTestResults(testCase, pass);
		}
	}

	/*
	 * Description: send test results to response queue (MDB_QUEUE_REPLY) for
	 * verification
	 */
	private void sendTestResults(String testCase, boolean results) {
		TextMessage msg = null;

		try {
			// create a msg sender for the response queue
			mSender = qSession.createSender(queueR);
			// and we'll send a text msg
			msg = qSession.createTextMessage();
			msg.setStringProperty("TestCase", testCase);
			msg.setText(testCase);
			if (results)
				msg.setStringProperty("Status", "Pass");
			else
				msg.setStringProperty("Status", "Fail");

			System.out.println("Sending response message");
			System.out.println("==================================Test Results from: " + testCase);
			System.out.println("==================================Status: " + results);
			mSender.send(msg);
		} catch (JMSException je) {
			TestUtil.printStackTrace(je);
			System.out.println("Error: " + je.getClass().getName() + " was thrown");
		} catch (Exception ee) {
			TestUtil.printStackTrace(ee);
			System.out.println("Error: " + ee.getClass().getName() + " was thrown");
		}
	}

	public void setMessageDrivenContext(MessageDrivenContext mdc) {
		System.out.println("jms.ee.mdb.mdb_msgPropsQ  In MsgBeanMsgTestPropsQ::setMessageDrivenContext()!!");
		this.mdc = mdc;
	}

	public void ejbRemove() {
		System.out.println("jms.ee.mdb.mdb_msgPropsQ  In MsgBeanMsgTestPropsQ::remove()!!");
	}
}
