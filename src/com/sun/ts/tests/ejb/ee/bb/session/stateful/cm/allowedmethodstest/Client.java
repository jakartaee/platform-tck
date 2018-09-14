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
 * @(#)Client.java	1.24 02/11/01
 */

package com.sun.ts.tests.ejb.ee.bb.session.stateful.cm.allowedmethodstest;

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

  private static final String testLookup2 = "java:comp/env/ejb/TestBeanNoTx";

  private static final String helperLookup = "java:comp/env/ejb/Helper";

  private static final String testProps = "allowedmethodstest.properties";

  private static final String testDir = System.getProperty("user.dir");

  private TestBean beanRef = null;

  private TestBeanHome beanHome = null;

  private TestBeanNoTx beanNoTxRef = null;

  private TestBeanNoTxHome beanNoTxHome = null;

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
    if (!p.getProperty("getEJBObject").equals(r[4])) {
      logErr("getEJBObject operations test failed");
      pass = false;
    }
    if (!p.getProperty("JNDI_Access").equals(r[5])) {
      logErr("JNDI_Access operations test failed");
      pass = false;
    }
    if (!p.getProperty("UserTransaction").equals(r[6])) {
      logErr("UserTransaction operations test failed");
      pass = false;
    }

    if (!p.getProperty("UserTransaction_Methods_Test1").equals(r[7])) {
      logErr("UserTransaction_Methods_Test1 operations test failed");
      pass = false;
    }
    if (!p.getProperty("UserTransaction_Methods_Test2").equals(r[8])) {
      logErr("UserTransaction_Methods_Test2 operations test failed");
      pass = false;
    }
    if (!p.getProperty("UserTransaction_Methods_Test3").equals(r[9])) {
      logErr("UserTransaction_Methods_Test3 operations test failed");
      pass = false;
    }
    if (!p.getProperty("UserTransaction_Methods_Test4").equals(r[10])) {
      logErr("UserTransaction_Methods_Test4 operations test failed");
      pass = false;
    }
    if (!p.getProperty("UserTransaction_Methods_Test5").equals(r[11])) {
      logErr("UserTransaction_Methods_Test5 operations test failed");
      pass = false;
    }
    if (!p.getProperty("UserTransaction_Methods_Test6").equals(r[12])) {
      logErr("UserTransaction_Methods_Test6 operations test failed");
      pass = false;
    }

    if (!p.getProperty("getEJBLocalHome").equals(r[13])) {
      logErr("getEJBLocalHome operations test failed");
      pass = false;
    }
    if (!p.getProperty("getEJBLocalObject").equals(r[14])) {
      logErr("getEJBLocalObject operations test failed");
      pass = false;
    }
    if (!p.getProperty("Timer_Methods").equals(r[15])) {
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

  private boolean checkResults(Hashtable results, String expected) {
    logTrace("checkResults");
    logMsg("-----------------------------------------------------");
    boolean pass = true;
    TestUtil.logMsg("Getting results for method: businessMethod");
    Properties p = (Properties) results.get("businessMethod");
    TestUtil.list(p);
    if (!p.getProperty("getRollbackOnly").equals(expected)) {
      logErr("getRollbackOnly operations test failed");
      pass = false;
    }
    if (!p.getProperty("setRollbackOnly").equals(expected)) {
      logErr("setRollbackOnly operations test failed");
      pass = false;
    }
    if (pass)
      logMsg("All operation tests passed as expected ...");
    else
      logErr("Not All operation tests passed - unexpected ...");
    logMsg("-----------------------------------------------------");
    return pass;
  }

  private boolean checkResults1(Hashtable results, String expected,
      String method) {
    logTrace("checkResults1");
    logMsg("-----------------------------------------------------");
    boolean pass = true;
    TestUtil.logMsg("Getting results for method: " + method);
    Properties p = (Properties) results.get(method);
    TestUtil.list(p);
    if (!p.getProperty("setRollbackOnly").equals(expected)) {
      logErr("setRollbackOnly operations test failed");
      pass = false;
    }
    if (pass)
      logMsg("All operation tests passed as expected ...");
    else
      logErr("Not All operation tests passed - unexpected ...");
    logMsg("-----------------------------------------------------");
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
      logMsg("Looking up home interface for EJB: " + testLookup2);
      beanNoTxHome = (TestBeanNoTxHome) nctx.lookup(testLookup2,
          TestBeanNoTxHome.class);
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
   * @testName: sfcmAllowedMethodsTest1
   * 
   * @assertion_ids: EJB:SPEC:81; EJB:SPEC:81.1; EJB:SPEC:81.2; EJB:SPEC:81.3;
   * EJB:SPEC:81.4; EJB:SPEC:81.6; EJB:SPEC:81.7; EJB:SPEC:81.10;
   * EJB:SPEC:81.11; EJB:SPEC:81.12; EJB:SPEC:81.13; EJB:SPEC:81.14;
   * EJB:SPEC:81.15; EJB:JAVADOC:195
   *
   * @test_Strategy: Operations allowed and not allowed in the ejbCreate method
   * of a stateful session bean with container-managed transaction demarcation
   * are:
   * 
   * o getEJBHome - allowed o getCallerPrincipal - allowed o getRollbackOnly -
   * not allowed o isCallerInRole - allowed o getEJBObject - allowed o
   * JNDI_Access - allowed o UserTransaction_Access- not allowed o
   * UserTransaction_Methods_Test1 - not allowed o UserTransaction_Methods_Test2
   * - not allowed o UserTransaction_Methods_Test3 - not allowed o
   * UserTransaction_Methods_Test4 - not allowed o UserTransaction_Methods_Test5
   * - not allowed o UserTransaction_Methods_Test6 - not allowed o
   * getEJBLocalHome - allowed o getEJBLocalObject - allowed o Timer Methods -
   * not allowed
   * 
   * Verify correct operations.
   *
   */

  public void sfcmAllowedMethodsTest1() throws Fault {
    logTrace("Operation Tests for ejbCreate");
    boolean pass = true;
    String expected[] = { "true", "true", "false", "true", "true", "true",
        "false", "false", "false", "false", "false", "false", "false", "true",
        "true", "false" };
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
      throw new Fault("sfcmAllowedMethodsTest1 failed", e);
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
      throw new Fault("sfcmAllowedMethodsTest1 failed");
  }

  /*
   * @testName: sfcmAllowedMethodsTest2
   * 
   * @assertion_ids: EJB:SPEC:82; EJB:SPEC:82.1; EJB:SPEC:82.2; EJB:SPEC:82.3;
   * EJB:SPEC:82.4; EJB:SPEC:82.6; EJB:SPEC:82.7; EJB:SPEC:82.10;
   * EJB:SPEC:82.11; EJB:SPEC:82.12; EJB:SPEC:82.14; EJB:JAVADOC:195;
   * EJB:JAVADOC:161
   * 
   * @test_Strategy: Operations allowed and not allowed in the setSessionContext
   * method of a stateful session bean with container-managed transaction
   * demarcation are: o getEJBHome - allowed o getCallerPrincipal - not allowed
   * o getRollbackOnly - not allowed o isCallerInRole - not allowed o
   * getEJBObject - not allowed o JNDI_Access - allowed o
   * UserTransaction_Access- not allowed o UserTransaction_Methods_Test1 - not
   * allowed o UserTransaction_Methods_Test2 - not allowed o
   * UserTransaction_Methods_Test3 - not allowed o UserTransaction_Methods_Test4
   * - not allowed o UserTransaction_Methods_Test5 - not allowed o
   * UserTransaction_Methods_Test6 - not allowed o getEJBLocalHome - allowed o
   * getEJBLocalObject - not allowed o Timer Methods - not allowed (not tested)
   *
   * Deploy it on the J2EE server. Verify correct operations.
   *
   */

  public void sfcmAllowedMethodsTest2() throws Fault {
    TestUtil.logTrace("Operation Tests for setSessionContext");
    boolean pass = true;
    String expected[] = { "true", "false", "false", "false", "false", "true",
        "false", "false", "false", "false", "false", "false", "false", "true",
        "false", "false" };
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 0);
      results = beanRef.getResults();
      pass = checkResults(results, "setSessionContext", expected);
    } catch (Exception e) {
      throw new Fault("sfcmAllowedMethodsTest2 failed", e);
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
      throw new Fault("sfcmAllowedMethodsTest2 failed");
  }

  /*
   * @testName: sfcmAllowedMethodsTest3
   * 
   * @assertion_ids: EJB:SPEC:83; EJB:SPEC:83.1; EJB:SPEC:83.2; EJB:SPEC:83.3;
   * EJB:SPEC:83.4; EJB:SPEC:83.6; EJB:SPEC:83.7; EJB:SPEC:83.10;
   * EJB:SPEC:83.11; EJB:SPEC:83.12; EJB:SPEC:83.13; EJB:SPEC:83.14;
   * EJB:SPEC:83.15; EJB:JAVADOC:210; EJB:JAVADOC:206; EJB:JAVADOC:202;
   * EJB:JAVADOC:198; EJB:JAVADOC:194
   * 
   * @test_Strategy: Operations allowed and not allowed in a business method of
   * a stateful session bean with container-managed transaction demarcation are:
   * o getEJBHome - allowed o getCallerPrincipal - allowed o getRollbackOnly -
   * allowed o isCallerInRole - allowed o getEJBObject - allowed o JNDI_Access -
   * allowed o UserTransaction_Access- not allowed o
   * UserTransaction_Methods_Test1 - not allowed o UserTransaction_Methods_Test2
   * - not allowed o UserTransaction_Methods_Test3 - not allowed o
   * UserTransaction_Methods_Test4 - not allowed o UserTransaction_Methods_Test5
   * - not allowed o UserTransaction_Methods_Test6 - not allowed o
   * getEJBLocalHome - allowed o getEJBLocalObject - allowed o Timer Methods -
   * allowed
   *
   * Deploy it on the J2EE server. Verify correct operations.
   *
   */

  public void sfcmAllowedMethodsTest3() throws Fault {
    TestUtil.logTrace("Operation Tests for businessMethod");
    boolean pass = true;
    String expected[] = { "true", "true", "true", "true", "true", "true",
        "false", "false", "false", "false", "false", "false", "false", "true",
        "true", "true" };
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 4);
      logMsg("Calling EJB business method");
      beanRef.businessMethod();
      results = beanRef.getResults();
      pass = checkResults(results, "businessMethod", expected);
    } catch (Exception e) {
      throw new Fault("sfcmAllowedMethodsTest3 failed", e);
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
      throw new Fault("sfcmAllowedMethodsTest3 failed");
  }

  /*
   * @testName: sfcmAllowedMethodsTest4
   * 
   * @assertion_ids: EJB:SPEC:85; EJB:SPEC:85.1; EJB:SPEC:85.2; EJB:SPEC:85.3;
   * EJB:SPEC:85.4; EJB:SPEC:85.6; EJB:SPEC:85.7; EJB:SPEC:85.10;
   * EJB:SPEC:85.11; EJB:SPEC:85.12; EJB:SPEC:85.13; EJB:SPEC:85.14;
   * EJB:SPEC:85.15; EJB:JAVADOC:210; EJB:JAVADOC:206; EJB:JAVADOC:202;
   * EJB:JAVADOC:198; EJB:JAVADOC:194; EJB:SPEC:80
   * 
   * @test_Strategy: Operations allowed and not allowed in the afterBegin method
   * of a stateful session bean with container-managed transaction demarcation
   * are: o getEJBHome - allowed o getCallerPrincipal - allowed o
   * getRollbackOnly - allowed o isCallerInRole - allowed o getEJBObject -
   * allowed o JNDI_Access - allowed o UserTransaction_Access- not allowed o
   * UserTransaction_Methods_Test1 - not allowed o UserTransaction_Methods_Test2
   * - not allowed o UserTransaction_Methods_Test3 - not allowed o
   * UserTransaction_Methods_Test4 - not allowed o UserTransaction_Methods_Test5
   * - not allowed o UserTransaction_Methods_Test6 - not allowed o
   * getEJBLocalHome - allowed o getEJBLocalObject - allowed o Timer Methods -
   * allowed
   *
   * Deploy it on the J2EE server. Verify correct operations.
   *
   */

  public void sfcmAllowedMethodsTest4() throws Fault {
    TestUtil.logTrace("Operation Tests for afterBegin");
    boolean pass = true;
    String expected[] = { "true", "true", "true", "true", "true", "true",
        "false", "false", "false", "false", "false", "false", "false", "true",
        "true", "true" };
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 1);
      logMsg("Calling business method");
      try {
        beanRef.businessMethod();
      } catch (RemoteException e) {
        TestUtil.printStackTrace(e);
      }
      logMsg("Getting results");
      results = beanRef.getResults();
      pass = checkResults(results, "afterBegin", expected);
    } catch (Exception e) {
      throw new Fault("sfcmAllowedMethodsTest4 failed", e);
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
      throw new Fault("sfcmAllowedMethodsTest4 failed");
  }

  /*
   * @testName: sfcmAllowedMethodsTest5
   * 
   * @assertion_ids: EJB:SPEC:86; EJB:SPEC:86.1; EJB:SPEC:86.2; EJB:SPEC:86.3;
   * EJB:SPEC:86.4; EJB:SPEC:86.6; EJB:SPEC:86.7; EJB:SPEC:86.10;
   * EJB:SPEC:86.11; EJB:SPEC:86.12; EJB:SPEC:86.13; EJB:SPEC:86.14;
   * EJB:SPEC:86.15; EJB:JAVADOC:210; EJB:JAVADOC:206; EJB:JAVADOC:202;
   * EJB:JAVADOC:198; EJB:JAVADOC:194
   * 
   * @test_Strategy: Operations allowed and not allowed in the beforeCompletion
   * method of a stateful session bean with container-managed transaction
   * demarcation are: o getEJBHome - allowed o getCallerPrincipal - allowed o
   * getRollbackOnly - allowed o isCallerInRole - allowed o getEJBObject -
   * allowed o JNDI_Access - allowed o UserTransaction_Access- not allowed o
   * UserTransaction_Methods_Test1 - not allowed o UserTransaction_Methods_Test2
   * - not allowed o UserTransaction_Methods_Test3 - not allowed o
   * UserTransaction_Methods_Test4 - not allowed o UserTransaction_Methods_Test5
   * - not allowed o UserTransaction_Methods_Test6 - not allowed o
   * getEJBLocalHome - allowed o getEJBLocalObject - allowed o Timer Methods -
   * allowed
   *
   * Deploy it on the J2EE server. Verify correct operations.
   *
   */

  public void sfcmAllowedMethodsTest5() throws Fault {
    TestUtil.logTrace("Operation Tests for beforeCompletion");
    boolean pass = true;
    String expected[] = { "true", "true", "true", "true", "true", "true",
        "false", "false", "false", "false", "false", "false", "false", "true",
        "true", "true" };
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 2);
      try {
        beanRef.businessMethod();
      } catch (RemoteException e) {
        TestUtil.printStackTrace(e);
      }
      results = beanRef.getResults();
      pass = checkResults(results, "beforeCompletion", expected);
    } catch (Exception e) {
      throw new Fault("sfcmAllowedMethodsTest5 failed", e);
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
      throw new Fault("sfcmAllowedMethodsTest5 failed");
  }

  /*
   * @testName: sfcmAllowedMethodsTest6
   * 
   * @assertion_ids: EJB:SPEC:87; EJB:SPEC:87.1; EJB:SPEC:87.2; EJB:SPEC:87.3;
   * EJB:SPEC:87.4; EJB:SPEC:87.6; EJB:SPEC:87.7; EJB:SPEC:87.10;
   * EJB:SPEC:87.11; EJB:SPEC:87.12; EJB:SPEC:87.13; EJB:SPEC:87.14;
   * EJB:SPEC:87.15; EJB:JAVADOC:195
   * 
   * @test_Strategy: Operations allowed and not allowed in the afterCompletion
   * method of a stateful session bean with container-managed transaction
   * demarcation are: o getEJBHome - allowed o getCallerPrincipal - allowed o
   * getRollbackOnly - not allowed o isCallerInRole - allowed o getEJBObject -
   * allowed o JNDI_Access - allowed o UserTransaction_Access- not allowed o
   * UserTransaction_Methods_Test1 - not allowed o UserTransaction_Methods_Test2
   * - not allowed o UserTransaction_Methods_Test3 - not allowed o
   * UserTransaction_Methods_Test4 - not allowed o UserTransaction_Methods_Test5
   * - not allowed o UserTransaction_Methods_Test6 - not allowed o
   * getEJBLocalHome - allowed o getEJBLocalObject - allowed o Timer Methods -
   * not allowed
   *
   * Deploy it on the J2EE server. Verify correct operations.
   *
   */

  public void sfcmAllowedMethodsTest6() throws Fault {
    TestUtil.logTrace("Operation Tests for afterCompletion");
    boolean pass = true;
    String expected[] = { "true", "true", "false", "true", "true", "true",
        "false", "false", "false", "false", "false", "false", "false", "true",
        "true", "false" };
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 3);
      try {
        beanRef.businessMethod();
      } catch (RemoteException e) {
        TestUtil.printStackTrace(e);
      }
      results = beanRef.getResults();
      pass = checkResults(results, "afterCompletion", expected);
    } catch (Exception e) {
      throw new Fault("sfcmAllowedMethodsTest6 failed", e);
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
      throw new Fault("sfcmAllowedMethodsTest6 failed");
  }

  /*
   * @testName: sfcmAllowedMethodsTest7
   * 
   * @assertion_ids: EJB:SPEC:90; EJB:SPEC:91; EJB:JAVADOC:26; EJB:JAVADOC:35
   * 
   * @test_Strategy: The getRollbackOnly and setRollBackOnly methods of the
   * SessionContext interface should be used only in the session bean methods
   * that execute in the context of a transaction. The container must throw the
   * java.lang.IllegalStateException if the methods are invoked while the
   * instance is not associated with a transaction. The following operations are
   * executed in a business method with transaction attribute NotSupported. o
   * getRollbackOnly - not allowed o setRollbackOnly - not allowed
   *
   *
   */

  public void sfcmAllowedMethodsTest7() throws Fault {
    TestUtil.logTrace("Operation Tests for business method of NotSupported");
    boolean pass = true;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanNoTxRef = (TestBeanNoTx) beanNoTxHome.create(props);
      beanNoTxRef.txNotSupported();
      results = beanNoTxRef.getResults();
      pass = checkResults(results, "false");
    } catch (Exception e) {
      throw new Fault("sfcmAllowedMethodsTest7 failed", e);
    }

    try {
      if (beanNoTxRef != null) {
        beanNoTxRef.remove();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception caught removing bean", e);
    }

    if (!pass)
      throw new Fault("sfcmAllowedMethodsTest7 failed");
  }

  /*
   * @testName: sfcmAllowedMethodsTest8
   * 
   * @assertion_ids: EJB:SPEC:90; EJB:SPEC:91; EJB:JAVADOC:26; EJB:JAVADOC:35
   * 
   * @test_Strategy: The getRollbackOnly and setRollBackOnly methods of the
   * SessionContext interface should be used only in the session bean methods
   * that execute in the context of a transaction. The container must throw the
   * java.lang.IllegalStateException if the methods are invoked while the
   * instance is not associated with a transaction. The following operations are
   * executed in a business method with transaction attribute Supports. o
   * getRollbackOnly - not allowed o setRollbackOnly - not allowed
   *
   */

  public void sfcmAllowedMethodsTest8() throws Fault {
    TestUtil.logTrace("Operation Tests for business method of Supports");
    boolean pass = true;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanNoTxRef = (TestBeanNoTx) beanNoTxHome.create(props);
      beanNoTxRef.txSupports();
      results = beanNoTxRef.getResults();
      pass = checkResults(results, "false");
    } catch (Exception e) {
      throw new Fault("sfcmAllowedMethodsTest8 failed", e);
    }

    try {
      if (beanNoTxRef != null) {
        beanNoTxRef.remove();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception caught removing bean", e);
    }

    if (!pass)
      throw new Fault("sfcmAllowedMethodsTest8 failed");
  }

  /*
   * @testName: sfcmAllowedMethodsTest9
   * 
   * @assertion_ids: EJB:SPEC:90; EJB:SPEC:91; EJB:JAVADOC:26; EJB:JAVADOC:35
   * 
   * @test_Strategy: The getRollbackOnly and setRollBackOnly methods of the
   * SessionContext interface should be used only in the session bean methods
   * that execute in the context of a transaction. The container must throw the
   * java.lang.IllegalStateException if the methods are invoked while the
   * instance is not associated with a transaction. The following operations are
   * executed in a business method with transaction attribute Never. o
   * getRollbackOnly - not allowed o setRollbackOnly - not allowed
   *
   */

  public void sfcmAllowedMethodsTest9() throws Fault {
    TestUtil.logTrace("Operation Tests for business method of Never");
    boolean pass = true;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanNoTxRef = (TestBeanNoTx) beanNoTxHome.create(props);
      beanNoTxRef.txNever();
      results = beanNoTxRef.getResults();
      pass = checkResults(results, "false");
    } catch (Exception e) {
      throw new Fault("sfcmAllowedMethodsTest9 failed", e);
    }

    try {
      if (beanNoTxRef != null) {
        beanNoTxRef.remove();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception caught removing bean", e);
    }

    if (!pass)
      throw new Fault("sfcmAllowedMethodsTest9 failed");
  }

  /*
   * @testName: sfcmAllowedMethodsTest10
   * 
   * @assertion_ids: EJB:SPEC:81.5; EJB:JAVADOC:35
   *
   * @test_Strategy: Operations allowed and not allowed in the ejbCreate method
   * of a stateful session bean with container-managed transaction demarcation
   * are:
   *
   * o setRollbackOnly - not allowed
   *
   */

  public void sfcmAllowedMethodsTest10() throws Fault {
    TestUtil.logTrace("Operation Tests for setRollbackOnly in ejbCreate");
    boolean pass = true;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 5);
      results = beanRef.getResults1();
      pass = checkResults1(results, "false", "ejbCreate");
    } catch (Exception e) {
      throw new Fault("sfcmAllowedMethodsTest10 failed", e);
    }

    try {
      if (beanRef != null) {
        beanRef.remove();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception caught removing bean", e);
    }

    if (!pass)
      throw new Fault("sfcmAllowedMethodsTest10 failed");
  }

  /*
   * @testName: sfcmAllowedMethodsTest11
   * 
   * @assertion_ids: EJB:SPEC:82.5; EJB:JAVADOC:35
   *
   * @test_Strategy: Operations allowed and not allowed in the setSessionContext
   * method of a stateful session bean with container-managed transaction
   * demarcation are:
   *
   * o setRollbackOnly - not allowed
   *
   */

  public void sfcmAllowedMethodsTest11() throws Fault {
    TestUtil
        .logTrace("Operation Tests for setRollbackOnly in setSessionContext");
    boolean pass = true;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 0);
      results = beanRef.getResults1();
      pass = checkResults1(results, "false", "setSessionContext");
    } catch (Exception e) {
      throw new Fault("sfcmAllowedMethodsTest11 failed", e);
    }

    try {
      if (beanRef != null) {
        beanRef.remove();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception caught removing bean", e);
    }

    if (!pass)
      throw new Fault("sfcmAllowedMethodsTest11 failed");
  }

  /*
   * @testName: sfcmAllowedMethodsTest12
   * 
   * @assertion_ids: EJB:SPEC:83.5; EJB:JAVADOC:35
   *
   * @test_Strategy: Operations allowed and not allowed in a business method of
   * a stateful session bean with container-managed transaction demarcation are:
   *
   * o setRollbackOnly - allowed
   *
   */

  public void sfcmAllowedMethodsTest12() throws Fault {
    TestUtil
        .logTrace("Operation Tests for setRollbackOnly in a business method");
    boolean pass = true;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 11);
      beanRef.businessMethod();
      results = beanRef.getResults1();
      pass = checkResults1(results, "true", "businessMethod");
    } catch (Exception e) {
      throw new Fault("sfcmAllowedMethodsTest12 failed", e);
    }

    try {
      if (beanRef != null) {
        beanRef.remove();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception caught removing bean", e);
    }

    if (!pass)
      throw new Fault("sfcmAllowedMethodsTest12 failed");
  }

  /*
   * @testName: sfcmAllowedMethodsTest13
   * 
   * @assertion_ids: EJB:SPEC:85.5; EJB:JAVADOC:34
   *
   * @test_Strategy: Operations allowed and not allowed in the afterBegin method
   * of a stateful session bean with container-managed transaction demarcation
   * are:
   *
   * o setRollbackOnly - allowed
   *
   */

  public void sfcmAllowedMethodsTest13() throws Fault {
    TestUtil.logTrace("Operation Tests for setRollbackOnly in afterBegin");
    boolean pass = true;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 8);
      beanRef.businessMethod();
      results = beanRef.getResults1();
      pass = checkResults1(results, "true", "afterBegin");
    } catch (Exception e) {
      throw new Fault("sfcmAllowedMethodsTest13 failed", e);
    }

    try {
      if (beanRef != null) {
        beanRef.remove();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception caught removing bean", e);
    }

    if (!pass)
      throw new Fault("sfcmAllowedMethodsTest13 failed");
  }

  /*
   * @testName: sfcmAllowedMethodsTest14
   * 
   * @assertion_ids: EJB:SPEC:86.5; EJB:JAVADOC:34
   *
   * @test_Strategy: Operations allowed and not allowed in the beforeCompletion
   * method of a stateful session bean with container-managed transaction
   * demarcation are:
   *
   * o setRollbackOnly - allowed
   *
   */

  public void sfcmAllowedMethodsTest14() throws Fault {
    TestUtil
        .logTrace("Operation Tests for setRollbackOnly in beforeCompletion");
    boolean pass = true;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 9);
      beanRef.businessMethod();
      results = beanRef.getResults1();
      pass = checkResults1(results, "true", "beforeCompletion");
    } catch (RemoteException re) {
      // Because the setRollbackOnly is allowed, the TX is being rolled back and
      // throws a remote exception
      TestUtil.logTrace("Caught java.rmi.RemoteException  - expected");
      try {
        results = beanRef.getResults1();
        pass = checkResults1(results, "true", "beforeCompletion");
      } catch (RemoteException re1) {
        TestUtil.logErr("Caught java.rmi.RemoteException  - Unexpected", re1);
      }
    } catch (Exception e) {
      throw new Fault("sfcmAllowedMethodsTest14 failed", e);
    }

    try {
      if (beanRef != null) {
        beanRef.remove();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception caught removing bean", e);
    }

    if (!pass)
      throw new Fault("sfcmAllowedMethodsTest14 failed");
  }

  /*
   * @testName: sfcmAllowedMethodsTest15
   * 
   * @assertion_ids: EJB:SPEC:87.5; EJB:JAVADOC:35
   *
   * @test_Strategy: Operations allowed and not allowed in the afterCompletion
   * method of a stateful session bean with container-managed transaction
   * demarcation are:
   *
   * o setRollbackOnly - not allowed
   *
   */

  public void sfcmAllowedMethodsTest15() throws Fault {
    TestUtil.logTrace("Operation Tests for setRollbackOnly in afterCompletion");
    boolean pass = true;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 10);
      beanRef.businessMethod();
      results = beanRef.getResults1();
      pass = checkResults1(results, "false", "afterCompletion");
    } catch (Exception e) {
      throw new Fault("sfcmAllowedMethodsTest15 failed", e);
    }

    try {
      if (beanRef != null) {
        beanRef.remove();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception caught removing bean", e);
    }

    if (!pass)
      throw new Fault("sfcmAllowedMethodsTest15 failed");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
