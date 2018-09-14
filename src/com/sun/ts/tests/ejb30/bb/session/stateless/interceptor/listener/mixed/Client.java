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

package com.sun.ts.tests.ejb30.bb.session.stateless.interceptor.listener.mixed;

import com.sun.javatest.Status;
import com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeIF;

import com.sun.ts.tests.ejb30.common.interceptor.ClientBase;
import javax.ejb.EJB;

public class Client extends ClientBase {
  @EJB(name = "AroundInvokeBean")
  static private AroundInvokeIF bean;

  protected AroundInvokeIF getBean() {
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
   * @testName: methodLevelInterceptorMixedTest
   * 
   * @test_Strategy: o method level interceptors may be specified
   * with @Interceptors or in descriptor. The one specified in descriptor is
   * invoked after the ones specified with annotations. Both default
   * interceptors and class level interceptors are excluded.
   */

  /*
   * @testName: methodLevelClassLevelInterceptorMixedTest
   * 
   * @test_Strategy: o method level interceptors may be specified
   * with @Interceptors or in descriptor. The one specified in descriptor is
   * invoked after the ones specified with annotations. Default interceptors are
   * excluded, but class level interceptors are included.
   */

  /*
   * @testName: repeatedInterceptors
   * 
   * @test_Strategy: o method level interceptors may be specified
   * with @Interceptors or in descriptor. Interceptors are additive.
   */

  /*
   * @testName: interceptorOrderingOverride
   * 
   * @test_Strategy: o method level interceptors may be specified
   * with @Interceptors or in descriptor. Interceptor order may be overridden
   * with <interceptor-order>
   */

}
