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
 * @(#)stmtClient2.java	1.25 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.stmt.stmt2;

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

//Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The stmtClient2 class tests methods of Statement interface using Sun's J2EE
 * Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class stmtClient2 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.stmt.stmt2";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements
  private transient Connection conn = null;

  private ResultSet rs = null;

  private Statement stmt = null;

  private Statement statemt = null;

  private transient DatabaseMetaData dbmd = null;

  private DataSource ds1 = null;

  private dbSchema dbSch = null;

  private rsSchema rsSch = null;

  private String drManager = null;

  private Properties sqlp = null;

  private Properties props = null;

  private PreparedStatement pstmt = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    stmtClient2 theTests = new stmtClient2();
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
        /*
         * sqlp = new Properties(); String sqlStmt= p.getProperty("rsQuery","");
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

        stmt = conn.createStatement();
        dbmd = conn.getMetaData();
        dbSch = new dbSchema();
        dbSch.createData(p, conn);
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
   * @testName: testGetResultSet02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:165; JDBC:JAVADOC:166;
   * 
   * @test_Strategy: Get a Statement object and call execute() method for
   * updating a row.Then call getResultSet() method It should return a Null
   * ResultSet object
   */
  public void testGetResultSet02() throws Fault {
    ResultSet retResSet = null;
    try {

      String sSqlStmt = sqlp.getProperty("Upd_Coffee_Tab", "");
      msg.setMsg("Query String :  " + sSqlStmt);

      msg.setMsg("Calling getResultSet on Statement");
      stmt.executeUpdate(sSqlStmt);
      retResSet = stmt.getResultSet();

      if (retResSet == null) {
        msg.setMsg("getResultSet method returns a Null ResultSet object ");
      } else {
        msg.printTestError(
            " getResultSet method does not return a ResultSet object",
            "Call to getResultSet is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getResultSet is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getResultSet is Failed!");

    }
  }

  /*
   * @testName: testGetResultSetConcurrency01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:179; JDBC:JAVADOC:180;
   * JDBC:JAVADOC:362;
   * 
   * @test_Strategy: Get a Statement object and call getResultSetConcurrency()
   * method It should return an int value either CONCUR_READ_ONLY or
   * CONCUR_UPDATABLE.
   */
  public void testGetResultSetConcurrency01() throws Fault {
    int rsConcur = 0;
    try {
      rsConcur = stmt.getResultSetConcurrency();

      if ((rsConcur == ResultSet.CONCUR_READ_ONLY)
          || (rsConcur == ResultSet.CONCUR_UPDATABLE)) {
        msg.setMsg(
            "getResultSetConcurrency method returns ResultSet Concurrency mode  "
                + rsConcur);
      } else {
        msg.printTestError(
            "getResultSetConcurrency method does not return a valid value",
            "Call to getResultSetConcurrency is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getResultSetConcurrency is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getResultSetConcurrency is Failed!");

    }
  }

  /*
   * @testName: testGetResultSetType01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:181; JDBC:JAVADOC:182;
   * 
   * @test_Strategy: Get a Statement object and call getResultSetType() method
   * It should return an int value which should be either TYPE_FORWARD_ONLY or
   * TYPE_SCROLL_INSENSITIVE or TYPE_SCROLL_SENSITIVE
   */
  public void testGetResultSetType01() throws Fault {
    int rsType = 0;
    try {
      rsType = stmt.getResultSetType();

      if (rsType == ResultSet.TYPE_FORWARD_ONLY) {
        msg.setMsg(
            "getResultSetType method returns TYPE_FORWARD_ONLY" + rsType);
      } else if (rsType == ResultSet.TYPE_SCROLL_INSENSITIVE) {
        msg.setMsg("getResultSetType method returns TYPE_SCROLL_INSENSITIVE "
            + rsType);
      } else if (rsType == ResultSet.TYPE_SCROLL_SENSITIVE) {
        msg.setMsg(
            "getResultSetType method returns TYPE_SCROLL_SENSITIVE  " + rsType);
      } else {
        msg.printTestError(
            " getResultSetType method does not return a valid value",
            "Call to getResultSetType is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getResultSetType is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getResultSetType is Failed!");

    }
  }

  /*
   * @testName: testGetResultSetType02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:181; JDBC:JAVADOC:182;
   * JDBC:JAVADOC:1179; JDBC:JAVADOC:1180; JDBC:JAVADOC:359;
   * 
   * @test_Strategy: Call Connection.createStatement with the Type mode as
   * TYPE_FORWARD_ONLY and call getResultSetType() method It should return a int
   * value and the value should be equal to ResultSet.TYPE_FORWARD_ONLY
   */
  public void testGetResultSetType02() throws Fault {
    int rsType = 0;
    Statement statemt = null;
    try {
      msg.setMsg("Creating Statement object with the ResultSet Type and Type");
      statemt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
          ResultSet.CONCUR_READ_ONLY);

      // invoking the getResultSetType method
      rsType = statemt.getResultSetType();

      if (rsType == ResultSet.TYPE_FORWARD_ONLY) {
        msg.setMsg(
            "getResultSetType method returns TYPE_FORWARD_ONLY " + rsType);
      } else {
        statemt.close();
        msg.printTestError(
            " getResultSetType method does not return a valid value",
            "Call to getResultSetType is Failed!");

      }
      statemt.close();
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getResultSetType is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getResultSetType is Failed!");

    }
  }

  /*
   * @testName: testGetResultSetType03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:181; JDBC:JAVADOC:182;
   * JDBC:JAVADOC:1179; JDBC:JAVADOC:1180;
   * 
   * @test_Strategy: Call Connection.createStatement with the Type mode as
   * TYPE_SCROLL_INSENSITIVE and call getResultSetType() method It should return
   * a int value and the value should be equal to
   * ResultSet.TYPE_SCROLL_INSENSITIVE
   */
  public void testGetResultSetType03() throws Fault {
    int rsType = 0;
    Statement statemt = null;
    try {
      msg.setMsg("Creating Statement object with the ResultSet Type and Type");

      // Clearing the warnings on the connection
      // so that the recent SQLwarning can be retrieved
      conn.clearWarnings();

      statemt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY);

      // invoking the getResultSetType method
      rsType = statemt.getResultSetType();

      // Checking to see if the correct int type was returned.

      if (rsType == ResultSet.TYPE_SCROLL_INSENSITIVE) {
        msg.setMsg("getResultSetType method returns TYPE_SCROLL_INSENSITIVE "
            + rsType);
      } else {
        // Checking on the connection object for the SQLWarning.
        // The driver should report the warnings if the TYPE_SCROLL_INSENSITIVE
        // is not supported
        SQLWarning sqlw = conn.getWarnings();

        if (sqlw != null) {
          msg.setMsg(
              "TYPE_SCROLL_INSENSITIVE not supported. getResultSetType returned SQLWarnings");
          conn.clearWarnings();
          statemt.close();
        } else {
          statemt.close();
          msg.printTestError(
              "TYPE_SCROLL_INSENSITIVE not supported. getResultSetType method did not generate SQLWarning",
              "Call to getResultSetType is Failed!");
        }
      }
      statemt.close();
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getResultSetType is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getResultSetType is Failed!");

    }
  }

  /*
   * @testName: testGetUpdateCount01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:167; JDBC:JAVADOC:168;
   * 
   * @test_Strategy: Get a Statement object and call the execute() method for
   * updating a row and call getUpdateCount() method It should return a int
   * value and the value should be equal to number of rows with the specified
   * condition for update
   */
  public void testGetUpdateCount01() throws Fault {
    int updCountVal = 0;
    int rowsAffectVal = 0;
    try {
      String sSqlStmt = sqlp.getProperty("Upd_Coffee_Tab", "");
      msg.setMsg("Query String : " + sSqlStmt);
      stmt.executeUpdate(sSqlStmt);

      msg.setMsg("Calling getUpdateCount on Statement");
      updCountVal = stmt.getUpdateCount();

      String sQuery = sqlp.getProperty("Coffee_Updcount_Query", "");
      msg.setMsg("Query String : " + sQuery);
      ResultSet rs1 = stmt.executeQuery(sQuery);
      rs1.next();

      rowsAffectVal = rs1.getInt(1);
      msg.setMsg(
          "Number of Rows Affected by Update Statement " + rowsAffectVal);

      if (updCountVal == rowsAffectVal) {
        msg.setMsg("getUpdateCount method returns :" + updCountVal);
      } else {
        msg.printTestError(" getUpdateCount method returns a invalid value",
            "Call to getUpdateCount is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getUpdateCount is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getUpdateCount is Failed!");

    }
  }

  /*
   * @testName: testGetUpdateCount02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:167; JDBC:JAVADOC:168;
   * 
   * @test_Strategy: Get a Statement object and call the execute() method for
   * selecting a non-existent row and call getUpdateCount() method It should
   * return a int value and the value should be equal to -1
   */
  public void testGetUpdateCount02() throws Fault {
    int updCountVal = 0;
    try {
      String sSqlStmt = sqlp.getProperty("SelCoffeeNull", "");
      msg.setMsg("Query String : " + sSqlStmt);
      stmt.execute(sSqlStmt);

      // invoke on the getUpdateCount
      msg.setMsg("Calling getMoreResults on Statement");
      updCountVal = stmt.getUpdateCount();

      if (updCountVal == -1) {
        msg.setMsg("getUpdateCount method returns :" + updCountVal);
      } else {
        msg.printTestError(" getUpdateCount method returns a invalid value",
            "Call to getUpdateCount is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getUpdateCount is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getUpdateCount is Failed!");

    }
  }

  /*
   * @testName: testGetWarnings
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:157; JDBC:JAVADOC:158;
   * 
   * @test_Strategy: Get a Statement object and call getWarnings() method should
   * return an SQLWarning object
   */
  public void testGetWarnings() throws Fault {
    try {

      rsSch.createTab("Integer_Tab", sqlp, conn);

      msg.setMsg("get the CallableStatement object");
      CallableStatement cstmt = conn.prepareCall("{call Integer_Proc(?,?,?)}");
      msg.setMsg("The Callable Statement " + cstmt);

      msg.setMsg("register the output parameters");
      cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
      cstmt.registerOutParameter(2, java.sql.Types.INTEGER);
      cstmt.registerOutParameter(3, java.sql.Types.INTEGER);

      msg.setMsg("execute the procedure");
      cstmt.executeUpdate();

      msg.setMsg("invoke getInt method");
      int nRetVal = cstmt.getInt(1);
      Statement state = (Statement) cstmt;

      msg.setMsg("Calling getWarnings method");
      SQLWarning sWarning = state.getWarnings();

      if (sWarning instanceof SQLWarning) {
        msg.setMsg("getWarnings method returns a SQLWarning object");
      } else if (sWarning == null) {
        msg.setMsg("getWarnings() method returns a null SQLWarning Object");
      }
      rsSch.dropTab("Integer_Tab", conn);
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getWarnings is Failed!");

    } catch (Exception ex) {
      msg.printError(ex, "Call to getWarnings is Failed!");

    }
  }

  /*
   * @testName: testSetFetchDirection04
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:171; JDBC:JAVADOC:172;
   * 
   * @test_Strategy: Get a Statement object and call the setFetchDirection(int
   * direction) method with an invalid value and it should throw an SQLException
   */
  public void testSetFetchDirection04() throws Fault {
    boolean sqlExceptFlag = false;
    try {

      msg.setMsg("Calling setFetchDirection method ");
      try {
        stmt.setFetchDirection(-1);
      } catch (SQLException sqe) {
        TestUtil.printStackTrace(sqe);

        sqlExceptFlag = true;
      }

      if (sqlExceptFlag) {
        msg.setMsg(
            "setFetchDirection method does not sets the invalid direction for the ResultSet ");
      } else {
        msg.printTestError(
            "setFetchDirection method sets the invalid direction for ResultSet",
            "Call to setFetchDirection is Failed");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to setFetchDirection is Failed");

    }
  }

  /*
   * @testName: testSetFetchSize02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:175; JDBC:JAVADOC:176;
   * 
   * @test_Strategy: Get a Statement object and call the setFetchSize(int rows)
   * method with the value of Statement.getMaxRows and call getFetchSize()
   * method and it should return a int value that is been set
   */
  public void testSetFetchSize02() throws Fault {
    int maxFetchSizeVal = 50;
    int maxRowsVal = 0;
    int retVal = 0;

    try {
      msg.setMsg("invoking the getMaxRows method");

      stmt.setMaxRows(maxFetchSizeVal);
      maxRowsVal = stmt.getMaxRows();
      msg.setMsg("Maximum Rows that Statement can contain " + maxRowsVal);
      if (maxRowsVal == 0) {
        // reset to something reasonable
        maxRowsVal = 25;
        msg.setMsg("Maximum Rows was 0 (zero) so reset to 25");
      }

      msg.setMsg("Calling the setFetchSize method");
      stmt.setFetchSize(maxRowsVal);

      msg.setMsg("invoke on the getFetchSize");
      retVal = stmt.getFetchSize();

      String str1 = "maxFetchSizeVal = " + maxFetchSizeVal + ".   retVal = "
          + retVal + ".";
      msg.setMsg(str1);
      msg.setMsg("maxRowsVal = " + maxRowsVal);
      if (maxFetchSizeVal == retVal) {
        msg.setMsg(
            "setFetchSize method sets the value as FetchSize for ResultSet");
      } else {
        msg.printTestError(
            "setFetchSize method does not set the value as Fetch Size for ResultSet",
            "Call to setFetchSize is Failed");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setFetchSize is Failed");

    } catch (Exception e) {
      msg.printError(e, "Call to setFetchSize is Failed");

    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      dbSch.destroyData(conn);
      // Close the Statement object
      stmt.close();
      // close the Database
      rsSch.dbUnConnect(conn);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }

}
