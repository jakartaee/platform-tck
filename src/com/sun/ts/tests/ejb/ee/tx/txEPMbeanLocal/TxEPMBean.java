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
package com.sun.ts.tests.ejb.ee.tx.txEPMbeanLocal;

import java.rmi.*;
import java.util.*;
import javax.ejb.*;
import com.sun.ts.lib.util.*;

/**
 * The TxEPMBean is an entity EJB. Most of the business methods of the TxEPMBean
 * class do not access the database. Instead, these business methods update the
 * instance variables, which are written to the database when the EJB Container
 * calls ejbStore().
 */

public interface TxEPMBean extends EJBLocalObject {

  /**
   * Accessors methods for CMP bean fields.
   *
   */

  public Integer getKeyId();

  public String getBrandName();

  public void setBrandName(String brand);

  public float getPrice();

  public void setPrice(float price);

  /**
   * change the brand name
   * 
   * @param newBrandName
   *          the name of the new brand
   * @return void
   */
  public void updateBrandName(String newBrandName);

  /**
   * change the brand name and throw an Exception if indicated by the flag.
   * 
   * @param newBrandName
   *          the name of the new brand
   * @param flag
   *          corresponds to the intended exception.
   * @return true if the transaction was rolled back; else false.
   * @exception AppException
   *              if triggered by the flag
   */
  public boolean updateBrandName(String newBrandName, int flag)
      throws AppException;

  /**
   * change the brand name and rollback if indicated by flag
   * 
   * @param newBrandName
   *          the name of the new brand
   * @param flag
   *          corresponds to the intended exception.
   * @return true if the transaction was rolled back; else false.
   * @exception AppException
   *              - if triggered by the flag
   */

  public boolean updateBrandNameRB(String newBrandName, int flag)
      throws AppException;

  /**
   * change the price
   * 
   * @param newPriceName
   *          the new price
   * @return void
   */
  public void updatePrice(float newPriceName);

  /**
   * Throw AppException
   * 
   * @return void
   * @exception AppException
   *              Throw the requested AppException.
   */
  public void throwAppException() throws AppException;

  /**
   * Throw SysException
   * 
   * @return void
   * @exception SysException
   *              Throw the requested SysException.
   */
  public void throwSysException();

  /**
   * Throw EJBException
   * 
   * @return void
   * @exception If
   *              an unexpected Exception occurs.
   * @exception EJBException
   *              Throw the requested EJBException.
   */
  public void throwEJBException();

  /**
   * Throw Error
   * 
   * @return void
   * @exception Error
   *              Throw the requested Error.
   */
  public void throwError();

  /**
   * This method throws a RemoveException
   *
   * @return void
   * @exception Forces
   *              a RemoveException.
   * @exception Throws
   *              the requested RemoveException
   */
  public void throwRemoveException() throws RemoveException;
}
