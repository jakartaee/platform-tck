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

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.entitycontexttest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.rmi.RemoteException;
import java.util.*;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import javax.ejb.CreateException;

//*****************************************************************************
//EntityContext test for entity bean access to it's runtime container context
//CMP 2.0 entity bean
//*****************************************************************************

public class Client extends EETest {
  // <env-entry> declared in ejb-jar.xml for the entity bean TestBean
  private static final String ENV_ENTRY_NAME = "user";

  private static final String ENV_ENTRY_VALUE = "cts1";

  private static final String testName = "EntityContextTest";

  // JNDI names of test beans
  private static final String testBean = "java:comp/env/ejb/TestBean";

  private static final String testProps = "entitycontexttest.properties";

  private static final String testDir = System.getProperty("user.dir");

  private Properties props = null;

  private TestBean beanRef = null;

  private TestBeanHome beanHome = null;

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
   * @assertion_ids: EJB:SPEC:249.1; EJB:JAVADOC:114
   * 
   * @test_Strategy: Create an Entity CMP 2.0 Bean. Deploy it on the J2EE
   * server. Verify EJBObject reference returned.
   *
   */

  public void test1() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create("1", "coffee-1");
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
   * @assertion_ids: EJB:SPEC:249.2; EJB:JAVADOC:22
   * 
   * @test_Strategy: Create an Entity CMP 2.0 Bean. Deploy it on the J2EE
   * server. Verify EJBHome reference returned.
   *
   */

  public void test2() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create("2", "coffee-2");
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
   * @assertion_ids: EJB:SPEC:249.3; EJB:JAVADOC:112
   * 
   * @test_Strategy: Create an Entity CMP 2.0 Bean. Deploy it on the J2EE
   * server. Verify EJBLocalObject received.
   *
   */

  public void test3() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create("3", "coffee-3");
      pass = beanRef.getEJBLocalObjectTest();
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
   * @assertion_ids: EJB:SPEC:249.5; EJB:JAVADOC:21
   * 
   * @test_Strategy: Create an Entity CMP 2.0 Bean. Deploy it on the J2EE
   * server. Verify Principal reference returned.
   *
   */

  public void test4() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create("4", "coffee-4");
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
   * @assertion_ids: EJB:SPEC:249.6; EJB:JAVADOC:32
   * 
   * @test_Strategy: Create an Entity CMP 2.0 Bean. Deploy it on the J2EE
   * server. Verify correct identity role. This is a POSITIVE test where caller
   * is in role1.
   *
   */

  public void test5() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create("5", "coffee-5");
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
   * @assertion_ids: EJB:SPEC:249.6; EJB:JAVADOC:32
   * 
   * @test_Strategy: Create an Entity CMP 2.0 Bean. Deploy it on the J2EE
   * server. Verify correct identity role. This is a NEGATIVE test where caller
   * is not in role2.
   *
   */

  public void test5b() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create("5b", "coffee-5b");
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
   * @assertion_ids: EJB:SPEC:249.7; EJB:JAVADOC:34
   * 
   * @test_Strategy: Create an Entity CMP 2.0 Bean. Deploy it on the J2EE
   * server. Verify setRollback functionality since entity beans are always
   * container-managed.
   *
   */

  public void test6() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create("6", "coffee-6");
      pass = beanRef.setRollbackOnlyTest();
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test6 failed", e);
    }
    if (!pass)
      throw new Fault("test6 failed");
  }

  /*
   * @testName: test7
   * 
   * @assertion_ids: EJB:SPEC:249.8; EJB:JAVADOC:25
   * 
   * @test_Strategy: Create an Entity CMP 2.0 Bean. Deploy it on the J2EE
   * server. Verify getRollback functionality since entity beans are always
   * container-managed.
   *
   */

  public void test7() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create("7", "coffee-7");
      pass = beanRef.getRollbackOnlyTest();
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test7 failed", e);
    }
    if (!pass)
      throw new Fault("test7 failed");
  }

  /*
   * @testName: test8
   * 
   * @assertion_ids: EJB:SPEC:249.11; EJB:JAVADOC:30
   * 
   * @test_Strategy: Create an Entity CMP 2.0 Bean. Deploy it on the J2EE
   * server. Verify entity bean cannot obtain UserTransaction.
   *
   */

  public void test8() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create("8", "coffee-8");
      logMsg("EntityBeans cannot obtain UserTransaction interface");
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
   * @assertion_ids: EJB:SPEC:249.9; EJB:JAVADOC:116
   * 
   * @test_Strategy: Create an Entity CMP 2.0 Bean. Deploy it on the J2EE
   * server. Verify entity bean can obtain its primary key.
   *
   */

  public void test9() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create("9", "coffee-9");
      logMsg("Obtain primary key");
      pass = beanRef.getPrimaryKeyTest("9");
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test9 failed", e);
    }
    if (!pass)
      throw new Fault("test9 failed");
  }

  /*
   * @testName: test10
   * 
   * @assertion_ids: EJB:SPEC:249.10; EJB:JAVADOC:27
   * 
   * @test_Strategy: Create an Entity CMP 2.0 Bean. Deploy it on the J2EE
   * server. Verify entity bean can obtain timer service.
   *
   */

  public void test10() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create("10", "coffee-10");
      logMsg("CMP 2.0 Entity Bean can obtain Timer Service");
      pass = beanRef.getTimerServiceTest();
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test10 failed", e);
    }
    if (!pass)
      throw new Fault("test10 failed");
  }

  /*
   * @testName: test11
   * 
   * @assertion_ids: EJB:SPEC:249.4; EJB:JAVADOC:23
   * 
   * @test_Strategy: Create an Entity CMP 2.0 Bean. Deploy it on the J2EE
   * server. Verify EJBLocalHome reference returned.
   *
   */
  public void test11() throws Fault {
    boolean pass = false;
    try {
      // create EJB instance
      logMsg("Create EJB instance");
      beanRef = (TestBean) beanHome.create("11", "coffee-11");
      pass = beanRef.getEJBLocalHomeTest();
      beanRef.remove();
    } catch (Exception e) {
      throw new Fault("test11 failed", e);
    }
    if (!pass)
      throw new Fault("test11 failed");
  }

  /*
   * @testName: getIt
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: client -> stateless FastPathBean -> stateless FastPathBean2
   * -> CMP 2.x Entity TestBean.getIt(). It is a non-CMP business method.
   */
  public void getIt() throws Exception {
    FastPath fastPath = getFastPathBean();
    String result = fastPath.getIt(ENV_ENTRY_NAME);
    if (ENV_ENTRY_VALUE.equals(result)) {
      logMsg("Got expected result when looking up env-entry user: " + result);
    } else {
      throw new Fault("Expecting " + ENV_ENTRY_VALUE
          + " from looking up env-entry user, but actaul " + result);
    }
  }

  /*
   * @testName: setIt
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: client -> stateless FastPathBean -> stateless FastPathBean2
   * -> CMP 2.x Entity TestBean.setIt(). It is a non-CMP business method.
   */
  public void setIt() throws Exception {
    FastPath fastPath = getFastPathBean();
    String result = fastPath.setIt(ENV_ENTRY_NAME);
    if (ENV_ENTRY_VALUE.equals(result)) {
      logMsg("Got expected result when looking up env-entry user: " + result);
    } else {
      throw new Fault("Expecting " + ENV_ENTRY_VALUE
          + " from looking up env-entry user, but actaul " + result);
    }
  }

  /*
   * @testName: getCoffeeId
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: client -> stateless FastPathBean -> stateless FastPathBean2
   * -> CMP 2.x Entity TestBean.getCoffeeId(). It is a CMP business method.
   */
  public void getCoffeeId() throws Exception {
    final String coffeeId = "coffeeId";
    FastPath fastPath = getFastPathBean();
    String result = fastPath.getCoffeeId(coffeeId);
    if (coffeeId.equals(result)) {
      logMsg("Got expected result when calling FastPathBean.getCoffeeId "
          + result);
    } else {
      throw new Fault("Expecting " + coffeeId
          + " from calling FastPathBean.getCoffeeId, but actual " + result);
    }
  }

  /*
   * @testName: setBrandName
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: client -> stateless FastPathBean -> stateless FastPathBean2
   * -> CMP 2.x Entity TestBean.setBrandName(). It is a CMP business method.
   */
  public void setBrandName() throws Exception {
    final String oldBrand = "oldBrand";
    final String newBrand = "newBrand";
    FastPath fastPath = getFastPathBean();
    String result = fastPath.setCoffeeBrand(oldBrand, newBrand);
    if (newBrand.equals(result)) {
      logMsg("Got expected result when calling FastPathBean.setCoffeeBrand "
          + result);
    } else {
      throw new Fault("Expecting " + newBrand
          + " from calling FastPathBean.setCoffeeBrand, but actual " + result);
    }
  }

  private FastPath getFastPathBean()
      throws Exception, CreateException, RemoteException {
    FastPathHome fastPathHome = (FastPathHome) nctx
        .lookup("java:comp/env/ejb/FastPathBean", FastPathHome.class);
    FastPath fastPath = fastPathHome.create();
    return fastPath;
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
