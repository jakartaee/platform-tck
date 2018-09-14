/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)compatClient1.java	1.36 03/05/16
 */

package com.sun.ts.tests.connector.localTx.compat;

import java.io.*;
import java.util.*;
import com.sun.javatest.Status;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.connector.util.DBSupport;
import com.sun.ts.tests.common.connector.whitebox.TSDataSource;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;

/*
 * This is used to test that connection to RA's which are using 
 * a connector 1.0 descriptor still works.  This code used to be
 * in the compat13/connector subdir.
 */
public class compatClient1 extends ServiceEETest implements Serializable {

  private TSNamingContext nctx = null;

  private TSConnection con = null;

  private String whitebox_tx = null;

  private String whitebox_tx_param = null;

  private TSDataSource ds1 = null;

  private TSDataSource ds2 = null;

  private String uname = null;

  private String password = null;

  private DBSupport dbutil = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    compatClient1 theTests = new compatClient1();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: oldwhitebox-tx; oldwhitebox-tx-param; rauser1, user
   * name; rapassword1, password for rauser1;
   *
   * @class.testArgs: -ap tssql.stmt
   *
   */
  public void setup(String[] args, Properties p) throws Fault {

    // Get JNDI lookups for both adapters. The harness will throw if these
    // properties can not be retrieved, so there is no need for error checking
    // here.
    whitebox_tx = p.getProperty("oldwhitebox-tx");
    whitebox_tx_param = p.getProperty("oldwhitebox-tx-param");

    logMsg("Using: " + whitebox_tx);
    logMsg("Using: " + whitebox_tx_param);

    // For application level sign on
    uname = p.getProperty("rauser1");
    password = p.getProperty("rapassword1");

    // Construct our DBSupport object. This object performs interactions
    // on a table, based on the properties object supplied.
    try {
      dbutil = new DBSupport();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logErr("###", e);
      throw new Fault(
          "Exception constructing DBSupport object: " + e.getMessage());
    }

    // Obtain our TSDataSources for interacting with our TS whitebox
    try {
      nctx = new TSNamingContext();
      ds1 = (TSDataSource) nctx.lookup(whitebox_tx);
      ds2 = (TSDataSource) nctx.lookup(whitebox_tx_param);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logErr("####", e);
      TestUtil.logMsg("Exception during JNDI lookup: " + e.getMessage());
    }
  }

  /*
   * @testName: testGetConnection1
   *
   * @assertion_ids: Connector:SPEC:29; Connector:SPEC:31; Connector:SPEC:269;
   *
   * 
   * @test_Strategy: Call DataSource.getConnection. Check whether the connection
   * has been correctly allocated from the ConnectionManager.
   *
   * Use the connection in some interactions with the database.
   *
   */
  public void testGetConnection1() throws Fault {

    // Get connection object using no parameters. Container managed
    // signon.
    try {
      con = ds1.getConnection();
      TestUtil.logMsg("Got connection from the DataSource.");
    } catch (Exception e) {
      TestUtil.logErr("Exception caught on creating connection.", e);
      TestUtil.printStackTrace(e);
      TestUtil.logErr("###", e);
      throw new Fault(e.getMessage());
    }

    // Verify connection object works by doing some end to end tests.
    TestUtil.logMsg("Checking for Connection Validity.");

    // Insert into table
    try {
      dbutil.insertIntoTable(con);
      TestUtil.logMsg("Values inserted into table!");
    } catch (Exception sqle) {
      TestUtil.logErr("Exception inserting into table.", sqle);
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Drop the table
    try {
      dbutil.dropTable(con);
      TestUtil.logMsg("Table has been dropped!");
    } catch (Exception sqle) {
      TestUtil.logErr("Exception dropping table.", sqle);
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

  }

  /*
   * @testName: testgetConnectionWithParameter1
   *
   * @assertion_ids: Connector:SPEC:30; Connector:SPEC:27; Connector:SPEC:18;
   * Connector:SPEC:269;
   *
   * @test_Strategy: Call the DataSource.getConnection(username,password). See
   * if ConnectionManager can create the ManagedConnection using
   * ManagedConnectionFactory. Check for the validity of the connection which is
   * returned by performing some queries to the database.
   *
   *
   */
  public void testgetConnectionWithParameter1() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds2.setLogFlag(true);
      con = ds2.getConnection(uname, password);
      ds2.setLogFlag(false);
      log = ds2.getLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Need to link these strings to assertion
    String toCheck1 = "LocalTxMCF";
    String toCheck2 = "TSManagedConnection";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
      if (str.startsWith(toCheck2)) {
        b2 = true;
      }
    }

    if (b1 && b2) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("Failed: couldn't find...");
    }

    // Verify connection object works by doing some end to end tests.
    TestUtil.logMsg("Performing end to end verification...");

    // Insert into table
    try {
      dbutil.insertIntoTable(con);
      TestUtil.logMsg("Values inserted into table!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception inserting into table.");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Drop the table
    try {
      dbutil.dropTable(con);
      TestUtil.logMsg("Table has been dropped!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception dropping table.");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

  }

  /* cleanup */
  public void cleanup() throws Fault {
    TestUtil.logMsg("Cleanup");
    try {
      con.close();
    } catch (Exception sqle) {
      TestUtil.printStackTrace(sqle);
      TestUtil.logErr("Exception on cleanup: " + sqle.getMessage());
    }
  }
}
