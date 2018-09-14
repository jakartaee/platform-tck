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
 * @(#)DBSupport.java	1.18 03/05/16
 */

package com.sun.ts.tests.integration.util;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import javax.ejb.*;
import java.rmi.*;
import java.sql.*;
import javax.sql.*;
import java.util.*;

public class DBSupport implements java.io.Serializable {

  private static final String DATASOURCE = "java:comp/env/jdbc/DB1";

  // database info
  private DataSource ds;

  private String tName;

  private TSNamingContext nctx;

  private transient Connection con = null;

  private String sqlString;

  private transient Statement stmt = null;

  private transient PreparedStatement pStmt = null;

  // Instance data for an Account
  private Account account;

  public DBSupport() throws Exception {
    lookupDataSource();
  }

  public void initDB(boolean initTab, boolean popTab) throws Exception {
    TestUtil.logTrace("initDB");

    if (!initTab)
      return;

    // Make the JDBC DB connection
    TestUtil.logTrace("Perform JDBC connection ...");
    try {
      con = getDBConnection();
      TestUtil.logTrace("Made the JDBC connection");
    } catch (Exception ex) {
      TestUtil.logErr(
          "Unexpected exception getting JDBC connection " + ex.getMessage());
      TestUtil.printStackTrace(ex);
      throw ex;
    }

    // Delete all rows in table
    try {
      sqlString = TestUtil.getProperty("Integration_Tab_Delete");
      tName = TestUtil.getTableName(sqlString);// get the table name
      stmt = con.createStatement();
      stmt.execute(sqlString);
      TestUtil.logTrace("Deleted all rows from table " + tName);
    } catch (Exception e) {
      // do nothing if table does not exist
    } finally {
      try {
        if (stmt != null)
          stmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (popTab)
      populateTable();
  }

  private void lookupDataSource() throws Exception {
    try {
      nctx = new TSNamingContext();
      TestUtil.logTrace("lookup: " + DATASOURCE);
      ds = (DataSource) nctx.lookup(DATASOURCE);
      TestUtil.logTrace("ds: " + ds);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Exception("Exception in lookupDataSource: " + e);
    }
  }

  private Connection getDBConnection() throws Exception {
    try {
      if (con != null) {
        try {
          closeDBConnection();
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
        }
      }
      con = ds.getConnection();
      if (con != null) {
        return con;
      } else {
        throw new Exception("Unable to get database connection");
      }
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new Exception("SQLException caught in getDBConnection");
    }
  }

  private void closeDBConnection() throws Exception {
    if (con != null) {
      con.close();
      con = null;
    }
  }

  private void getAccount(int acct) throws Exception {
    try {
      con = getDBConnection();
      sqlString = TestUtil.getProperty("Integration_Select_Account");
      pStmt = con.prepareStatement(sqlString);
      pStmt.setInt(1, acct);
      ResultSet result = pStmt.executeQuery();
      result.next();
      account = new Account(result.getInt("account"),
          result.getFloat("balance"));
      pStmt.close();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Exception("Exception caught in getAccount");
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  public void populateTable() throws Exception {
    TestUtil.logTrace("populateTable");

    // Make the JDBC DB connection
    TestUtil.logTrace("Perform JDBC connection ...");
    try {
      con = getDBConnection();
      TestUtil.logTrace("Made the JDBC connection");
    } catch (Exception ex) {
      TestUtil.logErr(
          "Unexpected exception getting JDBC connection " + ex.getMessage());
      TestUtil.printStackTrace(ex);
      throw ex;
    }

    // Populate the DB table
    try {
      stmt = con.createStatement();
      sqlString = TestUtil.getProperty("Integration_Insert1");
      stmt.executeUpdate(sqlString);
      sqlString = TestUtil.getProperty("Integration_Insert2");
      stmt.executeUpdate(sqlString);
      sqlString = TestUtil.getProperty("Integration_Insert3");
      stmt.executeUpdate(sqlString);
      sqlString = TestUtil.getProperty("Integration_Insert4");
      stmt.executeUpdate(sqlString);
      sqlString = TestUtil.getProperty("Integration_Insert5");
      stmt.executeUpdate(sqlString);
      TestUtil.logTrace("Successfully created the table " + tName);
      TestUtil.logTrace("jdbc connection closed");
    } catch (Exception e) {
      TestUtil.logErr("Exception creating table " + tName + e.getMessage());
      TestUtil.printStackTrace(e);
      throw e;
    } finally {
      try {
        if (stmt != null)
          stmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  public void updateAccount() throws Exception {
    try {
      con = getDBConnection();
      sqlString = TestUtil.getProperty("Integration_Update_Account");
      pStmt = con.prepareStatement(sqlString);
      pStmt.setFloat(1, (float) account.balance());
      pStmt.setInt(2, account.id());
      pStmt.executeUpdate();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Exception("Exception caught in updateAccount");
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  public void updateAccount(int acct, double balance) throws Exception {
    try {
      con = getDBConnection();
      sqlString = TestUtil.getProperty("Integration_Update_Account");
      pStmt = con.prepareStatement(sqlString);
      pStmt.setFloat(1, (float) balance);
      pStmt.setInt(2, acct);
      pStmt.executeUpdate();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Exception("Exception caught in updateAccount");
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  public double balance(int acct) throws Exception {
    try {
      getAccount(acct);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Exception("Exception caught in getBalance");
    }
    return account.balance();
  }

  public double deposit(int acct, double amt) throws Exception {
    try {
      getAccount(acct);
      account.deposit(amt);
      updateAccount();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Exception("Exception caught in getBalance");
    }
    return account.balance();
  }

  public double withdraw(int acct, double amt) throws Exception {
    try {
      getAccount(acct);
      account.withdraw(amt);
      updateAccount();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Exception("Exception caught in getBalance");
    }
    return account.balance();
  }

  public String getAllAccounts() throws Exception {
    StringBuffer accounts = new StringBuffer();
    try {
      con = getDBConnection();
      stmt = con.createStatement();
      sqlString = TestUtil.getProperty("Integration_Select_All");
      ResultSet result = stmt.executeQuery(sqlString);
      while (result.next()) {
        account = new Account(result.getInt("account"),
            result.getFloat("balance"));
        accounts.append("" + account.id() + "  " + account.balance() + "\n");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Exception("Exception caught in getAllAccounts");
    } finally {
      try {
        if (stmt != null)
          stmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
    return accounts.toString();
  }

  public void insert(int account, double balance) throws Exception {
    TestUtil.logTrace("insert");

    // Insert row into table
    try {
      con = getDBConnection();
      sqlString = TestUtil.getProperty("Integration_Insert");
      TestUtil.logMsg(sqlString);
      pStmt = con.prepareStatement(sqlString);
      pStmt.setInt(1, account);
      pStmt.setFloat(2, (float) balance);
      pStmt.executeUpdate();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Exception("Exception caught in insert");
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  public void delete(int acct) throws Exception {
    TestUtil.logTrace("delete");

    try {
      con = getDBConnection();
      sqlString = TestUtil.getProperty("Integration_Delete_Account");
      TestUtil.logMsg(sqlString);
      pStmt = con.prepareStatement(sqlString);
      pStmt.setInt(1, acct);
      int resultCount = pStmt.executeUpdate();
      if (resultCount != 1) {
        throw new Exception("delete failed: resultCount = " + resultCount);
      }
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new Exception("Exception caught in delete");
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  public boolean keyExists(int acct) throws Exception {
    try {
      con = getDBConnection();
      sqlString = TestUtil.getProperty("Integration_Select_Account");
      pStmt = con.prepareStatement(sqlString);
      pStmt.setInt(1, acct);
      ResultSet result = pStmt.executeQuery();
      if (!result.next())
        return false;
      else
        return true;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Exception("Exception caught in getAccount");
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

}
