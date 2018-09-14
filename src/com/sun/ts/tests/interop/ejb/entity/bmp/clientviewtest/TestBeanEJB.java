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

package com.sun.ts.tests.interop.ejb.entity.bmp.clientviewtest;

import com.sun.ts.tests.ejb.ee.bb.entity.util.DBSupport;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.naming.*;
import javax.ejb.*;
import java.rmi.*;
import java.sql.*;

public class TestBeanEJB implements EntityBean {
  private EntityContext ectx = null;

  private DBSupport db = null;

  private float cofPrice; // cached instance state

  public Integer ejbCreate(Properties p, boolean newTable, int cofID,
      String cofName, float cofPrice) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      TestUtil.logMsg("Initialize remote logging");
      TestUtil.init(p);
      TestUtil.logMsg("get DB connection");
      db.getDBConnection();
      if (newTable)
        db.tableInit();
      db.createNewRow(cofID, cofName, cofPrice);
      this.cofPrice = cofPrice;
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("SQL Exception in create");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    } finally {
      try {
        db.closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
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
    try {
      TestUtil.logMsg("Initialize DBSupport");
      db = new DBSupport(ectx);
    } catch (Exception e) {
      TestUtil.logErr("Exception ... " + e, e);
      throw new EJBException("unable to initialize DBSupport");
    }
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("unsetEntityContext");
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("ejbRemove");
    try {
      if (db == null) {
        TestUtil.logMsg("Initialize DBSupport");
        db = new DBSupport(ectx);
      }
      db.getDBConnection();
      db.removeRow(((Integer) ectx.getPrimaryKey()).intValue());
    } catch (SQLException se) {
      TestUtil.printStackTrace(se);
      throw new RemoveException("SQL Exception in remove");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new RemoveException("Unable to initialize DBSupport");
    } finally {
      try {
        db.closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
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
      if (db == null) {
        TestUtil.logMsg("Initialize DBSupport");
        db = new DBSupport(ectx);
      }
      db.getDBConnection();
      this.cofPrice = db.loadPrice(((Integer) ectx.getPrimaryKey()).intValue());
    } catch (SQLException se) {
      TestUtil.logErr("ejbLoad: No such entity exists", se);
      throw new NoSuchEntityException("SQL Exception in ejbLoad");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Unable to initialize DBSupport");
    } finally {
      try {
        db.closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
    try {
      if (db == null) {
        TestUtil.logMsg("Initialize DBSupport");
        db = new DBSupport(ectx);
      }
      db.getDBConnection();
      db.storePrice(((Integer) ectx.getPrimaryKey()).intValue(), cofPrice);
    } catch (SQLException se) {
      TestUtil.logErr("ejbLoad: No such entity exists", se);
      throw new NoSuchEntityException("SQL Exception in ejbLoad");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Unable to initialize DBSupport");
    } finally {
      try {
        db.closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  public Integer ejbFindTheBean(Properties p, Integer key)
      throws FinderException {
    TestUtil.logTrace("ejbFindTheBean");
    try {
      TestUtil.logMsg("Initialize remote logging");
      TestUtil.init(p);
      boolean foundKey = db.keyExists(key.intValue());
      if (foundKey)
        return key;
      else
        throw new FinderException("Key not found: " + key);
    } catch (SQLException se) {
      TestUtil.printStackTrace(se);
      throw new FinderException("SQL Exception in primary key finder");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new FinderException("Exception occurred: " + e);
    } finally {
      try {
        db.closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  public Integer ejbFindByPrimaryKey(Integer key) throws FinderException {
    TestUtil.logTrace("ejbFindByPrimaryKey");

    try {
      boolean foundKey = db.keyExists(key.intValue());
      if (foundKey)
        return key;
      else
        throw new FinderException("Key not found: " + key);
    } catch (SQLException se) {
      TestUtil.printStackTrace(se);
      throw new FinderException("SQL Exception in primary key finder");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new FinderException("Exception occurred: " + e);
    } finally {
      try {
        db.closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  public Collection ejbFindByName(Properties p, String name)
      throws FinderException {
    TestUtil.logTrace("ejbFindByName");
    try {
      TestUtil.logMsg("Initialize remote logging");
      TestUtil.init(p);
      Collection c = db.nameToKeyCollection(name);
      if (c.size() > 0)
        return c;
      else
        throw new FinderException("Name not found: " + name);
    } catch (SQLException se) {
      TestUtil.printStackTrace(se);
      throw new FinderException("SQL Exception in name finder");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new FinderException("Exception occurred: " + e);
    } finally {
      try {
        db.closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  public Collection ejbFindByPrice(Properties p, float price)
      throws FinderException {
    TestUtil.logTrace("ejbFindByPrice");
    try {
      TestUtil.logMsg("Initialize remote logging");
      TestUtil.init(p);
      Collection c = db.priceToKeyCollection(price);
      if (c.size() > 0)
        return c;
      throw new FinderException("Price not found: " + price);
    } catch (SQLException se) {
      TestUtil.printStackTrace(se);
      throw new FinderException("SQL Exception in price finder");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new FinderException("Exception occurred: " + e);
    } finally {
      try {
        db.closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  public Collection ejbFindWithinPriceRange(Properties p, float pmin,
      float pmax) throws FinderException {
    TestUtil.logTrace("ejbFindWithinPriceRange");
    try {
      TestUtil.logMsg("Initialize remote logging");
      TestUtil.init(p);
      Collection c = db.priceRangeToCollection(pmin, pmax);
      if (c.size() > 0)
        return c;
      else
        throw new FinderException(
            "Price Range not found: [" + pmin + "-" + pmax + "]");
    } catch (SQLException se) {
      TestUtil.printStackTrace(se);
      throw new FinderException("SQL Exception in price range finder");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new FinderException("Exception occurred: " + e);
    } finally {
      try {
        db.closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  public Collection ejbFindWithinPrimaryKeyRange(Properties p, Integer kmin,
      Integer kmax) throws FinderException {
    TestUtil.logTrace("ejbFindWithinPrimaryKeyRange");
    try {
      TestUtil.logMsg("Initialize remote logging");
      TestUtil.init(p);
      Collection c = db.primaryKeyRangeToCollection(kmin, kmax);
      if (c.size() > 0)
        return c;
      else
        throw new FinderException(
            "Primary Key Range not found: [" + kmin + "-" + kmax + "]");
    } catch (SQLException se) {
      TestUtil.printStackTrace(se);
      throw new FinderException("SQL Exception in primary key range finder");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new FinderException("Exception occurred: " + e);
    } finally {
      try {
        db.closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  // ===========================================================
  // TestBean interface (our business methods)

  public String ping(String s) {
    TestUtil.logTrace("ping : " + s);
    return "ping: " + s;
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
