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
package com.sun.ts.tests.jms.core20.jmsconsumertests;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.BytesMessage;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import jakarta.jms.JMSConsumer;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSProducer;
import jakarta.jms.JMSRuntimeException;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.MessageFormatRuntimeException;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.StreamMessage;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;


public class ClientIT {
	private static final String testName = "com.sun.ts.tests.jms.core20.jmsconsumertests.ClientIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(ClientIT.class.getName());

	// JMS tool which creates and/or looks up the JMS administered objects
	private transient JmsTool tool = null;

	// JMS objects
	private transient ConnectionFactory cf = null;

	private transient Queue queue = null;

	private transient Topic topic = null;

	private transient Destination destination = null;

	private transient JMSContext context = null;

	private transient JMSConsumer consumer = null;

	private transient JMSProducer producer = null;

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

	ArrayList queues = null;

	ArrayList connections = null;

	boolean queueTest = false;

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
			queues = new ArrayList(3);
			connections = new ArrayList(5);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
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
			logger.log(Logger.Level.INFO, "Close JMSContext");
			if (context != null)
				context.close();
			if (queueTest) {
				logger.log(Logger.Level.INFO, "Flush any messages left on Queue");
				tool.flushDestination();
			}
			logger.log(Logger.Level.INFO, "Close all connections and resources");
			tool.closeAllConnections(connections);
			tool.closeAllResources();
			producer = null;
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("cleanup failed!", e);
		}
	}

	/*
	 * @testName: queueReceiveTests
	 *
	 * @assertion_ids: JMS:JAVADOC:1102; JMS:JAVADOC:1104; JMS:JAVADOC:1106;
	 *
	 * @test_Strategy: Test the JMSConsumer receive API's. Tests the following
	 * API's:
	 *
	 * JMSConsumer.receive() JMSConsumer.receive(long timeout)
	 * JMSConsumer.receiveNoWait()
	 *
	 */
	@Test
	public void queueReceiveTests() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up JmsTool for COMMON_Q setup
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON_Q");
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			cf = tool.getConnectionFactory();
			destination = tool.getDefaultDestination();
			queue = (Queue) destination;
			logger.log(Logger.Level.INFO, "Create JMSContext with AUTO_ACKNOWLEDGE");
			context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			queueTest = true;

			// Create JMSProducer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSProducer");
			producer = context.createProducer();

			// Create JMSConsumer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSConsumer");
			consumer = context.createConsumer(destination);

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Create TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueReceiveTests");
			logger.log(Logger.Level.INFO, "Send and receive the TextMessage");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receive() to receive TextMessage");
			TextMessage actTextMessage = (TextMessage) consumer.receive();
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

			// send and receive TextMessage again
			logger.log(Logger.Level.INFO, "Send and receive the TextMessage again");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receive(long timeout) to receive TextMessage");
			actTextMessage = (TextMessage) consumer.receive(timeout);
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

			// send and receive TextMessage again
			logger.log(Logger.Level.INFO, "Send and receive the TextMessage again");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receiveNoWait() to receive TextMessage");
			actTextMessage = (TextMessage) consumer.receiveNoWait();
			if (actTextMessage == null) {
				logger.log(Logger.Level.INFO, "Did not receive message (THIS IS OK)");
				logger.log(Logger.Level.INFO, "Now block and wait for message via JMSConsumer.receive()");
				actTextMessage = (TextMessage) consumer.receive();
			}
			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage on blocking receive");
			}
			logger.log(Logger.Level.INFO, "Check the value in TextMessage");
			if (actTextMessage.getText().equals(expTextMessage.getText())) {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
						+ ", received " + actTextMessage.getText());
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Now don't send a message at all");
			logger.log(Logger.Level.INFO, "Calling receiveNoWait() again to receive TextMessage");
			actTextMessage = (TextMessage) consumer.receiveNoWait();
			if (actTextMessage != null) {
				logger.log(Logger.Level.ERROR, "actTextMessage != NULL (expected actTextMessage=NULL)");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("queueReceiveTests", e);
		} finally {
			try {
				if (consumer != null)
					consumer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("queueReceiveTests failed");
		}
	}

	/*
	 * @testName: queueReceiveBodyTests
	 *
	 * @assertion_ids: JMS:JAVADOC:1369; JMS:JAVADOC:1372; JMS:JAVADOC:1375;
	 *
	 * @test_Strategy: Send and receive messages of the following types:
	 * BytesMessage, MapMessage, ObjectMessage, TextMessage. Test the following
	 * API's: JMSConsumer.receiveBody(Class<T>), JMSConsumer.receiveBody(Class<T>,
	 * long), and JMSConsumer.recieveBodyNoWait(Class<T>)
	 *
	 * <T> T = JMSConsumer.receiveBody(Class<T>) <T> T =
	 * JMSConsumer.receiveBody(Class<T>, long) <T> T =
	 * JMSConsumer.receiveBodyNoWait(Class<T>)
	 *
	 * Test the following:
	 *
	 * String message = JMSConsumer.receiveBody(String.class) StringBuffer message =
	 * JMSConsumer.receiveBody(StringBuffer.class, long); byte[] message =
	 * JMSConsumer.receiveBody(byte[].class, long); Map message =
	 * JMSConsumer.receiveBodyNoWait(Map.class);
	 *
	 */
	@Test
	public void queueReceiveBodyTests() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		StringBuffer expSbuffer = new StringBuffer("This is it!");
		try {
			// set up JmsTool for COMMON_Q setup
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON_Q");
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			cf = tool.getConnectionFactory();
			destination = tool.getDefaultDestination();
			queue = (Queue) destination;
			logger.log(Logger.Level.INFO, "Create JMSContext with AUTO_ACKNOWLEDGE");
			context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			queueTest = true;

			// Create JMSProducer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSProducer");
			producer = context.createProducer();

			// Create JMSConsumer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSConsumer");
			consumer = context.createConsumer(destination);

			// Send and receive TextMessage
			logger.log(Logger.Level.INFO, "Create TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueReceiveBodyTests");
			logger.log(Logger.Level.INFO, "Send and receive the TextMessage");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receiveBody(String.class) to receive message as a String");
			String actMessage = consumer.receiveBody(String.class);
			if (actMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
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
			expObjectMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueReceiveBodyTests");
			logger.log(Logger.Level.INFO, "Send and receive the ObjectMessage");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expObjectMessage);
			logger.log(Logger.Level.INFO,
					"Call JMSConsumer.receiveBody(StringBuffer.class, long) to receive message as a StringBuffer");
			StringBuffer actSbuffer = consumer.receiveBody(StringBuffer.class, timeout);
			if (actSbuffer == null) {
				throw new Exception("Did not receive ObjectMessage");
			}
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
			bMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "queueReceiveBodyTests");
			bMsg.writeByte((byte) 1);
			bMsg.writeInt((int) 22);
			logger.log(Logger.Level.INFO, "Send and receive the BytesMessage");
			producer.send(destination, bMsg);
			logger.log(Logger.Level.INFO,
					"Call JMSConsumer.receiveBody(byte[].class, long) to receive message as a byte array");
			byte[] bytes = consumer.receiveBody(byte[].class, timeout);
			if (bytes == null) {
				throw new Exception("Did not receive BytesMessage");
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
			logger.log(Logger.Level.INFO, "Create MapMessage");
			MapMessage mMsg = context.createMapMessage();
			logger.log(Logger.Level.INFO, "Set some values in MapMessage");
			mMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "queueReceiveBodyTests");
			mMsg.setBoolean("booleanvalue", true);
			mMsg.setInt("intvalue", (int) 10);
			logger.log(Logger.Level.INFO, "Send and receive the MapMessage");
			producer.send(destination, mMsg);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receiveBodyNoWait(Map.class) to receive message as a Map");
			Map map = consumer.receiveBodyNoWait(Map.class);
			if (map == null) {
				for (int i = 0; i < 5; i++) {
					TestUtil.sleepSec(1);
					map = consumer.receiveBodyNoWait(Map.class);
					if (map != null)
						break;
				}
			}
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
			throw new Exception("queueReceiveBodyTests", e);
		} finally {
			try {
				if (consumer != null)
					consumer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("queueReceiveBodyTests failed");
		}
	}

	/*
	 * @testName: queueReceiveBodyExceptionTests
	 *
	 * @assertion_ids: JMS:JAVADOC:1371; JMS:JAVADOC:1374; JMS:JAVADOC:1377;
	 *
	 * @test_Strategy: Test exception cases for JMSConsumer.receiveBody(Class<T>),
	 * JMSConsumer.receiveBody(Class<T>, long), and
	 * JMSConsumer.recieveBodyNoWait(Class<T>).
	 *
	 * Test for exception MessageFormatRuntimeException.
	 *
	 */
	@Test
	public void queueReceiveBodyExceptionTests() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		StringBuffer expSbuffer = new StringBuffer("This is it!");
		try {
			// set up JmsTool for COMMON_Q setup
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON_Q");
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			cf = tool.getConnectionFactory();
			destination = tool.getDefaultDestination();
			queue = (Queue) destination;
			logger.log(Logger.Level.INFO, "Create JMSContext with AUTO_ACKNOWLEDGE");
			context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			queueTest = true;

			// Create JMSProducer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSProducer");
			producer = context.createProducer();

			// Create JMSConsumer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSConsumer");
			consumer = context.createConsumer(destination);

			// Send and receive TextMessage
			logger.log(Logger.Level.INFO, "Create TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueReceiveBodyExceptionTests");
			logger.log(Logger.Level.INFO, "Send and receive the TextMessage");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receiveBody(Class<T>) to receive message as wrong type");
			logger.log(Logger.Level.INFO,
					"Pass Boolean.class as parameter to receiveBody() expect MessageFormatRuntimeException");
			try {
				consumer.receiveBody(Boolean.class);
				logger.log(Logger.Level.ERROR, "Did not throw expected MessageFormatRuntimeException");
				pass = false;
			} catch (MessageFormatRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected MessageFormatRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			// Send and receive ObjectMessage
			logger.log(Logger.Level.INFO, "Create ObjectMessage of type StringBuffer");
			ObjectMessage expObjectMessage = context.createObjectMessage(expSbuffer);
			logger.log(Logger.Level.INFO, "Set some values in ObjectMessage");
			expObjectMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueReceiveBodyExceptionTests");
			logger.log(Logger.Level.INFO, "Send and receive the ObjectMessage");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expObjectMessage);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receiveBody(Class<T>) to receive message as wrong type");
			logger.log(Logger.Level.INFO,
					"Pass HashMap.class as parameter to receiveBody() expect MessageFormatRuntimeException");
			try {
				consumer.receiveBody(Boolean.class);
				logger.log(Logger.Level.ERROR, "Did not throw expected MessageFormatRuntimeException");
				pass = false;
			} catch (MessageFormatRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected MessageFormatRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			// Send and receive BytesMessage
			logger.log(Logger.Level.INFO, "Create BytesMessage");
			BytesMessage bMsg = context.createBytesMessage();
			logger.log(Logger.Level.INFO, "Set some values in BytesMessage");
			bMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "queueReceiveBodyExceptionTests");
			bMsg.writeByte((byte) 1);
			bMsg.writeInt((int) 22);
			logger.log(Logger.Level.INFO, "Send and receive the BytesMessage");
			producer.send(destination, bMsg);
			logger.log(Logger.Level.INFO,
					"Call JMSConsumer.receiveBodyNoWait(Class<T>) to receive message as wrong type");
			logger.log(Logger.Level.INFO,
					"Pass Boolean.class as parameter to receiveBodyNoWait() expect MessageFormatRuntimeException");
			try {
				for (int i = 0; i < 5; i++) {
					TestUtil.sleepSec(1);
					if (consumer.receiveBodyNoWait(Boolean.class) != null)
						break;
				}
				logger.log(Logger.Level.ERROR, "Did not throw expected MessageFormatRuntimeException");
				pass = false;
			} catch (MessageFormatRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected MessageFormatRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			// Send and receive MapMessage
			logger.log(Logger.Level.INFO, "Send MapMessage");
			MapMessage mMsg = context.createMapMessage();
			logger.log(Logger.Level.INFO, "Set some values in MapMessage");
			mMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "queueReceiveBodyExceptionTests");
			mMsg.setBoolean("booleanvalue", true);
			mMsg.setInt("intvalue", (int) 10);
			producer.send(destination, mMsg);
			logger.log(Logger.Level.INFO,
					"Call JMSConsumer.receiveBody(Map.class, long) to receive message as wrong type");
			logger.log(Logger.Level.INFO,
					"Pass Boolean.class as parameter to receiveBody() expect MessageFormatRuntimeException");
			try {
				consumer.receiveBody(Boolean.class, timeout);
				logger.log(Logger.Level.ERROR, "Did not throw expected MessageFormatRuntimeException");
				pass = false;
			} catch (MessageFormatRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected MessageFormatRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			// send and receive StreamMessage
			logger.log(Logger.Level.INFO, "Send StreamMessage");
			StreamMessage sMsg = context.createStreamMessage();
			logger.log(Logger.Level.INFO, "Set some values in StreamMessage");
			sMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "queueReceiveBodyExceptionTests");
			sMsg.writeBoolean(true);
			sMsg.writeInt((int) 22);
			producer.send(destination, sMsg);
			logger.log(Logger.Level.INFO,
					"Call JMSConsumer.receiveBody() on a StreamMessage expect MessageFormatRuntimeException");
			try {
				consumer.receiveBody(Boolean.class, timeout);
				logger.log(Logger.Level.ERROR, "Did not throw expected MessageFormatRuntimeException");
				pass = false;
			} catch (MessageFormatRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected MessageFormatRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			// send and receive Message
			logger.log(Logger.Level.INFO, "Send Message");
			Message msg = context.createMessage();
			logger.log(Logger.Level.INFO, "Set some values in Message");
			msg.setStringProperty("COM_SUN_JMS_TESTNAME", "queueReceiveBodyExceptionTests");
			msg.setBooleanProperty("booleanProperty", true);
			producer.send(destination, msg);
			long deliveryTime = msg.getJMSDeliveryTime();
			logger.log(Logger.Level.INFO,
					"Call JMSConsumer.receiveBody() on a Message expect MessageFormatRuntimeException");
			try {
				consumer.receiveBody(Boolean.class, timeout);
				logger.log(Logger.Level.ERROR, "Did not throw expected MessageFormatRuntimeException");
				pass = false;
			} catch (MessageFormatRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected MessageFormatRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("queueReceiveBodyExceptionTests", e);
		} finally {
			try {
				if (consumer != null)
					consumer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("queueReceiveBodyExceptionTests failed");
		}
	}

	/*
	 * @testName: queueGetMessageSelectorTest
	 *
	 * @assertion_ids: JMS:JAVADOC:1100;
	 *
	 * @test_Strategy: Test the JMSConsumer getMessageSelector API:
	 *
	 * JMSConsumer.getMessageSelector()
	 *
	 */
	@Test
	public void queueGetMessageSelectorTest() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up JmsTool for COMMON_Q setup
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON_Q");
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			cf = tool.getConnectionFactory();
			destination = tool.getDefaultDestination();
			queue = (Queue) destination;
			logger.log(Logger.Level.INFO, "Create JMSContext with AUTO_ACKNOWLEDGE");
			context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			queueTest = true;

			// Create JMSProducer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSProducer");
			producer = context.createProducer();

			// Create JMSConsumer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSConsumer");
			consumer = context.createConsumer(destination);

			// Get Message selector expression
			logger.log(Logger.Level.INFO, "Get message selector expression by calling getMessageSelector() API");
			String msgsel = consumer.getMessageSelector();

			logger.log(Logger.Level.INFO, "Expecting message selector to be NULL since we didn't set it");
			if (msgsel != null) {
				logger.log(Logger.Level.ERROR,
						"Message selector is NOT NULL (unexpected): <msgsel=" + msgsel + ">, expected NULL");
				pass = false;
			}
			consumer.close();

			// Create JMSConsumer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSConsumer with message selector 'lastMessage = TRUE'");
			consumer = context.createConsumer(destination, "lastMessage = TRUE");

			// Get Message selector expression
			logger.log(Logger.Level.INFO, "Get message selector expression by calling getMessageSelector() API");
			msgsel = consumer.getMessageSelector();

			logger.log(Logger.Level.INFO,
					"Expecting message selector to be NOT NULL since we set it to 'lastMessage = TRUE'");
			if (msgsel == null) {
				logger.log(Logger.Level.ERROR,
						"Message selector is NULL (unexpected): <msgsel=null>, expected 'lastMessage = TRUE'");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("queueGetMessageSelectorTest", e);
		} finally {
			try {
				if (consumer != null)
					consumer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("queueGetMessageSelectorTest failed");
		}
	}

	/*
	 * @testName: queueCloseTest
	 *
	 * @assertion_ids: JMS:JAVADOC:1098;
	 *
	 * @test_Strategy: Test the JMSConsumer close API.
	 *
	 * JMSConsumer.close()
	 *
	 */
	@Test
	public void queueCloseTest() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up JmsTool for COMMON_Q setup
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON_Q");
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			cf = tool.getConnectionFactory();
			destination = tool.getDefaultDestination();
			queue = (Queue) destination;
			logger.log(Logger.Level.INFO, "Create JMSContext with AUTO_ACKNOWLEDGE");
			context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			queueTest = true;

			// Create JMSProducer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSProducer");
			producer = context.createProducer();

			// Create JMSConsumer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSConsumer");
			consumer = context.createConsumer(destination);

			// send TextMessage
			logger.log(Logger.Level.INFO, "Create TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueCloseTest");
			logger.log(Logger.Level.INFO, "Send the TextMessage");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expTextMessage);

			logger.log(Logger.Level.INFO, "Close the JMSConsumer");
			consumer.close();

			logger.log(Logger.Level.INFO,
					"Try receiving a message on a closed JMSConsumer (expect JMSRuntimeException or no message)");
			try {
				TextMessage actTextMessage = (TextMessage) consumer.receive();
				if (actTextMessage != null) {
					logger.log(Logger.Level.ERROR,
							"Received a message (expected no message), <recvdMsg=" + actTextMessage + ">");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Did not receive a message (correct)");
				}
			} catch (JMSRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSRuntimeException: " + e);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("queueCloseTest", e);
		}

		if (!pass) {
			throw new Exception("queueCloseTest failed");
		}
	}

	/*
	 * @testName: topicReceiveTests
	 *
	 * @assertion_ids: JMS:JAVADOC:1102; JMS:JAVADOC:1104; JMS:JAVADOC:1106;
	 *
	 * @test_Strategy: Test the JMSConsumer receive API's. Tests the following
	 * API's:
	 *
	 * JMSConsumer.receive() JMSConsumer.receive(long timeout)
	 * JMSConsumer.receiveNoWait()
	 *
	 */
	@Test
	public void topicReceiveTests() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up JmsTool for COMMON_T setup
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON_T");
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			cf = tool.getConnectionFactory();
			destination = tool.getDefaultDestination();
			topic = (Topic) destination;
			logger.log(Logger.Level.INFO, "Create JMSContext with AUTO_ACKNOWLEDGE");
			context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			queueTest = false;

			// Create JMSProducer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSProducer");
			producer = context.createProducer();

			// Create JMSConsumer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSConsumer");
			consumer = context.createConsumer(destination);

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Create TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicReceiveTests");
			logger.log(Logger.Level.INFO, "Send and receive the TextMessage");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receive() to receive TextMessage");
			TextMessage actTextMessage = (TextMessage) consumer.receive();
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

			// send and receive TextMessage again
			logger.log(Logger.Level.INFO, "Send and receive the TextMessage again");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receive(long timeout) to receive TextMessage");
			actTextMessage = (TextMessage) consumer.receive(timeout);
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

			// send and receive TextMessage again
			logger.log(Logger.Level.INFO, "Send and receive the TextMessage again");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receiveNoWait() to receive TextMessage");
			actTextMessage = (TextMessage) consumer.receiveNoWait();
			if (actTextMessage == null) {
				logger.log(Logger.Level.INFO, "Did not receive message (THIS IS OK)");
				logger.log(Logger.Level.INFO, "Now block and wait for message via JMSConsumer.receive()");
				actTextMessage = (TextMessage) consumer.receive();
			}
			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage on blocking receive");
			}
			logger.log(Logger.Level.INFO, "Check the value in TextMessage");
			if (actTextMessage.getText().equals(expTextMessage.getText())) {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
						+ ", received " + actTextMessage.getText());
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Now don't send a message at all");
			logger.log(Logger.Level.INFO, "Calling receiveNoWait() again to receive TextMessage");
			actTextMessage = (TextMessage) consumer.receiveNoWait();
			if (actTextMessage != null) {
				logger.log(Logger.Level.ERROR, "actTextMessage != NULL (expected actTextMessage=NULL)");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("topicReceiveTests", e);
		} finally {
			try {
				if (consumer != null)
					consumer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("topicReceiveTests failed");
		}
	}

	/*
	 * @testName: topicReceiveBodyTests
	 *
	 * @assertion_ids: JMS:JAVADOC:1369; JMS:JAVADOC:1372; JMS:JAVADOC:1375;
	 *
	 * @test_Strategy: Send and receive messages of the following types:
	 * BytesMessage, MapMessage, ObjectMessage, TextMessage. Test the following
	 * API's: JMSConsumer.receiveBody(Class<T>), JMSConsumer.receiveBody(Class<T>,
	 * long), and JMSConsumer.recieveBodyNoWait(Class<T>)
	 *
	 * <T> T = JMSConsumer.receiveBody(Class<T>) <T> T =
	 * JMSConsumer.receiveBody(Class<T>, long) <T> T =
	 * JMSConsumer.receiveBodyNoWait(Class<T>)
	 *
	 * Test the following:
	 *
	 * String message = JMSConsumer.receiveBody(String.class) StringBuffer message =
	 * JMSConsumer.receiveBody(StringBuffer.class, long); byte[] message =
	 * JMSConsumer.receiveBody(byte[].class, long); Map message =
	 * JMSConsumer.receiveBodyNoWait(Map.class);
	 *
	 */
	@Test
	public void topicReceiveBodyTests() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		StringBuffer expSbuffer = new StringBuffer("This is it!");
		try {
			// set up JmsTool for COMMON_T setup
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON_T");
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			cf = tool.getConnectionFactory();
			destination = tool.getDefaultDestination();
			topic = (Topic) destination;
			logger.log(Logger.Level.INFO, "Create JMSContext with AUTO_ACKNOWLEDGE");
			context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			queueTest = false;

			// Create JMSProducer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSProducer");
			producer = context.createProducer();

			// Create JMSConsumer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSConsumer");
			consumer = context.createConsumer(destination);

			// Send and receive TextMessage
			logger.log(Logger.Level.INFO, "Create TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicReceiveBodyTests");
			logger.log(Logger.Level.INFO, "Send and receive the TextMessage");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receiveBody(String.class) to receive message as a String");
			String actMessage = consumer.receiveBody(String.class);
			if (actMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
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
			expObjectMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicReceiveBodyTests");
			logger.log(Logger.Level.INFO, "Send and receive the ObjectMessage");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expObjectMessage);
			logger.log(Logger.Level.INFO,
					"Call JMSConsumer.receiveBody(StringBuffer.class, long) to receive message as a StringBuffer");
			StringBuffer actSbuffer = consumer.receiveBody(StringBuffer.class, timeout);
			if (actSbuffer == null) {
				throw new Exception("Did not receive ObjectMessage");
			}
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
			bMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "topicReceiveBodyTests");
			bMsg.writeByte((byte) 1);
			bMsg.writeInt((int) 22);
			logger.log(Logger.Level.INFO, "Send and receive the BytesMessage");
			producer.send(destination, bMsg);
			logger.log(Logger.Level.INFO,
					"Call JMSConsumer.receiveBody(byte[].class, long) to receive message as a byte array");
			byte[] bytes = consumer.receiveBody(byte[].class, timeout);
			if (bytes == null) {
				throw new Exception("Did not receive BytesMessage");
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
			logger.log(Logger.Level.INFO, "Create MapMessage");
			MapMessage mMsg = context.createMapMessage();
			logger.log(Logger.Level.INFO, "Set some values in MapMessage");
			mMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "topicReceiveBodyTests");
			mMsg.setBoolean("booleanvalue", true);
			mMsg.setInt("intvalue", (int) 10);
			logger.log(Logger.Level.INFO, "Send and receive the MapMessage");
			producer.send(destination, mMsg);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receiveBodyNoWait(Map.class) to receive message as a Map");
			Map map = consumer.receiveBodyNoWait(Map.class);
			if (map == null) {
				for (int i = 0; i < 5; i++) {
					TestUtil.sleepSec(1);
					map = consumer.receiveBodyNoWait(Map.class);
					if (map != null)
						break;
				}
			}
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
			throw new Exception("topicReceiveBodyTests", e);
		} finally {
			try {
				if (consumer != null)
					consumer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("topicReceiveBodyTests failed");
		}
	}

	/*
	 * @testName: topicReceiveBodyExceptionTests
	 *
	 * @assertion_ids: JMS:JAVADOC:1371; JMS:JAVADOC:1374; JMS:JAVADOC:1377;
	 *
	 * @test_Strategy: Test exception cases for JMSConsumer.receiveBody(Class<T>),
	 * JMSConsumer.receiveBody(Class<T>, long), and
	 * JMSConsumer.recieveBodyNoWait(Class<T>).
	 *
	 * Test for exception MessageFormatRuntimeException.
	 *
	 */
	@Test
	public void topicReceiveBodyExceptionTests() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		StringBuffer expSbuffer = new StringBuffer("This is it!");
		try {
			// set up JmsTool for COMMON_T setup
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON_T");
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			cf = tool.getConnectionFactory();
			destination = tool.getDefaultDestination();
			topic = (Topic) destination;
			logger.log(Logger.Level.INFO, "Create JMSContext with AUTO_ACKNOWLEDGE");
			context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			queueTest = false;

			// Create JMSProducer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSProducer");
			producer = context.createProducer();

			// Create JMSConsumer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSConsumer");
			consumer = context.createConsumer(destination);

			// Send and receive TextMessage
			logger.log(Logger.Level.INFO, "Create TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicReceiveBodyExceptionTests");
			logger.log(Logger.Level.INFO, "Send and receive the TextMessage");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expTextMessage);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receiveBody(Class<T>) to receive message as wrong type");
			logger.log(Logger.Level.INFO,
					"Pass Boolean.class as parameter to receiveBody() expect MessageFormatRuntimeException");
			try {
				consumer.receiveBody(Boolean.class);
				logger.log(Logger.Level.ERROR, "Did not throw expected MessageFormatRuntimeException");
				pass = false;
			} catch (MessageFormatRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected MessageFormatRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			// Send and receive ObjectMessage
			logger.log(Logger.Level.INFO, "Create ObjectMessage of type StringBuffer");
			ObjectMessage expObjectMessage = context.createObjectMessage(expSbuffer);
			logger.log(Logger.Level.INFO, "Set some values in ObjectMessage");
			expObjectMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicReceiveBodyExceptionTests");
			logger.log(Logger.Level.INFO, "Send and receive the ObjectMessage");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expObjectMessage);
			logger.log(Logger.Level.INFO, "Call JMSConsumer.receiveBody(Class<T>) to receive message as wrong type");
			logger.log(Logger.Level.INFO,
					"Pass HashMap.class as parameter to receiveBody() expect MessageFormatRuntimeException");
			try {
				consumer.receiveBody(Boolean.class);
				logger.log(Logger.Level.ERROR, "Did not throw expected MessageFormatRuntimeException");
				pass = false;
			} catch (MessageFormatRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected MessageFormatRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			// Send and receive BytesMessage
			logger.log(Logger.Level.INFO, "Create BytesMessage");
			BytesMessage bMsg = context.createBytesMessage();
			logger.log(Logger.Level.INFO, "Set some values in BytesMessage");
			bMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "topicReceiveBodyExceptionTests");
			bMsg.writeByte((byte) 1);
			bMsg.writeInt((int) 22);
			logger.log(Logger.Level.INFO, "Send and receive the BytesMessage");
			producer.send(destination, bMsg);
			logger.log(Logger.Level.INFO,
					"Call JMSConsumer.receiveBodyNoWait(Class<T>) to receive message as wrong type");
			logger.log(Logger.Level.INFO,
					"Pass Boolean.class as parameter to receiveBodyNoWait() expect MessageFormatRuntimeException");
			try {
				for (int i = 0; i < 5; i++) {
					TestUtil.sleepSec(1);
					if (consumer.receiveBodyNoWait(Boolean.class) != null)
						break;
				}
				logger.log(Logger.Level.ERROR, "Did not throw expected MessageFormatRuntimeException");
				pass = false;
			} catch (MessageFormatRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected MessageFormatRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			// Send and receive MapMessage
			logger.log(Logger.Level.INFO, "Send MapMessage");
			MapMessage mMsg = context.createMapMessage();
			logger.log(Logger.Level.INFO, "Set some values in MapMessage");
			mMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "topicReceiveBodyExceptionTests");
			mMsg.setBoolean("booleanvalue", true);
			mMsg.setInt("intvalue", (int) 10);
			producer.send(destination, mMsg);
			logger.log(Logger.Level.INFO,
					"Call JMSConsumer.receiveBody(Map.class, long) to receive message as wrong type");
			logger.log(Logger.Level.INFO,
					"Pass Boolean.class as parameter to receiveBody() expect MessageFormatRuntimeException");
			try {
				consumer.receiveBody(Boolean.class, timeout);
				logger.log(Logger.Level.ERROR, "Did not throw expected MessageFormatRuntimeException");
				pass = false;
			} catch (MessageFormatRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected MessageFormatRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			// send and receive StreamMessage
			logger.log(Logger.Level.INFO, "Send StreamMessage");
			StreamMessage sMsg = context.createStreamMessage();
			logger.log(Logger.Level.INFO, "Set some values in StreamMessage");
			sMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "topicReceiveBodyExceptionTests");
			sMsg.writeBoolean(true);
			sMsg.writeInt((int) 22);
			producer.send(destination, sMsg);
			logger.log(Logger.Level.INFO,
					"Call JMSConsumer.receiveBody() on a StreamMessage expect MessageFormatRuntimeException");
			try {
				consumer.receiveBody(Boolean.class, timeout);
				logger.log(Logger.Level.ERROR, "Did not throw expected MessageFormatRuntimeException");
				pass = false;
			} catch (MessageFormatRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected MessageFormatRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			// send and receive Message
			logger.log(Logger.Level.INFO, "Send Message");
			Message msg = context.createMessage();
			logger.log(Logger.Level.INFO, "Set some values in Message");
			msg.setStringProperty("COM_SUN_JMS_TESTNAME", "topicReceiveBodyExceptionTests");
			msg.setBooleanProperty("booleanProperty", true);
			producer.send(destination, msg);
			long deliveryTime = msg.getJMSDeliveryTime();
			logger.log(Logger.Level.INFO,
					"Call JMSConsumer.receiveBody() on a Message expect MessageFormatRuntimeException");
			try {
				consumer.receiveBody(Boolean.class, timeout);
				logger.log(Logger.Level.ERROR, "Did not throw expected MessageFormatRuntimeException");
				pass = false;
			} catch (MessageFormatRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected MessageFormatRuntimeException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("topicReceiveBodyExceptionTests", e);
		} finally {
			try {
				if (consumer != null)
					consumer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("topicReceiveBodyExceptionTests failed");
		}
	}

	/*
	 * @testName: topicGetMessageSelectorTest
	 *
	 * @assertion_ids: JMS:JAVADOC:1100;
	 *
	 * @test_Strategy: Test the JMSConsumer getMessageSelector API:
	 *
	 * JMSConsumer.getMessageSelector()
	 *
	 */
	@Test
	public void topicGetMessageSelectorTest() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up JmsTool for COMMON_T setup
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON_T");
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			cf = tool.getConnectionFactory();
			destination = tool.getDefaultDestination();
			topic = (Topic) destination;
			logger.log(Logger.Level.INFO, "Create JMSContext with AUTO_ACKNOWLEDGE");
			context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			queueTest = false;

			// Create JMSProducer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSProducer");
			producer = context.createProducer();

			// Create JMSConsumer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSConsumer");
			consumer = context.createConsumer(destination);

			// Get Message selector expression
			logger.log(Logger.Level.INFO, "Get message selector expression by calling getMessageSelector() API");
			String msgsel = consumer.getMessageSelector();

			logger.log(Logger.Level.INFO, "Expecting message selector to be NULL since we didn't set it");
			if (msgsel != null) {
				logger.log(Logger.Level.ERROR,
						"Message selector is NOT NULL (unexpected): <msgsel=" + msgsel + ">, expected NULL");
				pass = false;
			}
			consumer.close();

			// Create JMSConsumer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSConsumer with message selector 'lastMessage = TRUE'");
			consumer = context.createConsumer(destination, "lastMessage = TRUE");

			// Get Message selector expression
			logger.log(Logger.Level.INFO, "Get message selector expression by calling getMessageSelector() API");
			msgsel = consumer.getMessageSelector();

			logger.log(Logger.Level.INFO,
					"Expecting message selector to be NOT NULL since we set it to 'lastMessage = TRUE'");
			if (msgsel == null) {
				logger.log(Logger.Level.ERROR,
						"Message selector is NULL (unexpected): <msgsel=null>, expected 'lastMessage = TRUE'");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("topicGetMessageSelectorTest", e);
		} finally {
			try {
				if (consumer != null)
					consumer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("topicGetMessageSelectorTest failed");
		}
	}

	/*
	 * @testName: topicCloseTest
	 *
	 * @assertion_ids: JMS:JAVADOC:1098;
	 *
	 * @test_Strategy: Test the JMSConsumer close API.
	 *
	 * JMSConsumer.close()
	 *
	 */
	@Test
	public void topicCloseTest() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up JmsTool for COMMON_T setup
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON_T");
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			cf = tool.getConnectionFactory();
			destination = tool.getDefaultDestination();
			topic = (Topic) destination;
			logger.log(Logger.Level.INFO, "Create JMSContext with AUTO_ACKNOWLEDGE");
			context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
			queueTest = false;

			// Create JMSProducer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSProducer");
			producer = context.createProducer();

			// Create JMSConsumer from JMSContext
			logger.log(Logger.Level.INFO, "Create JMSConsumer");
			consumer = context.createConsumer(destination);

			// send TextMessage
			logger.log(Logger.Level.INFO, "Create TextMessage");
			TextMessage expTextMessage = context.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicCloseTest");
			logger.log(Logger.Level.INFO, "Send the TextMessage");
			logger.log(Logger.Level.INFO, "Call JMSProducer.send(Destination, Message)");
			producer.send(destination, expTextMessage);

			logger.log(Logger.Level.INFO, "Close the JMSConsumer");
			consumer.close();

			logger.log(Logger.Level.INFO,
					"Try receiving a message on a closed JMSConsumer (expect JMSRuntimeException or no message)");
			try {
				TextMessage actTextMessage = (TextMessage) consumer.receive();
				if (actTextMessage != null) {
					logger.log(Logger.Level.ERROR,
							"Received a message (expected no message), <recvdMsg=" + actTextMessage + ">");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Did not receive a message (correct)");
				}
			} catch (JMSRuntimeException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSRuntimeException: " + e);
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("topicCloseTest", e);
		}

		if (!pass) {
			throw new Exception("topicCloseTest failed");
		}
	}
}
