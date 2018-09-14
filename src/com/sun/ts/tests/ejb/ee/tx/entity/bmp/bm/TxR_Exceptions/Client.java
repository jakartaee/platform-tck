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

package com.sun.ts.tests.ejb.ee.tx.entity.bmp.bm.TxR_Exceptions;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String testName = "TxR_Exceptions";

  private static final String testLookup = "java:comp/env/ejb/TestBean";

  private static final String envProps = "testbean.props";

  private static final String testDir = System.getProperty("user.dir");

  private TestBeanHome beanHome = null;

  private TestBean beanRef = null;

  private Properties testProps = new Properties();

  private TSNamingContext jctx = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */

  /*
   * @class.setup_props: java.naming.factory.initial;
   *
   * @class.testArgs: -ap tssql.stmt
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    logMsg("Setup tests");
    this.testProps = p;

    try {
      logMsg("Get the naming context");
      jctx = new TSNamingContext();

      logMsg("Getting the EJB Home interface for " + testLookup);
      beanHome = (TestBeanHome) jctx.lookup(testLookup, TestBeanHome.class);

      logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: test1
   *
   * @assertion_ids: EJB:SPEC:628.1
   *
   * @test_Strategy: AppException from Entity EJB. - Create an instance of a
   * stateful session Testbean bean. - Create an instance of an Entity
   * TxEBean(Required) bean. - Cause an AppException in the Entity bean. -
   * Ensure that the client receives AppException.
   */

  public void test1() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean:test1");
      testResult = beanRef.test1();

      if (!testResult)
        throw new Fault("test1 failed");
      else
        logMsg("test1 passed");
    } catch (Exception e) {
      throw new Fault("test1 failed", e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing bean: " + e.getMessage(), e);
      }
    }
  }

  /*
   * @testName: test2
   *
   * @assertion_ids: EJB:SPEC:629.2; EJB:SPEC:629.4
   *
   * @test_Strategy: SysException from Entity EJB. - Create an instance of a
   * stateful session Testbean bean. - Create an instance of an Entity
   * TxEBean(Required) bean. - Cause a SysException in the Entity bean. - Ensure
   * that the client receives TransactionRolledbackException. - Check that
   * transaction was marked for rollback. - Verify the transaction did not
   * commit.
   */

  public void test2() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean:test2");
      testResult = beanRef.test2();

      if (!testResult)
        throw new Fault("test2 failed");
      else
        logMsg("test2 passed");
    } catch (Exception e) {
      throw new Fault("test2 failed", e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing bean: " + e.getMessage(), e);
      }
    }
  }

  /*
   * @testName: test3
   *
   * @assertion_ids: EJB:SPEC:10619; EJB:SPEC:628.1
   *
   * @test_Strategy: CreateException from Entity EJB. - Create 1 instance of a
   * stateful session Testbean beans. - Create 3 instances of an Entity
   * TxEBean(Required) bean. - Cause a CreateException in the Entity bean. -
   * Ensure that the client receives CreateException.
   */

  public void test3() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean:test3");
      testResult = beanRef.test3();

      if (!testResult)
        throw new Fault("test3 failed");
      else
        logMsg("test3 passed");
    } catch (Exception e) {
      throw new Fault("test3 failed", e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing bean: " + e.getMessage(), e);
      }
    }
  }

  /*
   * @testName: test5
   *
   * @assertion_ids: EJB:SPEC:10619; EJB:JAVADOC:118
   *
   * @test_Strategy: FinderException from Entity EJB. - Create 1 instance of a
   * stateful session Testbean beans. - Create 2 instances of an Entity
   * TxEBean(Required) bean. - Delete the second instance. - Try to find the
   * second instance. - Catch FinderException
   */

  public void test5() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean:test5");
      testResult = beanRef.test5();

      if (!testResult)
        throw new Fault("test5 failed");
      else
        logMsg("test5 passed");
    } catch (Exception e) {
      throw new Fault("test5 failed", e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing bean: " + e.getMessage(), e);
      }
    }
  }

  /*
   * @testName: test6
   *
   * @assertion_ids: EJB:SPEC:10619; EJB:JAVADOC:143
   *
   * @test_Strategy: ObjectNotFoundException from Entity EJB. - Create 1
   * instance of a stateful session Testbean beans. - Create 1 instance of an
   * Entity TxEBean(Required) bean, creating a fresh database table. - Use
   * findByPrimaryKey(...) to find a record with a key that does not exist. Note
   * that the programmer knows all the keys that exist (only "1") since it is a
   * newly created table. - Catch ObjectNotFoundException
   */

  public void test6() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean:test6");
      testResult = beanRef.test6();

      if (!testResult)
        throw new Fault("test6 failed");
      else
        logMsg("test6 passed");
    } catch (Exception e) {
      throw new Fault("test6 failed", e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing bean: " + e.getMessage(), e);
      }
    }
  }

  /*
   * @testName: test7
   *
   * @assertion_ids: EJB:SPEC:10619
   *
   * @test_Strategy: RemoveException from Entity EJB. - Create 1 instance of an
   * Entity TxEBean(Required) bean. - Force a RemoveException. - Catch
   * RemoveException.
   */

  public void test7() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean:test7");
      testResult = beanRef.test7();

      if (!testResult)
        throw new Fault("test7 failed");
      else
        logMsg("test7 passed");
    } catch (Exception e) {
      throw new Fault("test7 failed", e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing bean: " + e.getMessage(), e);
      }
    }
  }

  /*
   * @testName: test8
   *
   * @assertion_ids: EJB:SPEC:629.2; EJB:SPEC:629.4
   *
   * @test_Strategy: EJBException from Entity EJB. - Create an instance of a
   * stateful session Testbean bean. - Create an instance of an Entity
   * TxEBean(Required) bean. - Cause a EJBException in the Entity bean. - Ensure
   * that the client receives TransactionRolledbackException. - Check that the
   * client transaction(t1) is still active. - Verify the transaction did not
   * commit.
   */

  public void test8() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean:test8");
      testResult = beanRef.test8();

      if (!testResult)
        throw new Fault("test8 failed");
      else
        logMsg("test8 passed");
    } catch (Exception e) {
      throw new Fault("test8 failed", e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing bean: " + e.getMessage(), e);
      }
    }
  }

  /*
   * @testName: test9
   *
   * @assertion_ids: EJB:SPEC:629.2; EJB:SPEC:629.4
   *
   * @test_Strategy: Error from Entity EJB. - Create an instance of a stateful
   * session Testbean bean. - Create an instance of an Entity TxEBean(Required)
   * bean. - Cause a Error in the Entity bean. - Ensure that the client receives
   * TransactionRolledbackException. - Check that the client transaction(t1) is
   * still active. - Verify the transaction did not commit.
   */

  public void test9() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean:test9");
      testResult = beanRef.test9();

      if (!testResult)
        throw new Fault("test9 failed");
      else
        logMsg("test9 passed");
    } catch (Exception e) {
      throw new Fault("test9 failed", e);
    } finally {
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing bean: " + e.getMessage(), e);
      }
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
