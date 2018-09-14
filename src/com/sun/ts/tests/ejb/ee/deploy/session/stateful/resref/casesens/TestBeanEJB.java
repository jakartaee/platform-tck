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

package com.sun.ts.tests.ejb.ee.deploy.session.stateful.resref.casesens;

import java.util.Properties;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.ejb.wrappers.StatefulWrapper;
import com.sun.ts.tests.assembly.util.shared.resref.casesens.TestCode;

public class TestBeanEJB extends StatefulWrapper {

  /**
   * Check that two resref entries whose names differ only by case are
   * associated with different runtime values (more exactly to distinct java
   * types, as specified in DD).
   */
  public boolean testCaseSensitivity(Properties p) {
    return TestCode.testCaseSensitivity(nctx);
  }

}
