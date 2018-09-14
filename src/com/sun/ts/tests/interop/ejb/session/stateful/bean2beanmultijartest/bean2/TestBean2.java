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

package com.sun.ts.tests.interop.ejb.session.stateful.bean2beanmultijartest.bean2;

import com.sun.ts.lib.util.*;

import javax.ejb.*;
import java.rmi.*;

public interface TestBean2 extends EJBObject {
  public String whoAmI() throws RemoteException;

  public boolean passBean1String(String s) throws RemoteException;

  public boolean passBean1Handle(Handle v) throws RemoteException;

  public boolean passBean1HomeHandle(HomeHandle v) throws RemoteException;

  public boolean passBean1EJBMetaData(EJBMetaData v) throws RemoteException;

  public String returnBean2String() throws RemoteException;

  public Handle returnBean2Handle() throws RemoteException;

  public HomeHandle returnBean2HomeHandle() throws RemoteException;

  public EJBMetaData returnBean2EJBMetaData() throws RemoteException;
}
