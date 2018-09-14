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

package com.sun.ts.tests.connector.localTx.transaction.conSharing3;

import java.rmi.*;
import java.util.*;
import javax.ejb.*;
import javax.transaction.*;
import com.sun.ts.lib.util.*;

public interface BeanA extends EJBObject {
  // Database methods
  /**
   * Make a JDBC <code>Connection</code> to the specified database. DataSource
   * is required for the <code>Connection</code>.
   * 
   * @param tName
   *          the name of the table
   * @return void
   * @exception RemoteException
   *              If the JDBC <code>Connection</code> could not be made.
   */
  public void dbConnectfirst() throws RemoteException;

  public void dbConnectsecond() throws RemoteException;

  /**
   * Creates the specified table.
   * 
   * @param tName
   *          the name of the table
   * @return void
   * @exception RemoteException
   *              If the specified table could not be created.
   */
  public void createData() throws RemoteException;

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
  public boolean insert(String str) throws RemoteException;

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
  public void delete(String str) throws RemoteException;

  /**
   * Drop the specified table.
   * 
   * @param tName
   *          the name of the table
   * @return void
   * @exception RemoteException
   *              If the table could not be dropped.
   */
  public void destroyData() throws RemoteException;

  /**
   * Closes the JDBC <code>Connection</code> to the RDBMS.
   * 
   * @param tName
   *          the name of the table
   * @return void
   * @exception RemoteException
   *              If the <code>Connection</code> could not be closed.
   */
  public void dbUnConnect() throws RemoteException;

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
  public Vector getResults() throws RemoteException;

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
}
