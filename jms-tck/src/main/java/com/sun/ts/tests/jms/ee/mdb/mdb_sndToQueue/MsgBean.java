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

package com.sun.ts.tests.jms.ee.mdb.mdb_sndToQueue;

import java.lang.System.Logger;
import java.util.Properties;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsUtil;

import jakarta.ejb.EJBException;
import jakarta.ejb.MessageDrivenBean;
import jakarta.ejb.MessageDrivenContext;
import jakarta.jms.BytesMessage;
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

public class MsgBean implements MessageDrivenBean, MessageListener {

	// properties object needed for logging, get this from the message object
	// passed into the onMessage method.
	private java.util.Properties p = null;

	private TSNamingContext context = null;

	private MessageDrivenContext mdc = null;

	private static final Logger logger = (Logger) System.getLogger(MsgBean.class.getName());

	// JMS
	private QueueConnectionFactory qFactory = null;

	private QueueConnection qConnection = null;

	private Queue queue = null;

	private QueueSender mSender = null;

	private QueueSession qSession = null;

	public MsgBean() {
		logger.log(Logger.Level.TRACE, "@MsgBean()!");
	};

	public void ejbCreate() {
		logger.log(Logger.Level.TRACE, "@MsgBean-ejbCreate() !!");
		try {
			context = new TSNamingContext();
			qFactory = (QueueConnectionFactory) context.lookup("java:comp/env/jms/MyQueueConnectionFactory");
			if (qFactory == null)
				logger.log(Logger.Level.ERROR, "qFactory error");
			logger.log(Logger.Level.TRACE, "got a qFactory !!");

			queue = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
			if (queue == null)
				logger.log(Logger.Level.ERROR, "queue error");

			logger.log(Logger.Level.TRACE, "got a queue ");
			p = new Properties();

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new EJBException("MDB ejbCreate Error!", e);
		}
	}

	public void onMessage(Message msg) {
		JmsUtil.initHarnessProps(msg, p);
		logger.log(Logger.Level.TRACE, "@onMessage! " + msg);

		try {
			qConnection = qFactory.createQueueConnection();
			if (qConnection == null)
				logger.log(Logger.Level.ERROR, "connection error");
			else {
				qConnection.start();
				qSession = qConnection.createQueueSession(true, 0);
			}
			logger.log(Logger.Level.TRACE, "started the connection !!");

			if (msg.getObjectProperty("properties") != null) {
				initLogging((java.util.Properties) msg.getObjectProperty("properties"));
			}

			// Send a message back to acknowledge that the mdb received the message.
			if (msg.getStringProperty("MessageType").equals("TextMessage")) {
				sendATextMessage();
			} else if (msg.getStringProperty("MessageType").equals("BytesMessage")) {
				sendABytesMessage();
			} else if (msg.getStringProperty("MessageType").equals("MapMessage")) {
				sendAMapMessage();
			} else if (msg.getStringProperty("MessageType").equals("StreamMessage")) {
				sendAStreamMessage();
			} else if (msg.getStringProperty("MessageType").equals("ObjectMessage")) {
				sendAnObjectMessage();
			} else {
				logger.log(Logger.Level.TRACE, "@onMessage - invalid message type found in StringProperty");
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception caught in onMessage!", e);
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

	// message bean helper methods follow.
	// Each method will send a simple message of the type requested.
	// this will send a text message to a Queue

	// must call init for logging to be properly performed
	public void initLogging(java.util.Properties p) {
		try {
			TestUtil.init(p);
			logger.log(Logger.Level.TRACE, "MsgBean initLogging OK.");
		} catch (RemoteLoggingInitException e) {
			TestUtil.printStackTrace(e);
			logger.log(Logger.Level.INFO, "MsgBean initLogging failed.");
			throw new EJBException(e.getMessage());
		}
	}

	private void sendATextMessage() {
		logger.log(Logger.Level.TRACE, "@sendATextMessage");
		try {
			String myMsg = "I am sending a text message as requested";

			// send a text message as requested to MDB_QUEUE_REPLY
			mSender = qSession.createSender(queue);

			TextMessage msg = qSession.createTextMessage();
			msg.setText(myMsg);
			msg.setStringProperty("MessageType", "TextMessageFromMsgBean");

			mSender.send(msg);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception caught sending a TextMessage!", e);
		}
	}

	private void sendABytesMessage() {
		logger.log(Logger.Level.TRACE, "@sendABytesMessage");
		try {
			byte aByte = 10;

			// send a text message as requested to MDB_QUEUE_REPLY
			mSender = qSession.createSender(queue);

			BytesMessage msg = qSession.createBytesMessage();
			JmsUtil.addPropsToMessage(msg, p);
			msg.writeByte(aByte);
			msg.setStringProperty("MessageType", "BytesMessageFromMsgBean");

			mSender.send(msg);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception caught sending a BytesMessage!", e);
		}
	}

	private void sendAMapMessage() {
		logger.log(Logger.Level.TRACE, "@sendAMapMessage");
		try {
			String myMsg = "I am sending a map message as requested";

			// send a text message as requested to MDB_QUEUE_REPLY
			mSender = qSession.createSender(queue);

			MapMessage msg = qSession.createMapMessage();
			JmsUtil.addPropsToMessage(msg, p);
			msg.setString("MapMessage", myMsg);
			msg.setStringProperty("MessageType", "MapMessageFromMsgBean");

			mSender.send(msg);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception caught sending a MapMessage!", e);
		}
	}

	private void sendAStreamMessage() {
		logger.log(Logger.Level.TRACE, "@sendAStreamMessage");
		try {
			String myMsg = "I am sending a stream message as requested";
			// send a text message as requested to MDB_QUEUE_REPLY
			mSender = qSession.createSender(queue);
			StreamMessage msg = qSession.createStreamMessage();
			JmsUtil.addPropsToMessage(msg, p);
			msg.writeString(myMsg);
			msg.setStringProperty("MessageType", "StreamMessageFromMsgBean");
			mSender.send(msg);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception caught sending a StreamMessage!", e);
		}
	}

	private void sendAnObjectMessage() {
		logger.log(Logger.Level.TRACE, "@sendAnObjectMessage");
		try {
			String myMsg = "I am sending a text message as requested";

			// send a text message as requested to MDB_QUEUE_REPLY
			mSender = qSession.createSender(queue);

			ObjectMessage msg = qSession.createObjectMessage();
			JmsUtil.addPropsToMessage(msg, p);
			msg.setObject(myMsg);
			msg.setStringProperty("MessageType", "ObjectMessageFromMsgBean");

			mSender.send(msg);
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Exception caught sending an ObjectMessage!", e);
		}
	}

	public void setMessageDrivenContext(MessageDrivenContext mdc) {
		logger.log(Logger.Level.TRACE, "In MsgBean::setMessageDrivenContext()!!");
		this.mdc = mdc;
	}

	public void ejbRemove() {
		logger.log(Logger.Level.TRACE, "In MsgBean::remove()!!");
	}
}
