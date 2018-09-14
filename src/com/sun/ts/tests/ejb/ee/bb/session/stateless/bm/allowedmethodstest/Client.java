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
 * @(#)Client.java	1.38 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.session.stateless.bm.allowedmethodstest;

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
  /**
   * Special value used in the expected result to indicate that this check is to
   * be skipped.
   */
  private static final String UNSPECIFIED = "unspecified";

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
    try {
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
      if (!UNSPECIFIED.equals(r[6])
          && !p.getProperty("UserTransaction_Methods_Test1").equals(r[6])) {
        logErr("UserTransaction_Methods_Test1 operations test failed");
        pass = false;
      }
      if (!UNSPECIFIED.equals(r[7])
          && !p.getProperty("UserTransaction_Methods_Test2").equals(r[7])) {
        logErr("UserTransaction_Methods_Test2 operations test failed");
        pass = false;
      }
      if (!UNSPECIFIED.equals(r[8])
          && !p.getProperty("UserTransaction_Methods_Test3").equals(r[8])) {
        logErr("UserTransaction_Methods_Test3 operations test failed");
        pass = false;
      }
      if (!UNSPECIFIED.equals(r[9])
          && !p.getProperty("UserTransaction_Methods_Test4").equals(r[9])) {
        logErr("UserTransaction_Methods_Test4 operations test failed");
        pass = false;
      }
      if (!p.getProperty("getEJBLocalHome").equals(r[10])) {
        logErr("getEJBLocalHome operations test failed");
        pass = false;
      }
      if (!p.getProperty("getEJBLocalObject").equals(r[11])) {
        logErr("getEJBLocalObject operations test failed");
        pass = false;
      }
      if (!p.getProperty("getTimerService").equals(r[12])) {
        logErr("getTimerService operations test failed");
        pass = false;
      }
      if (!p.getProperty("TimerService_Methods_Test1").equals(r[13])) {
        logErr("TimerService_Methods_Test1 operations test failed");
        pass = false;
      }
      if (!p.getProperty("TimerService_Methods_Test2").equals(r[14])) {
        logErr("TimerService_Methods_Test2 operations test failed");
        pass = false;
      }
      if (!p.getProperty("TimerService_Methods_Test3").equals(r[15])) {
        logErr("TimerService_Methods_Test3 operations test failed");
        pass = false;
      }
      if (!p.getProperty("TimerService_Methods_Test4").equals(r[16])) {
        logErr("TimerService_Methods_Test4 operations test failed");
        pass = false;
      }
      if (!p.getProperty("TimerService_Methods_Test5").equals(r[17])) {
        logErr("TimerService_Methods_Test5 operations test failed");
        pass = false;
      }
      if (!p.getProperty("TimerService_Methods_Test6").equals(r[18])) {
        logErr("TimerService_Methods_Test6 operations test failed");
        pass = false;
      }
      if (!p.getProperty("TimerService_Methods_Test7").equals(r[19])) {
        logErr("TimerService_Methods_Test7 operations test failed");
        pass = false;
      }
      if (!p.getProperty("getMessageContext").equals(r[20])) {
        logErr("getMessageContext operations test failed");
        pass = false;
      }
      if (!p.getProperty("getRollbackOnly").equals(r[21])) {
        logErr("getRollbackOnly operations test failed");
        pass = false;
      }
      if (!p.getProperty("setRollbackOnly").equals(r[22])) {
        logErr("setRollbackOnly operations test failed");
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
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
   * @testName: slbmAllowedMethodsTest1
   * 
   * @assertion_ids: EJB:SPEC:113; EJB:SPEC:113.1; EJB:SPEC:113.2;
   * EJB:SPEC:113.3; EJB:SPEC:113.4; EJB:SPEC:113.5; EJB:SPEC:113.6;
   * EJB:SPEC:113.7; EJB:SPEC:113.10; EJB:SPEC:113.11; EJB:SPEC:113.12;
   * EJB:JAVADOC:29; EJB:SPEC:113.13; EJB:SPEC:113.14; EJB:SPEC:113.15;
   * EJB:SPEC:106; EJB:SPEC:107; EJB:JAVADOC:215; EJB:JAVADOC:220;
   * EJB:JAVADOC:224; EJB:JAVADOC:227; EJB:JAVADOC:231; EJB:JAVADOC:173;
   * EJB:JAVADOC:191; EJB:JAVADOC:195
   * 
   * @test_Strategy: Operations allowed and not allowed in the ejbCreate method
   * of a stateless session bean with bean-managed transaction demarcation are:
   * o getEJBHome - allowed o getCallerPrincipal - not allowed o isCallerInRole
   * - not allowed o getEJBObject - allowed o JNDI_Access - allowed o
   * UserTransaction_Access - allowed o UserTransaction_Methods_Test1 -
   * UNSPECIFIED o UserTransaction_Methods_Test2 - UNSPECIFIED o
   * UserTransaction_Methods_Test3 - UNSPECIFIED o UserTransaction_Methods_Test4
   * - UNSPECIFIED o getEJBLocalHome - allowed o getEJBLocalObject - allowed o
   * getTimerService - allowed o TimerService_Methods_Test1 - not allowed o
   * TimerService_Methods_Test2 - not allowed o TimerService_Methods_Test3 - not
   * allowed o TimerService_Methods_Test4 - not allowed o
   * TimerService_Methods_Test5 - not allowed o TimerService_Methods_Test6 - not
   * allowed o TimerService_Methods_Test7 - not allowed o getMessageContext -
   * not allowed o getRollbackOnly - not allowed o setRollbackOnly - not allowed
   * 
   * Deploy it on the J2EE server. Verify correct operations.
   * 
   */

  public void slbmAllowedMethodsTest1() throws Fault {
    logTrace("Operation Tests for ejbCreate");
    boolean pass = true;
    String expected[] = { "true", "false", "false", "true", "true", "true",
        UNSPECIFIED, UNSPECIFIED, UNSPECIFIED, UNSPECIFIED, "true", "true",
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
      logMsg("check results");
      pass = checkResults(results, "ejbCreate", expected);
      logMsg("check status");
    } catch (Exception e) {
      e.printStackTrace();
      throw new Fault("slbmAllowedMethodsTest1 failed", e);
    }
    try {
      beanRef.remove();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
    }

    if (!pass)
      throw new Fault("slbmAllowedMethodsTest1 failed");
  }

  /*
   * @testName: slbmAllowedMethodsTest2
   * 
   * @assertion_ids: EJB:SPEC:114; EJB:SPEC:114.1; EJB:SPEC:114.2;
   * EJB:SPEC:114.3; EJB:SPEC:114.4; EJB:SPEC:114.5; EJB:SPEC:114.6;
   * EJB:SPEC:114.7; EJB:SPEC:114.10; EJB:SPEC:114.11; EJB:SPEC:114.12;
   * EJB:JAVADOC:73; EJB:SPEC:114.13; EJB:SPEC:114.14; EJB:SPEC:114.15;
   * EJB:JAVADOC:215; EJB:JAVADOC:220; EJB:JAVADOC:224; EJB:JAVADOC:227;
   * EJB:JAVADOC:231; EJB:JAVADOC:173; EJB:JAVADOC:191; EJB:JAVADOC:195
   * 
   * @test_Strategy: Operations allowed and not allowed in the setSessionContext
   * method of a stateless session bean with bean-managed transaction
   * demarcation are: o getEJBHome - allowed o getCallerPrincipal - not allowed
   * o isCallerInRole - not allowed o getEJBObject - not allowed o JNDI_Access -
   * allowed o UserTransaction_Access - not allowed o
   * UserTransaction_Methods_Test1 - not allowed o UserTransaction_Methods_Test2
   * - not allowed o UserTransaction_Methods_Test3 - not allowed o
   * UserTransaction_Methods_Test4 - not allowed o getEJBLocalHome - allowed o
   * getEJBLocalObject - not allowed o getTimerService - not allowed o
   * TimerService_Methods_Test1 - not allowed o TimerService_Methods_Test2 - not
   * allowed o TimerService_Methods_Test3 - not allowed o
   * TimerService_Methods_Test4 - not allowed o TimerService_Methods_Test5 - not
   * allowed o TimerService_Methods_Test6 - not allowed o
   * TimerService_Methods_Test7 - not allowed o getMessageContext - not allowed
   * o getRollbackOnly - not allowed o setRollbackOnly - not allowed
   * 
   * Create a stateless Session Bean. Deploy it on the J2EE server. Verify
   * correct operations.
   * 
   */

  public void slbmAllowedMethodsTest2() throws Fault {
    TestUtil.logTrace("Operation Tests for setSessionContext");
    boolean pass = true;
    String expected[] = { "true", "false", "false", "false", "true", "false",
        "false", "false", "false", "false", "true", "false", "false", "false",
        "false", "false", "false", "false", "false", "false", "false", "false",
        "false" };
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create();
      logMsg("Initialize remote logging");
      beanRef.initLogging(props);
      results = beanRef.getResults();
      pass = checkResults(results, "setSessionContext", expected);
    } catch (Exception e) {
      throw new Fault("slbmAllowedMethodsTest2 failed", e);
    }

    try {
      beanRef.remove();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
    }

    if (!pass)
      throw new Fault("slbmAllowedMethodsTest2 failed");
  }

  /*
   * @testName: slbmAllowedMethodsTest3
   * 
   * @assertion_ids: EJB:SPEC:115; EJB:SPEC:115.1; EJB:SPEC:115.2;
   * EJB:SPEC:115.3; EJB:SPEC:115.4; EJB:SPEC:115.5; EJB:SPEC:115.6;
   * EJB:SPEC:115.7; EJB:SPEC:115.10; EJB:SPEC:115.11; EJB:SPEC:115.12;
   * EJB:SPEC:115.13; EJB:SPEC:115.14; EJB:SPEC:115.15; EJB:JAVADOC:173
   * 
   * @test_Strategy: Operations allowed and not allowed in the business method
   * method from a component interface of a stateless session bean with bean-
   * managed transaction demarcation are: o getEJBHome - allowed o
   * getCallerPrincipal - allowed o isCallerInRole - allowed o getEJBObject -
   * allowed o JNDI_Access - allowed o UserTransaction_Access - allowed o
   * UserTransaction_Methods_Test1 - allowed o UserTransaction_Methods_Test2 -
   * allowed o UserTransaction_Methods_Test3 - allowed o
   * UserTransaction_Methods_Test4 - allowed o getEJBLocalHome - allowed o
   * getEJBLocalObject - allowed o getTimerService - allowed o
   * TimerService_Methods_Test1 - allowed o TimerService_Methods_Test2 - allowed
   * o TimerService_Methods_Test3 - allowed o TimerService_Methods_Test4 -
   * allowed o TimerService_Methods_Test5 - allowed o TimerService_Methods_Test6
   * - allowed o TimerService_Methods_Test7 - allowed o getMessageContext - not
   * allowed o getRollbackOnly - not allowed o setRollbackOnly - not allowed
   *
   * Create a stateless Session Bean. Deploy it on the J2EE server. Verify
   * correct operations for allowed methods and that getCallerPrincipal returns
   * non-null principal.
   *
   */

  public void slbmAllowedMethodsTest3() throws Fault {
    TestUtil.logTrace("Operation Tests for businessMethod");
    boolean pass = true;
    String expected[] = { "true", "true", "true", "true", "true", "true",
        "true", "true", "true", "true", "true", "true", "true", "true", "true",
        "true", "true", "true", "true", "true", "false", "false", "false" };
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
      pass = checkResults(results, "businessMethod", expected);
      helperRef.remove();
    } catch (Exception e) {
      throw new Fault("slbmAllowedMethodsTest3 failed", e);
    }

    try {
      beanRef.remove();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught removing bean:" + e, e);
    }

    if (!pass)
      throw new Fault("slbmAllowedMethodsTest3 failed");
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
