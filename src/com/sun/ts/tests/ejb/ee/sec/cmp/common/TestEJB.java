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
 * @(#)TestEJB.java	1.12 03/05/16
 */

package com.sun.ts.tests.ejb.ee.sec.cmp.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;

import java.sql.*;
import javax.naming.*;

public class TestEJB implements EntityBean {

  // JNDI names for looking up ejbs
  private static final String ejb1name = "java:comp/env/ejb/SecTest";

  private static final String ejb2name = "java:comp/env/ejb/SecTestRoleRef";

  // references to ejb interfaces
  private SecTestHome ejb1home = null;

  private SecTest ejb1ref = null;

  private SecTest ejb1ref2 = null;

  private SecTestRoleRefHome ejb2home = null;

  private SecTestRoleRef ejb2ref = null;

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

  public void TestEJB() throws CreateException {
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

      ejb1home = (SecTestHome) nctx.lookup(ejb1name, SecTestHome.class);
      ejb2home = (SecTestRoleRefHome) nctx.lookup(ejb2name,
          SecTestRoleRefHome.class);

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
      ejb1ref = ejb1home.create(props, newTable, 1, "coffee-1", 1);
      TestUtil.logMsg("Created SecTest EJB");

      TestUtil
          .logMsg("verifying caller principal " + caller + "in SecTest EJB");
      boolean result = ejb1ref.IsCaller(caller);
      return result;
    } catch (Exception e) {
      TestUtil.logErr("ERROR: Exception caught in method IsCallerB2", e);
      return false;
    } finally {
      try {
        if (ejb1ref != null) {
          TestUtil.logTrace("removing ejb1ref in IsCallerB2");
          ejb1ref.remove();
          TestUtil.logTrace("ejb1ref in IsCallerB2 removed");
        }
      } catch (Exception e) {
        TestUtil.logErr("ERROR: Exception caught removing bean in IsCallerB2",
            e);
      }
    }
  }

  public boolean InRole(String role, java.util.Properties props) {
    try {
      ejb1ref = ejb1home.create(props, newTable, 1, "coffee-1", 1);
      boolean result = ejb1ref.EjbSecRoleRef(role);
      return result;
    } catch (Exception e) {
      TestUtil.logErr("ERROR: Exception caught in method InRole", e);
      return false;
    } finally {
      try {
        if (ejb1ref != null) {
          TestUtil.logTrace("removing ejb1ref in InRole");
          ejb1ref.remove();
          TestUtil.logTrace("ejb1ref in InRole removed");
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception caught removing bean in InRole", e);
      }
    }
  }

  public boolean EjbNotAuthz(java.util.Properties props) {
    try {
      ejb1ref = ejb1home.create(props, newTable, 1, "coffee-1", 1);

      ejb1ref.EjbNotAuthz();
      TestUtil.logErr(
          "Method EjbNotAuthz did not generate an expected java.rmi.RemoteException");
      TestUtil.logTrace("removing ejb1ref in EjbNotAuthz");
      cleanup(ejb1ref);
      TestUtil.logTrace("ejb1ref in EjbNotAuthz removed");
      return false;
    } catch (java.rmi.RemoteException e) {
      TestUtil.logMsg("Caught java.rmi.RemoteException as expected");
      cleanup(ejb1ref);
      return true;
    } catch (Exception e) {
      TestUtil.logErr("Exception caught during EjbNotAuthz", e);
      cleanup(ejb1ref);
      return false;
    }
  }

  private void cleanup(SecTest ejbref) {
    try {
      if (ejbref != null) {
        ejbref.remove();
        ejbref = null;
      } else
        TestUtil.logMsg("ejbref == null");
    } catch (Exception ex) {
      TestUtil.logErr("ERROR: trying to remove the bean in cleanup: ", ex);
    }
  }

  public boolean EjbIsAuthz(java.util.Properties props) {
    TestUtil.logMsg("Starting Caller authorization test");
    try {
      ejb1ref = ejb1home.create(props, newTable, 1, "coffee-1", 1);
      boolean result = ejb1ref.EjbIsAuthz();
      return result;
    } catch (Exception e) {
      TestUtil.logErr("ERROR: Exception caught in method EjbIsAuthz", e);
      return false;
    } finally {
      try {
        if (ejb1ref != null) {
          TestUtil.logTrace("removing ejb1ref in EjbIsAuthz");
          ejb1ref.remove();
          TestUtil.logTrace("ejb1ref in EjbIsAuthz removed");
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception caught removing bean in EjbIsAuthz", e);
      }
    }
  }

  public boolean EjbSecRoleRef(String role, java.util.Properties props) {
    TestUtil.logMsg("Starting Security role reference positive test");
    try {
      ejb1ref = ejb1home.create(props, newTable, 1, "coffee-1", 1);
      boolean result = ejb1ref.EjbSecRoleRef(role);
      return result;
    } catch (Exception e) {
      TestUtil.logErr("ERROR: Exception caught in method EjbSecRoleRef", e);
      return false;
    } finally {
      try {
        if (ejb1ref != null) {
          TestUtil.logTrace("removing ejb1ref in EjbSecRoleRef");
          ejb1ref.remove();
          TestUtil.logTrace("ejb1ref in EjbSecRoleRef removed");
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception caught removing bean in EjbSecRoleRef", e);
      }
    }

  }

  public boolean EjbSecRoleRef1(String role, java.util.Properties props) {
    boolean result = true;
    TestUtil.logMsg("Starting Security role reference negative test");
    try {
      ejb1ref = ejb1home.create(props, newTable, 1, "coffee-1", 1);
      result = ejb1ref.EjbSecRoleRef(role);
    } catch (Exception e) {
      TestUtil.logErr("ERROR: Exception caught in method EjbSecRoleRef1", e);
      return false;
    } finally {
      try {
        if (ejb1ref != null) {
          TestUtil.logTrace("removing ejb1ref in EjbSecRoleRef1");
          ejb1ref.remove();
          TestUtil.logTrace("ejb1ref in EjbSecRoleRef1 removed");
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception caught removing bean in EjbSecRoleRef1", e);
      }
    }

    if (result)
      return false;
    return true;
  }

  public boolean EjbSecRoleRefScope(String role, java.util.Properties props) {
    boolean result1 = true;
    boolean result2 = true;
    try {
      ejb1ref = ejb1home.create(props, newTable, 1, "coffee-1", 1);
      // caller must be in security role linked to emp_secrole_ref. call must
      // succeed.
      result1 = ejb1ref.EjbSecRoleRef(role);
    } catch (Exception e) {
      TestUtil.logErr("ERROR: Exception caught in method EjbSecRoleRefScope",
          e);
      return false;
    } finally {
      try {
        if (ejb1ref != null) {
          TestUtil.logTrace("removing ejb1ref in EjbSecRoleRefScope");
          ejb1ref.remove();
          TestUtil.logTrace("ejb1ref in EjbSecRoleRefScope removed");
        }
      } catch (Exception e) {
        TestUtil.logErr(
            "Exception caught removing ejb1ref in EjbSecRoleRefScope", e);
      }
    }

    if (!result1)
      return false;

    try {
      ejb2ref = ejb2home.create(props, newTable, 2, "coffee-1", 1);
      result2 = ejb2ref.EjbSecRoleRefScope(role);
    } catch (Exception e) {
      TestUtil.logErr("ERROR: Exception caught in method EjbSecRoleRefScope",
          e);
      return false;
    } finally {
      try {
        if (ejb2ref != null) {
          TestUtil.logTrace("removing ejb2ref in EjbSecRoleRefScope");
          ejb2ref.remove();
          TestUtil.logTrace("ejb2ref in EjbSecRoleRefScope removed");
        }
      } catch (Exception e) {
        TestUtil.logErr(
            "Exception caught removing ejb2ref in EjbSecRoleRefScope", e);
      }

    }
    if (result2)
      return false;
    return true;
  }

  public boolean EjbOverloadedSecRoleRefs(String role1, String role2,
      java.util.Properties props) {
    TestUtil.logMsg("Starting Overloaded security role references test");
    boolean result1 = true;
    boolean result2 = true;
    try {
      ejb1ref = ejb1home.create(props, newTable, 1, "coffee-1", 1);
      result1 = ejb1ref.EjbOverloadedSecRoleRefs(role1);

    } catch (Exception e) {
      TestUtil.logErr("EjbOverloadedSecRoleRefs(" + role1 + "," + role2
          + ") failed with Exception: ", e);
      return false;
    } finally {
      try {
        if (ejb1ref != null) {
          TestUtil.logTrace("removing ejb1ref in EjbOverloadedSecRoleRefs");
          ejb1ref.remove();
          TestUtil.logTrace("ejb1ref in EjbOverloadedSecRoleRefs removed");
        }
      } catch (Exception e) {
        TestUtil.logErr(
            "Exception caught removing ejb1ref in EjbOverloadedSecRoleRefs", e);
      }

    }
    if (!result1)
      return false;

    try {
      ejb1ref2 = ejb1home.create(props, newTable, 2, "coffee-1", 1);
      result2 = ejb1ref2.EjbOverloadedSecRoleRefs(role1, role2);
    } catch (Exception e) {
      TestUtil.logErr("EjbOverloadedSecRoleRefs(" + role1 + "," + role2
          + ") failed with Exception: ", e);
      return false;
    } finally {
      try {
        if (ejb1ref2 != null) {
          TestUtil.logTrace("removing ejb1ref2 in EjbOverloadedSecRoleRefs");
          ejb1ref2.remove();
          TestUtil.logTrace("ejb1ref2 in EjbOverloadedSecRoleRefs removed");
        }
      } catch (Exception e) {
        TestUtil.logErr(
            "Exception caught removing ejb2ref in EjbOverloadedSecRoleRefs", e);
      }

    }
    if (result2)
      return false;
    return true;
  }

  public boolean checktest1(java.util.Properties props) {
    try {
      ejb1ref = ejb1home.create(props, newTable, 1, "coffee-1", 1);
      boolean result = ejb1ref.checktest1();
      return result;
    } catch (Exception e) {
      TestUtil.logErr("ERROR: Exception caught in method checktest1", e);
      return false;
    } finally {
      try {
        if (ejb1ref != null) {
          TestUtil.logTrace("removing ejb1ref in checktest1");
          ejb1ref.remove();
          TestUtil.logTrace("ejb1ref in checktest1 removed");
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception caught removing bean in checktest1", e);
      }
    }
  }

  public boolean excludetest1(java.util.Properties props) {
    try {
      ejb1ref = ejb1home.create(props, newTable, 1, "coffee-1", 1);
      boolean result = ejb1ref.excludetest1();
      TestUtil.logErr(
          "Method excludetest did not generate an expected java.rmi.RemoteException");
      TestUtil.logTrace("removing ejb1ref in excludetest1");
      cleanup(ejb1ref);
      TestUtil.logTrace("ejb1ref in excludetest1 removed");
      return false;
    } catch (java.rmi.RemoteException e) {
      TestUtil.logMsg("Caught java.rmi.RemoteException as expected");
      cleanup(ejb1ref);
      return true;
    } catch (Exception e) {
      TestUtil.logErr("ERROR: Unexpected exception caught during excludeTest1",
          e);
      cleanup(ejb1ref);
      return false;
    }
  }
}
