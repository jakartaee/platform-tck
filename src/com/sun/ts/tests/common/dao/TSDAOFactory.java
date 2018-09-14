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

package com.sun.ts.tests.common.dao;

import java.util.Properties;
import com.sun.ts.tests.common.dao.coffee.CoffeeDAO;
import com.sun.ts.tests.common.dao.coffee.DataSourceCoffeeDAO;
import com.sun.ts.tests.common.dao.coffee.TxCoffeeDAO;
import com.sun.ts.tests.common.dao.coffee.variants.StringPKCoffeeDAO;
import com.sun.ts.tests.common.dao.coffee.variants.StringPKDSCoffeeDAO;
import com.sun.ts.tests.common.dao.coffee.variants.LongPKCoffeeDAO;
import com.sun.ts.tests.common.dao.coffee.variants.LongPKDSCoffeeDAO;
import com.sun.ts.tests.common.dao.coffee.variants.FloatPKCoffeeDAO;
import com.sun.ts.tests.common.dao.coffee.variants.FloatPKDSCoffeeDAO;
import com.sun.ts.tests.common.dao.coffee.variants.CompoundPKCoffeeDAO;
import com.sun.ts.tests.common.dao.coffee.variants.CompoundPKDSCoffeeDAO;

/**
 * 
 */
public class TSDAOFactory extends DAOFactory {

  private boolean debug = true;

  private static final String PKG = "com.sun.ts.tests.common.dao.daofactory";

  /*
   * Back-end properties
   */
  public static final String BACKEND_ACCESS_PROPERTY = PKG + ".backend.access";

  public static final String BACKEND_ACCESS_DATASOURCE = "DataSource";

  /*
   * Authorization properties
   */
  public static final String AUTH_TYPE_PROPERTY = PKG + ".authorization.type";

  public static final String AUTH_TYPE_CONTAINER = "Container";

  public static final String AUTH_TYPE_COMPONENT = "Component";

  public static final String AUTH_USER_PROPERTY = PKG
      + ".authorization.component.user";

  public static final String AUTH_PASSWORD_PROPERTY = PKG
      + ".authorization.component.password";

  /*
   * DataSource backend specific properties
   */
  public static final String TABLE_PREFIX_PROPERTY = PKG + ".db.table.prefix";

  /** Data structure used for Coffee DAO's */
  public class CoffeeDAOInfo {
    public String tablePrefix;

    public boolean containerAuth;

    public String user;

    public String password;
  }

  public CoffeeDAO getCoffeeDAO() throws DAOException {
    return getCoffeeDAO(new Properties());
  }

  public CoffeeDAO getCoffeeDAO(Properties p) throws DAOException {
    CoffeeDAOInfo info;
    CoffeeDAO dao;

    info = parseCoffeeProps(p, DataSourceCoffeeDAO.DEFAULT_TABLE_PREFIX);

    if (info.containerAuth) {
      dao = new DataSourceCoffeeDAO(info.tablePrefix);
    } else {
      dao = new DataSourceCoffeeDAO(info.tablePrefix, info.user, info.password);
    }

    if (debug) {
      dao.setPolicy(DAO.STRICT_POLICY);
    }

    return dao;
  }

  public TxCoffeeDAO getTxCoffeeDAO() throws DAOException {
    throw new UnsupportedOperationException("To be implemented");
  }

  public TxCoffeeDAO getTxCoffeeDAO(Properties p) throws DAOException {
    throw new UnsupportedOperationException("To be implemented");
  }

  public StringPKCoffeeDAO getStringPKCoffeeDAO() throws DAOException {
    return getStringPKCoffeeDAO(new Properties());
  }

  public StringPKCoffeeDAO getStringPKCoffeeDAO(Properties p)
      throws DAOException {

    CoffeeDAOInfo info;
    StringPKCoffeeDAO dao;

    info = parseCoffeeProps(p, StringPKDSCoffeeDAO.DEFAULT_TABLE_PREFIX);

    if (info.containerAuth) {
      dao = new StringPKDSCoffeeDAO(info.tablePrefix);
    } else {
      dao = new StringPKDSCoffeeDAO(info.tablePrefix, info.user, info.password);
    }
    if (debug) {
      dao.setPolicy(DAO.STRICT_POLICY);
    }

    return dao;
  }

  public LongPKCoffeeDAO getLongPKCoffeeDAO() throws DAOException {
    return getLongPKCoffeeDAO(new Properties());
  }

  public LongPKCoffeeDAO getLongPKCoffeeDAO(Properties p) throws DAOException {

    CoffeeDAOInfo info;
    LongPKCoffeeDAO dao;

    info = parseCoffeeProps(p, LongPKDSCoffeeDAO.DEFAULT_TABLE_PREFIX);

    if (info.containerAuth) {
      dao = new LongPKDSCoffeeDAO(info.tablePrefix);
    } else {
      dao = new LongPKDSCoffeeDAO(info.tablePrefix, info.user, info.password);
    }
    if (debug) {
      dao.setPolicy(DAO.STRICT_POLICY);
    }

    return dao;
  }

  public FloatPKCoffeeDAO getFloatPKCoffeeDAO() throws DAOException {
    return getFloatPKCoffeeDAO(new Properties());
  }

  public FloatPKCoffeeDAO getFloatPKCoffeeDAO(Properties p)
      throws DAOException {

    CoffeeDAOInfo info;
    FloatPKCoffeeDAO dao;

    info = parseCoffeeProps(p, FloatPKDSCoffeeDAO.DEFAULT_TABLE_PREFIX);

    if (info.containerAuth) {
      dao = new FloatPKDSCoffeeDAO(info.tablePrefix);
    } else {
      dao = new FloatPKDSCoffeeDAO(info.tablePrefix, info.user, info.password);
    }
    if (debug) {
      dao.setPolicy(DAO.STRICT_POLICY);
    }

    return dao;
  }

  public CompoundPKCoffeeDAO getCompoundPKCoffeeDAO() throws DAOException {
    return getCompoundPKCoffeeDAO(new Properties());
  }

  public CompoundPKCoffeeDAO getCompoundPKCoffeeDAO(Properties p)
      throws DAOException {

    CoffeeDAOInfo info;
    CompoundPKCoffeeDAO dao;

    info = parseCoffeeProps(p, CompoundPKDSCoffeeDAO.DEFAULT_TABLE_PREFIX);

    if (info.containerAuth) {
      dao = new CompoundPKDSCoffeeDAO(info.tablePrefix);
    } else {
      dao = new CompoundPKDSCoffeeDAO(info.tablePrefix, info.user,
          info.password);
    }
    if (debug) {
      dao.setPolicy(DAO.STRICT_POLICY);
    }

    return dao;
  }

  protected CoffeeDAOInfo parseCoffeeProps(Properties props,
      String defaultTablePrefix) throws DAOException {

    String authType;
    String backendAccess;
    CoffeeDAOInfo info = new CoffeeDAOInfo();

    if (null == props) {
      throw new IllegalArgumentException("Null props");
    }
    if (null == defaultTablePrefix || "".equals(defaultTablePrefix)) {
      throw new IllegalArgumentException(
          "Invalid table prefix: " + defaultTablePrefix);
    }

    info.tablePrefix = props.getProperty(TABLE_PREFIX_PROPERTY,
        defaultTablePrefix);

    authType = props.getProperty(AUTH_TYPE_PROPERTY, AUTH_TYPE_CONTAINER);
    backendAccess = props.getProperty(BACKEND_ACCESS_PROPERTY,
        BACKEND_ACCESS_DATASOURCE);

    if (!backendAccess.equals(BACKEND_ACCESS_DATASOURCE)) {
      /* Only supports DataSource access for now */
      throw new InvalidDAOSettingException(BACKEND_ACCESS_PROPERTY,
          backendAccess, "Unsupported backend access");
    }

    if (null == info.tablePrefix || info.tablePrefix.equals("")) {
      throw new InvalidDAOSettingException(TABLE_PREFIX_PROPERTY,
          info.tablePrefix, "Null or empty table prefix");
    }
    info.containerAuth = authType.equals(AUTH_TYPE_CONTAINER);

    if (!(authType.equals(AUTH_TYPE_COMPONENT) || info.containerAuth)) {
      throw new InvalidDAOSettingException(AUTH_TYPE_PROPERTY, authType,
          "Unknown authorization type");
    }

    if (!info.containerAuth) {
      info.user = props.getProperty(AUTH_USER_PROPERTY);
      if (null == info.user) {
        throw new InvalidDAOSettingException(AUTH_USER_PROPERTY, info.user,
            "No username!");
      }
      info.password = props.getProperty(AUTH_PASSWORD_PROPERTY);
      if (null == info.password) {
        throw new InvalidDAOSettingException(AUTH_PASSWORD_PROPERTY,
            info.password, "No password!");
      }
    }

    return info;
  }

}
