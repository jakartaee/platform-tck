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
 * @(#)TxBeanEJB.java	1.48 03/05/16
 */

package com.sun.ts.tests.ejb.ee.tx.txbean;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import java.rmi.*;
import java.sql.*;
import javax.sql.*;
import javax.ejb.*;
import javax.transaction.*;

public class TxBeanEJB implements SessionBean {

  private SessionContext sctx = null;

  private TSNamingContext context = null;

  // con1 will be used for the table1 connection
  // con2 will be used for the table2 connection
  private transient Connection con1, con2;

  private transient Statement stmt;

  private transient PreparedStatement pStmt;

  // DataSources
  private DataSource ds;

  private String tName1 = null;

  private String tName2 = null;

  private Integer tSize = null;

  // Exception flags
  public static final int FLAGAPPEXCEPTION = -1;

  public static final int FLAGAPPEXCEPTIONWITHROLLBACK = -2;

  public static final int FLAGSYSEXCEPTION = -3;

  public static final int FLAGEJBEXCEPTION = -5;

  public static final int FLAGERROR = -6;

  public static final int FLAGROLLBACK = -7;

  // Required EJB methods
  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("ejbCreate");
  }

  public void setSessionContext(SessionContext sc) {
    TestUtil.logTrace("setSessionContext");
    try {
      this.sctx = sc;
      this.context = new TSNamingContext();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception setting EJB context", e);
      throw new EJBException(e.getMessage());
    }
  }

  public void ejbRemove() {
    TestUtil.logTrace("ejbRemove");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  // ===========================================================
  // The TxBean interface implementation

  // Database methods
  private void dbConnect2(String tName) {
    TestUtil.logTrace("dbConnect2");
    initSetup();
    try {
      if (tName.equals(this.tName1)) {
        conTable1();
      } else if (tName.equals(tName2)) {
        conTable2();
      } else {
        throw new EJBException("Invalid table name: " + tName);
      }

      TestUtil.logTrace("Made the JDBC connection to " + tName + " DB");
    } catch (Exception se) {
      TestUtil.logErr("Exception opening db connection for " + tName, se);
      throw new EJBException(se.getMessage());
    }
  }

  private void dbUnConnect2(String tName) {
    TestUtil.logTrace("dbUnConnect2");
    initSetup();

    try {
      if (tName.equals(this.tName1)) {
        con1.close();
        con1 = null;
      } else if (tName.equals(this.tName2)) {
        con2.close();
        con2 = null;
      } else {
        throw new EJBException("Invalid table name: " + tName);
      }
      TestUtil.logMsg("Closed " + tName + " connection");
    } catch (SQLException se) {
      TestUtil.logErr("SQLException closing db connection for " + tName, se);
      throw new EJBException(se.getMessage());
    }
  }

  public void createData(String tName) {
    TestUtil.logTrace("createData");
    initSetup();

    try {
      dbConnect2(tName);
      if (tName.equals(this.tName1)) {
        createTable1();
      } else if (tName.equals(this.tName2)) {
        createTable2();
      } else {
        throw new EJBException("Invalid table name: " + tName);
      }
      TestUtil.logMsg("Created " + tName);
    } catch (Exception e) {
      TestUtil.logErr("Exception creating table " + tName, e);
      throw new EJBException(e.getMessage());
    } finally {
      try {
        dbUnConnect2(tName);
      } catch (Exception se) {
        TestUtil.logErr("Exception closing db connection for " + tName, se);
        throw new EJBException(se.getMessage());
      }
    }
  }

  public boolean insert(String tName, int key) {
    TestUtil.logTrace("insert");
    initSetup();

    int newKey = key;
    String newName = null;
    float newPrice = (float) .00 + newKey;
    pStmt = null;

    try {
      dbConnect2(tName);
      if (tName.equals(this.tName1)) {
        newName = this.tName1 + "-" + newKey;
        // String updateString = "insert into " + this.tName1 + " values(?, ?,
        // ?)";
        String updateString = TestUtil.getProperty("TxBean_insert1");
        pStmt = con1.prepareStatement(updateString);
      } else if (tName.equals(this.tName2)) {
        newName = this.tName2 + "-" + newKey;
        // String updateString = "insert into " + this.tName2 + " values(?, ?,
        // ?)";
        String updateString = TestUtil.getProperty("TxBean_insert2");
        pStmt = con2.prepareStatement(updateString);
      } else {
        throw new EJBException("Invalid table name: " + tName);
      }

      // Perform the insert(s)
      pStmt.setInt(1, newKey);
      pStmt.setString(2, newName);
      pStmt.setFloat(3, newPrice);
      pStmt.executeUpdate();

      TestUtil.logMsg("Inserted a row into the table " + tName);
      return true;

    } catch (Exception e) {
      TestUtil.logErr("Exception inserting a row into table " + tName, e);
      return false;
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        dbUnConnect2(tName);
      } catch (SQLException se) {
        TestUtil.logErr("SQLException closing db connection for " + tName, se);
        throw new EJBException(se.getMessage());
      }
    }
  }

  public void delete(String tName, int fromKey, int toKey) {
    TestUtil.logTrace("delete");
    initSetup();

    pStmt = null;
    try {
      dbConnect2(tName);
      if (tName.equals(this.tName1)) {
        // String updateString = "delete from " + this.tName1 + " where KEY_ID =
        // ?";
        String updateString = TestUtil.getProperty("TxBean_delete1");
        pStmt = con1.prepareStatement(updateString);
      } else if (tName.equals(this.tName2)) {
        // String updateString = "delete from " + this.tName2 + " where KEY_ID =
        // ?";
        String updateString = TestUtil.getProperty("TxBean_delete2");
        pStmt = con2.prepareStatement(updateString);
      } else {
        throw new EJBException("Invalid table name: " + tName);
      }

      for (int i = fromKey; i <= toKey; i++) {
        pStmt.setInt(1, i);
        pStmt.executeUpdate();
      }
      TestUtil.logMsg("Deleted row(s) " + fromKey + " thru " + toKey
          + " from the table " + tName);

    } catch (Exception e) {
      TestUtil.logErr("Exception deleting row(s) " + fromKey + " thru " + toKey
          + " from the table " + tName, e);
      throw new EJBException(e.getMessage());
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        dbUnConnect2(tName);
      } catch (SQLException se) {
        TestUtil.logErr("SQLException closing db connection for " + tName, se);
        throw new EJBException(se.getMessage());
      }
    }
  }

  public boolean delete(String tName, int fromKey, int toKey, int flag)
      throws AppException {
    TestUtil.logTrace("delete with exception");
    initSetup();

    pStmt = null;
    boolean isRolledback = false;
    try {
      dbConnect2(tName);
      if (tName.equals(this.tName1)) {
        // String updateString = "delete from " + this.tName1 + " where KEY_ID =
        // ?";
        String updateString = TestUtil.getProperty("TxBean_delete1");
        pStmt = con1.prepareStatement(updateString);
      } else if (tName.equals(this.tName2)) {
        // String updateString = "delete from " + this.tName2 + " where KEY_ID =
        // ?";
        String updateString = TestUtil.getProperty("TxBean_delete2");
        pStmt = con2.prepareStatement(updateString);
      } else {
        throw new EJBException("Invalid table name: " + tName);
      }

      for (int i = fromKey; i <= toKey; i++) {
        pStmt.setInt(1, i);
        pStmt.executeUpdate();
      }
      TestUtil.logMsg("Deleted row(s) " + fromKey + " thru " + toKey
          + " from the table " + tName);

      // Check for intended exceptions to be thrown or rollbacks
      if (flag == FLAGAPPEXCEPTION)
        throwAppException();

      if (flag == FLAGAPPEXCEPTIONWITHROLLBACK) {
        TestUtil.logTrace("calling setRollbackOnly");
        sctx.setRollbackOnly();
        TestUtil.logTrace("Calling getRollbackOnly method");
        if (sctx.getRollbackOnly())
          isRolledback = true;
        throwAppException();
      }

      if (flag == FLAGSYSEXCEPTION) {
        throwSysException();
      }

      if (flag == FLAGEJBEXCEPTION) {
        throw new EJBException(
            "EJBException from TxBeanEJB delete with exception" + "method");
      }

      if (flag == FLAGERROR) {
        throw new Error("Error from TxBeanEJB delete with exception method");
      }

      if (flag == FLAGROLLBACK) {
        TestUtil.logTrace("Calling setRollbackOnly method");
        sctx.setRollbackOnly();
        TestUtil.logTrace("Calling getRollbackOnly method");
        if (sctx.getRollbackOnly())
          isRolledback = true;
      }
    } catch (AppException e) {
      TestUtil.printStackTrace(e);
      throw new AppException("AppException from delete");
    } catch (Exception e) {
      TestUtil.logErr("Exception deleting row(s) " + fromKey + " thru " + toKey
          + " from the table " + tName, e);
      throw new EJBException(e.getMessage());
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        dbUnConnect2(tName);
      } catch (SQLException se) {
        TestUtil.logErr("SQLException closing db connection for " + tName, se);
        throw new EJBException(se.getMessage());
      }
    }
    return isRolledback;
  }

  public void update(String tName, int key, String brandName) {
    TestUtil.logTrace("update for row brandName");
    initSetup();

    int newKey = key;
    String newName = brandName;
    pStmt = null;

    try {
      dbConnect2(tName);
      if (tName.equals(this.tName1)) {
        // String updateString = "update " + this.tName1 +
        // " set TABONE_NAME = ? where KEY_ID = ?";
        String updateString = TestUtil.getProperty("TxBean_update1");
        pStmt = con1.prepareStatement(updateString);
      } else if (tName.equals(this.tName2)) {
        // String updateString = "update " + this.tName2 +
        // " set TABTWO_NAME = ? where KEY_ID = ?";
        String updateString = TestUtil.getProperty("TxBean_update2");
        pStmt = con2.prepareStatement(updateString);
      } else {
        throw new EJBException("Invalid table name: " + tName);
      }

      // Perform the update
      pStmt.setString(1, newName);
      pStmt.setInt(2, newKey);
      pStmt.executeUpdate();
      TestUtil.logMsg("Updated a row in the table " + tName);

    } catch (Exception e) {
      TestUtil.logErr("Exception updating a row in the table " + tName, e);
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        dbUnConnect2(tName);
      } catch (SQLException se) {
        TestUtil.logErr("SQLException closing db connection for " + tName, se);
        throw new EJBException(se.getMessage());
      }
    }
  }

  public void update(String tName, int key, float price) {
    TestUtil.logTrace("update for row price");
    initSetup();

    int newKey = key;
    float newPrice = price;
    pStmt = null;

    try {
      dbConnect2(tName);
      if (tName.equals(this.tName1)) {
        // String updateString = "update " + this.tName1 +
        // " set PRICE = ? where KEY_ID = ?";
        String updateString = TestUtil.getProperty("TxBean_update3");
        pStmt = con1.prepareStatement(updateString);
      } else if (tName.equals(this.tName2)) {
        // String updateString = "update " + this.tName2 +
        // " set PRICE = ? where KEY_ID = ?";
        String updateString = TestUtil.getProperty("TxBean_update4");
        pStmt = con2.prepareStatement(updateString);
      } else {
        throw new EJBException("Invalid table name: " + tName);
      }

      pStmt.setFloat(1, newPrice);
      pStmt.setInt(2, newKey);
      pStmt.executeUpdate();
      TestUtil.logMsg("Updated a row in the table " + tName);

    } catch (Exception e) {
      TestUtil.logErr("Exception updating a row in the table " + tName, e);
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        dbUnConnect2(tName);
      } catch (SQLException se) {
        TestUtil.logErr("SQLException closing db connection for " + tName, se);
        throw new EJBException(se.getMessage());
      }
    }
  }

  public void destroyData(String tName) {
    TestUtil.logTrace("destroyData");
    initSetup();
    try {
      dbConnect2(tName);
      if (tName.equals(this.tName1)) {
        dropTable1();
      } else if (tName.equals(this.tName2)) {
        dropTable2();
      } else {
        throw new EJBException("Invalid table name: " + tName);
      }
      TestUtil.logMsg("Removed " + tName);
    } catch (Exception e) {
      TestUtil.logErr("Exception occured trying to drop table", e);
      throw new EJBException(e.getMessage());
    } finally {
      try {
        dbUnConnect2(tName);
      } catch (Exception se) {
        TestUtil.logErr("Exception closing db connection for " + tName, se);
        throw new EJBException(se.getMessage());
      }
    }
  }

  // Local resource manipulators
  public int getDefaultTxIsolationLevel(String tName) {
    TestUtil.logTrace("getDefaultTxIsolationLevel");
    initSetup();

    int iInt = 0;
    DatabaseMetaData dmd1, dmd2;
    dmd1 = dmd2 = null;

    try {
      dbConnect2(tName);
      if (tName.equals(this.tName1)) {
        dmd1 = con1.getMetaData();
        iInt = dmd1.getDefaultTransactionIsolation();
      } else if (tName.equals(this.tName2)) {
        dmd2 = con2.getMetaData();
        iInt = dmd2.getDefaultTransactionIsolation();
      } else {
        throw new EJBException("Invalid table name: " + tName);
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occured getting default Isolation level", e);
      throw new EJBException(e.getMessage());
    } finally {
      try {
        dbUnConnect2(tName);
      } catch (Exception se) {
        TestUtil.logErr("Exception closing db connection for " + tName, se);
        throw new EJBException(se.getMessage());
      }
    }
    return iInt;
  }

  // Test Results methods
  public Vector getResults(String tName) {
    TestUtil.logTrace("getResults");
    initSetup();

    ResultSet rs;
    Vector queryResults = new Vector();
    int i;
    String query, s, name;
    float f;
    stmt = null;

    try {
      dbConnect2(tName);
      if (tName.equals(this.tName1)) {
        // query = "select * from " + this.tName1;
        query = TestUtil.getProperty("TxBean_query1");
        stmt = con1.createStatement();
        rs = stmt.executeQuery(query);
        name = "TABONE_NAME";
      } else if (tName.equals(this.tName2)) {
        // query = "select * from " + this.tName2;
        query = TestUtil.getProperty("TxBean_query2");
        stmt = con2.createStatement();
        rs = stmt.executeQuery(query);
        name = "TABTWO_NAME";
      } else {
        throw new EJBException("Invalid table name: " + tName);
      }

      while (rs.next()) {
        i = rs.getInt("KEY_ID");
        s = rs.getString(name);
        f = rs.getFloat("PRICE");
        queryResults.addElement(new Integer(i));
        queryResults.addElement(s);
        queryResults.addElement(new Float(f));
      }
      TestUtil.logMsg("Obtained " + tName + " table ResultSet");

    } catch (Exception e) {
      TestUtil.logErr("Exception obtaining " + tName + " table ResultSet", e);
      throw new EJBException(e.getMessage());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
        dbUnConnect2(tName);
      } catch (SQLException se) {
        TestUtil.logErr("SQLException closing db connection for " + tName, se);
        throw new EJBException(se.getMessage());
      }
    }
    return queryResults;
  }

  public Vector getResults(String tName, int key) {
    TestUtil.logTrace("getResults for a single row");
    initSetup();

    ResultSet rs;
    Vector queryResults = new Vector();
    int i;
    String query, s, name;
    float f;
    // stmt = null;
    pStmt = null;

    try {
      dbConnect2(tName);
      if (tName.equals(this.tName1)) {
        // query = "select KEY_ID, TABONE_NAME, PRICE from " + this.tName1 +
        // " where KEY_ID = " + key;
        // stmt = con1.createStatement();
        // rs = stmt.executeQuery(query);
        query = TestUtil.getProperty("TxBean_query3");
        pStmt = con1.prepareStatement(query);
        pStmt.setInt(1, key);
        rs = pStmt.executeQuery();
        name = "TABONE_NAME";
      } else if (tName.equals(this.tName2)) {
        // query = "select KEY_ID, TABTWO_NAME, PRICE from " + this.tName2 +
        // " where KEY_ID = " + key;
        // stmt = con2.createStatement();
        // rs = stmt.executeQuery(query);
        query = TestUtil.getProperty("TxBean_query4");
        pStmt = con1.prepareStatement(query);
        pStmt.setInt(1, key);
        rs = pStmt.executeQuery();
        name = "TABTWO_NAME";
      } else {
        throw new EJBException("Invalid table name: " + tName);
      }

      while (rs.next()) {
        i = rs.getInt("KEY_ID");
        s = rs.getString(name);
        f = rs.getFloat("PRICE");
        queryResults.addElement(new Integer(i));
        queryResults.addElement(s);
        queryResults.addElement(new Float(f));
      }
      TestUtil.logMsg("Obtained " + tName + " table ResultSet");

    } catch (Exception e) {
      TestUtil.logErr("Exception obtaining " + tName + " table ResultSet", e);
      throw new EJBException(e.getMessage());
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        dbUnConnect2(tName);
      } catch (SQLException se) {
        TestUtil.logErr("SQLException closing db connection for " + tName, se);
        throw new EJBException(se.getMessage());
      }
    }
    return queryResults;
  }

  public void initLogging(Properties p) {
    TestUtil.logTrace("initLogging");
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

  public void initLogging() {
  }

  // Exception methods
  public void throwAppException() throws AppException {
    TestUtil.logTrace("throwAppException");
    throw new AppException("AppException from TxBean");
  }

  public void throwSysException() {
    TestUtil.logTrace("throwSysException");
    throw new SysException("SysException from TxBean");
  }

  public void throwEJBException() {
    TestUtil.logTrace("throwEJBException");
    throw new EJBException("EJBException from TxBean");
  }

  public void throwError() {
    TestUtil.logTrace("throwError");
    throw new Error("Error from TxBean");
  }

  // Utility methods
  public void listTableData(Vector dbResults) {
    TestUtil.logTrace("listTableData");
    initSetup();

    try {
      if (dbResults.isEmpty())
        TestUtil.logTrace("Empty vector!!!");
      else {
        for (int j = 0; j < dbResults.size(); j++)
          TestUtil.logTrace(dbResults.elementAt(j).toString());
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occured trying to list table data", e);
      throw new EJBException(e.getMessage());
    }
  }

  // private methods
  private void conTable1() {
    TestUtil.logTrace("conTable1");
    initSetup();
    try {
      con1 = ds.getConnection();
      TestUtil.logTrace("con1: " + con1.toString());
    } catch (SQLException e) {
      TestUtil.logErr("SQLException connecting to " + this.tName1 + " DB", e);
      throw new EJBException(e.getMessage());
    } catch (Exception ee) {
      TestUtil.logErr("Exception connecting to " + this.tName1 + " DB", ee);
      throw new EJBException(ee.getMessage());
    }
  }

  private void conTable2() {
    TestUtil.logTrace("conTable2");
    initSetup();
    try {
      con2 = ds.getConnection();
      TestUtil.logTrace("con2: " + con2.toString());
    } catch (SQLException e) {
      TestUtil.logErr("SQLException connecting to " + this.tName2 + " DB", e);
      throw new EJBException(e.getMessage());
    } catch (Exception ee) {
      TestUtil.logErr("Exception connecting to " + this.tName2 + " DB", ee);
      throw new EJBException(ee.getMessage());
    }
  }

  private void createTable1() {
    TestUtil.logTrace("createTable1");
    initSetup();
    pStmt = null;

    try {
      dropTable1();
      TestUtil.logTrace("Deleted all rows from table " + this.tName1);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("SQLException encountered while deleting rows from "
          + this.tName1 + ":" + e.getMessage());
    }

    try {
      // Add the prescribed table rows
      TestUtil.logTrace("Adding the " + this.tName1 + " table rows");
      // String updateString = "insert into " + this.tName1 + " values(?, ?,
      // ?)";
      String updateString = TestUtil.getProperty("TxBean_insert1");
      pStmt = con1.prepareStatement(updateString);

      for (int i = 1; i <= this.tSize.intValue(); i++) {
        // Perform the insert(s)
        int newKey = i;
        String newName = this.tName1 + "-" + i;
        float newPrice = i + (float) .00;

        pStmt.setInt(1, newKey);
        pStmt.setString(2, newName);
        pStmt.setFloat(3, newPrice);
        pStmt.executeUpdate();
      }

    } catch (SQLException e) {
      TestUtil.logErr("SQLException creating " + this.tName1 + " table", e);
      throw new EJBException(e.getMessage());
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
      } catch (SQLException se) {
        TestUtil.logErr("SQLException pStmt/stmt for " + this.tName1 + ":"
            + se.getMessage(), se);
        throw new EJBException(se.getMessage());
      }
    }
  }

  private void createTable2() {
    TestUtil.logTrace("createTable2");
    initSetup();
    pStmt = null;

    try {
      dropTable2();
      TestUtil.logTrace("Deleted all rows from table " + this.tName2);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("SQLException encountered while deleting rows from "
          + this.tName2 + ":" + e.getMessage());
    }

    try {

      // Add the prescribed table rows
      TestUtil.logTrace("Adding the " + this.tName2 + " table rows");
      // String updateString = "insert into " + this.tName2 + " values(?, ?,
      // ?)";
      String updateString = TestUtil.getProperty("TxBean_insert2");
      pStmt = con2.prepareStatement(updateString);

      for (int i = 1; i <= this.tSize.intValue(); i++) {
        // Perform the insert(s)
        int newKey = i;
        String newName = this.tName2 + "-" + i;
        float newPrice = i + (float) .00;

        pStmt.setInt(1, newKey);
        pStmt.setString(2, newName);
        pStmt.setFloat(3, newPrice);
        pStmt.executeUpdate();

      }
    } catch (SQLException e) {
      TestUtil.logErr("SQLException creating " + this.tName2 + " table", e);
      throw new EJBException(e.getMessage());
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
      } catch (SQLException se) {
        TestUtil.logErr(
            "SQLException pStmt/stmt for " + this.tName2 + se.getMessage(), se);
        throw new EJBException(se.getMessage());
      }
    }
  }

  private void dropTable1() {
    TestUtil.logTrace("dropTable1");
    initSetup();

    // String removeString = "drop table " + this.tName1;
    String removeString = TestUtil.getProperty("TxBean_Tab1_Delete");
    stmt = null;
    try {
      stmt = con1.createStatement();
      stmt.executeUpdate(removeString);
    } catch (SQLException e) {
      TestUtil.logErr("SQLException dropping " + this.tName1 + " table", e);
      throw new EJBException(e.getMessage());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException se) {
        TestUtil.logErr("SQLException stmt for " + this.tName1, se);
        throw new EJBException(se.getMessage());
      }
    }
  }

  private void dropTable2() {
    TestUtil.logTrace("dropTable2");
    initSetup();

    // String removeString = "drop table " + this.tName2;
    String removeString = TestUtil.getProperty("TxBean_Tab2_Delete");
    stmt = null;

    try {
      stmt = con2.createStatement();
      stmt.executeUpdate(removeString);
    } catch (SQLException e) {
      TestUtil.logErr("SQLException dropping " + this.tName2 + " table", e);
      throw new EJBException(e.getMessage());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
      } catch (SQLException se) {
        TestUtil.logErr("SQLException stmt for " + this.tName2, se);
        throw new EJBException(se.getMessage());
      }
    }
  }

  private void initSetup() {
    try {
      // Get the DataSource
      ds = (DataSource) context.lookup("java:comp/env/jdbc/DB1");
      TestUtil.logTrace("ds: " + ds);
      TestUtil.logTrace("DataSource lookup OK!");

      // Get the table names
      this.tName1 = TestUtil
          .getTableName(TestUtil.getProperty("TxBean_Tab1_Delete"));
      TestUtil.logTrace("tName1: " + this.tName1);
      this.tName2 = TestUtil
          .getTableName(TestUtil.getProperty("TxBean_Tab2_Delete"));
      TestUtil.logTrace("tName2: " + this.tName2);

      // Get the table size
      this.tSize = (Integer) context.lookup("java:comp/env/size");

    } catch (Exception e) {
      TestUtil.logErr("Create exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }

  }
  // ===========================================================
}
