/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.packaging.war.mbean.interceptor.lifecycleejbcdi;

import java.util.List;

import jakarta.inject.Inject;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;

/**
 * This test directory contains an empty beans.xml that will be packaged in WAR
 * as WEB-INF/beans.xml. This is to verify when CDI is enabled, the overriding
 * of EJB's lifecycle methods still works. The similar tests without beans.xml
 * are in ejb30/lite/interceptor. This test directory is the same as
 * ../lifecyclecdi, except that this test directory uses 2 EJB and @Inject,
 * instead of ManagedBean.
 */
public class Client extends EJBLiteClientBase {

  // @EJB
  @Inject
  private OverrideBean overrideBean;

  // @EJB
  @Inject
  private OverrideWithPostConstructBean overrideWithPostConstructBean;

  /*
   * @testName: overrideWithRegularMethod
   * 
   * @test_Strategy: If a PostConstruct method is overridden, it is no longer
   * invoked. This test override with a non-PostConstruct method.
   */
  public void overrideWithRegularMethod() {
    checkPostConstructRecords(overrideBean, new String[] { "OverrideBean" });
  }

  /*
   * @testName: overrideWithPostConstructBean
   * 
   * @test_Strategy: If a PostConstruct method is overridden, it is no longer
   * invoked. This test override with a PostConstruct method.
   */
  public void overrideWithPostConstructBean() {
    checkPostConstructRecords(overrideWithPostConstructBean,
        new String[] { "OverrideWithPostConstructBean" });
  }

  protected void checkPostConstructRecords(OverrideBeanBase b,
      String[] expectedPostConstruct) {
    List<String> actualPostConstruct = b.getPostConstructRecords();
    appendReason(
        Helper.compareResultList(expectedPostConstruct, actualPostConstruct));
  }
}
