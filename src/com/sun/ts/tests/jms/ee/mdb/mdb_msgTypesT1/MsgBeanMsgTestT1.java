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

package com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesT1;

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

public class MsgBeanMsgTestT1 implements MessageDrivenBean, MessageListener {

  // properties object needed for logging, get this from the message object
  // passed into
  // the onMessage method.
  private java.util.Properties p = null;

  private TSNamingContext context = null;

  private MessageDrivenContext mdc = null;

  // JMS
  private QueueConnectionFactory qFactory = null;

  private TopicConnectionFactory tFactory = null;

  private QueueConnection qConnection = null;

  private TopicConnection tConnection = null;

  private Queue queueR = null;

  private Queue queue = null;

  private Topic topic = null;

  private QueueSender mSender = null;

  private QueueSession qSession = null;

  private TopicPublisher tPublisher = null;

  private TopicSession tSession = null;

  public MsgBeanMsgTestT1() {
    TestUtil.logTrace("@MsgBeanMsgTestT1()!");
  };

  public void ejbCreate() {
    TestUtil.logTrace(
        "jms.ee.mdb.mdb_msgTypesT1  - @MsgBeanMsgTestT1-ejbCreate() !!");
    try {
      context = new TSNamingContext();
      qFactory = (QueueConnectionFactory) context
          .lookup("java:comp/env/jms/MyQueueConnectionFactory");
      if (qFactory == null) {
        TestUtil.logTrace("qFactory error");
      }
      TestUtil.logTrace("got a qFactory !!");
      tFactory = (TopicConnectionFactory) context
          .lookup("java:comp/env/jms/MyTopicConnectionFactory");
      if (tFactory == null) {
        TestUtil.logTrace("tFactory error");
      }
      TestUtil.logTrace("got a tFactory !!");

      queueR = (Queue) context.lookup("java:comp/env/jms/MDB_QUEUE_REPLY");
      if (queueR == null) {
        TestUtil.logTrace("queueR error");
      }

      topic = (Topic) context.lookup("java:comp/env/jms/MDB_TOPIC");
      if (topic == null) {
        TestUtil.logTrace("topic error");
      }

      p = new Properties();

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("MDB ejbCreate Error!", e);
    }
  }

  public void onMessage(Message msg) {
    JmsUtil.initHarnessProps(msg, p);
    TestUtil.logTrace("from jms.ee.mdb.mdb_msgTypesT1 @onMessage!" + msg);

    try {

      qConnection = qFactory.createQueueConnection();
      if (qConnection == null) {
        TestUtil.logTrace("connection error");
      } else {
        qConnection.start();
        qSession = qConnection.createQueueSession(true, 0);
      }
      tConnection = tFactory.createTopicConnection();
      if (tConnection == null) {
        TestUtil.logTrace("connection error");
      } else {
        tConnection.start();
        tSession = tConnection.createTopicSession(true, 0);
      }

      if (msg.getStringProperty("TestCase")
          .equals("bytesMsgNullStreamTopicTest")) {
        bytesMsgNullStreamTopicTest();
      } else if (msg.getStringProperty("TestCase")
          .equals("bytesMessageTopicTestsFullMsgCreate")) {
        TestUtil.logTrace(
            "@onMessage - running bytesMessageTopicTestsFullMsg1 - create the message");
        bytesMessageTopicTestsFullMsgCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("bytesMessageTopicTestsFullMsg")) {
        TestUtil.logTrace(
            "@onMessage - running bytesMessageTopicTestsFullMsg - read and verify the message");
        bytesMessageTopicTestsFullMsg((javax.jms.BytesMessage) msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageFullMsgTopicTestCreate")) {
        TestUtil.logTrace(
            "@onMessage - running mapMessageFullMsgTopicTestCreate - read and verify the message");
        mapMessageFullMsgTopicTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageFullMsgTopicTest")) {
        TestUtil.logTrace(
            "@onMessage - running mapMessageFullMsgTopicTest - read and verify the message");
        mapMessageFullMsgTopicTest((javax.jms.MapMessage) msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsBooleanCreate")) {
        TestUtil.logTrace(
            "@onMessage - running mapMessageConversionTopicTestsBooleanCreate - read and verify the message");
        mapMessageConversionTopicTestsBooleanCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsBoolean")) {
        TestUtil.logTrace(
            "@onMessage - running MapMessageConversionTopicTestsBoolean - read and verify the message");
        mapMessageConversionTopicTestsBoolean((javax.jms.MapMessage) msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsByteCreate")) {
        TestUtil.logTrace(
            "@onMessage - running mapMessageConversionTopicTestsByteCreate - read and verify the message");
        mapMessageConversionTopicTestsByteCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsByte")) {
        TestUtil.logTrace(
            "@onMessage - running  mapMessageConversionTopicTestsByte - read and verify the message");
        mapMessageConversionTopicTestsByte((javax.jms.MapMessage) msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsShortCreate")) {
        TestUtil.logTrace(
            "@onMessage - running mapMessageConversionTopicTestsShortCreate - read and verify the message");
        mapMessageConversionTopicTestsShortCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsShort")) {
        TestUtil.logTrace(
            "@onMessage - running  mapMessageConversionTopicTestsShort - read and verify the message");
        mapMessageConversionTopicTestsShort((javax.jms.MapMessage) msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsCharCreate")) {
        TestUtil.logTrace(
            "@onMessage - running mapMessageConversionTopicTestsCharCreate - read and verify the message");
        mapMessageConversionTopicTestsCharCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsChar")) {
        TestUtil.logTrace(
            "@onMessage - running  mapMessageConversionTopicTestsChar - read and verify the message");
        mapMessageConversionTopicTestsChar((javax.jms.MapMessage) msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsIntCreate")) {
        TestUtil.logTrace(
            "@onMessage - running mapMessageConversionTopicTestsIntCreate - read and verify the message");
        mapMessageConversionTopicTestsIntCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsInt")) {
        TestUtil.logTrace(
            "@onMessage - running  mapMessageConversionTopicTestsInt - read and verify the message");
        mapMessageConversionTopicTestsInt((javax.jms.MapMessage) msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsLongCreate")) {
        TestUtil.logTrace(
            "@onMessage - running mapMessageConversionTopicTestsLongCreate - read and verify the message");
        mapMessageConversionTopicTestsLongCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsLong")) {
        TestUtil.logTrace(
            "@onMessage - running  mapMessageConversionTopicTestsLong - read and verify the message");
        mapMessageConversionTopicTestsLong((javax.jms.MapMessage) msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsFloatCreate")) {
        TestUtil.logTrace(
            "@onMessage - running mapMessageConversionTopicTestsFloatCreate - read and verify the message");
        mapMessageConversionTopicTestsFloatCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsFloat")) {
        TestUtil.logTrace(
            "@onMessage - running  mapMessageConversionTopicTestsFloat - read and verify the message");
        mapMessageConversionTopicTestsFloat((javax.jms.MapMessage) msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsDoubleCreate")) {
        TestUtil.logTrace(
            "@onMessage - running mapMessageConversionTopicTestsDoubleCreate - read and verify the message");
        mapMessageConversionTopicTestsDoubleCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsDouble")) {
        TestUtil.logTrace(
            "@onMessage - running  mapMessageConversionTopicTestsDouble - read and verify the message");
        mapMessageConversionTopicTestsDouble((javax.jms.MapMessage) msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsStringCreate")) {
        TestUtil.logTrace(
            "@onMessage - running mapMessageConversionTopicTestsStringCreate - read and verify the message");
        mapMessageConversionTopicTestsStringCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsString")) {
        TestUtil.logTrace(
            "@onMessage - running  mapMessageConversionTopicTestsString - read and verify the message");
        mapMessageConversionTopicTestsString((javax.jms.MapMessage) msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsBytesCreate")) {
        TestUtil.logTrace(
            "@onMessage - running mapMessageConversionTopicTestsBytesCreate - read and verify the message");
        mapMessageConversionTopicTestsBytesCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsBytes")) {
        TestUtil.logTrace(
            "@onMessage - running  mapMessageConversionTopicTestsBytes - read and verify the message");
        mapMessageConversionTopicTestsBytes((javax.jms.MapMessage) msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsInvFormatStringCreate")) {
        TestUtil.logTrace(
            "@onMessage - running mapMessageConversionTopicTestsInvFormatStringCreate - read and verify the message");
        mapMessageConversionTopicTestsInvFormatStringCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("mapMessageConversionTopicTestsInvFormatString")) {
        TestUtil.logTrace(
            "@onMessage - running  mapMessageConversionTopicTestsInvFormatString - read and verify the message");
        mapMessageConversionTopicTestsInvFormatString(
            (javax.jms.MapMessage) msg);
      }

      else {
        TestUtil.logTrace(
            "@onMessage - invalid message type found in StringProperty");
        TestUtil
            .logTrace("@onMessage - could not find method for this testcase: "
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
   * BytesMessage does not support the concept of a null stream and attempting
   * to write a null into it must throw java.lang.NullPointerException. Jms
   * Specification 1.0.2, Section 3.12
   *
   * create a byte message. Use writeObject to write a null. verify a
   * java.lang.NullPointerException is thrown.
   * 
   */
  private void bytesMsgNullStreamTopicTest() {
    BytesMessage messageSentBytesMessage = null;
    boolean ok = true;
    TextMessage msg = null;
    TestUtil.logTrace("@bytesMsgNullStreamTopicTest");
    try {
      // create a msg sender for the response queue
      mSender = qSession.createSender(queueR);
      // and we'll send a text msg
      msg = qSession.createTextMessage();
      JmsUtil.addPropsToMessage(msg, p);
      msg.setStringProperty("TestCase", "bytesMsgNullStreamTopicTest");
      msg.setText("bytesMsgNullStreamTopicTest");

      TestUtil.logTrace(
          "Writing a null stream to byte message should throw a NullPointerException");
      messageSentBytesMessage = qSession.createBytesMessage();
      // write a null to the message
      messageSentBytesMessage.writeObject(null);
      TestUtil.logTrace(
          "Fail: message did not throw NullPointerException exception as expected");
    } catch (java.lang.NullPointerException np) {
      // this is what we want
      TestUtil.logTrace("Pass: NullPointerException thrown as expected");
      ok = true;
    } catch (JMSException jmsE) {
      TestUtil.printStackTrace(jmsE);
      // we did not get the anticipated exception
      TestUtil.logTrace("Error: " + jmsE.getClass().getName() + " was thrown");
      ok = false;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      // we did not get the anticipated exception here either!
      TestUtil.logTrace("Error: " + e.getClass().getName() + " was thrown");
      ok = false;
    }
    try {
      if (ok)
        msg.setStringProperty("Status", "Pass");
      else
        msg.setStringProperty("Status", "Fail");
      TestUtil.logTrace("Sending response message");
      mSender.send(msg);
    } catch (JMSException je) {
      TestUtil.printStackTrace(je);
      TestUtil.logTrace("Error: " + je.getClass().getName() + " was thrown");
    } catch (Exception ee) {
      TestUtil.printStackTrace(ee);
      TestUtil.logTrace("Error: " + ee.getClass().getName() + " was thrown");
    }
  }

  /*
   * Description: Creates a BytesMessage -. writes to the message using each
   * type of method and as an object. Sends the message to MDB_QUEUE Msg
   * verified by ejb.
   * 
   */
  private void bytesMessageTopicTestsFullMsgCreate() {
    mSender = null;
    TestUtil
        .logTrace("MsgBeanMsgTestT1 - @bytesMessageTopicTestsFullMsgCreate");
    try {
      BytesMessage messageSent = null;
      boolean pass = true;
      boolean booleanValue = false;
      byte byteValue = 127;
      byte[] bytesValue = { 127, -127, 1, 0 };
      char charValue = 'Z';
      double doubleValue = 6.02e23;
      float floatValue = 6.02e23f;
      int intValue = 2147483647;
      long longValue = 9223372036854775807L;
      Integer nInteger = new Integer(-2147483648);
      short shortValue = -32768;
      String utfValue = "what";
      TestUtil.logTrace("Creating 1 message");

      messageSent = qSession.createBytesMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "bytesMessageTopicTestsFullMsg");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace("Writing one of each primitive type to the message");
      // -----------------------------------------------------------------------------

      messageSent.writeBoolean(booleanValue);
      messageSent.writeByte(byteValue);
      messageSent.writeChar(charValue);
      messageSent.writeDouble(doubleValue);
      messageSent.writeFloat(floatValue);
      messageSent.writeInt(intValue);
      messageSent.writeLong(longValue);
      messageSent.writeObject(nInteger);
      messageSent.writeShort(shortValue);
      messageSent.writeUTF(utfValue);
      messageSent.writeBytes(bytesValue);
      messageSent.writeBytes(bytesValue, 0, 1);

      // set up testcase so onMessage invokes the correct method
      messageSent.setStringProperty("TestCase",
          "bytesMessageTopicTestsFullMsg");

      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      // send the message to defaultQueue

      tPublisher = tSession.createPublisher(topic);
      // send the message to another mdb handled Queue
      tPublisher.publish(messageSent);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: reads and verifies BytesMessage created by
   * bytesMessageTopicTestsFullMsgCreate
   */
  private void bytesMessageTopicTestsFullMsg(javax.jms.BytesMessage msg) {
    TestUtil.logTrace("MsgBeanMsgTestT1 - @bytesMessageTopicTestsFullMsg");
    String testCase = "bytesMessageTopicTestsFullMsg";
    try {
      BytesMessage messageSent = null;
      BytesMessage messageReceived = msg;
      boolean pass = true;
      boolean booleanValue = false;
      byte byteValue = 127;
      byte[] bytesValue = { 127, -127, 1, 0 };
      byte[] bytesValueRecvd = { 0, 0, 0, 0 };
      char charValue = 'Z';
      double doubleValue = 6.02e23;
      float floatValue = 6.02e23f;
      int intValue = 2147483647;
      long longValue = 9223372036854775807L;
      Integer nInteger = new Integer(-2147483648);
      short shortValue = -32768;
      String utfValue = "what";

      TestUtil.logTrace("Starting tests in @bytesMessageTopicTestsFullMsg");

      try {
        if (messageReceived.readBoolean() == booleanValue) {
          TestUtil.logTrace("Pass: boolean returned ok");
        } else {
          TestUtil.logTrace("Fail: boolean not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      try {
        if (messageReceived.readByte() == byteValue) {
          TestUtil.logTrace("Pass: Byte returned ok");
        } else {
          TestUtil.logTrace("Fail: Byte not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      try {
        if (messageReceived.readChar() == charValue) {
          TestUtil.logTrace("Pass: correct char");
        } else {
          TestUtil.logTrace("Fail: char not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      try {
        if (messageReceived.readDouble() == doubleValue) {
          TestUtil.logTrace("Pass: correct double");
        } else {
          TestUtil.logTrace("Fail: double not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      try {
        if (messageReceived.readFloat() == floatValue) {
          TestUtil.logTrace("Pass: correct float");
        } else {
          TestUtil.logTrace("Fail: float not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      try {
        if (messageReceived.readInt() == intValue) {
          TestUtil.logTrace("Pass: correct int");
        } else {
          TestUtil.logTrace("Fail: int not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      try {
        if (messageReceived.readLong() == longValue) {
          TestUtil.logTrace("Pass: correct long");
        } else {
          TestUtil.logTrace("Fail: long not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      try {
        if (messageReceived.readInt() == nInteger.intValue()) {
          TestUtil.logTrace("Pass: correct Integer returned");
        } else {
          TestUtil.logTrace("Fail: Integer not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      try {
        if (messageReceived.readShort() == shortValue) {
          TestUtil.logTrace("Pass: correct short");
        } else {
          TestUtil.logTrace("Fail: short not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      try {
        if (messageReceived.readUTF().equals(utfValue)) {
          TestUtil.logTrace("Pass: correct UTF");
        } else {
          TestUtil.logTrace("Fail: UTF not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      try {
        int nCount = messageReceived.readBytes(bytesValueRecvd);
        for (int i = 0; i < nCount; i++) {
          if (bytesValueRecvd[i] != bytesValue[i]) {
            TestUtil.logTrace("Fail: bytes value incorrect");
            pass = false;
          } else {
            TestUtil.logTrace("Pass: byte value " + i + " ok");
          }
        }

      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      try {
        int nCount = messageReceived.readBytes(bytesValueRecvd);
        TestUtil.logTrace("count returned " + nCount);
        if (bytesValueRecvd[0] != bytesValue[0]) {
          TestUtil.logTrace("Fail: bytes value incorrect");
          pass = false;
        } else {
          TestUtil.logTrace("Pass: byte value ok");
        }
        if (nCount == 1) {
          TestUtil.logTrace("Pass: correct count");
        } else {
          TestUtil.logTrace("Fail: count not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      sendTestResults(testCase, pass);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   *                 
   */
  private void mapMessageFullMsgTopicTestCreate() {
    boolean booleanValue = false;
    byte byteValue = 127;
    byte[] bytesValue = { 127, -127, 1, 0 };
    char charValue = 'Z';
    double doubleValue = 6.02e23;
    float floatValue = 6.02e23f;
    int intValue = 2147483647;
    long longValue = 9223372036854775807L;
    short shortValue = 32767;
    String stringValue = "Map Message Test";
    Integer integerValue = Integer.valueOf(100);
    String initial = "spring is here!";
    try {
      MapMessage messageSentMapMessage = null;

      TestUtil.logTrace("Send MapMessage to Topic.");
      messageSentMapMessage = qSession.createMapMessage();
      JmsUtil.addPropsToMessage(messageSentMapMessage, p);
      messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "mapMessageFullMsgTopicTestCreate");

      messageSentMapMessage.setBoolean("booleanValue", booleanValue);
      messageSentMapMessage.setByte("byteValue", byteValue);
      messageSentMapMessage.setBytes("bytesValue", bytesValue);
      messageSentMapMessage.setBytes("bytesValue2", bytesValue, 0, 1);
      messageSentMapMessage.setChar("charValue", charValue);
      messageSentMapMessage.setDouble("doubleValue", doubleValue);
      messageSentMapMessage.setFloat("floatValue", floatValue);
      messageSentMapMessage.setInt("intValue", intValue);
      messageSentMapMessage.setLong("longValue", longValue);
      messageSentMapMessage.setObject("integerValue", integerValue);
      messageSentMapMessage.setShort("shortValue", shortValue);

      messageSentMapMessage.setString("stringValue", stringValue);
      messageSentMapMessage.setString("nullTest", null);
      // set up testcase so onMessage invokes the correct method
      messageSentMapMessage.setStringProperty("TestCase",
          "mapMessageFullMsgTopicTest");

      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      // send the message to defaultQueue

      tPublisher = tSession.createPublisher(topic);
      // send the message to another mdb handled Queue
      tPublisher.publish(messageSentMapMessage);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   *                 
   */
  private void mapMessageFullMsgTopicTest(
      javax.jms.MapMessage messageReceivedMapMessage) {
    String testCase = "mapMessageFullMsgTopicTest";
    boolean pass = true;
    boolean booleanValue = false;
    byte byteValue = 127;
    byte[] bytesValue = { 127, -127, 1, 0 };
    char charValue = 'Z';
    double doubleValue = 6.02e23;
    float floatValue = 6.02e23f;
    int intValue = 2147483647;
    long longValue = 9223372036854775807L;
    short shortValue = 32767;
    String stringValue = "Map Message Test";
    Integer integerValue = Integer.valueOf(100);
    String initial = "spring is here!";
    try {
      try {
        if (messageReceivedMapMessage
            .getBoolean("booleanValue") == booleanValue) {
          TestUtil.logTrace("Pass: valid boolean returned");
        } else {
          TestUtil.logTrace("Fail: invalid boolean returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Fail: unexpected exception " + e.getClass().getName()
            + " was returned");
        pass = false;
      }

      try {
        if (messageReceivedMapMessage.getByte("byteValue") == byteValue) {
          TestUtil.logTrace("Pass: valid byte returned");
        } else {
          TestUtil.logTrace("Fail: invalid byte returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Fail: unexpected exception " + e.getClass().getName()
            + " was returned");
        pass = false;
      }

      try {
        byte[] b = messageReceivedMapMessage.getBytes("bytesValue");
        for (int i = 0; i < b.length; i++) {
          if (b[i] != bytesValue[i]) {
            TestUtil.logTrace("Fail: byte array " + i + " not valid");
            pass = false;
          } else {
            TestUtil.logTrace("Pass: byte array " + i + " valid");
          }
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Fail: unexpected exception " + e.getClass().getName()
            + " was returned");
        pass = false;
      }

      try {
        byte[] b = messageReceivedMapMessage.getBytes("bytesValue2");
        if (b[0] != bytesValue[0]) {
          TestUtil.logTrace("Fail: byte array not valid");
          pass = false;
        } else {
          TestUtil.logTrace("Pass: byte array valid");
        }

      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Fail: unexpected exception " + e.getClass().getName()
            + " was returned");
        pass = false;
      }

      try {
        if (messageReceivedMapMessage.getChar("charValue") == charValue) {
          TestUtil.logTrace("Pass: valid char returned");
        } else {
          TestUtil.logTrace("Fail: invalid char returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Fail: unexpected exception " + e.getClass().getName()
            + " was returned");
        pass = false;
      }

      try {
        if (messageReceivedMapMessage.getDouble("doubleValue") == doubleValue) {
          TestUtil.logTrace("Pass: valid double returned");
        } else {
          TestUtil.logTrace("Fail: invalid double returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Fail: unexpected exception " + e.getClass().getName()
            + " was returned");
        pass = false;
      }

      try {
        if (messageReceivedMapMessage.getFloat("floatValue") == floatValue) {
          TestUtil.logTrace("Pass: valid float returned");
        } else {
          TestUtil.logTrace("Fail: invalid float returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Fail: unexpected exception " + e.getClass().getName()
            + " was returned");
        pass = false;
      }

      try {
        if (messageReceivedMapMessage.getInt("intValue") == intValue) {
          TestUtil.logTrace("Pass: valid int returned");
        } else {
          TestUtil.logTrace("Fail: invalid int returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Fail: unexpected exception " + e.getClass().getName()
            + " was returned");
        pass = false;
      }

      try {
        if (messageReceivedMapMessage.getLong("longValue") == longValue) {
          TestUtil.logTrace("Pass: valid long returned");
        } else {
          TestUtil.logTrace("Fail: invalid long returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Fail: unexpected exception " + e.getClass().getName()
            + " was returned");
        pass = false;
      }

      try {

        if (messageReceivedMapMessage.getObject("integerValue").toString()
            .equals(integerValue.toString())) {
          TestUtil.logTrace("Pass: valid object returned");
        } else {
          TestUtil.logTrace("Fail: invalid object returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Fail: unexpected exception " + e.getClass().getName()
            + " was returned");
        pass = false;
      }

      try {
        if (messageReceivedMapMessage.getShort("shortValue") == shortValue) {
          TestUtil.logTrace("Pass: valid short returned");
        } else {
          TestUtil.logTrace("Fail: invalid short returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Fail: unexpected exception " + e.getClass().getName()
            + " was returned");
        pass = false;
      }

      try {
        if (messageReceivedMapMessage.getString("stringValue")
            .equals(stringValue)) {
          TestUtil.logTrace("Pass: valid string returned");
        } else {
          TestUtil.logTrace("Fail: invalid string returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Fail: unexpected exception " + e.getClass().getName()
            + " was returned");
        pass = false;
      }

      try {
        if (messageReceivedMapMessage.getString("nullTest") == null) {
          TestUtil.logTrace("Pass: null returned");
        } else {

          TestUtil.logTrace("Fail:  null not returned from getString");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Fail: unexpected exception " + e.getClass().getName()
            + " was returned");
        pass = false;
      }
      sendTestResults(testCase, pass);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * 
   * Description: Create a MapMessage -. use MapMessage method writeBoolean to
   * write a boolean to the message.
   */
  private void mapMessageConversionTopicTestsBooleanCreate() {
    try {
      MapMessage messageSent = null;
      MapMessage messageReceived = null;
      boolean booleanValue = true;
      boolean pass = true;

      // set up test tool for Queue
      TestUtil.logTrace("Creating 1 message");
      messageSent = qSession.createMapMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "mapMessageConversionTopicTestsBooleanCreate");
      messageSent.setStringProperty("TestCase",
          "mapMessageConversionTopicTestsBoolean");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for boolean primitive type section 3.11.3");
      // -----------------------------------------------------------------------------

      messageSent.setBoolean("booleanValue", booleanValue);
      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      tPublisher = tSession.createPublisher(topic);
      tPublisher.publish(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * 
   * Descripton: For MapMessages Verify the proper conversion support for
   * boolean as in 3.11.3
   */
  private void mapMessageConversionTopicTestsBoolean(
      javax.jms.MapMessage messageReceived) {
    String testCase = "mapMessageConversionTopicTestsBoolean";
    try {
      boolean booleanValue = true;
      boolean pass = true;

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for boolean primitive type section 3.11.3");
      // -----------------------------------------------------------------------------

      // now test conversions for boolean
      // -----------------------------------------------
      // boolean to boolean - valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readBoolean to read a boolean");
      try {
        if (messageReceived.getBoolean("booleanValue") == booleanValue) {
          TestUtil.logTrace("Pass: boolean to boolean - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // boolean to string valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readString to read a boolean");
      try {
        if (messageReceived.getString("booleanValue")
            .equals((Boolean.valueOf(booleanValue)).toString())) {
          TestUtil.logTrace("Pass: boolean to string - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // boolean to byte[] invalid
      // -----------------------------------------------

      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Use readBytes[] to read a boolean - expect MessageFormatException");
      int nCount = 0;
      try {
        byte[] b = messageReceived.getBytes("booleanValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      TestUtil.logTrace("Count returned from readBytes is : " + nCount);
      // -----------------------------------------------
      // boolean to byte invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Use readByte to read a boolean - expect MessageFormatException");
      try {
        byte b = messageReceived.getByte("booleanValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // boolean to short invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Use readShort to read a boolean - expect MessageFormatException");
      try {
        short s = messageReceived.getShort("booleanValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;

      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // boolean to char invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Use readChar to read a boolean - expect MessageFormatException");
      try {
        char c = messageReceived.getChar("booleanValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // boolean to int invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Use readInt to read a boolean - expect MessageFormatException");
      try {
        int i = messageReceived.getInt("booleanValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // boolean to long invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Use readLong to read a boolean - expect MessageFormatException");
      try {
        long l = messageReceived.getLong("booleanValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // boolean to float invalid
      // -----------------------------------------------

      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Use readFloat to read a boolean - expect MessageFormatException");
      try {
        float f = messageReceived.getFloat("booleanValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;

      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // boolean to double invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Use readDouble to read a boolean - expect MessageFormatException");
      try {
        double d = messageReceived.getDouble("booleanValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;

      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      sendTestResults(testCase, pass);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
  */
  private void mapMessageConversionTopicTestsByteCreate() {
    MapMessage messageSent = null;
    byte byteValue = 127;
    boolean pass = true;
    try {
      messageSent = qSession.createMapMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "mapMessageConversionTopicTestsByteCreate");
      messageSent.setStringProperty("TestCase",
          "mapMessageConversionTopicTestsByte");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");
      // -----------------------------------------------------------------------------

      messageSent.setByte("byteValue", byteValue);
      // send the message and then get it back

      tPublisher = tSession.createPublisher(topic);
      tPublisher.publish(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
  */
  private void mapMessageConversionTopicTestsByte(
      javax.jms.MapMessage messageReceived) {
    String testCase = "mapMessageConversionTopicTestsByte";
    MapMessage messageSent = null;
    byte byteValue = 127;
    boolean pass = true;
    try {
      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");
      // -----------------------------------------------
      // byte to boolean - invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getBoolean to read a byte - this is not valid");
      try {
        boolean b = messageReceived.getBoolean("byteValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // byte to string valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getString to read a byte");
      try {
        if (messageReceived.getString("byteValue")
            .equals(Byte.toString(byteValue))) {
          TestUtil.logTrace("Pass: byte to string - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // byte to byte[] invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Use getBytes[] to read a byte - expect MessageFormatException");
      int nCount = 0;
      try {
        byte[] b = messageReceived.getBytes("byteValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // byte to byte valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getByte to read a byte");
      try {
        if (messageReceived.getByte("byteValue") == byteValue) {
          TestUtil.logTrace("Pass: byte to byte - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // byte to short valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getShort to read a byte");
      try {
        if (messageReceived.getShort("byteValue") == byteValue) {
          TestUtil.logTrace("Pass: byte to short - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // byte to char invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getChar to read a boolean - this is not valid");
      try {
        char c = messageReceived.getChar("byteValue");
        pass = false;
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // byte to int valid
      // -----------------------------------------------

      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getInt to read a byte");
      try {
        if (messageReceived.getInt("byteValue") == byteValue) {
          TestUtil.logTrace("Pass: byte to int - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // byte to long valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getLong to read a byte");
      try {
        if (messageReceived.getLong("byteValue") == byteValue) {
          TestUtil.logTrace("Pass: byte to long - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // byte to float invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getFloat to read a boolean - this is not valid");
      try {
        float f = messageReceived.getFloat("byteValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // byte to double invalid
      // -----------------------------------------------

      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getDouble to read a boolean - this is not valid");
      try {
        double d = messageReceived.getDouble("byteValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      sendTestResults(testCase, pass);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: Create a MapMessage -. use MapMessage method writeShort to
   * write a short.
   */
  public void mapMessageConversionTopicTestsShortCreate() {
    try {
      MapMessage messageSent = null;
      short shortValue = 1;
      boolean pass = true;

      messageSent = qSession.createMapMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "MapMessageConversionTopicTestsShort");
      messageSent.setStringProperty("TestCase",
          "mapMessageConversionTopicTestsShort");
      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");
      // -----------------------------------------------------------------------------

      messageSent.setShort("shortValue", shortValue);
      TestUtil.logTrace("Sending message");
      tPublisher = tSession.createPublisher(topic);
      tPublisher.publish(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: MapMessage -. Verify the proper conversion support as in
   * 3.11.3
   */
  private void mapMessageConversionTopicTestsShort(
      javax.jms.MapMessage messageReceived) {
    String testCase = "mapMessageConversionTopicTestsShort";
    try {
      short shortValue = 1;
      boolean pass = true;

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");
      // -----------------------------------------------------------------------------

      // now test conversions for byte
      // -----------------------------------------------
      // short to boolean - invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getBoolean to read a short - this is not valid");
      try {
        boolean b = messageReceived.getBoolean("shortValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // short to string valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getString to read a short");
      try {
        if (messageReceived.getString("shortValue")
            .equals(Short.toString(shortValue))) {
          TestUtil.logTrace("Pass: short to string - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // short to byte[] invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Use getBytes[] to read a short - expect MessageFormatException");
      try {
        byte[] b = messageReceived.getBytes("shortValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // short to byte invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getByte to read a short - this is not valid");
      try {
        byte b = messageReceived.getByte("shortValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // short to short valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getShort to read a short");
      try {
        if (messageReceived.getShort("shortValue") == shortValue) {
          TestUtil.logTrace("Pass: short to short - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // short to char invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getChar to read a short - this is not valid");
      try {
        char c = messageReceived.getChar("shortValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // short to int valid
      // -----------------------------------------------

      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getInt to read a short");
      try {
        if (messageReceived.getInt("shortValue") == shortValue) {
          TestUtil.logTrace("Pass: short to int - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // short to long valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getLong to read a short");
      try {
        if (messageReceived.getLong("shortValue") == shortValue) {
          TestUtil.logTrace("Pass: short to long - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // short to float invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getFloat to read a short - this is not valid");
      try {
        float f = messageReceived.getFloat("shortValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // short to double invalid
      // -----------------------------------------------

      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getDouble to read a short - this is not valid");
      try {
        double d = messageReceived.getDouble("shortValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      sendTestResults(testCase, pass);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: Create a MapMessage -. use MapMessage method writeChar to
   * write a char. Verify the proper conversion support as in 3.11.3
   * 
   */
  private void mapMessageConversionTopicTestsCharCreate() {
    try {
      MapMessage messageSent = null;
      char charValue = 'a';
      boolean pass = true;
      messageSent = qSession.createMapMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "mapMessageConversionTopicTestsChar");
      messageSent.setStringProperty("TestCase",
          "mapMessageConversionTopicTestsChar");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");
      // -----------------------------------------------------------------------------
      messageSent.setChar("charValue", charValue);
      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      tPublisher = tSession.createPublisher(topic);
      tPublisher.publish(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: Create a MapMessage -. use MapMessage method writeInt to write
   * an int. Verify the proper conversion support as in 3.11.3
   * 
   */
  private void mapMessageConversionTopicTestsIntCreate() {
    try {
      MapMessage messageSent = null;
      int intValue = 6;
      boolean pass = true;

      // set up test tool for Queue
      messageSent = qSession.createMapMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "mapMessageConversionTopicTestsIntCreate");
      messageSent.setStringProperty("TestCase",
          "mapMessageConversionTopicTestsInt");
      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");
      // -----------------------------------------------------------------------------

      messageSent.setInt("intValue", intValue);
      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      tPublisher = tSession.createPublisher(topic);
      tPublisher.publish(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: Create a MapMessage -. use MapMessage method writeLong to
   * write a long. Verify the proper conversion support as in 3.11.3
   * 
   */
  private void mapMessageConversionTopicTestsLongCreate() {
    try {
      MapMessage messageSent = null;
      long longValue = 2;
      boolean pass = true;

      TestUtil.logTrace("Creating 1 message");
      messageSent = qSession.createMapMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "mapMessageConversionTopicTestsLongCreate");
      messageSent.setStringProperty("TestCase",
          "mapMessageConversionTopicTestsLong");
      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");
      // -----------------------------------------------------------------------------

      messageSent.setLong("longValue", longValue);
      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      tPublisher = tSession.createPublisher(topic);
      tPublisher.publish(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: Create a MapMessage -. use MapMessage method writeFloat to
   * write a float. Verify the proper conversion support as in 3.11.3
   * 
   */
  private void mapMessageConversionTopicTestsFloatCreate() {
    try {
      MapMessage messageSent = null;
      float floatValue = 5;
      boolean pass = true;

      messageSent = qSession.createMapMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "mapMessageConversionTopicTestsFloatCreate");
      messageSent.setStringProperty("TestCase",
          "mapMessageConversionTopicTestsFloat");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");
      // -----------------------------------------------------------------------------

      messageSent.setFloat("floatValue", floatValue);
      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      tPublisher = tSession.createPublisher(topic);
      tPublisher.publish(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: Create a MapMessage -. use MapMessage method writeDouble to
   * write a double. Verify the proper conversion support as in 3.11.3
   * 
   */
  private void mapMessageConversionTopicTestsDoubleCreate() {
    try {
      MapMessage messageSent = null;
      double doubleValue = 3;
      boolean pass = true;

      messageSent = qSession.createMapMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "mapMessageConversionTopicTestsDoubleCreate");
      messageSent.setStringProperty("TestCase",
          "mapMessageConversionTopicTestsDouble");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");
      // -----------------------------------------------------------------------------
      messageSent.setDouble("doubleValue", doubleValue);
      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      tPublisher = tSession.createPublisher(topic);
      tPublisher.publish(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: Create a MapMessage -. use MapMessage method writeString to
   * write a string. Verify the proper conversion support as in 3.11.3
   * 
   */
  private void mapMessageConversionTopicTestsStringCreate() {
    try {
      MapMessage messageSent = null;
      boolean pass = true;
      String myString = "10";
      String myString2 = "true";

      messageSent = qSession.createMapMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "mapMessageConversionTopicTestsStringCreate");
      messageSent.setStringProperty("TestCase",
          "mapMessageConversionTopicTestsString");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");
      // -----------------------------------------------------------------------------
      messageSent.setString("myString", myString);
      messageSent.setString("myString2", myString2);
      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      tPublisher = tSession.createPublisher(topic);
      tPublisher.publish(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * 
   * Description: Create a MapMessage -. use MapMessage method writeBytes to
   * write a byte[] to the message. Verify the proper conversion support as in
   * 3.11.3
   */
  private void mapMessageConversionTopicTestsBytesCreate() {
    try {
      MapMessage messageSent = null;
      byte[] byteValues = { 1, 2, 3 };
      boolean pass = true;

      messageSent = qSession.createMapMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "mapMessageConversionTopicTestsBytesCreate");
      messageSent.setStringProperty("TestCase",
          "mapMessageConversionTopicTestsBytes");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte[] primitive type section 3.11.3");
      // -----------------------------------------------------------------------------

      messageSent.setBytes("byteValues", byteValues);
      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      tPublisher = tSession.createPublisher(topic);
      tPublisher.publish(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * 
   * Description: Create a MapMessage -. use MapMessage method setString to
   * write a text string of "mytest string". Verify NumberFormatException is
   * thrown
   * 
   */
  private void mapMessageConversionTopicTestsInvFormatStringCreate() {
    try {
      MapMessage messageSent = null;
      boolean pass = true;
      String myString = "mytest string";

      messageSent = qSession.createMapMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "mapMessageConversionTopicTestsInvFormatStringCreate");
      messageSent.setStringProperty("TestCase",
          "mapMessageConversionTopicTestsInvFormatString");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");
      // -----------------------------------------------------------------------------

      messageSent.setString("myString", myString);
      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      TestUtil.logTrace("Sending message");
      tPublisher = tSession.createPublisher(topic);
      tPublisher.publish(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: use MapMessage method writeChar to write a char. Verify the
   * proper conversion support as in 3.11.3
   */
  private void mapMessageConversionTopicTestsChar(
      javax.jms.MapMessage messageReceived) {
    String testCase = "mapMessageConversionTopicTestsChar";
    try {
      char charValue = 'a';
      boolean pass = true;

      // now test conversions for byte
      // -----------------------------------------------
      // char to boolean - invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getBoolean to read a char - this is not valid");
      try {
        boolean b = messageReceived.getBoolean("charValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // char to string valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getString to read a char");
      try {
        if (messageReceived.getString("charValue")
            .equals(Character.valueOf(charValue).toString())) {
          TestUtil.logTrace("Pass: char to string - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // char to byte[] invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Use getBytes[] to read a char - expect MessageFormatException");
      try {
        byte[] b = messageReceived.getBytes("charValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // char to byte invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getByte to read a char - this is not valid");
      try {

        byte b = messageReceived.getByte("charValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // char to short invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getShort to read a char");
      try {
        short s = messageReceived.getShort("charValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // char to char valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getChar to read a char ");
      try {
        if (messageReceived.getChar("charValue") == charValue) {
          TestUtil.logTrace("Pass: char to char - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // char to int invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getInt to read a char ");
      try {
        int i = messageReceived.getInt("charValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // char to long invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getLong to read a char");
      try {
        long l = messageReceived.getLong("charValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // char to float invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getFloat to read a char - this is not valid");
      try {
        float f = messageReceived.getFloat("charValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // char to double invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getDouble to read a char - this is not valid");
      try {
        double d = messageReceived.getDouble("charValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      sendTestResults(testCase, pass);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: use MapMessage method writeInt to write an int. Verify the
   * proper conversion support as in 3.11.3
   * 
   */
  private void mapMessageConversionTopicTestsInt(
      javax.jms.MapMessage messageReceived) {
    String testCase = "mapMessageConversionTopicTestsInt";
    try {

      int intValue = 6;
      boolean pass = true;
      // now test conversions for byte
      // -----------------------------------------------
      // int to boolean - invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getBoolean to read an int - this is not valid");
      try {
        boolean b = messageReceived.getBoolean("intValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // int to string valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getString to read an int");
      try {
        if (messageReceived.getString("intValue")
            .equals(Integer.toString(intValue))) {
          TestUtil.logTrace("Pass: int to string - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // int to byte[] invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Use getBytes[] to read an int - expect MessageFormatException");
      int nCount = 0;
      try {
        byte[] b = messageReceived.getBytes("intValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // int to byte invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getByte to read an int - this is not valid");
      try {
        byte b = messageReceived.getByte("intValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // int to short invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getShort to read an int");
      try {
        short s = messageReceived.getShort("intValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // int to char invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getChar to read an int - this is not valid");
      try {
        char c = messageReceived.getChar("intValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // int to int valid
      // -----------------------------------------------

      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getInt to read an int");
      try {
        if (messageReceived.getInt("intValue") == intValue) {
          TestUtil.logTrace("Pass: int to int - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // int to long valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getLong to read an int");
      try {
        if (messageReceived.getLong("intValue") == intValue) {
          TestUtil.logTrace("Pass: int to long - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // int to float invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getFloat to read an int - this is not valid");
      try {
        float f = messageReceived.getFloat("intValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // int to double invalid
      // -----------------------------------------------

      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getDouble to read an int - this is not valid");
      try {
        double d = messageReceived.getDouble("intValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      sendTestResults(testCase, pass);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: use MapMessage method writeLong to write a long. Verify the
   * proper conversion support as in 3.11.3
   * 
   */
  private void mapMessageConversionTopicTestsLong(
      javax.jms.MapMessage messageReceived) {
    String testCase = "mapMessageConversionTopicTestsLong";
    try {

      long longValue = 2;
      boolean pass = true;
      // now test conversions for byte
      // -----------------------------------------------
      // long to boolean - invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getBoolean to read a long - this is not valid");
      try {
        boolean b = messageReceived.getBoolean("longValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // long to string valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getString to read a long");
      try {
        if (messageReceived.getString("longValue")
            .equals(Long.toString(longValue))) {
          TestUtil.logTrace("Pass: long to string - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // long to byte[] invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Use getBytes[] to read  a long - expect MessageFormatException");
      try {
        byte[] b = messageReceived.getBytes("longValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // long to byte invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getByte to read an long - this is not valid");
      try {
        byte b = messageReceived.getByte("longValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // long to short invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getShort to read a long");
      try {
        short s = messageReceived.getShort("longValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // long to char invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getChar to read a long - this is not valid");
      try {
        char c = messageReceived.getChar("longValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // long to int invalid
      // -----------------------------------------------

      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getInt to read a long");
      try {
        int i = messageReceived.getInt("longValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // long to long valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getLong to read a long");
      try {
        if (messageReceived.getLong("longValue") == longValue) {
          TestUtil.logTrace("Pass: int to long - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // long to float invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getFloat to read a long - this is not valid");
      try {
        float f = messageReceived.getFloat("longValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // long to double invalid
      // -----------------------------------------------

      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getDouble to read a long ");
      try {
        double d = messageReceived.getDouble("longValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      sendTestResults(testCase, pass);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: use MapMessage method writeFloat to write a float. Verify the
   * proper conversion support as in 3.11.3
   * 
   */
  private void mapMessageConversionTopicTestsFloat(
      javax.jms.MapMessage messageReceived) {
    String testCase = "mapMessageConversionTopicTestsFloat";
    try {
      float floatValue = 5;
      boolean pass = true;
      // now test conversions for byte
      // -----------------------------------------------
      // float to boolean - invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getBoolean to read a float  ");
      try {
        boolean b = messageReceived.getBoolean("floatValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // float to string valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getString to read a float");
      try {
        if (messageReceived.getString("floatValue")
            .equals(Float.toString(floatValue))) {
          TestUtil.logTrace("Pass: float to string - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // float to byte[] invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getBytes[] to read  a float ");
      try {
        byte[] b = messageReceived.getBytes("floatValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // float to byte invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getByte to read a float  ");
      try {
        byte b = messageReceived.getByte("floatValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // float to short invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getShort to read a float");
      try {
        short s = messageReceived.getShort("floatValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // float to char invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getChar to read a long  ");
      try {
        char c = messageReceived.getChar("floatValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // float to int invalid
      // -----------------------------------------------

      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getInt to read a float");
      try {
        int i = messageReceived.getInt("floatValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // float to long invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getLong to read a long");
      try {
        long l = messageReceived.getLong("floatValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // float to float valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getFloat to read a float  ");
      try {
        if (messageReceived.getFloat("floatValue") == floatValue) {
          TestUtil.logTrace("Pass: float to float - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // float to double valid
      // -----------------------------------------------

      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getDouble to read a float  ");
      try {
        if (messageReceived.getDouble("floatValue") == floatValue) {
          TestUtil.logTrace("Pass: float to double - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      sendTestResults(testCase, pass);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: use MapMessage method writeDouble to write a double. Verify
   * the proper conversion support as in 3.11.3
   * 
   */
  private void mapMessageConversionTopicTestsDouble(
      javax.jms.MapMessage messageReceived) {
    String testCase = "mapMessageConversionTopicTestsDouble";

    try {

      double doubleValue = 3;
      boolean pass = true;
      // now test conversions for byte
      // -----------------------------------------------
      // double to boolean - invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getBoolean to read a double  ");
      try {
        boolean b = messageReceived.getBoolean("doubleValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // double to string valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getString to read a double");
      try {
        if (messageReceived.getString("doubleValue")
            .equals(Double.toString(doubleValue))) {
          TestUtil.logTrace("Pass: double to string");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // double to byte[] invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getBytes[] to read  a double ");
      try {
        byte[] b = messageReceived.getBytes("doubleValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // double to byte invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getByte to read a double  ");
      try {
        byte b = messageReceived.getByte("doubleValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // double to short invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getShort to read a double");
      try {
        short s = messageReceived.getShort("doubleValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // double to char invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getChar to read a double  ");
      try {
        char c = messageReceived.getChar("doubleValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // double to int invalid
      // -----------------------------------------------

      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getInt to read a double");
      try {
        int i = messageReceived.getInt("doubleValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // double to long invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getLong to read a double");
      try {
        long l = messageReceived.getLong("doubleValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // double to float invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getFloat to read a double  ");
      try {
        float f = messageReceived.getFloat("doubleValue");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // double to double valid
      // -----------------------------------------------

      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getDouble to read an float  ");
      try {
        if (messageReceived.getDouble("doubleValue") == doubleValue) {
          TestUtil.logTrace("Pass: double to double ");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      sendTestResults(testCase, pass);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: use MapMessage method writeString to write a string. Verify
   * the proper conversion support as in 3.11.3
   * 
   */
  private void mapMessageConversionTopicTestsString(
      javax.jms.MapMessage messageReceived) {
    String testCase = "mapMessageConversionTopicTestsString";

    try {

      boolean pass = true;
      String myString = "10";
      String myString2 = "true";
      // now test conversions for String
      // -----------------------------------------------
      // string to string valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getString to read a String");
      try {
        if (messageReceived.getString("myString").equals(myString)) {
          TestUtil.logTrace("Pass: string to string - valid");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // string to byte[] invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getBytes[] to read a String");
      try {
        byte[] b = messageReceived.getBytes("myString");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // String to byte valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getByte to read a String");
      try {
        if (messageReceived.getByte("myString") == Byte.parseByte(myString)) {
          TestUtil.logTrace("Pass: String to byte ");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // string to short valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getShort to read a string");
      try {
        if (messageReceived.getShort("myString") == Short
            .parseShort(myString)) {
          TestUtil.logTrace("Pass: String to short ");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // String to char invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getChar to read a String ");
      try {
        char c = messageReceived.getChar("myString");
        TestUtil.logTrace("getChar returned " + c);
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // string to int valid
      // -----------------------------------------------

      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getInt to read a String");
      try {
        if (messageReceived.getInt("myString") == Integer.parseInt(myString)) {
          TestUtil.logTrace("Pass: String to int ");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // string to long valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getLong to read a String");
      try {
        if (messageReceived.getLong("myString") == Long.parseLong(myString)) {
          TestUtil.logTrace("Pass: String to long ");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // String to float valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getFloat to read a String");
      try {
        if (messageReceived.getFloat("myString") == Float
            .parseFloat(myString)) {
          TestUtil.logTrace("Pass: String to float ");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // String to double valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getDouble to read a String");
      try {

        if (messageReceived.getDouble("myString") == Double
            .parseDouble(myString)) {
          TestUtil.logTrace("Pass: String to double ");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // String to boolean
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getBoolean to read a string ");
      try {
        if (messageReceived.getBoolean("myString2") == Boolean
            .valueOf(myString2).booleanValue()) {
          TestUtil.logTrace("Pass: String to boolean ");
        } else {
          TestUtil.logTrace("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // String to boolean
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getBoolean to read a string that is not true");
      try {
        boolean b = messageReceived.getBoolean("myString");
        if (b != false) {
          TestUtil.logTrace("Fail: !true should have returned false");
          pass = false;
        } else {
          TestUtil.logTrace("Pass: !true returned false");
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      sendTestResults(testCase, pass);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: use MapMessage method writeBytes to write a byte[] to the
   * message. Verify the proper conversion support as in 3.11.3
   */
  private void mapMessageConversionTopicTestsBytes(
      javax.jms.MapMessage messageReceived) {
    String testCase = "mapMessageConversionTopicTestsBytes";

    try {

      byte[] byteValues = { 1, 2, 3 };
      boolean pass = true;

      // now test conversions for boolean
      // -----------------------------------------------
      // byte[] to byte[] - valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getBytes[] to read a byte[] ");
      try {
        byte[] b = messageReceived.getBytes("byteValues");
        for (int i = 0; i < b.length; i++) {
          if (b[i] != byteValues[i]) {
            TestUtil.logTrace("Fail: byte[] value returned is invalid");
            pass = false;
          } else {
            TestUtil.logTrace("Pass: byte[] returned is valid");
          }
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // byte[] to boolean - invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getBoolean to read a byte[]");
      try {
        boolean b = messageReceived.getBoolean("byteValues");
        TestUtil.logTrace(
            "Fail: byte[] to boolean conversion should have thrown MessageFormatException");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // byte[] to string invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getString to read a byte[]");
      try {
        String s = messageReceived.getString("byteValues");
        TestUtil.logTrace(
            "Fail: byte[] to boolean conversion should have thrown MessageFormatException");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // byte[] to byte invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Use getByte to read a byte[] - expect MessageFormatException");
      try {
        byte b = messageReceived.getByte("byteValues");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // byte[] to short invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Use getShort to read a byte[] - expect MessageFormatException");
      try {
        short s = messageReceived.getShort("byteValues");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // byte[] to char invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Use getChar to read a byte[] - expect MessageFormatException");
      try {

        char c = messageReceived.getChar("byteValues");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // byte[] to int invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Use getInt to read a byte[] - expect MessageFormatException");
      try {
        int i = messageReceived.getInt("byteValues");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // byte[] to long invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Use getLong to read a byte[] - expect MessageFormatException");
      try {
        long l = messageReceived.getLong("byteValues");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // byte[] to float invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Use getFloat to read a byte[] - expect MessageFormatException");
      try {
        float f = messageReceived.getFloat("byteValues");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // byte[] to double invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Use getDouble to read a byte[] - expect MessageFormatException");
      try {
        double d = messageReceived.getDouble("byteValues");
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      sendTestResults(testCase, pass);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: use MapMessage method setString to write a text string of
   * "mytest string". Verify NumberFormatException is thrown
   * 
   */
  private void mapMessageConversionTopicTestsInvFormatString(
      javax.jms.MapMessage messageReceived) {
    String testCase = "mapMessageConversionTopicTestsInvFormatString";

    try {
      boolean pass = true;
      String myString = "mytest string";

      // -----------------------------------------------
      // String to byte
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getByte to read a String that is not valid ");
      try {
        byte b = messageReceived.getByte("myString");
        TestUtil.logTrace("Fail: java.lang.NumberFormatException expected");
        pass = false;
      } catch (NumberFormatException nf) {
        TestUtil.logTrace("Pass: NumberFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // string to short
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getShort to read a string that is not valid ");
      try {
        short s = messageReceived.getShort("myString");
        TestUtil.logTrace("Fail: NumberFormatException was expected");
        pass = false;
      } catch (NumberFormatException nf) {
        TestUtil.logTrace("Pass: NumberFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // string to int
      // -----------------------------------------------

      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getInt to read a String that is not valid ");
      try {
        int i = messageReceived.getInt("myString");
        TestUtil.logTrace("Fail: NumberFormatException was expected");
        pass = false;
      } catch (NumberFormatException nf) {
        TestUtil.logTrace("Pass: NumberFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // string to long
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getLong to read a String that is not valid ");
      try {
        long l = messageReceived.getLong("myString");
        TestUtil.logTrace("Fail: NumberFormatException was expected");
        pass = false;
      } catch (NumberFormatException nf) {
        TestUtil.logTrace("Pass: NumberFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // String to float
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getFloat to read a String that is not valid ");
      try {
        float f = messageReceived.getFloat("myString");
        TestUtil.logTrace("Fail: NumberFormatException was expected");
        pass = false;
      } catch (NumberFormatException nf) {
        TestUtil.logTrace("Pass: NumberFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // String to double
      // -----------------------------------------------

      TestUtil.logTrace("--");
      TestUtil.logTrace("Use getDouble to read a String that is not valid ");
      try {
        double d = messageReceived.getDouble("myString");
        TestUtil.logTrace("Fail: NumberFormatException was expected");
        pass = false;
      } catch (NumberFormatException nf) {
        TestUtil.logTrace("Pass: NumberFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      sendTestResults(testCase, pass);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
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
        "jms.ee.mdb.mdb_msgTypesT1  In MsgBeanMsgTestT1::setMessageDrivenContext()!!");
    this.mdc = mdc;
  }

  public void ejbRemove() {
    TestUtil
        .logTrace("jms.ee.mdb.mdb_msgTypesT1  In MsgBeanMsgTestT1::remove()!!");
  }
}
