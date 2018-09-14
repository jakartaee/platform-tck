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

package com.sun.ts.tests.ejb30.common.callback;

abstract public class MDBClientBase
    extends com.sun.ts.tests.ejb30.common.messaging.ClientBase
    implements com.sun.ts.tests.ejb30.common.messaging.Constants {

  /*
   * testName: isPostConstructCalledTest
   * 
   * @test_Strategy: o using annotations: o CallbackListener o PostConstruct o
   * PreDestroy o verify callback methods in handler class are invoked o
   * callback methods may, in some cases, named as ejbCreate, ejbRemove
   */
  public void isPostConstructCalledTest() throws Fault {
    sendReceive();
  }

  /*
   * testName: isInjectionDoneTest
   * 
   * @test_Strategy: o using annotations: o CallbackListener o PostConstruct o
   * PreDestroy o Resource o verify dependency injection has not occurred when
   * callback method is called
   */
  public void isInjectionDoneTest() throws Fault {
    sendReceive();
  }

  /*
   * testName: isPostConstructOrPreDestroyCalledTest
   * 
   * @test_Strategy: o using annotations: o CallbackListener o PostConstruct o
   * PreDestroy o apply two/four callback annotations on the same method o
   * callback methods may use arbitrary names
   */
  public void isPostConstructOrPreDestroyCalledTest() throws Fault {
    sendReceive();
  }

}
