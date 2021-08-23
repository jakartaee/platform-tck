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

package com.sun.ts.tests.ejb30.bb.session.stateless.basic;

import com.sun.ts.tests.ejb30.common.calc.BaseRemoteCalculator;
import com.sun.ts.tests.ejb30.common.calc.RemoteCalculator;

/**
 * This bean is similar to RemoteCalculatorBean, and is added here to see
 * multiple beans with the same remote business interface can co-exist.
 *
 * This bean contains no resource or ejb injection. It has a post-construct
 * method that is declared in ejb-jar.xml. This is to verify that the post-
 * construct method is invoked even when there is no resource/ejb injection in a
 * bean.
 */
// @Stateless(name="RemoteCalculatorBean2",
// description="a simple stateless session bean")
// @Remote({RemoteCalculator.class})
public class RemoteCalculatorBean2 extends BaseRemoteCalculator
    implements RemoteCalculator {

  private int postConstructCallsCount;

  public RemoteCalculatorBean2() {
  }

  // @PostConstruct
  private void postConstruct() {
    postConstructCallsCount++;
  }

  @Override
  public int remoteAdd(int a, int b) {
    int retValue;
    retValue = super.remoteAdd(a, b);
    return retValue + postConstructCallsCount;
  }

}
