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
 * @(#)TestBeanEJB.java	1.24 03/05/27
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp.allowedmethodstest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import java.rmi.*;
import java.sql.*;

public class TestBeanEJB implements EntityBean {
  private EntityContext ectx = null;

  private Properties harnessProps = null;

  private TSNamingContext nctx = null;

  private String role = "Administrator";

  private Hashtable table = new Hashtable();

  private static final String testLookup = "java:comp/env/ejb/Helper";

  // These are the method tests
  private static final String tests[] = { "ejbCreate", "ejbPostCreate",
      "ejbFindByPrimaryKey", "ejbActivate", "ejbPassivate", "ejbLoad",
      "ejbStore", "setEntityContext", "businessMethod" };

  // This is the results of the operation tests
  private static final Properties methodList[] = { new Properties(),
      new Properties(), new Properties(), new Properties(), new Properties(),
      new Properties(), new Properties(), new Properties(), new Properties() };

  private boolean ejbLoadCalled = false;

  private boolean ejbStoreCalled = false;

  // Entity instance data
  public String BRAND_NAME;

  public Integer KEY_ID;

  public float PRICE;

  public Integer ejbCreate(Properties p, Helper ref, int KEY_ID,
      String BRAND_NAME, float PRICE, int flag) throws CreateException {
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
    }
    if (flag == 1)
      doOperationTests("ejbCreate");
    try {
      ref.setData(table);
    } catch (Exception e) {
      TestUtil.logErr("Caught Exception: " + e, e);
      throw new CreateException("Exception occurred: " + e);
    }
    return this.KEY_ID;
  }

  public void ejbPostCreate(Properties p, Helper ref, int KEY_ID,
      String BRAND_NAME, float PRICE, int flag) throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
    if (flag == 2)
      doOperationTests("ejbPostCreate");
    try {
      ref.setData(table);
    } catch (Exception e) {
      TestUtil.logErr("Caught Exception: " + e, e);
      throw new CreateException("Exception occurred: " + e);
    }
  }

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
    } catch (Exception e) {
      TestUtil.logErr("ERROR: Exception Caught ... " + e, e);
      throw new EJBException("unable to obtain naming context");
    }
    doOperationTests("setEntityContext");
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("unsetEntityContext");
  }

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
    if (ejbLoadCalled)
      return;
    doOperationTests("ejbLoad");
    ejbLoadCalled = true;
    ejbStoreCalled = false;
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
    if (ejbStoreCalled)
      return;
    doOperationTests("ejbStore");
    ejbStoreCalled = true;
    ejbLoadCalled = false;
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("ejbRemove");
    TestUtil.logMsg("PrimaryKey=" + ectx.getPrimaryKey());
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
    doOperationTests("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
    doOperationTests("ejbPassivate");
  }

  // ===========================================================
  // TestBean interface (our business methods)

  public Hashtable getResults() {
    TestUtil.logTrace("getResults");
    return table;
  }

  public void businessMethod(Helper ref) {
    TestUtil.logTrace("businessMethod");
    try {
      doOperationTests("businessMethod");
      ref.setData(table);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

  // ===========================================================
  // Private methods

  private int testIndex(String s) {
    TestUtil.logTrace("testIndex");
    for (int i = 0; i < tests.length; i++)
      if (s.equals(tests[i]))
        return i;
    return -1;
  }

  private void setTestList(int i) {
    TestUtil.logTrace("setTestList");
    methodList[i].setProperty("JNDI_Access", "true");
    methodList[i].setProperty("getEJBHome", "true");
    methodList[i].setProperty("getCallerPrincipal", "true");
    methodList[i].setProperty("getRollbackOnly", "true");
    methodList[i].setProperty("isCallerInRole", "true");
    methodList[i].setProperty("getEJBObject", "true");
    methodList[i].setProperty("getPrimaryKey", "true");
  }

  private void doOperationTests(String s) {
    TestUtil.logTrace("doOperationTests");
    int i = testIndex(s);
    TestUtil.logMsg("index for " + s + " is " + i);
    TestUtil.logMsg("methodList length=" + methodList.length);
    TestUtil.logMsg("tests length=" + tests.length);
    setTestList(i);
    TestUtil.logMsg("Operations testing for " + s + " method ...");

    // getEJBHome test
    try {
      ectx.getEJBHome();
      TestUtil.logMsg("Operations test: getEJBHome() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getEJBHome", "false");
      TestUtil.logMsg("Operations test: getEJBHome() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getEJBHome", "unexpected");
      TestUtil.logMsg(
          "Operations test: getEJBHome() - not allowed (Unexpected Exception) - "
              + e);
    }

    // getCallerPrincipal test
    try {
      ectx.getCallerPrincipal();
      TestUtil.logMsg("Operations test: getCallerPrincipal() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getCallerPrincipal", "false");
      TestUtil.logMsg("Operations test: getCallerPrincipal() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getCallerPrincipal", "unexpected");
      TestUtil.logMsg(
          "Operations test: getCallerPrincipal() - not allowed (Unexpected Exception) - "
              + e);
    }

    // getRollbackOnly test
    try {
      ectx.getRollbackOnly();
      TestUtil.logMsg("Operations test: getRollbackOnly() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getRollbackOnly", "false");
      TestUtil.logMsg("Operations test: getRollbackOnly() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getRollbackOnly", "unexpected");
      TestUtil.logMsg(
          "Operations test: getRollbackOnly() - not allowed (Unexpected Exception) - "
              + e);
    }

    // isCallerInRole test
    try {
      ectx.isCallerInRole(role);
      TestUtil.logMsg("Operations test: isCallerInRole() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("isCallerInRole", "false");
      TestUtil.logMsg("Operations test: isCallerInRole() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("isCallerInRole", "unexpected");
      TestUtil.logMsg(
          "Operations test: isCallerInRole() - not allowed (Unexpected Exception) - "
              + e);
    }

    // getEJBObject test
    try {
      ectx.getEJBObject();
      TestUtil.logMsg("Operations test: getEJBObject() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getEJBObject", "false");
      TestUtil.logMsg("Operations test: getEJBObject() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getEJBObject", "unexpected");
      TestUtil.logMsg(
          "Operations test: getEJBObject() - not allowed (Unexpected Exception) - "
              + e);
    }

    // getPrimaryKey test
    try {
      ectx.getPrimaryKey();
      TestUtil.logMsg("Operations test: getPrimaryKey() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getPrimaryKey", "false");
      TestUtil.logMsg("Operations test: getPrimaryKey() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getPrimaryKey", "unexpected");
      TestUtil.logMsg(
          "Operations test: getPrimaryKey() - not allowed (Unexpected Exception) - "
              + e);
    }

    // JNDI Access test
    try {
      Context ctx = (Context) nctx.lookup("java:comp/env");
      TestUtil.logMsg("Operations test: JNDI_Access - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("JNDI_Access", "false");
      TestUtil.logMsg("Operations test: JNDI_Access() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("JNDI_Access", "unexpected");
      TestUtil.logMsg(
          "Operations test: JNDI_Access() - not allowed (Unexpected Exception) - "
              + e);
    }

    table.put(s, methodList[i]);
  }

  // ===========================================================
}
