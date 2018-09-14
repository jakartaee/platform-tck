/*
 * Copyright (c) 2006, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)callStmtClient18.java	1.19 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt18;

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
 * The callStmtClient18 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class callStmtClient18 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt18";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private ResultSet rs = null;

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
    callStmtClient18 theTests = new callStmtClient18();
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
        dbmd = conn.getMetaData();
        stmt = conn.createStatement();
        rsSch = new rsSchema();
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
   * @testName: testSetObject201
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Min_Val of Double_Tab with the
   * maximum value of Double_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method.Both the values should be equal.
   */
  public void testSetObject201() throws Fault {
    Double maxDoubleVal;
    Double rDoubleVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Double_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_In_Min(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Double_Tab", 1, sqlp, conn);
      maxDoubleVal = new Double(smaxStringVal);
      // to set the Double
      cstmt.setObject(1, maxDoubleVal, java.sql.Types.DOUBLE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Double_Query_Min", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      rDoubleVal = (Double) rs.getObject(1);

      msg.addOutputMsg("" + maxDoubleVal, "" + rDoubleVal);
      if (rDoubleVal.equals(maxDoubleVal)) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "call to setObject is Failed!");

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

      }
    }
  }

  /*
   * @testName: testSetObject202
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType) method,update the column Null_Val of Double_Tab with the
   * minimum value of Double_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject202() throws Fault {
    Double minDoubleVal;
    Double rDoubleVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Double_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Double_Tab", 2, sqlp, conn);
      minDoubleVal = new Double(sminStringVal);

      // to set the Value
      cstmt.setObject(1, minDoubleVal, java.sql.Types.DOUBLE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Double_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rDoubleVal = (Double) rs.getObject(1);

      msg.addOutputMsg("" + minDoubleVal, "" + rDoubleVal);
      if (rDoubleVal.equals(minDoubleVal)) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "call to setObject is Failed!");

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

      }
    }
  }

  /*
   * @testName: testSetObject203
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Min_Val of Decimal_Tab with the
   * maximum value of Decimal_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject203() throws Fault {
    Double maxDoubleVal;
    BigDecimal rDecimalVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_In_Null(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);
      BigDecimal maxDecimalVal = new BigDecimal(smaxStringVal);
      maxDoubleVal = new Double(smaxStringVal);

      // to set the Double
      cstmt.setObject(1, maxDoubleVal, java.sql.Types.DECIMAL, 15);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Decimal_Query_Null", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      rDecimalVal = (BigDecimal) rs.getObject(1);

      msg.addOutputMsg("" + maxDecimalVal, "" + rDecimalVal);
      if (rDecimalVal.compareTo(maxDecimalVal) == 0) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "call to setObject is Failed!");

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

      }
    }
  }

  /*
   * @testName: testSetObject204
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType) method,update the column Null_Val of Decimal_Tab with the
   * minimum value of Decimal_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject204() throws Fault {
    Double minDoubleVal;
    BigDecimal rDecimalVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);
      BigDecimal minDecimalVal = new BigDecimal(sminStringVal);
      minDoubleVal = new Double(sminStringVal);

      // to set the Value
      cstmt.setObject(1, minDoubleVal, java.sql.Types.DECIMAL, 15);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Decimal_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rDecimalVal = (BigDecimal) rs.getObject(1);

      msg.addOutputMsg("" + minDecimalVal, "" + rDecimalVal);
      if (rDecimalVal.compareTo(minDecimalVal) == 0) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "call to setObject is Failed!");

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

      }
    }
  }

  /*
   * @testName: testSetObject205
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Min_Val of Numeric_Tab with the
   * maximum value of Numeric_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject205() throws Fault {
    Double maxDoubleVal;
    BigDecimal rDecimalVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Numeric_In_Null(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Numeric_Tab", 2, sqlp, conn);
      BigDecimal maxDecimalVal = new BigDecimal(smaxStringVal);
      maxDoubleVal = new Double(smaxStringVal);

      // to set the Double
      cstmt.setObject(1, maxDoubleVal, java.sql.Types.NUMERIC, 15);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Numeric_Query_Null", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      rDecimalVal = (BigDecimal) rs.getObject(1);

      msg.addOutputMsg("" + maxDecimalVal, "" + rDecimalVal);
      if (rDecimalVal.compareTo(maxDecimalVal) == 0) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "call to setObject is Failed!");

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

      }
    }
  }

  /*
   * @testName: testSetObject206
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType) method,update the column Null_Val of Numeric_Tab with the
   * minimum value of Numeric_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject206() throws Fault {
    Double minDoubleVal;
    BigDecimal rDecimalVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Numeric_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Numeric_Tab", 2, sqlp, conn);
      BigDecimal minDecimalVal = new BigDecimal(sminStringVal);
      minDoubleVal = new Double(sminStringVal);

      // to set the Value
      cstmt.setObject(1, minDoubleVal, java.sql.Types.NUMERIC, 15);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Numeric_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rDecimalVal = (BigDecimal) rs.getObject(1);

      msg.addOutputMsg("" + minDecimalVal, "" + rDecimalVal);
      if (rDecimalVal.compareTo(minDecimalVal) == 0) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "call to setObject is Failed!");

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

      }
    }
  }

  /*
   * @testName: testSetObject209
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the Name column of Char_Tab with the maximum
   * value of Double_Tab. Call the getObject(int columnno) method to retrieve
   * this value. Extract the maximum value from the tssql.stmt file. Compare
   * this value with the value returned by the getObject(int columnno) method.
   * Both the values should be equal.
   */
  public void testSetObject209() throws Fault {
    Double maxDoubleVal;
    String rStringVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_In_Name(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Double_Tab", 1, sqlp, conn);
      maxDoubleVal = Double.valueOf(smaxStringVal);

      // to set the Double
      cstmt.setObject(1, maxDoubleVal, java.sql.Types.CHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Char_Query_Name", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);

      msg.addOutputMsg(maxDoubleVal.toString(),
          Double.valueOf(rStringVal).toString());

      if (Utils.isMatchingFloatingPointVal(maxDoubleVal,
          Double.valueOf(rStringVal))) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "call to setObject is Failed!");

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

      }
    }
  }

  /*
   * @testName: testSetObject210
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType) method,update the column Null_Val of Char_Tab with the
   * minimum value of Double_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject210() throws Fault {
    Double minDoubleVal;
    String rStringVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Double_Tab", 2, sqlp, conn);
      minDoubleVal = Double.valueOf(sminStringVal);

      // to set the Value
      cstmt.setObject(1, minDoubleVal, java.sql.Types.CHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);

      msg.addOutputMsg(minDoubleVal.toString(),
          Double.valueOf(rStringVal).toString());

      if (Utils.isMatchingFloatingPointVal(minDoubleVal,
          Double.valueOf(rStringVal))) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "call to setObject is Failed!");

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

      }
    }
  }

  /*
   * @testName: testSetObject211
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the Name column of Varchar_Tab with the
   * maximum value of Double_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject211() throws Fault {
    Double maxDoubleVal;
    String rStringVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_In_Name(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Double_Tab", 1, sqlp, conn);
      maxDoubleVal = Double.valueOf(smaxStringVal);

      // to set the Double
      cstmt.setObject(1, maxDoubleVal, java.sql.Types.VARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Varchar_Query_Name", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);

      msg.addOutputMsg(maxDoubleVal.toString(),
          Double.valueOf(rStringVal).toString());

      if (Utils.isMatchingFloatingPointVal(maxDoubleVal,
          Double.valueOf(rStringVal))) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "call to setObject is Failed!");

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

      }
    }
  }

  /*
   * @testName: testSetObject212
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType) method,update the column Null_Val of Varchar_Tab with the
   * minimum value of Double_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject212() throws Fault {
    Double minDoubleVal;
    String rStringVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Double_Tab", 2, sqlp, conn);
      minDoubleVal = Double.valueOf(sminStringVal);
      // to set the Value
      cstmt.setObject(1, minDoubleVal, java.sql.Types.VARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);

      msg.addOutputMsg(minDoubleVal.toString(),
          Double.valueOf(rStringVal).toString());

      if (Utils.isMatchingFloatingPointVal(minDoubleVal,
          Double.valueOf(rStringVal))) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "call to setObject is Failed!");

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

      }
    }
  }

  /*
   * @testName: testSetObject213
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the Name column of Longvarchar_Tab with the
   * maximum value of Double_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject213() throws Fault {
    Double maxDoubleVal;
    String rStringVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Longvarchar_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Lvarchar_In_Name(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Double_Tab", 1, sqlp, conn);
      maxDoubleVal = Double.valueOf(smaxStringVal);

      // to set the Double
      cstmt.setObject(1, maxDoubleVal, java.sql.Types.LONGVARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Longvarchar_Query_Name", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);

      msg.addOutputMsg(maxDoubleVal.toString(),
          Double.valueOf(rStringVal).toString());

      if (Utils.isMatchingFloatingPointVal(maxDoubleVal,
          Double.valueOf(rStringVal))) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "call to setObject is Failed!");

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
        rsSch.dropTab("Longvarchar_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject214
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType) method,update the column Null_Val of Longvarchar_Tab with
   * the minimum value of Double_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject214() throws Fault {
    Double minDoubleVal;
    String rStringVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Lvarchar_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Double_Tab", 2, sqlp, conn);
      minDoubleVal = Double.valueOf(sminStringVal);

      // to set the Value
      cstmt.setObject(1, minDoubleVal, java.sql.Types.LONGVARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);

      msg.addOutputMsg(minDoubleVal.toString(),
          Double.valueOf(rStringVal).toString());

      if (Utils.isMatchingFloatingPointVal(minDoubleVal,
          Double.valueOf(rStringVal))) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "call to setObject is Failed!");

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

      }
    }
  }

  /*
   * @testName: testSetObject215
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set Byte Array object for
   * SQL Type BINARY and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Byte array object that
   * is been set.
   *
   */
  public void testSetObject215() throws Fault {
    ResultSet rs = null;
    byte[] oRetVal = null;
    byte[] bytearrVal = null;
    String binarySize = null;

    try {
      binarySize = sqlp.getProperty("binarySize");
      msg.setMsg("Binary Table Size : " + binarySize);

      rsSch.createTab("Binary_Tab", sqlp, conn);

      int bytearrsize = Integer.parseInt(binarySize);
      msg.setMsg("Binary Size : " + bytearrsize);

      byte[] bytearr = new byte[bytearrsize];
      String sbyteval = null;

      // to get the bytearray value
      for (int count = 0; count < bytearrsize; count++) {
        sbyteval = Integer.toString(count % 255);
        bytearr[count] = Byte.parseByte(sbyteval);
      }

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Binary_Proc_In(?)}");
      cstmt.setObject(1, bytearr, java.sql.Types.BINARY);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Binary_Val_Query = sqlp.getProperty("Binary_Query_Val", "");
      msg.setMsg(Binary_Val_Query);
      rs = stmt.executeQuery(Binary_Val_Query);
      rs.next();
      oRetVal = (byte[]) rs.getObject(1);

      for (int i = 0; i < bytearrsize; i++) {
        msg.addOutputMsg(Byte.toString(bytearr[i]), Byte.toString(oRetVal[i]));
        if (oRetVal[i] != bytearr[i]) {
          msg.printTestError(
              "setObject Method does not set the designated parameter value",
              "test setObject Failed!");

        }
      }
      msg.setMsg(
          "setObject Method sets the designated parameter with the object");
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "call to setObject is Failed!");

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
        rsSch.dropTab("Binary_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject216
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set Byte Array object for
   * SQL Type VARBINARY and call statement.executeQuery(String sql) method and
   * call ResultSet.getObject(int column). It should return a Byte array object
   * that is been set.
   *
   */
  public void testSetObject216() throws Fault {
    ResultSet rs = null;
    byte[] oRetVal = null;
    byte[] bytearrVal = null;
    String varbinarySize = null;

    try {
      varbinarySize = sqlp.getProperty("varbinarySize");
      msg.setMsg("Varbinary Table Size : " + varbinarySize);

      rsSch.createTab("Varbinary_Tab", sqlp, conn);

      int bytearrsize = Integer.parseInt(varbinarySize);
      msg.setMsg("Varbinary Size : " + bytearrsize);

      byte[] bytearr = new byte[bytearrsize];
      String sbyteval = null;

      // to get the bytearray value
      for (int count = 0; count < bytearrsize; count++) {
        sbyteval = Integer.toString(count % 255);
        bytearr[count] = Byte.parseByte(sbyteval);
      }

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varbinary_Proc_In(?)}");
      cstmt.setObject(1, bytearr, java.sql.Types.VARBINARY);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Varbinary_Val_Query = sqlp.getProperty("Varbinary_Query_Val", "");
      msg.setMsg(Varbinary_Val_Query);
      rs = stmt.executeQuery(Varbinary_Val_Query);
      rs.next();
      oRetVal = (byte[]) rs.getObject(1);

      for (int i = 0; i < bytearrsize; i++) {
        msg.addOutputMsg(Byte.toString(bytearr[i]), Byte.toString(oRetVal[i]));
        if (oRetVal[i] != bytearr[i]) {
          msg.printTestError(
              "setObject Method does not set the designated parameter value",
              "test setObject Failed!");

        }
      }
      msg.setMsg(
          "setObject Method sets the designated parameter with the object");
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "call to setObject is Failed!");

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
        rsSch.dropTab("Varbinary_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject217
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set Byte Array object for
   * SQL Type LONGVARBINARY and call statement.executeQuery(String sql) method
   * and call ResultSet.getObject(int column). It should return a Byte array
   * object that is been set.
   *
   */
  public void testSetObject217() throws Fault {
    ResultSet rs = null;
    byte[] oRetVal = null;
    byte[] bytearrVal = null;
    String longvarbinarySize = null;

    try {
      longvarbinarySize = sqlp.getProperty("longvarbinarySize");
      msg.setMsg("Longvarbinary Table Size : " + longvarbinarySize);

      rsSch.createTab("Longvarbinary_Tab", sqlp, conn);

      int bytearrsize = Integer.parseInt(longvarbinarySize);
      msg.setMsg("Varbinary Size : " + bytearrsize);

      byte[] bytearr = new byte[bytearrsize];
      String sbyteval = null;

      // to get the bytearray value
      for (int count = 0; count < bytearrsize; count++) {
        sbyteval = Integer.toString(count % 255);
        bytearr[count] = Byte.parseByte(sbyteval);
      }

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Longvarbinary_In(?)}");
      cstmt.setObject(1, bytearr, java.sql.Types.LONGVARBINARY);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Longvarbinary_Val_Query = sqlp
          .getProperty("Longvarbinary_Query_Val", "");
      msg.setMsg(Longvarbinary_Val_Query);
      rs = stmt.executeQuery(Longvarbinary_Val_Query);
      rs.next();
      oRetVal = (byte[]) rs.getObject(1);

      for (int i = 0; i < bytearrsize; i++) {
        msg.addOutputMsg(Byte.toString(bytearr[i]), Byte.toString(oRetVal[i]));
        if (oRetVal[i] != bytearr[i]) {
          msg.printTestError(
              "setObject Method does not set the designated parameter value",
              "test setObject Failed!");

        }
      }
      msg.setMsg(
          "setObject Method sets the designated parameter with the object");
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "call to setObject is Failed!");

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
        rsSch.dropTab("Longvarbinary_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject218
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set Date object for SQL
   * Type CHAR and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Date object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
  public void testSetObject218() throws Fault {
    ResultSet rs = null;
    java.sql.Date oRetVal = null;
    java.sql.Date mfgDateVal = null;
    String sMfgDateVal = null;
    String retStringVal = null;
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);

      // to convert String value into java.sql.Date value
      sMfgDateVal = rsSch.extractVal("Date_Tab", 1, sqlp, conn);
      sMfgDateVal = sMfgDateVal.substring(sMfgDateVal.indexOf('\'') + 1,
          sMfgDateVal.lastIndexOf('\''));
      mfgDateVal = java.sql.Date.valueOf(sMfgDateVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_In_Null(?)}");
      cstmt.setObject(1, mfgDateVal, java.sql.Types.CHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      retStringVal = (String) rs.getObject(1);
      retStringVal = retStringVal.trim();
      oRetVal = java.sql.Date.valueOf(retStringVal);

      msg.addOutputMsg("" + mfgDateVal, "" + oRetVal);

      if (oRetVal.equals(mfgDateVal)) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "call to setObject is Failed!");

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
      }
    }
  }

  /*
   * @testName: testSetObject219
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set Date object for SQL
   * Type VARCHAR and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a String object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal. *
   */
  public void testSetObject219() throws Fault {
    ResultSet rs = null;
    java.sql.Date oRetVal = null;
    java.sql.Date mfgDateVal = null;
    String sMfgDateVal = null;
    String retStringVal = null;
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      // to convert String value into java.sql.Date value
      sMfgDateVal = rsSch.extractVal("Date_Tab", 1, sqlp, conn);
      sMfgDateVal = sMfgDateVal.substring(sMfgDateVal.indexOf('\'') + 1,
          sMfgDateVal.lastIndexOf('\''));
      mfgDateVal = java.sql.Date.valueOf(sMfgDateVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_In_Null(?)}");
      cstmt.setObject(1, mfgDateVal, java.sql.Types.VARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      retStringVal = (String) rs.getObject(1);

      oRetVal = java.sql.Date.valueOf(retStringVal);

      msg.addOutputMsg("" + mfgDateVal, "" + oRetVal);
      if (oRetVal.equals(mfgDateVal)) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "call to setObject is Failed!");

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
      }
    }
  }

  /*
   * @testName: testSetObject220
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set Date object for SQL
   * Type LONGVARCHAR and call statement.executeQuery(String sql) method and
   * call ResultSet.getObject(int column). It should return a String object that
   * is been set. Compare the result with the extracted value from the
   * tssql.stmt file. Both the values should be equal. *
   */
  public void testSetObject220() throws Fault {
    ResultSet rs = null;
    java.sql.Date oRetVal = null;
    java.sql.Date mfgDateVal = null;
    String sMfgDateVal = null;
    String retStringVal = null;
    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      // to convert String value into java.sql.Date value
      sMfgDateVal = rsSch.extractVal("Date_Tab", 1, sqlp, conn);
      sMfgDateVal = sMfgDateVal.substring(sMfgDateVal.indexOf('\'') + 1,
          sMfgDateVal.lastIndexOf('\''));
      sMfgDateVal = sMfgDateVal.trim();
      mfgDateVal = java.sql.Date.valueOf(sMfgDateVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Lvarchar_In_Null(?)}");
      cstmt.setObject(1, mfgDateVal, java.sql.Types.LONGVARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      retStringVal = (String) rs.getObject(1);
      retStringVal = retStringVal.trim();
      oRetVal = java.sql.Date.valueOf(retStringVal);

      msg.addOutputMsg("" + mfgDateVal, "" + oRetVal);
      if (oRetVal.equals(mfgDateVal)) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "call to setObject is Failed!");

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
