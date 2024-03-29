/*
 * Copyright (c) 2022 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb32.lite.timer.schedule.auto.attr.stateless;

import com.sun.ts.tests.ejb30.timer.common.JsfClientBase;

@jakarta.inject.Named("client")
@jakarta.enterprise.context.RequestScoped
public class JsfClient extends JsfClientBase {

  @Override
  protected boolean needToRemoveStatusAndRecords() {
    return false;
  }

  /*
   * @testName: autoTimerInSuperClassNoParam
   * 
   * @test_Strategy: an auto-timer is declared in bean superclass. Its time- out
   * method takes no Timer param.
   */
  public void autoTimerInSuperClassNoParam() {
    passIfRecurringTimeout("autoTimerInSuperClassNoParam");
  }

  /*
   * @testName: autoTimerNonPersistent
   * 
   * @test_Strategy: a non-persistent auto-timer is declared in bean class.
   */
  public void autoTimerNonPersistent() {
    passIfRecurringTimeout("autoTimerNonPersistent");
  }

  /*
   * @testName: autoTimerWithInfo
   * 
   * @test_Strategy: verify the auto timer that was created with @Schedule in
   * bean class with info attr
   */
  public void autoTimerWithInfo() {
    passIfRecurringTimeout("autoTimerWithInfo");
  }

  /*
   * @testName: autoTimerWithoutInfo
   * 
   * @test_Strategy: verify the auto timer that was created with @Schedule in
   * bean class without info attr
   */
  public void autoTimerWithoutInfo() {
    passIfRecurringTimeout("autoTimerWithoutInfo");
  }
}
