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
package com.sun.ts.tests.ejb30.lite.packaging.war.mbean.interceptor.business;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.interceptor.AroundInvoke;
import javax.interceptor.ExcludeClassInterceptors;
import javax.interceptor.ExcludeDefaultInterceptors;
import javax.interceptor.Interceptors;
import javax.interceptor.InvocationContext;

import com.sun.ts.tests.ejb30.lite.interceptor.common.business.Interceptor1;
import com.sun.ts.tests.ejb30.lite.interceptor.common.business.InterceptorBaseBase;
import com.sun.ts.tests.ejb30.lite.interceptor.common.business.InterceptorBeanBase;
import com.sun.ts.tests.ejb30.lite.interceptor.common.business.InterceptorIF;

@ManagedBean
@ExcludeDefaultInterceptors
public class InterceptorOverrideBean extends InterceptorBeanBase
    implements InterceptorIF {

  private static final String simpleName = "InterceptorOverrideBean";

  @Override // override the superclass' interceptor method with a
            // non-interceptor method
  protected Object intercepInInterceptorBeanBase(InvocationContext inv)
      throws Exception {
    return super.intercepInInterceptorBeanBase(inv);
  }

  @SuppressWarnings("unused")
  @AroundInvoke
  private Object intercep(InvocationContext inv) throws Exception {
    InterceptorBaseBase.addToHistory(inv, simpleName);
    return inv.proceed();
  }

  @Override
  @Interceptors({ Interceptor1.class })
  // Interceptor1 is declared as a default interceptor in ejb-jar.xml, excluded
  // at class-level, and then reinstated as a method-level interceptor
  public void overrideBeanInterceptorMethod2(List<String> history) {
    super.overrideBeanInterceptorMethod2(history);
  }

  @Override
  @ExcludeClassInterceptors
  // OK to exclude class interceptors, though there is no class interceptors
  public void overrideInterceptorMethod(List<String> history) {
    super.overrideInterceptorMethod(history);
  }
}
