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

package com.sun.ts.tests.ejb.ee.bb.session.stateful.sessioncontexttest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.transaction.*;
import java.rmi.*;

import com.sun.javatest.Status;

//*****************************************************************************
//SessionContext test for session bean access to it's runtime container context
//*****************************************************************************

public class Client extends EETest {
  private static final String testName = "SessionContextTest";

  // JNDI names of test beans
  private static final String testBean = "java:comp/env/ejb/TestBean";

  private static final String txNotSupported = "java:comp/env/ejb/TxNotSupported";

  private static final String txSupports = "java:comp/env/ejb/TxSupports";

  private static final String txRequired = "java:comp/env/ejb/TxRequired";

  private static final String txRequiresNew = "java:comp/env/ejb/TxRequiresNew";

  private static final String txMandatory = "java:comp/env/ejb/TxMandatory";

  private static final String txNever = "java:comp/env/ejb/TxNever";

  private static final String testProps = "sessioncontexttest.properties";

  private static final String testDir = System.getProperty("user.dir");

  private Properties props = null;

  private TestBean beanRef = null;

  private TestBeanHome beanHome = null;

  private TestBean2 bean2Ref = null;

  private TestBean2Home bean2Home = null;

  private TSNamingContext nctx = null;

  private static final String user = "user", password = "password";

  private String user_value, password_value, role1_value = "Employee",
      role2_value = "Manager";

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * user; password;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    user_value = props.getProperty(user);
    password_value = props.getProperty(password);

    logMsg("user_value=" + user_value);
    logMsg("password_value=" + password_value);
    logMsg("role1_value=" + role1_value);
    logMsg("role2_value=" + role2_value);

    try {
      logMsg("Obtain naming context");
      nctx = new TSNamingContext();
      logMsg("Obtain login context and login as: " + user_value);
      TSLoginContext lc = new TSLoginContext();
      lc.login(user_value, password_value);

      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + testBean);
      beanHome = (TestBeanHome) nctx.lookup(testBean, TestBeanHome.class);
      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /* Run test */

  /*
   * @testName: test1
   * 
   * @assertion_ids: EJB:SPEC:61; EJB:SPEC:61.1
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Verify EJBObject reference returned.
   *
   */

  public void test1() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props);
      pass = beanRef.getEJBObjectTest();
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test1 failed", e);
    }
    if (!pass)
      throw new Fault("test1 failed");
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: EJB:SPEC:61; EJB:SPEC:61.2
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Verify EJBHome reference returned.
   *
   */

  public void test2() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props);
      pass = beanRef.getEJBHomeTest();
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test2 failed", e);
    }
    if (!pass)
      throw new Fault("test2 failed");
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: EJB:SPEC:61; EJB:SPEC:61.5
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Verify Properies object received.
   *
   */

  public void test3() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props);
      pass = beanRef.getEnvironmentTest();
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test3 failed", e);
    }
    if (!pass)
      throw new Fault("test3 failed");
  }

  /*
   * @testName: test4
   * 
   * @assertion_ids:EJB:SPEC:61; EJB:SPEC:61.6
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Verify Principal reference returned.
   *
   */

  public void test4() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props);
      pass = beanRef.getCallerPrincipalTest(user_value);
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test4 failed", e);
    }
    if (!pass)
      throw new Fault("test4 failed");
  }

  /*
   * @testName: test5
   * 
   * @assertion_ids: EJB:SPEC:61; EJB:SPEC:61.7
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Verify correct identity role.
   *
   */

  public void test5() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props);
      pass = beanRef.isCallerInRoleTest(role1_value);
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test5 failed", e);
    }
    if (!pass)
      throw new Fault("test5 failed");
  }

  /*
   * @testName: test5b
   * 
   * @assertion_ids: EJB:SPEC:61; EJB:SPEC:61.8
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Verify correct identity role.
   *
   */

  public void test5b() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props);
      pass = beanRef.isCallerInRoleTest(role2_value);
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test5b failed", e);
    }
    if (pass)
      throw new Fault("test5b failed");
  }

  /*
   * @testName: test6
   * 
   * @assertion_ids: EJB:SPEC:61; EJB:SPEC:61.9
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Verify IllegalStateException occurred because a bean instance
   * cannot mark the current transaction for rollbackonly if bean managed.
   *
   */

  public void test6() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props);
      pass = beanRef.setRollbackOnlyTest();
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test6 failed", e);
    }
    if (!pass)
      throw new Fault("test6 failed");
  }

  /*
   * @testName: test6b
   * 
   * @assertion_ids: EJB:SPEC:61; EJB:SPEC:61.9
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Verify transaction marked for rollback.
   *
   */

  public void test6b() throws Fault {
    boolean pass = false;
    try {
      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + txRequired);
      TestBean2Home beanHome2 = (TestBean2Home) nctx.lookup(txRequired,
          TestBean2Home.class);
      // create EJB instance
      logMsg("Create EJB instance");
      TestBean2 beanRef2 = (TestBean2) beanHome2.create(props);
      pass = beanRef2.setRollbackOnlyTest();
      beanRef2.remove();
    } catch (Exception e) {
      throw new Fault("test6b failed", e);
    }
    if (!pass)
      throw new Fault("test6b failed");
  }

  /*
   * @testName: test7
   * 
   * @assertion_ids: EJB:SPEC:61; EJB:SPEC:61.10
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Verify IllegalStateException occurred because a bean instance
   * cannot test if current transaction has been marked fro rollback if bean
   * managed.
   *
   */

  public void test7() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props);
      pass = beanRef.getRollbackOnlyTest();
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test7 failed", e);
    }
    if (!pass)
      throw new Fault("test7 failed");
  }

  /*
   * @testName: test7b
   * 
   * @assertion_ids: EJB:SPEC:61; EJB:SPEC:61.10
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Verify transaction rollback status.
   *
   */

  public void test7b() throws Fault {
    boolean pass = false;
    try {
      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + txRequired);
      TestBean2Home beanHome2 = (TestBean2Home) nctx.lookup(txRequired,
          TestBean2Home.class);
      // create EJB instance
      logMsg("Create EJB instance");
      TestBean2 beanRef2 = (TestBean2) beanHome2.create(props);
      pass = beanRef2.getRollbackOnlyTest();
      beanRef2.remove();
    } catch (Exception e) {
      throw new Fault("test7b failed", e);
    }
    if (!pass)
      throw new Fault("test7b failed");
  }

  /*
   * @testName: test8
   * 
   * @assertion_ids: EJB:SPEC:61; EJB:SPEC:61.11
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Verify TX_BEAN_MANAGED bean can obtain UserTransaction.
   *
   */

  public void test8() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props);
      logMsg("TX_BEAN_MANAGED bean can obtain UserTransaction interface");
      pass = beanRef.getUserTransactionTest();
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test8 failed", e);
    }
    if (!pass)
      throw new Fault("test8 failed");
  }

  /*
   * @testName: test9
   * 
   * @assertion_ids: EJB:SPEC:61; EJB:SPEC:61.11
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Verify bean with NotSupported transaction attribute set cannot
   * obtain UserTransaction.
   *
   */

  public void test9() throws Fault {
    boolean pass = false;
    try {
      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + txNotSupported);
      bean2Home = (TestBean2Home) nctx.lookup(txNotSupported,
          TestBean2Home.class);
      // create EJB instance
      logMsg("Create EJB instance");
      bean2Ref = (TestBean2) bean2Home.create(props);
      logMsg("NotSupported bean cannot obtain UserTransaction interface");
      pass = bean2Ref.getUserTransactionTest();
      bean2Ref.remove();
    } catch (Exception e) {
      throw new Fault("test9 failed", e);
    }
    if (!pass)
      throw new Fault("test9 failed");
  }

  /*
   * @testName: test10
   * 
   * @assertion_ids: EJB:SPEC:61; EJB:SPEC:61.11
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Verify bean with Supports transaction attribute set cannot obtain
   * UserTransaction.
   *
   */

  public void test10() throws Fault {
    boolean pass = false;
    try {
      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + txSupports);
      bean2Home = (TestBean2Home) nctx.lookup(txSupports, TestBean2Home.class);
      // create EJB instance
      logMsg("Create EJB instance");
      bean2Ref = (TestBean2) bean2Home.create(props);
      logMsg("Supports bean cannot obtain UserTransaction interface");
      pass = bean2Ref.getUserTransactionTest();
      bean2Ref.remove();
    } catch (Exception e) {
      throw new Fault("test10 failed", e);
    }
    if (!pass)
      throw new Fault("test10 failed");
  }

  /*
   * @testName: test11
   * 
   * @assertion_ids: EJB:SPEC:61; EJB:SPEC:61.11
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Verify bean with Required transaction attribute set cannot obtain
   * UserTransaction.
   *
   */

  public void test11() throws Fault {
    boolean pass = false;
    try {
      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + txRequired);
      bean2Home = (TestBean2Home) nctx.lookup(txRequired, TestBean2Home.class);
      // create EJB instance
      logMsg("Create EJB instance");
      bean2Ref = (TestBean2) bean2Home.create(props);
      logMsg("Required bean cannot obtain UserTransaction interface");
      pass = bean2Ref.getUserTransactionTest();
      bean2Ref.remove();
    } catch (Exception e) {
      throw new Fault("test11 failed", e);
    }
    if (!pass)
      throw new Fault("test11 failed");
  }

  /*
   * @testName: test12
   * 
   * @assertion_ids: EJB:SPEC:61; EJB:SPEC:61.11
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Verify bean with RequiresNew transaction attribute set cannot
   * obtain UserTransaction.
   *
   */

  public void test12() throws Fault {
    boolean pass = false;
    try {
      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + txRequiresNew);
      bean2Home = (TestBean2Home) nctx.lookup(txRequiresNew,
          TestBean2Home.class);
      // create EJB instance
      logMsg("Create EJB instance");
      bean2Ref = (TestBean2) bean2Home.create(props);
      logMsg("RequiresNew bean cannot obtain UserTransaction interface");
      pass = bean2Ref.getUserTransactionTest();
      bean2Ref.remove();
    } catch (Exception e) {
      throw new Fault("test12 failed", e);
    }
    if (!pass)
      throw new Fault("test12 failed");
  }

  /*
   * @testName: test13
   * 
   * @assertion_ids: EJB:SPEC:61; EJB:SPEC:61.11
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Verify bean with Mandatory transaction attribute set cannot obtain
   * UserTransaction.
   *
   */

  public void test13() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create(props);
      beanRef.beginTransaction();
      logMsg("Looking up home interface for EJB: " + txMandatory);
      bean2Home = (TestBean2Home) nctx.lookup(txMandatory, TestBean2Home.class);
      // create EJB instance
      logMsg("Create EJB instance");
      bean2Ref = (TestBean2) bean2Home.create(props);
      logMsg("Mandatory bean cannot obtain UserTransaction interface");
      pass = beanRef.getUserTransactionTest(bean2Ref);
      beanRef.commitTransaction();
      bean2Ref.remove();
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test13 failed", e);
    }
    if (!pass)
      throw new Fault("test13 failed");
  }

  /*
   * @testName: test14
   * 
   * @assertion_ids: EJB:SPEC:61; EJB:SPEC:61.11
   * 
   * @test_Strategy: Create a stateful Session Bean. Deploy it on the J2EE
   * server. Verify bean with Never transaction attribute set cannot obtain
   * UserTransaction.
   *
   */

  public void test14() throws Fault {
    boolean pass = false;
    try {
      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + txNever);
      bean2Home = (TestBean2Home) nctx.lookup(txNever, TestBean2Home.class);
      // create EJB instance
      logMsg("Create EJB instance");
      bean2Ref = (TestBean2) bean2Home.create(props);
      logMsg("TX_NEVER bean can obtain UserTransaction interface");
      pass = bean2Ref.getUserTransactionTest();
      bean2Ref.remove();
    } catch (Exception e) {
      throw new Fault("test14 failed", e);
    }
    if (!pass)
      throw new Fault("test14 failed");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
