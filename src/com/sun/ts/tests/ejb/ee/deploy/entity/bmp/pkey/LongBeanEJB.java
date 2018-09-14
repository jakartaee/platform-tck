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
 * @(#)LongBeanEJB.java	1.8 03/05/16
 */

package com.sun.ts.tests.ejb.ee.deploy.entity.bmp.pkey;

import java.util.Properties;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.NoSuchEntityException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.common.dao.DAOException;
import com.sun.ts.tests.common.dao.DAOFactory;
import com.sun.ts.tests.common.dao.coffee.variants.LongPKCoffeeDAO;

public class LongBeanEJB implements EntityBean {

  /** Cached instance state */
  private float cofPrice;

  protected TSNamingContext nctx = null;

  protected EntityContext ectx = null;

  protected LongPKCoffeeDAO dao = null;

  public void ping() {
  }

  /*
   * Bean life cycle.
   */

  public Long ejbCreate(Properties props, long cofID, String cofName,
      float cofPrice) throws CreateException {

    try {
      TestUtil.logTrace("[LongBean] ejbCreate()");

      TestUtil.logTrace("[LongBean] Initialize remote logging");
      TestUtil.init(props);

      TestUtil.logMsg("[LongBean] DBSupport Init");
      if (null == dao) {
        dao = DAOFactory.getInstance().getLongPKCoffeeDAO();
      }

      TestUtil.logTrace("[LongBean] Start session...");
      dao.startSession();
      TestUtil.logTrace("[LongBean] Create new row...");

      dao.create(cofID, cofName, cofPrice);
      this.cofPrice = cofPrice;
    } catch (Exception e) {
      TestUtil.logErr("[LongBean] Caught exception: " + e, e);
      throw new CreateException("Cannot create bean: " + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }

    return new Long(cofID);
  }

  public void ejbPostCreate(Properties props, long cofID, String cofName,
      float cofPrice) {
    TestUtil.logTrace("[LongBean] ejbPostCreate()");
  }

  public void setEntityContext(EntityContext c) throws EJBException {

    TestUtil.logTrace("[LongBean] setEntityContext()");
    ectx = c;

    try {
      TestUtil.logMsg("[LongBean] Obtaining TS Naming Context...");
      nctx = new TSNamingContext();
    } catch (NamingException e) {
      TestUtil.logErr("[LongBean] Naming Exception : " + e, e);
      throw new EJBException("Cannot obtain Naming Context" + e);
    } catch (Exception e) {
      TestUtil.logErr("[LongBean] Caught exception: " + e, e);
      throw new EJBException("Caught exception: " + e);
    }
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("[LongBean] unsetEntityContext()");
  }

  public void ejbRemove() throws RemoveException {

    TestUtil.logTrace("[LongBean] ejbRemove()");
    try {
      if (null == dao) {
        TestUtil.logMsg("[LongBean] Initialize DBSupport...");
        dao = DAOFactory.getInstance().getLongPKCoffeeDAO();
      }
      TestUtil.logTrace("[LongBean] Start session...");
      dao.startSession();
      TestUtil.logTrace("[LongBean] Remove row...");
      dao.delete(((Long) ectx.getPrimaryKey()).longValue());
    } catch (Exception e) {
      TestUtil.logErr("[LongBean] Caught Exception: " + e, e);
      throw new RemoveException("Cannot remove bean: " + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }
  }

  public void ejbActivate() {
    TestUtil.logTrace("[LongBean] ejbActivate()");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("[LongBean] ejbPassivate()");
  }

  public void ejbLoad() throws EJBException {

    TestUtil.logTrace("[LongBean] ejbLoad()");
    try {
      if (null == dao) {
        TestUtil.logMsg("[LongBean] Initialize DBSupport()");
        dao = DAOFactory.getInstance().getLongPKCoffeeDAO();
      }
      TestUtil.logTrace("[LongBean] Get DB connection...");
      dao.startSession();
      TestUtil.logTrace("[LongBean] Load row...");
      this.cofPrice = dao.loadPrice(((Long) ectx.getPrimaryKey()).longValue());
    } catch (DAOException e) {
      TestUtil.logErr("[LongBean] No such entity exists: ", e.getCause());
      throw new NoSuchEntityException("[ejbLoad] DAOException" + e.getCause());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("[ejbLoad] Unable to init DBSupport" + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }
  }

  public void ejbStore() throws EJBException {

    TestUtil.logTrace("[LongBean] ejbStore()");
    try {
      if (null == dao) {
        TestUtil.logMsg("[LongBean] Initializing DBSupport...");
        dao = DAOFactory.getInstance().getLongPKCoffeeDAO();
      }
      TestUtil.logTrace("[LongBean] Get DB connection...");
      dao.startSession();
      TestUtil.logTrace("[LongBean] Store row...");
      dao.storePrice(((Long) ectx.getPrimaryKey()).longValue(), cofPrice);
    } catch (DAOException e) {
      TestUtil.logErr("[LongBean] No such entity", e.getCause());
      throw new NoSuchEntityException("[ejbStore] DAOException" + e.getCause());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("[ejbStore] Unable to init DBSupport");
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
      dao = null;
    }
  }

  public Long ejbFindByPrimaryKey(Long key) throws FinderException {

    TestUtil.logTrace("[LongBean] ejbFindByPrimaryKey()");
    try {
      if (null == dao) {
        TestUtil.logMsg("[LongBean] Initializing DBSupport...");
        dao = DAOFactory.getInstance().getLongPKCoffeeDAO();
      }

      dao.startSession();
      if (dao.exists(key.longValue())) {
        return key;
      } else {
        throw new FinderException("[LongBean] Key not found: " + key);
      }
    } catch (DAOException e) {
      TestUtil.printStackTrace(e);
      throw new FinderException("DAOException " + e.getCause());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new FinderException("Exception occurred: " + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }
  }

}
