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
 * @(#)lTestEJB.java	1.16 03/05/16
 */

package com.sun.ts.tests.ejb.ee.sec.cmp20.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;

import java.sql.*;
import javax.naming.*;

public class lTestEJB implements EntityBean {

  // JNDI names for looking up ejbs
  private static final String ejb1name = "java:comp/env/ejb/SecTestLocal";

  private static final String ejb2name = "java:comp/env/ejb/SecTestRoleRefLocal";

  // references to ejb interfaces
  private SecTestLocalHome ejb1home = null;

  private SecTestLocal ejb1ref = null;

  private SecTestRoleRefLocalHome ejb2home = null;

  private SecTestRoleRefLocal ejb2ref = null;

  private EntityContext ectx = null;

  private boolean newTable = true;

  public String BRAND_NAME;

  public Integer KEY_ID;

  public float PRICE;

  private static final String UserNameProp = "user";

  private static final String UserPasswordProp = "password";

  private String username = "";

  private String password = "";

  private TSNamingContext nctx = null;

  public void lTestEJB() throws CreateException {
    TestUtil.logTrace("TestEJB ejbCreate OK!");
  }

  public Integer ejbCreate(Properties p, boolean newTable, int KEY_ID,
      String BRAND_NAME, float PRICE) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
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

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("ejbRemove");
  }

  public void ejbPostCreate(Properties p, boolean newTable, int KEY_ID,
      String BRAND_NAME, float PRICE) {
    TestUtil.logTrace("In ejbPostCreate !!");
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
  }

  public void setEntityContext(EntityContext sc) {
    ectx = sc;
    try {
      nctx = new TSNamingContext();

      ejb1home = (SecTestLocalHome) nctx.lookup(ejb1name);
      ejb2home = (SecTestRoleRefLocalHome) nctx.lookup(ejb2name);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("unable to obtain naming context");
    }
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

  public boolean IsCallerB1(String caller) {
    String name = ectx.getCallerPrincipal().getName();
    TestUtil.logMsg("IsCallerB1: " + name);

    if (name.indexOf(caller) < 0)
      return false;
    else
      return true;
  }

  public boolean IsCallerB2(String caller, java.util.Properties props) {
    try {
      ejb1ref = ejb1home.create(1, "coffee-1", 1);

      boolean result = ejb1ref.IsCaller(caller);
      ejb1ref.remove();
      return result;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      cleanup(ejb1ref);
      return false;
    }
  }

  public boolean InRole(String role, java.util.Properties props) {
    try {
      ejb1ref = ejb1home.create(1, "coffee-1", 1);

      boolean result = ejb1ref.EjbSecRoleRef(role);
      ejb1ref.remove();
      return result;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      cleanup(ejb1ref);
      return false;
    }
  }

  public boolean EjbNotAuthz(java.util.Properties props) {
    try {
      ejb1ref = ejb1home.create(1, "coffee-1", 1);

      ejb1ref.EjbNotAuthz();
      TestUtil.logErr(
          "Method call did not generate an expected java.rmi.RemoteException");
      ejb1ref.remove();
      return false;
    } catch (javax.ejb.EJBException e) {
      TestUtil.logMsg("Caught javax.ejb.EJBException as expected");
      cleanup(ejb1ref);
      return true;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      cleanup(ejb1ref);
      return false;
    }
  }

  private void cleanup(SecTestLocal ejbref) {

    if (ejbref != null)
      try {
        ejbref.remove();
        ejbref = null;
      } catch (Exception ex) {
        TestUtil.logErr("Cannot remove the bean: ", ex);
      }
    else
      TestUtil.logMsg("ejbref == null");
  }

  public boolean EjbIsAuthz(java.util.Properties props) {
    TestUtil.logMsg("Starting Caller authorization test");
    try {
      ejb1ref = ejb1home.create(1, "coffee-1", 1);

      boolean result = ejb1ref.EjbIsAuthz();
      ejb1ref.remove();

      if (!result)
        return false;

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      cleanup(ejb1ref);
      return false;
    }
    return true;
  }

  public boolean EjbSecRoleRef(String role, java.util.Properties props) {
    TestUtil.logMsg("Starting Security role reference positive test");
    try {
      ejb1ref = ejb1home.create(1, "coffee-1", 1);

      boolean result = ejb1ref.EjbSecRoleRef(role);
      ejb1ref.remove();

      if (!result)
        return false;
      return true;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      cleanup(ejb1ref);
      return false;
    }
  }

  public boolean EjbSecRoleRef1(String role, java.util.Properties props) {
    TestUtil.logMsg("Starting Security role reference negative test");
    try {
      ejb1ref = ejb1home.create(1, "coffee-1", 1);

      boolean result = ejb1ref.EjbSecRoleRef(role);
      ejb1ref.remove();

      if (result)
        return false;
      return true;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      cleanup(ejb1ref);
      return false;
    }
  }

  public boolean EjbSecRoleRefScope(String role, java.util.Properties props) {
    try {
      ejb1ref = ejb1home.create(1, "coffee-1", 1);

      // caller must be in security role linked to emp_secrole_ref. call must
      // succeed.
      boolean result = ejb1ref.EjbSecRoleRef(role);
      ejb1ref.remove();

      if (!result)
        return false;

      ejb2ref = ejb2home.create(1, "coffee-1", 1);
      result = ejb2ref.EjbSecRoleRefScope(role);
      ejb2ref.remove();

      if (result)
        return false;
      return true;

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean EjbOverloadedSecRoleRefs(String role1, String role2,
      java.util.Properties props) {
    TestUtil.logMsg("Starting Overloaded security role references test");
    try {
      ejb1ref = ejb1home.create(1, "coffee-1", 1);

      boolean result = ejb1ref.EjbOverloadedSecRoleRefs(role1);
      ejb1ref.remove();

      if (!result) {
        TestUtil
            .logErr("EjbOverloadedSecRoleRefs(emp_secrole_ref) returned false");
        return false;
      }

      ejb1ref = ejb1home.create(1, "coffee-1", 1);

      result = ejb1ref.EjbOverloadedSecRoleRefs(role1, role2);
      ejb1ref.remove();

      if (result) {
        TestUtil.logErr(
            "EjbOverloadedSecRoleRefs(emp_secrole_ref,mgr_secrole_ref) returned true");
        return false;
      }
      return true;
    } catch (Exception e) {
      TestUtil.logErr("EjbOverloadedSecRoleRefs(" + role1 + "," + role2
          + ") failed with Exception: ", e);
      return false;
    }
  }

  public boolean checktest1(java.util.Properties props) {
    TestUtil.logMsg("Starting unchecked test1");
    try {
      ejb1ref = ejb1home.create(1, "coffee-1", 1);

      boolean result = ejb1ref.checktest1();
      ejb1ref.remove();
      return result;
    } catch (Exception e) {
      TestUtil.logErr("checktest1 failed with Exception: ", e);
      return false;
    }
  }

  public boolean excludetest1(java.util.Properties props) {
    TestUtil.logMsg("Starting exclude test1");
    try {
      ejb1ref = ejb1home.create(1, "coffee-1", 1);

      boolean result = ejb1ref.excludetest1();
      ejb1ref.remove();
      return false;
    } catch (javax.ejb.EJBException ex) {
      TestUtil.logMsg("Got expected exception.");
      cleanup(ejb1ref);
      return true;
    } catch (Exception e) {
      TestUtil.logErr("excludetest1 failed with Exception: ", e);
      return false;
    }
  }

}
