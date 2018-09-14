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
 * @(#)resultSetClient1.java	1.32 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet1;

import java.io.*;
import java.util.*;

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
 * The resultSetClient1 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class resultSetClient1 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.resultSet.resultSet1";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection coffeeCon = null;

  private Statement stmt = null;

  private DataSource ds1 = null;

  private ResultSet rs = null;

  private String drManager = null;

  private String sqlStmt = null;

  private dbSchema dbSch = null;

  private int rscount = 0;

  private ResultSet rs1 = null;

  private String query1 = null;

  private int rowMaxVal = 0;

  private String query = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    resultSetClient1 theTests = new resultSetClient1();
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

        String fTableName = p.getProperty("ftable", "");
        msg = new JDBCTestMsg();

        /* String query = "SELECT COF_NAME, PRICE FROM " + fTableName; */
        query = p.getProperty("SelCoffeeAll", "");
        if (query.length() == 0)
          throw new Fault("Invalid SQL Statement ");
        msg.setMsg("Query to select All the rows :" + query);

        query1 = p.getProperty("SelCoffeeNull", "");

        /* query1="SELECT * FROM " + fTableName + " WHERE TYPE_ID=0"; */
        if (query1.length() == 0)
          throw new Fault("Invalid SQL Statement ");
        msg.setMsg("Query to select Null rows :" + query1);

        if (drManager.equals("yes")) {
          logTrace("Using DriverManager");
          DriverManagerConnection dmCon = new DriverManagerConnection();
          coffeeCon = dmCon.getConnection(p);
        } else {
          logTrace("Using DataSource");
          DataSourceConnection dsCon = new DataSourceConnection();
          coffeeCon = dsCon.getConnection(p);
        }
        dbSch = new dbSchema();
        dbSch.createData(p, coffeeCon);
        stmt = coffeeCon.createStatement(/*
                                          * ResultSet.TYPE_SCROLL_INSENSITIVE,
                                          * ResultSet.CONCUR_READ_ONLY
                                          */);
        rs = stmt.executeQuery(query.trim());

      } catch (SQLException ex) {
        msg.printSQLError(ex, "");
      }
    } catch (Exception e) {
      logErr("Setup Failed!", e);
    }
  }

  /*
   * This method is to calculate the number of rows in a ResultSet. Call the
   * rsCount(ResultSet rs) method. It Should return an Integer value
   *
   */
  public int rsCount(ResultSet rs) throws Fault {
    int count = 0;
    try {
      while (rs.next()) {
        count++;
      }
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to rsCount is Failed!");

    }
    return count;
  }

  /*
   * @testName: testGetConcurrency
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:492;
   * JDBC:JAVADOC:493; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query and call the
   * getConcurrency() method on that resultset object. It should return an
   * Integer value and the value should be equal to any of the values
   * CONCUR_READ_ONLY or CONCUR_UPDATABLE which are defined in the Resutset
   * interface.
   */

  public void testGetConcurrency() throws Fault {
    try {
      // invoke on the getConcurrency
      msg.setMsg("Calling getConcurrency on ResultSet");
      int concurrencyValue = rs.getConcurrency();
      if (concurrencyValue == ResultSet.CONCUR_READ_ONLY) {
        msg.setMsg("getConcurrency method returns ResultSet.CONCUR_READ_ONLY ");
      } else if (concurrencyValue == ResultSet.CONCUR_UPDATABLE) {
        msg.setMsg("getConcurrency method returns ResultSet.CONCUR_UPDATABLE");
      } else {
        msg.printTestError(" getConcurrency method returns a invalid value",
            "Call to getConcurrency is Failed");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getConcurrency is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getConcurrency is Failed!");

    }
  }

  /*
   * @testName: testGetFetchDirection
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:484;
   * JDBC:JAVADOC:485; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing a query and call the
   * getFetchDirection() method.It should return an Integer value and the value
   * should be equal to any of the values FETCH_FORWARD or FETCH_REVERSE or
   * FETCH_UNKNOWN which are defined in the Resultset interface.
   *
   */

  public void testGetFetchDirection() throws Fault {
    try {
      // invoke on the getFetchDirection
      msg.setMsg("Calling getFetchDirection on ResultSet");
      int fetchDirectionValue = rs.getFetchDirection();
      if (fetchDirectionValue == ResultSet.FETCH_FORWARD) {
        msg.setMsg("getFetchDirection method returns ResultSet.FETCH_FORWARD ");
      } else if (fetchDirectionValue == ResultSet.FETCH_REVERSE) {
        msg.setMsg("getFetchDirection method returns ResultSet.FETCH_REVERSE");
      } else if (fetchDirectionValue == ResultSet.FETCH_UNKNOWN) {
        msg.setMsg("getFetchDirection method returns ResultSet.FETCH_UNKNOWN");
      } else {
        msg.printTestError(" getFetchDirection method returns a invalid value",
            "Call to getFetchDirection is Failed");

      }
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getFetchDirection is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getFetchDirection is Failed!");

    }
  }

  /*
   * @testName: testGetFetchSize
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:488;
   * JDBC:JAVADOC:489; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object and call the getFetchSize() method
   * It should return a Integer value which should be greater than or equal to
   * zero.
   *
   */
  public void testGetFetchSize() throws Fault {
    try {
      // invoke on the getFetchSize
      msg.setMsg("Calling getFetchSize on ResultSet");
      int fetchSizeValue = rs.getFetchSize();
      if (fetchSizeValue >= 0) {
        msg.setMsg("getFetchSize method returns :" + fetchSizeValue);
      } else {
        msg.setMsg(" getFetchSize method returns a invalid value");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getFetchSize is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getFetchSize is Failed!");

    }
  }

  /*
   * @testName: testGetType
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:490;
   * JDBC:JAVADOC:491; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object and call the getType() method on
   * that object. It should return an Integer value and the value should be
   * equal to any of the values TYPE_FORWARD_ONLY or TYPE_SCROLL_INSENSITIVE or
   * TYPE_SCROLL_SENSITIVE which are defined in the Resultset interface.
   *
   */
  public void testGetType() throws Fault {
    try {
      // invoke on the getType
      msg.setMsg("Calling getType on ResultSet");
      int typeValue = rs.getType();
      if (typeValue == ResultSet.TYPE_FORWARD_ONLY) {
        msg.setMsg("getType method returns ResultSet.TYPE_FORWARD_ONLY ");
      } else if (typeValue == ResultSet.TYPE_SCROLL_INSENSITIVE) {
        msg.setMsg("getType method returns ResultSet.TYPE_SCROLL_INSENSITIVE");
      } else if (typeValue == ResultSet.TYPE_SCROLL_SENSITIVE) {
        msg.setMsg("getType method returns ResultSet.TYPE_SCROLL_SENSITIVE");
      } else {
        msg.printTestError(" getType method returns a invalid value",
            "Call to getType is Failed");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getType is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getType is Failed!");

    }
  }

  /*
   * @testName: testSetFetchSize01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:486;
   * JDBC:JAVADOC:487; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing a query and call the
   * setFetchSize(int rows). Set the value of rows to zero. The JDBC driver is
   * free to make its own best guess as to what the fetch size should be. Then
   * call getFetchSize() method.
   * 
   *
   */
  public void testSetFetchSize01() throws Fault {
    try {
      // invoke on the setFetchSize
      msg.setMsg("Calling setFetchSize on ResultSet");
      rs.setFetchSize(0);
      int fetchSizeValue = rs.getFetchSize();
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setFetchSize is Failed!");

    } catch (Exception e) {
      msg.printError(e, "setFetchSize Failed!");

    }
  }

  /*
   * @testName: testSetFetchSize02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:486;
   * JDBC:JAVADOC:487; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object and call the setFetchSize(int rows)
   * on that obect. Call Statement's getMaxRows() before calling setFetchSize()
   * method and pass the returned value from getMaxRows() method as the argument
   * to the setFetchSize() method. Then call getFetchSize() method to check
   * whether the returned value is the same that has been set.
   *
   */
  public void testSetFetchSize02() throws Fault {
    try {
      // Setting the maxRows to 5 to make sure that we have a non-zero value
      // for maxrows

      Statement stmt = coffeeCon.createStatement();
      stmt.setMaxRows(5);

      ResultSet rs = stmt.executeQuery(query.trim());

      // invoke on the setFetchSize
      // rowMaxVal is the maximum Rows that ResultSet can contain
      rowMaxVal = stmt.getMaxRows();

      msg.setMsg("Calling setFetchSize() on ResultSet");
      rs.setFetchSize(rowMaxVal);
      int fetchSizeValue = rs.getFetchSize();
      msg.addOutputMsg("" + rowMaxVal, "" + fetchSizeValue);

      if (fetchSizeValue == rowMaxVal) {
        msg.setMsg(
            "setFetchSize method sets number of rows that has been specified :"
                + fetchSizeValue);
      } else {
        msg.printTestError(
            "setFetchSize method does not set number of rows that has been specified",
            "Call to setFetchSize is Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setFetchSize is Failed!");

    } catch (Exception e) {
      msg.printError(e, "setFetchSize Failed!");

    }
  }

  /*
   * @testName: testSetFetchSize03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:486;
   * JDBC:JAVADOC:487; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object and call the setFetchSize(int rows)
   * on that object with the value greater than the Statement's maximum possible
   * rows as returned by getMaxRows() method and it should throw SQLException.
   * In case if the getMaxRows() method returns 0 which means unlimited rows
   * then appropriate message is displayed and test passes
   */
  public void testSetFetchSize03() throws Fault {
    boolean flag = false;
    try {
      // invoke on the setFetchSize
      // rowMaxVal is the maximum Rows that ResultSet can contain
      rowMaxVal = stmt.getMaxRows();
      msg.setMsg("Calling setFetchSize on ResultSet");
      try {
        if (rowMaxVal > 0) {
          rs.setFetchSize(rowMaxVal + 2);
        } else if (rowMaxVal == 0) {
          msg.setMsg("Resultset supports unlimited number of rows");
          flag = true;
        }
        msg.printTestMsg();
        msg.printOutputMsg();
      } catch (SQLException sqe) {
        TestUtil.printStackTrace(sqe);

        flag = true;
      }
      if (flag) {
        msg.setMsg(
            "setFetchSize method does not set the size greater than the Maximum Rows that ResultSet can contain");
      } else {
        msg.printTestError(
            "setFetchSize method sets the size greater than the Maximum Rows that ResultSet can contain ",
            "Call to setFetchSize is Failed!");

      }

    } catch (Exception e) {
      msg.printError(e, "setFetchSize Failed!");

    }
  }

  /*
   * @testName: testSetFetchSize04
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:486;
   * JDBC:JAVADOC:487; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object and call the setFetchSize(int rows)
   * on that object. And try to set a negative value .It should throw
   * SQLException.
   */

  public void testSetFetchSize04() throws Fault {
    boolean flag = false;
    try {
      // invoke on the setFetchSize
      msg.setMsg("Calling setFetchSize(-1) on ResultSet");
      try {

        rs.setFetchSize(-1);
      } catch (SQLException sqe) {
        TestUtil.printStackTrace(sqe);

        flag = true;
      }
      if (flag) {
        msg.setMsg("setFetchSize method does not set the size as negative ");
      } else {
        msg.printTestError("setFetchSize method sets the size as negative ",
            "Call to setFetchSize is Failed!");

      }
    } catch (Exception e) {
      msg.printError(e, "setFetchSize Failed!");

    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      stmt.close();
      // Close the database
      dbSch.destroyData(coffeeCon);
      dbSch.dbUnConnect(coffeeCon);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }
}
