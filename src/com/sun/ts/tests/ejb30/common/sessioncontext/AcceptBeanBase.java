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

package com.sun.ts.tests.ejb30.common.sessioncontext;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import static com.sun.ts.tests.ejb30.common.sessioncontext.Constants.FROM_BEAN;
import static com.sun.ts.tests.ejb30.common.sessioncontext.Constants.FROM_CLIENT;
import javax.ejb.SessionContext;

abstract public class AcceptBeanBase implements AcceptLocalIF, AcceptIF {
  abstract protected SessionContext getSessionContext();

  private void verifyRemote(String[] m) throws TestFailedException {
    if (FROM_CLIENT.equals(m[0])) {
      // ok
    } else {
      throw new TestFailedException(
          "Expecting '" + FROM_CLIENT + "', but actual '" + m[0] + "'");
    }
  }

  private void verifyLocal(String[] m) throws TestFailedException {
    if (FROM_BEAN.equals(m[0])) {
      // ok
    } else {
      throw new TestFailedException(
          "Expecting '" + FROM_BEAN + "', but actual '" + m[0] + "'");
    }
  }

  public void accept(CommonIF bean) throws TestFailedException {
    final String[] m = new String[] { FROM_CLIENT };
    if (bean instanceof Three1IF) {
      Three1IF bob = (Three1IF) bean;
      bob.hi(m);
      verifyRemote(m);
    } else if (bean instanceof Three2IF) {
      Three2IF bob = (Three2IF) bean;
      bob.hi(m);
      verifyRemote(m);
    } else if (bean instanceof ThreeLocal1IF) {
      ThreeLocal1IF bob = (ThreeLocal1IF) bean;
      bob.hi(m);
      verifyLocal(m);
    } else if (bean instanceof ThreeLocal2IF) {
      ThreeLocal2IF bob = (ThreeLocal2IF) bean;
      bob.hi(m);
      verifyLocal(m);
    }
  }

}
