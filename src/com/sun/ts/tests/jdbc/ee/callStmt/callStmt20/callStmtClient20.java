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

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt20;

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
 * The callStmtClient20 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class callStmtClient20 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt20";

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
    callStmtClient20 theTests = new callStmtClient20();
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
   * @testName: testRegisterOutParameter09
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setFloat() method to
   * set maximum Real value in null column of Real table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getFloat method. It should return a float value that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter09() throws Fault {
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);

      String sMaxRealVal = rsSch.extractVal("Real_Tab", 1, sqlp, conn);
      float maxRealVal = Float.parseFloat(sMaxRealVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_Io_Null(?)}");
      cstmt.setFloat(1, maxRealVal);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.REAL);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getFloat method");
      float oRetVal = cstmt.getFloat(1);

      msg.addOutputMsg("" + maxRealVal, "" + oRetVal);
      if (maxRealVal == oRetVal)
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
        rsSch.dropTab("Real_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter10
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setFloat() method to
   * set minimum real value in maximum value column in Real table and call
   * registerOutParameter(int parameterIndex,int jdbcType) method and call
   * getFloat method. It should return a float value that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter10() throws Fault {
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);

      String sMinRealVal = rsSch.extractVal("Real_Tab", 2, sqlp, conn);
      float minRealVal = Float.parseFloat(sMinRealVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_Io_Max(?)}");
      cstmt.setFloat(1, minRealVal);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.REAL);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getFloat method");
      float oRetVal = cstmt.getFloat(1);

      msg.addOutputMsg("" + minRealVal, "" + oRetVal);
      if (minRealVal == oRetVal)
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
        rsSch.dropTab("Real_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter11
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setBoolean() method to
   * set maximum BIT value in null column of BIT table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getBoolean method. It should return a boolean value that is been set.
   * (Note: This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter11() throws Fault {
    try {
      rsSch.createTab("Bit_Tab", sqlp, conn);

      boolean maxBooleanVal = rsSch.extractValAsBoolVal("Bit_Tab", 1, sqlp,
          conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bit_Io_Null(?)}");
      cstmt.setBoolean(1, maxBooleanVal);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.BIT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getBoolean method");
      boolean oRetVal = cstmt.getBoolean(1);

      msg.addOutputMsg("" + maxBooleanVal, "" + oRetVal);
      if (maxBooleanVal == oRetVal)
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
        rsSch.dropTab("Bit_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter12
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setBoolean() method to
   * set minimum BIT value in maximum value column in BIT table and call
   * registerOutParameter(int parameterIndex,int jdbcType,int scale) method and
   * call getBoolen method. It should return a boolean value that is been set.
   * (Note: This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter12() throws Fault {
    try {
      rsSch.createTab("Bit_Tab", sqlp, conn);

      boolean minBooleanVal = rsSch.extractValAsBoolVal("Bit_Tab", 2, sqlp,
          conn);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bit_Io_Max(?)}");
      cstmt.setBoolean(1, minBooleanVal);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.BIT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getBoolean method");
      boolean oRetVal = cstmt.getBoolean(1);

      msg.addOutputMsg("" + minBooleanVal, "" + oRetVal);
      if (minBooleanVal == oRetVal)
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
        rsSch.dropTab("Bit_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter13
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setShort() method to
   * set maximum Smallint value in null column of Smallint table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getShort method. It should return a byte value that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter13() throws Fault {
    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      String sMaxByteVal = rsSch.extractVal("Smallint_Tab", 1, sqlp, conn);
      short maxByteVal = Short.parseShort(sMaxByteVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_Io_Null(?)}");
      cstmt.setShort(1, maxByteVal);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getShort method");
      short oRetVal = cstmt.getShort(1);

      msg.addOutputMsg("" + maxByteVal, "" + oRetVal);
      if (maxByteVal == oRetVal)
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
        rsSch.dropTab("Smallint_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter14
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setShort() method to
   * set minimum Smallint value in maximum value column in Smallint table and
   * call registerOutParameter(int parameterIndex,int jdbcType) method and call
   * getShort method. It should return a byte value that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter14() throws Fault {
    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      String sMinByteVal = rsSch.extractVal("Smallint_Tab", 2, sqlp, conn);
      short minByteVal = Short.parseShort(sMinByteVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_Io_Max(?)}");
      cstmt.setShort(1, minByteVal);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.SMALLINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getShort method");
      short oRetVal = cstmt.getShort(1);

      msg.addOutputMsg("" + minByteVal, "" + oRetVal);
      if (minByteVal == oRetVal)
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
        rsSch.dropTab("Smallint_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter15
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setByte() method to set
   * maximum Tinyint value in null column of Tinyint table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getByte method. It should return a short value that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter15() throws Fault {
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      String sMaxShortVal = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);
      byte maxShortVal = Byte.parseByte(sMaxShortVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_Io_Null(?)}");
      cstmt.setByte(1, maxShortVal);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.TINYINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getByte method");
      byte oRetVal = cstmt.getByte(1);

      msg.addOutputMsg("" + maxShortVal, "" + oRetVal);
      if (maxShortVal == oRetVal)
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
        rsSch.dropTab("Tinyint_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter16
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setByte() method to set
   * minimum Tinyint value in maximum value column in Tinyint table and call
   * registerOutParameter(int parameterIndex,int jdbcType) method and call
   * getByte method. It should return a short value that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter16() throws Fault {
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      String sMinShortVal = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);
      byte minShortVal = Byte.parseByte(sMinShortVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Tinyint_Io_Max(?)}");
      cstmt.setByte(1, minShortVal);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.TINYINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getShort method");
      byte oRetVal = cstmt.getByte(1);

      msg.addOutputMsg("" + minShortVal, "" + oRetVal);
      if (minShortVal == oRetVal)
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
        rsSch.dropTab("Tinyint_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter17
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setInt() method to set
   * maximum Integer value in null column of Integer table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getInt method. It should return a int value that is been set. (Note: This
   * test case also checks the support for INOUT parameter in Stored Procedure)
   *
   */
  public void testRegisterOutParameter17() throws Fault {
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);

      String sMaxIntegerVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);
      int maxIntegerVal = Integer.parseInt(sMaxIntegerVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_Io_Null(?)}");
      cstmt.setInt(1, maxIntegerVal);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.INTEGER);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getInt method");
      int oRetVal = cstmt.getInt(1);

      msg.addOutputMsg("" + maxIntegerVal, "" + oRetVal);
      if (maxIntegerVal == oRetVal)
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
        rsSch.dropTab("Integer_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter18
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setInt() method to set
   * minimum Integer value in maximum value column in Integer table and call
   * registerOutParameter(int parameterIndex,int jdbcType) method and call
   * getInt method. It should return a int value that is been set. (Note: This
   * test case also checks the support for INOUT parameter in Stored Procedure)
   *
   */
  public void testRegisterOutParameter18() throws Fault {
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);

      String sMinIntegerVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);
      int minIntegerVal = Integer.parseInt(sMinIntegerVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_Io_Max(?)}");
      cstmt.setInt(1, minIntegerVal);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.INTEGER);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getInt method");
      int oRetVal = cstmt.getInt(1);

      msg.addOutputMsg("" + minIntegerVal, "" + oRetVal);
      if (minIntegerVal == oRetVal)
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
        rsSch.dropTab("Integer_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter19
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setLong() method to set
   * maximum Bigint value in null column of Bigint table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getLong method. It should return a long value that is been set. (Note: This
   * test case also checks the support for INOUT parameter in Stored Procedure)
   *
   */
  public void testRegisterOutParameter19() throws Fault {
    try {
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      String sMaxLongVal = rsSch.extractVal("Bigint_Tab", 1, sqlp, conn);
      long maxLongVal = Long.parseLong(sMaxLongVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_Io_Null(?)}");
      cstmt.setLong(1, maxLongVal);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.BIGINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getLong method");
      long oRetVal = cstmt.getLong(1);

      msg.addOutputMsg("" + maxLongVal, "" + oRetVal);
      if (maxLongVal == oRetVal)
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
        rsSch.dropTab("Bigint_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter20
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setLong() method to set
   * minimum Bigint value in maximum value column in Bigint table and call
   * registerOutParameter(int parameterIndex,int jdbcType) method and call
   * getLong method. It should return a long value that is been set. (Note: This
   * test case also checks the support for INOUT parameter in Stored Procedure)
   *
   */
  public void testRegisterOutParameter20() throws Fault {
    try {
      rsSch.createTab("Bigint_Tab", sqlp, conn);

      String sMinLongVal = rsSch.extractVal("Bigint_Tab", 2, sqlp, conn);
      long minLongVal = Long.parseLong(sMinLongVal);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_Io_Max(?)}");
      cstmt.setLong(1, minLongVal);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.BIGINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getLong method");
      long oRetVal = cstmt.getLong(1);

      msg.addOutputMsg("" + minLongVal, "" + oRetVal);
      if (minLongVal == oRetVal)
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
        rsSch.dropTab("Bigint_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter21
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
  public void testRegisterOutParameter21() throws Fault {
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);

      String sCoffeeName = rsSch.extractVal("Char_Tab", 1, sqlp, conn);
      sCoffeeName = sCoffeeName.substring(1, sCoffeeName.length() - 1);
      sCoffeeName = sCoffeeName.trim();
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_Io_Null(?)}");
      cstmt.setString(1, sCoffeeName);

      msg.setMsg("register the output parameters");
      int arraySize = sCoffeeName.length();

      cstmt.registerOutParameter(1, java.sql.Types.CHAR, arraySize);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getString method");
      String oRetVal = cstmt.getString(1);
      oRetVal = oRetVal.trim();

      msg.addOutputMsg(sCoffeeName, oRetVal);
      if (sCoffeeName.equals(oRetVal))
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
        rsSch.dropTab("Char_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter22
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setString() method to
   * set Varchar value in null column of Varchar table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getString method. It should return a String object that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter22() throws Fault {
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      String sCoffeeName = rsSch.extractVal("Varchar_Tab", 1, sqlp, conn);
      sCoffeeName = sCoffeeName.substring(1, sCoffeeName.length() - 1);
      sCoffeeName = sCoffeeName.trim();

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_Io_Null(?)}");
      cstmt.setString(1, sCoffeeName);
      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getString method");
      String oRetVal = cstmt.getString(1);
      oRetVal.trim();

      msg.addOutputMsg(sCoffeeName, oRetVal);
      if (sCoffeeName.equals(oRetVal))
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
        rsSch.dropTab("Varchar_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter23
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setString() method to
   * set Longvarchar value in null column of Longvarchar table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getString method. It should return a String object that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter23() throws Fault {
    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      String sCoffeeName = rsSch.extractVal("Longvarchar_Tab", 1, sqlp, conn);
      sCoffeeName = sCoffeeName.substring(1, sCoffeeName.length() - 1);
      sCoffeeName = sCoffeeName.trim();

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Lvarchar_Io_Null(?)}");
      cstmt.setString(1, sCoffeeName);
      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.LONGVARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg("invoke getString method");
      String oRetVal = cstmt.getString(1);
      oRetVal.trim();

      msg.addOutputMsg(sCoffeeName, oRetVal);
      if (sCoffeeName.equals(oRetVal))
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
        rsSch.dropTab("Longvarcharnull_Tab", conn);

      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testRegisterOutParameter24
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setDate() method to set
   * Date value in null column of Date table and call registerOutParameter(int
   * parameterIndex, int jdbcType) method and call getDate method. It should
   * return a Date object that is been set. (Note: This test case also checks
   * the support for INOUT parameter in Stored Procedure)
   *
   */
  public void testRegisterOutParameter24() throws Fault {
    try {
      rsSch.createTab("Date_Tab", sqlp, conn);

      String sMfgDate = rsSch.extractVal("Date_Tab", 1, sqlp, conn);
      sMfgDate = sMfgDate.substring(sMfgDate.indexOf('\'') + 1,
          sMfgDate.lastIndexOf('\''));
      java.sql.Date mfgDate = java.sql.Date.valueOf(sMfgDate);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Date_Io_Null(?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.DATE);
      cstmt.setDate(1, mfgDate);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getDate method");
      java.util.Date objRetVal = (java.util.Date) cstmt.getDate(1);
      long lDate = objRetVal.getTime();
      java.sql.Date oRetVal = new java.sql.Date(lDate);

      msg.addOutputMsg("" + mfgDate, "" + oRetVal);
      if (mfgDate.compareTo(oRetVal) == 0)
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
   * @testName: testRegisterOutParameter25
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setTime() method to set
   * Time value in null column of Time table and call registerOutParameter(int
   * parameterIndex, int jdbcType) method and call getTime method. It should
   * return a Time object that is been set. (Note: This test case also checks
   * the support for INOUT parameter in Stored Procedure)
   *
   */
  public void testRegisterOutParameter25() throws Fault {
    try {
      rsSch.createTab("Time_Tab", sqlp, conn);

      String sBrkTime = rsSch.extractVal("Time_Tab", 1, sqlp, conn);
      sBrkTime = sBrkTime.substring(sBrkTime.indexOf('\'') + 1,
          sBrkTime.lastIndexOf('\''));
      java.sql.Time brkTime = java.sql.Time.valueOf(sBrkTime);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Time_Io_Null(?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.TIME);

      cstmt.setTime(1, brkTime);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getTime method");
      java.util.Date objRetVal = (java.util.Date) cstmt.getTime(1);
      long lDate = objRetVal.getTime();
      java.sql.Time oRetVal = new java.sql.Time(lDate);

      msg.addOutputMsg("" + brkTime, "" + oRetVal);
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
   * @testName: testRegisterOutParameter26
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setTimestamp() method
   * to set Timestamp value in null column of Timestamp table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getTimestamp method. It should return a Timestamp object that is been set.
   * (Note: This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
  public void testRegisterOutParameter26() throws Fault {
    String sInTime = null;
    java.sql.Timestamp inTimestamp = null;
    try {
      rsSch.createTab("Timestamp_Tab", sqlp, conn);

      sInTime = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);
      sInTime = sInTime.substring(sInTime.indexOf('\'') + 1,
          sInTime.lastIndexOf('\''));
      inTimestamp = Timestamp.valueOf(sInTime);

      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Timestamp_Io_Null(?)}");

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.TIMESTAMP);
      cstmt.setTimestamp(1, inTimestamp);
      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getTimestamp method");
      java.sql.Timestamp oRetVal = cstmt.getTimestamp(1);

      msg.addOutputMsg("" + inTimestamp, "" + oRetVal);
      if (inTimestamp.equals(oRetVal))
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
   * @testName: testRegisterOutParameter27
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setBytes() method to
   * set Binary value in Binary table and call registerOutParameter(int
   * parameterIndex, int jdbcType) method and call getBytes method. It should
   * return a Byte Array object that is been set. (Note: This test case also
   * checks the support for INOUT parameter in Stored Procedure)
   *
   */
  public void testRegisterOutParameter27() throws Fault {
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
      cstmt.setBytes(1, bytearr);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.BINARY);

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
   * @testName: testRegisterOutParameter28
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setBytes() method to
   * set Varbinary value in Varbinary table and call registerOutParameter(int
   * parameterIndex, int jdbcType) method and call getBytes method. It should
   * return a Byte Array object that is been set. (Note: This test case also
   * checks the support for INOUT parameter in Stored Procedure)
   *
   */
  public void testRegisterOutParameter28() throws Fault {
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
      cstmt.setBytes(1, bytearr);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.VARBINARY);

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
