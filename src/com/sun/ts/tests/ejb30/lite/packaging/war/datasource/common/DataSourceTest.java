/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.packaging.war.datasource.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

import javax.sql.DataSource;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;

public final class DataSourceTest {

  private DataSourceTest() {
  }

  public static void verifyDataSource(StringBuilder reason, boolean connect,
      String... names) {
    for (String name : names) {
      DataSource ds = (DataSource) ServiceLocator.lookupNoTry(name);
      reason
          .append(String.format("%nLooked up DataSource with name %s%n", name));
      verifyDataSource(reason, connect, ds);
    }
  }

  public static void verifyDataSource(StringBuilder reason, boolean connect,
      DataSource... dss) {
    if (!connect) {
      for (DataSource ds : dss) {
        // if the ds is null, it will cause NPE and thus fail the test
        reason.append(String.format("%nGot DataSource %s", ds.toString()));
      }
    } else {
      for (DataSource ds : dss) {
        Connection connection = null;
        try {
          Helper.getLogger().info("About to getConnection from " + ds);
          connection = ds.getConnection();
          reason.append(String.format(
              "Got DataSource %s and opened connection %s%n", ds, connection));
        } catch (SQLException e) {
          throw new RuntimeException(e);
        } finally {
          if (connection != null) {
            try {
              connection.close();
            } catch (SQLException e) {
              Helper.getLogger().log(Level.WARNING,
                  "Failed to close the connection", e);
            }
          }
        }
      }
    }
  }
}
