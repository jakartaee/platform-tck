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

package com.sun.ts.tests.ejb30.common.generics;

import java.util.Arrays;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

public abstract class GreetingBeanBase
    implements RemoteIntGreetingIF, LocalIntGreetingIF, ParameterizedIF {

  @TransactionAttribute(TransactionAttributeType.MANDATORY)
  public Integer negate(Integer i) {
    return i - i - i;
  }

  @TransactionAttribute(TransactionAttributeType.MANDATORY)
  public Integer greet(Integer t) {
    return t;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.sun.ts.tests.ejb30.common.generics.ParameterizedIF#parameterizedParam(
   * java.util.List)
   */
  @TransactionAttribute(TransactionAttributeType.MANDATORY)
  public void parameterizedParam(List<String> ls) {
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.sun.ts.tests.ejb30.common.generics.ParameterizedIF#parameterizedReturn(
   * int)
   */
  @TransactionAttribute(TransactionAttributeType.MANDATORY)
  public List<String> parameterizedReturn(int i) {
    return Arrays.asList(String.valueOf(i));
  }
}
