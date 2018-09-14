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
package com.sun.ts.tests.ejb30.lite.packaging.war.servletcontextlistener;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import com.sun.ts.tests.ejb30.lite.basic.common.Basic1IF;

public class Client extends EJBLiteClientBase {
  private List<Basic1IF> beans = new ArrayList<Basic1IF>();

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    beans.add(
        (Basic1IF) ServiceLocator.lookupByShortNameNoTry("ejb/statelessBean"));
    beans.add(
        (Basic1IF) ServiceLocator.lookupByShortNameNoTry("ejb/statefulBean"));
    beans.add(
        (Basic1IF) ServiceLocator.lookupByShortNameNoTry("ejb/singletonBean"));
  }

  /*
   * @testName: onlyOneTest
   * 
   * @test_Strategy: The test logic is in ServletContextListener. The webapp
   * initialization will fail if test expectations are not met, assuming
   * contextInitialized method is invoked. This http request by this test method
   * does nothing else. Since a ServletContextListener is only called once per
   * webapp loading, there should be only one test for this directory. Also
   * verifies that ejb injected into TestServletContextListener can also be
   * looked up inside the test client (both are web components).
   */
  public void onlyOneTest() {
    appendReason("Test logics are in ServletContextListener class");
    TestServletContextListener.add(beans);
  }
}
