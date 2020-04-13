/*
 * Copyright (c) 2013, 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb32.lite.timer.interceptor.aroundtimeout.singleton.annotated;

import jakarta.ejb.Singleton;
import jakarta.interceptor.AroundTimeout;
import jakarta.interceptor.ExcludeDefaultInterceptors;
import jakarta.interceptor.InvocationContext;

import com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.AroundTimeoutIF;

@Singleton
@ExcludeDefaultInterceptors
public class InvocationContextMethodsBean extends AroundTimeoutBeanBase
    implements AroundTimeoutIF {

  @AroundTimeout
  @Override
  protected Object aroundTimeoutInAroundTimeoutBeanBase(InvocationContext inv)
      throws Exception {
    return invocationContextMethods(inv);
  }
}
