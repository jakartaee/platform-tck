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
 * @(#)transinflowClient1.java	1.3  03/05/16
 */

package com.sun.ts.tests.connector.localTx.transinflow;

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

public class transinflowClient1 extends ServiceEETest implements Serializable {

  private TSNamingContext nctx = null;

  private TSConnection con = null;

  private String whitebox_tx = null;

  private String whitebox_mdcomplete = null;

  private TSDataSource ds1 = null;

  private TSDataSource ds2 = null;

  private DBSupport dbutil = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    transinflowClient1 theTests = new transinflowClient1();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: whitebox-tx; whitebox-mdcomplete;
   *
   * @class.testArgs: -ap tssql.stmt
   *
   */
  public void setup(String[] args, Properties p) throws Fault {

    // Get JNDI lookups for both adapters. The harness will throw if these
    // properties can not be retrieved, so there is no need for error checking
    // here.
    whitebox_tx = p.getProperty("whitebox-tx");
    whitebox_mdcomplete = p.getProperty("whitebox-mdcomplete");

    logMsg("Using: " + whitebox_tx);
    logMsg("Using: " + whitebox_mdcomplete);

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
      nctx = new TSNamingContext();
      ds2 = (TSDataSource) nctx.lookup(whitebox_mdcomplete);
      ds1 = (TSDataSource) nctx.lookup(whitebox_tx);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Exception during JNDI lookup: " + e.getMessage());
    }
  }

  /*
   * @testName: testGetTransactionSupport
   *
   * @assertion_ids: Connector:SPEC:316; Connector:SPEC:206;
   *
   * @test_Strategy: This is testing that the ManagedConnectionFactorys
   * implementation of the TransactionSupport interfaces getTransactionSupport()
   * method is called. Furthermore, this tests that the LocalTransaction value,
   * which is hardcoded and returned from the getTransactionSupport() call, will
   * override the LocalTransaction setting in the ra.xml file. We will test that
   * NoTransaction support works and can assume our explicit returning of
   * NoTransaction in the getTransactionSupport() method took precedence over
   * anything else in ra.xml, if local transactions are supported.
   */
  public void testGetTransactionSupport() throws Fault {
    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("establishing connection to rar...");
    try {
      TestUtil.logMsg("Getting RA log.");
      ds2.setLogFlag(true);
      con = ds2.getConnection();
      log = ds2.getStateLog();
      TestUtil.logTrace("Got RA log and connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // verify this string is in connectors internal log
    String toCheck1 = "MDCompleteMCF.getTransactionSupport called";

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
      throw new Fault("testGetTransactionSupport is incorrectly implemented");
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
   * @testName: testSetResourceAdapterMethod
   *
   * @assertion_ids: Connector:SPEC:226; Connector:SPEC:5; Connector:SPEC:6;
   * Connector:SPEC:227;
   *
   * @test_Strategy: This is testing that a successful association was
   * established when the setResourceAdapter method was called on the
   * administered object and returned without throwing an exception. (Doing this
   * verifieds assertions 5, 6, 226.) Additionally, this checks to make sure the
   * setResourceAdapterMethod was not called twice which verifies assertion 227.
   * 
   */
  public void testSetResourceAdapterMethod() throws Fault {
    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("establishing connection to rar...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds2.setLogFlag(true);
      con = ds2.getConnection();
      log = ds2.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // verify this string is in connectors internal log
    // which verifies asertions 226, 5, 6
    String toCheck1 = "MDCompleteMCF setResourceAdapter 1";
    String toCheck2 = "MDCompleteMCF setResourceAdapter 2";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        // should find this if all is good
        b1 = true;
      }
      if (str.startsWith(toCheck2)) {
        // should NOT find this if all is good
        b2 = true;
      }
    }

    if (b1 & !b2) {
      TestUtil.logMsg("testSetResourceAdapterMethod called correctly");
    } else {
      throw new Fault(
          "testSetResourceAdapterMethod found errors with invocation of setResourceAdapter method.");
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
   * @testName: testXATerminator
   *
   * @assertion_ids: Connector:SPEC:120; Connector:JAVADOC:132;
   * Connector:JAVADOC:304; Connector:JAVADOC:340; Connector:JAVADOC:398;
   *
   * @test_Strategy: submit a work obect associated with a unique Xid. Call the
   * XATermintor object and do a commit on that particular Xid.
   *
   */
  public void testXATerminator() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("establishing connection to rar...");
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
    String toCheck1 = "WorkXid Submitted";
    String toCheck2 = "XATerminator is not null";
    String toCheck3 = "Xid Committed";

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
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("TransactionInflow is incorrectly implemented");
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
   * @testName: testTransactionSynchronizationRegistry
   *
   * @assertion_ids: Connector:SPEC:291; Connector:JAVADOC:130;
   *
   * @test_Strategy: verify bootstrap context exposes appservers
   * TransactionSynchronizationRegistry
   *
   */
  public void testTransactionSynchronizationRegistry() throws Fault {

    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("establishing connection to rar...");
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
    String toCheck1 = "getTransactionSynchronizationRegistry supported by Server";

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
      throw new Fault("TransactionInflow is incorrectly implemented");
    }

  }

  /*
   * @testName: testConcurrentWorkXid
   *
   * @assertion_ids: Connector:SPEC:125; Connector:JAVADOC:399;
   *
   * @test_Strategy: submit a work obect associated with a unique Xid. In the
   * run method of that work object submit another work object with the same Xid
   * which was used earlier. Check to see if the
   * WorkException.TX_CONCURRENT_WORK_DISALLOWED is thrown.
   *
   */
  public void testConcurrentWorkXid() throws Fault {

    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("establishing connection to rar...");
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
    String toCheck1 = "WorkException.TX_CONCURRENT_WORK_DISALLOWED caught";

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
      throw new Fault("WorkException.TX_CONCURRENT_WORK_DISALLOWED not caught");
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

  /* cleanup */
  public void cleanup() throws Fault {
    TestUtil.logMsg("Cleanup");

    if (ds1 != null) {
      ds1.clearLog();
    }

    if (ds2 != null) {
      ds2.clearLog();
    }

    try {
      if (con != null) {
        con.close();
      }
    } catch (Exception sqle) {
      TestUtil.logErr("Exception on cleanup: " + sqle.getMessage(), sqle);
    }
  }

}
