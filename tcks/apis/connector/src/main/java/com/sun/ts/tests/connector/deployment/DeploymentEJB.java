/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)DeploymentEJB.java	1.4  03/05/16
 */

package com.sun.ts.tests.connector.deployment;

import java.util.Properties;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;
import com.sun.ts.tests.common.connector.whitebox.TSDataSource;
import com.sun.ts.tests.connector.util.DBSupport;
import jakarta.annotation.PostConstruct;

public class DeploymentEJB {
  private TSNamingContext context = null;

  private Properties p = null;

  private boolean result = false;

  // con will be used for the table1 connection
  private transient TSConnection con;

  private DBSupport dbutil = null;

  // TSDataSources
  private TSDataSource ds = null;

  private String whitebox_tx = null;

  private String uname = null;

  private String password = null;

  public DeploymentEJB() {
  }

  public void initialize(Properties props) {
    p = props;
    try {
      this.context = new TSNamingContext();

      TestUtil.logTrace("initialize");
      TestUtil.init(props);

      System.out.println("Inside initialize of DeploymentEJB!");

      whitebox_tx = p.getProperty("whitebox-embed");

      System.out.println("whitebox value is " + whitebox_tx);
      TestUtil.logTrace("whitebox value is " + whitebox_tx);

      uname = p.getProperty("rauser1");
      password = p.getProperty("rapassword1");

      // Get the TSDataSource
      ds = (TSDataSource) context.lookup(whitebox_tx);
      TestUtil.logTrace("ds: " + ds);

      System.out.println("TSDataSource lookup OK!");
      TestUtil.logTrace("TSDataSource lookup OK!");
      dbutil = new DBSupport();

    } catch (Exception e) {
      TestUtil.logErr("init failed", e);
    }
  }

  public boolean testRarInEar() {
    System.out.println("Inside testRarInEar - Deployment EJB");

    // create EJB
    Deployment hr = null;
    try {
      TSNamingContext ic = new TSNamingContext();
      TestUtil.logMsg("Got the EJB!!");

      this.con = ds.getConnection();

      result = true;
      TestUtil.logMsg("Got connection from the DataSource.");
    } catch (Exception e) {
      // TestUtil.printStackTrace(e);
      TestUtil
          .logMsg("Exception caught on creating connection in Deployment EJB.");
    }

    // Verify connection object works by doing some end to end tests.
    TestUtil.logMsg("Checking for Connection Validity.");

    // Insert into table
    try {
      dbutil.insertIntoTable(con);
      TestUtil.logMsg("Values inserted into table!");
    } catch (Exception sqle) {
      TestUtil.printStackTrace(sqle);
      TestUtil.logMsg("Exception inserting into table.");
    }

    // Drop the table
    try {
      dbutil.dropTable(con);
      TestUtil.logMsg("Table has been dropped!");
    } catch (Exception sqle) {
      TestUtil.printStackTrace(sqle);
      TestUtil.logMsg("Exception dropping table.");
    }

    return result;
  }

  public void cleanup() {
    TestUtil.logTrace("cleanup");
    try {
      if (con != null) {
        con.close();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

}
