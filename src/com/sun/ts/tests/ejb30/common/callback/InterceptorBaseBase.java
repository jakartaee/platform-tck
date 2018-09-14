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

package com.sun.ts.tests.ejb30.common.callback;

import javax.annotation.Resource;
import org.omg.CORBA.ORB;

abstract public class InterceptorBaseBase {
  protected static final String NOT_INJECTED = "NOT_INJECTED";

  protected static final String POSTCONSTRUCT_CALLS_IN_CONTEXTDATA = "POSTCONSTRUCT_CALLS_IN_CONTEXTDATA";

  @Resource()
  private ORB orbInBaseBase;

  abstract protected String getInjectedLocation();

  protected String getInjectedBaseBaseLocation() {
    String result = (orbInBaseBase == null) ? NOT_INJECTED : "BASEBASE";
    return result;
  }

  /**
   * Note that this method may be overridden by subclasses. If so, the value
   * returned may be different from the shortName defined in the current
   * enclosing class. That is why we need to pass a symbol (same as shortName)
   * to myCreate0(InvocatioinContext, String symbol), to force a hardcoded
   * value.
   */
  protected String getShortName() {
    return "BASEBASE";
  }

}
