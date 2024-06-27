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
package com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesQ3;

import java.lang.System.Logger;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.commonee.MDB_Q_Test;

import jakarta.ejb.EJB;


public class MDBClientIT {

	@EJB(name = "ejb/MDB_MSGQ3_Test")
	private static MDB_Q_Test hr;

	private Properties props = new Properties();

	private static final Logger logger = (Logger) System.getLogger(MDBClientIT.class.getName());

	/* Test setup: */
	/*
	 * @class.setup_props: jms_timeout; user; password;
	 */
	@BeforeEach
	public void setup() throws Exception {
		try {
			props.put("jms_timeout", System.getProperty("jms_property"));
			props.put("user", System.getProperty("user"));
			props.put("password", System.getProperty("password"));

			if (hr == null) {
				throw new Exception("@EJB injection failed");
			}
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
	 * @testName: mdbMsgClearBodyQueueTextTest
	 *
	 * @assertion_ids: JMS:SPEC:71; JMS:SPEC:72; JMS:JAVADOC:431; JMS:JAVADOC:473;
	 * JMS:JAVADOC:449; JMS:SPEC:178; JMS:JAVADOC:291;
	 *
	 * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
	 * Create and send a Text message. Have the mdb read the message call clearBody,
	 * verify body is empty after clearBody. verify properties are not effected by
	 * clearBody. Write to the message again 3.11
	 */
	@Test
	public void mdbMsgClearBodyQueueTextTest() throws Exception {
		String testCase1 = "msgClearBodyQueueTextTestCreate";
		String testCase2 = "msgClearBodyQueueTextTest";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase msgClearBodyQueueTextTestCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from msgClearBodyQueueTextTest");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: msgClearBodyQueueTextTest failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbMsgClearBodyQueueObjectTest
	 *
	 * @assertion_ids: JMS:SPEC:71; JMS:SPEC:72; JMS:JAVADOC:431; JMS:JAVADOC:473;
	 * JMS:JAVADOC:449; JMS:SPEC:178; JMS:JAVADOC:291;
	 *
	 * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
	 * Create and send a Object message. Have the mdb read the message call
	 * clearBody, verify body is empty after clearBody. verify properties are not
	 * effected by clearBody. Write to the message again 3.11
	 */
	@Test
	public void mdbMsgClearBodyQueueObjectTest() throws Exception {
		String testCase1 = "msgClearBodyQueueObjectTestCreate";
		String testCase2 = "msgClearBodyQueueObjectTest";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase msgClearBodyQueueObjectTestCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from msgClearBodyQueueObjectTest");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: msgClearBodyQueueObjectTest failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbMsgClearBodyQueueMapTest
	 *
	 * @assertion_ids: JMS:SPEC:71; JMS:SPEC:72; JMS:JAVADOC:431; JMS:JAVADOC:473;
	 * JMS:JAVADOC:449; JMS:SPEC:178; JMS:JAVADOC:291;
	 *
	 * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
	 * Create and send a Map message. Have the mdb read the message call clearBody,
	 * verify body is empty after clearBody. verify properties are not effected by
	 * clearBody. Write to the message again 3.11
	 */
	@Test
	public void mdbMsgClearBodyQueueMapTest() throws Exception {
		String testCase1 = "msgClearBodyQueueMapTestCreate";
		String testCase2 = "msgClearBodyQueueMapTest";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase msgClearBodyQueueMapTestCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from msgClearBodyQueueMapTest");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: msgClearBodyQueueMapTest failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbMsgClearBodyQueueBytesTest
	 *
	 * @assertion_ids: JMS:SPEC:71; JMS:SPEC:72; JMS:JAVADOC:431; JMS:JAVADOC:473;
	 * JMS:JAVADOC:449; JMS:SPEC:178; JMS:JAVADOC:291;
	 *
	 * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
	 * Create and send a Bytes message. Have the mdb read the message call
	 * clearBody, verify body is empty after clearBody. verify properties are not
	 * effected by clearBody. Write to the message again 3.11
	 */
	@Test
	public void mdbMsgClearBodyQueueBytesTest() throws Exception {
		String testCase1 = "msgClearBodyQueueBytesTestCreate";
		String testCase2 = "msgClearBodyQueueBytesTest";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase msgClearBodyQueueBytesTestCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from msgClearBodyQueueBytesTest");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: msgClearBodyQueueBytesTest failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbMsgClearBodyQueueStreamTest
	 *
	 * @assertion_ids: JMS:SPEC:71; JMS:SPEC:72; JMS:JAVADOC:431; JMS:JAVADOC:473;
	 * JMS:JAVADOC:449; JMS:SPEC:178; JMS:JAVADOC:291;
	 *
	 * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
	 * Create and send a Stream message. Have the mdb read the message call
	 * clearBody, verify body is empty after clearBody. verify properties are not
	 * effected by clearBody. Write to the message again 3.11
	 */
	@Test
	public void mdbMsgClearBodyQueueStreamTest() throws Exception {
		String testCase1 = "msgClearBodyQueueStreamTestCreate";
		String testCase2 = "msgClearBodyQueueStreamTest";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase msgClearBodyQueueStreamTestCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from msgClearBodyQueueStreamTest");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: msgClearBodyQueueStreamTest failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbMsgResetQueueTest
	 *
	 * @assertion_ids: JMS:JAVADOC:174; JMS:JAVADOC:584;
	 *
	 * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
	 * create a stream message and a byte message. write to the message body, call
	 * the reset method, try to write to the body expect a
	 * MessageNotWriteableException to be thrown.
	 */
	@Test
	public void mdbMsgResetQueueTest() throws Exception {
		String testCase = "msgResetQueueTest";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase msgResetQueueTest");
			hr.askMDBToRunATest(testCase); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from msgResetQueueTest");
			if (!hr.checkOnResponse(testCase)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: msgResetQueueTest failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbReadNullCharNotValidQueueMapTest
	 *
	 * @assertion_ids: JMS:SPEC:79; JMS:JAVADOC:134; JMS:JAVADOC:439;
	 *
	 * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
	 * write a null string to a MapMessage. Attempt to read the null value as a
	 * char.
	 */
	@Test
	public void mdbReadNullCharNotValidQueueMapTest() throws Exception {
		String testCase1 = "readNullCharNotValidQueueMapTestCreate";
		String testCase2 = "readNullCharNotValidQueueMapTest";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase readNullCharNotValidQueueMapTestCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from readNullCharNotValidQueueMapTest");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: readNullCharNotValidQueueMapTest failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbReadNullCharNotValidQueueStreamTest
	 *
	 * @assertion_ids: JMS:SPEC:79; JMS:JAVADOC:134; JMS:JAVADOC:439;
	 *
	 * @test_Strategy: Call a session bean. Have the session bean invoke an mdb to
	 * write a null string to a StreamMessage. Attempt to read the null value as a
	 * char.
	 */
	@Test
	public void mdbReadNullCharNotValidQueueStreamTest() throws Exception {
		String testCase1 = "readNullCharNotValidQueueStreamTestCreate";
		String testCase2 = "readNullCharNotValidQueueStreamTest";
		try {
			// Have the EJB invoke the MDB
			System.out.println("client - run testcase readNullCharNotValidQueueStreamTestCreate");
			hr.askMDBToRunATest(testCase1); // create and send message to MDB_QUEUE
			System.out.println("client - Check for response from readNullCharNotValidQueueStreamTest");
			if (!hr.checkOnResponse(testCase2)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: readNullCharNotValidQueueStreamTest failed");
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
