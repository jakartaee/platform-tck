/*
 * Copyright (c) 2007, 2021  Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.common.callback;

import java.util.List;
import java.util.Properties;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.TLogger;

/**
 * A test client for callback methods. Note that since callback methods cannot
 * throw application exception, so we can only convey test result back to client
 * through the returned value.
 */
abstract public class ClientBase extends EETest {
  // for default interceptor PostConstruct calls, e.g.,
  // stateless/callback/defaultinterceptor/descriptor
  protected static final String[] INTERCEPTORS_AB = new String[] { "A", "B" };

  protected static final String[] INTERCEPTORS_BA = new String[] { "B", "A" };

  protected static final String[] INTERCEPTORS_A = new String[] { "A" };

  protected static final String[] INTERCEPTORS_B = new String[] { "B" };

  protected static final String[] INTERCEPTORS_NONE = new String[] {};

  // for allthreelevels interceptor PostConstruct calls, e.g.,
  // stateless/callback/allthreelevels/descriptor
  protected static final String[] INTERCEPTORS_ABCD_BEAN = new String[] { "A",
      "B", "C", "D", "BEAN" };

  protected static final String[] INTERCEPTORS_BDCA_BEAN = new String[] { "B",
      "D", "C", "A", "BEAN" };

  protected static final String[] INTERCEPTORS_CD_BEAN = new String[] { "C",
      "D", "BEAN" };

  protected static final String[] INTERCEPTORS_CDA_BEAN = new String[] { "C",
      "D", "A", "BEAN" };

  // for inheritance tests (e.g., stateless/callback/inheritance/descriptor).
  protected static final String[] INTERCEPTORS_ABCEFCG_SUPERSUPER_SUPER_BEAN = new String[] {
      "A", "B", "C", "E", "F", "C", "G", "SUPERSUPER", "SUPER", "BEAN" };

  protected static final String[] INTERCEPTORS_H_BEAN = new String[] { "H",
      "BEAN" };

  protected static final String[] INTERCEPTORS_I_BEAN = new String[] { "I",
      "BEAN" };

  protected static final String[] INTERCEPTORS_I_SUPER = new String[] { "I",
      "SUPER" };

  // for InvocationContext.getContextData() tests. Only lifecycle methods
  // in interceptor classes can take InvocationContext, and so "BEAN" values
  // are not in expected results.
  protected static final String[] INTERCEPTORS_ABCD = new String[] { "A", "B",
      "C", "D" };

  protected static final String[] INTERCEPTORS_BDCA = new String[] { "B", "D",
      "C", "A" };

  protected static final String[] INTERCEPTORS_CD = new String[] { "C", "D" };

  protected static final String[] INTERCEPTORS_CDA = new String[] { "C", "D",
      "A" };

  protected static final String[] INJECTIONS_NONE = new String[] {};

  protected Properties props;

  abstract protected CallbackIF getBean();

  abstract protected Callback2IF getBean2();

  protected Callback2IF getBean3() {
    return null;
  }

  protected Callback2IF getBean4() {
    return null;
  }

  protected Callback2IF getNoDefaultInterceptorBean() {
    return null;
  }

  protected Callback2IF getSingleDefaultInterceptorBean() {
    return null;
  }

  // abstract protected CallbackIF getSessionBean();
  protected CallbackIF getSessionBean() {
    return null;
  }

  /*
   * @class.setup_props:
   */
  public void setup(String[] args, Properties p) throws Exception {
    props = p;
  }

  /**
   * Removes all beans used in this client. It should only be used by sfsb,
   * though other bean types may also have a remove business method.
   */
  protected void remove() {
    if (getBean() != null) {
      try {
        getBean().removeFoo();
        TLogger.log("bean removed successfully.");
      } catch (Exception e) {
        // ignore
        TLogger.log("failed to remove bean.");
      }
    }
    if (getBean2() != null) {
      try {
        getBean2().removeFoo();
        TLogger.log("bean2 removed successfully.");
      } catch (Exception e) {
        // ignore
        TLogger.log("failed to remove bean2.");
      }
    }
    if (getSessionBean() != null) {
      try {
        getSessionBean().removeFoo();
        TLogger.log("SessionBeanCallbackBean removed successfully.");
      } catch (Exception e) {
        // ignore
        TLogger.log("failed to remove SessionBeanCallbackBean.");
      }
    }
    if (getBean3() != null) {
      try {
        getBean3().removeFoo();
        TLogger.log("bean3 removed successfully.");
      } catch (Exception e) {
        // ignore
        TLogger.log("failed to remove bean3.");
      }
    }
    if (getBean4() != null) {
      try {
        getBean4().removeFoo();
        TLogger.log("bean4 removed successfully.");
      } catch (Exception e) {
        // ignore
        TLogger.log("failed to remove bean4.");
      }
    }
    if (getNoDefaultInterceptorBean() != null) {
      try {
        getNoDefaultInterceptorBean().removeFoo();
        TLogger.log("noDefaultInterceptorBean removed successfully.");
      } catch (Exception e) {
        // ignore
        TLogger.log("failed to remove noDefaultInterceptorBean.");
      }
    }
    if (getSingleDefaultInterceptorBean() != null) {
      try {
        getSingleDefaultInterceptorBean().removeFoo();
        TLogger.log("singleDefaultInterceptorBean removed successfully.");
      } catch (Exception e) {
        // ignore
        TLogger.log("failed to remove singleDefaultInterceptorBean.");
      }
    }
  }

  public void cleanup() throws Exception {
  }

  /*
   * testName: isPostConstructCalledTest
   * 
   * @test_Strategy: o using annotations: o CallbackListener o PostConstruct o
   * PreDestroy o verify callback methods in handler class are invoked o
   * callback methods may, in some cases, named as ejbCreate, ejbRemove
   */
  public void isPostConstructCalledTest() throws Exception {
    boolean expected = true;
    boolean actual = getBean().isPostConstructCalledTest();
    if (expected != actual) {
      throw new Exception(
          "PostConstruct method has not been called when business method is called");
    }
  }

  /*
   * testName: isInjectionDoneTest
   * 
   * @test_Strategy: o using annotations: o CallbackListener o PostConstruct o
   * PreDestroy o Resource o verify dependency injection has occurred when
   * callback method is called
   */
  public void isInjectionDoneTest() throws Exception {
    boolean expected = true;
    boolean actual = getBean().isInjectionDoneTest();
    if (expected != actual) {
      throw new Exception(
          "Dependency injection has not occurred when callback method is called");
    }
  }

  /*
   * testName: isPostConstructCalledSessionBeanTest
   * 
   * @test_Strategy: o using annotations: o CallbackListener o PostConstruct o
   * PreDestroy o verify callback methods in handler class are invoked o
   * callback methods may, in some cases, named as ejbCreate, ejbRemove
   */
  public void isPostConstructCalledSessionBeanTest() throws Exception {
    boolean expected = true;
    boolean actual = getSessionBean().isPostConstructCalledTest();
    if (expected != actual) {
      throw new Exception(
          "PostConstruct method has not been called when business method is called");
    }
  }

  /*
   * testName: isInjectionDoneSessionBeanTest
   * 
   * @test_Strategy: o using annotations: o CallbackListener o PostConstruct o
   * PreDestroy o Resource o verify dependency injection has occurred when
   * callback method is called
   */
  public void isInjectionDoneSessionBeanTest() throws Exception {
    boolean expected = true;
    boolean actual = getSessionBean().isInjectionDoneTest();
    if (expected != actual) {
      throw new Exception(
          "Dependency injection has not occurred when callback method is called");
    }
  }

  /*
   * testName: isPostConstructOrPreDestroyCalledTest
   * 
   * @test_Strategy: o using annotations: o CallbackListener o PostConstruct o
   * PreDestroy o apply two/four callback annotations on the same method o
   * callback methods may use arbitrary names
   */
  public void isPostConstructOrPreDestroyCalledTest() throws Exception {
    boolean expected = true;
    boolean actual = getBean2().isPostConstructOrPreDestroyCalledTest();
    if (expected != actual) {
      throw new Exception(
          "PostConstruct or PreDestroy method has not been called when business method is called");
    }
  }

  /////////////////////////////////////////////////////////////////////////
  // These tests are for sfsb only
  /////////////////////////////////////////////////////////////////////////
  /*
   * testName: runtimeExceptionTest
   * 
   * @test_Strategy: o callback methods may throw runtimeException. If inside a
   * tx context, it causes the tx to rollback. o bean instance is no longer
   * usable after this test Note: this is not testable, because: o the only
   * predictible callback is PostConstruct; o if PostConstruct throws
   * RuntimeException, the bean is destroyed and not available to service
   * business methods.
   */
  // public void runtimeExceptionTest() throws Exception {
  // try {
  // bean.runtimeExceptionTest();
  // throw new Exception("Should not get here. RuntimeException should've been
  // thrown from PostConstruct method.");
  // } catch (Exception e) {
  // if(e instanceof RemoteException) {
  // Logger.log("Got RemoteException, OK.");
  // } else if(e instanceof EJBException) {
  // Logger.log("Got EJBException, OK.");
  // } else if(e instanceof RuntimeException) {
  // throw new Exception("Got RuntimeException. It should not be thrown to the
  // client.");
  // } else {
  // throw new Exception("Unexpected exception: " + e.getClass(), e);
  // }
  // }
  // }

  /*
   * testName: defaultInterceptorsForCallbackBean1
   * 
   * @test_Strategy: multiple default interceptors are configured for an ejb
   * jar. Verifies they are invoked in the correct order.
   *
   */
  public void defaultInterceptorsForCallbackBean1() throws Exception {
    List actual = getBean().getPostConstructCalls();
    Helper.compareResultList(INTERCEPTORS_AB, actual);
  }

  /*
   * testName: defaultInterceptorsForCallbackBean2
   * 
   * @test_Strategy: multiple default interceptors are configured for an ejb
   * jar. Verifies they are invoked in the correct order.
   *
   */
  public void defaultInterceptorsForCallbackBean2() throws Exception {
    List actual = getBean2().getPostConstructCalls();
    Helper.compareResultList(INTERCEPTORS_BA, actual);
  }

  /*
   * testName: defaultInterceptorsForCallbackBean3
   * 
   * @test_Strategy: multiple default interceptors are configured for an ejb
   * jar, but they are excluded for this CallbackBean3 with
   * 
   * @ExcludeDefaultInterceptors on bean class. Verifies they are not invoked.
   *
   */
  public void defaultInterceptorsForCallbackBean3() throws Exception {
    List actual = getBean3().getPostConstructCalls();
    Helper.compareResultList(INTERCEPTORS_NONE, actual);
  }

  /*
   * testName: defaultInterceptorsForCallbackBean4
   * 
   * @test_Strategy: multiple default interceptors are configured for an ejb
   * jar, but they are excluded for this CallbackBean4 with
   * exclude-default-interceptors in ejb-jar.xml. Verifies they are not invoked.
   * This test is the same as defaultInterceptorsForCallbackBean3 except this
   * one uses descriptor to exclude default interceptors.
   *
   */
  public void defaultInterceptorsForCallbackBean4() throws Exception {
    List actual = getBean4().getPostConstructCalls();
    Helper.compareResultList(INTERCEPTORS_NONE, actual);
  }

  /*
   * testName: noDefaultInterceptorJar
   * 
   * @test_Strategy: no default interceptors are configured for an ejb jar.
   * Verifies none is not invoked.
   *
   */
  public void noDefaultInterceptorJar() throws Exception {
    List actual = getNoDefaultInterceptorBean().getPostConstructCalls();
    Helper.compareResultList(INTERCEPTORS_NONE, actual);
  }

  /*
   * testName: singleDefaultInterceptorJar
   * 
   * @test_Strategy: single default interceptors are configured for an ejb jar.
   * Verifies only one is invoked.
   *
   */
  public void singleDefaultInterceptorJar() throws Exception {
    List actual = getSingleDefaultInterceptorBean().getPostConstructCalls();
    Helper.compareResultList(INTERCEPTORS_A, actual);
  }

  //////////////////////////////////////////////////////////////////////
  // the following tests only apply to threelevels/* directories.
  //////////////////////////////////////////////////////////////////////

  /*
   * testName: threeLevelInterceptorssForCallbackBean1
   * 
   * @test_Strategy: multiple default/class/method interceptorsare configured
   * for an ejb jar. Verifies they are invoked in the correct order.
   *
   */
  public void threeLevelInterceptorssForCallbackBean1() throws Exception {
    List actual = getBean().getPostConstructCalls();
    Helper.compareResultList(INTERCEPTORS_ABCD_BEAN, actual);
  }

  /*
   * testName: threeLevelInterceptorssForCallbackBean2
   * 
   * @test_Strategy: multiple default/class/method interceptors are configured
   * for an ejb jar. Verifies they are invoked in the correct order.
   *
   */
  public void threeLevelInterceptorssForCallbackBean2() throws Exception {
    List actual = getBean2().getPostConstructCalls();
    Helper.compareResultList(INTERCEPTORS_BDCA_BEAN, actual);
  }

  /*
   * testName: threeLevelInterceptorssForCallbackBean3
   * 
   * @test_Strategy: multiple default interceptors are configured for an ejb
   * jar, but they are excluded for this CallbackBean3 with
   * 
   * @ExcludethreeLevelInterceptorss on bean class. Verifies they are not
   * invoked. class/method level interceptors should be invoked.
   *
   */
  public void threeLevelInterceptorssForCallbackBean3() throws Exception {
    List actual = getBean3().getPostConstructCalls();
    Helper.compareResultList(INTERCEPTORS_CD_BEAN, actual);
  }

  /*
   * testName: threeLevelInterceptorssForCallbackBean4
   * 
   * @test_Strategy: multiple default interceptors are configured for an ejb
   * jar, but they are excluded for this CallbackBean4 with
   * exclude-default-interceptors in ejb-jar.xml. Verifies they are not invoked.
   * class/method level interceptors should be invoked. This test is the same as
   * threeLevelInterceptorssForCallbackBean3 except this one uses descriptor to
   * exclude default interceptors.
   *
   */
  public void threeLevelInterceptorssForCallbackBean4() throws Exception {
    List actual = getBean4().getPostConstructCalls();
    Helper.compareResultList(INTERCEPTORS_CD_BEAN, actual);
  }

  /*
   * testName: threeLevelSingleDefaultInterceptorsJar
   * 
   * @test_Strategy: single default interceptors are configured for an ejb jar.
   * Verifies only one default is invoked as well as class/method interceptors.
   *
   */
  public void threeLevelSingleDefaultInterceptorsJar() throws Exception {
    List actual = getSingleDefaultInterceptorBean().getPostConstructCalls();
    Helper.compareResultList(INTERCEPTORS_CDA_BEAN, actual);
  }

  //////////////////////////////////////////////////////////////////////

  /*
   * testName: threeLevelInvocationContextDataForCallbackBean1
   * 
   * @test_Strategy: multiple default/class/method interceptorsare configured
   * for an ejb jar. Verifies InvocationContext.getContextData().
   *
   */
  public void threeLevelInvocationContextDataForCallbackBean1() throws Exception {
    List actual = getBean().getPostConstructCallsInContextData();
    Helper.compareResultList(INTERCEPTORS_ABCD, actual);
  }

  /*
   * testName: threeLevelInvocationContextDataForCallbackBean2
   * 
   * @test_Strategy: multiple default/class/method interceptors are configured
   * for an ejb jar. Verifies InvocationContext.getContextData().
   *
   */
  public void threeLevelInvocationContextDataForCallbackBean2() throws Exception {
    List actual = getBean2().getPostConstructCallsInContextData();
    Helper.compareResultList(INTERCEPTORS_BDCA, actual);
  }

  /*
   * testName: threeLevelInvocationContextDataForCallbackBean3
   * 
   * @test_Strategy: multiple default interceptors are configured for an ejb
   * jar, but they are excluded for this CallbackBean3 with
   * 
   * @ExcludethreeLevelInvocationContextData on bean class. Verifies they are
   * not invoked. class/method level interceptors should be invoked. Verifies
   * InvocationContext.getContextData().
   */
  public void threeLevelInvocationContextDataForCallbackBean3() throws Exception {
    List actual = getBean3().getPostConstructCallsInContextData();
    Helper.compareResultList(INTERCEPTORS_CD, actual);
  }

  /*
   * testName: threeLevelInvocationContextDataForCallbackBean4
   * 
   * @test_Strategy: multiple default interceptors are configured for an ejb
   * jar, but they are excluded for this CallbackBean4 with
   * exclude-default-interceptors in ejb-jar.xml. Verifies they are not invoked.
   * class/method level interceptors should be invoked. Verifies
   * InvocationContext.getContextData().
   */
  public void threeLevelInvocationContextDataForCallbackBean4() throws Exception {
    List actual = getBean4().getPostConstructCallsInContextData();
    Helper.compareResultList(INTERCEPTORS_CD, actual);
  }

  /*
   * testName: threeLevelSingleDefaultInvocationContextJar
   * 
   * @test_Strategy: single default interceptors are configured for an ejb jar.
   * Verifies only one default is invoked as well as class/method interceptors.
   * Verifies InvocationContext.getContextData().
   */
  public void threeLevelSingleDefaultInvocationContextJar() throws Exception {
    List actual = getSingleDefaultInterceptorBean()
        .getPostConstructCallsInContextData();
    Helper.compareResultList(INTERCEPTORS_CDA, actual);
  }

  //////////////////////////////////////////////////////////////////////
  // inheritance tests
  //////////////////////////////////////////////////////////////////////
  /*
   * testName: inheritanceInterceptorsForCallbackBean3
   * 
   * @test_Strategy: multiple default interceptors are configured for an ejb
   * jar. class-level interceptors and in-bean lifecycle methods are also
   * defined, and both class-level interceptors and bean class have superclass
   * and super-superclass. Verify these all are invoked in the correct order.
   *
   */
  public void inheritanceInterceptorsForCallbackBean3() throws Exception {
    List actual = getBean3().getPostConstructCalls();
    Helper.compareResultList(INTERCEPTORS_ABCEFCG_SUPERSUPER_SUPER_BEAN,
        actual);
  }

  /*
   * testName: inheritanceInterceptorsForCallbackBean1
   * 
   * @test_Strategy: A bean that overrides and thus disables all lifecycle
   * callback methods in its superclasses. Its class-level interceptor,
   * InterceptorH, also overrides and disables its superclasses' lifecycle
   * callback methods. In both cases, overriding methods themselves are not
   * lifecycle methods.
   */
  public void inheritanceInterceptorsForCallbackBean1() throws Exception {
    List actual = getBean().getPostConstructCalls();
    Helper.compareResultList(INTERCEPTORS_H_BEAN, actual);
  }

  /*
   * testName: inheritanceInterceptorsForCallbackBean2
   * 
   * @test_Strategy: A bean that overrides and thus disables all lifecycle
   * callback methods in its superclasses. Its class-level interceptor,
   * InterceptorH, also overrides and disables its superclasses' lifecycle
   * callback methods. In both cases, one overriding method is
   * still @PostConstruct method, and the other is re-annotated as @PreDestroy
   * method.
   */
  public void inheritanceInterceptorsForCallbackBean2() throws Exception {
    List actual = getBean2().getPostConstructCalls();
    Helper.compareResultList(INTERCEPTORS_I_BEAN, actual);
  }

  /*
   * testName: inheritanceInterceptorsForCallbackBean4
   * 
   * @test_Strategy: A bean that does not contain any lifecycle methods. Its
   * superclass contains lifecycle methods, and also overrides/disables
   * lifecycle methods in ITS superclasses.
   */
  public void inheritanceInterceptorsForCallbackBean4() throws Exception {
    List actual = getBean4().getPostConstructCalls();
    Helper.compareResultList(INTERCEPTORS_I_SUPER, actual);
  }

  /*
   * testName: invocationContextIllegalStateException
   * 
   * @test_Strategy:
   */
  public void invocationContextIllegalStateException() throws Exception {
    CallbackIF bean1 = getBean();
    if (bean1.isGetParametersIllegalStateExceptionThrown()) {
      TLogger.log("Got expected IllegalStateException when calling "
          + "InvocationContext.getParameters() from a lifecycle method.");
    } else {
      throw new Exception("Expecting IllegalStateException when calling "
          + "InvocationContext.getParameters() from a lifecycle method. "
          + "But got none.");
    }
    if (bean1.isSetParametersIllegalStateExceptionThrown()) {
      TLogger.log("Got expected IllegalStateException when calling "
          + "InvocationContext.SetParameters() from a lifecycle method.");
    } else {
      throw new Exception("Expecting IllegalStateException when calling "
          + "InvocationContext.SetParameters() from a lifecycle method. "
          + "But got none.");
    }
  }

  /*
   * testName: invocationContextIllegalStateException2
   * 
   * @test_Strategy:
   */
  public void invocationContextIllegalStateException2() throws Exception {
    Callback2IF bean2 = getBean2();
    if (bean2.isGetParametersIllegalStateExceptionThrown()) {
      TLogger.log("Got expected IllegalStateException when calling "
          + "InvocationContext.getParameters() from a lifecycle method.");
    } else {
      throw new Exception("Expecting IllegalStateException when calling "
          + "InvocationContext.getParameters() from a lifecycle method. "
          + "But got none.");
    }
    if (bean2.isSetParametersIllegalStateExceptionThrown()) {
      TLogger.log("Got expected IllegalStateException when calling "
          + "InvocationContext.SetParameters() from a lifecycle method.");
    } else {
      throw new Exception("Expecting IllegalStateException when calling "
          + "InvocationContext.SetParameters() from a lifecycle method. "
          + "But got none.");
    }
  }
}
