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
package com.sun.ts.tests.jms.core.appclient.closedQueueReceiver;

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

public class ClosedQueueReceiverTests {
	private static final String TestName = "com.sun.ts.tests.jms.core.appclient.closedQueueReceiver.ClosedQueueReceiverTests";

	private static final String testDir = System.getProperty("user.dir");

	private static final Logger logger = (Logger) System.getLogger(ClosedQueueReceiverTests.class.getName());

	// JMS objects
	private static JmsTool tool = null;

	// Harness req's
	private Properties props = null;

	// properties read
	long timeout;

	String user;

	String password;

	String mode;

	ArrayList queues = null;

	ArrayList connections = null;

	/* Utility methods for tests */

	/**
	 * Used by tests that need a closed receiver for testing. Passes any exceptions
	 * up to caller.
	 * 
	 * @param int The type of session that needs to be created and closed
	 */
	private void createAndCloseReceiver() throws Exception {
		tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
		tool.getDefaultQueueConnection().start();

		logger.log(Logger.Level.TRACE, "Closing queue receiver");
		tool.getDefaultQueueReceiver().close();
		logger.log(Logger.Level.TRACE, "Receiver closed");
	}

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

	/**
	 * Method Declaration.
	 * 
	 * 
	 * @param args
	 * @param p
	 *
	 * @exception Fault
	 *
	 * @see
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
			// get ready for new test
			logger.log(Logger.Level.TRACE, "Getting Administrator and deleting any leftover destinations.");
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
	 * @testName: closedQueueReceiverGetMessageListenerTest
	 *
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:328;
	 *
	 * @test_Strategy: Close default receiver and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedQueueReceiverGetMessageListenerTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseReceiver();
			logger.log(Logger.Level.TRACE, "Try to call getMessageListener");
			try {
				MessageListener foo = tool.getDefaultQueueReceiver().getMessageListener();

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
			throw new Exception("closedQueueReceiverGetMessageListenerTest", e);
		}
	}

	/*
	 * @testName: closedQueueReceiverSetMessageListenerTest
	 *
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:330; JMS:JAVADOC:325;
	 *
	 * @test_Strategy: Close default receiver and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedQueueReceiverSetMessageListenerTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseReceiver();
			logger.log(Logger.Level.TRACE, "Try to call setMessageListener");
			try {
				MessageListener foo = new MessageListener() {

					public void onMessage(Message m) {
					}

				};

				tool.getDefaultQueueReceiver().setMessageListener(foo);
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
			throw new Exception("closedQueueReceiverSetMessageListenerTest", e);
		}
	}
}
