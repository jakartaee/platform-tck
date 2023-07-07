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
package com.sun.ts.tests.jms.core.appclient.closedTopicSession;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.Message;
import jakarta.jms.MessageListener;

public class ClosedTopicSessionTestsIT {
	private static final String testName = "com.sun.ts.tests.jms.ee.ejbjspservlet.closedTopicSession.ClosedTopicSessionTestsIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final Logger logger = (Logger) System.getLogger(ClosedTopicSessionTestsIT.class.getName());

	// Harness req's
	private Properties props = null;

	// JMS object
	private static JmsTool tool = null;

	// properties read
	long timeout;

	private String jmsUser;

	private String jmsPassword;

	private String mode;

	ArrayList connections = null;

	/* Utility methods for tests */

	/**
	 * Used by tests that need a closed session for testing. Passes any exceptions
	 * up to caller.
	 * 
	 * @param int The type of session that needs to be created and closed
	 */
	private void createAndCloseSession(int type) throws Exception {
		if ((type == JmsTool.TOPIC) || (type == JmsTool.TX_TOPIC)) {
			tool = new JmsTool(type, jmsUser, jmsPassword, mode);
			tool.getDefaultTopicConnection().start();
			logger.log(Logger.Level.INFO, "Closing topic session");
			tool.getDefaultTopicSession().close();
		}
		logger.log(Logger.Level.INFO, "Session closed");
	}

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
			timeout = Long.parseLong(System.getProperty("jms_timeout"));
			mode = System.getProperty("platform.mode");
			if (timeout < 1) {
				throw new Exception("'timeout' (milliseconds) in must be > 0");
			}

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
	 * @testName: closedTopicSessionSetMessageListenerTest
	 *
	 * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:330; JMS:JAVADOC:325;
	 * 
	 * @test_Strategy: Close default subscriber and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedTopicSessionSetMessageListenerTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSession(JmsTool.TOPIC);
			logger.log(Logger.Level.TRACE, "Try to call setMessageListener");
			try {
				MessageListener foo = new MessageListener() {
					public void onMessage(Message m) {
					}

				};

				tool.getDefaultTopicSubscriber().setMessageListener(foo);
				logger.log(Logger.Level.TRACE, "Fail: Exception was not thrown!");
			} catch (jakarta.jms.IllegalStateException ise) {
				logger.log(Logger.Level.TRACE, "Pass: threw expected error");
				passed = true;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Fail: wrong exception: " + e.getClass().getName() + " was returned");
			}
			if (!passed) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			throw new Exception("closedTopicSessionSetMessageListenerTest", e);
		}
	}

	/*
	 * @testName: closedTopicSessionGetMessageListenerTest
	 *
	 * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:328;
	 *
	 * @test_Strategy: Close default subscriber and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedTopicSessionGetMessageListenerTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSession(JmsTool.TOPIC);
			logger.log(Logger.Level.TRACE, "Try to call getMessageListener");
			try {
				MessageListener foo = tool.getDefaultTopicSubscriber().getMessageListener();

				logger.log(Logger.Level.TRACE, "Fail: Exception was not thrown!");
			} catch (jakarta.jms.IllegalStateException ise) {
				logger.log(Logger.Level.TRACE, "Pass: threw expected error");
				passed = true;
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.TRACE, "Fail: wrong exception: " + e.getClass().getName() + " was returned");
			}
			if (!passed) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			throw new Exception("closedTopicSessionGetMessageListenerTest", e);
		}
	}

}
