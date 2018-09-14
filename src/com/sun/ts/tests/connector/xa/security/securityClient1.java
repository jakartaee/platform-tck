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
 * @(#)securityClient1.java	1.22 03/05/16
 */

package com.sun.ts.tests.connector.xa.security;

import java.io.*;
import java.util.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.connector.util.DBSupport;
import com.sun.ts.tests.common.connector.whitebox.TSDataSource;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;

public class securityClient1 extends ServiceEETest implements Serializable {

  // Need to find out if this testName is needed

  private TSNamingContext ncxa = null;

  private TSConnection con = null;

  private TSConnection con1 = null;

  private String whitebox_xa = null;

  private String whitebox_xa_param = null;

  private String uname = null;

  private String password = null;

  private TSDataSource ds1 = null;

  private TSDataSource ds2 = null;

  private DBSupport dbutil = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    securityClient1 theTests = new securityClient1();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: whitebox-xa, JNDI name of TS WhiteBox;
   * whitebox-xa-param, conn w/ params; rauser1, user name; rapassword1,
   * password for user name;
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
      ncxa = new TSNamingContext();

      ds1 = (TSDataSource) ncxa.lookup(whitebox_xa);
      ds2 = (TSDataSource) ncxa.lookup(whitebox_xa_param);

      TestUtil.logMsg("ds1 JNDI lookup: " + ds1);
      TestUtil.logMsg("ds2 JNDI lookup: " + ds2);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Exception during JNDI lookup: " + e.getMessage());
    }

  }

  /*
   * @testName: testContainerManaged
   *
   * @assertion_ids: Connector:SPEC:63; Connector:SPEC:62;
   * Connector:JAVADOC:238; Connector:JAVADOC:333; Connector:JAVADOC:334;
   * Connector:JAVADOC:335; Connector:JAVADOC:337; Connector:JAVADOC:338;
   *
   * @test_Strategy: Call the TSDataSource.getConnection. Providing there are no
   * unused existing getConnection will call createManagedConnection and
   * PasswordCredential will be returned. If the PasswordCredential is not null
   * the test passes.
   */
  public void testContainerManaged() throws Fault {

    // Get connection object using no parameters. Container managed
    // signon.
    try {
      con = ds1.getConnection();
      TestUtil.logMsg("Got connection from the DataSource.");
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
   * @testName: testComponentManaged
   *
   * @assertion_ids: Connector:SPEC:63; Connector:JAVADOC:333;
   * Connector:JAVADOC:334; Connector:JAVADOC:335; Connector:JAVADOC:337;
   * Connector:JAVADOC:338;
   * 
   * @test_Strategy: Call the DataSource.getConnection(uname, password).
   * Providing there are no unused existing getConnection will call
   * createManagedConnection and PasswordCredential will be returned. If the
   * PasswordCredential is not null the test passes.
   */
  public void testComponentManaged() throws Fault {
    try {
      logMsg("Using: " + whitebox_xa_param);
      con = ds2.getConnection(uname, password);
      TestUtil.logMsg("Got connection object from the DataSource.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection.");
      throw new Fault(sqle.getMessage(), sqle);
    }
    // Verify connection object works by doing some end to end tests.
    // This is a temporary work around for bug id 4432340.
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
   * @testName: testAppEISSignon
   *
   * @assertion_ids: Connector:SPEC:65;
   * 
   * @test_Strategy: Call the TSDataSource.getConnection method. Check whether
   * LocalTxManagedConnectionFactory.createManagedConnection was called. Check
   * if the connection is valid or not by performing some transactions.
   *
   */
  public void testAppEISSignon() throws Fault {
    try {
      logMsg("Using: " + whitebox_xa_param);
      con = ds2.getConnection(uname, password);
      TestUtil.logMsg("Got connection object from the DataSource.");
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

  /*
   * @testName: testConnManagerAllocateConnection
   *
   * @assertion_ids: Connector:SPEC:64;
   *
   *
   * @test_Strategy: Call the TSDataSource.getConnection(). TSDataSource
   * instance calls connectionManger.allocationConnection internally to get the
   * the connection. If the connection is returned then check the validity of
   * the connection.
   */
  public void testConnManagerAllocateConnection() throws Fault {

    // Get connection object using no parameters. Container managed
    // signon.
    try {
      con = ds1.getConnection();
      TestUtil.logMsg("Got connection from the DataSource.");
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
