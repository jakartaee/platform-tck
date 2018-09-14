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
package com.sun.ts.tests.ejb30.lite.packaging.war.mbean.interceptor.lifecycle;

import java.util.logging.Level;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.interceptor.Interceptors;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor1;
import com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor2;
import com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor3;
import com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor4;
import com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor5;
import com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.Interceptor8;
import com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.InterceptorBeanBase;
import com.sun.ts.tests.ejb30.lite.interceptor.common.lifecycle.InterceptorIF;

//2, 1 & 3 are prepended here to replace default interceptors.  In other
//test directories (e.g., lite/interceptor/singleton/lifecycle/annotated,
//2, 1 & 3 are specified as default interceptors.
@ManagedBean("InterceptorBean")
@Interceptors({ Interceptor2.class, Interceptor1.class, Interceptor3.class,
    Interceptor5.class, Interceptor4.class, Interceptor8.class })
public class InterceptorBean extends InterceptorBeanBase
    implements InterceptorIF {
  private static final String simpleName = "InterceptorBean";

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    Helper.getLogger().logp(Level.FINE, simpleName, "postConstruct",
        "Adding to postConstruct record: " + simpleName);
    historySingletonBean.addPostConstructRecordFor(this, simpleName);
  }
}
