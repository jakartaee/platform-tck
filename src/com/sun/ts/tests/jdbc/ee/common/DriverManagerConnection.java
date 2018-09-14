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

package com.sun.ts.tests.jdbc.ee.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import java.sql.*;
import java.util.*;

public class DriverManagerConnection extends ServiceEETest
    implements JDBCTestConnectionManager {

  public Connection getConnection(Properties p)
      throws ClassNotFoundException, SQLException, Exception {
    Connection con = null;
    String dbName, dbUser, dbPassword, dbDriver;
    dbName = dbUser = dbPassword = dbDriver = null;

    dbName = p.getProperty("db1", "");
    dbUser = p.getProperty("user1", "");
    dbPassword = p.getProperty("password1", "");
    dbDriver = p.getProperty("Driver", "");

    TestUtil.logTrace("Database1 : " + dbName);
    TestUtil.logTrace("Username  : " + dbUser);
    TestUtil.logTrace("Password  : " + dbPassword);
    TestUtil.logTrace("Driver    : " + dbDriver);

    TestUtil.logTrace("About to load the driver class");
    Class.forName(dbDriver);
    TestUtil.logMsg("Successfully loaded the driver class");

    TestUtil.logTrace("About to make the DB connection");
    con = DriverManager.getConnection(dbName, dbUser, dbPassword);
    TestUtil.logMsg("Made the JDBC connection to the DB");

    return con;
  }

}
