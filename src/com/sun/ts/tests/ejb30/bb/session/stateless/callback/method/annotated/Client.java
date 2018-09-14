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

package com.sun.ts.tests.ejb30.bb.session.stateless.callback.method.annotated;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.ejb30.common.callback.Callback2IF;
import com.sun.ts.tests.ejb30.common.callback.CallbackIF;
import com.sun.ts.tests.ejb30.common.callback.ClientBase3;
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;

/**
 * A test client for callback methods. Note that since callback methods cannot
 * throw application exception, so we can only convey test result back to client
 * through the returned value.
 */

public class Client extends ClientBase3 {
  @EJB(beanName = "CallbackBean")
  private static CallbackIF bean;

  @EJB(beanName = "Callback2Bean")
  private static Callback2IF bean2;

  @EJB(beanName = "SessionBeanCallbackBean")
  private static CallbackIF sessionBeanCallbackBean;

  // used only in this class, not in any superclass
  @EJB(beanName = "EjbCreateCallbackBean")
  private static CallbackIF ejbCreateCallbackBean;

  protected Callback2IF getBean2() {
    return bean2;
  }

  protected CallbackIF getBean() {
    return bean;
  }

  protected CallbackIF getSessionBean() {
    return sessionBeanCallbackBean;
  }

  @PostConstruct
  private static void postConstruct() {
    addPostConstructCall(CLIENT);
    // check injected fields
    if (bean != null) {
      addInjectedField(bean);
    } else {
      TLogger.log("WARNING: Client.bean has not been "
          + "initialized when checking inside Client.postConstruct()");
    }
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
   * Callback methods may throw RuntimeException o callback methods may, in some
   * cases, named as ejbCreate, ejbRemove
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
   * @testName: isPostConstructOrPreDestroyCalledTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: o using annotations: o CallbackListener o PostConstruct o
   * PreDestroy o apply two callback annotations on the same method o Callback
   * methods may throw RuntimeException o callback methods may use arbitrary
   * names
   */

  /*
   * @testName: isPostConstructCalledSessionBeanTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: o using annotations: o CallbackListener o PostConstruct o
   * PreDestroy o verify callback methods in handler class are invoked o
   * callback methods may, in some cases, named as ejbCreate, ejbRemove
   */

  /*
   * @testName: isInjectionDoneSessionBeanTest
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: o using annotations: o CallbackListener o PostConstruct o
   * PreDestroy o Resource o verify dependency injection has occurred when
   * callback method is called
   */

  //////////////////////////////////////////////////////////////////////
  // tests inherited from ClientBase3
  //////////////////////////////////////////////////////////////////////
  /*
   * @testName: appclientPostConstructCallsCount
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */

  /*
   * @testName: appclientPostConstructCallOrder
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */

  /*
   * @testName: appclientInjectionCompleteInPostConstruct
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */

  /*
   * @testName: ejbCreateTreatedAsPostConstruct
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: * ejbCreate() in slsb is treated as PostConstruct, even
   * when it's not annotated with @PostConstruct, or when the bean class does
   * not implement javax.ejb.SessionBean interface. (ejb3 spec section 4.5.8,
   * 4.3.10.2)
   * 
   * callback methods can be in the superclass of the bean class.
   *
   * See bug 6486752
   */
  public void ejbCreateTreatedAsPostConstruct() throws Fault {
    boolean b = ejbCreateCallbackBean.isPostConstructCalledTest();
    if (b) {
      TLogger.log(
          "Got expected result.  ejbCreate method is called as a PostConstruct method.");
    } else {
      throw new Fault(
          "ejbCreate method is not called as a PostConstruct method in "
              + ejbCreateCallbackBean);
    }
  }

  /*
   * @testName: injectionDoneInEjbCreate
   * 
   * @assertion_ids:
   * 
   * @test_Strategy:
   */
  public void injectionDoneInEjbCreate() throws Fault {
    boolean b = ejbCreateCallbackBean.isInjectionDoneTest();
    if (b) {
      TLogger.log(
          "Got expected result.  ejbCreate method is called as a PostConstruct method and injections has completed.");
    } else {
      throw new Fault("injection has not completed when ejbCreate is called in "
          + ejbCreateCallbackBean);
    }
  }
}
