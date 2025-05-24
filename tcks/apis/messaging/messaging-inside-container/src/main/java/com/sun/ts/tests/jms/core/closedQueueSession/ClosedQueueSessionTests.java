/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jms.core.closedQueueSession;

import java.util.ArrayList;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;
import com.sun.ts.tests.jms.common.MessageTestImpl;

import jakarta.jms.BytesMessage;
import jakarta.jms.MapMessage;
import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import jakarta.jms.Queue;
import jakarta.jms.QueueBrowser;
import jakarta.jms.QueueReceiver;
import jakarta.jms.QueueSender;
import jakarta.jms.StreamMessage;
import jakarta.jms.TemporaryQueue;
import jakarta.jms.TextMessage;

/**
 * JMS TS tests. Testing method calls on closed QueueSession objects.
 */
public class ClosedQueueSessionTests extends ServiceEETest {
  private static final String TestName = "com.sun.ts.tests.jms.core.closedQueueSession.ClosedQueueSessionTests";

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
   */
  public static void main(String[] args) {
    ClosedQueueSessionTests theTests = new ClosedQueueSessionTests();
    Status s = theTests.run(args, System.out, System.err);

    s.exit();
  }

  /* Utility methods for tests */

  /**
   * Used by tests that need a closed session for testing. Passes any exceptions
   * up to caller.
   * 
   * @param int
   *          The type of session that needs to be created and closed
   */
  private void createAndCloseSession(int type, String user, String password)
      throws Exception {
    if ((type == JmsTool.QUEUE) || (type == JmsTool.TX_QUEUE)) {
      tool = new JmsTool(type, user, password, mode);
      tool.getDefaultQueueConnection().start();
      logMsg("Closing QueueSession");
      tool.getDefaultQueueSession().close();

    } else if (type == JmsTool.COMMON_QTX) {
      tool = new JmsTool(type, user, password, mode);
      tool.getDefaultConnection().start();
      logMsg("Closing Session");
      tool.getDefaultSession().close();
    } else {
      logErr("Unspecified type");
    }
  }

  /* Test setup: */

  /*
   * setup() is called before each test
   * 
   * Creates Administrator object and deletes all previous Destinations.
   * Individual tests create the JmsTool object with one default
   * QueueConnection, as well as a default Queue. Tests that require multiple
   * Destinations create the extras within the test
   * 
   * 
   * @class.setup_props: jms_timeout; user; password; platform.mode;
   * 
   * @exception Fault
   */

  public void setup(String[] args, Properties p) throws Exception {
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
      queues = new ArrayList(2);

      // get ready for new test
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Exception("Setup failed!", e);
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

  public void cleanup() throws Exception {
    try {
      if (tool != null) {
        logMsg("Cleanup: Closing QueueConnection");
        tool.doClientQueueTestCleanup(connections, queues);
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      logErr("An error occurred while cleaning");
      throw new Exception("Cleanup failed!", e);
    }
  }

  /* Tests */

  /*
   * @testName: closedQueueSessionCloseTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:229;
   * 
   * @test_Strategy: Close default session and call close method on it.
   */

  public void closedQueueSessionCloseTest() throws Exception {
    try {
      TestUtil.logTrace("Before create and close");
      createAndCloseSession(JmsTool.QUEUE, user, password);

      logMsg("Try to call close on closed session.");
      tool.getDefaultQueueSession().close();
    } catch (Exception e) {
      TestUtil.logTrace("fault " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedQueueSessionCloseTest");
    }
  }

  /*
   * @testName: closedQueueSessionCreateBrowserTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:190;
   * 
   * @test_Strategy: Close default session and call createBrowser on it. Check
   * for IllegalStateException.
   */

  public void closedQueueSessionCreateBrowserTest() throws Exception {
    String testName = "closedQueueSessionCreateBrowserTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create QueueBrowser with closed session.");
      try {
        QueueBrowser qB = tool.getDefaultQueueSession()
            .createBrowser(tool.getDefaultQueue());
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Exception(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      throw new Exception("closedQueueSessionCreateBrowserTest", e);
    }
  }

  /*
   * @testName: closedQueueSessionCreateBrowserMsgSelectorTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:192;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedQueueSessionCreateBrowserMsgSelectorTest() throws Exception {
    String testName = "closedQueueSessionCreateBrowserMsgSelectorTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create QueueBrowser with closed session.");
      try {
        QueueBrowser qB = tool.getDefaultQueueSession()
            .createBrowser(tool.getDefaultQueue(), "TEST = 'test'");
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Exception(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedQueueSessionCreateBrowserMsgSelectorTest");
    }
  }

  /*
   * @testName: closedQueueSessionCreateQueueTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:182;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedQueueSessionCreateQueueTest() throws Exception {
    String testName = "closedQueueSessionCreateQueueTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create queue with closed session.");
      try {
        Queue q = tool.getDefaultQueueSession().createQueue(testName);
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Exception(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedQueueSessionCreateQueueTest");
    }
  }

  /*
   * @testName: closedQueueSessionCreateReceiverTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:184;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedQueueSessionCreateReceiverTest() throws Exception {
    String testName = "closedQueueSessionCreateReceiverTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create Receiver with closed session.");
      try {
        QueueReceiver qR = tool.getDefaultQueueSession()
            .createReceiver(tool.getDefaultQueue());
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Exception(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedQueueSessionCreateReceiverTest");
    }
  }

  /*
   * @testName: closedQueueSessionCreateReceiverMsgSelectorTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:186;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedQueueSessionCreateReceiverMsgSelectorTest() throws Exception {
    String testName = "closedQueueSessionCreateReceiverMsgSelectorTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create Receiver with closed session.");
      try {
        QueueReceiver qR = tool.getDefaultQueueSession()
            .createReceiver(tool.getDefaultQueue(), "TEST = 'test'");
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Exception(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedQueueSessionCreateReceiverMsgSelectorTest");
    }
  }

  /*
   * @testName: closedQueueSessionCreateSenderTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:188;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedQueueSessionCreateSenderTest() throws Exception {
    String testName = "closedQueueSessionCreateSenderTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create Sender with closed session.");
      try {
        QueueSender qS = tool.getDefaultQueueSession()
            .createSender(tool.getDefaultQueue());
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Exception(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedQueueSessionCreateSenderTest");
    }
  }

  /*
   * @testName: closedQueueSessionCreateTempQueueTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:194;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedQueueSessionCreateTempQueueTest() throws Exception {
    String testName = "closedQueueSessionCreateTempQueueTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create TemporaryQueue with closed session.");
      try {
        TemporaryQueue tQ = tool.getDefaultQueueSession()
            .createTemporaryQueue();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Exception(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedQueueSessionCreateTempQueueTest");
    }
  }

  /*
   * @testName: closedQueueSessionCreateMessageTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:213;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedQueueSessionCreateMessageTest() throws Exception {
    String testName = "closedQueueSessionCreateMessageTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create message with closed session.");
      try {
        Message m = tool.getDefaultQueueSession().createMessage();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Exception(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedQueueSessionCreateMessageTest");
    }
  }

  /*
   * @testName: closedQueueSessionCreateBytesMessageTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:209;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedQueueSessionCreateBytesMessageTest() throws Exception {
    String testName = "closedQueueSessionCreateBytesMessageTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create BytesMessage with closed session.");
      try {
        BytesMessage m = tool.getDefaultQueueSession().createBytesMessage();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Exception(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedQueueSessionCreateBytesMessageTest");
    }
  }

  /*
   * @testName: closedQueueSessionCreateMapMessageTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:211;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedQueueSessionCreateMapMessageTest() throws Exception {
    String testName = "closedQueueSessionCreateMapMessageTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create MapMessage with closed session.");
      try {
        MapMessage m = tool.getDefaultQueueSession().createMapMessage();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Exception(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedQueueSessionCreateMapMessageTest");
    }
  }

  /*
   * @testName: closedQueueSessionCreateObjectMessageTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:215;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedQueueSessionCreateObjectMessageTest() throws Exception {
    String testName = "closedQueueSessionCreateObjectMessageTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create ObjectMessage with closed session.");
      try {
        ObjectMessage m = tool.getDefaultQueueSession().createObjectMessage();
        if (m != null)
          TestUtil.logTrace("m=" + m);
        TestUtil.logTrace("FAIL: expected IllegalStateException");
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Exception(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedQueueSessionCreateObjectMessageTest");
    }
  }

  /*
   * @testName: closedQueueSessionCreateObject2MessageTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:217;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedQueueSessionCreateObject2MessageTest() throws Exception {
    String testName = "closedQueueSessionCreateObject2MessageTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create ObjectMessage(object) with closed session.");
      try {
        String s = "Simple object";
        ObjectMessage m = tool.getDefaultQueueSession().createObjectMessage(s);
        if (m != null)
          TestUtil.logTrace("m=" + m);
        TestUtil.logTrace("FAIL: expected IllegalStateException");
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Exception(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedQueueSessionCreateObject2MessageTest");
    }
  }

  /*
   * @testName: closedQueueSessionCreateStreamMessageTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:219;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedQueueSessionCreateStreamMessageTest() throws Exception {
    String testName = "closedQueueSessionCreateStreamMessageTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create StreamMessage with closed session.");
      try {
        StreamMessage m = tool.getDefaultQueueSession().createStreamMessage();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Exception(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedQueueSessionCreateStreamMessageTest");
    }
  }

  /*
   * @testName: closedQueueSessionCreateTextMessageTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:221;
   * 
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedQueueSessionCreateTextMessageTest() throws Exception {
    String testName = "closedQueueSessionCreateTextMessageTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create TextMessage with closed session.");
      try {
        TextMessage m = tool.getDefaultQueueSession().createTextMessage();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Exception(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedQueueSessionCreateTextMessageTest");
    }
  }

  /*
   * @testName: closedQueueSessionCreateText2MessageTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:223;
   * 
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedQueueSessionCreateText2MessageTest() throws Exception {
    String testName = "closedQueueSessionCreateText2MessageTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create TextMessage with closed session.");
      try {
        TextMessage m = tool.getDefaultQueueSession()
            .createTextMessage("test message");
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Exception(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedQueueSessionCreateText2MessageTest");
    }
  }

  /*
   * @testName: closedQueueSessionReceiverCloseTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:338;
   * 
   * @test_Strategy: Close default receiver and call method on it.
   */

  public void closedQueueSessionReceiverCloseTest() throws Exception {
    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call close again");
      tool.getDefaultQueueReceiver().close();
    } catch (Exception e) {
      throw new Exception("closedQueueSessionReceiverCloseTest", e);
    }
  }

  /*
   * @testName: closedQueueSessionGetMessageSelectorTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:326;
   * 
   * @test_Strategy: Close default receiver and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSessionGetMessageSelectorTest() throws Exception {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call getMessageSelector");
      try {
        String foo = tool.getDefaultQueueReceiver().getMessageSelector();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Exception("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Exception("closedQueueSessionGetMessageSelectorTest", e);
    }
  }

  /*
   * @testName: closedQueueSessionReceiveTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:332;
   * 
   * @test_Strategy: Close default receiver and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSessionReceiveTest() throws Exception {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call receive");
      try {
        Message foo = tool.getDefaultQueueReceiver().receive();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Exception("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Exception("closedQueueSessionReceiveTest", e);
    }
  }

  /*
   * @testName: closedQueueSessionReceiveTimeoutTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:334;
   * 
   * @test_Strategy: Close default receiver and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSessionReceiveTimeoutTest() throws Exception {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call receive(timeout)");
      try {
        Message foo = tool.getDefaultQueueReceiver().receive(timeout);

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Exception("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Exception("closedQueueSessionReceiveTimeoutTest", e);
    }
  }

  /*
   * @testName: closedQueueSessionReceiveNoWaitTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:336;
   * 
   * @test_Strategy: Close default receiver and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSessionReceiveNoWaitTest() throws Exception {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call receiveNoWait");
      try {
        Message foo = tool.getDefaultQueueReceiver().receiveNoWait();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Exception("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Exception("closedQueueSessionReceiveNoWaitTest", e);
    }
  }

  /*
   * @testName: closedQueueSessionReceiverGetQueueTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:268;
   * 
   * @test_Strategy: Close default receiver and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSessionReceiverGetQueueTest() throws Exception {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call getQueue");
      try {
        Queue foo = tool.getDefaultQueueReceiver().getQueue();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Exception("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Exception("closedQueueSessionReceiverGetQueueTest", e);
    }
  }

  /*
   * @testName: closedQueueSessionSenderCloseTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:315;
   * 
   * 
   * @test_Strategy: Close default sender and call method on it.
   */

  public void closedQueueSessionSenderCloseTest() throws Exception {
    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call close again");
      tool.getDefaultQueueSender().close();
    } catch (Exception e) {
      throw new Exception("closedQueueSessionSenderCloseTest", e);
    }
  }

  /*
   * @testName: closedQueueSessionGetDeliveryModeTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:303;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSessionGetDeliveryModeTest() throws Exception {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call getDeliveryMode");
      try {
        int foo = tool.getDefaultQueueSender().getDeliveryMode();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Exception("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Exception("closedQueueSessionGetDeliveryModeTest", e);
    }
  }

  /*
   * @testName: closedQueueSessionGetDisableMessageIDTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:295;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSessionGetDisableMessageIDTest() throws Exception {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call getDisableMessageID");
      try {
        boolean foo = tool.getDefaultQueueSender().getDisableMessageID();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Exception("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Exception("closedQueueSessionGetDisableMessageIDTest", e);
    }
  }

  /*
   * @testName: closedQueueSessionGetDisableMessageTimestampTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:299;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSessionGetDisableMessageTimestampTest() throws Exception {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call getDisableMessageTimestamp");
      try {
        boolean foo = tool.getDefaultQueueSender().getDisableMessageTimestamp();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Exception("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Exception("closedQueueSessionGetDisableMessageTimestampTest", e);
    }
  }

  /*
   * @testName: closedQueueSessionGetPriorityTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:307;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSessionGetPriorityTest() throws Exception {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call getPriority");
      try {
        int foo = tool.getDefaultQueueSender().getPriority();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Exception("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Exception("closedQueueSessionGetPriorityTest", e);
    }
  }

  /*
   * @testName: closedQueueSessionGetTimeToLiveTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:311;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSessionGetTimeToLiveTest() throws Exception {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call getTimeToLive");
      try {
        long foo = tool.getDefaultQueueSender().getTimeToLive();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Exception("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Exception("closedQueueSessionGetTimeToLiveTest", e);
    }
  }

  /*
   * @testName: closedQueueSessionSetDeliveryModeTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:301;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSessionSetDeliveryModeTest() throws Exception {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call setDeliveryMode");
      try {
        tool.getDefaultQueueSender()
            .setDeliveryMode(Message.DEFAULT_DELIVERY_MODE);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Exception("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Exception("closedQueueSessionSetDeliveryModeTest", e);
    }
  }

  /*
   * @testName: closedQueueSessionSetDisableMessageIDTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:293;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSessionSetDisableMessageIDTest() throws Exception {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call setDisableMessageID");
      try {
        tool.getDefaultQueueSender().setDisableMessageID(true);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Exception("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Exception("closedQueueSessionSetDisableMessageIDTest", e);
    }
  }

  /*
   * @testName: closedQueueSessionSetDisableMessageTimestampTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:297;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSessionSetDisableMessageTimestampTest() throws Exception {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call setDisableMessageTimestamp");
      try {
        tool.getDefaultQueueSender().setDisableMessageTimestamp(true);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Exception("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Exception("closedQueueSessionSetDisableMessageTimestampTest", e);
    }
  }

  /*
   * @testName: closedQueueSessionSetPriorityTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:305;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSessionSetPriorityTest() throws Exception {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call setPriority");
      try {
        tool.getDefaultQueueSender().setPriority(Message.DEFAULT_PRIORITY);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      throw new Exception("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Exception("closedQueueSessionSetPriorityTest", e);
    }
  }

  /*
   * @testName: closedQueueSessionSetTimeToLiveTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:309;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSessionSetTimeToLiveTest() throws Exception {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call setTimeToLive");
      try {
        tool.getDefaultQueueSender()
            .setTimeToLive(Message.DEFAULT_TIME_TO_LIVE);
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Exception("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Exception("closedQueueSessionSetTimeToLiveTest", e);
    }
  }

  /*
   * @testName: closedQueueSessionSenderGetQueueTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:196;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSessionSenderGetQueueTest() throws Exception {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call getQueue");
      try {
        Queue foo = tool.getDefaultQueueSender().getQueue();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Exception("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Exception("closedQueueSessionSenderGetQueueTest", e);
    }
  }

  /*
   * @testName: closedQueueSessionSend1Test
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:198;
   * 
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSessionSend1Test() throws Exception {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call send(Message)");
      try {
        tool.getDefaultQueueSender().send(new MessageTestImpl());
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Exception("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Exception("closedQueueSessionSend1Test", e);
    }
  }

  /*
   * @testName: closedQueueSessionSend2Test
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:200;
   * 
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSessionSend2Test() throws Exception {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call send(Message,int,int,long)");
      try {
        tool.getDefaultQueueSender().send(new MessageTestImpl(),
            Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
            Message.DEFAULT_TIME_TO_LIVE);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Exception("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Exception("closedQueueSessionSend2Test", e);
    }
  }

  /*
   * @testName: closedQueueSessionSend3Test
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:202;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSessionSend3Test() throws Exception {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call send(Queue,Message)");
      try {
        tool.getDefaultQueueSender().send(new MessageTestImpl());
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Exception("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Exception("closedQueueSessionSend3Test", e);
    }
  }

  /*
   * @testName: closedQueueSessionSend4Test
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:204;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSessionSend4Test() throws Exception {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call send(Queue,Message,int,int,long)");
      try {
        tool.getDefaultQueueSender().send(new MessageTestImpl(),
            Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
            Message.DEFAULT_TIME_TO_LIVE);
        logErr("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      throw new Exception("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Exception("closedQueueSessionSend4Test", e);
    }
  }

  /*
   * @testName: closedQueueSessionRecoverTest
   *
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:235;
   *
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedQueueSessionRecoverTest() throws Exception {
    try {
      createAndCloseSession(JmsTool.TX_QUEUE, user, password);
      logMsg("Try to call recover() with closed session.");
      try {
        tool.getDefaultQueueSession().recover();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Exception("closedQueueSessionRecoverTest");
    } catch (Exception e) {
      TestUtil.logErr("closedQueueSessionRecoverTest failed: ", e);
      throw new Exception("closedQueueSessionRecoverTest", e);
    }
  }

  /*
   * @testName: closedSessionRecoverTest
   *
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:636;
   *
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedSessionRecoverTest() throws Exception {
    String testName = "closedSessionRecoverTest";

    try {
      createAndCloseSession(JmsTool.COMMON_QTX, user, password);
      logMsg("Try to call recover() with closed session.");
      try {
        tool.getDefaultSession().recover();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Exception(testName);
    } catch (Exception e) {
      TestUtil.logErr(testName + " failed: ", e);
      throw new Exception(testName);
    }
  }

  /*
   * @testName: closedQueueSessionCommitTest
   *
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:229;
   *
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedQueueSessionCommitTest() throws Exception {
    String testName = "closedQueueSessionCommitTest";

    try {
      createAndCloseSession(JmsTool.TX_QUEUE, user, password);
      logMsg("Try to call commit with closed session.");
      try {
        tool.getDefaultQueueSession().commit();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Exception(testName);
    } catch (Exception e) {
      TestUtil.logErr(testName + " failed: ", e);
      throw new Exception(testName);
    }
  }

  /*
   * @testName: closedQueueSessionGetTransactedTest
   *
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:225;
   *
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedQueueSessionGetTransactedTest() throws Exception {
    String testName = "closedQueueSessionGetTransactedTest";

    try {
      createAndCloseSession(JmsTool.TX_QUEUE, user, password);
      logMsg("Try to call getTransacted() with closed session.");
      try {
        boolean b = tool.getDefaultQueueSession().getTransacted();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }

      throw new Exception(testName);
    } catch (Exception e) {
      TestUtil.logErr(testName + " failed: ", e);
      throw new Exception(testName);
    }
  }

  /*
   * @testName: closedQueueSessionRollbackTest
   *
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:231;
   *
   * @test_Strategy: Close default session and call method on it. Check for
   * proper JMSException.
   */

  public void closedQueueSessionRollbackTest() throws Exception {
    String testName = "closedQueueSessionRollbackTest";

    try {
      createAndCloseSession(JmsTool.TX_QUEUE, user, password);
      logMsg("Try to call rollback() with closed session.");
      try {
        tool.getDefaultQueueSession().rollback();
      } catch (jakarta.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Exception(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Exception("closedQueueSessionRollbackTest");
    }
  }
}
