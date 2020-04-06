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

/*
 * @(#)TxBean.java  1.0 02/07/31
 */

package com.sun.ts.tests.xa.ee.resXcomp1;

import java.rmi.*;
import java.util.*;
import jakarta.ejb.*;
import jakarta.transaction.*;
import com.sun.ts.lib.util.*;

public interface TxBean extends EJBObject {
  public void dbConnect(String tName) throws RemoteException;

  public void createData(String tName) throws RemoteException;

  public boolean insert(String tName, int key) throws RemoteException;

  public void delete(String tName, int fromKey, int toKey)
      throws RemoteException;

  public void destroyData(String tName) throws RemoteException;

  public void dbUnConnect(String tName) throws RemoteException;

  public Vector getResults(String tName) throws RemoteException;

  public void initLogging(Properties p) throws RemoteException;

  public void throwEJBException() throws RemoteException;

  public void listTableData(Vector dbResults) throws RemoteException;
}
