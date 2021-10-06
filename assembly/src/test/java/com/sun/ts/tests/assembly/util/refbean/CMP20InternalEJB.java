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

package com.sun.ts.tests.assembly.util.refbean;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.ejb.wrappers.CMP20Wrapper;

public abstract class CMP20InternalEJB extends CMP20Wrapper {

  /** Method used to identify this bean */
  public boolean isTestCMP20Internal() {
    TestUtil.logTrace("CMP20Internal: isTestCMP20Internal()");
    return true;
  }

}
