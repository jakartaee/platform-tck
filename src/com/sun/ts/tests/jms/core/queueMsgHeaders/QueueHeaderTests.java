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

package com.sun.ts.tests.jms.core.queueMsgHeaders;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.ArrayList;
import java.rmi.RemoteException;
import java.util.Properties;
import com.sun.javatest.Status;

public class QueueHeaderTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.queueMsgHeaders.QueueHeaderTests";

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

  /* Run test in standalone mode */

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    QueueHeaderTests theTests = new QueueHeaderTests();
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
        throw new Exception("'user' in ts.jte must be null");
      }
      if (password == null) {
        throw new Exception("'password' in ts.jte must be null");
      }
      if (mode == null) {
        throw new Exception("'mode' in ts.jte must be null");
      }
      queues = new ArrayList(2);

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
        tool.doClientQueueTestCleanup(connections, queues);
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
   * @testName: msgHdrIDQTest
   * 
   * @assertion_ids: JMS:SPEC:4; JMS:JAVADOC:343;
   * 
   * @test_Strategy: Send to a Queue and receive Text, Map, Bytes, Stream, and
   * object message. call getJMSMessageID and verify that it starts with ID:
   */

  public void msgHdrIDQTest() throws Fault {
    boolean pass = true;
    byte bValue = 127;
    String id = null;

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      StreamMessage messageSentS = null;
      StreamMessage messageReceivedS = null;
      BytesMessage messageSentB = null;
      BytesMessage messageReceivedB = null;
      MapMessage messageReceivedM = null;
      MapMessage messageSentM = null;
      ObjectMessage messageSentO = null;
      ObjectMessage messageReceivedO = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();

      // send and receive Object message to Queue
      logTrace("Send ObjectMessage to Queue.");
      messageSentO = tool.getDefaultQueueSession().createObjectMessage();
      messageSentO.setObject("msgHdrIDQTest for ObjectMessage");
      messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrIDQTest");
      tool.getDefaultQueueSender().send(messageSentO);
      messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("getJMSMessageID ");
      logTrace(" " + messageReceivedO.getJMSMessageID());
      id = messageReceivedO.getJMSMessageID();
      if (!chkMessageID(id)) {
        logMsg("ObjectMessage error: JMSMessageID does not start with ID:");
        pass = false;
      }
      // send and receive map message to Queue
      logTrace("Send MapMessage to Queue.");
      messageSentM = tool.getDefaultQueueSession().createMapMessage();
      messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrIDQTest");
      messageSentM.setString("aString", "value");
      tool.getDefaultQueueSender().send(messageSentM);
      messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("getJMSMessageID ");
      logTrace(" " + messageReceivedM.getJMSMessageID());
      id = messageReceivedM.getJMSMessageID();
      if (!chkMessageID(id)) {
        logMsg("MapMessage error: JMSMessageID does not start with ID:");
        pass = false;
      }

      // send and receive bytes message to Queue
      logTrace("Send BytesMessage to Queue.");
      messageSentB = tool.getDefaultQueueSession().createBytesMessage();
      messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrIDQTest");
      messageSentB.writeByte(bValue);
      tool.getDefaultQueueSender().send(messageSentB);
      messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("getJMSMessageID ");
      logTrace(" " + messageReceivedB.getJMSMessageID());
      id = messageReceivedB.getJMSMessageID();
      if (!chkMessageID(id)) {
        logMsg("BytesMessage error: JMSMessageID does not start with ID:");
        pass = false;
      }

      // Send and receive a StreamMessage
      logTrace("sending a StreamMessage");
      messageSentS = tool.getDefaultQueueSession().createStreamMessage();
      messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrIDQTest");
      messageSentS.writeString("Testing...");
      logTrace("Sending message");
      tool.getDefaultQueueSender().send(messageSentS);
      messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("getJMSMessageID ");
      logTrace(" " + messageReceivedS.getJMSMessageID());
      id = messageReceivedS.getJMSMessageID();
      if (!chkMessageID(id)) {
        logMsg("StreamMessage error: JMSMessageID does not start with ID:");
        pass = false;
      }

      // TextMessage
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText("sending a TextMessage");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", "msgHdrIDQTest");
      logTrace("sending a TextMessage");
      tool.getDefaultQueueSender().send(messageSent);
      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
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
      throw new Fault("msgHdrIDQTest");
    }
  }

  /*
   * @testName: msgHdrTimeStampQTest
   * 
   * @assertion_ids: JMS:SPEC:7; JMS:JAVADOC:347;
   * 
   * @test_Strategy: With a queue destination Send a single Text, map, bytes,
   * stream, and object message check time of send against time send returns
   * JMSTimeStamp should be between these two
   */

  public void msgHdrTimeStampQTest() throws Fault {
    boolean pass = true;
    long timeBeforeSend;
    long timeAfterSend;
    byte bValue = 127;

    try {
      TextMessage messageSent = null;
      StreamMessage messageSentS = null;
      BytesMessage messageSentB = null;
      MapMessage messageSentM = null;
      ObjectMessage messageSentO = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();

      // send and receive Object message to Queue
      logTrace("Send ObjectMessage to Queue.");
      messageSentO = tool.getDefaultQueueSession().createObjectMessage();
      messageSentO.setObject("msgHdrTimeStampQTest for ObjectMessage");
      messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrTimeStampQTest");

      // get the current time in milliseconds - before and after the send
      timeBeforeSend = System.currentTimeMillis();
      tool.getDefaultQueueSender().send(messageSentO);

      // message has been sent
      timeAfterSend = System.currentTimeMillis();
      logTrace(" getJMSTimestamp");
      logTrace(" " + messageSentO.getJMSTimestamp());
      logTrace("Time at send is: " + timeBeforeSend);
      logTrace("Time after return fromsend is:" + timeAfterSend);
      if ((timeBeforeSend <= messageSentO.getJMSTimestamp())
          && (timeAfterSend >= messageSentO.getJMSTimestamp())) {
        logTrace("ObjectMessage TimeStamp pass");
      } else {
        logMsg("Error: invalid timestamp from ObjectMessage");
        pass = false;
      }

      // send map message to Queue
      logTrace("Send MapMessage to Queue.");
      messageSentM = tool.getDefaultQueueSession().createMapMessage();
      messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrTimeStampQTest");
      messageSentM.setString("aString", "value");

      // get the current time in milliseconds - before and after the send
      timeBeforeSend = System.currentTimeMillis();
      tool.getDefaultQueueSender().send(messageSentM);

      // message has been sent
      timeAfterSend = System.currentTimeMillis();
      logTrace(" getJMSTimestamp");
      logTrace(" " + messageSentM.getJMSTimestamp());
      logTrace("Time at send is: " + timeBeforeSend);
      logTrace("Time after return fromsend is:" + timeAfterSend);
      if (!(timeBeforeSend <= messageSentM.getJMSTimestamp())
          && (timeAfterSend >= messageSentM.getJMSTimestamp())) {
        logErr("Error: invalid timestamp from MapMessage");
        pass = false;
      }

      // send and receive bytes message to Queue
      logTrace("Send BytesMessage to Queue.");
      messageSentB = tool.getDefaultQueueSession().createBytesMessage();
      messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrTimeStampQTest");
      messageSentB.writeByte(bValue);

      // get the current time in milliseconds - before and after the send
      timeBeforeSend = System.currentTimeMillis();
      tool.getDefaultQueueSender().send(messageSentB);

      // message has been sent
      timeAfterSend = System.currentTimeMillis();
      logTrace(" getJMSTimestamp");
      logTrace(" " + messageSentB.getJMSTimestamp());
      logTrace("Time at send is: " + timeBeforeSend);
      logTrace("Time after return fromsend is:" + timeAfterSend);
      if ((timeBeforeSend <= messageSentB.getJMSTimestamp())
          && (timeAfterSend >= messageSentB.getJMSTimestamp())) {
        logTrace("BytesMessage TimeStamp pass");
      } else {
        logMsg("Error: invalid timestamp from BytesMessage");
        pass = false;
      }

      // Send and receive a StreamMessage
      logTrace("sending a StreamMessage");
      messageSentS = tool.getDefaultQueueSession().createStreamMessage();
      messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrTimeStampQTest");
      messageSentS.writeString("Testing...");
      logTrace("Sending message");

      // get the current time in milliseconds - before and after the send
      timeBeforeSend = System.currentTimeMillis();
      tool.getDefaultQueueSender().send(messageSentS);

      // message has been sent
      timeAfterSend = System.currentTimeMillis();
      logTrace(" getJMSTimestamp");
      logTrace(" " + messageSentS.getJMSTimestamp());
      logTrace("Time at send is: " + timeBeforeSend);
      logTrace("Time after return fromsend is:" + timeAfterSend);
      if ((timeBeforeSend <= messageSentS.getJMSTimestamp())
          && (timeAfterSend >= messageSentS.getJMSTimestamp())) {
        logTrace("StreamMessage TimeStamp pass");
      } else {
        logMsg("Error: invalid timestamp from StreamMessage");
        pass = false;
      }

      // TextMessage
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText("sending a TextMessage");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrTimeStampQTest");
      logTrace("sending a TextMessage");

      // get the current time in milliseconds - before and after the send
      timeBeforeSend = System.currentTimeMillis();
      tool.getDefaultQueueSender().send(messageSent);

      // message has been sent
      timeAfterSend = System.currentTimeMillis();
      logTrace(" getJMSTimestamp");
      logTrace(" " + messageSent.getJMSTimestamp());
      logTrace("Time at send is: " + timeBeforeSend);
      logTrace("Time after return fromsend is:" + timeAfterSend);
      if (!(timeBeforeSend <= messageSent.getJMSTimestamp())
          && (timeAfterSend >= messageSent.getJMSTimestamp())) {
        logErr("Error: invalid timestamp from TextMessage");
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: invalid TimeStamp returned from JMSTimeStamp");
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("msgHdrTimeStampQTest");
    }
  }

  /*
   * @testName: msgHdrCorlIdQTest
   * 
   * @assertion_ids: JMS:SPEC:246.7; JMS:JAVADOC:355; JMS:JAVADOC:357;
   * 
   * @test_Strategy: Send a message to a Queue with CorrelationID set. Receive
   * msg and verify the correlationid is as set by client
   */

  public void msgHdrCorlIdQTest() throws Fault {
    boolean pass = true;
    byte bValue = 127;
    String jmsCorrelationID = "testCorrelationid";
    String testName = "msgHdrCorlIdQTest";

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      StreamMessage messageSentS = null;
      StreamMessage messageReceivedS = null;
      BytesMessage messageSentB = null;
      BytesMessage messageReceivedB = null;
      MapMessage messageReceivedM = null;
      MapMessage messageSentM = null;
      ObjectMessage messageSentO = null;
      ObjectMessage messageReceivedO = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();

      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText("sending a message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      logTrace("Send TextMessage to Queue.");
      messageSent.setJMSCorrelationID(jmsCorrelationID);
      try {
        tool.getDefaultQueueSender().send(messageSent);
        messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
            .receive(timeout);
        if (messageReceived == null) {
          logErr("messageReceived is null");
          pass = false;
        } else if (messageReceived.getJMSCorrelationID() == null) {
          pass = false;
          logErr("TextMessage Error: JMSCorrelationID returned a  null");
        } else if (!messageReceived.getJMSCorrelationID()
            .equals(jmsCorrelationID)) {
          pass = false;
          logErr("TextMessage Error: JMSCorrelationID is incorrect");
        }
      } catch (java.lang.Exception e) {
        pass = false;
        logErr("Unexpected Exception: ", e);
      }

      // send and receive map message to Queue
      logTrace("Send MapMessage to Queue.");
      messageSentM = tool.getDefaultQueueSession().createMapMessage();
      messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      messageSentM.setJMSCorrelationID(jmsCorrelationID);
      messageSentM.setString("aString", "value");
      tool.getDefaultQueueSender().send(messageSentM);

      messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedM.getJMSCorrelationID() == null) {
        pass = false;
        logErr("MapMessage Error: JMSCorrelationID returned a  null");
      } else if (!messageReceivedM.getJMSCorrelationID()
          .equals(jmsCorrelationID)) {
        pass = false;
        logErr("MapMessage Error: JMSCorrelationID is incorrect");
      }

      // send and receive bytes message to Queue
      logTrace("Send BytesMessage to Queue.");
      messageSentB = tool.getDefaultQueueSession().createBytesMessage();
      messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      messageSentB.setJMSCorrelationID(jmsCorrelationID);
      messageSentB.writeByte(bValue);
      tool.getDefaultQueueSender().send(messageSentB);

      messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedB.getJMSCorrelationID() == null) {
        pass = false;
        logMsg("BytesMessage Error: JMSCorrelationID returned a  null");
      } else if (!messageReceivedB.getJMSCorrelationID()
          .equals(jmsCorrelationID)) {
        pass = false;
        logMsg("Byte Message Error: JMSCorrelationID is incorrect");
      }

      // Send and receive a StreamMessage
      logTrace("sending a StreamMessage");
      messageSentS = tool.getDefaultQueueSession().createStreamMessage();
      messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      messageSentS.setJMSCorrelationID(jmsCorrelationID);
      messageSentS.writeString("Testing...");
      tool.getDefaultQueueSender().send(messageSentS);

      messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedS.getJMSCorrelationID() == null) {
        pass = false;
        logErr("StreamMessage Error: JMSCorrelationID returned a  null");
      } else if (!messageReceivedS.getJMSCorrelationID()
          .equals(jmsCorrelationID)) {
        pass = false;
        logErr("StreamMessage Error: JMSCorrelationID is incorrect");
      }

      // send and receive Object message to Queue
      logTrace("Send ObjectMessage to Queue.");
      messageSentO = tool.getDefaultQueueSession().createObjectMessage();
      messageSentO.setObject("msgHdrIDQTest for ObjectMessage");
      messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      messageSentO.setJMSCorrelationID(jmsCorrelationID);
      tool.getDefaultQueueSender().send(messageSentO);

      messageReceivedO = (ObjectMessage) (Message) tool
          .getDefaultQueueReceiver().receive(timeout);
      if (messageReceivedO.getJMSCorrelationID() == null) {
        pass = false;
        logMsg("ObjectMessage Error: JMSCorrelationID returned a  null");
      } else if (!messageReceivedO.getJMSCorrelationID()
          .equals(jmsCorrelationID)) {
        pass = false;
        logMsg("ObjectMessage Error: JMSCorrelationID is incorrect");
      }

      if (!pass) {
        throw new Fault(
            "Error: invalid JMSCorrelationID returned from JMS Header");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault(testName);
    }
  }

  /*
   * @testName: msgHdrReplyToQTest
   * 
   * @assertion_ids: JMS:SPEC:12; JMS:SPEC:246.8; JMS:JAVADOC:359;
   * JMS:JAVADOC:361; JMS:JAVADOC:286; JMS:JAVADOC:289; JMS:JAVADOC:562;
   * JMS:JAVADOC:166;
   * 
   * @test_Strategy: Send a message to a Queue with ReplyTo set to null and then
   * set to a destination test with Text, map, object, byte, and stream messages
   * verify on receive.
   * 
   */

  public void msgHdrReplyToQTest() throws Fault {
    boolean pass = true;
    Queue replyQueue = null;
    byte bValue = 127;

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      StreamMessage messageSentS = null;
      StreamMessage messageReceivedS = null;
      BytesMessage messageSentB = null;
      BytesMessage messageReceivedB = null;
      MapMessage messageReceivedM = null;
      MapMessage messageSentM = null;
      ObjectMessage messageSentO = null;
      ObjectMessage messageReceivedO = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText("sending a message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrReplyToQTest");
      logTrace("Send Text message");

      // messageSent.setJMSReplyTo(tool.getDefaultQueue());
      tool.getDefaultQueueSender().send(messageSent);
      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceived.getJMSReplyTo() == null) {
        logTrace(" as expected replyto field is null");
      } else {
        logMsg(
            "ERROR: expected replyto field should have been null for this case");
        pass = false;
      }
      logTrace("Set ReplyTo and resend msg");
      messageSent.setJMSReplyTo(tool.getDefaultQueue());
      tool.getDefaultQueueSender().send(messageSent);
      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      replyQueue = (Queue) messageReceived.getJMSReplyTo();
      logTrace("Queue name is " + replyQueue.getQueueName());
      if (replyQueue.getQueueName()
          .equals(tool.getDefaultQueue().getQueueName())) {
        logTrace("Pass ");
      } else {
        logMsg("TextMessage Failed");
        pass = false;
      }

      // send and receive Object message to Queue
      logTrace("Send ObjectMessage to Queue.");
      messageSentO = tool.getDefaultQueueSession().createObjectMessage();
      messageSentO.setObject("msgHdrReplyToQTest for ObjectMessage");
      messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrReplyToQTest");
      tool.getDefaultQueueSender().send(messageSentO);
      messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedO.getJMSReplyTo() == null) {
        logTrace(" as expected replyto field is null");
      } else {
        replyQueue = (Queue) messageReceivedO.getJMSReplyTo();
        logMsg("ReplyTo is: " + replyQueue.toString());
        logMsg("ERROR: expected replyto field to be null in this case");
        pass = false;
      }
      logTrace("Set ReplyTo and resend msg");
      messageSentO.setJMSReplyTo(tool.getDefaultQueue());
      tool.getDefaultQueueSender().send(messageSentO);
      messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      replyQueue = (Queue) messageReceivedO.getJMSReplyTo();
      logTrace("Queue name is " + replyQueue.getQueueName());
      if (replyQueue.getQueueName()
          .equals(tool.getDefaultQueue().getQueueName())) {
        logTrace("Pass ");
      } else {
        logMsg("ObjectMessage ReplyTo Failed");
        pass = false;
      }

      // send and receive map message to Queue
      logTrace("Send MapMessage to Queue.");
      messageSentM = tool.getDefaultQueueSession().createMapMessage();
      messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrReplyToQTest");
      messageSentM.setString("aString", "value");
      tool.getDefaultQueueSender().send(messageSentM);
      messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedM.getJMSReplyTo() == null) {
        logTrace(" as expected replyto field is null");
      } else {
        logMsg("ERROR: expected replyto field to be null in this case");
        pass = false;
      }
      logTrace("Set ReplyTo and resend msg");
      messageSentM.setJMSReplyTo(tool.getDefaultQueue());
      tool.getDefaultQueueSender().send(messageSentM);
      messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("Received Map message ");
      replyQueue = (Queue) messageReceivedM.getJMSReplyTo();
      logTrace("Queue name is " + replyQueue.getQueueName());
      if (replyQueue.getQueueName()
          .equals(tool.getDefaultQueue().getQueueName())) {
        logTrace("Pass ");
      } else {
        logMsg("MapMessage ReplyTo Failed");
        pass = false;
      }

      // send and receive bytes message to Queue
      logTrace("Send BytesMessage to Queue.");
      messageSentB = tool.getDefaultQueueSession().createBytesMessage();
      messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrReplyToQTest");
      messageSentB.writeByte(bValue);
      tool.getDefaultQueueSender().send(messageSentB);
      messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedB.getJMSReplyTo() == null) {
        logTrace(" as expected replyto field is null");
      } else {
        logMsg("ERROR: expected replyto field to be null in this case");
        pass = false;
      }
      logTrace("Set ReplyTo and resend msg");
      messageSentB.setJMSReplyTo(tool.getDefaultQueue());
      tool.getDefaultQueueSender().send(messageSentB);
      messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("Received Bytes message ");
      replyQueue = (Queue) messageReceivedB.getJMSReplyTo();
      logTrace("Queue name is " + replyQueue.getQueueName());
      if (replyQueue.getQueueName()
          .equals(tool.getDefaultQueue().getQueueName())) {
        logTrace("Pass ");
      } else {
        logMsg("BytesMessage ReplyTo Failed");
        pass = false;
      }

      // Send and receive a StreamMessage
      messageSentS = tool.getDefaultQueueSession().createStreamMessage();
      messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrReplyToQTest");
      messageSentS.writeString("Testing...");
      logTrace("Sending StreamMessage");
      tool.getDefaultQueueSender().send(messageSentS);
      messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedS.getJMSReplyTo() == null) {
        logTrace(" as expected replyto field is null");
      } else {
        replyQueue = (Queue) messageReceivedS.getJMSReplyTo();
        logMsg("ERROR: expected replyto field to be null in this case");
        pass = false;
      }
      logTrace("Set ReplyTo and resend msg");
      messageSentS.setJMSReplyTo(tool.getDefaultQueue());
      tool.getDefaultQueueSender().send(messageSentS);
      messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("Received Stream message ");
      replyQueue = (Queue) messageReceivedS.getJMSReplyTo();
      logTrace("Queue name is " + replyQueue.getQueueName());
      if (replyQueue.getQueueName()
          .equals(tool.getDefaultQueue().getQueueName())) {
        logTrace("Pass ");
      } else {
        logMsg("StreamMessage ReplyTo Failed");
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: invalid Replyto returned from JMS Header");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("msgHdrReplyToQTest");
    }
  }

  /*
   * @testName: msgHdrJMSTypeQTest
   * 
   * @assertion_ids: JMS:SPEC:246.9; JMS:JAVADOC:375; JMS:JAVADOC:377;
   * 
   * @test_Strategy: Send a message to a Queue with JMSType set to TESTMSG test
   * with Text, map, object, byte, and stream messages verify on receive.
   * 
   */

  public void msgHdrJMSTypeQTest() throws Fault {
    boolean pass = true;
    byte bValue = 127;
    String type = "TESTMSG";

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      StreamMessage messageSentS = null;
      StreamMessage messageReceivedS = null;
      BytesMessage messageSentB = null;
      BytesMessage messageReceivedB = null;
      MapMessage messageReceivedM = null;
      MapMessage messageSentM = null;
      ObjectMessage messageSentO = null;
      ObjectMessage messageReceivedO = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText("sending a message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSTypeQTest");
      logTrace("JMSType test - Send a TextMessage");
      messageSent.setJMSType(type);
      tool.getDefaultQueueSender().send(messageSent);
      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("JMSType is " + (String) messageReceived.getJMSType());
      if (messageReceived.getJMSType().equals(type)) {
        logTrace("Pass ");
      } else {
        logMsg("TextMessage Failed");
        pass = false;
      }

      // send and receive Object message to Queue
      logTrace("JMSType test - Send ObjectMessage to Queue.");
      messageSentO = tool.getDefaultQueueSession().createObjectMessage();
      messageSentO.setObject("msgHdrJMSTypeQTest for ObjectMessage");
      messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSTypeQTest");
      messageSentO.setJMSType(type);
      tool.getDefaultQueueSender().send(messageSentO);
      messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("JMSType is " + (String) messageReceivedO.getJMSType());
      if (messageReceivedO.getJMSType().equals(type)) {
        logTrace("Pass ");
      } else {
        logMsg("ObjectMessage JMSType Failed");
        pass = false;
      }

      // send and receive map message to Queue
      logTrace("JMSType test - Send MapMessage to Queue.");
      messageSentM = tool.getDefaultQueueSession().createMapMessage();
      messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSTypeQTest");
      messageSentM.setString("aString", "value");
      messageSentM.setJMSType(type);
      tool.getDefaultQueueSender().send(messageSentM);
      messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("JMSType is " + (String) messageReceivedM.getJMSType());
      if (messageReceivedM.getJMSType().equals(type)) {
        logTrace("Pass ");
      } else {
        logMsg("MapMessage JMSType Failed");
        pass = false;
      }

      // send and receive bytes message to Queue
      logTrace("JMSType test - Send BytesMessage to Queue.");
      messageSentB = tool.getDefaultQueueSession().createBytesMessage();
      messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSTypeQTest");
      messageSentB.writeByte(bValue);
      messageSentB.setJMSType(type);
      tool.getDefaultQueueSender().send(messageSentB);
      messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("JMSType is " + (String) messageReceivedB.getJMSType());
      if (messageReceivedB.getJMSType().equals(type)) {
        logTrace("Pass ");
      } else {
        logMsg("BytesMessage JMSType Failed");
        pass = false;
      }

      // Send and receive a StreamMessage
      logTrace("JMSType test - sending a StreamMessage");
      messageSentS = tool.getDefaultQueueSession().createStreamMessage();
      messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSTypeQTest");
      messageSentS.writeString("Testing...");
      messageSentS.setJMSType(type);
      tool.getDefaultQueueSender().send(messageSentS);
      messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("JMSType is " + (String) messageReceivedS.getJMSType());
      if (messageReceivedS.getJMSType().equals(type)) {
        logTrace("Pass ");
      } else {
        logMsg("StreamMessage JMSType Failed");
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: invalid JMSType returned from JMS Header");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("msgHdrJMSTypeQTest");
    }
  }

  /*
   * @testName: msgHdrJMSPriorityQTest
   * 
   * @assertion_ids: JMS:SPEC:16; JMS:SPEC:18; JMS:SPEC:140; JMS:JAVADOC:305;
   * JMS:JAVADOC:383;
   * 
   * @test_Strategy: Send a message to a Queue with JMSPriority set to 2 test
   * with Text, map, object, byte, and stream messages verify on receive.
   * 
   */

  public void msgHdrJMSPriorityQTest() throws Fault {
    boolean pass = true;
    byte bValue = 127;
    int priority = 2;

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      StreamMessage messageSentS = null;
      StreamMessage messageReceivedS = null;
      BytesMessage messageSentB = null;
      BytesMessage messageReceivedB = null;
      MapMessage messageReceivedM = null;
      MapMessage messageSentM = null;
      ObjectMessage messageSentO = null;
      ObjectMessage messageReceivedO = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText("sending a message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSPriorityQTest");
      logTrace("JMSPriority test - Send a TextMessage");
      tool.getDefaultQueueSender().setPriority(priority);
      tool.getDefaultQueueSender().send(messageSent);
      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("JMSPriority is " + messageReceived.getJMSPriority());
      if (messageReceived.getJMSPriority() == priority) {
        logTrace("Pass ");
      } else {
        logMsg("TextMessage Failed");
        pass = false;
      }

      // send and receive Object message to Queue
      logTrace("JMSPriority test - Send ObjectMessage to Queue.");
      messageSentO = tool.getDefaultQueueSession().createObjectMessage();
      messageSentO.setObject("msgHdrJMSPriorityQTest for ObjectMessage");
      messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSPriorityQTest");
      tool.getDefaultQueueSender().setPriority(priority);
      tool.getDefaultQueueSender().send(messageSentO);
      messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("JMSPriority is " + messageReceivedO.getJMSPriority());
      if (messageReceivedO.getJMSPriority() == priority) {
        logTrace("Pass ");
      } else {
        logMsg("ObjectMessage JMSPriority Failed");
        pass = false;
      }

      // send and receive map message to Queue
      logTrace("JMSPriority test - Send MapMessage to Queue.");
      messageSentM = tool.getDefaultQueueSession().createMapMessage();
      messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSPriorityQTest");
      messageSentM.setString("aString", "value");
      tool.getDefaultQueueSender().setPriority(priority);
      tool.getDefaultQueueSender().send(messageSentM);
      messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("JMSPriority is " + messageReceivedM.getJMSPriority());
      if (messageReceivedM.getJMSPriority() == priority) {
        logTrace("Pass ");
      } else {
        logMsg("MapMessage JMSPriority Failed");
        pass = false;
      }

      // send and receive bytes message to Queue
      logTrace("JMSPriority test - Send BytesMessage to Queue.");
      messageSentB = tool.getDefaultQueueSession().createBytesMessage();
      messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSPriorityQTest");
      messageSentB.writeByte(bValue);
      tool.getDefaultQueueSender().setPriority(priority);
      tool.getDefaultQueueSender().send(messageSentB);
      messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("JMSPriority is " + messageReceivedB.getJMSPriority());
      if (messageReceivedB.getJMSPriority() == priority) {
        logTrace("Pass ");
      } else {
        logMsg("BytesMessage JMSPriority Failed");
        pass = false;
      }

      // Send and receive a StreamMessage
      logTrace("JMSPriority test - sending a StreamMessage");
      messageSentS = tool.getDefaultQueueSession().createStreamMessage();
      messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSPriorityQTest");
      messageSentS.writeString("Testing...");
      tool.getDefaultQueueSender().setPriority(priority);
      tool.getDefaultQueueSender().send(messageSentS);
      messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("JMSPriority is " + messageReceivedS.getJMSPriority());
      if (messageReceivedS.getJMSPriority() == priority) {
        logTrace("Pass ");
      } else {
        logMsg("StreamMessage JMSPriority Failed");
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: invalid JMSPriority returned from JMS Header");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("msgHdrJMSPriorityQTest");
    }
  }

  /*
   * @testName: msgHdrJMSExpirationQueueTest
   * 
   * @assertion_ids: JMS:SPEC:15.1; JMS:SPEC:15.2; JMS:SPEC:15.3; JMS:SPEC:140;
   * JMS:JAVADOC:309; JMS:JAVADOC:379;
   * 
   * @test_Strategy: 1. Send a message to a Queue with time to live set to 0.
   * Verify on receive that JMSExpiration gets set to 0. Test with Text, Map,
   * Object, Bytes, and Stream messages. 2. Send a message to a Queue with time
   * to live set to non-0; Verify on receive that JMSExpiration gets set
   * correctly.
   */

  public void msgHdrJMSExpirationQueueTest() throws Fault {
    boolean pass = true;
    byte bValue = 127;
    long forever = 0L;
    long timeToLive = timeout;
    String testName = "msgHdrJMSExpirationQueueTest";
    long timeBeforeSend = 0L;
    long timeAfterSend = 0L;

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      StreamMessage messageSentS = null;
      StreamMessage messageReceivedS = null;
      BytesMessage messageSentB = null;
      BytesMessage messageReceivedB = null;
      MapMessage messageReceivedM = null;
      MapMessage messageSentM = null;
      ObjectMessage messageSentO = null;
      ObjectMessage messageReceivedO = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();

      logTrace("JMSExpiration test - Send a TextMessage");
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText("sending a TextMessage");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      tool.getDefaultQueueSender().setTimeToLive(forever);
      tool.getDefaultQueueSender().send(messageSent);

      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceived.getJMSExpiration() != forever) {
        logErr("TextMessage Failed");
        pass = false;
      }

      logTrace("JMSExpiration test - Send a TextMessage");
      tool.getDefaultQueueSender().setTimeToLive(timeToLive);
      timeBeforeSend = System.currentTimeMillis();
      tool.getDefaultQueueSender().send(messageSent);
      timeAfterSend = System.currentTimeMillis();

      long exp = messageSent.getJMSExpiration();
      TestUtil.logTrace("JMSExpiration is set to=" + exp);
      TestUtil.logTrace("Time before send=" + timeBeforeSend);
      TestUtil.logTrace("Time after send=" + timeAfterSend);
      TestUtil.logTrace("Time to Live =" + timeToLive);

      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceived.getJMSExpiration() != exp) {
        TestUtil
            .logErr("TextMessage Failed: JMSExpiration didnot set correctly = "
                + messageReceived.getJMSExpiration());
        TestUtil.logErr("JMSExpiration was set to=" + exp);
        pass = false;
      }

      // send and receive Object message to Queue
      logTrace("JMSExpiration test - Send ObjectMessage to Queue.");
      messageSentO = tool.getDefaultQueueSession().createObjectMessage();
      messageSentO.setObject("msgHdrJMSExpirationQueueTest for ObjectMessage");
      messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      tool.getDefaultQueueSender().setTimeToLive(forever);
      tool.getDefaultQueueSender().send(messageSentO);
      messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedO.getJMSExpiration() != forever) {
        logMsg("ObjectMessage JMSExpiration Failed");
        pass = false;
      }

      logTrace("JMSExpiration test - Send an ObjectMessage");
      tool.getDefaultQueueSender().setTimeToLive(timeToLive);
      timeBeforeSend = System.currentTimeMillis();
      tool.getDefaultQueueSender().send(messageSentO);
      timeAfterSend = System.currentTimeMillis();

      exp = messageSentO.getJMSExpiration();
      TestUtil.logTrace("JMSExpiration is set to=" + exp);
      TestUtil.logTrace("Time before send=" + timeBeforeSend);
      TestUtil.logTrace("Time after send=" + timeAfterSend);
      TestUtil.logTrace("Time to Live =" + timeToLive);

      messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedO.getJMSExpiration() != exp) {
        logErr("ObjectMessage Failed: JMSExpiration didnot set correctly = "
            + messageReceivedO.getJMSExpiration());
        TestUtil.logErr("JMSExpiration was set to=" + exp);
        pass = false;
      }

      // send and receive map message to Queue
      logTrace("JMSExpiration test - Send MapMessage to Queue.");
      messageSentM = tool.getDefaultQueueSession().createMapMessage();
      messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      messageSentM.setString("aString", "value");
      tool.getDefaultQueueSender().setTimeToLive(forever);
      tool.getDefaultQueueSender().send(messageSentM);
      messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedM.getJMSExpiration() != forever) {
        logMsg("MapMessage JMSExpiration Failed");
        pass = false;
      }

      logTrace("JMSExpiration test - Send a MapMessage");
      tool.getDefaultQueueSender().setTimeToLive(timeToLive);
      timeBeforeSend = System.currentTimeMillis();
      tool.getDefaultQueueSender().send(messageSentM);
      timeAfterSend = System.currentTimeMillis();

      exp = messageSentM.getJMSExpiration();
      TestUtil.logTrace("JMSExpiration is set to=" + exp);
      TestUtil.logTrace("Time before send=" + timeBeforeSend);
      TestUtil.logTrace("Time after send=" + timeAfterSend);
      TestUtil.logTrace("Time to Live =" + timeToLive);

      messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedM.getJMSExpiration() != exp) {
        logErr("MapMessage Failed: JMSExpiration didnot set correctly = "
            + messageReceivedM.getJMSExpiration());
        TestUtil.logErr("JMSExpiration was set to=" + exp);
        pass = false;
      }

      // send and receive bytes message to Queue
      logTrace("JMSExpiration test - Send BytesMessage to Queue.");
      messageSentB = tool.getDefaultQueueSession().createBytesMessage();
      messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      messageSentB.writeByte(bValue);
      tool.getDefaultQueueSender().setTimeToLive(forever);
      tool.getDefaultQueueSender().send(messageSentB);
      messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedB.getJMSExpiration() != forever) {
        logMsg("BytesMessage JMSExpiration Failed");
        pass = false;
      }

      logTrace("JMSExpiration test - Send a BytesMessage");
      tool.getDefaultQueueSender().setTimeToLive(timeToLive);
      timeBeforeSend = System.currentTimeMillis();
      tool.getDefaultQueueSender().send(messageSentB);
      timeAfterSend = System.currentTimeMillis();

      exp = messageSentB.getJMSExpiration();
      TestUtil.logTrace("JMSExpiration is set to=" + exp);
      TestUtil.logTrace("Time before send=" + timeBeforeSend);
      TestUtil.logTrace("Time after send=" + timeAfterSend);
      TestUtil.logTrace("Time to Live =" + timeToLive);

      messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedB.getJMSExpiration() != exp) {
        logErr("BytesMessage Failed: JMSExpiration didnot set correctly = "
            + messageReceivedB.getJMSExpiration());
        TestUtil.logErr("JMSExpiration was set to=" + exp);
        pass = false;
      }

      // Send and receive a StreamMessage
      logTrace("JMSExpiration test - sending a StreamMessage");
      messageSentS = tool.getDefaultQueueSession().createStreamMessage();
      messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      messageSentS.writeString("Testing...");
      tool.getDefaultQueueSender().setTimeToLive(forever);
      tool.getDefaultQueueSender().send(messageSentS);
      messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedS.getJMSExpiration() != forever) {
        logMsg("StreamMessage JMSExpiration Failed");
        pass = false;
      }

      logTrace("JMSExpiration test - Send a StreamMessage");
      tool.getDefaultQueueSender().setTimeToLive(timeToLive);
      timeBeforeSend = System.currentTimeMillis();
      tool.getDefaultQueueSender().send(messageSentS);
      timeAfterSend = System.currentTimeMillis();

      exp = messageSentS.getJMSExpiration();
      TestUtil.logTrace("JMSExpiration is set to=" + exp);
      TestUtil.logTrace("Time before send=" + timeBeforeSend);
      TestUtil.logTrace("Time after send=" + timeAfterSend);
      TestUtil.logTrace("Time to Live =" + timeToLive);

      messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedS.getJMSExpiration() != exp) {
        logErr("StreamMessage Failed: JMSExpiration didnot set correctly = "
            + messageReceivedS.getJMSExpiration());
        TestUtil.logErr("JMSExpiration was set to=" + exp);
        pass = false;
      }

      if (!pass) {
        throw new Fault(
            "Error: invalid JMSExpiration returned from JMS Header");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault(testName);
    }
  }

  /*
   * @testName: msgHdrJMSDestinationQTest
   * 
   * @assertion_ids: JMS:SPEC:2; JMS:JAVADOC:363; JMS:JAVADOC:286;
   * 
   * @test_Strategy: Create and send a message to the default Queue. Receive msg
   * and verify that JMSDestination is set to the default Queue test with Text,
   * map, object, byte, and stream messages
   */

  public void msgHdrJMSDestinationQTest() throws Fault {
    boolean pass = true;
    byte bValue = 127;
    Queue replyDestination = null;

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      StreamMessage messageSentS = null;
      StreamMessage messageReceivedS = null;
      BytesMessage messageSentB = null;
      BytesMessage messageReceivedB = null;
      MapMessage messageReceivedM = null;
      MapMessage messageSentM = null;
      ObjectMessage messageSentO = null;
      ObjectMessage messageReceivedO = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText("sending a message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSDestinationQTest");
      logTrace("Send TextMessage to Queue.");
      tool.getDefaultQueueSender().send(messageSent);
      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("JMSDestination:  " + messageReceived.getJMSDestination());
      replyDestination = (Queue) messageReceived.getJMSDestination();
      if (replyDestination == null) {
        pass = false;
        logMsg("TextMessage Error: JMSDestination returned a  null");
      } else {
        logTrace("Queue name is " + replyDestination.getQueueName());
        if (replyDestination.getQueueName()
            .equals(tool.getDefaultQueue().getQueueName())) {
          logTrace("Pass ");
        } else {
          logMsg("TextMessage Failed");
          pass = false;
        }
      }

      // send and receive Object message to Queue
      logTrace("Send ObjectMessage to Queue.");
      messageSentO = tool.getDefaultQueueSession().createObjectMessage();
      messageSentO.setObject("msgHdrJMSDestinationQTest for ObjectMessage");
      messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSDestinationQTest");
      tool.getDefaultQueueSender().send(messageSentO);
      messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("JMSDestination:  " + messageReceivedO.getJMSDestination());
      replyDestination = (Queue) messageReceived.getJMSDestination();
      if (replyDestination == null) {
        pass = false;
        logMsg("ObjectMessage Error: JMSDestination returned a  null");
      } else {
        logTrace("Queue name is " + replyDestination.getQueueName());
        if (replyDestination.getQueueName()
            .equals(tool.getDefaultQueue().getQueueName())) {
          logTrace("Pass ");
        } else {
          logMsg("ObjectMessage Failed");
          pass = false;
        }
      }

      // send and receive map message to Queue
      logTrace("Send MapMessage to Queue.");
      messageSentM = tool.getDefaultQueueSession().createMapMessage();
      messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSDestinationQTest");
      messageSentM.setString("aString", "value");
      tool.getDefaultQueueSender().send(messageSentM);
      messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("JMSDestination:  " + messageReceivedM.getJMSDestination());
      replyDestination = (Queue) messageReceived.getJMSDestination();
      if (replyDestination == null) {
        pass = false;
        logMsg("MapMessage Error: JMSDestination returned a  null");
      } else {
        logTrace("Queue name is " + replyDestination.getQueueName());
        if (replyDestination.getQueueName()
            .equals(tool.getDefaultQueue().getQueueName())) {
          logTrace("Pass ");
        } else {
          logMsg("MapMessage Failed");
          pass = false;
        }
      }

      // send and receive bytes message to Queue
      logTrace("Send BytesMessage to Queue.");
      messageSentB = tool.getDefaultQueueSession().createBytesMessage();
      messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSDestinationQTest");
      messageSentB.writeByte(bValue);
      tool.getDefaultQueueSender().send(messageSentB);
      messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("JMSDestination:  " + messageReceivedB.getJMSDestination());
      replyDestination = (Queue) messageReceived.getJMSDestination();
      if (replyDestination == null) {
        pass = false;
        logMsg("BytesMessage Error: JMSDestination returned a  null");
      } else {
        logTrace("Queue name is " + replyDestination.getQueueName());
        if (replyDestination.getQueueName()
            .equals(tool.getDefaultQueue().getQueueName())) {
          logTrace("Pass ");
        } else {
          logMsg("BytesMessage Failed");
          pass = false;
        }
      }

      // Send and receive a StreamMessage
      logTrace("sending a StreamMessage");
      messageSentS = tool.getDefaultQueueSession().createStreamMessage();
      messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgHdrJMSDestinationQTest");
      messageSentS.writeString("Testing...");
      logTrace("Sending StreamMessage");
      tool.getDefaultQueueSender().send(messageSentS);
      messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("JMSDestination:  " + messageReceivedS.getJMSDestination());
      replyDestination = (Queue) messageReceived.getJMSDestination();
      if (replyDestination == null) {
        pass = false;
        logMsg("StreamMessage Error: JMSDestination returned a  null");
      } else {
        logTrace("Queue name is " + replyDestination.getQueueName());
        if (replyDestination.getQueueName()
            .equals(tool.getDefaultQueue().getQueueName())) {
          logTrace("Pass ");
        } else {
          logMsg("StreamMessage Failed");
          pass = false;
        }
      }
      if (!pass) {
        throw new Fault(
            "Error: invalid JMSDestination returned from JMS Header");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("msgHdrJMSDestinationQTest");
    }
  }

  /*
   * @testName: msgHdrJMSDeliveryModeQTest
   * 
   * @assertion_ids: JMS:SPEC:3; JMS:SPEC:140; JMS:SPEC:246.2; JMS:JAVADOC:301;
   * JMS:JAVADOC:367; JMS:JAVADOC:369;
   * 
   * @test_Strategy: 1. Create and send a message to the default Queue. Receive
   * the msg and verify that JMSDeliveryMode is set the default delivery mode of
   * persistent. 2. Create and test another message with a nonpersistent
   * delivery mode. Test with Text, map, object, byte, and stream messages 3.
   * Set JMSDeliveryMode to Message after receive. Verify that JMSDeliveryMode
   * is set correctly.
   */

  public void msgHdrJMSDeliveryModeQTest() throws Fault {
    boolean pass = true;
    byte bValue = 127;
    String testName = "msgHdrJMSDeliveryModeQTest";

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      StreamMessage messageSentS = null;
      StreamMessage messageReceivedS = null;
      BytesMessage messageSentB = null;
      BytesMessage messageReceivedB = null;
      MapMessage messageReceivedM = null;
      MapMessage messageSentM = null;
      ObjectMessage messageSentO = null;
      ObjectMessage messageReceivedO = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();

      logTrace("send TextMessage to Queue.");
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      tool.getDefaultQueueSender().send(messageSent);

      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceived.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
        pass = false;
        logErr(
            "TextMessage Error: JMSDeliveryMode should be set to persistent as default");
      }

      messageReceived.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
      if (messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
        pass = false;
        logErr(
            "TextMessage Error: JMSDeliveryMode not set correctly by setJMSDeliveryMode");
      }

      // send and receive Object message to Queue
      logTrace("send ObjectMessage to Queue.");
      messageSentO = tool.getDefaultQueueSession().createObjectMessage();
      messageSentO.setObject("Test for ObjectMessage");
      messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      tool.getDefaultQueueSender().send(messageSentO);

      messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedO.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
        pass = false;
        logMsg(
            "ObjectMessage Error: JMSDeliveryMode should be set to persistent as default");
      }

      messageReceivedO.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
      if (messageReceivedO
          .getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
        pass = false;
        logErr(
            "ObjectMessage Error: JMSDeliveryMode not set correctly by setJMSDeliveryMode");
      }

      // send and receive map message to Queue
      logTrace("send MapMessage to Queue.");
      messageSentM = tool.getDefaultQueueSession().createMapMessage();
      messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      messageSentM.setString("aString", "value");
      tool.getDefaultQueueSender().send(messageSentM);

      messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedM.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
        pass = false;
        logMsg(
            "MapMessage Error: JMSDeliveryMode should be set to persistent as default");
      }

      messageReceivedM.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
      if (messageReceivedM
          .getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
        pass = false;
        logErr(
            "MapMessage Error: JMSDeliveryMode not set correctly by setJMSDeliveryMode");
      }

      // send and receive bytes message to Queue
      logTrace("send BytesMessage to Queue.");
      messageSentB = tool.getDefaultQueueSession().createBytesMessage();
      messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      messageSentB.writeByte(bValue);
      tool.getDefaultQueueSender().send(messageSentB);

      messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedB.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
        pass = false;
        logErr(
            "BytesMessage Error: JMSDeliveryMode should be set to persistent as default");
      }

      messageReceivedB.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
      if (messageReceivedB
          .getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
        pass = false;
        logErr(
            "BytesMessage Error: JMSDeliveryMode not set correctly by setJMSDeliveryMode");
      }

      // send and receive a StreamMessage
      logTrace("sending a StreamMessage");
      messageSentS = tool.getDefaultQueueSession().createStreamMessage();
      messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      messageSentS.writeString("Testing...");
      tool.getDefaultQueueSender().send(messageSentS);

      messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedS.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
        pass = false;
        logErr(
            "StreamMessage Error: JMSDeliveryMode should be set to persistent as default");
      }

      messageReceivedS.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
      if (messageReceivedS
          .getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
        pass = false;
        logErr(
            "StreamMessage Error: JMSDeliveryMode not set correctly by setJMSDeliveryMode");
      }

      // Test again - this time set delivery mode to persistent
      logTrace("send TextMessage to Queue.");
      tool.getDefaultQueueSender().setDeliveryMode(DeliveryMode.NON_PERSISTENT);
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText("sending a message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      tool.getDefaultQueueSender().send(messageSent);

      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceived.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
        pass = false;
        logErr(
            "TextMessage Error: JMSDeliveryMode should be set to NON_PERSISTENT");
      }

      messageReceived.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
      if (messageReceived.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
        pass = false;
        logErr(
            "TextMessage Error: JMSDeliveryMode not set correctly by setJMSDeliveryMode");
      }

      // send and receive Object message to Queue
      logTrace("send ObjectMessage to Queue.");
      messageSentO = tool.getDefaultQueueSession().createObjectMessage();
      messageSentO.setObject("msgHdrJMSDeliveryModeQTest for ObjectMessage");
      messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      tool.getDefaultQueueSender().send(messageSentO);

      messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedO
          .getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
        pass = false;
        logErr(
            "ObjectMessage Error: JMSDeliveryMode should be set to NON_PERSISTENT");
      }

      messageReceivedO.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
      if (messageReceivedO.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
        pass = false;
        logErr(
            "ObjectMessage Error: JMSDeliveryMode not set correctly by setJMSDeliveryMode");
      }

      // send and receive map message to Queue
      logTrace("send MapMessage to Queue.");
      messageSentM = tool.getDefaultQueueSession().createMapMessage();
      messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      messageSentM.setString("aString", "value");
      tool.getDefaultQueueSender().send(messageSentM);

      messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedM
          .getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
        pass = false;
        logErr(
            "MapMessage Error: JMSDeliveryMode should be set to NON_PERSISTENT");
      }

      messageReceivedM.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
      if (messageReceivedM.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
        pass = false;
        logErr(
            "MapMessage Error: JMSDeliveryMode not set correctly by setJMSDeliveryMode");
      }

      // send and receive bytes message to Queue
      logTrace("send BytesMessage to Queue.");
      messageSentB = tool.getDefaultQueueSession().createBytesMessage();
      messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      messageSentB.writeByte(bValue);
      tool.getDefaultQueueSender().send(messageSentB);

      messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedB
          .getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
        pass = false;
        logErr(
            "BytesMessage Error: JMSDeliveryMode should be set to NON_PERSISTENT");
      }

      messageReceivedB.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
      if (messageReceivedB.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
        pass = false;
        logErr(
            "BytesMessage Error: JMSDeliveryMode not set correctly by setJMSDeliveryMode");
      }

      // send and receive a StreamMessage
      logTrace("sending a StreamMessage");
      messageSentS = tool.getDefaultQueueSession().createStreamMessage();
      messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      messageSentS.writeString("Testing...");
      tool.getDefaultQueueSender().send(messageSentS);

      messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedS
          .getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT) {
        pass = false;
        logErr(
            "StreamMessage Error: JMSDeliveryMode should be set to NON_PERSISTENT");
      }

      messageReceivedS.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
      if (messageReceivedS.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
        pass = false;
        logErr(
            "StreamMessage Error: JMSDeliveryMode not set correctly by setJMSDeliveryMode");
      }

      if (!pass) {
        throw new Fault(
            "Error: invalid JMSDeliveryMode returned from JMS Header");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault(testName);
    }
  }

  /*
   * @testName: msgHdrJMSRedeliveredTest
   * 
   * @assertion_ids: JMS:JAVADOC:371; JMS:JAVADOC:373;
   * 
   * @test_Strategy: 1. Create and send a message to the default Queue. Verify
   * at the receive that JMSRedelivered is false; 3. Set JMSRedelivered to true
   * after receive. Verify that JMSRedelivered is set correctly. Test with Text,
   * Map, Object, Bytes, and Stream messages
   */

  public void msgHdrJMSRedeliveredTest() throws Fault {
    boolean pass = true;
    byte bValue = 127;
    String testName = "msgHdrJMSRedeliveredTest";

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      StreamMessage messageSentS = null;
      StreamMessage messageReceivedS = null;
      BytesMessage messageSentB = null;
      BytesMessage messageReceivedB = null;
      MapMessage messageReceivedM = null;
      MapMessage messageSentM = null;
      ObjectMessage messageSentO = null;
      ObjectMessage messageReceivedO = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();

      logTrace("send TextMessage to Queue.");
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      tool.getDefaultQueueSender().send(messageSent);

      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceived.getJMSRedelivered() != false) {
        pass = false;
        logErr("TextMessage Error: JMSRedelivered should be false");
      }

      messageReceived.setJMSRedelivered(true);
      if (messageReceived.getJMSRedelivered() != true) {
        pass = false;
        logErr(
            "TextMessage Error: JMSRedelivered not set correctly by setJMSRedelivered");
      }

      // send and receive Object message to Queue
      logTrace("send ObjectMessage to Queue.");
      messageSentO = tool.getDefaultQueueSession().createObjectMessage();
      messageSentO.setObject("Test for ObjectMessage");
      messageSentO.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      tool.getDefaultQueueSender().send(messageSentO);

      messageReceivedO = (ObjectMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedO.getJMSRedelivered() != false) {
        pass = false;
        logErr("ObjectMessage Error: JMSRedelivered should be false");
      }

      messageReceivedO.setJMSRedelivered(true);
      if (messageReceivedO.getJMSRedelivered() != true) {
        pass = false;
        logErr(
            "ObjectMessage Error: JMSRedelivered not set correctly by setJMSRedelivered");
      }

      // send and receive map message to Queue
      logTrace("send MapMessage to Queue.");
      messageSentM = tool.getDefaultQueueSession().createMapMessage();
      messageSentM.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      messageSentM.setString("aString", "value");
      tool.getDefaultQueueSender().send(messageSentM);

      messageReceivedM = (MapMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedM.getJMSRedelivered() != false) {
        pass = false;
        logErr("MapMessage Error: JMSRedelivered should be false");
      }

      messageReceivedM.setJMSRedelivered(true);
      if (messageReceivedM.getJMSRedelivered() != true) {
        pass = false;
        logErr(
            "MapMessage Error: JMSRedelivered not set correctly by setJMSRedelivered");
      }

      // send and receive bytes message to Queue
      logTrace("send BytesMessage to Queue.");
      messageSentB = tool.getDefaultQueueSession().createBytesMessage();
      messageSentB.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      messageSentB.writeByte(bValue);
      tool.getDefaultQueueSender().send(messageSentB);

      messageReceivedB = (BytesMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedB.getJMSRedelivered() != false) {
        pass = false;
        logErr("BytesMessage Error: JMSRedelivered should be false");
      }

      messageReceivedB.setJMSRedelivered(true);
      if (messageReceivedB.getJMSRedelivered() != true) {
        pass = false;
        logErr(
            "BytesMessage Error: JMSRedelivered not set correctly by setJMSRedelivered");
      }

      // send and receive a StreamMessage
      logTrace("sending a StreamMessage");
      messageSentS = tool.getDefaultQueueSession().createStreamMessage();
      messageSentS.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      messageSentS.writeString("Testing...");
      tool.getDefaultQueueSender().send(messageSentS);

      messageReceivedS = (StreamMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceivedS.getJMSRedelivered() != false) {
        pass = false;
        logErr("StreamMessage Error: JMSRedelivered should be false");
      }

      messageReceivedS.setJMSRedelivered(true);
      if (messageReceivedS.getJMSRedelivered() != true) {
        pass = false;
        logErr(
            "StreamMessage Error: JMSRedelivered not set correctly by setJMSRedelivered");
      }

      if (!pass) {
        throw new Fault(
            "Error: invalid JMSDeliveryMode returned from JMS Header");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault(testName);
    }
  }
}
