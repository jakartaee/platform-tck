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
package com.sun.ts.tests.ejb30.lite.view.common;

import javax.ejb.Stateless;
import javax.interceptor.ExcludeDefaultInterceptors;

import com.sun.ts.tests.ejb30.common.busiface.BusinessLocalIF1;

/**
 * This bean class may be extended by other bean classes, Stateless, Stateful,
 * or Singleton. When inherited by other bean classes, it is only
 * implementation- inheritance, not component inheritance. The
 * component-defining metadata is not inherited. BusinessLocalIF1 does not
 * automatically become the business interface of the subclass bean class.
 */
@Stateless
@ExcludeDefaultInterceptors
public class SuperclassBean extends SuperclassBeanBase
    implements BusinessLocalIF1 {

  @Override
  public String[] businessMethodLocal1(String[] s) {
    Class<SuperclassBean> intf = SuperclassBean.class;
    try {
      @SuppressWarnings("unused")
      SuperclassBean b = sessionContext.getBusinessObject(intf);
    } catch (IllegalStateException e) {
      s[0] = IllegalStateException.class.getSimpleName();
    }

    Class<BusinessLocalIF1> intf2 = BusinessLocalIF1.class;
    BusinessLocalIF1 b2 = sessionContext.getBusinessObject(intf2);
    b2.remove();
    s[1] = intf.getSimpleName();
    return s;
  }
}
