/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TSNamingContextInterface;
import com.sun.ts.lib.util.TestUtil;

public class DataSourceConnection extends ServiceEETest
    implements JDBCTestConnectionManager {

  public Connection getConnection(Properties p)
      throws ClassNotFoundException, SQLException, Exception {
    TSNamingContextInterface jc = null;
    DataSource ds1 = null;
    Connection con = null;

    TestUtil.logTrace("Getting the initial context");
    jc = new TSNamingContext();
    TestUtil.logTrace("jc: " + jc.toString());

    TestUtil.logTrace("Looking up the JNDI DataSource names");
    ds1 = (DataSource) jc.lookup("java:comp/env/jdbc/DB1");
    TestUtil.logTrace("ds1: " + ds1.toString());

    TestUtil.logTrace("Attempting to make the connection");
    con = ds1.getConnection();
    TestUtil.logTrace("Made the connection via DataSource!");

    return con;
  }

}
