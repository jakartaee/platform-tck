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
package com.sun.ts.tests.jms.core.closedQueueSession;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.Properties;
import java.util.ArrayList;
import com.sun.javatest.Status;

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
      queues = new ArrayList(2);

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
        logMsg("Cleanup: Closing QueueConnection");
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
   * @testName: closedQueueSessionCloseTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:229;
   * 
   * @test_Strategy: Close default session and call close method on it.
   */

  public void closedQueueSessionCloseTest() throws Fault {
    try {
      TestUtil.logTrace("Before create and close");
      createAndCloseSession(JmsTool.QUEUE, user, password);

      logMsg("Try to call close on closed session.");
      tool.getDefaultQueueSession().close();
    } catch (Exception e) {
      TestUtil.logTrace("fault " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedQueueSessionCloseTest");
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

  public void closedQueueSessionCreateBrowserTest() throws Fault {
    String testName = "closedQueueSessionCreateBrowserTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create QueueBrowser with closed session.");
      try {
        QueueBrowser qB = tool.getDefaultQueueSession()
            .createBrowser(tool.getDefaultQueue());
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Fault(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      throw new Fault("closedQueueSessionCreateBrowserTest", e);
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

  public void closedQueueSessionCreateBrowserMsgSelectorTest() throws Fault {
    String testName = "closedQueueSessionCreateBrowserMsgSelectorTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create QueueBrowser with closed session.");
      try {
        QueueBrowser qB = tool.getDefaultQueueSession()
            .createBrowser(tool.getDefaultQueue(), "TEST = 'test'");
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Fault(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedQueueSessionCreateBrowserMsgSelectorTest");
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

  public void closedQueueSessionCreateQueueTest() throws Fault {
    String testName = "closedQueueSessionCreateQueueTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create queue with closed session.");
      try {
        Queue q = tool.getDefaultQueueSession().createQueue(testName);
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Fault(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedQueueSessionCreateQueueTest");
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

  public void closedQueueSessionCreateReceiverTest() throws Fault {
    String testName = "closedQueueSessionCreateReceiverTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create Receiver with closed session.");
      try {
        QueueReceiver qR = tool.getDefaultQueueSession()
            .createReceiver(tool.getDefaultQueue());
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Fault(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedQueueSessionCreateReceiverTest");
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

  public void closedQueueSessionCreateReceiverMsgSelectorTest() throws Fault {
    String testName = "closedQueueSessionCreateReceiverMsgSelectorTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create Receiver with closed session.");
      try {
        QueueReceiver qR = tool.getDefaultQueueSession()
            .createReceiver(tool.getDefaultQueue(), "TEST = 'test'");
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Fault(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedQueueSessionCreateReceiverMsgSelectorTest");
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

  public void closedQueueSessionCreateSenderTest() throws Fault {
    String testName = "closedQueueSessionCreateSenderTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create Sender with closed session.");
      try {
        QueueSender qS = tool.getDefaultQueueSession()
            .createSender(tool.getDefaultQueue());
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Fault(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedQueueSessionCreateSenderTest");
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

  public void closedQueueSessionCreateTempQueueTest() throws Fault {
    String testName = "closedQueueSessionCreateTempQueueTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create TemporaryQueue with closed session.");
      try {
        TemporaryQueue tQ = tool.getDefaultQueueSession()
            .createTemporaryQueue();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Fault(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedQueueSessionCreateTempQueueTest");
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

  public void closedQueueSessionCreateMessageTest() throws Fault {
    String testName = "closedQueueSessionCreateMessageTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create message with closed session.");
      try {
        Message m = tool.getDefaultQueueSession().createMessage();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Fault(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedQueueSessionCreateMessageTest");
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

  public void closedQueueSessionCreateBytesMessageTest() throws Fault {
    String testName = "closedQueueSessionCreateBytesMessageTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create BytesMessage with closed session.");
      try {
        BytesMessage m = tool.getDefaultQueueSession().createBytesMessage();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Fault(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedQueueSessionCreateBytesMessageTest");
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

  public void closedQueueSessionCreateMapMessageTest() throws Fault {
    String testName = "closedQueueSessionCreateMapMessageTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create MapMessage with closed session.");
      try {
        MapMessage m = tool.getDefaultQueueSession().createMapMessage();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Fault(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedQueueSessionCreateMapMessageTest");
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

  public void closedQueueSessionCreateObjectMessageTest() throws Fault {
    String testName = "closedQueueSessionCreateObjectMessageTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create ObjectMessage with closed session.");
      try {
        ObjectMessage m = tool.getDefaultQueueSession().createObjectMessage();
        if (m != null)
          TestUtil.logTrace("m=" + m);
        TestUtil.logTrace("FAIL: expected IllegalStateException");
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Fault(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedQueueSessionCreateObjectMessageTest");
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

  public void closedQueueSessionCreateObject2MessageTest() throws Fault {
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
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Fault(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedQueueSessionCreateObject2MessageTest");
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

  public void closedQueueSessionCreateStreamMessageTest() throws Fault {
    String testName = "closedQueueSessionCreateStreamMessageTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create StreamMessage with closed session.");
      try {
        StreamMessage m = tool.getDefaultQueueSession().createStreamMessage();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Fault(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedQueueSessionCreateStreamMessageTest");
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

  public void closedQueueSessionCreateTextMessageTest() throws Fault {
    String testName = "closedQueueSessionCreateTextMessageTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create TextMessage with closed session.");
      try {
        TextMessage m = tool.getDefaultQueueSession().createTextMessage();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Fault(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedQueueSessionCreateTextMessageTest");
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

  public void closedQueueSessionCreateText2MessageTest() throws Fault {
    String testName = "closedQueueSessionCreateText2MessageTest";

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logMsg("Try to create TextMessage with closed session.");
      try {
        TextMessage m = tool.getDefaultQueueSession()
            .createTextMessage("test message");
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Fault(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedQueueSessionCreateText2MessageTest");
    }
  }

  /*
   * @testName: closedQueueSessionReceiverCloseTest
   * 
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:338;
   * 
   * @test_Strategy: Close default receiver and call method on it.
   */

  public void closedQueueSessionReceiverCloseTest() throws Fault {
    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call close again");
      tool.getDefaultQueueReceiver().close();
    } catch (Exception e) {
      throw new Fault("closedQueueSessionReceiverCloseTest", e);
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

  public void closedQueueSessionGetMessageSelectorTest() throws Fault {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call getMessageSelector");
      try {
        String foo = tool.getDefaultQueueReceiver().getMessageSelector();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Fault("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Fault("closedQueueSessionGetMessageSelectorTest", e);
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

  public void closedQueueSessionReceiveTest() throws Fault {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call receive");
      try {
        Message foo = tool.getDefaultQueueReceiver().receive();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Fault("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Fault("closedQueueSessionReceiveTest", e);
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

  public void closedQueueSessionReceiveTimeoutTest() throws Fault {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call receive(timeout)");
      try {
        Message foo = tool.getDefaultQueueReceiver().receive(timeout);

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Fault("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Fault("closedQueueSessionReceiveTimeoutTest", e);
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

  public void closedQueueSessionReceiveNoWaitTest() throws Fault {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call receiveNoWait");
      try {
        Message foo = tool.getDefaultQueueReceiver().receiveNoWait();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Fault("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Fault("closedQueueSessionReceiveNoWaitTest", e);
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

  public void closedQueueSessionReceiverGetQueueTest() throws Fault {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call getQueue");
      try {
        Queue foo = tool.getDefaultQueueReceiver().getQueue();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Fault("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Fault("closedQueueSessionReceiverGetQueueTest", e);
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

  public void closedQueueSessionSenderCloseTest() throws Fault {
    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call close again");
      tool.getDefaultQueueSender().close();
    } catch (Exception e) {
      throw new Fault("closedQueueSessionSenderCloseTest", e);
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

  public void closedQueueSessionGetDeliveryModeTest() throws Fault {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call getDeliveryMode");
      try {
        int foo = tool.getDefaultQueueSender().getDeliveryMode();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Fault("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Fault("closedQueueSessionGetDeliveryModeTest", e);
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

  public void closedQueueSessionGetDisableMessageIDTest() throws Fault {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call getDisableMessageID");
      try {
        boolean foo = tool.getDefaultQueueSender().getDisableMessageID();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Fault("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Fault("closedQueueSessionGetDisableMessageIDTest", e);
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

  public void closedQueueSessionGetDisableMessageTimestampTest() throws Fault {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call getDisableMessageTimestamp");
      try {
        boolean foo = tool.getDefaultQueueSender().getDisableMessageTimestamp();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Fault("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Fault("closedQueueSessionGetDisableMessageTimestampTest", e);
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

  public void closedQueueSessionGetPriorityTest() throws Fault {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call getPriority");
      try {
        int foo = tool.getDefaultQueueSender().getPriority();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Fault("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Fault("closedQueueSessionGetPriorityTest", e);
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

  public void closedQueueSessionGetTimeToLiveTest() throws Fault {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call getTimeToLive");
      try {
        long foo = tool.getDefaultQueueSender().getTimeToLive();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Fault("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Fault("closedQueueSessionGetTimeToLiveTest", e);
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

  public void closedQueueSessionSetDeliveryModeTest() throws Fault {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call setDeliveryMode");
      try {
        tool.getDefaultQueueSender()
            .setDeliveryMode(Message.DEFAULT_DELIVERY_MODE);
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Fault("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Fault("closedQueueSessionSetDeliveryModeTest", e);
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

  public void closedQueueSessionSetDisableMessageIDTest() throws Fault {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call setDisableMessageID");
      try {
        tool.getDefaultQueueSender().setDisableMessageID(true);
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Fault("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Fault("closedQueueSessionSetDisableMessageIDTest", e);
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

  public void closedQueueSessionSetDisableMessageTimestampTest() throws Fault {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call setDisableMessageTimestamp");
      try {
        tool.getDefaultQueueSender().setDisableMessageTimestamp(true);
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Fault("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Fault("closedQueueSessionSetDisableMessageTimestampTest", e);
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

  public void closedQueueSessionSetPriorityTest() throws Fault {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call setPriority");
      try {
        tool.getDefaultQueueSender().setPriority(Message.DEFAULT_PRIORITY);
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      throw new Fault("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Fault("closedQueueSessionSetPriorityTest", e);
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

  public void closedQueueSessionSetTimeToLiveTest() throws Fault {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call setTimeToLive");
      try {
        tool.getDefaultQueueSender()
            .setTimeToLive(Message.DEFAULT_TIME_TO_LIVE);
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Fault("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Fault("closedQueueSessionSetTimeToLiveTest", e);
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

  public void closedQueueSessionSenderGetQueueTest() throws Fault {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call getQueue");
      try {
        Queue foo = tool.getDefaultQueueSender().getQueue();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Fault("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Fault("closedQueueSessionSenderGetQueueTest", e);
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

  public void closedQueueSessionSend1Test() throws Fault {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call send(Message)");
      try {
        tool.getDefaultQueueSender().send(new MessageTestImpl());
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Fault("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Fault("closedQueueSessionSend1Test", e);
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

  public void closedQueueSessionSend2Test() throws Fault {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call send(Message,int,int,long)");
      try {
        tool.getDefaultQueueSender().send(new MessageTestImpl(),
            Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
            Message.DEFAULT_TIME_TO_LIVE);
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Fault("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Fault("closedQueueSessionSend2Test", e);
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

  public void closedQueueSessionSend3Test() throws Fault {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call send(Queue,Message)");
      try {
        tool.getDefaultQueueSender().send(new MessageTestImpl());
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }

      throw new Fault("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Fault("closedQueueSessionSend3Test", e);
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

  public void closedQueueSessionSend4Test() throws Fault {

    try {
      createAndCloseSession(JmsTool.QUEUE, user, password);
      logTrace("Try to call send(Queue,Message,int,int,long)");
      try {
        tool.getDefaultQueueSender().send(new MessageTestImpl(),
            Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
            Message.DEFAULT_TIME_TO_LIVE);
        logErr("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        return;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      throw new Fault("Error: failures occurred during tests");

    } catch (Exception e) {
      throw new Fault("closedQueueSessionSend4Test", e);
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

  public void closedQueueSessionRecoverTest() throws Fault {
    try {
      createAndCloseSession(JmsTool.TX_QUEUE, user, password);
      logMsg("Try to call recover() with closed session.");
      try {
        tool.getDefaultQueueSession().recover();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Fault("closedQueueSessionRecoverTest");
    } catch (Exception e) {
      TestUtil.logErr("closedQueueSessionRecoverTest failed: ", e);
      throw new Fault("closedQueueSessionRecoverTest", e);
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

  public void closedSessionRecoverTest() throws Fault {
    String testName = "closedSessionRecoverTest";

    try {
      createAndCloseSession(JmsTool.COMMON_QTX, user, password);
      logMsg("Try to call recover() with closed session.");
      try {
        tool.getDefaultSession().recover();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Fault(testName);
    } catch (Exception e) {
      TestUtil.logErr(testName + " failed: ", e);
      throw new Fault(testName);
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

  public void closedQueueSessionCommitTest() throws Fault {
    String testName = "closedQueueSessionCommitTest";

    try {
      createAndCloseSession(JmsTool.TX_QUEUE, user, password);
      logMsg("Try to call commit with closed session.");
      try {
        tool.getDefaultQueueSession().commit();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Fault(testName);
    } catch (Exception e) {
      TestUtil.logErr(testName + " failed: ", e);
      throw new Fault(testName);
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

  public void closedQueueSessionGetTransactedTest() throws Fault {
    String testName = "closedQueueSessionGetTransactedTest";

    try {
      createAndCloseSession(JmsTool.TX_QUEUE, user, password);
      logMsg("Try to call getTransacted() with closed session.");
      try {
        boolean b = tool.getDefaultQueueSession().getTransacted();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }

      throw new Fault(testName);
    } catch (Exception e) {
      TestUtil.logErr(testName + " failed: ", e);
      throw new Fault(testName);
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

  public void closedQueueSessionRollbackTest() throws Fault {
    String testName = "closedQueueSessionRollbackTest";

    try {
      createAndCloseSession(JmsTool.TX_QUEUE, user, password);
      logMsg("Try to call rollback() with closed session.");
      try {
        tool.getDefaultQueueSession().rollback();
      } catch (javax.jms.IllegalStateException ise) {
        logMsg("Caught expected exception");
        return;
      }
      throw new Fault(testName);
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedQueueSessionRollbackTest");
    }
  }
}
