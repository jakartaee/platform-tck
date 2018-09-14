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
 * @(#)scalarClient3.java	1.21 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.escapeSyntax.scalar3;

import java.io.*;
import java.util.*;

import java.sql.*;
import javax.sql.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.rmi.RemoteException;

import com.sun.javatest.Status;
import com.sun.ts.tests.jdbc.ee.common.*;

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The scalarClient3 class tests methods of scalar Functions interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class scalarClient3 extends ServiceEETest {
  private static final String testName = "jdbc.ee.escapeSyntax";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private Statement stmt = null;

  private ResultSet rs = null;

  private String drManager = null;

  private fnSchema fnSch = null;

  private Properties props = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    scalarClient3 theTests = new scalarClient3();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */

  /*
   * @class.setup_props: Driver, the Driver name; db1, the database name with
   * url; user1, the database user name; password1, the database password; db2,
   * the database name with url; user2, the database user name; password2, the
   * database password; DriverManager, flag for DriverManager; ptable, the
   * primary table; ftable, the foreign table; cofSize, the initial size of the
   * ptable; cofTypeSize, the initial size of the ftable; binarySize, size of
   * binary data type; varbinarySize, size of varbinary data type;
   * longvarbinarySize, size of longvarbinary data type;
   * 
   * @class.testArgs: -ap tssql.stmt
   */
  public void setup(String[] args, Properties p) throws Fault {
    try {
      try {
        props = p;
        drManager = p.getProperty("DriverManager", "");
        if (drManager.length() == 0)
          throw new Fault("Invalid DriverManager Name");

        if (drManager.equals("yes")) {
          logTrace("Using DriverManager");
          DriverManagerConnection dmCon = new DriverManagerConnection();
          conn = dmCon.getConnection(p);
        } else {
          logTrace("Using DataSource");
          DataSourceConnection dsCon = new DataSourceConnection();
          conn = dsCon.getConnection(p);
        }
        stmt = conn.createStatement();
        fnSch = new fnSchema();
        msg = new JDBCTestMsg();
        fnSch.createTable(p, conn);

      } catch (SQLException ex) {
        logErr("SQL Exception : " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!", e);
    }
  }

  /*
   * @testName: testWeek
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function week. It should return an
   * integer.
   *
   */
  public void testWeek() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("WEEK", conn) == true) {

        isFuncFound = true;
        msg.setMsg("Time Date function WEEK  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("Time Date function WEEK is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testWeek failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function week
        queryString = props.getProperty("Week_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Week of the year: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testWeek failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testWeek failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testMonth
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function month. It should return an
   * integer.
   *
   */
  public void testMonth() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("MONTH", conn) == true) {

        isFuncFound = true;
        msg.setMsg("Time Date function MONTH  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("Time Date function MONTH is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testMonth failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function month
        queryString = props.getProperty("Month_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Month of the year: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testMonth failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testMonth failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testYear
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function year. It should return an
   * integer.
   *
   */
  public void testYear() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("YEAR", conn) == true) {

        isFuncFound = true;
        msg.setMsg("Time Date function YEAR  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("Time Date function YEAR is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testYear failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function year
        queryString = props.getProperty("Year_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Year component of date: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testYear failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testYear failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testMonthname
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function monthname. It should return a
   * character string.
   *
   */
  public void testMonthname() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("MONTHNAME", conn) == true) {

        isFuncFound = true;
        msg.setMsg("Time Date function MONTHNAME  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg(
            "Time Date function MONTHNAME is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testMonthname failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function monthname
        queryString = props.getProperty("Monthname_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Month Name: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testMonthname failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testMonthname failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testQuarter
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function quarter. It should return an
   * integer.
   *
   */
  public void testQuarter() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("QUARTER", conn) == true) {

        isFuncFound = true;
        msg.setMsg("Time Date function QUARTER  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("Time Date function QUARTER is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testQuarter failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function quarter
        queryString = props.getProperty("Quarter_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Quarter in date: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testQuarter Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testQuarter Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testNow
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function now. It should return a timestamp
   * value.
   *
   */
  public void testNow() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("NOW", conn) == true) {

        isFuncFound = true;
        msg.setMsg("Time Date function NOW  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("Time Date function NOW is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testNow failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function now
        queryString = props.getProperty("Now_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Current date & Time: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testNow failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testNow Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testHour
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function hour. It should return an
   * integer.
   *
   */
  public void testHour() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("HOUR", conn) == true) {

        isFuncFound = true;
        msg.setMsg("Time Date function HOUR  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("Time Date function HOUR is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testHour failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function hour
        queryString = props.getProperty("Hour_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Hour component of time: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testHour failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testHour failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testMinute
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function minute. It should return an
   * integer.
   *
   */
  public void testMinute() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("MINUTE", conn) == true) {

        isFuncFound = true;
        msg.setMsg("Time Date function MINUTE  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("Time Date function MINUTE is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testMinute failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function minute
        queryString = props.getProperty("Minute_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Minute component of time: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testMinute failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testMinute Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testSecond
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function second. It should return an
   * integer.
   *
   */
  public void testSecond() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("SECOND", conn) == true) {

        isFuncFound = true;
        msg.setMsg("Time Date function SECOND  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("Time Date function SECOND is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testSecond failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function second
        queryString = props.getProperty("Second_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Second component of time: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testSecond failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testSecond failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testDatabase
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function database. It should return a
   * string.
   *
   */
  public void testDatabase() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isSystemFuncFound("DATABASE", conn) == true) {

        isFuncFound = true;
        msg.setMsg("System function DATABASE is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("System function DATABASE is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testDatabase Failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function database
        queryString = props.getProperty("Database_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Database Name: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testDatabase Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testDatabase Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testAcos
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function acos. It should return a numeric
   * value.
   *
   */
  public void testAcos() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("ACOS", conn) == true) {

        isFuncFound = true;
        msg.setMsg("Numeric function ACOS  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("Numeric function ACOS is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testAcos failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function acos
        queryString = props.getProperty("Acos_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("ACOS value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testAcos Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testAcos Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testAsin
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function asin. It should return a numeric
   * value.
   *
   */
  public void testAsin() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("ASIN", conn) == true) {

        isFuncFound = true;
        msg.setMsg("Numeric function ASIN  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("Numeric function ASIN is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testAsin failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function asin
        queryString = props.getProperty("Asin_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("ASIN value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testAsin failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testAsin failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testAtan
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function atan. It should return a numeric
   * value.
   *
   */
  public void testAtan() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("ATAN", conn) == true) {

        isFuncFound = true;
        msg.setMsg("Numeric function ATAN  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("Numeric function ATAN is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testAtan failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function atan
        queryString = props.getProperty("Atan_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("ATAN value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testAtan failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testAtan failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testAtan2
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function atan2. It should return a numeric
   * value.
   *
   */
  public void testAtan2() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("ATAN2", conn) == true) {

        isFuncFound = true;
        msg.setMsg("Numeric function ATAN2 is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("Numeric function ATAN2 is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testAtan2 failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function atan2
        queryString = props.getProperty("Atan2_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("ATAN value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testAtan2 failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testAtan2 failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testDegrees
   * 
   * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function degrees. It should return a
   * numeric value.
   *
   */
  public void testDegrees() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("DEGREES", conn) == true) {

        isFuncFound = true;
        msg.setMsg("Numeric function DEGREES is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("Numeric function DEGREES is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testDegrees failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function degrees
        queryString = props.getProperty("Degrees_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("DEGREES value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testDegrees failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testDegrees failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testRadians
   * 
   * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function radians. It should return a
   * numeric value.
   *
   */
  public void testRadians() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("RADIANS", conn) == true) {

        isFuncFound = true;
        msg.setMsg("Numeric function RADIANS is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("Numeric function RADIANS is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testRadians failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function radians
        queryString = props.getProperty("Radians_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("RADIANS value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testRadians failed!");
      } catch (Exception e) {
        msg.printError(e, "Call to testRadians failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testPi
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function pi. It should return the constant
   * value of PI.
   *
   */
  public void testPi() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("PI", conn) == true) {

        isFuncFound = true;
        msg.setMsg("Numeric function PI is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("Numeric function PI is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testPi failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function pi
        queryString = props.getProperty("Pi_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("PI value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testPi failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testPi failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testRand
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function rand. It should return a numeric
   * value.
   *
   */
  public void testRand() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("RAND", conn) == true) {

        isFuncFound = true;
        msg.setMsg("Numeric function RAND is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("Numeric function RAND is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testRand Failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function rand
        queryString = props.getProperty("Rand_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("RAND value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testRand Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testRand Failed!");

      }
    }
  }

  /*
   * @testName: testDifference
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function difference. It should return an
   * integer.
   *
   */
  public void testDifference() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isStringFuncFound("DIFFERENCE", conn) == true) {
        // Function is supported by the DBMS
        isFuncFound = true;
        msg.setMsg("String function DIFFERENCE is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("String function DIFFERENCE is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testDifference Failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function difference
        queryString = props.getProperty("Difference_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("DIFFERENCE value: <" + retString + ">");
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testDifference Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testDifference Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testLocate02
   * 
   * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function locate. It should return an
   * integer.
   *
   */
  public void testLocate02() throws Fault {

    try {
      String queryString = null;
      // Query that contains a call to the function locate
      queryString = props.getProperty("Locate_Fn_Query", "");

      rs = stmt.executeQuery(queryString);
      rs.next();
      String retString = rs.getString(1);
      msg.setMsg("str1 starts at " + retString + " of str2");
      rs.close();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to testLocate02 Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to testLocate02 Failed!");

    }
    msg.printTestMsg();
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      stmt.close();
      // Close the database
      fnSch.dropTable(props, conn);
      fnSch.dbUnConnect(conn);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }

}
