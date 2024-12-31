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

package com.sun.ts.tests.ejb30.assembly.metainf.appclientejb;

import com.sun.ts.tests.ejb30.assembly.common.AssemblyBeanBase;
import com.sun.ts.tests.ejb30.assembly.common.AssemblyCommonIF;
import com.sun.ts.tests.ejb30.assembly.common.AssemblyLocalIF;
import com.sun.ts.tests.ejb30.assembly.common.AssemblyRemoteIF;
import com.sun.ts.tests.ejb30.common.calc.CalculatorException;

//already declared in ejb-jar.xml.  Annotations not needed
public class AssemblyBean extends AssemblyBeanBase
    implements AssemblyRemoteIF, AssemblyLocalIF, AssemblyCommonIF {

  public AssemblyBean() {
  }

  @Override
  public String dirUsedInClassPath() {
    CalculatorException e = new CalculatorException(
        "Just to show CalculatorException.class can be loaded in EJB.");
    String s = e.toString();
    try {
      s = s + ", Classloader info: "
          + (e.getClass().getClassLoader().toString());
    } catch (Exception ignore) {
    }
    return s;
  }

}
