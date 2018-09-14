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
 * @(#)lifecycleClient1.java	1.6 03/05/16
 */

package com.sun.ts.tests.connector.noTx.lifecycle;

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

public class lifecycleClient1 extends ServiceEETest implements Serializable {

  private TSNamingContext ncnotx = null;

  private TSConnection con = null;

  private String whitebox_notx = null;

  private String whitebox_notx_param = null;

  private TSDataSource ds1 = null;

  private DBSupport dbutil = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    lifecycleClient1 theTests = new lifecycleClient1();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: whitebox-notx, JNDI name of TS WhiteBox;
   *
   * @class.testArgs: -ap tssql.stmt
   *
   */
  public void setup(String[] args, Properties p) throws Fault {

    // Get JNDI lookups for both adapters. The harness will throw if these
    // properties can not be retrieved, so there is no need for error checking
    // here.
    whitebox_notx = p.getProperty("whitebox-notx");

    logMsg("Using: " + whitebox_notx);

    // Construct our DBSupport object. This object performs interactions
    // on a table, based on the properties object supplied.
    try {
      dbutil = new DBSupport();
    } catch (Exception e) {
      throw new Fault(
          "Exception constructing DBSupport object: " + e.getMessage(), e);
    }

    // Obtain our TSDataSources for interacting with our TS whitebox
    try {
      ncnotx = new TSNamingContext();
      ds1 = (TSDataSource) ncnotx.lookup(whitebox_notx);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Exception during JNDI lookup: " + e.getMessage());
    }
  }

  /*
   * @testName: testAssociationMCFandRA
   * 
   * @assertion_ids: Connector:SPEC:5; Connector:JAVADOC:259;
   *
   * @test_Strategy: Check to see if
   * NoTxManagedConnectionFacotry.setResourceAdapter and ResourceAdatper.start
   * was called once. If both are called once then the test passes.
   * 
   * Check for the validity of the connection which is returned by performing
   * some queries to the TSeis.
   *
   *
   */
  public void testAssociationMCFandRA() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "NoTxManagedConnectionFactory setResourceAdapter 1";
    String toCheck2 = "NoTxResourceAdapter Started 1";

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
      throw new Fault(
          "ManagedConnectionFactory.setResourceAdapter or ResourceAdapter.start not called");
    }

    // Verify connection object works by doing some end to end tests.
    TestUtil.logMsg("Performing end to end verification...");

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
   * @testName: testAssociationMCFandRA2
   * 
   * @assertion_ids: Connector:SPEC:227;
   *
   * @test_Strategy: Check to see if
   * NoTxManagedConnectionFacotry.setResourceAdapter was called twice. If it
   * was, then we fail assertion Connector:SPEC:227
   *
   */
  public void testAssociationMCFandRA2() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // the following string should not appear in the appservers internal log
    // if it does then it viloates assertion 227
    String toCheck1 = "NoTxManagedConnectionFactory setResourceAdapter 2";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (!b1) {
      // this is good - it means setResourceAdapter was NOT called twice.
      TestUtil.logMsg("Methods called correctly");
    } else {
      // this is bad - it means setResourceAdapter was called twice.
      throw new Fault(
          "ManagedConnectionFactory.setResourceAdapter called twice and should not have been.");
    }

    TestUtil.logMsg("Performing end to end verification...");
  }

  /*
   * @testName: testBootstrapforNull
   *
   * @assertion_ids: Connector:SPEC:4;
   *
   * @test_Strategy: Check to see if Bootstrap was passed from the application
   * server. and check to see if it was not null.
   * 
   * Check for the validity of the connection which is returned by performing
   * some queries to the TSeis.
   *
   *
   */
  public void testBootstrapforNull() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "NoTxResourceAdapter BootstrapContext Not Null ";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("ResourceAdapter not Instantiated");
    }

    // Verify connection object works by doing some end to end tests.
    TestUtil.logMsg("Performing end to end verification...");

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
   * @testName: testInstantiationOfRA
   *
   * @assertion_ids: Connector:SPEC:3; Connector:SPEC:162;
   *
   * @test_Strategy: Check to see if NoTxManagedConnectionFacotry constructor
   * was called atleast once. If it was then ResourceAdapter is instantiated
   * atleast once per resource adapter deployment and the test passes.
   * 
   * Check for the validity of the connection which is returned by performing
   * some queries to the TSeis.
   *
   *
   */
  public void testInstantiationOfRA() throws Fault {

    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "NoTxResourceAdapterImpl Constructor";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("ResourceAdapter not Instantiated");
    }

    // Verify connection object works by doing some end to end tests.
    TestUtil.logMsg("Performing end to end verification...");

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
   * @testName: testRASharability
   *
   * @assertion_ids: Connector:SPEC:11;
   *
   * @test_Strategy: Check to see if NoTxResourceAdapter.start was called more
   * than once. If it has then it is not being shared or reused.
   * 
   * Check for the validity of the connection which is returned by performing
   * some queries to the TSeis. for end-to-end verification.
   *
   */
  public void testRASharability() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "NoTxResourceAdapter Started 2";
    String toCheck2 = "NoTxResourceAdapter Started 1";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (!str.startsWith(toCheck1)) {
        b1 = true;
      }
      if (str.startsWith(toCheck2)) {
        b2 = true;
      }
    }

    if (b1 && b2) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("Resource Adapter is Shared or reused.");
    }

    // Verify connection object works by doing some end to end tests.
    TestUtil.logMsg("Performing end to end verification...");

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
   * @testName: testMCFcalledOnce
   *
   * @assertion_ids: Connector:SPEC:6;
   *
   * @test_Strategy: Check to see if
   * NoTxManagedConnectionFactory.setResourceAdapter was called exactly once. If
   * it has then the test passes.
   * 
   * Check for the validity of the connection which is returned by performing
   * some queries to the TSeis for end-to-end verification.
   *
   */
  public void testMCFcalledOnce() throws Fault {

    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "NoTxManagedConnectionFactory setResourceAdapter 1";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault(
          "NoTxManagedConnectionFactory.setResourceAdapter not called .");
    }

    // Verify connection object works by doing some end to end tests.
    TestUtil.logMsg("Performing end to end verification...");

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
   * @testName: testRAforJavaBean
   *
   * @assertion_ids: Connector:SPEC:1;
   *
   * @test_Strategy: Check to see if NoTxResourceAdapter.setRAName is called. If
   * it has then the test passes. We are checking the the app server side
   * contract to see if the method is called.
   * 
   * Check for the validity of the connection which is returned by performing
   * some queries to the TSeis for end-to-end verification.
   *
   */
  public void testRAforJavaBean() throws Fault {

    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "NoTxResourceAdapter.setRAName";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault(
          "Application Server did not call JavaBean setter and getter methods.");
    }

    // Verify connection object works by doing some end to end tests.
    TestUtil.logMsg("Performing end to end verification...");

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
    TestUtil.logMsg("Exception dropping table.");
  }
  /* cleanup */

  public void cleanup() throws Fault {
    TestUtil.logMsg("Cleanup");
    ds1.clearLog();
    try {
      con.close();
    } catch (Exception sqle) {
      TestUtil.logErr("Exception on cleanup: " + sqle.getMessage(), sqle);
    }
  }

}
