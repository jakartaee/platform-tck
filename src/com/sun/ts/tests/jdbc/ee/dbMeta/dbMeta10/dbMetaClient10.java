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
 * @(#)dbMetaClient10.java	1.33 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta10;

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
 * The dbMetaClient class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class dbMetaClient10 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.dbMeta.dbMeta10";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private transient DatabaseMetaData dbmd = null;

  private DataSource ds1 = null;

  private dbSchema dbSch = null;

  private String dbName = null, dbUser = null, drManager = null;

  private String sCatalogName = null, sSchemaName = null, sPtable = null,
      sFtable = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    dbMetaClient10 theTests = new dbMetaClient10();
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
        dbName = p.getProperty("db1", "");
        dbUser = p.getProperty("user1", "");
        sPtable = p.getProperty("ptable", "TSTABLE1");
        sFtable = p.getProperty("ftable", "TSTABLE2");
        if (dbName.length() == 0)
          throw new Fault("Invalid db1  Database Name");
        if (dbUser.length() == 0)
          throw new Fault("Invalid Login Id");
        if (sPtable.length() == 0)
          throw new Fault("Invalid Primary table");
        if (sFtable.length() == 0)
          throw new Fault("Invalid Foreign table");
        if (drManager.length() == 0)
          throw new Fault("Invalid DriverManager Name");

        int nLocdbname = dbName.indexOf('=');
        sSchemaName = dbUser;

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
   * @testName: testOthersUpdatesAreVisible2
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1086; JDBC:JAVADOC:1087;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the otherUpdatesAreVisible(int resType) method on that
   * object with ResultSet.TYPE_SCROLL_INSENSITIVE. It should return a boolean
   * value; either true or false.
   *
   */
  public void testOthersUpdatesAreVisible2() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.othersUpdatesAreVisible(TYPE_SCROLL_INSENSITIVE)");
      // invoke othersUpdatesAreVisible method
      boolean retValue = dbmd
          .othersUpdatesAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE);
      if (retValue)
        msg.setMsg(
            "Updates made by others are visible for TYPE_SCROLL_INSENSITIVE");
      else
        msg.setMsg(
            "Updates made by others are not visible for TYPE_SCROLL_INSENSITIVE");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to othersUpdatesAreVisible2 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to othersUpdatesAreVisible2 is Failed!");

    }
  }

  /*
   * @testName: testOthersUpdatesAreVisible3
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1086; JDBC:JAVADOC:1087;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the otherUpdatesAreVisible(int resType) method on that
   * object with ResultSet.TYPE_SCROLL_SENSITIVE. It should return a boolean
   * value; either true or false.
   *
   */
  public void testOthersUpdatesAreVisible3() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.othersUpdatesAreVisible(TYPE_SCROLL_SENSITIVE)");
      // invoke othersUpdatesAreVisible method
      boolean retValue = dbmd
          .othersUpdatesAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE);
      if (retValue)
        msg.setMsg(
            "Updates made by others are visible for TYPE_SCROLL_SENSITIVE");
      else
        msg.setMsg(
            "Updates made by others are not visible for TYPE_SCROLL_SENSITIVE");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to othersUpdatesAreVisible3 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to othersUpdatesAreVisible3 is Failed!");

    }
  }

  /*
   * @testName: testOthersDeletesAreVisible1
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1088; JDBC:JAVADOC:1089;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the otherDeletesAreVisible(int resType) method on that
   * object with ResultSet.TYPE_FORWARD_ONLY. It should return a boolean value;
   * either true or false.
   *
   */
  public void testOthersDeletesAreVisible1() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.othersDeletesAreVisible(TYPE_FORWARD_ONLY)");
      // invoke othersDeletesAreVisible method
      boolean retValue = dbmd
          .othersDeletesAreVisible(ResultSet.TYPE_FORWARD_ONLY);
      if (retValue)
        msg.setMsg("Deletes made by others are visible for TYPE_FORWARD_ONLY");
      else
        msg.setMsg(
            "Deletes made by others are not visible for TYPE_FORWARD_ONLY");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to othersDeletesAreVisible1 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to othersDeletesAreVisible1 is Failed!");

    }
  }

  /*
   * @testName: testOthersDeletesAreVisible2
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1088; JDBC:JAVADOC:1089;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the otherDeletesAreVisible(int resType) method on that
   * object with ResultSet.TYPE_SCROLL_INSENSITIVE. It should return a boolean
   * value; either true or false.
   *
   */
  public void testOthersDeletesAreVisible2() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.othersDeletesAreVisible(TYPE_SCROLL_INSENSITIVE)");
      // invoke othersDeletesAreVisible method
      boolean retValue = dbmd
          .othersDeletesAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE);
      if (retValue)
        msg.setMsg(
            "Deletes made by others are visible for TYPE_SCROLL_INSENSITIVE");
      else
        msg.setMsg(
            "Deletes made by others are not visible for TYPE_SCROLL_INSENSITIVE");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to othersDeletesAreVisible2 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to othersDeletesAreVisible2 is Failed!");

    }
  }

  /*
   * @testName: testOthersDeletesAreVisible3
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1088; JDBC:JAVADOC:1089;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the otherDeletesAreVisible(int resType) method on that
   * object with ResultSet.TYPE_SCROLL_SENSITIVE. It should return a boolean
   * value; either true or false.
   *
   */
  public void testOthersDeletesAreVisible3() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.othersDeletesAreVisible(TYPE_SCROLL_SENSITIVE)");
      // invoke othersDeletesAreVisible method
      boolean retValue = dbmd
          .othersDeletesAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE);
      if (retValue)
        msg.setMsg(
            "Deletes made by others are visible for TYPE_SCROLL_SENSITIVE");
      else
        msg.setMsg(
            "Deletes made by others are not visible for TYPE_SCROLL_SENSITIVE");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to othersDeletesAreVisible3 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to othersDeletesAreVisible3 is Failed!");

    }
  }

  /*
   * @testName: testOthersInsertsAreVisible1
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1090; JDBC:JAVADOC:1091;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the otherInsertsAreVisible(int resType) method on that
   * object with ResultSet.TYPE_FORWARD_ONLY. It should return a boolean value;
   * either true or false.
   *
   */
  public void testOthersInsertsAreVisible1() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.othersInsertsAreVisible(TYPE_FORWARD_ONLY)");
      // invoke othersInsertsAreVisible method
      boolean retValue = dbmd
          .othersInsertsAreVisible(ResultSet.TYPE_FORWARD_ONLY);
      if (retValue)
        msg.setMsg("Inserts made by others are visible for TYPE_FORWARD_ONLY");
      else
        msg.setMsg(
            "Inserts made by others are not visible for TYPE_FORWARD_ONLY");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to othersInsertsAreVisible1 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to othersInsertsAreVisible1 is Failed!");

    }
  }

  /*
   * @testName: testOthersInsertsAreVisible2
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1090; JDBC:JAVADOC:1091;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the otherInsertsAreVisible(int resType) method on that
   * object with ResultSet.TYPE_SCROLL_INSENSITIVE. It should return a boolean
   * value; either true or false.
   *
   */
  public void testOthersInsertsAreVisible2() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.othersInsertsAreVisible(TYPE_SCROLL_INSENSITIVE)");
      // invoke othersInsertsAreVisible method
      boolean retValue = dbmd
          .othersInsertsAreVisible(ResultSet.TYPE_SCROLL_INSENSITIVE);
      if (retValue)
        msg.setMsg(
            "Inserts made by others are visible for TYPE_SCROLL_INSENSITIVE");
      else
        msg.setMsg(
            "Inserts made by others are not visible for TYPE_SCROLL_INSENSITIVE");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to othersInsertsAreVisible2 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to othersInsertsAreVisible2 is Failed!");

    }
  }

  /*
   * @testName: testOthersInsertsAreVisible3
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1090; JDBC:JAVADOC:1091;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the otherInsertsAreVisible(int resType) method on that
   * object with ResultSet.TYPE_SCROLL_SENSITIVE. It should return a boolean
   * value; either true or false.
   *
   */
  public void testOthersInsertsAreVisible3() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.othersInsertsAreVisible(TYPE_SCROLL_SENSITIVE)");
      // invoke othersInsertsAreVisible method
      boolean retValue = dbmd
          .othersInsertsAreVisible(ResultSet.TYPE_SCROLL_SENSITIVE);
      if (retValue)
        msg.setMsg(
            "Inserts made by others are visible for TYPE_SCROLL_SENSITIVE");
      else
        msg.setMsg(
            "Inserts made by others are not visible for TYPE_SCROLL_SENSITIVE");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to othersInsertsAreVisible3 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to othersInsertsAreVisible3 is Failed!");

    }
  }

  /*
   * @testName: testUpdatesAreDetected1
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1092; JDBC:JAVADOC:1093;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the updatesAreDetected() method on that object with the
   * ResultSet Type as ResultSet.TYPE_FORWARD_ONLY. It should return a boolean
   * value; either true or false.
   *
   */
  public void testUpdatesAreDetected1() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.updatesAreDetected(TYPE_FORWARD_ONLY)");
      // invoke updatesAreDetected method
      boolean retValue = dbmd.updatesAreDetected(ResultSet.TYPE_FORWARD_ONLY);
      if (retValue)
        msg.setMsg("Visible row update can be detected for TYPE_FORWARD_ONLY");
      else
        msg.setMsg(
            "Visible row update cannot be detected for TYPE_FORWARD_ONLY");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to updatesAreDetected1 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to updatesAreDetected1 is Failed!");

    }
  }

  /*
   * @testName: testUpdatesAreDetected2
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1092; JDBC:JAVADOC:1093;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the updatesAreDetected() method on that object with the
   * ResultSet Type as ResultSet.TYPE_SCROLL_INSENSITIVE. It should return a
   * boolean value; either true or false.
   *
   */
  public void testUpdatesAreDetected2() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.updatesAreDetected(TYPE_SCROLL_INSENSITIVE)");
      // invoke updatesAreDetected method
      boolean retValue = dbmd
          .updatesAreDetected(ResultSet.TYPE_SCROLL_INSENSITIVE);
      if (retValue)
        msg.setMsg(
            "Visible row update can be detected for TYPE_SCROLL_INSENSITIVE");
      else
        msg.setMsg(
            "Visible row update cannot be detected for TYPE_SCROLL_INSENSITIVE");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to updatesAreDetected2 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to updatesAreDetected2 is Failed!");

    }
  }

  /*
   * @testName: testUpdatesAreDetected3
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1092; JDBC:JAVADOC:1093;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the updatesAreDetected() method on that object with the
   * ResultSet Type as ResultSet.TYPE_SCROLL_SENSITIVE. It should return a
   * boolean value; either true or false.
   *
   */
  public void testUpdatesAreDetected3() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.updatesAreDetected(TYPE_SCROLL_SENSITIVE)");
      // invoke updatesAreDetected method
      boolean retValue = dbmd
          .updatesAreDetected(ResultSet.TYPE_SCROLL_SENSITIVE);
      if (retValue)
        msg.setMsg(
            "Visible row update can be detected for TYPE_SCROLL_SENSITIVE");
      else
        msg.setMsg(
            "Visible row update cannot be detected for TYPE_SCROLL_SENSITIVE");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to updatesAreDetected3 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to updatesAreDetected3 is Failed!");

    }
  }

  /*
   * @testName: testDeletesAreDetected1
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1094; JDBC:JAVADOC:1095;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the deletesAreDetected() method on that object with the
   * result set type as ResultSet.TYPE_FORWARD_ONLY. It should return a boolean
   * value; either true or false.
   *
   */
  public void testDeletesAreDetected1() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.deletesAreDetected(TYPE_FORWARD_ONLY)");
      // invoke deletesAreDetected method
      boolean retValue = dbmd.deletesAreDetected(ResultSet.TYPE_FORWARD_ONLY);
      if (retValue)
        msg.setMsg("Visible row delete can be detected for TYPE_FORWARD_ONLY");
      else
        msg.setMsg(
            "Visible row delete cannot be detected for TYPE_FORWARD_ONLY");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to deletesAreDetected1 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to deletesAreDetected1 is Failed!");

    }
  }

  /*
   * @testName: testDeletesAreDetected2
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1094; JDBC:JAVADOC:1095;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the deletesAreDetected() method on that object with the
   * result set type as ResultSet.TYPE_SCROLL_INSENSITIVE. It should return a
   * boolean value; either true or false.
   *
   */
  public void testDeletesAreDetected2() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.deletesAreDetected(TYPE_SCROLL_INSENSITIVE)");
      // invoke deletesAreDetected method
      boolean retValue = dbmd
          .deletesAreDetected(ResultSet.TYPE_SCROLL_INSENSITIVE);
      if (retValue)
        msg.setMsg(
            "Visible row delete can be detected for TYPE_SCROLL_INSENSITIVE");
      else
        msg.setMsg(
            "Visible row delete cannot be detected for TYPE_SCROLL_INSENSITIVE");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to deletesAreDetected2 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to deletesAreDetected2 is Failed!");

    }
  }

  /*
   * @testName: testDeletesAreDetected3
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1094; JDBC:JAVADOC:1095;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the deletesAreDetected() method on that object with the
   * result set type as ResultSet.TYPE_SCROLL_SENSITIVE. It should return a
   * boolean value; either true or false.
   *
   */
  public void testDeletesAreDetected3() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.deletesAreDetected(TYPE_SCROLL_SENSITIVE)");
      // invoke deletesAreDetected method
      boolean retValue = dbmd
          .deletesAreDetected(ResultSet.TYPE_SCROLL_SENSITIVE);
      if (retValue)
        msg.setMsg(
            "Visible row delete can be detected for TYPE_SCROLL_SENSITIVE");
      else
        msg.setMsg(
            "Visible row delete cannot be detected for TYPE_SCROLL_SENSITIVE");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to deletesAreDetected3 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to deletesAreDetected3 is Failed!");

    }
  }

  /*
   * @testName: testInsertsAreDetected1
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1096; JDBC:JAVADOC:1097;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the insertsAreDetected() method on that object with the
   * result set type as ResultSet.TYPE_FORWARD_ONLY. It should return a boolean
   * value; either or false.
   *
   */
  public void testInsertsAreDetected1() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.insertsAreDetected(TYPE_FORWARD_ONLY)");
      // invoke insertsAreDetected method
      boolean retValue = dbmd.insertsAreDetected(ResultSet.TYPE_FORWARD_ONLY);
      if (retValue)
        msg.setMsg("Visible row insert can be detected for TYPE_FORWARD_ONLY");
      else
        msg.setMsg(
            "Visible row insert cannot be detected for TYPE_FORWARD_ONLY");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to insertsAreDetected1 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to insertsAreDetected1 is Failed!");

    }
  }

  /*
   * @testName: testInsertsAreDetected2
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1096; JDBC:JAVADOC:1097;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the insertsAreDetected() method on that object with the
   * result set type as ResultSet.TYPE_SCROLL_INSENSITIVE. It should return a
   * boolean value; either or false.
   *
   */
  public void testInsertsAreDetected2() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.insertsAreDetected(TYPE_SCROLL_INSENSITIVE)");
      // invoke insertsAreDetected method
      boolean retValue = dbmd
          .insertsAreDetected(ResultSet.TYPE_SCROLL_INSENSITIVE);
      if (retValue)
        msg.setMsg(
            "Visible row insert can be detected for TYPE_SCROLL_INSENSITIVE");
      else
        msg.setMsg(
            "Visible row insert cannot be detected for TYPE_SCROLL_INSENSITIVE");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to insertsAreDetected2 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to insertsAreDetected2 is Failed!");

    }
  }

  /*
   * @testName: testInsertsAreDetected3
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1096; JDBC:JAVADOC:1097;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the insertsAreDetected() method on that object with the
   * result set type as ResultSet.TYPE_SCROLL_SENSITIVE. It should return a
   * boolean value; either or false.
   *
   */
  public void testInsertsAreDetected3() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.insertsAreDetected(TYPE_SCROLL_SENSITIVE)");
      // invoke insertsAreDetected method
      boolean retValue = dbmd
          .insertsAreDetected(ResultSet.TYPE_SCROLL_SENSITIVE);
      if (retValue)
        msg.setMsg(
            "Visible row insert can be detected for TYPE_SCROLL_SENSITIVE");
      else
        msg.setMsg(
            "Visible row insert cannot be detected for TYPE_SCROLL_SENSITIVE");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to insertsAreDetected3 is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to insertsAreDetected3 is Failed!");

    }
  }

  /*
   * @testName: testGetUDTs
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1100; JDBC:JAVADOC:1101;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getUDTs() method on that object. It should return a
   * ResultSet object. Validate the column names and column ordering.
   */
  public void testGetUDTs() throws Fault {
    try {
      msg.setMsg("Calling DatabaseMetaData.getUDTs");
      // invoke getUDTs method
      ResultSet oRet_ResultSet = dbmd.getUDTs(sCatalogName, sSchemaName, "%",
          null);
      String sRetStr = new String();
      sRetStr = "";
      msg.setMsg("Store all the type names returned");
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(3) + ",";
      if (sRetStr == "")
        msg.setMsg("getUDTs did not return any user defined types");
      else
        msg.setMsg("The type names returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getUDTs is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getUDTs is Failed!");

    }
  }

  /*
   * @testName: testGetUDTs01
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1100; JDBC:JAVADOC:1101;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getUDTs() method on that object. It should return a
   * ResultSet object. Validate the column names and column ordering.
   *
   */
  public void testGetUDTs01() throws Fault {
    try {
      ResultSetMetaData rsmd = null;
      String sColumnNames[] = { "TYPE_CAT", "TYPE_SCHEM", "TYPE_NAME",
          "CLASS_NAME", "DATA_TYPE", "REMARKS" };
      boolean statusColumnMatch = true;
      boolean statusColumnCount = true;

      String sRetStr = new String();
      sRetStr = "";

      int iColumnNamesLength = sColumnNames.length;
      msg.setMsg("Calling DatabaseMetaData.getUDTs");

      msg.setMsg("invoke getUDTs method");
      ResultSet oRet_ResultSet = dbmd.getUDTs(sCatalogName, sSchemaName, "%",
          null);
      rsmd = oRet_ResultSet.getMetaData();

      int iCount = rsmd.getColumnCount();

      msg.setMsg("Minimum Column Count is:" + iColumnNamesLength);

      msg.setMsg("Comparing Column Lengths");
      if (iColumnNamesLength > iCount)
        statusColumnCount = false;
      else if (iColumnNamesLength < iCount) {
        iCount = iColumnNamesLength;
        statusColumnCount = true;
      } else
        statusColumnCount = true;

      msg.setMsg("Comparing Column Names...");

      while (iColumnNamesLength > 0) {
        if (sColumnNames[iColumnNamesLength - 1]
            .equalsIgnoreCase(rsmd.getColumnName(iCount))) {
          statusColumnMatch = true;
        } else {
          statusColumnMatch = false;
          break;
        }
        iCount--;
        iColumnNamesLength--;
      }

      if ((statusColumnMatch == false) && (statusColumnCount == true)) {
        msg.printTestError("Column names or order wrong.",
            "Call to getUDTs Failed!");

      }

      msg.setMsg("Store all the type names returned");
      while (oRet_ResultSet.next())
        sRetStr += oRet_ResultSet.getString(3) + ",";
      if (sRetStr == "")
        msg.setMsg("getUDTs did not return any user defined types");
      else
        msg.setMsg("The type names returned Are : "
            + sRetStr.substring(0, sRetStr.length() - 1));

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getUDTs Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getUDTs Failed!");

    }
  }

  /*
   * @testName: testSupportsTransactionIsolationLevel1
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1032; JDBC:JAVADOC:1033;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsTransactionIsolationLevel(int isolevel)
   * method on that object with the isolation level TRANSACTION_NONE. It should
   * return a boolean value; either true or false.
   *
   */
  public void testSupportsTransactionIsolationLevel1() throws Fault {
    try {
      msg.setMsg(
          "Calling DatabaseMetaData.supportsTransactionIsolationLevel(TRANSACTION_NONE)");
      // invoke supportsTransactionIsolationLevel method
      boolean retValue = dbmd
          .supportsTransactionIsolationLevel(Connection.TRANSACTION_NONE);
      if (retValue)
        msg.setMsg(
            "supportsTransactionIsolationLevel(TRANSACTION_NONE) is supported");
      else
        msg.setMsg(
            "supportsTransactionIsolationLevel(TRANSACTION_NONE) is not supported");

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle,
          "Call to supportsTransactionIsolationLevel1 is Failed!");

    } catch (Exception e) {
      msg.printError(e,
          "Call to supportsTransactionIsolationLevel1 is Failed!");

    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      // Close the database
      dbSch.destroyData(conn);
      dbSch.dbUnConnect(conn);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }
}
