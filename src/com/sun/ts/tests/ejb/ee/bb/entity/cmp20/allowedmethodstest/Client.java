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
 */

/*
 * @(#)Client.java	1.23 03/05/27
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.allowedmethodstest;

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
    if (!p.getProperty("getPrimaryKey").equals(r[5])) {
      logErr("getPrimaryKey operations test failed");
      pass = false;
    }
    if (!p.getProperty("getEJBLocalHome").equals(r[6])) {
      logErr("getEJBLocalHome operations test failed");
      pass = false;
    }
    if (!p.getProperty("getEJBLocalObject").equals(r[7])) {
      logErr("getEJBLocalObject operations test failed");
      pass = false;
    }
    if (!p.getProperty("getTimerService").equals(r[8])) {
      logErr("getTimerService operations test failed");
      pass = false;
    }

    if (!p.getProperty("TimerService_Methods_Test1").equals(r[9])) {
      logErr("TimerService_Methods_Test1 operations test failed");
      pass = false;
    }

    if (!p.getProperty("TimerService_Methods_Test2").equals(r[10])) {
      logErr("TimerService_Methods_Test2 operations test failed");
      pass = false;
    }

    if (!p.getProperty("TimerService_Methods_Test3").equals(r[11])) {
      logErr("TimerService_Methods_Test3 operations test failed");
      pass = false;
    }

    if (!p.getProperty("TimerService_Methods_Test4").equals(r[12])) {
      logErr("TimerService_Methods_Test4 operations test failed");
      pass = false;
    }
    if (!p.getProperty("TimerService_Methods_Test5").equals(r[13])) {
      logErr("TimerService_Methods_Test5 operations test failed");
      pass = false;
    }

    if (!p.getProperty("TimerService_Methods_Test6").equals(r[14])) {
      logErr("TimerService_Methods_Test6 operations test failed");
      pass = false;
    }

    if (!p.getProperty("TimerService_Methods_Test7").equals(r[15])) {
      logErr("TimerService_Methods_Test7 operations test failed");
      pass = false;
    }

    if (!p.getProperty("getRollbackOnly").equals(r[16])) {
      logErr("getRollbackOnly operations test failed");
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
   * user; password; generateSQL;
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
   * @testName: cmp20AllowedMethodsTest1
   * 
   * @assertion_ids: EJB:SPEC:252.1; EJB:SPEC:252.2; EJB:JAVADOC:190;
   * EJB:SPEC:252.3; EJB:SPEC:252.4; EJB:SPEC:252.5; EJB:SPEC:252.7;
   * EJB:SPEC:252.8; EJB:SPEC:252.9; EJB:SPEC:252.12; EJB:SPEC:252.13;
   * EJB:SPEC:252.14; EJB:JAVADOC:115; EJB:JAVADOC:110; EJB:JAVADOC:117;
   * EJB:SPEC:250; EJB:SPEC:251; EJB:JAVADOC:215; EJB:JAVADOC:220;
   * EJB:JAVADOC:224; EJB:JAVADOC:227; EJB:JAVADOC:231; EJB:JAVADOC:191;
   * EJB:JAVADOC:195; EJB:SPEC:823
   * 
   * @test_strategy: Operations allowed and not allowed in the ejbCreate method
   * of an entity bean are: o getEJBHome - allowed o getCallerPrincipal -
   * allowed o isCallerInRole - allowed o getEJBObject - not allowed o
   * JNDI_Access - allowed o getPrimaryKey - not allowed o getEJBLocalHome -
   * allowed o getEJBLocalObject - not allowed o getTimerService - allowed o
   * TimerService_Methods_Test1 - not allowed o TimerService_Methods_Test2 - not
   * allowed o TimerService_Methods_Test3 - not allowed o
   * TimerService_Methods_Test4 - not allowed o TimerService_Methods_Test5 - not
   * allowed o TimerService_Methods_Test6 - not allowed o
   * TimerService_Methods_Test7 - not allowed o getRollbackOnly - allowed o
   * setRollbackOnly - allowed - NOT TESTED
   *
   * Deploy it on the J2EE server. Verify correct operations. Ensure
   * getCallerPrincipal() does not return null.
   *
   *
   */

  public void cmp20AllowedMethodsTest1() throws Fault {
    logTrace("Operation Tests for ejbCreate");
    boolean pass = true;
    String expected[] = { "true", "true", "true", "false", "true", "false",
        "true", "false", "true", "false", "false", "false", "false", "false",
        "false", "false", "true" };
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
      throw new Fault("cmp20AllowedMethodsTest1 failed", e);
    }

    try {
      beanRef.findAndCancelTimer();
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
      throw new Fault("cmp20AllowedMethodsTest1 failed");
  }

  /*
   * @testName: cmp20AllowedMethodsTest2
   * 
   * @assertion_ids: EJB:SPEC:253.1; EJB:SPEC:253.2; EJB:SPEC:253.3;
   * EJB:SPEC:253.4; EJB:SPEC:253.5; EJB:SPEC:253.7; EJB:SPEC:253.8;
   * EJB:SPEC:253.9; EJB:SPEC:253.12; EJB:SPEC:253.13; EJB:SPEC:253.14
   * 
   * @test_strategy: Operations allowed and not allowed in the ejbPostCreate
   * method of an entity bean are: o getEJBHome - allowed o getCallerPrincipal -
   * allowed o isCallerInRole - allowed o getEJBObject - allowed o JNDI_Access -
   * allowed o getPrimaryKey - allowed o getEJBLocalHome - allowed o
   * getEJBLocalObject - allowed o getTimerService - allowed o
   * TimerService_Methods_Test1 - allowed o TimerService_Methods_Test2 - allowed
   * o TimerService_Methods_Test3 - allowed o TimerService_Methods_Test4 -
   * allowed o TimerService_Methods_Test5 - allowed o TimerService_Methods_Test6
   * - allowed o TimerService_Methods_Test7 - allowed o getRollbackOnly -
   * allowed o setRollbackOnly - allowed - NOT TESTED
   * 
   * Deploy it on the J2EE server. Verify correct operations.
   */

  public void cmp20AllowedMethodsTest2() throws Fault {
    TestUtil.logTrace("Operation Tests for ejbPostCreate");
    boolean pass = true;
    String expected[] = { "true", "true", "true", "true", "true", "true",
        "true", "true", "true", "true", "true", "true", "true", "true", "true",
        "true", "true" };
    try {
      // create Helper EJB instance
      logMsg("Create Helper EJB instance");
      helperRef = (Helper) helperHome.create();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, helperRef, 2, "coffee-1", 1,
          2);
      results = helperRef.getData();
      pass = checkResults(results, "ejbPostCreate", expected);
      helperRef.remove();
    } catch (Exception e) {
      throw new Fault("cmp20AllowedMethodsTest2 failed", e);
    }

    try {
      beanRef.findAndCancelTimer();
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
      throw new Fault("cmp20AllowedMethodsTest2 failed");
  }

  /*
   * @testName: cmp20AllowedMethodsTest3
   * 
   * @assertion_ids: EJB:SPEC:262.1; EJB:SPEC:262.2; EJB:JAVADOC:188;
   * EJB:SPEC:262.3; EJB:SPEC:262.4; EJB:SPEC:262.5; EJB:SPEC:262.7;
   * EJB:SPEC:262.8; EJB:SPEC:262.9; EJB:SPEC:262.12; EJB:SPEC:262.13;
   * EJB:SPEC:262.14; EJB:JAVADOC:106; EJB:JAVADOC:215; EJB:JAVADOC:220;
   * EJB:JAVADOC:224; EJB:JAVADOC:227; EJB:JAVADOC:231; EJB:JAVADOC:191;
   * EJB:JAVADOC:195;
   * 
   * @test_strategy: Operations allowed and not allowed in the setEntityContext
   * method of an entity bean are: o getEJBHome - allowed o getCallerPrincipal -
   * not allowed o isCallerInRole - not allowed o getEJBObject - not allowed o
   * JNDI_Access - allowed o getPrimaryKey - not allowed o getEJBLocalHome -
   * allowed o getEJBLocalObject - not allowed o getTimerService - not allowed o
   * TimerService_Methods_Test1 - not allowed o TimerService_Methods_Test2 - not
   * allowed o TimerService_Methods_Test3 - not allowed o
   * TimerService_Methods_Test4 - not allowed o TimerService_Methods_Test5 - not
   * allowed o TimerService_Methods_Test6 - not allowed o
   * TimerService_Methods_Test7 - not allowed o getRollbackOnly - not allowed o
   * setRollbackOnly - not allowed - NOT TESTED
   * 
   * Deploy it on the J2EE server. Verify correct operations.
   */

  public void cmp20AllowedMethodsTest3() throws Fault {
    TestUtil.logTrace("Operation Tests for setEntityContext");
    boolean pass = true;
    String expected[] = { "true", "false", "false", "false", "true", "false",
        "true", "false", "false", "false", "false", "false", "false", "false",
        "false", "false", "false" };
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 3, "coffee-1", 1);
      results = beanRef.getResults();
      pass = checkResults(results, "setEntityContext", expected);
    } catch (Exception e) {
      throw new Fault("cmp20AllowedMethodsTest3 failed", e);
    }

    try {
      beanRef.findAndCancelTimer();
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
      throw new Fault("cmp20AllowedMethodsTest3 failed");
  }

  /*
   * @testName: cmp20AllowedMethodsTest4
   * 
   * @assertion_ids: EJB:SPEC:260.1; EJB:SPEC:260.2; EJB:SPEC:260.3;
   * EJB:SPEC:260.4; EJB:SPEC:260.5; EJB:SPEC:260.7; EJB:SPEC:260.8;
   * EJB:SPEC:260.9; EJB:SPEC:260.12; EJB:SPEC:260.13; EJB:SPEC:260.14
   * 
   * @test_strategy: Operations allowed and not allowed in a business method
   * from the component interface of an entity bean are: o getEJBHome - allowed
   * o getCallerPrincipal - allowed o isCallerInRole - allowed o getEJBObject -
   * allowed o JNDI_Access - allowed o getPrimaryKey - allowed o getEJBLocalHome
   * - allowed o getEJBLocalObject - allowed o getTimerService - allowed o
   * TimerService_Methods_Test1 - allowed o TimerService_Methods_Test2 - allowed
   * o TimerService_Methods_Test3 - allowed o TimerService_Methods_Test4 -
   * allowed o TimerService_Methods_Test5 - allowed o TimerService_Methods_Test6
   * - allowed o TimerService_Methods_Test7 - allowed o getRollbackOnly -
   * allowed o setRollbackOnly - allowed - NOT TESTED
   * 
   * Deploy it on the J2EE server. Verify correct operations.
   *
   */

  public void cmp20AllowedMethodsTest4() throws Fault {
    TestUtil.logTrace("Operation Tests for businessMethod");
    boolean pass = true;
    String expected[] = { "true", "true", "true", "true", "true", "true",
        "true", "true", "true", "true", "true", "true", "true", "true", "true",
        "true", "true" };
    try {
      // create Helper EJB instance
      logMsg("Create Helper EJB instance");
      helperRef = (Helper) helperHome.create();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 4, "coffee-1", 1);
      logMsg("Calling EJB business method");
      beanRef.businessMethod(helperRef);
      results = helperRef.getData();
      pass = checkResults(results, "businessMethod", expected);
      helperRef.remove();
    } catch (Exception e) {
      throw new Fault("cmp20AllowedMethodsTest4 failed", e);
    }

    try {
      beanRef.findAndCancelTimer();
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
      throw new Fault("cmp20AllowedMethodsTest4 failed");
  }

  /*
   * @testName: cmp20AllowedMethodsTest5
   * 
   * @assertion_ids: EJB:SPEC:258.1; EJB:SPEC:258.2; EJB:SPEC:258.3;
   * EJB:SPEC:258.4; EJB:SPEC:258.5; EJB:SPEC:258.7; EJB:SPEC:258.8;
   * EJB:SPEC:258.9; EJB:SPEC:258.12; EJB:SPEC:258.13; EJB:SPEC:258.14
   * 
   * @test_strategy: Operations allowed and not allowed in the ejbLoad method of
   * an entity bean are: o getEJBHome - allowed o getCallerPrincipal - allowed o
   * isCallerInRole - allowed o getEJBObject - allowed o JNDI_Access - allowed o
   * getPrimaryKey - allowed o getEJBLocalHome - allowed o getEJBLocalObject -
   * allowed o getTimerService - allowed o TimerService_Methods_Test1 - allowed
   * o TimerService_Methods_Test2 - allowed o TimerService_Methods_Test3 -
   * allowed o TimerService_Methods_Test4 - allowed o TimerService_Methods_Test5
   * - allowed o TimerService_Methods_Test6 - allowed o
   * TimerService_Methods_Test7 - allowed o getRollbackOnly - allowed o
   * setRollbackOnly - allowed - NOT TESTED
   * 
   * Deploy it on the J2EE server. Verify correct operations.
   * 
   *
   */

  public void cmp20AllowedMethodsTest5() throws Fault {
    TestUtil.logTrace("Operation Tests for ejbLoad");
    boolean pass = true;
    String expected[] = { "true", "true", "true", "true", "true", "true",
        "true", "true", "true", "true", "true", "true", "true", "true", "true",
        "true", "true" };
    try {
      // create Helper EJB instance
      logMsg("Create Helper EJB instance");
      helperRef = (Helper) helperHome.create();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 5, "coffee-1", 1);
      logMsg("Calling EJB business method");
      try {
        beanRef.businessMethod(helperRef);
      } catch (RemoteException e) {
        TestUtil.logErr(
            "Unexpected RemoteException caught invoking business method:" + e,
            e);
      }
      results = helperRef.getData();
      pass = checkResults(results, "ejbLoad", expected);
      helperRef.remove();
    } catch (Exception e) {
      throw new Fault("cmp20AllowedMethodsTest5 failed", e);
    }

    try {
      beanRef.findAndCancelTimer();
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
      throw new Fault("cmp20AllowedMethodsTest5 failed");
  }

  /*
   * @testName: cmp20AllowedMethodsTest7
   * 
   * @assertion_ids: EJB:SPEC:255.1; EJB:SPEC:255.2; EJB:SPEC:255.3;
   * EJB:SPEC:255.4; EJB:SPEC:255.5; EJB:SPEC:255.7; EJB:SPEC:255.8;
   * EJB:SPEC:255.9; EJB:SPEC:255.12; EJB:SPEC:255.13; EJB:SPEC:255.14;
   * EJB:JAVADOC:215; EJB:JAVADOC:220; EJB:JAVADOC:224; EJB:JAVADOC:227;
   * EJB:JAVADOC:231; EJB:JAVADOC:191; EJB:JAVADOC:195;
   * 
   * 
   * @test_strategy: Operations allowed and not allowed in an ejbHome method of
   * an entity bean are: o getEJBHome - allowed o getCallerPrincipal - allowed o
   * isCallerInRole - allowed o getEJBObject - not allowed o JNDI_Access -
   * allowed o getPrimaryKey - not allowed o getEJBLocalHome - allowed o
   * getEJBLocalObject - not allowed o getTimerService - allowed o
   * TimerService_Methods_Test1 - not allowed o TimerService_Methods_Test2 - not
   * allowed o TimerService_Methods_Test3 - not allowed o
   * TimerService_Methods_Test4 - not allowed o TimerService_Methods_Test5 - not
   * allowed o TimerService_Methods_Test6 - not allowed o
   * TimerService_Methods_Test7 - not allowed o getRollbackOnly - allowed o
   * setRollbackOnly - allowed - NOT TESTED
   * 
   * Deploy it on the J2EE server. Verify correct operations.
   *
   */

  public void cmp20AllowedMethodsTest7() throws Fault {
    TestUtil.logTrace("Operation Tests for ejbHome");
    boolean pass = true;
    String expected[] = { "true", "true", "true", "false", "true", "false",
        "true", "false", "true", "false", "false", "false", "false", "false",
        "false", "false", "true" };
    try {
      logMsg("Create Helper EJB instance");
      helperRef = (Helper) helperHome.create();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 8, "coffee-1", 1);
      logMsg("Initialize remote logging");
      beanRef.initLogging(props);
      logMsg("Calling doTest home method");
      beanHome.doTest(helperRef);
      results = helperRef.getData();
      logTrace("Results for ejbHomeDoTest are:  " + helperRef.getData());
      pass = checkResults(results, "ejbHomeDoTest", expected);
      helperRef.remove();
    } catch (Exception e) {
      throw new Fault("cmp20AllowedMethodsTest7 failed", e);
    }

    try {
      beanRef.findAndCancelTimer();
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
      throw new Fault("cmp20AllowedMethodsTest7 failed");
  }

  /*
   * @testName: cmp20AllowedMethodsTest8
   * 
   * @assertion_ids: EJB:SPEC:61.6; EJB:SPEC:823
   * 
   * @test_Strategy: Verify Principal reference is returned for Entity CMP 2.0
   * bean extending TimedObject with security-identity set to
   * use-caller-identity.
   *
   */

  public void cmp20AllowedMethodsTest8() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 9, "coffee-1", 1);
      logMsg("Initialize remote logging");
      beanRef.initLogging(props);
      pass = beanRef.getCallerPrincipalTest(user_value);
    } catch (Exception e) {
      throw new Fault("cmp20AllowedMethodsTest8 failed", e);
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
      throw new Fault("cmp20AllowedMethodsTest8 failed");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("Cleanup OK");
  }
}
