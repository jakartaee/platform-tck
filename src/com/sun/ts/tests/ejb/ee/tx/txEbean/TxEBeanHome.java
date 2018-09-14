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

package com.sun.ts.tests.ejb.ee.tx.txEbean;

import java.rmi.*;
import java.util.*;
import java.sql.*;
import javax.ejb.*;

public interface TxEBeanHome extends EJBHome {

  public TxEBean create(String tName, int key, String brand, float price,
      Properties p) throws CreateException, DuplicateKeyException,
      RemoteException, SQLException;

  public TxEBean findtxEbean(String tName, Integer key, Properties p)
      throws FinderException, ObjectNotFoundException, RemoteException;

  public TxEBean findByPrimaryKey(Integer key)
      throws FinderException, ObjectNotFoundException, RemoteException;

  public Collection findByBrandName(String tName, String brandName,
      Properties p) throws FinderException, RemoteException;

  public Collection findByPrice(String tName, float price, Properties p)
      throws FinderException, RemoteException;

}
