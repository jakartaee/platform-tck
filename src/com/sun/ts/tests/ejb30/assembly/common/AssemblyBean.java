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

package com.sun.ts.tests.ejb30.assembly.common;

import com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

@Stateless
@Interceptors({ AssemblyInterceptor.class })
public class AssemblyBean extends AssemblyBeanBase
    implements AssemblyRemoteIF, AssemblyLocalIF {
  @EJB(name = "helloBean") // this ejb-ref is resolved in sun-ejb-jar.xml
  private HelloRemoteIF helloBean;

  public AssemblyBean() {
  }

  @Override
  protected HelloRemoteIF getHelloBean() {
    return helloBean;
  }

}
