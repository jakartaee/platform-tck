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
 * @(#)JTATestEJB.java	1.3  03/05/16
 */

package com.sun.ts.tests.connector.xa.transaction.jta;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import com.sun.ts.tests.connector.util.DBSupport;
import com.sun.ts.tests.common.connector.whitebox.TSDataSource;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;
import com.sun.javatest.Status;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

public class JTATestEJB implements SessionBean {
  private TSNamingContext context;

  private String str;

  private Properties p = null;

  // con will be used for the table1 connection
  private transient TSConnection con;

  private DBSupport dbutil = null;

  // TSDataSources
  private TSDataSource ds1;

  private String whitebox_xa = null;

  private String uname = null;

  private String password = null;

  public JTATestEJB() {
  }

  public void ejbCreate(Properties props) throws CreateException {
    p = props;
    try {
      TestUtil.logTrace("ejbCreate");
      System.out.println("ejbCreate done");
      TestUtil.init(props);
      whitebox_xa = p.getProperty("whitebox-xa");
      System.out.println("got props");
      uname = p.getProperty("rauser1");
      password = p.getProperty("rapassword1");
      System.out.println("whitebox_xa is : " + whitebox_xa);
      // Get the TSDataSource
      ds1 = (TSDataSource) context.lookup(whitebox_xa);
      TestUtil.logTrace("ds1: " + ds1);
      System.out.println("TSDataSource lookup OK!");
      TestUtil.logTrace("TSDataSource lookup OK!");
      dbutil = new DBSupport();
      this.str = str;
    } catch (Exception e) {
      TestUtil.logErr("init failed", e);
    }
  }

  public boolean testXAResource1() {
    System.out.println("JTATest EJB");
    boolean b1 = false;
    Vector log = null;
    boolean results = true;

    try {
      // create EJB
      JTATest hr = null;
      try {
        ds1.clearLog();
        TSNamingContext ic = new TSNamingContext();
        TestUtil.logMsg("Got the EJB!!");
        TestUtil.logMsg("Got RA log.");
        ds1.setLogFlag(true);
        con = ds1.getConnection();
        ds1.setLogFlag(false);
        log = ds1.getLog();
        TestUtil.logTrace("Got connection.");
      } catch (Exception sqle) {
        TestUtil.printStackTrace(sqle);
        TestUtil.logMsg("Exception caught on creating connection:");
      }

      // Need to link these strings to assertion
      String toCheck1 = "TSManagedConnection.getXAResource";
      String toCheck2 = "XAResourceImpl.start";

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
      for (int i = 0; i < log.size(); i++) {
        String str = (String) log.elementAt(i);
        if (str.startsWith(toCheck1)) {
          b1 = true;
        }
      }

      if (b1) {
        results = true;
        TestUtil.logMsg("Methods called correctly");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (con != null) {
          con.close();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return results;
  }

  public boolean testXAResource2() {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean results = true;

    try {
      // Obtain connection, perform API verification
      TestUtil.logMsg("Performing callback verification...");
      try {
        ds1.clearLog();
        TestUtil.logMsg("Got RA log.");
        ds1.setLogFlag(true);
        con = ds1.getConnection();
        ds1.setLogFlag(false);
        log = ds1.getLog();
        TestUtil.logTrace("Got connection.");
      } catch (Exception sqle) {
        TestUtil.printStackTrace(sqle);
        TestUtil.logMsg("Exception caught on creating connection:");
      }

      // Need to link these strings to assertion
      String toCheck1 = "XAResourceImpl.commit";

      // Turn tracing on if you want to see the log contents
      TestUtil.logTrace(log.toString());

      // Verify connection object works by doing some end to end tests.
      TestUtil.logMsg("Performing end to end verification...");

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
      for (int i = 0; i < log.size(); i++) {
        String str = (String) log.elementAt(i);
        if (str.startsWith(toCheck1)) {
          b1 = true;
        }
      }

      if (b1) {
        results = true;
        TestUtil.logMsg("Methods called correctly");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      try {
        if (con != null) {
          con.close();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return results;
  }

  public void setSessionContext(SessionContext sc) {

    try {
      TestUtil.logTrace("setSessionContext");
      this.context = new TSNamingContext();
    } catch (Exception sqle) {
      TestUtil.printStackTrace(sqle);
      sqle.getMessage();
    }

  }

  public void ejbRemove() {
    TestUtil.logTrace("ejbRemove");
    try {
      if (con != null) {
        con.close();
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void ejbActivate() {
  }

  public void ejbPassivate() {
  }

}
