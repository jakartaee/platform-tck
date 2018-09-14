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
 * @(#)prepStmtClient15.java	1.18 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt15;

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
 * The prepStmtClient15 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 11/24/00
 */

public class prepStmtClient15 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.prepStmt.prepStmt15";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private ResultSet rs = null;

  private Statement stmt = null;

  private PreparedStatement pstmt = null;

  private transient DatabaseMetaData dbmd = null;

  private DataSource ds1 = null;

  private dbSchema dbSch = null;

  private rsSchema rsSch = null;

  private csSchema csSch = null;

  private String drManager = null;

  private Properties sqlp = null;

  private Properties props = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    prepStmtClient15 theTests = new prepStmtClient15();
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
         * sqlp = new Properties(); String sqlStmt =
         * p.getProperty("rsQuery",""); InputStream istr = new
         * FileInputStream(sqlStmt); sqlp.load(istr);
         */
        sqlp = p;
        props = p;

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
        logErr("SQL Exception: " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testSetObject223
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Longvarchar_Tab with the maximum (mfg date) value of
   * Date_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the maximum (mfg date) value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject223() throws Fault {
    String maxStringVal = null;
    java.sql.Date maxDateVal = null;
    String rStringVal = null;
    java.sql.Date rDateVal = null;

    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Longvarchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extractthe Value of Date to be Updated to Null Column of LongVarchar Table");
      maxStringVal = rsSch.extractVal("Date_Tab", 1, sqlp, conn);
      maxStringVal = maxStringVal.substring(maxStringVal.indexOf('\'') + 1,
          maxStringVal.lastIndexOf('\''));
      maxDateVal = java.sql.Date.valueOf(maxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxDateVal, java.sql.Types.LONGVARCHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getObject(1).toString();
      rStringVal = rStringVal.trim();
      rDateVal = java.sql.Date.valueOf(rStringVal);

      msg.addOutputMsg("" + maxDateVal, "" + rDateVal);
      if (rDateVal.equals(maxDateVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        rsSch.dropTab("Longvarcharnull_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject224
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Date_Tab with the maximum (mfg date) value of
   * Date_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the maximum (mfg date) value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject224() throws Fault {
    java.sql.Date mfgDateVal = null;
    String sMfgStringVal = null;
    java.sql.Date rDateVal = null;

    try {
      rsSch.createTab("Date_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Date_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extractthe Value of Date to be Updated to Null Column of Date Table");
      sMfgStringVal = rsSch.extractVal("Date_Tab", 1, sqlp, conn);
      sMfgStringVal = sMfgStringVal.substring(sMfgStringVal.indexOf('\'') + 1,
          sMfgStringVal.lastIndexOf('\''));

      // to convert String value into java.sql.Date value
      mfgDateVal = java.sql.Date.valueOf(sMfgStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, mfgDateVal, java.sql.Types.DATE);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Date_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      java.util.Date orDateVal = (java.util.Date) rs.getObject(1);
      long ld = orDateVal.getTime();

      rDateVal = new java.sql.Date(ld);

      msg.addOutputMsg("" + mfgDateVal, "" + rDateVal);
      if (rDateVal.equals(mfgDateVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
   * @testName: testSetObject225
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Timestamp_Tab with the maximum (mfg timestamp) value
   * of Timestamp_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum (mfg timestamp) value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject225() throws Fault {
    java.sql.Date mfgDateVal = null;
    String sMfgStringVal = null;
    java.sql.Timestamp rTimestampVal = null;
    Timestamp mfgTimestampVal = null;
    java.sql.Date rDateVal = null;

    try {
      rsSch.createTab("Timestamp_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Timestamp_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extractthe Value of Timestamp to be Updated to Null Value of Timestamp Table");
      sMfgStringVal = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);
      sMfgStringVal = sMfgStringVal.substring(sMfgStringVal.indexOf('\'') + 1,
          sMfgStringVal.lastIndexOf('\''));

      mfgTimestampVal = Timestamp.valueOf(sMfgStringVal);

      sMfgStringVal = sMfgStringVal.substring(0, sMfgStringVal.indexOf(' '));

      mfgDateVal = java.sql.Date.valueOf(sMfgStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, mfgDateVal, java.sql.Types.TIMESTAMP);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Timestamp_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rTimestampVal = (java.sql.Timestamp) rs.getObject(1);
      String str = rTimestampVal.toString();
      str = str.substring(0, str.indexOf(' '));
      rDateVal = java.sql.Date.valueOf(str);

      msg.addOutputMsg("" + mfgDateVal, "" + rDateVal);
      if (rDateVal.equals(mfgDateVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
   * @testName: testSetObject226
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Char_Tab with the maximum (mfg time) value of
   * Time_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the maximum (mfg time) value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject226() throws Fault {
    String sbrkTimeVal = null;
    String rStringVal = null;
    java.sql.Time brkTimeVal = null;
    java.sql.Time rTimeVal = null;

    try {
      rsSch.createTab("Char_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Char_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extractthe Value of Time to be Updated to Null Value of Char Table");
      sbrkTimeVal = rsSch.extractVal("Time_Tab", 1, sqlp, conn);
      sbrkTimeVal = sbrkTimeVal.substring(sbrkTimeVal.indexOf('\'') + 1,
          sbrkTimeVal.lastIndexOf('\''));
      sbrkTimeVal = sbrkTimeVal.trim();

      brkTimeVal = java.sql.Time.valueOf(sbrkTimeVal);
      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, brkTimeVal, java.sql.Types.CHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rTimeVal = rs.getTime(1);

      msg.addOutputMsg("" + brkTimeVal, "" + rTimeVal);
      if (rTimeVal.equals(brkTimeVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
   * @testName: testSetObject227
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Varchar_Tab with the maximum (mfg time)value of
   * Time_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the maximum (mfg time) value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject227() throws Fault {
    String sbrkTimeVal = null;
    String rStringVal = null;
    java.sql.Time brkTimeVal = null;
    java.sql.Time rTimeVal = null;

    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Varchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extractthe Value of Time to be Updated to be updated to Null Value of Varchar Table");
      sbrkTimeVal = rsSch.extractVal("Time_Tab", 1, sqlp, conn);
      sbrkTimeVal = sbrkTimeVal.substring(sbrkTimeVal.indexOf('\'') + 1,
          sbrkTimeVal.lastIndexOf('\''));
      sbrkTimeVal = sbrkTimeVal.trim();

      brkTimeVal = java.sql.Time.valueOf(sbrkTimeVal);
      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, brkTimeVal, java.sql.Types.VARCHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rTimeVal = rs.getTime(1);

      msg.addOutputMsg("" + brkTimeVal, "" + rTimeVal);
      if (rTimeVal.toString().trim().equals(brkTimeVal.toString().trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        rsSch.dropTab("Varchar_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject228
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Longvarchar_Tab with the maximum (mfg time) value of
   * Time_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the maximum (mfg time) value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject228() throws Fault {
    String sbrkTimeVal = null;
    String rStringVal = null;
    java.sql.Time brkTimeVal = null;
    java.sql.Time rTimeVal = null;

    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Longvarchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extractthe Value of Time to be Updated for Null Value of Longvarchar Table");
      sbrkTimeVal = rsSch.extractVal("Time_Tab", 1, sqlp, conn);
      sbrkTimeVal = sbrkTimeVal.substring(sbrkTimeVal.indexOf('\'') + 1,
          sbrkTimeVal.lastIndexOf('\''));
      sbrkTimeVal = sbrkTimeVal.trim();

      brkTimeVal = java.sql.Time.valueOf(sbrkTimeVal);
      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, brkTimeVal, java.sql.Types.LONGVARCHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);
      rStringVal = rStringVal.trim();
      rTimeVal = java.sql.Time.valueOf(rStringVal);

      msg.addOutputMsg("" + brkTimeVal, "" + rTimeVal);
      if (rTimeVal.equals(brkTimeVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        rsSch.dropTab("Longvarcharnull_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject229
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Time_Tab with the maximum (mfg time) value of
   * Time_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the maximum (mfg time) value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject229() throws Fault {
    java.sql.Time brkTimeVal = null;
    String sBrkTimeVal = null;
    java.sql.Time rTimeVal = null;

    try {
      rsSch.createTab("Time_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Time_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extractthe Value of Time to be Updated ");
      sBrkTimeVal = rsSch.extractVal("Time_Tab", 1, sqlp, conn);
      sBrkTimeVal = sBrkTimeVal.substring(sBrkTimeVal.indexOf('\'') + 1,
          sBrkTimeVal.lastIndexOf('\''));

      // to convert String value into java.sql.Time value
      brkTimeVal = brkTimeVal.valueOf(sBrkTimeVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, brkTimeVal, java.sql.Types.TIME);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Time_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      java.util.Date orTimeVal = (java.util.Date) rs.getObject(1);

      long lt = orTimeVal.getTime();

      rTimeVal = new java.sql.Time(lt);

      msg.addOutputMsg("" + brkTimeVal, "" + rTimeVal);
      if (rTimeVal.toString().trim().equals(brkTimeVal.toString().trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
   * @testName: testSetObject230
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Char_Tab with the maximum (brktime) value of
   * Timestamp_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum (brktime) value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject230() throws Fault {
    String maxStringVal = null;
    String rStringVal = null;
    Timestamp maxTimestampVal = null;
    Timestamp rTimestampVal = null;

    try {
      // to create the Char table
      rsSch.createTab("Char_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Char_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extractthe Value of Timestamp to be Updated");
      maxStringVal = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);
      maxStringVal = maxStringVal.substring(maxStringVal.indexOf('\'') + 1,
          maxStringVal.lastIndexOf('\''));
      maxTimestampVal = java.sql.Timestamp.valueOf(maxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxTimestampVal, java.sql.Types.CHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Char_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = (String) rs.getObject(1);
      rStringVal = rStringVal.trim();
      rTimestampVal = java.sql.Timestamp.valueOf(rStringVal);

      msg.addOutputMsg("" + maxTimestampVal, "" + rTimestampVal);
      if (rTimestampVal.equals(maxTimestampVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
   * @testName: testSetObject231
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Varchar_Tab with the maximum (brktime) value of
   * Timestamp_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum (brktime) value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject231() throws Fault {
    Timestamp maxTimestampVal = null;
    String maxStringVal = null;
    String rStringVal = null;
    Timestamp rTimestampVal = null;

    try {
      // to create the varchar table
      rsSch.createTab("Varchar_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Varchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extractthe Value of Timestamp to be Updated");
      maxStringVal = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);
      maxStringVal = maxStringVal.substring(maxStringVal.indexOf('\'') + 1,
          maxStringVal.lastIndexOf('\''));
      maxTimestampVal = Timestamp.valueOf(maxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxTimestampVal, java.sql.Types.VARCHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getObject(1).toString();
      rStringVal = rStringVal.trim();
      rTimestampVal = Timestamp.valueOf(rStringVal);

      msg.addOutputMsg("" + maxTimestampVal, "" + rTimestampVal);
      if (rTimestampVal.equals(maxTimestampVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        rsSch.dropTab("Varchar_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject232
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Longvarchar_Tab with the maximum (brktime) value of
   * Timestamp_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum (brktime) value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject232() throws Fault {
    String maxStringVal = null;
    Timestamp maxTimestampVal = null;
    String rStringVal = null;
    Timestamp rTimestampVal = null;

    try {
      // to drop the table Longvarchar table
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Longvarchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extractthe Value of Timestamp to be Updated ");
      maxStringVal = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);
      maxStringVal = maxStringVal.substring(maxStringVal.indexOf('\'') + 1,
          maxStringVal.lastIndexOf('\''));
      maxTimestampVal = Timestamp.valueOf(maxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxTimestampVal, java.sql.Types.LONGVARCHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Longvarchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getObject(1).toString();
      rStringVal = rStringVal.trim();

      rTimestampVal = Timestamp.valueOf(rStringVal);

      msg.addOutputMsg("" + maxTimestampVal, "" + rTimestampVal);
      if (rTimestampVal.equals(maxTimestampVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
        rsSch.dropTab("Longvarcharnull_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject233
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Date_Tab with the maximum (brktime) value of
   * Timestamp_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum (brktime) value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject233() throws Fault {
    String sInTimeVal = null;
    String sRetVal = null;
    java.sql.Date inDateVal = null;
    java.sql.Date rDateVal = null;

    try {
      // to create the Date table
      rsSch.createTab("Date_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Date_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extractthe Value of Timestamp to be Updated");
      sInTimeVal = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);

      sInTimeVal = sInTimeVal.substring(sInTimeVal.indexOf('\'') + 1,
          sInTimeVal.lastIndexOf('\''));
      sInTimeVal = sInTimeVal.substring(0, sInTimeVal.indexOf(' '));

      inDateVal = java.sql.Date.valueOf(sInTimeVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, inDateVal, java.sql.Types.DATE);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Date_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      java.util.Date orDateVal = (java.util.Date) rs.getObject(1);
      long ld = orDateVal.getTime();

      rDateVal = new java.sql.Date(ld);

      msg.addOutputMsg("" + inDateVal, "" + rDateVal);
      if (rDateVal.equals(inDateVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
   * @testName: testSetObject234
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Time_Tab with the maximum (brktime) value of
   * Timestamp_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum (brktime) value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject234() throws Fault {
    java.sql.Time brkTimeVal = null;
    String sBrkStringVal = null;
    java.sql.Time rTimeVal = null;
    Timestamp brkTimestampVal = null;

    try {
      // to create the Time table
      rsSch.createTab("Time_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Time_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Value of Timestamp to be Updated ");
      sBrkStringVal = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);
      sBrkStringVal = sBrkStringVal.substring(sBrkStringVal.indexOf('\'') + 1,
          sBrkStringVal.lastIndexOf('\''));
      brkTimestampVal = Timestamp.valueOf(sBrkStringVal);

      sBrkStringVal = sBrkStringVal.substring(sBrkStringVal.indexOf(' ') + 1,
          sBrkStringVal.length());
      brkTimeVal = java.sql.Time.valueOf(sBrkStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, brkTimestampVal, java.sql.Types.TIME);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Time_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      java.util.Date orTimeVal = (java.util.Date) rs.getObject(1);
      long lt = orTimeVal.getTime();

      rTimeVal = new java.sql.Time(lt);

      msg.addOutputMsg("" + brkTimeVal, "" + rTimeVal);
      if (rTimeVal.toString().trim().equals(brkTimeVal.toString().trim())) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
   * @testName: testSetObject235
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Timestamp_Tab with the maximum (brktime) value of
   * Timestamp_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum (brktime) value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */

  public void testSetObject235() throws Fault {
    java.sql.Timestamp inTimeVal = null;
    String sInTimeVal = null;
    java.sql.Timestamp inTimestampVal = null;
    java.sql.Timestamp rTimestampVal = null;

    try {

      rsSch.createTab("Timestamp_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Timestamp_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extractthe Value of Timestamp to be Updated ");
      sInTimeVal = rsSch.extractVal("Timestamp_Tab", 1, sqlp, conn);

      sInTimeVal = sInTimeVal.substring(sInTimeVal.indexOf('\'') + 1,
          sInTimeVal.lastIndexOf('\''));
      inTimestampVal = java.sql.Timestamp.valueOf(sInTimeVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, inTimestampVal, java.sql.Types.TIMESTAMP);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Timestamp_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rTimestampVal = (java.sql.Timestamp) rs.getObject(1);

      msg.addOutputMsg("" + inTimestampVal, "" + rTimestampVal);
      if (rTimestampVal.equals(inTimestampVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the Object");
      } else {
        msg.printTestError(
            "setObject Method does not set the designated parameter with the Object",
            "test setObject Failed!");

      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to setObject is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to setObject is Failed!");

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
