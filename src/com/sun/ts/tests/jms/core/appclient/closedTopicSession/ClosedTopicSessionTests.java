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
package com.sun.ts.tests.jms.core.appclient.closedTopicSession;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.util.*;
import com.sun.javatest.Status;
import jakarta.jms.*;
import java.io.*;

public class ClosedTopicSessionTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.ee.ejbjspservlet.closedTopicSession.ClosedTopicSessionTests";

  private static final String testDir = System.getProperty("user.dir");

  // Harness req's
  private Properties props = null;

  // JMS object
  private static JmsTool tool = null;

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
    ClosedTopicSessionTests theTests = new ClosedTopicSessionTests();
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
  private void createAndCloseSession(int type) throws Exception {
    if ((type == JmsTool.TOPIC) || (type == JmsTool.TX_TOPIC)) {
      tool = new JmsTool(type, jmsUser, jmsPassword, mode);
      tool.getDefaultTopicConnection().start();
      logMsg("Closing topic session");
      tool.getDefaultTopicSession().close();
    }
    logMsg("Session closed");
  }

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
   * @class.setup_props: jms_timeout;user; password; platform.mode;
   * 
   * @exception Fault
   */
  public void setup(String[] args, Properties p) throws Fault {
    try {
      logTrace("In setup");
      // get props
      jmsUser = p.getProperty("user");
      jmsPassword = p.getProperty("password");
      timeout = Long.parseLong(p.getProperty("jms_timeout"));
      mode = p.getProperty("platform.mode");
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
   * @testName: closedTopicSessionSetMessageListenerTest
   *
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:330; JMS:JAVADOC:325;
   * 
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */
  public void closedTopicSessionSetMessageListenerTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call setMessageListener");
      try {
        MessageListener foo = new MessageListener() {
          public void onMessage(Message m) {
          }

        };

        tool.getDefaultTopicSubscriber().setMessageListener(foo);
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
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionSetMessageListenerTest", e);
    }
  }

  /*
   * @testName: closedTopicSessionGetMessageListenerTest
   *
   * @assertion_ids: JMS:SPEC:113; JMS:JAVADOC:328;
   *
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicSessionGetMessageListenerTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseSession(JmsTool.TOPIC);
      logTrace("Try to call getMessageListener");
      try {
        MessageListener foo = tool.getDefaultTopicSubscriber()
            .getMessageListener();

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
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicSessionGetMessageListenerTest", e);
    }
  }

}
