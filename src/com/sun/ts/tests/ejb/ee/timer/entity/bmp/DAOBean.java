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

package com.sun.ts.tests.ejb.ee.timer.entity.bmp;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import java.rmi.*;
import java.sql.*;
import javax.sql.DataSource;

public class DAOBean implements EntityBean {
  protected EntityContext ectx = null;

  protected TSNamingContext nctx = null;

  protected static final String DATASOURCE = "java:comp/env/jdbc/DBTimer";

  protected transient Connection dbConnection = null;

  protected String dsname = null; // dataSourcename

  protected DataSource ds = null; // dataSource

  protected int cofID = 0; // Coffee ID (Primary Key)

  protected String cofName = null; // Coffee Name

  protected float cofPrice = 0; // Coffee Price

  public Integer ejbCreate(Properties p, int coffeeID, String coffeName,
      float coffeePrice, int flag) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      TestUtil.logMsg("Initialize remote logging");
      TestUtil.init(p);

      tableInit();
      createNewRow(coffeeID, coffeName, coffeePrice);
    } catch (RemoteLoggingInitException e) {
      throw new CreateException(e.getMessage());
    } catch (Exception e) {
      throw new CreateException(
          "Unexpected Exception occurred in ejbCreate: " + e);
    }

    return new Integer(coffeeID);
  }

  public void ejbPostCreate(Properties p, int coffeeID, String coffeName,
      float coffeePrice, int flag) {
  }

  public Integer ejbCreate(Properties p, int coffeeID, String coffeName,
      float coffeePrice) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      TestUtil.logMsg("Initialize remote logging");
      TestUtil.init(p);

      tableInit();
      createNewRow(coffeeID, coffeName, coffeePrice);
    } catch (RemoteLoggingInitException e) {
      throw new CreateException(e.getMessage());
    } catch (Exception e) {
      throw new CreateException(
          "Unexpected Exception occurred in ejbCreate: " + e);
    }

    return new Integer(coffeeID);
  }

  public void ejbPostCreate(Properties p, int coffeeID, String coffeName,
      float coffeePrice) {
    TestUtil.logTrace("ejbPostCreate");
  }

  public void setEntityContext(EntityContext c) {
    TestUtil.logTrace("setEntityContext");
    ectx = c;
    try {
      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      // Lookup DataSource from JNDI
      TestUtil.logMsg("Lookup DataSource from JNDI : " + DATASOURCE);
      this.dsname = DATASOURCE;
      this.ds = (DataSource) nctx.lookup(this.dsname);
      TestUtil.logMsg("dsname=" + this.dsname + "ds=" + this.ds);
    } catch (NamingException e) {
      TestUtil.logErr("Unexpected NamingException ... ");
      throw new EJBException("Unable to obtain naming context:" + e);
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception ... ");
      throw new EJBException(
          "Unexpected Exception occurred in setEntityContext" + e);
    }
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("unsetEntityContext");
  }

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
  }

  public Integer ejbFindTheBean(Integer key) throws FinderException {
    return new Integer(key);
  }

  public Integer ejbFindByPrimaryKey(Integer key) throws FinderException {
    TestUtil.logTrace("ejbFindByPrimaryKey");

    try {
      TestUtil.logMsg("Check if Primary Key Exists");
      boolean foundKey = keyExists(key.intValue());
      if (foundKey)
        return key;
      else
        throw new FinderException("Key not found: " + key);
    } catch (Exception e) {
      throw new FinderException("Exception occurred: " + e);
    }
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("ejbRemove");
    TestUtil.logMsg("PrimaryKey=" + ectx.getPrimaryKey());
    try {
      removeRow(((Integer) ectx.getPrimaryKey()).intValue());
    } catch (Exception e) {
      throw new RemoveException("Unexpected Exception occurred in ejbRemove");
    }
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  // ===========================================================
  // DB Access methods

  private void getDBConnection() throws SQLException {
    TestUtil.logTrace("getDBConnection");
    if (dbConnection != null) {
      try {
        closeDBConnection();
      } catch (Exception e) {
        TestUtil.logErr("Exception occurred trying to close DB Connection", e);
      }
    }
    dbConnection = ds.getConnection();
  } // end getDBConnection

  private void closeDBConnection() throws SQLException {
    TestUtil.logTrace("closeDBConnection");
    if (dbConnection != null) {
      dbConnection.close();
      dbConnection = null;
    }
  } // end closeDBConnection

  private void tableInit() throws SQLException {
    Statement stmt = null;
    TestUtil.logTrace("tableInit");

    getDBConnection();

    try {
      stmt = dbConnection.createStatement();
      String sqlStr = TestUtil.getProperty("BB_Tab_Delete");
      stmt.executeUpdate(sqlStr);
      TestUtil.logMsg(
          "Deleted all rows from table " + TestUtil.getTableName(sqlStr));
    } catch (SQLException s) {
      throw new SQLException("SQL Exception in tableInit:" + s.getMessage());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.logErr("SQLException occurred closing DB Connection", e);
      }
    }
  } /* end tableInit */

  private void createNewRow(int cofID, String cofName, float cofPrice)
      throws CreateException, SQLException {
    PreparedStatement pStmt = null;
    TestUtil.logTrace("createNewRow");

    getDBConnection();

    try {
      String sqlStr = TestUtil.getProperty("BB_Insert1");
      TestUtil.logMsg(sqlStr);
      pStmt = dbConnection.prepareStatement(sqlStr);
      pStmt.setInt(1, cofID);
      pStmt.setString(2, cofName);
      pStmt.setFloat(3, cofPrice);
      if (pStmt.executeUpdate() != 1) {
        throw new CreateException("SQL INSERT failed in createNewRow");
      } else {
        this.cofID = cofID;
        this.cofName = cofName;
        this.cofPrice = cofPrice;
      }
    } catch (SQLException e) {
      throw new SQLException("SQL Exception in createNewRow" + e.getMessage());
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.logErr("SQLException occurred in createNewRow", e);
      }
    }
  } /* end createNewRow */

  private boolean keyExists(int pkey) throws SQLException {
    PreparedStatement pStmt = null;
    ResultSet result = null;

    TestUtil.logTrace("keyExists");

    getDBConnection();

    try {
      String sqlStr = TestUtil.getProperty("BB_Select1");
      TestUtil.logMsg(sqlStr);
      pStmt = dbConnection.prepareStatement(sqlStr);
      pStmt.setInt(1, pkey);
      result = pStmt.executeQuery();
      if (!result.next())
        return false;
      else
        return true;
    } catch (SQLException e) {
      throw new SQLException(
          "Caught SQL Exception in keyExists" + e.getMessage());
    } finally {
      try {
        if (result != null)
          result.close();
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.logErr("SQLException occurred in keyExists", e);
      }
    }
  } /* end keyExists */

  private void removeRow(int pkey) throws SQLException {
    PreparedStatement pStmt = null;

    TestUtil.logTrace("removeRow");

    getDBConnection();

    try {
      String sqlStr = TestUtil.getProperty("BB_Delete1");
      TestUtil.logMsg(sqlStr);
      pStmt = dbConnection.prepareStatement(sqlStr);
      pStmt.setInt(1, pkey);
      if (pStmt.executeUpdate() != 1) {
        throw new SQLException("SQL DELETE failed in removeRow");
      }
    } catch (SQLException e) {
      throw new SQLException("SQL Exception in removeRow:" + e.getMessage());
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.logErr("SQLException occurred in removeRow", e);
      }
    }
  } /* removeRow */

}
