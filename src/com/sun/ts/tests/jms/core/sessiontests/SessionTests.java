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
package com.sun.ts.tests.jms.core.sessiontests;

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

public class SessionTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.sessiontests.SessionTests";

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

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    SessionTests theTests = new SessionTests();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */

  /*
   * setup() is called before each test
   * 
   * Creates Administrator object and deletes all previous Destinations.
   * Individual tests create the JmsTool object with one default Queue and/or
   * Topic Connection, as well as a default Queue and Topic. Tests that require
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
        throw new Exception(
            "'jms_timeout' (milliseconds) in ts.jte must be > 0");
      }
      if (user == null) {
        throw new Exception("'user' in ts.jte must not be null");
      }
      if (password == null) {
        throw new Exception("'password' in ts.jte must not be null");
      }
      if (mode == null) {
        throw new Exception("'platform.mode' in ts.jte must not be null");
      }
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
  }

  /* Tests */

  /*
   * @testName: SimpleSendAndReceiveQ
   * 
   * @assertion_ids: JMS:SPEC:195; JMS:JAVADOC:502; JMS:JAVADOC:504;
   * JMS:JAVADOC:510; JMS:JAVADOC:242; JMS:JAVADOC:244; JMS:JAVADOC:221;
   * JMS:JAVADOC:317; JMS:JAVADOC:334;
   * 
   * @test_Strategy: Create a Text Message, send use a MessageProducer and
   * receive it use a MessageConsumer via a Queue.
   */
  public void SimpleSendAndReceiveQ() throws Fault {

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      boolean pass = true;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultConnection().start();

      messageSent = tool.getDefaultSession().createTextMessage();
      messageSent.setText("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "SimpleSendAndReceiveQ");

      // send the message and then get it back
      logTrace("Sending message to a Queue");
      tool.getDefaultProducer().send(messageSent);
      logTrace("Receiving message");
      messageReceived = (TextMessage) tool.getDefaultConsumer()
          .receive(timeout);

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        logMsg("Message text: \"" + messageReceived.getText() + "\"");
        logMsg("Received correct message");
      } else {
        logErr("didn't get the right message using Queue");
        pass = false;
      }

      if (pass != true)
        throw new Fault("SimpleSendAndReceiveQ Failed!");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("SimpleSendAndReceiveQ");
    } finally {
      try {
        tool.closeDefaultConnections();
        tool.flushDestination();
      } catch (Exception ex) {
        logErr("Error closing Connection", ex);
      }
    }
  }

  /*
   * @testName: SimpleSendAndReceiveT
   * 
   * @assertion_ids: JMS:SPEC:196; JMS:JAVADOC:502; JMS:JAVADOC:504;
   * JMS:JAVADOC:510; JMS:JAVADOC:242; JMS:JAVADOC:244; JMS:JAVADOC:221;
   * JMS:JAVADOC:317; JMS:JAVADOC:334;
   *
   * @test_Strategy: Create a Text Message, send use a MessageProducer and
   * receive it use a MessageConsumer via a Topic.
   */
  public void SimpleSendAndReceiveT() throws Fault {

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      boolean pass = true;

      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      tool.getDefaultConnection().start();

      messageSent = tool.getDefaultSession().createTextMessage();
      messageSent.setText("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "SimpleSendAndReceiveT");

      // send the message and then get it back
      logTrace("Sending message to a Topic");
      tool.getDefaultProducer().send(messageSent);
      logTrace("Receiving message");
      messageReceived = (TextMessage) tool.getDefaultConsumer()
          .receive(timeout);

      // Check to see if correct message received
      if (messageReceived.getText().equals(messageSent.getText())) {
        logMsg("Message text: \"" + messageReceived.getText() + "\"");
        logMsg("Received correct message");
      } else {
        logErr("didn't get the right message");
        pass = false;
      }

      if (pass != true)
        throw new Fault("SimpleSendAndReceiveT Failed!");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("SimpleSendAndReceiveT");
    } finally {
      try {
        tool.closeDefaultConnections();
      } catch (Exception ex) {
        logErr("Error closing Connection", ex);
      }
    }
  }

  /*
   * @testName: selectorAndBrowserTests
   *
   * @assertion_ids: JMS:SPEC:195; JMS:JAVADOC:502; JMS:JAVADOC:504;
   * JMS:JAVADOC:510; JMS:JAVADOC:242; JMS:JAVADOC:244; JMS:JAVADOC:246;
   * JMS:JAVADOC:317; JMS:JAVADOC:334; JMS:JAVADOC:338; JMS:JAVADOC:258;
   * JMS:JAVADOC:260; JMS:JAVADOC:221; JMS:SPEC:148; JMS:SPEC:149;
   * JMS:JAVADOC:278; JMS:JAVADOC:280; JMS:JAVADOC:288; JMS:JAVADOC:282;
   * JMS:JAVADOC:284;
   *
   * @test_Strategy: 1. Create two TextMessages, send use a MessageProducer 2.
   * Create a QueueBrowser to browse the Queue so that all two messages can be
   * seen. 3. Create a QueueBrowser with selector to browse the Queue so that
   * only one message can be seen; 4. Create a MessageConsumer with a message
   * selector so that only last message received. 5. Then create another
   * MessageConsumer to verify all messages except the last message can be
   * received from the Queue.
   */
  public void selectorAndBrowserTests() throws Fault {

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      TextMessage tempMsg = null;
      boolean pass = true;
      int numMessages = 2;
      QueueBrowser browseAll = null;
      QueueBrowser selectiveBrowser = null;
      String message = "Just a Test Message from JMS TCK";

      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultConsumer().close();
      tool.getDefaultConnection().start();

      messageSent = tool.getDefaultSession().createTextMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "selectorAndBrowserTests");
      messageSent.setText(message);
      messageSent.setBooleanProperty("lastMessage", false);
      tool.getDefaultProducer().send(messageSent);

      messageSent.setStringProperty("TEST", "test");
      messageSent.setBooleanProperty("lastMessage", true);
      tool.getDefaultProducer().send(messageSent);

      // check the browser w/o selector
      browseAll = tool.getDefaultSession()
          .createBrowser((Queue) tool.getDefaultDestination());

      // getting Emumeration that contains at least two test messages
      // without getting into dead loop.
      // Workaround for possible timing problem
      int msgCount = 0;
      Enumeration msgs = null;
      int i = 0;
      do {
        i++;
        msgCount = 0;
        msgs = browseAll.getEnumeration();
        TestUtil.logTrace("getting Enumeration " + i);
        while (msgs.hasMoreElements()) {
          tempMsg = (TextMessage) msgs.nextElement();
          if (tempMsg.getText().equals(messageSent.getText()))
            msgCount++;
        }
        TestUtil.logTrace("found " + msgCount + " messages total in browser");
      } while ((msgCount < 2) && (i < 10));

      if (!browseAll.getQueue().toString()
          .equals(tool.getDefaultDestination().toString())) {
        pass = false;
        logErr("Error: QueueBrowser.getQueue test failed");
        logErr(
            "QueueBrowser.getQueue=" + browseAll.getQueue().toString() + ".");
        logErr("tool.getDefaultDestination()="
            + tool.getDefaultDestination().toString() + ".");
      }

      browseAll.close();

      // Browse with selective QueueBrowser
      selectiveBrowser = tool.getDefaultSession()
          .createBrowser((Queue) tool.getDefaultDestination(), "TEST = 'test'");

      // getting Emumeration that contains at least two test messages
      // without getting into dead loop.
      // Workaround for possible timing problem
      i = 0;
      do {
        i++;
        msgCount = 0;
        msgs = selectiveBrowser.getEnumeration();
        TestUtil.logTrace("getting Enumeration " + i);
        while (msgs.hasMoreElements()) {
          tempMsg = (TextMessage) msgs.nextElement();
          if (tempMsg.getText().equals(messageSent.getText()))
            msgCount++;
        }
        TestUtil.logTrace("found " + msgCount + " messages total in browser");
      } while ((msgCount < 1) && (i < 10));

      String tmp = selectiveBrowser.getMessageSelector();
      if (tmp.indexOf("TEST") < 0 || tmp.indexOf("test") < 0) {
        pass = false;
        logErr("Error: QueueBrowser.getMessageSelector test failed");
        logErr("selectiveBrowser.getMessageSelector()="
            + selectiveBrowser.getMessageSelector());
      }
      selectiveBrowser.close();

      // check selective consumer
      MessageConsumer SelectorConsumer = tool.getDefaultSession()
          .createConsumer(tool.getDefaultDestination(), "TEST = 'test'");
      logTrace("Receiving message with selective consumer");
      messageReceived = (TextMessage) SelectorConsumer.receive(timeout);
      if (messageReceived == null) {
        pass = false;
        logErr("Did not receive expected message");
      } else if (messageReceived.getBooleanProperty("lastMessage") == false) {
        pass = false;
        logErr("Received incorrect message");
      }
      SelectorConsumer.close();

      // receive all remaining messages
      MessageConsumer qRec = tool.getDefaultSession()
          .createConsumer(tool.getDefaultDestination());
      logTrace("Receiving the remaining message");
      messageReceived = (TextMessage) qRec.receive(timeout);
      if (messageReceived == null) {
        pass = false;
        logErr("message did not remain on queue");
      } else if (messageReceived.getBooleanProperty("lastMessage") == true) {
        pass = false;
        logErr("received incorrect message");
      }
      qRec.close();

      if (pass != true)
        throw new Fault("selectorAndBrowserTests Failed!");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("selectorAndBrowserTests");
    } finally {
      try {
        tool.closeDefaultConnections();
        tool.flushDestination();
      } catch (Exception ex) {
        logErr("Error closing Connection", ex);
      }
    }
  }

  /*
   * @testName: SubscriberTests
   *
   * @assertion_ids: JMS:SPEC:196; JMS:SPEC:89; JMS:SPEC:162; JMS:JAVADOC:502;
   * JMS:JAVADOC:504; JMS:JAVADOC:510; JMS:JAVADOC:242; JMS:JAVADOC:244;
   * JMS:JAVADOC:254; JMS:JAVADOC:256; JMS:JAVADOC:266;
   *
   * @test_Strategy: 1. Create a new second connection and send two
   * TextMessages. 2. Create a DurableSubscriber defaultSub to verify all
   * messages received. 3. Create another DurableSubscriber tSubNoLocal with
   * noLocal set to true, and verify that no message can be received. 4. Create
   * another DurableSubscriber tSubSelect off the new connection with selector
   * to verify only one message received. 5. Send a message from from default
   * connection. 6. Verify that tSubNoLocal can receive the message from the
   * default connection 7. Unsubscribe the 3 durable subscriptions created
   * above.
   */
  public void SubscriberTests() throws Fault {
    String lookup = "DURABLE_SUB_CONNECTION_FACTORY";
    TextMessage messageSent = null;
    TextMessage messageReceived = null;
    TextMessage tempMsg = null;
    int numMessages = 2;
    boolean pass = true;
    TopicSubscriber defaultSub = null;
    TopicSubscriber tSubSelect = null;
    TopicSubscriber tSubNoLocal = null;
    String subscriptionName1 = "DurableSubscriberNoLocal";
    String subscriptionName2 = "DurableSubscriberSelect";
    String subscriptionName3 = "DurableSubscriberDefault";
    Connection newConn = null;
    Session newSess = null;
    MessageProducer newPub = null;
    String clientID = "CTS";

    try {
      logTrace("Setup tool for COMMON_T setup");
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);

      logTrace("Create second connection and second session");
      newConn = tool.getNewConnection(JmsTool.COMMON_T, user, password, lookup);
      newSess = newConn.createSession(false, Session.AUTO_ACKNOWLEDGE);
      logTrace(
          "Create 3 durabale subscriptions (default, selector, nolocal) using second session");
      tSubNoLocal = newSess.createDurableSubscriber(
          (Topic) tool.getDefaultDestination(), subscriptionName1, "", true);
      tSubSelect = newSess.createDurableSubscriber(
          (Topic) tool.getDefaultDestination(), subscriptionName2,
          "TEST = 'test'", false);
      defaultSub = newSess.createDurableSubscriber(
          (Topic) tool.getDefaultDestination(), subscriptionName3);
      logTrace("Create producer using second session");
      newPub = newSess.createProducer(tool.getDefaultDestination());

      tool.getDefaultConnection().start();
      newConn.start();

      // Create and send two messages from new connection
      messageSent = tool.getDefaultSession().createTextMessage();
      messageSent.setText("Just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "SubscriberTests");
      logTrace("Sending message to a Topic");
      messageSent.setBooleanProperty("lastMessage", false);
      newPub.send(messageSent);
      messageSent.setStringProperty("TEST", "test");
      messageSent.setBooleanProperty("lastMessage", true);
      newPub.send(messageSent);

      // Verify that tSubNoLocal cannot receive any message
      logTrace("tSubNoLocal Receiving message");
      messageReceived = (TextMessage) tSubNoLocal.receive(timeout);
      if (messageReceived != null) {
        pass = false;
        logErr("Error:  No_local subscriber did receive local message");
      }

      // Verify that defaultSub received correct messages
      logTrace("defaultSub Receiving message");
      for (int i = 0; i < numMessages; i++) {
        messageReceived = (TextMessage) defaultSub.receive(timeout);
        if (messageReceived == null) {
          pass = false;
          logErr("Error:  Did not receive message " + i);
        } else if (messageReceived.getText().equals(messageSent.getText())) {
          logMsg("Message text: \"" + messageReceived.getText() + "\"");
          logMsg("Received correct message " + i);
        } else {
          logErr("Error: didn't get the right message " + i);
          pass = false;
        }
      }

      // Verify that tSubSelect only receive the last message
      logTrace("tSubSelect Receiving message");
      messageReceived = (TextMessage) tSubSelect.receive(timeout);
      if (messageReceived == null) {
        pass = false;
        logErr("Error:  Did not receive correct message");
      } else if (messageReceived.getText().equals(messageSent.getText())) {
        logMsg("Message text: \"" + messageReceived.getText() + "\"");
        logMsg("Received correct message");
      } else {
        logErr("Error: didn't get the right message");
        pass = false;
      }

      // send message from default connection
      logTrace("sending message from default connection");
      messageSent.setBooleanProperty("newConnection", true);
      tool.getDefaultProducer().send(messageSent);

      // Verify that tSubNoLocal now can receive message from second connection
      logTrace("tSubNoLocal Receiving message");
      messageReceived = (TextMessage) tSubNoLocal.receive(timeout);
      if (messageReceived == null) {
        pass = false;
        logErr("Error:  Did not receive correct message");
      } else if (messageReceived.getText().equals(messageSent.getText())) {
        logMsg("Message text: \"" + messageReceived.getText() + "\"");
        logMsg("Received correct message");
      } else {
        logErr("Error: didn't get the right message");
        pass = false;
      }

      if (pass != true)
        throw new Fault("SubscriberTests Failed!");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("SubscriberTests");
    } finally {
      try {
        logTrace("Close 3 durable subscriptions");
        tSubNoLocal.close();
        defaultSub.close();
        tSubSelect.close();
        logTrace("Unsubscribe from 3 durable subscriptions");
        newSess.unsubscribe(subscriptionName1);
        newSess.unsubscribe(subscriptionName2);
        newSess.unsubscribe(subscriptionName3);
      } catch (Exception ex) {
        logErr("Error closing subscribers and unsubscribing from subscriptions",
            ex);
      }
      try {
        logTrace("Closing new connection");
        newConn.close();
      } catch (Exception ex) {
        logErr("Error closing the second Connection", ex);
      }
      try {
        tool.closeDefaultConnections();
      } catch (Exception ex) {
        logErr("Error closing Default Connection", ex);
      }
    }
  }

  /*
   * @testName: IllegalStateTestQ
   *
   * @assertion_ids: JMS:SPEC:195; JMS:JAVADOC:502; JMS:JAVADOC:504;
   * JMS:JAVADOC:510; JMS:JAVADOC:242; JMS:JAVADOC:635; JMS:JAVADOC:317;
   *
   * @test_Strategy: 1. Create a TextMessages, send use a MessageProducer 2.
   * Then rollback on the non-transacted session Verify that
   * IllegalStateException is thrown
   */
  public void IllegalStateTestQ() throws Fault {

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      boolean pass = true;

      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultConnection().start();

      messageSent = tool.getDefaultSession().createTextMessage();
      messageSent.setText("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "IllegalStateTestQ");

      // send the message and then get it back
      logTrace("Sending message to a Queue");
      tool.getDefaultProducer().send(messageSent);

      try {
        logTrace(
            "Rolling back a non-transacted session must throw IllegalStateException");
        tool.getDefaultSession().rollback();
        pass = false;
        logErr(
            "Error: QueueSession.rollback() didn't throw expected IllegalStateException");
      } catch (javax.jms.IllegalStateException en) {
        logMsg(
            "Got expected IllegalStateException from QueueSession.rollback()");
      }

      if (pass != true)
        throw new Fault("IllegalStateTestQ");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("IllegalStateTestQ");
    } finally {
      try {
        tool.closeDefaultConnections();
        tool.flushDestination();
      } catch (Exception ex) {
        logErr("Error closing Connection", ex);
      }
    }
  }

  /*
   * @testName: ackTests
   *
   * @assertion_ids: JMS:SPEC:195; JMS:SPEC:196; JMS:JAVADOC:502;
   * JMS:JAVADOC:504; JMS:JAVADOC:510; JMS:JAVADOC:242; JMS:JAVADOC:227;
   *
   * @test_Strategy: 1. Create a Session with Queue Configuration, verify
   * getAcknowledgeMode returns correct value; 2. Create a Session with Topic
   * Configuration, verify getAcknowledgeMode returns correct value;
   */
  public void ackTests() throws Fault {

    try {
      boolean pass = true;

      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      if (tool.getDefaultSession()
          .getAcknowledgeMode() != Session.AUTO_ACKNOWLEDGE) {
        pass = false;
        logErr("Error: getAcknowledgeMode failed");
      }

      try {
        tool.closeDefaultConnections();
      } catch (Exception ex) {
        logErr("Error closing Connection", ex);
      }

      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      if (tool.getDefaultSession()
          .getAcknowledgeMode() != Session.AUTO_ACKNOWLEDGE) {
        pass = false;
        logErr("Error: getAcknowledgeMode failed");
      }

      if (pass != true)
        throw new Fault("ackTests");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("ackTests");
    } finally {
      try {
        tool.closeDefaultConnections();
      } catch (Exception ex) {
        logErr("Error closing Connection", ex);
      }
    }
  }

  /*
   * @testName: InvalidDestinationTests
   *
   * @assertion_ids: JMS:SPEC:195; JMS:SPEC:196; JMS:JAVADOC:502;
   * JMS:JAVADOC:504; JMS:JAVADOC:510; JMS:JAVADOC:638; JMS:JAVADOC:639;
   * JMS:JAVADOC:641; JMS:JAVADOC:643; JMS:JAVADOC:644; JMS:JAVADOC:646;
   * JMS:JAVADOC:647; JMS:JAVADOC:649;
   * 
   * @test_Strategy: 1. Create a Session with Queue Configuration, using a null
   * Destination/Queue to verify InvalidDestinationException is thrown with
   * various methods 2. Create a Session with Topic Configuration, using a null
   * Destination/Topic to verify InvalidDestinationException is thrown with
   * various methods
   */
  public void InvalidDestinationTests() throws Fault {
    String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

    try {
      boolean pass = true;
      Destination dummy = null;
      Topic dummyT = null;
      Queue dummyQ = null;

      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultProducer().close();
      tool.getDefaultConsumer().close();

      try {
        tool.getDefaultSession().createConsumer(dummy);
        logErr(
            "Error: createConsumer(null) didn't throw expected InvalidDestinationException");
        pass = false;
      } catch (InvalidDestinationException ex) {
        logMsg(
            "Got expected InvalidDestinationException from createConsumer(null)");
      } catch (Exception e) {
        logErr("Error: createConsumer(null) throw incorrect Exception: ", e);
        pass = false;
      }

      try {
        tool.getDefaultSession().createConsumer(dummy, "TEST = 'test'");
        logErr(
            "Error: createConsumer(null, String) didn't throw expected InvalidDestinationException");
        pass = false;
      } catch (InvalidDestinationException ex) {
        logMsg(
            "Got expected InvalidDestinationException from createConsumer(null, String)");
      } catch (Exception e) {
        logErr(
            "Error: createConsumer(null, String) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        tool.getDefaultSession().createConsumer(dummy, "TEST = 'test'", true);
        logErr(
            "Error: createConsumer(null, String, boolean) didn't throw expected InvalidDestinationException");
        pass = false;
      } catch (InvalidDestinationException ex) {
        logMsg(
            "Got expected InvalidDestinationException from createConsumer(null, String, true)");
      } catch (Exception e) {
        logErr(
            "Error: createConsumer(null, String, true) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        tool.getDefaultSession().createBrowser(dummyQ);
        logErr(
            "Error: createBrowser(null) didn't throw expected InvalidDestinationException");
        pass = false;
      } catch (InvalidDestinationException ex) {
        logMsg(
            "Got expected InvalidDestinationException from createBrowser(null)");
      } catch (Exception e) {
        logErr("Error: createBrowser(null) throw incorrect Exception: ", e);
        pass = false;
      }

      try {
        tool.getDefaultSession().createBrowser(dummyQ, "TEST = 'test'");
        logErr(
            "Error: createBrowser(null, String) didn't throw expected InvalidDestinationException");
        pass = false;
      } catch (InvalidDestinationException ex) {
        logMsg(
            "Got expected InvalidDestinationException from createBrowser(null, String)");
      } catch (Exception e) {
        logErr("Error: createBrowser(null, String) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        tool.closeDefaultConnections();
      } catch (Exception ex) {
        logErr("Error closing the default Connection", ex);
      }

      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      tool.getDefaultProducer().close();
      tool.getDefaultConsumer().close();

      try {
        tool.getDefaultSession().createConsumer(dummy);
        logErr(
            "Error: createConsumer(null) didn't throw expected InvalidDestinationException");
        pass = false;
      } catch (InvalidDestinationException ex) {
        logMsg(
            "Got expected InvalidDestinationException from createConsumer(null)");
      } catch (Exception e) {
        logErr("Error: createConsumer(null) throw incorrect Exception: ", e);
        pass = false;
      }

      try {
        tool.getDefaultSession().createConsumer(dummy, "TEST = 'test'");
        logErr(
            "Error: createConsumer(null, String) didn't throw expected InvalidDestinationException");
        pass = false;
      } catch (InvalidDestinationException ex) {
        logMsg(
            "Got expected InvalidDestinationException from createConsumer(null, String)");
      } catch (Exception e) {
        logErr(
            "Error: createConsumer(null, String) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        tool.getDefaultSession().createConsumer(dummy, "TEST = 'test'", true);
        logErr(
            "Error: createConsumer(null, String, boolean) didn't throw expected InvalidDestinationException");
        pass = false;
      } catch (InvalidDestinationException ex) {
        logMsg(
            "Got expected InvalidDestinationException from createConsumer(null, String, true)");
      } catch (Exception e) {
        logErr(
            "Error: createConsumer(null, String, true) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        tool.closeDefaultConnections();
      } catch (Exception ex) {
        logErr("Error closing default Connection", ex);
      }

      Connection newConn = tool.getNewConnection(JmsTool.COMMON_T, user,
          password, lookup);
      Session newSession = newConn.createSession(false,
          Session.AUTO_ACKNOWLEDGE);

      try {
        newSession.unsubscribe("foo");
        logErr(
            "Error: unsubscribe(foo) didn't throw expected InvalidDestinationException");
        pass = false;
      } catch (InvalidDestinationException ex) {
        logMsg(
            "Got expected InvalidDestinationException from unsubscribe(foo)");
      } catch (Exception e) {
        logErr("Error: unsubscribe(foo) throw incorrect Exception: ", e);
        pass = false;
      }

      try {
        TopicSubscriber tsub = newSession.createDurableSubscriber(dummyT,
            "cts");
        logErr(
            "Error: createDurableSubscriber(null, String) didn't throw expected InvalidDestinationException");
        pass = false;
        tsub.close();
        newSession.unsubscribe("cts");
      } catch (InvalidDestinationException ex) {
        logMsg(
            "Got expected InvalidDestinationException from createDurableSubscriber(null, String)");
      } catch (Exception e) {
        logErr(
            "Error: createDurableSubscriber(null, String) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        TopicSubscriber tsub = newSession.createDurableSubscriber(dummyT, "cts",
            "TEST = 'test'", true);
        logErr(
            "Error: createDurableSubscriber(null, String, String, boolean) didn't throw expected InvalidDestinationException");
        pass = false;
        tsub.close();
        newSession.unsubscribe("cts");
      } catch (InvalidDestinationException ex) {
        logMsg(
            "Got expected InvalidDestinationException from createDurableSubscriber(null, String, String, boolean)");
      } catch (Exception e) {
        logErr(
            "Error: createDurableSubscriber(null, String, String, boolean) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        newConn.close();
      } catch (Exception ex) {
        logErr("Error closing new Connection", ex);
      }

      if (pass != true)
        throw new Fault("InvalidDestinationTests");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("InvalidDestinationTests");
    } finally {
      try {
        tool.closeDefaultConnections();
      } catch (Exception ex) {
        logErr("Error closing Connection", ex);
      }
    }
  }

  /*
   * @testName: InvalidSelectorTests
   *
   * @assertion_ids: JMS:SPEC:195; JMS:SPEC:196; JMS:SPEC:69; JMS:SPEC:175;
   * JMS:JAVADOC:640; JMS:JAVADOC:642; JMS:JAVADOC:645; JMS:JAVADOC:648;
   * 
   * @test_Strategy: 1. Create a Session with Queue Configuration, call
   * createConsumer/createBrowser with invalid selector to verify
   * InvalidSelectorException is thrown 2. Create a Session with Topic
   * Configuration, call createConsumer/createDurableSubscriber with invalid
   * selector to verify InvalidSelectorException is thrown
   */
  public void InvalidSelectorTests() throws Fault {
    String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

    try {
      boolean pass = true;

      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultProducer().close();
      tool.getDefaultConsumer().close();

      try {
        tool.getDefaultSession().createConsumer(tool.getDefaultDestination(),
            "=TEST 'test'");
        logErr(
            "Error: createConsumer(Destination, String) didn't throw expected InvalidSelectorException");
        pass = false;
      } catch (InvalidSelectorException ex) {
        logMsg(
            "Got expected InvalidSelectorException from createConsumer(Destination, String)");
      } catch (Exception e) {
        logErr(
            "Error: createConsumer(Destination, String) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        tool.getDefaultSession().createBrowser(
            (Queue) tool.getDefaultDestination(), "=TEST 'test'");
        logErr(
            "Error: createBrowser(Queue, String) didn't throw expected InvalidSelectorException");
        pass = false;
      } catch (InvalidSelectorException ex) {
        logMsg(
            "Got expected InvalidSelectorException from createBrowser(Queue, String)");
      } catch (Exception e) {
        logErr(
            "Error: createBrowser(Queue, String) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        tool.closeDefaultConnections();
      } catch (Exception ex) {
        logErr("Error closing Connection", ex);
      }

      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      tool.getDefaultProducer().close();
      tool.getDefaultConsumer().close();

      Connection newConn = tool.getNewConnection(JmsTool.COMMON_T, user,
          password, lookup);
      Session newSess = newConn.createSession(false, Session.AUTO_ACKNOWLEDGE);

      try {
        TopicSubscriber tsub = newSess.createDurableSubscriber(
            (Topic) tool.getDefaultDestination(), "mysubscription",
            "=TEST 'test'", true);
        logErr("Error: createDurableSubscriber(Topic, String, String, boolean) "
            + "didn't throw expected InvalidSelectorException");
        pass = false;
        tsub.close();
        newSess.unsubscribe("mysubscription");
      } catch (InvalidSelectorException ex) {
        logMsg("Got expected InvalidSelectorException from "
            + "createDurableSubscriber(Topic, String, String, boolean)");
      } catch (Exception e) {
        logErr("Error: createDurableSubscriber(Topic, String, String, boolean) "
            + "throw incorrect Exception: ", e);
        pass = false;
      }

      try {
        newConn.close();
      } catch (Exception ex) {
        logErr("Error closing new Connection", ex);
      }

      try {
        tool.getDefaultSession().createConsumer(tool.getDefaultDestination(),
            "=TEST 'test'", true);
        logErr(
            "Error: createConsumer(Destination, String, boolean) didn't throw expected InvalidSelectorException");
        pass = false;
      } catch (InvalidSelectorException ex) {
        logMsg(
            "Got expected InvalidSelectorException from createConsumer(Destination, String, boolean)");
      } catch (Exception e) {
        logErr(
            "Error: createConsumer(Destination, String, boolean) throw incorrect Exception: ",
            e);
        pass = false;
      }

      if (pass != true)
        throw new Fault("InvalidSelectorTests");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("InvalidSelectorTests");
    } finally {
      try {
        tool.closeDefaultConnections();
      } catch (Exception ex) {
        logErr("Error closing Connection", ex);
      }
    }
  }
}
