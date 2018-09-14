/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.ee.propagation.am;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;

import javax.ejb.EJB;
import java.util.Properties;

public class Client extends EETest {

  @EJB(name = "ejb/Stateful3Bean", beanInterface = Stateful3IF.class)
  private static Stateful3IF statefulBean;

  @EJB(name = "ejb/Stateful3Bean2", beanInterface = Stateful3IF2.class)
  private static Stateful3IF2 statefulBean2;

  @EJB(name = "ejb/Stateless3Bean", beanInterface = Stateless3IF.class)
  private static Stateless3IF statelessBean;

  private Properties props;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props:
   */

  public void setup(String[] args, Properties p) throws Fault {
    try {
      props = p;
    } catch (Exception e) {
      throw new Fault("Setup Failed!", e);
    }
  }

  /*
   * @testName: test1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:684; PERSISTENCE:SPEC:842;
   * PERSISTENCE:SPEC:850; PERSISTENCE:SPEC:852; PERSISTENCE:SPEC:853;
   * PERSISTENCE:SPEC:859; PERSISTENCE:SPEC:879; PERSISTENCE:SPEC:880;
   * PERSISTENCE:SPEC:885; PERSISTENCE:JAVADOC:58; PERSISTENCE:SPEC:1024
   * 
   * @test_Strategy: When an application-managed entity manager is used, the
   * application interacts directly with the persistence provider's entity
   * manager factory to manage the entity manager life cycle and to obtain and
   * destroy persistence contexts. All such application-managed pcs are extended
   * in scope and may span multiple transactions.
   *
   * Inject entity manager factory, but open and close each entity manager
   * within the business method.
   */

  public void test1() throws Fault {

    TestUtil.logTrace("Begin test1");
    boolean pass = false;

    try {

      statelessBean.init(props);
      statelessBean.doCleanup();
      pass = statelessBean.test1();
      statelessBean.doCleanup();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);
    }

    if (!pass)
      throw new Fault("test1 failed");
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:880
   * 
   * @test_Strategy: When an application-managed entity manager is used, the
   * application interacts directly with the persistence provider's entity
   * manager factory to manage the entity manager life cycle and to obtain and
   * destroy persistence contexts. All such application-managed pcs are extended
   * in scope and may span multiple transactions.
   *
   * Inject entity manager factory, but open and close each entity manager
   * within the business method.
   *
   */

  public void test2() throws Fault {

    TestUtil.logTrace("Begin test2");
    boolean pass = false;

    try {

      statelessBean.init(props);
      statelessBean.doCleanup();
      pass = statelessBean.test2();
      statelessBean.doCleanup();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);
    }

    if (!pass)
      throw new Fault("test2 failed");
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: PERSISTENCE:SPEC:692; PERSISTENCE:JAVADOC:140;
   * PERSISTENCE:JAVADOC:52
   * 
   * @test_Strategy: The persistence providers implementation of the merge
   * operation must examine the version attribute when an entity is being merged
   * and throw an OptimisticLockException if is discovered that the object being
   * merged is a stale copy of the entity.
   *
   */

  public void test3() throws Fault {

    TestUtil.logTrace("Begin test3");
    boolean pass = false;

    try {
      statefulBean.init(props);
      pass = statefulBean.test3();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);
    }

    if (!pass)
      throw new Fault("test3 failed");
  }

  /*
   * @testName: test4
   * 
   * @assertion_ids: PERSISTENCE:SPEC:886; PERSISTENCE:SPEC:881
   * 
   * @test_Strategy: When a JTA application-managed entity manager is used, if
   * the entity manager is created outside the scope of a current JTA
   * transaction, it is the responsibility of the application to associate the
   * entity manager with the transaction (if desired) by calling
   * EntityManager.joinTransaction.
   *
   * The enitity manager factory is injected into the stateful session bean. The
   * entity manager is obtained in the PostConstruct method of bean and closed
   * with when the bean is removed by a business method annotated with the
   * Remove annotation.
   *
   */

  public void test4() throws Fault {

    TestUtil.logTrace("Begin test4");
    boolean pass = false;

    try {
      statefulBean.init(props);
      pass = statefulBean.test4();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);
    }

    if (!pass)
      throw new Fault("test4 failed");
  }

  /*
   * @testName: test5
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:164; PERSISTENCE:SPEC:2420;
   * 
   * @test_Strategy: Test the @PersistenceUnits and verify that a managed entity
   * from one PU is not accessible in the other PU and visa versa.
   */

  public void test5() throws Fault {

    boolean pass = false;

    try {
      statefulBean2.init(props);
      pass = statefulBean2.test5();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);
    }

    if (!pass)
      throw new Fault("test5 failed");
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup complete");
  }

}
