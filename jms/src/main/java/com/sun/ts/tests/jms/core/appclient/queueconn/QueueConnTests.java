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
package com.sun.ts.tests.jms.core.appclient.queueconn;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.QueueConnection;
import jakarta.jms.QueueReceiver;
import jakarta.jms.QueueSender;
import jakarta.jms.QueueSession;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

public class QueueConnTests {
	private static final String testName = "com.sun.ts.tests.jms.core.appclient.queueconn.QueueConnTests";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(QueueConnTests.class.getName());

	// JMS objects
	private transient JmsTool tool = null;

	// Harness req's
	private Properties props = null;

	// properties read
	long timeout;

	String user;

	String password;

	String mode;

	ArrayList queues = null;

	ArrayList connections = null;

	/* Test setup: */

	/*
	 * setup() is called before each test
	 * 
	 * Creates Administrator object and deletes all previous Destinations.
	 * Individual tests create the JmsTool object with one default Queue Connection,
	 * as well as a default Queue and Topic. Tests that require multiple
	 * Destinations create the extras within the test
	 * 
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
				throw new Exception("'timeout' (milliseconds) must be > 0");
			}
			if (user == null) {
				throw new Exception("'user' must be null");
			}
			if (password == null) {
				throw new Exception("'password' must be null");
			}
			if (mode == null) {
				throw new Exception("'mode' must be null");
			}
			queues = new ArrayList(2);
			connections = new ArrayList(5);

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("Setup failed!", e);
		}
	}

	/* cleanup */

	/*
	 * cleanup() is called after each test
	 * 
	 * Closes the default connections that are created by setup(). Any separate
	 * connections made by individual tests should be closed by that test.
	 * 
	 * @exception Fault
	 */
	@AfterEach
	public void cleanup() throws Exception {
		try {
			if (tool != null) {
				logger.log(Logger.Level.INFO, "Cleanup: Closing Queue Connections");
				tool.doClientQueueTestCleanup(connections, queues);
			}

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			logger.log(Logger.Level.ERROR, "An error occurred while cleaning");
			throw new Exception("Cleanup failed!", e);
		}
	}

	/* Tests */

	/*
	 * @testName: connStoppedQueueTest
	 *
	 * @assertion_ids: JMS:JAVADOC:272; JMS:SPEC:100; JMS:SPEC:98; JMS:SPEC:99;
	 * JMS:JAVADOC:522; JMS:JAVADOC:524; JMS:JAVADOC:120; JMS:JAVADOC:221;
	 * JMS:JAVADOC:198; JMS:JAVADOC:334;
	 * 
	 * @test_Strategy: Stop the connection. Send a msg; start the connection and
	 * receive the msg. Should get the message
	 */
	@Test
	public void connStoppedQueueTest() throws Exception {
		boolean pass = true;

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			tool.getDefaultQueueConnection().stop();

			logger.log(Logger.Level.TRACE, "Creating 1 TextMessage");
			messageSent = tool.getDefaultQueueSession().createTextMessage();
			messageSent.setText("Message from connStoppedQueueTest");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "connStoppedQueueTest");

			logger.log(Logger.Level.TRACE, "Sending a TextMessage");
			tool.getDefaultQueueSender().send(messageSent);

			logger.log(Logger.Level.TRACE, "Receiving message");
			tool.getDefaultQueueConnection().start();
			messageReceived = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);
			if (messageReceived.getText().equals(messageSent.getText())) {
				logger.log(Logger.Level.INFO, "Pass: Received correct message");
			} else {
				throw new Exception("didn't get the right message");
			}

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "connStoppedQueueTest failed: ", e);
			throw new Exception("connStoppedQueueTest", e);
		}
	}

	/*
	 * @testName: closedQueueConnectionNoForcedAckTest
	 *
	 * @assertion_ids: JMS:JAVADOC:272; JMS:SPEC:105; JMS:JAVADOC:429; JMS:SPEC:115;
	 *
	 * @test_Strategy: Send and receive single message, don't acknowledge it. close
	 * the queue connection, get the message with a second connection.
	 */
	@Test
	public void closedQueueConnectionNoForcedAckTest() throws Exception {
		boolean pass = true;

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			QueueSession qSession = null;
			QueueReceiver qReceiver = null;
			QueueSender qSender = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);

			QueueConnection newConn = (QueueConnection) tool.getNewConnection(JmsTool.QUEUE, user, password);
			connections.add(newConn);

			qSession = newConn.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
			tool.getDefaultQueueReceiver().close();
			tool.getDefaultQueueSession().close();

			qReceiver = qSession.createReceiver(tool.getDefaultQueue());
			qSender = qSession.createSender(tool.getDefaultQueue());
			logger.log(Logger.Level.INFO, "create a new connection");
			newConn.start();

			logger.log(Logger.Level.INFO, "Creating 1 TextMessage");
			messageSent = qSession.createTextMessage();
			messageSent.setText("just a test");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "closedQueueConnectionNoForcedAckTest");
			logger.log(Logger.Level.INFO, "Sending a TextMessage");
			qSender.send(messageSent);

			logger.log(Logger.Level.INFO, "Receive the TextMessage");
			messageReceived = (TextMessage) qReceiver.receive(timeout);
			qReceiver.close();
			logger.log(Logger.Level.INFO, "Close the connection with no ack of message received");
			newConn.close();

			logger.log(Logger.Level.INFO, "Use default connection to retrieve the unacknowledged message");
			qSession = tool.getDefaultQueueConnection().createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
			qReceiver = qSession.createReceiver(tool.getDefaultQueue());
			tool.getDefaultQueueConnection().start();

			messageReceived = (TextMessage) qReceiver.receive(timeout);
			if (messageReceived == null) {
				logger.log(Logger.Level.ERROR, "Fail: no message received.");
				pass = false;
			} else if (messageReceived.getText().equals(messageSent.getText())) {
				logger.log(Logger.Level.INFO, "Pass: received correct msg");
			} else {
				logger.log(Logger.Level.ERROR, "Fail: didnt get correct msg");
				pass = false;
			}

			try {
				messageReceived.acknowledge();
			} catch (Exception e) {
				pass = false;
				logger.log(Logger.Level.ERROR, "Exception thrown on ack!", e);
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("closedQueueConnectionNoForcedAckTest");
		}
	}

}
