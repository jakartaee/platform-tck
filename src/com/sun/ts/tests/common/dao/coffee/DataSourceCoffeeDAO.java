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
 * id (int, primary key) | name (String) | price (float)
 *
 */
public class DataSourceCoffeeDAO extends DataSourceDAO implements CoffeeDAO {

  /** Name of the property whose value is the DB table name */
  public static final String DEFAULT_TABLE_PREFIX = "COFFEE";

  protected static final String SQL_EXISTS = "select";

  protected static final String SQL_CREATE = "insert";

  protected static final String SQL_LOAD = "select";

  protected static final String SQL_LOAD_PRICE = "select_price";

  protected static final String SQL_STORE = "update";

  protected static final String SQL_STORE_PRICE = "update_price";

  protected static final String SQL_DELETE = "delete";

  protected static final String SQL_DELETE_ALL = "delete_all";

  /**
   * Create a new CoffeeDAO object. If called from an EJB or a Web component,
   * you must make sure to call TestUtil.init() before creating a new DBSupport
   * object.
   */
  public DataSourceCoffeeDAO(String sqlTablePrefix) throws DAOException {
    super(sqlTablePrefix);
  }

  /**
   * Create a new CoffeeDAO object. If called from an EJB or a Web component,
   * you must make sure to call TestUtil.init() before creating a new DBSupport
   * object.
   */
  public DataSourceCoffeeDAO(String sqlTablePrefix, String user,
      String password) throws DAOException {

    super(sqlTablePrefix, user, password);
  }

  public boolean exists(int pkey) throws DAOException {
    PreparedStatement pStmt = null;
    ResultSet result = null;

    try {
      TestUtil.logTrace("[DataSourceCoffeeDAO] exists(" + pkey + ")");

      pStmt = getStmt(SQL_EXISTS);
      pStmt.setInt(1, pkey);
      result = pStmt.executeQuery();

      return result.next();
    } catch (SQLException e) {
      throw new DAOException("SQLException in keyExists: ", e);
    } finally {
      closeStmt(pStmt, result);
    }
  }

  public void create(CoffeeBean bean) throws CreateException, DAOException {
    create(bean.getId(), bean.getName(), bean.getPrice());
  }

  public void create(int id, String name, float price)
      throws CreateException, DAOException {

    PreparedStatement pStmt = null;
    ResultSet result;

    try {
      TestUtil.logTrace("[DataSourceCoffeeDAO] create {" + id + ", " + name
          + ", " + price + "}");

      pStmt = getStmt(SQL_CREATE);
      pStmt.setInt(1, id);
      pStmt.setString(2, name);
      pStmt.setFloat(3, price);
      TestUtil.logTrace("[DataSourceCoffeeDAO] Execute stmt" + pStmt);
      if (1 != pStmt.executeUpdate()) {
        throw new CreateException("DB INSERT failed");
      }
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new DAOException("SQLException in createNewRow", e);
    } finally {
      closeStmt(pStmt, null);
    }

    TestUtil.logTrace("[DataSourceCoffeeDAO] New row created !");
  }

  public CoffeeBean load(int id) throws DAOException {

    PreparedStatement pStmt = null;
    ResultSet result = null;
    float price;
    String name;
    CoffeeBean bean;

    try {
      TestUtil.logTrace("[DataSourceCoffeeDAO] load(" + id + ")");

      pStmt = getStmt(SQL_LOAD);
      pStmt.setInt(1, id);
      result = pStmt.executeQuery();
      if (!result.next()) {
        throw new SQLException("No record for PK = " + id);
      }

      name = result.getString(1);
      price = result.getFloat(2);
      bean = new CoffeeBean(id, name, price);
    } catch (SQLException e) {
      throw new DAOException("SQLException in load(): ", e);
    } finally {
      closeStmt(pStmt, result);
    }

    return bean;
  }

  public float loadPrice(int id) throws DAOException {

    PreparedStatement pStmt = null;
    ResultSet result = null;

    try {
      TestUtil.logTrace("[DataSourceCoffeeDAO] loadPrice(" + id + ")");

      pStmt = getStmt(SQL_LOAD_PRICE);
      pStmt.setInt(1, id);
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

  public void store(CoffeeBean bean) throws DAOException {

    PreparedStatement pStmt = null;
    ResultSet result;

    if (null == bean) {
      throw new IllegalArgumentException("Null bean");
    }

    try {
      TestUtil.logTrace("[DataSourceCoffeeDAO] store {id=" + bean.getId()
          + ", name=" + bean.getName() + ", price=" + bean.getPrice() + "}");
      pStmt = getStmt(SQL_STORE);
      pStmt.setString(1, bean.getName());
      pStmt.setFloat(2, bean.getPrice());
      pStmt.setInt(3, bean.getId());
      if (1 != pStmt.executeUpdate()) {
        throw new SQLException("UPDATE failed in storePrice");
      }
    } catch (SQLException e) {
      throw new DAOException("SQLException in store(): ", e);
    } finally {
      closeStmt(pStmt, null);
    }
  }

  public void storePrice(int id, float price) throws DAOException {

    PreparedStatement pStmt = null;
    ResultSet result;

    try {
      TestUtil.logTrace("[DataSourceCoffeeDAO] storePrice {id=" + id
          + ", price=" + price + "}");
      pStmt = getStmt(SQL_STORE_PRICE);
      pStmt.setFloat(1, price);
      pStmt.setInt(2, id);
      if (1 != pStmt.executeUpdate()) {
        throw new SQLException("UPDATE failed in storePrice");
      }
    } catch (SQLException e) {
      throw new DAOException("SQLException in storePrice(): ", e);
    } finally {
      closeStmt(pStmt, null);
    }
  }

  public void delete(int id) throws DAOException {

    PreparedStatement pStmt = null;
    ResultSet result;
    try {
      TestUtil.logTrace("[DataSourceCoffeeDAO] delete()");
      pStmt = getStmt(SQL_DELETE);
      pStmt.setInt(1, id);
      if (1 != pStmt.executeUpdate()) {
        throw new SQLException("DELETE failed in remove()");
      }
    } catch (SQLException e) {
      throw new DAOException("SQLException in remove(): ", e);
    } finally {
      closeStmt(pStmt, null);
    }
  }

  public Collection nameToKeyCollection(String name) throws DAOException {
    PreparedStatement pStmt = null;
    ResultSet result = null;

    try {
      TestUtil.logTrace("[DataSourceCoffeeDAO] nameToKey");
      pStmt = getStmt("select_pk_by_name");
      pStmt.setString(1, name);
      result = pStmt.executeQuery();
      ArrayList a = new ArrayList();
      while (result.next()) {
        Integer pkey = new Integer(result.getInt(1));
        a.add(pkey);
      }
      return a;
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new DAOException("SQLException in nameToKey: ", e);
    } finally {
      closeStmt(pStmt, null);
    }
  } /* end nameToKey */

  public Collection priceToKeyCollection(float price) throws DAOException {
    PreparedStatement pStmt = null;
    ResultSet result = null;

    try {
      TestUtil.logTrace("priceToKey");
      pStmt = getStmt("select_pk_by_price");
      pStmt.setFloat(1, price);
      result = pStmt.executeQuery();
      ArrayList a = new ArrayList();
      while (result.next()) {
        Integer pkey = new Integer(result.getInt(1));
        a.add(pkey);
      }
      return a;
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new DAOException("SQL Exception in priceToKey", e);
    } finally {
      closeStmt(pStmt, null);
    }
  } /* end priceToKey */

  public Collection priceRangeToCollection(float min, float max)
      throws DAOException {
    PreparedStatement pStmt = null;
    ResultSet result = null;

    try {
      TestUtil.logTrace("priceRangeToCollection");
      pStmt = getStmt("select_pk_by_price_range");
      pStmt.setFloat(1, min);
      pStmt.setFloat(2, max);
      result = pStmt.executeQuery();
      ArrayList a = new ArrayList();
      while (result.next()) {
        Integer pkey = new Integer(result.getInt(1));
        a.add(pkey);
      }
      return a;
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new DAOException("SQL Exception in priceRangeToCollection", e);
    } finally {
      closeStmt(pStmt, null);
    }
  } /* end priceRangeToCollection */

  public Collection primaryKeyRangeToCollection(Integer min, Integer max)
      throws DAOException {
    PreparedStatement pStmt = null;
    ResultSet result = null;

    try {
      TestUtil.logTrace("primaryKeyRangeToCollection");
      pStmt = getStmt("select_pk_by_pk_range");
      pStmt.setInt(1, min.intValue());
      pStmt.setInt(2, max.intValue());
      result = pStmt.executeQuery();
      ArrayList a = new ArrayList();
      while (result.next()) {
        Integer pkey = new Integer(result.getInt(1));
        a.add(pkey);
      }
      return a;
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new DAOException("SQL Exception in primaryKeyRangeToCollection: ",
          e);
    } finally {
      closeStmt(pStmt, null);
    }
  } /* end primaryKeyRangeToCollection */

}
