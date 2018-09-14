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
 * @(#)callStmtClient15.java	1.21 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt15;

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
 * The callStmtClient15 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class callStmtClient15 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt15";

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
    callStmtClient15 theTests = new callStmtClient15();
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
   * @testName: testSetObject141
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

  public void testSetObject141() throws Fault {
    Long maxLongVal;
    String rIntegerVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_In_Min(?)}");
      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);
      Integer maxIntegerVal = new Integer(smaxStringVal);
      maxLongVal = new Long(smaxStringVal);

      cstmt.setObject(1, maxLongVal, java.sql.Types.INTEGER);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      String Max_Val_Query = sqlp.getProperty("Integer_Query_Min", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      rIntegerVal = "" + rs.getObject(1);
      msg.addOutputMsg(smaxStringVal, rIntegerVal);

      if (rIntegerVal.trim().equals(smaxStringVal.trim())) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
            "test setObject Failed!");

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
   * @testName: testSetObject142
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

  public void testSetObject142() throws Fault {
    Long minLongVal;
    String rIntegerVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Integer_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);
      Integer minIntegerVal = new Integer(sminStringVal);
      minLongVal = new Long(sminStringVal);

      cstmt.setObject(1, minLongVal, java.sql.Types.INTEGER);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      String Null_Val_Query = sqlp.getProperty("Integer_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rIntegerVal = "" + rs.getObject(1);

      msg.addOutputMsg(sminStringVal, rIntegerVal);
      if (rIntegerVal.trim().equals(sminStringVal)) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
            "test setObject Failed!");

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
   * @testName: testSetObject143
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Min_Val of Bigint_Tab with the
   * maximum value of Bigint_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject143() throws Fault {
    Long maxLongVal;
    String rLongVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Bigint_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_In_Min(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Bigint_Tab", 1, sqlp, conn);
      maxLongVal = new Long(smaxStringVal);

      cstmt.setObject(1, maxLongVal, java.sql.Types.BIGINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      String Max_Val_Query = sqlp.getProperty("Bigint_Query_Min", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      rLongVal = "" + rs.getObject(1);

      msg.addOutputMsg(smaxStringVal, rLongVal);
      if (rLongVal.trim().equals(smaxStringVal.trim())) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
            "test setObject Failed!");

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
   * @testName: testSetObject144
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType) method,update the column Null_Val of Bigint_Tab with the
   * minimum value of Bigint_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject144() throws Fault {
    Long minLongVal;
    String rLongVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Bigint_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Bigint_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Bigint_Tab", 2, sqlp, conn);
      minLongVal = new Long(sminStringVal);

      cstmt.setObject(1, minLongVal, java.sql.Types.BIGINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Bigint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rLongVal = "" + rs.getObject(1);
      msg.addOutputMsg(sminStringVal, rLongVal);

      if (rLongVal.trim().equals(sminStringVal.trim())) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
            "test setObject Failed!");

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
   * @testName: testSetObject145
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

  public void testSetObject145() throws Fault {
    BigDecimal maxLongVal;
    Float rFloatVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_In_Min(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Real_Tab", 1, sqlp, conn);
      Float maxFloatVal = new Float(smaxStringVal);
      maxLongVal = new BigDecimal(smaxStringVal);

      cstmt.setObject(1, maxLongVal, java.sql.Types.REAL);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

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
            "setObject Method does not set the designated parameter value",
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
   * @testName: testSetObject146
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

  public void testSetObject146() throws Fault {
    BigDecimal minLongVal;
    Float rFloatVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Real_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Real_Tab", 2, sqlp, conn);
      Float minFloatVal = new Float(sminStringVal);
      minLongVal = new BigDecimal(sminStringVal);

      cstmt.setObject(1, minLongVal, java.sql.Types.REAL);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

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
            "setObject Method does not set the designated parameter value",
            " test setObject Failed");

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
   * @testName: testSetObject147
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

  public void testSetObject147() throws Fault {
    BigDecimal maxLongVal;
    Float rFloatVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Float_In_Min(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Float_Tab", 1, sqlp, conn);
      Float maxFloatVal = new Float(smaxStringVal);
      maxLongVal = new BigDecimal(smaxStringVal);

      cstmt.setObject(1, maxLongVal, java.sql.Types.FLOAT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      String Max_Val_Query = sqlp.getProperty("Float_Query_Min", "");
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
            "test setObject Failed!");

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
   * @testName: testSetObject148
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType) method,update the column Null_Val of Float_Tab with the
   * minimum value of Float_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject148() throws Fault {
    BigDecimal minLongVal;
    Float rFloatVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Float_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Float_Tab", 2, sqlp, conn);
      Float minFloatVal = new Float(sminStringVal);
      minLongVal = new BigDecimal(sminStringVal);

      cstmt.setObject(1, minLongVal, java.sql.Types.FLOAT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      String Null_Val_Query = sqlp.getProperty("Float_Query_Null", "");
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
            "test setObject Failed!");

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
   * @testName: testSetObject149
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
   * method. Both the values should be equal.
   */

  public void testSetObject149() throws Fault {
    BigDecimal maxLongVal;
    Double rDoubleVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Double_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_In_Min(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Double_Tab", 1, sqlp, conn);
      Double maxDoubleVal = new Double(smaxStringVal);
      maxLongVal = new BigDecimal(smaxStringVal);

      cstmt.setObject(1, maxLongVal, java.sql.Types.DOUBLE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      String Max_Val_Query = sqlp.getProperty("Double_Query_Min", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      Object oDoubleVal = rs.getObject(1);
      rDoubleVal = new Double(oDoubleVal.toString());

      msg.addOutputMsg("" + maxDoubleVal, "" + rDoubleVal);

      if (rDoubleVal.compareTo(maxDoubleVal) == 0) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
            "test setObject Failed!");

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
        rsSch.dropTab("Double_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject150
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

  public void testSetObject150() throws Fault {
    BigDecimal minLongVal;
    Double rDoubleVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Double_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Double_In_Null(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      sminStringVal = rsSch.extractVal("Double_Tab", 2, sqlp, conn);
      Double minDoubleVal = new Double(sminStringVal);
      minLongVal = new BigDecimal(sminStringVal);

      cstmt.setObject(1, minLongVal, java.sql.Types.DOUBLE);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      String Null_Val_Query = sqlp.getProperty("Double_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      Object oDoubleVal = rs.getObject(1);
      rDoubleVal = new Double(oDoubleVal.toString());

      msg.addOutputMsg("" + minDoubleVal, "" + rDoubleVal);
      if (rDoubleVal.compareTo(minDoubleVal) == 0) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
            "test setObject Failed!");

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
        rsSch.dropTab("Double_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject151
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Min_Val of Decimal_Tab with the
   * maximum value of Decimal_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject151() throws Fault {

    BigDecimal rDecimalVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_In_Null(?)}");

      // We are testing a different object type. Taking from Decimal_Tab itself
      // and converting to a Long before setting the object.
      smaxStringVal = rsSch.extractVal("Decimal_Tab", 1, sqlp, conn);
      BigDecimal maxDecimalVal = new BigDecimal(smaxStringVal);

      cstmt.setObject(1, maxDecimalVal, java.sql.Types.DECIMAL);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      String Max_Val_Query = sqlp.getProperty("Decimal_Query_Null", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      Object oDecimalVal = rs.getObject(1);
      rDecimalVal = new BigDecimal(oDecimalVal.toString());

      msg.addOutputMsg("" + maxDecimalVal, "" + rDecimalVal);

      if ((rDecimalVal.compareTo(maxDecimalVal) == 0)) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
            "test setObject Failed!");

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
        rsSch.dropTab("Decimal_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject152
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

  public void testSetObject152() throws Fault {

    BigDecimal rDecimalVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Decimal_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);
      BigDecimal minDecimalVal = new BigDecimal(sminStringVal);

      cstmt.setObject(1, minDecimalVal, java.sql.Types.DECIMAL, 15);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Decimal_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object oDecimalVal = rs.getObject(1);
      rDecimalVal = new BigDecimal(oDecimalVal.toString());
      msg.addOutputMsg("" + minDecimalVal, "" + rDecimalVal);

      if ((rDecimalVal.compareTo(minDecimalVal) == 0)) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
            "test setObject Failed!");

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
        rsSch.dropTab("Decimal_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject153
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Min_Val of Numeric_Tab with the
   * maximum value of Numeric_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject153() throws Fault {

    BigDecimal rDecimalVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Numeric_In_Null(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Numeric_Tab", 1, sqlp, conn);
      BigDecimal maxDecimalVal = new BigDecimal(smaxStringVal);

      cstmt.setObject(1, maxDecimalVal, java.sql.Types.NUMERIC);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      String Max_Val_Query = sqlp.getProperty("Numeric_Query_Null", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      Object oDecimalVal = rs.getObject(1);
      rDecimalVal = new BigDecimal(oDecimalVal.toString());
      msg.addOutputMsg("" + maxDecimalVal, "" + rDecimalVal);

      if ((rDecimalVal.compareTo(maxDecimalVal) == 0)) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
            "test setObject Failed!");

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
        rsSch.dropTab("Numeric_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject154
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

  public void testSetObject154() throws Fault {

    BigDecimal rDecimalVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Numeric_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Numeric_Tab", 2, sqlp, conn);
      BigDecimal minDecimalVal = new BigDecimal(sminStringVal);

      cstmt.setObject(1, minDecimalVal, java.sql.Types.NUMERIC, 15);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Numeric_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      Object oDecimalVal = rs.getObject(1);
      rDecimalVal = new BigDecimal(oDecimalVal.toString());

      msg.addOutputMsg("" + minDecimalVal, "" + rDecimalVal);
      if ((rDecimalVal.compareTo(minDecimalVal) == 0)) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
            "test setObject Failed!");

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
        rsSch.dropTab("Numeric_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject157
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the Name column of Char_Tab with the maximum
   * value of Bigint_Tab. Call the getObject(int columnno) method to retrieve
   * this value. Extract the maximum value from the tssql.stmt file. Compare
   * this value with the value returned by the getObject(int columnno) method.
   * Both the values should be equal.
   */

  public void testSetObject157() throws Fault {
    Long maxLongVal;
    String rStringVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_In_Name(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Bigint_Tab", 1, sqlp, conn);
      String maxStringVal = new String(smaxStringVal);
      maxLongVal = new Long(maxStringVal);

      cstmt.setObject(1, maxLongVal, java.sql.Types.CHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Max_Val_Query = sqlp.getProperty("Char_Query_Name", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      rStringVal = (String) rs.getObject(1);

      msg.addOutputMsg(maxStringVal, rStringVal);
      if (rStringVal.trim().equals(maxStringVal.trim())) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
            "test setObject Failed!");

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
   * @testName: testSetObject158
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType) method,update the column Null_Val of Char_Tab with the
   * minimum value of Bigint_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject158() throws Fault {
    Long minLongVal;
    String rStringVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Char_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Bigint_Tab", 2, sqlp, conn);
      String minStringVal = new String(sminStringVal);
      minLongVal = new Long(sminStringVal);

      cstmt.setObject(1, minLongVal, java.sql.Types.CHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rStringVal = (String) rs.getObject(1);
      msg.addOutputMsg(minStringVal, rStringVal);
      if (rStringVal.trim().equals(minStringVal.trim())) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
            "test setObject Failed!");

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
   * @testName: testSetObject159
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the Name column of Varchar_Tab with the
   * maximum value of Bigint_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject159() throws Fault {
    Long maxLongVal;
    String rStringVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_In_Name(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Bigint_Tab", 1, sqlp, conn);
      String maxStringVal = new String(smaxStringVal);
      maxLongVal = new Long(smaxStringVal);

      cstmt.setObject(1, maxLongVal, java.sql.Types.VARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      String Max_Val_Query = sqlp.getProperty("Varchar_Query_Name", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      rStringVal = (String) rs.getObject(1);

      msg.addOutputMsg(maxStringVal, rStringVal);
      if (rStringVal.trim().equals(maxStringVal.trim())) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
            "test setObject Failed!");

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
   * @testName: testSetObject160
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType) method,update the column Null_Val of Varchar_Tab with the
   * minimum value of Bigint_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject160() throws Fault {
    Long minLongVal;
    String rStringVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);
      msg.setMsg("get the CallableStatement object");
      cstmt = conn.prepareCall("{call Varchar_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Bigint_Tab", 2, sqlp, conn);
      String minStringVal = new String(sminStringVal);
      minLongVal = new Long(sminStringVal);

      cstmt.setObject(1, minLongVal, java.sql.Types.VARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "to query from the database to check the call of cstmt.executeUpdate");

      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rStringVal = (String) rs.getObject(1);
      msg.addOutputMsg(minStringVal, rStringVal);
      if (rStringVal.trim().equals(minStringVal.trim())) {
        msg.setMsg("setObject Method sets the designated parameter value ");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter value ",
            "test setObject Failed!");

      }
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
