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
 * @(#)resultSetClient18.java	1.24 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet18;

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
 * The resultSetClient18 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 99/10/12
 */

public class resultSetClient18 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.resultSet.resultSet18";

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

  private ResultSet oRes = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    resultSetClient18 theTests = new resultSetClient18();
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
   * @testName: testGetInt04
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
   * JDBC:JAVADOC:381; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object by executing the query that gets the
   * maximum value of table Smallint_Tab.Call the getInt(int columnIndex)
   * method. Compare the returned result with the value extracted from
   * tssql.stmt file.Both of them should be equal and the returned result must
   * be equal to the Maximum Value of JDBC Smallint datatype.
   */
  public void testGetInt04() throws Fault {
    try {
      // create the table
      rsSch.createTab("Smallint_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Smallint_Query_Max", "");

      oRes = stmt.executeQuery(sQuery);
      oRes.next();

      msg.setMsg("Calling ResultSet.getInt(MaximumValue)");
      msg.setMsg("get the Maximum value from the table using getInt Method");
      int oRetVal = oRes.getInt(1);
      msg.setMsg("get the Maximum value from the Insert String ");
      String sExtVal = rsSch.extractVal("Smallint_Tab", 1, sqlp, conn);
      int oExtVal = Integer.parseInt(sExtVal);

      msg.addOutputMsg("" + oExtVal, "" + oRetVal);
      if (oRetVal == oExtVal)
        msg.setMsg("getInt returns the Maximum Value " + oRetVal);
      else {
        msg.printTestError("getInt did not return the Maximum Value",
            "test getInt Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getInt is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getInt is Failed!");

    } finally {
      try {
        oRes.close();
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetInt05
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
   * JDBC:JAVADOC:381; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object by executing the query that gets the
   * minimum value of table Smallint_Tab.Call the getInt(int columnIndex)
   * method. Compare the returned result with the value extracted from
   * tssql.stmt file.Both of them should be equal and the returned result must
   * be equal to the Minimum Value of JDBC Smallint datatype.
   */
  public void testGetInt05() throws Fault {
    try {
      // create the table
      rsSch.createTab("Smallint_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Smallint_Query_Min", "");

      oRes = stmt.executeQuery(sQuery);
      oRes.next();

      msg.setMsg("Calling ResultSet.getInt(MinimumValue)");
      msg.setMsg("get the Minimum value from the table using getInt Method");
      int oRetVal = oRes.getInt(1);
      msg.setMsg("get the Minimum value from the Insert String ");
      String sExtVal = rsSch.extractVal("Smallint_Tab", 2, sqlp, conn);
      int oExtVal = Integer.parseInt(sExtVal);

      msg.addOutputMsg("" + oExtVal, "" + oRetVal);
      if (oRetVal == oExtVal)
        msg.setMsg("getInt returns the Minimum Value " + oRetVal);
      else {
        msg.printTestError("getInt did not return the Minimum Value",
            "test getInt Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getInt is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getInt is Failed!");

    } finally {
      try {
        oRes.close();
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetInt06
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
   * JDBC:JAVADOC:381; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Smallint_Tab.Call the getInt(int columnIndex)
   * method.Check if the value returned is zero.
   */
  public void testGetInt06() throws Fault {
    try {
      // create the table
      rsSch.createTab("Smallint_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Smallint_Query_Null", "");

      oRes = stmt.executeQuery(sQuery);
      oRes.next();

      msg.setMsg("Calling ResultSet.getInt(NullValue)");
      msg.setMsg("get the Null value from the table using getInt Method");
      int oRetVal = oRes.getInt(1);

      if (oRetVal == 0) {
        msg.setMsg(
            "Calling getInt method on a SQL Null column returns" + oRetVal);
      } else {
        msg.printTestError("getInt did not return the value zero",
            "test getInt Failed");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getInt is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getInt is Failed!");

    } finally {
      try {
        oRes.close();
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetInt07
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
   * JDBC:JAVADOC:381; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that gets the
   * maximum value of table Integer_Tab.Call the getInt(int columnIndex) method.
   * Compare the returned result with the value extracted from tssql.stmt
   * file.Both of them should be equal and the returned result must be equal to
   * the Maximum Value of JDBC Integer datatype.
   */
  public void testGetInt07() throws Fault {
    try {
      // create the table
      rsSch.createTab("Integer_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Integer_Query_Max", "");

      oRes = stmt.executeQuery(sQuery);
      oRes.next();

      msg.setMsg("Calling ResultSet.getInt(MaximumValue)");
      msg.setMsg("get the Maximum value from the table using getInt Method");
      int oRetVal = oRes.getInt(1);
      msg.setMsg("get the Maximum value from the Insert String");
      String sExtVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);
      int oExtVal = Integer.parseInt(sExtVal);

      msg.addOutputMsg("" + oExtVal, "" + oRetVal);
      if (oRetVal == oExtVal)
        msg.setMsg("getInt returns the Maximum Value " + oRetVal);
      else {
        msg.printTestError("getInt did not return the Maximum Value",
            "test getInt Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getInt is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getInt is Failed!");

    } finally {
      try {
        oRes.close();
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetInt08
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
   * JDBC:JAVADOC:381; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that gets the
   * minimum value of table Integer_Tab.Call the getInt(int columnIndex) method.
   * Compare the returned result with the value extracted from tssql.stmt
   * file.Both of them should be equal and the returned result must be equal to
   * the Minimum Value of JDBC Integer datatype.
   */
  public void testGetInt08() throws Fault {
    try {
      // create the table
      rsSch.createTab("Integer_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Integer_Query_Min", "");

      oRes = stmt.executeQuery(sQuery);
      oRes.next();

      msg.setMsg("Calling ResultSet.getInt(MinimumValue)");
      msg.setMsg("get the Minimum value from the table using getInt Method");
      int oRetVal = oRes.getInt(1);
      msg.setMsg("get the Minimum value from the Insert String ");
      String sExtVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);
      int oExtVal = Integer.parseInt(sExtVal);

      msg.addOutputMsg("" + oExtVal, "" + oRetVal);
      if (oRetVal == oExtVal)
        msg.setMsg("getInt returns the Minimum Value " + oRetVal);
      else {
        msg.printTestError("getInt did not return the Minimum Value",
            "test getInt Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getInt is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getInt is Failed!");

    } finally {
      try {
        oRes.close();
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetInt09
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
   * JDBC:JAVADOC:381; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Integer_Tab.Call the getInt(int columnIndex)
   * method.Check if the value returned is zero.
   */
  public void testGetInt09() throws Fault {
    try {
      // create the table
      rsSch.createTab("Integer_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Integer_Query_Null", "");

      oRes = stmt.executeQuery(sQuery);
      oRes.next();

      msg.setMsg("Calling ResultSet.getInt(NullValue)");

      msg.setMsg("get the Null value from the table using getInt Method");
      int oRetVal = oRes.getInt(1);

      if (oRetVal == 0) {
        msg.setMsg(
            "Calling getInt method on a SQL Null column returns" + oRetVal);
      } else {
        msg.printTestError("getInt did not return the value zero",
            "test getInt Failed");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getInt is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getInt is Failed!");

    } finally {
      try {
        oRes.close();
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetInt10
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
   * JDBC:JAVADOC:381; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the maximum value of table Real_Tab with an
   * integer value. Retrieve the value updated in the table by executing a query
   * in the Real_Tab. Compare the value inserted and the value retrieved. Both
   * of them should be equal.
   */
  public void testGetInt10() throws Fault {
    try {
      // create the table
      rsSch.createTab("Real_Tab", sqlp, conn);
      // update the Max value of Real table with the Integer value after
      // converting it to float
      String sUpdStmt = sqlp.getProperty("Real_Tab_Max_Update", "");
      pstmt = conn.prepareStatement(sUpdStmt);

      int intVal = 1000;
      float oSuppVal = new Integer(intVal).floatValue();
      pstmt.setFloat(1, oSuppVal);
      pstmt.executeUpdate();

      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Real_Query_Max", "");
      oRes = stmt.executeQuery(sQuery);
      oRes.next();
      msg.setMsg("Calling ResultSet.getInt");
      msg.setMsg("get the value from the table using getInt Method");
      int oRetVal = oRes.getInt(1);

      msg.addOutputMsg("" + intVal, "" + oRetVal);

      if ((float) oRetVal == (float) intVal)

        msg.setMsg("getInt returns the desired Value " + oRetVal);
      else {
        msg.printTestError("getInt did not return the desired Value",
            "test getInt Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getInt is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getInt is Failed!");

    } finally {
      try {
        // close the prepared statement
        pstmt.close();
        // drop the table
        oRes.close();
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetInt12
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
   * JDBC:JAVADOC:381; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Real_Tab.Call the getInt(int columnIndex)
   * method.Check if the value returned is zero.
   */
  public void testGetInt12() throws Fault {
    try {
      // create the table
      rsSch.createTab("Real_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Real_Query_Null", "");

      oRes = stmt.executeQuery(sQuery);
      oRes.next();

      msg.setMsg("Calling ResultSet.getInt(NullValue)");
      msg.setMsg("get the Null value from the table using getInt Method");
      int oRetVal = oRes.getInt(1);

      if (oRetVal == 0) {
        msg.setMsg(
            "Calling getInt method on a SQL Null column returns" + oRetVal);
      } else {
        msg.printTestError("getInt did not return the value zero",
            "test getInt Failed");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getInt is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getInt is Failed!");

    } finally {
      try {
        oRes.close();
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetInt16
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
   * JDBC:JAVADOC:381; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the maximum value of table Float_Tab with the
   * maximum value of table Integer_Tab.Now execute a query to get the maximum
   * value of Float_Tab table and retrieve the result of the query using the
   * getInt(int columnIndex) method.Compare the returned value, with the maximum
   * value of table Integer_Tab extracted from the tssql.stmt file. Both of them
   * should be equal.
   */
  public void testGetInt16() throws Fault {
    try {
      // create the table
      rsSch.createTab("Float_Tab", sqlp, conn);
      // update the Max value of Float table with the Max value of Integer
      String sUpdStmt = sqlp.getProperty("Float_Tab_Max_Update", "");
      pstmt = conn.prepareStatement(sUpdStmt);

      String sSuppVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);
      double oSuppVal = Double.parseDouble(sSuppVal);
      pstmt.setDouble(1, oSuppVal);
      pstmt.executeUpdate();

      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Float_Query_Max", "");
      oRes = stmt.executeQuery(sQuery);
      oRes.next();
      msg.setMsg("Calling ResultSet.getInt(MaximumValue)");
      msg.setMsg("get the Maximum value from the table using getInt Method");
      int oRetVal = oRes.getInt(1);
      msg.setMsg("get the Maximum value from the Insert String");
      String sExtVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);
      int oExtVal = Integer.parseInt(sExtVal);

      msg.addOutputMsg("" + oExtVal, "" + oRetVal);
      if (oRetVal == oExtVal)
        msg.setMsg("getInt returns the Maximum Value " + oRetVal);
      else {
        msg.printTestError("getInt did not return the Maximum Value",
            "test getInt Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getInt is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getInt is Failed!");

    } finally {
      try {
        // close the prepared statement
        pstmt.close();
        // drop the table
        oRes.close();
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetInt17
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
   * JDBC:JAVADOC:381; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using this,update the minimum value of table Float_Tab with the
   * minimum value of table Integer_Tab.Now execute a query to get the minimum
   * value of Float_Tab table and retrieve the result of the query using the
   * getInt(int columnIndex) method.Compare the returned value, with the minimum
   * value of table Integer_Tab extracted from the tssql.stmt file. Both of them
   * should be equal.
   */
  public void testGetInt17() throws Fault {
    try {
      // create the table
      rsSch.createTab("Float_Tab", sqlp, conn);
      // update the Min value of Float table with the Min value of Integer
      String sUpdStmt = sqlp.getProperty("Float_Tab_Min_Update", "");
      pstmt = conn.prepareStatement(sUpdStmt);

      String sSuppVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);
      double oSuppVal = Double.parseDouble(sSuppVal);
      pstmt.setDouble(1, oSuppVal);
      pstmt.executeUpdate();

      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Float_Query_Min", "");
      oRes = stmt.executeQuery(sQuery);
      oRes.next();
      msg.setMsg("Calling ResultSet.getInt(MinimumValue)");
      msg.setMsg("get the Minimum value from the table using getInt Method");
      int oRetVal = oRes.getInt(1);
      msg.setMsg("get the Minimum value from the Insert String");
      String sExtVal = rsSch.extractVal("Integer_Tab", 2, sqlp, conn);
      int oExtVal = Integer.parseInt(sExtVal);

      msg.addOutputMsg("" + oExtVal, "" + oRetVal);
      if (oRetVal == oExtVal)
        msg.setMsg("getInt returns the Minimum Value " + oRetVal);
      else {
        msg.printTestError("getInt did not return the Minimum Value",
            "test getInt Failed");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getInt is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getInt is Failed!");

    } finally {
      try {
        // close the prepared statement
        pstmt.close();
        // drop the table
        oRes.close();
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {
      }
      ;
    }

  }

  /*
   * @testName: testGetInt18
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
   * JDBC:JAVADOC:381; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Float_Tab.Call the getInt(int columnIndex)
   * method.Check if the value returned is zero.
   */
  public void testGetInt18() throws Fault {
    try {
      // create the table
      rsSch.createTab("Float_Tab", sqlp, conn);
      msg.setMsg("Execute the query and get the resultSet Object");
      String sQuery = sqlp.getProperty("Float_Query_Null", "");

      oRes = stmt.executeQuery(sQuery);
      oRes.next();

      msg.setMsg("Calling ResultSet.getInt(NullValue)");
      msg.setMsg("get the Null value from the table using getInt Method");
      int oRetVal = oRes.getInt(1);

      if (oRetVal == 0) {
        msg.setMsg(
            "Calling getInt method on a SQL Null column returns" + oRetVal);
      } else {
        msg.printTestError("getInt did not return the value zero",
            "test getInt Failed");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getInt is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getInt is Failed!");

    } finally {
      try {
        oRes.close();
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
