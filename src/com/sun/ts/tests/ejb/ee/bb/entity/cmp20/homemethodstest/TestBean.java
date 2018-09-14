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

import com.sun.ts.lib.util.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;

public interface TestBean extends EJBObject {
  // Business Methods for TestBean CMP Fields
  public String getFirst() throws RemoteException;

  public void setFirst(String s) throws RemoteException;

  public String getMiddle() throws RemoteException;

  public void setMiddle(String s) throws RemoteException;

  public String getLast() throws RemoteException;

  public void setLast(String s) throws RemoteException;

  public String getAccountNumber() throws RemoteException;

  public void setAccountNumber(String s) throws RemoteException;

  public String getPaymentType() throws RemoteException;

  public void setPaymentType(String s) throws RemoteException;

  public double getCardBalance() throws RemoteException;

  public void setCardBalance(double d) throws RemoteException;

  public String getCreditCardNumber() throws RemoteException;

  public void setCreditCardNumber(String s) throws RemoteException;

  public String getExpires() throws RemoteException;

  public void setExpires(String s) throws RemoteException;

  public String getStreet() throws RemoteException;

  public void setStreet(String s) throws RemoteException;

  public String getCity() throws RemoteException;

  public void setCity(String s) throws RemoteException;

  public String getState() throws RemoteException;

  public void setState(String s) throws RemoteException;

  public Integer getZip() throws RemoteException;

  public void setZip(Integer i) throws RemoteException;

  public String getName() throws RemoteException;

  public void setName(String s) throws RemoteException;

  public String getCode() throws RemoteException;

  public void setCode(String s) throws RemoteException;

  public String getHomePhone() throws RemoteException;

  public void setHomePhone(String s) throws RemoteException;

  public String getWorkPhone() throws RemoteException;

  public void setWorkPhone(String s) throws RemoteException;

  // Miscellaneous Business Methods
  public void initLogging(Properties p) throws RemoteException;
}
