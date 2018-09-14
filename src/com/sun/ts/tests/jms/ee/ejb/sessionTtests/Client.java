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
package com.sun.ts.tests.jms.ee.ejb.sessionTtests;

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

  private static final String testName = "com.sun.ts.tests.jms.ee.ejb.sessionTtests.Client";

  private static final String testDir = System.getProperty("user.dir");

  private static final long serialVersionUID = 1L;

  private transient Connection connr = null;

  @Resource(name = "jms/DURABLE_SUB_CONNECTION_FACTORY")
  private static ConnectionFactory cf;

  @Resource(name = "jms/MY_TOPIC")
  // private static Destination testDestination;
  private static Topic testDestination;

  private String name = "ctssub";

  // Harness req's
  private Properties props = null;

  // properties read from ts.jte file
  long timeout;

  String user;

  String password;

  String mode;

  @EJB(name = "ejb/SessionTestsT")
  private static TestsT beanRef;

  /* Run test in standalone mode */

  public static void main(String[] args) {
    Client theTestsT = new Client();
    Status s = theTestsT.run(args, System.out, System.err);

    s.exit();
  }

  /* Test setup: */

  /*
   * setup() is called before each test
   * 
   * Creates Administrator object and deletes all previous Destinations.
   * Individual tests create the JmsTool object with one default Queue and/or
   * Topic Connection, as well as a default Queue and Topic. TestsT that require
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

      if (cf == null || testDestination == null) {
        throw new Fault("@Resource injection failed");
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

  /*
   * @testName: simpleSendReceiveT
   * 
   * @assertion_ids: JMS:JAVADOC:504; JMS:JAVADOC:510; JMS:JAVADOC:242;
   * JMS:JAVADOC:244; JMS:JAVADOC:317; JMS:JAVADOC:334; JMS:JAVADOC:221;
   * 
   * @test_Strategy: Create a Text Message, send use a MessageProducer and
   * receive it use a MessageConsumer via a Topic
   */
  public void simpleSendReceiveT() throws Fault {
    String testMessage = "Just a test from simpleSendReceiveT";
    String messageReceived = null;

    try {
      connr = cf.createConnection(user, password);
      if (connr.getClientID() == null)
        connr.setClientID("cts");

      Session sessr = connr.createSession(true, Session.AUTO_ACKNOWLEDGE);
      TopicSubscriber recr = sessr.createDurableSubscriber(testDestination,
          name);

      try {
        recr.close();
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception closing topic subscriber: ", e);
      }
      try {
        connr.close();
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception closing connection: ", e);
      }

      beanRef.sendTextMessage_CT(testName, testMessage);
      messageReceived = beanRef.receiveTextMessage_CT();

      // Check to see if correct message received
      if (messageReceived == null) {
        throw new Exception("Null message received!");
      } else if (!messageReceived.equals(testMessage)) {
        throw new Exception("EJB didn't get the right message");
      } else {
        TestUtil.logMsg("Correct Message received");
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("simpleSendReceiveT");
    } finally {
      try {
        connr = cf.createConnection(user, password);

        Session sessr = connr.createSession(true, Session.AUTO_ACKNOWLEDGE);

        try {
          sessr.unsubscribe(name);
        } catch (Exception e) {
          TestUtil.logErr("Unexpected exception unsubscribing: ", e);
        }

        try {
          connr.close();
        } catch (Exception e) {
          TestUtil.logErr("Unexpected exception closing connection: ", e);
        }

      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception creating Connection: ", e);
      }

      try {
        if (null != beanRef)
          beanRef.remove();
      } catch (Exception e) {
        TestUtil.logErr("[Client] Ignoring Exception on " + "bean remove", e);
      }
    }
  }
}
