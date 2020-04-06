/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.compat12.ejb.sec;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import java.util.*;
import jakarta.ejb.*;
import java.rmi.*;

public class SecTestEJB implements SessionBean {

  private SessionContext sctx = null;

  public void ejbCreate(java.util.Properties p)
      throws RemoteException, CreateException {
  }

  public boolean EjbNotAuthz() {
    return true;
  }

  public boolean EjbIsAuthz() {
    return true;
  }

  public boolean EjbSecRoleRef(String role) {
    return sctx.isCallerInRole(role);
  }

  public boolean EjbOverloadedSecRoleRefs(String role1) {
    return sctx.isCallerInRole(role1);
  }

  public boolean EjbOverloadedSecRoleRefs(String role1, String role2) {
    return sctx.isCallerInRole(role1) && sctx.isCallerInRole(role2);
  }

  public void setSessionContext(SessionContext sc) {
    sctx = sc;
  }

  public void ejbRemove() throws RemoteException {
  }

  public void ejbActivate() {
  }

  public void ejbPassivate() {
  }
}
