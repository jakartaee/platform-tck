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
 * @(#)testConnClient.java	1.13 03/05/16
 */

package com.sun.ts.tests.samples.jdbc.ee.testConn;

import java.io.*;
import java.util.*;

import java.sql.*;
import javax.sql.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.rmi.RemoteException;

import com.sun.javatest.Status;
//import com.sun.ts.tests.jdbc.ee.common.*;

public class testConnClient extends ServiceEETest implements Serializable {

  // private static final String testName = "samples.jdbc.ee.testConn";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements
  private transient Connection conn = null;

  private DataSource ds1 = null;

  private Properties props = null;

  // Tables to be created
  private String pTableName = null;

  private String fTableName = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    testConnClient theTests = new testConnClient();
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
   *
   */

  public void setup(String[] args, Properties p) throws Fault {
    try {
      try {
        props = new Properties(p);
        pTableName = p.getProperty("ptable");
        fTableName = p.getProperty("ftable");

        TestUtil.logTrace("Getting the initial context...");
        jc = new TSNamingContext();
        TestUtil.logTrace("Initial context: " + jc.toString());

        TestUtil.logTrace("Looking up the JNDI DataSource names");
        ds1 = (DataSource) jc.lookup("java:comp/env/jdbc/DB1");
        TestUtil.logTrace("ds1: " + ds1.toString());

        getConnection();

      } catch (SQLException ex) {
        logErr("SQL Exception : " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testCreateTable
   * 
   * @assertion: To verify this JDBC connection can create tables.
   *
   * @test_Strategy: Using the Connection object created in the setup() method,
   * create a table, insert rows into it, then drop it.
   * 
   * This sample will ensure that your environment is suitable to run the JDBC
   * tests.
   *
   */
  public void testCreateTable() throws Fault {
    try {
      createTable(props, conn);
    } catch (RemoteException re) {
      TestUtil.logMsg("Remote exception on createTable: " + re.getMessage());
      throw new Fault("Call to testCreateTable Failed.", re);
    }
  }

  private void createTable(Properties testProps, Connection conn)
      throws RemoteException {
    TestUtil.logTrace("createTable");
    // drop the table if it exists
    try {
      dropTables(conn);
      TestUtil.logTrace(
          "Deleted all rows from Tables " + pTableName + " and " + fTableName);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);

      TestUtil.logTrace("SQLException while deleting rows from Tables "
          + pTableName + " and " + fTableName + ":" + e.getMessage());
    }

    // Get the size of the table as int
    String strTabSize = testProps.getProperty("cofSize");
    TestUtil.logTrace("strTabSize: " + strTabSize);

    Integer intTabSize = new Integer(strTabSize);
    TestUtil.logTrace("intTabSize: " + intTabSize.toString());
    logMsg("intTabSize: " + intTabSize.toString());

    int tSize = intTabSize.intValue();
    TestUtil.logTrace("tSize: " + tSize);

    String strTabTypeSize = testProps.getProperty("cofTypeSize");
    TestUtil.logTrace("strTabTypeSize: " + strTabTypeSize);

    Integer intTabTypeSize = new Integer(strTabTypeSize);
    TestUtil.logTrace("intTabTypeSize: " + intTabTypeSize.toString());
    logMsg("intTabTypeSize: " + intTabTypeSize.toString());

    int tTypeSize = intTabTypeSize.intValue();
    TestUtil.logTrace("tTypeSize: " + tTypeSize);

    try {

      // Add the prescribed table rows
      TestUtil.logTrace("Adding the " + pTableName + " table rows");
      String updateString1 = testProps.getProperty("Dbschema_Tab2_Insert", "");
      PreparedStatement pStmt1 = conn.prepareStatement(updateString1);
      for (int j = 1; j <= tTypeSize; j++) {
        String sTypeDesc = "Type-" + j;
        int newType = j;
        pStmt1.setInt(1, newType);
        pStmt1.setString(2, sTypeDesc);
        pStmt1.executeUpdate();
      }

      // Add the prescribed table rows
      TestUtil.logTrace("Adding the " + fTableName + " table rows");
      String updateString = testProps.getProperty("Dbschema_Tab1_Insert", "");
      PreparedStatement pStmt = conn.prepareStatement(updateString);
      for (int i = 1; i <= tSize; i++) {
        // Perform the insert(s)
        int newKey = i;
        String newName = fTableName + "-" + i;
        float newPrice = i + (float) .00;
        int newType = i % 5;
        if (newType == 0)
          newType = 5;
        pStmt.setInt(1, newKey);
        pStmt.setString(2, newName);
        pStmt.setFloat(3, newPrice);
        pStmt.setInt(4, newType);
        pStmt.executeUpdate();
      }

      pStmt.close();
      pStmt1.close();
    } catch (SQLException e) {
      TestUtil.logErr("SQLException inserting " + fTableName + " or "
          + pTableName + " table", e);
      TestUtil.logErr(e.getMessage());
      dropTables(conn);
      throw new RemoteException(e.getMessage());
    }
  }

  private void dropTables(Connection conn) throws RemoteException {
    TestUtil.logTrace("dropTables");
    // Delete the fTable
    String removeString = props.getProperty("Dbschema_Tab1_Delete", "");
    String removeString1 = props.getProperty("Dbschema_Tab2_Delete", "");
    try {
      Statement stmt = conn.createStatement();
      stmt.executeUpdate(removeString);
      stmt.close();
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);

      throw new RemoteException(e.getMessage());
    } finally {
      try {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(removeString1);
        stmt.close();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);

        throw new RemoteException(e.getMessage());
      }
    }

  }

  private void getConnection()
      throws ClassNotFoundException, SQLException, Exception {

    TestUtil.logTrace("Attempting to make the connection");
    conn = ds1.getConnection();
    TestUtil.logMsg(
        "Made the connection to: " + ds1.toString() + " via DataSource");
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      dropTables(conn);
      logMsg("Dropped tables OK");
      conn.close();
      logMsg("Closed connection OK");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
      logErr(e.getMessage());
    }
  }

}
