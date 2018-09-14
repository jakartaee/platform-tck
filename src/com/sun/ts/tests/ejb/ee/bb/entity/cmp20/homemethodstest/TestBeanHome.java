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

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.homemethodstest;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;

public interface TestBeanHome extends EJBHome {
  public TestBean create(int id, String first, String middle, String last,
      String accountNumber) throws RemoteException, CreateException;

  public TestBean create(int id, String accountNumber, String paymentType,
      double cardBalance, String creditCardNumber, String expires)
      throws RemoteException, CreateException;

  public TestBean createHomeAddress(int id, String street, String city,
      String state, int zip) throws RemoteException, CreateException;

  public TestBean createCountry(int id, String name, String code)
      throws RemoteException, CreateException;

  public TestBean createPhone(int id, String homePhone, String workPhone)
      throws RemoteException, CreateException;

  public void addCardFee(Integer key, double fee) throws RemoteException;

  public Collection findAllBeans() throws RemoteException, FinderException;

  public TestBean findByPrimaryKey(Integer key)
      throws RemoteException, FinderException;
}
