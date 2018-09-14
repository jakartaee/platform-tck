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

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt10;

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
 * The callStmtClient10 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class callStmtClient10 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt10";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  // private ResultSet rs=null;
  private rsSchema rsSch = null;

  private csSchema csSch = null;

  private String drManager = null;

  private Properties sqlp = null;

  private transient DatabaseMetaData dbmd = null;

  private Statement stmt = null;

  private CallableStatement cstmt = null;

  private Properties props = null;

  private JDBCTestMsg msg = null;
  /* Run test in standalone mode */

  public static void main(String[] args) {
    callStmtClient10 theTests = new callStmtClient10();
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
   * @testName: testSetObject41
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:6; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type FLOAT and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Double object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject41() throws Fault {
    ResultSet rs = null;
    Double oRetVal = null;
    Double minFloatVal = null;
    String sMinFloatVal = null;

    try {
      rsSch.createTab("Float_Tab", sqlp, conn);

      sMinFloatVal = rsSch.extractVal("Float_Tab", 2, sqlp, conn);

      minFloatVal = new Double(sMinFloatVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Float_In_Null(?)}");
      cstmt.setObject(1, sMinFloatVal, java.sql.Types.FLOAT);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Float_Query_Null", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      oRetVal = (Double) rs.getObject(1);

      msg.addOutputMsg("" + minFloatVal, "" + oRetVal);
      if (oRetVal.equals(minFloatVal)) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed!");

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
   * @testName: testSetObject42
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:8; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type DOUBLE and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Double object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject42() throws Fault {
    ResultSet rs = null;
    Double oRetVal = null;
    Double maxDoubleVal = null;
    String sMaxDoubleVal = null;

    try {
      rsSch.createTab("Double_Tab", sqlp, conn);

      sMaxDoubleVal = rsSch.extractVal("Double_Tab", 1, sqlp, conn);

      maxDoubleVal = new Double(sMaxDoubleVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_In_Min(?)}");
      cstmt.setObject(1, sMaxDoubleVal, java.sql.Types.DOUBLE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Double_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      oRetVal = (Double) rs.getObject(1);

      msg.addOutputMsg("" + maxDoubleVal, "" + oRetVal);
      if (oRetVal.equals(maxDoubleVal)) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed!");

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
   * @testName: testSetObject43
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type DOUBLE and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Double object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject43() throws Fault {
    ResultSet rs = null;
    Double oRetVal = null;
    Double minDoubleVal = null;
    String sMinDoubleVal = null;

    try {
      rsSch.createTab("Double_Tab", sqlp, conn);

      sMinDoubleVal = rsSch.extractVal("Double_Tab", 2, sqlp, conn);

      minDoubleVal = new Double(sMinDoubleVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_In_Null(?)}");
      cstmt.setObject(1, sMinDoubleVal, java.sql.Types.DOUBLE);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Double_Query_Null", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      oRetVal = (Double) rs.getObject(1);

      msg.addOutputMsg("" + minDoubleVal, "" + oRetVal);
      if (oRetVal.equals(minDoubleVal)) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed!");

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
   * @testName: testSetObject44
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JDBC:JAVADOC:10; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type DECIMAL and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a BigDecimal object that
   * is been set. Compare the result with the extracted value from the
   * tssql.stmt file. Both the values should be equal.
   *
   */
  public void testSetObject44() throws Fault {
    ResultSet rs = null;
    Object oRetVal = null;
    BigDecimal maxDecimalVal = null;
    String sMaxDecimalVal = null;

    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      sMaxDecimalVal = rsSch.extractVal("Decimal_Tab", 1, sqlp, conn);

      maxDecimalVal = new BigDecimal(sMaxDecimalVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_In_Max(?)}");
      cstmt.setObject(1, sMaxDecimalVal, java.sql.Types.DECIMAL, 15);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Decimal_Query_Max", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      oRetVal = rs.getObject(1);
      BigDecimal bRetVal = new BigDecimal(oRetVal.toString());

      msg.addOutputMsg(sMaxDecimalVal, oRetVal.toString());

      if (bRetVal.compareTo(maxDecimalVal) == 0) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed!");

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
   * @testName: testSetObject45
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type DECIMAL and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a BigDecimal object that
   * is been set. Compare the result with the extracted value from the
   * tssql.stmt file. Both the values should be equal.
   *
   */
  public void testSetObject45() throws Fault {
    ResultSet rs = null;
    Object oRetVal = null;
    BigDecimal minDecimalVal = null;
    String sMinDecimalVal = null;

    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      sMinDecimalVal = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);

      minDecimalVal = new BigDecimal(sMinDecimalVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_In_Null(?)}");
      cstmt.setObject(1, sMinDecimalVal, java.sql.Types.DECIMAL, 15);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Decimal_Query_Null", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      oRetVal = rs.getObject(1);
      BigDecimal bRetVal = new BigDecimal(oRetVal.toString());

      msg.addOutputMsg(sMinDecimalVal, oRetVal.toString());
      if (bRetVal.compareTo(minDecimalVal) == 0) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed!");

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
   * @testName: testSetObject46
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JDBC:JAVADOC:9; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type NUMERIC and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a BigDecimal object that
   * is been set. Compare the result with the extracted value from the
   * tssql.stmt file. Both the values should be equal.
   *
   */
  public void testSetObject46() throws Fault {
    ResultSet rs = null;
    Object oRetVal = null;
    BigDecimal maxNumericVal = null;
    String sMaxNumericVal = null;

    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      sMaxNumericVal = rsSch.extractVal("Numeric_Tab", 1, sqlp, conn);

      maxNumericVal = new BigDecimal(sMaxNumericVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Numeric_In_Max(?)}");
      cstmt.setObject(1, sMaxNumericVal, java.sql.Types.NUMERIC, 15);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Numeric_Query_Max", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      oRetVal = rs.getObject(1);
      BigDecimal bRetVal = new BigDecimal(oRetVal.toString());

      msg.addOutputMsg(sMaxNumericVal, oRetVal.toString());
      if (bRetVal.compareTo(maxNumericVal) == 0) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed!");

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
   * @testName: testSetObject47
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type NUMERIC and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a BigDecimal object that
   * is been set. Compare the result with the extracted value from the
   * tssql.stmt file. Both the values should be equal.
   *
   */
  public void testSetObject47() throws Fault {
    ResultSet rs = null;
    Object oRetVal = null;
    BigDecimal minNumericVal = null;
    String sMinNumericVal = null;

    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      sMinNumericVal = rsSch.extractVal("Numeric_Tab", 2, sqlp, conn);

      minNumericVal = new BigDecimal(sMinNumericVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Numeric_In_Null(?)}");
      cstmt.setObject(1, sMinNumericVal, java.sql.Types.NUMERIC, 15);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Numeric_Query_Null", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      oRetVal = rs.getObject(1);
      BigDecimal bRetVal = new BigDecimal(oRetVal.toString());

      msg.addOutputMsg(sMinNumericVal, oRetVal.toString());
      if (bRetVal.compareTo(minNumericVal) == 0) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed!");

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
   * @testName: testSetObject48
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:1; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type BIT and call statement.executeQuery(String sql) method and call
   * ResultSet.getBoolean(int column). It should return a boolean value that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   * -Description details- This test is actually testing the ability to
   * successfully call the setObject() method. In order to do that, this test
   * will get the MAXVAL value from the bit tab, and then it will use the
   * setObject method to set/change the MINVAL value to be the same as the
   * MAXVAL. After setting the MINVAL, a query is done to get the newly set
   * MINVAL value and a comparison is done to make sure it was indeed set to the
   * same value as MAXVAL.
   * 
   */
  public void testSetObject48() throws Fault {
    ResultSet rs = null;
    Boolean oRetVal = null;
    Boolean maxBooleanVal = null;
    String sMaxBooleanVal = null;

    try {
      rsSch.createTab("Bit_Tab", sqlp, conn);

      sMaxBooleanVal = rsSch.extractVal("Bit_Tab", 1, sqlp, conn);
      maxBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bit_In_Min(?)}");

      cstmt.setObject(1, sMaxBooleanVal, java.sql.Types.BIT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Bit_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      oRetVal = new Boolean(rs.getBoolean(1));

      msg.addOutputMsg("" + maxBooleanVal, "" + oRetVal);
      if (oRetVal.equals(maxBooleanVal)) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed!");

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
   * @testName: testSetObject49
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:1; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type BIT and call statement.executeQuery(String sql) method and call
   * ResultSet.getBoolean(int column). It should return a boolean value that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   * -Description details- This test is actually testing the ability to
   * successfully call the setObject() method. In order to do that, this test
   * will get the MINVAL value from the bit tab, and then it will use the
   * setObject method to set/change the MAXVAL value to be the same as the
   * MINVAL. After setting the MAXVAL, a query is done to get the newly set
   * MAXVAL value and a comparison is done to make sure it was indeed set to the
   * same value as MINVAL.
   *
   */
  public void testSetObject49() throws Fault {
    ResultSet rs = null;
    Boolean oRetVal = null;
    Boolean minBooleanVal = null;
    String sMinBooleanVal = null;

    try {
      rsSch.createTab("Bit_Tab", sqlp, conn);

      sMinBooleanVal = rsSch.extractVal("Bit_Tab", 2, sqlp, conn);
      minBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bit_In_Null(?)}");
      cstmt.setObject(1, sMinBooleanVal, java.sql.Types.BIT);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Min_Val_Query = sqlp.getProperty("Bit_Query_Null", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      oRetVal = new Boolean(rs.getBoolean(1));

      msg.addOutputMsg("" + minBooleanVal, "" + oRetVal);

      if (oRetVal.equals(minBooleanVal)) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed!");

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
   * @testName: testSetObject50
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:11; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type CHAR and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a String object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject50() throws Fault {
    ResultSet rs = null;
    String oRetVal = null;
    String sCoffeeName = null;

    try {
      rsSch.createTab("Char_Tab", sqlp, conn);

      sCoffeeName = rsSch.extractVal("Char_Tab", 1, sqlp, conn);
      sCoffeeName = sCoffeeName.substring(1, sCoffeeName.length() - 1);
      msg.setMsg("String to be updated  " + sCoffeeName);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_In_Null(?)}");
      cstmt.setObject(1, sCoffeeName, java.sql.Types.CHAR);

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
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed!");

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
   * @testName: testSetObject51
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:12; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type VARCHAR and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a String object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject51() throws Fault {
    ResultSet rs = null;
    String oRetVal = null;
    String sCoffeeName = null;

    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      sCoffeeName = rsSch.extractVal("Varchar_Tab", 1, sqlp, conn);
      sCoffeeName = sCoffeeName.substring(1, sCoffeeName.length() - 1);
      msg.setMsg("String to be updated  " + sCoffeeName);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_In_Null(?)}");
      cstmt.setObject(1, sCoffeeName, java.sql.Types.VARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      oRetVal = (String) rs.getObject(1);

      oRetVal.trim();
      sCoffeeName.trim();

      msg.addOutputMsg(sCoffeeName, oRetVal);
      if (oRetVal.equals(sCoffeeName)) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed!");

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
   * @testName: testSetObject52
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:13; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type LONGVARCHAR and call statement.executeQuery(String sql) method and
   * call ResultSet.getObject(int column). It should return a String object that
   * is been set. Compare the result with the extracted value from the
   * tssql.stmt file. Both the values should be equal.
   *
   */
  public void testSetObject52() throws Fault {
    ResultSet rs = null;
    String oRetVal = null;
    String sCoffeeName = null;

    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      sCoffeeName = rsSch.extractVal("Longvarchar_Tab", 1, sqlp, conn);
      sCoffeeName = sCoffeeName.substring(1, sCoffeeName.length() - 1);
      msg.setMsg("String to be updated  " + sCoffeeName);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Lvarchar_In_Null(?)}");
      cstmt.setObject(1, sCoffeeName, java.sql.Types.LONGVARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      oRetVal = (String) rs.getObject(1);
      oRetVal = oRetVal.trim();
      sCoffeeName = sCoffeeName.trim();

      msg.addOutputMsg(sCoffeeName, oRetVal);
      if (oRetVal.equals(sCoffeeName)) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed!");

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
   * @testName: testSetObject56
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:14;JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type DATE and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Date object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject56() throws Fault {
    ResultSet rs = null;
    java.util.Date oRetVal = null;
    java.sql.Date mfgDateVal = null;
    String sMfgDateVal = null;
    try {
      rsSch.createTab("Date_Tab", sqlp, conn);

      // to convert String value into java.sql.Date value
      sMfgDateVal = rsSch.extractVal("Date_Tab", 1, sqlp, conn);
      sMfgDateVal = sMfgDateVal.substring(sMfgDateVal.indexOf('\'') + 1,
          sMfgDateVal.lastIndexOf('\''));
      mfgDateVal = java.sql.Date.valueOf(sMfgDateVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Date_In_Null(?)}");
      cstmt.setObject(1, sMfgDateVal, java.sql.Types.DATE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Date_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      oRetVal = (java.util.Date) rs.getObject(1);

      long lDate = oRetVal.getTime();
      java.sql.Date dbDate = new java.sql.Date(lDate);

      msg.addOutputMsg("" + mfgDateVal, "" + dbDate);

      if (dbDate.compareTo(mfgDateVal) == 0) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed!");

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
   * @testName: testSetObject57
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:15; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type TIME and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Time object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject57() throws Fault {
    ResultSet rs = null;
    java.util.Date oRetVal = null;
    java.sql.Time brkTimeVal = null;
    String sBrkTimeVal = null;
    try {
      rsSch.createTab("Time_Tab", sqlp, conn);

      msg.setMsg("extract  Value of Time to be Updated");
      sBrkTimeVal = rsSch.extractVal("Time_Tab", 1, sqlp, conn);
      sBrkTimeVal = sBrkTimeVal.substring(sBrkTimeVal.indexOf('\'') + 1,
          sBrkTimeVal.lastIndexOf('\''));

      brkTimeVal = Time.valueOf(sBrkTimeVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Time_In_Null(?)}");
      cstmt.setObject(1, sBrkTimeVal, java.sql.Types.TIME);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Time_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      oRetVal = (java.util.Date) rs.getObject(1);

      long lDate = oRetVal.getTime();
      java.sql.Time dbDate = new java.sql.Time(lDate);

      msg.addOutputMsg("" + brkTimeVal, "" + dbDate);

      if (dbDate.toString().trim().equals(brkTimeVal.toString().trim())) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed!");

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
   * @testName: testSetObject58
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:16; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type TIMESTAMP and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Timestamp object that
   * is been set. Compare the result with the extracted value from the
   * tssql.stmt file. Both the values should be equal.
   *
   */
  public void testSetObject58() throws Fault {
    ResultSet rs = null;
    java.sql.Timestamp oRetVal = null;
    java.sql.Timestamp inTimeVal = null;
    String sInTimeVal = null;
    try {
      rsSch.createTab("Timestamp_Tab", sqlp, conn);

      // to convert String value into java.sql.Timestamp value
      sInTimeVal = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);
      sInTimeVal = sInTimeVal.substring(sInTimeVal.indexOf('\'') + 1,
          sInTimeVal.lastIndexOf('\''));
      inTimeVal = Timestamp.valueOf(sInTimeVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Timestamp_In_Null(?)}");
      cstmt.setObject(1, sInTimeVal, java.sql.Types.TIMESTAMP);

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
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed!");

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

  /*
   * @testName: testSetObject59
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:2; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Tinyint_Tab with the maximum value
   * of the Tinyint_Tab. Execute a query to retrieve the Min_Val from
   * Tinyint_Tab. Compare the returned value with the maximum value extracted
   * from tssql.stmt file. Both of them should be equal
   */
  public void testSetObject59() throws Fault {
    ResultSet rs = null;
    Integer maxTinyintVal = null;
    BigDecimal maxBigDecimalVal = null;
    String sTinyintVal = null;
    String smaxStringVal = null;
    try {

      rsSch.createTab("Tinyint_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_In_Min(?)}");

      msg.setMsg("extract  Maximum Value of Tinyint to be Updated");
      smaxStringVal = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);
      maxBigDecimalVal = new BigDecimal(smaxStringVal);
      maxTinyintVal = new Integer(smaxStringVal);

      cstmt.setObject(1, maxBigDecimalVal, java.sql.Types.TINYINT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Tinyint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      sTinyintVal = "" + rs.getObject(1);
      msg.addOutputMsg(smaxStringVal, sTinyintVal);

      if (sTinyintVal.trim().equals(smaxStringVal.trim())) {
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
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject60
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:2; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database Using the IN parameter of that
   * object,update the column Null_Val of the Tinyint_Tab with the minimum value
   * of the Tinyint_Tab. Execute a query to retrieve the Null_Val from
   * Tinyint_Tab. Compare the returned value with the minimum value extracted
   * from tssql.stmt file,Both of them should be equal
   */
  public void testSetObject60() throws Fault {
    ResultSet rs = null;
    Integer minTinyintVal = null;
    BigDecimal minBigDecimalVal = null;
    String sTinyintVal = null;
    String sminStringVal = null;
    try {

      rsSch.createTab("Tinyint_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_In_Null(?)}");

      msg.setMsg("extract  Maximum Value of Tinyint to be Updated");
      sminStringVal = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);
      minBigDecimalVal = new BigDecimal(sminStringVal);
      minTinyintVal = new Integer(sminStringVal);

      cstmt.setObject(1, minBigDecimalVal, java.sql.Types.TINYINT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Tinyint_Query_Null", "");
      msg.setMsg("Query String :" + Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      sTinyintVal = "" + rs.getObject(1);
      msg.addOutputMsg(sminStringVal, sTinyintVal);

      if (sTinyintVal.trim().equals(sminStringVal.trim())) {
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
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
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
