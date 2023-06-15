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
package com.sun.ts.tests.jms.ee.mdb.mdb_sndToTopic;

import java.lang.System.Logger;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.tests.jms.commonee.MDB_T_Test;

import jakarta.ejb.EJB;

/**
 * The MDBClient class invokes a test session bean, which will ask and the
 * message driven bean to send a text, byte, map, stream, and object message to
 * a topic Sun's EJB Reference Implementation.
 */

public class MDBClient {

	@EJB(name = "ejb/MDB_SNDToTopic_Test")
	private static MDB_T_Test hr;

	private Properties props = null;

	private static final Logger logger = (Logger) System.getLogger(MDBClient.class.getName());

	/* Test setup: */
	/*
	 * @class.setup_props: jms_timeout; user; password
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
	// Tests mdb sending jms messages
	// Tests with Topic
	// Tests with Text,Stream,Byte,Map and Object messages
	//
	/*
	 * @testName: mdbSendTextMsgToTopicTest
	 *
	 * @assertion_ids: JavaEE:SPEC:214; JMS:JAVADOC:109; JMS:JAVADOC:111;
	 * JMS:JAVADOC:91; JMS:JAVADOC:221; JMS:JAVADOC:120; JMS:JAVADOC:425;
	 * JMS:JAVADOC:99; JMS:JAVADOC:270; JMS:JAVADOC:522; JMS:JAVADOC:188;
	 * JMS:JAVADOC:198;
	 *
	 * @test_Strategy: Instruct the mdb to send a text msg. Create a stateful
	 * Session EJB Bean. Deploy it on the J2EE server. Have the EJB component send a
	 * message to a Topic Destination. handled by a message-driven bean Tell the mdb
	 * (MsgBeanToTopic) to send a text message with TopicPublisher to a topic that
	 * is handled by a second mdb(MsgBeanTopic). MsgBeanTopic verifies it received
	 * the message by sending another message to a Queue - MDB_QUEUE_REPLY.
	 *
	 */
	@Test
	public void mdbSendTextMsgToTopicTest() throws Exception {
		String messageType = "TextMessage";
		String matchMe = "TextMessageFromMsgBean";
		try {
			// Have the EJB invoke the MDB
			logger.log(Logger.Level.TRACE, "Call bean - have it tell mdb to send a text message;");
			// subscribe
			hr.askMDBToSendAMessage(messageType);
			// getMessage
			if (!hr.checkOnResponse(matchMe)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: mdbSendBytesMsgToTopicTest failed");
			}
			logger.log(Logger.Level.TRACE, "mdbSendTextMsgToTopicTest passed!");

		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}

	}

	/*
	 * @testName: mdbSendBytesMsgToTopicTest
	 *
	 * @assertion_ids: JavaEE:SPEC:214; JMS:JAVADOC:109; JMS:JAVADOC:111;
	 * JMS:JAVADOC:91; JMS:JAVADOC:221; JMS:JAVADOC:120; JMS:JAVADOC:425;
	 * JMS:JAVADOC:99; JMS:JAVADOC:270; JMS:JAVADOC:522; JMS:JAVADOC:188;
	 * JMS:JAVADOC:198; JMS:JAVADOC:209; JMS:JAVADOC:562;
	 *
	 * @test_Strategy: Instruct the mdb to send a bytes msg. Create a stateful
	 * Session EJB Bean. Deploy it on the J2EE server. Have the EJB component send a
	 * message to a Topic Destination. handled by a message-driven bean Tell the mdb
	 * (MsgBeanToTopic) to send a bytes message with TopicPublisher to a topic that
	 * is handled by a second mdb(MsgBeanTopic). MsgBeanTopic verifies it received
	 * the message by sending another message to a Queue - MDB_QUEUE_REPLY.
	 *
	 */
	@Test
	public void mdbSendBytesMsgToTopicTest() throws Exception {
		String messageType = "BytesMessage";
		String matchMe = "BytesMessageFromMsgBean";
		try {
			// Have the EJB invoke the MDB
			logger.log(Logger.Level.TRACE, "Call bean - have it tell mdb to send a Bytes message;");
			hr.askMDBToSendAMessage(messageType);
			if (!hr.checkOnResponse(matchMe)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: mdbSendBytesMsgToTopicTest failed");
			}

			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbSendMapMsgToTopicTest
	 *
	 * @assertion_ids: JavaEE:SPEC:214; JMS:JAVADOC:109; JMS:JAVADOC:111;
	 * JMS:JAVADOC:91; JMS:JAVADOC:221; JMS:JAVADOC:120; JMS:JAVADOC:425;
	 * JMS:JAVADOC:99; JMS:JAVADOC:270; JMS:JAVADOC:522; JMS:JAVADOC:188;
	 * JMS:JAVADOC:198; JMS:JAVADOC:211; JMS:JAVADOC:473;
	 *
	 * @test_Strategy: Instruct the mdb to send a map msg. Create a stateful Session
	 * EJB Bean. Deploy it on the J2EE server. Have the EJB component send a message
	 * to a Topic Destination. handled by a message-driven bean Tell the mdb
	 * (MsgBeanToTopic) to send a map message with TopicPublisher to a topic that is
	 * handled by a second mdb(MsgBeanTopic). MsgBeanTopic verifies it received the
	 * message by sending another message to a Queue - MDB_QUEUE_REPLY.
	 *
	 */
	@Test
	public void mdbSendMapMsgToTopicTest() throws Exception {
		String matchMe = "MapMessageFromMsgBean";
		String messageType = "MapMessage";
		try {
			// Have the EJB invoke the MDB
			logger.log(Logger.Level.TRACE, "Call bean - have it tell mdb to send a map message;");
			hr.askMDBToSendAMessage(messageType);
			if (!hr.checkOnResponse(matchMe)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: mdbSendMapMsgToTopicTest failed");
			}

			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}

	}

	/*
	 * @testName: mdbSendStreamMsgToTopicTest
	 *
	 * @assertion_ids: JavaEE:SPEC:214; JMS:JAVADOC:109; JMS:JAVADOC:111;
	 * JMS:JAVADOC:91; JMS:JAVADOC:221; JMS:JAVADOC:120; JMS:JAVADOC:425;
	 * JMS:JAVADOC:99; JMS:JAVADOC:270; JMS:JAVADOC:522; JMS:JAVADOC:188;
	 * JMS:JAVADOC:198; JMS:JAVADOC:219; JMS:JAVADOC:166;
	 *
	 * @test_Strategy: Instruct the mdb to send a stream msg. Create a stateful
	 * Session EJB Bean. Deploy it on the J2EE server. Have the EJB component send a
	 * message to a Topic Destination. handled by a message-driven bean Tell the mdb
	 * (MsgBeanToTopic) to send a stream message with TopicPublisher to a topic that
	 * is handled by a second mdb(MsgBeanTopic). MsgBeanTopic verifies it received
	 * the message by sending another message to a Queue - MDB_QUEUE_REPLY.
	 *
	 */
	@Test
	public void mdbSendStreamMsgToTopicTest() throws Exception {
		String matchMe = "StreamMessageFromMsgBean";
		String messageType = "StreamMessage";
		try {
			// Have the EJB invoke the MDB
			logger.log(Logger.Level.TRACE, "Call bean - have it tell mdb to send a stream message;");
			hr.askMDBToSendAMessage(messageType);
			if (!hr.checkOnResponse(matchMe)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: mdbSendStreamMsgToTopicTest failed");
			}

			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: mdbSendObjectMsgToTopicTest
	 *
	 * @assertion_ids: JavaEE:SPEC:214; JMS:JAVADOC:109; JMS:JAVADOC:111;
	 * JMS:JAVADOC:91; JMS:JAVADOC:221; JMS:JAVADOC:120; JMS:JAVADOC:425;
	 * JMS:JAVADOC:99; JMS:JAVADOC:270; JMS:JAVADOC:522; JMS:JAVADOC:188;
	 * JMS:JAVADOC:198; JMS:JAVADOC:215; JMS:JAVADOC:289;
	 *
	 * @test_Strategy: Instruct the mdb to send an object msg. Create a stateful
	 * Session EJB Bean. Deploy it on the J2EE server. Have the EJB component send a
	 * message to a Topic Destination. handled by a message-driven bean Tell the mdb
	 * (MsgBeanToTopic) to send an object message with TopicPublisher to a topic
	 * that is handled by a second mdb(MsgBeanTopic). MsgBeanTopic verifies it
	 * received the message by sending another message to a Queue - MDB_QUEUE_REPLY.
	 *
	 */
	@Test
	public void mdbSendObjectMsgToTopicTest() throws Exception {
		String matchMe = "ObjectMessageFromMsgBean";
		String messageType = "ObjectMessage";
		try {
			// Have the EJB invoke the MDB
			logger.log(Logger.Level.TRACE, "Call bean - have it tell mdb to send an object message;");
			hr.askMDBToSendAMessage(messageType);
			if (!hr.checkOnResponse(matchMe)) {
				logger.log(Logger.Level.TRACE, "Error: didn't get expected response from mdb");
				throw new Exception("ERROR: mdbSendObjectMsgToTopicTest failed");
			}

			logger.log(Logger.Level.TRACE, "Test passed!");
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/* cleanup -- none in this case */
	@AfterEach
	public void cleanup() throws Exception {
		logger.log(Logger.Level.INFO, "End  of client cleanup;");
	}
}
