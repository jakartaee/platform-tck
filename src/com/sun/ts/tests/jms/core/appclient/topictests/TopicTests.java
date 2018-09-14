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
 * $Id: TopicTests.java 64776 2012-02-06 17:54:30Z af70133 $
 */
package com.sun.ts.tests.jms.core.appclient.topictests;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;
import com.sun.javatest.Status;

public class TopicTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.appclient.topictests.TopicTests";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // JMS objects
  private transient JmsTool tool = null;

  ArrayList topics = null;

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
    TopicTests theTests = new TopicTests();
    Status s = theTests.run(args, System.out, System.err);

    s.exit();
  }

  /* Test setup: */

  /*
   * setup() is called before each test
   * 
   * Creates Administrator object and deletes all previous Destinations.
   * Individual tests create the JmsTool object with one default Topic
   * Connection, as well as a default Topic and Topic. Tests that require
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

      // for cleanup purposes - set up an array list of the topics the tests use
      // add default topic
      connections = new ArrayList(5);
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
        tool.closeAllConnections(connections);
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      logErr("An error occurred while cleaning");
      throw new Fault("Cleanup failed!", e);
    }
  }

  /*
   * @testName: receiveNullClosedSessionTopicTest
   *
   * @assertion_ids: JMS:JAVADOC:12; JMS:JAVADOC:13;
   *
   * @test_Strategy: Create a session in a separate thread that calls receive()
   * and replies to the message. Have the thread call receive() again with no
   * message sent. Close the thread's session and the receive() call should
   * return, finishing the thread's run() method. Verify that the thread is no
   * longer running.
   */
  public void receiveNullClosedSessionTopicTest() throws Fault {
    int waitTime = 15; // seconds

    try {

      // create Topic setup
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      logMsg("Close default TopicSubscriber");
      tool.getDefaultTopicSubscriber().close();
      tool.getDefaultTopicConnection().start();

      // create TopicRequestor for reply
      logMsg("Create TopicRequestor");
      TopicRequestor qReq = new TopicRequestor(tool.getDefaultTopicSession(),
          tool.getDefaultTopic());

      // create a thread to receive
      logMsg("Create SessionThread");
      SessionThread sT = new SessionThread((QueueConnection) null,
          tool.getDefaultTopicConnection());

      logMsg("Tell SessionThread to respond to messages");
      sT.setReplyToMessages(true);
      logMsg("Create Subscriber in SessionThread");
      sT.createConsumer(tool.getDefaultTopic());
      logMsg("Tell receiver to keep receiving\n(it will throw an "
          + "exception and stop when it receives the null message)");
      sT.setStayAlive(true);
      logMsg("Start the SessionThread");
      sT.start();

      // send/receive one message and close thread's session
      logMsg("Send one message and receive reply");
      Message tempMsg = tool.getDefaultTopicSession().createMessage();

      tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "receiveNullClosedSessionTopicTest");
      qReq.request(tempMsg);

      logMsg("Wait " + waitTime + " seconds for receive() to start again "
          + "before closing session...");
      for (int i = 0; i < 100000; i++) {
      }
      logMsg("Close the SessionThread's TopicSession");
      sT.getTopicSession().close();

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
      throw new Fault("receiveNullClosedSessionTopicTest");
    }
  }
  /* Tests */

  /*
   * @testName: setClientIDLateTopicTest
   *
   * @assertion_ids: JMS:SPEC:173; JMS:SPEC:198; JMS:SPEC:94; JMS:SPEC:91;
   *
   * @test_Strategy: create a connection, send and receive a msg, then set the
   * ClientID verify that IllegalStateException is thrown.
   *
   */
  public void setClientIDLateTopicTest() throws Fault {
    boolean booleanValue = true;
    boolean pass = true;
    String lookup = "MyTopicConnectionFactory";

    try {
      MapMessage messageSent;
      MapMessage messageReceived;
      TopicConnection tConn;
      TopicSession tSess;
      Topic topic;
      TopicPublisher tPublisher;
      TopicSubscriber tSub;

      // set up topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tConn = (TopicConnection) tool.getNewConnection(JmsTool.TOPIC, user,
          password, lookup);
      connections.add(tConn);
      tSess = tConn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
      topic = tool.createNewTopic("MY_TOPIC2");
      tPublisher = tSess.createPublisher(topic);
      tSub = tSess.createSubscriber(topic);
      tConn.start();

      logTrace("Creating 1 message");
      messageSent = tSess.createMapMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "setClientIDLateTopicTest");
      messageSent.setBoolean("booleanValue", booleanValue);

      // send the message and then get it back
      logTrace("Sending message");
      tPublisher.publish(messageSent);
      logTrace("Receiving message");
      messageReceived = (MapMessage) tSub.receive(timeout);
      // read the boolean
      messageReceived.getBoolean("booleanValue");

      logTrace("Attempt to set Client ID too late");
      try {
        tConn.setClientID("setClientIDLateTopicTest");
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
      throw new Fault("setClientIDLateTopicTest");
    }
  }

  /*
   * @testName: autoAckMsgListenerTopicTest
   *
   * @assertion_ids: JMS:SPEC:132; JMS:SPEC:136;
   *
   * @test_Strategy: Set up a receiver with a messagelistener. Send two messages
   * to the destination. The message listener will receive the messages and
   * automatically call recover() after the send one. It will verify that the
   * second message only is received a second time. After waiting for the
   * message listener to finish, the test checks the state of the listener.
   */
  public void autoAckMsgListenerTopicTest() throws Fault {
    try {
      Message messageSent = null;
      AutoAckMsgListener mListener = null;

      // create topic setup with message listener
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      logTrace("Create and set MessageListener.");
      mListener = new AutoAckMsgListener(new DoneLatch(),
          tool.getDefaultTopicSession());
      tool.getDefaultTopicSubscriber().setMessageListener(mListener);

      // create and send messages
      logTrace("Send and receive two messages");
      messageSent = tool.getDefaultTopicSession().createMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "autoAckMsgListenerTopicTest");
      messageSent.setBooleanProperty("lastMessage", false);
      tool.getDefaultTopicPublisher().publish(messageSent);
      messageSent.setBooleanProperty("lastMessage", true);
      tool.getDefaultTopicPublisher().publish(messageSent);

      tool.getDefaultTopicConnection().start();

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
      throw new Fault("autoAckMsgListenerTopicTest", e);
    }
  }

  /*
   * @testName: serialMsgListenerTopicTest
   *
   * @assertion_ids: JMS:SPEC:120; JMS:SPEC:121; JMS:SPEC:136;
   *
   * @test_Strategy: Create topic sessions with two receivers and message
   * listeners for two topics. Send multiple messages to the topics and then
   * start the connection to begin receiving messages. The message listeners
   * perform a Thread.sleep() in the onMessage() method, checking for concurrent
   * use of the other listener. The test is over when the harness determines
   * that the last message has been received.
   */
  public void serialMsgListenerTopicTest() throws Fault {
    try {
      TextMessage tMsg[] = new TextMessage[numMessages];
      Topic newT;
      TopicSubscriber tSub1;
      TopicSubscriber tSub2;
      TopicPublisher tPublisher1;
      TopicPublisher tPublisher2;
      SerialTestMessageListenerImpl myListener1;
      SerialTestMessageListenerImpl myListener2;

      // set up tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicSubscriber().close();
      newT = tool.createNewTopic("MY_TOPIC2");

      // set up receivers
      tSub1 = tool.getDefaultTopicSession()
          .createSubscriber(tool.getDefaultTopic());
      tSub2 = tool.getDefaultTopicSession().createSubscriber(newT);

      // set up message listeners
      logMsg("Create two message listeners");
      myListener1 = new SerialTestMessageListenerImpl();
      myListener2 = new SerialTestMessageListenerImpl();
      tSub1.setMessageListener(myListener1);
      tSub2.setMessageListener(myListener2);

      // create message producers
      tPublisher1 = tool.getDefaultTopicSession()
          .createPublisher(tool.getDefaultTopic());
      tPublisher2 = tool.getDefaultTopicSession().createPublisher(newT);

      // create and send messages before starting connection
      for (int i = 0; i < numMessages; i++) {
        logMsg("Create and send message " + i);
        tMsg[i] = tool.getDefaultTopicSession().createTextMessage();
        tMsg[i].setText("serialMsgListenerTopicTest" + i);
        tMsg[i].setStringProperty("COM_SUN_JMS_TESTNAME",
            "serialMsgListenerTopicTest" + i);

        // set flag on last message and send to both topics
        if (i == (numMessages - 1)) {
          tMsg[i].setBooleanProperty("COM_SUN_JMS_TEST_LASTMESSAGE", true);
          tPublisher1.publish(tMsg[i]);
          tPublisher2.publish(tMsg[i]);
        } else { // send to one topic or the other
          tMsg[i].setBooleanProperty("COM_SUN_JMS_TEST_LASTMESSAGE", false);
          if (i % 2 == 0) {
            tPublisher1.publish(tMsg[i]);
          } else {
            tPublisher2.publish(tMsg[i]);
          }
        }
      }
      logMsg("Start connection");
      tool.getDefaultTopicConnection().start();

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
      throw new Fault("serialMsgListenerTopicTest");
    }
  }

  /*
   * @testName: setGetChangeClientIDTopicTest
   *
   * @assertion_ids: JMS:JAVADOC:272; JMS:SPEC:90; JMS:SPEC:93; JMS:JAVADOC:514;
   * JMS:JAVADOC:512; JMS:JAVADOC:650; JMS:JAVADOC:651;
   *
   * @test_Strategy: Test setClientID()/getClientID(). Make sure that the
   * clientID set is the clientID returned. Then try and reset the clientID.
   * Verify that the IllegalStateException is thrown. 1. use a
   * TopicConnectionFactory that has no ClientID set, then call setClientID
   * twice. Then try and set the clientID on a second connection to the clientID
   * value of the first connection. Verify the InvalidClientIDException is
   * thrown.
   */
  public void setGetChangeClientIDTopicTest() throws Fault {
    boolean pass = true;
    TopicConnection tc, tc2 = null;
    String lookup = "MyTopicConnectionFactory";

    try {
      tool = new JmsTool(JmsTool.TOPIC_FACTORY, user, password, mode);

      tc = (TopicConnection) tool.getNewConnection(JmsTool.TOPIC, user,
          password, lookup);
      connections.add(tc);

      tc2 = (TopicConnection) tool.getNewConnection(JmsTool.TOPIC, user,
          password, lookup);
      connections.add(tc2);

      TestUtil.logMsg("Setting clientID!");
      tc.setClientID("ctstest");

      TestUtil.logMsg("Getting clientID!");
      String clientid = tc.getClientID();

      if (!clientid.equals("ctstest")) {
        TestUtil.logErr(
            "getClientID() returned " + clientid + ", expected ctstest");
        pass = false;
      } else {
        TestUtil.logMsg("setClientID/getClientID correct");
      }

      TestUtil.logMsg("Resetting clientID! (excpect IllegalStateException)");
      tc.setClientID("changeIt");
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
      tc2.setClientID("ctstest");
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
      throw new Fault("setGetChangeClientIDTopicTest");
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
    TopicConnection tc = null;

    try {
      TestUtil.logMsg("Setup JmsTool for TOPIC");
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);

      TestUtil.logMsg("Create ExceptionListener");
      ExceptionListener foo = new ExceptionListener() {

        public void onException(JMSException e) {
        }
      };

      TestUtil.logMsg("Call setExceptionListener on Connection object");
      tool.getDefaultTopicConnection().setExceptionListener(foo);

      ExceptionListener foo2 = tool.getDefaultTopicConnection()
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

    TopicSession session;

    final DoneLatch monitor;

    public AutoAckMsgListener(DoneLatch latch, TopicSession tSession) {
      this.monitor = latch;
      this.session = tSession;
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
   * @test_Strategy: Send x messages to x Topics from x senders. In a different
   * session using client_acknowledge, create x TopicSubscribers. Send the
   * messages in order 1,2,3...x. Receive them in order x...3,2,1, calling
   * session.recover() after receiving 1 message. All x messages should be
   * received. ("x" is specified by the numMessages parameter in ts.jte file.)
   *
   * Note: default TopicSubscriber can stay open, since testing is done with
   * newly created Destinations
   */
  public void reverseReceiveClientAckTest() throws Fault {
    boolean pass = true;

    try {

      // create Topic Connection
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      Topic t[] = new Topic[numMessages];
      TopicPublisher tPublisher[] = new TopicPublisher[numMessages];
      TopicSubscriber tSubscriber[] = new TopicSubscriber[numMessages];
      Message msg[] = new Message[numMessages];

      // create destinations using default session
      for (int i = 0; i < numMessages; i++) {
        TestUtil.logMsg("Creating Topic " + i + " of " + (numMessages - 1)
            + " (" + numMessages + " total)");
        t[i] = tool.createNewTopic("testT" + i);
      }

      // use default session for sending
      TestUtil.logMsg("Creating " + numMessages + " senders");
      for (int i = 0; i < numMessages; i++) {
        TestUtil.logMsg("sender " + i);
        tPublisher[i] = tool.getDefaultTopicSession().createPublisher(t[i]);
      }

      // create session for receiving
      TestUtil.logMsg("Creating CLIENT_ACKNOWLEDGE session for receiving");
      TopicSession receiveSession = tool.getDefaultTopicConnection()
          .createTopicSession(false, Session.CLIENT_ACKNOWLEDGE);

      // create receivers for receive session
      TestUtil
          .logMsg("Creating " + numMessages + " receivers in receive session");
      for (int i = 0; i < numMessages; i++) {
        TestUtil.logMsg("receiver " + i);
        tSubscriber[i] = receiveSession.createSubscriber(t[i]);
      }

      // start the connection
      tool.getDefaultTopicConnection().start();

      // send messages: 1,2,3,...
      Message tempMsg = null;

      for (int i = 0; i < numMessages; i++) {
        TestUtil.logMsg("Sending message " + i);
        tempMsg = tool.getDefaultTopicSession().createMessage();
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "reverseReceiveClientAckTest");
        tPublisher[i].publish(tempMsg);
      }

      // receive messages: ...,3,2,1
      TestUtil.logMsg(
          "Receive messages 0-" + (numMessages - 1) + " in reverse order");
      for (int i = (numMessages - 1); i >= 0; i--) {
        TestUtil.logMsg("Receive message " + i);
        msg[i] = tSubscriber[i].receive(timeout);
        if (msg[i] == null) {
          TestUtil.logErr("Did not receive message from subscriber[" + i + "]");
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
          "Try receiving message from all subscribers again (should not receive any)");
      for (int i = (numMessages - 1); i >= 0; i--) {
        msg[i] = tSubscriber[i].receive(timeout);
        if (msg[i] != null) {
          TestUtil.logErr(
              "Received message from subscriber[" + i + "], expected none");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("reverseReceiveClientAckTest");
    }
    if (!pass) {
      throw new Fault("reverseReceiveClientAckTest");
    }
  }

  /*
   * @testName: clientAckTopicTest
   *
   * @assertion_ids: JMS:SPEC:131; JMS:JAVADOC:122; JMS:SPEC:91;
   *
   * @test_Strategy: Send three messages to Topic. Receive all three and call
   * acknowledge on msg 2. Send and receive message 4. Recover and attempt to
   * receive msg 4.
   */
  public void clientAckTopicTest() throws Fault {
    boolean pass = true;

    try {
      TextMessage sent1 = null;
      TextMessage sent2 = null;
      TextMessage sent3 = null;
      TextMessage sent4 = null;

      TextMessage rec2 = null;
      TextMessage rec4 = null;

      TopicSession tSess = null;
      TopicSubscriber tSub = null;
      TopicPublisher tPublisher = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicSubscriber().close();
      tool.getDefaultTopicSession().close();

      tSess = tool.getDefaultTopicConnection().createTopicSession(false,
          Session.CLIENT_ACKNOWLEDGE);
      TestUtil.logMsg("Start connection");
      tool.getDefaultTopicConnection().start();

      // create two messages
      sent1 = tSess.createTextMessage();
      sent1.setText("test message 1");
      sent1.setStringProperty("COM_SUN_JMS_TESTNAME", "clientAckTopicTest1");
      sent2 = tSess.createTextMessage();
      sent2.setText("test message 2");
      sent2.setStringProperty("COM_SUN_JMS_TESTNAME", "clientAckTopicTest2");
      sent3 = tSess.createTextMessage();
      sent3.setText("test message 3");
      sent3.setStringProperty("COM_SUN_JMS_TESTNAME", "clientAckTopicTest3");

      sent4 = tSess.createTextMessage();
      sent4.setText("test message 4");
      sent4.setStringProperty("COM_SUN_JMS_TESTNAME", "clientAckTopicTest4");

      // create CLIENT_ACK session and consumer
      tSub = tSess.createSubscriber(tool.getDefaultTopic());
      tPublisher = tSess.createPublisher(tool.getDefaultTopic());

      // send messages
      TestUtil.logTrace("Send three messages");
      tPublisher.publish(sent1);
      tPublisher.publish(sent2);
      tPublisher.publish(sent3);

      // receive messages and acknowledge second
      TestUtil.logTrace("Receive three messages");
      tSub.receive(timeout);
      TestUtil.logTrace("Received the first message");
      rec2 = (TextMessage) tSub.receive(timeout);
      TestUtil.logTrace("Received the second message");
      tSub.receive(timeout);
      TestUtil.logTrace("Received the third message");

      // acknowledging msg 2 of the 3 received messages should acknowledge all 3
      // messages.
      TestUtil.logTrace("Acknowledging the second message");
      rec2.acknowledge();

      // send and receive message 4
      TestUtil.logTrace("Send the fourth message");
      tPublisher.publish(sent4);
      TestUtil.logTrace("Receive the fourth message");
      rec4 = (TextMessage) tSub.receive(timeout);
      TestUtil.logTrace("Received the fourth message");

      // recover and attempt to receive fourth message
      TestUtil.logTrace("Call session.recover()");
      tSess.recover();
      TestUtil.logTrace(
          "Attempt to receive unacked message - the fourth message again");
      rec4 = (TextMessage) tSub.receive(timeout);
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
        throw new Fault("clientAckTopicTest Failed!!");

    } catch (Exception e) {
      TestUtil.logErr("Error: ", e);
      throw new Fault("clientAckTopicTest", e);
    }
  }

  /*
   * @testName: nonAckMsgsRedeliveredTopicTest
   *
   * @assertion_ids: JMS:SPEC:145; JMS:JAVADOC:122; JMS:SPEC:91;
   *
   * @test_Strategy: Send messages to a topic that has a single TopicSubscriber
   * in a CLIENT_ACKNOWLEDGE session. Receive all the messages without
   * acknowledging the messages and call session recover. Verify that all the
   * messages may still be received from the topic.
   */
  public void nonAckMsgsRedeliveredTopicTest() throws Fault {
    boolean pass = true;

    try {
      TextMessage tempMsg;

      // create default TopicSession for sending messages
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      TestUtil.logTrace(
          "Close TopicSubscriber in default session -- only want one for Topic");
      tool.getDefaultTopicSubscriber().close();
      tool.getDefaultTopicSession().close();

      // create client_ack session for topic
      TopicSession tSession = tool.getDefaultTopicConnection()
          .createTopicSession(false, Session.CLIENT_ACKNOWLEDGE);
      TopicSubscriber tSubscriber = tSession
          .createSubscriber(tool.getDefaultTopic());
      TopicPublisher tPublisher = tSession
          .createPublisher(tool.getDefaultTopic());

      // start connection
      tool.getDefaultTopicConnection().start();

      // send messages
      for (int i = 0; i < numMessages; i++) {
        tempMsg = tSession.createTextMessage();
        tempMsg.setText("test message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "nonAckMsgsRedeliveredTopicTest" + i);
        tPublisher.publish(tempMsg);
        TestUtil.logTrace("sent message " + i);
      }

      // receive messages but do not acknowledge them
      for (int i = 0; i < numMessages; i++) {
        tempMsg = (TextMessage) tSubscriber.receive(timeout);
        if (tempMsg == null) {
          pass = false;
          TestUtil.logErr("Did not receive message " + i);
        } else {
          TestUtil.logTrace("received message " + i);
        }
      }
      TestUtil.logTrace("Call session recover()");
      tSession.recover();

      // receive messages again but this time acknowlege them
      for (int i = 0; i < numMessages; i++) {
        tempMsg = (TextMessage) tSubscriber.receive(timeout);
        if (tempMsg != null)
          tempMsg.acknowledge();
        if (tempMsg == null) {
          pass = false;
          TestUtil.logErr("Did not receive message " + i);
        } else {
          TestUtil.logTrace(
              "received message \"" + tempMsg.getText() + "\" second time");
        }
      }
      // try receiving one more message
      tempMsg = (TextMessage) tSubscriber.receive(timeout);
      if (tempMsg != null) {
        TestUtil.logErr("Should not have received a message");
        pass = false;
      }
      tPublisher.close();
      tSubscriber.close();
      tSession.close();

      if (!pass)
        throw new Fault("nonAckMsgsRedeliveredTopicTest failed!!!");
    } catch (Exception e) {
      TestUtil.logErr("nonAckMsgsRedeliveredTopicTest failed: ", e);
      throw new Fault("nonAckMsgsRedeliveredTopicTest failed", e);
    }
  }

  /*
   * @testName: topicRequestorSimpleSendAndRecvTest
   * 
   * @assertion_ids: JMS:JAVADOC:5; JMS:JAVADOC:6; JMS:JAVADOC:8; JMS:SPEC:170;
   * 
   * @test_Strategy: Send and receive simple JMS message using TopicRequestor
   * helper class. Tests the following API's:
   * 
   * TopicRequestor(TopicSession, Topic) TopicRequestor.request(Message)
   * TopicRequestor.close()
   */
  public void topicRequestorSimpleSendAndRecvTest() throws Fault {
    boolean pass = true;
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      TopicRequestor treq = null;

      // set up test tool for Topic
      TestUtil.logMsg("Set up JmsTool for TOPIC");
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logMsg("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText("This is the request message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "topicRequestorSimpleSendAndRecvTest");

      // set up MessageListener
      TestUtil.logMsg("Set up MessageListener");
      tool.getDefaultTopicSubscriber().setMessageListener(
          new RequestorMsgListener(tool.getDefaultTopicSession()));

      // set up TopicRequestor
      TestUtil.logMsg(
          "Set up TopicRequestor for request/response message exchange");
      TopicSession newts = tool.getDefaultTopicConnection()
          .createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
      treq = new TopicRequestor(newts, tool.getDefaultTopic());
      TestUtil.logMsg(
          "Send message request and receive message response using TopicRequestor");
      TestUtil
          .logMsg("Message request text: \"" + messageSent.getText() + "\"");
      messageReceived = (TextMessage) treq.request(messageSent);

      // Check to see if correct message received
      TestUtil.logMsg(
          "Message response text: \"" + messageReceived.getText() + "\"");
      if (messageReceived.getText().equals("This is the response message")) {
        TestUtil.logMsg("Received correct response message");
      } else {
        TestUtil.logErr("Received incorrect response message");
        pass = false;
      }

      // Close the TopicRequestor
      TestUtil.logMsg("Close TopicRequestor");
      treq.close();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("topicRequestorSimpleSendAndRecvTest");
    }
    if (!pass) {
      throw new Fault("topicRequestorSimpleSendAndRecvTest failed");
    }
  }

  /*
   * @testName: topicRequestorExceptionTests
   * 
   * @assertion_ids: JMS:JAVADOC:830; JMS:JAVADOC:831; JMS:JAVADOC:9;
   * 
   * @test_Strategy: Test negative exception cases for TopicRequestor API's.
   * Tests the following exceptions: InvalidDestinationException, JMSException.
   */
  public void topicRequestorExceptionTests() throws Fault {
    boolean pass = true;
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      Topic invalidDest = null;
      TopicRequestor treq = null;

      // set up test tool for Topic
      TestUtil.logMsg("Set up JmsTool for TOPIC");
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();

      // set up MessageListener
      TestUtil.logMsg("Set up MessageListener");
      tool.getDefaultTopicSubscriber().setMessageListener(
          new RequestorMsgListener(tool.getDefaultTopicSession()));

      TopicSession newts = tool.getDefaultTopicConnection()
          .createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

      // Try and set up TopicRequestor with InvalidDestination
      try {
        TestUtil.logMsg("Set up TopicRequestor with InvalidDestination");
        treq = new TopicRequestor(newts, invalidDest);
        TestUtil.logErr("Didn't throw InvalidDestinationException");
        pass = false;
      } catch (InvalidDestinationException e) {
        TestUtil.logMsg("Caught expected InvalidDestinationException");
      }

      // Try and set up TopicRequestor with closed TopicSession
      try {
        TestUtil.logMsg("Set up TopicRequestor with a closed TopicSession");
        newts.close();
        treq = new TopicRequestor(newts, tool.getDefaultTopic());
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      }
      tool.closeAllConnections(connections);

      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logMsg("Set up TopicRequestor");
      treq = new TopicRequestor(tool.getDefaultTopicSession(),
          tool.getDefaultTopic());

      TestUtil.logMsg("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "topicRequestorExceptionTests");

      // Close the TopicRequestor
      TestUtil.logMsg("Close TopicRequestor");
      treq.close();

      TestUtil.logMsg(
          "Try a request/response message exchange on a closed TopicRequestor");
      try {
        messageReceived = (TextMessage) treq.request(messageSent);
        if (messageReceived != null)
          TestUtil.logMsg("messageReceived=" + messageReceived.getText());
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("topicRequestorExceptionTests");
    }
    if (!pass) {
      throw new Fault("topicRequestorExceptionTests failed");
    }
  }

  private static class RequestorMsgListener implements MessageListener {
    TopicSession session = null;

    boolean pass = false;

    public RequestorMsgListener(TopicSession session) {
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
        Topic replyT = (Topic) message.getJMSReplyTo();
        TopicPublisher publisher = session.createPublisher(replyT);
        TextMessage responseMsg = session.createTextMessage();
        responseMsg.setText("This is the response message");
        responseMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "RequestorMsgListenerResponseMsg");
        TestUtil.logMsg("Sending back response message");
        publisher.publish(responseMsg);
      } catch (JMSException e) {
        TestUtil.printStackTrace(e);
        TestUtil.logMsg("Exception caught in RequestorMsgListener:\n" + e);
      }
    }
  }
}
