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
 * @(#)Client.java	1.8 03/05/16
 */
package com.sun.ts.tests.jms.ee.ejb.sessionQtests;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.tests.jms.commonee.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import javax.jms.*;
import java.io.*;
import javax.ejb.EJB;
import java.util.*;
import com.sun.javatest.Status;
import javax.annotation.Resource;

public class Client extends EETest {

  private static final String testName = "com.sun.ts.tests.jms.ee.ejb.sessionQtests.Client";

  private static final String testDir = System.getProperty("user.dir");

  // Harness req's
  private Properties props = null;

  // properties read from ts.jte file
  long timeout;

  String user;

  String password;

  String mode;

  @EJB(name = "ejb/SessionTestsQ")
  private static Tests beanRef;

  /* Run test in standalone mode */

  public static void main(String[] args) {
    Client theTests = new Client();
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
   * @class.setup_props: jms_timeout; user; password; platform.mode;
   * 
   */
  public void setup(String[] args, Properties p) throws Fault {
    try {

      if (beanRef == null) {
        throw new Fault("@EJB injection failed");
      }

      props = p;

      // get props
      timeout = Long.parseLong(p.getProperty("jms_timeout"));
      user = p.getProperty("user");
      password = p.getProperty("password");
      mode = p.getProperty("platform.mode");

      // check props for errors
      if (timeout < 1) {
        throw new Fault("'jms_timeout' (milliseconds) in ts.jte must be > 0");
      }
      if (user == null) {
        throw new Fault("'user' in ts.jte must not be null ");
      }
      if (password == null) {
        throw new Fault("'password' in ts.jte must not be null ");
      }
      if (mode == null) {
        throw new Fault("'platform.mode' in ts.jte must not be null");
      }

      beanRef.initLogging(props);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed!", e);
    }
  }

  /*
   * cleanup() is called after each test
   */
  public void cleanup() throws Fault {
  }

  private void flushTheQueue() throws Fault {
    JmsTool tool = null;
    try {
      tool = new JmsTool(JmsTool.COMMON_Q, user, password, mode);

      TestUtil.logTrace("Closing default Connection");
      tool.getDefaultConnection().close();

    } catch (Exception e) {
      TestUtil.logErr("Error creating JmsTool and closing Connection", e);
    } finally {
      try {
        tool.flushDestination();
      } catch (Exception e) {
        TestUtil.logErr("Error flush Destination: ", e);
      }
    }
  }

  /*
   * @testName: simpleSendReceiveQueueTest
   * 
   * @assertion_ids: JMS:JAVADOC:334; JMS:JAVADOC:122; JMS:JAVADOC:504;
   * JMS:JAVADOC:510; JMS:JAVADOC:242; JMS:JAVADOC:244; JMS:JAVADOC:221;
   * JMS:JAVADOC:317;
   * 
   * @test_Strategy: Create a Text Message, send use a MessageProducer and
   * receive it use a MessageConsumer via a Queue
   */
  public void simpleSendReceiveQueueTest() throws Fault {
    String testMessage = "Just a test";
    String messageReceived = null;

    try {

      beanRef.sendTextMessage_CQ(testName, testMessage);
      messageReceived = beanRef.receiveTextMessage_CQ();

      // Check to see if correct message received
      if (messageReceived != null) {
        if (messageReceived.equals(testMessage)) {
          logMsg("Message text: \"" + messageReceived + "\"");
          logMsg("Received correct message");
        } else {
          throw new Exception("didn't get the right message");
        }
      } else {
        throw new Exception("didn't get any message");
      }
    } catch (Exception e) {
      logErr("simpleSendReceiveQueueTest failed: ", e);
      throw new Fault("simpleSendReceiveQueueTest failed !", e);
    } finally {
      try {
        flushTheQueue();
        if (null != beanRef)
          beanRef.remove();
      } catch (Exception e) {
        TestUtil.logErr("[Client] Ignoring Exception on " + "bean remove", e);
      }
    }
  }

  /*
   * @testName: selectorAndBrowserTests
   *
   * @assertion_ids: JMS:JAVADOC:502; JMS:JAVADOC:504; JMS:JAVADOC:510;
   * JMS:JAVADOC:242; JMS:JAVADOC:244; JMS:JAVADOC:246; JMS:JAVADOC:317;
   * JMS:JAVADOC:334; JMS:JAVADOC:258; JMS:JAVADOC:260; JMS:JAVADOC:338;
   * JMS:JAVADOC:221; JMS:SPEC:148; JMS:SPEC:149; JMS:JAVADOC:278;
   * JMS:JAVADOC:280; JMS:JAVADOC:282; JMS:JAVADOC:284; JMS:JAVADOC:288;
   *
   * @test_Strategy: 1. Create two TextMessages, send use a MessageProducer 2.
   * Create a QueueBrowser to browse the Queue so that all two messages can be
   * seen. 3. Use the QueueBrowser to test getQueue 4. Create a QueueBrowser
   * with selector to browse the Queue so that only one message can be seen; 5.
   * Use the above QueueBrowser to test getMessageSelector 6. Create a
   * MessageConsumer with a message selector so that only last message received.
   * 7. Then create another MessageConsumer to verify all messages except the
   * last message can be received from the Queue.
   */

  public void selectorAndBrowserTests() throws Fault {
    String testMessage = "Just a test: selectorAndBrowserTests";
    boolean pass = true;
    String messageReceived = null;

    try {

      beanRef.sendMessageP_CQ(testName, testMessage, false);
      beanRef.sendMessagePP_CQ(testName, testMessage, true, "TEST", "test");

      int msgNum = beanRef.browseTextMessage_CQ(2, testMessage);
      TestUtil.logTrace("Default browser found " + msgNum + " messages");

      if (!beanRef.getQueue()) {
        TestUtil.logErr("Error: QueueBrowser.getQueue test failed");
        pass = false;
      }

      msgNum = beanRef.browseMessageS_CQ(1, testMessage, "TEST = 'test'");
      TestUtil.logTrace("Selective browser find " + msgNum + " messages");

      if (!beanRef.getSelector("TEST = 'test'")) {
        TestUtil.logErr("Error: QueueBrowser.getMessageSelector test failed");
        pass = false;
      }

      if (pass != true)
        throw new Fault("selectorAndBrowserTests Failed!");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("selectorAndBrowserTests");
    } finally {
      try {
        flushTheQueue();
        if (null != beanRef)
          beanRef.remove();
      } catch (Exception e) {
        TestUtil.logErr("[Client] Ignoring Exception on " + "bean remove", e);
      }
    }
  }
}
