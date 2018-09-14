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
package com.sun.ts.tests.jms.core.appclient.closedQueueReceiver;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.*;
import com.sun.javatest.Status;

public class ClosedQueueReceiverTests extends ServiceEETest {
  private static final String TestName = "com.sun.ts.tests.jms.core.appclient.closedQueueReceiver.ClosedQueueReceiverTests";

  private static final String testDir = System.getProperty("user.dir");

  // JMS objects
  private static JmsTool tool = null;

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
    ClosedQueueReceiverTests theTests = new ClosedQueueReceiverTests();
    Status s = theTests.run(args, System.out, System.err);

    s.exit();
  }

  /* Utility methods for tests */

  /**
   * Used by tests that need a closed receiver for testing. Passes any
   * exceptions up to caller.
   * 
   * @param int
   *          The type of session that needs to be created and closed
   */
  private void createAndCloseReceiver() throws Exception {
    tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
    tool.getDefaultQueueConnection().start();

    logTrace("Closing queue receiver");
    tool.getDefaultQueueReceiver().close();
    logTrace("Receiver closed");
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

  /**
   * Method Declaration.
   * 
   * 
   * @param args
   * @param p
   *
   * @exception Fault
   *
   * @see
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
      if (mode == null) {
        throw new Exception("'mode' in ts.jte must be null");
      }
      queues = new ArrayList(2);
      // get ready for new test
      logTrace("Getting Administrator and deleting any leftover destinations.");
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
   * @testName: closedQueueReceiverGetMessageListenerTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:328;
   *
   * @test_Strategy: Close default receiver and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueReceiverGetMessageListenerTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseReceiver();
      logTrace("Try to call getMessageListener");
      try {
        MessageListener foo = tool.getDefaultQueueReceiver()
            .getMessageListener();

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
      throw new Fault("closedQueueReceiverGetMessageListenerTest", e);
    }
  }

  /*
   * @testName: closedQueueReceiverSetMessageListenerTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:330; JMS:JAVADOC:325;
   *
   * @test_Strategy: Close default receiver and call method on it. Check for
   * IllegalStateException.
   */

  public void closedQueueReceiverSetMessageListenerTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseReceiver();
      logTrace("Try to call setMessageListener");
      try {
        MessageListener foo = new MessageListener() {

          public void onMessage(Message m) {
          }

        };

        tool.getDefaultQueueReceiver().setMessageListener(foo);
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
      throw new Fault("closedQueueReceiverSetMessageListenerTest", e);
    }
  }
}
