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

package com.sun.ts.tests.jms.core.streamMsgTopic;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.*;
import com.sun.javatest.Status;

public class StreamMsgTopicTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.streamMsgTopic.StreamMsgTopicTests";

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
    StreamMsgTopicTests theTests = new StreamMsgTopicTests();
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
      logErr("An error occurred while cleaning: ");
      throw new Fault("Cleanup failed!", e);
    }
  }

  /* Tests */

  /*
   * @testName: streamMessageConversionTopicTestsBoolean
   * 
   * @assertion_ids: JMS:SPEC:75.1; JMS:SPEC:75.2; JMS:JAVADOC:219;
   * JMS:JAVADOC:150; JMS:JAVADOC:128; JMS:JAVADOC:144; JMS:JAVADOC:723;
   * JMS:JAVADOC:726; JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:735;
   * JMS:JAVADOC:738; JMS:JAVADOC:741; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Create a StreamMessage -. use StreamMessage method
   * writeBoolean to write a boolean to the message. Verify the proper
   * conversion support as in 3.11.3
   */

  public void streamMessageConversionTopicTestsBoolean() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
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

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createStreamMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionTopicTestsBoolean");

      // -----------------------------------------------------------------------------
      logTrace(
          "Verify conversion support for boolean primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.writeBoolean(abool);

      // send the message and then get it back
      logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // now test conversions for boolean
      // -----------------------------------------------
      // boolean to boolean - valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readBoolean to read a boolean");
      try {
        if (messageReceived.readBoolean() == abool) {
          logTrace("Pass: boolean to boolean - valid");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // boolean to string valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readString to read a boolean");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readString()
            .equals((Boolean.valueOf(abool)).toString())) {
          logTrace("Pass: boolean to string - valid");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // boolean to byte[] invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg(
          "Use readBytes[] to read a boolean - expect MessageFormatException");
      int nCount = 0;

      try {

        // position to beginning of stream message.
        messageReceived.reset();
        nCount = messageReceived.readBytes(bValues);
        logErr("Fail: MessageFormatException was not thrown");
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }
      logMsg("Count returned from readBytes is : " + nCount);

      // -----------------------------------------------
      // boolean to byte invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readByte to read a boolean - expect MessageFormatException");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        messageReceived.readByte();
        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // boolean to short invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readShort to read a boolean - expect MessageFormatException");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        messageReceived.readShort();
        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // boolean to char invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readChar to read a boolean - expect MessageFormatException");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        messageReceived.readChar();
        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // boolean to int invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readInt to read a boolean - expect MessageFormatException");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        messageReceived.readInt();
        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // boolean to long invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readLong to read a boolean - expect MessageFormatException");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        messageReceived.readLong();
        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // boolean to float invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readFloat to read a boolean - expect MessageFormatException");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        messageReceived.readFloat();
        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // boolean to double invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg(
          "Use readDouble to read a boolean - expect MessageFormatException");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        messageReceived.readDouble();
        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("streamMessageConversionTopicTestsBoolean", e);
    }
  }

  /*
   * @testName: streamMessageConversionTopicTestsByte
   * 
   * @assertion_ids: JMS:SPEC:75.3; JMS:SPEC:75.4; JMS:JAVADOC:152;
   * JMS:JAVADOC:130; JMS:JAVADOC:132; JMS:JAVADOC:136; JMS:JAVADOC:138;
   * JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:729; JMS:JAVADOC:738;
   * JMS:JAVADOC:741; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Create a StreamMessage -. use StreamMessage method
   * writeByte to write a byte. Verify the proper conversion support as in
   * 3.11.3
   * 
   */

  public void streamMessageConversionTopicTestsByte() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
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

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createStreamMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionTopicTestsByte");

      // -----------------------------------------------------------------------------
      logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.writeByte(bValue);

      // send the message and then get it back
      logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // now test conversions for byte
      // -----------------------------------------------
      // byte to boolean - invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readByte to read a boolean - this is not valid");
      try {
        messageReceived.reset();
        boolean b = messageReceived.readBoolean();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // byte to string valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readString to read a byte");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readString().equals(Byte.toString(bValue))) {
          logTrace("Pass: byte to string - valid");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte to byte[] invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readBytes[] to read a byte - expect MessageFormatException");
      int nCount = 0;

      try {

        // position to beginning of stream message.
        messageReceived.reset();
        nCount = messageReceived.readBytes(bValues);
        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }
      logMsg("Count returned from readBytes is : " + nCount);

      // -----------------------------------------------
      // byte to byte valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readByte to read a byte");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readByte() == bValue) {
          logTrace("Pass: byte to byte - valid");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte to short valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readShort to read a byte");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readShort() == bValue) {
          logTrace("Pass: byte to short - valid");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte to char invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readChar to read a boolean - this is not valid");
      try {
        messageReceived.reset();
        char c = messageReceived.readChar();

        pass = false;
        logMsg("Fail: MessageFormatException was not thrown");
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // byte to int valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readInt to read a byte");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readInt() == bValue) {
          logTrace("Pass: byte to int - valid");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte to long valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readLong to read a byte");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readLong() == bValue) {
          logTrace("Pass: byte to long - valid");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // byte to float invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readFloat to read a boolean - this is not valid");
      try {
        messageReceived.reset();
        float f = messageReceived.readFloat();

        logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // byte to double invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readDouble to read a boolean - this is not valid");
      try {
        messageReceived.reset();
        double d = messageReceived.readDouble();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("streamMessageConversionTopicTestsByte", e);
    }
  }

  /*
   * @testName: streamMessageConversionTopicTestsShort
   * 
   * @assertion_ids: JMS:SPEC:75.5; JMS:SPEC:75.6; JMS:JAVADOC:154;
   * JMS:JAVADOC:132; JMS:JAVADOC:136; JMS:JAVADOC:138; JMS:JAVADOC:144;
   * JMS:JAVADOC:720; JMS:JAVADOC:723; JMS:JAVADOC:729; JMS:JAVADOC:738;
   * JMS:JAVADOC:741; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Create a StreamMessage -. use StreamMessage method
   * writeShort to write a short. Verify the proper conversion support as in
   * 3.11.3
   * 
   */

  public void streamMessageConversionTopicTestsShort() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
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

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createStreamMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionTopicTestsShort");

      // -----------------------------------------------------------------------------
      logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.writeShort(sValue);

      // send the message and then get it back
      logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // now test conversions for byte
      // -----------------------------------------------
      // short to boolean - invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readBoolean to read a short - this is not valid");
      try {
        messageReceived.reset();
        boolean b = messageReceived.readBoolean();

        logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // short to string valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readString to read a short");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readString().equals(Short.toString(sValue))) {
          logTrace("Pass: short to string - valid");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // short to byte[] invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readBytes[] to read a short - expect MessageFormatException");
      int nCount = 0;

      try {

        // position to beginning of stream message.
        messageReceived.reset();
        nCount = messageReceived.readBytes(bValues);
        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }
      logMsg("Count returned from readBytes is : " + nCount);

      // -----------------------------------------------
      // short to byte invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readByte to read a short - this is not valid");
      try {
        messageReceived.reset();
        byte b = messageReceived.readByte();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // short to short valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readShort to read a short");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readShort() == sValue) {
          logTrace("Pass: short to short - valid");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // short to char invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readChar to read a short - this is not valid");
      try {
        messageReceived.reset();
        char c = messageReceived.readChar();

        logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // short to int valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readInt to read a byte");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readInt() == sValue) {
          logTrace("Pass: short to int - valid");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // short to long valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readLong to read a short");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readLong() == sValue) {
          logTrace("Pass: short to long - valid");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // short to float invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readFloat to read a short - this is not valid");
      try {
        messageReceived.reset();
        float f = messageReceived.readFloat();

        logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // short to double invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readDouble to read a short - this is not valid");
      try {
        messageReceived.reset();
        double d = messageReceived.readDouble();

        logMsg("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("streamMessageConversionTopicTestsShort", e);
    }
  }

  /*
   * @testName: streamMessageConversionTopicTestsInt
   * 
   * @assertion_ids: JMS:SPEC:75.9; JMS:SPEC:75.10; JMS:JAVADOC:158;
   * JMS:JAVADOC:136; JMS:JAVADOC:138; JMS:JAVADOC:144; JMS:JAVADOC:720;
   * JMS:JAVADOC:723; JMS:JAVADOC:725; JMS:JAVADOC:729; JMS:JAVADOC:738;
   * JMS:JAVADOC:741; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Create a StreamMessage -. use StreamMessage method writeInt
   * to write an int. Verify the proper conversion support as in 3.11.3
   * 
   */

  public void streamMessageConversionTopicTestsInt() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
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

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createStreamMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionTopicTestsInt");

      // -----------------------------------------------------------------------------
      logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.writeInt(iValue);

      // send the message and then get it back
      logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // now test conversions for byte
      // -----------------------------------------------
      // int to boolean - invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readBoolean to read an int - this is not valid");
      try {
        messageReceived.reset();
        boolean b = messageReceived.readBoolean();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // int to string valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readString to read an int");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readString().equals(Integer.toString(iValue))) {
          logTrace("Pass: int to string - valid");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // int to byte[] invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readBytes[] to read an int - expect MessageFormatException");
      int nCount = 0;

      try {

        // position to beginning of stream message.
        messageReceived.reset();
        nCount = messageReceived.readBytes(bValues);
        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }
      logMsg("Count returned from readBytes is : " + nCount);

      // -----------------------------------------------
      // int to byte invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readByte to read an int - this is not valid");
      try {
        messageReceived.reset();
        byte b = messageReceived.readByte();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // int to short invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readShort to read an int");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        short s = messageReceived.readShort();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // int to char invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readChar to read an int - this is not valid");
      try {
        messageReceived.reset();
        char c = messageReceived.readChar();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // int to int valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readInt to read an int");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readInt() == iValue) {
          logTrace("Pass: int to int - valid");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // int to long valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readLong to read an int");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readLong() == iValue) {
          logTrace("Pass: int to long - valid");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // int to float invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readFloat to read an int - this is not valid");
      try {
        messageReceived.reset();
        float f = messageReceived.readFloat();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // int to double invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readDouble to read an int - this is not valid");
      try {
        messageReceived.reset();
        double d = messageReceived.readDouble();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("streamMessageConversionTopicTestsInt", e);
    }
  }

  /*
   * @testName: streamMessageConversionTopicTestsLong
   * 
   * @assertion_ids: JMS:SPEC:75.11; JMS:SPEC:75.12; JMS:JAVADOC:160;
   * JMS:JAVADOC:138; JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:723;
   * JMS:JAVADOC:725; JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:738;
   * JMS:JAVADOC:741; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Create a StreamMessage -. use StreamMessage method
   * writeLong to write a long. Verify the proper conversion support as in
   * 3.11.3
   * 
   */

  public void streamMessageConversionTopicTestsLong() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
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

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createStreamMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionTopicTestsLong");

      // -----------------------------------------------------------------------------
      logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.writeLong(lValue);

      // send the message and then get it back
      logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // now test conversions for byte
      // -----------------------------------------------
      // long to boolean - invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readBoolean to read a long - this is not valid");
      try {
        messageReceived.reset();
        boolean b = messageReceived.readBoolean();

        logErr("Fail: MessageFormatException was not thrown");
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // long to string valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readString to read a long");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readString().equals(Long.toString(lValue))) {
          logTrace("Pass: long to string - valid");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // long to byte[] invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readBytes[] to read  a long - expect MessageFormatException");
      int nCount = 0;

      try {

        // position to beginning of stream message.
        messageReceived.reset();
        nCount = messageReceived.readBytes(bValues);
        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }
      logMsg("Count returned from readBytes is : " + nCount);

      // -----------------------------------------------
      // long to byte invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readByte to read an long - this is not valid");
      try {
        messageReceived.reset();
        byte b = messageReceived.readByte();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // long to short invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readShort to read a long");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        short s = messageReceived.readShort();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // long to char invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readChar to read a long - this is not valid");
      try {
        messageReceived.reset();
        char c = messageReceived.readChar();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // long to int invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readInt to read a long");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        int i = messageReceived.readInt();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // long to long valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readLong to read a long");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readLong() == lValue) {
          logTrace("Pass: int to long - valid");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // long to float invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readFloat to read a long - this is not valid");
      try {
        messageReceived.reset();
        float f = messageReceived.readFloat();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // long to double invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readDouble to read an long - this is not valid");
      try {
        messageReceived.reset();
        double d = messageReceived.readDouble();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("streamMessageConversionTopicTestsLong", e);
    }
  }

  /*
   * @testName: streamMessageConversionTopicTestsFloat
   * 
   * @assertion_ids: JMS:SPEC:75.13; JMS:SPEC:75.14; JMS:JAVADOC:162;
   * JMS:JAVADOC:140; JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:723;
   * JMS:JAVADOC:725; JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:735;
   * JMS:JAVADOC:741; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Create a StreamMessage -. use StreamMessage method
   * writeFloat to write a float. Verify the proper conversion support as in
   * 3.11.3
   * 
   */

  public void streamMessageConversionTopicTestsFloat() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
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

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createStreamMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionTopicTestsFloat");

      // -----------------------------------------------------------------------------
      logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.writeFloat(fValue);

      // send the message and then get it back
      logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // now test conversions for byte
      // -----------------------------------------------
      // float to boolean - invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readBoolean to read a float  ");
      try {
        messageReceived.reset();
        boolean b = messageReceived.readBoolean();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // float to string valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readString to read a float");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readString().equals(Float.toString(fValue))) {
          logTrace("Pass: float to string - valid");
        } else {
          logErr("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // float to byte[] invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readBytes[] to read  a float ");
      int nCount = 0;

      try {

        // position to beginning of stream message.
        messageReceived.reset();
        nCount = messageReceived.readBytes(bValues);
        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }
      logMsg("Count returned from readBytes is : " + nCount);

      // -----------------------------------------------
      // float to byte invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readByte to read a float  ");
      try {
        messageReceived.reset();
        byte b = messageReceived.readByte();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // float to short invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readShort to read a float");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        short s = messageReceived.readShort();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // float to char invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readChar to read a long  ");
      try {
        messageReceived.reset();
        char c = messageReceived.readChar();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // float to int invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readInt to read a float");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        int i = messageReceived.readInt();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // float to long invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readLong to read a long");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        long l = messageReceived.readLong();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // float to float valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readFloat to read a float  ");
      try {
        messageReceived.reset();
        if (messageReceived.readFloat() == fValue) {
          logTrace("Pass: float to float - valid");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // float to double valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readDouble to read an float  ");
      try {
        messageReceived.reset();
        if (messageReceived.readDouble() == fValue) {
          logTrace("Pass: float to double - valid");
        } else {
          logMsg("Fail: wrong value returned");
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
      throw new Fault("streamMessageConversionTopicTestsFloat", e);
    }
  }

  /*
   * @testName: streamMessageConversionTopicTestsDouble
   * 
   * @assertion_ids: JMS:SPEC:75.15; JMS:SPEC:75.16; JMS:JAVADOC:164;
   * JMS:JAVADOC:142; JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:723;
   * JMS:JAVADOC:725; JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:735;
   * JMS:JAVADOC:738; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Create a StreamMessage -. use StreamMessage method
   * writeDouble to write a double. Verify the proper conversion support as in
   * 3.11.3
   * 
   */

  public void streamMessageConversionTopicTestsDouble() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
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

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createStreamMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionTopicTestsDouble");

      // -----------------------------------------------------------------------------
      logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.writeDouble(dValue);

      // send the message and then get it back
      logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // now test conversions for byte
      // -----------------------------------------------
      // double to boolean - invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readBoolean to read a double  ");
      try {
        messageReceived.reset();
        boolean b = messageReceived.readBoolean();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // double to string valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readString to read a double");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readString().equals(Double.toString(dValue))) {
          logTrace("Pass: double to string");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // double to byte[] invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readBytes[] to read  a double ");
      int nCount = 0;

      try {

        // position to beginning of stream message.
        messageReceived.reset();
        nCount = messageReceived.readBytes(bValues);
        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }
      logMsg("Count returned from readBytes is : " + nCount);

      // -----------------------------------------------
      // double to byte invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readByte to read a double  ");
      try {
        messageReceived.reset();
        byte b = messageReceived.readByte();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // double to short invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readShort to read a double");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        short s = messageReceived.readShort();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // double to char invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readChar to read a double  ");
      try {
        messageReceived.reset();
        char c = messageReceived.readChar();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // double to int invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readInt to read a double");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        int i = messageReceived.readInt();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // double to long invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readLong to read a double");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        long l = messageReceived.readLong();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // double to float invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readFloat to read a double  ");
      try {
        messageReceived.reset();
        float f = messageReceived.readFloat();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // double to double valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readDouble to read a double  ");
      try {
        messageReceived.reset();
        if (messageReceived.readDouble() == dValue) {
          logTrace("Pass: double to double ");
        } else {
          logMsg("Fail: wrong value returned");
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
      throw new Fault("streamMessageConversionTopicTestsDouble", e);
    }
  }

  /*
   * @testName: streamMessageConversionTopicTestsString
   * 
   * @assertion_ids: JMS:SPEC:75.17; JMS:SPEC:75.18; JMS:SPEC:77;
   * JMS:JAVADOC:166; JMS:JAVADOC:128; JMS:JAVADOC:130; JMS:JAVADOC:132;
   * JMS:JAVADOC:136; JMS:JAVADOC:138; JMS:JAVADOC:140; JMS:JAVADOC:142;
   * JMS:JAVADOC:144; JMS:JAVADOC:729; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Create a StreamMessage -. use StreamMessage method
   * writeString to write a string. Verify the proper conversion support as in
   * 3.11.3
   * 
   */

  public void streamMessageConversionTopicTestsString() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
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

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createStreamMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionTopicTestsString");

      // -----------------------------------------------------------------------------
      logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.writeString(myString);

      // send the message and then get it back
      logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // now test conversions for String
      // -----------------------------------------------
      // string to string valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readString to read a String");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readString().equals(myString)) {
          logTrace("Pass: string to string - valid");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // string to byte[] invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readBytes[] to read a String");
      int nCount = 0;

      try {

        // position to beginning of stream message.
        messageReceived.reset();
        nCount = messageReceived.readBytes(bValues);
        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }
      logMsg("Count returned from readBytes is : " + nCount);

      // -----------------------------------------------
      // String to byte valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readByte to read a String");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readByte() == Byte.parseByte(myString)) {
          logTrace("Pass: String to byte ");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // string to short valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readShort to read a string");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readShort() == Short.parseShort(myString)) {
          logTrace("Pass: String to short ");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // String to char invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readChar to read a String ");
      try {
        messageReceived.reset();
        char c = messageReceived.readChar();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // string to int valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readInt to read a String");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readInt() == Integer.parseInt(myString)) {
          logTrace("Pass: String to int ");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // string to long valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readLong to read a String");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readLong() == Long.parseLong(myString)) {
          logTrace("Pass: String to long ");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // String to float valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readFloat to read a String");
      try {
        messageReceived.reset();
        if (messageReceived.readFloat() == Float.parseFloat(myString)) {
          logTrace("Pass: String to float ");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // String to double valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readDouble to read a String");
      try {
        messageReceived.reset();
        if (messageReceived.readDouble() == Double.parseDouble(myString)) {
          logTrace("Pass: String to double ");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // String to boolean
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readBoolean to read a string ");
      try {
        messageReceived.clearBody();
        messageReceived.writeString("true");
        messageReceived.reset();
        if (messageReceived.readBoolean() == abool) {
          logTrace("Pass: String to boolean ");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // String to boolean
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readBoolean to read a string  that is !true ");
      try {
        messageReceived.reset();
        boolean b = messageReceived.readBoolean();

        if (b != true) {
          logErr("Fail: !true should return false");
          pass = false;
        } else {
          logTrace("Pass: !true returned false");
        }
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("streamMessageConversionTopicTestsString", e);
    }
  }

  /*
   * @testName: streamMessageConversionTopicTestsChar
   * 
   * @assertion_ids: JMS:SPEC:75.7; JMS:SPEC:75.8; JMS:JAVADOC:156;
   * JMS:JAVADOC:134; JMS:JAVADOC:144; JMS:JAVADOC:720; JMS:JAVADOC:723;
   * JMS:JAVADOC:726; JMS:JAVADOC:732; JMS:JAVADOC:735; JMS:JAVADOC:738;
   * JMS:JAVADOC:741; JMS:JAVADOC:747;
   * 
   * @test_Strategy: Create a StreamMessage -. use StreamMessage method
   * writeChar to write a char. Verify the proper conversion support as in
   * 3.11.3
   * 
   */

  public void streamMessageConversionTopicTestsChar() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
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

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createStreamMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionTopicTestsChar");

      // -----------------------------------------------------------------------------
      logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.writeChar(charValue);

      // send the message and then get it back
      logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // now test conversions for byte
      // -----------------------------------------------
      // char to boolean - invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readBoolean to read a char - this is not valid");
      try {
        messageReceived.reset();
        boolean b = messageReceived.readBoolean();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // char to string valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readString to read a char");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        String s = messageReceived.readString();

        logTrace("char returned for \"a\" is : " + s);
        if (s.equals("a")) {
          logTrace("Pass: char to string - valid");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // char to byte[] invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readBytes[] to read a char - expect MessageFormatException");
      int nCount = 0;

      try {

        // position to beginning of stream message.
        messageReceived.reset();
        nCount = messageReceived.readBytes(bValues);
        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }
      logMsg("Count returned from readBytes is : " + nCount);

      // -----------------------------------------------
      // char to byte invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readByte to read a char - this is not valid");
      try {
        messageReceived.reset();
        byte b = messageReceived.readByte();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // char to short invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readShort to read a char");
      try {
        messageReceived.reset();
        short s = messageReceived.readShort();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // char to char valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readChar to read a char ");
      try {
        messageReceived.reset();
        if (messageReceived.readChar() == 'a') {
          logTrace("Pass: char to char - valid");
        } else {
          logMsg("Fail: wrong value returned");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // char to int invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readInt to read a char ");
      try {
        messageReceived.reset();
        int i = messageReceived.readInt();

        logErr("Fail: MessageFormatException was not thrown");
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // char to long invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readLong to read a char");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        long l = messageReceived.readLong();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // char to float invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readFloat to read a char - this is not valid");
      try {
        messageReceived.reset();
        float f = messageReceived.readFloat();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // char to double invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readDouble to read a char - this is not valid");
      try {
        messageReceived.reset();
        double d = messageReceived.readDouble();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("streamMessageConversionTopicTestsChar", e);
    }
  }

  /*
   * @testName: streamMessageConversionTopicTestsBytes
   * 
   * @assertion_ids: JMS:SPEC:75.19; JMS:SPEC:75.20; JMS:JAVADOC:168;
   * JMS:JAVADOC:146; JMS:JAVADOC:720; JMS:JAVADOC:723; JMS:JAVADOC:725;
   * JMS:JAVADOC:729; JMS:JAVADOC:732; JMS:JAVADOC:735; JMS:JAVADOC:738;
   * JMS:JAVADOC:741; JMS:JAVADOC:744;
   * 
   * @test_Strategy: Create a StreamMessage -. use StreamMessage method
   * writeBytes to write a byte[] to the message. Verify the proper conversion
   * support as in 3.11.3
   */

  public void streamMessageConversionTopicTestsBytes() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
      byte bValue = 127;
      boolean abool = true;
      byte[] bValues = { 1, 2, 3 };
      byte[] bValues2 = { 0, 0, 0 };
      short sValue = 1;
      long lValue = 2;
      double dValue = 3;
      float fValue = 5;
      int iValue = 6;
      boolean pass = true;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createStreamMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionTopicTestsBytes");

      // -----------------------------------------------------------------------------
      logTrace(
          "Verify conversion support for byte[] primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.writeBytes(bValues);

      // send the message and then get it back
      logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // now test conversions for boolean
      // -----------------------------------------------
      // byte[] to byte[] - valid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readBytes[] to read a byte[] ");
      int nCount = 0;

      try {

        // position to beginning of stream message.
        messageReceived.reset();
        if (messageReceived.readBytes(bValues2) == 3) { // count should be 3.
          logTrace("Pass: byte[] to byte[] - valid");
        } else {
          logErr("Fail: count incorrect");
          pass = false;
        }
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // byte[] to boolean - invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readBoolean to read a byte[]");

      // position to beginning of stream message.
      messageReceived.reset();
      try {
        boolean b = messageReceived.readBoolean();

        logErr(
            "Fail: byte[] to boolean conversion should have thrown MessageFormatException");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // byte[] to string invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readString to read a byte[]");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        String s = messageReceived.readString();

        logErr(
            "Fail: byte[] to boolean conversion should have thrown MessageFormatException");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // byte[] to byte invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readByte to read a byte[] - expect MessageFormatException");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        byte b = messageReceived.readByte();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // byte[] to short invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readShort to read a byte[] - expect MessageFormatException");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        short s = messageReceived.readShort();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // byte[] to char invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readChar to read a byte[] - expect MessageFormatException");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        char c = messageReceived.readChar();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // byte[] to int invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readInt to read a byte[] - expect MessageFormatException");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        int i = messageReceived.readInt();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // byte[] to long invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readLong to read a byte[] - expect MessageFormatException");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        long l = messageReceived.readLong();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // byte[] to float invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readFloat to read a byte[] - expect MessageFormatException");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        float f = messageReceived.readFloat();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // byte[] to double invalid
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readDouble to read a byte[] - expect MessageFormatException");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        double d = messageReceived.readDouble();

        logErr("Fail: MessageFormatException was not thrown");
        pass = false;
      } catch (javax.jms.MessageFormatException e) {
        logTrace("Pass: MessageFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("streamMessageConversionTopicTestsBytes", e);
    }
  }

  /*
   * @testName: streamMessageConversionTopicTestsInvFormatString
   * 
   * @assertion_ids: JMS:SPEC:76; JMS:SPEC:81; JMS:JAVADOC:166; JMS:JAVADOC:130;
   * JMS:JAVADOC:132; JMS:JAVADOC:136; JMS:JAVADOC:138; JMS:JAVADOC:140;
   * JMS:JAVADOC:142; JMS:JAVADOC:144; JMS:JAVADOC:146;
   * 
   * @test_Strategy: Create a StreamMessage -. use StreamMessage method
   * writeString to write a text string of "mytest string". Verify
   * NumberFormatException is thrown Verify that the pointer was not incremented
   * by doing a read string
   * 
   */

  public void streamMessageConversionTopicTestsInvFormatString() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
      boolean pass = true;
      String myString = "mytest string";

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createStreamMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageConversionTopicTestsInvFormatString");

      // -----------------------------------------------------------------------------
      logTrace(
          "Verify conversion support for byte primitive type section 3.11.3");

      // -----------------------------------------------------------------------------
      messageSent.writeString(myString);

      // send the message and then get it back
      logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // -----------------------------------------------
      // String to byte
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readByte to read a String that is not valid ");
      try {
        byte b = messageReceived.readByte();

        logErr("Fail: java.lang.NumberFormatException expected");
        pass = false;
      } catch (java.lang.NumberFormatException e) {
        logTrace("Pass: NumberFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // pointer should not have moved
      // -----------------------------------------------
      logMsg("--");
      logMsg(
          "Verify that the data can be read as a string and pointer did not move");
      try {
        String s = messageReceived.readString();

        logTrace("message read: " + s);
        if (s.equals(myString)) {
          logTrace("Pass: able to read the string");
        } else {
          logMsg("Fail: string not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      // -----------------------------------------------
      // string to short
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readShort to read a string that is not valid ");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        short s = messageReceived.readShort();

        logErr("Fail: NumberFormatException was expected");
        pass = false;
      } catch (java.lang.NumberFormatException e) {
        logTrace("Pass: NumberFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // string to int
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readInt to read a String that is not valid ");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        int i = messageReceived.readInt();

        logErr("Fail: NumberFormatException was expected");
      } catch (java.lang.NumberFormatException e) {
        logTrace("Pass: NumberFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // string to long
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readLong to read a String that is not valid ");
      try {

        // position to beginning of stream message.
        messageReceived.reset();
        long l = messageReceived.readLong();

        logErr("Fail: NumberFormatException was expected");
        pass = false;
      } catch (java.lang.NumberFormatException e) {
        logTrace("Pass: NumberFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // String to float
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readFloat to read a String that is not valid ");
      try {
        messageReceived.reset();
        float f = messageReceived.readFloat();

        logErr("Fail: NumberFormatException was expected");
        pass = false;
      } catch (java.lang.NumberFormatException e) {
        logTrace("Pass: NumberFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }

      // -----------------------------------------------
      // String to double
      // -----------------------------------------------
      logMsg("--");
      logMsg("Use readDouble to read a String that is not valid ");
      try {
        messageReceived.reset();
        double d = messageReceived.readDouble();

        logMsg("Fail: NumberFormatException was expected");
        pass = false;
      } catch (java.lang.NumberFormatException e) {
        logTrace("Pass: NumberFormatException thrown as expected");
      } catch (Exception ee) {
        TestUtil.logErr("Error: Unexpected exception: ", ee);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("streamMessageConversionTopicTestsInvFormatString", e);
    }
  }

  /*
   * @testName: streamMessageTopicTestsFullMsg
   * 
   * @assertion_ids: JMS:SPEC:82; JMS:JAVADOC:150; JMS:JAVADOC:152;
   * JMS:JAVADOC:154; JMS:JAVADOC:156; JMS:JAVADOC:158; JMS:JAVADOC:160;
   * JMS:JAVADOC:162; JMS:JAVADOC:164; JMS:JAVADOC:166; JMS:JAVADOC:168;
   * JMS:JAVADOC:170; JMS:JAVADOC:172; JMS:JAVADOC:128; JMS:JAVADOC:130;
   * JMS:JAVADOC:132; JMS:JAVADOC:134; JMS:JAVADOC:136; JMS:JAVADOC:138;
   * JMS:JAVADOC:140; JMS:JAVADOC:142; JMS:JAVADOC:144; JMS:JAVADOC:146;
   * JMS:JAVADOC:148;
   * 
   * @test_Strategy: Create a StreamMessage -. write one of each primitive type.
   * Send the message. Verify the data received was as sent.
   * 
   */

  public void streamMessageTopicTestsFullMsg() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
      byte bValue = 127;
      boolean abool = false;
      byte[] bValues = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
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

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createStreamMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageTopicTestsFullMsg");

      // -----------------------------------------------------------------------------
      logTrace("");

      // -----------------------------------------------------------------------------
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
      messageSent.writeBytes(bValues2, 0, bValues.length);
      messageSent.writeObject(null);

      // send the message and then get it back
      logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      try {
        if (messageReceived.readBoolean() == abool) {
          logTrace("Pass: boolean returned ok");
        } else {
          logMsg("Fail: boolean not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      try {
        if (messageReceived.readByte() == bValue) {
          logTrace("Pass: Byte returned ok");
        } else {
          logMsg("Fail: Byte not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      try {
        int nCount;
        do {
          nCount = messageReceived.readBytes(bValuesReturned);
          logTrace("nCount is " + nCount);
          if (nCount != -1) {
            for (int i = 0; i < bValuesReturned.length; i++) {
              if (bValuesReturned[i] != bValues[i]) {
                logMsg("Fail: byte[] " + i + " is not valid");
                pass = false;
              } else {
                logTrace("PASS: byte[]" + i + " is valid");
              }
            }
          }
        } while (nCount >= bValuesReturned.length);
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
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
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      try {
        if (messageReceived.readDouble() == dValue) {
          logTrace("Pass: correct double");
        } else {
          logMsg("Fail: double not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      try {
        if (messageReceived.readFloat() == fValue) {
          logTrace("Pass: correct float");
        } else {
          logMsg("Fail: float not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      try {
        if (messageReceived.readInt() == iValue) {
          logTrace("Pass: correct int");
        } else {
          logMsg("Fail: int not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      try {
        if (messageReceived.readLong() == lValue) {
          logTrace("Pass: correct long");
        } else {
          logErr("Fail: long not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      try {
        if (messageReceived.readObject().equals(sTesting)) {
          logTrace("Pass: correct object");
        } else {
          logMsg("Fail: object not returned as expected");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      try {
        short tmp = messageReceived.readShort();
        if (tmp == sValue) {
          logTrace("Pass: correct short");
        } else {
          logErr("Fail: short not returned as expected");
          logErr("Expecting =" + sValue + ", got =" + tmp + ".");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }

      try {
        String tmp = messageReceived.readString();
        if (tmp.equals(myString)) {
          logTrace("Pass: correct string");
        } else {
          logErr("Fail: string not returned as expected");
          logErr("Expecting =" + myString + ", got =" + tmp + ".");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      try {
        int nCount;
        do {
          nCount = messageReceived.readBytes(bValuesReturned2);
          logTrace("nCount is " + nCount);
          if (nCount != -1) {
            for (int i = 0; i < bValuesReturned2.length; i++) {
              if (bValuesReturned2[i] != bValues2[i]) {
                logMsg("Fail: byte[] " + i + " is not valid");
                pass = false;
              } else {
                logTrace("PASS: byte[]" + i + " is valid");
              }
            }
          }
        } while (nCount >= bValuesReturned2.length);
      } catch (Exception e) {
        TestUtil.logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      try {
        if (messageReceived.readObject() == null) {
          logTrace("Pass: correct object");
        } else {
          logErr("Fail: object not returned as expected");
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
      throw new Fault("streamMessageTopicTestsFullMsg", e);
    }
  }

  /*
   * @testName: streamMessageTopicTestNull
   * 
   * @assertion_ids: JMS:SPEC:78; JMS:SPEC:86; JMS:JAVADOC:144; JMS:JAVADOC:172;
   * 
   * @test_Strategy: Create a StreamMessage Use writeString to write a null,
   * then use readString to read it back.
   */

  public void streamMessageTopicTestNull() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
      boolean pass = true;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, user, password, mode);
      tool.getDefaultTopicConnection().start();
      messageSent = tool.getDefaultTopicSession().createStreamMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageTopicTestNull");

      // -----------------------------------------------------------------------------
      logTrace("writeString(null) ");

      // -----------------------------------------------------------------------------
      try {
        messageSent.writeString(null);
      } catch (Exception e) {
        logMsg("Error: unexpected exception " + e.getClass().getName()
            + " was thrown");

        // It doesn't make sense to continue, throw an exception
        throw new Fault("Error: failed to write a null object with writeString",
            e);
      }

      // send the message and then get it back
      logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);

      // -----------------------------------------------
      //
      // -----------------------------------------------
      logMsg("  ");
      try {
        if (messageReceived.readObject() == null) {
          logTrace("Pass: Read a null");
        } else {
          logMsg("Fail: null value not returned");
          pass = false;
        }
      } catch (Exception e) {
        logErr("Error trying to read a null object");
        logErr("Error: unexpected exception: ", e);
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("streamMessageTopicTestNull", e);
    }
  }

  /*
   * @testName: streamMessageQNotWritable
   *
   * @assertion_ids: JMS:SPEC:73; JMS:JAVADOC:752; JMS:JAVADOC:753;
   * JMS:JAVADOC:754; JMS:JAVADOC:755; JMS:JAVADOC:756; JMS:JAVADOC:757;
   * JMS:JAVADOC:758; JMS:JAVADOC:759; JMS:JAVADOC:761; JMS:JAVADOC:762;
   * JMS:JAVADOC:764;
   *
   * @test_Strategy: Create a StreamMessage, send it to a Topic Receive it and
   * try to write to the received Message's body, MessageNotWritableException
   * should be thrown.
   */

  public void streamMessageQNotWritable() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
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
      messageSent = tool.getDefaultTopicSession().createStreamMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "streamMessageQNotWritable");

      // -----------------------------------------------------------------------------
      try {
        messageSent.writeString("Test Message for streamMessageQNotWritable");
      } catch (Exception e) {
        logMsg("Error: unexpected exception " + e.getClass().getName()
            + " was thrown");
        throw new Fault("Error: failed to writeString", e);
      }

      // send the message and then get it back
      logTrace("Sending message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber()
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

      logMsg("Writing a string ... ");
      try {
        messageReceived.writeString(ssValue);
        logErr("Shouldn't get here");
        throw new Fault("Error: test failed to be able to writeString");
      } catch (MessageNotWriteableException e) {
        logMsg("Got Expected MessageNotWriteableException with writeString");
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
      throw new Fault("streamMessageQNotWritable", e);
    }
  }

}
