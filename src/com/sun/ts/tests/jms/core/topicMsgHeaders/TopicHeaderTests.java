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
package com.sun.ts.tests.jms.core.topicMsgHeaders;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.*;
import com.sun.javatest.Status;

public class TopicHeaderTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.topicMsgHeaders.TopicHeaderTests";

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

  ArrayList connections = null;

  /* Run test in standalone mode */

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    TopicHeaderTests theTests = new TopicHeaderTests();
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
      logMsg("Didn't get expected exception");
      throw new Exception("Didn't catch expected exception");
    }
  }

  /* Test setup: */

  /*
   * setup() is called before each test
   * 
   * Creates Administrator object and deletes all previous Destinations.
   * Individual tests create the TestTools object with one default Queue and/or
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
        throw new Exception("'user' in ts.jte must be null");
      }
      if (password == null) {
        throw new Exception("'password' in ts.jte must be null");
      }
      if (mode == null) {
        throw new Exception("'mode' in ts.jte must be null");
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
    try {
      if (tool != null) {
        logMsg("Cleanup: Closing Queue and Topic Connections");
        tool.closeAllConnections(connections);
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      logErr("An error occurred while cleaning");
      throw new Fault("Cleanup failed!", e);
    }
  }

  /* Tests */

  /*
   * helper method for msgHdrIDQTest and msgHdrIDTopicTest verifies that the
   * JMSMessageID starts with ID:
   * 
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

  /*
   * @testName: msgHdrIDTopicTest
   * 
   * @assertion_ids: JMS:SPEC:4; JMS:JAVADOC:343;
   * 
   * @test_Strategy: With a topic destination Send and receive single Text, map,
   * bytes, stream, and object message call getJMSMessageID and verify that it
   * starts with ID:
   */

  public void msgHdrIDTopicTest() throws Fault {
    boolean pass = true;
    byte bValue = 127;
    String id = null;

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      StreamMessage messageSentStreamMessage = null;
      StreamMessage messageReceivedStreamMessage = null;
      BytesMessage messageSentBytesMessage = null;
      BytesMessage messageReceivedBytesMessage = null;
      MapMessage messageReceivedMapMessage = null;
      MapMessage messageSentMapMessage = null;
      ObjectMessage messageSentObjectMsg = null;
      ObjectMessage messageReceivedObjectMsg = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();

      // send and receive Object message to Topic
      logTrace("Send ObjectMessage to Topic.");
      messageSentObjectMsg = tool.getDefaultTopicSession()
          .createObjectMessage();
      messageSentObjectMsg.setObject("msgHdrIDTopicTest for Object Message");
      messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrIDToopicTest");
      tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
      messageReceivedObjectMsg = (ObjectMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logTrace("getJMSMessageID ");
      logTrace(" " + messageReceivedObjectMsg.getJMSMessageID());
      id = messageReceivedObjectMsg.getJMSMessageID();
      if (!chkMessageID(id)) {
        logMsg("ObjectMessage error: JMSMessageID does not start with ID:");
        pass = false;
      }

      // send and receive map message to Topic
      logTrace("Send MapMessage to Topic.");
      messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
      messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrIDTopicTest");
      messageSentMapMessage.setString("aString", "value");
      tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
      messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logTrace("getJMSMessageID ");
      logTrace(" " + messageReceivedMapMessage.getJMSMessageID());
      id = messageReceivedMapMessage.getJMSMessageID();
      if (!chkMessageID(id)) {
        logMsg("MapMessage error: JMSMessageID does not start with ID:");
        pass = false;
      }

      // send and receive bytes message to Topic
      logTrace("Send BytesMessage to Topic.");
      messageSentBytesMessage = tool.getDefaultTopicSession()
          .createBytesMessage();
      messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrIDTopicTest");
      messageSentBytesMessage.writeByte(bValue);
      tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
      messageReceivedBytesMessage = (BytesMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logTrace("getJMSMessageID ");
      logTrace(" " + messageReceivedBytesMessage.getJMSMessageID());
      id = messageReceivedBytesMessage.getJMSMessageID();
      if (!chkMessageID(id)) {
        logMsg("BytesMessage error: JMSMessageID does not start with ID:");
        pass = false;
      }

      // Send and receive a StreamMessage
      logTrace("sending a Stream message");
      messageSentStreamMessage = tool.getDefaultTopicSession()
          .createStreamMessage();
      messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrIDTopicTest");
      messageSentStreamMessage.writeString("Testing...");
      logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
      messageReceivedStreamMessage = (StreamMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logTrace("getJMSMessageID ");
      logTrace(" " + messageReceivedStreamMessage.getJMSMessageID());
      id = messageReceivedStreamMessage.getJMSMessageID();
      if (!chkMessageID(id)) {
        logMsg("StreamMessage error: JMSMessageID does not start with ID:");
        pass = false;
      }

      // Text Message
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText("sending a Text message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrIDTopicTest");
      logTrace("sending a Text message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      messageReceived = (TextMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logTrace("getJMSMessageID ");
      logTrace(" " + messageReceived.getJMSMessageID());
      id = messageReceived.getJMSMessageID();
      if (!chkMessageID(id)) {
        logMsg("TextMessage error: JMSMessageID does not start with ID:");
        pass = false;
      }
      if (!pass) {
        throw new Fault(
            "Error: invalid JMSMessageID returned from JMSMessageID");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("msgHdrIDTopicTest");
    }
  }

  /*
   * @testName: msgHdrTimeStampTopicTest
   * 
   * @assertion_ids: JMS:SPEC:7; JMS:JAVADOC:347;
   * 
   * @test_Strategy: With a Topic destination Send a single Text, map, bytes,
   * stream, and object message check time of send against time send returns
   * JMSTimeStamp should be between these two
   */

  public void msgHdrTimeStampTopicTest() throws Fault {
    boolean pass = true;
    long timeBeforeSend;
    long timeAfterSend;
    byte bValue = 127;
    String id = null;

    try {
      TextMessage messageSent = null;
      StreamMessage messageSentStreamMessage = null;
      BytesMessage messageSentBytesMessage = null;
      MapMessage messageSentMapMessage = null;
      ObjectMessage messageSentObjectMsg = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();

      // send an Object message to Topic
      logTrace("Send ObjectMessage to TOPIC.");
      messageSentObjectMsg = tool.getDefaultTopicSession()
          .createObjectMessage();
      messageSentObjectMsg
          .setObject("msgHdrTimeStampTopicTest for Object Message");
      messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrTimeStampTopicTest");

      // get the current time in milliseconds - before and after the send
      timeBeforeSend = System.currentTimeMillis();
      tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);

      // message has been sent
      timeAfterSend = System.currentTimeMillis();
      logTrace(" getJMSTimestamp");
      logTrace(" " + messageSentObjectMsg.getJMSTimestamp());
      logTrace("Time at send is: " + timeBeforeSend);
      logTrace("Time after return fromsend is:" + timeAfterSend);
      if ((timeBeforeSend <= messageSentObjectMsg.getJMSTimestamp())
          && (timeAfterSend >= messageSentObjectMsg.getJMSTimestamp())) {
        logTrace("Object Message TimeStamp pass");
      } else {
        logMsg("Error: invalid timestamp from ObjectMessage");
        pass = false;
      }

      // send map message to Topic
      logTrace("Send MapMessage to Topic.");
      messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
      messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrTimeStampTopicTest");
      messageSentMapMessage.setString("aString", "value");

      // get the current time in milliseconds - before and after the send
      timeBeforeSend = System.currentTimeMillis();
      tool.getDefaultTopicPublisher().publish(messageSentMapMessage);

      // message has been sent
      timeAfterSend = System.currentTimeMillis();
      logTrace(" getJMSTimestamp");
      logTrace(" " + messageSentMapMessage.getJMSTimestamp());
      logTrace("Time at send is: " + timeBeforeSend);
      logTrace("Time after return fromsend is:" + timeAfterSend);
      if ((timeBeforeSend <= messageSentMapMessage.getJMSTimestamp())
          && (timeAfterSend >= messageSentMapMessage.getJMSTimestamp())) {
        logTrace("MapMessage TimeStamp pass");
      } else {
        logMsg("Error: invalid timestamp from MapMessage");
        pass = false;
      }

      // send and receive bytes message to Topic
      logTrace("Send BytesMessage to Topic.");
      messageSentBytesMessage = tool.getDefaultTopicSession()
          .createBytesMessage();
      messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrTimeStampTopicTest");
      messageSentBytesMessage.writeByte(bValue);

      // get the current time in milliseconds - before and after the send
      timeBeforeSend = System.currentTimeMillis();
      tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);

      // message has been sent
      timeAfterSend = System.currentTimeMillis();
      logTrace(" getJMSTimestamp");
      logTrace(" " + messageSentBytesMessage.getJMSTimestamp());
      logTrace("Time at send is: " + timeBeforeSend);
      logTrace("Time after return fromsend is:" + timeAfterSend);
      if ((timeBeforeSend <= messageSentBytesMessage.getJMSTimestamp())
          && (timeAfterSend >= messageSentBytesMessage.getJMSTimestamp())) {
        logTrace("BytesMessage TimeStamp pass");
      } else {
        logMsg("Error: invalid timestamp from BytesMessage");
        pass = false;
      }

      // Send and receive a StreamMessage
      logTrace("sending a Stream message");
      messageSentStreamMessage = tool.getDefaultTopicSession()
          .createStreamMessage();
      messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrTimeStampTopicTest");
      messageSentStreamMessage.writeString("Testing...");
      logTrace("Sending message");

      // get the current time in milliseconds - before and after the send
      timeBeforeSend = System.currentTimeMillis();
      tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);

      // message has been sent
      timeAfterSend = System.currentTimeMillis();
      logTrace(" getJMSTimestamp");
      logTrace(" " + messageSentStreamMessage.getJMSTimestamp());
      logTrace("Time at send is: " + timeBeforeSend);
      logTrace("Time after return fromsend is:" + timeAfterSend);
      if ((timeBeforeSend <= messageSentStreamMessage.getJMSTimestamp())
          && (timeAfterSend >= messageSentStreamMessage.getJMSTimestamp())) {
        logTrace("StreamMessage TimeStamp pass");
      } else {
        logMsg("Error: invalid timestamp from StreamMessage");
        pass = false;
      }

      // Text Message
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText("sending a Text message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrTimeStampTopicTest");
      logTrace("sending a Text message");

      // get the current time in milliseconds - before and after the send
      timeBeforeSend = System.currentTimeMillis();
      tool.getDefaultTopicPublisher().publish(messageSent);

      // message has been sent
      timeAfterSend = System.currentTimeMillis();
      logTrace(" getJMSTimestamp");
      logTrace(" " + messageSent.getJMSTimestamp());
      logTrace("Time at send is: " + timeBeforeSend);
      logTrace("Time after return fromsend is:" + timeAfterSend);
      if ((timeBeforeSend <= messageSent.getJMSTimestamp())
          && (timeAfterSend >= messageSent.getJMSTimestamp())) {
        logTrace("TextMessage TimeStamp pass");
      } else {
        logMsg("Error: invalid timestamp from TextMessage");
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: invalid TimeStamp returned from JMSTimeStamp");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("msgHdrTimeStampTopicTest");
    }
  }

  /*
   * @testName: msgHdrCorlIdTopicTest
   * 
   * @assertion_ids: JMS:SPEC:246.7; JMS:JAVADOC:355; JMS:JAVADOC:357;
   * 
   * @test_Strategy: Send a message to a Topic with CorrelationID set. Receive
   * msg and verify the correlationid is as set by client
   * 
   */

  public void msgHdrCorlIdTopicTest() throws Fault {
    boolean pass = true;
    byte bValue = 127;
    String jmsCorrelationID = "test Correlation id";

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      StreamMessage messageSentStreamMessage = null;
      StreamMessage messageReceivedStreamMessage = null;
      BytesMessage messageSentBytesMessage = null;
      BytesMessage messageReceivedBytesMessage = null;
      MapMessage messageReceivedMapMessage = null;
      MapMessage messageSentMapMessage = null;
      ObjectMessage messageSentObjectMsg = null;
      ObjectMessage messageReceivedObjectMsg = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText("sending a message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrCorlIdTopicTest");
      logTrace("Sending Text message to Topic ");
      messageSent.setJMSCorrelationID(jmsCorrelationID);
      tool.getDefaultTopicPublisher().publish(messageSent);
      messageReceived = (TextMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logTrace("jmsCorrelationID:  " + messageReceived.getJMSCorrelationID());
      if (messageReceived.getJMSCorrelationID() == null) {
        pass = false;
        logMsg("Text Message Error: JMSCorrelationID returned a  null");
      } else if (messageReceived.getJMSCorrelationID()
          .equals(jmsCorrelationID)) {
        logTrace("pass");
      } else {
        pass = false;
        logMsg("Text Message Error: JMSCorrelationID is incorrect");
      }

      // send and receive Object message to Topic
      logTrace("Send ObjectMessage to Topic.");
      messageSentObjectMsg = tool.getDefaultTopicSession()
          .createObjectMessage();
      messageSentObjectMsg
          .setObject("msgHdrCorlIdTopicTest for Object Message");
      messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrCorlIdTopicTest");
      messageSentObjectMsg.setJMSCorrelationID(jmsCorrelationID);
      tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
      messageReceivedObjectMsg = (ObjectMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logTrace("jmsCorrelationID:  "
          + messageReceivedObjectMsg.getJMSCorrelationID());
      if (messageReceivedObjectMsg.getJMSCorrelationID() == null) {
        pass = false;
        logMsg("Object Message Error: JMSCorrelationID returned a  null");
      } else if (messageReceivedObjectMsg.getJMSCorrelationID()
          .equals(jmsCorrelationID)) {
        logTrace("pass");
      } else {
        pass = false;
        logMsg("Object Message Error: JMSCorrelationID is incorrect");
      }

      // send and receive map message to Topic
      logTrace("Send MapMessage to Topic.");
      messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
      messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrCorlIdTopicTest");
      messageSentMapMessage.setJMSCorrelationID(jmsCorrelationID);
      messageSentMapMessage.setString("aString", "value");
      tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
      messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logTrace("jmsCorrelationID:  "
          + messageReceivedMapMessage.getJMSCorrelationID());
      if (messageReceivedMapMessage.getJMSCorrelationID() == null) {
        pass = false;
        logMsg("Map Message Error: JMSCorrelationID returned a  null");
      } else if (messageReceivedMapMessage.getJMSCorrelationID()
          .equals(jmsCorrelationID)) {
        logTrace("pass");
      } else {
        pass = false;
        logMsg("Map Message Error: JMSCorrelationID is incorrect");
      }

      // send and receive bytes message to Topic
      logTrace("Send BytesMessage to Topic.");
      messageSentBytesMessage = tool.getDefaultTopicSession()
          .createBytesMessage();
      messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrCorlIdTopicTest");
      messageSentBytesMessage.setJMSCorrelationID(jmsCorrelationID);
      messageSentBytesMessage.writeByte(bValue);
      tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
      messageReceivedBytesMessage = (BytesMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logTrace("jmsCorrelationID:  "
          + messageReceivedBytesMessage.getJMSCorrelationID());
      if (messageReceivedBytesMessage.getJMSCorrelationID() == null) {
        pass = false;
        logMsg("Bytes Message Error: JMSCorrelationID returned a  null");
      } else if (messageReceivedBytesMessage.getJMSCorrelationID()
          .equals(jmsCorrelationID)) {
        logTrace("pass");
      } else {
        pass = false;
        logMsg("Byte Message Error: JMSCorrelationID is incorrect");
      }

      // Send and receive a StreamMessage
      logTrace("sending a Stream message");
      messageSentStreamMessage = tool.getDefaultTopicSession()
          .createStreamMessage();
      messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrCorlIdTopicTest");
      messageSentStreamMessage.setJMSCorrelationID(jmsCorrelationID);
      messageSentStreamMessage.writeString("Testing...");
      logTrace("Sending Stream message");
      tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
      messageReceivedStreamMessage = (StreamMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logTrace("jmsCorrelationID:  "
          + messageReceivedStreamMessage.getJMSCorrelationID());
      if (messageReceivedStreamMessage.getJMSCorrelationID() == null) {
        pass = false;
        logMsg("Stream Message Error: JMSCorrelationID returned a  null");
      } else if (messageReceivedStreamMessage.getJMSCorrelationID()
          .equals(jmsCorrelationID)) {
        logTrace("pass");
      } else {
        pass = false;
        logMsg("Stream Message Error: JMSCorrelationID is incorrect");
      }
      if (!pass) {
        throw new Fault(
            "Error: invalid JMSCorrelationID returned from JMS Header");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("msgHdrCorlIdTopicTest");
    }
  }

  /*
   * @testName: msgHdrReplyToTopicTest
   * 
   * @assertion_ids: JMS:SPEC:12; JMS:JAVADOC:359; JMS:JAVADOC:361;
   * JMS:JAVADOC:117; JMS:SPEC:246.8; JMS:JAVADOC:289; JMS:JAVADOC:562;
   * JMS:JAVADOC:166;
   * 
   * @test_Strategy: Send a message to a Topic with ReplyTo set to null and then
   * set to a destination test with Text, map, object, byte, and stream messages
   * verify on receive.
   * 
   */

  public void msgHdrReplyToTopicTest() throws Fault {
    boolean pass = true;
    Topic replyTopic = null;
    byte bValue = 127;

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      StreamMessage messageSentStreamMessage = null;
      StreamMessage messageReceivedStreamMessage = null;
      BytesMessage messageSentBytesMessage = null;
      BytesMessage messageReceivedBytesMessage = null;
      MapMessage messageReceivedMapMessage = null;
      MapMessage messageSentMapMessage = null;
      ObjectMessage messageSentObjectMsg = null;
      ObjectMessage messageReceivedObjectMsg = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText("sending a message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrReplyToTopicTest");
      logTrace("Send Text message");

      // messageSent.setJMSReplyTo(tool.getDefaultTopic());
      tool.getDefaultTopicPublisher().publish(messageSent);
      messageReceived = (TextMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      if (messageReceived.getJMSReplyTo() == null) {
        logTrace(" as expected replyto field is null");
      } else {
        logMsg(
            "ERROR: expected replyto field should have been null for this case");
        pass = false;
      }
      logTrace("Set ReplyTo and resend msg");
      messageSent.setJMSReplyTo(tool.getDefaultTopic());
      tool.getDefaultTopicPublisher().publish(messageSent);
      messageReceived = (TextMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      replyTopic = (Topic) messageReceived.getJMSReplyTo();
      logTrace("Topic name is " + replyTopic.getTopicName());
      if (replyTopic.getTopicName()
          .equals(tool.getDefaultTopic().getTopicName())) {
        logTrace("Pass ");
      } else {
        logMsg("Text Message Failed");
        pass = false;
      }

      // send and receive Object message to Topic
      logTrace("Send ObjectMessage to Topic.");
      messageSentObjectMsg = tool.getDefaultTopicSession()
          .createObjectMessage();
      messageSentObjectMsg
          .setObject("msgHdrReplyToTopicTest for Object Message");
      messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrReplyToTopicTest");
      tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
      messageReceivedObjectMsg = (ObjectMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      if (messageReceivedObjectMsg.getJMSReplyTo() == null) {
        logTrace(" as expected replyto field is null");
      } else {
        logMsg("ERROR: expected replyto field to be null in this case");
        pass = false;
      }
      logTrace("Set ReplyTo and resend msg");
      messageSentObjectMsg.setJMSReplyTo(tool.getDefaultTopic());
      tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
      messageReceivedObjectMsg = (ObjectMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      replyTopic = (Topic) messageReceivedObjectMsg.getJMSReplyTo();
      logTrace("Topic name is " + replyTopic.getTopicName());
      if (replyTopic.getTopicName()
          .equals(tool.getDefaultTopic().getTopicName())) {
        logTrace("Pass ");
      } else {
        logMsg("Object Message ReplyTo Failed");
        pass = false;
      }

      // send and receive map message to Topic
      logTrace("Send MapMessage to Topic.");
      messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
      messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrReplyToTopicTest");
      messageSentMapMessage.setString("aString", "value");
      tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
      messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      if (messageReceivedMapMessage.getJMSReplyTo() == null) {
        logTrace(" as expected replyto field is null");
      } else {
        logMsg("ERROR: expected replyto field to be null in this case");
        pass = false;
      }
      logTrace("Set ReplyTo and resend msg");
      messageSentMapMessage.setJMSReplyTo(tool.getDefaultTopic());
      tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
      messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logTrace("Received Map message ");
      replyTopic = (Topic) messageReceivedMapMessage.getJMSReplyTo();
      logTrace("Topic name is " + replyTopic.getTopicName());
      if (replyTopic.getTopicName()
          .equals(tool.getDefaultTopic().getTopicName())) {
        logTrace("Pass ");
      } else {
        logMsg("Map Message ReplyTo Failed");
        pass = false;
      }

      // send and receive bytes message to Topic
      logTrace("Send BytesMessage to Topic.");
      messageSentBytesMessage = tool.getDefaultTopicSession()
          .createBytesMessage();
      messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrReplyToTopicTest");
      messageSentBytesMessage.writeByte(bValue);
      tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
      messageReceivedBytesMessage = (BytesMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      if (messageReceivedBytesMessage.getJMSReplyTo() == null) {
        logTrace(" as expected replyto field is null");
      } else {
        logMsg("ERROR: expected replyto field to be null in this case");
        pass = false;
      }
      logTrace("Set ReplyTo and resend msg");
      messageSentBytesMessage.setJMSReplyTo(tool.getDefaultTopic());
      tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
      messageReceivedBytesMessage = (BytesMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logTrace("Received Bytes message ");
      replyTopic = (Topic) messageReceivedBytesMessage.getJMSReplyTo();
      logTrace("Topic name is " + replyTopic.getTopicName());
      if (replyTopic.getTopicName()
          .equals(tool.getDefaultTopic().getTopicName())) {
        logTrace("Pass ");
      } else {
        logMsg("Bytes Message ReplyTo Failed");
        pass = false;
      }

      // Send and receive a StreamMessage
      logTrace("sending a Stream message");
      messageSentStreamMessage = tool.getDefaultTopicSession()
          .createStreamMessage();
      messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrReplyToTopicTest");
      messageSentStreamMessage.writeString("Testing...");
      logTrace("Sending Stream message");
      tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
      messageReceivedStreamMessage = (StreamMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      if (messageReceivedStreamMessage.getJMSReplyTo() == null) {
        logTrace(" as expected replyto field is null");
      } else {
        logMsg("ERROR: expected replyto field to be null in this case");
        pass = false;
      }
      logTrace("Set ReplyTo and resend msg");
      messageSentStreamMessage.setJMSReplyTo(tool.getDefaultTopic());
      tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
      messageReceivedStreamMessage = (StreamMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logTrace("Received Stream message ");
      replyTopic = (Topic) messageReceivedStreamMessage.getJMSReplyTo();
      logTrace("Topic name is " + replyTopic.getTopicName());
      if (replyTopic.getTopicName()
          .equals(tool.getDefaultTopic().getTopicName())) {
        logTrace("Pass ");
      } else {
        logMsg("Stream Message ReplyTo Failed");
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: invalid Replyto returned from JMS Header");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("msgHdrReplyToTopicTest");
    }
  }

  /*
   * @testName: msgHdrJMSTypeTopicTest
   * 
   * @assertion_ids: JMS:SPEC:246.9; JMS:JAVADOC:375; JMS:JAVADOC:377;
   * 
   * @test_Strategy: Send a message to a Topic with JMSType set to TESTMSG test
   * with Text, map, object, byte, and stream messages verify on receive.
   * 
   */

  public void msgHdrJMSTypeTopicTest() throws Fault {
    boolean pass = true;
    byte bValue = 127;
    String type = "TESTMSG";

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      StreamMessage messageSentStreamMessage = null;
      StreamMessage messageReceivedStreamMessage = null;
      BytesMessage messageSentBytesMessage = null;
      BytesMessage messageReceivedBytesMessage = null;
      MapMessage messageReceivedMapMessage = null;
      MapMessage messageSentMapMessage = null;
      ObjectMessage messageSentObjectMsg = null;
      ObjectMessage messageReceivedObjectMsg = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText("sending a message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSTypeTopicTest");
      logTrace("JMSType test - Send a Text message");
      messageSent.setJMSType(type);
      tool.getDefaultTopicPublisher().publish(messageSent);
      messageReceived = (TextMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logTrace("JMSType is " + (String) messageReceived.getJMSType());
      if (messageReceived.getJMSType().equals(type)) {
        logTrace("Pass ");
      } else {
        logMsg("Text Message Failed");
        pass = false;
      }

      // send and receive Object message to Topic
      logTrace("JMSType test - Send ObjectMessage to Topic.");
      messageSentObjectMsg = tool.getDefaultTopicSession()
          .createObjectMessage();
      messageSentObjectMsg
          .setObject("msgHdrJMSTypeTopicTest for Object Message");
      messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSTypeTopicTest");
      messageSentObjectMsg.setJMSType(type);
      tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
      messageReceivedObjectMsg = (ObjectMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logTrace("JMSType is " + (String) messageReceivedObjectMsg.getJMSType());
      if (messageReceivedObjectMsg.getJMSType().equals(type)) {
        logTrace("Pass ");
      } else {
        logMsg("Object Message JMSType Failed");
        pass = false;
      }

      // send and receive map message to Topic
      logTrace("JMSType test - Send MapMessage to Topic.");
      messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
      messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSTypeTopicTest");
      messageSentMapMessage.setString("aString", "value");
      messageSentMapMessage.setJMSType(type);
      tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
      messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logTrace("JMSType is " + (String) messageReceivedMapMessage.getJMSType());
      if (messageReceivedMapMessage.getJMSType().equals(type)) {
        logTrace("Pass ");
      } else {
        logMsg("Map Message JMSType Failed");
        pass = false;
      }

      // send and receive bytes message to Topic
      logTrace("JMSType test - Send BytesMessage to Topic.");
      messageSentBytesMessage = tool.getDefaultTopicSession()
          .createBytesMessage();
      messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSTypeTopicTest");
      messageSentBytesMessage.writeByte(bValue);
      messageSentBytesMessage.setJMSType(type);
      tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
      messageReceivedBytesMessage = (BytesMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logTrace(
          "JMSType is " + (String) messageReceivedBytesMessage.getJMSType());
      if (messageReceivedBytesMessage.getJMSType().equals(type)) {
        logTrace("Pass ");
      } else {
        logMsg("Bytes Message JMSType Failed");
        pass = false;
      }

      // Send and receive a StreamMessage
      logTrace("JMSType test - sending a Stream message");
      messageSentStreamMessage = tool.getDefaultTopicSession()
          .createStreamMessage();
      messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSTypeTopicTest");
      messageSentStreamMessage.writeString("Testing...");
      messageSentStreamMessage.setJMSType(type);
      tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
      messageReceivedStreamMessage = (StreamMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logTrace(
          "JMSType is " + (String) messageReceivedStreamMessage.getJMSType());
      if (messageReceivedStreamMessage.getJMSType().equals(type)) {
        logTrace("Pass ");
      } else {
        logMsg("Stream Message JMSType Failed");
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: invalid JMSType returned from JMS Header");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("msgHdrJMSTypeTopicTest");
    }
  }

  /*
   * @testName: msgHdrJMSPriorityTopicTest
   * 
   * @assertion_ids: JMS:SPEC:16; JMS:SPEC:18; JMS:SPEC:140; JMS:JAVADOC:305;
   * JMS:JAVADOC:383;
   * 
   * @test_Strategy: Send a message to a Topic with JMSPriority set to 2 test
   * with Text, map, object, byte, and stream messages verify on receive.
   * 
   */

  public void msgHdrJMSPriorityTopicTest() throws Fault {
    boolean pass = true;
    byte bValue = 127;
    int priority = 2;

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      StreamMessage messageSentStreamMessage = null;
      StreamMessage messageReceivedStreamMessage = null;
      BytesMessage messageSentBytesMessage = null;
      BytesMessage messageReceivedBytesMessage = null;
      MapMessage messageReceivedMapMessage = null;
      MapMessage messageSentMapMessage = null;
      ObjectMessage messageSentObjectMsg = null;
      ObjectMessage messageReceivedObjectMsg = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText("sending a message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSPriorityTopicTest");
      logTrace("JMSPriority test - Send a Text message");
      tool.getDefaultTopicPublisher().setPriority(priority);
      tool.getDefaultTopicPublisher().publish(messageSent);
      messageReceived = (TextMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logTrace("JMSPriority is " + messageReceived.getJMSPriority());
      if (messageReceived.getJMSPriority() == priority) {
        logTrace("Pass ");
      } else {
        logMsg("Text Message Failed");
        pass = false;
      }

      // send and receive Object message to Topic
      logTrace("JMSPriority test - Send ObjectMessage to Topic.");
      messageSentObjectMsg = tool.getDefaultTopicSession()
          .createObjectMessage();
      messageSentObjectMsg
          .setObject("msgHdrJMSPriorityTopicTest for Object Message");
      messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSPriorityTopicTest");
      tool.getDefaultTopicPublisher().setPriority(priority);
      tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
      messageReceivedObjectMsg = (ObjectMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logTrace("JMSPriority is " + messageReceivedObjectMsg.getJMSPriority());
      if (messageReceivedObjectMsg.getJMSPriority() == priority) {
        logTrace("Pass ");
      } else {
        logMsg("Object Message JMSPriority Failed");
        pass = false;
      }

      // send and receive map message to Topic
      logTrace("JMSPriority test - Send MapMessage to Topic.");
      messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
      messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSPriorityTopicTest");
      messageSentMapMessage.setString("aString", "value");
      tool.getDefaultTopicPublisher().setPriority(priority);
      tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
      messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logTrace("JMSPriority is " + messageReceivedMapMessage.getJMSPriority());
      if (messageReceivedMapMessage.getJMSPriority() == priority) {
        logTrace("Pass ");
      } else {
        logMsg("Map Message JMSPriority Failed");
        pass = false;
      }

      // send and receive bytes message to Topic
      logTrace("JMSPriority test - Send BytesMessage to Topic.");
      messageSentBytesMessage = tool.getDefaultTopicSession()
          .createBytesMessage();
      messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSPriorityTopicTest");
      messageSentBytesMessage.writeByte(bValue);
      tool.getDefaultTopicPublisher().setPriority(priority);
      tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
      messageReceivedBytesMessage = (BytesMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logTrace(
          "JMSPriority is " + messageReceivedBytesMessage.getJMSPriority());
      if (messageReceivedBytesMessage.getJMSPriority() == priority) {
        logTrace("Pass ");
      } else {
        logMsg("Bytes Message JMSPriority Failed");
        pass = false;
      }

      // Send and receive a StreamMessage
      logTrace("JMSPriority test - sending a Stream message");
      messageSentStreamMessage = tool.getDefaultTopicSession()
          .createStreamMessage();
      messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSPriorityTopicTest");
      messageSentStreamMessage.writeString("Testing...");
      tool.getDefaultTopicPublisher().setPriority(priority);
      tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
      messageReceivedStreamMessage = (StreamMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logTrace(
          "JMSPriority is " + messageReceivedStreamMessage.getJMSPriority());
      if (messageReceivedStreamMessage.getJMSPriority() == priority) {
        logTrace("Pass ");
      } else {
        logMsg("Stream Message JMSPriority Failed");
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: invalid JMSPriority returned from JMS Header");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("msgHdrJMSPriorityTopicTest");
    }
  }

  /*
   * @testName: msgHdrJMSExpirationTopicTest
   * 
   * @assertion_ids: JMS:SPEC:15; JMS:SPEC:15.2; JMS:SPEC:15.3; JMS:SPEC:140;
   * JMS:JAVADOC:309; JMS:JAVADOC:379;
   * 
   * @test_Strategy: Send a message to a Topic with time to live set to 0 Verify
   * that JMSExpiration gets set to 0 test with Text, map, object, byte, and
   * stream messages verify on receive.
   * 
   */

  public void msgHdrJMSExpirationTopicTest() throws Fault {
    boolean pass = true;
    byte bValue = 127;
    long forever = 0;

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      StreamMessage messageSentStreamMessage = null;
      StreamMessage messageReceivedStreamMessage = null;
      BytesMessage messageSentBytesMessage = null;
      BytesMessage messageReceivedBytesMessage = null;
      MapMessage messageReceivedMapMessage = null;
      MapMessage messageSentMapMessage = null;
      ObjectMessage messageSentObjectMsg = null;
      ObjectMessage messageReceivedObjectMsg = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText("sending a message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSExpirationTopicTest");
      logMsg(
          "JMSExpiration test (set timetoLive=0) - Send TextMessage to Topic");
      tool.getDefaultTopicPublisher().setTimeToLive(forever);
      tool.getDefaultTopicPublisher().publish(messageSent);
      messageReceived = (TextMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logMsg("JMSExpiration is " + messageReceived.getJMSExpiration());
      if (messageReceived.getJMSExpiration() == forever) {
        logMsg("Pass ");
      } else {
        logMsg("TextMessage JMSExpiration Failed");
        logMsg("TextMessage.getJMSExpiration() returned  "
            + messageReceived.getJMSExpiration() + ", expected 0");
        pass = false;
      }
      logMsg(
          "JMSExpiration test (set timetoLive=60000) - Send TextMessage to Topic");
      tool.getDefaultTopicPublisher().setTimeToLive(60000);
      tool.getDefaultTopicPublisher().publish(messageSent);
      messageReceived = (TextMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      long currentTime = System.currentTimeMillis();
      logMsg("JMSExpiration is " + messageReceived.getJMSExpiration());
      long timeLeftToExpiration = messageReceived.getJMSExpiration()
          - currentTime;
      logMsg("TimeLeftToExpiration is " + timeLeftToExpiration);
      if (timeLeftToExpiration <= 60000) {
        logMsg("Pass ");
      } else {
        logMsg("TextMessage JMSExpiration Failed");
        logMsg("TextMessage JMSExpiration timeLeftToExpiration="
            + timeLeftToExpiration + ", expected <=60000");
        pass = false;
      }

      // send and receive Object message to Topic
      logMsg(
          "JMSExpiration test (set timeToLive=0) - Send ObjectMessage to Topic.");
      messageSentObjectMsg = tool.getDefaultTopicSession()
          .createObjectMessage();
      messageSentObjectMsg
          .setObject("msgHdrJMSExpirationTopicTest for Object Message");
      messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSExpirationTopicTest");
      tool.getDefaultTopicPublisher().setTimeToLive(forever);
      tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
      messageReceivedObjectMsg = (ObjectMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logMsg("JMSExpiration is " + messageReceivedObjectMsg.getJMSExpiration());
      if (messageReceivedObjectMsg.getJMSExpiration() == forever) {
        logMsg("Pass ");
      } else {
        logMsg("ObjectMessage JMSExpiration Failed");
        logMsg("ObjectMessage.getJMSExpiration() returned  "
            + messageReceivedObjectMsg.getJMSExpiration() + ", expected 0");
        pass = false;
      }
      logMsg(
          "JMSExpiration test (set timetoLive=60000) - Send ObjectMessage to Topic.");
      tool.getDefaultTopicPublisher().setTimeToLive(60000);
      tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
      messageReceivedObjectMsg = (ObjectMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      currentTime = System.currentTimeMillis();
      logMsg("JMSExpiration is " + messageReceivedObjectMsg.getJMSExpiration());
      timeLeftToExpiration = messageReceivedObjectMsg.getJMSExpiration()
          - currentTime;
      logMsg("TimeLeftToExpiration is " + timeLeftToExpiration);
      if (timeLeftToExpiration <= 60000) {
        logMsg("Pass ");
      } else {
        logMsg("ObjectMessage JMSExpiration Failed");
        logMsg("ObjectMessage JMSExpiration timeLeftToExpiration="
            + timeLeftToExpiration + ", expected <=60000");
        pass = false;
      }

      // send and receive map message to Topic
      logMsg(
          "JMSExpiration test (set timeToLive=0) - Send MapMessage to Topic.");
      messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
      messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSExpirationTopicTest");
      messageSentMapMessage.setString("aString", "value");
      tool.getDefaultTopicPublisher().setTimeToLive(forever);
      tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
      messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logMsg(
          "JMSExpiration is " + messageReceivedMapMessage.getJMSExpiration());
      if (messageReceivedMapMessage.getJMSExpiration() == forever) {
        logMsg("Pass ");
      } else {
        logMsg("MapMessage JMSExpiration Failed");
        logMsg("MapMessage.getJMSExpiration() returned  "
            + messageReceivedMapMessage.getJMSExpiration() + ", expected 0");
        pass = false;
      }
      logMsg(
          "JMSExpiration test (set timetoLive=60000) - Send MapMessage to Topic.");
      tool.getDefaultTopicPublisher().setTimeToLive(60000);
      tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
      messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      currentTime = System.currentTimeMillis();
      logMsg(
          "JMSExpiration is " + messageReceivedMapMessage.getJMSExpiration());
      timeLeftToExpiration = messageReceivedMapMessage.getJMSExpiration()
          - currentTime;
      logMsg("TimeLeftToExpiration is " + timeLeftToExpiration);
      if (timeLeftToExpiration <= 60000) {
        logMsg("Pass ");
      } else {
        logMsg("MapMessage JMSExpiration Failed");
        logMsg("MapMessage JMSExpiration timeLeftToExpiration="
            + timeLeftToExpiration + ", expected <=60000");
        pass = false;
      }

      // send and receive bytes message to Topic
      logMsg(
          "JMSExpiration test (set timeToLive=0) - Send BytesMessage to Topic.");
      messageSentBytesMessage = tool.getDefaultTopicSession()
          .createBytesMessage();
      messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSExpirationTopicTest");
      messageSentBytesMessage.writeByte(bValue);
      tool.getDefaultTopicPublisher().setTimeToLive(forever);
      tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
      messageReceivedBytesMessage = (BytesMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logMsg(
          "JMSExpiration is " + messageReceivedBytesMessage.getJMSExpiration());
      if (messageReceivedBytesMessage.getJMSExpiration() == forever) {
        logMsg("Pass ");
      } else {
        logMsg("BytesMessage JMSExpiration Failed");
        logMsg("BytesMessage.getJMSExpiration() returned  "
            + messageReceivedBytesMessage.getJMSExpiration() + ", expected 0");
        pass = false;
      }
      logMsg(
          "JMSExpiration test (set timetoLive=60000) - Send BytesMessage to Topic.");
      tool.getDefaultTopicPublisher().setTimeToLive(60000);
      tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
      messageReceivedBytesMessage = (BytesMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      currentTime = System.currentTimeMillis();
      logMsg(
          "JMSExpiration is " + messageReceivedBytesMessage.getJMSExpiration());
      timeLeftToExpiration = messageReceivedBytesMessage.getJMSExpiration()
          - currentTime;
      logMsg("TimeLeftToExpiration is " + timeLeftToExpiration);
      if (timeLeftToExpiration <= 60000) {
        logMsg("Pass ");
      } else {
        logMsg("BytesMessage JMSExpiration Failed");
        logMsg("BytesMessage JMSExpiration timeLeftToExpiration="
            + timeLeftToExpiration + ", expected <=60000");
        pass = false;
      }

      // Send and receive a StreamMessage
      logMsg(
          "JMSExpiration test (set timeToLive=0) - sending a Stream message");
      messageSentStreamMessage = tool.getDefaultTopicSession()
          .createStreamMessage();
      messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSExpirationTopicTest");
      messageSentStreamMessage.writeString("Testing...");
      tool.getDefaultTopicPublisher().setTimeToLive(forever);
      tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
      messageReceivedStreamMessage = (StreamMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logMsg("JMSExpiration is "
          + messageReceivedStreamMessage.getJMSExpiration());
      if (messageReceivedStreamMessage.getJMSExpiration() == forever) {
        logMsg("Pass ");
      } else {
        logMsg("StreamMessage JMSExpiration Failed");
        logMsg("StreamMessage.getJMSExpiration() returned  "
            + messageReceivedStreamMessage.getJMSExpiration() + ", expected 0");
        pass = false;
      }
      logMsg(
          "JMSExpiration test (set timetoLive=60000) - Send StreamMessage to Topic.");
      tool.getDefaultTopicPublisher().setTimeToLive(60000);
      tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
      messageReceivedStreamMessage = (StreamMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      currentTime = System.currentTimeMillis();
      logMsg("JMSExpiration is "
          + messageReceivedStreamMessage.getJMSExpiration());
      timeLeftToExpiration = messageReceivedStreamMessage.getJMSExpiration()
          - currentTime;
      logMsg("TimeLeftToExpiration is " + timeLeftToExpiration);
      if (timeLeftToExpiration <= 60000) {
        logMsg("Pass ");
      } else {
        logMsg("StreamMessage JMSExpiration Failed");
        logMsg("StreamMessage JMSExpiration timeLeftToExpiration="
            + timeLeftToExpiration + ", expected <=60000");
        pass = false;
      }
      if (!pass) {
        throw new Fault(
            "Error: invalid JMSExpiration returned from JMS Header");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("msgHdrJMSExpirationTopicTest");
    }
  }

  /*
   * @testName: msgHdrJMSDestinationTopicTest
   * 
   * @assertion_ids: JMS:SPEC:2; JMS:JAVADOC:363; JMS:JAVADOC:117;
   * 
   * @test_Strategy: Create and publish a message to the default Topic. Receive
   * the msg and verify that JMSDestination is set to the default Topic test
   * with Text, map, object, byte, and stream messages
   * 
   */

  public void msgHdrJMSDestinationTopicTest() throws Fault {
    boolean pass = true;
    byte bValue = 127;
    Topic replyDestination = null;

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      StreamMessage messageSentStreamMessage = null;
      StreamMessage messageReceivedStreamMessage = null;
      BytesMessage messageSentBytesMessage = null;
      BytesMessage messageReceivedBytesMessage = null;
      MapMessage messageReceivedMapMessage = null;
      MapMessage messageSentMapMessage = null;
      ObjectMessage messageSentObjectMsg = null;
      ObjectMessage messageReceivedObjectMsg = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText("publishing a message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSDestinationTopicTest");
      logTrace("publish Text Message to Topic.");
      tool.getDefaultTopicPublisher().publish(messageSent);
      messageReceived = (TextMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logTrace("JMSDestination:  " + messageReceived.getJMSDestination());
      replyDestination = (Topic) messageReceived.getJMSDestination();
      if (replyDestination != null)
        logTrace("Topic name is " + replyDestination.getTopicName());
      if (replyDestination == null) {
        pass = false;
        logMsg("Text Message Error: JMSDestination returned a  null");
      } else if (replyDestination.getTopicName()
          .equals(tool.getDefaultTopic().getTopicName())) {
        logTrace("Pass ");
      } else {
        logMsg("Text Message Failed");
        pass = false;
      }

      // publish and receive Object message to Topic
      logTrace("publish ObjectMessage to Topic.");
      messageSentObjectMsg = tool.getDefaultTopicSession()
          .createObjectMessage();
      messageSentObjectMsg.setObject("msgHdrIDQTest for Object Message");
      messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSDestinationTopicTest");
      tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
      messageReceivedObjectMsg = (ObjectMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logTrace(
          "JMSDestination:  " + messageReceivedObjectMsg.getJMSDestination());
      replyDestination = (Topic) messageReceived.getJMSDestination();
      if (replyDestination != null)
        logTrace("Topic name is " + replyDestination.getTopicName());
      if (replyDestination == null) {
        pass = false;
        logMsg("Object Message Error: JMSDestination returned a  null");
      } else if (replyDestination.getTopicName()
          .equals(tool.getDefaultTopic().getTopicName())) {
        logTrace("Pass ");
      } else {
        logMsg("Object Message Failed");
        pass = false;
      }

      // publish and receive map message to Topic
      logTrace("publish MapMessage to Topic.");
      messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
      messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSDestinationTopicTest");
      messageSentMapMessage.setString("aString", "value");
      tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
      messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logTrace(
          "JMSDestination:  " + messageReceivedMapMessage.getJMSDestination());
      replyDestination = (Topic) messageReceived.getJMSDestination();
      if (replyDestination != null)
        logTrace("Topic name is " + replyDestination.getTopicName());
      if (replyDestination == null) {
        pass = false;
        logMsg("Map Message Error: JMSDestination returned a  null");
      } else if (replyDestination.getTopicName()
          .equals(tool.getDefaultTopic().getTopicName())) {
        logTrace("Pass ");
      } else {
        logMsg("Map Message Failed");
        pass = false;
      }

      // publish and receive bytes message to Topic
      logTrace("publish BytesMessage to Topic.");
      messageSentBytesMessage = tool.getDefaultTopicSession()
          .createBytesMessage();
      messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSDestinationTopicTest");
      messageSentBytesMessage.writeByte(bValue);
      tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
      messageReceivedBytesMessage = (BytesMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logTrace("JMSDestination:  "
          + messageReceivedBytesMessage.getJMSDestination());
      replyDestination = (Topic) messageReceived.getJMSDestination();
      if (replyDestination != null)
        logTrace("Topic name is " + replyDestination.getTopicName());
      if (replyDestination == null) {
        pass = false;
        logMsg("Bytes Message Error: JMSDestination returned a  null");
      } else if (replyDestination.getTopicName()
          .equals(tool.getDefaultTopic().getTopicName())) {
        logTrace("Pass ");
      } else {
        logMsg("Bytes Message Failed");
        pass = false;
      }

      // publish and receive a StreamMessage
      logTrace("publishing a Stream message");
      messageSentStreamMessage = tool.getDefaultTopicSession()
          .createStreamMessage();
      messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSDestinationTopicTest");
      messageSentStreamMessage.writeString("Testing...");
      logTrace("publishing Stream message");
      tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
      messageReceivedStreamMessage = (StreamMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logTrace("JMSDestination:  "
          + messageReceivedStreamMessage.getJMSDestination());
      replyDestination = (Topic) messageReceived.getJMSDestination();
      if (replyDestination != null)
        logTrace("Topic name is " + replyDestination.getTopicName());
      if (replyDestination == null) {
        pass = false;
        logMsg("Stream Message Error: JMSDestination returned a  null");
      } else if (replyDestination.getTopicName()
          .equals(tool.getDefaultTopic().getTopicName())) {
        logTrace("Pass ");
      } else {
        logMsg("Stream Message Failed");
        pass = false;
      }
      if (!pass) {
        throw new Fault(
            "Error: invalid JMSDestination returned from JMS Header");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("msgHdrJMSDestinationTopicTest");
    }
  }

  /*
   * @testName: msgHdrJMSDeliveryModeTopicTest
   * 
   * @assertion_ids: JMS:SPEC:3; JMS:SPEC:140; JMS:JAVADOC:367; JMS:SPEC:246.2;
   * JMS:JAVADOC:301;
   * 
   * @test_Strategy: Create and publish a message to the default Topic. Receive
   * the msg and verify that JMSDeliveryMode is set the default delivery mode of
   * persistent. Create and test another message with a nonpersistent delivery
   * mode. test with Text, map, object, byte, and stream messages
   */

  public void msgHdrJMSDeliveryModeTopicTest() throws Fault {
    boolean pass = true;
    byte bValue = 127;

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      StreamMessage messageSentStreamMessage = null;
      StreamMessage messageReceivedStreamMessage = null;
      BytesMessage messageSentBytesMessage = null;
      BytesMessage messageReceivedBytesMessage = null;
      MapMessage messageReceivedMapMessage = null;
      MapMessage messageSentMapMessage = null;
      ObjectMessage messageSentObjectMsg = null;
      ObjectMessage messageReceivedObjectMsg = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logMsg("default delivery mode is " + Message.DEFAULT_DELIVERY_MODE);
      logMsg("persistent is: " + DeliveryMode.PERSISTENT);
      if (Message.DEFAULT_DELIVERY_MODE != DeliveryMode.PERSISTENT) {
        pass = false;
        logMsg("Error: default delivery mode was "
            + Message.DEFAULT_DELIVERY_MODE);
        logMsg("The default delivery mode should be persistent: "
            + DeliveryMode.PERSISTENT);
      }
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText("publishing a message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSDeliveryModeTopicTest");
      logTrace("publish Text Message to Topic.");
      tool.getDefaultTopicPublisher().publish(messageSent);
      messageReceived = (TextMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logTrace("JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
      if (messageReceived.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
        pass = false;
        logMsg(
            "Text Message Error: JMSDeliveryMode should be set to persistent as default");
      } else {
        logTrace("Text Message Pass ");
      }

      // publish and receive Object message to Topic
      logTrace("publish ObjectMessage to Topic.");
      messageSentObjectMsg = tool.getDefaultTopicSession()
          .createObjectMessage();
      messageSentObjectMsg.setObject("Test for Object Message");
      messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSDeliveryModeTopicTest");
      tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
      messageReceivedObjectMsg = (ObjectMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logTrace("JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
      if (messageReceived.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
        pass = false;
        logMsg(
            "Object Message Error: JMSDeliveryMode should be set to persistent as default");
      } else {
        logTrace("Object Message Pass ");
      }

      // publish and receive map message to Topic
      logTrace("publish MapMessage to Topic.");
      messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
      messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSDeliveryModeTopicTest");
      messageSentMapMessage.setString("aString", "value");
      tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
      messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logTrace("JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
      if (messageReceived.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
        pass = false;
        logMsg(
            "Map Message Error: JMSDeliveryMode should be set to persistent as default");
      } else {
        logTrace("Map Message Pass ");
      }

      // publish and receive bytes message to Topic
      logTrace("publish BytesMessage to Topic.");
      messageSentBytesMessage = tool.getDefaultTopicSession()
          .createBytesMessage();
      messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSDeliveryModeTopicTest");
      messageSentBytesMessage.writeByte(bValue);
      tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
      messageReceivedBytesMessage = (BytesMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logTrace("JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
      if (messageReceived.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
        pass = false;
        logMsg(
            "Bytes Message Error: JMSDeliveryMode should be set to persistent as default");
      } else {
        logTrace("Bytes Message Pass ");
      }

      // publish and receive a StreamMessage
      logTrace("publishing a Stream message");
      messageSentStreamMessage = tool.getDefaultTopicSession()
          .createStreamMessage();
      messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSDeliveryModeTopicTest");
      messageSentStreamMessage.writeString("Testing...");
      logTrace("publishing Stream message");
      tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
      messageReceivedStreamMessage = (StreamMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      logTrace("JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
      if (messageReceived.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
        pass = false;
        logMsg(
            "Stream Message Error: JMSDeliveryMode should be set to persistent as default");
      } else {
        logTrace("Stream Message Pass ");
      }

      // Test again - this time set delivery mode to persistent
      tool.getDefaultTopicPublisher()
          .setDeliveryMode(DeliveryMode.NON_PERSISTENT);
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText("publishing a message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSDeliveryModeTopicTest");
      logTrace("publish Text Message to Topic.");
      tool.getDefaultTopicPublisher().publish(messageSent);
      messageReceived = (TextMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logTrace("JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
      if (messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
        pass = false;
        logMsg(
            "Text Message Error: JMSDeliveryMode should be set to NON_PERSISTENT");
      } else {
        logTrace("Text Message Pass ");
      }

      // publish and receive Object message to Topic
      logTrace("publish ObjectMessage to Topic.");
      messageSentObjectMsg = tool.getDefaultTopicSession()
          .createObjectMessage();
      messageSentObjectMsg
          .setObject("msgHdrJMSDeliveryModeTopicTest for Object Message");
      messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSDeliveryModeTopicTest");
      tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
      messageReceivedObjectMsg = (ObjectMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      if (messageReceivedObjectMsg != null)
        logTrace("messageReceivedObjectMsg=" + messageReceivedObjectMsg);
      logTrace("JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
      if (messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
        pass = false;
        logMsg(
            "Object Message Error: JMSDeliveryMode should be set to NON_PERSISTENT");
      } else {
        logTrace("Object Message Pass ");
      }

      // publish and receive map message to Topic
      logTrace("publish MapMessage to Topic.");
      messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
      messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSDeliveryModeTopicTest");
      messageSentMapMessage.setString("aString", "value");
      tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
      messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      if (messageReceivedMapMessage != null)
        logTrace("messageReceivedMapMessage=" + messageReceivedMapMessage);
      logTrace("JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
      if (messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
        pass = false;
        logMsg(
            "Map Message Error: JMSDeliveryMode should be set to NON_PERSISTENT");
      } else {
        logTrace("Map Message Pass ");
      }

      // publish and receive bytes message to Topic
      logTrace("publish BytesMessage to Topic.");
      messageSentBytesMessage = tool.getDefaultTopicSession()
          .createBytesMessage();
      messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSDeliveryModeTopicTest");
      messageSentBytesMessage.writeByte(bValue);
      tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
      messageReceivedBytesMessage = (BytesMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      if (messageReceivedBytesMessage != null)
        logTrace("messageReceivedBytesMessage=" + messageReceivedBytesMessage);
      logTrace("JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
      if (messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
        pass = false;
        logMsg(
            "Bytes Message Error: JMSDeliveryMode should be set to NON_PERSISTENT");
      } else {
        logTrace("Bytes Message Pass ");
      }

      // publish and receive a StreamMessage
      logTrace("publishing a Stream message");
      messageSentStreamMessage = tool.getDefaultTopicSession()
          .createStreamMessage();
      messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSDeliveryModeTopicTest");
      messageSentStreamMessage.writeString("Testing...");
      logTrace("publishing Stream message");
      tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
      messageReceivedStreamMessage = (StreamMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
      if (messageReceivedStreamMessage != null)
        logTrace(
            "messageReceivedStreamMessage=" + messageReceivedStreamMessage);
      logTrace("JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
      if (messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
        pass = false;
        logMsg(
            "Stream Message Error: JMSDeliveryMode should be set to NON_PERSISTENT");
      } else {
        logTrace("Stream Message Pass ");
      }
      if (!pass) {
        throw new Fault(
            "Error: invalid JMSDeliveryMode returned from JMS Header");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("msgHdrJMSDeliveryModeTopicTest");
    }
  }

}
