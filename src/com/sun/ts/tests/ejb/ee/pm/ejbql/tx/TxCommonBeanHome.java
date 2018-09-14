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

package com.sun.ts.tests.ejb.ee.pm.ejbql.tx;

import java.rmi.*;
import java.util.*;
import java.sql.*;
import javax.ejb.*;

public interface TxCommonBeanHome extends EJBHome {

  public TxCommonBean create(Integer key, String brand, float price,
      Properties p) throws CreateException, RemoteException;

  public TxCommonBean findByPrimaryKey(Integer key)
      throws RemoteException, FinderException;

  public Collection findByBrandName(String brandName)
      throws RemoteException, FinderException;

  public Collection findByBrandName1(String brandName)
      throws RemoteException, FinderException;

  public Collection findByBrandName2(String brandName)
      throws RemoteException, FinderException;

  public Collection findAllBeans() throws RemoteException, FinderException;

}
