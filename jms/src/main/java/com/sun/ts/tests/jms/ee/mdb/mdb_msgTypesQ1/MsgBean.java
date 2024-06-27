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

package com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesQ1;

import java.lang.System.Logger;
import java.util.Properties;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsUtil;

import jakarta.ejb.EJBException;
import jakarta.ejb.MessageDrivenBean;
import jakarta.ejb.MessageDrivenContext;
import jakarta.jms.BytesMessage;
import jakarta.jms.JMSException;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.MessageFormatException;
import jakarta.jms.MessageListener;
import jakarta.jms.Queue;
import jakarta.jms.QueueConnection;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.QueueSender;
import jakarta.jms.QueueSession;
import jakarta.jms.TextMessage;

public class MsgBean implements MessageDrivenBean, MessageListener {

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

	private static final Logger logger = (Logger) System.getLogger(MsgBean.class.getName());

	public MsgBean() {
		logger.log(Logger.Level.TRACE, "@MsgBean()!");
	};

	public void ejbCreate() {
		logger.log(Logger.Level.TRACE, "jms.ee.mdb.mdb_msgTypesQ1  - @MsgBean-ejbCreate() !!");
		try {
			context = new TSNamingContext();
			qFactory = (QueueConnectionFactory) context.lookup("java:comp/env/jms/MyQueueConnectionFactory");
			if (qFactory == null) {
				logger.log(Logger.Level.TRACE, "qFactory error");
			}
			logger.log(Logger.Level.TRACE, "got a qFactory !!");

			queueR = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
			if (queueR == null) {
				logger.log(Logger.Level.TRACE, "queueR error");
			}

			queue = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE");
			if (queue == null) {
				logger.log(Logger.Level.TRACE, "queue error");
			}

			p = new Properties();

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new EJBException("MDB ejbCreate Error!", e);
		}
	}

	public void onMessage(Message msg) {
		JmsUtil.initHarnessProps(msg, p);
		logger.log(Logger.Level.TRACE, "from jms.ee.mdb.mdb_msgTypesQ1 @onMessage!" + msg);

		try {

			qConnection = qFactory.createQueueConnection();
			if (qConnection == null) {
				logger.log(Logger.Level.TRACE, "connection error");
			} else {
				qConnection.start();
				qSession = qConnection.createQueueSession(true, 0);
			}

			if (msg.getStringProperty("TestCase").equals("bytesMsgNullStreamQTest")) {
				bytesMsgNullStreamQTest();
			} else if (msg.getStringProperty("TestCase").equals("bytesMessageQTestsFullMsgCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - running bytesMessageQTestsFullMsg1 - create the message");
				bytesMessageQTestsFullMsgCreate();
			} else if (msg.getStringProperty("TestCase").equals("bytesMessageQTestsFullMsg")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running bytesMessageQTestsFullMsg - read and verify the message");
				bytesMessageQTestsFullMsg((jakarta.jms.BytesMessage) msg);
			} else if (msg.getStringProperty("TestCase").equals("mapMessageFullMsgQTestCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running mapMessageFullMsgQTestCreate - read and verify the message");
				mapMessageFullMsgQTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("mapMessageFullMsgQTest")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running mapMessageFullMsgQTest - read and verify the message");
				mapMessageFullMsgQTest((jakarta.jms.MapMessage) msg);
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsBooleanCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running mapMessageConversionQTestsBooleanCreate - read and verify the message");
				mapMessageConversionQTestsBooleanCreate();
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsBoolean")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running MapMessageConversionQTestsBoolean - read and verify the message");
				mapMessageConversionQTestsBoolean((jakarta.jms.MapMessage) msg);
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsByteCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running mapMessageConversionQTestsByteCreate - read and verify the message");
				mapMessageConversionQTestsByteCreate();
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsByte")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running  mapMessageConversionQTestsByte - read and verify the message");
				mapMessageConversionQTestsByte((jakarta.jms.MapMessage) msg);
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsShortCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running mapMessageConversionQTestsShortCreate - read and verify the message");
				mapMessageConversionQTestsShortCreate();
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsShort")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running  mapMessageConversionQTestsShort - read and verify the message");
				mapMessageConversionQTestsShort((jakarta.jms.MapMessage) msg);
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsCharCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running mapMessageConversionQTestsCharCreate - read and verify the message");
				mapMessageConversionQTestsCharCreate();
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsChar")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running  mapMessageConversionQTestsChar - read and verify the message");
				mapMessageConversionQTestsChar((jakarta.jms.MapMessage) msg);
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsIntCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running mapMessageConversionQTestsIntCreate - read and verify the message");
				mapMessageConversionQTestsIntCreate();
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsInt")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running  mapMessageConversionQTestsInt - read and verify the message");
				mapMessageConversionQTestsInt((jakarta.jms.MapMessage) msg);
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsLongCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running mapMessageConversionQTestsLongCreate - read and verify the message");
				mapMessageConversionQTestsLongCreate();
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsLong")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running  mapMessageConversionQTestsLong - read and verify the message");
				mapMessageConversionQTestsLong((jakarta.jms.MapMessage) msg);
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsFloatCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running mapMessageConversionQTestsFloatCreate - read and verify the message");
				mapMessageConversionQTestsFloatCreate();
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsFloat")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running  mapMessageConversionQTestsFloat - read and verify the message");
				mapMessageConversionQTestsFloat((jakarta.jms.MapMessage) msg);
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsDoubleCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running mapMessageConversionQTestsDoubleCreate - read and verify the message");
				mapMessageConversionQTestsDoubleCreate();
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsDouble")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running  mapMessageConversionQTestsDouble - read and verify the message");
				mapMessageConversionQTestsDouble((jakarta.jms.MapMessage) msg);
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsStringCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running mapMessageConversionQTestsStringCreate - read and verify the message");
				mapMessageConversionQTestsStringCreate();
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsString")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running  mapMessageConversionQTestsString - read and verify the message");
				mapMessageConversionQTestsString((jakarta.jms.MapMessage) msg);
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsBytesCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running mapMessageConversionQTestsBytesCreate - read and verify the message");
				mapMessageConversionQTestsBytesCreate();
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsBytes")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running  mapMessageConversionQTestsBytes - read and verify the message");
				mapMessageConversionQTestsBytes((jakarta.jms.MapMessage) msg);
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsInvFormatStringCreate")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running mapMessageConversionQTestsInvFormatStringCreate - read and verify the message");
				mapMessageConversionQTestsInvFormatStringCreate();
			} else if (msg.getStringProperty("TestCase").equals("mapMessageConversionQTestsInvFormatString")) {
				logger.log(Logger.Level.TRACE,
						"@onMessage - running  mapMessageConversionQTestsInvFormatString - read and verify the message");
				mapMessageConversionQTestsInvFormatString((jakarta.jms.MapMessage) msg);
			}

			else {
				logger.log(Logger.Level.TRACE, "@onMessage - invalid message type found in StringProperty");
				logger.log(Logger.Level.TRACE,
						"@onMessage - could not find method for this testcase: " + msg.getStringProperty("TestCase"));
			}
			logger.log(Logger.Level.TRACE, "@onMessage - Finished for this test!");

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
	 * BytesMessage does not support the concept of a null stream and attempting to
	 * write a null into it must throw java.lang.NullPointerException. Jms
	 * Specification 1.0.2, Section 3.12
	 *
	 * create a byte message. Use writeObject to write a null. verify a
	 * java.lang.NullPointerException is thrown.
	 * 
	 */
	private void bytesMsgNullStreamQTest() {
		BytesMessage messageSentBytesMessage = null;
		boolean ok = true;
		TextMessage msg = null;
		logger.log(Logger.Level.TRACE, "@bytesMsgNullStreamQTest");
		try {
			// create a msg sender for the response queue
			mSender = qSession.createSender(queueR);
			// and we'll send a text msg
			msg = qSession.createTextMessage();
			JmsUtil.addPropsToMessage(msg, p);
			msg.setStringProperty("TestCase", "bytesMsgNullStreamQTest");
			msg.setText("bytesMsgNullStreamQTest");

			logger.log(Logger.Level.TRACE, "Writing a null stream to byte message should throw a NullPointerException");
			messageSentBytesMessage = qSession.createBytesMessage();
			JmsUtil.addPropsToMessage(messageSentBytesMessage, p);
			// write a null to the message
			messageSentBytesMessage.writeObject(null);
			logger.log(Logger.Level.TRACE, "Fail: message did not throw NullPointerException exception as expected");
		} catch (java.lang.NullPointerException np) {
			// this is what we want
			logger.log(Logger.Level.TRACE, "Pass: NullPointerException thrown as expected");
			ok = true;
		} catch (JMSException jmsE) {
			TestUtil.printStackTrace(jmsE);
			// we did not get the anticipated exception
			logger.log(Logger.Level.TRACE, "Error: " + jmsE.getClass().getName() + " was thrown");
			ok = false;
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			// we did not get the anticipated exception here either!
			logger.log(Logger.Level.TRACE, "Error: " + e.getClass().getName() + " was thrown");
			ok = false;
		}
		try {
			if (ok)
				msg.setStringProperty("Status", "Pass");
			else
				msg.setStringProperty("Status", "Fail");
			logger.log(Logger.Level.TRACE, "Sending response message");
			mSender.send(msg);
		} catch (JMSException je) {
			TestUtil.printStackTrace(je);
			logger.log(Logger.Level.TRACE, "Error: " + je.getClass().getName() + " was thrown");
		} catch (Exception ee) {
			TestUtil.printStackTrace(ee);
			logger.log(Logger.Level.TRACE, "Error: " + ee.getClass().getName() + " was thrown");
		}
	}

	/*
	 * Description: Creates a BytesMessage -. writes to the message using each type
	 * of method and as an object. Sends the message to MDB_QUEUE Msg verified by
	 * ejb.
	 * 
	 */
	private void bytesMessageQTestsFullMsgCreate() {
		mSender = null;
		logger.log(Logger.Level.TRACE, "MsgBean - @bytesMessageQTestsFullMsgCreate");
		try {
			BytesMessage messageSent = null;
			boolean pass = true;
			boolean booleanValue = false;
			byte byteValue = 127;
			byte[] bytesValue = { 127, -127, 1, 0 };
			char charValue = 'Z';
			double doubleValue = 6.02e23;
			float floatValue = 6.02e23f;
			int intValue = 2147483647;
			long longValue = 9223372036854775807L;
			Integer nInteger = new Integer(-2147483648);
			short shortValue = -32768;
			String utfValue = "what";
			logger.log(Logger.Level.TRACE, "Creating 1 message");

			messageSent = qSession.createBytesMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "bytesMessageQTestsFullMsg");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Writing one of each primitive type to the message");
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

			// set up testcase so onMessage invokes the correct method
			messageSent.setStringProperty("TestCase", "bytesMessageQTestsFullMsg");

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			// send the message to defaultQueue

			mSender = qSession.createSender(queue);
			// send the message to another mdb handled Queue
			mSender.send(messageSent);

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: reads and verifies BytesMessage created by
	 * bytesMessageQTestsFullMsgCreate
	 */
	private void bytesMessageQTestsFullMsg(jakarta.jms.BytesMessage msg) {
		logger.log(Logger.Level.TRACE, "MsgBean - @bytesMessageQTestsFullMsg");
		String testCase = "bytesMessageQTestsFullMsg";
		try {
			BytesMessage messageSent = null;
			BytesMessage messageReceived = msg;
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

			logger.log(Logger.Level.TRACE, "Starting tests in @bytesMessageQTestsFullMsg");

			try {
				if (messageReceived.readBoolean() == booleanValue) {
					logger.log(Logger.Level.TRACE, "Pass: boolean returned ok");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: boolean not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readByte() == byteValue) {
					logger.log(Logger.Level.TRACE, "Pass: Byte returned ok");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: Byte not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			try {
				if (messageReceived.readChar() == charValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct char");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: char not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			try {
				if (messageReceived.readDouble() == doubleValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct double");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: double not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readFloat() == floatValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct float");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: float not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			try {
				if (messageReceived.readInt() == intValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct int");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: int not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readLong() == longValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct long");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: long not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			try {
				if (messageReceived.readInt() == nInteger.intValue()) {
					logger.log(Logger.Level.TRACE, "Pass: correct Integer returned");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: Integer not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			try {
				if (messageReceived.readShort() == shortValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct short");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: short not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			try {
				if (messageReceived.readUTF().equals(utfValue)) {
					logger.log(Logger.Level.TRACE, "Pass: correct UTF");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: UTF not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				int nCount = messageReceived.readBytes(bytesValueRecvd);
				for (int i = 0; i < nCount; i++) {
					if (bytesValueRecvd[i] != bytesValue[i]) {
						logger.log(Logger.Level.TRACE, "Fail: bytes value incorrect");
						pass = false;
					} else {
						logger.log(Logger.Level.TRACE, "Pass: byte value " + i + " ok");
					}
				}

			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				int nCount = messageReceived.readBytes(bytesValueRecvd);
				logger.log(Logger.Level.TRACE, "count returned " + nCount);
				if (bytesValueRecvd[0] != bytesValue[0]) {
					logger.log(Logger.Level.TRACE, "Fail: bytes value incorrect");
					pass = false;
				} else {
					logger.log(Logger.Level.TRACE, "Pass: byte value ok");
				}
				if (nCount == 1) {
					logger.log(Logger.Level.TRACE, "Pass: correct count");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: count not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			sendTestResults(testCase, pass);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 *                 
	 */
	private void mapMessageFullMsgQTestCreate() {
		boolean booleanValue = false;
		byte byteValue = 127;
		byte[] bytesValue = { 127, -127, 1, 0 };
		char charValue = 'Z';
		double doubleValue = 6.02e23;
		float floatValue = 6.02e23f;
		int intValue = 2147483647;
		long longValue = 9223372036854775807L;
		short shortValue = 32767;
		String stringValue = "Map Message Test";
		Integer integerValue = Integer.valueOf(100);
		String initial = "spring is here!";
		try {
			MapMessage messageSentMapMessage = null;

			logger.log(Logger.Level.TRACE, "Send MapMessage to Topic.");
			messageSentMapMessage = qSession.createMapMessage();
			JmsUtil.addPropsToMessage(messageSentMapMessage, p);
			messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "mapMessageFullMsgQTestCreate");

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
			// set up testcase so onMessage invokes the correct method
			messageSentMapMessage.setStringProperty("TestCase", "mapMessageFullMsgQTest");

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			// send the message to defaultQueue

			mSender = qSession.createSender(queue);
			// send the message to another mdb handled Queue
			mSender.send(messageSentMapMessage);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 *                 
	 */
	private void mapMessageFullMsgQTest(jakarta.jms.MapMessage messageReceivedMapMessage) {
		String testCase = "mapMessageFullMsgQTest";
		boolean pass = true;
		boolean booleanValue = false;
		byte byteValue = 127;
		byte[] bytesValue = { 127, -127, 1, 0 };
		char charValue = 'Z';
		double doubleValue = 6.02e23;
		float floatValue = 6.02e23f;
		int intValue = 2147483647;
		long longValue = 9223372036854775807L;
		short shortValue = 32767;
		String stringValue = "Map Message Test";
		Integer integerValue = Integer.valueOf(100);
		String initial = "spring is here!";
		try {
			try {
				if (messageReceivedMapMessage.getBoolean("booleanValue") == booleanValue) {
					logger.log(Logger.Level.TRACE, "Pass: valid boolean returned");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: invalid boolean returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE,
						"Fail: unexpected exception " + e.getClass().getName() + " was returned");
				pass = false;
			}

			try {
				if (messageReceivedMapMessage.getByte("byteValue") == byteValue) {
					logger.log(Logger.Level.TRACE, "Pass: valid byte returned");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: invalid byte returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE,
						"Fail: unexpected exception " + e.getClass().getName() + " was returned");
				pass = false;
			}

			try {
				byte[] b = messageReceivedMapMessage.getBytes("bytesValue");
				for (int i = 0; i < b.length; i++) {
					if (b[i] != bytesValue[i]) {
						logger.log(Logger.Level.TRACE, "Fail: byte array " + i + " not valid");
						pass = false;
					} else {
						logger.log(Logger.Level.TRACE, "Pass: byte array " + i + " valid");
					}
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE,
						"Fail: unexpected exception " + e.getClass().getName() + " was returned");
				pass = false;
			}

			try {
				byte[] b = messageReceivedMapMessage.getBytes("bytesValue2");
				if (b[0] != bytesValue[0]) {
					logger.log(Logger.Level.TRACE, "Fail: byte array not valid");
					pass = false;
				} else {
					logger.log(Logger.Level.TRACE, "Pass: byte array valid");
				}

			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE,
						"Fail: unexpected exception " + e.getClass().getName() + " was returned");
				pass = false;
			}

			try {
				if (messageReceivedMapMessage.getChar("charValue") == charValue) {
					logger.log(Logger.Level.TRACE, "Pass: valid char returned");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: invalid char returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE,
						"Fail: unexpected exception " + e.getClass().getName() + " was returned");
				pass = false;
			}

			try {
				if (messageReceivedMapMessage.getDouble("doubleValue") == doubleValue) {
					logger.log(Logger.Level.TRACE, "Pass: valid double returned");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: invalid double returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE,
						"Fail: unexpected exception " + e.getClass().getName() + " was returned");
				pass = false;
			}

			try {
				if (messageReceivedMapMessage.getFloat("floatValue") == floatValue) {
					logger.log(Logger.Level.TRACE, "Pass: valid float returned");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: invalid float returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE,
						"Fail: unexpected exception " + e.getClass().getName() + " was returned");
				pass = false;
			}

			try {
				if (messageReceivedMapMessage.getInt("intValue") == intValue) {
					logger.log(Logger.Level.TRACE, "Pass: valid int returned");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: invalid int returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE,
						"Fail: unexpected exception " + e.getClass().getName() + " was returned");
				pass = false;
			}

			try {
				if (messageReceivedMapMessage.getLong("longValue") == longValue) {
					logger.log(Logger.Level.TRACE, "Pass: valid long returned");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: invalid long returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE,
						"Fail: unexpected exception " + e.getClass().getName() + " was returned");
				pass = false;
			}

			try {

				if (messageReceivedMapMessage.getObject("integerValue").toString().equals(integerValue.toString())) {
					logger.log(Logger.Level.TRACE, "Pass: valid object returned");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: invalid object returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE,
						"Fail: unexpected exception " + e.getClass().getName() + " was returned");
				pass = false;
			}

			try {
				if (messageReceivedMapMessage.getShort("shortValue") == shortValue) {
					logger.log(Logger.Level.TRACE, "Pass: valid short returned");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: invalid short returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE,
						"Fail: unexpected exception " + e.getClass().getName() + " was returned");
				pass = false;
			}

			try {
				if (messageReceivedMapMessage.getString("stringValue").equals(stringValue)) {
					logger.log(Logger.Level.TRACE, "Pass: valid string returned");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: invalid string returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE,
						"Fail: unexpected exception " + e.getClass().getName() + " was returned");
				pass = false;
			}

			try {
				if (messageReceivedMapMessage.getString("nullTest") == null) {
					logger.log(Logger.Level.TRACE, "Pass: null returned");
				} else {

					logger.log(Logger.Level.TRACE, "Fail:  null not returned from getString");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE,
						"Fail: unexpected exception " + e.getClass().getName() + " was returned");
				pass = false;
			}
			sendTestResults(testCase, pass);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * 
	 * Description: Create a MapMessage -. use MapMessage method writeBoolean to
	 * write a boolean to the message.
	 */
	private void mapMessageConversionQTestsBooleanCreate() {
		try {
			MapMessage messageSent = null;
			MapMessage messageReceived = null;
			boolean booleanValue = true;
			boolean pass = true;

			// set up test tool for Queue
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = qSession.createMapMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "mapMessageConversionQTestsBooleanCreate");
			messageSent.setStringProperty("TestCase", "mapMessageConversionQTestsBoolean");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for boolean primitive type section 3.11.3");
			// -----------------------------------------------------------------------------

			messageSent.setBoolean("booleanValue", booleanValue);
			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			mSender = qSession.createSender(queue);
			mSender.send(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * 
	 * Descripton: For MapMessages Verify the proper conversion support for boolean
	 * as in 3.11.3
	 */
	private void mapMessageConversionQTestsBoolean(jakarta.jms.MapMessage messageReceived) {
		String testCase = "mapMessageConversionQTestsBoolean";
		try {
			boolean booleanValue = true;
			boolean pass = true;

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for boolean primitive type section 3.11.3");
			// -----------------------------------------------------------------------------

			// now test conversions for boolean
			// -----------------------------------------------
			// boolean to boolean - valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBoolean to read a boolean");
			try {
				if (messageReceived.getBoolean("booleanValue") == booleanValue) {
					logger.log(Logger.Level.TRACE, "Pass: boolean to boolean - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// boolean to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readString to read a boolean");
			try {
				if (messageReceived.getString("booleanValue").equals((Boolean.valueOf(booleanValue)).toString())) {
					logger.log(Logger.Level.TRACE, "Pass: boolean to string - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// boolean to byte[] invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readBytes[] to read a boolean - expect MessageFormatException");
			int nCount = 0;
			try {
				byte[] b = messageReceived.getBytes("booleanValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			logger.log(Logger.Level.TRACE, "Count returned from readBytes is : " + nCount);
			// -----------------------------------------------
			// boolean to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readByte to read a boolean - expect MessageFormatException");
			try {
				byte b = messageReceived.getByte("booleanValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// boolean to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readShort to read a boolean - expect MessageFormatException");
			try {
				short s = messageReceived.getShort("booleanValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;

			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// boolean to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readChar to read a boolean - expect MessageFormatException");
			try {
				char c = messageReceived.getChar("booleanValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// boolean to int invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readInt to read a boolean - expect MessageFormatException");
			try {
				int i = messageReceived.getInt("booleanValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// boolean to long invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readLong to read a boolean - expect MessageFormatException");
			try {
				long l = messageReceived.getLong("booleanValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// boolean to float invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readFloat to read a boolean - expect MessageFormatException");
			try {
				float f = messageReceived.getFloat("booleanValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;

			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// boolean to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use readDouble to read a boolean - expect MessageFormatException");
			try {
				double d = messageReceived.getDouble("booleanValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;

			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			sendTestResults(testCase, pass);

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	*/
	private void mapMessageConversionQTestsByteCreate() {
		MapMessage messageSent = null;
		byte byteValue = 127;
		boolean pass = true;
		try {
			messageSent = qSession.createMapMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "mapMessageConversionQTestsByteCreate");
			messageSent.setStringProperty("TestCase", "mapMessageConversionQTestsByte");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");
			// -----------------------------------------------------------------------------

			messageSent.setByte("byteValue", byteValue);
			// send the message and then get it back

			mSender = qSession.createSender(queue);
			mSender.send(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	*/
	private void mapMessageConversionQTestsByte(jakarta.jms.MapMessage messageReceived) {
		String testCase = "mapMessageConversionQTestsByte";
		MapMessage messageSent = null;
		byte byteValue = 127;
		boolean pass = true;
		try {
			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");
			// -----------------------------------------------
			// byte to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getBoolean to read a byte - this is not valid");
			try {
				boolean b = messageReceived.getBoolean("byteValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// byte to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getString to read a byte");
			try {
				if (messageReceived.getString("byteValue").equals(Byte.toString(byteValue))) {
					logger.log(Logger.Level.TRACE, "Pass: byte to string - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// byte to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getBytes[] to read a byte - expect MessageFormatException");
			int nCount = 0;
			try {
				byte[] b = messageReceived.getBytes("byteValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// byte to byte valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getByte to read a byte");
			try {
				if (messageReceived.getByte("byteValue") == byteValue) {
					logger.log(Logger.Level.TRACE, "Pass: byte to byte - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// byte to short valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getShort to read a byte");
			try {
				if (messageReceived.getShort("byteValue") == byteValue) {
					logger.log(Logger.Level.TRACE, "Pass: byte to short - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// byte to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getChar to read a boolean - this is not valid");
			try {
				char c = messageReceived.getChar("byteValue");
				pass = false;
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// byte to int valid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getInt to read a byte");
			try {
				if (messageReceived.getInt("byteValue") == byteValue) {
					logger.log(Logger.Level.TRACE, "Pass: byte to int - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// byte to long valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getLong to read a byte");
			try {
				if (messageReceived.getLong("byteValue") == byteValue) {
					logger.log(Logger.Level.TRACE, "Pass: byte to long - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// byte to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getFloat to read a boolean - this is not valid");
			try {
				float f = messageReceived.getFloat("byteValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// byte to double invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getDouble to read a boolean - this is not valid");
			try {
				double d = messageReceived.getDouble("byteValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			sendTestResults(testCase, pass);

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Create a MapMessage -. use MapMessage method writeShort to write
	 * a short.
	 */
	public void mapMessageConversionQTestsShortCreate() {
		try {
			MapMessage messageSent = null;
			short shortValue = 1;
			boolean pass = true;

			messageSent = qSession.createMapMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "MapMessageConversionQTestsShort");
			messageSent.setStringProperty("TestCase", "mapMessageConversionQTestsShort");
			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");
			// -----------------------------------------------------------------------------

			messageSent.setShort("shortValue", shortValue);
			logger.log(Logger.Level.TRACE, "Sending message");
			mSender = qSession.createSender(queue);
			mSender.send(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: MapMessage -. Verify the proper conversion support as in 3.11.3
	 */
	private void mapMessageConversionQTestsShort(jakarta.jms.MapMessage messageReceived) {
		String testCase = "mapMessageConversionQTestsShort";
		try {
			short shortValue = 1;
			boolean pass = true;

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");
			// -----------------------------------------------------------------------------

			// now test conversions for byte
			// -----------------------------------------------
			// short to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getBoolean to read a short - this is not valid");
			try {
				boolean b = messageReceived.getBoolean("shortValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// short to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getString to read a short");
			try {
				if (messageReceived.getString("shortValue").equals(Short.toString(shortValue))) {
					logger.log(Logger.Level.TRACE, "Pass: short to string - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// short to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getBytes[] to read a short - expect MessageFormatException");
			try {
				byte[] b = messageReceived.getBytes("shortValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// short to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getByte to read a short - this is not valid");
			try {
				byte b = messageReceived.getByte("shortValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// short to short valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getShort to read a short");
			try {
				if (messageReceived.getShort("shortValue") == shortValue) {
					logger.log(Logger.Level.TRACE, "Pass: short to short - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// short to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getChar to read a short - this is not valid");
			try {
				char c = messageReceived.getChar("shortValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// short to int valid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getInt to read a short");
			try {
				if (messageReceived.getInt("shortValue") == shortValue) {
					logger.log(Logger.Level.TRACE, "Pass: short to int - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// short to long valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getLong to read a short");
			try {
				if (messageReceived.getLong("shortValue") == shortValue) {
					logger.log(Logger.Level.TRACE, "Pass: short to long - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// short to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getFloat to read a short - this is not valid");
			try {
				float f = messageReceived.getFloat("shortValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// short to double invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getDouble to read a short - this is not valid");
			try {
				double d = messageReceived.getDouble("shortValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			sendTestResults(testCase, pass);

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Create a MapMessage -. use MapMessage method writeChar to write
	 * a char. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	private void mapMessageConversionQTestsCharCreate() {
		try {
			MapMessage messageSent = null;
			char charValue = 'a';
			boolean pass = true;
			messageSent = qSession.createMapMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "mapMessageConversionQTestsChar");
			messageSent.setStringProperty("TestCase", "mapMessageConversionQTestsChar");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");
			// -----------------------------------------------------------------------------
			messageSent.setChar("charValue", charValue);
			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			mSender = qSession.createSender(queue);
			mSender.send(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Create a MapMessage -. use MapMessage method writeInt to write
	 * an int. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	private void mapMessageConversionQTestsIntCreate() {
		try {
			MapMessage messageSent = null;
			int intValue = 6;
			boolean pass = true;

			// set up test tool for Queue
			messageSent = qSession.createMapMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "mapMessageConversionQTestsIntCreate");
			messageSent.setStringProperty("TestCase", "mapMessageConversionQTestsInt");
			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");
			// -----------------------------------------------------------------------------

			messageSent.setInt("intValue", intValue);
			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			mSender = qSession.createSender(queue);
			mSender.send(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Create a MapMessage -. use MapMessage method writeLong to write
	 * a long. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	private void mapMessageConversionQTestsLongCreate() {
		try {
			MapMessage messageSent = null;
			long longValue = 2;
			boolean pass = true;

			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = qSession.createMapMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "mapMessageConversionQTestsLongCreate");
			messageSent.setStringProperty("TestCase", "mapMessageConversionQTestsLong");
			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");
			// -----------------------------------------------------------------------------

			messageSent.setLong("longValue", longValue);
			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			mSender = qSession.createSender(queue);
			mSender.send(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Create a MapMessage -. use MapMessage method writeFloat to write
	 * a float. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	private void mapMessageConversionQTestsFloatCreate() {
		try {
			MapMessage messageSent = null;
			float floatValue = 5;
			boolean pass = true;

			messageSent = qSession.createMapMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "mapMessageConversionQTestsFloatCreate");
			messageSent.setStringProperty("TestCase", "mapMessageConversionQTestsFloat");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");
			// -----------------------------------------------------------------------------

			messageSent.setFloat("floatValue", floatValue);
			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			mSender = qSession.createSender(queue);
			mSender.send(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Create a MapMessage -. use MapMessage method writeDouble to
	 * write a double. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	private void mapMessageConversionQTestsDoubleCreate() {
		try {
			MapMessage messageSent = null;
			double doubleValue = 3;
			boolean pass = true;

			messageSent = qSession.createMapMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "mapMessageConversionQTestsDoubleCreate");
			messageSent.setStringProperty("TestCase", "mapMessageConversionQTestsDouble");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");
			// -----------------------------------------------------------------------------
			messageSent.setDouble("doubleValue", doubleValue);
			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			mSender = qSession.createSender(queue);
			mSender.send(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: Create a MapMessage -. use MapMessage method writeString to
	 * write a string. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	private void mapMessageConversionQTestsStringCreate() {
		try {
			MapMessage messageSent = null;
			boolean pass = true;
			String myString = "10";
			String myString2 = "true";

			messageSent = qSession.createMapMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "mapMessageConversionQTestsStringCreate");
			messageSent.setStringProperty("TestCase", "mapMessageConversionQTestsString");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");
			// -----------------------------------------------------------------------------
			messageSent.setString("myString", myString);
			messageSent.setString("myString2", myString2);
			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			mSender = qSession.createSender(queue);
			mSender.send(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * 
	 * Description: Create a MapMessage -. use MapMessage method writeBytes to write
	 * a byte[] to the message. Verify the proper conversion support as in 3.11.3
	 */
	private void mapMessageConversionQTestsBytesCreate() {
		try {
			MapMessage messageSent = null;
			byte[] byteValues = { 1, 2, 3 };
			boolean pass = true;

			messageSent = qSession.createMapMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "mapMessageConversionQTestsBytesCreate");
			messageSent.setStringProperty("TestCase", "mapMessageConversionQTestsBytes");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte[] primitive type section 3.11.3");
			// -----------------------------------------------------------------------------

			messageSent.setBytes("byteValues", byteValues);
			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			mSender = qSession.createSender(queue);
			mSender.send(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * 
	 * Description: Create a MapMessage -. use MapMessage method setString to write
	 * a text string of "mytest string". Verify NumberFormatException is thrown
	 * 
	 */
	private void mapMessageConversionQTestsInvFormatStringCreate() {
		try {
			MapMessage messageSent = null;
			boolean pass = true;
			String myString = "mytest string";

			messageSent = qSession.createMapMessage();
			JmsUtil.addPropsToMessage(messageSent, p);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "mapMessageConversionQTestsInvFormatStringCreate");
			messageSent.setStringProperty("TestCase", "mapMessageConversionQTestsInvFormatString");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");
			// -----------------------------------------------------------------------------

			messageSent.setString("myString", myString);
			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			logger.log(Logger.Level.TRACE, "Sending message");
			mSender = qSession.createSender(queue);
			mSender.send(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: use MapMessage method writeChar to write a char. Verify the
	 * proper conversion support as in 3.11.3
	 */
	private void mapMessageConversionQTestsChar(jakarta.jms.MapMessage messageReceived) {
		String testCase = "mapMessageConversionQTestsChar";
		try {
			char charValue = 'a';
			boolean pass = true;

			// now test conversions for byte
			// -----------------------------------------------
			// char to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getBoolean to read a char - this is not valid");
			try {
				boolean b = messageReceived.getBoolean("charValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// char to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getString to read a char");
			try {
				if (messageReceived.getString("charValue").equals(Character.valueOf(charValue).toString())) {
					logger.log(Logger.Level.TRACE, "Pass: char to string - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// char to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getBytes[] to read a char - expect MessageFormatException");
			try {
				byte[] b = messageReceived.getBytes("charValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// char to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getByte to read a char - this is not valid");
			try {

				byte b = messageReceived.getByte("charValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// char to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getShort to read a char");
			try {
				short s = messageReceived.getShort("charValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// char to char valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getChar to read a char ");
			try {
				if (messageReceived.getChar("charValue") == charValue) {
					logger.log(Logger.Level.TRACE, "Pass: char to char - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// char to int invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getInt to read a char ");
			try {
				int i = messageReceived.getInt("charValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// char to long invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getLong to read a char");
			try {
				long l = messageReceived.getLong("charValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// char to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getFloat to read a char - this is not valid");
			try {
				float f = messageReceived.getFloat("charValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// char to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getDouble to read a char - this is not valid");
			try {
				double d = messageReceived.getDouble("charValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			sendTestResults(testCase, pass);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: use MapMessage method writeInt to write an int. Verify the
	 * proper conversion support as in 3.11.3
	 * 
	 */
	private void mapMessageConversionQTestsInt(jakarta.jms.MapMessage messageReceived) {
		String testCase = "mapMessageConversionQTestsInt";
		try {

			int intValue = 6;
			boolean pass = true;
			// now test conversions for byte
			// -----------------------------------------------
			// int to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getBoolean to read an int - this is not valid");
			try {
				boolean b = messageReceived.getBoolean("intValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// int to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getString to read an int");
			try {
				if (messageReceived.getString("intValue").equals(Integer.toString(intValue))) {
					logger.log(Logger.Level.TRACE, "Pass: int to string - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// int to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getBytes[] to read an int - expect MessageFormatException");
			int nCount = 0;
			try {
				byte[] b = messageReceived.getBytes("intValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// int to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getByte to read an int - this is not valid");
			try {
				byte b = messageReceived.getByte("intValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// int to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getShort to read an int");
			try {
				short s = messageReceived.getShort("intValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// int to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getChar to read an int - this is not valid");
			try {
				char c = messageReceived.getChar("intValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// int to int valid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getInt to read an int");
			try {
				if (messageReceived.getInt("intValue") == intValue) {
					logger.log(Logger.Level.TRACE, "Pass: int to int - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// int to long valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getLong to read an int");
			try {
				if (messageReceived.getLong("intValue") == intValue) {
					logger.log(Logger.Level.TRACE, "Pass: int to long - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// int to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getFloat to read an int - this is not valid");
			try {
				float f = messageReceived.getFloat("intValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// int to double invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getDouble to read an int - this is not valid");
			try {
				double d = messageReceived.getDouble("intValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			sendTestResults(testCase, pass);

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: use MapMessage method writeLong to write a long. Verify the
	 * proper conversion support as in 3.11.3
	 * 
	 */
	private void mapMessageConversionQTestsLong(jakarta.jms.MapMessage messageReceived) {
		String testCase = "mapMessageConversionQTestsLong";
		try {

			long longValue = 2;
			boolean pass = true;
			// now test conversions for byte
			// -----------------------------------------------
			// long to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getBoolean to read a long - this is not valid");
			try {
				boolean b = messageReceived.getBoolean("longValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// long to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getString to read a long");
			try {
				if (messageReceived.getString("longValue").equals(Long.toString(longValue))) {
					logger.log(Logger.Level.TRACE, "Pass: long to string - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// long to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getBytes[] to read  a long - expect MessageFormatException");
			try {
				byte[] b = messageReceived.getBytes("longValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// long to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getByte to read an long - this is not valid");
			try {
				byte b = messageReceived.getByte("longValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// long to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getShort to read a long");
			try {
				short s = messageReceived.getShort("longValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// long to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getChar to read a long - this is not valid");
			try {
				char c = messageReceived.getChar("longValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// long to int invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getInt to read a long");
			try {
				int i = messageReceived.getInt("longValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// long to long valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getLong to read a long");
			try {
				if (messageReceived.getLong("longValue") == longValue) {
					logger.log(Logger.Level.TRACE, "Pass: int to long - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// long to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getFloat to read a long - this is not valid");
			try {
				float f = messageReceived.getFloat("longValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// long to double invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getDouble to read a long ");
			try {
				double d = messageReceived.getDouble("longValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			sendTestResults(testCase, pass);

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: use MapMessage method writeFloat to write a float. Verify the
	 * proper conversion support as in 3.11.3
	 * 
	 */
	private void mapMessageConversionQTestsFloat(jakarta.jms.MapMessage messageReceived) {
		String testCase = "mapMessageConversionQTestsFloat";
		try {
			float floatValue = 5;
			boolean pass = true;
			// now test conversions for byte
			// -----------------------------------------------
			// float to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getBoolean to read a float  ");
			try {
				boolean b = messageReceived.getBoolean("floatValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// float to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getString to read a float");
			try {
				if (messageReceived.getString("floatValue").equals(Float.toString(floatValue))) {
					logger.log(Logger.Level.TRACE, "Pass: float to string - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// float to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getBytes[] to read  a float ");
			try {
				byte[] b = messageReceived.getBytes("floatValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// float to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getByte to read a float  ");
			try {
				byte b = messageReceived.getByte("floatValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// float to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getShort to read a float");
			try {
				short s = messageReceived.getShort("floatValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// float to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getChar to read a long  ");
			try {
				char c = messageReceived.getChar("floatValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// float to int invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getInt to read a float");
			try {
				int i = messageReceived.getInt("floatValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// float to long invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getLong to read a long");
			try {
				long l = messageReceived.getLong("floatValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// float to float valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getFloat to read a float  ");
			try {
				if (messageReceived.getFloat("floatValue") == floatValue) {
					logger.log(Logger.Level.TRACE, "Pass: float to float - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// float to double valid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getDouble to read a float  ");
			try {
				if (messageReceived.getDouble("floatValue") == floatValue) {
					logger.log(Logger.Level.TRACE, "Pass: float to double - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			sendTestResults(testCase, pass);

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: use MapMessage method writeDouble to write a double. Verify the
	 * proper conversion support as in 3.11.3
	 * 
	 */
	private void mapMessageConversionQTestsDouble(jakarta.jms.MapMessage messageReceived) {
		String testCase = "mapMessageConversionQTestsDouble";

		try {

			double doubleValue = 3;
			boolean pass = true;
			// now test conversions for byte
			// -----------------------------------------------
			// double to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getBoolean to read a double  ");
			try {
				boolean b = messageReceived.getBoolean("doubleValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// double to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getString to read a double");
			try {
				if (messageReceived.getString("doubleValue").equals(Double.toString(doubleValue))) {
					logger.log(Logger.Level.TRACE, "Pass: double to string");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// double to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getBytes[] to read  a double ");
			try {
				byte[] b = messageReceived.getBytes("doubleValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// double to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getByte to read a double  ");
			try {
				byte b = messageReceived.getByte("doubleValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// double to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getShort to read a double");
			try {
				short s = messageReceived.getShort("doubleValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// double to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getChar to read a double  ");
			try {
				char c = messageReceived.getChar("doubleValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// double to int invalid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getInt to read a double");
			try {
				int i = messageReceived.getInt("doubleValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// double to long invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getLong to read a double");
			try {
				long l = messageReceived.getLong("doubleValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// double to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getFloat to read a double  ");
			try {
				float f = messageReceived.getFloat("doubleValue");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// double to double valid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getDouble to read an float  ");
			try {
				if (messageReceived.getDouble("doubleValue") == doubleValue) {
					logger.log(Logger.Level.TRACE, "Pass: double to double ");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			sendTestResults(testCase, pass);

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: use MapMessage method writeString to write a string. Verify the
	 * proper conversion support as in 3.11.3
	 * 
	 */
	private void mapMessageConversionQTestsString(jakarta.jms.MapMessage messageReceived) {
		String testCase = "mapMessageConversionQTestsString";

		try {

			boolean pass = true;
			String myString = "10";
			String myString2 = "true";
			// now test conversions for String
			// -----------------------------------------------
			// string to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getString to read a String");
			try {
				if (messageReceived.getString("myString").equals(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: string to string - valid");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// string to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getBytes[] to read a String");
			try {
				byte[] b = messageReceived.getBytes("myString");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// String to byte valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getByte to read a String");
			try {
				if (messageReceived.getByte("myString") == Byte.parseByte(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to byte ");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// string to short valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getShort to read a string");
			try {
				if (messageReceived.getShort("myString") == Short.parseShort(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to short ");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// String to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getChar to read a String ");
			try {
				char c = messageReceived.getChar("myString");
				logger.log(Logger.Level.TRACE, "getChar returned " + c);
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// string to int valid
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getInt to read a String");
			try {
				if (messageReceived.getInt("myString") == Integer.parseInt(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to int ");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// string to long valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getLong to read a String");
			try {
				if (messageReceived.getLong("myString") == Long.parseLong(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to long ");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// String to float valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getFloat to read a String");
			try {
				if (messageReceived.getFloat("myString") == Float.parseFloat(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to float ");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// String to double valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getDouble to read a String");
			try {

				if (messageReceived.getDouble("myString") == Double.parseDouble(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to double ");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// String to boolean
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getBoolean to read a string ");
			try {
				if (messageReceived.getBoolean("myString2") == Boolean.valueOf(myString2).booleanValue()) {
					logger.log(Logger.Level.TRACE, "Pass: String to boolean ");
				} else {
					logger.log(Logger.Level.TRACE, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// String to boolean
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getBoolean to read a string that is not true");
			try {
				boolean b = messageReceived.getBoolean("myString");
				if (b != false) {
					logger.log(Logger.Level.TRACE, "Fail: !true should have returned false");
					pass = false;
				} else {
					logger.log(Logger.Level.TRACE, "Pass: !true returned false");
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			sendTestResults(testCase, pass);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: use MapMessage method writeBytes to write a byte[] to the
	 * message. Verify the proper conversion support as in 3.11.3
	 */
	private void mapMessageConversionQTestsBytes(jakarta.jms.MapMessage messageReceived) {
		String testCase = "mapMessageConversionQTestsBytes";

		try {

			byte[] byteValues = { 1, 2, 3 };
			boolean pass = true;

			// now test conversions for boolean
			// -----------------------------------------------
			// byte[] to byte[] - valid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getBytes[] to read a byte[] ");
			try {
				byte[] b = messageReceived.getBytes("byteValues");
				for (int i = 0; i < b.length; i++) {
					if (b[i] != byteValues[i]) {
						logger.log(Logger.Level.TRACE, "Fail: byte[] value returned is invalid");
						pass = false;
					} else {
						logger.log(Logger.Level.TRACE, "Pass: byte[] returned is valid");
					}
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// byte[] to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getBoolean to read a byte[]");
			try {
				boolean b = messageReceived.getBoolean("byteValues");
				logger.log(Logger.Level.TRACE,
						"Fail: byte[] to boolean conversion should have thrown MessageFormatException");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// byte[] to string invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getString to read a byte[]");
			try {
				String s = messageReceived.getString("byteValues");
				logger.log(Logger.Level.TRACE,
						"Fail: byte[] to boolean conversion should have thrown MessageFormatException");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// byte[] to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getByte to read a byte[] - expect MessageFormatException");
			try {
				byte b = messageReceived.getByte("byteValues");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// byte[] to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getShort to read a byte[] - expect MessageFormatException");
			try {
				short s = messageReceived.getShort("byteValues");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// byte[] to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getChar to read a byte[] - expect MessageFormatException");
			try {

				char c = messageReceived.getChar("byteValues");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// byte[] to int invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getInt to read a byte[] - expect MessageFormatException");
			try {
				int i = messageReceived.getInt("byteValues");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// byte[] to long invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getLong to read a byte[] - expect MessageFormatException");
			try {
				long l = messageReceived.getLong("byteValues");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// byte[] to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getFloat to read a byte[] - expect MessageFormatException");
			try {
				float f = messageReceived.getFloat("byteValues");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// byte[] to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getDouble to read a byte[] - expect MessageFormatException");
			try {
				double d = messageReceived.getDouble("byteValues");
				logger.log(Logger.Level.TRACE, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (MessageFormatException mf) {
				logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			sendTestResults(testCase, pass);

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: use MapMessage method setString to write a text string of
	 * "mytest string". Verify NumberFormatException is thrown
	 * 
	 */
	private void mapMessageConversionQTestsInvFormatString(jakarta.jms.MapMessage messageReceived) {
		String testCase = "mapMessageConversionQTestsInvFormatString";

		try {
			boolean pass = true;
			String myString = "mytest string";

			// -----------------------------------------------
			// String to byte
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getByte to read a String that is not valid ");
			try {
				byte b = messageReceived.getByte("myString");
				logger.log(Logger.Level.TRACE, "Fail: java.lang.NumberFormatException expected");
				pass = false;
			} catch (NumberFormatException nf) {
				logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			// -----------------------------------------------
			// string to short
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getShort to read a string that is not valid ");
			try {
				short s = messageReceived.getShort("myString");
				logger.log(Logger.Level.TRACE, "Fail: NumberFormatException was expected");
				pass = false;
			} catch (NumberFormatException nf) {
				logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// string to int
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getInt to read a String that is not valid ");
			try {
				int i = messageReceived.getInt("myString");
				logger.log(Logger.Level.TRACE, "Fail: NumberFormatException was expected");
				pass = false;
			} catch (NumberFormatException nf) {
				logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// string to long
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getLong to read a String that is not valid ");
			try {
				long l = messageReceived.getLong("myString");
				logger.log(Logger.Level.TRACE, "Fail: NumberFormatException was expected");
				pass = false;
			} catch (NumberFormatException nf) {
				logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// String to float
			// -----------------------------------------------
			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getFloat to read a String that is not valid ");
			try {
				float f = messageReceived.getFloat("myString");
				logger.log(Logger.Level.TRACE, "Fail: NumberFormatException was expected");
				pass = false;
			} catch (NumberFormatException nf) {
				logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// String to double
			// -----------------------------------------------

			logger.log(Logger.Level.TRACE, "--");
			logger.log(Logger.Level.TRACE, "Use getDouble to read a String that is not valid ");
			try {
				double d = messageReceived.getDouble("myString");
				logger.log(Logger.Level.TRACE, "Fail: NumberFormatException was expected");
				pass = false;
			} catch (NumberFormatException nf) {
				logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Error: Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			sendTestResults(testCase, pass);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
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

			logger.log(Logger.Level.TRACE, "Sending response message");
			logger.log(Logger.Level.TRACE, "==================================Test Results from: " + testCase);
			logger.log(Logger.Level.TRACE, "==================================Status: " + results);
			mSender.send(msg);
		} catch (JMSException je) {
			TestUtil.printStackTrace(je);
			logger.log(Logger.Level.TRACE, "Error: " + je.getClass().getName() + " was thrown");
		} catch (Exception ee) {
			TestUtil.printStackTrace(ee);
			logger.log(Logger.Level.TRACE, "Error: " + ee.getClass().getName() + " was thrown");
		}
	}

	public void setMessageDrivenContext(MessageDrivenContext mdc) {
		logger.log(Logger.Level.TRACE, "jms.ee.mdb.mdb_msgTypesQ1  In MsgBean::setMessageDrivenContext()!!");
		this.mdc = mdc;
	}

	public void ejbRemove() {
		logger.log(Logger.Level.TRACE, "jms.ee.mdb.mdb_msgTypesQ1  In MsgBean::remove()!!");
	}
}
