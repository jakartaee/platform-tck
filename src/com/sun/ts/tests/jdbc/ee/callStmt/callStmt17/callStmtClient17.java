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
 * @(#)callStmtClient17.java	1.21 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt17;

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
 * The callStmtClient17 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class callStmtClient17 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt17";

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
    callStmtClient17 theTests = new callStmtClient17();
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
   * @testName: testSetObject183
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the Name column of Char_Tab with the maximum
   * value of Float_Tab. Call the getObject(int columnno) method to retrieve
   * this value. Extract the maximum value from the tssql.stmt file. Compare
   * this value with the value returned by the getObject(int columnno) method.
   * Both the values should be equal.
   */
  public void testSetObject183() throws Fault {
    Float maxFloatVal;
    String rStringVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_In_Name(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Float_Tab", 1, sqlp, conn);
      maxFloatVal = Float.valueOf(smaxStringVal);

      // to set the Float
      cstmt.setObject(1, maxFloatVal, java.sql.Types.CHAR);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Char_Query_Name", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);

      msg.addOutputMsg(maxFloatVal.toString(),
          Float.valueOf(rStringVal).toString());

      if (Utils.isMatchingFloatingPointVal(maxFloatVal,
          Float.valueOf(rStringVal))) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
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
        rsSch.dropTab("Char_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject184
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType) method,update the column Null_Val of Char_Tab with the
   * minimum value of Float_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject184() throws Fault {
    Float minFloatVal;
    String rStringVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Float_Tab", 2, sqlp, conn);
      minFloatVal = Float.valueOf(sminStringVal);
      // to set the Value
      cstmt.setObject(1, minFloatVal, java.sql.Types.CHAR);

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

      msg.addOutputMsg(minFloatVal.toString(),
          Float.valueOf(rStringVal).toString());

      if (Utils.isMatchingFloatingPointVal(minFloatVal,
          Float.valueOf(rStringVal))) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
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
        rsSch.dropTab("Char_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject185
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the Name column of Varchar_Tab with the
   * maximum value of Float_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject185() throws Fault {
    Float maxFloatVal;
    String rStringVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_In_Name(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Float_Tab", 1, sqlp, conn);
      maxFloatVal = Float.valueOf(smaxStringVal);

      // to set the Float
      cstmt.setObject(1, smaxStringVal, java.sql.Types.VARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      String Max_Val_Query = sqlp.getProperty("Varchar_Query_Name", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);

      msg.addOutputMsg(maxFloatVal.toString(),
          Float.valueOf(rStringVal).toString());

      if (Utils.isMatchingFloatingPointVal(maxFloatVal,
          Float.valueOf(rStringVal))) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
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

      }
    }
  }

  /*
   * @testName: testSetObject186
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType) method,update the column Null_Val of Varchar_Tab with the
   * minimum value of Float_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject186() throws Fault {
    Float minFloatVal;
    String rStringVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Float_Tab", 2, sqlp, conn);
      minFloatVal = Float.valueOf(sminStringVal);
      // to set the Value
      cstmt.setObject(1, minFloatVal, java.sql.Types.VARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rStringVal = (String) rs.getObject(1);

      msg.addOutputMsg(minFloatVal.toString(),
          Float.valueOf(rStringVal).toString());

      if (Utils.isMatchingFloatingPointVal(minFloatVal,
          Float.valueOf(rStringVal))) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
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

      }
    }
  }

  /*
   * @testName: testSetObject187
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the Name column of Longvarchar_Tab with the
   * maximum value of Float_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject187() throws Fault {
    Float maxFloatVal;
    String rStringVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Longvarchar_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Lvarchar_In_Name(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Float_Tab", 1, sqlp, conn);
      maxFloatVal = Float.valueOf(smaxStringVal);

      // to set the Float
      cstmt.setObject(1, maxFloatVal, java.sql.Types.LONGVARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      String Max_Val_Query = sqlp.getProperty("Longvarchar_Query_Name", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);

      msg.addOutputMsg(maxFloatVal.toString(),
          Float.valueOf(rStringVal).toString());

      if (Utils.isMatchingFloatingPointVal(maxFloatVal,
          Float.valueOf(rStringVal))) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
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
        rsSch.dropTab("Longvarchar_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject188
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType) method,update the column Null_Val of Longvarchar_Tab with
   * the minimum value of Float_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject188() throws Fault {
    Float minFloatVal;
    String rStringVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Lvarchar_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Float_Tab", 2, sqlp, conn);
      minFloatVal = Float.valueOf(sminStringVal);
      // to set the Value
      cstmt.setObject(1, minFloatVal, java.sql.Types.LONGVARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);

      msg.addOutputMsg(minFloatVal.toString(),
          Float.valueOf(rStringVal).toString());

      if (Utils.isMatchingFloatingPointVal(minFloatVal,
          Float.valueOf(rStringVal))) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
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

      }
    }
  }

  /*
   * @testName: testSetObject189
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Min_Val of Tinyint_Tab with the
   * maximum value of Tinyint_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject189() throws Fault {
    Double maxDoubleVal;
    Integer rIntegerVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_In_Min(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);
      Integer maxIntegerVal = new Integer(smaxStringVal);
      maxDoubleVal = new Double(smaxStringVal);

      // to set the Long
      cstmt.setObject(1, maxDoubleVal, java.sql.Types.TINYINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      String Max_Val_Query = sqlp.getProperty("Tinyint_Query_Min", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      Object oIntegerVal = rs.getObject(1);
      rIntegerVal = new Integer(oIntegerVal.toString());

      msg.addOutputMsg("" + maxIntegerVal, "" + rIntegerVal);
      if (rIntegerVal.compareTo(maxIntegerVal) == 0) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
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

      }
    }
  }

  /*
   * @testName: testSetObject190
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType) method,update the column Null_Val of Tinyint_Tab with the
   * minimum value of Tinyint_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject190() throws Fault {
    Double minDoubleVal;
    Integer rIntegerVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);
      Integer minIntegerVal = new Integer(sminStringVal);
      minDoubleVal = new Double(sminStringVal);

      // to set the Value
      cstmt.setObject(1, minDoubleVal, java.sql.Types.TINYINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Tinyint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object oIntegerVal = rs.getObject(1);
      rIntegerVal = new Integer(oIntegerVal.toString());

      msg.addOutputMsg("" + minIntegerVal, "" + rIntegerVal);
      if (rIntegerVal.compareTo(minIntegerVal) == 0) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
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

      }
    }
  }

  /*
   * @testName: testSetObject191
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Min_Val of Smallint_Tab with the
   * maximum value of Smallint_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject191() throws Fault {
    Double maxDoubleVal;
    Integer rIntegerVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_In_Min(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Smallint_Tab", 1, sqlp, conn);
      Integer maxIntegerVal = new Integer(smaxStringVal);
      maxDoubleVal = new Double(smaxStringVal);

      // to set the Double
      cstmt.setObject(1, maxDoubleVal, java.sql.Types.SMALLINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Smallint_Query_Min", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      Object oIntegerVal = rs.getObject(1);
      rIntegerVal = new Integer(oIntegerVal.toString());

      msg.addOutputMsg("" + maxIntegerVal, "" + rIntegerVal);
      if (rIntegerVal.compareTo(maxIntegerVal) == 0) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
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

      }
    }
  }

  /*
   * @testName: testSetObject192
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType) method,update the column Null_Val of Smallint_Tab with the
   * minimum value of Smallint_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject192() throws Fault {
    Double minDoubleVal;
    Integer rIntegerVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Smallint_Tab", 2, sqlp, conn);
      Integer minIntegerVal = new Integer(sminStringVal);
      minDoubleVal = new Double(sminStringVal);

      // to set the Value
      cstmt.setObject(1, minDoubleVal, java.sql.Types.SMALLINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      String Null_Val_Query = sqlp.getProperty("Smallint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object oIntegerVal = rs.getObject(1);
      rIntegerVal = new Integer(oIntegerVal.toString());

      msg.addOutputMsg("" + minIntegerVal, "" + rIntegerVal);
      if (rIntegerVal.compareTo(minIntegerVal) == 0) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
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

      }
    }
  }

  /*
   * @testName: testSetObject193
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Min_Val of Integer_Tab with the
   * maximum value of Integer_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject193() throws Fault {
    Double maxDoubleVal;
    Integer rIntegerVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_In_Min(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);
      Integer maxIntegerVal = new Integer(smaxStringVal);
      maxDoubleVal = new Double(smaxStringVal);

      // to set the Double
      cstmt.setObject(1, maxDoubleVal, java.sql.Types.INTEGER);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      String Max_Val_Query = sqlp.getProperty("Integer_Query_Min", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      Object oIntegerVal = rs.getObject(1);
      rIntegerVal = new Integer(oIntegerVal.toString());

      msg.addOutputMsg("" + maxIntegerVal, "" + rIntegerVal);
      if (rIntegerVal.compareTo(maxIntegerVal) == 0) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
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

      }
    }
  }

  /*
   * @testName: testSetObject194
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType) method,update the column Null_Val of Integer_Tab with the
   * minimum value of Integer_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject194() throws Fault {
    Double minDoubleVal;
    Integer rIntegerVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);
      Integer minIntegerVal = new Integer(sminStringVal);
      minDoubleVal = new Double(sminStringVal);

      // to set the Value
      cstmt.setObject(1, minDoubleVal, java.sql.Types.INTEGER);
      msg.setMsg("execute the procedure");
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
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
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

      }
    }
  }

  /*
   * @testName: testSetObject195
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Min_Val of Bigint_Tab with some
   * Long value after converting it to Double. Call the getObject(int columnno)
   * method to retrieve this value. Compare this value with the value being sent
   * to the database. Both the values should be equal.
   */
  public void testSetObject195() throws Fault {
    Double maxDoubleVal;
    Long rLongVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_In_Min(?)}");

      long longVal = 1000;
      smaxStringVal = new Long(longVal).toString();

      Long maxLongVal = new Long(smaxStringVal);
      maxDoubleVal = new Double(smaxStringVal);

      // to set the Double
      cstmt.setObject(1, maxDoubleVal, java.sql.Types.BIGINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Bigint_Query_Min", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      Object oLongVal = rs.getObject(1);
      rLongVal = new Long(oLongVal.toString());

      msg.addOutputMsg("" + maxLongVal, "" + rLongVal);
      if (rLongVal.compareTo(maxLongVal) == 0) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
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

      }
    }
  }

  /*
   * @testName: testSetObject197
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Min_Val of Real_Tab with the
   * maximum value of Real_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject197() throws Fault {
    Double maxDoubleVal;
    Float rFloatVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_In_Min(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Real_Tab", 1, sqlp, conn);
      Float maxFloatVal = new Float(smaxStringVal);
      maxDoubleVal = new Double(smaxStringVal);

      // to set the Double
      cstmt.setObject(1, maxDoubleVal, java.sql.Types.REAL);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Real_Query_Min", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      Object oFloatVal = rs.getObject(1);
      rFloatVal = new Float(oFloatVal.toString());
      msg.addOutputMsg("" + maxFloatVal, "" + rFloatVal);

      if (rFloatVal.compareTo(maxFloatVal) == 0) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
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

      }
    }
  }

  /*
   * @testName: testSetObject198
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType) method,update the column Null_Val of Real_Tab with the
   * minimum value of Real_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject198() throws Fault {
    Double minDoubleVal;
    Float rFloatVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Real_Tab", 2, sqlp, conn);
      Float minFloatVal = new Float(sminStringVal);
      minDoubleVal = new Double(sminStringVal);

      // to set the Value
      cstmt.setObject(1, minDoubleVal, java.sql.Types.REAL);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Real_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object oFloatVal = rs.getObject(1);
      rFloatVal = new Float(oFloatVal.toString());

      msg.addOutputMsg("" + minFloatVal, "" + rFloatVal);
      if (rFloatVal.compareTo(minFloatVal) == 0) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
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

      }
    }
  }

  /*
   * @testName: testSetObject199
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Min_Val of Float_Tab with the
   * maximum value of Float_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject199() throws Fault {
    Double maxDoubleVal;
    Double rDoubleVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Float_In_Min(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Float_Tab", 1, sqlp, conn);
      maxDoubleVal = new Double(smaxStringVal);
      // to set the Double
      cstmt.setObject(1, maxDoubleVal, java.sql.Types.FLOAT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Float_Query_Min", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      rDoubleVal = (Double) rs.getObject(1);

      msg.addOutputMsg("" + maxDoubleVal, "" + rDoubleVal);
      if (rDoubleVal.equals(maxDoubleVal)) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
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

      }
    }
  }

  /*
   * @testName: testSetObject200
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
  public void testSetObject200() throws Fault {
    Double minDoubleVal;
    Double rDoubleVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Float_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Float_Tab", 2, sqlp, conn);
      minDoubleVal = new Double(sminStringVal);

      // to set the Value
      cstmt.setObject(1, minDoubleVal, java.sql.Types.FLOAT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Float_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rDoubleVal = (Double) rs.getObject(1);

      msg.addOutputMsg("" + minDoubleVal, "" + rDoubleVal);
      if (rDoubleVal.equals(minDoubleVal)) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
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
