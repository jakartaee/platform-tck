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
 * @(#)securityClient1.java	1.22 03/05/16
 */

package com.sun.ts.tests.connector.localTx.security;

import java.io.Serializable;
import java.util.Properties;
import java.util.Vector;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;
import com.sun.ts.tests.common.connector.whitebox.TSDataSource;
import com.sun.ts.tests.connector.util.DBSupport;

public class securityClient1 extends ServiceEETest implements Serializable {

  // Need to find out if this testName is needed

  private TSNamingContext nctx = null;

  private TSConnection con = null;

  private TSConnection con2 = null;

  private String whitebox_tx = null;

  private String whitebox_tx_param = null;

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
   * @class.setup_props: whitebox-tx, JNDI name of TS WhiteBox;
   * whitebox-tx-param, conn w/ params; rauser1, user name; rapassword1,
   * password for user name;
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

    uname = p.getProperty("rauser1");
    password = p.getProperty("rapassword1");

    logMsg("Using: " + whitebox_tx);
    logMsg("Using: " + whitebox_tx_param);

    // Construct our DBSupport object. This object performs interactions
    // on a table, based on the properties object supplied.
    try {
      dbutil = new DBSupport();
    } catch (Exception e) {
      throw new Fault(
          "Exception constructing DBSupport object: " + e.getMessage(), e);
    }

    try {
      nctx = new TSNamingContext();

      ds1 = (TSDataSource) nctx.lookup(whitebox_tx);
      ds2 = (TSDataSource) nctx.lookup(whitebox_tx_param);

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
   * Connector:JAVADOC:244;
   *
   * @test_Strategy: Call the TSDataSource.getConnection. Providing there are no
   * unused existing getConnection will call createManagedConnection and
   * PasswordCredential will be returned. If the PasswordCredential is not null
   * the test passes. This is option A.
   */
  public void testContainerManaged() throws Fault {

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
   * @testName: testComponentManaged
   *
   * @assertion_ids: Connector:SPEC:63; Connector:JAVADOC:238;
   * Connector:JAVADOC:333; Connector:JAVADOC:334; Connector:JAVADOC:335;
   * Connector:JAVADOC:337; Connector:JAVADOC:338;
   * 
   * @test_Strategy: Call the TSDataSource.getConnection(uname, password).
   * Providing there are no unused existing getConnection will call
   * createManagedConnection and PasswordCredential will be returned. If the
   * PasswordCredential is not null the test passes. This is option C defined.
   */
  public void testComponentManaged() throws Fault {
    try {
      logMsg("Using: " + whitebox_tx_param);
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

  /*
   * @testName: testAppEISSignon
   *
   * @assertion_ids: Connector:SPEC:65; Connector:JAVADOC:239;
   * 
   * @test_Strategy: Call the TSDataSource.getConnection method. Check whether
   * LocalTxManagedConnectionFactory.createManagedConnection was called. Check
   * if the connection is valid or not by performing some transactions.
   *
   */
  public void testAppEISSignon() throws Fault {
    try {
      logMsg("Using: " + whitebox_tx_param);
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

  /*
   * @testName: testCBTestCPCandNullPrin
   *
   * @assertion_ids: Connector:SPEC:234;
   *
   * @test_Strategy: This validates the handling of a CPC - where no principals
   * will be added to the CPC execSubject and the CPC has a null principal.
   */
  public void testCBTestCPCandNullPrin() throws Fault {

    Vector log1 = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");

    try {
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log1 = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to verify the follow strings exists within the log...
    String toCheck1 = "cbTestCPCandNullPrin:  Case-1 security callbacks handled.";
    TestUtil.logTrace("Searching for string:  " + toCheck1);

    TestUtil.logTrace(log1.toString()); // dump log to console
    for (int i = 0; i < log1.size(); i++) {
      String str = (String) log1.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("testCBTestCPCandNullPrin() passed");
    } else {
      TestUtil.logMsg("couldnt find string in log1 = whitebox-tx");
      throw new Fault("testCBTestCPCandNullPrin() failed.");
    }
  }

  /*
   * @testName: testCBTestCPCandGPC
   *
   * @assertion_ids: Connector:SPEC:235; Connector:SPEC:238;
   *
   * @test_Strategy: Test case where GPC is followed by CPC - no principals are
   * added to the subject, the CPC (with a non-null principal), and one or more
   * GPC's (each non-null group) are handled.
   */
  public void testCBTestCPCandGPC() throws Fault {

    Vector log2 = null;
    boolean b2 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");

    try {
      ds2.setLogFlag(true);
      con2 = ds1.getConnection();
      log2 = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to verify the follow strings exists within the log...
    String toCheck1 = "cbTestCPCandGPC:  callbacks handled.";
    TestUtil.logTrace("Searching for string:  " + toCheck1);

    TestUtil.logTrace(log2.toString()); // dump log to console
    for (int i = 0; i < log2.size(); i++) {
      String str = (String) log2.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b2 = true;
      }
    }

    if (b2) {
      TestUtil.logMsg("testCBTestCPCandGPC() passed");
    } else {
      TestUtil.logMsg("couldnt find string in log2 = whitebox-tx-param");
      throw new Fault("testCBTestCPCandGPC() failed.");
    }
  }

  /*
   * @testName: testCBTestAllCallbacksAndPrin
   *
   * @assertion_ids: Connector:SPEC:229;
   *
   * @test_Strategy: Test handling of a CPC - where no principals will be added
   * to the CPC execSubject and the CPC has a null principal.
   */
  public void testCBTestAllCallbacksAndPrin() throws Fault {
    Vector log1 = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");

    try {
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log1 = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to verify the follow strings exists within the log...
    String toCheck1 = "cbTestAllCallbacksAndPrin:  callbacks handled.";
    TestUtil.logTrace("Searching for string:  " + toCheck1);

    TestUtil.logTrace(log1.toString()); // dump log to console
    for (int i = 0; i < log1.size(); i++) {
      String str = (String) log1.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("testCBTestAllCallbacksAndPrin() passed");
    } else {
      TestUtil.logMsg("couldnt find string in log1 = whitebox-tx");
      throw new Fault("testCBTestAllCallbacksAndPrin() failed.");
    }

  }

  /*
   * @testName: testCBTestCPCandPrin
   *
   * @assertion_ids: Connector:SPEC:234; Connector:SPEC:239;
   *
   * @test_Strategy: Test handling of a CPC - where no principals will be added
   * to the CPC subject and the CPC has a null principal.
   * 
   */
  public void testCBTestCPCandPrin() throws Fault {
    Vector log1 = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");

    try {
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log1 = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to verify the follow strings exists within the log...
    String toCheck1 = "cbTestCPCandPrin:  callbacks handled.";
    TestUtil.logTrace("Searching for string:  " + toCheck1);

    TestUtil.logTrace(log1.toString()); // dump log to console
    for (int i = 0; i < log1.size(); i++) {
      String str = (String) log1.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("testCBTestCPCandPrin() passed");
    } else {
      TestUtil.logMsg("couldnt find string in log1 for rar = whitebox-tx");
      throw new Fault("testCBTestCPCandPrin() failed.");
    }
  }

  /*
   * @testName: testCBTestAllCallbacksNullPrin
   *
   * @assertion_ids: Connector:SPEC:229;
   *
   * @test_Strategy: Test handling of a CPC - where no principals will be added
   * to the CPC subject and the CPC has a null principal.
   * 
   */
  public void testCBTestAllCallbacksNullPrin() throws Fault {
    Vector log1 = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");

    try {
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log1 = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to verify the follow strings exists within the log...
    String toCheck1 = "cbTestAllCallbacksNullPrin:  callbacks handled.";
    TestUtil.logTrace("Searching for string:  " + toCheck1);

    TestUtil.logTrace(log1.toString()); // dump log to console
    for (int i = 0; i < log1.size(); i++) {
      String str = (String) log1.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("testCBTestAllCallbacksNullPrin() passed");
    } else {
      TestUtil.logMsg("couldnt find string in log1 for rar = whitebox-tx");
      throw new Fault("testCBTestAllCallbacksNullPrin() failed.");
    }
  }

  /*
   * @testName: testCBTestCPCandPVC
   *
   * @assertion_ids: Connector:SPEC:234;
   *
   * @test_Strategy: Test case where PVC is followed by CPC - no principals are
   * added to the subject.
   * 
   */
  public void testCBTestCPCandPVC() throws Fault {
    Vector log1 = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");

    try {
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log1 = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to verify the follow strings exists within the log...
    String toCheck1 = "cbTestCPCandPVC:  callbacks handled.";
    TestUtil.logTrace("Searching for string:  " + toCheck1);

    TestUtil.logTrace(log1.toString()); // dump log to console
    for (int i = 0; i < log1.size(); i++) {
      String str = (String) log1.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("testCBTestCPCandPVC() passed");
    } else {
      TestUtil.logMsg("couldnt find string in log1 for rar = whitebox-tx");
      throw new Fault("testCBTestCPCandPVC() failed.");
    }
  }

  /*
   * @testName: testCBTestGPCandCPCFail
   *
   * @assertion_ids: Connector:SPEC:235;
   *
   * @test_Strategy: Test that a single (presumably non-group) principal was
   * added to the execSubject and the CPC is NOT handled.
   * 
   */
  public void testCBTestGPCandCPCFail() throws Fault {
    Vector log2 = null;
    boolean b2 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");

    try {
      ds2.setLogFlag(true);
      con = ds1.getConnection();
      log2 = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to verify the follow strings exists within the log...
    String toCheck1 = "cbTestGPCandCPCFail:  callbacks are NOT handled - as expected.";
    TestUtil.logTrace("Searching for string:  " + toCheck1);

    TestUtil.logTrace(log2.toString()); // dump log to console
    for (int i = 0; i < log2.size(); i++) {
      String str = (String) log2.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b2 = true;
      }
    }

    if (b2) {
      TestUtil.logMsg("testCBTestGPCandCPCFail() passed");
    } else {
      TestUtil
          .logMsg("couldnt find string in log1 for rar = whitebox-tx-param");
      throw new Fault("testCBTestGPCandCPCFail() failed.");
    }
  }

  /*
   * @testName: testCBTestEISCPCandPrin
   *
   * @assertion_ids: Connector:SPEC:238; Connector:SPEC:239;
   *
   * @test_Strategy: Test handling of a CPC - where no principals will be added
   * to the CPC subject and the CPC has a null principal. This is testing the
   * CPC being handled in a case-2 scenario with security-mapping on such that
   * the EIS creds need to be used and mapped.
   */
  public void testCBTestEISCPCandPrin() throws Fault {
    Vector log2 = null;
    boolean b2 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");

    try {
      ds2.setLogFlag(true);
      con = ds1.getConnection();
      log2 = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to verify the follow strings exists within the log...
    String toCheck1 = "cbEISTestCPCandPrin:  callbacks handled.";
    TestUtil.logTrace("Searching for string:  " + toCheck1);

    TestUtil.logTrace(log2.toString()); // dump log to console
    for (int i = 0; i < log2.size(); i++) {
      String str = (String) log2.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b2 = true;
      }
    }

    if (b2) {
      TestUtil.logMsg("testCBTestEISCPCandPrin() passed");
    } else {
      TestUtil
          .logMsg("couldnt find string in log1 for rar = whitebox-tx-param");
      throw new Fault("testCBTestEISCPCandPrin() failed.");
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

  /* cleanup */
  public void cleanup() throws Fault {
    ds1.clearLog();
    ds2.clearLog();
    TestUtil.logMsg("Cleanup");
    try {
      TestUtil.logTrace("Closing connection in cleanup.");
      if (con != null) {
        con.close();
      }
      if (con2 != null) {
        con2.close();
      }
    } catch (Exception sqle) {
      TestUtil.logErr("Exception on cleanup: " + sqle.getMessage(), sqle);
    }
  }
}
