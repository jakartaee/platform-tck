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
 * @(#)callStmtClient22.java	1.16 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt22;

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
 * The callStmtClient22 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class callStmtClient22 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt22";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private DataSource ds1 = null;

  private rsSchema rsSch = null;

  private String drManager = null;

  private Properties sqlp = null;

  private CallableStatement cstmt = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    callStmtClient22 theTests = new callStmtClient22();
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
      } catch (SQLException ex) {
        logErr("SQL Exception : " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testRegisterOutParameter49
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set Date value in null column of Date table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getDate method. It should return a Date object that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter49() throws Fault {
    try {
      rsSch.createTab("Date_Tab", sqlp, conn);

      String sMfgDate = rsSch.extractVal("Date_Tab", 1, sqlp, conn);
      sMfgDate = sMfgDate.substring(sMfgDate.indexOf('\'') + 1,
          sMfgDate.lastIndexOf('\''));
      java.sql.Date mfgDate = java.sql.Date.valueOf(sMfgDate);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Date_Io_Null(?)}");
      cstmt.setObject(1, mfgDate);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DATE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      java.sql.Date oRetVal = (java.sql.Date) cstmt.getObject(1);

      msg.addOutputMsg(" " + mfgDate, "" + oRetVal);
      if (mfgDate.equals(oRetVal))
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
        rsSch.dropTab("Date_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter50
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set Time Object in null column of Time table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getTime method. It should return a Time object that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter50() throws Fault {
    String sBrkTime = null;
    java.sql.Time brkTime = null;
    try {
      rsSch.createTab("Time_Tab", sqlp, conn);

      sBrkTime = rsSch.extractVal("Time_Tab", 1, sqlp, conn);
      sBrkTime = sBrkTime.substring(sBrkTime.indexOf('\'') + 1,
          sBrkTime.lastIndexOf('\''));
      brkTime = java.sql.Time.valueOf(sBrkTime);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Time_Io_Null(?)}");
      cstmt.setObject(1, brkTime);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.TIME);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      java.util.Date dRetVal = (java.util.Date) cstmt.getObject(1);
      long lDate = dRetVal.getTime();
      java.sql.Time oRetVal = new java.sql.Time(lDate);

      msg.addOutputMsg(" " + brkTime, "" + oRetVal);
      if (brkTime.toString().trim().equals(oRetVal.toString().trim()))
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
        rsSch.dropTab("Time_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter51
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set Timestamp value in null column of Timestamp table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getObject method. It should return a Timestamp object that is been set.
   * (Note: This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter51() throws Fault {
    try {
      rsSch.createTab("Timestamp_Tab", sqlp, conn);

      String sInTime = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);
      sInTime = sInTime.substring(sInTime.indexOf('\'') + 1,
          sInTime.lastIndexOf('\''));
      java.sql.Timestamp inTimestamp = Timestamp.valueOf(sInTime);
      msg.setMsg("Timestamp value to be updated   :   " + inTimestamp);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Timestamp_Io_Null(?)}");
      cstmt.setObject(1, inTimestamp);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getObject method");
      java.sql.Timestamp oRetVal = (java.sql.Timestamp) cstmt.getObject(1);

      msg.addOutputMsg(" " + inTimestamp, "" + oRetVal);
      if (inTimestamp.compareTo(oRetVal) == 0)
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
        rsSch.dropTab("Timestamp_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter52
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set Byte Array object in Binary table and call registerOutParameter(int
   * parameterIndex, int jdbcType) method and call getObject method. It should
   * return a Byte Array object that is been set. (Note: This test case also
   * checks the support for INOUT parameter in Stored Procedure)
   *
   */
  public void testRegisterOutParameter52() throws Fault {
    byte retByteArr[] = null;
    boolean byteArrFlag = false;
    String binarySize = null;

    try {
      rsSch.createTab("Binary_Tab", sqlp, conn);

      binarySize = sqlp.getProperty("binarySize");
      msg.setMsg("Binary Table Size : " + binarySize);

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
      cstmt = conn.prepareCall("{call Binary_Proc_Io(?)}");
      cstmt.setObject(1, bytearr);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.BINARY);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      retByteArr = (byte[]) cstmt.getObject(1);

      for (int i = 0; i < bytearrsize; i++) {
        msg.addOutputMsg(Byte.toString(bytearr[i]),
            Byte.toString(retByteArr[i]));
        if (retByteArr[i] != bytearr[i]) {
          msg.printTestError(
              "registerOutParameter does not register the OUT parameter",
              "test registerOutParameter Failed");

        }
      }
      msg.setMsg("registerOutParameter registers the OUT parameter");
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
        rsSch.dropTab("Binary_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter53
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set Byte Array object in Varbinary table and call registerOutParameter(int
   * parameterIndex, int jdbcType) method and call getObject method. It should
   * return a Byte Array object that is been set. (Note: This test case also
   * checks the support for INOUT parameter in Stored Procedure)
   *
   */
  public void testRegisterOutParameter53() throws Fault {
    byte retByteArr[] = null;
    boolean byteArrFlag = false;
    String varbinarySize = null;

    try {
      rsSch.createTab("Varbinary_Tab", sqlp, conn);

      varbinarySize = sqlp.getProperty("varbinarySize");
      msg.setMsg("Varbinary Table Size : " + varbinarySize);

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
      cstmt = conn.prepareCall("{call Varbinary_Proc_Io(?)}");
      cstmt.setObject(1, bytearr);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      retByteArr = (byte[]) cstmt.getObject(1);

      for (int i = 0; i < bytearrsize; i++) {
        msg.addOutputMsg(Byte.toString(bytearr[i]),
            Byte.toString(retByteArr[i]));
        if (retByteArr[i] != bytearr[i]) {
          msg.printTestError(
              "registerOutParameter does not register the OUT parameter",
              "test registerOutParameter Failed");

        }
      }
      msg.setMsg("registerOutParameter registers the OUT parameter");
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
        rsSch.dropTab("Varbinary_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter54
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set Byte Array object in Longvarbinary table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getObject method. It should return a Byte Array object that is been set.
   * (Note: This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter54() throws Fault {
    byte retByteArr[] = null;
    boolean byteArrFlag = false;
    String longvarbinarySize = null;

    try {
      rsSch.createTab("Longvarbinary_Tab", sqlp, conn);

      longvarbinarySize = sqlp.getProperty("longvarbinarySize");
      msg.setMsg("Longvarbinary Table Size : " + longvarbinarySize);

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
      cstmt = conn.prepareCall("{call Longvarbinary_Io(?)}");
      cstmt.setObject(1, bytearr);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.LONGVARBINARY);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getObject method");
      retByteArr = (byte[]) cstmt.getObject(1);

      for (int i = 0; i < bytearrsize; i++) {
        msg.addOutputMsg(Byte.toString(bytearr[i]),
            Byte.toString(retByteArr[i]));
        if (retByteArr[i] != bytearr[i]) {
          msg.printTestError(
              "registerOutParameter does not register the OUT parameter",
              "test registerOutParameter Failed");

        }
      }
      msg.setMsg("registerOutParameter registers the OUT parameter");
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
        rsSch.dropTab("Longvarbinary_Tab", conn);
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
