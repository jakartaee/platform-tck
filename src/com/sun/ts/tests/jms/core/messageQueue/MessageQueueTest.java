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
package com.sun.ts.tests.jms.core.messageQueue;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.*;
import com.sun.javatest.Status;

public class MessageQueueTest extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.messageQueue.MessageQueueTest";

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
    MessageQueueTest theTests = new MessageQueueTest();
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
        throw new Exception("'numProducers' in ts.jte must not be null");
      }
      if (mode == null) {
        throw new Exception("'platform.mode' in ts.jte must not be null");
      }
      queues = new ArrayList(2);
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
   * @testName: msgClearBodyQueueTest
   * 
   * @assertion_ids: JMS:SPEC:71; JMS:SPEC:72; JMS:JAVADOC:431; JMS:JAVADOC:473;
   * JMS:JAVADOC:449; JMS:SPEC:178; JMS:JAVADOC:291; JMS:JAVADOC:680;
   * JMS:JAVADOC:744;
   * 
   * @test_Strategy: For each type of message, create and send a message Send
   * and receive single Text, map, bytes, stream, and object message call
   * clearBody, verify body is empty after clearBody. verify properties are not
   * effected by clearBody. Write to the message again 3.11
   */

  public void msgClearBodyQueueTest() throws Fault {
    boolean pass = true;
    byte bValue = 127;
    byte bValue2 = 22;
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

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();

      // send and receive Object message to Queue
      logTrace("Send ObjectMessage to Queue.");
      messageSentObjectMsg = tool.getDefaultQueueSession()
          .createObjectMessage();
      messageSentObjectMsg.setObject("Initial message");
      messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgClearBodyQueueTest");
      tool.getDefaultQueueSender().send(messageSentObjectMsg);
      messageReceivedObjectMsg = (ObjectMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      try {
        logTrace("Testing Object message");
        logTrace("read 1st contents");
        logTrace("  " + messageReceivedObjectMsg.getObject());
        logTrace("Call to clearBody !!!!!!!!!!!!!!!");
        messageReceivedObjectMsg.clearBody();

        // message body should now be empty
        if (messageReceivedObjectMsg.getObject() == null) {
          logTrace("Empty body after clearBody as expected: null");
        } else {
          logTrace("Fail: message body was not empty");
          pass = false;
        }

        // properties should not have been deleted by the clearBody method.
        if (messageSentObjectMsg.getStringProperty("COM_SUN_JMS_TESTNAME")
            .equals("msgClearBodyQueueTest")) {
          logTrace("Pass: Object properties read ok after clearBody called");
        } else {
          logMsg("Fail: Object properties cleared after clearBody called");
          pass = false;
        }
        logTrace("write 2nd contents");
        messageReceivedObjectMsg.setObject("new stuff here!!!!!!");
        logTrace("read 2nd contents");
        if (messageReceivedObjectMsg.getObject()
            .equals("new stuff here!!!!!!")) {
          logTrace("Pass:");
        } else {
          logMsg("Fail: ");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: " + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // send and receive map message to Queue
      logTrace("Send MapMessage to Queue.");
      messageSentMapMessage = tool.getDefaultQueueSession().createMapMessage();
      messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgClearBodyQueueTest");
      messageSentMapMessage.setString("aString", "Initial message");
      tool.getDefaultQueueSender().send(messageSentMapMessage);
      messageReceivedMapMessage = (MapMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      try {
        logTrace("Test for MapMessage ");
        logTrace("read 1st contents");
        logTrace("  " + messageReceivedMapMessage.getString("aString"));
        logTrace("Call to clearBody !!!!!!!!!!!!!!!");
        messageReceivedMapMessage.clearBody();

        // message body should now be empty
        if (messageReceivedMapMessage.getString("aString") == null) {
          logTrace("Empty body after clearBody as expected: null");
        } else {
          logTrace("Fail: message body was not empty");
          pass = false;
        }

        // properties should not have been deleted by the clearBody method.
        if (messageReceivedMapMessage.getStringProperty("COM_SUN_JMS_TESTNAME")
            .equals("msgClearBodyQueueTest")) {
          logTrace("Pass: Map properties read ok after clearBody called");
        } else {
          logMsg("Fail: Map properties cleared after clearBody called");
          pass = false;
        }
        logTrace("write 2nd contents");
        messageReceivedMapMessage.setString("yes", "new stuff !!!!!");
        logTrace("read 2nd contents");
        if (messageReceivedMapMessage.getString("yes")
            .equals("new stuff !!!!!")) {
          logTrace("PASS:");
        } else {
          logMsg("FAIL:");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: " + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // send and receive bytes message to Queue
      logTrace("Send BytesMessage to Queue.");
      messageSentBytesMessage = tool.getDefaultQueueSession()
          .createBytesMessage();
      messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgClearBodyQueueTest");
      messageSentBytesMessage.writeByte(bValue);
      tool.getDefaultQueueSender().send(messageSentBytesMessage);
      messageReceivedBytesMessage = (BytesMessage) tool
          .getDefaultQueueReceiver().receive(timeout);
      try {
        logTrace("Test BytesMessage ");
        logTrace("read 1st contents");
        logTrace("  " + messageReceivedBytesMessage.readByte());
        logTrace("Call to clearBody !!!!!!!!!!!!!!!");
        messageReceivedBytesMessage.clearBody();
        logTrace(
            "Bytes message body should now be empty and in writeonly mode");
        try {
          byte b = messageReceivedBytesMessage.readByte();

          logTrace("Fail: MessageNotReadableException not thrown as expected");
          pass = false;
        } catch (Exception e) {
          if (e instanceof javax.jms.MessageNotReadableException) {
            logTrace("Pass: MessageEOFException thrown as expected");
          } else {
            logMsg("Error: Unexpected exception " + e.getClass().getName()
                + " was thrown");
            pass = false;
          }
        }

        // properties should not have been deleted by the clearBody method.
        if (messageReceivedBytesMessage
            .getStringProperty("COM_SUN_JMS_TESTNAME")
            .equals("msgClearBodyQueueTest")) {
          logTrace("Pass: Bytes msg properties read ok after clearBody called");
        } else {
          logMsg("Fail: Bytes msg properties cleared after clearBody called");
          pass = false;
        }
        logTrace("write 2nd contents");
        messageReceivedBytesMessage.writeByte(bValue2);
        logTrace("read 2nd contents");
        messageReceivedBytesMessage.reset();
        if (messageReceivedBytesMessage.readByte() == bValue2) {
          logTrace("Pass:");
        } else {
          logMsg("Fail:");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: " + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // Send and receive a StreamMessage
      logTrace("sending a Stream message");
      messageSentStreamMessage = tool.getDefaultQueueSession()
          .createStreamMessage();
      messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgClearBodyQueueTest");
      messageSentStreamMessage.writeString("Testing...");
      logTrace("Sending message");
      tool.getDefaultQueueSender().send(messageSentStreamMessage);
      messageReceivedStreamMessage = (StreamMessage) tool
          .getDefaultQueueReceiver().receive(timeout);
      try {
        logTrace("Test StreamMessage ");
        logTrace("read 1st contents");
        logTrace("  " + messageReceivedStreamMessage.readString());
        logTrace("Call to clearBody !!!!!!!!!!!!!!!");
        messageReceivedStreamMessage.clearBody();
        logTrace(
            "Stream message body should now be empty and in writeonly mode");
        try {
          String s = messageReceivedStreamMessage.readString();

          logMsg("Fail: MessageNotReadableException should have been thrown");
          pass = false;
        } catch (Exception e) {
          if (e instanceof javax.jms.MessageNotReadableException) {
            logTrace("Pass: MessageNotReadableException thrown as expected");
          } else {
            logMsg("Error: Unexpected exception " + e.getClass().getName()
                + " was thrown");
            pass = false;
          }
        }

        // properties should not have been deleted by the clearBody method.
        if (messageReceivedStreamMessage
            .getStringProperty("COM_SUN_JMS_TESTNAME")
            .equals("msgClearBodyQueueTest")) {
          logTrace(
              "Pass: Stream msg properties read ok after clearBody called");
        } else {
          logMsg("Fail: Stream msg properties cleared after clearBody called");
          pass = false;
        }
        logTrace("write 2nd contents");
        messageReceivedStreamMessage.writeString("new data");
        logTrace("read 2nd contents");
        messageReceivedStreamMessage.reset();
        if (messageReceivedStreamMessage.readString().equals("new data")) {
          logTrace("Pass:");
        } else {
          logMsg("Fail:");
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: " + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // Text Message
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText("sending a Text message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgClearBodyQueueTest");
      logTrace("sending a Text message");
      tool.getDefaultQueueSender().send(messageSent);
      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      try {
        logTrace("Test TextMessage ");
        logTrace("read 1st contents");
        logTrace("  " + messageReceived.getText());
        logTrace("Call to clearBody !!!!!!!!!!!!!!!");
        messageReceived.clearBody();

        // message body should now be empty
        if (messageReceived.getText() == null) {
          logTrace("Empty body after clearBody as expected: null");
        } else {
          logTrace("Fail: message body was not empty");
          pass = false;
        }

        // properties should not have been deleted by the clearBody method.
        if (messageReceived.getStringProperty("COM_SUN_JMS_TESTNAME")
            .equals("msgClearBodyQueueTest")) {
          logTrace("Pass: Text properties read ok after clearBody called");
        } else {
          logMsg("Fail: Text properties cleared after clearBody called");
          pass = false;
        }
        logTrace("write and read 2nd contents");
        messageReceived.setText("new data");
        if (messageReceived.getText().equals("new data")) {
          logTrace("Pass:");
        } else {
          logMsg("Fail:");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: " + e.getClass().getName() + " was thrown");
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: clearBody test failure");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("msgClearBodyQueueTest");
    }
  }

  /*
   * @testName: msgResetQueueTest
   * 
   * @assertion_ids: JMS:JAVADOC:174; JMS:JAVADOC:584; JMS:JAVADOC:760;
   * JMS:JAVADOC:705;
   * 
   * @test_Strategy: create a stream message and a byte message. write to the
   * message body, call the reset method, try to write to the body expect a
   * MessageNotWriteableException to be thrown.
   */

  public void msgResetQueueTest() throws Fault {
    boolean pass = true;
    int nInt = 1000;

    try {
      StreamMessage messageSentStreamMessage = null;
      BytesMessage messageSentBytesMessage = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();

      // StreamMessage
      try {
        logTrace("creating a Stream message");
        messageSentStreamMessage = tool.getDefaultQueueSession()
            .createStreamMessage();
        messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
            "msgResetQueueTest1");

        // write to the message
        messageSentStreamMessage.writeString("Testing...");
        logMsg("reset stream message -  now  should be in readonly mode");
        messageSentStreamMessage.reset();
        messageSentStreamMessage.writeString("new data");
        logMsg(
            "Fail: message did not throw MessageNotWriteable exception as expected");
        pass = false;
      } catch (MessageNotWriteableException nw) {
        logTrace("Pass: MessageNotWriteable thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: " + e.getClass().getName() + " was thrown");
        pass = false;
      }

      // BytesMessage
      try {
        logTrace("creating a Byte message");
        messageSentBytesMessage = tool.getDefaultQueueSession()
            .createBytesMessage();
        messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
            "msgResetQueueTest2");

        // write to the message
        messageSentBytesMessage.writeInt(nInt);
        logMsg("reset Byte message -  now  should be in readonly mode");
        messageSentBytesMessage.reset();
        messageSentBytesMessage.writeInt(nInt);
        logMsg(
            "Fail: message did not throw MessageNotWriteable exception as expected");
        pass = false;
      } catch (MessageNotWriteableException nw) {
        logTrace("Pass: MessageNotWriteable thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: " + e.getClass().getName() + " was thrown");
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: msgResetQueueTest test failure");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("msgResetQueueTest");
    }
  }

  /*
   * @testName: readNullCharNotValidQueueTest
   * 
   * @assertion_ids: JMS:SPEC:79; JMS:JAVADOC:134; JMS:JAVADOC:439;
   * 
   * @test_Strategy: Write a null string to a MapMessage and then a
   * StreamMessage. Attempt to read the null value as a char.
   * 
   */

  public void readNullCharNotValidQueueTest() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
      MapMessage mapSent = null;
      MapMessage mapReceived = null;
      char c;
      boolean pass = true;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultQueueSession().createStreamMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "readNullCharNotValidQueueTest");

      // -----------------------------------------------------------------------------
      // Stream Message
      // -----------------------------------------------------------------------------
      logTrace(
          "Write a null string to the stream message object with StreamMessage.writeString");
      messageSent.writeString(null);
      logTrace(" Send the message");
      tool.getDefaultQueueSender().send(messageSent);
      messageReceived = (StreamMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("Use readChar to read a null  ");
      try {
        messageReceived.readChar();
        logTrace("Fail: NullPointerException was not thrown");
        pass = false;
      } catch (java.lang.NullPointerException e) {
        logTrace("Pass: NullPointerException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: Unexpected exception " + e.getClass().getName()
            + " was thrown");
        pass = false;
      }

      // -----------------------------------------------------------------------------
      // Map Message
      // -----------------------------------------------------------------------------
      mapSent = tool.getDefaultQueueSession().createMapMessage();
      mapSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "readNullCharNotValidQueueTest");
      logTrace(
          "Write a null string to the map message object with mapMessage.setString");
      mapSent.setString("WriteANull", null);
      logTrace(" Send the message");
      tool.getDefaultQueueSender().send(mapSent);
      mapReceived = (MapMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logTrace("Use readChar to read a null  ");
      try {
        mapReceived.getChar("WriteANull");
        logMsg("Fail: NullPointerException was not thrown");
        pass = false;
      } catch (java.lang.NullPointerException e) {
        logTrace("Pass: NullPointerException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: Unexpected exception " + e.getClass().getName()
            + " was thrown");
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("readNullCharNotValidQueueTest", e);
    }
  }

  /*
   * @testName: messageQIllegalarg
   *
   * @assertion_ids: JMS:JAVADOC:775; JMS:JAVADOC:777; JMS:JAVADOC:779;
   * JMS:JAVADOC:781; JMS:JAVADOC:783; JMS:JAVADOC:785; JMS:JAVADOC:787;
   * JMS:JAVADOC:789; JMS:JAVADOC:791;
   * 
   * @test_Strategy: Create a TextMessage. Write to the message using each type
   * of setProperty method and as an object with null String as name. Verify
   * that IllegalArgumentException thrown.
   */

  public void messageQIllegalarg() throws Fault {
    try {
      TextMessage messageSent = null;
      boolean pass = true;
      byte bValue = 127;
      short sValue = 32767;
      char cValue = '\uFFFF';
      int iValue = 2147483647;
      long lValue = 9223372036854775807L;
      float fValue = 0.0f;
      double dValue = -0.0;
      String ssValue = "abc";

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "messageQIllegalarg");

      // -----------------------------------------------------------------------------

      logMsg("Writing a boolean property ... ");
      try {
        messageSent.setBooleanProperty("", pass);
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setBooleanProperty");
      } catch (IllegalArgumentException e) {
        logMsg("Got Expected IllegalArgumentException with setBooleanProperty");
      }

      logMsg("Writing a byte Property ... ");
      try {
        messageSent.setByteProperty("", bValue);
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setByteProperty");
      } catch (IllegalArgumentException e) {
        logMsg("Got Expected IllegalArgumentException with setByteProperty");
      }

      logMsg("Writing a short Property... ");
      try {
        messageSent.setShortProperty("", sValue);
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setShortProperty");
      } catch (IllegalArgumentException e) {
        logMsg("Got Expected IllegalArgumentException with setShortProperty");
      }

      logMsg("Writing a int Property ... ");
      try {
        messageSent.setIntProperty("", iValue);
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setIntProperty");
      } catch (IllegalArgumentException e) {
        logMsg("Got Expected IllegalArgumentException with setIntProperty");
      }

      logMsg("Writing a long Property ... ");
      try {
        messageSent.setLongProperty("", lValue);
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setLongProperty");
      } catch (IllegalArgumentException e) {
        logMsg("Got Expected IllegalArgumentException with setLongProperty");
      }

      logMsg("Writing a float Property ... ");
      try {
        messageSent.setFloatProperty("", fValue);
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setFloatProperty");
      } catch (IllegalArgumentException e) {
        logMsg("Got Expected IllegalArgumentException with setFloatProperty");
      }

      logMsg("Writing a double Property ... ");
      try {
        messageSent.setDoubleProperty("", dValue);
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setDoubleProperty");
      } catch (IllegalArgumentException e) {
        logMsg("Got Expected IllegalArgumentException with setDoubleProperty");
      }

      logMsg("Writing a string Property ... ");
      try {
        messageSent.setStringProperty("", ssValue);
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setStringProperty");
      } catch (IllegalArgumentException e) {
        logMsg("Got Expected IllegalArgumentException with setStringProperty");
      }

      logMsg("Writing a object Property ... ");
      try {
        messageSent.setObjectProperty("", new Integer(iValue));
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setObjectProperty");
      } catch (IllegalArgumentException e) {
        logMsg("Got Expected IllegalArgumentException with setObjectProperty");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("messageQIllegalarg", e);
    }
  }

}
