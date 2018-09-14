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
import javax.ejb.*;
import com.sun.ts.lib.util.*;

/**
 * <P>
 * TxEBean is an entity EJB. Most of the business methods of the TxEBean class
 * do not access the database. Instead, these business methods update the
 * instance variables, which are written to the database when the EJB Container
 * calls ejbStore().
 */

public interface TxEBean extends EJBObject {
  /**
   * This method is called to retrieve the current brand name.
   *
   * @return current brand name.
   * @exception RemoteException
   *              if unable to retrieve the brand name
   */
  public String getBrandName() throws RemoteException;

  /**
   * This method is called to retrieve the price.
   *
   * @return current price.
   * @exception RemoteException
   *              if unable to retrieve the price
   */
  public float getPrice() throws RemoteException;

  /**
   * This method accesses the database for the brand name.
   *
   * @return brand name from the database
   * @exception RemoteException
   *              if unable to get the brand name from the database
   */
  public String getDbBrandName() throws RemoteException;

  /**
   * This method accesses the database for the price.
   *
   * @return price from the database
   * @exception RemoteException
   *              if unable to get the price from the database
   */
  public float getDbPrice() throws RemoteException;

  /**
   * This method is called to update the brand name.
   * 
   * @param newBrandName
   *          the name of the new brand.
   * @return void
   * @exception RemoteException
   *              if unable to update the brand name
   */
  public void updateBrandName(String newBrandName) throws RemoteException;

  /**
   * This method is called to update the brand name.
   *
   * @param newBrandName
   *          new brand name
   * @param flag
   *          flags the intended exception
   * @return boolean true if rolled back; else false
   * @exception RemoteException
   *              if unable to update the brand name
   * @exception AppException
   *              triggered by flag
   */
  public boolean updateBrandName(String newBrandName, int flag)
      throws RemoteException, AppException;

  /**
   * This method is called to update the brand name.
   *
   * @param newBrandName
   *          new brand name
   * @param flag
   *          flags the intended exception
   * @return boolean true if rolled back; else false
   * @exception RemoteException
   *              if unable to update the brand name
   * @exception AppException
   *              triggered by the flag.
   */
  public boolean updateBrandNameRB(String newBrandName, int flag)
      throws RemoteException, AppException;

  /**
   * This method is called to update the price.
   *
   * @param newPriceName
   *          new price name
   * @return void
   * @exception RemoteException
   *              if an unexpected exception occurs
   */
  public void updatePrice(float newPriceName) throws RemoteException;

  /**
   * This method throws an Application Exception
   *
   * @return void
   * @exception RemoteException
   *              if an unexpected exception occurs.
   * @exception AppException
   *              Throw the requested AppException
   */
  public void throwAppException() throws RemoteException, AppException;

  /**
   * This method throws a System Exception
   *
   * @return void
   * @exception RemoteException
   *              if an unexpected exception occurs.
   * @exception SysException
   *              Throw the requested SysException
   */
  public void throwSysException() throws RemoteException;

  /**
   * This method throws an EJB Exception
   *
   * @return void
   * @exception RemoteException
   *              if an unexpected exception occurs.
   * @exception EJBException
   *              Throw the requested EJBException
   */
  public void throwEJBException() throws RemoteException;

  /**
   * This method throws an Error
   *
   * @return void
   * @exception Throws
   *              RemoteException if an unexpected exception occurs.
   * @exception Error
   *              Throw the requested Error
   */
  public void throwError() throws RemoteException;

  /**
   * This method throws a RemoveException
   *
   * @return void
   * @exception Forces
   *              a RemoveException.
   * @exception Throws
   *              the requested RemoveException
   */
  public void throwRemoveException() throws RemoteException, RemoveException;
}
