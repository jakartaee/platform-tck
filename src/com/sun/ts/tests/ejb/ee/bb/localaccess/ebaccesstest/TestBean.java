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

package com.sun.ts.tests.ejb.ee.bb.localaccess.ebaccesstest;

import com.sun.ts.lib.util.*;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;

public interface TestBean extends EJBObject {
  // Business Methods for CMP Fields
  public Integer getId() throws RemoteException;

  public String getName() throws RemoteException;

  public void setName(String v) throws RemoteException;

  public float getPrice() throws RemoteException;

  public void setPrice(float v) throws RemoteException;

  // Business Methods
  public boolean test1() throws RemoteException;

  public boolean test2() throws RemoteException;

  public boolean test3(Properties p) throws RemoteException;

  public boolean test4() throws RemoteException;

  public void cleanUpStatefulBean() throws RemoteException;
}
