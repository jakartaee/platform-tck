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
package com.sun.ts.tests.ejb30.lite.nointerface.annotated;

import java.util.Arrays;

import javax.annotation.Resource;
import javax.ejb.SessionContext;

import com.sun.ts.tests.ejb30.common.lite.NumberEnum;
import com.sun.ts.tests.ejb30.common.lite.NumberIF;

abstract public class BeanBase {
  @Resource
  protected SessionContext sessionContext;

  abstract public BeanBase passAsReturn();

  // declared or annotated as remove-method in stateful subclass
  public void remove() {
  }

  // just a regular method
  public void remove(boolean notRemoveMethod) {
  }

  protected String nonBusinessMethod() {
    // throw new IllegalStateException("Not a business method.");
    // the above exception will cause the client to receive an EJBException,
    // thus making the test pass. So return the warning instead.
    return "This is a protected method and must not be invoked by client: "
        + this;
  }

  protected String nonBusinessMethod2() {
    return "This is a package-default-access method and must not be invoked by client: "
        + this;
  }

  public String passAsParam(BeanBase b) {
    return "Invoked passAsParam, param=" + b + ", target=" + this;
  }

  // should be available to coexist with passAsParam(BeanBase b)
  public String passAsParam(BeanBase... b) {
    return "Invoked passAsParam..., param=" + Arrays.asList(b) + ", target="
        + this;
  }

  public int passEnumAsParams(NumberEnum e1, NumberIF e2) {
    return e2.add(e1.getNumber());
  }

  public NumberEnum passEnumAsReturn(int n) {
    return NumberEnum.getEnumFor(n);
  }
}
