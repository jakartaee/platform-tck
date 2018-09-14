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

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.entitybeantest;

import com.sun.ts.lib.util.*;

import javax.ejb.*;
import java.rmi.*;

public interface Helper extends EJBObject {
  public void reset() throws RemoteException;

  public void setCreate(boolean b) throws RemoteException;

  public boolean isCreate() throws RemoteException;

  public void setActivate(boolean b) throws RemoteException;

  public boolean isActivate() throws RemoteException;

  public void setPassivate(boolean b) throws RemoteException;

  public boolean isPassivate() throws RemoteException;

  public void setRemove(boolean b) throws RemoteException;

  public boolean isRemove() throws RemoteException;

  public void setContext(boolean b) throws RemoteException;

  public boolean isContext() throws RemoteException;

  public void setUnsetContext(boolean b) throws RemoteException;

  public boolean isUnsetContext() throws RemoteException;

  public void setLoad(boolean b) throws RemoteException;

  public boolean isLoad() throws RemoteException;

  public void setStore(boolean b) throws RemoteException;

  public boolean isStore() throws RemoteException;

  public void setCreateLifeCycle(boolean b) throws RemoteException;

  public boolean isCreateLifeCycle() throws RemoteException;

  public void setCreateMethodCalled(int b) throws RemoteException;

  public int isCreateMethodCalled() throws RemoteException;

  public boolean isCreateLifeCycle1() throws RemoteException;

  public boolean isCreateLifeCycle2() throws RemoteException;
}
