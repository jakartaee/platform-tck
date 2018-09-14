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
package com.sun.ts.tests.jms.core.appclient.closedTopicConnection;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.*;
import com.sun.javatest.Status;

/**
 * JMS product tests. Testing method calls on closed TopicConnection objects.
 */
public class ClosedTopicConnectionTests extends ServiceEETest {
  private static final String TestName = "com.sun.ts.tests.jms.core.appclient.closedTopicConnection.ClosedTopicConnectionTests";

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

  ArrayList connections = null;

  /* Run test in standalone mode */

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    ClosedTopicConnectionTests theTests = new ClosedTopicConnectionTests();
    Status s = theTests.run(args, System.out, System.err);

    s.exit();
  }

  /* Utility methods for tests */

  /**
   * Used by tests that need a closed connection for testing. Passes any
   * exceptions up to caller.
   * 
   * @param int
   *          The type of session that needs to be created and closed
   */
  private void createAndCloseConnection(int type, String user, String password)
      throws Exception {
    if ((type == JmsTool.TOPIC) || (type == JmsTool.TX_TOPIC)) {
      tool = new JmsTool(type, user, password, mode);
      tool.getDefaultTopicConnection().start();

      logTrace("Closing queue Connection");
      tool.getDefaultTopicConnection().close();
    }
    logTrace("Connection closed");
  }

  /* Test setup: */

  /*
   * setup() is called before each test
   * 
   * Creates Administrator object and deletes all previous Destinations.
   * Individual tests create the JmsTool object with one default Topic and/or
   * Topic Connection, as well as a default Topic and Topic. Tests that require
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
        throw new Exception("'numProducers' in ts.jte must be null");
      }
      if (mode == null) {
        throw new Exception("'mode' in ts.jte must be null");
      }

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
        logTrace("Cleanup: Closing Topic and Topic Connections");
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
   * @testName: closedTopicConnectionGetExceptionListenerTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:518;
   *
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionGetExceptionListenerTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      logTrace("Try to call getExceptionListener");
      try {
        ExceptionListener foo = tool.getDefaultTopicConnection()
            .getExceptionListener();

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
      throw new Fault("closedTopicConnectionGetExceptionListenerTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionSetClientIDTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:514;
   *
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionSetClientIDTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      logTrace("Try to call setClientID");
      try {
        tool.getDefaultTopicConnection().setClientID("foo");
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
      throw new Fault("closedTopicConnectionSetClientIDTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionSetExceptionListenerTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:520; JMS:JAVADOC:483;
   *
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionSetExceptionListenerTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      logTrace("Try to call setExceptionListener");
      try {
        ExceptionListener foo = new ExceptionListener() {

          public void onException(JMSException jmsE) {
          }

        };

        tool.getDefaultTopicConnection().setExceptionListener(foo);
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
      throw new Fault("closedTopicConnectionSetExceptionListenerTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionGetMessageListenerTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:328;
   *
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionGetMessageListenerTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      logTrace("Try to call getMessageListener");
      try {
        MessageListener foo = tool.getDefaultTopicSubscriber()
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
      throw new Fault("closedTopicConnectionGetMessageListenerTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionSetMessageListenerTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:330; JMS:JAVADOC:325;
   *
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionSetMessageListenerTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      logTrace("Try to call setMessageListener");
      try {
        MessageListener foo = new MessageListener() {

          public void onMessage(Message m) {
          }

        };

        tool.getDefaultTopicSubscriber().setMessageListener(foo);
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
      throw new Fault("closedTopicConnectionSetMessageListenerTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionStopTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:524;
   * 
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionStopTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call stop");
      try {
        tool.getDefaultTopicConnection().stop();
        TestUtil.logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        TestUtil.logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        TestUtil.logTrace("Fail: wrong exception: " + e.getClass().getName()
            + " was returned");
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicConnectionStopTest", e);
    }
  }

}
