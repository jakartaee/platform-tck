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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

/**
 * Data Access Object
 *
 * Abstract functionalities shared by all DAOs (independatly of the back-end and
 * the persitent data schema).
 */
public interface DAO extends java.io.Serializable {

  /** Strict mode attempting to detect mis-behaving client code (debug) */
  public static final int STRICT_POLICY = 1;

  /** More permissive code more suitable for a production environment */
  public static final int STANDARD_POLICY = 2;

  /**
   * Initialize external resources. Must be called before calling calling any
   * other method on a DAO.
   */
  public void startSession() throws DAOException;

  /**
   * Release external resources (they can be reinitialized later by calling
   * startSession()). If the DAO is used from an EJB, the EJB code must make
   * sure to close the session before any serialization of the EJB occurs.
   * Typically and EJB will start a new session when entering an EJB callback
   * and close it before exiting the callback.
   */
  public void stopSession();

  /** Set DAO policy (STRICY_POLICY or STANDARD_POLICY) */
  public void setPolicy(int policy);

}
