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
 * @(#)DBSupport2.java	1.11 03/05/16
 */

package com.sun.ts.tests.ejb.ee.sec.bmp.util;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.naming.*;
import javax.ejb.*;
import java.rmi.*;
import java.sql.*;
import javax.sql.DataSource;
import javax.transaction.UserTransaction;

public class DBSupport2 implements java.io.Serializable {

  private static final String DATASOURCE2 = "java:comp/env/jdbc/DB1";

  private transient Connection dbConnection = null;

  private EntityContext ectx = null;

  private SessionContext sctx = null;

  private TSNamingContext nctx = null;

  private UserTransaction ut = null;

  private static String user1 = "user1";

  private static String password1 = "password1";

  private String ctsuser = null; // TS1 username

  private String ctspassword = null; // TS1 password

  private String dsname1 = null; // TS2 dataSourcename

  private DataSource ds1 = null; // TS2 dataSource

  private int cofID = 0; // Coffee ID (Primary Key)

  private String cofName = null; // Coffee Name

  private float cofPrice = 0; // Coffee Price

  private boolean debug = true; // For debug

  public DBSupport2(EntityContext ectx) throws Exception {
    this(ectx, null);
  }

  public DBSupport2(EntityContext ectx, Properties p) throws Exception {

    TestUtil.logTrace("DBSupport2");

    this.ectx = ectx;
    if (debug)
      TestUtil.logMsg("Initializing DBSupport2 for an Entity Bean");

    try {
      if (debug)
        TestUtil.logMsg("Get naming context");
      this.nctx = new TSNamingContext();
    } catch (NamingException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Unable to get naming context");
    }

    // Lookup DataSource DB1 from JNDI
    if (debug)
      TestUtil.logMsg("Lookup DataSource DB1 from JNDI : " + DATASOURCE2);
    try {
      this.dsname1 = DATASOURCE2;
      this.ds1 = (DataSource) nctx.lookup(this.dsname1);
      if (debug)
        TestUtil.logMsg("dsname1=" + this.dsname1);
      if (debug)
        TestUtil.logMsg("ds1=" + this.ds1);
    } catch (NamingException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Unable to lookup " + DATASOURCE2);
    }
  } /* end DBSupport2 */

  public void tableInit() throws SQLException {
    Statement stmt = null;

    TestUtil.logTrace("tableInit");

    getDBConnection();

    try {
      stmt = dbConnection.createStatement();
      String sqlStr = TestUtil.getProperty("SEC_Tab1_Delete");
      TestUtil.logMsg(
          "Deleting all rows from " + TestUtil.getTableName(sqlStr) + " table");
      TestUtil.logMsg("SQL = '" + sqlStr + "'");
      stmt.executeUpdate(sqlStr);
      TestUtil.logMsg("Deletion complete!");
    } catch (SQLException s) {
      TestUtil.printStackTrace(s);
      /* if we can't delete all the rows, terminate */
      throw new SQLException("SQL Exception while removing all rows "
          + TestUtil.getTableName(TestUtil.getProperty("SEC_Tab1_Delete"))
          + " table in tableInit:" + s);
    } finally {
      try {
        if (null != stmt) {
          stmt.close();
        }
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.logTrace("Ignoring Exception (cleanup): " + e);
      }
    }
  } /* end tableInit */

  public void tableDrop() throws SQLException {
    TestUtil.logTrace("tableDrop");
    tableInit();
  } /* end tableDrop */

  public void tablePopulate(int cofID[], float cofPrice[], String cofName[])
      throws SQLException {
    PreparedStatement pStmt = null;

    TestUtil.logTrace("tablePopulate");

    getDBConnection();

    tableInit();

    TestUtil.logMsg("Inserting " + cofID.length + " rows of data into table");

    try {
      String sqlStr = TestUtil.getProperty("SEC_Insert1");
      TestUtil.logMsg(sqlStr);
      pStmt = dbConnection.prepareStatement(sqlStr);
      for (int i = 0; i < cofID.length; i++) {
        pStmt.setInt(1, cofID[i]);
        pStmt.setFloat(2, cofPrice[i]);
        pStmt.setString(3, cofName[i]);
        if (pStmt.executeUpdate() != 1) {
          throw new SQLException("SQL INSERT failed in tablePopulate");
        }
      }
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new SQLException("SQL Exception in tablePopulate");
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  } /* end tablePopulate */

  public boolean keyExists(int pkey) throws SQLException {
    PreparedStatement pStmt = null;
    ResultSet result = null;

    TestUtil.logTrace("keyExists");

    getDBConnection();

    try {
      String sqlStr = TestUtil.getProperty("SEC_Select1");
      TestUtil.logMsg(sqlStr);
      pStmt = dbConnection.prepareStatement(sqlStr);
      pStmt.setInt(1, pkey);
      result = pStmt.executeQuery();
      if (!result.next())
        return false;
      else
        return true;
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new SQLException("SQL Exception in keyExists");
    } finally {
      try {
        if (result != null)
          result.close();
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  } /* end keyExists */

  public Collection nameToKeyCollection(String name) throws SQLException {
    PreparedStatement pStmt = null;
    ResultSet result = null;

    TestUtil.logTrace("nameToKey");

    getDBConnection();

    try {
      String sqlStr = TestUtil.getProperty("SEC_Select2");
      TestUtil.logMsg(sqlStr);
      pStmt = dbConnection.prepareStatement(sqlStr);
      pStmt.setString(1, name);
      result = pStmt.executeQuery();
      ArrayList a = new ArrayList();
      while (result.next()) {
        Integer pkey = new Integer(result.getInt(1));
        a.add(pkey);
      }
      return a;
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new SQLException("SQL Exception in nameToKey");
    } finally {
      try {
        if (result != null)
          result.close();
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  } /* end nameToKey */

  public Collection priceToKeyCollection(float price) throws SQLException {
    PreparedStatement pStmt = null;
    ResultSet result = null;

    TestUtil.logTrace("priceToKey");

    getDBConnection();

    try {
      String sqlStr = TestUtil.getProperty("SEC_Select3");
      TestUtil.logMsg(sqlStr);
      pStmt = dbConnection.prepareStatement(sqlStr);
      pStmt.setFloat(1, price);
      result = pStmt.executeQuery();
      ArrayList a = new ArrayList();
      while (result.next()) {
        Integer pkey = new Integer(result.getInt(1));
        a.add(pkey);
      }
      return a;
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new SQLException("SQL Exception in priceToKey");
    } finally {
      try {
        if (result != null)
          result.close();
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  } /* end priceToKey */

  public Collection priceRangeToCollection(float min, float max)
      throws SQLException {
    PreparedStatement pStmt = null;
    ResultSet result = null;

    TestUtil.logTrace("priceRangeToCollection");

    getDBConnection();

    try {
      String sqlStr = TestUtil.getProperty("SEC_Select4");
      TestUtil.logMsg(sqlStr);
      pStmt = dbConnection.prepareStatement(sqlStr);
      pStmt.setFloat(1, min);
      pStmt.setFloat(2, max);
      result = pStmt.executeQuery();
      ArrayList a = new ArrayList();
      while (result.next()) {
        Integer pkey = new Integer(result.getInt(1));
        a.add(pkey);
      }
      return a;
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new SQLException("SQL Exception in priceRangeToCollection");
    } finally {
      try {
        if (result != null)
          result.close();
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  } /* end priceRangeToCollection */

  public Collection primaryKeyRangeToCollection(Integer min, Integer max)
      throws SQLException {
    PreparedStatement pStmt = null;
    ResultSet result = null;

    TestUtil.logTrace("primaryKeyRangeToCollection");

    getDBConnection();

    try {
      String sqlStr = TestUtil.getProperty("SEC_Select5");
      TestUtil.logMsg(sqlStr);
      pStmt = dbConnection.prepareStatement(sqlStr);
      pStmt.setInt(1, min.intValue());
      pStmt.setInt(2, max.intValue());
      result = pStmt.executeQuery();
      ArrayList a = new ArrayList();
      while (result.next()) {
        Integer pkey = new Integer(result.getInt(1));
        a.add(pkey);
      }
      return a;
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new SQLException("SQL Exception in primaryKeyRangeToCollection");
    } finally {
      try {
        if (result != null)
          result.close();
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  } /* end primaryKeyRangeToCollection */

  public void createNewRow(int cofID, float cofPrice, String cofName)
      throws CreateException, SQLException {
    PreparedStatement pStmt = null;

    TestUtil.logTrace("createNewRow");

    getDBConnection();

    try {
      String sqlStr = TestUtil.getProperty("SEC_Insert1");
      TestUtil.logMsg(sqlStr);
      pStmt = dbConnection.prepareStatement(sqlStr);
      pStmt.setInt(1, cofID);
      pStmt.setFloat(2, cofPrice);
      pStmt.setString(3, cofName);
      if (pStmt.executeUpdate() != 1) {
        throw new CreateException("SQL INSERT failed in createNewRow");
      } else {
        this.cofID = cofID;
        this.cofName = cofName;
        this.cofPrice = cofPrice;
      }
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new SQLException("SQL Exception in createNewRow:" + e);
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  } /* end createNewRow */

  public float loadPrice(int pkey) throws SQLException {
    PreparedStatement pStmt = null;
    ResultSet result = null;

    TestUtil.logTrace("loadPrice");

    getDBConnection();

    try {
      String sqlStr = TestUtil.getProperty("SEC_Select6");
      TestUtil.logMsg(sqlStr);
      pStmt = dbConnection.prepareStatement(sqlStr);
      pStmt.setInt(1, pkey);
      result = pStmt.executeQuery();
      if (!result.next()) {
        throw new SQLException(
            "SQL SELECT failed: no record for primary key = " + pkey);
      }
      return result.getFloat(1);
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new SQLException("SQL Exception in loadPrice");
    } finally {
      try {
        if (result != null)
          result.close();
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  } /* loadPrice */

  public void storePrice(int pkey, float cofPrice) throws SQLException {
    PreparedStatement pStmt = null;

    TestUtil.logTrace("storePrice");

    getDBConnection();

    try {
      String sqlStr = TestUtil.getProperty("SEC_Update1");
      TestUtil.logMsg(sqlStr);
      pStmt = dbConnection.prepareStatement(sqlStr);
      pStmt.setFloat(1, cofPrice);
      pStmt.setInt(2, pkey);
      if (pStmt.executeUpdate() != 1) {
        throw new SQLException("SQL UPDATE failed in storePrice");
      } else {
        this.cofPrice = cofPrice;
      }
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new SQLException("SQL Exception in storePrice");
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  } /* storePrice */

  public void removeRow(int pkey) throws SQLException {
    PreparedStatement pStmt = null;

    TestUtil.logTrace("removeRow");

    getDBConnection();

    try {
      String sqlStr = TestUtil.getProperty("SEC_Delete1");
      TestUtil.logMsg(sqlStr);
      pStmt = dbConnection.prepareStatement(sqlStr);
      pStmt.setInt(1, pkey);
      if (pStmt.executeUpdate() != 1) {
        throw new SQLException("SQL DELETE failed in removeRow");
      }
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new SQLException("SQL Exception in removeRow");
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  } /* removeRow */

  public void getDBConnection() throws SQLException {
    TestUtil.logTrace("getDBConnection");
    ctsuser = TestUtil.getProperty(user1);
    ctspassword = TestUtil.getProperty(password1);

    getDBConnection(ctsuser, ctspassword);

  }

  public void getDBConnection(String user, String password)
      throws SQLException {
    TestUtil.logTrace("getDBConnection with user and password");
    // Ignore username password sent from client,
    // use the default user, password from runtime.xml
    // This change is due to the fact that tests are using
    // ContainerManaged Sign-on instead of Application Managed sign-on
    // if(dbConnection == null)
    // dbConnection = ds1.getConnection(user, password);
    if (dbConnection == null)
      dbConnection = ds1.getConnection();

  } // end getDBConnection

  public void closeDBConnection() throws SQLException {
    TestUtil.logTrace("closeDBConnection");
    if (dbConnection != null) {
      dbConnection.close();
      dbConnection = null;
    }
  } // end closeDBConnection

  public void beginUserTransaction() throws Exception {
    ut.begin();
  } // end beginUserTransaction

  public void commitUserTransaction() throws Exception {
    ut.commit();
  } // end commitUserTransaction
}
