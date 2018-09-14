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

package com.sun.ts.tests.jms.ee.mdb.mdb_msgTypesQ2;

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

public class MsgBeanMsgTestQ2 implements MessageDrivenBean, MessageListener {

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

  public MsgBeanMsgTestQ2() {
    TestUtil.logTrace("@MsgBeanMsgTestQ2()!");
  };

  public void ejbCreate() {
    TestUtil.logTrace(
        "jms.ee.mdb.mdb_msgTypesQ2  - @MsgBeanMsgTestQ2-ejbCreate() !!");
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
    TestUtil.logTrace("from jms.ee.mdb.mdb_msgTypesQ2 @onMessage!" + msg);

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
          .equals("messageObjectCopyQTestCreate")) {
        TestUtil.logTrace(
            "@onMessage - running messageObjectCopyQTestCreate - create the message");
        messageObjectCopyQTestCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("messageObjectCopyQTest")) {
        TestUtil.logTrace(
            "@onMessage - running messageObjectCopyQTest - read and verify the message");
        messageObjectCopyQTest((javax.jms.ObjectMessage) msg);
      }
      if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsBooleanCreate")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsBooleanCreate - create the message");
        streamMessageConversionQTestsBooleanCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsBoolean")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsBoolean - read and verify the message");
        streamMessageConversionQTestsBoolean((javax.jms.StreamMessage) msg);
      }

      else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsByteCreate")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsByteCreate - create the message");
        streamMessageConversionQTestsByteCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsByte")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsByte - read and verify the message");
        streamMessageConversionQTestsByte((javax.jms.StreamMessage) msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsShortCreate")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsShortCreate - create the message");
        streamMessageConversionQTestsShortCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsShort")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsShort - read and verify the message");
        streamMessageConversionQTestsShort((javax.jms.StreamMessage) msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsIntCreate")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsIntCreate - create the message");
        streamMessageConversionQTestsIntCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsInt")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsInt - read and verify the message");
        streamMessageConversionQTestsInt((javax.jms.StreamMessage) msg);
      } else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsLongCreate")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsLongCreate - create the message");
        streamMessageConversionQTestsLongCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsLong")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsLong - read and verify the message");
        streamMessageConversionQTestsLong((javax.jms.StreamMessage) msg);
      }

      else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsFloatCreate")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsFloatCreate - create the message");
        streamMessageConversionQTestsFloatCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsFloat")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsFloat - read and verify the message");
        streamMessageConversionQTestsFloat((javax.jms.StreamMessage) msg);
      }

      else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsDoubleCreate")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsDoubleCreate - create the message");
        streamMessageConversionQTestsDoubleCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsDouble")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsDouble - read and verify the message");
        streamMessageConversionQTestsDouble((javax.jms.StreamMessage) msg);
      }

      else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsStringCreate")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsStringCreate - create the message");
        streamMessageConversionQTestsStringCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsString")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsString - read and verify the message");
        streamMessageConversionQTestsString((javax.jms.StreamMessage) msg);
      }

      else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsCharCreate")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsCharCreate - create the message");
        streamMessageConversionQTestsCharCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsChar")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsChar - read and verify the message");
        streamMessageConversionQTestsChar((javax.jms.StreamMessage) msg);
      }

      else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsBytesCreate")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsBytesCreate - create the message");
        streamMessageConversionQTestsBytesCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsBytes")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsBytes - read and verify the message");
        streamMessageConversionQTestsBytes((javax.jms.StreamMessage) msg);
      }

      else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsInvFormatStringCreate")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsInvFormatStringCreate - create the message");
        streamMessageConversionQTestsInvFormatStringCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("streamMessageConversionQTestsInvFormatString")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageConversionQTestsInvFormatString - read and verify the message");
        streamMessageConversionQTestsInvFormatString(
            (javax.jms.StreamMessage) msg);
      }

      else if (msg.getStringProperty("TestCase")
          .equals("streamMessageQTestsFullMsgCreate")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageQTestsFullMsgCreate - create the message");
        streamMessageQTestsFullMsgCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("streamMessageQTestsFullMsg")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageQTestsFullMsg - read and verify the message");
        streamMessageQTestsFullMsg((javax.jms.StreamMessage) msg);
      }

      else if (msg.getStringProperty("TestCase")
          .equals("streamMessageQTestNullCreate")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageQTestNullCreate - create the message");
        streamMessageQTestNullCreate();
      } else if (msg.getStringProperty("TestCase")
          .equals("streamMessageQTestNull")) {
        TestUtil.logTrace(
            "@onMessage - running streamMessageQTestNull - read and verify the message");
        streamMessageQTestNull((javax.jms.StreamMessage) msg);
      }

      else {
        TestUtil.logTrace(
            "@onMessage - invalid message type found in StringProperty");
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
   * Description: Create an object message. Write a StringBuffer to the message.
   * modify the StringBuffer and send the msg, verify that it does not effect
   * the msg
   */
  public void messageObjectCopyQTestCreate() {
    boolean pass = true;
    try {
      ObjectMessage messageSentObjectMsg = null;
      StringBuffer sBuff = new StringBuffer("This is");
      String initial = "This is";
      messageSentObjectMsg = qSession.createObjectMessage();
      JmsUtil.addPropsToMessage(messageSentObjectMsg, p);
      messageSentObjectMsg.setObject(sBuff);
      sBuff.append("a test ");
      messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "messageObjectCopyQTest");
      // set up testcase so onMessage invokes the correct method
      messageSentObjectMsg.setStringProperty("TestCase",
          "messageObjectCopyQTest");

      mSender = qSession.createSender(queue);
      mSender.send(messageSentObjectMsg);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * 
   * Description: Create a StreamMessage -. use StreamMessage method
   * writeBoolean to write a boolean to the message. Verify the proper
   * conversion support as in 3.11.3
   */
  private void streamMessageConversionQTestsBooleanCreate() {
    try {
      StreamMessage messageSent = null;
      boolean abool = true;

      messageSent = qSession.createStreamMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionQTestsBoolean");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for boolean primitive type section 3.11.3");
      // -----------------------------------------------------------------------------
      // set up testcase so onMessage invokes the correct method
      messageSent.setStringProperty("TestCase",
          "streamMessageConversionQTestsBoolean");
      messageSent.writeBoolean(abool);
      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * 
   * Description: Create a StreamMessage -. use StreamMessage method writeByte
   * to write a byte. Verify the proper conversion support as in 3.11.3
   * 
   */
  private void streamMessageConversionQTestsByteCreate() {
    try {
      StreamMessage messageSent = null;

      byte bValue = 127;
      messageSent = qSession.createStreamMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionQTestsByte");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");
      // -----------------------------------------------------------------------------
      // set up testcase so onMessage invokes the correct method
      messageSent.setStringProperty("TestCase",
          "streamMessageConversionQTestsByte");

      messageSent.writeByte(bValue);
      // send the message and then get it back
      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: Create a StreamMessage -. use StreamMessage method writeShort
   * to write a short. Verify the proper conversion support as in 3.11.3
   * 
   */
  private void streamMessageConversionQTestsShortCreate() {
    try {
      StreamMessage messageSent = null;
      short sValue = 1;
      messageSent = qSession.createStreamMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionQTestsShort");
      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");
      // -----------------------------------------------------------------------------
      // set up testcase so onMessage invokes the correct method
      messageSent.setStringProperty("TestCase",
          "streamMessageConversionQTestsShort");
      messageSent.writeShort(sValue);
      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: Create a StreamMessage -. use StreamMessage method writeInt to
   * write an int. Verify the proper conversion support as in 3.11.3
   * 
   */
  private void streamMessageConversionQTestsIntCreate() {
    try {
      StreamMessage messageSent = null;
      int iValue = 6;

      messageSent = qSession.createStreamMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionQTestsInt");
      // set up testcase so onMessage invokes the correct method
      messageSent.setStringProperty("TestCase",
          "streamMessageConversionQTestsInt");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");
      // -----------------------------------------------------------------------------
      messageSent.writeInt(iValue);
      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: Create a StreamMessage -. use StreamMessage method writeLong
   * to write a long. Verify the proper conversion support as in 3.11.3
   * 
   */
  private void streamMessageConversionQTestsLongCreate() {
    try {
      StreamMessage messageSent = null;
      long lValue = 2;
      messageSent = qSession.createStreamMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionQTestsLong");
      // set up testcase so onMessage invokes the correct method
      messageSent.setStringProperty("TestCase",
          "streamMessageConversionQTestsLong");
      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");
      // -----------------------------------------------------------------------------
      messageSent.writeLong(lValue);
      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: Create a StreamMessage -. use StreamMessage method writeFloat
   * to write a float. Verify the proper conversion support as in 3.11.3
   * 
   */
  private void streamMessageConversionQTestsFloatCreate() {
    try {
      StreamMessage messageSent = null;

      float fValue = 5;
      messageSent = qSession.createStreamMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionQTestsFloat");
      // set up testcase so onMessage invokes the correct method
      messageSent.setStringProperty("TestCase",
          "streamMessageConversionQTestsFloat");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");
      // -----------------------------------------------------------------------------

      messageSent.writeFloat(fValue);
      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * 
   * Description: Create a StreamMessage -. use StreamMessage method writeDouble
   * to write a double. Verify the proper conversion support as in 3.11.3
   * 
   */
  private void streamMessageConversionQTestsDoubleCreate() {
    try {
      StreamMessage messageSent = null;

      double dValue = 3;
      messageSent = qSession.createStreamMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionQTestsDouble");
      // set up testcase so onMessage invokes the correct method
      messageSent.setStringProperty("TestCase",
          "streamMessageConversionQTestsDouble");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");
      // -----------------------------------------------------------------------------

      messageSent.writeDouble(dValue);
      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: Create a StreamMessage -. use StreamMessage method writeString
   * to write a string. Verify the proper conversion support as in 3.11.3
   * 
   */
  private void streamMessageConversionQTestsStringCreate() {
    try {
      StreamMessage messageSent = null;
      String myString = "10";

      messageSent = qSession.createStreamMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionQTestsString");
      // set up testcase so onMessage invokes the correct method
      messageSent.setStringProperty("TestCase",
          "streamMessageConversionQTestsString");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");
      // -----------------------------------------------------------------------------

      messageSent.writeString(myString);
      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: Create a StreamMessage -. use StreamMessage method writeChar
   * to write a char. Verify the proper conversion support as in 3.11.3
   * 
   */
  private void streamMessageConversionQTestsCharCreate() {
    try {
      StreamMessage messageSent = null;

      char charValue = 'a';
      messageSent = qSession.createStreamMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionQTestsChar");
      // set up testcase so onMessage invokes the correct method
      messageSent.setStringProperty("TestCase",
          "streamMessageConversionQTestsChar");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");
      // -----------------------------------------------------------------------------

      messageSent.writeChar(charValue);
      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * 
   * Description: Create a StreamMessage -. use StreamMessage method writeBytes
   * to write a byte[] to the message. Verify the proper conversion support as
   * in 3.11.3
   */
  private void streamMessageConversionQTestsBytesCreate() {
    try {
      StreamMessage messageSent = null;
      byte[] bValues = { 1, 2, 3 };
      messageSent = qSession.createStreamMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionQTestsBytes");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte[] primitive type section 3.11.3");
      // -----------------------------------------------------------------------------
      // set up testcase so onMessage invokes the correct method
      messageSent.setStringProperty("TestCase",
          "streamMessageConversionQTestsBytes");

      messageSent.writeBytes(bValues);
      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * 
   * Description: Create a StreamMessage -. use StreamMessage method writeString
   * to write a text string of "mytest string". Verify NumberFormatException is
   * thrown Verify that the pointer was not incremented by doing a read string
   * 
   */
  private void streamMessageConversionQTestsInvFormatStringCreate() {
    try {
      StreamMessage messageSent = null;
      String myString = "mytest string";
      messageSent = qSession.createStreamMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionQTestsInvFormatString");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");
      // -----------------------------------------------------------------------------
      // set up testcase so onMessage invokes the correct method
      messageSent.setStringProperty("TestCase",
          "streamMessageConversionQTestsInvFormatString");
      messageSent.writeString(myString);
      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * 
   * Description: Create a StreamMessage -. write one of each primitive type.
   * Send the message. Verify the data received was as sent.
   * 
   */
  private void streamMessageQTestsFullMsgCreate() {
    try {

      StreamMessage messageSent = null;

      byte bValue = 127;
      boolean abool = false;
      byte[] bValues = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
      byte[] bValues2 = { 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
      char charValue = 'Z';
      short sValue = 32767;
      long lValue = 9223372036854775807L;
      double dValue = 6.02e23;
      float fValue = 6.02e23f;
      int iValue = 6;
      boolean pass = true;
      String myString = "text";
      String sTesting = "Testing StreamMessages";

      int nCount;

      messageSent = qSession.createStreamMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageQTestsFullMsg");

      messageSent.writeBytes(bValues2, 0, bValues2.length);
      messageSent.writeBoolean(abool);
      messageSent.writeByte(bValue);
      messageSent.writeBytes(bValues);
      messageSent.writeChar(charValue);
      messageSent.writeDouble(dValue);
      messageSent.writeFloat(fValue);
      messageSent.writeInt(iValue);
      messageSent.writeLong(lValue);
      messageSent.writeObject(sTesting);
      messageSent.writeShort(sValue);
      messageSent.writeString(myString);

      // the next line causes a Message Format exception to be thrown
      // temporarily comment this out.
      messageSent.writeObject(null);
      // set up testcase so onMessage invokes the correct method
      messageSent.setStringProperty("TestCase", "streamMessageQTestsFullMsg");

      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * 
   * Description: Create a StreamMessage Use writeString to write a null, then
   * use readString to read it back.
   */
  private void streamMessageQTestNullCreate() {
    try {
      StreamMessage messageSent = null;
      messageSent = qSession.createStreamMessage();
      JmsUtil.addPropsToMessage(messageSent, p);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageQTestNull");
      // -----------------------------------------------------------------------------
      TestUtil.logTrace("writeString(null) ");
      // -----------------------------------------------------------------------------
      // set up testcase so onMessage invokes the correct method
      messageSent.setStringProperty("TestCase", "streamMessageQTestNull");
      messageSent.writeString(null);
      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      mSender = qSession.createSender(queue);
      mSender.send(messageSent);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: Read ObjectMessage created by messageObjectCopyQTestCreate.
   * verify that modifying the sBuff object after the write does not effect the
   * msg
   */
  public void messageObjectCopyQTest(
      javax.jms.ObjectMessage messageReceivedObjectMsg) {
    boolean pass = true;
    String testCase = "messageObjectCopyQTest";
    String initial = "This is";
    try {
      TestUtil
          .logMsg("Ensure that changing the object did not change the message");
      StringBuffer s = (StringBuffer) messageReceivedObjectMsg.getObject();
      TestUtil.logTrace("s is " + s);
      if (s.toString().equals(initial)) {
        TestUtil.logTrace("Pass: msg was not changed");
      } else {
        TestUtil.logTrace("Fail: msg was changed!");
        pass = false;
      }
      sendTestResults(testCase, pass);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * Description: Read a StreamMessage -. Verify the proper conversion support
   * as in 3.11.3
   */
  private void streamMessageConversionQTestsBoolean(
      javax.jms.StreamMessage messageReceived) {
    String testCase = "streamMessageConversionQTestsBoolean";
    try {

      byte bValue = 127;
      boolean abool = true;
      byte[] bValues = { 0 };
      boolean pass = true;

      // now test conversions for boolean
      // -----------------------------------------------
      // boolean to boolean - valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readBoolean to read a boolean");
      try {
        if (messageReceived.readBoolean() == abool) {
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
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readString()
            .equals((Boolean.valueOf(abool)).toString())) {
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

        // position to beginning of stream message.
        messageReceived.reset();
        nCount = messageReceived.readBytes(bValues);
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
      } catch (javax.jms.MessageFormatException e) {
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
        // position to beginning of stream message.
        messageReceived.reset();
        bValue = messageReceived.readByte();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
        // position to beginning of stream message.
        messageReceived.reset();
        messageReceived.readShort();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;

      } catch (javax.jms.MessageFormatException e) {
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
        // position to beginning of stream message.
        messageReceived.reset();
        messageReceived.readChar();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
        // position to beginning of stream message.
        messageReceived.reset();
        messageReceived.readInt();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
        // position to beginning of stream message.
        messageReceived.reset();
        messageReceived.readLong();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
        // position to beginning of stream message.
        messageReceived.reset();
        messageReceived.readFloat();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;

      } catch (javax.jms.MessageFormatException e) {
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
        // position to beginning of stream message.
        messageReceived.reset();
        messageReceived.readDouble();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;

      } catch (javax.jms.MessageFormatException e) {
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
   * Description: Read a StreamMessage -. Verify the proper conversion support
   * as in 3.11.3
   * 
   */
  private void streamMessageConversionQTestsByte(
      javax.jms.StreamMessage messageReceived) {
    String testCase = "streamMessageConversionQTestsByte";
    try {
      byte bValue = 127;
      boolean abool = true;
      byte[] bValues = { 0 };
      char charValue = 'a';
      short sValue = 1;
      long lValue = 2;
      double dValue = 3;
      float fValue = 5;
      int iValue = 6;
      boolean pass = true;
      // now test conversions for byte
      // -----------------------------------------------
      // byte to boolean - invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readByte to read a boolean - this is not valid");
      try {
        messageReceived.reset();
        boolean b = messageReceived.readBoolean();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readString to read a byte");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readString().equals(Byte.toString(bValue))) {
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
          "Use readBytes[] to read a byte - expect MessageFormatException");
      int nCount = 0;
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        nCount = messageReceived.readBytes(bValues);
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      TestUtil.logTrace("Count returned from readBytes is : " + nCount);

      // -----------------------------------------------
      // byte to byte valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readByte to read a byte");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readByte() == bValue) {
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
      TestUtil.logTrace("Use readShort to read a byte");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readShort() == bValue) {
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
      TestUtil.logTrace("Use readChar to read a boolean - this is not valid");
      try {
        messageReceived.reset();
        char c = messageReceived.readChar();
        pass = false;
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readInt to read a byte");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readInt() == bValue) {
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
      TestUtil.logTrace("Use readLong to read a byte");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readLong() == bValue) {
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
      TestUtil.logTrace("Use readFloat to read a boolean - this is not valid");
      try {
        messageReceived.reset();
        float f = messageReceived.readFloat();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readDouble to read a boolean - this is not valid");
      try {
        messageReceived.reset();
        double d = messageReceived.readDouble();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
   * 
   * Description: Read a StreamMessage -. Verify the proper conversion support
   * as in 3.11.3
   * 
   */
  private void streamMessageConversionQTestsShort(
      javax.jms.StreamMessage messageReceived) {
    String testCase = "streamMessageConversionQTestsShort";
    try {

      byte bValue = 127;
      boolean abool = true;
      byte[] bValues = { 0 };
      char charValue = 'a';
      short sValue = 1;
      long lValue = 2;
      double dValue = 3;
      float fValue = 5;
      int iValue = 6;
      boolean pass = true;

      // now test conversions for byte
      // -----------------------------------------------
      // short to boolean - invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readBoolean to read a short - this is not valid");
      try {
        messageReceived.reset();
        boolean b = messageReceived.readBoolean();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readString to read a short");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readString().equals(Short.toString(sValue))) {
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
          "Use readBytes[] to read a short - expect MessageFormatException");
      int nCount = 0;
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        nCount = messageReceived.readBytes(bValues);
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      TestUtil.logTrace("Count returned from readBytes is : " + nCount);

      // -----------------------------------------------
      // short to byte invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readByte to read a short - this is not valid");
      try {
        messageReceived.reset();
        byte b = messageReceived.readByte();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readShort to read a short");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readShort() == sValue) {
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
      TestUtil.logTrace("Use readChar to read a short - this is not valid");
      try {
        messageReceived.reset();
        char c = messageReceived.readChar();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readInt to read a byte");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readInt() == sValue) {
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
      TestUtil.logTrace("Use readLong to read a short");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readLong() == sValue) {
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
      TestUtil.logTrace("Use readFloat to read a short - this is not valid");
      try {
        messageReceived.reset();
        float f = messageReceived.readFloat();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readDouble to read a short - this is not valid");
      try {
        messageReceived.reset();
        double d = messageReceived.readDouble();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
   * Description: Read a StreamMessage -. Verify the proper conversion support
   * as in 3.11.3
   * 
   */
  private void streamMessageConversionQTestsInt(
      javax.jms.StreamMessage messageReceived) {
    String testCase = "streamMessageConversionQTestsInt";

    try {

      byte bValue = 127;
      boolean abool = true;
      byte[] bValues = { 0 };
      char charValue = 'a';
      short sValue = 1;
      long lValue = 2;
      double dValue = 3;
      float fValue = 5;
      int iValue = 6;
      boolean pass = true;

      // now test conversions for byte
      // -----------------------------------------------
      // int to boolean - invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readBoolean to read an int - this is not valid");
      try {
        messageReceived.reset();
        boolean b = messageReceived.readBoolean();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readString to read an int");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readString().equals(Integer.toString(iValue))) {
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
          "Use readBytes[] to read an int - expect MessageFormatException");
      int nCount = 0;
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        nCount = messageReceived.readBytes(bValues);
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      TestUtil.logTrace("Count returned from readBytes is : " + nCount);

      // -----------------------------------------------
      // int to byte invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readByte to read an int - this is not valid");
      try {
        messageReceived.reset();
        byte b = messageReceived.readByte();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readShort to read an int");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        short s = messageReceived.readShort();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readChar to read an int - this is not valid");
      try {
        messageReceived.reset();
        char c = messageReceived.readChar();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readInt to read an int");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readInt() == iValue) {
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
      TestUtil.logTrace("Use readLong to read an int");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readLong() == iValue) {
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
      TestUtil.logTrace("Use readFloat to read an int - this is not valid");
      try {
        messageReceived.reset();
        float f = messageReceived.readFloat();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readDouble to read an int - this is not valid");
      try {
        messageReceived.reset();
        double d = messageReceived.readDouble();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
   * Description: Read a StreamMessage -. Verify the proper conversion support
   * as in 3.11.3
   */
  private void streamMessageConversionQTestsLong(
      javax.jms.StreamMessage messageReceived) {
    String testCase = "streamMessageConversionQTestsLong";

    try {

      byte bValue = 127;
      boolean abool = true;
      byte[] bValues = { 0 };
      char charValue = 'a';
      short sValue = 1;
      long lValue = 2;
      double dValue = 3;
      float fValue = 5;
      int iValue = 6;
      boolean pass = true;

      // now test conversions for byte
      // -----------------------------------------------
      // long to boolean - invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readBoolean to read a long - this is not valid");
      try {
        messageReceived.reset();
        boolean b = messageReceived.readBoolean();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readString to read a long");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readString().equals(Long.toString(lValue))) {
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
          "Use readBytes[] to read  a long - expect MessageFormatException");
      int nCount = 0;
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        nCount = messageReceived.readBytes(bValues);
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      TestUtil.logTrace("Count returned from readBytes is : " + nCount);

      // -----------------------------------------------
      // long to byte invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readByte to read an long - this is not valid");
      try {
        messageReceived.reset();
        byte b = messageReceived.readByte();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readShort to read a long");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        short s = messageReceived.readShort();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readChar to read a long - this is not valid");
      try {
        messageReceived.reset();
        char c = messageReceived.readChar();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readInt to read a long");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        int i = messageReceived.readInt();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readLong to read a long");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readLong() == lValue) {
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
      TestUtil.logTrace("Use readFloat to read a long - this is not valid");
      try {
        messageReceived.reset();
        float f = messageReceived.readFloat();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readDouble to read an long - this is not valid");
      try {
        messageReceived.reset();
        double d = messageReceived.readDouble();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
   * Description: Read a StreamMessage -. Verify the proper conversion support
   * as in 3.11.3
   */
  private void streamMessageConversionQTestsFloat(
      javax.jms.StreamMessage messageReceived) {
    String testCase = "streamMessageConversionQTestsFloat";

    try {

      byte bValue = 127;
      boolean abool = true;
      byte[] bValues = { 0 };
      char charValue = 'a';
      short sValue = 1;
      long lValue = 2;
      double dValue = 3;
      float fValue = 5;
      int iValue = 6;
      boolean pass = true;

      // now test conversions for byte
      // -----------------------------------------------
      // float to boolean - invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readBoolean to read a float  ");
      try {
        messageReceived.reset();
        boolean b = messageReceived.readBoolean();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readString to read a float");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readString().equals(Float.toString(fValue))) {
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
      TestUtil.logTrace("Use readBytes[] to read  a float ");
      int nCount = 0;
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        nCount = messageReceived.readBytes(bValues);
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      TestUtil.logTrace("Count returned from readBytes is : " + nCount);

      // -----------------------------------------------
      // float to byte invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readByte to read a float  ");
      try {
        messageReceived.reset();
        byte b = messageReceived.readByte();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readShort to read a float");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        short s = messageReceived.readShort();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readChar to read a long  ");
      try {
        messageReceived.reset();
        char c = messageReceived.readChar();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readInt to read a float");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        int i = messageReceived.readInt();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readLong to read a long");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        long l = messageReceived.readLong();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readFloat to read a float  ");
      try {
        messageReceived.reset();
        if (messageReceived.readFloat() == fValue) {
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
      // float to double invalid
      // -----------------------------------------------

      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readDouble to read an float  ");
      try {
        messageReceived.reset();
        if (messageReceived.readDouble() == fValue) {
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
   * Description: Read a StreamMessage -. Verify the proper conversion support
   * as in 3.11.3
   * 
   */
  private void streamMessageConversionQTestsDouble(
      javax.jms.StreamMessage messageReceived) {
    String testCase = "streamMessageConversionQTestsDouble";

    try {

      byte bValue = 127;
      boolean abool = true;
      byte[] bValues = { 0 };
      char charValue = 'a';
      short sValue = 1;
      long lValue = 2;
      double dValue = 3;
      float fValue = 5;
      int iValue = 6;
      boolean pass = true;

      // now test conversions for byte
      // -----------------------------------------------
      // double to boolean - invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readBoolean to read a double  ");
      try {
        messageReceived.reset();
        boolean b = messageReceived.readBoolean();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readString to read a double");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readString().equals(Double.toString(dValue))) {
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
      TestUtil.logTrace("Use readBytes[] to read  a double ");
      int nCount = 0;
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        nCount = messageReceived.readBytes(bValues);
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      TestUtil.logTrace("Count returned from readBytes is : " + nCount);

      // -----------------------------------------------
      // double to byte invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readByte to read a double  ");
      try {
        messageReceived.reset();
        byte b = messageReceived.readByte();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readShort to read a double");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        short s = messageReceived.readShort();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readChar to read a double  ");
      try {
        messageReceived.reset();
        char c = messageReceived.readChar();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readInt to read a double");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        int i = messageReceived.readInt();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readLong to read a double");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        long l = messageReceived.readLong();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readFloat to read a double  ");
      try {
        messageReceived.reset();
        float f = messageReceived.readFloat();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readDouble to read a double  ");
      try {
        messageReceived.reset();
        if (messageReceived.readDouble() == dValue) {
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
   * Description: Read a StreamMessage -. Verify the proper conversion support
   * as in 3.11.3
   */
  private void streamMessageConversionQTestsString(
      javax.jms.StreamMessage messageReceived) {
    String testCase = "streamMessageConversionQTestsString";

    try {

      byte bValue = 127;
      boolean abool = true;
      byte[] bValues = { 0 };
      char charValue = 'a';
      short sValue = 1;
      long lValue = 2;
      double dValue = 3;
      float fValue = 5;
      int iValue = 6;
      boolean pass = true;
      String myString = "10";

      // now test conversions for String
      // -----------------------------------------------
      // string to string valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readString to read a String");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readString().equals(myString)) {
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
      TestUtil.logTrace("Use readBytes[] to read a String");
      int nCount = 0;
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        nCount = messageReceived.readBytes(bValues);
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      TestUtil.logTrace("Count returned from readBytes is : " + nCount);

      // -----------------------------------------------
      // String to byte valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readByte to read a String");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readByte() == Byte.parseByte(myString)) {
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
      TestUtil.logTrace("Use readShort to read a string");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readShort() == Short.parseShort(myString)) {
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
      TestUtil.logTrace("Use readChar to read a String ");
      try {
        messageReceived.reset();
        char c = messageReceived.readChar();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readInt to read a String");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readInt() == Integer.parseInt(myString)) {
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
      TestUtil.logTrace("Use readLong to read a String");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readLong() == Long.parseLong(myString)) {
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
      TestUtil.logTrace("Use readFloat to read a String");
      try {
        messageReceived.reset();
        if (messageReceived.readFloat() == Float.parseFloat(myString)) {
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
      TestUtil.logTrace("Use readDouble to read a String");
      try {
        messageReceived.reset();
        if (messageReceived.readDouble() == Double.parseDouble(myString)) {
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
      TestUtil.logTrace("Use readBoolean to read a string ");
      try {
        messageReceived.clearBody();
        messageReceived.writeString("true");
        messageReceived.reset();
        if (messageReceived.readBoolean() == abool) {
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
      TestUtil.logTrace("Use readBoolean to read a string  that is !true ");
      try {
        messageReceived.reset();
        boolean b = messageReceived.readBoolean();
        if (b != true) {
          TestUtil.logTrace("Fail: !true should return false");
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
   * Description: Read a StreamMessage -. Verify the proper conversion support
   * as in 3.11.3
   * 
   */
  private void streamMessageConversionQTestsChar(
      javax.jms.StreamMessage messageReceived) {
    String testCase = "streamMessageConversionQTestsChar";

    try {

      byte bValue = 127;
      boolean abool = true;
      byte[] bValues = { 0 };
      char charValue = 'a';
      short sValue = 1;
      long lValue = 2;
      double dValue = 3;
      float fValue = 5;
      int iValue = 6;
      boolean pass = true;

      // now test conversions for byte
      // -----------------------------------------------
      // char to boolean - invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readBoolean to read a char - this is not valid");
      try {
        messageReceived.reset();
        boolean b = messageReceived.readBoolean();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readString to read a char");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        String s = messageReceived.readString();
        TestUtil.logTrace("char returned for \"a\" is : " + s);
        if (s.equals("a")) {
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
          "Use readBytes[] to read a char - expect MessageFormatException");
      int nCount = 0;
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        nCount = messageReceived.readBytes(bValues);
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      TestUtil.logTrace("Count returned from readBytes is : " + nCount);

      // -----------------------------------------------
      // char to byte invalid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readByte to read a char - this is not valid");
      try {
        messageReceived.reset();
        byte b = messageReceived.readByte();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readShort to read a char");
      try {
        messageReceived.reset();
        short s = messageReceived.readShort();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readChar to read a char ");
      try {
        messageReceived.reset();
        if (messageReceived.readChar() == 'a') {
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
      TestUtil.logTrace("Use readInt to read a char ");
      try {
        messageReceived.reset();
        int i = messageReceived.readInt();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readLong to read a char");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        long l = messageReceived.readLong();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readFloat to read a char - this is not valid");
      try {
        messageReceived.reset();
        float f = messageReceived.readFloat();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readDouble to read a char - this is not valid");
      try {
        messageReceived.reset();
        double d = messageReceived.readDouble();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
   * Description: Read a StreamMessage -. Verify the proper conversion support
   * as in 3.11.3
   */
  private void streamMessageConversionQTestsBytes(
      javax.jms.StreamMessage messageReceived) {
    String testCase = "streamMessageConversionQTestsBytes";

    try {

      byte bValue = 127;
      boolean abool = true;
      byte[] bValues2 = { 0, 0, 0 };
      short sValue = 1;
      long lValue = 2;
      double dValue = 3;
      float fValue = 5;
      int iValue = 6;
      boolean pass = true;

      // now test conversions for boolean
      // -----------------------------------------------
      // byte[] to byte[] - valid
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readBytes[] to read a byte[] ");
      int nCount = 0;
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readBytes(bValues2) == 3) { // count should be 3.
          TestUtil.logTrace("Pass: byte[] to byte[] - valid");
        } else {
          TestUtil.logTrace("Fail: count incorrect");
          pass = false;
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
      TestUtil.logTrace("Use readBoolean to read a byte[]");
      // position to beginning of stream message.
      messageReceived.reset();
      try {
        boolean b = messageReceived.readBoolean();
        TestUtil.logTrace(
            "Fail: byte[] to boolean conversion should have thrown MessageFormatException");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
      TestUtil.logTrace("Use readString to read a byte[]");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        String s = messageReceived.readString();
        TestUtil.logTrace(
            "Fail: byte[] to boolean conversion should have thrown MessageFormatException");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
          "Use readByte to read a byte[] - expect MessageFormatException");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        byte b = messageReceived.readByte();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
          "Use readShort to read a byte[] - expect MessageFormatException");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        short s = messageReceived.readShort();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
          "Use readChar to read a byte[] - expect MessageFormatException");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        char c = messageReceived.readChar();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
          "Use readInt to read a byte[] - expect MessageFormatException");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        int i = messageReceived.readInt();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
          "Use readLong to read a byte[] - expect MessageFormatException");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        long l = messageReceived.readLong();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
          "Use readFloat to read a byte[] - expect MessageFormatException");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        float f = messageReceived.readFloat();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
          "Use readDouble to read a byte[] - expect MessageFormatException");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        double d = messageReceived.readDouble();
        TestUtil.logTrace("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
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
   * 
   * Description: Read a StreamMessage -. Verify NumberFormatException is thrown
   * Verify that the pointer was not incremented by doing a read string
   * 
   */
  private void streamMessageConversionQTestsInvFormatString(
      javax.jms.StreamMessage messageReceived) {
    String testCase = "streamMessageConversionQTestsInvFormatString";

    try {

      boolean pass = true;
      String myString = "mytest string";

      // -----------------------------------------------
      // String to byte
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readByte to read a String that is not valid ");
      try {
        byte b = messageReceived.readByte();
        TestUtil.logTrace("Fail: java.lang.NumberFormatException expected");
        pass = false;
      } catch (java.lang.NumberFormatException e) {
        TestUtil.logTrace(
            "Pass: java.lang.NumberFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: Unexpected exception "
            + e.getClass().getName() + " was thrown");
        pass = false;
      }
      // -----------------------------------------------
      // pointer should not have moved
      // -----------------------------------------------

      TestUtil.logTrace("--");
      TestUtil.logTrace(
          "Verify that the data can be read as a string and pointer did not move");
      try {
        String s = messageReceived.readString();
        TestUtil.logTrace("message read: " + s);
        if (s.equals(myString)) {
          TestUtil.logTrace("Pass: able to read the string");
        } else {
          TestUtil.logTrace("Fail: string not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      // -----------------------------------------------
      // string to short
      // -----------------------------------------------
      TestUtil.logTrace("--");
      TestUtil.logTrace("Use readShort to read a string that is not valid ");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        short s = messageReceived.readShort();
        TestUtil.logTrace("Fail: NumberFormatException was expected");
        pass = false;
      } catch (java.lang.NumberFormatException e) {
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
      TestUtil.logTrace("Use readInt to read a String that is not valid ");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        int i = messageReceived.readInt();
        TestUtil.logTrace("Fail: NumberFormatException was expected");
      } catch (java.lang.NumberFormatException e) {
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
      TestUtil.logTrace("Use readLong to read a String that is not valid ");
      try {
        // position to beginning of stream message.
        messageReceived.reset();
        long l = messageReceived.readLong();
        TestUtil.logTrace("Fail: NumberFormatException was expected");
        pass = false;
      } catch (java.lang.NumberFormatException e) {
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
      TestUtil.logTrace("Use readFloat to read a String that is not valid ");
      try {
        messageReceived.reset();
        float f = messageReceived.readFloat();
        TestUtil.logTrace("Fail: NumberFormatException was expected");
        pass = false;
      } catch (java.lang.NumberFormatException e) {
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
      TestUtil.logTrace("Use readDouble to read a String that is not valid ");
      try {
        messageReceived.reset();
        double d = messageReceived.readDouble();
        TestUtil.logTrace("Fail: NumberFormatException was expected");
        pass = false;
      } catch (java.lang.NumberFormatException e) {
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
   * Description: Read a StreamMessage -. Verify the data received was as sent.
   * 
   */
  private void streamMessageQTestsFullMsg(
      javax.jms.StreamMessage messageReceived) {
    String testCase = "streamMessageQTestsFullMsg";

    try {
      byte bValue = 127;
      boolean abool = false;
      byte[] bValues2 = { 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
      byte[] bValuesReturned = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
      byte[] bValuesReturned2 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
      char charValue = 'Z';
      short sValue = 32767;
      long lValue = 9223372036854775807L;
      double dValue = 6.02e23;
      float fValue = 6.02e23f;
      int iValue = 6;
      boolean pass = true;
      String myString = "text";
      String sTesting = "Testing StreamMessages";

      try {
        int nCount = bValuesReturned2.length;
        do {
          nCount = messageReceived.readBytes(bValuesReturned2);
          TestUtil.logTrace("nCount is " + nCount);
          if (nCount != -1) {
            for (int i = 0; i < bValuesReturned2.length; i++) {
              if (bValuesReturned2[i] != bValues2[i]) {
                TestUtil.logTrace("Fail: byte[] " + i + " is not valid");
                pass = false;
              } else {
                TestUtil.logTrace("PASS: byte[]" + i + " is valid");
              }
            }
          }
        } while (nCount >= bValuesReturned2.length);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      try {
        if (messageReceived.readBoolean() == abool) {
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
        if (messageReceived.readByte() == bValue) {
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
        int nCount = bValuesReturned.length;
        do {
          nCount = messageReceived.readBytes(bValuesReturned);
          TestUtil.logTrace("nCount is " + nCount);
          if (nCount != -1) {
            for (int i = 0; i < bValuesReturned2.length; i++) {
              if (bValuesReturned2[i] != bValues2[i]) {
                TestUtil.logTrace("Fail: byte[] " + i + " is not valid");
                pass = false;
              } else {
                TestUtil.logTrace("PASS: byte[]" + i + " is valid");
              }
            }
          }
        } while (nCount >= bValuesReturned.length);

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
        if (messageReceived.readDouble() == dValue) {
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
        if (messageReceived.readFloat() == fValue) {
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
        if (messageReceived.readInt() == iValue) {
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
        if (messageReceived.readLong() == lValue) {
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
        if (messageReceived.readObject().equals(sTesting)) {
          TestUtil.logTrace("Pass: correct object");
        } else {
          TestUtil.logTrace("Fail: object not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      try {
        if (messageReceived.readShort() == sValue) {
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
        if (messageReceived.readString().equals(myString)) {
          TestUtil.logTrace("Pass: correct string");
        } else {
          TestUtil.logTrace("Fail: string not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }

      try {
        if (messageReceived.readObject() == null) {
          TestUtil.logTrace("Pass: correct object");
        } else {
          TestUtil.logTrace("Fail: object not returned as expected");
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
   * Description: Read a StreamMessage Use readString to read back a null
   */
  private void streamMessageQTestNull(javax.jms.StreamMessage messageReceived) {
    String testCase = "streamMessageQTestNull";
    try {
      boolean pass = true;
      try {
        if (messageReceived.readObject() == null) {
          TestUtil.logTrace("Pass: Read a null");
        } else {
          TestUtil.logTrace("Fail: null value not returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Error trying to read a null object");
        TestUtil.logTrace("Error: unexpected exception "
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
        "jms.ee.mdb.mdb_msgTypesQ2  In MsgBeanMsgTestQ2::setMessageDrivenContext()!!");
    this.mdc = mdc;
  }

  public void ejbRemove() {
    TestUtil
        .logTrace("jms.ee.mdb.mdb_msgTypesQ2  In MsgBeanMsgTestQ2::remove()!!");
  }

}
