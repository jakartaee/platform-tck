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
 * @(#)TestBeanEJB.java	1.7 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.entitybeantest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;
import java.sql.*;

public abstract class TestBeanEJB implements EntityBean {
  private EntityContext ectx = null;

  private Helper ref = null;

  // Proper lifecycle create call order for EJBObject creation.
  // A client invokes home.create(args ...)
  // - newInstance()
  // - setEntityContext(EntityContext ctx)
  // - ejbCreate(args ...)
  // - ejbPostCreate(args ...)

  private boolean ejbNewInstanceFlag = false;

  private boolean ejbEntityContextFlag = false;

  private boolean ejbCreateFlag = false;

  private boolean ejbPostCreateFlag = false;

  private boolean ejbLoadFlag = false;

  private boolean ejbStoreFlag = false;

  private boolean iAmDestroyed = false;

  private boolean createLifeCycleFlag = true;

  // ===========================================================
  // getters and setters for cmp fields

  public abstract Integer getId();

  public abstract void setId(Integer i);

  public abstract String getBrandName();

  public abstract void setBrandName(String s);

  public abstract float getPrice();

  public abstract void setPrice(float p);

  // ===========================================================

  // newInstance() invokes default no-arg constructor for class
  public TestBeanEJB() {
    TestUtil.logTrace("newInstance => default constructor called");
    ejbNewInstanceFlag = true;
    if (ejbEntityContextFlag || ejbCreateFlag || ejbPostCreateFlag)
      createLifeCycleFlag = false;
  }

  public Integer ejbCreate(int id, String brandName, float price, Helper r)
      throws CreateException, DuplicateKeyException {
    ref = r;
    TestUtil.logTrace("ejbCreate");
    TestUtil.logTrace("ejbNewInstanceFlag=" + ejbNewInstanceFlag
        + "\nejbEntityContextFlag=" + ejbEntityContextFlag + "\nejbCreateFlag="
        + ejbCreateFlag + "\nejbPostCreateFlag=" + ejbPostCreateFlag);
    ejbCreateFlag = true;
    if (!ejbNewInstanceFlag || !ejbEntityContextFlag || ejbPostCreateFlag)
      createLifeCycleFlag = false;
    Integer pk = new Integer(id);
    try {
      setId(pk);
      setBrandName(brandName);
      setPrice(price);

      ref.setCreateLifeCycle(createLifeCycleFlag);
      ref.setCreateMethodCalled(1);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }

    return null;
  }

  public void ejbPostCreate(int id, String brandName, float price, Helper r)
      throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
    ejbPostCreateFlag = true;

    if (!ejbNewInstanceFlag || !ejbEntityContextFlag || !ejbCreateFlag)
      createLifeCycleFlag = false;
  }

  public Integer ejbCreate(int id, Helper r)
      throws CreateException, DuplicateKeyException {
    ref = r;
    TestUtil.logTrace("ejbCreate");
    TestUtil.logTrace("ejbNewInstanceFlag=" + ejbNewInstanceFlag
        + "\nejbEntityContextFlag=" + ejbEntityContextFlag + "\nejbCreateFlag="
        + ejbCreateFlag + "\nejbPostCreateFlag=" + ejbPostCreateFlag);
    ejbCreateFlag = true;
    if (!ejbNewInstanceFlag || !ejbEntityContextFlag || ejbPostCreateFlag)
      createLifeCycleFlag = false;
    Integer pk = new Integer(id);
    try {
      setId(pk);
      setBrandName("unknown");
      setPrice((float) 0.0);

      ref.setCreateLifeCycle(createLifeCycleFlag);
      ref.setCreateMethodCalled(2);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }

    return pk;
  }

  public void ejbPostCreate(int n, Helper r) {
    TestUtil.logTrace("ejbPostCreate");
    ejbPostCreateFlag = true;
    if (!ejbNewInstanceFlag || !ejbEntityContextFlag || !ejbCreateFlag)
      createLifeCycleFlag = false;
  }

  public void setEntityContext(EntityContext c) {
    TestUtil.logTrace("setEntityContext");
    ectx = c;
    ejbEntityContextFlag = true;
    if (!ejbNewInstanceFlag || ejbCreateFlag || ejbPostCreateFlag)
      createLifeCycleFlag = false;
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("unsetEntityContext");
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("ejbRemove");
    try {
      if (ref != null)
        ref.setRemove(true);
    } catch (Exception e) {
      TestUtil.logErr("exception occurred contacting callback bean", e);
      throw new RemoveException("callback not notified");
    }
    reset();
    ref = null;
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
    try {
      if (ref != null)
        ref.setStore(true);
    } catch (Exception e) {
      TestUtil.logErr("exception occurred contacting callback bean", e);
      throw new EJBException("callback not notified");
    }
    ejbStoreFlag = true;
  }

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
    try {
      if (ref != null)
        ref.setLoad(true);
    } catch (Exception e) {
      TestUtil.logErr("exception occurred contacting callback bean", e);
      throw new EJBException("callback not notified");
    }
    ejbLoadFlag = true;
  }

  // ===========================================================
  // TestBean interface (our business methods)

  public void ping() {
    TestUtil.logTrace("ping");
  }

  // Throws RuntimeException
  public void throwEJBException() {
    TestUtil.logTrace("throwEJBException");
    iAmDestroyed = true;
    throw new EJBException("throwing EJBException");
  }

  // Throws Error
  public void throwError() {
    TestUtil.logTrace("throwError");
    iAmDestroyed = true;
    throw new Error("throwing Error");
  }

  public void reset() {
    ejbCreateFlag = false;
    ejbPostCreateFlag = false;
    ejbLoadFlag = false;
    ejbStoreFlag = false;
    createLifeCycleFlag = true;
  }

  public void setHelper(Helper r) {
    TestUtil.logTrace("setHelper");
    ref = r;
  }

  public void loadOrStoreTest(Helper r) {
    TestUtil.logTrace("loadTest");
    try {
      r.setLoad(ejbLoadFlag);
      ref = r;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Caught RemoteException: " + e);
      throw new EJBException(e.getMessage());
    }
  }

  public boolean iAmDestroyed() {
    return iAmDestroyed;
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
}
