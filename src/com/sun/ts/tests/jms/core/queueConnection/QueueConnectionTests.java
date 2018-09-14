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
package com.sun.ts.tests.jms.core.queueConnection;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.*;
import com.sun.javatest.Status;

public class QueueConnectionTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.queueConnection.QueueConnectionTests";

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
    QueueConnectionTests theTests = new QueueConnectionTests();
    Status s = theTests.run(args, System.out, System.err);

    s.exit();
  }

  /* Test setup: */

  /*
   * setup() is called before each test
   * 
   * Creates Administrator object and deletes all previous Destinations.
   * Individual tests create the JmsTool object with one default Queue
   * Connection, as well as a default Queue and Topic. Tests that require
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
      connections = new ArrayList(5);

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
        TestUtil.logMsg("Cleanup: Closing Queue Connections");
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
   * @testName: connNotStartedQueueTest
   *
   * @assertion_ids: JMS:JAVADOC:272; JMS:SPEC:97;
   *
   * @test_Strategy: Send two messages to a queue; Receive only the second
   * message using selectors; Close receiver; Create another connection without
   * starting it; Create a new receiver in new connection and try to receive
   * first message with receiveNoWait() Should not get a message
   */
  public void connNotStartedQueueTest() throws Fault {
    boolean pass = true;

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      QueueReceiver qRec = null;

      QueueConnection newConn = null;
      QueueSession newSess = null;
      QueueReceiver newRec = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueReceiver().close();
      qRec = tool.getDefaultQueueSession()
          .createReceiver(tool.getDefaultQueue(), "targetMessage = TRUE");
      tool.getDefaultQueueConnection().start();

      TestUtil.logMsg("Creating TextMessage");
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "connNotStartedQueueTest");

      TestUtil.logMsg("Sending two TextMessages");
      messageSent.setBooleanProperty("targetMessage", false);
      tool.getDefaultQueueSender().send(messageSent);
      messageSent.setBooleanProperty("targetMessage", true);
      tool.getDefaultQueueSender().send(messageSent);

      TestUtil.logMsg("Receiving second message only");
      messageReceived = (TextMessage) qRec.receive(timeout);
      if (messageReceived == null) {
        TestUtil.logErr("Fail: Did not receive any message");
        pass = false;
      } else if (messageReceived.getBooleanProperty("targetMessage") == false) {
        TestUtil.logErr("Fail: Received incorrect message");
        pass = false;
      }

      TestUtil
          .logMsg("Closing receiver, creating new connection without starting");
      qRec.close();
      tool.getDefaultQueueConnection().close();

      newConn = (QueueConnection) tool.getNewConnection(JmsTool.QUEUE, user,
          password);
      connections.add(newConn);

      newSess = newConn.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
      newRec = newSess.createReceiver(tool.getDefaultQueue());

      TestUtil.logMsg("receiving first message");
      messageReceived = (TextMessage) newRec.receiveNoWait();

      // message should be null
      if (messageReceived == null) {
        TestUtil.logMsg("Pass: message not recevied");
      } else {
        TestUtil.logErr(
            "Fail: message received even though connection not started!");
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("connNotStartedQueueTest");
    }
  }

  /*
   * @testName: metaDataTests
   * 
   * @assertion_ids: JMS:JAVADOC:486; JMS:JAVADOC:488; JMS:JAVADOC:490;
   * JMS:JAVADOC:492; JMS:JAVADOC:494; JMS:JAVADOC:496; JMS:JAVADOC:498;
   * 
   * @test_Strategy: Create a QueueConnection to get ConnectionMetaData. Verify
   * that all Content of the ConnectionMetaData matches the type defined in API
   * and JMS API version is correct.
   */

  public void metaDataTests() throws Fault {
    boolean pass = true;
    ConnectionMetaData data = null;

    try {
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      data = tool.getDefaultQueueConnection().getMetaData();

      if (!verifyMetaData(data))
        pass = false;

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        tool.getDefaultQueueConnection().close();
      } catch (Exception e) {
        logErr("Error closing QueueConnection in metaDataTests : ", e);
      }
    }

    try {
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);
      data = tool.getDefaultConnection().getMetaData();

      if (!verifyMetaData(data))
        pass = false;

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        tool.getDefaultConnection().close();
      } catch (Exception e) {
        logErr("Error closing Connection in metaDataTests : ", e);
      }
    }

    if (!pass) {
      throw new Fault("Error: metaDataTests failed");
    }
  }

  private boolean verifyMetaData(ConnectionMetaData data) {
    boolean pass = true;

    try {
      String tmp = data.getJMSVersion();
      TestUtil.logTrace("JMSVersion=" + tmp);

      if (!tmp.equals(JmsTool.JMS_VERSION)) {
        logErr("Error: incorrect JMSVersion=" + tmp);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Error: incorrect type returned for JMSVersion: ", e);
      pass = false;
    }

    try {
      int tmp = data.getJMSMajorVersion();
      TestUtil.logTrace("JMSMajorVersion=" + tmp);

      if (tmp != JmsTool.JMS_MAJOR_VERSION) {
        logErr("Error: incorrect JMSMajorVersion=" + tmp);
        pass = false;
      }
    } catch (Exception e) {
      logErr("Error: incorrect type returned for JMSMajorVersion: ", e);
      pass = false;
    }

    try {
      int tmp = data.getJMSMinorVersion();
      TestUtil.logTrace("JMSMinorVersion=" + tmp);
    } catch (Exception e) {
      logErr("Error: incorrect type returned for JMSMinorVersion: ", e);
      pass = false;
    }

    try {
      String tmp = data.getJMSProviderName();
      TestUtil.logTrace("JMSProviderName=" + tmp);
    } catch (Exception e) {
      logErr("Error: incorrect type returned for JMSProviderName: ", e);
      pass = false;
    }

    try {
      String tmp = data.getProviderVersion();
      TestUtil.logTrace("JMSProviderVersion=" + tmp);
    } catch (Exception e) {
      logErr("Error: incorrect type returned for ProviderVersion: ", e);
      pass = false;
    }

    try {
      int tmp = data.getProviderMajorVersion();
      TestUtil.logTrace("ProviderMajorVersion=" + tmp);
    } catch (Exception e) {
      logErr("Error: incorrect type returned for ProviderMajorVersion: ", e);
      pass = false;
    }

    try {
      int tmp = data.getProviderMinorVersion();
      TestUtil.logTrace("ProviderMinorVersion=" + tmp);
    } catch (Exception e) {
      logErr("Error: incorrect type returned for ProviderMinorVersion: ", e);
      pass = false;
    }
    return pass;
  }
}
