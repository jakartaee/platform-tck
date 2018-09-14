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
 * @(#)TestBeanEJB.java	1.14 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp.entitycontexttest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;
import java.io.*;
import java.security.Principal;
import javax.transaction.*;
import javax.naming.*;
import java.sql.*;

public class TestBeanEJB implements EntityBean {
  // Expected property names
  private final static String dataPropKey[] = { "user", "password", "server",
      "jdbcPoolName", };

  // Expected property values
  private final static String dataPropVal[] = { "cts1", "cts1", "JDBCTEST",
      "sessionContextPool", };

  private EntityContext ectx = null;

  private TSNamingContext nctx = null;

  private UserTransaction ut = null;

  // Entity instance data
  public String BRAND_NAME;

  public Integer KEY_ID;

  public float PRICE;

  public Integer ejbCreate(Properties p, int KEY_ID, String BRAND_NAME,
      float PRICE) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      TestUtil.logMsg("Initialize remote logging");
      TestUtil.init(p);
      this.KEY_ID = new Integer(KEY_ID);
      this.BRAND_NAME = BRAND_NAME;
      this.PRICE = PRICE;
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return this.KEY_ID;
  }

  public void ejbPostCreate(Properties p, int KEY_ID, String BRAND_NAME,
      float PRICE) {
    TestUtil.logTrace("ejbPostCreate");
  }

  public void setEntityContext(EntityContext c) {
    TestUtil.logTrace("setEntityContext");
    ectx = c;
    try {
      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();
    } catch (NamingException e) {
      TestUtil.logErr("NamingException ... " + e, e);
      throw new EJBException("unable to obtain naming context");
    } catch (Exception e) {
      TestUtil.logErr("Exception ... " + e, e);
      throw new EJBException("Exception occurred: " + e);
    }
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("unsetEntityContext");
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("ejbRemove");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
  }

  // ===========================================================
  // TestBean interface (our business methods)

  public boolean getEJBObjectTest() {
    TestUtil.logTrace("getEJBObjectTest");
    try {
      EJBObject object = ectx.getEJBObject();
      if (object != null) {
        TestUtil.logMsg("getEJBObject() returned EJBObject reference");
        return true;
      } else {
        TestUtil.logErr("getEJBObject() returned null reference");
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean getEJBHomeTest() {
    TestUtil.logTrace("getEJBHomeTest");
    try {
      TestBeanHome home = (TestBeanHome) ectx.getEJBHome();
      if (home != null) {
        TestUtil.logMsg("getEJBHome() returned EJBHome reference");
        return true;
      } else {
        TestUtil.logErr("getEJBHome() returned null reference");
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean getEnvironmentTest() {
    TestUtil.logTrace("getEnvironmentTest");
    int failures = 0;
    try {
      String propVal, envProp;
      for (int i = 0; i < dataPropKey.length; i++) {
        envProp = "java:comp/env/" + dataPropKey[i];
        TestUtil.logMsg("lookup: " + envProp);
        propVal = (String) nctx.lookup(envProp);
        TestUtil.logMsg("propVal=" + propVal);
        if (propVal == null) {
          TestUtil.logErr(
              "property name " + dataPropKey[i] + " not found in environment");
          failures++;
        } else if (!propVal.equals(dataPropVal[i])) {
          TestUtil.logErr(
              "property value " + propVal + " not equal to " + dataPropVal[i]);
          failures++;
        } else
          TestUtil.logMsg("property values are equal");
      }
      if (failures > 0)
        return false;
      else
        return true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean getCallerPrincipalTest(String s) {
    TestUtil.logTrace("getCallerPrincipalTest");
    try {
      Principal principal = ectx.getCallerPrincipal();
      if (principal != null) {
        TestUtil
            .logMsg("getCallerPrincipal() returned Principal: " + principal);
        String name = principal.getName();
        if (name.indexOf(s) < 0) {
          TestUtil.logErr("principal - expected: " + s + ", received: " + name);
          return false;
        } else
          return true;
      } else {
        TestUtil.logErr("getCallerPrincipal() returned null reference");
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

  public boolean isCallerInRoleTest(String s) {
    TestUtil.logTrace("isCallerInRoleTest");
    String role = s;
    try {
      boolean inRole = ectx.isCallerInRole(role);
      if (inRole)
        TestUtil.logMsg("isCallerInRole(" + role + ") is true");
      else
        TestUtil.logMsg("isCallerInRole(" + role + ") is false");
      return inRole;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

  public boolean getUserTransactionTest() {
    TestUtil.logTrace("getUserTransactionTest");

    boolean pass = true;

    TestUtil.logMsg("invoke EntityContext.getUserTransaction() method");
    try {
      UserTransaction ut = ectx.getUserTransaction();
      TestUtil.logErr("IllegalStateException not received - unexpected");
      pass = false;
    } catch (IllegalStateException e) {
      TestUtil.logMsg("IllegalStateException received - expected");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  public boolean setRollbackOnlyTest() {
    TestUtil.logTrace("setRollbackOnlyTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("get rollback status");
      if (!ectx.getRollbackOnly()) {
        TestUtil.logMsg("transaction not marked for rollback - expected");
      } else {
        TestUtil.logMsg("transaction marked for rollback - unexpected");
        return false;
      }
      TestUtil.logMsg("mark transaction for rollback");
      ectx.setRollbackOnly();
      TestUtil.logMsg("get rollback status");
      if (ectx.getRollbackOnly()) {
        TestUtil.logMsg("transaction marked for rollback - expected");
      } else {
        TestUtil.logMsg("transaction not marked for rollback - unexpected");
        pass = false;
      }
      return pass;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean getRollbackOnlyTest() {
    TestUtil.logTrace("getRollbackOnlyTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("get rollback status");
      if (!ectx.getRollbackOnly()) {
        TestUtil.logMsg("transaction not marked for rollback - expected");
      } else {
        TestUtil.logMsg("transaction marked for rollback - unexpected");
        return false;
      }
      TestUtil.logMsg("mark transaction for rollback");
      ectx.setRollbackOnly();
      TestUtil.logMsg("get rollback status");
      if (ectx.getRollbackOnly()) {
        TestUtil.logMsg("transaction marked for rollback - expected");
      } else {
        TestUtil.logMsg("transaction not marked for rollback - unexpected");
        pass = false;
      }
      return pass;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean getPrimaryKeyTest(Integer pk) {
    TestUtil.logTrace("getPrimaryKeyTest");
    try {
      Object o = ectx.getPrimaryKey();
      if (o == null) {
        TestUtil.logErr("primary key returned null");
        return false;
      }

      if (!(o instanceof Integer)) {
        TestUtil.logErr("primary key is not an instance of integer");
        return false;
      }

      Integer i = (Integer) o;

      if (i.equals(pk)) {
        TestUtil
            .logMsg("primaryKey match, expected: " + pk + " received: " + i);
        return true;
      } else {
        TestUtil
            .logErr("primaryKey mismatch, expected: " + pk + " received: " + i);
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    }
  }
}
