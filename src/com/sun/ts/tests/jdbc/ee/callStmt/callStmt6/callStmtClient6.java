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
 * @(#)callStmtClient6.java	1.16 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt6;

import java.io.*;
import java.util.*;
import java.math.*;

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
 * The callStmtClient6 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class callStmtClient6 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt6";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private DataSource ds1 = null;

  private rsSchema rsSch = null;

  private JDBCTestMsg msg = null;

  private String drManager = null;

  private Properties sqlp = null;

  private CallableStatement cstmt = null;

  private transient DatabaseMetaData dbmd = null;

  private Statement stmt = null;

  private ResultSet rs = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    callStmtClient6 theTests = new callStmtClient6();
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
        drManager = p.getProperty("DriverManager", "");
        if (drManager.length() == 0)
          throw new Fault("Invalid DriverManager Name");
        sqlp = p;

        if (drManager.equals("yes")) {
          logTrace("Using DriverManager");
          DriverManagerConnection dmCon = new DriverManagerConnection();
          conn = dmCon.getConnection(p);
        } else {
          logTrace("Using DataSource");
          DataSourceConnection dsCon = new DataSourceConnection();
          conn = dsCon.getConnection(p);
        }
        rsSch = new rsSchema();
        msg = new JDBCTestMsg();
        stmt = conn.createStatement(/*
                                     * ResultSet.TYPE_SCROLL_INSENSITIVE,
                                     * ResultSet.CONCUR_READ_ONLY
                                     */);
        dbmd = conn.getMetaData();
      } catch (SQLException ex) {
        logErr("SQL Exception : " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testSetString01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:674;
   * JDBC:JAVADOC:675; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using setChar(int parameterIndex,String x),update the column
   * minimum value of Char_Tab. Now execute a query to get the minimum value and
   * retrieve the result of the query using the getString(int columnIndex)
   * method.Compare the returned value, with the minimum value extracted from
   * the tssql.stmt file. Both of them should be equal.
   */
  public void testSetString01() throws Fault {
    String minStringVal = null;
    String rStringVal = null;
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_In_Name(?)}");

      msg.setMsg("extract the Minimum Value be Updated");
      minStringVal = rsSch.extractVal("Char_Tab", 2, sqlp, conn);
      minStringVal = minStringVal.substring(1, minStringVal.length() - 1);

      cstmt.setString(1, minStringVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      msg.setMsg("get the query string");
      String Max_Val_Query = sqlp.getProperty("Char_Query_Name", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      rStringVal = rs.getString(1);

      msg.addOutputMsg(minStringVal, rStringVal);
      if (rStringVal.trim().equals(minStringVal.trim())) {
        msg.setMsg(
            "setString Method sets the designated parameter to a String value ");
      } else {
        msg.printTestError(
            "setString Method does not set the designated parameter to a String value ",
            "test setString is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setString Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setString Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Char_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetString02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:674;
   * JDBC:JAVADOC:675; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using setString(int parameterIndex,String x),update the column
   * the maximum value of Char_Tab. Now execute a query to get the maximum value
   * and retrieve the result of the query using the getString(int columnIndex)
   * method.Compare the returned value, with the maximum value extracted from
   * the tssql.stmt file. Both of them should be equal.
   */

  public void testSetString02() throws Fault {
    String maxStringVal = null;
    String rStringVal = null;
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_In_Null(?)}");

      msg.setMsg(" extract the Maximum Value of int to be Updated");
      maxStringVal = rsSch.extractVal("Char_Tab", 1, sqlp, conn);
      maxStringVal = maxStringVal.substring(1, maxStringVal.length() - 1);
      msg.setMsg("String Value :" + maxStringVal);

      cstmt.setString(1, maxStringVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      msg.setMsg("get the query string");
      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getString(1);
      msg.addOutputMsg(maxStringVal, rStringVal);

      if (rStringVal.trim().equals(maxStringVal.trim())) {
        msg.setMsg(
            "setString Method sets the designated parameter to a String value ");
      } else {
        msg.printTestError(
            "setString Method does not set the designated parameter to a String value ",
            "test setString is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setString is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setString Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Char_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetString03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:674;
   * JDBC:JAVADOC:675; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using setString(int parameterIndex,String x),update the column
   * the minimum value of Varchar_Tab. Now execute a query to get the minimum
   * value and retrieve the result of the query using the getString(int
   * columnIndex) method.Compare the returned value, with the minimum value
   * extracted from the tssql.stmt file. Both of them should be equal.
   */
  public void testSetString03() throws Fault {
    String minStringVal = null;
    String rStringVal = null;
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_In_Name(?)}");

      msg.setMsg("to extract the Minimum Value of int to be Updated");
      minStringVal = rsSch.extractVal("Varchar_Tab", 2, sqlp, conn);
      minStringVal = minStringVal.substring(1, minStringVal.length() - 1);
      msg.setMsg("String Value :" + minStringVal);

      cstmt.setString(1, minStringVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      msg.setMsg("get the query string");
      String Max_Val_Query = sqlp.getProperty("Varchar_Query_Name", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      rStringVal = rs.getString(1);

      msg.addOutputMsg(minStringVal, rStringVal);
      if (rStringVal.trim().equals(minStringVal.trim())) {
        msg.setMsg(
            "setString Method sets the designated parameter to a String value ");
      } else {
        msg.printTestError(
            "setString Method does not set the designated parameter to a String value ",
            "test setString Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setString Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setString Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Varchar_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetString04
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:674;
   * JDBC:JAVADOC:675; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using setString(int parameterIndex,String x),update the column
   * the maximum value of Varchar_Tab. Now execute a query to get the maximum
   * value and retrieve the result of the query using the getInt(int
   * columnIndex) method.Compare the returned value, with the maximum value
   * extracted from the tssql.stmt file. Both of them should be equal.
   */
  public void testSetString04() throws Fault {
    String maxStringVal = null;
    String rStringVal = null;
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_In_Null(?)}");

      msg.setMsg("to extract the Maximum Value of int to be Updated");
      maxStringVal = rsSch.extractVal("Varchar_Tab", 1, sqlp, conn);
      maxStringVal = maxStringVal.substring(1, maxStringVal.length() - 1);
      msg.setMsg("String Value :" + maxStringVal);

      cstmt.setString(1, maxStringVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      msg.setMsg("get the query string");
      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getString(1);

      msg.addOutputMsg(maxStringVal, rStringVal);

      if (rStringVal.trim().equals(maxStringVal.trim())) {
        msg.setMsg(
            "setString Method sets the designated parameter to a String value ");
      } else {
        msg.printTestError(
            "setString Method does not set the designated parameter to a String value ",
            "test setString is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setString Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setString Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Varchar_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetString05
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:674;
   * JDBC:JAVADOC:675; JDBC:JAVADOC:398; JDBC:JAVADOC:399; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using setString(int parameterIndex,String x),update the column
   * the minimum value of Longvarchar_Tab. Now execute a query to get the
   * minimum value and retrieve the result of the query using the getString(int
   * columnIndex) method.Compare the returned value, with the minimum value
   * extracted from the tssql.stmt file. Both of them should be equal.
   */
  public void testSetString05() throws Fault {
    String minStringVal = null;
    String rStringVal = null;
    try {
      rsSch.createTab("Longvarchar_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Lvarchar_In_Name(?)}");

      msg.setMsg("to extract the Minimum Value of int to be Updated");
      minStringVal = rsSch.extractVal("Longvarchar_Tab", 1, sqlp, conn);
      minStringVal = minStringVal.substring(1, minStringVal.length() - 1);
      msg.setMsg("String Value :" + minStringVal);

      cstmt.setString(1, minStringVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      msg.setMsg("get the query string");
      String Max_Val_Query = sqlp.getProperty("Longvarchar_Query_Name", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      InputStream is = rs.getAsciiStream(1);
      System.out.println("AsciiStream Created");
      BufferedReader d = new BufferedReader(new InputStreamReader(is));
      System.out.println("BufferedReader Stream Created");
      rStringVal = d.readLine();

      msg.addOutputMsg(minStringVal, rStringVal);

      if (rStringVal.trim().equals(minStringVal.trim())) {
        msg.setMsg(
            "setString Method sets the designated parameter to a String value ");
      } else {
        msg.printTestError(
            "setString Method does not set the designated parameter to a String value ",
            "test setString is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setString Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setString Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Longvarchar_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetString06
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:674;
   * JDBC:JAVADOC:675; JDBC:JAVADOC:398; JDBC:JAVADOC:399; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using setString(int parameterIndex,String x),update the column
   * the maximum value of Longvarchar_Tab. Now execute a query to get the
   * maximum value and retrieve the result of the query using the getInt(int
   * columnIndex) method.Compare the returned value, with the maximum value
   * extracted from the tssql.stmt file. Both of them should be equal.
   */
  public void testSetString06() throws Fault {
    String maxStringVal = null;
    String rStringVal = null;
    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Lvarchar_In_Null(?)}");

      msg.setMsg("to extract the Maximum Value of int to be Updated");
      maxStringVal = rsSch.extractVal("Longvarchar_Tab", 1, sqlp, conn);
      maxStringVal = maxStringVal.substring(1, maxStringVal.length() - 1);
      msg.setMsg("String Value :" + maxStringVal);

      cstmt.setString(1, maxStringVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      msg.setMsg("get the query string");
      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      InputStream is = rs.getAsciiStream(1);
      System.out.println("AsciiStream Created");
      BufferedReader d = new BufferedReader(new InputStreamReader(is));
      System.out.println("BufferedReader Stream Created");
      rStringVal = d.readLine();

      msg.addOutputMsg(maxStringVal, rStringVal);

      if (rStringVal.trim().equals(maxStringVal.trim())) {
        msg.setMsg(
            "setString Method sets the designated parameter to a String value ");
      } else {
        msg.printTestError(
            "setString Method does not set the designated parameter to a String value ",
            "test setString is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setString Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setString Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Longvarcharnull_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetBigDecimal01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:672;
   * JDBC:JAVADOC:673; JDBC:JAVADOC:454; JDBC:JAVADOC:455; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database Using the IN parameter of that
   * object,,update the column Max_Val of Decimal_Tab with the minimum
   * value.Execute a query to get the minimum value and retrieve the result of
   * the query using the getBigDecimal(int parameterIndex) method.Compare the
   * returned value with the minimum value extracted from the tssql.stmt file.
   * Both of them should be equal.
   */
  public void testSetBigDecimal01() throws Fault {
    BigDecimal oRetVal = null;
    BigDecimal oExtVal = null;
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_In_Max(?)}");

      msg.setMsg("to extract the Minimum Value to be Updated");
      String sminStringVal = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);
      oExtVal = new BigDecimal(sminStringVal);
      msg.setMsg("Minimum decimal Value to be updated :" + oExtVal);

      cstmt.setBigDecimal(1, oExtVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");
      String Max_Val_Query = sqlp.getProperty("Decimal_Query_Max", "");
      msg.setMsg("Query String :" + Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      oRetVal = rs.getBigDecimal(1);
      msg.addOutputMsg("" + oExtVal, "" + oRetVal);

      if ((oRetVal.compareTo(oExtVal) == 0)) {
        msg.setMsg("setBigDecimal set the Minimum value " + oRetVal);
      } else {
        msg.printTestError("setBigDecimal did not set the Minimum value",
            "test setBigDecimal is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setBigDecimal Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setBigDecimal Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Decimal_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetBigDecimal02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:672;
   * JDBC:JAVADOC:673; JDBC:JAVADOC:454; JDBC:JAVADOC:455; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of Decimal_Tab with the maximum
   * value.Execute a query to get the maximum value and retrieve the result of
   * the query using the getBigDecimal(int parameterIndex) method.Compare the
   * returned value with the maximum value extracted from the tssql.stmt file.
   * Both of them should be equal.
   */
  public void testSetBigDecimal02() throws Fault {
    BigDecimal oRetVal = null;
    BigDecimal oExtVal = null;
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_In_Null(?)}");

      msg.setMsg("to extract the Minimum Value to be Updated");
      String smaxStringVal = rsSch.extractVal("Decimal_Tab", 1, sqlp, conn);
      oExtVal = new BigDecimal(smaxStringVal);
      msg.setMsg("Maximum decimal Value to be updated :" + oExtVal);

      cstmt.setBigDecimal(1, oExtVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Decimal_Query_Null", "");
      msg.setMsg("Query String :" + Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      oRetVal = rs.getBigDecimal(1);

      msg.addOutputMsg("" + oExtVal, "" + oRetVal);

      if ((oRetVal.compareTo(oExtVal) == 0)) {
        msg.setMsg("setBigDecimal sets the Maximum value ");
      } else {
        msg.printTestError("setBigDecimal did not set the Maximum value",
            "test setBigDecimal is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setBigDecimal Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setBigDecimal Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Decimal_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetBigDecimal03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:672;
   * JDBC:JAVADOC:673; JDBC:JAVADOC:454; JDBC:JAVADOC:455; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database Using the IN parameter of that
   * object,,update the column Max_Val of Numeric_Tab with the minimum
   * value.Execute a query to get the minimum value and retrieve the result of
   * the query using the getBigDecimal(int parameterIndex) method.Compare the
   * returned value with the minimum value extracted from the tssql.stmt file.
   * Both of them should be equal.
   */
  public void testSetBigDecimal03() throws Fault {
    BigDecimal oRetVal = null;
    BigDecimal oExtVal = null;
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Numeric_In_Max(?)}");

      msg.setMsg("to extract the Minimum Value to be Updated");
      String sminStringVal = rsSch.extractVal("Numeric_Tab", 2, sqlp, conn);
      oExtVal = new BigDecimal(sminStringVal);
      msg.setMsg("Minimum Numeric Value to be updated :" + oExtVal);

      cstmt.setBigDecimal(1, oExtVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");
      String Max_Val_Query = sqlp.getProperty("Numeric_Query_Max", "");
      msg.setMsg("Query String :" + Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      oRetVal = rs.getBigDecimal(1);

      msg.addOutputMsg("" + oExtVal, "" + oRetVal);

      if ((oRetVal.compareTo(oExtVal) == 0)) {
        msg.setMsg("setBigDecimal set the Minimum value " + oRetVal);
      } else {
        msg.printTestError("setBigDecimal did not set the Minimum value",
            "test setBigDecimal is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setBigDecimal Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setBigDecimal Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Numeric_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetBigDecimal04
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:672;
   * JDBC:JAVADOC:673; JDBC:JAVADOC:454; JDBC:JAVADOC:455; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of Numeric_Tab with the maximum
   * value.Execute a query to get the maximum value and retrieve the result of
   * the query using the getBigDecimal(int parameterIndex) method.Compare the
   * returned value with the maximum value extracted from the tssql.stmt file.
   * Both of them should be equal.
   */
  public void testSetBigDecimal04() throws Fault {
    BigDecimal oRetVal = null;
    BigDecimal oExtVal = null;
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Numeric_In_Null(?)}");

      msg.setMsg("to extract the Minimum Value to be Updated");
      String smaxStringVal = rsSch.extractVal("Numeric_Tab", 1, sqlp, conn);
      oExtVal = new BigDecimal(smaxStringVal);
      msg.setMsg("Maximum decimal Value to be updated :" + oExtVal);

      cstmt.setBigDecimal(1, oExtVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Numeric_Query_Null", "");
      msg.setMsg("Query String :" + Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      oRetVal = rs.getBigDecimal(1);

      msg.addOutputMsg("" + oExtVal, "" + oRetVal);
      if ((oRetVal.compareTo(oExtVal) == 0)) {
        msg.setMsg("setBigDecimal set the Maximum value " + oRetVal);
      } else {
        msg.printTestError("setBigDecimal did not set the Maximum value",
            "test setBigDecimal is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setBigDecimal Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setBigDecimal Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Numeric_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetBoolean01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:658;
   * JDBC:JAVADOC:659; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database Using the IN parameter of that
   * object,update the column Max_Val of Bit_Tab with the minimum value.Execute
   * a query to get the minimum value and retrieve the result of the query using
   * the getBoolean(int parameterIndex) method.Compare the returned value with
   * the minimum value extracted from the tssql.stmt file.Both of them should be
   * equal.
   */
  public void testSetBoolean01() throws Fault {
    Boolean oRetVal = null;
    Boolean oExtVal = null;
    try {
      rsSch.createTab("Bit_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bit_In_Max(?)}");

      msg.setMsg("to extract the Minimum Value to be Updated");
      oExtVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp, conn);
      msg.setMsg("Minimum Boolean Value to be updated :" + oExtVal);

      cstmt.setBoolean(1, oExtVal.booleanValue());
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");
      String Max_Val_Query = sqlp.getProperty("Bit_Query_Max", "");
      msg.setMsg("Query String :" + Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      boolean bRetVal = rs.getBoolean(1);

      oRetVal = new Boolean(bRetVal);
      msg.addOutputMsg("" + oExtVal, "" + oRetVal);

      if (oRetVal.equals(oExtVal)) {
        msg.setMsg("setBoolean set the Minimum value " + oRetVal);
      } else {
        msg.printTestError("setBoolean did not set the Minimum value",
            "test setBoolean is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setBoolean Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setBoolean Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Bit_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetBoolean02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:658;
   * JDBC:JAVADOC:659; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database Using the IN parameter of that
   * object,update the column Null_Val of Bit_Tab with the maximum value.Execute
   * a query to get the maximum value and retrieve the result of the query using
   * the getBoolean(int parameterIndex) method.Compare the returned value with
   * the maximum value extracted from the with tssql.stmt file. Both of them
   * should be equal.
   */
  public void testSetBoolean02() throws Fault {
    Boolean oRetVal = null;
    Boolean oExtVal = null;
    try {
      rsSch.createTab("Bit_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bit_In_Null(?)}");

      msg.setMsg("to extract the Minimum Value to be Updated");
      oExtVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp, conn);

      msg.setMsg("Maximum Boolean Value to be updated :" + oExtVal);

      cstmt.setBoolean(1, oExtVal.booleanValue());

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Bit_Query_Null", "");
      msg.setMsg("Query String :" + Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      boolean bRetVal = rs.getBoolean(1);

      oRetVal = new Boolean(bRetVal);
      msg.addOutputMsg("" + oExtVal, "" + oRetVal);

      if (oRetVal.equals(oExtVal)) {
        msg.setMsg("setBoolean set the Maximum value " + oRetVal);
      } else {
        msg.printTestError("setBoolean did not set the Maximum value",
            "test setBoolean is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setBoolean Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setBoolean Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Bit_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetByte01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:660;
   * JDBC:JAVADOC:661; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database Using the IN parameter of that
   * object,update the column Max_Val of Tinyint_Tab with the minimum
   * value.Execute a query to get the minimum value and retrieve the result of
   * the query using the getByte(int parameterIndex) method.Compare the returned
   * value with the minimum value extracted from the tssql.stmt file.Both of
   * them should be equal.
   */
  public void testSetByte01() throws Fault {
    Byte oRetVal = null;
    Byte oExtVal = null;
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_In_Max(?)}");

      msg.setMsg("to extract the Minimum Value to be Updated");
      String sminStringVal = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);
      oExtVal = new Byte(sminStringVal);
      msg.setMsg("Minimum Byte Value to be updated :" + oExtVal);
      byte bExtVal = oExtVal.byteValue();

      cstmt.setByte(1, bExtVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");
      String Max_Val_Query = sqlp.getProperty("Tinyint_Query_Max", "");
      msg.setMsg("Query String :" + Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      byte bRetVal = rs.getByte(1);

      oRetVal = new Byte(bRetVal);
      msg.addOutputMsg("" + oExtVal, "" + oRetVal);

      if (oRetVal.equals(oExtVal)) {
        msg.setMsg("setByte set the Minimum value " + oRetVal);
      } else {
        msg.printTestError("setByte did not set the Minimum value",
            "test setByte is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setByte Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setByte Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetByte02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:660;
   * JDBC:JAVADOC:661; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database Using the IN parameter of that
   * object,update the column Max_Val of Tinyint_Tab with the minimum
   * value.Execute a query to get the minimum value and retrieve the result of
   * the query using the getByte(int parameterIndex) method.Compare the returned
   * value with the minimum value extracted from the tssql.stmt file. Both of
   * them should be equal.
   */
  public void testSetByte02() throws Fault {
    Byte oRetVal = null;
    Byte oExtVal = null;
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_In_Null(?)}");

      msg.setMsg("to extract the Minimum Value to be Updated");
      String smaxStringVal = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);
      oExtVal = new Byte(smaxStringVal);
      msg.setMsg("Maximum Byte Value to be updated :" + oExtVal);

      byte bExtVal = oExtVal.byteValue();
      cstmt.setByte(1, bExtVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Tinyint_Query_Null", "");
      msg.setMsg("Query String :" + Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      byte bRetVal = rs.getByte(1);

      oRetVal = new Byte(bRetVal);
      msg.addOutputMsg("" + oExtVal, "" + oRetVal);
      if (oRetVal.equals(oExtVal)) {
        msg.setMsg("setByte set the Maximum value " + oRetVal);
      } else {
        msg.printTestError("setByte did not set the Maximum value",
            "test setByte is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setByte Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setByte Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetShort01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:662;
   * JDBC:JAVADOC:663; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using setShort(int parameterIndex,short x),update the column the
   * minimum value of Smallint_Tab. Now execute a query to get the minimum value
   * and retrieve the result of the query using the getShort(int columnIndex)
   * method.Compare the returned value, with the minimum value extracted from
   * the tssql.stmt file. Both of them should be equal.
   */
  public void testSetShort01() throws Fault {
    short minShortVal;
    short rShortVal;
    String minStringVal = null;
    String sminStringVal = null;
    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_In_Max(?)}");

      msg.setMsg("to extract the Minimum Value of Smallint to be Updated");
      sminStringVal = rsSch.extractVal("Smallint_Tab", 2, sqlp, conn);
      minStringVal = new String(sminStringVal);
      minShortVal = Short.parseShort(sminStringVal);
      msg.setMsg("Minimum Smallint Value to be updated :" + minShortVal);

      cstmt.setShort(1, minShortVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      msg.setMsg("get the query string");
      String Max_Val_Query = sqlp.getProperty("Smallint_Query_Max", "");
      msg.setMsg("Msg(Max_Val_Query);");
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      rShortVal = rs.getShort(1);

      msg.addOutputMsg("" + minShortVal, "" + rShortVal);
      if (rShortVal == minShortVal) {
        msg.setMsg(
            "setShort Method sets the designated parameter to a Short value ");
      } else {
        msg.printTestError(
            "setShort Method does not set the designated parameter to a Short value ",
            "test setShort is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setShort Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setShort Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetShort02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:662;
   * JDBC:JAVADOC:663; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using setShort(int parameterIndex,short x),update the column the
   * maximum value of Smallint_Tab. Now execute a query to get the maximum value
   * and retrieve the result of the query using the getShort(int columnIndex)
   * method.Compare the returned value, with the maximum value extracted from
   * the tssql.stmt file. Both of them should be equal.
   */
  public void testSetShort02() throws Fault {
    short maxShortVal;
    short rShortVal;
    String maxStringVal = null;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_In_Null(?)}");

      msg.setMsg("to extract the Maximum Value of Smallint to be Updated");
      smaxStringVal = rsSch.extractVal("Smallint_Tab", 1, sqlp, conn);
      maxStringVal = new String(smaxStringVal);
      maxShortVal = Short.parseShort(smaxStringVal);
      msg.setMsg("Maximum Smallint Value to be updated :" + maxShortVal);

      cstmt.setShort(1, maxShortVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      msg.setMsg("get the query string");
      String Null_Val_Query = sqlp.getProperty("Smallint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rShortVal = rs.getShort(1);
      msg.addOutputMsg("" + maxShortVal, "" + rShortVal);

      if (rShortVal == maxShortVal) {
        msg.setMsg(
            "setShort Method sets the designated parameter to a Short value ");
      } else {
        msg.printTestError(
            "setShort Method does not set the designated parameter to a Short value ",
            "test setShort is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setShort Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setShort Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetInt01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:664;
   * JDBC:JAVADOC:665; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using setInt(int parameterIndex,int x),update the column the
   * minimum value of Integer_Tab. Now execute a query to get the minimum value
   * and retrieve the result of the query using the getInt(int columnIndex)
   * method.Compare the returned value, with the minimum value extracted from
   * the tssql.stmt file. Both of them should be equal.
   */
  public void testSetInt01() throws Fault {
    int minIntegerVal;
    int rIntegerVal;
    String minStringVal = null;
    String sminStringVal = null;
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_In_Max(?)}");

      msg.setMsg("to extract the Minimum Value of int to be Updated");
      sminStringVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);
      minStringVal = new String(sminStringVal);
      minIntegerVal = Integer.parseInt(sminStringVal);
      msg.setMsg("Minimum int Value to be updated :" + minIntegerVal);

      cstmt.setInt(1, minIntegerVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      msg.setMsg("get the query string");
      String Max_Val_Query = sqlp.getProperty("Integer_Query_Max", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      rIntegerVal = rs.getInt(1);

      msg.addOutputMsg("" + minIntegerVal, "" + rIntegerVal);
      if (rIntegerVal == minIntegerVal) {
        msg.setMsg(
            "setInt Method sets the designated parameter to a int value ");
      } else {
        msg.printTestError(
            "setInt Method does not set the designated parameter to a int value ",
            "test setInt is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setInt Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setInt Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetInt02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:664;
   * JDBC:JAVADOC:665; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using setInt(int parameterIndex,int x),update the column the
   * maximum value of Integer_Tab. Now execute a query to get the maximum value
   * and retrieve the result of the query using the getInt(int columnIndex)
   * method.Compare the returned value, with the maximum value extracted from
   * the tssql.stmt file. Both of them should be equal.
   */
  public void testSetInt02() throws Fault {
    int maxIntegerVal;
    String maxStringVal = null;
    int rIntegerVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_In_Null(?)}");

      msg.setMsg("to extract the Maximum Value of int to be Updated");
      smaxStringVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);
      maxStringVal = new String(smaxStringVal);
      maxIntegerVal = Integer.parseInt(smaxStringVal);
      msg.setMsg("Maximum int Value to be updated :" + maxIntegerVal);

      cstmt.setInt(1, maxIntegerVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      msg.setMsg("get the query string");
      String Null_Val_Query = sqlp.getProperty("Integer_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rIntegerVal = rs.getInt(1);
      msg.addOutputMsg("" + maxIntegerVal, "" + rIntegerVal);
      if (rIntegerVal == maxIntegerVal) {
        msg.setMsg(
            "setInt Method sets the designated parameter to a int value ");
      } else {
        msg.printTestError(
            "setInt Method does not set the designated parameter to a int value ",
            "test setByte is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setInt Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setInt Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetLong01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:666;
   * JDBC:JAVADOC:667; JDBC:JAVADOC:382; JDBC:JAVADOC:383; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using setLong(int parameterIndex,long x),update the column the
   * minimum value of Bigint_Tab. Now execute a query to get the minimum value
   * and retrieve the result of the query using the getInt(int columnIndex)
   * method.Compare the returned value, with the minimum value extracted from
   * the tssql.stmt file. Both of them should be equal.
   */
  public void testSetLong01() throws Fault {
    long minLongVal;
    long rLongVal;
    String minStringVal = null;
    String sminStringVal = null;
    try {
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_In_Max(?)}");

      msg.setMsg("to extract the Minimum Value of int to be Updated");
      sminStringVal = rsSch.extractVal("Bigint_Tab", 2, sqlp, conn);
      minStringVal = new String(sminStringVal);
      minLongVal = Long.parseLong(sminStringVal);
      msg.setMsg("Minimum long Value to be updated :" + minLongVal);

      cstmt.setLong(1, minLongVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      msg.setMsg("get the query string");
      String Max_Val_Query = sqlp.getProperty("Bigint_Query_Max", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      rLongVal = rs.getLong(1);

      msg.addOutputMsg("" + minLongVal, "" + rLongVal);

      if (rLongVal == minLongVal) {
        msg.setMsg(
            "setLong Method sets the designated parameter to a long value ");
      } else {
        msg.printTestError(
            "setLong Method does not set the designated parameter to a long value ",
            "test setLong is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setLong Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setLong Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Bigint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetLong02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:666;
   * JDBC:JAVADOC:667; JDBC:JAVADOC:382; JDBC:JAVADOC:383; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using setLong(int parameterIndex,long x),update the column the
   * maximum value of Bigint_Tab. Now execute a query to get the maximum value
   * and retrieve the result of the query using the getInt(int columnIndex)
   * method.Compare the returned value, with the maximum value extracted from
   * the tssql.stmt file. Both of them should be equal.
   */
  public void testSetLong02() throws Fault {
    long maxLongVal;
    long rLongVal;
    String maxStringVal = null;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_In_Null(?)}");

      msg.setMsg("to extract the Maximum Value of int to be Updated");
      smaxStringVal = rsSch.extractVal("Bigint_Tab", 1, sqlp, conn);
      maxStringVal = new String(smaxStringVal);
      maxLongVal = Long.parseLong(smaxStringVal);
      msg.setMsg("Maximum long Value to be updated :" + maxLongVal);

      cstmt.setLong(1, maxLongVal);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      msg.setMsg("get the query string");
      String Null_Val_Query = sqlp.getProperty("Bigint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rLongVal = rs.getLong(1);

      msg.addOutputMsg("" + maxLongVal, "" + rLongVal);
      if (rLongVal == maxLongVal) {
        msg.setMsg(
            "setLong Method sets the designated parameter to a long value ");
      } else {
        msg.printTestError(
            "setLong Method does not set the designated parameter to a long value ",
            "test setLong is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setLong Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setLong Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Bigint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      // Close the database
      rsSch.dbUnConnect(conn);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }
}
