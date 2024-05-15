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
 * @(#)BeanAEJB.java	1.16 03/05/16
 */

package com.sun.ts.tests.connector.localTx.transaction.conSharing3;

import java.util.Properties;
import java.util.Vector;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;
import com.sun.ts.tests.common.connector.whitebox.TSDataSource;

import jakarta.ejb.EJBException;

public class BeanAEJB {

  // testProps represent the test specific properties passed in
  // from the test harness.
  private static final Properties testProps = null;

  private TSNamingContext context = null;

  // con will be used for the table1 connection
  private TSConnection con;

  // TSDataSources
  private TSDataSource ds;

  // The TxBean variables
  private static final String txBeanBRequired = "java:comp/env/ejb/TxBeanBRequired";

  private BeanB beanBRef = null;

  public void initialize() {
    TestUtil.logTrace("initialize");
    try {
      this.context = new TSNamingContext();

      // Get the TSDataSource
      ds = (TSDataSource) context.lookup("java:comp/env/eis/whitebox-tx");
      TestUtil.logTrace("ds: " + ds);
      TestUtil.logTrace("TSDataSource lookup OK!");

      TestUtil.logMsg("Looking up BeanB " + txBeanBRequired);
      beanBRef = (BeanB) context.lookup(txBeanBRequired, BeanB.class);
      beanBRef.initialize();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception setting EJB/DataSource", e);
      throw new EJBException(e.getMessage());
    }
  }

  // ===========================================================
  // The BeanA interface implementation

  // Database methods
  public void dbConnectfirst() {
    TestUtil.logTrace("dbConnect");
    try {
      conTable1first();
      createData();
      dbUnConnect();
    } catch (Exception se) {
      TestUtil.printStackTrace(se);
      throw new EJBException(se.getMessage());
    }
  }

  public void dbConnectsecond() {
    TestUtil.logTrace("dbConnect");
    try {
      conTable1second();
    } catch (Exception se) {
      TestUtil.printStackTrace(se);
      throw new EJBException(se.getMessage());
    }
  }

  public void dbUnConnect() {
    TestUtil.logTrace("dbUnConnect");
    try {
      if (con != null) {
        con.close();
        con = null;
      }
    } catch (Exception se) {
      TestUtil.logErr("Exception closing db connection ", se);
      throw new EJBException(se.getMessage());
    }
  }

  public void createData() {
    TestUtil.logTrace("createData");

    try {
      createTable1();
      TestUtil.logMsg("Created ");
    } catch (Exception e) {
      TestUtil.logErr("Exception creating table ", e);
      throw new EJBException(e.getMessage());
    }
  }

  public boolean insert(String str) {
    try {
      con.insert("1", "Hello");
      return true;

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public void delete(String str) {
    try {
      con.delete(str);
    } catch (Exception se) {
      TestUtil.logErr("Exception closing db connection ", se);
      throw new EJBException(se.getMessage());
    }
  }

  public void destroyData() {
    TestUtil.logTrace("destroyData");
    try {
      dropTable1();
    } catch (Exception e) {
      TestUtil.logErr("Exception occured trying to drop table", e);
      throw new EJBException(e.getMessage());
    }
  }

  // Test Results methods
  public Vector getResults() {
    TestUtil.logTrace("getResults");

    Vector queryResults = new Vector();

    try {
      queryResults = con.readData();
      return queryResults;
    } catch (Exception e) {
      TestUtil.logErr("Exception results", e);
      throw new EJBException(e.getMessage());
    }
  }

  // private methods
  private void conTable1first() {
    TestUtil.logTrace("conTable1");
    try {
      con = ds.getConnection();
    } catch (Exception e) {
      TestUtil.logErr("Exception connecting to DB", e);
      throw new EJBException(e.getMessage());
    }

    TestUtil.logTrace("dbConnect through Bean B");
    try {
      conUnconBeanB();
      TestUtil.logTrace("sucessfully created and destroyed another connection");
    } catch (Exception se) {
      TestUtil.logErr("Exception opening tseis connection through BeanB ", se);
      throw new EJBException(se.getMessage());
    }

  }

  private void conTable1second() {
    TestUtil.logTrace("conTable1");
    try {
      con = ds.getConnection();
    } catch (Exception e) {
      TestUtil.logErr("Exception connecting to DB", e);
      throw new EJBException(e.getMessage());
    }
  }

  private void createTable1() {
    TestUtil.logTrace("createTable1");

    try {
      dropTable1();
      TestUtil.logTrace("Deleted all rows ");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Exception encountered while deleting " + e.getMessage());
    }

    try {
      con.insert("2", "Hello");
    } catch (Exception e) {
      TestUtil.logErr("Exception creating ", e);
      throw new EJBException(e.getMessage());
    }
  }

  private void dropTable1() {
    TestUtil.logTrace("dropTable1");

    try {
      con.dropTable();
    } catch (Exception e) {
      TestUtil.logErr("Exception: cleaning table", e);
      throw new EJBException(e.getMessage());
    }
  }

  // **************** Begin Calling Business methods of BeanB
  // ******************//
  private void conUnconBeanB() {
    TestUtil.logTrace("connection to Database through BeanB");
    try {
      TestUtil.logTrace("Logging data from server");

      beanBRef.dbConnect();
      beanBRef.createData();
      beanBRef.dbUnConnect();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
  }
}
// **************** End of Calling Business methods of BeanB *****************//
// ===========================================================
