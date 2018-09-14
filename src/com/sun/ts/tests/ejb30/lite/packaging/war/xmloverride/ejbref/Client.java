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
package com.sun.ts.tests.ejb30.lite.packaging.war.xmloverride.ejbref;

import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import com.sun.ts.tests.ejb30.common.lite.NumberIF;

public class Client extends EJBLiteClientBase {
  @EJB
  // there are 2 beans implementing NumberIF, and so this @EJB is
  // ambiguous. It's fully resolved in web.xml with ejb-link
  private NumberIF overrideBean;

  private TestBean getTestBean() {
    return (TestBean) ServiceLocator
        .lookupNoTry("java:global/" + getModuleName() + "/TestBean");
  }

  /*
   * @testName: resolveByEjbLinkInXml
   * 
   * @test_Strategy: @EJB in TestBean is incomplete. But the corresponding
   * ejb-local-ref in web.xml resolves it with ejb-link.
   */
  public void resolveByEjbLinkInXml() {
    assertEquals("Check correct target EJB is resolved. ", 1,
        overrideBean.getNumber());
  }

  /*
   * @testName: availableInEjb
   * 
   * @test_Strategy: the ejb-local-ref declared in web.xml should be available
   * for lookup in ejb components.
   */
  public void availableInEjb() {
    assertEquals("Look up OverrideBean and invoke: ", 1,
        getTestBean().getNumber());
  }
}
