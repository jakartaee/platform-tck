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
 * @(#)scalarClient2.java	1.21 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.escapeSyntax.scalar2;

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
 * The scalarClient2 class tests methods of scalar Functions interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class scalarClient2 extends ServiceEETest {
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
    scalarClient2 theTests = new scalarClient2();
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
   * @testName: testAbs
   * 
   * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function abs. It should return a numeric
   * value.
   *
   */
  public void testAbs() throws Fault {

    try {
      String queryString = null;
      // Query that contains a call to the function abs
      queryString = props.getProperty("Abs_Fn_Query", "");

      rs = stmt.executeQuery(queryString);
      rs.next();
      String retString = rs.getString(1);
      msg.setMsg("Absolute value : " + retString);
      rs.close();
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to testAbs Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to testAbs Failed!");

    }

  }

  /*
   * @testName: testPower
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function power. It should return a numeric
   * value.
   *
   */
  public void testPower() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("POWER", conn) == true) {
        isFuncFound = true;
        msg.setMsg("Numeric function POWER  is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("Numeric function POWER is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testPower failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function power
        queryString = props.getProperty("Power_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Power value : " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testPower Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testPower Failed!");

      }
    }
    msg.printTestMsg();

  }

  /*
   * @testName: testRound
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function round. It should return a numeric
   * value.
   *
   */
  public void testRound() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("ROUND", conn) == true) {

        isFuncFound = true;
        msg.setMsg("Numeric function ROUND  is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("Numeric function ROUND is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testRound failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function round
        queryString = props.getProperty("Round_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Rounded value : " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testRound failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testRound failed!");

      }

    }
    msg.printTestMsg();
  }

  /*
   * @testName: testSign
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function sign. It should return an
   * integer.
   *
   */
  public void testSign() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("SIGN", conn) == true) {
        isFuncFound = true;
        msg.setMsg("Numeric function SIGN  is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("Numeric function SIGN is not supported by this DBMS");
      }

    } catch (Exception e) {
      msg.printError(e, "Call to testSign failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function sign
        queryString = props.getProperty("Sign_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Sign value : " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testSign failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testSign failed!");

      }
    }
    msg.printTestMsg();

  }

  /*
   * @testName: testSqrt
   * 
   * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function sqrt. It should return a numeric
   * value.
   *
   */
  public void testSqrt() throws Fault {

    try {
      String queryString = null;
      msg.setMsg("Query that contains a call to the function sqrt");
      queryString = props.getProperty("Sqrt_Fn_Query", "");
      msg.setMsg("queryString");
      rs = stmt.executeQuery(queryString);
      rs.next();
      String retString = rs.getString(1);
      msg.setMsg("Sqrt value : " + retString);
      rs.close();
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to testSqrt Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to testSqrt Failed!");

    }

  }

  /*
   * @testName: testTruncate
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function truncate. It should return a
   * numeric value.
   *
   */
  public void testTruncate() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("TRUNCATE", conn) == true) {
        isFuncFound = true;
        msg.setMsg("Numeric function TRUNCATE  is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("Numeric function TRUNCATE is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testTruncate failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        msg.setMsg("Query that contains a call to the function truncate");
        queryString = props.getProperty("Truncate_Fn_Query", "");
        msg.setMsg(queryString);
        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Truncated Number: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testTruncate Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testTruncate Failed!");

      }
    }
    msg.printTestMsg();

  }

  /*
   * @testName: testMod
   * 
   * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function mod. It should return an integer.
   *
   */
  public void testMod() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("MOD", conn) == true) {
        isFuncFound = true;
        msg.setMsg("Numeric function MOD  is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("Numeric function MOD is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testMod failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        msg.setMsg("Query that contains a call to the function mod");
        queryString = props.getProperty("Mod_Fn_Query", "");
        msg.setMsg(queryString);
        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Remainder value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testMod Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testMod Failed!");

      }
    }
    msg.printTestMsg();

  }

  /*
   * @testName: testFloor
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function floor. It should return an
   * integer.
   *
   */
  public void testFloor() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("FLOOR", conn) == true) {
        isFuncFound = true;
        msg.setMsg("Numeric function FLOOR  is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("Numeric function FLOOR is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testFloor failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        msg.setMsg("Query that contains a call to the function floor");
        queryString = props.getProperty("Floor_Fn_Query", "");
        msg.setMsg(queryString);
        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Largest Integer: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testFloor failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testFloor failed!");

      }
    }
    msg.printTestMsg();

  }

  /*
   * @testName: testCeiling
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function ceiling. It should return an
   * integer.
   *
   */
  public void testCeiling() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("CEILING", conn) == true) {
        isFuncFound = true;
        msg.setMsg("Numeric function CEILING  is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("Numeric function CEILING is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testCeiling failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        msg.setMsg("Query that contains a call to the function ceiling");
        queryString = props.getProperty("Ceiling_Fn_Query", "");
        msg.setMsg(queryString);
        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Smallest Integer: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testCeiling failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testCeiling failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testLog10
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function log10. It should return a numeric
   * value.
   *
   */
  public void testLog10() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("LOG10", conn) == true) {
        isFuncFound = true;
        msg.setMsg("Numeric function LOG10  is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("Numeric function LOG10 is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testLog10 failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function log10
        queryString = props.getProperty("Log10_Fn_Query", "");

        msg.setMsg(queryString);
        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Log10 value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testLog10 failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testLog10 failed!");

      }
    }
    msg.printTestMsg();

  }

  /*
   * @testName: testLog
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function log. It should return a numeric
   * value.
   *
   */
  public void testLog() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("LOG", conn) == true) {
        isFuncFound = true;
        msg.setMsg("Numeric function LOG  is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("Numeric function LOG is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testLog failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function log
        queryString = props.getProperty("Log_Fn_Query", "");
        msg.setMsg(queryString);
        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Log e  value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testLog failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testLog failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testExp
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function exp. It should return a numeric
   * value.
   *
   */
  public void testExp() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("EXP", conn) == true) {
        isFuncFound = true;
        msg.setMsg("Numeric function EXP  is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("Numeric function EXP is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testExp failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function exp
        queryString = props.getProperty("Exp_Fn_Query", "");
        msg.setMsg(queryString);
        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Exponential value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testExp failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testExp failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testCos
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function cos. It should return a numeric
   * value.
   *
   */
  public void testCos() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("COS", conn) == true) {
        isFuncFound = true;
        msg.setMsg("Numeric function COS  is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("Numeric function COS is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testCos failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function cos
        queryString = props.getProperty("Cos_Fn_Query", "");
        msg.setMsg(queryString);
        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("COS value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testCos failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testCos failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testTan
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function tan. It should return a numeric
   * value.
   * 
   */
  public void testTan() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("TAN", conn) == true) {
        isFuncFound = true;
        msg.setMsg("Numeric function TAN  is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("Numeric function TAN is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testTan failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function tan
        queryString = props.getProperty("Tan_Fn_Query", "");
        msg.setMsg(queryString);
        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("TAN value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testTan failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testTan failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testCot
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function cot. It should return a numeric
   * value.
   *
   *
   */
  public void testCot() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("COT", conn) == true) {
        isFuncFound = true;
        msg.setMsg("Numeric function COT  is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("Numeric function COT is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testCot failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function cot
        queryString = props.getProperty("Cot_Fn_Query", "");
        msg.setMsg(queryString);
        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("COT value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testCot failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testCot failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testCurdate
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function curdate. It should return a date
   * value.
   *
   */
  public void testCurdate() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isTimeDateFuncFound("CURDATE", conn) == true) {
        isFuncFound = true;
        msg.setMsg("Time Date function CURDATE  is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("Time Date function CURDATE is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testCurdate failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function curdate
        queryString = props.getProperty("Curdate_Fn_Query", "");
        msg.setMsg(queryString);
        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Current Date  value: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testCurdate failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testCurdate failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testDayname
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function dayname. It should return a
   * character string.
   *
   */
  public void testDayname() throws Fault {
    boolean isFuncFound = false;
    try {
      if (fnSch.isTimeDateFuncFound("DAYNAME", conn) == true) {
        isFuncFound = true;
        msg.setMsg("Time Date function DAYNAME  is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("Time Date function DAYNAME is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testDayname failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        queryString = props.getProperty("Dayname_Fn_Query", "");
        msg.setMsg(queryString);
        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Day Name: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testDayname failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testDayname failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testDayofmonth
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function dayofmonth. It should return an
   * integer.
   *
   */
  public void testDayofmonth() throws Fault {
    boolean isFuncFound = false;
    try {
      if (fnSch.isTimeDateFuncFound("DAYOFMONTH", conn) == true) {
        isFuncFound = true;
        msg.setMsg("Time Date function DAYOFMONTH  is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg(
            "Time Date function DAYOFMONTH is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testDayofmonth failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        queryString = props.getProperty("Dayofmonth_Fn_Query", "");
        msg.setMsg(queryString);
        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Day of Month: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testDayofmonth failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testDayofmonth failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testDayofweek
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function dayofweek. It should return an
   * integer.
   *
   */
  public void testDayofweek() throws Fault {
    boolean isFuncFound = false;
    try {
      if (fnSch.isTimeDateFuncFound("DAYOFWEEK", conn) == true) {
        isFuncFound = true;
        msg.setMsg("Time Date function DAYOFWEEK  is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg(
            "Time Date function DAYOFWEEK is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testDayofweek failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        queryString = props.getProperty("Dayofweek_Fn_Query", "");
        msg.setMsg(queryString);
        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Day of Week: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testDayofweek failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testDayofweek failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testDayofyear
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function dayofyear. It should return an
   * integer.
   *
   */
  public void testDayofyear() throws Fault {
    boolean isFuncFound = false;
    try {
      if (fnSch.isTimeDateFuncFound("DAYOFYEAR", conn) == true) {
        isFuncFound = true;
        msg.setMsg("Time Date function DAYOFYEAR  is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg(
            "Time Date function DAYOFYEAR is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testDayofyear failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        queryString = props.getProperty("Dayofyear_Fn_Query", "");
        msg.setMsg(queryString);
        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Day of Year: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testDayofyear failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testDayofyear failed!");

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
