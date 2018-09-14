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
 * @(#)SecTestRoleRefEJB.java	1.11 03/05/16
 */

// The purpose of this EJB is to test scoping of security propagation of role references.

package com.sun.ts.tests.ejb.ee.sec.bmp.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;

import com.sun.ts.tests.ejb.ee.sec.bmp.util.DBSupport;
import java.sql.*;
import javax.naming.*;

public class SecTestRoleRefEJB implements EntityBean {

  private EntityContext ectx = null;

  private static final String user1 = "user1";

  private static final String password1 = "password1";

  private String ctsuser = null;

  private String ctspassword = null;

  public void SecTestRoleRefEJB() throws CreateException {
    TestUtil.logTrace("In constructor");
  }

  public boolean EjbSecRoleRefScope(String role) {
    return ectx.isCallerInRole(role);
  }

  public Integer ejbCreate(Properties p, boolean newTable, int cofID,
      String cofName, float cofPrice) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    DBSupport db = null;
    try {
      TestUtil.init(p);
      db = new DBSupport(ectx);

      ctsuser = TestUtil.getProperty(user1);
      ctspassword = TestUtil.getProperty(password1);
      db.getDBConnection(ctsuser, ctspassword);

      if (newTable)
        db.tableInit();
      db.createNewRow(cofID, cofName, cofPrice);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
    } catch (SQLException se) {
      TestUtil.logErr("SQL Exception in create: ", se);
      throw new CreateException("SQL Exception in create" + se.getMessage());
    } catch (NamingException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Unable to obtain naming context");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Unable to initialize DBSupport");
    } finally {
      try {
        db.closeDBConnection();
      } catch (SQLException e) {
        TestUtil.logErr("SQL Exception in create when close DBConnection: "
            + e.getMessage(), e);
      }
    }
    return new Integer(cofID);
  }

  public void ejbRemove() throws RemoveException, javax.ejb.EJBException {
    TestUtil.logTrace("ejbRemove");
    DBSupport db = null;
    try {
      TestUtil.logMsg("Initialize DBSupport");
      db = new DBSupport(ectx);

      ctsuser = TestUtil.getProperty(user1);
      ctspassword = TestUtil.getProperty(password1);
      db.getDBConnection(ctsuser, ctspassword);

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
        TestUtil.logErr("SQL Exception in create when close DBConnection: "
            + e.getMessage(), e);
      }
    }
  }

  public Integer ejbFindByPrimaryKey(Integer key) throws FinderException {
    TestUtil.logTrace("ejbFindByPrimaryKey");
    DBSupport db = null;
    try {
      TestUtil.logMsg("Initialize DBSupport");
      db = new DBSupport(ectx);
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
      throw new FinderException("Unable to obtain naming context");
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
    DBSupport db = null;
    try {
      TestUtil.logMsg("Initialize DBSupport");
      db = new DBSupport(ectx);
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
      throw new FinderException("Unable to obtain naming context");
    } finally {
      try {
        db.closeDBConnection();
      } catch (SQLException e) {
        TestUtil.printStackTrace(e);
      }
    }
  }

  public void ejbPostCreate(Properties p, boolean newTable, int cofID,
      String cofName, float cofPrice) {
    TestUtil.logTrace("In ejbPostCreate !!");
  }

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
  }

  public void setEntityContext(EntityContext sc) {
    ectx = sc;
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("ejb.unsetEntityContext");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }
}
