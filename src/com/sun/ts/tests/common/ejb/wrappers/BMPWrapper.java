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

package com.sun.ts.tests.common.ejb.wrappers;

import java.util.Properties;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.EJBException;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.FinderException;
import javax.ejb.NoSuchEntityException;
import javax.naming.NamingException;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.tests.common.dao.DAOException;
import com.sun.ts.tests.common.dao.DAOFactory;
import com.sun.ts.tests.common.dao.coffee.CoffeeBean;
import com.sun.ts.tests.common.dao.coffee.CoffeeDAO;

/**
 * BMP wrapper that provide the bean life cycle methods for a BMP bean. This
 * class is intended to be subclassed by the final entity bean class that will
 * provide the test logic (core of business methods.
 */
public class BMPWrapper implements EntityBean {

  /** Cached instance state */
  private CoffeeBean cache;

  protected TSNamingContext nctx = null;

  protected EntityContext ectx = null;

  private CoffeeDAO dao = null;

  /*
   * Bean life cycle.
   */

  public void setEntityContext(EntityContext ctx) throws EJBException {

    TestUtil.logTrace("[BMPWrapper] setEntityContext()");
    this.ectx = ctx;

    try {
      TestUtil.logMsg("[BMPWrapper] Obtaining TS Naming Context...");
      nctx = new TSNamingContext();
    } catch (NamingException e) {
      TestUtil.logErr("[BMPWrapper] Naming Exception : " + e);
      throw new EJBException("Cannot obtain Naming Context" + e);
    } catch (Exception e) {
      TestUtil.logErr("[BMPWrapper] Caught exception: " + e);
      throw new EJBException("Caught exception: " + e);
    }
  }

  public Integer ejbCreate(Properties props, int id, String name, float price)
      throws CreateException {

    try {
      TestUtil.logTrace("[BMPWrapper] ejbCreate()");

      TestUtil.logTrace("[BMPWrapper] Initialize remote logging");
      TestUtil.init(props);

      TestUtil.logTrace("[BMPWrapper] DAO Init");
      if (null == dao) {
        dao = DAOFactory.getInstance().getCoffeeDAO();
      }
      dao.startSession();

      TestUtil.logTrace("[BMPWrapper] Create new row...");
      dao.create(id, name, price);
      this.cache = new CoffeeBean(id, name, price);
    } catch (Exception e) {
      TestUtil.logErr("[BMPWrapper] Unexpected exception ", e);
      throw new CreateException("Caught exception : " + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }

    return new Integer(id);
  }

  public void ejbPostCreate(Properties props, int id, String name,
      float price) {

    TestUtil.logTrace("[BMPWrapper] ejbPostCreate()");
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("[BMPWrapper] unsetEntityContext()");
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("[BMPWrapper] ejbRemove()");
    try {
      if (null == dao) {
        TestUtil.logMsg("[BMPWrapper] get DAO...");
        dao = DAOFactory.getInstance().getCoffeeDAO();
      }
      TestUtil.logTrace("[BMPWrapper] Start DAO session...");
      dao.startSession();
      TestUtil.logTrace("[BMPWrapper] Remove row...");
      dao.delete(((Integer) ectx.getPrimaryKey()).intValue());
    } catch (DAOException e) {
      throw new RemoveException("[BMPWrapper] Caught DAOException" + e);
    } catch (Exception e) {
      throw new RemoveException("[BMPWrapper] Caught exception: " + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }
  }

  public void ejbActivate() {
    TestUtil.logTrace("[BMPWrapper] ejbActivate()");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("[BMPWrapper] ejbPassivate()");
  }

  public void ejbLoad() throws EJBException {

    TestUtil.logTrace("[BMPWrapper] ejbLoad()");
    try {
      if (null == dao) {
        TestUtil.logMsg("[BMPWrapper] Get DAO");
        dao = DAOFactory.getInstance().getCoffeeDAO();
      }
      TestUtil.logTrace("[BMPWrapper] Start DAO session...");
      dao.startSession();
      TestUtil.logTrace("[BMPWrapper] Load row...");
      this.cache = dao.load(((Integer) ectx.getPrimaryKey()).intValue());
    } catch (DAOException e) {
      TestUtil.logErr("[BMPWrapper] No such entity exists: " + e);
      throw new NoSuchEntityException("[ejbLoad] DAOException" + e);
    } catch (Exception e) {
      throw new EJBException("[ejbLoad] Unable to init DAO " + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }
  }

  public void ejbStore() throws EJBException {
    TestUtil.logTrace("[BMPWrapper] ejbStore()");
    try {
      if (null == dao) {
        TestUtil.logMsg("[BMPWrapper] Get DAO...");
        dao = DAOFactory.getInstance().getCoffeeDAO();
      }
      TestUtil.logTrace("[BMPWrapper] Start DAO session...");
      dao.startSession();
      TestUtil.logTrace("[BMPWrapper] Store row...");
      dao.store(cache);
    } catch (DAOException e) {
      TestUtil.logErr("[BMPWrapper] No such entity: " + e);
      throw new NoSuchEntityException("[ejbStore] DAOException" + e);
    } catch (Exception e) {
      throw new EJBException("[ejbStore] Unable to init DAO");
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
      dao = null;
    }
  }

  public Integer ejbFindByPrimaryKey(Integer key) throws FinderException {

    TestUtil.logTrace("[BMPWrapper] ejbFindByPrimaryKey()");
    try {
      if (null == dao) {
        TestUtil.logMsg("[BMPWrapper] Get DAO...");
        dao = DAOFactory.getInstance().getCoffeeDAO();
      }

      dao.startSession();
      if (dao.exists(key.intValue())) {
        return key;
      } else {
        throw new FinderException("[BMPWrapper] Key not found: " + key);
      }
    } catch (DAOException e) {
      throw new FinderException("DAOException " + e);
    } catch (Exception e) {
      throw new FinderException("Exception occurred: " + e);
    } finally {
      if (null != dao) {
        dao.stopSession();
      }
    }
  }

}
