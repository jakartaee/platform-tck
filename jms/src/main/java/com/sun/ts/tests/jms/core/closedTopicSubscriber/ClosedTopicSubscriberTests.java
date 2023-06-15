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
package com.sun.ts.tests.jms.core.closedTopicSubscriber;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.Message;
import jakarta.jms.Topic;

public class ClosedTopicSubscriberTests {
	private static final String testName = "com.sun.ts.tests.jms.core.closedTopicSubscriber.ClosedTopicSubscriberTests";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(ClosedTopicSubscriberTests.class.getName());

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
			// get props
			jmsUser = System.getProperty("user");
			jmsPassword = System.getProperty("password");
			mode = System.getProperty("platform.mode");
			timeout = Long.parseLong(System.getProperty("jms_timeout"));
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
	/* Utility methods for tests */

	/**
	 * Used by tests that need a closed subscriber for testing. Passes any
	 * exceptions up to caller.
	 * 
	 * @param int The type of session that needs to be created and closed
	 */
	private void createAndCloseSubscriber() throws Exception {
		tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, mode);
		tool.getDefaultTopicConnection().start();

		logger.log(Logger.Level.TRACE, "Closing topic subscriber");
		tool.getDefaultTopicSubscriber().close();
		logger.log(Logger.Level.TRACE, "Subscriber closed");
	}

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
	 * @testName: closedTopicSubscriberCloseTest
	 * 
	 * @assertion_ids: JMS:SPEC:201; JMS:JAVADOC:338;
	 * 
	 * @test_Strategy: Close default subscriber and call method on it.
	 */
	@Test
	public void closedTopicSubscriberCloseTest() throws Exception {
		try {
			createAndCloseSubscriber();
			logger.log(Logger.Level.TRACE, "Try to call close again");
			tool.getDefaultTopicSubscriber().close();
		} catch (Exception e) {
			throw new Exception("closedTopicSubscriberCloseTest", e);
		}
	}

	/*
	 * @testName: closedTopicSubscriberGetMessageSelectorTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:326;
	 * 
	 * @test_Strategy: Close default subscriber and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedTopicSubscriberGetMessageSelectorTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSubscriber();
			logger.log(Logger.Level.TRACE, "Try to call getMessageSelector");
			try {
				String foo = tool.getDefaultTopicSubscriber().getMessageSelector();

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
			throw new Exception("closedTopicSubscriberGetMessageSelectorTest", e);
		}
	}

	/*
	 * @testName: closedTopicSubscriberReceiveTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:332;
	 * 
	 * @test_Strategy: Close default subscriber and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedTopicSubscriberReceiveTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSubscriber();
			logger.log(Logger.Level.TRACE, "Try to call receive");
			try {
				Message foo = tool.getDefaultTopicSubscriber().receive();

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
			throw new Exception("closedTopicSubscriberReceiveTest", e);
		}
	}

	/*
	 * @testName: closedTopicSubscriberReceiveTimeoutTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:334;
	 * 
	 * @test_Strategy: Close default subscriber and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedTopicSubscriberReceiveTimeoutTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSubscriber();
			logger.log(Logger.Level.TRACE, "Try to call receive(timeout)");
			try {
				Message foo = tool.getDefaultTopicSubscriber().receive(timeout);

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
			throw new Exception("closedTopicSubscriberReceiveTimeoutTest", e);
		}
	}

	/*
	 * @testName: closedTopicSubscriberReceiveNoWaitTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:336;
	 * 
	 * @test_Strategy: Close default subscriber and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedTopicSubscriberReceiveNoWaitTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSubscriber();
			logger.log(Logger.Level.TRACE, "Try to call receiveNoWait");
			try {
				Message foo = tool.getDefaultTopicSubscriber().receiveNoWait();

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
			throw new Exception("closedTopicSubscriberReceiveNoWaitTest", e);
		}
	}

	/*
	 * @testName: closedTopicSubscriberGetNoLocalTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:79;
	 * 
	 * @test_Strategy: Close default subscriber and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedTopicSubscriberGetNoLocalTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSubscriber();
			logger.log(Logger.Level.TRACE, "Try to call getNoLocal");
			try {
				boolean foo = tool.getDefaultTopicSubscriber().getNoLocal();

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
			throw new Exception("closedTopicSubscriberGetNoLocalTest", e);
		}
	}

	/*
	 * @testName: closedTopicSubscriberGetTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:77;
	 * 
	 * @test_Strategy: Close default subscriber and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedTopicSubscriberGetTopicTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSubscriber();
			logger.log(Logger.Level.TRACE, "Try to call getTopic");
			try {
				Topic foo = tool.getDefaultTopicSubscriber().getTopic();

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
			throw new Exception("closedTopicSubscriberGetTopicTest", e);
		}
	}

}
