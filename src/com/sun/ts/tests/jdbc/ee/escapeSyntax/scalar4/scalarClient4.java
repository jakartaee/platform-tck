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
 * @(#)scalarClient4.java	1.20 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.escapeSyntax.scalar4;

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
 * The scalarClient4 class tests methods of scalar Functions interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class scalarClient4 extends ServiceEETest {
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
    scalarClient4 theTests = new scalarClient4();
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
        fnSch.createTable(p, conn);
        msg = new JDBCTestMsg();

      } catch (SQLException ex) {
        logErr("SQL Exception : " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!", e);
    }
  }

  /*
   * @testName: testTimestampAdd01
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function timestampadd with the interval
   * value as SQL_TSI_FRAC_SECOND. It should return a timestamp value.
   *
   */
  public void testTimestampAdd01() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("TIMESTAMPADD", conn) == true) {

        isFuncFound = true;
        msg.setMsg(
            "Time Date function TIMESTAMPADD  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg(
            "Time Date function TIMESTAMPADD is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testTimestampAdd01 failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function timestampadd
        queryString = props.getProperty("Timestampaddfrac_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Timestamp value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testTimestampAdd01 failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testTimestampAdd01 failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testTimestampAdd02
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function timestampadd with the interval
   * value as SQL_TSI_SECOND. It should return a timestamp value.
   *
   */
  public void testTimestampAdd02() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("TIMESTAMPADD", conn) == true) {

        isFuncFound = true;
        msg.setMsg(
            "Time Date function TIMESTAMPADD  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg(
            "Time Date function TIMESTAMPADD is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testTimestampAdd02 failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function timestampadd
        queryString = props.getProperty("Timestampaddsecond_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Timestamp value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testTimestampAdd02 Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testTimestampAdd02 Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testTimestampAdd03
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function timestampadd with the interval
   * value as SQL_TSI_MINUTE. It should return a timestamp value.
   *
   */
  public void testTimestampAdd03() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("TIMESTAMPADD", conn) == true) {

        isFuncFound = true;
        msg.setMsg(
            "Time Date function TIMESTAMPADD  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg(
            "Time Date function TIMESTAMPADD is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testTimestampAdd03 failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function timestampadd
        queryString = props.getProperty("Timestampaddminute_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Timestamp value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testTimestampAdd03 Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testTimestampAdd03 Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testTimestampAdd04
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function timestampadd with the interval
   * value as SQL_TSI_HOUR. It should return a timestamp value.
   *
   */
  public void testTimestampAdd04() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("TIMESTAMPADD", conn) == true) {

        isFuncFound = true;
        msg.setMsg(
            "Time Date function TIMESTAMPADD  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg(
            "Time Date function TIMESTAMPADD is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testTimestampAdd04 failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function timestampadd
        queryString = props.getProperty("Timestampaddhour_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Timestamp value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testTimestampAdd04 failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testTimestampAdd04 failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testTimestampAdd05
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function timestampadd with the interval
   * value as SQL_TSI_DAY. It should return a timestamp value.
   *
   */
  public void testTimestampAdd05() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("TIMESTAMPADD", conn) == true) {

        isFuncFound = true;
        msg.setMsg(
            "Time Date function TIMESTAMPADD  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg(
            "Time Date function TIMESTAMPADD is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testTimestampAdd05 failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function timestampadd
        queryString = props.getProperty("Timestampaddday_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Timestamp value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testTimestampAdd05 Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testTimestampAdd05 Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testTimestampAdd06
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function timestampadd with the interval
   * value as SQL_TSI_WEEK. It should return a timestamp value.
   *
   */
  public void testTimestampAdd06() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("TIMESTAMPADD", conn) == true) {

        isFuncFound = true;
        msg.setMsg(
            "Time Date function TIMESTAMPADD  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg(
            "Time Date function TIMESTAMPADD is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testTimestampAdd06 failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function timestampadd
        queryString = props.getProperty("Timestampaddweek_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Timestamp value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testTimestampAdd06 failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testTimestampAdd06 failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testTimestampAdd07
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function timestampadd with the interval
   * value as SQL_TSI_MONTH. It should return a timestamp value.
   *
   */
  public void testTimestampAdd07() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("TIMESTAMPADD", conn) == true) {

        isFuncFound = true;
        msg.setMsg(
            "Time Date function TIMESTAMPADD  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg(
            "Time Date function TIMESTAMPADD is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testTimestampAdd07 failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function timestampadd
        queryString = props.getProperty("Timestampaddmonth_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Timestamp value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testTimestampAdd07 failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testTimestampAdd07 failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testTimestampAdd08
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function timestampadd with the interval
   * value as SQL_TSI_QUARTER. It should return a timestamp value.
   *
   */
  public void testTimestampAdd08() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("TIMESTAMPADD", conn) == true) {

        isFuncFound = true;
        msg.setMsg(
            "Time Date function TIMESTAMPADD  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg(
            "Time Date function TIMESTAMPADD is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testTimestampAdd08 failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function timestampadd
        queryString = props.getProperty("Timestampaddquarter_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Timestamp value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testTimestampAdd08 Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testTimestampAdd08 Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testTimestampAdd09
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function timestampadd with the interval
   * value as SQL_TSI_YEAR. It should return a timestamp value.
   *
   */
  public void testTimestampAdd09() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("TIMESTAMPADD", conn) == true) {

        isFuncFound = true;
        msg.setMsg(
            "Time Date function TIMESTAMPADD  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg(
            "Time Date function TIMESTAMPADD is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testTimestampAdd09 failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function timestampadd
        queryString = props.getProperty("Timestampaddyear_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Timestamp value: " + retString);
        rs.close();
        msg.printTestMsg();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testTimestampAdd09 failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testTimestampAdd09 failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testTimestampDiff01
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function timestampdiff with the interval
   * value as SQL_TSI_FRAC_SECOND. It should return an integer.
   *
   */
  public void testTimestampDiff01() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("TIMESTAMPDIFF", conn) == true) {

        isFuncFound = true;
        msg.setMsg(
            "Time Date function TIMESTAMPDIFF  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg(
            "Time Date function TIMESTAMPDIFF is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testTimestampDiff01 failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function timestampdiff
        queryString = props.getProperty("Timestampdifffrac_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Integer value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testTimestampDiff01 Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testTimestampDiff01 Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testTimestampDiff02
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function timestampdiff with the interval
   * value as SQL_TSI_SECOND. It should return an integer.
   *
   */
  public void testTimestampDiff02() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("TIMESTAMPDIFF", conn) == true) {

        isFuncFound = true;
        msg.setMsg(
            "Time Date function TIMESTAMPDIFF  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg(
            "Time Date function TIMESTAMPDIFF is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testTimestampDiff02 failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function timestampdiff
        queryString = props.getProperty("Timestampdiffsecond_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Integer value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testTimestampDiff02 failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testTimestampDiff02 failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testTimestampDiff03
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function timestampdiff with the interval
   * value as SQL_TSI_MINUTE. It should return an integer.
   *
   */
  public void testTimestampDiff03() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("TIMESTAMPDIFF", conn) == true) {

        isFuncFound = true;
        msg.setMsg(
            "Time Date function TIMESTAMPDIFF  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg(
            "Time Date function TIMESTAMPDIFF is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testTimestampDiff03 failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function timestampdiff
        queryString = props.getProperty("Timestampdiffminute_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Integer value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testTimestampDiff03 Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testTimestampDiff03 Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testTimestampDiff04
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function timestampdiff with the interval
   * value as SQL_TSI_HOUR. It should return an integer.
   *
   */
  public void testTimestampDiff04() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("TIMESTAMPDIFF", conn) == true) {

        isFuncFound = true;
        msg.setMsg(
            "Time Date function TIMESTAMPDIFF  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg(
            "Time Date function TIMESTAMPDIFF is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testTimestampDiff04 failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function timestampdiff
        queryString = props.getProperty("Timestampdiffhour_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Integer value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testTimestampDiff04 failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testTimestampDiff04 failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testTimestampDiff05
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function timestampdiff with the interval
   * value as SQL_TSI_DAY. It should return an integer.
   *
   */
  public void testTimestampDiff05() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("TIMESTAMPDIFF", conn) == true) {

        isFuncFound = true;
        msg.setMsg(
            "Time Date function TIMESTAMPDIFF  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg(
            "Time Date function TIMESTAMPDIFF is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testTimestampDiff05 failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function timestampdiff
        queryString = props.getProperty("Timestampdiffday_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Integer value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testTimestampDiff05 failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testTimestampDiff05 failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testTimestampDiff06
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function timestampdiff with the interval
   * value as SQL_TSI_WEEK. It should return an integer.
   *
   */
  public void testTimestampDiff06() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("TIMESTAMPDIFF", conn) == true) {

        isFuncFound = true;
        msg.setMsg(
            "Time Date function TIMESTAMPDIFF  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg(
            "Time Date function TIMESTAMPDIFF is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testTimestampDiff06 failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function timestampdiff
        queryString = props.getProperty("Timestampdiffweek_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Integer value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testTimestampDiff06 failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testTimestampDiff06 failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testTimestampDiff07
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function timestampdiff with the interval
   * value as SQL_TSI_MONTH. It should return an integer.
   *
   */
  public void testTimestampDiff07() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("TIMESTAMPDIFF", conn) == true) {

        isFuncFound = true;
        msg.setMsg(
            "Time Date function TIMESTAMPDIFF  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg(
            "Time Date function TIMESTAMPDIFF is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testTimestampDiff07 failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function timestampdiff
        queryString = props.getProperty("Timestampdiffmonth_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Integer value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testTimestampDiff07 failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testTimestampDiff07 failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testTimestampDiff08
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function timestampdiff with the interval
   * value as SQL_TSI_QUARTER. It should return an integer.
   *
   */
  public void testTimestampDiff08() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("TIMESTAMPDIFF", conn) == true) {

        isFuncFound = true;
        msg.setMsg(
            "Time Date function TIMESTAMPDIFF  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg(
            "Time Date function TIMESTAMPDIFF is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testTimestampDiff08 failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function timestampdiff
        queryString = props.getProperty("Timestampdiffquarter_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Integer value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testTimestampDiff08 Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testTimestampDiff08 Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testTimestampDiff09
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function timestampdiff with the interval
   * value as SQL_TSI_YEAR. It should return an integer.
   *
   */
  public void testTimestampDiff09() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("TIMESTAMPDIFF", conn) == true) {

        isFuncFound = true;
        msg.setMsg(
            "Time Date function TIMESTAMPDIFF  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg(
            "Time Date function TIMESTAMPDIFF is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testTimestampDiff09 failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function timestampdiff
        queryString = props.getProperty("Timestampdiffyear_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Integer value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testTimestampDiff09 failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testTimestampDiff09 failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testLeftOuterjoin
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a left outer join. It should return a ResultSet object.
   *
   */
  public void testLeftOuterjoin() throws Fault {
    boolean outerJoinSupport = false;

    try {
      // Check if left outer join is supported
      DatabaseMetaData dbmd = conn.getMetaData();
      outerJoinSupport = dbmd.supportsFullOuterJoins();
      if (outerJoinSupport == true) {

        msg.setMsg("Outer Join is supported");
      } else {

        msg.setMsg("Outer Join is not supported");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testOuterJoin failed!");

    }
    if (outerJoinSupport == true) {
      try {

        String queryString = null;
        // Query that contains a left outer join
        queryString = props.getProperty("Left_Oj_Query", "");

        rs = stmt.executeQuery(queryString);
        msg.setMsg("queryString: " + queryString);
        String retString;
        while (rs.next()) {
          retString = rs.getString(1);
          msg.setMsg("ResultSet value: " + retString);
        }
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testLeftOuterjoin Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testLeftOuterjoin Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testRightOuterjoin
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a right outer join. It should return a ResultSet object.
   * 
   */
  public void testRightOuterjoin() throws Fault {
    boolean outerJoinSupport = false;
    try {
      // Check if right outer join is supported
      DatabaseMetaData dbmd = conn.getMetaData();
      outerJoinSupport = dbmd.supportsFullOuterJoins();
      if (outerJoinSupport == true) {

        msg.setMsg("Outer Join is supported");
      } else {

        msg.setMsg("Outer Join is not supported");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testRightOuterjoin failed!");

    }
    if (outerJoinSupport == true) {
      try {

        String queryString = null;
        // Query that contains a right outer join
        queryString = props.getProperty("Right_Oj_Query", "");

        rs = stmt.executeQuery(queryString);
        msg.setMsg("queryString: " + queryString);
        String retString;
        while (rs.next()) {
          retString = rs.getString(1);
          msg.setMsg("ResultSet value: " + retString);
        }
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testRightOuterjoin failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testRightOuterjoin failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testFullOuterjoin
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a full outer join. It should return a ResultSet object.
   *
   */
  public void testFullOuterjoin() throws Fault {
    boolean fullOuterJoinSupport = false;
    try {
      // Check if full outer join is supported
      DatabaseMetaData dbmd = conn.getMetaData();
      fullOuterJoinSupport = dbmd.supportsFullOuterJoins();
      if (fullOuterJoinSupport == true) {

        msg.setMsg("Full Outer Join is supported");
      } else {

        msg.setMsg("Full Outer Join is not supported");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testFullOuterjoin failed!");

    }
    if (fullOuterJoinSupport == true) {
      try {
        String queryString = null;
        // Query that contains a full outer join
        queryString = props.getProperty("Full_Oj_Query", "");

        rs = stmt.executeQuery(queryString);
        msg.setMsg("queryString: " + queryString);
        String retString;
        while (rs.next()) {
          retString = rs.getString(1);
          msg.setMsg("ResultSet value: " + retString);
        }
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testFullOuterjoin failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testFullOuterjoin failed!");

      }
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
