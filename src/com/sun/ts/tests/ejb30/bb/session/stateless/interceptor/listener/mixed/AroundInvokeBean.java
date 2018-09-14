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

package com.sun.ts.tests.ejb30.bb.session.stateless.interceptor.listener.mixed;

import com.sun.ts.tests.ejb30.common.calc.CalculatorException;
import com.sun.ts.tests.ejb30.common.interceptor.InterceptorNoat1;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeBase;
import com.sun.ts.tests.ejb30.common.interceptor.AroundInvokeIF;
import com.sun.ts.tests.ejb30.common.interceptor.Interceptor1;
import java.util.ArrayList;
import javax.interceptor.ExcludeClassInterceptors;
import javax.interceptor.ExcludeDefaultInterceptors;
import javax.interceptor.Interceptors;

@Stateless
@Remote({ AroundInvokeIF.class })
// This bean must use cmt, since it uses setRollbackOnly
@ExcludeDefaultInterceptors
@Interceptors({ Interceptor1.class, Interceptor1.class })
public class AroundInvokeBean extends AroundInvokeBase
    implements AroundInvokeIF {
  @Resource(name = "ejbContext")
  private SessionContext ejbContext;

  public AroundInvokeBean() {
    super();
  }

  // ============ abstract methods from super ==========================
  protected javax.ejb.EJBContext getEJBContext() {
    return this.ejbContext;
  }

  // ============= override business methods from super ================
  @Override
  @ExcludeClassInterceptors
  @Interceptors({ InterceptorNoat1.class,
      MethodLevelOnlyNoopInterceptor.class })
  public ArrayList<String> methodLevelInterceptorMixedTest(
      ArrayList<String> alist) throws CalculatorException {
    return super.methodLevelInterceptorMixedTest(alist);
  }

  @Override
  @Interceptors({ Interceptor1.class, MethodLevelOnlyNoopInterceptor.class })
  public ArrayList<String> methodLevelClassLevelInterceptorMixedTest(
      ArrayList<String> alist) throws CalculatorException {
    return super.methodLevelClassLevelInterceptorMixedTest(alist);
  }

  @Override
  @ExcludeDefaultInterceptors
  @Interceptors({ InterceptorNoat1.class, InterceptorNoat1.class,
      MethodLevelOnlyNoopInterceptor.class })
  public ArrayList<String> repeatedInterceptors(ArrayList<String> alist)
      throws CalculatorException {
    return super.repeatedInterceptors(alist);
  }

  @Override
  @ExcludeDefaultInterceptors
  @Interceptors({ Interceptor1.class, InterceptorNoat1.class })
  public ArrayList<String> interceptorOrderingOverride(ArrayList<String> alist)
      throws CalculatorException {
    return super.interceptorOrderingOverride(alist);
  }
}
