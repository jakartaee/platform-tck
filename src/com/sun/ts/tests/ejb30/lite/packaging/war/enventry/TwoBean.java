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
import javax.ejb.Singleton;
import javax.interceptor.Interceptors;
import static com.sun.ts.tests.ejb30.common.helper.Helper.assertEquals;

@Singleton
@Interceptors({ Interceptor1.class, Interceptor2.class })
public class TwoBean extends BeanBase {

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.Client/myShort", description = "declared in web.xml")
  private short myShortFromWebXml;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.Client/myLong")
  private long myLongFromWebXml;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.OneBean/myShort", description = "declared in ejb-jar.xml#OneBean")
  private short myShortFromOneBean;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.OneBean/myLong")
  private long myLongFromOneBean;

  @Resource(description = "declared in ejb-jar.xml#TwoBean")
  private short myShort;

  @Resource(description = "declared in ejb-jar.xml#TwoBean")
  private long myLong;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.ThreeBean/myShort", description = "declared in ejb-jar.xml#ThreeBean")
  private short myShortFromThreeBean;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.ThreeBean/myLong")
  private long myLongFromThreeBean;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.OneBean/myBooleanTrue2")
  private boolean myBooleanTrue2FromOneBean;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.Client/myBooleanTrue2")
  private boolean myBooleanTrue2FromWebXml;

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    checkInjections(1);
  }

  @Override
  protected void verify(StringBuilder sb) throws RuntimeException {
    assertEquals("Check myLongFromOneBean web.xml: ", 8L, myLongFromWebXml, sb);
    assertEquals("Check myLongFromOneBean from OneBean", 1L, myLongFromOneBean,
        sb);
    assertEquals("Check myLongFromOneBean from TwoBean", 2L, myLong, sb);
    assertEquals("Check myLongFromOneBean from ThreeBean", 3L,
        myLongFromThreeBean, sb);

    assertEquals("Check myShortFromOneBean from web.xml", (short) 8,
        myShortFromWebXml, sb);
    assertEquals("Check myShortFromOneBean from OneBean", (short) 1,
        myShortFromOneBean, sb);
    assertEquals("Check myShortFromOneBean from TwoBean", (short) 2, myShort,
        sb);
    assertEquals("Check myShortFromOneBean from ThreeBean", (short) 3,
        myShortFromThreeBean, sb);

    assertEquals("Check myBooleanTrue2FromOneBean", true,
        myBooleanTrue2FromOneBean, sb);
    assertEquals("Check myBooleanTrue2FromWebXml", true,
        myBooleanTrue2FromWebXml, sb);
  }
}
