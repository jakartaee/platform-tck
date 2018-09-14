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
package com.sun.ts.tests.ejb30.lite.ejbcontext.common;

import java.util.List;
import javax.ejb.EJBContext;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;

public class Util {
  static String[] lookupNames = { "java:comp/EJBContext",
      "java:comp/env/ejbContextFromDescriptorInjection",
      "java:comp/env/com.sun.ts.tests.ejb30.lite.ejbcontext.common.EJBContextBeanBase/ejbContext",
      "java:comp/env/com.sun.ts.tests.ejb30.lite.ejbcontext.common.EJBContextBeanBase/sessionContext",
      "java:comp/env/com.sun.ts.tests.ejb30.lite.ejbcontext.common.Interceptor1/ejbContext",
      "java:comp/env/com.sun.ts.tests.ejb30.lite.ejbcontext.common.Interceptor1/sessionContext" };

  // called by bean base and interceptor class
  public static void postConstruct0(List<String> injectionRecords,
      List<EJBContext> lookupValuesInPostConstruct, EJBContext... ecs) {
    for (EJBContext ec : ecs) {
      injectionRecords.add(Helper.assertNotEquals(
          "Check field-, setter-, or descriptor-injected EJBContext ", null,
          ec));
    }
    for (String nm : lookupNames) { // to be verified in a business method
      EJBContext ec = (EJBContext) ServiceLocator.lookupNoTry(nm);
      lookupValuesInPostConstruct.add(ec);
    }
  }

  // called by concrete bean classes
  public static void postConstruct1(List<String> injectionRecords,
      EJBContext... ecs) {
    Helper.assertEquals("", 3, injectionRecords.size()); // 3 records inserted
                                                         // by bean base
    for (EJBContext ec : ecs) {
      injectionRecords.add(Helper.assertNotEquals(
          "Check field-, setter-, or descriptor-injected EJBContext ", null,
          ec));
    }
  }
}
