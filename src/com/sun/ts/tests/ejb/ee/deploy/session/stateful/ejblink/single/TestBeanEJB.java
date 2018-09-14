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

package com.sun.ts.tests.ejb.ee.deploy.session.stateful.ejblink.single;

import com.sun.ts.tests.common.ejb.wrappers.StatefulWrapper;
import com.sun.ts.tests.assembly.util.shared.ejbref.single.TestCode;

public class TestBeanEJB extends StatefulWrapper {

  public boolean testStatelessInternal() {
    return TestCode.testStatelessInternal(nctx, props);
  }

  public boolean testStatelessExternal() {
    return TestCode.testStatelessExternal(nctx, props);
  }

  public boolean testStatefulInternal() {
    return TestCode.testStatefulInternal(nctx, props);
  }

  public boolean testStatefulExternal() {
    return TestCode.testStatefulExternal(nctx, props);
  }

  public boolean testBMPInternal() {
    return TestCode.testBMPInternal(nctx, props);
  }

  public boolean testBMPExternal() {
    return TestCode.testBMPExternal(nctx, props);
  }

  public boolean testCMP11Internal() {
    return TestCode.testCMP11Internal(nctx, props);
  }

  public boolean testCMP11External() {
    return TestCode.testCMP11External(nctx, props);
  }

  public boolean testCMP20Internal() {
    return TestCode.testCMP20Internal(nctx, props);
  }

  public boolean testCMP20External() {
    return TestCode.testCMP20External(nctx, props);
  }

  public void cleanUpBean() {
    TestCode.cleanUpStatefulBean();
  }
}
