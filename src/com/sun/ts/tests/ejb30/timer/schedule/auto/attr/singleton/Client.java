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
package com.sun.ts.tests.ejb30.timer.schedule.auto.attr.singleton;

public class Client
    extends com.sun.ts.tests.ejb30.timer.schedule.auto.attr.stateless.Client {

  // some timeout records are recorded before starting the test (e.g,
  // postConstruct test)
  // So override setup method not to remove status.
  @Override
  protected boolean needToRemoveStatusAndRecords() {
    return false;
  }

  /*
   * @testName: postConstruct
   * 
   * @test_Strategy: check the number of auto timers inside bean's
   * post-construct method. All auto timers should have been created when
   * post-construct method is invoked.
   */
  public void postConstruct() {
    passIfTimeout("postConstruct");
  }

  /*
   * @testName: autoTimerInSuperClassNoParam
   * 
   * @test_Strategy: an auto-timer is declared in bean superclass. Its time- out
   * method takes no Timer param.
   */

  /*
   * @testName: autoTimerNonPersistent
   * 
   * @test_Strategy: a non-persistent auto-timer is declared in bean class along
   * with a persistent auto-timer.
   */

  /*
   * @testName: autoTimerPersistent
   * 
   * @test_Strategy: a non-persistent auto-timer is declared in bean class along
   * with a persistent auto-timer.
   */

  /*
   * @testName: autoTimerWithInfo
   * 
   * @test_Strategy: verify the auto timer that was created with @Schedule in
   * bean class with info attr
   */

  /*
   * @testName: autoTimerWithoutInfo
   * 
   * @test_Strategy: verify the auto timer that was created with @Schedule in
   * bean class without info attr
   */
}
