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

package com.sun.ts.tests.jpa.ee.propagation.cm.extended;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;

import javax.ejb.EJB;
import java.util.Properties;

public class Client extends EETest {

  @EJB(name = "ejb/Stateful3Bean", beanInterface = Stateful3IF.class)
  private static Stateful3IF bean;

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
   * @assertion_ids: JavaEE:SPEC:10060; JavaEE:SPEC:10061; PERSISTENCE:SPEC:844;
   * PERSISTENCE:SPEC:845; PERSISTENCE:SPEC:847; PERSISTENCE:SPEC:858;
   * PERSISTENCE:SPEC:865; PERSISTENCE:SPEC:866; PERSISTENCE:SPEC:867;
   * PERSISTENCE:SPEC:870; PERSISTENCE:SPEC:874; PERSISTENCE:SPEC:894;
   * PERSISTENCE:SPEC:896; PERSISTENCE:SPEC:900; PERSISTENCE:SPEC:856;
   * PERSISTENCE:SPEC:1022; PERSISTENCE:SPEC:913;
   * 
   * @test_Strategy: Deploy archive, inject Container-Managed JTA Entity Manager
   * with extended persistence context and call local business method to create
   * entities. Find entity after persist.
   */

  public void test1() throws Fault {

    TestUtil.logTrace("Begin test1");
    boolean pass = false;

    try {

      bean.init(props);
      pass = bean.test1();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);
    }

    if (!pass)
      throw new Fault("test1 failed");
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:48
   * 
   * @test_Strategy: getTransaction will throw an IllegalStateException if
   * invoked on a Container-Managed JTA EM
   *
   * Invoke entityManager.getTransaction() on a Container-Managed JTA
   * entitymanager and ensure the IllegalStateException is thrown.
   *
   */

  public void test2() throws Fault {

    TestUtil.logTrace("Begin test2");
    boolean pass = false;

    try {

      bean.init(props);
      pass = bean.test2();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);
    }

    if (!pass)
      throw new Fault("test2 failed");
  }

  /*
   * @testName: test3
   * 
   * @assertion_ids: PERSISTENCE:SPEC:868; PERSISTENCE:SPEC:869;
   * PERSISTENCE:SPEC:1811;
   * 
   * @test_Strategy: With a Container Managed Entity Manager with the type
   * defined as PersistenceContextType.EXTENDED:
   *
   * If a stateful session bean instantiates a stateful session bean which also
   * has an extended persistence context, the extended persistence context of
   * the first session bean is inherited by the second stateful session bean and
   * bound to it.
   *
   * With a container-managed extended persistence context, create an entity
   * from invoking a business method from the first bean to the second bean.
   * Once created verify that the entity is identical when finding in first bean
   * or from second bean.
   */

  public void test3() throws Fault {

    TestUtil.logTrace("Begin test3");
    boolean pass = false;

    try {
      bean.init(props);
      pass = bean.test3();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);
    }

    if (!pass)
      throw new Fault("test3 failed");
  }

  /*
   * @testName: test4
   * 
   * @assertion_ids: PERSISTENCE:SPEC:868; PERSISTENCE:SPEC:869
   * 
   * @test_Strategy: With a Container Managed Entity Manager with the type
   * defined as PersistenceContextType.EXTENDED:
   *
   * If a stateful session bean instantiates a stateful session bean which also
   * has an extended persistence context, the extended persistence context of
   * the first session bean is inherited by the second stateful session bean and
   * bound to it.
   *
   * With a container-managed extended persistence context, create entities,
   * modify the data and ensure the persistence context is propagated regardless
   * of the transaction boundaries.
   *
   */

  public void test4() throws Fault {

    TestUtil.logTrace("Begin test4");
    boolean pass = false;

    try {

      bean.init(props);
      pass = bean.test4();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);
    }

    if (!pass)
      throw new Fault("test4 failed");
  }

  /*
   * @testName: test5
   * 
   * @assertion_ids: PERSISTENCE:SPEC:868; PERSISTENCE:SPEC:869
   * 
   * @test_Strategy: With a Container Managed Entity Manager with the type
   * defined as PersistenceContextType.EXTENDED:
   *
   * With a container-managed extended persistence context, create entities. In
   * another business method, set a relationship and return back to the original
   * TX. Find the entity again, and check that the relationship is set and
   * visible to the current TX.
   *
   */

  public void test5() throws Fault {

    TestUtil.logTrace("Begin test5");
    boolean pass = false;

    try {

      bean.init(props);
      pass = bean.test5();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);
    }

    if (!pass)
      throw new Fault("test5 failed");
  }

  /*
   * @testName: test6
   * 
   * @assertion_ids: PERSISTENCE:SPEC:678; PERSISTENCE:SPEC:680;
   * PERSISTENCE:SPEC:683; PERSISTENCE:SPEC:687
   * 
   * @test_Strategy: With a Container Managed Entity Manager with the type
   * defined as PersistenceContextType.EXTENDED:
   *
   * For both transaction-scoped and extended persistence contexts transaction
   * rollback causes all "pre-existing" managed instances andremoved instances
   * [these are instances that were not persistent in the database at the start
   * of the transaction] instances to become detached. The instances' state will
   * be the state of the instances at the point at which the transaction was
   * rolled back.
   *
   * With a container-managed extended persistence context, ensure the
   * appropriate behavior as defined above.
   *
   */

  public void test6() throws Fault {

    TestUtil.logTrace("Begin test6");
    boolean pass = false;
    boolean pass1 = false;

    try {

      bean.init(props);
      bean.removeTestData();
      bean.createTestData();
      pass1 = bean.test6();

      if (pass1) {
        pass = bean.verifyTest6();
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);
    }

    if (!pass)
      throw new Fault("test6 failed");
  }

  /*
   * @testName: test7
   * 
   * @assertion_ids: PERSISTENCE:JAVADOC:44
   * 
   * @test_Strategy: With a Container Managed Entity Manager with the type
   * defined as PersistenceContextType.EXTENDED:
   *
   * Within an extended persistence context, ensure flush can be invoked and the
   * changes are flushed.
   *
   *
   */

  public void test7() throws Fault {

    TestUtil.logTrace("Begin test7");
    boolean pass = false;

    try {

      bean.init(props);
      pass = bean.test7();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);
    }

    if (!pass)
      throw new Fault("test7 failed");
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup complete");
  }

}
