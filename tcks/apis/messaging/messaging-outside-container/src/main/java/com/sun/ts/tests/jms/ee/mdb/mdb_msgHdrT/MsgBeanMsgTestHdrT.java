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

package com.sun.ts.tests.jms.ee.mdb.mdb_msgHdrT;

import java.lang.System.Logger;
import java.util.Enumeration;
import java.util.Vector;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsUtil;

import jakarta.ejb.EJBException;
import jakarta.ejb.MessageDrivenBean;
import jakarta.ejb.MessageDrivenContext;
import jakarta.jms.BytesMessage;
import jakarta.jms.DeliveryMode;
import jakarta.jms.JMSException;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.QueueConnection;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.QueueSender;
import jakarta.jms.QueueSession;
import jakarta.jms.StreamMessage;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;
import jakarta.jms.TopicConnection;
import jakarta.jms.TopicConnectionFactory;
import jakarta.jms.TopicPublisher;
import jakarta.jms.TopicSession;

public class MsgBeanMsgTestHdrT implements MessageDrivenBean, MessageListener {

	// properties object needed for logging, get this from the message object
	// passed into
	// the onMessage method.
	private java.util.Properties props = null;

	private TSNamingContext context = null;

	private MessageDrivenContext mdc = null;

	// JMS
	private QueueConnectionFactory qFactory = null;

	private QueueConnection qConnection = null;

	private Queue queueR = null;

	private QueueSession qSession = null;

	private QueueSender mSender;

	private Topic topic;

	private TopicConnection tConnection;

	private TopicSession tSession;

	private TopicConnectionFactory tFactory;

	private TopicPublisher tSender;

	private TextMessage messageSent = null;

	private StreamMessage messageSentStreamMessage = null;

	private BytesMessage messageSentBytesMessage = null;

	private MapMessage messageSentMapMessage = null;

	private ObjectMessage messageSentObjectMsg = null;

	private static final Logger logger = (Logger) System.getLogger(MsgBeanMsgTestHdrT.class.getName());

	public MsgBeanMsgTestHdrT() {
		logger.log(Logger.Level.TRACE, "@MsgBeanMsgTestHdrT()!");
	};

	public void ejbCreate() {
		props = new java.util.Properties();

		logger.log(Logger.Level.TRACE, "jms.ee.mdb.mdb_msgHdrT  - @MsgBeanMsgTestHdrT-ejbCreate() !!");
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

			tFactory = (TopicConnectionFactory) context.lookup("java:comp/env/jms/MyTopicConnectionFactory");
			topic = (Topic) context.lookup("java:comp/env/jms/MDB_TOPIC");

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new EJBException("MDB ejbCreate Error!", e);
		}
	}

	public void onMessage(Message msg) {

		logger.log(Logger.Level.TRACE, "from jms.ee.mdb.mdb_msgHdrT @onMessage!" + msg);

		try {
			JmsUtil.initHarnessProps(msg, props);
			logger.log(Logger.Level.TRACE, "from jms.ee.mdb.mdb_msgHdrT @onMessage!" + msg);

			logger.log(Logger.Level.TRACE, "onMessage will run TestCase: " + msg.getStringProperty("TestCase"));
			tConnection = tFactory.createTopicConnection();
			if (tConnection == null) {
				logger.log(Logger.Level.TRACE, "connection error");
			} else {
				tConnection.start();
				tSession = tConnection.createTopicSession(true, 0);
			}

			qConnection = qFactory.createQueueConnection();
			if (qConnection == null) {
				System.out.println("connection error");
			} else {
				qConnection.start();
				qSession = qConnection.createQueueSession(true, 0);
			}

			// Send a message back to acknowledge that the mdb received the message.

			// create testmessages
			Vector mVec = new Vector();
			messageSent = tSession.createTextMessage();
			mVec.addElement(messageSent);
			messageSentStreamMessage = tSession.createStreamMessage();
			mVec.addElement(messageSentStreamMessage);
			messageSentBytesMessage = tSession.createBytesMessage();
			mVec.addElement(messageSentBytesMessage);
			messageSentMapMessage = tSession.createMapMessage();
			mVec.addElement(messageSentMapMessage);
			messageSentObjectMsg = tSession.createObjectMessage();
			mVec.addElement(messageSentObjectMsg);

			// for each message addPropsToMessage
			Enumeration vNum = mVec.elements();
			while (vNum.hasMoreElements()) {
				JmsUtil.addPropsToMessage((Message) vNum.nextElement(), props);
			}

			if (msg.getStringProperty("TestCase").equals("msgHdrTimeStampTTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - running msgHdrTimeStampTTest - create the message");
				msgHdrTimeStampTTest();
			} else if (msg.getStringProperty("TestCase").equals("dummy")) {
				logger.log(Logger.Level.TRACE, "@onMessage - ignore this!");
			} else if (msg.getStringProperty("TestCase").equals("msgHdrCorlIdTTextTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrCorlIdTTextTestCreate!");
				msgHdrCorlIdTTextTestCreate();
			}

			else if (msg.getStringProperty("TestCase").equals("msgHdrCorlIdTTextTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrCorlIdTTextTest!");
				msgHdrCorlIdTTest(msg);
			} else if (msg.getStringProperty("TestCase").equals("msgHdrCorlIdTBytesTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrCorlIdTBytesTestCreate!");
				msgHdrCorlIdTBytesTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrCorlIdTBytesTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage -msgHdrCorlIdTBytesTest!");
				msgHdrCorlIdTTest(msg);
			} else if (msg.getStringProperty("TestCase").equals("msgHdrCorlIdTMapTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrCorlIdTMapTestCreate!");
				msgHdrCorlIdTMapTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrCorlIdTMapTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrCorlIdTMapTest!");
				msgHdrCorlIdTTest(msg);
			} else if (msg.getStringProperty("TestCase").equals("msgHdrCorlIdTStreamTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrCorlIdTStreamTestCreate!");
				msgHdrCorlIdTStreamTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrCorlIdTStreamTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrCorlIdTStreamTest!");
				msgHdrCorlIdTTest(msg);
			} else if (msg.getStringProperty("TestCase").equals("msgHdrCorlIdTObjectTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage -msgHdrCorlIdTObjectTestCreate!");
				msgHdrCorlIdTObjectTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrCorlIdTObjectTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrCorlIdTObjectTest!");
				msgHdrCorlIdTTest(msg);
			}

			else if (msg.getStringProperty("TestCase").equals("msgHdrReplyToTTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrReplyToTTestCreate!");
				msgHdrReplyToTTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrReplyToTTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrReplyToTTest!");
				msgHdrReplyToTTest(msg);
			} else if (msg.getStringProperty("TestCase").equals("msgHdrJMSTypeTTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrJMSTypeTTestCreate!");
				msgHdrJMSTypeTTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrJMSTypeTTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrJMSTypeTTest!");
				msgHdrJMSTypeTTest(msg);
			} else if (msg.getStringProperty("TestCase").equals("msgHdrJMSPriorityTTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrJMSPriorityTTestCreate!");
				msgHdrJMSPriorityTTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrJMSPriorityTTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrJMSPriorityTTest!");
				msgHdrJMSPriorityTTest(msg);
			} else if (msg.getStringProperty("TestCase").equals("msgHdrJMSExpirationTopicTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrJMSExpirationTopicTestCreate!");
				msgHdrJMSExpirationTopicTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrJMSExpirationTopicTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrJMSExpirationTopicTest!");
				msgHdrJMSExpirationTopicTest(msg);
			}

			else if (msg.getStringProperty("TestCase").equals("msgHdrJMSDestinationTTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrJMSDestinationTTestCreate!");
				msgHdrJMSDestinationTTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrJMSDestinationTTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrJMSDestinationTTest!");
				msgHdrJMSDestinationTTest(msg);
			} else if (msg.getStringProperty("TestCase").equals("msgHdrJMSDeliveryModeTTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrJMSDeliveryModeTTestCreate!");
				msgHdrJMSDeliveryModeTTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrJMSDeliveryModeTTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrJMSDeliveryModeTTest!");
				msgHdrJMSDeliveryModeTTest(msg);
			} else if (msg.getStringProperty("TestCase").equals("msgHdrIDTTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrIDTTestCreate!");
				msgHdrIDTTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrIDTTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrIDTTest!");
				msgHdrIDTTest(msg);
			}

			else {
				logger.log(Logger.Level.TRACE, "@onMessage - invalid message type found in StringProperty");
				logger.log(Logger.Level.TRACE,
						"Do not have a method for this testcase: " + msg.getStringProperty("TestCase"));
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
			if (tConnection != null) {
				try {
					tConnection.close();
				} catch (Exception e) {
					TestUtil.printStackTrace(e);
				}
			}
		}
	}

	/*
	 * Description: Send a single Text message check time of send against time send
	 * returns JMSTimeStamp should be between these two
	 */
	public void msgHdrTimeStampTTest() {
		boolean pass = true;
		long timeBeforeSend;
		long timeAfterSend;
		byte bValue = 127;
		String id = null;
		String testCase = "msgHdrTimeStampTTest";
		try {

			// Text Message
			messageSent.setText("sending a Text message");
			messageSent.setStringProperty("TestCase", "dummy");
			logger.log(Logger.Level.TRACE, "sending a Text message");

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			tSender = tSession.createPublisher(topic);
			tSender.publish(messageSent);
			// message has been sent
			timeAfterSend = System.currentTimeMillis();

			logger.log(Logger.Level.TRACE, " getJMSTimestamp");
			logger.log(Logger.Level.TRACE, " " + messageSent.getJMSTimestamp());
			logger.log(Logger.Level.TRACE, "Time at send is: " + timeBeforeSend);
			logger.log(Logger.Level.TRACE, "Time after return fromsend is:" + timeAfterSend);
			if ((timeBeforeSend <= messageSent.getJMSTimestamp()) && (timeAfterSend >= messageSent.getJMSTimestamp())) {
				logger.log(Logger.Level.TRACE, "TextMessage TimeStamp pass");
			} else {
				logger.log(Logger.Level.INFO, "Error: invalid timestamp from TextMessage");
				pass = false;
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		} finally {
			sendTestResults(testCase, pass);
		}
	}

	/*
	 * Description: create a text msg, set correlation id
	 */
	private void msgHdrCorlIdTTextTestCreate() {

		String jmsCorrelationID = "test Correlation id";
		try {

			messageSent.setText("sending a message");
			messageSent.setStringProperty("TestCase", "msgHdrCorlIdTTextTest");

			logger.log(Logger.Level.TRACE, "Send Text Message to Topic.");
			messageSent.setJMSCorrelationID(jmsCorrelationID);
			tSender = tSession.createPublisher(topic);
			tSender.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: create a stream msg, set correlation id
	 */
	private void msgHdrCorlIdTStreamTestCreate() {

		String jmsCorrelationID = "test Correlation id";
		try {

			messageSentStreamMessage.setStringProperty("TestCase", "msgHdrCorlIdTStreamTest");
			messageSentStreamMessage.setJMSCorrelationID(jmsCorrelationID);
			messageSentStreamMessage.writeString("Testing...");
			logger.log(Logger.Level.TRACE, "Sending Stream message");
			tSender = tSession.createPublisher(topic);
			tSender.publish(messageSentStreamMessage);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: create a bytes msg, set correlation id
	 */
	private void msgHdrCorlIdTBytesTestCreate() {
		byte bValue = 127;

		String jmsCorrelationID = "test Correlation id";
		try {

			logger.log(Logger.Level.TRACE, "Send BytesMessage to Topic.");
			messageSentBytesMessage.setStringProperty("TestCase", "msgHdrCorlIdTBytesTest");
			messageSentBytesMessage.setJMSCorrelationID(jmsCorrelationID);
			messageSentBytesMessage.writeByte(bValue);

			tSender = tSession.createPublisher(topic);
			tSender.publish(messageSentBytesMessage);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: create a mao msg, set correlation id
	 */
	private void msgHdrCorlIdTMapTestCreate() {

		String jmsCorrelationID = "test Correlation id";
		try {

			logger.log(Logger.Level.TRACE, "Send MapMessage to Topic.");

			messageSentMapMessage.setStringProperty("TestCase", "msgHdrCorlIdTMapTest");
			messageSentMapMessage.setJMSCorrelationID(jmsCorrelationID);
			messageSentMapMessage.setString("aString", "value");

			tSender = tSession.createPublisher(topic);
			tSender.publish(messageSentMapMessage);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: create an object msg, set correlation id
	 */
	private void msgHdrCorlIdTObjectTestCreate() {

		String jmsCorrelationID = "test Correlation id";
		try {

			logger.log(Logger.Level.TRACE, "Send ObjectMessage to Topic.");

			messageSentObjectMsg.setObject("msgHdrIDTObjectTest for Object Message");
			messageSentObjectMsg.setStringProperty("TestCase", "msgHdrCorlIdTObjectTest");
			messageSentObjectMsg.setJMSCorrelationID(jmsCorrelationID);

			tSender = tSession.createPublisher(topic);
			tSender.publish(messageSentObjectMsg);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}

	}

	public void msgHdrCorlIdTTest(jakarta.jms.Message messageReceived) {
		boolean pass = true;
		String jmsCorrelationID = "test Correlation id";
		try {
			logger.log(Logger.Level.TRACE, "jmsCorrelationID:  " + messageReceived.getJMSCorrelationID());
			if (messageReceived.getJMSCorrelationID() == null) {
				pass = false;
				logger.log(Logger.Level.INFO, "Text Message Error: JMSCorrelationID returned a  null");
			} else if (messageReceived.getJMSCorrelationID().equals(jmsCorrelationID)) {
				logger.log(Logger.Level.TRACE, "pass");
			} else {
				pass = false;
				logger.log(Logger.Level.INFO, "Text Message Error: JMSCorrelationID is incorrect");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		} finally {
			try {
				sendTestResults(messageReceived.getStringProperty("TestCase"), pass);
			} catch (Exception j) {
				TestUtil.printStackTrace(j);
			}
		}
	}

	/*
	 *                 
	 */
	public void msgHdrReplyToTTestCreate() {
		Topic replyTopic = null;
		try {

			messageSent.setText("sending a message");
			messageSent.setStringProperty("TestCase", "msgHdrReplyToTTest");
			messageSent.setJMSReplyTo(topic);

			tSender = tSession.createPublisher(topic);
			tSender.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	public void msgHdrReplyToTTest(jakarta.jms.Message messageReceived) {
		boolean pass = true;
		Topic replyTopic = null;
		String testCase = "msgHdrReplyToTTest";
		try {
			replyTopic = (Topic) messageReceived.getJMSReplyTo();
			logger.log(Logger.Level.TRACE, "Topic name is " + replyTopic.getTopicName());
			if (replyTopic.getTopicName().equals(topic.getTopicName())) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "ReplyTo Failed");
				pass = false;
			}

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		} finally {
			sendTestResults(testCase, pass);
		}
	}

	public void msgHdrJMSTypeTTestCreate() {
		boolean pass = true;
		byte bValue = 127;
		String type = "TESTMSG";
		try {
			messageSent.setText("sending a message");
			messageSent.setStringProperty("TestCase", "msgHdrJMSTypeTTest");
			logger.log(Logger.Level.TRACE, "JMSType test - Send a Text message");
			messageSent.setJMSType(type);
			tSender = tSession.createPublisher(topic);
			tSender.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	public void msgHdrJMSTypeTTest(jakarta.jms.Message messageReceived) {
		boolean pass = true;
		String type = "TESTMSG";
		String testCase = "msgHdrJMSTypeTTest";
		try {

			logger.log(Logger.Level.TRACE, "JMSType is " + (String) messageReceived.getJMSType());
			if (messageReceived.getJMSType().equals(type)) {
				logger.log(Logger.Level.TRACE, "Pass");
			} else {
				logger.log(Logger.Level.INFO, "Text Message Failed");
				pass = false;
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		} finally {
			sendTestResults(testCase, pass);
		}
	}

	public void msgHdrJMSPriorityTTestCreate() {
		boolean pass = true;
		byte bValue = 127;
		int priority = 2;
		try {

			messageSent.setText("sending a message");
			messageSent.setStringProperty("TestCase", "msgHdrJMSPriorityTTest");
			logger.log(Logger.Level.TRACE, "JMSPriority test - Send a Text message");
			tSender = tSession.createPublisher(topic);
			tSender.setPriority(priority);
			tSender.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	public void msgHdrJMSPriorityTTest(jakarta.jms.Message messageReceived) {
		boolean pass = true;
		int priority = 2;
		String testCase = "msgHdrJMSPriorityTTest";
		try {
			logger.log(Logger.Level.TRACE, "JMSPriority is " + messageReceived.getJMSPriority());
			if (messageReceived.getJMSPriority() == priority) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "JMSPriority test Failed");
				pass = false;
			}

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		} finally {
			sendTestResults(testCase, pass);
		}
	}

	public void msgHdrJMSExpirationTopicTestCreate() {
		boolean pass = true;
		long forever = 0;
		try {

			messageSent.setText("sending a message");
			messageSent.setStringProperty("TestCase", "msgHdrJMSExpirationTopicTest");
			logger.log(Logger.Level.TRACE, "JMSExpiration test - Send a Text message");

			tSender = tSession.createPublisher(topic);
			tSender.setTimeToLive(forever);
			tSender.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	public void msgHdrJMSExpirationTopicTest(jakarta.jms.Message messageReceived) {
		boolean pass = true;
		long forever = 0;
		String testCase = "msgHdrJMSExpirationTopicTest";
		try {

			logger.log(Logger.Level.TRACE, "JMSExpiration is " + messageReceived.getJMSExpiration());
			if (messageReceived.getJMSExpiration() == forever) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Text Message Failed");
				pass = false;
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		} finally {
			sendTestResults(testCase, pass);
		}
	}

	public void msgHdrJMSDestinationTTestCreate() {
		Topic replyDestination = null;
		try {

			messageSent.setText("sending a message");
			messageSent.setStringProperty("TestCase", "msgHdrJMSDestinationTTest");
			logger.log(Logger.Level.TRACE, "send msg for JMSDestination test.");
			tSender = tSession.createPublisher(topic);
			tSender.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	public void msgHdrJMSDestinationTTest(jakarta.jms.Message messageReceived) {
		boolean pass = true;
		Topic replyDestination = null;
		String testCase = "msgHdrJMSDestinationTTest";

		try {
			logger.log(Logger.Level.TRACE, "JMSDestination:  " + messageReceived.getJMSDestination());
			replyDestination = (Topic) messageReceived.getJMSDestination();

			if (replyDestination != null)
				logger.log(Logger.Level.TRACE, "Topic name is " + replyDestination.getTopicName());

			if (replyDestination == null) {
				pass = false;
				logger.log(Logger.Level.INFO, "Text Message Error: JMSDestination returned a  null");
			} else if (replyDestination.getTopicName().equals(topic.getTopicName())) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Text Message Failed");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception in msgHdrJMSDestinationTTest:", e);
		} finally {
			sendTestResults(testCase, pass);
		}
	}

	public void msgHdrJMSDeliveryModeTTestCreate() {
		try {

			messageSent.setText("sending a message");
			messageSent.setStringProperty("TestCase", "msgHdrJMSDeliveryModeTTest");
			tSender = tSession.createPublisher(topic);
			tSender.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	public void msgHdrJMSDeliveryModeTTest(jakarta.jms.Message messageReceived) {
		boolean pass = true;
		String testCase = "msgHdrJMSDeliveryModeTTest";
		try {

			logger.log(Logger.Level.TRACE, "JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
			if (messageReceived.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
				pass = false;
				logger.log(Logger.Level.INFO, "Error: JMSDeliveryMode should be set to persistent as default");
			} else {
				logger.log(Logger.Level.TRACE, "Pass: Default delivery mode is persistent");
			}

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		} finally {
			sendTestResults(testCase, pass);
		}
	}

	public void msgHdrIDTTestCreate() {
		String id = null;
		try {

			messageSent.setText("sending a Text message");
			messageSent.setStringProperty("TestCase", "msgHdrIDTTest");
			tSender = tSession.createPublisher(topic);
			tSender.publish(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	public void msgHdrIDTTest(jakarta.jms.Message messageReceived) {
		boolean pass = true;
		String id = null;
		String testCase = "msgHdrIDTTest";
		try {

			logger.log(Logger.Level.TRACE, "getJMSMessageID ");
			logger.log(Logger.Level.TRACE, " " + messageReceived.getJMSMessageID());
			id = messageReceived.getJMSMessageID();

			if (id.startsWith("ID:")) {
				logger.log(Logger.Level.TRACE, "Pass: JMSMessageID start with ID:");
			} else {
				logger.log(Logger.Level.INFO, "Error: JMSMessageID does not start with ID:");
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
		logger.log(Logger.Level.TRACE, "jms.ee.mdb.mdb_msgHdrT  In MsgBeanMsgTestHdrT::setMessageDrivenContext()!!");
		this.mdc = mdc;
	}

	public void ejbRemove() {
		logger.log(Logger.Level.TRACE, "jms.ee.mdb.mdb_msgHdrT  In MsgBeanMsgTestHdrT::remove()!!");
	}
}
