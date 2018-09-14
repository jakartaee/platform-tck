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

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.interceptor.Interceptors;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;

abstract public class BeanBase extends ComponentBase {

  @Resource
  private EJBContext ejbContext;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.Client/myBooleanTrue", description = "declared in web.xml")
  private boolean myBooleanTrueFromWebXml;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.Client/myBooleanFalse")
  private boolean myBooleanFalseFromWebXml;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.OneBean/myBooleanTrue", description = "declared in ejb-jar.xml#OneBean")
  private boolean myBooleanTrue;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.OneBean/myBooleanFalse")
  private boolean myBooleanFalse;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.TwoBean/myBooleanTrue", description = "declared in ejb-jar.xml#TwoBean")
  private boolean myBooleanTrueFromTwo;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.TwoBean/myBooleanFalse")
  private boolean myBooleanFalseFromTwo;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.ThreeBean/myBooleanTrue", description = "declared in ejb-jar.xml#ThreeBean")
  private boolean myBooleanTrueFromThree;

  @Resource(lookup = "java:comp/env/com.sun.ts.tests.ejb30.lite.packaging.war.enventry.ThreeBean/myBooleanFalse")
  private boolean myBooleanFalseFromThree;

  @Resource(lookup = "java:module/ModuleName")
  private String moduleNameInjected;

  @Resource(lookup = "java:app/AppName")
  private String appNameInjected;

  // business methods can be in superclass, and so can method-level interceptors
  @Interceptors(Interceptor3.class)
  public void getInjectionStatusForInterceptors(List<Boolean> sta) {
  }

  @Interceptors(Interceptor3.class)
  public void getInjectionRecordsForInterceptors(List<List<String>> rec) {
  }

  /**
   * Checks injections into BeanBase class only. It cannot call the template
   * method checkInjections, since the verify method in subclass will be called,
   * which verifies injections in subclass. This PostConstruct method should be
   * called before any PostConstruct in subclasses.
   */
  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    StringBuilder sb = new StringBuilder();
    try {
      assertNotEquals("Check injected EJBContext ", ejbContext, null);
      assertEquals("Check myBooleanTrue web.xml: ", true,
          myBooleanTrueFromWebXml, sb);
      assertEquals("Check myBooleanTrue from OneBean", true, myBooleanTrue, sb);
      assertEquals("Check myBooleanTrue from TwoBean", true,
          myBooleanTrueFromTwo, sb);
      assertEquals("Check myBooleanTrue from ThreeBean", true,
          myBooleanTrueFromThree, sb);

      assertEquals("Check myBooleanFalse from web.xml", false,
          myBooleanFalseFromWebXml, sb);
      assertEquals("Check myBooleanFalse from OneBean", false, myBooleanFalse,
          sb);
      assertEquals("Check myBooleanFalse from TwoBean", false,
          myBooleanFalseFromTwo, sb);
      assertEquals("Check myBooleanFalse from ThreeBean", false,
          myBooleanFalseFromThree, sb);
      setInjectionStatusAndRecord(true, sb.toString());
    } catch (Throwable e) {
      setInjectionStatusAndRecord(false, sb.toString(), e);
    }
  }

  public String appNameModuleName(String expected) {
    StringBuilder sb = new StringBuilder();
    String lookup = "java:module/ModuleName";
    String actual = (String) ServiceLocator.lookupNoTry(lookup);
    assertEquals("Check " + lookup, expected, actual, sb);
    assertEquals("Check injected value ", expected, moduleNameInjected, sb);

    lookup = "java:app/AppName";
    actual = (String) ServiceLocator.lookupNoTry(lookup);
    assertEquals("Check " + lookup, expected, actual, sb);
    assertEquals("Check injected value ", expected, appNameInjected, sb);
    return sb.toString();
  }
}
