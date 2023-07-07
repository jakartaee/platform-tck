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
package com.sun.ts.tests.jms.core.messageProducer;

import java.lang.System.Logger;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.DeliveryMode;
import jakarta.jms.Message;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;

public class MessageProducerTestsIT {
	private static final String testName = "com.sun.ts.tests.jms.core.messageProducer.MessageProducerTestsIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(MessageProducerTestsIT.class.getName());

	// Harness req's
	private Properties props = null;

	// JMS object
	private transient JmsTool tool = null;

	// properties read
	long timeout;

	private String jmsUser;

	private String jmsPassword;

	private String mode;

	/* Test setup: */

	/*
	 * @class.setup_props: jms_timeout;user; password; platform.mode;
	 * 
	 * @exception Fault
	 */
	@BeforeEach
	public void setup() throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "In setup");
			// get props
			jmsUser = System.getProperty("user");
			jmsPassword = System.getProperty("password");
			mode = System.getProperty("platform.mode");
			timeout = Long.parseLong(System.getProperty("jms_timeout"));
			if (timeout < 1) {
				throw new Exception("'timeout' (milliseconds) in must be > 0");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("Setup failed!", e);
		}
	}

	/*
	 * cleanup() is called after each test
	 */
	@AfterEach
	public void cleanup() throws Exception {

		if (tool != null) {
			try {
				if (tool.getDefaultConnection() != null) {
					logger.log(Logger.Level.TRACE, "Closing default Connection");
					tool.getDefaultConnection().close();
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error closing Connection in cleanup: ", e);
			}
		}
	}

	private void flushTheQueue() throws Exception {
		try {
			if (tool != null)
				if (tool.getDefaultConnection() != null)
					cleanup();

			tool = new JmsTool(JmsTool.COMMON_Q, jmsUser, jmsPassword, mode);

			logger.log(Logger.Level.TRACE, "Closing default Connection");
			tool.getDefaultConnection().close();
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Error closing connection and creating JmsTool: ", e);
		} finally {
			try {
				tool.flushDestination();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error flush Destination: ", e);
			}
		}
	}

	/*
	 * @testName: sendQueueTest1
	 * 
	 * @assertion_ids: JMS:JAVADOC:321; JMS:SPEC:253;
	 * 
	 * @test_Strategy: Send and receive single message using
	 * MessageProducer.send(Destination, Message) and MessageConsumer.receive(long).
	 * Verify message receipt.
	 */
	@Test
	public void sendQueueTest1() throws Exception {
		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			String testName = "sendQueueTest1";
			String testMessage = "Just a test from sendQueueTest1";
			boolean pass = true;
			MessageProducer msgproducer = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, jmsUser, jmsPassword, mode);
			tool.getDefaultProducer().close();
			msgproducer = tool.getDefaultSession().createProducer((Queue) null);
			tool.getDefaultConnection().start();

			logger.log(Logger.Level.INFO, "Creating 1 message");
			messageSent = tool.getDefaultSession().createTextMessage();
			messageSent.setText(testMessage);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);

			logger.log(Logger.Level.INFO, "Sending message");
			msgproducer.send(tool.getDefaultDestination(), messageSent);

			logger.log(Logger.Level.INFO, "Receiving message");
			messageReceived = (TextMessage) tool.getDefaultConsumer().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("didn't get any message");
			}

			// Check to see if correct message received
			if (!messageReceived.getText().equals(messageSent.getText())) {
				throw new Exception("didn't get the right message");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception(testName);
		} finally {
			try {
				flushTheQueue();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error flushing Queue", e);
			}
		}
	}

	/*
	 * @testName: sendQueueTest2
	 * 
	 * @assertion_ids: JMS:JAVADOC:323; JMS:SPEC:253;
	 * 
	 * @test_Strategy: Send and receive single message using
	 * MessageProducer.send(Destination, Message, int, int, long) and
	 * MessageConsumer.receive(long). Verify message receipt.
	 */
	@Test
	public void sendQueueTest2() throws Exception {
		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			String testName = "sendQueueTest2";
			String testMessage = "Just a test from sendQueueTest2";
			boolean pass = true;
			MessageProducer msgproducer = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, jmsUser, jmsPassword, mode);
			tool.getDefaultProducer().close();
			msgproducer = tool.getDefaultSession().createProducer((Queue) null);
			tool.getDefaultConnection().start();

			logger.log(Logger.Level.INFO, "Creating 1 message");
			messageSent = tool.getDefaultSession().createTextMessage();
			messageSent.setText(testMessage);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);

			logger.log(Logger.Level.INFO, "Sending message");
			msgproducer.send(tool.getDefaultDestination(), messageSent, DeliveryMode.NON_PERSISTENT,
					Message.DEFAULT_PRIORITY - 1, 0L);

			logger.log(Logger.Level.INFO, "Receiving message");
			messageReceived = (TextMessage) tool.getDefaultConsumer().receive(timeout);
			if (messageReceived == null) {
				logger.log(Logger.Level.ERROR, "didn't get any message");
				pass = false;
			} else if (!messageReceived.getText().equals(messageSent.getText())
					|| messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT
					|| messageReceived.getJMSPriority() != (Message.DEFAULT_PRIORITY - 1)) {
				pass = false;
				logger.log(Logger.Level.ERROR, "didn't get the right message.");
				logger.log(Logger.Level.ERROR, "text =" + messageReceived.getText());
				logger.log(Logger.Level.ERROR, "DeliveryMode =" + messageReceived.getJMSDeliveryMode());
				logger.log(Logger.Level.ERROR, "Priority =" + messageReceived.getJMSPriority());
			}

			if (!pass)
				throw new Exception(testName + " falied");
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception(testName);
		} finally {
			try {
				flushTheQueue();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error flushing Queue", e);
			}
		}
	}

	/*
	 * @testName: sendQueueTest3
	 * 
	 * @assertion_ids: JMS:JAVADOC:319; JMS:JAVADOC:313; JMS:SPEC:253;
	 * 
	 * @test_Strategy: Send and receive single message using
	 * MessageProducer.send(Message, int, int, long) and
	 * MessageConsumer.receive(long). Verify message receipt.
	 */
	@Test
	public void sendQueueTest3() throws Exception {
		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			String testName = "sendQueueTest3";
			String testMessage = "Just a test from sendQueueTest3";
			boolean pass = true;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, jmsUser, jmsPassword, mode);
			tool.getDefaultConnection().start();

			if (!((Queue) tool.getDefaultProducer().getDestination()).getQueueName()
					.equals(((Queue) tool.getDefaultDestination()).getQueueName())) {
				pass = false;
				logger.log(Logger.Level.ERROR, "getDestination test failed: "
						+ ((Queue) tool.getDefaultProducer().getDestination()).getQueueName());
			}

			logger.log(Logger.Level.INFO, "Creating 1 message");
			messageSent = tool.getDefaultSession().createTextMessage();
			messageSent.setText(testMessage);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);

			logger.log(Logger.Level.INFO, "Sending message");
			tool.getDefaultProducer().send(messageSent, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY - 1, 0L);

			logger.log(Logger.Level.INFO, "Receiving message");
			messageReceived = (TextMessage) tool.getDefaultConsumer().receive(timeout);
			if (messageReceived == null) {
				logger.log(Logger.Level.ERROR, "didn't get any message");
				pass = false;
			} else if (!messageReceived.getText().equals(messageSent.getText())
					|| messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT
					|| messageReceived.getJMSPriority() != (Message.DEFAULT_PRIORITY - 1)) {
				pass = false;
				logger.log(Logger.Level.ERROR, "didn't get the right message.");
				logger.log(Logger.Level.ERROR, "text =" + messageReceived.getText());
				logger.log(Logger.Level.ERROR, "DeliveryMode =" + messageReceived.getJMSDeliveryMode());
				logger.log(Logger.Level.ERROR, "Priority =" + messageReceived.getJMSPriority());
			}

			if (!pass)
				throw new Exception(testName + " falied");
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception(testName);
		} finally {
			try {
				flushTheQueue();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error flushing Queue", e);
			}
		}
	}

	/*
	 * @testName: sendTopicTest4
	 * 
	 * @assertion_ids: JMS:JAVADOC:321; JMS:SPEC:253;
	 * 
	 * @test_Strategy: Send and receive single message using
	 * MessageProducer.send(Destination, Message) and MessageConsumer.receive(long).
	 * Verify message receipt.
	 */
	@Test
	public void sendTopicTest4() throws Exception {
		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			String testName = "sendTopicTest4";
			String testMessage = "Just a test from sendTopicTest4";
			boolean pass = true;
			MessageProducer msgproducer = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, jmsUser, jmsPassword, mode);
			tool.getDefaultProducer().close();
			msgproducer = tool.getDefaultSession().createProducer((Topic) null);
			tool.getDefaultConnection().start();

			logger.log(Logger.Level.INFO, "Creating 1 message");
			messageSent = tool.getDefaultSession().createTextMessage();
			messageSent.setText(testMessage);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);

			logger.log(Logger.Level.INFO, "Sending message");
			msgproducer.send(tool.getDefaultDestination(), messageSent);

			logger.log(Logger.Level.INFO, "Receiving message");
			messageReceived = (TextMessage) tool.getDefaultConsumer().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("didn't get any message");
			}

			// Check to see if correct message received
			if (!messageReceived.getText().equals(messageSent.getText())) {
				throw new Exception("didn't get the right message");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception(testName);
		}

	}

	/*
	 * @testName: sendTopicTest5
	 * 
	 * @assertion_ids: JMS:JAVADOC:323; JMS:SPEC:253;
	 * 
	 * @test_Strategy: Send and receive single message using
	 * MessageProducer.send(Destination, Message, int, int, long). and
	 * MessageConsumer.receive(long). Verify message receipt.
	 */
	@Test
	public void sendTopicTest5() throws Exception {
		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			String testName = "sendTopicTest5";
			String testMessage = "Just a test from sendTopicTest5";
			boolean pass = true;
			MessageProducer msgproducer = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, jmsUser, jmsPassword, mode);
			tool.getDefaultProducer().close();
			msgproducer = tool.getDefaultSession().createProducer((Topic) null);
			tool.getDefaultConnection().start();

			logger.log(Logger.Level.INFO, "Creating 1 message");
			messageSent = tool.getDefaultSession().createTextMessage();
			messageSent.setText(testMessage);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);

			logger.log(Logger.Level.INFO, "Sending message");
			msgproducer.send(tool.getDefaultDestination(), messageSent, DeliveryMode.NON_PERSISTENT,
					Message.DEFAULT_PRIORITY - 1, 0L);

			logger.log(Logger.Level.INFO, "Receiving message");
			messageReceived = (TextMessage) tool.getDefaultConsumer().receive(timeout);
			if (messageReceived == null) {
				logger.log(Logger.Level.ERROR, "didn't get any message");
				pass = false;
			} else if (!messageReceived.getText().equals(messageSent.getText())
					|| messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT
					|| messageReceived.getJMSPriority() != (Message.DEFAULT_PRIORITY - 1)) {
				pass = false;
				logger.log(Logger.Level.ERROR, "didn't get the right message.");
				logger.log(Logger.Level.ERROR, "text =" + messageReceived.getText());
				logger.log(Logger.Level.ERROR, "DeliveryMode =" + messageReceived.getJMSDeliveryMode());
				logger.log(Logger.Level.ERROR, "Priority =" + messageReceived.getJMSPriority());
			}

			if (!pass)
				throw new Exception(testName + " falied");
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception(testName);
		}
	}

	/*
	 * @testName: sendTopicTest6
	 * 
	 * @assertion_ids: JMS:JAVADOC:319; JMS:JAVADOC:313; JMS:SPEC:253;
	 * 
	 * @test_Strategy: Send and receive single message using
	 * MessageProducer.send(Message, int, int, long) and
	 * MessageConsumer.receive(long). Verify message receipt.
	 */
	@Test
	public void sendTopicTest6() throws Exception {
		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			String testName = "sendTopicTest6";
			String testMessage = "Just a test from sendTopicTest6";
			boolean pass = true;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, jmsUser, jmsPassword, mode);
			tool.getDefaultConnection().start();

			if (!((Topic) tool.getDefaultProducer().getDestination()).getTopicName()
					.equals(((Topic) tool.getDefaultDestination()).getTopicName())) {
				pass = false;
				logger.log(Logger.Level.ERROR, "getDestination test failed: "
						+ ((Topic) tool.getDefaultProducer().getDestination()).getTopicName());
			}

			logger.log(Logger.Level.INFO, "Creating 1 message");
			messageSent = tool.getDefaultSession().createTextMessage();
			messageSent.setText(testMessage);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);

			logger.log(Logger.Level.INFO, "Sending message");
			tool.getDefaultProducer().send(messageSent, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY - 1, 0L);

			logger.log(Logger.Level.INFO, "Receiving message");
			messageReceived = (TextMessage) tool.getDefaultConsumer().receive(timeout);
			if (messageReceived == null) {
				logger.log(Logger.Level.ERROR, "didn't get any message");
				pass = false;
			} else if (!messageReceived.getText().equals(messageSent.getText())
					|| messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT
					|| messageReceived.getJMSPriority() != (Message.DEFAULT_PRIORITY - 1)) {
				pass = false;
				logger.log(Logger.Level.ERROR, "didn't get the right message.");
				logger.log(Logger.Level.ERROR, "text =" + messageReceived.getText());
				logger.log(Logger.Level.ERROR, "DeliveryMode =" + messageReceived.getJMSDeliveryMode());
				logger.log(Logger.Level.ERROR, "Priority =" + messageReceived.getJMSPriority());
			}

			if (!pass)
				throw new Exception(testName + " falied");
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception(testName);
		}
	}
}
