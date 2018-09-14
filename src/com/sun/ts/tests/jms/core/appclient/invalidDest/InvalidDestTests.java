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
package com.sun.ts.tests.jms.core.appclient.invalidDest;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import javax.jms.*;
import java.io.*;
import java.util.*;
import com.sun.javatest.Status;

public class InvalidDestTests extends ServiceEETest {
  private static final String testName = "com.sun.ts.tests.jms.core.appclient.invalidDest.InvalidDestTests";

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

  /**
   * Main method is used when not run from the JavaTest GUI.
   * 
   * @param args
   */
  public static void main(String[] args) {
    InvalidDestTests theTests = new InvalidDestTests();
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
  }

  /* Tests */
  /*
   * @testName: invalidDestinationTests
   *
   * @assertion_ids: JMS:JAVADOC:502; JMS:JAVADOC:504; JMS:JAVADOC:510;
   * JMS:JAVADOC:638; JMS:JAVADOC:639; JMS:JAVADOC:641; JMS:JAVADOC:643;
   * JMS:JAVADOC:644; JMS:JAVADOC:646; JMS:JAVADOC:647; JMS:JAVADOC:649;
   *
   * @test_Strategy: 1. Create a Session with Topic Configuration, using a null
   * Destination/Topic to verify InvalidDestinationException is thrown with
   * various methods
   */

  public void invalidDestinationTests() throws Fault {
    String lookup = "DURABLE_SUB_CONNECTION_FACTORY";

    try {
      boolean pass = true;
      Topic dummyT = null;

      JmsTool tool = new JmsTool(JmsTool.COMMON_T, user, password, lookup,
          mode);
      tool.getDefaultConnection().close();

      Connection newConn = tool.getNewConnection(JmsTool.COMMON_T, user,
          password, lookup);
      if (newConn.getClientID() == null) {
        newConn.setClientID("cts");
      }
      Session newSession = newConn.createSession(false,
          Session.AUTO_ACKNOWLEDGE);

      try {
        newSession.unsubscribe("foo");
        logErr(
            "Error: unsubscribe(foo) didn't throw expected InvalidDestinationException");
        pass = false;
      } catch (InvalidDestinationException ex) {
        logMsg(
            "Got expected InvalidDestinationException from unsubscribe(foo)");
      } catch (Exception e) {
        logErr("Error: unsubscribe(foo) throw incorrect Exception: ", e);
        pass = false;
      }

      try {
        newSession.createDurableSubscriber(dummyT, "cts");
        logErr(
            "Error: createDurableSubscriber(null, String) didn't throw expected InvalidDestinationException");
        pass = false;
      } catch (InvalidDestinationException ex) {
        logMsg(
            "Got expected InvalidDestinationException from createDurableSubscriber(null, String)");
      } catch (Exception e) {
        logErr(
            "Error: createDurableSubscriber(null, String) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        newSession.createDurableSubscriber(dummyT, "cts", "TEST = 'test'",
            true);
        logErr("Error: createDurableSubscriber(null, String, String, boolean) "
            + "didn't throw expected InvalidDestinationException");
        pass = false;
      } catch (InvalidDestinationException ex) {
        logMsg(
            "Got expected InvalidDestinationException from createDurableSubscriber(null, String, String, boolean)");
      } catch (Exception e) {
        logErr(
            "Error: createDurableSubscriber(null, String, String, boolean) throw incorrect Exception: ",
            e);
        pass = false;
      }

      try {
        newConn.close();
      } catch (Exception ex) {
        logErr("Error closing new Connection", ex);
      }

      if (pass != true)
        throw new Fault("invalidDestinationTests");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("invalidDestinationTests");
    }
  }

}
