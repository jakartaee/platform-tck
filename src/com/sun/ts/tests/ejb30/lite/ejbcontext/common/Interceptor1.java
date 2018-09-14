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

import static com.sun.ts.tests.ejb30.lite.ejbcontext.common.Util.lookupNames;
import static com.sun.ts.tests.ejb30.lite.ejbcontext.common.Util.postConstruct0;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;

public class Interceptor1 {
  @Resource
  private EJBContext ejbContext; // field-inject EJBContext

  private SessionContext sessionContext; // setter-inject SessionContext

  private EJBContext ejbContextFromDescriptorInjection;

  private EJBContext[] injectedEJBContexts = new EJBContext[3];

  private List<String> injectionRecords = new ArrayList<String>();

  private List<EJBContext> lookupValuesInPostConstruct = new ArrayList<EJBContext>();

  private List<EJBContext> lookupValuesInAroundInvoke = new ArrayList<EJBContext>();

  @SuppressWarnings("unused")
  @Resource
  private void setSessionContext(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  @SuppressWarnings("unused")
  @PreDestroy
  private void preDestroy0(InvocationContext inv) {
    Helper.preDestroy(this);
  }

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct(InvocationContext inv) {
    injectedEJBContexts[0] = ejbContext;
    injectedEJBContexts[1] = sessionContext;
    injectedEJBContexts[2] = ejbContextFromDescriptorInjection;
    Helper.getLogger().logp(Level.FINE, "Interceptor1", "postConstruct",
        String.format(
            "ejbContext= %s%n sessionContext= %s%n ejbContextFromDescriptorInjection= %s%n",
            ejbContext, sessionContext, ejbContextFromDescriptorInjection));

    try {
      postConstruct0(injectionRecords, lookupValuesInPostConstruct,
          injectedEJBContexts);
      inv.proceed();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  @SuppressWarnings("unused")
  @AroundInvoke
  private Object intercept(InvocationContext inv) throws Exception {
    String methodName = inv.getMethod().getName();
    Helper.assertEquals("lookupValuesInAroundInvoke must be empty ", 0,
        lookupValuesInAroundInvoke.size());
    if (methodName.equals("getInjectionRecordsInInterceptor")) {
      return injectionRecords;
    } else if (methodName.equals("lookupEJBContextInInterceptor")) {
      for (String nm : lookupNames) { // to be verified in
                                      // lookupEJBContextInInterceptor method
        EJBContext ec = (EJBContext) ServiceLocator.lookupNoTry(nm);
        lookupValuesInAroundInvoke.add(ec);
      }
      String result = lookupEJBContextInInterceptor();
      lookupValuesInAroundInvoke.clear();
      return result;
    }
    return inv.proceed();
  }

  private String lookupEJBContextInInterceptor() {
    StringBuilder sb = new StringBuilder();

    // verify the lookup performed in PostConstruct
    Helper.assertEquals("", lookupNames.length,
        lookupValuesInPostConstruct.size(), sb);
    for (EJBContext ec : lookupValuesInPostConstruct) {
      Helper.assertNotEquals("", null, ec, sb);
    }
    // verify the lookup performed in AroundInvoke
    Helper.assertEquals("", lookupNames.length,
        lookupValuesInAroundInvoke.size(), sb);
    for (EJBContext ec : lookupValuesInAroundInvoke) {
      Helper.assertNotEquals("", null, ec, sb);
    }
    return sb.toString();
  }
}
