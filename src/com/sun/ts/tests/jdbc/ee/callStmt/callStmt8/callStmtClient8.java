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

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt8;

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
 * The callStmtClient8 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class callStmtClient8 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt8";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private DataSource ds1 = null;

  private rsSchema rsSch = null;

  private csSchema csSch = null;

  private JDBCTestMsg msg = null;

  private String drManager = null;

  private Properties sqlp = null;

  private CallableStatement cstmt = null;

  private transient DatabaseMetaData dbmd = null;

  private Statement stmt = null;
  // private ResultSet rs = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    callStmtClient8 theTests = new callStmtClient8();
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
        csSch = new csSchema();
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
   * @testName: testSetObject01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set String object for SQL Type CHAR and
   * call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a String object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject01() throws Fault {
    ResultSet rs = null;
    String oRetVal = null;
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);

      String sCoffeeName = rsSch.extractVal("Char_Tab", 1, sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_In_Null(?)}");
      cstmt.setObject(1, sCoffeeName);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      oRetVal = (String) rs.getObject(1);

      msg.addOutputMsg(sCoffeeName, oRetVal);

      if (oRetVal.trim().equals(sCoffeeName.trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
   * @testName: testSetObject02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set String object for SQL Type VARCHAR
   * and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a String object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject02() throws Fault {
    ResultSet rs = null;
    String oRetVal = null;
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      String sCoffeeName = rsSch.extractVal("Varchar_Tab", 1, sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_In_Null(?)}");
      cstmt.setObject(1, sCoffeeName);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      oRetVal = (String) rs.getObject(1);
      msg.addOutputMsg(sCoffeeName, oRetVal);

      if (oRetVal.trim().equals(sCoffeeName.trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
   * @testName: testSetObject03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set String object for SQL Type
   * LONGVARCHAR and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a String object that is
   * been set. Compare the result with the extracted value from tssql.stmt file.
   * Both the values should be equal.
   *
   */
  public void testSetObject03() throws Fault {
    ResultSet rs = null;
    String oRetVal = null;
    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      String sCoffeeName = rsSch.extractVal("Longvarchar_Tab", 1, sqlp, conn);
      sCoffeeName = sCoffeeName.trim();

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Lvarchar_In_Null(?)}");
      cstmt.setObject(1, sCoffeeName);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      oRetVal = (String) rs.getObject(1);
      oRetVal = oRetVal.trim();
      msg.addOutputMsg(sCoffeeName, oRetVal);

      if (oRetVal.equals(sCoffeeName)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
   * @testName: testSetObject04
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set BigDecimal object for SQL Type
   * Numeric and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a BigDecimal object.
   * Compare the returned value with the extracted value from tssql.stmt file.
   * Both the values should be equal.
   *
   */
  public void testSetObject04() throws Fault {
    ResultSet rs = null;
    BigDecimal oRetVal = null;
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      String sMaxBigDecimalVal = rsSch.extractVal("Numeric_Tab", 2, sqlp, conn);
      BigDecimal maxBigDecimal = new BigDecimal(sMaxBigDecimalVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Numeric_In_Max(?)}");
      cstmt.setObject(1, maxBigDecimal);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Numeric_Query_Max", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      oRetVal = (BigDecimal) rs.getObject(1);
      msg.addOutputMsg("" + maxBigDecimal, "" + oRetVal);

      if ((oRetVal.compareTo(maxBigDecimal) == 0)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
      }
    }
  }

  /*
   * @testName: testSetObject05
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set BigDecimal object for SQL Type
   * Numeric and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column) method. It should return a BigDecimal
   * object. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject05() throws Fault {
    ResultSet rs = null;
    BigDecimal oRetVal = null;
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      String sMinBigDecimalVal = rsSch.extractVal("Numeric_Tab", 2, sqlp, conn);
      BigDecimal minBigDecimal = new BigDecimal(sMinBigDecimalVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Numeric_In_Null(?)}");
      cstmt.setObject(1, minBigDecimal);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Numeric_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      oRetVal = (BigDecimal) rs.getObject(1);
      msg.addOutputMsg("" + minBigDecimal, "" + oRetVal);

      if ((oRetVal.compareTo(minBigDecimal) == 0)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
      }
    }
  }

  /*
   * @testName: testSetObject06
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set BigDecimal object for SQL Type
   * Decimal and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a BigDecimal object.
   * Compare the returned value with the value extracted from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject06() throws Fault {
    ResultSet rs = null;
    BigDecimal oRetVal = null;
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      String sMaxBigDecimalVal = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);
      BigDecimal maxBigDecimal = new BigDecimal(sMaxBigDecimalVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_In_Max(?)}");
      cstmt.setObject(1, maxBigDecimal);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Decimal_Query_Max", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      oRetVal = (BigDecimal) rs.getObject(1);
      msg.addOutputMsg("" + maxBigDecimal, "" + oRetVal);

      if ((oRetVal.compareTo(maxBigDecimal) == 0)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
      }
    }
  }

  /*
   * @testName: testSetObject07
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set BigDecimal object for SQL Type
   * Decimal and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a BigDecimal object.
   * Compare the result with the extracted value from the tssql.stmt file. Both
   * the values should be equal.
   *
   */
  public void testSetObject07() throws Fault {
    ResultSet rs = null;
    BigDecimal oRetVal = null;
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      String sMinBigDecimalVal = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);
      BigDecimal minBigDecimal = new BigDecimal(sMinBigDecimalVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_In_Null(?)}");
      cstmt.setObject(1, minBigDecimal);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Decimal_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      oRetVal = (BigDecimal) rs.getObject(1);
      msg.addOutputMsg("" + minBigDecimal, "" + oRetVal);

      if ((oRetVal.compareTo(minBigDecimal) == 0)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
      }
    }
  }

  /*
   * @testName: testSetObject08
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Boolean object for SQL Type Bit and
   * call statement.executeQuery(String sql) method and call
   * ResultSet.getBoolean(int column). Compare the result with the extracted
   * value from the tssql.stmt file. Both the values should be equal.
   */
  public void testSetObject08() throws Fault {
    ResultSet rs = null;
    Boolean oRetVal = null;
    try {
      rsSch.createTab("Bit_Tab", sqlp, conn);

      Boolean maxBoolean = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bit_In_Min(?)}");
      cstmt.setObject(1, maxBoolean);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Bit_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      oRetVal = Boolean.valueOf(rs.getBoolean(1));

      msg.addOutputMsg("" + maxBoolean, "" + oRetVal);

      if (oRetVal.equals(maxBoolean)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
      }
    }
  }

  /*
   * @testName: testSetObject09
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Boolean object for SQL Type Bit and
   * call statement.executeQuery(String sql) method and call
   * ResultSet.getBoolen(int column). Compare the result with the extracted
   * value from the tssql.stmt file. Both the values should be equal.
   */
  public void testSetObject09() throws Fault {
    ResultSet rs = null;
    Boolean oRetVal = null;
    Boolean minBoolean = null;

    try {
      rsSch.createTab("Bit_Tab", sqlp, conn);

      minBoolean = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bit_In_Null(?)}");
      cstmt.setObject(1, minBoolean);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Bit_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      oRetVal = Boolean.valueOf(rs.getBoolean(1));

      msg.addOutputMsg("" + minBoolean, "" + oRetVal);

      if (oRetVal.equals(minBoolean)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
      }
    }
  }

  /*
   * @testName: testSetObject10
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Integer object for SQL Type Integer
   * and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Integer object. Compare
   * the result with the extracted value from the tssql.stmt file. Both the
   * values should be equal.
   *
   */
  public void testSetObject10() throws Fault {
    ResultSet rs = null;
    Integer oRetVal = null;
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);

      String sMaxIntegerVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);
      Integer maxInteger = new Integer(sMaxIntegerVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_In_Min(?)}");
      cstmt.setObject(1, maxInteger);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Integer_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      Object objRetVal = rs.getObject(1);
      oRetVal = new Integer(objRetVal.toString());
      msg.addOutputMsg("" + maxInteger, "" + oRetVal);

      if (oRetVal.compareTo(maxInteger) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
   * @testName: testSetObject11
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Integer object for SQL Type Integer
   * and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Integer object. Compare
   * the result with the extracted value from the tssql.stmt file. Both the
   * values should be equal.
   *
   */
  public void testSetObject11() throws Fault {
    ResultSet rs = null;
    Integer oRetVal = null;
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);

      String sMinIntegerVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);
      Integer minInteger = new Integer(sMinIntegerVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_In_Null(?)}");
      cstmt.setObject(1, minInteger);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Integer_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      Object objRetVal = rs.getObject(1);
      oRetVal = new Integer(objRetVal.toString());
      msg.addOutputMsg("" + minInteger, "" + oRetVal);

      if (oRetVal.compareTo(minInteger) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
   * @testName: testSetObject12
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Long object for SQL Type Bigint and
   * call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Long object. Compare
   * the result with the extracted value from the tssql.stmt file. Both the
   * values should be equal.
   *
   */
  public void testSetObject12() throws Fault {
    ResultSet rs = null;
    Long oRetVal = null;
    try {
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      String sMaxLongVal = rsSch.extractVal("Bigint_Tab", 1, sqlp, conn);
      Long maxLong = new Long(sMaxLongVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_In_Min(?)}");
      cstmt.setObject(1, maxLong);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Bigint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      Object objRetVal = rs.getObject(1);
      oRetVal = new Long(objRetVal.toString());
      msg.addOutputMsg("" + maxLong, "" + oRetVal);
      if (oRetVal.compareTo(maxLong) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
   * @testName: testSetObject13
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Long object for SQL Type Bigint and
   * call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Long object. Compare
   * the result with the extracted value from the tssql.stmt file. Both the
   * values should be equal.
   *
   */
  public void testSetObject13() throws Fault {
    ResultSet rs = null;
    Long oRetVal = null;
    try {
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      String sMinLongVal = rsSch.extractVal("Bigint_Tab", 2, sqlp, conn);
      Long minLong = new Long(sMinLongVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_In_Null(?)}");
      cstmt.setObject(1, minLong);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Bigint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      Object objRetVal = rs.getObject(1);
      oRetVal = new Long(objRetVal.toString());
      msg.addOutputMsg("" + minLong, "" + oRetVal);

      if (oRetVal.compareTo(minLong) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
   * @testName: testSetObject14
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Double object for SQL Type Double
   * and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Double object. Compare
   * the result with the extracted value from the tssql.stmt file. Both the
   * values should be equal.
   *
   */
  public void testSetObject14() throws Fault {
    ResultSet rs = null;
    Double oRetVal = null;
    try {
      rsSch.createTab("Double_Tab", sqlp, conn);

      String sMaxDoubleVal = rsSch.extractVal("Double_Tab", 1, sqlp, conn);
      Double maxDouble = new Double(sMaxDoubleVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_In_Min(?)}");
      cstmt.setObject(1, maxDouble);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Double_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      oRetVal = (Double) rs.getObject(1);
      msg.addOutputMsg("" + maxDouble, "" + oRetVal);

      if (oRetVal.equals(maxDouble)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
        rsSch.dropTab("Double_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject15
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Double object for SQL Type Double
   * and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Double object. Compare
   * the result with the extracted value from the tssql.stmt file. Both the
   * values should be equal.
   *
   */
  public void testSetObject15() throws Fault {
    ResultSet rs = null;
    Double oRetVal = null;
    try {
      rsSch.createTab("Double_Tab", sqlp, conn);

      String sMinDoubleVal = rsSch.extractVal("Double_Tab", 2, sqlp, conn);
      Double minDouble = new Double(sMinDoubleVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_In_Null(?)}");
      cstmt.setObject(1, minDouble);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Double_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      oRetVal = (Double) rs.getObject(1);
      msg.addOutputMsg("" + minDouble, "" + oRetVal);

      if (oRetVal.equals(minDouble)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
        rsSch.dropTab("Double_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject16
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Double object for SQL Type Float
   * and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Double object. Compare
   * the result with the extracted value from the tssql.stmt file. Both the
   * values should be equal.
   *
   */
  public void testSetObject16() throws Fault {
    ResultSet rs = null;
    Float oRetVal = null;
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);

      String sMaxFloatVal = rsSch.extractVal("Float_Tab", 1, sqlp, conn);
      Float maxFloat = new Float(sMaxFloatVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Float_In_Min(?)}");
      cstmt.setObject(1, maxFloat);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Float_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      Object objRetVal = rs.getObject(1);
      oRetVal = new Float(objRetVal.toString());
      msg.addOutputMsg("" + maxFloat, "" + oRetVal);
      if (oRetVal.compareTo(maxFloat) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject17
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Float object for SQL Type Float and
   * call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Float object. Compare
   * the result with the extracted value from the tssql.stmt file. Both the
   * values should be equal.
   *
   */
  public void testSetObject17() throws Fault {
    ResultSet rs = null;
    Float oRetVal = null;
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);

      String sMinFloatVal = rsSch.extractVal("Float_Tab", 2, sqlp, conn);
      Float minFloat = new Float(sMinFloatVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Float_In_Null(?)}");
      cstmt.setObject(1, minFloat);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Float_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      Object objRetVal = rs.getObject(1);
      oRetVal = new Float(objRetVal.toString());
      msg.addOutputMsg("" + minFloat, "" + oRetVal);

      if (oRetVal.compareTo(minFloat) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject18
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Date object for SQL Type Date and
   * call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Date object. Compare
   * the result with the extracted value from the tssql.stmt file. Both the
   * values should be equal.
   *
   */
  public void testSetObject18() throws Fault {
    ResultSet rs = null;
    java.sql.Date oRetVal = null;
    java.sql.Date mfgDateVal = null;

    try {
      rsSch.createTab("Date_Tab", sqlp, conn);

      String sMfgDateVal = rsSch.extractVal("Date_Tab", 1, sqlp, conn);
      sMfgDateVal = sMfgDateVal.substring(sMfgDateVal.indexOf('\'') + 1,
          sMfgDateVal.lastIndexOf('\''));
      mfgDateVal = java.sql.Date.valueOf(sMfgDateVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Date_In_Null(?)}");
      cstmt.setObject(1, mfgDateVal);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Date_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      java.util.Date objRetVal = (java.util.Date) rs.getObject(1);
      long lDate = objRetVal.getTime();
      oRetVal = new java.sql.Date(lDate);

      msg.addOutputMsg("" + mfgDateVal, "" + oRetVal);
      if (oRetVal.compareTo(mfgDateVal) == 0) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
        rsSch.dropTab("Date_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject19
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Time object for SQL Type Time and
   * call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Time object. Compare
   * the result with the extracted value from the tssql.stmt file. Both the
   * values should be equal.
   *
   */
  public void testSetObject19() throws Fault {
    ResultSet rs = null;
    java.sql.Time oRetVal = null;
    java.sql.Time brkTimeVal = null;

    try {
      rsSch.createTab("Time_Tab", sqlp, conn);

      String sBrkTimeVal = rsSch.extractVal("Time_Tab", 1, sqlp, conn);
      sBrkTimeVal = sBrkTimeVal.substring(sBrkTimeVal.indexOf('\'') + 1,
          sBrkTimeVal.lastIndexOf('\''));
      brkTimeVal = Time.valueOf(sBrkTimeVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Time_In_Null(?)}");
      cstmt.setObject(1, brkTimeVal);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Time_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      Thread.sleep(5000);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      java.util.Date objRetVal = (java.util.Date) rs.getObject(1);
      long lDate = objRetVal.getTime();
      oRetVal = new java.sql.Time(lDate);

      msg.addOutputMsg(brkTimeVal.toString(), oRetVal.toString());

      if (oRetVal.toString().trim().equals(brkTimeVal.toString().trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
        rsSch.dropTab("Time_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject20
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Timestamp object for SQL Type
   * Timestamp & call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Timestamp object.
   * Compare the result with the extracted value from the tssql.stmt file. Both
   * the values should be equal.
   *
   */
  public void testSetObject20() throws Fault {
    ResultSet rs = null;
    java.sql.Timestamp oRetVal = null;
    java.sql.Timestamp inTimeVal = null;

    try {
      rsSch.createTab("Timestamp_Tab", sqlp, conn);

      msg.setMsg("to extract the Value of Timestamp to be Updated");
      String sInTimeVal = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);
      sInTimeVal = sInTimeVal.substring(sInTimeVal.indexOf('\'') + 1,
          sInTimeVal.lastIndexOf('\''));
      msg.setMsg("Timestamp Value :" + sInTimeVal);

      // to convert String value into java.sql.Timestamp
      inTimeVal = inTimeVal.valueOf(sInTimeVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Timestamp_In_Null(?)}");
      cstmt.setObject(1, inTimeVal);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Timestamp_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      oRetVal = (java.sql.Timestamp) rs.getObject(1);

      msg.addOutputMsg("" + inTimeVal, "" + oRetVal);

      if (oRetVal.equals(inTimeVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject method is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
        rsSch.dropTab("Timestamp_Tab", conn);
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
