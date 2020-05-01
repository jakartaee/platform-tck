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
package com.sun.ts.tests.ejb30.lite.view.stateless.annotated;

import jakarta.ejb.Stateless;
import jakarta.interceptor.ExcludeDefaultInterceptors;

import com.sun.ts.tests.ejb30.common.busiface.BusinessLocalIF1;
import com.sun.ts.tests.ejb30.lite.view.common.SuperclassBean;

@Stateless
@ExcludeDefaultInterceptors
// only expose no-interface view. BusinessLocalIF1, which is implemented by
// SuperclassBean is not exposed as business interface.
public class SubclassExtendsBeanBean extends SuperclassBean {
  @Override
  public String[] businessMethodLocal1(String[] s) {
    try {
      sessionContext.getBusinessObject(BusinessLocalIF1.class);
    } catch (IllegalStateException e) {
      s[0] = IllegalStateException.class.getSimpleName();
    }

    Class<SubclassExtendsBeanBean> intf = SubclassExtendsBeanBean.class;
    SubclassExtendsBeanBean b = sessionContext.getBusinessObject(intf);
    b.remove();
    s[1] = intf.getSimpleName();
    return s;
  }
}
