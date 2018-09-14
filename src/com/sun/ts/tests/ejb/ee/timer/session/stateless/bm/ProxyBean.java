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

package com.sun.ts.tests.ejb.ee.timer.session.stateless.bm;

import java.rmi.RemoteException;
import java.util.Properties;
import javax.ejb.EJBObject;

public interface ProxyBean extends EJBObject {
  public boolean setup() throws RemoteException;

  public void cleanup() throws RemoteException;

  public boolean initializeTimer(int timerType, int timeoutAction)
      throws RemoteException;

  public boolean initializeAndCancelTimer(int timerType) throws RemoteException;

  public boolean cancelAndRollback(int timerType) throws RemoteException;

  public boolean timerExists() throws RemoteException;

  public boolean timerDoesNotExist() throws RemoteException;
}
