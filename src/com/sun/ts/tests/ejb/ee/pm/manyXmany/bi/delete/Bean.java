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

package com.sun.ts.tests.ejb.ee.pm.manyXmany.bi.delete;

import com.sun.ts.lib.util.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;

public interface Bean extends EJBObject {
  // Business Methods for Bean CMP Fields
  public String getId() throws RemoteException;

  public String getName() throws RemoteException;

  public void setName(String v) throws RemoteException;

  public int getValue() throws RemoteException;

  public void setValue(int v) throws RemoteException;

  // Miscellaneous Business Methods
  public void init(Properties p) throws RemoteException;

  public boolean test1() throws RemoteException;

  public boolean test2() throws RemoteException;

  public boolean test3() throws RemoteException;
}
