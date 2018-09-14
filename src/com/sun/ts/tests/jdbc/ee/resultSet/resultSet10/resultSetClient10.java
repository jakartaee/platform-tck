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
 * @(#)resultSetClient10.java	1.24 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet10;

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
 * The resultSetClient10 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 99/10/12
 */

public class resultSetClient10 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.resultSet.resultSet10";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements
  private transient Connection conn = null;

  private transient DatabaseMetaData dbmd = null;

  private Statement stmt = null;

  private PreparedStatement pstmt = null;

  private DataSource ds1 = null;

  private String drManager = null;

  private String sqlStmt = null;

  private dbSchema dbSch = null;

  private rsSchema rsSch = null;

  private Properties props = null;

  private Properties sqlp = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    resultSetClient10 theTests = new resultSetClient10();
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
        /*
         * sqlp=new Properties(); sqlStmt= p.getProperty("rsQuery","");
         * InputStream istr= new FileInputStream(sqlStmt); sqlp.load(istr);
         */
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
        dbSch = new dbSchema();
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
   * @testName: testGetByte01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that gets the
   * maximum value of table Tinyint_Tab.Call the getByte(int columnIndex)
   * method. Compare the returned result with the value extracted from
   * tssql.stmt file.Both of them should be equal and the returned result must
   * be equal to the Maximum Value of JDBC Tinyint datatype.
   */
  public void testGetByte01() throws Fault {
    byte retValue;
    byte maxByteVal;
    String smaxByteVal = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Tinyint_Query_Max", "");
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Tinyint_Tab");
      retValue = rs.getByte(1);
      msg.setMsg("Calling extractVal to get the Maximum Value of Byte");
      smaxByteVal = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);
      maxByteVal = Byte.parseByte(smaxByteVal);

      msg.addOutputMsg(" " + maxByteVal, " " + retValue);
      if (maxByteVal == retValue) {
        msg.setMsg("getByte method returns : " + retValue);
      } else {
        msg.printTestError(
            "getByte method does not return the Maximum value from Tinyint",
            "test getByte Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetByte02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that gets the
   * minimum value of table Tinyint_Tab.Call the getByte(int columnIndex)
   * method. Compare the returned result with the value extracted from
   * tssql.stmt file.Both of them should be equal and the returned result must
   * be equal to the minimum Value of JDBC Tinyint datatype.
   */
  public void testGetByte02() throws Fault {
    byte retValue;
    byte minByteVal;
    String sminByteVal = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Tinyint_Query_Min", "");
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Tinyint_Tab");
      retValue = rs.getByte(1);
      sminByteVal = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);
      minByteVal = Byte.parseByte(sminByteVal);

      msg.addOutputMsg("" + minByteVal, "" + retValue);
      if (minByteVal == retValue) {
        msg.setMsg("getByte method returns : " + retValue);
      } else {
        msg.printTestError(
            "getByte method does not return the Minimum value from Tinyint ",
            "test getByte Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetByte03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Tinyint_Tab.Call the getByte(int columnIndex)
   * method.Check if it returns the value zero.
   */
  public void testGetByte03() throws Fault {
    byte retValue;
    String snullByteVal = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Tinyint_Query_Null", "");
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Tinyint_Tab");
      retValue = rs.getByte(1);

      if (retValue == 0) {
        msg.setMsg(
            "Calling getByte method on a SQL Null column returns" + retValue);

      } else {
        msg.printTestError("getByte method does not return the value zero ",
            "test getByte Failed");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetByte76
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:408;
   * JDBC:JAVADOC:409; JDBC:JAVADOC:442; JDBC:JAVADOC:443; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that gets the
   * maximum value of table Tinyint_Tab.Call the getByte(String columnName)
   * method. Compare the returned result with the value extracted from
   * tssql.stmt file.Both of them should be equal and the returned result must
   * be equal to the Maximum Value of JDBC Tinyint datatype.
   */
  public void testGetByte76() throws Fault {
    byte retValue;
    byte maxByteVal;
    String smaxByteVal = null;
    ResultSet rs = null;
    ResultSetMetaData rsmd = null;
    String columname = null;
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Tinyint_Query_Max", "");
      rs = stmt.executeQuery(Max_Val_Query);
      rsmd = rs.getMetaData();
      rs.next();
      msg.setMsg("Calling getByte on Tinyint_Tab");
      columname = rsmd.getColumnName(1);
      retValue = rs.getByte(columname);
      msg.setMsg("Calling extractVal to get the Maximum Value of Byte");
      smaxByteVal = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);
      maxByteVal = Byte.parseByte(smaxByteVal);
      msg.addOutputMsg("" + maxByteVal, "" + retValue);
      if (maxByteVal == retValue) {
        msg.setMsg("getByte method returns : " + retValue);
      } else {
        msg.printTestError(
            "getByte method does not return the Maximum value from Tinyint",
            "Call to getByte is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetByte77
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:408;
   * JDBC:JAVADOC:409; JDBC:JAVADOC:442; JDBC:JAVADOC:443; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that gets the
   * minimum value of table Tinyint_Tab.Call the getByte(String columnName)
   * method. Compare the returned result with the value extracted from
   * tssql.stmt file.Both of them should be equal and the returned result must
   * be equal to the minimum Value of JDBC Tinyint datatype.
   */
  public void testGetByte77() throws Fault {
    byte retValue;
    byte minByteVal;
    String sminByteVal = null;
    ResultSet rs = null;
    ResultSetMetaData rsmd = null;
    String columname = null;
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Tinyint_Query_Min", "");
      rs = stmt.executeQuery(Min_Val_Query);
      rsmd = rs.getMetaData();
      rs.next();
      msg.setMsg("Calling getByte on Tinyint_Tab");
      columname = rsmd.getColumnName(1);
      retValue = rs.getByte(columname);
      sminByteVal = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);
      minByteVal = Byte.parseByte(sminByteVal);

      msg.addOutputMsg("" + minByteVal, "" + retValue);
      if (minByteVal == retValue) {
        msg.setMsg("getByte method returns : " + retValue);
      } else {
        msg.printTestError(
            "getByte method does not return the Minimum value from Tinyint ",
            "Call to getByte is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetByte78
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:408;
   * JDBC:JAVADOC:409; JDBC:JAVADOC:442; JDBC:JAVADOC:443; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Tinyint_Tab.Call the getByte(String columnName)
   * method.Check if it returns the value zero.
   */
  public void testGetByte78() throws Fault {
    byte retValue;
    String snullByteVal = null;
    ResultSet rs = null;
    ResultSetMetaData rsmd = null;
    String columname = null;
    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Tinyint_Query_Null", "");
      rs = stmt.executeQuery(Null_Val_Query);
      rsmd = rs.getMetaData();
      rs.next();
      msg.setMsg("Calling getByte on Tinyint_Tab");
      columname = rsmd.getColumnName(1);
      retValue = rs.getByte(columname);

      if (retValue == 0) {
        msg.setMsg(
            "Calling getByte method on a SQL Null column returns" + retValue);

      } else {
        msg.printTestError("getByte method does not return the value zero ",
            "Call to getByte is Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetByte04
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the maximum value of table Smallint_Tab with
   * the maximum value of table Tinyint_Tab.Now execute a query to get the
   * maximum value of Smallint_Tab table and retrieve the result of the query
   * using the getByte(int columnIndex) method.Compare the returned value, with
   * the maximum value of table Tinyint_Tab extracted from the tssql.stmt file.
   * Both of them should be equal.
   */
  public void testGetByte04() throws Fault {
    byte retValue;
    byte maxByteVal;
    short shortVal;
    ResultSet rs = null;
    String str = null;
    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);
      // get the maximum value of Byte
      str = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);
      String sPrepStatement = sqlp.getProperty("Smallint_Tab_Max_Update", null);
      shortVal = Short.parseShort(str);
      pstmt = conn.prepareStatement(sPrepStatement);
      pstmt.setShort(1, shortVal);
      pstmt.executeUpdate();
      String Max_Val_Query = sqlp.getProperty("Smallint_Query_Max", null);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Smallint_Tab");
      retValue = rs.getByte(1);
      maxByteVal = Byte.parseByte(str);

      msg.addOutputMsg("" + maxByteVal, "" + retValue);
      if (maxByteVal == retValue) {
        msg.setMsg("getByte method returns : " + retValue);
      } else {
        msg.printTestError(
            "getByte method does not return the Maximum value from Smallint",
            "test getByte Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");
    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        pstmt.close();
        rs.close();
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetByte05
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the minimum value of table Smallint_Tab with
   * the minimum value of table Tinyint_Tab.Now execute a query to get the
   * minimum value of Smallint_Tab table and retrieve the result of the query
   * using the getByte(int columnIndex) method.Compare the returned value, with
   * the minimum value of table Tinyint_Tab extracted from the tssql.stmt file.
   * Both of them should be equal.
   */

  public void testGetByte05() throws Fault {
    byte retValue;
    byte minByteVal;
    short shortVal;
    ResultSet rs = null;
    String str = null;
    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);
      // get the minimum value of Byte
      str = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);
      String sPrepStatement = sqlp.getProperty("Smallint_Tab_Min_Update", null);
      shortVal = Short.parseShort(str);
      pstmt = conn.prepareStatement(sPrepStatement);
      pstmt.setShort(1, shortVal);
      pstmt.executeUpdate();
      String Min_Val_Query = sqlp.getProperty("Smallint_Query_Min", null);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Smallint_Tab");
      retValue = rs.getByte(1);
      minByteVal = Byte.parseByte(str);

      msg.addOutputMsg("" + minByteVal, "" + retValue);
      if (minByteVal == retValue) {
        msg.setMsg("getByte method returns : " + retValue);
      } else {
        msg.printTestError(
            "getByte method does not return the Minimum value from Smallint",
            "test getByte Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");
    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        pstmt.close();
        rs.close();
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetByte06
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Smallint_Tab.Call the getByte(int columnIndex)
   * method.Check if it returns the value zero.
   */
  public void testGetByte06() throws Fault {
    byte retValue;
    String snullByteVal = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Smallint_Query_Null", "");
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Smallint_Tab");
      retValue = rs.getByte(1);

      if (retValue == 0) {
        msg.setMsg(
            "Calling getByte method on a SQL Null column returns" + retValue);

      } else {
        msg.printTestError("getByte method does not return the value zero ",
            "test getByte Failed");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");
    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetByte07
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the maximum value of table Integer_Tab with the
   * maximum value of table Tinyint_Tab.Now execute a query to get the maximum
   * value of Integer_Tab table and retrieve the result of the query using the
   * getByte(int columnIndex) method.Compare the returned value, with the
   * maximum value of table Tinyint_Tab extracted from the tssql.stmt file. Both
   * of them should be equal.
   */

  public void testGetByte07() throws Fault {
    byte retValue;
    byte maxByteVal;
    int intVal;
    ResultSet rs = null;
    String str = null;
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);
      // get the maximum value of Byte
      str = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);
      String sPrepStatement = sqlp.getProperty("Integer_Tab_Max_Update", null);
      intVal = Integer.parseInt(str);
      pstmt = conn.prepareStatement(sPrepStatement);
      pstmt.setInt(1, intVal);
      pstmt.executeUpdate();
      String Max_Val_Query = sqlp.getProperty("Integer_Query_Max", null);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Integer_Tab");
      retValue = rs.getByte(1);
      maxByteVal = Byte.parseByte(str);

      msg.addOutputMsg("" + maxByteVal, "" + retValue);
      if (maxByteVal == retValue) {
        msg.setMsg("getByte method returns : " + retValue);
      } else {
        msg.printTestError(
            "getByte method does not return the Maximum value from Integer table ",
            "test getByte Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");
    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        pstmt.close();
        rs.close();
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetByte08
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the minimum value of table Integer_Tab with the
   * minimum value of table Tinyint_Tab.Now execute a query to get the minimum
   * value of Integer_Tab table and retrieve the result of the query using the
   * getByte(int columnIndex) method.Compare the returned value, with the
   * minimum value of table Tinyint_Tab extracted from the tssql.stmt file. Both
   * of them should be equal.
   */
  public void testGetByte08() throws Fault {
    byte retValue;
    byte minByteVal;
    int intVal;
    ResultSet rs = null;
    String str = null;
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);
      // get the minimum value of Byte
      str = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);
      String sPrepStatement = sqlp.getProperty("Integer_Tab_Min_Update", null);
      intVal = Integer.parseInt(str);
      pstmt = conn.prepareStatement(sPrepStatement);
      pstmt.setInt(1, intVal);
      pstmt.executeUpdate();
      String Min_Val_Query = sqlp.getProperty("Integer_Query_Min", null);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Integer_Tab");
      retValue = rs.getByte(1);
      minByteVal = Byte.parseByte(str);

      msg.addOutputMsg("" + minByteVal, "" + retValue);
      if (minByteVal == retValue) {
        msg.setMsg("getByte method returns : " + retValue);
      } else {
        msg.printTestError(
            "getByte method does not return the Minimum value from Integer table ",
            "test getByte Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");
    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        pstmt.close();
        rs.close();
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetByte09
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Integer_Tab.Call the getByte(int columnIndex)
   * method.Check if it returns the value zero.
   */
  public void testGetByte09() throws Fault {
    byte retValue;
    String snullByteVal = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Integer_Query_Null", "");
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Integer_Tab");
      retValue = rs.getByte(1);

      if (retValue == 0) {
        msg.setMsg(
            "Calling getByte method on a SQL Null column returns" + retValue);

      } else {
        msg.printTestError("getByte method does not return the value zero ",
            "test getByte Failed");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetByte13
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the maximum value of table Real_Tab with the
   * maximum value of table Tinyint_Tab.Now execute a query to get the maximum
   * value of Real_Tab table and retrieve the result of the query using the
   * getByte(int columnIndex) method.Compare the returned value, with the
   * maximum value of table Tinyint_Tab extracted from the tssql.stmt file. Both
   * of them should be equal.
   */

  public void testGetByte13() throws Fault {
    byte retValue;
    byte maxByteVal;
    float floatVal;
    ResultSet rs = null;
    String str = null;
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);
      // get the maximum value of Byte
      str = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);
      String sPrepStatement = sqlp.getProperty("Real_Tab_Max_Update", null);
      floatVal = Float.parseFloat(str);
      pstmt = conn.prepareStatement(sPrepStatement);
      pstmt.setFloat(1, floatVal);
      pstmt.executeUpdate();
      String Max_Val_Query = sqlp.getProperty("Real_Query_Max", null);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Real_Tab");
      retValue = rs.getByte(1);
      maxByteVal = Byte.parseByte(str);

      msg.addOutputMsg("" + maxByteVal, "" + retValue);
      if (maxByteVal == retValue) {
        msg.setMsg("getByte method returns : " + retValue);
      } else {
        msg.printTestError(
            "getByte method does not return the Maximum value from Real table ",
            "test getByte Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");
    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        pstmt.close();
        rs.close();
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetByte14
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the minimum value of table Real_Tab with the
   * minimum value of table Tinyint_Tab.Now execute a query to get the minimum
   * value of Real_Tab table and retrieve the result of the query using the
   * getByte(int columnIndex) method.Compare the returned value, with the
   * minimum value of table Tinyint_Tab extracted from the tssql.stmt file. Both
   * of them should be equal.
   */
  public void testGetByte14() throws Fault {
    byte retValue;
    byte minByteVal;
    float floatVal;
    ResultSet rs = null;
    String str = null;
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);
      // get the minimum value of Byte
      str = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);
      String sPrepStatement = sqlp.getProperty("Real_Tab_Min_Update", null);
      floatVal = Float.parseFloat(str);
      pstmt = conn.prepareStatement(sPrepStatement);
      pstmt.setFloat(1, floatVal);
      pstmt.executeUpdate();
      String Min_Val_Query = sqlp.getProperty("Real_Query_Min", null);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Real_Tab");
      retValue = rs.getByte(1);
      minByteVal = Byte.parseByte(str);

      msg.addOutputMsg("" + minByteVal, "" + retValue);
      if (minByteVal == retValue) {
        msg.setMsg("getByte method returns : " + retValue);
      } else {
        msg.printTestError(
            "getByte method does not return the Minimum value from Real table ",
            "test getByte Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");
    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        pstmt.close();
        rs.close();
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetByte15
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Real_Tab.Call the getByte(int columnIndex)
   * method.Check if it returns the value zero.
   */
  public void testGetByte15() throws Fault {
    byte retValue;
    String snullByteVal = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Real_Tab", sqlp, conn);
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Real_Query_Null", "");
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Real_Tab");
      retValue = rs.getByte(1);

      if (retValue == 0) {
        msg.setMsg(
            "Calling getByte method on a SQL Null column returns" + retValue);

      } else {
        msg.printTestError("getByte method does not return the value zero ",
            "test getByte Failed");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetByte16
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the maximum value of table Float_Tab with the
   * maximum value of table Tinyint_Tab.Now execute a query to get the maximum
   * value of Float_Tab table and retrieve the result of the query using the
   * getByte(int columnIndex) method.Compare the returned value, with the
   * maximum value of table Tinyint_Tab extracted from the tssql.stmt file. Both
   * of them should be equal.
   */
  public void testGetByte16() throws Fault {
    byte retValue;
    byte maxByteVal;
    double doubleVal;
    ResultSet rs = null;
    String str = null;
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);
      // get the maximum value of Byte
      str = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);
      String sPrepStatement = sqlp.getProperty("Float_Tab_Max_Update", null);
      doubleVal = Double.parseDouble(str);
      pstmt = conn.prepareStatement(sPrepStatement);
      pstmt.setDouble(1, doubleVal);
      pstmt.executeUpdate();
      String Max_Val_Query = sqlp.getProperty("Float_Query_Max", null);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Float_Tab");
      retValue = rs.getByte(1);
      maxByteVal = Byte.parseByte(str);

      msg.addOutputMsg("" + maxByteVal, "" + retValue);
      if (maxByteVal == retValue) {
        msg.setMsg("getByte method returns : " + retValue);
      } else {
        msg.printTestError(
            "getByte method does not return the Maximum value from Float table ",
            "test getByte Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");
    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        pstmt.close();
        rs.close();
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetByte17
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the minimum value of table Float_Tab with the
   * minimum value of table Tinyint_Tab.Now execute a query to get the minimum
   * value of Float_Tab table and retrieve the result of the query using the
   * getByte(int columnIndex) method.Compare the returned value, with the
   * minimum value of table Tinyint_Tab extracted from the tssql.stmt file. Both
   * of them should be equal.
   */
  public void testGetByte17() throws Fault {
    byte retValue;
    byte minByteVal;
    double doubleVal;
    ResultSet rs = null;
    String str = null;
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);
      // get the minimum value of Byte
      str = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);
      String sPrepStatement = sqlp.getProperty("Float_Tab_Min_Update", null);
      doubleVal = Double.parseDouble(str);
      pstmt = conn.prepareStatement(sPrepStatement);
      pstmt.setDouble(1, doubleVal);
      pstmt.executeUpdate();
      String Min_Val_Query = sqlp.getProperty("Float_Query_Min", null);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Float_Tab");
      retValue = rs.getByte(1);
      minByteVal = Byte.parseByte(str);

      msg.addOutputMsg("" + minByteVal, "" + retValue);
      if (minByteVal == retValue) {
        msg.setMsg("getByte method returns : " + retValue);
      } else {
        msg.printTestError(
            "getByte method does not return the Minimum value from Float table ",
            "test getByte Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");
    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        pstmt.close();
        rs.close();
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetByte18
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Float_Tab.Call the getByte(int columnIndex)
   * method.Check if it returns the value zero.
   */
  public void testGetByte18() throws Fault {
    byte retValue;
    String snullByteVal = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Float_Tab", sqlp, conn);
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Float_Query_Null", "");
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Float_Tab");
      retValue = rs.getByte(1);

      if (retValue == 0) {
        msg.setMsg(
            "Calling getByte method on a SQL Null column returns" + retValue);

      } else {
        msg.printTestError("getByte method does not return the value zero ",
            "test getByte Failed");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      // Close the Statement
      stmt.close();
      // Close the database
      rsSch.dbUnConnect(conn);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }
}
