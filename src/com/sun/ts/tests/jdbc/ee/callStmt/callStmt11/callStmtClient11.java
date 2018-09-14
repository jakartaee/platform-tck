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
 * $Id$
 */

/*
 * @(#)callStmtClient11.java	1.18 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt11;

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
 * The callStmtClient11 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class callStmtClient11 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt11";

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

  private JDBCTestMsg msg = new JDBCTestMsg();

  /* Run test in standalone mode */
  public static void main(String[] args) {
    callStmtClient11 theTests = new callStmtClient11();
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
   * @testName: testSetObject61
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Smallint_Tab with the maximum value
   * of the Smallint_Tab. Execute a query to retrieve the Min_Val from
   * Smallint_Tab. Compare the returned value with the maximum value extracted
   * from tssql.stmt file. Both of them should be equal
   */
  public void testSetObject61() throws Fault {

    Integer maxSmallintVal = null;
    BigDecimal maxBigDecimalVal = null;
    String rSmallintVal = null;
    String smaxStringVal = null;
    try {

      rsSch.createTab("Smallint_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_In_Min(?)}");

      msg.setMsg("to extract the Maximum Value of Smallint to be Updated");
      smaxStringVal = rsSch.extractVal("Smallint_Tab", 1, sqlp, conn);
      maxBigDecimalVal = new BigDecimal(smaxStringVal);
      maxSmallintVal = new Integer(smaxStringVal);
      cstmt.setObject(1, maxBigDecimalVal, java.sql.Types.SMALLINT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Smallint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      rSmallintVal = "" + rs.getObject(1);
      msg.addOutputMsg(smaxStringVal, rSmallintVal);

      if (rSmallintVal.trim().equals(smaxStringVal.trim())) {
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
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject62
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Smallint_Tab with the minimum
   * value of the Smallint_Tab. Execute a query to retrieve the Null_Val from
   * Smallint_Tab. Compare the returned value with the minimum value extracted
   * from tssql.stmt file. Both of them should be equal
   */
  public void testSetObject62() throws Fault {
    Integer minSmallintVal = null;
    BigDecimal minBigDecimalVal = null;
    String rSmallintVal = null;
    String sminStringVal = null;
    try {

      rsSch.createTab("Smallint_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Smallint_In_Null(?)}");

      msg.setMsg("to extract the Minimum Value of Smallint to be Updated");
      sminStringVal = rsSch.extractVal("Smallint_Tab", 2, sqlp, conn);

      minBigDecimalVal = new BigDecimal(sminStringVal);
      minSmallintVal = new Integer(sminStringVal);

      cstmt.setObject(1, minBigDecimalVal, java.sql.Types.SMALLINT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Smallint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rSmallintVal = "" + rs.getObject(1);
      msg.addOutputMsg(sminStringVal, rSmallintVal);
      if (rSmallintVal.trim().equals(sminStringVal.trim())) {
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
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject63
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Integer_Tab with the maximum value
   * of the Integer_Tab. Execute a query to retrieve the Min_Val from
   * Integer_Tab. Compare the returned value with the maximum value extracted
   * from tssql.stmt file. Both of them should be equal
   */
  public void testSetObject63() throws Fault {

    Integer maxIntegerVal = null;
    BigDecimal maxBigDecimalVal = null;
    String rIntegerVal = null;
    String smaxStringVal = null;
    try {

      rsSch.createTab("Integer_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_In_Min(?)}");

      msg.setMsg("to extract the Maximum Value of Integer to be Updated");
      smaxStringVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);
      maxBigDecimalVal = new BigDecimal(smaxStringVal);
      maxIntegerVal = new Integer(smaxStringVal);

      cstmt.setObject(1, maxBigDecimalVal, java.sql.Types.INTEGER);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Integer_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      rIntegerVal = "" + rs.getObject(1);
      msg.addOutputMsg(smaxStringVal, rIntegerVal);

      if (rIntegerVal.trim().equals(smaxStringVal)) {
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
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject64
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Integer_Tab with the minimum value
   * of the Integer_Tab. Execute a query to retrieve the Null_Val from
   * Integer_Tab. Compare the returned value with the minimum value extracted
   * from tssql.stmt file. Both of them should be equal
   */
  public void testSetObject64() throws Fault {
    Integer minIntegerVal = null;
    BigDecimal minBigDecimalVal = null;
    String rIntegerVal = null;
    String sminStringVal = null;
    try {

      rsSch.createTab("Integer_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_In_Null(?)}");

      msg.setMsg("to extract the Maximum Value of Integer to be Updated");
      sminStringVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);

      minBigDecimalVal = new BigDecimal(sminStringVal);
      minIntegerVal = new Integer(sminStringVal);
      cstmt.setObject(1, minBigDecimalVal, java.sql.Types.INTEGER);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Integer_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rIntegerVal = "" + rs.getObject(1);
      msg.addOutputMsg(sminStringVal, rIntegerVal);

      if (rIntegerVal.trim().equals(sminStringVal.trim())) {
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
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject65
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Bigint_Tab with the maximum value
   * of the Bigint_Tab. Execute a query to retrieve the Min_Val from Bigint_Tab.
   * Compare the returned value with the maximum value extracted from tssql.stmt
   * file. Both of them should be equal
   */
  public void testSetObject65() throws Fault {
    Long maxBigintVal = null;
    BigDecimal maxBigDecimalVal = null;
    String rBigintVal = null;
    String smaxStringVal = null;
    try {

      rsSch.createTab("Bigint_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_In_Min(?)}");

      msg.setMsg("to extract the Maximum Value of Bigint to be Updated");
      smaxStringVal = rsSch.extractVal("Bigint_Tab", 1, sqlp, conn);
      maxBigDecimalVal = new BigDecimal(smaxStringVal);
      maxBigintVal = new Long(smaxStringVal);

      cstmt.setObject(1, maxBigDecimalVal, java.sql.Types.BIGINT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Bigint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      rBigintVal = "" + rs.getObject(1);
      msg.addOutputMsg(smaxStringVal, rBigintVal);

      if (rBigintVal.trim().equals(smaxStringVal.trim())) {
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
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject66
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Bigint_Tab with the minimum value
   * of the Bigint_Tab. Execute a query to retrieve the Null_Val from
   * Bigint_Tab. Compare the returned value with the minimum value extracted
   * from tssql.stmt file. Both of them should be equal
   */
  public void testSetObject66() throws Fault {
    Long minBigintVal = null;
    BigDecimal minBigDecimalVal = null;
    String rBigintVal = null;
    String sminStringVal = null;
    try {

      rsSch.createTab("Bigint_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_In_Null(?)}");

      msg.setMsg("to extract the Minimum Value of Bigint to be Updated");
      sminStringVal = rsSch.extractVal("Bigint_Tab", 2, sqlp, conn);
      minBigDecimalVal = new BigDecimal(sminStringVal);
      minBigintVal = new Long(sminStringVal);

      cstmt.setObject(1, minBigDecimalVal, java.sql.Types.BIGINT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Bigint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rBigintVal = "" + rs.getObject(1);
      msg.addOutputMsg(sminStringVal, rBigintVal);
      ;

      if (rBigintVal.trim().equals(sminStringVal.trim())) {
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
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject67
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:7; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Integer_Tab with the maximum value
   * of the Real_Tab. Execute a query to retrieve the Min_Val from Real_Tab.
   * Compare the returned value with the maximum value extracted from tssql.stmt
   * file. Both of them should be equal
   */
  public void testSetObject67() throws Fault {
    Float maxRealVal = null;
    BigDecimal maxBigDecimalVal = null;
    Float rRealVal = null;
    String smaxStringVal = null;
    try {

      rsSch.createTab("Real_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_In_Min(?)}");

      msg.setMsg(
          "to extract the Maximum Value of Integer to be Updated in Real table");
      smaxStringVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);
      maxBigDecimalVal = new BigDecimal(smaxStringVal);
      maxRealVal = new Float(smaxStringVal);

      cstmt.setObject(1, maxBigDecimalVal, java.sql.Types.REAL);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Real_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object oRealVal = rs.getObject(1);
      rRealVal = new Float(oRealVal.toString());

      msg.addOutputMsg("" + maxRealVal, "" + rRealVal);

      if (rRealVal.equals(maxRealVal)) {
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
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject68
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Real_Tab with the minimum value of
   * the Integer_Tab. Execute a query to retrieve the Null_Val from Real_Tab.
   * Compare the returned value with the minimum value extracted from tssql.stmt
   * file. Both of them should be equal
   */
  public void testSetObject68() throws Fault {
    Float minRealVal = null;
    BigDecimal minBigDecimalVal = null;
    Float rRealVal = null;
    String sminStringVal = null;
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_In_Null(?)}");

      msg.setMsg(
          "to extract the Minimum Value of Integer to be Updated in Real table");
      sminStringVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);
      minBigDecimalVal = new BigDecimal(sminStringVal);
      minRealVal = new Float(sminStringVal);

      cstmt.setObject(1, minBigDecimalVal, java.sql.Types.REAL);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Real_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object oRealVal = rs.getObject(1);

      rRealVal = new Float(oRealVal.toString());
      msg.addOutputMsg("" + minRealVal, "" + rRealVal);

      if (rRealVal.equals(minRealVal)) {
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
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject69
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Float_Tab with the maximum value of
   * the Decimal_Tab. Execute a query to retrieve the Min_Val from Float_Tab.
   * Compare the returned value with the maximum value extracted from tssql.stmt
   * file. Both of them should be equal
   */
  public void testSetObject69() throws Fault {
    Double maxFloatVal = null;
    BigDecimal maxBigDecimalVal = null;
    Double rFloatVal = null;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Float_In_Min(?)}");
      msg.setMsg(
          "to extract the Maximum Value of Decimal to be Updated  in Float");
      smaxStringVal = rsSch.extractVal("Decimal_Tab", 1, sqlp, conn);

      maxBigDecimalVal = new BigDecimal(smaxStringVal);
      maxFloatVal = new Double(smaxStringVal);

      cstmt.setObject(1, maxBigDecimalVal, java.sql.Types.FLOAT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Float_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      rFloatVal = (Double) rs.getObject(1);
      msg.addOutputMsg("" + maxFloatVal, "" + rFloatVal);

      if (rFloatVal.equals(maxFloatVal)) {
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
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject70
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Float_Tab with the minimum value
   * of the Decimal_Tab. Execute a query to retrieve the Null_Val from
   * Decimal_Tab. Compare the returned value with the minimum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject70() throws Fault {
    Double minFloatVal = null;
    BigDecimal minBigDecimalVal = null;
    Double rFloatVal = null;
    String sminStringVal = null;
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Float_In_Null(?)}");

      msg.setMsg(
          "to extract the Minimum Value of Decimal to be Updated in Float table");
      sminStringVal = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);
      minBigDecimalVal = new BigDecimal(sminStringVal);
      minFloatVal = new Double(sminStringVal);

      cstmt.setObject(1, minBigDecimalVal, java.sql.Types.FLOAT);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Float_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rFloatVal = (Double) rs.getObject(1);
      msg.addOutputMsg("" + minFloatVal, "" + rFloatVal);

      if (rFloatVal.equals(minFloatVal)) {
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
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject71
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Double_Tab with the maximum value
   * of the Double_Tab. Execute a query to retrieve the Min_Val from Double_Tab.
   * Compare the returned value with the maximum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject71() throws Fault {
    Double maxDoubleVal = null;
    BigDecimal maxBigDecimalVal = null;
    Double rDoubleVal = null;
    String smaxStringVal = null;
    try {

      rsSch.createTab("Double_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_In_Min(?)}");

      msg.setMsg("to extract the Maximum Value of Double to be Updated");
      smaxStringVal = rsSch.extractVal("Double_Tab", 1, sqlp, conn);
      maxBigDecimalVal = new BigDecimal(smaxStringVal);
      maxDoubleVal = new Double(smaxStringVal);

      cstmt.setObject(1, maxBigDecimalVal, java.sql.Types.DOUBLE);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      String Min_Val_Query = sqlp.getProperty("Double_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      rDoubleVal = (Double) rs.getObject(1);
      msg.addOutputMsg("" + maxDoubleVal, "" + rDoubleVal);

      if (rDoubleVal.equals(maxDoubleVal)) {
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
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject72
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Double_Tab with the minimum value
   * of the Double_Tab. Execute a query to retrieve the Null_Val from
   * Double_Tab. Compare the returned value with the minimum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject72() throws Fault {
    Double minDoubleVal = null;
    BigDecimal minBigDecimalVal = null;
    Double rDoubleVal = null;
    String sminStringVal = null;
    try {

      rsSch.createTab("Double_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_In_Null(?)}");

      msg.setMsg("to extract the Maximum Value of Double to be Updated");
      sminStringVal = rsSch.extractVal("Double_Tab", 2, sqlp, conn);
      minBigDecimalVal = new BigDecimal(sminStringVal);
      minDoubleVal = new Double(sminStringVal);

      cstmt.setObject(1, minBigDecimalVal, java.sql.Types.DOUBLE);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Double_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rDoubleVal = (Double) rs.getObject(1);
      msg.addOutputMsg("" + minDoubleVal, "" + rDoubleVal);
      if (rDoubleVal.equals(minDoubleVal)) {
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
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject73
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Decimal_Tab with the maximum value
   * of the Decimal_Tab. Execute a query to retrieve the Null_Val from
   * Decimal_Tab. Compare the returned value with the maximum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject73() throws Fault {
    BigDecimal maxDecimalVal = null;
    BigDecimal rDecimalVal = null;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_In_Null(?)}");

      msg.setMsg("to extract the Maximum Value of Decimal to be Updated");
      smaxStringVal = rsSch.extractVal("Decimal_Tab", 1, sqlp, conn);
      maxDecimalVal = new BigDecimal(smaxStringVal);
      cstmt.setObject(1, maxDecimalVal, java.sql.Types.DECIMAL, 15);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Decimal_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rDecimalVal = (BigDecimal) rs.getObject(1);
      msg.addOutputMsg("" + maxDecimalVal, "" + rDecimalVal);
      if ((rDecimalVal.compareTo(maxDecimalVal) == 0)) {
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
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject74
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Decimal_Tab with the minimum value
   * of the Decimal_Tab. Execute a query to retrieve the Null_Val from
   * Decimal_Tab. Compare the returned value with the minimum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject74() throws Fault {
    BigDecimal minDecimalVal = null;
    BigDecimal rDecimalVal = null;
    String sminStringVal = null;
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_In_Max(?)}");

      msg.setMsg("to extract the Minimum Value of Decimal to be Updated");
      sminStringVal = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);
      minDecimalVal = new BigDecimal(sminStringVal);
      cstmt.setObject(1, minDecimalVal, java.sql.Types.DECIMAL, 15);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      String Null_Val_Query = sqlp.getProperty("Decimal_Query_Max", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object oDecimalVal = rs.getObject(1);
      rDecimalVal = new BigDecimal(oDecimalVal.toString());

      msg.addOutputMsg("" + minDecimalVal, "" + rDecimalVal);
      if ((rDecimalVal.compareTo(minDecimalVal) == 0)) {
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
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject75
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Numeric_Tab with the maximum value
   * of the Numeric_Tab. Execute a query to retrieve the Min_Val from
   * Numeric_Tab. Compare the returned value with the maximum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject75() throws Fault {
    BigDecimal maxNumericVal = null;
    BigDecimal rNumericVal = null;
    String smaxStringVal = null;
    try {

      rsSch.createTab("Numeric_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Numeric_In_Max(?)}");

      msg.setMsg("to extract the Maximum Value of Numeric to be Updated");
      smaxStringVal = rsSch.extractVal("Numeric_Tab", 2, sqlp, conn);
      maxNumericVal = new BigDecimal(smaxStringVal);
      cstmt.setObject(1, maxNumericVal, java.sql.Types.NUMERIC, 15);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Min_Val_Query = sqlp.getProperty("Numeric_Query_Max", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object oNumericVal = rs.getObject(1);
      rNumericVal = new BigDecimal(oNumericVal.toString());

      msg.addOutputMsg("" + maxNumericVal, "" + rNumericVal);

      if ((rNumericVal.compareTo(maxNumericVal) == 0)) {
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
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject76
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Numeric_Tab with the minimum value
   * of the Numeric_Tab. Execute a query to retrieve the Null_Val from
   * Numeric_Tab. Compare the returned value with the minimum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
  public void testSetObject76() throws Fault {
    BigDecimal minNumericVal = null;
    BigDecimal rNumericVal = null;
    String sminStringVal = null;
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Numeric_In_Null(?)}");

      msg.setMsg("to extract the Maximum Value of Numeric to be Updated");
      sminStringVal = rsSch.extractVal("Numeric_Tab", 2, sqlp, conn);
      minNumericVal = new BigDecimal(sminStringVal);

      cstmt.setObject(1, minNumericVal, java.sql.Types.NUMERIC, 16);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Numeric_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object oNumericVal = rs.getObject(1);
      rNumericVal = new BigDecimal(oNumericVal.toString());

      msg.addOutputMsg("" + minNumericVal, "" + rNumericVal);

      if ((rNumericVal.compareTo(minNumericVal) == 0)) {
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
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject79
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Char_Tab with the maximum value of
   * the Decimal_Tab. Execute a query to retrieve the Null_Val from Char_Tab.
   * Compare the returned value with the maximum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject79() throws Fault {
    String maxStringVal = null;
    String rStringVal = null;
    BigDecimal maxBigDecimalVal = null;
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_In_Null(?)}");

      msg.setMsg("to extract the Value of String to be Updated");
      maxStringVal = rsSch.extractVal("Decimal_Tab", 1, sqlp, conn);
      maxStringVal = maxStringVal.trim();
      maxBigDecimalVal = new BigDecimal(maxStringVal);
      msg.setMsg("String Value :" + maxStringVal);

      cstmt.setObject(1, maxBigDecimalVal, java.sql.Types.CHAR);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getObject(1).toString();
      rStringVal = rStringVal.trim();
      msg.addOutputMsg(maxStringVal, rStringVal);

      if (rStringVal.equals(maxStringVal)) {
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
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        TestUtil.printStackTrace(e);

        msg.setMsg("Exception in finally block" + e);
      }
    }
  }

  /*
   * @testName: testSetObject80
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Char_Tab with the minimum value of
   * the Decimal_Tab. Execute a query to retrieve the Null_Val from Char_Tab.
   * Compare the returned value with the minimum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
  public void testSetObject80() throws Fault {
    String maxStringVal = null;
    String rStringVal = null;
    BigDecimal maxBigDecimalVal = null;
    BigDecimal rsBigDecimalVal = null;
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_In_Null(?)}");

      msg.setMsg("to extract the Value of String to be Updated");
      maxStringVal = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);
      maxStringVal = maxStringVal.trim();
      maxBigDecimalVal = new BigDecimal(maxStringVal);

      cstmt.setObject(1, maxBigDecimalVal, java.sql.Types.CHAR);
      cstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rStringVal = rs.getObject(1).toString().trim();
      rsBigDecimalVal = new BigDecimal(rStringVal);

      msg.addOutputMsg(maxStringVal, rsBigDecimalVal.toString());

      if (rsBigDecimalVal.compareTo(maxBigDecimalVal) == 0) {
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
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
