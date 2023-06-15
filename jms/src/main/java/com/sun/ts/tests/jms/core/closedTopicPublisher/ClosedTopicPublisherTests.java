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
package com.sun.ts.tests.jms.core.closedTopicPublisher;

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
import jakarta.jms.Topic;

public class ClosedTopicPublisherTests {
	private static final String testName = "com.sun.ts.tests.jms.core.closedTopicPublisher.ClosedTopicPublisherTests";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(ClosedTopicPublisherTests.class.getName());

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

	/* Utility methods for tests */

	/**
	 * Used by tests that need a closed publisher for testing. Passes any exceptions
	 * up to caller.
	 * 
	 * @param int The type of session that needs to be created and closed
	 */
	private void createAndClosePublisher() throws Exception {
		tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, mode);
		tool.getDefaultTopicConnection().start();
		logger.log(Logger.Level.TRACE, "Closing topic publisher");
		tool.getDefaultTopicPublisher().close();
		logger.log(Logger.Level.TRACE, "Publisher closed");
	}

	/* Tests */
	/*
	 * @testName: closedTopicPublisherCloseTest
	 * 
	 * @assertion_ids: JMS:SPEC:201; JMS:JAVADOC:315;
	 * 
	 * @test_Strategy: Close default publisher and call method on it.
	 */
	@Test
	public void closedTopicPublisherCloseTest() throws Exception {
		try {
			createAndClosePublisher();
			logger.log(Logger.Level.TRACE, "Try to call close again");
			tool.getDefaultTopicPublisher().close();
		} catch (Exception e) {
			throw new Exception("closedTopicPublisherCloseTest", e);
		}
	}

	/*
	 * @testName: closedTopicPublisherGetDeliveryModeTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:303;
	 * 
	 * @test_Strategy: Cannot call getDeliveryMode() on closed publishers Close
	 * default publisher and call method on it. Check for IllegalStateException.
	 */
	@Test
	public void closedTopicPublisherGetDeliveryModeTest() throws Exception {
		boolean passed = false;

		try {
			createAndClosePublisher();
			logger.log(Logger.Level.TRACE, "Try to call getDeliveryMode");
			try {
				int foo = tool.getDefaultTopicPublisher().getDeliveryMode();

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
			throw new Exception("closedTopicPublisherGetDeliveryModeTest", e);
		}
	}

	/*
	 * @testName: closedTopicPublisherGetDisableMessageIDTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:295;
	 * 
	 * @test_Strategy: Close default publisher and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedTopicPublisherGetDisableMessageIDTest() throws Exception {
		boolean passed = false;

		try {
			createAndClosePublisher();
			logger.log(Logger.Level.TRACE, "Try to call getDisableMessageID");
			try {
				boolean foo = tool.getDefaultTopicPublisher().getDisableMessageID();

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
			throw new Exception("closedTopicPublisherGetDisableMessageIDTest", e);
		}
	}

	/*
	 * @testName: closedTopicPublisherGetDisableMessageTimestampTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:299;
	 * 
	 * @test_Strategy: Close default publisher and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedTopicPublisherGetDisableMessageTimestampTest() throws Exception {
		boolean passed = false;

		try {
			createAndClosePublisher();
			logger.log(Logger.Level.TRACE, "Try to call getDisableMessageTimestamp");
			try {
				boolean foo = tool.getDefaultTopicPublisher().getDisableMessageTimestamp();

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
			throw new Exception("closedTopicPublisherGetDisableMessageTimestampTest", e);
		}
	}

	/*
	 * @testName: closedTopicPublisherGetPriorityTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:307;
	 * 
	 * @test_Strategy: Close default publisher and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedTopicPublisherGetPriorityTest() throws Exception {
		boolean passed = false;

		try {
			createAndClosePublisher();
			logger.log(Logger.Level.TRACE, "Try to call getPriority");
			try {
				int foo = tool.getDefaultTopicPublisher().getPriority();

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
			throw new Exception("closedTopicPublisherGetPriorityTest", e);
		}
	}

	/*
	 * @testName: closedTopicPublisherGetTimeToLiveTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:311;
	 * 
	 * @test_Strategy: Close default publisher and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedTopicPublisherGetTimeToLiveTest() throws Exception {
		boolean passed = false;

		try {
			createAndClosePublisher();
			logger.log(Logger.Level.TRACE, "Try to call getTimeToLive");
			try {
				long foo = tool.getDefaultTopicPublisher().getTimeToLive();

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
			throw new Exception("closedTopicPublisherGetTimeToLiveTest", e);
		}
	}

	/*
	 * @testName: closedTopicPublisherSetDeliveryModeTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:301;
	 * 
	 * @test_Strategy: Close default publisher and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedTopicPublisherSetDeliveryModeTest() throws Exception {
		boolean passed = false;

		try {
			createAndClosePublisher();
			logger.log(Logger.Level.TRACE, "Try to call setDeliveryMode");
			try {
				tool.getDefaultTopicPublisher().setDeliveryMode(Message.DEFAULT_DELIVERY_MODE);
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
			throw new Exception("closedTopicPublisherSetDeliveryModeTest", e);
		}
	}

	/*
	 * @testName: closedTopicPublisherSetDisableMessageIDTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:293;
	 * 
	 * @test_Strategy: Close default publisher and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedTopicPublisherSetDisableMessageIDTest() throws Exception {
		boolean passed = false;

		try {
			createAndClosePublisher();
			logger.log(Logger.Level.TRACE, "Try to call setDisableMessageID");
			try {
				tool.getDefaultTopicPublisher().setDisableMessageID(true);
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
			throw new Exception("closedTopicPublisherSetDisableMessageIDTest", e);
		}
	}

	/*
	 * @testName: closedTopicPublisherSetDisableMessageTimestampTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:297;
	 * 
	 * @test_Strategy: Close default publisher and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedTopicPublisherSetDisableMessageTimestampTest() throws Exception {
		boolean passed = false;

		try {
			createAndClosePublisher();
			logger.log(Logger.Level.TRACE, "Try to call setDisableMessageTimestamp");
			try {
				tool.getDefaultTopicPublisher().setDisableMessageTimestamp(true);
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
			throw new Exception("closedTopicPublisherSetDisableMessageTimestampTest", e);
		}
	}

	/*
	 * @testName: closedTopicPublisherSetPriorityTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:305;
	 * 
	 * @test_Strategy: Close default publisher and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedTopicPublisherSetPriorityTest() throws Exception {
		boolean passed = false;

		try {
			createAndClosePublisher();
			logger.log(Logger.Level.TRACE, "Try to call setPriority");
			try {
				tool.getDefaultTopicPublisher().setPriority(Message.DEFAULT_PRIORITY);
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
			throw new Exception("closedTopicPublisherSetPriorityTest", e);
		}
	}

	/*
	 * @testName: closedTopicPublisherSetTimeToLiveTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:309;
	 * 
	 * @test_Strategy: Close default publisher and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedTopicPublisherSetTimeToLiveTest() throws Exception {
		boolean passed = false;

		try {
			createAndClosePublisher();
			logger.log(Logger.Level.TRACE, "Try to call setTimeToLive");
			try {
				tool.getDefaultTopicPublisher().setTimeToLive(Message.DEFAULT_TIME_TO_LIVE);
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
			throw new Exception("closedTopicPublisherSetTimeToLiveTest", e);
		}
	}

	/*
	 * @testName: closedTopicPublisherGetTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:97;
	 * 
	 * @test_Strategy: Close default publisher and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedTopicPublisherGetTopicTest() throws Exception {
		boolean passed = false;

		try {
			createAndClosePublisher();
			logger.log(Logger.Level.TRACE, "Try to call getTopic");
			try {
				Topic foo = tool.getDefaultTopicPublisher().getTopic();

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
			throw new Exception("closedTopicPublisherGetTopicTest", e);
		}
	}

	/*
	 * @testName: closedTopicPublisherPublish1Test
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:99;
	 * 
	 * @test_Strategy: Close default publisher and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedTopicPublisherPublish1Test() throws Exception {
		boolean passed = false;

		try {
			createAndClosePublisher();
			logger.log(Logger.Level.TRACE, "Try to call publish(Message)");
			try {
				tool.getDefaultTopicPublisher().publish(new MessageTestImpl());
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
			throw new Exception("closedTopicPublisherPublish1Test", e);
		}
	}

	/*
	 * @testName: closedTopicPublisherPublish2Test
	 * 
	 * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:101;
	 * 
	 * 
	 * @test_Strategy: Close default publisher and call method on it. Check for
	 * IllegalStateException.
	 */
	@Test
	public void closedTopicPublisherPublish2Test() throws Exception {
		boolean passed = false;

		try {
			createAndClosePublisher();
			logger.log(Logger.Level.TRACE, "Try to call publish(Message,int,int,long)");
			try {
				tool.getDefaultTopicPublisher().publish(new MessageTestImpl(), Message.DEFAULT_DELIVERY_MODE,
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
			throw new Exception("closedTopicPublisherPublish2Test", e);
		}
	}
}
