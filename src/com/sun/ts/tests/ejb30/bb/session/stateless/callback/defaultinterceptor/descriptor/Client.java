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

package com.sun.ts.tests.ejb30.bb.session.stateless.callback.defaultinterceptor.descriptor;

import com.sun.javatest.Status;
import com.sun.ts.tests.ejb30.common.callback.ClientBase;
import com.sun.ts.tests.ejb30.common.callback.Callback2IF;
import com.sun.ts.tests.ejb30.common.callback.CallbackIF;
import javax.ejb.EJB;

/**
 * A test client for callback methods. Note that since callback methods cannot
 * throw application exception, so we can only convey test result back to client
 * through the returned value.
 */

public class Client extends ClientBase {
  @EJB(beanName = "CallbackBean")
  private static CallbackIF bean;

  @EJB(beanName = "Callback2Bean")
  private static Callback2IF bean2;

  @EJB(beanName = "Callback3Bean")
  private static Callback2IF bean3;

  @EJB(beanName = "Callback4Bean")
  private static Callback2IF bean4;

  @EJB(beanName = "singleDefaultInterceptorBean")
  private static Callback2IF singleDefaultInterceptorBean;

  protected Callback2IF getSingleDefaultInterceptorBean() {
    return singleDefaultInterceptorBean;
  }

  protected Callback2IF getBean4() {
    return bean4;
  }

  protected Callback2IF getBean3() {
    return bean3;
  }

  protected Callback2IF getBean2() {
    return bean2;
  }

  protected CallbackIF getBean() {
    return bean;
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props:
   */

  /*
   * @testName: isPostConstructCalledTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: o using annotations: o CallbackListener o PostConstruct o
   * PreDestroy o verify callback methods in handler class are invoked o
   * Callback methods may throw RuntimeException
   */

  /*
   * @testName: isInjectionDoneTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: o using annotations: o CallbackListener o PostConstruct o
   * PreDestroy o Resource o verify dependency injection has occurred when
   * callback method is called o Callback methods may throw RuntimeException
   */

  /*
   * @testName: defaultInterceptorsForCallbackBean1
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: multiple default interceptors are configured for an ejb
   * jar. Verifies they are invoked in the correct order.
   *
   */
  /*
   * @testName: defaultInterceptorsForCallbackBean2
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: multiple default interceptors are configured for an ejb
   * jar. Verifies they are invoked in the correct order.
   *
   */
  /*
   * @testName: defaultInterceptorsForCallbackBean3
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: multiple default interceptors are configured for an ejb
   * jar, but they are excluded for this CallbackBean3 with
   * 
   * @ExcludeDefaultInterceptors on bean class. Verifies they are not invoked.
   *
   */
  /*
   * @testName: defaultInterceptorsForCallbackBean4
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: multiple default interceptors are configured for an ejb
   * jar, but they are excluded for this CallbackBean4 with
   * exclude-default-interceptors in ejb-jar.xml. Verifies they are not invoked.
   * This test is the same as defaultInterceptorsForCallbackBean3 except this
   * one uses descriptor to exclude default interceptors.
   *
   */
  /*
   * @testName: singleDefaultInterceptorJar
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: single default interceptors are configured for an ejb jar.
   * Verifies only one is invoked.
   *
   */
  /*
   * @testName: isInterceptorInjectionDoneForCallbackBean1
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: two default interceptors are configured for an ejb jar.
   * Verifies injections on both interceptor and its superclass are all done by
   * the time PostConstruct is called.
   *
   */
  /*
   * @testName: isInterceptorInjectionDoneForCallbackBean2
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: two default interceptors are configured for an ejb jar.
   * Verifies injections on both interceptor and its superclass are all done by
   * the time PostConstruct is called.
   *
   */
  /*
   * @testName: isInterceptorInjectionDoneForSingleDefaultInterceptorJar
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: single default interceptors are configured for an ejb jar.
   * Verifies only one is invoked.
   *
   */

}
