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
 * @(#)workmgtClient1.java	1.3  03/05/16
 */

package com.sun.ts.tests.connector.noTx.workmgt;

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

public class workmgtClient1 extends ServiceEETest implements Serializable {

  private TSNamingContext ncnotx = null;

  private TSConnection con = null;

  private String whitebox_notx = null;

  private TSDataSource ds1 = null;

  private DBSupport dbutil = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    workmgtClient1 theTests = new workmgtClient1();
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
   * @testName: testWorkManagerImplementaion
   *
   * @assertion_ids: Connector:SPEC:70; Connector:JAVADOC:131;
   * Connector:JAVADOC:396; Connector:JAVADOC:404;
   * 
   * @test_Strategy: Check to see if a non null WorkManager was acquired from
   * BootStrapContext and whether work object can be submitted to the acquired
   * workmanager object.
   * 
   * Check for the validity of the connection which is returned by performing
   * some queries to the TSeis.
   *
   *
   *
   */
  public void testWorkManagerImplementaion() throws Fault {

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
    String toCheck1 = "NoTxResourceAdapter WorkManager Not Null";
    String toCheck2 = "Work Object Submitted";

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
      throw new Fault("WorkManager is null or corrupted");
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
   * @testName: testWorkListenerImplementation
   *
   *
   * @assertion_ids: Connector:SPEC:77; Connector:JAVADOC:365;
   * Connector:JAVADOC:392;
   *
   *
   * @test_Strategy: Submit a work object to the WorkManager and see if the
   * notification is being received by the WorkListener provided at the time of
   * submition.
   * 
   * Check for the validity of the connection which is returned by performing
   * some queries to the TSeis.
   *
   *
   */
  public void testWorkListenerImplementation() throws Fault {

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
    String toCheck1 = "WorkListenerImpl.workAccepted";

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
      throw new Fault("WorkManager is null or corrupted");
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
   * @testName: testScheduleWork
   *
   * @assertion_ids: Connector:SPEC:77; Connector:JAVADOC:400;
   *
   * @test_Strategy: Submit a work object to the WorkManager using scheduleWork
   * and see it the work is completed.
   * 
   * Check for the validity of the connection which is returned by performing
   * some queries to the TSeis.
   *
   *
   */
  public void testScheduleWork() throws Fault {

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
    String toCheck1 = "Schedule Work Called";

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
      throw new Fault("WorkManager is null or corrupted");
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
   * @testName: testScheduleWorkListener
   *
   * @assertion_ids: Connector:SPEC:77; Connector:JAVADOC:339;
   *
   * @test_Strategy: Submit a work object to the WorkManager using scheduleWork
   * with worklistener call and see it the work is completed.
   * 
   * Check for the validity of the connection which is returned by performing
   * some queries to the TSeis.
   *
   *
   */
  public void testScheduleWorkListener() throws Fault {

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
    String toCheck1 = "Schedule Work Listener Called";

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
      throw new Fault("WorkManager is null or corrupted");
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
   * @testName: testWorkCompletedException
   *
   * @assertion_ids: Connector:SPEC:73; Connector:JAVADOC:397;
   * Connector:JAVADOC:405; Connector:JAVADOC:407;
   *
   * @test_Strategy: Submit a rogue work object to the WorkManager and see if it
   * throws a WorkCompletedException.
   * 
   * Check for the validity of the connection which is returned by performing
   * some queries to the TSeis.
   *
   *
   */
  public void testWorkCompletedException() throws Fault {

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
    String toCheck1 = "Rogue work throws WorkCompletedException";

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
      throw new Fault("WorkCompletedException not thrown");
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
   * @testName: testForUnsharedTimer
   *
   * @assertion_ids: Connector:SPEC:80; Connector:JAVADOC:128;
   *
   *
   * @test_Strategy: Get two Timer instance from BootStrapContext check for
   * equality of the intance. If they are not equal then the tests passes. If
   * UnavailableException is thrown then test passes. If
   * UnsupportedOperationException then tests passes.
   * 
   * Check for the validity of the connection which is returned by performing
   * some queries to the TSeis for end to end verification.
   *
   */
  public void testForUnsharedTimer() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    boolean b4 = false;

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
    String toCheck1 = "New Timer Provided by BootstrapContext";
    String toCheck2 = "Timer UnavailableException";
    String toCheck3 = "Timer UnsupportedOperationException";
    String toCheck4 = "Timer is Null";

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

    if (b1 || b2 || b3) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("WorkManager is null or corrupted");
    }

    if (b4) {
      throw new Fault("WorkManager is null or corrupted");
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
   * @testName: testNestedWork
   *
   * @assertion_ids: Connector:SPEC:71;
   *
   *
   * @test_Strategy: WorkImpl object in whitebox creates a nested work object
   * which is then submitted to the workmanager. If the NestWork object is
   * sucessfully submitted to the WorkManager then the test passes.
   * 
   * Check for the validity of the connection which is returned by performing
   * some queries to the TSeis for end to end verification.
   *
   */
  public void testNestedWork() throws Fault {

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
    String toCheck1 = "NestWork.run";

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
      throw new Fault("WorkManager is null or corrupted");
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
   * @testName: testUnknownWorkDuration
   *
   * @assertion_ids: Connector:SPEC:76; Connector:JAVADOC:406;
   *
   *
   * @test_Strategy: Submit Work object to WorkManager with a low startTimeout.
   * See if the Value returned from the WorkManager is less than -1. If the
   * value is less than -1 then tests fails. If the value is -1 or a positive
   * value then the test passes.
   * 
   * Check for the validity of the connection which is returned by performing
   * some queries to the TSeis for end to end verification.
   *
   */
  public void testUnknownWorkDuration() throws Fault {

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
    String toCheck1 = "WorkManager value returned";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);

      if (str.startsWith(toCheck1)) {
        int endVal = str.length();
        String str1 = str.substring(27, endVal);
        int value = Integer.parseInt(str1);
        if (value < -1) {
          b1 = false;
        } else {
          b1 = true;
        }
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("WorkManager returned value less than -1");
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
    ds1.clearLog();
    try {
      con.close();
    } catch (Exception sqle) {
      TestUtil.logErr("Exception on cleanup: " + sqle.getMessage(), sqle);
    }
  }

}
