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

package com.sun.ts.tests.ejb30.bb.session.stateless.cm.allowed;

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

  // protected SessionContextAllowedIF lookupSessionContextAllowedBean() throws
  // javax.naming.NamingException {
  // return sessionContextAllowedBean;
  // }
  //
  // protected CallbackAllowedIF lookupCallbackAllowedBean() throws
  // javax.naming.NamingException {
  // return callbackAllowedBean;
  // }
  //
  // protected AllowedIF lookupAllowedBean() throws javax.naming.NamingException
  // {
  // return allowedBean;
  // }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props:
   */

  /////////////////////////////////////////////////////////////////////////

  /*
   * @testName: postConstructTest
   * 
   * @assertion_ids: EJB:SPEC:108; EJB:SPEC:108.1; EJB:SPEC:108.2;
   * EJB:SPEC:108.3; EJB:SPEC:108.4; EJB:SPEC:108.5; EJB:SPEC:108.6;
   * EJB:SPEC:108.7; EJB:SPEC:108.10; EJB:SPEC:108.11; EJB:SPEC:108.12;
   * EJB:SPEC:108.13; EJB:SPEC:108.14; EJB:SPEC:108.15; EJB:SPEC:106;
   * EJB:SPEC:107; EJB:JAVADOC:36; EJB:JAVADOC:40; EJB:JAVADOC:44;
   * EJB:JAVADOC:48; EJB:JAVADOC:51; EJB:JAVADOC:86; EJB:JAVADOC:102;
   * EJB:JAVADOC:72
   *
   * @test_Strategy: Operations allowed and not allowed in the ejbCreate method
   * of a stateless session bean with container-managed transaction demarcation
   * are: Deploy it on the J2EE server. Verify correct operations.
   *
   */

  public void postConstructTest() throws Fault {
    Properties results = null;
    Properties expected = new Properties();
    expected.setProperty(getEJBHome, disallowed);
    expected.setProperty(getCallerPrincipal, disallowed);
    expected.setProperty(isCallerInRole, disallowed);
    expected.setProperty(getEJBObject, disallowed);
    expected.setProperty(JNDI_Access, allowed);
    // expected.setProperty(EJBContext_lookup, allowed);
    expected.setProperty(UserTransaction, disallowed);
    expected.setProperty(UserTransaction_Methods_Test1, disallowed);
    expected.setProperty(UserTransaction_Methods_Test2, disallowed);
    expected.setProperty(UserTransaction_Methods_Test3, disallowed);
    expected.setProperty(UserTransaction_Methods_Test4, disallowed);
    expected.setProperty(UserTransaction_Methods_Test5, disallowed);
    expected.setProperty(UserTransaction_Methods_Test6, disallowed);
    expected.setProperty(getEJBLocalHome, disallowed);
    expected.setProperty(getEJBLocalObject, disallowed);
    expected.setProperty(getTimerService, allowed);
    expected.setProperty(TimerService_Methods_Test1, disallowed);
    expected.setProperty(TimerService_Methods_Test2, disallowed);
    expected.setProperty(TimerService_Methods_Test3, disallowed);
    expected.setProperty(TimerService_Methods_Test4, disallowed);
    expected.setProperty(TimerService_Methods_Test5, disallowed);
    expected.setProperty(TimerService_Methods_Test6, disallowed);
    expected.setProperty(TimerService_Methods_Test7, disallowed);
    expected.setProperty(getMessageContext, disallowed);
    expected.setProperty(getRollbackOnly, disallowed);
    expected.setProperty(setRollbackOnly, disallowed);
    expected.setProperty(getBusinessObject, allowed);

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
   * @assertion_ids: EJB:SPEC:109; EJB:SPEC:109.1; EJB:SPEC:109.2;
   * EJB:SPEC:109.3; EJB:SPEC:109.4; EJB:SPEC:109.5; EJB:SPEC:109.6;
   * EJB:SPEC:109.7; EJB:SPEC:109.10; EJB:SPEC:109.11; EJB:SPEC:109.12;
   * EJB:SPEC:109.13; EJB:SPEC:109.14; EJB:SPEC: 109.15; EJB:JAVADOC:72;
   * EJB:JAVADOC:73; EJB:JAVADOC:36; EJB:JAVADOC:40; EJB:JAVADOC:44;
   * EJB:JAVADOC:48; EJB:JAVADOC:51; EJB:JAVADOC:86; EJB:JAVADOC:102
   *
   * @test_Strategy: Operations allowed and not allowed in the setSessionContext
   * method of a stateless session bean with container-managed transaction
   * demarcation are: Create a stateless Session Bean. Deploy it on the J2EE
   * server. Verify correct operations.
   */

  public void setSessionContextTest() throws Fault {
    Properties results = null;
    Properties expected = new Properties();
    expected.setProperty(getEJBHome, disallowed);
    expected.setProperty(getCallerPrincipal, disallowed);
    expected.setProperty(isCallerInRole, disallowed);
    expected.setProperty(getEJBObject, disallowed);
    expected.setProperty(JNDI_Access, allowed);
    // expected.setProperty(EJBContext_lookup, allowed);
    expected.setProperty(UserTransaction, disallowed);
    expected.setProperty(UserTransaction_Methods_Test1, disallowed);
    expected.setProperty(UserTransaction_Methods_Test2, disallowed);
    expected.setProperty(UserTransaction_Methods_Test3, disallowed);
    expected.setProperty(UserTransaction_Methods_Test4, disallowed);
    expected.setProperty(UserTransaction_Methods_Test5, disallowed);
    expected.setProperty(UserTransaction_Methods_Test6, disallowed);
    expected.setProperty(getEJBLocalHome, disallowed);
    expected.setProperty(getEJBLocalObject, disallowed);
    expected.setProperty(getTimerService, disallowed);
    expected.setProperty(TimerService_Methods_Test1, disallowed);
    expected.setProperty(TimerService_Methods_Test2, disallowed);
    expected.setProperty(TimerService_Methods_Test3, disallowed);
    expected.setProperty(TimerService_Methods_Test4, disallowed);
    expected.setProperty(TimerService_Methods_Test5, disallowed);
    expected.setProperty(TimerService_Methods_Test6, disallowed);
    expected.setProperty(TimerService_Methods_Test7, disallowed);
    expected.setProperty(getMessageContext, disallowed);
    expected.setProperty(getRollbackOnly, disallowed);
    expected.setProperty(setRollbackOnly, disallowed);

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
   * @assertion_ids: EJB:SPEC:110; EJB:SPEC:110.1; EJB:SPEC:110.2;
   * EJB:SPEC:110.3; EJB:SPEC:110.4; EJB:SPEC:110.5; EJB:SPEC:110.6;
   * EJB:SPEC:110.7; EJB:SPEC:110.10; EJB:SPEC:110.11; EJB:SPEC:110.12;
   * EJB:SPEC:110.13; EJB:SPEC:110.14; EJB:SPEC:110.15; EJB:JAVADOC:72
   *
   * @test_Strategy: Operations allowed and not allowed in the business method
   * method from a component interface of a stateless session bean with
   * container- managed transaction demarcation are: Create a stateless Session
   * Bean. Deploy it on the J2EE server. Verify correct operations.
   *
   */
  public void businessTest() throws Fault {
    Properties results = null;
    Properties expected = new Properties();
    expected.setProperty(getEJBHome, disallowed);
    expected.setProperty(getCallerPrincipal, allowed);
    expected.setProperty(isCallerInRole, allowed);
    expected.setProperty(getEJBObject, disallowed);
    expected.setProperty(JNDI_Access, allowed);
    // expected.setProperty(EJBContext_lookup, allowed);
    expected.setProperty(UserTransaction, disallowed);
    expected.setProperty(UserTransaction_Methods_Test1, disallowed);
    expected.setProperty(UserTransaction_Methods_Test2, disallowed);
    expected.setProperty(UserTransaction_Methods_Test3, disallowed);
    expected.setProperty(UserTransaction_Methods_Test4, disallowed);
    expected.setProperty(UserTransaction_Methods_Test5, disallowed);
    expected.setProperty(UserTransaction_Methods_Test6, disallowed);
    expected.setProperty(getEJBLocalHome, disallowed);
    expected.setProperty(getEJBLocalObject, disallowed);
    expected.setProperty(getTimerService, allowed);
    expected.setProperty(TimerService_Methods_Test1, allowed);
    expected.setProperty(TimerService_Methods_Test2, allowed);
    expected.setProperty(TimerService_Methods_Test3, allowed);
    expected.setProperty(TimerService_Methods_Test4, allowed);
    expected.setProperty(TimerService_Methods_Test5, allowed);
    expected.setProperty(TimerService_Methods_Test6, allowed);
    expected.setProperty(TimerService_Methods_Test7, allowed);
    expected.setProperty(getMessageContext, disallowed);
    expected.setProperty(getRollbackOnly, allowed);
    expected.setProperty(setRollbackOnly, allowed);
    expected.setProperty(getBusinessObject, allowed);

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
   * @assertion_ids: EJB:SPEC:110; EJB:SPEC:110.1; EJB:SPEC:110.2;
   * EJB:SPEC:110.3; EJB:SPEC:110.4; EJB:SPEC:110.5; EJB:SPEC:110.6;
   * EJB:SPEC:110.7; EJB:SPEC:110.10; EJB:SPEC:110.11; EJB:SPEC:110.12;
   * EJB:SPEC:110.13; EJB:SPEC:110.14; EJB:SPEC:110.15; EJB:JAVADOC:72
   *
   * @test_Strategy: Operations allowed and not allowed in the business method
   * method from a component interface of a stateless session bean with
   * container- managed transaction demarcation are: Create a stateless Session
   * Bean. Deploy it on the J2EE server. Verify correct operations.
   *
   */

  public void preInvokeTest() throws Fault {
    Properties results = null;
    Properties expected = new Properties();
    expected.setProperty(getEJBHome, disallowed);
    expected.setProperty(getCallerPrincipal, allowed);
    expected.setProperty(isCallerInRole, allowed);
    expected.setProperty(getEJBObject, disallowed);
    expected.setProperty(JNDI_Access, allowed);
    // expected.setProperty(EJBContext_lookup, allowed);
    expected.setProperty(UserTransaction, disallowed);
    expected.setProperty(UserTransaction_Methods_Test1, disallowed);
    expected.setProperty(UserTransaction_Methods_Test2, disallowed);
    expected.setProperty(UserTransaction_Methods_Test3, disallowed);
    expected.setProperty(UserTransaction_Methods_Test4, disallowed);
    expected.setProperty(UserTransaction_Methods_Test5, disallowed);
    expected.setProperty(UserTransaction_Methods_Test6, disallowed);
    expected.setProperty(getEJBLocalHome, disallowed);
    expected.setProperty(getEJBLocalObject, disallowed);
    expected.setProperty(getTimerService, allowed);
    expected.setProperty(TimerService_Methods_Test1, allowed);
    expected.setProperty(TimerService_Methods_Test2, allowed);
    expected.setProperty(TimerService_Methods_Test3, allowed);
    expected.setProperty(TimerService_Methods_Test4, allowed);
    expected.setProperty(TimerService_Methods_Test5, allowed);
    expected.setProperty(TimerService_Methods_Test6, allowed);
    expected.setProperty(TimerService_Methods_Test7, allowed);
    expected.setProperty(getMessageContext, disallowed);
    expected.setProperty(getRollbackOnly, allowed);
    expected.setProperty(setRollbackOnly, allowed);
    expected.setProperty(getBusinessObject, allowed);

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
   * @assertion_ids: EJB:SPEC:110; EJB:SPEC:110.1; EJB:SPEC:110.2;
   * EJB:SPEC:110.3; EJB:SPEC:110.4; EJB:SPEC:110.5; EJB:SPEC:110.6;
   * EJB:SPEC:110.7; EJB:SPEC:110.10; EJB:SPEC:110.11; EJB:SPEC:110.12;
   * EJB:SPEC:110.13; EJB:SPEC:110.14; EJB:SPEC:110.15; EJB:JAVADOC:72
   *
   * @test_Strategy: Operations allowed and not allowed in the business method
   * method from a component interface of a stateless session bean with
   * container- managed transaction demarcation are: Create a stateless Session
   * Bean. Deploy it on the J2EE server. Verify correct operations.
   *
   */

  public void postInvokeTest() throws Fault {
    Properties results = null;
    Properties expected = new Properties();
    expected.setProperty(getEJBHome, disallowed);
    expected.setProperty(getCallerPrincipal, allowed);
    expected.setProperty(isCallerInRole, allowed);
    expected.setProperty(getEJBObject, disallowed);
    expected.setProperty(JNDI_Access, allowed);
    // expected.setProperty(EJBContext_lookup, allowed);
    expected.setProperty(UserTransaction, disallowed);
    expected.setProperty(UserTransaction_Methods_Test1, disallowed);
    expected.setProperty(UserTransaction_Methods_Test2, disallowed);
    expected.setProperty(UserTransaction_Methods_Test3, disallowed);
    expected.setProperty(UserTransaction_Methods_Test4, disallowed);
    expected.setProperty(UserTransaction_Methods_Test5, disallowed);
    expected.setProperty(UserTransaction_Methods_Test6, disallowed);
    expected.setProperty(getEJBLocalHome, disallowed);
    expected.setProperty(getEJBLocalObject, disallowed);
    expected.setProperty(getTimerService, allowed);
    expected.setProperty(TimerService_Methods_Test1, allowed);
    expected.setProperty(TimerService_Methods_Test2, allowed);
    expected.setProperty(TimerService_Methods_Test3, allowed);
    expected.setProperty(TimerService_Methods_Test4, allowed);
    expected.setProperty(TimerService_Methods_Test5, allowed);
    expected.setProperty(TimerService_Methods_Test6, allowed);
    expected.setProperty(TimerService_Methods_Test7, allowed);
    expected.setProperty(getMessageContext, disallowed);
    expected.setProperty(getRollbackOnly, allowed);
    expected.setProperty(setRollbackOnly, allowed);
    expected.setProperty(getBusinessObject, allowed);

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
   * @testName: txNotSupportedTest
   * 
   * @assertion_ids: EJB:SPEC:118; EJB:SPEC:119
   * 
   * @test_Strategy: Create a stateless Session Bean, call a business method
   * with the NotSupported transaction attribute and verify that the
   * java.lang.IllegalStateException is thrown as getRollbackOnly() or
   * setRollbackOnly() must be executed in the ` context of a transaction.
   *
   */

  public void txNotSupportedTest() throws Fault {
    try {
      allowedBean = lookupAllowedBean();
      allowedBean.txNotSupported();
    } catch (Exception e) {
      throw new Fault("txNotSupportedTest failed", e);
    }
  }

  /*
   * @testName: txSupportsTest
   * 
   * @assertion_ids: EJB:SPEC:118; EJB:SPEC:119
   * 
   * @test_Strategy: Create a stateless Session Bean, call a business method
   * with the Supports transaction attribute and verify that the
   * java.lang.IllegalStateException is thrown as getRollbackOnly() or
   * setRollbackOnly() must be executed in the context of a transaction.
   *
   */

  public void txSupportsTest() throws Fault {
    try {
      allowedBean = lookupAllowedBean();
      allowedBean.txSupports();
    } catch (Exception e) {
      throw new Fault("txSupportsTest failed", e);
    }
  }

  /*
   * @testName: txNeverTest
   * 
   * @assertion_ids: EJB:SPEC:118; EJB:SPEC:119
   * 
   * @test_Strategy: Create a stateless Session Bean, call a business method
   * with the Never transaction attribute and verify that the
   * java.lang.IllegalStateException is thrown as getRollbackOnly() or
   * setRollbackOnly() must be executed in the context of a transaction.
   *
   *
   */

  public void txNeverTest() throws Fault {
    try {
      allowedBean = lookupAllowedBean();
      allowedBean.txNever();
    } catch (Exception e) {
      throw new Fault("txNeverTest failed", e);
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
