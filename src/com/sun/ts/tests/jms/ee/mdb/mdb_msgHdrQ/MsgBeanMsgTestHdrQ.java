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

package com.sun.ts.tests.jms.ee.mdb.mdb_msgHdrQ;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Properties;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.naming.*;
import javax.jms.*;
import java.sql.*;
import javax.sql.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jms.common.*;
import com.sun.ts.tests.jms.commonee.*;

public class MsgBeanMsgTestHdrQ implements MessageDrivenBean, MessageListener {

  // properties object needed for logging, get this from the message object
  // passed into
  // the onMessage method.
  private java.util.Properties p = null;

  private TSNamingContext context = null;

  private MessageDrivenContext mdc = null;

  // JMS
  private QueueConnectionFactory qFactory = null;

  private QueueConnection qConnection = null;

  private Queue queueR = null;

  private Queue queue = null;

  private QueueSender mSender = null;

  private QueueSession qSession = null;

  private TextMessage messageSent = null;

  private StreamMessage messageSentStreamMessage = null;

  private BytesMessage messageSentBytesMessage = null;

  private MapMessage messageSentMapMessage = null;

  private ObjectMessage messageSentObjectMessage = null;

  public MsgBeanMsgTestHdrQ() {
    TestUtil.logTrace("@MsgBeanMsgTestHdrQ()!");
  };

  public void ejbCreate() {
    TestUtil.logTrace(
        "jms.ee.mdb.mdb_msgHdrQ  - @MsgBeanMsgTestHdrQ-ejbCreate() !!");
    try {
      context = new TSNamingContext();
      qFactory = (QueueConnectionFactory) context
          .lookup("java:comp/env/jms/MyQueueConnectionFactory");
      if (qFactory == null) {
        TestUtil.logTrace("qFactory error");
      }

      queueR = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
      if (queueR == null) {
        TestUtil.logTrace("queueR error");
      }
      queue = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("MDB ejbCreate Error!", e);
    }
  }

  public void onMessage(Message msg) {

    try {
      p = new Properties();
      JmsUtil.initHarnessProps(msg, p);

      TestUtil.logTrace("from jms.ee.mdb.mdb_msgHdrQ @onMessage!" + msg);

      TestUtil.logTrace(
          "onMessage will run TestCase: " + msg.getStringProperty("TestCase"));
      qConnection = qFactory.createQueueConnection();
      if (qConnection == null) {
        throw new Exception("Null QueueConnection created");
      }
      qConnection.start();

      qSession = qConnection.createQueueSession(true, 0);

      // create testmessages
      Vector mVec = new Vector();
      messageSent = qSession.createTextMessage();
      mVec.addElement(messageSent);
      messageSentStreamMessage = qSession.createStreamMessage();
      mVec.addElement(messageSentStreamMessage);
      messageSentBytesMessage = qSession.createBytesMessage();
      mVec.addElement(messageSentBytesMessage);
      messageSentMapMessage = qSession.createMapMessage();
      mVec.addElement(messageSentMapMessage);
      messageSentObjectMessage = qSession.createObjectMessage();
      mVec.addElement(messageSentObjectMessage);

      // for each message addPropsToMessage
      Enumeration vNum = mVec.elements();
      while (vNum.hasMoreElements()) {
        JmsUtil.addPropsToMessage((Message) vNum.nextElement(), p);
      }

      // myTestUtil.logTrace - for trace logging messages

      if (msg.getStringProperty("TestCase").equals("msgHdrTimeStampQTest")) {
        TestUtil.logTrace(
            "@onMessage - running msgHdrTimeStampQTest - create the message");
        msgHdrTimeStampQTest();
      } else if (msg.getStringProperty("TestCase").equals("dummy")) {
        TestUtil.logTrace("@onMessage - ignore this!");
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrCorlIdQTextTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrCorlIdQTextTestCreate!");
        msgHdrCorlIdQTextTestCreate();
      }

      else if (msg.getStringProperty("TestCase")
          .equals("msgHdrCorlIdQTextTest")) {
        TestUtil.logTrace("@onMessage - msgHdrCorlIdQTextTest!");
        msgHdrCorlIdQTest(msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrCorlIdQBytesTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrCorlIdQBytesTestCreate!");
        msgHdrCorlIdQBytesTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrCorlIdQBytesTest")) {
        TestUtil.logTrace("@onMessage -msgHdrCorlIdQBytesTest!");
        msgHdrCorlIdQTest(msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrCorlIdQMapTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrCorlIdQMapTestCreate!");
        msgHdrCorlIdQMapTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrCorlIdQMapTest")) {
        TestUtil.logTrace("@onMessage - msgHdrCorlIdQMapTest!");
        msgHdrCorlIdQTest(msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrCorlIdQStreamTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrCorlIdQStreamTestCreate!");
        msgHdrCorlIdQStreamTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrCorlIdQStreamTest")) {
        TestUtil.logTrace("@onMessage - msgHdrCorlIdQStreamTest!");
        msgHdrCorlIdQTest(msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrCorlIdQObjectTestCreate")) {
        TestUtil.logTrace("@onMessage -msgHdrCorlIdQObjectTestCreate!");
        msgHdrCorlIdQObjectTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrCorlIdQObjectTest")) {
        TestUtil.logTrace("@onMessage - msgHdrCorlIdQObjectTest!");
        msgHdrCorlIdQTest(msg);
      }

      else if (msg.getStringProperty("TestCase")
          .equals("msgHdrReplyToQTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrReplyToQTestCreate!");
        msgHdrReplyToQTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrReplyToQTest")) {
        TestUtil.logTrace("@onMessage - msgHdrReplyToQTest!");
        msgHdrReplyToQTest(msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrJMSTypeQTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrJMSTypeQTestCreate!");
        msgHdrJMSTypeQTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrJMSTypeQTest")) {
        TestUtil.logTrace("@onMessage - msgHdrJMSTypeQTest!");
        msgHdrJMSTypeQTest(msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrJMSPriorityQTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrJMSPriorityQTestCreate!");
        msgHdrJMSPriorityQTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrJMSPriorityQTest")) {
        TestUtil.logTrace("@onMessage - msgHdrJMSPriorityQTest!");
        msgHdrJMSPriorityQTest(msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrJMSExpirationQueueTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrJMSExpirationQueueTestCreate!");
        msgHdrJMSExpirationQueueTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrJMSExpirationQueueTest")) {
        TestUtil.logTrace("@onMessage - msgHdrJMSExpirationQueueTest!");
        msgHdrJMSExpirationQueueTest(msg);
      }

      else if (msg.getStringProperty("TestCase")
          .equals("msgHdrJMSDestinationQTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrJMSDestinationQTestCreate!");
        msgHdrJMSDestinationQTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrJMSDestinationQTest")) {
        TestUtil.logTrace("@onMessage - msgHdrJMSDestinationQTest!");
        msgHdrJMSDestinationQTest(msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrJMSDeliveryModeQTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrJMSDeliveryModeQTestCreate!");
        msgHdrJMSDeliveryModeQTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrJMSDeliveryModeQTest")) {
        TestUtil.logTrace("@onMessage - msgHdrJMSDeliveryModeQTest!");
        msgHdrJMSDeliveryModeQTest(msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrIDQTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrIDQTestCreate!");
        msgHdrIDQTestCreate();
      } else if (msg.getStringProperty("TestCase").equals("msgHdrIDQTest")) {
        TestUtil.logTrace("@onMessage - msgHdrIDQTest!");
        msgHdrIDQTest(msg);
      }

      else {
        TestUtil.logTrace(
            "@onMessage - invalid message type found in StringProperty");
        TestUtil.logTrace("Do not have a method for this testcase: "
            + msg.getStringProperty("TestCase"));
      }

      TestUtil.logTrace("@onMessage - Finished for this test!");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    } finally {
      if (qConnection != null) {
        try {
          qConnection.close();
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
        }
      }
    }

  }

  /*
   * Description: Send a single Text, map, bytes, stream, and object message
   * check time of send against time send returns JMSTimeStamp should be between
   * these two
   */
  public void msgHdrTimeStampQTest() {
    boolean pass = true;
    long timeBeforeSend;
    long timeAfterSend;
    byte bValue = 127;
    String id = null;
    String testCase = "msgHdrTimeStampQTest";
    try {

      // send Object message
      TestUtil.logTrace("Send ObjectMessage to Queue.");
      messageSentObjectMessage
          .setObject("msgHdrTimeStampQTest for Object Message");
      messageSentObjectMessage.setStringProperty("TestCase", "dummy");

      // get the current time in milliseconds - before and after the send
      timeBeforeSend = System.currentTimeMillis();
      mSender = qSession.createSender(queue);
      mSender.send(messageSentObjectMessage);
      // message has been sent
      timeAfterSend = System.currentTimeMillis();
      TestUtil.logTrace(" getJMSTimestamp");
      TestUtil.logTrace(" " + messageSentObjectMessage.getJMSTimestamp());
      TestUtil.logTrace("Time at send is: " + timeBeforeSend);
      TestUtil.logTrace("Time after return fromsend is:" + timeAfterSend);
      if ((timeBeforeSend <= messageSentObjectMessage.getJMSTimestamp())
          && (timeAfterSend >= messageSentObjectMessage.getJMSTimestamp())) {
        TestUtil.logTrace("Object Message TimeStamp pass");
      } else {
        TestUtil.logMsg("Error: invalid timestamp from ObjectMessage");
        pass = false;
      }

      // send map message to Queue
      TestUtil.logTrace("Send MapMessage to Queue.");

      messageSentMapMessage.setStringProperty("TestCase", "dummy");
      messageSentMapMessage.setString("aString", "value");

      // get the current time in milliseconds - before and after the send
      timeBeforeSend = System.currentTimeMillis();
      mSender.send(messageSentMapMessage);
      // message has been sent
      timeAfterSend = System.currentTimeMillis();

      TestUtil.logTrace(" getJMSTimestamp");
      TestUtil.logTrace(" " + messageSentMapMessage.getJMSTimestamp());
      TestUtil.logTrace("Time at send is: " + timeBeforeSend);
      TestUtil.logTrace("Time after return fromsend is:" + timeAfterSend);
      if ((timeBeforeSend <= messageSentMapMessage.getJMSTimestamp())
          && (timeAfterSend >= messageSentMapMessage.getJMSTimestamp())) {
        TestUtil.logTrace("MapMessage TimeStamp pass");
      } else {
        TestUtil.logMsg("Error: invalid timestamp from MapMessage");
        pass = false;
      }

      // send bytes message

      TestUtil.logTrace("Send BytesMessage to Queue.");

      messageSentBytesMessage.setStringProperty("TestCase", "dummy");

      messageSentBytesMessage.writeByte(bValue);

      // get the current time in milliseconds - before and after the send
      timeBeforeSend = System.currentTimeMillis();
      mSender.send(messageSentBytesMessage);
      // message has been sent
      timeAfterSend = System.currentTimeMillis();

      TestUtil.logTrace(" getJMSTimestamp");
      TestUtil.logTrace(" " + messageSentBytesMessage.getJMSTimestamp());
      TestUtil.logTrace("Time at send is: " + timeBeforeSend);
      TestUtil.logTrace("Time after return fromsend is:" + timeAfterSend);
      if ((timeBeforeSend <= messageSentBytesMessage.getJMSTimestamp())
          && (timeAfterSend >= messageSentBytesMessage.getJMSTimestamp())) {
        TestUtil.logTrace("BytesMessage TimeStamp pass");
      } else {
        TestUtil.logMsg("Error: invalid timestamp from BytesMessage");
        pass = false;
      }

      // Send a StreamMessage
      TestUtil.logTrace("sending a Stream message");

      messageSentStreamMessage.setStringProperty("TestCase", "dummy");

      messageSentStreamMessage.writeString("Testing...");
      TestUtil.logTrace("Sending message");

      // get the current time in milliseconds - before and after the send
      timeBeforeSend = System.currentTimeMillis();
      mSender.send(messageSentStreamMessage);
      // message has been sent
      timeAfterSend = System.currentTimeMillis();

      TestUtil.logTrace(" getJMSTimestamp");
      TestUtil.logTrace(" " + messageSentStreamMessage.getJMSTimestamp());
      TestUtil.logTrace("Time at send is: " + timeBeforeSend);
      TestUtil.logTrace("Time after return fromsend is:" + timeAfterSend);
      if ((timeBeforeSend <= messageSentStreamMessage.getJMSTimestamp())
          && (timeAfterSend >= messageSentStreamMessage.getJMSTimestamp())) {
        TestUtil.logTrace("StreamMessage TimeStamp pass");
      } else {
        TestUtil.logMsg("Error: invalid timestamp from StreamMessage");
        pass = false;
      }

      // Text Message

      messageSent.setText("sending a Text message");
      messageSent.setStringProperty("TestCase", "dummy");

      TestUtil.logTrace("sending a Text message");

      // get the current time in milliseconds - before and after the send
      timeBeforeSend = System.currentTimeMillis();
      mSender.send(messageSent);
      // message has been sent
      timeAfterSend = System.currentTimeMillis();

      TestUtil.logTrace(" getJMSTimestamp");
      TestUtil.logTrace(" " + messageSent.getJMSTimestamp());
      TestUtil.logTrace("Time at send is: " + timeBeforeSend);
      TestUtil.logTrace("Time after return fromsend is:" + timeAfterSend);
      if ((timeBeforeSend <= messageSent.getJMSTimestamp())
          && (timeAfterSend >= messageSent.getJMSTimestamp())) {
        TestUtil.logTrace("TextMessage TimeStamp pass");
      } else {
        TestUtil.logMsg("Error: invalid timestamp from TextMessage");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    } finally {
      sendTestResults(testCase, pass);
    }
  }

  /*
   * Description: create a text msg, set correlation id
   */
  private void msgHdrCorlIdQTextTestCreate() {

    String jmsCorrelationID = "test Correlation id";
    try {

      messageSent.setText("sending a message");
      messageSent.setStringProperty("TestCase", "msgHdrCorlIdQTextTest");

      TestUtil.logTrace("Send Text Message to Queue.");
      messageSent.setJMSCorrelationID(jmsCorrelationID);
      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: create a stream msg, set correlation id
   */
  private void msgHdrCorlIdQStreamTestCreate() {

    String jmsCorrelationID = "test Correlation id";
    try {

      messageSentStreamMessage.setStringProperty("TestCase",
          "msgHdrCorlIdQStreamTest");
      messageSentStreamMessage.setJMSCorrelationID(jmsCorrelationID);
      messageSentStreamMessage.writeString("Testing...");
      TestUtil.logTrace("Sending Stream message");
      mSender = qSession.createSender(queue);
      mSender.send(messageSentStreamMessage);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: create a bytes msg, set correlation id
   */
  private void msgHdrCorlIdQBytesTestCreate() {
    byte bValue = 127;

    String jmsCorrelationID = "test Correlation id";
    try {

      TestUtil.logTrace("Send BytesMessage to Queue.");

      messageSentBytesMessage.setStringProperty("TestCase",
          "msgHdrCorlIdQBytesTest");
      messageSentBytesMessage.setJMSCorrelationID(jmsCorrelationID);
      messageSentBytesMessage.writeByte(bValue);

      mSender = qSession.createSender(queue);
      mSender.send(messageSentBytesMessage);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: create a mao msg, set correlation id
   */
  private void msgHdrCorlIdQMapTestCreate() {

    String jmsCorrelationID = "test Correlation id";
    try {

      TestUtil.logTrace("Send MapMessage to Queue.");

      messageSentMapMessage.setStringProperty("TestCase",
          "msgHdrCorlIdQMapTest");
      messageSentMapMessage.setJMSCorrelationID(jmsCorrelationID);
      messageSentMapMessage.setString("aString", "value");

      mSender = qSession.createSender(queue);
      mSender.send(messageSentMapMessage);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: create an object msg, set correlation id
   */
  private void msgHdrCorlIdQObjectTestCreate() {

    String jmsCorrelationID = "test Correlation id";
    try {
      TestUtil.logTrace("Send ObjectMessage to Queue.");

      messageSentObjectMessage
          .setObject("msgHdrIDQObjectTest for Object Message");
      messageSentObjectMessage.setStringProperty("TestCase",
          "msgHdrCorlIdQObjectTest");
      messageSentObjectMessage.setJMSCorrelationID(jmsCorrelationID);

      mSender = qSession.createSender(queue);
      mSender.send(messageSentObjectMessage);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }

  }

  public void msgHdrCorlIdQTest(javax.jms.Message messageReceived) {
    boolean pass = true;
    String jmsCorrelationID = "test Correlation id";
    try {
      TestUtil.logTrace(
          "jmsCorrelationID:  " + messageReceived.getJMSCorrelationID());
      if (messageReceived.getJMSCorrelationID() == null) {
        pass = false;
        TestUtil
            .logMsg("Text Message Error: JMSCorrelationID returned a  null");
      } else if (messageReceived.getJMSCorrelationID()
          .equals(jmsCorrelationID)) {
        TestUtil.logTrace("pass");
      } else {
        pass = false;
        TestUtil.logMsg("Text Message Error: JMSCorrelationID is incorrect");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    } finally {
      try {
        sendTestResults(messageReceived.getStringProperty("TestCase"), pass);
      } catch (Exception j) {
        TestUtil.printStackTrace(j);
      }
    }
  }

  /*
   *
   */
  public void msgHdrReplyToQTestCreate() {
    Queue replyQueue = null;
    try {

      messageSent.setText("sending a message");
      messageSent.setStringProperty("TestCase", "msgHdrReplyToQTest");
      messageSent.setJMSReplyTo(queue);

      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  public void msgHdrReplyToQTest(javax.jms.Message messageReceived) {
    boolean pass = true;
    Queue replyQueue = null;
    String testCase = "msgHdrReplyToQTest";
    try {
      replyQueue = (Queue) messageReceived.getJMSReplyTo();
      TestUtil.logTrace("Queue name is " + replyQueue.getQueueName());
      if (replyQueue.getQueueName().equals(queue.getQueueName())) {
        TestUtil.logTrace("Pass ");
      } else {
        TestUtil.logMsg("ReplyTo Failed");
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    } finally {
      sendTestResults(testCase, pass);
    }
  }

  public void msgHdrJMSTypeQTestCreate() {
    boolean pass = true;
    byte bValue = 127;
    String type = "TESTMSG";
    try {
      messageSent.setText("sending a message");
      messageSent.setStringProperty("TestCase", "msgHdrJMSTypeQTest");
      TestUtil.logTrace("JMSType test - Send a Text message");
      messageSent.setJMSType(type);
      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  public void msgHdrJMSTypeQTest(javax.jms.Message messageReceived) {
    boolean pass = true;
    String type = "TESTMSG";
    String testCase = "msgHdrJMSTypeQTest";
    try {

      TestUtil.logTrace("JMSType is " + (String) messageReceived.getJMSType());
      if (messageReceived.getJMSType().equals(type)) {
        TestUtil.logTrace("Pass");
      } else {
        TestUtil.logMsg("Text Message Failed");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    } finally {
      sendTestResults(testCase, pass);
    }
  }

  public void msgHdrJMSPriorityQTestCreate() {
    boolean pass = true;
    byte bValue = 127;
    int priority = 2;
    try {

      messageSent.setText("sending a message");
      messageSent.setStringProperty("TestCase", "msgHdrJMSPriorityQTest");
      TestUtil.logTrace("JMSPriority test - Send a Text message");
      mSender = qSession.createSender(queue);
      mSender.setPriority(priority);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  public void msgHdrJMSPriorityQTest(javax.jms.Message messageReceived) {
    boolean pass = true;
    int priority = 2;
    String testCase = "msgHdrJMSPriorityQTest";
    try {
      TestUtil.logTrace("JMSPriority is " + messageReceived.getJMSPriority());
      if (messageReceived.getJMSPriority() == priority) {
        TestUtil.logTrace("Pass ");
      } else {
        TestUtil.logMsg("JMSPriority test Failed");
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    } finally {
      sendTestResults(testCase, pass);
    }
  }

  public void msgHdrJMSExpirationQueueTestCreate() {
    boolean pass = true;
    long forever = 0;
    try {

      messageSent.setText("sending a message");
      messageSent.setStringProperty("TestCase", "msgHdrJMSExpirationQueueTest");
      TestUtil.logTrace("JMSExpiration test - Send a Text message");

      mSender = qSession.createSender(queue);
      mSender.setTimeToLive(forever);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  public void msgHdrJMSExpirationQueueTest(javax.jms.Message messageReceived) {
    boolean pass = true;
    long forever = 0;
    String testCase = "msgHdrJMSExpirationQueueTest";
    try {

      TestUtil
          .logTrace("JMSExpiration is " + messageReceived.getJMSExpiration());
      if (messageReceived.getJMSExpiration() == forever) {
        TestUtil.logTrace("Pass ");
      } else {
        TestUtil.logMsg("Text Message Failed");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    } finally {
      sendTestResults(testCase, pass);
    }
  }

  public void msgHdrJMSDestinationQTestCreate() {
    Queue replyDestination = null;
    try {

      messageSent.setText("sending a message");
      messageSent.setStringProperty("TestCase", "msgHdrJMSDestinationQTest");
      TestUtil.logTrace("send msg for JMSDestination test.");
      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  public void msgHdrJMSDestinationQTest(javax.jms.Message messageReceived) {
    boolean pass = true;
    Queue replyDestination = null;
    String testCase = "msgHdrJMSDestinationQTest";

    try {
      TestUtil
          .logTrace("JMSDestination:  " + messageReceived.getJMSDestination());
      replyDestination = (Queue) messageReceived.getJMSDestination();
      if (replyDestination != null)
        TestUtil.logTrace("Queue name is " + replyDestination.getQueueName());

      if (replyDestination == null) {
        pass = false;
        TestUtil.logMsg("Text Message Error: JMSDestination returned a  null");
      } else if (replyDestination.getQueueName().equals(queue.getQueueName())) {
        TestUtil.logTrace("Pass ");
      } else {
        TestUtil.logMsg("Text Message Failed");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "Unexpected Exception thrown in msgHdrJMSDestinationQTest:", e);
    } finally {
      sendTestResults(testCase, pass);
    }
  }

  public void msgHdrJMSDeliveryModeQTestCreate() {
    try {

      messageSent.setText("sending a message");
      messageSent.setStringProperty("TestCase", "msgHdrJMSDeliveryModeQTest");
      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  public void msgHdrJMSDeliveryModeQTest(javax.jms.Message messageReceived) {
    boolean pass = true;
    String testCase = "msgHdrJMSDeliveryModeQTest";
    try {

      TestUtil.logTrace(
          "JMSDeliveryMode:  " + messageReceived.getJMSDeliveryMode());
      if (messageReceived.getJMSDeliveryMode() != DeliveryMode.PERSISTENT) {
        pass = false;
        TestUtil.logMsg(
            "Error: JMSDeliveryMode should be set to persistent as default");
      } else {
        TestUtil.logTrace("Pass: Default delivery mode is persistent");
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    } finally {
      sendTestResults(testCase, pass);
    }
  }

  public void msgHdrIDQTestCreate() {
    String id = null;
    try {

      messageSent.setText("sending a Text message");
      messageSent.setStringProperty("TestCase", "msgHdrIDQTest");
      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  public void msgHdrIDQTest(javax.jms.Message messageReceived) {
    boolean pass = true;
    String id = null;
    String testCase = "msgHdrIDQTest";
    try {

      TestUtil.logTrace("getJMSMessageID ");
      TestUtil.logTrace(" " + messageReceived.getJMSMessageID());
      id = messageReceived.getJMSMessageID();

      if (id.startsWith("ID:")) {
        TestUtil.logTrace("Pass: JMSMessageID start with ID:");
      } else {
        TestUtil.logMsg("Error: JMSMessageID does not start with ID:");
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    } finally {
      sendTestResults(testCase, pass);
    }
  }

  /*
   * Description: send test results to response queue (MDB_QUEUE_REPLY) for
   * verification
   */
  private void sendTestResults(String testCase, boolean results) {
    TextMessage msg = null;

    try {
      // create a msg sender for the response queue
      mSender = qSession.createSender(queueR);
      // and we'll send a text msg
      msg = qSession.createTextMessage();
      msg.setStringProperty("TestCase", testCase);
      msg.setText(testCase);
      if (results)
        msg.setStringProperty("Status", "Pass");
      else
        msg.setStringProperty("Status", "Fail");

      mSender.send(msg);
    } catch (JMSException je) {
      TestUtil.printStackTrace(je);
      System.out.println("Error: " + je.getClass().getName() + " was thrown");
    } catch (Exception ee) {
      TestUtil.printStackTrace(ee);
      System.out.println("Error: " + ee.getClass().getName() + " was thrown");
    }
  }

  public void setMessageDrivenContext(MessageDrivenContext mdc) {
    TestUtil.logTrace(
        "jms.ee.mdb.mdb_msgHdrQ  In MsgBeanMsgTestHdrQ::setMessageDrivenContext()!!");
    this.mdc = mdc;
  }

  public void ejbRemove() {
    TestUtil
        .logTrace("jms.ee.mdb.mdb_msgHdrQ  In MsgBeanMsgTestHdrQ::remove()!!");
  }
}
