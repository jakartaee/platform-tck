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
package com.sun.ts.tests.jms.core.mapMsgTopic;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.*;
import com.sun.javatest.Status;

public class MapMsgTopicTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.mapMsgTopic.MapMsgTopicTests";

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
    MapMsgTopicTests theTests = new MapMsgTopicTests();
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
        TestUtil.logMsg("Cleanup: Closing Queue and Topic Connections");
        tool.closeAllConnections(connections);
      }
    } catch (Exception e) {
      TestUtil.logErr("An error occurred while cleaning: ", e);
      throw new Fault("Cleanup failed!", e);
    }
  }

  /* Tests */

  /*
   * @testName: mapMessageFullMsgTopicTest
   * 
   * @assertion_ids: JMS:SPEC:74; JMS:JAVADOC:211; JMS:JAVADOC:457;
   * JMS:JAVADOC:459; JMS:JAVADOC:475; JMS:JAVADOC:477; JMS:JAVADOC:479;
   * JMS:JAVADOC:461; JMS:JAVADOC:463; JMS:JAVADOC:465; JMS:JAVADOC:467;
   * JMS:JAVADOC:469; JMS:JAVADOC:471; JMS:JAVADOC:473; JMS:JAVADOC:433;
   * JMS:JAVADOC:435; JMS:JAVADOC:437; JMS:JAVADOC:439; JMS:JAVADOC:441;
   * JMS:JAVADOC:443; JMS:JAVADOC:445; JMS:JAVADOC:447; JMS:JAVADOC:449;
   * JMS:JAVADOC:451; JMS:JAVADOC:453;
   * 
   * @test_Strategy: Create a MapMessage -. write to the message using each type
   * of method and as an object. Send the message. Verify the data received was
   * as sent.
   * 
   * 
   */

  public void mapMessageFullMsgTopicTest() throws Fault {
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
      MapMessage messageSentMapMessage = null;
      MapMessage messageReceivedMapMessage = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();

      // send and receive map message to Topic
      TestUtil.logTrace("Send MapMessage to Topic.");
      messageSentMapMessage = tool.getDefaultTopicSession().createMapMessage();
      messageSentMapMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "mapMessageFullMsgTopicTest");
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
      tool.getDefaultTopicPublisher().publish(messageSentMapMessage);
      messageReceivedMapMessage = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      try {
        if (messageReceivedMapMessage
            .getBoolean("booleanValue") == booleanValue) {
          TestUtil.logTrace("Pass: valid boolean returned");
        } else {
          TestUtil.logMsg("Fail: invalid boolean returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Fail: unexpected exception: ", e);
        pass = false;
      }
      try {
        if (messageReceivedMapMessage.getByte("byteValue") == byteValue) {
          TestUtil.logTrace("Pass: valid byte returned");
        } else {
          TestUtil.logMsg("Fail: invalid byte returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Fail: unexpected exception: ", e);
        pass = false;
      }
      try {
        byte[] b = messageReceivedMapMessage.getBytes("bytesValue");

        for (int i = 0; i < b.length; i++) {
          if (b[i] != bytesValue[i]) {
            TestUtil.logMsg("Fail: byte array " + i + " not valid");
            pass = false;
          } else {
            TestUtil.logTrace("Pass: byte array " + i + " valid");
          }
        }
      } catch (Exception e) {
        TestUtil.logErr("Fail: unexpected exception: ", e);
        pass = false;
      }
      try {
        byte[] b = messageReceivedMapMessage.getBytes("bytesValue2");

        if (b[0] != bytesValue[0]) {
          TestUtil.logMsg("Fail: byte array not valid");
          pass = false;
        } else {
          TestUtil.logTrace("Pass: byte array valid");
        }
      } catch (Exception e) {
        TestUtil.logErr("Fail: unexpected exception: ", e);
        pass = false;
      }
      try {
        if (messageReceivedMapMessage.getChar("charValue") == charValue) {
          TestUtil.logTrace("Pass: valid char returned");
        } else {
          TestUtil.logMsg("Fail: invalid char returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Fail: unexpected exception: ", e);
        pass = false;
      }
      try {
        if (messageReceivedMapMessage.getDouble("doubleValue") == doubleValue) {
          TestUtil.logTrace("Pass: valid double returned");
        } else {
          TestUtil.logMsg("Fail: invalid double returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Fail: unexpected exception: ", e);
        pass = false;
      }
      try {
        if (messageReceivedMapMessage.getFloat("floatValue") == floatValue) {
          TestUtil.logTrace("Pass: valid float returned");
        } else {
          TestUtil.logMsg("Fail: invalid float returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Fail: unexpected exception: ", e);
        pass = false;
      }
      try {
        if (messageReceivedMapMessage.getInt("intValue") == intValue) {
          TestUtil.logTrace("Pass: valid int returned");
        } else {
          TestUtil.logMsg("Fail: invalid int returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Fail: unexpected exception: ", e);
        pass = false;
      }
      try {
        if (messageReceivedMapMessage.getLong("longValue") == longValue) {
          TestUtil.logTrace("Pass: valid long returned");
        } else {
          TestUtil.logMsg("Fail: invalid long returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Fail: unexpected exception: ", e);
        pass = false;
      }
      try {
        if (messageReceivedMapMessage.getObject("integerValue").toString()
            .equals(integerValue.toString())) {
          TestUtil.logTrace("Pass: valid object returned");
        } else {
          TestUtil.logMsg("Fail: invalid object returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Fail: unexpected exception: ", e);
        pass = false;
      }
      try {
        if (messageReceivedMapMessage.getShort("shortValue") == shortValue) {
          TestUtil.logTrace("Pass: valid short returned");
        } else {
          TestUtil.logMsg("Fail: invalid short returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Fail: unexpected exception: ", e);
        pass = false;
      }
      try {
        if (messageReceivedMapMessage.getString("stringValue")
            .equals(stringValue)) {
          TestUtil.logTrace("Pass: valid string returned");
        } else {
          TestUtil.logMsg("Fail: invalid string returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Fail: unexpected exception: ", e);
        pass = false;
      }
      try {
        if (messageReceivedMapMessage.getString("nullTest") == null) {
          TestUtil.logTrace("Pass: null returned");
        } else {
          TestUtil.logMsg("Fail:  null not returned from getString");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Fail: unexpected exception: ", e);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: mapMessageFullMsgTopicTest test failure");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("mapMessageFullMsgTopicTest");
    }
  }

  /*
   * @testName: MapMessageConversionTopicTestsBoolean
   * 
   * @assertion_ids: JMS:SPEC:75.1; JMS:SPEC:75.2; JMS:JAVADOC:457;
   * JMS:JAVADOC:433; JMS:JAVADOC:449; JMS:JAVADOC:796; JMS:JAVADOC:797;
   * JMS:JAVADOC:798; JMS:JAVADOC:799; JMS:JAVADOC:800; JMS:JAVADOC:801;
   * JMS:JAVADOC:802; JMS:JAVADOC:804;
   * 
   * @test_Strategy: Create a MapMessage -. use MapMessage method writeBoolean
   * to write a boolean to the message. Verify the proper conversion support as
   * in 3.11.3
   */

  public void MapMessageConversionTopicTestsBoolean() throws Fault {
    try {
      MapMessage messageSent = null;
      MapMessage messageReceived = null;
      boolean booleanValue = true;
      boolean pass = true;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createMapMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "MapMessageConversionTopicTestsBoolean");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for boolean primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.setBoolean("booleanValue", booleanValue);

      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // now test conversions for boolean
      // -----------------------------------------------
      // boolean to boolean - valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use readBoolean to read a boolean");
      try {
        if (messageReceived.getBoolean("booleanValue") == booleanValue) {
          TestUtil.logTrace("Pass: boolean to boolean - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // boolean to string valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use readString to read a boolean");
      try {
        if (messageReceived.getString("booleanValue")
            .equals((Boolean.valueOf(booleanValue)).toString())) {
          TestUtil.logTrace("Pass: boolean to string - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // boolean to byte[] invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg(
          "Use readBytes[] to read a boolean - expect MessageFormatException");
      int nCount = 0;

      try {
        byte[] b = messageReceived.getBytes("booleanValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      TestUtil.logMsg("Count returned from readBytes is : " + nCount);

      // -----------------------------------------------
      // boolean to byte invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg(
          "Use readByte to read a boolean - expect MessageFormatException");
      try {
        byte b = messageReceived.getByte("booleanValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // boolean to short invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg(
          "Use readShort to read a boolean - expect MessageFormatException");
      try {
        short s = messageReceived.getShort("booleanValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // boolean to char invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg(
          "Use readChar to read a boolean - expect MessageFormatException");
      try {
        char c = messageReceived.getChar("booleanValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // boolean to int invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg(
          "Use readInt to read a boolean - expect MessageFormatException");
      try {
        int i = messageReceived.getInt("booleanValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // boolean to long invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg(
          "Use readLong to read a boolean - expect MessageFormatException");
      try {
        long l = messageReceived.getLong("booleanValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // boolean to float invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg(
          "Use readFloat to read a boolean - expect MessageFormatException");
      try {
        float f = messageReceived.getFloat("booleanValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // boolean to double invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg(
          "Use readDouble to read a boolean - expect MessageFormatException");
      try {
        double d = messageReceived.getDouble("booleanValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("MapMessageConversionTopicTestsBoolean", e);
    }
  }

  /*
   * @testName: MapMessageConversionTopicTestsByte
   * 
   * @assertion_ids: JMS:SPEC:75.3; JMS:SPEC:75.4; JMS:JAVADOC:459;
   * JMS:JAVADOC:435; JMS:JAVADOC:437; JMS:JAVADOC:441; JMS:JAVADOC:443;
   * JMS:JAVADOC:449; JMS:JAVADOC:795; JMS:JAVADOC:798; JMS:JAVADOC:801;
   * JMS:JAVADOC:802; JMS:JAVADOC:804;
   * 
   * @test_Strategy: Create a MapMessage -. use MapMessage method setByte to
   * write a byte. Verify the proper conversion support as in 3.11.3
   * 
   */

  public void MapMessageConversionTopicTestsByte() throws Fault {
    MapMessage messageSent = null;
    MapMessage messageReceived = null;
    byte byteValue = 127;
    boolean pass = true;

    try {

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createMapMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "MapMessageConversionTopicTestsByte");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.setByte("byteValue", byteValue);

      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // now test conversions for byte
      // -----------------------------------------------
      // byte to boolean - invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getBoolean to read a byte - this is not valid");
      try {
        boolean b = messageReceived.getBoolean("byteValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte to string valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getString to read a byte");
      try {
        if (messageReceived.getString("byteValue")
            .equals(Byte.toString(byteValue))) {
          TestUtil.logTrace("Pass: byte to string - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte to byte[] invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg(
          "Use getBytes[] to read a byte - expect MessageFormatException");
      int nCount = 0;

      try {
        byte[] b = messageReceived.getBytes("byteValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte to byte valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getByte to read a byte");
      try {
        if (messageReceived.getByte("byteValue") == byteValue) {
          TestUtil.logTrace("Pass: byte to byte - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte to short valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getShort to read a byte");
      try {
        if (messageReceived.getShort("byteValue") == byteValue) {
          TestUtil.logTrace("Pass: byte to short - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte to char invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getChar to read a boolean - this is not valid");
      try {
        char c = messageReceived.getChar("byteValue");

        pass = false;
        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte to int valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getInt to read a byte");
      try {
        if (messageReceived.getInt("byteValue") == byteValue) {
          TestUtil.logTrace("Pass: byte to int - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte to long valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getLong to read a byte");
      try {
        if (messageReceived.getLong("byteValue") == byteValue) {
          TestUtil.logTrace("Pass: byte to long - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte to float invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getFloat to read a boolean - this is not valid");
      try {
        float f = messageReceived.getFloat("byteValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte to double invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getDouble to read a boolean - this is not valid");
      try {
        double d = messageReceived.getDouble("byteValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("MapMessageConversionTopicTestsByte", e);
    }
  }

  /*
   * @testName: MapMessageConversionTopicTestsShort
   * 
   * @assertion_ids: JMS:SPEC:75.5; JMS:SPEC:75.6; JMS:JAVADOC:461;
   * JMS:JAVADOC:437; JMS:JAVADOC:441; JMS:JAVADOC:443; JMS:JAVADOC:449;
   * JMS:JAVADOC:795; JMS:JAVADOC:796; JMS:JAVADOC:798; JMS:JAVADOC:801;
   * JMS:JAVADOC:802; JMS:JAVADOC:804;
   * 
   * @test_Strategy: Create a MapMessage -. use MapMessage method writeShort to
   * write a short. Verify the proper conversion support as in 3.11.3
   * 
   */

  public void MapMessageConversionTopicTestsShort() throws Fault {
    try {
      MapMessage messageSent = null;
      MapMessage messageReceived = null;
      short shortValue = 1;
      boolean pass = true;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createMapMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "MapMessageConversionTopicTestsShort");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.setShort("shortValue", shortValue);

      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // now test conversions for byte
      // -----------------------------------------------
      // short to boolean - invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getBoolean to read a short - this is not valid");
      try {
        boolean b = messageReceived.getBoolean("shortValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // short to string valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getString to read a short");
      try {
        if (messageReceived.getString("shortValue")
            .equals(Short.toString(shortValue))) {
          TestUtil.logTrace("Pass: short to string - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // short to byte[] invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg(
          "Use getBytes[] to read a short - expect MessageFormatException");
      try {
        byte[] b = messageReceived.getBytes("shortValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // short to byte invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getByte to read a short - this is not valid");
      try {
        byte b = messageReceived.getByte("shortValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // short to short valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getShort to read a short");
      try {
        if (messageReceived.getShort("shortValue") == shortValue) {
          TestUtil.logTrace("Pass: short to short - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // short to char invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getChar to read a short - this is not valid");
      try {
        char c = messageReceived.getChar("shortValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // short to int valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getInt to read a short");
      try {
        if (messageReceived.getInt("shortValue") == shortValue) {
          TestUtil.logTrace("Pass: short to int - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // short to long valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getLong to read a short");
      try {
        if (messageReceived.getLong("shortValue") == shortValue) {
          TestUtil.logTrace("Pass: short to long - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // short to float invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getFloat to read a short - this is not valid");
      try {
        float f = messageReceived.getFloat("shortValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // short to double invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getDouble to read a short - this is not valid");
      try {
        double d = messageReceived.getDouble("shortValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("MapMessageConversionTopicTestsShort", e);
    }
  }

  /*
   * @testName: MapMessageConversionTopicTestsChar
   * 
   * @assertion_ids: JMS:SPEC:75.7; JMS:SPEC:75.8; JMS:JAVADOC:463;
   * JMS:JAVADOC:439; JMS:JAVADOC:449; JMS:JAVADOC:795; JMS:JAVADOC:796;
   * JMS:JAVADOC:797; JMS:JAVADOC:799; JMS:JAVADOC:800; JMS:JAVADOC:801;
   * JMS:JAVADOC:802; JMS:JAVADOC:804;
   * 
   * @test_Strategy: Create a MapMessage -. use MapMessage method writeChar to
   * write a char. Verify the proper conversion support as in 3.11.3
   * 
   */

  public void MapMessageConversionTopicTestsChar() throws Fault {
    try {
      MapMessage messageSent = null;
      MapMessage messageReceived = null;
      char charValue = 'a';
      boolean pass = true;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createMapMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "MapMessageConversionTopicTestsChar");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.setChar("charValue", charValue);

      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // now test conversions for byte
      // -----------------------------------------------
      // char to boolean - invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getBoolean to read a char - this is not valid");
      try {
        boolean b = messageReceived.getBoolean("charValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // char to string valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getString to read a char");
      try {
        if (messageReceived.getString("charValue")
            .equals(Character.valueOf(charValue).toString())) {
          TestUtil.logTrace("Pass: char to string - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // char to byte[] invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg(
          "Use getBytes[] to read a char - expect MessageFormatException");
      try {
        byte[] b = messageReceived.getBytes("charValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // char to byte invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getByte to read a char - this is not valid");
      try {
        byte b = messageReceived.getByte("charValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // char to short invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getShort to read a char");
      try {
        short s = messageReceived.getShort("charValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // char to char valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getChar to read a char ");
      try {
        if (messageReceived.getChar("charValue") == charValue) {
          TestUtil.logTrace("Pass: char to char - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // char to int invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getInt to read a char ");
      try {
        int i = messageReceived.getInt("charValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // char to long invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getLong to read a char");
      try {
        long l = messageReceived.getLong("charValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // char to float invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getFloat to read a char - this is not valid");
      try {
        float f = messageReceived.getFloat("charValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // char to double invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getDouble to read a char - this is not valid");
      try {
        double d = messageReceived.getDouble("charValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("MapMessageConversionTopicTestsChar", e);
    }
  }

  /*
   * @testName: MapMessageConversionTopicTestsInt
   * 
   * @assertion_ids: JMS:SPEC:75.9; JMS:SPEC:75.10; JMS:JAVADOC:465;
   * JMS:JAVADOC:441; JMS:JAVADOC:443; JMS:JAVADOC:449; JMS:JAVADOC:795;
   * JMS:JAVADOC:796; JMS:JAVADOC:797; JMS:JAVADOC:798; JMS:JAVADOC:801;
   * JMS:JAVADOC:802; JMS:JAVADOC:804;
   * 
   * @test_Strategy: Create a MapMessage -. use MapMessage method writeInt to
   * write an int. Verify the proper conversion support as in 3.11.3
   * 
   */

  public void MapMessageConversionTopicTestsInt() throws Fault {
    try {
      MapMessage messageSent = null;
      MapMessage messageReceived = null;
      int intValue = 6;
      boolean pass = true;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createMapMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "MapMessageConversionTopicTestsInt");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.setInt("intValue", intValue);

      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // now test conversions for byte
      // -----------------------------------------------
      // int to boolean - invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getBoolean to read an int - this is not valid");
      try {
        boolean b = messageReceived.getBoolean("intValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // int to string valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getString to read an int");
      try {
        if (messageReceived.getString("intValue")
            .equals(Integer.toString(intValue))) {
          TestUtil.logTrace("Pass: int to string - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // int to byte[] invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg(
          "Use getBytes[] to read an int - expect MessageFormatException");
      int nCount = 0;

      try {
        byte[] b = messageReceived.getBytes("intValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // int to byte invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getByte to read an int - this is not valid");
      try {
        byte b = messageReceived.getByte("intValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // int to short invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getShort to read an int");
      try {
        short s = messageReceived.getShort("intValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // int to char invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getChar to read an int - this is not valid");
      try {
        char c = messageReceived.getChar("intValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // int to int valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getInt to read an int");
      try {
        if (messageReceived.getInt("intValue") == intValue) {
          TestUtil.logTrace("Pass: int to int - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // int to long valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getLong to read an int");
      try {
        if (messageReceived.getLong("intValue") == intValue) {
          TestUtil.logTrace("Pass: int to long - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // int to float invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getFloat to read an int - this is not valid");
      try {
        float f = messageReceived.getFloat("intValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // int to double invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getDouble to read an int - this is not valid");
      try {
        double d = messageReceived.getDouble("intValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("MapMessageConversionTopicTestsInt", e);
    }
  }

  /*
   * @testName: MapMessageConversionTopicTestsLong
   * 
   * @assertion_ids: JMS:SPEC:75.11; JMS:SPEC:75.12; JMS:JAVADOC:467;
   * JMS:JAVADOC:443; JMS:JAVADOC:449; JMS:JAVADOC:795; JMS:JAVADOC:796;
   * JMS:JAVADOC:797; JMS:JAVADOC:798; JMS:JAVADOC:799; JMS:JAVADOC:801;
   * JMS:JAVADOC:802; JMS:JAVADOC:804;
   * 
   * @test_Strategy: Create a MapMessage -. use MapMessage method writeLong to
   * write a long. Verify the proper conversion support as in 3.11.3
   * 
   */

  public void MapMessageConversionTopicTestsLong() throws Fault {
    try {
      MapMessage messageSent = null;
      MapMessage messageReceived = null;
      long longValue = 2;
      boolean pass = true;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createMapMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "MapMessageConversionTopicTestsLong");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.setLong("longValue", longValue);

      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // now test conversions for byte
      // -----------------------------------------------
      // long to boolean - invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getBoolean to read a long - this is not valid");
      try {
        boolean b = messageReceived.getBoolean("longValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // long to string valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getString to read a long");
      try {
        if (messageReceived.getString("longValue")
            .equals(Long.toString(longValue))) {
          TestUtil.logTrace("Pass: long to string - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // long to byte[] invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg(
          "Use getBytes[] to read  a long - expect MessageFormatException");
      try {
        byte[] b = messageReceived.getBytes("longValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // long to byte invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getByte to read an long - this is not valid");
      try {
        byte b = messageReceived.getByte("longValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // long to short invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getShort to read a long");
      try {
        short s = messageReceived.getShort("longValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // long to char invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getChar to read a long - this is not valid");
      try {
        char c = messageReceived.getChar("longValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // long to int invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getInt to read a long");
      try {
        int i = messageReceived.getInt("longValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // long to long valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getLong to read a long");
      try {
        if (messageReceived.getLong("longValue") == longValue) {
          TestUtil.logTrace("Pass: int to long - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // long to float invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getFloat to read a long - this is not valid");
      try {
        float f = messageReceived.getFloat("longValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // long to double invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getDouble to read a long ");
      try {
        double d = messageReceived.getDouble("longValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("MapMessageConversionTopicTestsLong", e);
    }
  }

  /*
   * @testName: MapMessageConversionTopicTestsFloat
   * 
   * @assertion_ids: JMS:SPEC:75.13; JMS:SPEC:75.14; JMS:JAVADOC:469;
   * JMS:JAVADOC:445; JMS:JAVADOC:449; JMS:JAVADOC:795; JMS:JAVADOC:796;
   * JMS:JAVADOC:797; JMS:JAVADOC:798; JMS:JAVADOC:799; JMS:JAVADOC:800;
   * JMS:JAVADOC:802; JMS:JAVADOC:804;
   * 
   * @test_Strategy: Create a MapMessage -. use MapMessage method writeFloat to
   * write a float. Verify the proper conversion support as in 3.11.3
   * 
   */

  public void MapMessageConversionTopicTestsFloat() throws Fault {
    try {
      MapMessage messageSent = null;
      MapMessage messageReceived = null;
      float floatValue = 5;
      boolean pass = true;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createMapMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "MapMessageConversionTopicTestsFloat");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.setFloat("floatValue", floatValue);

      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // now test conversions for byte
      // -----------------------------------------------
      // float to boolean - invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getBoolean to read a float  ");
      try {
        boolean b = messageReceived.getBoolean("floatValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // float to string valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getString to read a float");
      try {
        if (messageReceived.getString("floatValue")
            .equals(Float.toString(floatValue))) {
          TestUtil.logTrace("Pass: float to string - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // float to byte[] invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getBytes[] to read  a float ");
      try {
        byte[] b = messageReceived.getBytes("floatValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // float to byte invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getByte to read a float  ");
      try {
        byte b = messageReceived.getByte("floatValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // float to short invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getShort to read a float");
      try {
        short s = messageReceived.getShort("floatValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // float to char invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getChar to read a long  ");
      try {
        char c = messageReceived.getChar("floatValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // float to int invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getInt to read a float");
      try {
        int i = messageReceived.getInt("floatValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // float to long invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getLong to read a long");
      try {
        long l = messageReceived.getLong("floatValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // float to float valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getFloat to read a float  ");
      try {
        if (messageReceived.getFloat("floatValue") == floatValue) {
          TestUtil.logTrace("Pass: float to float - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // float to double valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getDouble to read a float  ");
      try {
        if (messageReceived.getDouble("floatValue") == floatValue) {
          TestUtil.logTrace("Pass: float to double - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("MapMessageConversionTopicTestsFloat", e);
    }
  }

  /*
   * @testName: MapMessageConversionTopicTestsDouble
   * 
   * @assertion_ids: JMS:SPEC:75.15; JMS:SPEC:75.16; JMS:JAVADOC:471;
   * JMS:JAVADOC:447; JMS:JAVADOC:449; JMS:JAVADOC:795; JMS:JAVADOC:796;
   * JMS:JAVADOC:797; JMS:JAVADOC:798; JMS:JAVADOC:799; JMS:JAVADOC:800;
   * JMS:JAVADOC:801; JMS:JAVADOC:804;
   * 
   * @test_Strategy: Create a MapMessage -. use MapMessage method writeDouble to
   * write a double. Verify the proper conversion support as in 3.11.3
   * 
   */

  public void MapMessageConversionTopicTestsDouble() throws Fault {
    try {
      MapMessage messageSent = null;
      MapMessage messageReceived = null;
      double doubleValue = 3;
      boolean pass = true;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createMapMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "MapMessageConversionTopicTestsDouble");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.setDouble("doubleValue", doubleValue);

      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // now test conversions for byte
      // -----------------------------------------------
      // double to boolean - invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getBoolean to read a double  ");
      try {
        boolean b = messageReceived.getBoolean("doubleValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // double to string valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getString to read a double");
      try {
        if (messageReceived.getString("doubleValue")
            .equals(Double.toString(doubleValue))) {
          TestUtil.logTrace("Pass: double to string");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // double to byte[] invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getBytes[] to read  a double ");
      try {
        byte[] b = messageReceived.getBytes("doubleValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // double to byte invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getByte to read a double  ");
      try {
        byte b = messageReceived.getByte("doubleValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // double to short invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getShort to read a double");
      try {
        short s = messageReceived.getShort("doubleValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // double to char invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getChar to read a double  ");
      try {
        char c = messageReceived.getChar("doubleValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // double to int invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getInt to read a double");
      try {
        int i = messageReceived.getInt("doubleValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // double to long invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getLong to read a double");
      try {
        long l = messageReceived.getLong("doubleValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // double to float invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getFloat to read a double  ");
      try {
        float f = messageReceived.getFloat("doubleValue");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // double to double valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getDouble to read an float  ");
      try {
        if (messageReceived.getDouble("doubleValue") == doubleValue) {
          TestUtil.logTrace("Pass: double to double ");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("MapMessageConversionTopicTestsDouble", e);
    }
  }

  /*
   * @testName: MapMessageConversionTopicTestsString
   * 
   * @assertion_ids: JMS:SPEC:75.17; JMS:SPEC:75.18; JMS:JAVADOC:473;
   * JMS:JAVADOC:433; JMS:JAVADOC:435; JMS:JAVADOC:437; JMS:JAVADOC:441;
   * JMS:JAVADOC:443; JMS:JAVADOC:445; JMS:JAVADOC:447; JMS:JAVADOC:449;
   * JMS:JAVADOC:798; JMS:JAVADOC:804;
   * 
   * @test_Strategy: Create a MapMessage -. use MapMessage method writeString to
   * write a string. Verify the proper conversion support as in 3.11.3
   * 
   */

  public void MapMessageConversionTopicTestsString() throws Fault {
    try {
      MapMessage messageSent = null;
      MapMessage messageReceived = null;
      boolean pass = true;
      String myString = "10";
      String myString2 = "true";

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createMapMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "MapMessageConversionTopicTestsString");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.setString("myString", myString);
      messageSent.setString("myString2", myString2);

      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // now test conversions for String
      // -----------------------------------------------
      // string to string valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getString to read a String");
      try {
        if (messageReceived.getString("myString").equals(myString)) {
          TestUtil.logTrace("Pass: string to string - valid");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // string to byte[] invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getBytes[] to read a String");
      try {
        byte[] b = messageReceived.getBytes("myString");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // String to byte valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getByte to read a String");
      try {
        if (messageReceived.getByte("myString") == Byte.parseByte(myString)) {
          TestUtil.logTrace("Pass: String to byte ");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // string to short valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getShort to read a string");
      try {
        if (messageReceived.getShort("myString") == Short
            .parseShort(myString)) {
          TestUtil.logTrace("Pass: String to short ");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // String to char invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getChar to read a String ");
      try {
        char c = messageReceived.getChar("myString");

        TestUtil.logTrace("getChar returned " + c);
        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // string to int valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getInt to read a String");
      try {
        if (messageReceived.getInt("myString") == Integer.parseInt(myString)) {
          TestUtil.logTrace("Pass: String to int ");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // string to long valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getLong to read a String");
      try {
        if (messageReceived.getLong("myString") == Long.parseLong(myString)) {
          TestUtil.logTrace("Pass: String to long ");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // String to float valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getFloat to read a String");
      try {
        if (messageReceived.getFloat("myString") == Float
            .parseFloat(myString)) {
          TestUtil.logTrace("Pass: String to float ");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // String to double valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getDouble to read a String");
      try {
        if (messageReceived.getDouble("myString") == Double
            .parseDouble(myString)) {
          TestUtil.logTrace("Pass: String to double ");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // String to boolean
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getBoolean to read a string ");
      try {
        if (messageReceived.getBoolean("myString2") == Boolean
            .valueOf(myString2).booleanValue()) {
          TestUtil.logTrace("Pass: String to boolean ");
        } else {
          TestUtil.logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // String to boolean
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getBoolean to read a string that is not true");
      try {
        boolean b = messageReceived.getBoolean("myString");

        if (b != false) {
          TestUtil.logMsg("Fail: !true should have returned false");
          pass = false;
        } else {
          TestUtil.logTrace("Pass: !true returned false");
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("MapMessageConversionTopicTestsString", e);
    }
  }

  /*
   * @testName: MapMessageConversionTopicTestsBytes
   * 
   * @assertion_ids: JMS:SPEC:75.19; JMS:SPEC:75.20; JMS:JAVADOC:475;
   * JMS:JAVADOC:451; JMS:JAVADOC:795; JMS:JAVADOC:796; JMS:JAVADOC:797;
   * JMS:JAVADOC:798; JMS:JAVADOC:799; JMS:JAVADOC:800; JMS:JAVADOC:801;
   * JMS:JAVADOC:802; JMS:JAVADOC:803;
   * 
   * @test_Strategy: Create a MapMessage -. use MapMessage method writeBytes to
   * write a byte[] to the message. Verify the proper conversion support as in
   * 3.11.3
   */

  public void MapMessageConversionTopicTestsBytes() throws Fault {
    try {
      MapMessage messageSent = null;
      MapMessage messageReceived = null;
      byte[] byteValues = { 1, 2, 3 };
      boolean pass = true;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createMapMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "MapMessageConversionTopicTestsBytes");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte[] primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.setBytes("byteValues", byteValues);

      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // now test conversions for boolean
      // -----------------------------------------------
      // byte[] to byte[] - valid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getBytes[] to read a byte[] ");
      try {
        byte[] b = messageReceived.getBytes("byteValues");

        for (int i = 0; i < b.length; i++) {
          if (b[i] != byteValues[i]) {
            TestUtil.logMsg("Fail: byte[] value returned is invalid");
            pass = false;
          } else {
            TestUtil.logTrace("Pass: byte[] returned is valid");
          }
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte[] to boolean - invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getBoolean to read a byte[]");
      try {
        boolean b = messageReceived.getBoolean("byteValues");

        TestUtil.logMsg(
            "Fail: byte[] to boolean conversion should have thrown MessageFormatException");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte[] to string invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getString to read a byte[]");
      try {
        String s = messageReceived.getString("byteValues");

        TestUtil.logMsg(
            "Fail: byte[] to boolean conversion should have thrown MessageFormatException");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte[] to byte invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg(
          "Use getByte to read a byte[] - expect MessageFormatException");
      try {
        byte b = messageReceived.getByte("byteValues");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte[] to short invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg(
          "Use getShort to read a byte[] - expect MessageFormatException");
      try {
        short s = messageReceived.getShort("byteValues");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte[] to char invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg(
          "Use getChar to read a byte[] - expect MessageFormatException");
      try {
        char c = messageReceived.getChar("byteValues");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte[] to int invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg(
          "Use getInt to read a byte[] - expect MessageFormatException");
      try {
        int i = messageReceived.getInt("byteValues");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte[] to long invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg(
          "Use getLong to read a byte[] - expect MessageFormatException");
      try {
        long l = messageReceived.getLong("byteValues");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte[] to float invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg(
          "Use getFloat to read a byte[] - expect MessageFormatException");
      try {
        float f = messageReceived.getFloat("byteValues");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte[] to double invalid
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg(
          "Use getDouble to read a byte[] - expect MessageFormatException");
      try {
        double d = messageReceived.getDouble("byteValues");

        TestUtil.logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (MessageFormatException mf) {
        TestUtil.logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("MapMessageConversionTopicTestsBytes", e);
    }
  }

  /*
   * @testName: MapMessageConversionTopicTestsInvFormatString
   * 
   * @assertion_ids: JMS:SPEC:76;
   * 
   * @test_Strategy: Create a MapMessage -. use MapMessage method setString to
   * write a text string of "mytest string". Verify NumberFormatException is
   * thrown
   * 
   */

  public void MapMessageConversionTopicTestsInvFormatString() throws Fault {
    try {
      MapMessage messageSent = null;
      MapMessage messageReceived = null;
      boolean pass = true;
      String myString = "mytest string";

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createMapMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "MapMessageConversionTopicTestsInvFormatString");

      // -----------------------------------------------------------------------------
      TestUtil.logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.setString("myString", myString);

      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // -----------------------------------------------
      // String to byte
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getByte to read a String that is not valid ");
      try {
        byte b = messageReceived.getByte("myString");

        TestUtil.logMsg("Fail: java.lang.NumberFormatException expected");
        pass = false;
      } catch (NumberFormatException nf) {
        TestUtil.logTrace("Pass: NumberFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // string to short
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getShort to read a string that is not valid ");
      try {
        short s = messageReceived.getShort("myString");

        TestUtil.logMsg("Fail: NumberFormatException was expected");
        pass = false;
      } catch (NumberFormatException nf) {
        TestUtil.logTrace("Pass: NumberFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // string to int
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getInt to read a String that is not valid ");
      try {
        int i = messageReceived.getInt("myString");

        TestUtil.logMsg("Fail: NumberFormatException was expected");
        pass = false;
      } catch (NumberFormatException nf) {
        TestUtil.logTrace("Pass: NumberFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // string to long
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getLong to read a String that is not valid ");
      try {
        long l = messageReceived.getLong("myString");

        TestUtil.logMsg("Fail: NumberFormatException was expected");
        pass = false;
      } catch (NumberFormatException nf) {
        TestUtil.logTrace("Pass: NumberFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // String to float
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getFloat to read a String that is not valid ");
      try {
        float f = messageReceived.getFloat("myString");

        TestUtil.logMsg("Fail: NumberFormatException was expected");
        pass = false;
      } catch (NumberFormatException nf) {
        TestUtil.logTrace("Pass: NumberFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // String to double
      // -----------------------------------------------
      TestUtil.logMsg("--");
      TestUtil.logMsg("Use getDouble to read a String that is not valid ");
      try {
        double d = messageReceived.getDouble("myString");

        TestUtil.logMsg("Fail: NumberFormatException was expected");
        pass = false;
      } catch (NumberFormatException nf) {
        TestUtil.logTrace("Pass: NumberFormatException thrown as expected");
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("MapMessageConversionTopicTestsInvFormatString", e);
    }
  }

  /*
   * @testName: mapMessageTNotWritable
   *
   * @assertion_ids: JMS:SPEC:73; JMS:JAVADOC:806; JMS:JAVADOC:808;
   * JMS:JAVADOC:810; JMS:JAVADOC:812; JMS:JAVADOC:814; JMS:JAVADOC:816;
   * JMS:JAVADOC:818; JMS:JAVADOC:820; JMS:JAVADOC:822; JMS:JAVADOC:824;
   * JMS:JAVADOC:826; JMS:JAVADOC:829;
   *
   * @test_Strategy: Create a MapMessage, send it to a Topic Receive it and try
   * to write to the received Message's body, MessageNotWritableException should
   * be thrown.
   */

  public void mapMessageTNotWritable() throws Fault {
    try {
      MapMessage messageSent = null;
      MapMessage messageReceived = null;
      boolean pass = true;
      byte bValue = 127;
      short sValue = 32767;
      char cValue = '\uFFFF';
      int iValue = 2147483647;
      long lValue = 9223372036854775807L;
      float fValue = 0.0f;
      double dValue = -0.0;
      String ssValue = "abc";
      byte[] bbValue = { 0, 88, 127 };

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      messageSent = tool.getDefaultTopicSession().createMapMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "mapMessageTNotWritable");

      // -----------------------------------------------------------------------------
      try {
        messageSent.setString("ssValue", ssValue);
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception was thrown: ", e);
        throw new Fault("Error: failed to setString", e);
      }

      // send the message and then get it back
      TestUtil.logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      TestUtil.logMsg("Writing a boolean ... ");
      try {
        messageReceived.setBoolean("pass", pass);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setBoolean");
      } catch (MessageNotWriteableException e) {
        TestUtil.logMsg(
            "Got Expected MessageNotWriteableException with setBoolean");
      }

      TestUtil.logMsg("Writing a byte ... ");
      try {
        messageReceived.setByte("bValue", bValue);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setByte");
      } catch (MessageNotWriteableException e) {
        TestUtil
            .logMsg("Got Expected MessageNotWriteableException with setByte");
      }

      TestUtil.logMsg("Writing a short ... ");
      try {
        messageReceived.setShort("sValue", sValue);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setShort");
      } catch (MessageNotWriteableException e) {
        TestUtil
            .logMsg("Got Expected MessageNotWriteableException with setShort");
      }

      TestUtil.logMsg("Writing a char ... ");
      try {
        messageReceived.setChar("cValue", cValue);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setChar");
      } catch (MessageNotWriteableException e) {
        TestUtil
            .logMsg("Got Expected MessageNotWriteableException with setChar");
      }

      TestUtil.logMsg("Writing a int ... ");
      try {
        messageReceived.setInt("iValue", iValue);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setInt");
      } catch (MessageNotWriteableException e) {
        TestUtil
            .logMsg("Got Expected MessageNotWriteableException with setInt");
      }

      TestUtil.logMsg("Writing a long ... ");
      try {
        messageReceived.setLong("lValue", lValue);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setLong");
      } catch (MessageNotWriteableException e) {
        TestUtil
            .logMsg("Got Expected MessageNotWriteableException with setLong");
      }

      TestUtil.logMsg("Writing a float ... ");
      try {
        messageReceived.setFloat("fValue", fValue);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setFloat");
      } catch (MessageNotWriteableException e) {
        TestUtil
            .logMsg("Got Expected MessageNotWriteableException with setFloat");
      }

      TestUtil.logMsg("Writing a double ... ");
      try {
        messageReceived.setDouble("dValue", dValue);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setDouble");
      } catch (MessageNotWriteableException e) {
        TestUtil
            .logMsg("Got Expected MessageNotWriteableException with setDouble");
      }

      TestUtil.logMsg("Writing a bytes... ");
      try {
        messageReceived.setBytes("bbValue", bbValue);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setBytes");
      } catch (MessageNotWriteableException e) {
        TestUtil
            .logMsg("Got Expected MessageNotWriteableException with setBytes");
      }

      TestUtil.logMsg("Writing a bytes... ");
      try {
        messageReceived.setBytes("bbValue", bbValue, 0, 1);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setBytes");
      } catch (MessageNotWriteableException e) {
        TestUtil
            .logMsg("Got Expected MessageNotWriteableException with setBytes");
      }

      TestUtil.logMsg("Writing a string ... ");
      try {
        messageReceived.setString("ssValue", ssValue);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setString");
      } catch (MessageNotWriteableException e) {
        TestUtil
            .logMsg("Got Expected MessageNotWriteableException with setString");
      }

      TestUtil.logMsg("Writing a object ... ");
      try {
        messageReceived.setObject("oValue", new Integer(iValue));
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setObject");
      } catch (MessageNotWriteableException e) {
        TestUtil
            .logMsg("Got Expected MessageNotWriteableException with setObject");
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("mapMessageTNotWriteable", e);
    }
  }

  /*
   * @testName: mapMessageTIllegalarg
   *
   * @assertion_ids: JMS:JAVADOC:805; JMS:JAVADOC:807; JMS:JAVADOC:809;
   * JMS:JAVADOC:811; JMS:JAVADOC:813; JMS:JAVADOC:815; JMS:JAVADOC:817;
   * JMS:JAVADOC:819; JMS:JAVADOC:821; JMS:JAVADOC:823; JMS:JAVADOC:825;
   * JMS:JAVADOC:827;
   *
   * @test_Strategy: Create a MapMessage. Write to the message using each type
   * of set method and as an object with null String as name. Verify that
   * IllegalArgumentException thrown.
   */

  public void mapMessageTIllegalarg() throws Fault {
    try {
      MapMessage messageSent = null;
      boolean pass = true;
      byte bValue = 127;
      short sValue = 32767;
      char cValue = '\uFFFF';
      int iValue = 2147483647;
      long lValue = 9223372036854775807L;
      float fValue = 0.0f;
      double dValue = -0.0;
      String ssValue = "abc";
      byte[] bbValue = { 0, 88, 127 };

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      messageSent = tool.getDefaultTopicSession().createMapMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "mapMessageTIllegalarg");

      // -----------------------------------------------------------------------------

      TestUtil.logMsg("Writing a boolean ... ");
      try {
        messageSent.setBoolean("", pass);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setBoolean");
      } catch (IllegalArgumentException e) {
        TestUtil
            .logMsg("Got Expected IllegalArgumentException with setBoolean");
      }

      TestUtil.logMsg("Writing a byte ... ");
      try {
        messageSent.setByte("", bValue);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setByte");
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("Got Expected IllegalArgumentException with setByte");
      }

      TestUtil.logMsg("Writing a short ... ");
      try {
        messageSent.setShort("", sValue);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setShort");
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("Got Expected IllegalArgumentException with setShort");
      }

      TestUtil.logMsg("Writing a char ... ");
      try {
        messageSent.setChar("", cValue);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setChar");
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("Got Expected IllegalArgumentException with setChar");
      }

      TestUtil.logMsg("Writing a int ... ");
      try {
        messageSent.setInt("", iValue);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setInt");
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("Got Expected IllegalArgumentException with setInt");
      }

      TestUtil.logMsg("Writing a long ... ");
      try {
        messageSent.setLong("", lValue);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setLong");
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("Got Expected IllegalArgumentException with setLong");
      }

      TestUtil.logMsg("Writing a float ... ");
      try {
        messageSent.setFloat("", fValue);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setFloat");
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("Got Expected IllegalArgumentException with setFloat");
      }
      TestUtil.logMsg("Writing a double ... ");
      try {
        messageSent.setDouble("", dValue);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setDouble");
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("Got Expected IllegalArgumentException with setDouble");
      }

      TestUtil.logMsg("Writing a bytes... ");
      try {
        messageSent.setBytes("", bbValue);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setBytes");
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("Got Expected IllegalArgumentException with setBytes");
      }

      TestUtil.logMsg("Writing a bytes... ");
      try {
        messageSent.setBytes("", bbValue, 0, 1);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setBytes");
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("Got Expected IllegalArgumentException with setBytes");
      }

      TestUtil.logMsg("Writing a string ... ");
      try {
        messageSent.setString("", ssValue);
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setString");
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("Got Expected IllegalArgumentException with setString");
      }

      TestUtil.logMsg("Writing a object ... ");
      try {
        messageSent.setObject("", new Integer(iValue));
        TestUtil.logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to setObject");
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("Got Expected IllegalArgumentException with setObject");
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("mapMessageTIllegalarg", e);
    }
  }

}
