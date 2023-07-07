/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jms.core20.jmsproducertopictests;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.Serializable;
import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.BytesMessage;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.ConnectionMetaData;
import jakarta.jms.DeliveryMode;
import jakarta.jms.Destination;
import jakarta.jms.JMSConsumer;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSProducer;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.MessageFormatException;
import jakarta.jms.MessageFormatRuntimeException;
import jakarta.jms.MessageNotWriteableRuntimeException;
import jakarta.jms.ObjectMessage;
import jakarta.jms.StreamMessage;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;

public class ClientIT {
	private static final String testName = "com.sun.ts.tests.jms.core20.jmsproducertopictests.ClientIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(ClientIT.class.getName());

	// JMS tool which creates and/or looks up the JMS administered objects
	private transient JmsTool tool = null, tool2 = null;

	// JMS objects
	private transient ConnectionFactory cf = null;

	private transient ConnectionFactory cf2 = null;

	private transient Topic topic = null;

	private transient Destination destination = null;

	private transient Topic topic2 = null;

	private transient Destination destination2 = null;

	private transient JMSContext context = null;

	private transient JMSContext context2 = null;

	private transient JMSProducer producer = null;

	private transient JMSProducer producer2 = null;

	private transient JMSConsumer consumer = null;

	private transient JMSConsumer consumer2 = null;

	// Harness req's
	private Properties props = null;

	// properties read
	long timeout;

	String user;

	String password;

	String mode;

	// used for tests
	private static final int numMessages = 3;

	private static final int iterations = 5;

	ArrayList connections = null;

	/* Utility methods for tests */

	/*
	 * helper method verifies that the ConnectionMetaData
	 * 
	 * @param ConnectionMetaData returned from getJMSMessageID
	 * 
	 * @return boolean true if ConnectionMetaData is as expected
	 */
	private boolean verifyMetaData(ConnectionMetaData data) {
		boolean pass = true;

		try {
			String tmp = data.getJMSVersion();
			logger.log(Logger.Level.TRACE, "JMSVersion=" + tmp);

			if (!tmp.equals("2.0")) {
				logger.log(Logger.Level.ERROR, "Error: incorrect JMSVersion=" + tmp);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error: incorrect type returned for JMSVersion: ", e);
			pass = false;
		}

		try {
			int tmp = data.getJMSMajorVersion();
			logger.log(Logger.Level.TRACE, "JMSMajorVersion=" + tmp);

			if (tmp != 2) {
				logger.log(Logger.Level.ERROR, "Error: incorrect JMSMajorVersion=" + tmp);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error: incorrect type returned for JMSMajorVersion: ", e);
			pass = false;
		}

		try {
			int tmp = data.getJMSMinorVersion();
			logger.log(Logger.Level.TRACE, "JMSMinorVersion=" + tmp);

			if (tmp != 0) {
				logger.log(Logger.Level.ERROR, "Error: incorrect JMSMajorVersion=" + tmp);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error: incorrect type returned for JMSMinorVersion: ", e);
			pass = false;
		}

		try {
			String tmp = data.getJMSProviderName();
			logger.log(Logger.Level.TRACE, "JMSProviderName=" + tmp);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error: incorrect type returned for JMSProviderName: ", e);
			pass = false;
		}

		try {
			String tmp = data.getProviderVersion();
			logger.log(Logger.Level.TRACE, "JMSProviderVersion=" + tmp);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error: incorrect type returned for ProviderVersion: ", e);
			pass = false;
		}

		try {
			int tmp = data.getProviderMajorVersion();
			logger.log(Logger.Level.TRACE, "ProviderMajorVersion=" + tmp);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error: incorrect type returned for ProviderMajorVersion: ", e);
			pass = false;
		}

		try {
			int tmp = data.getProviderMinorVersion();
			logger.log(Logger.Level.TRACE, "ProviderMinorVersion=" + tmp);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error: incorrect type returned for ProviderMinorVersion: ", e);
			pass = false;
		}
		return pass;
	}

	/*
	 * helper method verifies that the JMSMessageID starts with ID:
	 * 
	 * @param String returned from getJMSMessageID
	 * 
	 * @return boolean true if id correctly starts with ID:
	 */
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
			String lookupDurableTopicFactory = "DURABLE_SUB_CONNECTION_FACTORY";
			String lookupNormalTopicFactory = "MyTopicConnectionFactory";

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
				throw new Exception("'user' is null ");
			}
			if (password == null) {
				throw new Exception("'password' is null ");
			}
			if (mode == null) {
				throw new Exception("'platform.mode' is null");
			}
			connections = new ArrayList(5);

			// set up JmsTool for COMMON_T setup
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON_T and normal topic connection factory");
			tool = new JmsTool(JmsTool.COMMON_T, user, password, lookupNormalTopicFactory, mode);
			cf = tool.getConnectionFactory();
			tool.getDefaultConnection().close(); // Close connection (Create
													// JMSContext to use instead)
			destination = tool.getDefaultDestination();
			topic = (Topic) destination;

			// create JMSContext with AUTO_ACKNOWLEDGE then create consumer/producer
			context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			producer = context.createProducer();
			consumer = context.createConsumer(topic);

			// set up JmsTool for COMMON_T setup
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON_T and durable topic connection factory");
			tool2 = new JmsTool(JmsTool.COMMON_T, user, password, lookupDurableTopicFactory, mode);
			tool2.getDefaultConnection().close(); // Close connection (Create
													// JMSContext to use instead)
			cf2 = tool2.getConnectionFactory();
			destination2 = tool2.getDefaultDestination();
			topic2 = (Topic) destination2;

			// create second JMSContext with AUTO_ACKNOWLEDGE, then create producer
			context2 = cf2.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			producer2 = context2.createProducer();

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("setup failed!", e);
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
			logger.log(Logger.Level.INFO, "Close JMSContext Objects");
			if (context != null) {
				context.close();
				context = null;
			}
			if (context2 != null) {
				context2.close();
				context2 = null;
			}
			producer = producer2 = null;
			logger.log(Logger.Level.INFO, "Close JMSConsumer Objects");
			if (consumer != null) {
				consumer.close();
				consumer = null;
			}
			tool.closeAllResources();
			tool2.closeAllResources();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("cleanup failed!", e);
		}
	}

	/*
	 * Cleanup method for tests that use durable subscriptions
	 */
	private void cleanupSubscription(JMSConsumer consumer, JMSContext context, String subName) {
		if (consumer != null) {
			try {
				logger.log(Logger.Level.TRACE, "Closing durable consumer: " + consumer);
				consumer.close();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Exception during JMSConsumer.close: ", e);
			}
		}

		if (context != null) {
			try {
				logger.log(Logger.Level.TRACE, "Unsubscribing \"" + subName + "\"");
				context.unsubscribe(subName);
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Exception during JMSContext.unsubscribe: ", e);
			}
		}
	}

	/*
	 * @testName: sendAndRecvTest1
	 *
	 * @assertion_ids: JMS:JAVADOC:1234;
	 *
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 *
	 * JMSProducer.send(Destination, Message) JMSConsumer.receive(long)
	 *
	 */
	@Test
	public void sendAndRecvTest1() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvTest1");
			logger.log(Logger.Level.INFO, "Sending TextMessage via JMSProducer.send(Destination, Message)");
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Receive TextMessage via JMSconsumer.receive(long)");
			TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the value in TextMessage");
			if (actTextMessage.getText().equals(expTextMessage.getText())) {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
						+ ", received " + actTextMessage.getText());
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("sendAndRecvTest1", e);
		}

		if (!pass) {
			throw new Exception("sendAndRecvTest1 failed");
		}
	}

	/*
	 * @testName: sendAndRecvTest2
	 *
	 * @assertion_ids: JMS:JAVADOC:1239;
	 *
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 *
	 * JMSProducer.send(Destination, String) JMSConsumer.receiveBody(String, long)
	 *
	 */
	@Test
	public void sendAndRecvTest2() throws Exception {
		boolean pass = true;
		String expTextMessage = "Where are you!";
		try {
			// send and receive TextMessage payload
			logger.log(Logger.Level.INFO, "Sending TextMessage via JMSProducer.send(Destination, String)");
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Receive TextMessage via JMSConsumer.receiveBody(String, long)");
			String actTextMessage = consumer.receiveBody(String.class, timeout);
			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the value in TextMessage");
			if (actTextMessage.equals(expTextMessage)) {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR,
						"TextMessage is incorrect expected " + expTextMessage + ", received " + actTextMessage);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("sendAndRecvTest2", e);
		}

		if (!pass) {
			throw new Exception("sendAndRecvTest2 failed");
		}
	}

	/*
	 * @testName: sendAndRecvTest3
	 *
	 * @assertion_ids: JMS:JAVADOC:1251;
	 *
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 *
	 * JMSProducer.send(Destination, Serializable)
	 * JMSConsumer.receiveBody(Serializable, long)
	 */
	@Test
	public void sendAndRecvTest3() throws Exception {
		boolean pass = true;
		try {
			// send and receive ObjectMessage
			logger.log(Logger.Level.INFO, "Send ObjectMessage");
			logger.log(Logger.Level.INFO, "Set some values in ObjectMessage");
			ObjectMessage expObjectMessage = context.createObjectMessage();
			StringBuffer expSb = new StringBuffer("Where are you!");
			logger.log(Logger.Level.INFO, "Set object in ObjectMessage to a StringBuffer");
			expObjectMessage.setObject(expSb);
			expObjectMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvTest3");
			logger.log(Logger.Level.INFO, "Sending TextMessage via JMSProducer.send(Destination, Serializable)");
			producer.send(destination, expObjectMessage);
			logger.log(Logger.Level.INFO, "Receive ObjectMessage via JMSConsumer.receiveBody(Serializable, long)");
			StringBuffer actSb = (StringBuffer) consumer.receiveBody(Serializable.class, timeout);
			if (actSb == null) {
				throw new Exception("Did not receive ObjectMessage");
			}
			logger.log(Logger.Level.INFO, "Check the value in ObjectMessage");
			if (actSb.toString().equals(expSb.toString())) {
				logger.log(Logger.Level.INFO, "ObjectMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR,
						"ObjectMessage is incorrect expected " + expSb.toString() + ", received " + actSb.toString());
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("sendAndRecvTest3", e);
		}

		if (!pass) {
			throw new Exception("sendAndRecvTest3 failed");
		}
	}

	/*
	 * @testName: sendAndRecvTest4
	 *
	 * @assertion_ids: JMS:JAVADOC:1243;
	 *
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 *
	 * JMSProducer.send(Destination, Map<String, Object>)
	 * JMSConsumer.receiveBody(Map, long)
	 */
	@Test
	public void sendAndRecvTest4() throws Exception {
		boolean pass = true;
		Map<String, Object> mapMsgSend = new HashMap<String, Object>();
		mapMsgSend.put("StringValue", "sendAndRecvTest4");
		mapMsgSend.put("BooleanValue", true);
		mapMsgSend.put("IntValue", (int) 10);
		try {
			// send and receive MapMessage payload
			logger.log(Logger.Level.INFO, "Send MapMessage via JMSProducer.send(Destination, Map<String, Object>)");
			producer.send(destination, mapMsgSend);
			logger.log(Logger.Level.INFO, "Receive MapMessage via JMSConsumer.receiveBody(Map.class, long");
			Map<String, Object> mapMsgRecv = consumer.receiveBody(Map.class, timeout);
			if (mapMsgRecv == null) {
				throw new Exception("Did not receive MapMessage");
			}
			logger.log(Logger.Level.INFO, "Compare MapMsgSend and MapMsgRecv for equality");
			for (Entry<String, Object> entry : mapMsgSend.entrySet()) {
				String key = entry.getKey();
				logger.log(Logger.Level.INFO, "key " + key + ": " + entry.getValue().equals(mapMsgRecv.get(key)));
				if (entry.getValue().equals(mapMsgRecv.get(key))) {
					continue;
				} else {
					pass = false;
				}
			}
			if (pass) {
				logger.log(Logger.Level.INFO, "MapMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR,
						"MapMessage is incorrect expected " + mapMsgSend + ", received " + mapMsgRecv);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("sendAndRecvTest4", e);
		}

		if (!pass) {
			throw new Exception("sendAndRecvTest4 failed");
		}
	}

	/*
	 * @testName: sendAndRecvTest5
	 *
	 * @assertion_ids: JMS:JAVADOC:1247;
	 *
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 *
	 * JMSProducer.send(Destination, byte[]) JMSConsumer.receiveBody(byte[], long)
	 *
	 */
	@Test
	public void sendAndRecvTest5() throws Exception {
		boolean pass = true;
		String messageSend = "Where are you!";
		byte[] bytesMsgSend = messageSend.getBytes();
		try {
			// send and receive BytesMessage
			logger.log(Logger.Level.INFO, "Send BytesMessage via JMSProducer.send(Destination, byte[])");
			producer.send(destination, bytesMsgSend);
			logger.log(Logger.Level.INFO, "Receive BytesMessage via JMSConsumer.receiveBody(byte[].class, long");
			byte[] bytesMsgRecv = consumer.receiveBody(byte[].class, timeout);
			if (bytesMsgRecv == null) {
				throw new Exception("Did not receive BytesMessage");
			}
			logger.log(Logger.Level.INFO, "Compare BytesMsgSend and BytesMsgRecv for equality");
			String messageRecv = new String(bytesMsgRecv);
			if (messageRecv.equals(messageSend)) {
				logger.log(Logger.Level.INFO, "BytesMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR,
						"BytesMessage is incorrect expected " + messageRecv + ", received " + messageSend);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("sendAndRecvTest5", e);
		}

		if (!pass) {
			throw new Exception("sendAndRecvTest5 failed");
		}
	}

	/*
	 * @testName: sendAndRecvMsgsOfEachMsgTypeTest
	 *
	 * @assertion_ids: JMS:JAVADOC:925; JMS:JAVADOC:927; JMS:JAVADOC:929;
	 * JMS:JAVADOC:934; JMS:JAVADOC:937; JMS:JAVADOC:940; JMS:JAVADOC:964;
	 * JMS:JAVADOC:966; JMS:JAVADOC:942; JMS:JAVADOC:847; JMS:JAVADOC:1104;
	 * JMS:JAVADOC:1234; JMS:JAVADOC:875; JMS:JAVADOC:936; JMS:JAVADOC:1177;
	 *
	 * @test_Strategy: Send and receive messages of each message type: Message,
	 * BytesMessage, MapMessage, ObjectMessage, StreamMessage, TextMessage. Gets the
	 * delivery time of each send of the message. Tests the following API's
	 *
	 * ConnectionFactory.createContext(String, String, int)
	 * JMSContext.createConsumer(Destination) JMSContext.createProducer()
	 * JMSContext.createMessage() JMSContext.createBytesMessage()
	 * JMSContext.createMapMessage() JMSContext.createObjectMessage()
	 * JMSContext.createObjectMessage(Serializable object)
	 * JMSContext.createStreamMessage() JMSContext.createTextMessage()
	 * JMSContext.createTextMessage(String) JMSContext.createConsumer(Destination)
	 * JMSProducer.send(Destination, Message) JMSConsumer.receive(long timeout)
	 *
	 */
	@Test
	public void sendAndRecvMsgsOfEachMsgTypeTest() throws Exception {
		boolean pass = true;
		try {
			// send and receive Message
			logger.log(Logger.Level.INFO, "Send Message");
			Message msg = context.createMessage();
			logger.log(Logger.Level.INFO, "Set some values in Message");
			msg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgsOfEachMsgTypeTest");
			msg.setBooleanProperty("booleanProperty", true);
			producer.send(destination, msg);
			long deliveryTime = msg.getJMSDeliveryTime();
			logger.log(Logger.Level.INFO, "Receive Message");
			Message msgRecv = (Message) consumer.receive(timeout);
			if (msgRecv == null) {
				logger.log(Logger.Level.ERROR, "Did not receive Message");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the values in Message");
				if (msgRecv.getBooleanProperty("booleanProperty") == true) {
					logger.log(Logger.Level.INFO, "booleanproperty is correct");
				} else {
					logger.log(Logger.Level.INFO, "booleanproperty is incorrect");
					pass = false;
				}
			}

			// send and receive BytesMessage
			logger.log(Logger.Level.INFO, "Send BytesMessage");
			BytesMessage bMsg = context.createBytesMessage();
			logger.log(Logger.Level.INFO, "Set some values in BytesMessage");
			bMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgsOfEachMsgTypeTest");
			bMsg.writeByte((byte) 1);
			bMsg.writeInt((int) 22);
			producer.send(destination, bMsg);
			deliveryTime = bMsg.getJMSDeliveryTime();
			logger.log(Logger.Level.INFO, "Receive BytesMessage");
			BytesMessage bMsgRecv = (BytesMessage) consumer.receive(timeout);
			if (bMsgRecv == null) {
				logger.log(Logger.Level.ERROR, "Did not receive BytesMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the values in BytesMessage");
				if (bMsgRecv.readByte() == (byte) 1) {
					logger.log(Logger.Level.INFO, "bytevalue is correct");
				} else {
					logger.log(Logger.Level.INFO, "bytevalue is incorrect");
					pass = false;
				}
				if (bMsgRecv.readInt() == (int) 22) {
					logger.log(Logger.Level.INFO, "intvalue is correct");
				} else {
					logger.log(Logger.Level.INFO, "intvalue is incorrect");
					pass = false;
				}
			}

			// send and receive MapMessage
			logger.log(Logger.Level.INFO, "Send MapMessage");
			MapMessage mMsg = context.createMapMessage();
			logger.log(Logger.Level.INFO, "Set some values in MapMessage");
			mMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgsOfEachMsgTypeTest");
			mMsg.setBoolean("booleanvalue", true);
			mMsg.setInt("intvalue", (int) 10);
			producer.send(destination, mMsg);
			deliveryTime = mMsg.getJMSDeliveryTime();
			logger.log(Logger.Level.INFO, "Receive MapMessage");
			MapMessage mMsgRecv = (MapMessage) consumer.receive(timeout);
			if (mMsgRecv == null) {
				logger.log(Logger.Level.ERROR, "Did not receive MapMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the values in MapMessage");
				Enumeration list = mMsgRecv.getMapNames();
				String name = null;
				while (list.hasMoreElements()) {
					name = (String) list.nextElement();
					if (name.equals("booleanvalue")) {
						if (mMsgRecv.getBoolean(name) == true) {
							logger.log(Logger.Level.INFO, "booleanvalue is correct");
						} else {
							logger.log(Logger.Level.ERROR, "booleanvalue is incorrect");
							pass = false;
						}
					} else if (name.equals("intvalue")) {
						if (mMsgRecv.getInt(name) == 10) {
							logger.log(Logger.Level.INFO, "intvalue is correct");
						} else {
							logger.log(Logger.Level.ERROR, "intvalue is incorrect");
							pass = false;
						}
					} else {
						logger.log(Logger.Level.ERROR, "Unexpected name of [" + name + "] in MapMessage");
						pass = false;
					}
				}
			}

			// send and receive ObjectMessage
			logger.log(Logger.Level.INFO, "Send ObjectMessage");
			StringBuffer sb1 = new StringBuffer("This is a StringBuffer");
			logger.log(Logger.Level.INFO, "Set some values in ObjectMessage");
			ObjectMessage oMsg = context.createObjectMessage();
			oMsg.setObject(sb1);
			oMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgsOfEachMsgTypeTest");
			producer.send(destination, oMsg);
			deliveryTime = oMsg.getJMSDeliveryTime();
			logger.log(Logger.Level.INFO, "Receive ObjectMessage");
			ObjectMessage oMsgRecv = (ObjectMessage) consumer.receive(timeout);
			if (oMsgRecv == null) {
				logger.log(Logger.Level.ERROR, "Did not receive ObjectMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the value in ObjectMessage");
				StringBuffer sb2 = (StringBuffer) oMsgRecv.getObject();
				if (sb2.toString().equals(sb1.toString())) {
					logger.log(Logger.Level.INFO, "objectvalue is correct");
				} else {
					logger.log(Logger.Level.ERROR, "objectvalue is incorrect");
					pass = false;
				}
			}

			// send and receive ObjectMessage passing object as param
			logger.log(Logger.Level.INFO, "Send ObjectMessage passing object as param");
			sb1 = new StringBuffer("This is a StringBuffer");
			logger.log(Logger.Level.INFO, "Set some values in ObjectMessage passing object as param");
			oMsg = context.createObjectMessage(sb1);
			oMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgsOfEachMsgTypeTest");
			producer.send(destination, oMsg);
			deliveryTime = oMsg.getJMSDeliveryTime();
			logger.log(Logger.Level.INFO, "Receive ObjectMessage");
			oMsgRecv = (ObjectMessage) consumer.receive(timeout);
			if (oMsgRecv == null) {
				logger.log(Logger.Level.ERROR, "Did not receive ObjectMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the value in ObjectMessage");
				StringBuffer sb2 = (StringBuffer) oMsgRecv.getObject();
				if (sb2.toString().equals(sb1.toString())) {
					logger.log(Logger.Level.INFO, "objectvalue is correct");
				} else {
					logger.log(Logger.Level.ERROR, "objectvalue is incorrect");
					pass = false;
				}
			}

			// send and receive StreamMessage
			logger.log(Logger.Level.INFO, "Send StreamMessage");
			StreamMessage sMsg = context.createStreamMessage();
			logger.log(Logger.Level.INFO, "Set some values in StreamMessage");
			sMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgsOfEachMsgTypeTest");
			sMsg.writeBoolean(true);
			sMsg.writeInt((int) 22);
			producer.send(destination, sMsg);
			deliveryTime = sMsg.getJMSDeliveryTime();
			logger.log(Logger.Level.INFO, "Receive StreamMessage");
			StreamMessage sMsgRecv = (StreamMessage) consumer.receive(timeout);
			if (sMsgRecv == null) {
				logger.log(Logger.Level.ERROR, "Did not receive StreamMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the values in StreamMessage");
				if (sMsgRecv.readBoolean() == true) {
					logger.log(Logger.Level.INFO, "booleanvalue is correct");
				} else {
					logger.log(Logger.Level.INFO, "booleanvalue is incorrect");
					pass = false;
				}
				if (sMsgRecv.readInt() == (int) 22) {
					logger.log(Logger.Level.INFO, "intvalue is correct");
				} else {
					logger.log(Logger.Level.INFO, "intvalue is incorrect");
					pass = false;
				}
			}

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Send TextMessage");
			TextMessage tMsg = context.createTextMessage();
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			tMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgsOfEachMsgTypeTest");
			tMsg.setText("Hello There!");
			producer.send(destination, tMsg);
			deliveryTime = tMsg.getJMSDeliveryTime();
			logger.log(Logger.Level.INFO, "Receive TextMessage");
			TextMessage tMsgRecv = (TextMessage) consumer.receive(timeout);
			if (tMsgRecv == null) {
				logger.log(Logger.Level.ERROR, "Did not receive TextMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the value in TextMessage");
				if (tMsgRecv.getText().equals("Hello There!")) {
					logger.log(Logger.Level.INFO, "TextMessage is correct");
				} else {
					logger.log(Logger.Level.ERROR, "TextMessage is incorrect");
					pass = false;
				}
			}

			// send and receive TextMessage passing string as param
			logger.log(Logger.Level.INFO, "Send TextMessage");
			tMsg = context.createTextMessage("Where are you!");
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			tMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgsOfEachMsgTypeTest");
			producer.send(destination, tMsg);
			deliveryTime = tMsg.getJMSDeliveryTime();
			logger.log(Logger.Level.INFO, "Receive TextMessage");
			tMsgRecv = (TextMessage) consumer.receive(timeout);
			if (tMsgRecv == null) {
				logger.log(Logger.Level.ERROR, "Did not receive TextMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the value in TextMessage");
				if (tMsgRecv.getText().equals("Where are you!")) {
					logger.log(Logger.Level.INFO, "TextMessage is correct");
				} else {
					logger.log(Logger.Level.ERROR, "TextMessage is incorrect");
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("sendAndRecvMsgsOfEachMsgTypeTest", e);
		}

		if (!pass) {
			throw new Exception("sendAndRecvMsgsOfEachMsgTypeTest failed");
		}
	}

	/*
	 * @testName: setGetDeliveryModeTest
	 *
	 * @assertion_ids: JMS:JAVADOC:1192; JMS:JAVADOC:1259;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * JMSProducer.setDeliveryMode(int). JMSProducer.getDeliveryMode().
	 */
	@Test
	public void setGetDeliveryModeTest() throws Exception {
		boolean pass = true;

		// Test default case
		try {
			int expDeliveryMode = DeliveryMode.PERSISTENT;
			logger.log(Logger.Level.INFO, "Calling getDeliveryMode and expect " + expDeliveryMode + " to be returned");
			int actDeliveryMode = producer.getDeliveryMode();
			if (actDeliveryMode != expDeliveryMode) {
				logger.log(Logger.Level.ERROR,
						"getDeliveryMode() returned " + actDeliveryMode + ", expected " + expDeliveryMode);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("setGetDeliveryModeTest");
		}

		// Test non-default case
		try {
			int expDeliveryMode = DeliveryMode.NON_PERSISTENT;
			logger.log(Logger.Level.INFO, "Calling setDeliveryMode(" + expDeliveryMode + ")");
			producer.setDeliveryMode(expDeliveryMode);
			logger.log(Logger.Level.INFO, "Calling getDeliveryMode and expect " + expDeliveryMode + " to be returned");
			int actDeliveryMode = producer.getDeliveryMode();
			if (actDeliveryMode != expDeliveryMode) {
				logger.log(Logger.Level.ERROR,
						"getDeliveryMode() returned " + actDeliveryMode + ", expected " + expDeliveryMode);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("setGetDeliveryModeTest");
		}

		if (!pass) {
			throw new Exception("setGetDeliveryModeTest failed");
		}
	}

	/*
	 * @testName: setGetDeliveryDelayTest
	 *
	 * @assertion_ids: JMS:JAVADOC:1190; JMS:JAVADOC:1257;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * JMSProducer.setDeliveryDelay(long). JMSProducer.getDeliveryDelay().
	 */
	@Test
	public void setGetDeliveryDelayTest() throws Exception {
		boolean pass = true;

		// Test default case
		try {
			long expDeliveryDelay = 0L;
			logger.log(Logger.Level.INFO,
					"Calling getDeliveryDelay and expect " + expDeliveryDelay + " to be returned");
			long actDeliveryDelay = producer.getDeliveryDelay();
			if (actDeliveryDelay != expDeliveryDelay) {
				logger.log(Logger.Level.ERROR,
						"getDeliveryDelay() returned " + actDeliveryDelay + ", expected " + expDeliveryDelay);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("setGetDeliveryDelayTest");
		}

		// Test non-default case
		try {
			long expDeliveryDelay = 1L;
			logger.log(Logger.Level.INFO, "Calling setDeliveryDelay(" + expDeliveryDelay + ")");
			producer.setDeliveryDelay(expDeliveryDelay);
			logger.log(Logger.Level.INFO,
					"Calling getDeliveryDelay and expect " + expDeliveryDelay + " to be returned");
			long actDeliveryDelay = producer.getDeliveryDelay();
			if (actDeliveryDelay != expDeliveryDelay) {
				logger.log(Logger.Level.ERROR,
						"getDeliveryDelay() returned " + actDeliveryDelay + ", expected " + expDeliveryDelay);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("setGetDeliveryDelayTest");
		}

		if (!pass) {
			throw new Exception("setGetDeliveryDelayTest failed");
		}
	}

	/*
	 * @testName: setGetDisableMessageIDTest
	 *
	 * @assertion_ids: JMS:JAVADOC:1194; JMS:JAVADOC:1261;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * JMSProducer.setDisableMessageID(boolean). JMSProducer.getDisableMessageID().
	 */
	@Test
	public void setGetDisableMessageIDTest() throws Exception {
		boolean pass = true;
		// Test default case
		try {
			boolean expDisableMessageID = true;
			logger.log(Logger.Level.INFO, "Calling setDisableMessageID(" + expDisableMessageID + ")");
			producer.setDisableMessageID(expDisableMessageID);
			logger.log(Logger.Level.INFO,
					"Calling getDisableMessageID and expect " + expDisableMessageID + " to be returned");
			boolean actDisableMessageID = producer.getDisableMessageID();
			if (actDisableMessageID != expDisableMessageID) {
				logger.log(Logger.Level.ERROR,
						"getDisableMessageID() returned " + actDisableMessageID + ", expected " + expDisableMessageID);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("setGetDisableMessageIDTest");
		}

		// Test non-default case
		try {
			boolean expDisableMessageID = false;
			logger.log(Logger.Level.INFO, "Calling setDisableMessageID(" + expDisableMessageID + ")");
			producer.setDisableMessageID(expDisableMessageID);
			logger.log(Logger.Level.INFO,
					"Calling getDisableMessageID and expect " + expDisableMessageID + " to be returned");
			boolean actDisableMessageID = producer.getDisableMessageID();
			if (actDisableMessageID != expDisableMessageID) {
				logger.log(Logger.Level.ERROR,
						"getDisableMessageID() returned " + actDisableMessageID + ", expected " + expDisableMessageID);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("setGetDisableMessageIDTest");
		}

		if (!pass) {
			throw new Exception("setGetDisableMessageIDTest failed");
		}
	}

	/*
	 * @testName: setGetDisableMessageTimestampTest
	 *
	 * @assertion_ids: JMS:JAVADOC:1196; JMS:JAVADOC:1263;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * JMSProducer.setDisableMessageTimestamp(boolean).
	 * JMSProducer.getDisableMessageTimestamp().
	 */
	@Test
	public void setGetDisableMessageTimestampTest() throws Exception {
		boolean pass = true;
		// Test default case
		try {
			boolean expDisableMessageTimestamp = true;
			logger.log(Logger.Level.INFO, "Calling setDisableMessageTimestamp(" + expDisableMessageTimestamp + ")");
			producer.setDisableMessageTimestamp(expDisableMessageTimestamp);
			logger.log(Logger.Level.INFO,
					"Calling getDisableMessageTimestamp and expect " + expDisableMessageTimestamp + " to be returned");
			boolean actDisableMessageTimestamp = producer.getDisableMessageTimestamp();
			if (actDisableMessageTimestamp != expDisableMessageTimestamp) {
				logger.log(Logger.Level.ERROR, "getDisableMessageTimestamp() returned " + actDisableMessageTimestamp
						+ ", expected " + expDisableMessageTimestamp);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("setGetDisableMessageTimestampTest");
		}

		// Test non-default case
		try {
			boolean expDisableMessageTimestamp = false;
			logger.log(Logger.Level.INFO, "Calling setDisableMessageTimestamp(" + expDisableMessageTimestamp + ")");
			producer.setDisableMessageTimestamp(expDisableMessageTimestamp);
			logger.log(Logger.Level.INFO,
					"Calling getDisableMessageTimestamp and expect " + expDisableMessageTimestamp + " to be returned");
			boolean actDisableMessageTimestamp = producer.getDisableMessageTimestamp();
			if (actDisableMessageTimestamp != expDisableMessageTimestamp) {
				logger.log(Logger.Level.ERROR, "getDisableMessageTimestamp() returned " + actDisableMessageTimestamp
						+ ", expected " + expDisableMessageTimestamp);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("setGetDisableMessageTimestampTest");
		}

		if (!pass) {
			throw new Exception("setGetDisableMessageTimestampTest failed");
		}
	}

	/*
	 * @testName: setGetPriorityTest
	 *
	 * @assertion_ids: JMS:JAVADOC:1220; JMS:JAVADOC:1273;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * JMSProducer.setPriority(int). JMSProducer.getPriority().
	 */
	@Test
	public void setGetPriorityTest() throws Exception {
		boolean pass = true;
		try {
			// Test default
			int expPriority = Message.DEFAULT_PRIORITY;
			logger.log(Logger.Level.INFO, "Calling getPriority and expect " + expPriority + " to be returned");
			int actPriority = producer.getPriority();
			if (actPriority != expPriority) {
				logger.log(Logger.Level.ERROR, "getPriority() returned " + actPriority + ", expected " + expPriority);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("setGetPriorityTest");
		}

		// Test non-default
		int expPriority[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

		// Cycle through all priorties
		for (int i = 0; i < expPriority.length; i++) {
			try {
				logger.log(Logger.Level.INFO, "Calling setPriority(" + expPriority[i] + ")");
				producer.setPriority(expPriority[i]);
				logger.log(Logger.Level.INFO, "Calling getPriority and expect " + expPriority[i] + " to be returned");
				int actPriority = producer.getPriority();
				if (actPriority != expPriority[i]) {
					logger.log(Logger.Level.ERROR,
							"getPriority() returned " + actPriority + ", expected " + expPriority[i]);
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught exception: " + e);
				throw new Exception("setGetPriorityTest");
			}
		}

		if (!pass) {
			throw new Exception("setGetPriorityTest failed");
		}
	}

	/*
	 * @testName: setGetTimeToLiveTest
	 *
	 * @assertion_ids: JMS:JAVADOC:1230; JMS:JAVADOC:1303;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * JMSProducer.setTimeToLive(long). JMSProducer.getTimeToLive()
	 */
	@Test
	public void setGetTimeToLiveTest() throws Exception {
		boolean pass = true;

		try {
			// Test default
			long expTimeToLive = 0;
			logger.log(Logger.Level.INFO, "Calling getTimeToLive and expect " + expTimeToLive + " to be returned");
			long actTimeToLive = producer.getTimeToLive();
			if (actTimeToLive != expTimeToLive) {
				logger.log(Logger.Level.ERROR,
						"getTimeToLive() returned " + actTimeToLive + ", expected " + expTimeToLive);
				pass = false;
			}

			// Test non-default
			expTimeToLive = 1000;
			logger.log(Logger.Level.INFO, "Calling setTimeToLive(" + expTimeToLive + ")");
			producer.setTimeToLive(expTimeToLive);
			logger.log(Logger.Level.INFO, "Calling getTimeToLive and expect " + expTimeToLive + " to be returned");
			actTimeToLive = producer.getTimeToLive();
			if (actTimeToLive != expTimeToLive) {
				logger.log(Logger.Level.ERROR,
						"getTimeToLive() returned " + actTimeToLive + ", expected " + expTimeToLive);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("setGetTimeToLiveTest");
		}

		if (!pass) {
			throw new Exception("setGetTimeToLiveTest failed");
		}
	}

	/*
	 * @testName: deliveryDelayTest
	 * 
	 * @assertion_ids: JMS:SPEC:261; JMS:SPEC:256; JMS:JAVADOC:1257;
	 * 
	 * @test_Strategy: Send message and verify that message is not delivered until
	 * the DeliveryDelay of 20 seconds is reached. Test DeliveryMode.PERSISTENT and
	 * DeliveryMode.NON_PERSISTENT.
	 */
	@Test
	public void deliveryDelayTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "------------------------------------------------------");
			logger.log(Logger.Level.INFO, "BEGIN TEST deliveryDelayTest with DeliveryDelay=20Secs");
			logger.log(Logger.Level.INFO, "------------------------------------------------------");
			producer.setDeliveryDelay(20000);

			// Send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage message = context.createTextMessage("This is a test!");

			logger.log(Logger.Level.INFO, "Set StringProperty COM_SUN_JMS_TESTNAME");
			message.setStringProperty("COM_SUN_JMS_TESTNAME", "deliveryDelayTest");

			logger.log(Logger.Level.INFO, "Sending message with DeliveryMode.PERSISTENT and DeliveryDelay=20Secs");
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			producer.setPriority(Message.DEFAULT_PRIORITY);
			producer.setTimeToLive(0L);
			producer.send(destination, message);

			logger.log(Logger.Level.INFO, "Waiting 10 seconds to receive message");
			message = (TextMessage) consumer.receive(10000);
			if (message != null) {
				logger.log(Logger.Level.ERROR, "FAILED: Message received before delivery delay of 20 secs elapsed");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Didn't receive message after 10 seconds (CORRECT)");
				logger.log(Logger.Level.INFO, "Sleeping 5 more seconds before receiving message");
				Thread.sleep(5000);
				logger.log(Logger.Level.INFO, "Waiting 10 more seconds to receive message");
				message = (TextMessage) consumer.receive(10000);
				if (message == null) {
					logger.log(Logger.Level.ERROR,
							"FAILED: Message was not received after delivery delay of 20 secs elapsed");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Received message after 20 secs elapsed (CORRECT)");
				}
			}

			logger.log(Logger.Level.INFO, "Sending message with DeliveryMode.NON_PERSISTENT and DeliveryDelay=20Secs");
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			producer.setPriority(Message.DEFAULT_PRIORITY);
			producer.setTimeToLive(0L);
			producer.send(destination, message);

			logger.log(Logger.Level.INFO, "Waiting 10 seconds to receive message");
			message = (TextMessage) consumer.receive(10000);
			if (message != null) {
				logger.log(Logger.Level.ERROR, "FAILED: Message received before delivery delay of 20 secs elapsed");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Didn't receive message after 10 seconds (CORRECT)");
				logger.log(Logger.Level.INFO, "Sleeping 5 more seconds before receiving message");
				Thread.sleep(5000);
				logger.log(Logger.Level.INFO, "Waiting 10 more seconds to receive message");
				message = (TextMessage) consumer.receive(10000);
				if (message == null) {
					logger.log(Logger.Level.ERROR,
							"FAILED: Message was not received after delivery delay of 20 secs elapsed");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Received message after 20 secs elapsed (CORRECT)");
				}
			}
			logger.log(Logger.Level.INFO, "----------------------------------------------------");
			logger.log(Logger.Level.INFO, "END TEST deliveryDelayTest with DeliveryDelay=20Secs");
			logger.log(Logger.Level.INFO, "----------------------------------------------------");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("deliveryDelayTest", e);
		}

		if (!pass) {
			throw new Exception("deliveryDelayTest failed");
		}
	}

	/*
	 * @testName: msgHdrMessageIDTest
	 * 
	 * @assertion_ids: JMS:SPEC:4; JMS:JAVADOC:343; JMS:JAVADOC:1261;
	 * JMS:JAVADOC:1194;
	 * 
	 * @test_Strategy: Send to a Topic and receive Text, Map, Bytes, Stream, and
	 * Object message. Call getJMSMessageID and verify that it starts with ID:
	 */
	@Test
	public void msgHdrMessageIDTest() throws Exception {
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

			producer.setDisableMessageID(false);

			// send and receive Object message to Topic
			logger.log(Logger.Level.INFO, "Send ObjectMessage to Topic.");
			messageSentO = context.createObjectMessage();
			messageSentO.setObject("msgHdrMessageIDTest for ObjectMessage");
			messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrMessageIDTest");
			producer.send(destination, messageSentO);
			logger.log(Logger.Level.INFO, "Receive ObjectMessage from Topic.");
			messageReceivedO = (ObjectMessage) consumer.receive(timeout);
			if (messageReceivedO == null) {
				logger.log(Logger.Level.ERROR, "Received no message NULL (unexpected)");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "getJMSMessageID=" + messageReceivedO.getJMSMessageID());
				id = messageReceivedO.getJMSMessageID();
				if (!chkMessageID(id)) {
					logger.log(Logger.Level.ERROR, "ObjectMessage error: JMSMessageID does not start with ID:");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Objectessage JMSMessageID pass");
				}
			}
			// send and receive map message to Topic
			logger.log(Logger.Level.INFO, "Send MapMessage to Topic.");
			messageSentM = context.createMapMessage();
			messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrMessageIDTest");
			messageSentM.setString("aString", "value");
			producer.send(destination, messageSentM);
			logger.log(Logger.Level.INFO, "Receive MapMessage from Topic.");
			messageReceivedM = (MapMessage) consumer.receive(timeout);
			if (messageReceivedM == null) {
				logger.log(Logger.Level.ERROR, "Received no message NULL (unexpected)");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "getJMSMessageID=" + messageReceivedM.getJMSMessageID());
				id = messageReceivedM.getJMSMessageID();
				if (!chkMessageID(id)) {
					logger.log(Logger.Level.ERROR, "MapMessage error: JMSMessageID does not start with ID:");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "MapMessage JMSMessageID pass");
				}
			}

			// send and receive bytes message to Topic
			logger.log(Logger.Level.INFO, "Send BytesMessage to Topic.");
			messageSentB = context.createBytesMessage();
			messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrMessageIDTest");
			messageSentB.writeByte(bValue);
			producer.send(destination, messageSentB);
			logger.log(Logger.Level.INFO, "Receive BytesMessage from Topic.");
			messageReceivedB = (BytesMessage) consumer.receive(timeout);
			if (messageReceivedB == null) {
				logger.log(Logger.Level.ERROR, "Received no message NULL (unexpected)");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "getJMSMessageID=" + messageReceivedB.getJMSMessageID());
				id = messageReceivedB.getJMSMessageID();
				if (!chkMessageID(id)) {
					logger.log(Logger.Level.ERROR, "BytesMessage error: JMSMessageID does not start with ID:");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "BytesMessage JMSMessageID pass");
				}
			}

			// Send and receive a StreamMessage
			logger.log(Logger.Level.INFO, "Send StreamMessage to Topic");
			messageSentS = context.createStreamMessage();
			messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrMessageIDTest");
			messageSentS.writeString("Testing...");
			logger.log(Logger.Level.INFO, "Sending message");
			producer.send(destination, messageSentS);
			logger.log(Logger.Level.INFO, "Receive StreamMessage from Topic.");
			messageReceivedS = (StreamMessage) consumer.receive(timeout);
			if (messageReceivedM == null) {
				logger.log(Logger.Level.ERROR, "Received no message NULL (unexpected)");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "getJMSMessageID=" + messageReceivedS.getJMSMessageID());
				id = messageReceivedS.getJMSMessageID();
				if (!chkMessageID(id)) {
					logger.log(Logger.Level.ERROR, "StreamMessage error: JMSMessageID does not start with ID:");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "StreamMessage JMSMessageID pass");
				}
			}

			// TextMessage
			logger.log(Logger.Level.INFO, "Send TextMessage to Topic");
			messageSent = context.createTextMessage();
			messageSent.setText("sending a TextMessage");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrMessageIDTest");
			producer.send(destination, messageSent);
			logger.log(Logger.Level.INFO, "Receive TextMessage from Topic.");
			messageReceived = (TextMessage) consumer.receive(timeout);
			if (messageReceived == null) {
				logger.log(Logger.Level.ERROR, "Received no message NULL (unexpected)");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "getJMSMessageID=" + messageReceived.getJMSMessageID());
				id = messageReceived.getJMSMessageID();
				if (!chkMessageID(id)) {
					logger.log(Logger.Level.ERROR, "TextMessage error: JMSMessageID does not start with ID:");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "TextMessage JMSMessageID pass");
				}
			}
			if (!pass) {
				throw new Exception("Error: invalid JMSMessageID returned from JMSMessageID");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.INFO, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("msgHdrMessageIDTest");
		}
	}

	/*
	 * @testName: msgHdrTimeStampTest
	 * 
	 * @assertion_ids: JMS:SPEC:7; JMS:JAVADOC:347; JMS:JAVADOC:1263;
	 * JMS:JAVADOC:1196;
	 * 
	 * @test_Strategy: Send to a Topic a single Text, map, bytes, stream, and object
	 * message. Call getJMSTimestamp() and check time of send against time send
	 * returns. JMSTimeStamp should be between these two
	 */
	@Test
	public void msgHdrTimeStampTest() throws Exception {
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

			producer.setDisableMessageTimestamp(false);

			// send and receive Object message to Topic
			logger.log(Logger.Level.INFO, "Send ObjectMessage to Topic.");
			messageSentO = context.createObjectMessage();
			messageSentO.setObject("msgHdrTimeStampTest for ObjectMessage");
			messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrTimeStampTest");

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			producer.send(destination, messageSentO);

			// message has been sent
			timeAfterSend = System.currentTimeMillis();
			logger.log(Logger.Level.INFO, "getJMSTimestamp=" + messageSentO.getJMSTimestamp());
			logger.log(Logger.Level.INFO, "Time at send is: " + timeBeforeSend);
			logger.log(Logger.Level.INFO, "Time after return fromsend is:" + timeAfterSend);
			if ((timeBeforeSend <= messageSentO.getJMSTimestamp())
					&& (timeAfterSend >= messageSentO.getJMSTimestamp())) {
				logger.log(Logger.Level.INFO, "ObjectMessage JMSTimeStamp pass");
			} else {
				logger.log(Logger.Level.ERROR, "ObjectMessage invalid JMSTimeStamp failed");
				pass = false;
			}

			// send map message to Topic
			logger.log(Logger.Level.INFO, "Send MapMessage to Topic.");
			messageSentM = context.createMapMessage();
			messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrTimeStampTest");
			messageSentM.setString("aString", "value");

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			producer.send(destination, messageSentM);

			// message has been sent
			timeAfterSend = System.currentTimeMillis();
			logger.log(Logger.Level.INFO, "getJMSTimestamp=" + messageSentM.getJMSTimestamp());
			logger.log(Logger.Level.INFO, "Time at send is: " + timeBeforeSend);
			logger.log(Logger.Level.INFO, "Time after return fromsend is:" + timeAfterSend);
			if ((timeBeforeSend <= messageSentM.getJMSTimestamp())
					&& (timeAfterSend >= messageSentM.getJMSTimestamp())) {
				logger.log(Logger.Level.INFO, "MapMessage JMSTimeStamp pass");
			} else {
				logger.log(Logger.Level.ERROR, "MapMessage invalid JMSTimeStamp failed");
				pass = false;
			}

			// send and receive bytes message to Topic
			logger.log(Logger.Level.INFO, "Send BytesMessage to Topic.");
			messageSentB = context.createBytesMessage();
			messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrTimeStampTest");
			messageSentB.writeByte(bValue);

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			producer.send(destination, messageSentB);

			// message has been sent
			timeAfterSend = System.currentTimeMillis();
			logger.log(Logger.Level.INFO, "getJMSTimestamp=" + messageSentB.getJMSTimestamp());
			logger.log(Logger.Level.INFO, "Time at send is: " + timeBeforeSend);
			logger.log(Logger.Level.INFO, "Time after return fromsend is:" + timeAfterSend);
			if ((timeBeforeSend <= messageSentB.getJMSTimestamp())
					&& (timeAfterSend >= messageSentB.getJMSTimestamp())) {
				logger.log(Logger.Level.INFO, "BytesMessage JMSTimeStamp pass");
			} else {
				logger.log(Logger.Level.ERROR, "BytesMessage invalid JMSTimeStamp failed");
				pass = false;
			}

			// Send and receive a StreamMessage
			logger.log(Logger.Level.INFO, "Send StreamMessage to Topic");
			messageSentS = context.createStreamMessage();
			messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrTimeStampTest");
			messageSentS.writeString("Testing...");
			logger.log(Logger.Level.INFO, "Sending message");

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			producer.send(destination, messageSentS);

			// message has been sent
			timeAfterSend = System.currentTimeMillis();
			logger.log(Logger.Level.INFO, "getJMSTimestamp=" + messageSentS.getJMSTimestamp());
			logger.log(Logger.Level.INFO, "Time at send is: " + timeBeforeSend);
			logger.log(Logger.Level.INFO, "Time after return fromsend is:" + timeAfterSend);
			if ((timeBeforeSend <= messageSentS.getJMSTimestamp())
					&& (timeAfterSend >= messageSentS.getJMSTimestamp())) {
				logger.log(Logger.Level.INFO, "StreamMessage JMSTimeStamp pass");
			} else {
				logger.log(Logger.Level.ERROR, "StreamMessage invalid JMSTimeStamp failed");
				pass = false;
			}

			// TextMessage
			logger.log(Logger.Level.INFO, "Send TextMessage to Topic");
			messageSent = context.createTextMessage();
			messageSent.setText("sending a TextMessage to Topic");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrTimeStampTest");

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			producer.send(destination, messageSent);

			// message has been sent
			timeAfterSend = System.currentTimeMillis();
			logger.log(Logger.Level.INFO, "getJMSTimestamp=" + messageSent.getJMSTimestamp());
			logger.log(Logger.Level.INFO, "Time at send is: " + timeBeforeSend);
			logger.log(Logger.Level.INFO, "Time after return fromsend is:" + timeAfterSend);
			if ((timeBeforeSend <= messageSent.getJMSTimestamp()) && (timeAfterSend >= messageSent.getJMSTimestamp())) {
				logger.log(Logger.Level.INFO, "TextMessage JMSTimeStamp pass");
			} else {
				logger.log(Logger.Level.ERROR, "TextMessage invalid JMSTimeStamp failed");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.INFO, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("msgHdrTimeStampTest");
		}
	}

	/*
	 * @testName: msgHdrJMSPriorityTest
	 * 
	 * @assertion_ids: JMS:SPEC:16; JMS:SPEC:18; JMS:SPEC:140; JMS:JAVADOC:1220;
	 * JMS:JAVADOC:1273; JMS:JAVADOC:383;
	 * 
	 * @test_Strategy: Send a message to a Topic with JMSPriority set to 2 test with
	 * Text, map, object, byte, and stream messages Call getJMSPriorty() and check
	 * that it matches the priority that was set on the JMSContext.
	 */
	@Test
	public void msgHdrJMSPriorityTest() throws Exception {
		boolean pass = true;
		byte bValue = 127;
		int priority2 = 2;
		int priority4 = 4;

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

			logger.log(Logger.Level.INFO, "Setting priority to 2");
			producer.setPriority(priority2);

			messageSent = context.createTextMessage();
			messageSent.setText("sending a message");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSPriorityTest");
			logger.log(Logger.Level.INFO, "JMSPriority test - Send a TextMessage to Topic");
			producer.send(destination, messageSent);
			logger.log(Logger.Level.INFO, "JMSPriority test - Recv a TextMessage from Topic");
			messageReceived = (TextMessage) consumer.receive(timeout);
			if (messageReceived == null) {
				logger.log(Logger.Level.ERROR, "Received no message NULL (unexpected)");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "JMSPriority is " + messageReceived.getJMSPriority());
				if (messageReceived.getJMSPriority() == priority2) {
					logger.log(Logger.Level.INFO, "TextMessage JMSPriority passed");
				} else {
					logger.log(Logger.Level.ERROR, "TextMessage JMSPriority failed");
					pass = false;
				}
			}

			// send and receive Object message to Topic
			logger.log(Logger.Level.INFO, "JMSPriority test - Send ObjectMessage to Topic.");
			messageSentO = context.createObjectMessage();
			messageSentO.setObject("msgHdrJMSPriorityTest for ObjectMessage");
			messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSPriorityTest");
			producer.send(destination, messageSentO);
			logger.log(Logger.Level.INFO, "JMSPriority test - Recv a ObjectMessage from Topic");
			messageReceivedO = (ObjectMessage) consumer.receive(timeout);
			if (messageReceivedO == null) {
				logger.log(Logger.Level.ERROR, "Received no message NULL (unexpected)");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "JMSPriority is " + messageReceivedO.getJMSPriority());
				if (messageReceivedO.getJMSPriority() == priority2) {
					logger.log(Logger.Level.INFO, "ObjectMessage JMSPriority passed");
				} else {
					logger.log(Logger.Level.ERROR, "ObjectMessage JMSPriority failed");
					pass = false;
				}
			}

			// send and receive map message to Topic
			logger.log(Logger.Level.INFO, "JMSPriority test - Send MapMessage to Topic.");
			messageSentM = context.createMapMessage();
			messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSPriorityTest");
			messageSentM.setString("aString", "value");
			producer.send(destination, messageSentM);
			logger.log(Logger.Level.INFO, "JMSPriority test - Recv a MapMessage from Topic");
			messageReceivedM = (MapMessage) consumer.receive(timeout);
			if (messageReceivedM == null) {
				logger.log(Logger.Level.ERROR, "Received no message NULL (unexpected)");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "JMSPriority is " + messageReceivedM.getJMSPriority());
				if (messageReceivedM.getJMSPriority() == priority2) {
					logger.log(Logger.Level.INFO, "MapMessage JMSPriority passed");
				} else {
					logger.log(Logger.Level.ERROR, "MapMessage JMSPriority failed");
					pass = false;
				}
			}

			logger.log(Logger.Level.INFO, "Setting priority to 4");
			producer.setPriority(priority4);

			// send and receive bytes message to Topic
			logger.log(Logger.Level.INFO, "JMSPriority test - Send BytesMessage to Topic.");
			messageSentB = context.createBytesMessage();
			messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSPriorityTest");
			messageSentB.writeByte(bValue);
			producer.send(destination, messageSentB);
			logger.log(Logger.Level.INFO, "JMSPriority test - Recv a BytesMessage from Topic");
			messageReceivedB = (BytesMessage) consumer.receive(timeout);
			if (messageReceivedB == null) {
				logger.log(Logger.Level.ERROR, "Received no message NULL (unexpected)");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "JMSPriority is " + messageReceivedB.getJMSPriority());
				if (messageReceivedB.getJMSPriority() == priority4) {
					logger.log(Logger.Level.INFO, "BytesMessage JMSPriority passed");
				} else {
					logger.log(Logger.Level.ERROR, "BytesMessage JMSPriority failed");
					pass = false;
				}
			}

			// Send and receive a StreamMessage
			logger.log(Logger.Level.INFO, "JMSPriority test - Send a StreamMessage to Topic");
			messageSentS = context.createStreamMessage();
			messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSPriorityTest");
			messageSentS.writeString("Testing...");
			producer.send(destination, messageSentS);
			logger.log(Logger.Level.INFO, "JMSPriority test - Recv a StreamMessage from Topic");
			messageReceivedS = (StreamMessage) consumer.receive(timeout);
			if (messageReceivedS == null) {
				logger.log(Logger.Level.ERROR, "Received no message NULL (unexpected)");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "JMSPriority is " + messageReceivedS.getJMSPriority());
				if (messageReceivedS.getJMSPriority() == priority4) {
					logger.log(Logger.Level.INFO, "StreamMessage JMSPriority passed");
				} else {
					logger.log(Logger.Level.ERROR, "StreamMessage JMSPriority failed");
					pass = false;
				}
			}
			if (!pass) {
				throw new Exception("Error: invalid JMSPriority returned from JMS Header");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.INFO, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("msgHdrJMSPriorityTest");
		}
	}

	/*
	 * @testName: msgHdrJMSExpirationTest
	 * 
	 * @assertion_ids: JMS:SPEC:15.1; JMS:SPEC:15.2; JMS:SPEC:15.3; JMS:SPEC:140;
	 * JMS:JAVADOC:1303; JMS:JAVADOC:379;
	 * 
	 * @test_Strategy: 1. Send a message to a Topic with time to live set to 0.
	 * Verify on receive that JMSExpiration gets set to 0. Test with Text, Map,
	 * Object, Bytes, and Stream messages. 2. Send a message to a Topic with time to
	 * live set to non-0; Verify on receive that JMSExpiration gets set correctly.
	 */
	@Test
	public void msgHdrJMSExpirationTest() throws Exception {
		boolean pass = true;
		byte bValue = 127;
		long forever = 0L;
		long timeToLive = 5000L;
		String testName = "msgHdrJMSExpirationTest";
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

			logger.log(Logger.Level.INFO, "JMSExpiration test - Send a TextMessage (timeToLive is forever)");
			messageSent = context.createTextMessage();
			messageSent.setText("sending a TextMessage");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			producer.setTimeToLive(forever);
			producer.send(destination, messageSent);

			logger.log(Logger.Level.INFO, "JMSExpiration test - Recv a TextMessage");
			messageReceived = (TextMessage) consumer.receive(timeout);
			if (messageReceived == null) {
				logger.log(Logger.Level.ERROR, "Received no message NULL (unexpected)");
				pass = false;
			} else {
				if (messageReceived.getJMSExpiration() != forever) {
					logger.log(Logger.Level.ERROR, "TextMessage JMSExpiration failed");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "TextMessage JMSExpiration passed");
				}
			}

			logger.log(Logger.Level.INFO, "JMSExpiration test - Send a TextMessage (timeToLive is 5000)");
			producer.setTimeToLive(timeToLive);
			timeBeforeSend = System.currentTimeMillis();
			producer.send(destination, messageSent);
			timeAfterSend = System.currentTimeMillis();

			long exp = messageSent.getJMSExpiration();
			logger.log(Logger.Level.INFO, "JMSExpiration is set to=" + exp);
			logger.log(Logger.Level.INFO, "Time before send=" + timeBeforeSend);
			logger.log(Logger.Level.INFO, "Time after send=" + timeAfterSend);
			logger.log(Logger.Level.INFO, "Time to Live =" + timeToLive);

			logger.log(Logger.Level.INFO, "JMSExpiration test - Recv a TextMessage");
			messageReceived = (TextMessage) consumer.receive(timeout);
			if (messageReceived == null) {
				logger.log(Logger.Level.ERROR, "Received no message NULL (unexpected)");
				pass = false;
			} else {
				if (messageReceived.getJMSExpiration() != exp) {
					logger.log(Logger.Level.ERROR, "TextMessage failed: JMSExpiration didn't set correctly = "
							+ messageReceived.getJMSExpiration());
					logger.log(Logger.Level.ERROR, "JMSExpiration was set to=" + exp);
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "TextMessage JMSExpiration passed");
				}
			}

			// send and receive Object message to Topic
			logger.log(Logger.Level.INFO, "JMSExpiration test - Send a ObjectMessage (timeToLive is forever)");
			messageSentO = context.createObjectMessage();
			messageSentO.setObject("msgHdrJMSExpirationTest for ObjectMessage");
			messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			producer.setTimeToLive(forever);
			producer.send(destination, messageSentO);
			logger.log(Logger.Level.INFO, "JMSExpiration test - Recv a ObjectMessage");
			messageReceivedO = (ObjectMessage) consumer.receive(timeout);
			if (messageReceivedO == null) {
				logger.log(Logger.Level.ERROR, "Received no message NULL (unexpected)");
				pass = false;
			} else {
				if (messageReceivedO.getJMSExpiration() != forever) {
					logger.log(Logger.Level.INFO, "ObjectMessage JMSExpiration failed");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "ObjectMessage JMSExpiration passed");
				}
			}

			logger.log(Logger.Level.INFO, "JMSExpiration test - Send a ObjectMessage (timeToLive is 5000)");
			producer.setTimeToLive(timeToLive);
			timeBeforeSend = System.currentTimeMillis();
			producer.send(destination, messageSentO);
			timeAfterSend = System.currentTimeMillis();

			exp = messageSentO.getJMSExpiration();
			logger.log(Logger.Level.INFO, "JMSExpiration is set to=" + exp);
			logger.log(Logger.Level.INFO, "Time before send=" + timeBeforeSend);
			logger.log(Logger.Level.INFO, "Time after send=" + timeAfterSend);
			logger.log(Logger.Level.INFO, "Time to Live =" + timeToLive);

			logger.log(Logger.Level.INFO, "JMSExpiration test - Recv a ObjectMessage");
			messageReceivedO = (ObjectMessage) consumer.receive(timeout);
			if (messageReceivedO == null) {
				logger.log(Logger.Level.ERROR, "Received no message NULL (unexpected)");
				pass = false;
			} else {
				if (messageReceivedO.getJMSExpiration() != exp) {
					logger.log(Logger.Level.ERROR, "ObjectMessage failed: JMSExpiration didn't set correctly = "
							+ messageReceivedO.getJMSExpiration());
					logger.log(Logger.Level.ERROR, "JMSExpiration was set to=" + exp);
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "ObjectMessage JMSExpiration passed");
				}
			}

			// send and receive map message to Topic
			logger.log(Logger.Level.INFO, "JMSExpiration test - Send a MapMessage (timeToLive is forever)");
			messageSentM = context.createMapMessage();
			messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentM.setString("aString", "value");
			producer.setTimeToLive(forever);
			producer.send(destination, messageSentM);
			logger.log(Logger.Level.INFO, "JMSExpiration test - Recv a MapMessage");
			messageReceivedM = (MapMessage) consumer.receive(timeout);
			if (messageReceivedM == null) {
				logger.log(Logger.Level.ERROR, "Received no message NULL (unexpected)");
				pass = false;
			} else {
				if (messageReceivedM.getJMSExpiration() != forever) {
					logger.log(Logger.Level.INFO, "MapMessage JMSExpiration failed");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "MapMessage JMSExpiration passed");
				}
			}

			logger.log(Logger.Level.INFO, "JMSExpiration test - Send a MapMessage (timeToLive is 5000)");
			producer.setTimeToLive(timeToLive);
			timeBeforeSend = System.currentTimeMillis();
			producer.send(destination, messageSentM);
			timeAfterSend = System.currentTimeMillis();

			exp = messageSentM.getJMSExpiration();
			logger.log(Logger.Level.INFO, "JMSExpiration is set to=" + exp);
			logger.log(Logger.Level.INFO, "Time before send=" + timeBeforeSend);
			logger.log(Logger.Level.INFO, "Time after send=" + timeAfterSend);
			logger.log(Logger.Level.INFO, "Time to Live =" + timeToLive);

			logger.log(Logger.Level.INFO, "JMSExpiration test - Recv a MapMessage");
			messageReceivedM = (MapMessage) consumer.receive(timeout);
			if (messageReceivedM == null) {
				logger.log(Logger.Level.ERROR, "Received no message NULL (unexpected)");
				pass = false;
			} else {
				if (messageReceivedM.getJMSExpiration() != exp) {
					logger.log(Logger.Level.ERROR, "MapMessage failed: JMSExpiration didn't set correctly = "
							+ messageReceivedM.getJMSExpiration());
					logger.log(Logger.Level.ERROR, "JMSExpiration was set to=" + exp);
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "MapMessage JMSExpiration passed");
				}
			}

			// send and receive bytes message to Topic
			logger.log(Logger.Level.INFO, "JMSExpiration test - Send a Bytesessage (timeToLive is forever)");
			messageSentB = context.createBytesMessage();
			messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentB.writeByte(bValue);
			producer.setTimeToLive(forever);
			producer.send(destination, messageSentB);
			logger.log(Logger.Level.INFO, "JMSExpiration test - Recv a BytesMessage");
			messageReceivedB = (BytesMessage) consumer.receive(timeout);
			if (messageReceivedB == null) {
				logger.log(Logger.Level.ERROR, "Received no message NULL (unexpected)");
				pass = false;
			} else {
				if (messageReceivedB.getJMSExpiration() != forever) {
					logger.log(Logger.Level.INFO, "BytesMessage JMSExpiration failed");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "BytesMessage JMSExpiration passed");
				}
			}

			logger.log(Logger.Level.INFO, "JMSExpiration test - Send a Bytesessage (timeToLive is 5000)");
			producer.setTimeToLive(timeToLive);
			timeBeforeSend = System.currentTimeMillis();
			producer.send(destination, messageSentB);
			timeAfterSend = System.currentTimeMillis();

			exp = messageSentB.getJMSExpiration();
			logger.log(Logger.Level.INFO, "JMSExpiration is set to=" + exp);
			logger.log(Logger.Level.INFO, "Time before send=" + timeBeforeSend);
			logger.log(Logger.Level.INFO, "Time after send=" + timeAfterSend);
			logger.log(Logger.Level.INFO, "Time to Live =" + timeToLive);

			logger.log(Logger.Level.INFO, "JMSExpiration test - Recv a BytesMessage");
			messageReceivedB = (BytesMessage) consumer.receive(timeout);
			if (messageReceivedB == null) {
				logger.log(Logger.Level.ERROR, "Received no message NULL (unexpected)");
				pass = false;
			} else {
				if (messageReceivedB.getJMSExpiration() != exp) {
					logger.log(Logger.Level.ERROR, "BytesMessage failed: JMSExpiration didn't set correctly = "
							+ messageReceivedB.getJMSExpiration());
					logger.log(Logger.Level.ERROR, "JMSExpiration was set to=" + exp);
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "BytesMessage JMSExpiration passed");
				}
			}

			// Send and receive a StreamMessage
			logger.log(Logger.Level.INFO, "JMSExpiration test - Send a Streamessage (timeToLive is forever)");
			messageSentS = context.createStreamMessage();
			messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentS.writeString("Testing...");
			producer.setTimeToLive(forever);
			producer.send(destination, messageSentS);
			logger.log(Logger.Level.INFO, "JMSExpiration test - Recv a StreamMessage");
			messageReceivedS = (StreamMessage) consumer.receive(timeout);
			if (messageReceivedS == null) {
				logger.log(Logger.Level.ERROR, "Received no message NULL (unexpected)");
				pass = false;
			} else {
				if (messageReceivedS.getJMSExpiration() != forever) {
					logger.log(Logger.Level.INFO, "StreamMessage JMSExpiration failed");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "StreamMessage JMSExpiration passed");
				}
			}

			logger.log(Logger.Level.INFO, "JMSExpiration test - Send a StreamMessage (timeToLive is 5000)");
			producer.setTimeToLive(timeToLive);
			timeBeforeSend = System.currentTimeMillis();
			producer.send(destination, messageSentS);
			timeAfterSend = System.currentTimeMillis();

			exp = messageSentS.getJMSExpiration();
			logger.log(Logger.Level.INFO, "JMSExpiration is set to=" + exp);
			logger.log(Logger.Level.INFO, "Time before send=" + timeBeforeSend);
			logger.log(Logger.Level.INFO, "Time after send=" + timeAfterSend);
			logger.log(Logger.Level.INFO, "Time to Live =" + timeToLive);

			logger.log(Logger.Level.INFO, "JMSExpiration test - Recv a StreamMessage");
			messageReceivedS = (StreamMessage) consumer.receive(timeout);
			if (messageReceivedS == null) {
				logger.log(Logger.Level.ERROR, "Received no message NULL (unexpected)");
				pass = false;
			} else {
				if (messageReceivedS.getJMSExpiration() != exp) {
					logger.log(Logger.Level.ERROR, "StreamMessage failed: JMSExpiration didn't set correctly = "
							+ messageReceivedS.getJMSExpiration());
					logger.log(Logger.Level.ERROR, "JMSExpiration was set to=" + exp);
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "StreamMessage JMSExpiration passed");
				}
			}

			if (!pass) {
				throw new Exception("Error: invalid JMSExpiration returned from JMS Header");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.INFO, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception(testName);
		}
	}

	/*
	 * @testName: msgHdrJMSDeliveryModeTest
	 * 
	 * @assertion_ids: JMS:SPEC:3; JMS:SPEC:140; JMS:SPEC:246.2; JMS:JAVADOC:1192;
	 * JMS:JAVADOC:1259; JMS:JAVADOC:367;
	 * 
	 * @test_Strategy: 1. Create and send a message to the default Topic. Receive
	 * the msg and verify that JMSDeliveryMode is set the default delivery mode of
	 * persistent. 2. Create and test another message with a nonpersistent delivery
	 * mode. Test with Text, map, object, byte, and stream messages 3. Set
	 * JMSDeliveryMode to Message after receive. Verify that JMSDeliveryMode is set
	 * correctly.
	 */
	@Test
	public void msgHdrJMSDeliveryModeTest() throws Exception {
		boolean pass = true;
		byte bValue = 127;
		String testName = "msgHdrJMSDeliveryModeTest";

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

			// send and receive Text message to Topic
			logger.log(Logger.Level.INFO, "send TextMessage to Topic with DeliveryMode.PERSISTENT.");
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			messageSent = context.createTextMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			producer.send(destination, messageSent);
			logger.log(Logger.Level.INFO, "receive TextMessage");
			messageReceived = (TextMessage) consumer.receive(timeout);
			if (messageReceived == null) {
				pass = false;
				logger.log(Logger.Level.ERROR, "TextMessage is null (unexpected)");
			} else if (messageReceived.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR, "TextMessage failed: JMSDeliveryMode should be set to persistent");
			} else {
				logger.log(Logger.Level.INFO, "TextMessage JMSDeliveryMode passed");
			}

			logger.log(Logger.Level.INFO, "send TextMessage to Topic with DeliveryMode.NON_PERSISTENT.");
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			messageSent = context.createTextMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			producer.send(destination, messageSent);
			logger.log(Logger.Level.INFO, "receive TextMessage");
			messageReceived = (TextMessage) consumer.receive(timeout);
			if (messageReceived == null) {
				pass = false;
				logger.log(Logger.Level.ERROR, "TextMessage is null (unexpected)");
			} else if (messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR, "TextMessage failed: JMSDeliveryMode should be set to non persistent");
			} else {
				logger.log(Logger.Level.INFO, "TextMessage JMSDeliveryMode passed");
			}

			// send and receive Object message to Topic
			logger.log(Logger.Level.INFO, "send ObjectMessage to Topic with DeliveryMode.PERSISTENT.");
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			messageSentO = context.createObjectMessage();
			messageSentO.setObject("Test for ObjectMessage");
			messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			producer.send(destination, messageSentO);
			logger.log(Logger.Level.INFO, "receive ObjectMessage");
			messageReceivedO = (ObjectMessage) consumer.receive(timeout);
			if (messageReceivedO == null) {
				pass = false;
				logger.log(Logger.Level.ERROR, "ObjectMessage is null (unexpected)");
			} else if (messageReceivedO.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR, "ObjectMessage failed: JMSDeliveryMode should be set to persistent");
			} else {
				logger.log(Logger.Level.INFO, "ObjectMessage JMSDeliveryMode passed");
			}

			logger.log(Logger.Level.INFO, "send ObjectMessage to Topic with DeliveryMode.NON_PERSISTENT.");
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			messageSentO = context.createObjectMessage();
			messageSentO.setObject("Test for ObjectMessage");
			messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			producer.send(destination, messageSentO);
			logger.log(Logger.Level.INFO, "receive ObjectMessage");
			messageReceivedO = (ObjectMessage) consumer.receive(timeout);
			if (messageReceivedO == null) {
				pass = false;
				logger.log(Logger.Level.ERROR, "ObjectMessage is null (unexpected)");
			} else if (messageReceivedO.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR, "ObjectMessage failed: JMSDeliveryMode should be set to non persistent");
			} else {
				logger.log(Logger.Level.INFO, "ObjectMessage JMSDeliveryMode passed");
			}

			// send and receive map message to Topic
			logger.log(Logger.Level.INFO, "send MapMessage to Topic with DeliveryMode.PERSISTENT.");
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			messageSentM = context.createMapMessage();
			messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentM.setString("aString", "value");
			producer.send(destination, messageSentM);
			logger.log(Logger.Level.INFO, "receive MapMessage");
			messageReceivedM = (MapMessage) consumer.receive(timeout);
			if (messageReceivedM == null) {
				pass = false;
				logger.log(Logger.Level.ERROR, "MapMessage is null (unexpected)");
			} else if (messageReceivedM.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR, "MapMessage failed: JMSDeliveryMode should be set to persistent");
			} else {
				logger.log(Logger.Level.INFO, "MapMessage JMSDeliveryMode passed");
			}

			logger.log(Logger.Level.INFO, "send MapMessage to Topic with DeliveryMode.NON_PERSISTENT.");
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			messageSentM = context.createMapMessage();
			messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentM.setString("aString", "value");
			producer.send(destination, messageSentM);
			logger.log(Logger.Level.INFO, "receive MapMessage");
			messageReceivedM = (MapMessage) consumer.receive(timeout);
			if (messageReceivedM == null) {
				pass = false;
				logger.log(Logger.Level.ERROR, "MapMessage is null (unexpected)");
			} else if (messageReceivedM.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR, "MapMessage failed: JMSDeliveryMode should be set to non persistent");
			} else {
				logger.log(Logger.Level.INFO, "MapMessage JMSDeliveryMode passed");
			}

			// send and receive bytes message to Topic
			logger.log(Logger.Level.INFO, "send BytesMessage to Topic with DeliveryMode.PERSISTENT.");
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			messageSentB = context.createBytesMessage();
			messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentB.writeByte(bValue);
			producer.send(destination, messageSentB);
			logger.log(Logger.Level.INFO, "receive BytesMessage");
			messageReceivedB = (BytesMessage) consumer.receive(timeout);
			if (messageReceivedB == null) {
				pass = false;
				logger.log(Logger.Level.ERROR, "BytesMessage is null (unexpected)");
			} else if (messageReceivedB.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR, "BytesMessage failed: JMSDeliveryMode should be set to persistent");
			} else {
				logger.log(Logger.Level.INFO, "BytesMessage JMSDeliveryMode passed");
			}

			logger.log(Logger.Level.INFO, "send BytesMessage to Topic with DeliveryMode.NON_PERSISTENT.");
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			messageSentB = context.createBytesMessage();
			messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentB.writeByte(bValue);
			producer.send(destination, messageSentB);
			logger.log(Logger.Level.INFO, "receive BytesMessage");
			messageReceivedB = (BytesMessage) consumer.receive(timeout);
			if (messageReceivedB == null) {
				pass = false;
				logger.log(Logger.Level.ERROR, "BytesMessage is null (unexpected)");
			} else if (messageReceivedB.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR, "BytesMessage failed: JMSDeliveryMode should be set to non persistent");
			} else {
				logger.log(Logger.Level.INFO, "BytesMessage JMSDeliveryMode passed");
			}

			// send and receive a StreamMessage
			logger.log(Logger.Level.INFO, "send StreamMessage to Topic with DeliveryMode.PERSISTENT.");
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			messageSentS = context.createStreamMessage();
			messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentS.writeString("Testing...");
			producer.send(destination, messageSentS);
			logger.log(Logger.Level.INFO, "receive StreamMessage");
			messageReceivedS = (StreamMessage) consumer.receive(timeout);
			if (messageReceivedS == null) {
				pass = false;
				logger.log(Logger.Level.ERROR, "StreamMessage is null (unexpected)");
			} else if (messageReceivedS.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR, "StreamMessage failed: JMSDeliveryMode should be set to persistent");
			} else {
				logger.log(Logger.Level.INFO, "StreamMessage JMSDeliveryMode passed");
			}

			logger.log(Logger.Level.INFO, "send StreamMessage to Topic with DeliveryMode.NON_PERSISTENT.");
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			messageSentS = context.createStreamMessage();
			messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
			messageSentS.writeString("Testing...");
			producer.send(destination, messageSentS);
			logger.log(Logger.Level.INFO, "receive StreamMessage");
			messageReceivedS = (StreamMessage) consumer.receive(timeout);
			if (messageReceivedS == null) {
				pass = false;
				logger.log(Logger.Level.ERROR, "StreamMessage is null (unexpected)");
			} else if (messageReceivedS.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.ERROR, "StreamMessage failed: JMSDeliveryMode should be set to non persistent");
			} else {
				logger.log(Logger.Level.INFO, "StreamMessage JMSDeliveryMode passed");
			}

			if (!pass) {
				throw new Exception("Error: invalid JMSDeliveryMode returned from JMS Header");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.INFO, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception(testName);
		}
	}

	/*
	 * @testName: msgHdrJMSDeliveryTimeTest
	 * 
	 * @assertion_ids: JMS:SPEC:246.11; JMS:SPEC:261; JMS:SPEC:256;
	 * JMS:JAVADOC:1257; JMS:JAVADOC:875;
	 * 
	 * @test_Strategy: Send message and verify that JMSDeliveryTime is correct with
	 * the DeliveryDelay set to 20 seconds. Test with DeliveryMode.PERSISTENT and
	 * DeliveryMode.NON_PERSISTENT.
	 *
	 * Retrieve and verify the JMSDeliveryTime
	 */
	@Test
	public void msgHdrJMSDeliveryTimeTest() throws Exception {
		boolean pass = true;
		try {
			logger.log(Logger.Level.INFO, "---------------------------------------------------------------");
			logger.log(Logger.Level.INFO, "BEGIN TEST msgHdrJMSDeliveryTimeTest with DeliveryDelay=20Secs");
			logger.log(Logger.Level.INFO, "---------------------------------------------------------------");

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage messageSnd = context.createTextMessage("This is a test!");

			logger.log(Logger.Level.INFO, "Set StringProperty COM_SUN_JMS_TESTNAME");
			messageSnd.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrJMSDeliveryTimeTest");

			logger.log(Logger.Level.INFO, "Sending message with DeliveryMode.PERSISTENT and DeliveryDelay=20Secs");
			producer.setDeliveryDelay(20000);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			producer.setPriority(Message.DEFAULT_PRIORITY);
			producer.setTimeToLive(0L);
			producer.send(destination, messageSnd);

			// Get deliverytime and current GMT time after send
			logger.log(Logger.Level.INFO, "Get JMSDeliveryTime after sending message");
			long deliverydelay = producer.getDeliveryDelay();
			long gmtTimeAfterSend = System.currentTimeMillis();
			long JMSDeliveryTimeAfterSend = messageSnd.getJMSDeliveryTime();

			logger.log(Logger.Level.INFO, "Receive message with timeout value of 21Secs");
			TextMessage messageRcv = (TextMessage) consumer.receive(21000);
			if (messageRcv == null) {
				logger.log(Logger.Level.ERROR,
						"FAILED: Message was not received after delivery delay of 30 secs elapsed");
				pass = false;
			} else {
				// Get JMSDeliverytime after receive
				logger.log(Logger.Level.INFO, "Get JMSDeliveryTime after receiving message");
				long gmtTimeAfterRecv = System.currentTimeMillis();
				long JMSDeliveryTimeAfterRecv = messageRcv.getJMSDeliveryTime();

				logger.log(Logger.Level.INFO, "Check JMSDeliverytime");
				logger.log(Logger.Level.INFO, "JMSDeliveryTime after send = " + JMSDeliveryTimeAfterSend);
				logger.log(Logger.Level.INFO, "JMSDeliveryTime after receive = " + JMSDeliveryTimeAfterRecv);
				if (JMSDeliveryTimeAfterSend == JMSDeliveryTimeAfterRecv) {
					logger.log(Logger.Level.INFO, "JMSDeliveryTimeAfterSend = JMSDeliveryTimeAfterRecv (PASS)");
				} else {
					logger.log(Logger.Level.ERROR, "JMSDeliveryTimeAfterSend != JMSDeliveryTimeAfterRecv (FAIL)");
					pass = false;
				}
				logger.log(Logger.Level.INFO, "gmtTimeAfterSend after send = " + gmtTimeAfterSend);
				logger.log(Logger.Level.INFO, "gmtTimeAfterRecv after receive = " + gmtTimeAfterRecv);
				if (gmtTimeAfterRecv >= (gmtTimeAfterSend + deliverydelay - 250)) {
					logger.log(Logger.Level.INFO, "gmtTimeAfterRecv >= (gmtTimeAfterSend + deliverydelay) (PASS)");
				} else {
					logger.log(Logger.Level.ERROR, "gmtTimeAfterRecv < (gmtTimeAfterSend + deliverydelay) (FAIL)");
					pass = false;
				}
			}

			logger.log(Logger.Level.INFO, "Sending message with DeliveryMode.NON_PERSISTENT and DeliveryDelay=20Secs");
			producer.setDeliveryDelay(20000);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			producer.setPriority(Message.DEFAULT_PRIORITY);
			producer.setTimeToLive(0L);
			producer.send(destination, messageSnd);

			// Get deliverytime and current GMT time after send
			logger.log(Logger.Level.INFO, "Get JMSDeliveryTime after sending message");
			gmtTimeAfterSend = System.currentTimeMillis();
			JMSDeliveryTimeAfterSend = messageSnd.getJMSDeliveryTime();

			logger.log(Logger.Level.INFO, "Receive message with timeout value of 21Secs");
			messageRcv = (TextMessage) consumer.receive(21000);
			if (messageRcv == null) {
				logger.log(Logger.Level.ERROR,
						"FAILED: Message was not received after delivery delay of 20 secs elapsed");
				pass = false;
			} else {
				// Get JMSDeliverytime after receive
				logger.log(Logger.Level.INFO, "Get JMSDeliveryTime after receiving message");
				long gmtTimeAfterRecv = System.currentTimeMillis();
				long JMSDeliveryTimeAfterRecv = messageRcv.getJMSDeliveryTime();

				logger.log(Logger.Level.INFO, "Check JMSDeliverytime");
				logger.log(Logger.Level.INFO, "JMSDeliveryTime after send = " + JMSDeliveryTimeAfterSend);
				logger.log(Logger.Level.INFO, "JMSDeliveryTime after receive = " + JMSDeliveryTimeAfterRecv);
				if (JMSDeliveryTimeAfterSend == JMSDeliveryTimeAfterRecv) {
					logger.log(Logger.Level.INFO, "JMSDeliveryTimeAfterSend = JMSDeliveryTimeAfterRecv (PASS)");
				} else {
					logger.log(Logger.Level.ERROR, "JMSDeliveryTimeAfterSend != JMSDeliveryTimeAfterRecv (FAIL)");
					pass = false;
				}
				logger.log(Logger.Level.INFO, "gmtTimeAfterSend after send = " + gmtTimeAfterSend);
				logger.log(Logger.Level.INFO, "gmtTimeAfterRecv after receive = " + gmtTimeAfterRecv);
				if (gmtTimeAfterRecv >= (gmtTimeAfterSend + deliverydelay - 250)) {
					logger.log(Logger.Level.INFO, "gmtTimeAfterRecv >= (gmtTimeAfterSend + deliverydelay) (PASS)");
				} else {
					logger.log(Logger.Level.ERROR, "gmtTimeAfterRecv < (gmtTimeAfterSend + deliverydelay) (FAIL)");
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "-------------------------------------------------------------");
			logger.log(Logger.Level.INFO, "END TEST msgHdrJMSDeliveryTimeTest with DeliveryDelay=20Secs");
			logger.log(Logger.Level.INFO, "-------------------------------------------------------------");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("msgHdrJMSDeliveryTimeTest", e);
		}

		if (!pass) {
			throw new Exception("msgHdrJMSDeliveryTimeTest failed");
		}
	}

	/*
	 * @testName: setGetAllPropertyTypesTest
	 *
	 * @assertion_ids: JMS:JAVADOC:1180; JMS:JAVADOC:1184; JMS:JAVADOC:1187;
	 * JMS:JAVADOC:1198; JMS:JAVADOC:1201; JMS:JAVADOC:1204; JMS:JAVADOC:1215;
	 * JMS:JAVADOC:1218; JMS:JAVADOC:1222; JMS:JAVADOC:1224; JMS:JAVADOC:1227;
	 * JMS:JAVADOC:1232; JMS:JAVADOC:1275; JMS:JAVADOC:1278; JMS:JAVADOC:1281;
	 * JMS:JAVADOC:1284; JMS:JAVADOC:1287; JMS:JAVADOC:1290; JMS:JAVADOC:1293;
	 * JMS:JAVADOC:1296; JMS:JAVADOC:1299;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * JMSProducer.setProperty(String, boolean) JMSProducer.setProperty(String,
	 * byte) JMSProducer.setProperty(String, double) JMSProducer.setProperty(String,
	 * float) JMSProducer.setProperty(String, int) JMSProducer.setProperty(String,
	 * long) JMSProducer.setProperty(String, Object) JMSProducer.setProperty(String,
	 * short) JMSProducer.setProperty(String, String)
	 * JMSProducer.getBooleanProperty(String) JMSProducer.getByteProperty(String)
	 * JMSProducer.getDoubleProperty(String) JMSProducer.getFloatProperty(String)
	 * JMSProducer.getIntProperty(String) JMSProducer.getLongProperty(String)
	 * JMSProducer.getObjectProperty(String) JMSProducer.getShortProperty(String)
	 * JMSProducer.getStringProperty(String) JMSProducer.clearProperties(String)
	 * JMSProducer.getPropertyNames() JMSProducer.propertyExists()
	 */
	@Test
	public void setGetAllPropertyTypesTest() throws Exception {
		boolean pass = true;
		boolean bool = true;
		byte bValue = 127;
		short nShort = 10;
		int nInt = 5;
		long nLong = 333;
		float nFloat = 1;
		double nDouble = 100;
		String testString = "test";
		int numPropertyNames = 16;

		try {
			logger.log(Logger.Level.INFO, "Set all JMSProducer properties");
			producer.setProperty("TESTBOOLEAN", bool);
			producer.setProperty("TESTBYTE", bValue);
			producer.setProperty("TESTDOUBLE", nDouble);
			producer.setProperty("TESTFLOAT", nFloat);
			producer.setProperty("TESTINT", nInt);
			producer.setProperty("TESTLONG", nLong);
			producer.setProperty("TESTSHORT", nShort);
			producer.setProperty("TESTSTRING", "test");
			producer.setProperty("OBJTESTBOOLEAN", Boolean.valueOf(bool));
			producer.setProperty("OBJTESTBYTE", Byte.valueOf(bValue));
			producer.setProperty("OBJTESTDOUBLE", Double.valueOf(nDouble));
			producer.setProperty("OBJTESTFLOAT", Float.valueOf(nFloat));
			producer.setProperty("OBJTESTINT", Integer.valueOf(nInt));
			producer.setProperty("OBJTESTLONG", Long.valueOf(nLong));
			producer.setProperty("OBJTESTSHORT", Short.valueOf(nShort));
			producer.setProperty("OBJTESTSTRING", "test");

			logger.log(Logger.Level.INFO, "Get all JMSProducer properties");
			if (producer.getBooleanProperty("TESTBOOLEAN") == bool) {
				logger.log(Logger.Level.INFO, "Pass: getBooleanProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getBooleanProperty");
				pass = false;
			}
			if (producer.getByteProperty("TESTBYTE") == bValue) {
				logger.log(Logger.Level.INFO, "Pass: getByteProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getByteProperty");
				pass = false;
			}
			if (producer.getLongProperty("TESTLONG") == nLong) {
				logger.log(Logger.Level.INFO, "Pass: getLongProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getLongProperty");
				pass = false;
			}
			if (producer.getStringProperty("TESTSTRING").equals(testString)) {
				logger.log(Logger.Level.INFO, "Pass: getStringProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getStringProperty");
				pass = false;
			}
			if (producer.getDoubleProperty("TESTDOUBLE") == nDouble) {
				logger.log(Logger.Level.INFO, "Pass: getDoubleProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getDoubleProperty");
				pass = false;
			}
			if (producer.getFloatProperty("TESTFLOAT") == nFloat) {
				logger.log(Logger.Level.INFO, "Pass: getFloatProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getFloatProperty");
				pass = false;
			}
			if (producer.getIntProperty("TESTINT") == nInt) {
				logger.log(Logger.Level.INFO, "Pass: getIntProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getIntProperty");
				pass = false;
			}
			if (producer.getShortProperty("TESTSHORT") == nShort) {
				logger.log(Logger.Level.INFO, "Pass: getShortProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getShortProperty");
				pass = false;
			}
			if (((Boolean) producer.getObjectProperty("OBJTESTBOOLEAN")).booleanValue() == bool) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Boolean value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Boolean value returned from getObjectProperty");
				pass = false;
			}
			if (((Byte) producer.getObjectProperty("OBJTESTBYTE")).byteValue() == bValue) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Byte value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Byte value returned from getObjectProperty");
				pass = false;
			}
			if (((Long) producer.getObjectProperty("OBJTESTLONG")).longValue() == nLong) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Long value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Long value returned from getObjectProperty");
				pass = false;
			}
			if (((String) producer.getObjectProperty("OBJTESTSTRING")).equals(testString)) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct String value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect String value returned from getObjectProperty");
				pass = false;
			}
			if (((Double) producer.getObjectProperty("OBJTESTDOUBLE")).doubleValue() == nDouble) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Double value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Double value returned from getObjectProperty");
				pass = false;
			}
			if (((Float) producer.getObjectProperty("OBJTESTFLOAT")).floatValue() == nFloat) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Float value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Float value returned from getObjectProperty");
				pass = false;
			}
			if (((Integer) producer.getObjectProperty("OBJTESTINT")).intValue() == nInt) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Integer value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Integer value returned from getObjectProperty");
				pass = false;
			}
			if (((Short) producer.getObjectProperty("OBJTESTSHORT")).shortValue() == nShort) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Short value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Short value returned from getObjectProperty");
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Now check all property names returned by JMSProducer.getPropertyNames()");
			// iterate thru the property names
			int i = 0;
			Set<String> propNames = producer.getPropertyNames();
			Iterator<String> iterator = propNames.iterator();
			do {
				String tmp = iterator.next();

				if (!tmp.startsWith("JMS")) {
					i++;
					if (tmp.equals("TESTBOOLEAN") || tmp.equals("TESTBYTE") || tmp.equals("TESTINT")
							|| tmp.equals("TESTSHORT") || tmp.equals("TESTFLOAT") || tmp.equals("TESTDOUBLE")
							|| tmp.equals("TESTSTRING") || tmp.equals("TESTLONG") || tmp.equals("OBJTESTBOOLEAN")
							|| tmp.equals("OBJTESTBYTE") || tmp.equals("OBJTESTINT") || tmp.equals("OBJTESTSHORT")
							|| tmp.equals("OBJTESTFLOAT") || tmp.equals("OBJTESTDOUBLE") || tmp.equals("OBJTESTSTRING")
							|| tmp.equals("OBJTESTLONG")) {
						logger.log(Logger.Level.INFO, "Producer Property set by client: " + tmp);
					} else {
						logger.log(Logger.Level.ERROR, "Producer Property not set by client: " + tmp);
						pass = false;
					}
				} else {
					logger.log(Logger.Level.INFO, "JMSProperty Name is: " + tmp);
				}
			} while (iterator.hasNext());
			if (i == numPropertyNames) {
				logger.log(Logger.Level.INFO, "Pass: # of properties is " + numPropertyNames + " as expected");
			} else {
				logger.log(Logger.Level.INFO, "Fail: expected " + numPropertyNames + " property names, but got " + i);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("setGetAllPropertyTypesTest");
		}

		if (!pass) {
			throw new Exception("setGetAllPropertyTypesTest failed");
		}
	}

	/*
	 * @testName: setGetAllHeaderTypesTest
	 *
	 * @assertion_ids: JMS:JAVADOC:1265; JMS:JAVADOC:1267; JMS:JAVADOC:1269;
	 * JMS:JAVADOC:1271; JMS:JAVADOC:1207; JMS:JAVADOC:1209; JMS:JAVADOC:1211;
	 * JMS:JAVADOC:1213;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * JMSProducer.setJMSCorrelationID(String);
	 * JMSProducer.setJMSCorrelationIDAsBytes(byte[]);
	 * JMSProducer.setJMSReplyTo(Destination); JMSProducer.setJMSType(String);
	 * JMSProducer.getJMSCorrelationID(); JMSProducer.getJMSCorrelationIDAsBytes();
	 * JMSProducer.getJMSReplyTo(); JMSProducer.getJMSType();
	 */
	@Test
	public void setGetAllHeaderTypesTest() throws Exception {
		boolean pass = true;

		try {

			try {
				logger.log(Logger.Level.INFO, "Set JMSProducer message header JMSCorrelationID as bytes");
				byte[] cid = "TestCorrelationID".getBytes();
				producer.setJMSCorrelationIDAsBytes(cid);

				logger.log(Logger.Level.INFO, "Get JMSProducer message header JMSCorrelationID as bytes");
				cid = producer.getJMSCorrelationIDAsBytes();
				String cidString = new String(cid);
				if (cid == null) {
					logger.log(Logger.Level.INFO, "Fail: getJMSCorrelationID returned null");
					pass = false;
				} else if (cidString.equals("TestCorrelationID")) {
					logger.log(Logger.Level.INFO, "Pass: getJMSCorrelationID returned correct value");
				} else {
					logger.log(Logger.Level.INFO, "Fail: getJMSCorrelationID returned incorrect value, got: "
							+ cidString + " expected: TestCorrelationID");
					pass = false;
				}
			} catch (java.lang.UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "UnsupportedOperationException - no further testing.");
			}

			logger.log(Logger.Level.INFO, "Set all JMSProducer message headers JMSCorrelationID, JMSType, JMSReplyTo");
			producer.setJMSCorrelationID("TestCorrelationID");
			producer.setJMSType("TestMessage");
			producer.setJMSReplyTo(topic);

			logger.log(Logger.Level.INFO, "Get all JMSProducer message headers JMSCorrelationID, JMSType, JMSReplyTo");
			String temp = null;
			Destination tempdest = null;
			temp = producer.getJMSCorrelationID();
			if (temp == null) {
				logger.log(Logger.Level.INFO, "Fail: getJMSCorrelationID returned null");
				pass = false;
			} else if (temp.equals("TestCorrelationID")) {
				logger.log(Logger.Level.INFO, "Pass: getJMSCorrelationID returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: getJMSCorrelationID returned incorrect value, got: " + temp
						+ " expected: TestCorrelationID");
				pass = false;
			}
			temp = producer.getJMSType();
			if (temp == null) {
				logger.log(Logger.Level.INFO, "Fail: getJMSType returned null");
				pass = false;
			} else if (temp.equals("TestMessage")) {
				logger.log(Logger.Level.INFO, "Pass: getJMSType returned correct value");
			} else {
				logger.log(Logger.Level.INFO,
						"Fail: getJMSType returned incorrect value, got: " + temp + " expected: TestMessage");
				pass = false;
			}
			tempdest = producer.getJMSReplyTo();
			if (tempdest == null) {
				logger.log(Logger.Level.INFO, "Fail: getJMSReplyTo returned null");
				pass = false;
			} else if (tempdest.equals(topic)) {
				logger.log(Logger.Level.INFO, "Pass: getJMSReplyTo returned correct value");
			} else {
				logger.log(Logger.Level.INFO,
						"Fail: getJMSReplyTo returned incorrect value, got: " + tempdest + " expected: " + topic);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("setGetAllHeaderTypesTest");
		}

		if (!pass) {
			throw new Exception("setGetAllHeaderTypesTest failed");
		}
	}

	/*
	 * @testName: msgPropertiesTest
	 * 
	 * @assertion_ids: JMS:SPEC:20.1; JMS:SPEC:20.2; JMS:SPEC:20.3; JMS:SPEC:20.4;
	 * JMS:SPEC:20.5; JMS:SPEC:20.6; JMS:SPEC:20.7; JMS:SPEC:20.8; JMS:SPEC:21;
	 * JMS:SPEC:23; JMS:SPEC:25; JMS:SPEC:26; JMS:SPEC:10; JMS:SPEC:27; JMS:SPEC:28;
	 * JMS:SPEC:29; JMS:SPEC:31; JMS:SPEC:32; JMS:SPEC:34; JMS:SPEC:19; JMS:SPEC:70;
	 * JMS:SPEC:71; JMS:SPEC:24; JMS:JAVADOC:1180; JMS:JAVADOC:1184;
	 * JMS:JAVADOC:1187; JMS:JAVADOC:1198; JMS:JAVADOC:1201; JMS:JAVADOC:1204;
	 * JMS:JAVADOC:1215; JMS:JAVADOC:1218; JMS:JAVADOC:1222; JMS:JAVADOC:1224;
	 * JMS:JAVADOC:1227; JMS:JAVADOC:1232; JMS:JAVADOC:1275; JMS:JAVADOC:1278;
	 * JMS:JAVADOC:1281; JMS:JAVADOC:1284; JMS:JAVADOC:1287; JMS:JAVADOC:1290;
	 * JMS:JAVADOC:1293; JMS:JAVADOC:1296; JMS:JAVADOC:1299;
	 * 
	 * @test_Strategy: Set and read properties for boolean, byte, short, int, long,
	 * float, double, and String. Verify expected results Set and read properties
	 * for Boolean, Byte, Short, Int, Long, Float, Double, and String. Verify
	 * expected results.
	 * 
	 * Call property get methods (other than getStringProperty and
	 * getObjectProperty) for non-existent properties and verify that a null pointer
	 * exception is returned. Call getStringProperty and getObjectProperty for
	 * non-existent properties and verify that a null is returned.
	 * 
	 * Set object properties and verify the correct value is returned with the
	 * getObjectProperty method.
	 * 
	 * Call the clearProperties method on the JMSProducer and verify that the
	 * message properties for that JMSProducer were deleted. Test that
	 * getObjectProperty returns a null and the getShortProperty throws a null
	 * pointer exception.
	 * 
	 * Call getJMSXPropertyNames() and verify that the names of the required JMSX
	 * properties for JMSXGroupID and JMSXGroupSeq are returned.
	 */
	@Test
	public void msgPropertiesTest() throws Exception {
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
		String message = "Where are you!";

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;

			// ------------------------------------------------------------------------------
			// Set JMSProducer message properties
			// Set properties for boolean, byte, short, int, long, float, double, and
			// String.
			// ------------------------------------------------------------------------------
			logger.log(Logger.Level.INFO, "Set primitive property types on JMSProducer");
			producer.setProperty("TESTBOOLEAN", bool);
			producer.setProperty("TESTBYTE", bValue);
			producer.setProperty("TESTDOUBLE", nDouble);
			producer.setProperty("TESTFLOAT", nFloat);
			producer.setProperty("TESTINT", nInt);
			producer.setProperty("TESTLONG", nLong);
			producer.setProperty("TESTSHORT", nShort);
			producer.setProperty("TESTSTRING", "test");

			// ------------------------------------------------------------------------------
			// Set JMSProducer message properties
			// Set properties for Boolean, Byte, Short, Int, Long, Float, Double, and
			// String.
			// ------------------------------------------------------------------------------
			logger.log(Logger.Level.INFO, "Set Object property types on JMSProducer");
			producer.setProperty("OBJTESTBOOLEAN", Boolean.valueOf(bool));
			producer.setProperty("OBJTESTBYTE", Byte.valueOf(bValue));
			producer.setProperty("OBJTESTDOUBLE", Double.valueOf(nDouble));
			producer.setProperty("OBJTESTFLOAT", Float.valueOf(nFloat));
			producer.setProperty("OBJTESTINT", Integer.valueOf(nInt));
			producer.setProperty("OBJTESTLONG", Long.valueOf(nLong));
			producer.setProperty("OBJTESTSHORT", Short.valueOf(nShort));
			producer.setProperty("OBJTESTSTRING", "test");

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			messageSent = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "messageSent=" + messageSent.getText());
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgPropertiesTest");
			producer.send(destination, messageSent);
			messageReceived = (TextMessage) consumer.receive(timeout);
			logger.log(Logger.Level.INFO, "messageReceived=" + messageReceived.getText());

			// Iterate thru the property names
			int i = 0;
			logger.log(Logger.Level.INFO, "Retrieve and verify correct # of properties set");
			propertyNames = messageReceived.getPropertyNames();
			do {
				String tmp = (String) propertyNames.nextElement();
				logger.log(Logger.Level.INFO, "Property Name is: " + tmp);
				if (tmp.indexOf("JMS") != 0)
					i++;
				else if (tmp.equals("JMSXDeliveryCount"))
					i++;
			} while (propertyNames.hasMoreElements());

			if (i == numPropertyNames) {
				logger.log(Logger.Level.INFO, "Pass: # of properties is " + numPropertyNames + " as expected");
			} else {
				logger.log(Logger.Level.INFO, "Fail: expected " + numPropertyNames + " property names, but got " + i);
				pass = false;
			}

			// -------------------------------------------------------------------------
			// Retrieve the JMSProducer properties and verify that they are correct
			// Get properties for boolean, byte, short, int, long, float, double, and
			// String.
			// ------------------------------------------------------------------------
			logger.log(Logger.Level.INFO,
					"Get properties for boolean, byte, short, int, long, float, double, and String.");
			logger.log(Logger.Level.INFO, "Retrieve and verify that JMSProducer properties were set correctly");
			if (producer.getBooleanProperty("TESTBOOLEAN") == bool) {
				logger.log(Logger.Level.INFO, "Pass: getBooleanProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getBooleanProperty");
				pass = false;
			}
			if (producer.getByteProperty("TESTBYTE") == bValue) {
				logger.log(Logger.Level.INFO, "Pass: getByteProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getByteProperty");
				pass = false;
			}
			if (producer.getLongProperty("TESTLONG") == nLong) {
				logger.log(Logger.Level.INFO, "Pass: getLongProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getLongProperty");
				pass = false;
			}
			if (producer.getStringProperty("TESTSTRING").equals(testString)) {
				logger.log(Logger.Level.INFO, "Pass: getStringProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getStringProperty");
				pass = false;
			}
			if (producer.getDoubleProperty("TESTDOUBLE") == nDouble) {
				logger.log(Logger.Level.INFO, "Pass: getDoubleProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getDoubleProperty");
				pass = false;
			}
			if (producer.getFloatProperty("TESTFLOAT") == nFloat) {
				logger.log(Logger.Level.INFO, "Pass: getFloatProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getFloatProperty");
				pass = false;
			}
			if (producer.getIntProperty("TESTINT") == nInt) {
				logger.log(Logger.Level.INFO, "Pass: getIntProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getIntProperty");
				pass = false;
			}
			if (producer.getShortProperty("TESTSHORT") == nShort) {
				logger.log(Logger.Level.INFO, "Pass: getShortProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getShortProperty");
				pass = false;
			}

			// -----------------------------------------------------------------------------
			// Retrieve the JMSProducer properties and verify that they are correct
			// Get properties for Boolean, Byte, Short, Integer, Long, Float, Double,
			// String.
			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.INFO,
					"Get properties for Boolean, Byte, Short, Integer, Long, Float, Double, String.");
			if (((Boolean) producer.getObjectProperty("OBJTESTBOOLEAN")).booleanValue() == bool) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Boolean value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Boolean value returned from getObjectProperty");
				pass = false;
			}
			if (((Byte) producer.getObjectProperty("OBJTESTBYTE")).byteValue() == bValue) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Byte value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Byte value returned from getObjectProperty");
				pass = false;
			}
			if (((Long) producer.getObjectProperty("OBJTESTLONG")).longValue() == nLong) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Long value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Long value returned from getObjectProperty");
				pass = false;
			}
			if (((String) producer.getObjectProperty("OBJTESTSTRING")).equals(testString)) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct String value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect String value returned from getObjectProperty");
				pass = false;
			}
			if (((Double) producer.getObjectProperty("OBJTESTDOUBLE")).doubleValue() == nDouble) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Double value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Double value returned from getObjectProperty");
				pass = false;
			}
			if (((Float) producer.getObjectProperty("OBJTESTFLOAT")).floatValue() == nFloat) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Float value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Float value returned from getObjectProperty");
				pass = false;
			}
			if (((Integer) producer.getObjectProperty("OBJTESTINT")).intValue() == nInt) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Integer value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Integer value returned from getObjectProperty");
				pass = false;
			}
			if (((Short) producer.getObjectProperty("OBJTESTSHORT")).shortValue() == nShort) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Short value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Short value returned from getObjectProperty");
				pass = false;
			}

			// ---------------------------------------------------------------------------
			// Set JMSProducer message headers (Set JMSCorrelationID, JMSType,
			// JMSReplyTo)
			// ---------------------------------------------------------------------------
			logger.log(Logger.Level.INFO, "Set message headers JMSCorrelationID, JMSType, JMSReplyTo on JMSProducer");
			producer.setJMSCorrelationID("TestCorrelationID");
			producer.setJMSType("TestMessage");
			producer.setJMSReplyTo(topic);

			// ---------------------------------------------------------------------------
			// Retrieve JMSProducer message headers and verify that they are set
			// correctly
			// ---------------------------------------------------------------------------
			String temp = null;
			Destination tempdest = null;
			temp = producer.getJMSCorrelationID();
			if (temp == null) {
				logger.log(Logger.Level.INFO, "Fail: getJMSCorrelationID returned null");
				pass = false;
			} else if (temp.equals("TestCorrelationID")) {
				logger.log(Logger.Level.INFO, "Pass: getJMSCorrelationID returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: getJMSCorrelationID returned incorrect value, got: " + temp
						+ " expected: TestCorrelationID");
				pass = false;
			}
			temp = producer.getJMSType();
			if (temp == null) {
				logger.log(Logger.Level.INFO, "Fail: getJMSType returned null");
				pass = false;
			} else if (temp.equals("TestMessage")) {
				logger.log(Logger.Level.INFO, "Pass: getJMSType returned correct value");
			} else {
				logger.log(Logger.Level.INFO,
						"Fail: getJMSType returned incorrect value, got: " + temp + " expected: TestMessage");
				pass = false;
			}
			tempdest = producer.getJMSReplyTo();
			if (tempdest == null) {
				logger.log(Logger.Level.INFO, "Fail: getJMSReplyTo returned null");
				pass = false;
			} else if (tempdest.equals(topic)) {
				logger.log(Logger.Level.INFO, "Pass: getJMSReplyTo returned correct value");
			} else {
				logger.log(Logger.Level.INFO,
						"Fail: getJMSReplyTo returned incorrect value, got: " + tempdest + " expected: " + topic);
				pass = false;
			}

			// --------------------------------------------------------------------------------------
			// Create a TextMessage, send it then receive it and verify that all the
			// JMSProducer
			// properties are set in the TextMessage
			// --------------------------------------------------------------------------------------
			logger.log(Logger.Level.INFO, "Create a TextMessage");
			messageSent = context.createTextMessage();
			messageSent.setText(testMessageBody);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgPropertiesTest");

			logger.log(Logger.Level.INFO, "Send the TextMessage");
			producer.send(destination, messageSent);
			logger.log(Logger.Level.INFO, "messageSent=" + messageSent.getText());

			logger.log(Logger.Level.INFO, "Receive the TextMessage");
			messageReceived = (TextMessage) consumer.receive(timeout);
			logger.log(Logger.Level.INFO, "messageReceived=" + messageReceived.getText());

			// --------------------------------------------------------------------------------------
			// Retrieve the properties from the received TextMessage and verify that
			// they are correct
			// Get properties for boolean, byte, short, int, long, float, double, and
			// String.
			// -------------------------------------------------------------------------------------
			logger.log(Logger.Level.INFO, "Retrieve and verify that TextMessage message properties were set correctly");
			if (messageReceived.getBooleanProperty("TESTBOOLEAN") == bool) {
				logger.log(Logger.Level.INFO, "Pass: getBooleanProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getBooleanProperty");
				pass = false;
			}
			if (messageReceived.getByteProperty("TESTBYTE") == bValue) {
				logger.log(Logger.Level.INFO, "Pass: getByteProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getByteProperty");
				pass = false;
			}
			if (messageReceived.getLongProperty("TESTLONG") == nLong) {
				logger.log(Logger.Level.INFO, "Pass: getLongProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getLongProperty");
				pass = false;
			}
			if (messageReceived.getStringProperty("TESTSTRING").equals(testString)) {
				logger.log(Logger.Level.INFO, "Pass: getStringProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getStringProperty");
				pass = false;
			}
			if (messageReceived.getDoubleProperty("TESTDOUBLE") == nDouble) {
				logger.log(Logger.Level.INFO, "Pass: getDoubleProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getDoubleProperty");
				pass = false;
			}
			if (messageReceived.getFloatProperty("TESTFLOAT") == nFloat) {
				logger.log(Logger.Level.INFO, "Pass: getFloatProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getFloatProperty");
				pass = false;
			}
			if (messageReceived.getIntProperty("TESTINT") == nInt) {
				logger.log(Logger.Level.INFO, "Pass: getIntProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getIntProperty");
				pass = false;
			}
			if (messageReceived.getShortProperty("TESTSHORT") == nShort) {
				logger.log(Logger.Level.INFO, "Pass: getShortProperty returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getShortProperty");
				pass = false;
			}
			if (messageReceived.getIntProperty("JMSXDeliveryCount") >= 1) {
				logger.log(Logger.Level.INFO, "Pass: getIntProperty(JMSXDeliveryCount) returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect value returned from getIntProperty(JMSXDeliveryCount)");
				pass = false;
			}

			// --------------------------------------------------------------------------------------
			// Retrieve the properties from the received TextMessage and verify that
			// they are correct
			// Get properties for Boolean, Byte, Short, Integer, Long, Float, Double,
			// and String.
			// --------------------------------------------------------------------------------------
			if (((Boolean) messageReceived.getObjectProperty("OBJTESTBOOLEAN")).booleanValue() == bool) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Boolean value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Boolean value returned from getObjectProperty");
				pass = false;
			}
			if (((Byte) messageReceived.getObjectProperty("OBJTESTBYTE")).byteValue() == bValue) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Byte value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Byte value returned from getObjectProperty");
				pass = false;
			}
			if (((Long) messageReceived.getObjectProperty("OBJTESTLONG")).longValue() == nLong) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Long value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Long value returned from getObjectProperty");
				pass = false;
			}
			if (((String) messageReceived.getObjectProperty("OBJTESTSTRING")).equals(testString)) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct String value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect String value returned from getObjectProperty");
				pass = false;
			}
			if (((Double) messageReceived.getObjectProperty("OBJTESTDOUBLE")).doubleValue() == nDouble) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Double value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Double value returned from getObjectProperty");
				pass = false;
			}
			if (((Float) messageReceived.getObjectProperty("OBJTESTFLOAT")).floatValue() == nFloat) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Float value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Float value returned from getObjectProperty");
				pass = false;
			}
			if (((Integer) messageReceived.getObjectProperty("OBJTESTINT")).intValue() == nInt) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Integer value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Integer value returned from getObjectProperty");
				pass = false;
			}
			if (((Short) messageReceived.getObjectProperty("OBJTESTSHORT")).shortValue() == nShort) {
				logger.log(Logger.Level.INFO, "Pass: getObjectProperty returned correct Short value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: incorrect Short value returned from getObjectProperty");
				pass = false;
			}

			// ---------------------------------------------------------------------------------------
			// Retrieve message headers from the received TextMessage and verify that
			// they are correct
			// ---------------------------------------------------------------------------------------
			temp = messageReceived.getJMSCorrelationID();
			if (temp == null) {
				logger.log(Logger.Level.INFO, "Fail: getJMSCorrelationID returned null");
				pass = false;
			} else if (temp.equals("TestCorrelationID")) {
				logger.log(Logger.Level.INFO, "Pass: getJMSCorrelationID returned correct value");
			} else {
				logger.log(Logger.Level.INFO, "Fail: getJMSCorrelationID returned incorrect value, got: " + temp
						+ " expected: TestCorrelationID");
				pass = false;
			}
			temp = messageReceived.getJMSType();
			if (temp == null) {
				logger.log(Logger.Level.INFO, "Fail: getJMSType returned null");
				pass = false;
			} else if (temp.equals("TestMessage")) {
				logger.log(Logger.Level.INFO, "Pass: getJMSType returned correct value");
			} else {
				logger.log(Logger.Level.INFO,
						"Fail: getJMSType returned incorrect value, got: " + temp + " expected: TestMessage");
				pass = false;
			}
			tempdest = messageReceived.getJMSReplyTo();
			if (tempdest == null) {
				logger.log(Logger.Level.INFO, "Fail: getJMSReplyTo returned null");
				pass = false;
			} else if (tempdest.equals(topic)) {
				logger.log(Logger.Level.INFO, "Pass: getJMSReplyTo returned correct value");
			} else {
				logger.log(Logger.Level.INFO,
						"Fail: getJMSReplyTo returned incorrect value, got: " + tempdest + " expected: " + topic);
				pass = false;
			}

			// ----------------------------------------------------------------------------------
			// The other property get methods (other than getStringProperty and
			// getObjectProperty)
			// must behave as if the property exists with a null value
			// ----------------------------------------------------------------------------------
			try {
				boolean b = producer.getBooleanProperty("TESTDUMMY");
				if (b != false) {
					logger.log(Logger.Level.INFO, "Fail: should have received false for getBooleanProperty");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.INFO, "Caught unexpected exception: " + e);
				pass = false;
			}
			try {
				byte value = producer.getByteProperty("TESTDUMMY");
				logger.log(Logger.Level.INFO, "Fail: NumberFormatException should have occurred for getByteProperty");
				pass = false;
			} catch (java.lang.NumberFormatException np) {
				logger.log(Logger.Level.INFO, "Pass: NumberFormatException as expected ");
			}
			try {
				short value = producer.getShortProperty("TESTDUMMY");
				logger.log(Logger.Level.INFO, "Fail: NumberFormatException should have occurred for getShortProperty");
				pass = false;
			} catch (java.lang.NumberFormatException np) {
				logger.log(Logger.Level.INFO, "Pass: NumberFormatException as expected ");
			}
			try {
				int value = producer.getIntProperty("TESTDUMMY");
				logger.log(Logger.Level.INFO, "Fail: NumberFormatException should have occurred for getIntProperty");
				pass = false;
			} catch (java.lang.NumberFormatException np) {
				logger.log(Logger.Level.INFO, "Pass: NumberFormatException as expected ");
			}
			try {
				long value = producer.getLongProperty("TESTDUMMY");
				logger.log(Logger.Level.INFO, "Fail: NumberFormatException should have occurred for getLongProperty");
				pass = false;
			} catch (java.lang.NumberFormatException np) {
				logger.log(Logger.Level.INFO, "Pass: NumberFormatException as expected ");
			}
			try {
				float value = producer.getFloatProperty("TESTDUMMY");
				logger.log(Logger.Level.INFO, "Fail: NullPointerException should have occurred for getFloatProperty");
				pass = false;
			} catch (java.lang.NullPointerException np) {
				logger.log(Logger.Level.INFO, "Pass: NullPointerException as expected ");
			}
			try {
				double value = producer.getDoubleProperty("TESTDUMMY");
				logger.log(Logger.Level.INFO, "Fail: NullPointerException should have occurred for getDoubleProperty");
				pass = false;
			} catch (java.lang.NullPointerException np) {
				logger.log(Logger.Level.INFO, "Pass: NullPointerException as expected ");
			}

			// clear JMSProducer properties
			producer.clearProperties();

			// -------------------------------------------------------------------
			// All JMSProducer properties are deleted by the clearProperties method.
			// This leaves the message with an empty set of properties.
			// -------------------------------------------------------------------
			Long aLong = (Long) producer.getObjectProperty("OBJTESTLONG");
			if (aLong == null) {
				logger.log(Logger.Level.INFO, "Pass: property was cleared");
			} else {
				logger.log(Logger.Level.INFO, "Fail: getObjectProperty should have returned null for cleared property");
				pass = false;
			}
			try {
				short aShort = producer.getShortProperty("TESTSHORT");
				logger.log(Logger.Level.INFO, "Fail: NumberFormatException should have occurred for getShortProperty");
				pass = false;
			} catch (java.lang.NumberFormatException np) {
				logger.log(Logger.Level.INFO, "Pass: NumberFormatException as expected ");
			}

			// Check that we have no property names
			Set<String> propNames = producer.getPropertyNames();
			Iterator<String> iterator = propNames.iterator();
			boolean hasElements = iterator.hasNext();
			if (hasElements) {
				logger.log(Logger.Level.INFO, "Fail: JMSProducer.getPropertyName() has properties (unexpected)");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Pass: JMSProducer.getPropertyName() has no properties (expected)");
			}

			// -------------------------------------------------------------------
			// JMSContext.getJMSXPropertyNames() method returns the
			// names of the JMSX properties supported by a connection.
			// -------------------------------------------------------------------
			try {
				ConnectionMetaData data = context.getMetaData();
				Enumeration cmd = data.getJMSXPropertyNames();
				String propName;

				if (cmd == null) {
					logger.log(Logger.Level.INFO, "Fail: no JMSX property names were returned!");
					logger.log(Logger.Level.INFO,
							"expected JMSXGroupID, JMSXGroupSeq, JMSXDeliveryCount at a miniumum");
					pass = false;
				} else {
					int iCount = 0;
					do {
						propName = (String) cmd.nextElement();
						logger.log(Logger.Level.INFO, propName);
						if (propName.equals("JMSXGroupID") || propName.equals("JMSXGroupSeq")
								|| propName.equals("JMSXDeliveryCount")) {
							iCount++;
						}
					} while (cmd.hasMoreElements());
					if (iCount > 1) {
						logger.log(Logger.Level.INFO, "Pass:");
					} else {
						logger.log(Logger.Level.INFO, "Fail: Expected property names not returned");
						pass = false;
					}
				}
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: unexpected exception: " + ee);
				pass = false;
			}
			if (!pass) {
				throw new Exception("msgPropertiesTest failed");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			e.printStackTrace();
			throw new Exception("msgPropertiesTest failed");
		}
	}

	/*
	 * @testName: msgPropertiesConversionTests
	 *
	 * @assertion_ids: JMS:SPEC:22.1; JMS:SPEC:22.2; JMS:SPEC:22.3; JMS:SPEC:22.4;
	 * JMS:SPEC:22.5; JMS:SPEC:22.6; JMS:SPEC:22.7; JMS:SPEC:22.8; JMS:SPEC:22.9;
	 * JMS:SPEC:22.10; JMS:SPEC:22.11; JMS:SPEC:22.12; JMS:SPEC:22.13;
	 * JMS:SPEC:22.14; JMS:SPEC:22.15; JMS:SPEC:22.16; JMS:JAVADOC:1180;
	 * JMS:JAVADOC:1184; JMS:JAVADOC:1187; JMS:JAVADOC:1198; JMS:JAVADOC:1201;
	 * JMS:JAVADOC:1204; JMS:JAVADOC:1215; JMS:JAVADOC:1218; JMS:JAVADOC:1222;
	 * JMS:JAVADOC:1224; JMS:JAVADOC:1227; JMS:JAVADOC:1232; JMS:JAVADOC:1275;
	 * JMS:JAVADOC:1278; JMS:JAVADOC:1281; JMS:JAVADOC:1284; JMS:JAVADOC:1287;
	 * JMS:JAVADOC:1290; JMS:JAVADOC:1293; JMS:JAVADOC:1296; JMS:JAVADOC:1299;
	 * JMS:JAVADOC:1186; JMS:JAVADOC:1189; JMS:JAVADOC:1200; JMS:JAVADOC:1203;
	 * JMS:JAVADOC:1206; JMS:JAVADOC:1217; JMS:JAVADOC:1226; JMS:JAVADOC:1229;
	 * 
	 * @test_Strategy: Create a JMSProducer, set properties for all of the primitive
	 * types verify the conversion by getting the properties.
	 */
	@Test
	public void msgPropertiesConversionTests() throws Exception {
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

			// ------------------------------------------------------------------------------
			// set properties for boolean, byte, short, int, long, float, double, and
			// String.
			// ------------------------------------------------------------------------------
			producer.setProperty("TESTBOOLEAN", bool);
			producer.setProperty("TESTBYTE", bValue);
			producer.setProperty("TESTSHORT", nShort);
			producer.setProperty("TESTINT", nInt);
			producer.setProperty("TESTFLOAT", nFloat);
			producer.setProperty("TESTDOUBLE", nDouble);
			producer.setProperty("TESTSTRING", "test");
			producer.setProperty("TESTLONG", nLong);
			producer.setProperty("TESTSTRINGTRUE", "true");
			producer.setProperty("TESTSTRINGFALSE", "false");
			producer.setProperty("TESTSTRING1", "1");

			// -------------------------------------------------------------------
			// test conversions for property values
			// -------------------------------------------------------------------
			// property set as boolean can be read only as string or boolean
			// -------------------------------------------------------------------
			// valid - boolean to string
			String myBool = producer.getStringProperty("TESTBOOLEAN");

			if (Boolean.valueOf(myBool).booleanValue() == bool) {
				logger.log(Logger.Level.INFO, "Pass: conversion from boolean to string - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from boolean to string failed");
				pass = false;
			}

			// invalid - boolean to byte
			try {
				producer.getByteProperty("TESTBOOLEAN");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: boolean to byte ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- boolean to byte");
				pass = false;
			}

			// invalid - boolean to short
			try {
				producer.getShortProperty("TESTBOOLEAN");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: boolean to short ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- boolean to short");
				pass = false;
			}

			// invalid - boolean to int
			try {
				producer.getIntProperty("TESTBOOLEAN");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: boolean to int ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception --boolean to int ");
				pass = false;
			}

			// invalid - boolean to long
			try {
				producer.getLongProperty("TESTBOOLEAN");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: boolean to long ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- boolean to long");
				pass = false;
			}

			// invalid - boolean to float
			try {
				producer.getFloatProperty("TESTBOOLEAN");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: boolean to float ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- boolean to float");
				pass = false;
			}

			// invalid - boolean to double
			try {
				producer.getDoubleProperty("TESTBOOLEAN");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: boolean to double ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- boolean to double");
				pass = false;
			}

			// -------------------------------------------------------------------
			// property set as byte can be read as a byte,short,int,long or string
			// valid - byte to string
			String myByte = producer.getStringProperty("TESTBYTE");

			if (Byte.valueOf(myByte).byteValue() == bValue) {
				logger.log(Logger.Level.INFO, "Pass: conversion from byte to string - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from byte to string failed");
				pass = false;
			}

			// valid - byte to short
			if (producer.getShortProperty("TESTBYTE") == bValue) {
				logger.log(Logger.Level.INFO, "Pass: conversion from byte to short - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from byte to short failed");
				pass = false;
			}

			// valid - byte to int
			if (producer.getIntProperty("TESTBYTE") == bValue) {
				logger.log(Logger.Level.INFO, "Pass: conversion from byte to int - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from byte to int failed");
				pass = false;
			}

			// valid - byte to long
			if (producer.getLongProperty("TESTBYTE") == bValue) {
				logger.log(Logger.Level.INFO, "Pass: conversion from byte to long - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from byte to long failed");
				pass = false;
			}

			// invalid - byte to boolean
			try {
				producer.getBooleanProperty("TESTBYTE");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: byte to boolean ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- byte to boolean");
				pass = false;
			}

			// invalid - byte to float
			try {
				producer.getFloatProperty("TESTBYTE");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: byte to float ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception --byte to float ");
				pass = false;
			}

			// invalid - byte to double
			try {
				producer.getDoubleProperty("TESTBYTE");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: byte to double ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- byte to double");
				pass = false;
			}

			// -------------------------------------------------
			// property set as short can be read as short,int,long or string
			// valid - short to string
			String myshort = producer.getStringProperty("TESTSHORT");

			if (Short.valueOf(myshort).shortValue() == nShort) {
				logger.log(Logger.Level.INFO, "Pass: conversion from short to string - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from short to string failed");
				pass = false;
			}

			// valid - short to int
			if (producer.getIntProperty("TESTSHORT") == nShort) {
				logger.log(Logger.Level.INFO, "Pass: conversion from short to int - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from short to int failed");
				pass = false;
			}

			// valid - short to long
			if (producer.getLongProperty("TESTSHORT") == nShort) {
				logger.log(Logger.Level.INFO, "Pass: conversion from short to long - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from short to long failed");
				pass = false;
			}

			// invalid - short to boolean
			try {
				producer.getBooleanProperty("TESTSHORT");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: short to boolean ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- short to boolean");
				pass = false;
			}

			// invalid - short to byte
			try {
				producer.getByteProperty("TESTSHORT");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: short to byte ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- short to byte");
				pass = false;
			}

			// invalid - short to float
			try {
				producer.getFloatProperty("TESTSHORT");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: short to float ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- short to float");
				pass = false;
			}

			// invalid - short to double
			try {
				producer.getDoubleProperty("TESTSHORT");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: short to double ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- short to double");
				pass = false;
			}

			// -------------------------------------------------
			// property set as int can be read only as int, long or string
			// valid - int to string
			if (Integer.valueOf(producer.getStringProperty("TESTINT")).intValue() == nInt) {
				logger.log(Logger.Level.INFO, "Pass: conversion from int to string - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from int to string failed");
				pass = false;
			}

			// valid - int to long
			if (producer.getLongProperty("TESTINT") == nInt) {
				logger.log(Logger.Level.INFO, "Pass: conversion from int to long - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from int to long failed");
				pass = false;
			}

			// invalid - int to boolean
			try {
				producer.getBooleanProperty("TESTINT");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: int to boolean ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- int to boolean");
				pass = false;
			}

			// invalid - int to byte
			try {
				producer.getByteProperty("TESTINT");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: int to byte ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception --  int to byte");
				pass = false;
			}

			// invalid - int to short
			try {
				producer.getShortProperty("TESTINT");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: int to short ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected -- int to short ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception ");
				pass = false;
			}

			// invalid - int to float
			try {
				producer.getFloatProperty("TESTINT");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: int to float ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- int to float");
				pass = false;
			}

			// invalid - int to double
			try {
				producer.getDoubleProperty("TESTINT");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: int to double ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- int to double");
				pass = false;
			}

			// -------------------------------------------------------------------
			// property set as long can be read only as long,or a string
			// valid - long to string
			if (Long.valueOf(producer.getStringProperty("TESTLONG")).longValue() == nLong) {
				logger.log(Logger.Level.INFO, "Pass: conversion from long to string - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from long to string failed");
				pass = false;
			}

			// invalid - long to boolean
			try {
				producer.getBooleanProperty("TESTLONG");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: long to boolean ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- long to boolean");
				pass = false;
			}

			// invalid - long to byte
			try {
				producer.getByteProperty("TESTLONG");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: long to byte ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- long to byte");
				pass = false;
			}

			// invalid - long to short
			try {
				producer.getShortProperty("TESTLONG");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: long to short ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- long to short ");
				pass = false;
			}

			// invalid - long to int
			try {
				producer.getIntProperty("TESTLONG");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: long to int ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- long to int");
				pass = false;
			}

			// invalid - long to float
			try {
				producer.getFloatProperty("TESTLONG");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: long to float ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- long to float");
				pass = false;
			}

			// invalid - long to double
			try {
				producer.getDoubleProperty("TESTLONG");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: long to double ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- long to double");
				pass = false;
			}

			// -------------------------------------------------------------------
			// property set as float can be read only as float,double or a string
			// valid - float to string
			if (Float.valueOf(producer.getStringProperty("TESTFLOAT")).floatValue() == nFloat) {
				logger.log(Logger.Level.INFO, "Pass: conversion from float to string - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from float to string failed");
				pass = false;
			}

			// valid - float to double
			if (producer.getDoubleProperty("TESTFLOAT") == nFloat) {
				logger.log(Logger.Level.INFO, "Pass: conversion from long to double - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from long to double failed");
				pass = false;
			}

			// invalid - float to boolean
			try {
				producer.getBooleanProperty("TESTFLOAT");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: float to boolean ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- float to boolean ");
				pass = false;
			}

			// invalid - float to byte
			try {
				producer.getByteProperty("TESTFLOAT");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: float to byte ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- float to byte");
				pass = false;
			}

			// invalid - float to short
			try {
				producer.getShortProperty("TESTFLOAT");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: float to short ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception - float to short ");
				pass = false;
			}

			// invalid - float to int
			try {
				producer.getIntProperty("TESTFLOAT");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: float to int ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception --- float to int");
				pass = false;
			}

			// invalid - float to long
			try {
				producer.getLongProperty("TESTFLOAT");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: float to long ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception --  float to long");
				pass = false;
			}

			// -------------------------------------------------------------------
			// property set as double can be read only as double or string
			// valid - double to string
			if (Double.valueOf(producer.getStringProperty("TESTDOUBLE")).doubleValue() == nDouble) {
				logger.log(Logger.Level.INFO, "Pass: conversion from double to string - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from double to string failed");
				pass = false;
			}

			// invalid - double to boolean
			try {
				producer.getBooleanProperty("TESTDOUBLE");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: double to boolean ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- double to boolean ");
				pass = false;
			}

			// invalid - double to byte
			try {
				producer.getByteProperty("TESTDOUBLE");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: double to byte ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- double to byte ");
				pass = false;
			}

			// invalid - double to short
			try {
				producer.getShortProperty("TESTDOUBLE");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: double to short ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- double to short");
				pass = false;
			}

			// invalid - double to int
			try {
				producer.getIntProperty("TESTDOUBLE");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: double to int ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception --- double to int ");
				pass = false;
			}

			// invalid - double to long
			try {
				producer.getLongProperty("TESTDOUBLE");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: double to long ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- double to long");
				pass = false;
			}

			// invalid - double to float
			try {
				producer.getFloatProperty("TESTDOUBLE");
				logger.log(Logger.Level.INFO,
						"Fail: MessageFormatRuntimeException should have occurred for type conversion error");
				logger.log(Logger.Level.INFO, "Fail: unsupported conversion: double to float ");
				pass = false;
			} catch (MessageFormatRuntimeException me) {
				logger.log(Logger.Level.INFO, "Pass: MessageFormatRuntimeException as expected ");
			} catch (Exception ee) {
				logger.log(Logger.Level.INFO, "Fail: Caught unexpected exception: " + ee);
				logger.log(Logger.Level.INFO, "Fail: did not catch expected Exception -- double to float");
				pass = false;
			}

			// -------------------------------------------------------------------
			// property set as string can be read as boolean, byte, short,
			// int, long, float, double, and String.
			// valid - string to boolean
			if ((producer.getBooleanProperty("TESTSTRINGTRUE")) == true) {
				logger.log(Logger.Level.INFO, "Pass: conversion from string to boolean - expect true - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from string to boolean - expect true  - failed");
				pass = false;
			}
			if ((producer.getBooleanProperty("TESTSTRINGFALSE")) == false) {
				logger.log(Logger.Level.INFO, "Pass: conversion from string to boolean expect false - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from string to boolean expect false - failed");
				pass = false;
			}

			// valid - string to byte
			if (producer.getByteProperty("TESTSTRING1") == 1) {
				logger.log(Logger.Level.INFO, "Pass: conversion from string to byte - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from string to byte failed");
				pass = false;
			}

			// valid - string to short
			if (producer.getShortProperty("TESTSTRING1") == 1) {
				logger.log(Logger.Level.INFO, "Pass: conversion from string to short - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from string to short failed");
				pass = false;
			}

			// valid - string to int
			if (producer.getIntProperty("TESTSTRING1") == 1) {
				logger.log(Logger.Level.INFO, "Pass: conversion from string to int - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from string to int failed");
				pass = false;
			}

			// valid - string to long
			if (producer.getLongProperty("TESTSTRING1") == 1) {
				logger.log(Logger.Level.INFO, "Pass: conversion from string to long - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from string to long failed");
				pass = false;
			}

			// valid - string to float
			if (producer.getFloatProperty("TESTSTRING1") == 1) {
				logger.log(Logger.Level.INFO, "Pass: conversion from string to float - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from string to float failed");
				pass = false;
			}

			// valid - string to double
			if (producer.getDoubleProperty("TESTSTRING1") == 1) {
				logger.log(Logger.Level.INFO, "Pass: conversion from string to double - ok");
			} else {
				logger.log(Logger.Level.INFO, "Fail: conversion from string to double failed");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.INFO, "Caught unexpected exception: " + e);
			throw new Exception("msgPropertiesConversionTests failed");
		}

		if (!pass) {
			throw new Exception("msgPropertiesConversionTests failed");
		}
	}

	/*
	 * @testName: msgPropertyExistTest
	 * 
	 * @assertion_ids: JMS:JAVADOC:1180; JMS:JAVADOC:1184; JMS:JAVADOC:1187;
	 * JMS:JAVADOC:1198; JMS:JAVADOC:1201; JMS:JAVADOC:1204; JMS:JAVADOC:1215;
	 * JMS:JAVADOC:1218; JMS:JAVADOC:1222; JMS:JAVADOC:1224; JMS:JAVADOC:1227;
	 * JMS:JAVADOC:1232; JMS:JAVADOC:1275; JMS:JAVADOC:1278; JMS:JAVADOC:1281;
	 * JMS:JAVADOC:1284; JMS:JAVADOC:1287; JMS:JAVADOC:1290; JMS:JAVADOC:1293;
	 * JMS:JAVADOC:1296; JMS:JAVADOC:1299;
	 * 
	 * @test_Strategy: Set and read properties for boolean, byte, short, int, long,
	 * float, double, and String. Verify expected results.
	 */
	@Test
	public void msgPropertyExistTest() throws Exception {
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
		String testMessageBody = "Testing msgPropertyExistTest";

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;

			// ------------------------------------------------------------------------------
			// set properties for boolean, byte, short, int, long, float, double, and
			// String.
			// ------------------------------------------------------------------------------
			producer.setProperty("TESTBOOLEAN", bool);
			producer.setProperty("TESTBYTE", bValue);
			producer.setProperty("TESTSHORT", nShort);
			producer.setProperty("TESTINT", nInt);
			producer.setProperty("TESTFLOAT", nFloat);
			producer.setProperty("TESTDOUBLE", nDouble);
			producer.setProperty("TESTSTRING", "test");
			producer.setProperty("TESTLONG", nLong);
			producer.setProperty("OBJTESTBOOLEAN", Boolean.valueOf(bool));

			// --------------------------------------------------------------------------------------
			// Create a TextMessage, send it then receive it and verify that all the
			// JMSProducer
			// properties are set in the TextMessage
			// --------------------------------------------------------------------------------------
			logger.log(Logger.Level.INFO, "Create a TextMessage");
			messageSent = context.createTextMessage();
			messageSent.setText(testMessageBody);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgPropertiesTest");

			logger.log(Logger.Level.INFO, "Send the TextMessage");
			producer.send(destination, messageSent);

			logger.log(Logger.Level.INFO, "Receive the TextMessage");
			messageReceived = (TextMessage) consumer.receive(timeout);

			if (messageReceived == null) {
				pass = false;
				logger.log(Logger.Level.ERROR, "messageReceived is null (unexpected)");
			} else {

				// iterate thru the property names
				int i = 0;
				propertyNames = messageReceived.getPropertyNames();
				do {
					String tmp = (String) propertyNames.nextElement();

					if (!tmp.startsWith("JMS")) {
						i++;
						if (tmp.equals("TESTBOOLEAN") || tmp.equals("TESTBYTE") || tmp.equals("TESTINT")
								|| tmp.equals("TESTSHORT") || tmp.equals("TESTFLOAT") || tmp.equals("TESTDOUBLE")
								|| tmp.equals("TESTSTRING") || tmp.equals("TESTLONG") || tmp.equals("OBJTESTBOOLEAN")
								|| tmp.equals("COM_SUN_JMS_TESTNAME")) {
							logger.log(Logger.Level.INFO, "Application Property set by client is: " + tmp);
							if (!messageReceived.propertyExists(tmp)) {
								pass = messageReceived.propertyExists(tmp);
								logger.log(Logger.Level.ERROR, "Positive propertyExists test failed for " + tmp);
							} else if (messageReceived.propertyExists(tmp + "1")) {
								pass = false;
								logger.log(Logger.Level.ERROR, "Negative propertyExists test failed for " + tmp + "1");
							}
						} else {
							logger.log(Logger.Level.ERROR, "Appclication Property not set by client: " + tmp);
							pass = false;
						}
					} else {
						logger.log(Logger.Level.INFO, "JMSProperty Name is: " + tmp);
					}
				} while (propertyNames.hasMoreElements());
			}

			if (!pass) {
				throw new Exception("msgPropertyExistTest failed");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.INFO, "Caught unexpected exception: " + e);
			throw new Exception("msgPropertyExistTest failed");
		}
	}

	/*
	 * @testName: msgJMSXPropertiesTest
	 * 
	 * @assertion_ids: JMS:SPEC:34; JMS:SPEC:34.3; JMS:SPEC:34.4; JMS:SPEC:34.5;
	 * JMS:SPEC:257;
	 *
	 * @test_Strategy: Set and read JMSX properties JMSXGroupID and JMSXGroupSeq.
	 * Verify the value of the JMSX properties JMSXGroupID and JMSXGroupSeq are the
	 * same as set by client. Verify that the JMS provider sets the mandatory
	 * JMSXDeliveryCount.
	 *
	 * 1) Create JMSContext and JMSConsumer for Topic. 2) Create TextMessage and set
	 * JMSXGroupID and JMSXGroupSeq message properties. 3) Send the TextMessage to
	 * the Topic. 4) Receive the TextMessage from the Topic. 5) Verify the
	 * TextMessage. Verify that the message properites JMSXGroupID, JMSXGroupSeq,
	 * and JMSXDeliveryCount are correct.
	 */
	@Test
	public void msgJMSXPropertiesTest() throws Exception {
		boolean pass = true;
		String message = "Testing msgJMSXPropertiesTest";
		int seq = 123450;
		String id = "msgJMSXPropertiesTest";
		try {
			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);

			logger.log(Logger.Level.INFO, "Set StringProperty COM_SUN_JMS_TESTNAME");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "msgJMSXPropertiesTest");

			logger.log(Logger.Level.INFO, "Set JMSXGroupID and JMSXGroupSeq");
			expTextMessage.setStringProperty("JMSXGroupID", id);
			expTextMessage.setIntProperty("JMSXGroupSeq", seq);

			logger.log(Logger.Level.INFO, "Send the TextMessage");
			producer.send(destination, expTextMessage);

			logger.log(Logger.Level.INFO, "Receive the TextMessage");
			TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);

			logger.log(Logger.Level.INFO, "Verify the value in TextMessage");
			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}

			if (actTextMessage.getText().equals(expTextMessage.getText())) {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
						+ ", received " + actTextMessage.getText());
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Verify properties JMSXGroupID, JMSXGroupSeq, JMSXDeliveryCount in TextMessage");
			if (actTextMessage.propertyExists("JMSXGroupID")) {
				if (actTextMessage.getStringProperty("JMSXGroupID").equals(id)) {
					logger.log(Logger.Level.INFO, "Pass: getStringProperty(JMSXGroupID) returned correct value");
				} else {
					logger.log(Logger.Level.ERROR,
							"Fail: incorrect value returned from getStringProperty(JMSXGroupID)");
					pass = false;
				}
			} else {
				logger.log(Logger.Level.ERROR, "Fail: TextMessage does not contain expected JMSXGroupID property");
				pass = false;
			}

			if (actTextMessage.propertyExists("JMSXGroupSeq")) {
				if (actTextMessage.getIntProperty("JMSXGroupSeq") == seq) {
					logger.log(Logger.Level.INFO, "Pass: getIntProperty(JMSXGroupSeq) returned correct value");
				} else {
					logger.log(Logger.Level.ERROR, "Fail: incorrect value returned from getIntProperty(JMSXGroupSeq)");
					pass = false;
				}
			} else {
				logger.log(Logger.Level.ERROR, "Fail: TextMessage does not contain expected JMSXGroupSeq property");
				pass = false;
			}

			if (actTextMessage.propertyExists("JMSXDeliveryCount")) {
				if (actTextMessage.getIntProperty("JMSXDeliveryCount") == 1) {
					logger.log(Logger.Level.INFO, "Pass: getIntProperty(JMSXDeliveryCount) returned correct value");
				} else {
					logger.log(Logger.Level.ERROR,
							"Fail: incorrect value returned from getIntProperty(JMSXDeliveryCount)");
					pass = false;
				}
			} else {
				logger.log(Logger.Level.ERROR,
						"Fail: TextMessage does not contain expected JMSXDeliveryCount property");
				pass = false;
			}

			// -------------------------------------------------------------------
			// ConnectionMetaData.getJMSXPropertyNames() method returns the
			// names of the JMSX properties supported by a connection.
			// -------------------------------------------------------------------
			logger.log(Logger.Level.INFO, "Verify the JMSXProperties in ConnectionData");
			try {
				logger.log(Logger.Level.INFO, "Get ConnectionMetaData");
				ConnectionMetaData data = context.getMetaData();
				logger.log(Logger.Level.INFO, "Get JMSXPropertyNames");
				Enumeration cmd = data.getJMSXPropertyNames();
				logger.log(Logger.Level.INFO, "Verify that we have JMSXGroupID, JMSXGroupSeq, JMSXDeliveryCount");
				if (cmd == null) {
					logger.log(Logger.Level.ERROR, "No JMSX property names were returned (Failed)");
					pass = false;
				} else {
					int iCount = 0;
					do {
						String propName = (String) cmd.nextElement();
						logger.log(Logger.Level.TRACE, "Found JMSX property [" + propName + "]");
						if (propName.equals("JMSXGroupID"))
							iCount++;
						else if (propName.equals("JMSXGroupSeq"))
							iCount++;
						else if (propName.equals("JMSXDeliveryCount"))
							iCount++;
					} while (cmd.hasMoreElements());

					if (iCount > 2) {
						logger.log(Logger.Level.INFO, "Expected JMSX property names were returned (Passed)");
					} else {
						logger.log(Logger.Level.ERROR, "Expected JMSX property names not returned (Failed)");
						pass = false;
					}
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught exception: " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("msgJMSXPropertiesTest", e);
		}

		if (!pass) {
			throw new Exception("msgJMSXPropertiesTest failed");
		}
	}

	/*
	 * @testName: setPropertyExceptionTests
	 *
	 * @assertion_ids: JMS:JAVADOC:1280; JMS:JAVADOC:1283; JMS:JAVADOC:1286;
	 * JMS:JAVADOC:1289; JMS:JAVADOC:1292; JMS:JAVADOC:1295; JMS:JAVADOC:1298;
	 * JMS:JAVADOC:1301; JMS:JAVADOC:1277; JMS:JAVADOC:1302;
	 *
	 * @test_Strategy: Tests IllegalArgumentException and
	 * MessageFormatRuntimeException conditions from the following API's:
	 *
	 * JMSProducer.setProperty(String, boolean) throws IllegalArgumentException
	 * JMSProducer.setProperty(String, byte)throws IllegalArgumentException
	 * JMSProducer.setProperty(String, double)throws IllegalArgumentException
	 * JMSProducer.setProperty(String, float)throws IllegalArgumentException
	 * JMSProducer.setProperty(String, int)throws IllegalArgumentException
	 * JMSProducer.setProperty(String, long)throws IllegalArgumentException
	 * JMSProducer.setProperty(String, Object)throws IllegalArgumentException
	 * JMSProducer.setProperty(String, short)throws IllegalArgumentException
	 * JMSProducer.setProperty(String, String)throws IllegalArgumentException
	 * JMSProducer.setProperty(String, Object)throws MessageFormatRuntimeException
	 */
	@Test
	public void setPropertyExceptionTests() throws Exception {
		boolean pass = true;
		boolean bool = true;
		byte bValue = 127;
		short nShort = 10;
		int nInt = 5;
		long nLong = 333;
		float nFloat = 1;
		double nDouble = 100;
		String testString = "test";

		try {
			// Create JMSProducer from JMSContext
			logger.log(Logger.Level.INFO, "Create a JMSProducer from JMSContext");
			producer = context.createProducer();

			logger.log(Logger.Level.INFO, "Test IllegalArgumentException from all JMSProducer setProperty() API's");
			try {
				logger.log(Logger.Level.INFO, "Test IllegalArgumentException for setProperty(\"\", boolean)");
				producer.setProperty("", bool);
				logger.log(Logger.Level.ERROR, "Fail: Did not throw expected IllegalArgumentException");
				pass = false;
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Pass: Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: Caught unexpected exception: " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Test IllegalArgumentException for setProperty(\"\", byte)");
				producer.setProperty("", bValue);
				logger.log(Logger.Level.ERROR, "Fail: Did not throw expected IllegalArgumentException");
				pass = false;
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Pass: Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: Caught unexpected exception: " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Test IllegalArgumentException for setProperty(\"\", double)");
				producer.setProperty("", nDouble);
				logger.log(Logger.Level.ERROR, "Fail: Did not throw expected IllegalArgumentException");
				pass = false;
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Pass: Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: Caught unexpected exception: " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Test IllegalArgumentException for setProperty(\"\", float)");
				producer.setProperty("", nFloat);
				logger.log(Logger.Level.ERROR, "Fail: Did not throw expected IllegalArgumentException");
				pass = false;
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Pass: Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: Caught unexpected exception: " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Test IllegalArgumentException for setProperty(\"\", int)");
				producer.setProperty("", nInt);
				logger.log(Logger.Level.ERROR, "Fail: Did not throw expected IllegalArgumentException");
				pass = false;
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Pass: Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: Caught unexpected exception: " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Test IllegalArgumentException for setProperty(\"\", long)");
				producer.setProperty("", nLong);
				logger.log(Logger.Level.ERROR, "Fail: Did not throw expected IllegalArgumentException");
				pass = false;
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Pass: Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: Caught unexpected exception: " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Test IllegalArgumentException for setProperty(\"\", short)");
				producer.setProperty("", nShort);
				logger.log(Logger.Level.ERROR, "Fail: Did not throw expected IllegalArgumentException");
				pass = false;
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Pass: Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: Caught unexpected exception: " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Test IllegalArgumentException for setProperty(\"\", String)");
				producer.setProperty("", "test");
				logger.log(Logger.Level.ERROR, "Fail: Did not throw expected IllegalArgumentException");
				pass = false;
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Pass: Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: Caught unexpected exception: " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Test IllegalArgumentException for setProperty(\"\", Object)");
				producer.setProperty("", Long.valueOf(nLong));
				logger.log(Logger.Level.ERROR, "Fail: Did not throw expected IllegalArgumentException");
				pass = false;
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Pass: Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: Caught unexpected exception: " + e);
				pass = false;
			}

			try {
				logger.log(Logger.Level.INFO, "Test IllegalArgumentException for setProperty(null, boolean)");
				producer.setProperty(null, bool);
				logger.log(Logger.Level.ERROR, "Fail: Did not throw expected IllegalArgumentException");
				pass = false;
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Pass: Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: Caught unexpected exception: " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Test IllegalArgumentException for setProperty(null, byte)");
				producer.setProperty(null, bValue);
				logger.log(Logger.Level.ERROR, "Fail: Did not throw expected IllegalArgumentException");
				pass = false;
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Pass: Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: Caught unexpected exception: " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Test IllegalArgumentException for setProperty(null, double)");
				producer.setProperty(null, nDouble);
				logger.log(Logger.Level.ERROR, "Fail: Did not throw expected IllegalArgumentException");
				pass = false;
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Pass: Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: Caught unexpected exception: " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Test IllegalArgumentException for setProperty(null, float)");
				producer.setProperty(null, nFloat);
				logger.log(Logger.Level.ERROR, "Fail: Did not throw expected IllegalArgumentException");
				pass = false;
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Pass: Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: Caught unexpected exception: " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Test IllegalArgumentException for setProperty(null, int)");
				producer.setProperty(null, nInt);
				logger.log(Logger.Level.ERROR, "Fail: Did not throw expected IllegalArgumentException");
				pass = false;
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Pass: Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: Caught unexpected exception: " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Test IllegalArgumentException for setProperty(null, long)");
				producer.setProperty(null, nLong);
				logger.log(Logger.Level.ERROR, "Fail: Did not throw expected IllegalArgumentException");
				pass = false;
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Pass: Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: Caught unexpected exception: " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Test IllegalArgumentException for setProperty(null, short)");
				producer.setProperty(null, nShort);
				logger.log(Logger.Level.ERROR, "Fail: Did not throw expected IllegalArgumentException");
				pass = false;
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Pass: Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: Caught unexpected exception: " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Test IllegalArgumentException for setProperty(null, String)");
				producer.setProperty(null, "test");
				logger.log(Logger.Level.ERROR, "Fail: Did not throw expected IllegalArgumentException");
				pass = false;
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Pass: Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: Caught unexpected exception: " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Test IllegalArgumentException for setProperty(null, Object)");
				producer.setProperty(null, Long.valueOf(nLong));
				logger.log(Logger.Level.ERROR, "Fail: Did not throw expected IllegalArgumentException");
				pass = false;
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Pass: Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: Caught unexpected exception: " + e);
				pass = false;
			}

			try {
				logger.log(Logger.Level.INFO, "Test MessageFormatRuntimeException for setProperty(String, Object)");
				producer.setProperty("name1", new java.util.Date());
				logger.log(Logger.Level.ERROR, "Fail: Did not throw expected MessageFormatRuntimeException");
				pass = false;
			} catch (MessageFormatRuntimeException e) {
				logger.log(Logger.Level.INFO, "Pass: Caught expected MessageFormatRuntimeException: " + e);
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Fail: Caught unexpected exception: " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("setPropertyExceptionTests");
		}

		if (!pass) {
			throw new Exception("setPropertyExceptionTests failed");
		}
	}

	/*
	 * @testName: sendExceptionTests
	 *
	 * @assertion_ids: JMS:JAVADOC:1241; JMS:JAVADOC:1245; JMS:JAVADOC:1238;
	 *
	 * @test_Strategy: Tests MessageFormatRuntimeException and
	 * MessageNotWriteableRuntimeException conditions from the following API's:
	 *
	 * JMSProducer.send(Destination, Message) throws
	 * MessageNotWriteableRuntimeException JMSProducer.send(Destination, Message)
	 * throws MessageFormatRuntimeException JMSProducer.send(Destination, Map)
	 * throws MessageFormatRuntimeException
	 *
	 */
	@Test
	public void sendExceptionTests() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		boolean bool = true;
		byte bValue = 127;
		short nShort = 10;
		int nInt = 5;
		long nLong = 333;
		float nFloat = 1;
		double nDouble = 100;
		String testString = "test";
		try {
			logger.log(Logger.Level.INFO,
					"Testing JMSProducer.send(Destination, Message) for MessageFormatRuntimeException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling send(Destination, Message) -> expect MessageFormatRuntimeException");
				producer.send(destination, (Message) null);
				logger.log(Logger.Level.ERROR, "MessageFormatRuntimeException was not thrown");
				pass = false;
			} catch (MessageFormatRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected MessageFormatRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected MessageFormatRuntimeException, received " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO,
					"Testing JMSProducer.send(Destination, Map) for MessageFormatRuntimeException");
			try {
				Map<String, Object> mp = new HashMap<String, Object>();
				mp.put("1", new ArrayList(2));
				mp.put("2", new Properties());
				mp.put("2", new Properties());
				mp.put("1", new ArrayList(2));
				logger.log(Logger.Level.INFO, "Calling send(Destination, Map) -> expect MessageFormatRuntimeException");
				producer.send(destination, mp);
				logger.log(Logger.Level.ERROR, "MessageFormatRuntimeException was not thrown");
				pass = false;
			} catch (MessageFormatRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected MessageFormatRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected MessageFormatRuntimeException, received " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO,
					"Testing JMSProducer.send(Destination, Message) for MessageNotWriteableRuntimeException");
			try {
				// send and receive TextMessage
				logger.log(Logger.Level.INFO, "Create TextMessage");
				TextMessage sendTextMessage = context.createTextMessage(message);
				sendTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "sendExceptionTests");
				logger.log(Logger.Level.INFO, "Send TextMessage");
				producer.send(destination, sendTextMessage);
				logger.log(Logger.Level.INFO, "Receive TextMessage");
				TextMessage recvTextMessage = (TextMessage) consumer.receive(timeout);
				if (recvTextMessage == null) {
					logger.log(Logger.Level.ERROR, "Did not receive TextMessage");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Check the value in TextMessage");
					if (recvTextMessage.getText().equals(sendTextMessage.getText())) {
						logger.log(Logger.Level.INFO, "TextMessage is correct");
					} else {
						logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + sendTextMessage.getText()
								+ ", received " + recvTextMessage.getText());
						pass = false;
					}
				}
				logger.log(Logger.Level.INFO, "Set a bunch of JMSProducer properties");
				producer.setProperty("TESTBOOLEAN", bool);
				producer.setProperty("TESTBYTE", bValue);
				producer.setProperty("TESTDOUBLE", nDouble);
				producer.setProperty("TESTFLOAT", nFloat);
				producer.setProperty("TESTINT", nInt);
				producer.setProperty("TESTLONG", nLong);
				producer.setProperty("TESTSHORT", nShort);
				producer.setProperty("TESTSTRING", testString);
				producer.setProperty("OBJTESTLONG", Long.valueOf(nLong));
				logger.log(Logger.Level.INFO,
						"Using received TextMessage try and send it (expect MessageNotWriteableRuntimeException)");
				producer.send(destination, recvTextMessage);
				logger.log(Logger.Level.ERROR, "MessageNotWriteableRuntimeException was not thrown");
				pass = false;
			} catch (MessageNotWriteableRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected MessageNotWriteableRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected MessageNotWriteableRuntimeException, received " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("sendExceptionTests", e);
		}

		if (!pass) {
			throw new Exception("sendExceptionTests failed");
		}
	}

	/*
	 * @testName: getBodyTests
	 *
	 * @assertion_ids: JMS:JAVADOC:1357;
	 *
	 * @test_Strategy: Send and receive messages of the following types:
	 * BytesMessage, MapMessage, ObjectMessage, TextMessage. Call Message.getBody()
	 * to return the message as the specified Object type.
	 *
	 * Object = Message.getBody(Class)
	 *
	 * Test the following:
	 *
	 * String message = Message.getBody(String.class) byte[] message =
	 * Message.getBody(byte[].class); StringBuffer message =
	 * Message.getBody(StringBuffer.class); Map message =
	 * Message.getBody(Map.class);
	 *
	 */
	@Test
	public void getBodyTests() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		StringBuffer expSbuffer = new StringBuffer("This is it!");
		try {
			// Send and receive TextMessage
			logger.log(Logger.Level.INFO, "Create TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "getBodyTests");
			logger.log(Logger.Level.INFO, "Send and receive the TextMessage");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receive(long) to receive TextMessage");
			TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Call TextMessage.getBody(String.class) to extract TextMessage as String");
			String actMessage = actTextMessage.getBody(String.class);
			logger.log(Logger.Level.INFO, "Check the value in String");
			if (actMessage.equals(message)) {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR,
						"TextMessage is incorrect expected " + message + ", received " + actMessage);
				pass = false;
			}

			// Send and receive ObjectMessage
			logger.log(Logger.Level.INFO, "Create ObjectMessage");
			ObjectMessage expObjectMessage = context.createObjectMessage(expSbuffer);
			logger.log(Logger.Level.INFO, "Set some values in ObjectMessage");
			expObjectMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "getBodyTests");
			logger.log(Logger.Level.INFO, "Send and receive the ObjectMessage");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expObjectMessage);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receive(long) to receive ObjectMessage");
			ObjectMessage actObjectMessage = (ObjectMessage) consumer.receive(timeout);
			if (actObjectMessage == null) {
				throw new Exception("Did not receive ObjectMessage");
			}
			logger.log(Logger.Level.INFO,
					"Call ObjectMessage.getBody(StringBuffer.class) to extract ObjectMessage as StringBuffer");
			StringBuffer actSbuffer = actObjectMessage.getBody(StringBuffer.class);
			logger.log(Logger.Level.INFO, "Check the value in StringBuffer");
			if (actSbuffer.toString().equals(expSbuffer.toString())) {
				logger.log(Logger.Level.INFO, "ObjectMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR,
						"ObjectMessage is incorrect expected " + expSbuffer + ", received " + actSbuffer);
				pass = false;
			}

			// Send and receive BytesMessage
			logger.log(Logger.Level.INFO, "Create BytesMessage");
			BytesMessage bMsg = context.createBytesMessage();
			logger.log(Logger.Level.INFO, "Set some values in BytesMessage");
			bMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "getBodyTests");
			bMsg.writeByte((byte) 1);
			bMsg.writeInt((int) 22);
			logger.log(Logger.Level.INFO, "Send and receive the BytesMessage");
			producer.send(destination, bMsg);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receive(long) to receive BytesMessage");
			BytesMessage actBytesMessage = (BytesMessage) consumer.receive(timeout);
			if (actBytesMessage == null) {
				throw new Exception("Did not receive BytesMessage");
			}
			logger.log(Logger.Level.INFO,
					"Call BytesMessage.getBody(StringBuffer.class) to extract BytesMessage as byte[] array");
			byte[] bytes = actBytesMessage.getBody(byte[].class);
			if (bytes == null) {
				logger.log(Logger.Level.ERROR, "Did not receive BytesMessage");
				pass = false;
			} else {
				try {
					DataInputStream di = new DataInputStream(new ByteArrayInputStream(bytes));
					logger.log(Logger.Level.INFO, "Check the values in BytesMessage");
					if (di.readByte() == (byte) 1) {
						logger.log(Logger.Level.INFO, "bytevalue is correct");
					} else {
						logger.log(Logger.Level.INFO, "bytevalue is incorrect");
						pass = false;
					}
					if (di.readInt() == (int) 22) {
						logger.log(Logger.Level.INFO, "intvalue is correct");
					} else {
						logger.log(Logger.Level.INFO, "intvalue is incorrect");
						pass = false;
					}
					try {
						byte b = di.readByte();
					} catch (EOFException e) {
						logger.log(Logger.Level.INFO, "Caught expected EOFException");
					} catch (Exception e) {
						logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
						pass = false;
					}
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
					pass = false;
				}
			}
			// Send and receive MapMessage
			logger.log(Logger.Level.INFO, "Send MapMessage");
			MapMessage mMsg = context.createMapMessage();
			logger.log(Logger.Level.INFO, "Set some values in MapMessage");
			mMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "getBodyTests");
			mMsg.setBoolean("booleanvalue", true);
			mMsg.setInt("intvalue", (int) 10);
			producer.send(destination, mMsg);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receive(long) to receive MapMessage");
			MapMessage actMapMessage = (MapMessage) consumer.receive(timeout);
			if (actMapMessage == null) {
				throw new Exception("Did not receive MapMessage");
			}
			logger.log(Logger.Level.INFO, "Call MapMessage.getBody(Map.class) to extract MapMessage as a Map object");
			Map map = actMapMessage.getBody(Map.class);
			if (map == null) {
				logger.log(Logger.Level.ERROR, "Did not receive MapMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the values in MapMessage");
				logger.log(Logger.Level.INFO, "map.size()=" + map.size());
				if (map.size() != 2) {
					logger.log(Logger.Level.ERROR, "Map size is " + map.size() + ", expected 2");
					pass = false;
				}
				Iterator<String> it = map.keySet().iterator();
				String name = null;
				while (it.hasNext()) {
					name = (String) it.next();
					if (name.equals("booleanvalue")) {
						if ((boolean) map.get(name) == true) {
							logger.log(Logger.Level.INFO, "booleanvalue is correct");
						} else {
							logger.log(Logger.Level.ERROR, "booleanvalue is incorrect");
							pass = false;
						}
					} else if (name.equals("intvalue")) {
						if ((int) map.get(name) == 10) {
							logger.log(Logger.Level.INFO, "intvalue is correct");
						} else {
							logger.log(Logger.Level.ERROR, "intvalue is incorrect");
							pass = false;
						}
					} else {
						logger.log(Logger.Level.ERROR, "Unexpected name of [" + name + "] in MapMessage");
						pass = false;
					}
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("getBodyTests", e);
		} finally {
			try {
				if (consumer != null)
					consumer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("getBodyTests failed");
		}
	}

	/*
	 * @testName: getBodyExceptionTests
	 *
	 * @assertion_ids: JMS:JAVADOC:1359;
	 *
	 * @test_Strategy: Test exception case for Message.getBody(Class). Test
	 * MessageFormatException.
	 *
	 * Object = Message.getBody(Class)
	 *
	 */
	@Test
	public void getBodyExceptionTests() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		StringBuffer expSbuffer = new StringBuffer("This is it!");
		try {
			// Send and receive TextMessage
			logger.log(Logger.Level.INFO, "Create TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "getBodyExceptionTests");
			logger.log(Logger.Level.INFO, "Send and receive the TextMessage");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receive(long) to receive TextMessage");
			TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Call TextMessage.getBody(Boolean.class) to extract TextMessage "
					+ "as Boolean (expect MessageFormatException)");
			try {
				Boolean myBool = actTextMessage.getBody(Boolean.class);
				logger.log(Logger.Level.ERROR, "Expected MessageFormatException to be thrown");
				pass = false;
			} catch (MessageFormatException e) {
				logger.log(Logger.Level.INFO, "Caught correct MessageFormatException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			// Send and receive ObjectMessage
			logger.log(Logger.Level.INFO, "Create ObjectMessage of type StringBuffer");
			ObjectMessage expObjectMessage = context.createObjectMessage(expSbuffer);
			logger.log(Logger.Level.INFO, "Set some values in ObjectMessage");
			expObjectMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "getBodyExceptionTests");
			logger.log(Logger.Level.INFO, "Send and receive the ObjectMessage");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expObjectMessage);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receive(long) to receive ObjectMessage");
			ObjectMessage actObjectMessage = (ObjectMessage) consumer.receive(timeout);
			if (actObjectMessage == null) {
				throw new Exception("Did not receive ObjectMessage");
			}
			logger.log(Logger.Level.INFO, "Call ObjectMessage.getBody(HashMap.class) to extract ObjectMessage "
					+ "as HashMap (expect MessageFormatException");
			try {
				HashMap hmap = actObjectMessage.getBody(HashMap.class);
				logger.log(Logger.Level.ERROR, "Expected MessageFormatException to be thrown");
				pass = false;
			} catch (MessageFormatException e) {
				logger.log(Logger.Level.INFO, "Caught correct MessageFormatException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			// send and receive StreamMessage
			logger.log(Logger.Level.INFO, "Create StreamMessage");
			StreamMessage expStreamMsg = context.createStreamMessage();
			logger.log(Logger.Level.INFO, "Set some values in StreamMessage");
			expStreamMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "getBodyExceptionTests");
			expStreamMsg.writeBoolean(true);
			expStreamMsg.writeInt((int) 22);
			logger.log(Logger.Level.INFO, "Send and receive the StreamMessage");
			producer.send(destination, expStreamMsg);
			StreamMessage actStreamMsg = (StreamMessage) consumer.receive(timeout);
			if (actStreamMsg == null) {
				throw new Exception("Did not receive StreamMessage");
			}
			logger.log(Logger.Level.INFO, "Call StreamMessage.getBody(HashMap.class) to extract StreamMessage "
					+ "as HashMap (expect MessageFormatException");
			try {
				HashMap hmap = actStreamMsg.getBody(HashMap.class);
				logger.log(Logger.Level.ERROR, "Expected MessageFormatException to be thrown");
				pass = false;
			} catch (MessageFormatException e) {
				logger.log(Logger.Level.INFO, "Caught correct MessageFormatException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			// Create BytesMessage
			logger.log(Logger.Level.INFO, "Create BytesMessage");
			BytesMessage bMsg = context.createBytesMessage();
			logger.log(Logger.Level.INFO, "Set some values in BytesMessage");
			bMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "getBodyExceptionTests");
			bMsg.writeByte((byte) 1);
			bMsg.writeInt((int) 22);
			logger.log(Logger.Level.INFO, "BytesMessage is in write-only mode");
			logger.log(Logger.Level.INFO, "Call BytesMessage.getBody(StringBuffer.class) to receive "
					+ "BytesMessage as StringBuffer(expect MessageFormatException)");
			try {
				bMsg.getBody(StringBuffer.class);
				logger.log(Logger.Level.ERROR, "Expected MessageFormatException to be thrown");
				pass = false;
			} catch (MessageFormatException e) {
				logger.log(Logger.Level.INFO, "Caught correct MessageFormatException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Send and receive the BytesMessage");
			producer.send(destination, bMsg);
			BytesMessage actBytesMsg = (BytesMessage) consumer.receive(timeout);
			try {
				actBytesMsg.getBody(StringBuffer.class);
				logger.log(Logger.Level.ERROR, "Expected MessageFormatException to be thrown");
				pass = false;
			} catch (MessageFormatException e) {
				logger.log(Logger.Level.INFO, "Caught correct MessageFormatException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("getBodyExceptionTests", e);
		} finally {
			try {
				if (consumer != null)
					consumer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("getBodyExceptionTests failed");
		}
	}

	/*
	 * @testName: isBodyAssignableToTest
	 *
	 * @assertion_ids: JMS:JAVADOC:1361;
	 *
	 * @test_Strategy: Test Message.isBodyAssignableTo(Class) API.
	 *
	 * boolean = Message.isBodyAssignableTo(Class)
	 *
	 */
	@Test
	public void isBodyAssignableToTest() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		StringBuffer expSbuffer = new StringBuffer("This is it!");
		try {
			// Send and receive TextMessage
			logger.log(Logger.Level.INFO, "Create TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "isBodyAssignableToTest");
			logger.log(Logger.Level.INFO, "Send and receive the TextMessage");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receive(long) to receive TextMessage");
			TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			try {
				logger.log(Logger.Level.INFO, "Call TextMessage.isBodyAssignableTo(Boolean.class) (expect false)");
				boolean b = actTextMessage.isBodyAssignableTo(Boolean.class);
				if (b) {
					logger.log(Logger.Level.ERROR, "Expected false got true");
					pass = false;
				}
				logger.log(Logger.Level.INFO, "Call TextMessage.isBodyAssignableTo(String.class) (expect true)");
				b = actTextMessage.isBodyAssignableTo(String.class);
				if (!b) {
					logger.log(Logger.Level.ERROR, "Expected true got false");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			// Send and receive ObjectMessage
			logger.log(Logger.Level.INFO, "Create ObjectMessage");
			ObjectMessage expObjectMessage = context.createObjectMessage(expSbuffer);
			logger.log(Logger.Level.INFO, "Set some values in ObjectMessage");
			expObjectMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "isBodyAssignableToTest");
			logger.log(Logger.Level.INFO, "Send and receive the ObjectMessage");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expObjectMessage);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receive(long) to receive ObjectMessage");
			ObjectMessage actObjectMessage = (ObjectMessage) consumer.receive(timeout);
			if (actObjectMessage == null) {
				throw new Exception("Did not receive ObjectMessage");
			}
			try {
				logger.log(Logger.Level.INFO, "Call ObjectMessage.isBodyAssignableTo(Boolean.class) (expect false)");
				boolean b = actObjectMessage.isBodyAssignableTo(Boolean.class);
				if (b) {
					logger.log(Logger.Level.ERROR, "Expected false got true");
					pass = false;
				}
				logger.log(Logger.Level.INFO,
						"Call ObjectMessage.isBodyAssignableTo(StringBuffer.class) (expect true)");
				b = actObjectMessage.isBodyAssignableTo(StringBuffer.class);
				if (!b) {
					logger.log(Logger.Level.ERROR, "Expected true got false");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			// Send and receive BytesMessage
			logger.log(Logger.Level.INFO, "Create BytesMessage");
			BytesMessage bMsg = context.createBytesMessage();
			logger.log(Logger.Level.INFO, "Set some values in BytesMessage");
			bMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "getBodyTest");
			bMsg.writeByte((byte) 1);
			bMsg.writeInt((int) 22);
			logger.log(Logger.Level.INFO, "Send and receive the BytesMessage");
			producer.send(destination, bMsg);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receive(long) to receive BytesMessage");
			BytesMessage actBytesMessage = (BytesMessage) consumer.receive(timeout);
			if (actBytesMessage == null) {
				throw new Exception("Did not receive BytesMessage");
			}
			try {
				logger.log(Logger.Level.INFO, "Call BytesMessage.isBodyAssignableTo(String.class) (expect false)");
				boolean b = actBytesMessage.isBodyAssignableTo(String.class);
				if (b) {
					logger.log(Logger.Level.ERROR, "Expected false got true");
					pass = false;
				}
				logger.log(Logger.Level.INFO, "Call BytesMessage.isBodyAssignableTo(byte[].class) (expect true)");
				b = actBytesMessage.isBodyAssignableTo(byte[].class);
				if (!b) {
					logger.log(Logger.Level.ERROR, "Expected true got false");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			// Send and receive MapMessage
			logger.log(Logger.Level.INFO, "Send MapMessage");
			MapMessage mMsg = context.createMapMessage();
			logger.log(Logger.Level.INFO, "Set some values in MapMessage");
			mMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "getBodyTest");
			mMsg.setBoolean("booleanvalue", true);
			mMsg.setInt("intvalue", (int) 10);
			producer.send(destination, mMsg);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receive(long) to receive MapMessage");
			MapMessage actMapMessage = (MapMessage) consumer.receive(timeout);
			if (actMapMessage == null) {
				throw new Exception("Did not receive MapMessage");
			}
			try {
				logger.log(Logger.Level.INFO, "Call MapMessage.isBodyAssignableTo(String.class) (expect false)");
				boolean b = actMapMessage.isBodyAssignableTo(String.class);
				if (b) {
					logger.log(Logger.Level.ERROR, "Expected false got true");
					pass = false;
				}
				logger.log(Logger.Level.INFO, "Call MapMessage.isBodyAssignableTo(Map.class) (expect true)");
				b = actMapMessage.isBodyAssignableTo(Map.class);
				if (!b) {
					logger.log(Logger.Level.ERROR, "Expected true got false");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("isBodyAssignableToTest", e);
		} finally {
			try {
				if (consumer != null)
					consumer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("isBodyAssignableToTest failed");
		}
	}
}
