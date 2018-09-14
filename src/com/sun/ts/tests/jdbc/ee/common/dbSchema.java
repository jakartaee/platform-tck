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
 * @(#)dbSchema.java	1.22 03/05/16
 */
package com.sun.ts.tests.jdbc.ee.common;

import java.util.*;
import java.sql.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;

import java.rmi.RemoteException;

/**
 * The dbSchema class creates the database and tables using Sun's J2EE Reference
 * Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class dbSchema extends ServiceEETest {
  private String pTableName = null;

  private String fTableName = null;

  // used only in dropTable() as properties are not passed as a parameter.
  private Properties props = null;

  public void createData(Properties p, Connection conn) throws RemoteException {
    TestUtil.logTrace("createData");

    // Get the name of the tables
    pTableName = p.getProperty("ptable", "");
    fTableName = p.getProperty("ftable", "");
    try {
      // Create the database tables
      props = p;
      createTable(p, conn);
      TestUtil.logTrace(
          "Initialized the tables " + pTableName + " and " + fTableName);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);

      dbUnConnect(conn);
      throw new RemoteException(e.getMessage());
    }
  }

  public void destroyData(Connection conn) throws RemoteException {
    TestUtil.logTrace("destroyData");
    try {
      dropTables(conn);
      TestUtil.logTrace(
          "Deleted all rows from tables " + pTableName + " and " + fTableName);
    } catch (Exception e) {
      TestUtil
          .logErr("Exception while attempting to Delete all rows from tables "
              + pTableName + " and " + fTableName, e);
      throw new RemoteException(e.getMessage());
    }
  }

  public void dbUnConnect(Connection conn) throws RemoteException {
    TestUtil.logTrace("dbUnConnect");
    // Close the DB connections
    try {
      conn.close();
      TestUtil.logMsg("Closed the database connection");
    } catch (Exception e) {
      TestUtil.logErr(
          "Exception occured while trying to close the DB connection", e);
      throw new RemoteException(e.getMessage());
    }
  }

  private void createTable(Properties testProps, Connection conn)
      throws RemoteException {
    TestUtil.logTrace("createTable");

    // Remove all rows from the table
    try {
      dropTables(conn);
      TestUtil.logTrace("Deleted all rows from Tables " + pTableName + " and "
          + fTableName + " dropped");
    } catch (Exception e) {
      TestUtil.logErr("SQLException encountered while deleting rows for Tables "
          + pTableName + " and " + fTableName, e);
    }

    // Get the size of the table as int
    String strTabSize = testProps.getProperty("cofSize");
    TestUtil.logTrace("strTabSize: " + strTabSize);

    Integer intTabSize = new Integer(strTabSize);
    TestUtil.logTrace("intTabSize: " + intTabSize.toString());
    logTrace("intTabSize: " + intTabSize.toString());

    int tSize = intTabSize.intValue();
    TestUtil.logTrace("tSize: " + tSize);

    String strTabTypeSize = testProps.getProperty("cofTypeSize");
    TestUtil.logTrace("strTabTypeSize: " + strTabTypeSize);

    Integer intTabTypeSize = new Integer(strTabTypeSize);
    TestUtil.logTrace("intTabTypeSize: " + intTabTypeSize.toString());
    logTrace("intTabTypeSize: " + intTabTypeSize.toString());

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
      TestUtil.logErr("SQLException creating " + fTableName + " or "
          + pTableName + " table", e);
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
}
