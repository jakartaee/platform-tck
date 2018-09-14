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
 * @(#)callStmtClient19.java	1.20 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt19;

import java.io.*;
import java.util.*;
import java.math.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

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
 * The callStmtClient19 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class callStmtClient19 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt19";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private DataSource ds1 = null;

  private rsSchema rsSch = null;

  private String drManager = null;

  private Properties sqlp = null;

  private CallableStatement cstmt = null;

  private transient DatabaseMetaData dbmd = null;

  private Statement stmt = null;

  private ResultSet rs = null;

  private JDBCTestMsg msg = null;
  /* Run test in standalone mode */

  public static void main(String[] args) {
    callStmtClient19 theTests = new callStmtClient19();
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
        stmt = conn.createStatement(/*
                                     * ResultSet.TYPE_SCROLL_INSENSITIVE,
                                     * ResultSet.CONCUR_READ_ONLY
                                     */);
        dbmd = conn.getMetaData();
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
   * @testName: testSetObject221
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set Date object for SQL
   * Type Date and call statement.executeQuery method and call getObject method
   * of ResultSet. It should return a String object that is been set.
   *
   */
  public void testSetObject221() throws Fault {

    java.sql.Date oRetVal = null;
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
      cstmt.setObject(1, mfgDateVal, java.sql.Types.DATE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Date_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      java.util.Date dRetVal = (java.util.Date) rs.getObject(1);
      long ld = dRetVal.getTime();

      oRetVal = new java.sql.Date(ld);

      msg.addOutputMsg("" + mfgDateVal, "" + oRetVal);

      if (oRetVal.compareTo(mfgDateVal) == 0) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed");

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
   * @testName: testSetObject223
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set Time object for SQL
   * Type Char and call statement.executeQuery method and call getObject method
   * of ResultSet. It should return a String object that is been set.
   *
   */
  public void testSetObject223() throws Fault {

    java.sql.Time oRetVal = null;
    java.sql.Time brkTimeVal = null;
    String sBrkTimeVal = null;
    String retStringVal = null;
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);

      msg.setMsg("extract the Value of Time to be Updated");
      sBrkTimeVal = rsSch.extractVal("Time_Tab", 1, sqlp, conn);
      sBrkTimeVal = sBrkTimeVal.substring(sBrkTimeVal.indexOf('\'') + 1,
          sBrkTimeVal.lastIndexOf('\''));
      brkTimeVal = Time.valueOf(sBrkTimeVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_In_Null(?)}");
      cstmt.setObject(1, brkTimeVal, java.sql.Types.CHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      retStringVal = (String) rs.getObject(1);
      retStringVal = retStringVal.trim();

      oRetVal = java.sql.Time.valueOf(retStringVal);

      msg.addOutputMsg("" + brkTimeVal, "" + oRetVal);
      if (oRetVal.equals(brkTimeVal)) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to registerOutParameter is Failed!");

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
   * @testName: testSetObject224
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set Time object for SQL
   * Type Varchar and call statement.executeQuery method and call getObject
   * method of ResultSet. It should return a String object that is been set.
   *
   */
  public void testSetObject224() throws Fault {

    java.sql.Time oRetVal = null;
    java.sql.Time brkTimeVal = null;
    String sBrkTimeVal = null;
    String retStringVal = null;
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      msg.setMsg("extract the Value of Time to be Updated");
      sBrkTimeVal = rsSch.extractVal("Time_Tab", 1, sqlp, conn);
      sBrkTimeVal = sBrkTimeVal.substring(sBrkTimeVal.indexOf('\'') + 1,
          sBrkTimeVal.lastIndexOf('\''));
      brkTimeVal = Time.valueOf(sBrkTimeVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_In_Null(?)}");
      cstmt.setObject(1, brkTimeVal, java.sql.Types.VARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      retStringVal = (String) rs.getObject(1);

      oRetVal = java.sql.Time.valueOf(retStringVal);

      msg.addOutputMsg("" + brkTimeVal, "" + oRetVal);

      if (oRetVal.equals(brkTimeVal)) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed");

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
   * @testName: testSetObject225
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set Time object for SQL
   * Type Longvarchar and call statement.executeQuery method and call getObject
   * method of ResultSet. It should return a String object that is been set.
   *
   */
  public void testSetObject225() throws Fault {

    java.sql.Time oRetVal = null;
    java.sql.Time brkTimeVal = null;
    String sBrkTimeVal = null;
    String retStringVal = null;
    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      msg.setMsg("extract the Value of Time to be Updated");
      sBrkTimeVal = rsSch.extractVal("Time_Tab", 1, sqlp, conn);
      sBrkTimeVal = sBrkTimeVal.substring(sBrkTimeVal.indexOf('\'') + 1,
          sBrkTimeVal.lastIndexOf('\''));
      sBrkTimeVal = sBrkTimeVal.trim();

      brkTimeVal = Time.valueOf(sBrkTimeVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Lvarchar_In_Null(?)}");
      cstmt.setObject(1, brkTimeVal, java.sql.Types.LONGVARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      retStringVal = "" + rs.getObject(1);
      msg.addOutputMsg(sBrkTimeVal, retStringVal);

      if (retStringVal.equals(sBrkTimeVal)) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed");

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
   * @testName: testSetObject226
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set Time object for SQL
   * Type Time and call statement.executeQuery method and call getObject method
   * of ResultSet. It should return a String object that is been set.
   *
   */
  public void testSetObject226() throws Fault {

    java.sql.Time oRetVal = null;
    java.sql.Time brkTimeVal = null;
    String sBrkTimeVal = null;
    try {
      rsSch.createTab("Time_Tab", sqlp, conn);

      msg.setMsg("extract the Value of Time to be Updated");
      sBrkTimeVal = rsSch.extractVal("Time_Tab", 1, sqlp, conn);
      sBrkTimeVal = sBrkTimeVal.substring(sBrkTimeVal.indexOf('\'') + 1,
          sBrkTimeVal.lastIndexOf('\''));

      brkTimeVal = Time.valueOf(sBrkTimeVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Time_In_Null(?)}");
      cstmt.setObject(1, brkTimeVal, java.sql.Types.TIME);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Time_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      java.util.Date dRetVal = (java.util.Date) rs.getObject(1);
      long lDate = dRetVal.getTime();
      oRetVal = new java.sql.Time(lDate);

      msg.addOutputMsg("" + brkTimeVal, "" + oRetVal);
      if (oRetVal.toString().trim().equals(brkTimeVal.toString().trim())) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed");

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
   * @testName: testSetObject227
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set Timestamp object for
   * SQL Type Char and call statement.executeQuery method and call getObject
   * method of ResultSet. It should return a String object that is been set.
   *
   */
  public void testSetObject227() throws Fault {

    java.sql.Timestamp oRetVal = null;
    java.sql.Timestamp inTimeVal = null;
    String sInTimeVal = null;
    String retStringVal = null;
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);

      // to convert String value into java.sql.Timestamp value
      sInTimeVal = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);
      sInTimeVal = sInTimeVal.substring(sInTimeVal.indexOf('\'') + 1,
          sInTimeVal.lastIndexOf('\''));
      inTimeVal = Timestamp.valueOf(sInTimeVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_In_Null(?)}");
      cstmt.setObject(1, inTimeVal, java.sql.Types.CHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      retStringVal = (String) rs.getObject(1);

      oRetVal = java.sql.Timestamp.valueOf(retStringVal);
      msg.addOutputMsg("" + inTimeVal, "" + oRetVal);

      if (oRetVal.equals(inTimeVal)) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed");

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
   * @testName: testSetObject228
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set Timestamp object for
   * SQL Type Varchar and call statement.executeQuery method and call getObject
   * method of ResultSet. It should return a String object that is been set.
   *
   */
  public void testSetObject228() throws Fault {

    java.sql.Timestamp oRetVal = null;
    java.sql.Timestamp inTimeVal = null;
    String sInTimeVal = null;
    String retStringVal = null;
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      // to convert String value into java.sql.Timestamp value
      sInTimeVal = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);
      sInTimeVal = sInTimeVal.substring(sInTimeVal.indexOf('\'') + 1,
          sInTimeVal.lastIndexOf('\''));
      inTimeVal = Timestamp.valueOf(sInTimeVal);
      msg.setMsg("Timestamp value to be updated   :   " + inTimeVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_In_Null(?)}");
      cstmt.setObject(1, inTimeVal, java.sql.Types.VARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      retStringVal = (String) rs.getObject(1);

      oRetVal = java.sql.Timestamp.valueOf(retStringVal);

      msg.addOutputMsg("" + inTimeVal, "" + oRetVal);
      if (oRetVal.equals(inTimeVal)) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed");

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
   * @testName: testSetObject229
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set Timestamp object for
   * SQL Type Longvarchar and call statement.executeQuery method and call
   * getObject method of ResultSet. It should return a String object that is
   * been set.
   *
   */
  public void testSetObject229() throws Fault {

    java.sql.Timestamp oRetVal = null;
    java.sql.Timestamp inTimeVal = null;
    String sInTimeVal = null;
    String retStringVal = null;
    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      // to convert String value into java.sql.Timestamp value
      sInTimeVal = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);
      sInTimeVal = sInTimeVal.substring(sInTimeVal.indexOf('\'') + 1,
          sInTimeVal.lastIndexOf('\''));
      sInTimeVal = sInTimeVal.trim();
      inTimeVal = Timestamp.valueOf(sInTimeVal);
      msg.setMsg("Timestamp value to be updated   :   " + inTimeVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Lvarchar_In_Null(?)}");
      cstmt.setObject(1, inTimeVal, java.sql.Types.LONGVARCHAR);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      retStringVal = (String) rs.getObject(1);
      retStringVal = retStringVal.trim();

      oRetVal = java.sql.Timestamp.valueOf(retStringVal);

      msg.addOutputMsg("" + inTimeVal, "" + oRetVal);
      if (oRetVal.equals(inTimeVal)) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed");

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
   * @testName: testSetObject231
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set Timestamp object for
   * SQL Type Time and call statement.executeQuery method and call getObject
   * method of ResultSet. It should return a String object that is been set.
   *
   */
  public void testSetObject231() throws Fault {
    java.sql.Time brkTimeVal = null;
    String sBrkStringVal = null;
    java.sql.Time rTimeVal = null;
    Timestamp brkTimestampVal = null;

    try {
      // to create the Time table
      rsSch.createTab("Time_Tab", sqlp, conn);

      msg.setMsg("extract the Value of Timestamp to be Updated");
      sBrkStringVal = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);
      sBrkStringVal = sBrkStringVal.substring(sBrkStringVal.indexOf('\'') + 1,
          sBrkStringVal.lastIndexOf('\''));
      brkTimestampVal = Timestamp.valueOf(sBrkStringVal);
      msg.setMsg("Timestamp Value :" + brkTimestampVal);

      sBrkStringVal = sBrkStringVal.substring(sBrkStringVal.indexOf(' ') + 1,
          sBrkStringVal.length());
      brkTimeVal = java.sql.Time.valueOf(sBrkStringVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Time_In_Null(?)}");
      cstmt.setObject(1, brkTimestampVal, java.sql.Types.TIME);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Time_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      java.util.Date oTimeVal = (java.util.Date) rs.getObject(1);
      long lDate = oTimeVal.getTime();
      rTimeVal = new java.sql.Time(lDate);

      msg.addOutputMsg("" + brkTimeVal, "" + rTimeVal);

      if (rTimeVal.toString().trim().equals(brkTimeVal.toString().trim())) {
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
      msg.printSQLError(sqle, "Call to setObject method is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject method is Failed!");

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
        rsSch.dropTab("Time_Tab", conn);
      } catch (Exception e) {
      }
    }

  }

  /*
   * @testName: testSetObject232
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set Timestamp object for
   * SQL Type Timestamp and call statement.executeQuery method and call
   * getObject method of ResultSet. It should return a String object that is
   * been set.
   *
   */
  public void testSetObject232() throws Fault {

    java.sql.Timestamp oRetVal = null;
    java.sql.Timestamp inTimeVal = null;
    String sInTimeVal = null;
    try {
      rsSch.createTab("Timestamp_Tab", sqlp, conn);

      // to convert String value into java.sql.Timestamp value
      sInTimeVal = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);
      sInTimeVal = sInTimeVal.substring(sInTimeVal.indexOf('\'') + 1,
          sInTimeVal.lastIndexOf('\''));
      inTimeVal = java.sql.Timestamp.valueOf(sInTimeVal);
      msg.setMsg("Timestamp value to be updated   :   " + inTimeVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Timestamp_In_Null(?)}");
      cstmt.setObject(1, inTimeVal, java.sql.Types.TIMESTAMP);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      String Null_Val_Query = sqlp.getProperty("Timestamp_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      oRetVal = (java.sql.Timestamp) rs.getObject(1);

      msg.addOutputMsg("" + inTimeVal, "" + oRetVal);
      if (oRetVal.compareTo(inTimeVal) == 0) {
        msg.setMsg(
            "setObject method sets the designated parameter with the object");
      } else {
        msg.printTestError(
            "setObject method does not set the designated parameter with the object",
            "test setObject Failed");

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
   * @testName: testRegisterOutParameter01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1237;
   * JDBC:JAVADOC:1238; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setBigDecimal(int
   * parameterIndex, int jdbcType) method to set maximum BigDecimal value in
   * null column and call registerOutParameter(int parameterIndex,int jdbcType,
   * int scale) method and call getBigDecimal method. It should return a
   * BigDecimal object that is been set. (Note: This test case also checks the
   * support for INOUT parameter in Stored Procedure)
   *
   */
  public void testRegisterOutParameter01() throws Fault {
    String sMaxNumericVal = null;
    BigDecimal maxDecimalVal = null;
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      sMaxNumericVal = rsSch.extractVal("Numeric_Tab", 1, sqlp, conn);
      maxDecimalVal = new BigDecimal(sMaxNumericVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Numeric_Io_Null(?)}");
      cstmt.setBigDecimal(1, maxDecimalVal);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.NUMERIC, 15);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getBigDecimal method");
      BigDecimal oRetVal = cstmt.getBigDecimal(1);

      msg.addOutputMsg("" + maxDecimalVal, "" + oRetVal);
      if ((maxDecimalVal.compareTo(oRetVal) == 0))
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to registerOutParameter is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to registerOutParameter is Failed!");

    } finally {
      try {
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
   * @testName: testRegisterOutParameter02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1237;
   * JDBC:JAVADOC:1238; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setBigDecimal(int
   * parameterIndex, int jdbcType) method to set minimum BigDecimal value in
   * maximum value column and call registerOutParameter(int parameterIndex,int
   * jdbcType,int scale) method and call getBigDecimal method. It should return
   * a BigDecimal object that is been set. (Note: This test case also checks the
   * support for INOUT parameter in Stored Procedure)
   *
   */
  public void testRegisterOutParameter02() throws Fault {
    String sMinNumericVal = null;
    BigDecimal minDecimalVal = null;
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      sMinNumericVal = rsSch.extractVal("Numeric_Tab", 2, sqlp, conn);
      minDecimalVal = new BigDecimal(sMinNumericVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Numeric_Io_Max(?)}");
      cstmt.setBigDecimal(1, minDecimalVal);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.NUMERIC, 15);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getBigDecimal method");
      BigDecimal oRetVal = cstmt.getBigDecimal(1);

      msg.addOutputMsg("" + minDecimalVal, "" + oRetVal);
      if ((minDecimalVal.compareTo(oRetVal) == 0))
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to registerOutParameter is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to registerOutParameter is Failed!");

    } finally {
      try {
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
   * @testName: testRegisterOutParameter03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1237;
   * JDBC:JAVADOC:1238; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setBigDecimal(int
   * parameterIndex, int jdbcType) method to set maximum Decimal value in null
   * column and call registerOutParameter(int parameterIndex,int jdbcType, int
   * scale) method and call getBigDecimal method. It should return a BigDecimal
   * object that is been set. (Note: This test case also checks the support for
   * INOUT parameter in Stored Procedure)
   *
   */
  public void testRegisterOutParameter03() throws Fault {
    String sMaxDecimalVal = null;
    BigDecimal maxDecimalVal = null;
    BigDecimal oRetVal = null;
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      sMaxDecimalVal = rsSch.extractVal("Decimal_Tab", 1, sqlp, conn);
      maxDecimalVal = new BigDecimal(sMaxDecimalVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_Io_Null(?)}");
      cstmt.setBigDecimal(1, maxDecimalVal);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DECIMAL, 15);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getBigDecimal method");
      oRetVal = cstmt.getBigDecimal(1);

      msg.addOutputMsg("" + maxDecimalVal, "" + oRetVal);
      if ((maxDecimalVal.compareTo(oRetVal) == 0))
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to registerOutParameter is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to registerOutParameter is Failed!");

    } finally {
      try {
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
   * @testName: testRegisterOutParameter04
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1237;
   * JDBC:JAVADOC:1238; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setBigDecimal(int
   * parameterIndex, int jdbcType) method to set minimum Decimal value in
   * maximum value column in Decimal table and call registerOutParameter(int
   * parameterIndex,int jdbcType,int scale) method and call getBigDecimal
   * method. It should return a BigDecimal object that is been set. (Note: This
   * test case also checks the support for INOUT parameter in Stored Procedure)
   *
   */
  public void testRegisterOutParameter04() throws Fault {
    String sMinDecimalVal = null;
    BigDecimal minDecimalVal = null;
    BigDecimal oRetVal = null;
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      sMinDecimalVal = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);
      minDecimalVal = new BigDecimal(sMinDecimalVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_Io_Max(?)}");
      cstmt.setBigDecimal(1, minDecimalVal);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DECIMAL, 15);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getBigDecimal method");
      oRetVal = cstmt.getBigDecimal(1);

      msg.addOutputMsg("" + minDecimalVal, "" + oRetVal);
      if ((minDecimalVal.compareTo(oRetVal) == 0))
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to registerOutParameter is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to registerOutParameter is Failed!");

    } finally {
      try {
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
   * @testName: testRegisterOutParameter05
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setDouble(int
   * parameterIndex, int jdbcType) method to set maximum Double value in null
   * column and call registerOutParameter(int parameterIndex,int jdbcType)
   * method and call getDouble method. It should return a double value that is
   * been set. (Note: This test case also checks the support for INOUT parameter
   * in Stored Procedure)
   *
   */
  public void testRegisterOutParameter05() throws Fault {
    String sMaxDoubleVal = null;
    try {
      rsSch.createTab("Double_Tab", sqlp, conn);

      sMaxDoubleVal = rsSch.extractVal("Double_Tab", 1, sqlp, conn);
      double maxDoubleVal = Double.parseDouble(sMaxDoubleVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_Io_Null(?)}");
      cstmt.setDouble(1, maxDoubleVal);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DOUBLE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getDouble method");
      double oRetVal = cstmt.getDouble(1);

      msg.addOutputMsg("" + maxDoubleVal, "" + oRetVal);
      if (maxDoubleVal == oRetVal)
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to registerOutParameter is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to registerOutParameter is Failed!");

    } finally {
      try {
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
   * @testName: testRegisterOutParameter06
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setDouble(int
   * parameterIndex, int jdbcType) method to set minimum double value in maximum
   * value column in Double table and call registerOutParameter(int
   * parameterIndex,int jdbcType) method and call getDouble method. It should
   * return a double value that is been set. (Note: This test case also checks
   * the support for INOUT parameter in Stored Procedure)
   *
   */
  public void testRegisterOutParameter06() throws Fault {
    try {
      rsSch.createTab("Double_Tab", sqlp, conn);

      String sMinDoubleVal = rsSch.extractVal("Double_Tab", 2, sqlp, conn);
      double minDoubleVal = Double.parseDouble(sMinDoubleVal);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_Io_Max(?)}");
      cstmt.setDouble(1, minDoubleVal);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DOUBLE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getDouble method");
      double oRetVal = cstmt.getDouble(1);

      msg.addOutputMsg("" + minDoubleVal, "" + oRetVal);
      if (minDoubleVal == oRetVal)
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to registerOutParameter is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to registerOutParameter is Failed!");

    } finally {
      try {
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
   * @testName: testRegisterOutParameter07
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setDouble(int
   * parameterIndex, int jdbcType) method to set maximum Float value in null
   * column and call registerOutParameter method and call getDouble method. It
   * should return a double value that is been set. (Note: This test case also
   * checks the support for INOUT parameter in Stored Procedure)
   *
   */
  public void testRegisterOutParameter07() throws Fault {
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);

      String sMaxFloatVal = rsSch.extractVal("Float_Tab", 1, sqlp, conn);
      double maxFloatVal = Double.parseDouble(sMaxFloatVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Float_Io_Null(?)}");
      cstmt.setDouble(1, maxFloatVal);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DOUBLE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getDouble method");
      double oRetVal = cstmt.getDouble(1);

      msg.addOutputMsg("" + maxFloatVal, "" + oRetVal);
      if (maxFloatVal == oRetVal)
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to registerOutParameter is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to registerOutParameter is Failed!");

    } finally {
      try {
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
   * @testName: testRegisterOutParameter08
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setDouble() method to
   * set minimum float value in maximum value column in Float table and call
   * registerOutParameter(int parameterIndex,int jdbcType) method and call
   * getDouble method. It should return a double value that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter08() throws Fault {
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);

      String sMinFloatVal = rsSch.extractVal("Float_Tab", 2, sqlp, conn);
      double minFloatVal = Double.parseDouble(sMinFloatVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Float_Io_Max(?)}");
      cstmt.setDouble(1, minFloatVal);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DOUBLE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getDouble method");
      double oRetVal = cstmt.getDouble(1);

      msg.addOutputMsg("" + minFloatVal, "" + oRetVal);
      if (minFloatVal == oRetVal)
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to registerOutParameter is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to registerOutParameter is Failed!");

    } finally {
      try {
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
