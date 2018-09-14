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

package com.sun.ts.tests.compat12.ejb.Tx_Single;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import javax.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String testName = "Tx_Single";

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
   * @class.setup_props: java.naming.factory.initial; generateSQL;
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

  /* Run test */

  /*
   * @testName: compat12TxTest1
   *
   * @assertion_ids: JavaEE:SPEC:283
   *
   * @test_Strategy: Container managed Tx commit - Required Entity EJBs. Create
   * a few instances of an Entity EJB (TxEBean) bean. Perform updates to the
   * Entity EJB's instance data. Ensure that the instance data is updated on
   * method return. Ensure that the database fields are updated on method
   * return.
   *
   * If the client invokes the enterprise Bean's method while the client is not
   * associated with a transaction context, the container automatically starts a
   * new transaction before delegating a method call to the enterprise Bean
   * business method. The Container automatically enlists all the resource
   * managers accessed by the business method with the transaction. If the
   * business method invokes other enterprise beans, the Container passes the
   * transaction context with the invocation. The Container attempts to commit
   * the transaction when the business method has completed. The container
   * performs the commit protocol before the method result is sent to the
   * client. (Container-managed transaction demarcation Required) The Container
   * must invoke the ejbCreate() and ejbPostCreate() methods in the transaction
   * context determined by the transaction attribute of the matching create()
   * method, as described in 11.6.2. (Section 9.1.5.2 Container's view -
   * ejbCreate) The Container must invoke this method (ejbStore()) in the same
   * transaction context as the previously invoked ejbLoad or ejbCreate method.
   * (Section 9.1.5.2 Container's view - ejbStore) The Container invokes this
   * method (ejbRemove()) in the transaction context determined by the
   * transaction attribute of the invoked remove() method. (Section 9.1.5.2
   * Container's view - ejbRemove)
   */
  public void compat12TxTest1() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean:compat12TxTest1");
      testResult = beanRef.compat12TxTest1();

      if (!testResult)
        throw new Fault("compat12TxTest1 failed");
      else
        logMsg("compat12TxTest1 passed");
    } catch (Exception e) {
      throw new Fault("compat12TxTest1 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: compat12TxTest2
   *
   * @assertion_ids: JavaEE:SPEC:283
   *
   * @test_Strategy: Container managed Tx commit - Required Entity EJBs.
   * FindByPrimaryKey an instance of an Entity EJB (TxEBean) bean. Perform
   * updates to the Entity EJB's instance data. Ensure that the instance data is
   * updated on method return. Ensure that the database fields are updated on
   * method return.
   *
   * If the client invokes the enterprise Bean's method while the client is not
   * associated with a transaction context, the container automatically starts a
   * new transaction before delegating a method call to the enterprise Bean
   * business method. The Container automatically enlists all the resource
   * managers accessed by the business method with the transaction. If the
   * business method invokes other enterprise beans, the Container passes the
   * transaction context with the invocation. The Container attempts to commit
   * the transaction when the business method has completed. The container
   * performs the commit protocol before the method result is sent to the
   * client. (Container-managed transaction demarcation Required) The Container
   * must invoke the ejbCreate() and ejbPostCreate() methods in the transaction
   * context determined by the transaction attribute of the matching create()
   * method, as described in 11.6.2. (Section 9.1.5.2 Container's view -
   * ejbCreate) The Container must invoke the ejbFind<Method>() in the
   * transaction context determined by the transaction attribute of the matching
   * find() method, as described in subsection 11.6.2. (Section 9.1.5.2
   * Container's view - ejbFind<Method>()) The Container must invoke this method
   * (ejbStore()) in the same transaction context as the previously invoked
   * ejbLoad or ejbCreate method. (Section 9.1.5.2 Container's view - ejbStore)
   * The Container invokes this method (ejbRemove()) in the transaction context
   * determined by the transaction attribute of the invoked remove() method.
   * (Section 9.1.5.2 Container's view - ejbRemove)
   */
  public void compat12TxTest2() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean:compat12TxTest2");
      testResult = beanRef.compat12TxTest2();

      if (!testResult)
        throw new Fault("compat12TxTest2 failed");
      else
        logMsg("compat12TxTest2 passed");
    } catch (Exception e) {
      throw new Fault("compat12TxTest2 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
      }
    }
  }

  /*
   * @testName: compat12TxTest4
   *
   * @assertion_ids: JavaEE:SPEC:283
   *
   * @test_Strategy: Container managed Tx - Mandatory Entity EJBs. Attempt to
   * create an instance of an Entity EJB (TxEBean) bean, without a transaction
   * context. Ensure that javax.transacton.TransactionRequiredException
   * exception is thrown.
   *
   * The Container must invoke an enterprise Bean method whose transaction
   * attribute is set to Mandatory in a client's transaction context. The client
   * is required to call with a transaction context. If the client calls without
   * a transaction context, the Container throws
   * javax.transaction.TransactionRequiredException exception.
   * (Container-managed transaction demarcation Mandatory) The Container must
   * invoke the ejbCreate() and ejbPostCreate() methods in the transaction
   * context determined by the transaction attribute of the matching create()
   * method, as described in 11.6.2. (Section 9.1.5.2 Container's view -
   * ejbCreate)
   */
  public void compat12TxTest4() throws Fault {
    try {
      logMsg("Creating EJB TestBean instance");
      beanRef = (TestBean) beanHome.create(testProps);

      boolean testResult = false;

      logMsg("Execute TestBean:compat12TxTest4");
      testResult = beanRef.compat12TxTest4();

      if (!testResult)
        throw new Fault("compat12TxTest4 failed");
      else
        logMsg("compat12TxTest4 passed");
    } catch (Exception e) {
      throw new Fault("compat12TxTestt4 failed", e);
    } finally {
      try {
        beanRef.remove();
      } catch (Exception e) {
      }
    }
  }

  /* Test cleanup: */
  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
