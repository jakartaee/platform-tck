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
package com.sun.ts.tests.ejb30.lite.xmloverride.resource;

import java.net.MalformedURLException;
import java.net.URL;
import javax.annotation.Resource;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;

public class Client extends EJBLiteClientBase {
  @Resource(name = "unmappedEnvEntry")
  private Integer unmappedEnvEntry = 1;

  private TestBean getTestBean() {
    return (TestBean) ServiceLocator
        .lookupNoTry("java:global/" + getModuleName() + "/TestBean");
  }

  /*
   * @testName: unmappedEnvEntry
   *
   * @test_Strategy: this @Resource has no mapped <env-entry> in descriptor, so
   * the default value of the field is unchanged.
   */
  public void unmappedEnvEntry() {
    assertEquals("Check correct the default value is retained. ", 1,
        unmappedEnvEntry);
  }
}
