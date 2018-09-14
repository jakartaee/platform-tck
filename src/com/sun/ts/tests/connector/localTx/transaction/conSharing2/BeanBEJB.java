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
 * @(#)BeanBEJB.java	1.16 03/05/16
 */

package com.sun.ts.tests.connector.localTx.transaction.conSharing2;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import java.rmi.*;
import javax.ejb.*;
import javax.transaction.*;
import com.sun.ts.tests.common.connector.whitebox.TSDataSource;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;

public class BeanBEJB implements SessionBean {

  // testProps represent the test specific properties passed in
  // from the test harness.
  private static final Properties testProps = null;

  private SessionContext sctx = null;

  private TSNamingContext context = null;

  // con will be used for the table1 connection
  private TSConnection con;

  // TSDataSources
  private TSDataSource ds;

  // Required EJB methods
  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("ejbCreate");
  }

  public void setSessionContext(SessionContext sc) {
    TestUtil.logTrace("setSessionContext");
    try {
      this.sctx = sc;
      this.context = new TSNamingContext();

      // Get the TSDataSource
      ds = (TSDataSource) context.lookup("java:comp/env/eis/whitebox-tx");
      TestUtil.logTrace("ds: " + ds);
      TestUtil.logTrace("TSDataSource lookup OK!");

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception setting EJB context/DataSources",
          e);
      throw new EJBException(e.getMessage());
    }
  }

  public void ejbRemove() {
    TestUtil.logTrace("ejbRemove");
  }

  public void ejbDestroy() {
    TestUtil.logTrace("ejbDestroy");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
    try {
      con = ds.getConnection();
    } catch (Exception e) {
      TestUtil.logErr("Exception connecting to DB", e);
      throw new EJBException(e.getMessage());
    }

  }

  public void ejbPassivate() {
    dbUnConnect();
    TestUtil.logTrace("ejbPassivate");
  }

  // ===========================================================
  // The BeanB interface implementation

  // Database methods
  public void dbConnect() {
    TestUtil.logTrace("dbConnect");
    try {
      conTable2();
      TestUtil.logTrace("Made connection to DB");
    } catch (Exception se) {
      TestUtil.logErr("Exception opening db connection ", se);
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
      createTable2();
    } catch (Exception e) {
      TestUtil.logErr("Exception creating table ", e);
      throw new EJBException(e.getMessage());
    }
  }

  public boolean insert(String str) {
    try {
      con.insert("3", str);
      return true;

    } catch (Exception e) {
      TestUtil.logErr("Exception inserting a row into table ", e);
      return false;
    }
  }

  public void delete(String str) {
    TestUtil.logTrace("delete");
    try {
      con.delete(str);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

  public void destroyData() {
    TestUtil.logTrace("destroyData");
    try {
      dropTable2();
    } catch (Exception e) {
      TestUtil.logErr("Exception occured trying to drop table", e);
      throw new EJBException(e.getMessage());
    }
  }

  // Test Results methods
  public Vector getResults() {
    TestUtil.logTrace("getResults");
    try {
      Vector results = con.readData();
      return results;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

  // private methods
  private void conTable2() {
    TestUtil.logTrace("conTable2");
    try {
      con = ds.getConnection();
    } catch (Exception e) {
      TestUtil.logErr("Exception connecting DB", e);
      throw new EJBException(e.getMessage());
    }
  }

  private void createTable2() {
    try {
      dropTable2();
      TestUtil.logTrace("Deleted all rows from table ");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg(
          "Exception encountered while deleting rows " + e.getMessage());
    }
  }

  private void dropTable2() {
    TestUtil.logTrace("dropTable2");
    try {
      con.dropTable();
    } catch (Exception e) {
      TestUtil.logErr("Exception dropping ", e);
      throw new EJBException(e.getMessage());
    }
  }

  // ===========================================================
}
