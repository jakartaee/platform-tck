/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2022 Contributors to Eclipse Foundation. All rights reserved.
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
 * $Id: Client.java 68655 2012-11-21 16:26:50Z af70133 $
 */
package com.sun.ts.tests.jms.core20.sessiontests;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.BytesMessage;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import jakarta.jms.InvalidDestinationException;
import jakarta.jms.InvalidSelectorException;
import jakarta.jms.JMSException;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageProducer;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;
import jakarta.jms.Session;
import jakarta.jms.StreamMessage;
import jakarta.jms.TemporaryQueue;
import jakarta.jms.TemporaryTopic;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;
import jakarta.jms.TopicSubscriber;


public class ClientIT {
	private static final String testName = "com.sun.ts.tests.jms.core20.sessiontests.ClientIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(ClientIT.class.getName());

	// JMS tool which creates and/or looks up the JMS administered objects
	private transient JmsTool toolT = null;

	private transient JmsTool toolT2 = null;

	private transient JmsTool toolQ = null;

	// JMS objects
	private transient ConnectionFactory cf = null;

	private transient Topic topic = null;

	private transient Queue queue = null;

	private transient Destination destination = null;

	private transient Connection connection = null;

	private transient Session session = null;

	private transient MessageProducer producer = null;

	private transient MessageConsumer consumer = null;

	private transient MessageConsumer consumer2 = null;

	private transient TopicSubscriber subscriber = null;

	private transient TopicSubscriber subscriber2 = null;

	// Harness req's
	private Properties props = null;

	// properties read
	long timeout;

	String user;

	String password;

	String mode;

	String vehicle = null;

	// used for tests
	private static final String lookupDurableTopicFactory = "DURABLE_SUB_CONNECTION_FACTORY";

	private static final String lookupNormalTopicFactory = "MyTopicConnectionFactory";

	private static final int numMessages = 3;

	private static final int iterations = 5;

	ArrayList connections = null;

	boolean queueTest = false;

	/*
	 * setupGlobalVarsQ() for Queue
	 */
	public void setupGlobalVarsQ() throws Exception {
		cf = toolQ.getConnectionFactory();
		destination = toolQ.getDefaultDestination();
		session = toolQ.getDefaultSession();
		connection = toolQ.getDefaultConnection();
		producer = toolQ.getDefaultProducer();
		consumer = toolQ.getDefaultConsumer();
		queue = (Queue) destination;
		connection.start();
		queueTest = true;
	}

	/*
	 * setupGlobalVarsT() for Topic with normal connection factory (clientid not
	 * set)
	 */
	public void setupGlobalVarsT() throws Exception {
		cf = toolT.getConnectionFactory();
		destination = toolT.getDefaultDestination();
		session = toolT.getDefaultSession();
		connection = toolT.getDefaultConnection();
		producer = toolT.getDefaultProducer();
		consumer = toolT.getDefaultConsumer();
		topic = (Topic) destination;
		connection.start();
		queueTest = false;
	}

	/*
	 * setupGlobalVarsT2() for Topic with durable connection factory (clientid set)
	 */
	public void setupGlobalVarsT2() throws Exception {
		cf = toolT2.getConnectionFactory();
		destination = toolT2.getDefaultDestination();
		session = toolT2.getDefaultSession();
		connection = toolT2.getDefaultConnection();
		producer = toolT2.getDefaultProducer();
		consumer = toolT2.getDefaultConsumer();
		topic = (Topic) destination;
		connection.start();
		queueTest = false;
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

			// get props
			timeout = Long.parseLong(System.getProperty("jms_timeout"));
			user = System.getProperty("user");
			password = System.getProperty("password");
			mode = System.getProperty("platform.mode");
			vehicle = System.getProperty("vehicle");

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

			// set up JmsTool for COMMON_T setup (normal factory has clientid not set)
			toolT = new JmsTool(JmsTool.COMMON_T, user, password, lookupNormalTopicFactory, mode);

			// set up JmsTool for COMMON_Q setup
			toolQ = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
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
			logger.log(Logger.Level.INFO, "Closing default Connection");
			toolQ.getDefaultConnection().close();
			toolT.getDefaultConnection().close();
			if (toolT2 != null)
				toolT2.getDefaultConnection().close();
			if (queueTest) {
				logger.log(Logger.Level.INFO, "Flush any messages left on Queue");
				toolQ.flushDestination();
			}
			toolQ.closeAllResources();
			toolT.closeAllResources();
			if (toolT2 != null)
				toolT2.closeAllResources();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("cleanup failed!", e);
		}
	}

	/*
	 * Cleanup method for tests that use durable subscriptions
	 */
	private void cleanupSubscription(MessageConsumer consumer, Session session, String subName) {
		if (consumer != null) {
			try {
				logger.log(Logger.Level.TRACE, "Closing durable consumer: " + consumer);
				consumer.close();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "exception during close: ", e);
			}
		}

		if (session != null) {
			try {
				logger.log(Logger.Level.TRACE, "Unsubscribing \"" + subName + "\"");
				session.unsubscribe(subName);
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "exception during unsubscribe: ", e);
			}
		}
	}

	private void cleanupSubscription(TopicSubscriber tSub, Session session, String subName) {
		if (tSub != null) {
			try {
				logger.log(Logger.Level.TRACE, "Closing durable subscriber: " + tSub);
				tSub.close();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "exception during close: ", e);
			}
		}

		if (session != null) {
			try {
				logger.log(Logger.Level.TRACE, "Unsubscribing \"" + subName + "\"");
				session.unsubscribe(subName);
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "exception during unsubscribe: ", e);
			}
		}
	}

	/*
	 * @testName: sendRecvMsgsOfEachMsgTypeTopicTest
	 *
	 * @assertion_ids: JMS:JAVADOC:209; JMS:JAVADOC:212; JMS:JAVADOC:213;
	 * JMS:JAVADOC:215; JMS:JAVADOC:217; JMS:JAVADOC:219; JMS:JAVADOC:221;
	 * JMS:JAVADOC:223; JMS:JAVADOC:242; JMS:JAVADOC:317; JMS:JAVADOC:504;
	 * JMS:JAVADOC:510;
	 *
	 * @test_Strategy: Send and receive messages of each message type: Message,
	 * BytesMessage, MapMessage, ObjectMessage, StreamMessage, TextMessage. Tests
	 * the following API's
	 *
	 * ConnectionFactory.createConnection(String, String)
	 * Connection.createSession(boolean, int) Session.createMessage()
	 * Session.createBytesMessage() Session.createMapMessage()
	 * Session.createObjectMessage() Session.createObjectMessage(Serializable
	 * object) Session.createStreamMessage() Session.createTextMessage()
	 * Session.createTextMessage(String) Session.createConsumer(Destination)
	 * Session.createProducer(Destination) MessageProducer.send(Message)
	 * MessageConsumer.receive(long timeout)
	 */
	@Test
	public void sendRecvMsgsOfEachMsgTypeTopicTest() throws Exception {
		boolean pass = true;
		try {
			// Setup for Topic
			setupGlobalVarsT();

			// send and receive Message
			logger.log(Logger.Level.INFO, "Send Message");
			Message msg = session.createMessage();
			logger.log(Logger.Level.INFO, "Set some values in Message");
			msg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendRecvMsgsOfEachMsgTypeTopicTest");
			msg.setBooleanProperty("booleanProperty", true);
			producer.send(msg);
			logger.log(Logger.Level.INFO, "Receive Message");
			Message msgRecv = consumer.receive(timeout);
			if (msgRecv == null) {
				throw new Exception("Did not receive Message");
			}
			logger.log(Logger.Level.INFO, "Check the values in Message");
			if (msgRecv.getBooleanProperty("booleanProperty") == true) {
				logger.log(Logger.Level.INFO, "booleanproperty is correct");
			} else {
				logger.log(Logger.Level.INFO, "booleanproperty is incorrect");
				pass = false;
			}

			// send and receive BytesMessage
			logger.log(Logger.Level.INFO, "Send BytesMessage");
			BytesMessage bMsg = session.createBytesMessage();
			logger.log(Logger.Level.INFO, "Set some values in BytesMessage");
			bMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendRecvMsgsOfEachMsgTypeTopicTest");
			bMsg.writeByte((byte) 1);
			bMsg.writeInt((int) 22);
			producer.send(bMsg);
			logger.log(Logger.Level.INFO, "Receive BytesMessage");
			BytesMessage bMsgRecv = (BytesMessage) consumer.receive(timeout);
			if (bMsgRecv == null) {
				throw new Exception("Did not receive BytesMessage");
			}
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

			// send and receive MapMessage
			logger.log(Logger.Level.INFO, "Send MapMessage");
			MapMessage mMsg = session.createMapMessage();
			logger.log(Logger.Level.INFO, "Set some values in MapMessage");
			mMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendRecvMsgsOfEachMsgTypeTopicTest");
			mMsg.setBoolean("booleanvalue", true);
			mMsg.setInt("intvalue", (int) 10);
			producer.send(mMsg);
			logger.log(Logger.Level.INFO, "Receive MapMessage");
			MapMessage mMsgRecv = (MapMessage) consumer.receive(timeout);
			if (mMsgRecv == null) {
				throw new Exception("Did not receive MapMessage");
			}
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

			// send and receive ObjectMessage
			logger.log(Logger.Level.INFO, "Send ObjectMessage");
			StringBuffer sb1 = new StringBuffer("This is a StringBuffer");
			logger.log(Logger.Level.INFO, "Set some values in ObjectMessage");
			ObjectMessage oMsg = session.createObjectMessage();
			oMsg.setObject(sb1);
			oMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendRecvMsgsOfEachMsgTypeTopicTest");
			producer.send(oMsg);
			logger.log(Logger.Level.INFO, "Receive ObjectMessage");
			ObjectMessage oMsgRecv = (ObjectMessage) consumer.receive(timeout);
			if (oMsgRecv == null) {
				throw new Exception("Did not receive ObjectMessage");
			}
			logger.log(Logger.Level.INFO, "Check the value in ObjectMessage");
			StringBuffer sb2 = (StringBuffer) oMsgRecv.getObject();
			if (sb2.toString().equals(sb1.toString())) {
				logger.log(Logger.Level.INFO, "objectvalue is correct");
			} else {
				logger.log(Logger.Level.ERROR, "objectvalue is incorrect");
				pass = false;
			}

			// send and receive ObjectMessage passing object as param
			logger.log(Logger.Level.INFO, "Send ObjectMessage passing object as param");
			sb1 = new StringBuffer("This is a StringBuffer");
			logger.log(Logger.Level.INFO, "Set some values in ObjectMessage passing object as param");
			oMsg = session.createObjectMessage(sb1);
			oMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendRecvMsgsOfEachMsgTypeTopicTest");
			producer.send(oMsg);
			logger.log(Logger.Level.INFO, "Receive ObjectMessage");
			oMsgRecv = (ObjectMessage) consumer.receive(timeout);
			if (oMsgRecv == null) {
				throw new Exception("Did not receive ObjectMessage");
			}
			logger.log(Logger.Level.INFO, "Check the value in ObjectMessage");
			sb2 = (StringBuffer) oMsgRecv.getObject();
			if (sb2.toString().equals(sb1.toString())) {
				logger.log(Logger.Level.INFO, "objectvalue is correct");
			} else {
				logger.log(Logger.Level.ERROR, "objectvalue is incorrect");
				pass = false;
			}

			// send and receive StreamMessage
			logger.log(Logger.Level.INFO, "Send StreamMessage");
			StreamMessage sMsg = session.createStreamMessage();
			logger.log(Logger.Level.INFO, "Set some values in StreamMessage");
			sMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendRecvMsgsOfEachMsgTypeTopicTest");
			sMsg.writeBoolean(true);
			sMsg.writeInt((int) 22);
			producer.send(sMsg);
			logger.log(Logger.Level.INFO, "Receive StreamMessage");
			StreamMessage sMsgRecv = (StreamMessage) consumer.receive(timeout);
			if (sMsgRecv == null) {
				throw new Exception("Did not receive StreamMessage");
			}
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

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Send TextMessage");
			TextMessage tMsg = session.createTextMessage();
			logger.log(Logger.Level.INFO, "Set some values in MapMessage");
			tMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendRecvMsgsOfEachMsgTypeTopicTest");
			tMsg.setText("Hello There!");
			producer.send(tMsg);
			logger.log(Logger.Level.INFO, "Receive TextMessage");
			TextMessage tMsgRecv = (TextMessage) consumer.receive(timeout);
			if (tMsgRecv == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the value in TextMessage");
			if (tMsgRecv.getText().equals("Hello There!")) {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR, "TextMessage is incorrect");
				pass = false;
			}

			// send and receive TextMessage passing string as param
			logger.log(Logger.Level.INFO, "Send TextMessage");
			tMsg = session.createTextMessage("Where are you!");
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			tMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendRecvMsgsOfEachMsgTypeTopicTest");
			producer.send(tMsg);
			logger.log(Logger.Level.INFO, "Receive TextMessage");
			tMsgRecv = (TextMessage) consumer.receive(timeout);
			if (tMsgRecv == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the value in TextMessage");
			if (tMsgRecv.getText().equals("Where are you!")) {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR, "TextMessage is incorrect");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			e.printStackTrace();
			throw new Exception("sendRecvMsgsOfEachMsgTypeTopicTest", e);
		}

		if (!pass) {
			throw new Exception("sendRecvMsgsOfEachMsgTypeTopicTest failed");
		}
	}

	/*
	 * @testName: createTemporayTopicTest
	 *
	 * @assertion_ids: JMS:JAVADOC:93;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * Session.createTemporaryTopic().
	 *
	 * Send and receive a TextMessage to temporary topic. Compare send and recv
	 * message for equality.
	 */
	@Test
	public void createTemporayTopicTest() throws Exception {
		boolean pass = true;
		try {

			String message = "a text message";

			// Setup for Topic
			setupGlobalVarsT();

			// create a TemporaryTopic
			logger.log(Logger.Level.INFO, "Creating TemporaryTopic");
			TemporaryTopic tempTopic = session.createTemporaryTopic();

			// Create a MessageConsumer for this Temporary Topic
			logger.log(Logger.Level.INFO, "Creating MessageConsumer for TemporaryTopic");
			MessageConsumer tConsumer = session.createConsumer(tempTopic);

			// Create a MessageProducer for this Temporary Topic
			logger.log(Logger.Level.INFO, "Creating MessageProducer for TemporaryTopic");
			MessageProducer tProducer = session.createProducer(tempTopic);

			// Send TextMessage to temporary topic
			logger.log(Logger.Level.INFO, "Creating TextMessage with text [" + message + "]");
			TextMessage tMsg = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			tMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "createTemporayTopicTest");
			logger.log(Logger.Level.INFO, "Send TextMessage to temporaty topic");
			tProducer.send(tMsg);

			// Receive TextMessage from temporary topic
			logger.log(Logger.Level.INFO, "Receive TextMessage from temporaty topic");
			TextMessage tMsgRecv = (TextMessage) tConsumer.receive(timeout);
			if (tMsgRecv == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the value in TextMessage");
			if (tMsgRecv.getText().equals(message)) {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR, "TextMessage is incorrect");
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Attempting to delete temporary topic with an open consumer should not be allowed");
			try {
				tempTopic.delete();
				pass = false;
				logger.log(Logger.Level.ERROR, "TemporaryTopic.delete() didn't throw expected Exception");
			} catch (JMSException em) {
				logger.log(Logger.Level.INFO, "Received expected JMSException: ");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected Exception: " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO, "Now close the open consumer");
			try {
				tConsumer.close();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught exception: " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Attempting to delete temporary topic with no open consumer should be allowed");
			try {
				tempTopic.delete();
			} catch (Exception e) {
				pass = false;
				logger.log(Logger.Level.ERROR, "Received unexpected Exception: ", e);
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			e.printStackTrace();
			throw new Exception("createTemporayTopicTest");
		}

		if (!pass) {
			throw new Exception("createTemporayTopicTest failed");
		}
	}

	/*
	 * @testName: getTransactedTopicTest
	 *
	 * @assertion_ids: JMS:JAVADOC:225;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * Session.getTransacted().
	 */
	@Test
	public void getTransactedTopicTest() throws Exception {
		boolean pass = true;

		// Test for transacted mode false
		try {
			// Setup for Topic
			setupGlobalVarsT();
			session.close();

			session = connection.createSession(Session.AUTO_ACKNOWLEDGE);
			boolean expTransacted = false;
			logger.log(Logger.Level.INFO, "Calling getTransacted and expect " + expTransacted + " to be returned");
			boolean actTransacted = session.getTransacted();
			if (actTransacted != expTransacted) {
				logger.log(Logger.Level.ERROR,
						"getTransacted() returned " + actTransacted + ", expected " + expTransacted);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("getTransactedTopicTest", e);
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception e) {
			}
		}

		// Test for transacted mode true
		if ((vehicle.equals("appclient") || vehicle.equals("standalone"))) {
			try {
				session = connection.createSession(Session.SESSION_TRANSACTED);
				boolean expTransacted = true;
				logger.log(Logger.Level.INFO, "Calling getTransacted and expect " + expTransacted + " to be returned");
				boolean actTransacted = session.getTransacted();
				if (actTransacted != expTransacted) {
					logger.log(Logger.Level.ERROR,
							"getTransacted() returned " + actTransacted + ", expected " + expTransacted);
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught exception: " + e);
				throw new Exception("getTransactedTopicTest", e);
			} finally {
				try {
					if (session != null)
						session.close();
				} catch (Exception e) {
				}
			}
		}

		if (!pass) {
			throw new Exception("getTransactedTopicTest failed");
		}
	}

	/*
	 * @testName: getAcknowledgeModeTopicTest
	 *
	 * @assertion_ids: JMS:JAVADOC:227;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * Session.getAcknowledgeMode().
	 */
	@Test
	public void getAcknowledgeModeTopicTest() throws Exception {
		boolean pass = true;

		// Test for AUTO_ACKNOWLEDGE mode
		try {
			// Setup for Topic
			setupGlobalVarsT();
			session.close();

			session = connection.createSession(Session.AUTO_ACKNOWLEDGE);
			int expAcknowledgeMode = Session.AUTO_ACKNOWLEDGE;
			logger.log(Logger.Level.INFO,
					"Calling getAcknowledgeMode and expect " + expAcknowledgeMode + " to be returned");
			int actAcknowledgeMode = session.getAcknowledgeMode();
			if (actAcknowledgeMode != expAcknowledgeMode) {
				logger.log(Logger.Level.ERROR,
						"getAcknowledgeMode() returned " + actAcknowledgeMode + ", expected " + expAcknowledgeMode);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("getAcknowledgeModeTopicTest", e);
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception e) {
			}
		}

		// Test for DUPS_OK_ACKNOWLEDGE mode
		try {
			session = connection.createSession(Session.DUPS_OK_ACKNOWLEDGE);
			int expAcknowledgeMode = Session.DUPS_OK_ACKNOWLEDGE;
			logger.log(Logger.Level.INFO,
					"Calling getAcknowledgeMode and expect " + expAcknowledgeMode + " to be returned");
			int actAcknowledgeMode = session.getAcknowledgeMode();
			if (actAcknowledgeMode != expAcknowledgeMode) {
				logger.log(Logger.Level.ERROR,
						"getAcknowledgeMode() returned " + actAcknowledgeMode + ", expected " + expAcknowledgeMode);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("getAcknowledgeModeTopicTest", e);
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception e) {
			}
		}

		// Test for SESSION_TRANSACTED mode
		if ((vehicle.equals("appclient") || vehicle.equals("standalone"))) {
			try {
				session = connection.createSession(Session.SESSION_TRANSACTED);
				int expAcknowledgeMode = Session.SESSION_TRANSACTED;
				logger.log(Logger.Level.INFO,
						"Calling getAcknowledgeMode and expect " + expAcknowledgeMode + " to be returned");
				int actAcknowledgeMode = session.getAcknowledgeMode();
				if (actAcknowledgeMode != expAcknowledgeMode) {
					logger.log(Logger.Level.ERROR,
							"getAcknowledgeMode() returned " + actAcknowledgeMode + ", expected " + expAcknowledgeMode);
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught exception: " + e);
				throw new Exception("getAcknowledgeModeTopicTest", e);
			} finally {
				try {
					if (session != null)
						session.close();
				} catch (Exception e) {
				}
			}
		}

		if (!pass) {
			throw new Exception("getAcknowledgeModeTopicTest failed");
		}
	}

	/*
	 * @testName: createConsumerProducerTopicTest
	 *
	 * @assertion_ids: JMS:JAVADOC:244; JMS:JAVADOC:246; JMS:JAVADOC:248;
	 * JMS:JAVADOC:224; JMS:JAVADOC:242; JMS:JAVADOC:221; JMS:JAVADOC:504;
	 * JMS:JAVADOC:510; JMS:JAVADOC:597; JMS:JAVADOC:334;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * ConnectionFactory.createConnection(String, String)
	 * Connection.createSession(boolean, int) Session.createTextMessage(String)
	 * Session.createConsumer(Destination) Session.createConsumer(Destination,
	 * String) Session.createConsumer(Destination, String, boolean)
	 * Session.createProducer(Destination) MessageProducer.send(Message)
	 * MessageConsumer.receive(long timeout)
	 * 
	 * 1. Create a MessageConsumer with selector to consumer just the last message
	 * in the Topic with boolproperty (lastMessage=TRUE). 2. Create a
	 * MessageConsumer again to consume all the messages in the Topic. 3. Send x
	 * text messages to a Topic. 4. Verify that both consumers work as expected.
	 */
	@Test
	public void createConsumerProducerTopicTest() throws Exception {
		boolean pass = true;
		MessageConsumer consumerSelect = null;
		try {
			TextMessage tempMsg = null;
			Enumeration msgs = null;

			// Setup for Topic
			setupGlobalVarsT();
			consumer.close();

			// Create selective MessageConsumer to consume messages in Topic with
			// boolean
			// property (lastMessage=TRUE)
			logger.log(Logger.Level.INFO,
					"Create selective consumer to consume messages in Topic with boolproperty (lastMessage=TRUE)");
			consumerSelect = session.createConsumer(destination, "lastMessage=TRUE");

			// Create normal MessageConsumer to consume all messages in Topic
			logger.log(Logger.Level.INFO, "Create normal consumer to consume all messages in Topic");
			consumer = session.createConsumer(destination);

			// send "numMessages" messages to Topic plus end of stream message
			logger.log(Logger.Level.INFO, "Send " + numMessages + " messsages to Topic");
			for (int i = 1; i <= numMessages; i++) {
				tempMsg = session.createTextMessage("Message " + i);
				tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "createConsumerProducerTopicTest" + i);
				tempMsg.setJMSType("");
				if (i == numMessages) {
					tempMsg.setBooleanProperty("lastMessage", true);
				} else {
					tempMsg.setBooleanProperty("lastMessage", false);
				}
				producer.send(tempMsg);
				logger.log(Logger.Level.INFO, "Message " + i + " sent");
			}

			logger.log(Logger.Level.INFO,
					"Consume messages with selective consumer which has boolproperty (lastMessage=TRUE)");
			if (consumerSelect != null)
				tempMsg = (TextMessage) consumerSelect.receive(timeout);
			if (tempMsg == null) {
				logger.log(Logger.Level.ERROR, "MessageConsumer.receive() returned NULL");
				logger.log(Logger.Level.ERROR, "Message " + numMessages + " missing from Topic");
				pass = false;
			} else if (!tempMsg.getText().equals("Message " + numMessages)) {
				logger.log(Logger.Level.ERROR, "Received [" + tempMsg.getText() + "] expected [Message 3]");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Received expected message: " + tempMsg.getText());
			}

			// Try to receive one more message (should return null)
			logger.log(Logger.Level.INFO, "Make sure selective consumer receives no more messages");
			if (consumerSelect != null)
				tempMsg = (TextMessage) consumerSelect.receive(timeout);
			if (tempMsg != null) {
				logger.log(Logger.Level.ERROR,
						"MessageConsumer.receive() returned a message [" + tempMsg.getText() + "] (Expected NULL)");
				logger.log(Logger.Level.ERROR, "MessageConsumer with selector should have returned just 1 message");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "No more messages for selective consumer (Correct)");
			}

			try {
				if (consumerSelect != null)
					consumerSelect.close();
			} catch (Exception e) {
			}

			logger.log(Logger.Level.INFO, "Consume all messages with normal consumer");
			for (int msgCount = 1; msgCount <= numMessages; msgCount++) {
				tempMsg = (TextMessage) consumer.receive(timeout);
				if (tempMsg == null) {
					logger.log(Logger.Level.ERROR, "MessageConsumer.receive() returned NULL");
					logger.log(Logger.Level.ERROR, "Message " + msgCount + " missing from Topic");
					pass = false;
				} else if (!tempMsg.getText().equals("Message " + msgCount)) {
					logger.log(Logger.Level.ERROR,
							"Received [" + tempMsg.getText() + "] expected [Message " + msgCount + "]");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Received message: " + tempMsg.getText());
				}
			}

			// Try to receive one more message (should return null)
			logger.log(Logger.Level.INFO, "Make sure normal consumer receives no more messages");
			tempMsg = (TextMessage) consumer.receive(timeout);
			if (tempMsg != null) {
				logger.log(Logger.Level.ERROR, "Received [" + tempMsg.getText() + "] expected no message");
				logger.log(Logger.Level.ERROR, "MessageConsumer should have returned " + numMessages + " messages");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			e.printStackTrace();
			throw new Exception("createConsumerProducerTopicTest");
		} finally {
			try {
				if (consumer != null)
					consumer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("createConsumerProducerTopicTest failed");
		}
	}

	/*
	 * @testName: createDurableSubscriberTopicTest1
	 *
	 * @assertion_ids: JMS:JAVADOC:254;
	 *
	 * @test_Strategy: Creates a durable subscription with the specified name on the
	 * specified topic, and creates a TopicSubscriber on that durable subscription.
	 *
	 * This uses a connection factory WITH client identifier set. The client
	 * identifer MUST be set for this API.
	 *
	 * Tests the following API method:
	 *
	 * Session.createDurableSubscriber(Topic, String)
	 *
	 * 1. Send a text message to a Topic. 2. Consume message via the TopicSubscriber
	 * created.
	 */
	@Test
	public void createDurableSubscriberTopicTest1() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		TopicSubscriber tSub = null;
		try {
			TextMessage expTextMessage = null;

			// set up JmsTool for COMMON_T setup (durable factory has clientid set)
			toolT.getDefaultConnection().close();
			toolT.closeAllResources();
			toolT2 = new JmsTool(JmsTool.COMMON_T, user, password, lookupDurableTopicFactory, mode);

			// Setup for Topic
			setupGlobalVarsT2();

			// Create Durable Subscription and a TopicSubscriber for it
			logger.log(Logger.Level.INFO, "Create a Durable Subscription and a TopicSubscriber for it");
			tSub = session.createDurableSubscriber(topic, "createDurableSubscriberTopicTest1");

			logger.log(Logger.Level.INFO, "Send message to Topic");
			expTextMessage = session.createTextMessage(message);
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "createDurableSubscriberTopicTest1");
			producer.send(expTextMessage);
			logger.log(Logger.Level.INFO, "Message sent");

			logger.log(Logger.Level.INFO, "Receive TextMessage");
			TextMessage actTextMessage = (TextMessage) tSub.receive(timeout);
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
			e.printStackTrace();
			throw new Exception("createDurableSubscriberTopicTest1", e);
		} finally {
			try {
				cleanupSubscription(tSub, session, "createDurableSubscriberTopicTest1");
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("createDurableSubscriberTopicTest1 failed");
		}
	}

	/*
	 * @testName: createDurableSubscriberTopicTest2
	 *
	 * @assertion_ids: JMS:JAVADOC:256;
	 *
	 * @test_Strategy: Creates a durable subscription with the specified name on the
	 * specified topic, and creates a TopicSubscriber on that durable subscription,
	 * specifying a message selector and whether messages published by its own
	 * connection should be delivered to it.
	 *
	 * This uses a connection factory WITH client identifier set. The client
	 * identifer MUST be set for this API.
	 *
	 * Tests the following API method:
	 *
	 * Session.createDurableSubscriber(Topic,String,String,boolean)
	 *
	 * 1 Create a durable subscriber on the specified topic, and create a
	 * TopicSubscriber with the specified message selector 2 Send a number of
	 * messages to the destination 3 Test both noLocal=true and noLocal=false cases
	 * 4 Verify message with specified selector received by listener in the
	 * noLocal=false case only.
	 *
	 */
	@Test
	public void createDurableSubscriberTopicTest2() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		TopicSubscriber tSub = null;
		try {
			// set up JmsTool for COMMON_T setup (durable factory has clientid set)
			toolT.getDefaultConnection().close();
			toolT.closeAllResources();
			toolT2 = new JmsTool(JmsTool.COMMON_T, user, password, lookupDurableTopicFactory, mode);

			// Setup for Topic
			setupGlobalVarsT2();

			// Create Durable Subscription and a TopicSubscriber for it
			// Test the noLocal=false case with message selector
			logger.log(Logger.Level.INFO, "Create a Durable Subscription and a TopicSubscriber for it");
			tSub = session.createDurableSubscriber(topic, "createDurableSubscriberTopicTest2", "lastMessage = TRUE",
					false);

			// send "numMessages" messages to Topic
			logger.log(Logger.Level.INFO, "Send " + numMessages + " to Topic");
			for (int i = 1; i <= numMessages; i++) {
				TextMessage tempMsg = session.createTextMessage("Message " + i);
				tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "createDurableSubscriberTopicTest2" + i);
				if (i == numMessages) {
					tempMsg.setBooleanProperty("lastMessage", true);
				} else {
					tempMsg.setBooleanProperty("lastMessage", false);
				}
				producer.send(tempMsg);
				logger.log(Logger.Level.INFO, "Message " + i + " sent");
			}

			logger.log(Logger.Level.INFO, "Receive TextMessage");
			TextMessage expTextMessage = session.createTextMessage("Message " + numMessages);
			TextMessage actTextMessage = (TextMessage) tSub.receive(timeout);
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
			e.printStackTrace();
			throw new Exception("createDurableSubscriberTopicTest2", e);
		} finally {
			try {
				cleanupSubscription(tSub, session, "createDurableSubscriberTopicTest2");
			} catch (Exception e) {
			}
		}

		try {

			// Create Durable Subscription and a TopicSubscriber for it
			// Test the noLocal=true case with message selector
			logger.log(Logger.Level.INFO, "Create a Durable Subscription and a TopicSubscriber for it");
			tSub = session.createDurableSubscriber(topic, "createDurableSubscriberTopicTest2", "lastMessage = TRUE",
					true);

			// send "numMessages" messages to Topic
			logger.log(Logger.Level.INFO, "Send " + numMessages + " to Topic");
			for (int i = 1; i <= numMessages; i++) {
				TextMessage tempMsg = session.createTextMessage("Message " + i);
				tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "createDurableSubscriberTopicTest2" + i);
				if (i == numMessages) {
					tempMsg.setBooleanProperty("lastMessage", true);
				} else {
					tempMsg.setBooleanProperty("lastMessage", false);
				}
				producer.send(tempMsg);
				logger.log(Logger.Level.INFO, "Message " + i + " sent");
			}

			logger.log(Logger.Level.INFO, "Receive TextMessage");
			TextMessage actTextMessage = (TextMessage) tSub.receive(timeout);

			if (actTextMessage != null) {
				logger.log(Logger.Level.ERROR, "message were delivered when noLocal=true");
				pass = false;
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			e.printStackTrace();
			throw new Exception("createDurableSubscriberTopicTest2", e);
		} finally {
			try {
				cleanupSubscription(tSub, session, "createDurableSubscriberTopicTest2");
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("createDurableSubscriberTopicTest2 failed");
		}
	}

	/*
	 * @testName: createDurableConsumerTopicTest1
	 *
	 * @assertion_ids: JMS:JAVADOC:1087;
	 *
	 * @test_Strategy: Creates a durable subscription with the specified name on the
	 * specified topic, and creates a MessageConsumer on that durable subscription.
	 *
	 * This uses a connection factory WITH client identifier set. The client
	 * identifer MUST be set for this API.
	 *
	 * Tests the following API method:
	 *
	 * Session.createDurableConsumer(Topic, String)
	 *
	 * 1. Create a durable subscription with the specified name on the specified
	 * topic and create a durable MessageConsumer on that durable subscription. This
	 * uses a connection factory WITH client identifier set. 2. Send TextMessage
	 * (message1) to the Topic. 3. Consume message via MessageConsumer created.
	 * Verify message1 received. 4. Close consumer. 5. Send another TextMessage
	 * (message2) to the Topic. 6. Recreate the durable MessageConsumer on that
	 * durable subscription. 7. Consume message via MessageConsumer created. Verify
	 * message2 received.
	 */
	@Test
	public void createDurableConsumerTopicTest1() throws Exception {
		boolean pass = true;
		String message1 = "Where are you!";
		String message2 = "Who are you!";
		MessageConsumer tCon = null;
		try {
			TextMessage expTextMessage = null;

			// set up JmsTool for COMMON_T setup (durable factory has clientid set)
			toolT.getDefaultConnection().close();
			toolT.closeAllResources();
			toolT2 = new JmsTool(JmsTool.COMMON_T, user, password, lookupDurableTopicFactory, mode);

			// Setup for Topic
			setupGlobalVarsT2();

			// Create Durable Subscription and a MessageConsumer for it
			logger.log(Logger.Level.INFO, "Create a Durable Subscription and a MessageConsumer for it");
			tCon = session.createDurableConsumer(topic, "createDurableConsumerTopicTest1");

			logger.log(Logger.Level.INFO, "Send TextMessage message1 to Topic");
			expTextMessage = session.createTextMessage(message1);
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "createDurableConsumerTopicTest1");
			producer.send(expTextMessage);
			logger.log(Logger.Level.INFO, "TextMessage message1 sent");

			logger.log(Logger.Level.INFO, "Receive TextMessage message1");
			TextMessage actTextMessage = (TextMessage) tCon.receive(timeout);
			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the value in TextMessage message1");
			if (actTextMessage.getText().equals(expTextMessage.getText())) {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
						+ ", received " + actTextMessage.getText());
				pass = false;
			}
			logger.log(Logger.Level.INFO, "Close durable MessageConsumer");
			tCon.close();

			logger.log(Logger.Level.INFO, "Send TextMessage message2 to Topic");
			expTextMessage = session.createTextMessage(message1);
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "createDurableConsumerTopicTest1");
			producer.send(expTextMessage);
			logger.log(Logger.Level.INFO, "TextMessage message2 sent");

			// Recreate Durable Subscription and a MessageConsumer for it
			logger.log(Logger.Level.INFO, "Recreate a Durable Subscription and a MessageConsumer for it");
			tCon = session.createDurableConsumer(topic, "createDurableConsumerTopicTest1");

			logger.log(Logger.Level.INFO, "Receive TextMessage message2");
			actTextMessage = (TextMessage) tCon.receive(timeout);
			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the value in TextMessage message2");
			if (actTextMessage.getText().equals(expTextMessage.getText())) {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
						+ ", received " + actTextMessage.getText());
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			e.printStackTrace();
			throw new Exception("createDurableConsumerTopicTest1", e);
		} finally {
			try {
				cleanupSubscription(tCon, session, "createDurableConsumerTopicTest1");
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("createDurableConsumerTopicTest1 failed");
		}
	}

	/*
	 * @testName: createDurableConsumerTopicTest2
	 *
	 * @assertion_ids: JMS:JAVADOC:1090;
	 *
	 * @test_Strategy: Creates a durable subscription with the specified name on the
	 * specified topic, and creates a MessageConsumer on that durable subscription,
	 * specifying a message selector and whether messages published by its own
	 * connection should be delivered to it.
	 *
	 * This uses a connection factory WITH client identifier set. The client
	 * identifer MUST be set for this API.
	 *
	 * Tests the following API method:
	 *
	 * Session.createDurableConsumer(Topic,String,String,boolean)
	 *
	 * 1. Create a durable subscription with the specified name on the specified
	 * topic and create a durable MessageConsumer on that durable subscription
	 * specifing a message selector and whether messages published by its own
	 * connection should be delivered to it. 2. Send a number of messages to the
	 * Topic. 3. Test both noLocal=true and noLocal=false cases. 4. Verify message
	 * with specified selector received by MessageConsumer in the noLocal=false case
	 * only.
	 *
	 */
	@Test
	public void createDurableConsumerTopicTest2() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		MessageConsumer tCon = null;
		try {
			// set up JmsTool for COMMON_T setup (durable factory has clientid set)
			toolT.getDefaultConnection().close();
			toolT.closeAllResources();
			toolT2 = new JmsTool(JmsTool.COMMON_T, user, password, lookupDurableTopicFactory, mode);

			// Setup for Topic
			setupGlobalVarsT2();

			// Create Durable Subscription and a MessageConsumer for it
			// Test the noLocal=false case with message selector
			logger.log(Logger.Level.INFO,
					"Create a Durable Subscription and a MessageConsumer with message selector, noLocal=false");
			tCon = session.createDurableConsumer(topic, "createDurableConsumerTopicTest2", "lastMessage = TRUE", false);

			// send "numMessages" messages to Topic
			logger.log(Logger.Level.INFO, "Send " + numMessages + " to Topic");
			for (int i = 1; i <= numMessages; i++) {
				TextMessage tempMsg = session.createTextMessage("Message " + i);
				tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "createDurableConsumerTopicTest2" + i);
				if (i == numMessages) {
					tempMsg.setBooleanProperty("lastMessage", true);
				} else {
					tempMsg.setBooleanProperty("lastMessage", false);
				}
				producer.send(tempMsg);
				logger.log(Logger.Level.INFO, "Message " + i + " sent");
			}

			logger.log(Logger.Level.INFO, "Receive TextMessage");
			logger.log(Logger.Level.INFO, "This is noLacal=false case so expect to get just last message");
			TextMessage expTextMessage = session.createTextMessage("Message " + numMessages);
			TextMessage actTextMessage = (TextMessage) tCon.receive(timeout);
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
			e.printStackTrace();
			throw new Exception("createDurableConsumerTopicTest2", e);
		} finally {
			try {
				cleanupSubscription(tCon, session, "createDurableConsumerTopicTest2");
			} catch (Exception e) {
			}
		}

		try {
			// Create Durable Subscription and a MessageConsumer for it
			// Test the noLocal=true case with message selector
			logger.log(Logger.Level.INFO,
					"Create a Durable Subscription and a MessageConsumer with message selector, noLocal=true");
			tCon = session.createDurableConsumer(topic, "createDurableConsumerTopicTest2", "lastMessage = TRUE", true);

			// send "numMessages" messages to Topic
			logger.log(Logger.Level.INFO, "Send " + numMessages + " to Topic");
			for (int i = 1; i <= numMessages; i++) {
				TextMessage tempMsg = session.createTextMessage("Message " + i);
				tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "createDurableConsumerTopicTest2" + i);
				if (i == numMessages) {
					tempMsg.setBooleanProperty("lastMessage", true);
				} else {
					tempMsg.setBooleanProperty("lastMessage", false);
				}
				producer.send(tempMsg);
				logger.log(Logger.Level.INFO, "Message " + i + " sent");
			}

			logger.log(Logger.Level.INFO, "Receive TextMessage");
			TextMessage actTextMessage = (TextMessage) tCon.receive(timeout);

			if (actTextMessage != null) {
				logger.log(Logger.Level.ERROR, "Message was delivered when noLocal=true");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			e.printStackTrace();
			throw new Exception("createDurableConsumerTopicTest2", e);
		} finally {
			try {
				cleanupSubscription(tCon, session, "createDurableConsumerTopicTest2");
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("createDurableConsumerTopicTest2 failed");
		}
	}

	/*
	 * @testName: createSharedConsumerTopicTest1
	 *
	 * @assertion_ids: JMS:JAVADOC:1163; JMS:SPEC:269;
	 *
	 * @test_Strategy: Creates a shared non-durable subscription with the specified
	 * name on the specified topic, and creates a MessageConsumer on that
	 * subscription.
	 *
	 * Tests the following API method:
	 *
	 * Session.createSharedConsumer(Topic, String)
	 *
	 * 1. Creates a shared non-durable subscription with the specified name on the
	 * specified topic, and creates a MessageConsumer on that subscription. 2.
	 * Create a second MessageConsumer on that subscription. 3. Send a text message
	 * to the Topic. 4. Consume message via the first MessageConsumer and message
	 * should be received. 5. Attempt to consume message via second MessageConsumer
	 * and no message should be received. 6. Re-Send a text message to the Topic. 7.
	 * Consume message via the second MessageConsumer and message should be
	 * received. 8. Attempt to consume message via first MessageConsumer and no
	 * message should be received.
	 */
	@Test
	public void createSharedConsumerTopicTest1() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		String sharedSubscriptionName = "createSharedConsumerTopicTest1";
		TextMessage expTextMessage = null;
		try {
			// Setup for Topic
			setupGlobalVarsT();

			// Create shared non-durable Subscription and a MessageConsumer for it
			logger.log(Logger.Level.INFO, "Create a shared non-durable Subscription and a MessageConsumer for it");
			consumer.close();
			consumer = session.createSharedConsumer(topic, sharedSubscriptionName);

			// Create a second MessageConsumer for the subscription
			logger.log(Logger.Level.INFO, "Create a second MessageConsumer for the Subscription");
			consumer2 = session.createSharedConsumer(topic, sharedSubscriptionName);

			logger.log(Logger.Level.INFO, "Send message to Topic");
			expTextMessage = session.createTextMessage(message);
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", sharedSubscriptionName);
			producer.send(expTextMessage);
			logger.log(Logger.Level.INFO, "Message sent");

			logger.log(Logger.Level.INFO, "Receive TextMessage from consumer1");
			TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage == null) {
				logger.log(Logger.Level.ERROR, "Did not receive TextMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the value in TextMessage");
				if (actTextMessage.getText().equals(expTextMessage.getText())) {
					logger.log(Logger.Level.INFO, "TextMessage is correct");
				} else {
					logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
							+ ", received " + actTextMessage.getText());
					pass = false;
				}
			}

			logger.log(Logger.Level.INFO, "Attempt to Receive TextMessage from consumer2 - there should be none");
			actTextMessage = (TextMessage) consumer2.receive(timeout);
			if (actTextMessage != null) {
				throw new Exception("Did receive TextMessage - unexpected.");
			} else
				logger.log(Logger.Level.INFO, "Did not receive TextMessage - expected.");

			logger.log(Logger.Level.INFO, "Send another message to Topic");
			expTextMessage = session.createTextMessage(message);
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", sharedSubscriptionName);
			producer.send(expTextMessage);
			logger.log(Logger.Level.INFO, "Message sent");

			logger.log(Logger.Level.INFO, "Receive TextMessage from consumer2");
			actTextMessage = (TextMessage) consumer2.receive(timeout);
			if (actTextMessage == null) {
				logger.log(Logger.Level.ERROR, "Did not receive TextMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the value in TextMessage");
				if (actTextMessage.getText().equals(expTextMessage.getText())) {
					logger.log(Logger.Level.INFO, "TextMessage is correct");
				} else {
					logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
							+ ", received " + actTextMessage.getText());
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Attempt to Receive TextMessage from consumer1 - there should be none");
			actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage != null) {
				throw new Exception("Did receive TextMessage - unexpected.");
			} else
				logger.log(Logger.Level.INFO, "Did not receive TextMessage - expected.");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			e.printStackTrace();
			throw new Exception("createSharedConsumerTopicTest1", e);
		} finally {
			try {
				if (consumer != null)
					consumer.close();
				if (consumer2 != null)
					consumer2.close();
			} catch (Exception e) {
				logger.log(Logger.Level.INFO, "Ignoring exception closing consumers: " + e);
			}
		}

		if (!pass) {
			throw new Exception("createSharedConsumerTopicTest1 failed");
		}
	}

	/*
	 * @testName: createSharedConsumerTopicTest2
	 *
	 * @assertion_ids: JMS:JAVADOC:1167; JMS:SPEC:269; JMS:SPEC:270;
	 *
	 * @test_Strategy: Creates a shared non-durable subscription with the specified
	 * name on the specified topic, and creates a MessageConsumer on that
	 * subscription, specifying a message selector.
	 *
	 * Tests the following API method:
	 *
	 * Session.createSharedConsumer(Topic, String, String)
	 *
	 * 1. Create a shared non-durable subscription with the specified name on the
	 * specified topic, and creates a MessageConsumer on that subscription,
	 * specifying a message selector. 2. Create a second MessageConsumer on that
	 * subscription. 3. Send a text message to the Topic. 4. Consume message via
	 * first MessageConsumer and message selector and message should be received. 5.
	 * Attempt to consume message via second MessageConsumer and message selector
	 * and no message received. 6. Re-Send a text message to the Topic. 7. Consume
	 * message via second MessageConsumer and message selector and message should be
	 * received. 8. Attempt to consume message via first MessageConsumer and message
	 * selector and no message received.
	 */
	@Test
	public void createSharedConsumerTopicTest2() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		String sharedSubscriptionName = "createSharedConsumerTopicTest2";
		try {
			// Setup for Topic
			setupGlobalVarsT();

			// Create shared non-durable Subscription and a MessageConsumer for it
			logger.log(Logger.Level.INFO, "Create a shared non-durable Subscription and a MessageConsumer for it");
			consumer.close();
			consumer = session.createSharedConsumer(topic, sharedSubscriptionName, "lastMessage = TRUE");

			// Create a second MessageConsumer for the subscription
			logger.log(Logger.Level.INFO, "Create a second MessageConsumer for the Subscription");
			consumer2 = session.createSharedConsumer(topic, sharedSubscriptionName, "lastMessage = TRUE");

			// send "numMessages" messages to Topic
			logger.log(Logger.Level.INFO, "Send " + numMessages + " to Topic");
			for (int i = 1; i <= numMessages; i++) {
				TextMessage tempMsg = session.createTextMessage("Message " + i);
				tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME", sharedSubscriptionName + i);
				if (i == numMessages) {
					tempMsg.setBooleanProperty("lastMessage", true);
				} else {
					tempMsg.setBooleanProperty("lastMessage", false);
				}
				producer.send(tempMsg);
				logger.log(Logger.Level.INFO, "Message " + i + " sent");
			}

			logger.log(Logger.Level.INFO, "Receive TextMessage from consumer1");
			TextMessage expTextMessage = session.createTextMessage("Message " + numMessages);
			TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage == null) {
				logger.log(Logger.Level.ERROR, "Did not receive TextMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the value in TextMessage");
				if (actTextMessage.getText().equals(expTextMessage.getText())) {
					logger.log(Logger.Level.INFO, "TextMessage is correct");
				} else {
					logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
							+ ", received " + actTextMessage.getText());
					pass = false;
				}
			}

			logger.log(Logger.Level.INFO, "Attempt to Receive TextMessage from consumer2 - there should be none");
			actTextMessage = (TextMessage) consumer2.receive(timeout);
			if (actTextMessage != null) {
				throw new Exception("Did receive TextMessage - unexpected.");
			} else
				logger.log(Logger.Level.INFO, "Did not receive TextMessage - expected.");

			// send "numMessages" messages to Topic
			logger.log(Logger.Level.INFO, "Send " + numMessages + " to Topic");
			for (int i = 1; i <= numMessages; i++) {
				TextMessage tempMsg = session.createTextMessage("Message " + i);
				tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME", sharedSubscriptionName + i);
				if (i == numMessages) {
					tempMsg.setBooleanProperty("lastMessage", true);
				} else {
					tempMsg.setBooleanProperty("lastMessage", false);
				}
				producer.send(tempMsg);
				logger.log(Logger.Level.INFO, "Message " + i + " sent");
			}

			logger.log(Logger.Level.INFO, "Receive TextMessage from consumer2");
			expTextMessage = session.createTextMessage("Message " + numMessages);
			actTextMessage = (TextMessage) consumer2.receive(timeout);
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
			logger.log(Logger.Level.INFO, "Attempt to Receive TextMessage from consumer1 - there should be none");
			actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage != null) {
				throw new Exception("Did receive TextMessage - unexpected.");
			} else
				logger.log(Logger.Level.INFO, "Did not receive TextMessage - expected.");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			e.printStackTrace();
			throw new Exception("createSharedConsumerTopicTest2", e);
		} finally {
			try {
				if (consumer != null)
					consumer.close();
				if (consumer2 != null)
					consumer2.close();
			} catch (Exception e) {
				logger.log(Logger.Level.INFO, "Ignoring exception closing consumers: " + e);
			}
		}

		if (!pass) {
			throw new Exception("createSharedConsumerTopicTest2 failed");
		}
	}

	/*
	 * @testName: createSharedDurableConsumerTopicTest1
	 *
	 * @assertion_ids: JMS:JAVADOC:1393;
	 *
	 * @test_Strategy: Creates a shared durable subscription with the specified name
	 * on the specified topic and creates a JMSConsumer on that durable
	 * subscription.
	 *
	 * This uses a connection factory WITH client identifier set.
	 *
	 * Tests the following API method:
	 *
	 * Session.createSharedDurableConsumer(Topic, String)
	 *
	 * 1. Create a shared durable subscription with the specified name on the
	 * specified topic and create a durable JMSConsumer on that durable
	 * subscription. This uses a connection factory WITH client identifier set. 2.
	 * Create a 2nd JMSConsumer for it. 3. Send TextMessage (message1) to the Topic.
	 * 3. Consume message via 1st JMSConsumer created. Verify message1 received. 4.
	 * Close 1st consumer. 5. Send another TextMessage (message2) to the Topic. 6.
	 * Consume message via 2nd JMSConsumer created. Verify message2 received.
	 */
	@Test
	public void createSharedDurableConsumerTopicTest1() throws Exception {
		boolean pass = true;
		String message1 = "Message1!";
		String message2 = "Message2!";
		String durableSubscriptionName = "createSharedDurableConsumerTopicTest1";
		try {
			// set up JmsTool for COMMON_T setup (durable factory has clientid set)
			toolT.getDefaultConnection().close();
			toolT.closeAllResources();
			toolT2 = new JmsTool(JmsTool.COMMON_T, user, password, lookupDurableTopicFactory, mode);

			// Setup for Topic
			setupGlobalVarsT2();

			TextMessage expTextMessage = null;

			// Create a shared Durable Subscription and a JMSConsumer for it
			logger.log(Logger.Level.INFO, "Create a shared Durable Subscription and 1st JMSConsumer for it");
			consumer = session.createSharedDurableConsumer(topic, durableSubscriptionName);

			// Create 2nd JMSConsumer for it
			logger.log(Logger.Level.INFO, "Create 2nd JMSConsumer for it");
			consumer2 = session.createSharedDurableConsumer(topic, durableSubscriptionName);

			logger.log(Logger.Level.INFO, "Send TextMessage message1 to Topic");
			expTextMessage = session.createTextMessage(message1);
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "createSharedDurableConsumerTopicTest1");
			producer.send(expTextMessage);
			logger.log(Logger.Level.INFO, "TextMessage message1 sent");

			logger.log(Logger.Level.INFO, "Receive TextMessage message1 from consumer1");
			TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage == null) {
				logger.log(Logger.Level.ERROR, "Did not receive TextMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the value in TextMessage message1");
				if (actTextMessage.getText().equals(expTextMessage.getText())) {
					logger.log(Logger.Level.INFO, "TextMessage is correct");
				} else {
					logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
							+ ", received " + actTextMessage.getText());
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Close 1st shared durable JMSConsumer");
			consumer.close();

			logger.log(Logger.Level.INFO, "Send TextMessage message2 to Topic");
			expTextMessage = session.createTextMessage(message2);
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "createSharedDurableConsumerTopicTest1");
			producer.send(expTextMessage);
			logger.log(Logger.Level.INFO, "TextMessage message2 sent");

			logger.log(Logger.Level.INFO, "Receive TextMessage message2 from consumer2");
			actTextMessage = (TextMessage) consumer2.receive(timeout);
			if (actTextMessage == null) {
				logger.log(Logger.Level.ERROR, "Did not receive TextMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the value in TextMessage message2");
				if (actTextMessage.getText().equals(expTextMessage.getText())) {
					logger.log(Logger.Level.INFO, "TextMessage is correct");
				} else {
					logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
							+ ", received " + actTextMessage.getText());
					pass = false;
				}
			}

			logger.log(Logger.Level.INFO, "Now there should be no more messages to receive from topic");

			logger.log(Logger.Level.INFO, "Recreate Durable Subscription and 1st JMSConsumer for it");
			consumer = session.createSharedDurableConsumer(topic, durableSubscriptionName);

			logger.log(Logger.Level.INFO, "Try and receive a message from consumer1 (should get NONE)");
			actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage != null) {
				logger.log(Logger.Level.ERROR, "Consumer1 received a message (FAIL)");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Consumer1 didn't receive a message (PASS)");
			}

			logger.log(Logger.Level.INFO, "Try and receive a message from consumer2 (should get NONE)");
			actTextMessage = (TextMessage) consumer2.receive(timeout);
			if (actTextMessage != null) {
				logger.log(Logger.Level.ERROR, "Consumer2 received a message (FAIL)");
				pass = false;
				throw new Exception("Consumer2 received a message (FAIL)");
			} else {
				logger.log(Logger.Level.INFO, "Consumer2 didn't receive a message (PASS)");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			e.printStackTrace();
			throw new Exception("createSharedDurableConsumerTopicTest1", e);
		} finally {
			try {
				if (consumer != null)
					consumer.close();
				cleanupSubscription(consumer2, session, durableSubscriptionName);
				toolT2.getDefaultConnection().close();
				toolT2.closeAllResources();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			}
		}

		if (!pass) {
			throw new Exception("createSharedDurableConsumerTopicTest1 failed");
		}
	}

	/*
	 * @testName: createSharedDurableConsumerTopicTest2
	 *
	 * @assertion_ids: JMS:JAVADOC:1396;
	 *
	 * @test_Strategy: Creates a shared durable subscription with the specified name
	 * on the specified topic and creates a JMSConsumer on that durable
	 * subscription, specifying a message selector and whether messages published by
	 * its own connection should be delivered to it.
	 *
	 * This uses a connection factory WITH client identifier set.
	 *
	 * Tests the following API method:
	 *
	 * Session.createSharedDurableConsumer(Topic,String,String)
	 *
	 * 1. Create a shared durable subscription with the specified name on the
	 * specified topic and create a durable JMSConsumer on that durable subscription
	 * specifing a message selector and whether messages published by its own
	 * connection should be delivered to it. This uses a connection factory WITH
	 * client identifier set. 2. Create a 2nd JMSConsumer for it. 3. Send a number
	 * of messages to the Topic.
	 *
	 */
	@Test
	public void createSharedDurableConsumerTopicTest2() throws Exception {
		boolean pass = true;
		String durableSubscriptionName = "createSharedDurableConsumerTopicTest2";
		try {
			// set up JmsTool for COMMON_T setup (durable factory has clientid set)
			toolT.getDefaultConnection().close();
			toolT.closeAllResources();
			toolT2 = new JmsTool(JmsTool.COMMON_T, user, password, lookupDurableTopicFactory, mode);

			// Setup for Topic
			setupGlobalVarsT2();

			// Create a shared Durable Subscription and a JMSConsumer for it
			logger.log(Logger.Level.INFO,
					"Create shared Durable Subscription and 1st JMSConsumer with message selector");
			consumer = session.createSharedDurableConsumer(topic, durableSubscriptionName, "lastMessage = TRUE");
			// Create 2nd JMSConsumer for it
			logger.log(Logger.Level.INFO, "Create 2nd JMSConsumer with message selector");
			consumer2 = session.createSharedDurableConsumer(topic, durableSubscriptionName, "lastMessage = TRUE");

			// send "numMessages" messages to Topic
			logger.log(Logger.Level.INFO, "Send " + numMessages + " messages to Topic");
			for (int i = 1; i <= numMessages; i++) {
				TextMessage tempMsg = session.createTextMessage("Message " + i);
				tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "createSharedDurableConsumerTopicTest2" + i);
				if (i == numMessages) {
					tempMsg.setBooleanProperty("lastMessage", true);
				} else {
					tempMsg.setBooleanProperty("lastMessage", false);
				}
				producer.send(tempMsg);
				logger.log(Logger.Level.INFO, "Message " + i + " sent");
			}

			logger.log(Logger.Level.INFO, "Receive TextMessage from consumer1");
			TextMessage expTextMessage = session.createTextMessage("Message " + numMessages);
			TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage == null) {
				logger.log(Logger.Level.ERROR, "Did not receive TextMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the value in TextMessage");
				if (actTextMessage.getText().equals(expTextMessage.getText())) {
					logger.log(Logger.Level.INFO, "TextMessage is correct");
				} else {
					logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
							+ ", received " + actTextMessage.getText());
					pass = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			e.printStackTrace();
			throw new Exception("createSharedDurableConsumerTopicTest2", e);
		} finally {
			try {
				if (consumer != null)
					consumer.close();
				cleanupSubscription(consumer2, session, durableSubscriptionName);
				toolT2.getDefaultConnection().close();
				toolT2.closeAllResources();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			}
		}

		if (!pass) {
			throw new Exception("createSharedDurableConsumerTopicTest2 failed");
		}
	}

	/*
	 * @testName: createSharedDurableConsumerTopicTest3
	 *
	 * @assertion_ids: JMS:JAVADOC:1393;
	 *
	 * @test_Strategy: Creates a shared durable subscription with the specified name
	 * on the specified topic and creates a JMSConsumer on that durable
	 * subscription.
	 *
	 * This uses a connection factory WITH client identifier unset.
	 *
	 * Tests the following API method:
	 *
	 * Session.createSharedDurableConsumer(Topic, String)
	 *
	 * 1. Create a shared durable subscription with the specified name on the
	 * specified topic and create a durable JMSConsumer on that durable
	 * subscription. This uses a connection factory WITH client identifier unset. 2.
	 * Create a 2nd JMSConsumer for it. 3. Send TextMessage (message1) to the Topic.
	 * 3. Consume message via 1st JMSConsumer created. Verify message1 received. 4.
	 * Close 1st consumer. 5. Send another TextMessage (message2) to the Topic. 6.
	 * Consume message via 2nd JMSConsumer created. Verify message2 received.
	 */
	@Test
	public void createSharedDurableConsumerTopicTest3() throws Exception {
		boolean pass = true;
		String message1 = "Message1!";
		String message2 = "Message2!";
		String durableSubscriptionName = "createSharedDurableConsumerTopicTest3";
		try {
			// Setup for Topic
			setupGlobalVarsT();

			TextMessage expTextMessage = null;

			// Create a shared Durable Subscription and a JMSConsumer for it
			logger.log(Logger.Level.INFO, "Create a shared Durable Subscription and 1st JMSConsumer for it");
			consumer = session.createSharedDurableConsumer(topic, durableSubscriptionName);

			// Create 2nd JMSConsumer for it
			logger.log(Logger.Level.INFO, "Create 2nd JMSConsumer for it");
			consumer2 = session.createSharedDurableConsumer(topic, durableSubscriptionName);

			logger.log(Logger.Level.INFO, "Send TextMessage message1 to Topic");
			expTextMessage = session.createTextMessage(message1);
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "createSharedDurableConsumerTopicTest3");
			producer.send(expTextMessage);
			logger.log(Logger.Level.INFO, "TextMessage message1 sent");

			logger.log(Logger.Level.INFO, "Receive TextMessage message1 from consumer1");
			TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage == null) {
				logger.log(Logger.Level.ERROR, "Did not receive TextMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the value in TextMessage message1");
				if (actTextMessage.getText().equals(expTextMessage.getText())) {
					logger.log(Logger.Level.INFO, "TextMessage is correct");
				} else {
					logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
							+ ", received " + actTextMessage.getText());
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Close 1st shared durable JMSConsumer");
			consumer.close();

			logger.log(Logger.Level.INFO, "Send TextMessage message2 to Topic");
			expTextMessage = session.createTextMessage(message2);
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "createSharedDurableConsumerTopicTest3");
			producer.send(expTextMessage);
			logger.log(Logger.Level.INFO, "TextMessage message2 sent");

			logger.log(Logger.Level.INFO, "Receive TextMessage message2 from consumer2");
			actTextMessage = (TextMessage) consumer2.receive(timeout);
			if (actTextMessage == null) {
				logger.log(Logger.Level.ERROR, "Did not receive TextMessage");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Check the value in TextMessage message2");
				if (actTextMessage.getText().equals(expTextMessage.getText())) {
					logger.log(Logger.Level.INFO, "TextMessage is correct");
				} else {
					logger.log(Logger.Level.ERROR, "TextMessage is incorrect expected " + expTextMessage.getText()
							+ ", received " + actTextMessage.getText());
					pass = false;
				}
			}

			logger.log(Logger.Level.INFO, "Now there should be no more messages to receive from topic");

			logger.log(Logger.Level.INFO, "Recreate Durable Subscription and 1st JMSConsumer for it");
			consumer = session.createSharedDurableConsumer(topic, durableSubscriptionName);

			logger.log(Logger.Level.INFO, "Try and receive a message from consumer1 (should get NONE)");
			actTextMessage = (TextMessage) consumer.receive(timeout);
			if (actTextMessage != null) {
				logger.log(Logger.Level.ERROR, "Consumer1 received a message (FAIL)");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Consumer1 didn't receive a message (PASS)");
			}

			logger.log(Logger.Level.INFO, "Try and receive a message from consumer2 (should get NONE)");
			actTextMessage = (TextMessage) consumer2.receive(timeout);
			if (actTextMessage != null) {
				logger.log(Logger.Level.ERROR, "Consumer2 received a message (FAIL)");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Consumer2 didn't receive a message (PASS)");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			e.printStackTrace();
			throw new Exception("createSharedDurableConsumerTopicTest3", e);
		} finally {
			try {
				if (consumer != null)
					consumer.close();
				cleanupSubscription(consumer2, session, durableSubscriptionName);
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("createSharedDurableConsumerTopicTest3 failed");
		}
	}

	/*
	 * @testName: invalidDestinationExceptionTests
	 *
	 * @assertion_ids: JMS:JAVADOC:1089; JMS:JAVADOC:1092; JMS:JAVADOC:643;
	 * JMS:JAVADOC:644; JMS:JAVADOC:1165; JMS:JAVADOC:1169; JMS:JAVADOC:1395;
	 * JMS:JAVADOC:1398;
	 *
	 * @test_Strategy: Pass an invalid topic and test for
	 * InvalidDestinationException.
	 *
	 * Session.createDurableSubscriber(Topic, String)
	 * Session.createDurableSubscriber(Topic, String, String, boolean)
	 * Session.createDurableConsumer(Topic, String)
	 * Session.createDurableConsumer(Topic, String, String, boolean)
	 * Session.createSharedConsumer(Topic, String)
	 * Session.createSharedConsumer(Topic, String, String)
	 * Session.createSharedDurableConsumer(Topic, String)
	 * Session.createSharedDurableConsumer(Topic, String, String)
	 */
	@Test
	public void invalidDestinationExceptionTests() throws Exception {
		boolean pass = true;
		Destination invalidDestination = null;
		Topic invalidTopic = null;
		try {
			// set up JmsTool for COMMON_T setup (durable factory has clientid set)
			toolT.getDefaultConnection().close();
			toolT.closeAllResources();
			toolT2 = new JmsTool(JmsTool.COMMON_T, user, password, lookupDurableTopicFactory, mode);

			// Setup for Topic
			setupGlobalVarsT2();

			logger.log(Logger.Level.INFO,
					"Testing Session.createDurableSubscriber(Topic, String) for InvalidDestinationException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling Session.createDurableSubscriber(Topic, String) -> expect InvalidDestinationException");
				session.createDurableSubscriber(invalidTopic, "InvalidDestinationException");
			} catch (InvalidDestinationException e) {
				logger.log(Logger.Level.INFO, "Got InvalidDestinationException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing Session.createDurableSubscriber(Topic, String, String, boolean) for InvalidDestinationException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling Session.createDurableSubscriber(Topic, String, String, boolean) -> expect InvalidDestinationException");
				session.createDurableSubscriber(invalidTopic, "InvalidDestinationException", "lastMessage = TRUE",
						false);
			} catch (InvalidDestinationException e) {
				logger.log(Logger.Level.INFO, "Got InvalidDestinationException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing Session.createDurableConsumer(Topic, String) for InvalidDestinationException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling Session.createDurableConsumer(Topic, String) -> expect InvalidDestinationException");
				session.createDurableConsumer(invalidTopic, "InvalidDestinationException");
			} catch (InvalidDestinationException e) {
				logger.log(Logger.Level.INFO, "Got InvalidDestinationException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing Session.createDurableConsumer(Topic, String, String, boolean) for InvalidDestinationException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling Session.createDurableConsumer(Topic, String, String, boolean) -> expect InvalidDestinationException");
				session.createDurableConsumer(invalidTopic, "InvalidDestinationException", "lastMessage = TRUE", false);
			} catch (InvalidDestinationException e) {
				logger.log(Logger.Level.INFO, "Got InvalidDestinationException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing Session.createSharedConsumer(Topic, String) for InvalidDestinationException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling Session.createSharedConsumer(Topic, String) for InvalidDestinationException");
				session.createSharedConsumer(invalidTopic, "InvalidDestinationException");
			} catch (InvalidDestinationException e) {
				logger.log(Logger.Level.INFO, "Got InvalidDestinationException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing Session.createSharedConsumer(Topic, String, String) for InvalidDestinationException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling Session.createSharedConsumer(Topic, String, String) for InvalidDestinationException");
				session.createSharedConsumer(invalidTopic, "InvalidDestinationException", "lastMessage = TRUE");
			} catch (InvalidDestinationException e) {
				logger.log(Logger.Level.INFO, "Got InvalidDestinationException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing Session.createSharedDurableConsumer(Topic, String) for InvalidDestinationException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling Session.createSharedDurableConsumer(Topic, String) -> expect InvalidDestinationException");
				session.createSharedDurableConsumer(invalidTopic, "InvalidDestinationException");
			} catch (InvalidDestinationException e) {
				logger.log(Logger.Level.INFO, "Got InvalidDestinationException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing Session.createSharedDurableConsumer(Topic, String, String) for InvalidDestinationException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling Session.createSharedDurableConsumer(Topic, String, String) -> expect InvalidDestinationException");
				session.createSharedDurableConsumer(invalidTopic, "InvalidDestinationException", "lastMessage = TRUE");
			} catch (InvalidDestinationException e) {
				logger.log(Logger.Level.INFO, "Got InvalidDestinationException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationException, received " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("invalidDestinationExceptionTests", e);
		}

		if (!pass) {
			throw new Exception("invalidDestinationExceptionTests failed");
		}
	}

	/*
	 * @testName: invalidSelectorExceptionTopicTests
	 *
	 * @assertion_ids: JMS:JAVADOC:1093; JMS:JAVADOC:1399; JMS:JAVADOC:1166;
	 * JMS:JAVADOC:1170;
	 *
	 * @test_Strategy: Pass an invalid selector and test for
	 * InvalidSelectorException.
	 *
	 * Session.createDurableConsumer(Topic, String, String, boolean)
	 * Session.createSharedConsumer(Topic, String, String)
	 * Session.createSharedDurableConsumer(Topic, String, String)
	 */
	@Test
	public void invalidSelectorExceptionTopicTests() throws Exception {
		boolean pass = true;
		String invalidMessageSelector = "=TEST 'test'";
		try {
			// set up JmsTool for COMMON_T setup (durable factory has clientid set)
			toolT.getDefaultConnection().close();
			toolT.closeAllResources();
			toolT2 = new JmsTool(JmsTool.COMMON_T, user, password, lookupDurableTopicFactory, mode);

			// Setup for Topic
			setupGlobalVarsT2();

			logger.log(Logger.Level.INFO,
					"Testing Session.createDurableConsumer(Topic, String, String, boolean) for InvalidSelectorException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling Session.createDurableConsumer(Topic, String, String, boolean) -> expect InvalidSelectorException");
				session.createDurableConsumer(topic, "InvalidSelectorException", invalidMessageSelector, false);
			} catch (InvalidSelectorException e) {
				logger.log(Logger.Level.INFO, "Got InvalidSelectorException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidSelectorException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing Session.createSharedConsumer(Topic, String, String) for InvalidSelectorException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling Session.createSharedConsumer(Topic, String, String) for InvalidSelectorException");
				session.createSharedConsumer(topic, "InvalidSelectorException", invalidMessageSelector);
			} catch (InvalidSelectorException e) {
				logger.log(Logger.Level.INFO, "Got InvalidSelectorException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidSelectorException, received " + e);
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Testing Session.createSharedDurableConsumer(Topic, String, String) for InvalidSelectorException");
			try {
				logger.log(Logger.Level.INFO,
						"Calling Session.createSharedDurableConsumer(Topic, String, String) -> expect InvalidSelectorException");
				session.createSharedDurableConsumer(topic, "InvalidSelectorException", invalidMessageSelector);
			} catch (InvalidSelectorException e) {
				logger.log(Logger.Level.INFO, "Got InvalidSelectorException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidSelectorException, received " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("invalidSelectorExceptionTopicTests", e);
		}

		if (!pass) {
			throw new Exception("invalidSelectorExceptionTopicTests failed");
		}
	}

	/*
	 * @testName: illegalStateExceptionTests
	 * 
	 * @assertion_ids: JMS:SPEC:185.9; JMS:SPEC:185.10; JMS:SPEC:185;
	 * JMS:JAVADOC:1349; JMS:JAVADOC:1350; JMS:JAVADOC:1390; JMS:JAVADOC:1391;
	 *
	 * @test_Strategy: Create a QueueSession and call Topic specific methods
	 * inherited from Session, and verify that jakarta.jms.IllegalStateException is
	 * thrown.
	 * 
	 * Call the following topic methods on a QueueSession:
	 * 
	 * QueueSession.createDurableConsumer(Topic, String)
	 * QueueSession.createDurableConsumer(Topic, String, String, boolean)
	 * QueueSession.createSharedConsumer(Topic, String)
	 * QueueSession.createSharedConsumer(Topic, String, String)
	 * QueueSession.createSharedDurableConsumer(Topic, String)
	 * QueueSession.createSharedDurableConsumer(Topic, String, String)
	 *
	 * Test jakarta.jms.IllegalStateException from the following API's. Also test
	 * when nolocal=true and client id is not set.
	 *
	 * Session.createDurableSubscriber(Topic, String)
	 * Session.createDurableSubscriber(Topic, String, String, boolean)
	 * Session.createDurableConsumer(Topic, String)
	 * Session.createDurableConsumer(Topic, String, String, boolean)
	 */
	@Test
	public void illegalStateExceptionTests() throws Exception {
		JmsTool toolq = null;
		String lookupNormalTopicFactory = "MyTopicConnectionFactory";
		boolean pass = true;
		MessageConsumer consumer = null;
		TopicSubscriber subscriber = null;
		try {
			// Set up JmsTool for Queue setup
			toolq = new JmsTool(JmsTool.QUEUE, user, password, mode);

			Destination destination = toolT.getDefaultDestination();
			Topic topic = (Topic) destination;
			try {
				logger.log(Logger.Level.INFO, "Calling QueueSession.createDurableConsumer(Topic, String)");
				toolq.getDefaultQueueSession().createDurableConsumer(topic, "mySub1");
				pass = false;
				logger.log(Logger.Level.ERROR, "QueueSession.createDurableConsumer(Topic, String) "
						+ "didn't throw expected IllegalStateException.");
			} catch (jakarta.jms.IllegalStateException ex) {
				logger.log(Logger.Level.INFO, "Got expected IllegalStateException from "
						+ "QueueSession.createDurableConsumer(Topic, String)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			try {
				logger.log(Logger.Level.INFO,
						"Calling QueueSession.createDurableConsumer(Topic, String, String, boolean)");
				toolq.getDefaultQueueSession().createDurableConsumer(topic, "mySub1", "TEST = 'test'", false);
				pass = false;
				logger.log(Logger.Level.ERROR, "QueueSession.createDurableConsumer(Topic, String, String, boolean) "
						+ "didn't throw expected IllegalStateException.");
			} catch (jakarta.jms.IllegalStateException ex) {
				logger.log(Logger.Level.INFO, "Got expected IllegalStateException from "
						+ "QueueSession.createDurableConsumer(Topic, String, String, boolean)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			try {
				logger.log(Logger.Level.INFO, "Calling QueueSession.createSharedConsumer(Topic, String)");
				toolq.getDefaultQueueSession().createSharedConsumer(topic, "mySub1");
				pass = false;
				logger.log(Logger.Level.ERROR, "QueueSession.createSharedConsumer(Topic, String) "
						+ "didn't throw expected IllegalStateException.");
			} catch (jakarta.jms.IllegalStateException ex) {
				logger.log(Logger.Level.INFO, "Got expected IllegalStateException from "
						+ "QueueSession.createSharedConsumer(Topic, String)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			try {
				logger.log(Logger.Level.INFO, "Calling QueueSession.createSharedConsumer(Topic, String, String)");
				toolq.getDefaultQueueSession().createSharedConsumer(topic, "mySub1", "TEST = 'test'");
				pass = false;
				logger.log(Logger.Level.ERROR, "QueueSession.createSharedConsumer(Topic, String, String) "
						+ "didn't throw expected IllegalStateException.");
			} catch (jakarta.jms.IllegalStateException ex) {
				logger.log(Logger.Level.INFO, "Got expected IllegalStateException from "
						+ "QueueSession.createSharedConsumer(Topic, String, String)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			try {
				logger.log(Logger.Level.INFO, "Calling QueueSession.createSharedDurableConsumer(Topic, String)");
				toolq.getDefaultQueueSession().createSharedDurableConsumer(topic, "mySub1");
				pass = false;
				logger.log(Logger.Level.ERROR, "QueueSession.createSharedDurableConsumer(Topic, String) "
						+ "didn't throw expected IllegalStateException.");
			} catch (jakarta.jms.IllegalStateException ex) {
				logger.log(Logger.Level.INFO, "Got expected IllegalStateException from "
						+ "QueueSession.createSharedDurableConsumer(Topic, String)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			try {
				logger.log(Logger.Level.INFO,
						"Calling QueueSession.createSharedDurableConsumer(Topic, String, String)");
				toolq.getDefaultQueueSession().createSharedDurableConsumer(topic, "mySub1", "TEST = 'test'");
				pass = false;
				logger.log(Logger.Level.ERROR, "QueueSession.createSharedDurableConsumer(Topic, String, String) "
						+ "didn't throw expected IllegalStateException.");
			} catch (jakarta.jms.IllegalStateException ex) {
				logger.log(Logger.Level.INFO, "Got expected IllegalStateException from "
						+ "QueueSession.createSharedDurableConsumer(Topic, String, String)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			try {
				logger.log(Logger.Level.INFO,
						"Create a Durable Subscriber with clientid unset (expect IllegalStateException");
				logger.log(Logger.Level.INFO, "Calling Session.createDurableSubscriber(Topic, String)");
				subscriber = toolT.getDefaultSession().createDurableSubscriber(topic, "mySub1");
				pass = false;
				logger.log(Logger.Level.ERROR, "Session.createDurableSubscriber(Topic, String) "
						+ "didn't throw expected IllegalStateException.");
				try {
					subscriber.close();
					toolT.getDefaultSession().unsubscribe("mySub1");
				} catch (Exception e) {
				}
			} catch (jakarta.jms.IllegalStateException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected IllegalStateException from " + "Session.createDurableSubscriber(Topic, String)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			try {
				logger.log(Logger.Level.INFO,
						"Create a Durable Subscriber with noLocal=true and clientid unset (expect IllegalStateException");
				logger.log(Logger.Level.INFO,
						"Calling Session.createDurableSubscriber(Topic, String, String, boolean)");
				subscriber = toolT.getDefaultSession().createDurableSubscriber(topic, "mySub1", "TEST = 'test'", true);
				pass = false;
				logger.log(Logger.Level.ERROR, "Session.createDurableSubscriber(Topic, String, String, boolean) "
						+ "didn't throw expected IllegalStateException.");
				try {
					subscriber.close();
					toolT.getDefaultSession().unsubscribe("mySub1");
				} catch (Exception e) {
				}
			} catch (jakarta.jms.IllegalStateException ex) {
				logger.log(Logger.Level.INFO, "Got expected IllegalStateException from "
						+ "Session.createDurableSubscriber(Topic, String, String, boolean)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			try {
				logger.log(Logger.Level.INFO,
						"Create a Durable Consumer with clientid unset (expect IllegalStateException");
				logger.log(Logger.Level.INFO, "Calling Session.createDurableConsumer(Topic, String)");
				consumer = toolT.getDefaultSession().createDurableConsumer(topic, "mySub1");
				pass = false;
				logger.log(Logger.Level.ERROR, "Session.createDurableConsumer(Topic, String) "
						+ "didn't throw expected IllegalStateException.");
				try {
					consumer.close();
					toolT.getDefaultSession().unsubscribe("mySub1");
				} catch (Exception e) {
				}
			} catch (jakarta.jms.IllegalStateException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected IllegalStateException from " + "Session.createDurableConsumer(Topic, String)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}

			try {
				logger.log(Logger.Level.INFO,
						"Create a Durable Consumer with noLocal=true and clientid unset (expect IllegalStateException");
				logger.log(Logger.Level.INFO, "Calling Session.createDurableConsumer(Topic, String, String, boolean)");
				consumer = toolT.getDefaultSession().createDurableConsumer(topic, "mySub1", "TEST = 'test'", true);
				pass = false;
				logger.log(Logger.Level.ERROR, "Session.createDurableConsumer(Topic, String, String, boolean) "
						+ "didn't throw expected IllegalStateException.");
				try {
					consumer.close();
					toolT.getDefaultSession().unsubscribe("mySub1");
				} catch (Exception e) {
				}
			} catch (jakarta.jms.IllegalStateException ex) {
				logger.log(Logger.Level.INFO, "Got expected IllegalStateException from "
						+ "Session.createDurableConsumer(Topic, String, String, boolean)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("illegalStateExceptionTests Failed");
		} finally {
			try {
				toolq.getDefaultQueueConnection().close();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			}
		}

		if (!pass) {
			throw new Exception("illegalStateExceptionTests Failed");
		}
	}

	/*
	 * @testName: jMSExceptionTests
	 * 
	 * @assertion_ids: JMS:JAVADOC:1091; JMS:JAVADOC:257; JMS:JAVADOC:1090;
	 * JMS:JAVADOC:1164; JMS:JAVADOC:1168; JMS:JAVADOC:1394; JMS:JAVADOC:1397;
	 *
	 * @test_Strategy: Test JMSException conditions from various API methods.
	 *
	 * TopicSession.createDurableSubscriber(Topic, String)
	 * TopicSession.createDurableSubscriber(Topic, String. String, boolean)
	 * Session.createDurableSubscriber(Topic, String)
	 * Session.createDurableSubscriber(Topic, String. String, boolean)
	 * Session.createDurableConsumer(Topic, String)
	 * Session.createDurableConsumer(Topic, String. String, boolean)
	 * Session.createSharedConsumer(Topic, String)
	 * Session.createSharedConsumer(Topic, String, String)
	 * Session.createSharedDurableConsumer(Topic, String)
	 * Session.createSharedDurableConsumer(Topic, String. String)
	 */
	@Test
	public void jMSExceptionTests() throws Exception {
		boolean pass = true;
		JmsTool toolt = null;
		Topic mytopic = null;
		try {
			// Setup for Topic
			setupGlobalVarsT();

			// Create second Topic
			mytopic = (Topic) toolT.createNewTopic("MY_TOPIC2");

			// set up JmsTool for TOPIC setup (normal factory no clientid set)
			toolt = new JmsTool(JmsTool.TOPIC, user, password, lookupNormalTopicFactory, mode);

			try {
				logger.log(Logger.Level.INFO,
						"Create a Durable TopicSubscriber with noLocal=true and clientid unset (expect JMSException");
				logger.log(Logger.Level.INFO,
						"Calling TopicSession.createDurableSubscriber(Topic, String, String, boolean)");
				subscriber = toolt.getDefaultTopicSession().createDurableSubscriber(topic, "mySub1", "TEST = 'test'",
						true);
				pass = false;
				logger.log(Logger.Level.ERROR, "TopicSession.createDurableSubscriber(Topic, String, String, boolean) "
						+ "didn't throw expected JMSException.");
			} catch (jakarta.jms.JMSException ex) {
				logger.log(Logger.Level.INFO, "Got expected JMSException from "
						+ "TopicSession.createDurableSubscriber(Topic, String, String, boolean)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			} finally {
				try {
					if (subscriber != null)
						subscriber.close();
					toolt.getDefaultTopicConnection().close();
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
					pass = false;
				}
			}

			try {
				logger.log(Logger.Level.INFO,
						"Create a Durable Consumer with noLocal=true and clientid unset (expect JMSException");
				logger.log(Logger.Level.INFO, "Calling Session.createDurableConsumer(Topic, String, String, boolean)");
				consumer = null;
				consumer = session.createDurableConsumer(topic, "mySub1", "TEST = 'test'", true);
				pass = false;
				logger.log(Logger.Level.ERROR, "Session.createDurableConsumer(Topic, String, String, boolean) "
						+ "didn't throw expected JMSException.");
			} catch (jakarta.jms.JMSException ex) {
				logger.log(Logger.Level.INFO, "Got expected JMSException from "
						+ "Session.createDurableConsumer(Topic, String, String, boolean)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			} finally {
				try {
					if (consumer != null)
						consumer.close();
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
					pass = false;
				}
			}

			try {
				logger.log(Logger.Level.INFO,
						"Create a Durable TopicSubscriber with noLocal=true and clientid unset (expect JMSException");
				logger.log(Logger.Level.INFO,
						"Calling Session.createDurableSubscriber(Topic, String, String, boolean)");
				subscriber = null;
				subscriber = session.createDurableSubscriber(topic, "mySub1", "TEST = 'test'", true);
				pass = false;
				logger.log(Logger.Level.ERROR, "Session.createDurableSubscriber(Topic, String, String, boolean) "
						+ "didn't throw expected JMSException.");
			} catch (jakarta.jms.JMSException ex) {
				logger.log(Logger.Level.INFO, "Got expected JMSException from "
						+ "Session.createDurableSubscriber(Topic, String, String, boolean)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
				pass = false;
			} finally {
				try {
					if (subscriber != null)
						subscriber.close();
				} catch (Exception e) {
					logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
					pass = false;
				}
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("jMSExceptionTests Failed");
		}

		try {
			// Setup for Durable Topic
			toolT.getDefaultConnection().close();
			toolT.closeAllResources();
			// set up JmsTool for COMMON_T setup (durable factory has clientid set)
			toolT2 = new JmsTool(JmsTool.COMMON_T, user, password, lookupDurableTopicFactory, mode);
			setupGlobalVarsT2();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("jMSExceptionTests Failed");
		}

		// Create durable subscriber specifying topic and name
		// Create second durable subscriber with same name but specifying different
		// topic,
		// Verify JMSException is thrown.
		try {
			logger.log(Logger.Level.INFO, "Create durable subscriber");
			logger.log(Logger.Level.INFO, "Calling Session.createDurableSubscriber(Topic, String)");
			subscriber = subscriber2 = null;
			subscriber = session.createDurableSubscriber(topic, "dummySubSCJMSException");
			logger.log(Logger.Level.INFO, "Create second durable subscriber with same name but different topic");
			logger.log(Logger.Level.INFO, "Calling Session.createDurableSubscriber(Topic, String)");
			subscriber2 = session.createDurableSubscriber(mytopic, "dummySubSCJMSException");
			logger.log(Logger.Level.INFO, "Verify that JMSException is thrown");
			pass = false;
			logger.log(Logger.Level.ERROR, "Didn't throw expected JMSException");
		} catch (JMSException ex) {
			logger.log(Logger.Level.INFO,
					"Got expected JMSException from " + "Session.createDurableSubscriber(Topic, String)");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			pass = false;
		} finally {
			try {
				if (subscriber != null)
					subscriber.close();
				if (subscriber2 != null)
					subscriber2.close();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			}
		}

		// Create durable subscriber specifying topic, name, message selector and
		// nolcal
		// Create second durable subscriber with same name but specifying different
		// topic
		// message selector, or nolocal. Verify JMSException is thrown.
		try {
			logger.log(Logger.Level.INFO, "Create durable subscriber");
			logger.log(Logger.Level.INFO, "Calling Session.createDurableSubscriber(Topic, String, String, boolean)");
			subscriber = subscriber2 = null;
			subscriber = session.createDurableSubscriber(topic, "dummySubSCJMSException", "TEST = 'test'", false);
			logger.log(Logger.Level.INFO, "Create second durable subscriber with same name but different topic");
			logger.log(Logger.Level.INFO, "Calling Session.createDurableSubscriber(Topic, String, String, boolean)");
			subscriber2 = session.createDurableSubscriber(mytopic, "dummySubSCJMSException", "TEST = 'test'", false);
			logger.log(Logger.Level.INFO, "Verify that JMSException is thrown");
			pass = false;
			logger.log(Logger.Level.ERROR, "Didn't throw expected JMSException");
		} catch (JMSException ex) {
			logger.log(Logger.Level.INFO, "Got expected JMSException from "
					+ "Session.createDurableSubscriber(Topic, String, String, boolean)");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			pass = false;
		} finally {
			try {
				if (subscriber != null)
					subscriber.close();
				if (subscriber2 != null)
					subscriber2.close();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			}
		}

		// Create shared consumer specifying topic and name
		// Create second shared consumer with same name but specifying different
		// topic,
		// Verify JMSException is thrown.
		try {
			logger.log(Logger.Level.INFO, "Create shared consumer");
			logger.log(Logger.Level.INFO, "Calling Session.createSharedConsumer(Topic, String)");
			consumer = consumer2 = null;
			consumer = session.createSharedConsumer(topic, "dummySubSCJMSException");
			logger.log(Logger.Level.INFO, "Create second shared consumer with same name but different topic");
			logger.log(Logger.Level.INFO, "Calling Session.createSharedConsumer(Topic, String)");
			consumer2 = session.createSharedConsumer(mytopic, "dummySubSCJMSException");
			logger.log(Logger.Level.INFO, "Verify that JMSException is thrown");
			pass = false;
			logger.log(Logger.Level.ERROR, "Didn't throw expected JMSException");
		} catch (JMSException ex) {
			logger.log(Logger.Level.INFO,
					"Got expected JMSException from " + "Session.createSharedConsumer(Topic, String)");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			pass = false;
		} finally {
			try {
				if (consumer != null)
					consumer.close();
				if (consumer2 != null)
					consumer2.close();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			}
		}

		// Create shared consumer specifying topic and name and message selector
		// Create second shared consumer with same name but specifying different
		// topic and message selector
		// Verify JMSException is thrown.
		try {
			logger.log(Logger.Level.INFO, "Create shared consumer");
			logger.log(Logger.Level.INFO, "Calling Session.createSharedConsumer(Topic, String, String)");
			consumer = consumer2 = null;
			consumer = session.createSharedConsumer(topic, "dummySubSCJMSException", "TEST = 'test'");
			logger.log(Logger.Level.INFO, "Create second shared consumer with same name but different topic");
			logger.log(Logger.Level.INFO, "Calling Session.createSharedConsumer(Topic, String, String)");
			consumer2 = session.createSharedConsumer(mytopic, "dummySubSCJMSException", "TEST = 'test'");
			logger.log(Logger.Level.INFO, "Verify that JMSException is thrown");
			pass = false;
			logger.log(Logger.Level.ERROR, "Didn't throw expected JMSException");
		} catch (JMSException ex) {
			logger.log(Logger.Level.INFO,
					"Got expected JMSException from " + "Session.createSharedConsumer(Topic, String, String)");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			pass = false;
		} finally {
			try {
				if (consumer != null)
					consumer.close();
				if (consumer2 != null)
					consumer2.close();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			}
		}

		// Create shared durable subscription specifying topic, name. selector.
		// Create second shared durable subscription with same name but specifying
		// different topic,
		// selector. Verify JMSException is thrown.
		try {
			logger.log(Logger.Level.INFO, "Create shared durable subscription");
			logger.log(Logger.Level.INFO, "Calling Session.createSharedDurableConsumer(Topic, String, String)");
			consumer = consumer2 = null;
			consumer = session.createSharedDurableConsumer(topic, "dummySubSJMSException", "TEST = 'test'");
			logger.log(Logger.Level.INFO,
					"Create second shared durable subscription with same name but different other args");
			logger.log(Logger.Level.INFO, "Calling Session.createSharedDurableConsumer(Topic, String, String)");
			consumer2 = session.createSharedDurableConsumer(mytopic, "dummySubSJMSException", "TEST = 'test2'");
			logger.log(Logger.Level.INFO, "Verify that JMSException is thrown");
			pass = false;
			logger.log(Logger.Level.ERROR, "Didn't throw expected JMSException");
		} catch (JMSException ex) {
			logger.log(Logger.Level.INFO,
					"Got expected JMSException from " + "Session.createSharedDurableConsumer(Topic, String, String)");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			pass = false;
		} finally {
			try {
				if (consumer2 != null)
					consumer2.close();
				cleanupSubscription(consumer, session, "dummySubSJMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			}
		}

		// Create durable subscription specifying topic and name
		// Create second durable subscription with same name but specifying
		// different topic,
		// Verify JMSException is thrown.
		try {
			logger.log(Logger.Level.INFO, "Create durable subscription");
			logger.log(Logger.Level.INFO, "Calling Session.createDurableConsumer(Topic, String)");
			consumer = consumer2 = null;
			consumer = session.createDurableConsumer(topic, "dummySubDJMSException");
			logger.log(Logger.Level.INFO, "Create second durable subscription with same name but different topic");
			logger.log(Logger.Level.INFO, "Calling Session.createDurableConsumer(Topic, String)");
			consumer2 = session.createDurableConsumer(mytopic, "dummySubDJMSException");
			logger.log(Logger.Level.INFO, "Verify that JMSException is thrown");
			pass = false;
			logger.log(Logger.Level.ERROR, "Didn't throw expected JMSException");
		} catch (JMSException ex) {
			logger.log(Logger.Level.INFO,
					"Got expected JMSException from " + "Session.createDurableConsumer(Topic, String)");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			pass = false;
		} finally {
			try {
				if (consumer2 != null)
					consumer2.close();
				cleanupSubscription(consumer, session, "dummySubDJMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			}
		}

		// Create shared durable subscription specifying topic and name
		// Create second shared durable subscription with same name but specifying
		// different topic,
		// Verify JMSException is thrown.
		try {
			logger.log(Logger.Level.INFO, "Create shared durable subscription");
			logger.log(Logger.Level.INFO, "Calling Session.createSharedDurableConsumer(Topic, String)");
			consumer = consumer2 = null;
			consumer = session.createSharedDurableConsumer(topic, "dummySubSDJMSException");
			logger.log(Logger.Level.INFO,
					"Create second shared durable subscription with same name but different topic");
			logger.log(Logger.Level.INFO, "Calling Session.createSharedDurableConsumer(Topic, String)");
			consumer2 = session.createSharedDurableConsumer(mytopic, "dummySubSDJMSException");
			logger.log(Logger.Level.INFO, "Verify that JMSException is thrown");
			pass = false;
			logger.log(Logger.Level.ERROR, "Didn't throw expected JMSException");
		} catch (JMSException ex) {
			logger.log(Logger.Level.INFO,
					"Got expected JMSException from " + "Session.createSharedDurableConsumer(Topic, String)");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			pass = false;
		} finally {
			try {
				if (consumer2 != null)
					consumer2.close();
				cleanupSubscription(consumer, session, "dummySubSDJMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			}
		}

		// Create durable subscription specifying topic, name. selector, and nolocal
		// value.
		// Create second durable subscription with same name but specifying
		// different topic,
		// selector, or nolocal value. Verify JMSException is thrown.
		try {
			logger.log(Logger.Level.INFO, "Create durable subscription");
			logger.log(Logger.Level.INFO, "Calling Session.createDurableConsumer(Topic, String, String, boolean)");
			consumer = consumer2 = null;
			consumer = session.createDurableConsumer(topic, "dummySubDJMSException", "TEST = 'test'", false);
			logger.log(Logger.Level.INFO, "Create second durable subscription with same name but different other args");
			logger.log(Logger.Level.INFO, "Calling Session.createDurableConsumer(Topic, String, String, boolean)");
			consumer2 = session.createDurableConsumer(mytopic, "dummySubDJMSException", "TEST = 'test2'", false);
			logger.log(Logger.Level.INFO, "Verify that JMSException is thrown");
			pass = false;
			logger.log(Logger.Level.ERROR, "Didn't throw expected JMSException");
		} catch (JMSException ex) {
			logger.log(Logger.Level.INFO, "Got expected JMSException from "
					+ "Session.createDurableConsumer(Topic, String, String, boolean)");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			pass = false;
		} finally {
			try {
				if (consumer2 != null)
					consumer2.close();
				cleanupSubscription(consumer, session, "dummySubDJMSException");
				toolT.getDefaultConnection().close();
				toolT.closeAllResources();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			}
		}

		if (!pass) {
			throw new Exception("jMSExceptionTests Failed");
		}
	}

	/*
	 * @testName: sendAndRecvMsgsOfEachMsgTypeQueueTest
	 *
	 * @assertion_ids: JMS:JAVADOC:209; JMS:JAVADOC:212; JMS:JAVADOC:213;
	 * JMS:JAVADOC:215; JMS:JAVADOC:217; JMS:JAVADOC:219; JMS:JAVADOC:221;
	 * JMS:JAVADOC:223; JMS:JAVADOC:242; JMS:JAVADOC:317; JMS:JAVADOC:504;
	 * JMS:JAVADOC:510;
	 *
	 * @test_Strategy: Send and receive messages of each message type: Message,
	 * BytesMessage, MapMessage, ObjectMessage, StreamMessage, TextMessage. Tests
	 * the following API's
	 *
	 * ConnectionFactory.createConnection(String, String)
	 * Connection.createSession(boolean, int) Session.createMessage()
	 * Session.createBytesMessage() Session.createMapMessage()
	 * Session.createObjectMessage() Session.createObjectMessage(Serializable
	 * object) Session.createStreamMessage() Session.createTextMessage()
	 * Session.createTextMessage(String) Session.createConsumer(Destination)
	 * Session.createProducer(Destination) MessageProducer.send(Message)
	 * MessageConsumer.receive(long timeout)
	 */
	@Test
	public void sendAndRecvMsgsOfEachMsgTypeQueueTest() throws Exception {
		boolean pass = true;
		try {
			// Setup for Queue
			setupGlobalVarsQ();

			// send and receive Message
			logger.log(Logger.Level.INFO, "Send Message");
			Message msg = session.createMessage();
			logger.log(Logger.Level.INFO, "Set some values in Message");
			msg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgsOfEachMsgTypeQueueTest");
			msg.setBooleanProperty("booleanProperty", true);
			producer.send(msg);
			logger.log(Logger.Level.INFO, "Receive Message");
			Message msgRecv = consumer.receive(timeout);
			if (msgRecv == null) {
				throw new Exception("Did not receive Message");
			}
			logger.log(Logger.Level.INFO, "Check the values in Message");
			if (msgRecv.getBooleanProperty("booleanProperty") == true) {
				logger.log(Logger.Level.INFO, "booleanproperty is correct");
			} else {
				logger.log(Logger.Level.INFO, "booleanproperty is incorrect");
				pass = false;
			}

			// send and receive BytesMessage
			logger.log(Logger.Level.INFO, "Send BytesMessage");
			BytesMessage bMsg = session.createBytesMessage();
			logger.log(Logger.Level.INFO, "Set some values in BytesMessage");
			bMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgsOfEachMsgTypeQueueTest");
			bMsg.writeByte((byte) 1);
			bMsg.writeInt((int) 22);
			producer.send(bMsg);
			logger.log(Logger.Level.INFO, "Receive BytesMessage");
			BytesMessage bMsgRecv = (BytesMessage) consumer.receive(timeout);
			if (bMsgRecv == null) {
				throw new Exception("Did not receive BytesMessage");
			}
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

			// send and receive MapMessage
			logger.log(Logger.Level.INFO, "Send MapMessage");
			MapMessage mMsg = session.createMapMessage();
			logger.log(Logger.Level.INFO, "Set some values in MapMessage");
			mMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgsOfEachMsgTypeQueueTest");
			mMsg.setBoolean("booleanvalue", true);
			mMsg.setInt("intvalue", (int) 10);
			producer.send(mMsg);
			logger.log(Logger.Level.INFO, "Receive MapMessage");
			MapMessage mMsgRecv = (MapMessage) consumer.receive(timeout);
			if (mMsgRecv == null) {
				throw new Exception("Did not receive MapMessage");
			}
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

			// send and receive ObjectMessage
			logger.log(Logger.Level.INFO, "Send ObjectMessage");
			StringBuffer sb1 = new StringBuffer("This is a StringBuffer");
			logger.log(Logger.Level.INFO, "Set some values in ObjectMessage");
			ObjectMessage oMsg = session.createObjectMessage();
			oMsg.setObject(sb1);
			oMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgsOfEachMsgTypeQueueTest");
			producer.send(oMsg);
			logger.log(Logger.Level.INFO, "Receive ObjectMessage");
			ObjectMessage oMsgRecv = (ObjectMessage) consumer.receive(timeout);
			if (oMsgRecv == null) {
				throw new Exception("Did not receive ObjectMessage");
			}
			logger.log(Logger.Level.INFO, "Check the value in ObjectMessage");
			StringBuffer sb2 = (StringBuffer) oMsgRecv.getObject();
			if (sb2.toString().equals(sb1.toString())) {
				logger.log(Logger.Level.INFO, "objectvalue is correct");
			} else {
				logger.log(Logger.Level.ERROR, "objectvalue is incorrect");
				pass = false;
			}

			// send and receive ObjectMessage passing object as param
			logger.log(Logger.Level.INFO, "Send ObjectMessage passing object as param");
			sb1 = new StringBuffer("This is a StringBuffer");
			logger.log(Logger.Level.INFO, "Set some values in ObjectMessage passing object as param");
			oMsg = session.createObjectMessage(sb1);
			oMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgsOfEachMsgTypeQueueTest");
			producer.send(oMsg);
			logger.log(Logger.Level.INFO, "Receive ObjectMessage");
			oMsgRecv = (ObjectMessage) consumer.receive(timeout);
			if (oMsgRecv == null) {
				throw new Exception("Did not receive ObjectMessage");
			}
			logger.log(Logger.Level.INFO, "Check the value in ObjectMessage");
			sb2 = (StringBuffer) oMsgRecv.getObject();
			if (sb2.toString().equals(sb1.toString())) {
				logger.log(Logger.Level.INFO, "objectvalue is correct");
			} else {
				logger.log(Logger.Level.ERROR, "objectvalue is incorrect");
				pass = false;
			}

			// send and receive StreamMessage
			logger.log(Logger.Level.INFO, "Send StreamMessage");
			StreamMessage sMsg = session.createStreamMessage();
			logger.log(Logger.Level.INFO, "Set some values in StreamMessage");
			sMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgsOfEachMsgTypeQueueTest");
			sMsg.writeBoolean(true);
			sMsg.writeInt((int) 22);
			producer.send(sMsg);
			logger.log(Logger.Level.INFO, "Receive StreamMessage");
			StreamMessage sMsgRecv = (StreamMessage) consumer.receive(timeout);
			if (sMsgRecv == null) {
				throw new Exception("Did not receive StreamMessage");
			}
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

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Send TextMessage");
			TextMessage tMsg = session.createTextMessage();
			logger.log(Logger.Level.INFO, "Set some values in MapMessage");
			tMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgsOfEachMsgTypeQueueTest");
			tMsg.setText("Hello There!");
			producer.send(tMsg);
			logger.log(Logger.Level.INFO, "Receive TextMessage");
			TextMessage tMsgRecv = (TextMessage) consumer.receive(timeout);
			if (tMsgRecv == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the value in TextMessage");
			if (tMsgRecv.getText().equals("Hello There!")) {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR, "TextMessage is incorrect");
				pass = false;
			}

			// send and receive TextMessage passing string as param
			logger.log(Logger.Level.INFO, "Send TextMessage");
			tMsg = session.createTextMessage("Where are you!");
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			tMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "sendAndRecvMsgsOfEachMsgTypeQueueTest");
			producer.send(tMsg);
			logger.log(Logger.Level.INFO, "Receive TextMessage");
			tMsgRecv = (TextMessage) consumer.receive(timeout);
			if (tMsgRecv == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the value in TextMessage");
			if (tMsgRecv.getText().equals("Where are you!")) {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR, "TextMessage is incorrect");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			e.printStackTrace();
			throw new Exception("sendAndRecvMsgsOfEachMsgTypeQueueTest", e);
		}

		if (!pass) {
			throw new Exception("sendAndRecvMsgsOfEachMsgTypeQueueTest failed");
		}
	}

	/*
	 * @testName: createTemporayQueueTest
	 *
	 * @assertion_ids: JMS:JAVADOC:194;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * Session.createTemporaryQueue().
	 *
	 * Send and receive a TextMessage to temporary queue. Compare send and recv
	 * message for equality.
	 */
	@Test
	public void createTemporayQueueTest() throws Exception {
		boolean pass = true;
		MessageConsumer tConsumer = null;
		try {

			String message = "a text message";

			// Setup for Queue
			setupGlobalVarsQ();

			// create a TemporaryQueue
			logger.log(Logger.Level.INFO, "Creating TemporaryQueue");
			TemporaryQueue tempQueue = session.createTemporaryQueue();

			// Create a MessageConsumer for this Temporary Queue
			logger.log(Logger.Level.INFO, "Creating MessageConsumer");
			tConsumer = session.createConsumer(tempQueue);

			// Create a MessageProducer for this Temporary Queue
			logger.log(Logger.Level.INFO, "Creating MessageProducer");
			MessageProducer tProducer = session.createProducer(tempQueue);

			// Send TextMessage to temporary queue
			logger.log(Logger.Level.INFO, "Creating TextMessage with text [" + message + "]");
			TextMessage tMsg = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			tMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "createTemporayQueueTest");
			logger.log(Logger.Level.INFO, "Send TextMessage to temporaty queue");
			tProducer.send(tMsg);

			// Receive TextMessage from temporary queue
			logger.log(Logger.Level.INFO, "Receive TextMessage from temporaty queue");
			TextMessage tMsgRecv = null;
			if (tConsumer != null)
				tMsgRecv = (TextMessage) tConsumer.receive(timeout);
			if (tMsgRecv == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the value in TextMessage");
			if (tMsgRecv.getText().equals(message)) {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			} else {
				logger.log(Logger.Level.ERROR, "TextMessage is incorrect");
				pass = false;
			}

			logger.log(Logger.Level.INFO,
					"Attempting to delete temporary queue with an open consumer should not be allowed");
			try {
				tempQueue.delete();
				pass = false;
				logger.log(Logger.Level.ERROR, "TemporaryQueue.delete() didn't throw expected Exception");
			} catch (JMSException em) {
				logger.log(Logger.Level.INFO, "Received expected JMSException: ");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Received unexpected JMSException: " + e);
				pass = false;
			}

			try {
				if (tConsumer != null)
					tConsumer.close();
			} catch (Exception e) {
			}

			logger.log(Logger.Level.INFO,
					"Attempting to delete temporary queue with no open consumer should be allowed");
			try {
				tempQueue.delete();
			} catch (Exception e) {
				pass = false;
				logger.log(Logger.Level.ERROR, "Received unexpected Exception: ", e);
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			e.printStackTrace();
			throw new Exception("createTemporayQueueTest");
		}

		if (!pass) {
			throw new Exception("createTemporayQueueTest failed");
		}
	}

	/*
	 * @testName: createQueueBrowserTest
	 *
	 * @assertion_ids: JMS:JAVADOC:258; JMS:JAVADOC:260;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * Session.createBrowser(Queue) Session.createBrowser(Queue, String)
	 * 
	 * 1. Send x text messages to a Queue. 2. Create a QueueBrowser with selector to
	 * browse just the last message in the Queue. 3. Create a QueueBrowser again to
	 * browse all the messages in the queue.
	 */
	@Test
	public void createQueueBrowserTest() throws Exception {
		boolean pass = true;
		QueueBrowser qBrowser = null;
		try {
			TextMessage tempMsg = null;
			Enumeration msgs = null;

			// Setup for Queue
			setupGlobalVarsQ();
			consumer.close();

			// send "numMessages" messages to Queue plus end of stream message
			logger.log(Logger.Level.INFO, "Send " + numMessages + " to Queue");
			for (int i = 1; i <= numMessages; i++) {
				tempMsg = session.createTextMessage("Message " + i);
				tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "createQueueBrowserTest" + i);
				if (i == numMessages) {
					tempMsg.setBooleanProperty("lastMessage", true);
				} else {
					tempMsg.setBooleanProperty("lastMessage", false);
				}
				producer.send(tempMsg);
				logger.log(Logger.Level.INFO, "Message " + i + " sent");
			}

			// create QueueBrowser to peek at last message in Queue using message
			// selector
			logger.log(Logger.Level.INFO,
					"Create QueueBrowser to peek at last message in Queue using message selector");
			qBrowser = session.createBrowser(queue, "lastMessage = TRUE");

			// check that browser just has the last message in the Queue
			logger.log(Logger.Level.INFO, "Check that browser has just the last message");
			int msgCount = 0;
			msgs = qBrowser.getEnumeration();
			while (msgs.hasMoreElements()) {
				tempMsg = (TextMessage) msgs.nextElement();
				if (!tempMsg.getText().equals("Message " + numMessages)) {
					logger.log(Logger.Level.ERROR, "Found [" + tempMsg.getText() + "] in browser expected [Message 3]");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Found correct [Message 3] in browser");
				}
				msgCount++;
			}
			if (msgCount != 1) {
				logger.log(Logger.Level.ERROR, "Found " + msgCount + " messages in browser expected 1");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Found 1 message in browser (correct)");
			}
			qBrowser.close();

			// create QueueBrowser to peek at all messages in the Queue
			logger.log(Logger.Level.INFO, "Create QueueBrowser to browse all messages in the Queue");
			qBrowser = session.createBrowser(queue);

			// check for messages
			logger.log(Logger.Level.INFO, "Check that browser contains all " + numMessages + " messages");
			msgCount = 0;
			int msgIndex = 1;
			msgs = qBrowser.getEnumeration();
			while (msgs.hasMoreElements()) {
				tempMsg = (TextMessage) msgs.nextElement();
				if (!tempMsg.getText().equals("Message " + msgIndex)) {
					logger.log(Logger.Level.ERROR,
							"Found [" + tempMsg.getText() + "] in browser expected [Message " + msgIndex + "]");
					pass = false;
				}
				msgCount++;
				msgIndex++;
			}
			if (msgCount != numMessages) {
				logger.log(Logger.Level.ERROR, "Found " + msgCount + " messages in browser expected " + numMessages);
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Found " + numMessages + " messages in browser (correct)");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			e.printStackTrace();
			throw new Exception("createQueueBrowserTest");
		} finally {
			try {
				if (qBrowser != null)
					qBrowser.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("createQueueBrowserTest failed");
		}
	}

	/*
	 * @testName: getTransactedQueueTest
	 *
	 * @assertion_ids: JMS:JAVADOC:225;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * Session.getTransacted().
	 */
	@Test
	public void getTransactedQueueTest() throws Exception {
		boolean pass = true;

		// Test for transacted mode false
		try {
			// Setup for Queue
			setupGlobalVarsQ();
			session.close();

			session = connection.createSession(Session.AUTO_ACKNOWLEDGE);
			boolean expTransacted = false;
			logger.log(Logger.Level.INFO, "Calling getTransacted and expect " + expTransacted + " to be returned");
			boolean actTransacted = session.getTransacted();
			if (actTransacted != expTransacted) {
				logger.log(Logger.Level.ERROR,
						"getTransacted() returned " + actTransacted + ", expected " + expTransacted);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("getTransactedQueueTest", e);
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception e) {
			}
		}

		// Test for transacted mode true
		if ((vehicle.equals("appclient") || vehicle.equals("standalone"))) {
			try {
				session = connection.createSession(Session.SESSION_TRANSACTED);
				boolean expTransacted = true;
				logger.log(Logger.Level.INFO, "Calling getTransacted and expect " + expTransacted + " to be returned");
				boolean actTransacted = session.getTransacted();
				if (actTransacted != expTransacted) {
					logger.log(Logger.Level.ERROR,
							"getTransacted() returned " + actTransacted + ", expected " + expTransacted);
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught exception: " + e);
				throw new Exception("getTransactedQueueTest", e);
			} finally {
				try {
					if (session != null)
						session.close();
				} catch (Exception e) {
				}
			}
		}

		if (!pass) {
			throw new Exception("getTransactedQueueTest failed");
		}
	}

	/*
	 * @testName: getAcknowledgeModeQueueTest
	 *
	 * @assertion_ids: JMS:JAVADOC:227;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * Session.getAcknowledgeMode().
	 */
	@Test
	public void getAcknowledgeModeQueueTest() throws Exception {
		boolean pass = true;

		// Test for AUTO_ACKNOWLEDGE mode
		try {
			// Setup for Queue
			setupGlobalVarsQ();
			session.close();

			session = connection.createSession(Session.AUTO_ACKNOWLEDGE);
			int expAcknowledgeMode = Session.AUTO_ACKNOWLEDGE;
			logger.log(Logger.Level.INFO,
					"Calling getAcknowledgeMode and expect " + expAcknowledgeMode + " to be returned");
			int actAcknowledgeMode = session.getAcknowledgeMode();
			if (actAcknowledgeMode != expAcknowledgeMode) {
				logger.log(Logger.Level.ERROR,
						"getAcknowledgeMode() returned " + actAcknowledgeMode + ", expected " + expAcknowledgeMode);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("getAcknowledgeModeTopicTest", e);
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception e) {
			}
		}

		// Test for DUPS_OK_ACKNOWLEDGE mode
		try {
			session = connection.createSession(Session.DUPS_OK_ACKNOWLEDGE);
			int expAcknowledgeMode = Session.DUPS_OK_ACKNOWLEDGE;
			logger.log(Logger.Level.INFO,
					"Calling getAcknowledgeMode and expect " + expAcknowledgeMode + " to be returned");
			int actAcknowledgeMode = session.getAcknowledgeMode();
			if (actAcknowledgeMode != expAcknowledgeMode) {
				logger.log(Logger.Level.ERROR,
						"getAcknowledgeMode() returned " + actAcknowledgeMode + ", expected " + expAcknowledgeMode);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("getAcknowledgeModeTopicTest", e);
		} finally {
			try {
				if (session != null)
					session.close();
			} catch (Exception e) {
			}
		}

		// Test for SESSION_TRANSACTED mode
		if ((vehicle.equals("appclient") || vehicle.equals("standalone"))) {
			try {
				session = connection.createSession(Session.SESSION_TRANSACTED);
				int expAcknowledgeMode = Session.SESSION_TRANSACTED;
				logger.log(Logger.Level.INFO,
						"Calling getAcknowledgeMode and expect " + expAcknowledgeMode + " to be returned");
				int actAcknowledgeMode = session.getAcknowledgeMode();
				if (actAcknowledgeMode != expAcknowledgeMode) {
					logger.log(Logger.Level.ERROR,
							"getAcknowledgeMode() returned " + actAcknowledgeMode + ", expected " + expAcknowledgeMode);
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Caught exception: " + e);
				throw new Exception("getAcknowledgeModeTopicTest", e);
			} finally {
				try {
					if (session != null)
						session.close();
				} catch (Exception e) {
				}
			}
		}

		if (!pass) {
			throw new Exception("getAcknowledgeModeQueueTest failed");
		}
	}

	/*
	 * @testName: createConsumerProducerQueueTest
	 *
	 * @assertion_ids: JMS:JAVADOC:221; JMS:JAVADOC:242; JMS:JAVADOC:244;
	 * JMS:JAVADOC:246; JMS:JAVADOC:248; JMS:JAVADOC:504; JMS:JAVADOC:510;
	 * JMS:JAVADOC:597; JMS:JAVADOC:334;
	 *
	 * @test_Strategy: Test the following APIs:
	 *
	 * ConnectionFactory.createConnection(String, String)
	 * Connection.createSession(boolean, int) Session.createTextMessage(String)
	 * Session.createConsumer(Destination) Session.createConsumer(Destination,
	 * String) Session.createConsumer(Destination, String, boolean)
	 * Session.createProducer(Destination) MessageProducer.send(Message)
	 * MessageConsumer.receive(long timeout)
	 * 
	 * 1. Send x text messages to a Queue. 2. Create a MessageConsumer with selector
	 * to consume just the last message in the Queue. 3. Create a MessageConsumer
	 * again to consume the rest of the messages in the Queue.
	 */
	@Test
	public void createConsumerProducerQueueTest() throws Exception {
		boolean pass = true;
		try {
			TextMessage tempMsg = null;
			Enumeration msgs = null;

			// Setup for Queue
			setupGlobalVarsQ();
			consumer.close();

			// send "numMessages" messages to Queue plus end of stream message
			logger.log(Logger.Level.INFO, "Send " + numMessages + " messages to Queue");
			for (int i = 1; i <= numMessages; i++) {
				tempMsg = session.createTextMessage("Message " + i);
				tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "createConsumerProducerQueueTest" + i);
				tempMsg.setJMSType("");
				if (i == numMessages) {
					tempMsg.setBooleanProperty("lastMessage", true);
				} else {
					tempMsg.setBooleanProperty("lastMessage", false);
				}
				producer.send(tempMsg);
				logger.log(Logger.Level.INFO, "Message " + i + " sent");
			}

			// Create MessageConsumer to consume last message in Queue using message
			// selector
			logger.log(Logger.Level.INFO,
					"Create selective consumer to consume messages in Queue with boolproperty (lastMessage=TRUE)");
			consumer = session.createConsumer(destination, "lastMessage=TRUE");

			logger.log(Logger.Level.INFO,
					"Consume messages with selective consumer which has boolproperty (lastMessage=TRUE)");
			if (consumer != null)
				tempMsg = (TextMessage) consumer.receive(timeout);
			if (tempMsg == null) {
				logger.log(Logger.Level.ERROR, "MessageConsumer.receive() returned NULL");
				logger.log(Logger.Level.ERROR, "Message " + numMessages + " missing from Queue");
				pass = false;
			} else if (!tempMsg.getText().equals("Message " + numMessages)) {
				logger.log(Logger.Level.ERROR, "Received [" + tempMsg.getText() + "] expected [Message 3]");
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "Received expected message: " + tempMsg.getText());
			}

			// Try to receive one more message (should return null)
			logger.log(Logger.Level.INFO, "Make sure selective consumer receives no more messages");
			if (consumer != null)
				tempMsg = (TextMessage) consumer.receive(timeout);
			if (tempMsg != null) {
				logger.log(Logger.Level.ERROR, "MessageConsumer.receive() returned NULL");
				logger.log(Logger.Level.ERROR, "MessageConsumer with selector should have returned just 1 message");
				pass = false;
			}

			try {
				if (consumer != null)
					consumer.close();
			} catch (Exception e) {
			}

			// Create MessageConsumer to consume rest of messages in Queue
			logger.log(Logger.Level.INFO, "Consume rest of messages with normal consumer");
			consumer = session.createConsumer(destination);
			for (int msgCount = 1; msgCount < numMessages; msgCount++) {
				tempMsg = (TextMessage) consumer.receive(timeout);
				if (tempMsg == null) {
					logger.log(Logger.Level.ERROR, "MessageConsumer.receive() returned NULL");
					logger.log(Logger.Level.ERROR, "Message " + msgCount + " missing from Queue");
					pass = false;
				} else if (!tempMsg.getText().equals("Message " + msgCount)) {
					logger.log(Logger.Level.ERROR,
							"Received [" + tempMsg.getText() + "] expected [Message " + msgCount + "]");
					pass = false;
				} else {
					logger.log(Logger.Level.INFO, "Received message: " + tempMsg.getText());
				}
			}

			// Try to receive one more message (should return null)
			logger.log(Logger.Level.INFO, "Make sure normal consumer receives no more messages");
			tempMsg = (TextMessage) consumer.receive(timeout);
			if (tempMsg != null) {
				logger.log(Logger.Level.ERROR,
						"MessageConsumer should have returned " + (numMessages - 1) + " message");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			e.printStackTrace();
			throw new Exception("createConsumerProducerQueueTest");
		} finally {
			try {
				if (consumer != null)
					consumer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("createConsumerProducerQueueTest failed");
		}
	}

}
