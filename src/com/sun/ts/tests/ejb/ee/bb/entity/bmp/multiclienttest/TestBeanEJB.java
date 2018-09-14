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

package com.sun.ts.tests.ejb.ee.bb.entity.bmp.multiclienttest;

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

  float cofPrice = 0; // Instance data for Coffee Price

  public Integer ejbCreate(Properties p, boolean newTable, int cofID,
      String cofName, float cofPrice) throws CreateException {
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

    return new Integer(cofID);
  }

  public void ejbPostCreate(Properties p, boolean newTable, int cofID,
      String cofName, float cofPrice) {
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
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
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

  public void updatePrice(float newPrice) {
    TestUtil.logTrace("updatePrice");
    cofPrice = newPrice;
  }

  public float getPrice() {
    TestUtil.logTrace("getPrice");
    return cofPrice;
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
