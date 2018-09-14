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
 * @(#)StringBeanEJB.java	1.8 03/05/16
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
import com.sun.ts.tests.common.dao.coffee.variants.StringPKCoffeeDAO;

public class StringBeanEJB implements EntityBean {

  /** Cached instance state */
  private float cofPrice;

  protected TSNamingContext nctx = null;

  protected EntityContext ectx = null;

  private StringPKCoffeeDAO dao = null;

  public void ping() {
    TestUtil.logTrace("[StringBean] ping()");
  }

  /*
   * Bean life cycle.
   */

  public String ejbCreate(Properties props, String cofID, String cofName,
      float cofPrice) throws CreateException {

    try {
      TestUtil.logTrace("[StringBean] ejbCreate()");

      TestUtil.logMsg("[StringBean] Initialize remote logging");
      TestUtil.init(props);

      TestUtil.logMsg("[StringBean] DBSupport Init");
      if (null == dao) {
        dao = DAOFactory.getInstance().getStringPKCoffeeDAO();
      }

      TestUtil.logTrace("[StringBean] Get DB connection...");
      dao.startSession();
      TestUtil.logTrace("[StringBean] Create new row...");

      dao.create(cofID, cofName, cofPrice);
      this.cofPrice = cofPrice;
    } catch (DAOException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("[ejbCreate] DAO Exception: " + e.getCause());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("[ejbCreate] Exception caught: " + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }

    return new String(cofID);
  }

  public void ejbPostCreate(Properties props, String cofID, String cofName,
      float cofPrice) {

    TestUtil.logTrace("[StringBean] ejbPostCreate()");
  }

  public void setEntityContext(EntityContext c) throws EJBException {

    TestUtil.logTrace("[StringBean] setEntityContext()");
    ectx = c;

    try {
      TestUtil.logMsg("[StringBean] Obtaining TS Naming Context...");
      nctx = new TSNamingContext();
    } catch (NamingException e) {
      TestUtil.logErr("[StringBean] Naming Exception : " + e, e);
      throw new EJBException("Cannot obtain Naming Context" + e);
    } catch (Exception e) {
      TestUtil.logErr("[StringBean] Caught exception: " + e, e);
      throw new EJBException("Caught exception: " + e);
    }
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("[StringBean] unsetEntityContext()");
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("[StringBean] ejbRemove()");
    try {
      if (null == dao) {
        TestUtil.logMsg("[StringBean] Initialize DBSupport...");
        dao = DAOFactory.getInstance().getStringPKCoffeeDAO();
      }
      TestUtil.logTrace("[StringBean] Get DB connection...");
      dao.startSession();
      TestUtil.logTrace("[StringBean] Remove row...");
      dao.delete(((String) ectx.getPrimaryKey()));
    } catch (DAOException e) {
      TestUtil.printStackTrace(e);
      throw new RemoveException(
          "[StringBean] Caught DAOException" + e.getCause());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new RemoveException("[StringBean] Caught exception: " + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }
  }

  public void ejbActivate() {
    TestUtil.logTrace("[StringBean] ejbActivate()");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("[StringBean] ejbPassivate()");
  }

  public void ejbLoad() throws EJBException {

    TestUtil.logTrace("[StringBean] ejbLoad()");
    try {
      if (null == dao) {
        TestUtil.logMsg("[StringBean] Initialize DBSupport()");
        dao = DAOFactory.getInstance().getStringPKCoffeeDAO();
      }
      TestUtil.logTrace("[StringBean] Get DB connection...");
      dao.startSession();
      TestUtil.logTrace("[StringBean] Load row...");
      this.cofPrice = dao.loadPrice(((String) ectx.getPrimaryKey()));
    } catch (DAOException e) {
      TestUtil.logErr("[StringBean] No such entity exists: ", e);
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
    TestUtil.logTrace("[StringBean] ejbStore()");
    try {
      if (null == dao) {
        TestUtil.logMsg("[StringBean] Initializing DBSupport...");
        dao = DAOFactory.getInstance().getStringPKCoffeeDAO();
      }
      TestUtil.logTrace("[StringBean] Get DB connection...");
      dao.startSession();
      TestUtil.logTrace("[StringBean] Store row...");
      dao.storePrice(((String) ectx.getPrimaryKey()), cofPrice);
    } catch (DAOException e) {
      TestUtil.logErr("[StringBean] No such entity", e.getCause());
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

  public String ejbFindByPrimaryKey(String key) throws FinderException {

    TestUtil.logTrace("[StringBean] ejbFindByPrimaryKey()");
    try {
      if (null == dao) {
        TestUtil.logMsg("[StringBean] Initializing DBSupport...");
        dao = DAOFactory.getInstance().getStringPKCoffeeDAO();
      }
      dao.startSession();
      if (dao.exists(key)) {
        return key;
      } else {
        throw new FinderException("[StringBean] Key not found: " + key);
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
