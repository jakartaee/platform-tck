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

package com.sun.ts.tests.ejb30.common.ejblink;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

abstract public class EjbLinkBeanBase implements EjbLinkIF {
  public void call() throws TestFailedException {
  }

  public void localCall(int[] f) throws TestFailedException {
    f[0] = 1;
  }

  public void callOneLocal() throws TestFailedException {
    EjbLinkLocalIF localBean1 = (EjbLinkLocalIF) ServiceLocator
        .lookupNoTry(Constants.BEAN1_LOCAL_REF_NAME);
    int[] f = new int[] { 0 };
    int expected = 1;
    localBean1.localCall(f);
    if (f[0] != expected) {
      throw new TestFailedException(
          "Expecting " + expected + ", but got " + f[0]);
    }
  }

}
