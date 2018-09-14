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

package com.sun.ts.tests.ejb30.bb.session.stateless.callback.inheritance.descriptor;

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

  protected Callback2IF getSingleDefaultInterceptorBean() {
    return null;
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
   * @testName: inheritanceInterceptorsForCallbackBean3
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: multiple default interceptors are configured for an ejb
   * jar. class-level interceptors and in-bean lifecycle methods are also
   * defined, and both class-level interceptors and bean class have superclass
   * and super-superclass. Verify these all are invoked in the correct order.
   *
   */

  /*
   * @testName: inheritanceInterceptorsForCallbackBean1
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: A bean that overrides and thus disables all lifecycle
   * callback methods in its superclasses. Its class-level interceptor,
   * InterceptorH, also overrides and disables its superclasses' lifecycle
   * callback methods. In both cases, overriding methods themselves are not
   * lifecycle methods.
   */

  /*
   * @testName: inheritanceInterceptorsForCallbackBean2
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: A bean that overrides and thus disables all lifecycle
   * callback methods in its superclasses. Its class-level interceptor,
   * InterceptorH, also overrides and disables its superclasses' lifecycle
   * callback methods. In both cases, one overriding method is
   * still @PostConstruct method, and the other is re-annotated as @PreDestroy
   * method.
   */

  /*
   * @testName: inheritanceInterceptorsForCallbackBean4
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: A bean that does not contain any lifecycle methods. Its
   * superclass contains lifecycle methods, and also overrides/disables
   * lifecycle methods in ITS superclasses.
   */
}
