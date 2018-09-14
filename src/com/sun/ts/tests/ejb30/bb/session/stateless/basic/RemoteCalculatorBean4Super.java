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
import com.sun.ts.tests.ejb30.common.helper.TLogger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.omg.CORBA.ORB;

abstract public class RemoteCalculatorBean4Super extends BaseRemoteCalculator
    implements RemoteCalculator {

  @Resource
  private ORB orb;

  @Override
  public int remoteAdd(int a, int b) {
    int retValue;
    retValue = super.remoteAdd(a, b);
    return retValue + (orb == null ? 0 : orb.toString().length());
  }

}
