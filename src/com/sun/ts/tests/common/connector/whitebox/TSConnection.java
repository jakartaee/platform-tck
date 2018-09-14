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

package com.sun.ts.tests.common.connector.whitebox;

import java.util.Vector;
import java.util.Hashtable;
import com.sun.ts.tests.common.connector.util.*;

/**
 * A Pooled object interface.
 *
 * @version 2.0, 06/06/02
 * @author Gursharan Singh/Binod P.G
 */
public interface TSConnection {

  /**
   * Insert a key and value in Test Information System (TSEIS).
   *
   * @param key
   *          Key to insert.
   * @param value
   *          value to insert.
   * @throws Exception
   *           If the key is already present in the EIS.
   */
  public void insert(String key, String value) throws Exception;

  /**
   * Delete the key and value from Test Information System (TSEIS).
   *
   * @param key
   *          Key to delete.
   * @throws Exception
   *           If the key is not present in the EIS.
   */
  public void delete(String key) throws Exception;

  /**
   * Update the key and value in Test Information System (TSEIS).
   *
   * @param key
   *          Key to update.
   * @param value
   *          value to update.
   * @throws Exception
   *           If the key is not present in the EIS.
   */
  public void update(String key, String value) throws Exception;

  /**
   * Read the value for the key.
   *
   * @param key
   *          Key to read.
   * @return String value.
   * @throws Exception
   *           If the key is not present in the EIS.
   */
  public String readValue(String key) throws Exception;

  /**
   * Drops all data in the EIS.
   *
   * @throws Exception
   *           If there is any exception while droppping.
   */
  public void dropTable() throws Exception;

  /**
   * Rolls back all the operations.
   */
  public void rollback();

  /**
   * Commits all the operations.
   *
   * @throws Exception
   *           If commit fails.
   */
  public void commit() throws Exception;

  public void begin() throws Exception;

  /**
   * Closes this connection.
   *
   * @throws Exception
   *           If close fails.
   */
  public void close() throws Exception;

  /**
   * Sets the auto-commit flag to the value passed in. True indicates that all
   * the operation will be committed. If a false is passed, EIS will wait until
   * an explicit commit is executed.
   *
   * @param flag
   *          True or False
   */
  public void setAutoCommit(boolean flag);

  /**
   * Get the auto-commt flag value.
   *
   * @return the boolean value indicating auto-commit.
   */
  public boolean getAutoCommit();

  /**
   * Get all the data in the TSEis. Only Data is returned. Keys are not.
   *
   * @return Vector containing all the data values.
   * @throws Exception
   *           If read fails.
   */
  public Vector readData() throws Exception;

  /**
   * Get the data cache of the connection accumulated during a transaction.
   *
   * @returns Data cache of operations on the connection.
   */
  public Hashtable getTempTable();
}
