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
package com.sun.ts.tests.jms.core.closedTopicSubscriber;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.util.*;
import java.io.*;
import com.sun.javatest.Status;
import javax.jms.*;

public class ClosedTopicSubscriberTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.closedTopicSubscriber.ClosedTopicSubscriberTests";

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

  /* Run test in standalone mode */

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    ClosedTopicSubscriberTests theTests = new ClosedTopicSubscriberTests();
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
      throw new Fault("Setup failed!", e);
    }
  }

  /* cleanup */
  /* Utility methods for tests */

  /**
   * Used by tests that need a closed subscriber for testing. Passes any
   * exceptions up to caller.
   * 
   * @param int
   *          The type of session that needs to be created and closed
   */
  private void createAndCloseSubscriber() throws Exception {
    tool = new JmsTool(JmsTool.TOPIC, jmsUser, jmsPassword, mode);
    tool.getDefaultTopicConnection().start();

    logTrace("Closing topic subscriber");
    tool.getDefaultTopicSubscriber().close();
    logTrace("Subscriber closed");
  }

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
   * @testName: closedTopicSubscriberCloseTest
   * 
   * @assertion_ids: JMS:SPEC:201; JMS:JAVADOC:338;
   * 
   * @test_Strategy: Close default subscriber and call method on it.
   */
  public void closedTopicSubscriberCloseTest() throws Fault {
    try {
      createAndCloseSubscriber();
      logTrace("Try to call close again");
      tool.getDefaultTopicSubscriber().close();
    } catch (Exception e) {
      throw new Fault("closedTopicSubscriberCloseTest", e);
    }
  }

  /*
   * @testName: closedTopicSubscriberGetMessageSelectorTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:326;
   * 
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSubscriberGetMessageSelectorTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSubscriber();
      logTrace("Try to call getMessageSelector");
      try {
        String foo = tool.getDefaultTopicSubscriber().getMessageSelector();

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
      throw new Fault("closedTopicSubscriberGetMessageSelectorTest", e);
    }
  }

  /*
   * @testName: closedTopicSubscriberReceiveTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:332;
   * 
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSubscriberReceiveTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSubscriber();
      logTrace("Try to call receive");
      try {
        Message foo = tool.getDefaultTopicSubscriber().receive();

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
      throw new Fault("closedTopicSubscriberReceiveTest", e);
    }
  }

  /*
   * @testName: closedTopicSubscriberReceiveTimeoutTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:334;
   * 
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSubscriberReceiveTimeoutTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSubscriber();
      logTrace("Try to call receive(timeout)");
      try {
        Message foo = tool.getDefaultTopicSubscriber().receive(timeout);

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
      throw new Fault("closedTopicSubscriberReceiveTimeoutTest", e);
    }
  }

  /*
   * @testName: closedTopicSubscriberReceiveNoWaitTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:336;
   * 
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSubscriberReceiveNoWaitTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSubscriber();
      logTrace("Try to call receiveNoWait");
      try {
        Message foo = tool.getDefaultTopicSubscriber().receiveNoWait();

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
      throw new Fault("closedTopicSubscriberReceiveNoWaitTest", e);
    }
  }

  /*
   * @testName: closedTopicSubscriberGetNoLocalTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:79;
   * 
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */
  public void closedTopicSubscriberGetNoLocalTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSubscriber();
      logTrace("Try to call getNoLocal");
      try {
        boolean foo = tool.getDefaultTopicSubscriber().getNoLocal();

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
      throw new Fault("closedTopicSubscriberGetNoLocalTest", e);
    }
  }

  /*
   * @testName: closedTopicSubscriberGetTopicTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:77;
   * 
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSubscriberGetTopicTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSubscriber();
      logTrace("Try to call getTopic");
      try {
        Topic foo = tool.getDefaultTopicSubscriber().getTopic();

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
      throw new Fault("closedTopicSubscriberGetTopicTest", e);
    }
  }

}
