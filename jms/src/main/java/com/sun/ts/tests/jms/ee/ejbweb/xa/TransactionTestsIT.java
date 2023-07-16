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
package com.sun.ts.tests.jms.ee.ejbweb.xa;

import java.lang.System.Logger;
import java.util.Enumeration;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TSNamingContextInterface;
import com.sun.ts.lib.util.TestUtil;

import jakarta.jms.Message;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;
import jakarta.jms.QueueConnection;
import jakarta.jms.QueueConnectionFactory;
import jakarta.jms.QueueReceiver;
import jakarta.jms.QueueSender;
import jakarta.jms.QueueSession;
import jakarta.jms.TextMessage;
import jakarta.jms.Topic;
import jakarta.jms.TopicConnection;
import jakarta.jms.TopicConnectionFactory;
import jakarta.jms.TopicPublisher;
import jakarta.jms.TopicSession;
import jakarta.jms.TopicSubscriber;
import jakarta.transaction.UserTransaction;


public class TransactionTestsIT {
	private static final String testName = "com.sun.ts.tests.jms.ee.ejbweb.xa.TransactionTestsIT";

	private static final String testDir = System.getProperty("user.dir");

	private static final long serialVersionUID = 1L;

	private static final Logger logger = (Logger) System.getLogger(TransactionTestsIT.class.getName());

	// Naming specific member variables
	private TSNamingContextInterface context = null;

	private Properties props = null;

	private String jmsUser;

	private String jmsPassword;

	long timeout;

	private transient QueueConnection qConnect = null;

	private transient Queue bmtQ = null;

	private transient QueueSession qSession = null;

	private transient QueueConnectionFactory qFactory = null;

	private transient QueueSender qSender = null;

	private transient QueueReceiver qReceiver = null;

	private transient Topic bmtT = null;

	private transient TopicConnection tConnect = null;

	private transient TopicSession tSession = null;

	private transient TopicConnectionFactory tFactory = null;

	private transient TopicPublisher tPublisher = null;

	private transient TopicSubscriber tSub = null;

	private transient TextMessage msg = null;

	private transient UserTransaction ut = null;

	private boolean transacted = true;

	private boolean queueTest = false;

	/* Test setup: */

	/*
	 * setup() is called before each test
	 * 
	 * Individual tests create a temporary Queue
	 * 
	 * @class.setup_props: jms_timeout; user; password;
	 * 
	 * @exception Fault
	 */
	@BeforeEach
	public void setup() throws Exception {
		try {
			jmsUser = System.getProperty("user");
			jmsPassword = System.getProperty("password");
			timeout = Long.parseLong(System.getProperty("jms_timeout"));

			// check props for errors
			if (timeout < 1) {
				throw new Exception("'jms_timeout' (milliseconds) in must be > 0");
			}
			if (jmsUser == null) {
				throw new Exception("'user' is null ");
			}
			if (jmsPassword == null) {
				throw new Exception("'password' is null ");
			}

			context = new TSNamingContext();
			qFactory = (QueueConnectionFactory) context.lookup("java:comp/env/jms/MyQueueConnectionFactory");
			bmtQ = (Queue) context.lookup("java:comp/env/jms/QUEUE_BMT");

			tFactory = (TopicConnectionFactory) context.lookup("java:comp/env/jms/MyTopicConnectionFactory");
			bmtT = (Topic) context.lookup("java:comp/env/jms/TOPIC_BMT");

			ut = (UserTransaction) context.lookup("java:comp/UserTransaction");
			logger.log(Logger.Level.TRACE, "in client setup");
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
			logger.log(Logger.Level.TRACE, "Cleanup");
			try {
				if (ut != null && ut.getStatus() != jakarta.transaction.Status.STATUS_NO_TRANSACTION) {
					logger.log(Logger.Level.TRACE, "Cleanup open transaction");
					printTxStatus();
					ut.commit();
				}
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Exception during cleanup of transaction", e);
			}
			try {
				if (queueTest) {
					logger.log(Logger.Level.TRACE, "Cleanup of Queue and close receiver/sender");
					flushQueue(qSession, qReceiver, bmtQ);
					logger.log(Logger.Level.TRACE, "Closing receiver and sender");
					if (qReceiver != null)
						qReceiver.close();
					if (qSender != null)
						qSender.close();
					qReceiver = null;
					qSender = null;
				} else {
					logger.log(Logger.Level.TRACE, "Cleanup of Topic and close publisher");
					if (tPublisher != null)
						tPublisher.close();
					tPublisher = null;
				}
			} catch (Exception e) {
				if (queueTest)
					logger.log(Logger.Level.ERROR, "Exception during cleanup of Queue", e);
				else
					logger.log(Logger.Level.ERROR, "Exception during cleanup of Topic", e);
			}
			try {
				logger.log(Logger.Level.TRACE, "Closing sessions");
				if (tSession != null)
					tSession.close();
				if (qSession != null)
					qSession.close();
				tSession = null;
				qSession = null;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Exception during closing of sessions", e);
			}
			try {
				logger.log(Logger.Level.TRACE, "Closing connections");
				if (tConnect != null)
					tConnect.close();
				if (qConnect != null)
					qConnect.close();
				tConnect = null;
				qConnect = null;
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "Exception during closing of connections", e);
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			logger.log(Logger.Level.ERROR, "An error occurred while cleaning");
			throw new Exception("Cleanup failed!", e);
		}
	}

	/*
	 * Cleanup method for tests that use durable subscriptions
	 */
	private void cleanupSubscription(TopicSubscriber sub, TopicSession session, String subName) {
		if (sub != null) {
			try {
				logger.log(Logger.Level.TRACE, "Closing durable subscriber: " + sub);
				sub.close();
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "exception during close: ", e);
			}
		}

		if (session != null) {
			try {
				logger.log(Logger.Level.TRACE, "Unsubscribing \"" + subName + "\"");
				session.unsubscribe(subName);
			} catch (Exception e) {
				logger.log(Logger.Level.ERROR, "exception during unsubscribe: ", e);
			}
		}
	}

	private void flushQueue(QueueSession qSession, QueueReceiver qReceiver, Queue queue) throws Exception {
		int numMsgsFlushed = 0;
		int numMsgs = 0;
		Enumeration msgs = null;

		try {
			logger.log(Logger.Level.TRACE, "Flush Queue " + queue.getQueueName());
			QueueBrowser qBrowser = qSession.createBrowser(queue);

			// count the number of messages
			msgs = qBrowser.getEnumeration();
			while (msgs.hasMoreElements()) {
				msgs.nextElement();
				numMsgs++;
			}
			qBrowser.close();

			if (numMsgs == 0) {
				logger.log(Logger.Level.TRACE, "No Messages left on Queue " + queue.getQueueName());
			} else {
				logger.log(Logger.Level.TRACE, numMsgs + " Messages left on Queue " + queue.getQueueName());

				// flush the queue
				Message msg = qReceiver.receiveNoWait();
				while (msg != null) {
					numMsgsFlushed++;
					msg = qReceiver.receiveNoWait();
				}
				if (numMsgsFlushed > 0) {
					logger.log(Logger.Level.INFO, "flush queue of " + numMsgsFlushed + " messages");
				}
			}
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
		}
	}

	private void printTxStatus() {
		logger.log(Logger.Level.TRACE, "Transaction Status: ");
		int status;

		try {
			status = ut.getStatus();
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			logger.log(Logger.Level.TRACE, "Unable to get transaction status");
			return;
		}

		switch (status) {
		case jakarta.transaction.Status.STATUS_ACTIVE:
			logger.log(Logger.Level.TRACE,
					"==== A transaction is associated with" + " the target object and it is in the active state.");
			break;

		case jakarta.transaction.Status.STATUS_COMMITTED:
			logger.log(Logger.Level.TRACE,
					"==== A transaction is associated with the target object" + " and it has been committed..");
			break;
		case jakarta.transaction.Status.STATUS_COMMITTING:
			logger.log(Logger.Level.TRACE, "==== A transaction is associated with the target object"
					+ " and it is in the process of committing.");
			break;
		case jakarta.transaction.Status.STATUS_MARKED_ROLLBACK:
			logger.log(Logger.Level.TRACE, "==== A transaction is associated with the target object"
					+ " and it has been marked forrollback, perhaps as a result of a setRollbackOnly operation. ");
			break;
		case jakarta.transaction.Status.STATUS_NO_TRANSACTION:
			logger.log(Logger.Level.TRACE, "==== No transaction is currently associated with the target object.");
			break;
		case jakarta.transaction.Status.STATUS_PREPARED:
			logger.log(Logger.Level.TRACE,
					"====A transaction is associated with the target object" + " and it has been prepared, i.e. ");
			break;
		case jakarta.transaction.Status.STATUS_PREPARING:
			logger.log(Logger.Level.TRACE, "==== A transaction is associated with the target object"
					+ " and it is in the process of preparing. ");
			break;

		case jakarta.transaction.Status.STATUS_ROLLEDBACK:
			logger.log(Logger.Level.TRACE, "==== A transaction is associated with the target object"
					+ " and the outcome has been determined as rollback. ");
			break;
		case jakarta.transaction.Status.STATUS_ROLLING_BACK:
			logger.log(Logger.Level.TRACE, "==== A transaction is associated with the target object"
					+ " and it is in the process of rolling back.");
			break;

		case jakarta.transaction.Status.STATUS_UNKNOWN:
			logger.log(Logger.Level.TRACE, "==== A transaction is associated with the target object"
					+ " but its current status cannot be  determined ");
			break;

		default:
			logger.log(Logger.Level.TRACE, "??? jakarta.transaction.Status is  " + status);
			break;
		}
	}

	private void createTestMessage(String TestCase, int num) throws Exception {
		logger.log(Logger.Level.TRACE, "Invoked createTestMessage");

		String myMessage = "EjbWebTransaction tests";
		try {
			if (queueTest)
				msg = qSession.createTextMessage();
			else
				msg = tSession.createTextMessage();
			msg.setText(myMessage);
			msg.setIntProperty("TestCaseNum", num);
			msg.setStringProperty("COM_SUN_JMS_TESTNAME", TestCase);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("Error in createTestMessage!");
		}
	}

	private void testInitForQ(String TestCase, int TestNum) throws Exception {
		try {
			logger.log(Logger.Level.INFO, "TestCase = " + TestCase);
			queueTest = true;

			// create QueueConnection, start user transaction, create QueueSession
			logger.log(Logger.Level.INFO, "Create QueueConnection, start user transaction, create QueueSession");
			qConnect = qFactory.createQueueConnection(jmsUser, jmsPassword);
			qConnect.start();
			ut.begin();
			qSession = qConnect.createQueueSession(transacted, 0);

			// Create a receiver and a sender
			logger.log(Logger.Level.INFO, "Create receiver and sender");
			qReceiver = qSession.createReceiver(bmtQ);
			qSender = qSession.createSender(bmtQ);

			// construct a message for this test case
			logger.log(Logger.Level.INFO, "Construct a test message for this test case");
			createTestMessage(TestCase, TestNum);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("Error in testInitForQ!");
		}
	}

	private void testInitForT(String TestCase, int TestNum) throws Exception {
		try {
			logger.log(Logger.Level.INFO, "TestCase = " + TestCase);
			queueTest = false;

			// create TopicConnection, start user transaction, create TopicSession
			logger.log(Logger.Level.INFO, "Create TopicConnection, start user transaction, create TopicSession");
			tConnect = tFactory.createTopicConnection(jmsUser, jmsPassword);
			tConnect.start();
			ut.begin();
			tSession = tConnect.createTopicSession(transacted, 0);

			// Create subscriber and publisher with TestCase as subscription name
			logger.log(Logger.Level.INFO, "Create subscriber and publisher with TestCase as subscription name");
			tSub = tSession.createDurableSubscriber(bmtT, TestCase);
			tPublisher = tSession.createPublisher(bmtT);

			// construct a message for this test case
			logger.log(Logger.Level.INFO, "Construct a test message for this test case");
			createTestMessage(TestCase, TestNum);
		} catch (Exception e) {
			TestUtil.printStackTrace(e);
			throw new Exception("Error in testInitForT!");
		}
	}

	/* Tests */
	/*
	 * @testName: Test01
	 * 
	 * @assertion_ids: JMS:SPEC:122; JavaEE:SPEC:72; JMS:SPEC:265; JMS:SPEC:265.1;
	 *
	 * @test_Strategy: Start a transaction. Send a message to Queue QUEUE_BMT.
	 * Commit the transaction. Verify that you can receive the message.
	 *
	 */
	@Test
	public void Test01() throws Exception {
		String TestCase = "Test01";
		int TestNum = 1;
		TextMessage msgRec;
		try {
			testInitForQ(TestCase, TestNum);

			// send the message
			logger.log(Logger.Level.INFO, "Begin TX: sending test message to Queue then: Commit TX");
			qSender.send(msg);
			// commit the transaction
			ut.commit();

			ut.begin();
			logger.log(Logger.Level.INFO, "Begin TX: receiving test message from Queue then: Commit TX");
			msgRec = (TextMessage) qReceiver.receive(timeout);
			ut.commit();

			logger.log(Logger.Level.INFO, "Verify message received");
			if (msgRec == null) {
				logger.log(Logger.Level.INFO, "Test " + TestNum + " Fail!");
				throw new Exception("Message was not received");
			} else if (msgRec.getIntProperty("TestCaseNum") == TestNum) {
				logger.log(Logger.Level.INFO, "Test " + TestNum + " Pass!");
			} else {
				logger.log(Logger.Level.INFO, "Unknown Message was received in error");
				logger.log(Logger.Level.INFO, "msgRec.getIntProperty(\"TestCaseNum\")="
						+ msgRec.getIntProperty("TestCaseNum") + " expected " + TestNum);
				throw new Exception("Unknown Message was received in error");
			}
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: Test02
	 * 
	 * @assertion_ids: JMS:SPEC:123; JavaEE:SPEC:72; JMS:SPEC:265; JMS:SPEC:265.1;
	 * 
	 * @test_Strategy: Start a transaction. Send a message to Queue QUEUE_BMT.
	 * Rollback the transaction. Verify that you cannot receive the message.
	 *
	 */
	@Test
	public void Test02() throws Exception {
		String TestCase = "Test02";
		int TestNum = 2;
		TextMessage msgRec;
		try {
			testInitForQ(TestCase, TestNum);

			logger.log(Logger.Level.INFO, "Begin TX: sending test message to Queue then: Rollback TX");
			qSender.send(msg);
			ut.rollback();

			ut.begin();
			logger.log(Logger.Level.INFO, "Begin TX: try receiving test message from Queue then: Commit TX");
			msgRec = (TextMessage) qReceiver.receive(timeout);
			ut.commit();

			logger.log(Logger.Level.INFO, "Verify no message received");
			if (msgRec == null) {
				logger.log(Logger.Level.INFO, "Test " + TestNum + " Pass!");
			} else if (msgRec.getIntProperty("TestCaseNum") == TestNum) {
				logger.log(Logger.Level.INFO, "Test " + TestNum + " Fail!");
				throw new Exception("Message was received in error");
			} else {
				logger.log(Logger.Level.INFO, "Unknown Message was received in error");
				logger.log(Logger.Level.INFO, "msgRec.getIntProperty(\"TestCaseNum\")="
						+ msgRec.getIntProperty("TestCaseNum") + " expected " + TestNum);
				throw new Exception("Unknown Message was received in error");
			}
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: Test03
	 * 
	 * @assertion_ids: JMS:SPEC:122; JavaEE:SPEC:72; JMS:SPEC:265; JMS:SPEC:265.1;
	 * 
	 * @test_Strategy: Send a message to Queue QUEUE_BMT. Start a transaction.
	 * Receive the message for the queue Commit the transaction. Verify that you
	 * cannot receive the message again.
	 *
	 */
	@Test
	public void Test03() throws Exception {
		String TestCase = "Test03";
		int TestNum = 3;
		TextMessage msgRec;
		try {
			testInitForQ(TestCase, TestNum);

			logger.log(Logger.Level.INFO, "Begin TX: sending test message to Queue then: Commit TX");
			qSender.send(msg);
			ut.commit();

			ut.begin();
			logger.log(Logger.Level.INFO, "Begin TX: receiving test message from Queue then: Commit TX");
			msgRec = (TextMessage) qReceiver.receive(timeout);
			ut.commit();
			if (msgRec == null) {
				throw new Exception("Unable to complete test! Did not receive a message");
			}

			ut.begin();
			logger.log(Logger.Level.INFO, "Begin TX: receiving test message again from Queue then: Commit TX");
			msgRec = (TextMessage) qReceiver.receive(timeout);
			ut.commit();

			logger.log(Logger.Level.INFO, "Verify no message received");
			if (msgRec == null) {
				logger.log(Logger.Level.INFO, "Test " + TestNum + " Pass!");
			} else if (msgRec.getIntProperty("TestCaseNum") == TestNum) {
				logger.log(Logger.Level.INFO, "Test " + TestNum + " Fail!");
				throw new Exception("Message was received in error");
			} else {
				logger.log(Logger.Level.INFO, "Unknown Message was received in error");
				logger.log(Logger.Level.INFO, "msgRec.getIntProperty(\"TestCaseNum\")="
						+ msgRec.getIntProperty("TestCaseNum") + " expected " + TestNum);
				throw new Exception("Unknown Message was received in error");
			}
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: Test04
	 * 
	 * @assertion_ids: JMS:SPEC:123; JavaEE:SPEC:72; JMS:SPEC:265; JMS:SPEC:265.1;
	 * 
	 * @test_Strategy: Send a message to Queue QUEUE_BMT. Start a transaction.
	 * Receive the message for the queue Rollback the transaction. Verify that you
	 * can receive the message again.
	 *
	 */
	@Test
	public void Test04() throws Exception {
		String TestCase = "Test04";
		int TestNum = 4;
		TextMessage msgRec;
		try {
			testInitForQ(TestCase, TestNum);

			logger.log(Logger.Level.INFO, "Begin TX: sending test message to Queue then: Commit TX");
			qSender.send(msg);
			ut.commit();

			// start a transaction
			ut.begin();
			logger.log(Logger.Level.INFO, "Begin TX: receiving test message from Queue then: Rollback TX");
			msgRec = (TextMessage) qReceiver.receive(timeout);
			ut.rollback();

			ut.begin();
			logger.log(Logger.Level.INFO, "Begin TX: receiving test message again from Queue then: Commit TX");
			msgRec = (TextMessage) qReceiver.receive(timeout);
			ut.commit();

			logger.log(Logger.Level.INFO, "Verify message received");
			if (msgRec == null) {
				logger.log(Logger.Level.INFO, "Test " + TestNum + " Fail!");
				throw new Exception("Message was not received ");
			} else if (msgRec.getIntProperty("TestCaseNum") == TestNum) {
				logger.log(Logger.Level.INFO, "Test " + TestNum + " Pass!");
			} else {
				logger.log(Logger.Level.INFO, "Unknown Message was received in error");
				logger.log(Logger.Level.INFO, "msgRec.getIntProperty(\"TestCaseNum\")="
						+ msgRec.getIntProperty("TestCaseNum") + " expected " + TestNum);
				throw new Exception("Unknown Message was received in error");
			}
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: Test05
	 * 
	 * @assertion_ids: JMS:SPEC:122; JavaEE:SPEC:72; JMS:SPEC:265; JMS:SPEC:265.1;
	 * 
	 * @test_Strategy: Start a transaction. Send a message to Topic TOPIC_BMT.
	 * Commit the transaction. Verify that you can receive the message.
	 *
	 */
	@Test
	public void Test05() throws Exception {
		String TestCase = "Test05";
		int TestNum = 5;
		TextMessage msgRec;
		try {
			testInitForT(TestCase, TestNum);

			// send the message
			logger.log(Logger.Level.INFO, "Begin TX: sending test message to Topic then: Commit TX");
			tPublisher.publish(msg);
			ut.commit();

			ut.begin();
			logger.log(Logger.Level.INFO, "Begin TX: receiving test message from Topic then: Commit TX");
			msgRec = (TextMessage) tSub.receive(timeout);
			ut.commit();

			logger.log(Logger.Level.INFO, "Verify message received");
			if (msgRec == null) {
				logger.log(Logger.Level.INFO, "Test " + TestNum + " Fail!");
				throw new Exception("Message was not received");
			} else if (msgRec.getIntProperty("TestCaseNum") == TestNum) {
				logger.log(Logger.Level.INFO, "Test " + TestNum + " Pass!");
			} else {
				logger.log(Logger.Level.INFO, "Unknown Message was received in error");
				logger.log(Logger.Level.INFO, "msgRec.getIntProperty(\"TestCaseNum\")="
						+ msgRec.getIntProperty("TestCaseNum") + " expected " + TestNum);
				throw new Exception("Unknown Message was received in error");
			}
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		} finally {
			cleanupSubscription(tSub, tSession, TestCase);
		}
	}

	/*
	 * @testName: Test06
	 * 
	 * @assertion_ids: JMS:SPEC:123; JavaEE:SPEC:72; JMS:SPEC:265; JMS:SPEC:265.1;
	 * 
	 * @test_Strategy: Start a transaction. Send a message to Topic TOPIC_BMT.
	 * Rollback the transaction. Verify that you cannot receive the message.
	 *
	 */
	@Test
	public void Test06() throws Exception {
		String TestCase = "Test06";
		int TestNum = 6;
		TextMessage msgRec;
		try {
			testInitForT(TestCase, TestNum);

			logger.log(Logger.Level.INFO, "Begin TX: sending test message to Topic then: Rollback TX");
			// send the message
			tPublisher.publish(msg);
			// rollback the transaction
			ut.rollback();

			ut.begin();
			logger.log(Logger.Level.INFO, "Begin TX: receiving test message from Topic then: Commit TX");
			msgRec = (TextMessage) tSub.receive(timeout);
			ut.commit();

			logger.log(Logger.Level.INFO, "Verify no message received");
			if (msgRec == null) {
				logger.log(Logger.Level.INFO, "Test " + TestNum + " Pass!");
			} else if (msgRec.getIntProperty("TestCaseNum") == TestNum) {
				logger.log(Logger.Level.INFO, "Test " + TestNum + " Fail!");
				throw new Exception("Message was received in error");
			} else {
				logger.log(Logger.Level.INFO, "Unknown Message was received in error");
				logger.log(Logger.Level.INFO, "msgRec.getIntProperty(\"TestCaseNum\")="
						+ msgRec.getIntProperty("TestCaseNum") + " expected " + TestNum);
				throw new Exception("Unknown Message was received in error");
			}
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		} finally {
			cleanupSubscription(tSub, tSession, TestCase);
		}
	}

	/*
	 * @testName: Test07
	 * 
	 * @assertion_ids: JMS:SPEC:122; JavaEE:SPEC:72; JMS:SPEC:265; JMS:SPEC:265.1;
	 * 
	 * @test_Strategy: Send a message to Topic TOPIC_BMT. Start a transaction.
	 * Receive the message for the topic Commit the transaction. Verify that you
	 * cannot receive the message again.
	 *
	 */
	@Test
	public void Test07() throws Exception {
		String TestCase = "Test07";
		int TestNum = 7;
		TextMessage msgRec;
		try {
			testInitForT(TestCase, TestNum);

			logger.log(Logger.Level.INFO, "Begin TX: sending test message to Topic then: Commit TX");
			// send the message
			tPublisher.publish(msg);
			ut.commit();

			ut.begin();
			logger.log(Logger.Level.INFO, "Begin TX: receiving test message from Topic then: Commit TX");
			msgRec = (TextMessage) tSub.receive(timeout);
			ut.commit();
			if (msgRec == null) {
				throw new Exception("Unable to complete test! Did not receive a message");
			}

			ut.begin();
			logger.log(Logger.Level.INFO, "Begin TX: receiving test message again from Topic then: Commit TX");
			msgRec = (TextMessage) tSub.receive(timeout);
			ut.commit();

			logger.log(Logger.Level.INFO, "Verify no message received");
			if (msgRec == null) {
				logger.log(Logger.Level.INFO, "Test " + TestNum + " Pass!");
			} else if (msgRec.getIntProperty("TestCaseNum") == TestNum) {
				logger.log(Logger.Level.INFO, "Test " + TestNum + " Fail!");
				throw new Exception("Message was received in error");
			} else {
				logger.log(Logger.Level.INFO, "Unknown Message was received in error");
				logger.log(Logger.Level.INFO, "msgRec.getIntProperty(\"TestCaseNum\")="
						+ msgRec.getIntProperty("TestCaseNum") + " expected " + TestNum);
				throw new Exception("Unknown Message was received in error");
			}
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		} finally {
			cleanupSubscription(tSub, tSession, TestCase);
		}
	}

	/*
	 * @testName: Test08
	 * 
	 * @assertion_ids: JMS:SPEC:123; JavaEE:SPEC:72; JMS:SPEC:265; JMS:SPEC:265.1;
	 * 
	 * @test_Strategy: Send a message to Topic TOPIC_BMT. Start a transaction.
	 * Receive the message for the topic Rollback the transaction. Verify that you
	 * can receive the message again.
	 *
	 */
	@Test
	public void Test08() throws Exception {
		String TestCase = "Test08";
		int TestNum = 8;
		TextMessage msgRec;
		try {
			testInitForT(TestCase, TestNum);

			logger.log(Logger.Level.INFO, "Begin TX: sending test message to Topic then: Commit TX");
			// send the message
			tPublisher.publish(msg);
			ut.commit();

			// start a transaction, receive a message, then rollback
			ut.begin();
			logger.log(Logger.Level.INFO, "Begin TX: receiving test message from Topic then: Rollback TX");
			msgRec = (TextMessage) tSub.receive(timeout);
			ut.rollback();
			if (msgRec == null) {
				throw new Exception("Unable to complete test! Did not receive a message");
			}

			// start transaction, then try to receive again - should get a message.
			ut.begin();
			logger.log(Logger.Level.INFO, "Begin TX: receiving test message again from Topic then: Commit TX");
			msgRec = (TextMessage) tSub.receive(timeout);
			ut.commit();

			logger.log(Logger.Level.INFO, "Verify message received");
			if (msgRec == null) {
				logger.log(Logger.Level.INFO, "Test " + TestNum + " Failed!");
				throw new Exception("Message was not received ");
			} else if (msgRec.getIntProperty("TestCaseNum") == TestNum) {
				logger.log(Logger.Level.INFO, "Test " + TestNum + " Pass!");
			} else {
				logger.log(Logger.Level.INFO, "Unknown Test Message " + TestNum + " Fail!");
				logger.log(Logger.Level.INFO, "msgRec.getIntProperty(\"TestCaseNum\")="
						+ msgRec.getIntProperty("TestCaseNum") + " expected " + TestNum);
				throw new Exception("Unknown Message was received in error");
			}
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		} finally {
			cleanupSubscription(tSub, tSession, TestCase);
		}
	}

	/*
	 * @testName: Test09
	 * 
	 * @assertion_ids: JMS:SPEC:122; JavaEE:SPEC:72; JMS:SPEC:265; JMS:SPEC:265.1;
	 *
	 * @test_Strategy: Start a transaction. Send message to Queue QUEUE_BMT. Commit
	 * the transaction. Start another transaction. Send message again to Queue
	 * QUEUE_BMT. Commit the transaction. Start a transaction. Receive message from
	 * Queue QUEUE_BMT. Commit the transaction. Start a transaction. Receive message
	 * from Queue QUEUE_BMT. Commit the transaction. Verify that you can receive the
	 * message twice.
	 */
	@Test
	public void Test09() throws Exception {
		String TestCase = "Test09";
		int TestNum = 9;
		TextMessage msgRec;
		try {
			testInitForQ(TestCase, TestNum);

			// send the message
			logger.log(Logger.Level.INFO, "Begin TX: sending test message to Queue then: Commit TX");
			qSender.send(msg);
			// commit the transaction
			ut.commit();

			// send the message again
			ut.begin();
			logger.log(Logger.Level.INFO, "Begin TX: sending test message again to Queue then: Commit TX");
			qSender.send(msg);
			// commit the transaction
			ut.commit();

			for (int i = 0; i < 2; i++) {
				ut.begin();
				logger.log(Logger.Level.INFO, "Begin TX: receiving test message from Queue then: Commit TX");
				msgRec = (TextMessage) qReceiver.receive(timeout);
				ut.commit();

				logger.log(Logger.Level.INFO, "Verify message received");
				if (msgRec == null) {
					logger.log(Logger.Level.INFO, "Test " + TestNum + " Fail!");
					throw new Exception("Message was not received");
				} else if (msgRec.getIntProperty("TestCaseNum") == TestNum) {
					logger.log(Logger.Level.INFO, "Test " + TestNum + " Pass!");
				} else {
					logger.log(Logger.Level.INFO, "Unknown Message was received in error");
					logger.log(Logger.Level.INFO, "msgRec.getIntProperty(\"TestCaseNum\")="
							+ msgRec.getIntProperty("TestCaseNum") + " expected " + TestNum);
					throw new Exception("Unknown Message was received in error");
				}
			}
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		}
	}

	/*
	 * @testName: Test10
	 * 
	 * @assertion_ids: JMS:SPEC:122; JavaEE:SPEC:72; JMS:SPEC:265; JMS:SPEC:265.1;
	 *
	 * @test_Strategy: Start a transaction. Send message to Topic TOPIC_BMT. Commit
	 * the transaction. Start another transaction. Send message again to Topic
	 * TOPIC_BMT. Commit the transaction. Start a transaction. Receive message from
	 * Topic TOPIC_BMT. Commit the transaction. Start a transaction. Receive message
	 * from Topic TOPIC_BMT. Commit the transaction. Verify that you can receive the
	 * message twice.
	 */
	@Test
	public void Test10() throws Exception {
		String TestCase = "Test10";
		int TestNum = 10;
		TextMessage msgRec;
		try {
			testInitForT(TestCase, TestNum);

			logger.log(Logger.Level.INFO, "Begin TX: sending test message to Topic then: Commit TX");
			// send the message
			tPublisher.publish(msg);
			// commit the transaction
			ut.commit();

			// send the message again
			ut.begin();
			logger.log(Logger.Level.INFO, "Begin TX: sending test message again to Topic then: Commit TX");
			tPublisher.publish(msg);
			// commit the transaction
			ut.commit();

			for (int i = 0; i < 2; i++) {
				ut.begin();
				logger.log(Logger.Level.INFO, "Begin TX: receiving test message from Queue then: Commit TX");
				msgRec = (TextMessage) tSub.receive(timeout);
				ut.commit();

				logger.log(Logger.Level.INFO, "Verify message received");
				if (msgRec == null) {
					logger.log(Logger.Level.INFO, "Test " + TestNum + " Fail!");
					throw new Exception("Message was not received");
				} else if (msgRec.getIntProperty("TestCaseNum") == TestNum) {
					logger.log(Logger.Level.INFO, "Test " + TestNum + " Pass!");
				} else {
					logger.log(Logger.Level.INFO, "Unknown Message was received in error");
					logger.log(Logger.Level.INFO, "msgRec.getIntProperty(\"TestCaseNum\")="
							+ msgRec.getIntProperty("TestCaseNum") + " expected " + TestNum);
					throw new Exception("Unknown Message was received in error");
				}
			}
		} catch (Exception e) {
			throw new Exception("Test Failed!", e);
		} finally {
			cleanupSubscription(tSub, tSession, TestCase);
		}
	}
}
