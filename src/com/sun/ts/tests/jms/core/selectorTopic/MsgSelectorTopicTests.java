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
package com.sun.ts.tests.jms.core.selectorTopic;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jms.common.*;
import java.io.*;
import java.util.*;
import com.sun.javatest.Status;
import javax.jms.*;

public class MsgSelectorTopicTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.selectorTopic.MsgSelectorTopicTests";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // JMS objects
  private transient JmsTool tool = null;

  private transient Message msg = null;

  private transient TopicSubscriber subscriber = null;

  private transient TemporaryTopic tempT = null;

  private transient TopicPublisher publisher = null;

  private transient TopicConnection tConnect;

  private transient TopicSession session;

  private transient TopicConnectionFactory tFactory;

  private boolean transacted = false;

  // Harness req's
  private Properties props = null;

  // properties read from ts.jte file
  private long jms_timeout;

  private String jmsUser = null;

  private String jmsPassword = null;

  private String mode = null;

  private boolean noLocal = false;

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    MsgSelectorTopicTests theTests = new MsgSelectorTopicTests();
    Status s = theTests.run(args, System.out, System.err);

    s.exit();
  }

  /* Utility methods for tests */

  /*
   * The beginning of the test creates receiver and message. This is broken
   * apart from the rest of the test so that the user can specify message
   * properties before sending the message.
   */
  private void startTest(String selector, String headerValue) throws Exception {
    logTrace("startTest(): Creating receiver with message selector");

    tempT = session.createTemporaryTopic();
    subscriber = session.createSubscriber(tempT, selector, noLocal);

    logTrace("Creating message");
    msg = session.createMessage();
    msg.setStringProperty("COM_SUN_JMS_TESTNAME", "MsgSelectorTopicTests");
    msg.setJMSType(headerValue);
  }

  /*
   * Send the message and try to receive it. Check the result against the
   * expectation.
   */
  private void finishTestReceive() throws Exception {
    logTrace("finishTestReceive(): Sending test message");
    msg.setBooleanProperty("first_message", true);
    // ii
    publisher = session.createPublisher(tempT);

    publisher.publish(msg);
    logTrace("Attempt to receive message");
    Message msg1 = subscriber.receive(jms_timeout);
    logTrace("Received message: " + msg1);

    // check result
    if (msg1 == null) {
      throw new Exception("Did not receive message!");
    } else if (msg1.getBooleanProperty("first_message") == true) {
      logTrace("test passed");
    } else {
      logMsg("Received completely unexpected message.");
      throw new Exception("Received unexpected message -- not part of test");
    }
  }

  /*
   * Send the message. Used with finishTest() to send a second message and make
   * sure that the first is not received my the message consumer.
   */
  private void sendFirstMessage() throws JMSException {
    logTrace(
        "sendFirstMessage(): Sending message that does not match selector");
    msg.setBooleanProperty("second_message", false);
    // ii
    publisher = session.createPublisher(tempT);
    publisher.publish(msg);

    msg = session.createMessage();
    msg.setStringProperty("COM_SUN_JMS_TESTNAME", "MsgSelectorTopicTests_2");
  }

  /*
   * Send the second message which does match the selector. Receive() and verify
   * that only this second message is received.
   */
  private void finishTest() throws Exception {

    logTrace("finishTest: Sending message that should match selector");
    msg.setBooleanProperty("second_message", true);
    publisher = session.createPublisher(tempT);
    publisher.publish(msg);

    logTrace("Attempt to receive message. Should receive second message only.");
    Message msg1 = subscriber.receive(jms_timeout);
    logTrace("Received message: " + msg1);

    // check result
    if (msg1 == null) {
      throw new Exception("Did not receive message!");
    } else if (msg1.getBooleanProperty("second_message") == true) {
      logTrace("test passed");
    } else if (msg1.getBooleanProperty("second_message") == false) {
      throw new Exception("Incorrectly received non-matching message!");
    }
  }

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

  /* Test setup: */

  /*
   * setup() is called before each test
   * 
   * Individual tests create a temporary Topic
   * 
   * @class.setup_props: jms_timeout; user; password; platform.mode;
   * 
   * @exception Fault
   */
  public void setup(String[] args, Properties p) throws Fault {
    try {
      props = p;
      // get props
      jms_timeout = Long.parseLong(props.getProperty("jms_timeout"));
      // check props for errors
      if (jms_timeout < 1) {
        throw new Exception("'timeout' (milliseconds) in ts.jte must be > 0");
      }
      jmsUser = props.getProperty("user");
      jmsPassword = props.getProperty("password");
      mode = p.getProperty("platform.mode");

      tool = new JmsTool(JmsTool.TOPIC_FACTORY, jmsUser, jmsPassword, mode);
      tFactory = tool.getTopicConnectionFactory();
      tConnect = tFactory.createTopicConnection(jmsUser, jmsPassword);
      tConnect.start();

      session = tConnect.createTopicSession(transacted,
          Session.AUTO_ACKNOWLEDGE);
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
      logTrace(" closing connection");
      tConnect.close();
      tempT = null;
      session.close();

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      logErr("An error occurred while cleaning");
      throw new Fault("Cleanup failed!", e);
    }
  }

  /* Tests */

  /*
   * @testName: selectorTest01
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:55; JMS:SPEC:49; JMS:SPEC:39;
   * 
   * @test_Strategy: create receiver with selector set msg header to include
   * string send message check receipt of message
   */
  public void selectorTest01() throws Fault {
    try {
      String selector = "JMSType='literal'";
      String value = "literal"; // set for JMSType automatically

      startTest(selector, value);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: identifierTest01
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:56; JMS:SPEC:49; JMS:SPEC:43;
   * 
   * @test_Strategy: create receiver with selector send message with identifiers
   * including '_' and '$' check receipt of message
   */
  public void identifierTest01() throws Fault {
    try {
      String selector = "$myProp=TRUE AND _myProp=FALSE";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("$myProp", true);
      msg.setBooleanProperty("_myProp", false);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: whitespaceTest1
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:53;
   * 
   * @test_Strategy: create receiver with selector containing extra spaces send
   * message with header set check receipt of message
   */
  public void whitespaceTest1() throws Fault {
    try {
      String selector = "JMSType   =   'foo'";
      String value = "foo"; // set for JMSType automatically

      startTest(selector, value);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: expressionTest1
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:54;
   * 
   * @test_Strategy: create receiver with selector send message with TRUE
   * boolean property check receipt of message
   */
  public void expressionTest1() throws Fault {
    try {
      String selector = "myProp";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myProp", true);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: bracketingTest1
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:57;
   * 
   * @test_Strategy: create receiver with selector that should evaluate to FALSE
   * send message check that message is not received
   */
  public void bracketingTest1() throws Fault {
    try {
      String selector = "(myTrueProp OR myFalseProp) AND myFalseProp";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myTrueProp", true);
      msg.setBooleanProperty("myFalseProp", false);
      sendFirstMessage();
      msg.setBooleanProperty("myTrueProp", true);
      msg.setBooleanProperty("myFalseProp", true);
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: comparisonTest01
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:59;
   * 
   * @test_Strategy: create receiver with selector containing operators send
   * message with properties matching selector check receipt of message
   */
  public void comparisonTest01() throws Fault {
    try {
      String selector = "myProp0 = 'foo' AND " + "myProp1 > 0 AND "
          + "myProp2 >= 2 AND " + "myProp3 >= 2 AND " + "myProp4 < 5 AND "
          + "myProp5 <= 6 AND " + "myProp6 <= 6 AND " + "myProp7 <> 7";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setStringProperty("myProp0", "foo");
      msg.setIntProperty("myProp1", 1);
      msg.setFloatProperty("myProp2", 2.0f);
      msg.setIntProperty("myProp3", 3);
      msg.setDoubleProperty("myProp4", 4.0);
      msg.setIntProperty("myProp5", 5);
      msg.setIntProperty("myProp6", 6);
      msg.setFloatProperty("myProp7", 0f);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: operatorTest1
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:62;
   * 
   * @test_Strategy: create receiver with selector containing +,- send message
   * with numeric properties check receipt of message
   */
  public void operatorTest1() throws Fault {
    try {
      String selector = "-myProp0 < 0 AND +myProp1 < 0";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setIntProperty("myProp0", 5);
      msg.setIntProperty("myProp1", -5);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: betweenTest1
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:64;
   * 
   * @test_Strategy: create receiver with selector containing BETWEEN send
   * message matching selector check receipt of message
   */
  public void betweenTest1() throws Fault {
    try {
      String selector = "myProp0 BETWEEN 5 and 10 AND "
          + "myProp1 BETWEEN -1 and 1 AND " + "myProp2 BETWEEN 0 and 2";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setIntProperty("myProp0", 7);
      msg.setIntProperty("myProp1", -1);
      msg.setIntProperty("myProp2", 2);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: inTest1
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:65;
   * 
   * @test_Strategy: create receiver with selector containing IN send message
   * matching selector check receipt of message
   */
  public void inTest1() throws Fault {
    try {
      String selector = "JMSType IN ('foo','jms','test')";
      String value = "jms"; // set for JMSType automatically

      startTest(selector, value);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: likeTest01
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:66;
   * 
   * @test_Strategy: create receiver with message selector containing LIKE send
   * message matching selector check receipt of message
   */
  public void likeTest01() throws Fault {
    try {
      String selector = "JMSType LIKE 'jms'";
      String value = "jms"; // set for JMSType automatically

      startTest(selector, value);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: isNullTest1
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:67;
   * 
   * @test_Strategy: create receiver with message selector containing IS NULL
   * selector references unknown property send message check receipt of message
   */
  public void isNullTest1() throws Fault {
    try {
      String selector = "myNullProp IS NULL";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: caseTest1
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:247;
   * 
   * @test_Strategy: create receiver with message selector selector contains
   * lower case versions of selector operators send message matching selector
   * check receipt of message
   */
  public void caseTest1() throws Fault {
    try {
      String selector = "myProp0 is null and "
          + "myProp1 like 'fooG_%' escape 'G' and "
          + "myProp2 in ('a', 'b') and " + "myProp3 not between 0 and 10 and "
          + "(myProp4 or false)";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setStringProperty("myProp1", "foo_test");
      msg.setStringProperty("myProp2", "a");
      msg.setIntProperty("myProp3", 20);
      msg.setBooleanProperty("myProp4", true);
      finishTestReceive();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: precedenceTest1
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:58;
   * 
   * @test_Strategy: create receiver with message selector containing AND and OR
   * send message not matching selector check that message is not received !F&F
   * = (!F)&F = T&F = F incorrect order would be !F&F -> !(F&F) = !F = T
   */
  public void precedenceTest1() throws Fault {
    try {
      String selector = "NOT myTrueProp AND myFalseProp";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myTrueProp", true);
      msg.setBooleanProperty("myFalseProp", false);
      sendFirstMessage();
      msg.setBooleanProperty("myTrueProp", false);
      msg.setBooleanProperty("myFalseProp", true);
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: nullTest01
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:49; JMS:SPEC:60;
   * 
   * @test_Strategy: create receiver with selector referencing unknown property
   * send message check that message is not received
   */
  public void nullTest01() throws Fault {
    try {
      String selector = "myProp + 2 < 10";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      sendFirstMessage();
      msg.setIntProperty("myProp", 0);
      finishTest();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("test failed");
    }
  }

  /*
   * @testName: nullTest11
   *
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:37;
   *
   * @test_Strategy: create receiver with null selector. check receipt of
   * message
   */
  public void nullTest11() throws Fault {
    try {
      String selector = null;
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myNullProp", false);
      finishTest();
    } catch (Exception e) {
      throw new Fault("test failed", e);
    }
  }

  /*
   * @testName: emptyTest
   *
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:37;
   *
   * @test_Strategy: create receiver with empty selector. check receipt of
   * message
   */
  public void emptyTest() throws Fault {
    try {
      String selector = "";
      String value = ""; // set for JMSType automatically

      startTest(selector, value);
      msg.setBooleanProperty("myEmptyProp", false);
      finishTest();
    } catch (Exception e) {
      throw new Fault("test failed", e);
    }
  }

  /*
   * @testName: durableTopicEmptyStringSelTest
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:37;
   * 
   * @test_Strategy: The javadoc documents the messageSelector parameter as
   * follows: A value of null or an empty string indicates that there is no
   * message selector for the message consumer.
   *
   * Test Case: Test with messageSelector = empty string.
   * 
   * 1) Lookup durable connection factory (clientid set), topic, and create a
   * durable subscription with an empty string message selector for that topic
   * then close the connection. 2) Lookup non durable connection factory (no
   * clientid set), topic, send a TextMessage, then close the connection. 3)
   * Lookup durable connection factory (clientid set), topic and reactivate the
   * durable subscription created in step 1 above but this time not specifying a
   * message selector. Try and receive the message that was sent in step 2. The
   * message should be received. If message received then test passes. If no
   * message recevied then test failed.
   *
   */
  public void durableTopicEmptyStringSelTest() throws Fault {
    String lookupDurable = "DURABLE_SUB_CONNECTION_FACTORY";
    String lookupNonDurable = "MyTopicConnectionFactory";
    String msgSelectorEmptyString = "";
    TopicSubscriber durableTS = null;
    boolean noLocal = false;
    boolean pass = true;

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;

      // set up test tool for Topic
      TestUtil.logMsg("TEST CASE [message selector=empty string] test case");
      TestUtil.logMsg("BEGIN STEP1");
      TestUtil.logMsg(
          "Setup JmsTool for TOPIC using connection factory with ClientID set");
      tool = new JmsTool(JmsTool.DURABLE_TOPIC, jmsUser, jmsPassword,
          lookupDurable, mode);

      // create durable subscription with empty string message selector
      TestUtil
          .logMsg("Create durable subscription subname=mySubTestEmptyString,"
              + " msgSelector=\"\" and noLocal=false");
      tool.getDefaultTopicSubscriber().close();
      durableTS = tool.getDefaultTopicSession().createDurableSubscriber(
          tool.getDefaultTopic(), "mySubTestEmptyString",
          msgSelectorEmptyString, noLocal);
      TestUtil.logMsg("Close connection and resources");
      durableTS.close();
      tool.closeAllResources();
      tool.getDefaultTopicConnection().close();
      TestUtil.logMsg("END STEP1");

      // set up test tool for Topic
      TestUtil.logMsg("BEGIN STEP2");
      TestUtil.logMsg(
          "Setup JmsTool for TOPIC using connection factory with no ClientID set");
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookupNonDurable,
          mode);
      tool.getDefaultTopicConnection().start();

      TestUtil.logMsg("Create a message and publish the message");
      messageSent = tool.getDefaultTopicSession()
          .createTextMessage("Hello There!");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "durableTopicEmptyStringSelTest");
      tool.getDefaultTopicPublisher().publish(messageSent);

      TestUtil.logMsg("Close connection and resources");
      tool.closeAllResources();
      tool.getDefaultTopicConnection().close();
      TestUtil.logMsg("END STEP2");

      // set up test tool for Topic
      TestUtil.logMsg("BEGIN STEP3");
      TestUtil.logMsg(
          "Setup JmsTool for TOPIC using connection factory with ClientID set");
      tool = new JmsTool(JmsTool.DURABLE_TOPIC, jmsUser, jmsPassword,
          lookupDurable, mode);

      // Reactivate durable subscription created in step 1
      TestUtil.logMsg(
          "Reactivate durable subscription subname=mySubTestEmptyString in STEP1");
      tool.getDefaultTopicSubscriber().close();
      durableTS = tool.getDefaultTopicSession().createDurableSubscriber(
          tool.getDefaultTopic(), "mySubTestEmptyString");
      tool.getDefaultTopicConnection().start();

      TestUtil.logMsg("Receive the message that was sent in STEP2");
      messageReceived = (TextMessage) durableTS.receive(jms_timeout);
      if (messageReceived == null) {
        TestUtil.logErr("didn't get the message");
        TestUtil.logErr("TEST CASE [message selector=empty string] failed");
        pass = false;
      } else if (messageReceived.getText().equals(messageSent.getText())) {
        TestUtil.logMsg("Received correct message: text=["
            + messageReceived.getText() + "]");
        TestUtil.logMsg("TEST CASE [message selector=empty string] passed");
      } else {
        TestUtil.logErr("Received incorrect message: text=["
            + messageReceived.getText() + "] expected=[Hello There!]");
        TestUtil.logErr("didn't get the right message");
        TestUtil.logErr("TEST CASE [message selector=empty string] failed");
        pass = false;
      }
      TestUtil.logMsg("END STEP3");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("durableTopicEmptyStringSelTest failed");
    } finally {
      try {
        cleanupSubscription(durableTS, tool.getDefaultTopicSession(),
            "mySubTestEmptyString");
        tool.closeAllResources();
        tool.getDefaultTopicConnection().close();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        throw new Fault("durableTopicEmptyStringSelTest during cleanup");
      }
    }

    if (!pass)
      throw new Fault("durableTopicEmptyStringSelTest failed");
  }

  /*
   * @testName: durableTopicNullSelTest
   * 
   * @assertion_ids: JMS:SPEC:36; JMS:SPEC:37;
   * 
   * @test_Strategy: The javadoc documents the messageSelector parameter as
   * follows: A value of null or an empty string indicates that there is no
   * message selector for the message consumer.
   *
   * Test Case: Test with messageSelector = null.
   * 
   * 1) Lookup durable connection factory (clientid set), topic, and create a
   * durable subscription with an empty string message selector for that topic
   * then close the connection. 2) Lookup non durable connection factory (no
   * clientid set), topic, send a TextMessage, then close the connection. 3)
   * Lookup durable connection factory (clientid set), topic and reactivate the
   * durable subscription created in step 1 above but this time not specifying a
   * message selector. Try and receive the message that was sent in step 2. The
   * message should be received. If message received then test passes. If no
   * message recevied then test failed.
   *
   */
  public void durableTopicNullSelTest() throws Fault {
    String lookupDurable = "DURABLE_SUB_CONNECTION_FACTORY";
    String lookupNonDurable = "MyTopicConnectionFactory";
    String msgSelectorNull = null;
    TopicSubscriber durableTS = null;
    boolean noLocal = false;
    boolean pass = true;

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;

      // set up test tool for Topic
      TestUtil.logMsg("TEST CASE [message selector=null] test case");
      TestUtil.logMsg("BEGIN STEP1");
      TestUtil.logMsg(
          "Setup JmsTool for TOPIC using connection factory with ClientID set");
      tool = new JmsTool(JmsTool.DURABLE_TOPIC, jmsUser, jmsPassword,
          lookupDurable, mode);

      // create durable subscription with null message selector
      TestUtil.logMsg("Create durable subscription subname=mySubTestNull,"
          + " msgSelector=null and noLocal=false");
      tool.getDefaultTopicSubscriber().close();
      durableTS = tool.getDefaultTopicSession().createDurableSubscriber(
          tool.getDefaultTopic(), "mySubTestNull", msgSelectorNull, noLocal);
      TestUtil.logMsg("Close connection and resources");
      durableTS.close();
      tool.closeAllResources();
      tool.getDefaultTopicConnection().close();
      TestUtil.logMsg("END STEP1");

      // set up test tool for Topic
      TestUtil.logMsg("BEGIN STEP2");
      TestUtil.logMsg(
          "Setup JmsTool for TOPIC using connection factory with no ClientID set");
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookupNonDurable,
          mode);
      tool.getDefaultTopicConnection().start();

      TestUtil.logMsg("Create a message and publish the message");
      messageSent = tool.getDefaultTopicSession()
          .createTextMessage("Hello There!");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "durableTopicNullSelTest");
      tool.getDefaultTopicPublisher().publish(messageSent);

      TestUtil.logMsg("Close connection and resources");
      tool.closeAllResources();
      tool.getDefaultTopicConnection().close();
      TestUtil.logMsg("END STEP2");

      // set up test tool for Topic
      TestUtil.logMsg("BEGIN STEP3");
      TestUtil.logMsg(
          "Setup JmsTool for TOPIC using connection factory with ClientID set");
      tool = new JmsTool(JmsTool.DURABLE_TOPIC, jmsUser, jmsPassword,
          lookupDurable, mode);

      // Reactivate durable subscription created in step 1
      TestUtil.logMsg(
          "Reactivate durable subscription subname=mySubTestNull in STEP1");
      tool.getDefaultTopicSubscriber().close();
      durableTS = tool.getDefaultTopicSession()
          .createDurableSubscriber(tool.getDefaultTopic(), "mySubTestNull");
      tool.getDefaultTopicConnection().start();

      TestUtil.logMsg("Receive the message that was sent in STEP2");
      messageReceived = (TextMessage) durableTS.receive(jms_timeout);
      if (messageReceived == null) {
        TestUtil.logErr("didn't get the message");
        TestUtil.logErr("TEST CASE [message selector=null] failed");
        pass = false;
      } else if (messageReceived.getText().equals(messageSent.getText())) {
        TestUtil.logMsg("Received correct message: text=["
            + messageReceived.getText() + "]");
        TestUtil.logMsg("TEST CASE [message selector=null] passed");
      } else {
        TestUtil.logErr("Received incorrect message: text=["
            + messageReceived.getText() + "] expected=[Hello There!]");
        TestUtil.logErr("didn't get the right message");
        TestUtil.logErr("TEST CASE [message selector=null] failed");
        pass = false;
      }
      TestUtil.logMsg("END STEP3");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("durableTopicNullSelTest failed");
    } finally {
      try {
        cleanupSubscription(durableTS, tool.getDefaultTopicSession(),
            "mySubTestNull");
        tool.closeAllResources();
        tool.getDefaultTopicConnection().close();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        throw new Fault("durableTopicNullSelTest during cleanup");
      }
    }

    if (!pass)
      throw new Fault("durableTopicNullSelTest failed");
  }

}// The End......
