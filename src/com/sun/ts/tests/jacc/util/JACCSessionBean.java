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

/**
 * $Id$
 *
 * @author Raja Perumal
 *         08/22/02
 */

package com.sun.ts.tests.jacc.util;

import java.io.Serializable;
import java.rmi.RemoteException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.security.Principal;

public class JACCSessionBean implements SessionBean {

  String arg1;

  int arg2;

  long arg3;

  private SessionContext context;

  public JACCSessionBean() {
  }

  public void ejbCreate(String arg1, int arg2, long arg3)
      throws RemoteException {
    this.arg1 = arg1;
    this.arg2 = arg2;
    this.arg3 = arg3;
  }

  public void setSessionContext(SessionContext sc) {
    this.context = sc;
  }

  public void ejbRemove() throws RemoteException {
  }

  public void ejbActivate() throws RemoteException {
  }

  public void ejbPassivate() throws RemoteException {
  }

  public String getArg1() throws RemoteException {
    return this.arg1;
  }

  public int getArg2() throws RemoteException {
    return this.arg2;
  }

  public long getArg3() throws RemoteException {
    return this.arg3;
  }

  public String getCallerName() throws RemoteException {
    String callerName = null;
    Principal callerPrincipal = context.getCallerPrincipal();

    if (callerPrincipal != null)
      callerName = callerPrincipal.getName();

    // invoke isCallerInRole() for the following tests
    // EJBRoleRefPermissionEquals() and
    // EJBRoleRefPermissionHashCode()
    boolean isCallerInRole = context.isCallerInRole("EMP");

    return callerName;
  }

}
