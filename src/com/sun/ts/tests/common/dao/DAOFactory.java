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
import com.sun.ts.tests.common.dao.coffee.TxCoffeeDAO;
import com.sun.ts.tests.common.dao.coffee.variants.StringPKCoffeeDAO;
import com.sun.ts.tests.common.dao.coffee.variants.LongPKCoffeeDAO;
import com.sun.ts.tests.common.dao.coffee.variants.FloatPKCoffeeDAO;
import com.sun.ts.tests.common.dao.coffee.variants.CompoundPKCoffeeDAO;

/**
 * 
 */
public abstract class DAOFactory implements java.io.Serializable {

  protected static DAOFactory _INSTANCE;

  /** Return a DAOFactory instance. This method is NOT thread safe */
  public final static DAOFactory getInstance() {
    return getInstance(null);
  }

  /** Return a DAOFactory instance. This method is NOT thread safe */
  public final static DAOFactory getInstance(Properties props) {
    if (null == _INSTANCE) {
      _INSTANCE = new TSDAOFactory();
    }

    return _INSTANCE;
  }

  public abstract CoffeeDAO getCoffeeDAO() throws DAOException;

  public abstract CoffeeDAO getCoffeeDAO(Properties props) throws DAOException;

  public abstract TxCoffeeDAO getTxCoffeeDAO() throws DAOException;

  public abstract TxCoffeeDAO getTxCoffeeDAO(Properties props)
      throws DAOException;

  public abstract StringPKCoffeeDAO getStringPKCoffeeDAO() throws DAOException;

  public abstract LongPKCoffeeDAO getLongPKCoffeeDAO() throws DAOException;

  public abstract FloatPKCoffeeDAO getFloatPKCoffeeDAO() throws DAOException;

  public abstract CompoundPKCoffeeDAO getCompoundPKCoffeeDAO()
      throws DAOException;

}
