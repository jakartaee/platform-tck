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
package com.sun.ts.tests.compat12.ejb.Tx_Single;

import java.rmi.*;
import java.util.*;
import javax.ejb.*;
import com.sun.ts.lib.util.*;

/**
 * The TxECMPBean is an entity EJB. Most of the business methods of the
 * TxECMPBean class do not access the database. Instead, these business methods
 * update the instance variables, which are written to the database when the EJB
 * Container calls ejbStore().
 */

public interface TxECMPBean extends EJBObject {
  /**
   * Retrieve the current brand name.
   * 
   * @return the current brand name
   * @exception RemoteException
   *              if an error occurs while retrieving the brand name
   */
  public String getBrandName() throws RemoteException;

  /**
   * Retrieve the current price
   * 
   * @return the current price
   * @exception RemoteException
   *              if an error occurs while retrieving the price
   */
  public float getPrice() throws RemoteException;

  /**
   * Check the rolled back status for the recent transaction
   * 
   * @return true if the transaction was rolled back; else false
   * @exception RemoteException
   *              if the rolled back status could not be obtained.
   */
  public boolean getTxRollbackStatus() throws RemoteException;

  /**
   * change the brand name
   * 
   * @param newBrandName
   *          the name of the new brand
   * @return void
   * @exception RemoteException
   *              if the brand name could not be obtained
   */
  public void updateBrandName(String newBrandName) throws RemoteException;

  /**
   * change the brand name and throw an Exception if indicated by the flag.
   * 
   * @param newBrandName
   *          the name of the new brand
   * @param flag
   *          corresponds to the intended exception.
   * @return void
   * @exception RemoteException
   *              if the brand name could not be obtained
   * @exception AppException
   *              if triggered by the flag
   */
  public void updateBrandName(String newBrandName, int flag)
      throws RemoteException, AppException;

  /**
   * change the brand name and rollback if indicated by flag
   * 
   * @param newBrandName
   *          the name of the new brand
   * @param flag
   *          corresponds to the intended exception.
   * @return true if the transaction was rolled back; else false.
   * @exception RemoteException
   *              if the brand name could not be obtained
   * @exception AppException
   *              - if triggered by the flag
   */

  public boolean updateBrandNameRB(String newBrandName, int flag)
      throws RemoteException, AppException;

  /**
   * change the price
   * 
   * @param newPriceName
   *          the new price
   * @return void
   * @exception RemoteException
   *              if the price could not be updated
   */
  public void updatePrice(float newPriceName) throws RemoteException;

  /**
   * Throw AppException
   * 
   * @return void
   * @exception RemoteException
   *              If an unexpected Exception occurs.
   * @exception AppException
   *              Throw the requested AppException.
   */
  public void throwAppException() throws RemoteException, AppException;

  /**
   * Throw SysException
   * 
   * @return void
   * @exception RemoteException
   *              If an unexpected Exception occurs.
   * @exception SysException
   *              Throw the requested SysException.
   */
  public void throwSysException() throws RemoteException;

  /**
   * Throw RemoteException
   * 
   * @return void
   * @exception RemoteException
   *              Throw the requested RemoteException.
   */
  public void throwRemoteException() throws RemoteException;

  /**
   * Throw EJBException
   * 
   * @return void
   * @exception RemoteException
   *              If an unexpected Exception occurs.
   * @exception EJBException
   *              Throw the requested EJBException.
   */
  public void throwEJBException() throws RemoteException;

  /**
   * Throw Error
   * 
   * @return void
   * @exception RemoteException
   *              If an unexpected Exception occurs.
   * @exception Error
   *              Throw the requested Error.
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

  /**
   * Sets the remove exception flag to true.
   * 
   * @return void
   * @exception RemoteException
   *              If an unexpected Exception occurs.
   */
  public void setRemoveException() throws RemoteException;
}
