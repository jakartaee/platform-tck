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

public interface CustomerHome extends EJBHome {
  public Customer create(String id, String name, AddressDVC home,
      AddressDVC work, Country country) throws RemoteException, CreateException;

  public Customer findByPrimaryKey(String key)
      throws RemoteException, FinderException;

  public Collection findAllCustomers() throws RemoteException, FinderException;

  public Collection findAllCustomersByAliasName(String alias)
      throws RemoteException, FinderException;

  public Collection findCustomersByCreditCardType(String creditcard)
      throws RemoteException, FinderException;

  public Customer findCustomerByHomeAddress(String street, String city,
      String state, String zip) throws RemoteException, FinderException;

  public Collection findCustomersByHomeInfo(String street, String city,
      String state, String zip) throws RemoteException, FinderException;

  public Customer findCustomerByName(String name)
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery8()
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery9()
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery10()
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery11()
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery12()
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery13()
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery14()
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery15()
      throws RemoteException, FinderException;

  public Customer findCustomerByQuery16()
      throws RemoteException, FinderException;

  public Customer findCustomerByQuery17(String name)
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery22(String name)
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery23(String name)
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery24()
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery25()
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery26()
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery27()
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery28()
      throws RemoteException, FinderException;

  public Customer findCustomerByQuery29(String street, String city,
      String state, String zip) throws RemoteException, FinderException;

  public Collection findCustomersByQuery30()
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery32(String city)
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery33()
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery34()
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery35()
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery36()
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery37(String s)
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery38(String s)
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery39()
      throws RemoteException, FinderException;

  public Customer findCustomerByQuery40()
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery41()
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery42(double d)
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery43()
      throws RemoteException, FinderException;

  public Collection findCustomersByQuery44()
      throws RemoteException, FinderException;

  // Miscellaneous Wrapper Home Methods for ejbSelect's
  public AddressDVC selectHomeAddress()
      throws RemoteException, AddressException;

  public Collection selectAllWorkAddresses()
      throws RemoteException, AddressException;

  public Set selectHomeZipCodesByCity(String city)
      throws RemoteException, AddressException;

  public Collection selectAllHomeZipCodesByCity(String city)
      throws RemoteException, AddressException;

  public Collection selectCustomersByAlias(String s) throws RemoteException;

  public Collection selectCustomersByAlias(String s, String t)
      throws RemoteException;

  public Collection selectPhonesByArea(String area)
      throws RemoteException, PhoneException;

  public Set selectCustomerAddressBySet(String state) throws RemoteException;

  public Collection selectCustomerAddressByCollection(String state)
      throws RemoteException;

  public Collection selectCustomersByWorkZipCode() throws RemoteException;

  public Collection selectCustomersByNotNullWorkZipCode()
      throws RemoteException;

  public String selectCustomerByHomeAddress() throws RemoteException;

  public long selectAllHomeCities() throws RemoteException;

  public long selectNotNullHomeCities() throws RemoteException;

  public Collection selectCustomersByQuery42()
      throws RemoteException, AddressException;

  // Miscellaneous Wrapper Home Methods for local finders
  public Customer getCustomerByHomePhoneNumber(String phone)
      throws RemoteException;

  public Customer getCustomerByQuery29(String street, String city, String state,
      String zip) throws RemoteException;

  public Collection getCustomersByWorkCity(String city) throws RemoteException;

  public Collection getCustomersByQuery32(String city) throws RemoteException;

  public String getSpouseInfo() throws RemoteException;

  public void addSpouseEntry(SpouseDVC s)
      throws RemoteException, SpouseException;
}
