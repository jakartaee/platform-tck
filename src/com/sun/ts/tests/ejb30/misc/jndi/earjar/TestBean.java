/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.misc.jndi.earjar;

import static com.sun.ts.tests.ejb30.common.helper.Helper.assertEquals;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Singleton;

import com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.lite.basic.common.GlobalJNDITest;
import com.sun.ts.tests.ejb30.lite.basic.stateless.BasicBean;

@Singleton
public class TestBean implements TestIF {
  @Resource(lookup = "java:module/ModuleName")
  private String moduleNameInjected;

  @Resource(lookup = "java:app/AppName")
  private String appNameInjected;

  @EJB(lookup = "java:global/misc_jndi_earjar/misc_jndi_earjar_ejb/BasicBean!com.sun.ts.tests.ejb30.lite.basic.stateless.BasicBean")
  private BasicBean basicBean;

  public String globalJNDI(String appName, String moduleName) {
    return basicBean.globalJNDI(appName, moduleName);
  }

  public String appJNDI(String moduleName) {
    return basicBean.appJNDI(moduleName);
  }

  public String moduleJNDI() {
    return basicBean.moduleJNDI();
  }

  public int add(int a, int b) {
    return a + b;
  }

  public String globalJNDIHelloEJB(String appName, String moduleName,
      String beanName, Class<?> intf) {
    String lookupName = GlobalJNDITest.getGlobalJNDIName(appName, moduleName,
        beanName, intf);
    HelloRemoteIF h = (HelloRemoteIF) ServiceLocator.lookupNoTry(lookupName);
    return h.getMessage().toString();
  }

  public String appNameModuleName() {
    StringBuilder sb = new StringBuilder();
    String lookup = "java:module/ModuleName";
    String expected = TestIF.EJB_MODULE_NAME;
    String actual = (String) ServiceLocator.lookupNoTry(lookup);
    assertEquals("Check " + lookup, expected, actual, sb);
    assertEquals("Check injected value ", expected, moduleNameInjected, sb);

    lookup = "java:app/AppName";
    expected = TestIF.APP_NAME;
    actual = (String) ServiceLocator.lookupNoTry(lookup);
    assertEquals("Check " + lookup, expected, actual, sb);
    assertEquals("Check injected value ", expected, appNameInjected, sb);
    return sb.toString();
  }

}
