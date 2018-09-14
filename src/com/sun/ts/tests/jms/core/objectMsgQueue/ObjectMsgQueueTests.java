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
package com.sun.ts.tests.jms.core.objectMsgQueue;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.*;
import java.io.*;
import java.util.*;
import com.sun.javatest.Status;

public class ObjectMsgQueueTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.objectMsgQueue.ObjectMsgQueueTests";

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
    ObjectMsgQueueTests theTests = new ObjectMsgQueueTests();
    Status s = theTests.run(args, System.out, System.err);

    s.exit();
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
        throw new Exception(
            "'jms_timeout' (milliseconds) in ts.jte must be > 0");
      }
      if (user == null) {
        throw new Exception("'numConsumers' in ts.jte must not be null");
      }
      if (password == null) {
        throw new Exception("'numProducers' in ts.jte must not be null");
      }
      if (mode == null) {
        throw new Exception("'platform.mode' in ts.jte must not be null");
      }
      queues = new ArrayList(2);

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
   * @testName: messageObjectCopyQTest
   * 
   * @assertion_ids: JMS:SPEC:85; JMS:JAVADOC:291;
   * 
   * @test_Strategy: Create an object message. Write a StringBuffer to the
   * message. modify the StringBuffer and send the msg, verify that it does not
   * effect the msg
   */

  public void messageObjectCopyQTest() throws Fault {
    boolean pass = true;

    try {
      ObjectMessage messageSentObject = null;
      ObjectMessage messageReceivedObject = null;
      StringBuffer sBuff = new StringBuffer("This is");
      String initial = "This is";

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      messageSentObject = tool.getDefaultQueueSession().createObjectMessage();
      messageSentObject.setObject(sBuff);
      sBuff.append("a test ");
      messageSentObject.setStringProperty("COM_SUN_JMS_TESTNAME",
          "messageObjectCopyQTest");
      tool.getDefaultQueueSender().send(messageSentObject);
      messageReceivedObject = (ObjectMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);
      logMsg("Ensure that changing the object did not change the message");
      StringBuffer s = (StringBuffer) messageReceivedObject.getObject();

      logTrace("s is " + s);
      if (s.toString().equals(initial)) {
        logTrace("Pass: msg was not changed");
      } else {
        logTrace("Fail: msg was changed!");
        pass = false;
      }
      if (!pass) {
        throw new Fault("Error: failures occurred during tests");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("messageObjectCopyQTest");
    }
  }

  /*
   * @testName: notWritableTest
   * 
   * @assertion_ids: JMS:JAVADOC:717;
   * 
   * @test_Strategy: Create an object message. Try to setObject upon receiving
   * the message. Verify that MessageNotWriteableException is thrown.
   */

  public void notWritableTest() throws Fault {
    boolean pass = true;
    StringBuffer sBuff = new StringBuffer("This is");
    String testName = "notWritableTest";

    try {
      ObjectMessage messageSentObject = null;
      ObjectMessage messageReceivedObject = null;

      // set up test tool for Queue
      tool = new JmsTool(JmsTool.QUEUE, user, password, mode);
      tool.getDefaultQueueConnection().start();
      messageSentObject = tool.getDefaultQueueSession().createObjectMessage();
      messageSentObject.setObject(sBuff);
      messageSentObject.setStringProperty("COM_SUN_JMS_TESTNAME", testName);
      tool.getDefaultQueueSender().send(messageSentObject);

      messageReceivedObject = (ObjectMessage) tool.getDefaultQueueReceiver()
          .receive(timeout);

      try {
        messageReceivedObject.setObject(sBuff);
        TestUtil
            .logErr("Error: expected MessageNotWriteableException not thrown");
        pass = false;
      } catch (MessageNotWriteableException ex) {
        TestUtil.logTrace("Got expected MessageNotWriteableException");
      }

      if (!pass)
        throw new Fault(testName);
    } catch (Exception e) {
      TestUtil.logErr(testName + " failed: ", e);
      throw new Fault(testName);
    }
  }
}
