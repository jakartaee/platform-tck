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

package com.sun.ts.tests.common.dao.coffee.variants;

import java.util.Properties;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ejb.CreateException;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.dao.DAOException;
import com.sun.ts.tests.common.dao.DataSourceDAO;

/**
 * DAO Object for table using the "coffee" DB schema:
 *
 * id (String, primary key) | name (String) | price (float)
 *
 */
public class StringPKDSCoffeeDAO extends DataSourceDAO
    implements StringPKCoffeeDAO {

  /** Name of the property whose value is the DB table name */
  public static final String DEFAULT_TABLE_PREFIX = "COFFEE_STRING_PK";

  protected static final String SQL_EXISTS = "select";

  protected static final String SQL_CREATE = "insert";

  protected static final String SQL_LOAD_PRICE = "select_price";

  protected static final String SQL_STORE_PRICE = "update_price";

  protected static final String SQL_DELETE = "delete";

  protected static final String SQL_DELETE_ALL = "delete_all";

  /**
   * Create a new CoffeeDAO object. If called from an EJB or a Web component,
   * you must make sure to call TestUtil.init() before creating a new DBSupport
   * object.
   */
  public StringPKDSCoffeeDAO(String sqlTablePrefix) throws DAOException {
    super(sqlTablePrefix);
  }

  /**
   * Create a new CoffeeDAO object. If called from an EJB or a Web component,
   * you must make sure to call TestUtil.init() before creating a new DBSupport
   * object.
   */
  public StringPKDSCoffeeDAO(String sqlTablePrefix, String user,
      String password) throws DAOException {

    super(sqlTablePrefix, user, password);
  }

  public boolean exists(String pkey) throws DAOException {
    PreparedStatement pStmt = null;
    ResultSet result = null;

    try {
      TestUtil.logTrace("[StringPKDSCoffeeDAO] exists(" + pkey + ")");

      pStmt = getStmt(SQL_EXISTS);
      pStmt.setString(1, pkey);
      result = pStmt.executeQuery();

      return result.next();
    } catch (SQLException e) {
      throw new DAOException("SQLException in keyExists: ", e);
    } finally {
      closeStmt(pStmt, result);
    }
  }

  public void create(String id, String name, float price)
      throws CreateException, DAOException {

    PreparedStatement pStmt = null;
    ResultSet result;

    try {
      TestUtil.logTrace("[StringPKDSCoffeeDAO] create {" + id + ", " + name
          + ", " + price + "}");

      pStmt = getStmt(SQL_CREATE);
      pStmt.setString(1, id);
      pStmt.setString(2, name);
      pStmt.setFloat(3, price);
      TestUtil.logTrace("[StringPKDSCoffeeDAO] Execute stmt" + pStmt);
      if (1 != pStmt.executeUpdate()) {
        throw new CreateException("DB INSERT failed");
      }
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new DAOException("SQLException in createNewRow", e);
    } finally {
      closeStmt(pStmt, null);
    }

    TestUtil.logTrace("[StringPKDSCoffeeDAO] New row created !");
  }

  public float loadPrice(String id) throws DAOException {

    PreparedStatement pStmt = null;
    ResultSet result = null;

    try {
      TestUtil.logTrace("[StringPKDSCoffeeDAO] loadPrice(" + id + ")");

      pStmt = getStmt(SQL_LOAD_PRICE);
      pStmt.setString(1, id);
      result = pStmt.executeQuery();
      if (!result.next()) {
        throw new SQLException("No record for PK = " + id);
      }

      return result.getFloat(1);
    } catch (SQLException e) {
      throw new DAOException("SQLException in loadPrice(): ", e);
    } finally {
      closeStmt(pStmt, result);
    }
  }

  public void storePrice(String id, float price) throws DAOException {

    PreparedStatement pStmt = null;
    ResultSet result;

    try {
      TestUtil.logTrace("[StringPKDSCoffeeDAO] storePrice {id=" + id
          + ", price=" + price + "}");
      pStmt = getStmt(SQL_STORE_PRICE);
      pStmt.setFloat(1, price);
      pStmt.setString(2, id);
      if (1 != pStmt.executeUpdate()) {
        throw new SQLException("UPDATE failed in storePrice");
      }
    } catch (SQLException e) {
      throw new DAOException("SQLException in storePrice(): ", e);
    } finally {
      closeStmt(pStmt, null);
    }
  }

  public void delete(String id) throws DAOException {

    PreparedStatement pStmt = null;
    ResultSet result;

    try {
      TestUtil.logTrace("[StringPKDSCoffeeDAO] delete()");
      pStmt = getStmt(SQL_DELETE);
      pStmt.setString(1, id);
      if (1 != pStmt.executeUpdate()) {
        throw new SQLException("DELETE failed in remove()");
      }
    } catch (SQLException e) {
      throw new DAOException("SQLException in remove(): ", e);
    } finally {
      closeStmt(pStmt, null);
    }
  }

}
