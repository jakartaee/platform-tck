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
package com.sun.ts.tests.ejb30.lite.packaging.war.enventry;

import static com.sun.ts.tests.ejb30.common.helper.Helper.assertEquals;
import static com.sun.ts.tests.ejb30.common.helper.Helper.assertNotEquals;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.interceptor.InvocationContext;

public class Interceptor1 extends InterceptorBase {

  @Resource // should be able to inject EJBContext
  private EJBContext ejbContext;

  // inject 3 float fields that are declared in ejb-jar.xml#OneBean, TwoBean,
  // and ThreeBean
  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.OneBean/myFloat", description = "declared in ejb-jar.xml#OneBean")
  private float myFloatFromOne;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.TwoBean/myFloat", description = "declared in ejb-jar.xml#TwoBean")
  private float myFloatFromTwo;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.ThreeBean/myFloat", description = "declared in ejb-jar.xml#ThreeBean")
  private float myFloatFromThree;

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct(InvocationContext inv) {
    checkInjections(1);
    try {
      inv.proceed();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  final protected void verify(StringBuilder sb) throws RuntimeException {
    assertNotEquals("Check injected EJBContext ", ejbContext, null);
    assertEquals("Check myFloatFromOne ", (float) 1, myFloatFromOne, sb);
    assertEquals("Check myFloatFromTwo ", (float) 2, myFloatFromTwo, sb);
    assertEquals("Check myFloatFromThree ", (float) 3, myFloatFromThree, sb);
  }
}
