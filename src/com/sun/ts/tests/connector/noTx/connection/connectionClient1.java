/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)connectionClient1.java	1.21 03/05/16
 */

package com.sun.ts.tests.connector.noTx.connection;

import java.io.Serializable;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;
import com.sun.ts.tests.common.connector.whitebox.TSDataSource;
import com.sun.ts.tests.connector.util.DBSupport;

public class connectionClient1 extends ServiceEETest implements Serializable {

  private TSNamingContext ncnoTx = null;

  private TSConnection con = null;

  private String whitebox_notx = null;

  private String whitebox_notx_param = null;

  private String uname = null;

  private String password = null;

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
   * @class.setup_props: whitebox-notx, JNDI name of TS WhiteBox;
   * whitebox-notx-param, conn w/ params; rauser1, user name; rapassword1,
   * password for rauser1;
   *
   * @class.testArgs: -ap tssql.stmt
   *
   */
  public void setup(String[] args, Properties p) throws Fault {

    // Get JNDI lookups for both adapters. The harness will throw if these
    // properties can not be retrieved, so there is no need for error checking
    // here.
    whitebox_notx = p.getProperty("whitebox-notx");
    whitebox_notx_param = p.getProperty("whitebox-notx-param");

    uname = p.getProperty("rauser1");
    password = p.getProperty("rapassword1");

    logMsg("Using: " + whitebox_notx);
    logMsg("Using: " + whitebox_notx_param);

    // Construct our DBSupport object. This object performs interactions
    // on a table, based on the properties object supplied.
    try {
      dbutil = new DBSupport();
    } catch (Exception e) {
      throw new Fault(
          "Exception constructing DBSupport object: " + e.getMessage(), e);
    }

    try {
      ncnoTx = new TSNamingContext();

      ds1 = (TSDataSource) ncnoTx.lookup(whitebox_notx);
      ds2 = (TSDataSource) ncnoTx.lookup(whitebox_notx_param);

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
   * @test_Strategy: Call TSDataSource.getConnection. Check whether the
   * connection has been correctly allocated from the ConnectionManager.
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
      TestUtil.logMsg("Got connection from the TSDataSource.");
    } catch (Exception e) {
      TestUtil.logMsg("Exception caught on creating connection.");
      throw new Fault(e.getMessage(), e);
    }

    // Verify connection object works by doing some end to end tests.

    TestUtil.logMsg("Checking for Connection Validity.");

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

  }

  /*
   * @testName: testgetConnectionWithParameter1
   *
   * @assertion_ids: Connector:SPEC:30; Connector:SPEC:27; Connector:SPEC:18;
   *
   * @test_Strategy: Call the TSDataSource.getConnection(username,password). See
   * if ConnectionManager can create the ManagedConnection using
   * ManagedConnectionFactory. Check for the validity of the connection which is
   * returned by performing some queries to the database.
   *
   *
   */
  public void testgetConnectionWithParameter1() throws Fault {

    try {
      logMsg("Using: " + whitebox_notx_param);
      con = ds2.getConnection(uname, password);
      TestUtil.logMsg("Got connection object from the TSDataSource.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection.");
      throw new Fault(sqle.getMessage(), sqle);
    }
    // Verify connection object works by doing some end to end tests.

    TestUtil.logMsg("Checking for Connection Validity.");

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

  }

  /* cleanup */
  public void cleanup() throws Fault {
    TestUtil.logMsg("Cleanup");
    try {
      TestUtil.logTrace("Closing connection in cleanup.");
      con.close();
    } catch (Exception sqle) {
      TestUtil.logErr("Exception on cleanup: " + sqle.getMessage(), sqle);
    }
  }
}
