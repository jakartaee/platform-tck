/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.rmi.RemoteException;

import java.io.*;
import java.util.Properties;
import com.sun.javatest.Status;
import javax.jms.*;
import javax.transaction.*;
import java.util.ArrayList;
import java.util.Enumeration;

public class TransactionTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.ee.ejbweb.xa.TransactionTests";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

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

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    TransactionTests theTests = new TransactionTests();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

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
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      jmsUser = p.getProperty("user");
      jmsPassword = p.getProperty("password");
      timeout = Long.parseLong(p.getProperty("jms_timeout"));

      // check props for errors
      if (timeout < 1) {
        throw new Fault("'jms_timeout' (milliseconds) in ts.jte must be > 0");
      }
      if (jmsUser == null) {
        throw new Fault("'user' in ts.jte must not be null ");
      }
      if (jmsPassword == null) {
        throw new Fault("'password' in ts.jte must not be null ");
      }

      context = new TSNamingContext();
      qFactory = (QueueConnectionFactory) context
          .lookup("java:comp/env/jms/MyQueueConnectionFactory");
      bmtQ = (Queue) context.lookup("java:comp/env/jms/QUEUE_BMT");

      tFactory = (TopicConnectionFactory) context
          .lookup("java:comp/env/jms/MyTopicConnectionFactory");
      bmtT = (Topic) context.lookup("java:comp/env/jms/TOPIC_BMT");

      ut = (UserTransaction) context.lookup("java:comp/UserTransaction");
      TestUtil.logTrace("in client setup");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed!", e);
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
  public void cleanup() throws Fault {
    try {
      logTrace("Cleanup");
      try {
        if (ut != null && ut
            .getStatus() != javax.transaction.Status.STATUS_NO_TRANSACTION) {
          logTrace("Cleanup open transaction");
          printTxStatus();
          ut.commit();
        }
      } catch (Exception e) {
        logErr("Exception during cleanup of transaction", e);
      }
      try {
        if (queueTest) {
          logTrace("Cleanup of Queue and close receiver/sender");
          flushQueue(qSession, qReceiver, bmtQ);
          logTrace("Closing receiver and sender");
          if (qReceiver != null)
            qReceiver.close();
          if (qSender != null)
            qSender.close();
          qReceiver = null;
          qSender = null;
        } else {
          logTrace("Cleanup of Topic and close publisher");
          if (tPublisher != null)
            tPublisher.close();
          tPublisher = null;
        }
      } catch (Exception e) {
        if (queueTest)
          logErr("Exception during cleanup of Queue", e);
        else
          logErr("Exception during cleanup of Topic", e);
      }
      try {
        logTrace("Closing sessions");
        if (tSession != null)
          tSession.close();
        if (qSession != null)
          qSession.close();
        tSession = null;
        qSession = null;
      } catch (Exception e) {
        logErr("Exception during closing of sessions", e);
      }
      try {
        logTrace("Closing connections");
        if (tConnect != null)
          tConnect.close();
        if (qConnect != null)
          qConnect.close();
        tConnect = null;
        qConnect = null;
      } catch (Exception e) {
        logErr("Exception during closing of connections", e);
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      logErr("An error occurred while cleaning");
      throw new Fault("Cleanup failed!", e);
    }
  }

  /*
   * Cleanup method for tests that use durable subscriptions
   */
  private void cleanupSubscription(TopicSubscriber sub, TopicSession session,
      String subName) {
    if (sub != null) {
      try {
        TestUtil.logTrace("Closing durable subscriber: " + sub);
        sub.close();
      } catch (Exception e) {
        TestUtil.logErr("exception during close: ", e);
      }
    }

    if (session != null) {
      try {
        TestUtil.logTrace("Unsubscribing \"" + subName + "\"");
        session.unsubscribe(subName);
      } catch (Exception e) {
        TestUtil.logErr("exception during unsubscribe: ", e);
      }
    }
  }

  private void flushQueue(QueueSession qSession, QueueReceiver qReceiver,
      Queue queue) throws Exception {
    int numMsgsFlushed = 0;
    int numMsgs = 0;
    Enumeration msgs = null;

    try {
      TestUtil.logTrace("Flush Queue " + queue.getQueueName());
      QueueBrowser qBrowser = qSession.createBrowser(queue);

      // count the number of messages
      msgs = qBrowser.getEnumeration();
      while (msgs.hasMoreElements()) {
        msgs.nextElement();
        numMsgs++;
      }
      qBrowser.close();

      if (numMsgs == 0) {
        TestUtil.logTrace("No Messages left on Queue " + queue.getQueueName());
      } else {
        TestUtil.logTrace(
            numMsgs + " Messages left on Queue " + queue.getQueueName());

        // flush the queue
        Message msg = qReceiver.receiveNoWait();
        while (msg != null) {
          numMsgsFlushed++;
          msg = qReceiver.receiveNoWait();
        }
        if (numMsgsFlushed > 0) {
          TestUtil.logMsg("flush queue of " + numMsgsFlushed + " messages");
        }
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  private void printTxStatus() {
    logTrace("Transaction Status: ");
    int status;

    try {
      status = ut.getStatus();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      logTrace("Unable to get transaction status");
      return;
    }

    switch (status) {
    case javax.transaction.Status.STATUS_ACTIVE:
      logTrace("==== A transaction is associated with"
          + " the target object and it is in the active state.");
      break;

    case javax.transaction.Status.STATUS_COMMITTED:
      logTrace("==== A transaction is associated with the target object"
          + " and it has been committed..");
      break;
    case javax.transaction.Status.STATUS_COMMITTING:
      logTrace("==== A transaction is associated with the target object"
          + " and it is in the process of committing.");
      break;
    case javax.transaction.Status.STATUS_MARKED_ROLLBACK:
      logTrace("==== A transaction is associated with the target object"
          + " and it has been marked forrollback, perhaps as a result of a setRollbackOnly operation. ");
      break;
    case javax.transaction.Status.STATUS_NO_TRANSACTION:
      logTrace(
          "==== No transaction is currently associated with the target object.");
      break;
    case javax.transaction.Status.STATUS_PREPARED:
      logTrace("====A transaction is associated with the target object"
          + " and it has been prepared, i.e. ");
      break;
    case javax.transaction.Status.STATUS_PREPARING:
      logTrace("==== A transaction is associated with the target object"
          + " and it is in the process of preparing. ");
      break;

    case javax.transaction.Status.STATUS_ROLLEDBACK:
      logTrace("==== A transaction is associated with the target object"
          + " and the outcome has been determined as rollback. ");
      break;
    case javax.transaction.Status.STATUS_ROLLING_BACK:
      logTrace("==== A transaction is associated with the target object"
          + " and it is in the process of rolling back.");
      break;

    case javax.transaction.Status.STATUS_UNKNOWN:
      logTrace("==== A transaction is associated with the target object"
          + " but its current status cannot be  determined ");
      break;

    default:
      TestUtil.logTrace("??? javax.transaction.Status is  " + status);
      break;
    }
  }

  private void createTestMessage(String TestCase, int num) throws Exception {
    logTrace("Invoked createTestMessage");

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
      logMsg("TestCase = " + TestCase);
      queueTest = true;

      // create QueueConnection, start user transaction, create QueueSession
      logMsg(
          "Create QueueConnection, start user transaction, create QueueSession");
      qConnect = qFactory.createQueueConnection(jmsUser, jmsPassword);
      qConnect.start();
      ut.begin();
      qSession = qConnect.createQueueSession(transacted, 0);

      // Create a receiver and a sender
      logMsg("Create receiver and sender");
      qReceiver = qSession.createReceiver(bmtQ);
      qSender = qSession.createSender(bmtQ);

      // construct a message for this test case
      logMsg("Construct a test message for this test case");
      createTestMessage(TestCase, TestNum);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Exception("Error in testInitForQ!");
    }
  }

  private void testInitForT(String TestCase, int TestNum) throws Exception {
    try {
      logMsg("TestCase = " + TestCase);
      queueTest = false;

      // create TopicConnection, start user transaction, create TopicSession
      logMsg(
          "Create TopicConnection, start user transaction, create TopicSession");
      tConnect = tFactory.createTopicConnection(jmsUser, jmsPassword);
      tConnect.start();
      ut.begin();
      tSession = tConnect.createTopicSession(transacted, 0);

      // Create subscriber and publisher with TestCase as subscription name
      logMsg(
          "Create subscriber and publisher with TestCase as subscription name");
      tSub = tSession.createDurableSubscriber(bmtT, TestCase);
      tPublisher = tSession.createPublisher(bmtT);

      // construct a message for this test case
      logMsg("Construct a test message for this test case");
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
  public void Test01() throws Fault {
    String TestCase = "Test01";
    int TestNum = 1;
    TextMessage msgRec;
    try {
      testInitForQ(TestCase, TestNum);

      // send the message
      logMsg("Begin TX: sending test message to Queue then: Commit TX");
      qSender.send(msg);
      // commit the transaction
      ut.commit();

      ut.begin();
      logMsg("Begin TX: receiving test message from Queue then: Commit TX");
      msgRec = (TextMessage) qReceiver.receive(timeout);
      ut.commit();

      logMsg("Verify message received");
      if (msgRec == null) {
        logMsg("Test " + TestNum + " Fail!");
        throw new Exception("Message was not received");
      } else if (msgRec.getIntProperty("TestCaseNum") == TestNum) {
        logMsg("Test " + TestNum + " Pass!");
      } else {
        logMsg("Unknown Message was received in error");
        logMsg("msgRec.getIntProperty(\"TestCaseNum\")="
            + msgRec.getIntProperty("TestCaseNum") + " expected " + TestNum);
        throw new Exception("Unknown Message was received in error");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
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
  public void Test02() throws Fault {
    String TestCase = "Test02";
    int TestNum = 2;
    TextMessage msgRec;
    try {
      testInitForQ(TestCase, TestNum);

      logMsg("Begin TX: sending test message to Queue then: Rollback TX");
      qSender.send(msg);
      ut.rollback();

      ut.begin();
      logMsg("Begin TX: try receiving test message from Queue then: Commit TX");
      msgRec = (TextMessage) qReceiver.receive(timeout);
      ut.commit();

      logMsg("Verify no message received");
      if (msgRec == null) {
        logMsg("Test " + TestNum + " Pass!");
      } else if (msgRec.getIntProperty("TestCaseNum") == TestNum) {
        logMsg("Test " + TestNum + " Fail!");
        throw new Exception("Message was received in error");
      } else {
        logMsg("Unknown Message was received in error");
        logMsg("msgRec.getIntProperty(\"TestCaseNum\")="
            + msgRec.getIntProperty("TestCaseNum") + " expected " + TestNum);
        throw new Exception("Unknown Message was received in error");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
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
  public void Test03() throws Fault {
    String TestCase = "Test03";
    int TestNum = 3;
    TextMessage msgRec;
    try {
      testInitForQ(TestCase, TestNum);

      logMsg("Begin TX: sending test message to Queue then: Commit TX");
      qSender.send(msg);
      ut.commit();

      ut.begin();
      logMsg("Begin TX: receiving test message from Queue then: Commit TX");
      msgRec = (TextMessage) qReceiver.receive(timeout);
      ut.commit();
      if (msgRec == null) {
        throw new Exception(
            "Unable to complete test! Did not receive a message");
      }

      ut.begin();
      logMsg(
          "Begin TX: receiving test message again from Queue then: Commit TX");
      msgRec = (TextMessage) qReceiver.receive(timeout);
      ut.commit();

      logMsg("Verify no message received");
      if (msgRec == null) {
        logMsg("Test " + TestNum + " Pass!");
      } else if (msgRec.getIntProperty("TestCaseNum") == TestNum) {
        logMsg("Test " + TestNum + " Fail!");
        throw new Exception("Message was received in error");
      } else {
        logMsg("Unknown Message was received in error");
        logMsg("msgRec.getIntProperty(\"TestCaseNum\")="
            + msgRec.getIntProperty("TestCaseNum") + " expected " + TestNum);
        throw new Exception("Unknown Message was received in error");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
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
  public void Test04() throws Fault {
    String TestCase = "Test04";
    int TestNum = 4;
    TextMessage msgRec;
    try {
      testInitForQ(TestCase, TestNum);

      logMsg("Begin TX: sending test message to Queue then: Commit TX");
      qSender.send(msg);
      ut.commit();

      // start a transaction
      ut.begin();
      logMsg("Begin TX: receiving test message from Queue then: Rollback TX");
      msgRec = (TextMessage) qReceiver.receive(timeout);
      ut.rollback();

      ut.begin();
      logMsg(
          "Begin TX: receiving test message again from Queue then: Commit TX");
      msgRec = (TextMessage) qReceiver.receive(timeout);
      ut.commit();

      logMsg("Verify message received");
      if (msgRec == null) {
        logMsg("Test " + TestNum + " Fail!");
        throw new Exception("Message was not received ");
      } else if (msgRec.getIntProperty("TestCaseNum") == TestNum) {
        logMsg("Test " + TestNum + " Pass!");
      } else {
        logMsg("Unknown Message was received in error");
        logMsg("msgRec.getIntProperty(\"TestCaseNum\")="
            + msgRec.getIntProperty("TestCaseNum") + " expected " + TestNum);
        throw new Exception("Unknown Message was received in error");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
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
  public void Test05() throws Fault {
    String TestCase = "Test05";
    int TestNum = 5;
    TextMessage msgRec;
    try {
      testInitForT(TestCase, TestNum);

      // send the message
      logMsg("Begin TX: sending test message to Topic then: Commit TX");
      tPublisher.publish(msg);
      ut.commit();

      ut.begin();
      logMsg("Begin TX: receiving test message from Topic then: Commit TX");
      msgRec = (TextMessage) tSub.receive(timeout);
      ut.commit();

      logMsg("Verify message received");
      if (msgRec == null) {
        logMsg("Test " + TestNum + " Fail!");
        throw new Exception("Message was not received");
      } else if (msgRec.getIntProperty("TestCaseNum") == TestNum) {
        logMsg("Test " + TestNum + " Pass!");
      } else {
        logMsg("Unknown Message was received in error");
        logMsg("msgRec.getIntProperty(\"TestCaseNum\")="
            + msgRec.getIntProperty("TestCaseNum") + " expected " + TestNum);
        throw new Exception("Unknown Message was received in error");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
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
  public void Test06() throws Fault {
    String TestCase = "Test06";
    int TestNum = 6;
    TextMessage msgRec;
    try {
      testInitForT(TestCase, TestNum);

      logMsg("Begin TX: sending test message to Topic then: Rollback TX");
      // send the message
      tPublisher.publish(msg);
      // rollback the transaction
      ut.rollback();

      ut.begin();
      logMsg("Begin TX: receiving test message from Topic then: Commit TX");
      msgRec = (TextMessage) tSub.receive(timeout);
      ut.commit();

      logMsg("Verify no message received");
      if (msgRec == null) {
        logMsg("Test " + TestNum + " Pass!");
      } else if (msgRec.getIntProperty("TestCaseNum") == TestNum) {
        logMsg("Test " + TestNum + " Fail!");
        throw new Exception("Message was received in error");
      } else {
        logMsg("Unknown Message was received in error");
        logMsg("msgRec.getIntProperty(\"TestCaseNum\")="
            + msgRec.getIntProperty("TestCaseNum") + " expected " + TestNum);
        throw new Exception("Unknown Message was received in error");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
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
  public void Test07() throws Fault {
    String TestCase = "Test07";
    int TestNum = 7;
    TextMessage msgRec;
    try {
      testInitForT(TestCase, TestNum);

      logMsg("Begin TX: sending test message to Topic then: Commit TX");
      // send the message
      tPublisher.publish(msg);
      ut.commit();

      ut.begin();
      logMsg("Begin TX: receiving test message from Topic then: Commit TX");
      msgRec = (TextMessage) tSub.receive(timeout);
      ut.commit();
      if (msgRec == null) {
        throw new Exception(
            "Unable to complete test! Did not receive a message");
      }

      ut.begin();
      logMsg(
          "Begin TX: receiving test message again from Topic then: Commit TX");
      msgRec = (TextMessage) tSub.receive(timeout);
      ut.commit();

      logMsg("Verify no message received");
      if (msgRec == null) {
        logMsg("Test " + TestNum + " Pass!");
      } else if (msgRec.getIntProperty("TestCaseNum") == TestNum) {
        logMsg("Test " + TestNum + " Fail!");
        throw new Exception("Message was received in error");
      } else {
        logMsg("Unknown Message was received in error");
        logMsg("msgRec.getIntProperty(\"TestCaseNum\")="
            + msgRec.getIntProperty("TestCaseNum") + " expected " + TestNum);
        throw new Exception("Unknown Message was received in error");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
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
  public void Test08() throws Fault {
    String TestCase = "Test08";
    int TestNum = 8;
    TextMessage msgRec;
    try {
      testInitForT(TestCase, TestNum);

      logMsg("Begin TX: sending test message to Topic then: Commit TX");
      // send the message
      tPublisher.publish(msg);
      ut.commit();

      // start a transaction, receive a message, then rollback
      ut.begin();
      logMsg("Begin TX: receiving test message from Topic then: Rollback TX");
      msgRec = (TextMessage) tSub.receive(timeout);
      ut.rollback();
      if (msgRec == null) {
        throw new Exception(
            "Unable to complete test! Did not receive a message");
      }

      // start transaction, then try to receive again - should get a message.
      ut.begin();
      logMsg(
          "Begin TX: receiving test message again from Topic then: Commit TX");
      msgRec = (TextMessage) tSub.receive(timeout);
      ut.commit();

      logMsg("Verify message received");
      if (msgRec == null) {
        logMsg("Test " + TestNum + " Failed!");
        throw new Exception("Message was not received ");
      } else if (msgRec.getIntProperty("TestCaseNum") == TestNum) {
        logMsg("Test " + TestNum + " Pass!");
      } else {
        logMsg("Unknown Test Message " + TestNum + " Fail!");
        logMsg("msgRec.getIntProperty(\"TestCaseNum\")="
            + msgRec.getIntProperty("TestCaseNum") + " expected " + TestNum);
        throw new Exception("Unknown Message was received in error");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    } finally {
      cleanupSubscription(tSub, tSession, TestCase);
    }
  }

  /*
   * @testName: Test09
   * 
   * @assertion_ids: JMS:SPEC:122; JavaEE:SPEC:72; JMS:SPEC:265; JMS:SPEC:265.1;
   *
   * @test_Strategy: Start a transaction. Send message to Queue QUEUE_BMT.
   * Commit the transaction. Start another transaction. Send message again to
   * Queue QUEUE_BMT. Commit the transaction. Start a transaction. Receive
   * message from Queue QUEUE_BMT. Commit the transaction. Start a transaction.
   * Receive message from Queue QUEUE_BMT. Commit the transaction. Verify that
   * you can receive the message twice.
   */
  public void Test09() throws Fault {
    String TestCase = "Test09";
    int TestNum = 9;
    TextMessage msgRec;
    try {
      testInitForQ(TestCase, TestNum);

      // send the message
      logMsg("Begin TX: sending test message to Queue then: Commit TX");
      qSender.send(msg);
      // commit the transaction
      ut.commit();

      // send the message again
      ut.begin();
      logMsg("Begin TX: sending test message again to Queue then: Commit TX");
      qSender.send(msg);
      // commit the transaction
      ut.commit();

      for (int i = 0; i < 2; i++) {
        ut.begin();
        logMsg("Begin TX: receiving test message from Queue then: Commit TX");
        msgRec = (TextMessage) qReceiver.receive(timeout);
        ut.commit();

        logMsg("Verify message received");
        if (msgRec == null) {
          logMsg("Test " + TestNum + " Fail!");
          throw new Exception("Message was not received");
        } else if (msgRec.getIntProperty("TestCaseNum") == TestNum) {
          logMsg("Test " + TestNum + " Pass!");
        } else {
          logMsg("Unknown Message was received in error");
          logMsg("msgRec.getIntProperty(\"TestCaseNum\")="
              + msgRec.getIntProperty("TestCaseNum") + " expected " + TestNum);
          throw new Exception("Unknown Message was received in error");
        }
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: Test10
   * 
   * @assertion_ids: JMS:SPEC:122; JavaEE:SPEC:72; JMS:SPEC:265; JMS:SPEC:265.1;
   *
   * @test_Strategy: Start a transaction. Send message to Topic TOPIC_BMT.
   * Commit the transaction. Start another transaction. Send message again to
   * Topic TOPIC_BMT. Commit the transaction. Start a transaction. Receive
   * message from Topic TOPIC_BMT. Commit the transaction. Start a transaction.
   * Receive message from Topic TOPIC_BMT. Commit the transaction. Verify that
   * you can receive the message twice.
   */
  public void Test10() throws Fault {
    String TestCase = "Test10";
    int TestNum = 10;
    TextMessage msgRec;
    try {
      testInitForT(TestCase, TestNum);

      logMsg("Begin TX: sending test message to Topic then: Commit TX");
      // send the message
      tPublisher.publish(msg);
      // commit the transaction
      ut.commit();

      // send the message again
      ut.begin();
      logMsg("Begin TX: sending test message again to Topic then: Commit TX");
      tPublisher.publish(msg);
      // commit the transaction
      ut.commit();

      for (int i = 0; i < 2; i++) {
        ut.begin();
        logMsg("Begin TX: receiving test message from Queue then: Commit TX");
        msgRec = (TextMessage) tSub.receive(timeout);
        ut.commit();

        logMsg("Verify message received");
        if (msgRec == null) {
          logMsg("Test " + TestNum + " Fail!");
          throw new Exception("Message was not received");
        } else if (msgRec.getIntProperty("TestCaseNum") == TestNum) {
          logMsg("Test " + TestNum + " Pass!");
        } else {
          logMsg("Unknown Message was received in error");
          logMsg("msgRec.getIntProperty(\"TestCaseNum\")="
              + msgRec.getIntProperty("TestCaseNum") + " expected " + TestNum);
          throw new Exception("Unknown Message was received in error");
        }
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    } finally {
      cleanupSubscription(tSub, tSession, TestCase);
    }
  }
}
