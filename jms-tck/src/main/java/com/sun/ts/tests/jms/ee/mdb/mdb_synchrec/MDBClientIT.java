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
package com.sun.ts.tests.jms.ee.mdb.mdb_synchrec;

import java.lang.System.Logger;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TSNamingContextInterface;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsUtil;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Queue;
import jakarta.jms.QueueConnection;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.QueueReceiver;
import jakarta.jms.QueueSender;
import jakarta.jms.QueueSession;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;


public class MDBClientIT {

	// Naming specific member variables
	private TSNamingContextInterface context = null;

	private Properties props = new Properties();

	private Queue mdbRcvrQueue;

	private Queue rcvrQueue;

	private QueueConnection qConnect;

	private Queue cmtQ;

	private QueueSession session;

	private QueueConnectionFactory qFactory;

	private QueueSender qSender;

	private String jmsUser = null;

	private String jmsPassword = null;

	private TextMessage msg = null;

	// get this from ts.jte
	long timeout;

	private static final Logger logger = (Logger) System.getLogger(MDBClientIT.class.getName());

	/*
	 * Test setup:
	 * 
	 * @class.setup_props: jms_timeout, in milliseconds - how long to wait on
	 * synchronous receive; user;password
	 *
	 */
	@BeforeEach
	public void setup() throws Exception {
		jmsUser = System.getProperty("user");
		jmsPassword = System.getProperty("password");
		try {
			timeout = Integer.parseInt(System.getProperty("jms_timeout"));

			logger.log(Logger.Level.TRACE, "in client setup");

			context = new TSNamingContext();
			logger.log(Logger.Level.TRACE, "Client: Do lookups!");
			qFactory = (QueueConnectionFactory) context.lookup("java:comp/env/jms/MyQueueConnectionFactory");
			cmtQ = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE");
			rcvrQueue = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
			mdbRcvrQueue = (Queue) context.lookup("java:comp/env/jms/MY_QUEUE");
			qConnect = qFactory.createQueueConnection(jmsUser, jmsPassword);
			session = qConnect.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			qConnect.start();
			cleanTheQueue(rcvrQueue);
			cleanTheQueue(mdbRcvrQueue);

		} catch (Exception e) {
			throw new Exception("Setup Failed!", e);
		}
	}
	/* Run tests */

	/*
	 * @testName: test1
	 *
	 * @assertion_ids: JavaEE:SPEC:214; JMS:JAVADOC:270; JMS:JAVADOC:522;
	 * JMS:JAVADOC:188; JMS:JAVADOC:221; JMS:JAVADOC:120; JMS:JAVADOC:425;
	 * JMS:JAVADOC:198; JMS:JAVADOC:184; JMS:JAVADOC:334; JMS:JAVADOC:405;
	 * 
	 * @test_Strategy: Verify synchronous receive in an mdb. send a msg to
	 * MDB_QURUR_REPLY - mdb will do a synchronous rec on it Invoke a cmt mdb by
	 * writing to MDB_QUEUE In onMessage mdb method, do a synchronous receive Notify
	 * the client by sending a message to QUEUE_REPLY if mdb was able to
	 * successfully receive the message
	 *
	 */
	@Test
	public void test1() throws Exception {
		String TestCase = "syncRecTest1";
		int TestNum = 1;
		String mdbMessage = "my mdb message";

		try {
			// create a text message
			createTestMessage(TestCase, TestNum);
			// send a message to receiver queue that the mdb can synchronously receive
			qSender = session.createSender(mdbRcvrQueue);
			// send the message to invoke mdb
			JmsUtil.addPropsToMessage(msg, props);
			msg.setStringProperty("TestCase", mdbMessage);
			qSender.send(msg);

			msg.setStringProperty("COM_SUN_JMS_TESTNAME", TestCase);
			qSender = session.createSender(cmtQ);
			// send the message to invoke mdb
			qSender.send(msg);

			// verify that message was requeued and pass
			TestCase = "mdbResponse";
			if (!checkOnResponse(TestCase)) {
				throw new Exception("syncRecTest1 - ");
			}
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/* cleanup -- none in this case */
	@AfterEach
	public void cleanup() throws Exception {
		try {
			msg = null;
			if (qConnect != null) {
				qConnect.close();
			}
			logger.log(Logger.Level.INFO, "End  of client cleanup;");
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	private void createTestMessage(String TestCase, int num) {
		String myMessage = "MDB synchronous receive test";
		try {
			msg = session.createTextMessage();
			msg.setStringProperty("user", jmsUser);
			msg.setStringProperty("password", jmsPassword);
			msg.setText(myMessage);
			msg.setIntProperty("TestCaseNum", num);

		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			logger.log(Logger.Level.INFO, "Error setting user and password in jms msg");
		}
	}

	public boolean checkOnResponse(String prop) {
		boolean status = false;
		try {
			logger.log(Logger.Level.TRACE, "@checkOnResponse");
			for (int i = 0; i < 10; i++) {
				status = getMessage(session, prop);
				if (status)
					break;
			}
			logger.log(Logger.Level.TRACE, "Close the session");
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
		return status;
	}

	private boolean getMessage(QueueSession session, String prop) throws Exception {
		try {
			logger.log(Logger.Level.TRACE, "top of getMessage");
			boolean gotit = false;
			String selector = "TestCase = 'mdbResponse'";
			QueueReceiver rcvr = session.createReceiver(rcvrQueue, selector);
			// dequeue the response from the mdb
			Message msgRec = null;
			msgRec = rcvr.receive(timeout);
			if (msgRec == null) {
				// not good
				logger.log(Logger.Level.TRACE, "No message to receive!!!");
			} else {
				logger.log(Logger.Level.TRACE, "Success: getMessage received a msg!!!");
				gotit = recvMessageInternal(msgRec, prop);
			}

			return gotit;
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			logger.log(Logger.Level.ERROR, "exception: ", e);
			throw new Exception("getMessage threw an exception!");
		}

	}

	private boolean recvMessageInternal(Message msgRec, String prop) throws JMSException {
		boolean retcode = false;
		logger.log(Logger.Level.TRACE, "@recvMessageInternal");
		if (msgRec != null) {

			logger.log(Logger.Level.TRACE, "Msg: " + msgRec.toString());
			logger.log(Logger.Level.TRACE, "TestCase: " + msgRec.getStringProperty("TestCase"));
			logger.log(Logger.Level.TRACE, "Status: " + msgRec.getStringProperty("Status"));
			logger.log(Logger.Level.TRACE, "=================================================");
			logger.log(Logger.Level.TRACE, "Msg: " + msgRec.toString());

			if (msgRec.getStringProperty("TestCase").equals(prop)
					&& msgRec.getStringProperty("Status").equals("Pass")) {
				logger.log(Logger.Level.TRACE, "TestCase: " + msgRec.getStringProperty("TestCase"));
				logger.log(Logger.Level.TRACE, "Status from msg: " + msgRec.getStringProperty("Status"));
				logger.log(Logger.Level.TRACE, "Pass: we got the expected msg back! ");
				retcode = true;
			} else if (msgRec.getStringProperty("Status").equals("Fail")) {
				logger.log(Logger.Level.TRACE, "TestCase: " + msgRec.getStringProperty("TestCase"));
				logger.log(Logger.Level.TRACE, "Status from msg: " + msgRec.getStringProperty("Status"));
				logger.log(Logger.Level.TRACE, "Fail: Error(s) occurred! ");
			} else {
				logger.log(Logger.Level.TRACE, "Fail: we didnt get the expected msg back! ");
				logger.log(Logger.Level.TRACE, "TestCase:  " + msgRec.getStringProperty("TestCase"));
			}
		} else if (msgRec == null) {
			logger.log(Logger.Level.TRACE, "Fail: we didnt get the expected msg back! ");
		}
		return retcode;
	}

	private void cleanTheQueue(Queue q) {

		try {
			// make sure nothing is left in QUEUE_REPLY
			logger.log(Logger.Level.TRACE, "Check if any messages left in queue");
			QueueReceiver qR = session.createReceiver(q);
			Message msg = qR.receive(timeout);
			while (msg != null) {
				logger.log(Logger.Level.TRACE, "Cleaned up a message in QUEUE!");
				msg = qR.receive(timeout);
			}
			qR.close();
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			logger.log(Logger.Level.TRACE, "Error in cleanTheQueue");
		}

	}

}
