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

public class Interceptor8 extends InterceptorOverrideBase {
  private static final String simpleName = "Interceptor8";

  @AroundInvoke
  @Override // override the superclass' interceptor method with interceptor
            // method
  protected Object interceptInInterceptorOverrideBase(InvocationContext inv)
      throws Exception {
    Helper.getLogger().logp(Level.FINE, simpleName,
        "interceptInInterceptorOverrideBase",
        "Adding around-invoke record: " + simpleName);
    addToHistory(inv, simpleName);
    return inv.proceed();
  }
}
