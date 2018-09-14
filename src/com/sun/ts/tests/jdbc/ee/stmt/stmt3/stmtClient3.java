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
 * @(#)stmtClient3.java	1.18 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.stmt.stmt3;

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
 * The stmtClient3 class tests methods of Statement interface using Sun's J2EE
 * Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class stmtClient3 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.stmt.stmt3";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private Statement stmt = null;

  private DataSource ds1 = null;

  private String drManager = null;

  private Properties sqlp = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    stmtClient3 theTests = new stmtClient3();
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
   * @testName: testSetFetchSize05
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:175; JDBC:JAVADOC:176;
   *
   * @test_Strategy: Get a Statement object and call the setFetchSize(int rows)
   * method with the negative value and it should throw SQLException
   *
   */
  public void testSetFetchSize05() throws Fault {
    int maxFetchSizeVal = 0;
    String sMaxFetchSizeVal = null;
    boolean sqlExceptFlag = false;
    try {

      sMaxFetchSizeVal = sqlp.getProperty("Max_Set_Val", "");
      maxFetchSizeVal = Integer.parseInt(sMaxFetchSizeVal);
      maxFetchSizeVal = maxFetchSizeVal * (-1);
      msg.setMsg("Maximum Value to be set as Fetch Size " + maxFetchSizeVal);

      msg.setMsg("Calling setFetchSize method");
      try {
        stmt.setFetchSize(maxFetchSizeVal);
      } catch (SQLException sqle) {
        TestUtil.printStackTrace(sqle);

        sqlExceptFlag = true;
      }
      if (sqlExceptFlag) {
        msg.setMsg("setFetchSize method does not set the invalid value ");
      } else {
        msg.printTestError("setFetchSize method sets the Invalid value ",
            "Call to setFetchSize is Failed");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to setFetchSize is Failed!");

    }
  }

  /*
   * @testName: testSetMaxFieldSize01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:143; JDBC:JAVADOC:144;
   *
   * @test_Strategy: Get a Statement object and call the setMaxFieldSize(int
   * max) method and call getMaxFieldSize() method and it should return an
   * integer value that is been set
   *
   */
  public void testSetMaxFieldSize01() throws Fault {
    int maxFieldSizeVal = 0;
    String sMaxFieldSizeVal = null;
    int retVal = 0;
    try {
      sMaxFieldSizeVal = sqlp.getProperty("Max_Set_Val", "");
      maxFieldSizeVal = Integer.parseInt(sMaxFieldSizeVal);

      // for Maximum Portability the value is multiplied with 256
      maxFieldSizeVal = maxFieldSizeVal * 256;
      msg.setMsg("Maximum Field Size Value to be set " + maxFieldSizeVal);

      msg.setMsg("Calling maxFieldSize method ");
      stmt.setMaxFieldSize(maxFieldSizeVal);

      msg.setMsg("invoke on the getMaxFieldSize");
      retVal = stmt.getMaxFieldSize();

      if (maxFieldSizeVal == retVal) {
        msg.setMsg(
            "setMaxFieldSize method sets the value for Maximum Field Size");
      } else {
        msg.printTestError(
            "setMaxFieldSize method does not set the value for Maximum Field Size",
            "Call to setMaxFieldSize is Failed");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setMaxFieldSize is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setMaxFieldSize is Failed!");

    }
  }

  /*
   * @testName: testSetMaxFieldSize02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:143; JDBC:JAVADOC:144;
   *
   * @test_Strategy: Get a Statement object and call the setMaxFieldSize(int
   * max) method with an invalid value (negative value) and It should throw a
   * SQLException
   *
   */

  public void testSetMaxFieldSize02() throws Fault {
    int maxFieldSizeVal = 0;
    String sMaxFieldSizeVal = null;
    boolean sqlExceptFlag = false;

    try {
      sMaxFieldSizeVal = sqlp.getProperty("Max_Set_Val", "");
      maxFieldSizeVal = Integer.parseInt(sMaxFieldSizeVal);
      maxFieldSizeVal = maxFieldSizeVal * (-1);
      msg.setMsg("Rows Value to be set " + maxFieldSizeVal);

      msg.setMsg("Calling the setMaxFieldSize method");
      try {
        stmt.setMaxFieldSize(maxFieldSizeVal);
      } catch (SQLException sqe) {
        TestUtil.printStackTrace(sqe);

        sqlExceptFlag = true;
      }

      if (sqlExceptFlag) {
        msg.setMsg("setMaxFieldSize method does not set the Invalid value ");
      } else {
        msg.printTestError("setMaxFieldSize method sets the Invalid value",
            "Call to setMaxFieldSize is Failed");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to setMaxFieldSize is Failed!");

    }
  }

  /*
   * @testName: testSetMaxRows01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:147; JDBC:JAVADOC:148;
   *
   * @test_Strategy: Get a Statement object and call the setMaxRows(int rows)
   * method and call getMaxRows() method and it should return a integer value
   * that is been set
   *
   */
  public void testSetMaxRows01() throws Fault {
    int maxRowsVal = 0;
    String sMaxRowsVal = null;
    int retVal = 0;
    try {
      sMaxRowsVal = sqlp.getProperty("Max_Set_Val", "");
      msg.setMsg("sMaxRowsVal = " + sMaxRowsVal);

      maxRowsVal = Integer.parseInt(sMaxRowsVal);
      msg.setMsg("Maximum Rows Value to be set " + maxRowsVal);

      msg.setMsg("Calling setMaxRows method");
      stmt.setMaxRows(maxRowsVal);

      msg.setMsg("invoke getMaxRows");
      retVal = stmt.getMaxRows();

      String str1 = "maxRowsVal = " + maxRowsVal + ".   retVal = " + retVal
          + ".";
      msg.setMsg(str1);

      if (maxRowsVal == retVal) {
        msg.setMsg("setMaxRows method sets the value for Maximum Rows");
      } else {
        msg.printTestError(
            "setMaxRows method does not set the value for Maximum Rows",
            "Call to setMaxRows is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setMaxRows is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setMaxRows is Failed!");

    }
  }

  /*
   * @testName: testSetMaxRows02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:147; JDBC:JAVADOC:148;
   *
   * @test_Strategy: Get a Statement object and call the setMaxRows(int rows)
   * method with an invalid value (negative value) and It should throw an
   * SQLException
   *
   */
  public void testSetMaxRows02() throws Fault {
    int maxRowsVal = 0;
    String sMaxRowsVal = null;
    boolean sqlExceptFlag = false;

    try {
      sMaxRowsVal = sqlp.getProperty("Max_Set_Val", "");
      maxRowsVal = Integer.parseInt(sMaxRowsVal);
      maxRowsVal = maxRowsVal * (-1);
      msg.setMsg("Rows Value to be set " + maxRowsVal);

      msg.setMsg("Calling setMaxRows method");
      try {
        stmt.setMaxRows(maxRowsVal);
      } catch (SQLException sqe) {
        TestUtil.printStackTrace(sqe);

        sqlExceptFlag = true;
      }

      if (sqlExceptFlag) {
        msg.setMsg("setMaxRows method does not set the Invalid value");
      } else {
        msg.printTestError("setMaxRows method sets the Invalid value",
            "Call to setMaxRows is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to setMaxRows is Failed!");

    }
  }

  /*
   * @testName: testSetQueryTimeout02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:153; JDBC:JAVADOC:154;
   *
   * @test_Strategy: Get a Statement object and call the setQueryTimeout(int
   * secval) method with an invalid value (negative value)and It should throw an
   * SQLException
   *
   */

  public void testSetQueryTimeout02() throws Fault {
    int maxQueryTimeVal = 0;
    String sMaxQueryTimeVal = null;
    boolean sqlExceptFlag = false;

    try {
      sMaxQueryTimeVal = sqlp.getProperty("Max_Set_Val", "");
      maxQueryTimeVal = Integer.parseInt(sMaxQueryTimeVal);
      maxQueryTimeVal = maxQueryTimeVal * (-1);
      msg.setMsg("Seconds Value to be set as QueryTimeout " + maxQueryTimeVal);

      msg.setMsg("Calling maxQueryTimeout method");
      try {
        stmt.setQueryTimeout(maxQueryTimeVal);
      } catch (SQLException sqe) {
        TestUtil.printStackTrace(sqe);

        sqlExceptFlag = true;
      }

      if (sqlExceptFlag) {
        msg.setMsg("setQueryTimeout method does not set the Invalid value");
      } else {
        msg.printTestError("setQueryTimeout method sets the Invalid value",
            "Call to setQueryTimeout is Failed");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to setQueryTimeout is Failed!");

    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      // Close the Statement object
      stmt.close();
      conn.close();
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }

}
