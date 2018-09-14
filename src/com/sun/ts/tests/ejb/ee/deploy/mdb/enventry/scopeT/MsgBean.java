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

package com.sun.ts.tests.ejb.ee.deploy.mdb.enventry.scopeT;

import java.util.Properties;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.ejb.wrappers.MDBWrapper;
import com.sun.ts.tests.assembly.util.shared.enventry.scope.TestCode;

public class MsgBean extends MDBWrapper {

  public Boolean checkEntry(Properties props) {
    boolean pass;
    String entryLookup = null;
    String expectedValue = null;

    try {
      entryLookup = TestUtil.getProperty("entryLookup");
      expectedValue = TestUtil.getProperty("expectedValue");

      pass = TestCode.checkEntry(nctx, entryLookup, expectedValue);
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("[MsgBean] caught exception: ", e);
    }

    return new Boolean(pass);
  }

}
