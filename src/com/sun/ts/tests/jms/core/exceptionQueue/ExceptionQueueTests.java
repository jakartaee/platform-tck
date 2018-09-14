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
package com.sun.ts.tests.jms.core.exceptionQueue;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import javax.jms.*;
import java.io.*;
import java.util.Properties;
import java.util.ArrayList;
import com.sun.javatest.Status;

public class ExceptionQueueTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.exceptionQueue.ExceptionQueueTests";

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
    ExceptionQueueTests theTests = new ExceptionQueueTests();
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
        throw new Exception("'timeout' (milliseconds) in ts.jte must be > 0");
      }
      if (user == null) {
        throw new Exception("'user' in ts.jte must be null");
      }
      if (password == null) {
        throw new Exception("'password' in ts.jte must be null");
      }
      queues = new ArrayList(2);
      connections = new ArrayList(2);

      // get ready for new test
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
        tool.doClientQueueTestCleanup(connections, queues);
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logErr("An error occurred while cleaning");
      throw new Fault("Cleanup failed!", e);
    }
  }

  /* Tests */

  /*
   * @testName: xInvalidDestinationExceptionQTest
   * 
   * @assertion_ids: JMS:SPEC:174; JMS:JAVADOC:190; JMS:JAVADOC:192;
   * JMS:JAVADOC:184; JMS:JAVADOC:186; JMS:JAVADOC:188; JMS:JAVADOC:622;
   * JMS:JAVADOC:623; JMS:JAVADOC:618; JMS:JAVADOC:619; JMS:JAVADOC:621;
   *
   * @test_Strategy: pass an invalid Queue object to createBrowser(null)
   * createBrowser(null,selector) createReceiver(null)
   * createReceiver(null,selector) createSender(null)This null is valid
   * 
   */

  public void xInvalidDestinationExceptionQTest() throws Fault {
    boolean pass = true;
    QueueBrowser qBrowser = null;
    QueueSender qSender = null;
    QueueReceiver qReceiver = null;

    try {

      // create Queue Connection and QueueBrowser
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      TestUtil.logTrace("** Close default QueueReceiver **");
      tool.getDefaultQueueReceiver().close();
      tool.getDefaultQueueConnection().start();

      // create QueueBrowser
      TestUtil.logMsg("Test createBrowser(null)");
      Queue dummy = null;

      try {
        qBrowser = tool.getDefaultQueueSession().createBrowser(dummy);
        if (qBrowser != null)
          TestUtil.logTrace("qBrowser=" + qBrowser);
        TestUtil.logTrace("FAIL: expected InvalidDestinationException!");
        pass = false;
      } catch (Exception ee) {
        if (ee instanceof javax.jms.InvalidDestinationException) {
          logTrace("Pass: InvalidDestinationException thrown as expected");
        } else {
          TestUtil.logMsg("Error: unexpected error " + ee.getClass().getName()
              + " was thrown");
          pass = false;
        }
      }
      TestUtil.logMsg("Test createBrowser(null),selector");
      try {
        qBrowser = tool.getDefaultQueueSession().createBrowser(dummy, "TEST");
        if (qBrowser != null)
          TestUtil.logTrace("qBrowser=" + qBrowser);
        TestUtil.logTrace("FAIL: expected InvalidDestinationException!");
        pass = false;
      } catch (Exception ee) {
        if (ee instanceof javax.jms.InvalidDestinationException) {
          logTrace("Pass: InvalidDestinationException thrown as expected");
        } else {
          TestUtil.logMsg("Error: unexpected error " + ee.getClass().getName()
              + " was thrown");
          pass = false;
        }
      }
      TestUtil.logMsg("Test createReceiver(null)");
      try {
        qReceiver = tool.getDefaultQueueSession().createReceiver(dummy);
        if (qReceiver != null)
          TestUtil.logTrace("qReceiver=" + qReceiver);
        TestUtil.logTrace("FAIL: expected InvalidDestinationException!");
        pass = false;
      } catch (Exception ee) {
        if (ee instanceof javax.jms.InvalidDestinationException) {
          logTrace("Pass: InvalidDestinationException thrown as expected");
        } else {
          TestUtil.logMsg("Error: unexpected error " + ee.getClass().getName()
              + " was thrown");
          pass = false;
        }
      }
      TestUtil.logMsg("Test createReceiver(null,selector)");
      try {
        qReceiver = tool.getDefaultQueueSession().createReceiver(dummy, "TEST");
        if (qReceiver != null)
          TestUtil.logTrace("qReceiver=" + qReceiver);
        TestUtil.logTrace("FAIL: expected InvalidDestinationException!");
        pass = false;
      } catch (Exception ee) {
        if (ee instanceof javax.jms.InvalidDestinationException) {
          logTrace("Pass: InvalidDestinationException thrown as expected");
        } else {
          TestUtil.logMsg("Error: unexpected error " + ee.getClass().getName()
              + " was thrown");
          pass = false;
        }
      }
      TestUtil.logMsg("Test createSender(null) - null is valid here ");
      try {
        qSender = tool.getDefaultQueueSession().createSender(dummy);
        if (qSender != null)
          TestUtil.logTrace("qSender=" + qSender);
        TestUtil.logTrace("PASS: ");
      } catch (Exception ee) {
        TestUtil.printStackTrace(ee);
        TestUtil.logMsg("Error: unexpected error " + ee.getClass().getName()
            + " was thrown");
        pass = false;
      }
      if (!pass) {
        throw new Fault(
            "Error: failures occurred during xInvalidDestinationExceptionQTest tests");
      }
    } catch (Exception e) {
      throw new Fault("xInvalidDestinationExceptionQTest", e);
    }
  }

  /*
   * @testName: xMessageNotReadableExceptionQueueTest
   * 
   * @assertion_ids: JMS:SPEC:178; JMS:JAVADOC:680;
   * 
   * @test_Strategy: create a BytesMessage, read it.
   */

  public void xMessageNotReadableExceptionQueueTest() throws Fault {
    try {
      BytesMessage msg = null;

      // create Queue Connection
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();

      // Create a BytesMessage
      msg = tool.getDefaultQueueSession().createBytesMessage();
      msg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageNotReadableExceptionQueueTest");
      try {
        msg.readByte();
        TestUtil.logTrace("FAIL --- should not have gotten this far!");
        throw new Fault("Fail: Did not throw expected error!!!");
      } catch (MessageNotReadableException nr) {
        TestUtil.logTrace("Passed.\n");
        // TestUtil.printStackTrace(nr);
        TestUtil.logTrace("MessageNotReadableException occurred!");
        TestUtil.logTrace(" " + nr.getMessage());
      }
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.logTrace("Expected MessageNotReadableException did not occur!");
      throw new Fault("xMessageNotReadableExceptionQueueTest test failed", e);
    }
  }

  /*
   * @testName: xMessageNotWriteableExceptionQTestforTextMessage
   * 
   * @assertion_ids: JMS:SPEC:179; JMS:SPEC:70; JMS:JAVADOC:766;
   * 
   * @test_Strategy: When a client receives a text message it is in read-only
   * mode. Send a message and have the client attempt to write to it.
   */

  public void xMessageNotWriteableExceptionQTestforTextMessage() throws Fault {
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      byte bValue = 127;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageNotWriteableExceptionQTestforTextMessage");
      logTrace("Sending message");
      tool.getDefaultQueueSender().send(messageSent);
      logTrace("Receiving message");
      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("getMessage returned null!! - test did not run!");
      } else {
        logTrace("Got message - OK");
      }

      // Received message should be read-only - verify proper exception is
      // thrown
      try {
        messageReceived.setText("testing...");
      } catch (MessageNotWriteableException nr) {
        TestUtil.logTrace("Passed.\n");
        // TestUtil.printStackTrace(nr);
        TestUtil.logTrace("MessageNotWriteableException occurred!");
        TestUtil.logTrace(" " + nr.getMessage());
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("xMessageNotWriteableExceptionQTestforTextMessage");
    }
  }

  /*
   * @testName: xMessageNotWriteableExceptionQTestforBytesMessage
   * 
   * @assertion_ids: JMS:SPEC:73; JMS:SPEC:179; JMS:SPEC:70; JMS:JAVADOC:702;
   * 
   * @test_Strategy: When a client receives a Bytes message it is in read-only
   * mode. Send a message and have the client attempt to write to it.
   */

  public void xMessageNotWriteableExceptionQTestforBytesMessage() throws Fault {
    try {
      BytesMessage messageSent = null;
      BytesMessage messageReceived = null;
      byte bValue = 127;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultQueueSession().createBytesMessage();
      messageSent.writeByte(bValue);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageNotWriteableExceptionQTestforBytesMessage");
      logTrace("Sending message");
      tool.getDefaultQueueSender().send(messageSent);
      logTrace("Receiving message");
      messageReceived = (BytesMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("getMessage returned null!! - test did not run!");
      } else {
        logTrace("Got message - OK");
      }

      // Received message should be read-only - verify proper exception is
      // thrown
      try {
        messageReceived.writeByte(bValue);
      } catch (MessageNotWriteableException nr) {
        TestUtil.logTrace("Passed.\n");
        // TestUtil.printStackTrace(nr);
        TestUtil.logTrace("MessageNotWriteableException occurred!");
        TestUtil.logTrace(" " + nr.getMessage());
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("xMessageNotWriteableExceptionQTestforBytesMessage", e);
    }
  }

  /*
   * @testName: xMessageNotWriteableExceptionQTestforStreamMessage
   * 
   * @assertion_ids: JMS:SPEC:73; JMS:SPEC:179; JMS:JAVADOC:760;
   * 
   * @test_Strategy: When a client receives a Stream message it is in read-only
   * mode. Send a message and have the client attempt to write to it.
   */

  public void xMessageNotWriteableExceptionQTestforStreamMessage()
      throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
      byte bValue = 127;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultQueueSession().createStreamMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageNotWriteableExceptionQTestforStreamMessage");

      // Irene - what's this?
      // messageSent.writeByte(bValue);
      messageSent.writeString("Testing...");
      logTrace("Sending message");
      tool.getDefaultQueueSender().send(messageSent);
      logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("getMessage returned null!! - test did not run!");
      } else {
        logTrace("Got message - OK");
      }

      // Received message should be read-only - verify proper exception is
      // thrown
      try {
        messageReceived.writeString("Testing...");
      } catch (MessageNotWriteableException nr) {
        TestUtil.logTrace("Passed.\n");
        // TestUtil.printStackTrace(nr);
        TestUtil.logTrace("MessageNotWriteableException occurred!");
        TestUtil.logTrace(" " + nr.getMessage());
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("xMessageNotWriteableExceptionQTestforStreamMessage", e);
    }
  }

  /*
   * @testName: xMessageNotWriteableExceptionQTestforMapMessage
   * 
   * @assertion_ids: JMS:SPEC:73; JMS:SPEC:179; JMS:JAVADOC:822;
   * 
   * @test_Strategy: When a client receives a Map message it is in read-only
   * mode. Send a message and have the client attempt to write to it.
   */

  public void xMessageNotWriteableExceptionQTestforMapMessage() throws Fault {
    try {
      MapMessage messageSent = null;
      MapMessage messageReceived = null;
      byte bValue = 127;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultQueueSession().createMapMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageNotWriteableExceptionQTestforMapMessage");

      // messageSent.setByte("aByte",bValue);
      messageSent.setString("aString", "value");
      logTrace("Sending message");
      tool.getDefaultQueueSender().send(messageSent);
      logTrace("Receiving message");
      messageReceived = (MapMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("getMessage returned null!! - test did not run!");
      } else {
        logTrace("Got message - OK");
      }

      // Received message should be read-only - verify proper exception is
      // thrown
      try {

        // messageReceived.setByte("aByte",bValue);
        messageReceived.setString("aString", "value");
      } catch (MessageNotWriteableException nr) {
        TestUtil.logTrace("Passed.\n");
        // TestUtil.printStackTrace(nr);
        TestUtil.logTrace("MessageNotWriteableException occurred!");
        TestUtil.logTrace(" " + nr.getMessage());
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("xMessageNotWriteableExceptionQTestforMapMessage", e);
    }
  }

  /*
   * @testName: xNullPointerExceptionQueueTest
   * 
   * @assertion_ids: JMS:SPEC:86.1;
   * 
   * @test_Strategy: Create a bytes message. Attempt to write null to it. Verify
   * that a NullPointerException is thrown.
   * 
   */

  public void xNullPointerExceptionQueueTest() throws Fault {
    try {
      BytesMessage msg = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      msg = tool.getDefaultQueueSession().createBytesMessage();
      msg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xNullPointerExceptionQueueTest");
      msg.writeBytes(null);
    } catch (java.lang.NullPointerException nullp) {
      TestUtil.logTrace("Passed.\n");
      // TestUtil.printStackTrace(nullp);
      TestUtil.logTrace("NullPointerException occurred!");
      TestUtil.logTrace(" " + nullp.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.logTrace("Expected NullPointerException did not occur!");
      throw new Fault("xNullPointerExceptionQueueTest");
    }
  }

  /*
   * @testName: xMessageEOFExceptionQTestforBytesMessage
   * 
   * @assertion_ids: JMS:SPEC:176; JMS:JAVADOC:679;
   * 
   * @test_Strategy: Send a message to the queue with one byte. Retreive message
   * from queue and read two bytes.
   * 
   * 
   */

  public void xMessageEOFExceptionQTestforBytesMessage() throws Fault {
    try {
      BytesMessage messageSent = null;
      BytesMessage messageReceived = null;
      byte bValue = 127;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultQueueSession().createBytesMessage();
      messageSent.writeByte(bValue);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageEOFExceptionQTestforBytesMessage");
      logTrace("Sending message");
      tool.getDefaultQueueSender().send(messageSent);
      logTrace("Receiving message");
      messageReceived = (BytesMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("getMessage returned null!! - test did not run!");
      } else {
        logTrace("Got message - OK");
      }

      // Received message should contain one byte
      // reading 2 bytes should throw the excpected exception
      messageReceived.readByte();
      try {
        messageReceived.readByte();

        // Should not reach here !!
        TestUtil.logTrace("Failed:  expected MessageEOFException not thrown");
        throw new Fault("Fail: Did not throw expected error!!!");
      } catch (MessageEOFException end) {
        TestUtil.logTrace("Passed.\n");
        // TestUtil.printStackTrace(end);
        TestUtil.logTrace("MessageEOFException occurred!");
        TestUtil.logTrace(" " + end.getMessage());
      }
    } catch (Exception e) {
      throw new Fault("xMessageEOFExceptionQTestforBytesMessage", e);
    }
  }

  /*
   * @testName: xMessageEOFExceptionQTestforStreamMessage
   * 
   * @assertion_ids: JMS:SPEC:176; JMS:JAVADOC:722;
   * 
   * @test_Strategy: Send a message to the queue with one byte. Retreive message
   * from queue and read two bytes.
   * 
   * 
   */

  public void xMessageEOFExceptionQTestforStreamMessage() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
      byte bValue = 127;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultQueueSession().createStreamMessage();
      messageSent.writeByte(bValue);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageEOFExceptionQTestforStreamMessage");
      logTrace("Sending message");
      tool.getDefaultQueueSender().send(messageSent);
      logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("getMessage returned null!! - test did not run!");
      } else {
        logTrace("Got message - OK");
      }

      // Received message should contain one byte
      // reading 2 bytes should throw the excpected exception
      messageReceived.readByte();
      try {
        messageReceived.readByte();

        // Should not reach here !!
        TestUtil.logTrace("Failed:  expected MessageEOFException not thrown");
        throw new Fault("Fail: Did not throw expected error!!!");
      } catch (MessageEOFException end) {
        TestUtil.logTrace("Passed.\n");
        // TestUtil.printStackTrace(end);
        TestUtil.logTrace("MessageEOFException occurred!");
        TestUtil.logTrace(" " + end.getMessage());
      }
    } catch (Exception e) {
      throw new Fault("xMessageEOFExceptionQTestforStreamMessage", e);
    }
  }

  /*
   * @testName: xMessageFormatExceptionQTestforBytesMessage
   * 
   * @assertion_ids: JMS:SPEC:177;
   * 
   * @test_Strategy: Call writeObject with a q session object
   */

  public void xMessageFormatExceptionQTestforBytesMessage() throws Fault {
    try {
      BytesMessage messageSent = null;
      BytesMessage messageReceived = null;
      byte bValue = 127;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultQueueSession().createBytesMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageFormatExceptionQTestforBytesMessage");
      logTrace("try to write an invalid object");
      try {
        messageSent.writeObject(tool.getDefaultQueueSession());

        // Should not reach here !!
        TestUtil.logTrace("Failed:  expected MessageEOFException not thrown");
        throw new Fault("Fail: Did not throw expected error!!!");
      } catch (MessageFormatException fe) {
        TestUtil.logTrace("Passed.\n");
        // TestUtil.printStackTrace(fe);
        TestUtil.logTrace("MessageFormatException occurred!");
        TestUtil.logTrace(" " + fe.getMessage());
      }
    } catch (Exception e) {
      throw new Fault("xMessageFormatExceptionQTestforBytesMessage", e);
    }
  }

  /*
   * @testName: xMessageFormatExceptionQTestforStreamMessage
   * 
   * @assertion_ids: JMS:SPEC:177; JMS:JAVADOC:744;
   * 
   * @test_Strategy: Write a byte array, read it as a string.
   */

  public void xMessageFormatExceptionQTestforStreamMessage() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
      byte[] bValues = { 127, 0, 3 };

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      logTrace("Creating 1 message");
      messageSent = tool.getDefaultQueueSession().createStreamMessage();
      messageSent.writeBytes(bValues);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageFormatExceptionQTestforStreamMessage");
      logTrace("Sending message");
      tool.getDefaultQueueSender().send(messageSent);
      logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("getMessage returned null!! - test did not run!");
      } else {
        logTrace("Got message - OK");
      }

      // Received message should contain a byte, read a string
      try {
        messageReceived.readString();

        // Should not reach here !!
        TestUtil
            .logTrace("Failed:  expected MessageFormatException not thrown");
        throw new Fault("Fail: Did not throw expected error!!!");
      } catch (MessageFormatException fe) {
        TestUtil.logMsg("Passed.\n");
        TestUtil.logTrace("MessageFormatException occurred!");
        TestUtil.logTrace(" " + fe.getMessage());
      }
    } catch (Exception e) {
      throw new Fault("xMessageFormatExceptionQTestforStreamMessage", e);
    }
  }

  /*
   * @testName: xInvalidSelectorExceptionQueueTest
   * 
   * @assertion_ids: JMS:SPEC:69; JMS:SPEC:175; JMS:JAVADOC:624;
   * JMS:JAVADOC:620;
   * 
   * @test_Strategy: call createBrowser with an invalid selector string call
   * createReceiver with an invalid selector string
   */

  public void xInvalidSelectorExceptionQueueTest() throws Fault {
    try {
      QueueBrowser qBrowser = null;
      QueueReceiver qReceiver = null;
      boolean pass = true;

      // close default QueueReceiver
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      TestUtil.logTrace("** Close default QueueReceiver **");
      tool.getDefaultQueueReceiver().close();
      tool.getDefaultQueueConnection().start();

      // send message to Queue
      TestUtil.logTrace("Send message to Queue. Text = \"message 1\"");
      TextMessage msg1 = tool.getDefaultQueueSession().createTextMessage();

      msg1.setText("message 1");
      msg1.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xInvalidSelectorExceptionQueueTest");
      tool.getDefaultQueueSender().send(msg1);

      try {
        // create QueueBrowser
        TestUtil.logTrace("call createBrowser with incorrect selector.");
        qBrowser = tool.getDefaultQueueSession()
            .createBrowser(tool.getDefaultQueue(), "=TEST 'test'");
        TestUtil.logErr(
            "Error --- createBrowser didn't throw expected InvalidSelectorException!");
        pass = false;
      } catch (InvalidSelectorException es) {
        TestUtil
            .logTrace("createBrowser threw expected InvalidSelectorException!");
      } catch (Exception e) {
        pass = false;
        TestUtil.logErr("Error -- Incorrect Exception thrown by createBrowser.",
            e);
      } finally {
        if (qBrowser != null) {
          try {
            qBrowser.close();
          } catch (Exception ee) {
            TestUtil.logErr("Error -- failed to close qBrowser.", ee);
          }
        }
      }

      try {
        // create QueueReceiver
        TestUtil.logTrace("call createReceiver with incorrect selector.");
        qReceiver = tool.getDefaultQueueSession()
            .createReceiver(tool.getDefaultQueue(), "=TEST 'test'");
        if (qReceiver != null)
          TestUtil.logTrace("qReceiver=" + qReceiver);
        TestUtil.logErr(
            "Error --- createReceiver didn't throw expected InvalidSelectorException!");
        pass = false;
      } catch (InvalidSelectorException es) {
        TestUtil
            .logMsg("createReceiver threw expected InvalidSelectorException!");
      } catch (Exception e) {
        pass = false;
        TestUtil.logErr(
            "Error -- Incorrect Exception thrown by createReceiver.", e);
      } finally {
        if (qReceiver != null) {
          try {
            qReceiver.close();
          } catch (Exception ee) {
            TestUtil.logErr("Error -- failed to close qReceiver.", ee);
          }
        }
      }

      if (pass != true)
        throw new Fault("xInvalidSelectorExceptionQueueTest failed!!");

    } catch (Exception e) {
      throw new Fault("xInvalidSelectorExceptionQueueTest: ", e);
    }

  }

  /*
   * @testName: xIllegalStateExceptionQueueTest
   * 
   * @assertion_ids: JMS:SPEC:171; JMS:JAVADOC:634;
   * 
   * @test_Strategy: Call session.commit() when there is no transaction to be
   * committed. Verify that the proper exception is thrown.
   */

  public void xIllegalStateExceptionQueueTest() throws Fault {
    boolean passed = false;

    try {

      // create Queue Connection
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      TestUtil.logMsg("Calling session.commit(), an illegal operation.");
      try {
        tool.getDefaultQueueSession().commit();
      } catch (javax.jms.IllegalStateException iStateE) {
        passed = true;
        TestUtil.logMsg("Received javax.jms.IllegalStateException -- GOOD");
        TestUtil.logTrace("Exception message: " + iStateE.getMessage());
      }
      if (passed == false) { // need case for no exception being thrown
        throw new Exception("Did not receive IllegalStateException");
      }
    } catch (Exception e) { // handles case of other exception being thrown
      TestUtil.printStackTrace(e);
      throw new Fault("xIllegalStateExceptionQueueTest");
    }
  }

  /*
   * @testName: xUnsupportedOperationExceptionQTest1
   * 
   * @assertion_ids: JMS:JAVADOC:668; JMS:JAVADOC:671;
   * 
   * @test_Strategy: Create a QueueSender with a null Queue. Verify that
   * UnsupportedOperationException is thrown when send is called without a valid
   * Queue.
   */

  public void xUnsupportedOperationExceptionQTest1() throws Fault {
    try {
      TextMessage messageSent = null;
      boolean pass = true;
      Queue myQueue = null;
      QueueSender qSender = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);

      // Close default QueueSender and create a new one with null Queue
      tool.getDefaultQueueSender().close();
      qSender = tool.getDefaultQueueSession().createSender(myQueue);

      tool.getDefaultQueueConnection().start();

      try {
        messageSent = tool.getDefaultQueueSession().createTextMessage();
        messageSent.setText("sending a Text message");
        messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
            "xUnsupportedOperationExceptionQTest1");

        logTrace("sending a Text message");
        qSender.send(messageSent);

        pass = false;
        logErr(
            "Error: QueueSender.send(Message) didn't throw expected UnsupportedOperationException.");
      } catch (UnsupportedOperationException ex) {
        logMsg(
            "Got expected UnsupportedOperationException from QueueSender.send(Message)");
      } catch (Exception e) {
        logErr("Error: QueueSender.send(Message) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        messageSent = tool.getDefaultQueueSession().createTextMessage();
        messageSent.setText("sending a Text message");
        messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
            "xUnsupportedOperationExceptionQTest1");

        logTrace("sending a Text message");
        qSender.send(messageSent, Message.DEFAULT_DELIVERY_MODE,
            Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);

        pass = false;
        logErr(
            "Error: QueueSender.send(Message, int, int, long) didn't throw expected UnsupportedOperationException.");
      } catch (UnsupportedOperationException ex) {
        logMsg(
            "Got expected UnsupportedOperationException from QueueSender.send(Message, int, int, long)");
      } catch (Exception e) {
        logErr(
            "Error: QueueSender.send(Message, int, int, long) throw incorrect Exception: ",
            e);
        pass = false;
      }

      if (pass != true)
        throw new Fault("xUnsupportedOperationExceptionQTest1 Failed!");
    } catch (Exception e) {
      logErr("xUnsupportedOperationExceptionQTest1 Failed!", e);
      throw new Fault("xUnsupportedOperationExceptionQTest1 Failed!", e);
    } finally {
      try {
        if (tool.getDefaultQueueConnection() != null) {
          tool.flushQueue();
          tool.getDefaultQueueConnection().close();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception closing QueueConnection and cleanup", e);
      }
    }
  }

  /*
   * @testName: xUnsupportedOperationExceptionQTest2
   * 
   * @assertion_ids: JMS:JAVADOC:599; JMS:JAVADOC:602;
   *
   * @test_Strategy: Create a MessageProducer with a null Destination. Verify
   * that UnsupportedOperationException is thrown when send is called without a
   * valid Destination.
   */

  public void xUnsupportedOperationExceptionQTest2() throws Fault {
    try {
      TextMessage messageSent = null;
      boolean pass = true;
      Destination myDest = null;
      MessageProducer mSender = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);

      // Close default MessageProducer and create a new one with null
      // Destination
      tool.getDefaultProducer().close();
      mSender = tool.getDefaultSession().createProducer(myDest);

      tool.getDefaultConnection().start();

      try {
        messageSent = tool.getDefaultSession().createTextMessage();
        messageSent.setText("sending a Text message");
        messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
            "xUnsupportedOperationExceptionQTest2");

        logTrace("sending a Text message");
        mSender.send(messageSent);

        pass = false;
        logErr(
            "Error: MessageProducer.send(Message) didn't throw expected UnsupportedOperationException.");
      } catch (UnsupportedOperationException ex) {
        logMsg(
            "Got expected UnsupportedOperationException from MessageProducer.send(Message)");
      } catch (Exception e) {
        logErr(
            "Error: MessageProducer.send(Message) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        messageSent = tool.getDefaultSession().createTextMessage();
        messageSent.setText("sending a Text message");
        messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
            "xUnsupportedOperationExceptionQTest2");

        logTrace("sending a Text message");
        mSender.send(messageSent, Message.DEFAULT_DELIVERY_MODE,
            Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);

        pass = false;
        logErr(
            "Error: MessageProducer.send(Message, int, int, long) didn't throw expected UnsupportedOperationException.");
      } catch (UnsupportedOperationException ex) {
        logMsg(
            "Got expected UnsupportedOperationException from MessageProducer.send(Message, int, int, long)");
      } catch (Exception e) {
        logErr(
            "Error: MessageProducer.send(Message, int, int, long) throw incorrect Exception: ",
            e);
        pass = false;
      }

      if (pass != true)
        throw new Fault("xUnsupportedOperationExceptionQTest2 Failed!");

    } catch (Exception e) {
      logErr("xUnsupportedOperationExceptionQTest2 Failed!", e);
      throw new Fault("xUnsupportedOperationExceptionQTest2 Failed!", e);
    } finally {
      try {
        if (tool.getDefaultConnection() != null) {
          tool.flushDestination();
          tool.getDefaultConnection().close();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception closing Connection and cleanup", e);
      }
    }
  }

  /*
   * @testName: xUnsupportedOperationExceptionQTest3
   * 
   * @assertion_ids: JMS:JAVADOC:605;
   *
   * @test_Strategy: Create a MessageProducer with a valid Destination. Verify
   * that UnsupportedOperationException is thrown when send is called with
   * another valid Destination.
   */

  public void xUnsupportedOperationExceptionQTest3() throws Fault {
    try {
      TextMessage messageSent = null;
      boolean pass = true;
      Destination myDest = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultConnection().start();

      try {
        messageSent = tool.getDefaultSession().createTextMessage();
        messageSent.setText("sending a Text message");
        messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
            "xUnsupportedOperationExceptionQTest3");

        logTrace("get the second Destination");
        myDest = (Destination) tool.getQueueDestination("MY_QUEUE2");

        logTrace("sending a Text message");
        tool.getDefaultProducer().send(myDest, messageSent);

        pass = false;
        logErr(
            "Error: MessageProducer.send(Destination, Message) didn't throw expected UnsupportedOperationException.");
      } catch (UnsupportedOperationException ex) {
        logMsg(
            "Got expected UnsupportedOperationException from MessageProducer.send(Destination, Message)");
      } catch (Exception e) {
        logErr(
            "Error: MessageProducer.send(Destination, Message) throw incorrect Exception: ",
            e);
        pass = false;
      }

      if (pass != true)
        throw new Fault("xUnsupportedOperationExceptionQTest3 Failed!");

    } catch (Exception e) {
      logErr("xUnsupportedOperationExceptionQTest3 Failed!", e);
      throw new Fault("xUnsupportedOperationExceptionQTest3 Failed!", e);
    } finally {
      try {
        if (tool.getDefaultConnection() != null) {
          tool.flushDestination();
          tool.getDefaultConnection().close();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception closing Connection and cleanup", e);
      }
    }
  }

  /*
   * @testName: xInvalidDestinationExceptionQTests
   *
   * @assertion_ids: JMS:JAVADOC:502; JMS:JAVADOC:504; JMS:JAVADOC:510;
   * JMS:JAVADOC:638; JMS:JAVADOC:639; JMS:JAVADOC:641; JMS:JAVADOC:643;
   * JMS:JAVADOC:644; JMS:JAVADOC:646; JMS:JAVADOC:647; JMS:JAVADOC:649;
   *
   * @test_Strategy: Create a Session with Queue Configuration, using a null
   * Destination/Queue to verify InvalidDestinationException is thrown with
   * various methods
   */

  public void xInvalidDestinationExceptionQTests() throws Fault {

    try {
      boolean pass = true;
      Destination dummy = null;
      Queue dummyQ = null;

      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultProducer().close();
      tool.getDefaultConsumer().close();

      try {
        tool.getDefaultSession().createConsumer(dummy);
        logErr(
            "Error: createConsumer(null) didn't throw expected InvalidDestinationException");
        pass = false;
      } catch (InvalidDestinationException ex) {
        logMsg(
            "Got expected InvalidDestinationException from createConsumer(null)");
      } catch (Exception e) {
        logErr("Error: createConsumer(null) throw incorrect Exception: ", e);
        pass = false;
      }

      try {
        tool.getDefaultSession().createConsumer(dummy, "TEST = 'test'");
        logErr(
            "Error: createConsumer(null, String) didn't throw expected InvalidDestinationException");
        pass = false;
      } catch (InvalidDestinationException ex) {
        logMsg(
            "Got expected InvalidDestinationException from createConsumer(null, String)");
      } catch (Exception e) {
        logErr(
            "Error: createConsumer(null, String) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        tool.getDefaultSession().createConsumer(dummy, "TEST = 'test'", true);
        logErr(
            "Error: createConsumer(null, String, boolean) didn't throw expected InvalidDestinationException");
        pass = false;
      } catch (InvalidDestinationException ex) {
        logMsg(
            "Got expected InvalidDestinationException from createConsumer(null, String, true)");
      } catch (Exception e) {
        logErr(
            "Error: createConsumer(null, String, true) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        tool.getDefaultSession().createBrowser(dummyQ);
        logErr(
            "Error: createBrowser(null) didn't throw expected InvalidDestinationException");
        pass = false;
      } catch (InvalidDestinationException ex) {
        logMsg(
            "Got expected InvalidDestinationException from createBrowser(null)");
      } catch (Exception e) {
        logErr("Error: createBrowser(null) throw incorrect Exception: ", e);
        pass = false;
      }

      try {
        tool.getDefaultSession().createBrowser(dummyQ, "TEST = 'test'");
        logErr(
            "Error: createBrowser(null, String) didn't throw expected InvalidDestinationException");
        pass = false;
      } catch (InvalidDestinationException ex) {
        logMsg(
            "Got expected InvalidDestinationException from createBrowser(null, String)");
      } catch (Exception e) {
        logErr("Error: createBrowser(null, String) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        tool.closeDefaultConnections();
      } catch (Exception ex) {
        logErr("Error closing the default Connection", ex);
      }

      if (pass != true)
        throw new Fault("xInvalidDestinationExceptionQTests");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("xInvalidDestinationExceptionQTests");
    } finally {
      try {
        tool.closeDefaultConnections();
      } catch (Exception ex) {
        logErr("Error closing Connection", ex);
      }
    }
  }

  /*
   * @testName: xMessageNotReadableExceptionQBytesMsgTest
   *
   * @assertion_ids: JMS:JAVADOC:676; JMS:JAVADOC:678; JMS:JAVADOC:682;
   * JMS:JAVADOC:684; JMS:JAVADOC:686; JMS:JAVADOC:688; JMS:JAVADOC:690;
   * JMS:JAVADOC:692; JMS:JAVADOC:694; JMS:JAVADOC:696; JMS:JAVADOC:698;
   * JMS:JAVADOC:699; JMS:JAVADOC:700;
   *
   * @test_Strategy: Create a BytesMessage, call various read methods on it
   * before sending. Verify that javax.jms.MessageNotReadableException is
   * thrown.
   */

  public void xMessageNotReadableExceptionQBytesMsgTest() throws Fault {
    try {
      BytesMessage messageSent = null;
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

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultProducer().close();
      tool.getDefaultConsumer().close();

      logTrace("Creating 1 message");
      messageSent = tool.getDefaultSession().createBytesMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageNotReadableExceptionQBytesMsgTest");

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

      try {
        messageSent.getBodyLength();
        pass = false;
        TestUtil.logErr(
            "Error: getBodyLength didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil.logTrace(
            "getBodyLength threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("getBodyLength threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageSent.readBoolean();
        pass = false;
        TestUtil.logErr(
            "Error: readBoolean didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil.logTrace(
            "readBoolean threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readBoolean threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageSent.readByte();
        pass = false;
        TestUtil.logErr(
            "Error: readByte didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil
            .logTrace("readByte threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readByte threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageSent.readUnsignedByte();
        pass = false;
        TestUtil.logErr(
            "Error: readUnsignedByte didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil.logTrace(
            "readUnsignedByte threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readUnsignedByte threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageSent.readShort();
        pass = false;
        TestUtil.logErr(
            "Error: readShort didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil
            .logTrace("readShort threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readShort threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageSent.readUnsignedShort();
        pass = false;
        TestUtil.logErr(
            "Error: readUnsignedShort didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil.logTrace(
            "readUnsignedShort threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readUnsignedShort threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageSent.readChar();
        pass = false;
        TestUtil.logErr(
            "Error: readChar didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil
            .logTrace("readChar threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readChar threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageSent.readInt();
        pass = false;
        TestUtil.logErr(
            "Error: readInt didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil
            .logTrace("readInt threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readInt threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageSent.readLong();
        pass = false;
        TestUtil.logErr(
            "Error: readLong didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil
            .logTrace("readLong threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readLong threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageSent.readFloat();
        pass = false;
        TestUtil.logErr(
            "Error: readFloat didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil
            .logTrace("readFloat threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readFloat threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageSent.readDouble();
        pass = false;
        TestUtil.logErr(
            "Error: readDouble didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil
            .logTrace("readDouble threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readDouble threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageSent.readUTF();
        pass = false;
        TestUtil.logErr(
            "Error: readUTF didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil
            .logTrace("readUTF threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readUTF threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageSent.readBytes(bytesValueRecvd);
        pass = false;
        TestUtil.logErr(
            "Error: readBytes(byte[]) didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil.logTrace(
            "readBytes(byte[]) threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readBytes(byte[]) threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageSent.readBytes(bytesValueRecvd, 1);
        pass = false;
        TestUtil.logErr(
            "Error: readBytes(byte[], int) didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil.logTrace(
            "readBytes(byte[], int) threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readBytes(byte[],int) threw Wrong Exception!", e);
        pass = false;
      }

      if (pass != true)
        throw new Fault("xMessageNotReadableExceptionQBytesMsgTest Failed!");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("xMessageNotReadableExceptionQBytesMsgTest:");
    } finally {
      try {
        tool.closeDefaultConnections();
      } catch (Exception ex) {
        logErr("Error closing Connection", ex);
      }
    }
  }

  /*
   * @testName: xMessageNotReadableExceptionQStreamMsgTest
   *
   * @assertion_ids: JMS:SPEC:73.1; JMS:JAVADOC:431; JMS:JAVADOC:721;
   * JMS:JAVADOC:724; JMS:JAVADOC:727; JMS:JAVADOC:730; JMS:JAVADOC:733;
   * JMS:JAVADOC:736; JMS:JAVADOC:739; JMS:JAVADOC:742; JMS:JAVADOC:745;
   * JMS:JAVADOC:748; JMS:JAVADOC:751;
   *
   * @test_Strategy: Create a StreamMessage, send and receive via a Topic; Call
   * clearBoldy right after receiving the message; Call various read methods on
   * received message; Verify javax.jms.MessageNotReadableException is thrown.
   */

  public void xMessageNotReadableExceptionQStreamMsgTest() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
      boolean pass = true;
      byte bValue = 127;
      boolean abool = false;
      byte[] bValues = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
      byte[] bValues2 = { 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
      byte[] bValuesReturned = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
      char charValue = 'Z';
      short sValue = 32767;
      long lValue = 9223372036854775807L;
      double dValue = 6.02e23;
      float fValue = 6.02e23f;
      int iValue = 6;
      String myString = "text";
      String sTesting = "Testing StreamMessages";

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultConnection().start();

      logTrace("Creating 1 message");
      messageSent = tool.getDefaultSession().createStreamMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageNotReadableExceptionQStreamMsgTest");

      // -----------------------------------------------------------------------------
      logTrace("");
      logMsg("Writing one of each primitive type to the message");
      // -----------------------------------------------------------------------------
      messageSent.writeBytes(bValues2, 0, bValues.length);
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
      messageSent.writeObject(null);

      // send the message and then get it back
      logTrace("Sending message");
      tool.getDefaultProducer().send(messageSent);
      logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultConsumer()
          .receive(timeout);

      TestUtil.logTrace("call ClearBody()");
      messageReceived.clearBody();

      try {
        messageReceived.readBoolean();
        pass = false;
        TestUtil.logErr(
            "Error: readBoolean didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil.logTrace(
            "readBoolean threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readBoolean threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageReceived.readByte();
        pass = false;
        TestUtil.logErr(
            "Error: readByte didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil
            .logTrace("readByte threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readByte threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageReceived.readShort();
        pass = false;
        TestUtil.logErr(
            "Error: readShort didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil
            .logTrace("readShort threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readShort threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageReceived.readChar();
        pass = false;
        TestUtil.logErr(
            "Error: readChar didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil
            .logTrace("readChar threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readChar threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageReceived.readInt();
        pass = false;
        TestUtil.logErr(
            "Error: readInt didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil
            .logTrace("readInt threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readInt threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageReceived.readLong();
        pass = false;
        TestUtil.logErr(
            "Error: readLong didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil
            .logTrace("readLong threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readLong threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageReceived.readFloat();
        pass = false;
        TestUtil.logErr(
            "Error: readFloat didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil
            .logTrace("readFloat threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readFloat threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageReceived.readDouble();
        pass = false;
        TestUtil.logErr(
            "Error: readDouble didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil
            .logTrace("readDouble threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readDouble threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageReceived.readString();
        pass = false;
        TestUtil.logErr(
            "Error: readString didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil
            .logTrace("readString threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readString threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageReceived.readBytes(bValuesReturned);
        pass = false;
        TestUtil.logErr(
            "Error: readBytes(byte[]) didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil.logTrace(
            "readBytes(byte[]) threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readBytes(byte[]) threw Wrong Exception!", e);
        pass = false;
      }

      try {
        messageReceived.readObject();
        pass = false;
        TestUtil.logErr(
            "Error: readObject didn't throw Expected MessageNotReadableException!");
      } catch (MessageNotReadableException nr) {
        TestUtil
            .logTrace("readObject threw Expected MessageNotReadableException!");
      } catch (Exception e) {
        TestUtil.logErr("readObject threw Wrong Exception!", e);
        pass = false;
      }

      if (pass != true)
        throw new Fault("xMessageNotReadableExceptionQStreamMsgTest Failed!");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("xMessageNotReadableExceptionQStreamMsgTest:");
    } finally {
      try {
        tool.closeDefaultConnections();
      } catch (Exception ex) {
        logErr("Error closing Connection", ex);
      }
    }
  }

  /*
   * @testName: xIllegalStateExceptionTestTopicMethodsQ
   * 
   * @assertion_ids: JMS:SPEC:185.2; JMS:SPEC:185.3; JMS:SPEC:185.4;
   * JMS:SPEC:185.5; JMS:SPEC:185;
   *
   * @test_Strategy: Create a QueueSession and call Topic specific methods
   * inherited from Session, and verify that javax.jms.IllegalStateException is
   * thrown.
   */

  public void xIllegalStateExceptionTestTopicMethodsQ() throws Fault {

    try {
      boolean pass = true;
      Topic myTopic = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      myTopic = tool.createNewTopic("MY_TOPIC");

      try {
        tool.getDefaultQueueSession().createDurableSubscriber(myTopic, "cts");
        pass = false;
        TestUtil.logErr(
            "Error: QueueSession.createDurableSubscriber(Topic, String) "
                + "didn't throw expected IllegalStateException.");
      } catch (javax.jms.IllegalStateException ex) {
        TestUtil.logMsg(
            "Got expected IllegalStateException from QueueSession.createDurableSubscriber(Topic, String)");
      } catch (Exception e) {
        TestUtil.logErr(
            "Error: QueueSession.createDurableSubscriber(Topic, String) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        tool.getDefaultQueueSession().createDurableSubscriber(myTopic, "cts",
            "TEST = 'test'", false);
        pass = false;
        TestUtil.logErr(
            "Error: QueueSession.createDurableSubscriber(Topic, String, String, boolean) "
                + "didn't throw expected IllegalStateException.");
      } catch (javax.jms.IllegalStateException ex) {
        TestUtil.logMsg("Got expected IllegalStateException from "
            + "QueueSession.createDurableSubscriber(Topic, String, String, boolean)");
      } catch (Exception e) {
        TestUtil.logErr(
            "Error: QueueSession.createDurableSubscriber(Topic, String, String, boolean) "
                + "throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        tool.getDefaultQueueSession().createTemporaryTopic();
        pass = false;
        TestUtil.logErr(
            "Error: QueueSession.createTemporayTopic() didn't throw expected IllegalStateException.");
      } catch (javax.jms.IllegalStateException ex) {
        TestUtil.logMsg(
            "Got expected IllegalStateException from QueueSession.createTemporayTopic()");
      } catch (Exception e) {
        TestUtil.logErr(
            "Error: QueueSession.createTemporayTopic() throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        tool.getDefaultQueueSession().createTopic("foo");
        pass = false;
        TestUtil.logErr(
            "Error: QueueSession.createTopic(String) didn't throw expected IllegalStateException.");
      } catch (javax.jms.IllegalStateException ex) {
        TestUtil.logMsg(
            "Got expected IllegalStateException from QueueSession.createTopic(String)");
      } catch (Exception e) {
        TestUtil.logErr(
            "Error: QueueSession.createTopic(String) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        tool.getDefaultQueueSession().unsubscribe("foo");
        pass = false;
        TestUtil.logErr(
            "Error: QueueSession.unsubscribe(String) didn't throw expected IllegalStateException.");
      } catch (javax.jms.IllegalStateException ex) {
        TestUtil.logMsg(
            "Got expected IllegalStateException from QueueSession.unsubscribe(String)");
      } catch (Exception e) {
        TestUtil.logErr(
            "Error: QueueSession.unsubscribe(String) throw incorrect Exception: ",
            e);
        pass = false;
      }

      if (pass != true)
        throw new Fault("xIllegalStateExceptionTestTopicMethodsQ Failed!");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("xIllegalStateExceptionTestTopicMethodsQ");
    } finally {
      try {
        tool.closeDefaultConnections();
      } catch (Exception ex) {
        TestUtil.logErr("Error closing Connection", ex);
      }
    }
  }

  /*
   * @testName: xIllegalStateExceptionTestRollbackQ
   *
   * @assertion_ids: JMS:JAVADOC:502; JMS:JAVADOC:504; JMS:JAVADOC:510;
   * JMS:JAVADOC:242; JMS:JAVADOC:635; JMS:JAVADOC:317;
   *
   * @test_Strategy: 1. Create a TextMessages, send use a MessageProducer 2.
   * Then rollback on the non-transacted session Verify that
   * IllegalStateException is thrown
   */

  public void xIllegalStateExceptionTestRollbackQ() throws Fault {

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      boolean pass = true;

      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultConnection().start();

      messageSent = tool.getDefaultSession().createTextMessage();
      messageSent.setText("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xIllegalStateExceptionTestRollbackQ");

      // send the message and then get it back
      logTrace("Sending message to a Queue");
      tool.getDefaultProducer().send(messageSent);

      try {
        logTrace(
            "Rolling back a non-transacted session must throw IllegalStateException");
        tool.getDefaultSession().rollback();
        pass = false;
        TestUtil.logErr(
            "Error: QueueSession.rollback() didn't throw expected IllegalStateException");
      } catch (javax.jms.IllegalStateException en) {
        TestUtil.logMsg(
            "Got expected IllegalStateException from QueueSession.rollback()");
      }

      if (pass != true)
        throw new Fault("xIllegalStateExceptionTestRollbackQ");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("xIllegalStateExceptionTestRollbackQ");
    } finally {
      try {
        tool.closeDefaultConnections();
        tool.flushDestination();
      } catch (Exception ex) {
        TestUtil.logErr("Error closing Connection", ex);
      }
    }
  }
}
