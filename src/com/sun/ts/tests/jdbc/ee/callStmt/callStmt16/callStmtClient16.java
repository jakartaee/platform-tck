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
 * @(#)callStmtClient16.java	1.21 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt16;

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
 * The callStmtClient16 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class callStmtClient16 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt16";

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
    callStmtClient16 theTests = new callStmtClient16();
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
   * @testName: testSetObject161
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the Name column of Longvarchar_Tab with the
   * maximum value of Bigint_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject161() throws Fault {
    Long maxLongVal;
    String rStringVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Longvarchar_Tab", sqlp, conn);

      cstmt = conn.prepareCall("{call Lvarchar_In_Name(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Bigint_Tab", 1, sqlp, conn);
      String maxStringVal = new String(smaxStringVal);
      maxLongVal = new Long(smaxStringVal);
      // to set the Long
      cstmt.setObject(1, maxLongVal, java.sql.Types.LONGVARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");
      String Max_Val_Query = sqlp.getProperty("Longvarchar_Query_Name", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      rStringVal = (String) rs.getObject(1);
      msg.addOutputMsg(maxStringVal, rStringVal);

      if (rStringVal.trim().equals(maxStringVal.trim())) {
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
        rsSch.dropTab("Longvarchar_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject162
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType) method,update the column Null_Val of Longvarchar_Tab with
   * the minimum value of Bigint_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
  public void testSetObject162() throws Fault {
    Long minLongVal;
    String rStringVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      cstmt = conn.prepareCall("{call Lvarchar_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Bigint_Tab", 2, sqlp, conn);
      String minStringVal = new String(sminStringVal);
      minLongVal = new Long(sminStringVal);

      // to set the Long
      cstmt.setObject(1, minLongVal, java.sql.Types.LONGVARCHAR);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rStringVal = (String) rs.getObject(1);
      msg.addOutputMsg(minStringVal, rStringVal);

      if (rStringVal.trim().equals(minStringVal.trim())) {
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
        rsSch.dropTab("Longvarcharnull_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject163
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
  public void testSetObject163() throws Fault {
    Float maxFloatVal;
    Integer rIntegerVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      cstmt = conn.prepareCall("{call Tinyint_In_Min(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);
      Integer maxIntegerVal = new Integer(smaxStringVal);
      maxFloatVal = new Float(smaxStringVal);
      // to set the Long
      cstmt.setObject(1, maxFloatVal, java.sql.Types.TINYINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");
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
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject164
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
  public void testSetObject164() throws Fault {
    Float minFloatVal;
    Integer rIntegerVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      cstmt = conn.prepareCall("{call Tinyint_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);
      Integer minIntegerVal = new Integer(sminStringVal);
      minFloatVal = new Float(sminStringVal);

      // to set the Value
      cstmt.setObject(1, minFloatVal, java.sql.Types.TINYINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");
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
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject165
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
   * method.Both the values should be equal.
   */
  public void testSetObject165() throws Fault {
    Float maxFloatVal;
    Integer rIntegerVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      cstmt = conn.prepareCall("{call Smallint_In_Min(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Smallint_Tab", 1, sqlp, conn);
      Integer maxIntegerVal = new Integer(smaxStringVal);
      maxFloatVal = new Float(smaxStringVal);
      // to set the Float
      cstmt.setObject(1, maxFloatVal, java.sql.Types.SMALLINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");
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
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject166
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
  public void testSetObject166() throws Fault {
    Float minFloatVal;
    Integer rIntegerVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      cstmt = conn.prepareCall("{call Smallint_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Smallint_Tab", 2, sqlp, conn);
      Integer minIntegerVal = new Integer(sminStringVal);
      minFloatVal = new Float(sminStringVal);
      // to set the Value
      cstmt.setObject(1, minFloatVal, java.sql.Types.SMALLINT);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");
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
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject167
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Min_Val of Integer_Tab with some
   * integer value after converting it to Float. Call the getObject(int
   * columnno) method to retrieve this value. Compare this value with the value
   * that is sent to database. Both the values should be equal.
   */

  public void testSetObject167() throws Fault {
    Float maxFloatVal;
    Integer rIntegerVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);
      cstmt = conn.prepareCall("{call Integer_In_Min(?)}");

      // get an integer value to update the Integer_Tab

      int intVal = 1000;
      smaxStringVal = (String) new Integer(intVal).toString();
      Integer maxIntegerVal = new Integer(smaxStringVal);
      maxFloatVal = new Float(smaxStringVal);

      // to set the Float
      cstmt.setObject(1, maxFloatVal, java.sql.Types.INTEGER);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Integer_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      Object oIntegerVal = rs.getObject(1);
      rIntegerVal = new Integer(oIntegerVal.toString());

      msg.addOutputMsg("" + maxIntegerVal, "" + rIntegerVal);
      if (rIntegerVal.compareTo(maxIntegerVal) == 0) {
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
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject171
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
  public void testSetObject171() throws Fault {
    Float maxFloatVal;
    Float rFloatVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);

      cstmt = conn.prepareCall("{call Real_In_Min(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Real_Tab", 1, sqlp, conn);
      maxFloatVal = new Float(smaxStringVal);
      // to set the Float
      cstmt.setObject(1, maxFloatVal, java.sql.Types.REAL);
      cstmt.executeUpdate();

      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");

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
   * @testName: testSetObject172
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
  public void testSetObject172() throws Fault {
    Float minFloatVal;
    Float rFloatVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);

      cstmt = conn.prepareCall("{call Real_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Real_Tab", 2, sqlp, conn);
      minFloatVal = new Float(sminStringVal);

      // to set the Value
      cstmt.setObject(1, minFloatVal, java.sql.Types.REAL);
      cstmt.executeUpdate();

      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");

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
   * @testName: testSetObject173
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
  public void testSetObject173() throws Fault {
    Float maxFloatVal;
    Float rFloatVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);

      cstmt = conn.prepareCall("{call Float_In_Min(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Float_Tab", 1, sqlp, conn);
      maxFloatVal = new Float(smaxStringVal);

      // to set the Float
      cstmt.setObject(1, maxFloatVal, java.sql.Types.FLOAT);
      cstmt.executeUpdate();
      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");

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
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject174
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
  public void testSetObject174() throws Fault {
    Float minFloatVal;
    Float rFloatVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);

      cstmt = conn.prepareCall("{call Float_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Float_Tab", 2, sqlp, conn);
      minFloatVal = new Float(sminStringVal);

      // to set the Value
      cstmt.setObject(1, minFloatVal, java.sql.Types.FLOAT);
      cstmt.executeUpdate();

      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");

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
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject177
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
  public void testSetObject177() throws Fault {
    Float maxFloatVal;
    Float rDecimalVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      cstmt = conn.prepareCall("{call Decimal_In_Max(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Decimal_Tab", 1, sqlp, conn);
      maxFloatVal = new Float(smaxStringVal);

      // to set the Float
      cstmt.setObject(1, maxFloatVal, java.sql.Types.DECIMAL, 15);
      cstmt.executeUpdate();

      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Decimal_Query_Max", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      Object oDecimalVal = rs.getObject(1);
      rDecimalVal = new Float(oDecimalVal.toString());
      msg.addOutputMsg("" + maxFloatVal, "" + rDecimalVal);

      if ((rDecimalVal.compareTo(maxFloatVal) == 0)) {
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
        rsSch.dropTab("Decimal_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject178
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType, int scale) method,update the column Null_Val of Decimal_Tab
   * with the minimum value of Decimal_Tab. Call the getObject(int columnno)
   * method to retrieve this value. Extract the minimum value from the
   * tssql.stmt file. Compare this value with the value returned by the
   * getObject(int columnno) method. Both the values should be equal.
   */
  public void testSetObject178() throws Fault {
    Float minFloatVal;
    BigDecimal rDecimalVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);

      cstmt = conn.prepareCall("{call Decimal_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);
      BigDecimal minDecimalVal = new BigDecimal(sminStringVal);
      minFloatVal = new Float(sminStringVal);

      // to set the Value
      cstmt.setObject(1, minFloatVal, java.sql.Types.DECIMAL, 15);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");

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
        rsSch.dropTab("Decimal_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject179
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
   * method.Both the values should be equal.
   */
  public void testSetObject179() throws Fault {
    Float maxFloatVal;
    Float rDecimalVal;
    String smaxStringVal = null;
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      cstmt = conn.prepareCall("{call Numeric_In_Max(?)}");

      msg.setMsg("extract the Minimum Value to be Updated");
      smaxStringVal = rsSch.extractVal("Numeric_Tab", 1, sqlp, conn);
      maxFloatVal = new Float(smaxStringVal);

      // to set the Float
      cstmt.setObject(1, maxFloatVal, java.sql.Types.NUMERIC, 15);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");

      String Max_Val_Query = sqlp.getProperty("Numeric_Query_Max", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      Object oDecimalVal = rs.getObject(1);
      rDecimalVal = new Float(oDecimalVal.toString());

      msg.addOutputMsg("" + maxFloatVal, "" + rDecimalVal);
      if ((rDecimalVal.compareTo(maxFloatVal) == 0)) {
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
        rsSch.dropTab("Numeric_Tab", conn);
      } catch (Exception e) {

      }
    }
  }

  /*
   * @testName: testSetObject180
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
  public void testSetObject180() throws Fault {
    Float minFloatVal;
    BigDecimal rDecimalVal;
    String sminStringVal = null;
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);

      cstmt = conn.prepareCall("{call Numeric_In_Null(?)}");

      msg.setMsg("extract the Maximum Value to be Updated");
      sminStringVal = rsSch.extractVal("Numeric_Tab", 2, sqlp, conn);
      BigDecimal minDecimalVal = new BigDecimal(sminStringVal);
      minFloatVal = new Float(sminStringVal);
      // to set the Value
      cstmt.setObject(1, minFloatVal, java.sql.Types.NUMERIC, 15);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();
      msg.setMsg(
          "query from the database to check the call of cstmt.executeUpdate");
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
        rsSch.dropTab("Numeric_Tab", conn);
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
