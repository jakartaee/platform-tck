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

package com.sun.ts.tests.ejb.ee.sec.cmp20.common;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

public interface Test extends EJBObject {
  public boolean IsCallerB1(String caller) throws RemoteException;

  public boolean IsCallerB2(String caller, java.util.Properties p)
      throws RemoteException;

  public boolean InRole(String role, java.util.Properties p)
      throws RemoteException;

  public boolean EjbNotAuthz(java.util.Properties p) throws RemoteException;

  public boolean EjbIsAuthz(java.util.Properties p) throws RemoteException;

  public boolean EjbSecRoleRef(String role, java.util.Properties p)
      throws RemoteException;

  public boolean EjbSecRoleRef1(String role, java.util.Properties p)
      throws RemoteException;

  public boolean EjbOverloadedSecRoleRefs(String role1, String role2,
      java.util.Properties p) throws RemoteException;

  public boolean EjbSecRoleRefScope(String role, java.util.Properties p)
      throws RemoteException;

  public boolean checktest1(java.util.Properties p) throws RemoteException;

  public boolean excludetest1(java.util.Properties p) throws RemoteException;

  public Integer getId() throws RemoteException;

  public String getBrandName() throws RemoteException;

  public void setBrandName(String s) throws RemoteException;

  public float getPrice() throws RemoteException;

  public void setPrice(float p) throws RemoteException;

}
