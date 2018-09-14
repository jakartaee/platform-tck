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
 * @(#)ReferencedBeanCode.java	1.8 03/05/16
 */
package com.sun.ts.tests.assembly.util.shared.ejbref.common;

import javax.ejb.EJBException;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;

public class ReferencedBeanCode {
  public static final String envEntryName = "java:comp/env/myName";

  /**
   * Method used to identify this bean. Return the value of bean env entry
   * called 'myName'.
   */
  public static String whoAreYou(TSNamingContext nctx) throws EJBException {

    String name;

    try {
      TestUtil.logTrace("CaseBean: whoAreYou()");
      name = (String) nctx.lookup(envEntryName);
      TestUtil.logTrace("CaseBean: my name is '" + name + "'");
    } catch (Exception e) {
      TestUtil.logErr("CaseBean: Caught exception: " + e, e);
      throw new EJBException(e.getMessage());
    }

    return name;
  }
}
