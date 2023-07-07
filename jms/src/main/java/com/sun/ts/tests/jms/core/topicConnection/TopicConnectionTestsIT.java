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
package com.sun.ts.tests.jms.core.topicConnection;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.ConnectionMetaData;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;
import jakarta.jms.TopicConnection;
import jakarta.jms.TopicSession;
import jakarta.jms.TopicSubscriber;

public class TopicConnectionTestsIT {
	private static final String testName = "com.sun.ts.tests.jms.core.topicConnection.TopicConnectionTestsIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(TopicConnectionTestsIT.class.getName());

	// Harness req's
	private Properties props = null;

	// JMS object
	private transient JmsTool tool = null;

	// properties read
	long timeout;

	private String jmsUser;

	private String jmsPassword;

	private String mode;

	ArrayList connections = null;

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
			throw new Exception("Didn't catch expected exception");
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
			logger.log(Logger.Level.TRACE, "In setup");
			// get props
			jmsUser = System.getProperty("user");
			jmsPassword = System.getProperty("password");
			mode = System.getProperty("platform.mode");
			timeout = Long.parseLong(System.getProperty("jms_timeout"));
			if (timeout < 1) {
				throw new Exception("'timeout' (milliseconds) in must be > 0");
			}
			connections = new ArrayList(5);

			// get ready for new test
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
				logger.log(Logger.Level.INFO, "Cleanup: Closing Topic Connections");
				tool.closeAllConnections(connections);
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			logger.log(Logger.Level.ERROR, "An error occurred while cleaning");
			throw new Exception("Cleanup failed!", e);
		}
	}

	/* Tests */

	/*
	 * @testName: connNotStartedTopicTest
	 *
	 * @assertion_ids: JMS:SPEC:97; JMS:JAVADOC:522;
	 *
	 * @test_Strategy: Make certain that one can not receive a message on a
	 * non-started connection. Setup to test assertion involves a best effort
	 * attempt to ensure that subscriber has a message to deliver but can not
	 * because the connection has never been started. Create two subscribers to the
	 * same topic on different connections. Only start one of the connections.
	 * Publish messages to the topic. Receive the messages on the started
	 * connection. Make sure the message is not available to be received on the
	 * non-started connection. Start the previously non-started connection and make
	 * sure that the message are now received.
	 *
	 */
	@Test
	public void connNotStartedTopicTest() throws Exception {
		boolean pass = true;
		String lookup = "MyTopicConnectionFactory";
		final int NUM_MSGS = 5;

		try {
			Topic topic = null;
			TextMessage messageSent = null;
			TextMessage messageReceived = null;

			TopicConnection nonStartedConn = null;
			TopicSession nonStartedSess = null;
			TopicSubscriber nonStartedSub = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, mode);
			topic = tool.getDefaultTopic();

			nonStartedConn = (TopicConnection) tool.getNewConnection(JmsTool.TOPIC, jmsUser, jmsPassword, lookup);
			connections.add(nonStartedConn);
			nonStartedSess = nonStartedConn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			nonStartedSub = nonStartedSess.createSubscriber(topic);

			tool.getDefaultTopicConnection().start();

			logger.log(Logger.Level.INFO, "Creating message");
			messageSent = tool.getDefaultTopicSession().createTextMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "connNotStartedTopicTest");

			logger.log(Logger.Level.INFO, "Publishing messages");
			for (int i = 0; i < NUM_MSGS; i++) {
				messageSent.setText("just a test. Msg num " + i);
				tool.getDefaultTopicPublisher().publish(messageSent);
			}

			// receive messages on started connection.
			logger.log(Logger.Level.INFO, "Receive  messages on started connection");
			for (int i = 0; i < NUM_MSGS; i++) {
				messageReceived = (TextMessage) tool.getDefaultTopicSubscriber().receive(timeout);
				if (messageReceived == null) {
					logger.log(Logger.Level.INFO, "Fail: Did not receive expected message");
					pass = false;
				}
			}

			// ensure that can not receive messages on non-started connection.
			logger.log(Logger.Level.INFO, "Ensure messages not received on non-started connection");
			for (int i = 0; i < NUM_MSGS; i++) {
				messageReceived = (TextMessage) nonStartedSub.receiveNoWait();
				if (messageReceived != null) {
					logger.log(Logger.Level.INFO, "Fail: Received message on a non-started connection");
					pass = false;
				}
			}

			// Start connection and verify messages can be received.
			logger.log(Logger.Level.INFO, "Ensure messages received on now started connection");
			nonStartedConn.start();
			for (int i = 0; i < NUM_MSGS; i++) {
				messageReceived = (TextMessage) nonStartedSub.receive(timeout);
				if (messageReceived == null) {
					logger.log(Logger.Level.INFO, "Fail: Did not receive expected message");
					pass = false;
				}
			}
			nonStartedConn.close();

			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("connNotStartedTopicTest");
		}
	}

	/*
	 * @testName: metaDataTests
	 * 
	 * @assertion_ids: JMS:JAVADOC:486; JMS:JAVADOC:488; JMS:JAVADOC:490;
	 * JMS:JAVADOC:492; JMS:JAVADOC:494; JMS:JAVADOC:496; JMS:JAVADOC:498;
	 * 
	 * @test_Strategy: Create a TopicConnection to get ConnectionMetaData. Verify
	 * that all Content of the ConnectionMetaData matches the type defined in API
	 * and JMS API version is correct.
	 */
	@Test
	public void metaDataTests() throws Exception {
		boolean pass = true;
		ConnectionMetaData data = null;

		try {
			tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, mode);
			data = tool.getDefaultTopicConnection().getMetaData();

			if (!verifyMetaData(data))
				pass = false;

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			pass = false;
		} finally {
			try {
				tool.getDefaultTopicConnection().close();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error closing TopicConnection in metaDataTests : ", e);
			}
		}

		try {
			tool = new JmsTool(JmsTool.COMMON_T, jmsUser, jmsPassword, mode);
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
