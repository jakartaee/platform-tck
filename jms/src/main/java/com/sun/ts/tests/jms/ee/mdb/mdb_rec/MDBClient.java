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

package com.sun.ts.tests.jms.ee.mdb.mdb_rec;

import java.lang.System.Logger;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;

import jakarta.ejb.EJB;

public class MDBClient {

	@EJB(name = "ejb/MDB_AR_Test")
	private static MDB_AR_Test hr;

	private Properties props = null;

	private static final Logger logger = (Logger) System.getLogger(MDBClient.class.getName());

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
	// Tests mdb asynchronous receives
	// Test with Queue and Topic
	// Test with Text,Stream,Byte,Map and Object messages
	//

	/*
	 * @testName: asynRecTextMsgQueueTest
	 * 
	 * @assertion_ids: EJB:SPEC:506; JMS:JAVADOC:270; JMS:JAVADOC:274;
	 * JMS:JAVADOC:221; JMS:JAVADOC:188; JMS:JAVADOC:120; JMS:JAVADOC:425;
	 * JMS:JAVADOC:198;
	 *
	 * @test_Strategy: Test with a Text message Create a stateful Session EJB Bean.
	 * Deploy it on the J2EE server. Have the EJB component send a text message to a
	 * Queue Destination. handled by a message-driven bean Verify that the mdb
	 * received the message
	 *
	 *
	 */
	public void asynRecTextMsgQueueTest() throws Exception {
		String matchMe;

		try {
			// Have the EJB invoke the MDB
			logger.log(Logger.Level.INFO, "Call bean - have it send mdb a message;");

			matchMe = "mdb_asynchRecFromQueue_Text_Msg_Test";
			hr.sendTextMessageToQ(matchMe);
			if (!hr.checkOnResponse(matchMe)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: test1 failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: asynRecMapMsgQueueTest
	 * 
	 * @assertion_ids: EJB:SPEC:506; JMS:JAVADOC:270; JMS:JAVADOC:274;
	 * JMS:JAVADOC:221; JMS:JAVADOC:188; JMS:JAVADOC:120; JMS:JAVADOC:425;
	 * JMS:JAVADOC:198; JMS:JAVADOC:211; JMS:JAVADOC:473;
	 *
	 * @test_Strategy: Test with a Map message Create a stateful Session EJB Bean.
	 * Deploy it on the J2EE server. Have the EJB component send a text message to a
	 * Queue Destination. handled by a message-driven bean Verify that the mdb
	 * received the message
	 *
	 *
	 */
	@Test
	public void asynRecMapMsgQueueTest() throws Exception {
		String matchMe;

		try {
			// Have the EJB invoke the MDB
			logger.log(Logger.Level.INFO, "Call bean  - have it send mdb a message;");

			matchMe = "mdb_asynchRecFromQueue_MapMessage_Test";
			logger.log(Logger.Level.TRACE, "Have bean send mdb a map message;");
			hr.sendMapMessageToQ(matchMe);
			if (!hr.checkOnResponse(matchMe)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: test3 failed");
			}

			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: asynRecObjectMsgQueueTest
	 * 
	 * @assertion_ids: EJB:SPEC:506; JMS:JAVADOC:270; JMS:JAVADOC:274;
	 * JMS:JAVADOC:221; JMS:JAVADOC:188; JMS:JAVADOC:120; JMS:JAVADOC:425;
	 * JMS:JAVADOC:198; JMS:JAVADOC:215; JMS:JAVADOC:289;
	 *
	 * @test_Strategy: Test with a Object message Create a stateful Session EJB
	 * Bean. Deploy it on the J2EE server. Have the EJB component send a text
	 * message to a Queue Destination. handled by a message-driven bean Verify that
	 * the mdb received the message
	 *
	 *
	 */
	@Test
	public void asynRecObjectMsgQueueTest() throws Exception {
		String matchMe;

		try {
			// Have the EJB invoke the MDB
			logger.log(Logger.Level.INFO, "Call bean - have it send mdb a message;");

			matchMe = "mdb_asynchRecFromQueue_ObjectMessage_Test";
			logger.log(Logger.Level.TRACE, "Have bean send mdb a object message;");
			hr.sendObjectMessageToQ(matchMe);
			if (!hr.checkOnResponse(matchMe)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: test5 failed");
			}

			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: asynRecTextMsgTopicTest
	 * 
	 * @assertion_ids: EJB:SPEC:506; JMS:JAVADOC:270; JMS:JAVADOC:274;
	 * JMS:JAVADOC:221; JMS:JAVADOC:188; JMS:JAVADOC:120; JMS:JAVADOC:425;
	 * JMS:JAVADOC:198; JMS:JAVADOC:109; JMS:JAVADOC:111; JMS:JAVADOC:91;
	 * JMS:JAVADOC:99;
	 *
	 * @test_Strategy: Testing with a Text message Create a stateful Session EJB
	 * Bean. Deploy it on the J2EE server. Have the EJB component send a Text
	 * message to a Topic Destination. handled by a message-driven bean Verify that
	 * the mdb received the message
	 *
	 *
	 */
	@Test
	public void asynRecTextMsgTopicTest() throws Exception {
		String matchMe = "mdb_asynchRecFromTopic_Text_Msg_Test";
		try {
			// Have the EJB invoke the MDB
			logger.log(Logger.Level.INFO, "Call bean  - have it send mdb a message;");
			hr.sendTextMessageToTopic(matchMe);
			if (!hr.checkOnResponse(matchMe)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: test6 failed");
			}
			logger.log(Logger.Level.INFO, "Test passed;");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: asynRecBytesMsgQueueTest
	 * 
	 * @assertion_ids: EJB:SPEC:506; JMS:JAVADOC:270; JMS:JAVADOC:274;
	 * JMS:JAVADOC:221; JMS:JAVADOC:188; JMS:JAVADOC:120; JMS:JAVADOC:425;
	 * JMS:JAVADOC:198; JMS:JAVADOC:209; JMS:JAVADOC:562;
	 *
	 * @test_Strategy: Test with a Bytes message Create a stateful Session EJB Bean.
	 * Deploy it on the J2EE server. Have the EJB component send a text message to a
	 * Queue Destination. handled by a message-driven bean Verify that the mdb
	 * received the message
	 *
	 *
	 */
	@Test
	public void asynRecBytesMsgQueueTest() throws Exception {
		String matchMe;

		try {
			// Have the EJB invoke the MDB
			logger.log(Logger.Level.INFO, "Call bean  - have it send mdb a message;");

			matchMe = "mdb_asynchRecFromQueue_BytesMessage_Test";
			logger.log(Logger.Level.TRACE, "Have bean send mdb a bytes message;");
			hr.sendBytesMessageToQ(matchMe);
			if (!hr.checkOnResponse(matchMe)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: test2 failed");
			}

			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: asynRecStreamMsgQueueTest
	 * 
	 * @assertion_ids: EJB:SPEC:506; JMS:JAVADOC:270; JMS:JAVADOC:274;
	 * JMS:JAVADOC:221; JMS:JAVADOC:188; JMS:JAVADOC:120; JMS:JAVADOC:425;
	 * JMS:JAVADOC:198; JMS:JAVADOC:219; JMS:JAVADOC:166;
	 *
	 * @test_Strategy: Test with a Stream message Create a stateful Session EJB
	 * Bean. Deploy it on the J2EE server. Have the EJB component send a text
	 * message to a Queue Destination. handled by a message-driven bean Verify that
	 * the mdb received the message
	 *
	 *
	 */
	@Test
	public void asynRecStreamMsgQueueTest() throws Exception {
		String matchMe;

		try {
			// Have the EJB invoke the MDB
			logger.log(Logger.Level.INFO, "Call bean - have it send mdb a message;");

			matchMe = "mdb_asynchRecFromQueue_StreamMessage_Test";
			logger.log(Logger.Level.TRACE, "Have bean send mdb a stream message;");
			hr.sendStreamMessageToQ(matchMe);
			if (!hr.checkOnResponse(matchMe)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: test4 failed");
			}
			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: asynRecMapMsgTopicTest
	 * 
	 * @assertion_ids: EJB:SPEC:506; JMS:JAVADOC:270; JMS:JAVADOC:274;
	 * JMS:JAVADOC:221; JMS:JAVADOC:188; JMS:JAVADOC:120; JMS:JAVADOC:425;
	 * JMS:JAVADOC:198; JMS:JAVADOC:109; JMS:JAVADOC:111; JMS:JAVADOC:91;
	 * JMS:JAVADOC:99; JMS:JAVADOC:211; JMS:JAVADOC:473;
	 *
	 * @test_Strategy: Testing with a Map message Create a stateful Session EJB
	 * Bean. Deploy it on the J2EE server. Have the EJB component send a Map message
	 * to a Topic Destination. handled by a message-driven bean Verify that the mdb
	 * received the message
	 *
	 *
	 */
	@Test
	public void asynRecMapMsgTopicTest() throws Exception {
		String matchMe = "mdb_asynchRecFromTopic_Map_Msg_Test";
		try {
			// Have the EJB invoke the MDB
			logger.log(Logger.Level.INFO, "Call bean - have it send mdb a message;");
			hr.sendMapMessageToTopic(matchMe);
			if (!hr.checkOnResponse(matchMe)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: asynRecMapMsgTopicTest failed");
			}
			logger.log(Logger.Level.INFO, "Test passed;");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: asynRecObjectMsgTopicTest
	 * 
	 * @assertion_ids: EJB:SPEC:506; JMS:JAVADOC:270; JMS:JAVADOC:274;
	 * JMS:JAVADOC:221; JMS:JAVADOC:188; JMS:JAVADOC:120; JMS:JAVADOC:425;
	 * JMS:JAVADOC:198; JMS:JAVADOC:109; JMS:JAVADOC:111; JMS:JAVADOC:91;
	 * JMS:JAVADOC:99; JMS:JAVADOC:215; JMS:JAVADOC:289;
	 *
	 * @test_Strategy: Testing with a Object message Create a stateful Session EJB
	 * Bean. Deploy it on the J2EE server. Have the EJB component send a Object
	 * message to a Topic Destination. handled by a message-driven bean Verify that
	 * the mdb received the message
	 *
	 *
	 */
	@Test
	public void asynRecObjectMsgTopicTest() throws Exception {
		String matchMe = "mdb_asynchRecFromTopic_Object_Msg_Test";
		try {
			// Have the EJB invoke the MDB
			logger.log(Logger.Level.INFO, "Call bean - have it send mdb a message;");
			hr.sendObjectMessageToTopic(matchMe);
			if (!hr.checkOnResponse(matchMe)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: asynRecObjectMsgTopicTest failed");
			}
			logger.log(Logger.Level.INFO, "Test passed;");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: asynRecBytesMsgTopicTest
	 * 
	 * @assertion_ids: EJB:SPEC:506; JMS:JAVADOC:270; JMS:JAVADOC:274;
	 * JMS:JAVADOC:221; JMS:JAVADOC:188; JMS:JAVADOC:120; JMS:JAVADOC:425;
	 * JMS:JAVADOC:198; JMS:JAVADOC:109; JMS:JAVADOC:111; JMS:JAVADOC:91;
	 * JMS:JAVADOC:99; JMS:JAVADOC:209; JMS:JAVADOC:562;
	 *
	 * @test_Strategy: Testing with a Bytes message Create a stateful Session EJB
	 * Bean. Deploy it on the J2EE server. Have the EJB component send a Bytes
	 * message to a Topic Destination. handled by a message-driven bean Verify that
	 * the mdb received the message
	 *
	 *
	 */
	@Test
	public void asynRecBytesMsgTopicTest() throws Exception {
		String matchMe = "mdb_asynchRecFromTopic_Bytes_Msg_Test";
		try {
			// Have the EJB invoke the MDB
			logger.log(Logger.Level.INFO, "Call bean - have it send mdb a message;");
			hr.sendBytesMessageToTopic(matchMe);
			if (!hr.checkOnResponse(matchMe)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: asynRecBytesMsgTopicTest failed");
			}
			logger.log(Logger.Level.INFO, "Test passed;");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: asynRecStreamMsgTopicTest
	 * 
	 * @assertion_ids: EJB:SPEC:506; JMS:JAVADOC:270; JMS:JAVADOC:274;
	 * JMS:JAVADOC:221; JMS:JAVADOC:188; JMS:JAVADOC:120; JMS:JAVADOC:425;
	 * JMS:JAVADOC:198; JMS:JAVADOC:109; JMS:JAVADOC:111; JMS:JAVADOC:91;
	 * JMS:JAVADOC:99; JMS:JAVADOC:219; JMS:JAVADOC:166;
	 *
	 * @test_Strategy: Testing with a Stream message Create a stateful Session EJB
	 * Bean. Deploy it on the J2EE server. Have the EJB component send a Stream
	 * message to a Topic Destination. handled by a message-driven bean Verify that
	 * the mdb received the message
	 *
	 *
	 */
	@Test
	public void asynRecStreamMsgTopicTest() throws Exception {
		String matchMe = "mdb_asynchRecFromTopic_Stream_Msg_Test";
		try {
			// Have the EJB invoke the MDB
			logger.log(Logger.Level.INFO, "Call bean - have it send mdb a message;");
			hr.sendStreamMessageToTopic(matchMe);
			if (!hr.checkOnResponse(matchMe)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: asynRecStreamMsgTopicTest failed");
			}
			logger.log(Logger.Level.INFO, "Test passed;");
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
			logger.log(Logger.Level.ERROR, "Unexpected Exception cleaning up the Queue", e);
		} finally {
			logger.log(Logger.Level.TRACE, "Closing all connections");
			try {
				hr.cleanup();
			} catch (Exception oh) {
				logger.log(Logger.Level.ERROR, "Fail: unexpected exception closing Connections", oh);
				TestUtil.printStackTrace(oh);
				throw new Exception("Fail");
			}
			logger.log(Logger.Level.TRACE, "Removing EJBs");
			try {
				hr.remove();
			} catch (Exception oh) {
				logger.log(Logger.Level.ERROR, "Fail: unexpected exception removing EJB", oh);
				TestUtil.printStackTrace(oh);
				throw new Exception("Fail");
			}
		}
	}
}
