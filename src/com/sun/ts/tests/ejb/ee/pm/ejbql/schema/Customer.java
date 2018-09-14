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

import com.sun.ts.lib.util.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;

public interface Customer extends EJBObject {
  // Business Methods for CMP Fields
  public String getId() throws RemoteException;

  public String getName() throws RemoteException;

  public void setName(String v) throws RemoteException;

  public Country getCountry() throws RemoteException;

  public void setCountry(Country v) throws RemoteException;

  // Miscellaneous Business Methods
  public void addAlias(Alias a) throws RemoteException;

  public void addOrder(Order a) throws RemoteException;

  public void addSpouse(SpouseDVC s) throws RemoteException, SpouseException;

  public void addCreditCard(CreditCardDVC a)
      throws RemoteException, CreditCardException;

  public AddressDVC getClientHomeAddress()
      throws RemoteException, AddressException;

  public AddressDVC getClientWorkAddress()
      throws RemoteException, AddressException;

  public Collection getClientCreditCards()
      throws RemoteException, CreditCardException;

  public Collection getClientOrders() throws RemoteException;

  public Collection getClientAliases() throws RemoteException;

  public void initLogging(Properties p) throws RemoteException;
}
