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
package com.sun.ts.tests.jms.core.appclient.txqueuetests;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.io.Serializable;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.ArrayList;
import com.sun.javatest.Status;

public class TxQueueTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.appclient.txqueuetests.TxQueueTests";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // JMS objects
  private transient JmsTool tool = null;

  // Harness req's
  private Properties props = null;

  // properties read from ts.jte file
  long timeout;

  String user;

  String password;

  String mode;

  ArrayList queues = null;

  ArrayList connections = null;

  /* Run test in standalone mode */

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    TxQueueTests theTests = new TxQueueTests();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

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
  public void setup(String[] args, Properties p) throws Fault {
    try {

      // get props
      timeout = Long.parseLong(p.getProperty("jms_timeout"));
      user = p.getProperty("user");
      password = p.getProperty("password");
      mode = p.getProperty("platform.mode");

      // check props for errors
      if (timeout < 1) {
        throw new Exception("'timeout' (milliseconds) in ts.jte must be > 0");
      }
      if (user == null) {
        throw new Exception("'user' in ts.jte must be null");
      }
      if (password == null) {
        throw new Exception("'password' in ts.jte must be null");
      }
      if (mode == null) {
        throw new Exception("'mode' in ts.jte must be null");
      }
      queues = new ArrayList(2);
      connections = new ArrayList(2);

      // get ready for new test
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
      if (tool != null) {
        logMsg("Cleanup: Closing Queue and Topic Connections");
        tool.doClientQueueTestCleanup(connections, queues);
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      logErr("An error occurred while cleaning");
      throw new Fault("Cleanup failed!", e);
    }
  }

  /* Tests */

  /*
   * @testName: simpleSendReceiveTxQueueTest
   * 
   * @assertion_ids: JMS:SPEC:122; JMS:SPEC:123; JMS:SPEC:124; JMS:SPEC:125;
   * 
   * @test_Strategy: Send and receive single message to verify that Queues are
   * working in a transacted session.
   */
  public void simpleSendReceiveTxQueueTest() throws Fault {
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.TX_QUEUE, user, password, mode);
      logMsg("Start connection");
      tool.getDefaultQueueConnection().start();

      // send and receive message
      logMsg("Creating 1 message");
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText("transaction message test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "simpleSendReceiveTxQueueTest");
      logMsg("Sending message");
      tool.getDefaultQueueSender().send(messageSent);
      logMsg("Call commit");
      tool.getDefaultQueueSession().commit();
      logMsg("Receiving message");
      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logMsg("Call commit");
      tool.getDefaultQueueSession().commit();

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        logMsg("Message text: \"" + messageReceived.getText() + "\"");
        logMsg("Received correct message");
      } else {
        throw new Exception("didn't get the right message");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("simpleSendReceiveQueueTxTest");
    }
  }

  /*
   * @testName: commitAckMsgQueueTest
   * 
   * @assertion_ids: JMS:SPEC:130;
   * 
   * @test_Strategy: Create tx_session and receive one message from a queue.
   * Call commit() and close session. Create non_tx new session and send/receive
   * message. Should only receive the one message.
   */
  public void commitAckMsgQueueTest() throws Fault {
    try {
      TextMessage mSent = null;
      TextMessage mReceived = null;
      QueueSession qSess = null;
      QueueSender qSender = null;
      QueueReceiver qRec = null;
      String msg = "test message for commitAckMsgTest";

      // close default session and create tx AUTO_ACK session
      tool = new JmsTool(JmsTool.TX_QUEUE, user, password, mode);
      tool.getDefaultQueueReceiver().close();
      qSess = tool.getDefaultQueueSession();
      qSender = tool.getDefaultQueueSender();
      qRec = qSess.createReceiver(tool.getDefaultQueue());
      tool.getDefaultQueueConnection().start();

      // send message
      logTrace("Send first message");
      mSent = qSess.createTextMessage();
      mSent.setBooleanProperty("lastMessage", false);
      mSent.setText(msg);
      mSent.setStringProperty("COM_SUN_JMS_TESTNAME", "commitAckMsgQueueTest1");
      qSender.send(mSent);
      qSess.commit();

      logTrace("Send second message");
      mSent.setBooleanProperty("lastMessage", true);
      qSender.send(mSent);
      qSess.commit();

      logTrace(
          "Message sent. Receive with tx session, do not call acknowledge().");
      mReceived = (TextMessage) qRec.receive(timeout);
      if (mReceived == null) {
        logMsg("Did not receive message!");
        throw new Exception("Did not receive message first time");
      }
      logTrace("Received message: \"" + mReceived.getText() + "\"");

      // commit and close session
      logTrace("Call commit() without calling acknowledge().");
      qSess.commit();
      logTrace("Close session and create new one.");
      qSess.close();
      // create new (non-tx) session
      qSess = tool.getDefaultQueueConnection().createQueueSession(false,
          Session.AUTO_ACKNOWLEDGE);
      qRec = qSess.createReceiver(tool.getDefaultQueue());

      // check for messages; should receive second message
      mReceived = (TextMessage) qRec.receive(timeout);
      if (mReceived == null) {
        logMsg("Did not receive message!");
        throw new Exception("Did not receive expected message");
      } else if (mReceived.getBooleanProperty("lastMessage") == false) {
        logMsg(
            "Received orignal message again. Was not acknowledged by commit().");
        throw new Exception("Message not acknowledged by commit");
      } else if (mReceived.getBooleanProperty("lastMessage") == true) {
        logMsg("Pass: received proper message");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("commitAckMsgQueueTest");
    }
  }

  /*
   * @testName: rollbackRecoverQueueTest
   * 
   * @assertion_ids: JMS:SPEC:130;
   * 
   * @test_Strategy: Create tx_session and receive one message from a queue.
   * Call rollback() and close session. Create new session and receive message.
   */
  public void rollbackRecoverQueueTest() throws Fault {
    try {
      TextMessage mSent = null;
      TextMessage mReceived = null;
      QueueSession qSess = null;
      QueueSender qSender = null;
      QueueReceiver qRec = null;
      String msg = "test message for rollbackRecoverTest";

      // close default session and create tx AUTO_ACK session
      tool = new JmsTool(JmsTool.TX_QUEUE, user, password, mode);
      tool.getDefaultQueueSession().close();
      qSess = tool.getDefaultQueueConnection().createQueueSession(true, 0);
      qSender = qSess.createSender(tool.getDefaultQueue());
      qRec = qSess.createReceiver(tool.getDefaultQueue());
      tool.getDefaultQueueConnection().start();

      // send message
      mSent = qSess.createTextMessage();
      mSent.setText(msg);
      mSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "rollbackRecoverQueueTest");
      qSender.send(mSent);
      qSess.commit();

      // receive message
      logTrace("Message sent. Receive with tx session, do not acknowledge.");
      mReceived = (TextMessage) qRec.receive(timeout);
      if (mReceived == null) {
        logMsg("Did not receive message!");
        throw new Exception("Did not receive message first time");
      }
      logTrace("Received message: \"" + mReceived.getText() + "\"");

      // rollback and close session
      logTrace("Call rollback() without acknowledging message.");
      qSess.rollback();
      logTrace("Close session and create new one.");
      qSess.close();

      // create new (non-tx) session
      qSess = tool.getDefaultQueueConnection().createQueueSession(false,
          Session.AUTO_ACKNOWLEDGE);
      qRec = qSess.createReceiver(tool.getDefaultQueue());

      // check for messages; should receive one
      mReceived = (TextMessage) qRec.receive(timeout);
      if (mReceived == null) {
        logMsg("Did not receive message!");
        throw new Exception("Did not receive expected message");
      }
      if (mReceived.getText().equals(msg)) {
        logMsg("Received orignal message again. Was not acknowledged.");
      } else {
        throw new Exception("Received unexpected message");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("rollbackRecoverQueueTest");
    }
  }

  /*
   * @testName: redeliveredFlagTxQueueTest
   * 
   * @assertion_ids: JMS:SPEC:129; JMS:JAVADOC:371; JMS:SPEC:13;
   * 
   * @test_Strategy: Send message to a queue and receive it with a CLIENT_ACK
   * session. Check that the redelivered flag is FALSE. Call rollback and
   * receive the message again. Check that the flag is now TRUE.
   */
  public void redeliveredFlagTxQueueTest() throws Fault {
    try {
      TextMessage tMsg = null;
      QueueSession qSess = null;
      QueueReceiver qRec = null;

      // create queue setup with CLIENT_ACK session for receiving
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueReceiver().close();
      qSess = tool.getDefaultQueueConnection().createQueueSession(true,
          Session.CLIENT_ACKNOWLEDGE);
      qRec = qSess.createReceiver(tool.getDefaultQueue());
      tool.getDefaultQueueConnection().start();

      // create and send message
      logTrace("send and receive one message");
      tMsg = tool.getDefaultQueueSession().createTextMessage();
      tMsg.setText("test message for redelivered flag");
      tMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "redeliveredFlagTxQueueTest");
      tool.getDefaultQueueSender().send(tMsg);
      qSess.commit();

      // receive message and check flag
      tMsg = (TextMessage) qRec.receive(timeout);
      if (tMsg == null) {
        logMsg("Did not receive expected message!");
        throw new Exception("Did not receive message");
      }
      logTrace("Message received. Check redelivered flag.");
      boolean redelivered = tMsg.getJMSRedelivered();

      logTrace("redelivered = " + redelivered);
      if (redelivered == true) {
        throw new Exception("Message redelivered flag should be false");
      }

      // rollback, receive, check flag
      logTrace("calling rollback()");
      qSess.rollback();
      logTrace("receive message again");
      tMsg = (TextMessage) qRec.receive(timeout);
      qSess.commit();
      redelivered = tMsg.getJMSRedelivered();
      logTrace("redelivered flag = " + redelivered);
      if (redelivered == false) {
        throw new Exception("Message redelivered flag should be true");
      }
    } catch (Exception e) {
      logMsg("Error: " + e);
      throw new Fault("redeliveredFlagTxQueueTest", e);
    }
  }

  /*
   * @testName: transactionRollbackOnSessionCloseRecQTest
   * 
   * @assertion_ids: JMS:SPEC:104;
   * 
   * @test_Strategy: Use the default queue session, receiver and sender. Set up
   * an additional queue session and sender. Send and receive a transacted
   * message. Send another transacted message, but close the session after
   * receive with no commit. Verify that the message is rolled back to the queue
   */
  public void transactionRollbackOnSessionCloseRecQTest() throws Fault {
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      TextMessage messageReceived2 = null;

      // set up test tool for Queue for transacted session
      tool = new JmsTool(JmsTool.TX_QUEUE, user, password, mode);
      logTrace("Start connection");
      tool.getDefaultQueueConnection().start();

      // send and receive message
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText("transaction message test");
      logTrace("Sending message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "transactionRollbackOnSessionCloseRecQTest");
      tool.getDefaultQueueSender().send(messageSent);
      tool.getDefaultQueueSession().commit();
      logTrace("Receiving message");
      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      tool.getDefaultQueueSession().commit();

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        logTrace("Message text: \"" + messageReceived.getText() + "\"");
        logTrace("Received correct message");
      } else {
        throw new Exception("didn't get the right message");
      }

      // Create another Queue Session, and queue sender
      QueueSession newQSess = tool.getDefaultQueueConnection()
          .createQueueSession(true, 0);
      messageSent.setText("part 2");
      logTrace("Sending message");
      tool.getDefaultQueueSender().send(messageSent);
      tool.getDefaultQueueSession().commit();
      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("close session - don't do a commit on receive");
      tool.getDefaultQueueSession().close();

      QueueReceiver qReceiver = newQSess.createReceiver(tool.getDefaultQueue());
      messageReceived2 = (TextMessage) qReceiver.receive(timeout);
      newQSess.commit();
      if (messageReceived.getText().equals(messageReceived2.getText())) {
        logTrace("Message text: \"" + messageReceived.getText() + "\"");
        logTrace("Message2 text: \"" + messageReceived2.getText() + "\"");
        logTrace("Received correct message");
      } else {
        throw new Fault(
            "received message not rolled back to q on close session");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("transactionRollbackOnSessionCloseRecQTest");
    }
  }

  /*
   * @testName: transactionRollbackOnSendQTest
   * 
   * @assertion_ids: JMS:SPEC:123;
   * 
   * @test_Strategy: Use the default queue session, receiver and sender. Set up
   * an additional queue session and sender. Send and receive a transacted
   * message. Send another transacted message, but rollback after the send.
   * Verify that the message is not received.
   */
  public void transactionRollbackOnSendQTest() throws Fault {
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      logTrace("Start connection");
      tool.getDefaultQueueConnection().start();

      // Create another queue session and sender
      QueueSession newQSess = tool.getDefaultQueueConnection()
          .createQueueSession(true, 0);
      QueueSender newSender = newQSess.createSender(tool.getDefaultQueue());

      // send and receive message
      logTrace("Creating 1 message");
      messageSent = newQSess.createTextMessage();
      messageSent.setBooleanProperty("lastMessage", false);
      messageSent.setText("transaction message test");
      logTrace("Sending message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "transactionRollbackOnSendQTest");
      newSender.send(messageSent);
      logTrace("Call commit");
      newQSess.commit();
      logTrace("Receiving message");
      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        logTrace("Message text: \"" + messageReceived.getText() + "\"");
        logTrace("Received correct message");
      } else {
        throw new Exception("didn't get the right message");
      }

      // Send another message, but rollback the session.
      logTrace("Send a message, but then rollback");
      newSender.send(messageSent);
      newQSess.rollback();

      logTrace("Send 3rd message");
      messageSent.setBooleanProperty("lastMessage", true);
      newSender.send(messageSent);
      newQSess.commit();

      logTrace("Attempt to receive last message only");
      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);

      if (messageReceived == null) {
        throw new Fault("Fail: Should have received message");
      } else if (messageReceived.getBooleanProperty("lastMessage") == true) {
        logTrace("Pass: last msg received, proper message was rolledback");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("transactionRollbackOnSendQTest");
    }
  }

  /*
   * @testName: transactionRollbackOnRecQTest
   * 
   * @assertion_ids: JMS:SPEC:123;
   * 
   * @test_Strategy: Use the default queue session, receiver and sender. Set up
   * an additional queue session and sender. Send and receive a transacted
   * message. Send another transacted message, but rollback the session after
   * receive. Verify that the message is rolled back to the queue
   */
  public void transactionRollbackOnRecQTest() throws Fault {
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      TextMessage messageReceived2 = null;

      // set up test tool for Queue for transacted session
      tool = new JmsTool(JmsTool.TX_QUEUE, user, password, mode);
      logTrace("Start connection");
      tool.getDefaultQueueConnection().start();

      // send and receive message
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText("transaction message test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "transactionRollbackOnRecQTest");
      logTrace("Sending message");
      tool.getDefaultQueueSender().send(messageSent);
      logTrace("Call commit");
      tool.getDefaultQueueSession().commit();
      logTrace("Receiving message");
      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("Call commit");
      tool.getDefaultQueueSession().commit();

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        logTrace("Message text: \"" + messageReceived.getText() + "\"");
        logTrace("Received correct message");
      } else {
        throw new Exception("didn't get the right message");
      }
      // Send another message
      // Verify that now the message will not be received.
      messageSent.setText("receive rollback");
      logTrace("Sending message");
      tool.getDefaultQueueSender().send(messageSent);

      // commit.
      tool.getDefaultQueueSession().commit();
      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("rollback session on receive");
      tool.getDefaultQueueSession().rollback();
      logTrace("Message should have been recovered");
      messageReceived2 = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      tool.getDefaultQueueSession().commit();
      if (messageReceived2 == null) {
        throw new Fault("Fail:  message not recovered");
      }
      if (messageReceived.getText().equals(messageReceived2.getText())) {
        logTrace("Message text: \"" + messageReceived.getText() + "\"");
        logTrace("Message2 text: \"" + messageReceived2.getText() + "\"");
        logTrace("Pass:  Message rolled back to the Q");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("transactionRollbackOnRecQTest");
    }
  }

  /*
   * @testName: txRollbackOnConnectionCloseRecQTest
   * 
   * @assertion_ids: JMS:SPEC:104;
   * 
   * @test_Strategy: Use the default queue session, receiver and sender. Set up
   * an additional queue session and sender. Send and receive a transacted
   * message. Send another transacted message, but close the connection after
   * receive with no commit. Verify that the message is rolled back to the queue
   */
  public void txRollbackOnConnectionCloseRecQTest() throws Fault {
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      TextMessage messageReceived2 = null;

      // set up test tool for Queue for transacted session
      tool = new JmsTool(JmsTool.TX_QUEUE, user, password, mode);
      logTrace("Start connection");
      tool.getDefaultQueueConnection().start();
      tool.getDefaultQueueReceiver().close();
      // open a new connection
      logTrace("Creating new Connection");
      QueueConnection newQConn = (QueueConnection) tool
          .getNewConnection(JmsTool.QUEUE, user, password);
      connections.add(newQConn);

      // Create another Queue Session, queue receiver and queue sender
      QueueSession newQSess = newQConn.createQueueSession(true,
          Session.AUTO_ACKNOWLEDGE);
      QueueSender qSender = newQSess.createSender(tool.getDefaultQueue());
      QueueReceiver qReceiver = newQSess.createReceiver(tool.getDefaultQueue());

      newQConn.start();

      // send and receive message
      logTrace("Creating 1 message");
      messageSent = newQSess.createTextMessage();
      messageSent.setText("transaction message test");
      logTrace("Sending message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "txRollbackOnConnectionCloseRecQTest");
      qSender.send(messageSent);
      logTrace("Call commit");
      newQSess.commit();
      logTrace("Receiving message");
      messageReceived = (TextMessage) qReceiver.receive(timeout);
      logTrace("Call commit");
      newQSess.commit();

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        logTrace("Message text: \"" + messageReceived.getText() + "\"");
        logTrace("Received correct message");
      } else {
        throw new Exception("didn't get the right message");
      }

      // Send another message
      // Verify that now the message will not be received.
      messageSent.setText("part 2");
      logTrace("Sending message");
      qSender.send(messageSent);

      // commit.
      newQSess.commit();
      messageReceived = (TextMessage) qReceiver.receive(timeout);
      logTrace("close connection - don't do a commit on receive");
      newQConn.close();
      tool.getDefaultQueueConnection().start();

      QueueReceiver qR = tool.getDefaultQueueSession()
          .createReceiver(tool.getDefaultQueue());
      messageReceived2 = (TextMessage) qR.receive(timeout);
      logTrace("Receive was not commited, message should be restored");
      tool.getDefaultQueueSession().commit();
      if (messageReceived == null) {
        throw new Fault(
            "Fail:received message not rolled back to q on close session");
      }
      if (messageReceived.getText().equals(messageReceived2.getText())) {
        logTrace("Message text: \"" + messageReceived.getText() + "\"");
        logTrace("Message2 text: \"" + messageReceived2.getText() + "\"");
        logTrace("Pass: message restored to Q");
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("txRollbackOnConnectionCloseRecQTest");
    }
  }

  /*
   * @testName: txCloseRolledBackSessionRecQTest
   * 
   * @assertion_ids: JMS:SPEC:104;
   * 
   * @test_Strategy: Use the default queue session, receiver and sender. Set up
   * an additional queue session and sender. Send and receive a transacted
   * message. Send another transacted message, do a rollback and close the
   * sesion after receive with no commit. Verify that the message is rolled back
   * to the queue.
   */
  public void txCloseRolledBackSessionRecQTest() throws Fault {
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      TextMessage messageReceived2 = null;

      // set up test tool for Queue for transacted session
      tool = new JmsTool(JmsTool.TX_QUEUE, user, password, mode);
      logTrace("Start connection");
      tool.getDefaultQueueConnection().start();

      // Create another Queue Session, queue receiver and queue sender
      QueueSession newQSess = tool.getDefaultQueueConnection()
          .createQueueSession(true, Session.AUTO_ACKNOWLEDGE);
      QueueSender qSender = newQSess.createSender(tool.getDefaultQueue());

      tool.getDefaultQueueReceiver().close();
      QueueReceiver qReceiver = newQSess.createReceiver(tool.getDefaultQueue());
      // send and receive message
      logTrace("Creating 1 message");
      messageSent = newQSess.createTextMessage();
      messageSent.setText("transaction message test");
      logTrace("Sending message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "txCloseRolledBackSessionRecQTest");
      qSender.send(messageSent);
      logTrace("Call commit");
      newQSess.commit();
      logTrace("Receiving message");
      messageReceived = (TextMessage) qReceiver.receive(timeout);
      logTrace("Call commit");
      newQSess.commit();

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        logTrace("Message text: \"" + messageReceived.getText() + "\"");
        logTrace("Received correct message");
      } else {
        throw new Exception("didn't get the right message");
      }

      // Send another message
      // Verify that now the message will not be received.
      messageSent.setText("part 2");
      logTrace("Sending message");
      qSender.send(messageSent);

      // commit.
      newQSess.commit();
      messageReceived = (TextMessage) qReceiver.receive(timeout);
      newQSess.rollback();
      logTrace("close session - don't do a commit on receive");
      newQSess.close();
      tool.getDefaultQueueConnection().start();
      QueueReceiver qR = tool.getDefaultQueueSession()
          .createReceiver(tool.getDefaultQueue());
      messageReceived2 = (TextMessage) qR.receive(timeout);

      tool.getDefaultQueueSession().commit();
      if (messageReceived.getText().equals(messageReceived2.getText())) {
        logTrace("Message text: \"" + messageReceived.getText() + "\"");
        logTrace("Message2 text: \"" + messageReceived2.getText() + "\"");
        logTrace("Received correct message");
      } else {
        throw new Fault(
            "received message not rolled back to q on close session");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("txCloseRolledBackSessionRecQTest");
    }
  }

  /*
   * @testName: txMultiQTest
   * 
   * @assertion_ids: JMS:SPEC:125;
   * 
   * @test_Strategy: Use 2 destination Queues - the default and create a new
   * one. In the default session Send and commit a message to MY_QUEUE Create a
   * new newQSess, qSender and qReceiver for Q2. Send a msg to Q2. - no commit
   * yet Have qReceiver request a msg - expect none - send was not committed.
   * Request a msg with the default receiver - expect a msg from MY_QUEUE commit
   * the send form newQSess - for Q2. Verify qReceiver receives message from Q2.
   * 
   */
  public void txMultiQTest() throws Fault {
    try {
      TextMessage messageSent = null;
      TextMessage messageSent2 = null;
      TextMessage messageReceived = null;

      // set up test tool for Queue for transacted session
      tool = new JmsTool(JmsTool.TX_QUEUE, user, password, mode);
      logTrace("Start connection");
      tool.getDefaultQueueConnection().start();

      // Create another Queue Session, queue receiver and queue sender
      QueueSession newQSess = tool.getDefaultQueueConnection()
          .createQueueSession(true, Session.AUTO_ACKNOWLEDGE);
      QueueSession newQSess1 = tool.getDefaultQueueConnection()
          .createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
      Queue newQueue = tool.createNewQueue("Q2");
      QueueSender qSender = newQSess.createSender(newQueue);
      QueueReceiver qReceiver = newQSess1.createReceiver(newQueue);
      queues.add(newQueue);

      // send and commit a message MY_QUEUE
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText("This is from MY_QUEUE");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "txMultiQTest");
      tool.getDefaultQueueSender().send(messageSent);
      tool.getDefaultQueueSession().commit();

      // send a message to Q2 without commit.
      messageSent2 = newQSess.createTextMessage();
      messageSent2.setText("This is from Q2");
      messageSent2.setStringProperty("COM_SUN_JMS_TESTNAME", "txMultiQTest");
      qSender.send(messageSent2);

      // qReceiver is from NewQ session - it's Q is "Q2" -
      logTrace("Verify there is no msg to receive");
      messageReceived = (TextMessage) qReceiver.receive(timeout);
      if (messageReceived != null) {
        throw new Fault(
            "Error: Did not commit send - should not have received a msg!");
      }

      // commit new sessions send
      newQSess.commit();

      // Now look at the default Q - should be a committed msg to receive.
      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Fault("Did not get the committed msg from MY_QUEUE!");
      }
      logTrace(" msg received for default q is: " + messageReceived.getText());

      // Verify that this is the correct message
      if (messageReceived.getText().equals(messageSent.getText())) {
        logTrace("Message text: \"" + messageReceived.getText() + "\"");
      } else {
        throw new Fault("This was not the expected message!!");
      }

      // commit the receive from MY_QUEUE
      tool.getDefaultQueueSession().commit();

      // now there should be a message to receive. from Q2
      // qReceiver is from NewQ session - it's Queue is "Q2" -
      logTrace("Now there should be a message to receive");
      messageReceived = (TextMessage) qReceiver.receive(timeout);
      if (messageReceived == null) {
        throw new Fault("Error: Should have received a msg!");
      } else {
        logTrace("Msg recvd = " + messageReceived);
      }
      logTrace(" msg received for Q2 is: " + messageReceived.getText());

      // Verify that this is the correct message
      if (messageReceived.getText().equals(messageSent2.getText())) {
        logTrace("Message text: \"" + messageReceived.getText() + "\"");
      } else {
        throw new Fault("This was not the expected message!!");
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("txMultiQTest");
    }
  }

  /*
   * @testName: commitRollbackMultiMsgsTest
   *
   * @assertion_ids: JMS:JAVADOC:231; JMS:JAVADOC:221; JMS:JAVADOC:244;
   * JMS:JAVADOC:242; JMS:JAVADOC:229; JMS:JAVADOC:504; JMS:JAVADOC:510;
   * JMS:JAVADOC:597; JMS:JAVADOC:334;
   *
   * @test_Strategy: Test the following APIs:
   *
   * ConnectionFactory.createConnection(String, String)
   * Connection.createSession(boolean, int) Session.createTextMessage(String)
   * Session.createConsumer(Destination) Session.createProducer(Destination)
   * Session.commit() Session.rollback() MessageProducer.send(Message)
   * MessagingConsumer.receive(long timeout)
   * 
   * 1. Create Session with SESSION_TRANSACTED. This is done in the setup()
   * routine. 2. Send x messages to a Queue. 3. Call rollback() to rollback the
   * sent messages. 4. Create a MessageConsumer to consume the messages in the
   * Queue. Should not receive any messages since the sent messages were rolled
   * back. Verify that no messages are received. 5. Send x messages to a Queue.
   * 6. Call commit() to commit the sent messages. 7. Create a MessageConsumer
   * to consume the messages in the Queue. Should receive all the messages since
   * the sent messages were committed. Verify that all messages are received.
   */
  public void commitRollbackMultiMsgsTest() throws Fault {
    boolean pass = true;
    int numMessages = 3;
    try {
      TextMessage tempMsg = null;

      // set up JmsTool for COMMON_QTX setup
      tool = new JmsTool(JmsTool.COMMON_QTX, user, password, mode);
      Destination destination = tool.getDefaultDestination();
      Session session = tool.getDefaultSession();
      Connection connection = tool.getDefaultConnection();
      MessageProducer producer = tool.getDefaultProducer();
      MessageConsumer consumer = tool.getDefaultConsumer();
      Queue queue = (Queue) destination;
      connection.start();
      queues.add(queue);

      // Send "numMessages" messages to Queue and call rollback
      TestUtil.logMsg(
          "Send " + numMessages + " messages to Queue and call rollback()");
      for (int i = 1; i <= numMessages; i++) {
        tempMsg = session.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "commitRollbackMultiMsgsTest" + i);
        producer.send(tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      session.rollback();

      TestUtil.logMsg(
          "Should not consume any messages in Queue since rollback() was called");
      tempMsg = (TextMessage) consumer.receive(timeout);
      if (tempMsg != null) {
        TestUtil.logErr("Received message " + tempMsg.getText()
            + ", expected NULL (NO MESSAGES)");
        pass = false;
      }

      // Send "numMessages" messages to Queue and call commit
      TestUtil.logMsg(
          "Send " + numMessages + " messages to Queue and call commit()");
      for (int i = 1; i <= numMessages; i++) {
        tempMsg = session.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "commitRollbackMultiMsgsTest" + i);
        producer.send(tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      session.commit();

      TestUtil.logMsg(
          "Should consume all messages in Queue since commit() was called");
      for (int msgCount = 1; msgCount <= numMessages; msgCount++) {
        tempMsg = (TextMessage) consumer.receive(timeout);
        if (tempMsg == null) {
          TestUtil.logErr("MessageConsumer.receive() returned NULL");
          TestUtil.logErr("Message " + msgCount + " missing from Queue");
          pass = false;
        } else if (!tempMsg.getText().equals("Message " + msgCount)) {
          TestUtil.logErr("Received [" + tempMsg.getText()
              + "] expected [Message " + msgCount + "]");
          pass = false;
        } else {
          TestUtil.logMsg("Received message: " + tempMsg.getText());
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("commitRollbackMultiMsgsTest");
    }

    if (!pass) {
      throw new Fault("commitRollbackMultiMsgsTest failed");
    }
  }
}
