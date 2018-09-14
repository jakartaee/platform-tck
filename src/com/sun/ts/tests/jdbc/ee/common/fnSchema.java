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
 * @(#)fnSchema.java	1.16 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.common;

import java.io.*;
import java.util.*;
import java.sql.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;

import java.rmi.RemoteException;

/**
 * The fnSchema class creates the database using Sun's J2EE Reference
 * Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class fnSchema extends ServiceEETest {

  public void createTable(Properties props, Connection conn)
      throws RemoteException {
    String insertString1 = null;
    String insertString2 = null;

    TestUtil.logTrace("createTable");

    // drop Table if it exists
    try {
      dropTable(props, conn);
    } catch (Exception e) {
      TestUtil.logErr(
          "SQLException encountered while deleting rows from tables", e);
    }

    try {
      Statement stmt = conn.createStatement();

      insertString1 = props.getProperty("Fnschema_Tab1_Insert", "");

      TestUtil.logTrace("insertString1: " + insertString1);
      TestUtil.logTrace("Executed: '" + insertString1 + "' successfully");
      stmt.executeUpdate(insertString1);

      insertString2 = props.getProperty("Fnschema_Tab2_Insert", "");

      TestUtil.logTrace("insertString2: " + insertString2);
      TestUtil.logTrace("Executed: '" + insertString2 + "' successfully");
      stmt.executeUpdate(insertString2);

      stmt.close();
    } catch (SQLException e) {
      TestUtil.logErr("SQLException while inserting data", e);
      dropTable(props, conn);
      throw new RemoteException(e.getMessage());
    }
  }

  public void dropTable(Properties props, Connection conn)
      throws RemoteException {
    TestUtil.logTrace("dropTable");
    // Delete the Tables
    String dropString1 = props.getProperty("Fnschema_Tab1_Delete", "");
    String dropString2 = props.getProperty("Fnschema_Tab2_Delete", "");
    try {
      // Since the scrollable resultSet is optional, the
      // parameters are commented out.
      Statement stmt = conn.createStatement(/*
                                             * ResultSet.TYPE_SCROLL_INSENSITIVE
                                             * ,ResultSet.CONCUR_READ_ONLY
                                             */);
      stmt.executeUpdate(dropString1);
      TestUtil.logTrace("Executed: '" + dropString1 + "' successfully");
      stmt.executeUpdate(dropString2);
      TestUtil.logTrace("Executed: '" + dropString2 + "' successfully");
      stmt.close();
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);

      throw new RemoteException(e.getMessage());
    }
  }

  public void dbUnConnect(Connection conn) throws RemoteException {
    TestUtil.logTrace("dbUnConnect");
    // Close the DB connections
    try {
      conn.close();
      TestUtil.logMsg("Closed the Database connection");
    } catch (Exception e) {
      TestUtil.logErr(
          "Exception occured while trying to close the DB connection", e);
      throw new RemoteException(e.getMessage());
    }
  }

  public boolean isStringFuncFound(String funcName, Connection conn)
      throws RemoteException {
    TestUtil.logTrace("isStringFuncFound");
    // Check if the String Function is supported by the DBMS

    try {
      DatabaseMetaData dbmd = null;
      dbmd = conn.getMetaData();
      String strFuncList = dbmd.getStringFunctions();
      String formatStrFuncList = ',' + strFuncList + ',';
      String formatFuncName = ',' + funcName + ',';
      int index;
      index = formatStrFuncList.indexOf(formatFuncName);
      if (index >= 0)
        return true;
      else
        return false;
    } catch (Exception e) {
      TestUtil.logErr(
          "Exception thrown while trying to check the presence of the string func.",
          e);
      throw new RemoteException(e.getMessage());
    }
  }

  public boolean isSystemFuncFound(String funcName, Connection conn)
      throws RemoteException {
    TestUtil.logTrace("isSystemFuncFound");
    // Check if the System Function is supported by the DBMS

    try {
      DatabaseMetaData dbmd = null;
      dbmd = conn.getMetaData();
      String systemFuncList = dbmd.getSystemFunctions();
      String formatSystemFuncList = ',' + systemFuncList + ',';
      String formatFuncName = ',' + funcName + ',';
      int index;
      index = formatSystemFuncList.indexOf(formatFuncName);
      if (index >= 0)
        return true;
      else
        return false;
    } catch (Exception e) {
      TestUtil.logErr(
          "Exception thrown while trying to check the presence of the system func.",
          e);
      throw new RemoteException(e.getMessage());
    }
  }

  public boolean isNumericFuncFound(String funcName, Connection conn)
      throws RemoteException {
    TestUtil.logTrace("isNumericFuncFound");
    // Check if the Numeric Function is supported by the DBMS

    try {
      DatabaseMetaData dbmd = null;
      dbmd = conn.getMetaData();
      String numericFuncList = dbmd.getNumericFunctions();
      String formatNumericFuncList = ',' + numericFuncList + ',';
      String formatFuncName = ',' + funcName + ',';
      int index;
      index = formatNumericFuncList.indexOf(formatFuncName);
      if (index >= 0)
        return true;
      else
        return false;
    } catch (Exception e) {
      TestUtil.logErr(
          "Exception thrown while trying to check the presence of the Numeric func.",
          e);
      throw new RemoteException(e.getMessage());
    }
  }

  public boolean isTimeDateFuncFound(String funcName, Connection conn)
      throws RemoteException {
    TestUtil.logTrace("isTimeDateFuncFound");
    // Check if the Time & Date Function is supported by the DBMS

    try {
      DatabaseMetaData dbmd = null;
      dbmd = conn.getMetaData();
      String timeDateFuncList = dbmd.getTimeDateFunctions();
      String formatTimeDateFuncList = ',' + timeDateFuncList + ',';
      String formatFuncName = ',' + funcName + ',';
      int index;
      index = formatTimeDateFuncList.indexOf(formatFuncName);
      if (index >= 0)
        return true;
      else
        return false;
    } catch (Exception e) {
      TestUtil.logErr(
          "Exception thrown while trying to check the presence of the Time & Date func.",
          e);
      throw new RemoteException(e.getMessage());
    }
  }
}
