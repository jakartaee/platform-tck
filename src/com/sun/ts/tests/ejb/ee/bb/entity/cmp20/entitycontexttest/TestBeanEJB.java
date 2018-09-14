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
 * @(#)TestBeanEJB.java	1.3 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.entitycontexttest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;
import java.security.Principal;
import javax.transaction.*;

public abstract class TestBeanEJB implements EntityBean, TimedObject {
  private EntityContext ectx = null;

  private Properties harnessProps = null;

  private TSNamingContext nctx = null;

  private String role = "Administrator";

  // ===========================================================
  // getters and setters for cmp fields

  public abstract String getId();

  public abstract void setId(String i);

  public abstract String getBrandName();

  public abstract void setBrandName(String s);

  // ===========================================================

  public String ejbCreate(String id, String brandName) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      setId(id);
      setBrandName(brandName);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(String id, String brandName)
      throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
  }

  public void setEntityContext(EntityContext c) {
    TestUtil.logTrace("setEntityContext");
    ectx = c;
    try {
      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();
    } catch (Exception e) {
      TestUtil.logErr("ERROR: Exception Caught ... " + e, e);
      throw new EJBException("unable to obtain naming context");
    }
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("unsetEntityContext");
  }

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
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

  public void ejbTimeout(javax.ejb.Timer timer) {
    TestUtil.logTrace("ejbTimeout");
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

  public boolean getEJBLocalObjectTest() {
    TestUtil.logTrace("getEJBLocalObjectTest");
    try {
      EJBLocalObject lobject = ectx.getEJBLocalObject();
      if (lobject != null) {
        TestUtil.logMsg(
            "getEJBLocalObject() returned" + " EJBLocalObject reference");
        return true;
      } else {
        TestUtil.logErr("getEJBLocalObject() returned null reference");
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean getEJBLocalHomeTest() {
    TestUtil.logTrace("getEJBLocalHomeTest");
    try {
      TestBeanLocalHome lhome = (TestBeanLocalHome) ectx.getEJBLocalHome();
      if (lhome != null) {
        TestUtil.logMsg("getEJBLocalHome() returned EJBLocalHome reference");
        return true;
      } else {
        TestUtil.logErr("getEJBLocalHome() returned null reference");
        return false;
      }
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

  public boolean getPrimaryKeyTest(String pk) {
    TestUtil.logTrace("getPrimaryKeyTest");
    try {
      Object o = ectx.getPrimaryKey();
      if (o == null) {
        TestUtil.logErr("primary key returned null");
        return false;
      }

      if (!(o instanceof String)) {
        TestUtil.logErr("primary key is not an instance of String");
        return false;
      }
      String s = (String) o;

      if (s.equals(pk)) {
        TestUtil
            .logMsg("primaryKey match, expected: " + pk + " received: " + s);
        return true;
      } else {
        TestUtil
            .logErr("primaryKey mismatch, expected: " + pk + " received: " + s);
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean getTimerServiceTest() {
    TestUtil.logTrace("getTimerServiceTest");
    try {
      javax.ejb.TimerService ts = ectx.getTimerService();
      if (ts instanceof javax.ejb.TimerService) {
        TestUtil.logMsg("getTimerService() returned Timer Service reference");
        return true;
      } else {
        TestUtil.logErr("getTimerService() returned invalid reference");
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    }
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

  public String getIt(String envEntryName) {
    return doIt(envEntryName);
  }

  public String setIt(String envEntryName) {
    return doIt(envEntryName);
  }

  private String doIt(String envEntryName) {
    String user = null;
    try {
      user = (String) nctx.lookup("java:comp/env/" + envEntryName);
    } catch (Exception e) {
      user = TestUtil.printStackTraceToString(e);
    }
    return user;
  }

}
