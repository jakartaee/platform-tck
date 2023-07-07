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
package com.sun.ts.tests.jms.core.objectMsgTopic;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.ObjectMessage;

public class ObjectMsgTopicTestIT {
	private static final String testName = "com.sun.ts.tests.jms.core.objectMsgTopic.ObjectMsgTopicTestIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(ObjectMsgTopicTestIT.class.getName());

	// JMS objects
	private transient JmsTool tool = null;

	// Harness req's
	private Properties props = null;

	// properties read
	long timeout;

	String user;

	String password;

	String mode;

	ArrayList connections = null;

	/* Test setup: */

	/*
	 * setup() is called before each test
	 * 
	 * Creates Administrator object and deletes all previous Destinations.
	 * Individual tests create the JmsTool object with one default Queue and/or
	 * Topic Connection, as well as a default Queue and Topic. Tests that require
	 * multiple Destinations create the extras within the test
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
				throw new Exception("'user' is null");
			}
			if (password == null) {
				throw new Exception("'password' is null");
			}
			if (mode == null) {
				throw new Exception("'platform.mode' is null");
			}

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
				logger.log(Logger.Level.INFO, "Cleanup: Closing Queue and Topic Connections");
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
	 * @testName: messageObjectCopyTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:85; JMS:JAVADOC:291;
	 *
	 * @test_Strategy: Create an object message. Write a StringBuffer to the
	 * message. modify the StringBuffer and send the msg, verify that it does not
	 * effect the msg
	 */
	@Test
	public void messageObjectCopyTopicTest() throws Exception {
		boolean pass = true;

		try {
			ObjectMessage messageSentObjectMsg = null;
			ObjectMessage messageReceivedObjectMsg = null;
			StringBuffer sBuff = new StringBuffer("This is");
			String initial = "This is";

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
			tool.getDefaultTopicConnection().start();
			messageSentObjectMsg = tool.getDefaultTopicSession().createObjectMessage();
			messageSentObjectMsg.setObject(sBuff);
			sBuff.append("a test ");
			messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "messageObjectCopyTopicTest");
			tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
			messageReceivedObjectMsg = (ObjectMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			logger.log(Logger.Level.INFO, "Ensure that changing the object did not change the message");
			StringBuffer s = (StringBuffer) messageReceivedObjectMsg.getObject();

			logger.log(Logger.Level.TRACE, "s is " + s);
			if (s.toString().equals(initial)) {
				logger.log(Logger.Level.TRACE, "Pass: msg was not changed");
			} else {
				logger.log(Logger.Level.TRACE, "Fail: msg was changed!");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("messageObjectCopyTopicTest");
		}
	}

}
