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
package com.sun.ts.tests.jms.core.closedTopicConnection;

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
  private static final String TestName = "com.sun.ts.tests.jms.core.closedTopicConnection.ClosedTopicConnectionTests";

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

      TestUtil.logTrace("Closing queue Connection");
      tool.getDefaultTopicConnection().close();
    }
    TestUtil.logTrace("Connection closed");
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
        throw new Exception(
            "'jms_timeout' (milliseconds) in ts.jte must be > 0");
      }
      if (user == null) {
        throw new Exception("'user' in ts.jte must not be null");
      }
      if (password == null) {
        throw new Exception("'numProducers' in ts.jte must not be null");
      }
      if (mode == null) {
        throw new Exception("'platform.mode' in ts.jte must not be null");
      }

      // get ready for new test
      TestUtil.logTrace(
          "Getting Administrator and deleting any leftover destinations.");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed!", e);
    }
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
        TestUtil.logTrace("Cleanup: Closing Topic and Topic Connections");
        tool.closeAllConnections(connections);
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logErr("An error occurred while cleaning");
      throw new Fault("Cleanup failed!", e);
    }
  }

  /* Tests */

  /*
   * @testName: closedTopicConnectionCommitTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:111; JMS:JAVADOC:229;
   *
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionCommitTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TX_TOPIC, user, password);
      TestUtil.logTrace("Try to call commit with closed connection.");
      try {
        tool.getDefaultTopicSession().commit();
        TestUtil.logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        TestUtil.logTrace("Caught expected exception");
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
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicConnectionCommitTest");
    }
  }

  /*
   * @testName: closedTopicConnectionRollbackTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:SPEC:111; JMS:JAVADOC:231;
   *
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionRollbackTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call rollback() with closed connection.");
      try {
        tool.getDefaultTopicSession().rollback();
        TestUtil.logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        TestUtil.logTrace("Caught expected exception");
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
      throw new Fault("closedTopicConnectionRollbackTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionRecoverTest
   *
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:111; JMS:JAVADOC:235;
   * 
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionRecoverTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call recover() with closed connection.");
      try {
        tool.getDefaultTopicSession().recover();
        TestUtil.logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        TestUtil.logTrace("Caught expected exception");
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
      throw new Fault("closedTopicConnectionRecoverTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionCloseTest
   * 
   * @assertion_ids: JMS:SPEC:108; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * 
   * @test_Strategy: Close default Connection and call method on it.
   */

  public void closedTopicConnectionCloseTest() throws Fault {
    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call close again");
      tool.getDefaultTopicConnection().close();
    } catch (Exception e) {
      throw new Fault("closedTopicConnectionCloseTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionGetClientIDTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:512;
   * 
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionGetClientIDTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call getClientID");
      try {
        String foo = tool.getDefaultTopicConnection().getClientID();

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
      throw new Fault("closedTopicConnectionGetClientIDTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionGetMetaDataTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:516;
   * 
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionGetMetaDataTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call getMetaData");
      try {
        ConnectionMetaData foo = tool.getDefaultTopicConnection().getMetaData();

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
      throw new Fault("closedTopicConnectionGetMetaDataTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionStartTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:522;
   * 
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionStartTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call start");
      try {
        tool.getDefaultTopicConnection().start();
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
      throw new Fault("closedTopicConnectionStartTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionCreateTopicSessionTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:111;
   * 
   * 
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionCreateTopicSessionTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call createTopicSession");
      try {
        TopicSession foo = tool.getDefaultTopicConnection()
            .createTopicSession(true, Session.AUTO_ACKNOWLEDGE);

        TestUtil.logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        TestUtil.logTrace("Pass: threw expected error");
        passed = true;
      } catch (Exception e) {
        TestUtil.logErr("Fail: wrong exception was returned:", e);
      }
      if (!passed) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      throw new Fault("closedTopicConnectionCreateTopicSessionTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionSessionCloseTest
   * 
   * @assertion_ids: JMS:SPEC:201; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:111; JMS:JAVADOC:233;
   * 
   * @test_Strategy: Close default session and call method on it.
   */

  public void closedTopicConnectionSessionCloseTest() throws Fault {
    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logMsg("Try to call close on closed session.");
      tool.getDefaultTopicSession().close();
    } catch (Exception e) {
      TestUtil.logTrace(" " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("closedTopicConnectionSessionCloseTest");
    }
  }

  /*
   * @testName: closedTopicConnectionCreateTopicTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:111; JMS:JAVADOC:81;
   * 
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionCreateTopicTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to create topic with closed Connection.");
      try {
        Topic t = tool.getDefaultTopicSession()
            .createTopic("closedTopicConnectionCreateTopicTest");

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
      throw new Fault("closedTopicConnectionCreateTopicTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionCreateSubscriberTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:111; JMS:JAVADOC:83;
   *
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionCreateSubscriberTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to create Subscriber with closed Connection.");
      try {
        TopicSubscriber tS = tool.getDefaultTopicSession()
            .createSubscriber(tool.getDefaultTopic());

        TestUtil.logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        TestUtil.logTrace("Caught expected exception");
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
      throw new Fault("closedTopicConnectionCreateSubscriberTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionCreateSubscriberMsgSelectorTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:111; JMS:JAVADOC:85;
   * 
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionCreateSubscriberMsgSelectorTest()
      throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to create Receiver with closed Connection.");
      try {
        TopicSubscriber tS = tool.getDefaultTopicSession()
            .createSubscriber(tool.getDefaultTopic(), "TEST = 'test'", false);

        TestUtil.logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        TestUtil.logTrace("Caught expected exception");
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
      throw new Fault("closedTopicConnectionCreateSubscriberMsgSelectorTest",
          e);
    }
  }

  /*
   * @testName: closedTopicConnectionCreatePublisherTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:111; JMS:JAVADOC:91;
   * 
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionCreatePublisherTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to create Publisher with closed Connection.");
      try {
        TopicPublisher tP = tool.getDefaultTopicSession()
            .createPublisher(tool.getDefaultTopic());

        TestUtil.logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        TestUtil.logTrace("Caught expected exception");
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
      throw new Fault("closedTopicConnectionCreatePublisherTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionCreateTempTopicTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:111; JMS:JAVADOC:93;
   * 
   * @test_Strategy: Close default Connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionCreateTempTopicTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to create TemporaryTopic with closed connection.");
      try {
        TemporaryTopic tT = tool.getDefaultTopicSession()
            .createTemporaryTopic();

        TestUtil.logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        TestUtil.logTrace("Caught expected exception");
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
      throw new Fault("closedTopicConnectionCreateTempTopicTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionCreateMessageTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:111; JMS:JAVADOC:213;
   * 
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionCreateMessageTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to create message with closed connection.");
      try {
        Message m = tool.getDefaultTopicSession().createMessage();

        TestUtil.logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        TestUtil.logTrace("Caught expected exception");
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
      throw new Fault("closedTopicConnectionCreateMessageTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionCreateBytesMessageTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:111; JMS:JAVADOC:209;
   * 
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionCreateBytesMessageTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to create BytesMessage with closed connection.");
      try {
        BytesMessage m = tool.getDefaultTopicSession().createBytesMessage();

        TestUtil.logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        TestUtil.logTrace("Caught expected exception");
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
      throw new Fault("closedTopicConnectionCreateBytesMessageTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionCreateMapMessageTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:111; JMS:JAVADOC:211;
   * 
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionCreateMapMessageTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to create MapMessage with closed connection.");
      try {
        MapMessage m = tool.getDefaultTopicSession().createMapMessage();

        TestUtil.logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        TestUtil.logTrace("Caught expected exception");
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
      throw new Fault("closedTopicConnectionCreateMapMessageTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionCreateObjectMessageTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:111; JMS:JAVADOC:215;
   * 
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionCreateObjectMessageTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to create ObjectMessage with closed connection.");
      try {
        ObjectMessage m = tool.getDefaultTopicSession().createObjectMessage();
        if (m != null)
          TestUtil.logTrace("m=" + m);
        TestUtil.logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        TestUtil.logTrace("Caught expected exception");
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
      throw new Fault("closedTopicConnectionCreateObjectMessageTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionCreateObject2MessageTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:111; JMS:JAVADOC:217;
   * 
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionCreateObject2MessageTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace(
          "Try to create ObjectMessage(object) with closed connection.");
      try {
        String s = "Simple object";
        ObjectMessage m = tool.getDefaultTopicSession().createObjectMessage(s);
        if (m != null)
          TestUtil.logTrace("m=" + m);
        TestUtil.logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        TestUtil.logTrace("Caught expected exception");
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
      throw new Fault("closedTopicConnectionCreateObject2MessageTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionCreateStreamMessageTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:111; JMS:JAVADOC:219;
   * 
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionCreateStreamMessageTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to create StreamMessage with closed connection.");
      try {
        StreamMessage m = tool.getDefaultTopicSession().createStreamMessage();

        TestUtil.logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        TestUtil.logTrace("Caught expected exception");
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
      throw new Fault("closedTopicConnectionCreateStreamMessageTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionCreateTextMessageTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:111; JMS:JAVADOC:221;
   * 
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionCreateTextMessageTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to create TextMessage with closed connection.");
      try {
        TextMessage m = tool.getDefaultTopicSession().createTextMessage();

        TestUtil.logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        TestUtil.logTrace("Caught expected exception");
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
      throw new Fault("closedTopicConnectionCreateTextMessageTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionCreateText2MessageTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:111; JMS:JAVADOC:223;
   * 
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionCreateText2MessageTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to create TextMessage with closed connection.");
      try {
        TextMessage m = tool.getDefaultTopicSession()
            .createTextMessage("test message");

        TestUtil.logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        TestUtil.logTrace("Caught expected exception");
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
      throw new Fault("closedTopicConnectionCreateText2MessageTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionGetTransactedTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:111; JMS:JAVADOC:225;
   * 
   * @test_Strategy: Close default connection and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionGetTransactedTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call getTransacted() with closed connection.");
      try {
        boolean b = tool.getDefaultTopicSession().getTransacted();

        TestUtil.logTrace("Fail: Exception was not thrown!");
      } catch (javax.jms.IllegalStateException ise) {
        TestUtil.logTrace("Caught expected exception");
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
      throw new Fault("closedTopicConnectionGetTransactedTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionSubscriberCloseTest
   * 
   * @assertion_ids: JMS:SPEC:201; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:338;
   * 
   * @test_Strategy: Close default subscriber and call method on it.
   */

  public void closedTopicConnectionSubscriberCloseTest() throws Fault {
    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call close again");
      tool.getDefaultTopicSubscriber().close();
    } catch (Exception e) {
      throw new Fault("closedTopicConnectionSubscriberCloseTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionGetMessageSelectorTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:326;
   * 
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionGetMessageSelectorTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call getMessageSelector");
      try {
        String foo = tool.getDefaultTopicSubscriber().getMessageSelector();

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
      throw new Fault("closedTopicConnectionGetMessageSelectorTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionReceiveTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:332;
   * 
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionReceiveTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call receive");
      try {
        Message foo = tool.getDefaultTopicSubscriber().receive();

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
      throw new Fault("closedTopicConnectionReceiveTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionReceiveTimeoutTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:334;
   * 
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionReceiveTimeoutTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call receive(timeout)");
      try {
        Message foo = tool.getDefaultTopicSubscriber().receive(timeout);

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
      throw new Fault("closedTopicConnectionReceiveTimeoutTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionReceiveNoWaitTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:336;
   * 
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionReceiveNoWaitTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call receiveNoWait");
      try {
        Message foo = tool.getDefaultTopicSubscriber().receiveNoWait();

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
      throw new Fault("closedTopicConnectionReceiveNoWaitTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionGetNoLocalTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:79;
   * 
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionGetNoLocalTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call getNoLocal");
      try {
        boolean foo = tool.getDefaultTopicSubscriber().getNoLocal();

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
      throw new Fault("closedTopicConnectionGetNoLocalTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionSubscriberGetTopicTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:77;
   * 
   * @test_Strategy: Close default subscriber and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionSubscriberGetTopicTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call getTopic");
      try {
        Topic foo = tool.getDefaultTopicSubscriber().getTopic();

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
      throw new Fault("closedTopicConnectionSubscriberGetTopicTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionPublisherCloseTest
   * 
   * @assertion_ids: JMS:SPEC:201; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:315;
   * 
   * @test_Strategy: Close default publisher and call method on it.
   */

  public void closedTopicConnectionPublisherCloseTest() throws Fault {
    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call close again");
      tool.getDefaultTopicPublisher().close();
    } catch (Exception e) {
      throw new Fault("closedTopicConnectionPublisherCloseTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionGetDeliveryModeTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:303;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionGetDeliveryModeTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call getDeliveryMode");
      try {
        int foo = tool.getDefaultTopicPublisher().getDeliveryMode();

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
      throw new Fault("closedTopicConnectionGetDeliveryModeTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionGetDisableMessageIDTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:295;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionGetDisableMessageIDTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call getDisableMessageID");
      try {
        boolean foo = tool.getDefaultTopicPublisher().getDisableMessageID();

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
      throw new Fault("closedTopicConnectionGetDisableMessageIDTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionGetDisableMessageTimestampTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:299;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionGetDisableMessageTimestampTest()
      throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call getDisableMessageTimestamp");
      try {
        boolean foo = tool.getDefaultTopicPublisher()
            .getDisableMessageTimestamp();

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
      throw new Fault("closedTopicConnectionGetDisableMessageTimestampTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionGetPriorityTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:307;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionGetPriorityTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call getPriority");
      try {
        int foo = tool.getDefaultTopicPublisher().getPriority();

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
      throw new Fault("closedTopicConnectionGetPriorityTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionGetTimeToLiveTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:311;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  /**
   * Method Declaration.
   * 
   * 
   * @exception Fault
   *
   * @see
   */
  public void closedTopicConnectionGetTimeToLiveTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call getTimeToLive");
      try {
        long foo = tool.getDefaultTopicPublisher().getTimeToLive();

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
      throw new Fault("closedTopicConnectionGetTimeToLiveTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionSetDeliveryModeTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:301;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */
  public void closedTopicConnectionSetDeliveryModeTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call setDeliveryMode");
      try {
        tool.getDefaultTopicPublisher()
            .setDeliveryMode(Message.DEFAULT_DELIVERY_MODE);
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
      throw new Fault("closedTopicConnectionSetDeliveryModeTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionSetDisableMessageIDTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:293;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  /**
   * Method Declaration.
   * 
   * 
   * @exception Fault
   *
   * @see
   */
  public void closedTopicConnectionSetDisableMessageIDTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call setDisableMessageID");
      try {
        tool.getDefaultTopicPublisher().setDisableMessageID(true);
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
      throw new Fault("closedTopicConnectionSetDisableMessageIDTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionSetDisableMessageTimestampTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:297;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionSetDisableMessageTimestampTest()
      throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call setDisableMessageTimestamp");
      try {
        tool.getDefaultTopicPublisher().setDisableMessageTimestamp(true);
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
      throw new Fault("closedTopicConnectionSetDisableMessageTimestampTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionSetPriorityTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:305;
   *
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionSetPriorityTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call setPriority");
      try {
        tool.getDefaultTopicPublisher().setPriority(Message.DEFAULT_PRIORITY);
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
      throw new Fault("closedTopicConnectionSetPriorityTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionSetTimeToLiveTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:309;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionSetTimeToLiveTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call setTimeToLive");
      try {
        tool.getDefaultTopicPublisher()
            .setTimeToLive(Message.DEFAULT_TIME_TO_LIVE);
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
      throw new Fault("closedTopicConnectionSetTimeToLiveTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionPublisherGetTopicTest
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:97;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionPublisherGetTopicTest() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call getTopic");
      try {
        Topic foo = tool.getDefaultTopicPublisher().getTopic();

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
      throw new Fault("closedTopicConnectionPublisherGetTopicTest", e);
    }
  }

  /*
   * @testName: closedTopicConnectionPublish1Test
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:99;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionPublish1Test() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call publish(Message)");
      try {
        tool.getDefaultTopicPublisher().publish(new MessageTestImpl());
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
      throw new Fault("closedTopicConnectionPublish1Test", e);
    }
  }

  /*
   * @testName: closedTopicConnectionPublish2Test
   * 
   * @assertion_ids: JMS:SPEC:107; JMS:JAVADOC:107; JMS:JAVADOC:526;
   * JMS:JAVADOC:101;
   * 
   * @test_Strategy: Close default publisher and call method on it. Check for
   * IllegalStateException.
   */

  public void closedTopicConnectionPublish2Test() throws Fault {
    boolean passed = false;

    try {
      createAndCloseConnection(JmsTool.TOPIC, user, password);
      TestUtil.logTrace("Try to call publish(Message,int,int,long)");
      try {
        tool.getDefaultTopicPublisher().publish(new MessageTestImpl(),
            Message.DEFAULT_DELIVERY_MODE, Message.DEFAULT_PRIORITY,
            Message.DEFAULT_TIME_TO_LIVE);
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
      throw new Fault("closedTopicConnectionPublish2Test", e);
    }
  }

}
