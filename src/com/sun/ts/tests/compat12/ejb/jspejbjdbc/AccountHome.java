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

package com.sun.ts.tests.compat12.ejb.jspejbjdbc;

import jakarta.ejb.*;
import java.rmi.*;
import java.util.Properties;

public interface AccountHome extends EJBHome {
  Account create(int account, double balance, boolean newtable,
      Properties props) throws CreateException, RemoteException;

  Account findTheBean(Integer account, Properties props)
      throws FinderException, RemoteException;

  Account findByPrimaryKey(Integer account)
      throws FinderException, RemoteException;
}
