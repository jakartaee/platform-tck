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
 * @(#)SecTestEJB.java	1.14 03/05/16
 */

package com.sun.ts.tests.ejb.ee.sec.bmp.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;
import javax.naming.*;

import com.sun.ts.tests.ejb.ee.sec.bmp.util.*;
import java.sql.*;

public class SecTestEJB implements EntityBean {

  private EntityContext ectx = null;

  private float cofPrice = 0;

  private static final String user1 = "user1";

  private static final String password1 = "password1";

  private String ctsuser = null;

  private String ctspassword = null;

  public void SecTestEJB() throws CreateException {
    TestUtil.logTrace("In constructor!");
  }

  public Integer ejbCreate(Properties p, boolean newTable, int cofID,
      String cofName, float cofPrice) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    DBSupport db = null;
    try {
      TestUtil.init(p);
      TestUtil.logMsg("Initialize DBSupport");
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

  public void ejbRemove() throws RemoveException {
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
      ctsuser = TestUtil.getProperty(user1);
      ctspassword = TestUtil.getProperty(password1);
      TestUtil.logMsg("Get DB Connection");
      db.getDBConnection(ctsuser, ctspassword);

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
        TestUtil.logErr("SQL Exception in create when close DBConnection: "
            + e.getMessage(), e);
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
      TestUtil.logMsg("Get DB Connection");

      ctsuser = TestUtil.getProperty(user1);
      ctspassword = TestUtil.getProperty(password1);
      db.getDBConnection(ctsuser, ctspassword);

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
        TestUtil.logErr("SQL Exception in create when close DBConnection: "
            + e.getMessage(), e);
      }
    }
  }

  public void ejbPostCreate(Properties p, boolean newTable, int cofID,
      String cofName, float cofPrice) {
    TestUtil.logTrace("In ejbPostCreate !!");
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
  }

  public void setEntityContext(EntityContext x) {
    TestUtil.logTrace("setEntityContext");
    ectx = x;
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("unsetEntityContext");
  }

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  public boolean IsCaller(String caller) {
    if (ectx.getCallerPrincipal().getName().indexOf(caller) < 0)
      return false;
    else
      return true;
  }

  public boolean EjbNotAuthz() {
    return true;
  }

  public boolean EjbIsAuthz() {
    return true;
  }

  public boolean EjbSecRoleRef(String role) {
    return ectx.isCallerInRole(role);
  }

  public boolean EjbOverloadedSecRoleRefs(String role1) {
    TestUtil.logMsg(
        "isCallerInRole(" + role1 + ") = " + ectx.isCallerInRole(role1));
    return ectx.isCallerInRole(role1);
  }

  public boolean EjbOverloadedSecRoleRefs(String role1, String role2) {
    TestUtil
        .logMsg("isCallerInRole(" + role1 + ")= " + ectx.isCallerInRole(role1)
            + "isCallerInRole(" + role2 + ")= " + ectx.isCallerInRole(role2));
    return ectx.isCallerInRole(role1) && ectx.isCallerInRole(role2);
  }

  public boolean checktest1() {
    return true;
  }

  public boolean excludetest1() {
    return true;
  }
}
