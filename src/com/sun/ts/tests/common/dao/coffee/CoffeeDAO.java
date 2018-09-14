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

package com.sun.ts.tests.common.dao.coffee;

import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ejb.CreateException;
import com.sun.ts.tests.common.dao.DAO;
import com.sun.ts.tests.common.dao.DAOException;

/**
 * DAO Object for table using the "coffee" DB schema:
 *
 * id (int, primary key) | name (String) | price (float)
 *
 */
public interface CoffeeDAO extends DAO {

  public boolean exists(int id) throws DAOException;

  public void create(CoffeeBean bean) throws CreateException, DAOException;

  public void create(int id, String name, float price)
      throws CreateException, DAOException;

  public CoffeeBean load(int id) throws DAOException;

  public float loadPrice(int id) throws DAOException;

  public void store(CoffeeBean bean) throws DAOException;

  public void storePrice(int id, float price) throws DAOException;

  public Collection priceToKeyCollection(float price) throws DAOException;

  public Collection priceRangeToCollection(float min, float max)
      throws DAOException;

  public Collection primaryKeyRangeToCollection(Integer min, Integer max)
      throws DAOException;

  public Collection nameToKeyCollection(String name) throws DAOException;

  public void delete(int id) throws DAOException;

  public void deleteAll() throws DAOException;

  /**
   * Convenience method for test setup. Start its own session and delete all
   * pre-existing entities
   */
  public void cleanup() throws DAOException;

}
