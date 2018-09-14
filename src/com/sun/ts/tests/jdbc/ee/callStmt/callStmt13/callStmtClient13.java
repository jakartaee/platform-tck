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
 * %W% %E%
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt13;

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
 * The callStmtClient13 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class callStmtClient13 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt13";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private ResultSet rs = null;

  private csSchema csSch = null;

  private rsSchema rsSch = null;

  private String drManager = null;

  private Properties sqlp = null;

  private transient DatabaseMetaData dbmd = null;

  private Statement stmt = null;

  private CallableStatement cstmt = null;

  private Properties props = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    callStmtClient13 theTests = new callStmtClient13();
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
        stmt = conn.createStatement();
        rsSch = new rsSchema();
        csSch = new csSchema();
        msg = new JDBCTestMsg();
      } catch (SQLException ex) {
        logErr("SQL Exception : " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testSetObject101
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Numeric_Tab with the maximum value
   * of the Bit_Tab. Execute a query to retrieve the Null_Val from Numeric_Tab.
   * Compare the returned value with the maximum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject101() throws Fault {
    Boolean oMaxNumericVal = null;
    BigDecimal rNumericVal = null;
    String smaxNumericVal = null;
    BigDecimal maxNumericVal = null;
    try {

      rsSch.createTab("Numeric_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Numeric_In_Null(?)}");

      msg.setMsg(
          "extract the Maximum Value of Boolean to be Updated in Numeric Table");
      smaxNumericVal = rsSch.extractValAsNumericString("Bit_Tab", 1, sqlp,
          conn);
      maxNumericVal = new BigDecimal(smaxNumericVal);
      oMaxNumericVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp, conn);

      cstmt.setObject(1, oMaxNumericVal, java.sql.Types.NUMERIC, 2);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Numeric_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rNumericVal = (BigDecimal) rs.getObject(1);

      msg.addOutputMsg("" + maxNumericVal, "" + rNumericVal);

      if ((rNumericVal.compareTo(maxNumericVal)) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
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
   * @testName: testSetObject102
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Numeric_Tab with the minimum value
   * of the Bit_Tab. Execute a query to retrieve the Null_Val from Numeric_Tab.
   * Compare the returned value with the minimum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject102() throws Fault {
    Boolean oMinNumericVal = null;
    BigDecimal rNumericVal = null;
    String sminNumericVal = null;
    BigDecimal minNumericVal = null;
    try {

      rsSch.createTab("Numeric_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Numeric_In_Max(?)}");

      msg.setMsg(
          "extract the Minimum Value of Boolean to be Updated in Numeric Table");

      sminNumericVal = rsSch.extractValAsNumericString("Bit_Tab", 2, sqlp,
          conn);
      minNumericVal = new BigDecimal(sminNumericVal);
      oMinNumericVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp, conn);

      cstmt.setObject(1, oMinNumericVal, java.sql.Types.NUMERIC, 2);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Numeric_Query_Max", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rNumericVal = (BigDecimal) rs.getObject(1);
      msg.addOutputMsg("" + minNumericVal, "" + rNumericVal);

      if ((rNumericVal.compareTo(minNumericVal)) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
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
   * @testName: testSetObject105
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Char_Tab with the maximum value of
   * the Bit_Tab. Execute a query to retrieve the Null_Val from Char_Tab.
   * Compare the returned value with the maximum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject105() throws Fault {
    Boolean oMaxBooleanVal = null;
    String smaxBooleanVal = null;
    String rStringVal = null;
    try {

      rsSch.createTab("Char_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_In_Null(?)}");

      msg.setMsg(
          "extract the Maximum Value of Boolean to be Updated in Char Table");
      smaxBooleanVal = rsSch.extractValAsNumericString("Bit_Tab", 1, sqlp,
          conn);
      oMaxBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp, conn);

      cstmt.setObject(1, oMaxBooleanVal, java.sql.Types.CHAR);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);
      rStringVal = rStringVal.trim();

      // Tests will pass for three possibilities.
      // 1. A driver supports boolean and saves boolean as "true" in char_tab
      // 2. A driver doesn't support boolean , saves boolean as "1" in char_tab
      // 3. A driver doesn't support boolean , saves boolean as "true" in
      // char_tab

      if (rStringVal.equals(oMaxBooleanVal.toString())
          || rStringVal.equals(smaxBooleanVal.trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Char_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject106
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Char_Tab with the minimum value of
   * the Bit_Tab. Execute a query to retrieve the Null_Val from Char_Tab.
   * Compare the returned value with the minimum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject106() throws Fault {
    Boolean oMinBooleanVal = null;
    String sminBooleanVal = null;
    String rStringVal = null;
    try {

      rsSch.createTab("Char_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_In_Null(?)}");

      msg.setMsg(
          "extract the Minimum Value of Boolean to be Updated in Char Table");
      sminBooleanVal = rsSch.extractValAsNumericString("Bit_Tab", 2, sqlp,
          conn);
      oMinBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp, conn);

      cstmt.setObject(1, oMinBooleanVal, java.sql.Types.CHAR);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);
      rStringVal = rStringVal.trim();

      // Tests will pass for three possibilities.
      // 1. A driver supports boolean and saves boolean as "true" in char_tab
      // 2. A driver doesn't support boolean , saves boolean as "1" in char_tab
      // 3. A driver doesn't support boolean , saves boolean as "true" in
      // char_tab

      if (rStringVal.equals(oMinBooleanVal.toString())
          || rStringVal.equals(sminBooleanVal.trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Char_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject107
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Varchar_Tab with the maximum value
   * of the Bit_Tab. Execute a query to retrieve the Null_Val from Varchar_Tab.
   * Compare the returned value with the maximum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject107() throws Fault {
    Boolean oMaxBooleanVal = null;
    String smaxBooleanVal = null;
    String rStringVal = null;
    try {

      rsSch.createTab("Varchar_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_In_Null(?)}");

      msg.setMsg(
          "extract the Maximum Value of Boolean to be Updated in Varchar Table");
      smaxBooleanVal = rsSch.extractValAsNumericString("Bit_Tab", 1, sqlp,
          conn);
      oMaxBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp, conn);

      cstmt.setObject(1, oMaxBooleanVal, java.sql.Types.VARCHAR);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);
      rStringVal = rStringVal.trim();

      // Tests will pass for three possibilities.
      // 1. A driver supports boolean and saves boolean as "true" in char_tab
      // 2. A driver doesn't support boolean , saves boolean as "1" in char_tab
      // 3. A driver doesn't support boolean , saves boolean as "true" in
      // char_tab

      if (rStringVal.equals(oMaxBooleanVal.toString())
          || rStringVal.equals(smaxBooleanVal.trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Varchar_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject108
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Varchar_Tab with the minimum value
   * of the Bit_Tab. Execute a query to retrieve the Null_Val from Varchar_Tab.
   * Compare the returned value with the minimum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject108() throws Fault {
    Boolean oMinBooleanVal = null;
    String sminBooleanVal = null;
    String rStringVal = null;
    try {

      rsSch.createTab("Varchar_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_In_Null(?)}");

      msg.setMsg(
          "extract the Minimum Value of Boolean to be Updated in Varchar Table");
      sminBooleanVal = rsSch.extractValAsNumericString("Bit_Tab", 2, sqlp,
          conn);
      oMinBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp, conn);

      cstmt.setObject(1, oMinBooleanVal, java.sql.Types.VARCHAR);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);
      rStringVal = rStringVal.trim();

      // Tests will pass for three possibilities.
      // 1. A driver supports boolean and saves boolean as "true" in char_tab
      // 2. A driver doesn't support boolean , saves boolean as "1" in char_tab
      // 3. A driver doesn't support boolean , saves boolean as "true" in
      // char_tab

      if (rStringVal.equals(oMinBooleanVal.toString())
          || rStringVal.equals(sminBooleanVal.trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Varchar_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject109
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Longvarchar_Tab with the maximum
   * value of the Bit_Tab. Execute a query to retrieve the Null_Val from
   * Longvarchar_Tab. Compare the returned value with the maximum value
   * extracted from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject109() throws Fault {
    Boolean oMaxBooleanVal = null;
    String smaxBooleanVal = null;
    String rStringVal = null;
    try {

      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Lvarchar_In_Null(?)}");

      msg.setMsg(
          "extract the Maximum Value of Boolean to be Updated in Longvarchar Table");
      smaxBooleanVal = rsSch.extractValAsNumericString("Bit_Tab", 1, sqlp,
          conn);
      oMaxBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp, conn);

      cstmt.setObject(1, oMaxBooleanVal, java.sql.Types.LONGVARCHAR);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);
      rStringVal = rStringVal.trim();

      // Tests will pass for three possibilities.
      // 1. A driver supports boolean and saves boolean as "true" in char_tab
      // 2. A driver doesn't support boolean , saves boolean as "1" in char_tab
      // 3. A driver doesn't support boolean , saves boolean as "true" in
      // char_tab

      if (rStringVal.equals(oMaxBooleanVal.toString())
          || rStringVal.equals(smaxBooleanVal.trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Longvarcharnull_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject110
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Longvarchar_Tab with the minimum
   * value of the Bit_Tab. Execute a query to retrieve the Null_Val from
   * Longvarchar_Tab. Compare the returned value with the minimum value
   * extracted from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject110() throws Fault {
    Boolean oMinBooleanVal = null;
    String sminBooleanVal = null;
    String rStringVal = null;
    try {

      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Lvarchar_In_Null(?)}");

      msg.setMsg(
          "extract the Minimum Value of Boolean to be Updated in Longvarchar Table");
      sminBooleanVal = rsSch.extractValAsNumericString("Bit_Tab", 2, sqlp,
          conn);
      oMinBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp, conn);

      cstmt.setObject(1, oMinBooleanVal, java.sql.Types.LONGVARCHAR);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);
      rStringVal = rStringVal.trim();

      // Tests will pass for three possibilities.
      // 1. A driver supports boolean and saves boolean as "true" in char_tab
      // 2. A driver doesn't support boolean , saves boolean as "1" in char_tab
      // 3. A driver doesn't support boolean , saves boolean as "true" in
      // char_tab

      if (rStringVal.equals(oMinBooleanVal.toString())
          || rStringVal.equals(sminBooleanVal.trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Longvarcharnull_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject111
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Tinyint_Tab with the maximum value
   * of the Tinyint_Tab. Execute a query to retrieve the Min_Val from
   * Tinyint_Tab. Compare the returned value with the maximum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject111() throws Fault {
    Integer maxIntegerVal = null;
    Integer rTinyintVal = null;
    String smaxStringVal = null;
    try {

      rsSch.createTab("Tinyint_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_In_Min(?)}");

      msg.setMsg("extract the Maximum Value of Tinyint to be Updated");
      smaxStringVal = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);
      maxIntegerVal = new Integer(smaxStringVal);
      cstmt.setObject(1, maxIntegerVal, java.sql.Types.TINYINT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Tinyint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object oTinyintVal = rs.getObject(1);
      rTinyintVal = new Integer(oTinyintVal.toString());

      msg.addOutputMsg("" + maxIntegerVal, "" + rTinyintVal);
      if (rTinyintVal.compareTo(maxIntegerVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
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
   * @testName: testSetObject112
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Tinyint_Tab with the minimum value
   * of the Tinyint_Tab. Execute a query to retrieve the Null_Val from
   * Tinyint_Tab. Compare the returned value with the minimum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject112() throws Fault {
    Integer minIntegerVal = null;
    Integer rTinyintVal = null;
    String sminStringVal = null;
    try {

      rsSch.createTab("Tinyint_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_In_Null(?)}");

      msg.setMsg("extract the Minimum Value of Tinyint to be Updated");
      sminStringVal = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);

      minIntegerVal = new Integer(sminStringVal);
      cstmt.setObject(1, minIntegerVal, java.sql.Types.TINYINT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Tinyint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object oTinyintVal = rs.getObject(1);
      rTinyintVal = new Integer(oTinyintVal.toString());

      msg.addOutputMsg("" + minIntegerVal, "" + rTinyintVal);
      if (rTinyintVal.compareTo(minIntegerVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
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
   * @testName: testSetObject113
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Smallint_Tab with the maximum value
   * of the Smallint_Tab. Execute a query to retrieve the Min_Val from
   * Smallint_Tab. Compare the returned value with the maximum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject113() throws Fault {
    Integer maxIntegerVal = null;
    Integer rSmallintVal = null;
    String smaxStringVal = null;
    try {

      rsSch.createTab("Smallint_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_In_Min(?)}");

      msg.setMsg("extract the Maximum Value of Smallint to be Updated");
      smaxStringVal = rsSch.extractVal("Smallint_Tab", 1, sqlp, conn);

      maxIntegerVal = new Integer(smaxStringVal);
      cstmt.setObject(1, maxIntegerVal, java.sql.Types.SMALLINT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Smallint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object oSmallintVal = rs.getObject(1);
      rSmallintVal = new Integer(oSmallintVal.toString());

      msg.addOutputMsg("" + maxIntegerVal, "" + rSmallintVal);
      if (rSmallintVal.compareTo(maxIntegerVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject114
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Smallint_Tab with the minimum
   * value of the Smallint_Tab. Execute a query to retrieve the Null_Val from
   * Smallint_Tab. Compare the returned value with the minimum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject114() throws Fault {
    Integer minIntegerVal = null;
    Integer rSmallintVal = null;
    String sminStringVal = null;
    try {

      rsSch.createTab("Smallint_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_In_Null(?)}");

      msg.setMsg("extract the Minimum Value of Smallint to be Updated");
      sminStringVal = rsSch.extractVal("Smallint_Tab", 2, sqlp, conn);

      minIntegerVal = new Integer(sminStringVal);
      cstmt.setObject(1, minIntegerVal, java.sql.Types.SMALLINT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Smallint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object oSmallintVal = rs.getObject(1);
      rSmallintVal = new Integer(oSmallintVal.toString());

      msg.addOutputMsg("" + minIntegerVal, "" + rSmallintVal);
      if (rSmallintVal.compareTo(minIntegerVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject115
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Integer_Tab with the maximum value
   * of the Integer_Tab. Execute a query to retrieve the Min_Val from
   * Integer_Tab. Compare the returned value with the maximum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject115() throws Fault {
    Integer maxIntegerVal = null;
    Integer rIntegerVal = null;
    String smaxStringVal = null;
    try {

      rsSch.createTab("Integer_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_In_Min(?)}");

      msg.setMsg("extract the Maximum Value of Integer to be Updated");
      smaxStringVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);
      maxIntegerVal = new Integer(smaxStringVal);
      cstmt.setObject(1, maxIntegerVal, java.sql.Types.INTEGER);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Integer_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object oIntegerVal = rs.getObject(1);
      rIntegerVal = new Integer(oIntegerVal.toString());

      msg.addOutputMsg("" + maxIntegerVal, "" + rIntegerVal);
      if (rIntegerVal.compareTo(maxIntegerVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject116
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Integer_Tab with the minimum value
   * of the Integer_Tab. Execute a query to retrieve the Null_Val from
   * Integer_Tab. Compare the returned value with the minimum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject116() throws Fault {
    Integer minIntegerVal = null;
    Integer rIntegerVal = null;
    String sminStringVal = null;
    try {

      rsSch.createTab("Integer_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_In_Null(?)}");

      msg.setMsg("extract the Minimum Value of Integer to be Updated");
      sminStringVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);
      minIntegerVal = new Integer(sminStringVal);
      cstmt.setObject(1, minIntegerVal, java.sql.Types.INTEGER);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Integer_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object oIntegerVal = rs.getObject(1);
      rIntegerVal = new Integer(oIntegerVal.toString());

      msg.addOutputMsg("" + minIntegerVal, "" + rIntegerVal);
      if (rIntegerVal.compareTo(minIntegerVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject117
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Bigint_Tab with the maximum value
   * of the Integer_Tab. Execute a query to retrieve the Min_Val from
   * Bigint_Tab. Compare the returned value with the maximum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject117() throws Fault {
    Long maxBigintVal = null;
    Integer maxIntegerVal = null;
    Long rBigintVal = null;
    String smaxStringVal = null;
    try {

      rsSch.createTab("Bigint_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_In_Min(?)}");

      msg.setMsg("extract the Maximum Value of Bigint to be Updated");
      smaxStringVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);
      maxBigintVal = new Long(smaxStringVal);
      maxIntegerVal = new Integer(smaxStringVal);
      cstmt.setObject(1, maxIntegerVal, java.sql.Types.BIGINT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Bigint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object oBigintVal = rs.getObject(1);
      rBigintVal = new Long(oBigintVal.toString());

      msg.addOutputMsg("" + maxBigintVal, "" + rBigintVal);
      if (rBigintVal.compareTo(maxBigintVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Bigint_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject118
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Bigint_Tab with the minimum value
   * of the Integer_Tab. Execute a query to retrieve the Null_Val from
   * Bigint_Tab. Compare the returned value with the minimum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject118() throws Fault {
    Long minBigintVal = null;
    Integer minIntegerVal = null;
    Long rBigintVal = null;
    String sminStringVal = null;
    try {

      rsSch.createTab("Bigint_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_In_Null(?)}");

      msg.setMsg("extract the Minimum Value of Bigint to be Updated");
      sminStringVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);
      minIntegerVal = new Integer(sminStringVal);
      minBigintVal = new Long(sminStringVal);

      cstmt.setObject(1, minIntegerVal, java.sql.Types.BIGINT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Bigint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object oBigintVal = rs.getObject(1);
      rBigintVal = new Long(oBigintVal.toString());

      msg.addOutputMsg("" + minBigintVal, "" + rBigintVal);
      if (rBigintVal.compareTo(minBigintVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Bigint_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject119
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Real_Tab with the maximum value of
   * the Integer_Tab. Execute a query to retrieve the Min_Val from Real_Tab.
   * Compare the returned value with the maximum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject119() throws Fault {
    Float maxRealVal = null;
    Integer maxIntegerVal = null;
    Float rRealVal = null;
    String smaxStringVal = null;
    try {

      rsSch.createTab("Real_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_In_Min(?)}");

      msg.setMsg("extract the Maximum Value of Float to be Updated");
      smaxStringVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);
      maxIntegerVal = new Integer(smaxStringVal);
      maxRealVal = new Float(smaxStringVal);
      cstmt.setObject(1, maxIntegerVal, java.sql.Types.REAL);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Real_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object oRealVal = rs.getObject(1);
      rRealVal = new Float(oRealVal.toString());
      msg.addOutputMsg("" + maxRealVal, "" + rRealVal);

      if (rRealVal.compareTo(maxRealVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject120
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Real_Tab with the minimum value of
   * the Integer_Tab. Execute a query to retrieve the Null_Val from Real_Tab.
   * Compare the returned value with the minimum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject120() throws Fault {
    Float minRealVal = null;
    Integer minIntegerVal = null;
    Float rRealVal = null;
    String sminStringVal = null;
    try {

      rsSch.createTab("Real_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_In_Null(?)}");

      msg.setMsg("extract the Minimum Value of Float to be Updated");
      sminStringVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);
      minIntegerVal = new Integer(sminStringVal);
      minRealVal = new Float(sminStringVal);
      msg.setMsg("Minimum Real Value :" + minRealVal);

      cstmt.setObject(1, minIntegerVal, java.sql.Types.REAL);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Real_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object oRealVal = rs.getObject(1);
      rRealVal = new Float(oRealVal.toString());

      msg.addOutputMsg("" + minRealVal, "" + rRealVal);
      if (rRealVal.compareTo(minRealVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject Method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {

      if (stmt != null) {
        stmt.close();
        stmt = null;
      }

      // Close the database
      rsSch.dbUnConnect(conn);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }
}
