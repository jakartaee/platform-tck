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
 * @(#)Client.java	1.35 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.session.stateless.cm.allowedmethodstest;

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
    if (results == null) {
      logMsg("ERROR: Results object not found for method (" + method
          + ") ... Skipping");
      SKIP = true;
      return false;
    }
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
    if (!p.getProperty("isCallerInRole").equals(r[2])) {
      logErr("isCallerInRole operations test failed");
      pass = false;
    }
    if (!p.getProperty("getEJBObject").equals(r[3])) {
      logErr("getEJBObject operations test failed");
      pass = false;
    }
    if (!p.getProperty("JNDI_Access").equals(r[4])) {
      logErr("JNDI_Access operations test failed");
      pass = false;
    }

    if (!p.getProperty("UserTransaction").equals(r[5])) {
      logErr("UserTransaction operations test failed");
      pass = false;
    }
    if (!p.getProperty("UserTransaction_Methods_Test1").equals(r[6])) {
      logErr("UserTransaction_Methods_Test1 operations test failed");
      pass = false;
    }
    if (!p.getProperty("UserTransaction_Methods_Test2").equals(r[7])) {
      logErr("UserTransaction_Methods_Test2 operations test failed");
      pass = false;
    }
    if (!p.getProperty("UserTransaction_Methods_Test3").equals(r[8])) {
      logErr("UserTransaction_Methods_Test3 operations test failed");
      pass = false;
    }
    if (!p.getProperty("UserTransaction_Methods_Test4").equals(r[9])) {
      logErr("UserTransaction_Methods_Test4 operations test failed");
      pass = false;
    }
    if (!p.getProperty("UserTransaction_Methods_Test5").equals(r[10])) {
      logErr("UserTransaction_Methods_Test5 operations test failed");
      pass = false;
    }
    if (!p.getProperty("UserTransaction_Methods_Test6").equals(r[11])) {
      logErr("UserTransaction_Methods_Test6 operations test failed");
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
    if (!p.getProperty("getTimerService").equals(r[14])) {
      logErr("getTimerService operations test failed");
      pass = false;
    }
    if (!p.getProperty("TimerService_Methods_Test1").equals(r[15])) {
      logErr("TimerService_Methods_Test1 operations test failed");
      pass = false;
    }

    if (!p.getProperty("TimerService_Methods_Test2").equals(r[16])) {
      logErr("TimerService_Methods_Test2 operations test failed");
      pass = false;
    }

    if (!p.getProperty("TimerService_Methods_Test3").equals(r[17])) {
      logErr("TimerService_Methods_Test3 operations test failed");
      pass = false;
    }

    if (!p.getProperty("TimerService_Methods_Test4").equals(r[18])) {
      logErr("TimerService_Methods_Test4 operations test failed");
      pass = false;
    }

    if (!p.getProperty("TimerService_Methods_Test5").equals(r[19])) {
      logErr("TimerService_Methods_Test5 operations test failed");
      pass = false;
    }

    if (!p.getProperty("TimerService_Methods_Test6").equals(r[20])) {
      logErr("TimerService_Methods_Test6 operations test failed");
      pass = false;
    }

    if (!p.getProperty("TimerService_Methods_Test7").equals(r[21])) {
      logErr("TimerService_Methods_Test7 operations test failed");
      pass = false;
    }

    if (!p.getProperty("getMessageContext").equals(r[22])) {
      logErr("getMessageContext operations test failed");
      pass = false;
    }

    if (!p.getProperty("getRollbackOnly").equals(r[23])) {
      logErr("getRollbackOnly operations test failed");
      pass = false;
    }

    if (!p.getProperty("setRollbackOnly").equals(r[24])) {
      logErr("setRollbackOnly operations test failed");
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
   * @testName: slcmAllowedMethodsTest1
   * 
   * @assertion_ids: EJB:SPEC:108; EJB:SPEC:108.1; EJB:SPEC:108.2;
   * EJB:SPEC:108.3; EJB:SPEC:108.4; EJB:SPEC:108.5; EJB:SPEC:108.6;
   * EJB:SPEC:108.7; EJB:SPEC:108.10; EJB:SPEC:108.11; EJB:SPEC:108.12;
   * EJB:SPEC:108.13; EJB:SPEC:108.14; EJB:SPEC:108.15; EJB:SPEC:106;
   * EJB:SPEC:107; EJB:JAVADOC:215; EJB:JAVADOC:220; EJB:JAVADOC:224;
   * EJB:JAVADOC:227; EJB:JAVADOC:231; EJB:JAVADOC:191; EJB:JAVADOC:195;
   * EJB:JAVADOC:173
   *
   * @test_Strategy: Operations allowed and not allowed in the ejbCreate method
   * of a stateless session bean with container-managed transaction demarcation
   * are: o getEJBHome - allowed o getCallerPrincipal - not allowed o
   * isCallerInRole - not allowed o getEJBObject - allowed o JNDI_Access -
   * allowed o UserTransaction_Access - not allowed o
   * UserTransaction_Methods_Test1 - not allowed o UserTransaction_Methods_Test2
   * - not allowed o UserTransaction_Methods_Test3 - not allowed o
   * UserTransaction_Methods_Test4 - not allowed o UserTransaction_Methods_Test5
   * - not allowed o UserTransaction_Methods_Test6 - not allowed o
   * getEJBLocalHome - allowed o getEJBLocalObject - allowed o getTimerService -
   * allowed o TimerService_Methods_Test1 - not allowed o
   * TimerService_Methods_Test2 - not allowed o TimerService_Methods_Test3 - not
   * allowed o TimerService_Methods_Test4 - not allowed o
   * TimerService_Methods_Test5 - not allowed o TimerService_Methods_Test6 - not
   * allowed o TimerService_Methods_Test7 - not allowed o getMessageContext -
   * not allowed o getRollbackOnly - not allowed o setRollbackOnly - not allowed
   *
   * Deploy it on the J2EE server. Verify correct operations.
   *
   */

  public void slcmAllowedMethodsTest1() throws Fault {
    logTrace("Operation Tests for ejbCreate");
    boolean pass = true;
    String expected[] = { "true", "false", "false", "true", "true", "false",
        "false", "false", "false", "false", "false", "false", "true", "true",
        "true", "false", "false", "false", "false", "false", "false", "false",
        "false", "false", "false" };
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create();
      logMsg("Initialize remote logging");
      beanRef.initLogging(props);
      logMsg("get results");
      results = beanRef.getResults();
      logTrace("results for ejbCreate are:" + beanRef.getResults());
      logMsg("check results");
      pass = checkResults(results, "ejbCreate", expected);
      logMsg("check status");
    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault("slcmAllowedMethodsTest1 failed", e);
    }
    try {
      beanRef.remove();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
    }

    if (!pass)
      throw new Fault("slcmAllowedMethodsTest1 failed");
  }

  /*
   * @testName: slcmAllowedMethodsTest2
   * 
   * @assertion_ids: EJB:SPEC:109; EJB:SPEC:109.1; EJB:SPEC:109.2;
   * EJB:SPEC:109.3; EJB:SPEC:109.4; EJB:SPEC:109.5; EJB:SPEC:109.6;
   * EJB:SPEC:109.7; EJB:SPEC:109.10; EJB:SPEC:109.11; EJB:SPEC:109.12;
   * EJB:SPEC:109.13; EJB:SPEC:109.14; EJB:SPEC: 109.15; EJB:JAVADOC:173;
   * EJB:JAVADOC:161; EJB:JAVADOC:215; EJB:JAVADOC:220; EJB:JAVADOC:224;
   * EJB:JAVADOC:227; EJB:JAVADOC:231; EJB:JAVADOC:191; EJB:JAVADOC:195
   * 
   * @test_Strategy: Operations allowed and not allowed in the setSessionContext
   * method of a stateless session bean with container-managed transaction
   * demarcation are: o getEJBHome - allowed o getCallerPrincipal - not allowed
   * o isCallerInRole - not allowed o getEJBObject - not allowed o JNDI_Access -
   * allowed o UserTransaction_Access - not allowed o
   * UserTransaction_Methods_Test1 - not allowed o UserTransaction_Methods_Test2
   * - not allowed o UserTransaction_Methods_Test3 - not allowed o
   * UserTransaction_Methods_Test4 - not allowed o UserTransaction_Methods_Test5
   * - not allowed o UserTransaction_Methods_Test6 - not allowed o
   * getEJBLocalHome - allowed o getEJBLocalObject - not allowed o
   * getTimerService - not allowed o TimerService_Methods_Test1 - not allowed o
   * TimerService_Methods_Test2 - not allowed o TimerService_Methods_Test3 - not
   * allowed o TimerService_Methods_Test4 - not allowed o
   * TimerService_Methods_Test5 - not allowed o TimerService_Methods_Test6 - not
   * allowed o TimerService_Methods_Test7 - not allowed o getMessageContext -
   * not allowed o getRollbackOnly - not allowed o setRollbackOnly - not allowed
   *
   * Create a stateless Session Bean. Deploy it on the J2EE server. Verify
   * correct operations.
   *
   */

  public void slcmAllowedMethodsTest2() throws Fault {
    TestUtil.logTrace("Operation Tests for setSessionContext");
    boolean pass = true;
    String expected[] = { "true", "false", "false", "false", "true", "false",
        "false", "false", "false", "false", "false", "false", "true", "false",
        "false", "false", "false", "false", "false", "false", "false", "false",
        "false", "false", "false" };
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create();
      logMsg("Initialize remote logging");
      beanRef.initLogging(props);
      results = beanRef.getResults();
      pass = checkResults(results, "setSessionContext", expected);
    } catch (Exception e) {
      throw new Fault("slcmAllowedMethodsTest2 failed", e);
    }

    try {
      beanRef.remove();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
    }
    if (!pass)
      throw new Fault("slcmAllowedMethodsTest2 failed");
  }

  /*
   * @testName: slcmAllowedMethodsTest3
   * 
   * @assertion_ids: EJB:SPEC:110; EJB:SPEC:110.1; EJB:SPEC:110.2;
   * EJB:SPEC:110.3; EJB:SPEC:110.4; EJB:SPEC:110.5; EJB:SPEC:110.6;
   * EJB:SPEC:110.7; EJB:SPEC:110.10; EJB:SPEC:110.11; EJB:SPEC:110.12;
   * EJB:SPEC:110.13; EJB:SPEC:110.14; EJB:SPEC:110.15; EJB:JAVADOC:173
   * 
   * @test_Strategy: Operations allowed and not allowed in the business method
   * method from a component interface of a stateless session bean with
   * container- managed transaction demarcation are: o getEJBHome - allowed o
   * getCallerPrincipal - allowed o isCallerInRole - allowed o getEJBObject -
   * allowed o JNDI_Access - allowed o UserTransaction_Access - not allowed o
   * UserTransaction_Methods_Test1 - not allowed o UserTransaction_Methods_Test2
   * - not allowed o UserTransaction_Methods_Test3 - not allowed o
   * UserTransaction_Methods_Test4 - not allowed o UserTransaction_Methods_Test5
   * - not allowed o UserTransaction_Methods_Test6 - not allowed o
   * getEJBLocalHome - allowed o getEJBLocalObject - allowed o getTimerService -
   * allowed o TimerService_Methods_Test1 - allowed o TimerService_Methods_Test2
   * - allowed o TimerService_Methods_Test3 - allowed o
   * TimerService_Methods_Test4 - allowed o TimerService_Methods_Test5 - allowed
   * o TimerService_Methods_Test6 - allowed o TimerService_Methods_Test7 -
   * allowed o getMessageContext - not allowed o getRollbackOnly - allowed o
   * setRollbackOnly - allowed
   *
   * Create a stateless Session Bean. Deploy it on the J2EE server. Verify
   * correct operations.
   *
   */

  public void slcmAllowedMethodsTest3() throws Fault {
    TestUtil.logTrace("Operation Tests for businessMethod");
    boolean pass = true;
    String expected[] = { "true", "true", "true", "true", "true", "false",
        "false", "false", "false", "false", "false", "false", "true", "true",
        "true", "true", "true", "true", "true", "true", "true", "true", "false",
        "true", "true" };
    try {
      // create Helper EJB instance
      logMsg("Create Helper EJB instance");
      helperRef = (Helper) helperHome.create();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create();
      logMsg("Initialize remote logging");
      beanRef.initLogging(props);
      logMsg("Calling EJB business method");
      beanRef.businessMethod(helperRef);
      results = helperRef.getData();
      TestUtil.logTrace("results for business methods are:" + results);
      pass = checkResults(results, "businessMethod", expected);
      helperRef.remove();
    } catch (Exception e) {
      throw new Fault("slcmAllowedMethodsTest3 failed", e);
    }

    try {
      beanRef.remove();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
    }

    if (!pass)
      throw new Fault("slcmAllowedMethodsTest3 failed");
  }

  /*
   * @testName: slcmAllowedMethodsTest4
   * 
   * @assertion_ids: EJB:SPEC:118; EJB:SPEC:119; EJB:JAVADOC:26; EJB:JAVADOC:35
   * 
   * @test_Strategy: Create a stateless Session Bean, call a business method
   * with the NotSupported transaction attribute and verify that the
   * java.lang.IllegalStateException is thrown as getRollbackOnly() or
   * setRollbackOnly() must be executed in the ` context of a transaction.
   *
   */

  public void slcmAllowedMethodsTest4() throws Fault {
    TestUtil.logTrace(
        "Operation Tests for business method with NotSupported TX attribute");
    boolean pass = true;
    try {
      // create Helper EJB instance
      logMsg("Create Helper EJB instance");
      helperRef = (Helper) helperHome.create();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create();
      logMsg("Initialize remote logging");
      beanRef.initLogging(props);
      beanRef.txNotSupported(helperRef);
      results = helperRef.getData();
      pass = checkResults(results, "false");
      helperRef.remove();
    } catch (Exception e) {
      throw new Fault("slcmAllowedMethodsTest4 failed", e);
    }
    try {
      beanRef.remove();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
    }
    if (!pass)
      throw new Fault("slcmAllowedMethodsTest4 failed");
  }

  /*
   * @testName: slcmAllowedMethodsTest5
   * 
   * @assertion_ids: EJB:SPEC:118; EJB:SPEC:119
   * 
   * @test_Strategy: Create a stateless Session Bean, call a business method
   * with the Supports transaction attribute and verify that the
   * java.lang.IllegalStateException is thrown as getRollbackOnly() or
   * setRollbackOnly() must be executed in the context of a transaction.
   *
   */

  public void slcmAllowedMethodsTest5() throws Fault {
    TestUtil.logTrace(
        "Operation Tests for business method with Supports TX Attribute");
    boolean pass = true;
    try {
      // create Helper EJB instance
      logMsg("Create Helper EJB instance");
      helperRef = (Helper) helperHome.create();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create();
      logMsg("Initialize remote logging");
      beanRef.initLogging(props);
      beanRef.txSupports(helperRef);
      results = helperRef.getData();
      pass = checkResults(results, "false");
      helperRef.remove();
    } catch (Exception e) {
      throw new Fault("slcmAllowedMethodsTest5 failed", e);
    }

    try {
      beanRef.remove();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
    }

    if (!pass)
      throw new Fault("slcmAllowedMethodsTest5 failed");
  }

  /*
   * @testName: slcmAllowedMethodsTest6
   * 
   * @assertion_ids: EJB:SPEC:118; EJB:SPEC:119; EJB:JAVADOC:26; EJB:JAVADOC:35
   * 
   * @test_Strategy: Create a stateless Session Bean, call a business method
   * with the Never transaction attribute and verify that the
   * java.lang.IllegalStateException is thrown as getRollbackOnly() or
   * setRollbackOnly() must be executed in the context of a transaction.
   * 
   *
   */

  public void slcmAllowedMethodsTest6() throws Fault {
    TestUtil.logTrace(
        "Operation Tests for business method with Never TX Attribute");
    boolean pass = true;
    try {
      // create Helper EJB instance
      logMsg("Create Helper EJB instance");
      helperRef = (Helper) helperHome.create();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create();
      logMsg("Initialize remote logging");
      beanRef.initLogging(props);
      beanRef.txNever(helperRef);
      results = helperRef.getData();
      pass = checkResults(results, "false");
      helperRef.remove();
    } catch (Exception e) {
      throw new Fault("slcmAllowedMethodsTest6 failed", e);
    }

    try {
      beanRef.remove();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
    }

    if (!pass)
      throw new Fault("slcmAllowedMethodsTest6 failed");
  }

  /*
   * @testName: slcmAllowedMethodsTest7
   * 
   * @assertion_ids: EJB:SPEC:61.6; EJB:SPEC:823; EJB:JAVADOC:21
   * 
   * @test_Strategy: Verify Principal reference is returned for SSL bean
   * extending TimedObject with security-identity as use-caller-identity - never
   * null.
   *
   */

  public void slcmAllowedMethodsTest7() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create();
      beanRef.initLogging(props);
      pass = beanRef.getCallerPrincipalTest(user_value);
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("slcmAllowedMethodsTest7 failed", e);
    }
    if (!pass)
      throw new Fault("slcmAllowedMethodsTest7 failed");
  }

  public void cleanup() throws Fault {
    try {
      beanRef = (TestBean) beanHome.create();
      beanRef.findAndCancelTimer();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught removing timers:" + e, e);
    }
    logMsg("cleanup ok");
  }
}
