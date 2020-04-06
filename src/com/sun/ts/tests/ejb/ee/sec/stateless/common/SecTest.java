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

package com.sun.ts.tests.ejb.ee.sec.stateless.common;

import jakarta.ejb.EJBObject;
import java.rmi.RemoteException;

public interface SecTest extends EJBObject {
  public void initLogging(java.util.Properties p) throws RemoteException;

  public boolean IsCaller(String caller) throws RemoteException;

  public boolean EjbNotAuthz() throws RemoteException;

  public boolean EjbIsAuthz() throws RemoteException;

  public boolean EjbSecRoleRef(String role) throws RemoteException;

  public boolean EjbOverloadedSecRoleRefs(String role1) throws RemoteException;

  public boolean EjbOverloadedSecRoleRefs(String role1, String role2)
      throws RemoteException;

  public boolean checktest1() throws RemoteException;

  public boolean excludetest1() throws RemoteException;
}
