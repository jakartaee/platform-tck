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
package com.sun.ts.tests.jms.core.appclient.queueconn;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.*;
import com.sun.javatest.Status;

public class QueueConnTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.appclient.queueconn.QueueConnTests";

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
    QueueConnTests theTests = new QueueConnTests();
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
   * @testName: connStoppedQueueTest
   *
   * @assertion_ids: JMS:JAVADOC:272; JMS:SPEC:100; JMS:SPEC:98; JMS:SPEC:99;
   * JMS:JAVADOC:522; JMS:JAVADOC:524; JMS:JAVADOC:120; JMS:JAVADOC:221;
   * JMS:JAVADOC:198; JMS:JAVADOC:334;
   * 
   * @test_Strategy: Stop the connection. Send a msg; start the connection and
   * receive the msg. Should get the message
   */

  public void connStoppedQueueTest() throws Fault {
    boolean pass = true;

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      tool.getDefaultQueueConnection().stop();

      TestUtil.logTrace("Creating 1 TextMessage");
      messageSent = tool.getDefaultQueueSession().createTextMessage();
      messageSent.setText("Message from connStoppedQueueTest");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "connStoppedQueueTest");

      TestUtil.logTrace("Sending a TextMessage");
      tool.getDefaultQueueSender().send(messageSent);

      TestUtil.logTrace("Receiving message");
      tool.getDefaultQueueConnection().start();
      messageReceived = (TextMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      if (messageReceived.getText().equals(messageSent.getText())) {
        TestUtil.logMsg("Pass: Received correct message");
      } else {
        throw new Exception("didn't get the right message");
      }

    } catch (Exception e) {
      TestUtil.logErr("connStoppedQueueTest failed: ", e);
      throw new Fault("connStoppedQueueTest", e);
    }
  }

  /*
   * @testName: closedQueueConnectionNoForcedAckTest
   *
   * @assertion_ids: JMS:JAVADOC:272; JMS:SPEC:105; JMS:JAVADOC:429;
   * JMS:SPEC:115;
   *
   * @test_Strategy: Send and receive single message, don't acknowledge it.
   * close the queue connection, get the message with a second connection.
   */

  public void closedQueueConnectionNoForcedAckTest() throws Fault {
    boolean pass = true;

    try {
      TextMessage messageSent = null;
      TextMessage messageReceived = null;
      QueueSession qSession = null;
      QueueReceiver qReceiver = null;
      QueueSender qSender = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);

      QueueConnection newConn = (QueueConnection) tool
          .getNewConnection(JmsTool.QUEUE, user, password);
      connections.add(newConn);

      qSession = newConn.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
      tool.getDefaultQueueReceiver().close();
      tool.getDefaultQueueSession().close();

      qReceiver = qSession.createReceiver(tool.getDefaultQueue());
      qSender = qSession.createSender(tool.getDefaultQueue());
      TestUtil.logMsg("create a new connection");
      newConn.start();

      TestUtil.logMsg("Creating 1 TextMessage");
      messageSent = qSession.createTextMessage();
      messageSent.setText("just a test");
      messageSent.setStringProperty("COM_SUN_JMS_TESTNAME",
          "closedQueueConnectionNoForcedAckTest");
      TestUtil.logMsg("Sending a TextMessage");
      qSender.send(messageSent);

      TestUtil.logMsg("Receive the TextMessage");
      messageReceived = (TextMessage) qReceiver.receive(timeout);
      qReceiver.close();
      TestUtil.logMsg("Close the connection with no ack of message received");
      newConn.close();

      TestUtil.logMsg(
          "Use default connection to retrieve the unacknowledged message");
      qSession = tool.getDefaultQueueConnection().createQueueSession(false,
          Session.CLIENT_ACKNOWLEDGE);
      qReceiver = qSession.createReceiver(tool.getDefaultQueue());
      tool.getDefaultQueueConnection().start();

      messageReceived = (TextMessage) qReceiver.receive(timeout);
      if (messageReceived == null) {
        TestUtil.logErr("Fail: no message received.");
        pass = false;
      } else if (messageReceived.getText().equals(messageSent.getText())) {
        TestUtil.logMsg("Pass: received correct msg");
      } else {
        TestUtil.logErr("Fail: didnt get correct msg");
        pass = false;
      }

      try {
        messageReceived.acknowledge();
      } catch (Exception e) {
        pass = false;
        TestUtil.logErr("Exception thrown on ack!", e);
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("closedQueueConnectionNoForcedAckTest");
    }
  }

}
