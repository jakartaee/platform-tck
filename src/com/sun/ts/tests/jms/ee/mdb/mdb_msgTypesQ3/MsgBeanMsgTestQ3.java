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

package com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesQ3;

import java.io.Serializable;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.naming.*;
import javax.jms.*;
import java.sql.*;
import java.util.Properties;
import javax.sql.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jms.common.*;
import com.sun.ts.tests.jms.commonee.*;

public class MsgBeanMsgTestQ3 implements MessageDrivenBean, MessageListener {

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

  public MsgBeanMsgTestQ3() {
    TestUtil.logTrace("@MsgBeanMsgTest3()!");
  };

  public void ejbCreate() {
    TestUtil.logTrace(
        "jms.ee.mdb.mdb_msgTypesQ3  - @MsgBeanMsgTest3-ejbCreate() !!");
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

      queue = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE");
      if (queue == null) {
        TestUtil.logTrace("queue error");
      }

      p = new Properties();

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("MDB ejbCreate Error!", e);
    }
  }

  public void onMessage(Message msg) {

    JmsUtil.initHarnessProps(msg, p);
    TestUtil.logTrace("from jms.ee.mdb.mdb_msgTypesQ3 @onMessage!" + msg);

    try {
      TestUtil.logTrace(
          "onMessage will run TestCase: " + msg.getStringProperty("TestCase"));
      qConnection = qFactory.createQueueConnection();
      if (qConnection == null) {
        TestUtil.logTrace("connection error");
      } else {
        qConnection.start();
        qSession = qConnection.createQueueSession(true, 0);
      }
      if (msg.getStringProperty("TestCase")
          .equals("msgClearBodyQueueTextTestCreate")) {
        TestUtil.logTrace(
            "@onMessage - running msgClearBodyQueueTextTestCreate - create the message");
        msgClearBodyQueueTextTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgClearBodyQueueTextTest")) {
        TestUtil.logTrace(
            "@onMessage - running msgClearBodyQueueTextTest - read and verify the message");
        msgClearBodyQueueTextTest((javax.jms.TextMessage) msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("msgClearBodyQueueObjectTestCreate")) {
        TestUtil.logTrace(
            "@onMessage - running msgClearBodyQueueObjectTestCreate - create the message");
        msgClearBodyQueueObjectTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgClearBodyQueueObjectTest")) {
        TestUtil.logTrace(
            "@onMessage - running msgClearBodyQueueObjectTest - read and verify the message");
        msgClearBodyQueueObjectTest((javax.jms.ObjectMessage) msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("msgClearBodyQueueMapTestCreate")) {
        TestUtil.logTrace(
            "@onMessage - running msgClearBodyQueueMapTestCreate - create the message");
        msgClearBodyQueueMapTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgClearBodyQueueMapTest")) {
        TestUtil.logTrace(
            "@onMessage - running msgClearBodyQueueMapTest - read and verify the message");
        msgClearBodyQueueMapTest((javax.jms.MapMessage) msg);
      }

      else if (msg.getStringProperty("TestCase")
          .equals("msgClearBodyQueueBytesTestCreate")) {
        TestUtil.logTrace(
            "@onMessage - running msgClearBodyQueueBytesTestCreate - create the message");
        msgClearBodyQueueBytesTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgClearBodyQueueBytesTest")) {
        TestUtil.logTrace(
            "@onMessage - running msgClearBodyQueueBytesTest - read and verify the message");
        msgClearBodyQueueBytesTest((javax.jms.BytesMessage) msg);
      }

      else if (msg.getStringProperty("TestCase")
          .equals("msgClearBodyQueueStreamTestCreate")) {
        TestUtil.logTrace(
            "@onMessage - running msgClearBodyQueueStreamTestCreate - create the message");
        msgClearBodyQueueStreamTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("msgClearBodyQueueStreamTest")) {
        TestUtil.logTrace(
            "@onMessage - running msgClearBodyQueueStreamTest - read and verify the message");
        msgClearBodyQueueStreamTest((javax.jms.StreamMessage) msg);
      }

      else if (msg.getStringProperty("TestCase").equals("msgResetQueueTest")) {
        TestUtil.logTrace(
            "@onMessage - running msgResetQueueTest - read and verify the message");
        msgResetQueueTest();
      } else if (msg.getStringProperty("TestCase")
          .equals("readNullCharNotValidQueueStreamTestCreate")) {
        TestUtil.logTrace(
            "@onMessage - running readNullCharNotValidQueueStreamTestCreate - read and verify the message");
        readNullCharNotValidQueueStreamTestCreate();
      }

      else if (msg.getStringProperty("TestCase")
          .equals("readNullCharNotValidQueueStreamTest")) {
        TestUtil.logTrace(
            "@onMessage - running readNullCharNotValidQueueStreamTest - read and verify the message");
        readNullCharNotValidQueueStreamTest((javax.jms.StreamMessage) msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("readNullCharNotValidQueueMapTestCreate")) {
        TestUtil.logTrace(
            "@onMessage - running readNullCharNotValidQueueMapTestCreate - read and verify the message");
        readNullCharNotValidQueueMapTestCreate();
      }

      else if (msg.getStringProperty("TestCase")
          .equals("readNullCharNotValidQueueMapTest")) {
        TestUtil.logTrace(
            "@onMessage - running readNullCharNotValidQueueMapTest - read and verify the message");
        readNullCharNotValidQueueMapTest((javax.jms.MapMessage) msg);
      } else {
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
   * Description: create and send a text message
   */
  private void msgClearBodyQueueTextTestCreate() {
    try {
      TextMessage messageSent = null;
      messageSent = qSession.createTextMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setText("sending a Text message");
      messageSent.setStringProperty("TestCase", "msgClearBodyQueueTextTest");
      TestUtil.logTrace("sending a Text message");
      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: create and send an object message
   */
  private void msgClearBodyQueueObjectTestCreate() {
    try {
      ObjectMessage messageSentObjectMsg = null;
      // send and receive Object message to Queue
      TestUtil.logTrace("Send ObjectMessage to Queue.");
      messageSentObjectMsg = qSession.createObjectMessage();
      JmsUtil.addPropsToMessage(messageSentObjectMsg, p);
      messageSentObjectMsg.setObject("Initial message");
      messageSentObjectMsg.setStringProperty("TestCase",
          "msgClearBodyQueueObjectTest");
      mSender = qSession.createSender(queue);
      mSender.send(messageSentObjectMsg);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: create and send a Map message
   */
  private void msgClearBodyQueueMapTestCreate() {
    try {
      MapMessage messageSentMapMessage = null;
      // send and receive Map message to Queue
      TestUtil.logTrace("Send MapMessage to Queue.");
      messageSentMapMessage = qSession.createMapMessage();
      JmsUtil.addPropsToMessage(messageSentMapMessage, p);
      messageSentMapMessage.setStringProperty("TestCase",
          "msgClearBodyQueueMapTest");
      messageSentMapMessage.setString("aString", "Initial message");
      mSender = qSession.createSender(queue);
      mSender.send(messageSentMapMessage);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: create and send a Bytes message
   */
  private void msgClearBodyQueueBytesTestCreate() {
    byte bValue = 127;
    try {
      BytesMessage messageSentBytesMessage = null;
      // send and receive bytes message to Queue
      TestUtil.logTrace("Send BytesMessage to Queue.");
      messageSentBytesMessage = qSession.createBytesMessage();
      JmsUtil.addPropsToMessage(messageSentBytesMessage, p);
      messageSentBytesMessage.setStringProperty("TestCase",
          "msgClearBodyQueueBytesTest");
      messageSentBytesMessage.writeByte(bValue);
      mSender = qSession.createSender(queue);
      mSender.send(messageSentBytesMessage);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: create and send a Stream message
   */
  private void msgClearBodyQueueStreamTestCreate() {
    try {
      StreamMessage messageSentStreamMessage = null;
      // Send and receive a StreamMessage
      TestUtil.logTrace("sending a Stream message");
      messageSentStreamMessage = qSession.createStreamMessage();
      JmsUtil.addPropsToMessage(messageSentStreamMessage, p);
      messageSentStreamMessage.setStringProperty("TestCase",
          "msgClearBodyQueueStreamTest");
      messageSentStreamMessage.writeString("Testing...");
      TestUtil.logTrace("Sending message");
      mSender = qSession.createSender(queue);
      mSender.send(messageSentStreamMessage);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: Receive a single Text message call clearBody, verify body is
   * empty after clearBody. verify properties are not effected by clearBody.
   * Write to the message again 3.11
   */
  public void msgClearBodyQueueTextTest(javax.jms.TextMessage messageReceived) {
    String testCase = "msgClearBodyQueueTextTest";
    boolean pass = true;
    // Text Message
    try {
      TestUtil.logTrace("Test TextMessage ");
      TestUtil.logTrace("read 1st contents");
      TestUtil.logTrace("  " + messageReceived.getText());
      TestUtil.logTrace("Call to clearBody !!!!!!!!!!!!!!!");
      messageReceived.clearBody();
      // message body should now be empty
      if (messageReceived.getText() == null) {
        TestUtil.logTrace("Empty body after clearBody as expected: null");
      } else {
        TestUtil.logTrace("Fail: message body was not empty");
        pass = false;
      }
      // properties should not have been deleted by the clearBody method.
      if (messageReceived.getStringProperty("TestCase")
          .equals("msgClearBodyQueueTextTest")) {
        TestUtil
            .logTrace("Pass: Text properties read ok after clearBody called");
      } else {
        TestUtil
            .logTrace("Fail: Text properties cleared after clearBody called");
        pass = false;
      }
      TestUtil.logTrace("write and read 2nd contents");
      messageReceived.setText("new data");
      if (messageReceived.getText().equals("new data")) {
        TestUtil.logTrace("Pass:");
      } else {
        TestUtil.logTrace("Fail:");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logTrace("Error: " + e.getClass().getName() + " was thrown");
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      sendTestResults(testCase, pass);
    }
  }

  /*
   * Description: Receive a single Object message call clearBody, verify body is
   * empty after clearBody. verify properties are not effected by clearBody.
   * Write to the message again 3.11
   */
  public void msgClearBodyQueueObjectTest(
      javax.jms.ObjectMessage messageReceivedObjectMsg) {
    String testCase = "msgClearBodyQueueObjectTest";
    boolean pass = true;
    try {
      TestUtil.logTrace("Testing Object message");
      TestUtil.logTrace("read 1st contents");
      TestUtil.logTrace("  " + messageReceivedObjectMsg.getObject());
      TestUtil.logTrace("Call to clearBody !!!!!!!!!!!!!!!");
      messageReceivedObjectMsg.clearBody();
      // message body should now be empty
      if (messageReceivedObjectMsg.getObject() == null) {
        TestUtil.logTrace("Empty body after clearBody as expected: null");
      } else {
        TestUtil.logTrace("Fail: message body was not empty");
        pass = false;
      }
      // properties should not have been deleted by the clearBody method.
      if (messageReceivedObjectMsg.getStringProperty("TestCase")
          .equals("msgClearBodyQueueObjectTest")) {
        TestUtil
            .logTrace("Pass: Object properties read ok after clearBody called");
      } else {
        TestUtil
            .logTrace("Fail: Object properties cleared after clearBody called");
        pass = false;
      }
      TestUtil.logTrace("write 2nd contents");
      messageReceivedObjectMsg.setObject("new stuff here!!!!!!");
      TestUtil.logTrace("read 2nd contents");
      if (messageReceivedObjectMsg.getObject().equals("new stuff here!!!!!!")) {
        TestUtil.logTrace("Pass:");
      } else {
        TestUtil.logTrace("Fail: ");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logTrace("Error: " + e.getClass().getName() + " was thrown");
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      sendTestResults(testCase, pass);
    }
  }

  /*
   * Description: Receive a single Map message call clearBody, verify body is
   * empty after clearBody. verify properties are not effected by clearBody.
   * Write to the message again 3.11
   */
  private void msgClearBodyQueueMapTest(
      javax.jms.MapMessage messageReceivedMapMessage) {
    String testCase = "msgClearBodyQueueMapTest";
    boolean pass = true;
    try {
      TestUtil.logTrace("Test for MapMessage ");
      TestUtil.logTrace("read 1st contents");
      TestUtil.logTrace("  " + messageReceivedMapMessage.getString("aString"));
      TestUtil.logTrace("Call to clearBody !!!!!!!!!!!!!!!");
      messageReceivedMapMessage.clearBody();
      // message body should now be empty
      if (messageReceivedMapMessage.getString("aString") == null) {
        TestUtil.logTrace("Empty body after clearBody as expected: null");
      } else {
        TestUtil.logTrace("Fail: message body was not empty");

        TestUtil.logTrace(
            "Contains: " + messageReceivedMapMessage.getString("aString"));
        pass = false;
      }

      // properties should not have been deleted by the clearBody method.
      if (messageReceivedMapMessage.getStringProperty("TestCase")
          .equals("msgClearBodyQueueMapTest")) {
        TestUtil
            .logTrace("Pass: Map properties read ok after clearBody called");
      } else {
        TestUtil
            .logTrace("Fail: Map properties cleared after clearBody called");
        pass = false;
      }
      TestUtil.logTrace("write 2nd contents");
      messageReceivedMapMessage.setString("yes", "new stuff !!!!!");
      TestUtil.logTrace("read 2nd contents");
      if (messageReceivedMapMessage.getString("yes")
          .equals("new stuff !!!!!")) {
        TestUtil.logTrace("PASS:");
      } else {
        TestUtil.logTrace("FAIL:");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logTrace("Error: " + e.getClass().getName() + " was thrown");
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      sendTestResults(testCase, pass);
    }
  }

  /*
   * Description: Receive a single Bytes message call clearBody, verify body is
   * empty after clearBody. verify properties are not effected by clearBody.
   * Write to the message again 3.11
   */
  public void msgClearBodyQueueBytesTest(
      javax.jms.BytesMessage messageReceivedBytesMessage) {
    String testCase = "msgClearBodyQueueBytesTest";
    boolean pass = true;
    byte bValue2 = 22;
    try {
      TestUtil.logTrace("Test BytesMessage ");
      TestUtil.logTrace("read 1st contents");
      TestUtil.logTrace("  " + messageReceivedBytesMessage.readByte());
      TestUtil.logTrace("Call to clearBody !!!!!!!!!!!!!!!");
      messageReceivedBytesMessage.clearBody();
      TestUtil.logTrace(
          "Bytes message body should now be empty and in writeonly mode");
      try {
        byte b = messageReceivedBytesMessage.readByte();
        TestUtil.logTrace(
            "Fail: MessageNotReadableException not thrown as expected");
        pass = false;
      } catch (javax.jms.MessageNotReadableException e) {
        TestUtil
            .logTrace("Pass: MessageNotReadableException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // properties should not have been deleted by the clearBody method.
      if (messageReceivedBytesMessage.getStringProperty("TestCase")
          .equals("msgClearBodyQueueBytesTest")) {
        TestUtil.logTrace(
            "Pass: Bytes msg properties read ok after clearBody called");
      } else {
        TestUtil.logTrace(
            "Fail: Bytes msg properties cleared after clearBody called");
        pass = false;
      }

      TestUtil.logTrace("write 2nd contents");
      messageReceivedBytesMessage.writeByte(bValue2);
      TestUtil.logTrace("read 2nd contents");
      messageReceivedBytesMessage.reset();
      if (messageReceivedBytesMessage.readByte() == bValue2) {
        TestUtil.logTrace("Pass:");
      } else {
        TestUtil.logTrace("Fail:");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logTrace("Error: " + e.getClass().getName() + " was thrown");
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      sendTestResults(testCase, pass);
    }
  }

  /*
   * Description: Receive a single Stream message call clearBody, verify body is
   * empty after clearBody. verify properties are not effected by clearBody.
   * Write to the message again 3.11
   */
  public void msgClearBodyQueueStreamTest(
      javax.jms.StreamMessage messageReceivedStreamMessage) {
    String testCase = "msgClearBodyQueueStreamTest";
    boolean pass = true;
    try {
      TestUtil.logTrace("Test StreamMessage ");
      TestUtil.logTrace("read 1st contents");
      TestUtil.logTrace("  " + messageReceivedStreamMessage.readString());
      TestUtil.logTrace("Call to clearBody !!!!!!!!!!!!!!!");
      messageReceivedStreamMessage.clearBody();

      TestUtil.logTrace(
          "Stream message body should now be empty and in writeonly mode");
      try {
        String s = messageReceivedStreamMessage.readString();
        TestUtil.logTrace(
            "Fail: MessageNotReadableException should have been thrown");
        pass = false;
      } catch (MessageNotReadableException e) {
        TestUtil
            .logTrace("Pass: MessageNotReadableException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // properties should not have been deleted by the clearBody method.
      if (messageReceivedStreamMessage.getStringProperty("TestCase")
          .equals("msgClearBodyQueueStreamTest")) {
        TestUtil.logTrace(
            "Pass: Stream msg properties read ok after clearBody called");
      } else {
        TestUtil.logTrace(
            "Fail: Stream msg properties cleared after clearBody called");
        pass = false;
      }
      TestUtil.logTrace("write 2nd contents");
      messageReceivedStreamMessage.writeString("new data");
      TestUtil.logTrace("read 2nd contents");
      messageReceivedStreamMessage.reset();
      if (messageReceivedStreamMessage.readString().equals("new data")) {
        TestUtil.logTrace("Pass:");
      } else {
        TestUtil.logTrace("Fail:");
      }
    } catch (Exception e) {
      TestUtil.logTrace("Error: " + e.getClass().getName() + " was thrown");
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      sendTestResults(testCase, pass);
    }
  }

  /*
   *
   * Description: create a stream message and a byte message. write to the
   * message body, call the reset method, try to write to the body expect a
   * MessageNotWriteableException to be thrown.
   */
  private void msgResetQueueTest() {
    boolean pass = true;
    int nInt = 1000;
    String testCase = "msgResetQueueTest";
    try {
      StreamMessage messageSentStreamMessage = null;
      BytesMessage messageSentBytesMessage = null;
      // StreamMessage
      try {
        TestUtil.logTrace("creating a Stream message");
        messageSentStreamMessage = qSession.createStreamMessage();
        JmsUtil.addPropsToMessage(messageSentStreamMessage, p);
        messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
            "msgResetQueueTest1");
        // write to the message
        messageSentStreamMessage.writeString("Testing...");
        TestUtil.logTrace(
            "reset stream message -  now  should be in readonly mode");
        messageSentStreamMessage.reset();
        messageSentStreamMessage.writeString("new data");
        TestUtil.logTrace(
            "Fail: message did not throw MessageNotWriteable exception as expected");
        pass = false;
      } catch (MessageNotWriteableException nw) {
        TestUtil.logTrace("Pass: MessageNotWriteable thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: " + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // BytesMessage
      try {
        TestUtil.logTrace("creating a Byte message");
        messageSentBytesMessage = qSession.createBytesMessage();
        JmsUtil.addPropsToMessage(messageSentBytesMessage, p);
        messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
            "msgResetQueueTest2");
        // write to the message
        messageSentBytesMessage.writeInt(nInt);
        TestUtil
            .logTrace("reset Byte message -  now  should be in readonly mode");
        messageSentBytesMessage.reset();
        messageSentBytesMessage.writeInt(nInt);
        TestUtil.logTrace(
            "Fail: message did not throw MessageNotWriteable exception as expected");
        pass = false;
      } catch (MessageNotWriteableException nw) {
        TestUtil.logTrace("Pass: MessageNotWriteable thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: " + e.getClass().getName() + " was thrown");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    } finally {
      sendTestResults(testCase, pass);
    }
  }

  /*
   *
   * Description: Write a null string to a MapMessage.
   *
   */
  public void readNullCharNotValidQueueMapTestCreate() {
    try {
      MapMessage mapSent = null;
      char c;
      boolean pass = true;
      mapSent = qSession.createMapMessage();
      JmsUtil.addPropsToMessage(mapSent, p);
      mapSent.setStringProperty("TestCase", "readNullCharNotValidQueueMapTest");
      TestUtil.logTrace(
          "Write a null string to the map message object with mapMessage.setString");
      mapSent.setString("WriteANull", null);
      TestUtil.logTrace(" Send the message");
      mSender = qSession.createSender(queue);
      mSender.send(mapSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   *
   * Description: Write a null string to a StreamMessage.
   *
   */
  public void readNullCharNotValidQueueStreamTestCreate() {
    try {
      StreamMessage messageSent = null;
      char c;
      boolean pass = true;
      messageSent = qSession.createStreamMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("TestCase",
          "readNullCharNotValidQueueStreamTest");
      TestUtil.logTrace(
          "Write a null string to the map message object with streamMessage.setString");
      messageSent.writeString(null);
      TestUtil.logTrace(" Send the message");
      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: Attempt to read a null string from a MapMessage. Should throw
   * a null pointer exception
   */

  public void readNullCharNotValidQueueMapTest(
      javax.jms.MapMessage mapReceived) {
    String testCase = "readNullCharNotValidQueueMapTest";
    boolean pass = true;
    try {
      char c;
      TestUtil.logTrace("Use readChar to read a null  ");
      try {
        c = mapReceived.getChar("WriteANull");
        TestUtil.logTrace("Fail: NullPointerException was not thrown");
        pass = false;
      } catch (java.lang.NullPointerException e) {
        TestUtil.logTrace("Pass: NullPointerException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    } finally {
      sendTestResults(testCase, pass);
    }

  }
  /*
   * Description: Attempt to read a null string from a StreamMessage. Should
   * throw a null pointer exception
   */

  public void readNullCharNotValidQueueStreamTest(
      javax.jms.StreamMessage messageReceived) {
    String testCase = "readNullCharNotValidQueueStreamTest";
    boolean pass = true;
    try {
      char c;
      TestUtil.logTrace("Use readChar to read a null  ");
      try {
        c = messageReceived.readChar();
        TestUtil.logTrace("Fail: NullPointerException was not thrown");
        pass = false;
      } catch (java.lang.NullPointerException e) {
        TestUtil.logTrace("Pass: NullPointerException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
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
        "jms.ee.mdb.mdb_msgTypesQ3  In MsgBeanMsgTest3::setMessageDrivenContext()!!");
    this.mdc = mdc;
  }

  public void ejbRemove() {
    TestUtil
        .logTrace("jms.ee.mdb.mdb_msgTypesQ3  In MsgBeanMsgTest3::remove()!!");
  }
}
