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
 * @(#)resultSetClient45.java	1.26 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet45;

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
 * The resultSetClient45 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 99/10/12
 */

public class resultSetClient45 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.resultSet.resultSet45";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements
  private transient Connection conn = null;

  private transient DatabaseMetaData dbmd = null;

  private Statement stmt = null;

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
    resultSetClient45 theTests = new resultSetClient45();
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
        dbSch = new dbSchema();
        rsSch = new rsSchema();
        stmt = conn.createStatement();
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
   * @testName: testGetString41
   *
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
   * JDBC:JAVADOC:405; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * the minimum value of Smallint_Tab table. Call the getString(String
   * columnName) method to retrieve this value.Extract the minimum value of
   * Smallint_Tab table as a String from the tssql.stmt file. Compare this value
   * with the value returned by the getString method. Both the values should be
   * equal.
   */
  public void testGetString41() throws Fault {
    try {
      // create the table
      rsSch.createTab("Smallint_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Smallint_Query_Min", "");
      ResultSet oRes = stmt.executeQuery(sQuery);
      oRes.next();
      msg.setMsg("Calling ResultSet.getString(MinimumValue)");
      msg.setMsg("get the Minimum value from the table using getString Method");
      ResultSetMetaData rsMetaData = oRes.getMetaData();
      String sColName = rsMetaData.getColumnName(1);
      String oRetVal = oRes.getString(sColName);

      msg.setMsg("get the Minimum value from the Insert String");
      String sExtVal = rsSch.extractVal("Smallint_Tab", 2, sqlp, conn);
      oRetVal = oRetVal.trim();
      sExtVal = sExtVal.trim();

      Short intRetVal = new Short(oRetVal);
      Short intExtVal = new Short(sExtVal);

      msg.addOutputMsg("" + intExtVal, "" + intRetVal);
      if (intRetVal.equals(intExtVal))
        msg.setMsg("getString returns the Minimum Value " + oRetVal);
      else {
        msg.printTestError("getString did not return the Minimum Value",
            "test getString Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getString is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getString is Failed!");

    } finally {
      try {
        stmt.close();
        // drop the table
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetString42
   *
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
   * JDBC:JAVADOC:405; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * the null column value from Smallint_Tab table. Call the getString(String
   * columnName) method.Check if it returns null.
   */
  public void testGetString42() throws Fault {
    try {
      // create the table
      rsSch.createTab("Smallint_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Smallint_Query_Null", "");
      ResultSet oRes = stmt.executeQuery(sQuery);
      oRes.next();
      msg.setMsg("Calling ResultSet.getString(NullValue)");
      msg.setMsg("get the null value from the table using getString Method");
      ResultSetMetaData rsMetaData = oRes.getMetaData();
      String sColName = rsMetaData.getColumnName(1);
      String oRetVal = oRes.getString(sColName);

      // check whether the value is null or not
      if (oRetVal == null)
        msg.setMsg("getString returns the null value " + oRetVal);
      else {
        msg.printTestError("getString did not return the null value",
            "test getString Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getString is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getString is Failed!");

    } finally {
      try {
        stmt.close();
        // drop the table
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetString43
   *
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
   * JDBC:JAVADOC:405; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * the maximum value of Integer_Tab table. Call the getString(String
   * columnName) method to retrieve this value.Extract the maximum value of
   * Integer_Tab table as a String from the tssql.stmt file. Compare this value
   * with the value returned by the getString method. Both the values should be
   * equal.
   */
  public void testGetString43() throws Fault {
    try {
      // create the table
      rsSch.createTab("Integer_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Integer_Query_Max", "");
      ResultSet oRes = stmt.executeQuery(sQuery);
      oRes.next();
      msg.setMsg("Calling ResultSet.getString(MaximumValue)");
      msg.setMsg("get the Maximum value from the table using getString Method");
      ResultSetMetaData rsMetaData = oRes.getMetaData();
      String sColName = rsMetaData.getColumnName(1);
      String oRetVal = oRes.getString(sColName);

      msg.setMsg("get the Maximum value from the Insert String");
      String sExtVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);

      oRetVal = oRetVal.trim();
      sExtVal = sExtVal.trim();
      Integer intRetVal = new Integer(oRetVal);
      Integer intExtVal = new Integer(sExtVal);

      msg.addOutputMsg("" + intExtVal, "" + intRetVal);
      if (intRetVal.equals(intExtVal))
        msg.setMsg("getString returns the Maximum Value " + oRetVal);
      else {
        msg.printTestError("getString did not return the Maximum Value",
            "test getString Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getString is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getString is Failed!");

    } finally {
      try {
        stmt.close();
        // drop the table
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetString44
   *
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
   * JDBC:JAVADOC:405; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * the minimum value of Integer_Tab table. Call the getString(String
   * columnName) method to retrieve this value.Extract the minimum value of
   * Integer_Tab table as a String from the tssql.stmt file. Compare this value
   * with the value returned by the getString method. Both the values should be
   * equal.
   */
  public void testGetString44() throws Fault {
    try {
      // create the table
      rsSch.createTab("Integer_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Integer_Query_Min", "");
      ResultSet oRes = stmt.executeQuery(sQuery);
      oRes.next();
      msg.setMsg("Calling ResultSet.getString(MinimumValue)");
      msg.setMsg("get the Minimum value from the table using getString Method");
      ResultSetMetaData rsMetaData = oRes.getMetaData();
      String sColName = rsMetaData.getColumnName(1);
      String oRetVal = oRes.getString(sColName);

      msg.setMsg("get the Minimum value from the Insert String");
      String sExtVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);

      oRetVal = oRetVal.trim();
      sExtVal = sExtVal.trim();
      Integer intRetVal = new Integer(oRetVal);
      Integer intExtVal = new Integer(sExtVal);

      msg.addOutputMsg("" + intExtVal, "" + intRetVal);
      if (intRetVal.equals(intExtVal))
        msg.setMsg("getString returns the Minimum Value " + oRetVal);
      else {
        msg.printTestError("getString did not return the Minimum Value",
            "test getString Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getString is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getString is Failed!");

    } finally {
      try {
        stmt.close();
        // drop the table
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetString45
   *
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
   * JDBC:JAVADOC:405; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * the null column value from Integer_Tab table. Call the getString(String
   * columnName) method.Check if it returns null.
   */
  public void testGetString45() throws Fault {
    try {
      // create the table
      rsSch.createTab("Integer_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Integer_Query_Null", "");
      ResultSet oRes = stmt.executeQuery(sQuery);
      oRes.next();
      msg.setMsg("Calling ResultSet.getString(NullValue)");
      msg.setMsg("get the null value from the table using getString Method");
      ResultSetMetaData rsMetaData = oRes.getMetaData();
      String sColName = rsMetaData.getColumnName(1);
      String oRetVal = oRes.getString(sColName);

      // check whether the value is null or not
      if (oRetVal == null)
        msg.setMsg("getString returns the null value " + oRetVal);
      else {
        msg.printTestError("getString did not return the null value",
            "test getString Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getString is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getString is Failed!");

    } finally {
      try {
        stmt.close();
        // drop the table
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetString47
   *
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
   * JDBC:JAVADOC:405; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * the minimum value of Real_Tab table. Call the getString(String columnName)
   * method to retrieve this value.Extract the minimum value of Real_Tab table
   * as a String from the tssql.stmt file. Compare this value with the value
   * returned by the getString method. Both the values should be equal.
   */
  public void testGetString47() throws Fault {
    try {
      // create the table
      rsSch.createTab("Real_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Real_Query_Min", "");
      ResultSet oRes = stmt.executeQuery(sQuery);
      oRes.next();
      msg.setMsg("Calling ResultSet.getString(MinimumValue)");
      msg.setMsg("get the Minimum value from the table using getString Method");
      ResultSetMetaData rsMetaData = oRes.getMetaData();
      String sColName = rsMetaData.getColumnName(1);
      String oRetVal = oRes.getString(sColName);

      msg.setMsg("get the Minimum value from the Insert String");
      String sExtVal = rsSch.extractVal("Real_Tab", 2, sqlp, conn);

      oRetVal = oRetVal.trim();
      sExtVal = sExtVal.trim();
      Float fltRetVal = new Float(oRetVal);
      Float fltExtVal = new Float(sExtVal);

      msg.addOutputMsg("" + fltExtVal, "" + fltRetVal);
      if (fltRetVal.equals(fltExtVal))
        msg.setMsg("getString returns the Minimum Value " + oRetVal);
      else {
        msg.printTestError("getString did not return the Minimum Value",
            "test getString Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getString is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getString is Failed!");

    } finally {
      try {
        stmt.close();
        // drop the table
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetString48
   *
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
   * JDBC:JAVADOC:405; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * the null column value from Real_Tab table. Call the getString(String
   * columnName) method.Check if it returns null.
   */
  public void testGetString48() throws Fault {
    try {
      // create the table
      rsSch.createTab("Real_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Real_Query_Null", "");
      ResultSet oRes = stmt.executeQuery(sQuery);
      oRes.next();
      msg.setMsg("Calling ResultSet.getString(NullValue)");
      msg.setMsg("get the null value from the table using getString Method");
      ResultSetMetaData rsMetaData = oRes.getMetaData();
      String sColName = rsMetaData.getColumnName(1);
      String oRetVal = oRes.getString(sColName);

      // check whether the value is null or not
      if (oRetVal == null)
        msg.setMsg("getString returns the null value " + oRetVal);
      else {
        msg.printTestError("getString did not return the null value",
            "test getString Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getString is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getString is Failed!");

    } finally {
      try {
        stmt.close();
        // drop the table
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetString53
   *
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
   * JDBC:JAVADOC:405; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * the minimum value of Float_Tab table. Call the getString(String columnName)
   * method to retrieve this value.Extract the minimum value of Float_Tab table
   * as a String from the tssql.stmt file. Compare this value with the value
   * returned by the getString method. Both the values should be equal.
   */
  public void testGetString53() throws Fault {
    try {
      // create the table
      rsSch.createTab("Float_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Float_Query_Min", "");
      ResultSet oRes = stmt.executeQuery(sQuery);
      oRes.next();
      msg.setMsg("Calling ResultSet.getString(MinimumValue)");
      msg.setMsg("get the Minimum value from the table using getString Method");
      ResultSetMetaData rsMetaData = oRes.getMetaData();
      String sColName = rsMetaData.getColumnName(1);
      String oRetVal = oRes.getString(sColName);

      msg.setMsg("get the Minimum value from the Insert String");
      String sExtVal = rsSch.extractVal("Float_Tab", 2, sqlp, conn);

      oRetVal = oRetVal.trim();
      sExtVal = sExtVal.trim();
      Double dbRetVal = new Double(oRetVal);
      Double dbExtVal = new Double(sExtVal);

      if (dbRetVal.equals(dbExtVal))
        msg.setMsg("getString returns the Minimum Value " + oRetVal);
      else {
        msg.printTestError("getString did not return the Minimum Value",
            "test getString Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getString is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getString is Failed!");

    } finally {
      try {
        stmt.close();
        // drop the table
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetString54
   *
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
   * JDBC:JAVADOC:405; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * the null column value from Float_Tab table. Call the getString(String
   * columnName) method.Check if it returns null.
   */
  public void testGetString54() throws Fault {
    try {
      // create the table
      rsSch.createTab("Float_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Float_Query_Null", "");
      ResultSet oRes = stmt.executeQuery(sQuery);
      oRes.next();
      msg.setMsg("Calling ResultSet.getString(NullValue)");
      msg.setMsg("get the null value from the table using getString Method");
      ResultSetMetaData rsMetaData = oRes.getMetaData();
      String sColName = rsMetaData.getColumnName(1);
      String oRetVal = oRes.getString(sColName);

      // check whether the value is null or not
      if (oRetVal == null)
        msg.setMsg("getString returns the null value " + oRetVal);
      else {
        msg.printTestError("getString did not return the null value",
            "test getString Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getString is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getString is Failed!");

    } finally {
      try {
        stmt.close();
        // drop the table
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetString58
   *
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
   * JDBC:JAVADOC:405; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * the maximum value of Decimal_Tab table. Call the getString(String
   * columnName) method to retrieve this value.Extract the maximum value of
   * Decimal_Tab table as a String from the tssql.stmt file. Compare this value
   * with the value returned by the getString method. Both the values should be
   * equal.
   */
  public void testGetString58() throws Fault {
    try {
      // create the table
      rsSch.createTab("Decimal_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Decimal_Query_Max", "");
      ResultSet oRes = stmt.executeQuery(sQuery);
      oRes.next();
      msg.setMsg("Calling.. ResultSet.getString(MaximumValue)");
      msg.setMsg("get the Maximum value from the table using getString Method");
      ResultSetMetaData rsMetaData = oRes.getMetaData();
      String sColName = rsMetaData.getColumnName(1);

      String oRetVal = oRes.getString(sColName);

      msg.setMsg("get the Maximum value from the Insert String");
      String sExtVal = rsSch.extractVal("Decimal_Tab", 1, sqlp, conn);

      oRetVal = oRetVal.trim();
      sExtVal = sExtVal.trim();
      BigDecimal bdRetVal = new BigDecimal(oRetVal);
      BigDecimal bdExtVal = new BigDecimal(sExtVal);

      msg.addOutputMsg("" + bdExtVal, "" + bdRetVal);
      if (bdRetVal.compareTo(bdExtVal) == 0)
        msg.setMsg("getString returns the Maximum Value " + oRetVal);
      else {
        msg.printTestError("getString did not return the Maximum Value",
            "test getString Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getString is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getString is Failed!");

    } finally {
      try {
        stmt.close();
        // drop the table
        rsSch.dropTab("Decimal_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetString59
   *
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
   * JDBC:JAVADOC:405; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * the minimum value of Decimal_Tab table. Call the getString(String
   * columnName) method to retrieve this value.Extract the minimum value of
   * Decimal_Tab table as a String from the tssql.stmt file. Compare this value
   * with the value returned by the getString method. Both the values should be
   * equal.
   */
  public void testGetString59() throws Fault {
    try {
      // create the table
      rsSch.createTab("Decimal_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Decimal_Query_Min", "");
      ResultSet oRes = stmt.executeQuery(sQuery);
      oRes.next();
      msg.setMsg("Calling ResultSet.getString(MinimumValue)");
      msg.setMsg("get the Minimum value from the table using getString Method");
      ResultSetMetaData rsMetaData = oRes.getMetaData();
      String sColName = rsMetaData.getColumnName(1);
      String oRetVal = oRes.getString(sColName);

      msg.setMsg("get the Minimum value from the Insert String");
      String sExtVal = rsSch.extractVal("Decimal_Tab", 2, sqlp, conn);

      oRetVal = oRetVal.trim();
      sExtVal = sExtVal.trim();
      BigDecimal bdRetVal = new BigDecimal(oRetVal);
      BigDecimal bdExtVal = new BigDecimal(sExtVal);

      msg.addOutputMsg("" + bdExtVal, "" + bdRetVal);
      if (bdRetVal.compareTo(bdExtVal) == 0)
        msg.setMsg("getString returns the Minimum Value " + oRetVal);
      else {
        msg.printTestError("getString did not return the Minimum Value",
            "test getString Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getString is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getString is Failed!");

    } finally {
      try {
        stmt.close();
        // drop the table
        rsSch.dropTab("Decimal_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetString60
   *
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
   * JDBC:JAVADOC:405; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * the null column value from Decimal_Tab table. Call the getString(String
   * columnName) method.Check if it returns null.
   */
  public void testGetString60() throws Fault {
    try {
      // create the table
      rsSch.createTab("Decimal_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Decimal_Query_Null", "");
      ResultSet oRes = stmt.executeQuery(sQuery);
      oRes.next();
      msg.setMsg("Calling ResultSet.getString(NullValue)");
      msg.setMsg("get the null value from the table using getString Method");
      ResultSetMetaData rsMetaData = oRes.getMetaData();
      String sColName = rsMetaData.getColumnName(1);
      String oRetVal = oRes.getString(sColName);

      // check whether the value is null or not
      if (oRetVal == null)
        msg.setMsg("getString returns the null value " + oRetVal);
      else {
        msg.printTestError("getString did not return the null value",
            "test getString Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getString is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getString is Failed!");

    } finally {
      try {
        stmt.close();
        // drop the table
        rsSch.dropTab("Decimal_Tab", conn);
      } catch (Exception eclean) {
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
