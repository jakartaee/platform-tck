/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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
import jakarta.interceptor.InvocationContext;

import com.sun.ts.tests.ejb30.common.helper.Helper;

//TODO Make some interceptors (e.g., 1, 5, 7) pure java classes without interceptor-
//related annotations.  They will be declared as interceptors in descriptors.
//Copy and modify from the generated full descriptors.
//For both annotated and descriptor subdirs
public class Interceptor1 extends InterceptorBase {
  private static final String simpleName = "Interceptor1";

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct(InvocationContext inv) {
    Helper.getLogger().logp(Level.FINE, simpleName, "postConstruct",
        "Adding postConstruct record: " + simpleName);
    historySingletonBean.addPostConstructRecordFor(inv.getTarget(), simpleName);

    Object t = inv.getTimer();
    Helper.assertEquals(null, null, t);

    try {
      inv.proceed();
    } catch (Exception ex) {
      Helper.getLogger().log(Level.SEVERE, simpleName, ex);
      historySingletonBean.addPostConstructRecordFor(inv.getTarget(),
          ex.toString());
    }
  }
}
