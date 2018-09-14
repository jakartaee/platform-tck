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

package com.sun.ts.tests.ejb.ee.pm.ejbql.schema;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;

public interface ProductHome extends EJBHome {
  public Product create(String id, String name, double price, int quantity,
      long partNumber) throws RemoteException, CreateException;

  public Product findByPrimaryKey(String key)
      throws RemoteException, FinderException;

  public Collection findAllProducts() throws RemoteException, FinderException;

  public Product findProductByName(String name)
      throws RemoteException, FinderException;

  public Collection findAllProductsByQuantity()
      throws RemoteException, FinderException;

  public Collection findProductsByQuery1()
      throws RemoteException, FinderException;

  public Collection findProductsByHighestQuantity()
      throws RemoteException, FinderException;

  public Collection findProductsByQuery2()
      throws RemoteException, FinderException;

  public Collection findProductsByQuery3()
      throws RemoteException, FinderException;

  public Collection findProductsByQuery4()
      throws RemoteException, FinderException;

  public Collection findProductsByQuery5()
      throws RemoteException, FinderException;

  public Collection findProductsByQuery6()
      throws RemoteException, FinderException;

  public Collection findProductsByQuery7(int i)
      throws RemoteException, FinderException;

  public Collection findProductsByQuery8(String s)
      throws RemoteException, FinderException;

  public Collection findProductsByQuery9()
      throws RemoteException, FinderException;

  // Miscellaneous Wrapper Home Methods for ejbSelect's
  public Collection selectAllProducts() throws RemoteException, FinderException;

  public Product selectProductByName(String name)
      throws RemoteException, FinderException;

  public Product selectProductByType() throws RemoteException, FinderException;

  public Collection selectProductsByPartNumber()
      throws RemoteException, FinderException;

  public long selectCountSingle() throws RemoteException;

  public double selectSumSingle() throws RemoteException;

}
