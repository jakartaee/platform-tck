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

package com.sun.ts.tests.ejb.ee.tx.txbean;

import java.rmi.*;
import java.util.*;
import javax.ejb.*;
import javax.transaction.*;
import com.sun.ts.lib.util.*;

/**
 * <P>
 * TxBean is a session EJB which contains the public interface for business
 * methods that communicate with a RDBMS via JDBC.
 * 
 * @author Elizabeth Blair
 * @since TS 1.0
 */

public interface TxBean extends EJBObject {
  // Database methods
  /**
   * Creates the specified table.
   * 
   * @param tName
   *          the name of the table
   * @return void
   * @exception RemoteException
   *              If the specified table could not be created.
   */
  public void createData(String tName) throws RemoteException;

  /**
   * Add a new row to the specified table, where key is unique.
   * 
   * @param tName
   *          the name of the table
   * @param key
   *          the unique key id of the new row
   * @return boolean <code>true</code> if row was inserted; false otherwise
   * @exception RemoteException
   *              If the new row could not be added.
   */
  public boolean insert(String tName, int key) throws RemoteException;

  /**
   * Delete a range of rows from the specified table.
   * 
   * @param tName
   *          the name of the table
   * @param fromKey
   *          the start of the range of rows
   * @param toKey
   *          the end of the range of row
   * @return void
   * @exception RemoteException
   *              If the range of rows could not be deleted.
   */
  public void delete(String tName, int fromKey, int toKey)
      throws RemoteException;

  /**
   * Delete a range of rows from the specified table and triggers a specified
   * Exception.
   * 
   * @param tName
   *          the name of the table
   * @param fromKey
   *          the start of the range of rows
   * @param toKey
   *          the end of the range of row
   * @param flag
   *          identifies the type of Exception to trigger
   * @return boolean true if rolledback; false otherwise
   * @exception RemoteException
   *              If the range of rows could not be deleted.
   * @exception AppException
   *              If AppException is triggered.
   */
  public boolean delete(String tName, int fromKey, int toKey, int flag)
      throws RemoteException, AppException;

  /**
   * Update a row in the specified table based on the unique key. This method
   * updates the row NAME field.
   * 
   * @param tName
   *          the name of the table
   * @param key
   *          the unique key id of the row
   * @param brandName
   *          the new brand
   * @return void
   * @exception RemoteException
   *              If the row could not be updated.
   */
  public void update(String tName, int key, String brandName)
      throws RemoteException;

  /**
   * Update a row in the specified table based on the unique key. This method
   * updates the row PRICE field.
   * 
   * @param tName
   *          the name of the table
   * @param key
   *          the unique key id of the row
   * @param price
   *          the new price
   * @return void
   * @exception RemoteException
   *              If the row could not be updated.
   */
  public void update(String tName, int key, float price) throws RemoteException;

  /**
   * Drop the specified table.
   * 
   * @param tName
   *          the name of the table
   * @return void
   * @exception RemoteException
   *              If the table could not be dropped.
   */
  public void destroyData(String tName) throws RemoteException;

  /**
   * Get the default isolation level for this resource.
   * 
   * @param tName
   *          the name of the table
   * @return int the default JDBC isolation level.
   * @exception RemoteException
   *              If the default isolation level could not be determined.
   */

  // Local resource manipulators
  public int getDefaultTxIsolationLevel(String tName) throws RemoteException;

  // Test Results methods
  /**
   * Get the results of the specified table.
   * 
   * @param tName
   *          the name of the table
   * @return Vector the Vector contains the entire contents of the specified
   *         table in a prescribed format.
   * @exception RemoteException
   *              If the table results could not be obtained.
   */
  public Vector getResults(String tName) throws RemoteException;

  /**
   * Get the results of a row in the specified table.
   * 
   * @param tName
   *          the name of the table
   * @param key
   *          the unique key identifer for the row
   * @return Vector the Vector contains the row of the specified table in a
   *         prescribed format.
   * @exception RemoteException
   *              If the table row result could not be obtained.
   */
  public Vector getResults(String tName, int key) throws RemoteException;

  /**
   * Required method of the TS test infrastructure. Allows server side logging
   * to be pulled back to the client.
   * 
   * @param p
   *          the TS environment specific properties
   * @return void
   * @exception RemoteException
   *              If the client/server logging relationship could not be
   *              established.
   */
  public void initLogging(Properties p) throws RemoteException;

  /**
   * Convenience method for use in testing EJB:SPEC:568.3.1. See documentation
   * in tx/session/stateful/cm/TxM_Single/Client.java
   * 
   * @return void
   * @exception RemoteException
   *              If the call fails in any way.
   */
  public void initLogging() throws RemoteException;

  // Exception methods
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

  // Utility methods
  /**
   * Utility method, dumps the entire contents of the specified table to the
   * test log.
   * 
   * @params dbResults the <code>Vector</code> which contains the table data.
   * @return void
   * @exception RemoteException
   *              If the table results could not be listed.
   */
  public void listTableData(Vector dbResults) throws RemoteException;
}
