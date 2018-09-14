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
 * @(#)connectionClient1.java	1.26 03/05/16
 */

package com.sun.ts.tests.connector.xa.connection;

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

public class connectionClient1 extends ServiceEETest implements Serializable {

  private TSNamingContext nc = null;

  private TSConnection con = null;

  private String uname = null;

  private String password = null;

  private String whitebox_xa = null;

  private String whitebox_xa_param = null;

  private TSDataSource ds1 = null;

  private TSDataSource ds2 = null;

  private DBSupport dbutil = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    connectionClient1 theTests = new connectionClient1();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: whitebox-xa, JNDI name of TS WhiteBox;
   * whitebox-xa-param, conn w/ params; rauser1, user name; rapassword1,
   * password for rauser1;
   * 
   * @class.testArgs: -ap tssql.stmt
   *
   */
  public void setup(String[] args, Properties p) throws Fault {

    // Get JNDI lookups for both adapters. The harness will throw if these
    // properties can not be retrieved, so there is no need for error checking
    // here.
    whitebox_xa = p.getProperty("whitebox-xa");
    whitebox_xa_param = p.getProperty("whitebox-xa-param");

    uname = p.getProperty("rauser1");
    password = p.getProperty("rapassword1");

    logMsg("Using: " + uname);
    logMsg("Using: " + password);

    logMsg("Using: " + whitebox_xa);
    logMsg("Using: " + whitebox_xa_param);

    // Construct our DBSupport object. This object performs interactions
    // on a table, based on the properties object supplied.
    try {
      dbutil = new DBSupport();
    } catch (Exception e) {
      throw new Fault(
          "Exception constructing DBSupport object: " + e.getMessage(), e);
    }

    try {
      nc = new TSNamingContext();

      ds1 = (TSDataSource) nc.lookup(whitebox_xa);
      ds2 = (TSDataSource) nc.lookup(whitebox_xa_param);

      TestUtil.logMsg("ds1 JNDI lookup: " + ds1);
      TestUtil.logMsg("ds2 JNDI lookup: " + ds2);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Exception during JNDI lookup: " + e.getMessage());
    }
  }

  /*
   * @testName: testGetConnection1
   *
   * @assertion_ids: Connector:SPEC:29; Connector:SPEC:31;
   *
   *
   * @test_Strategy: Call DataSource.getConnection. Check whether the connection
   * has been correctly allocated from the ConnectionManager.
   *
   * Check for the validity of the connection which is returned by performing
   * some queries to the database.
   *
   */
  public void testGetConnection1() throws Fault {

    // Get connection object using no parameters. Container managed
    // signon.
    try {
      con = ds1.getConnection();
      TestUtil.logMsg("Got Connection Object from the DataSource.");
      TestUtil.logMsg("Checking for Connection Validity.");
    } catch (Exception e) {
      TestUtil.logMsg("Exception caught on creating connection.");
      TestUtil.printStackTrace(e);
      throw new Fault(e.getMessage());
    }

    // Insert into table
    try {
      dbutil.insertIntoTable(con);
      TestUtil.logMsg("Values inserted into table!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception inserting into table.");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Drop the table
    try {
      dbutil.dropTable(con);
      TestUtil.logMsg("Table has been dropped!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception dropping table.");
      throw new Fault(sqle.getMessage(), sqle);
    }

    TestUtil.logMsg("Connection Object is Valid.");

  }

  /*
   * @testName: testgetConnectionWithParameter1
   *
   * @assertion_ids: Connector:SPEC:30; Connector:SPEC:27; Connector:SPEC:18;
   *
   * @test_Strategy: Call the DataSource.getConnection(username,password). See
   * if ConnectionManager can create the ManagedConnection using
   * ManagedConnectionFactory. Check for the validity of the connection which is
   * returned by performing some queries to the database.
   *
   *
   */

  public void testgetConnectionWithParameter1() throws Fault {

    try {
      logMsg("Using: " + whitebox_xa);
      logMsg("Using: " + whitebox_xa_param);
      con = ds2.getConnection(uname, password);

      logMsg("Using: " + uname);
      logMsg("Using: " + password);

      TestUtil.logMsg("Got connection object from the DataSource.");
      TestUtil.logMsg("Checking for Connection Validity.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection.");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Insert into table
    try {
      dbutil.insertIntoTable(con);
      TestUtil.logMsg("Values inserted into table!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception inserting into table.");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Drop the table
    try {
      dbutil.dropTable(con);
      TestUtil.logMsg("Table has been dropped!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception dropping table.");
      throw new Fault(sqle.getMessage(), sqle);
    }

    TestUtil.logMsg("Connection Object is Valid.");

  }

  /* cleanup */
  public void cleanup() throws Fault {
    ds1.clearLog();
    ds2.clearLog();
    try {
      con.close();
    } catch (Exception e) {
      logErr("Error closing connection during cleanup: " + e.getMessage(), e);
    }
  }
}
