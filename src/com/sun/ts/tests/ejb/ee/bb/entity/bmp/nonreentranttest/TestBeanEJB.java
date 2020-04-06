/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb.ee.bb.entity.bmp.nonreentranttest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.naming.*;
import jakarta.ejb.*;
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

  private static final String loopBackBean = "java:comp/env/ejb/LoopBackBean";

  public Integer ejbCreate(Properties p, int cofID, String cofName,
      float cofPrice) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      TestUtil.logMsg("Initialize remote logging");
      TestUtil.init(p);

      TestUtil.logMsg("DAO Init");
      if (null == dao) {
        dao = DAOFactory.getInstance().getCoffeeDAO();
      }
      TestUtil.logMsg("Get DB Connection");
      dao.startSession();

      TestUtil.logTrace("DAO: Create new row...");
      dao.create(cofID, cofName, cofPrice);
      this.cache = new CoffeeBean(cofID, cofName, cofPrice);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }
    return new Integer(cofID);
  }

  public void ejbPostCreate(Properties p, int cofID, String cofName,
      float cofPrice) {
    TestUtil.logTrace("ejbPostCreate");
  }

  public void setEntityContext(EntityContext c) {
    TestUtil.logTrace("setEntityContext");
    ectx = c;
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("unsetEntityContext");
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("ejbRemove");
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
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
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

  public void sleep(int n) {
    TestUtil.logTrace("sleep");
    long t1, t2;
    t1 = System.currentTimeMillis();
    while ((t2 = System.currentTimeMillis()) < (t1 + n))
      ;
  }

  public boolean loopBackSameBean() {
    TestUtil.logTrace("loopBackSameBean");

    boolean pass;

    TestUtil.logMsg("Perform loopback test");
    try {
      TestUtil.logMsg("getPrimaryKey() object");
      Object o = ectx.getPrimaryKey();
      TestUtil.logMsg("getEJBObject() reference");
      TestBean ref = (TestBean) ectx.getEJBObject();
      TestUtil.logMsg("Performing self-referential loopback call test");
      ref.ping();
      TestUtil.logErr("No exception occurred during loopback call");
      pass = false;
    } catch (RemoteException e) {
      TestUtil.logMsg("Caught RemoteException as expected: " + e);
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected Exception: " + e);
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  public boolean loopBackAnotherBean(Properties p) {
    TestUtil.logTrace("loopBackAnotherBean");

    boolean pass;

    try {
      // lookup EJB home
      TestUtil.logMsg("Lookup home interface for EJB: " + loopBackBean);
      LoopBackHome beanHome = (LoopBackHome) lookup(loopBackBean,
          LoopBackHome.class);
      // create EJB instance
      TestUtil.logMsg("Create EJB instance");
      LoopBack beanRef = (LoopBack) beanHome.create();
      TestUtil.logMsg("Initialize remote logging");
      beanRef.initLogging(p);
      TestUtil.logMsg("Set bean reference");
      beanRef.setBeanRef((TestBean) ectx.getEJBObject());
      TestUtil.logMsg("Performing loopback call test");
      pass = beanRef.loopBackTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private Object lookup(String s, Class c) {
    TSNamingContext nctx = null;
    try {
      TestUtil.logMsg("obtain naming context");
      nctx = new TSNamingContext();
      if (c != null)
        return nctx.lookup(s, c);
      else
        return nctx.lookup(s);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("lookup failed: " + e);
    }
  }
}
