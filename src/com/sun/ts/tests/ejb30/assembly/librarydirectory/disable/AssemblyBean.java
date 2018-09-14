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

package com.sun.ts.tests.ejb30.assembly.librarydirectory.disable;

import com.sun.ts.tests.ejb30.assembly.common.AssemblyBeanBase;
import com.sun.ts.tests.ejb30.assembly.common.AssemblyInterceptor;
import com.sun.ts.tests.ejb30.assembly.common.AssemblyLocalIF;
import com.sun.ts.tests.ejb30.assembly.common.AssemblyRemoteIF;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

/**
 * This bean is packaged into lib/shared.jar, but this library-directory is
 * disabled by using an empty element value. Although this bean has the same
 * name as the one in ../../common, and implements the same remote/local
 * interface, it should not cause any conflict since this directory is disabled
 * in application.xml
 */
@Stateless(name = "AssemblyBean")
@Interceptors({ AssemblyInterceptor.class })
public class AssemblyBean extends AssemblyBeanBase
    implements AssemblyRemoteIF, AssemblyLocalIF {

  public AssemblyBean() {
  }

  @Override
  public int remoteAdd(int a, int b) {
    fail();
    return 9999999;
  }

  private void fail() throws EJBException {
    throw new EJBException("Should not get here since this bean is packaged "
        + "in a library jar that has been disabled in application.xml");
  }

  @Override
  public int getPostConstructCalls() {
    fail();
    return 999999;
  }

}
