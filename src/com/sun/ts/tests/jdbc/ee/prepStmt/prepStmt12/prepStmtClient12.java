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
 * $Id$
 */

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt12;

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
 * The prepStmtClient12 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 11/27/00
 */

public class prepStmtClient12 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.prepStmt.prepStmt12";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private ResultSet rs = null;

  private Statement stmt = null;

  private PreparedStatement pstmt = null;

  private DataSource ds1 = null;

  private dbSchema dbSch = null;

  private rsSchema rsSch = null;

  private csSchema csSch = null;

  private String drManager = null;

  private transient DatabaseMetaData dbmd = null;

  private Properties sqlp = null;

  private Properties props = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    prepStmtClient12 theTests = new prepStmtClient12();
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
        logErr("SQL Exception: " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testSetObject163
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val of Varchar_Tab with the minimum value of Bigint_Tab.
   * Call the getObject(int columnno) method to retrieve this value. Extract the
   * minimum value from the tssql.stmt file. Compare this value with the value
   * returned by the getObject(int columnno) method. Both the values should be
   * equal.
   */

  public void testSetObject163() throws Fault {
    Long maxLongVal = null;
    String maxStringVal = null;
    String rStringVal = null;

    try {
      rsSch.createTab("Varchar_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Varchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Minimum Value of BigInteger, to be Updated for NULL value of Varchar Table");
      maxStringVal = rsSch.extractVal("Bigint_Tab", 2, sqlp, conn);
      maxLongVal = new Long(maxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxLongVal, java.sql.Types.VARCHAR);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      String Null_Val_Query = sqlp.getProperty("Varchar_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      rStringVal = rs.getObject(1).toString();
      rStringVal = rStringVal.trim();
      maxStringVal = maxStringVal.trim();

      msg.addOutputMsg(maxStringVal, rStringVal);
      if (rStringVal.equals(maxStringVal)) {
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
   * @testName: testSetObject164
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Longvarchar_Tab with the maximum value
   * of Bigint_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject164() throws Fault {
    String maxStringVal = null;
    Long maxLongVal = null;
    String rStringVal = null;

    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Longvarchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg(
          "extract the Maximum Value of Biginteger, to be Updated for NULL value of Longvarchar Table  ");
      maxStringVal = rsSch.extractVal("Bigint_Tab", 1, sqlp, conn);
      maxLongVal = new Long(maxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxLongVal, java.sql.Types.LONGVARCHAR);
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
      maxStringVal = maxStringVal.trim();

      msg.addOutputMsg(maxStringVal, rStringVal);
      if (rStringVal.equals(maxStringVal)) {
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
   * @testName: testSetObject165
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Longvarchar_Tab with the minimum value
   * of Bigint_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject165() throws Fault {
    String maxStringVal = null;
    Long maxLongVal = null;
    String rStringVal = null;

    try {
      rsSch.createTab("Longvarcharnull_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Longvarchar_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);
      msg.setMsg(
          "extract the Minimum Value of Bigint, to be Updated for NULL value of Varchar Table");
      maxStringVal = rsSch.extractVal("Bigint_Tab", 2, sqlp, conn);
      maxLongVal = new Long(maxStringVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, maxLongVal, java.sql.Types.LONGVARCHAR);
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
      maxStringVal = maxStringVal.trim();

      msg.addOutputMsg(maxStringVal, rStringVal);
      if (rStringVal.equals(maxStringVal)) {
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
   * @testName: testSetObject166
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Tinyint_Tab with the maximum value of
   * Tinyint_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject166() throws Fault {
    Float oMaxTinyintVal = null;
    Integer rTinyintVal = null;
    String smaxTinyintVal = null;
    Integer maxTinyintVal = null;

    try {
      // to create the Tinyint Table
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Tinyint_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Tinyint to be Updated");
      smaxTinyintVal = rsSch.extractVal("Tinyint_Tab", 1, sqlp, conn);

      oMaxTinyintVal = new Float(smaxTinyintVal);
      maxTinyintVal = new Integer(smaxTinyintVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxTinyintVal, java.sql.Types.TINYINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Tinyint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orTinyintVal = rs.getObject(1);
      rTinyintVal = new Integer(orTinyintVal.toString());

      msg.addOutputMsg("" + maxTinyintVal, "" + rTinyintVal);
      if (rTinyintVal.equals(maxTinyintVal)) {
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
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject167
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Tinyint_Tab with the minimum value of
   * Tinyint_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject167() throws Fault {
    Float oMinTinyintVal = null;
    Integer rTinyintVal = null;
    String sminTinyintVal = null;
    Integer minTinyintVal = null;

    try {
      // to create the Tinyint Table
      rsSch.createTab("Tinyint_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Tinyint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Tinyint to be Updated");
      sminTinyintVal = rsSch.extractVal("Tinyint_Tab", 2, sqlp, conn);

      oMinTinyintVal = new Float(sminTinyintVal);
      minTinyintVal = new Integer(sminTinyintVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinTinyintVal, java.sql.Types.TINYINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Tinyint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orTinyintVal = rs.getObject(1);
      rTinyintVal = new Integer(orTinyintVal.toString());

      msg.addOutputMsg("" + minTinyintVal, "" + rTinyintVal);

      if (rTinyintVal.equals(minTinyintVal)) {
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
        rsSch.dropTab("Tinyint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject168
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Smallint_Tab with the maximum value of
   * Smallint_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the maximum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject168() throws Fault {
    Float oMaxSmallintVal = null;
    Integer rSmallintVal = null;
    String smaxSmallintVal = null;
    Integer maxSmallintVal = null;

    try {
      // to create the Smallint Table
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Smallint_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Smallint to be Updated");
      smaxSmallintVal = rsSch.extractVal("Smallint_Tab", 1, sqlp, conn);

      oMaxSmallintVal = new Float(smaxSmallintVal);
      maxSmallintVal = new Integer(smaxSmallintVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxSmallintVal, java.sql.Types.SMALLINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Smallint_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orSmallintVal = rs.getObject(1);
      rSmallintVal = new Integer(orSmallintVal.toString());

      msg.addOutputMsg("" + maxSmallintVal, "" + rSmallintVal);

      if (rSmallintVal.equals(maxSmallintVal)) {
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
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject169
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Smallint_Tab with the minimum value of
   * Smallint_Tab. Call the getObject(int columnno) method to retrieve this
   * value. Extract the minimum value from the tssql.stmt file. Compare this
   * value with the value returned by the getObject(int columnno) method. Both
   * the values should be equal.
   */

  public void testSetObject169() throws Fault {
    Float oMinSmallintVal = null;
    Integer rSmallintVal = null;
    String sminSmallintVal = null;
    Integer minSmallintVal = null;

    try {
      // to create the Smallint Table
      rsSch.createTab("Smallint_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Smallint_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Smallint to be Updated ");
      sminSmallintVal = rsSch.extractVal("Smallint_Tab", 2, sqlp, conn);

      oMinSmallintVal = new Float(sminSmallintVal);
      minSmallintVal = new Integer(sminSmallintVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinSmallintVal, java.sql.Types.SMALLINT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Smallint_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orSmallintVal = rs.getObject(1);
      rSmallintVal = new Integer(orSmallintVal.toString());

      msg.addOutputMsg("" + minSmallintVal, "" + rSmallintVal);
      if (rSmallintVal.equals(minSmallintVal)) {
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
        rsSch.dropTab("Smallint_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject170
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Integer_Tab with the some integer value
   * after converting it to Float. Call the getObject(int columnno) method to
   * retrieve this value. Compare this value with the value that is inserted
   * into the databse using setObject method. Both the values should be equal.
   */

  public void testSetObject170() throws Fault {
    Float oMaxIntegerVal = null;
    Integer rIntegerVal = null;
    String smaxIntegerVal = null;
    Integer maxIntegerVal = null;

    try {

      // to create the Integer Table
      rsSch.createTab("Integer_Tab", sqlp, conn);

      // get the prepStmt object for updating the Integer_Tab
      String sPrepStmt = sqlp.getProperty("Integer_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("get an Integer value to update");

      int intVal = 1000;

      smaxIntegerVal = new Integer(intVal).toString();

      oMaxIntegerVal = new Float(smaxIntegerVal);
      maxIntegerVal = new Integer(smaxIntegerVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxIntegerVal, java.sql.Types.INTEGER);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Integer_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orIntegerVal = rs.getObject(1);
      rIntegerVal = new Integer(orIntegerVal.toString());

      msg.addOutputMsg("" + maxIntegerVal, "" + rIntegerVal);
      if (rIntegerVal.equals(maxIntegerVal)) {
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
        rsSch.dropTab("Integer_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject174
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Real_Tab with the maximum value of
   * Real_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the maximum value from the tssql.stmt file. Compare this value with
   * the value returned by the getObject(int columnno) method. Both the values
   * should be equal.
   */

  public void testSetObject174() throws Fault {
    Float oMaxRealVal = null;
    Float rRealVal = null;
    String smaxRealVal = null;

    try {
      // to create the Real Table
      rsSch.createTab("Real_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Real_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Float to be Updated");
      smaxRealVal = rsSch.extractVal("Real_Tab", 1, sqlp, conn);

      oMaxRealVal = new Float(smaxRealVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxRealVal, java.sql.Types.REAL);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Real_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orRealVal = rs.getObject(1);
      rRealVal = new Float(orRealVal.toString());

      msg.addOutputMsg("" + oMaxRealVal, "" + rRealVal);
      if (rRealVal.equals(oMaxRealVal)) {
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
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject175
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Real_Tab with the minimum value of
   * Real_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the minimum value from the tssql.stmt file. Compare this value with
   * the value returned by the getObject(int columnno) method. Both the values
   * should be equal.
   */

  public void testSetObject175() throws Fault {
    Float oMinRealVal = null;
    Float rRealVal = null;
    String sminRealVal = null;

    try {
      // to create the Bigint Table
      rsSch.createTab("Real_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Real_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Long to be Updated");
      sminRealVal = rsSch.extractVal("Real_Tab", 2, sqlp, conn);

      oMinRealVal = new Float(sminRealVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinRealVal, java.sql.Types.REAL);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Real_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orRealVal = rs.getObject(1);
      rRealVal = new Float(orRealVal.toString());

      msg.addOutputMsg("" + oMinRealVal, "" + rRealVal);

      if (rRealVal.equals(oMinRealVal)) {
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
        rsSch.dropTab("Real_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject176
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Min_Val of Float_Tab with the maximum value of
   * Float_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the maximum value from the tssql.stmt file. Compare this value with
   * the value returned by the getObject(int columnno) method. Both the values
   * should be equal.
   */

  public void testSetObject176() throws Fault {
    Float oMaxFloatVal = null;
    Float rFloatVal = null;
    String smaxFloatVal = null;
    Float maxFloatVal = null;
    Float rf = null;

    try {
      // to create the Float Table
      rsSch.createTab("Float_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Float_Tab_Min_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Maximum Value of Float to be Updated");
      smaxFloatVal = rsSch.extractVal("Float_Tab", 1, sqlp, conn);

      oMaxFloatVal = new Float(smaxFloatVal);
      maxFloatVal = new Float(smaxFloatVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMaxFloatVal, java.sql.Types.FLOAT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Min_Val_Query = sqlp.getProperty("Float_Query_Min", "");
      msg.setMsg(Min_Val_Query);
      rs = stmt.executeQuery(Min_Val_Query);
      rs.next();

      Object orFloatVal = rs.getObject(1);
      rFloatVal = new Float(orFloatVal.toString());

      msg.addOutputMsg("" + oMaxFloatVal, "" + rFloatVal);

      if (rFloatVal.equals(oMaxFloatVal)) {
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
        rsSch.dropTab("Float_Tab", conn);
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: testSetObject177
   * 
   * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
   * method,update the column Null_Val of Float_Tab with the minimum value of
   * Float_Tab. Call the getObject(int columnno) method to retrieve this value.
   * Extract the minimum value from the tssql.stmt file. Compare this value with
   * the value returned by the getObject(int columnno) method. Both the values
   * should be equal.
   */

  public void testSetObject177() throws Fault {
    Float oMinFloatVal = null;
    Float rFloatVal = null;
    String sminFloatVal = null;
    Float minFloatVal = null;

    try {
      // to create the Float Table
      rsSch.createTab("Float_Tab", sqlp, conn);

      String sPrepStmt = sqlp.getProperty("Float_Tab_Null_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      msg.setMsg("extract the Minimum Value of Double to be Updated");
      sminFloatVal = rsSch.extractVal("Float_Tab", 2, sqlp, conn);

      oMinFloatVal = new Float(sminFloatVal);
      minFloatVal = new Float(sminFloatVal);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setObject(1, oMinFloatVal, java.sql.Types.FLOAT);
      pstmt.executeUpdate();

      msg.setMsg(
          "to query from the database to check the call of pstmt.executeUpdate");
      // to get the query string
      String Null_Val_Query = sqlp.getProperty("Float_Query_Null", "");
      msg.setMsg(Null_Val_Query);
      rs = stmt.executeQuery(Null_Val_Query);
      rs.next();

      Object orFloatVal = rs.getObject(1);
      rFloatVal = new Float(orFloatVal.toString());

      msg.addOutputMsg("" + minFloatVal, "" + rFloatVal);
      if (rFloatVal.equals(minFloatVal)) {
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
        rsSch.dropTab("Float_Tab", conn);
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
