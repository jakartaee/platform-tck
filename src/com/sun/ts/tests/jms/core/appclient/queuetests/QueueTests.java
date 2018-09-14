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
package com.sun.ts.tests.jms.core.appclient.queuetests;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import com.sun.javatest.Status;

public class QueueTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.appclient.queuetests.QueueTests";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // JMS objects
  private transient JmsTool tool = null;

  ArrayList queues = null;

  ArrayList connections = null;

  // Harness req's
  private Properties props = null;

  // properties read from ts.jte file
  long timeout;

  String user;

  String password;

  String mode;

  // used for tests
  private static final int numMessages = 3;

  private static final int iterations = 5;
  /* Run test in standalone mode */

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    QueueTests theTests = new QueueTests();
    Status s = theTests.run(args, System.out, System.err);

    s.exit();
  }

  /* Test setup: */

  /*
   * setup() is called before each test
   * 
   * Creates Administrator object and deletes all previous Destinations.
   * Individual tests create the JmsTool object with one default Queue
   * Connection, as well as a default Queue and Topic. Tests that require
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
        throw new Exception("'user' in ts.jte must be null ");
      }
      if (password == null) {
        throw new Exception("'password' in ts.jte must be null ");
      }
      if (mode == null) {
        throw new Exception("'mode' in ts.jte must be null");
      }

      // for cleanup purposes - set up an array list of the queues the tests use
      // add default queue
      queues = new ArrayList(10);
      connections = new ArrayList(5);
      TestUtil.logMsg("adding the default queue to the queue cleaner lsit\n");

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
        tool.doClientQueueTestCleanup(connections, queues);
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      logErr("An error occurred while cleaning");
      throw new Fault("Cleanup failed!", e);
    }
  }

  /*
   * @testName: receiveNullClosedSessionQueueTest
   *
   * @assertion_ids: JMS:JAVADOC:12; JMS:JAVADOC:13;
   *
   * @test_Strategy: Create a session in a separate thread that calls receive()
   * and replies to the message. Have the thread call receive() again with no
   * message sent. Close the thread's session and the receive() call should
   * return, finishing the thread's run() method. Verify that the thread is no
   * longer running.
   */
  public void receiveNullClosedSessionQueueTest() throws Fault {
    int waitTime = 15; // seconds

    try {

      // create Queue setup
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      logMsg("Close default QueueReceiver");
      tool.getDefaultQueueReceiver().close();
      tool.getDefaultQueueConnection().start();

      // create QueueRequestor for reply
      logMsg("Create QueueRequestor");
      QueueRequestor qReq = new QueueRequestor(tool.getDefaultQueueSession(),
          tool.getDefaultQueue());

      // create a thread to receive
      logMsg("Create SessionThread");
      SessionThread sT = new SessionThread(tool.getDefaultQueueConnection(),
          (TopicConnection) null);

      logMsg("Tell SessionThread to respond to messages");
      sT.setReplyToMessages(true);
      logMsg("Create Receiver in SessionThread");
      sT.createConsumer(tool.getDefaultQueue());
      logMsg("Tell receiver to keep receiving\n(it will throw an "
          + "exception and stop when it receives the null message)");
      sT.setStayAlive(true);
      logMsg("Start the SessionThread");
      sT.start();

      // send/receive one message and close thread's session
      logMsg("Send one message and receive reply");
      Message tempMsg = tool.getDefaultQueueSession().createMessage();

      tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "receiveNullClosedSessionQueueTest");
      qReq.request(tempMsg);

      logMsg("Wait " + waitTime + " seconds for receive() to start again "
          + "before closing session...");
      for (int i = 0; i < 100000; i++) {
      }
      logMsg("Close the SessionThread's QueueSession");
      sT.getQueueSession().close();

      // wait for session to close. Using TS timeout here
      logMsg(
          "Wait for thread to close (will close after receiving null message)");
      sT.join();

      // check to see if thread is still waiting
      if (sT.isAlive()) {
        logErr(
            "thread still waiting on receive() -- BAD [could be timing problem]");
        throw new Exception("receive() call still waiting");
      } else {
        logMsg("receive() properly received a null message -- GOOD");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("receiveNullClosedSessionQueueTest");
    }
  }
  /* Tests */

  /*
   * @testName: setClientIDLateQueueTest
   *
   * @assertion_ids: JMS:SPEC:173; JMS:SPEC:198; JMS:SPEC:94; JMS:SPEC:91;
   *
   * @test_Strategy: create a connection, send and receive a msg, then set the
   * ClientID verify that IllegalStateException is thrown.
   *
   */
  public void setClientIDLateQueueTest() throws Fault {
    boolean booleanValue = true;
    boolean pass = true;

    try {
      MapMessage messageSent;
      MapMessage messageReceived;
      QueueConnection qConn;
      QueueSession qSess;
      Queue queue;
      QueueSender qSender;
      QueueReceiver qRec;

      // set up queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      qConn = (QueueConnection) tool.getNewConnection(JmsTool.QUEUE, user,
          password);
      connections.add(qConn);
      qSess = qConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
      queue = tool.createNewQueue("testQueue2");
      // add testQueue2 to our cleanup list
      queues.add(queue);
      qSender = qSess.createSender(queue);
      qRec = qSess.createReceiver(queue);
      qConn.start();

      logTrace("Creating 1 message");
      messageSent = qSess.createMapMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "setClientIDLateQueueTest");
      messageSent.setBoolean("booleanValue", booleanValue);

      // send the message and then get it back
      logTrace("Sending message");
      qSender.send(messageSent);
      logTrace("Receiving message");
      messageReceived = (MapMessage) qRec.receive(timeout);
      // read the boolean
      messageReceived.getBoolean("booleanValue");

      logTrace("Attempt to set Client ID too late");
      try {
        qConn.setClientID("setClientIDLateQueueTest");
        pass = false;
        logMsg("Error: Illegal state exception was not thrown");
      } catch (javax.jms.IllegalStateException is) {
        logTrace("Pass: IllegalStateException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: " + e.getClass().getName() + " was thrown");
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("setClientIDLateQueueTest");
    }
  }

  /*
   * @testName: autoAckMsgListenerQueueTest
   *
   * @assertion_ids: JMS:SPEC:132; JMS:SPEC:136;
   *
   * @test_Strategy: Set up a receiver with a messagelistener. Send two messages
   * to the destination. The message listener will receive the messages and
   * automatically call recover() after the send one. It will verify that the
   * second message only is received a second time. After waiting for the
   * message listener to finish, the test checks the state of the listener.
   */
  public void autoAckMsgListenerQueueTest() throws Fault {
    try {
      Message messageSent = null;
      AutoAckMsgListener mListener = null;

      // create queue setup with message listener
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      logTrace("Create and set MessageListener.");
      mListener = new AutoAckMsgListener(new DoneLatch(),
          tool.getDefaultQueueSession());
      tool.getDefaultQueueReceiver().setMessageListener(mListener);

      // create and send messages
      logTrace("Send and receive two messages");
      messageSent = tool.getDefaultQueueSession().createMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "autoAckMsgListenerQueueTest");
      messageSent.setBooleanProperty("lastMessage", false);
      tool.getDefaultQueueSender().send(messageSent);
      messageSent.setBooleanProperty("lastMessage", true);
      tool.getDefaultQueueSender().send(messageSent);

      tool.getDefaultQueueConnection().start();

      // wait until message is received
      logTrace(
          "waiting until message has been received by message listener...");
      mListener.monitor.waitTillDone();

      // check message listener status
      if (mListener.getPassed() == false) {
        throw new Fault("failed");
      }
    } catch (Exception e) {
      logMsg("Error: " + e);
      throw new Fault("autoAckMsgListenerQueueTest", e);
    }
  }

  /*
   * @testName: serialMsgListenerQueueTest
   *
   * @assertion_ids: JMS:SPEC:120; JMS:SPEC:121; JMS:SPEC:136;
   *
   * @test_Strategy: Create queue sessions with two receivers and message
   * listeners for two queues. Send multiple messages to the queues and then
   * start the connection to begin receiving messages. The message listeners
   * perform a Thread.sleep() in the onMessage() method, checking for concurrent
   * use of the other listener. The test is over when the harness determines
   * that the last message has been received.
   */
  public void serialMsgListenerQueueTest() throws Fault {
    try {
      TextMessage tMsg[] = new TextMessage[numMessages];
      Queue newQ;
      QueueReceiver qRec1;
      QueueReceiver qRec2;
      QueueSender qSender1;
      QueueSender qSender2;
      SerialTestMessageListenerImpl myListener1;
      SerialTestMessageListenerImpl myListener2;

      // set up tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueReceiver().close();
      newQ = tool.createNewQueue("testQueue2");
      queues.add(newQ);

      // set up receivers
      qRec1 = tool.getDefaultQueueSession()
          .createReceiver(tool.getDefaultQueue());
      qRec2 = tool.getDefaultQueueSession().createReceiver(newQ);

      // set up message listeners
      logMsg("Create two message listeners");
      myListener1 = new SerialTestMessageListenerImpl();
      myListener2 = new SerialTestMessageListenerImpl();
      qRec1.setMessageListener(myListener1);
      qRec2.setMessageListener(myListener2);

      // create message producers
      qSender1 = tool.getDefaultQueueSession()
          .createSender(tool.getDefaultQueue());
      qSender2 = tool.getDefaultQueueSession().createSender(newQ);

      // create and send messages before starting connection
      for (int i = 0; i < numMessages; i++) {
        logMsg("Create and send message " + i);
        tMsg[i] = tool.getDefaultQueueSession().createTextMessage();
        tMsg[i].setText("serialMsgListenerQueueTest" + i);
        tMsg[i].setStringProperty("COM_SUN_JMS_TESTNAME",
            "serialMsgListenerQueueTest" + i);

        // set flag on last message and send to both queues
        if (i == (numMessages - 1)) {
          tMsg[i].setBooleanProperty("COM_SUN_JMS_TEST_LASTMESSAGE", true);
          qSender1.send(tMsg[i]);
          qSender2.send(tMsg[i]);
        } else { // send to one queue or the other
          tMsg[i].setBooleanProperty("COM_SUN_JMS_TEST_LASTMESSAGE", false);
          if (i % 2 == 0) {
            qSender1.send(tMsg[i]);
          } else {
            qSender2.send(tMsg[i]);
          }
        }
      }
      logMsg("Start connection");
      tool.getDefaultQueueConnection().start();

      // wait until test is over
      myListener1.monitor.waitTillDone();
      myListener2.monitor.waitTillDone();
      if ((myListener1.testFailed == true)
          || (myListener2.testFailed == true)) {
        logMsg("Test failed in message listener");
        throw new Exception(
            "Concurrent use of MessageListener or JMSException");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("serialMsgListenerQueueTest");
    }
  }

  /*
   * @testName: setGetChangeClientIDQueueTest
   *
   * @assertion_ids: JMS:JAVADOC:272; JMS:SPEC:90; JMS:SPEC:93; JMS:JAVADOC:514;
   * JMS:JAVADOC:512; JMS:JAVADOC:650; JMS:JAVADOC:651;
   *
   * @test_Strategy: Test setClientID()/getClientID(). Make sure that the
   * clientID set is the clientID returned. Then try and reset the clientID.
   * Verify that the IllegalStateException is thrown. 1. use a
   * QueueConnectionFactory that has no ClientID set, then call setClientID
   * twice. Then try and set the clientID on a second connection to the clientID
   * value of the first connection. Verify the InvalidClientIDException is
   * thrown.
   */
  public void setGetChangeClientIDQueueTest() throws Fault {
    boolean pass = true;
    QueueConnection qc, qc2 = null;

    try {
      tool = new JmsTool(JmsTool.QUEUE_FACTORY, user, password, mode);

      qc = (QueueConnection) tool.getNewConnection(JmsTool.QUEUE, user,
          password);
      connections.add(qc);

      qc2 = (QueueConnection) tool.getNewConnection(JmsTool.QUEUE, user,
          password);
      connections.add(qc2);

      TestUtil.logMsg("Setting clientID!");
      qc.setClientID("ctstest");

      TestUtil.logMsg("Getting clientID!");
      String clientid = qc.getClientID();

      if (!clientid.equals("ctstest")) {
        TestUtil.logErr(
            "getClientID() returned " + clientid + ", expected ctstest");
        pass = false;
      } else {
        TestUtil.logMsg("setClientID/getClientID correct");
      }

      TestUtil.logMsg("Resetting clientID! (excpect IllegalStateException)");
      qc.setClientID("changeIt");
      TestUtil.logErr("Failed: No exception on ClientID reset");
      pass = false;
    } catch (InvalidClientIDException e) {
      TestUtil.logErr("Incorrect exception received: " + e.getMessage());
      pass = false;
    } catch (javax.jms.IllegalStateException ee) {
      TestUtil.logMsg("Expected Exception received: " + ee.getMessage());
    } catch (Exception eee) {
      TestUtil.logErr("Incorrect exception received: " + eee.getMessage());
      pass = false;
    }

    try {
      TestUtil.logMsg(
          "Set clientID on second connection to value of clientID on first connection");
      TestUtil.logMsg("Expect InvalidClientIDException");
      qc2.setClientID("ctstest");
      TestUtil
          .logErr("Failed: No exception on ClientID when one already exists");
      pass = false;
    } catch (InvalidClientIDException e) {
      TestUtil.logMsg("Expected exception received: " + e.getMessage());
    } catch (Exception eee) {
      TestUtil.logErr("Incorrect exception received: " + eee.getMessage());
      pass = false;
    }
    if (!pass) {
      throw new Fault("setGetChangeClientIDQueueTest");
    }
  }

  /*
   * @testName: setGetExceptionListenerTest
   *
   * @assertion_ids: JMS:JAVADOC:518; JMS:JAVADOC:520;
   *
   * @test_Strategy: Test setExceptionListener()/getExceptionListener() API's.
   */
  public void setGetExceptionListenerTest() throws Fault {
    boolean pass = true;
    QueueConnection qc = null;

    try {
      TestUtil.logMsg("Setup JmsTool for QUEUE");
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);

      TestUtil.logMsg("Create ExceptionListener");
      ExceptionListener foo = new ExceptionListener() {

        public void onException(JMSException e) {
        }
      };

      TestUtil.logMsg("Call setExceptionListener on Connection object");
      tool.getDefaultQueueConnection().setExceptionListener(foo);

      ExceptionListener foo2 = tool.getDefaultQueueConnection()
          .getExceptionListener();
      if (!foo2.equals(foo)) {
        TestUtil
            .logErr("getExceptionListener doesn't match setExceptionListener");
        pass = false;
      }
    } catch (Exception eee) {
      TestUtil.logErr("Unexpected exception received: " + eee.getMessage());
      pass = false;
    }
    if (!pass) {
      throw new Fault("setGetExceptionListenerTest");
    }
  }

  private static class AutoAckMsgListener implements MessageListener {
    private boolean passed;

    QueueSession session;

    final DoneLatch monitor;

    public AutoAckMsgListener(DoneLatch latch, QueueSession qSession) {
      this.monitor = latch;
      this.session = qSession;
    }

    // get state of test
    public boolean getPassed() {
      return passed;
    }

    // will receive two messages
    public void onMessage(Message message) {
      try {
        if (message.getBooleanProperty("lastMessage") == false) {
          TestUtil.logTrace("Received first message.");
          if (message.getJMSRedelivered() == true) {

            // should not re-receive this one
            TestUtil.logMsg("Error: received first message twice");
            passed = false;
          }
        } else {
          if (message.getJMSRedelivered() == false) {

            // received second message for first time
            TestUtil.logTrace("Received second message. Calling recover()");
            session.recover();
          } else {

            // should be redelivered after recover
            TestUtil.logTrace("Received second message again as expected");
            passed = true;
            monitor.allDone();
          }
        }
      } catch (JMSException e) {
        TestUtil.printStackTrace(e);
        TestUtil.logMsg("Exception caught in message listener:\n" + e);
        passed = false;
        monitor.allDone();
      }

    }
  }

  /*
   * @testName: reverseReceiveClientAckTest
   *
   * @assertion_ids: JMS:SPEC:123; JMS:SPEC:129; JMS:SPEC:91;
   *
   * @test_Strategy: Send x messages to x Queues from x senders. In a different
   * session using client_acknowledge, create x QueueReceivers. Send the
   * messages in order 1,2,3...x. Receive them in order x...3,2,1, calling
   * session.recover() after receiving 1 message. All x messages should be
   * received. ("x" is specified by the numMessages parameter in ts.jte file.)
   *
   * Note: default QueueReceiver can stay open, since testing is done with newly
   * created Destinations
   */
  public void reverseReceiveClientAckTest() throws Fault {
    boolean pass = true;

    try {

      // create Queue Connection
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      Queue q[] = new Queue[numMessages];
      QueueSender qSender[] = new QueueSender[numMessages];
      QueueReceiver qReceiver[] = new QueueReceiver[numMessages];
      Message msg[] = new Message[numMessages];

      // create destinations using default session
      for (int i = 0; i < numMessages; i++) {
        TestUtil.logMsg("Creating Queue " + i + " of " + (numMessages - 1)
            + " (" + numMessages + " total)");
        q[i] = tool.createNewQueue("testQ" + i);
        queues.add(q[i]);
      }

      // use default session for sending
      TestUtil.logMsg("Creating " + numMessages + " senders");
      for (int i = 0; i < numMessages; i++) {
        TestUtil.logMsg("sender " + i);
        qSender[i] = tool.getDefaultQueueSession().createSender(q[i]);
      }

      // create session for receiving
      TestUtil.logMsg("Creating CLIENT_ACKNOWLEDGE session for receiving");
      QueueSession receiveSession = tool.getDefaultQueueConnection()
          .createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);

      // create receivers for receive session
      TestUtil
          .logMsg("Creating " + numMessages + " receivers in receive session");
      for (int i = 0; i < numMessages; i++) {
        TestUtil.logMsg("receiver " + i);
        qReceiver[i] = receiveSession.createReceiver(q[i]);
      }

      // start the connection
      tool.getDefaultQueueConnection().start();

      // send messages: 1,2,3,...
      Message tempMsg = null;

      for (int i = 0; i < numMessages; i++) {
        TestUtil.logMsg("Sending message " + i);
        tempMsg = tool.getDefaultQueueSession().createMessage();
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "reverseReceiveClientAckTest");
        qSender[i].send(tempMsg);
      }

      // receive messages: ...,3,2,1
      TestUtil.logMsg(
          "Receive messages 0-" + (numMessages - 1) + " in reverse order");
      for (int i = (numMessages - 1); i >= 0; i--) {
        TestUtil.logMsg("Receive message " + i);
        msg[i] = qReceiver[i].receive(timeout);
        if (msg[i] == null) {
          TestUtil.logErr("Did not receive message from receiver[" + i + "]");
          pass = false;
        } else {
          TestUtil.logMsg("msg = " + msg[i]);
          TestUtil.logMsg("Acknowledge message " + i);
          msg[i].acknowledge();
        }

        // recover after receiving 1 message
        if (i == (numMessages - 1)) {
          TestUtil.logMsg("session.recover()");
          receiveSession.recover();
        }
      }

      TestUtil.logMsg(
          "Try receiving message from all receivers again (should not receive any)");
      for (int i = (numMessages - 1); i >= 0; i--) {
        msg[i] = qReceiver[i].receive(timeout);
        if (msg[i] != null) {
          TestUtil.logErr(
              "Received message from receiver[" + i + "], expected none");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("reverseReceiveClientAckTest");
    } finally {
      try {
        TestUtil.logMsg("Cleanup: Closing Queue Connections");
        tool.doClientQueueTestCleanup(connections, queues);
      } catch (Exception e) {
        TestUtil.logErr("An error occurred while cleaning", e);
        throw new Fault("Cleanup failed!", e);
      }
    }
    if (!pass) {
      throw new Fault("reverseReceiveClientAckTest");
    }
  }

  /*
   * @testName: clientAckQueueTest
   *
   * @assertion_ids: JMS:SPEC:131; JMS:JAVADOC:122; JMS:SPEC:91;
   *
   * @test_Strategy: Send three messages to Queue. Receive all three and call
   * acknowledge on msg 2. Send and receive message 4. Recover and attempt to
   * receive msg 4.
   */
  public void clientAckQueueTest() throws Fault {
    boolean pass = true;

    try {
      TextMessage sent1 = null;
      TextMessage sent2 = null;
      TextMessage sent3 = null;
      TextMessage sent4 = null;

      TextMessage rec2 = null;
      TextMessage rec4 = null;

      QueueSession qSess = null;
      QueueReceiver qRec = null;
      QueueSender qSender = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueReceiver().close();
      tool.getDefaultQueueSession().close();

      qSess = tool.getDefaultQueueConnection().createQueueSession(false,
          Session.CLIENT_ACKNOWLEDGE);
      TestUtil.logMsg("Start connection");
      tool.getDefaultQueueConnection().start();

      // create two messages
      sent1 = qSess.createTextMessage();
      sent1.setText("test message 1");
      sent1.setStringProperty("COM_SUN_JMS_TESTNAME", "clientAckQueueTest1");
      sent2 = qSess.createTextMessage();
      sent2.setText("test message 2");
      sent2.setStringProperty("COM_SUN_JMS_TESTNAME", "clientAckQueueTest2");
      sent3 = qSess.createTextMessage();
      sent3.setText("test message 3");
      sent3.setStringProperty("COM_SUN_JMS_TESTNAME", "clientAckQueueTest3");

      sent4 = qSess.createTextMessage();
      sent4.setText("test message 4");
      sent4.setStringProperty("COM_SUN_JMS_TESTNAME", "clientAckQueueTest4");

      // create CLIENT_ACK session and consumer
      qRec = qSess.createReceiver(tool.getDefaultQueue());
      qSender = qSess.createSender(tool.getDefaultQueue());

      // send messages
      TestUtil.logTrace("Send three messages");
      qSender.send(sent1);
      qSender.send(sent2);
      qSender.send(sent3);

      // receive messages and acknowledge second
      TestUtil.logTrace("Receive three messages");
      qRec.receive(timeout);
      TestUtil.logTrace("Received the first message");
      rec2 = (TextMessage) qRec.receive(timeout);
      TestUtil.logTrace("Received the second message");
      qRec.receive(timeout);
      TestUtil.logTrace("Received the third message");

      // acknowledging msg 2 of the 3 received messages should acknowledge all 3
      // messages.
      TestUtil.logTrace("Acknowledging the second message");
      rec2.acknowledge();

      // send and receive message 4
      TestUtil.logTrace("Send the fourth message");
      qSender.send(sent4);
      TestUtil.logTrace("Receive the fourth message");
      rec4 = (TextMessage) qRec.receive(timeout);
      TestUtil.logTrace("Received the fourth message");

      // recover and attempt to receive fourth message
      TestUtil.logTrace("Call session.recover()");
      qSess.recover();
      TestUtil.logTrace(
          "Attempt to receive unacked message - the fourth message again");
      rec4 = (TextMessage) qRec.receive(timeout);
      if (rec4 == null) {
        pass = false;
        TestUtil.logErr("Did not receive unacked message");
      } else {
        if (!rec4.getText().equals(sent4.getText())) {
          pass = false;
          TestUtil.logErr("Received wrong message: " + rec4.getText());
        } else {
          TestUtil.logMsg("Re-received message: " + rec4.getText());
        }
        TestUtil.logTrace("Acknowledge the received message");
        rec4.acknowledge();
      }

      if (!pass)
        throw new Fault("clientAckQueueTest Failed!!");

    } catch (Exception e) {
      TestUtil.logErr("Error: ", e);
      throw new Fault("clientAckQueueTest", e);
    } finally {
      try {
        TestUtil.logMsg("Cleanup: Closing Queue Connections");
        tool.doClientQueueTestCleanup(connections, queues);
      } catch (Exception e) {
        TestUtil.logErr("An error occurred while cleaning", e);
        throw new Fault("Cleanup failed!", e);
      }
    }
  }

  /*
   * @testName: nonAckMsgsRedeliveredQueueTest
   *
   * @assertion_ids: JMS:SPEC:145; JMS:JAVADOC:122; JMS:SPEC:91;
   *
   * @test_Strategy: Send messages to a queue that has a single QueueReceiver in
   * a CLIENT_ACKNOWLEDGE session. Receive all the messages and close the
   * session without acknowledging the messages. Create a new receiver and
   * verify that all the messages may still be received from the queue.
   */
  public void nonAckMsgsRedeliveredQueueTest() throws Fault {
    boolean pass = true;

    try {
      TextMessage tempMsg;

      // create default QueueSession for sending messages
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      TestUtil.logTrace(
          "Close QueueReceiver in default session -- only want one for Queue");
      tool.getDefaultQueueReceiver().close();
      tool.getDefaultQueueSession().close();

      // create client_ack session for queue
      QueueSession qSession = tool.getDefaultQueueConnection()
          .createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
      QueueReceiver qReceiver = qSession.createReceiver(tool.getDefaultQueue());
      QueueSender qSender = qSession.createSender(tool.getDefaultQueue());

      // start connection
      tool.getDefaultQueueConnection().start();

      // send messages
      for (int i = 0; i < numMessages; i++) {
        tempMsg = qSession.createTextMessage();
        tempMsg.setText("test message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "nonAckMsgsRedeliveredQueueTest" + i);
        qSender.send(tempMsg);
        TestUtil.logTrace("sent message " + i);
      }

      // receive messages but do not acknowledge them
      for (int i = 0; i < numMessages; i++) {
        tempMsg = (TextMessage) qReceiver.receive(timeout);
        if (tempMsg == null) {
          pass = false;
          TestUtil.logErr("Did not receive message " + i);
        } else {
          TestUtil.logTrace("received message " + i);
        }
      }
      // close session
      qSender.close();
      qReceiver.close();
      qSession.close();

      // create new receiver and receive messages
      qSession = tool.getDefaultQueueConnection().createQueueSession(false,
          Session.AUTO_ACKNOWLEDGE);
      qReceiver = qSession.createReceiver(tool.getDefaultQueue());
      for (int i = 0; i < numMessages; i++) {
        tempMsg = (TextMessage) qReceiver.receive(timeout);
        if (tempMsg == null) {
          pass = false;
          TestUtil.logErr("Did not receive message " + i);
        } else {
          TestUtil.logTrace(
              "received message \"" + tempMsg.getText() + "\" second time");
        }
      }

      if (!pass)
        throw new Fault("nonAckMsgsRedeliveredQueueTest failed!!!");
    } catch (Exception e) {
      TestUtil.logErr("nonAckMsgsRedeliveredQueueTest failed: ", e);
      throw new Fault("nonAckMsgsRedeliveredQueueTest failed", e);
    } finally {
      try {
        TestUtil.logMsg("Cleanup: Closing Queue Connections");
        tool.doClientQueueTestCleanup(connections, queues);
      } catch (Exception e) {
        TestUtil.logErr("An error occurred while cleaning", e);
        throw new Fault("Cleanup failed!", e);
      }
    }
  }

  /*
   * @testName: queueRequestorSimpleSendAndRecvTest
   * 
   * @assertion_ids: JMS:JAVADOC:12; JMS:JAVADOC:13; JMS:JAVADOC:15;
   * JMS:SPEC:273;
   * 
   * @test_Strategy: Send and receive simple JMS message using QueueRequestor
   * helper class. Tests the following API's:
   * 
   * QueueRequestor(QueueSession, Queue) QueueRequestor.request(Message)
   * QueueRequestor.close()
   */
  public void queueRequestorSimpleSendAndRecvTest() throws Fault {
    boolean pass = true;
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      QueueRequestor qreq = null;

      // set up test tool for Queue
      TestUtil.logMsg("Set up JmsTool for QUEUE");
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      TestUtil.logMsg("Creating 1 message");
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText("This is the request message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "queueRequestorSimpleSendAndRecvTest");

      // set up MessageListener
      TestUtil.logMsg("Set up MessageListener");
      tool.getDefaultQueueReceiver().setMessageListener(
          new RequestorMsgListener(tool.getDefaultQueueSession()));

      // set up QueueRequestor
      TestUtil.logMsg(
          "Set up QueueRequestor for request/response message exchange");
      QueueSession newqs = tool.getDefaultQueueConnection()
          .createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
      qreq = new QueueRequestor(newqs, tool.getDefaultQueue());
      TestUtil.logMsg(
          "Send message request and receive message response using QueueRequestor");
      TestUtil
          .logMsg("Message request text: \"" + messageSent.getText() + "\"");
      messageReceived = (TextMessage) qreq.request(messageSent);

      // Check to see if correct message received
      TestUtil.logMsg(
          "Message response text: \"" + messageReceived.getText() + "\"");
      if (messageReceived.getText().equals("This is the response message")) {
        TestUtil.logMsg("Received correct response message");
      } else {
        TestUtil.logErr("Received incorrect response message");
        pass = false;
      }

      // Close the QueueRequestor
      TestUtil.logMsg("Close QueueRequestor");
      qreq.close();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("queueRequestorSimpleSendAndRecvTest failed");
    }
    if (!pass) {
      throw new Fault("queueRequestorSimpleSendAndRecvTest failed");
    }
  }

  /*
   * @testName: queueRequestorExceptionTests
   * 
   * @assertion_ids: JMS:JAVADOC:832; JMS:JAVADOC:833; JMS:JAVADOC:16;
   * 
   * @test_Strategy: Test negative exception cases for QueueRequestor API's.
   * Tests the following exceptions: InvalidDestinationException, JMSException.
   */
  public void queueRequestorExceptionTests() throws Fault {
    boolean pass = true;
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      Queue invalidDest = null;
      QueueRequestor qreq = null;

      // set up test tool for Queue
      TestUtil.logMsg("Set up JmsTool for QUEUE");
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();

      // set up MessageListener
      TestUtil.logMsg("Set up MessageListener");
      tool.getDefaultQueueReceiver().setMessageListener(
          new RequestorMsgListener(tool.getDefaultQueueSession()));

      QueueSession newqs = tool.getDefaultQueueConnection()
          .createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

      // Try and set up QueueRequestor with InvalidDestination
      try {
        TestUtil.logMsg("Set up QueueRequestor with InvalidDestination");
        qreq = new QueueRequestor(newqs, invalidDest);
        TestUtil.logErr("Didn't throw InvalidDestinationException");
        pass = false;
      } catch (InvalidDestinationException e) {
        TestUtil.logMsg("Caught expected InvalidDestinationException");
      }

      // Try and set up QueueRequestor with closed QueueSession
      try {
        TestUtil.logMsg("Set up QueueRequestor with a closed QueueSession");
        newqs.close();
        qreq = new QueueRequestor(newqs, tool.getDefaultQueue());
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      }
      tool.closeAllConnections(connections);

      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      TestUtil.logMsg("Set up QueueRequestor");
      qreq = new QueueRequestor(tool.getDefaultQueueSession(),
          tool.getDefaultQueue());

      TestUtil.logMsg("Creating 1 message");
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "queueRequestorExceptionTests");

      // Close the QueueRequestor
      TestUtil.logMsg("Close QueueRequestor");
      qreq.close();

      TestUtil.logMsg(
          "Try a request/response message exchange on a closed QueueRequestor");
      try {
        messageReceived = (TextMessage) qreq.request(messageSent);
        if (messageReceived != null)
          TestUtil.logMsg("messageReceived=" + messageReceived.getText());
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
        qreq.close();
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("queueRequestorExceptionTests failed");
    }
    if (!pass) {
      throw new Fault("queueRequestorExceptionTests failed");
    }
  }

  private static class RequestorMsgListener implements MessageListener {
    QueueSession session = null;

    boolean pass = false;

    public RequestorMsgListener(QueueSession session) {
      this.session = session;
    }

    public boolean getPass() {
      return pass;
    }

    public void onMessage(Message message) {
      try {
        TestUtil.logMsg("RequestorMsgListener.onMessage()");
        if (message instanceof TextMessage) {
          TextMessage tmsg = (TextMessage) message;
          TestUtil.logMsg("Request message=" + tmsg.getText());
          if (tmsg.getText().equals("This is the request message")) {
            TestUtil.logMsg("Received request message is correct");
            pass = true;
          } else {
            TestUtil.logErr("Received request message is incorrect");
            pass = false;
          }
        } else {
          TestUtil.logErr("Received request message is not a TextMessage");
          pass = false;
        }
        Queue replyQ = (Queue) message.getJMSReplyTo();
        QueueSender sender = session.createSender(replyQ);
        TextMessage responseMsg = session.createTextMessage();
        responseMsg.setText("This is the response message");
        responseMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "RequestorMsgListenerResponseMsg");
        TestUtil.logMsg("Sending back response message");
        sender.send(responseMsg);
      } catch (JMSException e) {
        TestUtil.printStackTrace(e);
        TestUtil.logMsg("Exception caught in RequestorMsgListener:\n" + e);
      }
    }
  }
}
