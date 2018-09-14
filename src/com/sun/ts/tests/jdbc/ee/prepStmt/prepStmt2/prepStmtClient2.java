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
 * @(#)prepStmtClient2.java	1.16 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt2;

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
 * The prepStmtClient2 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 11/24/00
 */

public class prepStmtClient2 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.prepStmt.prepStmt2";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements
  private transient Connection conn = null;

  private ResultSet rs = null;

  private Statement stmt = null;

  private DataSource ds1 = null;

  private dbSchema dbSch = null;

  private rsSchema rsSch = null;

  private csSchema csSch = null;

  private String drManager = null;

  private transient DatabaseMetaData dbmd = null;

  private Properties sqlp = null;

  private Properties props = null;

  private PreparedStatement pstmt = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    prepStmtClient2 theTests = new prepStmtClient2();
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
   * ptable; binarySize, size of binary data type; varbinarySize, size of
   * varbinary data type; longvarbinarySize, size of longvarbinary data type;
   * 
   * @class.testArgs: -ap tssql.stmt
   */

  public void setup(String[] args, Properties p) throws Fault {
    try {
      try {
        drManager = p.getProperty("DriverManager", "");
        if (drManager.length() == 0)
          throw new Fault("Invalid DriverManager Name");
        // sqlp = new Properties();
        props = p;
        /*
         * String sqlStmt = p.getProperty("rsQuery",""); InputStream istr = new
         * FileInputStream(sqlStmt); sqlp.load(istr);
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
        rsSch = new rsSchema();
        csSch = new csSchema();
        msg = new JDBCTestMsg();
      } catch (SQLException ex) {
        logErr("SQL Exception: : " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testSetInt02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:664; JDBC:JAVADOC:665;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database Using setInt(int parameterIndex,int x),update the column with the
   * maximum value of Integer_Tab. Now execute a query to get the maximum value
   * and retrieve the result of the query using the getInt(int columnIndex)
   * method Compare the returned value, with the maximum value extracted from
   * the tssql.stmt file. Both of them should be equal
   */

  public void testSetInt02() throws Fault {
    int maxIntegerVal = 0;
    int rIntegerVal = 0;

    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Integer_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Value of Maximum value to be Updated");
      String smaxIntegerVal = rsSch.extractVal("Integer_Tab", 1, sqlp, conn);

      // to convert the String into integer value
      maxIntegerVal = Integer.parseInt(smaxIntegerVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setInt(1, maxIntegerVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Integer_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rIntegerVal = rs.getInt(1);
      msg.addOutputMsg("" + maxIntegerVal, "" + rIntegerVal);

      if (rIntegerVal == maxIntegerVal) {
        msg.setMsg(
            "setInt Method sets the designated parameter to a int value ");
      } else {
        msg.printTestError(
            "setInteger Method does not set the designated parameter to a int value ",
            "Call to setInt is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setInt is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setInt Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetDate01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:678; JDBC:JAVADOC:679;
   * JDBC:JAVADOC:392; JDBC:JAVADOC:393; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database execute the precompiled SQL Statement by calling setDate(int
   * parameterIndex,Date x) method and call the ResultSet.getDate(int) method to
   * check and it should return a String Value that it is been set
   */

  public void testSetDate01() throws Fault {
    java.sql.Date mfgDateVal = null;
    java.sql.Date rDateVal = null;
    String smfgDateVal = null;

    try {
      rsSch.createTab("Date_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Date_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Value of Maximum value to be Updated");
      smfgDateVal = rsSch.extractVal("Date_Tab", 1, sqlp, conn);
      smfgDateVal = smfgDateVal.substring(smfgDateVal.indexOf('\'') + 1,
          smfgDateVal.lastIndexOf('\''));

      // to convert the String into Date Val
      mfgDateVal = mfgDateVal.valueOf(smfgDateVal);
      msg.setMsg(" get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setDate(1, mfgDateVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Date_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rDateVal = rs.getDate(1);

      msg.addOutputMsg("" + mfgDateVal, "" + rDateVal);

      if (rDateVal.equals(mfgDateVal)) {
        msg.setMsg(
            "setDate Method sets the designated parameter to a Date value ");
      } else {
        msg.printTestError(
            "setDate Method does not set the designated parameter to a Date value ",
            "Call to setDate is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setDate is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setDate is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Date_Tab", conn);
      } catch (Exception e) {
      }
    }

  }

  /*
   * @testName: testSetDate02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:714; JDBC:JAVADOC:715;
   * JDBC:JAVADOC:612; JDBC:JAVADOC:613; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database execute the precompiled SQL Statement by calling setDate(int
   * parameterIndex,Date x,Calendar cal) method and call the
   * ResultSet.getDate(int) method to check and it should return a String Value
   * that it is been set
   */

  public void testSetDate02() throws Fault {
    java.sql.Date mfgDateVal = null;
    java.sql.Date rDateVal = null;
    String smfgDateVal = null;
    Calendar cal = null;

    try {
      rsSch.createTab("Date_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Date_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Value of Maximum value to be Updated");
      smfgDateVal = rsSch.extractVal("Date_Tab", 1, sqlp, conn);
      smfgDateVal = smfgDateVal.substring(smfgDateVal.indexOf('\'') + 1,
          smfgDateVal.lastIndexOf('\''));

      // to convert the String into Date Val
      mfgDateVal = mfgDateVal.valueOf(smfgDateVal);

      cal = Calendar.getInstance();

      msg.setMsg(" get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setDate(1, mfgDateVal, cal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Date_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rDateVal = rs.getDate(1, cal);

      msg.addOutputMsg("" + mfgDateVal, "" + rDateVal);
      if (rDateVal.equals(mfgDateVal)) {
        msg.setMsg(
            "setDate Method sets the designated parameter to a Date value ");
      } else {
        msg.printTestError(
            "setDate Method does not set the designated parameter to a Date value ",
            "Call to setDate is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setDate is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setDate is Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Date_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetDouble01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:670; JDBC:JAVADOC:671;
   * JDBC:JAVADOC:386; JDBC:JAVADOC:387; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database Using setDouble(int parameterIndex,double x),update the column the
   * minimum value of Double_Tab. Now execute a query to get the minimum value
   * and retrieve the result of the query using the getDouble(int columnIndex)
   * method.Compare the returned value, with the minimum value extracted from
   * the tssql.stmt file. Both of them should be equal.
   */

  public void testSetDouble01() throws Fault {
    double rDoubleVal = 0;
    double minDoubleVal = 0;

    try {
      rsSch.createTab("Double_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Double_Tab_Max_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Value of Minimum value to be Updated");
      String sminDoubleVal = rsSch.extractVal("Double_Tab", 2, sqlp, conn);

      // to convert the String into Double value
      minDoubleVal = Double.parseDouble(sminDoubleVal);

      msg.setMsg(" get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setDouble(1, minDoubleVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Double_Query_Max", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      rDoubleVal = rs.getDouble(1);

      msg.addOutputMsg("" + minDoubleVal, "" + rDoubleVal);
      if (rDoubleVal == minDoubleVal) {
        msg.setMsg(
            "setDouble Method sets the designated parameter to a Double value ");
      } else {
        msg.printTestError(
            "setDouble Method does not set the designated parameter to a Double value ",
            "Call to setDouble is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setDouble is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setDouble Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Double_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetDouble02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:678; JDBC:JAVADOC:679;
   * JDBC:JAVADOC:386; JDBC:JAVADOC:387; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using setDouble(int parameterIndex,double x),update the column
   * the maximum value of Double_Tab. Now execute a query to get the maximum
   * value and retrieve the result of the query using the getDouble(int
   * columnIndex) method.Compare the returned value, with the maximum value
   * extracted from the tssql.stmt file. Both of them should be equal.
   */

  public void testSetDouble02() throws Fault {
    double maxDoubleVal = 0;
    double rDoubleVal = 0;

    try {
      rsSch.createTab("Double_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Double_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Value of Maximum value to be Updated");
      String smaxDoubleVal = rsSch.extractVal("Double_Tab", 1, sqlp, conn);

      // to convert the String into Double value
      maxDoubleVal = Double.parseDouble(smaxDoubleVal);

      msg.setMsg(" get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setDouble(1, maxDoubleVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Double_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rDoubleVal = rs.getDouble(1);
      msg.addOutputMsg("" + maxDoubleVal, "" + rDoubleVal);

      if (rDoubleVal == maxDoubleVal) {
        msg.setMsg(
            "setDouble Method sets the designated parameter to a Double value ");
      } else {
        msg.printTestError(
            "setDouble Method does not set the designated parameter to a Double value ",
            "Call to setDouble is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setDouble is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setDouble Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Double_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetLong01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:666; JDBC:JAVADOC:667;
   * JDBC:JAVADOC:382; JDBC:JAVADOC:383; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using setLong(int parameterIndex,long x),update the column the
   * minimum value of Bigint_Tab. Now execute a query to get the minimum value
   * and retrieve the result of the query using the getLong(int columnIndex)
   * method.Compare the returned value, with the minimum value extracted from
   * the tssql.stmt file. Both of them should be equal.
   */

  public void testSetLong01() throws Fault {
    long rLongVal = 0;
    long minLongVal = 0;

    try {
      rsSch.createTab("Bigint_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Bigint_Tab_Max_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);
      msg.setMsg("to extract the Value of Minimum value to be Updated");
      String sminLongVal = rsSch.extractVal("Bigint_Tab", 2, sqlp, conn);

      // to convert the String into long value
      minLongVal = Long.parseLong(sminLongVal);
      msg.setMsg(" get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setLong(1, minLongVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Bigint_Query_Max", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      rLongVal = rs.getLong(1);

      msg.addOutputMsg("" + minLongVal, "" + rLongVal);
      if (rLongVal == minLongVal) {
        msg.setMsg(
            "setLong Method sets the designated parameter to a long value ");
      } else {
        msg.printTestError(
            "setLong Method does not set the designated parameter to a long value ",
            "Call to setLong is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setLong is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setLong Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Bigint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetLong02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:666; JDBC:JAVADOC:667;
   * JDBC:JAVADOC:382; JDBC:JAVADOC:383; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using setLong(int parameterIndex,long x),update the column the
   * maximum value of Bigint_Tab. Now execute a query to get the maximum value
   * and retrieve the result of the query using the getLong(int columnIndex)
   * method.Compare the returned value, with the maximum value extracted from
   * the tssql.stmt file. Both of them should be equal.
   */

  public void testSetLong02() throws Fault {
    long maxLongVal = 0;
    long rLongVal = 0;

    try {
      rsSch.createTab("Bigint_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Bigint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Value of Maximum value to be Updated");
      String smaxLongVal = rsSch.extractVal("Bigint_Tab", 1, sqlp, conn);

      // to convert the String into long value
      maxLongVal = Long.parseLong(smaxLongVal);
      msg.setMsg(" get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setLong(1, maxLongVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Bigint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rLongVal = rs.getLong(1);

      msg.addOutputMsg("" + maxLongVal, "" + rLongVal);
      if (rLongVal == maxLongVal) {
        msg.setMsg(
            "setLong Method sets the designated parameter to a long value ");
      } else {
        msg.printTestError(
            "setLong Method does not set the designated parameter to a long value ",
            "Call to setLong is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    }

    catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setLong is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setLong Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Bigint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetShort01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:662; JDBC:JAVADOC:663;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using setShort(int parameterIndex,short x),update the column the
   * minimum value of Smallint_Tab. Now execute a query to get the minimum value
   * and retrieve the result of the query using the getShort(int columnIndex)
   * method.Compare the returned value, with the minimum value extracted from
   * the tssql.stmt file. Both of them should be equal.
   */

  public void testSetShort01() throws Fault {
    short rShortVal = 0;
    short minShortVal = 0;

    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Smallint_Tab_Max_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);
      msg.setMsg("to extract the Value of Minimum value to be Updated ");
      String sminShortVal = rsSch.extractVal("Smallint_Tab", 2, sqlp, conn);

      // to convert the String into Short value
      minShortVal = Short.parseShort(sminShortVal);

      msg.setMsg(" get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setShort(1, minShortVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Max_Val_Query = sqlp.getProperty("Smallint_Query_Max", "");
      msg.setMsg(Max_Val_Query);
      rs = stmt.executeQuery(Max_Val_Query);
      rs.next();

      rShortVal = rs.getShort(1);

      msg.addOutputMsg("" + minShortVal, "" + rShortVal);
      if (rShortVal == minShortVal) {
        msg.setMsg(
            "setShort Method sets the designated parameter to a Short value ");
      } else {
        msg.printTestError(
            "setShort Method does not set the designated parameter to a Short value ",
            "Call to setShort is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setShort is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setShort Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetShort02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:662; JDBC:JAVADOC:663;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using setShort(int parameterIndex,short x),update the column the
   * maximum value of Smallint_Tab. Now execute a query to get the maximum value
   * and retrieve the result of the query using the getShort(int columnIndex)
   * method.Compare the returned value, with the maximum value extracted from
   * the tssql.stmt file. Both of them should be equal.
   */

  public void testSetShort02() throws Fault {
    short maxShortVal = 0;
    short rShortVal = 0;

    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Smallint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("to extract the Value of Maximum value to be Updated");
      String smaxShortVal = rsSch.extractVal("Smallint_Tab", 1, sqlp, conn);

      // to convert the String into Short value
      maxShortVal = Short.parseShort(smaxShortVal);

      msg.setMsg(" get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setShort(1, maxShortVal);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Smallint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rShortVal = rs.getShort(1);

      msg.addOutputMsg("" + maxShortVal, "" + rShortVal);
      if (rShortVal == maxShortVal) {
        msg.setMsg(
            "setShort Method sets the designated parameter to a Short value ");
      } else {
        msg.printTestError(
            "setShort Method does not set the designated parameter to a Short value ",
            "Call to setShort is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setShort is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setShort Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetNull01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:4; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for INTEGER Type and retrieve the same value by executing a query.
   * Call the ResultSet.wasNull() method to check it. It should return a true
   * value.
   *
   */

  public void testSetNull01() throws Fault {
    boolean nullFlag;
    int rIntegerVal = 0;
    try {
      rsSch.createTab("Integer_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Integer_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(" get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setNull(1, java.sql.Types.INTEGER);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Integer_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rIntegerVal = rs.getInt(1);
      nullFlag = rs.wasNull();

      msg.addOutputMsg("true", "" + nullFlag);

      if (nullFlag) {
        msg.setMsg(
            "setNull Method sets the designated parameter to a SQL Null");
      } else {
        msg.printTestError(
            "setNull Method does not set the designated parameter to a SQL Null ",
            "Call to setNull is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    }

    catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setNull is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setShort Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetNull02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:6; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for FLOAT Type and retrieve the same value by executing a query. Call
   * the ResultSet.wasNull() method to check it. It should return a true value.
   */

  public void testSetNull02() throws Fault {
    boolean nullFlag;
    float rFloatVal = 0;

    try {
      rsSch.createTab("Float_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Float_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      msg.setMsg(" get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setNull(1, java.sql.Types.FLOAT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Float_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rFloatVal = rs.getFloat(1);
      nullFlag = rs.wasNull();

      msg.addOutputMsg("true", "" + nullFlag);
      if (nullFlag) {
        msg.setMsg(
            "setNull Method sets the designated parameter to a SQL Null");
      } else {
        msg.printTestError(
            "setNull Method does not set the designated parameter to a SQL Null ",
            "Call to setNull is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    }

    catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setNull is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setNull Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetNull03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:3; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for SMALLINT Type and retrieve the same value by executing a query.
   * Call the ResultSet.wasNull() method to check it. It should return a true
   * value.
   */

  public void testSetNull03() throws Fault {
    boolean nullFlag;
    short rShortVal = 0;

    try {
      rsSch.createTab("Smallint_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Smallint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      msg.setMsg(" get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setNull(1, java.sql.Types.SMALLINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Smallint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rShortVal = rs.getShort(1);
      nullFlag = rs.wasNull();

      msg.addOutputMsg("true", "" + nullFlag);
      if (nullFlag) {
        msg.setMsg(
            "setNull Method sets the designated parameter to a SQL Null");
      } else {
        msg.printTestError(
            "setNull Method does not set the designated parameter to a SQL Null ",
            "Call to setNull is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    }

    catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setNull is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setNull Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetNull04
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:10; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for CHAR Type and retrieve the same value by executing a query. Call
   * the ResultSet.wasNull() method to check it. It should return a true value.
   */

  public void testSetNull04() throws Fault {
    boolean nullFlag;
    String rStringVal = null;

    try {
      rsSch.createTab("Char_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Char_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(" get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setNull(1, java.sql.Types.CHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rStringVal = rs.getString(1);
      nullFlag = rs.wasNull();

      msg.addOutputMsg("true", "" + nullFlag);
      if (nullFlag) {
        msg.setMsg(
            "setNull Method sets the designated parameter to a SQL Null");
      } else {
        msg.printTestError(
            "setNull Method does not set the designated parameter to a SQL Null ",
            "Call to setNull is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setNull is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setNull Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Char_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetNull05
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:15; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for TIME Type and retrieve the same value by executing a query. Call
   * the ResultSet.wasNull() method to check it. It should return a true value.
   */

  public void testSetNull05() throws Fault {
    boolean nullFlag;
    Time rTimeVal = null;

    try {
      rsSch.createTab("Time_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Time_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(" get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setNull(1, java.sql.Types.TIME);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Time_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rTimeVal = rs.getTime(1);
      nullFlag = rs.wasNull();

      msg.addOutputMsg("true", "" + nullFlag);
      if (nullFlag) {
        msg.setMsg(
            "setNull Method sets the designated parameter to a SQL Null");
      } else {
        msg.printTestError(
            "setNull Method does not set the designated parameter to a SQL Null ",
            "Call to setNull is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setNull is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setNull Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Time_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetNull06
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:16; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for TIMESTAMP Type and retrieve the same value by executing a query.
   * Call the ResultSet.wasNull() method to check it. It should return a true
   * value.
   */

  public void testSetNull06() throws Fault {
    boolean nullFlag;
    Timestamp rTimestampVal = null;

    try {
      rsSch.createTab("Timestamp_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Timestamp_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(" get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setNull(1, java.sql.Types.TIMESTAMP);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Timestamp_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rTimestampVal = rs.getTimestamp(1);
      nullFlag = rs.wasNull();

      msg.addOutputMsg("true", "" + nullFlag);
      if (nullFlag) {
        msg.setMsg(
            "setNull Method sets the designated parameter to a SQL Null");
      } else {
        msg.printTestError(
            "setNull Method does not set the designated parameter to a SQL Null ",
            "Call to setNull is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    }

    catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setNull is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setNull Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Timestamp_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetNull07
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:14; JDBC:JAVADOC:392; JDBC:JAVADOC:393; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for DATE Type and retrieve the same value by executing a query. Call
   * the ResultSet.wasNull() method to check it. It should return a true value.
   */

  public void testSetNull07() throws Fault {
    boolean nullFlag;
    java.sql.Date rDateVal = null;

    try {
      rsSch.createTab("Date_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Date_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(" get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setNull(1, java.sql.Types.DATE);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Date_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rDateVal = rs.getDate(1);
      nullFlag = rs.wasNull();

      msg.addOutputMsg("true", "" + nullFlag);
      if (nullFlag) {
        msg.setMsg(
            "setNull Method sets the designated parameter to a SQL Null");
      } else {
        msg.printTestError(
            "setNull Method does not set the designated parameter to a SQL Null ",
            "Call to setNull is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    }

    catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setNull is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setNull Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Date_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetNull08
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:9; JDBC:JAVADOC:454; JDBC:JAVADOC:455; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for NUMERIC Type and retrieve the same value by executing a query.
   * Call the ResultSet.wasNull() method to check it. It should return a true
   * value.
   */

  public void testSetNull08() throws Fault {
    boolean nullFlag;
    BigDecimal rBigDecimalVal = null;

    try {
      rsSch.createTab("Numeric_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Numeric_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(" get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setNull(1, java.sql.Types.NUMERIC);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Numeric_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rBigDecimalVal = rs.getBigDecimal(1);
      nullFlag = rs.wasNull();

      msg.addOutputMsg("true", "" + nullFlag);
      if (nullFlag) {
        msg.setMsg(
            "setNull Method sets the designated parameter to a SQL Null");
      } else {
        msg.printTestError(
            "setNull Method does not set the designated parameter to a SQL Null ",
            "Call to setNull is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    }

    catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setNull is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setNull Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Numeric_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetNull09
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:2; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for TINYINT Type and retrieve the same value by executing a query.
   * Call the ResultSet.wasNull() method to check it. It should return a true
   * value.
   */

  public void testSetNull09() throws Fault {
    boolean nullFlag;
    byte rByteVal = 0;

    try {
      rsSch.createTab("Tinyint_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Tinyint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(" get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setNull(1, java.sql.Types.TINYINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Tinyint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rByteVal = rs.getByte(1);
      nullFlag = rs.wasNull();

      msg.addOutputMsg("true", "" + nullFlag);
      if (nullFlag) {
        msg.setMsg(
            "setNull Method sets the designated parameter to a SQL Null");
      } else {
        msg.printTestError(
            "setNull Method does not set the designated parameter to a SQL Null ",
            "Call to setNull is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    }

    catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setNull is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setNull Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetNull10
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:8; JDBC:JAVADOC:386; JDBC:JAVADOC:387; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for DOUBLE Type and retrieve the same value by executing a query. Call
   * the ResultSet.wasNull() method to check it. It should return a true value.
   */

  public void testSetNull10() throws Fault {
    boolean nullFlag;
    double rDoubleVal = 0;

    try {
      rsSch.createTab("Double_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Double_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(" get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setNull(1, java.sql.Types.DOUBLE);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Double_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rDoubleVal = rs.getDouble(1);
      nullFlag = rs.wasNull();

      msg.addOutputMsg("true", "" + nullFlag);
      if (nullFlag) {
        msg.setMsg(
            "setNull Method sets the designated parameter to a SQL Null");
      } else {
        msg.printTestError(
            "setNull Method does not set the designated parameter to a SQL Null ",
            "Call to setNull is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    }

    catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setNull is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setNull Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Double_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetNull11
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:5; JDBC:JAVADOC:382; JDBC:JAVADOC:383; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for BIGINT Type and retrieve the same value by executing a query. Call
   * the ResultSet.wasNull() method to check it. It should return a true value.
   */

  public void testSetNull11() throws Fault {
    boolean nullFlag;
    long rLongVal = 0;

    try {
      rsSch.createTab("Bigint_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Bigint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(" get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setNull(1, java.sql.Types.BIGINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Bigint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();
      rLongVal = rs.getLong(1);
      nullFlag = rs.wasNull();

      msg.addOutputMsg("true", "" + nullFlag);
      if (nullFlag) {
        msg.setMsg(
            "setNull Method sets the designated parameter to a SQL Null");
      } else {
        msg.printTestError(
            "setNull Method does not set the designated parameter to a SQL Null ",
            "Call to setNull is Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    }

    catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setNull is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setNull Failed!");

    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Bigint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      if (pstmt != null) {
        pstmt.close();
        pstmt = null;
      }
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
