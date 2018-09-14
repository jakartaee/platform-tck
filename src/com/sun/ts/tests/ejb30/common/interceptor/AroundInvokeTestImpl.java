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

package com.sun.ts.tests.ejb30.common.interceptor;

import javax.interceptor.InvocationContext;
import com.sun.ts.tests.ejb30.common.calc.CalculatorException;
import com.sun.ts.tests.ejb30.common.helper.TLogger;

import java.security.Principal;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import javax.ejb.EJBContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class AroundInvokeTestImpl implements
    com.sun.ts.tests.ejb30.common.interceptor.Constants, java.io.Serializable {
  // instance fields
  /**
   * used only when this class is called from a interceptor method inside a bean
   * class. In this case, this class is just a helper class, not an interceptor
   * class. Since interceptor classes are stateless, so this field should not be
   * used when this class is used as a super class of an interceptor class.
   * ejbContext is stored in order to test InvocationContext.getEJBContext();
   */
  // InvocationContext.getEJBContext() method was removed on 1/31/2006
  // protected EJBContext ejbContext;

  /**
   * The lookup name for EJBContext. An instance of SessionContext or
   * MessageDrivenContext is injected into all AroundInvoke beans. This injected
   * resouce should have the name set to "ejbContext."
   */
  public static final String EJBCONTEXT_NAME = "java:comp/env/ejbContext";

  /**
   * same as ejbContext. Used to test InvocationContext.getMethod(), and
   * InvocationContext.getBean();
   */
  protected Object bean;

  protected Principal beanPrincipal;

  public AroundInvokeTestImpl() {
    super();
  }

  public AroundInvokeTestImpl(Object bean, java.security.Principal princ) {
    this.bean = bean;
    this.beanPrincipal = princ;
  }

  /**
   * Gets the current test name. For session beans, the test method is the same
   * as business method by convention. So InvocationContext.getMethod().
   * getName() returns the test name. For mdb, test name is embedded inside
   * message.
   */
  protected static String getTestName(InvocationContext ctx) {
    Method meth = ctx.getMethod();
    String methName = meth.getName();
    if (onMessage.equals(methName)) {
      Object[] params = ctx.getParameters();
      if (params.length == 1 && params[0] instanceof javax.jms.Message) {
        // otherwise, this is not a MessageListener.onMessage method. It
        // may just other methods unrelated to mdb.
        javax.jms.Message msg = (javax.jms.Message) params[0];
        try {
          methName = msg.getStringProperty(
              com.sun.ts.tests.ejb30.common.messaging.Constants.TEST_NAME_KEY);
        } catch (javax.jms.JMSException e) {
          throw new IllegalStateException(
              "Failed to get test name from message: " + msg);
        }
      }
    }
    return methName;
  }

  public Object intercept(InvocationContext ctx) throws CalculatorException {
    int orderInChain = 1;
    Object result = null;
    String methName = getTestName(ctx);
    if (getBeanTest.equals(methName)) {
      getBeanTest(ctx);
    } else if (getMethodTest.equals(methName)) {
      getMethodTest(ctx);
    } else if (getParametersTest.equals(methName)) {
      getParametersTest(ctx);
    } else if (getParametersEmptyTest.equals(methName)) {
      getParametersEmptyTest(ctx);
    } else if (setParametersTest.equals(methName)) {
      setParametersTest(ctx);
    } else if (getContextDataTest.equals(methName)) {
      putContextData(ctx);
    } else if (exceptionTest.equals(methName)) {
      exceptionTest(ctx);
    } else if (suppressExceptionTest.equals(methName)) {
      // nothing to do
    } else if (txRollbackOnlyTest.equals(methName)) {
      txRollbackOnlyTest(ctx);
    } else if (txRollbackOnlyAfterTest.equals(methName)) {
      // do nothing here. txRollbackOnlyAfterTest(ctx); after proceed();
    } else if (orderTest.equals(methName)) {
      checkOrder0(ctx, orderInChain);
    } else if (sameInvocationContextTest.equals(methName)) {
      checkInvocationContext0(ctx, orderInChain);
    } else if (runtimeExceptionTest.equals(methName)) {
      runtimeExceptionTest(ctx);
    } else if (runtimeExceptionAfterTest.equals(methName)) {
      // do nothing here. throw runtime exception after proceed();
    } else if (sameSecContextTest.equals(methName)) {
      sameSecContextTest(ctx);
    } else if (methodLevelInterceptorMixedTest.equals(methName)
        || methodLevelClassLevelInterceptorMixedTest.equals(methName)
        || repeatedInterceptors.equals(methName)
        || interceptorOrderingOverride.equals(methName)) {
      methodLevelInterceptorMixedTest(ctx);
    }
    try {
      result = ctx.proceed();
    } catch (CalculatorException e) {
      if (suppressExceptionTest.equals(methName)) {
        // suppress it
      } else {
        throw e;
      }
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    } finally {
      if (getContextDataTest.equals(methName)) {
        getContextDataTest(ctx);
      } else if (txRollbackOnlyAfterTest.equals(methName)) {
        txRollbackOnlyAfterTest(ctx);
      } else if (runtimeExceptionAfterTest.equals(methName)) {
        runtimeExceptionAfterTest(ctx);
        // throw new RuntimeException("From AroundInvoke method to mark tx
        // rollback only.");
      }
    }
    return result;
  }

  /**
   * A wrapper around the static version to enable polymorphism.
   */
  protected void checkOrder0(InvocationContext ctx, int orderInChain)
      throws CalculatorException {
    AroundInvokeTestImpl.checkOrder(ctx, orderInChain);
  }

  /**
   * A wrapper around the static version to enable polymorphism.
   */
  protected void checkInvocationContext0(InvocationContext ctx,
      int orderInChain) throws CalculatorException {
    AroundInvokeTestImpl.checkInvocationContext(ctx, orderInChain);
  }

  /**
   * A convenience method to be used by other interceptor classes or bean. It is
   * used by interceptor #2, which is in a interceptor class, and by interceptor
   * #3, which is inside the bean class.
   */
  public static Object intercept2(InvocationContext ctx, int orderInChain)
      throws CalculatorException {
    Object result = null;
    String methName = getTestName(ctx);

    if (orderTest.equals(methName)) {
      AroundInvokeTestImpl.checkOrder(ctx, orderInChain);
    } else if (sameInvocationContextTest.equals(methName)) {
      AroundInvokeTestImpl.checkInvocationContext(ctx, orderInChain);
    }
    try {
      result = ctx.proceed();
    } catch (CalculatorException e) {
      throw e;
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
    return result;
  }

  public static void checkInvocationContext(InvocationContext ctx,
      int orderInChain) throws CalculatorException {
    Map<String, Object> map = ctx.getContextData();
    Object obj = map.get(SAME_INVOCATIONCONTEXT_KEY);
    if (obj == null) {
      map.put(SAME_INVOCATIONCONTEXT_KEY, ctx);
    } else if (obj == ctx) {
      TLogger.log(
          "same instance of InvocationContext is used in this interceptor.");
    } else {
      throw new CalculatorException(
          "The following InvocationContext are expected to be the same instance "
              + ctx + ", " + obj);
    }
  }

  /**
   * Checks order of current interceptor in chain. interceptors extends or
   * delegates to this class should be the first interceptor in chain.
   * Otherwise, override this method.
   * 
   * @param num
   *          the oder of current interceptor. num must be 1 or greater.
   */
  public static void checkOrder(InvocationContext ctx, int num)
      throws CalculatorException {
    Map<String, Object> map = ctx.getContextData();
    Object obj = map.get(INTERCEPTOR_ORDER_KEY);
    if (num == 1) {
      if (obj == null) {
        // it should be null since this is the first interceptor in chain
        Integer order = new Integer(1);
        map.put(INTERCEPTOR_ORDER_KEY, order);
        TLogger.log("order of current interceptor: " + 1);
      } else {
        throw new CalculatorException("Expected null for key "
            + INTERCEPTOR_ORDER_KEY + ", actual " + obj);
      }
    } else if (num > 1) {
      if (obj == null) {
        throw new CalculatorException(
            "Illegal state of InvocationContext: keyed data is null.");
      } else {
        int order = (Integer) obj; // order of previous interceptor.
        if (order == num - 1) {
          int currentOrder = incrementOrder(map);
          TLogger.log("order of current interceptor: " + currentOrder);
        } else {
          throw new CalculatorException(
              "Expected order of previous interceptor " + (num - 1)
                  + ", actual " + order);
        }
      }
    }
  }

  /**
   * the keyed data should not be null when calling this method.
   */
  public static int incrementOrder(Map<String, Object> map) {
    Object obj = map.get(INTERCEPTOR_ORDER_KEY);
    int order = ((Integer) obj).intValue();
    Integer newOrder = new Integer(order + 1);
    map.put(INTERCEPTOR_ORDER_KEY, newOrder);
    return order + 1;
  }

  protected void sameSecContextTest(InvocationContext ctx)
      throws CalculatorException {
    EJBContext ejbContext = getEJBContext();
    java.security.Principal princ = ejbContext.getCallerPrincipal();
    if (princ == null) {
      throw new CalculatorException(
          "In AroundInvokeTestImpl, caller principal is null.");
    }
    if (this.bean != null) {
      // it means, the current interceptor is inside the bean class
      // beanPrincipal should have been initialized in ctor.
      // Note that EJBContext, getCallPrincipal() never returns null.
      if (this.beanPrincipal != null) {
        if (!princ.equals(beanPrincipal)) {
          throw new CalculatorException(
              "Principal in interceptor is not the same as in bean.");
        } else {
          // this.bean is not null, and beanPrincipal is not null,
          // and they are the same.
          TLogger.log(
              "sameSecContextTest passed: interceptor has the same Principal as bean method.");
        }
      } else {
        throw new CalculatorException(
            "In AroundInvokeTestImpl, bean is not null, but beanPrincipal is null.");
      }
    } else {
      // this.bean is null
      // it means this interceptor is a separate interceptor class.
      // we pass thru in this case.
    }
  }

  protected void txRollbackOnlyTest(InvocationContext ctx)
      throws CalculatorException {
    EJBContext con = getEJBContext();
    con.setRollbackOnly();
  }

  protected void txRollbackOnlyAfterTest(InvocationContext ctx)
      throws CalculatorException {
    EJBContext con = getEJBContext();
    con.setRollbackOnly();
    if (!con.getRollbackOnly()) {
      throw new CalculatorException(
          "tx was set rollback only in interceptor, but subsequent EJBContext.getRollbackOnly() returns false.");
    }
  }

  protected void runtimeExceptionTest(InvocationContext ctx)
      throws CalculatorException {
    throw new RuntimeException(
        "From AroundInvoke method to mark tx rollback only.");
  }

  protected void runtimeExceptionAfterTest(InvocationContext ctx)
      throws CalculatorException {
    throw new RuntimeException(
        "From AroundInvoke method to mark tx rollback only.");
  }

  protected void exceptionTest(InvocationContext ctx)
      throws CalculatorException {
    TLogger.log("about to throw CalculatorException");
    throw new CalculatorException(
        "Expected CalculatorException from exceptionTest");
  }

  protected void suppressExceptionTest(InvocationContext ctx)
      throws CalculatorException {
    // nothing to do
  }

  protected void getBeanTest(InvocationContext ctx) throws CalculatorException {
    // which one is required by spec, == or equals?
    Object bean = ctx.getTarget();
    if (this.bean == null) {
      // it has not been initialized, if the current interceptor is interceptor
      // class
      // in this case, we just check a non-null is returned.
      if (bean == null) {
        throw new CalculatorException(
            "InvocationContext.getBean() returned null.");
      } else {
        return;
      }
    }

    if (this.bean != bean) {
      throw new CalculatorException(
          "Expected " + this.bean + ", actual " + bean);
    } else {
      TLogger.log("getBeanTest passed");
    }
  }

  protected void getParametersTest(InvocationContext ctx)
      throws CalculatorException {
    Object[] params = ctx.getParameters();
    // we know the corresponding business method has one string param
    String errorMsg = null;
    if (params == null) {
      errorMsg = ", actual null";
    } else if (params.length != 1) {
      errorMsg = ", actual " + java.util.Arrays.asList(params).toString();
    } else if (!OLD_PARAM_VALUE.equals(params[0])) {
      errorMsg = ", actual " + params[0];
    }
    if (errorMsg != null) {
      throw new CalculatorException("Expected " + OLD_PARAM_VALUE + errorMsg);
    }
  }

  protected void getParametersEmptyTest(InvocationContext ctx)
      throws CalculatorException {
    Object[] params = ctx.getParameters();
    if (params == null || params.length == 0) {
      // passed
    } else {
      throw new CalculatorException("Expected null or empty array, actual "
          + Arrays.asList(params).toString());
    }
  }

  protected void setParametersTest(InvocationContext ctx)
      throws CalculatorException {
    Object[] params = new Object[] { NEW_PARAM_VALUE };
    ctx.setParameters(params);
  }

  protected void putContextData(InvocationContext ctx)
      throws CalculatorException {
    Map<String, Object> map = ctx.getContextData();
    map.put(INTERCEPTOR_MSG_KEY, INTERCEPTOR_MSG);
    String logmsg = "put into InvocationContext data: " + INTERCEPTOR_MSG_KEY
        + "=" + INTERCEPTOR_MSG;
    TLogger.log(logmsg);
  }

  protected void getContextDataTest(InvocationContext ctx)
      throws CalculatorException {
    Map<String, Object> map = ctx.getContextData();
    String msg = (String) map.get(INTERCEPTOR_MSG_KEY);
    if (!INTERCEPTOR_MSG.equals(msg)) {
      throw new CalculatorException("Expected InvocationContext data for key "
          + INTERCEPTOR_MSG_KEY + ": " + INTERCEPTOR_MSG + ", actual " + msg);
    }
    String passed = "getContextDataTest for InvocationContext passed.";
    TLogger.log(passed);
  }

  protected void getMethodTest(InvocationContext ctx)
      throws CalculatorException {
    // we know for this bean the client calls INTERCEPTED_METHOD
    Method meth = ctx.getMethod();

    // it has not been initialized, if the current interceptor is interceptor
    // class
    // in this case, we just check a non-null is returned.
    if (this.bean == null) {
      if (meth == null) {
        throw new CalculatorException(
            "InvocationContext.getMethod() returned null.");
      } else {
        return;
      }
    }

    Method expectedMeth = null;
    try {
      expectedMeth = this.bean.getClass().getMethod(getMethodTest,
          (Class[]) null);
    } catch (NoSuchMethodException e) {
      throw new CalculatorException(
          "Failed to get method " + getMethodTest + " from bean " + this.bean,
          e);
    }
    if (!meth.equals(expectedMeth)) {
      throw new CalculatorException(
          "Expected " + expectedMeth + ", actual " + meth);
    }
    String passed = "getMethodTest for InvocationContext passed.";
    TLogger.log(passed);
  }

  protected static EJBContext getEJBContext() {
    EJBContext ec = null;
    try {
      InitialContext ic = new InitialContext();
      Object ob = ic.lookup(EJBCONTEXT_NAME);
      ec = (EJBContext) ob;
    } catch (NamingException e) {
      throw new IllegalStateException(
          "Make sure SessionContext or MessageDrivenContext with name "
              + EJBCONTEXT_NAME + " is injected into AroundInvoke bean.",
          e);
    }
    return ec;
  }

  protected void methodLevelInterceptorMixedTest(InvocationContext ctx)
      throws CalculatorException {
    Object[] params = ctx.getParameters();
    if (params == null || params.length == 0) {
      throw new CalculatorException(
          "Expecting method params to have 1 ArrayList<String> element, but actual "
              + "is null or empty: " + params);
    }
    ArrayList<String> alist = (ArrayList<String>) params[0];
    // add the short class name of the current interceptor. For example,
    // Interceptor1
    String fullClassName = getClass().getName();
    alist.add(fullClassName.substring(fullClassName.lastIndexOf(".") + 1));
  }
}
