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
 * @(#)callStmtClient4.java	1.19 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt4;

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
 * The callStmtClient4 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class callStmtClient4 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt4";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements
  private transient Connection conn = null;

  private rsSchema rsSch = null;

  private JDBCTestMsg msg = null;

  private String drManager = null;

  private Properties sqlp = null;

  private transient DatabaseMetaData dbmd = null;

  private CallableStatement cstmt = null;

  private Properties props = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    callStmtClient4 theTests = new callStmtClient4();
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
   * @testName: testGetObject21
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve a Time value from
   * Time_Tab.Extract the same Time value from the tssql.stmt file.Compare this
   * value with the value returned by the getObject(int parameterIndex).Both the
   * values should be equal.
   */
  public void testGetObject21() throws Fault {
    Time oRetVal = null;
    Time nonNullTimeVal = null;
    try {
      rsSch.createTab("Time_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Time_Proc(?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.TIME);
      cstmt.registerOutParameter(2, java.sql.Types.TIME);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      oRetVal = (Time) cstmt.getObject(1);
      String sRetStr = rsSch.extractVal("Time_Tab", 1, sqlp, conn);
      sRetStr = sRetStr.substring(sRetStr.indexOf('\'') + 1,
          sRetStr.lastIndexOf('\''));
      nonNullTimeVal = Time.valueOf(sRetStr);

      msg.addOutputMsg(sRetStr, oRetVal.toString());
      if (oRetVal.equals(nonNullTimeVal))
        msg.setMsg("getObject returns the Break Time for type Time ");
      else {
        msg.printTestError(
            "getObject did not return the proper Break Time for type Time",
            "test getObject Failed!");

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
        rsSch.dropTab("Time_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject22
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the null value from
   * Time_Tab.Check if it returns null
   */
  public void testGetObject22() throws Fault {
    try {
      rsSch.createTab("Time_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Time_Proc(?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.TIME);
      cstmt.registerOutParameter(2, java.sql.Types.TIME);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("Calling CallableStatement.getObject(Time.NullValue)");

      msg.setMsg("invoke getObject method");
      Time oRetVal = (Time) cstmt.getObject(2);

      msg.addOutputMsg("null", "" + oRetVal);

      if (oRetVal == null)
        msg.setMsg("getObject returns the null value for type Time ");
      else {
        msg.printTestError(
            "getObject did not return the null value for type Time",
            "test getObject Failed!");

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
        rsSch.dropTab("Time_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject23
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve a Timestamp value from
   * Timestamp_Tab.Extract the same Timestamp value from the tssql.stmt
   * file.Compare this value with the value returned by the getObject(int
   * parameterIndex).Both the values should be equal.
   */
  public void testGetObject23() throws Fault {
    Timestamp oRetVal = null;
    Timestamp nonNullTimestampVal = null;
    try {
      rsSch.createTab("Timestamp_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Timestamp_Proc(?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
      cstmt.registerOutParameter(2, java.sql.Types.TIMESTAMP);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      oRetVal = (Timestamp) cstmt.getObject(1);
      String sRetStr = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);
      sRetStr = sRetStr.substring(sRetStr.indexOf('\'') + 1,
          sRetStr.lastIndexOf('\''));
      nonNullTimestampVal = Timestamp.valueOf(sRetStr);

      msg.addOutputMsg(sRetStr, oRetVal.toString());
      if (oRetVal.equals(nonNullTimestampVal))
        msg.setMsg(
            "getObject returns the In Time for type Timestamp " + oRetVal);
      else {
        msg.printTestError(
            "getObject did not return the proper In Time for type Timestamp",
            "test getObject Failed!");

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
        rsSch.dropTab("Timestamp_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject24
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the null value from
   * Timestamp_Tab.Check if it returns null
   */
  public void testGetObject24() throws Fault {
    try {
      rsSch.createTab("Timestamp_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Timestamp_Proc(?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
      cstmt.registerOutParameter(2, java.sql.Types.TIMESTAMP);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("Calling CallableStatement.getObject(Timestamp.NullValue)");

      msg.setMsg("invoke getObject method");
      Timestamp oRetVal = (Timestamp) cstmt.getObject(2);
      msg.addOutputMsg("null", "" + oRetVal);

      if (oRetVal == null)
        msg.setMsg("getObject returns the null value for type Timestamp ");
      else {
        msg.printTestError(
            "getObject did not return the null value for type Timestamp",
            "test getObject failed");

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
        rsSch.dropTab("Timestamp_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject25
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve a Date value from from
   * Date_Tab.Extract the Date char value from the tssql.stmt file.Compare this
   * value with the value returned by the getObject(int parameterIndex).Both the
   * values should be equal.
   */
  public void testGetObject25() throws Fault {
    java.sql.Date oRetVal = null;
    try {
      rsSch.createTab("Date_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Date_Proc(?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DATE);
      cstmt.registerOutParameter(2, java.sql.Types.DATE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      oRetVal = (java.sql.Date) cstmt.getObject(1);
      String sRetStr = rsSch.extractVal("Date_Tab", 1, sqlp, conn);
      sRetStr = sRetStr.substring(sRetStr.indexOf('\'') + 1,
          sRetStr.lastIndexOf('\''));
      sRetStr = sRetStr.trim();
      java.sql.Date oExtVal = java.sql.Date.valueOf(sRetStr);

      msg.addOutputMsg(oExtVal.toString(), oRetVal.toString());

      if (oRetVal.toString().equals(oExtVal.toString()))
        msg.setMsg("getObject returns the proper Date for type Date ");
      else {
        msg.printTestError(
            "getObject did not return the proper Date for type Date",
            "test getObject Failed!");

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
        rsSch.dropTab("Date_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject26
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the null value from
   * Date_Tab.Check if it returns null
   */
  public void testGetObject26() throws Fault {
    try {
      rsSch.createTab("Date_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Date_Proc(?,?)}");
      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DATE);
      cstmt.registerOutParameter(2, java.sql.Types.DATE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      java.sql.Date oRetVal = (java.sql.Date) cstmt.getObject(2);
      msg.addOutputMsg("null", "" + oRetVal);

      if (oRetVal == null)
        msg.setMsg("getObject returns the null value for type Date ");
      else {
        msg.printTestError(
            "getObject did not return the null value for type Date",
            "test getObject Failed!");
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
        rsSch.dropTab("Date_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject27
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the maximum value from
   * Tinyint_Tab. Extract the maximum value from the tssql.stmt file.Compare
   * this value with the value returned by the getObject(int parameterIndex)
   * Both the values should be equal.
   */
  public void testGetObject27() throws Fault {
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.TINYINT);
      cstmt.registerOutParameter(2, java.sql.Types.TINYINT);
      cstmt.registerOutParameter(3, java.sql.Types.TINYINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Object obj = cstmt.getObject(1);
      Byte oRetVal = new Byte(obj.toString());

      String sRetStr = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);
      Byte oExtVal = new Byte(sRetStr);

      msg.addOutputMsg(sRetStr, oRetVal.toString());

      if ((oRetVal.compareTo(oExtVal)) == 0)
        msg.setMsg("getObject returns the Maximum Value for type TINYINT ");
      else {
        msg.printTestError(
            "getObject did not return the Maximum Value for type TINYINT ",
            "test getObject failed!");

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
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject28
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the minimum value from
   * Tinyint_Tab. Extract the minimum value from the tssql.stmt file.Compare
   * this value with the value returned by the getObject(int parameterIndex)
   * Both the values should be equal.
   */
  public void testGetObject28() throws Fault {
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.TINYINT);
      cstmt.registerOutParameter(2, java.sql.Types.TINYINT);
      cstmt.registerOutParameter(3, java.sql.Types.TINYINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Object obj = cstmt.getObject(2);
      Byte oRetVal = new Byte(obj.toString());

      String sRetStr = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);
      Byte oExtVal = new Byte(sRetStr);

      msg.addOutputMsg(sRetStr, oRetVal.toString());

      if ((oRetVal.compareTo(oExtVal)) == 0)
        msg.setMsg(
            "getObject returns the Minimum Value for type TINYINT " + oRetVal);
      else {
        msg.printTestError(
            "getObject did not return the Minimum Value for type TINYINT ",
            "test getObject Failed!");

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
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject29
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the null value from
   * Tinyint_Tab.Check if it returns null
   */
  public void testGetObject29() throws Fault {
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.TINYINT);
      cstmt.registerOutParameter(2, java.sql.Types.TINYINT);
      cstmt.registerOutParameter(3, java.sql.Types.TINYINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Object oRetVal = cstmt.getObject(3);
      msg.addOutputMsg("null", "" + oRetVal);

      if (oRetVal == null)
        msg.setMsg(
            "getObject returns the Null Value for type Byte(JDBC TINYINT) ");
      else {
        msg.printTestError(
            "getObject did not return the Null Value for type Byte(JDBC TINYINT)",
            "test getObject Failed!");

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
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject30
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the maximum value from
   * Double_Tab. Extract the maximum value from the tssql.stmt file.Compare this
   * value with the value returned by the getObject(int parameterIndex) Both the
   * values should be equal.
   */
  public void testGetObject30() throws Fault {
    try {
      rsSch.createTab("Double_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DOUBLE);
      cstmt.registerOutParameter(2, java.sql.Types.DOUBLE);
      cstmt.registerOutParameter(3, java.sql.Types.DOUBLE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Double oRetVal = (Double) cstmt.getObject(1);
      String sRetStr = rsSch.extractVal("Double_Tab", 1, sqlp, conn);
      Double oExtVal = new Double(sRetStr);

      msg.addOutputMsg(sRetStr, oRetVal.toString());
      if (oRetVal.equals(oExtVal))
        msg.setMsg(
            "getObject returns the Maximum Value for type Double(JDBC DOUBLE) ");
      else {
        msg.printTestError(
            "getObject did not return the Maximum Value for type Double(JDBC DOUBLE)",
            "test getObject Failed!");

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
        rsSch.dropTab("Double_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject31
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the minimum value of
   * the parameter from Double_Tab. Extract the minimum value from the
   * tssql.stmt file.Compare this value with the value returned by the
   * getObject(int parameterIndex) Both the values should be equal.
   */
  public void testGetObject31() throws Fault {
    try {
      rsSch.createTab("Double_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DOUBLE);
      cstmt.registerOutParameter(2, java.sql.Types.DOUBLE);
      cstmt.registerOutParameter(3, java.sql.Types.DOUBLE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "Calling CallableStatement.getObject(Double.MinimumValue(JDBC DOUBLE))");

      msg.setMsg("invoke getObject method");
      Double oRetVal = (Double) cstmt.getObject(2);
      String sRetStr = rsSch.extractVal("Double_Tab", 2, sqlp, conn);
      Double oExtVal = new Double(sRetStr);

      msg.addOutputMsg(sRetStr, oRetVal.toString());

      if (oRetVal.equals(oExtVal))
        msg.setMsg(
            "getObject returns the Minimum Value for type Double(JDBC DOUBLE) ");
      else {
        msg.printTestError(
            "getObject did not return the Minimum Value for type Double(JDBC DOUBLE)",
            "test getObject Failed!");

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
        rsSch.dropTab("Double_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject32
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the null value from
   * Double_Tab.Check if it returns null
   */
  public void testGetObject32() throws Fault {
    try {
      rsSch.createTab("Double_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DOUBLE);
      cstmt.registerOutParameter(2, java.sql.Types.DOUBLE);
      cstmt.registerOutParameter(3, java.sql.Types.DOUBLE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Double oRetVal = (Double) cstmt.getObject(3);
      msg.addOutputMsg("null", "" + oRetVal);

      if (oRetVal == null)
        msg.setMsg(
            "getObject returns the Null Value for type Double(JDBC DOUBLE) ");
      else {
        msg.printTestError(
            "getObject did not return the Null Value for type Double(JDBC DOUBLE)",
            "test getObject Failed!");

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
        rsSch.dropTab("Double_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject33
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the maximum value from
   * Real_Tab. Extract the maximum value from the tssql.stmt file.Compare this
   * value with the value returned by the getObject(int parameterIndex) Both the
   * values should be equal.
   */
  public void testGetObject33() throws Fault {
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.REAL);
      cstmt.registerOutParameter(2, java.sql.Types.REAL);
      cstmt.registerOutParameter(3, java.sql.Types.REAL);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("Calling CallableStatement.getObject(Float.MaximumValue)");

      msg.setMsg("invoke getObject method");
      Float oRetVal = (Float) cstmt.getObject(1);
      String sRetStr = rsSch.extractVal("Real_Tab", 1, sqlp, conn);
      Float oExtVal = new Float(sRetStr);

      msg.addOutputMsg(sRetStr, oRetVal.toString());
      if (oRetVal.equals(oExtVal))
        msg.setMsg("getObject returns the Maximum Value for type Float ");
      else {
        msg.printTestError(
            "getObject did not return the Maximum Value for type Float",
            "test getObject Failed!");

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
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject34
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the minimum value from
   * Real_Tab. Extract the minimum value from the tssql.stmt file.Compare this
   * value with the value returned by the getObject(int parameterIndex) Both the
   * values should be equal.
   */
  public void testGetObject34() throws Fault {
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.REAL);
      cstmt.registerOutParameter(2, java.sql.Types.REAL);
      cstmt.registerOutParameter(3, java.sql.Types.REAL);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Float oRetVal = (Float) cstmt.getObject(2);
      String sRetStr = rsSch.extractVal("Real_Tab", 2, sqlp, conn);
      Float oExtVal = new Float(sRetStr);

      msg.addOutputMsg(sRetStr, oRetVal.toString());

      if (oRetVal.equals(oExtVal))
        msg.setMsg("getObject returns the Minimum Value for type Float ");
      else {
        msg.printTestError(
            "getObject did not return the Minimum Value for type Float",
            "test getObject Failed!");

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
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject35
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the null value from
   * Real_Tab.Check if it returns null
   */
  public void testGetObject35() throws Fault {
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.REAL);
      cstmt.registerOutParameter(2, java.sql.Types.REAL);
      cstmt.registerOutParameter(3, java.sql.Types.REAL);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Float oRetVal = (Float) cstmt.getObject(3);
      msg.addOutputMsg("null", "" + oRetVal);

      if (oRetVal == null)
        msg.setMsg("getObject returns the Null Value for type Float ");
      else {
        msg.printTestError(
            "getObject did not return the Null Value for type Float",
            "test getObject Failed!");

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
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject36
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve a Varchar value from
   * Varchar_Tab.Extract the same Varchar value from the tssql.stmt file.Compare
   * this value with the value returned by the getObject(int
   * parameterIndex).Both the values should be equal.
   */
  public void testGetObject36() throws Fault {
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_Proc(?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
      cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      String oRetVal = (String) cstmt.getObject(1);
      String oExtVal = rsSch.extractVal("Varchar_Tab", 1, sqlp, conn);
      oExtVal = oExtVal.trim();
      oRetVal = oRetVal.trim();

      msg.addOutputMsg(oExtVal, oRetVal);

      if (oRetVal.equals(oExtVal.substring(1, oExtVal.length() - 1)))
        msg.setMsg("getObject returns the Name for type String(JDBC VARCHAR) ");
      else {
        msg.printTestError(
            "getObject did not return the Name for type String(JDBC VARCHAR)",
            "test getObject Failed!");

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
        rsSch.dropTab("Varchar_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject37
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the null value from
   * Varchar_Tab.Check if it returns null
   */
  public void testGetObject37() throws Fault {
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_Proc(?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
      cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      String oRetVal = (String) cstmt.getObject(2);
      msg.addOutputMsg("null", "" + oRetVal);

      if (oRetVal == null)
        msg.setMsg(
            "getObject returns the null value for type String(JDBC VARCHAR) ");
      else {
        msg.printTestError(
            "getObject did not return the null value for type String(JDBC VARCHAR)",
            "test getObject Failed!");

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
        rsSch.dropTab("Varchar_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject38
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve a Longvarchar value
   * from Longvarchar_Tab.Extract the same Longvarchar value from the tssql.stmt
   * file.Compare this value with the value returned by the getObject(int
   * parameterIndex).Both the values should be equal.
   */
  public void testGetObject38() throws Fault {
    try {
      rsSch.createTab("Longvarchar_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Longvarchar_Proc(?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.LONGVARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      String oRetVal = (String) cstmt.getObject(1);
      String oExtVal = rsSch.extractVal("Longvarchar_Tab", 1, sqlp, conn);
      oExtVal = oExtVal.trim();
      oRetVal = oRetVal.trim();
      msg.addOutputMsg(oExtVal, oRetVal);

      if (oRetVal.equals(oExtVal.substring(1, oExtVal.length() - 1)))
        msg.setMsg(
            "getObject returns the Name for type String(JDBC LONGVARCHAR) ");
      else {
        msg.printTestError(
            "getObject did not return the Name for type String(JDBC LONGVARCHAR)",
            "test getObject Failed!");

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
        rsSch.dropTab("Longvarchar_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject39
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType) method. Execute the stored procedure and call
   * the getObject(int parameterIndex) method to retrieve the null value from
   * Longvarchar_Tab.Check if it returns null
   */
  public void testGetObject39() throws Fault {
    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Lvarcharnull_Proc(?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.LONGVARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "Calling CallableStatement.getObject(String.NullValue(JDBC LONGVARCHAR))");

      msg.setMsg("invoke getObject method");
      String oRetVal = (String) cstmt.getObject(1);
      msg.addOutputMsg("null", oRetVal);

      if (oRetVal == null)
        msg.setMsg(
            "getObject returns the null value for type String(JDBC LONGVARCHAR) ");
      else {
        msg.printTestError(
            "getObject did not return the null value for type String(JDBC LONGVARCHAR)",
            "test getObject Failed!");

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
        rsSch.dropTab("Longvarcharnull_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetObject40
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
   * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Register the parameter using registerOutParameter(int
   * parameterIndex,int sqlType,int scale) method. Execute the stored procedure
   * and call the getObject(int parameterIndex) method to retrieve the maximum
   * value from Decimal_Tab. Extract the maximum value from the tssql.stmt
   * file.Compare this value with the value returned by the getObject(int
   * parameterIndex) Both the values should be equal.
   */
  public void testGetObject40() throws Fault {
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_Proc(?,?,?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DECIMAL, 15);
      cstmt.registerOutParameter(2, java.sql.Types.DECIMAL, 15);
      cstmt.registerOutParameter(3, java.sql.Types.DECIMAL, 15);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      BigDecimal oRetVal = (BigDecimal) cstmt.getObject(1);
      String sRetStr = rsSch.extractVal("Decimal_Tab", 1, sqlp, conn);
      BigDecimal oExtVal = new BigDecimal(sRetStr);
      msg.addOutputMsg(sRetStr, oRetVal.toString());

      if ((oRetVal.compareTo(oExtVal) == 0))
        msg.setMsg(
            "getObject returns the Maximum value for type BigDecimal(JDBC DECIMAL) ");
      else {
        msg.printTestError(
            "getObject did not return the Maximum value for type BigDecimal(JDBC DECIMAL)",
            "test getObject Failed!");

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
        rsSch.dropTab("Decimal_Tab", conn);
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
