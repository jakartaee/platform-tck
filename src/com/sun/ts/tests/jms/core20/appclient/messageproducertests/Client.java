/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jms.core20.appclient.messageproducertests;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Enumeration;
import com.sun.javatest.Status;

public class Client extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core20.messageproducertests.Client";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // JMS tool which creates and/or looks up the JMS administered objects
  private transient JmsTool tool = null;

  // JMS tool which creates and/or looks up the JMS administered objects
  private transient JmsTool toolForProducer = null;

  // JMS objects
  transient MessageProducer producer = null;

  transient MessageConsumer consumer = null;

  transient Connection connection = null;

  transient Session session = null;

  transient Destination destination = null;

  // Harness req's
  private Properties props = null;

  // properties read from ts.jte file
  long timeout;

  String user;

  String password;

  String mode;

  // used for tests
  private static final int numMessages = 3;

  private static final int iterations = 5;

  boolean queueTest = false;

  /* Run test in standalone mode */

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    Client theTests = new Client();
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
      TestUtil.logMsg("Didn't get expected exception");
      throw new Exception("Didn't get expected exception");
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
        throw new Exception("'user' in ts.jte must not be null ");
      }
      if (password == null) {
        throw new Exception("'password' in ts.jte must not be null ");
      }
      if (mode == null) {
        throw new Exception("'platform.mode' in ts.jte must not be null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("setup failed!", e);
    }
  }

  /* cleanup */

  /*
   * cleanup() is called after each test
   * 
   * @exception Fault
   */
  public void cleanup() throws Fault {
    try {
      TestUtil.logMsg("Closing default Connection");
      tool.getDefaultConnection().close();
      if (queueTest) {
        TestUtil.logMsg("Flush any messages left on Queue");
        tool.flushDestination();
      }
      tool.closeAllResources();

      if (toolForProducer != null) {
        toolForProducer.getDefaultConnection().close();
        if (queueTest) {
          TestUtil.logMsg("Flush any messages left on Queue");
          toolForProducer.flushDestination();
        }
        toolForProducer.closeAllResources();
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("cleanup failed!", e);
    }
  }

  /*
   * @testName: queueSendRecvCompletionListenerTest1
   * 
   * @assertion_ids: JMS:JAVADOC:898;
   * 
   * @test_Strategy: Send a message using the following API method and verify
   * the send and recv of data:
   * 
   * MessageProducer.send(Destination, Message, CompletionListener)
   */
  public void queueSendRecvCompletionListenerTest1() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    try {
      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Queue) null);
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = true;

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "queueSendRecvCompletionListenerTest1");

      // Create CompletionListener for Message to be sent
      MyCompletionListener listener = new MyCompletionListener();

      TestUtil.logMsg("Calling send(Destination,Message,CompletionListener)");
      producer.send(destination, expTextMessage, listener);

      TestUtil.logMsg("Poll listener waiting for TestMessage to arrive");
      TextMessage actTextMessage = null;
      for (int i = 0; !listener.isComplete() && i < 60; i++) {
        TestUtil.logMsg("Loop " + i
            + ": sleep 2 seconds waiting for messages to arrive at listener");
        TestUtil.sleepSec(2);
      }
      if (listener.isComplete())
        actTextMessage = (TextMessage) listener.getMessage();

      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg("Check the value in TextMessage");
      if (actTextMessage.getText().equals(expTextMessage.getText())) {
        TestUtil.logMsg("TextMessage is correct");
      } else {
        TestUtil.logErr(
            "TextMessage is incorrect expected " + expTextMessage.getText()
                + ", received " + actTextMessage.getText());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("queueSendRecvCompletionListenerTest1", e);
    } finally {
      try {
        producer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("queueSendRecvCompletionListenerTest1 failed");
    }
  }

  /*
   * @testName: queueSendRecvCompletionListenerTest2
   * 
   * @assertion_ids: JMS:JAVADOC:903;
   * 
   * @test_Strategy: Send a message using the following API method and verify
   * the send and recv of data:
   * 
   * MessageProducer.send(Destination, Message, int, int, long,
   * CompletionListener)
   */
  public void queueSendRecvCompletionListenerTest2() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    try {
      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Queue) null);
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = true;

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "queueSendRecvCompletionListenerTest2");

      // Create CompletionListener for Message to be sent
      MyCompletionListener listener = new MyCompletionListener();

      TestUtil.logMsg(
          "Calling send(Destination,Message,int,int,long,CompletionListener)");
      producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT,
          Message.DEFAULT_PRIORITY, 0L, listener);

      TestUtil.logMsg("Poll listener waiting for TestMessage to arrive");
      TextMessage actTextMessage = null;
      for (int i = 0; !listener.isComplete() && i < 60; i++) {
        TestUtil.logMsg("Loop " + i
            + ": sleep 2 seconds waiting for messages to arrive at listener");
        TestUtil.sleepSec(2);
      }
      if (listener.isComplete())
        actTextMessage = (TextMessage) listener.getMessage();

      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg(
          "Check the values in TextMessage, deliverymode, priority, time to live");
      if (!actTextMessage.getText().equals(expTextMessage.getText())
          || actTextMessage.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT
          || actTextMessage.getJMSPriority() != Message.DEFAULT_PRIORITY
          || actTextMessage.getJMSExpiration() != 0L) {
        TestUtil.logErr("Didn't get the right message.");
        TestUtil.logErr("text=" + actTextMessage.getText() + ", expected "
            + expTextMessage.getText());
        TestUtil.logErr("DeliveryMode=" + actTextMessage.getJMSDeliveryMode()
            + ", expected " + expTextMessage.getJMSDeliveryMode());
        TestUtil.logErr("Priority=" + actTextMessage.getJMSPriority()
            + ", expected " + expTextMessage.getJMSPriority());
        TestUtil.logErr("TimeToLive=" + actTextMessage.getJMSExpiration()
            + ", expected " + expTextMessage.getJMSExpiration());
        pass = false;
      } else {
        TestUtil.logMsg("TextMessage is correct");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("queueSendRecvCompletionListenerTest2", e);
    } finally {
      try {
        producer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("queueSendRecvCompletionListenerTest2 failed");
    }
  }

  /*
   * @testName: queueSendRecvCompletionListenerTest3
   * 
   * @assertion_ids: JMS:JAVADOC:888;
   * 
   * @test_Strategy: Send a message using the following API method and verify
   * the send and recv of data:
   * 
   * MessageProducer.send(Message, CompletionListener)
   */
  public void queueSendRecvCompletionListenerTest3() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    try {
      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      producer = tool.getDefaultProducer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = true;

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "queueSendRecvCompletionListenerTest3");

      // Create CompletionListener for Message to be sent
      MyCompletionListener listener = new MyCompletionListener();

      TestUtil.logMsg("Calling send(Message,CompletionListener)");
      producer.send(expTextMessage, listener);

      TestUtil.logMsg("Poll listener waiting for TestMessage to arrive");
      TextMessage actTextMessage = null;
      for (int i = 0; !listener.isComplete() && i < 60; i++) {
        TestUtil.logMsg("Loop " + i
            + ": sleep 2 seconds waiting for messages to arrive at listener");
        TestUtil.sleepSec(2);
      }
      if (listener.isComplete())
        actTextMessage = (TextMessage) listener.getMessage();

      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg("Check the value in TextMessage");
      if (actTextMessage.getText().equals(expTextMessage.getText())) {
        TestUtil.logMsg("TextMessage is correct");
      } else {
        TestUtil.logErr(
            "TextMessage is incorrect expected " + expTextMessage.getText()
                + ", received " + actTextMessage.getText());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("queueSendRecvCompletionListenerTest3", e);
    }

    if (!pass) {
      throw new Fault("queueSendRecvCompletionListenerTest3 failed");
    }
  }

  /*
   * @testName: queueSendRecvCompletionListenerTest4
   * 
   * @assertion_ids: JMS:JAVADOC:893;
   * 
   * @test_Strategy: Send a message using the following API method and verify
   * the send and recv of data:
   * 
   * MessageProducer.send(Message, int, int, long, CompletionListener)
   */
  public void queueSendRecvCompletionListenerTest4() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    try {
      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      producer = tool.getDefaultProducer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = true;

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "queueSendRecvCompletionListeneTest4");

      // Create CompletionListener for Message to be sent
      MyCompletionListener listener = new MyCompletionListener();

      TestUtil.logMsg("Calling send(Message,int,int,long,CompletionListener)");
      producer.send(expTextMessage, DeliveryMode.PERSISTENT,
          Message.DEFAULT_PRIORITY, 0L, listener);

      TestUtil.logMsg("Poll listener waiting for TestMessage to arrive");
      TextMessage actTextMessage = null;
      for (int i = 0; !listener.isComplete() && i < 60; i++) {
        TestUtil.logMsg("Loop " + i
            + ": sleep 2 seconds waiting for messages to arrive at listener");
        TestUtil.sleepSec(2);
      }
      if (listener.isComplete())
        actTextMessage = (TextMessage) listener.getMessage();

      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg(
          "Check the values in TextMessage, deliverymode, priority, time to live");
      if (!actTextMessage.getText().equals(expTextMessage.getText())
          || actTextMessage.getJMSDeliveryMode() != DeliveryMode.PERSISTENT
          || actTextMessage.getJMSPriority() != Message.DEFAULT_PRIORITY
          || actTextMessage.getJMSExpiration() != 0L) {
        TestUtil.logErr("Didn't get the right message.");
        TestUtil.logErr("text=" + actTextMessage.getText() + ", expected "
            + expTextMessage.getText());
        TestUtil.logErr("DeliveryMode=" + actTextMessage.getJMSDeliveryMode()
            + ", expected " + expTextMessage.getJMSDeliveryMode());
        TestUtil.logErr("Priority=" + actTextMessage.getJMSPriority()
            + ", expected " + expTextMessage.getJMSPriority());
        TestUtil.logErr("TimeToLive=" + actTextMessage.getJMSExpiration()
            + ", expected " + expTextMessage.getJMSExpiration());
        pass = false;
      } else {
        TestUtil.logMsg("TextMessage is correct");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("queueSendRecvCompletionListenerTest4", e);
    }

    if (!pass) {
      throw new Fault("queueSendRecvCompletionListenerTest4 failed");
    }
  }

  /*
   * @testName: queueJMSExceptionTests
   * 
   * @assertion_ids: JMS:JAVADOC:904;
   * 
   * @test_Strategy: Test for JMSException from MessageProducer API's.
   */
  public void queueJMSExceptionTests() throws Fault {
    boolean pass = true;
    TextMessage tempMsg = null;
    String message = "Where are you!";
    try {
      // set up test tool for Queue
      TestUtil.logMsg("Setup JmsTool for COMMON QUEUE");
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Queue) null);
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = true;

      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "queueJMSExceptionTests");

      // Create CompletionListener for Message to be sent
      MyCompletionListener listener = new MyCompletionListener();

      try {
        TestUtil.logMsg("Try and set an invalid mode of -1 on send");
        producer.send(destination, expTextMessage, -1, Message.DEFAULT_PRIORITY,
            0L, listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 30; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof JMSException) {
            TestUtil.logMsg("Exception is expected JMSException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected JMSException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e);
        pass = false;
      }
      try {
        TestUtil.logMsg("Try and set an invalid priority of -1 on send");
        producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT,
            -1, 0L, listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 30; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof JMSException) {
            TestUtil.logMsg("Exception is expected JMSException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected JMSException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("queueJMSExceptionTests", e);
    }

    if (!pass) {
      throw new Fault("queueJMSExceptionTests failed");
    }
  }

  /*
   * @testName: queueInvalidDestinationExceptionTests
   * 
   * @assertion_ids: JMS:JAVADOC:891; JMS:JAVADOC:896; JMS:JAVADOC:901;
   * JMS:JAVADOC:906;
   * 
   * @test_Strategy: Test for InvalidDestinationException from MessageProducer
   * API's.
   */
  public void queueInvalidDestinationExceptionTests() throws Fault {
    boolean pass = true;
    TextMessage tempMsg = null;
    String message = "Where are you!";
    try {
      // set up test tool for Queue
      TestUtil.logMsg("Setup JmsTool for COMMON QUEUE");
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      toolForProducer = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      producer = toolForProducer.getDefaultProducer();
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = null;
      connection.start();
      queueTest = true;
      MyCompletionListener listener = new MyCompletionListener();

      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "queueInvalidDestinationExceptionTests");

      try {
        TestUtil.logMsg("Send message with invalid destination");
        producer.send(destination, expTextMessage, listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 30; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof InvalidDestinationException) {
            TestUtil
                .logMsg("Exception is expected InvalidDestinationException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected InvalidDestinationException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (InvalidDestinationException e) {
        TestUtil.logMsg("Caught expected InvalidDestinationException");
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (Exception e) {
        TestUtil.logErr("Expected InvalidDestinationException, received " + e);
        pass = false;
      }
      try {
        TestUtil.logMsg("Send message with invalid destination");
        producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT,
            Message.DEFAULT_PRIORITY, 0L, listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 30; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof InvalidDestinationException) {
            TestUtil
                .logMsg("Exception is expected InvalidDestinationException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected InvalidDestinationException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (InvalidDestinationException e) {
        TestUtil.logMsg("Caught expected InvalidDestinationException");
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (Exception e) {
        TestUtil.logErr("Expected InvalidDestinationException, received " + e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("queueInvalidDestinationExceptionTests", e);
    } finally {
      try {
        producer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("queueInvalidDestinationExceptionTests failed");
    }
  }

  /*
   * @testName: queueUnsupportedOperationExceptionTests
   * 
   * @assertion_ids: JMS:JAVADOC:892; JMS:JAVADOC:897; JMS:JAVADOC:902;
   * JMS:JAVADOC:1323;
   * 
   * @test_Strategy: Test for UnsupportedOperationException from MessageProducer
   * API's.
   */
  public void queueUnsupportedOperationExceptionTests() throws Fault {
    boolean pass = true;
    TextMessage tempMsg = null;
    String message = "Where are you!";
    try {
      // set up test tool for Queue
      TestUtil.logMsg("Setup JmsTool for COMMON QUEUE");
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      producer = tool.getDefaultProducer();
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = true;
      MyCompletionListener listener = new MyCompletionListener();

      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "queueUnsupportedOperationExceptionTests");

      try {
        TestUtil.logMsg("Send message with invalid destination");
        producer.send(destination, expTextMessage, listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 30; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof UnsupportedOperationException) {
            TestUtil
                .logMsg("Exception is expected UnsupportedOperationException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected UnsupportedOperationException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (Exception e) {
        TestUtil
            .logErr("Expected UnsupportedOperationException, received " + e);
        pass = false;
      }
      try {
        TestUtil.logMsg("Send message with invalid destination");
        producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT,
            Message.DEFAULT_PRIORITY, 0L, listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 30; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof UnsupportedOperationException) {
            TestUtil
                .logMsg("Exception is expected UnsupportedOperationException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected UnsupportedOperationException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (Exception e) {
        TestUtil
            .logErr("Expected UnsupportedOperationException, received " + e);
        pass = false;
      }

      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Queue) null);

      try {
        TestUtil.logMsg("Send message with invalid destination");
        producer.send(expTextMessage, listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 30; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof UnsupportedOperationException) {
            TestUtil
                .logMsg("Exception is expected UnsupportedOperationException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected UnsupportedOperationException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (Exception e) {
        TestUtil
            .logErr("Expected UnsupportedOperationException, received " + e);
        pass = false;
      }
      try {
        TestUtil.logMsg("Send message with invalid destination");
        producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT,
            Message.DEFAULT_PRIORITY, 0L, listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 30; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof UnsupportedOperationException) {
            TestUtil
                .logMsg("Exception is expected UnsupportedOperationException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected UnsupportedOperationException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (Exception e) {
        TestUtil
            .logErr("Expected UnsupportedOperationException, received " + e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("queueUnsupportedOperationExceptionTests", e);
    } finally {
      try {
        producer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("queueUnsupportedOperationExceptionTests failed");
    }
  }

  /*
   * @testName: queueIllegalArgumentExceptionTests
   * 
   * @assertion_ids: JMS:JAVADOC:1319; JMS:JAVADOC:1320; JMS:JAVADOC:1321;
   * JMS:JAVADOC:1322;
   * 
   * @test_Strategy: Test for IllegalArgumentException from MessageProducer
   * API's.
   */
  public void queueIllegalArgumentExceptionTests() throws Fault {
    boolean pass = true;
    TextMessage tempMsg = null;
    String message = "Where are you!";
    try {
      // set up test tool for Queue
      TestUtil.logMsg("Setup JmsTool for COMMON QUEUE");
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Queue) null);
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = false;
      MyCompletionListener listener = new MyCompletionListener();

      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "queueIllegalArgumentExceptionTests");

      try {
        TestUtil.logMsg("Send message with null CompletionListener");
        producer.send(destination, expTextMessage, null);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 30; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof IllegalArgumentException) {
            TestUtil.logMsg("Exception is expected IllegalArgumentException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected IllegalArgumentException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("Caught expected IllegalArgumentException");
      } catch (Exception e) {
        TestUtil.logErr("Expected IllegalArgumentException, received " + e);
        pass = false;
      }
      try {
        TestUtil.logMsg("Send message with null CompletionListener");
        producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT,
            Message.DEFAULT_PRIORITY, 0L, null);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 30; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof IllegalArgumentException) {
            TestUtil.logMsg("Exception is expected IllegalArgumentException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected IllegalArgumentException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("Caught expected IllegalArgumentException");
      } catch (Exception e) {
        TestUtil.logErr("Expected IllegalArgumentException, received " + e);
        pass = false;
      }

      producer.close();
      producer = tool.getDefaultSession().createProducer(destination);

      try {
        TestUtil.logMsg("Send message with null CompletionListener");
        producer.send(expTextMessage, null);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 30; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof IllegalArgumentException) {
            TestUtil.logMsg("Exception is expected IllegalArgumentException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected IllegalArgumentException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("Caught expected IllegalArgumentException");
      } catch (Exception e) {
        TestUtil.logErr("Expected IllegalArgumentException, received " + e);
        pass = false;
      }
      try {
        TestUtil.logMsg("Send message with null CompletionListener");
        producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT,
            Message.DEFAULT_PRIORITY, 0L, null);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 30; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof IllegalArgumentException) {
            TestUtil.logMsg("Exception is expected IllegalArgumentException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected IllegalArgumentException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("Caught expected IllegalArgumentException");
      } catch (Exception e) {
        TestUtil.logErr("Expected IllegalArgumentException, received " + e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("queueIllegalArgumentExceptionTests", e);
    } finally {
      try {
        producer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("queueIllegalArgumentExceptionTests failed");
    }
  }

  /*
   * @testName: queueMessageFormatExceptionTests
   * 
   * @assertion_ids: JMS:JAVADOC:890; JMS:JAVADOC:895; JMS:JAVADOC:900;
   * JMS:JAVADOC:905;
   * 
   * @test_Strategy: Test MessageFormatException conditions from API methods
   * with CompletionListener.
   * 
   * MessageProducer.send(Message, CompletionListener)
   * MessageProducer.send(Message, int, int, long, CompletionListener)
   * MessageProducer.send(Destination, Message, CompletionListener)
   * MessageProducer.send(Destination, Message, int, int, long,
   * CompletionListener)
   * 
   * Tests the following exception conditions:
   * 
   * MessageFormatException
   */
  public void queueMessageFormatExceptionTests() throws Fault {
    boolean pass = true;
    try {
      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      producer = tool.getDefaultProducer();
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = true;

      TextMessage invalidTMsg = new InvalidTextMessageTestImpl();
      invalidTMsg.setText("hello");

      // Create CompletionListener
      MyCompletionListener listener = new MyCompletionListener();

      try {
        TestUtil.logMsg(
            "Calling MessageProducer.send(Message, CompletionListener) -> expect MessageFormatException");
        producer.send(invalidTMsg, listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 30; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof MessageFormatException) {
            TestUtil.logMsg("Exception is expected MessageFormatException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected MessageFormatException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (MessageFormatException e) {
        TestUtil.logMsg("Got MessageFormatException as expected.");
      } catch (Exception e) {
        TestUtil.logErr("Expected MessageFormatException, received " + e);
        pass = false;
      }
      try {
        TestUtil.logMsg(
            "Calling MessageProducer.send(Message, int, int, long, CompletionListener) -> expect MessageFormatException");
        producer.send(invalidTMsg, Message.DEFAULT_DELIVERY_MODE,
            Message.DEFAULT_PRIORITY - 1, Message.DEFAULT_TIME_TO_LIVE,
            listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 30; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof MessageFormatException) {
            TestUtil.logMsg("Exception is expected MessageFormatException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected MessageFormatException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (MessageFormatException e) {
        TestUtil.logMsg("Got MessageFormatException as expected.");
      } catch (Exception e) {
        TestUtil.logErr("Expected MessageFormatException, received " + e);
        pass = false;
      }

      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Queue) null);

      try {
        TestUtil.logMsg(
            "Calling MessageProducer.send(Destination, Message, CompletionListener) -> expect MessageFormatException");
        producer.send(destination, invalidTMsg, listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 30; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof MessageFormatException) {
            TestUtil.logMsg("Exception is expected MessageFormatException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected MessageFormatException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (MessageFormatException e) {
        TestUtil.logMsg("Got MessageFormatException as expected.");
      } catch (Exception e) {
        TestUtil.logErr("Expected MessageFormatException, received " + e);
        pass = false;
      }
      try {
        TestUtil.logMsg(
            "Calling MessageProducer.send(Destination, Message, int, int, long, CompletionListener) -> expect MessageFormatException");
        producer.send(destination, invalidTMsg, Message.DEFAULT_DELIVERY_MODE,
            Message.DEFAULT_PRIORITY - 1, Message.DEFAULT_TIME_TO_LIVE,
            listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 30; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof MessageFormatException) {
            TestUtil.logMsg("Exception is expected MessageFormatException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected MessageFormatException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (MessageFormatException e) {
        TestUtil.logMsg("Got MessageFormatException as expected.");
      } catch (Exception e) {
        TestUtil.logErr("Expected MessageFormatException, received " + e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      throw new Fault("queueMessageFormatExceptionTests", e);
    }

    if (!pass) {
      throw new Fault("queueMessageFormatExceptionTests failed");
    }
  }

  /*
   * @testName: queueIllegalStateExceptionTests
   * 
   * @assertion_ids: JMS:JAVADOC:1355;
   * 
   * @test_Strategy: Test IllegalStateException conditions. Calling
   * MessageProducer.close() in CompletionListener MUST throw
   * IllegalStateException.
   */
  public void queueIllegalStateExceptionTests() throws Fault {
    boolean pass = true;
    try {
      // set up test tool for Queue
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Queue) null);
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = true;

      // Create CompetionListener
      MyCompletionListener listener = new MyCompletionListener(producer);

      TestUtil.logMsg(
          "Testing MessageProducer.close() from CompletionListener (expect IllegalStateException)");
      try {
        // Create TextMessage
        TestUtil.logMsg("Creating TextMessage");
        TextMessage expTextMessage = session
            .createTextMessage("Call close method");
        TestUtil.logMsg("Set some values in TextMessage");
        expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
            "queueIllegalStateExceptionTests");

        TestUtil.logMsg(
            "Send async message specifying CompletionListener to recieve async message");
        TestUtil.logMsg(
            "CompletionListener will call MessageProducer.close() (expect IllegalStateException)");
        producer.send(destination, expTextMessage, listener);
        TextMessage actTextMessage = null;
        TestUtil.logMsg("Poll listener until we receive exception");
        for (int i = 0; i < 30; i++) {
          if (listener.isComplete()) {
            listener.setComplete(false);
            break;
          } else {
            TestUtil.sleepSec(2);
          }
        }
        TestUtil.logMsg(
            "Check if we got correct exception from MessageProducer.close()");
        if (listener.gotException()) {
          if (listener.gotCorrectException()) {
            TestUtil.logMsg("Got correct IllegalStateException");
          } else {
            TestUtil.logErr("Expected IllegalStateException, received: "
                + listener.getException());
            pass = false;
          }
        } else {
          TestUtil.logErr("Expected IllegalStateException, got no exception");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Expected IllegalStateException, received " + e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      throw new Fault("queueIllegalStateExceptionTests", e);
    }

    if (!pass) {
      throw new Fault("queueIllegalStateExceptionTests failed");
    }
  }

  /*
   * @testName: topicSendRecvCompletionListenerTest1
   * 
   * @assertion_ids: JMS:JAVADOC:898;
   * 
   * @test_Strategy: Send a message using the following API method and verify
   * the send and recv of data:
   * 
   * MessageProducer.send(Destination, Message, CompletionListener)
   */
  public void topicSendRecvCompletionListenerTest1() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    try {
      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Topic) null);
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = false;

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "topicSendRecvCompletionListenerTest1");

      // Create CompletionListener for Message to be sent
      MyCompletionListener listener = new MyCompletionListener();

      TestUtil.logMsg("Calling send(Destination,Message,CompletionListener)");
      producer.send(destination, expTextMessage, listener);

      TestUtil.logMsg("Poll listener waiting for TestMessage to arrive");
      TextMessage actTextMessage = null;
      for (int i = 0; !listener.isComplete() && i < 60; i++) {
        TestUtil.logMsg("Loop " + i
            + ": sleep 2 seconds waiting for messages to arrive at listener");
        TestUtil.sleepSec(2);
      }
      if (listener.isComplete())
        actTextMessage = (TextMessage) listener.getMessage();

      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg("Check the value in TextMessage");
      if (actTextMessage.getText().equals(expTextMessage.getText())) {
        TestUtil.logMsg("TextMessage is correct");
      } else {
        TestUtil.logErr(
            "TextMessage is incorrect expected " + expTextMessage.getText()
                + ", received " + actTextMessage.getText());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("topicSendRecvCompletionListenerTest1", e);
    } finally {
      try {
        producer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("topicSendRecvCompletionListenerTest1 failed");
    }
  }

  /*
   * @testName: topicSendRecvCompletionListenerTest2
   * 
   * @assertion_ids: JMS:JAVADOC:903;
   * 
   * @test_Strategy: Send a message using the following API method and verify
   * the send and recv of data:
   * 
   * MessageProducer.send(Destination, Message, int, int, long,
   * CompletionListener)
   */
  public void topicSendRecvCompletionListenerTest2() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    try {
      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Topic) null);
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = false;

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "topicSendRecvCompletionListenerTest2");

      // Create CompletionListener for Message to be sent
      MyCompletionListener listener = new MyCompletionListener();

      TestUtil.logMsg(
          "Calling send(Destination,Message,int,int,long,CompletionListener)");
      producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT,
          Message.DEFAULT_PRIORITY, 0L, listener);

      TestUtil.logMsg("Poll listener waiting for TestMessage to arrive");
      TextMessage actTextMessage = null;
      for (int i = 0; !listener.isComplete() && i < 60; i++) {
        TestUtil.logMsg("Loop " + i
            + ": sleep 2 seconds waiting for messages to arrive at listener");
        TestUtil.sleepSec(2);
      }
      if (listener.isComplete())
        actTextMessage = (TextMessage) listener.getMessage();

      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg(
          "Check the values in TextMessage, deliverymode, priority, time to live");
      if (!actTextMessage.getText().equals(expTextMessage.getText())
          || actTextMessage.getJMSDeliveryMode() != DeliveryMode.NON_PERSISTENT
          || actTextMessage.getJMSPriority() != Message.DEFAULT_PRIORITY
          || actTextMessage.getJMSExpiration() != 0L) {
        TestUtil.logErr("Didn't get the right message.");
        TestUtil.logErr("text=" + actTextMessage.getText() + ", expected "
            + expTextMessage.getText());
        TestUtil.logErr("DeliveryMode=" + actTextMessage.getJMSDeliveryMode()
            + ", expected " + expTextMessage.getJMSDeliveryMode());
        TestUtil.logErr("Priority=" + actTextMessage.getJMSPriority()
            + ", expected " + expTextMessage.getJMSPriority());
        TestUtil.logErr("TimeToLive=" + actTextMessage.getJMSExpiration()
            + ", expected " + expTextMessage.getJMSExpiration());
        pass = false;
      } else {
        TestUtil.logMsg("TextMessage is correct");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("topicSendRecvCompletionListenerTest2", e);
    } finally {
      try {
        producer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("topicSendRecvCompletionListenerTest2 failed");
    }
  }

  /*
   * @testName: topicSendRecvCompletionListenerTest3
   * 
   * @assertion_ids: JMS:JAVADOC:888;
   * 
   * @test_Strategy: Send a message using the following API method and verify
   * the send and recv of data:
   * 
   * MessageProducer.send(Message, CompletionListener)
   */
  public void topicSendRecvCompletionListenerTest3() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    try {
      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      producer = tool.getDefaultProducer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = false;

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "topicSendRecvCompletionListenerTest3");

      // Create CompletionListener for Message to be sent
      MyCompletionListener listener = new MyCompletionListener();

      TestUtil.logMsg("Calling send(Message,CompletionListener)");
      producer.send(expTextMessage, listener);

      TestUtil.logMsg("Poll listener waiting for TestMessage to arrive");
      TextMessage actTextMessage = null;
      for (int i = 0; !listener.isComplete() && i < 60; i++) {
        TestUtil.logMsg("Loop " + i
            + ": sleep 2 seconds waiting for messages to arrive at listener");
        TestUtil.sleepSec(2);
      }
      if (listener.isComplete())
        actTextMessage = (TextMessage) listener.getMessage();

      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg("Check the value in TextMessage");
      if (actTextMessage.getText().equals(expTextMessage.getText())) {
        TestUtil.logMsg("TextMessage is correct");
      } else {
        TestUtil.logErr(
            "TextMessage is incorrect expected " + expTextMessage.getText()
                + ", received " + actTextMessage.getText());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("topicSendRecvCompletionListenerTest3", e);
    }

    if (!pass) {
      throw new Fault("topicSendRecvCompletionListenerTest3 failed");
    }
  }

  /*
   * @testName: topicSendRecvCompletionListenerTest4
   * 
   * @assertion_ids: JMS:JAVADOC:893;
   * 
   * @test_Strategy: Send a message using the following API method and verify
   * the send and recv of data:
   * 
   * MessageProducer.send(Message, int, int, long, CompletionListener)
   */
  public void topicSendRecvCompletionListenerTest4() throws Fault {
    boolean pass = true;
    String message = "Where are you!";
    try {
      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      producer = tool.getDefaultProducer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = false;

      // send and receive TextMessage
      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "topicSendRecvCompletionListeneTest4");

      // Create CompletionListener for Message to be sent
      MyCompletionListener listener = new MyCompletionListener();

      TestUtil.logMsg("Calling send(Message,int,int,long,CompletionListener)");
      producer.send(expTextMessage, DeliveryMode.PERSISTENT,
          Message.DEFAULT_PRIORITY, 0L, listener);

      TestUtil.logMsg("Poll listener waiting for TestMessage to arrive");
      TextMessage actTextMessage = null;
      for (int i = 0; !listener.isComplete() && i < 60; i++) {
        TestUtil.logMsg("Loop " + i
            + ": sleep 2 seconds waiting for messages to arrive at listener");
        TestUtil.sleepSec(2);
      }
      if (listener.isComplete())
        actTextMessage = (TextMessage) listener.getMessage();

      if (actTextMessage == null) {
        throw new Fault("Did not receive TextMessage");
      }
      TestUtil.logMsg(
          "Check the values in TextMessage, deliverymode, priority, time to live");
      if (!actTextMessage.getText().equals(expTextMessage.getText())
          || actTextMessage.getJMSDeliveryMode() != DeliveryMode.PERSISTENT
          || actTextMessage.getJMSPriority() != Message.DEFAULT_PRIORITY
          || actTextMessage.getJMSExpiration() != 0L) {
        TestUtil.logErr("Didn't get the right message.");
        TestUtil.logErr("text=" + actTextMessage.getText() + ", expected "
            + expTextMessage.getText());
        TestUtil.logErr("DeliveryMode=" + actTextMessage.getJMSDeliveryMode()
            + ", expected " + expTextMessage.getJMSDeliveryMode());
        TestUtil.logErr("Priority=" + actTextMessage.getJMSPriority()
            + ", expected " + expTextMessage.getJMSPriority());
        TestUtil.logErr("TimeToLive=" + actTextMessage.getJMSExpiration()
            + ", expected " + expTextMessage.getJMSExpiration());
        pass = false;
      } else {
        TestUtil.logMsg("TextMessage is correct");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("topicSendRecvCompletionListenerTest4", e);
    }

    if (!pass) {
      throw new Fault("topicSendRecvCompletionListenerTest4 failed");
    }
  }

  /*
   * @testName: topicJMSExceptionTests
   * 
   * @assertion_ids: JMS:JAVADOC:904;
   * 
   * @test_Strategy: Test for JMSException from MessageProducer API's.
   */
  public void topicJMSExceptionTests() throws Fault {
    boolean pass = true;
    TextMessage tempMsg = null;
    String message = "Where are you!";
    try {
      // set up test tool for Queue
      TestUtil.logMsg("Setup JmsTool for COMMON TOPIC");
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Topic) null);
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = false;

      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "topicJMSExceptionTests");

      // Create CompletionListener for Message to be sent
      MyCompletionListener listener = new MyCompletionListener();

      try {
        TestUtil.logMsg("Try and set an invalid mode of -1 on send");
        producer.send(destination, expTextMessage, -1, Message.DEFAULT_PRIORITY,
            0L, listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 10; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof JMSException) {
            TestUtil.logMsg("Exception is expected JMSException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected JMSException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e);
        pass = false;
      }
      try {
        TestUtil.logMsg("Try and set an invalid priority of -1 on send");
        producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT,
            -1, 0L, listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 10; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof JMSException) {
            TestUtil.logMsg("Exception is expected JMSException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected JMSException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (JMSException e) {
        TestUtil.logMsg("Caught expected JMSException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSException, received " + e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("topicJMSExceptionTests", e);
    }

    if (!pass) {
      throw new Fault("topicJMSExceptionTests failed");
    }
  }

  /*
   * @testName: topicInvalidDestinationExceptionTests
   * 
   * @assertion_ids: JMS:JAVADOC:891; JMS:JAVADOC:896; JMS:JAVADOC:901;
   * JMS:JAVADOC:906;
   * 
   * @test_Strategy: Test for InvalidDestinationException from MessageProducer
   * API's.
   */
  public void topicInvalidDestinationExceptionTests() throws Fault {
    boolean pass = true;
    TextMessage tempMsg = null;
    String message = "Where are you!";
    try {
      // set up test tool for Topic
      TestUtil.logMsg("Setup JmsTool for COMMON TOPIC");
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      toolForProducer = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      producer = toolForProducer.getDefaultProducer();
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = null;
      connection.start();
      queueTest = false;
      MyCompletionListener listener = new MyCompletionListener();

      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "queueInvalidDestinationExceptionTests");

      try {
        TestUtil.logMsg("Send message with invalid destination");
        producer.send(destination, expTextMessage, listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 10; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof InvalidDestinationException) {
            TestUtil
                .logMsg("Exception is expected InvalidDestinationException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected InvalidDestinationException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (InvalidDestinationException e) {
        TestUtil.logMsg("Caught expected InvalidDestinationException");
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (Exception e) {
        TestUtil.logErr("Expected InvalidDestinationException, received " + e);
        pass = false;
      }
      try {
        TestUtil.logMsg("Send message with invalid destination");
        producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT,
            Message.DEFAULT_PRIORITY, 0L, listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 10; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof InvalidDestinationException) {
            TestUtil
                .logMsg("Exception is expected InvalidDestinationException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected InvalidDestinationException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (InvalidDestinationException e) {
        TestUtil.logMsg("Caught expected InvalidDestinationException");
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (Exception e) {
        TestUtil.logErr("Expected InvalidDestinationException, received " + e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("queueInvalidDestinationExceptionTests", e);
    } finally {
      try {
        producer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("topicInvalidDestinationExceptionTests failed");
    }
  }

  /*
   * @testName: topicUnsupportedOperationExceptionTests
   * 
   * @assertion_ids: JMS:JAVADOC:892; JMS:JAVADOC:897; JMS:JAVADOC:902;
   * JMS:JAVADOC:1323;
   * 
   * @test_Strategy: Test for UnsupportedOperationException from MessageProducer
   * API's.
   */
  public void topicUnsupportedOperationExceptionTests() throws Fault {
    boolean pass = true;
    TextMessage tempMsg = null;
    String message = "Where are you!";
    try {
      // set up test tool for Topic
      TestUtil.logMsg("Setup JmsTool for COMMON TOPIC");
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      producer = tool.getDefaultProducer();
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = false;
      MyCompletionListener listener = new MyCompletionListener();

      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "topicUnsupportedOperationExceptionTests");

      try {
        TestUtil.logMsg("Send message with invalid destination");
        producer.send(destination, expTextMessage, listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 10; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof UnsupportedOperationException) {
            TestUtil
                .logMsg("Exception is expected UnsupportedOperationException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected UnsupportedOperationException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (Exception e) {
        TestUtil
            .logErr("Expected UnsupportedOperationException, received " + e);
        pass = false;
      }
      try {
        TestUtil.logMsg("Send message with invalid destination");
        producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT,
            Message.DEFAULT_PRIORITY, 0L, listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 10; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof UnsupportedOperationException) {
            TestUtil
                .logMsg("Exception is expected UnsupportedOperationException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected UnsupportedOperationException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (Exception e) {
        TestUtil
            .logErr("Expected UnsupportedOperationException, received " + e);
        pass = false;
      }

      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Topic) null);

      try {
        TestUtil.logMsg("Send message with invalid destination");
        producer.send(expTextMessage, listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 10; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof UnsupportedOperationException) {
            TestUtil
                .logMsg("Exception is expected UnsupportedOperationException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected UnsupportedOperationException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (Exception e) {
        TestUtil
            .logErr("Expected UnsupportedOperationException, received " + e);
        pass = false;
      }
      try {
        TestUtil.logMsg("Send message with invalid destination");
        producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT,
            Message.DEFAULT_PRIORITY, 0L, listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 10; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof UnsupportedOperationException) {
            TestUtil
                .logMsg("Exception is expected UnsupportedOperationException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected UnsupportedOperationException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (UnsupportedOperationException e) {
        TestUtil.logMsg("Caught expected UnsupportedOperationException");
      } catch (Exception e) {
        TestUtil
            .logErr("Expected UnsupportedOperationException, received " + e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("queueUnsupportedOperationExceptionTests", e);
    } finally {
      try {
        producer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("topicUnsupportedOperationExceptionTests failed");
    }
  }

  /*
   * @testName: topicIllegalArgumentExceptionTests
   * 
   * @assertion_ids: JMS:JAVADOC:1319; JMS:JAVADOC:1320; JMS:JAVADOC:1321;
   * JMS:JAVADOC:1322;
   * 
   * @test_Strategy: Test for IllegalArgumentException from MessageProducer
   * API's.
   */
  public void topicIllegalArgumentExceptionTests() throws Fault {
    boolean pass = true;
    TextMessage tempMsg = null;
    String message = "Where are you!";
    try {
      // set up test tool for Topic
      TestUtil.logMsg("Setup JmsTool for COMMON TOPIC");
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Topic) null);
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = false;
      MyCompletionListener listener = new MyCompletionListener();

      TestUtil.logMsg("Creating TextMessage");
      TextMessage expTextMessage = session.createTextMessage(message);
      TestUtil.logMsg("Set some values in TextMessage");
      expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
          "topicIllegalArgumentExceptionTests");

      try {
        TestUtil.logMsg("Send message with null CompletionListener");
        producer.send(destination, expTextMessage, null);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 10; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof IllegalArgumentException) {
            TestUtil.logMsg("Exception is expected IllegalArgumentException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected IllegalArgumentException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("Caught expected IllegalArgumentException");
      } catch (Exception e) {
        TestUtil.logErr("Expected IllegalArgumentException, received " + e);
        pass = false;
      }
      try {
        TestUtil.logMsg("Send message with null CompletionListener");
        producer.send(destination, expTextMessage, DeliveryMode.NON_PERSISTENT,
            Message.DEFAULT_PRIORITY, 0L, null);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 10; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof IllegalArgumentException) {
            TestUtil.logMsg("Exception is expected IllegalArgumentException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected IllegalArgumentException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("Caught expected IllegalArgumentException");
      } catch (Exception e) {
        TestUtil.logErr("Expected IllegalArgumentException, received " + e);
        pass = false;
      }

      producer.close();
      producer = tool.getDefaultSession().createProducer(destination);

      try {
        TestUtil.logMsg("Send message with null CompletionListener");
        producer.send(expTextMessage, null);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 10; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof IllegalArgumentException) {
            TestUtil.logMsg("Exception is expected IllegalArgumentException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected IllegalArgumentException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("Caught expected IllegalArgumentException");
      } catch (Exception e) {
        TestUtil.logErr("Expected IllegalArgumentException, received " + e);
        pass = false;
      }
      try {
        TestUtil.logMsg("Send message with null CompletionListener");
        producer.send(expTextMessage, DeliveryMode.NON_PERSISTENT,
            Message.DEFAULT_PRIORITY, 0L, null);
        TestUtil.logMsg("Poll listener until we receive exception");
        Exception exception = null;
        for (int i = 0; i < 10; i++) {
          TestUtil.sleepSec(2);
          if (listener.isComplete()) {
            listener.setComplete(false);
            exception = listener.getException();
            TestUtil.logMsg("Received Exception after polling loop " + (i + 1));
            break;
          }
          TestUtil.logMsg("Completed polling loop " + i);
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof IllegalArgumentException) {
            TestUtil.logMsg("Exception is expected IllegalArgumentException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected IllegalArgumentException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (IllegalArgumentException e) {
        TestUtil.logMsg("Caught expected IllegalArgumentException");
      } catch (Exception e) {
        TestUtil.logErr("Expected IllegalArgumentException, received " + e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("queueIllegalArgumentExceptionTests", e);
    } finally {
      try {
        producer.close();
      } catch (Exception e) {
      }
    }

    if (!pass) {
      throw new Fault("topicIllegalArgumentExceptionTests failed");
    }
  }

  /*
   * @testName: topicMessageFormatExceptionTests
   * 
   * @assertion_ids: JMS:JAVADOC:890; JMS:JAVADOC:895; JMS:JAVADOC:900;
   * JMS:JAVADOC:905;
   * 
   * @test_Strategy: Test MessageFormatException conditions from API methods
   * with CompletionListener.
   * 
   * MessageProducer.send(Message, CompletionListener)
   * MessageProducer.send(Message, int, int, long, CompletionListener)
   * MessageProducer.send(Destination, Message, CompletionListener)
   * MessageProducer.send(Destination, Message, int, int, long,
   * CompletionListener)
   * 
   * Tests the following exception conditions:
   * 
   * MessageFormatException
   */
  public void topicMessageFormatExceptionTests() throws Fault {
    boolean pass = true;
    try {
      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      producer = tool.getDefaultProducer();
      consumer = tool.getDefaultConsumer();
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = false;

      TextMessage invalidTMsg = new InvalidTextMessageTestImpl();
      invalidTMsg.setText("hello");

      // Create CompletionListener
      MyCompletionListener listener = new MyCompletionListener();

      try {
        TestUtil.logMsg(
            "Calling MessageProducer.send(Message, CompletionListener) -> expect MessageFormatException");
        producer.send(invalidTMsg, listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        TestUtil.sleepSec(5);
        Exception exception = null;
        if (listener.isComplete()) {
          listener.setComplete(false);
          exception = listener.getException();
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof MessageFormatException) {
            TestUtil.logMsg("Exception is expected MessageFormatException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected MessageFormatException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (MessageFormatException e) {
        TestUtil.logMsg("Got MessageFormatException as expected.");
      } catch (Exception e) {
        TestUtil.logErr("Expected MessageFormatException, received " + e);
        pass = false;
      }
      try {
        TestUtil.logMsg(
            "Calling MessageProducer.send(Message, int, int, long, CompletionListener) -> expect MessageFormatException");
        producer.send(invalidTMsg, Message.DEFAULT_DELIVERY_MODE,
            Message.DEFAULT_PRIORITY - 1, Message.DEFAULT_TIME_TO_LIVE,
            listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        TestUtil.sleepSec(5);
        Exception exception = null;
        if (listener.isComplete()) {
          listener.setComplete(false);
          exception = listener.getException();
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof MessageFormatException) {
            TestUtil.logMsg("Exception is expected MessageFormatException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected MessageFormatException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (MessageFormatException e) {
        TestUtil.logMsg("Got MessageFormatException as expected.");
      } catch (Exception e) {
        TestUtil.logErr("Expected MessageFormatException, received " + e);
        pass = false;
      }

      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Queue) null);

      try {
        TestUtil.logMsg(
            "Calling MessageProducer.send(Destination, Message, CompletionListener) -> expect MessageFormatException");
        producer.send(destination, invalidTMsg, listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        TestUtil.sleepSec(5);
        Exception exception = null;
        if (listener.isComplete()) {
          listener.setComplete(false);
          exception = listener.getException();
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof MessageFormatException) {
            TestUtil.logMsg("Exception is expected MessageFormatException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected MessageFormatException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (MessageFormatException e) {
        TestUtil.logMsg("Got MessageFormatException as expected.");
      } catch (Exception e) {
        TestUtil.logErr("Expected MessageFormatException, received " + e);
        pass = false;
      }
      try {
        TestUtil.logMsg(
            "Calling MessageProducer.send(Destination, Message, int, int, long, CompletionListener) -> expect MessageFormatException");
        producer.send(destination, invalidTMsg, Message.DEFAULT_DELIVERY_MODE,
            Message.DEFAULT_PRIORITY - 1, Message.DEFAULT_TIME_TO_LIVE,
            listener);
        TestUtil.logMsg("Poll listener until we receive exception");
        TestUtil.sleepSec(5);
        Exception exception = null;
        if (listener.isComplete()) {
          listener.setComplete(false);
          exception = listener.getException();
        }

        if (exception == null) {
          pass = false;
          TestUtil.logErr("Didn't throw and exception");
        } else {
          TestUtil.logMsg("Check the value in Exception");
          if (exception instanceof MessageFormatException) {
            TestUtil.logMsg("Exception is expected MessageFormatException");
          } else {
            TestUtil.logErr(
                "Exception is incorrect expected MessageFormatException, received "
                    + exception.getCause());
            pass = false;
          }
        }
      } catch (MessageFormatException e) {
        TestUtil.logMsg("Got MessageFormatException as expected.");
      } catch (Exception e) {
        TestUtil.logErr("Expected MessageFormatException, received " + e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      throw new Fault("topicMessageFormatExceptionTests", e);
    }

    if (!pass) {
      throw new Fault("topicMessageFormatExceptionTests failed");
    }
  }

  /*
   * @testName: topicIllegalStateExceptionTests
   * 
   * @assertion_ids: JMS:JAVADOC:1355;
   * 
   * @test_Strategy: Test IllegalStateException conditions. Calling
   * MessageProducer.close() in CompletionListener MUST throw
   * IllegalStateException.
   */
  public void topicIllegalStateExceptionTests() throws Fault {
    boolean pass = true;
    try {
      // set up test tool for Topic
      tool = new JmsTool(JmsTool.COMMON_T, user, password, mode);
      tool.getDefaultProducer().close();
      producer = tool.getDefaultSession().createProducer((Topic) null);
      connection = tool.getDefaultConnection();
      session = tool.getDefaultSession();
      destination = tool.getDefaultDestination();
      connection.start();
      queueTest = false;

      // Create CompetionListener
      MyCompletionListener listener = new MyCompletionListener(producer);

      TestUtil.logMsg(
          "Testing MessageProducer.close() from CompletionListener (expect IllegalStateException)");
      try {
        // Create TextMessage
        TestUtil.logMsg("Creating TextMessage");
        TextMessage expTextMessage = session
            .createTextMessage("Call close method");
        TestUtil.logMsg("Set some values in TextMessage");
        expTextMessage.setStringProperty("COM_SUN_JMS_TESTNAME",
            "topicIllegalStateExceptionTests");

        TestUtil.logMsg(
            "Send async message specifying CompletionListener to recieve async message");
        TestUtil.logMsg(
            "CompletionListener will call MessageProducer.close() (expect IllegalStateException)");
        producer.send(destination, expTextMessage, listener);
        TextMessage actTextMessage = null;
        TestUtil.logMsg("Poll listener until we receive message or exception");
        for (int i = 0; i < 10; i++) {
          if (listener.isComplete()) {
            listener.setComplete(false);
            break;
          } else {
            TestUtil.sleepSec(2);
          }
        }
        TestUtil.logMsg(
            "Check if we got correct exception from MessageProducer.close()");
        if (listener.gotException()) {
          if (listener.gotCorrectException()) {
            TestUtil.logMsg("Got correct IllegalStateException");
          } else {
            TestUtil.logErr("Expected IllegalStateException, received: "
                + listener.getException());
            pass = false;
          }
        } else {
          TestUtil.logErr("Expected IllegalStateException, got no exception");
          pass = false;
        }
      } catch (Exception e) {
        TestUtil.logErr("Expected IllegalStateException, received " + e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e);
      throw new Fault("topicIllegalStateExceptionTests", e);
    }

    if (!pass) {
      throw new Fault("topicIllegalStateExceptionTests failed");
    }
  }
}
