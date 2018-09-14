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
 * @(#)callStmtClient12.java	1.18 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt12;

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
 * The callStmtClient12 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class callStmtClient12 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt12";

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
    callStmtClient12 theTests = new callStmtClient12();
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
   * @testName: testSetObject81
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Varchar_Tab with the maximum value
   * of the Decimal_Tab. Execute a query to retrieve the Null_Val from
   * Varchar_Tab. Compare the returned value with the maximum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject81() throws Fault {
    BigDecimal maxBigDecimalVal = null;
    String maxStringVal = null;
    String rStringVal = null;
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_In_Null(?)}");

      msg.setMsg("to extract the Value of String to be updated");
      maxStringVal = rsSch.extractVal("Decimal_Tab", 1, sqlp, conn);
      maxStringVal = maxStringVal.trim();
      maxBigDecimalVal = new BigDecimal(maxStringVal);

      cstmt.setObject(1, maxBigDecimalVal, java.sql.Types.VARCHAR);
      cstmt.executeUpdate();

      msg.setMsg(
          "to queryfrom the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getObject(1).toString();
      rStringVal = rStringVal.trim();
      msg.addOutputMsg(maxStringVal, rStringVal);

      if (rStringVal.equals(maxStringVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");
      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

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
   * @testName: testSetObject82
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Varchar_Tab with the minimum value
   * of the Decimal_Tab. Execute a query to retrieve the Null_Val from
   * Varchar_Tab. Compare the returned value with the minimum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject82() throws Fault {
    BigDecimal maxBigDecimalVal = null;
    BigDecimal rsBigDecimalVal = null;
    String maxStringVal = null;
    String rStringVal = null;
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_In_Null(?)}");

      msg.setMsg("to extract the Value of String to be Updated");
      maxStringVal = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);
      maxStringVal = maxStringVal.trim();
      maxBigDecimalVal = new BigDecimal(maxStringVal);

      cstmt.setObject(1, maxBigDecimalVal, java.sql.Types.VARCHAR);
      cstmt.executeUpdate();

      msg.setMsg(
          "to queryfrom the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getObject(1).toString().trim();
      rsBigDecimalVal = new BigDecimal(rStringVal);

      msg.addOutputMsg(maxStringVal, rsBigDecimalVal.toString());

      if (rsBigDecimalVal.compareTo(maxBigDecimalVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

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
   * @testName: testSetObject83
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Longvarchar_Tab with the maximum
   * value of the Decimal_Tab. Execute a query to retrieve the Null_Val from
   * Longvarchar_Tab. Compare the returned value with the maximum value
   * extracted from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject83() throws Fault {
    String maxStringVal = null;
    BigDecimal maxBigDecimalVal = null;
    String rStringVal = null;
    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Lvarchar_In_Null(?)}");

      msg.setMsg("to extract the Value of String to be Updated");
      maxStringVal = rsSch.extractVal("Decimal_Tab", 1, sqlp, conn);
      maxStringVal = maxStringVal.trim();
      maxBigDecimalVal = new BigDecimal(maxStringVal);

      cstmt.setObject(1, maxBigDecimalVal, java.sql.Types.LONGVARCHAR);
      cstmt.executeUpdate();

      msg.setMsg(
          "to queryfrom the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getObject(1).toString();
      rStringVal = rStringVal.trim();
      msg.addOutputMsg(maxStringVal, rStringVal);

      if (rStringVal.equals(maxStringVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

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
   * @testName: testSetObject84
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Longvarchar_Tab with the minimum
   * value of the Decimal_Tab. Execute a query to retrieve the Null_Val from
   * Longvarchar_Tab. Compare the returned value with the minimum value
   * extracted from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject84() throws Fault {
    String maxStringVal = null;
    BigDecimal maxBigDecimalVal = null;
    BigDecimal rsBigDecimalVal = null;

    String rStringVal = null;
    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Lvarchar_In_Null(?)}");

      msg.setMsg("to extract the Value of String to be updated");
      maxStringVal = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);
      maxStringVal = maxStringVal.trim();
      maxBigDecimalVal = new BigDecimal(maxStringVal);

      cstmt.setObject(1, maxBigDecimalVal, java.sql.Types.LONGVARCHAR);
      cstmt.executeUpdate();

      msg.setMsg(
          "to queryfrom the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getObject(1).toString().trim();
      rsBigDecimalVal = new BigDecimal(rStringVal);

      msg.addOutputMsg(maxStringVal, rsBigDecimalVal.toString());

      if (rsBigDecimalVal.compareTo(maxBigDecimalVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

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
   * @testName: testSetObject85
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Tinyint_Tab with the maximum value
   * of the Bit_Tab. Execute a query to retrieve the Min_Val from Tinyint_Tab.
   * Compare the returned value with the maximum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject85() throws Fault {
    Boolean oMaxTinyintVal = null;
    Integer rTinyintVal = null;
    String smaxTinyintVal = null;
    Integer maxTinyintVal = null;
    boolean booleanVal;
    try {

      rsSch.createTab("Tinyint_Tab", sqlp, conn);
      msg.setMsg("Calling prepareCall");
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_In_Min(?)}");

      msg.setMsg("to extract the Maximum Value of Tinyint to be updated");
      msg.setMsg("extracting value from tssql.stmt");

      smaxTinyintVal = rsSch.extractValAsNumericString("Bit_Tab", 1, sqlp,
          conn);
      oMaxTinyintVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp, conn);
      maxTinyintVal = Integer.valueOf(smaxTinyintVal);

      msg.setMsg("Calling setObject method :");
      cstmt.setObject(1, oMaxTinyintVal, java.sql.Types.TINYINT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to queryfrom the database to check the call of cstmt.executeUpdate");

      String Min_Val_Query = sqlp.getProperty("Tinyint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object oTinyintVal = rs.getObject(1);
      rTinyintVal = new Integer(oTinyintVal.toString());
      msg.addOutputMsg("" + maxTinyintVal, "" + rTinyintVal);

      if ((rTinyintVal.compareTo(maxTinyintVal)) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

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
   * @testName: testSetObject86
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Tinyint_Tab with the minimum value
   * of the Bit_Tab. Execute a query to retrieve the Null_Val from Tinyint_Tab.
   * Compare the returned value with the minimum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject86() throws Fault {
    Boolean oMinTinyintVal = null;
    Integer rTinyintVal = null;
    String sminTinyintVal = null;
    Integer minTinyintVal = null;
    boolean booleanVal;
    try {

      rsSch.createTab("Tinyint_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_In_Null(?)}");

      msg.setMsg("to extract the Minimum Value of Tinyint to be updated");
      sminTinyintVal = rsSch.extractValAsNumericString("Bit_Tab", 2, sqlp,
          conn);
      minTinyintVal = Integer.valueOf(sminTinyintVal);
      oMinTinyintVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp, conn);

      cstmt.setObject(1, oMinTinyintVal, java.sql.Types.TINYINT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to queryfrom the database to check the call of cstmt.executeUpdate");

      String Null_Val_Query = sqlp.getProperty("Tinyint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object oTinyintVal = rs.getObject(1);
      rTinyintVal = new Integer(oTinyintVal.toString());
      msg.addOutputMsg("" + minTinyintVal, "" + rTinyintVal);

      if (rTinyintVal.compareTo(minTinyintVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

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
   * @testName: testSetObject87
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Smallint_Tab with the maximum value
   * of the Bit_Tab. Execute a query to retrieve the Min_Val from Smallint_Tab.
   * Compare the returned value with the maximum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject87() throws Fault {
    Boolean oMaxSmallintVal = null;
    Integer rSmallintVal = null;
    String smaxSmallintVal = null;
    Integer maxSmallintVal = null;
    boolean booleanVal;
    try {

      // to create the Smallint Table
      rsSch.createTab("Smallint_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_In_Min(?)}");

      msg.setMsg(
          "to extract the Maximum Value of Boolean to be Updated in Smallint table");
      smaxSmallintVal = rsSch.extractValAsNumericString("Bit_Tab", 1, sqlp,
          conn);
      maxSmallintVal = Integer.valueOf(smaxSmallintVal);
      oMaxSmallintVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp, conn);

      cstmt.setObject(1, oMaxSmallintVal, java.sql.Types.SMALLINT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to queryfrom the database to check the call of cstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Smallint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object oSmallintVal = rs.getObject(1);
      rSmallintVal = Integer.valueOf(oSmallintVal.toString());
      msg.addOutputMsg("" + maxSmallintVal, "" + rSmallintVal);

      if (rSmallintVal.compareTo(maxSmallintVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

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
   * @testName: testSetObject88
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Smallint_Tab with the minimum
   * value of the Bit_Tab. Execute a query to retrieve the Null_Val from
   * Smallint_Tab. Compare the returned value with the minimum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject88() throws Fault {
    Boolean oMinSmallintVal = null;
    Integer rSmallintVal = null;
    String sminSmallintVal = null;
    Integer minSmallintVal = null;
    boolean booleanVal;
    try {

      rsSch.createTab("Smallint_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_In_Null(?)}");

      msg.setMsg(
          "to extract the Minimum Value of Boolean to be Updated in Smallint table");
      sminSmallintVal = rsSch.extractValAsNumericString("Bit_Tab", 2, sqlp,
          conn);
      minSmallintVal = Integer.valueOf(sminSmallintVal);
      oMinSmallintVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp, conn);

      cstmt.setObject(1, oMinSmallintVal, java.sql.Types.SMALLINT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to queryfrom the database to check the call of cstmt.executeUpdate");

      String Null_Val_Query = sqlp.getProperty("Smallint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object oSmallintVal = rs.getObject(1);
      rSmallintVal = Integer.valueOf(oSmallintVal.toString());
      msg.addOutputMsg("" + minSmallintVal, "" + rSmallintVal);

      if (rSmallintVal.compareTo(minSmallintVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

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
   * @testName: testSetObject89
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Integer_Tab with the maximum value
   * of the Bit_Tab. Execute a query to retrieve the Min_Val from Integer_Tab.
   * Compare the returned value with the maximum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject89() throws Fault {
    Boolean oMaxIntegerVal = null;
    Integer rIntegerVal = null;
    String smaxIntegerVal = null;
    Integer maxIntegerVal = null;
    boolean booleanVal;
    try {

      // to create the Integer Table
      rsSch.createTab("Integer_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_In_Min(?)}");

      msg.setMsg(
          "to extract the Maximum Value of Boolean to be Updated in Integer table");
      smaxIntegerVal = rsSch.extractValAsNumericString("Bit_Tab", 1, sqlp,
          conn);
      maxIntegerVal = Integer.valueOf(smaxIntegerVal);
      oMaxIntegerVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp, conn);

      cstmt.setObject(1, oMaxIntegerVal, java.sql.Types.INTEGER);
      cstmt.executeUpdate();

      msg.setMsg(
          "to queryfrom the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Integer_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object oIntegerVal = rs.getObject(1);
      rIntegerVal = Integer.valueOf(oIntegerVal.toString());
      msg.addOutputMsg("" + maxIntegerVal, "" + rIntegerVal);

      if (rIntegerVal.compareTo(maxIntegerVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

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
   * @testName: testSetObject90
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Integer_Tab with the minimum value
   * of the Bit_Tab. Execute a query to retrieve the Null_Val from Integer_Tab.
   * Compare the returned value with the minimum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject90() throws Fault {
    Boolean oMinIntegerVal = null;
    Integer rIntegerVal = null;
    String sminIntegerVal = null;
    Integer minIntegerVal = null;
    boolean booleanVal;
    try {

      rsSch.createTab("Integer_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_In_Null(?)}");

      msg.setMsg(
          "to extract the Minimum Value of Boolean to be Updated in Integer table");
      sminIntegerVal = rsSch.extractValAsNumericString("Bit_Tab", 2, sqlp,
          conn);
      minIntegerVal = Integer.valueOf(sminIntegerVal);
      oMinIntegerVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp, conn);

      cstmt.setObject(1, oMinIntegerVal, java.sql.Types.INTEGER);
      cstmt.executeUpdate();

      msg.setMsg(
          "to queryfrom the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Integer_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object oIntegerVal = rs.getObject(1);
      rIntegerVal = Integer.valueOf(oIntegerVal.toString());

      msg.addOutputMsg("" + minIntegerVal, "" + rIntegerVal);
      if (rIntegerVal.compareTo(minIntegerVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

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
   * @testName: testSetObject91
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Bigint_Tab with the maximum value
   * of the Bit_Tab. Execute a query to retrieve the Min_Val from Bigint_Tab.
   * Compare the returned value with the maximum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject91() throws Fault {
    Boolean oMaxBigintVal = null;
    Long rBigintVal = null;
    String smaxBigintVal = null;
    Long maxBigintVal = null;
    boolean booleanVal;
    try {

      rsSch.createTab("Bigint_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_In_Min(?)}");

      msg.setMsg(
          "to extract the Maximum Value of Boolean to be Updated in Bigint table");
      smaxBigintVal = rsSch.extractValAsNumericString("Bit_Tab", 1, sqlp, conn);
      maxBigintVal = Long.valueOf(smaxBigintVal);
      oMaxBigintVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp, conn);

      cstmt.setObject(1, oMaxBigintVal, java.sql.Types.BIGINT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to queryfrom the database to check the call of cstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Bigint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object oBigintVal = rs.getObject(1);
      rBigintVal = Long.valueOf(oBigintVal.toString());
      msg.addOutputMsg("" + maxBigintVal, "" + rBigintVal);

      if (rBigintVal.compareTo(maxBigintVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

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
   * @testName: testSetObject92
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Bigint_Tab with the minimum value
   * of the Bit_Tab. Execute a query to retrieve the Null_Val from Bigint_Tab.
   * Compare the returned value with the minimum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject92() throws Fault {
    Boolean oMinBigintVal = null;
    Long rBigintVal = null;
    String sminBigintVal = null;
    Long minBigintVal = null;
    boolean booleanVal;
    try {

      rsSch.createTab("Bigint_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_In_Null(?)}");

      msg.setMsg(
          "to extract the Minimum Value of Boolean to be Updated in Bigint table");
      sminBigintVal = rsSch.extractValAsNumericString("Bit_Tab", 2, sqlp, conn);
      minBigintVal = Long.valueOf(sminBigintVal);
      oMinBigintVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp, conn);

      cstmt.setObject(1, oMinBigintVal, java.sql.Types.BIGINT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to queryfrom the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Bigint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object oBigintVal = rs.getObject(1);
      rBigintVal = Long.valueOf(oBigintVal.toString());
      msg.addOutputMsg("" + minBigintVal, "" + rBigintVal);

      if (rBigintVal.compareTo(minBigintVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");
      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

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
   * @testName: testSetObject93
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Real_Tab with the maximum value of
   * the Bit_Tab. Execute a query to retrieve the Min_Val from Real_Tab. Compare
   * the returned value with the maximum value extracted from tssql.stmt file.
   * Both of them should be equal.
   */
  public void testSetObject93() throws Fault {
    Boolean oMaxRealVal = null;
    Float rRealVal = null;
    String smaxRealVal = null;
    Float maxRealVal = null;
    boolean booleanVal;
    try {

      rsSch.createTab("Real_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_In_Min(?)}");

      msg.setMsg(
          "to extract the Maximum Value of Boolean to be Updated in Real table");
      smaxRealVal = rsSch.extractValAsNumericString("Bit_Tab", 1, sqlp, conn);
      oMaxRealVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp, conn);
      maxRealVal = Float.valueOf(smaxRealVal);

      cstmt.setObject(1, oMaxRealVal, java.sql.Types.REAL);
      cstmt.executeUpdate();

      msg.setMsg(
          "to queryfrom the database to check the call of cstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Real_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object oRealVal = rs.getObject(1);
      rRealVal = Float.valueOf(oRealVal.toString());
      msg.addOutputMsg("" + maxRealVal, "" + rRealVal);

      if (rRealVal.compareTo(maxRealVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");
      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

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
   * @testName: testSetObject94
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Real_Tab with the minimum value of
   * the Bit_Tab. Execute a query to retrieve the Null_Val from Real_Tab.
   * Compare the returned value with the minimum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject94() throws Fault {
    Boolean oMinRealVal = null;
    Float rRealVal = null;
    String sminRealVal = null;
    Float minRealVal = null;
    boolean booleanVal;
    try {

      rsSch.createTab("Real_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_In_Null(?)}");

      msg.setMsg(
          "to extract the Minimum Value of Boolean to be Updated in Real table");
      sminRealVal = rsSch.extractValAsNumericString("Bit_Tab", 2, sqlp, conn);
      minRealVal = Float.valueOf(sminRealVal);
      oMinRealVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp, conn);

      cstmt.setObject(1, oMinRealVal, java.sql.Types.REAL);
      cstmt.executeUpdate();

      msg.setMsg(
          "to queryfrom the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Real_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object oRealVal = rs.getObject(1);
      rRealVal = Float.valueOf(oRealVal.toString());
      msg.addOutputMsg("" + minRealVal, "" + rRealVal);

      if (rRealVal.compareTo(minRealVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");
      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

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
   * @testName: testSetObject95
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Float_Tab with the maximum value of
   * the Bit_Tab. Execute a query to retrieve the Min_Val from Float_Tab.
   * Compare the returned value with the maximum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject95() throws Fault {
    Boolean oMaxFloatVal = null;
    Double rFloatVal = null;
    String smaxFloatVal = null;
    Double maxFloatVal = null;
    boolean booleanVal;
    try {

      rsSch.createTab("Float_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Float_In_Min(?)}");

      msg.setMsg(
          "to extract the Maximum Value of Boolean to be Updated in Float table");
      smaxFloatVal = rsSch.extractValAsNumericString("Bit_Tab", 1, sqlp, conn);
      maxFloatVal = Double.valueOf(smaxFloatVal);
      oMaxFloatVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp, conn);

      cstmt.setObject(1, oMaxFloatVal, java.sql.Types.FLOAT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to queryfrom the database to check the call of cstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Float_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      rFloatVal = (Double) rs.getObject(1);
      msg.addOutputMsg("" + maxFloatVal, "" + rFloatVal);

      if (rFloatVal.equals(maxFloatVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");
      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

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
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject96
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Float_Tab with the minimum value
   * of the Bit_Tab. Execute a query to retrieve the Null_Val from Float_Tab.
   * Compare the returned value with the minimum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject96() throws Fault {
    Boolean oMinFloatVal = null;
    Double rFloatVal = null;
    String sminFloatVal = null;
    Double minFloatVal = null;
    boolean booleanVal;
    try {

      rsSch.createTab("Float_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Float_In_Null(?)}");

      msg.setMsg(
          "to extract the Minimum Value of Boolean to be Updated in Double table");
      sminFloatVal = rsSch.extractValAsNumericString("Bit_Tab", 2, sqlp, conn);
      minFloatVal = Double.valueOf(sminFloatVal);
      oMinFloatVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp, conn);

      cstmt.setObject(1, oMinFloatVal, java.sql.Types.FLOAT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to queryfrom the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Float_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rFloatVal = (Double) rs.getObject(1);
      msg.addOutputMsg("" + minFloatVal, "" + rFloatVal);
      if (rFloatVal.equals(minFloatVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");
      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

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
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject97
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Double_Tab with the maximum value
   * of the Bit_Tab. Execute a query to retrieve the Min_Val from Double_Tab.
   * Compare the returned value with the maximum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject97() throws Fault {
    Boolean oMaxDoubleVal = null;
    Double rDoubleVal = null;
    String smaxDoubleVal = null;
    Double maxDoubleVal = null;
    boolean booleanVal;
    try {

      rsSch.createTab("Double_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_In_Min(?)}");

      msg.setMsg(
          "to extract the Maximum Value of Boolean to be Updated in Double table");

      smaxDoubleVal = rsSch.extractValAsNumericString("Bit_Tab", 1, sqlp, conn);
      maxDoubleVal = Double.valueOf(smaxDoubleVal);
      oMaxDoubleVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp, conn);

      cstmt.setObject(1, oMaxDoubleVal, java.sql.Types.DOUBLE);
      cstmt.executeUpdate();

      msg.setMsg(
          "to queryfrom the database to check the call of cstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Double_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      rDoubleVal = (Double) rs.getObject(1);
      msg.addOutputMsg("" + maxDoubleVal, "" + rDoubleVal);

      if (rDoubleVal.equals(maxDoubleVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");
      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

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
        rsSch.dropTab("Double_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject98
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Double_Tab with the minimum value
   * of the Bit_Tab. Execute a query to retrieve the Null_Val from Double_Tab.
   * Compare the returned value with the minimum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject98() throws Fault {
    Boolean oMinDoubleVal = null;
    Double rDoubleVal = null;
    String sminDoubleVal = null;
    Double minDoubleVal = null;
    boolean booleanVal;
    try {

      rsSch.createTab("Double_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_In_Null(?)}");

      msg.setMsg(
          "to extract the Minimum Value of Boolean to be Updated in Double table");

      sminDoubleVal = rsSch.extractValAsNumericString("Bit_Tab", 2, sqlp, conn);
      minDoubleVal = Double.valueOf(sminDoubleVal);
      oMinDoubleVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp, conn);

      cstmt.setObject(1, oMinDoubleVal, java.sql.Types.DOUBLE);
      cstmt.executeUpdate();

      msg.setMsg(
          "to queryfrom the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Double_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rDoubleVal = (Double) rs.getObject(1);
      msg.addOutputMsg("" + minDoubleVal, "" + rDoubleVal);

      if (rDoubleVal.equals(minDoubleVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");
      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

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
        rsSch.dropTab("Double_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject99
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Decimal_Tab with the maximum value
   * of the Bit_Tab. Execute a query to retrieve the Null_Val from Decimal_Tab.
   * Compare the returned value with the maximum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject99() throws Fault {
    Boolean oMaxDecimalVal = null;
    BigDecimal rDecimalVal = null;
    String smaxDecimalVal = null;
    BigDecimal maxDecimalVal = null;
    boolean booleanVal;
    try {

      rsSch.createTab("Decimal_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_In_Null(?)}");

      msg.setMsg(
          "to extract the Maximum Value of Boolean to be Updated in Decimal Table");
      smaxDecimalVal = rsSch.extractValAsNumericString("Bit_Tab", 1, sqlp,
          conn);
      maxDecimalVal = new BigDecimal(smaxDecimalVal);
      oMaxDecimalVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp, conn);

      cstmt.setObject(1, oMaxDecimalVal, java.sql.Types.DECIMAL, 2);
      cstmt.executeUpdate();

      msg.setMsg(
          "to queryfrom the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Decimal_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rDecimalVal = (BigDecimal) rs.getObject(1);
      msg.addOutputMsg("" + maxDecimalVal, "" + rDecimalVal);

      if ((rDecimalVal.compareTo(maxDecimalVal)) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");
      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

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
        rsSch.dropTab("Decimal_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject100
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Decimal_Tab with the minimum value
   * of the Bit_Tab. Execute a query to retrieve the Null_Val from Decimal_Tab.
   * Compare the returned value with the minimum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject100() throws Fault {
    Boolean oMinDecimalVal = null;
    BigDecimal rDecimalVal = null;
    String sminDecimalVal = null;
    BigDecimal minDecimalVal = null;
    boolean booleanVal;
    try {

      rsSch.createTab("Decimal_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_In_Max(?)}");

      msg.setMsg(
          "to extract the Minimum Value of Boolean to be Updated in Decimal Table");
      sminDecimalVal = rsSch.extractValAsNumericString("Bit_Tab", 2, sqlp,
          conn);
      minDecimalVal = new BigDecimal(sminDecimalVal);
      oMinDecimalVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp, conn);

      cstmt.setObject(1, oMinDecimalVal, java.sql.Types.DECIMAL, 2);
      cstmt.executeUpdate();

      msg.setMsg(
          "to queryfrom the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Decimal_Query_Max", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rDecimalVal = (BigDecimal) rs.getObject(1);
      msg.addOutputMsg("" + minDecimalVal, "" + rDecimalVal);

      if ((rDecimalVal.compareTo(minDecimalVal)) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed");
      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

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
        rsSch.dropTab("Decimal_Tab", conn);
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
