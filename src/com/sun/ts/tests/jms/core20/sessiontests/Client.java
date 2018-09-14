/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id: Client.java 68655 2012-11-21 16:26:50Z af70133 $
 */
package com.sun.ts.tests.jms.core20.sessiontests;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Enumeration;
import com.sun.javatest.Status;

public class Client extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core20.sessiontests.Client";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // JMS tool which creates and/or looks up the JMS administered objects
  private transient JmsTool toolT = null;

  private transient JmsTool toolT2 = null;

  private transient JmsTool toolQ = null;

  // JMS objects
  private transient ConnectionFactory cf = null;

  private transient Topic topic = null;

  private transient Queue queue = null;

  private transient Destination destination = null;

  private transient Connection connection = null;

  private transient Session session = null;

  private transient MessageProducer producer = null;

  private transient MessageConsumer consumer = null;

  private transient MessageConsumer consumer2 = null;

  private transient TopicSubscriber subscriber = null;

  private transient TopicSubscriber subscriber2 = null;

  // Harness req's
  private Properties props = null;

  // properties read from ts.jte file
  long timeout;

  String user;

  String password;

  String mode;

  String vehicle = null;

  // used for tests
  private static final String lookupDurableTopicFactory = "DURABLE_SUB_CONNECTION_FACTORY";

  private static final String lookupNormalTopicFactory = "MyTopicConnectionFactory";

  private static final int numMessages = 3;

  private static final int iterations = 5;

  ArrayList connections = null;

  boolean queueTest = false;

  /* Run test in standalone mode */

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * setupGlobalVarsQ() for Queue
   */
  public void setupGlobalVarsQ() throws Exception {
    cf = toolQ.getConnectionFactory();
    destination = toolQ.getDefaultDestination();
    session = toolQ.getDefaultSession();
    connection = toolQ.getDefaultConnection();
    producer = toolQ.getDefaultProducer();
    consumer = toolQ.getDefaultConsumer();
    queue = (Queue) destination;
    connection.start();
    queueTest = true;
  }

  /*
   * setupGlobalVarsT() for Topic with normal connection factory (clientid not
   * set)
   */
  public void setupGlobalVarsT() throws Exception {
    cf = toolT.getConnectionFactory();
    destination = toolT.getDefaultDestination();
    session = toolT.getDefaultSession();
    connection = toolT.getDefaultConnection();
    producer = toolT.getDefaultProducer();
    consumer = toolT.getDefaultConsumer();
    topic = (Topic) destination;
    connection.start();
    queueTest = false;
  }

  /*
   * setupGlobalVarsT2() for Topic with durable connection factory (clientid
   * set)
   */
  public void setupGlobalVarsT2() throws Exception {
    cf = toolT2.getConnectionFactory();
    destination = toolT2.getDefaultDestination();
    session = toolT2.getDefaultSession();
    connection = toolT2.getDefaultConnection();
    producer = toolT2.getDefaultProducer();
    consumer = toolT2.getDefaultConsumer();
    topic = (Topic) destination;
    connection.start();
    queueTest = false;
  }

  /* Test setup: */

  /*
   * setup() is called before each test
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
      vehicle = p.getProperty("vehicle");

      // check props for errors
      if (timeout < 1) {
        throw new Exception(
            "'jms_timeout' (milliseconds) in ts.jte must be > 0");
      }
      if (user == null) {
        throw new Exception("'user' in ts.jte must not be null ");
      }
      if (password == null) {
        throw new Exception("'password' in ts.jte must not be null ");
      }
      if (mode == null) {
        throw new Exception("'platform.mode' in ts.jte must not be null");
      }
      connections = new ArrayList(5);

      // set up JmsTool for COMMON_T setup (normal factory has clientid not set)
      toolT = new JmsTool(JmsTool.COMMON_T, user, password,
          lookupNormalTopicFactory, mode);

      // set up JmsTool for COMMON_Q setup
      toolQ = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("setup failed!", e);
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
      TestUtil.logMsg("Closing default Connection");
      toolQ.getDefaultConnection().close();
      toolT.getDefaultConnection().close();
      if (toolT2 != null)
        toolT2.getDefaultConnection().close();
      if (queueTest) {
        TestUtil.logMsg("Flush any messages left on Queue");
        toolQ.flushDestination();
      }
      toolQ.closeAllResources();
      toolT.closeAllResources();
      if (toolT2 != null)
        toolT2.closeAllResources();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("cleanup failed!", e);
    }
  }

  /*
   * Cleanup method for tests that use durable subscriptions
   */
  private void cleanupSubscription(MessageConsumer consumer, Session session,
      String subName) {
    if (consumer != null) {
      try {
        TestUtil.logTrace("Closing durable consumer: " + consumer);
        consumer.close();
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

  private void cleanupSubscription(TopicSubscriber tSub, Session session,
      String subName) {
    if (tSub != null) {
      try {
        TestUtil.logTrace("Closing durable subscriber: " + tSub);
        tSub.close();
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
   * @testName: sendRecvMsgsOfEachMsgTypeTopicTest
   *
   * @assertion_ids: JMS:JAVADOC:209; JMS:JAVADOC:212; JMS:JAVADOC:213;
   * JMS:JAVADOC:215; JMS:JAVADOC:217; JMS:JAVADOC:219; JMS:JAVADOC:221;
   * JMS:JAVADOC:223; JMS:JAVADOC:242; JMS:JAVADOC:317; JMS:JAVADOC:504;
   * JMS:JAVADOC:510;
   *
   * @test_Strategy: Send and receive messages of each message type: Message,
   * BytesMessage, MapMessage, ObjectMessage, StreamMessage, TextMessage. Tests
   * the following API's
   *
   * ConnectionFactory.createConnection(String, String)
   * Connection.createSession(boolean, int) Session.createMessage()
   * Session.createBytesMessage() Session.createMapMessage()
   * Session.createObjectMessage() Session.createObjectMessage(Serializable
   * object) Session.createStreamMessage() Session.createTextMessage()
   * Session.createTextMessage(String) Session.createConsumer(Destination)
   * Session.createProducer(Destination) MessageProducer.send(Message)
   * MessageConsumer.receive(long timeout)
   */
  public void sendRecvMsgsOfEachMsgTypeTopicTest() throws Fault {
    boolean pass = true;
    try {
      // Setup for Topic
      setupGlobalVarsT();

      // send and receive Message
      TestUtil.logMsg("Send Message");
      Message msg = session.createMessage();
      TestUtil.logMsg("Set some values in Message");
      msg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendRecvMsgsOfEachMsgTypeTopicTest");
      msg.setBooleanProperty("booleanProperty", true);
      producer.send(msg);
      TestUtil.logMsg("Receive Message");
      Message msgRecv = consumer.receive(timeout);
      if (msgRecv == null) {
        throw new Fault("Did not receive Message");
      }
      TestUtil.logMsg("Check the values in Message");
      if (msgRecv.getBooleanProperty("booleanProperty") == true) {
        TestUtil.logMsg("booleanproperty is correct");
      } else {
        TestUtil.logMsg("booleanproperty is incorrect");
        pass = false;
      }

      // send and receive BytesMessage
      TestUtil.logMsg("Send BytesMessage");
      BytesMessage bMsg = session.createBytesMessage();
      TestUtil.logMsg("Set some values in BytesMessage");
      bMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendRecvMsgsOfEachMsgTypeTopicTest");
      bMsg.writeByte((byte) 1);
      bMsg.writeInt((int) 22);
      producer.send(bMsg);
      TestUtil.logMsg("Receive BytesMessage");
      BytesMessage bMsgRecv = (BytesMessage) consumer.receive(timeout);
      if (bMsgRecv == null) {
        throw new Fault("Did not receive BytesMessage");
      }
      TestUtil.logMsg("Check the values in BytesMessage");
      if (bMsgRecv.readByte() == (byte) 1) {
        TestUtil.logMsg("bytevalue is correct");
      } else {
        TestUtil.logMsg("bytevalue is incorrect");
        pass = false;
      }
      if (bMsgRecv.readInt() == (int) 22) {
        TestUtil.logMsg("intvalue is correct");
      } else {
        TestUtil.logMsg("intvalue is incorrect");
        pass = false;
      }

      // send and receive MapMessage
      TestUtil.logMsg("Send MapMessage");
      MapMessage mMsg = session.createMapMessage();
      TestUtil.logMsg("Set some values in MapMessage");
      mMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendRecvMsgsOfEachMsgTypeTopicTest");
      mMsg.setBoolean("booleanvalue", true);
      mMsg.setInt("intvalue", (int) 10);
      producer.send(mMsg);
      TestUtil.logMsg("Receive MapMessage");
      MapMessage mMsgRecv = (MapMessage) consumer.receive(timeout);
      if (mMsgRecv == null) {
        throw new Fault("Did not receive MapMessage");
      }
      TestUtil.logMsg("Check the values in MapMessage");
      Enumeration list = mMsgRecv.getMapNames();
      String name = null;
      while (list.hasMoreElements()) {
        name = (String) list.nextElement();
        if (name.equals("booleanvalue")) {
          if (mMsgRecv.getBoolean(name) == true) {
            TestUtil.logMsg("booleanvalue is correct");
          } else {
            TestUtil.logErr("booleanvalue is incorrect");
            pass = false;
          }
        } else if (name.equals("intvalue")) {
          if (mMsgRecv.getInt(name) == 10) {
            TestUtil.logMsg("intvalue is correct");
          } else {
            TestUtil.logErr("intvalue is incorrect");
            pass = false;
          }
        } else {
          TestUtil.logErr("Unexpected name of [" + name + "] in MapMessage");
          pass = false;
        }
      }

      // send and receive ObjectMessage
      TestUtil.logMsg("Send ObjectMessage");
      StringBuffer sb1 = new StringBuffer("This is a StringBuffer");
      TestUtil.logMsg("Set some values in ObjectMessage");
      ObjectMessage oMsg = session.createObjectMessage();
      oMsg.setObject(sb1);
      oMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendRecvMsgsOfEachMsgTypeTopicTest");
      producer.send(oMsg);
      TestUtil.logMsg("Receive ObjectMessage");
      ObjectMessage oMsgRecv = (ObjectMessage) consumer.receive(timeout);
      if (oMsgRecv == null) {
        throw new Fault("Did not receive ObjectMessage");
      }
      TestUtil.logMsg("Check the value in ObjectMessage");
      StringBuffer sb2 = (StringBuffer) oMsgRecv.getObject();
      if (sb2.toString().equals(sb1.toString())) {
        TestUtil.logMsg("objectvalue is correct");
      } else {
        TestUtil.logErr("objectvalue is incorrect");
        pass = false;
      }

      // send and receive ObjectMessage passing object as param
      TestUtil.logMsg("Send ObjectMessage passing object as param");
      sb1 = new StringBuffer("This is a StringBuffer");
      TestUtil
          .logMsg("Set some values in ObjectMessage passing object as param");
      oMsg = session.createObjectMessage(sb1);
      oMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendRecvMsgsOfEachMsgTypeTopicTest");
      producer.send(oMsg);
      TestUtil.logMsg("Receive ObjectMessage");
      oMsgRecv = (ObjectMessage) consumer.receive(timeout);
      if (oMsgRecv == null) {
        throw new Fault("Did not receive ObjectMessage");
      }
      TestUtil.logMsg("Check the value in ObjectMessage");
      sb2 = (StringBuffer) oMsgRecv.getObject();
      if (sb2.toString().equals(sb1.toString())) {
        TestUtil.logMsg("objectvalue is correct");
      } else {
        TestUtil.logErr("objectvalue is incorrect");
        pass = false;
      }

      // send and receive StreamMessage
      TestUtil.logMsg("Send StreamMessage");
      StreamMessage sMsg = session.createStreamMessage();
      TestUtil.logMsg("Set some values in StreamMessage");
      sMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendRecvMsgsOfEachMsgTypeTopicTest");
      sMsg.writeBoolean(true);
      sMsg.writeInt((int) 22);
      producer.send(sMsg);
      TestUtil.logMsg("Receive StreamMessage");
      StreamMessage sMsgRecv = (StreamMessage) consumer.receive(timeout);
      if (sMsgRecv == null) {
        throw new Fault("Did not receive StreamMessage");
      }
      TestUtil.logMsg("Check the values in StreamMessage");
      if (sMsgRecv.readBoolean() == true) {
        TestUtil.logMsg("booleanvalue is correct");
      } else {
        TestUtil.logMsg("booleanvalue is incorrect");
        pass = false;
      }
      if (sMsgRecv.readInt() == (int) 22) {
        TestUtil.logMsg("intvalue is correct");
      } else {
        TestUtil.logMsg("intvalue is incorrect");
        pass = false;
      }

      // send and receive TextMessage
      TestUtil.logMsg("Send TextMessage");
      TextMessage tMsg = session.createTextMessage();
      TestUtil.logMsg("Set some values in MapMessage");
      tMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendRecvMsgsOfEachMsgTypeTopicTest");
      tMsg.setText("Hello There!");
      producer.send(tMsg);
      TestUtil.logMsg("Receive TextMessage");
      TextMessage tMsgRecv = (TextMessage) consumer.receive(timeout);
      if (tMsgRecv == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg("Check the value in TextMessage");
      if (tMsgRecv.getText().equals("Hello There!")) {
        TestUtil.logMsg("TextMessage is correct");
      } else {
        TestUtil.logErr("TextMessage is incorrect");
        pass = false;
      }

      // send and receive TextMessage passing string as param
      TestUtil.logMsg("Send TextMessage");
      tMsg = session.createTextMessage("Where are you!");
      TestUtil.logMsg("Set some values in TextMessage");
      tMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendRecvMsgsOfEachMsgTypeTopicTest");
      producer.send(tMsg);
      TestUtil.logMsg("Receive TextMessage");
      tMsgRecv = (TextMessage) consumer.receive(timeout);
      if (tMsgRecv == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg("Check the value in TextMessage");
      if (tMsgRecv.getText().equals("Where are you!")) {
        TestUtil.logMsg("TextMessage is correct");
      } else {
        TestUtil.logErr("TextMessage is incorrect");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      e.printStackTrace();
      throw new Fault("sendRecvMsgsOfEachMsgTypeTopicTest", e);
    }

    if (!pass) {
      throw new Fault("sendRecvMsgsOfEachMsgTypeTopicTest failed");
    }
  }

  /*
   * @testName: createTemporayTopicTest
   *
   * @assertion_ids: JMS:JAVADOC:93;
   *
   * @test_Strategy: Test the following APIs:
   *
   * Session.createTemporaryTopic().
   *
   * Send and receive a TextMessage to temporary topic. Compare send and recv
   * message for equality.
   */
  public void createTemporayTopicTest() throws Fault {
    boolean pass = true;
    try {

      String message = "a text message";

      // Setup for Topic
      setupGlobalVarsT();

      // create a TemporaryTopic
      TestUtil.logMsg("Creating TemporaryTopic");
      TemporaryTopic tempTopic = session.createTemporaryTopic();

      // Create a MessageConsumer for this Temporary Topic
      TestUtil.logMsg("Creating MessageConsumer for TemporaryTopic");
      MessageConsumer tConsumer = session.createConsumer(tempTopic);

      // Create a MessageProducer for this Temporary Topic
      TestUtil.logMsg("Creating MessageProducer for TemporaryTopic");
      MessageProducer tProducer = session.createProducer(tempTopic);

      // Send TextMessage to temporary topic
      TestUtil.logMsg("Creating TextMessage with text [" + message + "]");
      TextMessage tMsg = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      tMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "createTemporayTopicTest");
      TestUtil.logMsg("Send TextMessage to temporaty topic");
      tProducer.send(tMsg);

      // Receive TextMessage from temporary topic
      TestUtil.logMsg("Receive TextMessage from temporaty topic");
      TextMessage tMsgRecv = (TextMessage) tConsumer.receive(timeout);
      if (tMsgRecv == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg("Check the value in TextMessage");
      if (tMsgRecv.getText().equals(message)) {
        TestUtil.logMsg("TextMessage is correct");
      } else {
        TestUtil.logErr("TextMessage is incorrect");
        pass = false;
      }

      TestUtil.logMsg(
          "Attempting to delete temporary topic with an open consumer should not be allowed");
      try {
        tempTopic.delete();
        pass = false;
        TestUtil
            .logErr("TemporaryTopic.delete() didn't throw expected Exception");
      } catch (JMSException em) {
        TestUtil.logMsg("Received expected JMSException: ");
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected Exception: " + e);
        pass = false;
      }

      TestUtil.logMsg("Now close the open consumer");
      try {
        tConsumer.close();
      } catch (Exception e) {
        TestUtil.logErr("Caught exception: " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Attempting to delete temporary topic with no open consumer should be allowed");
      try {
        tempTopic.delete();
      } catch (Exception e) {
        pass = false;
        TestUtil.logErr("Received unexpected Exception: ", e);
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      e.printStackTrace();
      throw new Fault("createTemporayTopicTest");
    }

    if (!pass) {
      throw new Fault("createTemporayTopicTest failed");
    }
  }

  /*
   * @testName: getTransactedTopicTest
   *
   * @assertion_ids: JMS:JAVADOC:225;
   *
   * @test_Strategy: Test the following APIs:
   *
   * Session.getTransacted().
   */
  public void getTransactedTopicTest() throws Fault {
    boolean pass = true;

    // Test for transacted mode false
    try {
      // Setup for Topic
      setupGlobalVarsT();
      session.close();

      session = connection.createSession(Session.AUTO_ACKNOWLEDGE);
      boolean expTransacted = false;
      TestUtil.logMsg("Calling getTransacted and expect " + expTransacted
          + " to be returned");
      boolean actTransacted = session.getTransacted();
      if (actTransacted != expTransacted) {
        TestUtil.logErr("getTransacted() returned " + actTransacted
            + ", expected " + expTransacted);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("getTransactedTopicTest", e);
    } finally {
      try {
        if (session != null)
          session.close();
      } catch (Exception e) {
      }
    }

    // Test for transacted mode true
    if ((vehicle.equals("appclient") || vehicle.equals("standalone"))) {
      try {
        session = connection.createSession(Session.SESSION_TRANSACTED);
        boolean expTransacted = true;
        TestUtil.logMsg("Calling getTransacted and expect " + expTransacted
            + " to be returned");
        boolean actTransacted = session.getTransacted();
        if (actTransacted != expTransacted) {
          TestUtil.logErr("getTransacted() returned " + actTransacted
              + ", expected " + expTransacted);
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught exception: " + e);
        throw new Fault("getTransactedTopicTest", e);
      } finally {
        try {
          if (session != null)
            session.close();
        } catch (Exception e) {
        }
      }
    }

    if (!pass) {
      throw new Fault("getTransactedTopicTest failed");
    }
  }

  /*
   * @testName: getAcknowledgeModeTopicTest
   *
   * @assertion_ids: JMS:JAVADOC:227;
   *
   * @test_Strategy: Test the following APIs:
   *
   * Session.getAcknowledgeMode().
   */
  public void getAcknowledgeModeTopicTest() throws Fault {
    boolean pass = true;

    // Test for AUTO_ACKNOWLEDGE mode
    try {
      // Setup for Topic
      setupGlobalVarsT();
      session.close();

      session = connection.createSession(Session.AUTO_ACKNOWLEDGE);
      int expAcknowledgeMode = Session.AUTO_ACKNOWLEDGE;
      TestUtil.logMsg("Calling getAcknowledgeMode and expect "
          + expAcknowledgeMode + " to be returned");
      int actAcknowledgeMode = session.getAcknowledgeMode();
      if (actAcknowledgeMode != expAcknowledgeMode) {
        TestUtil.logErr("getAcknowledgeMode() returned " + actAcknowledgeMode
            + ", expected " + expAcknowledgeMode);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("getAcknowledgeModeTopicTest", e);
    } finally {
      try {
        if (session != null)
          session.close();
      } catch (Exception e) {
      }
    }

    // Test for DUPS_OK_ACKNOWLEDGE mode
    try {
      session = connection.createSession(Session.DUPS_OK_ACKNOWLEDGE);
      int expAcknowledgeMode = Session.DUPS_OK_ACKNOWLEDGE;
      TestUtil.logMsg("Calling getAcknowledgeMode and expect "
          + expAcknowledgeMode + " to be returned");
      int actAcknowledgeMode = session.getAcknowledgeMode();
      if (actAcknowledgeMode != expAcknowledgeMode) {
        TestUtil.logErr("getAcknowledgeMode() returned " + actAcknowledgeMode
            + ", expected " + expAcknowledgeMode);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("getAcknowledgeModeTopicTest", e);
    } finally {
      try {
        if (session != null)
          session.close();
      } catch (Exception e) {
      }
    }

    // Test for SESSION_TRANSACTED mode
    if ((vehicle.equals("appclient") || vehicle.equals("standalone"))) {
      try {
        session = connection.createSession(Session.SESSION_TRANSACTED);
        int expAcknowledgeMode = Session.SESSION_TRANSACTED;
        TestUtil.logMsg("Calling getAcknowledgeMode and expect "
            + expAcknowledgeMode + " to be returned");
        int actAcknowledgeMode = session.getAcknowledgeMode();
        if (actAcknowledgeMode != expAcknowledgeMode) {
          TestUtil.logErr("getAcknowledgeMode() returned " + actAcknowledgeMode
              + ", expected " + expAcknowledgeMode);
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught exception: " + e);
        throw new Fault("getAcknowledgeModeTopicTest", e);
      } finally {
        try {
          if (session != null)
            session.close();
        } catch (Exception e) {
        }
      }
    }

    if (!pass) {
      throw new Fault("getAcknowledgeModeTopicTest failed");
    }
  }

  /*
   * @testName: createConsumerProducerTopicTest
   *
   * @assertion_ids: JMS:JAVADOC:244; JMS:JAVADOC:246; JMS:JAVADOC:248;
   * JMS:JAVADOC:224; JMS:JAVADOC:242; JMS:JAVADOC:221; JMS:JAVADOC:504;
   * JMS:JAVADOC:510; JMS:JAVADOC:597; JMS:JAVADOC:334;
   *
   * @test_Strategy: Test the following APIs:
   *
   * ConnectionFactory.createConnection(String, String)
   * Connection.createSession(boolean, int) Session.createTextMessage(String)
   * Session.createConsumer(Destination) Session.createConsumer(Destination,
   * String) Session.createConsumer(Destination, String, boolean)
   * Session.createProducer(Destination) MessageProducer.send(Message)
   * MessageConsumer.receive(long timeout)
   * 
   * 1. Create a MessageConsumer with selector to consumer just the last message
   * in the Topic with boolproperty (lastMessage=TRUE). 2. Create a
   * MessageConsumer again to consume all the messages in the Topic. 3. Send x
   * text messages to a Topic. 4. Verify that both consumers work as expected.
   */
  public void createConsumerProducerTopicTest() throws Fault {
    boolean pass = true;
    MessageConsumer consumerSelect = null;
    try {
      TextMessage tempMsg = null;
      Enumeration msgs = null;

      // Setup for Topic
      setupGlobalVarsT();
      consumer.close();

      // Create selective MessageConsumer to consume messages in Topic with
      // boolean
      // property (lastMessage=TRUE)
      TestUtil.logMsg(
          "Create selective consumer to consume messages in Topic with boolproperty (lastMessage=TRUE)");
      consumerSelect = session.createConsumer(destination, "lastMessage=TRUE");

      // Create normal MessageConsumer to consume all messages in Topic
      TestUtil
          .logMsg("Create normal consumer to consume all messages in Topic");
      consumer = session.createConsumer(destination);

      // send "numMessages" messages to Topic plus end of stream message
      TestUtil.logMsg("Send " + numMessages + " messsages to Topic");
      for (int i = 1; i <= numMessages; i++) {
        tempMsg = session.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "createConsumerProducerTopicTest" + i);
        tempMsg.setJMSType("");
        if (i == numMessages) {
          tempMsg.setBooleanProperty("lastMessage", true);
        } else {
          tempMsg.setBooleanProperty("lastMessage", false);
        }
        producer.send(tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      TestUtil.logMsg(
          "Consume messages with selective consumer which has boolproperty (lastMessage=TRUE)");
      if (consumerSelect != null)
        tempMsg = (TextMessage) consumerSelect.receive(timeout);
      if (tempMsg == null) {
        TestUtil.logErr("MessageConsumer.receive() returned NULL");
        TestUtil.logErr("Message " + numMessages + " missing from Topic");
        pass = false;
      } else if (!tempMsg.getText().equals("Message " + numMessages)) {
        TestUtil.logErr(
            "Received [" + tempMsg.getText() + "] expected [Message 3]");
        pass = false;
      } else {
        TestUtil.logMsg("Received expected message: " + tempMsg.getText());
      }

      // Try to receive one more message (should return null)
      TestUtil.logMsg("Make sure selective consumer receives no more messages");
      if (consumerSelect != null)
        tempMsg = (TextMessage) consumerSelect.receive(timeout);
      if (tempMsg != null) {
        TestUtil.logErr("MessageConsumer.receive() returned a message ["
            + tempMsg.getText() + "] (Expected NULL)");
        TestUtil.logErr(
            "MessageConsumer with selector should have returned just 1 message");
        pass = false;
      } else {
        TestUtil.logMsg("No more messages for selective consumer (Correct)");
      }

      try {
        if (consumerSelect != null)
          consumerSelect.close();
      } catch (Exception e) {
      }

      TestUtil.logMsg("Consume all messages with normal consumer");
      for (int msgCount = 1; msgCount <= numMessages; msgCount++) {
        tempMsg = (TextMessage) consumer.receive(timeout);
        if (tempMsg == null) {
          TestUtil.logErr("MessageConsumer.receive() returned NULL");
          TestUtil.logErr("Message " + msgCount + " missing from Topic");
          pass = false;
        } else if (!tempMsg.getText().equals("Message " + msgCount)) {
          TestUtil.logErr("Received [" + tempMsg.getText()
              + "] expected [Message " + msgCount + "]");
          pass = false;
        } else {
          TestUtil.logMsg("Received message: " + tempMsg.getText());
        }
      }

      // Try to receive one more message (should return null)
      TestUtil.logMsg("Make sure normal consumer receives no more messages");
      tempMsg = (TextMessage) consumer.receive(timeout);
      if (tempMsg != null) {
        TestUtil
            .logErr("Received [" + tempMsg.getText() + "] expected no message");
        TestUtil.logErr("MessageConsumer should have returned " + numMessages
            + " messages");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      e.printStackTrace();
      throw new Fault("createConsumerProducerTopicTest");
    } finally {
      try {
        if (consumer != null)
          consumer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("createConsumerProducerTopicTest failed");
    }
  }

  /*
   * @testName: createDurableSubscriberTopicTest1
   *
   * @assertion_ids: JMS:JAVADOC:254;
   *
   * @test_Strategy: Creates a durable subscription with the specified name on
   * the specified topic, and creates a TopicSubscriber on that durable
   * subscription.
   *
   * This uses a connection factory WITH client identifier set. The client
   * identifer MUST be set for this API.
   *
   * Tests the following API method:
   *
   * Session.createDurableSubscriber(Topic, String)
   *
   * 1. Send a text message to a Topic. 2. Consume message via the
   * TopicSubscriber created.
   */
  public void createDurableSubscriberTopicTest1() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    TopicSubscriber tSub = null;
    try {
      TextMessage expTextMessage = null;

      // set up JmsTool for COMMON_T setup (durable factory has clientid set)
      toolT.getDefaultConnection().close();
      toolT.closeAllResources();
      toolT2 = new JmsTool(JmsTool.COMMON_T, user, password,
          lookupDurableTopicFactory, mode);

      // Setup for Topic
      setupGlobalVarsT2();

      // Create Durable Subscription and a TopicSubscriber for it
      TestUtil
          .logMsg("Create a Durable Subscription and a TopicSubscriber for it");
      tSub = session.createDurableSubscriber(topic,
          "createDurableSubscriberTopicTest1");

      TestUtil.logMsg("Send message to Topic");
      expTextMessage = session.createTextMessage(message);
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "createDurableSubscriberTopicTest1");
      producer.send(expTextMessage);
      TestUtil.logMsg("Message sent");

      TestUtil.logMsg("Receive TextMessage");
      TextMessage actTextMessage = (TextMessage) tSub.receive(timeout);
      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg("Check the value in TextMessage");
      if (actTextMessage.getText().equals(expTextMessage.getText())) {
        TestUtil.logMsg("TextMessage is correct");
      } else {
        TestUtil.logErr(
            "TextMessage is incorrect expected " + expTextMessage.getText()
                + ", received " + actTextMessage.getText());
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      e.printStackTrace();
      throw new Fault("createDurableSubscriberTopicTest1", e);
    } finally {
      try {
        cleanupSubscription(tSub, session, "createDurableSubscriberTopicTest1");
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("createDurableSubscriberTopicTest1 failed");
    }
  }

  /*
   * @testName: createDurableSubscriberTopicTest2
   *
   * @assertion_ids: JMS:JAVADOC:256;
   *
   * @test_Strategy: Creates a durable subscription with the specified name on
   * the specified topic, and creates a TopicSubscriber on that durable
   * subscription, specifying a message selector and whether messages published
   * by its own connection should be delivered to it.
   *
   * This uses a connection factory WITH client identifier set. The client
   * identifer MUST be set for this API.
   *
   * Tests the following API method:
   *
   * Session.createDurableSubscriber(Topic,String,String,boolean)
   *
   * 1 Create a durable subscriber on the specified topic, and create a
   * TopicSubscriber with the specified message selector 2 Send a number of
   * messages to the destination 3 Test both noLocal=true and noLocal=false
   * cases 4 Verify message with specified selector received by listener in the
   * noLocal=false case only.
   *
   */
  public void createDurableSubscriberTopicTest2() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    TopicSubscriber tSub = null;
    try {
      // set up JmsTool for COMMON_T setup (durable factory has clientid set)
      toolT.getDefaultConnection().close();
      toolT.closeAllResources();
      toolT2 = new JmsTool(JmsTool.COMMON_T, user, password,
          lookupDurableTopicFactory, mode);

      // Setup for Topic
      setupGlobalVarsT2();

      // Create Durable Subscription and a TopicSubscriber for it
      // Test the noLocal=false case with message selector
      TestUtil
          .logMsg("Create a Durable Subscription and a TopicSubscriber for it");
      tSub = session.createDurableSubscriber(topic,
          "createDurableSubscriberTopicTest2", "lastMessage = TRUE", false);

      // send "numMessages" messages to Topic
      TestUtil.logMsg("Send " + numMessages + " to Topic");
      for (int i = 1; i <= numMessages; i++) {
        TextMessage tempMsg = session.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "createDurableSubscriberTopicTest2" + i);
        if (i == numMessages) {
          tempMsg.setBooleanProperty("lastMessage", true);
        } else {
          tempMsg.setBooleanProperty("lastMessage", false);
        }
        producer.send(tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      TestUtil.logMsg("Receive TextMessage");
      TextMessage expTextMessage = session
          .createTextMessage("Message " + numMessages);
      TextMessage actTextMessage = (TextMessage) tSub.receive(timeout);
      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg("Check the value in TextMessage");
      if (actTextMessage.getText().equals(expTextMessage.getText())) {
        TestUtil.logMsg("TextMessage is correct");
      } else {
        TestUtil.logErr(
            "TextMessage is incorrect expected " + expTextMessage.getText()
                + ", received " + actTextMessage.getText());
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      e.printStackTrace();
      throw new Fault("createDurableSubscriberTopicTest2", e);
    } finally {
      try {
        cleanupSubscription(tSub, session, "createDurableSubscriberTopicTest2");
      } catch (Exception e) {
      }
    }

    try {

      // Create Durable Subscription and a TopicSubscriber for it
      // Test the noLocal=true case with message selector
      TestUtil
          .logMsg("Create a Durable Subscription and a TopicSubscriber for it");
      tSub = session.createDurableSubscriber(topic,
          "createDurableSubscriberTopicTest2", "lastMessage = TRUE", true);

      // send "numMessages" messages to Topic
      TestUtil.logMsg("Send " + numMessages + " to Topic");
      for (int i = 1; i <= numMessages; i++) {
        TextMessage tempMsg = session.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "createDurableSubscriberTopicTest2" + i);
        if (i == numMessages) {
          tempMsg.setBooleanProperty("lastMessage", true);
        } else {
          tempMsg.setBooleanProperty("lastMessage", false);
        }
        producer.send(tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      TestUtil.logMsg("Receive TextMessage");
      TextMessage actTextMessage = (TextMessage) tSub.receive(timeout);

      if (actTextMessage != null) {
        TestUtil.logErr("message were delivered when noLocal=true");
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      e.printStackTrace();
      throw new Fault("createDurableSubscriberTopicTest2", e);
    } finally {
      try {
        cleanupSubscription(tSub, session, "createDurableSubscriberTopicTest2");
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("createDurableSubscriberTopicTest2 failed");
    }
  }

  /*
   * @testName: createDurableConsumerTopicTest1
   *
   * @assertion_ids: JMS:JAVADOC:1087;
   *
   * @test_Strategy: Creates a durable subscription with the specified name on
   * the specified topic, and creates a MessageConsumer on that durable
   * subscription.
   *
   * This uses a connection factory WITH client identifier set. The client
   * identifer MUST be set for this API.
   *
   * Tests the following API method:
   *
   * Session.createDurableConsumer(Topic, String)
   *
   * 1. Create a durable subscription with the specified name on the specified
   * topic and create a durable MessageConsumer on that durable subscription.
   * This uses a connection factory WITH client identifier set. 2. Send
   * TextMessage (message1) to the Topic. 3. Consume message via MessageConsumer
   * created. Verify message1 received. 4. Close consumer. 5. Send another
   * TextMessage (message2) to the Topic. 6. Recreate the durable
   * MessageConsumer on that durable subscription. 7. Consume message via
   * MessageConsumer created. Verify message2 received.
   */
  public void createDurableConsumerTopicTest1() throws Fault {
    boolean pass = true;
    String message1 = "Where are you!";
    String message2 = "Who are you!";
    MessageConsumer tCon = null;
    try {
      TextMessage expTextMessage = null;

      // set up JmsTool for COMMON_T setup (durable factory has clientid set)
      toolT.getDefaultConnection().close();
      toolT.closeAllResources();
      toolT2 = new JmsTool(JmsTool.COMMON_T, user, password,
          lookupDurableTopicFactory, mode);

      // Setup for Topic
      setupGlobalVarsT2();

      // Create Durable Subscription and a MessageConsumer for it
      TestUtil
          .logMsg("Create a Durable Subscription and a MessageConsumer for it");
      tCon = session.createDurableConsumer(topic,
          "createDurableConsumerTopicTest1");

      TestUtil.logMsg("Send TextMessage message1 to Topic");
      expTextMessage = session.createTextMessage(message1);
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "createDurableConsumerTopicTest1");
      producer.send(expTextMessage);
      TestUtil.logMsg("TextMessage message1 sent");

      TestUtil.logMsg("Receive TextMessage message1");
      TextMessage actTextMessage = (TextMessage) tCon.receive(timeout);
      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg("Check the value in TextMessage message1");
      if (actTextMessage.getText().equals(expTextMessage.getText())) {
        TestUtil.logMsg("TextMessage is correct");
      } else {
        TestUtil.logErr(
            "TextMessage is incorrect expected " + expTextMessage.getText()
                + ", received " + actTextMessage.getText());
        pass = false;
      }
      TestUtil.logMsg("Close durable MessageConsumer");
      tCon.close();

      TestUtil.logMsg("Send TextMessage message2 to Topic");
      expTextMessage = session.createTextMessage(message1);
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "createDurableConsumerTopicTest1");
      producer.send(expTextMessage);
      TestUtil.logMsg("TextMessage message2 sent");

      // Recreate Durable Subscription and a MessageConsumer for it
      TestUtil.logMsg(
          "Recreate a Durable Subscription and a MessageConsumer for it");
      tCon = session.createDurableConsumer(topic,
          "createDurableConsumerTopicTest1");

      TestUtil.logMsg("Receive TextMessage message2");
      actTextMessage = (TextMessage) tCon.receive(timeout);
      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg("Check the value in TextMessage message2");
      if (actTextMessage.getText().equals(expTextMessage.getText())) {
        TestUtil.logMsg("TextMessage is correct");
      } else {
        TestUtil.logErr(
            "TextMessage is incorrect expected " + expTextMessage.getText()
                + ", received " + actTextMessage.getText());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      e.printStackTrace();
      throw new Fault("createDurableConsumerTopicTest1", e);
    } finally {
      try {
        cleanupSubscription(tCon, session, "createDurableConsumerTopicTest1");
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("createDurableConsumerTopicTest1 failed");
    }
  }

  /*
   * @testName: createDurableConsumerTopicTest2
   *
   * @assertion_ids: JMS:JAVADOC:1090;
   *
   * @test_Strategy: Creates a durable subscription with the specified name on
   * the specified topic, and creates a MessageConsumer on that durable
   * subscription, specifying a message selector and whether messages published
   * by its own connection should be delivered to it.
   *
   * This uses a connection factory WITH client identifier set. The client
   * identifer MUST be set for this API.
   *
   * Tests the following API method:
   *
   * Session.createDurableConsumer(Topic,String,String,boolean)
   *
   * 1. Create a durable subscription with the specified name on the specified
   * topic and create a durable MessageConsumer on that durable subscription
   * specifing a message selector and whether messages published by its own
   * connection should be delivered to it. 2. Send a number of messages to the
   * Topic. 3. Test both noLocal=true and noLocal=false cases. 4. Verify message
   * with specified selector received by MessageConsumer in the noLocal=false
   * case only.
   *
   */
  public void createDurableConsumerTopicTest2() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    MessageConsumer tCon = null;
    try {
      // set up JmsTool for COMMON_T setup (durable factory has clientid set)
      toolT.getDefaultConnection().close();
      toolT.closeAllResources();
      toolT2 = new JmsTool(JmsTool.COMMON_T, user, password,
          lookupDurableTopicFactory, mode);

      // Setup for Topic
      setupGlobalVarsT2();

      // Create Durable Subscription and a MessageConsumer for it
      // Test the noLocal=false case with message selector
      TestUtil.logMsg(
          "Create a Durable Subscription and a MessageConsumer with message selector, noLocal=false");
      tCon = session.createDurableConsumer(topic,
          "createDurableConsumerTopicTest2", "lastMessage = TRUE", false);

      // send "numMessages" messages to Topic
      TestUtil.logMsg("Send " + numMessages + " to Topic");
      for (int i = 1; i <= numMessages; i++) {
        TextMessage tempMsg = session.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "createDurableConsumerTopicTest2" + i);
        if (i == numMessages) {
          tempMsg.setBooleanProperty("lastMessage", true);
        } else {
          tempMsg.setBooleanProperty("lastMessage", false);
        }
        producer.send(tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      TestUtil.logMsg("Receive TextMessage");
      TestUtil.logMsg(
          "This is noLacal=false case so expect to get just last message");
      TextMessage expTextMessage = session
          .createTextMessage("Message " + numMessages);
      TextMessage actTextMessage = (TextMessage) tCon.receive(timeout);
      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg("Check the value in TextMessage");
      if (actTextMessage.getText().equals(expTextMessage.getText())) {
        TestUtil.logMsg("TextMessage is correct");
      } else {
        TestUtil.logErr(
            "TextMessage is incorrect expected " + expTextMessage.getText()
                + ", received " + actTextMessage.getText());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      e.printStackTrace();
      throw new Fault("createDurableConsumerTopicTest2", e);
    } finally {
      try {
        cleanupSubscription(tCon, session, "createDurableConsumerTopicTest2");
      } catch (Exception e) {
      }
    }

    try {
      // Create Durable Subscription and a MessageConsumer for it
      // Test the noLocal=true case with message selector
      TestUtil.logMsg(
          "Create a Durable Subscription and a MessageConsumer with message selector, noLocal=true");
      tCon = session.createDurableConsumer(topic,
          "createDurableConsumerTopicTest2", "lastMessage = TRUE", true);

      // send "numMessages" messages to Topic
      TestUtil.logMsg("Send " + numMessages + " to Topic");
      for (int i = 1; i <= numMessages; i++) {
        TextMessage tempMsg = session.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "createDurableConsumerTopicTest2" + i);
        if (i == numMessages) {
          tempMsg.setBooleanProperty("lastMessage", true);
        } else {
          tempMsg.setBooleanProperty("lastMessage", false);
        }
        producer.send(tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      TestUtil.logMsg("Receive TextMessage");
      TextMessage actTextMessage = (TextMessage) tCon.receive(timeout);

      if (actTextMessage != null) {
        TestUtil.logErr("Message was delivered when noLocal=true");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      e.printStackTrace();
      throw new Fault("createDurableConsumerTopicTest2", e);
    } finally {
      try {
        cleanupSubscription(tCon, session, "createDurableConsumerTopicTest2");
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("createDurableConsumerTopicTest2 failed");
    }
  }

  /*
   * @testName: createSharedConsumerTopicTest1
   *
   * @assertion_ids: JMS:JAVADOC:1163; JMS:SPEC:269;
   *
   * @test_Strategy: Creates a shared non-durable subscription with the
   * specified name on the specified topic, and creates a MessageConsumer on
   * that subscription.
   *
   * Tests the following API method:
   *
   * Session.createSharedConsumer(Topic, String)
   *
   * 1. Creates a shared non-durable subscription with the specified name on the
   * specified topic, and creates a MessageConsumer on that subscription. 2.
   * Create a second MessageConsumer on that subscription. 3. Send a text
   * message to the Topic. 4. Consume message via the first MessageConsumer and
   * message should be received. 5. Attempt to consume message via second
   * MessageConsumer and no message should be received. 6. Re-Send a text
   * message to the Topic. 7. Consume message via the second MessageConsumer and
   * message should be received. 8. Attempt to consume message via first
   * MessageConsumer and no message should be received.
   */
  public void createSharedConsumerTopicTest1() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    String sharedSubscriptionName = "createSharedConsumerTopicTest1";
    TextMessage expTextMessage = null;
    try {
      // Setup for Topic
      setupGlobalVarsT();

      // Create shared non-durable Subscription and a MessageConsumer for it
      TestUtil.logMsg(
          "Create a shared non-durable Subscription and a MessageConsumer for it");
      consumer.close();
      consumer = session.createSharedConsumer(topic, sharedSubscriptionName);

      // Create a second MessageConsumer for the subscription
      TestUtil.logMsg("Create a second MessageConsumer for the Subscription");
      consumer2 = session.createSharedConsumer(topic, sharedSubscriptionName);

      TestUtil.logMsg("Send message to Topic");
      expTextMessage = session.createTextMessage(message);
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          sharedSubscriptionName);
      producer.send(expTextMessage);
      TestUtil.logMsg("Message sent");

      TestUtil.logMsg("Receive TextMessage from consumer1");
      TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
      if (actTextMessage == null) {
        TestUtil.logErr("Did not receive TextMessage");
        pass = false;
      } else {
        TestUtil.logMsg("Check the value in TextMessage");
        if (actTextMessage.getText().equals(expTextMessage.getText())) {
          TestUtil.logMsg("TextMessage is correct");
        } else {
          TestUtil.logErr(
              "TextMessage is incorrect expected " + expTextMessage.getText()
                  + ", received " + actTextMessage.getText());
          pass = false;
        }
      }

      TestUtil.logMsg(
          "Attempt to Receive TextMessage from consumer2 - there should be none");
      actTextMessage = (TextMessage) consumer2.receive(timeout);
      if (actTextMessage != null) {
        throw new Fault("Did receive TextMessage - unexpected.");
      } else
        TestUtil.logMsg("Did not receive TextMessage - expected.");

      TestUtil.logMsg("Send another message to Topic");
      expTextMessage = session.createTextMessage(message);
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          sharedSubscriptionName);
      producer.send(expTextMessage);
      TestUtil.logMsg("Message sent");

      TestUtil.logMsg("Receive TextMessage from consumer2");
      actTextMessage = (TextMessage) consumer2.receive(timeout);
      if (actTextMessage == null) {
        TestUtil.logErr("Did not receive TextMessage");
        pass = false;
      } else {
        TestUtil.logMsg("Check the value in TextMessage");
        if (actTextMessage.getText().equals(expTextMessage.getText())) {
          TestUtil.logMsg("TextMessage is correct");
        } else {
          TestUtil.logErr(
              "TextMessage is incorrect expected " + expTextMessage.getText()
                  + ", received " + actTextMessage.getText());
          pass = false;
        }
      }
      TestUtil.logMsg(
          "Attempt to Receive TextMessage from consumer1 - there should be none");
      actTextMessage = (TextMessage) consumer.receive(timeout);
      if (actTextMessage != null) {
        throw new Fault("Did receive TextMessage - unexpected.");
      } else
        TestUtil.logMsg("Did not receive TextMessage - expected.");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      e.printStackTrace();
      throw new Fault("createSharedConsumerTopicTest1", e);
    } finally {
      try {
        if (consumer != null)
          consumer.close();
        if (consumer2 != null)
          consumer2.close();
      } catch (Exception e) {
        TestUtil.logMsg("Ignoring exception closing consumers: " + e);
      }
    }

    if (!pass) {
      throw new Fault("createSharedConsumerTopicTest1 failed");
    }
  }

  /*
   * @testName: createSharedConsumerTopicTest2
   *
   * @assertion_ids: JMS:JAVADOC:1167; JMS:SPEC:269; JMS:SPEC:270;
   *
   * @test_Strategy: Creates a shared non-durable subscription with the
   * specified name on the specified topic, and creates a MessageConsumer on
   * that subscription, specifying a message selector.
   *
   * Tests the following API method:
   *
   * Session.createSharedConsumer(Topic, String, String)
   *
   * 1. Create a shared non-durable subscription with the specified name on the
   * specified topic, and creates a MessageConsumer on that subscription,
   * specifying a message selector. 2. Create a second MessageConsumer on that
   * subscription. 3. Send a text message to the Topic. 4. Consume message via
   * first MessageConsumer and message selector and message should be received.
   * 5. Attempt to consume message via second MessageConsumer and message
   * selector and no message received. 6. Re-Send a text message to the Topic.
   * 7. Consume message via second MessageConsumer and message selector and
   * message should be received. 8. Attempt to consume message via first
   * MessageConsumer and message selector and no message received.
   */
  public void createSharedConsumerTopicTest2() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    String sharedSubscriptionName = "createSharedConsumerTopicTest2";
    try {
      // Setup for Topic
      setupGlobalVarsT();

      // Create shared non-durable Subscription and a MessageConsumer for it
      TestUtil.logMsg(
          "Create a shared non-durable Subscription and a MessageConsumer for it");
      consumer.close();
      consumer = session.createSharedConsumer(topic, sharedSubscriptionName,
          "lastMessage = TRUE");

      // Create a second MessageConsumer for the subscription
      TestUtil.logMsg("Create a second MessageConsumer for the Subscription");
      consumer2 = session.createSharedConsumer(topic, sharedSubscriptionName,
          "lastMessage = TRUE");

      // send "numMessages" messages to Topic
      TestUtil.logMsg("Send " + numMessages + " to Topic");
      for (int i = 1; i <= numMessages; i++) {
        TextMessage tempMsg = session.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            sharedSubscriptionName + i);
        if (i == numMessages) {
          tempMsg.setBooleanProperty("lastMessage", true);
        } else {
          tempMsg.setBooleanProperty("lastMessage", false);
        }
        producer.send(tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      TestUtil.logMsg("Receive TextMessage from consumer1");
      TextMessage expTextMessage = session
          .createTextMessage("Message " + numMessages);
      TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
      if (actTextMessage == null) {
        TestUtil.logErr("Did not receive TextMessage");
        pass = false;
      } else {
        TestUtil.logMsg("Check the value in TextMessage");
        if (actTextMessage.getText().equals(expTextMessage.getText())) {
          TestUtil.logMsg("TextMessage is correct");
        } else {
          TestUtil.logErr(
              "TextMessage is incorrect expected " + expTextMessage.getText()
                  + ", received " + actTextMessage.getText());
          pass = false;
        }
      }

      TestUtil.logMsg(
          "Attempt to Receive TextMessage from consumer2 - there should be none");
      actTextMessage = (TextMessage) consumer2.receive(timeout);
      if (actTextMessage != null) {
        throw new Fault("Did receive TextMessage - unexpected.");
      } else
        TestUtil.logMsg("Did not receive TextMessage - expected.");

      // send "numMessages" messages to Topic
      TestUtil.logMsg("Send " + numMessages + " to Topic");
      for (int i = 1; i <= numMessages; i++) {
        TextMessage tempMsg = session.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            sharedSubscriptionName + i);
        if (i == numMessages) {
          tempMsg.setBooleanProperty("lastMessage", true);
        } else {
          tempMsg.setBooleanProperty("lastMessage", false);
        }
        producer.send(tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      TestUtil.logMsg("Receive TextMessage from consumer2");
      expTextMessage = session.createTextMessage("Message " + numMessages);
      actTextMessage = (TextMessage) consumer2.receive(timeout);
      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg("Check the value in TextMessage");
      if (actTextMessage.getText().equals(expTextMessage.getText())) {
        TestUtil.logMsg("TextMessage is correct");
      } else {
        TestUtil.logErr(
            "TextMessage is incorrect expected " + expTextMessage.getText()
                + ", received " + actTextMessage.getText());
        pass = false;
      }
      TestUtil.logMsg(
          "Attempt to Receive TextMessage from consumer1 - there should be none");
      actTextMessage = (TextMessage) consumer.receive(timeout);
      if (actTextMessage != null) {
        throw new Fault("Did receive TextMessage - unexpected.");
      } else
        TestUtil.logMsg("Did not receive TextMessage - expected.");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      e.printStackTrace();
      throw new Fault("createSharedConsumerTopicTest2", e);
    } finally {
      try {
        if (consumer != null)
          consumer.close();
        if (consumer2 != null)
          consumer2.close();
      } catch (Exception e) {
        TestUtil.logMsg("Ignoring exception closing consumers: " + e);
      }
    }

    if (!pass) {
      throw new Fault("createSharedConsumerTopicTest2 failed");
    }
  }

  /*
   * @testName: createSharedDurableConsumerTopicTest1
   *
   * @assertion_ids: JMS:JAVADOC:1393;
   *
   * @test_Strategy: Creates a shared durable subscription with the specified
   * name on the specified topic and creates a JMSConsumer on that durable
   * subscription.
   *
   * This uses a connection factory WITH client identifier set.
   *
   * Tests the following API method:
   *
   * Session.createSharedDurableConsumer(Topic, String)
   *
   * 1. Create a shared durable subscription with the specified name on the
   * specified topic and create a durable JMSConsumer on that durable
   * subscription. This uses a connection factory WITH client identifier set. 2.
   * Create a 2nd JMSConsumer for it. 3. Send TextMessage (message1) to the
   * Topic. 3. Consume message via 1st JMSConsumer created. Verify message1
   * received. 4. Close 1st consumer. 5. Send another TextMessage (message2) to
   * the Topic. 6. Consume message via 2nd JMSConsumer created. Verify message2
   * received.
   */
  public void createSharedDurableConsumerTopicTest1() throws Fault {
    boolean pass = true;
    String message1 = "Message1!";
    String message2 = "Message2!";
    String durableSubscriptionName = "createSharedDurableConsumerTopicTest1";
    try {
      // set up JmsTool for COMMON_T setup (durable factory has clientid set)
      toolT.getDefaultConnection().close();
      toolT.closeAllResources();
      toolT2 = new JmsTool(JmsTool.COMMON_T, user, password,
          lookupDurableTopicFactory, mode);

      // Setup for Topic
      setupGlobalVarsT2();

      TextMessage expTextMessage = null;

      // Create a shared Durable Subscription and a JMSConsumer for it
      TestUtil.logMsg(
          "Create a shared Durable Subscription and 1st JMSConsumer for it");
      consumer = session.createSharedDurableConsumer(topic,
          durableSubscriptionName);

      // Create 2nd JMSConsumer for it
      TestUtil.logMsg("Create 2nd JMSConsumer for it");
      consumer2 = session.createSharedDurableConsumer(topic,
          durableSubscriptionName);

      TestUtil.logMsg("Send TextMessage message1 to Topic");
      expTextMessage = session.createTextMessage(message1);
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "createSharedDurableConsumerTopicTest1");
      producer.send(expTextMessage);
      TestUtil.logMsg("TextMessage message1 sent");

      TestUtil.logMsg("Receive TextMessage message1 from consumer1");
      TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
      if (actTextMessage == null) {
        TestUtil.logErr("Did not receive TextMessage");
        pass = false;
      } else {
        TestUtil.logMsg("Check the value in TextMessage message1");
        if (actTextMessage.getText().equals(expTextMessage.getText())) {
          TestUtil.logMsg("TextMessage is correct");
        } else {
          TestUtil.logErr(
              "TextMessage is incorrect expected " + expTextMessage.getText()
                  + ", received " + actTextMessage.getText());
          pass = false;
        }
      }
      TestUtil.logMsg("Close 1st shared durable JMSConsumer");
      consumer.close();

      TestUtil.logMsg("Send TextMessage message2 to Topic");
      expTextMessage = session.createTextMessage(message2);
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "createSharedDurableConsumerTopicTest1");
      producer.send(expTextMessage);
      TestUtil.logMsg("TextMessage message2 sent");

      TestUtil.logMsg("Receive TextMessage message2 from consumer2");
      actTextMessage = (TextMessage) consumer2.receive(timeout);
      if (actTextMessage == null) {
        TestUtil.logErr("Did not receive TextMessage");
        pass = false;
      } else {
        TestUtil.logMsg("Check the value in TextMessage message2");
        if (actTextMessage.getText().equals(expTextMessage.getText())) {
          TestUtil.logMsg("TextMessage is correct");
        } else {
          TestUtil.logErr(
              "TextMessage is incorrect expected " + expTextMessage.getText()
                  + ", received " + actTextMessage.getText());
          pass = false;
        }
      }

      TestUtil
          .logMsg("Now there should be no more messages to receive from topic");

      TestUtil
          .logMsg("Recreate Durable Subscription and 1st JMSConsumer for it");
      consumer = session.createSharedDurableConsumer(topic,
          durableSubscriptionName);

      TestUtil
          .logMsg("Try and receive a message from comsumer1 (should get NONE)");
      actTextMessage = (TextMessage) consumer.receive(timeout);
      if (actTextMessage != null) {
        TestUtil.logErr("Consumer1 received a message (FAIL)");
        pass = false;
      } else {
        TestUtil.logMsg("Consumer1 didn't receive a message (PASS)");
      }

      TestUtil
          .logMsg("Try and receive a message from comsumer2 (should get NONE)");
      actTextMessage = (TextMessage) consumer2.receive(timeout);
      if (actTextMessage != null) {
        TestUtil.logErr("Consumer2 received a message (FAIL)");
        pass = false;
        throw new Fault("Consumer2 received a message (FAIL)");
      } else {
        TestUtil.logMsg("Consumer2 didn't receive a message (PASS)");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      e.printStackTrace();
      throw new Fault("createSharedDurableConsumerTopicTest1", e);
    } finally {
      try {
        if (consumer != null)
          consumer.close();
        cleanupSubscription(consumer2, session, durableSubscriptionName);
        toolT2.getDefaultConnection().close();
        toolT2.closeAllResources();
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
      }
    }

    if (!pass) {
      throw new Fault("createSharedDurableConsumerTopicTest1 failed");
    }
  }

  /*
   * @testName: createSharedDurableConsumerTopicTest2
   *
   * @assertion_ids: JMS:JAVADOC:1396;
   *
   * @test_Strategy: Creates a shared durable subscription with the specified
   * name on the specified topic and creates a JMSConsumer on that durable
   * subscription, specifying a message selector and whether messages published
   * by its own connection should be delivered to it.
   *
   * This uses a connection factory WITH client identifier set.
   *
   * Tests the following API method:
   *
   * Session.createSharedDurableConsumer(Topic,String,String)
   *
   * 1. Create a shared durable subscription with the specified name on the
   * specified topic and create a durable JMSConsumer on that durable
   * subscription specifing a message selector and whether messages published by
   * its own connection should be delivered to it. This uses a connection
   * factory WITH client identifier set. 2. Create a 2nd JMSConsumer for it. 3.
   * Send a number of messages to the Topic.
   *
   */
  public void createSharedDurableConsumerTopicTest2() throws Fault {
    boolean pass = true;
    String durableSubscriptionName = "createSharedDurableConsumerTopicTest2";
    try {
      // set up JmsTool for COMMON_T setup (durable factory has clientid set)
      toolT.getDefaultConnection().close();
      toolT.closeAllResources();
      toolT2 = new JmsTool(JmsTool.COMMON_T, user, password,
          lookupDurableTopicFactory, mode);

      // Setup for Topic
      setupGlobalVarsT2();

      // Create a shared Durable Subscription and a JMSConsumer for it
      TestUtil.logMsg(
          "Create shared Durable Subscription and 1st JMSConsumer with message selector");
      consumer = session.createSharedDurableConsumer(topic,
          durableSubscriptionName, "lastMessage = TRUE");
      // Create 2nd JMSConsumer for it
      TestUtil.logMsg("Create 2nd JMSConsumer with message selector");
      consumer2 = session.createSharedDurableConsumer(topic,
          durableSubscriptionName, "lastMessage = TRUE");

      // send "numMessages" messages to Topic
      TestUtil.logMsg("Send " + numMessages + " messages to Topic");
      for (int i = 1; i <= numMessages; i++) {
        TextMessage tempMsg = session.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "createSharedDurableConsumerTopicTest2" + i);
        if (i == numMessages) {
          tempMsg.setBooleanProperty("lastMessage", true);
        } else {
          tempMsg.setBooleanProperty("lastMessage", false);
        }
        producer.send(tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      TestUtil.logMsg("Receive TextMessage from consumer1");
      TextMessage expTextMessage = session
          .createTextMessage("Message " + numMessages);
      TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
      if (actTextMessage == null) {
        TestUtil.logErr("Did not receive TextMessage");
        pass = false;
      } else {
        TestUtil.logMsg("Check the value in TextMessage");
        if (actTextMessage.getText().equals(expTextMessage.getText())) {
          TestUtil.logMsg("TextMessage is correct");
        } else {
          TestUtil.logErr(
              "TextMessage is incorrect expected " + expTextMessage.getText()
                  + ", received " + actTextMessage.getText());
          pass = false;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      TestUtil.logErr("Caught exception: " + e);
      e.printStackTrace();
      throw new Fault("createSharedDurableConsumerTopicTest2", e);
    } finally {
      try {
        if (consumer != null)
          consumer.close();
        cleanupSubscription(consumer2, session, durableSubscriptionName);
        toolT2.getDefaultConnection().close();
        toolT2.closeAllResources();
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
      }
    }

    if (!pass) {
      throw new Fault("createSharedDurableConsumerTopicTest2 failed");
    }
  }

  /*
   * @testName: createSharedDurableConsumerTopicTest3
   *
   * @assertion_ids: JMS:JAVADOC:1393;
   *
   * @test_Strategy: Creates a shared durable subscription with the specified
   * name on the specified topic and creates a JMSConsumer on that durable
   * subscription.
   *
   * This uses a connection factory WITH client identifier unset.
   *
   * Tests the following API method:
   *
   * Session.createSharedDurableConsumer(Topic, String)
   *
   * 1. Create a shared durable subscription with the specified name on the
   * specified topic and create a durable JMSConsumer on that durable
   * subscription. This uses a connection factory WITH client identifier unset.
   * 2. Create a 2nd JMSConsumer for it. 3. Send TextMessage (message1) to the
   * Topic. 3. Consume message via 1st JMSConsumer created. Verify message1
   * received. 4. Close 1st consumer. 5. Send another TextMessage (message2) to
   * the Topic. 6. Consume message via 2nd JMSConsumer created. Verify message2
   * received.
   */
  public void createSharedDurableConsumerTopicTest3() throws Fault {
    boolean pass = true;
    String message1 = "Message1!";
    String message2 = "Message2!";
    String durableSubscriptionName = "createSharedDurableConsumerTopicTest3";
    try {
      // Setup for Topic
      setupGlobalVarsT();

      TextMessage expTextMessage = null;

      // Create a shared Durable Subscription and a JMSConsumer for it
      TestUtil.logMsg(
          "Create a shared Durable Subscription and 1st JMSConsumer for it");
      consumer = session.createSharedDurableConsumer(topic,
          durableSubscriptionName);

      // Create 2nd JMSConsumer for it
      TestUtil.logMsg("Create 2nd JMSConsumer for it");
      consumer2 = session.createSharedDurableConsumer(topic,
          durableSubscriptionName);

      TestUtil.logMsg("Send TextMessage message1 to Topic");
      expTextMessage = session.createTextMessage(message1);
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "createSharedDurableConsumerTopicTest3");
      producer.send(expTextMessage);
      TestUtil.logMsg("TextMessage message1 sent");

      TestUtil.logMsg("Receive TextMessage message1 from consumer1");
      TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
      if (actTextMessage == null) {
        TestUtil.logErr("Did not receive TextMessage");
        pass = false;
      } else {
        TestUtil.logMsg("Check the value in TextMessage message1");
        if (actTextMessage.getText().equals(expTextMessage.getText())) {
          TestUtil.logMsg("TextMessage is correct");
        } else {
          TestUtil.logErr(
              "TextMessage is incorrect expected " + expTextMessage.getText()
                  + ", received " + actTextMessage.getText());
          pass = false;
        }
      }
      TestUtil.logMsg("Close 1st shared durable JMSConsumer");
      consumer.close();

      TestUtil.logMsg("Send TextMessage message2 to Topic");
      expTextMessage = session.createTextMessage(message2);
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "createSharedDurableConsumerTopicTest3");
      producer.send(expTextMessage);
      TestUtil.logMsg("TextMessage message2 sent");

      TestUtil.logMsg("Receive TextMessage message2 from consumer2");
      actTextMessage = (TextMessage) consumer2.receive(timeout);
      if (actTextMessage == null) {
        TestUtil.logErr("Did not receive TextMessage");
        pass = false;
      } else {
        TestUtil.logMsg("Check the value in TextMessage message2");
        if (actTextMessage.getText().equals(expTextMessage.getText())) {
          TestUtil.logMsg("TextMessage is correct");
        } else {
          TestUtil.logErr(
              "TextMessage is incorrect expected " + expTextMessage.getText()
                  + ", received " + actTextMessage.getText());
          pass = false;
        }
      }

      TestUtil
          .logMsg("Now there should be no more messages to receive from topic");

      TestUtil
          .logMsg("Recreate Durable Subscription and 1st JMSConsumer for it");
      consumer = session.createSharedDurableConsumer(topic,
          durableSubscriptionName);

      TestUtil
          .logMsg("Try and receive a message from comsumer1 (should get NONE)");
      actTextMessage = (TextMessage) consumer.receive(timeout);
      if (actTextMessage != null) {
        TestUtil.logErr("Consumer1 received a message (FAIL)");
        pass = false;
      } else {
        TestUtil.logMsg("Consumer1 didn't receive a message (PASS)");
      }

      TestUtil
          .logMsg("Try and receive a message from comsumer2 (should get NONE)");
      actTextMessage = (TextMessage) consumer2.receive(timeout);
      if (actTextMessage != null) {
        TestUtil.logErr("Consumer2 received a message (FAIL)");
        pass = false;
      } else {
        TestUtil.logMsg("Consumer2 didn't receive a message (PASS)");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      e.printStackTrace();
      throw new Fault("createSharedDurableConsumerTopicTest3", e);
    } finally {
      try {
        if (consumer != null)
          consumer.close();
        cleanupSubscription(consumer2, session, durableSubscriptionName);
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("createSharedDurableConsumerTopicTest3 failed");
    }
  }

  /*
   * @testName: invalidDestinationExceptionTests
   *
   * @assertion_ids: JMS:JAVADOC:1089; JMS:JAVADOC:1092; JMS:JAVADOC:643;
   * JMS:JAVADOC:644; JMS:JAVADOC:1165; JMS:JAVADOC:1169; JMS:JAVADOC:1395;
   * JMS:JAVADOC:1398;
   *
   * @test_Strategy: Pass an invalid topic and test for
   * InvalidDestinationException.
   *
   * Session.createDurableSubscriber(Topic, String)
   * Session.createDurableSubscriber(Topic, String, String, boolean)
   * Session.createDurableConsumer(Topic, String)
   * Session.createDurableConsumer(Topic, String, String, boolean)
   * Session.createSharedConsumer(Topic, String)
   * Session.createSharedConsumer(Topic, String, String)
   * Session.createSharedDurableConsumer(Topic, String)
   * Session.createSharedDurableConsumer(Topic, String, String)
   */
  public void invalidDestinationExceptionTests() throws Fault {
    boolean pass = true;
    Destination invalidDestination = null;
    Topic invalidTopic = null;
    try {
      // set up JmsTool for COMMON_T setup (durable factory has clientid set)
      toolT.getDefaultConnection().close();
      toolT.closeAllResources();
      toolT2 = new JmsTool(JmsTool.COMMON_T, user, password,
          lookupDurableTopicFactory, mode);

      // Setup for Topic
      setupGlobalVarsT2();

      TestUtil.logMsg(
          "Testing Session.createDurableSubscriber(Topic, String) for InvalidDestinationException");
      try {
        TestUtil.logMsg(
            "Calling Session.createDurableSubscriber(Topic, String) -> expect InvalidDestinationException");
        session.createDurableSubscriber(invalidTopic,
            "InvalidDestinationException");
      } catch (InvalidDestinationException e) {
        TestUtil.logMsg("Got InvalidDestinationException as expected.");
      } catch (Exception e) {
        TestUtil.logErr("Expected InvalidDestinationException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing Session.createDurableSubscriber(Topic, String, String, boolean) for InvalidDestinationException");
      try {
        TestUtil.logMsg(
            "Calling Session.createDurableSubscriber(Topic, String, String, boolean) -> expect InvalidDestinationException");
        session.createDurableSubscriber(invalidTopic,
            "InvalidDestinationException", "lastMessage = TRUE", false);
      } catch (InvalidDestinationException e) {
        TestUtil.logMsg("Got InvalidDestinationException as expected.");
      } catch (Exception e) {
        TestUtil.logErr("Expected InvalidDestinationException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing Session.createDurableConsumer(Topic, String) for InvalidDestinationException");
      try {
        TestUtil.logMsg(
            "Calling Session.createDurableConsumer(Topic, String) -> expect InvalidDestinationException");
        session.createDurableConsumer(invalidTopic,
            "InvalidDestinationException");
      } catch (InvalidDestinationException e) {
        TestUtil.logMsg("Got InvalidDestinationException as expected.");
      } catch (Exception e) {
        TestUtil.logErr("Expected InvalidDestinationException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing Session.createDurableConsumer(Topic, String, String, boolean) for InvalidDestinationException");
      try {
        TestUtil.logMsg(
            "Calling Session.createDurableConsumer(Topic, String, String, boolean) -> expect InvalidDestinationException");
        session.createDurableConsumer(invalidTopic,
            "InvalidDestinationException", "lastMessage = TRUE", false);
      } catch (InvalidDestinationException e) {
        TestUtil.logMsg("Got InvalidDestinationException as expected.");
      } catch (Exception e) {
        TestUtil.logErr("Expected InvalidDestinationException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing Session.createSharedConsumer(Topic, String) for InvalidDestinationException");
      try {
        TestUtil.logMsg(
            "Calling Session.createSharedConsumer(Topic, String) for InvalidDestinationException");
        session.createSharedConsumer(invalidTopic,
            "InvalidDestinationException");
      } catch (InvalidDestinationException e) {
        TestUtil.logMsg("Got InvalidDestinationException as expected.");
      } catch (Exception e) {
        TestUtil.logErr("Expected InvalidDestinationException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing Session.createSharedConsumer(Topic, String, String) for InvalidDestinationException");
      try {
        TestUtil.logMsg(
            "Calling Session.createSharedConsumer(Topic, String, String) for InvalidDestinationException");
        session.createSharedConsumer(invalidTopic,
            "InvalidDestinationException", "lastMessage = TRUE");
      } catch (InvalidDestinationException e) {
        TestUtil.logMsg("Got InvalidDestinationException as expected.");
      } catch (Exception e) {
        TestUtil.logErr("Expected InvalidDestinationException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing Session.createSharedDurableConsumer(Topic, String) for InvalidDestinationException");
      try {
        TestUtil.logMsg(
            "Calling Session.createSharedDurableConsumer(Topic, String) -> expect InvalidDestinationException");
        session.createSharedDurableConsumer(invalidTopic,
            "InvalidDestinationException");
      } catch (InvalidDestinationException e) {
        TestUtil.logMsg("Got InvalidDestinationException as expected.");
      } catch (Exception e) {
        TestUtil.logErr("Expected InvalidDestinationException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing Session.createSharedDurableConsumer(Topic, String, String) for InvalidDestinationException");
      try {
        TestUtil.logMsg(
            "Calling Session.createSharedDurableConsumer(Topic, String, String) -> expect InvalidDestinationException");
        session.createSharedDurableConsumer(invalidTopic,
            "InvalidDestinationException", "lastMessage = TRUE");
      } catch (InvalidDestinationException e) {
        TestUtil.logMsg("Got InvalidDestinationException as expected.");
      } catch (Exception e) {
        TestUtil.logErr("Expected InvalidDestinationException, received " + e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("invalidDestinationExceptionTests", e);
    }

    if (!pass) {
      throw new Fault("invalidDestinationExceptionTests failed");
    }
  }

  /*
   * @testName: invalidSelectorExceptionTopicTests
   *
   * @assertion_ids: JMS:JAVADOC:1093; JMS:JAVADOC:1399; JMS:JAVADOC:1166;
   * JMS:JAVADOC:1170;
   *
   * @test_Strategy: Pass an invalid selector and test for
   * InvalidSelectorException.
   *
   * Session.createDurableConsumer(Topic, String, String, boolean)
   * Session.createSharedConsumer(Topic, String, String)
   * Session.createSharedDurableConsumer(Topic, String, String)
   */
  public void invalidSelectorExceptionTopicTests() throws Fault {
    boolean pass = true;
    String invalidMessageSelector = "=TEST 'test'";
    try {
      // set up JmsTool for COMMON_T setup (durable factory has clientid set)
      toolT.getDefaultConnection().close();
      toolT.closeAllResources();
      toolT2 = new JmsTool(JmsTool.COMMON_T, user, password,
          lookupDurableTopicFactory, mode);

      // Setup for Topic
      setupGlobalVarsT2();

      TestUtil.logMsg(
          "Testing Session.createDurableConsumer(Topic, String, String, boolean) for InvalidSelectorException");
      try {
        TestUtil.logMsg(
            "Calling Session.createDurableConsumer(Topic, String, String, boolean) -> expect InvalidSelectorException");
        session.createDurableConsumer(topic, "InvalidSelectorException",
            invalidMessageSelector, false);
      } catch (InvalidSelectorException e) {
        TestUtil.logMsg("Got InvalidSelectorException as expected.");
      } catch (Exception e) {
        TestUtil.logErr("Expected InvalidSelectorException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing Session.createSharedConsumer(Topic, String, String) for InvalidSelectorException");
      try {
        TestUtil.logMsg(
            "Calling Session.createSharedConsumer(Topic, String, String) for InvalidSelectorException");
        session.createSharedConsumer(topic, "InvalidSelectorException",
            invalidMessageSelector);
      } catch (InvalidSelectorException e) {
        TestUtil.logMsg("Got InvalidSelectorException as expected.");
      } catch (Exception e) {
        TestUtil.logErr("Expected InvalidSelectorException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing Session.createSharedDurableConsumer(Topic, String, String) for InvalidSelectorException");
      try {
        TestUtil.logMsg(
            "Calling Session.createSharedDurableConsumer(Topic, String, String) -> expect InvalidSelectorException");
        session.createSharedDurableConsumer(topic, "InvalidSelectorException",
            invalidMessageSelector);
      } catch (InvalidSelectorException e) {
        TestUtil.logMsg("Got InvalidSelectorException as expected.");
      } catch (Exception e) {
        TestUtil.logErr("Expected InvalidSelectorException, received " + e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("invalidSelectorExceptionTopicTests", e);
    }

    if (!pass) {
      throw new Fault("invalidSelectorExceptionTopicTests failed");
    }
  }

  /*
   * @testName: illegalStateExceptionTests
   * 
   * @assertion_ids: JMS:SPEC:185.9; JMS:SPEC:185.10; JMS:SPEC:185;
   * JMS:JAVADOC:1349; JMS:JAVADOC:1350; JMS:JAVADOC:1390; JMS:JAVADOC:1391;
   *
   * @test_Strategy: Create a QueueSession and call Topic specific methods
   * inherited from Session, and verify that javax.jms.IllegalStateException is
   * thrown.
   * 
   * Call the following topic methods on a QueueSession:
   * 
   * QueueSession.createDurableConsumer(Topic, String)
   * QueueSession.createDurableConsumer(Topic, String, String, boolean)
   * QueueSession.createSharedConsumer(Topic, String)
   * QueueSession.createSharedConsumer(Topic, String, String)
   * QueueSession.createSharedDurableConsumer(Topic, String)
   * QueueSession.createSharedDurableConsumer(Topic, String, String)
   *
   * Test javax.jms.IllegalStateException from the following API's. Also test
   * when nolocal=true and client id is not set.
   *
   * Session.createDurableSubscriber(Topic, String)
   * Session.createDurableSubscriber(Topic, String, String, boolean)
   * Session.createDurableConsumer(Topic, String)
   * Session.createDurableConsumer(Topic, String, String, boolean)
   */
  public void illegalStateExceptionTests() throws Fault {
    JmsTool toolq = null;
    String lookupNormalTopicFactory = "MyTopicConnectionFactory";
    boolean pass = true;
    MessageConsumer consumer = null;
    TopicSubscriber subscriber = null;
    try {
      // Set up JmsTool for Queue setup
      toolq = new JmsTool(JmsTool.QUEUE, user, password, mode);

      Destination destination = toolT.getDefaultDestination();
      Topic topic = (Topic) destination;
      try {
        TestUtil.logMsg(
            "Calling QueueSession.createDurableConsumer(Topic, String)");
        toolq.getDefaultQueueSession().createDurableConsumer(topic, "mySub1");
        pass = false;
        TestUtil.logErr("QueueSession.createDurableConsumer(Topic, String) "
            + "didn't throw expected IllegalStateException.");
      } catch (javax.jms.IllegalStateException ex) {
        TestUtil.logMsg("Got expected IllegalStateException from "
            + "QueueSession.createDurableConsumer(Topic, String)");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        pass = false;
      }

      try {
        TestUtil.logMsg(
            "Calling QueueSession.createDurableConsumer(Topic, String, String, boolean)");
        toolq.getDefaultQueueSession().createDurableConsumer(topic, "mySub1",
            "TEST = 'test'", false);
        pass = false;
        TestUtil.logErr(
            "QueueSession.createDurableConsumer(Topic, String, String, boolean) "
                + "didn't throw expected IllegalStateException.");
      } catch (javax.jms.IllegalStateException ex) {
        TestUtil.logMsg("Got expected IllegalStateException from "
            + "QueueSession.createDurableConsumer(Topic, String, String, boolean)");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        pass = false;
      }

      try {
        TestUtil
            .logMsg("Calling QueueSession.createSharedConsumer(Topic, String)");
        toolq.getDefaultQueueSession().createSharedConsumer(topic, "mySub1");
        pass = false;
        TestUtil.logErr("QueueSession.createSharedConsumer(Topic, String) "
            + "didn't throw expected IllegalStateException.");
      } catch (javax.jms.IllegalStateException ex) {
        TestUtil.logMsg("Got expected IllegalStateException from "
            + "QueueSession.createSharedConsumer(Topic, String)");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        pass = false;
      }

      try {
        TestUtil.logMsg(
            "Calling QueueSession.createSharedConsumer(Topic, String, String)");
        toolq.getDefaultQueueSession().createSharedConsumer(topic, "mySub1",
            "TEST = 'test'");
        pass = false;
        TestUtil
            .logErr("QueueSession.createSharedConsumer(Topic, String, String) "
                + "didn't throw expected IllegalStateException.");
      } catch (javax.jms.IllegalStateException ex) {
        TestUtil.logMsg("Got expected IllegalStateException from "
            + "QueueSession.createSharedConsumer(Topic, String, String)");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        pass = false;
      }

      try {
        TestUtil.logMsg(
            "Calling QueueSession.createSharedDurableConsumer(Topic, String)");
        toolq.getDefaultQueueSession().createSharedDurableConsumer(topic,
            "mySub1");
        pass = false;
        TestUtil
            .logErr("QueueSession.createSharedDurableConsumer(Topic, String) "
                + "didn't throw expected IllegalStateException.");
      } catch (javax.jms.IllegalStateException ex) {
        TestUtil.logMsg("Got expected IllegalStateException from "
            + "QueueSession.createSharedDurableConsumer(Topic, String)");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        pass = false;
      }

      try {
        TestUtil.logMsg(
            "Calling QueueSession.createSharedDurableConsumer(Topic, String, String)");
        toolq.getDefaultQueueSession().createSharedDurableConsumer(topic,
            "mySub1", "TEST = 'test'");
        pass = false;
        TestUtil.logErr(
            "QueueSession.createSharedDurableConsumer(Topic, String, String) "
                + "didn't throw expected IllegalStateException.");
      } catch (javax.jms.IllegalStateException ex) {
        TestUtil.logMsg("Got expected IllegalStateException from "
            + "QueueSession.createSharedDurableConsumer(Topic, String, String)");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        pass = false;
      }

      try {
        TestUtil.logMsg(
            "Create a Durable Subscriber with clientid unset (expect IllegalStateException");
        TestUtil
            .logMsg("Calling Session.createDurableSubscriber(Topic, String)");
        subscriber = toolT.getDefaultSession().createDurableSubscriber(topic,
            "mySub1");
        pass = false;
        TestUtil.logErr("Session.createDurableSubscriber(Topic, String) "
            + "didn't throw expected IllegalStateException.");
        try {
          subscriber.close();
          toolT.getDefaultSession().unsubscribe("mySub1");
        } catch (Exception e) {
        }
      } catch (javax.jms.IllegalStateException ex) {
        TestUtil.logMsg("Got expected IllegalStateException from "
            + "Session.createDurableSubscriber(Topic, String)");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        pass = false;
      }

      try {
        TestUtil.logMsg(
            "Create a Durable Subscriber with noLocal=true and clientid unset (expect IllegalStateException");
        TestUtil.logMsg(
            "Calling Session.createDurableSubscriber(Topic, String, String, boolean)");
        subscriber = toolT.getDefaultSession().createDurableSubscriber(topic,
            "mySub1", "TEST = 'test'", true);
        pass = false;
        TestUtil.logErr(
            "Session.createDurableSubscriber(Topic, String, String, boolean) "
                + "didn't throw expected IllegalStateException.");
        try {
          subscriber.close();
          toolT.getDefaultSession().unsubscribe("mySub1");
        } catch (Exception e) {
        }
      } catch (javax.jms.IllegalStateException ex) {
        TestUtil.logMsg("Got expected IllegalStateException from "
            + "Session.createDurableSubscriber(Topic, String, String, boolean)");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        pass = false;
      }

      try {
        TestUtil.logMsg(
            "Create a Durable Consumer with clientid unset (expect IllegalStateException");
        TestUtil.logMsg("Calling Session.createDurableConsumer(Topic, String)");
        consumer = toolT.getDefaultSession().createDurableConsumer(topic,
            "mySub1");
        pass = false;
        TestUtil.logErr("Session.createDurableConsumer(Topic, String) "
            + "didn't throw expected IllegalStateException.");
        try {
          consumer.close();
          toolT.getDefaultSession().unsubscribe("mySub1");
        } catch (Exception e) {
        }
      } catch (javax.jms.IllegalStateException ex) {
        TestUtil.logMsg("Got expected IllegalStateException from "
            + "Session.createDurableConsumer(Topic, String)");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        pass = false;
      }

      try {
        TestUtil.logMsg(
            "Create a Durable Consumer with noLocal=true and clientid unset (expect IllegalStateException");
        TestUtil.logMsg(
            "Calling Session.createDurableConsumer(Topic, String, String, boolean)");
        consumer = toolT.getDefaultSession().createDurableConsumer(topic,
            "mySub1", "TEST = 'test'", true);
        pass = false;
        TestUtil.logErr(
            "Session.createDurableConsumer(Topic, String, String, boolean) "
                + "didn't throw expected IllegalStateException.");
        try {
          consumer.close();
          toolT.getDefaultSession().unsubscribe("mySub1");
        } catch (Exception e) {
        }
      } catch (javax.jms.IllegalStateException ex) {
        TestUtil.logMsg("Got expected IllegalStateException from "
            + "Session.createDurableConsumer(Topic, String, String, boolean)");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      throw new Fault("illegalStateExceptionTests Failed");
    } finally {
      try {
        toolq.getDefaultQueueConnection().close();
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        pass = false;
      }
    }

    if (!pass) {
      throw new Fault("illegalStateExceptionTests Failed");
    }
  }

  /*
   * @testName: jMSExceptionTests
   * 
   * @assertion_ids: JMS:JAVADOC:1091; JMS:JAVADOC:257; JMS:JAVADOC:1090;
   * JMS:JAVADOC:1164; JMS:JAVADOC:1168; JMS:JAVADOC:1394; JMS:JAVADOC:1397;
   *
   * @test_Strategy: Test JMSException conditions from various API methods.
   *
   * TopicSession.createDurableSubscriber(Topic, String)
   * TopicSession.createDurableSubscriber(Topic, String. String, boolean)
   * Session.createDurableSubscriber(Topic, String)
   * Session.createDurableSubscriber(Topic, String. String, boolean)
   * Session.createDurableConsumer(Topic, String)
   * Session.createDurableConsumer(Topic, String. String, boolean)
   * Session.createSharedConsumer(Topic, String)
   * Session.createSharedConsumer(Topic, String, String)
   * Session.createSharedDurableConsumer(Topic, String)
   * Session.createSharedDurableConsumer(Topic, String. String)
   */
  public void jMSExceptionTests() throws Fault {
    boolean pass = true;
    JmsTool toolt = null;
    Topic mytopic = null;
    try {
      // Setup for Topic
      setupGlobalVarsT();

      // Create second Topic
      mytopic = (Topic) toolT.createNewTopic("MY_TOPIC2");

      // set up JmsTool for TOPIC setup (normal factory no clientid set)
      toolt = new JmsTool(JmsTool.TOPIC, user, password,
          lookupNormalTopicFactory, mode);

      try {
        TestUtil.logMsg(
            "Create a Durable TopicSubscriber with noLocal=true and clientid unset (expect JMSException");
        TestUtil.logMsg(
            "Calling TopicSession.createDurableSubscriber(Topic, String, String, boolean)");
        subscriber = toolt.getDefaultTopicSession()
            .createDurableSubscriber(topic, "mySub1", "TEST = 'test'", true);
        pass = false;
        TestUtil.logErr(
            "TopicSession.createDurableSubscriber(Topic, String, String, boolean) "
                + "didn't throw expected JMSException.");
      } catch (javax.jms.JMSException ex) {
        TestUtil.logMsg("Got expected JMSException from "
            + "TopicSession.createDurableSubscriber(Topic, String, String, boolean)");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        pass = false;
      } finally {
        try {
          if (subscriber != null)
            subscriber.close();
          toolt.getDefaultTopicConnection().close();
        } catch (Exception e) {
          TestUtil.logErr("Caught unexpected exception: " + e);
          pass = false;
        }
      }

      try {
        TestUtil.logMsg(
            "Create a Durable Consumer with noLocal=true and clientid unset (expect JMSException");
        TestUtil.logMsg(
            "Calling Session.createDurableConsumer(Topic, String, String, boolean)");
        consumer = null;
        consumer = session.createDurableConsumer(topic, "mySub1",
            "TEST = 'test'", true);
        pass = false;
        TestUtil.logErr(
            "Session.createDurableConsumer(Topic, String, String, boolean) "
                + "didn't throw expected JMSException.");
      } catch (javax.jms.JMSException ex) {
        TestUtil.logMsg("Got expected JMSException from "
            + "Session.createDurableConsumer(Topic, String, String, boolean)");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        pass = false;
      } finally {
        try {
          if (consumer != null)
            consumer.close();
        } catch (Exception e) {
          TestUtil.logErr("Caught unexpected exception: " + e);
          pass = false;
        }
      }

      try {
        TestUtil.logMsg(
            "Create a Durable TopicSubscriber with noLocal=true and clientid unset (expect JMSException");
        TestUtil.logMsg(
            "Calling Session.createDurableSubscriber(Topic, String, String, boolean)");
        subscriber = null;
        subscriber = session.createDurableSubscriber(topic, "mySub1",
            "TEST = 'test'", true);
        pass = false;
        TestUtil.logErr(
            "Session.createDurableSubscriber(Topic, String, String, boolean) "
                + "didn't throw expected JMSException.");
      } catch (javax.jms.JMSException ex) {
        TestUtil.logMsg("Got expected JMSException from "
            + "Session.createDurableSubscriber(Topic, String, String, boolean)");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        pass = false;
      } finally {
        try {
          if (subscriber != null)
            subscriber.close();
        } catch (Exception e) {
          TestUtil.logErr("Caught unexpected exception: " + e);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      throw new Fault("jMSExceptionTests Failed");
    }

    try {
      // Setup for Durable Topic
      toolT.getDefaultConnection().close();
      toolT.closeAllResources();
      // set up JmsTool for COMMON_T setup (durable factory has clientid set)
      toolT2 = new JmsTool(JmsTool.COMMON_T, user, password,
          lookupDurableTopicFactory, mode);
      setupGlobalVarsT2();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      throw new Fault("jMSExceptionTests Failed");
    }

    // Create durable subscriber specifying topic and name
    // Create second durable subscriber with same name but specifying different
    // topic,
    // Verify JMSException is thrown.
    try {
      TestUtil.logMsg("Create durable subscriber");
      TestUtil.logMsg("Calling Session.createDurableSubscriber(Topic, String)");
      subscriber = subscriber2 = null;
      subscriber = session.createDurableSubscriber(topic,
          "dummySubSCJMSException");
      TestUtil.logMsg(
          "Create second durable subscriber with same name but different topic");
      TestUtil.logMsg("Calling Session.createDurableSubscriber(Topic, String)");
      subscriber2 = session.createDurableSubscriber(mytopic,
          "dummySubSCJMSException");
      TestUtil.logMsg("Verify that JMSException is thrown");
      pass = false;
      TestUtil.logErr("Didn't throw expected JMSException");
    } catch (JMSException ex) {
      TestUtil.logMsg("Got expected JMSException from "
          + "Session.createDurableSubscriber(Topic, String)");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      pass = false;
    } finally {
      try {
        if (subscriber != null)
          subscriber.close();
        if (subscriber2 != null)
          subscriber2.close();
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
      }
    }

    // Create durable subscriber specifying topic, name, message selector and
    // nolcal
    // Create second durable subscriber with same name but specifying different
    // topic
    // message selector, or nolocal. Verify JMSException is thrown.
    try {
      TestUtil.logMsg("Create durable subscriber");
      TestUtil.logMsg(
          "Calling Session.createDurableSubscriber(Topic, String, String, boolean)");
      subscriber = subscriber2 = null;
      subscriber = session.createDurableSubscriber(topic,
          "dummySubSCJMSException", "TEST = 'test'", false);
      TestUtil.logMsg(
          "Create second durable subscriber with same name but different topic");
      TestUtil.logMsg(
          "Calling Session.createDurableSubscriber(Topic, String, String, boolean)");
      subscriber2 = session.createDurableSubscriber(mytopic,
          "dummySubSCJMSException", "TEST = 'test'", false);
      TestUtil.logMsg("Verify that JMSException is thrown");
      pass = false;
      TestUtil.logErr("Didn't throw expected JMSException");
    } catch (JMSException ex) {
      TestUtil.logMsg("Got expected JMSException from "
          + "Session.createDurableSubscriber(Topic, String, String, boolean)");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      pass = false;
    } finally {
      try {
        if (subscriber != null)
          subscriber.close();
        if (subscriber2 != null)
          subscriber2.close();
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
      }
    }

    // Create shared consumer specifying topic and name
    // Create second shared consumer with same name but specifying different
    // topic,
    // Verify JMSException is thrown.
    try {
      TestUtil.logMsg("Create shared consumer");
      TestUtil.logMsg("Calling Session.createSharedConsumer(Topic, String)");
      consumer = consumer2 = null;
      consumer = session.createSharedConsumer(topic, "dummySubSCJMSException");
      TestUtil.logMsg(
          "Create second shared consumer with same name but different topic");
      TestUtil.logMsg("Calling Session.createSharedConsumer(Topic, String)");
      consumer2 = session.createSharedConsumer(mytopic,
          "dummySubSCJMSException");
      TestUtil.logMsg("Verify that JMSException is thrown");
      pass = false;
      TestUtil.logErr("Didn't throw expected JMSException");
    } catch (JMSException ex) {
      TestUtil.logMsg("Got expected JMSException from "
          + "Session.createSharedConsumer(Topic, String)");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      pass = false;
    } finally {
      try {
        if (consumer != null)
          consumer.close();
        if (consumer2 != null)
          consumer2.close();
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
      }
    }

    // Create shared consumer specifying topic and name and message selector
    // Create second shared consumer with same name but specifying different
    // topic and message selector
    // Verify JMSException is thrown.
    try {
      TestUtil.logMsg("Create shared consumer");
      TestUtil.logMsg(
          "Calling Session.createSharedConsumer(Topic, String, String)");
      consumer = consumer2 = null;
      consumer = session.createSharedConsumer(topic, "dummySubSCJMSException",
          "TEST = 'test'");
      TestUtil.logMsg(
          "Create second shared consumer with same name but different topic");
      TestUtil.logMsg(
          "Calling Session.createSharedConsumer(Topic, String, String)");
      consumer2 = session.createSharedConsumer(mytopic,
          "dummySubSCJMSException", "TEST = 'test'");
      TestUtil.logMsg("Verify that JMSException is thrown");
      pass = false;
      TestUtil.logErr("Didn't throw expected JMSException");
    } catch (JMSException ex) {
      TestUtil.logMsg("Got expected JMSException from "
          + "Session.createSharedConsumer(Topic, String, String)");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      pass = false;
    } finally {
      try {
        if (consumer != null)
          consumer.close();
        if (consumer2 != null)
          consumer2.close();
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
      }
    }

    // Create shared durable subscription specifying topic, name. selector.
    // Create second shared durable subscription with same name but specifying
    // different topic,
    // selector. Verify JMSException is thrown.
    try {
      TestUtil.logMsg("Create shared durable subscription");
      TestUtil.logMsg(
          "Calling Session.createSharedDurableConsumer(Topic, String, String)");
      consumer = consumer2 = null;
      consumer = session.createSharedDurableConsumer(topic,
          "dummySubSJMSException", "TEST = 'test'");
      TestUtil.logMsg(
          "Create second shared durable subscription with same name but different other args");
      TestUtil.logMsg(
          "Calling Session.createSharedDurableConsumer(Topic, String, String)");
      consumer2 = session.createSharedDurableConsumer(mytopic,
          "dummySubSJMSException", "TEST = 'test2'");
      TestUtil.logMsg("Verify that JMSException is thrown");
      pass = false;
      TestUtil.logErr("Didn't throw expected JMSException");
    } catch (JMSException ex) {
      TestUtil.logMsg("Got expected JMSException from "
          + "Session.createSharedDurableConsumer(Topic, String, String)");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      pass = false;
    } finally {
      try {
        if (consumer2 != null)
          consumer2.close();
        cleanupSubscription(consumer, session, "dummySubSJMSException");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
      }
    }

    // Create durable subscription specifying topic and name
    // Create second durable subscription with same name but specifying
    // different topic,
    // Verify JMSException is thrown.
    try {
      TestUtil.logMsg("Create durable subscription");
      TestUtil.logMsg("Calling Session.createDurableConsumer(Topic, String)");
      consumer = consumer2 = null;
      consumer = session.createDurableConsumer(topic, "dummySubDJMSException");
      TestUtil.logMsg(
          "Create second durable subscription with same name but different topic");
      TestUtil.logMsg("Calling Session.createDurableConsumer(Topic, String)");
      consumer2 = session.createDurableConsumer(mytopic,
          "dummySubDJMSException");
      TestUtil.logMsg("Verify that JMSException is thrown");
      pass = false;
      TestUtil.logErr("Didn't throw expected JMSException");
    } catch (JMSException ex) {
      TestUtil.logMsg("Got expected JMSException from "
          + "Session.createDurableConsumer(Topic, String)");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      pass = false;
    } finally {
      try {
        if (consumer2 != null)
          consumer2.close();
        cleanupSubscription(consumer, session, "dummySubDJMSException");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
      }
    }

    // Create shared durable subscription specifying topic and name
    // Create second shared durable subscription with same name but specifying
    // different topic,
    // Verify JMSException is thrown.
    try {
      TestUtil.logMsg("Create shared durable subscription");
      TestUtil
          .logMsg("Calling Session.createSharedDurableConsumer(Topic, String)");
      consumer = consumer2 = null;
      consumer = session.createSharedDurableConsumer(topic,
          "dummySubSDJMSException");
      TestUtil.logMsg(
          "Create second shared durable subscription with same name but different topic");
      TestUtil
          .logMsg("Calling Session.createSharedDurableConsumer(Topic, String)");
      consumer2 = session.createSharedDurableConsumer(mytopic,
          "dummySubSDJMSException");
      TestUtil.logMsg("Verify that JMSException is thrown");
      pass = false;
      TestUtil.logErr("Didn't throw expected JMSException");
    } catch (JMSException ex) {
      TestUtil.logMsg("Got expected JMSException from "
          + "Session.createSharedDurableConsumer(Topic, String)");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      pass = false;
    } finally {
      try {
        if (consumer2 != null)
          consumer2.close();
        cleanupSubscription(consumer, session, "dummySubSDJMSException");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
      }
    }

    // Create durable subscription specifying topic, name. selector, and nolocal
    // value.
    // Create second durable subscription with same name but specifying
    // different topic,
    // selector, or nolocal value. Verify JMSException is thrown.
    try {
      TestUtil.logMsg("Create durable subscription");
      TestUtil.logMsg(
          "Calling Session.createDurableConsumer(Topic, String, String, boolean)");
      consumer = consumer2 = null;
      consumer = session.createDurableConsumer(topic, "dummySubDJMSException",
          "TEST = 'test'", false);
      TestUtil.logMsg(
          "Create second durable subscription with same name but different other args");
      TestUtil.logMsg(
          "Calling Session.createDurableConsumer(Topic, String, String, boolean)");
      consumer2 = session.createDurableConsumer(mytopic,
          "dummySubDJMSException", "TEST = 'test2'", false);
      TestUtil.logMsg("Verify that JMSException is thrown");
      pass = false;
      TestUtil.logErr("Didn't throw expected JMSException");
    } catch (JMSException ex) {
      TestUtil.logMsg("Got expected JMSException from "
          + "Session.createDurableConsumer(Topic, String, String, boolean)");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      pass = false;
    } finally {
      try {
        if (consumer2 != null)
          consumer2.close();
        cleanupSubscription(consumer, session, "dummySubDJMSException");
        toolT.getDefaultConnection().close();
        toolT.closeAllResources();
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
      }
    }

    if (!pass) {
      throw new Fault("jMSExceptionTests Failed");
    }
  }

  /*
   * @testName: sendAndRecvMsgsOfEachMsgTypeQueueTest
   *
   * @assertion_ids: JMS:JAVADOC:209; JMS:JAVADOC:212; JMS:JAVADOC:213;
   * JMS:JAVADOC:215; JMS:JAVADOC:217; JMS:JAVADOC:219; JMS:JAVADOC:221;
   * JMS:JAVADOC:223; JMS:JAVADOC:242; JMS:JAVADOC:317; JMS:JAVADOC:504;
   * JMS:JAVADOC:510;
   *
   * @test_Strategy: Send and receive messages of each message type: Message,
   * BytesMessage, MapMessage, ObjectMessage, StreamMessage, TextMessage. Tests
   * the following API's
   *
   * ConnectionFactory.createConnection(String, String)
   * Connection.createSession(boolean, int) Session.createMessage()
   * Session.createBytesMessage() Session.createMapMessage()
   * Session.createObjectMessage() Session.createObjectMessage(Serializable
   * object) Session.createStreamMessage() Session.createTextMessage()
   * Session.createTextMessage(String) Session.createConsumer(Destination)
   * Session.createProducer(Destination) MessageProducer.send(Message)
   * MessageConsumer.receive(long timeout)
   */
  public void sendAndRecvMsgsOfEachMsgTypeQueueTest() throws Fault {
    boolean pass = true;
    try {
      // Setup for Queue
      setupGlobalVarsQ();

      // send and receive Message
      TestUtil.logMsg("Send Message");
      Message msg = session.createMessage();
      TestUtil.logMsg("Set some values in Message");
      msg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendAndRecvMsgsOfEachMsgTypeQueueTest");
      msg.setBooleanProperty("booleanProperty", true);
      producer.send(msg);
      TestUtil.logMsg("Receive Message");
      Message msgRecv = consumer.receive(timeout);
      if (msgRecv == null) {
        throw new Fault("Did not receive Message");
      }
      TestUtil.logMsg("Check the values in Message");
      if (msgRecv.getBooleanProperty("booleanProperty") == true) {
        TestUtil.logMsg("booleanproperty is correct");
      } else {
        TestUtil.logMsg("booleanproperty is incorrect");
        pass = false;
      }

      // send and receive BytesMessage
      TestUtil.logMsg("Send BytesMessage");
      BytesMessage bMsg = session.createBytesMessage();
      TestUtil.logMsg("Set some values in BytesMessage");
      bMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendAndRecvMsgsOfEachMsgTypeQueueTest");
      bMsg.writeByte((byte) 1);
      bMsg.writeInt((int) 22);
      producer.send(bMsg);
      TestUtil.logMsg("Receive BytesMessage");
      BytesMessage bMsgRecv = (BytesMessage) consumer.receive(timeout);
      if (bMsgRecv == null) {
        throw new Fault("Did not receive BytesMessage");
      }
      TestUtil.logMsg("Check the values in BytesMessage");
      if (bMsgRecv.readByte() == (byte) 1) {
        TestUtil.logMsg("bytevalue is correct");
      } else {
        TestUtil.logMsg("bytevalue is incorrect");
        pass = false;
      }
      if (bMsgRecv.readInt() == (int) 22) {
        TestUtil.logMsg("intvalue is correct");
      } else {
        TestUtil.logMsg("intvalue is incorrect");
        pass = false;
      }

      // send and receive MapMessage
      TestUtil.logMsg("Send MapMessage");
      MapMessage mMsg = session.createMapMessage();
      TestUtil.logMsg("Set some values in MapMessage");
      mMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendAndRecvMsgsOfEachMsgTypeQueueTest");
      mMsg.setBoolean("booleanvalue", true);
      mMsg.setInt("intvalue", (int) 10);
      producer.send(mMsg);
      TestUtil.logMsg("Receive MapMessage");
      MapMessage mMsgRecv = (MapMessage) consumer.receive(timeout);
      if (mMsgRecv == null) {
        throw new Fault("Did not receive MapMessage");
      }
      TestUtil.logMsg("Check the values in MapMessage");
      Enumeration list = mMsgRecv.getMapNames();
      String name = null;
      while (list.hasMoreElements()) {
        name = (String) list.nextElement();
        if (name.equals("booleanvalue")) {
          if (mMsgRecv.getBoolean(name) == true) {
            TestUtil.logMsg("booleanvalue is correct");
          } else {
            TestUtil.logErr("booleanvalue is incorrect");
            pass = false;
          }
        } else if (name.equals("intvalue")) {
          if (mMsgRecv.getInt(name) == 10) {
            TestUtil.logMsg("intvalue is correct");
          } else {
            TestUtil.logErr("intvalue is incorrect");
            pass = false;
          }
        } else {
          TestUtil.logErr("Unexpected name of [" + name + "] in MapMessage");
          pass = false;
        }
      }

      // send and receive ObjectMessage
      TestUtil.logMsg("Send ObjectMessage");
      StringBuffer sb1 = new StringBuffer("This is a StringBuffer");
      TestUtil.logMsg("Set some values in ObjectMessage");
      ObjectMessage oMsg = session.createObjectMessage();
      oMsg.setObject(sb1);
      oMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendAndRecvMsgsOfEachMsgTypeQueueTest");
      producer.send(oMsg);
      TestUtil.logMsg("Receive ObjectMessage");
      ObjectMessage oMsgRecv = (ObjectMessage) consumer.receive(timeout);
      if (oMsgRecv == null) {
        throw new Fault("Did not receive ObjectMessage");
      }
      TestUtil.logMsg("Check the value in ObjectMessage");
      StringBuffer sb2 = (StringBuffer) oMsgRecv.getObject();
      if (sb2.toString().equals(sb1.toString())) {
        TestUtil.logMsg("objectvalue is correct");
      } else {
        TestUtil.logErr("objectvalue is incorrect");
        pass = false;
      }

      // send and receive ObjectMessage passing object as param
      TestUtil.logMsg("Send ObjectMessage passing object as param");
      sb1 = new StringBuffer("This is a StringBuffer");
      TestUtil
          .logMsg("Set some values in ObjectMessage passing object as param");
      oMsg = session.createObjectMessage(sb1);
      oMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendAndRecvMsgsOfEachMsgTypeQueueTest");
      producer.send(oMsg);
      TestUtil.logMsg("Receive ObjectMessage");
      oMsgRecv = (ObjectMessage) consumer.receive(timeout);
      if (oMsgRecv == null) {
        throw new Fault("Did not receive ObjectMessage");
      }
      TestUtil.logMsg("Check the value in ObjectMessage");
      sb2 = (StringBuffer) oMsgRecv.getObject();
      if (sb2.toString().equals(sb1.toString())) {
        TestUtil.logMsg("objectvalue is correct");
      } else {
        TestUtil.logErr("objectvalue is incorrect");
        pass = false;
      }

      // send and receive StreamMessage
      TestUtil.logMsg("Send StreamMessage");
      StreamMessage sMsg = session.createStreamMessage();
      TestUtil.logMsg("Set some values in StreamMessage");
      sMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendAndRecvMsgsOfEachMsgTypeQueueTest");
      sMsg.writeBoolean(true);
      sMsg.writeInt((int) 22);
      producer.send(sMsg);
      TestUtil.logMsg("Receive StreamMessage");
      StreamMessage sMsgRecv = (StreamMessage) consumer.receive(timeout);
      if (sMsgRecv == null) {
        throw new Fault("Did not receive StreamMessage");
      }
      TestUtil.logMsg("Check the values in StreamMessage");
      if (sMsgRecv.readBoolean() == true) {
        TestUtil.logMsg("booleanvalue is correct");
      } else {
        TestUtil.logMsg("booleanvalue is incorrect");
        pass = false;
      }
      if (sMsgRecv.readInt() == (int) 22) {
        TestUtil.logMsg("intvalue is correct");
      } else {
        TestUtil.logMsg("intvalue is incorrect");
        pass = false;
      }

      // send and receive TextMessage
      TestUtil.logMsg("Send TextMessage");
      TextMessage tMsg = session.createTextMessage();
      TestUtil.logMsg("Set some values in MapMessage");
      tMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendAndRecvMsgsOfEachMsgTypeQueueTest");
      tMsg.setText("Hello There!");
      producer.send(tMsg);
      TestUtil.logMsg("Receive TextMessage");
      TextMessage tMsgRecv = (TextMessage) consumer.receive(timeout);
      if (tMsgRecv == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg("Check the value in TextMessage");
      if (tMsgRecv.getText().equals("Hello There!")) {
        TestUtil.logMsg("TextMessage is correct");
      } else {
        TestUtil.logErr("TextMessage is incorrect");
        pass = false;
      }

      // send and receive TextMessage passing string as param
      TestUtil.logMsg("Send TextMessage");
      tMsg = session.createTextMessage("Where are you!");
      TestUtil.logMsg("Set some values in TextMessage");
      tMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "sendAndRecvMsgsOfEachMsgTypeQueueTest");
      producer.send(tMsg);
      TestUtil.logMsg("Receive TextMessage");
      tMsgRecv = (TextMessage) consumer.receive(timeout);
      if (tMsgRecv == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg("Check the value in TextMessage");
      if (tMsgRecv.getText().equals("Where are you!")) {
        TestUtil.logMsg("TextMessage is correct");
      } else {
        TestUtil.logErr("TextMessage is incorrect");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      e.printStackTrace();
      throw new Fault("sendAndRecvMsgsOfEachMsgTypeQueueTest", e);
    }

    if (!pass) {
      throw new Fault("sendAndRecvMsgsOfEachMsgTypeQueueTest failed");
    }
  }

  /*
   * @testName: createTemporayQueueTest
   *
   * @assertion_ids: JMS:JAVADOC:194;
   *
   * @test_Strategy: Test the following APIs:
   *
   * Session.createTemporaryQueue().
   *
   * Send and receive a TextMessage to temporary queue. Compare send and recv
   * message for equality.
   */
  public void createTemporayQueueTest() throws Fault {
    boolean pass = true;
    MessageConsumer tConsumer = null;
    try {

      String message = "a text message";

      // Setup for Queue
      setupGlobalVarsQ();

      // create a TemporaryQueue
      TestUtil.logMsg("Creating TemporaryQueue");
      TemporaryQueue tempQueue = session.createTemporaryQueue();

      // Create a MessageConsumer for this Temporary Queue
      TestUtil.logMsg("Creating MessageConsumer");
      tConsumer = session.createConsumer(tempQueue);

      // Create a MessageProducer for this Temporary Queue
      TestUtil.logMsg("Creating MessageProducer");
      MessageProducer tProducer = session.createProducer(tempQueue);

      // Send TextMessage to temporary queue
      TestUtil.logMsg("Creating TextMessage with text [" + message + "]");
      TextMessage tMsg = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      tMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "createTemporayQueueTest");
      TestUtil.logMsg("Send TextMessage to temporaty queue");
      tProducer.send(tMsg);

      // Receive TextMessage from temporary queue
      TestUtil.logMsg("Receive TextMessage from temporaty queue");
      TextMessage tMsgRecv = null;
      if (tConsumer != null)
        tMsgRecv = (TextMessage) tConsumer.receive(timeout);
      if (tMsgRecv == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg("Check the value in TextMessage");
      if (tMsgRecv.getText().equals(message)) {
        TestUtil.logMsg("TextMessage is correct");
      } else {
        TestUtil.logErr("TextMessage is incorrect");
        pass = false;
      }

      TestUtil.logMsg(
          "Attempting to delete temporary queue with an open consumer should not be allowed");
      try {
        tempQueue.delete();
        pass = false;
        TestUtil
            .logErr("TemporaryQueue.delete() didn't throw expected Exception");
      } catch (JMSException em) {
        TestUtil.logMsg("Received expected JMSException: ");
      } catch (Exception e) {
        TestUtil.logErr("Received unexpected JMSException: " + e);
        pass = false;
      }

      try {
        if (tConsumer != null)
          tConsumer.close();
      } catch (Exception e) {
      }

      TestUtil.logMsg(
          "Attempting to delete temporary queue with no open consumer should be allowed");
      try {
        tempQueue.delete();
      } catch (Exception e) {
        pass = false;
        TestUtil.logErr("Received unexpected Exception: ", e);
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      e.printStackTrace();
      throw new Fault("createTemporayQueueTest");
    }

    if (!pass) {
      throw new Fault("createTemporayQueueTest failed");
    }
  }

  /*
   * @testName: createQueueBrowserTest
   *
   * @assertion_ids: JMS:JAVADOC:258; JMS:JAVADOC:260;
   *
   * @test_Strategy: Test the following APIs:
   *
   * Session.createBrowser(Queue) Session.createBrowser(Queue, String)
   * 
   * 1. Send x text messages to a Queue. 2. Create a QueueBrowser with selector
   * to browse just the last message in the Queue. 3. Create a QueueBrowser
   * again to browse all the messages in the queue.
   */
  public void createQueueBrowserTest() throws Fault {
    boolean pass = true;
    QueueBrowser qBrowser = null;
    try {
      TextMessage tempMsg = null;
      Enumeration msgs = null;

      // Setup for Queue
      setupGlobalVarsQ();
      consumer.close();

      // send "numMessages" messages to Queue plus end of stream message
      TestUtil.logMsg("Send " + numMessages + " to Queue");
      for (int i = 1; i <= numMessages; i++) {
        tempMsg = session.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "createQueueBrowserTest" + i);
        if (i == numMessages) {
          tempMsg.setBooleanProperty("lastMessage", true);
        } else {
          tempMsg.setBooleanProperty("lastMessage", false);
        }
        producer.send(tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      // create QueueBrowser to peek at last message in Queue using message
      // selector
      TestUtil.logMsg(
          "Create QueueBrowser to peek at last message in Queue using message selector");
      qBrowser = session.createBrowser(queue, "lastMessage = TRUE");

      // check that browser just has the last message in the Queue
      TestUtil.logMsg("Check that browser has just the last message");
      int msgCount = 0;
      msgs = qBrowser.getEnumeration();
      while (msgs.hasMoreElements()) {
        tempMsg = (TextMessage) msgs.nextElement();
        if (!tempMsg.getText().equals("Message " + numMessages)) {
          TestUtil.logErr("Found [" + tempMsg.getText()
              + "] in browser expected [Message 3]");
          pass = false;
        } else {
          TestUtil.logMsg("Found correct [Message 3] in browser");
        }
        msgCount++;
      }
      if (msgCount != 1) {
        TestUtil
            .logErr("Found " + msgCount + " messages in browser expected 1");
        pass = false;
      } else {
        TestUtil.logMsg("Found 1 message in browser (correct)");
      }
      qBrowser.close();

      // create QueueBrowser to peek at all messages in the Queue
      TestUtil
          .logMsg("Create QueueBrowser to browse all messages in the Queue");
      qBrowser = session.createBrowser(queue);

      // check for messages
      TestUtil.logMsg(
          "Check that browser contains all " + numMessages + " messages");
      msgCount = 0;
      int msgIndex = 1;
      msgs = qBrowser.getEnumeration();
      while (msgs.hasMoreElements()) {
        tempMsg = (TextMessage) msgs.nextElement();
        if (!tempMsg.getText().equals("Message " + msgIndex)) {
          TestUtil.logErr("Found [" + tempMsg.getText()
              + "] in browser expected [Message " + msgIndex + "]");
          pass = false;
        }
        msgCount++;
        msgIndex++;
      }
      if (msgCount != numMessages) {
        TestUtil.logErr("Found " + msgCount + " messages in browser expected "
            + numMessages);
        pass = false;
      } else {
        TestUtil
            .logMsg("Found " + numMessages + " messages in browser (correct)");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      e.printStackTrace();
      throw new Fault("createQueueBrowserTest");
    } finally {
      try {
        if (qBrowser != null)
          qBrowser.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("createQueueBrowserTest failed");
    }
  }

  /*
   * @testName: getTransactedQueueTest
   *
   * @assertion_ids: JMS:JAVADOC:225;
   *
   * @test_Strategy: Test the following APIs:
   *
   * Session.getTransacted().
   */
  public void getTransactedQueueTest() throws Fault {
    boolean pass = true;

    // Test for transacted mode false
    try {
      // Setup for Queue
      setupGlobalVarsQ();
      session.close();

      session = connection.createSession(Session.AUTO_ACKNOWLEDGE);
      boolean expTransacted = false;
      TestUtil.logMsg("Calling getTransacted and expect " + expTransacted
          + " to be returned");
      boolean actTransacted = session.getTransacted();
      if (actTransacted != expTransacted) {
        TestUtil.logErr("getTransacted() returned " + actTransacted
            + ", expected " + expTransacted);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("getTransactedQueueTest", e);
    } finally {
      try {
        if (session != null)
          session.close();
      } catch (Exception e) {
      }
    }

    // Test for transacted mode true
    if ((vehicle.equals("appclient") || vehicle.equals("standalone"))) {
      try {
        session = connection.createSession(Session.SESSION_TRANSACTED);
        boolean expTransacted = true;
        TestUtil.logMsg("Calling getTransacted and expect " + expTransacted
            + " to be returned");
        boolean actTransacted = session.getTransacted();
        if (actTransacted != expTransacted) {
          TestUtil.logErr("getTransacted() returned " + actTransacted
              + ", expected " + expTransacted);
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught exception: " + e);
        throw new Fault("getTransactedQueueTest", e);
      } finally {
        try {
          if (session != null)
            session.close();
        } catch (Exception e) {
        }
      }
    }

    if (!pass) {
      throw new Fault("getTransactedQueueTest failed");
    }
  }

  /*
   * @testName: getAcknowledgeModeQueueTest
   *
   * @assertion_ids: JMS:JAVADOC:227;
   *
   * @test_Strategy: Test the following APIs:
   *
   * Session.getAcknowledgeMode().
   */
  public void getAcknowledgeModeQueueTest() throws Fault {
    boolean pass = true;

    // Test for AUTO_ACKNOWLEDGE mode
    try {
      // Setup for Queue
      setupGlobalVarsQ();
      session.close();

      session = connection.createSession(Session.AUTO_ACKNOWLEDGE);
      int expAcknowledgeMode = Session.AUTO_ACKNOWLEDGE;
      TestUtil.logMsg("Calling getAcknowledgeMode and expect "
          + expAcknowledgeMode + " to be returned");
      int actAcknowledgeMode = session.getAcknowledgeMode();
      if (actAcknowledgeMode != expAcknowledgeMode) {
        TestUtil.logErr("getAcknowledgeMode() returned " + actAcknowledgeMode
            + ", expected " + expAcknowledgeMode);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("getAcknowledgeModeTopicTest", e);
    } finally {
      try {
        if (session != null)
          session.close();
      } catch (Exception e) {
      }
    }

    // Test for DUPS_OK_ACKNOWLEDGE mode
    try {
      session = connection.createSession(Session.DUPS_OK_ACKNOWLEDGE);
      int expAcknowledgeMode = Session.DUPS_OK_ACKNOWLEDGE;
      TestUtil.logMsg("Calling getAcknowledgeMode and expect "
          + expAcknowledgeMode + " to be returned");
      int actAcknowledgeMode = session.getAcknowledgeMode();
      if (actAcknowledgeMode != expAcknowledgeMode) {
        TestUtil.logErr("getAcknowledgeMode() returned " + actAcknowledgeMode
            + ", expected " + expAcknowledgeMode);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("getAcknowledgeModeTopicTest", e);
    } finally {
      try {
        if (session != null)
          session.close();
      } catch (Exception e) {
      }
    }

    // Test for SESSION_TRANSACTED mode
    if ((vehicle.equals("appclient") || vehicle.equals("standalone"))) {
      try {
        session = connection.createSession(Session.SESSION_TRANSACTED);
        int expAcknowledgeMode = Session.SESSION_TRANSACTED;
        TestUtil.logMsg("Calling getAcknowledgeMode and expect "
            + expAcknowledgeMode + " to be returned");
        int actAcknowledgeMode = session.getAcknowledgeMode();
        if (actAcknowledgeMode != expAcknowledgeMode) {
          TestUtil.logErr("getAcknowledgeMode() returned " + actAcknowledgeMode
              + ", expected " + expAcknowledgeMode);
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught exception: " + e);
        throw new Fault("getAcknowledgeModeTopicTest", e);
      } finally {
        try {
          if (session != null)
            session.close();
        } catch (Exception e) {
        }
      }
    }

    if (!pass) {
      throw new Fault("getAcknowledgeModeQueueTest failed");
    }
  }

  /*
   * @testName: createConsumerProducerQueueTest
   *
   * @assertion_ids: JMS:JAVADOC:221; JMS:JAVADOC:242; JMS:JAVADOC:244;
   * JMS:JAVADOC:246; JMS:JAVADOC:248; JMS:JAVADOC:504; JMS:JAVADOC:510;
   * JMS:JAVADOC:597; JMS:JAVADOC:334;
   *
   * @test_Strategy: Test the following APIs:
   *
   * ConnectionFactory.createConnection(String, String)
   * Connection.createSession(boolean, int) Session.createTextMessage(String)
   * Session.createConsumer(Destination) Session.createConsumer(Destination,
   * String) Session.createConsumer(Destination, String, boolean)
   * Session.createProducer(Destination) MessageProducer.send(Message)
   * MessageConsumer.receive(long timeout)
   * 
   * 1. Send x text messages to a Queue. 2. Create a MessageConsumer with
   * selector to consume just the last message in the Queue. 3. Create a
   * MessageConsumer again to consume the rest of the messages in the Queue.
   */
  public void createConsumerProducerQueueTest() throws Fault {
    boolean pass = true;
    try {
      TextMessage tempMsg = null;
      Enumeration msgs = null;

      // Setup for Queue
      setupGlobalVarsQ();
      consumer.close();

      // send "numMessages" messages to Queue plus end of stream message
      TestUtil.logMsg("Send " + numMessages + " messages to Queue");
      for (int i = 1; i <= numMessages; i++) {
        tempMsg = session.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "createConsumerProducerQueueTest" + i);
        tempMsg.setJMSType("");
        if (i == numMessages) {
          tempMsg.setBooleanProperty("lastMessage", true);
        } else {
          tempMsg.setBooleanProperty("lastMessage", false);
        }
        producer.send(tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      // Create MessageConsumer to consume last message in Queue using message
      // selector
      TestUtil.logMsg(
          "Create selective consumer to consume messages in Queue with boolproperty (lastMessage=TRUE)");
      consumer = session.createConsumer(destination, "lastMessage=TRUE");

      TestUtil.logMsg(
          "Consume messages with selective consumer which has boolproperty (lastMessage=TRUE)");
      if (consumer != null)
        tempMsg = (TextMessage) consumer.receive(timeout);
      if (tempMsg == null) {
        TestUtil.logErr("MessageConsumer.receive() returned NULL");
        TestUtil.logErr("Message " + numMessages + " missing from Queue");
        pass = false;
      } else if (!tempMsg.getText().equals("Message " + numMessages)) {
        TestUtil.logErr(
            "Received [" + tempMsg.getText() + "] expected [Message 3]");
        pass = false;
      } else {
        TestUtil.logMsg("Received expected message: " + tempMsg.getText());
      }

      // Try to receive one more message (should return null)
      TestUtil.logMsg("Make sure selective consumer receives no more messages");
      if (consumer != null)
        tempMsg = (TextMessage) consumer.receive(timeout);
      if (tempMsg != null) {
        TestUtil.logErr("MessageConsumer.receive() returned NULL");
        TestUtil.logErr(
            "MessageConsumer with selector should have returned just 1 message");
        pass = false;
      }

      try {
        if (consumer != null)
          consumer.close();
      } catch (Exception e) {
      }

      // Create MessageConsumer to consume rest of messages in Queue
      TestUtil.logMsg("Consume rest of messages with normal consumer");
      consumer = session.createConsumer(destination);
      for (int msgCount = 1; msgCount < numMessages; msgCount++) {
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

      // Try to receive one more message (should return null)
      TestUtil.logMsg("Make sure normal consumer receives no more messages");
      tempMsg = (TextMessage) consumer.receive(timeout);
      if (tempMsg != null) {
        TestUtil.logErr("MessageConsumer should have returned "
            + (numMessages - 1) + " message");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      e.printStackTrace();
      throw new Fault("createConsumerProducerQueueTest");
    } finally {
      try {
        if (consumer != null)
          consumer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("createConsumerProducerQueueTest failed");
    }
  }

}
