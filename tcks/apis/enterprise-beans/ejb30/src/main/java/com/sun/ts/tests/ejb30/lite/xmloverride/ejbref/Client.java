/*
 * Copyright (c) 2010, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.xmloverride.ejbref;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import com.sun.ts.tests.ejb30.common.lite.NumberIF;

public class Client extends EJBLiteClientBase {

  private TestBean getTestBean() {
    return (TestBean) ServiceLocator
        .lookupNoTry("java:global/" + getModuleName() + "/TestBean");
  }

  /*
   * @testName: resolveByEjbLinkInXml
   * 
   * @test_Strategy: @EJB in TestBean is incomplete. But the corresponding
   * ejb-local-ref in ejb-jar.xml resolves it with ejb-link.
   */
  public void resolveByEjbLinkInXml() {
    assertEquals("Check correct target EJB is resolved. ", 1,
        getTestBean().getNumber());
  }

  /*
   * @testName: availableInWebComponent
   * 
   * @test_Strategy: the ejb-local-ref declared in ejb-jar.xml should be
   * available for lookup in web components.
   */
  public void availableInWebComponent() {
    if (getContainer() != null) {
      return; // skip if running in ejbembed
    }
    NumberIF overrideBean = (NumberIF) ServiceLocator.lookupNoTry(
        "java:comp/env/com.sun.ts.tests.ejb30.lite.xmloverride.ejbref.TestBean/overrideBean");
    assertEquals("Look up OverrideBean and invoke: ", 1,
        overrideBean.getNumber());
  }

  /*
   * @testName: overrideLookup
   * 
   * @test_Strategy: lookup-name in ejb-jar.xml overrides lookup attr in @EJB
   */
  public void overrideLookup() {
    assertEquals("Check correct target EJB is resolved. ", 1,
        getTestBean().overrideLookup());
  }

  /*
   * @testName: overrideInterfaceType
   * 
   * @test_Strategy: <local> in ejb-jar.xml overrides beanInterface attr in @EJB
   */
  public void overrideInterfaceType() {
    assertEquals("Check correct target EJB is resolved. ", 1,
        getTestBean().overrideInterfaceType());
  }

  /*
   * @testName: overrideBeanName
   * 
   * @test_Strategy: <ejb-link> in ejb-jar.xml overrides beanName attr in @EJB
   */
  public void overrideBeanName() {
    assertEquals("Check correct target EJB is resolved. ", 1,
        getTestBean().overrideBeanName());
  }
}
