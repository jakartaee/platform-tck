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
 * @(#)batchUpdateClient.java	1.34 03/05/16
 */
package com.sun.ts.tests.jdbc.ee.batchUpdate;

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
 * The batchUpdateClient class tests methods of Statement, PreparedStatement and
 * CallableStatement interfaces using Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class batchUpdateClient extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.batchUpdate";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements
  private transient Connection conn = null;

  private ResultSet rs = null;

  private Statement stmt = null;

  private DataSource ds1 = null;

  private dbSchema dbSch = null;

  private String drManager = null;

  private transient DatabaseMetaData dbmd = null;

  private Properties sqlp = null;

  private boolean supbatupdflag;

  private String fTableName = null;

  PreparedStatement pstmt = null;

  PreparedStatement pstmt1 = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    batchUpdateClient theTests = new batchUpdateClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

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
        fTableName = p.getProperty("ftable", "");

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

        dbSch = new dbSchema();
        dbSch.createData(p, conn);
        dbmd = conn.getMetaData();
        supbatupdflag = dbmd.supportsBatchUpdates();
        logTrace("Driver Supports BatchUpdates  : " + supbatupdflag);
        msg = new JDBCTestMsg();

        if (!supbatupdflag) {
          logTrace("Driver does not support Batch Updates ");
          throw new Fault("Driver does not support Batch Updates ");
        }

        Insert_Tab(p, conn);
        // conn.setAutoCommit(false);
        stmt = conn.createStatement();

      } catch (SQLException ex) {
        logErr("SQL Exception : " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /* This Method is to insert new Values into the Table */
  public void Insert_Tab(Properties testProps, Connection conn) throws Fault {
    String newName = null;
    float newPrice = 0;
    int newType = 0;
    String sDelete = sqlp.getProperty("BatchUpdate_Delete");

    try {
      logTrace("About to delete the Existing Rows");
      Statement stm = conn.createStatement();
      stm.execute(sDelete);
      logTrace("Deleted the Previous Existed Rows ");

      String strCofSize = testProps.getProperty("cofSize");
      TestUtil.logTrace("strCofSize: " + strCofSize);

      String strCofTypeSize = testProps.getProperty("cofTypeSize");
      TestUtil.logTrace("strCofTypeSize : " + strCofTypeSize);

      int cofTypeSize = Integer.parseInt(strCofTypeSize);
      int cofSize = Integer.parseInt(strCofSize);

      // Inserting the New Value
      TestUtil.logTrace("Adding the " + fTableName + " table rows");
      String updateString1 = sqlp.getProperty("BatchInsert_String");
      pstmt = conn.prepareStatement(updateString1);

      int newKey = 1;
      for (int i = 1; i <= cofTypeSize && newKey <= cofSize; i++) {
        for (int j = 1; j <= i && newKey <= cofSize; j++) {
          newName = "COFFEE-" + newKey;
          newPrice = newKey + (float) .00;
          newType = i;
          pstmt.setInt(1, newKey);
          pstmt.setString(2, newName);
          pstmt.setFloat(3, newPrice);
          pstmt.setInt(4, newType);
          pstmt.executeUpdate();
          newKey = newKey + 1;
        }
      }

      logTrace("Inserted the Rows ");
    } catch (SQLException sql) {
      logErr("SQL Exception " + sql.getMessage());
      throw new Fault("Call to SetupFailed!", sql);
    } catch (Exception ex) {
      logErr("Exception " + ex.getMessage());
      throw new Fault("Call to Setup Failed!", ex);
    }
  }

  /*
   * @testName: testAddBatch01
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:700; JDBC:JAVADOC:701;
   * JDBC:SPEC:23;
   * 
   * 
   * @test_Strategy: Get a PreparedStatement object and call the addBatch()
   * method with 3 SQL statements and call the executeBatch() method and it
   * should return array of Integer values of length 3
   *
   */
  public void testAddBatch01() throws Fault {
    int i = 0;
    int retValue[] = { 0, 0, 0 };
    try {
      String sPrepStmt = sqlp.getProperty("CoffeeTab_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setInt(1, 2);
      pstmt.addBatch();

      pstmt.setInt(1, 3);
      pstmt.addBatch();

      pstmt.setInt(1, 4);
      pstmt.addBatch();

      int[] updateCount = pstmt.executeBatch();
      int updateCountlen = updateCount.length;

      msg.setMsg("Successfully Updated");
      msg.setMsg("updateCount Length :" + updateCountlen);

      if (updateCountlen != 3) {
        msg.printTestError("addBatch does not add the SQL Statements to Batch ",
            "call to addBatch failed");

      } else {
        msg.setMsg("addBatch add the SQL statements to Batch ");
      }

      String sPrepStmt1 = sqlp.getProperty("BatchUpdate_Query");

      pstmt1 = conn.prepareStatement(sPrepStmt1);

      // 2 is the number that is set First for Type Id in Prepared Statement
      for (int n = 2; n <= 4; n++) {
        pstmt1.setInt(1, n);
        rs = pstmt1.executeQuery();
        rs.next();
        retValue[i++] = rs.getInt(1);
      }

      pstmt1.close();

      for (int j = 0; j < updateCount.length; j++) {

        msg.addOutputMsg("" + updateCount[j], "" + retValue[j]);
        if (updateCount[j] != retValue[j]
            && updateCount[j] != Statement.SUCCESS_NO_INFO) {
          msg.printTestError(
              "affected row count does not match with the updateCount value",
              "Call to addBatch is Failed!");
        }
      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (BatchUpdateException b) {
      msg.printSQLError(b,
          "BatchUpdateException :  Call to addBatch is Failed!");

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to addBatch is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to addBatch is Failed!");

    }
  }

  /*
   * @testName: testAddBatch02
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:183; JDBC:JAVADOC:184;
   * JDBC:JAVADOC:187; JDBC:JAVADOC:188; JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a Statement object and call the addBatch() method with
   * 3 SQL statements and call the executeBatch() method and it should return an
   * array of Integer of length 3.
   *
   */
  public void testAddBatch02() throws Fault {
    int i = 0;
    int retValue[] = { 0, 0, 0 };
    int updCountLength = 0;

    try {
      String sUpdCoffee = sqlp.getProperty("Upd_Coffee_Tab");
      String sDelCoffee = sqlp.getProperty("Del_Coffee_Tab");
      String sInsCoffee = sqlp.getProperty("Ins_Coffee_Tab");

      msg.setMsg("execute the addBatch method");
      stmt.addBatch(sUpdCoffee);
      stmt.addBatch(sDelCoffee);
      stmt.addBatch(sInsCoffee);

      int[] updateCount = stmt.executeBatch();
      updCountLength = updateCount.length;

      if (updCountLength != 3) {
        msg.printTestError("addBatch does not add the SQL Statements to Batch ",
            "Call to addBatch is Failed!");

      } else {
        msg.setMsg("addBatch add the SQL statements to Batch ");
      }

      String sPrepStmt1 = sqlp.getProperty("BatchUpdate_Query");

      pstmt1 = conn.prepareStatement(sPrepStmt1);

      // 2 is the number that is set First for Type Id in Prepared Statement
      pstmt1.setInt(1, 1);
      rs = pstmt1.executeQuery();
      rs.next();
      retValue[i++] = rs.getInt(1);

      pstmt1.close();

      // 1 as delete Statement will delete only one row
      retValue[i++] = 1;
      // 1 as insert Statement will insert only one row
      retValue[i++] = 1;
      msg.setMsg("ReturnValue count : " + retValue.length);

      for (int j = 0; j < updateCount.length; j++) {

        msg.addOutputMsg("" + updateCount[j], "" + retValue[j]);
        if (updateCount[j] != retValue[j]) {
          msg.setMsg(
              "affected row count does not match with the updateCount value");
        }
      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (BatchUpdateException b) {
      msg.printSQLError(b,
          "BatchUpdateException :  Call to addBatch is Failed!");

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to addBatch is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to addBatch is Failed!");

    }
  }

  /*
   * @testName: testAddBatch03
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:700; JDBC:JAVADOC:701;
   * JDBC:JAVADOC:187; JDBC:JAVADOC:188; JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a CallableStatement object and call the addBatch()
   * method with 3 SQL statements and call the executeBatch() method and it
   * should return an array of Integer of length 3.
   */
  public void testAddBatch03() throws Fault {
    int i = 0;
    int retValue[] = { 0, 0, 0 };
    int updCountLength = 0;
    try {
      msg.setMsg("get the CallableStatement object");
      CallableStatement cstmt = conn.prepareCall("{call UpdCoffee_Proc(?)}");
      cstmt.setInt(1, 2);
      cstmt.addBatch();

      cstmt.setInt(1, 3);
      cstmt.addBatch();

      cstmt.setInt(1, 4);
      cstmt.addBatch();

      msg.setMsg("execute the executeBatch method");
      int[] updateCount = cstmt.executeBatch();
      updCountLength = updateCount.length;

      msg.setMsg("Successfully Updated");

      if (updCountLength != 3) {
        msg.printTestError("addBatch does not add the SQL Statements to Batch ",
            "Call to addBatch is Failed!");

      } else {
        msg.setMsg("addBatch add the SQL statements to Batch ");
      }

      String sPrepStmt1 = sqlp.getProperty("BatchUpdate_Query");

      pstmt1 = conn.prepareStatement(sPrepStmt1);

      // 2 is the number that is set First for Type Id in Prepared Statement
      for (int n = 2; n <= 4; n++) {
        pstmt1.setInt(1, n);
        rs = pstmt1.executeQuery();
        rs.next();
        retValue[i++] = rs.getInt(1);
      }

      pstmt1.close();

      for (int j = 0; j < updateCount.length; j++) {
        msg.addOutputMsg("" + updateCount[j], "" + retValue[j]);
        if (updateCount[j] != retValue[j]) {
          msg.setMsg("addBatch does not add the SQL Statements to Batch");
        }
      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (BatchUpdateException b) {
      msg.printSQLError(b,
          "BatchUpdateException :  Call to addBatch is Failed!");

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to addBatch is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to addBatch is Failed!");

    }
  }

  /*
   * @testName: testClearBatch01
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:185; JDBC:JAVADOC:186;
   * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a PreparedStatement object and call the addBatch()
   * method and call the clearBatch() method and then call executeBatch() to
   * check the call of clearBatch()method The executeBatch() method should
   * return a zero value.
   */
  public void testClearBatch01() throws Fault {
    try {
      String sPrepStmt = sqlp.getProperty("CoffeeTab_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setInt(1, 2);
      pstmt.addBatch();

      pstmt.setInt(1, 3);
      pstmt.addBatch();

      pstmt.setInt(1, 4);
      pstmt.addBatch();

      msg.setMsg("execute clearBatch() method");
      pstmt.clearBatch();
      int[] updateCount = pstmt.executeBatch();
      int updCountLength = updateCount.length;

      if (updCountLength == 0) {
        msg.setMsg("clearBatch Method clears the current Batch ");
      } else {
        msg.printTestError(
            "clearBatch Method does not clear the Current Batch ",
            "Call to clearBatch is Failed!");

      }
      msg.printTestMsg();

    } catch (BatchUpdateException b) {
      msg.printSQLError(b,
          "BatchUpdateException :  Call to clearBatch is Failed!");
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to clearBatch is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to clearBatch is Failed!");

    }
  }

  /*
   * @testName: testClearBatch02
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:185; JDBC:JAVADOC:186;
   * JDBC:JAVADOC:183; JDBC:JAVADOC:184; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a Statement object and call the addBatch() method and
   * call the clearBatch() method and then call executeBatch() to check the call
   * of clearBatch()method.The executeBatch() method should return a zero value.
   *
   */
  public void testClearBatch02() throws Fault {
    int updCountLength = 0;
    try {
      String sUpdCoffee = sqlp.getProperty("Upd_Coffee_Tab");
      String sInsCoffee = sqlp.getProperty("Ins_Coffee_Tab");
      String sDelCoffee = sqlp.getProperty("Del_Coffee_Tab");

      msg.setMsg("execute addBatch method");
      stmt.addBatch(sUpdCoffee);
      stmt.addBatch(sDelCoffee);
      stmt.addBatch(sInsCoffee);

      msg.setMsg("execute clearBatch method");
      stmt.clearBatch();

      int[] updateCount = stmt.executeBatch();
      updCountLength = updateCount.length;

      if (updCountLength == 0) {
        msg.setMsg("clearBatch Method clears the current Batch ");
      } else {
        msg.printTestError("clearBatch Method does not clear the current Batch",
            "Call to clearBatch is Failed!");

      }
      msg.printTestMsg();

    } catch (BatchUpdateException b) {
      msg.printSQLError(b,
          "BatchUpdateException :  Call to clearBatch is Failed!");
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to clearBatch is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to clearBatch is Failed!");

    }
  }

  /*
   * @testName: testClearBatch03
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:185; JDBC:JAVADOC:186;
   * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a CallableStatement object and call the addBatch()
   * method and call the clearBatch() method and then call executeBatch() to
   * check the call of clearBatch()method. The executeBatch() method should
   * return a zero value.
   *
   */
  public void testClearBatch03() throws Fault {
    int updCountLength = 0;
    try {
      msg.setMsg("get the CallableStatement object");
      CallableStatement cstmt = conn.prepareCall("{call UpdCoffee_Proc(?)}");
      cstmt.setInt(1, 2);
      cstmt.addBatch();

      cstmt.setInt(1, 3);
      cstmt.addBatch();

      cstmt.setInt(1, 4);
      cstmt.addBatch();

      msg.setMsg("execute clearBatch method");
      cstmt.clearBatch();
      int[] updateCount = cstmt.executeBatch();

      updCountLength = updateCount.length;

      if (updCountLength == 0) {
        msg.setMsg("clearBatch Method clears the current Batch ");
      } else {
        msg.printTestError("clearBatch Method does not clear the current Batch",
            "Call to clearBatch is Failed!");

      }
      msg.printTestMsg();

    } catch (BatchUpdateException b) {
      msg.printSQLError(b,
          "BatchUpdateException :  Call to addBatch is Failed!");

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to clearBatch is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to clearBatch is Failed!");

    }
  }

  /*
   * @testName: testExecuteBatch01
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a PreparedStatement object and call the addBatch()
   * method with a 3 valid SQL statements and call the executeBatch() method It
   * should return an array of Integer values of length 3.
   *
   */
  public void testExecuteBatch01() throws Fault {
    int i = 0;
    int retValue[] = { 0, 0, 0 };
    int updCountLength = 0;
    try {
      String sPrepStmt = sqlp.getProperty("CoffeeTab_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setInt(1, 1);
      pstmt.addBatch();

      pstmt.setInt(1, 2);
      pstmt.addBatch();

      pstmt.setInt(1, 3);
      pstmt.addBatch();

      msg.setMsg("execute the executeBatch Method");
      int[] updateCount = pstmt.executeBatch();
      updCountLength = updateCount.length;
      msg.setMsg("Successfully Updated");
      msg.setMsg("updateCount Length :" + updCountLength);

      if (updCountLength != 3) {
        msg.printTestError(
            "executeBatch does not execute the Batch of SQL statements",
            "Call to executeBatch is Failed!");

      } else {
        msg.setMsg("executeBatch executes the Batch of SQL statements");
      }

      String sPrepStmt1 = sqlp.getProperty("BatchUpdate_Query");

      pstmt1 = conn.prepareStatement(sPrepStmt1);

      for (int n = 1; n <= 3; n++) {
        pstmt1.setInt(1, n);
        rs = pstmt1.executeQuery();
        rs.next();
        retValue[i++] = rs.getInt(1);
      }

      pstmt1.close();

      msg.setMsg("retvalue length : " + retValue.length);

      for (int j = 0; j < updateCount.length; j++) {
        msg.addOutputMsg("" + updateCount[j], "" + retValue[j]);
        if (updateCount[j] != retValue[j]
            && updateCount[j] != Statement.SUCCESS_NO_INFO) {
          msg.printTestError(
              "executeBatch does not execute the Batch of SQL statements",
              "Call to executeBatch is Failed!");
        }
      }
      msg.printTestMsg();
      msg.printOutputMsg();
    } catch (BatchUpdateException b) {
      msg.printSQLError(b,
          "BatchUpdateException :  Call to executeBatch is Failed!");

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to executeBatch is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to executeBatch is Failed!");

    }
  }

  /*
   * @testName: testExecuteBatch02
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * 
   * @test_Strategy: Get a PreparedStatement object and call the executeBatch()
   * method without calling addBatch() method.It should return an array of zero
   * length.
   */
  public void testExecuteBatch02() throws Fault {
    try {
      String sPrepStmt = sqlp.getProperty("CoffeeTab_Update", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setInt(1, 1);
      pstmt.setInt(1, 2);
      pstmt.setInt(1, 3);

      int[] updateCount = pstmt.executeBatch();
      int updCountLength = updateCount.length;
      msg.setMsg("UpdateCount Length : " + updCountLength);

      if (updCountLength == 0) {
        msg.setMsg("executeBatch does not execute Empty Batch");
      } else {
        msg.printTestError("executeBatch executes Empty Batch",
            "Call to executeBatch is Failed!");

      }
      msg.printTestMsg();

    } catch (BatchUpdateException b) {
      msg.printSQLError(b,
          "BatchUpdateException :  Call to executeBatch is Failed!");

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to executeBatch is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to executeBatch is Failed!");

    }
  }

  /*
   * @testName: testExecuteBatch03
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a PreparedStatement object and call the addBatch()
   * method and call the executeBatch() method with a select Statement It should
   * throw BatchUpdateException
   *
   */
  public void testExecuteBatch03() throws Fault {
    boolean bexpflag = false;
    try {
      String sPrepStmt = sqlp.getProperty("CoffeeTab_Select", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pstmt.setInt(1, 1);
      pstmt.addBatch();

      try {
        msg.setMsg("execute the executeBatch method");
        int[] updateCount = pstmt.executeBatch();

      } catch (BatchUpdateException b) {
        TestUtil.printStackTrace(b);

        bexpflag = true;
      }
      if (bexpflag) {
        msg.setMsg(
            "executeBatch does not execute the Batch with a SQL select statement ");
      } else {
        msg.printTestError(
            "executeBatch executes the Batch with a SQL select statement ",
            "Call to executeBatch is Failed!");

      }
      msg.printTestMsg();

    } catch (BatchUpdateException b) {
      msg.printSQLError(b,
          "BatchUpdateException :  Call to executeBatch is Failed!");

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to executeBatch is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to executeBatch is Failed!");

    }
  }

  /*
   * @testName: testExecuteBatch04
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:JAVADOC:183; JDBC:JAVADOC:184; JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a Statement object and call the addBatch() method with
   * 3 valid SQL statements and call the executeBatch() method It should return
   * an array of Integer values of length 3
   */
  public void testExecuteBatch04() throws Fault {
    int i = 0;
    int retValue[] = { 0, 0, 0 };
    int updCountLength = 0;
    try {
      String sUpdCoffee = sqlp.getProperty("Upd_Coffee_Tab");
      String sInsCoffee = sqlp.getProperty("Ins_Coffee_Tab");
      String sDelCoffee = sqlp.getProperty("Del_Coffee_Tab");

      stmt.addBatch(sUpdCoffee);
      stmt.addBatch(sDelCoffee);
      stmt.addBatch(sInsCoffee);

      msg.setMsg("execute the executeBatch method");
      int[] updateCount = stmt.executeBatch();
      updCountLength = updateCount.length;

      msg.setMsg("Successfully Updated");
      msg.setMsg("updateCount Length :" + updCountLength);

      if (updCountLength != 3) {
        msg.printTestError(
            "executeBatch does not execute the Batch of SQL statements",
            "Call to executeBatch is Failed!");

      } else {
        msg.setMsg("executeBatch executes the Batch of SQL statements");
      }

      String sPrepStmt1 = sqlp.getProperty("BatchUpdate_Query");

      pstmt1 = conn.prepareStatement(sPrepStmt1);

      pstmt1.setInt(1, 1);
      rs = pstmt1.executeQuery();
      rs.next();
      retValue[i++] = rs.getInt(1);

      // 1 as Delete Statement will delete only one row
      retValue[i++] = 1;
      // 1 as Insert Statement will insert only one row
      retValue[i++] = 1;

      for (int j = 0; j < updateCount.length; j++) {
        msg.addOutputMsg("" + updateCount[j], "" + retValue[j]);

        if (updateCount[j] != retValue[j]) {
          msg.setMsg(
              "affected row count does not match with the updateCount value");

        }
      }
      msg.printTestMsg();

    } catch (BatchUpdateException b) {
      msg.printSQLError(b,
          "BatchUpdateException :  Call to executeBatch is Failed!");

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to executeBatch is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to executeBatch is Failed!");

    }
  }

  /*
   * @testName: testExecuteBatch05
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:JAVADOC:183; JDBC:JAVADOC:184;
   * 
   * @test_Strategy: Get a Statement object and call the executeBatch() method
   * without adding statements into a batch. It should return an array of
   * Integer value of zero length
   */
  public void testExecuteBatch05() throws Fault {
    int updCountLength = 0;
    try {
      String sUpdCoffee = sqlp.getProperty("Upd_Coffee_Tab");
      String sInsCoffee = sqlp.getProperty("Ins_Coffee_Tab");
      String sDelCoffee = sqlp.getProperty("Del_Coffee_Tab");

      int[] updateCount = stmt.executeBatch();
      updCountLength = updateCount.length;
      msg.setMsg("updateCount Length :" + updCountLength);

      if (updCountLength == 0) {
        msg.setMsg("executeBatch Method does not execute the Empty Batch ");
      } else {
        msg.printTestError("executeBatch Method executes the Empty Batch",
            "Call to executeBatch is Failed!");

      }
      msg.printTestMsg();

    } catch (BatchUpdateException b) {
      msg.printSQLError(b,
          "BatchUpdateException :  Call to executeBatch is Failed!");

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to executeBatch is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to executeBatch is Failed!");

    }
  }

  /*
   * @testName: testExecuteBatch06
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:JAVADOC:183; JDBC:JAVADOC:184; JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a Statement object and call the addBatch() method and
   * call the executeBatch() method with a violation in SQL constraints.It
   * should throw an BatchUpdateException
   */
  public void testExecuteBatch06() throws Fault {
    boolean bexpflag = false;
    try {
      // Insert a row which is already Present
      String sInsCoffee = sqlp.getProperty("Ins_Coffee_Tab");
      String sDelCoffee = sqlp.getProperty("Del_Coffee_Tab");
      String sUpdCoffee = sqlp.getProperty("Upd_Coffee_Tab");

      stmt.addBatch(sInsCoffee);
      stmt.addBatch(sInsCoffee);
      stmt.addBatch(sDelCoffee);

      try {
        int[] updateCount = stmt.executeBatch();
      } catch (BatchUpdateException b) {
        TestUtil.printStackTrace(b);

        bexpflag = true;
        int[] updCounts = b.getUpdateCounts();
        for (int i = 0; i < updCounts.length; i++) {
          msg.setMsg("Update counts of Successful Commands : " + updCounts[i]);
        }

      }
      if (bexpflag) {
        msg.setMsg(
            "executeBatch does not execute the SQL statement with a violation SQL constraint");
      } else {
        msg.printTestError(
            "executeBatch executes the SQL statement with a violation Constraints",
            "Call to executeBatch is Failed!");

      }
      msg.printTestMsg();

    } catch (BatchUpdateException b) {
      msg.printSQLError(b,
          "BatchUpdateException :  Call to executeBatch is Failed!");

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to executeBatch is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to executeBatch is Failed!");

    }
  }

  /*
   * @testName: testExecuteBatch07
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:JAVADOC:183; JDBC:JAVADOC:184; JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a Statement object and call the addBatch() method and
   * call the executeBatch() method with a select Statement It should throw an
   * BatchUpdateException
   */
  public void testExecuteBatch07() throws Fault {
    boolean bexpflag = false;
    try {

      String sDelCoffee = sqlp.getProperty("Del_Coffee_Tab");
      String sUpdCoffee = sqlp.getProperty("Upd_Coffee_Tab");
      String sSelCoffee = sqlp.getProperty("Sel_Coffee_Tab");

      msg.setMsg("sSelCoffee = " + sSelCoffee);
      Statement stmt = conn.createStatement();
      stmt.addBatch(sSelCoffee);

      try {
        int[] updateCount = stmt.executeBatch();
        msg.setMsg("updateCount Length : " + updateCount.length);
      } catch (BatchUpdateException be) {
        TestUtil.printStackTrace(be);

        bexpflag = true;
      }
      if (bexpflag) {
        msg.setMsg(
            "executeBatch does not execute the Batch with a SQL select statement ");
      } else {
        msg.printTestError(
            "executeBatch executes the Batch with a SQL select statement ",
            "Call to executeBatch is Failed");

      }
      msg.printTestMsg();

    } catch (BatchUpdateException b) {
      msg.printSQLError(b,
          "BatchUpdateException :  Call to executeBatch is Failed!");

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to executeBatch is Failed");

    } catch (Exception e) {
      msg.printError(e, "Call to executeBatch is Failed");

    }
  }

  /*
   * @testName: testExecuteBatch08
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a CallableStatement object and call the addBatch()
   * method with 3 valid SQL statements and call the executeBatch() method It
   * should return an array of Integer Values of length 3.
   */
  public void testExecuteBatch08() throws Fault {
    int i = 0;
    int retValue[] = { 0, 0, 0 };
    int updCountLength = 0;
    try {
      msg.setMsg("get the CallableStatement object");
      CallableStatement cstmt = conn.prepareCall("{call UpdCoffee_Proc(?)}");

      cstmt.setInt(1, 4);
      cstmt.addBatch();

      cstmt.setInt(1, 3);
      cstmt.addBatch();

      cstmt.setInt(1, 1);
      cstmt.addBatch();

      int[] updateCount = cstmt.executeBatch();
      updCountLength = updateCount.length;

      msg.setMsg("Successfully Updated");
      msg.setMsg("updateCount Length :" + updCountLength);

      if (updCountLength != 3) {
        msg.printTestError(
            "executeBatch does not execute the Batch of SQL statements",
            "Call to executeBatch is Failed");

      } else {
        msg.setMsg("executeBatch executes the Batch of SQL statements");
      }

      String sPrepStmt1 = sqlp.getProperty("BatchUpdate_Query");

      pstmt1 = conn.prepareStatement(sPrepStmt1);

      // 2 is the number that is set First for Type Id in Prepared Statement
      for (int n = 1; n <= 3; n++) {
        pstmt1.setInt(1, n);
        rs = pstmt1.executeQuery();
        rs.next();
        retValue[i++] = rs.getInt(1);
      }

      pstmt1.close();

      for (int j = 0; j < updateCount.length; j++) {

        msg.addOutputMsg("" + updateCount[j], "" + retValue[j]);
        if (updateCount[j] != retValue[j]) {
          msg.setMsg(
              "affected row count does not match with the updateCount value");
        }
      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (BatchUpdateException b) {
      msg.printSQLError(b,
          "BatchUpdateException :  Call to executeBatch is Failed!");

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to executeBatch is Failed");

    } catch (Exception e) {
      msg.printError(e, "Call to executeBatch is Failed");

    }
  }

  /*
   * @testName: testExecuteBatch09
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * 
   * @test_Strategy: Get a CallableStatement object and call the executeBatch()
   * method without adding the statements into Batch. It should return an array
   * of Integer Value of zero length
   */
  public void testExecuteBatch09() throws Fault {
    int updCountLength = 0;
    try {
      // get the CallableStatement object
      CallableStatement cstmt = conn.prepareCall("{call UpdCoffee_Proc(?)}");
      cstmt.setInt(1, 1);
      cstmt.setInt(1, 2);
      cstmt.setInt(1, 3);

      int[] updateCount = cstmt.executeBatch();
      updCountLength = updateCount.length;

      msg.setMsg("updateCount Length :" + updCountLength);
      if (updCountLength == 0) {
        msg.setMsg("executeBatch Method does not execute the Empty Batch");
      } else {
        msg.printTestError("executeBatch Method executes the Empty Batch",
            "Call to executeBatch is Failed!");

      }
      msg.printTestMsg();

    } catch (BatchUpdateException b) {
      msg.printSQLError(b,
          "BatchUpdateException :  Call to executeBatch is Failed!");

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to executeBatch is Failed");

    } catch (Exception e) {
      msg.printError(e, "Call to executeBatch is Failed");

    }
  }

  /*
   * @testName: testExecuteBatch12
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a CallableStatement object with different SQL
   * statements in the stored Procedure and call the addBatch() method with 3
   * statements and call the executeBatch() method It should return an array of
   * Integer Values of length 3.
   */
  public void testExecuteBatch12() throws Fault {

    try {

      msg.setMsg("get the CallableStatement object");
      CallableStatement cstmt = conn.prepareCall("{call Coffee_Proc(?)}");
      cstmt.setInt(1, 2);
      cstmt.addBatch();
      cstmt.setInt(1, 3);
      cstmt.addBatch();
      cstmt.setInt(1, 5);
      cstmt.addBatch();
      int[] updateCount = cstmt.executeBatch();
      int updCountLength = updateCount.length;

      msg.setMsg("updateCountLength : " + updCountLength);

      if (updCountLength != 3) {
        msg.printTestError(
            "executeBatch does not execute the Batch of SQL statements",
            "Call to executeBatch is Failed");

      } else {
        msg.setMsg("executeBatch executes the Batch of SQL statements");
      }

      for (int i = 0; i < updCountLength; i++) {
        msg.setMsg("UpdateCount Value : " + updateCount[i]);
      }
      msg.printTestMsg();

    }

    catch (BatchUpdateException b) {
      msg.printSQLError(b,
          "BatchUpdateException :  Call to executeBatch is Failed!");

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to executeBatch is Failed");

    }

    catch (Exception e) {
      msg.printError(e, "Call to executeBatch is Failed");

    }
  }

  /*
   * @testName: testContinueBatch01
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a PreparedStatement object and call the addBatch()
   * method with 3 SQL statements.Among these 3 SQL statements first is
   * valid,second is invalid and third is again valid. Then call the
   * executeBatch() method and it should return array of Integer values of
   * length 3, if it supports continued updates. Then check whether the third
   * command in the batch after the invalid command executed properly.
   */
  public void testContinueBatch01() throws Fault {
    int batchUpdates[] = { 0, 0, 0 };
    int updateCount[] = { 0, 0, 0 };
    int buCountlen = 0;
    int updateCountlen = 0;

    try {
      String sPrepStmt = sqlp.getProperty("CoffeeTab_Continue1", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);

      // Now add a legal update to the batch
      pstmt.setInt(1, 1);
      pstmt.setString(2, "Continue-1");
      pstmt.setString(3, "COFFEE-1");
      pstmt.addBatch();

      // Now add an illegal update to the batch by
      // forcing a unique constraint violation
      // Try changing the key_id of row 3 to 1.
      pstmt.setInt(1, 1);
      pstmt.setString(2, "Invalid");
      pstmt.setString(3, "COFFEE-3");
      pstmt.addBatch();

      // Now add a second legal update to the batch
      // which will be processed ONLY if the driver supports
      // continued batch processing.
      pstmt.setInt(1, 2);
      pstmt.setString(2, "Continue-2");
      pstmt.setString(3, "COFFEE-2");
      pstmt.addBatch();

      // The executeBatch() method will result in a
      // BatchUpdateException
      msg.setMsg("execute the method executeBatch");
      updateCount = pstmt.executeBatch();
    } catch (BatchUpdateException b) {
      TestUtil.printStackTrace(b);

      msg.setMsg("Caught expected BatchUpdateException");
      batchUpdates = b.getUpdateCounts();
      buCountlen = batchUpdates.length;
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to continueUpdate failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to continueUpdate failed!");

    }

    if (buCountlen == 1) {
      msg.setMsg("Driver does not support continued updates - OK");
      return;
    } else if (buCountlen == 3) {
      msg.setMsg("Driver supports continued updates.");
      // Check to see if the third row from the batch was added
      try {
        String query = sqlp.getProperty("CoffeeTab_ContinueSelect1", "");
        msg.setMsg("Query is: " + query);
        rs = stmt.executeQuery(query);
        rs.next();
        int count = rs.getInt(1);
        rs.close();
        stmt.close();
        msg.setMsg("Count val is: " + count);

        // Make sure that we have the correct error code for
        // the failed update.

        if (!(batchUpdates[1] == -3 && count == 1)) {
          throw new Fault("Driver did not insert after error.");
        }
        msg.printTestMsg();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to continueUpdate failed!");

      }
    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      pstmt.close();
      stmt.close();
      // conn.setAutoCommit(true);
      dbSch.destroyData(conn);
      // Close the database
      dbSch.dbUnConnect(conn);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }
}
