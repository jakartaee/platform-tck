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
package com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle;

import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import com.sun.ts.tests.ejb30.common.helper.Helper;

abstract public class InterceptorBaseBase {
  // we cannot use a getSimpleName() method, since it will always return the
  // simpleName of the subclass. So we have to declare a static simpleName
  // in each layer of the class hierarchy
  private static final String simpleName = "InterceptorBaseBase";

  @EJB(name = "historySingletonBean") // share the same name as in
                                      // InterceptorBeanBase
  protected HistorySingletonBean historySingletonBean;

  @PostConstruct
  protected void postConstructInInterceptorBaseBase(InvocationContext inv) {
    Helper.getLogger().logp(Level.FINE, simpleName,
        "postConstructInInterceptorBaseBase",
        "Adding postConstruct record: " + simpleName + ", this: " + this);
    historySingletonBean.addPostConstructRecordFor(inv.getTarget(), simpleName);
    try {
      inv.proceed();
    } catch (Exception ex) {
      Helper.getLogger().log(Level.SEVERE, simpleName, ex);
      historySingletonBean.addPostConstructRecordFor(inv.getTarget(),
          ex.toString());
    }
  }

  @PostActivate
  protected void postActivate(InvocationContext inv) {
    Helper.getLogger().logp(Level.FINE, simpleName, "postActivate",
        "PostActivate method, current class " + this);
  }

  @PrePassivate
  @PreDestroy
  protected void preDestroy(InvocationContext inv) {
    Helper.getLogger().logp(Level.FINE, simpleName, "preDestroy",
        "PreDestroy/PrePassivate method, current class " + this);
  }

  // to verify lifecycle and business interceptor methods can co-exist
  @SuppressWarnings("unused")
  @AroundInvoke
  private Object interceptInInterceptorBaseBase(InvocationContext inv)
      throws Exception {
    Helper.getLogger().logp(Level.FINE, simpleName,
        "interceptInInterceptorBaseBase",
        "Adding around-invoke record: " + simpleName + ", this: " + this);
    historySingletonBean.addAroundInvokeRecord(simpleName);
    return inv.proceed();
  }
}
