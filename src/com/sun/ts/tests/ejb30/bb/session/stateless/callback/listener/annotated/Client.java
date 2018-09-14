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

package com.sun.ts.tests.ejb30.bb.session.stateless.callback.listener.annotated;

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
   * @assertion_ids: EJB:JAVADOC:254
   * 
   * @test_Strategy: o using annotations: o CallbackListener o PostConstruct o
   * PreDestroy o verify callback methods in handler class are invoked o
   * Callback methods may throw RuntimeException
   */

  /*
   * @testName: isInjectionDoneTest
   * 
   * @assertion_ids: EJB:JAVADOC:254
   * 
   * @test_Strategy: o using annotations: o CallbackListener o PostConstruct o
   * PreDestroy o Resource o verify dependency injection has occurred when
   * callback method is called o Callback methods may throw RuntimeException
   */

  /*
   * @testName: isPostConstructOrPreDestroyCalledTest
   * 
   * @assertion_ids: EJB:JAVADOC:254
   * 
   * @test_Strategy: o using annotations: o CallbackListener o PostConstruct o
   * PreDestroy o apply two callback annotations on the same method o Callback
   * methods may throw RuntimeException
   */

  /*
   * @testName: invocationContextIllegalStateException
   * 
   * @assertion_ids: EJB:JAVADOC:257; EJB:JAVADOC:261; EJB:JAVADOC:254
   * 
   * @test_Strategy:
   */

  /*
   * @testName: invocationContextIllegalStateException2
   * 
   * @assertion_ids: EJB:JAVADOC:257; EJB:JAVADOC:261; EJB:JAVADOC:254
   * 
   * @test_Strategy:
   */

}
