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
package com.sun.ts.tests.jms.core.queueConnection;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.ConnectionMetaData;
import jakarta.jms.QueueConnection;
import jakarta.jms.QueueReceiver;
import jakarta.jms.QueueSession;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

public class QueueConnectionTests {
	private static final String testName = "com.sun.ts.tests.jms.core.queueConnection.QueueConnectionTests";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(QueueConnectionTests.class.getName());

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
				throw new Exception("'jms_timeout' (milliseconds) in must be > 0");
			}
			if (user == null) {
				throw new Exception("'user' in must not be null");
			}
			if (password == null) {
				throw new Exception("'password' in must not be null");
			}
			if (mode == null) {
				throw new Exception("'platform.mode' in must not be null");
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
	 * @testName: connNotStartedQueueTest
	 *
	 * @assertion_ids: JMS:JAVADOC:272; JMS:SPEC:97;
	 *
	 * @test_Strategy: Send two messages to a queue; Receive only the second message
	 * using selectors; Close receiver; Create another connection without starting
	 * it; Create a new receiver in new connection and try to receive first message
	 * with receiveNoWait() Should not get a message
	 */
	@Test
	public void connNotStartedQueueTest() throws Exception {
		boolean pass = true;

		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			QueueReceiver qRec = null;

			QueueConnection newConn = null;
			QueueSession newSess = null;
			QueueReceiver newRec = null;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueReceiver().close();
			qRec = tool.getDefaultQueueSession().createReceiver(tool.getDefaultQueue(), "targetMessage = TRUE");
			tool.getDefaultQueueConnection().start();

			logger.log(Logger.Level.INFO, "Creating TextMessage");
			messageSent = tool.getDefaultQueueSession().createTextMessage();
			messageSent.setText("just a test");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "connNotStartedQueueTest");

			logger.log(Logger.Level.INFO, "Sending two TextMessages");
			messageSent.setBooleanProperty("targetMessage", false);
			tool.getDefaultQueueSender().send(messageSent);
			messageSent.setBooleanProperty("targetMessage", true);
			tool.getDefaultQueueSender().send(messageSent);

			logger.log(Logger.Level.INFO, "Receiving second message only");
			messageReceived = (TextMessage) qRec.receive(timeout);
			if (messageReceived == null) {
				logger.log(Logger.Level.ERROR, "Fail: Did not receive any message");
				pass = false;
			} else if (messageReceived.getBooleanProperty("targetMessage") == false) {
				logger.log(Logger.Level.ERROR, "Fail: Received incorrect message");
				pass = false;
			}

			logger.log(Logger.Level.INFO, "Closing receiver, creating new connection without starting");
			qRec.close();
			tool.getDefaultQueueConnection().close();

			newConn = (QueueConnection) tool.getNewConnection(JmsTool.QUEUE, user, password);
			connections.add(newConn);

			newSess = newConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			newRec = newSess.createReceiver(tool.getDefaultQueue());

			logger.log(Logger.Level.INFO, "receiving first message");
			messageReceived = (TextMessage) newRec.receiveNoWait();

			// message should be null
			if (messageReceived == null) {
				logger.log(Logger.Level.INFO, "Pass: message not recevied");
			} else {
				logger.log(Logger.Level.ERROR, "Fail: message received even though connection not started!");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("connNotStartedQueueTest");
		}
	}

	/*
	 * @testName: metaDataTests
	 * 
	 * @assertion_ids: JMS:JAVADOC:486; JMS:JAVADOC:488; JMS:JAVADOC:490;
	 * JMS:JAVADOC:492; JMS:JAVADOC:494; JMS:JAVADOC:496; JMS:JAVADOC:498;
	 * 
	 * @test_Strategy: Create a QueueConnection to get ConnectionMetaData. Verify
	 * that all Content of the ConnectionMetaData matches the type defined in API
	 * and JMS API version is correct.
	 */
	@Test
	public void metaDataTests() throws Exception {
		boolean pass = true;
		ConnectionMetaData data = null;

		try {
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			data = tool.getDefaultQueueConnection().getMetaData();

			if (!verifyMetaData(data))
				pass = false;

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			pass = false;
		} finally {
			try {
				tool.getDefaultQueueConnection().close();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error closing QueueConnection in metaDataTests : ", e);
			}
		}

		try {
			tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
			data = tool.getDefaultConnection().getMetaData();

			if (!verifyMetaData(data))
				pass = false;

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			pass = false;
		} finally {
			try {
				tool.getDefaultConnection().close();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error closing Connection in metaDataTests : ", e);
			}
		}

		if (!pass) {
			throw new Exception("Error: metaDataTests failed");
		}
	}

	private boolean verifyMetaData(ConnectionMetaData data) {
		boolean pass = true;

		try {
			String tmp = data.getJMSVersion();
			logger.log(Logger.Level.TRACE, "JMSVersion=" + tmp);

			if (!tmp.equals(JmsTool.JMS_VERSION)) {
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

			if (tmp != JmsTool.JMS_MAJOR_VERSION) {
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
}
