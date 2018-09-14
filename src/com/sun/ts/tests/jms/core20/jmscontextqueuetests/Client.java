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
package com.sun.ts.tests.jms.core20.jmscontextqueuetests;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import com.sun.javatest.Status;

public class Client extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core20.jmscontextqueuetests.Client";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // JMS tool which creates and/or looks up the JMS administered objects
  private transient JmsTool tool = null;

  // JMS objects
  private transient ConnectionFactory cf = null;

  private transient Queue queue = null;

  private transient Destination destination = null;

  private transient JMSContext context = null;

  private transient JMSContext contextToSendMsg = null;

  private transient JMSProducer producer = null;

  private transient JMSConsumer consumer = null;

  // Harness req's
  private Properties props = null;

  // properties read from ts.jte file
  long timeout;

  String user;

  String password;

  String mode;

  String vehicle = null;

  // used for tests
  private static final int numMessages = 3;

  private static final int iterations = 5;

  ArrayList queues = null;

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
      TestUtil.logMsg("JMSVersion=" + tmp);

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
      TestUtil.logMsg("JMSMajorVersion=" + tmp);

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
      TestUtil.logMsg("JMSMinorVersion=" + tmp);

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
      TestUtil.logMsg("JMSProviderName=" + tmp);
    } catch (Exception e) {
      TestUtil.logErr("Error: incorrect type returned for JMSProviderName: ",
          e);
      pass = false;
    }

    try {
      String tmp = data.getProviderVersion();
      TestUtil.logMsg("JMSProviderVersion=" + tmp);
    } catch (Exception e) {
      TestUtil.logErr("Error: incorrect type returned for ProviderVersion: ",
          e);
      pass = false;
    }

    try {
      int tmp = data.getProviderMajorVersion();
      TestUtil.logMsg("ProviderMajorVersion=" + tmp);
    } catch (Exception e) {
      TestUtil.logErr(
          "Error: incorrect type returned for ProviderMajorVersion: ", e);
      pass = false;
    }

    try {
      int tmp = data.getProviderMinorVersion();
      TestUtil.logMsg("ProviderMinorVersion=" + tmp);
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
    logMsg("Results: " + status[index]);
    return retcode;
  }

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
      queues = new ArrayList(3);
      connections = new ArrayList(5);

      // set up JmsTool for COMMON_Q setup
      TestUtil.logMsg("Setup JmsTool for COMMON_Q setup");
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      cf = tool.getConnectionFactory();
      destination = tool.getDefaultDestination();
      queue = (Queue) destination;
      tool.getDefaultConnection().close();
      TestUtil.logMsg("Create JMSContext with AUTO_ACKNOWLEDGE");
      context = cf.createContext(user, password, JMSContext.AUTO_ACKNOWLEDGE);
      contextToSendMsg = cf.createContext(user, password,
          JMSContext.AUTO_ACKNOWLEDGE);
      producer = contextToSendMsg.createProducer();
      consumer = context.createConsumer(destination);
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
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
    boolean pass = true;
    try {
      TestUtil.logMsg("Close JMSContext Objects");
      if (context != null)
        context.close();
      context = null;
      if (contextToSendMsg != null)
        contextToSendMsg.close();
      contextToSendMsg = null;
      producer = null;
      TestUtil.logMsg("Flush any messages left on Queue");
      tool.flushDestination();
      TestUtil.logMsg("Close JMSConsumer objects");
      if (consumer != null)
        consumer.close();
      consumer = null;
      tool.closeAllResources();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("cleanup failed!", e);
    }

    if (!pass)
      throw new Fault("cleanup failed!");
  }

  /*
   * @testName: createTemporayQueueTest
   *
   * @assertion_ids: JMS:JAVADOC:960;
   *
   * @test_Strategy: Test the following APIs:
   *
   * JMSContext.createTemporaryQueue()
   *
   * Send and receive a message to temporary queue. Compare send and recv
   * message for equality.
   */
  public void createTemporayQueueTest() throws Fault {
    boolean pass = true;
    try {

      String sendMessage = "a text message";

      // create a TemporaryQueue
      TestUtil.logMsg("Creating TemporaryQueue");
      TemporaryQueue tempQueue = context.createTemporaryQueue();

      // Create a JMSConsumer for this Temporary Queue
      TestUtil.logMsg("Create JMSConsumer");
      JMSConsumer smc = context.createConsumer(tempQueue);

      // Send message to temporary queue
      TestUtil.logMsg("Send message to temporaty queue using JMSProducer");
      producer.send(tempQueue, sendMessage);

      // Receive message from temporary queue
      TestUtil.logMsg("Receive message from temporaty queue");
      String recvMessage = smc.receiveBody(String.class, timeout);

      TestUtil.logMsg("Checking received message");
      if (recvMessage == null) {
        throw new Fault("Did not receive Message");
      }

      if (!recvMessage.equals(sendMessage)) {
        TestUtil.logErr("Unexpected message: received " + recvMessage
            + " , expected " + sendMessage);
        pass = false;
      } else {
        TestUtil.logMsg(
            "received correct message: " + recvMessage + " as expected");
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
        TestUtil.logErr("Caught unexpected exception: " + e);
        pass = false;
      }

      TestUtil.logMsg("Close JMSConsumer");
      smc.close();

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
      throw new Fault("createTemporayQueueTest");
    }

    if (!pass) {
      throw new Fault("createTemporayQueueTest failed");
    }
  }

  /*
   * @testName: createQueueBrowserTest
   *
   * @assertion_ids: JMS:JAVADOC:918; JMS:JAVADOC:921;
   *
   * @test_Strategy: Test the following APIs:
   *
   * JMSContext.createBrowser(Queue) JMSContext.createBrowser(Queue, String)
   * 
   * 1. Send x text messages to a Queue. 2. Create a QueueBrowser with selector
   * to browse just the last message in the Queue. 3. Create a QueueBrowser
   * again to browse all the messages in the queue.
   */
  public void createQueueBrowserTest() throws Fault {
    boolean pass = true;
    try {
      TextMessage tempMsg = null;
      QueueBrowser qBrowser = null;
      Enumeration msgs = null;

      // Close consumer created in setup() method
      consumer.close();
      consumer = null;

      // send "numMessages" messages to Queue plus end of stream message
      TestUtil.logMsg("Send " + numMessages + " messages to Queue");
      for (int i = 1; i <= numMessages; i++) {
        tempMsg = context.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "createQueueBrowserTest" + i);
        String tmp = null;
        if (i == numMessages) {
          tempMsg.setBooleanProperty("lastMessage", true);
          tmp = "with boolean property lastMessage=true";
        } else {
          tempMsg.setBooleanProperty("lastMessage", false);
          tmp = "with boolean property lastMessage=false";
        }
        producer.send(destination, tempMsg);
        TestUtil.logMsg("Message " + i + " sent " + tmp);
      }

      // create QueueBrowser to peek at last message in Queue using message
      // selector
      TestUtil.logMsg(
          "Create QueueBrowser to peek at last message in Queue using message selector (lastMessage = TRUE)");
      qBrowser = context.createBrowser(queue, "lastMessage = TRUE");

      // check that browser just has the last message in the Queue
      TestUtil.logMsg("Check that browser has just the last message");
      int msgCount = 0;
      msgs = qBrowser.getEnumeration();
      while (msgs.hasMoreElements()) {
        tempMsg = (TextMessage) msgs.nextElement();
        TestUtil.logMsg("Text=" + tempMsg.getText() + ", lastMessage="
            + tempMsg.getBooleanProperty("lastMessage"));
        if (!tempMsg.getText().equals("Message " + numMessages)) {
          TestUtil.logErr("Found [" + tempMsg.getText()
              + "] in browser expected only [Message 3]");
          pass = false;
        } else {
          TestUtil.logMsg("Found [Message " + numMessages + "] in browser");
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
      qBrowser = context.createBrowser(queue);

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
        } else {
          TestUtil
              .logMsg("Found [Message " + msgIndex + "] in browser (correct)");
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
      qBrowser.close();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("createQueueBrowserTest");
    }

    if (!pass) {
      throw new Fault("createQueueBrowserTest failed");
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
   * 1. Send x text messages to a Queue. 2. Create a JMSConsumer with selector
   * to consume just the last message in the Queue. 3. Create a JMSConsumer
   * again to consume the rest of the messages in the Queue.
   */
  public void createConsumerTest() throws Fault {
    boolean pass = true;
    JMSConsumer consumerSelect = null;
    try {
      TextMessage tempMsg = null;
      Enumeration msgs = null;

      // Close normal JMSConsumer created in setup() method
      consumer.close();
      consumer = null;

      // Create selective JMSConsumer with message selector (lastMessage=TRUE)
      TestUtil.logMsg(
          "Create selective JMSConsumer with selector [\"lastMessage=TRUE\"]");
      consumerSelect = context.createConsumer(destination, "lastMessage=TRUE");

      // send "numMessages" messages to Queue plus end of stream message
      TestUtil.logMsg("Send " + numMessages + " messages to Queue");
      for (int i = 1; i <= numMessages; i++) {
        tempMsg = context.createTextMessage("Message " + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "createConsumerTest" + i);
        String tmp = null;
        if (i == numMessages) {
          tempMsg.setBooleanProperty("lastMessage", true);
          tmp = "with boolean property lastMessage=true";
        } else {
          tempMsg.setBooleanProperty("lastMessage", false);
          tmp = "with boolean property lastMessage=false";
        }
        producer.send(destination, tempMsg);
        TestUtil.logMsg("Message " + i + " sent " + tmp);
      }

      // Receive last message with selective consumer
      TestUtil.logMsg(
          "Receive last message with selective JMSConsumer and boolean property (lastMessage=TRUE)");
      tempMsg = (TextMessage) consumerSelect.receive(timeout);
      if (tempMsg == null) {
        TestUtil.logErr("JMSConsumer.receive() returned NULL");
        TestUtil.logErr("Message " + numMessages + " missing from Queue");
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
        TestUtil.logErr("JMSConsumer.receive() returned a message ["
            + tempMsg.getText() + "]");
        TestUtil.logErr(
            "JMSConsumer with selector should have returned just 1 message");
        pass = false;
      } else {
        TestUtil.logMsg("Received no message (CORRECT)");
      }

      // Close selective JMSConsumer
      TestUtil.logMsg("Close selective JMSConsumer");
      consumerSelect.close();
      consumerSelect = null;

      // Create normal JMSConsumer
      TestUtil.logMsg("Create normal JMSConsumer");
      consumer = context.createConsumer(destination);

      // Receive rest of messages with normal JMSConsumer
      TestUtil.logMsg("Receive rest of messages with normal JMSConsumer");
      for (int msgCount = 1; msgCount < numMessages; msgCount++) {
        tempMsg = (TextMessage) consumer.receive(timeout);
        if (tempMsg == null) {
          TestUtil.logErr("JMSConsumer.receive() returned NULL");
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
      TestUtil.logMsg("Try receiving one more message (should get none)");
      tempMsg = (TextMessage) consumer.receive(timeout);
      if (tempMsg != null) {
        TestUtil.logErr("JMSConsumer returned message " + tempMsg.getText()
            + " (expected None)");
        pass = false;
      } else {
        TestUtil.logMsg("Received no message (CORRECT)");
      }

      // Close normal consumer
      TestUtil.logMsg("Close normal JMSConsumer");
      consumer.close();
      consumer = null;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("createConsumerTest");
    } finally {
      try {
        if (consumerSelect != null)
          consumerSelect.close();
        if (consumer != null)
          consumer.close();
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
      }
    }

    if (!pass) {
      throw new Fault("createConsumerTest failed");
    }
  }

  /*
   * @testName: getMetaDataTest
   *
   * @assertion_ids: JMS:JAVADOC:982;
   *
   * @test_Strategy: Call JMSContext.getMetaData() to retrieve the
   * ConnectionMetaData and then verify the ConnectionMetaData for correctness.
   *
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
        TestUtil.logErr("Caught unexpected exception: " + e);
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
          TestUtil.logErr("Caught unexpected exception: " + e);
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
      TestUtil.logMsg("Calling getTransacted and expect false to be returned");
      boolean transacted = context.getTransacted();
      if (transacted) {
        TestUtil.logErr("getTransacted() returned true expected false");
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
        TestUtil.logErr("Caught unexpected exception: " + e);
      }
    }

    // Test for transacted mode true
    if ((vehicle.equals("appclient") || vehicle.equals("standalone"))) {
      try {
        context = cf.createContext(user, password,
            JMSContext.SESSION_TRANSACTED);
        TestUtil.logMsg("Calling getTransacted and expect true to be returned");
        boolean transacted = context.getTransacted();
        if (!transacted) {
          TestUtil.logErr("getTransacted() returned false expected true");
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
          TestUtil.logErr("Caught unexpected exception: " + e);
        }
      }
    }

    if (!pass) {
      throw new Fault("getTransactedTest failed");
    }
  }

  /*
   * @testName: getClientIDTest
   *
   * @assertion_ids: JMS:JAVADOC:970;
   *
   * @test_Strategy: Test the following APIs:
   *
   * JMSContext.getClientID()
   */
  public void getClientIDTest() throws Fault {
    boolean pass = true;
    try {
      String cid = context.getClientID();
      TestUtil.logErr("getClientID() returned " + cid + ", expected clientid");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("getClientIDTest");
    }

    if (!pass) {
      throw new Fault("getClientIDTest failed");
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
   * @testName: multipleCloseContextTest
   * 
   * @assertion_ids: JMS:JAVADOC:912; JMS:SPEC:108;
   * 
   * @test_Strategy: Call close() twice on a JMSContext. This MUST NOT throw an
   * exception.
   */
  public void multipleCloseContextTest() throws Fault {
    try {
      TestUtil.logMsg("Call close on JMSContext created in setup.");
      context.close();
      TestUtil.logMsg("Call close on a JMSContext a second time");
      context.close();
    } catch (Exception e) {
      TestUtil.logMsg("Caught unexpected exception: " + e);
      throw new Fault("multipleCloseContextTest");
    }
  }

  /*
   * @testName: invalidDestinationRuntimeExceptionTests
   *
   * @assertion_ids: JMS:JAVADOC:944; JMS:JAVADOC:947; JMS:JAVADOC:951;
   * JMS:JAVADOC:920; JMS:JAVADOC:923; JMS:JAVADOC:1254; JMS:JAVADOC:1237;
   * JMS:JAVADOC:1242; JMS:JAVADOC:1246; JMS:JAVADOC:1250;
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
   * JMSContext.createBrowser(Queue, String) JMSContext.createBrowser(Queue)
   */
  public void invalidDestinationRuntimeExceptionTests() throws Fault {
    boolean pass = true;
    Destination invalidDestination = null;
    Queue invalidQueue = null;
    String message = "Where are you!";
    byte[] bytesMsgSend = message.getBytes();
    Map<String, Object> mapMsgSend = new HashMap<String, Object>();
    mapMsgSend.put("StringValue", "sendAndRecvTest7");
    mapMsgSend.put("BooleanValue", true);
    mapMsgSend.put("IntValue", (int) 10);
    try {
      // send to an invalid destination
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
          "Testing JMSContext.createBrowser(Queue, String) for InvalidDestinationRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling JMSContext.createBrowser(Queue, String) -> expect InvalidDestinationRuntimeException");
        context.createBrowser(invalidQueue, "lastMessage = TRUE");
      } catch (InvalidDestinationRuntimeException e) {
        TestUtil.logMsg("Got InvalidDestinationRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil.logErr(
            "Expected InvalidDestinationRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing JMSContext.createBrowser(Queue) for InvalidDestinationRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling JMSContext.createBrowser(Queue) -> expect InvalidDestinationRuntimeException");
        context.createBrowser(invalidQueue);
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
   * @assertion_ids: JMS:JAVADOC:948; JMS:JAVADOC:952; JMS:JAVADOC:924;
   *
   * @test_Strategy: Test InvalidSelectorRuntimeException conditions from
   * various API methods.
   *
   * JMSContext.createConsumer(Destination, String)
   * JMSContext.createConsumer(Destination, String, boolean)
   * JMSContext.createQueue(Queue, String)
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
        context.createConsumer(destination, invalidMessageSelector);
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
        context.createConsumer(destination, invalidMessageSelector, false);
      } catch (InvalidSelectorRuntimeException e) {
        TestUtil.logMsg("Got InvalidSelectorRuntimeException as expected.");
      } catch (Exception e) {
        TestUtil
            .logErr("Expected InvalidSelectorRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Testing JMSContext.createBrowser(Queue, String) for InvalidSelectorRuntimeException");
      try {
        TestUtil.logMsg(
            "Calling JMSContext.createBrowser(Queue, String) -> expect InvalidSelectorRuntimeException");
        context.createBrowser(queue, invalidMessageSelector);
      } catch (InvalidSelectorRuntimeException e) {
        TestUtil.logMsg("Got InvalidSelectorRuntimeException, as expected.");
      } catch (Exception e) {
        TestUtil
            .logErr("Expected InvalidSelectorRuntimeException,, received " + e);
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
   * @testName: illegalStateRuntimeExceptionTest
   *
   * @assertion_ids: JMS:JAVADOC:917; JMS:JAVADOC:994; JMS:JAVADOC:997;
   * JMS:JAVADOC:1340;
   *
   * @test_Strategy: 1. Create a TextMessages and send to Queue 2. Then invoke
   * JMSContext.commit() on a non-transacted session Verify that
   * IllegalStateRuntimeException is thrown 3. Then test invoke
   * JMSContext.rollback() on a non-transacted session Verify that
   * IllegalStateRuntimeException is thrown 3. Then test invoke
   * JMSContext.recover() on a transacted session Verify that
   * IllegalStateRuntimeException is thrown 4. Create JMSContext with
   * CLIENT_ACKNOWLEDGE then close JMSContext. Then test invoke
   * JMSContext.acknowledge() on the JMSContext. Verify that
   * IllegalStateRuntimeException is thrown
   *
   * JMSContext.commit(); JMSContext.rollback(); JMSContext.recover();
   * JMSContext.acknowledge();
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
      // send the message to the Queue
      TestUtil.logMsg("Sending message to the Queue");
      TestUtil.logMsg(
          "Sending TextMessage via JMSProducer.send(Destination, Message)");
      producer.send(destination, expTextMessage);

      try {
        TestUtil.logMsg(
            "JMSContext.commit() on non-transacted session must throw IllegalStateRuntimeException");
        context.commit();
        pass = false;
        TestUtil.logErr(
            "Error: JMSContext.commit() didn't throw expected IllegalStateRuntimeException");
      } catch (javax.jms.IllegalStateRuntimeException e) {
        logMsg(
            "Got expected IllegalStateRuntimeException from JMSContext.commit()");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        pass = false;
      }

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      expTextMessage = context.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "illegalStateRuntimeExceptionTest");
      // send the message to the Queue
      TestUtil.logMsg("Sending message to the Queue");
      TestUtil.logMsg(
          "Sending TextMessage via JMSProducer.send(Destination, Message)");
      producer.send(destination, expTextMessage);

      try {
        TestUtil.logMsg(
            "JMSContext.rollback() on non-transacted session must throw IllegalStateRuntimeException");
        context.rollback();
        pass = false;
        TestUtil.logErr(
            "Error: JMSContext.rollback() didn't throw expected IllegalStateRuntimeException");
      } catch (javax.jms.IllegalStateRuntimeException e) {
        logMsg(
            "Got expected IllegalStateRuntimeException from JMSContext.rollback()");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        pass = false;
      }

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
          TestUtil.logMsg("Caught expected exception" + e);
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
