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
package com.sun.ts.tests.jms.core.exceptionTopic;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.io.*;
import java.util.Properties;
import java.util.ArrayList;
import com.sun.javatest.Status;
import javax.jms.*;

public class ExceptionTopicTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.exceptionTopic.ExceptionTopicTests";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // Harness req's
  private Properties props = null;

  // JMS object
  private transient JmsTool tool = null;

  // properties read from ts.jte file
  long timeout;

  private String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

  private String jmsUser;

  private String jmsPassword;

  private String mode;

  ArrayList connections = null;

  /* Run test in standalone mode */

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    ExceptionTopicTests theTests = new ExceptionTopicTests();
    Status s = theTests.run(args, System.out, System.err);

    s.exit();
  }

  /* Utility methods for tests */

  /*
   * Checks passed flag for negative tests and throws exception back to caller
   * which passes ot to harness.
   * 
   * @param boolean Pass/Fail flag
   */
  private void checkExceptionPass(boolean passed) throws Exception {
    if (passed == false) {
      logMsg("Didn't get expected exception");
      throw new Exception("Didn't catch expected exception");
    }
  }

  /* Test setup: */

  /*
   * setup() is called before each test
   * 
   * @class.setup_props: jms_timeout; user; password; platform.mode;
   * 
   * @exception Fault
   */
  public void setup(String[] args, Properties p) throws Fault {
    try {
      TestUtil.logTrace("In setup");
      // get props
      jmsUser = p.getProperty("user");
      jmsPassword = p.getProperty("password");
      mode = p.getProperty("platform.mode");
      timeout = Long.parseLong(p.getProperty("jms_timeout"));
      if (timeout < 1) {
        throw new Exception("'timeout' (milliseconds) in ts.jte must be > 0");
      }
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
   * @testName: xInvalidDestinationExceptionTopicTest
   * 
   * @assertion_ids: JMS:SPEC:174; JMS:JAVADOC:83; JMS:JAVADOC:85;
   * JMS:JAVADOC:87; JMS:JAVADOC:89; JMS:JAVADOC:91; JMS:JAVADOC:95;
   * JMS:JAVADOC:625; JMS:JAVADOC:626; JMS:JAVADOC:628; JMS:JAVADOC:629;
   * JMS:JAVADOC:631; JMS:JAVADOC:632;
   * 
   * @test_Strategy: pass an invalid Topic object to createSubscriber(null)
   * createSubscriber(null,selector,nolocal)
   * createDurableSubscriber(null,subscriber)
   * createDurableSubscriber(null,String,selector,nolocal) createPublisher(null)
   * - null is valid here. unsubscribe(invalidSubscriptionName)
   */

  public void xInvalidDestinationExceptionTopicTest() throws Fault {
    boolean pass = true;
    TopicPublisher tPublisher = null;
    TopicSubscriber tSubscriber = null;
    Topic dummy = null;

    try {
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
      TestUtil.logTrace("** Close default TopicSubscriber **");
      tool.getDefaultTopicSubscriber().close();
      tool.getDefaultTopicConnection().start();
      logMsg("Test unsubscribe(invalidSubscriptionName)");
      try {
        tool.getDefaultTopicSession().unsubscribe("invalidSubscriptionName");
        TestUtil.logTrace("FAIL: expected InvalidDestinationException!");
        pass = false;
      } catch (Exception ee) {
        if (ee instanceof javax.jms.InvalidDestinationException) {
          TestUtil
              .logTrace("Pass: InvalidDestinationException thrown as expected");
        } else {
          logMsg("Error: unexpected error " + ee.getClass().getName()
              + " was thrown");
          pass = false;
        }
      }
      logMsg("Test createSubscriber(null)");
      try {
        tSubscriber = tool.getDefaultTopicSession().createSubscriber(dummy);
        TestUtil.logTrace("FAIL: expected InvalidDestinationException!");
        pass = false;
      } catch (Exception ee) {
        if (ee instanceof javax.jms.InvalidDestinationException) {
          TestUtil
              .logTrace("Pass: InvalidDestinationException thrown as expected");
        } else {
          logMsg("Error: unexpected error " + ee.getClass().getName()
              + " was thrown");
          pass = false;
        }
      }
      logMsg("Test createSubscriber(null,selector,nolocal)");
      try {
        tSubscriber = tool.getDefaultTopicSession().createSubscriber(dummy,
            "TEST", true);
        TestUtil.logTrace("FAIL: expected InvalidDestinationException!");
        pass = false;
      } catch (Exception ee) {
        if (ee instanceof javax.jms.InvalidDestinationException) {
          TestUtil
              .logTrace("Pass: InvalidDestinationException thrown as expected");
        } else {
          logMsg("Error: unexpected error " + ee.getClass().getName()
              + " was thrown");
          pass = false;
        }
      }
      logMsg("Test createDurableSubscriber(null,String)");
      try {
        tSubscriber = tool.getDefaultTopicSession()
            .createDurableSubscriber(dummy, "durable");
        TestUtil.logTrace("FAIL: expected InvalidDestinationException!");
        pass = false;
      } catch (Exception ee) {
        if (ee instanceof javax.jms.InvalidDestinationException) {
          TestUtil
              .logTrace("Pass: InvalidDestinationException thrown as expected");
        } else {
          logMsg("Error: unexpected error " + ee.getClass().getName()
              + " was thrown");
          pass = false;
        }
      }
      logMsg("Test createDurableSubscriber(null,String,selector,nolocal)");
      try {
        tSubscriber = tool.getDefaultTopicSession()
            .createDurableSubscriber(dummy, "durable", "TEST", true);
        TestUtil.logTrace("FAIL: expected InvalidDestinationException!");
        pass = false;
      } catch (Exception ee) {
        if (ee instanceof javax.jms.InvalidDestinationException) {
          TestUtil
              .logTrace("Pass: InvalidDestinationException thrown as expected");
        } else {
          logMsg("Error: unexpected error " + ee.getClass().getName()
              + " was thrown");
          pass = false;
        }
      }
      logMsg("Test createPublisher(null) - This is valid");
      try {
        tPublisher = tool.getDefaultTopicSession().createPublisher(dummy);
        if (tPublisher != null)
          logMsg("tPublisher=" + tPublisher);
        TestUtil.logTrace("PASS: null allowed for unidentified producer");
      } catch (Exception ee) {
        TestUtil.printStackTrace(ee);
        logErr("Error: unexpected error " + ee.getClass().getName()
            + " was thrown");
        pass = false;
      }
      if (!pass) {
        throw new Fault(
            "Error: failures occurred during xInvalidDestinationExceptionQTest tests");
      }
    } catch (Exception e) {
      throw new Fault("xInvalidDestinationExceptionTopicTest", e);
    }
  }

  /*
   * @testName: xMessageNotReadableExceptionTopicTest
   * 
   * @assertion_ids: JMS:SPEC:178; JMS:JAVADOC:680;
   * 
   * @test_Strategy: create a BytesMessage, read it.
   */

  public void xMessageNotReadableExceptionTopicTest() throws Fault {
    try {
      BytesMessage msg = null;

      // create Topic Connection
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
      tool.getDefaultTopicConnection().start();

      // Create a BytesMessage
      msg = tool.getDefaultTopicSession().createBytesMessage();
      msg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageNotReadableExceptionTopicTest");
      try {
        msg.readByte();
        TestUtil.logErr("FAIL --- should not have gotten this far!");
        throw new Fault("Fail: Did not throw expected error!!!");
      } catch (MessageNotReadableException nr) {
        TestUtil.logMsg("Passed with expected MessageNotReadableException.");
      }
    } catch (Exception e) {
      TestUtil.logErr("Expected MessageNotReadableException did not occur:", e);
      throw new Fault("xMessageNotReadableExceptionTopicTest test failed", e);
    }
  }

  /*
   * @testName: xMessageNotWriteableExceptionTTestforTextMessage
   * 
   * @assertion_ids: JMS:SPEC:179; JMS:SPEC:70; JMS:JAVADOC:766;
   * 
   * @test_Strategy: When a client receives a text message it is in read-only
   * mode. Publish a message and have the client attempt to write to it.
   */

  public void xMessageNotWriteableExceptionTTestforTextMessage() throws Fault {
    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      byte bValue = 127;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createTextMessage();
      messageSent.setText("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageNotWriteableExceptionTTestforTextMessage");
      TestUtil.logTrace("Publishing message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (TextMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("getMessage returned null!! - test did not run!");
      } else {
        TestUtil.logTrace("Got message - OK");
      }

      // Received message should be read-only - verify proper exception is
      // thrown
      try {
        messageReceived.setText("testing...");
      } catch (MessageNotWriteableException nr) {
        TestUtil.logMsg("Passed with expected MessageNotWriteableException");
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "xMessageNotWriteableExceptionTTestforTextMessage failed:", e);
      throw new Fault("xMessageNotWriteableExceptionTTestforTextMessage", e);
    }
  }

  /*
   * @testName: xMessageNotWriteableExceptionTestforBytesMessage
   * 
   * @assertion_ids: JMS:SPEC:73; JMS:SPEC:179; JMS:JAVADOC:702;
   * 
   * @test_Strategy: When a client receives a Bytes message it is in read-only
   * mode. Publish a message and have the client attempt to write to it.
   */

  public void xMessageNotWriteableExceptionTestforBytesMessage() throws Fault {
    try {
      BytesMessage messageSent = null;
      BytesMessage messageReceived = null;
      byte bValue = 127;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createBytesMessage();
      messageSent.writeByte(bValue);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageNotWriteableExceptionTestforBytesMessage");
      TestUtil.logTrace("Publishing message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (BytesMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("getMessage returned null!! - test did not run!");
      } else {
        TestUtil.logTrace("Got message - OK");
      }

      // Received message should be read-only - verify proper exception is
      // thrown
      try {
        messageReceived.writeByte(bValue);
      } catch (MessageNotWriteableException nr) {
        TestUtil.logMsg("Passed with expected MessageNotWriteableException.");
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "xMessageNotWriteableExceptionTestforBytesMessage failed: ", e);
      throw new Fault("xMessageNotWriteableExceptionTestforBytesMessage", e);
    }
  }

  /*
   * @testName: xMessageNotWriteableExceptionTTestforStreamMessage
   * 
   * @assertion_ids: JMS:SPEC:73; JMS:SPEC:179; JMS:JAVADOC:760;
   * 
   * @test_Strategy: When a client receives a Stream message it is in read-only
   * mode. Publish a message and have the client attempt to write to it.
   */

  public void xMessageNotWriteableExceptionTTestforStreamMessage()
      throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createStreamMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageNotWriteableExceptionTTestforStreamMessage");

      messageSent.writeString("Testing...");
      TestUtil.logTrace("Publishing message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("getMessage returned null!! - test did not run!");
      } else {
        TestUtil.logTrace("Got message - OK");
      }

      // Received message should be read-only - verify proper exception is
      // thrown
      try {
        messageReceived.writeString("Testing...");
      } catch (MessageNotWriteableException nr) {
        TestUtil.logTrace("Passed with expected MessageNotWriteableException");
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "xMessageNotWriteableExceptionTTestforStreamMessage failed: ", e);
      throw new Fault("xMessageNotWriteableExceptionTTestforStreamMessage", e);
    }
  }

  /*
   * @testName: xMessageNotWriteableExceptionTTestforMapMessage
   * 
   * @assertion_ids: JMS:SPEC:73; JMS:SPEC:179; JMS:JAVADOC:822;
   * 
   * @test_Strategy: When a client receives a Map message it is in read-only
   * mode. Publish a message and have the client attempt to write to it.
   */
  public void xMessageNotWriteableExceptionTTestforMapMessage() throws Fault {
    try {
      MapMessage messageSent = null;
      MapMessage messageReceived = null;
      byte bValue = 127;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createMapMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageNotWriteableExceptionTTestforMapMessage");
      messageSent.setString("aString", "value");
      TestUtil.logTrace("Publishing message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (MapMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("getMessage returned null!! - test did not run!");
      } else {
        TestUtil.logTrace("Got message - OK");
      }

      // Received message should be read-only - verify proper exception is
      // thrown
      try {
        messageReceived.setString("aString", "value");
      } catch (MessageNotWriteableException nr) {
        TestUtil.logMsg(
            "Passed with expected MessageNotWriteableException occurred.");
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "xMessageNotWriteableExceptionTTestforMapMessage failed: ", e);
      throw new Fault("xMessageNotWriteableExceptionTTestforMapMessage", e);
    }
  }

  /*
   * @testName: xNullPointerExceptionTopicTest
   * 
   * @assertion_ids: JMS:SPEC:86.1;
   * 
   * @test_Strategy: Create a bytes message. Attempt to write null to it. Verify
   * that a NullPointerException is thrown.
   * 
   */

  public void xNullPointerExceptionTopicTest() throws Fault {
    try {
      BytesMessage msg = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
      tool.getDefaultTopicConnection().start();
      msg = tool.getDefaultTopicSession().createBytesMessage();
      msg.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xNullPointerExceptionTopicTest");
      msg.writeBytes(null);
    } catch (java.lang.NullPointerException nullp) {
      TestUtil.logMsg("Passed with expected NullPointerException.");
    } catch (Exception e) {
      TestUtil.logErr("Expected NullPointerException did not occur!", e);
      throw new Fault("xNullPointerExceptionTopicTest");
    }
  }

  /*
   * @testName: xMessageEOFExceptionTTestforBytesMessage
   * 
   * @assertion_ids: JMS:SPEC:176;
   * 
   * @test_Strategy: Publish a bytes message to a topic with one byte. Retreive
   * message from the topic and read two bytes.
   */

  public void xMessageEOFExceptionTTestforBytesMessage() throws Fault {
    try {
      BytesMessage messageSent = null;
      BytesMessage messageReceived = null;
      byte bValue = 127;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createBytesMessage();
      messageSent.writeByte(bValue);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageEOFExceptionTTestforBytesMessage");
      TestUtil.logTrace("Publishing message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (BytesMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("getMessage returned null!! - test did not run!");
      } else {
        TestUtil.logTrace("Got message - OK");
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
        TestUtil.logTrace("MessageEOFException occurred!");
        TestUtil.logTrace(" " + end.getMessage());
      }
    } catch (Exception e) {
      throw new Fault("xMessageEOFExceptionTTestforBytesMessage", e);
    }
  }

  /*
   * @testName: xMessageEOFExceptionTTestforStreamMessage
   * 
   * @assertion_ids: JMS:SPEC:176;
   * 
   * @test_Strategy: Publish a Stream message to a topic with one byte. Retreive
   * message from the topic and read two bytes.
   */

  public void xMessageEOFExceptionTTestforStreamMessage() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
      byte bValue = 127;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createStreamMessage();
      messageSent.writeByte(bValue);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageEOFExceptionTTestforStreamMessage");
      TestUtil.logTrace("Publishing message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("getMessage returned null!! - test did not run!");
      } else {
        TestUtil.logTrace("Got message - OK");
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
        TestUtil.logTrace("MessageEOFException occurred!");
        TestUtil.logTrace(" " + end.getMessage());
      }
    } catch (Exception e) {
      throw new Fault("xMessageEOFExceptionTTestforStreamMessage", e);
    }
  }

  /*
   * @testName: xMessageFormatExceptionTTestforBytesMessage
   * 
   * @assertion_ids: JMS:SPEC:177;
   * 
   * 
   * @test_Strategy: Call writeObject with a topic session object
   */

  public void xMessageFormatExceptionTTestforBytesMessage() throws Fault {
    try {
      BytesMessage messageSent = null;
      BytesMessage messageReceived = null;
      byte bValue = 127;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createBytesMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageFormatExceptionTTestforBytesMessage");
      TestUtil.logTrace("try to write an invalid object");
      try {
        messageSent.writeObject(tool.getDefaultTopicSession());

        // Should not reach here !!
        TestUtil
            .logTrace("Failed:  expected MessageFormatException not thrown");
        throw new Fault("Fail: Did not throw expected error!!!");
      } catch (MessageFormatException fe) {
        TestUtil.logTrace("Passed.\n");
        TestUtil.logTrace("MessageFormatException occurred!");
        TestUtil.logTrace(" " + fe.getMessage());
      }
    } catch (Exception e) {
      throw new Fault("xMessageFormatExceptionTTestforBytesMessage", e);
    }
  }

  /*
   * @testName: xMessageFormatExceptionTTestforStreamMessage
   * 
   * @assertion_ids: JMS:SPEC:177; JMS:JAVADOC:744;
   * 
   * @test_Strategy: Write a byte array, read it as a string.
   */

  public void xMessageFormatExceptionTTestforStreamMessage() throws Fault {
    try {
      StreamMessage messageSent = null;
      StreamMessage messageReceived = null;
      byte[] bValues = { 127, 0, 3 };

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
      tool.getDefaultTopicConnection().start();
      TestUtil.logTrace("Creating 1 message");
      messageSent = tool.getDefaultTopicSession().createStreamMessage();
      messageSent.writeBytes(bValues);
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageFormatExceptionTTestforStreamMessage");
      TestUtil.logTrace("Publishing message");
      tool.getDefaultTopicPublisher().publish(messageSent);
      TestUtil.logTrace("Receiving message");
      messageReceived = (StreamMessage) tool.getDefaultTopicSubscriber()
          .receive(timeout);
      if (messageReceived == null) {
        throw new Exception("getMessage returned null!! - test did not run!");
      } else {
        TestUtil.logTrace("Got message - OK");
      }

      // Received message should contain a byte, read a string
      try {
        messageReceived.readString();

        // Should not reach here !!
        TestUtil
            .logTrace("Failed:  expected MessageFormatException not thrown");
        throw new Fault("Fail: Did not throw expected error!!!");
      } catch (MessageFormatException fe) {
        TestUtil.logTrace("Passed.\n");
        TestUtil.logTrace("MessageFormatException occurred!");
        TestUtil.logTrace(" " + fe.getMessage());
      }
    } catch (Exception e) {
      throw new Fault("xMessageFormatExceptionTTestforStreamMessage", e);
    }
  }

  /*
   * @testName: xInvalidSelectorExceptionTopicTest
   * 
   * @assertion_ids: JMS:SPEC:69; JMS:SPEC:175; JMS:JAVADOC:627;
   * JMS:JAVADOC:630;
   * 
   * @test_Strategy: Call createSubscriber/createDurableSubscriber with an
   * invalid selector string
   */

  public void xInvalidSelectorExceptionTopicTest() throws Fault {
    try {
      TopicSubscriber tSub = null;

      // create Topic Connection and TopicSubscriber
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
      TestUtil.logTrace("** Close default TopicSubscriber **");
      tool.getDefaultTopicSubscriber().close();
      tool.getDefaultTopicConnection().start();

      // create TopicSubscriber
      tSub = tool.getDefaultTopicSession()
          .createSubscriber(tool.getDefaultTopic(), "=TEST 'test'", false);
      TestUtil.logTrace("FAIL --- should not have gotten this far!");
      throw new Fault("xInvalidSelectorException test Failed!");
    } catch (InvalidSelectorException es) {
      TestUtil.logTrace("Passed.\n");
      TestUtil.logMsg("InvalidSelectorException occurred!");
    } catch (Exception e) {
      TestUtil.logErr("Got Exception instead: ", e);
      TestUtil.logTrace("Expected InvalidSelectorException did not occur!");
      throw new Fault("xInvalidSelectorExceptionTopicTest eee", e);
    }
  }

  /*
   * @testName: xIllegalStateExceptionTopicTest
   * 
   * @assertion_ids: JMS:SPEC:171; JMS:JAVADOC:634;
   * 
   * @test_Strategy: Call session.commit() when there is no transaction to be
   * committed. Verify that the proper exception is thrown.
   */

  public void xIllegalStateExceptionTopicTest() throws Fault {
    boolean passed = false;

    try {

      // create Topic Connection
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
      tool.getDefaultTopicConnection().start();
      logMsg("Calling session.commit(), an illegal operation.");
      try {
        tool.getDefaultTopicSession().commit();
      } catch (javax.jms.IllegalStateException iStateE) {
        logMsg("Received javax.jms.IllegalStateException -- GOOD");
        passed = true;
      }
      if (passed == false) { // need case for no exception being thrown
        throw new Exception("Did not receive IllegalStateException");
      }
    } catch (Exception e) { // handles case of other exception being thrown
      TestUtil.printStackTrace(e);
      throw new Fault("xIllegalStateExceptionTopicTest");
    }
  }

  /*
   * @testName: xUnsupportedOperationExceptionTTest1
   * 
   * @assertion_ids: JMS:JAVADOC:658; JMS:JAVADOC:661;
   *
   * @test_Strategy: Create a TopicPublisher with a null Topic. Verify that
   * UnsupportedOperationException is thrown when publish is called without a
   * valid Topic.
   */

  public void xUnsupportedOperationExceptionTTest1() throws Fault {
    try {
      TextMessage messageSent = null;
      boolean pass = true;
      Topic myTopic = null;
      TopicPublisher tPub = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);

      // Close default TopicPublisher and create a new one with null Topic
      tool.getDefaultTopicPublisher().close();
      tPub = tool.getDefaultTopicSession().createPublisher(myTopic);

      tool.getDefaultTopicConnection().start();

      try {
        messageSent = tool.getDefaultTopicSession().createTextMessage();
        messageSent.setText("sending a Text message");
        messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
            "xUnsupportedOperationExceptionTTest1");

        logTrace("sending a Text message");
        tPub.publish(messageSent);

        pass = false;
        logErr(
            "Error: TopicPublisher.publish(Message) didn't throw expected UnsupportedOperationException.");
      } catch (UnsupportedOperationException ex) {
        logMsg(
            "Got expected UnsupportedOperationException from  TopicPublisher.publish(Message)");
      } catch (Exception e) {
        logErr(
            "Error:  TopicPublisher.publish(Message) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        messageSent = tool.getDefaultTopicSession().createTextMessage();
        messageSent.setText("sending a Text message");
        messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
            "xUnsupportedOperationExceptionTTest1");

        logTrace("sending a Text message");
        tPub.publish(messageSent, Message.DEFAULT_DELIVERY_MODE,
            Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);

        pass = false;
        logErr(
            "Error: TopicPublisher.publish(Message, int, int, long) didn't throw expected UnsupportedOperationException.");
      } catch (UnsupportedOperationException ex) {
        logMsg(
            "Got expected UnsupportedOperationException from  TopicPublisher.publish(Message, int, int, long)");
      } catch (Exception e) {
        logErr(
            "Error:  TopicPublisher.publish(Message, int, int, long) throw incorrect Exception: ",
            e);
        pass = false;
      }

      if (pass != true)
        throw new Fault("xUnsupportedOperationExceptionTTest1 Failed!");

    } catch (Exception e) {
      logErr("xUnsupportedOperationExceptionTTest1 Failed!", e);
      throw new Fault("xUnsupportedOperationExceptionTTest1 Failed!", e);
    } finally {
      try {
        if (tool.getDefaultTopicConnection() != null) {
          tool.getDefaultTopicConnection().close();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception closing TopicConnection and cleanup", e);
      }
    }
  }

  /*
   * @testName: xInvalidDestinationExceptionTTests
   *
   * @assertion_ids: JMS:JAVADOC:502; JMS:JAVADOC:504; JMS:JAVADOC:510;
   * JMS:JAVADOC:638; JMS:JAVADOC:639; JMS:JAVADOC:641; JMS:JAVADOC:643;
   * JMS:JAVADOC:644; JMS:JAVADOC:646; JMS:JAVADOC:647; JMS:JAVADOC:649;
   *
   * @test_Strategy: Create a Session with Topic Configuration, using a null
   * Destination/Topic to verify InvalidDestinationException is thrown with
   * various methods
   */

  public void xInvalidDestinationExceptionTTests() throws Fault {
    String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

    try {
      boolean pass = true;
      Destination dummy = null;
      Topic dummyT = null;

      tool = new JmsTool(JmsTool.COMMON_T, jmsUser, jmsPassword, lookup, mode);
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
        tool.closeDefaultConnections();
      } catch (Exception ex) {
        logErr("Error closing default Connection", ex);
      }

      if (pass != true)
        throw new Fault("xInvalidDestinationExceptionTTests");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("xInvalidDestinationExceptionTTests");
    } finally {
      try {
        tool.closeDefaultConnections();
      } catch (Exception ex) {
        logErr("Error closing Connection", ex);
      }
    }
  }

  /*
   * @testName: xMessageNotReadableExceptionTBytesMsgTest
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

  public void xMessageNotReadableExceptionTBytesMsgTest() throws Fault {
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

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, jmsUser, jmsPassword, lookup, mode);
      tool.getDefaultProducer().close();
      tool.getDefaultConsumer().close();

      logTrace("Creating 1 message");
      messageSent = tool.getDefaultSession().createBytesMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageNotReadableExceptionTBytesMsgTest");

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
        throw new Fault("xMessageNotReadableExceptionTBytesMsgTest Failed!");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("xMessageNotReadableExceptionTBytesMsgTest:");
    } finally {
      try {
        tool.closeDefaultConnections();
      } catch (Exception ex) {
        logErr("Error closing Connection", ex);
      }
    }
  }

  /*
   * @testName: xMessageNotReadableExceptionTStreamMsgTest
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

  public void xMessageNotReadableExceptionTStreamMsgTest() throws Fault {
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

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, jmsUser, jmsPassword, lookup, mode);
      tool.getDefaultConnection().start();

      logTrace("Creating 1 message");
      messageSent = tool.getDefaultSession().createStreamMessage();
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "xMessageNotReadableExceptionTStreamMsgTest");

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
        throw new Fault("xMessageNotReadableExceptionTStreamMsgTest Failed!");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("xMessageNotReadableExceptionTStreamMsgTest:");
    } finally {
      try {
        tool.closeDefaultConnections();
      } catch (Exception ex) {
        logErr("Error closing Connection", ex);
      }
    }
  }

  /*
   * @testName: xIllegalStateExceptionTestQueueMethodsT
   * 
   * @assertion_ids: JMS:SPEC:185.6; JMS:SPEC:185.7; JMS:SPEC:185.8;
   * JMS:SPEC:185;
   *
   * @test_Strategy: Create a TopicSession and call Queue specific methods
   * inherited from Session, and verify that javax.jms.IllegalStateException is
   * thrown.
   */

  public void xIllegalStateExceptionTestQueueMethodsT() throws Fault {

    try {
      boolean pass = true;
      Queue myQueue = null;

      // set up test tool for Topic
      tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, lookup, mode);
      myQueue = tool.createNewQueue("MY_QUEUE");

      try {
        tool.getDefaultTopicSession().createBrowser(myQueue);
        pass = false;
        TestUtil.logErr(
            "Error: TopicSession.createBrowser(Queue) didn't throw expected IllegalStateException.");
      } catch (javax.jms.IllegalStateException ex) {
        TestUtil.logMsg(
            "Got expected IllegalStateException from TopicSession.createBrowser(Queue)");
      } catch (Exception e) {
        TestUtil.logErr(
            "Error: TopicSession.createBrowser(Queue) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        tool.getDefaultTopicSession().createBrowser(myQueue, "TEST = 'test'");
        pass = false;
        TestUtil.logErr(
            "Error: TopicSession.createBrowser(Queue, String) didn't throw expected IllegalStateException.");
      } catch (javax.jms.IllegalStateException ex) {
        TestUtil.logMsg(
            "Got expected IllegalStateException from TopicSession.createBrowser(Queue, String)");
      } catch (Exception e) {
        TestUtil.logErr(
            "Error: TopicSession.createBrowser(Queue, String) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        tool.getDefaultTopicSession().createTemporaryQueue();
        pass = false;
        TestUtil.logErr(
            "Error: TopicSession.createTemporayQueue() didn't throw expected IllegalStateException.");
      } catch (javax.jms.IllegalStateException ex) {
        TestUtil.logMsg(
            "Got expected IllegalStateException from TopicSession.createTemporayQueue()");
      } catch (Exception e) {
        TestUtil.logErr(
            "Error: TopicSession.createTemporayQueue() throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        tool.getDefaultTopicSession().createQueue("foo");
        pass = false;
        TestUtil.logErr(
            "Error: TopicSession.createQueue(String) didn't throw expected IllegalStateException.");
      } catch (javax.jms.IllegalStateException ex) {
        TestUtil.logMsg(
            "Got expected IllegalStateException from TopicSession.createQueue(String)");
      } catch (Exception e) {
        TestUtil.logErr(
            "Error: TopicSession.createQueue(String) throw incorrect Exception: ",
            e);
        pass = false;
      }

      if (pass != true)
        throw new Fault("xIllegalStateExceptionTestQueueMethodsT Failed!");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("xIllegalStateExceptionTestQueueMethodsT");
    } finally {
      try {
        tool.closeDefaultConnections();
      } catch (Exception ex) {
        TestUtil.logErr("Error closing Connection", ex);
      }
    }
  }
}
