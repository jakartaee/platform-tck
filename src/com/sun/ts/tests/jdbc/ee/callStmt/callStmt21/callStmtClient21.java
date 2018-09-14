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
 * @(#)callStmtClient21.java	1.27 07/10/03
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt21;

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
 * The callStmtClient21 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class callStmtClient21 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt21";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private DataSource ds1 = null;

  private csSchema csSch = null;

  private rsSchema rsSch = null;

  private String drManager = null;

  private Properties sqlp = null;

  private CallableStatement cstmt = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    callStmtClient21 theTests = new callStmtClient21();
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
      } catch (SQLException ex) {
        logErr("SQL Exception : " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testRegisterOutParameter29
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setBytes() method to
   * set Longvarbinary value in Longvarbinary table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getBytes method. It should return a Byte Array object that is been set.
   * (Note: This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter29() throws Fault {
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
      cstmt.setBytes(1, bytearr);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.LONGVARBINARY);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getBytes method");
      retByteArr = cstmt.getBytes(1);

      for (int i = 0; i < bytearrsize; i++) {

        msg.addOutputMsg(Byte.toString(bytearr[i]),
            Byte.toString(retByteArr[i]));
        if (retByteArr[i] != bytearr[i]) {
          msg.printTestError(
              "registerOutParameter does not register the OUT parameter",
              "test registerOutParameter Failed!");

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

  /*
   * @testName: testRegisterOutParameter30
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set maximum Double value in null column and call registerOutParameter
   * method and call getObject method. It should return a Double object that is
   * been set. (Note: This test case also checks the support for INOUT parameter
   * in Stored Procedure)
   *
   */
  public void testRegisterOutParameter30() throws Fault {
    try {
      rsSch.createTab("Double_Tab", sqlp, conn);

      String sMaxDoubleVal = rsSch.extractVal("Double_Tab", 1, sqlp, conn);
      Double maxDoubleVal = new Double(sMaxDoubleVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_Io_Null(?)}");
      cstmt.setDouble(1, maxDoubleVal.doubleValue());

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DOUBLE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Double oRetVal = (Double) cstmt.getObject(1);

      msg.addOutputMsg("" + maxDoubleVal, "" + oRetVal);
      if (maxDoubleVal.equals(oRetVal))
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed!");

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
   * @testName: testRegisterOutParameter31
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set minimum double value in maximum value column in Double table and call
   * registerOutParameter(int parameterIndex,int jdbcType,int scale) method and
   * call getObject method. It should return a Double object that is been set.
   * (Note: This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter31() throws Fault {
    try {
      rsSch.createTab("Double_Tab", sqlp, conn);

      String sMinDoubleVal = rsSch.extractVal("Double_Tab", 2, sqlp, conn);
      Double minDoubleVal = new Double(sMinDoubleVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_Io_Max(?)}");
      cstmt.setDouble(1, minDoubleVal.doubleValue());

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DOUBLE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Double oRetVal = (Double) cstmt.getObject(1);

      msg.addOutputMsg("" + minDoubleVal, "" + oRetVal);
      if (minDoubleVal.equals(oRetVal))
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed!");

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
   * @testName: testRegisterOutParameter32
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set Double value in null column and call registerOutParameter method and
   * call getObject method. It should return a Double object that is been set.
   * (Note: This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter32() throws Fault {
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);

      String sMaxFloatVal = rsSch.extractVal("Float_Tab", 1, sqlp, conn);
      Float maxFloatVal = Float.valueOf(sMaxFloatVal);
      Double doubleVal = Double.valueOf(sMaxFloatVal);
      msg.setMsg("get the CallableStatement object");

      cstmt = conn.prepareCall("{call Float_Io_Null(?)}");
      cstmt.setDouble(1, doubleVal.doubleValue());

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.FLOAT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Double oRetVal = (Double) cstmt.getObject(1);

      msg.addOutputMsg("" + doubleVal, "" + oRetVal);

      if (Utils.isMatchingFloatingPointVal(doubleVal, oRetVal)) {
        msg.setMsg("registerOutParameter registers the OUT parameter");
      } else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed!");
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
   * @testName: testRegisterOutParameter33
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set minimum float value in maximum value column in Float table and call
   * registerOutParameter(int parameterIndex,int jdbcType,int scale) method and
   * call getObject method. It should return a Double object that is been set.
   * (Note: This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter33() throws Fault {
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);

      String sMinFloatVal = rsSch.extractVal("Float_Tab", 2, sqlp, conn);
      Double minFloatVal = Double.valueOf(sMinFloatVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Float_Io_Max(?)}");
      cstmt.setDouble(1, minFloatVal.doubleValue());

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DOUBLE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getObject method");
      Double oRetVal = (Double) cstmt.getObject(1);

      msg.addOutputMsg("" + minFloatVal, "" + oRetVal);

      if (Utils.isMatchingFloatingPointVal(minFloatVal, oRetVal))
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed!");

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
   * @testName: testRegisterOutParameter34
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set maximum Real value in null column of Real table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getObject method. It should return a Float object that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter34() throws Fault {
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);

      String sMaxRealVal = rsSch.extractVal("Real_Tab", 1, sqlp, conn);
      Float maxRealVal = new Float(sMaxRealVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_Io_Null(?)}");
      cstmt.setFloat(1, maxRealVal.floatValue());

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.REAL);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getObject method");
      Float oRetVal = (Float) cstmt.getObject(1);

      msg.addOutputMsg("" + maxRealVal, "" + oRetVal);
      if (maxRealVal.equals(oRetVal))
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed!");

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
        rsSch.dropTab("Real_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter35
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set minimum real value in maximum value column in Real table and call
   * registerOutParameter(int parameterIndex,int jdbcType,int scale) method and
   * call getObject method. It should return a Float object that is been set.
   * (Note: This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter35() throws Fault {
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);

      String sMinRealVal = rsSch.extractVal("Real_Tab", 2, sqlp, conn);
      Float minRealVal = new Float(sMinRealVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_Io_Max(?)}");
      cstmt.setFloat(1, minRealVal.floatValue());

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.REAL);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Float oRetVal = (Float) cstmt.getObject(1);

      msg.addOutputMsg("" + minRealVal, "" + oRetVal);
      if (minRealVal.equals(oRetVal))
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed!");

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
        rsSch.dropTab("Real_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter36
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set maximum BIT value in null column of BIT table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getObject method. It should return a Boolean object that is been set.
   * (Note: This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter36() throws Fault {
    Boolean oMaxBooleanVal = null;
    String smaxBooleanVal = null;
    String rStringVal = null;
    try {
      rsSch.createTab("Bit_Tab", sqlp, conn);

      smaxBooleanVal = rsSch.extractValAsNumericString("Bit_Tab", 1, sqlp,
          conn);
      oMaxBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 1, sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bit_Io_Null(?)}");
      cstmt.setBoolean(1, oMaxBooleanVal.booleanValue());

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.BIT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getObject method");

      rStringVal = cstmt.getObject(1).toString();
      rStringVal = rStringVal.trim();

      if (rStringVal.equals(oMaxBooleanVal.toString())
          || rStringVal.equals(smaxBooleanVal.trim()))
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed!");

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
        rsSch.dropTab("Bit_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter37
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set minimum BIT value in maximum value column in BIT table and call
   * registerOutParameter(int parameterIndex,int jdbcType,int scale) method and
   * call getBoolen method. It should return a Boolean object that is been set.
   * (Note: This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter37() throws Fault {
    String sminBooleanVal = null;
    Boolean oMinBooleanVal = null;
    String rStringVal = null;
    try {
      rsSch.createTab("Bit_Tab", sqlp, conn);

      sminBooleanVal = rsSch.extractValAsNumericString("Bit_Tab", 2, sqlp,
          conn);
      oMinBooleanVal = rsSch.extractValAsBoolObj("Bit_Tab", 2, sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bit_Io_Max(?)}");
      cstmt.setBoolean(1, oMinBooleanVal.booleanValue());

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.BIT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getObject method");

      rStringVal = cstmt.getObject(1).toString();
      rStringVal = rStringVal.trim();

      if (rStringVal.equals(oMinBooleanVal.toString())
          || rStringVal.equals(sminBooleanVal.trim()))
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed!");

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
        rsSch.dropTab("Bit_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter38
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set maximum Smallint value in null column of Smallint table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getObject method. It should return a Integer object that is been set.
   * (Note: This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter38() throws Fault {
    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      String sMaxByteVal = rsSch.extractVal("Smallint_Tab", 1, sqlp, conn);
      Short maxByteVal = new Short(sMaxByteVal);
      Integer imaxByteVal = new Integer(sMaxByteVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_Io_Null(?)}");
      cstmt.setShort(1, maxByteVal.shortValue());

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      String sRetVal = "" + cstmt.getObject(1);

      msg.addOutputMsg(sMaxByteVal, sRetVal);

      if (sMaxByteVal.trim().equals(sRetVal.trim()))
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed!");

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
        rsSch.dropTab("Smallint_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter39
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set minimum Smallint value in maximum value column in Smallint table and
   * call registerOutParameter(int parameterIndex,int jdbcType) method and call
   * getObject method. It should return a Integer object that is been set.
   * (Note: This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter39() throws Fault {
    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      String sMinByteVal = rsSch.extractVal("Smallint_Tab", 2, sqlp, conn);
      Short minByteVal = new Short(sMinByteVal);
      Integer iminByteVal = new Integer(sMinByteVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_Io_Max(?)}");
      cstmt.setShort(1, minByteVal.shortValue());

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      String sRetVal = "" + cstmt.getObject(1);

      msg.addOutputMsg(sMinByteVal, sRetVal);
      if (sMinByteVal.trim().equals(sRetVal.trim()))
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed!");

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
        rsSch.dropTab("Smallint_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter40
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set maximum Tinyint value in null column of Tinyint table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getObject method. It should return a Byte object that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter40() throws Fault {
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      String sMaxShortVal = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);
      Byte maxShortVal = new Byte(sMaxShortVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_Io_Null(?)}");
      cstmt.setByte(1, maxShortVal.byteValue());

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.TINYINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Object obj = cstmt.getObject(1);
      Byte oRetVal = new Byte(obj.toString());

      msg.addOutputMsg("" + maxShortVal, "" + oRetVal);
      if ((maxShortVal.compareTo(oRetVal)) == 0)
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed!");

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
        rsSch.dropTab("Tinyint_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter41
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set minimum Tinyint value in maximum value column in Tinyint table and call
   * registerOutParameter(int parameterIndex,int jdbcType) method and call
   * getObject method. It should return a Byte object that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter41() throws Fault {
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      String sMinShortVal = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);
      Byte minShortVal = new Byte(sMinShortVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_Io_Max(?)}");
      cstmt.setByte(1, minShortVal.byteValue());

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.TINYINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Object obj = cstmt.getObject(1);
      Byte oRetVal = new Byte(obj.toString());

      msg.addOutputMsg("" + minShortVal, "" + oRetVal);
      if ((minShortVal.compareTo(oRetVal)) == 0)
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed!");

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
        rsSch.dropTab("Tinyint_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter42
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set maximum Integer value in null column of Integer table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getObject method. It should return a Integer object that is been set.
   * (Note: This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter42() throws Fault {
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);

      String sMaxIntegerVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);
      Integer maxIntegerVal = new Integer(sMaxIntegerVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_Io_Null(?)}");
      cstmt.setInt(1, maxIntegerVal.intValue());

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.INTEGER);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Integer oRetVal = (Integer) cstmt.getObject(1);

      msg.addOutputMsg("" + maxIntegerVal, "" + oRetVal);
      if (maxIntegerVal.equals(oRetVal))
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed!");

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
        rsSch.dropTab("Integer_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter43
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set minimum Integer value in maximum value column in Integer table and call
   * registerOutParameter(int parameterIndex,int jdbcType) method and call
   * getObject method. It should return a Integer object that is been set.
   * (Note: This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter43() throws Fault {
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);

      String sMinIntegerVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);
      Integer minIntegerVal = new Integer(sMinIntegerVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_Io_Max(?)}");
      cstmt.setInt(1, minIntegerVal.intValue());

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.INTEGER);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getObject method");
      Integer oRetVal = (Integer) cstmt.getObject(1);

      msg.addOutputMsg("" + minIntegerVal, "" + oRetVal);
      if (minIntegerVal.equals(oRetVal))
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed!");

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
        rsSch.dropTab("Integer_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter44
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set maximum Bigint value in null column of Bigint table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getObject method. It should return a Long Object that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter44() throws Fault {
    try {
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      String sMaxLongVal = rsSch.extractVal("Bigint_Tab", 1, sqlp, conn);
      Long maxLongVal = new Long(sMaxLongVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_Io_Null(?)}");
      cstmt.setLong(1, maxLongVal.longValue());
      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.BIGINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getObject method");
      Long oRetVal = (Long) cstmt.getObject(1);

      msg.addOutputMsg("" + maxLongVal, "" + oRetVal);
      if (maxLongVal.equals(oRetVal))
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed!");

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
        rsSch.dropTab("Bigint_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter45
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set minimum Bigint value in maximum value column in Bigint table and call
   * registerOutParameter(int parameterIndex,int jdbcType) method and call
   * getObject method. It should return a Long object that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter45() throws Fault {
    try {
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      String sMinLongVal = rsSch.extractVal("Bigint_Tab", 2, sqlp, conn);
      Long minLongVal = new Long(sMinLongVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_Io_Max(?)}");
      cstmt.setLong(1, minLongVal.longValue());

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.BIGINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      Long oRetVal = (Long) cstmt.getObject(1);

      msg.addOutputMsg("" + minLongVal, "" + oRetVal);
      if (minLongVal.equals(oRetVal))
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed!");

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
        rsSch.dropTab("Bigint_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter46
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1237;
   * JDBC:JAVADOC:1238; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setString() method to
   * set Char value in null column of Char table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getString method. It should return a String object that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter46() throws Fault {
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);

      String sCoffeeName = rsSch.extractVal("Char_Tab", 1, sqlp, conn);
      sCoffeeName = sCoffeeName.substring(1, sCoffeeName.length() - 1);
      sCoffeeName = sCoffeeName.trim();

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_Io_Null(?)}");
      cstmt.setString(1, sCoffeeName.toString());

      int precision = sCoffeeName.length();

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.CHAR, precision);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      String oRetVal = (String) cstmt.getObject(1);
      oRetVal = oRetVal.trim();

      msg.addOutputMsg(sCoffeeName, oRetVal);
      if (sCoffeeName.equals(oRetVal))
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed!");

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
        rsSch.dropTab("Char_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter47
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set Varchar value in null column of Varchar table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getObject method. It should return a String object that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter47() throws Fault {
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      String sCoffeeName = rsSch.extractVal("Varchar_Tab", 1, sqlp, conn);
      sCoffeeName = sCoffeeName.substring(1, sCoffeeName.length() - 1);
      sCoffeeName = sCoffeeName.trim();

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_Io_Null(?)}");
      cstmt.setString(1, sCoffeeName.toString());

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      String oRetVal = (String) cstmt.getObject(1);
      oRetVal = oRetVal.trim();

      msg.addOutputMsg(sCoffeeName, oRetVal);
      if (sCoffeeName.equals(oRetVal))
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed!");

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
        rsSch.dropTab("Varchar_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter48
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set Longvarchar value in null column of Longvarchar table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getObject method. It should return a String object that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter48() throws Fault {
    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      String sCoffeeName = rsSch.extractVal("Longvarchar_Tab", 1, sqlp, conn);
      sCoffeeName = sCoffeeName.substring(1, sCoffeeName.length() - 1);
      sCoffeeName = sCoffeeName.trim();

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Lvarchar_Io_Null(?)}");
      cstmt.setString(1, sCoffeeName.toString());

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.LONGVARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getObject method");
      String oRetVal = (String) cstmt.getObject(1);
      oRetVal = oRetVal.trim();

      msg.addOutputMsg(sCoffeeName, oRetVal);
      if (sCoffeeName.equals(oRetVal))
        msg.setMsg("registerOutParameter registers the OUT parameter");
      else {
        msg.printTestError(
            "registerOutParameter does not register the OUT parameter",
            "test registerOutParameter Failed!");

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
        rsSch.dropTab("Longvarcharnull_Tab", conn);
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
