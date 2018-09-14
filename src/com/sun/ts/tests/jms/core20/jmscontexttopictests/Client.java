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
 * $Id: Client.java 68661 2012-11-21 22:51:15Z adf $
 */
package com.sun.ts.tests.jms.core20.jmscontexttopictests;

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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Client extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core20.jmscontexttopictests.Client";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // JMS tool which creates and/or looks up the JMS administered objects
  private transient JmsTool tool = null, tool2 = null;

  // JMS objects
  private transient ConnectionFactory cf = null;

  private transient ConnectionFactory cf2 = null;

  private transient Topic topic = null;

  private transient Destination destination = null;

  private transient Topic topic2 = null;

  private transient Destination destination2 = null;

  private transient JMSContext context = null;

  private transient JMSContext context2 = null;

  private transient JMSProducer producer = null;

  private transient JMSProducer producer2 = null;

  private transient JMSConsumer consumer = null;

  private transient JMSConsumer consumer2 = null;

  // Harness req's
  private Properties props = null;

  // properties read from ts.jte file
  long timeout;

  String user;

  String password;

  String mode;

  String vehicle;

  // used for tests
  private static final int numMessages = 3;

  private static final int iterations = 5;

  ArrayList connections = null;

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
   * helper method verifies that the ConnectionMetaData
   * 
   * @param ConnectionMetaData returned from getJMSMessageID
   * 
   * @return boolean true if ConnectionMetaData is as expected
   */
  private boolean verifyMetaData(ConnectionMetaData data) {
    boolean pass = true;

    try {
      String tmp = data.getJMSVersion();
      TestUtil.logTrace("JMSVersion=" + tmp);

      if (!tmp.equals(JmsTool.JMS_VERSION)) {
        TestUtil.logErr("Error: incorrect JMSVersion=" + tmp);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Error: incorrect type returned for JMSVersion: ", e);
      pass = false;
    }

    try {
      int tmp = data.getJMSMajorVersion();
      TestUtil.logTrace("JMSMajorVersion=" + tmp);

      if (tmp != JmsTool.JMS_MAJOR_VERSION) {
        TestUtil.logErr("Error: incorrect JMSMajorVersion=" + tmp);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Error: incorrect type returned for JMSMajorVersion: ",
          e);
      pass = false;
    }

    try {
      int tmp = data.getJMSMinorVersion();
      TestUtil.logTrace("JMSMinorVersion=" + tmp);

      if (tmp != JmsTool.JMS_MINOR_VERSION) {
        TestUtil.logErr("Error: incorrect JMSMinorVersion=" + tmp);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Error: incorrect type returned for JMSMinorVersion: ",
          e);
      pass = false;
    }

    try {
      String tmp = data.getJMSProviderName();
      TestUtil.logTrace("JMSProviderName=" + tmp);
    } catch (Exception e) {
      TestUtil.logErr("Error: incorrect type returned for JMSProviderName: ",
          e);
      pass = false;
    }

    try {
      String tmp = data.getProviderVersion();
      TestUtil.logTrace("JMSProviderVersion=" + tmp);
    } catch (Exception e) {
      TestUtil.logErr("Error: incorrect type returned for ProviderVersion: ",
          e);
      pass = false;
    }

    try {
      int tmp = data.getProviderMajorVersion();
      TestUtil.logTrace("ProviderMajorVersion=" + tmp);
    } catch (Exception e) {
      TestUtil.logErr(
          "Error: incorrect type returned for ProviderMajorVersion: ", e);
      pass = false;
    }

    try {
      int tmp = data.getProviderMinorVersion();
      TestUtil.logTrace("ProviderMinorVersion=" + tmp);
    } catch (Exception e) {
      TestUtil.logErr(
          "Error: incorrect type returned for ProviderMinorVersion: ", e);
      pass = false;
    }
    return pass;
  }

  /*
   * helper method verifies that the JMSMessageID starts with ID:
   * 
   * @param String returned from getJMSMessageID
   * 
   * @return boolean true if id correctly starts with ID:
   */
  private boolean chkMessageID(String id) {
    String status[] = { "Pass", "Fail" };
    boolean retcode = true;

    // message id must start with ID: - unless it is null
    int index = 0;

    if (id == null) {
      ;
    } else if (id.startsWith("ID:")) {
      ;
    } else {
      index = 1;
      retcode = false;
    }
    logTrace("Results: " + status[index]);
    return retcode;
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
      String lookupDurableTopicFactory = "DURABLE_SUB_CONNECTION_FACTORY";
      String lookupNormalTopicFactory = "MyTopicConnectionFactory";

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

      // set up JmsTool for COMMON_T setup
      TestUtil.logMsg(
          "Setup JmsTool for COMMON_T and normal topic connection factory");
      tool = new JmsTool(JmsTool.COMMON_T, user, password,
          lookupNormalTopicFactory, mode);
      cf = tool.getConnectionFactory();
      tool.getDefaultConnection().close(); // Close connection (Create
                                           // JMSContext to use instead)
      destination = tool.getDefaultDestination();
      topic = (Topic) destination;

      // create JMSContext with AUTO_ACKNOWLEDGE then create consumer/producer
      context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
      producer = context.createProducer();
      consumer = context.createConsumer(topic);

      // set up JmsTool for COMMON_T setup
      TestUtil.logMsg(
          "Setup JmsTool for COMMON_T and durable topic connection factory");
      tool2 = new JmsTool(JmsTool.COMMON_T, user, password,
          lookupDurableTopicFactory, mode);
      tool2.getDefaultConnection().close(); // Close connection (Create
                                            // JMSContext to use instead)
      cf2 = tool2.getConnectionFactory();
      destination2 = tool2.getDefaultDestination();
      topic2 = (Topic) destination2;

      // create second JMSContext with AUTO_ACKNOWLEDGE, then create producer
      context2 = cf2.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
      producer2 = context2.createProducer();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("setup failed!", e);
    }
  }

  /*
   * cleanup() is called after each test
   * 
   * @exception Fault
   */
  public void cleanup() throws Fault {
    try {
      TestUtil.logMsg("Close JMSContext Objects");
      if (context != null) {
        context.close();
        context = null;
      }
      if (context2 != null) {
        context2.close();
        context2 = null;
      }
      producer = producer2 = null;
      TestUtil.logMsg("Close JMSConsumer Objects");
      if (consumer != null) {
        consumer.close();
        consumer = null;
      }
      if (tool != null) {
        tool.closeAllResources();
        tool = null;
      }
      if (tool2 != null) {
        tool2.closeAllResources();
        tool2 = null;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("cleanup failed!", e);
    }
  }

  /*
   * Cleanup method for tests that use durable subscriptions
   */
  private void cleanupSubscription(JMSConsumer consumer, JMSContext context,
      String subName) {
    if (consumer != null) {
      try {
        TestUtil.logTrace("Closing durable consumer: " + consumer);
        consumer.close();
        consumer = null;
      } catch (Exception e) {
        TestUtil.logErr("Exception during JMSConsumer.close: ", e);
      }
    }

    if (context != null) {
      try {
        TestUtil.logTrace("Unsubscribing \"" + subName + "\"");
        context.unsubscribe(subName);
      } catch (Exception e) {
        TestUtil.logErr("Exception during JMSContext.unsubscribe: ", e);
      }
    }
  }

  /*
   * @testName: createTemporayTopicTest
   *
   * @assertion_ids: JMS:JAVADOC:962;
   *
   * @test_Strategy: Test the following APIs:
   *
   * JMSContext.createTemporaryTopic().
   *
   * Send and receive a message to temporary topic. Compare send and recv
   * message for equality.
   */
  public void createTemporayTopicTest() throws Fault {
    boolean pass = true;
    JMSConsumer consumer = null;
    try {

      String sendMessage = "a text message";

      // create a TemporaryTopic
      TestUtil.logMsg("Creating TemporaryTopic");
      TemporaryTopic tempTopic = context.createTemporaryTopic();

      // Create a JMSConsumer for this Temporary Topic
      TestUtil.logMsg("Create JMSConsumer for TemporaryTopic.");
      consumer = context.createConsumer(tempTopic);

      // Send message to temporary topic
      TestUtil.logMsg("Send message to temporary topic");
      producer.send(tempTopic, sendMessage);

      // Receive message from temporary topic
      TestUtil.logMsg("Receive message from temporaty topic");
      String recvMessage = consumer.receiveBody(String.class, timeout);

      TestUtil.logMsg("Checking received message");
      if (recvMessage == null) {
        throw new Fault("Did not receive Message");
      }

      TestUtil.logMsg("Verify correct message received.");
      if (!recvMessage.equals(sendMessage)) {
        TestUtil.logErr("unexpected message: received " + recvMessage
            + " , expected " + sendMessage);
        pass = false;
      } else {
        TestUtil.logMsg(
            "received correct message: " + recvMessage + " as expected");
      }

      TestUtil.logMsg(
          "Attempting to delete temporary topic with an open consumer should not be allowed");
      try {
        tempTopic.delete();
        pass = false;
        TestUtil
            .logErr("TemporaryTopic.delete() didn't throw expected Exception");
      } catch (JMSException em) {
        TestUtil.logTrace("Received expected JMSException: ");
      }

      TestUtil.logMsg("Now close the open consumer");
      try {
        consumer.close();
      } catch (Exception e) {
        TestUtil.logErr("Caught exception closing JMSConsumer: " + e);
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
      TestUtil.printStackTrace(e);
      throw new Fault("createTemporayTopicTest");
    } finally {
      try {
        if (consumer != null)
          consumer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("createTemporayTopicTest failed");
    }
  }

  /*
   * @testName: getMetaDataTest
   *
   * @assertion_ids: JMS:JAVADOC:982;
   *
   * @test_Strategy: Call JMSContext.getMetaData() to retrieve the
   * ConnectionMetaData and then verify the ConnectionMetaData for correctness.
   */
  public void getMetaDataTest() throws Fault {
    boolean pass = true;
    ConnectionMetaData data = null;

    try {
      data = context.getMetaData();

      if (!verifyMetaData(data))
        pass = false;

    } catch (Exception e) {
      TestUtil.logMsg("Caught unexpected exception: " + e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("getMetaDataTest failed");
    }
  }

  /*
   * @testName: getSessionModeTest
   *
   * @assertion_ids: JMS:JAVADOC:986;
   *
   * @test_Strategy: Test the following APIs:
   *
   * JMSContext.getSessionMode().
   *
   * Cycle through all session modes to create each JMSContext with each mode
   * and verify that each session mode is set correctly.
   */
  public void getSessionModeTest() throws Fault {
    boolean pass = true;
    JMSContext context = null;

    // Test default case
    try {
      context = cf.createContext(user, password);
      int expSessionMode = JMSContext.AUTO_ACKNOWLEDGE;
      TestUtil.logMsg("Calling getSessionMode and expect " + expSessionMode
          + " to be returned");
      int actSessionMode = context.getSessionMode();
      if (actSessionMode != expSessionMode) {
        TestUtil.logErr("getSessionMode() returned " + actSessionMode
            + ", expected " + expSessionMode);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("getSessionModeTest");
    } finally {
      try {
        if (context != null)
          context.close();
      } catch (Exception e) {
      }
    }

    // Test non-default case
    int expSessionMode[] = { JMSContext.SESSION_TRANSACTED,
        JMSContext.AUTO_ACKNOWLEDGE, JMSContext.CLIENT_ACKNOWLEDGE,
        JMSContext.DUPS_OK_ACKNOWLEDGE, };

    // Cycle through all session modes
    for (int i = 0; i < expSessionMode.length; i++) {
      if ((vehicle.equals("ejb") || vehicle.equals("jsp")
          || vehicle.equals("servlet"))) {
        if (expSessionMode[i] == JMSContext.SESSION_TRANSACTED
            || expSessionMode[i] == JMSContext.CLIENT_ACKNOWLEDGE)
          continue;
      }
      try {
        TestUtil.logMsg(
            "Creating context with session mode (" + expSessionMode[i] + ")");
        context = cf.createContext(user, password, expSessionMode[i]);
        TestUtil.logMsg("Calling getSessionMode and expect " + expSessionMode[i]
            + " to be returned");
        int actSessionMode = context.getSessionMode();
        if (actSessionMode != expSessionMode[i]) {
          TestUtil.logErr("getSessionMode() returned " + actSessionMode
              + ", expected " + expSessionMode[i]);
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught exception: " + e);
        throw new Fault("getSessionModeTest");
      } finally {
        try {
          if (context != null)
            context.close();
        } catch (Exception e) {
        }
      }
    }

    if (!pass) {
      throw new Fault("getSessionModeTest failed");
    }
  }

  /*
   * @testName: getTransactedTest
   *
   * @assertion_ids: JMS:JAVADOC:990;
   *
   * @test_Strategy: Test the following APIs:
   *
   * JMSContext.getTransacted().
   *
   * Create a JMSContext with JMSContext.AUTO_ACKNOWLEDGE and verify that
   * JMSContext.getTransacted() returns false.
   *
   * Create a JMSContext with JMSContext.SESSION_TRANSACTED and verify that
   * JMSContext.getTransacted() returns true.
   */
  public void getTransactedTest() throws Fault {
    boolean pass = true;
    JMSContext context = null;

    // Test for transacted mode false
    try {
      context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
      boolean expTransacted = false;
      TestUtil.logMsg("Calling getTransacted and expect " + expTransacted
          + " to be returned");
      boolean actTransacted = context.getTransacted();
      if (actTransacted != expTransacted) {
        TestUtil.logErr("getTransacted() returned " + actTransacted
            + ", expected " + expTransacted);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      pass = false;
    } finally {
      try {
        if (context != null)
          context.close();
      } catch (Exception e) {
      }
    }

    // Test for transacted mode true
    if ((vehicle.equals("appclient") || vehicle.equals("standalone"))) {
      try {
        context = cf.createContext(user, password,
            JMSContext.SESSION_TRANSACTED);
        boolean expTransacted = true;
        TestUtil.logMsg("Calling getTransacted and expect " + expTransacted
            + " to be returned");
        boolean actTransacted = context.getTransacted();
        if (actTransacted != expTransacted) {
          TestUtil.logErr("getTransacted() returned " + actTransacted
              + ", expected " + expTransacted);
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Caught exception: " + e);
        throw new Fault("getTransactedTest");
      } finally {
        try {
          if (context != null)
            context.close();
        } catch (Exception e) {
        }
      }
    }

    if (!pass) {
      throw new Fault("getTransactedTest failed");
    }
  }

  /*
   * @testName: setGetAutoStartTest
   *
   * @assertion_ids: JMS:JAVADOC:1138; JMS:JAVADOC:1129;
   *
   * @test_Strategy: Test the following APIs:
   *
   * JMSContext.setAutoStart(boolean). JMSContext.getAutoStart().
   */
  public void setGetAutoStartTest() throws Fault {
    boolean pass = true;
    // Test default case
    try {
      boolean expAutoStart = true;
      TestUtil.logMsg("Calling getAutoStart and expect " + expAutoStart
          + " to be returned");
      boolean actAutoStart = context.getAutoStart();
      if (actAutoStart != expAutoStart) {
        TestUtil.logErr("getAutoStart() returned " + actAutoStart
            + ", expected " + expAutoStart);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      pass = false;
    }

    // Test non-default case
    try {
      boolean expAutoStart = false;
      TestUtil.logMsg("Calling setAutoStart(" + expAutoStart + ")");
      context.setAutoStart(expAutoStart);
      TestUtil.logMsg("Calling getAutoStart and expect " + expAutoStart
          + " to be returned");
      boolean actAutoStart = context.getAutoStart();
      if (actAutoStart != expAutoStart) {
        TestUtil.logErr("getAutoStart() returned " + actAutoStart
            + ", expected " + expAutoStart);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("setGetAutoStartTest");
    }

    if (!pass) {
      throw new Fault("setGetAutoStartTest failed");
    }
  }

  /*
   * @testName: createConsumerTest
   *
   * @assertion_ids: JMS:JAVADOC:942; JMS:JAVADOC:945; JMS:JAVADOC:949;
   *
   * @test_Strategy: Test the following APIs:
   *
   * JMSContext.createConsumer(Destination)
   * JMSContext.createConsumer(Destination, String)
   * JMSContext.createConsumer(Destination, String, boolean)
   * 
   * 1. Send x text messages to a Topic. 2. Create a JMSConsumer with selector
   * to consume just the last message in the Topic. 3. Create a JMSConsumer
   * again to consume the rest of the messages in the Topic. 4. Test
   * createConsumer with noLocal=false case 5. Test createConsumer with
   * noLocal=true case
   */
  public void createConsumerTest() throws Fault {
    boolean pass = true;
    JMSConsumer consumerSelect = null;
    try {
      TextMessage tempMsg = null;
      Enumeration msgs = null;

      // Create selective consumer
      TestUtil.logMsg(
          "Create selective JMSConsumer with selector [\"lastMessage=TRUE\"]");
      consumerSelect = context.createConsumer(destination, "lastMessage=TRUE");

      // Send "numMessages" messages to Topic plus end of stream message
      TestUtil.logMsg("Send " + numMessages + " to Topic");
      for (int i = 1; i <= numMessages; i++) {
        tempMsg = context.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "createConsumerTest" + i);
        if (i == numMessages) {
          TestUtil.logMsg("Set boolean property lastMessage=true");
          tempMsg.setBooleanProperty("lastMessage", true);
        } else {
          TestUtil.logMsg("Set boolean property lastMessage=false");
          tempMsg.setBooleanProperty("lastMessage", false);
        }
        producer.send(destination, tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      // Receive last message in Topic using selective consumer
      TestUtil.logMsg(
          "Receive last message with selective JMSConsumer and boolean property lastMessage=true");
      tempMsg = (TextMessage) consumerSelect.receive(timeout);
      if (tempMsg == null) {
        TestUtil.logErr("JMSConsumer.receive() returned NULL");
        TestUtil.logErr("Message " + numMessages + " missing from Topic");
        pass = false;
      } else if (!tempMsg.getText().equals("Message " + numMessages)) {
        TestUtil.logErr("Received [" + tempMsg.getText()
            + "] expected [Message " + numMessages + "]");
        pass = false;
      } else {
        TestUtil.logMsg("Received expected message: " + tempMsg.getText());
      }

      // Try to receive one more message (should return null)
      TestUtil.logMsg("Try receiving one more message (should get none)");
      tempMsg = (TextMessage) consumerSelect.receive(timeout);
      if (tempMsg != null) {
        TestUtil.logErr("JMSConsumer received message " + tempMsg.getText()
            + " (Expected None)");
        TestUtil.logErr(
            "JMSConsumer with selector should have returned just 1 message");
        pass = false;
      } else {
        TestUtil.logMsg("Received no more messages (CORRECT)");
      }
      consumerSelect.close();
      consumerSelect = null;

      // Receive rest of messages in Topic with normal JMSConsumer
      TestUtil.logMsg("Receive rest of messages with normal JMSConsumer");
      for (int msgCount = 1; msgCount <= numMessages; msgCount++) {
        tempMsg = (TextMessage) consumer.receive(timeout);
        if (tempMsg == null) {
          TestUtil.logErr("JMSConsumer.receive() returned NULL");
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
      tempMsg = (TextMessage) consumer.receive(timeout);
      TestUtil.logMsg("Try receiving one more message (should get none)");
      if (tempMsg != null) {
        TestUtil.logErr("JMSConsumer received message " + tempMsg.getText()
            + " (Expected None)");
        TestUtil.logErr("JMSConsumer should have returned just "
            + (numMessages - 1) + " messages");
        pass = false;
      } else {
        TestUtil.logMsg("Received no more messages (CORRECT)");
      }

      // Create selective JMSConsumer to consume last message in Topic using
      // message
      // selector and noLocal=false
      TestUtil.logMsg(
          "Create selective JMSConsumer with selector [\"lastMessage=TRUE\"] and noLocal=false");
      consumerSelect = context.createConsumer(topic, "lastMessage=TRUE", false);

      // send "numMessages" messages to Topic plus end of stream message
      TestUtil.logMsg("Send " + numMessages + " to Topic");
      for (int i = 1; i <= numMessages; i++) {
        tempMsg = context.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "createConsumerTest" + i);
        if (i == numMessages) {
          TestUtil.logMsg("Set boolean property lastMessage=true");
          tempMsg.setBooleanProperty("lastMessage", true);
        } else {
          TestUtil.logMsg("Set boolean property lastMessage=false");
          tempMsg.setBooleanProperty("lastMessage", false);
        }
        producer.send(destination, tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      TestUtil.logMsg(
          "Receive last message with selective JMSConsumer and boolean property lastMessage=true");
      tempMsg = (TextMessage) consumerSelect.receive(timeout);
      if (tempMsg == null) {
        TestUtil.logErr("JMSConsumer.receive() returned NULL");
        TestUtil.logErr("Message " + numMessages + " missing from Topic");
        pass = false;
      } else if (!tempMsg.getText().equals("Message " + numMessages)) {
        TestUtil.logErr("Received [" + tempMsg.getText()
            + "] expected [Message " + numMessages + "]");
        pass = false;
      } else {
        TestUtil.logMsg("Received expected message: " + tempMsg.getText());
      }

      // Try to receive one more message (should return null)
      TestUtil.logMsg("Try receiving one more message (should get none)");
      tempMsg = (TextMessage) consumerSelect.receive(timeout);
      if (tempMsg != null) {
        TestUtil.logErr("JMSConsumer received message " + tempMsg.getText()
            + " (Expected None)");
        TestUtil.logErr(
            "JMSConsumer with selector should have returned just 1 message");
        pass = false;
      } else {
        TestUtil.logMsg("Received no more messages (CORRECT)");
      }
      consumerSelect.close();
      consumerSelect = null;

      TestUtil.logMsg(
          "Create selective JMSConsumer with selector [\"lastMessage=TRUE\"] and noLocal=true");
      consumerSelect = context.createConsumer(topic, "lastMessage=TRUE", true);

      // Test noLocal=true case
      // send "numMessages" messages to Topic plus end of stream message
      TestUtil.logMsg("Send " + numMessages + " to Topic");
      for (int i = 1; i <= numMessages; i++) {
        tempMsg = context.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "createConsumerTest" + i);
        if (i == numMessages) {
          tempMsg.setBooleanProperty("lastMessage", true);
        } else {
          tempMsg.setBooleanProperty("lastMessage", false);
        }
        producer.send(destination, tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      // Receive last message in Topic using message selector and noLocal=true
      TestUtil.logMsg(
          "Try receiving a message with selective JMSConsumer (should get none)");
      tempMsg = (TextMessage) consumerSelect.receive(timeout);
      if (tempMsg == null) {
        TestUtil.logMsg("Received no message (CORRECT)");
      } else {
        TestUtil.logErr("JMSConsumer received message " + tempMsg.getText()
            + " (Expected None)");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("createConsumerTest");
    } finally {
      try {
        if (consumerSelect != null)
          consumerSelect.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("createConsumerTest failed");
    }
  }

  /*
   * @testName: createDurableConsumerTest1
   *
   * @assertion_ids: JMS:JAVADOC:953;
   *
   * @test_Strategy: Creates a durable subscription with the specified name on
   * the specified topic and creates a MessageConsumer on that durable
   * subscription.
   *
   * This uses a connection factory WITH client identifier set.
   *
   * Tests the following API method:
   *
   * JMSContext.createDurableConsumer(Topic, String)
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
  public void createDurableConsumerTest1() throws Fault {
    boolean pass = true;
    String message1 = "Where are you!";
    String message2 = "Who are you!";
    String durableSubscriptionName = "createDurableConsumerTest1";
    try {
      TextMessage expTextMessage = null;

      // Create Durable Subscription and a MessageConsumer for it
      TestUtil
          .logMsg("Create a Durable Subscription and a MessageConsumer for it");
      consumer2 = context2.createDurableConsumer(topic,
          durableSubscriptionName);

      TestUtil.logMsg("Send TextMessage message1 to Topic");
      expTextMessage = context2.createTextMessage(message1);
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "createDurableConsumerTest1");
      producer2.send(destination2, expTextMessage);
      TestUtil.logMsg("TextMessage message1 sent");

      TestUtil.logMsg("Receive TextMessage message1");
      TextMessage actTextMessage = (TextMessage) consumer2.receive(timeout);
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
      consumer2.close();

      TestUtil.logMsg("Send TextMessage message2 to Topic");
      expTextMessage = context2.createTextMessage(message1);
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "createDurableConsumerTest1");
      producer2.send(destination2, expTextMessage);
      TestUtil.logMsg("TextMessage message2 sent");

      // Recreate Durable Subscription and a MessageConsumer for it
      TestUtil.logMsg(
          "Recreate a Durable Subscription and a MessageConsumer for it");
      consumer2 = context2.createDurableConsumer(topic,
          durableSubscriptionName);

      TestUtil.logMsg("Receive TextMessage message2");
      actTextMessage = (TextMessage) consumer2.receive(timeout);
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
      throw new Fault("createDurableConsumerTest1", e);
    } finally {
      try {
        cleanupSubscription(consumer2, context2, durableSubscriptionName);
        producer2 = null;
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("createDurableConsumerTest1 failed");
    }
  }

  /*
   * @testName: createDurableConsumerTest2
   *
   * @assertion_ids: JMS:JAVADOC:956;
   *
   * @test_Strategy: Creates a durable subscription with the specified name on
   * the specified topic and creates a MessageConsumer on that durable
   * subscription, specifying a message selector and whether messages published
   * by its own connection should be delivered to it.
   *
   * This uses a connection factory WITH client identifier set.
   *
   * Tests the following API method:
   *
   * JMSContext.createDurableConsumer(Topic,String,String,boolean)
   *
   * 1. Create a durable subscription with the specified name on the specified
   * topic and create a durable MessageConsumer on that durable subscription
   * specifing a message selector and whether messages published by its own
   * connection should be delivered to it. This uses a connection factory WITH
   * client identifier set. 2. Send a number of messages to the Topic. 3. Test
   * both noLocal=true and noLocal=false cases. 4. Verify message with specified
   * selector received by MessageConsumer in the noLocal=false case only.
   *
   */
  public void createDurableConsumerTest2() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    String durableSubscriptionName = "createDurableConsumerTest2";
    try {
      // Create Durable Subscription and a MessageConsumer for it
      // Test the noLocal=false case with message selector
      TestUtil.logMsg(
          "Create a Durable Subscription and a MessageConsumer with message selector, noLocal=false");
      consumer2 = context2.createDurableConsumer(topic, durableSubscriptionName,
          "lastMessage = TRUE", false);

      // send "numMessages" messages to Topic
      TestUtil.logMsg("Send " + numMessages + " messages to Topic");
      for (int i = 1; i <= numMessages; i++) {
        TextMessage tempMsg = context.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "createDurableConsumerTest2" + i);
        if (i == numMessages) {
          tempMsg.setBooleanProperty("lastMessage", true);
        } else {
          tempMsg.setBooleanProperty("lastMessage", false);
        }
        producer2.send(destination2, tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      TestUtil.logMsg("Receive TextMessage");
      TestUtil.logMsg(
          "This is noLacal=false case so expect to get just last message");
      TextMessage expTextMessage = context2
          .createTextMessage("Message " + numMessages);
      TextMessage actTextMessage = (TextMessage) consumer2.receive(timeout);
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
      throw new Fault("createDurableConsumerTest2", e);
    } finally {
      try {
        if (consumer2 != null)
          consumer2.close();
      } catch (Exception e) {
      }
    }

    try {
      // Create Durable Subscription and a MessageConsumer for it
      // Test the noLocal=true case with message selector
      TestUtil.logMsg(
          "Create a Durable Subscription and a MessageConsumer with message selector, noLocal=true");
      consumer2 = context2.createDurableConsumer(topic, durableSubscriptionName,
          "lastMessage = TRUE", true);

      // send "numMessages" messages to Topic
      TestUtil.logMsg("Send " + numMessages + " messages to Topic");
      for (int i = 1; i <= numMessages; i++) {
        TextMessage tempMsg = context2.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "createDurableConsumerTest2" + i);
        if (i == numMessages) {
          tempMsg.setBooleanProperty("lastMessage", true);
        } else {
          tempMsg.setBooleanProperty("lastMessage", false);
        }
        producer2.send(destination2, tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      TestUtil.logMsg("Receive TextMessage");
      TextMessage actTextMessage = (TextMessage) consumer2.receive(timeout);

      if (actTextMessage != null) {
        TestUtil.logErr("Message was delivered when noLocal=true");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      e.printStackTrace();
      throw new Fault("createDurableConsumerTest2", e);
    } finally {
      try {
        cleanupSubscription(consumer2, context2, durableSubscriptionName);
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("createDurableConsumerTest2 failed");
    }
  }

  /*
   * @testName: createSharedDurableConsumerTest1
   *
   * @assertion_ids: JMS:JAVADOC:1382;
   *
   * @test_Strategy: Creates a shared durable subscription with the specified
   * name on the specified topic and creates a JMSConsumer on that durable
   * subscription.
   *
   * This uses a connection factory WITH client identifier set.
   *
   * Tests the following API method:
   *
   * JMSContext.createSharedDurableConsumer(Topic, String)
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
  public void createSharedDurableConsumerTest1() throws Fault {
    boolean pass = true;
    String message1 = "Message1!";
    String message2 = "Message2!";
    String durableSubscriptionName = "createSharedDurableConsumerTest1";
    try {
      // Close default consumer
      consumer.close();

      TextMessage expTextMessage = null;

      // Create a shared Durable Subscription and a JMSConsumer for it
      TestUtil.logMsg(
          "Create a shared Durable Subscription and 1st JMSConsumer for it");
      consumer = context2.createSharedDurableConsumer(topic,
          durableSubscriptionName);

      // Create 2nd JMSConsumer for it
      TestUtil.logMsg("Create 2nd JMSConsumer for it");
      consumer2 = context2.createSharedDurableConsumer(topic,
          durableSubscriptionName);

      TestUtil.logMsg("Send TextMessage message1 to Topic");
      expTextMessage = context2.createTextMessage(message1);
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "createSharedDurableConsumerTest1");
      producer2.send(destination2, expTextMessage);
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
      expTextMessage = context2.createTextMessage(message2);
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "createSharedDurableConsumerTest1");
      producer2.send(destination2, expTextMessage);
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
      consumer = context2.createSharedDurableConsumer(topic,
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
      throw new Fault("createSharedDurableConsumerTest1", e);
    } finally {
      try {
        producer2 = null;
        if (consumer != null)
          consumer.close();
        cleanupSubscription(consumer2, context2, durableSubscriptionName);
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
      }
    }

    if (!pass) {
      throw new Fault("createSharedDurableConsumerTest1 failed");
    }
  }

  /*
   * @testName: createSharedDurableConsumerTest2
   *
   * @assertion_ids: JMS:JAVADOC:1385;
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
   * JMSContext.createSharedDurableConsumer(Topic,String,String)
   *
   * 1. Create a shared durable subscription with the specified name on the
   * specified topic and create a durable JMSConsumer on that durable
   * subscription specifing a message selector and whether messages published by
   * its own connection should be delivered to it. This uses a connection
   * factory WITH client identifier set. 2. Create a 2nd JMSConsumer for it. 3.
   * Send a number of messages to the Topic.
   *
   */
  public void createSharedDurableConsumerTest2() throws Fault {
    boolean pass = true;
    String durableSubscriptionName = "createSharedDurableConsumerTest2";
    try {
      // Close default consumer
      consumer.close();

      // Create a shared Durable Subscription and a JMSConsumer for it
      TestUtil.logMsg(
          "Create shared Durable Subscription and 1st JMSConsumer with message selector");
      consumer = context2.createSharedDurableConsumer(topic,
          durableSubscriptionName, "lastMessage = TRUE");
      // Create 2nd JMSConsumer for it
      TestUtil.logMsg("Create 2nd JMSConsumer with message selector");
      consumer2 = context2.createSharedDurableConsumer(topic,
          durableSubscriptionName, "lastMessage = TRUE");

      // send "numMessages" messages to Topic
      TestUtil.logMsg("Send " + numMessages + " messages to Topic");
      for (int i = 1; i <= numMessages; i++) {
        TextMessage tempMsg = context.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "createSharedDurableConsumerTest2" + i);
        if (i == numMessages) {
          tempMsg.setBooleanProperty("lastMessage", true);
        } else {
          tempMsg.setBooleanProperty("lastMessage", false);
        }
        producer2.send(destination2, tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      TestUtil.logMsg("Receive TextMessage from consumer1");
      TextMessage expTextMessage = context2
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
      TestUtil.logErr("Caught exception: " + e);
      e.printStackTrace();
      throw new Fault("createSharedDurableConsumerTest2", e);
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

    if (!pass) {
      throw new Fault("createSharedDurableConsumerTest2 failed");
    }
  }

  /*
   * @testName: createSharedDurableConsumerTest3
   *
   * @assertion_ids: JMS:JAVADOC:1382;
   *
   * @test_Strategy: Creates a shared durable subscription with the specified
   * name on the specified topic and creates a JMSConsumer on that durable
   * subscription.
   *
   * This uses a connection factory WITH client identifier unset.
   *
   * Tests the following API method:
   *
   * JMSContext.createSharedDurableConsumer(Topic, String)
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
  public void createSharedDurableConsumerTest3() throws Fault {
    boolean pass = true;
    String message1 = "Message1!";
    String message2 = "Message2!";
    String durableSubscriptionName = "createSharedDurableConsumerTest3";
    try {
      // Close default consumer
      consumer.close();

      TextMessage expTextMessage = null;

      // Create a shared Durable Subscription and a JMSConsumer for it
      TestUtil.logMsg(
          "Create a shared Durable Subscription and 1st JMSConsumer for it");
      consumer = context.createSharedDurableConsumer(topic,
          durableSubscriptionName);

      // Create 2nd JMSConsumer for it
      TestUtil.logMsg("Create 2nd JMSConsumer for it");
      consumer2 = context.createSharedDurableConsumer(topic,
          durableSubscriptionName);

      TestUtil.logMsg("Send TextMessage message1 to Topic");
      expTextMessage = context.createTextMessage(message1);
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "createSharedDurableConsumerTest3");
      producer2.send(destination, expTextMessage);
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
      expTextMessage = context.createTextMessage(message2);
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "createSharedDurableConsumerTest3");
      producer2.send(destination, expTextMessage);
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
      consumer = context.createSharedDurableConsumer(topic,
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
      throw new Fault("createSharedDurableConsumerTest3", e);
    } finally {
      try {
        producer2 = null;
        if (consumer != null)
          consumer.close();
        cleanupSubscription(consumer2, context, durableSubscriptionName);
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
      }
    }

    if (!pass) {
      throw new Fault("createSharedDurableConsumerTest3 failed");
    }
  }

  /*
   * @testName: createSharedConsumerTest1
   *
   * @assertion_ids: JMS:JAVADOC:1151; JMS:SPEC:269;
   *
   * @test_Strategy: Creates a shared non-durable subscription with the
   * specified name on the specified topic, and creates a JMSConsumer on that
   * subscription.
   *
   * Tests the following API method:
   *
   * JMSContext.createSharedConsumer(Topic, String)
   *
   * 1. Creates a shared non-durable subscription with the specified name on the
   * specified topic, and creates a JMSConsumer on that subscription. 2. Create
   * a second JMSConsumer on that subscription. 3. Send a text message to the
   * Topic. 4. Consume message via the first JMSConsumer and message should be
   * received. 5. Attempt to consume message via second JMSConsumer and no
   * message should be received. 6. Re-Send a text message to the Topic. 7.
   * Consume message via the second JMSConsumer and message should be received.
   * 8. Attempt to consume message via first JMSConsumer and no message should
   * be received.
   */
  public void createSharedConsumerTest1() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    String sharedSubscriptionName = "createSharedConsumerTest1";
    try {
      TextMessage expTextMessage = null;

      // Create shared non-durable Subscription and a JMSConsumer for it
      TestUtil.logMsg(
          "Create a shared non-durable Subscription and a JMSConsumer for it");
      consumer.close();
      consumer = context.createSharedConsumer(topic, sharedSubscriptionName);

      // Create a second JMSConsumer for the subscription
      TestUtil.logMsg("Create a second JMSConsumer for the Subscription");
      consumer2 = context.createSharedConsumer(topic, sharedSubscriptionName);

      TestUtil.logMsg("Send message to Topic");
      expTextMessage = context.createTextMessage(message);
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          sharedSubscriptionName);
      producer.send(destination, expTextMessage);
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
      expTextMessage = context.createTextMessage(message);
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          sharedSubscriptionName);
      producer.send(destination, expTextMessage);
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
      throw new Fault("createSharedConsumerTest1", e);
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
      throw new Fault("createSharedConsumerTest1 failed");
    }
  }

  /*
   * @testName: createSharedConsumerTest2
   *
   * @assertion_ids: JMS:JAVADOC:1155; JMS:SPEC:269; JMS:SPEC:270;
   *
   * @test_Strategy: Creates a shared non-durable subscription with the
   * specified name on the specified topic and creates a JMSConsumer on that
   * subscription, specifying a message selector.
   *
   * Tests the following API method:
   *
   * JMSContext.createSharedConsumer(Topic, String, String)
   *
   * 1. Create a shared non-durable subscription with the specified name on the
   * specified topic, and creates a JMSConsumer on that subscription, specifying
   * a message selector. 2. Create a second JMSConsumer on that subscription. 3.
   * Send a text message to the Topic. 4. Consume message via first JMSConsumer
   * and msg selector and message should be received. 5. Attempt to consume
   * message via second JMSConsumer and msg selector and no message received. 6.
   * Re-Send a text message to the Topic. 7. Consume message via second
   * JMSConsumer and msg selector and message should be received. 8. Attempt to
   * consume message via first JMSConsumer and msg selector and no message
   * received.
   *
   */
  public void createSharedConsumerTest2() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    String sharedSubscriptionName = "createSharedConsumerTest2";
    try {
      // Create shared non-durable Subscription and a JMSConsumer for it
      TestUtil.logMsg(
          "Create a shared non-durable Subscription and a JMSConsumer for it");
      consumer.close();
      consumer = context.createSharedConsumer(topic, sharedSubscriptionName,
          "lastMessage = TRUE");

      // Create a second JMSConsumer for the subscription
      TestUtil.logMsg("Create a second JMSConsumer for the Subscription");
      consumer2 = context.createSharedConsumer(topic, sharedSubscriptionName,
          "lastMessage = TRUE");

      // send "numMessages" messages to Topic
      TestUtil.logMsg("Send " + numMessages + " to Topic");
      for (int i = 1; i <= numMessages; i++) {
        TextMessage tempMsg = context.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            sharedSubscriptionName + i);
        if (i == numMessages) {
          tempMsg.setBooleanProperty("lastMessage", true);
        } else {
          tempMsg.setBooleanProperty("lastMessage", false);
        }
        producer.send(destination, tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      TestUtil.logMsg("Receive TextMessage from consumer1");
      TextMessage expTextMessage = context
          .createTextMessage("Message " + numMessages);
      TextMessage actTextMessage = (TextMessage) consumer.receive(timeout);
      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
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
        TextMessage tempMsg = context.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            sharedSubscriptionName + i);
        if (i == numMessages) {
          tempMsg.setBooleanProperty("lastMessage", true);
        } else {
          tempMsg.setBooleanProperty("lastMessage", false);
        }
        producer.send(destination, tempMsg);
        TestUtil.logMsg("Message " + i + " sent");
      }

      TestUtil.logMsg("Receive TextMessage from consumer2");
      expTextMessage = context.createTextMessage("Message " + numMessages);
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
      throw new Fault("createSharedConsumerTest2", e);
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
      throw new Fault("createSharedConsumerTest2 failed");
    }
  }

  /*
   * @testName: multipleCloseContextTest
   * 
   * @assertion_ids: JMS:JAVADOC:912; JMS:SPEC:108;
   * 
   * @test_Strategy: Call close() twice on a JMSContext. This MUST NOT throw an
   * exception.
   */
  public void multipleCloseContextTest() throws Fault {
    try {
      TestUtil.logTrace("Call close on JMSContext created in setup.");
      context.close();
      TestUtil.logTrace("Call close on a JMSContext a second time");
      context.close();
    } catch (Exception e) {
      TestUtil.logMsg("Caught unexpected exception: " + e);
      throw new Fault("multipleCloseContextTest");
    }
  }

  /*
   * @testName: simpleDurableConsumerTest
   * 
   * @assertion_ids: JMS:JAVADOC:1084; JMS:JAVADOC:953;
   * 
   * @test_Strategy: Send single message to a topic and verify receipt of it
   * with a durable subscriber. This uses a connection factory WITHOUT client
   * identifier set. Tests the use of the following API's:
   *
   * o JMSContext.createDurableConsumer(Topic, String) o
   * JMSContext.unsubscribe(String)
   */
  public void simpleDurableConsumerTest() throws Fault {
    String durableSubscriptionName = "simpleDurableConsumerTest";

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;

      TestUtil.logMsg("Create DurableConsumer with subscriber name");
      consumer2 = context2.createDurableConsumer(topic,
          durableSubscriptionName);

      TestUtil.logMsg("Creating and sending 1 message");
      messageSent = context2.createTextMessage("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          durableSubscriptionName);
      producer.send(destination, messageSent);
      TestUtil.logMsg("Receiving message");
      messageReceived = (TextMessage) consumer2.receive(timeout);

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        TestUtil.logMsg("Message text: \"" + messageReceived.getText() + "\"");
        TestUtil.logMsg("Received correct message");
      } else {
        throw new Exception("didn't get the right message");
      }

    } catch (Exception e) {
      TestUtil.logMsg("Caught unexpected exception: " + e);
      throw new Fault("simpleDurableConsumerTest");
    } finally {
      try {
        cleanupSubscription(consumer2, context2, durableSubscriptionName);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: inactiveDurableConsumerTopicRecTest
   * 
   * @assertion_ids: JMS:JAVADOC:953; JMS:JAVADOC:1084; JMS:JAVADOC:1098;
   * 
   * @test_Strategy: Send and receive a message from a topic. Inactivate the
   * subscriber, send another message. Verify that when the subscriber is
   * activated the message is received. This uses a connection factory WITH
   * client identifier set.
   *
   * o JMSContext.createDurableConsumer(Topic, String) o JMSConsumer.close() o
   * JMSContext.unsubscribe(String)
   */
  public void inactiveDurableConsumerTopicRecTest() throws Fault {
    String durableSubscriptionName = "inactiveDurableConsumerTopicRecTest";

    try {
      TextMessage messageSent = null;
      TextMessage messageSent2 = null;
      TextMessage messageReceived = null;

      // Create Durable Subscription and a JMSConsumer for it
      TestUtil.logMsg("Create a Durable Subscription and a JMSConsumer for it");
      consumer2 = context2.createDurableConsumer(topic2,
          durableSubscriptionName);

      // send message
      TestUtil.logTrace("Creating and sending 1 message");
      messageSent = context2.createTextMessage("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          durableSubscriptionName);
      producer2.send(destination2, messageSent);

      TestUtil.logTrace("Receiving message");
      messageReceived = (TextMessage) consumer2.receive(timeout);

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        TestUtil
            .logTrace("Message text: \"" + messageReceived.getText() + "\"");
        TestUtil.logMsg("Received correct message");
      } else {
        throw new Fault("didn't get the right message");
      }

      // make the durable subscriber inactive
      consumer2.close();

      // send more messages
      TestUtil.logTrace("Creating and sending another message");
      messageSent2 = context2
          .createTextMessage("test that messages are durable");
      messageSent2.setStringProperty("COM_SUN_JMS_TESTNAME",
          durableSubscriptionName);
      producer2.send(destination2, messageSent2);

      // Create Durable Subscription and a JMSConsumer for it
      TestUtil.logMsg("Create a Durable Subscription and a JMSConsumer for it");
      consumer2 = context2.createDurableConsumer(topic2,
          durableSubscriptionName);

      messageReceived = (TextMessage) consumer2.receive(timeout);

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent2.getText())) {
        TestUtil
            .logTrace("Message text: \"" + messageReceived.getText() + "\"");
        TestUtil.logTrace("Received correct message");
      } else {
        throw new Fault("Received incorrect message.");
      }

    } catch (Exception e) {
      TestUtil.logMsg("Caught unexpected exception: " + e);
      throw new Fault("inactiveDurableConsumerTopicRecTest");
    } finally {
      try {
        cleanupSubscription(consumer2, context2, durableSubscriptionName);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: durableConsumerTopicNoLocalTest
   * 
   * @assertion_ids: JMS:SPEC:161; JMS:SPEC:164; JMS:SPEC:165; JMS:JAVADOC:256;
   * JMS:JAVADOC:99; JMS:JAVADOC:334;
   * 
   * 
   * @test_Strategy: 1) Create topic connection with normal consumer and
   * (no_local=true) durable consumer. 2) Publish x messages to topic and
   * receive them with normal consumer. 3) Try and receive messages with
   * (no_local=true) durable consumer and verify that you cannot receive them.
   * 4) Publish x more messages to topic. 4) Close the (no_local=true) durable
   * consumer. 5) Create a new (no_local=false) durable consumer with the same
   * subscription name and same topic as (no_local=true) durable consumer. 6)
   * Try and receive messages with (no_local=false) durable consumer. Verify
   * that you cannot receive any messages. Recreating a durable consumer with a
   * change to (no_local setting) causes previous durable subscription to become
   * invalid so all the old messages are deleted and you start anew with a clean
   * slate.
   *
   * A client can change an existing durable subscription by creating a durable
   * JMSConsumer with the same name and topic but different (no_local setting).
   * Changing a durable consumer is equivalent to unsubscribing (deleting) the
   * old one and creating a new one.
   *
   * So if a client subsequently changes the no_local setting, all the existing
   * messages stored in the durable subscription become invalid since they are
   * inconsistent with the new no_local setting. The only safe thing to do is to
   * delete all the old messages and start anew.
   *
   * This uses a connection factory WITH client identifier set.
   */
  public void durableConsumerTopicNoLocalTest() throws Fault {
    JMSConsumer tConNoLocal = null;
    String subscriptionName = "DurableConsumerTopicNoLocalTestSubscription";
    String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

    try {
      int num = 10;
      Message messageSent = null;
      Message messageReceived = null;

      // create normal Consumer from JMSContext
      TestUtil.logMsg("Create normal Consumer");
      consumer2 = context2.createConsumer(topic);

      // create DurableConsumer with no_local=true from JMSContext
      TestUtil.logMsg("Create DurableConsumer with no_local=true");
      tConNoLocal = context2.createDurableConsumer(topic, subscriptionName, "",
          true);

      // publish messages to topic
      TestUtil.logMsg("Sending " + num + " messages to topic");
      messageSent = context2.createMessage();
      messageSent.setBooleanProperty("lastMessage", false);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "durableConsumerTopicNoLocalTest");
      for (int i = 0; i < num; i++) {
        producer2.send(destination2, messageSent);
      }

      // receive messages with default normal consumer
      TestUtil.logMsg("Attempting to receive messages from normal consumer");
      for (int i = 0; i < num; i++) {
        messageReceived = consumer2.receive(timeout);
        if (messageReceived == null) {
          throw new Fault("Should have received message");
        } else if (messageReceived.getBooleanProperty("lastMessage") == false) {
          TestUtil.logMsg("Received correct message lastMessage=false");
        } else {
          throw new Fault("Received incorrect message lastMessage=true");
        }
      }

      // try and receive with (no_local=true) consumer (should not receive any
      // messages)
      TestUtil.logMsg(
          "Attempting to receive messages from (no_local=true) consumer");
      messageReceived = tConNoLocal.receive(timeout);
      if (messageReceived == null) {
        TestUtil.logMsg("Did not receive message (correct)");
      } else {
        throw new Fault("Received unexpected message (incorrect)");
      }

      // publish more messages to topic
      TestUtil.logMsg("Sending " + num + " messages to topic");
      for (int i = 0; i < num; i++) {
        producer2.send(destination2, messageSent);
      }

      // need to inactivate durable consumer before creating new durable
      // consumer
      TestUtil.logMsg("Close DurableConsumer with no_local=true");
      tConNoLocal.close();

      // create new DurableConsumer with no_local=false
      TestUtil.logMsg("Create DurableConsumer with no_local=false");
      tConNoLocal = context2.createDurableConsumer(topic, subscriptionName, "",
          false);

      // try and receive a message from this new durable consumer with
      // (no_local=false)
      // should not receive any messages because creating a new DurableConsumer
      // with a
      // different (no_local=false) setting will delete the previous
      // subscription and any
      // messages that were queued
      messageReceived = tConNoLocal.receive(timeout);
      if (messageReceived == null) {
        TestUtil.logMsg(
            "No_local=false consumer did not receive any message (expected)");
      } else {
        throw new Fault(
            "No_local=false consumer received message (unexpected)");
      }

      // publish more messages to topic
      TestUtil.logMsg("Sending " + num + " messages to topic");
      messageSent = context2.createMessage();
      messageSent.setBooleanProperty("lastMessage", false);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "durableConsumerTopicNoLocalTest");
      for (int i = 0; i < num; i++) {
        producer2.send(destination2, messageSent);
      }

      // receive messages with (no_local=false) consumer (should receive all
      // messages)
      TestUtil.logMsg(
          "Attempting to receive messages from (no_local=false) consumer");
      for (int i = 0; i < num; i++) {
        messageReceived = tConNoLocal.receive(timeout);
        if (messageReceived == null) {
          throw new Fault("Should have received message");
        } else if (messageReceived.getBooleanProperty("lastMessage") == false) {
          TestUtil.logMsg("Received correct message lastMessage=false");
        } else {
          throw new Fault("Received incorrect message lastMessage=true");
        }
      }

      // try and receive one more message (there should be none at this point)
      messageReceived = tConNoLocal.receive(timeout);
      if (messageReceived != null) {
        throw new Fault("Received unexpected final message");
      }
    } catch (Exception e) {
      TestUtil.logMsg("Caught unexpected exception: " + e);
      throw new Fault("durableConsumerTopicNoLocalTest2");
    } finally {
      try {
        if (consumer2 != null)
          consumer2.close();
        cleanupSubscription(tConNoLocal, context2, subscriptionName);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: durableConsumerChangeSelectorTest
   * 
   * @assertion_ids: JMS:SPEC:164; JMS:SPEC:165; JMS:JAVADOC:122;
   * JMS:JAVADOC:256; JMS:JAVADOC:99; JMS:JAVADOC:334;
   * 
   * @test_Strategy: Create a durable consumer for the default topic. Create a
   * durable topic consumer again, use the same name as the above but change the
   * selector.
   *
   * This uses a connection factory WITH client identifier set.
   */
  public void durableConsumerChangeSelectorTest() throws Fault {
    Topic newTestTopic;
    JMSConsumer durableCon = null;
    String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;

      // Create a durable consumer with a specified message selector
      TestUtil.logMsg(
          "Create DurableConsumer with MessageSelector=TEST='test' and no_local=false");
      durableCon = context2.createDurableConsumer(topic,
          "durableConsumerChangeSelectorTest", "TEST = 'test'", false);

      // publish and receive message with specified message selector
      TestUtil.logMsg("Send and receive the message");
      messageSent = context2.createTextMessage();
      messageSent.setStringProperty("TEST", "test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "durableConsumerChangeSelectorTest");
      messageSent.setText("For default topic ");
      producer2.send(destination2, messageSent);
      TestUtil.logMsg("Receiving message");
      messageReceived = (TextMessage) durableCon.receive(timeout);

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        TestUtil.logMsg("Message text: \"" + messageReceived.getText() + "\"");
        TestUtil.logMsg("Received correct message");
      } else {
        throw new Fault("didn't get the right message");
      }

      // need to inactivate topic consumer before switching to selector
      durableCon.close();

      // Create new durable consumer with a different message selector specified
      TestUtil.logMsg(
          "Create DurableConsumer with new MessageSelector=TEST='new one' and no_local=false");
      durableCon = context2.createDurableConsumer(topic,
          "durableConsumerChangeSelectorTest", "TEST = 'new one'", false);

      // Publish message with old message selector
      messageSent.setBooleanProperty("lastMessage", false);
      producer2.send(destination2, messageSent);

      // Create and Publish a message with the new message selector
      messageSent = context2.createTextMessage();
      messageSent.setStringProperty("TEST", "new one");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "durableConsumerChangeSelectorTest");
      messageSent.setText("For new topic");
      messageSent.setBooleanProperty("lastMessage", true);
      producer2.send(destination2, messageSent);

      // receive message
      messageReceived = (TextMessage) durableCon.receive(timeout);
      if (messageReceived != null) {
        if (messageReceived.getText().equals(messageSent.getText())
            && messageReceived.getBooleanProperty("lastMessage") == true) {
          TestUtil
              .logMsg("Message text: \"" + messageReceived.getText() + "\"");
          TestUtil.logMsg("Received correct message");
        } else {
          throw new Fault("didn't get the right message");
        }
      } else {
        throw new Fault("didn't get any message");
      }

    } catch (Exception e) {
      TestUtil.logMsg("Caught unexpected exception: " + e);
      throw new Fault("durableConsumerChangeSelectorTest");
    } finally {
      try {
        cleanupSubscription(durableCon, context2,
            "durableConsumerChangeSelectorTest");
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: durableConsumerChangeSelectorTest2
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
   * JMSConsumer with the same name and a new topic and/or message selector.
   * Changing a durable consumer is equivalent to unsubscribing (deleting) the
   * old one and creating a new one.
   *
   * So if a client subsequently changes the message selector, all the existing
   * messages stored in the durable subscription become invalid since they are
   * inconsistent with the new message selector. The only safe thing to do is to
   * delete all the old messages and start anew.
   *
   * This uses a connection factory WITH client identifier set.
   */
  public void durableConsumerChangeSelectorTest2() throws Fault {
    Topic newTestTopic;
    JMSConsumer durableCon = null;
    String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

    try {
      TextMessage messageSent = null;
      TextMessage messageSent2 = null;
      TextMessage messageReceived = null;

      // Create a durable consumer with a specified message selector
      TestUtil.logMsg(
          "Create durable subscription with MessageSelector=\"TEST='test'\",");
      TestUtil.logMsg("TopicName=" + topic.getTopicName()
          + " and SubscriptionName=" + "durableConsumerChangeSelectorTest2");
      durableCon = context2.createDurableConsumer(topic,
          "durableConsumerChangeSelectorTest2", "TEST = 'test'", false);

      TestUtil.logMsg(
          "Create/Send first message with string property \"TEST = 'test'\"");
      messageSent = context2.createTextMessage();
      messageSent.setStringProperty("TEST", "test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "durableConsumerChangeSelectorTest2");
      messageSent.setText("Message #1 with string property TEST='test'");
      producer2.send(destination, messageSent);

      TestUtil.logMsg(
          "Create/Send second message with string property \"TEST = 'test again'\"");
      messageSent2 = context2.createTextMessage();
      messageSent2.setStringProperty("TEST", "test again");
      messageSent2.setStringProperty("COM_SUN_JMS_TESTNAME",
          "durableConsumerChangeSelectorTest2");
      messageSent2.setText("Message #2 with string property TEST='test again'");
      producer2.send(destination, messageSent2);

      // Check and verify that first message is received
      TestUtil.logMsg("Try receiving first message (should get message)");
      messageReceived = (TextMessage) durableCon.receive(timeout);
      if (messageReceived == null) {
        TestUtil.logMsg("Did not receive any message (incorrect)");
        throw new Fault("didn't receive any message");
      } else if (messageReceived.getText().equals(messageSent.getText())) {
        TestUtil.logMsg("Message text: \"" + messageReceived.getText() + "\"");
        TestUtil.logMsg("Received correct first message");
      } else {
        TestUtil.logMsg("Message text: \"" + messageReceived.getText() + "\"");
        throw new Fault("didn't get the right message");
      }

      // Check and verify that seconde message is not received
      TestUtil.logMsg("Try receiving second message (should not get message)");
      messageReceived = (TextMessage) durableCon.receive(timeout);
      if (messageReceived == null) {
        TestUtil.logMsg("Did not receive second message (correct)");
      } else if (messageReceived.getText().equals(messageSent2.getText())) {
        TestUtil.logMsg("Message text: \"" + messageReceived.getText() + "\"");
        throw new Fault("received second message (unexpected)");
      } else {
        TestUtil.logMsg("Message text: \"" + messageReceived.getText() + "\"");
        throw new Fault("received unexpected message");
      }

      // need to inactivate topic consumer before switching new consumer
      TestUtil.logMsg("Close durable subscription");
      durableCon.close();

      // change selector
      TestUtil.logMsg(
          "Create new durable subscription with MessageSelector=\"TEST='test again'\",");
      TestUtil.logMsg("TopicName=" + topic.getTopicName()
          + " and SubscriptionName=" + "durableConsumerChangeSelectorTest2");
      durableCon = context2.createDurableConsumer(topic,
          "durableConsumerChangeSelectorTest2", "TEST = 'test again'", false);

      // receive message
      messageReceived = (TextMessage) durableCon.receive(timeout);
      if (messageReceived == null) {
        TestUtil.logMsg("Did not receive any messages (correct)");
      } else {
        TestUtil.logMsg("Message text: \"" + messageReceived.getText() + "\"");
        throw new Fault("received unexpected message");
      }
    } catch (Exception e) {
      TestUtil.logMsg("Caught unexpected exception: " + e);
      throw new Fault("durableConsumerChangeSelectorTest2");
    } finally {
      try {
        cleanupSubscription(durableCon, context2,
            "durableConsumerChangeSelectorTest2");
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: verifyClientIDOnAdminConfiguredIDTest
   *
   * @assertion_ids: JMS:JAVADOC:970; JMS:JAVADOC:1040; JMS:SPEC:264.5;
   * JMS:SPEC:173; JMS:SPEC:198; JMS:SPEC:91;
   *
   * @test_Strategy: Test the following APIs:
   *
   * JMSContext.getClientID()
   *
   * Check and verify the client id of an administratively configured client id.
   */
  public void verifyClientIDOnAdminConfiguredIDTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Get client id from an administratively configured client id");
      String cid = context2.getClientID();
      if (cid == null) {
        TestUtil.logErr("getClientID returned null (expected cts)");
        pass = false;
      } else if (!cid.equals("cts")) {
        TestUtil.logErr("getClientID() returned " + cid + ", expected cts");
        pass = false;
      } else {
        TestUtil.logMsg("getClientID returned cts (Correct)");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected: " + e);
      throw new Fault("verifyClientIDOnAdminConfiguredIDTest");
    }

    if (!pass) {
      throw new Fault("verifyClientIDOnAdminConfiguredIDTest failed");
    }
  }

  /*
   * @testName: invalidDestinationRuntimeExceptionTests
   *
   * @assertion_ids: JMS:JAVADOC:944; JMS:JAVADOC:947; JMS:JAVADOC:951;
   * JMS:JAVADOC:955; JMS:JAVADOC:958; JMS:JAVADOC:1086; JMS:JAVADOC:1153;
   * JMS:JAVADOC:1157; JMS:JAVADOC:1254; JMS:JAVADOC:1237; JMS:JAVADOC:1242;
   * JMS:JAVADOC:1246; JMS:JAVADOC:1250; JMS:JAVADOC:958; JMS:JAVADOC:1383;
   * JMS:JAVADOC:1161;
   *
   * @test_Strategy: Test InvalidDestinationRuntimeException conditions from
   * various API methods.
   *
   * JMSProducer.send(Destination, Message) JMSProducer.send(Destination,
   * String) JMSProducer.send(Destination, Serializable)
   * JMSProducer.send(Destination, byte[]) JMSProducer.send(Destination, Map)
   * JMSContext.createConsumer(Destination)
   * JMSContext.createConsumer(Destination, String)
   * JMSContext.createConsumer(Destination, String, boolean)
   * JMSContext.createDurableConsumer(Topic, String)
   * JMSContext.createDurableConsumer(Topic, String, String, boolean)
   * JMSContext.createSharedConsumer(Topic, String)
   * JMSContext.createSharedConsumer(Topic, String, String)
   * JMSContext.createSharedDurableConsumer(Topic, String)
   * JMSContext.createSharedDurableConsumer(Topic, String, String)
   * JMSContext.unsubscribe(String)
   */
  public void invalidDestinationRuntimeExceptionTests() throws Fault {
    boolean pass = true;
    Destination invalidDestination = null;
    Topic invalidTopic = null;
    String message = "Where are you!";
    byte[] bytesMsgSend = message.getBytes();
    Map<String, Object> mapMsgSend = new HashMap<String, Object>();
    mapMsgSend.put("StringValue", "sendAndRecvTest7");
    mapMsgSend.put("BooleanValue", true);
    mapMsgSend.put("IntValue", (int) 10);
    try {
      // send to an invalid topic
      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = context.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "invalidDestinationRuntimeExceptionTests");

      TestUtil.logMsg(
          "Testing JMSProducer.send(Destination, Message) for InvalidDestinationRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling send(Destination, Message) -> expect InvalidDestinationRuntimeException");
        producer.send(invalidDestination, expTextMessage);
      } catch (InvalidDestinationRuntimeException e) {
        TestUtil.logMsg("Got InvalidDestinationRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil.logErr(
            "Expected InvalidDestinationRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing JMSProducer.send(Destination, String) for InvalidDestinationRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling send(Destination, String) -> expect InvalidDestinationRuntimeException");
        producer.send(invalidDestination, message);
      } catch (InvalidDestinationRuntimeException e) {
        TestUtil.logMsg("Got InvalidDestinationRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil.logErr(
            "Expected InvalidDestinationRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing JMSProducer.send(Destination, Serializable) for InvalidDestinationRuntimeException");
      TestUtil.logMsg("Send ObjectMessage");
      TestUtil.logMsg("Set some values in ObjectMessage");
      ObjectMessage om = context.createObjectMessage();
      StringBuffer sb = new StringBuffer(message);
      om.setObject(sb);
      om.setStringProperty("COM_SUN_JMS_TESTNAME",
          "invalidDestinationRuntimeExceptionTests");
      try {
        TestUtil.logMsg(
            "Calling send(Destination, Serializable) -> expect InvalidDestinationRuntimeException");
        producer.send(invalidDestination, om);
      } catch (InvalidDestinationRuntimeException e) {
        TestUtil.logMsg("Got InvalidDestinationRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil.logErr(
            "Expected InvalidDestinationRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing JMSProducer.send(Destination, byte[]) for InvalidDestinationRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling send(Destination, byte[]) -> expect InvalidDestinationRuntimeException");
        producer.send(invalidDestination, bytesMsgSend);
      } catch (InvalidDestinationRuntimeException e) {
        TestUtil.logMsg("Got InvalidDestinationRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil.logErr(
            "Expected InvalidDestinationRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing JMSProducer.send(Destination, Map) for InvalidDestinationRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling send(Destination, Map) -> expect InvalidDestinationRuntimeException");
        producer.send(invalidDestination, mapMsgSend);
      } catch (InvalidDestinationRuntimeException e) {
        TestUtil.logMsg("Got InvalidDestinationRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil.logErr(
            "Expected InvalidDestinationRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing JMSContext.createConsumer(Destination) for InvalidDestinationRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling JMSContext.createConsumer(Destination) -> expect InvalidDestinationRuntimeException");
        context.createConsumer(invalidDestination);
      } catch (InvalidDestinationRuntimeException e) {
        TestUtil.logMsg("Got InvalidDestinationRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil.logErr(
            "Expected InvalidDestinationRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing JMSContext.createConsumer(Destination, String) for InvalidDestinationRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling JMSContext.createConsumer(Destination, String) -> expect InvalidDestinationRuntimeException");
        context.createConsumer(invalidDestination, "lastMessage = TRUE");
      } catch (InvalidDestinationRuntimeException e) {
        TestUtil.logMsg("Got InvalidDestinationRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil.logErr(
            "Expected InvalidDestinationRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing JMSContext.createConsumer(Destination, String, boolean) for InvalidDestinationRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling JMSContext.createConsumer(Destination, String, boolean) -> expect InvalidDestinationRuntimeException");
        context.createConsumer(invalidDestination, "lastMessage = TRUE", false);
      } catch (InvalidDestinationRuntimeException e) {
        TestUtil.logMsg("Got InvalidDestinationRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil.logErr(
            "Expected InvalidDestinationRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing JMSContext.createDurableConsumer(Topic, String) for InvalidDestinationRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling JMSContext.createDurableConsumer(Topic, String) -> expect InvalidDestinationRuntimeException");
        context.createDurableConsumer(invalidTopic,
            "InvalidDestinationRuntimeException");
      } catch (InvalidDestinationRuntimeException e) {
        TestUtil.logMsg("Got InvalidDestinationRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil.logErr(
            "Expected InvalidDestinationRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing JMSContext.createDurableConsumer(Topic, String, String, boolean) for InvalidDestinationRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling JMSContext.createDurableConsumer(Topic, String, String, boolean) -> expect InvalidDestinationRuntimeException");
        context.createDurableConsumer(invalidTopic,
            "InvalidDestinationRuntimeException", "lastMessage = TRUE", false);
      } catch (InvalidDestinationRuntimeException e) {
        TestUtil.logMsg("Got InvalidDestinationRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil.logErr(
            "Expected InvalidDestinationRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing JMSContext.createSharedDurableConsumer(Topic, String) for InvalidDestinationRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling JMSContext.createSharedDurableConsumer(Topic, String) -> expect InvalidDestinationRuntimeException");
        context.createSharedDurableConsumer(invalidTopic,
            "InvalidDestinationRuntimeException");
      } catch (InvalidDestinationRuntimeException e) {
        TestUtil.logMsg("Got InvalidDestinationRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil.logErr(
            "Expected InvalidDestinationRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing JMSContext.createSharedDurableConsumer(Topic, String, String) for InvalidDestinationRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling JMSContext.createSharedDurableConsumer(Topic, String, String) -> expect InvalidDestinationRuntimeException");
        context.createSharedDurableConsumer(invalidTopic,
            "InvalidDestinationRuntimeException", "lastMessage = TRUE");
      } catch (InvalidDestinationRuntimeException e) {
        TestUtil.logMsg("Got InvalidDestinationRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil.logErr(
            "Expected InvalidDestinationRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing JMSContext.unsubscribe(String) for InvalidDestinationRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling JMSContext.unsubscribe(String) -> expect InvalidDestinationRuntimeException");
        context.unsubscribe("InvalidSubscriptionName");
      } catch (InvalidDestinationRuntimeException e) {
        TestUtil.logMsg("Got InvalidDestinationRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil.logErr(
            "Expected InvalidDestinationRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing JMSContext.createSharedConsumer(Topic, String) for InvalidDestinationRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling JMSContext.createSharedConsumer(Topic, String) for InvalidDestinationRuntimeException");
        context.createSharedConsumer(invalidTopic,
            "InvalidDestinationRuntimeException");
      } catch (InvalidDestinationRuntimeException e) {
        TestUtil.logMsg("Got InvalidDestinationRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil.logErr(
            "Expected InvalidDestinationRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing JMSContext.createSharedConsumer(Topic, String, String) for InvalidDestinationRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling JMSContext.createSharedConsumer(Topic, String, String) for InvalidDestinationRuntimeException");
        context.createSharedConsumer(invalidTopic,
            "InvalidDestinationRuntimeException", "lastMessage = TRUE");
      } catch (InvalidDestinationRuntimeException e) {
        TestUtil.logMsg("Got InvalidDestinationRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil.logErr(
            "Expected InvalidDestinationRuntimeException, received " + e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("invalidDestinationRuntimeExceptionTests", e);
    }

    if (!pass) {
      throw new Fault("invalidDestinationRuntimeExceptionTests failed");
    }
  }

  /*
   * @testName: invalidSelectorRuntimeExceptionTests
   *
   * @assertion_ids: JMS:JAVADOC:948; JMS:JAVADOC:952; JMS:JAVADOC:959;
   * JMS:JAVADOC:1154; JMS:JAVADOC:1158; JMS:JAVADOC:959; JMS:JAVADOC:1162;
   *
   * @test_Strategy: Test InvalidSelectorRuntimeException conditions from
   * various API methods.
   *
   * JMSContext.createConsumer(Destination, String)
   * JMSContext.createConsumer(Destination, String, boolean)
   * JMSContext.createDurableConsumer(Topic, String, String, boolean)
   * JMSContext.createSharedConsumer(Topic, String, String)
   * JMSContext.createSharedDurableConsumer(Topic, String, String)
   *
   */
  public void invalidSelectorRuntimeExceptionTests() throws Fault {
    boolean pass = true;
    String invalidMessageSelector = "=TEST 'test'";
    try {

      TestUtil.logMsg(
          "Testing JMSContext.createConsumer(Destination, String) for InvalidSelectorRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling JMSContext.createConsumer(Destination, String) -> expect InvalidSelectorRuntimeException");
        context.createConsumer(topic, invalidMessageSelector);
      } catch (InvalidSelectorRuntimeException e) {
        TestUtil.logMsg("Got InvalidSelectorRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil
            .logErr("Expected InvalidSelectorRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing JMSContext.createConsumer(Destination, String, boolean) for InvalidSelectorRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling JMSContext.createConsumer(Destination, String, boolean) -> expect InvalidSelectorRuntimeException");
        context.createConsumer(topic, invalidMessageSelector, false);
      } catch (InvalidSelectorRuntimeException e) {
        TestUtil.logMsg("Got InvalidSelectorRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil
            .logErr("Expected InvalidSelectorRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing JMSContext.createDurableConsumer(Topic, String, String, boolean) for InvalidSelectorRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling JMSContext.createDurableConsumer(Topic, String, String, boolean) -> expect InvalidSelectorRuntimeException");
        context2.createDurableConsumer(topic, "InvalidSelectorRuntimeException",
            invalidMessageSelector, false);
      } catch (InvalidSelectorRuntimeException e) {
        TestUtil.logMsg("Got InvalidSelectorRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil
            .logErr("Expected InvalidSelectorRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing JMSContext.createSharedDurableConsumer(Topic, String, String) for InvalidSelectorRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling JMSContext.createSharedDurableConsumer(Topic, String, String) -> expect InvalidSelectorRuntimeException");
        context.createSharedDurableConsumer(topic,
            "InvalidSelectorRuntimeException", invalidMessageSelector);
      } catch (InvalidSelectorRuntimeException e) {
        TestUtil.logMsg("Got InvalidSelectorRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil
            .logErr("Expected InvalidSelectorRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing JMSContext.createSharedConsumer(Topic, String, String) for InvalidSelectorRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling JMSContext.createSharedConsumer(Topic, String, String) for InvalidSelectorRuntimeException");
        context.createSharedConsumer(topic, "InvalidSelectorRuntimeException",
            invalidMessageSelector);
      } catch (InvalidSelectorRuntimeException e) {
        TestUtil.logMsg("Got InvalidSelectorRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil
            .logErr("Expected InvalidSelectorRuntimeException, received " + e);
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("invalidSelectorRuntimeExceptionTests", e);
    }

    if (!pass) {
      throw new Fault("invalidSelectorRuntimeExceptionTests failed");
    }
  }

  /*
   * @testName: jMSRuntimeExceptionTests
   *
   * @assertion_ids: JMS:JAVADOC:932; JMS:SPEC:264.2; JMS:JAVADOC:1274;
   * JMS:JAVADOC:1260; JMS:JAVADOC:1384; JMS:JAVADOC:954; JMS:JAVADOC:1160;
   * JMS:JAVADOC:957; JMS:JAVADOC:1152; JMS:JAVADOC:1156;
   *
   * @test_Strategy: Test JMSRuntimeException conditions from various API
   * methods.
   *
   * JMSContext.createContext(int); JMSProducer.setPriority(int);
   * JMSProducer.setDeliveryMode(long); JMSContext.createDurableConsumer(Topic,
   * String) JMSContext.createDurableConsumer(Topic, String. String, boolean)
   * JMSContext.createSharedConsumer(Topic, String)
   * JMSContext.createSharedConsumer(Topic, String, String)
   * JMSContext.createSharedDurableConsumer(Topic, String)
   * JMSContext.createSharedDurableConsumer(Topic, String. String)
   */
  public void jMSRuntimeExceptionTests() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    if ((vehicle.equals("ejb") || vehicle.equals("jsp")
        || vehicle.equals("servlet"))) {
      try {
        TestUtil.logMsg(
            "Calling createContext must throw JMSRuntimeException for EJB/WEB container");
        context.createContext(JMSContext.AUTO_ACKNOWLEDGE);
        TestUtil.logErr("Didn't throw JMSRuntimeException");
        pass = false;
      } catch (JMSRuntimeException e) {
        TestUtil.logMsg("Caught expected JMSRuntimeException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSRuntimeException, received " + e);
        pass = false;
      }
    }

    try {
      if (consumer != null)
        consumer.close();
    } catch (Exception e) {
    }

    try {
      TestUtil.logMsg("Try and set an invalid priority of -1");
      producer.setPriority(-1);
      TestUtil.logErr("Didn't throw JMSRuntimeException");
      pass = false;
    } catch (JMSRuntimeException e) {
      TestUtil.logMsg("Caught expected JMSRuntimeException");
    } catch (Exception e) {
      TestUtil.logErr("Expected JMSRuntimeException, received " + e);
      pass = false;
    }
    try {
      TestUtil.logMsg("Try and set an delivery mode to live of -1");
      producer.setDeliveryMode(-1);
      TestUtil.logErr("Didn't throw JMSRuntimeException");
      pass = false;
    } catch (JMSRuntimeException e) {
      TestUtil.logMsg("Caught expected JMSRuntimeException");
    } catch (Exception e) {
      TestUtil.logErr("Expected JMSRuntimeException, received " + e);
      pass = false;
    }

    // Create shared consumer specifying topic and name
    // Create second shared consumer with same name but specifying different
    // topic,
    // Verify JMSRuntimeException is thrown.
    try {
      TestUtil.logMsg("Create shared consumer");
      TestUtil.logMsg("Calling JMSContext.createSharedConsumer(Topic, String)");
      consumer = context2.createSharedConsumer(topic, "dummySubSCJMSRuntime");
      TestUtil.logMsg(
          "Create second shared consumer with same name but different topic");
      TestUtil.logMsg("Calling JMSContext.createSharedConsumer(Topic, String)");
      Topic mytopic = (Topic) tool.createNewTopic("MY_TOPIC2");
      consumer2 = context2.createSharedConsumer(mytopic,
          "dummySubSCJMSRuntime");
      TestUtil.logMsg("Verify that JMSRuntimeException is thrown");
      pass = false;
      TestUtil.logErr("Didn't throw expected JMSRuntimeException");
    } catch (JMSRuntimeException ex) {
      TestUtil.logMsg("Got expected JMSRuntimeException from "
          + "JMSContext.createSharedConsumer(Topic, String)");
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
      }
    }

    // Create shared consumer specifying topic and name and message selector
    // Create second shared consumer with same name but specifying different
    // topic and message selector
    // Verify JMSRuntimeException is thrown.
    try {
      TestUtil.logMsg("Create shared consumer");
      TestUtil.logMsg(
          "Calling JMSContext.createSharedConsumer(Topic, String, String)");
      consumer = context2.createSharedConsumer(topic, "dummySubSCJMSRuntime",
          "TEST = 'test'");
      TestUtil.logMsg(
          "Create second shared consumer with same name but different topic");
      TestUtil.logMsg(
          "Calling JMSContext.createSharedConsumer(Topic, String, String)");
      Topic mytopic = (Topic) tool.createNewTopic("MY_TOPIC2");
      consumer2 = context2.createSharedConsumer(mytopic, "dummySubSCJMSRuntime",
          "TEST = 'test'");
      TestUtil.logMsg("Verify that JMSRuntimeException is thrown");
      pass = false;
      TestUtil.logErr("Didn't throw expected JMSRuntimeException");
    } catch (JMSRuntimeException ex) {
      TestUtil.logMsg("Got expected JMSRuntimeException from "
          + "JMSContext.createSharedConsumer(Topic, String, String)");
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
      }
    }

    // Create shared durable subscription specifying topic, name. selector.
    // Create second
    // shared durable subscription with same name but specifying different
    // topic, selector.
    // Verify JMSRuntimeException is thrown.
    try {
      TestUtil.logMsg("Create shared durable subscription");
      TestUtil.logMsg(
          "Calling JMSContext.createSharedDurableConsumer(Topic, String, String)");
      consumer = context2.createSharedDurableConsumer(topic,
          "dummySubSJMSRuntime", "TEST = 'test'");
      TestUtil.logMsg(
          "Create second shared durable subscription with same name but different other args");
      TestUtil.logMsg(
          "Calling JMSContext.createSharedDurableConsumer(Topic, String, String)");
      consumer2 = context2.createSharedDurableConsumer(topic,
          "dummySubSJMSRuntime", "TEST = 'test2'");
      TestUtil.logMsg("Verify that JMSRuntimeException is thrown");
      pass = false;
      TestUtil.logErr("Didn't throw expected JMSRuntimeException");
    } catch (JMSRuntimeException ex) {
      TestUtil.logMsg("Got expected JMSRuntimeException from "
          + "JMSContext.createSharedDurableConsumer(Topic, String, String)");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      pass = false;
    } finally {
      try {
        if (consumer2 != null)
          consumer2.close();
        cleanupSubscription(consumer, context2, "dummySubSJMSRuntime");
      } catch (Exception e) {
      }
    }

    // Create durable subscription specifying topic and name
    // Create second durable subscription with same name but specifying
    // different topic,
    // Verify JMSRuntimeException is thrown.
    try {
      TestUtil.logMsg("Create durable subscription");
      TestUtil
          .logMsg("Calling JMSContext.createDurableConsumer(Topic, String)");
      consumer = context2.createDurableConsumer(topic, "dummySubDJMSRuntime");
      TestUtil.logMsg(
          "Create second durable subscription with same name but different topic");
      TestUtil
          .logMsg("Calling JMSContext.createDurableConsumer(Topic, String)");
      Topic mytopic = (Topic) tool.createNewTopic("MY_TOPIC2");
      consumer2 = context2.createDurableConsumer(mytopic,
          "dummySubDJMSRuntime");
      TestUtil.logMsg("Verify that JMSRuntimeException is thrown");
      pass = false;
      TestUtil.logErr("Didn't throw expected JMSRuntimeException");
    } catch (JMSRuntimeException ex) {
      TestUtil.logMsg("Got expected JMSRuntimeException from "
          + "JMSContext.createDurableConsumer(Topic, String)");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      pass = false;
    } finally {
      try {
        if (consumer2 != null)
          consumer2.close();
        cleanupSubscription(consumer, context2, "dummySubDJMSRuntime");
      } catch (Exception e) {
      }
    }

    // Create shared durable subscription specifying topic and name
    // Create second shared durable subscription with same name but specifying
    // different topic,
    // Verify JMSRuntimeException is thrown.
    try {
      TestUtil.logMsg("Create shared durable subscription");
      TestUtil.logMsg(
          "Calling JMSContext.createSharedDurableConsumer(Topic, String)");
      consumer = context2.createSharedDurableConsumer(topic,
          "dummySubSDJMSRuntime");
      TestUtil.logMsg(
          "Create second shared durable subscription with same name but different topic");
      TestUtil.logMsg(
          "Calling JMSContext.createSharedDurableConsumer(Topic, String)");
      Topic mytopic = (Topic) tool.createNewTopic("MY_TOPIC2");
      consumer2 = context2.createSharedDurableConsumer(mytopic,
          "dummySubSDJMSRuntime");
      TestUtil.logMsg("Verify that JMSRuntimeException is thrown");
      pass = false;
      TestUtil.logErr("Didn't throw expected JMSRuntimeException");
    } catch (JMSRuntimeException ex) {
      TestUtil.logMsg("Got expected JMSRuntimeException from "
          + "JMSContext.createSharedDurableConsumer(Topic, String)");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      pass = false;
    } finally {
      try {
        if (consumer2 != null)
          consumer2.close();
        cleanupSubscription(consumer, context2, "dummySubSDJMSRuntime");
      } catch (Exception e) {
      }
    }

    // Create durable subscription specifying topic, name. selector, and nolocal
    // value.
    // Create second durable subscription with same name but specifying
    // different topic,
    // selector, or nolocal value. Verify JMSRuntimeException is thrown.
    try {
      TestUtil.logMsg("Create durable subscription");
      TestUtil.logMsg(
          "Calling JMSContext.createDurableConsumer(Topic, String, String, boolean)");
      consumer = context2.createDurableConsumer(topic, "dummySubDJMSRuntime",
          "TEST = 'test'", true);
      TestUtil.logMsg(
          "Create second durable subscription with same name but different other args");
      TestUtil.logMsg(
          "Calling JMSContext.createDurableConsumer(Topic, String, String, boolean)");
      consumer2 = context2.createDurableConsumer(topic, "dummySubDJMSRuntime",
          "TEST = 'test2'", false);
      TestUtil.logMsg("Verify that JMSRuntimeException is thrown");
      pass = false;
      TestUtil.logErr("Didn't throw expected JMSRuntimeException");
    } catch (JMSRuntimeException ex) {
      TestUtil.logMsg("Got expected JMSRuntimeException from "
          + "JMSContext.createDurableConsumer(Topic, String, String, boolean)");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      pass = false;
    } finally {
      try {
        if (consumer2 != null)
          consumer2.close();
        cleanupSubscription(consumer, context2, "dummySubDJMSRuntime");
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("jMSRuntimeExceptionTests failed");
    }
  }

  /*
   * @testName: illegalStateRuntimeExceptionTest
   *
   * @assertion_ids: JMS:JAVADOC:917; JMS:JAVADOC:994; JMS:JAVADOC:997;
   * JMS:JAVADOC:1340; JMS:JAVADOC:1341; JMS:JAVADOC:1378;
   *
   * @test_Strategy: 1. Create a TextMessages and send to Topic 2. Then invoke
   * JMSContext.commit() on a non-transacted session Verify that
   * IllegalStateRuntimeException is thrown 3. Then test invoke
   * JMSContext.rollback() on a non-transacted session Verify that
   * IllegalStateRuntimeException is thrown 3. Then test invoke
   * JMSContext.recover() on a transacted session Verify that
   * IllegalStateRuntimeException is thrown 4. Create JMSContext with
   * CLIENT_ACKNOWLEDGE then close JMSContext. Then test invoke
   * JMSContext.acknowledge() on the JMSContext. Verify that
   * IllegalStateRuntimeException is thrown 5. Verify that
   * IllegalStateRuntimeException is thrown if nolocal=true and client id is
   * unset for JMSContext.createSharedConsumer(...) and
   * JMSContext.createSharedDurableConsumer(...) methods. 6. Verify that
   * IllegalStateRuntimeException is thrown if client id is unset for
   * JMSContext.createDurableConsumer(...) methods.
   *
   * JMSContext.commit(); JMSContext.rollback(); JMSContext.recover();
   * JMSContext.acknowledge(); JMSContext.createDurableConsumer(Topic, String)
   * JMSContext.createDurableConsumer(Topic, String. String, boolean)
   */
  public void illegalStateRuntimeExceptionTest() throws Fault {
    boolean pass = true;
    String message = "Where are you!";

    try {
      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = context.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "illegalStateRuntimeExceptionTest");
      // send the message to the Topic
      TestUtil.logTrace("Sending message to the Topic");
      TestUtil.logMsg(
          "Sending TextMessage via JMSProducer.send(Destination, Message)");
      producer.send(destination, expTextMessage);

      try {
        TestUtil.logTrace(
            "JMSContext.commit() on non-transacted session must throw IllegalStateRuntimeException");
        context.commit();
        pass = false;
        TestUtil.logErr(
            "Error: JMSContext.commit() didn't throw expected IllegalStateRuntimeException");
      } catch (javax.jms.IllegalStateRuntimeException e) {
        logMsg(
            "Got expected IllegalStateRuntimeException from JMSContext.commit()");
      } catch (Exception e) {
        logErr("Caught unexpected exception: " + e);
        pass = false;
      }

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      expTextMessage = context.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "illegalStateRuntimeExceptionTest");
      // send the message to the Topic
      TestUtil.logTrace("Sending message to the Topic");
      TestUtil.logMsg(
          "Sending TextMessage via JMSProducer.send(Destination, Message)");
      producer.send(destination, expTextMessage);

      try {
        TestUtil.logTrace(
            "JMSContext.rollback() on non-transacted session must throw IllegalStateRuntimeException");
        context.rollback();
        pass = false;
        TestUtil.logErr(
            "Error: JMSContext.rollback() didn't throw expected IllegalStateRuntimeException");
      } catch (javax.jms.IllegalStateRuntimeException e) {
        logMsg(
            "Got expected IllegalStateRuntimeException from JMSContext.rollback()");
      } catch (Exception e) {
        logErr("Caught unexpected exception: " + e);
        pass = false;
      }

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      expTextMessage = context.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "illegalStateRuntimeExceptionTest");
      // send the message to the Topic
      TestUtil.logTrace("Sending message to the Topic");
      TestUtil.logMsg(
          "Sending TextMessage via JMSProducer.send(Destination, Message)");
      producer.send(destination, expTextMessage);

      if ((vehicle.equals("appclient") || vehicle.equals("standalone"))) {
        // create JMSContext with SESSION_TRANSACTED then create producer
        JMSContext contextTX = cf.createContext(user, password,
            JMSContext.SESSION_TRANSACTED);
        JMSProducer producerTX = contextTX.createProducer();

        // send and receive TextMessage
        TestUtil.logMsg("Creating TextMessage");
        expTextMessage = contextTX.createTextMessage(message);
        TestUtil.logMsg("Set some values in TextMessage");
        expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
            "illegalStateRuntimeExceptionTest");
        // send the message to the Queue
        TestUtil.logMsg("Sending message to the Queue");
        TestUtil.logMsg(
            "Sending TextMessage via JMSProducer.send(Destination, Message)");
        producerTX.send(destination, expTextMessage);

        try {
          TestUtil.logMsg(
              "JMSContext.recover() on a transacted session must throw IllegalStateRuntimeException");
          contextTX.recover();
          pass = false;
          TestUtil.logErr(
              "Error: JMSContext.recover() didn't throw expected IllegalStateRuntimeException");
        } catch (javax.jms.IllegalStateRuntimeException e) {
          logMsg(
              "Got expected IllegalStateRuntimeException from JMSContext.recover()");
        } catch (Exception e) {
          TestUtil.logErr("Caught unexpected exception: " + e);
          pass = false;
        }
        contextTX.close();

        try {
          TestUtil.logMsg("Create JMSContext with CLIENT_ACKNOWLEDGE");
          JMSContext msgcontext = cf.createContext(user, password,
              JMSContext.CLIENT_ACKNOWLEDGE);
          TestUtil.logMsg("Close JMSContext");
          msgcontext.close();
          TestUtil.logMsg(
              "Call JMSContext.acknowledge() on a closed session which is illegal");
          msgcontext.acknowledge();
          TestUtil.logErr("Didn't throw IllegalStateRuntimeException");
          pass = false;
        } catch (javax.jms.IllegalStateRuntimeException e) {
          logMsg(
              "Got expected IllegalStateRuntimeException from JMSContext.acknowledge()");
        } catch (Exception e) {
          TestUtil.logErr("Caught expected exception" + e);
          pass = false;
        }
      }

      try {
        if (consumer != null)
          consumer.close();
      } catch (Exception e) {
      }

      // Create durable consumer with client identifier unset
      // Must throw IllegalStateRuntimeException
      try {
        consumer = null;
        TestUtil
            .logMsg("Calling JMSContext.createDurableConsumer(Topic, String)");
        TestUtil.logMsg(
            "Create Durable Consumer with client id unset (expect IllegalStateRuntimeException)");
        consumer = context.createDurableConsumer(topic,
            "dummySubDIllegalState1");
        TestUtil.logErr(
            "No exception occurred - expected IllegalStateRuntimeException");
        pass = false;
      } catch (javax.jms.IllegalStateRuntimeException e) {
        TestUtil.logMsg("Got expected IllegalStateRuntimeException");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        pass = false;
      } finally {
        try {
          if (consumer != null)
            cleanupSubscription(consumer, context, "dummySubDIllegalState1");
        } catch (Exception e) {
        }
      }

      // Create durable consumer with client identifier unset (nolocal=false)
      // Must throw IllegalStateRuntimeException
      try {
        consumer = null;
        TestUtil.logMsg(
            "Calling JMSContext.createDurableConsumer(Topic, String, String, boolean)");
        TestUtil.logMsg(
            "Create Durable Consumer with client id unset, nolocal=false (expect IllegalStateRuntimeException)");
        consumer = context.createDurableConsumer(topic,
            "dummySubDIllegalState2", "lastMessage = TRUE", false);
        TestUtil.logErr(
            "No exception occurred - expected IllegalStateRuntimeException");
        pass = false;
      } catch (javax.jms.IllegalStateRuntimeException e) {
        TestUtil.logMsg("Got expected IllegalStateRuntimeException");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception" + e);
        pass = false;
      } finally {
        try {
          if (consumer != null)
            cleanupSubscription(consumer, context, "dummySubDIllegalState2");
        } catch (Exception e) {
        }
      }

      // Create durable consumer with client identifier unset (nolocal=true)
      // Must throw IllegalStateRuntimeException
      try {
        consumer = null;
        TestUtil.logMsg(
            "Calling JMSContext.createDurableConsumer(Topic, String, String, boolean)");
        TestUtil.logMsg(
            "Create Durable Consumer with client id unset, nolocal=true (expect IllegalStateRuntimeException)");
        consumer = context.createDurableConsumer(topic,
            "dummySubDIllegalState2", "lastMessage = TRUE", true);
        TestUtil.logErr(
            "No exception occurred - expected IllegalStateRuntimeException");
        pass = false;
      } catch (javax.jms.IllegalStateRuntimeException e) {
        TestUtil.logMsg("Got expected IllegalStateRuntimeException");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception" + e);
        pass = false;
      } finally {
        try {
          if (consumer != null)
            cleanupSubscription(consumer, context, "dummySubDIllegalState2");
        } catch (Exception e) {
        }
      }

    } catch (Exception e) {
      TestUtil.logMsg("Caught unexpected exception: " + e);
      throw new Fault("illegalStateRuntimeExceptionTest");
    }

    if (!pass) {
      throw new Fault("illegalStateRuntimeExceptionTest");
    }
  }
}
