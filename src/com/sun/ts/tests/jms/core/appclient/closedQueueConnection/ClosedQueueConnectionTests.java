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
package com.sun.ts.tests.jms.core.appclient.closedQueueConnection;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.*;
import com.sun.javatest.Status;

/**
 * JMS TS tests. Testing method calls on closed QueueConnection objects.
 */
public class ClosedQueueConnectionTests extends ServiceEETest {
  private static final String TestName = "com.sun.ts.tests.jms.core.appclient.closedQueueConnection.ClosedQueueConnectionTests";

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
    ClosedQueueConnectionTests theTests = new ClosedQueueConnectionTests();
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
  private void createAndCloseConnection(int type) throws Exception {
    if ((type == JmsTool.QUEUE) || (type == JmsTool.TX_QUEUE)) {
      tool = new JmsTool(type, user, password, mode);
      tool.getDefaultQueueConnection().start();

      logTrace("Closing queue Connection");
      tool.getDefaultQueueConnection().close();
    }
    logTrace("Connection closed");
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
        throw new Exception("'users' in ts.jte must be null");
      }
      if (password == null) {
        throw new Exception("'password' in ts.jte must be null");
      }
      if (mode == null) {
        throw new Exception("'mode' in ts.jte must be null");
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
   * @testName: closedQueueConnectionSetClientIDTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:270; JMS:JAVADOC:526;
   * JMS:JAVADOC:514;
   *
   * @test_Strategy: Close default Connection and call setClientID() method on
   * it. Check for IllegalStateException.
   */
  public void closedQueueConnectionSetClientIDTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call setClientID");
      try {
        tool.getDefaultQueueConnection().setClientID("foo");
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
      throw new Fault("closedQueueConnectionSetClientIDTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionSetExceptionListenerTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:270; JMS:JAVADOC:526;
   * JMS:JAVADOC:520; JMS:JAVADOC:483;
   *
   * @test_Strategy: Close default Connection and call the setExceptionListener
   * method on it. Check for IllegalStateException.
   */
  public void closedQueueConnectionSetExceptionListenerTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call setExceptionListener");
      try {
        ExceptionListener foo = new ExceptionListener() {

          public void onException(JMSException jmsE) {
          }

        };

        tool.getDefaultQueueConnection().setExceptionListener(foo);
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
      throw new Fault("closedQueueConnectionSetExceptionListenerTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionGetMessageListenerTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:270; JMS:JAVADOC:526;
   * JMS:JAVADOC:328;
   * 
   * @test_Strategy: Close default receiver and call the getMessageListener()
   * method on the QueueReceiver associated with it. Check for
   * IllegalStateException.
   */
  public void closedQueueConnectionGetMessageListenerTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
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
      throw new Fault("closedQueueConnectionGetMessageListenerTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionSetMessageListenerTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:270; JMS:JAVADOC:526;
   * JMS:JAVADOC:330; JMS:JAVADOC:325;
   *
   * @test_Strategy: Close default receiver and call the setMessageListener
   * method on the QueueReceiver associated with it. Check for
   * IllegalStateException.
   */
  public void closedQueueConnectionSetMessageListenerTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
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
      throw new Fault("closedQueueConnectionSetMessageListenerTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionGetExceptionListenerTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:270; JMS:JAVADOC:526;
   * JMS:JAVADOC:518;
   *
   * @test_Strategy: Close default Connection and call the
   * getExceptionListener() method on it. Check for IllegalStateException.
   */
  public void closedQueueConnectionGetExceptionListenerTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call getExceptionListener");
      try {
        ExceptionListener foo = tool.getDefaultQueueConnection()
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
      throw new Fault("closedQueueConnectionGetExceptionListenerTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionStopTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:270; JMS:JAVADOC:526;
   * JMS:JAVADOC:524;
   *
   * @test_Strategy: Close default Connection and call the stop method on it.
   * Check for IllegalStateException.
   */
  public void closedQueueConnectionStopTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.QUEUE);
      logTrace("Try to call stop");
      try {
        tool.getDefaultQueueConnection().stop();
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
      throw new Fault("closedQueueConnectionStopTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionAckTest
   *
   * @assertion_ids: JMS:JAVADOC:272; JMS:SPEC:106; JMS:JAVADOC:794;
   *
   * @test_Strategy: Send and receive single message. Close the queue
   * connection, call acknowledge, then verify that IllegalStateException is
   * thrown.
   */
  public void closedQueueConnectionAckTest() throws Fault {
    boolean pass = true;

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      QueueSession qSession = null;
      QueueReceiver qReceiver = null;
      QueueSender qSender = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueReceiver().close();
      tool.getDefaultQueueSession().close();

      TestUtil.logTrace("Creating new session");
      qSession = tool.getDefaultQueueConnection().createQueueSession(false,
          Session.CLIENT_ACKNOWLEDGE);
      qReceiver = qSession.createReceiver(tool.getDefaultQueue());
      qSender = qSession.createSender(tool.getDefaultQueue());
      tool.getDefaultQueueConnection().start();

      TestUtil.logMsg("Creating 1 TextMessage");
      messageSent = qSession.createTextMessage();
      messageSent.setText("Message from closedQueueConnectionAckTest");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "closedQueueConnectionAckTest");

      TestUtil.logMsg("Sending a TextMessage");
      qSender.send(messageSent);

      TestUtil.logMsg("Receiving TextMessage");
      messageReceived = (TextMessage) qReceiver.receive(timeout);
      TestUtil.logMsg("Closing DefaultQueueConnection");
      qReceiver.close();
      qSender.close();
      qSession.close();
      tool.getDefaultQueueConnection().close();

      try {
        if (messageReceived == null) {
          pass = false;
          TestUtil.logErr("Didnot receive any message!!");
        } else {
          messageReceived.acknowledge();
          pass = false;
          TestUtil.logErr("Should not be here!");
        }
      } catch (javax.jms.IllegalStateException is) {
        TestUtil.logMsg(
            "Pass: IllegalStateException thrown by acknowledge as expected");
      } catch (Exception e) {
        pass = false;
        TestUtil.logErr("Expected IllegalStateException, got", e);
      }

      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("closedQueueConnectionAckTest");
    }
  }
}
