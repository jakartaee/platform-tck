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
 * @(#)lTestEJB.java	1.17 03/05/16
 */

package com.sun.ts.tests.ejb.ee.sec.stateless.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;

public class lTestEJB implements SessionBean {

  // JNDI names for looking up ejbs
  private static final String ejb1name = "java:comp/env/ejb/SecTestLocal";

  private static final String ejb2name = "java:comp/env/ejb/SecTestRoleRefLocal";

  // references to ejb interfaces
  private SecTestLocalHome ejb1home = null;

  private SecTestLocal ejb1ref = null;

  private SecTestRoleRefLocalHome ejb2home = null;

  private SecTestRoleRefLocal ejb2ref = null;

  private SessionContext sctx = null;

  private static final String UserNameProp = "user";

  private static final String UserPasswordProp = "password";

  private String username = "";

  private String password = "";

  private TSNamingContext nctx = null;

  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("ejbCreate");
  }

  public void initLogging(java.util.Properties p) {
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("TestEJB initLogging failed.");
      throw new EJBException(e.getMessage());
    }
  }

  public void setSessionContext(SessionContext sc) {
    sctx = sc;
    try {
      nctx = new TSNamingContext();

      ejb1home = (SecTestLocalHome) nctx.lookup(ejb1name);
      ejb2home = (SecTestRoleRefLocalHome) nctx.lookup(ejb2name);
      TestUtil.logMsg("setSessionContext in TestEJB");
    } catch (Exception e) {
      TestUtil.logErr("Exception in Setup: ", e);
    }
  }

  public void ejbRemove() {
  }

  public void ejbActivate() {
  }

  public void ejbPassivate() {
  }

  public boolean IsCallerB1(String caller) {
    String name = sctx.getCallerPrincipal().getName();
    TestUtil.logMsg("IsCallerB1: " + name);

    if (name.indexOf(caller) < 0)
      return false;
    else
      return true;
  }

  public boolean IsCallerB2(String caller, java.util.Properties p) {
    try {
      ejb1ref = ejb1home.create();
      ejb1ref.initLogging(p);
      boolean result = ejb1ref.IsCaller(caller);
      ejb1ref.remove();
      return result;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean InRole(String role, java.util.Properties p) {
    try {
      ejb1ref = ejb1home.create();
      ejb1ref.initLogging(p);
      boolean result = ejb1ref.EjbSecRoleRef(role);
      ejb1ref.remove();
      return result;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean EjbNotAuthz(java.util.Properties p) {
    try {
      ejb1ref = ejb1home.create();
      ejb1ref.initLogging(p);
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

  public boolean EjbIsAuthz(java.util.Properties p) {
    TestUtil.logMsg("Starting Caller authorization test");
    try {
      ejb1ref = ejb1home.create();
      ejb1ref.initLogging(p);
      boolean result = ejb1ref.EjbIsAuthz();
      ejb1ref.remove();

      if (!result)
        return false;

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      return false;
    }
    return true;
  }

  public boolean EjbSecRoleRef(String role, java.util.Properties p) {
    TestUtil.logMsg("Starting Security role reference positive test");
    try {
      ejb1ref = ejb1home.create();
      ejb1ref.initLogging(p);
      boolean result = ejb1ref.EjbSecRoleRef(role);
      ejb1ref.remove();

      if (!result)
        return false;
      return true;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean EjbSecRoleRef1(String role, java.util.Properties p) {
    TestUtil.logMsg("Starting Security role reference negative test");
    try {
      ejb1ref = ejb1home.create();
      ejb1ref.initLogging(p);
      boolean result = ejb1ref.EjbSecRoleRef(role);
      ejb1ref.remove();

      if (result)
        return false;
      return true;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean EjbSecRoleRefScope(String role, java.util.Properties p) {
    try {
      ejb1ref = ejb1home.create();
      ejb1ref.initLogging(p);

      // caller must be in security role linked to emp_secrole_ref. call must
      // succeed.
      boolean result = ejb1ref.EjbSecRoleRef(role);
      ejb1ref.remove();

      if (!result)
        return false;

      ejb2ref = ejb2home.create();
      ejb2ref.initLogging(p);
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
      java.util.Properties p) {
    TestUtil.logMsg("Starting Overloaded security role references test");
    try {
      ejb1ref = ejb1home.create();
      ejb1ref.initLogging(p);
      boolean result = ejb1ref.EjbOverloadedSecRoleRefs(role1);
      ejb1ref.remove();

      if (!result) {
        TestUtil
            .logErr("EjbOverloadedSecRoleRefs(emp_secrole_ref) returned false");
        return false;
      }

      ejb1ref = ejb1home.create();
      ejb1ref.initLogging(p);
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

  public boolean checktest1(java.util.Properties p) {
    try {
      ejb1ref = ejb1home.create();
      ejb1ref.initLogging(p);

      boolean result = ejb1ref.checktest1();
      ejb1ref.remove();

      return result;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean excludetest1(java.util.Properties p) {
    try {
      ejb1ref = ejb1home.create();

      boolean result = ejb1ref.excludetest1();
      ejb1ref.remove();
      TestUtil.logTrace("Should not be here.");
      return false;

    } catch (javax.ejb.EJBException e) {
      TestUtil.logTrace("Got expected EJBException");
      return true;
    } catch (Exception ex) {
      TestUtil.logErr("Got wrong Exception in excludetest1:", ex);
      return false;
    }
  }

}
