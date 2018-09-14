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

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet41;

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
 * The resultSetClient41 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 99/10/12
 */

public class resultSetClient41 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.resultSet.resultSet41";

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

  private csSchema csSch = null;

  private Properties props = null;

  private Properties sqlp = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    resultSetClient41 theTests = new resultSetClient41();
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
        csSch = new csSchema();
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
   * @testName: testGetBoolean67
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:406;
   * JDBC:JAVADOC:407; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that gets the
   * maximum value of table Bit_Tab.Call the getBoolean(String columnName)
   * method. Compare the returned result with the value extracted from
   * tssql.stmt file.Both of them should be equal and the returned result must
   * be equal to the Maximum Value of JDBC Bit datatype.
   */

  public void testGetBoolean67() throws Fault {
    try {
      // create the table
      rsSch.createTab("Bit_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Bit_Query_Max", "");
      ResultSet oRes = stmt.executeQuery(sQuery);
      oRes.next();
      msg.setMsg("Calling ResultSet.getBoolean(MaximumValue)");
      msg.setMsg(
          "get the Maximum value from the table using getBoolean Method");
      ResultSetMetaData rsMetaData = oRes.getMetaData();
      String sColName = rsMetaData.getColumnName(1);
      boolean oRetVal = oRes.getBoolean(sColName);

      msg.setMsg("get the Maximum value from the Insert String ");
      boolean oExtVal = rsSch.extractValAsBoolVal("Bit_Tab", 1, sqlp, conn);

      msg.addOutputMsg("" + oExtVal, "" + oRetVal);
      if (oRetVal == oExtVal)
        msg.setMsg("getBoolean returns the Maximum Value " + oRetVal);
      else {
        msg.printTestError("getBoolean did not return the Maximum Value",
            "test getBoolean Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getBoolean is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getBoolean is Failed!");

    } finally {
      try {
        stmt.close();
        // drop the table
        rsSch.dropTab("Bit_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetBoolean68
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:406;
   * JDBC:JAVADOC:407; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that gets the
   * minimum value of table Bit_Tab.Call the getBoolean(String columnName)
   * method. Compare the returned result with the value extracted from
   * tssql.stmt file.Both of them should be equal and the returned result must
   * be equal to the Minimum Value of JDBC Bit datatype.
   */
  public void testGetBoolean68() throws Fault {
    try {
      // create the table
      rsSch.createTab("Bit_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Bit_Query_Min", "");
      ResultSet oRes = stmt.executeQuery(sQuery);
      oRes.next();
      msg.setMsg("Calling ResultSet.getBoolean(MinimumValue)");
      msg.setMsg(
          "get the Minimum value from the table using getBoolean Method");
      ResultSetMetaData rsMetaData = oRes.getMetaData();
      String sColName = rsMetaData.getColumnName(1);
      boolean oRetVal = oRes.getBoolean(sColName);

      msg.setMsg("get the Minimum value from the Insert String");
      boolean oExtVal = rsSch.extractValAsBoolVal("Bit_Tab", 2, sqlp, conn);

      msg.addOutputMsg("" + oExtVal, "" + oRetVal);
      if (oRetVal == oExtVal)
        msg.setMsg("getBoolean returns the Minimum Value " + oRetVal);
      else {
        msg.printTestError("getBoolean did not return the Minimum Value",
            "test getBoolean Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getBoolean is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getBoolean is Failed!");

    } finally {
      try {
        stmt.close();
        // drop the table
        rsSch.dropTab("Bit_Tab", conn);
      } catch (Exception eclean) {
      }
    }
  }

  /*
   * @testName: testGetBoolean69
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:406;
   * JDBC:JAVADOC:407; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Bit_Tab.Call the getBoolean(String columnName)
   * method.Check if the value returned is boolean value false.
   */
  public void testGetBoolean69() throws Fault {
    try {
      // create the table
      rsSch.createTab("Bit_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Bit_Query_Null", "");
      ResultSet oRes = stmt.executeQuery(sQuery);
      oRes.next();
      msg.setMsg("Calling ResultSet.getBoolean(NullValue)");
      msg.setMsg("get the Null value from the table using getBoolean Method");
      ResultSetMetaData rsMetaData = oRes.getMetaData();
      String sColName = rsMetaData.getColumnName(1);
      boolean oRetVal = oRes.getBoolean(sColName);

      // check whether the value is boolean false
      if (oRetVal == false) {
        msg.setMsg(
            "Calling getBoolean method on a SQL Null column returns boolean "
                + oRetVal);
      } else {
        msg.printTestError("getBoolean did not return the boolean value false",
            "test getBoolean Failed");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getBoolean is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getBoolean is Failed!");

    } finally {
      try {
        stmt.close();
        // drop the table
        rsSch.dropTab("Bit_Tab", conn);
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
