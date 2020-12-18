/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.singleton.annotated;

import com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.AroundTimeoutBeanBase;
import com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.AroundTimeoutIF;
import com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.Interceptor3;
import com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.Interceptor4;
import com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.InterceptorBase;

import jakarta.ejb.Singleton;
import jakarta.interceptor.AroundTimeout;
import jakarta.interceptor.Interceptors;
import jakarta.interceptor.InvocationContext;

@Singleton
@Interceptors({ Interceptor4.class, Interceptor3.class })
public class AroundTimeoutOverrideBean extends AroundTimeoutBeanBase
    implements AroundTimeoutIF {
  private static final String simpleName = "AroundTimeoutOverrideBean";

  @SuppressWarnings("unused")
  @AroundTimeout
  private Object aroundTimeoutInAroundTimeoutOverrideBean(InvocationContext inv)
      throws Exception {
    return InterceptorBase.handleAroundTimeout(inv, simpleName, this,
        "aroundTimeoutInAroundTimeoutOverrideBean");
  }
}
