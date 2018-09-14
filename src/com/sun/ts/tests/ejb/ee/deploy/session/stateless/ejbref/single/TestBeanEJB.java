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

package com.sun.ts.tests.ejb.ee.deploy.session.stateless.ejbref.single;

import java.util.Properties;
import com.sun.ts.tests.common.ejb.wrappers.StatelessWrapper;
import com.sun.ts.tests.assembly.util.shared.ejbref.single.TestCode;
import com.sun.ts.tests.assembly.util.refbean.*;

public class TestBeanEJB extends StatelessWrapper {

  /*
   * EJB references test methods.
   */

  public boolean testStatelessInternal(Properties props) {
    return TestCode.testStatelessInternal(nctx, props);
  }

  public boolean testStatelessExternal(Properties props) {
    return TestCode.testStatelessExternal(nctx, props);
  }

  public boolean testStatefulInternal(Properties props) {
    return TestCode.testStatefulInternal(nctx, props);
  }

  public boolean testStatefulExternal(Properties props) {
    return TestCode.testStatefulExternal(nctx, props);
  }

  public boolean testBMPInternal(Properties props) {
    return TestCode.testBMPInternal(nctx, props);
  }

  public boolean testBMPExternal(Properties props) {
    return TestCode.testBMPExternal(nctx, props);
  }

  public boolean testCMP11Internal(Properties props) {
    return TestCode.testCMP11Internal(nctx, props);
  }

  public boolean testCMP11External(Properties props) {
    return TestCode.testCMP11External(nctx, props);
  }

  public boolean testCMP20Internal(Properties props) {
    return TestCode.testCMP20Internal(nctx, props);
  }

  public boolean testCMP20External(Properties props) {
    return TestCode.testCMP20External(nctx, props);
  }

  public void cleanUpBean() {
    TestCode.cleanUpStatefulBean();
  }
}
