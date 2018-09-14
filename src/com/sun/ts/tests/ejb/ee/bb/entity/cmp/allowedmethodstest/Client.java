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
 * @(#)Client.java	1.28 03/05/27
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp.allowedmethodstest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.transaction.*;
import javax.rmi.PortableRemoteObject;
import java.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {
  private static final String testName = "AllowedmethodsTest";

  private static final String testBean = "java:comp/env/ejb/TestBean";

  private static final String testProps = "allowedmethodstest.properties";

  private static final String testDir = System.getProperty("user.dir");

  private static final String helperLookup = "java:comp/env/ejb/Helper";

  private TestBean beanRef = null;

  private TestBeanHome beanHome = null;

  private Helper helperRef = null;

  private HelperHome helperHome = null;

  private Properties props = null;

  private TSNamingContext nctx = null;

  private Hashtable results = null;

  private boolean SKIP = false;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  private boolean checkResults(Hashtable results, String method, String r[]) {
    logTrace("checkResults");
    logMsg("-----------------------------------------------------");
    boolean pass = true;
    TestUtil.logMsg("Getting results for method: (" + method + ")");
    Properties p = (Properties) results.get(method);
    if (p == null) {
      logMsg("ERROR: Property object not found for method (" + method
          + ") ... Skipping");
      SKIP = true;
      return false;
    }
    TestUtil.list(p);
    if (!p.getProperty("getEJBHome").equals(r[0])) {
      logErr("getEJBHome operations test failed");
      pass = false;
    }
    if (!p.getProperty("getCallerPrincipal").equals(r[1])) {
      logErr("getCallerPrincipal operations test failed");
      pass = false;
    }
    if (!p.getProperty("getRollbackOnly").equals(r[2])) {
      logErr("getRollbackOnly operations test failed");
      pass = false;
    }
    if (!p.getProperty("isCallerInRole").equals(r[3])) {
      logErr("isCallerInRole operations test failed");
      pass = false;
    }
    if (!p.getProperty("getEJBObject").equals(r[4])) {
      logErr("getEJBObject operations test failed");
      pass = false;
    }
    if (!p.getProperty("getPrimaryKey").equals(r[5])) {
      logErr("getPrimaryKey operations test failed");
      pass = false;
    }
    if (!p.getProperty("JNDI_Access").equals(r[6])) {
      logErr("JNDI_Access operations test failed");
      pass = false;
    }
    if (pass) {
      logMsg("All operation tests passed as expected ...");
    } else if (SKIP) {
      logMsg("ERROR: Unable to obtain test results");
      SKIP = false;
    } else {
      logErr("ERROR: Not All operation tests passed - unexpected ...");
      logMsg("-----------------------------------------------------");
    }
    return pass;

  }

  /* Test setup */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * generateSQL;
   * 
   * @class.testArgs: -ap tssql.stmt
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + testBean);
      beanHome = (TestBeanHome) nctx.lookup(testBean, TestBeanHome.class);
      logMsg("Looking up home interface for EJB: " + helperLookup);
      helperHome = (HelperHome) nctx.lookup(helperLookup, HelperHome.class);
      logMsg("Setup ok");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: cmpAllowedMethodsTest1
   * 
   * @assertion_ids: EJB:SPEC:453.1; EJB:SPEC:453.3; EJB:SPEC:453.4;
   * EJB:SPEC:453.5; EJB:SPEC:453.7; EJB:SPEC:453.9; EJB:SPEC:453.12
   * 
   * @test_Strategy: The following operations tests for ejbCreate are: o
   * getEJBHome - allowed o getCallerPrincipal - allowed o getRollbackOnly -
   * allowed o isCallerInRole - allowed o getEJBObject - not allowed o
   * getPrimaryKey - not allowed o JNDI_Access - allowed
   *
   * Create Entity CMP 1.1 bean. Deploy it on the J2EE server. Verify correct
   * operations.
   *
   */

  public void cmpAllowedMethodsTest1() throws Fault {
    logTrace("Operation Tests for ejbCreate");
    boolean pass = true;
    String expected[] = { "true", "true", "true", "true", "false", "false",
        "true" };
    try {
      // create Helper EJB instance
      logMsg("Create Helper EJB instance");
      helperRef = (Helper) helperHome.create();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, helperRef, 1, "coffee-1", 1,
          1);
      results = helperRef.getData();
      pass = checkResults(results, "ejbCreate", expected);
      helperRef.remove();
    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault("cmpAllowedMethodsTest1 failed", e);
    }
    try {
      if (beanRef != null) {
        beanRef.remove();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception caught removing bean:" + e, e);
    }

    if (!pass)
      throw new Fault("cmpAllowedMethodsTest1 failed");
  }

  /*
   * @testName: cmpAllowedMethodsTest2
   * 
   * @assertion_ids: EJB:SPEC:454.1; EJB:SPEC:454.3; EJB:SPEC:454.4;
   * EJB:SPEC:454.5; EJB:SPEC:454.7; EJB:SPEC:454.9; EJB:SPEC:454.12
   * 
   * @test_Strategy: The following operations tests for ejbPostCreate are: o
   * getEJBHome - allowed o getCallerPrincipal - allowed o getRollbackOnly -
   * allowed o isCallerInRole - allowed o getEJBObject - allowed o getPrimaryKey
   * - allowed o JNDI_Access - allowed
   *
   * Create Entity CMP 1.1 bean. Deploy it on the J2EE server. Verify correct
   * operations.
   *
   */

  public void cmpAllowedMethodsTest2() throws Fault {
    TestUtil.logTrace("Operation Tests for ejbPostCreate");
    boolean pass = true;
    String expected[] = { "true", "true", "true", "true", "true", "true",
        "true" };
    try {
      // create Helper EJB instance
      logMsg("Create Helper EJB instance");
      helperRef = (Helper) helperHome.create();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, helperRef, 1, "coffee-1", 1,
          2);
      results = helperRef.getData();
      pass = checkResults(results, "ejbPostCreate", expected);
      helperRef.remove();
    } catch (Exception e) {
      throw new Fault("cmpAllowedMethodsTest2 failed", e);
    }

    try {
      if (beanRef != null) {
        beanRef.remove();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception caught removing bean:" + e, e);
    }

    if (!pass)
      throw new Fault("cmpAllowedMethodsTest2 failed");
  }

  /*
   * @testName: cmpAllowedMethodsTest3
   * 
   * @assertion_ids: EJB:SPEC:463.1; EJB:SPEC:463.3; EJB:SPEC:463.4;
   * EJB:SPEC:463.5; EJB:SPEC:463.7; EJB:SPEC:463.9; EJB:SPEC:463.12
   * 
   * @test_Strategy: The following operations tests for setEntityContext are: o
   * getEJBHome - allowed o getCallerPrincipal - not allowed o getRollbackOnly -
   * not allowed o isCallerInRole - not allowed o getEJBObject - not allowed o
   * getPrimaryKey - not allowed o JNDI_Access - allowed
   *
   * Create Entity CMP 1.1 bean. Deploy it on the J2EE server. Verify correct
   * operations.
   *
   */

  public void cmpAllowedMethodsTest3() throws Fault {
    TestUtil.logTrace("Operation Tests for setEntityContext");
    boolean pass = true;
    String expected[] = { "true", "false", "false", "false", "false", "false",
        "true" };
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);
      results = beanRef.getResults();
      pass = checkResults(results, "setEntityContext", expected);
    } catch (Exception e) {
      throw new Fault("cmpAllowedMethodsTest3 failed", e);
    }

    try {
      if (beanRef != null) {
        beanRef.remove();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception caught removing bean:" + e, e);
    }

    if (!pass)
      throw new Fault("cmpAllowedMethodsTest3 failed");
  }

  /*
   * @testName: cmpAllowedMethodsTest4
   * 
   * @assertion_ids: EJB:SPEC:461.1; EJB:SPEC:461.3; EJB:SPEC:461.4;
   * EJB:SPEC:461.5; EJB:SPEC:461.7; EJB:SPEC:461.9; EJB:SPEC:461.12
   * 
   * @test_Strategy: The following operations tests for businessMethod are: o
   * getEJBHome - allowed o getCallerPrincipal - allowed o getRollbackOnly -
   * allowed o isCallerInRole - allowed o getEJBObject - allowed o getPrimaryKey
   * - allowed o JNDI_Access - allowed
   *
   * Deploy it on the J2EE server. Verify correct operations.
   *
   */

  public void cmpAllowedMethodsTest4() throws Fault {
    TestUtil.logTrace("Operation Tests for businessMethod");
    boolean pass = true;
    String expected[] = { "true", "true", "true", "true", "true", "true",
        "true" };
    try {
      // create Helper EJB instance
      logMsg("Create Helper EJB instance");
      helperRef = (Helper) helperHome.create();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);
      logMsg("Calling EJB business method");
      beanRef.businessMethod(helperRef);
      results = helperRef.getData();
      pass = checkResults(results, "businessMethod", expected);
      helperRef.remove();
    } catch (Exception e) {
      throw new Fault("cmpAllowedMethodsTest4 failed", e);
    }

    try {
      if (beanRef != null) {
        beanRef.remove();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception caught removing bean:" + e, e);
    }

    if (!pass)
      throw new Fault("cmpAllowedMethodsTest4 failed");
  }

  /*
   * @testName: cmpAllowedMethodsTest5
   * 
   * @assertion_ids: EJB:SPEC:459.1; EJB:SPEC:459.3; EJB:SPEC:459.4;
   * EJB:SPEC:459.5; EJB:SPEC:459.7; EJB:SPEC:459.9; EJB:SPEC:459.12
   * 
   * @test_Strategy: The following operations tests for ejbLoad are: o
   * getEJBHome - allowed o getCallerPrincipal - allowed o getRollbackOnly -
   * allowed o isCallerInRole - allowed o getEJBObject - allowed o getPrimaryKey
   * - allowed o JNDI_Access - allowed
   *
   * Create Entity CMP 1.1 bean. Deploy it on the J2EE server. Verify correct
   * operations.
   *
   */

  public void cmpAllowedMethodsTest5() throws Fault {
    TestUtil.logTrace("Operation Tests for ejbLoad");
    boolean pass = true;
    String expected[] = { "true", "true", "true", "true", "true", "true",
        "true" };
    try {
      // create Helper EJB instance
      logMsg("Create Helper EJB instance");
      helperRef = (Helper) helperHome.create();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1, "coffee-1", 1);
      logMsg("Calling EJB business method");
      try {
        beanRef.businessMethod(helperRef);
      } catch (RemoteException e) {
        TestUtil.printStackTrace(e);
      }
      results = helperRef.getData();
      pass = checkResults(results, "ejbLoad", expected);
      helperRef.remove();
    } catch (Exception e) {
      throw new Fault("cmpAllowedMethodsTest5 failed", e);
    }

    try {
      if (beanRef != null) {
        beanRef.remove();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception caught removing bean:" + e, e);
    }

    if (!pass)
      throw new Fault("cmpAllowedMethodsTest5 failed");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
