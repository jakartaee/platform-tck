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
package com.sun.ts.tests.jms.core.streamMsgQueue;

import java.lang.System.Logger;
import java.util.ArrayList;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;

import jakarta.jms.MessageNotWriteableException;
import jakarta.jms.StreamMessage;

public class StreamMsgQueueTestsIT {
	private static final String testName = "com.sun.ts.tests.jms.core.streamMsgQueue.StreamMsgQueueTestsIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(StreamMsgQueueTestsIT.class.getName());

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
	 * @testName: streamMessageConversionQTestsBoolean
	 * 
	 * @assertion_ids: JMS:SPEC:75.1; JMS:SPEC:75.2; JMS:JAVADOC:219;
	 * JMS:JAVADOC:150; JMS:JAVADOC:128; JMS:JAVADOC:144; JMS:JAVADOC:723;
	 * JMS:JAVADOC:726; JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:735;
	 * JMS:JAVADOC:738; JMS:JAVADOC:741; JMS:JAVADOC:747;
	 * 
	 * @test_Strategy: Create a StreamMessage -. use StreamMessage method
	 * writeBoolean to write a boolean to the message. Verify the proper conversion
	 * support as in 3.11.3
	 */
	@Test
	public void streamMessageConversionQTestsBoolean() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			byte bValue = 127;
			boolean abool = true;
			byte[] bValues = { 0 };
			char charValue = 'a';
			short sValue = 1;
			long lValue = 2;
			double dValue = 3;
			float fValue = 5;
			int iValue = 6;
			boolean pass = true;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createStreamMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionQTestsBoolean");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for boolean primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.writeBoolean(abool);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);

			// now test conversions for boolean
			// -----------------------------------------------
			// boolean to boolean - valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBoolean to read a boolean");
			try {
				if (messageReceived.readBoolean() == abool) {
					logger.log(Logger.Level.TRACE, "Pass: boolean to boolean - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// boolean to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readString to read a boolean");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readString().equals((Boolean.valueOf(abool)).toString())) {
					logger.log(Logger.Level.TRACE, "Pass: boolean to string - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// boolean to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBytes[] to read a boolean - expect MessageFormatException");
			int nCount = 0;

			try {

				// position to beginning of stream message.
				messageReceived.reset();
				nCount = messageReceived.readBytes(bValues);
				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Count returned from readBytes is : " + nCount);

			// -----------------------------------------------
			// boolean to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readByte to read a boolean - expect MessageFormatException");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				messageReceived.readByte();
				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// boolean to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readShort to read a boolean - expect MessageFormatException");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				messageReceived.readShort();
				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// boolean to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readChar to read a boolean - expect MessageFormatException");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				messageReceived.readChar();
				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// boolean to int invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readInt to read a boolean - expect MessageFormatException");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				messageReceived.readInt();
				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// boolean to long invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readLong to read a boolean - expect MessageFormatException");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				messageReceived.readLong();
				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// boolean to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readFloat to read a boolean - expect MessageFormatException");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				messageReceived.readFloat();
				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// boolean to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readDouble to read a boolean - expect MessageFormatException");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				messageReceived.readDouble();
				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("streamMessageConversionQTestsBoolean", e);
		}
	}

	/*
	 * @testName: streamMessageConversionQTestsByte
	 * 
	 * @assertion_ids: JMS:SPEC:75.3; JMS:SPEC:75.4; JMS:JAVADOC:152;
	 * JMS:JAVADOC:130; JMS:JAVADOC:132; JMS:JAVADOC:136; JMS:JAVADOC:138;
	 * JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:729; JMS:JAVADOC:738;
	 * JMS:JAVADOC:741; JMS:JAVADOC:747;
	 * 
	 * @test_Strategy: Create a StreamMessage -. use StreamMessage method writeByte
	 * to write a byte. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void streamMessageConversionQTestsByte() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			byte bValue = 127;
			boolean abool = true;
			byte[] bValues = { 0 };
			char charValue = 'a';
			short sValue = 1;
			long lValue = 2;
			double dValue = 3;
			float fValue = 5;
			int iValue = 6;
			boolean pass = true;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createStreamMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionQTestsByte");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.writeByte(bValue);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);

			// now test conversions for byte
			// -----------------------------------------------
			// byte to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readByte to read a boolean - this is not valid");
			try {
				messageReceived.reset();
				boolean b = messageReceived.readBoolean();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// byte to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readString to read a byte");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readString().equals(Byte.toString(bValue))) {
					logger.log(Logger.Level.TRACE, "Pass: byte to string - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// byte to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBytes[] to read a byte - expect MessageFormatException");
			int nCount = 0;

			try {

				// position to beginning of stream message.
				messageReceived.reset();
				nCount = messageReceived.readBytes(bValues);
				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Count returned from readBytes is : " + nCount);

			// -----------------------------------------------
			// byte to byte valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readByte to read a byte");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readByte() == bValue) {
					logger.log(Logger.Level.TRACE, "Pass: byte to byte - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// byte to short valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readShort to read a byte");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readShort() == bValue) {
					logger.log(Logger.Level.TRACE, "Pass: byte to short - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// byte to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readChar to read a boolean - this is not valid");
			try {
				messageReceived.reset();
				char c = messageReceived.readChar();

				pass = false;
				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// byte to int valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readInt to read a byte");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readInt() == bValue) {
					logger.log(Logger.Level.TRACE, "Pass: byte to int - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// byte to long valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readLong to read a byte");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readLong() == bValue) {
					logger.log(Logger.Level.TRACE, "Pass: byte to long - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// byte to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readFloat to read a boolean - this is not valid");
			try {
				messageReceived.reset();
				float f = messageReceived.readFloat();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// byte to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readDouble to read a boolean - this is not valid");
			try {
				messageReceived.reset();
				double d = messageReceived.readDouble();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("streamMessageConversionQTestsByte", e);
		}
	}

	/*
	 * @testName: streamMessageConversionQTestsShort
	 * 
	 * @assertion_ids: JMS:SPEC:75.5; JMS:SPEC:75.6; JMS:JAVADOC:154;
	 * JMS:JAVADOC:132; JMS:JAVADOC:136; JMS:JAVADOC:138; JMS:JAVADOC:144;
	 * JMS:JAVADOC:720; JMS:JAVADOC:723; JMS:JAVADOC:729; JMS:JAVADOC:738;
	 * JMS:JAVADOC:741; JMS:JAVADOC:747;
	 * 
	 * @test_Strategy: Create a StreamMessage -. use StreamMessage method writeShort
	 * to write a short. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void streamMessageConversionQTestsShort() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			byte bValue = 127;
			boolean abool = true;
			byte[] bValues = { 0 };
			char charValue = 'a';
			short sValue = 1;
			long lValue = 2;
			double dValue = 3;
			float fValue = 5;
			int iValue = 6;
			boolean pass = true;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createStreamMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionQTestsShort");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.writeShort(sValue);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);

			// now test conversions for byte
			// -----------------------------------------------
			// short to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBoolean to read a short - this is not valid");
			try {
				messageReceived.reset();
				boolean b = messageReceived.readBoolean();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// short to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readString to read a short");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readString().equals(Short.toString(sValue))) {
					logger.log(Logger.Level.TRACE, "Pass: short to string - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// short to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBytes[] to read a short - expect MessageFormatException");
			int nCount = 0;

			try {

				// position to beginning of stream message.
				messageReceived.reset();
				nCount = messageReceived.readBytes(bValues);
				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Count returned from readBytes is : " + nCount);

			// -----------------------------------------------
			// short to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readByte to read a short - this is not valid");
			try {
				messageReceived.reset();
				byte b = messageReceived.readByte();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// short to short valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readShort to read a short");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readShort() == sValue) {
					logger.log(Logger.Level.TRACE, "Pass: short to short - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// short to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readChar to read a short - this is not valid");
			try {
				messageReceived.reset();
				char c = messageReceived.readChar();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// short to int valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readInt to read a byte");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readInt() == sValue) {
					logger.log(Logger.Level.TRACE, "Pass: short to int - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// short to long valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readLong to read a short");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readLong() == sValue) {
					logger.log(Logger.Level.TRACE, "Pass: short to long - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// short to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readFloat to read a short - this is not valid");
			try {
				messageReceived.reset();
				float f = messageReceived.readFloat();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// short to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readDouble to read a short - this is not valid");
			try {
				messageReceived.reset();
				double d = messageReceived.readDouble();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("streamMessageConversionQTestsShort", e);
		}
	}

	/*
	 * @testName: streamMessageConversionQTestsInt
	 * 
	 * @assertion_ids: JMS:SPEC:75.9; JMS:SPEC:75.10; JMS:JAVADOC:158;
	 * JMS:JAVADOC:136; JMS:JAVADOC:138; JMS:JAVADOC:144; JMS:JAVADOC:720;
	 * JMS:JAVADOC:723; JMS:JAVADOC:726; JMS:JAVADOC:729; JMS:JAVADOC:738;
	 * JMS:JAVADOC:741; JMS:JAVADOC:747;
	 * 
	 * @test_Strategy: Create a StreamMessage -. use StreamMessage method writeInt
	 * to write an int. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void streamMessageConversionQTestsInt() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			byte bValue = 127;
			boolean abool = true;
			byte[] bValues = { 0 };
			char charValue = 'a';
			short sValue = 1;
			long lValue = 2;
			double dValue = 3;
			float fValue = 5;
			int iValue = 6;
			boolean pass = true;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createStreamMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionQTestsInt");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.writeInt(iValue);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);

			// now test conversions for byte
			// -----------------------------------------------
			// int to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBoolean to read an int - this is not valid");
			try {
				messageReceived.reset();
				boolean b = messageReceived.readBoolean();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// int to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readString to read an int");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readString().equals(Integer.toString(iValue))) {
					logger.log(Logger.Level.TRACE, "Pass: int to string - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// int to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBytes[] to read an int - expect MessageFormatException");
			int nCount = 0;

			try {

				// position to beginning of stream message.
				messageReceived.reset();
				nCount = messageReceived.readBytes(bValues);
				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Count returned from readBytes is : " + nCount);

			// -----------------------------------------------
			// int to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readByte to read an int - this is not valid");
			try {
				messageReceived.reset();
				byte b = messageReceived.readByte();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// int to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readShort to read an int");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				short s = messageReceived.readShort();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// int to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readChar to read an int - this is not valid");
			try {
				messageReceived.reset();
				char c = messageReceived.readChar();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// int to int valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readInt to read an int");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readInt() == iValue) {
					logger.log(Logger.Level.TRACE, "Pass: int to int - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// int to long valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readLong to read an int");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readLong() == iValue) {
					logger.log(Logger.Level.TRACE, "Pass: int to long - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// int to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readFloat to read an int - this is not valid");
			try {
				messageReceived.reset();
				float f = messageReceived.readFloat();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// int to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readDouble to read an int - this is not valid");
			try {
				messageReceived.reset();
				double d = messageReceived.readDouble();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("streamMessageConversionQTestsInt", e);
		}
	}

	/*
	 * @testName: streamMessageConversionQTestsLong
	 * 
	 * @assertion_ids: JMS:SPEC:75.11; JMS:SPEC:75.12; JMS:JAVADOC:160;
	 * JMS:JAVADOC:138; JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:723;
	 * JMS:JAVADOC:726; JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:738;
	 * JMS:JAVADOC:741; JMS:JAVADOC:747;
	 * 
	 * @test_Strategy: Create a StreamMessage -. use StreamMessage method writeLong
	 * to write a long. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void streamMessageConversionQTestsLong() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			byte bValue = 127;
			boolean abool = true;
			byte[] bValues = { 0 };
			char charValue = 'a';
			short sValue = 1;
			long lValue = 2;
			double dValue = 3;
			float fValue = 5;
			int iValue = 6;
			boolean pass = true;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createStreamMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionQTestsLong");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.writeLong(lValue);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);

			// now test conversions for byte
			// -----------------------------------------------
			// long to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBoolean to read a long - this is not valid");
			try {
				messageReceived.reset();
				boolean b = messageReceived.readBoolean();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// long to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readString to read a long");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readString().equals(Long.toString(lValue))) {
					logger.log(Logger.Level.TRACE, "Pass: long to string - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// long to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBytes[] to read  a long - expect MessageFormatException");
			int nCount = 0;

			try {

				// position to beginning of stream message.
				messageReceived.reset();
				nCount = messageReceived.readBytes(bValues);
				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Count returned from readBytes is : " + nCount);

			// -----------------------------------------------
			// long to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readByte to read an long - this is not valid");
			try {
				messageReceived.reset();
				byte b = messageReceived.readByte();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// long to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readShort to read a long");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				short s = messageReceived.readShort();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// long to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readChar to read a long - this is not valid");
			try {
				messageReceived.reset();
				char c = messageReceived.readChar();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// long to int invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readInt to read a long");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				int i = messageReceived.readInt();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// long to long valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readLong to read a long");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readLong() == lValue) {
					logger.log(Logger.Level.TRACE, "Pass: int to long - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// long to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readFloat to read a long - this is not valid");
			try {
				messageReceived.reset();
				float f = messageReceived.readFloat();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// long to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readDouble to read an long - this is not valid");
			try {
				messageReceived.reset();
				double d = messageReceived.readDouble();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("streamMessageConversionQTestsLong", e);
		}
	}

	/*
	 * @testName: streamMessageConversionQTestsFloat
	 * 
	 * @assertion_ids: JMS:SPEC:75.13; JMS:SPEC:75.14; JMS:JAVADOC:162;
	 * JMS:JAVADOC:140; JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:723;
	 * JMS:JAVADOC:726; JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:735;
	 * JMS:JAVADOC:741; JMS:JAVADOC:747;
	 * 
	 * @test_Strategy: Create a StreamMessage -. use StreamMessage method writeFloat
	 * to write a float. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void streamMessageConversionQTestsFloat() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			byte bValue = 127;
			boolean abool = true;
			byte[] bValues = { 0 };
			char charValue = 'a';
			short sValue = 1;
			long lValue = 2;
			double dValue = 3;
			float fValue = 5;
			int iValue = 6;
			boolean pass = true;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createStreamMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionQTestsFloat");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.writeFloat(fValue);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);

			// now test conversions for byte
			// -----------------------------------------------
			// float to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBoolean to read a float  ");
			try {
				messageReceived.reset();
				boolean b = messageReceived.readBoolean();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// float to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readString to read a float");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readString().equals(Float.toString(fValue))) {
					logger.log(Logger.Level.TRACE, "Pass: float to string - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// float to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBytes[] to read  a float ");
			int nCount = 0;

			try {

				// position to beginning of stream message.
				messageReceived.reset();
				nCount = messageReceived.readBytes(bValues);
				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Count returned from readBytes is : " + nCount);

			// -----------------------------------------------
			// float to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readByte to read a float  ");
			try {
				messageReceived.reset();
				byte b = messageReceived.readByte();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// float to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readShort to read a float");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				short s = messageReceived.readShort();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// float to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readChar to read a long  ");
			try {
				messageReceived.reset();
				char c = messageReceived.readChar();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// float to int invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readInt to read a float");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				int i = messageReceived.readInt();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// float to long invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readLong to read a long");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				long l = messageReceived.readLong();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// float to float valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readFloat to read a float  ");
			try {
				messageReceived.reset();
				if (messageReceived.readFloat() == fValue) {
					logger.log(Logger.Level.TRACE, "Pass: float to float - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// float to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readDouble to read an float  ");
			try {
				messageReceived.reset();
				if (messageReceived.readDouble() == fValue) {
					logger.log(Logger.Level.TRACE, "Pass: float to double - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("streamMessageConversionQTestsFloat", e);
		}
	}

	/*
	 * @testName: streamMessageConversionQTestsDouble
	 * 
	 * @assertion_ids: JMS:SPEC:75.15; JMS:SPEC:75.16; JMS:JAVADOC:164;
	 * JMS:JAVADOC:142; JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:723;
	 * JMS:JAVADOC:726; JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:735;
	 * JMS:JAVADOC:738; JMS:JAVADOC:747;
	 * 
	 * @test_Strategy: Create a StreamMessage -. use StreamMessage method
	 * writeDouble to write a double. Verify the proper conversion support as in
	 * 3.11.3
	 * 
	 */
	@Test
	public void streamMessageConversionQTestsDouble() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			byte bValue = 127;
			boolean abool = true;
			byte[] bValues = { 0 };
			char charValue = 'a';
			short sValue = 1;
			long lValue = 2;
			double dValue = 3;
			float fValue = 5;
			int iValue = 6;
			boolean pass = true;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createStreamMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionQTestsDouble");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.writeDouble(dValue);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);

			// now test conversions for byte
			// -----------------------------------------------
			// double to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBoolean to read a double  ");
			try {
				messageReceived.reset();
				boolean b = messageReceived.readBoolean();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// double to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readString to read a double");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readString().equals(Double.toString(dValue))) {
					logger.log(Logger.Level.TRACE, "Pass: double to string");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// double to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBytes[] to read  a double ");
			int nCount = 0;

			try {

				// position to beginning of stream message.
				messageReceived.reset();
				nCount = messageReceived.readBytes(bValues);
				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Count returned from readBytes is : " + nCount);

			// -----------------------------------------------
			// double to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readByte to read a double  ");
			try {
				messageReceived.reset();
				byte b = messageReceived.readByte();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// double to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readShort to read a double");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				short s = messageReceived.readShort();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// double to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readChar to read a double  ");
			try {
				messageReceived.reset();
				char c = messageReceived.readChar();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// double to int invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readInt to read a double");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				int i = messageReceived.readInt();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// double to long invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readLong to read a double");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				long l = messageReceived.readLong();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// double to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readFloat to read a double  ");
			try {
				messageReceived.reset();
				float f = messageReceived.readFloat();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// double to double valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readDouble to read a double  ");
			try {
				messageReceived.reset();
				if (messageReceived.readDouble() == dValue) {
					logger.log(Logger.Level.TRACE, "Pass: double to double ");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("streamMessageConversionQTestsDouble", e);
		}
	}

	/*
	 * @testName: streamMessageConversionQTestsString
	 * 
	 * @assertion_ids: JMS:SPEC:75.17; JMS:SPEC:75.18; JMS:SPEC:77; JMS:JAVADOC:166;
	 * JMS:JAVADOC:128; JMS:JAVADOC:130; JMS:JAVADOC:132; JMS:JAVADOC:136;
	 * JMS:JAVADOC:138; JMS:JAVADOC:140; JMS:JAVADOC:142; JMS:JAVADOC:144;
	 * JMS:JAVADOC:729; JMS:JAVADOC:747;
	 * 
	 * @test_Strategy: Create a StreamMessage -. use StreamMessage method
	 * writeString to write a string. Verify the proper conversion support as in
	 * 3.11.3
	 * 
	 */
	@Test
	public void streamMessageConversionQTestsString() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			byte bValue = 127;
			boolean abool = true;
			byte[] bValues = { 0 };
			char charValue = 'a';
			short sValue = 1;
			long lValue = 2;
			double dValue = 3;
			float fValue = 5;
			int iValue = 6;
			boolean pass = true;
			String myString = "10";

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createStreamMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionQTestsString");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.writeString(myString);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);

			// now test conversions for String
			// -----------------------------------------------
			// string to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readString to read a String");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readString().equals(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: string to string - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// string to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBytes[] to read a String");
			int nCount = 0;

			try {

				// position to beginning of stream message.
				messageReceived.reset();
				nCount = messageReceived.readBytes(bValues);
				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Count returned from readBytes is : " + nCount);

			// -----------------------------------------------
			// String to byte valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readByte to read a String");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readByte() == Byte.parseByte(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to byte ");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// string to short valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readShort to read a string");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readShort() == Short.parseShort(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to short ");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// String to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readChar to read a String ");
			try {
				messageReceived.reset();
				char c = messageReceived.readChar();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// string to int valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readInt to read a String");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readInt() == Integer.parseInt(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to int ");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// string to long valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readLong to read a String");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readLong() == Long.parseLong(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to long ");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// String to float valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readFloat to read a String");
			try {
				messageReceived.reset();
				if (messageReceived.readFloat() == Float.parseFloat(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to float ");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// String to double valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readDouble to read a String");
			try {
				messageReceived.reset();
				if (messageReceived.readDouble() == Double.parseDouble(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: String to double ");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// String to boolean
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBoolean to read a string ");
			try {
				messageReceived.clearBody();
				messageReceived.writeString("true");
				messageReceived.reset();
				if (messageReceived.readBoolean() == abool) {
					logger.log(Logger.Level.TRACE, "Pass: String to boolean ");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// String to boolean
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBoolean to read a string  that is !true ");
			try {
				messageReceived.reset();
				boolean b = messageReceived.readBoolean();

				if (b != true) {
					logger.log(Logger.Level.INFO, "Fail: !true should return false");
					pass = false;
				} else {
					logger.log(Logger.Level.TRACE, "Pass: !true returned false");
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("streamMessageConversionQTestsString", e);
		}
	}

	/*
	 * @testName: streamMessageConversionQTestsChar
	 * 
	 * @assertion_ids: JMS:SPEC:75.7; JMS:SPEC:75.8; JMS:JAVADOC:156;
	 * JMS:JAVADOC:134; JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:723;
	 * JMS:JAVADOC:726; JMS:JAVADOC:732; JMS:JAVADOC:735; JMS:JAVADOC:738;
	 * JMS:JAVADOC:741; JMS:JAVADOC:747;
	 * 
	 * @test_Strategy: Create a StreamMessage -. use StreamMessage method writeChar
	 * to write a char. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void streamMessageConversionQTestsChar() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			byte bValue = 127;
			boolean abool = true;
			byte[] bValues = { 0 };
			char charValue = 'a';
			short sValue = 1;
			long lValue = 2;
			double dValue = 3;
			float fValue = 5;
			int iValue = 6;
			boolean pass = true;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createStreamMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionQTestsChar");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.writeChar(charValue);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);

			// now test conversions for byte
			// -----------------------------------------------
			// char to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBoolean to read a char - this is not valid");
			try {
				messageReceived.reset();
				boolean b = messageReceived.readBoolean();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// char to string valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readString to read a char");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				String s = messageReceived.readString();

				logger.log(Logger.Level.TRACE, "char returned for \"a\" is : " + s);
				if (s.equals("a")) {
					logger.log(Logger.Level.TRACE, "Pass: char to string - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// char to byte[] invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBytes[] to read a char - expect MessageFormatException");
			int nCount = 0;

			try {

				// position to beginning of stream message.
				messageReceived.reset();
				nCount = messageReceived.readBytes(bValues);
				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			logger.log(Logger.Level.INFO, "Count returned from readBytes is : " + nCount);

			// -----------------------------------------------
			// char to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readByte to read a char - this is not valid");
			try {
				messageReceived.reset();
				byte b = messageReceived.readByte();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// char to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readShort to read a char");
			try {
				messageReceived.reset();
				short s = messageReceived.readShort();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// char to char valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readChar to read a char ");
			try {
				messageReceived.reset();
				if (messageReceived.readChar() == 'a') {
					logger.log(Logger.Level.TRACE, "Pass: char to char - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: wrong value returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// char to int invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readInt to read a char ");
			try {
				messageReceived.reset();
				int i = messageReceived.readInt();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// char to long invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readLong to read a char");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				long l = messageReceived.readLong();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// char to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readFloat to read a char - this is not valid");
			try {
				messageReceived.reset();
				float f = messageReceived.readFloat();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// char to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readDouble to read a char - this is not valid");
			try {
				messageReceived.reset();
				double d = messageReceived.readDouble();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("streamMessageConversionQTestsChar", e);
		}
	}

	/*
	 * @testName: streamMessageConversionQTestsBytes
	 * 
	 * @assertion_ids: JMS:SPEC:75.19; JMS:SPEC:75.20; JMS:JAVADOC:168;
	 * JMS:JAVADOC:146; JMS:JAVADOC:720; JMS:JAVADOC:723; JMS:JAVADOC:725;
	 * JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:735; JMS:JAVADOC:738;
	 * JMS:JAVADOC:741; JMS:JAVADOC:744;
	 * 
	 * @test_Strategy: Create a StreamMessage -. use StreamMessage method writeBytes
	 * to write a byte[] to the message. Verify the proper conversion support as in
	 * 3.11.3
	 */
	@Test
	public void streamMessageConversionQTestsBytes() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			byte bValue = 127;
			boolean abool = true;
			byte[] bValues = { 1, 2, 3 };
			byte[] bValues2 = { 0, 0, 0 };
			short sValue = 1;
			long lValue = 2;
			double dValue = 3;
			float fValue = 5;
			int iValue = 6;
			boolean pass = true;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createStreamMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionQTestsBytes");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte[] primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.writeBytes(bValues);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);

			// now test conversions for boolean
			// -----------------------------------------------
			// byte[] to byte[] - valid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBytes[] to read a byte[] ");
			int nCount = 0;

			try {

				// position to beginning of stream message.
				messageReceived.reset();
				if (messageReceived.readBytes(bValues2) == 3) { // count should be 3.
					logger.log(Logger.Level.TRACE, "Pass: byte[] to byte[] - valid");
				} else {
					logger.log(Logger.Level.INFO, "Fail: count incorrect");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// byte[] to boolean - invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readBoolean to read a byte[]");

			// position to beginning of stream message.
			messageReceived.reset();
			try {
				boolean b = messageReceived.readBoolean();

				logger.log(Logger.Level.INFO,
						"Fail: byte[] to boolean conversion should have thrown MessageFormatException");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// byte[] to string invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readString to read a byte[]");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				String s = messageReceived.readString();

				logger.log(Logger.Level.INFO,
						"Fail: byte[] to boolean conversion should have thrown MessageFormatException");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// byte[] to byte invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readByte to read a byte[] - expect MessageFormatException");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				byte b = messageReceived.readByte();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// byte[] to short invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readShort to read a byte[] - expect MessageFormatException");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				short s = messageReceived.readShort();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// byte[] to char invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readChar to read a byte[] - expect MessageFormatException");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				char c = messageReceived.readChar();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// byte[] to int invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readInt to read a byte[] - expect MessageFormatException");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				int i = messageReceived.readInt();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// byte[] to long invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readLong to read a byte[] - expect MessageFormatException");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				long l = messageReceived.readLong();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// byte[] to float invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readFloat to read a byte[] - expect MessageFormatException");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				float f = messageReceived.readFloat();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// byte[] to double invalid
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readDouble to read a byte[] - expect MessageFormatException");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				double d = messageReceived.readDouble();

				logger.log(Logger.Level.INFO, "Fail: MessageFormatException was not thrown");
				pass = false;
			} catch (Exception e) {
				if (e instanceof jakarta.jms.MessageFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: MessageFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("streamMessageConversionQTestsBytes", e);
		}
	}

	/*
	 * @testName: streamMessageConversionQTestsInvFormatString
	 * 
	 * @assertion_ids: JMS:SPEC:76; JMS:SPEC:81; JMS:JAVADOC:166; JMS:JAVADOC:130;
	 * JMS:JAVADOC:132; JMS:JAVADOC:136; JMS:JAVADOC:138; JMS:JAVADOC:140;
	 * JMS:JAVADOC:142; JMS:JAVADOC:144; JMS:JAVADOC:146;
	 * 
	 * @test_Strategy: Create a StreamMessage -. use StreamMessage method
	 * writeString to write a text string of "mytest string". Verify
	 * NumberFormatException is thrown Verify that the pointer was not incremented
	 * by doing a read string
	 * 
	 */
	@Test
	public void streamMessageConversionQTestsInvFormatString() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			boolean pass = true;
			String myString = "mytest string";

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createStreamMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageConversionQTestsInvFormatString");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "Verify conversion support for byte primitive type section 3.11.3");

			// -----------------------------------------------------------------------------
			messageSent.writeString(myString);

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);

			// -----------------------------------------------
			// String to byte
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readByte to read a String that is not valid ");
			try {
				byte b = messageReceived.readByte();

				logger.log(Logger.Level.INFO, "Fail: java.lang.NumberFormatException expected");
				pass = false;
			} catch (Exception e) {
				if (e instanceof java.lang.NumberFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// pointer should not have moved
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Verify that the data can be read as a string and pointer did not move");
			try {
				String s = messageReceived.readString();

				logger.log(Logger.Level.TRACE, "message read: " + s);
				if (s.equals(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: able to read the string");
				} else {
					logger.log(Logger.Level.INFO, "Fail: string not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}

			// -----------------------------------------------
			// string to short
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readShort to read a string that is not valid ");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				short s = messageReceived.readShort();

				logger.log(Logger.Level.INFO, "Fail: NumberFormatException was expected");
				pass = false;
			} catch (Exception e) {
				if (e instanceof java.lang.NumberFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// string to int
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readInt to read a String that is not valid ");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				int i = messageReceived.readInt();

				logger.log(Logger.Level.INFO, "Fail: NumberFormatException was expected");
			} catch (Exception e) {
				if (e instanceof java.lang.NumberFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// string to long
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readLong to read a String that is not valid ");
			try {

				// position to beginning of stream message.
				messageReceived.reset();
				long l = messageReceived.readLong();

				logger.log(Logger.Level.INFO, "Fail: NumberFormatException was expected");
				pass = false;
			} catch (Exception e) {
				if (e instanceof java.lang.NumberFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// String to float
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readFloat to read a String that is not valid ");
			try {
				messageReceived.reset();
				float f = messageReceived.readFloat();

				logger.log(Logger.Level.INFO, "Fail: NumberFormatException was expected");
				pass = false;
			} catch (Exception e) {
				if (e instanceof java.lang.NumberFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}

			// -----------------------------------------------
			// String to double
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "--");
			logger.log(Logger.Level.INFO, "Use readDouble to read a String that is not valid ");
			try {
				messageReceived.reset();
				double d = messageReceived.readDouble();

				logger.log(Logger.Level.INFO, "Fail: NumberFormatException was expected");
				pass = false;
			} catch (Exception e) {
				if (e instanceof java.lang.NumberFormatException) {
					logger.log(Logger.Level.TRACE, "Pass: NumberFormatException thrown as expected");
				} else {
					logger.log(Logger.Level.ERROR, "Unexpected exception " + e.getClass().getName() + " was thrown");
					pass = false;
				}
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("streamMessageConversionQTestsInvFormatString", e);
		}
	}

	/*
	 * @testName: streamMessageQTestsFullMsg
	 * 
	 * @assertion_ids: JMS:SPEC:82; JMS:JAVADOC:150; JMS:JAVADOC:152;
	 * JMS:JAVADOC:154; JMS:JAVADOC:156; JMS:JAVADOC:158; JMS:JAVADOC:160;
	 * JMS:JAVADOC:162; JMS:JAVADOC:164; JMS:JAVADOC:166; JMS:JAVADOC:168;
	 * JMS:JAVADOC:170; JMS:JAVADOC:172; JMS:JAVADOC:128; JMS:JAVADOC:130;
	 * JMS:JAVADOC:132; JMS:JAVADOC:134; JMS:JAVADOC:136; JMS:JAVADOC:138;
	 * JMS:JAVADOC:140; JMS:JAVADOC:142; JMS:JAVADOC:144; JMS:JAVADOC:146;
	 * JMS:JAVADOC:148;
	 * 
	 * @test_Strategy: Create a StreamMessage -. write one of each primitive type.
	 * Send the message. Verify the data received was as sent.
	 * 
	 */
	@Test
	public void streamMessageQTestsFullMsg() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			byte bValue = 127;
			boolean abool = false;
			byte[] bValues = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
			byte[] bValues2 = { 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
			byte[] bValuesReturned = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			byte[] bValuesReturned2 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			char charValue = 'Z';
			short sValue = 32767;
			long lValue = 9223372036854775807L;
			double dValue = 6.02e23;
			float fValue = 6.02e23f;
			int iValue = 6;
			boolean pass = true;
			String myString = "text";
			String sTesting = "Testing StreamMessages";

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			logger.log(Logger.Level.TRACE, "Creating 1 message");
			messageSent = tool.getDefaultQueueSession().createStreamMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageQTestsFullMsg");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "");

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
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);
			try {
				int nCount;
				do {
					nCount = messageReceived.readBytes(bValuesReturned2);
					logger.log(Logger.Level.TRACE, "nCount is " + nCount);
					if (nCount != -1) {
						for (int i = 0; i < bValuesReturned2.length; i++) {
							if (bValuesReturned2[i] != bValues2[i]) {
								logger.log(Logger.Level.INFO, "Fail: byte[] " + i + " is not valid");
								pass = false;
							} else {
								logger.log(Logger.Level.TRACE, "PASS: byte[]" + i + " is valid");
							}
						}
					}
				} while (nCount >= bValuesReturned2.length);
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readBoolean() == abool) {
					logger.log(Logger.Level.TRACE, "Pass: boolean returned ok");
				} else {
					logger.log(Logger.Level.INFO, "Fail: boolean not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readByte() == bValue) {
					logger.log(Logger.Level.TRACE, "Pass: Byte returned ok");
				} else {
					logger.log(Logger.Level.INFO, "Fail: Byte not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				int nCount;
				do {
					nCount = messageReceived.readBytes(bValuesReturned);
					logger.log(Logger.Level.TRACE, "nCount is " + nCount);
					if (nCount != -1) {
						for (int i = 0; i < bValuesReturned2.length; i++) {
							if (bValuesReturned2[i] != bValues2[i]) {
								logger.log(Logger.Level.INFO, "Fail: byte[] " + i + " is not valid");
								pass = false;
							} else {
								logger.log(Logger.Level.TRACE, "PASS: byte[]" + i + " is valid");
							}
						}
					}
				} while (nCount >= bValuesReturned2.length);
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readChar() == charValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct char");
				} else {
					logger.log(Logger.Level.INFO, "Fail: char not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readDouble() == dValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct double");
				} else {
					logger.log(Logger.Level.INFO, "Fail: double not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readFloat() == fValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct float");
				} else {
					logger.log(Logger.Level.INFO, "Fail: float not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readInt() == iValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct int");
				} else {
					logger.log(Logger.Level.INFO, "Fail: int not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readLong() == lValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct long");
				} else {
					logger.log(Logger.Level.INFO, "Fail: long not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readObject().equals(sTesting)) {
					logger.log(Logger.Level.TRACE, "Pass: correct object");
				} else {
					logger.log(Logger.Level.INFO, "Fail: object not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readShort() == sValue) {
					logger.log(Logger.Level.TRACE, "Pass: correct short");
				} else {
					logger.log(Logger.Level.INFO, "Fail: short not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readString().equals(myString)) {
					logger.log(Logger.Level.TRACE, "Pass: correct string");
				} else {
					logger.log(Logger.Level.INFO, "Fail: string not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			try {
				if (messageReceived.readObject() == null) {
					logger.log(Logger.Level.TRACE, "Pass: correct object");
				} else {
					logger.log(Logger.Level.INFO, "Fail: object not returned as expected");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "unexpected exception" + e.getClass().getName() + "was thrown");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("streamMessageQTestsFullMsg", e);
		}
	}

	/*
	 * @testName: streamMessageQTestNull
	 * 
	 * @assertion_ids: JMS:SPEC:78; JMS:SPEC:86; JMS:JAVADOC:144; JMS:JAVADOC:172;
	 * 
	 * @test_Strategy: Create a StreamMessage Use writeString to write a null, then
	 * use readString to read it back.
	 */
	@Test
	public void streamMessageQTestNull() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			boolean pass = true;

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			messageSent = tool.getDefaultQueueSession().createStreamMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageQTestNull");

			// -----------------------------------------------------------------------------
			logger.log(Logger.Level.TRACE, "writeString(null) ");

			// -----------------------------------------------------------------------------
			try {
				messageSent.writeString(null);
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "unexpected exception " + e.getClass().getName() + " was thrown");

				// It doesn't make sense to continue, throw an exception
				throw new Exception("Error: failed to write a null object with writeString", e);
			}

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);

			// -----------------------------------------------
			//
			// -----------------------------------------------
			logger.log(Logger.Level.INFO, "  ");
			try {
				if (messageReceived.readObject() == null) {
					logger.log(Logger.Level.TRACE, "Pass: Read a null");
				} else {
					logger.log(Logger.Level.INFO, "Fail: null value not returned");
					pass = false;
				}
			} catch (Exception e) {
				TestUtil.printStackTrace(e);
				logger.log(Logger.Level.ERROR, "trying to read a null object");
				logger.log(Logger.Level.ERROR, "unexpected exception " + e.getClass().getName() + " was thrown");
				pass = false;
			}
			if (!pass) {
				throw new Exception("Error: failures occurred during tests");
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("streamMessageQTestNull", e);
		}
	}

	/*
	 * @testName: streamMessageQNotWritable
	 *
	 * @assertion_ids: JMS:SPEC:73; JMS:JAVADOC:752; JMS:JAVADOC:753;
	 * JMS:JAVADOC:754; JMS:JAVADOC:755; JMS:JAVADOC:756; JMS:JAVADOC:757;
	 * JMS:JAVADOC:758; JMS:JAVADOC:759; JMS:JAVADOC:761; JMS:JAVADOC:762;
	 * JMS:JAVADOC:764;
	 *
	 * @test_Strategy: Create a StreamMessage, send it to a Queue. Receive it and
	 * try to write to the received Message's body, MessageNotWritableException
	 * should be thrown.
	 */
	@Test
	public void streamMessageQNotWritable() throws Exception {
		try {
			StreamMessage messageSent = null;
			StreamMessage messageReceived = null;
			boolean pass = true;
			byte bValue = 127;
			short sValue = 32767;
			char cValue = '\uFFFF';
			int iValue = 2147483647;
			long lValue = 9223372036854775807L;
			float fValue = 0.0f;
			double dValue = -0.0;
			String ssValue = "abc";
			byte[] bbValue = { 0, 88, 127 };

			// set up test tool for Queue
			tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
			tool.getDefaultQueueConnection().start();
			messageSent = tool.getDefaultQueueSession().createStreamMessage();
			messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "streamMessageQNotWritable");

			// -----------------------------------------------------------------------------
			try {
				messageSent.writeString("Test Message for streamMessageQNotWritable");
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "unexpected exception " + e.getClass().getName() + " was thrown");
				throw new Exception("Error: failed to writeString", e);
			}

			// send the message and then get it back
			logger.log(Logger.Level.TRACE, "Sending message");
			tool.getDefaultQueueSender().send(messageSent);
			logger.log(Logger.Level.TRACE, "Receiving message");
			messageReceived = (StreamMessage) tool.getDefaultQueueReceiver().receive(timeout);

			logger.log(Logger.Level.INFO, "Writing a boolean ... ");
			try {
				messageReceived.writeBoolean(pass);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeBoolean");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeBoolean");
			}

			logger.log(Logger.Level.INFO, "Writing a byte ... ");
			try {
				messageReceived.writeByte(bValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeByte");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeByte");
			}

			logger.log(Logger.Level.INFO, "Writing a short ... ");
			try {
				messageReceived.writeShort(sValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeShort");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeShort");
			}

			logger.log(Logger.Level.INFO, "Writing a char ... ");
			try {
				messageReceived.writeChar(cValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeChar");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeChar");
			}

			logger.log(Logger.Level.INFO, "Writing a int ... ");
			try {
				messageReceived.writeInt(iValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeInt");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeInt");
			}

			logger.log(Logger.Level.INFO, "Writing a long ... ");
			try {
				messageReceived.writeLong(lValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeLong");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeLong");
			}

			logger.log(Logger.Level.INFO, "Writing a float ... ");
			try {
				messageReceived.writeFloat(fValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeFloat");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeFloat");
			}

			logger.log(Logger.Level.INFO, "Writing a double ... ");
			try {
				messageReceived.writeDouble(dValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeDouble");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeDouble");
			}

			logger.log(Logger.Level.INFO, "Writing a bytes... ");
			try {
				messageReceived.writeBytes(bbValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeBytes");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeBytes");
			}

			logger.log(Logger.Level.INFO, "Writing a string ... ");
			try {
				messageReceived.writeString(ssValue);
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeString");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeString");
			}

			logger.log(Logger.Level.INFO, "Writing a object ... ");
			try {
				messageReceived.writeObject(new Integer(iValue));
				logger.log(Logger.Level.ERROR, "Shouldn't get here");
				throw new Exception("Error: test failed to be able to writeObject");
			} catch (MessageNotWriteableException e) {
				logger.log(Logger.Level.INFO, "Got Expected MessageNotWriteableException with writeObject");
			}

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("streamMessageQNotWritable", e);
		}
	}

}
