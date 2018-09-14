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
package com.sun.ts.tests.jms.core.bytesMsgTopic;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.*;
import com.sun.javatest.Status;

public class BytesMsgTopicTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.bytesMsgTopic.BytesMsgTopicTests";

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
    BytesMsgTopicTests theTests = new BytesMsgTopicTests();
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

      // get ready for new test
      logMsg("Getting Administrator and deleting any leftover destinations.");
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
   * @testName: bytesMsgNullStreamTopicTest
   * 
   * @assertion_ids: JMS:SPEC:86.1; JMS:JAVADOC:714;
   * 
   * @test_Strategy: create a byte message. Use writeObject to write a null.
   * verify a java.lang.NullPointerException is thrown.
   */

  public void bytesMsgNullStreamTopicTest() throws Fault {
    boolean pass = true;
    int nInt = 1000;

    try {
      BytesMessage messageSentBytesMessage = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();

      // BytesMessage
      try {
        logMsg(
            "Writing a null stream to byte message should throw a NullPointerException");
        messageSentBytesMessage = tool.getDefaultTopicSession()
            .createBytesMessage();
        messageSentBytesMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
            "bytesMsgNullStreamTopicTest");

        // write a null to the message
        messageSentBytesMessage.writeObject(null);
        logMsg(
            "Fail: message did not throw NullPointerException exception as expected");
        pass = false;
      } catch (java.lang.NullPointerException np) {
        logTrace("Pass: NullPointerException thrown as expected");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: " + e.getClass().getName() + " was thrown");
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: bytesMsgNullStreamTopicTest test failure");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("bytesMsgNullStreamTopicTest");
    }
  }

  /*
   * @testName: bytesMessageTopicTestsFullMsg
   * 
   * @assertion_ids: JMS:JAVADOC:560; JMS:JAVADOC:562; JMS:JAVADOC:564;
   * JMS:JAVADOC:566; JMS:JAVADOC:568; JMS:JAVADOC:570; JMS:JAVADOC:572;
   * JMS:JAVADOC:574; JMS:JAVADOC:576; JMS:JAVADOC:578; JMS:JAVADOC:580;
   * JMS:JAVADOC:582; JMS:JAVADOC:534; JMS:JAVADOC:536; JMS:JAVADOC:540;
   * JMS:JAVADOC:544; JMS:JAVADOC:546; JMS:JAVADOC:548; JMS:JAVADOC:550;
   * JMS:JAVADOC:552; JMS:JAVADOC:554; JMS:JAVADOC:556; JMS:JAVADOC:558;
   * 
   * @test_Strategy: Create a BytesMessage -. write to the message using each
   * type of method and as an object. Send the message. Verify the data received
   * was as sent.
   * 
   */

  public void bytesMessageTopicTestsFullMsg() throws Fault {
    try {
      BytesMessage messageSent = null;
      BytesMessage messageReceived = null;
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

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createBytesMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "bytesMessageTopicTestsFullMsg");

      // -----------------------------------------------------------------------------
      logMsg("Writing one of each primitive type to the message");

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

      // send the message and then get it back
      logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logTrace("Receiving message");
      messageReceived = (BytesMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      try {
        if (messageReceived.readBoolean() == booleanValue) {
          logTrace("Pass: boolean returned ok");
        } else {
          logMsg("Fail: boolean not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      try {
        if (messageReceived.readByte() == byteValue) {
          logTrace("Pass: Byte returned ok");
        } else {
          logMsg("Fail: Byte not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      try {
        if (messageReceived.readChar() == charValue) {
          logTrace("Pass: correct char");
        } else {
          logMsg("Fail: char not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      try {
        if (messageReceived.readDouble() == doubleValue) {
          logTrace("Pass: correct double");
        } else {
          logMsg("Fail: double not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      try {
        if (messageReceived.readFloat() == floatValue) {
          logTrace("Pass: correct float");
        } else {
          logMsg("Fail: float not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      try {
        if (messageReceived.readInt() == intValue) {
          logTrace("Pass: correct int");
        } else {
          logMsg("Fail: int not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      try {
        if (messageReceived.readLong() == longValue) {
          logTrace("Pass: correct long");
        } else {
          logMsg("Fail: long not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      try {
        if (messageReceived.readInt() == nInteger.intValue()) {
          logTrace("Pass: correct Integer returned");
        } else {
          logMsg("Fail: Integer not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      try {
        if (messageReceived.readShort() == shortValue) {
          logTrace("Pass: correct short");
        } else {
          logMsg("Fail: short not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      try {
        if (messageReceived.readUTF().equals(utfValue)) {
          logTrace("Pass: correct UTF");
        } else {
          logMsg("Fail: UTF not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      try {
        int nCount = messageReceived.readBytes(bytesValueRecvd);

        for (int i = 0; i < nCount; i++) {
          if (bytesValueRecvd[i] != bytesValue[i]) {
            logMsg("Fail: bytes value incorrect");
            pass = false;
          } else {
            logTrace("Pass: byte value " + i + " ok");
          }
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      try {
        int nCount = messageReceived.readBytes(bytesValueRecvd);

        logTrace("count returned " + nCount);
        if (bytesValueRecvd[0] != bytesValue[0]) {
          logMsg("Fail: bytes value incorrect");
          pass = false;
        } else {
          logTrace("Pass: byte value ok");
        }
        if (nCount == 1) {
          logTrace("Pass: correct count");
        } else {
          logMsg("Fail: count not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logMsg("Error: unexpected exception" + e.getClass().getName()
            + "was thrown");
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("bytesMessageTopicTestsFullMsg", e);
    }
  }

  /*
   * @testName: bytesMessageTNotWriteable
   *
   * @assertion_ids: JMS:SPEC:73; JMS:JAVADOC:701; JMS:JAVADOC:702;
   * JMS:JAVADOC:703; JMS:JAVADOC:704; JMS:JAVADOC:705; JMS:JAVADOC:706;
   * JMS:JAVADOC:707; JMS:JAVADOC:708; JMS:JAVADOC:709; JMS:JAVADOC:710;
   * JMS:JAVADOC:711; JMS:JAVADOC:713;
   *
   * @test_Strategy: Create a BytesMessage - send it to a Topic. Write to the
   * received message using each type of method and as an object. Verify
   * MessageNotWriteableException thrown
   */

  public void bytesMessageTNotWriteable() throws Fault {
    try {
      BytesMessage messageSent = null;
      BytesMessage messageReceived = null;
      boolean pass = true;
      byte bValue = 127;
      byte[] bbValue = { 127, -127, 1, 0 };
      char cValue = 'Z';
      double dValue = 6.02e23;
      float fValue = 6.02e23f;
      int iValue = 2147483647;
      long lValue = 9223372036854775807L;
      short sValue = -32768;
      String ssValue = "what";

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createBytesMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "bytesMessageTNotWriteable");

      messageSent.writeBytes(bbValue);

      // send the message and then get it back
      logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logTrace("Receiving message");
      messageReceived = (BytesMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      logMsg("Writing a boolean ... ");
      try {
        messageReceived.writeBoolean(pass);
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to writeBoolean");
      } catch (MessageNotWriteableException e) {
        logMsg("Got Expected MessageNotWriteableException with writeBoolean");
      }

      logMsg("Writing a byte ... ");
      try {
        messageReceived.writeByte(bValue);
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to writeByte");
      } catch (MessageNotWriteableException e) {
        logMsg("Got Expected MessageNotWriteableException with writeByte");
      }

      logMsg("Writing a short ... ");
      try {
        messageReceived.writeShort(sValue);
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to writeShort");
      } catch (MessageNotWriteableException e) {
        logMsg("Got Expected MessageNotWriteableException with writeShort");
      }

      logMsg("Writing a char ... ");
      try {
        messageReceived.writeChar(cValue);
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to writeChar");
      } catch (MessageNotWriteableException e) {
        logMsg("Got Expected MessageNotWriteableException with writeChar");
      }

      logMsg("Writing a int ... ");
      try {
        messageReceived.writeInt(iValue);
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to writeInt");
      } catch (MessageNotWriteableException e) {
        logMsg("Got Expected MessageNotWriteableException with writeInt");
      }

      logMsg("Writing a long ... ");
      try {
        messageReceived.writeLong(lValue);
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to writeLong");
      } catch (MessageNotWriteableException e) {
        logMsg("Got Expected MessageNotWriteableException with writeLong");
      }

      logMsg("Writing a float ... ");
      try {
        messageReceived.writeFloat(fValue);
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to writeFloat");
      } catch (MessageNotWriteableException e) {
        logMsg("Got Expected MessageNotWriteableException with writeFloat");
      }

      logMsg("Writing a double ... ");
      try {
        messageReceived.writeDouble(dValue);
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to writeDouble");
      } catch (MessageNotWriteableException e) {
        logMsg("Got Expected MessageNotWriteableException with writeDouble");
      }

      logMsg("Writing a bytes... ");
      try {
        messageReceived.writeBytes(bbValue);
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to writeBytes");
      } catch (MessageNotWriteableException e) {
        logMsg("Got Expected MessageNotWriteableException with writeBytes");
      }

      logMsg("Writing a bytes... ");
      try {
        messageReceived.writeBytes(bbValue, 0, 2);
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to writeBytes");
      } catch (MessageNotWriteableException e) {
        logMsg("Got Expected MessageNotWriteableException with writeBytes");
      }

      logMsg("Writing a UTF ... ");
      try {
        messageReceived.writeUTF(ssValue);
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to writeUTF");
      } catch (MessageNotWriteableException e) {
        logMsg("Got Expected MessageNotWriteableException with writeUTF");
      }

      logMsg("Writing a object ... ");
      try {
        messageReceived.writeObject(new Integer(iValue));
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to writeObject");
      } catch (MessageNotWriteableException e) {
        logMsg("Got Expected MessageNotWriteableException with writeObject");
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("BytesMessageQueueNotWriteable", e);
    }
  }
}
