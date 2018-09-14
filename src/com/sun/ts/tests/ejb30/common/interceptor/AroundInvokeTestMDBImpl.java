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

import java.lang.reflect.Method;
import java.util.Map;

import javax.ejb.EJBContext;
import javax.interceptor.InvocationContext;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;

import com.sun.ts.tests.ejb30.common.calc.CalculatorException;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.messaging.StatusReporter;

public class AroundInvokeTestMDBImpl extends AroundInvokeTestImpl
    implements java.io.Serializable {
  // @Resource(name="qFactory")
  // private QueueConnectionFactory qFactory;
  //
  // @Resource(name="replyQueue")
  // private Queue replyQueue;
  //
  static protected QueueConnectionFactory getQueueConnectionFactory(
      InvocationContext inv) {
    EJBContext ejbContext = getEJBContext();
    return getQueueConnectionFactory(ejbContext);
  }

  static protected Queue getQueue(InvocationContext inv) {
    EJBContext ejbContext = getEJBContext();
    return getQueue(ejbContext);
  }

  static protected QueueConnectionFactory getQueueConnectionFactory(
      EJBContext ejbContext) {
    Object obj = ejbContext.lookup("qFactory");
    return (QueueConnectionFactory) obj;
  }

  static protected Queue getQueue(EJBContext ejbContext) {
    Object obj = ejbContext.lookup("replyQueue");
    return (Queue) obj;
  }

  public AroundInvokeTestMDBImpl() {
    super();
  }

  public AroundInvokeTestMDBImpl(Object bean, java.security.Principal princ) {
    this.bean = bean;
    this.beanPrincipal = princ;
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
      AroundInvokeTestMDBImpl.checkOrder(ctx, orderInChain);
    } else if (sameInvocationContextTest.equals(methName)) {
      AroundInvokeTestMDBImpl.checkInvocationContext(ctx, orderInChain);
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
      int orderInChain) {
    String testname = sameInvocationContextTest;
    boolean status = true;
    String reason = null;
    Map<String, Object> map = ctx.getContextData();
    Object obj = map.get(SAME_INVOCATIONCONTEXT_KEY);
    if (obj == null) {// interceptor #1
      map.put(SAME_INVOCATIONCONTEXT_KEY, ctx);
      TLogger.log("In interceptor " + orderInChain
          + ", an instance of InvocationContext is saved inside context data.");
    } else if (obj == ctx) {
      TLogger.log("In interceptor " + orderInChain
          + ", same instance of InvocationContext is found, and continue to the next interceptor...");
      if (orderInChain == NUM_OF_INTERCEPTORS) {
        status = true;
        reason = "same instance of InvocationContext is used in this interceptor.";
        TLogger.log(reason);
        StatusReporter.report(testname, status, reason,
            getQueueConnectionFactory(ctx), getQueue(ctx));
      }
    } else {
      status = false;
      reason = "The following InvocationContext are expected to be the same instance, will not go to the next interceptor:"
          + ctx + ", " + obj;
      TLogger.log(reason);
      StatusReporter.report(testname, status, reason,
          getQueueConnectionFactory(ctx), getQueue(ctx));
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
  public static void checkOrder(InvocationContext ctx, int num) {
    String testname = orderTest;
    boolean status = true;
    String reason = null;
    Map<String, Object> map = ctx.getContextData();
    Object obj = map.get(INTERCEPTOR_ORDER_KEY);
    if (num == 1) {
      if (obj == null) {
        // it should be null since this is the first interceptor in
        // chain
        Integer order = new Integer(1);
        map.put(INTERCEPTOR_ORDER_KEY, order);
        TLogger.log("order of current interceptor: " + 1);
      } else {
        status = false;
        reason = "Expected null for key " + INTERCEPTOR_ORDER_KEY + ", actual "
            + obj;
        TLogger.log(reason);
        StatusReporter.report(testname, status, reason,
            getQueueConnectionFactory(ctx), getQueue(ctx));
        return;
      }
    } else if (num > 1) {
      if (obj == null) {
        status = false;
        reason = "Illegal state of InvocationContext: keyed data is null.";
        TLogger.log(reason);
        StatusReporter.report(testname, status, reason,
            getQueueConnectionFactory(ctx), getQueue(ctx));
        return;
      } else {
        int order = (Integer) obj; // order of previous interceptor.
        if (order == num - 1) {
          int currentOrder = incrementOrder(map);
          TLogger.log("order of current interceptor: " + currentOrder);
        } else {
          status = false;
          reason = "Expected order of previous interceptor " + (num - 1)
              + ", actual " + order;
          TLogger.log(reason);
          StatusReporter.report(testname, status, reason,
              getQueueConnectionFactory(ctx), getQueue(ctx));
          return;
        }
      }
    }
    // we don't return positive msg until we know we are done with all
    // interceptors.
    if (num == NUM_OF_INTERCEPTORS) {
      status = true;
      reason = testname + " passed.";
      TLogger.log(reason);
      StatusReporter.report(testname, status, reason,
          getQueueConnectionFactory(ctx), getQueue(ctx));
    }
  }

  // ============== override ================================
  protected void getParametersTest(InvocationContext ctx) {
    String expected = "Object[]{Message}";
    String reason = null;
    boolean status = true;
    Object[] params = ctx.getParameters();
    String errorMsg = null;
    if (params == null) {
      status = false;
      errorMsg = ", actual null";
    } else if (params.length != 1) {
      status = false;
      errorMsg = ", actual " + java.util.Arrays.asList(params).toString();
    } else if (!(params[0] instanceof javax.jms.Message)) {
      status = false;
      errorMsg = ", actual " + params[0];
    }
    if (errorMsg == null) {
      reason = "getParametersTest passed.";
    } else {
      reason = "Expected " + expected + errorMsg;
    }
    TLogger.log(reason);
    StatusReporter.report(getParametersTest, status, reason,
        getQueueConnectionFactory(ctx), getQueue(ctx));
  }

  public static void ensureRollbackOnly(Message msg, EJBContext ejbContext) {
    boolean status = false;
    String reason = null;
    String testname = null;
    try {
      testname = msg.getStringProperty(
          com.sun.ts.tests.ejb30.common.messaging.Constants.TEST_NAME_KEY);
    } catch (javax.jms.JMSException e) {
      status = false;
      reason = "Failed to get test name from message: " + msg;
      TLogger.log(reason);
      StatusReporter.report(getParametersTest, status, reason,
          getQueueConnectionFactory(ejbContext), getQueue(ejbContext));
      return;
    }
    if (testname.equals(
        com.sun.ts.tests.ejb30.common.interceptor.Constants.txRollbackOnlyTest)) {
      // tx was set to rollback only in interceptor
      boolean rb = ejbContext.getRollbackOnly();
      if (rb) {
        status = true;
        reason = "tx was set to rollbackonly in interceptor, as expected.";
      } else {
        status = false;
        reason = "Expected tx set to rollbackonly, actual getRollbackOnly() returned "
            + rb;
      }
      TLogger.log(reason);
      StatusReporter.report(testname, status, reason,
          getQueueConnectionFactory(ejbContext), getQueue(ejbContext));
    }
  }

  // @todo modify Message and set it
  protected void setParametersTest(InvocationContext ctx) {
    Object[] params = ctx.getParameters();
    ctx.setParameters(params);
    Object[] params2 = ctx.getParameters();
    boolean status = true;
    String reason = null;
    if (params.length != params2.length) {
      status = false;
      reason = "Expected 2 InvocationContext.getParameters() to be the same, but they are of different length:"
          + params.length + ", and " + params2.length;
    } else if (!params[0].equals(params2[0])) {
      status = false;
      reason = "Expected 2 InvocationContext.getParameters() return the same, but they were different:"
          + "param: " + java.util.Arrays.asList(params) + "\n" + "param2:"
          + java.util.Arrays.asList(params2);
    } else {
      status = true;
      reason = "setParametersTest passed.";
    }
    TLogger.log(reason);
    StatusReporter.report(setParametersTest, status, reason,
        getQueueConnectionFactory(ctx), getQueue(ctx));
  }

  protected void getMethodTest(InvocationContext ctx) {
    // should return onMessage(Message)
    boolean status = true;
    String reason = null;
    Method meth = ctx.getMethod();
    Method expectedMeth = null;
    if (this.bean == null) {
      String methName = meth.getName();
      Class<?>[] paramTypes = meth.getParameterTypes();
      if (methName.equals(onMessage)) {
        if (paramTypes != null && paramTypes.length == 1
            && paramTypes[0].equals(javax.jms.Message.class)) {
          status = true;
          reason = "The method returned from getMethod is correct:" + onMessage;
        } else {
          status = false;
          reason = "The method returned from getMethod: param types do not match: "
              + ((paramTypes == null) ? "null"
                  : java.util.Arrays.asList(paramTypes).toString());
        }
      } else {
        status = false;
        reason = "The method returned from getMethod is not named " + onMessage;
      }
      TLogger.log(reason);
      StatusReporter.report(getMethodTest, status, reason,
          getQueueConnectionFactory(ctx), getQueue(ctx));
    } else {
      try {
        // Object[] params = new Object[]{javax.jms.Message.class};
        expectedMeth = this.bean.getClass().getMethod(onMessage,
            javax.jms.Message.class);
      } catch (NoSuchMethodException e) {
        status = false;
        reason = "Failed to get method " + onMessage + " from bean "
            + this.bean;
        TLogger.log(reason, e);
        StatusReporter.report(getMethodTest, status, reason,
            getQueueConnectionFactory(ctx), getQueue(ctx));
        return;
      }
      if (!meth.equals(expectedMeth)) {
        status = false;
        reason = "Expected " + expectedMeth + ", actual " + meth;
      } else {
        status = true;
        reason = "Expected method is the same as returned by getMethod().";
      }
      TLogger.log(reason);
      StatusReporter.report(getMethodTest, status, reason,
          getQueueConnectionFactory(ctx), getQueue(ctx));
    }
  }

  protected void txRollbackOnlyTest(InvocationContext ctx) {
    EJBContext con = getEJBContext();
    con.setRollbackOnly();
  }

  protected void txRollbackOnlyAfterTest(InvocationContext ctx) {
    boolean status = true;
    String reason = null;
    EJBContext con = getEJBContext();
    con.setRollbackOnly();
    boolean rb = con.getRollbackOnly();
    if (!rb) {
      status = false;
      reason = "Expected getRollbackOnly() returns true, actual " + rb;
    } else {
      status = true;
      reason = "txRollbackOnlyAfterTest passed.";
    }
    StatusReporter.report(txRollbackOnlyAfterTest, status, reason,
        getQueueConnectionFactory(ctx), getQueue(ctx));
  }

  protected void getContextDataTest(InvocationContext ctx) {
    boolean status = true;
    String reason = null;
    Map<String, Object> map = ctx.getContextData();
    String msg = (String) map.get(INTERCEPTOR_MSG_KEY);
    if (!INTERCEPTOR_MSG.equals(msg)) {
      status = false;
      reason = "Expected InvocationContext data for key " + INTERCEPTOR_MSG_KEY
          + ": " + INTERCEPTOR_MSG + ", actual " + msg;
    } else {
      status = true;
      reason = "getContextDataTest for InvocationContext passed.";
    }
    TLogger.log(reason);
    StatusReporter.report(getContextDataTest, status, reason,
        getQueueConnectionFactory(ctx), getQueue(ctx));
  }

  protected void getBeanTest(InvocationContext ctx) {
    // which one is required by spec, == or equals?
    boolean status = true;
    String reason = null;
    Object bean = ctx.getTarget();
    if (this.bean == null) {
      // it has not been initialized, if the current interceptor is
      // interceptor class
      // in this case, we just check a non-null is returned.
      if (bean == null) {
        status = false;
        reason = "InvocationContext.getBean() returned null.";
        TLogger.log(reason);
        StatusReporter.report(getBeanTest, status, reason,
            getQueueConnectionFactory(ctx), getQueue(ctx));
        return;
      } else {
        status = true;
        TLogger.log(reason);
        StatusReporter.report(getBeanTest, status, reason,
            getQueueConnectionFactory(ctx), getQueue(ctx));
        return;
      }
    }

    if (this.bean != bean) {
      status = false;
      reason = "Expected " + this.bean + ", actual " + bean;
    } else {
      status = true;
      reason = "getBeanTest passed";
    }
    TLogger.log(reason);
    StatusReporter.report(getBeanTest, status, reason,
        getQueueConnectionFactory(ctx), getQueue(ctx));
  }

  protected void suppressExceptionTest(InvocationContext ctx) {
    throw new IllegalStateException("This test does not apply to MDB.");
  }

  protected void runtimeExceptionTest(InvocationContext ctx) {
    throw new IllegalStateException("This test does not apply to MDB.");
  }

  protected void runtimeExceptionAfterTest(InvocationContext ctx) {
    throw new IllegalStateException("This test does not apply to MDB.");
  }

  protected void exceptionTest(InvocationContext ctx) {
    throw new IllegalStateException("This test does not apply to MDB.");
  }

  protected void getParametersEmptyTest(InvocationContext ctx) {
    throw new IllegalStateException("This test does not apply to MDB.");
  }

  /**
   * A wrapper around the static version to enable polymorphism.
   */
  protected void checkOrder0(InvocationContext ctx, int orderInChain) {

    AroundInvokeTestMDBImpl.checkOrder(ctx, orderInChain);
  }

  /**
   * A wrapper around the static version to enable polymorphism.
   */
  protected void checkInvocationContext0(InvocationContext ctx,
      int orderInChain) {

    AroundInvokeTestMDBImpl.checkInvocationContext(ctx, orderInChain);
  }

  protected void sameSecContextTest(InvocationContext ctx) {
    boolean status = true;
    String reason = null;
    EJBContext ejbContext = getEJBContext();
    java.security.Principal princ = ejbContext.getCallerPrincipal();
    if (princ == null) {
      status = false;
      reason = "In AroundInvokeTestMDBImpl, caller principal is null.";
      TLogger.log(reason);
      StatusReporter.report(sameSecContextTest, status, reason,
          getQueueConnectionFactory(ctx), getQueue(ctx));
      return;
    }
    if (this.bean != null) {
      // it means, the current interceptor is inside the bean class
      // beanPrincipal should have been initialized in ctor.
      // Note that EJBContext, getCallPrincipal() never returns null.
      if (this.beanPrincipal != null) {
        if (!princ.equals(beanPrincipal)) {
          status = false;
          reason = "Principal in interceptor is not the same as in bean.";
        } else {
          // this.bean is not null, and beanPrincipal is not null,
          // and they are the same.
          status = true;
          reason = "sameSecContextTest passed: interceptor has the same Principal as bean method.";
        }
      } else {
        status = false;
        reason = "In AroundInvokeTestMDBImpl, bean is not null, but beanPrincipal is null.";
      }
    } else {
      // this.bean is null
      // it means this interceptor is a separate interceptor class.
      // we pass thru in this case.
    }
    TLogger.log(reason);
    StatusReporter.report(sameSecContextTest, status, reason,
        getQueueConnectionFactory(ctx), getQueue(ctx));
  }

}
