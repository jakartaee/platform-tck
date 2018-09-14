/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle;

import java.util.logging.Level;

import javax.interceptor.AroundConstruct;
import javax.interceptor.InvocationContext;

import com.sun.ts.tests.ejb30.common.helper.Helper;

public class InterceptorA extends Interceptor9 {

  private static final String simpleName = "InterceptorA";

  @AroundConstruct
  @SuppressWarnings("unused")
  private void aroundConstruct(InvocationContext ic) {
    // Target should be null before proceed()
    Object savedTarget = ic.getTarget();

    try {
      ic.proceed();
    } catch (Exception ex) {
      Helper.getLogger().log(Level.SEVERE, simpleName, ex);
      // Add a AroundConstruct record using addPostConstructRecordFor() to
      // verify their order being invoked
      historySingletonBean.addPostConstructRecordFor(ic.getTarget(),
          ex.toString());
      return;
    }

    if (savedTarget != null) {
      historySingletonBean.addPostConstructRecordFor(ic.getTarget(),
          "NotNullTargetBeforeProceed");
      return;
    }

    Helper.getLogger().logp(Level.FINE, simpleName, "aroundConstruct",
        "Adding aroundConstruct record: " + simpleName);
    // Add a AroundConstruct record using addPostConstructRecordFor() to
    // verify their order being invoked
    historySingletonBean.addPostConstructRecordFor(ic.getTarget(), simpleName);
  }
}
