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
package com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesT2;

import java.lang.System.Logger;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.commonee.MDB_T_Test;

import jakarta.ejb.EJB;


public class MDBClientIT {

	@EJB(name = "ejb/MDB_MSGT2_Test")
	private static MDB_T_Test hr;

	private Properties props = new Properties();

	private static final Logger logger = (Logger) System.getLogger(MDBClientIT.class.getName());

	/* Test setup: */
	/*
	 * @class.setup_props: jms_timeout; user; password; harness.log.port;
	 * harness.log.traceflag;
	 */
	@BeforeEach
	public void setup() throws Exception {
		try {
			if (hr == null) {
				throw new Exception("@EJB injection failed");
			}
			props.put("jms_timeout", System.getProperty("jms_property"));
			props.put("user", System.getProperty("user"));
			props.put("password", System.getProperty("password"));

			hr.setup(props);
			if (hr.isThereSomethingInTheQueue()) {
				logger.log(Logger.Level.TRACE, "Error: message(s) left in Q");
				hr.cleanTheQueue();
			} else {
				logger.log(Logger.Level.TRACE, "Nothing left in queue");
			}
			logger.log(Logger.Level.INFO, "Setup ok;");
		} catch (Exception e) {
			throw new Exception("Setup Failed!", e);
		}
	}

	/* Run tests */
	//
	/*
	 * @testName: mdbMessageObjectCopyTopicTest
	 *
	 * @assertion_ids: JMS:SPEC:85; JMS:JAVADOC:291;
	 *
	 * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
	 * create an object message. Write a StringBuffer to the message.. modify the
	 * StringBuffer and send the msg, verify that it does not effect the msg
	 */
	@Test
	public void mdbMessageObjectCopyTopicTest() throws Exception {
		String testCase1 = "messageObjectCopyTopicTestCreate";
		String testCase2 = "messageObjectCopyTopicTest";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase messageObjectCopyTopicTestCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from messageObjectCopyTopicTest");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: messageObjectCopyTopicTest failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbStreamMessageConversionTopicTestsBoolean
	 * 
	 * @assertion_ids: JMS:SPEC:75.1; JMS:SPEC:75.2; JMS:JAVADOC:219;
	 * JMS:JAVADOC:150; JMS:JAVADOC:128; JMS:JAVADOC:144; JMS:JAVADOC:723;
	 * JMS:JAVADOC:726; JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:735;
	 * JMS:JAVADOC:738; JMS:JAVADOC:741; JMS:JAVADOC:747;
	 * 
	 * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
	 * create a StreamMessage -. use StreamMessage method writeBoolean to write a
	 * boolean to the message. Verify the proper conversion support as in 3.11.3
	 */
	@Test
	public void mdbStreamMessageConversionTopicTestsBoolean() throws Exception {
		String testCase1 = "streamMessageConversionTopicTestsBooleanCreate";
		String testCase2 = "streamMessageConversionTopicTestsBoolean";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase streamMessageConversionTopicTestsBooleanCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from streamMessageConversionTopicTestsBoolean");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: streamMessageConversionTopicTestsBoolean failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbStreamMessageConversionTopicTestsByte
	 * 
	 * @assertion_ids: JMS:SPEC:75.3; JMS:SPEC:75.4; JMS:JAVADOC:152;
	 * JMS:JAVADOC:130; JMS:JAVADOC:132; JMS:JAVADOC:136; JMS:JAVADOC:138;
	 * JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:729; JMS:JAVADOC:738;
	 * JMS:JAVADOC:741; JMS:JAVADOC:747;
	 * 
	 * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
	 * create a StreamMessage -. use StreamMessage method writeByte to write a byte.
	 * Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void mdbStreamMessageConversionTopicTestsByte() throws Exception {
		String testCase1 = "streamMessageConversionTopicTestsByteCreate";
		String testCase2 = "streamMessageConversionTopicTestsByte";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase streamMessageConversionTopicTestsByteCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from streamMessageConversionTopicTestsByte");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: streamMessageConversionTopicTestsByte failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbStreamMessageConversionTopicTestsShort
	 * 
	 * @assertion_ids: JMS:SPEC:75.5; JMS:SPEC:75.6; JMS:JAVADOC:154;
	 * JMS:JAVADOC:132; JMS:JAVADOC:136; JMS:JAVADOC:138; JMS:JAVADOC:144;
	 * JMS:JAVADOC:720; JMS:JAVADOC:723; JMS:JAVADOC:729; JMS:JAVADOC:738;
	 * JMS:JAVADOC:741; JMS:JAVADOC:747;
	 * 
	 * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
	 * create a StreamMessage -. use StreamMessage method writeShort to write a
	 * short. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void mdbStreamMessageConversionTopicTestsShort() throws Exception {
		String testCase1 = "streamMessageConversionTopicTestsShortCreate";
		String testCase2 = "streamMessageConversionTopicTestsShort";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase streamMessageConversionTopicTestsShortCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from streamMessageConversionTopicTestsShort");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: streamMessageConversionTopicTestsShort failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbStreamMessageConversionTopicTestsInt
	 * 
	 * @assertion_ids: JMS:SPEC:75.9; JMS:SPEC:75.10; JMS:JAVADOC:158;
	 * JMS:JAVADOC:136; JMS:JAVADOC:138; JMS:JAVADOC:144; JMS:JAVADOC:720;
	 * JMS:JAVADOC:723; JMS:JAVADOC:725; JMS:JAVADOC:729; JMS:JAVADOC:738;
	 * JMS:JAVADOC:741; JMS:JAVADOC:747;
	 * 
	 * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
	 * create a StreamMessage -. use StreamMessage method writeInt to write an int.
	 * Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void mdbStreamMessageConversionTopicTestsInt() throws Exception {
		String testCase1 = "streamMessageConversionTopicTestsIntCreate";
		String testCase2 = "streamMessageConversionTopicTestsInt";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase streamMessageConversionTopicTestsIntCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from streamMessageConversionTopicTestsInt");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: streamMessageConversionTopicTestsInt failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbStreamMessageConversionTopicTestsLong
	 * 
	 * @assertion_ids: JMS:SPEC:75.11; JMS:SPEC:75.12; JMS:JAVADOC:160;
	 * JMS:JAVADOC:138; JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:723;
	 * JMS:JAVADOC:725; JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:738;
	 * JMS:JAVADOC:741; JMS:JAVADOC:747;
	 * 
	 * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
	 * create a StreamMessage -. use StreamMessage method writeLong to write a long.
	 * Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void mdbStreamMessageConversionTopicTestsLong() throws Exception {
		String testCase1 = "streamMessageConversionTopicTestsLongCreate";
		String testCase2 = "streamMessageConversionTopicTestsLong";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase streamMessageConversionTopicTestsLongCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from streamMessageConversionTopicTestsLong");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: streamMessageConversionTopicTestsLong failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbStreamMessageConversionTopicTestsFloat
	 * 
	 * @assertion_ids: JMS:SPEC:75.13; JMS:SPEC:75.14; JMS:JAVADOC:162;
	 * JMS:JAVADOC:140; JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:723;
	 * JMS:JAVADOC:725; JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:735;
	 * JMS:JAVADOC:741; JMS:JAVADOC:747;
	 * 
	 * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
	 * create a StreamMessage -. use StreamMessage method writeFloat to write a
	 * float. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void mdbStreamMessageConversionTopicTestsFloat() throws Exception {
		String testCase1 = "streamMessageConversionTopicTestsFloatCreate";
		String testCase2 = "streamMessageConversionTopicTestsFloat";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase streamMessageConversionTopicTestsFloatCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from streamMessageConversionTopicTestsFloat");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: streamMessageConversionTopicTestsFloat failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbStreamMessageConversionTopicTestsDouble
	 * 
	 * @assertion_ids: JMS:SPEC:75.15; JMS:SPEC:75.16; JMS:JAVADOC:164;
	 * JMS:JAVADOC:142; JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:723;
	 * JMS:JAVADOC:725; JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:735;
	 * JMS:JAVADOC:738; JMS:JAVADOC:747;
	 * 
	 * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
	 * create a StreamMessage -. use StreamMessage method writeDouble to write a
	 * double. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void mdbStreamMessageConversionTopicTestsDouble() throws Exception {
		String testCase1 = "streamMessageConversionTopicTestsDoubleCreate";
		String testCase2 = "streamMessageConversionTopicTestsDouble";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase streamMessageConversionTopicTestsDoubleCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from streamMessageConversionTopicTestsDouble");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: streamMessageConversionTopicTestsDouble failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbStreamMessageConversionTopicTestsString
	 * 
	 * @assertion_ids: JMS:SPEC:75.17; JMS:SPEC:75.18; JMS:SPEC:77; JMS:JAVADOC:166;
	 * JMS:JAVADOC:128; JMS:JAVADOC:130; JMS:JAVADOC:132; JMS:JAVADOC:136;
	 * JMS:JAVADOC:138; JMS:JAVADOC:140; JMS:JAVADOC:142; JMS:JAVADOC:144;
	 * JMS:JAVADOC:729; JMS:JAVADOC:747;
	 * 
	 * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
	 * create a StreamMessage -. use StreamMessage method writeString to write a
	 * string. Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void mdbStreamMessageConversionTopicTestsString() throws Exception {
		String testCase1 = "streamMessageConversionTopicTestsStringCreate";
		String testCase2 = "streamMessageConversionTopicTestsString";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase streamMessageConversionTopicTestsStringCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from streamMessageConversionTopicTestsString");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: streamMessageConversionTopicTestsString failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbStreamMessageConversionTopicTestsChar
	 * 
	 * @assertion_ids: JMS:SPEC:75.7; JMS:SPEC:75.8; JMS:JAVADOC:156;
	 * JMS:JAVADOC:134; JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:723;
	 * JMS:JAVADOC:726; JMS:JAVADOC:732; JMS:JAVADOC:735; JMS:JAVADOC:738;
	 * JMS:JAVADOC:741; JMS:JAVADOC:747;
	 * 
	 * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
	 * create a StreamMessage -. use StreamMessage method writeChar to write a char.
	 * Verify the proper conversion support as in 3.11.3
	 * 
	 */
	@Test
	public void mdbStreamMessageConversionTopicTestsChar() throws Exception {
		String testCase1 = "streamMessageConversionTopicTestsCharCreate";
		String testCase2 = "streamMessageConversionTopicTestsChar";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase streamMessageConversionTopicTestsCharCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from streamMessageConversionTopicTestsChar");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: streamMessageConversionTopicTestsChar failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbStreamMessageConversionTopicTestsBytes
	 * 
	 * @assertion_ids: JMS:SPEC:75.19; JMS:SPEC:75.20; JMS:JAVADOC:168;
	 * JMS:JAVADOC:146; JMS:JAVADOC:720; JMS:JAVADOC:723; JMS:JAVADOC:725;
	 * JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:735; JMS:JAVADOC:738;
	 * JMS:JAVADOC:741; JMS:JAVADOC:744;
	 * 
	 * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
	 * create a StreamMessage -. use StreamMessage method writeBytes to write a
	 * byte[] to the message. Verify the proper conversion support as in 3.11.3
	 */
	@Test
	public void mdbStreamMessageConversionTopicTestsBytes() throws Exception {
		String testCase1 = "streamMessageConversionTopicTestsBytesCreate";
		String testCase2 = "streamMessageConversionTopicTestsBytes";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase streamMessageConversionTopicTestsBytesCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from streamMessageConversionTopicTestsBytes");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: streamMessageConversionTopicTestsBytes failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbStreamMessageConversionTopicTestsInvFormatString
	 * 
	 * @assertion_ids: JMS:SPEC:76; JMS:SPEC:81; JMS:JAVADOC:166; JMS:JAVADOC:130;
	 * JMS:JAVADOC:132; JMS:JAVADOC:136; JMS:JAVADOC:138; JMS:JAVADOC:140;
	 * JMS:JAVADOC:142; JMS:JAVADOC:144; JMS:JAVADOC:146;
	 * 
	 * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
	 * create a StreamMessage -. use StreamMessage method writeString to write a
	 * text string of "mytest string". Verify NumberFormatException is thrown Verify
	 * that the pointer was not incremented by doing a read string
	 * 
	 */
	@Test
	public void mdbStreamMessageConversionTopicTestsInvFormatString() throws Exception {
		String testCase1 = "streamMessageConversionTopicTestsInvFormatStringCreate";
		String testCase2 = "streamMessageConversionTopicTestsInvFormatString";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase streamMessageConversionTopicTestsInvFormatStringCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from streamMessageConversionTopicTestsInvFormatString");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: streamMessageConversionTopicTestsInvFormatString failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbStreamMessageTopicTestsFullMsg
	 * 
	 * @assertion_ids: JMS:SPEC:82; JMS:JAVADOC:150; JMS:JAVADOC:152;
	 * JMS:JAVADOC:154; JMS:JAVADOC:156; JMS:JAVADOC:158; JMS:JAVADOC:160;
	 * JMS:JAVADOC:162; JMS:JAVADOC:164; JMS:JAVADOC:166; JMS:JAVADOC:168;
	 * JMS:JAVADOC:170; JMS:JAVADOC:172; JMS:JAVADOC:128; JMS:JAVADOC:130;
	 * JMS:JAVADOC:132; JMS:JAVADOC:134; JMS:JAVADOC:136; JMS:JAVADOC:138;
	 * JMS:JAVADOC:140; JMS:JAVADOC:142; JMS:JAVADOC:144; JMS:JAVADOC:146;
	 * JMS:JAVADOC:148;
	 * 
	 * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
	 * create a StreamMessage -. write one of each primitive type. Send the message.
	 * Verify the data received was as sent.
	 * 
	 */
	@Test
	public void mdbStreamMessageTopicTestsFullMsg() throws Exception {
		String testCase1 = "streamMessageTopicTestsFullMsgCreate";
		String testCase2 = "streamMessageTopicTestsFullMsg";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase streamMessageTopicTestsFullMsgCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from streamMessageTopicTestsFullMsg");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: streamMessageTopicTestsFullMsg failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbStreamMessageTopicTestNull
	 * 
	 * @assertion_ids: JMS:SPEC:78; JMS:SPEC:86; JMS:JAVADOC:144; JMS:JAVADOC:172;
	 * 
	 * @test_Strategy: Invoke a session bean. Have it send a request to an mdb to
	 * create a StreamMessage -. Use writeString to write a null, then use
	 * readString to read it back.
	 */
	@Test
	public void mdbStreamMessageTopicTestNull() throws Exception {
		String testCase1 = "streamMessageTopicTestNullCreate";
		String testCase2 = "streamMessageTopicTestNull";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase streamMessageTopicTestNullCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from streamMessageTopicTestNull");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: streamMessageTopicTestNull failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/* cleanup -- none in this case */
	@AfterEach
	public void cleanup() throws Exception {
		try {
			if (hr.isThereSomethingInTheQueue()) {
				logger.log(Logger.Level.TRACE, "Error: message(s) left in Q");
				hr.cleanTheQueue();
			} else {
				logger.log(Logger.Level.TRACE, "Nothing left in queue");
			}
			logger.log(Logger.Level.INFO, "End  of client cleanup;");
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
		;
	}

}
