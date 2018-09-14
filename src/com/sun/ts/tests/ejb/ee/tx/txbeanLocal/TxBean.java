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

package com.sun.ts.tests.ejb.ee.tx.txbeanLocal;

import java.rmi.*;
import java.util.*;
import javax.ejb.*;
import javax.transaction.*;
import com.sun.ts.lib.util.*;

/**
 * <P>
 * TxBean is a session EJB which contains the public interface for business
 * methods that communicate with a RDBMS via JDBC.
 */

public interface TxBean extends EJBLocalObject {
  // Database methods
  /**
   * Creates the specified table.
   * 
   * @param tName
   *          the name of the table
   * @return void
   */
  public void createData(String tName);

  /**
   * Add a new row to the specified table, where key is unique.
   * 
   * @param tName
   *          the name of the table
   * @param key
   *          the unique key id of the new row
   * @return boolean <code>true</code> if row was inserted; false otherwise
   */
  public boolean insert(String tName, int key);

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
   */
  public void delete(String tName, int fromKey, int toKey);

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
   * @exception AppException
   *              If AppException is triggered.
   */
  public boolean delete(String tName, int fromKey, int toKey, int flag)
      throws AppException;

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
   */
  public void update(String tName, int key, String brandName);

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
   */
  public void update(String tName, int key, float price);

  /**
   * Drop the specified table.
   * 
   * @param tName
   *          the name of the table
   * @return void
   */
  public void destroyData(String tName);

  // Local resource manipulators
  /**
   * Get the default isolation level for this resource.
   * 
   * @param tName
   *          the name of the table
   * @return int the default JDBC isolation level.
   */
  public int getDefaultTxIsolationLevel(String tName);

  // Test Results methods
  /**
   * Get the results of the specified table.
   * 
   * @param tName
   *          the name of the table
   * @return Vector the Vector contains the entire contents of the specified
   *         table in a prescribed format.
   */
  public Vector getResults(String tName);

  /**
   * Get the results of a row in the specified table.
   * 
   * @param tName
   *          the name of the table
   * @param key
   *          the unique key identifer for the row
   * @return Vector the Vector contains the row of the specified table in a
   *         prescribed format.
   */
  public Vector getResults(String tName, int key);

  /**
   * Required method of the TS test infrastructure. Allows server side logging
   * to be pulled back to the client.
   * 
   * @param p
   *          the TS environment specific properties
   * @return void not be established.
   */
  public void initLogging(Properties p);

  /**
   * Convenience method for use in testing EJB:SPEC:568.3.1. See documentation
   * in tx/sessionLocal/stateful/cm/TxM_Single/Client.java
   * 
   * @return void
   */
  public void initLogging();

  // Exception methods
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

  // Utility methods
  /**
   * Utility method, dumps the entire contents of the specified table to the
   * test log.
   * 
   * @params dbResults the <code>Vector</code> which contains the table data.
   * @return void
   */
  public void listTableData(Vector dbResults);
}
