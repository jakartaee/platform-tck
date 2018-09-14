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
 * $Id$
 */
package com.sun.ts.tests.jms.core20.appclient.jmsconsumertests;

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
  private static final String testName = "com.sun.ts.tests.jms.core20.appclient.jmsconsumertests.Client";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // JMS tool which creates and/or looks up the JMS administered objects
  private transient JmsTool tool = null;

  // JMS objects
  transient ConnectionFactory cf = null;

  transient JMSContext context = null;

  transient JMSContext contextToSendMsg = null;

  transient JMSContext contextToCreateMsg = null;

  transient JMSConsumer consumer = null;

  transient JMSProducer producer = null;

  transient Destination destination = null;

  transient Queue queue = null;

  transient Topic topic = null;

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

  boolean queueTest = false;

  boolean topicTest = false;

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

  /* Utility methods for tests */

  /*
   * Checks passed flag for negative tests and throws exception back to caller
   * which passes ot to harness.
   * 
   * @param boolean Pass/Fail flag
   */
  private void checkExceptionPass(boolean passed) throws Exception {
    if (passed == false) {
      TestUtil.logMsg("Didn't get expected exception");
      throw new Exception("Didn't get expected exception");
    }
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
      TestUtil.logMsg("Close JMSContext objects");
      if (context != null) {
        context.close();
        context = null;
      }
      if (contextToSendMsg != null) {
        contextToSendMsg.close();
        contextToSendMsg = null;
      }
      if (contextToCreateMsg != null) {
        contextToCreateMsg.close();
        contextToCreateMsg = null;
      }
      TestUtil.logMsg("Close JMSConsumer objects");
      if (consumer != null) {
        consumer.close();
        consumer = null;
      }
      TestUtil.logMsg("Closing default Connection");
      tool.getDefaultConnection().close();
      if (queueTest) {
        TestUtil.logMsg("Flush any messages left on Queue");
        tool.flushDestination();
      }
      tool.closeAllResources();
      producer = null;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("cleanup failed!", e);
    }
  }

  /*
   * @testName: queueSendRecvMessageListenerTest
   * 
   * @assertion_ids: JMS:JAVADOC:1234; JMS:JAVADOC:1145; JMS:JAVADOC:1149;
   * JMS:JAVADOC:325; JMS:SPEC:264.4; JMS:SPEC:264;
   * 
   * @test_Strategy: Creates a new consumer on the specified destination that
   * will deliver messages to the specified MessageListener. Tests the following
   * API method:
   * 
   * JMSConsumer.setMessageListener(MessageListener)
   * JMSConsumer.getMessageListener() JMSProducer.send(Destination, Message)
   * MessageListener.onMessage(Message)
   * 
   * 1 Setup MessageListener for the specified destination 2 Send a message to
   * the destination 3 Verify message received by listener
   */
  public void queueSendRecvMessageListenerTest() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    try {
      // set up test tool for Queue
      TestUtil.logMsg("Setup JmsTool for COMMON QUEUE");
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      cf = tool.getConnectionFactory();
      tool.getDefaultConnection().close();
      destination = tool.getDefaultDestination();
      queue = (Queue) destination;
      queueTest = true;

      TestUtil.logMsg("Create JMSContext with AUTO_ACKNOWLEDGE");
      context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
      contextToSendMsg = cf.createContext(user, password,
          JMSContext.AUTO_ACKNOWLEDGE);
      contextToCreateMsg = cf.createContext(user, password,
          JMSContext.AUTO_ACKNOWLEDGE);

      TestUtil.logMsg("Create JMSProducer");
      producer = contextToSendMsg.createProducer();

      TestUtil.logMsg("Create JMSConsumer");
      consumer = context.createConsumer(destination);

      // Creates a new consumer on the specified destination that
      // will deliver messages to the specified MessageListener.
      MyMessageListener listener = new MyMessageListener();
      consumer.setMessageListener(listener);

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = contextToCreateMsg
          .createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "queueSendRecvMessageListenerTest");
      TestUtil.logMsg(
          "Send the TestMessage via JMSProducer.send(Destination, Message)");
      producer.send(destination, expTextMessage);

      TestUtil.logMsg("Poll listener waiting for TestMessage to arrive");
      TextMessage actTextMessage = null;
      for (int i = 0; i < 60; i++) {
        TestUtil.sleepSec(2);
        if (listener.isComplete()) {
          listener.setComplete(false);
          actTextMessage = (TextMessage) listener.getMessage();
          TestUtil.logMsg("Received TextMessage after polling loop " + (i + 1));
          break;
        }
        TestUtil.logMsg("Completed polling loop " + i);
      }

      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage (actTextMessage=NULL)");
      }

      TestUtil.logMsg("Check value of TextMessage returned");
      if (!actTextMessage.getText().equals(expTextMessage.getText())) {
        TestUtil.logErr("Received [" + actTextMessage.getText() + "] expected ["
            + expTextMessage.getText() + "]");
        pass = false;
      }

      TestUtil.logMsg(
          "Retreive MessageListener by calling consumer.getMessageListener()");
      MessageListener messageListener = consumer.getMessageListener();
      if (messageListener == null) {
        TestUtil.logErr("getMessageListener() returned NULL");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("queueSendRecvMessageListenerTest", e);
    }

    if (!pass) {
      throw new Fault("queueSendRecvMessageListenerTest failed");
    }
  }

  /*
   * @testName: topicSendRecvMessageListenerTest
   * 
   * @assertion_ids: JMS:JAVADOC:1234; JMS:JAVADOC:1145; JMS:JAVADOC:1149;
   * JMS:JAVADOC:325; JMS:SPEC:264.4; JMS:SPEC:264;
   * 
   * @test_Strategy: Creates a new consumer on the specified destination that
   * will deliver messages to the specified MessageListener. Tests the following
   * API method:
   * 
   * JMSConsumer.setMessageListener(MessageListener)
   * JMSConsumer.getMessageListener() JMSProducer.send(Destination, Message)
   * MessageListener.onMessage(Message)
   * 
   * 1 Setup MessageListener for the specified destination 2 Send a message to
   * the destination 3 Verify message received by listener
   */
  public void topicSendRecvMessageListenerTest() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    try {
      // set up test tool for Topic
      TestUtil.logMsg("Setup JmsTool for COMMON TOPIC");
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      cf = tool.getConnectionFactory();
      tool.getDefaultConnection().close();
      destination = tool.getDefaultDestination();
      topic = (Topic) destination;
      topicTest = true;

      TestUtil.logMsg("Create JMSContext with AUTO_ACKNOWLEDGE");
      context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
      contextToSendMsg = cf.createContext(user, password,
          JMSContext.AUTO_ACKNOWLEDGE);
      contextToCreateMsg = cf.createContext(user, password,
          JMSContext.AUTO_ACKNOWLEDGE);

      TestUtil.logMsg("Create JMSProducer");
      producer = contextToSendMsg.createProducer();

      TestUtil.logMsg("Create JMSConsumer");
      consumer = context.createConsumer(destination);

      // Creates a new consumer on the specified destination that
      // will deliver messages to the specified MessageListener.
      MyMessageListener listener = new MyMessageListener();
      consumer.setMessageListener(listener);

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = contextToCreateMsg
          .createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "topicSendRecvMessageListenerTest");
      TestUtil.logMsg(
          "Send the TestMessage via JMSProducer.send(Destination, Message)");
      producer.send(destination, expTextMessage);

      TestUtil.logMsg("Poll listener waiting for TestMessage to arrive");
      TextMessage actTextMessage = null;
      for (int i = 1; i < 60; i++) {
        TestUtil.sleepSec(2);
        if (listener.isComplete()) {
          listener.setComplete(false);
          actTextMessage = (TextMessage) listener.getMessage();
          TestUtil.logMsg("Received TextMessage after polling loop " + i);
          break;
        }
        TestUtil.logMsg("Completed polling loop " + i);
      }

      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage (actTextMessage=NULL)");
      }

      TestUtil.logMsg("Check value of TextMessage returned");
      if (!actTextMessage.getText().equals(expTextMessage.getText())) {
        TestUtil.logErr("Received [" + actTextMessage.getText() + "] expected ["
            + expTextMessage.getText() + "]");
        pass = false;
      }

      TestUtil.logMsg(
          "Retreive MessageListener by calling consumer.getMessageListener()");
      MessageListener messageListener = consumer.getMessageListener();
      if (messageListener == null) {
        TestUtil.logErr("getMessageListener() returned NULL");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("topicSendRecvMessageListenerTest", e);
    }

    if (!pass) {
      throw new Fault("topicSendRecvMessageListenerTest failed");
    }
  }
}
