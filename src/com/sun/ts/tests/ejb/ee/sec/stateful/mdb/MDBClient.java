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

package com.sun.ts.tests.ejb.ee.sec.stateful.mdb;

import java.util.Properties;
import javax.ejb.EJBHome;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.porting.*;

import com.sun.javatest.Status;

/**
 * The MDBClient class invokes a test session bean, which will ask and the
 * message driven bean to send a text, byte, map, stream, and object message to
 * a queue
 */

public class MDBClient extends EETest {

  private TSLoginContext lc = null;

  private String username = "";

  private String password = "";

  // Naming specific member variables
  private TSNamingContextInterface context = null;

  private MDB_SND_Test hr = null;

  private MDB_SND_TestHome home = null;

  private Properties props = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    MDBClient theTests = new MDBClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: jms_timeout; authuser; authpassword;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      TestUtil.logTrace("in client setup");

      context = new TSNamingContext();

      username = props.getProperty("authuser");
      password = props.getProperty("authpassword");
      lc = new TSLoginContext();
      lc.login(username, password);

      home = (MDB_SND_TestHome) context.lookup("java:comp/env/ejb/MDB_SND_Test",
          MDB_SND_TestHome.class);
      hr = home.create(props);
      TestUtil.logTrace("about to delete all destinations!");
      if (hr.isThereSomethingInTheQueue()) {
        TestUtil.logTrace("Error: message(s) left in Q");
        hr.cleanTheQueue();
      } else {
        TestUtil.logTrace("Nothing left in queue");
      }
      logMsg("Setup ok;");
    } catch (Exception e) {
      throw new Fault("Setup Failed!", e);
    }
  }

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:61.7; EJB:SPEC:81.4; EJB:SPEC:827; EJB:SPEC:822;
   * EJB:SPEC:817; EJB:SPEC:815; EJB:SPEC:814; EJB:SPEC:786; EJB:SPEC:785
   *
   * @test_Strategy: 1. Create a stateful Session EJB Bean. 2. Have the EJB
   * component send a message to a Queue Destination. handled by a
   * message-driven bean 3. Set the MDB with run-as-specified-identity
   * <Administrator> 4. Create another stateful session bean with a method 5.
   * Protect the method with security role <Employee> 6. Since MDB uses
   * run-as-specified-identity, <Administrator>, which is subset of the security
   * role <Employee> set on the method permission, so access to the method
   * should be allowed. 7. Verify call returns successfully by the message sent
   * back by MDB.
   *
   */

  public void test1() throws Fault {
    String messageType = "TextMessage";
    String matchMe = "TextMessageFromMsgBean";

    try {
      hr.askMDBToSendAMessage(messageType);
      if (!hr.checkOnResponse(matchMe)) {
        TestUtil.logTrace("Error: didn't get expected response from mdb");
        throw new Fault("ERROR: Positive test failed");
      }
      TestUtil.logTrace("Test passed!");
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    } finally {
      // send ObjectMessage to remove Test Bean
      try {
        hr.askMDBToSendAMessage("ObjectMessage");
      } catch (Exception ee) {
        TestUtil.logErr("Exception removing stateful test bean", ee);
      }
    }
  }

  public void cleanup() throws Fault {
    try {
      if (hr.isThereSomethingInTheQueue()) {
        TestUtil.logTrace("Error: message(s) left in Q");
        hr.cleanTheQueue();
      } else {
        TestUtil.logTrace("Nothing left in queue");
      }
      if (hr != null) {
        TestUtil.logTrace("Removing client stateful session bean");
        hr.remove();
      }
      logMsg("End  of client cleanup;");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    ;
  }
}
