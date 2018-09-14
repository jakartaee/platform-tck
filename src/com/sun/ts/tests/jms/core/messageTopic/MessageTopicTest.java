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
package com.sun.ts.tests.jms.core.messageTopic;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.*;
import com.sun.javatest.Status;

public class MessageTopicTest extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.messageTopic.MessageTopicTest";

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
    MessageTopicTest theTests = new MessageTopicTest();
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
      logErr("An error occurred while cleaning");
      throw new Fault("Cleanup failed!", e);
    }
  }

  /* Tests */

  /*
   * @testName: msgClearBodyTopicTest
   * 
   * @assertion_ids: JMS:SPEC:71; JMS:SPEC:72; JMS:JAVADOC:431; JMS:JAVADOC:473;
   * JMS:JAVADOC:449; JMS:SPEC:178; JMS:JAVADOC:291;
   * 
   * @test_Strategy: For each type of message, create and send a message Send
   * and receive single Text, map, bytes, stream, and object message call
   * clearBody, verify body is empty after clearBody. verify properties are not
   * effected by clearBody. Write to the message again 3.11
   */

  public void msgClearBodyTopicTest() throws Fault {
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

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();

      // send and receive Object message to Topic
      logTrace("Send ObjectMessage to Topic.");
      messageSentObjectMsg = tool.getDefaultTopicSession()
          .createObjectMessage();
      messageSentObjectMsg.setObject("Initial message");
      messageSentObjectMsg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgClearBodyTopicTest");
      tool.getDefaultTopicPublisher().publish(messageSentObjectMsg);
      messageReceivedObjectMsg = (ObjectMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
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
            .equals("msgClearBodyTopicTest")) {
          logTrace("Pass: Object properties read ok after clearBody called");
        } else {
          logErr("Fail: Object properties cleared after clearBody called");
          pass = false;
        }
        logTrace("write 2nd contents");
        messageReceivedObjectMsg.setObject("new stuff here!!!!!!");
        logTrace("read 2nd contents");
        if (messageReceivedObjectMsg.getObject()
            .equals("new stuff here!!!!!!")) {
          logTrace("Pass:");
        } else {
          logErr("Fail: ");
          pass = false;
        }
      } catch (Exception e) {
        logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // send and receive map message to Topic
      logTrace("Send MapMessage to Topic.");
      messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
      messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgClearBodyTopicTest");
      messageSentMapMessage.setString("aString", "Initial message");
      tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
      messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber()
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
            .equals("msgClearBodyTopicTest")) {
          logTrace("Pass: Map properties read ok after clearBody called");
        } else {
          logErr("Fail: Map properties cleared after clearBody called");
          pass = false;
        }
        logTrace("write 2nd contents");
        messageReceivedMapMessage.setString("yes", "new stuff !!!!!");
        logTrace("read 2nd contents");
        if (messageReceivedMapMessage.getString("yes")
            .equals("new stuff !!!!!")) {
          logTrace("PASS:");
        } else {
          logErr("FAIL:");
          pass = false;
        }
      } catch (Exception e) {
        logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // send and receive bytes message to Topic
      logTrace("Send BytesMessage to Topic.");
      messageSentBytesMessage = tool.getDefaultTopicSession()
          .createBytesMessage();
      messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgClearBodyTopicTest");
      messageSentBytesMessage.writeByte(bValue);
      tool.getDefaultTopicPublisher().publish(messageSentBytesMessage);
      messageReceivedBytesMessage = (BytesMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
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

          logErr("Fail: MessageNotReadableException not thrown as expected");
          pass = false;
        } catch (javax.jms.MessageNotReadableException e) {
          logTrace("Pass: MessageNotReadableException thrown as expected");
        } catch (Exception ee) {
          logErr("Error: Unexpected exception: ", ee);
          pass = false;
        }

        // properties should not have been deleted by the clearBody method.
        if (messageReceivedBytesMessage
            .getStringProperty("COM_SUN_JMS_TESTNAME")
            .equals("msgClearBodyTopicTest")) {
          logTrace("Pass: Bytes msg properties read ok after clearBody called");
        } else {
          logErr("Fail: Bytes msg properties cleared after clearBody called");
          pass = false;
        }
        logTrace("write 2nd contents");
        messageReceivedBytesMessage.writeByte(bValue2);
        logTrace("read 2nd contents");
        messageReceivedBytesMessage.reset();
        if (messageReceivedBytesMessage.readByte() == bValue2) {
          logTrace("Pass:");
        } else {
          logErr("Fail:");
          pass = false;
        }
      } catch (Exception e) {
        logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // Send and receive a StreamMessage
      logTrace("sending a Stream message");
      messageSentStreamMessage = tool.getDefaultTopicSession()
          .createStreamMessage();
      messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgClearBodyTopicTest");
      messageSentStreamMessage.writeString("Testing...");
      logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSentStreamMessage);
      messageReceivedStreamMessage = (StreamMessage) tool
          .getDefaultTopicSubscriber().receive(timeout);
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

          logErr("Fail: MessageNotReadableException should have been thrown");
          pass = false;
        } catch (javax.jms.MessageNotReadableException e) {
          logTrace("Pass: MessageNotReadableException thrown as expected");
        } catch (Exception ee) {
          logErr("Error: Unexpected exception: ", ee);
          pass = false;
        }

        // properties should not have been deleted by the clearBody method.
        if (messageReceivedStreamMessage
            .getStringProperty("COM_SUN_JMS_TESTNAME")
            .equals("msgClearBodyTopicTest")) {
          logTrace(
              "Pass: Stream msg properties read ok after clearBody called");
        } else {
          logErr("Fail: Stream msg properties cleared after clearBody called");
          pass = false;
        }
        logTrace("write 2nd contents");
        messageReceivedStreamMessage.writeString("new data");
        logTrace("read 2nd contents");
        messageReceivedStreamMessage.reset();
        if (messageReceivedStreamMessage.readString().equals("new data")) {
          logTrace("Pass:");
        } else {
          logErr("Fail:");
        }
      } catch (Exception e) {
        logErr("Error: ", e);
        pass = false;
      }

      // Text Message
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText("sending a Text message");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "msgClearBodyTopicTest");
      logTrace("sending a Text message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      messageReceived = (TextMessage) tool.getDefaultTopicSubscriber()
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
            .equals("msgClearBodyTopicTest")) {
          logTrace("Pass: Text properties read ok after clearBody called");
        } else {
          logErr("Fail: Text properties cleared after clearBody called");
          pass = false;
        }
        logTrace("write and read 2nd contents");
        messageReceived.setText("new data");
        if (messageReceived.getText().equals("new data")) {
          logTrace("Pass:");
        } else {
          logErr("Fail:");
          pass = false;
        }
      } catch (Exception e) {
        logErr("Error: ", e);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: clearBody test failure");
      }
    } catch (Exception e) {
      throw new Fault("msgClearBodyTopicTest", e);
    }
  }

  /*
   * @testName: msgResetTopicTest
   * 
   * @assertion_ids: JMS:JAVADOC:174; JMS:JAVADOC:584;
   *
   * @test_Strategy: create a stream message and a byte message. write to the
   * message body, call the reset method, try to write to the body expect a
   * MessageNotWriteableException to be thrown.
   */

  public void msgResetTopicTest() throws Fault {
    boolean pass = true;
    int nInt = 1000;

    try {
      StreamMessage messageSentStreamMessage = null;
      BytesMessage messageSentBytesMessage = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();

      // StreamMessage
      try {
        logTrace("creating a Stream message");
        messageSentStreamMessage = tool.getDefaultTopicSession()
            .createStreamMessage();
        messageSentStreamMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
            "msgResetTopicTest");

        // write to the message
        messageSentStreamMessage.writeString("Testing...");
        logMsg("reset stream message -  now  should be in readonly mode");
        messageSentStreamMessage.reset();
        messageSentStreamMessage.writeString("new data");
        logErr(
            "Fail: message did not throw MessageNotWriteable exception as expected");
        pass = false;
      } catch (MessageNotWriteableException nw) {
        logTrace("Pass: MessageNotWriteable thrown as expected");
      } catch (Exception e) {
        logErr("Error: ", e);
        pass = false;
      }

      // BytesMessage
      try {
        logTrace("creating a Byte message");
        messageSentBytesMessage = tool.getDefaultTopicSession()
            .createBytesMessage();
        messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
            "msgResetTopicTest");

        // write to the message
        messageSentBytesMessage.writeInt(nInt);
        logMsg("reset Byte message -  now  should be in readonly mode");
        messageSentBytesMessage.reset();
        messageSentBytesMessage.writeInt(nInt);
        logErr(
            "Fail: message did not throw MessageNotWriteable exception as expected");
        pass = false;
      } catch (MessageNotWriteableException nw) {
        logTrace("Pass: MessageNotWriteable thrown as expected");
      } catch (Exception e) {
        logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: msgResetTopicTest test failure");
      }
    } catch (Exception e) {
      throw new Fault("msgResetTopicTest", e);
    }
  }

  /*
   * @testName: readNullCharNotValidTopicTest
   * 
   * @assertion_ids: JMS:SPEC:79; JMS:JAVADOC:134; JMS:JAVADOC:439;
   * 
   * @test_Strategy: Write a null string to a MapMessage and then a
   * StreamMessage. Attempt to read the null value as a char. Verify that a
   * NullPointerException is thrown.
   * 
   */

  public void readNullCharNotValidTopicTest() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
      MapMessage mapSent = null;
      MapMessage mapReceived = null;
      char c;
      boolean pass = true;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createStreamMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "readNullCharNotValidTopicTest");

      // -----------------------------------------------------------------------------
      // Stream Message
      // -----------------------------------------------------------------------------
      logTrace(
          "Write a null string to the stream message object with StreamMessage.writeString");
      messageSent.writeString(null);
      logTrace(" Send the message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logTrace("Use readChar to read a null  ");
      try {
        messageReceived.readChar();
        logErr("Fail: NullPointerException was not thrown");
        pass = false;
      } catch (java.lang.NullPointerException e) {
        logTrace("Pass: NullPointerException thrown as expected");
      } catch (Exception e) {
        logErr("Error: Unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------------------------------------
      // Map Message
      // -----------------------------------------------------------------------------
      mapSent = tool.getDefaultTopicSession().createMapMessage();
      mapSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "readNullCharNotValidTopicTest");
      logTrace(
          "Write a null string to the map message object with mapMessage.setString");
      mapSent.setString("WriteANull", null);
      logTrace(" Send the message");
      tool.getDefaultTopicPublisher().publish(mapSent);
      mapReceived = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      logTrace("Use readChar to read a null  ");
      try {
        mapReceived.getChar("WriteANull");
        logErr("Fail: NullPointerException was not thrown");
        pass = false;
      } catch (java.lang.NullPointerException e) {
        logTrace("Pass: NullPointerException thrown as expected");
      } catch (Exception e) {
        logErr("Error: Unexpected exception: ", e);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("readNullCharNotValidTopicTest", e);
    }
  }

  /*
   * @testName: messageTIllegalarg
   *
   * @assertion_ids: JMS:JAVADOC:775; JMS:JAVADOC:777; JMS:JAVADOC:779;
   * JMS:JAVADOC:781; JMS:JAVADOC:783; JMS:JAVADOC:785; JMS:JAVADOC:787;
   * JMS:JAVADOC:789; JMS:JAVADOC:791;
   *
   * @test_Strategy: Create a TextMessage. Write to the message using each type
   * of setProperty method and as an object with null String as name. Verify
   * that IllegalArgumentException thrown.
   */

  public void messageTIllegalarg() throws Fault {
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

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "messageTIllegalarg");

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
      throw new Fault("messageTIllegalarg", e);
    }
  }

}
