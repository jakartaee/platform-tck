/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.interceptor.singleton.lifecycle.annotated;

import com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.InterceptorBeanBase;
import com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.InterceptorIF;
import javax.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import javax.interceptor.ExcludeDefaultInterceptors;

@Singleton
@ExcludeDefaultInterceptors
public class InterceptorOverrideBean extends InterceptorBeanBase
    implements InterceptorIF {

  private static final String simpleName = "InterceptorOverrideBean";

  @PostConstruct
  protected void postConstruct() {
    historySingletonBean.addPostConstructRecordFor(this, simpleName);
  }

  @Override // override the superclass' PostConstruct method with a
            // non-PostConstruct method
  final protected void postConstructInInterceptorBeanBase() {
    throw new RuntimeException(
        "Overriding the superclass' PostConstruct method with a non-PostConstruct method, and neither is to be called.");
  }
}
