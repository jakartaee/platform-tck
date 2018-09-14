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
package com.sun.ts.tests.ejb30.lite.interceptor.common.business;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import java.util.logging.Level;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

//TODO Make some interceptors (e.g., 1, 5, 7) pure java classes without interceptor-
//related annotations.  They will be declared as interceptors in descriptors.
//Copy and modify from the generated full descriptors.
//For both annotated and descriptor subdirs
public class Interceptor1 extends InterceptorBase {
  private static final String simpleName = "Interceptor1";

  @SuppressWarnings("unused")
  @AroundInvoke
  private Object intercept(InvocationContext inv) throws Exception {
    Helper.getLogger().logp(Level.FINE, simpleName, "intercept",
        "Adding around-invoke record: " + simpleName);
    addToHistory(inv, simpleName);
    String methodName = inv.getMethod().getName();
    if (methodName.equals("skipProceed")) {
      Helper.getLogger().logp(Level.INFO, simpleName, "intercept",
          "Skip InvocationContext.proceed for business method " + methodName);
      return null;
    }
    return inv.proceed();
  }
}
