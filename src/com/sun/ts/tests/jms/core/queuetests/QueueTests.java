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
package com.sun.ts.tests.jms.core.queuetests;

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

public class QueueTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.queuetests.QueueTests";

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
      throw new Exception("Didn't catch expected exception");
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
      queues = new ArrayList(10);
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
   * @exception Fault
   */
  public void cleanup() throws Fault {
    try {
      TestUtil.logMsg("Cleanup: Closing Queue Connections");
      tool.doClientQueueTestCleanup(connections, queues);
    } catch (Exception e) {
      TestUtil.logErr("An error occurred while cleaning", e);
      throw new Fault("Cleanup failed!", e);
    }
  }

  /*
   * @testName: emptyMsgsQueueTest
   *
   * @assertion_ids: JMS:SPEC:85; JMS:SPEC:88; JMS:JAVADOC:198; JMS:JAVADOC:334;
   * JMS:JAVADOC:301; JMS:JAVADOC:209; JMS:JAVADOC:211; JMS:JAVADOC:213;
   * JMS:JAVADOC:215; JMS:JAVADOC:217; JMS:JAVADOC:219; JMS:JAVADOC:221;
   * JMS:JAVADOC:223;
   *
   * @test_Strategy: Send and receive empty messages of each type.
   *
   */
  public void emptyMsgsQueueTest() throws Fault {
    try {

      // create Queue Connection
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueSender().setDeliveryMode(DeliveryMode.NON_PERSISTENT);
      tool.getDefaultQueueConnection().start();

      // send and receive simple message to Queue
      TestUtil.logMsg("Send generic Message to Queue.");
      Message msg = tool.getDefaultQueueSession().createMessage();

      msg.setStringProperty("COM_SUN_JMS_TESTNAME", "emptyMsgsQueueTest");
      tool.getDefaultQueueSender().send(msg);
      if (tool.getDefaultQueueReceiver().receive(timeout) == null) {
        throw new Exception("Did not receive message");
      }

      // send and receive bytes message to Queue
      TestUtil.logMsg("Send BytesMessage to Queue.");
      BytesMessage bMsg = tool.getDefaultQueueSession().createBytesMessage();

      bMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "emptyMsgsQueueTest");
      tool.getDefaultQueueSender().send(bMsg);
      if (tool.getDefaultQueueReceiver().receive(timeout) == null) {
        throw new Exception("Did not receive message");
      }

      // send and receive map message to Queue
      TestUtil.logMsg("Send MapMessage to Queue.");
      MapMessage mMsg = tool.getDefaultQueueSession().createMapMessage();

      mMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "emptyMsgsQueueTest");
      tool.getDefaultQueueSender().send(mMsg);
      if (tool.getDefaultQueueReceiver().receive(timeout) == null) {
        throw new Exception("Did not receive message");
      }

      // send and receive object message to Queue
      TestUtil.logMsg("Send ObjectMessage to Queue.");
      ObjectMessage oMsg = tool.getDefaultQueueSession().createObjectMessage();

      oMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "emptyMsgsQueueTest");
      tool.getDefaultQueueSender().send(oMsg);
      if (tool.getDefaultQueueReceiver().receive(timeout) == null) {
        throw new Exception("Did not receive message");
      }

      // send and receive stream message to Queue
      TestUtil.logMsg("Send SreamMessage to Queue.");
      StreamMessage sMsg = tool.getDefaultQueueSession().createStreamMessage();

      sMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "emptyMsgsQueueTest");
      tool.getDefaultQueueSender().send(sMsg);
      if (tool.getDefaultQueueReceiver().receive(timeout) == null) {
        throw new Exception("Did not receive message");
      }

      // send and receive text message to Queue
      TestUtil.logMsg("Send TextMessage to Queue.");
      TextMessage tMsg = tool.getDefaultQueueSession().createTextMessage();

      tMsg.setStringProperty("COM_SUN_JMS_TESTNAME", "emptyMsgsQueueTest");
      tool.getDefaultQueueSender().send(tMsg);
      if (tool.getDefaultQueueReceiver().receive(timeout) == null) {
        throw new Exception("Did not receive message");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("emptyMsgsQueueTest failed");
    }
  }

  /*
   * @testName: autoAckQueueTest
   *
   * @assertion_ids: JMS:SPEC:132; JMS:JAVADOC:198; JMS:JAVADOC:334;
   * JMS:JAVADOC:235;
   *
   * @test_Strategy: Send two messages to a queue. Receive the first message and
   * call session.recover(). Attempt to receive the second message.
   */
  public void autoAckQueueTest() throws Fault {
    try {
      Message messageSent = null;
      Message messageReceived = null;

      // create queue setup
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();

      // create and send messages
      messageSent = tool.getDefaultQueueSession().createMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "autoAckQueueTest");

      TestUtil.logTrace("send two messages");
      messageSent.setBooleanProperty("lastMessage", false);
      tool.getDefaultQueueSender().send(messageSent);
      messageSent.setBooleanProperty("lastMessage", true);
      tool.getDefaultQueueSender().send(messageSent);

      // receive message and call recover
      messageReceived = tool.getDefaultQueueReceiver().receive(timeout);
      if (messageReceived == null) {
        throw new Fault("Did not receive message");
      } else if (messageReceived.getBooleanProperty("lastMessage") == true) {
        throw new Fault("Error: received second message first");
      }
      TestUtil.logTrace("Message received. Call recover.");
      tool.getDefaultQueueSession().recover();

      // attempt to receive second message
      messageReceived = tool.getDefaultQueueReceiver().receive(timeout);

      // check message
      if (messageReceived == null) {
        throw new Fault("Did not receive second message as expected");
      } else if (messageReceived.getBooleanProperty("lastMessage") == false) {
        throw new Fault("Received original message again");
      } else {
        TestUtil.logMsg("Did not receive message again - GOOD");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("autoAckQueueTest");
    }
  }

  /*
   * @testName: simpleSendReceiveQueueTest
   * 
   * @assertion_ids: JMS:SPEC:138; JMS:JAVADOC:198; JMS:JAVADOC:334;
   * JMS:JAVADOC:122;
   * 
   * @test_Strategy: Send and receive single message to verify that Queues are
   * working.
   */
  public void simpleSendReceiveQueueTest() throws Fault {
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      TestUtil.logMsg("Creating 1 message");
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "simpleSendReceiveQueueTest");
      TestUtil.logMsg("Sending message");
      tool.getDefaultQueueSender().send(messageSent);
      TestUtil.logMsg("Receiving message");
      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        TestUtil.logMsg("Message text: \"" + messageReceived.getText() + "\"");
        TestUtil.logMsg("Received correct message");
      } else {
        throw new Exception("didn't get the right message");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("simpleSendReceiveQueueTest failed");
    }
  }

  /*
   * @testName: messageOrderQueueTest
   * 
   * @assertion_ids: JMS:SPEC:146; JMS:SPEC:147; JMS:JAVADOC:198;
   * JMS:JAVADOC:122; JMS:JAVADOC:334;
   * 
   * @test_Strategy: Send messages to a queue and receive them verify that the
   * text of each matches the order of the text in the sent messages.
   */
  public void messageOrderQueueTest() throws Fault {
    try {
      TextMessage tempMsg;
      String text[] = new String[numMessages];

      // set up tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();

      // create and send messages to queue
      for (int i = 0; i < numMessages; i++) {
        text[i] = "message order test " + i;
        tempMsg = tool.getDefaultQueueSession().createTextMessage();
        tempMsg.setText(text[i]);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "messageOrderQueueTest" + i);
        tool.getDefaultQueueSender().send(tempMsg);
        TestUtil.logTrace("Sent message: " + tempMsg.getText());
      }

      // receive messages and verify order
      for (int i = 0; i < numMessages; i++) {
        tempMsg = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);
        if (tempMsg == null) {
          throw new Exception("cannot receive message");
        }
        TestUtil.logTrace("Received message: " + tempMsg.getText());
        if (!tempMsg.getText().equals(text[i])) {
          TestUtil.logMsg("Received message: " + tempMsg.getText());
          TestUtil.logMsg("Should have: " + text[i]);
          throw new Exception("received wrong message");
        }
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("messageOrderQueueTest failed");
    }
  }

  /*
   * @testName: temporaryQueueNotConsumableTest
   * 
   * @assertion_ids: JMS:SPEC:144; JMS:JAVADOC:194;
   * 
   * @test_Strategy: Create temporary queue and a separate QueueSession. Try to
   * create a receiver for the temporary queue from the new session, which
   * should throw a JMSException. Also sends a blank message to verify that the
   * temporary queue is working.
   */
  public void temporaryQueueNotConsumableTest() throws Fault {
    boolean passed = false;

    try {

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();

      // create the TemporaryQueue
      TestUtil.logMsg("Creating TemporaryQueue");
      TemporaryQueue tempQ = tool.getDefaultQueueSession()
          .createTemporaryQueue();

      // open a new connection, create Session and Sender
      TestUtil.logMsg("Creating new Connection");
      QueueConnection newQConn = (QueueConnection) tool
          .getNewConnection(JmsTool.QUEUE, user, password);
      connections.add(newQConn);

      TestUtil.logMsg("Create new Session");
      QueueSession newQSess = newQConn.createQueueSession(false,
          Session.AUTO_ACKNOWLEDGE);

      TestUtil.logMsg("Create new sender for TemporaryQueue");
      QueueSender newQSender = newQSess.createSender(tempQ);

      // send message to verify TemporaryQueue
      TestUtil.logMsg("Send message to TemporaryQueue");
      Message msg = tool.getDefaultQueueSession().createMessage();

      msg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "temporaryQueueNotConsumableTest");
      newQSender.send(msg);

      // try to create receiver for the TemporaryQueue
      TestUtil.logMsg(
          "Attempt to create receiver for TemporaryQueue from another Session");
      try {
        QueueReceiver newQReceiver = newQSess.createReceiver(tempQ);
        if (newQReceiver != null)
          TestUtil.logTrace("newQReceiver=" + newQReceiver);
      } catch (JMSException e) {
        TestUtil.logMsg("Received expected JMSException -- GOOD");
        passed = true;
      }

      // close new connection
      TestUtil.logTrace("Closing new QueueConnection");
      newQConn.close();

      // throw exception if test failed
      checkExceptionPass(passed);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("temporaryQueueNotConsumableTest failed");
    }
  }

  /*
   * @testName: messageSelectorMsgRemainsOnQueueTest
   * 
   * @assertion_ids: JMS:SPEC:146; JMS:SPEC:147; JMS:JAVADOC:186;
   * 
   * @test_Strategy: Send two messages to a queue and receive the second one
   * using a message selector. Verify that the first message is still in the
   * queue by receiving with a different receiver.
   */
  public void messageSelectorMsgRemainsOnQueueTest() throws Fault {
    boolean pass = true;

    try {
      Message messageSent = null;
      Message messageReceived = null;

      // create Queue Connection
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);

      // create receiver with message selector (close default receiver)
      TestUtil.logMsg("Creating receiver with message selector");
      tool.getDefaultQueueReceiver().close();
      QueueReceiver qSelectiveReceiver = tool.getDefaultQueueSession()
          .createReceiver(tool.getDefaultQueue(), "TEST = 'test'");

      // start connection
      tool.getDefaultQueueConnection().start();

      // send two messages
      TestUtil.logTrace("Sending two Messages");
      messageSent = tool.getDefaultQueueSession().createMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "messageSelectorMsgRemainsOnQueueTest");

      messageSent.setBooleanProperty("lastMessage", false);
      tool.getDefaultQueueSender().send(messageSent);
      messageSent.setStringProperty("TEST", "test");
      messageSent.setBooleanProperty("lastMessage", true);
      tool.getDefaultQueueSender().send(messageSent);

      // check selective receiver
      TestUtil.logTrace("Receiving message with selective receiver");
      messageReceived = qSelectiveReceiver.receive(timeout);
      if (messageReceived == null) {
        pass = false;
        TestUtil.logErr("Did not receive any message");
      } else if (messageReceived.getBooleanProperty("lastMessage") == false) {
        pass = false;
        TestUtil.logErr("Received incorrect message");
      } else {
        TestUtil.logMsg("Selective Receiver received correct message");
      }

      // receive original message with normal receiver
      qSelectiveReceiver.close();
      QueueReceiver qRec = tool.getDefaultQueueSession()
          .createReceiver(tool.getDefaultQueue());
      messageReceived = qRec.receive(timeout);
      if (messageReceived == null) {
        pass = false;
        TestUtil.logErr("Un-received message did not remain on queue");
      } else if (messageReceived.getBooleanProperty("lastMessage") == true) {
        pass = false;
        TestUtil.logErr("received incorrect message");
      } else {
        TestUtil.logMsg("Corrected Message left on Queue is received");
      }

      if (!pass)
        throw new Fault("messageSelectorMsgRemainsOnQueueTest Failed!!");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("messageSelectorMsgRemainsOnQueueTest failed");
    }
  }

  /*
   * @testName: msgSelectorMsgHeaderQueueTest
   * 
   * @assertion_ids: JMS:SPEC:38; JMS:JAVADOC:186;
   * 
   * @test_Strategy: Create receiver with a message selector that uses a message
   * header. Send two messages, one that has the proper header and one that
   * doesn't, and try to receive only matching message.
   */
  public void msgSelectorMsgHeaderQueueTest() throws Fault {
    boolean pass = true;

    try {

      // create Queue Connection
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);

      // create receiver with message selector (close default receiver)
      TestUtil.logMsg("Creating receiver with message selector");
      tool.getDefaultQueueReceiver().close();
      QueueReceiver qSelectiveReceiver = tool.getDefaultQueueSession()
          .createReceiver(tool.getDefaultQueue(), "JMSType = 'test_message'");

      // start connection
      tool.getDefaultQueueConnection().start();

      // send messages
      Message m = tool.getDefaultQueueSession().createMessage();
      m.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgSelectorMsgHeaderQueueTest");

      TestUtil.logTrace("Sending message not matching selector");
      m.setJMSType("foo");
      m.setBooleanProperty("lastMessage", false);
      tool.getDefaultQueueSender().send(m);

      TestUtil.logTrace("Sending test message with header property JMSType");
      m.setJMSType("test_message");
      m.setBooleanProperty("lastMessage", true);
      tool.getDefaultQueueSender().send(m);

      // attempt to receive correct message
      TestUtil.logMsg("Attempt to receive 'good' message");
      Message msg1 = qSelectiveReceiver.receive(timeout);
      qSelectiveReceiver.close();
      if (msg1 == null) {
        pass = false;
        TestUtil.logErr("Did not receive any message");
      } else if (msg1.getBooleanProperty("lastMessage") == true) {
        TestUtil.logMsg("Received correct message -- GOOD");
      } else {
        pass = false;
        TestUtil.logErr("Received message not matching header");
      }

      // remove remaining message from queue
      QueueReceiver qRec = tool.getDefaultQueueSession()
          .createReceiver(tool.getDefaultQueue());
      msg1 = qRec.receive(timeout);
      if (msg1 == null) {
        pass = false;
        TestUtil.logErr("No message received.");
      } else if (msg1.getBooleanProperty("lastMessage") == false) {
        TestUtil.logMsg("Received correct message left");
      } else {
        pass = false;
        TestUtil.logErr("Received incorrect message");
      }

      if (!pass)
        throw new Fault("msgSelectorMsgHeaderQueueTest failed!!");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("msgSelectorMsgHeaderQueueTest failed");
    }
  }

  /*
   * @testName: queueBrowserMsgsRemainOnQueueTest
   *
   * @assertion_ids: JMS:JAVADOC:186; JMS:JAVADOC:221; JMS:JAVADOC:411;
   * JMS:JAVADOC:425; JMS:JAVADOC:120; JMS:JAVADOC:190; JMS:JAVADOC:282;
   * JMS:JAVADOC:284; JMS:SPEC:148; JMS:SPEC:149; JMS:JAVADOC:122;
   *
   * @test_Strategy: 1. Create a Queue and send x messages to it. 2. Create a
   * QueueReceiver with a message selector so that only last message received.
   * 3. Create a QueueBrowser to browse the queue. 4. Then create another
   * QueueReceiver to verify all x messages can be received from the Queue.
   */
  public void queueBrowserMsgsRemainOnQueueTest() throws Fault {
    try {
      TextMessage tempMsg = null;
      QueueReceiver lastMessageReceiver = null;
      QueueReceiver qRec = null;
      QueueBrowser qBrowser = null;
      Enumeration msgs = null;

      // create QueueConnection
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueReceiver().close();
      lastMessageReceiver = tool.getDefaultQueueSession()
          .createReceiver(tool.getDefaultQueue(), "lastMessage = TRUE");
      tool.getDefaultQueueConnection().start();

      // send "numMessages" messages to queue plus end of stream message
      for (int i = 0; i <= numMessages; i++) {
        tempMsg = tool.getDefaultQueueSession().createTextMessage();
        if (i == numMessages) {
          tempMsg.setBooleanProperty("lastMessage", true);
        } else {
          tempMsg.setBooleanProperty("lastMessage", false);
        }
        tempMsg.setText("message" + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "queueBrowserMsgsRemainOnQueueTest" + i);
        tool.getDefaultQueueSender().send(tempMsg);
        TestUtil.logTrace("message " + i + " sent");
      }

      // receive final message, all messages now processed
      tempMsg = (TextMessage) lastMessageReceiver.receive(timeout);
      if (tempMsg == null) {
        throw new Fault("Did not receive expected message");
      } else {
        TestUtil.logTrace("Received last sent message, lastMessage="
            + tempMsg.getBooleanProperty("lastMessage"));
      }
      lastMessageReceiver.close();

      // create QueueBrowser
      TestUtil.logTrace("Creating QueueBrowser");
      qBrowser = tool.getDefaultQueueSession()
          .createBrowser(tool.getDefaultQueue());

      // check for messages
      TestUtil.logMsg(
          "Checking for " + numMessages + " messages with QueueBrowser");
      int msgCount = 0;
      msgs = qBrowser.getEnumeration();
      while (msgs.hasMoreElements()) {
        tempMsg = (TextMessage) msgs.nextElement();
        msgCount++;
      }
      TestUtil.logTrace("found " + msgCount + " messages total in browser");

      // check to see if all messages found
      if (msgCount != numMessages) {

        // not the test assertion, test can still pass
        TestUtil.logMsg("Warning: browser did not find all messages");
      }
      qBrowser.close();

      // continue and try to receive all messages
      qRec = tool.getDefaultQueueSession()
          .createReceiver(tool.getDefaultQueue());
      TestUtil
          .logMsg("Receive all remaining messages to verify still in queue");
      for (msgCount = 0; msgCount < numMessages; msgCount++) {
        tempMsg = (TextMessage) qRec.receive(timeout);
        if (tempMsg == null) {
          throw new Fault("Message " + msgCount + " missing from queue");
        } else {
          TestUtil.logTrace("received message: " + tempMsg.getText());
        }
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("queueBrowserMsgsRemainOnQueueTest failed");
    }
  }

  /*
   * @testName: inactiveClientReceiveQueueTest
   * 
   * @assertion_ids: JMS:SPEC:150;
   * 
   * @test_Strategy: Create a queue with no receiver and send messages to it.
   * Create a new session with a receiver for the queue and verify that the
   * messages can still be received.
   */
  public void inactiveClientReceiveQueueTest() throws Fault {
    try {
      TextMessage tempMsg;
      QueueSession qSession;
      QueueReceiver qReceiver;

      // create queue with no receiver
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueReceiver().close();
      tool.getDefaultQueueConnection().start();

      // create and send messages
      for (int i = 0; i < numMessages; i++) {
        tempMsg = tool.getDefaultQueueSession().createTextMessage();
        tempMsg.setText("message" + i);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "inactiveClientReceiveQueueTest" + i);
        tool.getDefaultQueueSender().send(tempMsg);
        TestUtil.logTrace("message " + i + " sent");
      }

      tool.getDefaultQueueSession().close();

      // create session with a receiver for the queue
      qSession = tool.getDefaultQueueConnection().createQueueSession(false,
          Session.AUTO_ACKNOWLEDGE);
      qReceiver = qSession.createReceiver(tool.getDefaultQueue());

      // attempt to receive all messages
      for (int i = 0; i < numMessages; i++) {
        tempMsg = (TextMessage) qReceiver.receive(timeout);
        if (tempMsg == null) {
          throw new Exception("Did not receive all messages");
        }
        TestUtil.logTrace("received message " + i);
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("inactiveClientReceiveQueueTest failed");
    }
  }

  /*
   * @testName: msgProducerNullDestinationQueueTest
   * 
   * @assertion_ids: JMS:SPEC:139; JMS:JAVADOC:188; JMS:JAVADOC:186;
   * JMS:JAVADOC:198;
   * 
   * @test_Strategy: Create a q sender, specifying null for the destination send
   * the message to the default destination and then verify receiving it
   */
  public void msgProducerNullDestinationQueueTest() throws Fault {
    boolean pass = true;
    QueueSender qSender = null;
    Queue nullQ = null;
    TextMessage messageSent = null;
    TextMessage messageReceived = null;

    try {
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      TestUtil.logTrace("Creating  1 message");
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText(
          "test creating a producer without specifying the destination");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgProducerNullDestinationQueueTest");
      TestUtil.logMsg("Test createSender(null) - This is valid");
      try {
        qSender = tool.getDefaultQueueSession().createSender(nullQ);
        TestUtil.logTrace("PASS: null allowed for unidentified producer");
      } catch (Exception ee) {
        TestUtil.printStackTrace(ee);
        TestUtil.logMsg("Error: unexpected error " + ee.getClass().getName()
            + " was thrown");
        pass = false;
      }

      // publish to a queue and then get the message.
      TestUtil.logTrace("Send a message");
      qSender.send(tool.getDefaultQueue(), messageSent);
      TestUtil.logTrace("Receive a message");
      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceived == null) {
        pass = false;
      } else {
        TestUtil.logTrace("Got message - OK");
      }
      if (!pass) {
        throw new Fault(
            "Error: failures occurred during msgProducerNullDestinationQueueTest tests");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("msgProducerNullDestinationQueueTest failed");
    }
  }

  /*
   * @testName: multipleCloseQueueConnectionTest
   * 
   * @assertion_ids: JMS:SPEC:108;
   * 
   * @test_Strategy: Call close() twice on a connection and catch any exception.
   */
  public void multipleCloseQueueConnectionTest() throws Fault {
    try {

      // create Queue Connection
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      TestUtil.logTrace("Call close on a connection ");
      tool.getDefaultQueueConnection().close();
      TestUtil.logTrace("Call close on a connection a second time");
      tool.getDefaultQueueConnection().close();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("multipleCloseQueueConnectionTest failed");
    }
  }

  /*
   * @testName: messageOrderDeliveryModeQueueTest
   * 
   * @assertion_ids: JMS:SPEC:127; JMS:SPEC:128; JMS:JAVADOC:122;
   * 
   * @test_Strategy: Send non persistent messages to a queue and receive them.
   * Verify that the text of each matches the order of the text in the sent
   * messages.
   */
  public void messageOrderDeliveryModeQueueTest() throws Fault {
    try {
      TextMessage tempMsg;
      String text[] = new String[numMessages];

      // set up tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();

      // Persistent delivery is the default
      tool.getDefaultQueueSender().setDeliveryMode(DeliveryMode.NON_PERSISTENT);

      // create and send messages to queue
      for (int i = 0; i < numMessages; i++) {
        text[i] = "message order test " + i;
        tempMsg = tool.getDefaultQueueSession().createTextMessage();
        tempMsg.setText(text[i]);
        tempMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
            "messageOrderDeliveryModeQueueTest" + i);
        tool.getDefaultQueueSender().send(tempMsg);
        TestUtil.logTrace("Sent message: " + tempMsg.getText());
      }

      // receive messages and verify order
      for (int i = 0; i < numMessages; i++) {
        tempMsg = (TextMessage) tool.getDefaultQueueReceiver().receive(timeout);
        if (tempMsg == null) {
          throw new Exception("cannot receive message");
        }
        TestUtil.logTrace("Received message: " + tempMsg.getText());
        if (!tempMsg.getText().equals(text[i])) {
          TestUtil.logMsg("Received message: " + tempMsg.getText());
          TestUtil.logMsg("Should have: " + text[i]);
          throw new Exception("received wrong message");
        }
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("messageOrderDeliveryModeQueueTest failed");
    }
  }

  /*
   * @testName: tempQueueTests
   *
   * @assertion_ids: JMS:SPEC:144; JMS:JAVADOC:262; JMS:JAVADOC:126;
   *
   * @test_Strategy: 1. Create a TemporaryQueue from a Session. Send a
   * TextMessage and Receive it using the TemporaryQueue. Verify the Message
   * received correctly. 2. Try to delete the TemporaryQueue without closing
   * MessageConsumer, verify that JMSException is thrown. 3. Close the
   * MessageConsumer, verify that the TemporaryQueue can be deleted. 4. Try to
   * create a MessageConsumer using Session from a different Connection, verify
   * that JMSException is thrown.
   */
  public void tempQueueTests() throws Fault {
    boolean pass = true;
    TextMessage msgSent;
    TextMessage msgReceived;
    String testName = "tempQueueTests";
    String message = "Just a test from tempQueueTests";
    TemporaryQueue tempQ = null;
    Connection newConn = null;

    try {
      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultProducer().close();
      tool.getDefaultConsumer().close();
      tool.getDefaultConnection().start();

      // create the TemporaryQueue
      TestUtil.logMsg("Creating TemporaryQueue");
      tempQ = tool.getDefaultSession().createTemporaryQueue();

      // open a new connection, create Session and Sender
      TestUtil.logMsg("Create new sender for TemporaryQueue");
      MessageProducer sender = tool.getDefaultSession().createProducer(tempQ);
      MessageConsumer receiver = tool.getDefaultSession().createConsumer(tempQ);

      // send message to verify TemporaryQueue
      TestUtil.logMsg("Send message to TemporaryQueue");
      msgSent = tool.getDefaultSession().createTextMessage();
      msgSent.setText(message);
      msgSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      sender.send(msgSent);

      // try to create receiver for the TemporaryQueue
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
        tempQ.delete();
        pass = false;
        TestUtil.logErr(
            "Didn't throw expected JMSException calling TemporaryQueue.delete()");
      } catch (JMSException em) {
        TestUtil.logTrace("Received expected JMSException: ");
      }
      receiver.close();

      try {
        tempQ.delete();
        TestUtil.logTrace(
            "Succesfully calling TemporaryQueue.delete() after closing Receiver");
      } catch (Exception e) {
        pass = false;
        TestUtil.logErr("Received unexpected Exception: ", e);
      }

      tempQ = tool.getDefaultSession().createTemporaryQueue();
      newConn = (Connection) tool.getNewConnection(JmsTool.COMMON_Q, user,
          password);
      connections.add(newConn);
      Session newSess = newConn.createSession(false, Session.AUTO_ACKNOWLEDGE);

      // try to create receiver for the TemporaryQueue
      TestUtil.logMsg(
          "Attempt to create MessageConsumer for TemporaryQueue from another Connection");
      try {
        MessageConsumer newReceiver = newSess.createConsumer(tempQ);
        if (newReceiver != null)
          TestUtil.logTrace("newReceiver=" + newReceiver);
        logTrace("FAIL: expected IllegalStateException");
        TestUtil.logErr(
            "Didn't throw expected JMSException calling Session.createConsumer(TemporaryQueue)");
      } catch (JMSException e) {
        TestUtil
            .logTrace("Received expected JMSException from createConsumer.");
      }

      if (!pass)
        throw new Fault(testName + " failed");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault(testName);
    }
  }
}
