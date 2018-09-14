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

package com.sun.ts.tests.jms.ee.mdb.mdb_msgHdrT;

import java.io.Serializable;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.naming.*;
import javax.jms.*;
import java.sql.*;
import java.util.Enumeration;
import java.util.Vector;
import java.util.Properties;
import javax.sql.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jms.common.*;
import com.sun.ts.tests.jms.commonee.*;

public class MsgBeanMsgTestHdrT implements MessageDrivenBean, MessageListener {

  // properties object needed for logging, get this from the message object
  // passed into
  // the onMessage method.
  private java.util.Properties props = null;

  private TSNamingContext context = null;

  private MessageDrivenContext mdc = null;

  // JMS
  private QueueConnectionFactory qFactory = null;

  private QueueConnection qConnection = null;

  private Queue queueR = null;

  private QueueSession qSession = null;

  private QueueSender mSender;

  private Topic topic;

  private TopicConnection tConnection;

  private TopicSession tSession;

  private TopicConnectionFactory tFactory;

  private TopicPublisher tSender;

  private TextMessage messageSent = null;

  private StreamMessage messageSentStreamMessage = null;

  private BytesMessage messageSentBytesMessage = null;

  private MapMessage messageSentMapMessage = null;

  private ObjectMessage messageSentObjectMsg = null;

  public MsgBeanMsgTestHdrT() {
    TestUtil.logTrace("@MsgBeanMsgTestHdrT()!");
  };

  public void ejbCreate() {
    props = new java.util.Properties();

    TestUtil.logTrace(
        "jms.ee.mdb.mdb_msgHdrT  - @MsgBeanMsgTestHdrT-ejbCreate() !!");
    try {
      context = new TSNamingContext();
      qFactory = (QueueConnectionFactory) context
          .lookup("java:comp/env/jms/MyQueueConnectionFactory");
      if (qFactory == null) {
        TestUtil.logTrace("qFactory error");
      }
      TestUtil.logTrace("got a qFactory !!");
      queueR = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
      if (queueR == null) {
        TestUtil.logTrace("queueR error");
      }

      tFactory = (TopicConnectionFactory) context
          .lookup("java:comp/env/jms/MyTopicConnectionFactory");
      topic = (Topic) context.lookup("java:comp/env/jms/MDB_TOPIC");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("MDB ejbCreate Error!", e);
    }
  }

  public void onMessage(Message msg) {

    TestUtil.logTrace("from jms.ee.mdb.mdb_msgHdrT @onMessage!" + msg);

    try {
      JmsUtil.initHarnessProps(msg, props);
      TestUtil.logTrace("from jms.ee.mdb.mdb_msgHdrT @onMessage!" + msg);

      TestUtil.logTrace(
          "onMessage will run TestCase: " + msg.getStringProperty("TestCase"));
      tConnection = tFactory.createTopicConnection();
      if (tConnection == null) {
        TestUtil.logTrace("connection error");
      } else {
        tConnection.start();
        tSession = tConnection.createTopicSession(true, 0);
      }

      qConnection = qFactory.createQueueConnection();
      if (qConnection == null) {
        System.out.println("connection error");
      } else {
        qConnection.start();
        qSession = qConnection.createQueueSession(true, 0);
      }

      // Send a message back to acknowledge that the mdb received the message.

      // create testmessages
      Vector mVec = new Vector();
      messageSent = tSession.createTextMessage();
      mVec.addElement(messageSent);
      messageSentStreamMessage = tSession.createStreamMessage();
      mVec.addElement(messageSentStreamMessage);
      messageSentBytesMessage = tSession.createBytesMessage();
      mVec.addElement(messageSentBytesMessage);
      messageSentMapMessage = tSession.createMapMessage();
      mVec.addElement(messageSentMapMessage);
      messageSentObjectMsg = tSession.createObjectMessage();
      mVec.addElement(messageSentObjectMsg);

      // for each message addPropsToMessage
      Enumeration vNum = mVec.elements();
      while (vNum.hasMoreElements()) {
        JmsUtil.addPropsToMessage((Message) vNum.nextElement(), props);
      }

      if (msg.getStringProperty("TestCase").equals("msgHdrTimeStampTTest")) {
        TestUtil.logTrace(
            "@onMessage - running msgHdrTimeStampTTest - create the message");
        msgHdrTimeStampTTest();
      } else if (msg.getStringProperty("TestCase").equals("dummy")) {
        TestUtil.logTrace("@onMessage - ignore this!");
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrCorlIdTTextTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrCorlIdTTextTestCreate!");
        msgHdrCorlIdTTextTestCreate();
      }

      else if (msg.getStringProperty("TestCase")
          .equals("msgHdrCorlIdTTextTest")) {
        TestUtil.logTrace("@onMessage - msgHdrCorlIdTTextTest!");
        msgHdrCorlIdTTest(msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrCorlIdTBytesTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrCorlIdTBytesTestCreate!");
        msgHdrCorlIdTBytesTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrCorlIdTBytesTest")) {
        TestUtil.logTrace("@onMessage -msgHdrCorlIdTBytesTest!");
        msgHdrCorlIdTTest(msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrCorlIdTMapTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrCorlIdTMapTestCreate!");
        msgHdrCorlIdTMapTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrCorlIdTMapTest")) {
        TestUtil.logTrace("@onMessage - msgHdrCorlIdTMapTest!");
        msgHdrCorlIdTTest(msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrCorlIdTStreamTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrCorlIdTStreamTestCreate!");
        msgHdrCorlIdTStreamTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrCorlIdTStreamTest")) {
        TestUtil.logTrace("@onMessage - msgHdrCorlIdTStreamTest!");
        msgHdrCorlIdTTest(msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrCorlIdTObjectTestCreate")) {
        TestUtil.logTrace("@onMessage -msgHdrCorlIdTObjectTestCreate!");
        msgHdrCorlIdTObjectTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrCorlIdTObjectTest")) {
        TestUtil.logTrace("@onMessage - msgHdrCorlIdTObjectTest!");
        msgHdrCorlIdTTest(msg);
      }

      else if (msg.getStringProperty("TestCase")
          .equals("msgHdrReplyToTTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrReplyToTTestCreate!");
        msgHdrReplyToTTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrReplyToTTest")) {
        TestUtil.logTrace("@onMessage - msgHdrReplyToTTest!");
        msgHdrReplyToTTest(msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrJMSTypeTTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrJMSTypeTTestCreate!");
        msgHdrJMSTypeTTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrJMSTypeTTest")) {
        TestUtil.logTrace("@onMessage - msgHdrJMSTypeTTest!");
        msgHdrJMSTypeTTest(msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrJMSPriorityTTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrJMSPriorityTTestCreate!");
        msgHdrJMSPriorityTTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrJMSPriorityTTest")) {
        TestUtil.logTrace("@onMessage - msgHdrJMSPriorityTTest!");
        msgHdrJMSPriorityTTest(msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrJMSExpirationTopicTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrJMSExpirationTopicTestCreate!");
        msgHdrJMSExpirationTopicTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrJMSExpirationTopicTest")) {
        TestUtil.logTrace("@onMessage - msgHdrJMSExpirationTopicTest!");
        msgHdrJMSExpirationTopicTest(msg);
      }

      else if (msg.getStringProperty("TestCase")
          .equals("msgHdrJMSDestinationTTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrJMSDestinationTTestCreate!");
        msgHdrJMSDestinationTTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrJMSDestinationTTest")) {
        TestUtil.logTrace("@onMessage - msgHdrJMSDestinationTTest!");
        msgHdrJMSDestinationTTest(msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrJMSDeliveryModeTTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrJMSDeliveryModeTTestCreate!");
        msgHdrJMSDeliveryModeTTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrJMSDeliveryModeTTest")) {
        TestUtil.logTrace("@onMessage - msgHdrJMSDeliveryModeTTest!");
        msgHdrJMSDeliveryModeTTest(msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("msgHdrIDTTestCreate")) {
        TestUtil.logTrace("@onMessage - msgHdrIDTTestCreate!");
        msgHdrIDTTestCreate();
      } else if (msg.getStringProperty("TestCase").equals("msgHdrIDTTest")) {
        TestUtil.logTrace("@onMessage - msgHdrIDTTest!");
        msgHdrIDTTest(msg);
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
      if (tConnection != null) {
        try {
          tConnection.close();
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
        }
      }
    }
  }

  /*
   * Description: Send a single Text message check time of send against time
   * send returns JMSTimeStamp should be between these two
   */
  public void msgHdrTimeStampTTest() {
    boolean pass = true;
    long timeBeforeSend;
    long timeAfterSend;
    byte bValue = 127;
    String id = null;
    String testCase = "msgHdrTimeStampTTest";
    try {

      // Text Message
      messageSent.setText("sending a Text message");
      messageSent.setStringProperty("TestCase", "dummy");
      TestUtil.logTrace("sending a Text message");

      // get the current time in milliseconds - before and after the send
      timeBeforeSend = System.currentTimeMillis();
      tSender = tSession.createPublisher(topic);
      tSender.publish(messageSent);
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
  private void msgHdrCorlIdTTextTestCreate() {

    String jmsCorrelationID = "test Correlation id";
    try {

      messageSent.setText("sending a message");
      messageSent.setStringProperty("TestCase", "msgHdrCorlIdTTextTest");

      TestUtil.logTrace("Send Text Message to Topic.");
      messageSent.setJMSCorrelationID(jmsCorrelationID);
      tSender = tSession.createPublisher(topic);
      tSender.publish(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: create a stream msg, set correlation id
   */
  private void msgHdrCorlIdTStreamTestCreate() {

    String jmsCorrelationID = "test Correlation id";
    try {

      messageSentStreamMessage.setStringProperty("TestCase",
          "msgHdrCorlIdTStreamTest");
      messageSentStreamMessage.setJMSCorrelationID(jmsCorrelationID);
      messageSentStreamMessage.writeString("Testing...");
      TestUtil.logTrace("Sending Stream message");
      tSender = tSession.createPublisher(topic);
      tSender.publish(messageSentStreamMessage);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: create a bytes msg, set correlation id
   */
  private void msgHdrCorlIdTBytesTestCreate() {
    byte bValue = 127;

    String jmsCorrelationID = "test Correlation id";
    try {

      TestUtil.logTrace("Send BytesMessage to Topic.");
      messageSentBytesMessage.setStringProperty("TestCase",
          "msgHdrCorlIdTBytesTest");
      messageSentBytesMessage.setJMSCorrelationID(jmsCorrelationID);
      messageSentBytesMessage.writeByte(bValue);

      tSender = tSession.createPublisher(topic);
      tSender.publish(messageSentBytesMessage);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: create a mao msg, set correlation id
   */
  private void msgHdrCorlIdTMapTestCreate() {

    String jmsCorrelationID = "test Correlation id";
    try {

      TestUtil.logTrace("Send MapMessage to Topic.");

      messageSentMapMessage.setStringProperty("TestCase",
          "msgHdrCorlIdTMapTest");
      messageSentMapMessage.setJMSCorrelationID(jmsCorrelationID);
      messageSentMapMessage.setString("aString", "value");

      tSender = tSession.createPublisher(topic);
      tSender.publish(messageSentMapMessage);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: create an object msg, set correlation id
   */
  private void msgHdrCorlIdTObjectTestCreate() {

    String jmsCorrelationID = "test Correlation id";
    try {

      TestUtil.logTrace("Send ObjectMessage to Topic.");

      messageSentObjectMsg.setObject("msgHdrIDTObjectTest for Object Message");
      messageSentObjectMsg.setStringProperty("TestCase",
          "msgHdrCorlIdTObjectTest");
      messageSentObjectMsg.setJMSCorrelationID(jmsCorrelationID);

      tSender = tSession.createPublisher(topic);
      tSender.publish(messageSentObjectMsg);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }

  }

  public void msgHdrCorlIdTTest(javax.jms.Message messageReceived) {
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
  public void msgHdrReplyToTTestCreate() {
    Topic replyTopic = null;
    try {

      messageSent.setText("sending a message");
      messageSent.setStringProperty("TestCase", "msgHdrReplyToTTest");
      messageSent.setJMSReplyTo(topic);

      tSender = tSession.createPublisher(topic);
      tSender.publish(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  public void msgHdrReplyToTTest(javax.jms.Message messageReceived) {
    boolean pass = true;
    Topic replyTopic = null;
    String testCase = "msgHdrReplyToTTest";
    try {
      replyTopic = (Topic) messageReceived.getJMSReplyTo();
      TestUtil.logTrace("Topic name is " + replyTopic.getTopicName());
      if (replyTopic.getTopicName().equals(topic.getTopicName())) {
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

  public void msgHdrJMSTypeTTestCreate() {
    boolean pass = true;
    byte bValue = 127;
    String type = "TESTMSG";
    try {
      messageSent.setText("sending a message");
      messageSent.setStringProperty("TestCase", "msgHdrJMSTypeTTest");
      TestUtil.logTrace("JMSType test - Send a Text message");
      messageSent.setJMSType(type);
      tSender = tSession.createPublisher(topic);
      tSender.publish(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  public void msgHdrJMSTypeTTest(javax.jms.Message messageReceived) {
    boolean pass = true;
    String type = "TESTMSG";
    String testCase = "msgHdrJMSTypeTTest";
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

  public void msgHdrJMSPriorityTTestCreate() {
    boolean pass = true;
    byte bValue = 127;
    int priority = 2;
    try {

      messageSent.setText("sending a message");
      messageSent.setStringProperty("TestCase", "msgHdrJMSPriorityTTest");
      TestUtil.logTrace("JMSPriority test - Send a Text message");
      tSender = tSession.createPublisher(topic);
      tSender.setPriority(priority);
      tSender.publish(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  public void msgHdrJMSPriorityTTest(javax.jms.Message messageReceived) {
    boolean pass = true;
    int priority = 2;
    String testCase = "msgHdrJMSPriorityTTest";
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

  public void msgHdrJMSExpirationTopicTestCreate() {
    boolean pass = true;
    long forever = 0;
    try {

      messageSent.setText("sending a message");
      messageSent.setStringProperty("TestCase", "msgHdrJMSExpirationTopicTest");
      TestUtil.logTrace("JMSExpiration test - Send a Text message");

      tSender = tSession.createPublisher(topic);
      tSender.setTimeToLive(forever);
      tSender.publish(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  public void msgHdrJMSExpirationTopicTest(javax.jms.Message messageReceived) {
    boolean pass = true;
    long forever = 0;
    String testCase = "msgHdrJMSExpirationTopicTest";
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

  public void msgHdrJMSDestinationTTestCreate() {
    Topic replyDestination = null;
    try {

      messageSent.setText("sending a message");
      messageSent.setStringProperty("TestCase", "msgHdrJMSDestinationTTest");
      TestUtil.logTrace("send msg for JMSDestination test.");
      tSender = tSession.createPublisher(topic);
      tSender.publish(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  public void msgHdrJMSDestinationTTest(javax.jms.Message messageReceived) {
    boolean pass = true;
    Topic replyDestination = null;
    String testCase = "msgHdrJMSDestinationTTest";

    try {
      TestUtil
          .logTrace("JMSDestination:  " + messageReceived.getJMSDestination());
      replyDestination = (Topic) messageReceived.getJMSDestination();

      if (replyDestination != null)
        TestUtil.logTrace("Topic name is " + replyDestination.getTopicName());

      if (replyDestination == null) {
        pass = false;
        TestUtil.logMsg("Text Message Error: JMSDestination returned a  null");
      } else if (replyDestination.getTopicName().equals(topic.getTopicName())) {
        TestUtil.logTrace("Pass ");
      } else {
        TestUtil.logMsg("Text Message Failed");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception in msgHdrJMSDestinationTTest:", e);
    } finally {
      sendTestResults(testCase, pass);
    }
  }

  public void msgHdrJMSDeliveryModeTTestCreate() {
    try {

      messageSent.setText("sending a message");
      messageSent.setStringProperty("TestCase", "msgHdrJMSDeliveryModeTTest");
      tSender = tSession.createPublisher(topic);
      tSender.publish(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  public void msgHdrJMSDeliveryModeTTest(javax.jms.Message messageReceived) {
    boolean pass = true;
    String testCase = "msgHdrJMSDeliveryModeTTest";
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

  public void msgHdrIDTTestCreate() {
    String id = null;
    try {

      messageSent.setText("sending a Text message");
      messageSent.setStringProperty("TestCase", "msgHdrIDTTest");
      tSender = tSession.createPublisher(topic);
      tSender.publish(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  public void msgHdrIDTTest(javax.jms.Message messageReceived) {
    boolean pass = true;
    String id = null;
    String testCase = "msgHdrIDTTest";
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

      TestUtil.logTrace("Sending response message");
      TestUtil.logTrace(
          "==================================Test Results from: " + testCase);
      TestUtil.logTrace("==================================Status: " + results);
      mSender.send(msg);
    } catch (JMSException je) {
      TestUtil.printStackTrace(je);
      TestUtil.logTrace("Error: " + je.getClass().getName() + " was thrown");
    } catch (Exception ee) {
      TestUtil.printStackTrace(ee);
      TestUtil.logTrace("Error: " + ee.getClass().getName() + " was thrown");
    }
  }

  public void setMessageDrivenContext(MessageDrivenContext mdc) {
    TestUtil.logTrace(
        "jms.ee.mdb.mdb_msgHdrT  In MsgBeanMsgTestHdrT::setMessageDrivenContext()!!");
    this.mdc = mdc;
  }

  public void ejbRemove() {
    TestUtil
        .logTrace("jms.ee.mdb.mdb_msgHdrT  In MsgBeanMsgTestHdrT::remove()!!");
  }
}
