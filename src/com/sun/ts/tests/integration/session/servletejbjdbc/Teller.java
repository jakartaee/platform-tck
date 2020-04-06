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

package com.sun.ts.tests.integration.session.servletejbjdbc;

import com.sun.ts.lib.util.*;

import jakarta.ejb.*;
import java.rmi.*;
import java.util.Properties;

public interface Teller extends EJBObject {
  public void transfer(int from, int to, double amt) throws RemoteException;

  public double balance(int acct) throws RemoteException;

  public double withdraw(int acct, double amt) throws RemoteException;

  public double deposit(int acct, double amt) throws RemoteException;

  public String getAllAccounts() throws RemoteException;
}
