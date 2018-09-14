/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

import javax.ejb.Singleton;
import javax.interceptor.InvocationContext;

import com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.AroundTimeoutIF;
import com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.InterceptorBase;
import com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.MethodOverrideBeanBase;

/**
 * This class overrides the @AroundTimeout method in the superclass. The
 * overriding method in this class is also a @AroundTimeout method. Verifies
 * that the superclass' @AroundTimeout is disabled.
 */
@Singleton
public class MethodOverride2Bean extends MethodOverrideBeanBase
    implements AroundTimeoutIF {
  private static final String simpleName = "MethodOverride2Bean";

  @Override
  protected Object aroundTimeoutInMethodOverrideBeanBase(InvocationContext inv)
      throws Exception {
    return InterceptorBase.handleAroundTimeout(inv, simpleName, this,
        "aroundTimeoutInMethodOverrideBean");
  }
}
