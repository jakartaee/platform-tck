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
 * @(#)FloatBeanEJB.java	1.8 03/05/16
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
import com.sun.ts.tests.common.dao.coffee.variants.FloatPKCoffeeDAO;

public class FloatBeanEJB implements EntityBean {

  /** Cached instance state */
  private float cofPrice;

  protected TSNamingContext nctx = null;

  protected EntityContext ectx = null;

  protected FloatPKCoffeeDAO dao = null;

  public void ping() {
  }

  /*
   * Bean life cycle.
   */

  public Float ejbCreate(Properties props, float cofID, String cofName,
      float cofPrice) throws CreateException {

    try {
      TestUtil.logTrace("[FloatBean] ejbCreate()");

      TestUtil.logTrace("[FloatBean] Initialize remote logging");
      TestUtil.init(props);

      TestUtil.logMsg("[FloatBean] DBSupport Init");
      if (null == dao) {
        dao = DAOFactory.getInstance().getFloatPKCoffeeDAO();
      }

      TestUtil.logTrace("[FloatBean] Get DB connection...");
      dao.startSession();
      TestUtil.logTrace("[FloatBean] Create new row...");

      dao.create(cofID, cofName, cofPrice);
      this.cofPrice = cofPrice;
    } catch (Exception e) {
      TestUtil.logErr("[FloatBean] Caught exception: " + e, e);
      throw new CreateException("Cannot create bean: " + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }

    return new Float(cofID);
  }

  public void ejbPostCreate(Properties props, float cofID, String cofName,
      float cofPrice) {
    TestUtil.logTrace("[FloatBean] ejbPostCreate()");
  }

  public void setEntityContext(EntityContext c) throws EJBException {

    TestUtil.logTrace("[FloatBean] setEntityContext()");
    ectx = c;

    try {
      TestUtil.logMsg("[FloatBean] Obtaining TS Naming Context...");
      nctx = new TSNamingContext();
    } catch (NamingException e) {
      TestUtil.logErr("[FloatBean] Naming Exception : " + e, e);
      throw new EJBException("Cannot obtain Naming Context" + e);
    } catch (Exception e) {
      TestUtil.logErr("[FloatBean] Caught exception: " + e, e);
      throw new EJBException("Caught exception: " + e);
    }
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("[FloatBean] unsetEntityContext()");
  }

  public void ejbRemove() throws RemoveException {

    TestUtil.logTrace("[FloatBean] ejbRemove()");
    try {
      if (null == dao) {
        TestUtil.logMsg("[FloatBean] Initialize DBSupport...");
        dao = DAOFactory.getInstance().getFloatPKCoffeeDAO();
      }
      TestUtil.logTrace("[FloatBean] Get DB connection...");
      dao.startSession();
      TestUtil.logTrace("[FloatBean] Remove row...");
      dao.delete(((Float) ectx.getPrimaryKey()).floatValue());
    } catch (Exception e) {
      TestUtil.logErr("[FloatBean] Caught Exception: " + e, e);
      throw new RemoveException("Cannot remove bean: " + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }
  }

  public void ejbActivate() {
    TestUtil.logTrace("[FloatBean] ejbActivate()");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("[FloatBean] ejbPassivate()");
  }

  public void ejbLoad() throws EJBException {

    TestUtil.logTrace("[FloatBean] ejbLoad()");
    try {
      if (null == dao) {
        TestUtil.logMsg("[FloatBean] Initialize DBSupport()");
        dao = DAOFactory.getInstance().getFloatPKCoffeeDAO();
      }
      TestUtil.logTrace("[FloatBean] Get DB connection...");
      dao.startSession();
      TestUtil.logTrace("[FloatBean] Load row...");
      this.cofPrice = dao
          .loadPrice(((Float) ectx.getPrimaryKey()).floatValue());
    } catch (DAOException e) {
      TestUtil.logErr("[FloatBean] No such entity exists: ", e);
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

    TestUtil.logTrace("[FloatBean] ejbStore()");
    try {
      if (null == dao) {
        TestUtil.logMsg("[FloatBean] Initializing DBSupport...");
        dao = DAOFactory.getInstance().getFloatPKCoffeeDAO();
      }
      TestUtil.logTrace("[FloatBean] Get DB connection...");
      dao.startSession();
      TestUtil.logTrace("[FloatBean] Store row...");
      dao.storePrice(((Float) ectx.getPrimaryKey()).floatValue(), cofPrice);
    } catch (DAOException e) {
      TestUtil.logErr("[FloatBean] No such entity" + e, e);
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

  public Float ejbFindByPrimaryKey(Float key) throws FinderException {

    TestUtil.logTrace("[FloatBean] ejbFindByPrimaryKey()");
    try {
      if (null == dao) {
        TestUtil.logMsg("[FloatBean] Initializing DBSupport...");
        dao = DAOFactory.getInstance().getFloatPKCoffeeDAO();
      }

      dao.startSession();
      if (dao.exists(key.floatValue())) {
        return key;
      } else {
        throw new FinderException("[FloatBean] Key not found: " + key);
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
