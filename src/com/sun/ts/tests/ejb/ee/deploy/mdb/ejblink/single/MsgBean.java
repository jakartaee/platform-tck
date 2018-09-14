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

package com.sun.ts.tests.ejb.ee.deploy.mdb.ejblink.single;

import java.util.Properties;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jms.common.JmsUtil;
import com.sun.ts.tests.common.ejb.wrappers.MDBWrapper;
import com.sun.ts.tests.assembly.util.shared.ejbref.single.TestCode;

public class MsgBean extends MDBWrapper {

  boolean pass;

  public Boolean testStatelessInternal(Properties props) {
    pass = TestCode.testStatelessInternal(nctx, props);
    return new Boolean(pass);
  }

  public Boolean testStatelessExternal(Properties props) {
    pass = TestCode.testStatelessExternal(nctx, props);
    return new Boolean(pass);
  }

  public Boolean testStatefulInternal(Properties props) {
    pass = TestCode.testStatefulInternal(nctx, props);
    return new Boolean(pass);
  }

  public Boolean testStatefulExternal(Properties props) {
    pass = TestCode.testStatefulExternal(nctx, props);
    return new Boolean(pass);
  }

  public Boolean testBMPInternal(Properties props) {
    pass = TestCode.testBMPInternal(nctx, props);
    return new Boolean(pass);
  }

  public Boolean testBMPExternal(Properties props) {
    pass = TestCode.testBMPExternal(nctx, props);
    return new Boolean(pass);
  }

  public Boolean testCMP11Internal(Properties props) {
    pass = TestCode.testCMP11Internal(nctx, props);
    return new Boolean(pass);
  }

  public Boolean testCMP11External(Properties props) {
    pass = TestCode.testCMP11External(nctx, props);
    return new Boolean(pass);
  }

  public Boolean testCMP20Internal(Properties props) {
    pass = TestCode.testCMP20Internal(nctx, props);
    return new Boolean(pass);
  }

  public Boolean testCMP20External(Properties props) {
    pass = TestCode.testCMP20External(nctx, props);
    return new Boolean(pass);
  }

  public Boolean cleanUpBean(Properties props) {
    TestCode.cleanUpStatefulBean();
    return Boolean.TRUE;
  }
}
