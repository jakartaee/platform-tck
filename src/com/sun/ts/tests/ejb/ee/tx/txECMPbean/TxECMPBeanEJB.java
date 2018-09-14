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
 * @(#)TxECMPBeanEJB.java	1.24 03/05/16
 */
package com.sun.ts.tests.ejb.ee.tx.txECMPbean;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import java.rmi.*;
import java.sql.*;
import javax.sql.*;
import javax.ejb.*;

public class TxECMPBeanEJB implements EntityBean {

  // testProps represent the test specific properties passed in
  // from the test harness.
  private Properties testProps = null;

  private EntityContext ectx = null;

  private TSNamingContext context = null;

  // public instance data
  public Integer KEY_ID = new Integer(0);

  public String BRAND_NAME = null;

  public float PRICE = 0;

  // Exception flags
  public static final int FLAGAPPEXCEPTION = -1;

  public static final int FLAGAPPEXCEPTIONWITHROLLBACK = -2;

  public static final int FLAGSYSEXCEPTION = -3;

  public static final int FLAGREMOTEEXCEPTION = -4;

  public static final int FLAGEJBEXCEPTION = -5;

  public static final int FLAGERROR = -6;

  public static final int FLAGROLLBACK = -7;

  // Required Entity EJB methods
  // ------------------------------------------------------------------------
  // public constructor which takes no arguments
  public TxECMPBeanEJB() {
    TestUtil.logTrace("TxECMPBeanEJB no arg constructor");
  }

  // ejbCreate() inserts the entity state into the database
  public Integer ejbCreate(String tName, Integer key, String brand, float price,
      Properties p)
      throws CreateException, DuplicateKeyException, SQLException {

    TestUtil.logMsg("ejbCreate");

    TestUtil.logTrace("KEY_ID: " + key);
    TestUtil.logTrace("BRAND_NAME: " + brand);
    TestUtil.logTrace("PRICE: " + price);

    try {
      initLogging(p);
      this.KEY_ID = key;
      this.BRAND_NAME = brand;
      this.PRICE = price;

    } catch (Exception e) {
      TestUtil.logErr("Exception inserting a new row into table", e);
      throw new CreateException(e.getMessage());
    } finally {
      // Because this is a container managed Entity bean there isn't much
      // that needs to be do manually.
    }
    return this.KEY_ID;
  }

  // The Container invokes ejbPostCreate() immediately after it calls ejbCreate
  // Must match ejbCreate() signature
  public void ejbPostCreate(String tName, Integer key, String brand,
      float price, Properties p) throws CreateException, SQLException {
    TestUtil.logTrace("ejbPostCreate");
  }

  public void setEntityContext(EntityContext c) {
    TestUtil.logTrace("setEntityContext");
    ectx = c;
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("unsetEntityContext");
  }

  // ejbRemove() deletes the entity state from the database
  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("ejbRemove");

  }

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
  }

  // ejbStore() writes the instance variables to the database,
  // the Container automatically does this synchronization.
  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  // ===========================================================
  // The TxECMPBean business methods
  // Most of the business methods of the TxECMPBean class do not access the
  // database.
  // Instead, these business methods update the instance variables, which are
  // written to the database when the EJB Container calls ejbStore().

  public String getBrandName() {
    TestUtil.logMsg("getBrandName");
    return this.BRAND_NAME;
  }

  public float getPrice() {
    TestUtil.logMsg("getPrice");
    return this.PRICE;
  }

  public void updateBrandName(String newBrandName) {
    TestUtil.logMsg("updateBrandName");
    this.BRAND_NAME = newBrandName;
  }

  public boolean updateBrandName(String newBrandName, int flag)
      throws AppException {
    TestUtil.logTrace("updateBrandName w/ Exception");
    boolean isRolledback = false;
    try {
      this.BRAND_NAME = newBrandName;

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

      if (flag == FLAGREMOTEEXCEPTION) {
        throw new RemoteException("RemoteException from updateBrandName()");
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
      this.BRAND_NAME = newBrandName;

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
    this.PRICE = newPriceName;
  }

  public void throwAppException() throws AppException {
    TestUtil.logTrace("throwAppException");
    throw new AppException("AppException from TxECMPBean");
  }

  public void throwSysException() {
    TestUtil.logTrace("throwSysException");
    throw new SysException("SysException from TxECMPBean");
  }

  public void throwEJBException() {
    TestUtil.logTrace("throwEJBException");
    throw new EJBException("EJBException from TxECMPBean");
  }

  public void throwError() {
    TestUtil.logTrace("throwError");
    throw new Error("Error from TxECMPBean");
  }

  public void throwRemoveException() throws RemoveException {
    TestUtil.logTrace("throwRemoveException");
    throw new RemoveException("RemoveException from TxEBean");
  }

  // ===========================================================
  // Private methods

  private void initLogging(Properties p) throws Exception {
    TestUtil.logTrace("initLogging");
    this.testProps = p;
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new Exception(e.getMessage());
    }
  }
}
