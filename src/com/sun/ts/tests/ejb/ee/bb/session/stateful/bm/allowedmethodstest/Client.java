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
 * $Id$
 */

package com.sun.ts.tests.ejb.ee.bb.session.stateful.bm.allowedmethodstest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.transaction.*;
import java.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {
  private static final String testName = "AllowedmethodsTest";

  private static final String testLookup = "java:comp/env/ejb/TestBean";

  private static final String helperLookup = "java:comp/env/ejb/Helper";

  private static final String testProps = "allowedmethodstest.properties";

  private static final String testDir = System.getProperty("user.dir");

  private TestBean beanRef = null;

  private TestBeanHome beanHome = null;

  private Helper helperRef = null;

  private HelperHome helperHome = null;

  private Properties props = null;

  private TSNamingContext nctx = null;

  private Hashtable results = null;

  private boolean SKIP = false;

  private static final String user = "user", password = "password";

  private String user_value, password_value;

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
      logMsg(
          "Property object not found for method (" + method + ") ... Skipping");
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
    if (!p.getProperty("setRollbackOnly").equals(r[4])) {
      logErr("setRollbackOnly operations test failed");
      pass = false;
    }
    if (!p.getProperty("getEJBObject").equals(r[5])) {
      logErr("getEJBObject operations test failed");
      pass = false;
    }
    if (!p.getProperty("JNDI_Access").equals(r[6])) {
      logErr("JNDI_Access operations test failed");
      pass = false;
    }

    if (!p.getProperty("UserTransaction").equals(r[7])) {
      logErr("UserTransaction operations test failed");
      pass = false;
    }

    if (!p.getProperty("UserTransaction_Methods_Test1").equals(r[8])) {
      logErr("UserTransaction_Methods_Test1 operations test failed");
      pass = false;
    }
    if (!p.getProperty("UserTransaction_Methods_Test2").equals(r[9])) {
      logErr("UserTransaction_Methods_Test2 operations test failed");
      pass = false;
    }
    if (!p.getProperty("UserTransaction_Methods_Test3").equals(r[10])) {
      logErr("UserTransaction_Methods_Test3 operations test failed");
      pass = false;
    }
    if (!p.getProperty("UserTransaction_Methods_Test4").equals(r[11])) {
      logErr("UserTransaction_Methods_Test4 operations test failed");
      pass = false;
    }
    if (!p.getProperty("getEJBLocalHome").equals(r[12])) {
      logErr("getEJBLocalHome operations test failed");
      pass = false;
    }
    if (!p.getProperty("getEJBLocalObject").equals(r[13])) {
      logErr("getEJBLocalObject operations test failed");
      pass = false;
    }
    if (!p.getProperty("Timer_Methods").equals(r[14])) {
      logErr("Timer_Service_Methods operations test failed");
      pass = false;
    }

    if (pass) {
      logMsg("All operation tests passed as expected ...");
    } else if (SKIP) {
      logMsg("ERROR: Unable to obtain test results");
      SKIP = false;
    } else {
      logErr("Not All operation tests passed - unexpected ...");
      logMsg("-----------------------------------------------------");
    }
    return pass;
  }

  /* Test setup */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * user; password;
   * 
   * @class.testArgs: -ap tssql.stmt
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    user_value = props.getProperty(user);
    password_value = props.getProperty(password);

    logMsg("user_value=" + user_value);
    logMsg("password_value=" + password_value);

    try {
      logMsg("Obtain naming context");
      nctx = new TSNamingContext();
      logMsg("Obtain login context and login as: " + user_value);
      TSLoginContext lc = new TSLoginContext();
      lc.login(user_value, password_value);

      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + testLookup);
      beanHome = (TestBeanHome) nctx.lookup(testLookup, TestBeanHome.class);
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
   * @testName: sfbmAllowedMethodsTest1
   * 
   * @assertion_ids: EJB:SPEC:92; EJB:SPEC:92.1; EJB:SPEC:92.2; EJB:SPEC:92.3;
   * EJB:SPEC:92.4; EJB:SPEC:92.5; EJB:SPEC:92.6; EJB:SPEC:92.7; EJB:SPEC:92.10;
   * EJB:SPEC:92.11; EJB:SPEC:92.12; EJB:SPEC:92.13; EJB:SPEC:92.14;
   * EJB:SPEC:92.15; EJB:SPEC:90; EJB:JAVADOC:183; EJB:JAVADOC:211
   *
   * @test_Strategy: Operations allowed and not allowed in the ejbCreate method
   * of a stateful session bean with bean-managed transaction demarcation are:
   * 
   * o getEJBHome - allowed o getCallerPrincipal - allowed o getRollbackOnly -
   * not allowed o isCallerInRole - allowed o setRollbackOnly - not allowed o
   * getEJBObject - allowed o JNDI_Access - allowed o UserTransaction_Access-
   * allowed o UserTransaction_Methods_Test1 - allowed o
   * UserTransaction_Methods_Test2 - allowed o UserTransaction_Methods_Test3 -
   * allowed o UserTransaction_Methods_Test4 - allowed o getEJBLocalHome -
   * allowed o getEJBLocalObject - allowed o Timer Methods - not allowed
   * 
   * Deploy it on the J2EE server. Verify correct operations.
   */

  public void sfbmAllowedMethodsTest1() throws Fault {
    logTrace("Operation Tests for ejbCreate");
    boolean pass = true;
    String expected[] = { "true", "true", "false", "true", "false", "true",
        "true", "true", "true", "true", "true", "true", "true", "true",
        "false" };
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 0);
      logMsg("get results");
      results = beanRef.getResults();
      logMsg("check results");
      pass = checkResults(results, "ejbCreate", expected);
      logMsg("check status");
    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault("sfbmAllowedMethodsTest1 failed", e);
    }

    try {
      beanRef.stopTestTimer();
    } catch (Exception e) {
      TestUtil.logErr("Exception caught removing timer", e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception caught removing bean", e);
      }
    }

    if (!pass)
      throw new Fault("sfbmAllowedMethodsTest1 failed");
  }

  /*
   * @testName: sfbmAllowedMethodsTest2
   * 
   * @assertion_ids: EJB:SPEC:93; EJB:SPEC:93.1; EJB:SPEC:93.2; EJB:SPEC:93.3;
   * EJB:SPEC:93.4; EJB:SPEC:93.5; EJB:SPEC:93.6; EJB:SPEC:93.7; EJB:SPEC:93.10;
   * EJB:SPEC:93.11; EJB:SPEC:93.12; EJB:SPEC:93.14; EJB:JAVADOC:211;
   * EJB:JAVADOC:161
   * 
   * @test_Strategy: Operations allowed and not allowed in the setSessionContext
   * method of a stateful session bean with bean-managed transaction demarcation
   * are:
   *
   * o getEJBHome - allowed o getCallerPrincipal - not allowed o getRollbackOnly
   * - not allowed o isCallerInRole - not allowed o setRollbackOnly - not
   * allowed o getEJBObject - not allowed o JNDI_Access - allowed o
   * UserTransaction_Access- not allowed o UserTransaction_Methods_Test1 - not
   * allowed o UserTransaction_Methods_Test2 - not allowed o
   * UserTransaction_Methods_Test3 - not allowed o UserTransaction_Methods_Test4
   * - not allowed o getEJBLocalHome - allowed o getEJBLocalObject - not allowed
   * o Timer Methods - not allowed (not tested)
   * 
   * Deploy it on the J2EE server. Verify correct operations.
   */

  public void sfbmAllowedMethodsTest2() throws Fault {
    TestUtil.logTrace("Operation Tests for setSessionContext");
    boolean pass = true;
    String expected[] = { "true", "false", "false", "false", "false", "false",
        "true", "false", "false", "false", "false", "false", "true", "false",
        "false" };
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1);
      results = beanRef.getResults();
      pass = checkResults(results, "setSessionContext", expected);
    } catch (Exception e) {
      throw new Fault("sfbmAllowedMethodsTest2 failed", e);
    }

    try {
      beanRef.stopTestTimer();
    } catch (Exception e) {
      TestUtil.logErr("Exception caught removing timer", e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception caught removing bean", e);
      }
    }

    if (!pass)
      throw new Fault("sfbmAllowedMethodsTest2 failed");
  }

  /*
   * @testName: sfbmAllowedMethodsTest3
   * 
   * @assertion_ids: EJB:SPEC:94; EJB:SPEC:94.1; EJB:SPEC:94.2; EJB:SPEC:94.3;
   * EJB:SPEC:94.4; EJB:SPEC:94.5; EJB:SPEC:94.6; EJB:SPEC:94.7; EJB:SPEC:94.10;
   * EJB:SPEC:94.11; EJB:SPEC:94.12; EJB:SPEC:94.13; EJB:SPEC:94.14;
   * EJB:SPEC:94.15; EJB:JAVADOC:210; EJB:JAVADOC:206; EJB:JAVADOC:202;
   * EJB:JAVADOC:198; EJB:JAVADOC:194
   *
   * @test_Strategy: Operations allowed and not allowed in a business method of
   * a stateful session bean with bean-managed transaction demarcation are:
   *
   * o getEJBHome - allowed o getCallerPrincipal - allowed o getRollbackOnly -
   * not allowed o isCallerInRole - allowed o setRollbackOnly - not allowed o
   * getEJBObject - allowed o JNDI_Access - allowed o Resource_Access - allowed
   * o UserTransaction_Access- allowed o UserTransaction_Methods_Test1 - allowed
   * o UserTransaction_Methods_Test2 - allowed o UserTransaction_Methods_Test3 -
   * allowed o UserTransaction_Methods_Test4 - allowed o getEJBLocalHome -
   * allowed o getEJBLocalObject - allowed o Timer Methods - allowed
   * 
   * Deploy it on the J2EE server. Verify correct operations.
   * 
   */

  public void sfbmAllowedMethodsTest3() throws Fault {
    TestUtil.logTrace("Operation Tests for businessMethod");
    boolean pass = true;
    String expected[] = { "true", "true", "false", "true", "false", "true",
        "true", "true", "true", "true", "true", "true", "true", "true",
        "true" };
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1);
      logMsg("Calling EJB business method");
      beanRef.businessMethod();
      results = beanRef.getResults();
      pass = checkResults(results, "businessMethod", expected);
    } catch (Exception e) {
      throw new Fault("sfbmAllowedMethodsTest3 failed", e);
    }

    try {
      beanRef.stopTestTimer();
    } catch (Exception e) {
      TestUtil.logErr("Exception caught removing timer", e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception caught removing bean", e);
      }
    }

    if (!pass)
      throw new Fault("sfbmAllowedMethodsTest3 failed");
  }

  /*
   * @testName: sfbmAllowedMethodsTest4
   * 
   * @assertion_ids: EJB:SPEC:579
   *
   * @test_Strategy: For a bean-managed stateful session bean, attempt to call
   * ut.begin() after a TX has already been started. This is disallowed so check
   * that a javax.transaction. NotSupportedException is thrown.
   *
   */

  public void sfbmAllowedMethodsTest4() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 99);
      logMsg("Calling EJB test method");
      pass = beanRef.testUTBegin();
    } catch (Exception e) {
      throw new Fault("sfbmAllowedMethodsTest4 failed", e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception caught removing bean", e);
      }
    }

    if (!pass)
      throw new Fault("sfbmAllowedMethodsTest4 failed");

  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
