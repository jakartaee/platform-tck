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
 * @(#)Client.java	1.39 03/05/27
 */

package com.sun.ts.tests.ejb.ee.bb.entity.bmp.allowedmethodstest;

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

  private long timerDuration = 0;

  private long timerWait = 0;

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
   * ejb_timeout; ejb_wait; user; password;
   * 
   * @class.testArgs: -ap tssql.stmt
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    user_value = props.getProperty(user);
    password_value = props.getProperty(password);
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

      timerDuration = Long.parseLong(p.getProperty("ejb_timeout"));
      timerWait = Long.parseLong(p.getProperty("ejb_wait"));

      logMsg("Setup ok");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: bmpAllowedMethodsTest1
   * 
   * @assertion_ids: EJB:SPEC:453.1; EJB:SPEC:453.2; EJB:SPEC:453.3;
   * EJB:SPEC:453.4; EJB:SPEC:453.5; EJB:SPEC:453.7; EJB:SPEC:453.8;
   * EJB:SPEC:453.9; EJB:SPEC:453.12; EJB:SPEC:453.13; EJB:SPEC:453.14;
   * EJB:JAVADOC:215; EJB:JAVADOC:220; EJB:JAVADOC:224; EJB:JAVADOC:227;
   * EJB:JAVADOC:231; EJB:JAVADOC:191; EJB:JAVADOC:195
   * 
   * @test_Strategy: Operations allowed and not allowed in the ejbCreate method
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
   * Deploy it on the J2EE server. Verify correct operations.
   * 
   *
   */

  public void bmpAllowedMethodsTest1() throws Fault {
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
      throw new Fault("bmpAllowedMethodsTest1 failed", e);
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
      throw new Fault("bmpAllowedMethodsTest1 failed");
  }

  /*
   * @testName: bmpAllowedMethodsTest2
   * 
   * @assertion_ids: EJB:SPEC:454.1; EJB:SPEC:454.2; EJB:SPEC:454.3;
   * EJB:SPEC:454.4; EJB:SPEC:454.5; EJB:SPEC:454.7; EJB:SPEC:454.8;
   * EJB:SPEC:454.9; EJB:SPEC:454.12; EJB:SPEC:454.13; EJB:SPEC:454.14
   * 
   * @test_Strategy: Operations allowed and not allowed in the ejbPostCreate
   * method of an entity bean are: o getEJBHome - allowed o getCallerPrincipal -
   * allowed o isCallerInRole - allowed o getEJBObject - allowed o JNDI_Access -
   * allowed o getPrimaryKey - allowed o getEJBLocalHome - allowed o
   * getEJBLocalObject - allowed o getTimerService - allowed o
   * TimerService_Methods_Test1 - allowed o TimerService_Methods_Test2 - allowed
   * o TimerService_Methods_Test3 - allowed o TimerService_Methods_Test4 -
   * allowed o TimerService_Methods_Test5 - allowed o TimerService_Methods_Test6
   * - allowed o TimerService_Methods_Test7 - allowed o getRollbackOnly -
   * allowed o getRollbackOnly - allowed o setRollbackOnly - allowed - NOT
   * TESTED
   * 
   * Deploy it on the J2EE server. Verify correct operations.
   *
   */

  public void bmpAllowedMethodsTest2() throws Fault {
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
      beanRef = (TestBean) beanHome.create(props, helperRef, 2, "coffee-2", 1,
          2);
      results = helperRef.getData();
      pass = checkResults(results, "ejbPostCreate", expected);
      helperRef.remove();
    } catch (Exception e) {
      throw new Fault("bmpAllowedMethodsTest2 failed", e);
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
      throw new Fault("bmpAllowedMethodsTest2 failed");
  }

  /*
   * @testName: bmpAllowedMethodsTest3
   * 
   * @assertion_ids: EJB:SPEC:463.1; EJB:SPEC:463.2; EJB:JAVADOC:115;
   * EJB:SPEC:463.3; EJB:SPEC:463.4; EJB:SPEC:463.5; EJB:SPEC:463.7;
   * EJB:SPEC:463.8; EJB:SPEC:463.9; EJB:SPEC:463.12; EJB:SPEC:463.13;
   * EJB:SPEC:463.14
   * 
   * @test_Strategy: Operations allowed and not allowed in the setEntityContext
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
   *
   */

  public void bmpAllowedMethodsTest3() throws Fault {
    TestUtil.logTrace("Operation Tests for setEntityContext");
    boolean pass = true;
    String expected[] = { "true", "false", "false", "false", "true", "false",
        "true", "false", "false", "false", "false", "false", "false", "false",
        "false", "false", "false" };
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 3, "coffee-3", 1);
      results = beanRef.getResults();
      pass = checkResults(results, "setEntityContext", expected);
    } catch (Exception e) {
      throw new Fault("bmpAllowedMethodsTest3 failed", e);
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
      throw new Fault("bmpAllowedMethodsTest3 failed");
  }

  /*
   * @testName: bmpAllowedMethodsTest4
   * 
   * @assertion_ids: EJB:SPEC:461.1; EJB:SPEC:461.2; EJB:SPEC:461.3;
   * EJB:SPEC:461.4; EJB:SPEC:461.5; EJB:SPEC:461.7; EJB:SPEC:461.8;
   * EJB:SPEC:461.9; EJB:SPEC:461.12; EJB:SPEC:461.13; EJB:SPEC:461.14
   * 
   * @test_Strategy: Operations allowed and not allowed in a business method
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

  public void bmpAllowedMethodsTest4() throws Fault {
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
      beanRef = (TestBean) beanHome.create(props, 4, "coffee-4", 1);
      logMsg("Calling EJB business method");
      beanRef.businessMethod(helperRef);
      results = helperRef.getData();
      pass = checkResults(results, "businessMethod", expected);
      helperRef.remove();
    } catch (Exception e) {
      throw new Fault("bmpAllowedMethodsTest4 failed", e);
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
      throw new Fault("bmpAllowedMethodsTest4 failed");
  }

  /*
   * @testName: bmpAllowedMethodsTest7
   * 
   * @assertion_ids: EJB:SPEC:456.1; EJB:SPEC:456.2; EJB:SPEC:456.3;
   * EJB:SPEC:456.4; EJB:SPEC:456.5; EJB:SPEC:456.7; EJB:SPEC:456.8;
   * EJB:SPEC:456.9; EJB:SPEC:456.12; EJB:SPEC:456.13; EJB:SPEC:456.14
   * 
   * @test_Strategy: Operations allowed and not allowed in an ejbHome method of
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

  public void bmpAllowedMethodsTest7() throws Fault {
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
      beanRef = (TestBean) beanHome.create(props, 9, "coffee-9", 1);
      logMsg("Calling doTest home method");
      beanHome.doTest(helperRef);
      results = helperRef.getData();
      logTrace("Results for ejbHomeDoTest are:  " + helperRef.getData());
      pass = checkResults(results, "ejbHomeDoTest", expected);
    } catch (Exception e) {
      throw new Fault("bmpAllowedMethodsTest7 failed", e);
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
      throw new Fault("bmpAllowedMethodsTest7 failed");
  }

  /*
   *
   * @testName: bmpAllowedMethodsTest8
   * 
   * @assertion_ids: EJB:SPEC:10464.1; EJB:SPEC:10464.2; EJB:SPEC:10464.3;
   * EJB:SPEC:10464.4; EJB:SPEC:10464.5; EJB:SPEC:10464.7; EJB:SPEC:10464.8;
   * EJB:SPEC:10464.9; EJB:SPEC:10464.12; EJB:SPEC:10464.13; EJB:SPEC:10464.14
   * 
   * @test_Strategy: Operations allowed and not allowed in an ejbFind method of
   * an entity bean are: o getEJBHome - allowed o getCallerPrincipal - allowed o
   * isCallerInRole - allowed o getEJBObject - not allowed o JNDI_Access -
   * allowed o getPrimaryKey - not allowed o getEJBLocalHome - allowed o
   * getEJBLocalObject - not allowed o getTimerService - not allowed o
   * TimerService_Methods_Test1 - not allowed o TimerService_Methods_Test2 - not
   * allowed o TimerService_Methods_Test3 - not allowed o
   * TimerService_Methods_Test4 - not allowed o TimerService_Methods_Test5 - not
   * allowed o TimerService_Methods_Test6 - not allowed o
   * TimerService_Methods_Test7 - not allowed o getRollbackOnly - allowed o
   * setRollbackOnly - allowed - NOT TESTED
   * 
   * Deploy it on the J2EE server. Verify correct operations.
   * 
   *
   */

  public void bmpAllowedMethodsTest8() throws Fault {
    TestUtil.logTrace("Operation Tests for ejbFindTheBean");
    boolean pass = true;
    String expected[] = { "true", "true", "true", "false", "true", "false",
        "true", "false", "false", "false", "false", "false", "false", "false",
        "false", "false", "true" };
    try {
      logMsg("Create Helper EJB instance");
      helperRef = (Helper) helperHome.create();
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props, 10, "coffee-10", 1);
      beanRef = beanHome.findTheBean(props, new Integer(10), helperRef);
      results = helperRef.getData();
      TestUtil.logTrace("results are:  " + helperRef.getData());
      pass = checkResults(results, "ejbFindTheBean", expected);
      helperRef.remove();
    } catch (Exception e) {
      throw new Fault("bmpAllowedMethodsTest8 failed", e);
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
      throw new Fault("bmpAllowedMethodsTest8 failed");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
