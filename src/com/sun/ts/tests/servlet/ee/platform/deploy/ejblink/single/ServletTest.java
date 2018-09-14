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

package com.sun.ts.tests.servlet.ee.platform.deploy.ejblink.single;

import com.sun.ts.tests.assembly.util.shared.ejbref.single.TestCode;
import com.sun.ts.tests.common.web.ServletWrapper;

import java.util.Properties;

/** Servlet test driver */
public class ServletTest extends ServletWrapper {

  public Boolean testStateless(Properties props) {
    boolean pass;

    pass = TestCode.testStatelessExternal(nctx, props);
    return new Boolean(pass);
  }

  public Boolean testStateful(Properties props) {
    boolean pass;

    pass = TestCode.testStatefulExternal(nctx, props);
    return new Boolean(pass);
  }

  public Boolean testBMP(Properties props) {
    boolean pass;

    pass = TestCode.testBMPExternal(nctx, props);
    return new Boolean(pass);
  }

  public Boolean testCMP11(Properties props) {
    boolean pass;

    pass = TestCode.testCMP11External(nctx, props);
    return new Boolean(pass);
  }

  public Boolean testCMP20(Properties props) {
    boolean pass;

    pass = TestCode.testCMP20External(nctx, props);
    return new Boolean(pass);
  }

  public Boolean cleanUpBean(Properties props) {
    TestCode.cleanUpStatefulBean();
    return Boolean.TRUE;
  }
}
