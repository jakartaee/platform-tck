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
 * @(#)CompoundBeanEJB.java	1.8 03/05/16
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
import com.sun.ts.tests.common.dao.coffee.variants.CompoundPKCoffeeDAO;
import com.sun.ts.tests.common.dao.coffee.variants.CompoundPK;

public class CompoundBeanEJB implements EntityBean {

  /** Cached instance state */
  private float cofPrice;

  protected TSNamingContext nctx = null;

  protected EntityContext ectx = null;

  protected CompoundPKCoffeeDAO dao = null;

  public void ping() {
  }

  /*
   * Bean life cycle.
   */

  public CompoundPK ejbCreate(Properties props, CompoundPK cofID,
      String cofName, float cofPrice) throws CreateException {

    try {
      TestUtil.logTrace("[CompoundBean] ejbCreate()");

      TestUtil.logTrace("[CompoundBean] Initialize remote logging");
      TestUtil.init(props);

      TestUtil.logMsg("[CompoundBean] DBSupport Init");
      if (null == dao) {
        dao = DAOFactory.getInstance().getCompoundPKCoffeeDAO();
      }

      TestUtil.logTrace("[CompoundBean] Get DB connection...");
      dao.startSession();
      TestUtil.logTrace("[CompoundBean] Create new row...");

      dao.create(cofID, cofName, cofPrice);
      this.cofPrice = cofPrice;
    } catch (Exception e) {
      TestUtil.logErr("[CompoundBean] Caught exception: " + e, e);
      throw new CreateException("Cannot create bean: " + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }

    return cofID;
  }

  public void ejbPostCreate(Properties props, CompoundPK cofID, String cofName,
      float cofPrice) {
    TestUtil.logTrace("[CompoundBean] ejbPostCreate()");
  }

  public void setEntityContext(EntityContext c) throws EJBException {

    TestUtil.logTrace("[CompoundBean] setEntityContext()");
    ectx = c;

    try {
      TestUtil.logMsg("[CompoundBean] Obtaining TS Naming Context...");
      nctx = new TSNamingContext();
    } catch (NamingException e) {
      TestUtil.logErr("[CompoundBean] Naming Exception : " + e, e);
      throw new EJBException("Cannot obtain Naming Context" + e);
    } catch (Exception e) {
      TestUtil.logErr("[CompoundBean] Caught exception: " + e, e);
      throw new EJBException("Caught exception: " + e);
    }
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("[CompoundBean] unsetEntityContext()");
  }

  public void ejbRemove() throws RemoveException {

    TestUtil.logTrace("[CompoundBean] ejbRemove()");
    try {
      if (null == dao) {
        TestUtil.logMsg("[CompoundBean] Initialize DBSupport...");
        dao = DAOFactory.getInstance().getCompoundPKCoffeeDAO();
      }
      TestUtil.logTrace("[CompoundBean] Get DB connection...");
      dao.startSession();
      TestUtil.logTrace("[CompoundBean] Remove row...");
      dao.delete(((CompoundPK) ectx.getPrimaryKey()));
    } catch (Exception e) {
      TestUtil.logErr("[CompoundBean] Caught Exception: " + e, e);
      throw new RemoveException("Cannot remove bean: " + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }
  }

  public void ejbActivate() {
    TestUtil.logTrace("[CompoundBean] ejbActivate()");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("[CompoundBean] ejbPassivate()");
  }

  public void ejbLoad() throws EJBException {

    TestUtil.logTrace("[CompoundBean] ejbLoad()");
    try {
      if (null == dao) {
        TestUtil.logMsg("[CompoundBean] Initialize DBSupport()");
        dao = DAOFactory.getInstance().getCompoundPKCoffeeDAO();
      }
      TestUtil.logTrace("[CompoundBean] Get DB connection...");
      dao.startSession();
      TestUtil.logTrace("[CompoundBean] Load row...");
      this.cofPrice = dao.loadPrice(((CompoundPK) ectx.getPrimaryKey()));
    } catch (DAOException e) {
      TestUtil.logErr("[CompoundBean] No such entity exists: ", e);
      throw new NoSuchEntityException("[ejbLoad] DAOException" + e);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("[ejbLoad] Unexpected exception" + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }
  }

  public void ejbStore() throws EJBException {

    TestUtil.logTrace("[CompoundBean] ejbStore()");
    try {
      if (null == dao) {
        TestUtil.logMsg("[CompoundBean] Initializing DBSupport...");
        dao = DAOFactory.getInstance().getCompoundPKCoffeeDAO();
      }
      TestUtil.logTrace("[CompoundBean] Get DB connection...");
      dao.startSession();
      TestUtil.logTrace("[CompoundBean] Store row...");
      dao.storePrice(((CompoundPK) ectx.getPrimaryKey()), cofPrice);
    } catch (DAOException e) {
      TestUtil.logErr("[CompoundBean] No such entity" + e.getCause(), e);
      throw new NoSuchEntityException("[ejbStore] DAOException" + e);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("[ejbStore] Unable to init DBSupport");
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }
  }

  public CompoundPK ejbFindByPrimaryKey(CompoundPK key) throws FinderException {

    TestUtil.logTrace("[CompoundBean] ejbFindByPrimaryKey()");
    try {
      if (null == dao) {
        TestUtil.logMsg("[CompoundBean] Initializing DBSupport...");
        dao = DAOFactory.getInstance().getCompoundPKCoffeeDAO();
      }

      dao.startSession();
      if (dao.exists(key)) {
        return key;
      } else {
        throw new FinderException("[CompoundBean] Key not found: " + key);
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
