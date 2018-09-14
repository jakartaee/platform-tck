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
 * @(#)eventClient1.java	1.19 03/05/16
 */

package com.sun.ts.tests.connector.xa.event;

import java.io.Serializable;
import java.util.Vector;
import java.util.Properties;

import com.sun.javatest.Status;

import java.sql.*;
import javax.sql.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;
import com.sun.ts.tests.connector.util.DBSupport;
import com.sun.ts.tests.common.connector.whitebox.TSDataSource;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;

public class eventClient1 extends ServiceEETest implements Serializable {

  private TSNamingContext nctx = null;

  private TSConnection con = null;

  private String whitebox_xa = null;

  private TSDataSource ds1 = null;

  private DBSupport dbutil = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    eventClient1 theTests = new eventClient1();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: whitebox-xa, JNDI name of TS WhiteBox;
   *
   * @class.testArgs: -ap tssql.stmt
   *
   */
  public void setup(String[] args, Properties p) throws Fault {

    // Get JNDI lookups for both adapters. The harness will throw if these
    // properties can not be retrieved, so there is no need for error checking
    // here.
    whitebox_xa = p.getProperty("whitebox-xa");
    logMsg("Using: " + whitebox_xa);

    // Construct our DBSupport object. This object performs interactions
    // on a table, based on the properties object supplied.
    try {
      dbutil = new DBSupport();
    } catch (Exception e) {
      throw new Fault(
          "Exception constructing DBSupport object: " + e.getMessage(), e);
    }

    // Obtain a TSDataSource object to interact with our resource adapter.
    try {
      nctx = new TSNamingContext();
      ds1 = (TSDataSource) nctx.lookup(whitebox_xa);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Exception during JNDI lookup: " + e.getMessage());
    }
  }

  /*
   * @testName: testConnectionEventListener
   *
   * @assertion_ids: Connector:SPEC:32; Connector:SPEC:35; Connector:SPEC:28;
   * Connector:JAVADOC:155; Connector:JAVADOC:156; Connector:JAVADOC:157;
   * Connector:JAVADOC:158; Connector:JAVADOC:159; Connector:JAVADOC:160;
   *
   * @test_Strategy: Call Con.close and verify CONNECTION_CLOSED event has been
   * sent to the ConnectionEventListener through
   * JdbcConnectionEventListerner.sendEvent.
   */
  public void testConnectionEventListener() throws Fault {
    boolean b = false;
    try {
      con = ds1.getConnection();
      TestUtil.logMsg("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    try {
      // Turn logging on for close method; check for CONNECTION_CLOSED
      ds1.setLogFlag(true);
      con.close();
      ds1.setLogFlag(false);

      // Check if the connection event was called.
      String toCheck = "TSConnectionEventListener.sendEvent:CONNECTION_CLOSED:";
      Vector log = ds1.getLog();
      if (log.contains(toCheck)) {
        b = true;
      }

      TestUtil.logTrace(log.toString());

      if (b == true) {
        TestUtil.logMsg("CONNECTION_CLOSED called correctly.");
      } else {
        throw new Fault("CONNECTION_CLOSED event was not called.");
      }

      // Clean up log
      ds1.clearLog();

    } catch (Exception e) {
      TestUtil.logMsg("Exception caught on closing connection:");
      throw new Fault(e.getMessage(), e);
    }

  }

  /* cleanup */
  public void cleanup() throws Fault {
    TestUtil.logMsg("Cleanup");
    try {
      ds1.clearLog();
      TestUtil.logTrace("Closing connection in cleanup.");
      con.close();
    } catch (Exception sqle) {
      TestUtil.logErr("Exception on cleanup: " + sqle.getMessage(), sqle);
    }
  }
}
