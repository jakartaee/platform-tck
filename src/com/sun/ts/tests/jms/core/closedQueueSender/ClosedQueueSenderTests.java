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
package com.sun.ts.tests.jms.core.closedQueueSender;

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
 * JMS TS tests. Testing method calls on closed QueueSender objects.
 */
public class ClosedQueueSenderTests extends ServiceEETest {
  private static final String TestName = "com.sun.ts.tests.jms.core.closedQueueSender.ClosedQueueSenderTests";

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
    ClosedQueueSenderTests theTests = new ClosedQueueSenderTests();
    Status s = theTests.run(args, System.out, System.err);

    s.exit();
  }

  /* Utility methods for tests */

  /**
   * Used by tests that need a closed sender for testing. Passes any exceptions
   * up to caller.
   * 
   * @param int
   *          The type of session that needs to be created and closed
   */
  private void createAndCloseSender() throws Exception {
    tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
    tool.getDefaultQueueConnection().start();

    logTrace("Closing queue sender");
    tool.getDefaultQueueSender().close();
    logTrace("Sender closed");
  }

  /* Test setup: */

  /*
   * setup() is called before each test
   * 
   * Creates Administrator object and deletes all previous Destinations.
   * Individual tests create the TestTools object with one default Queue and/or
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
      queues = new ArrayList(2);

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
   * @testName: closedQueueSenderCloseTest
   * 
   * @assertion_ids: JMS:SPEC:201; JMS:JAVADOC:315;
   * 
   * @test_Strategy: Close default sender and call method on it.
   */

  public void closedQueueSenderCloseTest() throws Fault {
    try {
      createAndCloseSender();
      logTrace("Try to call close again");
      tool.getDefaultQueueSender().close();
    } catch (Exception e) {
      throw new Fault("closedQueueSenderCloseTest", e);
    }
  }

  /*
   * @testName: closedQueueSenderGetDeliveryModeTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:303;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSenderGetDeliveryModeTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSender();
      logTrace("Try to call getDeliveryMode");
      try {
        int foo = tool.getDefaultQueueSender().getDeliveryMode();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedQueueSenderGetDeliveryModeTest", e);
    }
  }

  /*
   * @testName: closedQueueSenderGetDisableMessageIDTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:295;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSenderGetDisableMessageIDTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSender();
      logTrace("Try to call getDisableMessageID");
      try {
        boolean foo = tool.getDefaultQueueSender().getDisableMessageID();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedQueueSenderGetDisableMessageIDTest", e);
    }
  }

  /*
   * @testName: closedQueueSenderGetDisableMessageTimestampTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:299;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSenderGetDisableMessageTimestampTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSender();
      logTrace("Try to call getDisableMessageTimestamp");
      try {
        boolean foo = tool.getDefaultQueueSender().getDisableMessageTimestamp();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedQueueSenderGetDisableMessageTimestampTest", e);
    }
  }

  /*
   * @testName: closedQueueSenderGetPriorityTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:307;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSenderGetPriorityTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSender();
      logTrace("Try to call getPriority");
      try {
        int foo = tool.getDefaultQueueSender().getPriority();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedQueueSenderGetPriorityTest", e);
    }
  }

  /*
   * @testName: closedQueueSenderGetTimeToLiveTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:311;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSenderGetTimeToLiveTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSender();
      logTrace("Try to call getTimeToLive");
      try {
        long foo = tool.getDefaultQueueSender().getTimeToLive();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedQueueSenderGetTimeToLiveTest", e);
    }
  }

  /*
   * @testName: closedQueueSenderSetDeliveryModeTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:301;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSenderSetDeliveryModeTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSender();
      logTrace("Try to call setDeliveryMode");
      try {
        tool.getDefaultQueueSender()
            .setDeliveryMode(Message.DEFAULT_DELIVERY_MODE);
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedQueueSenderSetDeliveryModeTest", e);
    }
  }

  /*
   * @testName: closedQueueSenderSetDisableMessageIDTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:293;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSenderSetDisableMessageIDTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSender();
      logTrace("Try to call setDisableMessageID");
      try {
        tool.getDefaultQueueSender().setDisableMessageID(true);
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedQueueSenderSetDisableMessageIDTest", e);
    }
  }

  /*
   * @testName: closedQueueSenderSetDisableMessageTimestampTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:297;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSenderSetDisableMessageTimestampTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSender();
      logTrace("Try to call setDisableMessageTimestamp");
      try {
        tool.getDefaultQueueSender().setDisableMessageTimestamp(true);
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedQueueSenderSetDisableMessageTimestampTest", e);
    }
  }

  /*
   * @testName: closedQueueSenderSetPriorityTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:305;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSenderSetPriorityTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSender();
      logTrace("Try to call setPriority");
      try {
        tool.getDefaultQueueSender().setPriority(Message.DEFAULT_PRIORITY);
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedQueueSenderSetPriorityTest", e);
    }
  }

  /*
   * @testName: closedQueueSenderSetTimeToLiveTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:309;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSenderSetTimeToLiveTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSender();
      logTrace("Try to call setTimeToLive");
      try {
        tool.getDefaultQueueSender()
            .setTimeToLive(Message.DEFAULT_TIME_TO_LIVE);
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedQueueSenderSetTimeToLiveTest", e);
    }
  }

  /*
   * @testName: closedQueueSenderGetQueueTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:196;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSenderGetQueueTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSender();
      logTrace("Try to call getQueue");
      try {
        Queue foo = tool.getDefaultQueueSender().getQueue();

        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedQueueSenderGetQueueTest", e);
    }
  }

  /*
   * @testName: closedQueueSenderSend1Test
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:198;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSenderSend1Test() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSender();
      logTrace("Try to call send(Message)");
      try {
        tool.getDefaultQueueSender().send(new MessageTestImpl());
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedQueueSenderSend1Test", e);
    }
  }

  /*
   * @testName: closedQueueSenderSend2Test
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:200;
   * 
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSenderSend2Test() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSender();
      logTrace("Try to call send(Message,int,int,long)");
      try {
        tool.getDefaultQueueSender().send(new MessageTestImpl(),
            Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
            Message.DEFAULT_TIME_TO_LIVE);
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedQueueSenderSend2Test", e);
    }
  }

  /*
   * @testName: closedQueueSenderSend3Test
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:202;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSenderSend3Test() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSender();
      logTrace("Try to call send(Queue,Message)");
      try {
        tool.getDefaultQueueSender().send(new MessageTestImpl());
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedQueueSenderSend3Test", e);
    }
  }

  /*
   * @testName: closedQueueSenderSend4Test
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:204;
   * 
   * @test_Strategy: Close default sender and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueSenderSend4Test() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSender();
      logTrace("Try to call send(Queue,Message,int,int,long)");
      try {
        tool.getDefaultQueueSender().send(new MessageTestImpl(),
            Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
            Message.DEFAULT_TIME_TO_LIVE);
        logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedQueueSenderSend4Test", e);
    }
  }

}
