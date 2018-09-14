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
 * @(#)connectionClient1.java	1.36 03/05/16
 */

package com.sun.ts.tests.connector.localTx.connection;

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
    connectionClient1 theTests = new connectionClient1();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: whitebox-tx, JNDI name of TS WhiteBox;
   * whitebox-tx-param, conn w/ params; rauser1, user name; rapassword1,
   * password for rauser1;
   *
   * @class.testArgs: -ap tssql.stmt
   *
   */
  public void setup(String[] args, Properties p) throws Fault {

    // Get JNDI lookups for both adapters. The harness will throw if these
    // properties can not be retrieved, so there is no need for error checking
    // here.
    whitebox_tx = p.getProperty("whitebox-tx");
    whitebox_tx_param = p.getProperty("whitebox-tx-param");

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
   * @assertion_ids: Connector:SPEC:29; Connector:SPEC:31;
   * Connector:JAVADOC:161;
   * 
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
   * Connector:JAVADOC:234;
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
      ds2.clearLog();
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
    String toCheck1 = "LocalTxManagedConnectionFactory";
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

  /*
   * @testName: testAPIResourceException
   *
   * @assertion_ids: Connector:JAVADOC:7; Connector:JAVADOC:9;
   * Connector:JAVADOC:10; Connector:JAVADOC:11; Connector:JAVADOC:12;
   * Connector:JAVADOC:13; Connector:JAVADOC:14; Connector:JAVADOC:15;
   *
   * @test_Strategy: Call the DataSource.getConnection(). verify the API
   * assertions for ResourceException;
   *
   *
   */
  public void testAPIResourceException() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    boolean b5 = false;
    boolean b6 = false;
    boolean b7 = false;
    boolean b8 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "ResourceException(null) passed";
    String toCheck2 = "ResourceException(str) passed";
    String toCheck3 = "ResourceException.setErrorCode(str) passed";
    String toCheck4 = "ResourceException(str, str) passed";
    String toCheck5 = "ResourceException(throwable) passed";
    String toCheck6 = "ResourceException(str, someThrowable) passed";
    String toCheck7 = "ResourceException.getMessage() passed";
    String toCheck8 = "ResourceException.getErrorCode() passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
      if (str.startsWith(toCheck4)) {
        b4 = true;
      }
      if (str.startsWith(toCheck5)) {
        b5 = true;
      }
      if (str.startsWith(toCheck6)) {
        b6 = true;
      }
      if (str.startsWith(toCheck7)) {
        b7 = true;
      }
      if (str.startsWith(toCheck8)) {
        b8 = true;
      }
    }

    if (b1 && b2 && b3 && b4 && b5 && b6 && b7 && b8) {
      TestUtil.logMsg("ResourceException API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      if (!b4) {
        err += "b4 failed, ";
      }
      if (!b5) {
        err += "b5 failed, ";
      }
      if (!b6) {
        err += "b6 failed, ";
      }
      if (!b7) {
        err += "b7 failed, ";
      }
      if (!b8) {
        err += "b8 failed, ";
      }
      throw new Fault("ResourceException API assertions failed: " + err);
    }

  }

  /*
   * @testName: testAPINotSupportedException
   *
   * @assertion_ids: Connector:JAVADOC:1; Connector:JAVADOC:2;
   * Connector:JAVADOC:3; Connector:JAVADOC:4; Connector:JAVADOC:5;
   *
   * @test_Strategy: Call the DataSource.getConnection(). verify the API
   * assertions for NotSupportedException;
   *
   */
  public void testAPINotSupportedException() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    boolean b5 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "NotSupportedException(null) passed";
    String toCheck2 = "NotSupportedException(str) passed";
    String toCheck3 = "NotSupportedException(str, str) passed";
    String toCheck4 = "NotSupportedException(throwable) passed";
    String toCheck5 = "NotSupportedException(str, throwable) passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
      if (str.startsWith(toCheck4)) {
        b4 = true;
      }
      if (str.startsWith(toCheck5)) {
        b5 = true;
      }

    }

    if (b1 && b2 && b3 && b4 && b5) {
      TestUtil.logMsg("NotSupportedException API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      if (!b4) {
        err += "b4 failed, ";
      }
      if (!b5) {
        err += "b5 failed, ";
      }
      throw new Fault("NotSupportedException API assertions failed: " + err);
    }

  }

  /*
   * @testName: testAPILocalTransactionException
   *
   * @assertion_ids: Connector:JAVADOC:209; Connector:JAVADOC:210;
   * Connector:JAVADOC:211; Connector:JAVADOC:212; Connector:JAVADOC:213;
   * 
   *
   * @test_Strategy: Call the DataSource.getConnection(). verify the API
   * assertions for LocalTransactionException;
   *
   */
  public void testAPILocalTransactionException() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    boolean b5 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "LocalTransactionException(null) passed";
    String toCheck2 = "LocalTransactionException(str) passed";
    String toCheck3 = "LocalTransactionException(str, str) passed";
    String toCheck4 = "LocalTransactionException(throwable) passed";
    String toCheck5 = "LocalTransactionException(str, throwable) passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
      if (str.startsWith(toCheck4)) {
        b4 = true;
      }
      if (str.startsWith(toCheck5)) {
        b5 = true;
      }

    }

    if (b1 && b2 && b3 && b4 && b5) {
      TestUtil.logMsg("LocalTransactionException API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      if (!b4) {
        err += "b4 failed, ";
      }
      if (!b5) {
        err += "b5 failed, ";
      }
      throw new Fault(
          "LocalTransactionException API assertions failed: " + err);
    }

  }

  /*
   * @testName: testAPIResourceAllocationException
   *
   * @assertion_ids: Connector:JAVADOC:272; Connector:JAVADOC:273;
   * Connector:JAVADOC:274; Connector:JAVADOC:275; Connector:JAVADOC:276;
   * 
   *
   * @test_Strategy: Call the DataSource.getConnection(). verify the API
   * assertions for ResourceAllocationException;
   *
   */
  public void testAPIResourceAllocationException() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    boolean b5 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "ResourceAllocationException(null) passed";
    String toCheck2 = "ResourceAllocationException(str) passed";
    String toCheck3 = "ResourceAllocationException(str, str) passed";
    String toCheck4 = "ResourceAllocationException(throwable) passed";
    String toCheck5 = "ResourceAllocationException(str, throwable) passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
      if (str.startsWith(toCheck4)) {
        b4 = true;
      }
      if (str.startsWith(toCheck5)) {
        b5 = true;
      }

    }

    if (b1 && b2 && b3 && b4 && b5) {
      TestUtil.logMsg("ResourceAllocationException API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      if (!b4) {
        err += "b4 failed, ";
      }
      if (!b5) {
        err += "b5 failed, ";
      }
      throw new Fault(
          "ResourceAllocationException API assertions failed: " + err);
    }

  }

  /*
   * @testName: testAPIResourceAdapterInternalException
   *
   * @assertion_ids: Connector:JAVADOC:267; Connector:JAVADOC:268;
   * Connector:JAVADOC:269; Connector:JAVADOC:270; Connector:JAVADOC:271;
   * 
   *
   * @test_Strategy: Call the DataSource.getConnection(). verify the API
   * assertions for ResourceAdapterInternalException;
   *
   */
  public void testAPIResourceAdapterInternalException() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    boolean b5 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "ResourceAdapterInternalException(null) passed";
    String toCheck2 = "ResourceAdapterInternalException(str) passed";
    String toCheck3 = "ResourceAdapterInternalException(str, str) passed";
    String toCheck4 = "ResourceAdapterInternalException(throwable) passed";
    String toCheck5 = "ResourceAdapterInternalException(str, throwable) passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
      if (str.startsWith(toCheck4)) {
        b4 = true;
      }
      if (str.startsWith(toCheck5)) {
        b5 = true;
      }

    }

    if (b1 && b2 && b3 && b4 && b5) {
      TestUtil.logMsg("ResourceAdapterInternalException API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      if (!b4) {
        err += "b4 failed, ";
      }
      if (!b5) {
        err += "b5 failed, ";
      }
      throw new Fault(
          "ResourceAdapterInternalException API assertions failed: " + err);
    }

  }

  /*
   * @testName: testAPISecurityException
   *
   * @assertion_ids: Connector:JAVADOC:282; Connector:JAVADOC:283;
   * Connector:JAVADOC:284; Connector:JAVADOC:285; Connector:JAVADOC:286;
   * 
   *
   * @test_Strategy: Call the DataSource.getConnection(). verify the API
   * assertions for SecurityException;
   *
   */
  public void testAPISecurityException() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    boolean b5 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "SecurityException(null) passed";
    String toCheck2 = "SecurityException(str) passed";
    String toCheck3 = "SecurityException(str, str) passed";
    String toCheck4 = "SecurityException(throwable) passed";
    String toCheck5 = "SecurityException(str, throwable) passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
      if (str.startsWith(toCheck4)) {
        b4 = true;
      }
      if (str.startsWith(toCheck5)) {
        b5 = true;
      }

    }

    if (b1 && b2 && b3 && b4 && b5) {
      TestUtil.logMsg("SecurityException API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      if (!b4) {
        err += "b4 failed, ";
      }
      if (!b5) {
        err += "b5 failed, ";
      }
      throw new Fault("SecurityException API assertions failed: " + err);
    }

  }

  /*
   * @testName: testAPISharingViolationException
   *
   * @assertion_ids: Connector:JAVADOC:289; Connector:JAVADOC:290;
   * Connector:JAVADOC:291; Connector:JAVADOC:292; Connector:JAVADOC:293;
   * 
   *
   * @test_Strategy: Call the DataSource.getConnection(). verify the API
   * assertions for SharingViolationException;
   *
   */
  public void testAPISharingViolationException() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    boolean b5 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "SharingViolationException(null) passed";
    String toCheck2 = "SharingViolationException(str) passed";
    String toCheck3 = "SharingViolationException(str, str) passed";
    String toCheck4 = "SharingViolationException(throwable) passed";
    String toCheck5 = "SharingViolationException(str, throwable) passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
      if (str.startsWith(toCheck4)) {
        b4 = true;
      }
      if (str.startsWith(toCheck5)) {
        b5 = true;
      }

    }

    if (b1 && b2 && b3 && b4 && b5) {
      TestUtil.logMsg("SharingViolationException API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      if (!b4) {
        err += "b4 failed, ";
      }
      if (!b5) {
        err += "b5 failed, ";
      }
      throw new Fault(
          "SharingViolationException API assertions failed: " + err);
    }

  }

  /*
   * @testName: testAPIUnavailableException
   *
   * @assertion_ids: Connector:JAVADOC:297; Connector:JAVADOC:298;
   * Connector:JAVADOC:299; Connector:JAVADOC:300; Connector:JAVADOC:301;
   * 
   *
   * @test_Strategy: Call the DataSource.getConnection(). verify the API
   * assertions for UnavailableException;
   *
   */
  public void testAPIUnavailableException() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    boolean b5 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "UnavailableException(null) passed";
    String toCheck2 = "UnavailableException(str) passed";
    String toCheck3 = "UnavailableException(str, str) passed";
    String toCheck4 = "UnavailableException(throwable) passed";
    String toCheck5 = "UnavailableException(str, throwable) passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
      if (str.startsWith(toCheck4)) {
        b4 = true;
      }
      if (str.startsWith(toCheck5)) {
        b5 = true;
      }

    }

    if (b1 && b2 && b3 && b4 && b5) {
      TestUtil.logMsg("UnavailableException API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      if (!b4) {
        err += "b4 failed, ";
      }
      if (!b5) {
        err += "b5 failed, ";
      }
      throw new Fault("UnavailableException API assertions failed: " + err);
    }

  }

  /*
   * @testName: testAPIWorkException
   *
   * @assertion_ids: Connector:JAVADOC:387; Connector:JAVADOC:388;
   * Connector:JAVADOC:389; Connector:JAVADOC:390; Connector:JAVADOC:391;
   * 
   *
   * @test_Strategy: Call the DataSource.getConnection(). verify the API
   * assertions for WorkException;
   *
   */
  public void testAPIWorkException() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    boolean b5 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "WorkException(null) passed";
    String toCheck2 = "WorkException(str) passed";
    String toCheck3 = "WorkException(str, str) passed";
    String toCheck4 = "WorkException(throwable) passed";
    String toCheck5 = "WorkException(str, throwable) passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
      if (str.startsWith(toCheck4)) {
        b4 = true;
      }
      if (str.startsWith(toCheck5)) {
        b5 = true;
      }

    }

    if (b1 && b2 && b3 && b4 && b5) {
      TestUtil.logMsg("WorkException API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      if (!b4) {
        err += "b4 failed, ";
      }
      if (!b5) {
        err += "b5 failed, ";
      }
      throw new Fault("WorkException API assertions failed: " + err);
    }

  }

  /*
   * @testName: testAPIWorkCompletedException
   *
   * @assertion_ids: Connector:JAVADOC:370; Connector:JAVADOC:371;
   * Connector:JAVADOC:372; Connector:JAVADOC:373; Connector:JAVADOC:374;
   * 
   *
   * @test_Strategy: Call the DataSource.getConnection(). verify the API
   * assertions for WorkCompletedException;
   *
   */
  public void testAPIWorkCompletedException() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    boolean b5 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "WorkCompletedException(null) passed";
    String toCheck2 = "WorkCompletedException(str) passed";
    String toCheck3 = "WorkCompletedException(str, str) passed";
    String toCheck4 = "WorkCompletedException(throwable) passed";
    String toCheck5 = "WorkCompletedException(str, throwable) passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
      if (str.startsWith(toCheck4)) {
        b4 = true;
      }
      if (str.startsWith(toCheck5)) {
        b5 = true;
      }

    }

    if (b1 && b2 && b3 && b4 && b5) {
      TestUtil.logMsg("WorkCompletedException API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      if (!b4) {
        err += "b4 failed, ";
      }
      if (!b5) {
        err += "b5 failed, ";
      }
      throw new Fault("WorkCompletedException API assertions failed: " + err);
    }

  }

  /*
   * @testName: testAPIWorkRejectedException
   *
   * @assertion_ids: Connector:JAVADOC:408; Connector:JAVADOC:409;
   * Connector:JAVADOC:410; Connector:JAVADOC:411; Connector:JAVADOC:412;
   * 
   *
   * @test_Strategy: Call the DataSource.getConnection(). verify the API
   * assertions for WorkRejectedException;
   *
   */
  public void testAPIWorkRejectedException() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    boolean b5 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "WorkRejectedException(null) passed";
    String toCheck2 = "WorkRejectedException(str) passed";
    String toCheck3 = "WorkRejectedException(str, str) passed";
    String toCheck4 = "WorkRejectedException(throwable) passed";
    String toCheck5 = "WorkRejectedException(str, throwable) passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
      if (str.startsWith(toCheck4)) {
        b4 = true;
      }
      if (str.startsWith(toCheck5)) {
        b5 = true;
      }

    }

    if (b1 && b2 && b3 && b4 && b5) {
      TestUtil.logMsg("WorkRejectedException API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      if (!b4) {
        err += "b4 failed, ";
      }
      if (!b5) {
        err += "b5 failed, ";
      }
      throw new Fault("WorkRejectedException API assertions failed: " + err);
    }

  }

  /*
   * @testName: testAPIEISSystemException
   *
   * @assertion_ids: Connector:JAVADOC:181; Connector:JAVADOC:182;
   * Connector:JAVADOC:183; Connector:JAVADOC:184; Connector:JAVADOC:185;
   * 
   *
   * @test_Strategy: Call the DataSource.getConnection(). verify the API
   * assertions for EISSystemException;
   *
   */
  public void testAPIEISSystemException() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    boolean b5 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "EISSystemException(null) passed";
    String toCheck2 = "EISSystemException(str) passed";
    String toCheck3 = "EISSystemException(str, str) passed";
    String toCheck4 = "EISSystemException(throwable) passed";
    String toCheck5 = "EISSystemException(str, throwable) passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
      if (str.startsWith(toCheck4)) {
        b4 = true;
      }
      if (str.startsWith(toCheck5)) {
        b5 = true;
      }

    }

    if (b1 && b2 && b3 && b4 && b5) {
      TestUtil.logMsg("EISSystemException API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      if (!b4) {
        err += "b4 failed, ";
      }
      if (!b5) {
        err += "b5 failed, ";
      }
      throw new Fault("EISSystemException API assertions failed: " + err);
    }

  }

  /*
   * @testName: testAPIApplicationServerInternalException
   *
   * @assertion_ids: Connector:JAVADOC:118; Connector:JAVADOC:119;
   * Connector:JAVADOC:120; Connector:JAVADOC:121; Connector:JAVADOC:122;
   * 
   *
   * @test_Strategy: Call the DataSource.getConnection(). verify the API
   * assertions for ApplicationServerInternalException;
   *
   */
  public void testAPIApplicationServerInternalException() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    boolean b5 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "ApplicationServerInternalException(null) passed";
    String toCheck2 = "ApplicationServerInternalException(str) passed";
    String toCheck3 = "ApplicationServerInternalException(str, str) passed";
    String toCheck4 = "ApplicationServerInternalException(throwable) passed";
    String toCheck5 = "ApplicationServerInternalException(str, throwable) passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
      if (str.startsWith(toCheck4)) {
        b4 = true;
      }
      if (str.startsWith(toCheck5)) {
        b5 = true;
      }

    }

    if (b1 && b2 && b3 && b4 && b5) {
      TestUtil
          .logMsg("ApplicationServerInternalException API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      if (!b4) {
        err += "b4 failed, ";
      }
      if (!b5) {
        err += "b5 failed, ";
      }
      throw new Fault(
          "ApplicationServerInternalException API assertions failed: " + err);
    }

  }

  /*
   * @testName: testAPICommException
   *
   * @assertion_ids: Connector:JAVADOC:134; Connector:JAVADOC:135;
   * Connector:JAVADOC:136; Connector:JAVADOC:137; Connector:JAVADOC:138;
   * 
   *
   * @test_Strategy: Call the DataSource.getConnection(). verify the API
   * assertions for CommException;
   *
   */
  public void testAPICommException() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    boolean b5 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "CommException(null) passed";
    String toCheck2 = "CommException(str) passed";
    String toCheck3 = "CommException(str, str) passed";
    String toCheck4 = "CommException(throwable) passed";
    String toCheck5 = "CommException(str, throwable) passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
      if (str.startsWith(toCheck4)) {
        b4 = true;
      }
      if (str.startsWith(toCheck5)) {
        b5 = true;
      }

    }

    if (b1 && b2 && b3 && b4 && b5) {
      TestUtil.logMsg("CommException API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      if (!b4) {
        err += "b4 failed, ";
      }
      if (!b5) {
        err += "b5 failed, ";
      }
      throw new Fault("CommException API assertions failed: " + err);
    }

  }

  /*
   * @testName: testAPIRetryableWorkRejectedException
   *
   * @assertion_ids: Connector:JAVADOC:352; Connector:JAVADOC:353;
   * Connector:JAVADOC:354; Connector:JAVADOC:355; Connector:JAVADOC:356;
   *
   * @test_Strategy: Call the DataSource.getConnection(). verify the API
   * assertions for RetryableWorkRejectedException;
   *
   */
  public void testAPIRetryableWorkRejectedException() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    boolean b5 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "RetryableWorkRejectedException(null) passed";
    String toCheck2 = "RetryableWorkRejectedException(str) passed";
    String toCheck3 = "RetryableWorkRejectedException(str, str) passed";
    String toCheck4 = "RetryableWorkRejectedException(throwable) passed";
    String toCheck5 = "RetryableWorkRejectedException(str, throwable) passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
      if (str.startsWith(toCheck4)) {
        b4 = true;
      }
      if (str.startsWith(toCheck5)) {
        b5 = true;
      }

    }

    if (b1 && b2 && b3 && b4 && b5) {
      TestUtil.logMsg("RetryableWorkRejectedException API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      if (!b4) {
        err += "b4 failed, ";
      }
      if (!b5) {
        err += "b5 failed, ";
      }
      throw new Fault(
          "RetryableWorkRejectedException API assertions failed: " + err);
    }

  }

  /*
   * @testName: testAPIIllegalStateException
   *
   * @assertion_ids: Connector:JAVADOC:186; Connector:JAVADOC:187;
   * Connector:JAVADOC:188; Connector:JAVADOC:189; Connector:JAVADOC:190;
   * 
   *
   * @test_Strategy: Call the DataSource.getConnection(). verify the API
   * assertions for IllegalStateException;
   *
   */
  public void testAPIIllegalStateException() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    boolean b5 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "IllegalStateException(null) passed";
    String toCheck2 = "IllegalStateException(str) passed";
    String toCheck3 = "IllegalStateException(str, str) passed";
    String toCheck4 = "IllegalStateException(throwable) passed";
    String toCheck5 = "IllegalStateException(str, throwable) passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
      if (str.startsWith(toCheck4)) {
        b4 = true;
      }
      if (str.startsWith(toCheck5)) {
        b5 = true;
      }

    }

    if (b1 && b2 && b3 && b4 && b5) {
      TestUtil.logMsg("IllegalStateException API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      if (!b4) {
        err += "b4 failed, ";
      }
      if (!b5) {
        err += "b5 failed, ";
      }
      throw new Fault("IllegalStateException API assertions failed: " + err);
    }

  }

  /*
   * @testName: testAPIRetryableUnavailableException
   *
   * @assertion_ids: Connector:JAVADOC:277; Connector:JAVADOC:278;
   * Connector:JAVADOC:279; Connector:JAVADOC:280; Connector:JAVADOC:281;
   * 
   *
   * @test_Strategy: Call the DataSource.getConnection(). verify the API
   * assertions for RetryableUnavailableException;
   *
   */
  public void testAPIRetryableUnavailableException() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    boolean b5 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "RetryableUnavailableException(null) passed";
    String toCheck2 = "RetryableUnavailableException(str) passed";
    String toCheck3 = "RetryableUnavailableException(str, str) passed";
    String toCheck4 = "RetryableUnavailableException(throwable) passed";
    String toCheck5 = "RetryableUnavailableException(str, throwable) passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
      if (str.startsWith(toCheck4)) {
        b4 = true;
      }
      if (str.startsWith(toCheck5)) {
        b5 = true;
      }

    }

    if (b1 && b2 && b3 && b4 && b5) {
      TestUtil.logMsg("RetryableUnavailableException API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      if (!b4) {
        err += "b4 failed, ";
      }
      if (!b5) {
        err += "b5 failed, ";
      }
      throw new Fault(
          "RetryableUnavailableException API assertions failed: " + err);
    }

  }

  /*
   * @testName: testAPIManagedConnectionMetaData
   *
   * @assertion_ids: Connector:JAVADOC:248; Connector:JAVADOC:250;
   * Connector:JAVADOC:252; Connector:JAVADOC:254; Connector:JAVADOC:23;
   * Connector:JAVADOC:227;
   * 
   *
   * @test_Strategy: Call the DataSource.getConnection(). verify the API
   * assertions for ManagedConnectionMetaData;
   *
   */
  public void testAPIManagedConnectionMetaData() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    boolean b5 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "ManagedConnectionMetaData.getEISProductName() passed";
    String toCheck2 = "ManagedConnectionMetaData.getEISProductVersion() passed";
    String toCheck3 = "ManagedConnectionMetaData.getMaxConnections() passed";
    String toCheck4 = "ManagedConnectionMetaData.getUserName() passed";
    String toCheck5 = "Connection.getMetaData() passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
      if (str.startsWith(toCheck4)) {
        b4 = true;
      }
      if (str.startsWith(toCheck5)) {
        b5 = true;
      }

    }

    if (b1 && b2 && b3 && b4 && b5) {
      TestUtil.logMsg("ManagedConnectionMetaData API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      if (!b4) {
        err += "b4 failed, ";
      }
      if (!b5) {
        err += "b5 failed, ";
      }
      throw new Fault("EISSystemException API assertions failed: " + err);
    }

  }

  /*
   * @testName: testAPIManagedConnection
   *
   * @assertion_ids: Connector:JAVADOC:225; Connector:JAVADOC:223;
   * Connector:JAVADOC:230;
   * 
   * 
   *
   * @test_Strategy: Call the DataSource.getConnection(). verify the API
   * assertions for ManagedConnection
   *
   */
  public void testAPIManagedConnection() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "ManagedConnection.getLogWriter() passed";
    String toCheck2 = "ManagedConnection.getMetaData() passed";
    String toCheck3 = "ManagedConnection.getLocalTransaction() passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
    }

    if (b1 && b2 && b3) {
      TestUtil.logMsg("ManagedConnectionMetaData API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      throw new Fault("EISSystemException API assertions failed: " + err);
    }

  }

  /*
   * @testName: testAPIInvalidPropertyException
   *
   * @assertion_ids: Connector:JAVADOC:192; Connector:JAVADOC:193;
   * Connector:JAVADOC:194; Connector:JAVADOC:195; Connector:JAVADOC:196;
   * Connector:JAVADOC:197; Connector:JAVADOC:191;
   *
   * @test_Strategy: Call the DataSource.getConnection(). verify the API
   * assertions for InvalidPropertyException;
   *
   */
  public void testAPIInvalidPropertyException() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;
    boolean b5 = false;
    boolean b6 = false;
    boolean b7 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "InvalidPropertyException(null) passed";
    String toCheck2 = "InvalidPropertyException(str) passed";
    String toCheck3 = "InvalidPropertyException(str, str) passed";
    String toCheck4 = "InvalidPropertyException(throwable) passed";
    String toCheck5 = "InvalidPropertyException(str, throwable) passed";
    String toCheck6 = "InvalidPropertyException.setInvalidPropertyDescriptors() passed";
    String toCheck7 = "InvalidPropertyException.getInvalidPropertyDescriptors() passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
      if (str.startsWith(toCheck4)) {
        b4 = true;
      }
      if (str.startsWith(toCheck5)) {
        b5 = true;
      }
      if (str.startsWith(toCheck6)) {
        b6 = true;
      }
      if (str.startsWith(toCheck7)) {
        b7 = true;
      }
    }

    if (b1 && b2 && b3 && b4 && b5 && b6 && b7) {
      TestUtil.logMsg("InvalidPropertyException API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      if (!b4) {
        err += "b4 failed, ";
      }
      if (!b5) {
        err += "b5 failed, ";
      }
      if (!b6) {
        err += "b6 failed, ";
      }
      if (!b7) {
        err += "b7 failed, ";
      }
      throw new Fault("InvalidPropertyException API assertions failed: " + err);
    }

  }

  /*
   * @testName: testAPIHintsContext
   *
   * @assertion_ids: Connector:JAVADOC:345; Connector:JAVADOC:346;
   * Connector:JAVADOC:347; Connector:JAVADOC:348; Connector:JAVADOC:349;
   * Connector:JAVADOC:350; Connector:JAVADOC:351;
   *
   * @test_Strategy: verify the API assertions for HintsContext;
   *
   */
  public void testAPIHintsContext() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      con = ds1.getConnection();
      TestUtil.logTrace("Got connection.");
      log = ds1.getStateLog();
      TestUtil.logMsg("Got RA log.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      TestUtil.printStackTrace(sqle);
      throw new Fault(sqle.getMessage());
    }

    // Strings to verify
    String toCheck1 = "HintsContext() passed";
    String toCheck2 = "HintsContext.setName() and HintsContext.getName() passed";
    String toCheck3 = "HintsContext.setDescription() and HintsContext.getDescription() passed";
    String toCheck4 = "HintsContext.setHints() and HintsContext.getHints() passed";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
      if (str.startsWith(toCheck4)) {
        b4 = true;
      }
    }

    if (b1 && b2 && b3 && b4) {
      TestUtil.logMsg("InvalidPropertyException API assertions passed");
    } else {
      String err = "";
      if (!b1) {
        err = "b1 failed, ";
      }
      if (!b2) {
        err += "b2 failed, ";
      }
      if (!b3) {
        err += "b3 failed, ";
      }
      if (!b4) {
        err += "b4 failed, ";
      }
      throw new Fault("InvalidPropertyException API assertions failed: " + err);
    }

  }

  /* cleanup */
  public void cleanup() throws Fault {
    TestUtil.logMsg("Cleanup");
    // ds1.clearLog();
    // ds2.clearLog();
    try {
      con.close();
    } catch (Exception sqle) {
      TestUtil.printStackTrace(sqle);
      TestUtil.logErr("Exception on cleanup: " + sqle.getMessage());
    }
  }
}
