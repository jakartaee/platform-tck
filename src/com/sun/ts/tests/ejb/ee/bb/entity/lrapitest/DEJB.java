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
 * @(#)DEJB.java	1.9 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.entity.lrapitest;

import com.sun.ts.tests.ejb.ee.bb.entity.util.DBSupport;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.naming.*;
import javax.ejb.*;
import java.rmi.*;
import java.sql.*;

public class DEJB implements EntityBean {
  private EntityContext ectx = null;

  private TSNamingContext nctx = null;

  private DBSupport db = null;

  float cofPrice = 0; // Instance data for Coffee Price

  public Integer ejbCreateD(Properties p, int cofID, String cofName,
      float cofPrice) throws CreateException {
    TestUtil.logTrace("ejbCreateD");
    try {
      TestUtil.logMsg("Initialize remote logging");
      TestUtil.init(p);

      db.getDBConnection();
      db.tableInit();
      db.createNewRow(cofID, cofName, cofPrice);
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

  public void ejbPostCreateD(Properties p, int cofID, String cofName,
      float cofPrice) {
    TestUtil.logTrace("ejbPostCreateD");
  }

  public void setEntityContext(EntityContext c) {
    TestUtil.logTrace("setEntityContext");
    ectx = c;
    try {
      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();
      TestUtil.logMsg("Initialize DBSupport");
      db = new DBSupport(ectx);
    } catch (NamingException e) {
      TestUtil.logErr("NamingException ... " + e, e);
      throw new EJBException("unable to obtain naming context");
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
      throw new RemoveException("unable to initialize DBSupport");
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

  public Integer ejbFindByPrimaryKey(Integer key) throws FinderException {
    TestUtil.logTrace("ejbFindByPrimaryKey");

    try {
      TestUtil.logMsg("Get DB Connection");
      db.getDBConnection();
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

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
    try {
      if (db == null) {
        TestUtil.logMsg("Initialize DBSupport");
        db = new DBSupport(ectx);
      }
      db.getDBConnection();
      cofPrice = db.loadPrice(((Integer) ectx.getPrimaryKey()).intValue());
    } catch (SQLException se) {
      TestUtil.printStackTrace(se);
      throw new EJBException("SQL Exception in ejbLoad");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("unable to initialize DBSupport");
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
      TestUtil.printStackTrace(se);
      throw new EJBException("SQL Exception in ejbStore");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("unable to initialize DBSupport");
    } finally {
      try {
        db.closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  public String ejbHomeAddBar(String s) {
    TestUtil.logTrace("ejbHomeAddBar");
    return s + "bar";
  }

  // ===========================================================
  // D interface (our business methods)

  // This method is only exposed through the Local Interface
  public String whoAmILocal() {
    return "whoAmILocal cofPrice is: " + cofPrice;
  }
}
