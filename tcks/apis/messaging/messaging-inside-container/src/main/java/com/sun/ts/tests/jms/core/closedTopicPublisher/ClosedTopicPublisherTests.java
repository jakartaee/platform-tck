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
package com.sun.ts.tests.jms.core.closedTopicPublisher;

import java.util.ArrayList;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsTool;
import com.sun.ts.tests.jms.common.MessageTestImpl;

import jakarta.jms.Message;
import jakarta.jms.Topic;

public class ClosedTopicPublisherTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.closedTopicPublisher.ClosedTopicPublisherTests";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // Harness req's
  private Properties props = null;

  // JMS object
  private transient JmsTool tool = null;

  // properties read from ts.jte file
  long timeout;

  private String jmsUser;

  private String jmsPassword;

  private String mode;

  ArrayList connections = null;

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    ClosedTopicPublisherTests theTests = new ClosedTopicPublisherTests();
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
  public void setup(String[] args, Properties p) throws Exception {
    try {
      logTrace("In setup");
      // get props
      jmsUser = p.getProperty("user");
      jmsPassword = p.getProperty("password");
      mode = p.getProperty("platform.mode");
      timeout = Long.parseLong(p.getProperty("jms_timeout"));
      if (timeout < 1) {
        throw new Exception("'timeout' (milliseconds) in ts.jte must be > 0");
      }

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
        logMsg("Cleanup: Closing Queue and Topic Connections");
        tool.closeAllConnections(connections);
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      logErr("An error occurred while cleaning");
      throw new Exception("Cleanup failed!", e);
    }
  }

  /* Utility methods for tests */

  /**
   * Used by tests that need a closed publisher for testing. Passes any
   * exceptions up to caller.
   * 
   * @param int
   *          The type of session that needs to be created and closed
   */
  private void createAndClosePublisher() throws Exception {
    tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, mode);
    tool.getDefaultTopicConnection().start();
    logTrace("Closing topic publisher");
    tool.getDefaultTopicPublisher().close();
    logTrace("Publisher closed");
  }

  /* Tests */
  /*
   * @testName: closedTopicPublisherCloseTest
   * 
   * @assertion_ids: JMS:SPEC:201; JMS:JAVADOC:315;
   * 
   * @test_Strategy: Close default publisher and call method on it.
   */

  public void closedTopicPublisherCloseTest() throws Exception {
    try {
      createAndClosePublisher();
      logTrace("Try to call close again");
      tool.getDefaultTopicPublisher().close();
    } catch (Exception e) {
      throw new Exception("closedTopicPublisherCloseTest", e);
    }
  }

  /*
   * @testName: closedTopicPublisherGetDeliveryModeTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:303;
   * 
   * @test_Strategy: Cannot call getDeliveryMode() on closed publishers Close
   * default publisher and call method on it. Check for IllegalStateException.
   */

  public void closedTopicPublisherGetDeliveryModeTest() throws Exception {
    boolean passed = false;

    try {
      createAndClosePublisher();
      logTrace("Try to call getDeliveryMode");
      try {
        int foo = tool.getDefaultTopicPublisher().getDeliveryMode();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicPublisherGetDeliveryModeTest", e);
    }
  }

  /*
   * @testName: closedTopicPublisherGetDisableMessageIDTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:295;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicPublisherGetDisableMessageIDTest() throws Exception {
    boolean passed = false;

    try {
      createAndClosePublisher();
      logTrace("Try to call getDisableMessageID");
      try {
        boolean foo = tool.getDefaultTopicPublisher().getDisableMessageID();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicPublisherGetDisableMessageIDTest", e);
    }
  }

  /*
   * @testName: closedTopicPublisherGetDisableMessageTimestampTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:299;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicPublisherGetDisableMessageTimestampTest()
      throws Exception {
    boolean passed = false;

    try {
      createAndClosePublisher();
      logTrace("Try to call getDisableMessageTimestamp");
      try {
        boolean foo = tool.getDefaultTopicPublisher()
            .getDisableMessageTimestamp();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicPublisherGetDisableMessageTimestampTest", e);
    }
  }

  /*
   * @testName: closedTopicPublisherGetPriorityTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:307;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicPublisherGetPriorityTest() throws Exception {
    boolean passed = false;

    try {
      createAndClosePublisher();
      logTrace("Try to call getPriority");
      try {
        int foo = tool.getDefaultTopicPublisher().getPriority();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicPublisherGetPriorityTest", e);
    }
  }

  /*
   * @testName: closedTopicPublisherGetTimeToLiveTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:311;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicPublisherGetTimeToLiveTest() throws Exception {
    boolean passed = false;

    try {
      createAndClosePublisher();
      logTrace("Try to call getTimeToLive");
      try {
        long foo = tool.getDefaultTopicPublisher().getTimeToLive();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicPublisherGetTimeToLiveTest", e);
    }
  }

  /*
   * @testName: closedTopicPublisherSetDeliveryModeTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:301;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicPublisherSetDeliveryModeTest() throws Exception {
    boolean passed = false;

    try {
      createAndClosePublisher();
      logTrace("Try to call setDeliveryMode");
      try {
        tool.getDefaultTopicPublisher()
            .setDeliveryMode(Message.DEFAULT_DELIVERY_MODE);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicPublisherSetDeliveryModeTest", e);
    }
  }

  /*
   * @testName: closedTopicPublisherSetDisableMessageIDTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:293;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicPublisherSetDisableMessageIDTest() throws Exception {
    boolean passed = false;

    try {
      createAndClosePublisher();
      logTrace("Try to call setDisableMessageID");
      try {
        tool.getDefaultTopicPublisher().setDisableMessageID(true);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicPublisherSetDisableMessageIDTest", e);
    }
  }

  /*
   * @testName: closedTopicPublisherSetDisableMessageTimestampTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:297;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicPublisherSetDisableMessageTimestampTest()
      throws Exception {
    boolean passed = false;

    try {
      createAndClosePublisher();
      logTrace("Try to call setDisableMessageTimestamp");
      try {
        tool.getDefaultTopicPublisher().setDisableMessageTimestamp(true);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicPublisherSetDisableMessageTimestampTest", e);
    }
  }

  /*
   * @testName: closedTopicPublisherSetPriorityTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:305;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicPublisherSetPriorityTest() throws Exception {
    boolean passed = false;

    try {
      createAndClosePublisher();
      logTrace("Try to call setPriority");
      try {
        tool.getDefaultTopicPublisher().setPriority(Message.DEFAULT_PRIORITY);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicPublisherSetPriorityTest", e);
    }
  }

  /*
   * @testName: closedTopicPublisherSetTimeToLiveTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:309;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicPublisherSetTimeToLiveTest() throws Exception {
    boolean passed = false;

    try {
      createAndClosePublisher();
      logTrace("Try to call setTimeToLive");
      try {
        tool.getDefaultTopicPublisher()
            .setTimeToLive(Message.DEFAULT_TIME_TO_LIVE);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicPublisherSetTimeToLiveTest", e);
    }
  }

  /*
   * @testName: closedTopicPublisherGetTopicTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:97;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicPublisherGetTopicTest() throws Exception {
    boolean passed = false;

    try {
      createAndClosePublisher();
      logTrace("Try to call getTopic");
      try {
        Topic foo = tool.getDefaultTopicPublisher().getTopic();

        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicPublisherGetTopicTest", e);
    }
  }

  /*
   * @testName: closedTopicPublisherPublish1Test
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:99;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicPublisherPublish1Test() throws Exception {
    boolean passed = false;

    try {
      createAndClosePublisher();
      logTrace("Try to call publish(Message)");
      try {
        tool.getDefaultTopicPublisher().publish(new MessageTestImpl());
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicPublisherPublish1Test", e);
    }
  }

  /*
   * @testName: closedTopicPublisherPublish2Test
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:101;
   * 
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */
  public void closedTopicPublisherPublish2Test() throws Exception {
    boolean passed = false;

    try {
      createAndClosePublisher();
      logTrace("Try to call publish(Message,int,int,long)");
      try {
        tool.getDefaultTopicPublisher().publish(new MessageTestImpl(),
            Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
            Message.DEFAULT_TIME_TO_LIVE);
        logTrace("Fail: Exception was not thrown!");
      } catch (jakarta.jms.IllegalStateException ise) {
        logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Exception("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Exception("closedTopicPublisherPublish2Test", e);
    }
  }
}
