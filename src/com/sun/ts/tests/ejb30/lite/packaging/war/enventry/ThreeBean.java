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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateful;
import javax.interceptor.Interceptors;
import static com.sun.ts.tests.ejb30.common.helper.Helper.assertEquals;

@Stateful
@Interceptors({ Interceptor1.class, Interceptor2.class })
public class ThreeBean extends BeanBase {

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.Client/myFloat", description = "declared in web.xml")
  private float myFloatFromWebXml;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.Client/myDouble")
  private double myDoubleFromWebXml;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.OneBean/myFloat", description = "declared in ejb-jar.xml#OneBean")
  private float myFloatFromOneBean;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.OneBean/myDouble")
  private double myDoubleFromOneBean;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.TwoBean/myFloat", description = "declared in ejb-jar.xml#TwoBean")
  private float myFloatFromTwoBean;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.TwoBean/myDouble")
  private double myDoubleFromTwoBean;

  @Resource(description = "declared in ejb-jar.xml#ThreeBean")
  private float myFloat;

  @Resource(description = "declared in ejb-jar.xml#ThreeBean")
  private double myDouble;

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    checkInjections(1);
  }

  @Override
  protected void verify(StringBuilder sb) throws RuntimeException {
    assertEquals("Check myFloatFromOneBean web.xml: ", (float) 8,
        myFloatFromWebXml, sb);
    assertEquals("Check myFloatFromOneBean from OneBean", (float) 1,
        myFloatFromOneBean, sb);
    assertEquals("Check myFloatFromOneBean from TwoBean", (float) 2,
        myFloatFromTwoBean, sb);
    assertEquals("Check myFloatFromOneBean from ThreeBean", (float) 3, myFloat,
        sb);

    assertEquals("Check myDoubleFromOneBean from web.xml", (double) 8,
        myDoubleFromWebXml, sb);
    assertEquals("Check myDoubleFromOneBean from OneBean", (double) 1,
        myDoubleFromOneBean, sb);
    assertEquals("Check myDoubleFromOneBean from TwoBean", (double) 2,
        myDoubleFromTwoBean, sb);
    assertEquals("Check myDoubleFromOneBean from ThreeBean", (double) 3,
        myDouble, sb);
  }
}
