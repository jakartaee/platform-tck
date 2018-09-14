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

public interface OrderHome extends EJBHome {
  public Order create(String id, Customer customer)
      throws RemoteException, CreateException;

  public Order findByPrimaryKey(String key)
      throws RemoteException, FinderException;

  public Collection findAllOrdersByCustomerName(String name)
      throws RemoteException, FinderException;

  public Collection findAllOrders() throws RemoteException, FinderException;

  public Collection findOrdersByQuery1()
      throws RemoteException, FinderException;

  public Collection findOrdersByQuery2()
      throws RemoteException, FinderException;

  public Collection findOrdersByQuery3()
      throws RemoteException, FinderException;

  public Collection findOrdersByQuery4()
      throws RemoteException, FinderException;

  public Collection findOrdersByQuery5()
      throws RemoteException, FinderException;

  public Collection findOrdersByQuery6()
      throws RemoteException, FinderException;

  public Collection findOrdersByQuery9(String name)
      throws RemoteException, FinderException;

  public Collection findOrdersByQuery12()
      throws RemoteException, FinderException;

  public Collection findOrdersByQuery13()
      throws RemoteException, FinderException;

  public Collection findOrdersByQuery14()
      throws RemoteException, FinderException;

  public Collection findOrdersByQuery16(double d)
      throws RemoteException, FinderException;

  public Collection findOrdersByQuery17()
      throws RemoteException, FinderException;

  public Collection findOrdersByQuery18()
      throws RemoteException, FinderException;

  public Collection findAllOrdersWithGreaterPrice()
      throws RemoteException, FinderException;

  public Collection findApprovedCreditCards()
      throws RemoteException, FinderException;

  public Collection findOrdersByPrice(String name)
      throws RemoteException, FinderException;

  public Collection findOrdersByQuery19()
      throws RemoteException, FinderException;

  public Collection findOrdersByQuery20()
      throws RemoteException, FinderException;

  public Collection findOrdersByQuery21(String s)
      throws RemoteException, FinderException;

  public Collection findOrdersByQuery21(String s, double d)
      throws RemoteException, FinderException;

  public Collection findOrdersByQuery22(double d)
      throws RemoteException, FinderException;

  // Miscellaneous Wrapper Home Methods for ejbSelect's
  public Collection selectAllExpiredCreditCards()
      throws RemoteException, CreditCardException;

  public Collection selectAllLineItems()
      throws RemoteException, LineItemException;

  public Collection selectSampleLineItems(LineItemDVC l)
      throws RemoteException, LineItemException;

  public Collection selectCreditCardBalances()
      throws RemoteException, CreditCardException;

  public Collection selectAllCreditCardBalances()
      throws RemoteException, CreditCardException;

  public java.lang.String selectMinSingle() throws RemoteException;

  public int selectMaxSingle() throws RemoteException;

  public double selectAvgSingle() throws RemoteException;
}
