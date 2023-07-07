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
package com.sun.ts.tests.jms.core.exceptionTopic;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.BytesMessage;
import jakarta.jms.Destination;
import jakarta.jms.InvalidDestinationException;
import jakarta.jms.InvalidSelectorException;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.MessageEOFException;
import jakarta.jms.MessageFormatException;
import jakarta.jms.MessageNotReadableException;
import jakarta.jms.MessageNotWriteableException;
import jakarta.jms.Queue;
import jakarta.jms.StreamMessage;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;
import jakarta.jms.TopicPublisher;
import jakarta.jms.TopicSubscriber;

public class ExceptionTopicTestsIT {
	private static final String testName = "com.sun.ts.tests.jms.core.exceptionTopic.ExceptionTopicTestsIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(ExceptionTopicTestsIT.class.getName());

	// Harness req's
	private Properties props = null;

	// JMS object
	private transient JmsTool tool = null;

	// properties read
	long timeout;

	private String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

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
			connections = new ArrayList(2);

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
	 * @testName: xInvalidDestinationExceptionTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:174; JMS:JAVADOC:83; JMS:JAVADOC:85; JMS:JAVADOC:87;
	 * JMS:JAVADOC:89; JMS:JAVADOC:91; JMS:JAVADOC:95; JMS:JAVADOC:625;
	 * JMS:JAVADOC:626; JMS:JAVADOC:628; JMS:JAVADOC:629; JMS:JAVADOC:631;
	 * JMS:JAVADOC:632;
	 * 
	 * @test_Strategy: pass an invalid Topic object to createSubscriber(null)
	 * createSubscriber(null,selector,nolocal)
	 * createDurableSubscriber(null,subscriber)
	 * createDurableSubscriber(null,String,selector,nolocal) createPublisher(null) -
	 * null is valid here. unsubscribe(invalidSubscriptionName)
	 */
	@Test
	public void xInvalidDestinationExceptionTopicTest() throws Exception {
		boolean pass = true;
		TopicPublisher tPublisher = null;
		TopicSubscriber tSubscriber = null;
		Topic dummy = null;

		try {
			tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
			logger.log(Logger.Level.TRACE, "** Close default TopicSubscriber **");
			tool.getDefaultTopicSubscriber().close();
			tool.getDefaultTopicConnection().start();
			logger.log(Logger.Level.INFO, "Test unsubscribe(invalidSubscriptionName)");
			try {
				tool.getDefaultTopicSession().unsubscribe("invalidSubscriptionName");
				logger.log(Logger.Level.TRACE, "FAIL: expected InvalidDestinationException!");
				pass = false;
			} catch (Exception ee) {
				if (ee instanceof jakarta.jms.InvalidDestinationException) {
					logger.log(Logger.Level.TRACE, "Pass: InvalidDestinationException thrown as expected");
				} else {
					logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Test createSubscriber(null)");
			try {
				tSubscriber = tool.getDefaultTopicSession().createSubscriber(dummy);
				logger.log(Logger.Level.TRACE, "FAIL: expected InvalidDestinationException!");
				pass = false;
			} catch (Exception ee) {
				if (ee instanceof jakarta.jms.InvalidDestinationException) {
					logger.log(Logger.Level.TRACE, "Pass: InvalidDestinationException thrown as expected");
				} else {
					logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Test createSubscriber(null,selector,nolocal)");
			try {
				tSubscriber = tool.getDefaultTopicSession().createSubscriber(dummy, "TEST", true);
				logger.log(Logger.Level.TRACE, "FAIL: expected InvalidDestinationException!");
				pass = false;
			} catch (Exception ee) {
				if (ee instanceof jakarta.jms.InvalidDestinationException) {
					logger.log(Logger.Level.TRACE, "Pass: InvalidDestinationException thrown as expected");
				} else {
					logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Test createDurableSubscriber(null,String)");
			try {
				tSubscriber = tool.getDefaultTopicSession().createDurableSubscriber(dummy, "durable");
				logger.log(Logger.Level.TRACE, "FAIL: expected InvalidDestinationException!");
				pass = false;
			} catch (Exception ee) {
				if (ee instanceof jakarta.jms.InvalidDestinationException) {
					logger.log(Logger.Level.TRACE, "Pass: InvalidDestinationException thrown as expected");
				} else {
					logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Test createDurableSubscriber(null,String,selector,nolocal)");
			try {
				tSubscriber = tool.getDefaultTopicSession().createDurableSubscriber(dummy, "durable", "TEST", true);
				logger.log(Logger.Level.TRACE, "FAIL: expected InvalidDestinationException!");
				pass = false;
			} catch (Exception ee) {
				if (ee instanceof jakarta.jms.InvalidDestinationException) {
					logger.log(Logger.Level.TRACE, "Pass: InvalidDestinationException thrown as expected");
				} else {
					logger.log(Logger.Level.INFO, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Test createPublisher(null) - This is valid");
			try {
				tPublisher = tool.getDefaultTopicSession().createPublisher(dummy);
				if (tPublisher != null)
					logger.log(Logger.Level.INFO, "tPublisher=" + tPublisher);
				logger.log(Logger.Level.TRACE, "PASS: null allowed for unidentified producer");
			} catch (Exception ee) {
				TestUtil.printStackTrace(ee);
				logger.log(Logger.Level.ERROR, "Error: unexpected error " + ee.getClass().getName() + " was thrown");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during xInvalidDestinationExceptionQTest tests");
			}
		} catch (Exception e) {
			throw new Exception("xInvalidDestinationExceptionTopicTest", e);
		}
	}

	/*
	 * @testName: xMessageNotReadableExceptionTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:178; JMS:JAVADOC:680;
	 * 
	 * @test_Strategy: create a BytesMessage, read it.
	 */
	@Test
	public void xMessageNotReadableExceptionTopicTest() throws Exception {
		try {
			BytesMessage msg = null;

			// create Topic Connection
			tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
			tool.getDefaultTopicConnection().start();

			// Create a BytesMessage
			msg = tool.getDefaultTopicSession().createBytesMessage();
			msg.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageNotReadableExceptionTopicTest");
			try {
				msg.readByte();
				logger.log(Logger.Level.ERROR, "FAIL --- should not have gotten this far!");
				throw new Exception("Fail: Did not throw expected error!!!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.INFO, "Passed with expected MessageNotReadableException.");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Expected MessageNotReadableException did not occur:", e);
			throw new Exception("xMessageNotReadableExceptionTopicTest test failed", e);
		}
	}

	/*
	 * @testName: xMessageNotWriteableExceptionTTestforTextMessage
	 * 
	 * @assertion_ids: JMS:SPEC:179; JMS:SPEC:70; JMS:JAVADOC:766;
	 * 
	 * @test_Strategy: When a client receives a text message it is in read-only
	 * mode. Publish a message and have the client attempt to write to it.
	 */
	@Test
	public void xMessageNotWriteableExceptionTTestforTextMessage() throws Exception {
		try {
			TextMessage messageSent = null;
			TextMessage messageReceived = null;
			byte bValue = 127;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
			tool.getDefaultTopicConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultTopicSession().createTextMessage();
			messageSent.setText("just a test");
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageNotWriteableExceptionTTestforTextMessage");
			logger.log(Logger.Level.TRACE, "Publishing message");
			tool.getDefaultTopicPublisher().publish(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (TextMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("getMessage returned null!! - test did not run!");
			} else {
				logger.log(Logger.Level.TRACE, "Got message - OK");
			}

			// Received message should be read-only - verify proper exception is
			// thrown
			try {
				messageReceived.setText("testing...");
			} catch (MessageNotWriteableException nr) {
				logger.log(Logger.Level.INFO, "Passed with expected MessageNotWriteableException");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "xMessageNotWriteableExceptionTTestforTextMessage failed:", e);
			throw new Exception("xMessageNotWriteableExceptionTTestforTextMessage", e);
		}
	}

	/*
	 * @testName: xMessageNotWriteableExceptionTestforBytesMessage
	 * 
	 * @assertion_ids: JMS:SPEC:73; JMS:SPEC:179; JMS:JAVADOC:702;
	 * 
	 * @test_Strategy: When a client receives a Bytes message it is in read-only
	 * mode. Publish a message and have the client attempt to write to it.
	 */
	@Test
	public void xMessageNotWriteableExceptionTestforBytesMessage() throws Exception {
		try {
			BytesMessage messageSent = null;
			BytesMessage messageReceived = null;
			byte bValue = 127;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
			tool.getDefaultTopicConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultTopicSession().createBytesMessage();
			messageSent.writeByte(bValue);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageNotWriteableExceptionTestforBytesMessage");
			logger.log(Logger.Level.TRACE, "Publishing message");
			tool.getDefaultTopicPublisher().publish(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (BytesMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("getMessage returned null!! - test did not run!");
			} else {
				logger.log(Logger.Level.TRACE, "Got message - OK");
			}

			// Received message should be read-only - verify proper exception is
			// thrown
			try {
				messageReceived.writeByte(bValue);
			} catch (MessageNotWriteableException nr) {
				logger.log(Logger.Level.INFO, "Passed with expected MessageNotWriteableException.");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "xMessageNotWriteableExceptionTestforBytesMessage failed: ", e);
			throw new Exception("xMessageNotWriteableExceptionTestforBytesMessage", e);
		}
	}

	/*
	 * @testName: xMessageNotWriteableExceptionTTestforStreamMessage
	 * 
	 * @assertion_ids: JMS:SPEC:73; JMS:SPEC:179; JMS:JAVADOC:760;
	 * 
	 * @test_Strategy: When a client receives a Stream message it is in read-only
	 * mode. Publish a message and have the client attempt to write to it.
	 */
	@Test
	public void xMessageNotWriteableExceptionTTestforStreamMessage() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
			tool.getDefaultTopicConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultTopicSession().createStreamMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageNotWriteableExceptionTTestforStreamMessage");

			messageSent.writeString("Testing...");
			logger.log(Logger.Level.TRACE, "Publishing message");
			tool.getDefaultTopicPublisher().publish(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("getMessage returned null!! - test did not run!");
			} else {
				logger.log(Logger.Level.TRACE, "Got message - OK");
			}

			// Received message should be read-only - verify proper exception is
			// thrown
			try {
				messageReceived.writeString("Testing...");
			} catch (MessageNotWriteableException nr) {
				logger.log(Logger.Level.TRACE, "Passed with expected MessageNotWriteableException");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "xMessageNotWriteableExceptionTTestforStreamMessage failed: ", e);
			throw new Exception("xMessageNotWriteableExceptionTTestforStreamMessage", e);
		}
	}

	/*
	 * @testName: xMessageNotWriteableExceptionTTestforMapMessage
	 * 
	 * @assertion_ids: JMS:SPEC:73; JMS:SPEC:179; JMS:JAVADOC:822;
	 * 
	 * @test_Strategy: When a client receives a Map message it is in read-only mode.
	 * Publish a message and have the client attempt to write to it.
	 */
	@Test
	public void xMessageNotWriteableExceptionTTestforMapMessage() throws Exception {
		try {
			MapMessage messageSent = null;
			MapMessage messageReceived = null;
			byte bValue = 127;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
			tool.getDefaultTopicConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultTopicSession().createMapMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageNotWriteableExceptionTTestforMapMessage");
			messageSent.setString("aString", "value");
			logger.log(Logger.Level.TRACE, "Publishing message");
			tool.getDefaultTopicPublisher().publish(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (MapMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("getMessage returned null!! - test did not run!");
			} else {
				logger.log(Logger.Level.TRACE, "Got message - OK");
			}

			// Received message should be read-only - verify proper exception is
			// thrown
			try {
				messageReceived.setString("aString", "value");
			} catch (MessageNotWriteableException nr) {
				logger.log(Logger.Level.INFO, "Passed with expected MessageNotWriteableException occurred.");
			}
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "xMessageNotWriteableExceptionTTestforMapMessage failed: ", e);
			throw new Exception("xMessageNotWriteableExceptionTTestforMapMessage", e);
		}
	}

	/*
	 * @testName: xNullPointerExceptionTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:86.1;
	 * 
	 * @test_Strategy: Create a bytes message. Attempt to write null to it. Verify
	 * that a NullPointerException is thrown.
	 * 
	 */
	@Test
	public void xNullPointerExceptionTopicTest() throws Exception {
		try {
			BytesMessage msg = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
			tool.getDefaultTopicConnection().start();
			msg = tool.getDefaultTopicSession().createBytesMessage();
			msg.setStringProperty("COM_SUN_JMS_TESTNAME", "xNullPointerExceptionTopicTest");
			msg.writeBytes(null);
		} catch (java.lang.NullPointerException nullp) {
			logger.log(Logger.Level.INFO, "Passed with expected NullPointerException.");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Expected NullPointerException did not occur!", e);
			throw new Exception("xNullPointerExceptionTopicTest");
		}
	}

	/*
	 * @testName: xMessageEOFExceptionTTestforBytesMessage
	 * 
	 * @assertion_ids: JMS:SPEC:176;
	 * 
	 * @test_Strategy: Publish a bytes message to a topic with one byte. Retreive
	 * message from the topic and read two bytes.
	 */
	@Test
	public void xMessageEOFExceptionTTestforBytesMessage() throws Exception {
		try {
			BytesMessage messageSent = null;
			BytesMessage messageReceived = null;
			byte bValue = 127;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
			tool.getDefaultTopicConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultTopicSession().createBytesMessage();
			messageSent.writeByte(bValue);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageEOFExceptionTTestforBytesMessage");
			logger.log(Logger.Level.TRACE, "Publishing message");
			tool.getDefaultTopicPublisher().publish(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (BytesMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("getMessage returned null!! - test did not run!");
			} else {
				logger.log(Logger.Level.TRACE, "Got message - OK");
			}

			// Received message should contain one byte
			// reading 2 bytes should throw the excpected exception
			messageReceived.readByte();
			try {
				messageReceived.readByte();

				// Should not reach here !!
				logger.log(Logger.Level.TRACE, "Failed:  expected MessageEOFException not thrown");
				throw new Exception("Fail: Did not throw expected error!!!");
			} catch (MessageEOFException end) {
				logger.log(Logger.Level.TRACE, "Passed.\n");
				logger.log(Logger.Level.TRACE, "MessageEOFException occurred!");
				logger.log(Logger.Level.TRACE, " " + end.getMessage());
			}
		} catch (Exception e) {
			throw new Exception("xMessageEOFExceptionTTestforBytesMessage", e);
		}
	}

	/*
	 * @testName: xMessageEOFExceptionTTestforStreamMessage
	 * 
	 * @assertion_ids: JMS:SPEC:176;
	 * 
	 * @test_Strategy: Publish a Stream message to a topic with one byte. Retreive
	 * message from the topic and read two bytes.
	 */
	@Test
	public void xMessageEOFExceptionTTestforStreamMessage() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			byte bValue = 127;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
			tool.getDefaultTopicConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultTopicSession().createStreamMessage();
			messageSent.writeByte(bValue);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageEOFExceptionTTestforStreamMessage");
			logger.log(Logger.Level.TRACE, "Publishing message");
			tool.getDefaultTopicPublisher().publish(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("getMessage returned null!! - test did not run!");
			} else {
				logger.log(Logger.Level.TRACE, "Got message - OK");
			}

			// Received message should contain one byte
			// reading 2 bytes should throw the excpected exception
			messageReceived.readByte();
			try {
				messageReceived.readByte();

				// Should not reach here !!
				logger.log(Logger.Level.TRACE, "Failed:  expected MessageEOFException not thrown");
				throw new Exception("Fail: Did not throw expected error!!!");
			} catch (MessageEOFException end) {
				logger.log(Logger.Level.TRACE, "Passed.\n");
				logger.log(Logger.Level.TRACE, "MessageEOFException occurred!");
				logger.log(Logger.Level.TRACE, " " + end.getMessage());
			}
		} catch (Exception e) {
			throw new Exception("xMessageEOFExceptionTTestforStreamMessage", e);
		}
	}

	/*
	 * @testName: xMessageFormatExceptionTTestforBytesMessage
	 * 
	 * @assertion_ids: JMS:SPEC:177;
	 * 
	 * 
	 * @test_Strategy: Call writeObject with a topic session object
	 */
	@Test
	public void xMessageFormatExceptionTTestforBytesMessage() throws Exception {
		try {
			BytesMessage messageSent = null;
			BytesMessage messageReceived = null;
			byte bValue = 127;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
			tool.getDefaultTopicConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultTopicSession().createBytesMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageFormatExceptionTTestforBytesMessage");
			logger.log(Logger.Level.TRACE, "try to write an invalid object");
			try {
				messageSent.writeObject(tool.getDefaultTopicSession());

				// Should not reach here !!
				logger.log(Logger.Level.TRACE, "Failed:  expected MessageFormatException not thrown");
				throw new Exception("Fail: Did not throw expected error!!!");
			} catch (MessageFormatException fe) {
				logger.log(Logger.Level.TRACE, "Passed.\n");
				logger.log(Logger.Level.TRACE, "MessageFormatException occurred!");
				logger.log(Logger.Level.TRACE, " " + fe.getMessage());
			}
		} catch (Exception e) {
			throw new Exception("xMessageFormatExceptionTTestforBytesMessage", e);
		}
	}

	/*
	 * @testName: xMessageFormatExceptionTTestforStreamMessage
	 * 
	 * @assertion_ids: JMS:SPEC:177; JMS:JAVADOC:744;
	 * 
	 * @test_Strategy: Write a byte array, read it as a string.
	 */
	@Test
	public void xMessageFormatExceptionTTestforStreamMessage() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			byte[] bValues = { 127, 0, 3 };

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
			tool.getDefaultTopicConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultTopicSession().createStreamMessage();
			messageSent.writeBytes(bValues);
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageFormatExceptionTTestforStreamMessage");
			logger.log(Logger.Level.TRACE, "Publishing message");
			tool.getDefaultTopicPublisher().publish(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber().receive(timeout);
			if (messageReceived == null) {
				throw new Exception("getMessage returned null!! - test did not run!");
			} else {
				logger.log(Logger.Level.TRACE, "Got message - OK");
			}

			// Received message should contain a byte, read a string
			try {
				messageReceived.readString();

				// Should not reach here !!
				logger.log(Logger.Level.TRACE, "Failed:  expected MessageFormatException not thrown");
				throw new Exception("Fail: Did not throw expected error!!!");
			} catch (MessageFormatException fe) {
				logger.log(Logger.Level.TRACE, "Passed.\n");
				logger.log(Logger.Level.TRACE, "MessageFormatException occurred!");
				logger.log(Logger.Level.TRACE, " " + fe.getMessage());
			}
		} catch (Exception e) {
			throw new Exception("xMessageFormatExceptionTTestforStreamMessage", e);
		}
	}

	/*
	 * @testName: xInvalidSelectorExceptionTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:69; JMS:SPEC:175; JMS:JAVADOC:627; JMS:JAVADOC:630;
	 * 
	 * @test_Strategy: Call createSubscriber/createDurableSubscriber with an invalid
	 * selector string
	 */
	@Test
	public void xInvalidSelectorExceptionTopicTest() throws Exception {
		try {
			TopicSubscriber tSub = null;

			// create Topic Connection and TopicSubscriber
			tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
			logger.log(Logger.Level.TRACE, "** Close default TopicSubscriber **");
			tool.getDefaultTopicSubscriber().close();
			tool.getDefaultTopicConnection().start();

			// create TopicSubscriber
			tSub = tool.getDefaultTopicSession().createSubscriber(tool.getDefaultTopic(), "=TEST 'test'", false);
			logger.log(Logger.Level.TRACE, "FAIL --- should not have gotten this far!");
			throw new Exception("xInvalidSelectorException test Failed!");
		} catch (InvalidSelectorException es) {
			logger.log(Logger.Level.TRACE, "Passed.\n");
			logger.log(Logger.Level.INFO, "InvalidSelectorException occurred!");
		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "Got Exception instead: ", e);
			logger.log(Logger.Level.TRACE, "Expected InvalidSelectorException did not occur!");
			throw new Exception("xInvalidSelectorExceptionTopicTest eee", e);
		}
	}

	/*
	 * @testName: xIllegalStateExceptionTopicTest
	 * 
	 * @assertion_ids: JMS:SPEC:171; JMS:JAVADOC:634;
	 * 
	 * @test_Strategy: Call session.commit() when there is no transaction to be
	 * committed. Verify that the proper exception is thrown.
	 */
	@Test
	public void xIllegalStateExceptionTopicTest() throws Exception {
		boolean passed = false;

		try {

			// create Topic Connection
			tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
			tool.getDefaultTopicConnection().start();
			logger.log(Logger.Level.INFO, "Calling session.commit(), an illegal operation.");
			try {
				tool.getDefaultTopicSession().commit();
			} catch (jakarta.jms.IllegalStateException iStateE) {
				logger.log(Logger.Level.INFO, "Received jakarta.jms.IllegalStateException -- GOOD");
				passed = true;
			}
			if (passed == false) { // need case for no exception being thrown
				throw new Exception("Did not receive IllegalStateException");
			}
		} catch (Exception e) { // handles case of other exception being thrown
			TestUtil.printStackTrace(e);
			throw new Exception("xIllegalStateExceptionTopicTest");
		}
	}

	/*
	 * @testName: xUnsupportedOperationExceptionTTest1
	 * 
	 * @assertion_ids: JMS:JAVADOC:658; JMS:JAVADOC:661;
	 *
	 * @test_Strategy: Create a TopicPublisher with a null Topic. Verify that
	 * UnsupportedOperationException is thrown when publish is called without a
	 * valid Topic.
	 */
	@Test
	public void xUnsupportedOperationExceptionTTest1() throws Exception {
		try {
			TextMessage messageSent = null;
			boolean pass = true;
			Topic myTopic = null;
			TopicPublisher tPub = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);

			// Close default TopicPublisher and create a new one with null Topic
			tool.getDefaultTopicPublisher().close();
			tPub = tool.getDefaultTopicSession().createPublisher(myTopic);

			tool.getDefaultTopicConnection().start();

			try {
				messageSent = tool.getDefaultTopicSession().createTextMessage();
				messageSent.setText("sending a Text message");
				messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xUnsupportedOperationExceptionTTest1");

				logger.log(Logger.Level.TRACE, "sending a Text message");
				tPub.publish(messageSent);

				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: TopicPublisher.publish(Message) didn't throw expected UnsupportedOperationException.");
			} catch (UnsupportedOperationException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected UnsupportedOperationException from  TopicPublisher.publish(Message)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error:  TopicPublisher.publish(Message) throw incorrect Exception: ",
						e);
				pass = false;
			}

			try {
				messageSent = tool.getDefaultTopicSession().createTextMessage();
				messageSent.setText("sending a Text message");
				messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xUnsupportedOperationExceptionTTest1");

				logger.log(Logger.Level.TRACE, "sending a Text message");
				tPub.publish(messageSent, Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
						Message.DEFAULT_TIME_TO_LIVE);

				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: TopicPublisher.publish(Message, int, int, long) didn't throw expected UnsupportedOperationException.");
			} catch (UnsupportedOperationException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected UnsupportedOperationException from  TopicPublisher.publish(Message, int, int, long)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR,
						"Error:  TopicPublisher.publish(Message, int, int, long) throw incorrect Exception: ", e);
				pass = false;
			}

			if (pass != true)
				throw new Exception("xUnsupportedOperationExceptionTTest1 Failed!");

		} catch (Exception e) {
			logger.log(Logger.Level.ERROR, "xUnsupportedOperationExceptionTTest1 Failed!", e);
			throw new Exception("xUnsupportedOperationExceptionTTest1 Failed!", e);
		} finally {
			try {
				if (tool.getDefaultTopicConnection() != null) {
					tool.getDefaultTopicConnection().close();
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Exception closing TopicConnection and cleanup", e);
			}
		}
	}

	/*
	 * @testName: xInvalidDestinationExceptionTTests
	 *
	 * @assertion_ids: JMS:JAVADOC:502; JMS:JAVADOC:504; JMS:JAVADOC:510;
	 * JMS:JAVADOC:638; JMS:JAVADOC:639; JMS:JAVADOC:641; JMS:JAVADOC:643;
	 * JMS:JAVADOC:644; JMS:JAVADOC:646; JMS:JAVADOC:647; JMS:JAVADOC:649;
	 *
	 * @test_Strategy: Create a Session with Topic Configuration, using a null
	 * Destination/Topic to verify InvalidDestinationException is thrown with
	 * various methods
	 */
	@Test
	public void xInvalidDestinationExceptionTTests() throws Exception {
		String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

		try {
			boolean pass = true;
			Destination dummy = null;
			Topic dummyT = null;

			tool = new JmsTool(JmsTool.COMMON_T, jmsUser, jmsPassword, lookup, mode);
			tool.getDefaultProducer().close();
			tool.getDefaultConsumer().close();

			try {
				tool.getDefaultSession().createConsumer(dummy);
				logger.log(Logger.Level.ERROR,
						"Error: createConsumer(null) didn't throw expected InvalidDestinationException");
				pass = false;
			} catch (InvalidDestinationException ex) {
				logger.log(Logger.Level.INFO, "Got expected InvalidDestinationException from createConsumer(null)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: createConsumer(null) throw incorrect Exception: ", e);
				pass = false;
			}

			try {
				tool.getDefaultSession().createConsumer(dummy, "TEST = 'test'");
				logger.log(Logger.Level.ERROR,
						"Error: createConsumer(null, String) didn't throw expected InvalidDestinationException");
				pass = false;
			} catch (InvalidDestinationException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected InvalidDestinationException from createConsumer(null, String)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: createConsumer(null, String) throw incorrect Exception: ", e);
				pass = false;
			}

			try {
				tool.getDefaultSession().createConsumer(dummy, "TEST = 'test'", true);
				logger.log(Logger.Level.ERROR,
						"Error: createConsumer(null, String, boolean) didn't throw expected InvalidDestinationException");
				pass = false;
			} catch (InvalidDestinationException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected InvalidDestinationException from createConsumer(null, String, true)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: createConsumer(null, String, true) throw incorrect Exception: ",
						e);
				pass = false;
			}

			try {
				tool.closeDefaultConnections();
			} catch (Exception ex) {
				logger.log(Logger.Level.ERROR, "Error closing default Connection", ex);
			}

			if (pass != true)
				throw new Exception("xInvalidDestinationExceptionTTests");

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("xInvalidDestinationExceptionTTests");
		} finally {
			try {
				tool.closeDefaultConnections();
			} catch (Exception ex) {
				logger.log(Logger.Level.ERROR, "Error closing Connection", ex);
			}
		}
	}

	/*
	 * @testName: xMessageNotReadableExceptionTBytesMsgTest
	 *
	 * @assertion_ids: JMS:JAVADOC:676; JMS:JAVADOC:678; JMS:JAVADOC:682;
	 * JMS:JAVADOC:684; JMS:JAVADOC:686; JMS:JAVADOC:688; JMS:JAVADOC:690;
	 * JMS:JAVADOC:692; JMS:JAVADOC:694; JMS:JAVADOC:696; JMS:JAVADOC:698;
	 * JMS:JAVADOC:699; JMS:JAVADOC:700;
	 *
	 * @test_Strategy: Create a BytesMessage, call various read methods on it before
	 * sending. Verify that jakarta.jms.MessageNotReadableException is thrown.
	 */
	@Test
	public void xMessageNotReadableExceptionTBytesMsgTest() throws Exception {
		try {
			BytesMessage messageSent = null;
			boolean pass = true;
			boolean booleanValue = false;
			byte byteValue = 127;
			byte[] bytesValue = { 127, -127, 1, 0 };
			byte[] bytesValueRecvd = { 0, 0, 0, 0 };
			char charValue = 'Z';
			double doubleValue = 6.02e23;
			float floatValue = 6.02e23f;
			int intValue = 2147483647;
			long longValue = 9223372036854775807L;
			Integer nInteger = new Integer(-2147483648);
			short shortValue = -32768;
			String utfValue = "what";

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, jmsUser, jmsPassword, lookup, mode);
			tool.getDefaultProducer().close();
			tool.getDefaultConsumer().close();

			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultSession().createBytesMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageNotReadableExceptionTBytesMsgTest");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.INFO, "Writing one of each primitive type to the message");

			// -----------------------------------------------------------------------------
			messageSent.writeBoolean(booleanValue);
			messageSent.writeByte(byteValue);
			messageSent.writeChar(charValue);
			messageSent.writeDouble(doubleValue);
			messageSent.writeFloat(floatValue);
			messageSent.writeInt(intValue);
			messageSent.writeLong(longValue);
			messageSent.writeObject(nInteger);
			messageSent.writeShort(shortValue);
			messageSent.writeUTF(utfValue);
			messageSent.writeBytes(bytesValue);
			messageSent.writeBytes(bytesValue, 0, 1);

			try {
				messageSent.getBodyLength();
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: getBodyLength didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "getBodyLength threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "getBodyLength threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readBoolean();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readBoolean didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readBoolean threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readBoolean threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readByte();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readByte didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readByte threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readByte threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readUnsignedByte();
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: readUnsignedByte didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readUnsignedByte threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readUnsignedByte threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readShort();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readShort didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readShort threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readShort threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readUnsignedShort();
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: readUnsignedShort didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readUnsignedShort threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readUnsignedShort threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readChar();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readChar didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readChar threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readChar threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readInt();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readInt didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readInt threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readInt threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readLong();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readLong didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readLong threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readLong threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readFloat();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readFloat didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readFloat threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readFloat threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readDouble();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readDouble didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readDouble threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readDouble threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readUTF();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readUTF didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readUTF threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readUTF threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readBytes(bytesValueRecvd);
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: readBytes(byte[]) didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readBytes(byte[]) threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readBytes(byte[]) threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageSent.readBytes(bytesValueRecvd, 1);
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: readBytes(byte[], int) didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readBytes(byte[], int) threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readBytes(byte[],int) threw Wrong Exception!", e);
				pass = false;
			}

			if (pass != true)
				throw new Exception("xMessageNotReadableExceptionTBytesMsgTest Failed!");

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("xMessageNotReadableExceptionTBytesMsgTest:");
		} finally {
			try {
				tool.closeDefaultConnections();
			} catch (Exception ex) {
				logger.log(Logger.Level.ERROR, "Error closing Connection", ex);
			}
		}
	}

	/*
	 * @testName: xMessageNotReadableExceptionTStreamMsgTest
	 *
	 * @assertion_ids: JMS:SPEC:73.1; JMS:JAVADOC:431; JMS:JAVADOC:721;
	 * JMS:JAVADOC:724; JMS:JAVADOC:727; JMS:JAVADOC:730; JMS:JAVADOC:733;
	 * JMS:JAVADOC:736; JMS:JAVADOC:739; JMS:JAVADOC:742; JMS:JAVADOC:745;
	 * JMS:JAVADOC:748; JMS:JAVADOC:751;
	 *
	 * @test_Strategy: Create a StreamMessage, send and receive via a Topic; Call
	 * clearBoldy right after receiving the message; Call various read methods on
	 * received message; Verify jakarta.jms.MessageNotReadableException is thrown.
	 */
	@Test
	public void xMessageNotReadableExceptionTStreamMsgTest() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			boolean pass = true;
			byte bValue = 127;
			boolean abool = false;
			byte[] bValues = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
			byte[] bValues2 = { 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
			byte[] bValuesReturned = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			char charValue = 'Z';
			short sValue = 32767;
			long lValue = 9223372036854775807L;
			double dValue = 6.02e23;
			float fValue = 6.02e23f;
			int iValue = 6;
			String myString = "text";
			String sTesting = "Testing StreamMessages";

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.COMMON_T, jmsUser, jmsPassword, lookup, mode);
			tool.getDefaultConnection().start();

			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultSession().createStreamMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "xMessageNotReadableExceptionTStreamMsgTest");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "");
			logger.log(Logger.Level.INFO, "Writing one of each primitive type to the message");
			// -----------------------------------------------------------------------------
			messageSent.writeBytes(bValues2, 0, bValues.length);
			messageSent.writeBoolean(abool);
			messageSent.writeByte(bValue);
			messageSent.writeBytes(bValues);
			messageSent.writeChar(charValue);
			messageSent.writeDouble(dValue);
			messageSent.writeFloat(fValue);
			messageSent.writeInt(iValue);
			messageSent.writeLong(lValue);
			messageSent.writeObject(sTesting);
			messageSent.writeShort(sValue);
			messageSent.writeString(myString);
			messageSent.writeObject(null);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultProducer().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultConsumer().receive(timeout);

			logger.log(Logger.Level.TRACE, "call ClearBody()");
			messageReceived.clearBody();

			try {
				messageReceived.readBoolean();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readBoolean didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readBoolean threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readBoolean threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageReceived.readByte();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readByte didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readByte threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readByte threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageReceived.readShort();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readShort didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readShort threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readShort threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageReceived.readChar();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readChar didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readChar threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readChar threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageReceived.readInt();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readInt didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readInt threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readInt threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageReceived.readLong();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readLong didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readLong threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readLong threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageReceived.readFloat();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readFloat didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readFloat threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readFloat threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageReceived.readDouble();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readDouble didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readDouble threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readDouble threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageReceived.readString();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readString didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readString threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readString threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageReceived.readBytes(bValuesReturned);
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: readBytes(byte[]) didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readBytes(byte[]) threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readBytes(byte[]) threw Wrong Exception!", e);
				pass = false;
			}

			try {
				messageReceived.readObject();
				pass = false;
				logger.log(Logger.Level.ERROR, "Error: readObject didn't throw Expected MessageNotReadableException!");
			} catch (MessageNotReadableException nr) {
				logger.log(Logger.Level.TRACE, "readObject threw Expected MessageNotReadableException!");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "readObject threw Wrong Exception!", e);
				pass = false;
			}

			if (pass != true)
				throw new Exception("xMessageNotReadableExceptionTStreamMsgTest Failed!");

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("xMessageNotReadableExceptionTStreamMsgTest:");
		} finally {
			try {
				tool.closeDefaultConnections();
			} catch (Exception ex) {
				logger.log(Logger.Level.ERROR, "Error closing Connection", ex);
			}
		}
	}

	/*
	 * @testName: xIllegalStateExceptionTestQueueMethodsT
	 * 
	 * @assertion_ids: JMS:SPEC:185.6; JMS:SPEC:185.7; JMS:SPEC:185.8; JMS:SPEC:185;
	 *
	 * @test_Strategy: Create a TopicSession and call Queue specific methods
	 * inherited from Session, and verify that jakarta.jms.IllegalStateException is
	 * thrown.
	 */
	@Test
	public void xIllegalStateExceptionTestQueueMethodsT() throws Exception {

		try {
			boolean pass = true;
			Queue myQueue = null;

			// set up test tool for Topic
			tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
			myQueue = tool.createNewQueue("MY_QUEUE");

			try {
				tool.getDefaultTopicSession().createBrowser(myQueue);
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: TopicSession.createBrowser(Queue) didn't throw expected IllegalStateException.");
			} catch (jakarta.jms.IllegalStateException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected IllegalStateException from TopicSession.createBrowser(Queue)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: TopicSession.createBrowser(Queue) throw incorrect Exception: ",
						e);
				pass = false;
			}

			try {
				tool.getDefaultTopicSession().createBrowser(myQueue, "TEST = 'test'");
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: TopicSession.createBrowser(Queue, String) didn't throw expected IllegalStateException.");
			} catch (jakarta.jms.IllegalStateException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected IllegalStateException from TopicSession.createBrowser(Queue, String)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR,
						"Error: TopicSession.createBrowser(Queue, String) throw incorrect Exception: ", e);
				pass = false;
			}

			try {
				tool.getDefaultTopicSession().createTemporaryQueue();
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: TopicSession.createTemporayQueue() didn't throw expected IllegalStateException.");
			} catch (jakarta.jms.IllegalStateException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected IllegalStateException from TopicSession.createTemporayQueue()");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: TopicSession.createTemporayQueue() throw incorrect Exception: ",
						e);
				pass = false;
			}

			try {
				tool.getDefaultTopicSession().createQueue("foo");
				pass = false;
				logger.log(Logger.Level.ERROR,
						"Error: TopicSession.createQueue(String) didn't throw expected IllegalStateException.");
			} catch (jakarta.jms.IllegalStateException ex) {
				logger.log(Logger.Level.INFO,
						"Got expected IllegalStateException from TopicSession.createQueue(String)");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Error: TopicSession.createQueue(String) throw incorrect Exception: ",
						e);
				pass = false;
			}

			if (pass != true)
				throw new Exception("xIllegalStateExceptionTestQueueMethodsT Failed!");

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("xIllegalStateExceptionTestQueueMethodsT");
		} finally {
			try {
				tool.closeDefaultConnections();
			} catch (Exception ex) {
				logger.log(Logger.Level.ERROR, "Error closing Connection", ex);
			}
		}
	}
}
