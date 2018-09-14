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

import java.util.ArrayList;
import java.util.Properties;

import com.sun.ts.lib.harness.EETest;
import com.sun.javatest.Status;

import com.sun.ts.tests.ejb30.common.calc.CalculatorException;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import java.rmi.RemoteException;
import javax.ejb.EJBException;

/**
 * To add a new test for interceptors: o add xxxTest() to ClientBase.java and
 * Client.java o add xxxTest(...) to bean interface, bean base, and bean class o
 * add xxxTest(InvocationContext ctx) to AroundInvokeTestImpl.java o add in
 * AroundInvokeTestImpl.intercept(): xxxTest(ctx) o add to Constants.java:
 * public static final String..
 */
// @todo res lookup thru EJBContext
// @todo programming restrictions for interceptors
abstract public class ClientBase extends EETest implements Constants {
  protected Properties props;

  /*
   * @class.setup_props:
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
  }

  public void cleanup() throws Fault {
  }

  /**
   * Removes all beans used in this client. It should only be used by sfsb,
   * though other bean types may also have a remove business method.
   */
  protected void remove() {
    if (getBean() != null) {
      try {
        getBean().remove();
        TLogger.log("bean removed successfully.");
      } catch (Exception e) {
        // ignore
        TLogger.log("failed to remove bean.");
      }
    }
  }

  /*
   * testName: getBeanTest
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */
  public void getBeanTest() throws Fault {
    try {
      getBean().getBeanTest();
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * testName: getParametersTest
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */
  public void getParametersTest() throws Fault {
    String expected = OLD_PARAM_VALUE;
    String result = null;
    try {
      result = getBean().getParametersTest(OLD_PARAM_VALUE);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
    if (!expected.equals(result)) {
      throw new Fault("Expected " + expected + ", actual " + result);
    }
  }

  /////////////////////////////////////////////////////////////////////////
  // getParametersEmptyTest does not apply to mdb
  /////////////////////////////////////////////////////////////////////////
  /*
   * testName: getParametersEmptyTest
   * 
   * @test_Strategy: o no parameters in business method.
   * InvocationContext.getParameters() should return null or Object[]{}; This is
   * verified in interceptor method.
   */
  public void getParametersEmptyTest() throws Fault {
    try {
      getBean().getParametersEmptyTest();
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * testName: setParametersTest
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */
  public void setParametersTest() throws Fault {
    String expected = NEW_PARAM_VALUE;
    String result = null;
    try {
      result = getBean().setParametersTest(OLD_PARAM_VALUE);
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
    if (!expected.equals(result)) {
      throw new Fault("Expected " + expected + ", actual " + result);
    }
  }

  /*
   * testName: getContextDataTest
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */
  public void getContextDataTest() throws Fault {
    try {
      getBean().getContextDataTest();
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * testName: getMethodTest
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */
  public void getMethodTest() throws Fault {
    try {
      getBean().getMethodTest();
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * testName: txRollbackOnlyTest
   * 
   * @test_Strategy: o interceptor marks a tx to be rollback only before
   * proceed();
   */
  public void txRollbackOnlyTest() throws Fault {
    try {
      getBean().txRollbackOnlyTest();
    } catch (CalculatorException e) {
      throw new Fault("tx not rolled back properly.", e);
    } catch (Exception e) {
      throw new Fault("Unexpected exception", e);
    }
  }

  /*
   * testName: txRollbackOnlyAfterTest
   * 
   * @test_Strategy: o interceptor marks a tx to be rollback only after
   * proceed();
   */
  public void txRollbackOnlyAfterTest() throws Fault {
    try {
      getBean().txRollbackOnlyAfterTest();
    } catch (CalculatorException e) {
      throw new Fault("tx not rolled back properly.", e);
    } catch (Exception e) {
      // client may get RemoteException since the tx was rolled back, but this
      // exception should not be CalculatorException.
      // throw new Fault("Unexpected exception", e);
    }
  }

  //////////////////////////////////////////////////////////////////////////
  // sameSecContextTest only applies to interceptor/**/method/
  // since we need the bean to pass in Principal to compare the one in
  // interceptor methods.
  //////////////////////////////////////////////////////////////////////////
  /*
   * testName: sameSecContextTest
   * 
   * @test_Strategy: o interceptor method occurs with the same security context
   * as the business method
   */
  public void sameSecContextTest() throws Fault {
    boolean expected = true;
    boolean result = false;
    try {
      result = getBean().sameSecContextTest();
    } catch (CalculatorException e) {
      throw new Fault("Test failed:", e);
    } catch (Exception e) {
      throw new Fault("Test failed:", e);
    }
    if (result != expected) {
      throw new Fault("Expected " + expected + ", actual=" + result + ".");
    }
  }

  ////////////////////////////////////////////////////////////////////////
  // the 4 exception tests do not apply to mdb:
  // o onMessage() does not throw application exceptions;
  // o runtime exceptions from onMessage() are propogated to resource adaptor
  // and cannot be verified.
  ////////////////////////////////////////////////////////////////////////

  /*
   * testName: exceptionTest
   * 
   * @test_Strategy: o using @AroundInvoke annotation in bean class o test
   * InvocationContext methods o interceptor method can throw exceptions
   */
  public void exceptionTest() throws Fault {
    String expected = null;
    String result = null;
    try {
      result = getBean().exceptionTest();
    } catch (CalculatorException e) {
      TLogger.log("got expected exceptioin.");
    } catch (Exception e) {
      throw new Fault("Expected a CalculatorException, actual ", e);
    }
    if (result != null) {
      throw new Fault("Expected " + expected + ", actual=" + result + ".");
    }
  }

  /*
   * testName: suppressExceptionTest
   * 
   * @test_Strategy: o the checked exception throwb by the business method can
   * be supressed by the interceptor.
   */
  public void suppressExceptionTest() throws Fault {
    try {
      getBean().suppressExceptionTest();
    } catch (CalculatorException e) {
      throw new Fault(
          "This exception should have been supressed by interceptor.", e);
    } catch (Exception e) {
      throw new Fault("Unexpected exception", e);
    }
  }

  /*
   * testName: runtimeExceptionTest
   * 
   * @test_Strategy: o interceptor marks a tx to be rollback only before
   * proceed(), by runtime exception o bean instance is no longer usable after
   * this test
   */
  public void runtimeExceptionTest() throws Fault {
    try {
      getBean().runtimeExceptionTest();
      throw new Fault(
          "Should not get here. RuntimeException should've been thrown from AroundInvoke method.");
    } catch (CalculatorException e) {
      throw new Fault("tx not rolled back properly.", e);
    } catch (Exception e) {
      if (e instanceof RemoteException) {
        TLogger.log("Got RemoteException, OK.");
      } else if (e instanceof EJBException) {
        TLogger.log("Got EJBException, OK.");
      } else if (e instanceof RuntimeException) {
        throw new Fault(
            "Got RuntimeException. It should not be thrown to the client.");
      } else {
        throw new Fault("Unexpected exception: " + e.getClass(), e);
      }
    }
  }

  /*
   * testName: runtimeExceptionAfterTest
   * 
   * @test_Strategy: o interceptor marks a tx to be rollback only after
   * proceed(), by runtime exception o bean instance is no longer usable after
   * this test
   */
  public void runtimeExceptionAfterTest() throws Fault {
    try {
      getBean().runtimeExceptionAfterTest();
      throw new Fault(
          "Should not get here. RuntimeException should've been thrown from AroundInvoke method.");
    } catch (CalculatorException e) {
      throw new Fault("tx not rolled back properly.", e);
    } catch (Exception e) {
      if (e instanceof RemoteException) {
        TLogger.log("Got RemoteException, OK.");
      } else if (e instanceof EJBException) {
        TLogger.log("Got EJBException, OK.");
      } else if (e instanceof RuntimeException) {
        throw new Fault(
            "Got RuntimeException. It should not be thrown to the client.");
      } else {
        throw new Fault("Unexpected exception: " + e.getClass(), e);
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // orderTest and sameInvocationContextTest can only be tested when multiple
  // AroundInvoke methods are used. Since bean class can only have one
  // AroundInvoke method, do not inherite the two tests if you are only using
  // AroundInvoke method in bean class.
  ///////////////////////////////////////////////////////////////////////////
  /*
   * testName: orderTest
   * 
   * @test_Strategy: o the order of interceptors
   */
  public void orderTest() throws Fault {
    try {
      getBean().orderTest();
    } catch (CalculatorException e) {
      throw new Fault("Test failed.", e);
    } catch (Exception e) {
      throw new Fault("Unexpected exception", e);
    }
  }

  /*
   * testName: sameInvocationContextTest
   * 
   * @test_Strategy: o the same instance of InvocationContext is passed to all
   * interceptors
   */
  public void sameInvocationContextTest() throws Fault {
    try {
      getBean().sameInvocationContextTest();
    } catch (CalculatorException e) {
      throw new Fault("Test failed.", e);
    } catch (Exception e) {
      throw new Fault("Unexpected exception", e);
    }
  }

  /*
   * testName: methodLevelInterceptorMixedTest
   * 
   * @test_Strategy: o method level interceptors may be specified
   * with @Interceptors or in descriptor. The one specified in descriptor is
   * invoked after the ones specified with annotations.
   */
  public void methodLevelInterceptorMixedTest() throws CalculatorException {
    String firstMethodInterceptor = "InterceptorNoat1";
    String secondMethodInterceptor = "Interceptor1";
    String shortBeanName = "AroundInvokeBean";

    ArrayList<String> expected = new ArrayList<String>();
    expected.add(firstMethodInterceptor);
    expected.add(secondMethodInterceptor);
    expected.add(shortBeanName);

    ArrayList<String> result = getBean()
        .methodLevelInterceptorMixedTest(new ArrayList<String>());
    if (expected.equals(result)) {
      TLogger.log("Method-level interceptors are invoked in the correct order: "
          + result);
    } else {
      throw new CalculatorException(
          "Method-level interceptors are not invoked in the correct order. "
              + "Expecting " + expected + ", actual " + result);
    }
  }

  /*
   * testName: methodLevelClassLevelInterceptorMixedTest
   * 
   * @test_Strategy: o method level interceptors may be specified
   * with @Interceptors or in descriptor. The one specified in descriptor is
   * invoked after the ones specified with annotations. Default interceptors are
   * excluded, but class level interceptors are included.
   */
  public void methodLevelClassLevelInterceptorMixedTest()
      throws CalculatorException {
    String firstMethodInterceptor = "Interceptor1";
    String secondMethodInterceptor = "InterceptorNoat1";
    String firstClassInterceptor = "Interceptor1";
    String secondClassInterceptor = "InterceptorNoat1";
    String shortBeanName = "AroundInvokeBean";

    ArrayList<String> expected = new ArrayList<String>();
    expected.add(firstClassInterceptor);
    expected.add(firstClassInterceptor);
    expected.add(secondClassInterceptor);
    expected.add(secondClassInterceptor);
    expected.add(firstMethodInterceptor);
    expected.add(secondMethodInterceptor);
    expected.add(shortBeanName);

    ArrayList<String> result = getBean()
        .methodLevelClassLevelInterceptorMixedTest(new ArrayList<String>());
    if (expected.equals(result)) {
      TLogger.log("Method-level interceptors are invoked in the correct order: "
          + result);
    } else {
      throw new CalculatorException(
          "Method-level interceptors are not invoked in the correct order. "
              + "Expecting " + expected + ", actual " + result);
    }
  }

  /*
   * testName: repeatedInterceptors
   * 
   * @test_Strategy: o method level interceptors may be specified
   * with @Interceptors or in descriptor. Interceptors are additive.
   */
  public void repeatedInterceptors() throws CalculatorException {
    String firstMethodInterceptor = "InterceptorNoat1";
    String secondMethodInterceptor = "Interceptor1";
    String firstClassInterceptor = "Interceptor1";
    String secondClassInterceptor = "InterceptorNoat1";
    String shortBeanName = "AroundInvokeBean";

    ArrayList<String> expected = new ArrayList<String>();
    // 2 class interceptors annotated
    expected.add(firstClassInterceptor);
    expected.add(firstClassInterceptor);
    // 2 class interceptors in descriptor
    expected.add(secondClassInterceptor);
    expected.add(secondClassInterceptor);
    // 2 method interceptors annotated
    expected.add(firstMethodInterceptor);
    expected.add(firstMethodInterceptor);
    // 3 method interceptors in descriptor
    expected.add(secondMethodInterceptor);
    expected.add(secondMethodInterceptor);
    expected.add(firstMethodInterceptor);
    // bean class
    expected.add(shortBeanName);

    ArrayList<String> result = getBean()
        .repeatedInterceptors(new ArrayList<String>());
    if (expected.equals(result)) {
      TLogger.log("Method-level interceptors are invoked in the correct order: "
          + result);
    } else {
      throw new CalculatorException(
          "Method-level interceptors are not invoked in the correct order. "
              + "Expecting " + expected + ", actual " + result);
    }
  }

  /*
   * testName: interceptorOrderingOverride
   * 
   * @test_Strategy: o method level interceptors may be specified
   * with @Interceptors or in descriptor. Interceptor order may be overridden
   * with <interceptor-order>
   */
  public void interceptorOrderingOverride() throws CalculatorException {
    String firstMethodInterceptor = "InterceptorNoat1";
    String secondMethodInterceptor = "Interceptor1";
    String shortBeanName = "AroundInvokeBean";

    ArrayList<String> expected = new ArrayList<String>();
    // 2 method interceptors in <interceptor-order>
    expected.add(firstMethodInterceptor);
    expected.add(secondMethodInterceptor);
    // bean class
    expected.add(shortBeanName);

    ArrayList<String> result = getBean()
        .interceptorOrderingOverride(new ArrayList<String>());
    if (expected.equals(result)) {
      TLogger.log("Method-level interceptors are invoked in the correct order: "
          + result);
    } else {
      throw new CalculatorException(
          "Method-level interceptors are not invoked in the correct order. "
              + "Expecting " + expected + ", actual " + result);
    }
  }

  //////////////////////////////////////////////////////////////////////////
  // afterBeginTest and beforeCompletionTest can only be used with
  // sfsb with SessionSynchronization. Otherwise, do not inherit them.
  //////////////////////////////////////////////////////////////////////////
  /*
   * testName: afterBeginTest
   * 
   * @test_Strategy: o afterBegin happens before any interceptor methods
   */
  public void afterBeginTest() throws Fault {
    try {
      getBean().afterBeginTest();
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * testName: beforeCompletionTest
   * 
   * @test_Strategy: o beforeCompletion happens after any interceptor methods
   */
  public void beforeCompletionTest() throws Fault {
    try {
      getBean().beforeCompletionTest();
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  abstract protected AroundInvokeIF getBean();
}
