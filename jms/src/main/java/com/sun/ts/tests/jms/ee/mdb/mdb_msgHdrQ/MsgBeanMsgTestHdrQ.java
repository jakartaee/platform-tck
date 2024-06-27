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

package com.sun.ts.tests.jms.ee.mdb.mdb_msgHdrQ;

import java.lang.System.Logger;
import java.util.Enumeration;
import java.util.Properties;
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

public class MsgBeanMsgTestHdrQ implements MessageDrivenBean, MessageListener {

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

	private StreamMessage messageSentStreamMessage = null;

	private BytesMessage messageSentBytesMessage = null;

	private MapMessage messageSentMapMessage = null;

	private ObjectMessage messageSentObjectMessage = null;

	private static final Logger logger = (Logger) System.getLogger(MsgBeanMsgTestHdrQ.class.getName());

	public MsgBeanMsgTestHdrQ() {
		logger.log(Logger.Level.TRACE, "@MsgBeanMsgTestHdrQ()!");
	};

	public void ejbCreate() {
		logger.log(Logger.Level.TRACE, "jms.ee.mdb.mdb_msgHdrQ  - @MsgBeanMsgTestHdrQ-ejbCreate() !!");
		try {
			context = new TSNamingContext();
			qFactory = (QueueConnectionFactory) context.lookup("java:comp/env/jms/MyQueueConnectionFactory");
			if (qFactory == null) {
				logger.log(Logger.Level.TRACE, "qFactory error");
			}

			queueR = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
			if (queueR == null) {
				logger.log(Logger.Level.TRACE, "queueR error");
			}
			queue = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE");

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new EJBException("MDB ejbCreate Error!", e);
		}
	}

	public void onMessage(Message msg) {

		try {
			p = new Properties();
			JmsUtil.initHarnessProps(msg, p);

			logger.log(Logger.Level.TRACE, "from jms.ee.mdb.mdb_msgHdrQ @onMessage!" + msg);

			logger.log(Logger.Level.TRACE, "onMessage will run TestCase: " + msg.getStringProperty("TestCase"));
			qConnection = qFactory.createQueueConnection();
			if (qConnection == null) {
				throw new Exception("Null QueueConnection created");
			}
			qConnection.start();

			qSession = qConnection.createQueueSession(true, 0);

			// create testmessages
			Vector mVec = new Vector();
			messageSent = qSession.createTextMessage();
			mVec.addElement(messageSent);
			messageSentStreamMessage = qSession.createStreamMessage();
			mVec.addElement(messageSentStreamMessage);
			messageSentBytesMessage = qSession.createBytesMessage();
			mVec.addElement(messageSentBytesMessage);
			messageSentMapMessage = qSession.createMapMessage();
			mVec.addElement(messageSentMapMessage);
			messageSentObjectMessage = qSession.createObjectMessage();
			mVec.addElement(messageSentObjectMessage);

			// for each message addPropsToMessage
			Enumeration vNum = mVec.elements();
			while (vNum.hasMoreElements()) {
				JmsUtil.addPropsToMessage((Message) vNum.nextElement(), p);
			}

			// myTestUtil.logTrace - for trace logging messages

			if (msg.getStringProperty("TestCase").equals("msgHdrTimeStampQTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - running msgHdrTimeStampQTest - create the message");
				msgHdrTimeStampQTest();
			} else if (msg.getStringProperty("TestCase").equals("dummy")) {
				logger.log(Logger.Level.TRACE, "@onMessage - ignore this!");
			} else if (msg.getStringProperty("TestCase").equals("msgHdrCorlIdQTextTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrCorlIdQTextTestCreate!");
				msgHdrCorlIdQTextTestCreate();
			}

			else if (msg.getStringProperty("TestCase").equals("msgHdrCorlIdQTextTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrCorlIdQTextTest!");
				msgHdrCorlIdQTest(msg);
			} else if (msg.getStringProperty("TestCase").equals("msgHdrCorlIdQBytesTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrCorlIdQBytesTestCreate!");
				msgHdrCorlIdQBytesTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrCorlIdQBytesTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage -msgHdrCorlIdQBytesTest!");
				msgHdrCorlIdQTest(msg);
			} else if (msg.getStringProperty("TestCase").equals("msgHdrCorlIdQMapTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrCorlIdQMapTestCreate!");
				msgHdrCorlIdQMapTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrCorlIdQMapTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrCorlIdQMapTest!");
				msgHdrCorlIdQTest(msg);
			} else if (msg.getStringProperty("TestCase").equals("msgHdrCorlIdQStreamTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrCorlIdQStreamTestCreate!");
				msgHdrCorlIdQStreamTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrCorlIdQStreamTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrCorlIdQStreamTest!");
				msgHdrCorlIdQTest(msg);
			} else if (msg.getStringProperty("TestCase").equals("msgHdrCorlIdQObjectTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage -msgHdrCorlIdQObjectTestCreate!");
				msgHdrCorlIdQObjectTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrCorlIdQObjectTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrCorlIdQObjectTest!");
				msgHdrCorlIdQTest(msg);
			}

			else if (msg.getStringProperty("TestCase").equals("msgHdrReplyToQTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrReplyToQTestCreate!");
				msgHdrReplyToQTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrReplyToQTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrReplyToQTest!");
				msgHdrReplyToQTest(msg);
			} else if (msg.getStringProperty("TestCase").equals("msgHdrJMSTypeQTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrJMSTypeQTestCreate!");
				msgHdrJMSTypeQTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrJMSTypeQTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrJMSTypeQTest!");
				msgHdrJMSTypeQTest(msg);
			} else if (msg.getStringProperty("TestCase").equals("msgHdrJMSPriorityQTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrJMSPriorityQTestCreate!");
				msgHdrJMSPriorityQTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrJMSPriorityQTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrJMSPriorityQTest!");
				msgHdrJMSPriorityQTest(msg);
			} else if (msg.getStringProperty("TestCase").equals("msgHdrJMSExpirationQueueTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrJMSExpirationQueueTestCreate!");
				msgHdrJMSExpirationQueueTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrJMSExpirationQueueTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrJMSExpirationQueueTest!");
				msgHdrJMSExpirationQueueTest(msg);
			}

			else if (msg.getStringProperty("TestCase").equals("msgHdrJMSDestinationQTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrJMSDestinationQTestCreate!");
				msgHdrJMSDestinationQTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrJMSDestinationQTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrJMSDestinationQTest!");
				msgHdrJMSDestinationQTest(msg);
			} else if (msg.getStringProperty("TestCase").equals("msgHdrJMSDeliveryModeQTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrJMSDeliveryModeQTestCreate!");
				msgHdrJMSDeliveryModeQTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrJMSDeliveryModeQTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrJMSDeliveryModeQTest!");
				msgHdrJMSDeliveryModeQTest(msg);
			} else if (msg.getStringProperty("TestCase").equals("msgHdrIDQTestCreate")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrIDQTestCreate!");
				msgHdrIDQTestCreate();
			} else if (msg.getStringProperty("TestCase").equals("msgHdrIDQTest")) {
				logger.log(Logger.Level.TRACE, "@onMessage - msgHdrIDQTest!");
				msgHdrIDQTest(msg);
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
		}

	}

	/*
	 * Description: Send a single Text, map, bytes, stream, and object message check
	 * time of send against time send returns JMSTimeStamp should be between these
	 * two
	 */
	public void msgHdrTimeStampQTest() {
		boolean pass = true;
		long timeBeforeSend;
		long timeAfterSend;
		byte bValue = 127;
		String id = null;
		String testCase = "msgHdrTimeStampQTest";
		try {

			// send Object message
			logger.log(Logger.Level.TRACE, "Send ObjectMessage to Queue.");
			messageSentObjectMessage.setObject("msgHdrTimeStampQTest for Object Message");
			messageSentObjectMessage.setStringProperty("TestCase", "dummy");

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			mSender = qSession.createSender(queue);
			mSender.send(messageSentObjectMessage);
			// message has been sent
			timeAfterSend = System.currentTimeMillis();
			logger.log(Logger.Level.TRACE, " getJMSTimestamp");
			logger.log(Logger.Level.TRACE, " " + messageSentObjectMessage.getJMSTimestamp());
			logger.log(Logger.Level.TRACE, "Time at send is: " + timeBeforeSend);
			logger.log(Logger.Level.TRACE, "Time after return fromsend is:" + timeAfterSend);
			if ((timeBeforeSend <= messageSentObjectMessage.getJMSTimestamp())
					&& (timeAfterSend >= messageSentObjectMessage.getJMSTimestamp())) {
				logger.log(Logger.Level.TRACE, "Object Message TimeStamp pass");
			} else {
				logger.log(Logger.Level.INFO, "Error: invalid timestamp from ObjectMessage");
				pass = false;
			}

			// send map message to Queue
			logger.log(Logger.Level.TRACE, "Send MapMessage to Queue.");

			messageSentMapMessage.setStringProperty("TestCase", "dummy");
			messageSentMapMessage.setString("aString", "value");

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			mSender.send(messageSentMapMessage);
			// message has been sent
			timeAfterSend = System.currentTimeMillis();

			logger.log(Logger.Level.TRACE, " getJMSTimestamp");
			logger.log(Logger.Level.TRACE, " " + messageSentMapMessage.getJMSTimestamp());
			logger.log(Logger.Level.TRACE, "Time at send is: " + timeBeforeSend);
			logger.log(Logger.Level.TRACE, "Time after return fromsend is:" + timeAfterSend);
			if ((timeBeforeSend <= messageSentMapMessage.getJMSTimestamp())
					&& (timeAfterSend >= messageSentMapMessage.getJMSTimestamp())) {
				logger.log(Logger.Level.TRACE, "MapMessage TimeStamp pass");
			} else {
				logger.log(Logger.Level.INFO, "Error: invalid timestamp from MapMessage");
				pass = false;
			}

			// send bytes message

			logger.log(Logger.Level.TRACE, "Send BytesMessage to Queue.");

			messageSentBytesMessage.setStringProperty("TestCase", "dummy");

			messageSentBytesMessage.writeByte(bValue);

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			mSender.send(messageSentBytesMessage);
			// message has been sent
			timeAfterSend = System.currentTimeMillis();

			logger.log(Logger.Level.TRACE, " getJMSTimestamp");
			logger.log(Logger.Level.TRACE, " " + messageSentBytesMessage.getJMSTimestamp());
			logger.log(Logger.Level.TRACE, "Time at send is: " + timeBeforeSend);
			logger.log(Logger.Level.TRACE, "Time after return fromsend is:" + timeAfterSend);
			if ((timeBeforeSend <= messageSentBytesMessage.getJMSTimestamp())
					&& (timeAfterSend >= messageSentBytesMessage.getJMSTimestamp())) {
				logger.log(Logger.Level.TRACE, "BytesMessage TimeStamp pass");
			} else {
				logger.log(Logger.Level.INFO, "Error: invalid timestamp from BytesMessage");
				pass = false;
			}

			// Send a StreamMessage
			logger.log(Logger.Level.TRACE, "sending a Stream message");

			messageSentStreamMessage.setStringProperty("TestCase", "dummy");

			messageSentStreamMessage.writeString("Testing...");
			logger.log(Logger.Level.TRACE, "Sending message");

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			mSender.send(messageSentStreamMessage);
			// message has been sent
			timeAfterSend = System.currentTimeMillis();

			logger.log(Logger.Level.TRACE, " getJMSTimestamp");
			logger.log(Logger.Level.TRACE, " " + messageSentStreamMessage.getJMSTimestamp());
			logger.log(Logger.Level.TRACE, "Time at send is: " + timeBeforeSend);
			logger.log(Logger.Level.TRACE, "Time after return fromsend is:" + timeAfterSend);
			if ((timeBeforeSend <= messageSentStreamMessage.getJMSTimestamp())
					&& (timeAfterSend >= messageSentStreamMessage.getJMSTimestamp())) {
				logger.log(Logger.Level.TRACE, "StreamMessage TimeStamp pass");
			} else {
				logger.log(Logger.Level.INFO, "Error: invalid timestamp from StreamMessage");
				pass = false;
			}

			// Text Message

			messageSent.setText("sending a Text message");
			messageSent.setStringProperty("TestCase", "dummy");

			logger.log(Logger.Level.TRACE, "sending a Text message");

			// get the current time in milliseconds - before and after the send
			timeBeforeSend = System.currentTimeMillis();
			mSender.send(messageSent);
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
	private void msgHdrCorlIdQTextTestCreate() {

		String jmsCorrelationID = "test Correlation id";
		try {

			messageSent.setText("sending a message");
			messageSent.setStringProperty("TestCase", "msgHdrCorlIdQTextTest");

			logger.log(Logger.Level.TRACE, "Send Text Message to Queue.");
			messageSent.setJMSCorrelationID(jmsCorrelationID);
			mSender = qSession.createSender(queue);
			mSender.send(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: create a stream msg, set correlation id
	 */
	private void msgHdrCorlIdQStreamTestCreate() {

		String jmsCorrelationID = "test Correlation id";
		try {

			messageSentStreamMessage.setStringProperty("TestCase", "msgHdrCorlIdQStreamTest");
			messageSentStreamMessage.setJMSCorrelationID(jmsCorrelationID);
			messageSentStreamMessage.writeString("Testing...");
			logger.log(Logger.Level.TRACE, "Sending Stream message");
			mSender = qSession.createSender(queue);
			mSender.send(messageSentStreamMessage);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: create a bytes msg, set correlation id
	 */
	private void msgHdrCorlIdQBytesTestCreate() {
		byte bValue = 127;

		String jmsCorrelationID = "test Correlation id";
		try {

			logger.log(Logger.Level.TRACE, "Send BytesMessage to Queue.");

			messageSentBytesMessage.setStringProperty("TestCase", "msgHdrCorlIdQBytesTest");
			messageSentBytesMessage.setJMSCorrelationID(jmsCorrelationID);
			messageSentBytesMessage.writeByte(bValue);

			mSender = qSession.createSender(queue);
			mSender.send(messageSentBytesMessage);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: create a mao msg, set correlation id
	 */
	private void msgHdrCorlIdQMapTestCreate() {

		String jmsCorrelationID = "test Correlation id";
		try {

			logger.log(Logger.Level.TRACE, "Send MapMessage to Queue.");

			messageSentMapMessage.setStringProperty("TestCase", "msgHdrCorlIdQMapTest");
			messageSentMapMessage.setJMSCorrelationID(jmsCorrelationID);
			messageSentMapMessage.setString("aString", "value");

			mSender = qSession.createSender(queue);
			mSender.send(messageSentMapMessage);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	/*
	 * Description: create an object msg, set correlation id
	 */
	private void msgHdrCorlIdQObjectTestCreate() {

		String jmsCorrelationID = "test Correlation id";
		try {
			logger.log(Logger.Level.TRACE, "Send ObjectMessage to Queue.");

			messageSentObjectMessage.setObject("msgHdrIDQObjectTest for Object Message");
			messageSentObjectMessage.setStringProperty("TestCase", "msgHdrCorlIdQObjectTest");
			messageSentObjectMessage.setJMSCorrelationID(jmsCorrelationID);

			mSender = qSession.createSender(queue);
			mSender.send(messageSentObjectMessage);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}

	}

	public void msgHdrCorlIdQTest(jakarta.jms.Message messageReceived) {
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
	public void msgHdrReplyToQTestCreate() {
		Queue replyQueue = null;
		try {

			messageSent.setText("sending a message");
			messageSent.setStringProperty("TestCase", "msgHdrReplyToQTest");
			messageSent.setJMSReplyTo(queue);

			mSender = qSession.createSender(queue);
			mSender.send(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	public void msgHdrReplyToQTest(jakarta.jms.Message messageReceived) {
		boolean pass = true;
		Queue replyQueue = null;
		String testCase = "msgHdrReplyToQTest";
		try {
			replyQueue = (Queue) messageReceived.getJMSReplyTo();
			logger.log(Logger.Level.TRACE, "Queue name is " + replyQueue.getQueueName());
			if (replyQueue.getQueueName().equals(queue.getQueueName())) {
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

	public void msgHdrJMSTypeQTestCreate() {
		boolean pass = true;
		byte bValue = 127;
		String type = "TESTMSG";
		try {
			messageSent.setText("sending a message");
			messageSent.setStringProperty("TestCase", "msgHdrJMSTypeQTest");
			logger.log(Logger.Level.TRACE, "JMSType test - Send a Text message");
			messageSent.setJMSType(type);
			mSender = qSession.createSender(queue);
			mSender.send(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	public void msgHdrJMSTypeQTest(jakarta.jms.Message messageReceived) {
		boolean pass = true;
		String type = "TESTMSG";
		String testCase = "msgHdrJMSTypeQTest";
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

	public void msgHdrJMSPriorityQTestCreate() {
		boolean pass = true;
		byte bValue = 127;
		int priority = 2;
		try {

			messageSent.setText("sending a message");
			messageSent.setStringProperty("TestCase", "msgHdrJMSPriorityQTest");
			logger.log(Logger.Level.TRACE, "JMSPriority test - Send a Text message");
			mSender = qSession.createSender(queue);
			mSender.setPriority(priority);
			mSender.send(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	public void msgHdrJMSPriorityQTest(jakarta.jms.Message messageReceived) {
		boolean pass = true;
		int priority = 2;
		String testCase = "msgHdrJMSPriorityQTest";
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

	public void msgHdrJMSExpirationQueueTestCreate() {
		boolean pass = true;
		long forever = 0;
		try {

			messageSent.setText("sending a message");
			messageSent.setStringProperty("TestCase", "msgHdrJMSExpirationQueueTest");
			logger.log(Logger.Level.TRACE, "JMSExpiration test - Send a Text message");

			mSender = qSession.createSender(queue);
			mSender.setTimeToLive(forever);
			mSender.send(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	public void msgHdrJMSExpirationQueueTest(jakarta.jms.Message messageReceived) {
		boolean pass = true;
		long forever = 0;
		String testCase = "msgHdrJMSExpirationQueueTest";
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

	public void msgHdrJMSDestinationQTestCreate() {
		Queue replyDestination = null;
		try {

			messageSent.setText("sending a message");
			messageSent.setStringProperty("TestCase", "msgHdrJMSDestinationQTest");
			logger.log(Logger.Level.TRACE, "send msg for JMSDestination test.");
			mSender = qSession.createSender(queue);
			mSender.send(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	public void msgHdrJMSDestinationQTest(jakarta.jms.Message messageReceived) {
		boolean pass = true;
		Queue replyDestination = null;
		String testCase = "msgHdrJMSDestinationQTest";

		try {
			logger.log(Logger.Level.TRACE, "JMSDestination:  " + messageReceived.getJMSDestination());
			replyDestination = (Queue) messageReceived.getJMSDestination();
			if (replyDestination != null)
				logger.log(Logger.Level.TRACE, "Queue name is " + replyDestination.getQueueName());

			if (replyDestination == null) {
				pass = false;
				logger.log(Logger.Level.INFO, "Text Message Error: JMSDestination returned a  null");
			} else if (replyDestination.getQueueName().equals(queue.getQueueName())) {
				logger.log(Logger.Level.TRACE, "Pass ");
			} else {
				logger.log(Logger.Level.INFO, "Text Message Failed");
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Unexpected Exception thrown in msgHdrJMSDestinationQTest:", e);
		} finally {
			sendTestResults(testCase, pass);
		}
	}

	public void msgHdrJMSDeliveryModeQTestCreate() {
		try {

			messageSent.setText("sending a message");
			messageSent.setStringProperty("TestCase", "msgHdrJMSDeliveryModeQTest");
			mSender = qSession.createSender(queue);
			mSender.send(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	public void msgHdrJMSDeliveryModeQTest(jakarta.jms.Message messageReceived) {
		boolean pass = true;
		String testCase = "msgHdrJMSDeliveryModeQTest";
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

	public void msgHdrIDQTestCreate() {
		String id = null;
		try {

			messageSent.setText("sending a Text message");
			messageSent.setStringProperty("TestCase", "msgHdrIDQTest");
			mSender = qSession.createSender(queue);
			mSender.send(messageSent);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	public void msgHdrIDQTest(jakarta.jms.Message messageReceived) {
		boolean pass = true;
		String id = null;
		String testCase = "msgHdrIDQTest";
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
		logger.log(Logger.Level.TRACE, "jms.ee.mdb.mdb_msgHdrQ  In MsgBeanMsgTestHdrQ::setMessageDrivenContext()!!");
		this.mdc = mdc;
	}

	public void ejbRemove() {
		logger.log(Logger.Level.TRACE, "jms.ee.mdb.mdb_msgHdrQ  In MsgBeanMsgTestHdrQ::remove()!!");
	}
}
