/*
 * Copyright (c) 2015, 2023 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jms.core20.appclient.messageproducertests;

import java.lang.System.Logger;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.InvalidTextMessageTestImpl;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.Connection;
import jakarta.jms.DeliveryMode;
import jakarta.jms.Destination;
import jakarta.jms.InvalidDestinationException;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageFormatException;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;


public class ClientIT {
	private static final String testName = "com.sun.ts.tests.jms.core20.messageproducertests.ClientIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(ClientIT.class.getName());

	// JMS tool which creates and/or looks up the JMS administered objects
	private transient JmsTool tool = null;

	// JMS tool which creates and/or looks up the JMS administered objects
	private transient JmsTool toolForProducer = null;

	// JMS objects
	transient MessageProducer producer = null;

	transient MessageConsumer consumer = null;

	transient Connection connection = null;

	transient Session session = null;

	transient Destination destination = null;

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

	boolean queueTest = false;

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
			throw new Exception("Didn't get expected exception");
		}
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
			tool.getDefaultConnection().close();
			if (queueTest) {
				logger.log(Logger.Level.INFO, "Flush any messages left on Queue");
				tool.flushDestination();
			}
			tool.closeAllResources();

			if (toolForProducer != null) {
				toolForProducer.getDefaultConnection().close();
				if (queueTest) {
					logger.log(Logger.Level.INFO, "Flush any messages left on Queue");
					toolForProducer.flushDestination();
				}
				toolForProducer.closeAllResources();
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("cleanup failed!", e);
		}
	}

	/*
	 * @testName: queueSendRecvCompletionListenerTest1
	 * 
	 * @assertion_ids: JMS:JAVADOC:898;
	 * 
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 * 
	 * MessageProducer.send(Destination, Message, CompletionListener)
	 */
	@Test
	public void queueSendRecvCompletionListenerTest1() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Queue) null);
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = true;

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueSendRecvCompletionListenerTest1");

			// Create CompletionListener for Message to be sent
			MyCompletionListener listener = new MyCompletionListener();

			logger.log(Logger.Level.INFO, "Calling send(Destination,Message,CompletionListener)");
			producer.send(destination, expTextMessage, listener);

			logger.log(Logger.Level.INFO, "Poll listener waiting for TestMessage to arrive");
			TextMessage actTextMessage = null;
			for (int i = 0; !listener.isComplete() && i < 60; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for messages to arrive at listener");
				TestUtil.sleepSec(2);
			}
			if (listener.isComplete())
				actTextMessage = (TextMessage) listener.getMessage();

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
			throw new Exception("queueSendRecvCompletionListenerTest1", e);
		} finally {
			try {
				producer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("queueSendRecvCompletionListenerTest1 failed");
		}
	}

	/*
	 * @testName: queueSendRecvCompletionListenerTest2
	 * 
	 * @assertion_ids: JMS:JAVADOC:903;
	 * 
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 * 
	 * MessageProducer.send(Destination, Message, int, int, long,
	 * CompletionListener)
	 */
	@Test
	public void queueSendRecvCompletionListenerTest2() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Queue) null);
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = true;

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueSendRecvCompletionListenerTest2");

			// Create CompletionListener for Message to be sent
			MyCompletionListener listener = new MyCompletionListener();

			logger.log(Logger.Level.INFO, "Calling send(Destination,Message,int,int,long,CompletionListener)");
			producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 0L,
					listener);

			logger.log(Logger.Level.INFO, "Poll listener waiting for TestMessage to arrive");
			TextMessage actTextMessage = null;
			for (int i = 0; !listener.isComplete() && i < 60; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for messages to arrive at listener");
				TestUtil.sleepSec(2);
			}
			if (listener.isComplete())
				actTextMessage = (TextMessage) listener.getMessage();

			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the values in TextMessage, deliverymode, priority, time to live");
			if (!actTextMessage.getText().equals(expTextMessage.getText())
					|| actTextMessage.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT
					|| actTextMessage.getJMSPriority() != Message.DEFAULT_PRIORITY
					|| actTextMessage.getJMSExpiration() != 0L) {
				logger.log(Logger.Level.ERROR, "Didn't get the right message.");
				logger.log(Logger.Level.ERROR,
						"text=" + actTextMessage.getText() + ", expected " + expTextMessage.getText());
				logger.log(Logger.Level.ERROR, "DeliveryMode=" + actTextMessage.getJMSDeliveryMode() + ", expected "
						+ expTextMessage.getJMSDeliveryMode());
				logger.log(Logger.Level.ERROR, "Priority=" + actTextMessage.getJMSPriority() + ", expected "
						+ expTextMessage.getJMSPriority());
				logger.log(Logger.Level.ERROR, "TimeToLive=" + actTextMessage.getJMSExpiration() + ", expected "
						+ expTextMessage.getJMSExpiration());
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("queueSendRecvCompletionListenerTest2", e);
		} finally {
			try {
				producer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("queueSendRecvCompletionListenerTest2 failed");
		}
	}

	/*
	 * @testName: queueSendRecvCompletionListenerTest3
	 * 
	 * @assertion_ids: JMS:JAVADOC:888;
	 * 
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 * 
	 * MessageProducer.send(Message, CompletionListener)
	 */
	@Test
	public void queueSendRecvCompletionListenerTest3() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			producer = tool.getDefaultProducer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = true;

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueSendRecvCompletionListenerTest3");

			// Create CompletionListener for Message to be sent
			MyCompletionListener listener = new MyCompletionListener();

			logger.log(Logger.Level.INFO, "Calling send(Message,CompletionListener)");
			producer.send(expTextMessage, listener);

			logger.log(Logger.Level.INFO, "Poll listener waiting for TestMessage to arrive");
			TextMessage actTextMessage = null;
			for (int i = 0; !listener.isComplete() && i < 60; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for messages to arrive at listener");
				TestUtil.sleepSec(2);
			}
			if (listener.isComplete())
				actTextMessage = (TextMessage) listener.getMessage();

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
			throw new Exception("queueSendRecvCompletionListenerTest3", e);
		}

		if (!pass) {
			throw new Exception("queueSendRecvCompletionListenerTest3 failed");
		}
	}

	/*
	 * @testName: queueSendRecvCompletionListenerTest4
	 * 
	 * @assertion_ids: JMS:JAVADOC:893;
	 * 
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 * 
	 * MessageProducer.send(Message, int, int, long, CompletionListener)
	 */
	@Test
	public void queueSendRecvCompletionListenerTest4() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			producer = tool.getDefaultProducer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = true;

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueSendRecvCompletionListeneTest4");

			// Create CompletionListener for Message to be sent
			MyCompletionListener listener = new MyCompletionListener();

			logger.log(Logger.Level.INFO, "Calling send(Message,int,int,long,CompletionListener)");
			producer.send(expTextMessage, DeliveryMode.PERSISTENT, Message.DEFAULT_PRIORITY, 0L, listener);

			logger.log(Logger.Level.INFO, "Poll listener waiting for TestMessage to arrive");
			TextMessage actTextMessage = null;
			for (int i = 0; !listener.isComplete() && i < 60; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for messages to arrive at listener");
				TestUtil.sleepSec(2);
			}
			if (listener.isComplete())
				actTextMessage = (TextMessage) listener.getMessage();

			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the values in TextMessage, deliverymode, priority, time to live");
			if (!actTextMessage.getText().equals(expTextMessage.getText())
					|| actTextMessage.getJMSDeliveryMode() != DeliveryMode.PERSISTENT
					|| actTextMessage.getJMSPriority() != Message.DEFAULT_PRIORITY
					|| actTextMessage.getJMSExpiration() != 0L) {
				logger.log(Logger.Level.ERROR, "Didn't get the right message.");
				logger.log(Logger.Level.ERROR,
						"text=" + actTextMessage.getText() + ", expected " + expTextMessage.getText());
				logger.log(Logger.Level.ERROR, "DeliveryMode=" + actTextMessage.getJMSDeliveryMode() + ", expected "
						+ expTextMessage.getJMSDeliveryMode());
				logger.log(Logger.Level.ERROR, "Priority=" + actTextMessage.getJMSPriority() + ", expected "
						+ expTextMessage.getJMSPriority());
				logger.log(Logger.Level.ERROR, "TimeToLive=" + actTextMessage.getJMSExpiration() + ", expected "
						+ expTextMessage.getJMSExpiration());
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("queueSendRecvCompletionListenerTest4", e);
		}

		if (!pass) {
			throw new Exception("queueSendRecvCompletionListenerTest4 failed");
		}
	}

	/*
	 * @testName: queueJMSExceptionTests
	 * 
	 * @assertion_ids: JMS:JAVADOC:904;
	 * 
	 * @test_Strategy: Test for JMSException from MessageProducer API's.
	 */
	@Test
	public void queueJMSExceptionTests() throws Exception {
		boolean pass = true;
		TextMessage tempMsg = null;
		String message = "Where are you!";
		try {
			// set up test tool for Queue
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON QUEUE");
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Queue) null);
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = true;

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueJMSExceptionTests");

			// Create CompletionListener for Message to be sent
			MyCompletionListener listener = new MyCompletionListener();

			try {
				logger.log(Logger.Level.INFO, "Try and set an invalid mode of -1 on send");
				producer.send(destination, expTextMessage, -1, Message.DEFAULT_PRIORITY, 0L, listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 30; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof JMSException) {
						logger.log(Logger.Level.INFO, "Exception is expected JMSException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected JMSException, received " + exception.getCause());
						pass = false;
					}
				}
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Try and set an invalid priority of -1 on send");
				producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT, -1, 0L, listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 30; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof JMSException) {
						logger.log(Logger.Level.INFO, "Exception is expected JMSException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected JMSException, received " + exception.getCause());
						pass = false;
					}
				}
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("queueJMSExceptionTests", e);
		}

		if (!pass) {
			throw new Exception("queueJMSExceptionTests failed");
		}
	}

	/*
	 * @testName: queueInvalidDestinationExceptionTests
	 * 
	 * @assertion_ids: JMS:JAVADOC:891; JMS:JAVADOC:896; JMS:JAVADOC:901;
	 * JMS:JAVADOC:906;
	 * 
	 * @test_Strategy: Test for InvalidDestinationException from MessageProducer
	 * API's.
	 */
	@Test
	public void queueInvalidDestinationExceptionTests() throws Exception {
		boolean pass = true;
		TextMessage tempMsg = null;
		String message = "Where are you!";
		try {
			// set up test tool for Queue
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON QUEUE");
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			toolForProducer = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			producer = toolForProducer.getDefaultProducer();
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = null;
			connection.start();
			queueTest = true;
			MyCompletionListener listener = new MyCompletionListener();

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueInvalidDestinationExceptionTests");

			try {
				logger.log(Logger.Level.INFO, "Send message with invalid destination");
				producer.send(destination, expTextMessage, listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 30; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof InvalidDestinationException) {
						logger.log(Logger.Level.INFO, "Exception is expected InvalidDestinationException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected InvalidDestinationException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (InvalidDestinationException e) {
				logger.log(Logger.Level.INFO, "Caught expected InvalidDestinationException");
			} catch (UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationException, received " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Send message with invalid destination");
				producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 0L,
						listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 30; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof InvalidDestinationException) {
						logger.log(Logger.Level.INFO, "Exception is expected InvalidDestinationException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected InvalidDestinationException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (InvalidDestinationException e) {
				logger.log(Logger.Level.INFO, "Caught expected InvalidDestinationException");
			} catch (UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationException, received " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("queueInvalidDestinationExceptionTests", e);
		} finally {
			try {
				producer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("queueInvalidDestinationExceptionTests failed");
		}
	}

	/*
	 * @testName: queueUnsupportedOperationExceptionTests
	 * 
	 * @assertion_ids: JMS:JAVADOC:892; JMS:JAVADOC:897; JMS:JAVADOC:902;
	 * JMS:JAVADOC:1323;
	 * 
	 * @test_Strategy: Test for UnsupportedOperationException from MessageProducer
	 * API's.
	 */
	@Test
	public void queueUnsupportedOperationExceptionTests() throws Exception {
		boolean pass = true;
		TextMessage tempMsg = null;
		String message = "Where are you!";
		try {
			// set up test tool for Queue
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON QUEUE");
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			producer = tool.getDefaultProducer();
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = true;
			MyCompletionListener listener = new MyCompletionListener();

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueUnsupportedOperationExceptionTests");

			try {
				logger.log(Logger.Level.INFO, "Send message with invalid destination");
				producer.send(destination, expTextMessage, listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 30; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof UnsupportedOperationException) {
						logger.log(Logger.Level.INFO, "Exception is expected UnsupportedOperationException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected UnsupportedOperationException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected UnsupportedOperationException, received " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Send message with invalid destination");
				producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 0L,
						listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 30; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof UnsupportedOperationException) {
						logger.log(Logger.Level.INFO, "Exception is expected UnsupportedOperationException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected UnsupportedOperationException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected UnsupportedOperationException, received " + e);
				pass = false;
			}

			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Queue) null);

			try {
				logger.log(Logger.Level.INFO, "Send message with invalid destination");
				producer.send(expTextMessage, listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 30; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof UnsupportedOperationException) {
						logger.log(Logger.Level.INFO, "Exception is expected UnsupportedOperationException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected UnsupportedOperationException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected UnsupportedOperationException, received " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Send message with invalid destination");
				producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 0L, listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 30; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof UnsupportedOperationException) {
						logger.log(Logger.Level.INFO, "Exception is expected UnsupportedOperationException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected UnsupportedOperationException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected UnsupportedOperationException, received " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("queueUnsupportedOperationExceptionTests", e);
		} finally {
			try {
				producer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("queueUnsupportedOperationExceptionTests failed");
		}
	}

	/*
	 * @testName: queueIllegalArgumentExceptionTests
	 * 
	 * @assertion_ids: JMS:JAVADOC:1319; JMS:JAVADOC:1320; JMS:JAVADOC:1321;
	 * JMS:JAVADOC:1322;
	 * 
	 * @test_Strategy: Test for IllegalArgumentException from MessageProducer API's.
	 */
	@Test
	public void queueIllegalArgumentExceptionTests() throws Exception {
		boolean pass = true;
		TextMessage tempMsg = null;
		String message = "Where are you!";
		try {
			// set up test tool for Queue
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON QUEUE");
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Queue) null);
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = false;
			MyCompletionListener listener = new MyCompletionListener();

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueIllegalArgumentExceptionTests");

			try {
				logger.log(Logger.Level.INFO, "Send message with null CompletionListener");
				producer.send(destination, expTextMessage, null);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 30; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof IllegalArgumentException) {
						logger.log(Logger.Level.INFO, "Exception is expected IllegalArgumentException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected IllegalArgumentException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected IllegalArgumentException, received " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Send message with null CompletionListener");
				producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 0L,
						null);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 30; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof IllegalArgumentException) {
						logger.log(Logger.Level.INFO, "Exception is expected IllegalArgumentException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected IllegalArgumentException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected IllegalArgumentException, received " + e);
				pass = false;
			}

			producer.close();
			producer = tool.getDefaultSession().createProducer(destination);

			try {
				logger.log(Logger.Level.INFO, "Send message with null CompletionListener");
				producer.send(expTextMessage, null);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 30; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof IllegalArgumentException) {
						logger.log(Logger.Level.INFO, "Exception is expected IllegalArgumentException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected IllegalArgumentException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected IllegalArgumentException, received " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Send message with null CompletionListener");
				producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 0L, null);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 30; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof IllegalArgumentException) {
						logger.log(Logger.Level.INFO, "Exception is expected IllegalArgumentException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected IllegalArgumentException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected IllegalArgumentException, received " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("queueIllegalArgumentExceptionTests", e);
		} finally {
			try {
				producer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("queueIllegalArgumentExceptionTests failed");
		}
	}

	/*
	 * @testName: queueMessageFormatExceptionTests
	 * 
	 * @assertion_ids: JMS:JAVADOC:890; JMS:JAVADOC:895; JMS:JAVADOC:900;
	 * JMS:JAVADOC:905;
	 * 
	 * @test_Strategy: Test MessageFormatException conditions from API methods with
	 * CompletionListener.
	 * 
	 * MessageProducer.send(Message, CompletionListener)
	 * MessageProducer.send(Message, int, int, long, CompletionListener)
	 * MessageProducer.send(Destination, Message, CompletionListener)
	 * MessageProducer.send(Destination, Message, int, int, long,
	 * CompletionListener)
	 * 
	 * Tests the following exception conditions:
	 * 
	 * MessageFormatException
	 */
	@Test
	public void queueMessageFormatExceptionTests() throws Exception {
		boolean pass = true;
		try {
			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			producer = tool.getDefaultProducer();
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = true;

			TextMessage invalidTMsg = new InvalidTextMessageTestImpl();
			invalidTMsg.setText("hello");

			// Create CompletionListener
			MyCompletionListener listener = new MyCompletionListener();

			try {
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Message, CompletionListener) -> expect MessageFormatException");
				producer.send(invalidTMsg, listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 30; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof MessageFormatException) {
						logger.log(Logger.Level.INFO, "Exception is expected MessageFormatException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected MessageFormatException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (MessageFormatException e) {
				logger.log(Logger.Level.INFO, "Got MessageFormatException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected MessageFormatException, received " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Message, int, int, long, CompletionListener) -> expect MessageFormatException");
				producer.send(invalidTMsg, Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY - 1,
						Message.DEFAULT_TIME_TO_LIVE, listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 30; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof MessageFormatException) {
						logger.log(Logger.Level.INFO, "Exception is expected MessageFormatException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected MessageFormatException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (MessageFormatException e) {
				logger.log(Logger.Level.INFO, "Got MessageFormatException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected MessageFormatException, received " + e);
				pass = false;
			}

			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Queue) null);

			try {
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Destination, Message, CompletionListener) -> expect MessageFormatException");
				producer.send(destination, invalidTMsg, listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 30; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof MessageFormatException) {
						logger.log(Logger.Level.INFO, "Exception is expected MessageFormatException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected MessageFormatException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (MessageFormatException e) {
				logger.log(Logger.Level.INFO, "Got MessageFormatException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected MessageFormatException, received " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Destination, Message, int, int, long, CompletionListener) -> expect MessageFormatException");
				producer.send(destination, invalidTMsg, Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY - 1,
						Message.DEFAULT_TIME_TO_LIVE, listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 30; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof MessageFormatException) {
						logger.log(Logger.Level.INFO, "Exception is expected MessageFormatException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected MessageFormatException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (MessageFormatException e) {
				logger.log(Logger.Level.INFO, "Got MessageFormatException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected MessageFormatException, received " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("queueMessageFormatExceptionTests", e);
		}

		if (!pass) {
			throw new Exception("queueMessageFormatExceptionTests failed");
		}
	}

	/*
	 * @testName: queueIllegalStateExceptionTests
	 * 
	 * @assertion_ids: JMS:JAVADOC:1355;
	 * 
	 * @test_Strategy: Test IllegalStateException conditions. Calling
	 * MessageProducer.close() in CompletionListener MUST throw
	 * IllegalStateException.
	 */
	@Test
	public void queueIllegalStateExceptionTests() throws Exception {
		boolean pass = true;
		try {
			// set up test tool for Queue
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Queue) null);
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = true;

			// Create CompetionListener
			MyCompletionListener listener = new MyCompletionListener(producer);

			logger.log(Logger.Level.INFO,
					"Testing MessageProducer.close() from CompletionListener (expect IllegalStateException)");
			try {
				// Create TextMessage
				logger.log(Logger.Level.INFO, "Creating TextMessage");
				TextMessage expTextMessage = session.createTextMessage("Call close method");
				logger.log(Logger.Level.INFO, "Set some values in TextMessage");
				expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueIllegalStateExceptionTests");

				logger.log(Logger.Level.INFO,
						"Send async message specifying CompletionListener to recieve async message");
				logger.log(Logger.Level.INFO,
						"CompletionListener will call MessageProducer.close() (expect IllegalStateException)");
				producer.send(destination, expTextMessage, listener);
				TextMessage actTextMessage = null;
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				for (int i = 0; i < 30; i++) {
					if (listener.isComplete()) {
						listener.setComplete(false);
						break;
					} else {
						TestUtil.sleepSec(2);
					}
				}
				logger.log(Logger.Level.INFO, "Check if we got correct exception from MessageProducer.close()");
				if (listener.gotException()) {
					if (listener.gotCorrectException()) {
						logger.log(Logger.Level.INFO, "Got correct IllegalStateException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected IllegalStateException, received: " + listener.getException());
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected IllegalStateException, got no exception");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected IllegalStateException, received " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("queueIllegalStateExceptionTests", e);
		}

		if (!pass) {
			throw new Exception("queueIllegalStateExceptionTests failed");
		}
	}

	/*
	 * @testName: topicSendRecvCompletionListenerTest1
	 * 
	 * @assertion_ids: JMS:JAVADOC:898;
	 * 
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 * 
	 * MessageProducer.send(Destination, Message, CompletionListener)
	 */
	@Test
	public void topicSendRecvCompletionListenerTest1() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Topic) null);
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = false;

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicSendRecvCompletionListenerTest1");

			// Create CompletionListener for Message to be sent
			MyCompletionListener listener = new MyCompletionListener();

			logger.log(Logger.Level.INFO, "Calling send(Destination,Message,CompletionListener)");
			producer.send(destination, expTextMessage, listener);

			logger.log(Logger.Level.INFO, "Poll listener waiting for TestMessage to arrive");
			TextMessage actTextMessage = null;
			for (int i = 0; !listener.isComplete() && i < 60; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for messages to arrive at listener");
				TestUtil.sleepSec(2);
			}
			if (listener.isComplete())
				actTextMessage = (TextMessage) listener.getMessage();

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
			throw new Exception("topicSendRecvCompletionListenerTest1", e);
		} finally {
			try {
				producer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("topicSendRecvCompletionListenerTest1 failed");
		}
	}

	/*
	 * @testName: topicSendRecvCompletionListenerTest2
	 * 
	 * @assertion_ids: JMS:JAVADOC:903;
	 * 
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 * 
	 * MessageProducer.send(Destination, Message, int, int, long,
	 * CompletionListener)
	 */
	@Test
	public void topicSendRecvCompletionListenerTest2() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Topic) null);
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = false;

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicSendRecvCompletionListenerTest2");

			// Create CompletionListener for Message to be sent
			MyCompletionListener listener = new MyCompletionListener();

			logger.log(Logger.Level.INFO, "Calling send(Destination,Message,int,int,long,CompletionListener)");
			producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 0L,
					listener);

			logger.log(Logger.Level.INFO, "Poll listener waiting for TestMessage to arrive");
			TextMessage actTextMessage = null;
			for (int i = 0; !listener.isComplete() && i < 60; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for messages to arrive at listener");
				TestUtil.sleepSec(2);
			}
			if (listener.isComplete())
				actTextMessage = (TextMessage) listener.getMessage();

			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the values in TextMessage, deliverymode, priority, time to live");
			if (!actTextMessage.getText().equals(expTextMessage.getText())
					|| actTextMessage.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT
					|| actTextMessage.getJMSPriority() != Message.DEFAULT_PRIORITY
					|| actTextMessage.getJMSExpiration() != 0L) {
				logger.log(Logger.Level.ERROR, "Didn't get the right message.");
				logger.log(Logger.Level.ERROR,
						"text=" + actTextMessage.getText() + ", expected " + expTextMessage.getText());
				logger.log(Logger.Level.ERROR, "DeliveryMode=" + actTextMessage.getJMSDeliveryMode() + ", expected "
						+ expTextMessage.getJMSDeliveryMode());
				logger.log(Logger.Level.ERROR, "Priority=" + actTextMessage.getJMSPriority() + ", expected "
						+ expTextMessage.getJMSPriority());
				logger.log(Logger.Level.ERROR, "TimeToLive=" + actTextMessage.getJMSExpiration() + ", expected "
						+ expTextMessage.getJMSExpiration());
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("topicSendRecvCompletionListenerTest2", e);
		} finally {
			try {
				producer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("topicSendRecvCompletionListenerTest2 failed");
		}
	}

	/*
	 * @testName: topicSendRecvCompletionListenerTest3
	 * 
	 * @assertion_ids: JMS:JAVADOC:888;
	 * 
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 * 
	 * MessageProducer.send(Message, CompletionListener)
	 */
	@Test
	public void topicSendRecvCompletionListenerTest3() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			producer = tool.getDefaultProducer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = false;

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicSendRecvCompletionListenerTest3");

			// Create CompletionListener for Message to be sent
			MyCompletionListener listener = new MyCompletionListener();

			logger.log(Logger.Level.INFO, "Calling send(Message,CompletionListener)");
			producer.send(expTextMessage, listener);

			logger.log(Logger.Level.INFO, "Poll listener waiting for TestMessage to arrive");
			TextMessage actTextMessage = null;
			for (int i = 0; !listener.isComplete() && i < 60; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for messages to arrive at listener");
				TestUtil.sleepSec(2);
			}
			if (listener.isComplete())
				actTextMessage = (TextMessage) listener.getMessage();

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
			throw new Exception("topicSendRecvCompletionListenerTest3", e);
		}

		if (!pass) {
			throw new Exception("topicSendRecvCompletionListenerTest3 failed");
		}
	}

	/*
	 * @testName: topicSendRecvCompletionListenerTest4
	 * 
	 * @assertion_ids: JMS:JAVADOC:893;
	 * 
	 * @test_Strategy: Send a message using the following API method and verify the
	 * send and recv of data:
	 * 
	 * MessageProducer.send(Message, int, int, long, CompletionListener)
	 */
	@Test
	public void topicSendRecvCompletionListenerTest4() throws Exception {
		boolean pass = true;
		String message = "Where are you!";
		try {
			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			producer = tool.getDefaultProducer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = false;

			// send and receive TextMessage
			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicSendRecvCompletionListeneTest4");

			// Create CompletionListener for Message to be sent
			MyCompletionListener listener = new MyCompletionListener();

			logger.log(Logger.Level.INFO, "Calling send(Message,int,int,long,CompletionListener)");
			producer.send(expTextMessage, DeliveryMode.PERSISTENT, Message.DEFAULT_PRIORITY, 0L, listener);

			logger.log(Logger.Level.INFO, "Poll listener waiting for TestMessage to arrive");
			TextMessage actTextMessage = null;
			for (int i = 0; !listener.isComplete() && i < 60; i++) {
				logger.log(Logger.Level.INFO,
						"Loop " + i + ": sleep 2 seconds waiting for messages to arrive at listener");
				TestUtil.sleepSec(2);
			}
			if (listener.isComplete())
				actTextMessage = (TextMessage) listener.getMessage();

			if (actTextMessage == null) {
				throw new Exception("Did not receive TextMessage");
			}
			logger.log(Logger.Level.INFO, "Check the values in TextMessage, deliverymode, priority, time to live");
			if (!actTextMessage.getText().equals(expTextMessage.getText())
					|| actTextMessage.getJMSDeliveryMode() != DeliveryMode.PERSISTENT
					|| actTextMessage.getJMSPriority() != Message.DEFAULT_PRIORITY
					|| actTextMessage.getJMSExpiration() != 0L) {
				logger.log(Logger.Level.ERROR, "Didn't get the right message.");
				logger.log(Logger.Level.ERROR,
						"text=" + actTextMessage.getText() + ", expected " + expTextMessage.getText());
				logger.log(Logger.Level.ERROR, "DeliveryMode=" + actTextMessage.getJMSDeliveryMode() + ", expected "
						+ expTextMessage.getJMSDeliveryMode());
				logger.log(Logger.Level.ERROR, "Priority=" + actTextMessage.getJMSPriority() + ", expected "
						+ expTextMessage.getJMSPriority());
				logger.log(Logger.Level.ERROR, "TimeToLive=" + actTextMessage.getJMSExpiration() + ", expected "
						+ expTextMessage.getJMSExpiration());
				pass = false;
			} else {
				logger.log(Logger.Level.INFO, "TextMessage is correct");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			TestUtil.printStackTrace(e);
			throw new Exception("topicSendRecvCompletionListenerTest4", e);
		}

		if (!pass) {
			throw new Exception("topicSendRecvCompletionListenerTest4 failed");
		}
	}

	/*
	 * @testName: topicJMSExceptionTests
	 * 
	 * @assertion_ids: JMS:JAVADOC:904;
	 * 
	 * @test_Strategy: Test for JMSException from MessageProducer API's.
	 */
	@Test
	public void topicJMSExceptionTests() throws Exception {
		boolean pass = true;
		TextMessage tempMsg = null;
		String message = "Where are you!";
		try {
			// set up test tool for Queue
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON TOPIC");
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Topic) null);
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = false;

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicJMSExceptionTests");

			// Create CompletionListener for Message to be sent
			MyCompletionListener listener = new MyCompletionListener();

			try {
				logger.log(Logger.Level.INFO, "Try and set an invalid mode of -1 on send");
				producer.send(destination, expTextMessage, -1, Message.DEFAULT_PRIORITY, 0L, listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 10; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof JMSException) {
						logger.log(Logger.Level.INFO, "Exception is expected JMSException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected JMSException, received " + exception.getCause());
						pass = false;
					}
				}
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Try and set an invalid priority of -1 on send");
				producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT, -1, 0L, listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 10; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof JMSException) {
						logger.log(Logger.Level.INFO, "Exception is expected JMSException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected JMSException, received " + exception.getCause());
						pass = false;
					}
				}
			} catch (JMSException e) {
				logger.log(Logger.Level.INFO, "Caught expected JMSException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected JMSException, received " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("topicJMSExceptionTests", e);
		}

		if (!pass) {
			throw new Exception("topicJMSExceptionTests failed");
		}
	}

	/*
	 * @testName: topicInvalidDestinationExceptionTests
	 * 
	 * @assertion_ids: JMS:JAVADOC:891; JMS:JAVADOC:896; JMS:JAVADOC:901;
	 * JMS:JAVADOC:906;
	 * 
	 * @test_Strategy: Test for InvalidDestinationException from MessageProducer
	 * API's.
	 */
	@Test
	public void topicInvalidDestinationExceptionTests() throws Exception {
		boolean pass = true;
		TextMessage tempMsg = null;
		String message = "Where are you!";
		try {
			// set up test tool for Topic
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON TOPIC");
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			toolForProducer = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			producer = toolForProducer.getDefaultProducer();
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = null;
			connection.start();
			queueTest = false;
			MyCompletionListener listener = new MyCompletionListener();

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "queueInvalidDestinationExceptionTests");

			try {
				logger.log(Logger.Level.INFO, "Send message with invalid destination");
				producer.send(destination, expTextMessage, listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 10; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof InvalidDestinationException) {
						logger.log(Logger.Level.INFO, "Exception is expected InvalidDestinationException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected InvalidDestinationException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (InvalidDestinationException e) {
				logger.log(Logger.Level.INFO, "Caught expected InvalidDestinationException");
			} catch (UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationException, received " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Send message with invalid destination");
				producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 0L,
						listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 10; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof InvalidDestinationException) {
						logger.log(Logger.Level.INFO, "Exception is expected InvalidDestinationException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected InvalidDestinationException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (InvalidDestinationException e) {
				logger.log(Logger.Level.INFO, "Caught expected InvalidDestinationException");
			} catch (UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected InvalidDestinationException, received " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("queueInvalidDestinationExceptionTests", e);
		} finally {
			try {
				producer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("topicInvalidDestinationExceptionTests failed");
		}
	}

	/*
	 * @testName: topicUnsupportedOperationExceptionTests
	 * 
	 * @assertion_ids: JMS:JAVADOC:892; JMS:JAVADOC:897; JMS:JAVADOC:902;
	 * JMS:JAVADOC:1323;
	 * 
	 * @test_Strategy: Test for UnsupportedOperationException from MessageProducer
	 * API's.
	 */
	@Test
	public void topicUnsupportedOperationExceptionTests() throws Exception {
		boolean pass = true;
		TextMessage tempMsg = null;
		String message = "Where are you!";
		try {
			// set up test tool for Topic
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON TOPIC");
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			producer = tool.getDefaultProducer();
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = false;
			MyCompletionListener listener = new MyCompletionListener();

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicUnsupportedOperationExceptionTests");

			try {
				logger.log(Logger.Level.INFO, "Send message with invalid destination");
				producer.send(destination, expTextMessage, listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 10; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof UnsupportedOperationException) {
						logger.log(Logger.Level.INFO, "Exception is expected UnsupportedOperationException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected UnsupportedOperationException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected UnsupportedOperationException, received " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Send message with invalid destination");
				producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 0L,
						listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 10; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof UnsupportedOperationException) {
						logger.log(Logger.Level.INFO, "Exception is expected UnsupportedOperationException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected UnsupportedOperationException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected UnsupportedOperationException, received " + e);
				pass = false;
			}

			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Topic) null);

			try {
				logger.log(Logger.Level.INFO, "Send message with invalid destination");
				producer.send(expTextMessage, listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 10; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof UnsupportedOperationException) {
						logger.log(Logger.Level.INFO, "Exception is expected UnsupportedOperationException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected UnsupportedOperationException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected UnsupportedOperationException, received " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Send message with invalid destination");
				producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 0L, listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 10; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof UnsupportedOperationException) {
						logger.log(Logger.Level.INFO, "Exception is expected UnsupportedOperationException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected UnsupportedOperationException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (UnsupportedOperationException e) {
				logger.log(Logger.Level.INFO, "Caught expected UnsupportedOperationException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected UnsupportedOperationException, received " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("queueUnsupportedOperationExceptionTests", e);
		} finally {
			try {
				producer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("topicUnsupportedOperationExceptionTests failed");
		}
	}

	/*
	 * @testName: topicIllegalArgumentExceptionTests
	 * 
	 * @assertion_ids: JMS:JAVADOC:1319; JMS:JAVADOC:1320; JMS:JAVADOC:1321;
	 * JMS:JAVADOC:1322;
	 * 
	 * @test_Strategy: Test for IllegalArgumentException from MessageProducer API's.
	 */
	@Test
	public void topicIllegalArgumentExceptionTests() throws Exception {
		boolean pass = true;
		TextMessage tempMsg = null;
		String message = "Where are you!";
		try {
			// set up test tool for Topic
			logger.log(Logger.Level.INFO, "Setup JmsTool for COMMON TOPIC");
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Topic) null);
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = false;
			MyCompletionListener listener = new MyCompletionListener();

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			TextMessage expTextMessage = session.createTextMessage(message);
			logger.log(Logger.Level.INFO, "Set some values in TextMessage");
			expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicIllegalArgumentExceptionTests");

			try {
				logger.log(Logger.Level.INFO, "Send message with null CompletionListener");
				producer.send(destination, expTextMessage, null);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 10; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof IllegalArgumentException) {
						logger.log(Logger.Level.INFO, "Exception is expected IllegalArgumentException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected IllegalArgumentException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected IllegalArgumentException, received " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Send message with null CompletionListener");
				producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 0L,
						null);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 10; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof IllegalArgumentException) {
						logger.log(Logger.Level.INFO, "Exception is expected IllegalArgumentException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected IllegalArgumentException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected IllegalArgumentException, received " + e);
				pass = false;
			}

			producer.close();
			producer = tool.getDefaultSession().createProducer(destination);

			try {
				logger.log(Logger.Level.INFO, "Send message with null CompletionListener");
				producer.send(expTextMessage, null);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 10; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof IllegalArgumentException) {
						logger.log(Logger.Level.INFO, "Exception is expected IllegalArgumentException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected IllegalArgumentException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected IllegalArgumentException, received " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO, "Send message with null CompletionListener");
				producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT, Message.DEFAULT_PRIORITY, 0L, null);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				Exception exception = null;
				for (int i = 0; i < 10; i++) {
					TestUtil.sleepSec(2);
					if (listener.isComplete()) {
						listener.setComplete(false);
						exception = listener.getException();
						logger.log(Logger.Level.INFO, "Received Exception after polling loop " + (i + 1));
						break;
					}
					logger.log(Logger.Level.INFO, "Completed polling loop " + i);
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof IllegalArgumentException) {
						logger.log(Logger.Level.INFO, "Exception is expected IllegalArgumentException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected IllegalArgumentException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (IllegalArgumentException e) {
				logger.log(Logger.Level.INFO, "Caught expected IllegalArgumentException");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected IllegalArgumentException, received " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught exception: " + e);
			throw new Exception("queueIllegalArgumentExceptionTests", e);
		} finally {
			try {
				producer.close();
			} catch (Exception e) {
			}
		}

		if (!pass) {
			throw new Exception("topicIllegalArgumentExceptionTests failed");
		}
	}

	/*
	 * @testName: topicMessageFormatExceptionTests
	 * 
	 * @assertion_ids: JMS:JAVADOC:890; JMS:JAVADOC:895; JMS:JAVADOC:900;
	 * JMS:JAVADOC:905;
	 * 
	 * @test_Strategy: Test MessageFormatException conditions from API methods with
	 * CompletionListener.
	 * 
	 * MessageProducer.send(Message, CompletionListener)
	 * MessageProducer.send(Message, int, int, long, CompletionListener)
	 * MessageProducer.send(Destination, Message, CompletionListener)
	 * MessageProducer.send(Destination, Message, int, int, long,
	 * CompletionListener)
	 * 
	 * Tests the following exception conditions:
	 * 
	 * MessageFormatException
	 */
	@Test
	public void topicMessageFormatExceptionTests() throws Exception {
		boolean pass = true;
		try {
			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			producer = tool.getDefaultProducer();
			consumer = tool.getDefaultConsumer();
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = false;

			TextMessage invalidTMsg = new InvalidTextMessageTestImpl();
			invalidTMsg.setText("hello");

			// Create CompletionListener
			MyCompletionListener listener = new MyCompletionListener();

			try {
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Message, CompletionListener) -> expect MessageFormatException");
				producer.send(invalidTMsg, listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				TestUtil.sleepSec(5);
				Exception exception = null;
				if (listener.isComplete()) {
					listener.setComplete(false);
					exception = listener.getException();
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof MessageFormatException) {
						logger.log(Logger.Level.INFO, "Exception is expected MessageFormatException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected MessageFormatException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (MessageFormatException e) {
				logger.log(Logger.Level.INFO, "Got MessageFormatException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected MessageFormatException, received " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Message, int, int, long, CompletionListener) -> expect MessageFormatException");
				producer.send(invalidTMsg, Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY - 1,
						Message.DEFAULT_TIME_TO_LIVE, listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				TestUtil.sleepSec(5);
				Exception exception = null;
				if (listener.isComplete()) {
					listener.setComplete(false);
					exception = listener.getException();
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof MessageFormatException) {
						logger.log(Logger.Level.INFO, "Exception is expected MessageFormatException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected MessageFormatException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (MessageFormatException e) {
				logger.log(Logger.Level.INFO, "Got MessageFormatException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected MessageFormatException, received " + e);
				pass = false;
			}

			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Queue) null);

			try {
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Destination, Message, CompletionListener) -> expect MessageFormatException");
				producer.send(destination, invalidTMsg, listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				TestUtil.sleepSec(5);
				Exception exception = null;
				if (listener.isComplete()) {
					listener.setComplete(false);
					exception = listener.getException();
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof MessageFormatException) {
						logger.log(Logger.Level.INFO, "Exception is expected MessageFormatException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected MessageFormatException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (MessageFormatException e) {
				logger.log(Logger.Level.INFO, "Got MessageFormatException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected MessageFormatException, received " + e);
				pass = false;
			}
			try {
				logger.log(Logger.Level.INFO,
						"Calling MessageProducer.send(Destination, Message, int, int, long, CompletionListener) -> expect MessageFormatException");
				producer.send(destination, invalidTMsg, Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY - 1,
						Message.DEFAULT_TIME_TO_LIVE, listener);
				logger.log(Logger.Level.INFO, "Poll listener until we receive exception");
				TestUtil.sleepSec(5);
				Exception exception = null;
				if (listener.isComplete()) {
					listener.setComplete(false);
					exception = listener.getException();
				}

				if (exception == null) {
					pass = false;
					logger.log(Logger.Level.ERROR, "Didn't throw and exception");
				} else {
					logger.log(Logger.Level.INFO, "Check the value in Exception");
					if (exception instanceof MessageFormatException) {
						logger.log(Logger.Level.INFO, "Exception is expected MessageFormatException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Exception is incorrect expected MessageFormatException, received "
										+ exception.getCause());
						pass = false;
					}
				}
			} catch (MessageFormatException e) {
				logger.log(Logger.Level.INFO, "Got MessageFormatException as expected.");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected MessageFormatException, received " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("topicMessageFormatExceptionTests", e);
		}

		if (!pass) {
			throw new Exception("topicMessageFormatExceptionTests failed");
		}
	}

	/*
	 * @testName: topicIllegalStateExceptionTests
	 * 
	 * @assertion_ids: JMS:JAVADOC:1355;
	 * 
	 * @test_Strategy: Test IllegalStateException conditions. Calling
	 * MessageProducer.close() in CompletionListener MUST throw
	 * IllegalStateException.
	 */
	@Test
	public void topicIllegalStateExceptionTests() throws Exception {
		boolean pass = true;
		try {
			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
			tool.getDefaultProducer().close();
			producer = tool.getDefaultSession().createProducer((Topic) null);
			connection = tool.getDefaultConnection();
			session = tool.getDefaultSession();
			destination = tool.getDefaultDestination();
			connection.start();
			queueTest = false;

			// Create CompetionListener
			MyCompletionListener listener = new MyCompletionListener(producer);

			logger.log(Logger.Level.INFO,
					"Testing MessageProducer.close() from CompletionListener (expect IllegalStateException)");
			try {
				// Create TextMessage
				logger.log(Logger.Level.INFO, "Creating TextMessage");
				TextMessage expTextMessage = session.createTextMessage("Call close method");
				logger.log(Logger.Level.INFO, "Set some values in TextMessage");
				expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME", "topicIllegalStateExceptionTests");

				logger.log(Logger.Level.INFO,
						"Send async message specifying CompletionListener to recieve async message");
				logger.log(Logger.Level.INFO,
						"CompletionListener will call MessageProducer.close() (expect IllegalStateException)");
				producer.send(destination, expTextMessage, listener);
				TextMessage actTextMessage = null;
				logger.log(Logger.Level.INFO, "Poll listener until we receive message or exception");
				for (int i = 0; i < 10; i++) {
					if (listener.isComplete()) {
						listener.setComplete(false);
						break;
					} else {
						TestUtil.sleepSec(2);
					}
				}
				logger.log(Logger.Level.INFO, "Check if we got correct exception from MessageProducer.close()");
				if (listener.gotException()) {
					if (listener.gotCorrectException()) {
						logger.log(Logger.Level.INFO, "Got correct IllegalStateException");
					} else {
						logger.log(Logger.Level.ERROR,
								"Expected IllegalStateException, received: " + listener.getException());
						pass = false;
					}
				} else {
					logger.log(Logger.Level.ERROR, "Expected IllegalStateException, got no exception");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Expected IllegalStateException, received " + e);
				pass = false;
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Caught unexpected exception: " + e);
			throw new Exception("topicIllegalStateExceptionTests", e);
		}

		if (!pass) {
			throw new Exception("topicIllegalStateExceptionTests failed");
		}
	}
}
