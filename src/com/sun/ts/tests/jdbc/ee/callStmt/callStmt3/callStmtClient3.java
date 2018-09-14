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
 * @(#)callStmtClient3.java	1.28 07/10/03
 */
package com.sun.ts.tests.jdbc.ee.callStmt.callStmt3;

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
 * The callStmtClient3 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */
public class callStmtClient3 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt3";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;
  // Harness requirements

  private transient Connection conn = null;

  private csSchema csSch = null;

  private rsSchema rsSch = null;

  private JDBCTestMsg msg = null;

  private String drManager = null;

  private Properties sqlp = null;

  private transient DatabaseMetaData dbmd = null;

  private CallableStatement cstmt = null;

  private Properties props = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    callStmtClient3 theTests = new callStmtClient3();
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
      } catch (SQLException ex) {
        logErr("SQL Exception : " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testGetObject01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType,int scale) method. Execute the stored procedure
   * and call the getObject(int parameterIndex) method to retrieve the maximum
   * value of the parameter from Numeric_Tab. Extract the maximum value from the
   * tssql.stmt file.Compare this value with the value returned by the
   * getObject(int parameterIndex) Both the values should be equal.
   */
  public void testGetObject01() throws Fault {
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Numeric_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.NUMERIC, 15);
      cstmt.registerOutParameter(2, java.sql.Types.NUMERIC, 15);
      cstmt.registerOutParameter(3, java.sql.Types.NUMERIC, 15);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      BigDecimal oRetVal = (BigDecimal) cstmt.getObject(1);
      String sRetStr = rsSch.extractVal("Numeric_Tab", 1, sqlp, conn);
      msg.setMsg("extracted maximum value from Numeric_Tab");
      BigDecimal oExtVal = new BigDecimal(sRetStr);

      msg.addOutputMsg(sRetStr, oRetVal.toString());

      if ((oRetVal.compareTo(oExtVal) == 0))
        msg.setMsg("getObject returns the Maximum value for type BigDecimal ");
      else {
        msg.printTestError(
            "getObject did not return the Maximum value for type BigDecimal",
            "Call to getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Numeric_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType,int scale) method. Execute the stored procedure
   * and call the getObject(int parameterIndex) method to retrieve the minimum
   * value of the parameter from Numeric_Tab. Extract the minimum value from the
   * tssql.stmt file.Compare this value with the value returned by the
   * getObject(int parameterIndex) Both the values should be equal.
   */
  public void testGetObject02() throws Fault {
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Numeric_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.NUMERIC, 15);
      cstmt.registerOutParameter(2, java.sql.Types.NUMERIC, 15);
      cstmt.registerOutParameter(3, java.sql.Types.NUMERIC, 15);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      BigDecimal oRetVal = (BigDecimal) cstmt.getObject(2);
      String sRetStr = rsSch.extractVal("Numeric_Tab", 2, sqlp, conn);
      msg.setMsg("extracted minimum value from Numeric_Tab");
      BigDecimal oExtVal = new BigDecimal(sRetStr);

      msg.addOutputMsg(sRetStr, oRetVal.toString());

      if ((oRetVal.compareTo(oExtVal) == 0))
        msg.setMsg("getObject returns the Minimum value for type BigDecimal "
            + oRetVal);
      else {
        msg.printTestError(
            "getObject did not return the Minimum value of type BigDecimal",
            "Call to getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Numeric_Tab", conn);

      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType,int scale) method. Execute the stored procedure
   * and call the getObject(int parameterIndex) method to retrieve the null
   * value from Numeric_Tab. Check if it returns null
   */
  public void testGetObject03() throws Fault {
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Numeric_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.NUMERIC, 15);
      cstmt.registerOutParameter(2, java.sql.Types.NUMERIC, 15);
      cstmt.registerOutParameter(3, java.sql.Types.NUMERIC, 15);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      BigDecimal oRetVal = (BigDecimal) cstmt.getObject(3);
      msg.addOutputMsg("null", "" + oRetVal);

      if (oRetVal == null)
        msg.setMsg(
            "getObject returns the Null value for type BigDecimal " + oRetVal);
      else {
        msg.printTestError(
            "getObject did not return the Null value for type BigDecimal",
            "Call to getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Numeric_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject04
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method.to retrieve the maximum value of
   * the parameter from Float_Tab. Extract the maximum value from the tssql.stmt
   * file.Compare this value with the value returned by the getObject(int
   * parameterIndex) Both the values should be equal.
   */
  public void testGetObject04() throws Fault {
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Float_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
      cstmt.registerOutParameter(2, java.sql.Types.FLOAT);
      cstmt.registerOutParameter(3, java.sql.Types.FLOAT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      String sRetStr = rsSch.extractVal("Float_Tab", 1, sqlp, conn);
      msg.setMsg("extracted maximum value from Float_Tab");
      msg.setMsg(
          "Calling CallableStatement.getObject(Double.MaximumValue(JDBC FLOAT))");

      try {
        msg.setMsg("invoke getObject method");

        Float oRetVal = (Float) cstmt.getObject(1);
        msg.setMsg("It is a Float");
        Float oExtVal = Float.valueOf(sRetStr);
        msg.setMsg("Value retrieved using cstmt	  :" + oRetVal);
        msg.setMsg("Value extracted from ctsql.stmt   :" + oExtVal);
        msg.addOutputMsg(sRetStr, oRetVal.toString());

        if (Utils.isMatchingFloatingPointVal(oExtVal, oRetVal))
          msg.setMsg(
              "getObject returns the Maximum value for type Double(JDBC FLOAT) "
                  + oRetVal);
        else {
          msg.printTestError(
              "getObject did not return the Maximum value for type Double(JDBC FLOAT)",
              "Call to getObject Failed!");

        }
      } catch (ClassCastException ce) {
        msg.setMsg("Assuming it is a Double");
        Double oRetVal = (Double) cstmt.getObject(1);
        Double oExtVal = new Double(sRetStr);
        msg.setMsg("Value retrieved using cstmt	  :" + oRetVal);
        msg.setMsg("Value extracted from ctsql.stmt   :" + oExtVal);
        msg.addOutputMsg(sRetStr, oRetVal.toString());

        if (Utils.isMatchingFloatingPointVal(oExtVal, oRetVal))
          msg.setMsg(
              "getObject returns the Maximum value for type Double(JDBC FLOAT) "
                  + oRetVal);
        else {
          msg.printTestError(
              "getObject did not return the Maximum value for type Double(JDBC FLOAT)",
              "Call to getObject Failed!");

        }

      }

      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject05
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the minimum value of
   * the parameter from Float_Tab. Extract the minimum value from the tssql.stmt
   * file.Compare this value with the value returned by the getObject(int
   * parameterIndex) Both the values should be equal.
   */
  public void testGetObject05() throws Fault {
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Float_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
      cstmt.registerOutParameter(2, java.sql.Types.FLOAT);
      cstmt.registerOutParameter(3, java.sql.Types.FLOAT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "Calling CallableStatement.getObject(Double.MinimumValue(JDBC FLOAT))");
      String sRetStr = rsSch.extractVal("Float_Tab", 2, sqlp, conn);
      msg.setMsg("extracted minimum value from Float_Tab");

      try {
        msg.setMsg("invoke getObject method");
        Float oRetVal = (Float) cstmt.getObject(2);
        msg.setMsg("It is a Float");
        Float oExtVal = Float.valueOf(sRetStr);
        msg.setMsg("From ctssql value is " + sRetStr);
        msg.setMsg("From cstmt value is " + oRetVal);
        msg.addOutputMsg(sRetStr, oRetVal.toString());

        if (Utils.isMatchingFloatingPointVal(oExtVal, oRetVal))
          msg.setMsg(
              "getObject returns the Minimum value for type Double(JDBC FLOAT) "
                  + oRetVal);
        else {
          msg.printTestError(
              "getObject did not return the Minimum value for type Double(JDBC FLOAT)",
              "Call to getObject Failed!");

        }
      } catch (ClassCastException ce) {
        msg.setMsg("invoke getObject method");
        Double oRetVal = (Double) cstmt.getObject(2);
        msg.setMsg("It is a Double");
        Float oExtVal = Float.valueOf(sRetStr);
        msg.setMsg("From tssql value is " + sRetStr);
        msg.setMsg("value retrieved by getObject method is " + oRetVal);
        msg.addOutputMsg(sRetStr, oRetVal.toString());

        if (Utils.isMatchingFloatingPointVal(oExtVal,
            Float.valueOf(oRetVal.floatValue())))
          msg.setMsg(
              "getObject returns the Minimum value for type Double(JDBC FLOAT) "
                  + oRetVal);
        else {
          msg.printTestError(
              "getObject did not return the Minimum value for type Double(JDBC FLOAT)",
              "Call to getObject Failed!");

        }

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject06
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the null value from
   * Float_Tab.Check if it returns null
   */
  public void testGetObject06() throws Fault {
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Float_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.FLOAT);
      cstmt.registerOutParameter(2, java.sql.Types.FLOAT);
      cstmt.registerOutParameter(3, java.sql.Types.FLOAT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Float oRetVal = (Float) cstmt.getObject(3);
      msg.addOutputMsg("null", "" + oRetVal);
      if (oRetVal == null)
        msg.setMsg(
            "getObject returns the Null value for type Double (JDBC FLOAT) "
                + oRetVal);
      else {
        msg.printTestError(
            "getObject did not return the Null value for type Double(JDBC FLOAT)",
            "Call to getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject07
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the maximum value of
   * the parameter from Smallint_Tab. Extract the maximum value from the
   * tssql.stmt file.Compare this value with the value returned by the
   * getObject(int parameterIndex) Both the values should be equal.
   */
  public void testGetObject07() throws Fault {
    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
      cstmt.registerOutParameter(2, java.sql.Types.SMALLINT);
      cstmt.registerOutParameter(3, java.sql.Types.SMALLINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      String sRetVal = "" + cstmt.getObject(1);
      String sRetStr = rsSch.extractVal("Smallint_Tab", 1, sqlp, conn);
      msg.setMsg("extracted maximum value from Smallint_Tab");
      msg.addOutputMsg(sRetStr, sRetVal);

      if (sRetVal.trim().equals(sRetStr.trim()))
        msg.setMsg(
            "getObject returns the Maximum value for type Integer(JDBC SMALLINT) "
                + sRetVal);
      else {
        msg.printTestError(
            "getObject did not return the Maximum value for type Integer(JDBC SMALLINT)",
            "Call to getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject08
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the minimum value of
   * the parameter from Smallint_Tab. Extract the minimum value from the
   * tssql.stmt file.Compare this value with the value returned by the
   * getObject(int parameterIndex) Both the values should be equal.
   */
  public void testGetObject08() throws Fault {
    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
      cstmt.registerOutParameter(2, java.sql.Types.SMALLINT);
      cstmt.registerOutParameter(3, java.sql.Types.SMALLINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      String sRetVal = "" + cstmt.getObject(2);
      String sRetStr = rsSch.extractVal("Smallint_Tab", 2, sqlp, conn);
      msg.setMsg("extracted minimum value from Smallint_Tab");
      msg.addOutputMsg(sRetStr, sRetVal);

      if (sRetVal.trim().equals(sRetStr.trim()))
        msg.setMsg(
            "getObject returns the Minimum value for type Integer(JDBC SMALLINT) "
                + sRetVal);
      else {
        msg.printTestError(
            "getObject did not return the Minimum value for type Integer(JDBC SMALLINT)",
            "Call to getObject Failed!");
      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject09
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the null value from
   * Smallint_Tab.Check if it returns null
   */
  public void testGetObject09() throws Fault {
    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);
      cstmt.registerOutParameter(2, java.sql.Types.SMALLINT);
      cstmt.registerOutParameter(3, java.sql.Types.SMALLINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Integer oRetVal = (Integer) cstmt.getObject(3);
      msg.addOutputMsg("null", "" + oRetVal);

      if (oRetVal == null)
        msg.setMsg(
            "getObject returns the Null value for type Integer(JDBC SMALLINT) "
                + oRetVal);
      else {
        msg.printTestError(
            "getObject did not return the Null value for type Integer(JDBC SMALLINT)",
            "Call to getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject10
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve a char value from
   * Char_Tab.Extract the same char value from the tssql.stmt file.Compare this
   * value with the value returned by the getObject(int parameterIndex).Both the
   * values should be equal.
   */
  public void testGetObject10() throws Fault {
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_Proc(?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.CHAR);
      cstmt.registerOutParameter(2, java.sql.Types.CHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      String oRetVal = (String) cstmt.getObject(1);
      oRetVal = oRetVal.trim();
      String oExtVal = rsSch.extractVal("Char_Tab", 1, sqlp, conn);
      msg.setMsg("extracted char value from Char_Tab");
      oExtVal = oExtVal.trim();
      msg.addOutputMsg(oExtVal, oRetVal);

      if (oRetVal.equals(oExtVal.substring(1, oExtVal.length() - 1)))
        msg.setMsg("getObject returns the Name for type String" + oRetVal);
      else {
        msg.printTestError("getObject did not return the Name for type String",
            "Call to getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Char_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject11
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the null value from
   * Char_Tab.Check if it returns null
   */
  public void testGetObject11() throws Fault {
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_Proc(?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.CHAR);
      cstmt.registerOutParameter(2, java.sql.Types.CHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      String oRetVal = (String) cstmt.getObject(2);
      msg.addOutputMsg("null", oRetVal);
      if (oRetVal == null)
        msg.setMsg(
            "getObject returns the null value for type String " + oRetVal);
      else {
        msg.printTestError(
            "getObject did not return the null value for type String",
            "Call to getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Char_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject12
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the maximum value of
   * the parameter from Integer_Tab. Extract the maximum value from the
   * tssql.stmt file.Compare this value with the value returned by the
   * getObject(int parameterIndex) Both the values should be equal.
   */
  public void testGetObject12() throws Fault {
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
      cstmt.registerOutParameter(2, java.sql.Types.INTEGER);
      cstmt.registerOutParameter(3, java.sql.Types.INTEGER);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Integer oRetVal = (Integer) cstmt.getObject(1);
      String sRetStr = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);
      msg.setMsg("extracted maximum value from Integer_Tab");
      Integer oExtVal = new Integer(sRetStr);
      msg.addOutputMsg(sRetStr, oExtVal.toString());

      if (oRetVal.equals(oExtVal))
        msg.setMsg(
            "getObject returns the Maximum value for type Integer " + oRetVal);
      else {
        msg.printTestError(
            "getObject did not return the Maximum value for type Integer",
            "Call to getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject13
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the minimum value of
   * the parameter from Integer_Tab. Extract the minimum value from the
   * tssql.stmt file.Compare this value with the value returned by the
   * getObject(int parameterIndex) Both the values should be equal.
   */
  public void testGetObject13() throws Fault {
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
      cstmt.registerOutParameter(2, java.sql.Types.INTEGER);
      cstmt.registerOutParameter(3, java.sql.Types.INTEGER);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Integer oRetVal = (Integer) cstmt.getObject(2);
      String sRetStr = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);
      msg.setMsg("extracted minimum value from Integer_Tab");
      Integer oExtVal = new Integer(sRetStr);
      msg.addOutputMsg(sRetStr, oExtVal.toString());

      if (oRetVal.equals(oExtVal))
        msg.setMsg(
            "getObject returns the Minimum value for type Integer " + oRetVal);
      else {
        msg.printTestError(
            "getObject did not return the Minimum value for type Integer",
            "Call to getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject14
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the null value from
   * Integer_Tab.Check if it returns null
   */
  public void testGetObject14() throws Fault {
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
      cstmt.registerOutParameter(2, java.sql.Types.INTEGER);
      cstmt.registerOutParameter(3, java.sql.Types.INTEGER);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Integer oRetVal = (Integer) cstmt.getObject(3);
      msg.addOutputMsg("null", "" + oRetVal);

      if (oRetVal == null)
        msg.setMsg(
            "getObject returns the Null value for type Integer " + oRetVal);
      else {
        msg.printTestError(
            "getObject did not return the Null value for type Integer",
            "Call to getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject15
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the maximum value of
   * the parameter from Bit_Tab. Extract the maximum value from the tssql.stmt
   * file.Compare this value with the value returned by the getObject(int
   * parameterIndex) Both the values should be equal.
   */
  public void testGetObject15() throws Fault {

    Boolean oMaxBooleanVal = null;
    boolean booleanVal = false;
    String smaxBooleanVal = null;
    String rStringVal = null;
    try {
      rsSch.createTab("Bit_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bit_Proc(?,?,?)}");

      smaxBooleanVal = rsSch.extractValAsNumericString("Bit_Tab", 1, sqlp,
          conn);
      msg.setMsg("extracted maximum value from Bit_Tab: " + smaxBooleanVal);
      oMaxBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp, conn);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.BIT);
      cstmt.registerOutParameter(2, java.sql.Types.BIT);
      cstmt.registerOutParameter(3, java.sql.Types.BIT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      rStringVal = cstmt.getObject(1).toString();
      rStringVal = rStringVal.trim();

      if (rStringVal.equals(oMaxBooleanVal.toString())
          || rStringVal.equals(smaxBooleanVal.trim()))
        msg.setMsg("getObject returns the Maximum value for type Boolean "
            + oMaxBooleanVal);
      else {
        msg.printTestError(
            "getObject did not return the Maximum value for type Boolean",
            "Call to getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Bit_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject16
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the minimum value of
   * the parameter from Bit_Tab. Extract the minimum value from the tssql.stmt
   * file.Compare this value with the value returned by the getObject(int
   * parameterIndex) Both the values should be equal.
   */
  public void testGetObject16() throws Fault {
    Boolean oMinBooleanVal = null;
    boolean booleanVal;
    String sminBooleanVal = null;
    String rStringVal = null;
    try {
      rsSch.createTab("Bit_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bit_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.BIT);
      cstmt.registerOutParameter(2, java.sql.Types.BIT);
      cstmt.registerOutParameter(3, java.sql.Types.BIT);

      sminBooleanVal = rsSch.extractVal("Bit_Tab", 2, sqlp, conn);
      msg.setMsg("extracted minimum value from Bit_Tab");

      if (csSch.supportsType("BOOLEAN", conn)) {
        if (sminBooleanVal.equals("1")) {
          // mysql returns "1" for a max bit and not "true"
          sminBooleanVal = "true";
        }
        oMinBooleanVal = new Boolean(sminBooleanVal);
      } else {
        booleanVal = (sminBooleanVal.trim().equals("1")) ? true : false;
        oMinBooleanVal = new Boolean(booleanVal);
      }

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      rStringVal = cstmt.getObject(2).toString();
      rStringVal = rStringVal.trim();

      if (rStringVal.equals(oMinBooleanVal.toString())
          || rStringVal.equals(sminBooleanVal.trim()))
        msg.setMsg("getObject returns the Minimum value for type Boolean "
            + oMinBooleanVal);
      else {
        msg.printTestError(
            "getObject did not return the Minimum value for type Boolean",
            "Call to getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Bit_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject18
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the maximum value of
   * the parameter from Bigint_Tab. Extract the maximum value from the
   * tssql.stmt file.Compare this value with the value returned by the
   * getObject(int parameterIndex) Both the values should be equal.
   */
  public void testGetObject18() throws Fault {
    try {
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.BIGINT);
      cstmt.registerOutParameter(2, java.sql.Types.BIGINT);
      cstmt.registerOutParameter(3, java.sql.Types.BIGINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getObject method");
      Long oRetVal = (Long) cstmt.getObject(1);
      String sRetStr = rsSch.extractVal("Bigint_Tab", 1, sqlp, conn);
      msg.setMsg("extracted maximum value from Bigint_Tab");
      Long oExtVal = new Long(sRetStr);

      msg.addOutputMsg(sRetStr, oExtVal.toString());

      if (oRetVal.equals(oExtVal))
        msg.setMsg(
            "getObject returns the Maximum value for type Long " + oRetVal);
      else {
        msg.printTestError(
            "getObject did not return the Maximum value for type Long",
            "Call to getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Bigint_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject19
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the minimum value of
   * the parameter from Bigint_Tab. Extract the minimum value from the
   * tssql.stmt file.Compare this value with the value returned by the
   * getObject(int parameterIndex) Both the values should be equal.
   */
  public void testGetObject19() throws Fault {
    try {
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.BIGINT);
      cstmt.registerOutParameter(2, java.sql.Types.BIGINT);
      cstmt.registerOutParameter(3, java.sql.Types.BIGINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("Calling CallableStatement.getObject(Long.MinimumValue)");

      msg.setMsg("invoke getObject method");
      Long oRetVal = (Long) cstmt.getObject(2);
      String sRetStr = rsSch.extractVal("Bigint_Tab", 2, sqlp, conn);
      msg.setMsg("extracted minimum value from Bigint_Tab");
      Long oExtVal = new Long(sRetStr);
      msg.addOutputMsg(sRetStr, oRetVal.toString());
      if (oRetVal.equals(oExtVal))
        msg.setMsg(
            "getObject returns the Minimum value for type Long " + oRetVal);
      else {
        msg.printTestError(
            "getObject did not return the Minimum value for type Long",
            "Call to getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Bigint_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject20
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the null value from
   * Bigint_Tab.Check if it returns null
   */
  public void testGetObject20() throws Fault {
    try {
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.BIGINT);
      cstmt.registerOutParameter(2, java.sql.Types.BIGINT);
      cstmt.registerOutParameter(3, java.sql.Types.BIGINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Long oRetVal = (Long) cstmt.getObject(3);
      msg.addOutputMsg("0", "" + oRetVal);

      if (oRetVal == null)
        msg.setMsg("getObject returns the Null value for type Long " + oRetVal);
      else {
        msg.printTestError(
            "getObject did not return the Null value for type Long",
            "Call to getObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getObject Failed!");

    } finally {
      try {
        if (cstmt != null) {
          cstmt.close();
          cstmt = null;
        }
        rsSch.dropTab("Bigint_Tab", conn);
      } catch (Exception e) {
      }
      ;
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
