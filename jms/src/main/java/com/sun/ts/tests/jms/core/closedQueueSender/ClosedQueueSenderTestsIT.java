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
package com.sun.ts.tests.jms.core.closedQueueSender;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;
import com.sun.ts.tests.jms.common.MessageTestImpl;

import jakarta.jms.Message;
import jakarta.jms.Queue;

/**
 * JMS TS tests. Testing method calls on closed QueueSender objects.
 */

public class ClosedQueueSenderTestsIT {
	private static final String TestName = "com.sun.ts.tests.jms.core.closedQueueSender.ClosedQueueSenderTestsIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(ClosedQueueSenderTestsIT.class.getName());

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

	/* Utility methods for tests */

	/**
	 * Used by tests that need a closed sender for testing. Passes any exceptions up
	 * to caller.
	 * 
	 * @param int The type of session that needs to be created and closed
	 */
	private void createAndCloseSender() throws Exception {
		tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
		tool.getDefaultQueueConnection().start();

		logger.log(Logger.Level.TRACE, "Closing queue sender");
		tool.getDefaultQueueSender().close();
		logger.log(Logger.Level.TRACE, "Sender closed");
	}

	/* Test setup: */

	/*
	 * setup() is called before each test
	 * 
	 * Creates Administrator object and deletes all previous Destinations.
	 * Individual tests create the TestTools object with one default Queue and/or
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
			queues = new ArrayList(2);

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
	 * @testName: closedQueueSenderCloseTest
	 * 
	 * @assertion_ids: JMS:SPEC:201; JMS:JAVADOC:315;
	 * 
	 * @test_Strategy: Close default sender and call method on it.
	 */
	@Test
	public void closedQueueSenderCloseTest() throws Exception {
		try {
			createAndCloseSender();
			logger.log(Logger.Level.TRACE, "Try to call close again");
			tool.getDefaultQueueSender().close();
		} catch (Exception e) {
			throw new Exception("closedQueueSenderCloseTest", e);
		}
	}

	/*
	 * @testName: closedQueueSenderGetDeliveryModeTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:303;
	 * 
	 * @test_Strategy: Close default sender and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedQueueSenderGetDeliveryModeTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSender();
			logger.log(Logger.Level.TRACE, "Try to call getDeliveryMode");
			try {
				int foo = tool.getDefaultQueueSender().getDeliveryMode();

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
			throw new Exception("closedQueueSenderGetDeliveryModeTest", e);
		}
	}

	/*
	 * @testName: closedQueueSenderGetDisableMessageIDTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:295;
	 * 
	 * @test_Strategy: Close default sender and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedQueueSenderGetDisableMessageIDTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSender();
			logger.log(Logger.Level.TRACE, "Try to call getDisableMessageID");
			try {
				boolean foo = tool.getDefaultQueueSender().getDisableMessageID();

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
			throw new Exception("closedQueueSenderGetDisableMessageIDTest", e);
		}
	}

	/*
	 * @testName: closedQueueSenderGetDisableMessageTimestampTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:299;
	 * 
	 * @test_Strategy: Close default sender and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedQueueSenderGetDisableMessageTimestampTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSender();
			logger.log(Logger.Level.TRACE, "Try to call getDisableMessageTimestamp");
			try {
				boolean foo = tool.getDefaultQueueSender().getDisableMessageTimestamp();

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
			throw new Exception("closedQueueSenderGetDisableMessageTimestampTest", e);
		}
	}

	/*
	 * @testName: closedQueueSenderGetPriorityTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:307;
	 * 
	 * @test_Strategy: Close default sender and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedQueueSenderGetPriorityTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSender();
			logger.log(Logger.Level.TRACE, "Try to call getPriority");
			try {
				int foo = tool.getDefaultQueueSender().getPriority();

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
			throw new Exception("closedQueueSenderGetPriorityTest", e);
		}
	}

	/*
	 * @testName: closedQueueSenderGetTimeToLiveTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:311;
	 * 
	 * @test_Strategy: Close default sender and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedQueueSenderGetTimeToLiveTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSender();
			logger.log(Logger.Level.TRACE, "Try to call getTimeToLive");
			try {
				long foo = tool.getDefaultQueueSender().getTimeToLive();

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
			throw new Exception("closedQueueSenderGetTimeToLiveTest", e);
		}
	}

	/*
	 * @testName: closedQueueSenderSetDeliveryModeTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:301;
	 * 
	 * @test_Strategy: Close default sender and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedQueueSenderSetDeliveryModeTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSender();
			logger.log(Logger.Level.TRACE, "Try to call setDeliveryMode");
			try {
				tool.getDefaultQueueSender().setDeliveryMode(Message.DEFAULT_DELIVERY_MODE);
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
			throw new Exception("closedQueueSenderSetDeliveryModeTest", e);
		}
	}

	/*
	 * @testName: closedQueueSenderSetDisableMessageIDTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:293;
	 * 
	 * @test_Strategy: Close default sender and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedQueueSenderSetDisableMessageIDTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSender();
			logger.log(Logger.Level.TRACE, "Try to call setDisableMessageID");
			try {
				tool.getDefaultQueueSender().setDisableMessageID(true);
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
			throw new Exception("closedQueueSenderSetDisableMessageIDTest", e);
		}
	}

	/*
	 * @testName: closedQueueSenderSetDisableMessageTimestampTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:297;
	 * 
	 * @test_Strategy: Close default sender and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedQueueSenderSetDisableMessageTimestampTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSender();
			logger.log(Logger.Level.TRACE, "Try to call setDisableMessageTimestamp");
			try {
				tool.getDefaultQueueSender().setDisableMessageTimestamp(true);
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
			throw new Exception("closedQueueSenderSetDisableMessageTimestampTest", e);
		}
	}

	/*
	 * @testName: closedQueueSenderSetPriorityTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:305;
	 * 
	 * @test_Strategy: Close default sender and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedQueueSenderSetPriorityTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSender();
			logger.log(Logger.Level.TRACE, "Try to call setPriority");
			try {
				tool.getDefaultQueueSender().setPriority(Message.DEFAULT_PRIORITY);
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
			throw new Exception("closedQueueSenderSetPriorityTest", e);
		}
	}

	/*
	 * @testName: closedQueueSenderSetTimeToLiveTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:309;
	 * 
	 * @test_Strategy: Close default sender and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedQueueSenderSetTimeToLiveTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSender();
			logger.log(Logger.Level.TRACE, "Try to call setTimeToLive");
			try {
				tool.getDefaultQueueSender().setTimeToLive(Message.DEFAULT_TIME_TO_LIVE);
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
			throw new Exception("closedQueueSenderSetTimeToLiveTest", e);
		}
	}

	/*
	 * @testName: closedQueueSenderGetQueueTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:196;
	 * 
	 * @test_Strategy: Close default sender and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedQueueSenderGetQueueTest() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSender();
			logger.log(Logger.Level.TRACE, "Try to call getQueue");
			try {
				Queue foo = tool.getDefaultQueueSender().getQueue();

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
			throw new Exception("closedQueueSenderGetQueueTest", e);
		}
	}

	/*
	 * @testName: closedQueueSenderSend1Test
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:198;
	 * 
	 * @test_Strategy: Close default sender and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedQueueSenderSend1Test() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSender();
			logger.log(Logger.Level.TRACE, "Try to call send(Message)");
			try {
				tool.getDefaultQueueSender().send(new MessageTestImpl());
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
			throw new Exception("closedQueueSenderSend1Test", e);
		}
	}

	/*
	 * @testName: closedQueueSenderSend2Test
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:200;
	 * 
	 * 
	 * @test_Strategy: Close default sender and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedQueueSenderSend2Test() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSender();
			logger.log(Logger.Level.TRACE, "Try to call send(Message,int,int,long)");
			try {
				tool.getDefaultQueueSender().send(new MessageTestImpl(), Message.DEFAULT_DELIVERY_MODE,
						Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);
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
			throw new Exception("closedQueueSenderSend2Test", e);
		}
	}

	/*
	 * @testName: closedQueueSenderSend3Test
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:202;
	 * 
	 * @test_Strategy: Close default sender and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedQueueSenderSend3Test() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSender();
			logger.log(Logger.Level.TRACE, "Try to call send(Queue,Message)");
			try {
				tool.getDefaultQueueSender().send(new MessageTestImpl());
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
			throw new Exception("closedQueueSenderSend3Test", e);
		}
	}

	/*
	 * @testName: closedQueueSenderSend4Test
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:204;
	 * 
	 * @test_Strategy: Close default sender and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedQueueSenderSend4Test() throws Exception {
		boolean passed = false;

		try {
			createAndCloseSender();
			logger.log(Logger.Level.TRACE, "Try to call send(Queue,Message,int,int,long)");
			try {
				tool.getDefaultQueueSender().send(new MessageTestImpl(), Message.DEFAULT_DELIVERY_MODE,
						Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);
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
			throw new Exception("closedQueueSenderSend4Test", e);
		}
	}

}
