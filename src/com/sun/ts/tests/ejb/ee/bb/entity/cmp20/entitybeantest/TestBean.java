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

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.entitybeantest;

import com.sun.ts.lib.util.*;

import java.util.*;
import jakarta.ejb.*;
import java.rmi.*;

public interface TestBean extends EJBObject {
  // Business Methods for TestBean CMP Fields
  public Integer getId() throws RemoteException;

  public String getBrandName() throws RemoteException;

  public void setBrandName(String s) throws RemoteException;

  public float getPrice() throws RemoteException;

  public void setPrice(float p) throws RemoteException;

  // Miscellaneous Business Methods

  public void setHelper(Helper ref) throws RemoteException;

  public void ping() throws RemoteException;

  public void loadOrStoreTest(Helper ref) throws RemoteException;

  public void reset() throws RemoteException;

  public void throwEJBException() throws RemoteException;

  public void throwError() throws RemoteException;

  public boolean iAmDestroyed() throws RemoteException;

  public void initLogging(Properties p) throws RemoteException;
}
