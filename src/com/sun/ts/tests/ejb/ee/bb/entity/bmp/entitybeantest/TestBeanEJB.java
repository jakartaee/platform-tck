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

package com.sun.ts.tests.ejb.ee.bb.entity.bmp.entitybeantest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.naming.*;
import javax.ejb.*;
import java.rmi.*;
import com.sun.ts.tests.common.dao.DAOException;
import com.sun.ts.tests.common.dao.DAOFactory;
import com.sun.ts.tests.common.dao.coffee.CoffeeBean;
import com.sun.ts.tests.common.dao.coffee.CoffeeDAO;

public class TestBeanEJB implements EntityBean {
  // Cached instance state
  private CoffeeBean cache;

  private CoffeeDAO dao = null;

  private EntityContext ectx = null;

  private TSNamingContext nctx = null;

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

  float cofPrice = 0; // Instance data for Coffee Price

  // newInstance() invokes default no-arg constructor for class
  public TestBeanEJB() {
    TestUtil.logTrace("newInstance => default constructor called");
    ejbNewInstanceFlag = true;
    if (ejbEntityContextFlag || ejbCreateFlag || ejbPostCreateFlag)
      createLifeCycleFlag = false;
  }

  public Integer ejbCreate(Properties p, int cofID, String cofName,
      float cofPrice, Helper r) throws CreateException, DuplicateKeyException {
    ref = r;
    TestUtil.logTrace("ejbCreate");
    TestUtil.logTrace("ejbNewInstanceFlag=" + ejbNewInstanceFlag
        + "\nejbEntityContextFlag=" + ejbEntityContextFlag + "\nejbCreateFlag="
        + ejbCreateFlag + "\nejbPostCreateFlag=" + ejbPostCreateFlag);
    ejbCreateFlag = true;
    if (!ejbNewInstanceFlag || !ejbEntityContextFlag || ejbPostCreateFlag)
      createLifeCycleFlag = false;
    try {
      TestUtil.logMsg("Initialize remote logging");
      TestUtil.init(p);
      TestUtil.logMsg("DAO Init");
      if (null == dao) {
        dao = DAOFactory.getInstance().getCoffeeDAO();
      }
      TestUtil.logMsg("Get DB Connection");
      dao.startSession();

      if (dao.exists(cofID)) {
        TestUtil.logErr("key exists - duplicate");
        throw new DuplicateKeyException();
      } else
        TestUtil.logMsg("key does not exist - create entity");

      TestUtil.logTrace("DAO: Create new row...");
      dao.create(cofID, cofName, cofPrice);
      this.cache = new CoffeeBean(cofID, cofName, cofPrice);
    } catch (DuplicateKeyException de) {
      TestUtil.printStackTrace(de);
      throw new DuplicateKeyException();
    } catch (RemoteLoggingInitException re) {
      TestUtil.printStackTrace(re);
      throw new CreateException(re.getMessage());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }

    try {
      ref.setCreateLifeCycle(createLifeCycleFlag);
      ref.setCreateMethodCalled(1);
    } catch (Exception e) {
      TestUtil.logErr("Caught Exception: " + e, e);
      throw new CreateException("Exception occurred: " + e);
    }

    return new Integer(cofID);
  }

  public void ejbPostCreate(Properties p, int cofID, String cofName,
      float cofPrice, Helper r) {
    TestUtil.logTrace("ejbPostCreate");
    ejbPostCreateFlag = true;
    if (!ejbNewInstanceFlag || !ejbEntityContextFlag || !ejbCreateFlag)
      createLifeCycleFlag = false;
  }

  public Integer ejbCreate(Properties p, int cofID, Helper r)
      throws CreateException, DuplicateKeyException {
    ref = r;
    TestUtil.logTrace("ejbCreate");
    TestUtil.logTrace("ejbNewInstanceFlag=" + ejbNewInstanceFlag
        + "\nejbEntityContextFlag=" + ejbEntityContextFlag + "\nejbCreateFlag="
        + ejbCreateFlag + "\nejbPostCreateFlag=" + ejbPostCreateFlag);
    ejbCreateFlag = true;
    if (!ejbNewInstanceFlag || !ejbEntityContextFlag || ejbPostCreateFlag)
      createLifeCycleFlag = false;
    try {
      TestUtil.logMsg("initialize remote logging");
      TestUtil.init(p);

      TestUtil.logMsg("DAO Init");
      if (null == dao) {
        dao = DAOFactory.getInstance().getCoffeeDAO();
      }
      dao.startSession();

      TestUtil.logMsg("Get DB Connection");
      if (dao.exists(cofID)) {
        TestUtil.logErr("key exists - duplicate");
        throw new DuplicateKeyException();
      } else
        TestUtil.logMsg("key does not exist - create entity");

      TestUtil.logTrace("DAO: Create new row...");
      dao.create(cofID, "unknown", 0);
      this.cache = new CoffeeBean(cofID, "unknown", 0);
    } catch (DuplicateKeyException de) {
      TestUtil.printStackTrace(de);
      throw new DuplicateKeyException();
    } catch (RemoteLoggingInitException re) {
      TestUtil.printStackTrace(re);
      throw new CreateException(re.getMessage());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }

    try {
      ref.setCreateLifeCycle(createLifeCycleFlag);
      ref.setCreateMethodCalled(2);
    } catch (Exception e) {
      TestUtil.logErr("Caught Exception: " + e, e);
      throw new CreateException("Exception occurred: " + e);
    }

    return new Integer(cofID);
  }

  public void ejbPostCreate(Properties p, int n, Helper r) {
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
    try {
      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();
    } catch (NamingException e) {
      TestUtil.logErr("NamingException ... " + e, e);
      throw new EJBException("unable to obtain naming context");
    } catch (Exception e) {
      TestUtil.logErr("Exception ... " + e, e);
      throw new EJBException("Exception in setEntityContext");
    }
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
      TestUtil.logErr("Exception occurred contacting callback bean", e);
      throw new RemoveException("callback not notified");
    }
    ref = null;
    reset();
    try {
      if (null == dao) {
        TestUtil.logMsg("get DAO...");
        dao = DAOFactory.getInstance().getCoffeeDAO();
      }
      TestUtil.logTrace("Start DAO session...");
      dao.startSession();
      TestUtil.logTrace("Remove row...");
      dao.delete(((Integer) ectx.getPrimaryKey()).intValue());
    } catch (DAOException e) {
      throw new RemoveException("Caught DAOException" + e);
    } catch (Exception e) {
      throw new RemoveException("Caught exception: " + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }

  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
    ejbStoreFlag = false;
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
    reset();
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
    ejbStoreFlag = true;
    try {
      if (null == dao) {
        TestUtil.logMsg("Get DAO...");
        dao = DAOFactory.getInstance().getCoffeeDAO();
      }
      TestUtil.logTrace("Start DAO session...");
      dao.startSession();
      TestUtil.logTrace("Store row...");
      dao.store(cache);
    } catch (DAOException de) {
      TestUtil.logErr("No such entity: " + de);
      throw new NoSuchEntityException("[ejbStore] DAOException" + de);
    } catch (Exception e) {
      throw new EJBException("[ejbStore] Unable to init DAO");
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
      dao = null;
    }

    try {
      if (ref != null)
        ref.setStore(true);
    } catch (Exception e) {
      TestUtil.logErr("exception occurred contacting callback bean", e);
      throw new EJBException("callback not notified");
    }
  }

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
    ejbLoadFlag = true;
    try {
      if (null == dao) {
        TestUtil.logMsg("Get DAO");
        dao = DAOFactory.getInstance().getCoffeeDAO();
      }
      TestUtil.logTrace("Start DAO session...");
      dao.startSession();
      TestUtil.logTrace("Load row...");
      this.cache = dao.load(((Integer) ectx.getPrimaryKey()).intValue());
    } catch (DAOException e) {
      TestUtil.logErr("No such entity exists: " + e);
      throw new NoSuchEntityException("[ejbload] DAOException" + e);
    } catch (Exception e) {
      throw new EJBException("[ejbload] Unable to init DAO " + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }

    try {
      if (ref != null)
        ref.setLoad(true);
    } catch (Exception e) {
      TestUtil.logErr("exception occurred contacting callback bean", e);
      throw new EJBException("callback not notified");
    }
  }

  public Integer ejbFindTheBean(Properties p, Integer key)
      throws FinderException {
    TestUtil.logTrace("ejbFindTheBean");
    try {
      if (null == dao) {
        TestUtil.logMsg("Get DAO...");
        dao = DAOFactory.getInstance().getCoffeeDAO();
      }
      dao.startSession();
      if (dao.exists(key.intValue())) {
        return key;
      } else {
        throw new FinderException("Key not found: " + key);
      }
    } catch (DAOException de) {
      throw new FinderException("DAOException " + de);
    } catch (Exception e) {
      throw new FinderException("Exception occurred: " + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }

  }

  public Integer ejbFindByPrimaryKey(Integer key) throws FinderException {
    TestUtil.logTrace("ejbFindByPrimaryKey");

    try {
      if (null == dao) {
        TestUtil.logMsg("Get DAO...");
        dao = DAOFactory.getInstance().getCoffeeDAO();
      }
      dao.startSession();
      if (dao.exists(key.intValue())) {
        return key;
      } else {
        throw new FinderException("Key not found: " + key);
      }
    } catch (DAOException de) {
      throw new FinderException("DAOException " + de);
    } catch (Exception e) {
      throw new FinderException("Exception occurred: " + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }

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
    TestUtil.logTrace("loadOrStoreTest");
    try {
      r.setLoad(ejbLoadFlag);
      ref = r;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Caught Exception: " + e);
      throw new EJBException(e.getMessage());
    }
  }

  public boolean iAmDestroyed() {
    return iAmDestroyed;
  }
}
