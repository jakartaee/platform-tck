/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.interceptor.common.business;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJBContext;
import javax.interceptor.AroundInvoke;
import javax.interceptor.ExcludeClassInterceptors;
import javax.interceptor.ExcludeDefaultInterceptors;
import javax.interceptor.Interceptors;
import javax.interceptor.InvocationContext;

import com.sun.ts.tests.ejb30.common.appexception.AtCheckedRollbackAppException;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;

public class InterceptorBeanBase implements InterceptorIF {
  private static final String simpleName = "InterceptorBeanBase";

  @AroundInvoke
  protected Object intercepInInterceptorBeanBase(InvocationContext inv)
      throws Exception {
    Helper.getLogger().logp(Level.FINE, simpleName,
        "intercepInInterceptorBeanBase",
        "Adding around-invoke record: " + simpleName + ", this:" + this);
    InterceptorBaseBase.addToHistory(inv, simpleName);
    return inv.proceed();
  }

  // Method-level interceptors can be specified in bean superclasses, whereas
  // class-level interceptors cannot.

  @Interceptors({ Interceptor7.class, Interceptor6.class })
  public void allInterceptors(List<String> history) {
  }

  @Interceptors({ Interceptor6.class, Interceptor7.class })
  @ExcludeDefaultInterceptors
  public void excludeDefaultInterceptors(List<String> history) {
  }

  @Interceptors({ Interceptor6.class, Interceptor7.class })
  @ExcludeClassInterceptors
  public void excludeClassInterceptors(List<String> history) {
  }

  @ExcludeDefaultInterceptors
  @Interceptors({ Interceptor8.class, Interceptor6.class, Interceptor7.class })
  @ExcludeClassInterceptors
  public void overrideInterceptorMethod(List<String> history) {
  }

  public void overrideBeanInterceptorMethod(List<String> history) {
  }

  public void overrideBeanInterceptorMethod2(List<String> history) {
  }

  @Interceptors({ Interceptor1.class })
  // Interceptor1 is declared as a default interceptor in ejb-jar.xml, excluded
  // at class-level, and then reinstated as a method-level interceptor
  public void overrideBeanInterceptorMethod3(List<String> history) {
  }

  @ExcludeClassInterceptors
  @ExcludeDefaultInterceptors
  // ok to exclude class interceptors, though there is no class interceptors
  // ok to exclude default interceptors again on method-level
  public void overrideBeanInterceptorMethod4(List<String> history) {
  }

  @ExcludeClassInterceptors
  @ExcludeDefaultInterceptors
  @Interceptors({ Interceptor1.class, Interceptor2.class })
  public void skipProceed(List<String> history) {
    throw new RuntimeException(
        "Should not reach here.  Invocation should have been intercepted.");
  }

  @ExcludeDefaultInterceptors
  @ExcludeClassInterceptors
  @Interceptors(Interceptor9.class)
  public void applicationExceptionRollback()
      throws AtCheckedRollbackAppException {
    // Interceptor9 aroundInvoke method throws AtCheckedRollbackAppException,
    // which should cause the tx to rollback
  }

  public Map<String, Object> getContextData(List<String> history) {
    EJBContext ejbContext = (EJBContext) ServiceLocator
        .lookupNoTry("java:comp/EJBContext");
    return ejbContext.getContextData();
  }

}
