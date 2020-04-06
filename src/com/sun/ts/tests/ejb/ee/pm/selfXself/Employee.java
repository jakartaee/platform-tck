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

package com.sun.ts.tests.ejb.ee.pm.selfXself;

import com.sun.ts.lib.util.*;

import java.util.*;
import java.rmi.*;
import jakarta.ejb.*;

public interface Employee extends EJBObject {
  // Business Methods for CMP Fields
  public Integer getId() throws RemoteException;

  public String getFirstName() throws RemoteException;

  public void setFirstName(String v) throws RemoteException;

  public String getLastName() throws RemoteException;

  public void setLastName(String v) throws RemoteException;

  public java.util.Date getHireDate() throws RemoteException;

  public void setHireDate(java.util.Date d) throws RemoteException;

  public float getSalary() throws RemoteException;

  public void setSalary(float f) throws RemoteException;

  // Business Methods for Tests

  public void addDepartment(Department d) throws RemoteException;

  public void addManager(Employee e) throws RemoteException;

  public boolean test3() throws RemoteException;

  public boolean test4() throws RemoteException;

  public void initLogging(Properties p) throws RemoteException;
}
