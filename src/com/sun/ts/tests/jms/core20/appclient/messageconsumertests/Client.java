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
package com.sun.ts.tests.jms.core20.appclient.messageconsumertests;

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
  private static final String testName = "com.sun.ts.tests.jms.core20.appclient.messageconsumertests.Client";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // JMS tool which creates and/or looks up the JMS administered objects
  private transient JmsTool tool = null;

  // JMS tool which creates and/or looks up the JMS administered objects
  private transient JmsTool toolForProducer = null;

  // JMS objects
  transient MessageProducer producer = null;

  transient MessageConsumer consumer = null;

  transient Connection connection = null;

  transient Session session = null;

  transient Destination destination = null;

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
      TestUtil.logMsg("Closing default Connection");
      tool.getDefaultConnection().close();
      if (queueTest) {
        TestUtil.logMsg("Flush any messages left on Queue");
        tool.flushDestination();
      }
      tool.closeAllResources();

      if (toolForProducer != null) {
        toolForProducer.getDefaultConnection().close();
        if (queueTest) {
          TestUtil.logMsg("Flush any messages left on Queue");
          toolForProducer.flushDestination();
        }
        toolForProducer.closeAllResources();
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("cleanup failed!", e);
    }
  }

  /*
   * @testName: queueSendRecvMessageListenerTest
   * 
   * @assertion_ids: JMS:JAVADOC:317; JMS:JAVADOC:328; JMS:JAVADOC:330;
   * JMS:SPEC:264.4; JMS:SPEC:264; JMS:SPEC:137;
   * 
   * @test_Strategy: Creates a new consumer on the specified destination that
   * will deliver messages to the specified MessageListener. Tests the following
   * API method:
   * 
   * MessageProducer.send(Message)
   * MessageConsumer.setMessageListener(MessageListener)
   * MessageConsumer.getMessageListener()
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
      toolForProducer = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      logMsg("Initialize variables after JmsTool setup");
      producer = toolForProducer.getDefaultProducer();
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = true;

      // Creates a new consumer on the specified destination that
      // will deliver messages to the specified MessageListener.
      TestUtil.logMsg("Create message listener MyMessageListener");
      MyMessageListener listener = new MyMessageListener();
      TestUtil.logMsg(
          "Set message listener MyMessageListener on this MessageConsumer");
      consumer.setMessageListener(listener);

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      // TextMessage expTextMessage = session.createTextMessage(message);
      TextMessage expTextMessage = new TextMessageTestImpl();
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setText(message);
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "queueSendRecvMessageListenerTest");
      TestUtil.logMsg("Calling MessageProducer.send(Message)");
      producer.send(expTextMessage);

      TestUtil.logMsg("Poll listener waiting for TestMessage to arrive");
      TextMessage actTextMessage = null;
      for (int i = 0; !listener.isComplete() && i < 60; i++) {
        TestUtil.logMsg("Loop " + i
            + ": sleep 2 seconds waiting for messages to arrive at listener");
        TestUtil.sleepSec(2);
      }
      if (listener.isComplete())
        actTextMessage = (TextMessage) listener.getMessage();

      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage (actTextMessage=NULL)");
      }

      TestUtil.logMsg("Check value of message returned");
      if (!actTextMessage.getText().equals(expTextMessage.getText())) {
        TestUtil.logErr("Received [" + actTextMessage.getText() + "] expected ["
            + expTextMessage.getText() + "]");
        pass = false;
      }

      TestUtil.logMsg(
          "Retreive MessageListener by calling MessageConsumer.getMessageListener()");
      MessageListener messageListener = consumer.getMessageListener();
      if (messageListener == null) {
        TestUtil.logErr("getMessageListener() returned NULL");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("queueSendRecvMessageListenerTest", e);
    } finally {
      try {
        producer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("queueSendRecvMessageListenerTest failed");
    }
  }

  /*
   * @testName: topicSendRecvMessageListenerTest
   * 
   * @assertion_ids: JMS:JAVADOC:317; JMS:JAVADOC:328; JMS:JAVADOC:330;
   * JMS:SPEC:264.4; JMS:SPEC:264;
   * 
   * @test_Strategy: Creates a new consumer on the specified destination that
   * will deliver messages to the specified MessageListener. Tests the following
   * API method:
   * 
   * MessageProducer.send(Message)
   * MessageConsumer.setMessageListener(MessageListener)
   * MessageConsumer.getMessageListener()
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
      toolForProducer = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      logMsg("Initialize variables after JmsTool setup");
      producer = toolForProducer.getDefaultProducer();
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = false;

      // Creates a new consumer on the specified destination that
      // will deliver messages to the specified MessageListener.
      TestUtil.logMsg("Create message listener MyMessageListener");
      MyMessageListener listener = new MyMessageListener();
      TestUtil.logMsg(
          "Set message listener MyMessageListener on this MessageConsumer");
      consumer.setMessageListener(listener);

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      // TextMessage expTextMessage = session.createTextMessage(message);
      TextMessage expTextMessage = new TextMessageTestImpl();
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setText(message);
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "topicSendRecvMessageListenerTest");
      TestUtil.logMsg("Calling MessageProducer.send(Message)");
      producer.send(expTextMessage);

      TestUtil.logMsg("Poll listener waiting for TestMessage to arrive");
      TextMessage actTextMessage = null;
      for (int i = 0; !listener.isComplete() && i < 60; i++) {
        TestUtil.logMsg("Loop " + i
            + ": sleep 2 seconds waiting for messages to arrive at listener");
        TestUtil.sleepSec(2);
      }
      if (listener.isComplete())
        actTextMessage = (TextMessage) listener.getMessage();

      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage (actTextMessage=NULL)");
      }

      TestUtil.logMsg("Check value of message returned");
      if (!actTextMessage.getText().equals(expTextMessage.getText())) {
        TestUtil.logErr("Received [" + actTextMessage.getText() + "] expected ["
            + expTextMessage.getText() + "]");
        pass = false;
      }

      TestUtil.logMsg(
          "Retreive MessageListener by calling MessageConsumer.getMessageListener()");
      MessageListener messageListener = consumer.getMessageListener();
      if (messageListener == null) {
        TestUtil.logErr("getMessageListener() returned NULL");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("topicSendRecvMessageListenerTest", e);
    } finally {
      try {
        producer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("topicSendRecvMessageListenerTest failed");
    }
  }
}
