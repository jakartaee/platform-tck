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
 * @(#)TestBeanEJB.java	1.25 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.session.stateful.sessioncontexttest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;
import java.io.*;
import java.security.Principal;
import javax.transaction.*;

public class TestBeanEJB implements SessionBean {
  // Expected property names
  private final static String dataPropKey[] = { "user", "password", "server",
      "jdbcPoolName", };

  // Expected property values
  private final static String dataPropVal[] = { "cts1", "cts1", "JDBCTEST",
      "sessionContextPool", };

  private SessionContext sctx = null;

  private Properties harnessProps = null;

  private UserTransaction ut = null;

  private TSNamingContext nctx = null;

  public TestBeanEJB() {
    TestUtil.logTrace("newInstance() => default constructor called");
  }

  public void ejbCreate(Properties p) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    harnessProps = p;
    try {
      TestUtil.logMsg("initialize remote logging");
      TestUtil.init(p);
      TestUtil.logMsg("obtain naming context");
      nctx = new TSNamingContext();
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("unable to obtain naming context");
    }
  }

  public void setSessionContext(SessionContext sc) {
    TestUtil.logTrace("setSessionContext");
    this.sctx = sc;
  }

  public void ejbRemove() {
    TestUtil.logTrace("ejbRemove");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  // ===========================================================
  // TestBean interface (our business methods)

  public boolean getEJBObjectTest() {
    TestUtil.logTrace("getEJBObjectTest");
    try {
      EJBObject object = sctx.getEJBObject();
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
      TestBeanHome home = (TestBeanHome) sctx.getEJBHome();
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
      Principal principal = sctx.getCallerPrincipal();
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
      boolean inRole = sctx.isCallerInRole(role);
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

  public boolean setRollbackOnlyTest() {
    TestUtil.logTrace("setRollbackOnlyTest");
    boolean pass = true;
    TestUtil.logMsg("set rollback status - IllegalStateException");
    try {
      sctx.setRollbackOnly();
      TestUtil.logErr("no IllegalStateException occurred - unexpected");
      pass = false;
    } catch (IllegalStateException e) {
      TestUtil.logMsg("IllegalStateException occurred - expected");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception: " + e, e);
      pass = false;
    }
    return pass;
  }

  public boolean getRollbackOnlyTest() {
    TestUtil.logTrace("getRollbackOnlyTest");
    boolean pass = true;
    TestUtil.logMsg("get rollback status - IllegalStateException");
    try {
      boolean status = sctx.getRollbackOnly();
      TestUtil.logErr("no IllegalStateException occurred - unexpected");
      pass = false;
    } catch (IllegalStateException e) {
      TestUtil.logMsg("IllegalStateException occurred - expected");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception: " + e, e);
      pass = false;
    }
    return pass;
  }

  public boolean getUserTransactionTest() {
    TestUtil.logTrace("getUserTransactionTest");
    try {
      UserTransaction ut = sctx.getUserTransaction();
      if (ut != null)
        return true;
      else
        return false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean getUserTransactionTest(TestBean2 ref) {
    TestUtil.logTrace("getUserTransactionTest");
    try {
      boolean pass = ref.getUserTransactionTest();
      return pass;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean beginTransaction() {
    TestUtil.logTrace("beginTransaction");
    try {
      ut = sctx.getUserTransaction();
      ut.begin();
      return true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean commitTransaction() {
    TestUtil.logTrace("commitTransaction");
    try {
      ut.commit();
      return true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  // ===========================================================
}
