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
package com.sun.ts.tests.jms.core.topictests;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.util.*;
import java.io.*;
import com.sun.javatest.Status;
import javax.jms.*;

public class TopicTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.topictests.TopicTests";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // Harness req's
  private Properties props = null;

  // JMS object
  private transient JmsTool tool = null;

  // properties read from ts.jte file
  long timeout;

  private String jmsUser;

  private String jmsPassword;

  private String mode;

  public static final int TOPIC = 1;

  ArrayList connections = null;

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

  /* Utility methods for tests */

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

  /*
   * Checks passed flag for negative tests and throws exception back to caller
   * which passes ot to harness.
   * 
   * @param boolean Pass/Fail flag
   */
  private void checkExceptionPass(boolean passed) throws Exception {
    if (passed == false) {
      TestUtil.logMsg("Didn't get expected exception");
      throw new Exception("Didn't catch expected exception");
    }
  }

  /* Test setup: */

  /*
   * setup() is called before each test
   * 
   * @class.setup_props: jms_timeout;user; password; platform.mode;
   * 
   * @exception Fault
   */
  public void setup(String[] args, Properties p) throws Fault {
    try {
      TestUtil.logTrace("In setup");
      // get props
      jmsUser = p.getProperty("user");
      jmsPassword = p.getProperty("password");
      mode = p.getProperty("platform.mode");
      timeout = Long.parseLong(p.getProperty("jms_timeout"));
      if (timeout < 1) {
        throw new Exception("'timeout' (milliseconds) in ts.jte must be > 0");
      }
      connections = new ArrayList(10);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed!", e);
    }
  }

  /* cleanup */

  /*
   * cleanup() is called after each test
   * 
   * @exception Fault
   */
  public void cleanup() throws Fault {
    try {
      TestUtil.logMsg("Cleanup: Closing Topic Connections");
      tool.closeAllConnections(connections);
    } catch (Exception e) {
      TestUtil.logErr("An error occurred while cleaning", e);
      throw new Fault("Cleanup failed!", e);
    }
  }

  /* Tests */

  /*
   * @testName: simpleSendReceiveTopicTest
   * 
   * @assertion_ids: JMS:SPEC:158; JMS:SPEC:242; JMS:JAVADOC:122;
   * 
   * @test_Strategy: Send and receive single message. Verify message receipt.
   */
  public void simpleSendReceiveTopicTest() throws Fault {
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logMsg("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "simpleSendReceiveTopicTest");
      TestUtil.logMsg("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logMsg("Receiving message");
      messageReceived = (TextMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("didn't get any message");
      }
      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        TestUtil.logMsg("Message text: \"" + messageReceived.getText() + "\"");
        TestUtil.logMsg("Received message");
      } else {
        throw new Exception("didn't get the right message");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("simpleSendReceiveTopicTest");
    }
  }

  /*
   * @testName: inactiveNonDurableSubscriberTopicRecTest
   * 
   * @assertion_ids: JMS:SPEC:153; JMS:SPEC:154; JMS:JAVADOC:122; JMS:SPEC:152;
   * 
   * @test_Strategy: Send and receive a message to/from a topic. Inactivate the
   * subscriber, publish another message. Verify that when the subscriber is
   * activated again that there is no messages to to receive.
   */
  public void inactiveNonDurableSubscriberTopicRecTest() throws Fault {
    TopicSubscriber tSub = null;
    TopicSession tSession = null;
    TopicConnection newTConn = null;
    String lookup = "MyTopicConnectionFactory";

    try {
      TextMessage messageSent = null;
      TextMessage messageSent2 = null;
      TextMessage messageReceived = null;

      // set up test tool for Topic
      TestUtil.logTrace("Set up JmsTool for Topic");
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);

      // get default Subscriber
      TestUtil.logTrace("Getting default subscriber");
      tSub = tool.getDefaultTopicSubscriber();

      TestUtil.logTrace("Start Connection");
      tool.getDefaultTopicConnection().start();

      // send message
      TestUtil.logTrace("Create and publish first message");
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "inactiveNonDurableSubscriberTopicRecTest");
      tool.getDefaultTopicPublisher().publish(messageSent);

      TestUtil.logTrace("Receive first message");
      messageReceived = (TextMessage) tSub.receive(timeout);

      // Check to see if first message received
      TestUtil.logTrace("Verify that first message is received");
      if (messageReceived.getText().equals(messageSent.getText())) {
        TestUtil
            .logTrace("Message text: \"" + messageReceived.getText() + "\"");
        TestUtil.logMsg("Received correct message");
      } else {
        throw new Exception("didn't get the right message");
      }

      // make the subscriber inactive
      TestUtil.logTrace("Close default subscriber");
      tSub.close();

      // publish another second message
      TestUtil.logTrace("Create and publish second message");
      messageSent2 = tool.getDefaultTopicSession().createTextMessage();
      messageSent2.setText("test that messages are nondurable");
      messageSent2.setStringProperty("COM_SUN_JMS_TESTNAME",
          "inactiveNonDurableSubscriberTopicRecTest");
      tool.getDefaultTopicPublisher().publish(messageSent2);
      tool.getDefaultTopicConnection().close();

      TestUtil.logTrace("Recreate default subscriber");
      newTConn = (TopicConnection) tool.getNewConnection(JmsTool.TOPIC, jmsUser,
          jmsPassword, lookup);
      connections.add(newTConn);
      tSession = newTConn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
      tSub = tSession.createSubscriber(tool.getDefaultTopic());
      newTConn.start();
      TestUtil.logTrace(
          "Try to receive second message (should not receive a message)");
      messageReceived = (TextMessage) tSub.receive(timeout);

      // Check to see that no message is available
      if (messageReceived != null) {
        throw new Exception("Received second message. (Expected NO messages)");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("inactiveNonDurableSubscriberTopicRecTest");
    }
  }

  /*
   * @testName: noLocalDeliveryTopicTest
   * 
   * @assertion_ids: JMS:SPEC:161;
   * 
   * @test_Strategy: Create connection with normal subscriber and no_local
   * subscriber. Send x messages to topic and receive them with regular
   * subscriber. Create second connection with subscriber. Send message from
   * first connection and receive it with second. Send message from second
   * connection and attempt receive with no_local subscriber. Should only get
   * message from second connection.
   */
  public void noLocalDeliveryTopicTest() throws Fault {
    String lookup = "MyTopicConnectionFactory";

    try {
      int num = 10;
      TopicSubscriber tSubNoLocal = null;
      TopicConnection newConn = null;
      TopicSession newSess = null;
      TopicPublisher newPub = null;
      TopicSubscriber newSub = null;
      Message messageSent = null;
      Message messageReceived = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, mode);
      tSubNoLocal = tool.getDefaultTopicSession()
          .createSubscriber(tool.getDefaultTopic(), "", true);
      tool.getDefaultTopicConnection().start();

      // publish messages
      TestUtil.logTrace("Sending " + num + " messages to topic");
      messageSent = tool.getDefaultTopicSession().createMessage();
      messageSent.setBooleanProperty("lastMessage", false);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "noLocalDeliveryTopicTest");
      for (int i = 0; i < num; i++) {
        tool.getDefaultTopicPublisher().publish(messageSent);
      }

      // receive
      TestUtil.logMsg("Attempting to receive messages");
      for (int i = 0; i < num; i++) {
        messageReceived = tool.getDefaultTopicSubscriber().receive(timeout);
        if (messageReceived == null) {
          throw new Fault("Should have received message");
        }
      }

      // create new connection
      TestUtil.logMsg("Creating new connection");
      newConn = (TopicConnection) tool.getNewConnection(JmsTool.TOPIC, jmsUser,
          jmsPassword, lookup);
      connections.add(newConn);

      newSess = newConn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
      newSub = newSess.createSubscriber(tool.getDefaultTopic());
      newPub = newSess.createPublisher(tool.getDefaultTopic());
      newConn.start();

      // send another message and receive with second connection
      tool.getDefaultTopicPublisher().publish(messageSent);
      messageReceived = newSub.receive(timeout);
      if (messageReceived == null) {
        throw new Fault("new connection should have received message");
      }

      // send message from new connection
      TestUtil.logTrace("New connection sending message");
      messageSent.setBooleanProperty("lastMessage", true);
      newPub.publish(messageSent);
      TestUtil.logTrace("Closing new connection");
      newConn.close();

      // receive message and check
      TestUtil.logTrace("Try to receive only message from new connection");
      messageReceived = tSubNoLocal.receive(timeout);
      if (messageReceived == null) {
        throw new Fault("No_local subscriber did not receive any message");
      } else if (messageReceived.getBooleanProperty("lastMessage") == false) {
        throw new Fault("No_local subscriber received local message");
      }
      TestUtil.logTrace("Received correct message");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("noLocalDeliveryTopicTest");
    }
  }

  /*
   * @testName: simpleDurableSubscriberTopicTest
   * 
   * @assertion_ids: JMS:SPEC:161; JMS:JAVADOC:87; JMS:JAVADOC:122;
   * 
   * @test_Strategy: Send single message to a topic and verify receipt of it
   * with a durable subscriber.
   * 
   */
  public void simpleDurableSubscriberTopicTest() throws Fault {
    TopicSubscriber durableTS = null;
    String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.DURABLE_TOPIC, jmsUser, jmsPassword, lookup,
          mode);

      // close default Subscriber and create DurableSubscriber
      TestUtil.logMsg("Create DurableSubscriber");
      tool.getDefaultTopicSubscriber().close();
      durableTS = tool.getDefaultTopicSession().createDurableSubscriber(
          tool.getDefaultTopic(), "myDurableTopicSubscriber");

      // start Connection and send/receive message
      tool.getDefaultTopicConnection().start();
      TestUtil.logMsg("Creating and sending 1 message");
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "simpleDurableSubscriberTopicTest");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logMsg("Receiving message");
      messageReceived = (TextMessage) durableTS.receive(timeout);

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        TestUtil.logMsg("Message text: \"" + messageReceived.getText() + "\"");
        TestUtil.logMsg("Received correct message");
      } else {
        throw new Exception("didn't get the right message");
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("simpleDurableSubscriberTopicTest");
    } finally {
      cleanupSubscription(durableTS, tool.getDefaultTopicSession(),
          "myDurableTopicSubscriber");
    }
  }

  /*
   * @testName: temporaryTopicConnectionClosesTest
   * 
   * @assertion_ids: JMS:SPEC:155; JMS:JAVADOC:93;
   * 
   * @test_Strategy: Create temporary topic and then close the connection.
   * Verify that the temporary topic closes by trying to send a message to it.
   * The test also sends a blank message to the temporary topic to verify that
   * it is working.
   */
  public void temporaryTopicConnectionClosesTest() throws Fault {
    boolean passed = false;
    String lookup = "MyTopicConnectionFactory";

    try {
      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, mode);
      tool.getDefaultTopicConnection().start();

      // create the TemporaryTopic
      TestUtil.logTrace("Creating TemporaryTopic");
      TemporaryTopic tempT = tool.getDefaultTopicSession()
          .createTemporaryTopic();

      // open a new connection, create Session and Sender
      TestUtil.logTrace("Creating new Connection");
      TopicConnection newTConn = (TopicConnection) tool
          .getNewConnection(JmsTool.TOPIC, jmsUser, jmsPassword, lookup);
      connections.add(newTConn);

      TestUtil.logTrace("Create new Session");
      TopicSession newTSess = newTConn.createTopicSession(false,
          Session.AUTO_ACKNOWLEDGE);

      TestUtil.logTrace("Create new sender for TemporaryTopic");
      TopicPublisher newTPublisher = newTSess.createPublisher(tempT);

      // send message to verify TemporaryTopic exists so far
      TestUtil.logTrace("Send message to TemporaryTopic");
      TextMessage tMsg = newTSess.createTextMessage();

      tMsg.setText("test message");
      tMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "temporaryTopicConnectionClosesTest");
      TestUtil.logTrace("TextMessage created. Now publishing");
      newTPublisher.publish(tMsg);

      // close the connection
      TestUtil.logTrace("Close original Connection");
      tool.getDefaultTopicConnection().close();

      // send message to verify TemporaryTopic no longer exists
      // TestUtil.logTrace("Send second message to TemporaryTopic. Should
      // fail.");
      // try {
      // Message tempM = newTSess.createMessage();

      // tempM.setStringProperty("COM_SUN_JMS_TESTNAME",
      // "temporaryTopicConnectionClosesTest");
      // newTPublisher.publish(tempM);
      // } catch (JMSException e) {
      // TestUtil.logMsg("Received expected JMSException");
      // TestUtil.logErr("Exception thrown: ", e);
      // passed = true;
      // }

      // close new connection
      TestUtil.logTrace("Close new TopicConnection");
      newTConn.close();

      // throw exception if test failed
      passed = true;
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("temporaryTopicConnectionClosesTest");
    }
  }

  /*
   * @testName: temporaryTopicNotConsumableTest
   * 
   * @assertion_ids: JMS:SPEC:117; JMS:SPEC:243; JMS:JAVADOC:93;
   * 
   * @test_Strategy: Create temporary topic and a separate TopicSession. Try to
   * create a receiver for the temporary topic from the new session, which
   * should throw a JMSException. Also sends a blank message to verify that the
   * temporary topic is working.
   */
  public void temporaryTopicNotConsumableTest() throws Fault {
    boolean passed = false;
    String lookup = "MyTopicConnectionFactory";

    try {

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, mode);
      tool.getDefaultTopicConnection().start();

      // create the TemporaryTopic
      TestUtil.logMsg("Creating TemporaryTopic");
      TemporaryTopic tempT = tool.getDefaultTopicSession()
          .createTemporaryTopic();

      // open a new connection, create Session and Sender
      TestUtil.logMsg("Creating new Connection");
      TopicConnection newTConn = (TopicConnection) tool
          .getNewConnection(JmsTool.TOPIC, jmsUser, jmsPassword, lookup);
      connections.add(newTConn);
      TestUtil.logMsg("Create new Session");
      TopicSession newTSess = newTConn.createTopicSession(false,
          Session.AUTO_ACKNOWLEDGE);

      TestUtil.logMsg("Create new publisher for TemporaryTopic");
      TopicPublisher newTPublisher = newTSess.createPublisher(tempT);

      // send message to verify TemporaryTopic
      TestUtil.logMsg("Send message to TemporaryTopic");
      TextMessage tMsg = newTSess.createTextMessage();

      tMsg.setText("test message");
      tMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "temporaryTopicNotConsumableTest");
      TestUtil.logMsg("TextMessage created. Now publishing");
      newTPublisher.publish(tMsg);

      // try to create receiver for the TemporaryTopic
      TestUtil.logMsg(
          "Attempt to create subscriber for TemporaryTopic from another Session");
      try {
        TopicSubscriber newTSubscriber = newTSess.createSubscriber(tempT);
        if (newTSubscriber != null)
          TestUtil.logTrace("newTSubscriber=" + newTSubscriber);
      } catch (JMSException e) {
        TestUtil.logMsg("Received expected JMSException -- GOOD");
        TestUtil.logMsg("Received Exception:", e);
        passed = true;
      }

      // close new connection
      TestUtil.logTrace("Close new TopicConnection");
      newTConn.close();

      // throw exception if test failed
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("temporaryTopicNotConsumableTest");
    }
  }

  /*
   * @testName: msgSelectorMsgHeaderTopicTest
   * 
   * @assertion_ids: JMS:SPEC:38; JMS:SPEC:160; JMS:SPEC:246.9;
   * 
   * @test_Strategy: Create subscriber with a message selector that uses message
   * header JMSType. Send two messages, one that has the matching header value
   * and one that doesn't, and try to receive message. Should only receive one
   * matching message.
   */
  public void msgSelectorMsgHeaderTopicTest() throws Fault {
    try {

      // create Topic Connection
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, mode);

      // create subscriber with message selector
      TestUtil.logMsg("Creating subscriber with message selector");
      TopicSubscriber tSelectiveSubscriber = tool.getDefaultTopicSession()
          .createSubscriber(tool.getDefaultTopic(), "JMSType = 'test_message'",
              false);

      // start connection
      tool.getDefaultTopicConnection().start();

      // send messages
      Message m = tool.getDefaultTopicSession().createMessage();
      m.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgSelectorMsgHeaderTopicTest");

      TestUtil.logTrace("Sending message not matching selector");
      m.setJMSType("foo");
      m.setBooleanProperty("lastMessage", false);
      tool.getDefaultTopicPublisher().publish(m);

      TestUtil.logTrace("Sending message that matches selector");
      m.setJMSType("test_message");
      m.setBooleanProperty("lastMessage", true);
      tool.getDefaultTopicPublisher().publish(m);

      // attempt to receive correct message
      TestUtil.logMsg("Attempt to receive 'good' message");
      Message msg1 = tSelectiveSubscriber.receive(timeout);
      if (msg1 == null) {
        throw new Exception("Did not receive expected message");
      } else if (msg1.getBooleanProperty("lastMessage") == true) {
        TestUtil.logMsg("Received correct message -- GOOD");
      } else {
        TestUtil.logMsg("Received message not matching header");
        throw new Fault("Received incorrect message");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("msgSelectorMsgHeaderTopicTest");
    }
  }

  /*
   * @testName: inactiveDurableSubscriberTopicRecTest
   * 
   * @assertion_ids: JMS:SPEC:153; JMS:SPEC:154; JMS:JAVADOC:122;
   * 
   * @test_Strategy: Send and receive a message from a topic. Inactivate the
   * subscriber, publish another message. Verify that when the subscriber is
   * activated the message is received.
   */
  public void inactiveDurableSubscriberTopicRecTest() throws Fault {
    TopicSubscriber durableTS = null;
    TopicSession tSession = null;
    TopicConnection newTConn = null;
    String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

    try {
      TextMessage messageSent = null;
      TextMessage messageSent1 = null;
      TextMessage messageReceived = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.DURABLE_TOPIC, jmsUser, jmsPassword, lookup,
          mode);

      // close default Subscriber and create DurableSubscriber
      TestUtil.logTrace("Create DurableSubscriber");
      tool.getDefaultTopicSubscriber().close();
      durableTS = tool.getDefaultTopicSession().createDurableSubscriber(
          tool.getDefaultTopic(), "inactiveDurableSubscriberTopicRecTest");

      TestUtil.logTrace("Start Connection");
      tool.getDefaultTopicConnection().start();

      // send message
      TestUtil.logTrace("Creating and sending 1 message");
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "inactiveDurableSubscriberTopicRecTest");
      tool.getDefaultTopicPublisher().publish(messageSent);

      TestUtil.logTrace("Receiving message");
      messageReceived = (TextMessage) durableTS.receive(timeout);

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        TestUtil
            .logTrace("Message text: \"" + messageReceived.getText() + "\"");
        TestUtil.logMsg("Received correct message");
      } else {
        throw new Fault("didn't get the right message");
      }

      // make the durable subscriber inactive
      durableTS.close();

      // publish more messages
      messageSent1 = tool.getDefaultTopicSession().createTextMessage();
      messageSent1.setText("test that messages are durable");
      messageSent1.setStringProperty("COM_SUN_JMS_TESTNAME",
          "inactiveDurableSubscriberTopicRecTest");
      tool.getDefaultTopicPublisher().publish(messageSent1);
      tool.getDefaultTopicConnection().close();

      newTConn = (TopicConnection) tool.getNewConnection(JmsTool.DURABLE_TOPIC,
          jmsUser, jmsPassword, lookup);
      connections.add(newTConn);
      tSession = newTConn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

      durableTS = tSession.createDurableSubscriber(tool.getDefaultTopic(),
          "inactiveDurableSubscriberTopicRecTest");
      newTConn.start();
      TestUtil.logTrace("Receiving message");
      messageReceived = (TextMessage) durableTS.receive(timeout);

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent1.getText())) {
        TestUtil
            .logTrace("Message text: \"" + messageReceived.getText() + "\"");
        TestUtil.logTrace("Received correct message");
      } else {
        throw new Fault("Received incorrect message.");
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("inactiveDurableSubscriberTopicRecTest");
    } finally {
      cleanupSubscription(durableTS, tSession,
          "inactiveDurableSubscriberTopicRecTest");
    }
  }

  /*
   * @testName: durableSubscriberTopicNoLocalTest
   * 
   * @assertion_ids: JMS:SPEC:161; JMS:SPEC:126;
   * 
   * @test_Strategy: Create connection with normal subscriber and no_local
   * durable subscriber. Send x messages to topic and receive them with regular
   * subscriber. Create second connection with subscriber. Send message from
   * first connection and receive it with second. Send message from second
   * connection and attempt receive with no_local subscriber. Should only get
   * message from second connection.
   * 
   */
  public void durableSubscriberTopicNoLocalTest() throws Fault {
    TopicSubscriber tSubNoLocal = null;
    String subscriptionName = "DurableSubscriberTopicNoLocalTestSubscription";
    String lookup = "DURABLE_SUB_CONNECTION_FACTORY";
    String lookup2 = "MyTopicConnectionFactory";

    try {
      int num = 10;
      TopicConnection newConn = null;
      TopicSession newSess = null;
      TopicPublisher newPub = null;
      TopicSubscriber newSub = null;
      Message messageSent = null;
      Message messageReceived = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.DURABLE_TOPIC, jmsUser, jmsPassword, lookup,
          mode);

      // create DurableSubscriber
      TestUtil.logTrace("Create DurableSubscriber");
      tSubNoLocal = tool.getDefaultTopicSession().createDurableSubscriber(
          tool.getDefaultTopic(), subscriptionName, "", true);
      tool.getDefaultTopicConnection().start();

      // publish messages
      TestUtil.logTrace("Sending " + num + " messages to topic");
      messageSent = tool.getDefaultTopicSession().createMessage();
      messageSent.setBooleanProperty("lastMessage", false);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "noLocalDeliveryTopicTest");
      TopicPublisher defaultPub = tool.getDefaultTopicPublisher();
      for (int i = 0; i < num; i++) {
        defaultPub.publish(messageSent);
      }

      // receive
      TestUtil.logMsg("Attempting to receive messages");
      TopicSubscriber defaultSub = tool.getDefaultTopicSubscriber();
      for (int i = 0; i < num; i++) {
        messageReceived = defaultSub.receive(timeout);
        if (messageReceived == null) {
          throw new Fault("Should have received message");
        }
      }

      // create new connection
      TestUtil.logMsg("Creating new connection");
      newConn = (TopicConnection) tool.getNewConnection(JmsTool.TOPIC, jmsUser,
          jmsPassword, lookup2);
      connections.add(newConn);
      newSess = newConn.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
      newSub = newSess.createSubscriber(tool.getDefaultTopic());
      newPub = newSess.createPublisher(tool.getDefaultTopic());
      newConn.start();

      // send another message and receive with second connection
      defaultPub.publish(messageSent);
      messageReceived = newSub.receive(timeout);
      if (messageReceived == null) {
        throw new Fault("new connection should have received message");
      }

      // send message from new connection
      TestUtil.logTrace("New connection sending message");
      messageSent.setBooleanProperty("lastMessage", true);
      newPub.publish(messageSent);
      TestUtil.logTrace("Closing new connection");
      newConn.close();

      // receive message
      TestUtil.logTrace("Try to receive only message from default connection");
      messageReceived = tSubNoLocal.receive(timeout);

      // check message
      if (messageReceived == null) {
        throw new Fault("No_local subscriber did not receive any message");
      } else if (messageReceived.getBooleanProperty("lastMessage") == false) {
        throw new Fault("No_local subscriber received local message");
      }
      TestUtil.logTrace("Received correct message");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("durableSubscriberTopicNoLocalTest");
    } finally {
      cleanupSubscription(tSubNoLocal, tool.getDefaultTopicSession(),
          subscriptionName);
    }
  }

  /*
   * @testName: durableSubscriberTopicNoLocalTest2
   * 
   * @assertion_ids: JMS:SPEC:161; JMS:SPEC:164; JMS:SPEC:165; JMS:JAVADOC:256;
   * JMS:JAVADOC:99; JMS:JAVADOC:334;
   * 
   * 
   * @test_Strategy: 1) Create topic connection with normal subscriber and
   * (no_local=true) durable subscriber. 2) Publish x messages to topic and
   * receive them with normal subscriber. 3) Try and receive messages with
   * (no_local=true) durable subscriber and verify that you cannot receive them.
   * 4) Publish x more messages to topic. 4) Close the (no_local=true) durable
   * subscriber. 5) Create a new (no_local=false) durable subscriber with the
   * same subscription name and same topic as (no_local=true) durable
   * subscriber. 6) Try and receive messages with (no_local=false) durable
   * subscriber. Verify that you cannot receive any messages. Recreating a
   * durable subscriber with a change to (no_local setting) causes previous
   * durable subscription to become invalid so all the old messages are deleted
   * and you start anew with a clean slate.
   *
   * A client can change an existing durable subscription by creating a durable
   * TopicSubscriber with the same name and topic but different (no_local
   * setting). Changing a durable subscriber is equivalent to unsubscribing
   * (deleting) the old one and creating a new one.
   *
   * So if a client subsequently changes the no_local setting, all the existing
   * messages stored in the durable subscription become invalid since they are
   * inconsistent with the new no_local setting. The only safe thing to do is to
   * delete all the old messages and start anew.
   */
  public void durableSubscriberTopicNoLocalTest2() throws Fault {
    TopicSubscriber tSubNoLocal = null;
    String subscriptionName = "DurableSubscriberTopicNoLocalTest2Subscription";
    String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

    try {
      int num = 10;
      Message messageSent = null;
      Message messageReceived = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.DURABLE_TOPIC, jmsUser, jmsPassword, lookup,
          mode);

      // create DurableSubscriber with no_local=true
      TestUtil.logTrace("Create DurableSubscriber with no_local=true");
      tSubNoLocal = tool.getDefaultTopicSession().createDurableSubscriber(
          tool.getDefaultTopic(), subscriptionName, "", true);
      tool.getDefaultTopicConnection().start();

      // publish messages from default publisher
      TestUtil.logTrace("Sending " + num + " messages to topic");
      messageSent = tool.getDefaultTopicSession().createMessage();
      messageSent.setBooleanProperty("lastMessage", false);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "durableSubscriberTopicNoLocalTest2");
      TopicPublisher defaultPub = tool.getDefaultTopicPublisher();
      for (int i = 0; i < num; i++) {
        defaultPub.publish(messageSent);
      }

      // receive with default normal subscriber
      TestUtil.logMsg("Attempting to receive messages from normal subscriber");
      TopicSubscriber defaultSub = tool.getDefaultTopicSubscriber();
      for (int i = 0; i < num; i++) {
        messageReceived = defaultSub.receive(timeout);
        if (messageReceived == null) {
          throw new Fault("Should have received message");
        } else if (messageReceived.getBooleanProperty("lastMessage") == false) {
          TestUtil.logTrace("Received correct message lastMessage=false");
        } else {
          throw new Fault("Received incorrect message lastMessage=true");
        }
      }

      // try and receive with (no_local=true) subscriber (should not receive any
      // messages)
      TestUtil.logMsg(
          "Attempting to receive messages from (no_local=true) subscriber");
      messageReceived = tSubNoLocal.receive(timeout);
      if (messageReceived == null) {
        TestUtil.logTrace("Did not receive message (correct)");
      } else {
        throw new Fault("Received unexpected message (incorrect)");
      }

      // publish more messages using default topic publisher
      TestUtil.logTrace("Sending " + num + " messages to topic");
      for (int i = 0; i < num; i++) {
        defaultPub.publish(messageSent);
      }

      // need to inactivate durable subscriber before creating new durable
      // subscriber
      TestUtil.logTrace("Close DurableSubscriber with no_local=true");
      tSubNoLocal.close();

      // recreate DurableSubscriber with no_local=false
      TestUtil.logTrace("Create DurableSubscriber with no_local=false");
      tSubNoLocal = tool.getDefaultTopicSession().createDurableSubscriber(
          tool.getDefaultTopic(), subscriptionName, "", false);

      // try and receive a message from this new durable subscriber with
      // (no_local=false)
      // should not receive any messages because creating a new
      // DurableSubscriber with a
      // different (no_local=false) setting will delete the previous
      // subscription and any
      // messages that were queued
      messageReceived = tSubNoLocal.receive(timeout);
      if (messageReceived == null) {
        TestUtil.logMsg(
            "No_local=false subscriber did not receive any message (expected)");
      } else {
        throw new Fault(
            "No_local=false subscriber received message (unexpected)");
      }

      // publish more messages with default publisher
      TestUtil.logTrace("Sending " + num + " messages to topic");
      messageSent = tool.getDefaultTopicSession().createMessage();
      messageSent.setBooleanProperty("lastMessage", false);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "durableSubscriberTopicNoLocalTest2");
      for (int i = 0; i < num; i++) {
        defaultPub.publish(messageSent);
      }

      // receive messages with (no_local=false) subscriber (should receive all
      // messages)
      TestUtil.logMsg(
          "Attempting to receive messages from (no_local=false) subscriber");
      for (int i = 0; i < num; i++) {
        messageReceived = tSubNoLocal.receive(timeout);
        if (messageReceived == null) {
          throw new Fault("Should have received message");
        } else if (messageReceived.getBooleanProperty("lastMessage") == false) {
          TestUtil.logTrace("Received correct message lastMessage=false");
        } else {
          throw new Fault("Received incorrect message lastMessage=true");
        }
      }

      // try and receive one more message (there should be none at this point)
      messageReceived = tSubNoLocal.receive(timeout);
      if (messageReceived != null) {
        throw new Fault("Received unexpected final message");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("durableSubscriberTopicNoLocalTest2");
    } finally {
      cleanupSubscription(tSubNoLocal, tool.getDefaultTopicSession(),
          subscriptionName);
    }
  }

  /*
   * @testName: durableSubscriberNewTopicTest
   * 
   * @assertion_ids: JMS:SPEC:165; JMS:JAVADOC:122;
   * 
   * @test_Strategy: Create 2 topics. Create a durable subscriber for the first
   * topic. send and receive a message. Create a durable topic subscriber again,
   * use the same name as the above but for the second topic.
   * 
   */
  public void durableSubscriberNewTopicTest() throws Fault {
    TopicSubscriber durableTS = null;
    String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      Topic newTestTopic = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.DURABLE_TOPIC, jmsUser, jmsPassword, lookup,
          mode);

      // Create a second topic for the test
      newTestTopic = tool.createNewTopic("MY_TOPIC2");

      // Create a publisher for the newTestTopic Topic
      TopicPublisher newTestPublisher = tool.getDefaultTopicSession()
          .createPublisher(newTestTopic);

      // Create a durable subscriber for the default topic
      // close default Subscriber and create DurableSubscriber
      tool.getDefaultTopicSubscriber().close();
      durableTS = tool.getDefaultTopicSession().createDurableSubscriber(
          tool.getDefaultTopic(), "durableSubscriberNewTopicTest");

      // start Connection and send/receive message
      tool.getDefaultTopicConnection().start();
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText("For default topic");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "durableSubscriberNewTopicTest");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (TextMessage) durableTS.receive(timeout);

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        TestUtil
            .logTrace("Message text: \"" + messageReceived.getText() + "\"");
        TestUtil.logTrace("Received correct message");
      } else {
        throw new Fault("didn't get the right message");
      }

      // need to inactivate topic subscriber before switching to the new topic
      durableTS.close();

      // change to the new topic
      durableTS = tool.getDefaultTopicSession().createDurableSubscriber(
          newTestTopic, "durableSubscriberNewTopicTest");

      // Create and Publish a message to the new Topic
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText("For new topic");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "durableSubscriberNewTopicTest");
      newTestPublisher.publish(messageSent);
      messageReceived = (TextMessage) durableTS.receive(timeout);
      if (messageReceived.getText().equals(messageSent.getText())) {
        TestUtil
            .logTrace("Message text: \"" + messageReceived.getText() + "\"");
        TestUtil.logTrace("Received correct message");
      } else {
        throw new Fault("didn't get the right message");
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("durableSubscriberNewTopicTest");
    } finally {
      cleanupSubscription(durableTS, tool.getDefaultTopicSession(),
          "durableSubscriberNewTopicTest");
    }
  }

  /*
   * @testName: durableSubscriberChangeSelectorTest
   * 
   * @assertion_ids: JMS:SPEC:164; JMS:SPEC:165; JMS:JAVADOC:122;
   * JMS:JAVADOC:256; JMS:JAVADOC:99; JMS:JAVADOC:334;
   * 
   * @test_Strategy: Create a durable subscriber for the default topic. Create a
   * durable topic subscriber again, use the same name as the above but change
   * the selector.
   * 
   */
  public void durableSubscriberChangeSelectorTest() throws Fault {
    Topic newTestTopic;
    TopicSubscriber durableTS = null;
    String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.DURABLE_TOPIC, jmsUser, jmsPassword, lookup,
          mode);

      // Create a durable subscriber for the default topic
      // close default Subscriber and create DurableSubscriber
      tool.getDefaultTopicSubscriber().close();

      // Create a durable subscriber with a selector specified.
      durableTS = tool.getDefaultTopicSession().createDurableSubscriber(
          tool.getDefaultTopic(), "durableSubscriberChangeSelectorTest",
          "TEST = 'test'", false);

      // start Connection and send/receive message
      tool.getDefaultTopicConnection().start();
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setStringProperty("TEST", "test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "durableSubscriberChangeSelectorTest");
      messageSent.setText("For default topic ");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (TextMessage) durableTS.receive(timeout);

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        TestUtil
            .logTrace("Message text: \"" + messageReceived.getText() + "\"");
        TestUtil.logTrace("Received correct message");
      } else {
        throw new Fault("didn't get the right message");
      }

      // need to inactivate topic subscriber before switching to selector
      durableTS.close();

      // change selector
      durableTS = tool.getDefaultTopicSession().createDurableSubscriber(
          tool.getDefaultTopic(), "durableSubscriberChangeSelectorTest",
          "TEST = 'new one'", false);

      // Publish a message with old selector
      messageSent.setBooleanProperty("lastMessage", false);
      tool.getDefaultTopicPublisher().publish(messageSent);

      // Create and Publish a message with the new selector
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setStringProperty("TEST", "new one");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "durableSubscriberChangeSelectorTest");
      messageSent.setText("For new topic");
      messageSent.setBooleanProperty("lastMessage", true);
      tool.getDefaultTopicPublisher().publish(messageSent);

      // receive message
      messageReceived = (TextMessage) durableTS.receive(timeout);
      if (messageReceived != null) {
        if (messageReceived.getText().equals(messageSent.getText())
            && messageReceived.getBooleanProperty("lastMessage") == true) {
          TestUtil
              .logTrace("Message text: \"" + messageReceived.getText() + "\"");
          TestUtil.logTrace("Received correct message");
        } else {
          throw new Fault("didn't get the right message");
        }
      } else {
        throw new Fault("didn't get any message");
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("durableSubscriberChangeSelectorTest");
    } finally {
      cleanupSubscription(durableTS, tool.getDefaultTopicSession(),
          "durableSubscriberChangeSelectorTest");
    }
  }

  /*
   * @testName: durableSubscriberChangeSelectorTest2
   * 
   * @assertion_ids: JMS:SPEC:164; JMS:SPEC:165; JMS:JAVADOC:122;
   * JMS:JAVADOC:256; JMS:JAVADOC:99; JMS:JAVADOC:334;
   * 
   * @test_Strategy: 1) Create a durable subscription with a message selector
   * string property of (TEST="test") for the default topic. 2) Publish first
   * message with string property that matches the message selector
   * (TEST="test"). 3) Publish second message with string property that does not
   * match the message selector (TEST="test again"). 4) Verify that you can
   * receive the first message. 5) Verify that you cannot receive the second
   * message. 6) Close durable subscription. 7) Create a new durable
   * subscription with the same default topic and subscription name but with a
   * different message selector (TEST="test again") which matches the string
   * property of the second message that was published. 8) Try to receive this
   * second message. It should not recieve the second message. Verify that is
   * does not receive the second message. 9) Close durable subscription.
   *
   * A client can change an existing durable subscription by creating a durable
   * TopicSubscriber with the same name and a new topic and/or message selector.
   * Changing a durable subscriber is equivalent to unsubscribing (deleting) the
   * old one and creating a new one.
   *
   * So if a client subsequently changes the message selector, all the existing
   * messages stored in the durable subscription become invalid since they are
   * inconsistent with the new message selector. The only safe thing to do is to
   * delete all the old messages and start anew.
   */
  public void durableSubscriberChangeSelectorTest2() throws Fault {
    Topic newTestTopic;
    TopicSubscriber durableTS = null;
    String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

    try {
      TextMessage messageSent = null;
      TextMessage messageSent2 = null;
      TextMessage messageReceived = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.DURABLE_TOPIC, jmsUser, jmsPassword, lookup,
          mode);

      // Create a durable subscriber for the default topic
      // close default Subscriber and create DurableSubscriber
      tool.getDefaultTopicSubscriber().close();

      TestUtil.logTrace(
          "Create durable subscription with MessageSelector=\"TEST='test'\",");
      TestUtil.logTrace("TopicName=" + tool.getDefaultTopic().getTopicName()
          + " and SubscriptionName=" + "durableSubscriberChangeSelectorTest2");
      // Create a durable subscriber with a selector specified.
      durableTS = tool.getDefaultTopicSession().createDurableSubscriber(
          tool.getDefaultTopic(), "durableSubscriberChangeSelectorTest2",
          "TEST = 'test'", false);

      // start Connection and send/receive message
      tool.getDefaultTopicConnection().start();

      TestUtil.logTrace(
          "Create/Send first message with string property \"TEST = 'test'\"");
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setStringProperty("TEST", "test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "durableSubscriberChangeSelectorTest2");
      messageSent.setText("Message #1 with string property TEST='test'");
      tool.getDefaultTopicPublisher().publish(messageSent);

      TestUtil.logTrace(
          "Create/Send second message with string property \"TEST = 'test again'\"");
      messageSent2 = tool.getDefaultTopicSession().createTextMessage();
      messageSent2.setStringProperty("TEST", "test again");
      messageSent2.setStringProperty("COM_SUN_JMS_TESTNAME",
          "durableSubscriberChangeSelectorTest2");
      messageSent2.setText("Message #2 with string property TEST='test again'");
      tool.getDefaultTopicPublisher().publish(messageSent2);

      // Check and verify that first message is received
      TestUtil.logTrace("Try receiving first message (should get message)");
      messageReceived = (TextMessage) durableTS.receive(timeout);
      if (messageReceived == null) {
        TestUtil.logTrace("Did not receive any message (incorrect)");
        throw new Fault("didn't receive any message");
      } else if (messageReceived.getText().equals(messageSent.getText())) {
        TestUtil
            .logTrace("Message text: \"" + messageReceived.getText() + "\"");
        TestUtil.logTrace("Received correct first message");
      } else {
        TestUtil
            .logTrace("Message text: \"" + messageReceived.getText() + "\"");
        throw new Fault("didn't get the right message");
      }

      // Check and verify that seconde message is not received
      TestUtil
          .logTrace("Try receiving second message (should not get message)");
      messageReceived = (TextMessage) durableTS.receive(timeout);
      if (messageReceived == null) {
        TestUtil.logTrace("Did not receive second message (correct)");
      } else if (messageReceived.getText().equals(messageSent2.getText())) {
        TestUtil
            .logTrace("Message text: \"" + messageReceived.getText() + "\"");
        throw new Fault("received second message (unexpected)");
      } else {
        TestUtil
            .logTrace("Message text: \"" + messageReceived.getText() + "\"");
        throw new Fault("received unexpected message");
      }

      // need to inactivate topic subscriber before switching new subscriber
      TestUtil.logTrace("Close durable subscription");
      durableTS.close();

      // change selector
      TestUtil.logTrace(
          "Create new durable subscription with MessageSelector=\"TEST='test again'\",");
      TestUtil.logTrace("TopicName=" + tool.getDefaultTopic().getTopicName()
          + " and SubscriptionName=" + "durableSubscriberChangeSelectorTest2");
      durableTS = tool.getDefaultTopicSession().createDurableSubscriber(
          tool.getDefaultTopic(), "durableSubscriberChangeSelectorTest2",
          "TEST = 'test again'", false);

      // receive message
      messageReceived = (TextMessage) durableTS.receive(timeout);
      if (messageReceived == null) {
        TestUtil.logTrace("Did not receive any messages (correct)");
      } else {
        TestUtil
            .logTrace("Message text: \"" + messageReceived.getText() + "\"");
        throw new Fault("received unexpected message");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("durableSubscriberChangeSelectorTest2");
    } finally {
      cleanupSubscription(durableTS, tool.getDefaultTopicSession(),
          "durableSubscriberChangeSelectorTest2");
    }
  }

  /*
   * @testName: msgProducerNullDestinationTopicTest
   * 
   * @assertion_ids: JMS:SPEC:139; JMS:SPEC:158; JMS:SPEC:242; JMS:JAVADOC:103;
   * JMS:JAVADOC:105; JMS:JAVADOC:122;
   * 
   * @test_Strategy: Create Publisher with null Destination. Send with
   * destination specified and receive single message. Verify message receipt.
   */

  public void msgProducerNullDestinationTopicTest() throws Fault {
    boolean pass = true;
    TopicPublisher tPublisher = null;
    Topic nullTopic = null;
    TextMessage messageSent = null;
    TextMessage messageReceived = null;
    String testName = "msgProducerNullDestinationTopicTest";
    String message = "Just a test from msgProducerNullDestinationTopicTest";

    try {
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, mode);
      tool.getDefaultTopicPublisher().close();

      try {
        tPublisher = tool.getDefaultTopicSession().createPublisher(nullTopic);
        TestUtil.logTrace("PASS: null allowed for unidentified producer");
      } catch (Exception ee) {
        TestUtil.logErr("Error: unexpected Exception: ", ee);
        pass = false;
      }
      tool.getDefaultTopicConnection().start();

      TestUtil.logTrace("Creating  1 message");
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText(message);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);

      // publish to a topic and then get the message.
      TestUtil.logTrace("Publish a message");
      tPublisher.publish(tool.getDefaultTopic(), messageSent);
      TestUtil.logTrace("Receive a message");
      messageReceived = (TextMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      if (messageReceived == null) {
        pass = false;
      }

      TestUtil.logMsg("Publish message the second time ");
      tPublisher.publish(tool.getDefaultTopic(), messageSent,
          Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
          Message.DEFAULT_TIME_TO_LIVE);

      TestUtil.logMsg("Receiving message again");
      messageReceived = (TextMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      if (messageReceived == null) {
        TestUtil.logErr("didn't get any message");
        pass = false;
      } else if (!messageReceived.getText().equals(messageSent.getText())) {
        pass = false;
        TestUtil.logErr("didn't get the right message");
      }

      if (!pass) {
        throw new Fault(
            "Error: failures occurred during msgProducerNullDestinationTopicTest tests");
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred! ", e);
      TestUtil.printStackTrace(e);
      throw new Fault("msgProducerNullDestinationTopicTest");
    }
  }

  /*
   * @testName: multipleCloseTopicConnectionTest
   * 
   * @assertion_ids: JMS:SPEC:108;
   * 
   * @test_Strategy: Call close() twice on a connection and catch any exception.
   */
  public void multipleCloseTopicConnectionTest() throws Fault {
    try {

      // create Topic Connection
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logTrace("Call close on a connection ");
      tool.getDefaultTopicConnection().close();
      TestUtil.logTrace("Call close on a connection a second time");
      tool.getDefaultTopicConnection().close();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("multipleCloseTopicConnectionTest");
    }
  }

  /*
   * @testName: consumerTests
   *
   * @assertion_ids: JMS:SPEC:196; JMS:SPEC:158; JMS:SPEC:160; JMS:SPEC:161;
   * JMS:SPEC:126; JMS:JAVADOC:248; JMS:SPEC:266; JMS:SPEC:267;
   *
   * @test_Strategy: 1. Create a new connection and send two TextMessages; 2.
   * Create a MessageConsumer defaultConsumer to verify all messages received.
   * 3. Create another MessageConsumer noLocalConsumer with noLocal set to true,
   * and verify that no message can be received. 4. Create another
   * MessageConsumer selectConsumer off the new connection with selector to
   * verify only one message received. 5. Send a message from from default
   * connection; 6. Verify that noLocalConsumer can receive the message from the
   * default connection
   */

  public void consumerTests() throws Fault {
    String lookup = "MyTopicConnectionFactory";

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      TextMessage tempMsg = null;
      int numMessages = 2;
      boolean pass = true;
      MessageConsumer defaultConsumer = null;
      MessageConsumer selectConsumer = null;
      MessageConsumer noLocalConsumer = null;
      Connection newConn = null;
      Session newSess = null;
      MessageProducer newPub = null;
      String testName = "consumerTests";

      tool = new JmsTool(JmsTool.COMMON_T, jmsUser, jmsPassword, mode);

      newConn = tool.getNewConnection(JmsTool.COMMON_T, jmsUser, jmsPassword,
          lookup);
      connections.add(newConn);
      newSess = newConn.createSession(false, Session.AUTO_ACKNOWLEDGE);
      noLocalConsumer = newSess.createConsumer(tool.getDefaultDestination(),
          null, true);
      selectConsumer = newSess.createConsumer(tool.getDefaultDestination(),
          "TEST = 'test'", false);
      defaultConsumer = newSess.createConsumer(tool.getDefaultDestination());
      newPub = newSess.createProducer(tool.getDefaultDestination());

      tool.getDefaultConnection().start();
      newConn.start();

      // Create and send two messages from new connection
      messageSent = tool.getDefaultSession().createTextMessage();
      messageSent.setText("Just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      TestUtil.logTrace("Sending message to a Topic");
      messageSent.setBooleanProperty("lastMessage", false);
      newPub.send(messageSent);

      messageSent.setStringProperty("TEST", "test");
      messageSent.setBooleanProperty("lastMessage", true);
      newPub.send(messageSent);

      // Verify that noLocalConsumer cannot receive any message
      TestUtil.logTrace("noLocalConsumer Receiving message");
      messageReceived = (TextMessage) noLocalConsumer.receive(timeout);
      if (messageReceived != null) {
        pass = false;
        TestUtil.logErr(
            "Error:  No_local MessageConsumer did receive local message");
      }

      // Verify that defaultConsumer received correct messages
      TestUtil.logTrace("defaultConsumer Receiving message");
      for (int i = 0; i < numMessages; i++) {
        messageReceived = (TextMessage) defaultConsumer.receive(timeout);
        if (messageReceived == null) {
          pass = false;
          TestUtil.logErr("Error:  Did not receive message " + i);
        } else if (!messageReceived.getText().equals(messageSent.getText())) {
          TestUtil.logErr("Error: didn't get the right message " + i);
          pass = false;
        }
      }
      // Verify that selectConsumer only receive the last message
      TestUtil.logTrace("selectConsumer Receiving message");
      messageReceived = (TextMessage) selectConsumer.receive(timeout);
      if (messageReceived == null) {
        pass = false;
        TestUtil.logErr("Error:  Did not receive correct message");
      } else if (!messageReceived.getText().equals(messageSent.getText())) {
        TestUtil.logErr("Error: didn't get the right message");
        pass = false;
      }

      // send message from default connection
      TestUtil.logTrace("sending message from default connection");
      messageSent.setBooleanProperty("newConnection", true);
      tool.getDefaultProducer().send(messageSent);

      // Verify that noLocalConsumer now can receive message from second
      // connection
      TestUtil.logTrace("noLocalConsumer Receiving message");
      messageReceived = (TextMessage) noLocalConsumer.receive(timeout);
      if (messageReceived == null) {
        pass = false;
        TestUtil.logErr("Error:  Did not receive correct message");
      } else if (messageReceived.getText().equals(messageSent.getText())) {
        TestUtil.logMsg("Message text: \"" + messageReceived.getText() + "\"");
        TestUtil.logMsg("Received correct message");
      } else {
        TestUtil.logErr("Error: didn't get the right message");
        pass = false;
      }

      noLocalConsumer.close();
      defaultConsumer.close();
      selectConsumer.close();

      try {
        TestUtil.logTrace("Closing new connection");
        newConn.close();
      } catch (Exception ex) {
        TestUtil.logErr("Error closing the second Connection", ex);
      }

      if (pass != true)
        throw new Fault(testName + " Failed!");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault(testName);
    }
  }

  /*
   * @testName: tempTopicTests
   *
   * @assertion_ids: JMS:SPEC:144; JMS:SPEC:161; JMS:JAVADOC:264;
   * JMS:JAVADOC:124; JMS:JAVADOC:837;
   *
   * @test_Strategy: 1. Create a TemporaryTopic from a Session. Send a
   * TextMessage and Receive it using the TemporaryTopic. Verify the Message
   * received correctly. 2. Try to delete the TemporaryTopic without closing
   * MessageConsumer, verify that JMSException is thrown. 3. Close the
   * MessageConsumer, verify that the TemporaryTopic can be deleted. 4. Try to
   * create a MessageConsumer using Session from a different Connection, verify
   * that JMSException is thrown.
   */
  public void tempTopicTests() throws Fault {
    boolean pass = true;
    TextMessage msgSent;
    TextMessage msgReceived;
    String testName = "tempTopicTests";
    String message = "Just a test from tempTopicTests";
    TemporaryTopic tempT = null;
    Connection newConn = null;
    String lookup = "MyTopicConnectionFactory";

    try {
      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, jmsUser, jmsPassword, mode);
      tool.getDefaultProducer().close();
      tool.getDefaultConsumer().close();
      tool.getDefaultConnection().start();

      // create the TemporaryTopic
      TestUtil.logMsg("Creating TemporaryTopic");
      tempT = tool.getDefaultSession().createTemporaryTopic();

      // open a new connection, create Session and Sender
      TestUtil.logMsg("Create new sender for TemporaryTopic");
      MessageProducer sender = tool.getDefaultSession().createProducer(tempT);
      MessageConsumer receiver = tool.getDefaultSession().createConsumer(tempT);

      // send message to verify TemporaryTopic
      TestUtil.logMsg("Send message to TemporaryTopic");
      msgSent = tool.getDefaultSession().createTextMessage();
      msgSent.setText(message);
      msgSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      sender.send(msgSent);

      // try to create receiver for the TemporarTopic
      msgReceived = (TextMessage) receiver.receive(timeout);

      if (msgReceived == null) {
        pass = false;
        TestUtil.logErr("didnot receive message");
      } else if (!msgReceived.getText().equals(message)) {
        pass = false;
        TestUtil.logErr("Received wrong message=" + msgReceived.getText());
        TestUtil.logErr("Should have: " + message);
      }

      try {
        tempT.delete();
        pass = false;
        TestUtil
            .logErr("TemporaryTopic.delete() didn't throw expected Exception");
      } catch (JMSException em) {
        TestUtil.logTrace("Received expected JMSException: ");
      }
      receiver.close();

      try {
        tempT.delete();
      } catch (Exception e) {
        pass = false;
        TestUtil.logErr("Received unexpected Exception: ", e);
      }

      tempT = tool.getDefaultSession().createTemporaryTopic();
      newConn = (Connection) tool.getNewConnection(JmsTool.COMMON_T, jmsUser,
          jmsPassword, lookup);
      Session newSess = newConn.createSession(false, Session.AUTO_ACKNOWLEDGE);

      // try to create receiver for the TemporaryTopic
      TestUtil.logMsg(
          "Attempt to create MessageConsumer for TemporaryTopic from another Connection");
      try {
        MessageConsumer newReceiver = newSess.createConsumer(tempT);
        if (newReceiver != null)
          TestUtil.logTrace("newReceiver=" + newReceiver);
      } catch (JMSException e) {
        TestUtil
            .logTrace("Received expected JMSException from createConsumer.");
      }

      if (!pass)
        throw new Fault(testName + " failed");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault(testName);
    } finally {
      try {
        tool.getDefaultConnection().close();
      } catch (Exception e) {
        TestUtil.logErr("Error closing Connection in " + testName, e);
      }
      try {
        newConn.close();
      } catch (Exception e) {
        TestUtil.logErr("Error closing the new Connection in " + testName, e);
      }
    }
  }
}
