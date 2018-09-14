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

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.entitycontexttest;

import com.sun.ts.lib.util.*;

import javax.ejb.*;
import java.rmi.*;

public interface FastPath extends EJBObject {
  public String getIt(String envEntryName) throws RemoteException;

  public String setIt(String envEntryName) throws RemoteException;

  public String getCoffeeId(String coffeeId) throws RemoteException;

  public String setCoffeeBrand(String oldBrand, String newBrand)
      throws RemoteException;
}
