/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.nointerface.descriptor;

import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.lite.nointerface.annotated.HasInterface;

public class Client
    extends com.sun.ts.tests.ejb30.lite.nointerface.annotated.ClientBase {
  @SuppressWarnings("unused")
  @EJB(name = "ejb/stateless", beanName = "NoInterfaceStatelessBean")
  private void setStateless(NoInterfaceStatelessBean stateless) {
    this.stateless = stateless;
  }

  @SuppressWarnings("unused")
  @EJB(name = "ejb/stateful", beanName = "NoInterfaceStatefulBean")
  private void setStateful(NoInterfaceStatefulBean stateful) {
    this.stateful = stateful;
  }

  @SuppressWarnings("unused")
  @EJB(name = "ejb/statefulToBeRemoved", beanName = "NoInterfaceStatefulBean")
  private void setStatefulToBeRemoved(
      NoInterfaceStatefulBean statefulToBeRemoved) {
    this.statefulToBeRemoved = statefulToBeRemoved;
  }

  @SuppressWarnings("unused")
  @EJB(name = "ejb/singleton", beanName = "NoInterfaceSingletonBean")
  private void setSingleton(NoInterfaceSingletonBean singleton) {
    this.singleton = singleton;
  }

  @SuppressWarnings("unused")
  @EJB(beanName = "HasInterfaceSingletonBean")
  private void setHasInterfaceSingleton(HasInterface hasInterfaceSingleton) {
    this.hasInterfaceSingleton = hasInterfaceSingleton;
  }

  /*
   * @testName: nonBusinessMethods
   * 
   * @test_Strategy: Invoking non-public methods results in EJBException.
   */

  /*
   * @testName: invokeRemovedStateful
   * 
   * @test_Strategy: Invoking a removed stateful no-interface bean results in
   * javax.ejb.NoSuchEJBException.
   */

  /*
   * @testName: passAsParam
   * 
   * @test_Strategy: no-interface view bean reference can be passed by param of
   * any local business interface or no-interface method. Using varargs...
   */

  /*
   * @testName: passAsReturn
   * 
   * @test_Strategy: no-interface view bean reference can be passed by param of
   * any local business interface or no-interface method. Using covariant return
   * types. Also tests that injected no-interface beans can be looked up via
   * jndi.
   */

  /*
   * @testName: passEnumAsParams
   * 
   * @test_Strategy: pass (NumberEnum, NumberIF) to each bean, which returns the
   * sum.
   */

  /*
   * @testName: passEnumAsReturn
   * 
   * @test_Strategy: pass (NumberEnum, NumberIF) to each bean, which returns the
   * sum.
   */
}
