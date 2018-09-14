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

package com.sun.ts.tests.ejb30.bb.session.stateful.cm.allowed;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.ejb30.common.allowed.AllowedIF;
import com.sun.ts.tests.ejb30.common.allowed.CallbackAllowedIF;
import com.sun.ts.tests.ejb30.common.allowed.NoTxAllowedIF;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import java.util.Properties;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.allowed.ClientBase;
import com.sun.ts.tests.ejb30.common.allowed.Constants;

import com.sun.ts.tests.ejb30.common.allowed.SessionContextAllowedIF;
import javax.ejb.EJB;
import javax.ejb.EJBException;

public class Client extends ClientBase implements Constants {
  @EJB(name = "ejb/allowedBean", beanName = "AllowedBean")
  private static AllowedIF allowedBean;

  @EJB(name = "ejb/callbackAllowedBean", beanName = "CallbackAllowedBean")
  private static CallbackAllowedIF callbackAllowedBean;

  @EJB(name = "ejb/sessionContextAllowedBean", beanName = "SessionContextAllowedBean")
  private static SessionContextAllowedIF sessionContextAllowedBean;

  @EJB(beanName = "NoTxAllowedBean")
  private static NoTxAllowedIF noTxAllowedBean;

  @EJB(beanName = "SetRollbackOnlyBean")
  private static SetRollbackOnlyIF setRollbackOnlyBean;

  @EJB(name = "ejb/injectionAllowedBean", beanName = "InjectionAllowedBean")
  private static SessionContextAllowedIF injectionAllowedBean;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props:
   */

  public void cleanup() throws com.sun.ts.lib.harness.EETest.Fault {
    remove();
  }

  // protected NoTxAllowedIF lookupNoTxAllowedBean()
  // throws javax.naming.NamingException {
  // return (NoTxAllowedIF) ServiceLocator.lookup(
  // noTxAllowedBeanName, NoTxAllowedIF.class);
  // }
  //
  // protected SetRollbackOnlyIF lookupRollbackOnlyBean()
  // throws javax.naming.NamingException {
  // return (SetRollbackOnlyIF) ServiceLocator.lookup(
  // setRollbackOnlyBeanName, SetRollbackOnlyIF.class);
  // }

  protected void remove() {
    super.remove();
    if (noTxAllowedBean != null) {
      try {
        noTxAllowedBean.remove();
        TLogger.log("noTxAllowed removed successfully.");
      } catch (Exception e) {
        // ignore
        TLogger.log("failed to remove noTxAllowed.");
      }
    }
    if (setRollbackOnlyBean != null) {
      try {
        setRollbackOnlyBean.remove();
        TLogger.log("setRollbackOnlyBean removed successfully.");
      } catch (Exception e) {
        // ignore
        TLogger.log("failed to remove setRollbackOnlyBean.");
      }
    }
  }

  /////////////////////////////////////////////////////////////////////////

  /*
   * @testName: postConstructTest
   * 
   * @assertion_ids: EJB:SPEC:81; EJB:SPEC:81.1; EJB:SPEC:81.2; EJB:SPEC:81.3;
   * EJB:SPEC:81.4; EJB:SPEC:81.6; EJB:SPEC:81.7; EJB:SPEC:81.10;
   * EJB:SPEC:81.11; EJB:SPEC:81.12; EJB:SPEC:81.13; EJB:SPEC:81.14;
   * EJB:SPEC:81.15; EJB:JAVADOC:54
   *
   * @test_Strategy: Operations allowed and not allowed in the ejbCreate method
   * of a stateful session bean with container-managed transaction demarcation
   * are:
   *
   */

  public void postConstructTest() throws Fault {
    Properties results = null;
    Properties expected = new Properties();
    expected.setProperty(getEJBHome, disallowed);
    expected.setProperty(getCallerPrincipal, allowed);
    // expected.setProperty(getRollbackOnly, disallowed);
    expected.setProperty(isCallerInRole, allowed);
    expected.setProperty(getEJBObject, disallowed);
    expected.setProperty(JNDI_Access, allowed);

    expected.setProperty(UserTransaction, disallowed);
    expected.setProperty(UserTransaction_Methods_Test1, disallowed);
    expected.setProperty(UserTransaction_Methods_Test2, disallowed);
    expected.setProperty(UserTransaction_Methods_Test3, disallowed);
    expected.setProperty(UserTransaction_Methods_Test4, disallowed);
    expected.setProperty(UserTransaction_Methods_Test5, disallowed);
    expected.setProperty(UserTransaction_Methods_Test6, disallowed);

    expected.setProperty(getEJBLocalHome, disallowed);
    expected.setProperty(getEJBLocalObject, disallowed);

    // expected.setProperty(Timer_Methods, disallowed);
    expected.setProperty(getBusinessObject, allowed);

    // expected.setProperty(getMessageContext, disallowed);
    // expected.setProperty(setRollbackOnly, disallowed);

    try {
      callbackAllowedBean = lookupCallbackAllowedBean();
      results = callbackAllowedBean.getResults();
    } catch (Exception e) {
      throw new Fault("postConstructTest failed", e);
    }
    checkResults(results, expected);
  }

  /*
   * @testName: setSessionContextTest
   * 
   * @assertion_ids: EJB:SPEC:82; EJB:SPEC:82.1; EJB:SPEC:82.2; EJB:SPEC:82.3;
   * EJB:SPEC:82.4; EJB:SPEC:82.6; EJB:SPEC:82.7; EJB:SPEC:82.10;
   * EJB:SPEC:82.11; EJB:SPEC:82.12; EJB:SPEC:82.14; EJB:JAVADOC:54;
   * EJB:JAVADOC:73
   *
   * @test_Strategy: Operations allowed and not allowed in the setSessionContext
   * method of a stateful session bean with container-managed transaction
   * demarcation are:
   */

  public void setSessionContextTest() throws Fault {
    Properties results = null;
    Properties expected = new Properties();
    expected.setProperty(getEJBHome, disallowed);
    expected.setProperty(getCallerPrincipal, disallowed);
    // expected.setProperty(getRollbackOnly, disallowed);
    expected.setProperty(isCallerInRole, disallowed);
    expected.setProperty(getEJBObject, disallowed);
    expected.setProperty(JNDI_Access, allowed);

    expected.setProperty(UserTransaction, disallowed);
    expected.setProperty(UserTransaction_Methods_Test1, disallowed);
    expected.setProperty(UserTransaction_Methods_Test2, disallowed);
    expected.setProperty(UserTransaction_Methods_Test3, disallowed);
    expected.setProperty(UserTransaction_Methods_Test4, disallowed);
    expected.setProperty(UserTransaction_Methods_Test5, disallowed);
    expected.setProperty(UserTransaction_Methods_Test6, disallowed);

    expected.setProperty(getEJBLocalHome, disallowed);
    expected.setProperty(getEJBLocalObject, disallowed);

    expected.setProperty(Timer_Methods, disallowed);

    // expected.setProperty(getMessageContext, disallowed);
    // expected.setProperty(setRollbackOnly, disallowed);

    try {
      sessionContextAllowedBean = lookupSessionContextAllowedBean();
      results = sessionContextAllowedBean.getResults();
    } catch (Exception e) {
      throw new Fault("setSessionContextTest failed", e);
    }
    checkResults(results, expected);
  }

  /*
   * @testName: businessTest
   * 
   * @assertion_ids: EJB:SPEC:83; EJB:SPEC:83.1; EJB:SPEC:83.2; EJB:SPEC:83.3;
   * EJB:SPEC:83.4; EJB:SPEC:83.6; EJB:SPEC:83.7; EJB:SPEC:83.10;
   * EJB:SPEC:83.11; EJB:SPEC:83.12; EJB:SPEC:83.13; EJB:SPEC:83.14;
   * EJB:SPEC:83.15; EJB:JAVADOC:53; EJB:JAVADOC:89; EJB:JAVADOC:93;
   * EJB:JAVADOC:97; EJB:JAVADOC:101
   * 
   * @test_Strategy: Operations allowed and not allowed in a business method of
   * a stateful session bean with container-managed transaction demarcation are:
   *
   */
  public void businessTest() throws Fault {
    Properties results = null;
    Properties expected = new Properties();
    expected.setProperty(getEJBHome, disallowed);
    expected.setProperty(getCallerPrincipal, allowed);
    expected.setProperty(getRollbackOnly, allowed);
    expected.setProperty(isCallerInRole, allowed);
    expected.setProperty(getEJBObject, disallowed);
    expected.setProperty(JNDI_Access, allowed);

    expected.setProperty(UserTransaction, disallowed);
    expected.setProperty(UserTransaction_Methods_Test1, disallowed);
    expected.setProperty(UserTransaction_Methods_Test2, disallowed);
    expected.setProperty(UserTransaction_Methods_Test3, disallowed);
    expected.setProperty(UserTransaction_Methods_Test4, disallowed);
    expected.setProperty(UserTransaction_Methods_Test5, disallowed);
    expected.setProperty(UserTransaction_Methods_Test6, disallowed);

    expected.setProperty(getEJBLocalHome, disallowed);
    expected.setProperty(getEJBLocalObject, disallowed);

    expected.setProperty(Timer_Methods, allowed);
    expected.setProperty(getBusinessObject, allowed);

    // expected.setProperty(getMessageContext, disallowed);
    // expected.setProperty(setRollbackOnly, disallowed);

    try {
      allowedBean = lookupAllowedBean();
      results = allowedBean.business();
    } catch (Exception e) {
      throw new Fault("businessTest failed", e);
    }
    checkResults(results, expected);
  }

  /*
   * @testName: preInvokeTest
   * 
   * @assertion_ids: EJB:SPEC:83; EJB:SPEC:83.1; EJB:SPEC:83.2; EJB:SPEC:83.3;
   * EJB:SPEC:83.4; EJB:SPEC:83.6; EJB:SPEC:83.7; EJB:SPEC:83.10;
   * EJB:SPEC:83.11; EJB:SPEC:83.12; EJB:SPEC:83.13; EJB:SPEC:83.14;
   * EJB:SPEC:83.15; EJB:JAVADOC:53; EJB:JAVADOC:89; EJB:JAVADOC:93;
   * EJB:JAVADOC:97; EJB:JAVADOC:101
   * 
   * @test_Strategy: Operations allowed and not allowed in a preInvoke method of
   * a stateful session bean with container-managed transaction demarcation are:
   *
   */
  public void preInvokeTest() throws Fault {
    Properties results = null;
    Properties expected = new Properties();
    expected.setProperty(getEJBHome, disallowed);
    expected.setProperty(getCallerPrincipal, allowed);
    expected.setProperty(getRollbackOnly, allowed);
    expected.setProperty(isCallerInRole, allowed);
    expected.setProperty(getEJBObject, disallowed);
    expected.setProperty(JNDI_Access, allowed);

    expected.setProperty(UserTransaction, disallowed);
    expected.setProperty(UserTransaction_Methods_Test1, disallowed);
    expected.setProperty(UserTransaction_Methods_Test2, disallowed);
    expected.setProperty(UserTransaction_Methods_Test3, disallowed);
    expected.setProperty(UserTransaction_Methods_Test4, disallowed);
    expected.setProperty(UserTransaction_Methods_Test5, disallowed);
    expected.setProperty(UserTransaction_Methods_Test6, disallowed);

    expected.setProperty(getEJBLocalHome, disallowed);
    expected.setProperty(getEJBLocalObject, disallowed);

    expected.setProperty(Timer_Methods, allowed);
    expected.setProperty(getBusinessObject, allowed);

    // expected.setProperty(getMessageContext, disallowed);
    // expected.setProperty(setRollbackOnly, disallowed);

    try {
      allowedBean = lookupAllowedBean();
      results = allowedBean.preInvokeTest();
    } catch (Exception e) {
      throw new Fault("preInvokeTest failed", e);
    }
    checkResults(results, expected);
  }

  /*
   * @testName: postInvokeTest
   * 
   * @assertion_ids: EJB:SPEC:83; EJB:SPEC:83.1; EJB:SPEC:83.2; EJB:SPEC:83.3;
   * EJB:SPEC:83.4; EJB:SPEC:83.6; EJB:SPEC:83.7; EJB:SPEC:83.10;
   * EJB:SPEC:83.11; EJB:SPEC:83.12; EJB:SPEC:83.13; EJB:SPEC:83.14;
   * EJB:SPEC:83.15; EJB:JAVADOC:53; EJB:JAVADOC:89; EJB:JAVADOC:93;
   * EJB:JAVADOC:97; EJB:JAVADOC:101
   * 
   * @test_Strategy: Operations allowed and not allowed in a postInvoke method
   * of a stateful session bean with container-managed transaction demarcation
   * are:
   *
   */
  public void postInvokeTest() throws Fault {
    Properties results = null;
    Properties expected = new Properties();
    expected.setProperty(getEJBHome, disallowed);
    expected.setProperty(getCallerPrincipal, allowed);
    expected.setProperty(getRollbackOnly, allowed);
    expected.setProperty(isCallerInRole, allowed);
    expected.setProperty(getEJBObject, disallowed);
    expected.setProperty(JNDI_Access, allowed);

    expected.setProperty(UserTransaction, disallowed);
    expected.setProperty(UserTransaction_Methods_Test1, disallowed);
    expected.setProperty(UserTransaction_Methods_Test2, disallowed);
    expected.setProperty(UserTransaction_Methods_Test3, disallowed);
    expected.setProperty(UserTransaction_Methods_Test4, disallowed);
    expected.setProperty(UserTransaction_Methods_Test5, disallowed);
    expected.setProperty(UserTransaction_Methods_Test6, disallowed);

    expected.setProperty(getEJBLocalHome, disallowed);
    expected.setProperty(getEJBLocalObject, disallowed);

    expected.setProperty(Timer_Methods, allowed);
    expected.setProperty(getBusinessObject, allowed);

    // expected.setProperty(getMessageContext, disallowed);
    // expected.setProperty(setRollbackOnly, disallowed);

    try {
      allowedBean = lookupAllowedBean();
      allowedBean.postInvokeTest();
      results = allowedBean.getResultsPostInvoke();
    } catch (Exception e) {
      throw new Fault("postInvokeTest failed", e);
    }
    checkResults(results, expected);
  }

  /*
   * @testName: afterBeginTest
   * 
   * @assertion_ids: EJB:SPEC:83; EJB:SPEC:83.1; EJB:SPEC:83.2; EJB:SPEC:83.3;
   * EJB:SPEC:83.4; EJB:SPEC:83.6; EJB:SPEC:83.7; EJB:SPEC:83.10;
   * EJB:SPEC:83.11; EJB:SPEC:83.12; EJB:SPEC:83.13; EJB:SPEC:83.14;
   * EJB:SPEC:83.15; EJB:JAVADOC:53; EJB:JAVADOC:89; EJB:JAVADOC:93;
   * EJB:JAVADOC:97; EJB:JAVADOC:101
   * 
   * @test_Strategy: Operations allowed and not allowed in a afterBegin method
   * of a stateful session bean with container-managed transaction demarcation
   * are:
   *
   */
  public void afterBeginTest() throws Fault {
    Properties results = null;
    Properties expected = new Properties();
    expected.setProperty(getEJBHome, disallowed);
    expected.setProperty(getCallerPrincipal, allowed);
    expected.setProperty(getRollbackOnly, allowed);
    expected.setProperty(isCallerInRole, allowed);
    expected.setProperty(getEJBObject, disallowed);
    expected.setProperty(JNDI_Access, allowed);

    expected.setProperty(UserTransaction, disallowed);
    expected.setProperty(UserTransaction_Methods_Test1, disallowed);
    expected.setProperty(UserTransaction_Methods_Test2, disallowed);
    expected.setProperty(UserTransaction_Methods_Test3, disallowed);
    expected.setProperty(UserTransaction_Methods_Test4, disallowed);
    expected.setProperty(UserTransaction_Methods_Test5, disallowed);
    expected.setProperty(UserTransaction_Methods_Test6, disallowed);

    expected.setProperty(getEJBLocalHome, disallowed);
    expected.setProperty(getEJBLocalObject, disallowed);

    expected.setProperty(Timer_Methods, allowed);
    expected.setProperty(getBusinessObject, allowed);

    // expected.setProperty(getMessageContext, disallowed);
    // expected.setProperty(setRollbackOnly, disallowed);

    try {
      allowedBean = lookupAllowedBean();
      allowedBean.setTestMethod(afterBeginTest);
      results = allowedBean.afterBeginTest();
    } catch (Exception e) {
      throw new Fault("afterBeginTest failed", e);
    }
    checkResults(results, expected);
  }

  /*
   * @testName: beforeCompletionTest
   * 
   * @assertion_ids: EJB:SPEC:86; EJB:SPEC:86.1; EJB:SPEC:86.2; EJB:SPEC:86.3;
   * EJB:SPEC:86.4; EJB:SPEC:86.6; EJB:SPEC:86.7; EJB:SPEC:86.10;
   * EJB:SPEC:86.11; EJB:SPEC:86.12; EJB:SPEC:86.13; EJB:SPEC:86.14;
   * EJB:SPEC:86.15; EJB:JAVADOC:53; EJB:JAVADOC:89; EJB:JAVADOC:93;
   * EJB:JAVADOC:97; EJB:JAVADOC:101
   * 
   * @test_Strategy: Operations allowed and not allowed in the beforeCompletion
   * method of a stateful session bean with container-managed transaction
   * demarcation are:
   *
   */
  public void beforeCompletionTest() throws Fault {
    Properties results = null;
    Properties expected = new Properties();
    expected.setProperty(getEJBHome, disallowed);
    expected.setProperty(getCallerPrincipal, allowed);
    expected.setProperty(getRollbackOnly, allowed);
    expected.setProperty(isCallerInRole, allowed);
    expected.setProperty(getEJBObject, disallowed);
    expected.setProperty(JNDI_Access, allowed);

    expected.setProperty(UserTransaction, disallowed);
    expected.setProperty(UserTransaction_Methods_Test1, disallowed);
    expected.setProperty(UserTransaction_Methods_Test2, disallowed);
    expected.setProperty(UserTransaction_Methods_Test3, disallowed);
    expected.setProperty(UserTransaction_Methods_Test4, disallowed);
    expected.setProperty(UserTransaction_Methods_Test5, disallowed);
    expected.setProperty(UserTransaction_Methods_Test6, disallowed);

    expected.setProperty(getEJBLocalHome, disallowed);
    expected.setProperty(getEJBLocalObject, disallowed);

    expected.setProperty(Timer_Methods, allowed);
    expected.setProperty(getBusinessObject, allowed);

    // expected.setProperty(getMessageContext, disallowed);
    // expected.setProperty(setRollbackOnly, disallowed);

    try {
      allowedBean = lookupAllowedBean();
      allowedBean.setTestMethod(beforeCompletionTest);
      results = allowedBean.beforeCompletionTest();
      // @todo should we catch javax.transaction.TransactionRolledbackException?
      // } catch (javax.transaction.TransactionRolledbackException e) {
      //
    } catch (Exception e) {
      throw new Fault("beforeCompletionTest failed", e);
    }
    checkResults(results, expected);
  }

  /*
   * @testName: afterCompletionTest
   * 
   * @assertion_ids: EJB:SPEC:87; EJB:SPEC:87.1; EJB:SPEC:87.2; EJB:SPEC:87.3;
   * EJB:SPEC:87.4; EJB:SPEC:87.6; EJB:SPEC:87.7; EJB:SPEC:87.10;
   * EJB:SPEC:87.11; EJB:SPEC:87.12; EJB:SPEC:87.13; EJB:SPEC:87.14;
   * EJB:SPEC:87.15; EJB:JAVADOC:54
   * 
   * @test_Strategy: Operations allowed and not allowed in the afterCompletion
   * method of a stateful session bean with container-managed transaction
   * demarcation are:
   */
  public void afterCompletionTest() throws Fault {
    Properties results = null;
    Properties expected = new Properties();
    expected.setProperty(getEJBHome, disallowed);
    expected.setProperty(getCallerPrincipal, allowed);
    expected.setProperty(getRollbackOnly, disallowed);
    expected.setProperty(isCallerInRole, allowed);
    expected.setProperty(getEJBObject, disallowed);
    expected.setProperty(JNDI_Access, allowed);

    expected.setProperty(UserTransaction, disallowed);
    expected.setProperty(UserTransaction_Methods_Test1, disallowed);
    expected.setProperty(UserTransaction_Methods_Test2, disallowed);
    expected.setProperty(UserTransaction_Methods_Test3, disallowed);
    expected.setProperty(UserTransaction_Methods_Test4, disallowed);
    expected.setProperty(UserTransaction_Methods_Test5, disallowed);
    expected.setProperty(UserTransaction_Methods_Test6, disallowed);

    expected.setProperty(getEJBLocalHome, disallowed);
    expected.setProperty(getEJBLocalObject, disallowed);

    // expected.setProperty(Timer_Methods, disallowed);
    expected.setProperty(getBusinessObject, allowed);

    // expected.setProperty(getMessageContext, disallowed);
    // expected.setProperty(setRollbackOnly, disallowed);

    try {
      allowedBean = lookupAllowedBean();
      allowedBean.setTestMethod(afterCompletionTest);
      allowedBean.afterCompletionTest();
      results = allowedBean.getResultsAfterCompletion();
    } catch (Exception e) {
      throw new Fault("afterCompletionTest failed", e);
    }
    checkResults(results, expected);
  }

  /*
   * @testName: txNotSupportedTest
   * 
   * @assertion_ids: EJB:SPEC:90; EJB:SPEC:91
   * 
   * @test_Strategy: The getRollbackOnly and setRollBackOnly methods of the
   * SessionContext interface should be used only in the session bean methods
   * that execute in the context of a transaction. The container must throw the
   * java.lang.IllegalStateException if the methods are invoked while the
   * instance is not associated with a transaction. The following operations are
   * executed in a business method with transaction attribute NotSupported. o
   * getRollbackOnly - not allowed o setRollbackOnly - not allowed
   *
   *
   */
  public void txNotSupportedTest() throws Fault {
    try {
      noTxAllowedBean.txNotSupported();
    } catch (Exception e) {
      throw new Fault("txNotSupportedTest failed", e);
    }
  }

  /*
   * @testName: txSupportsTest
   * 
   * @assertion_ids: EJB:SPEC:90; EJB:SPEC:91
   * 
   * @test_Strategy: The getRollbackOnly and setRollBackOnly methods of the
   * SessionContext interface should be used only in the session bean methods
   * that execute in the context of a transaction. The container must throw the
   * java.lang.IllegalStateException if the methods are invoked while the
   * instance is not associated with a transaction. The following operations are
   * executed in a business method with transaction attribute Supports. o
   * getRollbackOnly - not allowed o setRollbackOnly - not allowed
   */
  public void txSupportsTest() throws Fault {
    try {
      noTxAllowedBean.txSupports();
    } catch (Exception e) {
      throw new Fault("txSupportsTest failed", e);
    }
  }

  /*
   * @testName: txNeverTest
   * 
   * @assertion_ids: EJB:SPEC:90; EJB:SPEC:91
   * 
   * @test_Strategy: The getRollbackOnly and setRollBackOnly methods of the
   * SessionContext interface should be used only in the session bean methods
   * that execute in the context of a transaction. The container must throw the
   * java.lang.IllegalStateException if the methods are invoked while the
   * instance is not associated with a transaction. The following operations are
   * executed in a business method with transaction attribute Never. o
   * getRollbackOnly - not allowed o setRollbackOnly - not allowed
   */
  public void txNeverTest() throws Fault {
    try {
      noTxAllowedBean.txNever();
    } catch (Exception e) {
      throw new Fault("txNeverTest failed", e);
    }
  }

  /*
   * @testName: businessSetRollbackOnlyTest
   * 
   * @assertion_ids: EJB:SPEC:83.5
   *
   * @test_Strategy: Operations allowed and not allowed in a business method of
   * a stateful session bean with container-managed transaction demarcation are:
   *
   * o setRollbackOnly - allowed
   *
   */

  public void businessSetRollbackOnlyTest() throws Fault {
    String result = null;
    String expected = allowed;
    String reason = null;
    try {
      setRollbackOnlyBean.setTestMethod(businessSetRollbackOnlyTest);
      setRollbackOnlyBean.businessSetRollbackOnlyTest();
      result = setRollbackOnlyBean.getResultFor(businessSetRollbackOnlyTest);
      if (!expected.equals(result)) {
        reason = "Expected " + expected + ", but got " + result;
        throw new Fault(reason);
      }
    } catch (Exception e) {
      reason = "Expected " + expected;
      throw new Fault(reason, e);
    }
  }

  /*
   * @testName: afterBeginSetRollbackOnlyTest
   * 
   * @assertion_ids: EJB:SPEC:83.5
   *
   * @test_Strategy: Operations allowed and not allowed in a afterBegin method
   * of a stateful session bean with container-managed transaction demarcation
   * are:
   *
   * o setRollbackOnly - allowed
   *
   */

  public void afterBeginSetRollbackOnlyTest() throws Fault {
    String result = null;
    String expected = allowed;
    String reason = null;
    try {
      setRollbackOnlyBean.setTestMethod(afterBeginSetRollbackOnlyTest);
      setRollbackOnlyBean.afterBeginSetRollbackOnlyTest();
      result = setRollbackOnlyBean.getResultFor(afterBeginSetRollbackOnlyTest);
      if (!expected.equals(result)) {
        reason = "Expected " + expected + ", but got " + result;
        throw new Fault(reason);
      }
    } catch (Exception e) {
      reason = "Expected " + expected;
      throw new Fault(reason, e);
    }
  }

  /*
   * @testName: beforeCompletionSetRollbackOnlyTest
   * 
   * @assertion_ids: EJB:SPEC:83.5
   *
   * @test_Strategy: Operations allowed and not allowed in a beforeCompletion
   * method of a stateful session bean with container-managed transaction
   * demarcation are:
   *
   * o setRollbackOnly - allowed
   *
   */

  public void beforeCompletionSetRollbackOnlyTest() throws Fault {
    String result = null;
    String expected = allowed;
    String reason = null;
    try {
      setRollbackOnlyBean.setTestMethod(beforeCompletionSetRollbackOnlyTest);
      setRollbackOnlyBean.beforeCompletionSetRollbackOnlyTest();
      result = setRollbackOnlyBean
          .getResultFor(beforeCompletionSetRollbackOnlyTest);
      if (!expected.equals(result)) {
        reason = "Expected " + expected + ", but got " + result;
        throw new Fault(reason);
      }
      // @todo should we catch javax.transaction.TransactionRolledbackException?
    } catch (EJBException e) {
      result = setRollbackOnlyBean
          .getResultFor(beforeCompletionSetRollbackOnlyTest);
      if (!expected.equals(result)) {
        reason = "Expected " + expected + ", but got " + result;
        throw new Fault(reason);
      }
    }
  }

  /*
   * @testName: afterCompletionSetRollbackOnlyTest
   * 
   * @assertion_ids: EJB:SPEC:83.5
   *
   * @test_Strategy: Operations allowed and not allowed in a afterCompletion
   * method of a stateful session bean with container-managed transaction
   * demarcation are:
   *
   * o setRollbackOnly - allowed
   *
   */

  public void afterCompletionSetRollbackOnlyTest() throws Fault {
    String result = null;
    String expected = disallowed;
    String reason = null;
    try {
      setRollbackOnlyBean.setTestMethod(afterCompletionSetRollbackOnlyTest);
      setRollbackOnlyBean.afterCompletionSetRollbackOnlyTest();
      result = setRollbackOnlyBean
          .getResultFor(afterCompletionSetRollbackOnlyTest);
      if (!expected.equals(result)) {
        reason = "Expected " + expected + ", but got " + result;
        throw new Fault(reason);
      }
    } catch (Exception e) {
      reason = "Expected " + expected;
      throw new Fault(reason, e);
    }
  }

  //////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////

  /*
   * @testName: injectionMethod
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   *
   */
}
