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
package com.sun.ts.tests.jms.core20.messageproducertests;

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
  private static final String testName = "com.sun.ts.tests.jms.core20.messageproducertests.Client";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // JMS tool which creates and/or looks up the JMS administered objects
  private transient JmsTool tool = null;

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
      TestUtil.logErr("Caught exception: " + e.getMessage());
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
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("cleanup failed!", e);
    }
  }

  /*
   * @testName: queueSendAndRecvTest1
   *
   * @assertion_ids: JMS:JAVADOC:321; JMS:JAVADOC:334;
   *
   * @test_Strategy: Send a message using the following API method and verify
   * the send and recv of data:
   *
   * MessageProducer.send(Destination, Message) MessageConsumer.receive(timeout)
   */
  public void queueSendAndRecvTest1() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    try {
      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Queue) null);
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = true;

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "queueSendAndRecvTest1");
      TestUtil.logMsg("Sending TextMessage");
      TestUtil.logMsg("Calling MessageProducer.send(Destination, Message)");
      producer.send(destination, expTextMessage);
      TestUtil.logMsg("Receive TextMessage");
      TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
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
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("queueSendAndRecvTest1", e);
    } finally {
      try {
        producer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("queueSendAndRecvTest1 failed");
    }
  }

  /*
   * @testName: queueSendAndRecvTest2
   *
   * @assertion_ids: JMS:JAVADOC:323; JMS:JAVADOC:334;
   *
   * @test_Strategy: Send a message using the following API method and verify
   * the send and recv of data:
   *
   * MessageProducer.send(Destination, Message, int, int, long)
   * MessageConsumer.receive(timeout)
   */
  public void queueSendAndRecvTest2() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    try {
      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Queue) null);
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = true;

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "queueSendAndRecvTest2");
      TestUtil.logMsg("Sending TextMessage");
      TestUtil.logMsg(
          "Calling MessageProducer.send(Destination, Message, int, int, long)");
      producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT,
          Message.DEFAULT_PRIORITY - 1, 0L);
      TestUtil.logMsg("Receive TextMessage");
      TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg(
          "Check the values in TextMessage, deliverymode, priority, time to live");
      if (!actTextMessage.getText().equals(expTextMessage.getText())
          || actTextMessage.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT
          || actTextMessage.getJMSPriority() != (Message.DEFAULT_PRIORITY - 1)
          || actTextMessage.getJMSExpiration() != 0L) {
        TestUtil.logErr("Didn't get the right message.");
        TestUtil.logErr("text=" + actTextMessage.getText() + ", expected "
            + expTextMessage.getText());
        TestUtil.logErr("DeliveryMode=" + actTextMessage.getJMSDeliveryMode()
            + ", expected " + expTextMessage.getJMSDeliveryMode());
        TestUtil.logErr("Priority=" + actTextMessage.getJMSPriority()
            + ", expected " + expTextMessage.getJMSPriority());
        TestUtil.logErr("TimeToLive=" + actTextMessage.getJMSExpiration()
            + ", expected " + expTextMessage.getJMSExpiration());
        pass = false;
      } else {
        TestUtil.logMsg("TextMessage is correct");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("queueSendAndRecvTest2", e);
    } finally {
      try {
        producer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("queueSendAndRecvTest2 failed");
    }
  }

  /*
   * @testName: queueSendAndRecvTest3
   *
   * @assertion_ids: JMS:JAVADOC:317; JMS:JAVADOC:334;
   *
   * @test_Strategy: Send a message using the following API method and verify
   * the send and recv of data:
   *
   * MessageProducer.send(Message) MessageConsumer.receive(timeout)
   */
  public void queueSendAndRecvTest3() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    try {
      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      producer = tool.getDefaultProducer();
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = true;

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "queueSendAndRecvTest3");
      TestUtil.logMsg("Sending TextMessage");
      TestUtil.logMsg("Calling MessageProducer.send(Message)");
      producer.send(expTextMessage);
      TestUtil.logMsg("Receive TextMessage");
      TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
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
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("queueSendAndRecvTest3", e);
    }

    if (!pass) {
      throw new Fault("queueSendAndRecvTest3 failed");
    }
  }

  /*
   * @testName: queueSendAndRecvTest4
   *
   * @assertion_ids: JMS:JAVADOC:319; JMS:JAVADOC:334;
   *
   * @test_Strategy: Send a message using the following API method and verify
   * the send and recv of data:
   *
   * MessageProducer.send(Message, int, int, long)
   * MessageConsumer.receive(timeout)
   */
  public void queueSendAndRecvTest4() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    try {
      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      producer = tool.getDefaultProducer();
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = true;

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "queueSendAndRecvTest4");
      TestUtil.logMsg("Sending TextMessage");
      TestUtil.logMsg("Calling MessageProducer.send(Message, int, int, long)");
      producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT,
          Message.DEFAULT_PRIORITY - 1, 0L);
      TestUtil.logMsg("Receive TextMessage");
      TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg(
          "Check the values in TextMessage, deliverymode, priority, time to live");
      if (!actTextMessage.getText().equals(expTextMessage.getText())
          || actTextMessage.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT
          || actTextMessage.getJMSPriority() != (Message.DEFAULT_PRIORITY - 1)
          || actTextMessage.getJMSExpiration() != 0L) {
        TestUtil.logErr("Didn't get the right message.");
        TestUtil.logErr("text=" + actTextMessage.getText() + ", expected "
            + expTextMessage.getText());
        TestUtil.logErr("DeliveryMode=" + actTextMessage.getJMSDeliveryMode()
            + ", expected " + expTextMessage.getJMSDeliveryMode());
        TestUtil.logErr("Priority=" + actTextMessage.getJMSPriority()
            + ", expected " + expTextMessage.getJMSPriority());
        TestUtil.logErr("TimeToLive=" + actTextMessage.getJMSExpiration()
            + ", expected " + expTextMessage.getJMSExpiration());
        pass = false;
      } else {
        TestUtil.logMsg("TextMessage is correct");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("queueSendAndRecvTest4", e);
    }

    if (!pass) {
      throw new Fault("queueSendAndRecvTest4 failed");
    }
  }

  /*
   * @testName: queueSetGetDeliveryModeTest
   *
   * @assertion_ids: JMS:JAVADOC:301; JMS:JAVADOC:303;
   *
   * @test_Strategy: Test the following APIs:
   *
   * MessageProducer.setDeliveryMode(int). MessageProducer.getDeliveryMode().
   */
  public void queueSetGetDeliveryModeTest() throws Fault {
    boolean pass = true;

    // Test default case
    try {
      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      producer = tool.getDefaultProducer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      queueTest = true;

      long expDeliveryMode = DeliveryMode.PERSISTENT;
      TestUtil.logMsg("Calling getDeliveryMode and expect " + expDeliveryMode
          + " to be returned");
      long actDeliveryMode = producer.getDeliveryMode();
      if (actDeliveryMode != expDeliveryMode) {
        TestUtil.logErr("getDeliveryMode() returned " + actDeliveryMode
            + ", expected " + expDeliveryMode);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("queueSetGetDeliveryModeTest");
    }

    // Test non-default case
    try {
      int expDeliveryMode = DeliveryMode.NON_PERSISTENT;
      TestUtil.logMsg("Calling setDeliveryMode(" + expDeliveryMode + ")");
      producer.setDeliveryMode(expDeliveryMode);
      TestUtil.logMsg("Calling getDeliveryMode and expect " + expDeliveryMode
          + " to be returned");
      int actDeliveryMode = producer.getDeliveryMode();
      if (actDeliveryMode != expDeliveryMode) {
        TestUtil.logErr("getDeliveryMode() returned " + actDeliveryMode
            + ", expected " + expDeliveryMode);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("queueSetGetDeliveryModeTest");
    }

    if (!pass) {
      throw new Fault("queueSetGetDeliveryModeTest failed");
    }
  }

  /*
   * @testName: queueSetGetDeliveryDelayTest
   *
   * @assertion_ids: JMS:JAVADOC:907; JMS:JAVADOC:886; JMS:SPEC:261;
   *
   * @test_Strategy: Test the following APIs:
   *
   * MessageProducer.setDeliveryDelay(long). MessageProducer.getDeliveryDelay().
   */
  public void queueSetGetDeliveryDelayTest() throws Fault {
    boolean pass = true;

    // Test default case
    try {
      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      producer = tool.getDefaultProducer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      queueTest = true;

      long expDeliveryDelay = 0L;
      TestUtil.logMsg("Calling getDeliveryDelay and expect " + expDeliveryDelay
          + " to be returned");
      long actDeliveryDelay = producer.getDeliveryDelay();
      if (actDeliveryDelay != expDeliveryDelay) {
        TestUtil.logErr("getDeliveryDelay() returned " + actDeliveryDelay
            + ", expected " + expDeliveryDelay);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("queueSetGetDeliveryDelayTest");
    }

    // Test non-default case
    try {
      long expDeliveryDelay = 1L;
      TestUtil.logMsg("Calling setDeliveryDelay(" + expDeliveryDelay + ")");
      producer.setDeliveryDelay(expDeliveryDelay);
      TestUtil.logMsg("Calling getDeliveryDelay and expect " + expDeliveryDelay
          + " to be returned");
      long actDeliveryDelay = producer.getDeliveryDelay();
      if (actDeliveryDelay != expDeliveryDelay) {
        TestUtil.logErr("getDeliveryDelay() returned " + actDeliveryDelay
            + ", expected " + expDeliveryDelay);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("queueSetGetDeliveryDelayTest");
    }

    if (!pass) {
      throw new Fault("queueSetGetDeliveryDelayTest failed");
    }
  }

  /*
   * @testName: queueSetGetDisableMessageIDTest
   *
   * @assertion_ids: JMS:JAVADOC:293; JMS:JAVADOC:295;
   *
   * @test_Strategy: Test the following APIs:
   *
   * MessageProducer.setDisableMessageID(int).
   * MessageProducer.getDisableMessageID().
   */
  public void queueSetGetDisableMessageIDTest() throws Fault {
    boolean pass = true;
    // Test true case
    try {
      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      producer = tool.getDefaultProducer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      queueTest = true;

      boolean expDisableMessageID = true;
      TestUtil
          .logMsg("Calling setDisableMessageID(" + expDisableMessageID + ")");
      producer.setDisableMessageID(expDisableMessageID);
      TestUtil.logMsg("Calling getDisableMessageID and expect "
          + expDisableMessageID + " to be returned");
      boolean actDisableMessageID = producer.getDisableMessageID();
      if (actDisableMessageID != expDisableMessageID) {
        TestUtil.logErr("getDisableMessageID() returned " + actDisableMessageID
            + ", expected " + expDisableMessageID);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("queueSetGetDisableMessageIDTest");
    }

    // Test false case
    try {
      boolean expDisableMessageID = false;
      TestUtil
          .logMsg("Calling setDisableMessageID(" + expDisableMessageID + ")");
      producer.setDisableMessageID(expDisableMessageID);
      TestUtil.logMsg("Calling getDisableMessageID and expect "
          + expDisableMessageID + " to be returned");
      boolean actDisableMessageID = producer.getDisableMessageID();
      if (actDisableMessageID != expDisableMessageID) {
        TestUtil.logErr("getDisableMessageID() returned " + actDisableMessageID
            + ", expected " + expDisableMessageID);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("queueSetGetDisableMessageIDTest");
    }

    if (!pass) {
      throw new Fault("queueSetGetDisableMessageIDTest failed");
    }
  }

  /*
   * @testName: queueSetGetDisableMessageTimestampTest
   *
   * @assertion_ids: JMS:JAVADOC:297; JMS:JAVADOC:299;
   *
   * @test_Strategy: Test the following APIs:
   *
   * MessageProducer.setDisableMessageTimestamp(int).
   * MessageProducer.getDisableMessageTimestamp().
   */
  public void queueSetGetDisableMessageTimestampTest() throws Fault {
    boolean pass = true;
    // Test true case
    try {
      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      producer = tool.getDefaultProducer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      queueTest = true;

      boolean expDisableMessageTimestamp = true;
      TestUtil.logMsg("Calling setDisableMessageTimestamp("
          + expDisableMessageTimestamp + ")");
      producer.setDisableMessageTimestamp(expDisableMessageTimestamp);
      TestUtil.logMsg("Calling getDisableMessageTimestamp and expect "
          + expDisableMessageTimestamp + " to be returned");
      boolean actDisableMessageTimestamp = producer
          .getDisableMessageTimestamp();
      if (actDisableMessageTimestamp != expDisableMessageTimestamp) {
        TestUtil.logErr("getDisableMessageTimestamp() returned "
            + actDisableMessageTimestamp + ", expected "
            + expDisableMessageTimestamp);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("queueSetGetDisableMessageTimestampTest");
    }

    // Test false case
    try {
      boolean expDisableMessageTimestamp = false;
      TestUtil.logMsg("Calling setDisableMessageTimestamp("
          + expDisableMessageTimestamp + ")");
      producer.setDisableMessageTimestamp(expDisableMessageTimestamp);
      TestUtil.logMsg("Calling getDisableMessageTimestamp and expect "
          + expDisableMessageTimestamp + " to be returned");
      boolean actDisableMessageTimestamp = producer
          .getDisableMessageTimestamp();
      if (actDisableMessageTimestamp != expDisableMessageTimestamp) {
        TestUtil.logErr("getDisableMessageTimestamp() returned "
            + actDisableMessageTimestamp + ", expected "
            + expDisableMessageTimestamp);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("queueSetGetDisableMessageTimestampTest");
    }

    if (!pass) {
      throw new Fault("queueSetGetDisableMessageTimestampTest failed");
    }
  }

  /*
   * @testName: queueSetGetPriorityTest
   *
   * @assertion_ids: JMS:JAVADOC:305; JMS:JAVADOC:307;
   *
   * @test_Strategy: Test the following APIs:
   *
   * MessageProducer.setPriority(int). MessageProducer.getPriority().
   */
  public void queueSetGetPriorityTest() throws Fault {
    boolean pass = true;
    try {
      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      producer = tool.getDefaultProducer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      queueTest = true;

      // Test default
      int expPriority = 4;
      TestUtil.logMsg(
          "Calling getPriority and expect " + expPriority + " to be returned");
      int actPriority = producer.getPriority();
      if (actPriority != expPriority) {
        TestUtil.logErr("getPriority() returned " + actPriority + ", expected "
            + expPriority);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("queueSetGetPriorityTest");
    }

    // Test non-default
    int expPriority[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

    // Cycle through all priorties
    for (int i = 0; i < expPriority.length; i++) {
      try {
        TestUtil.logMsg("Calling setPriority(" + expPriority[i] + ")");
        producer.setPriority(expPriority[i]);
        TestUtil.logMsg("Calling getPriority and expect " + expPriority[i]
            + " to be returned");
        int actPriority = producer.getPriority();
        if (actPriority != expPriority[i]) {
          TestUtil.logErr("getPriority() returned " + actPriority
              + ", expected " + expPriority[i]);
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught exception: " + e.getMessage());
        throw new Fault("queueSetGetPriorityTest");
      }
    }

    if (!pass) {
      throw new Fault("queueSetGetPriorityTest failed");
    }
  }

  /*
   * @testName: queueSetGetTimeToLiveTest
   *
   * @assertion_ids: JMS:JAVADOC:309; JMS:JAVADOC:311;
   *
   * @test_Strategy: Test the following APIs:
   *
   * MessageProducer.setTimeToLive(long). MessageProducer.getTimeToLive()
   */
  public void queueSetGetTimeToLiveTest() throws Fault {
    boolean pass = true;

    try {
      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      producer = tool.getDefaultProducer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      queueTest = true;

      // Test default
      long expTimeToLive = 0;
      TestUtil.logMsg("Calling getTimeToLive and expect " + expTimeToLive
          + " to be returned");
      long actTimeToLive = producer.getTimeToLive();
      if (actTimeToLive != expTimeToLive) {
        TestUtil.logErr("getTimeToLive() returned " + actTimeToLive
            + ", expected " + expTimeToLive);
        pass = false;
      }

      // Test non-default
      expTimeToLive = 1000;
      TestUtil.logMsg("Calling setTimeToLive(" + expTimeToLive + ")");
      producer.setTimeToLive(expTimeToLive);
      TestUtil.logMsg("Calling getTimeToLive and expect " + expTimeToLive
          + " to be returned");
      actTimeToLive = producer.getTimeToLive();
      if (actTimeToLive != expTimeToLive) {
        TestUtil.logErr("getTimeToLive() returned " + actTimeToLive
            + ", expected " + expTimeToLive);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("queueSetGetTimeToLiveTest");
    }

    if (!pass) {
      throw new Fault("queueSetGetTimeToLiveTest failed");
    }
  }

  /*
   * @testName: queueInvalidDestinationExceptionTests
   *
   * @assertion_ids: JMS:JAVADOC:598; JMS:JAVADOC:601; JMS:JAVADOC:604;
   * JMS:JAVADOC:607;
   *
   * @test_Strategy: Test for InvalidDestinationException from MessageProducer
   * API's.
   *
   * MessageProducer.send(Destination, Message)
   * MessageProducer.send(Destination, Message, init, int, int)
   */
  public void queueInvalidDestinationExceptionTests() throws Fault {
    boolean pass = true;
    TextMessage tempMsg = null;
    String message = "Where are you!";
    try {
      // set up test tool for Queue
      TestUtil.logMsg("Setup JmsTool for COMMON QUEUE");
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Queue) null);
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = null;
      connection.start();
      queueTest = true;

      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "queueInvalidDestinationExceptionTests");

      try {
        TestUtil.logMsg("Send message with invalid destination");
        producer.send(destination, expTextMessage);
        TestUtil.logErr("Didn't throw InvalidDestinationException");
      } catch (InvalidDestinationException e) {
        TestUtil.logMsg("Caught expected InvalidDestinationException");
      } catch (Exception e) {
        TestUtil.logErr(
            "Expected InvalidDestinationException, received " + e.getMessage());
        pass = false;
      }
      try {
        TestUtil.logMsg("Send message with invalid destination");
        producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT,
            Message.DEFAULT_PRIORITY, 0L);
        TestUtil.logErr("Didn't throw InvalidDestinationException");
      } catch (InvalidDestinationException e) {
        TestUtil.logMsg("Caught expected InvalidDestinationException");
      } catch (Exception e) {
        TestUtil.logErr(
            "Expected InvalidDestinationException, received " + e.getMessage());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("queueInvalidDestinationExceptionTests", e);
    } finally {
      try {
        producer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("queueInvalidDestinationExceptionTests failed");
    }
  }

  /*
   * @testName: queueUnsupportedOperationExceptionTests
   *
   * @assertion_ids: JMS:JAVADOC:599; JMS:JAVADOC:602; JMS:JAVADOC:605;
   * JMS:JAVADOC:1318;
   *
   * @test_Strategy: Test for UnsupportedOperationException from MessageProducer
   * API's.
   * 
   * MessageProducer.send(Destination, Message)
   * MessageProducer.send(Destination, Message, init, int, int)
   * MessageProducer.send(Message) MessageProducer.send(Message, init, int, int)
   */
  public void queueUnsupportedOperationExceptionTests() throws Fault {
    boolean pass = true;
    TextMessage tempMsg = null;
    String message = "Where are you!";
    try {
      // set up test tool for Queue
      TestUtil.logMsg("Setup JmsTool for COMMON QUEUE");
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      producer = tool.getDefaultProducer();
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = true;

      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "queueUnsupportedOperationExceptionTests");

      try {
        TestUtil
            .logMsg("Send message with destination specified at creation time");
        producer.send(destination, expTextMessage);
        TestUtil.logErr("Didn't throw UnsupportedOperationException");
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (Exception e) {
        TestUtil.logErr("Expected UnsupportedOperationException, received "
            + e.getMessage());
        pass = false;
      }
      try {
        TestUtil
            .logMsg("Send message with destination specified at creation time");
        producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT,
            Message.DEFAULT_PRIORITY, 0L);
        TestUtil.logErr("Didn't throw UnsupportedOperationException");
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (Exception e) {
        TestUtil.logErr("Expected UnsupportedOperationException, received "
            + e.getMessage());
        pass = false;
      }

      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Queue) null);

      try {
        TestUtil.logMsg(
            "Send message with destination not specified at creation time");
        producer.send(expTextMessage);
        TestUtil.logErr("Didn't throw UnsupportedOperationException");
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (Exception e) {
        TestUtil.logErr("Expected UnsupportedOperationException, received "
            + e.getMessage());
        pass = false;
      }
      try {
        TestUtil.logMsg(
            "Send message with destination not specified at creation time");
        producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT,
            Message.DEFAULT_PRIORITY, 0L);
        TestUtil.logErr("Didn't throw UnsupportedOperationException");
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (Exception e) {
        TestUtil.logErr("Expected UnsupportedOperationException, received "
            + e.getMessage());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("queueUnsupportedOperationExceptionTests", e);
    } finally {
      try {
        producer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("queueUnsupportedOperationExceptionTests failed");
    }
  }

  /*
   * @testName: queueDeliveryDelayTest
   * 
   * @assertion_ids: JMS:SPEC:261; JMS:JAVADOC:907;
   * 
   * @test_Strategy: Send message and verify that message is not delivered until
   * the DeliveryDelay of 30 seconds is reached. Test DeliveryMode.PERSISTENT
   * and DeliveryMode.NON_PERSISTENT.
   *
   * MessageProducer.setDeliveryDelay(30000) MessageProducer.send(Destination,
   * Message, int, int, long)
   */
  public void queueDeliveryDelayTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "-----------------------------------------------------------");
      TestUtil.logMsg(
          "BEGIN TEST queueDeliveryDelayTest with DeliveryDelay=30Secs");
      TestUtil.logMsg(
          "-----------------------------------------------------------");
      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Queue) null);
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = true;

      producer.setDeliveryDelay(30000);

      // Send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage message = session.createTextMessage("This is a test!");

      TestUtil.logMsg("Set StringProperty COM_SUN_JMS_TESTNAME");
      message.setStringProperty("COM_SUN_JMS_TESTNAME",
          "queueDeliveryDelayTest");

      TestUtil.logMsg(
          "Sending message with DeliveryMode.PERSISTENT and DeliveryDelay=30Secs");
      producer.send(destination, message, DeliveryMode.PERSISTENT,
          Message.DEFAULT_PRIORITY, 0L);

      TestUtil.logMsg("Waiting 15 seconds to receive message");
      message = (TextMessage) consumer.receive(15000);
      if (message != null) {
        TestUtil.logErr(
            "FAILED: Message received before delivery delay of 30 secs elapsed");
        pass = false;
      } else {
        TestUtil.logMsg("Message not available after 15 seconds (CORRECT)");
        TestUtil.logMsg("Sleeping 15 more seconds before receiving message");
        Thread.sleep(15000);
        TestUtil.logMsg("Waiting 15 seconds to receive message");
        message = (TextMessage) consumer.receive(15000);
        if (message == null) {
          TestUtil.logErr(
              "FAILED: Message was not received after delivery delay of 30 secs elapsed");
          pass = false;
        } else {
          TestUtil
              .logMsg("Message received after 30 seconds expired (CORRECT)");
        }
      }

      TestUtil.logMsg(
          "Sending message with DeliveryMode.NON_PERSISTENT and DeliveryDelay=30Secs");
      producer.send(destination, message, DeliveryMode.NON_PERSISTENT,
          Message.DEFAULT_PRIORITY, 0L);

      TestUtil.logMsg("Waiting 15 seconds to receive message");
      message = (TextMessage) consumer.receive(15000);
      if (message != null) {
        TestUtil.logErr(
            "FAILED: Message received before delivery delay of 30 secs elapsed");
        pass = false;
      } else {
        TestUtil.logMsg("Message not available after 15 seconds (CORRECT)");
        TestUtil.logMsg("Sleeping 15 more seconds before receiving message");
        Thread.sleep(15000);
        TestUtil.logMsg("Waiting 15 seconds to receive message");
        message = (TextMessage) consumer.receive(15000);
        if (message == null) {
          TestUtil.logErr(
              "FAILED: Message was not received after delivery delay of 30 secs elapsed");
          pass = false;
        } else {
          TestUtil
              .logMsg("Message received after 30 seconds expired (CORRECT)");
        }
      }
      TestUtil
          .logMsg("---------------------------------------------------------");
      TestUtil
          .logMsg("END TEST queueDeliveryDelayTest with DeliveryDelay=30Secs");
      TestUtil
          .logMsg("---------------------------------------------------------");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("queueDeliveryDelayTest", e);
    }

    if (!pass) {
      throw new Fault("queueDeliveryDelayTest failed");
    }
  }

  /*
   * @testName: topicSendAndRecvTest1
   *
   * @assertion_ids: JMS:JAVADOC:321; JMS:JAVADOC:334;
   *
   * @test_Strategy: Send a message using the following API method and verify
   * the send and recv of data:
   *
   * MessageProducer.send(Destination, Message) MessageConsumer.receive(timeout)
   */ public void topicSendAndRecvTest1() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    try {
      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Topic) null);
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = false;

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "topicSendAndRecvTest1");
      TestUtil.logMsg("Sending TextMessage");
      TestUtil.logMsg("Calling MessageProducer.send(Destination, Message)");
      producer.send(destination, expTextMessage);
      TestUtil.logMsg("Receive TextMessage");
      TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
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
      consumer.close();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("topicSendAndRecvTest1", e);
    } finally {
      try {
        producer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("topicSendAndRecvTest1 failed");
    }
  }

  /*
   * @testName: topicSendAndRecvTest2
   *
   * @assertion_ids: JMS:JAVADOC:323; JMS:JAVADOC:334;
   *
   * @test_Strategy: Send a message using the following API method and verify
   * the send and recv of data:
   *
   * MessageProducer.send(Destination, Message, int, int, long)
   * MessageConsumer.receive(timeout)
   */
  public void topicSendAndRecvTest2() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    try {
      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Topic) null);
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = false;

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "topicSendAndRecvTest2");
      TestUtil.logMsg("Sending TextMessage");
      TestUtil.logMsg(
          "Calling MessageProducer.send(Destination, Message, int, int, long)");
      producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT,
          Message.DEFAULT_PRIORITY - 1, 0L);
      TestUtil.logMsg("Receive TextMessage");
      TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg(
          "Check the values in TextMessage, deliverymode, priority, time to live");
      if (!actTextMessage.getText().equals(expTextMessage.getText())
          || actTextMessage.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT
          || actTextMessage.getJMSPriority() != (Message.DEFAULT_PRIORITY - 1)
          || actTextMessage.getJMSExpiration() != 0L) {
        TestUtil.logErr("Didn't get the right message.");
        TestUtil.logErr("text=" + actTextMessage.getText() + ", expected "
            + expTextMessage.getText());
        TestUtil.logErr("DeliveryMode=" + actTextMessage.getJMSDeliveryMode()
            + ", expected " + expTextMessage.getJMSDeliveryMode());
        TestUtil.logErr("Priority=" + actTextMessage.getJMSPriority()
            + ", expected " + expTextMessage.getJMSPriority());
        TestUtil.logErr("TimeToLive=" + actTextMessage.getJMSExpiration()
            + ", expected " + expTextMessage.getJMSExpiration());
        pass = false;
      } else {
        TestUtil.logMsg("TextMessage is correct");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("topicSendAndRecvTest2", e);
    } finally {
      try {
        producer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("topicSendAndRecvTest2 failed");
    }
  }

  /*
   * @testName: topicSendAndRecvTest3
   *
   * @assertion_ids: JMS:JAVADOC:317; JMS:JAVADOC:334;
   *
   * @test_Strategy: Send a message using the following API method and verify
   * the send and recv of data:
   *
   * MessageProducer.send(Message) MessageConsumer.receive(timeout)
   */
  public void topicSendAndRecvTest3() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    try {
      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      producer = tool.getDefaultProducer();
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = false;

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "topicSendAndRecvTest3");
      TestUtil.logMsg("Sending TextMessage");
      TestUtil.logMsg("Calling MessageProducer.send(Message)");
      producer.send(expTextMessage);
      TestUtil.logMsg("Receive TextMessage");
      TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
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
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("topicSendAndRecvTest3", e);
    }

    if (!pass) {
      throw new Fault("topicSendAndRecvTest3 failed");
    }
  }

  /*
   * @testName: topicSendAndRecvTest4
   *
   * @assertion_ids: JMS:JAVADOC:319; JMS:JAVADOC:334;
   *
   * @test_Strategy: Send a message using the following API method and verify
   * the send and recv of data:
   *
   * MessageProducer.send(Message, int, int, long)
   * MessageConsumer.receive(timeout)
   */
  public void topicSendAndRecvTest4() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    try {
      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      producer = tool.getDefaultProducer();
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = false;

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "topicSendAndRecvTest4");
      TestUtil.logMsg("Sending TextMessage");
      TestUtil.logMsg("Calling MessageProducer.send(Message, int, int, long)");
      producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT,
          Message.DEFAULT_PRIORITY - 1, 0L);
      TestUtil.logMsg("Receive TextMessage");
      TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg(
          "Check the values in TextMessage, deliverymode, priority, time to live");
      if (!actTextMessage.getText().equals(expTextMessage.getText())
          || actTextMessage.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT
          || actTextMessage.getJMSPriority() != (Message.DEFAULT_PRIORITY - 1)
          || actTextMessage.getJMSExpiration() != 0L) {
        TestUtil.logErr("Didn't get the right message.");
        TestUtil.logErr("text=" + actTextMessage.getText() + ", expected "
            + expTextMessage.getText());
        TestUtil.logErr("DeliveryMode=" + actTextMessage.getJMSDeliveryMode()
            + ", expected " + expTextMessage.getJMSDeliveryMode());
        TestUtil.logErr("Priority=" + actTextMessage.getJMSPriority()
            + ", expected " + expTextMessage.getJMSPriority());
        TestUtil.logErr("TimeToLive=" + actTextMessage.getJMSExpiration()
            + ", expected " + expTextMessage.getJMSExpiration());
        pass = false;
      } else {
        TestUtil.logMsg("TextMessage is correct");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("topicSendAndRecvTest4", e);
    }

    if (!pass) {
      throw new Fault("topicSendAndRecvTest4 failed");
    }
  }

  /*
   * @testName: topicSetGetDeliveryModeTest
   *
   * @assertion_ids: JMS:JAVADOC:301; JMS:JAVADOC:303;
   *
   * @test_Strategy: Test the following APIs:
   *
   * MessageProducer.setDeliveryMode(int). MessageProducer.getDeliveryMode().
   */
  public void topicSetGetDeliveryModeTest() throws Fault {
    boolean pass = true;

    // Test default case
    try {
      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      producer = tool.getDefaultProducer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      queueTest = false;

      int expDeliveryMode = DeliveryMode.PERSISTENT;
      TestUtil.logMsg("Calling getDeliveryMode and expect " + expDeliveryMode
          + " to be returned");
      int actDeliveryMode = producer.getDeliveryMode();
      if (actDeliveryMode != expDeliveryMode) {
        TestUtil.logErr("getDeliveryMode() returned " + actDeliveryMode
            + ", expected " + expDeliveryMode);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("topicSetGetDeliveryModeTest");
    }

    // Test non-default case
    try {
      int expDeliveryMode = DeliveryMode.NON_PERSISTENT;
      TestUtil.logMsg("Calling setDeliveryMode(" + expDeliveryMode + ")");
      producer.setDeliveryMode(expDeliveryMode);
      TestUtil.logMsg("Calling getDeliveryMode and expect " + expDeliveryMode
          + " to be returned");
      int actDeliveryMode = producer.getDeliveryMode();
      if (actDeliveryMode != expDeliveryMode) {
        TestUtil.logErr("getDeliveryMode() returned " + actDeliveryMode
            + ", expected " + expDeliveryMode);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("topicSetGetDeliveryModeTest");
    }

    if (!pass) {
      throw new Fault("topicSetGetDeliveryModeTest failed");
    }
  }

  /*
   * @testName: topicSetGetDeliveryDelayTest
   *
   * @assertion_ids: JMS:JAVADOC:907; JMS:JAVADOC:886; JMS:SPEC:261;
   *
   * @test_Strategy: Test the following APIs:
   *
   * MessageProducer.setDeliveryDelay(long). MessageProducer.getDeliveryDelay().
   */
  public void topicSetGetDeliveryDelayTest() throws Fault {
    boolean pass = true;

    // Test default case
    try {
      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      producer = tool.getDefaultProducer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      queueTest = false;

      long expDeliveryDelay = 0L;
      TestUtil.logMsg("Calling getDeliveryDelay and expect " + expDeliveryDelay
          + " to be returned");
      long actDeliveryDelay = producer.getDeliveryDelay();
      if (actDeliveryDelay != expDeliveryDelay) {
        TestUtil.logErr("getDeliveryDelay() returned " + actDeliveryDelay
            + ", expected " + expDeliveryDelay);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("topicSetGetDeliveryDelayTest");
    }

    // Test non-default case
    try {
      long expDeliveryDelay = 1L;
      TestUtil.logMsg("Calling setDeliveryDelay(" + expDeliveryDelay + ")");
      producer.setDeliveryDelay(expDeliveryDelay);
      TestUtil.logMsg("Calling getDeliveryDelay and expect " + expDeliveryDelay
          + " to be returned");
      long actDeliveryDelay = producer.getDeliveryDelay();
      if (actDeliveryDelay != expDeliveryDelay) {
        TestUtil.logErr("getDeliveryDelay() returned " + actDeliveryDelay
            + ", expected " + expDeliveryDelay);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("topicSetGetDeliveryDelayTest");
    }

    if (!pass) {
      throw new Fault("topicSetGetDeliveryDelayTest failed");
    }
  }

  /*
   * @testName: topicSetGetDisableMessageIDTest
   *
   * @assertion_ids: JMS:JAVADOC:293; JMS:JAVADOC:295;
   *
   * @test_Strategy: Test the following APIs:
   *
   * MessageProducer.setDisableMessageID(int).
   * MessageProducer.getDisableMessageID().
   */
  public void topicSetGetDisableMessageIDTest() throws Fault {
    boolean pass = true;
    // Test true case
    try {
      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      producer = tool.getDefaultProducer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      queueTest = false;

      boolean expDisableMessageID = true;
      TestUtil
          .logMsg("Calling setDisableMessageID(" + expDisableMessageID + ")");
      producer.setDisableMessageID(expDisableMessageID);
      TestUtil.logMsg("Calling getDisableMessageID and expect "
          + expDisableMessageID + " to be returned");
      boolean actDisableMessageID = producer.getDisableMessageID();
      if (actDisableMessageID != expDisableMessageID) {
        TestUtil.logErr("getDisableMessageID() returned " + actDisableMessageID
            + ", expected " + expDisableMessageID);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("topicSetGetDisableMessageIDTest");
    }

    // Test false case
    try {
      boolean expDisableMessageID = false;
      TestUtil
          .logMsg("Calling setDisableMessageID(" + expDisableMessageID + ")");
      producer.setDisableMessageID(expDisableMessageID);
      TestUtil.logMsg("Calling getDisableMessageID and expect "
          + expDisableMessageID + " to be returned");
      boolean actDisableMessageID = producer.getDisableMessageID();
      if (actDisableMessageID != expDisableMessageID) {
        TestUtil.logErr("getDisableMessageID() returned " + actDisableMessageID
            + ", expected " + expDisableMessageID);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("topicSetGetDisableMessageIDTest");
    }

    if (!pass) {
      throw new Fault("topicSetGetDisableMessageIDTest failed");
    }
  }

  /*
   * @testName: topicSetGetDisableMessageTimestampTest
   *
   * @assertion_ids: JMS:JAVADOC:297; JMS:JAVADOC:299;
   *
   * @test_Strategy: Test the following APIs:
   *
   * MessageProducer.setDisableMessageTimestamp(int).
   * MessageProducer.getDisableMessageTimestamp().
   */
  public void topicSetGetDisableMessageTimestampTest() throws Fault {
    boolean pass = true;
    // Test true case
    try {
      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      producer = tool.getDefaultProducer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      queueTest = false;

      boolean expDisableMessageTimestamp = true;
      TestUtil.logMsg("Calling setDisableMessageTimestamp("
          + expDisableMessageTimestamp + ")");
      producer.setDisableMessageTimestamp(expDisableMessageTimestamp);
      TestUtil.logMsg("Calling getDisableMessageTimestamp and expect "
          + expDisableMessageTimestamp + " to be returned");
      boolean actDisableMessageTimestamp = producer
          .getDisableMessageTimestamp();
      if (actDisableMessageTimestamp != expDisableMessageTimestamp) {
        TestUtil.logErr("getDisableMessageTimestamp() returned "
            + actDisableMessageTimestamp + ", expected "
            + expDisableMessageTimestamp);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("topicSetGetDisableMessageTimestampTest");
    }

    // Test false case
    try {
      boolean expDisableMessageTimestamp = false;
      TestUtil.logMsg("Calling setDisableMessageTimestamp("
          + expDisableMessageTimestamp + ")");
      producer.setDisableMessageTimestamp(expDisableMessageTimestamp);
      TestUtil.logMsg("Calling getDisableMessageTimestamp and expect "
          + expDisableMessageTimestamp + " to be returned");
      boolean actDisableMessageTimestamp = producer
          .getDisableMessageTimestamp();
      if (actDisableMessageTimestamp != expDisableMessageTimestamp) {
        TestUtil.logErr("getDisableMessageTimestamp() returned "
            + actDisableMessageTimestamp + ", expected "
            + expDisableMessageTimestamp);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("topicSetGetDisableMessageTimestampTest");
    }

    if (!pass) {
      throw new Fault("topicSetGetDisableMessageTimestampTest failed");
    }
  }

  /*
   * @testName: topicSetGetPriorityTest
   *
   * @assertion_ids: JMS:JAVADOC:305; JMS:JAVADOC:307;
   *
   * @test_Strategy: Test the following APIs:
   *
   * MessageProducer.setPriority(int). MessageProducer.getPriority().
   */
  public void topicSetGetPriorityTest() throws Fault {
    boolean pass = true;
    try {
      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      producer = tool.getDefaultProducer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      queueTest = false;

      // Test default
      int expPriority = 4;
      TestUtil.logMsg(
          "Calling getPriority and expect " + expPriority + " to be returned");
      int actPriority = producer.getPriority();
      if (actPriority != expPriority) {
        TestUtil.logErr("getPriority() returned " + actPriority + ", expected "
            + expPriority);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("topicSetGetPriorityTest");
    }

    // Test non-default
    int expPriority[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

    // Cycle through all priorties
    for (int i = 0; i < expPriority.length; i++) {
      try {
        TestUtil.logMsg("Calling setPriority(" + expPriority[i] + ")");
        producer.setPriority(expPriority[i]);
        TestUtil.logMsg("Calling getPriority and expect " + expPriority[i]
            + " to be returned");
        int actPriority = producer.getPriority();
        if (actPriority != expPriority[i]) {
          TestUtil.logErr("getPriority() returned " + actPriority
              + ", expected " + expPriority[i]);
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught exception: " + e.getMessage());
        throw new Fault("topicSetGetPriorityTest");
      }
    }

    if (!pass) {
      throw new Fault("topicSetGetPriorityTest failed");
    }
  }

  /*
   * @testName: topicSetGetTimeToLiveTest
   *
   * @assertion_ids: JMS:JAVADOC:309; JMS:JAVADOC:311;
   *
   * @test_Strategy: Test the following APIs:
   *
   * MessageProducer.setTimeToLive(long). MessageProducer.getTimeToLive()
   * 
   * public void topicSetGetTimeToLiveTest() throws Fault { boolean pass = true;
   * 
   * try { // set up test tool for Topic tool = new JmsTool(JmsTool.COMMON_T,
   * user, password, mode); producer = tool.getDefaultProducer(); connection =
   * tool.getDefaultConnection(); session = tool.getDefaultSession();
   * destination = tool.getDefaultDestination(); queueTest = false;
   * 
   * // Test default long expTimeToLive = 0;
   * TestUtil.logMsg("Calling getTimeToLive and expect " + expTimeToLive +
   * " to be returned"); long actTimeToLive = producer.getTimeToLive();
   * if(actTimeToLive != expTimeToLive) {
   * TestUtil.logErr("getTimeToLive() returned "+ actTimeToLive + ", expected "
   * + expTimeToLive); pass = false; }
   * 
   * // Test non-default long expTimeToLive = 1000;
   * TestUtil.logMsg("Calling setTimeToLive("+expTimeToLive+")");
   * producer.setTimeToLive(expTimeToLive);
   * TestUtil.logMsg("Calling getTimeToLive and expect " + expTimeToLive +
   * " to be returned"); long actTimeToLive = producer.getTimeToLive();
   * if(actTimeToLive != expTimeToLive) {
   * TestUtil.logErr("getTimeToLive() returned "+ actTimeToLive + ", expected "
   * + expTimeToLive); pass = false; } } catch (Exception e) {
   * TestUtil.logErr("Caught exception: " + e.getMessage()); throw new
   * Fault("topicSetGetTimeToLiveTest"); }
   * 
   * if (!pass) { throw new Fault("topicSetGetTimeToLiveTest failed"); } }
   * 
   * /*
   * 
   * @testName: topicInvalidDestinationExceptionTests
   *
   * @assertion_ids: JMS:JAVADOC:598; JMS:JAVADOC:601; JMS:JAVADOC:604;
   * JMS:JAVADOC:607;
   *
   * @test_Strategy: Test for InvalidDestinationException from MessageProducer
   * API's.
   * 
   * MessageProducer.send(Destination, Message)
   * MessageProducer.send(Destination, Message, init, int, int)
   */
  public void topicInvalidDestinationExceptionTests() throws Fault {
    boolean pass = true;
    TextMessage tempMsg = null;
    String message = "Where are you!";
    try {
      // set up test tool for Topic
      TestUtil.logMsg("Setup JmsTool for COMMON TOPIC");
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Queue) null);
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = null;
      connection.start();
      queueTest = false;

      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "topicInvalidDestinationExceptionTests");

      try {
        TestUtil.logMsg("Send message with invalid destination");
        producer.send(destination, expTextMessage);
        TestUtil.logErr("Didn't throw InvalidDestinationException");
      } catch (InvalidDestinationException e) {
        TestUtil.logMsg("Caught expected InvalidDestinationException");
      } catch (Exception e) {
        TestUtil.logErr(
            "Expected InvalidDestinationException, received " + e.getMessage());
        pass = false;
      }
      try {
        TestUtil.logMsg("Send message with invalid destination");
        producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT,
            Message.DEFAULT_PRIORITY, 0L);
        TestUtil.logErr("Didn't throw InvalidDestinationException");
      } catch (InvalidDestinationException e) {
        TestUtil.logMsg("Caught expected InvalidDestinationException");
      } catch (Exception e) {
        TestUtil.logErr(
            "Expected InvalidDestinationException, received " + e.getMessage());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("queueInvalidDestinationExceptionTests", e);
    } finally {
      try {
        producer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("topicInvalidDestinationExceptionTests failed");
    }
  }

  /*
   * @testName: topicUnsupportedOperationExceptionTests
   *
   * @assertion_ids: JMS:JAVADOC:599; JMS:JAVADOC:602; JMS:JAVADOC:605;
   * JMS:JAVADOC:1318;
   *
   * @test_Strategy: Test for UnsupportedOperationException from MessageProducer
   * API's.
   *
   * MessageProducer.send(Destination, Message)
   * MessageProducer.send(Destination, Message, init, int, int)
   * MessageProducer.send(Message) MessageProducer.send(Message, init, int, int)
   * 
   */
  public void topicUnsupportedOperationExceptionTests() throws Fault {
    boolean pass = true;
    TextMessage tempMsg = null;
    String message = "Where are you!";
    try {
      // set up test tool for Topic
      TestUtil.logMsg("Setup JmsTool for COMMON TOPIC");
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      producer = tool.getDefaultProducer();
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = false;

      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "topicUnsupportedOperationExceptionTests");

      try {
        TestUtil
            .logMsg("Send message with destination specified at creation time");
        producer.send(destination, expTextMessage);
        TestUtil.logErr("Didn't throw UnsupportedOperationException");
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (Exception e) {
        TestUtil.logErr("Expected UnsupportedOperationException, received "
            + e.getMessage());
        pass = false;
      }
      try {
        TestUtil
            .logMsg("Send message with destination specified at creation time");
        producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT,
            Message.DEFAULT_PRIORITY, 0L);
        TestUtil.logErr("Didn't throw UnsupportedOperationException");
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (Exception e) {
        TestUtil.logErr("Expected UnsupportedOperationException, received "
            + e.getMessage());
        pass = false;
      }

      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Queue) null);

      try {
        TestUtil.logMsg(
            "Send message with destination not specified at creation time");
        producer.send(expTextMessage);
        TestUtil.logErr("Didn't throw UnsupportedOperationException");
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (Exception e) {
        TestUtil.logErr("Expected UnsupportedOperationException, received "
            + e.getMessage());
        pass = false;
      }
      try {
        TestUtil.logMsg(
            "Send message with destination not specified at creation time");
        producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT,
            Message.DEFAULT_PRIORITY, 0L);
        TestUtil.logErr("Didn't throw UnsupportedOperationException");
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (Exception e) {
        TestUtil.logErr("Expected UnsupportedOperationException, received "
            + e.getMessage());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("topicUnsupportedOperationExceptionTests", e);
    } finally {
      try {
        producer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("topicUnsupportedOperationExceptionTests failed");
    }
  }

  /*
   * @testName: topicDeliveryDelayTest
   * 
   * @assertion_ids: JMS:SPEC:261; JMS:JAVADOC:907;
   * 
   * @test_Strategy: Send message and verify that message is not delivered until
   * the DeliveryDelay of 30 seconds is reached. Test DeliveryMode.PERSISTENT
   * and DeliveryMode.NON_PERSISTENT.
   * 
   * MessageProducer.setDeliveryDelay(30000) MessageProducer.send(Destination,
   * Message, int, int, long)
   */
  public void topicDeliveryDelayTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "-----------------------------------------------------------");
      TestUtil.logMsg(
          "BEGIN TEST topicDeliveryDelayTest with DeliveryDelay=30Secs");
      TestUtil.logMsg(
          "-----------------------------------------------------------");
      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Queue) null);
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = false;

      producer.setDeliveryDelay(30000);

      // Send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage message = session.createTextMessage("This is a test!");

      TestUtil.logMsg("Set StringProperty COM_SUN_JMS_TESTNAME");
      message.setStringProperty("COM_SUN_JMS_TESTNAME",
          "topicDeliveryDelayTest");

      TestUtil.logMsg(
          "Sending message with DeliveryMode.PERSISTENT and DeliveryDelay=30Secs");
      producer.send(destination, message, DeliveryMode.PERSISTENT,
          Message.DEFAULT_PRIORITY, 0L);

      TestUtil.logMsg("Waiting 15 seconds to receive message");
      message = (TextMessage) consumer.receive(15000);
      if (message != null) {
        TestUtil.logErr(
            "FAILED: Message received before delivery delay of 30 secs elapsed");
        pass = false;
      } else {
        TestUtil.logMsg("Message not available after 15 seconds (CORRECT)");
        TestUtil.logMsg("Sleeping 15 more seconds before receiving message");
        Thread.sleep(15000);
        TestUtil.logMsg("Waiting 15 seconds to receive message");
        message = (TextMessage) consumer.receive(15000);
        if (message == null) {
          TestUtil.logErr(
              "FAILED: Message was not received after delivery delay of 30 secs elapsed");
          pass = false;
        } else {
          TestUtil
              .logMsg("Message received after 30 seconds expired (CORRECT)");
        }
      }

      TestUtil.logMsg(
          "Sending message with DeliveryMode.NON_PERSISTENT and DeliveryDelay=30Secs");
      producer.send(destination, message, DeliveryMode.NON_PERSISTENT,
          Message.DEFAULT_PRIORITY, 0L);

      TestUtil.logMsg("Waiting 15 seconds to receive message");
      message = (TextMessage) consumer.receive(15000);
      if (message != null) {
        TestUtil.logErr(
            "FAILED: Message received before delivery delay of 30 secs elapsed");
        pass = false;
      } else {
        TestUtil.logMsg("Message not available after 15 seconds (CORRECT)");
        TestUtil.logMsg("Sleeping 15 more seconds before receiving message");
        Thread.sleep(15000);
        TestUtil.logMsg("Waiting 15 seconds to receive message");
        message = (TextMessage) consumer.receive(15000);
        if (message == null) {
          TestUtil.logErr(
              "FAILED: Message was not received after delivery delay of 30 secs elapsed");
          pass = false;
        } else {
          TestUtil
              .logMsg("Message received after 30 seconds expired (CORRECT)");
        }
      }
      TestUtil
          .logMsg("---------------------------------------------------------");
      TestUtil
          .logMsg("END TEST topicDeliveryDelayTest with DeliveryDelay=30Secs");
      TestUtil
          .logMsg("---------------------------------------------------------");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      throw new Fault("topicDeliveryDelayTest", e);
    }

    if (!pass) {
      throw new Fault("topicDeliveryDelayTest failed");
    }
  }

  /*
   * @testName: JMSExceptionTests
   *
   * @assertion_ids: JMS:JAVADOC:302; JMS:JAVADOC:306; JMS:JAVADOC:908;
   * JMS:JAVADOC:310; JMS:JAVADOC:320;
   *
   * @test_Strategy: Test for JMSException from MessageProducer API's.
   * 
   * MessageProducer.setPriority(int) MessageProducer.setDeliveryMode(int)
   * MessageProducer.send(Message, int, int, long) MessageProducer.send(Message,
   * int, int, long, CompletionListener) MessageProducer.send(Destination,
   * Message, int, int, long) MessageProducer.send(Destination, Message, int,
   * int, long, CompletionListener)
   */
  public void JMSExceptionTests() throws Fault {
    boolean pass = true;
    TextMessage tempMsg = null;
    String message = "Where are you!";
    try {
      // set up test tool for Queue
      TestUtil.logMsg("Setup JmsTool for COMMON QUEUE");
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      producer = tool.getDefaultProducer();
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = true;

      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "JMSExceptionTests");

      try {
        TestUtil.logMsg("Try and set an invalid priority of -1");
        TestUtil.logMsg("Calling MessageProducer.setPriorty(-1)");
        producer.setPriority(-1);
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e.getMessage());
        pass = false;
      }
      try {
        TestUtil.logMsg("Try and set an invalid delivery mode of -1");
        TestUtil.logMsg("Calling MessageProducer.setDeliveryMode(-1)");
        producer.setDeliveryMode(-1);
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e.getMessage());
        pass = false;
      }
      try {
        TestUtil.logMsg("Try and send message with delivery mode of -1");
        TestUtil.logMsg(
            "Calling MessageProducer.send(Message, -1, Message.DEFAULT_PRIORITY, 0L)");
        producer.send(expTextMessage, -1, Message.DEFAULT_PRIORITY, 0L);
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e.getMessage());
        pass = false;
      }
      try {
        TestUtil.logMsg("Try and send message with priority of -1");
        TestUtil.logMsg(
            "Calling MessageProducer.send(Message, DeliveryMode.NON_PERSISTENT, -1, 0L)");
        producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT, -1, 0L);
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e.getMessage());
        pass = false;
      }
      try {
        TestUtil.logMsg("Try and send message with delivery mode of -1");
        TestUtil.logMsg(
            "Calling MessageProducer.send(Message, -1, Message.DEFAULT_PRIORITY, 0L, CompletionListener");
        producer.send(expTextMessage, -1, Message.DEFAULT_PRIORITY, 0L,
            new MyCompletionListener());
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e.getMessage());
        pass = false;
      }
      try {
        TestUtil.logMsg("Try and send message with priority of -1");
        TestUtil.logMsg(
            "Calling MessageProducer.send(Message, DeliveryMode.NON_PERSISTENT, -1, 0L, CompletionListener");
        producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT, -1, 0L,
            new MyCompletionListener());
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e.getMessage());
        pass = false;
      }

      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Queue) null);

      try {
        TestUtil.logMsg("Try and send message with delivery mode of -1");
        TestUtil.logMsg(
            "Calling MessageProducer.send(Destination, Message, -1, Message.DEFAULT_PRIORITY, 0L)");
        producer.send(destination, expTextMessage, -1, Message.DEFAULT_PRIORITY,
            0L);
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e.getMessage());
        pass = false;
      }
      try {
        TestUtil.logMsg("Try and send message with priority of -1");
        TestUtil.logMsg(
            "Calling MessageProducer.send(Destination, Message, DeliveryMode.NON_PERSISTENT, -1, 0L)");
        producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT,
            -1, 0L);
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e.getMessage());
        pass = false;
      }
      try {
        TestUtil.logMsg("Try and send message with delivery mode of -1");
        TestUtil.logMsg(
            "Calling MessageProducer.send(Destination, Message, -1, Message.DEFAULT_PRIORITY, 0L, CompletionListener)");
        producer.send(destination, expTextMessage, -1, Message.DEFAULT_PRIORITY,
            0L, new MyCompletionListener());
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e.getMessage());
        pass = false;
      }
      try {
        TestUtil.logMsg("Try and send message with priority of -1");
        TestUtil.logMsg(
            "Calling MessageProducer.send(Destination, Message, DeliveryMode.NON_PERSISTENT, -1, 0L, CompletionListener)");
        producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT,
            -1, 0L, new MyCompletionListener());
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e.getMessage());
        pass = false;
      }

      cleanup();

      // set up test tool for Topic
      TestUtil.logMsg("Setup JmsTool for COMMON TOPIC");
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      producer = tool.getDefaultProducer();
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = false;

      TestUtil.logMsg("Creating TextMessage");
      expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "JMSExceptionTests");

      try {
        TestUtil.logMsg("Try and set an invalid priority of -1");
        TestUtil.logMsg("Calling MessageProducer.setPriorty(-1)");
        producer.setPriority(-1);
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e.getMessage());
        pass = false;
      }
      try {
        TestUtil.logMsg("Try and set an invalid delivery mode of -1");
        TestUtil.logMsg("Calling MessageProducer.setDeliveryMode(-1)");
        producer.setDeliveryMode(-1);
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e.getMessage());
        pass = false;
      }
      try {
        TestUtil.logMsg("Try and send message with delivery mode of -1");
        TestUtil.logMsg(
            "Calling MessageProducer.send(Message, -1, Message.DEFAULT_PRIORITY, 0L)");
        producer.send(expTextMessage, -1, Message.DEFAULT_PRIORITY, 0L);
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e.getMessage());
        pass = false;
      }
      try {
        TestUtil.logMsg("Try and send message with priority of -1");
        TestUtil.logMsg(
            "Calling MessageProducer.send(Message, DeliveryMode.NON_PERSISTENT, -1, 0L)");
        producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT, -1, 0L);
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e.getMessage());
        pass = false;
      }
      try {
        TestUtil.logMsg("Try and send message with delivery mode of -1");
        TestUtil.logMsg(
            "Calling MessageProducer.send(Message, -1, Message.DEFAULT_PRIORITY, 0L, CompletionListener");
        producer.send(expTextMessage, -1, Message.DEFAULT_PRIORITY, 0L,
            new MyCompletionListener());
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e.getMessage());
        pass = false;
      }
      try {
        TestUtil.logMsg("Try and send message with priority of -1");
        TestUtil.logMsg(
            "Calling MessageProducer.send(Message, DeliveryMode.NON_PERSISTENT, -1, 0L, CompletionListener");
        producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT, -1, 0L,
            new MyCompletionListener());
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e.getMessage());
        pass = false;
      }

      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Topic) null);

      try {
        TestUtil.logMsg("Try and send message with delivery mode of -1");
        TestUtil.logMsg(
            "Calling MessageProducer.send(Destination, Message, -1, Message.DEFAULT_PRIORITY, 0L)");
        producer.send(destination, expTextMessage, -1, Message.DEFAULT_PRIORITY,
            0L);
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e.getMessage());
        pass = false;
      }
      try {
        TestUtil.logMsg("Try and send message with priority of -1");
        TestUtil.logMsg(
            "Calling MessageProducer.send(Destination, Message, DeliveryMode.NON_PERSISTENT, -1, 0L)");
        producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT,
            -1, 0L);
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e.getMessage());
        pass = false;
      }
      try {
        TestUtil.logMsg("Try and send message with delivery mode of -1");
        TestUtil.logMsg(
            "Calling MessageProducer.send(Destination, Message, -1, Message.DEFAULT_PRIORITY, 0L, CompletionListener)");
        producer.send(destination, expTextMessage, -1, Message.DEFAULT_PRIORITY,
            0L, new MyCompletionListener());
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e.getMessage());
        pass = false;
      }
      try {
        TestUtil.logMsg("Try and send message with priority of -1");
        TestUtil.logMsg(
            "Calling MessageProducer.send(Destination, Message, DeliveryMode.NON_PERSISTENT, -1, 0L, CompletionListener)");
        producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT,
            -1, 0L, new MyCompletionListener());
        TestUtil.logErr("Didn't throw JMSException");
        pass = false;
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e.getMessage());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("JMSExceptionTests", e);
    }

    if (!pass) {
      throw new Fault("JMSExceptionTests failed");
    }
  }

  private static class MyCompletionListener implements CompletionListener {

    public MyCompletionListener() {
      TestUtil.logMsg("MyCompletionListener()");
    }

    public void onCompletion(Message message) {
      TestUtil.logMsg("onCompletion()");
    }

    public void onException(Message message, Exception exception) {
      TestUtil.logMsg("onException()");
    }
  }
}
