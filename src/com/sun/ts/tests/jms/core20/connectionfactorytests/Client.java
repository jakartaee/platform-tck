/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jms.core20.connectionfactorytests;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Enumeration;
import com.sun.javatest.Status;

public class Client extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core20.connectionfactorytests.Client";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  // JMS tool which creates and/or looks up the JMS administered objects
  private transient JmsTool tool = null;

  // JMS objects
  private transient ConnectionFactory cf = null;

  private transient QueueConnectionFactory qcf = null;

  private transient TopicConnectionFactory tcf = null;

  private transient JMSContext context = null;

  // Harness req's
  private Properties props = null;

  // properties read from ts.jte file
  long timeout;

  String user;

  String password;

  String mode;

  ArrayList connections = null;

  String vehicle = null;

  /* Run test in standalone mode */

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * Utility method to return the session mode as a String
   */
  private String printSessionMode(int sessionMode) {
    switch (sessionMode) {
    case JMSContext.SESSION_TRANSACTED:
      return "SESSION_TRANSACTED";
    case JMSContext.AUTO_ACKNOWLEDGE:
      return "AUTO_ACKNOWLEDGE";
    case JMSContext.CLIENT_ACKNOWLEDGE:
      return "CLIENT_ACKNOWLEDGE";
    case JMSContext.DUPS_OK_ACKNOWLEDGE:
      return "DUPS_OK_ACKNOWLEDGE";
    default:
      return "UNEXPECTED_SESSIONMODE";
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
        throw new Exception("'user' in ts.jte must not be null ");
      }
      if (password == null) {
        throw new Exception("'password' in ts.jte must not be null ");
      }
      if (mode == null) {
        throw new Exception("'platform.mode' in ts.jte must not be null");
      }
      connections = new ArrayList(5);
      vehicle = p.getProperty("vehicle");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed!", e);
    }
  }

  /* cleanup */

  /*
   * cleanup() is called after each test
   * 
   * @exception Fault
   */
  public void cleanup() throws Fault {
  }

  /*
   * @testName: qcfCreateJMSContextTest1
   *
   * @assertion_ids: JMS:JAVADOC:841;
   *
   * @test_Strategy: Creates a JMSContext with the default user identity and an
   * unspecified sessionMode. Tests API:
   *
   * QueueConnectionFactory.createContext() JMSContext.getSessionMode()
   */
  public void qcfCreateJMSContextTest1() throws Fault {
    boolean pass = true;
    try {
      // set up JmsTool for QUEUE_FACTORY setup
      TestUtil.logMsg("Setup JmsTool for QUEUE_FACTORY");
      tool = new JmsTool(JmsTool.QUEUE_FACTORY, user, password, mode);
      qcf = tool.getQueueConnectionFactory();

      TestUtil.logMsg("Test QueueConnectionFactory.createContext()");
      context = qcf.createContext();
      TestUtil.logMsg(
          "Verify that JMSContext.getSessionMode() returns JMSContext.AUTO_ACKNOWLEDGE");
      int expSessionMode = JMSContext.AUTO_ACKNOWLEDGE;
      int actSessionMode = context.getSessionMode();
      if (actSessionMode != expSessionMode) {
        TestUtil.logErr(
            "getSessionMode() returned " + printSessionMode(actSessionMode)
                + ", expected " + printSessionMode(expSessionMode));
        pass = false;
      }
      context.close();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("qcfCreateJMSContextTest1");
    }

    if (!pass) {
      throw new Fault("qcfCreateJMSContextTest1 failed");
    }
  }

  /*
   * @testName: qcfCreateJMSContextTest3
   *
   * @assertion_ids: JMS:JAVADOC:844;
   *
   * @test_Strategy: Creates a JMSContext with the specified user identity and
   * an unspecified sessionMode. Tests API:
   *
   * QueueConnectionFactory.createContext(String, String)
   * JMSContext.getSessionMode()
   */
  public void qcfCreateJMSContextTest3() throws Fault {
    boolean pass = true;
    try {
      // set up JmsTool for QUEUE_FACTORY setup
      TestUtil.logMsg("Setup JmsTool for QUEUE_FACTORY");
      tool = new JmsTool(JmsTool.QUEUE_FACTORY, user, password, mode);
      qcf = tool.getQueueConnectionFactory();

      TestUtil
          .logMsg("Test QueueConnectionFactory.createContext(String, String)");
      context = qcf.createContext(user, password);
      TestUtil.logMsg(
          "Verify that JMSContext.getSessionMode() returns JMSContext.AUTO_ACKNOWLEDGE");
      int expSessionMode = JMSContext.AUTO_ACKNOWLEDGE;
      int actSessionMode = context.getSessionMode();
      if (actSessionMode != expSessionMode) {
        TestUtil.logErr(
            "getSessionMode() returned " + printSessionMode(actSessionMode)
                + ", expected " + printSessionMode(expSessionMode));
        pass = false;
      }
      context.close();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("qcfCreateJMSContextTest3");
    }

    if (!pass) {
      throw new Fault("qcfCreateJMSContextTest3 failed");
    }
  }

  /*
   * @testName: qcfCreateConnectionTest
   *
   * @assertion_ids: JMS:JAVADOC:502;JMS:JAVADOC:504;
   *
   * @test_Strategy: Create a connection with the default user identity. Create
   * a connection with the specified user identity. Tests the following API's:
   *
   * QueueConnectionFactory.createConnection()
   * QueueConnectionFactory.createConnection(String, String)
   * QueueConnectionFactory.createQueueConnection()
   * QueueConnectionFactory.createQueueConnection(String, String)
   */
  public void qcfCreateConnectionTest() throws Fault {
    boolean pass = true;
    try {
      // set up JmsTool for QUEUE_FACTORY setup
      TestUtil.logMsg("Setup JmsTool for QUEUE_FACTORY");
      tool = new JmsTool(JmsTool.QUEUE_FACTORY, user, password, mode);
      qcf = tool.getQueueConnectionFactory();

      TestUtil.logMsg("Test QueueConnectionFactory.createQueueConnection()");
      QueueConnection qc = qcf.createQueueConnection();
      qc.close();

      TestUtil.logMsg(
          "Test QueueConnectionFactory.createQueueConnection(String, String) with valid credentials");
      qc = qcf.createQueueConnection(user, password);
      qc.close();

      TestUtil.logMsg("Test QueueConnectionFactory.createConnection()");
      Connection c = qcf.createConnection();
      c.close();

      TestUtil.logMsg(
          "Test QueueConnectionFactory.createConnection(String, String) with valid credentials");
      c = qcf.createConnection(user, password);
      c.close();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("qcfCreateConnectionTest");
    }

    if (!pass) {
      throw new Fault("qcfCreateConnectionTest failed");
    }
  }

  /*
   * @testName: tcfCreateJMSContextTest1
   *
   * @assertion_ids: JMS:JAVADOC:841;
   *
   * @test_Strategy: Creates a JMSContext with the default user identity and an
   * unspecified sessionMode. Tests API:
   *
   * TopicConnectionFactory.createContext() JMSContext.getSessionMode()
   */
  public void tcfCreateJMSContextTest1() throws Fault {
    boolean pass = true;
    try {
      // set up JmsTool for TOPIC_FACTORY setup
      TestUtil.logMsg("Setup JmsTool for TOPIC_FACTORY");
      tool = new JmsTool(JmsTool.TOPIC_FACTORY, user, password, mode);
      tcf = tool.getTopicConnectionFactory();

      TestUtil.logMsg("Test TopicConnectionFactory.createContext()");
      context = tcf.createContext();
      TestUtil.logMsg(
          "Verify that JMSContext.getSessionMode() returns JMSContext.AUTO_ACKNOWLEDGE");
      int expSessionMode = JMSContext.AUTO_ACKNOWLEDGE;
      int actSessionMode = context.getSessionMode();
      if (actSessionMode != expSessionMode) {
        TestUtil.logErr(
            "getSessionMode() returned " + printSessionMode(actSessionMode)
                + ", expected " + printSessionMode(expSessionMode));
        pass = false;
      }
      context.close();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("tcfCreateJMSContextTest1");
    }

    if (!pass) {
      throw new Fault("tcfCreateJMSContextTest1 failed");
    }
  }

  /*
   * @testName: tcfCreateJMSContextTest3
   *
   * @assertion_ids: JMS:JAVADOC:844;
   *
   * @test_Strategy: Creates a JMSContext with the specified user identity and
   * an unspecified sessionMode. Tests API:
   *
   * TopicConnectionFactory.createContext(String, String)
   * JMSContext.getSessionMode()
   */
  public void tcfCreateJMSContextTest3() throws Fault {
    boolean pass = true;
    try {
      // set up JmsTool for TOPIC_FACTORY setup
      TestUtil.logMsg("Setup JmsTool for TOPIC_FACTORY");
      tool = new JmsTool(JmsTool.TOPIC_FACTORY, user, password, mode);
      tcf = tool.getTopicConnectionFactory();

      TestUtil
          .logMsg("Test TopicConnectionFactory.createContext(String, String)");
      context = tcf.createContext(user, password);
      TestUtil.logMsg(
          "Verify that JMSContext.getSessionMode() returns JMSContext.AUTO_ACKNOWLEDGE");
      int expSessionMode = JMSContext.AUTO_ACKNOWLEDGE;
      int actSessionMode = context.getSessionMode();
      if (actSessionMode != expSessionMode) {
        TestUtil.logErr(
            "getSessionMode() returned " + printSessionMode(actSessionMode)
                + ", expected " + printSessionMode(expSessionMode));
        pass = false;
      }
      context.close();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new Fault("tcfCreateJMSContextTest3");
    }

    if (!pass) {
      throw new Fault("tcfCreateJMSContextTest3 failed");
    }
  }

  /*
   * @testName: tcfCreateConnectionTest
   *
   * @assertion_ids: JMS:JAVADOC:502;JMS:JAVADOC:504;
   *
   * @test_Strategy: Create a connection with the default user identity. Create
   * a connection with the specified user identity. Tests tye following API's:
   *
   * TopicConnectionFactory.createConnection()
   * TopicConnectionFactory.createConnection(String, String)
   * TopicConnectionFactory.createTopicConnection()
   * TopicConnectionFactory.createTopicConnection(String, String)
   */
  public void tcfCreateConnectionTest() throws Fault {
    boolean pass = true;
    try {
      // set up JmsTool for TOPIC_FACTORY setup
      TestUtil.logMsg("Setup JmsTool for TOPIC_FACTORY");
      tool = new JmsTool(JmsTool.TOPIC_FACTORY, user, password, mode);
      tcf = tool.getTopicConnectionFactory();

      TestUtil.logMsg("Test TopicConnectionFactory.createTopicConnection()");
      TopicConnection tc = tcf.createTopicConnection();
      tc.close();

      TestUtil.logMsg(
          "Test TopicConnectionFactory.createTopicConnection(String, String) with valid credentials");
      tc = tcf.createTopicConnection(user, password);
      tc.close();

      TestUtil.logMsg("Test TopicConnectionFactory.createConnection()");
      Connection c = tcf.createConnection();
      c.close();

      TestUtil.logMsg(
          "Test TopicConnectionFactory.createConnection(String, String) with valid credentials");
      c = tcf.createConnection(user, password);
      c.close();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("tcfCreateConnectionTest");
    }

    if (!pass) {
      throw new Fault("tcfCreateConnectionTest failed");
    }
  }

  /*
   * @testName: createConnectionExceptionTests
   *
   * @assertion_ids: JMS:JAVADOC:596;
   *
   * @test_Strategy: Try and create a connection with invalid user credentials.
   * Tests for JMSSecurityException.
   *
   * QueueConnectionFactory.createQueueConnection(String, String)
   * TopicConnectionFactory.createTopicConnection(String, String)
   */
  public void createConnectionExceptionTests() throws Fault {
    boolean pass = true;
    try {
      // set up JmsTool for QUEUE_FACTORY setup
      TestUtil.logMsg("Setup JmsTool for QUEUE_FACTORY");
      tool = new JmsTool(JmsTool.QUEUE_FACTORY, user, password, mode);
      qcf = tool.getQueueConnectionFactory();

      TestUtil.logMsg(
          "Test QueueConnectionFactory.createQueueConnection(String, String) with invalid credentials");
      TestUtil.logMsg("Verify JMSSecurityException is thrown");
      QueueConnection c = qcf.createQueueConnection("invalid", "invalid");
      TestUtil.logErr("Didn't throw expected JMSSecurityException");
      pass = false;
      c.close();
    } catch (JMSSecurityException e) {
      TestUtil.logMsg("Caught expected JMSSecurityException");
    } catch (Exception e) {
      TestUtil.logErr("Expected JMSSecurityException, received " + e);
      pass = false;
    }

    try {
      // set up JmsTool for TOPIC_FACTORY setup
      TestUtil.logMsg("Setup JmsTool for TOPIC_FACTORY");
      tool = new JmsTool(JmsTool.TOPIC_FACTORY, user, password, mode);
      tcf = tool.getTopicConnectionFactory();

      TestUtil.logMsg(
          "Test TopicConnectionFactory.createTopicConnection(String, String) with invalid credentials");
      TestUtil.logMsg("Verify JMSSecurityException is thrown");
      TopicConnection c = tcf.createTopicConnection("invalid", "invalid");
      TestUtil.logErr("Didn't throw expected JMSSecurityException");
      pass = false;
      c.close();
    } catch (JMSSecurityException e) {
      TestUtil.logMsg("Caught expected JMSSecurityException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      pass = false;
    }

    if (!pass) {
      throw new Fault("createConnectionExceptionTests failed");
    }
  }

  /*
   * @testName: createJMSContextExceptionTests
   *
   * @assertion_ids: JMS:JAVADOC:842; JMS:JAVADOC:843; JMS:JAVADOC:845;
   * JMS:JAVADOC:846; JMS:JAVADOC:848; JMS:JAVADOC:849; JMS:JAVADOC:850;
   * JMS:JAVADOC:851;
   *
   * @test_Strategy: Try and create a JMSContext with invalid user credentials
   * and invalid session mode. Tests for the exceptions: JMSRuntimeException and
   * JMSSecurityRuntimeException.
   *
   * QueueConnectionFactory.createContext(String, String)
   * QueueConnectionFactory.createContext(int)
   * TopicConnectionFactory.createContext(String, String)
   * TopicConnectionFactory.createContext(int)
   */
  public void createJMSContextExceptionTests() throws Fault {
    boolean pass = true;
    try {
      // set up JmsTool for QUEUE_FACTORY setup
      TestUtil.logMsg("Setup JmsTool for QUEUE_FACTORY");
      tool = new JmsTool(JmsTool.QUEUE_FACTORY, user, password, mode);
      qcf = tool.getQueueConnectionFactory();

      TestUtil.logMsg(
          "Test QueueConnectionFactory.createContext(String, String) with bad credentials");
      TestUtil.logMsg("Verify JMSRuntimeSecurityException is thrown");
      try {
        context = qcf.createContext("invalid", "invalid");
        TestUtil.logErr("Didn't throw expected JMSSecurityRuntimeException");
        pass = false;
        context.close();
      } catch (JMSSecurityRuntimeException e) {
        TestUtil.logMsg("Caught expected JMSRuntimeSecurityException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSSecurityRuntimeException, received " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Test QueueConnectionFactory.createContext(int) with bad session mode");
      TestUtil.logMsg("Verify JMSRuntimeException is thrown");
      try {
        context = qcf.createContext(-1);
        TestUtil.logErr("Didn't throw expected JMSRuntimeException");
        pass = false;
        context.close();
      } catch (JMSRuntimeException e) {
        TestUtil.logMsg("Caught expected JMSRuntimeException");
      } catch (Exception e) {
        TestUtil.logErr("Expected JMSRuntimeException, received " + e);
        pass = false;
      }

      // set up JmsTool for TOPIC_FACTORY setup
      TestUtil.logMsg("Setup JmsTool for TOPIC_FACTORY");
      tool = new JmsTool(JmsTool.TOPIC_FACTORY, user, password, mode);
      tcf = tool.getTopicConnectionFactory();

      TestUtil.logMsg(
          "Test TopicConnectionFactory.createContext(String, String) with bad credentials");
      TestUtil.logMsg("Verify JMSSecurityRuntimeException is thrown");
      try {
        context = tcf.createContext("invalid", "invalid");
        TestUtil.logErr("Didn't throw expected JMSSecurityRuntimeException");
        pass = false;
        context.close();
      } catch (JMSSecurityRuntimeException e) {
        TestUtil.logMsg("Caught expected JMSRuntimeSecurityException");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        pass = false;
      }

      TestUtil.logMsg(
          "Test TopicConnectionFactory.createContext(int) with bad session mode");
      TestUtil.logMsg("Verify JMSRuntimeException is thrown");
      try {
        context = tcf.createContext(-1);
        TestUtil.logErr("Didn't throw expected JMSRuntimeException");
        pass = false;
        context.close();
      } catch (JMSRuntimeException e) {
        TestUtil.logMsg("Caught expected JMSRuntimeException");
      } catch (Exception e) {
        TestUtil.logErr("Caught unexpected exception: " + e);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      throw new Fault("createJMSContextExceptionTests");
    }

    if (!pass) {
      throw new Fault("createJMSContextExceptionTests failed");
    }
  }
}
