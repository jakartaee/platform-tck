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
 * id (CompoundPK, primary key) | name (String) | price (float)
 *
 */
public class CompoundPKDSCoffeeDAO extends DataSourceDAO
    implements CompoundPKCoffeeDAO {

  /** Name of the property whose value is the DB table name */
  public static final String DEFAULT_TABLE_PREFIX = "COFFEE_COMPOUND_PK";

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
  public CompoundPKDSCoffeeDAO(String sqlTablePrefix) throws DAOException {
    super(sqlTablePrefix);
  }

  /**
   * Create a new CoffeeDAO object. If called from an EJB or a Web component,
   * you must make sure to call TestUtil.init() before creating a new DBSupport
   * object.
   */
  public CompoundPKDSCoffeeDAO(String sqlTablePrefix, String user,
      String password) throws DAOException {

    super(sqlTablePrefix, user, password);
  }

  public boolean exists(CompoundPK pkey) throws DAOException {
    PreparedStatement pStmt = null;
    ResultSet result = null;

    try {
      TestUtil.logTrace("[CompoundPKDSCoffeeDAO] exists(" + pkey + ")");

      pStmt = getStmt(SQL_EXISTS);
      pStmt.setInt(1, pkey.pmIDInteger.intValue());
      pStmt.setString(2, pkey.pmIDString);
      pStmt.setFloat(3, pkey.pmIDFloat.floatValue());
      result = pStmt.executeQuery();

      return result.next();
    } catch (SQLException e) {
      throw new DAOException("SQLException in keyExists: ", e);
    } finally {
      closeStmt(pStmt, result);
    }
  }

  public void create(CompoundPK id, String name, float price)
      throws CreateException, DAOException {

    PreparedStatement pStmt = null;
    ResultSet result;

    try {
      TestUtil.logTrace("[CompoundPKDSCoffeeDAO] create {" + id + ", " + name
          + ", " + price + "}");

      pStmt = getStmt(SQL_CREATE);
      pStmt.setInt(1, id.pmIDInteger.intValue());
      pStmt.setString(2, id.pmIDString);
      pStmt.setFloat(3, id.pmIDFloat.floatValue());
      pStmt.setString(4, name);
      pStmt.setFloat(5, price);
      TestUtil.logTrace("[CompoundPKDSCoffeeDAO] Execute stmt" + pStmt);
      if (1 != pStmt.executeUpdate()) {
        throw new CreateException("DB INSERT failed");
      }
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new DAOException("SQLException in createNewRow", e);
    } finally {
      closeStmt(pStmt, null);
    }

    TestUtil.logTrace("[CompoundPKDSCoffeeDAO] New row created !");
  }

  public float loadPrice(CompoundPK id) throws DAOException {

    PreparedStatement pStmt = null;
    ResultSet result = null;

    try {
      TestUtil.logTrace("[CompoundPKDSCoffeeDAO] loadPrice(" + id + ")");

      pStmt = getStmt(SQL_LOAD_PRICE);
      pStmt.setInt(1, id.pmIDInteger.intValue());
      pStmt.setString(2, id.pmIDString);
      pStmt.setFloat(3, id.pmIDFloat.floatValue());

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

  public void storePrice(CompoundPK id, float price) throws DAOException {

    PreparedStatement pStmt = null;
    ResultSet result;

    try {
      TestUtil.logTrace("[CompoundPKDSCoffeeDAO] storePrice {id=" + id
          + ", price=" + price + "}");
      pStmt = getStmt(SQL_STORE_PRICE);
      pStmt.setFloat(1, price);
      pStmt.setInt(2, id.pmIDInteger.intValue());
      pStmt.setString(3, id.pmIDString);
      pStmt.setFloat(4, id.pmIDFloat.floatValue());

      if (1 != pStmt.executeUpdate()) {
        throw new SQLException("UPDATE failed in storePrice");
      }
    } catch (SQLException e) {
      throw new DAOException("SQLException in storePrice(): ", e);
    } finally {
      closeStmt(pStmt, null);
    }
  }

  public void delete(CompoundPK id) throws DAOException {

    PreparedStatement pStmt = null;
    ResultSet result;

    try {
      TestUtil.logTrace("[CompoundPKDSCoffeeDAO] delete()");
      pStmt = getStmt(SQL_DELETE);
      pStmt.setInt(1, id.pmIDInteger.intValue());
      pStmt.setString(2, id.pmIDString);
      pStmt.setFloat(3, id.pmIDFloat.floatValue());
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
