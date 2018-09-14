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
 * @(#)resultSetClient11.java	1.23 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet11;

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
 * The resultSetClient11 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 99/10/12
 */

public class resultSetClient11 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.resultSet.resultSet11";

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
    resultSetClient11 theTests = new resultSetClient11();
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
   * @testName: testGetByte22
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the maximum value of table Decimal_Tab with the
   * maximum value of table Tinyint_Tab.Now execute a query to get the maximum
   * value of Decimal_Tab table and retrieve the result of the query using the
   * getByte(int columnIndex) method.Compare the returned value, with the
   * maximum value of table Tinyint_Tab extracted from the tssql.stmt file. Both
   * of them should be equal.
   */
  public void testGetByte22() throws Fault {
    byte retValue;
    byte maxByteVal;
    String str = null;
    BigDecimal bigDecimalVal = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Decimal_Query_Max", "");
      str = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);
      String sPrepStatement = sqlp.getProperty("Decimal_Tab_Max_Update", null);
      bigDecimalVal = new BigDecimal(str);
      pstmt = conn.prepareStatement(sPrepStatement);
      pstmt.setBigDecimal(1, bigDecimalVal);
      pstmt.executeUpdate();
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Decimal_Tab");
      retValue = rs.getByte(1);
      maxByteVal = Byte.parseByte(str);

      msg.addOutputMsg("" + maxByteVal, "" + retValue);
      if (maxByteVal == retValue) {
        msg.setMsg("getByte method returns : " + retValue);
      } else {
        msg.printTestError(
            "getByte method does not return the Maximum value from Decimal Table ",
            "test getByte Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    }

    catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        pstmt.close();
        rs.close();
        rsSch.dropTab("Decimal_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetByte23
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the minimum value of table Decimal_Tab with the
   * minimum value of table Tinyint_Tab.Now execute a query to get the maximum
   * value of Decimal_Tab table and retrieve the result of the query using the
   * getByte(int columnIndex) method.Compare the returned value, with the
   * minimum value of table Tinyint_Tab extracted from the tssql.stmt file. Both
   * of them should be equal.
   */
  public void testGetByte23() throws Fault {
    byte retValue;
    byte minByteVal;
    String str = null;
    BigDecimal bigDecimalVal = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Decimal_Query_Min", "");
      str = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);
      String sPrepStatement = sqlp.getProperty("Decimal_Tab_Min_Update", null);
      bigDecimalVal = new BigDecimal(str);
      pstmt = conn.prepareStatement(sPrepStatement);
      pstmt.setBigDecimal(1, bigDecimalVal);
      pstmt.executeUpdate();
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Decimal_Tab");
      retValue = rs.getByte(1);
      minByteVal = Byte.parseByte(str);

      msg.addOutputMsg("" + minByteVal, "" + retValue);
      if (minByteVal == retValue) {
        msg.setMsg("getByte method returns : " + retValue);
      } else {
        msg.printTestError(
            "getByte method does not return the Minimum value from Decimal Table ",
            "test getByte Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    }

    catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        pstmt.close();
        rs.close();
        rsSch.dropTab("Decimal_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetByte24
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Double_Tab.Call the getByte(int columnIndex)
   * method.Check if it returns the value zero.
   */
  public void testGetByte24() throws Fault {
    byte retValue;
    String snullByteVal = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Decimal_Tab", sqlp, conn);
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Decimal_Query_Null", "");
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      // invoke on the getByte
      msg.setMsg("Calling  getByte on Decimal_Tab");
      retValue = rs.getByte(1);

      if (retValue == 0) {
        msg.setMsg(
            "Calling getByte method on a SQL Null column returns" + retValue);

      } else {
        msg.printTestError("getByte method does not return the value zero ",
            "test getByte Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Decimal_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetByte25
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the maximum value of table Numeric_Tab with the
   * maximum value of table Tinyint_Tab.Now execute a query to get the maximum
   * value of Numeric_Tab table and retrieve the result of the query using the
   * getByte(int columnIndex) method.Compare the returned value, with the
   * maximum value of table Tinyint_Tab extracted from the tssql.stmt file. Both
   * of them should be equal.
   */

  public void testGetByte25() throws Fault {
    byte retValue;
    byte maxByteVal;
    String str = null;
    BigDecimal bigDecimalVal = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Numeric_Query_Max", "");
      str = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);
      String sPrepStatement = sqlp.getProperty("Numeric_Tab_Max_Update", null);
      bigDecimalVal = new BigDecimal(str);
      pstmt = conn.prepareStatement(sPrepStatement);
      pstmt.setBigDecimal(1, bigDecimalVal);
      pstmt.executeUpdate();
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Numeric_Tab");
      retValue = rs.getByte(1);
      maxByteVal = Byte.parseByte(str);

      msg.addOutputMsg("" + maxByteVal, "" + retValue);
      if (maxByteVal == retValue) {
        msg.setMsg("getByte method returns : " + retValue);
      } else {
        msg.printTestError(
            "getByte method does not return the Maximum value from Numeric Table ",
            "test getByte Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    }

    catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        pstmt.close();
        rs.close();
        rsSch.dropTab("Numeric_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetByte26
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the minimum value of table Numeric_Tab with the
   * minimum value of table Tinyint_Tab.Now execute a query to get the minimum
   * value of Numeric_Tab table and retrieve the result of the query using the
   * getByte(int columnIndex) method.Compare the returned value, with the
   * minimum value of table Tinyint_Tab extracted from the tssql.stmt file. Both
   * of them should be equal.
   */
  public void testGetByte26() throws Fault {
    byte retValue;
    byte minByteVal;
    String str = null;
    BigDecimal bigDecimalVal = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Numeric_Query_Min", "");
      str = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);
      String sPrepStatement = sqlp.getProperty("Numeric_Tab_Min_Update", null);
      bigDecimalVal = new BigDecimal(str);
      pstmt = conn.prepareStatement(sPrepStatement);
      pstmt.setBigDecimal(1, bigDecimalVal);
      pstmt.executeUpdate();
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Numeric_Tab");
      retValue = rs.getByte(1);
      minByteVal = Byte.parseByte(str);

      msg.addOutputMsg("" + minByteVal, "" + retValue);
      if (minByteVal == retValue) {
        msg.setMsg("getByte method returns : " + retValue);
      } else {
        msg.printTestError(
            "getByte method does not return the Minimum value from Numeric Table ",
            "test getByte Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    }

    catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        pstmt.close();
        rs.close();
        rsSch.dropTab("Numeric_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetByte27
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Numeric_Tab.Call the getByte(int columnIndex)
   * method.Check if it returns the value zero.
   */
  public void testGetByte27() throws Fault {
    byte retValue;
    String snullByteVal = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Numeric_Query_Null", "");
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      // invoke on the getByte
      msg.setMsg("Calling  getByte on Numeric_Tab");
      retValue = rs.getByte(1);

      if (retValue == 0) {
        msg.setMsg(
            "Calling getByte method on a SQL Null column returns" + retValue);

      } else {
        msg.printTestError("getByte method does not return the value zero ",
            "test getByte Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Numeric_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetByte31
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the table Char_Tab with the maximum value of
   * table Tinyint_Tab.Now execute a query to get that value from Char_Tab table
   * and retrieve the result of the query using the getByte(int columnIndex)
   * method.Compare the returned value, with the maximum value of table
   * Tinyint_Tab extracted from the tssql.stmt file. Both of them should be
   * equal.
   */
  public void testGetByte31() throws Fault {
    byte retValue;
    byte maxByteVal;
    String str = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Char_Query_Name", "");
      str = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);
      String sPrepStatement = sqlp.getProperty("Char_Tab_Name_Update", null);
      pstmt = conn.prepareStatement(sPrepStatement);
      pstmt.setString(1, str);
      pstmt.executeUpdate();
      maxByteVal = Byte.parseByte(str);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Char_Tab");
      retValue = rs.getByte(1);

      msg.addOutputMsg("" + maxByteVal, "" + retValue);
      if (maxByteVal == retValue) {
        msg.setMsg("getByte method returns : " + retValue);
      } else {
        msg.printTestError(
            "getByte method does not return the maximum value from Char Table ",
            "test getByte Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    }

    catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        pstmt.close();
        rs.close();
        rsSch.dropTab("Char_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetByte32
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the table Char_Tab with the minimum value of
   * table Tinyint_Tab.Now execute a query to get that value from Char_Tab table
   * and retrieve the result of the query using the getByte(int columnIndex)
   * method.Compare the returned value, with the minimum value of table
   * Tinyint_Tab extracted from the tssql.stmt file. Both of them should be
   * equal.
   */

  public void testGetByte32() throws Fault {
    byte retValue;
    byte minByteVal;
    String str = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Char_Query_Name", "");
      str = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);
      String sPrepStatement = sqlp.getProperty("Char_Tab_Name_Update", null);
      pstmt = conn.prepareStatement(sPrepStatement);
      pstmt.setString(1, str);
      pstmt.executeUpdate();
      minByteVal = Byte.parseByte(str);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Char_Tab");
      retValue = rs.getByte(1);

      msg.addOutputMsg("" + minByteVal, "" + retValue);
      if (minByteVal == retValue) {
        msg.setMsg("getByte method returns : " + retValue);
      } else {
        msg.printTestError(
            "getByte method does not return the minimum value from Char Table ",
            "test getByte Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    }

    catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        pstmt.close();
        rs.close();
        rsSch.dropTab("Char_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetByte33
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Char_Tab.Call the getByte(int columnIndex)
   * method.Check if it returns the value zero.
   */

  public void testGetByte33() throws Fault {
    byte retValue;
    String snullByteVal = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Char_Tab", sqlp, conn);
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      // invoke on the getByte
      msg.setMsg("Calling  getByte on Char_Tab");
      retValue = rs.getByte(1);

      if (retValue == 0) {
        msg.setMsg(
            "Calling getByte method on a SQL Null column returns" + retValue);

      } else {
        msg.printTestError("getByte method does not return the value zero ",
            "test getByte Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Char_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetByte34
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the table Varchar_Tab with the maximum value of
   * table Tinyint_Tab.Now execute a query to get that value from Varchar_Tab
   * table and retrieve the result of the query using the getByte(int
   * columnIndex) method.Compare the returned value, with the maximum value of
   * table Tinyint_Tab extracted from the tssql.stmt file. Both of them should
   * be equal.
   */
  public void testGetByte34() throws Fault {
    byte retValue;
    byte maxByteVal;
    String str = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Varchar_Query_Name", "");
      str = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);
      String sPrepStatement = sqlp.getProperty("Varchar_Tab_Name_Update", null);
      pstmt = conn.prepareStatement(sPrepStatement);
      pstmt.setString(1, str);
      pstmt.executeUpdate();
      maxByteVal = Byte.parseByte(str);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Varchar_Tab");
      retValue = rs.getByte(1);

      msg.addOutputMsg("" + maxByteVal, "" + retValue);
      if (maxByteVal == retValue) {
        msg.setMsg("getByte method returns : " + retValue);
      } else {
        msg.printTestError(
            "getByte method does not return the maximum value from Varchar Table ",
            "test getByte Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    }

    catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        pstmt.close();
        rs.close();
        rsSch.dropTab("Varchar_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetByte35
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the table Varchar_Tab with the minimum value of
   * table Tinyint_Tab.Now execute a query to get that value from Varchar_Tab
   * table and retrieve the result of the query using the getByte(int
   * columnIndex) method.Compare the returned value, with the minimum value of
   * table Tinyint_Tab extracted from the tssql.stmt file. Both of them should
   * be equal.
   */
  public void testGetByte35() throws Fault {
    byte retValue;
    byte minByteVal;
    String str = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Varchar_Query_Name", "");
      str = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);
      String sPrepStatement = sqlp.getProperty("Varchar_Tab_Name_Update", null);
      pstmt = conn.prepareStatement(sPrepStatement);
      pstmt.setString(1, str);
      pstmt.executeUpdate();
      minByteVal = Byte.parseByte(str);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();
      msg.setMsg("Calling getByte on Varchar_Tab");
      retValue = rs.getByte(1);

      msg.addOutputMsg("" + minByteVal, "" + retValue);
      if (minByteVal == retValue) {
        msg.setMsg("getByte method returns : " + retValue);
      } else {
        msg.printTestError(
            "getByte method does not return the minimum value from Varchar Table ",
            "test getByte Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    }

    catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        pstmt.close();
        rs.close();
        rsSch.dropTab("Varchar_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }
  }

  /*
   * @testName: testGetByte36
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
   * JDBC:JAVADOC:377; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Varchar_Tab.Call the getByte(int columnIndex)
   * method.Check if it returns the value zero.
   */

  public void testGetByte36() throws Fault {
    byte retValue;
    String snullByteVal = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      // invoke on the getByte
      msg.setMsg("Calling  getByte on Varchar_Tab");
      retValue = rs.getByte(1);

      if (retValue == 0) {
        msg.setMsg(
            "Calling getByte method on a SQL Null column returns" + retValue);

      } else {
        msg.printTestError("getByte method does not return the value zero ",
            "test getByte Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getByte is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getByte is Failed!");

    } finally {
      try {
        rs.close();
        rsSch.dropTab("Varchar_Tab", conn);
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
