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
 * @(#)TxEBeanEJB.java	1.14 03/05/16
 */
package com.sun.ts.tests.ejb.ee.tx.txEbeanLocal;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import java.rmi.*;
import java.sql.*;
import javax.sql.*;
import javax.ejb.*;

public class TxEBeanEJB implements EntityBean {

  private EntityContext ectx = null;

  private TSNamingContext context = null;

  // DataSources
  private DataSource ds1;

  // private class data
  private int key = 0;

  private String brand = null;

  private float price = 0;

  private String tName = null;

  private String tName1 = null;

  // Exception flags
  // FLAGREMOTEEXCEPTION is not used anywhere in this bean.
  public static final int FLAGAPPEXCEPTION = -1;

  public static final int FLAGAPPEXCEPTIONWITHROLLBACK = -2;

  public static final int FLAGSYSEXCEPTION = -3;

  public static final int FLAGREMOTEEXCEPTION = -4;

  public static final int FLAGEJBEXCEPTION = -5;

  public static final int FLAGERROR = -6;

  public static final int FLAGROLLBACK = -7;

  // Required Entity EJB methods
  // ---------------------------------------------------------------------------------
  // public constructor which takes no arguments
  public TxEBeanEJB() {
    TestUtil.logTrace("TxEBeanEJB no arg constructor");
  }

  // ejbCreate() inserts the entity state into the database
  public Integer ejbCreate(String tName, int key, String brand, float price,
      Properties p)
      throws CreateException, DuplicateKeyException, SQLException {
    TestUtil.logTrace("ejbCreate");

    String newName = null;
    Connection con = null;
    PreparedStatement pStmt = null;
    Integer findEJBkey = null;

    this.tName = tName;
    TestUtil.logTrace("tName: " + this.tName);
    TestUtil.logTrace("key: " + key);
    TestUtil.logTrace("price: " + price);

    try {
      initLogging(p);

      // create the table layout, if needed
      if (key == 1)
        createTableLayout();

      // Check if key already exists
      if (keyExists(tName, key)) {
        TestUtil.logTrace("key: " + key + " already exists");
        throw new DuplicateKeyException();
      }

      con = getDBConnection();

      newName = this.tName + "-" + key;
      // String updateString = "insert into " + this.tName + " values(?, ?, ?)";
      String updateString = TestUtil.getProperty("TxEBean_updateString1");
      pStmt = con.prepareStatement(updateString);

      // Perform the insert(s)
      pStmt.setInt(1, key);
      pStmt.setString(2, newName);
      pStmt.setFloat(3, price);
      pStmt.executeUpdate();

      TestUtil.logMsg("New row inserted into table");
      this.key = key;
      this.brand = newName;
      this.price = price;

    } catch (DuplicateKeyException de) {
      TestUtil.printStackTrace(de);
      TestUtil.logTrace("Caught DuplicateKeyException, rethrowing to client");
      throw new DuplicateKeyException();
    } catch (Exception e) {
      TestUtil.logErr("Exception inserting a new row into table:" + this.tName,
          e);
      throw new CreateException(e.getMessage());
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        if (con != null)
          con.close();
      } catch (SQLException se) {
        TestUtil.logErr("SQLException closing db connection for " + this.tName,
            se);
        throw new EJBException(se.getMessage());
      }
    }
    return new Integer(key);
  }

  // The Container invokes ejbPostCreate() immediately after it calls ejbCreate
  // Must match ejbCreate() signature
  public void ejbPostCreate(String tName, int key, String brand, float price,
      Properties p) throws CreateException, SQLException {
    TestUtil.logTrace("ejbPostCreate");
  }

  // ejbRemove() deletes the entity state from the database
  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("ejbRemove");

    Connection con = null;
    PreparedStatement pStmt = null;

    try {
      con = getDBConnection();

      // String updateString = "delete from " + this.tName + " where KEY_ID =
      // ?";
      String updateString = TestUtil.getProperty("TxEBean_updateString2");
      pStmt = con.prepareStatement(updateString);
      int theRow = ((Integer) ectx.getPrimaryKey()).intValue();

      pStmt.setInt(1, theRow);
      pStmt.executeUpdate();
      TestUtil.logMsg("Row deleted from table");

    } catch (Exception e) {
      TestUtil.logErr("Exception deleting row from table: " + this.tName, e);
      throw new RemoveException(e.getMessage());
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        if (con != null)
          con.close();
      } catch (SQLException se) {
        TestUtil.logErr("SQLException closing db connection for " + this.tName,
            se);
        throw new EJBException(se.getMessage());
      }
    }
  }

  // ejbFindtxEbean() locates an Entity EJB
  // Returns at most ONE Entity object
  public Integer ejbFindtxEbean(String tName, Integer key, Properties p)
      throws FinderException, ObjectNotFoundException {
    TestUtil.logTrace("ejbFindtxEbean");

    Connection con = null;
    PreparedStatement pStmt = null;
    ResultSet rs = null;
    this.tName = tName;

    try {
      initLogging(p);

      con = getDBConnection();

      // String selectString = "select KEY_ID from " + this.tName + " where
      // KEY_ID = ?";
      String selectString = TestUtil.getProperty("TxEBean_selectString1");
      pStmt = con.prepareStatement(selectString);

      pStmt.setInt(1, key.intValue());
      rs = pStmt.executeQuery();

      if (rs.next()) {
        TestUtil.logTrace("ejbFindtxEbean for Row key: " + key.toString()
            + " successfully located");
      } else {
        TestUtil
            .logErr("ejbFindtxEbean could not find Row key: " + key.toString());
        throw new ObjectNotFoundException(
            "ejbFindtxEbean could not find Row key: " + key.toString());
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception finding primary key: " + key.toString(), e);
      throw new ObjectNotFoundException(e.getMessage());
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        if (con != null)
          con.close();
      } catch (SQLException se) {
        TestUtil.logErr("SQLException closing db connection for " + this.tName,
            se);
        throw new EJBException(se.getMessage());
      }
    }
    return key;
  }

  public Integer ejbFindByPrimaryKey(Integer key)
      throws FinderException, ObjectNotFoundException {
    TestUtil.logTrace("ejbFindByPrimaryKey");

    Connection con = null;
    PreparedStatement pStmt = null;
    ResultSet rs = null;

    try {

      con = getDBConnection();

      // String selectString = "select KEY_ID from " + this.tName + " where
      // KEY_ID = ?";
      String selectString = TestUtil.getProperty("TxEBean_selectString1");
      pStmt = con.prepareStatement(selectString);

      pStmt.setInt(1, key.intValue());
      rs = pStmt.executeQuery();

      if (rs.next()) {
        TestUtil.logTrace("ejbFindByPrimaryKey for Row key: " + key.toString()
            + " successfully located");
      } else {
        TestUtil.logErr(
            "ejbFindByPrimaryKey could not find Row key: " + key.toString());
        throw new ObjectNotFoundException(
            "ejbFindByPrimaryKey could not find Row key: " + key.toString());
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception finding primary key: " + key.toString(), e);
      throw new ObjectNotFoundException(e.getMessage());
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        if (con != null)
          con.close();
      } catch (SQLException se) {
        TestUtil.logErr("SQLException closing db connection for " + this.tName,
            se);
        throw new EJBException(se.getMessage());
      }
    }
    return key;
  }

  // Returns a Collection of objects of the Entity EJB's primary key type
  public Collection ejbFindByBrandName(String tName, String brandName,
      Properties p) throws FinderException {
    TestUtil.logTrace("ejbFindByBrandName");

    Connection con = null;
    PreparedStatement pStmt = null;
    ResultSet rs = null;
    int primaryKey = 0;
    this.tName = tName;

    try {
      initLogging(p);
      con = getDBConnection();

      // String selectString = "select KEY_ID from " + this.tName + " where
      // BRAND_NAME = ?";
      String selectString = TestUtil.getProperty("TxEBean_selectString2");
      pStmt = con.prepareStatement(selectString);

      pStmt.setString(1, brandName);
      rs = pStmt.executeQuery();
      if (!rs.next()) {
        TestUtil.logTrace(
            "No rows could be found matching brand name: " + brandName);
        throw new FinderException();
      }
      ArrayList arrayOfKeys = new ArrayList();

      while (rs.next()) {
        primaryKey = rs.getInt(1);
        arrayOfKeys.add(new Integer(primaryKey));
        TestUtil
            .logTrace("EJB for brand: " + brandName + " successfully located");
        TestUtil.logTrace("Primary key located is: " + primaryKey);
      }
      return arrayOfKeys;

    } catch (FinderException fe) {
      TestUtil.printStackTrace(fe);
      throw new FinderException(
          "FinderException thrown from ejbFindByBrandName");
    } catch (Exception e) {
      TestUtil.logErr("Exception finding EJB for brand name: " + brandName, e);
      throw new FinderException(e.getMessage());
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        if (con != null)
          con.close();
      } catch (SQLException se) {
        TestUtil.logErr("SQLException closing db connection for " + this.tName,
            se);
        throw new EJBException(se.getMessage());
      }
    }
  }

  // Returns a Collection of objects of the Entity EJB's primary key type
  public Collection ejbFindByPrice(String tName, float price, Properties p)
      throws FinderException {
    TestUtil.logTrace("ejbFindByPrice");

    Connection con = null;
    PreparedStatement pStmt = null;
    ResultSet rs = null;
    int primaryKey = 0;
    this.tName = tName;

    try {
      initLogging(p);
      con = getDBConnection();

      // String selectString = "select KEY_ID from " + this.tName + " where
      // PRICE = ?";
      String selectString = TestUtil.getProperty("TxEBean_selectString3");
      pStmt = con.prepareStatement(selectString);

      pStmt.setFloat(1, price);
      rs = pStmt.executeQuery();
      ArrayList arrayOfKeys = new ArrayList();

      while (rs.next()) {
        primaryKey = rs.getInt(1);
        arrayOfKeys.add(new Integer(primaryKey));
        TestUtil.logTrace("EJB for price: " + price + " successfully located");
        TestUtil.logTrace("Primary key located is: " + primaryKey);
      }
      return arrayOfKeys;

    } catch (Exception e) {
      TestUtil.logErr("Exception finding EJB for price: " + price, e);
      throw new FinderException(e.getMessage());
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        if (con != null)
          con.close();
      } catch (SQLException se) {
        TestUtil.logErr("SQLException closing db connection for " + this.tName,
            se);
        throw new EJBException(se.getMessage());
      }
    }
  }

  public void setEntityContext(EntityContext ec) {
    TestUtil.logTrace("setEntityContext");
    try {
      this.ectx = ec;
      this.context = new TSNamingContext();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception setting EJB context/DataSources",
          e);
      throw new EJBException(e.getMessage());
    }
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("unsetEntityContext");
  }

  // ejbLoad() refreshes the EJB instance variables from the database,
  // the Container automatically does this synchronization.
  public void ejbLoad() throws NoSuchEntityException {
    TestUtil.logTrace("ejbLoad");

    Connection con = null;
    PreparedStatement pStmt = null;
    ResultSet rs = null;

    try {
      // Check to make sure the Entity object exists
      TestUtil.logTrace("Checking for Entity Object");
      int theRow = ((Integer) ectx.getPrimaryKey()).intValue();

      TestUtil.logTrace("Row: " + theRow);
      TestUtil.logTrace("tName: " + this.tName);

      if (!keyExists(this.tName, theRow)) {
        TestUtil.logTrace("Row: " + theRow + " does not exist");
        throw new NoSuchEntityException();
      }
      TestUtil.logTrace("Row " + theRow + " exists");

      con = getDBConnection();

      // String selectString = "select KEY_ID, BRAND_NAME, PRICE " +
      // "from " + this.tName + " where KEY_ID = ?";
      String selectString = TestUtil.getProperty("TxEBean_selectString4");
      pStmt = con.prepareStatement(selectString);
      theRow = ((Integer) ectx.getPrimaryKey()).intValue();

      pStmt.setInt(1, theRow);
      rs = pStmt.executeQuery();

      TestUtil.logTrace("Updating the EJB instance data via ejbLoad()");
      if (rs.next()) {
        this.key = rs.getInt(1);
        this.brand = rs.getString(2);
        this.price = rs.getFloat(3);
      } else {
        throw new EJBException("ejbLoad failed: Row key " + key + " not found");
      }
      TestUtil.logTrace("ejbLoad() successfully located Row key " + key
          + " and updated the EJB instance data!");

    } catch (NoSuchEntityException ne) {
      TestUtil.printStackTrace(ne);
      throw new NoSuchEntityException("NoSuchEntityException from ejbLoad()");
    } catch (Exception e) {
      TestUtil.logErr("Exception from ejbLoad for row: " + this.key, e);
      throw new EJBException(e.getMessage());
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        if (con != null)
          con.close();
      } catch (SQLException se) {
        TestUtil.logErr("SQLException closing db connection for " + this.tName,
            se);
        throw new EJBException(se.getMessage());
      }
    }
  }

  // ejbStore() writes the instance variables to the database,
  // the Container automatically does this synchronization.
  public void ejbStore() {
    TestUtil.logTrace("ejbStore");

    Connection con = null;
    PreparedStatement pStmt = null;

    try {
      con = getDBConnection();

      // String updateString = "update " + this.tName + " set BRAND_NAME = ?,
      // PRICE = ? " +
      // "where KEY_ID = ?";
      String updateString = TestUtil.getProperty("TxEBean_updateString3");
      pStmt = con.prepareStatement(updateString);
      int theRow = ((Integer) ectx.getPrimaryKey()).intValue();

      pStmt.setString(1, this.brand);
      pStmt.setFloat(2, this.price);
      pStmt.setInt(3, theRow);

      pStmt.executeUpdate();
      TestUtil.logTrace("ejbStore() successfully located Row key " + key
          + " and updated the database with the EJB instance data!");

    } catch (Exception e) {
      TestUtil.logErr("Exception from ejbStore for row: " + this.key, e);
      throw new EJBException(e.getMessage());
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        if (con != null)
          con.close();
      } catch (SQLException se) {
        TestUtil.logErr("SQLException closing db connection for " + this.tName,
            se);
        throw new EJBException(se.getMessage());
      }
    }
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
    try {
      this.context = new TSNamingContext();

      // Get the data sources
      ds1 = (DataSource) context.lookup("java:comp/env/jdbc/DB1");
      TestUtil.logTrace("ds1: " + ds1);
      TestUtil.logTrace("DataSource1 lookup OK!");

      // Get the table names
      this.tName1 = TestUtil
          .getTableName(TestUtil.getProperty("TxEBean_Delete"));
      this.tName = this.tName1;

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception setting EJB context/DataSources",
          e);
      throw new EJBException(e.getMessage());
    }
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  // ===========================================================
  // The TxEBean business methods
  // Most of the business methods of the TxEBean class do not access the
  // database.
  // Instead, these business methods update the instance variables, which are
  // written to the database when the EJB Container calls ejbStore().

  public String getBrandName() {
    TestUtil.logTrace("getBrandName");
    return this.brand;
  }

  public float getPrice() {
    TestUtil.logTrace("getPrice");
    return this.price;
  }

  public String getDbBrandName() {
    TestUtil.logTrace("getDbBrandName");

    Connection con = null;
    PreparedStatement pStmt = null;
    ResultSet rs = null;
    String dbBrandName = null;

    try {
      con = getDBConnection();

      // String selectString = "select BRAND_NAME " + "from " + this.tName +
      // " where KEY_ID = ?";
      String selectString = TestUtil.getProperty("TxEBean_selectString5");
      pStmt = con.prepareStatement(selectString);
      int theRow = ((Integer) ectx.getPrimaryKey()).intValue();

      pStmt.setInt(1, theRow);
      rs = pStmt.executeQuery();

      TestUtil.logTrace("Getting the Brand name value from the DB");
      if (rs.next()) {
        dbBrandName = rs.getString(1);
        TestUtil.logTrace("The DB Brand name is " + dbBrandName);
      } else {
        throw new EJBException(
            "getDbBrandName failed: Row key " + this.key + " not found");
      }
      TestUtil.logTrace("getDbBrandName successfully located Row key "
          + this.key + " and obtained the database Brand name data!");
      return dbBrandName;

    } catch (Exception e) {
      TestUtil.logErr("Exception from getDbPrice for row: " + this.key, e);
      throw new EJBException(e.getMessage());
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        if (con != null)
          con.close();
      } catch (SQLException se) {
        TestUtil.logErr("SQLException closing db connection for " + this.tName,
            se);
        throw new EJBException(se.getMessage());
      }
    }
  }

  public float getDbPrice() {
    TestUtil.logTrace("getDbPrice");

    Connection con = null;
    PreparedStatement pStmt = null;
    ResultSet rs = null;
    float dbPrice = 0;

    try {
      con = getDBConnection();

      // String selectString = "select PRICE " + "from " + this.tName +
      // " where KEY_ID = ?";
      String selectString = TestUtil.getProperty("TxEBean_selectString6");
      pStmt = con.prepareStatement(selectString);
      int theRow = ((Integer) ectx.getPrimaryKey()).intValue();

      pStmt.setInt(1, theRow);
      rs = pStmt.executeQuery();

      TestUtil.logTrace("Getting the Price value from the DB");
      if (rs.next()) {
        dbPrice = rs.getFloat(1);
        TestUtil.logTrace("The DB Price is " + dbPrice);
      } else {
        throw new EJBException(
            "getDbPrice failed: Row key " + this.key + " not found");
      }
      TestUtil.logTrace("getDbPrice successfully located Row key " + this.key
          + " and obtained the database Price data!");
      return dbPrice;

    } catch (Exception e) {
      TestUtil.logErr("Exception from getDbPrice for row: " + this.key, e);
      throw new EJBException(e.getMessage());
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        if (con != null)
          con.close();
      } catch (SQLException se) {
        TestUtil.logErr("SQLException closing db connection for " + this.tName,
            se);
        throw new EJBException(se.getMessage());
      }
    }
  }

  public void updateBrandName(String newBrandName) {
    TestUtil.logTrace("updateBrandName");

    // Check to make sure that the Entity EJB exists.
    try {
      TestUtil.logTrace("Checking for Entity Object");
      int theRow = ((Integer) ectx.getPrimaryKey()).intValue();

      TestUtil.logTrace("Row: " + theRow);
      TestUtil.logTrace("tName: " + this.tName);

      if (!keyExists(this.tName, theRow)) {
        TestUtil.logTrace("Row: " + theRow + " does not exist");
        throw new NoSuchEntityException();
      }
      TestUtil.logTrace("Row " + theRow + " exists");

      TestUtil.logTrace("Updating the brand name");
      this.brand = newBrandName;

    } catch (NoSuchEntityException ne) {
      TestUtil.printStackTrace(ne);
      throw new NoSuchEntityException(
          "NoSuchEntityException from updateBrandName()");
    } catch (Exception e) {
      TestUtil.logErr("Exeption from updateBrandName()", e);
      throw new EJBException(e.getMessage());
    }
  }

  public boolean updateBrandName(String newBrandName, int flag)
      throws AppException {
    TestUtil.logTrace("updateBrandName w/ Exception");
    boolean isRolledback = false;
    try {
      this.brand = newBrandName;

      // Check for intended exceptions to be thrown
      if (flag == FLAGAPPEXCEPTION)
        throwAppException();

      if (flag == FLAGAPPEXCEPTIONWITHROLLBACK) {
        TestUtil.logTrace("calling setRollbackOnly");
        ectx.setRollbackOnly();
        TestUtil.logTrace("Calling getRollbackOnly method");
        if (ectx.getRollbackOnly())
          isRolledback = true;
        throwAppException();
      }

      if (flag == FLAGSYSEXCEPTION) {
        throwSysException();
      }

      if (flag == FLAGEJBEXCEPTION) {
        throw new EJBException("EJBException from updateBrandName()");
      }

      if (flag == FLAGERROR) {
        throw new Error("Error from updateBrandName()");
      }

      if (flag == FLAGROLLBACK) {
        TestUtil.logTrace("Calling setRollbackOnly method");
        ectx.setRollbackOnly();
        TestUtil.logTrace("Calling getRollbackOnly method");
        if (ectx.getRollbackOnly())
          isRolledback = true;
      }

    } catch (AppException e) {
      TestUtil.printStackTrace(e);
      throw new AppException("AppException from updateBrandName()");
    } catch (Exception e) {
      TestUtil.logErr("Exeption from updateBrandName()", e);
      throw new EJBException(e.getMessage());
    }
    return isRolledback;
  }

  public boolean updateBrandNameRB(String newBrandName, int flag)
      throws AppException {
    TestUtil.logTrace("updateBrandNameRB");
    boolean isRolledback = false;
    try {
      this.brand = newBrandName;

      // Check for intended exceptions to be thrown
      if (flag == FLAGAPPEXCEPTIONWITHROLLBACK) {
        TestUtil.logTrace("calling setRollbackOnly");
        ectx.setRollbackOnly();
        TestUtil.logTrace("Calling getRollbackOnly method");
        if (ectx.getRollbackOnly()) {
          isRolledback = true;
          TestUtil.logTrace("Tx isRolledBack " + isRolledback);
        }
        throwAppException();
      }

      if (flag == FLAGROLLBACK) {
        TestUtil.logTrace("Calling setRollbackOnly method");
        ectx.setRollbackOnly();
        TestUtil.logTrace("Calling getRollbackOnly method");
        if (ectx.getRollbackOnly()) {
          isRolledback = true;
          TestUtil.logTrace("Tx isRolledBack " + isRolledback);
        }
      }

    } catch (AppException e) {
      TestUtil.printStackTrace(e);
      throw new AppException("AppException from updateBrandName()");
    } catch (Exception e) {
      TestUtil.logErr("Exeption from updateBrandName()", e);
      throw new EJBException(e.getMessage());
    }
    return isRolledback;
  }

  public void updatePrice(float newPriceName) {
    TestUtil.logTrace("updatePrice");
    this.price = newPriceName;
  }

  public void throwAppException() throws AppException {
    TestUtil.logTrace("throwAppException");
    throw new AppException("AppException from TxEBean");
  }

  public void throwSysException() {
    TestUtil.logTrace("throwSysException");
    throw new SysException("SysException from TxEBean");
  }

  public void throwEJBException() {
    TestUtil.logTrace("throwEJBException");
    throw new EJBException("EJBException from TxEBean");
  }

  public void throwError() {
    TestUtil.logTrace("throwError");
    throw new Error("Error from TxEBean");
  }

  public void throwRemoveException() throws RemoveException {
    TestUtil.logTrace("throwRemoveException");
    throw new RemoveException("RemoveException from TxEBean");
  }

  // ===========================================================
  // Private methods

  private void initLogging(Properties p) throws Exception {
    TestUtil.logTrace("initLogging");
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new Exception(e.getMessage());
    }
  }

  private Connection getDBConnection() throws SQLException {
    TestUtil.logTrace("getDBConnection");
    initSetup();
    Connection con = null;
    try {
      TestUtil.logTrace("tName: " + this.tName);

      con = ds1.getConnection();

      TestUtil.logTrace("Made the JDBC connection to " + this.tName);
    } catch (Exception e) {
      TestUtil.logErr("Exception connecting to database for " + this.tName, e);
      throw new EJBException(e.getMessage());
    }
    TestUtil.logTrace("con: " + con.toString());
    return con;
  }

  private void createTableLayout() throws SQLException {
    TestUtil.logTrace("createTableLayout");

    Connection con = null;
    Statement stmt = null;
    try {
      con = getDBConnection();

      // drop the table if it exists
      stmt = con.createStatement();
      String dropString = TestUtil.getProperty("TxEBean_Delete");
      stmt.executeUpdate(dropString);
      TestUtil.logMsg("Table " + this.tName + " cleared");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg(this.tName1 + " encountered problem clearing rows!");
      throw new EJBException(e.getMessage());
    } finally {
      try {
        if (stmt != null)
          stmt.close();
        if (con != null)
          con.close();
      } catch (SQLException se) {
        TestUtil.printStackTrace(se);
      }
    }

  }

  private boolean keyExists(String tName, int key) throws SQLException {
    TestUtil.logTrace("keyExists");
    Connection con = null;
    Statement stmt = null;
    PreparedStatement pStmt = null;
    boolean b = false;

    try {
      con = getDBConnection();
      // stmt = con.createStatement();
      // String queryStr = "select KEY_ID from " + tName + " where KEY_ID = " +
      // key;

      // ResultSet result = stmt.executeQuery(queryStr);

      String queryStr = TestUtil.getProperty("TxEBean_queryStr1");
      pStmt = con.prepareStatement(queryStr);

      pStmt.setInt(1, key);
      ResultSet result;
      result = pStmt.executeQuery();

      if (result.next())
        b = true;

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logTrace("Exception from keyExists: " + e.getMessage());
    } finally {
      try {
        if (pStmt != null)
          pStmt.close();
        if (con != null)
          con.close();
      } catch (SQLException se) {
        TestUtil.logErr("SQLException closing db connection for " + this.tName,
            se);
        throw new EJBException(se.getMessage());
      }
    }
    return b;
  }

  private void initSetup() {
    try {

      // Get the data sources
      ds1 = (DataSource) context.lookup("java:comp/env/jdbc/DB1");
      TestUtil.logTrace("ds1: " + ds1);
      TestUtil.logTrace("DataSource1 lookup OK!");

      // Get the table names
      this.tName1 = TestUtil
          .getTableName(TestUtil.getProperty("TxEBean_Delete"));
      TestUtil.logTrace("tName1: " + this.tName1);
      this.tName = this.tName1;
    } catch (Exception e) {
      TestUtil.logErr("Create exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
  }

}
