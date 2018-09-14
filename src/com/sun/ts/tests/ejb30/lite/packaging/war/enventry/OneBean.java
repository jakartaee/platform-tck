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
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import static com.sun.ts.tests.ejb30.common.helper.Helper.assertEquals;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
@Interceptors({ Interceptor1.class, Interceptor2.class })
public class OneBean extends BeanBase {

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.Client/myByte", description = "declared in web.xml")
  private byte myByteFromWebXml;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.Client/myInt")
  private int myIntFromWebXml;

  @Resource(description = "declared in ejb-jar.xml#OneBean")
  private byte myByte;

  @Resource(description = "declared in ejb-jar.xml#OneBean")
  private int myInt;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.TwoBean/myByte", description = "declared in ejb-jar.xml#TwoBean")
  private byte myByteFromTwo;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.TwoBean/myInt")
  private int myIntFromTwo;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.ThreeBean/myByte", description = "declared in ejb-jar.xml#ThreeBean")
  private byte myByteFromThree;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.ThreeBean/myInt")
  private int myIntFromThree;

  // injected in ejb-jar.xml
  private boolean myBooleanTrue2;

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    checkInjections(1);
  }

  @Override
  protected void verify(StringBuilder sb) throws RuntimeException {
    assertEquals("Check myByte web.xml: ", (byte) 8, myByteFromWebXml, sb);
    assertEquals("Check myByte from OneBean", (byte) 1, myByte, sb);
    assertEquals("Check myByte from TwoBean", (byte) 2, myByteFromTwo, sb);
    assertEquals("Check myByte from ThreeBean", (byte) 3, myByteFromThree, sb);

    assertEquals("Check myInt from web.xml", 8, myIntFromWebXml, sb);
    assertEquals("Check myInt from OneBean", 1, myInt, sb);
    assertEquals("Check myInt from TwoBean", 2, myIntFromTwo, sb);
    assertEquals("Check myInt from ThreeBean", 3, myIntFromThree, sb);

    assertEquals("Check myBooleanTrue2", true, myBooleanTrue2, sb);
  }
}
