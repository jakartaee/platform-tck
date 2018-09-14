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

package com.sun.ts.tests.ejb30.bb.session.stateful.bm.allowed;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.ejb30.common.allowed.AllowedIF;
import com.sun.ts.tests.ejb30.common.allowed.CallbackAllowedIF;
import java.util.Properties;
import com.sun.ts.tests.ejb30.common.allowed.ClientBase;
import com.sun.ts.tests.ejb30.common.allowed.Constants;

import com.sun.ts.tests.ejb30.common.allowed.SessionContextAllowedIF;
import javax.ejb.EJB;

public class Client extends ClientBase implements Constants {
  @EJB(name = "ejb/allowedBean", beanName = "AllowedBean")
  private static AllowedIF allowedBean;

  @EJB(name = "ejb/callbackAllowedBean", beanName = "CallbackAllowedBean")
  private static CallbackAllowedIF callbackAllowedBean;

  @EJB(name = "ejb/sessionContextAllowedBean", beanName = "SessionContextAllowedBean")
  private static SessionContextAllowedIF sessionContextAllowedBean;

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

  /////////////////////////////////////////////////////////////////////////

  /*
   * @testName: postConstructTest
   * 
   * @assertion_ids: EJB:SPEC:92; EJB:SPEC:92.1; EJB:SPEC:92.2; EJB:SPEC:92.3;
   * EJB:SPEC:92.4; EJB:SPEC:92.5; EJB:SPEC:92.6; EJB:SPEC:92.7; EJB:SPEC:92.10;
   * EJB:SPEC:92.11; EJB:SPEC:92.12; EJB:SPEC:92.13; EJB:SPEC:92.14;
   * EJB:SPEC:92.15; EJB:SPEC:90; EJB:JAVADOC:183; EJB:JAVADOC:54
   *
   * @test_Strategy: Operations allowed and not allowed in the ejbCreate method
   * of a stateful session bean with bean-managed transaction demarcation are:
   */

  public void postConstructTest() throws Fault {
    Properties results = null;
    Properties expected = new Properties();
    expected.setProperty(getEJBHome, disallowed);
    expected.setProperty(getCallerPrincipal, allowed);

    expected.setProperty(getRollbackOnly, disallowed);
    expected.setProperty(setRollbackOnly, disallowed);

    expected.setProperty(isCallerInRole, allowed);
    expected.setProperty(getEJBObject, disallowed);
    expected.setProperty(JNDI_Access, allowed);

    expected.setProperty(UserTransaction, allowed);
    expected.setProperty(UserTransaction_Methods_Test1, allowed);
    expected.setProperty(UserTransaction_Methods_Test2, allowed);
    expected.setProperty(UserTransaction_Methods_Test3, allowed);
    // expected.setProperty(UserTransaction_Methods_Test4, allowed);
    // expected.setProperty(UserTransaction_Methods_Test5, allowed);
    expected.setProperty(UserTransaction_Methods_Test6, allowed);

    expected.setProperty(getEJBLocalHome, disallowed);
    expected.setProperty(getEJBLocalObject, disallowed);

    // expected.setProperty(Timer_Methods, disallowed);
    expected.setProperty(getBusinessObject, allowed);

    // expected.setProperty(getMessageContext, disallowed);

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
   * @assertion_ids: EJB:SPEC:93; EJB:SPEC:93.1; EJB:SPEC:93.2; EJB:SPEC:93.3;
   * EJB:SPEC:93.4; EJB:SPEC:93.5; EJB:SPEC:93.6; EJB:SPEC:93.7; EJB:SPEC:93.10;
   * EJB:SPEC:93.11; EJB:SPEC:93.12; EJB:SPEC:93.14; EJB:JAVADOC:54;
   * EJB:JAVADOC:73
   * 
   * @test_Strategy: Operations allowed and not allowed in the setSessionContext
   * method of a stateful session bean with bean-managed transaction demarcation
   * are:
   */

  public void setSessionContextTest() throws Fault {
    Properties results = null;
    Properties expected = new Properties();
    expected.setProperty(getEJBHome, disallowed);
    expected.setProperty(getCallerPrincipal, disallowed);

    expected.setProperty(getRollbackOnly, disallowed);
    expected.setProperty(setRollbackOnly, disallowed);

    expected.setProperty(isCallerInRole, disallowed);
    expected.setProperty(getEJBObject, disallowed);
    expected.setProperty(JNDI_Access, allowed);

    expected.setProperty(UserTransaction, disallowed);
    expected.setProperty(UserTransaction_Methods_Test1, disallowed);
    expected.setProperty(UserTransaction_Methods_Test2, disallowed);
    expected.setProperty(UserTransaction_Methods_Test3, disallowed);
    // expected.setProperty(UserTransaction_Methods_Test4, disallowed);
    // expected.setProperty(UserTransaction_Methods_Test5, disallowed);
    expected.setProperty(UserTransaction_Methods_Test6, disallowed);

    expected.setProperty(getEJBLocalHome, disallowed);
    expected.setProperty(getEJBLocalObject, disallowed);

    // @todo not tested in ejb2.1
    expected.setProperty(Timer_Methods, disallowed);

    // expected.setProperty(getMessageContext, disallowed);

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
   * @assertion_ids: EJB:SPEC:94; EJB:SPEC:94.1; EJB:SPEC:94.2; EJB:SPEC:94.3;
   * EJB:SPEC:94.4; EJB:SPEC:94.5; EJB:SPEC:94.6; EJB:SPEC:94.7; EJB:SPEC:94.10;
   * EJB:SPEC:94.11; EJB:SPEC:94.12; EJB:SPEC:94.13; EJB:SPEC:94.14;
   * EJB:SPEC:94.15; EJB:JAVADOC:53; EJB:JAVADOC:89; EJB:JAVADOC:93;
   * EJB:JAVADOC:97; EJB:JAVADOC:101
   *
   * @test_Strategy: Operations allowed and not allowed in a business method of
   * a stateful session bean with bean-managed transaction demarcation are:
   */
  public void businessTest() throws Fault {
    Properties results = null;
    Properties expected = new Properties();
    expected.setProperty(getEJBHome, disallowed);
    expected.setProperty(getCallerPrincipal, allowed);

    expected.setProperty(getRollbackOnly, disallowed);
    expected.setProperty(setRollbackOnly, disallowed);

    expected.setProperty(isCallerInRole, allowed);
    expected.setProperty(getEJBObject, disallowed);
    expected.setProperty(JNDI_Access, allowed);

    expected.setProperty(UserTransaction, allowed);
    expected.setProperty(UserTransaction_Methods_Test1, allowed);
    expected.setProperty(UserTransaction_Methods_Test2, allowed);
    expected.setProperty(UserTransaction_Methods_Test3, allowed);
    // expected.setProperty(UserTransaction_Methods_Test4, allowed);
    // expected.setProperty(UserTransaction_Methods_Test5, allowed);
    expected.setProperty(UserTransaction_Methods_Test6, allowed);

    expected.setProperty(getEJBLocalHome, disallowed);
    expected.setProperty(getEJBLocalObject, disallowed);

    expected.setProperty(Timer_Methods, allowed);
    expected.setProperty(getBusinessObject, allowed);

    // expected.setProperty(getMessageContext, disallowed);

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
   * @assertion_ids: EJB:SPEC:94; EJB:SPEC:94.1; EJB:SPEC:94.2; EJB:SPEC:94.3;
   * EJB:SPEC:94.4; EJB:SPEC:94.5; EJB:SPEC:94.6; EJB:SPEC:94.7; EJB:SPEC:94.10;
   * EJB:SPEC:94.11; EJB:SPEC:94.12; EJB:SPEC:94.13; EJB:SPEC:94.14;
   * EJB:SPEC:94.15; EJB:JAVADOC:53; EJB:JAVADOC:89; EJB:JAVADOC:93;
   * EJB:JAVADOC:97; EJB:JAVADOC:101
   *
   * @test_Strategy: Operations allowed and not allowed in a preInvoke method of
   * a stateful session bean with bean-managed transaction demarcation are:
   */
  public void preInvokeTest() throws Fault {
    Properties results = null;
    Properties expected = new Properties();
    expected.setProperty(getEJBHome, disallowed);
    expected.setProperty(getCallerPrincipal, allowed);

    expected.setProperty(getRollbackOnly, disallowed);
    expected.setProperty(setRollbackOnly, disallowed);

    expected.setProperty(isCallerInRole, allowed);
    expected.setProperty(getEJBObject, disallowed);
    expected.setProperty(JNDI_Access, allowed);

    expected.setProperty(UserTransaction, allowed);
    expected.setProperty(UserTransaction_Methods_Test1, allowed);
    expected.setProperty(UserTransaction_Methods_Test2, allowed);
    expected.setProperty(UserTransaction_Methods_Test3, allowed);
    // expected.setProperty(UserTransaction_Methods_Test4, allowed);
    // expected.setProperty(UserTransaction_Methods_Test5, allowed);
    expected.setProperty(UserTransaction_Methods_Test6, allowed);

    expected.setProperty(getEJBLocalHome, disallowed);
    expected.setProperty(getEJBLocalObject, disallowed);

    expected.setProperty(Timer_Methods, allowed);
    expected.setProperty(getBusinessObject, allowed);

    // expected.setProperty(getMessageContext, disallowed);

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
   * @assertion_ids: EJB:SPEC:94; EJB:SPEC:94.1; EJB:SPEC:94.2; EJB:SPEC:94.3;
   * EJB:SPEC:94.4; EJB:SPEC:94.5; EJB:SPEC:94.6; EJB:SPEC:94.7; EJB:SPEC:94.10;
   * EJB:SPEC:94.11; EJB:SPEC:94.12; EJB:SPEC:94.13; EJB:SPEC:94.14;
   * EJB:SPEC:94.15; EJB:JAVADOC:53; EJB:JAVADOC:89; EJB:JAVADOC:93;
   * EJB:JAVADOC:97; EJB:JAVADOC:101
   *
   * @test_Strategy: Operations allowed and not allowed in a postInvoke method
   * of a stateful session bean with bean-managed transaction demarcation are:
   */
  public void postInvokeTest() throws Fault {
    Properties results = null;
    Properties expected = new Properties();
    expected.setProperty(getEJBHome, disallowed);
    expected.setProperty(getCallerPrincipal, allowed);

    expected.setProperty(getRollbackOnly, disallowed);
    expected.setProperty(setRollbackOnly, disallowed);

    expected.setProperty(isCallerInRole, allowed);
    expected.setProperty(getEJBObject, disallowed);
    expected.setProperty(JNDI_Access, allowed);

    expected.setProperty(UserTransaction, allowed);
    expected.setProperty(UserTransaction_Methods_Test1, allowed);
    expected.setProperty(UserTransaction_Methods_Test2, allowed);
    expected.setProperty(UserTransaction_Methods_Test3, allowed);
    // expected.setProperty(UserTransaction_Methods_Test4, allowed);
    // expected.setProperty(UserTransaction_Methods_Test5, allowed);
    expected.setProperty(UserTransaction_Methods_Test6, allowed);

    expected.setProperty(getEJBLocalHome, disallowed);
    expected.setProperty(getEJBLocalObject, disallowed);

    expected.setProperty(Timer_Methods, allowed);
    expected.setProperty(getBusinessObject, allowed);

    // expected.setProperty(getMessageContext, disallowed);

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
   * @testName: utBeginTest
   * 
   * @assertion_ids: EJB:SPEC:579
   *
   * @test_Strategy: For a bean-managed stateful session bean, attempt to call
   * ut.begin() after a TX has already been started. This is disallowed so check
   * that a javax.transaction. NotSupportedException is thrown.
   */

  public void utBeginTest() throws Fault {
    boolean pass = false;
    try {
      allowedBean.utBeginTest();
    } catch (Exception e) {
      throw new Fault("utBeginTest failed", e);
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
