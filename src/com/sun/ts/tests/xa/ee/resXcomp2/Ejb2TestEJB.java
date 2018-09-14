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
 * @(#)Ejb2TestEJB.java	1.3 03/05/16
 */

/*
 * @(#)Ejb2TestEJB.java	1.16 02/07/19
 */
package com.sun.ts.tests.xa.ee.resXcomp2;

//import javax.jms.*;
//import java.rmi.*; 
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.porting.*;

import java.sql.*;
import javax.sql.*;
import javax.ejb.*;
import java.util.*;

import com.sun.ts.tests.common.connector.whitebox.TSEISDataSource;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;

public class Ejb2TestEJB implements SessionBean {
  // testProps represent the test specific properties passed in
  // from the test harness.
  private SessionContext sctx = null;

  private TSNamingContext context = null;

  private Properties testProps = null;

  // DataSources
  // ds1 is associated with dbTable1; con1 will be used for the dbTable1
  // connection
  private DataSource ds1 = null;

  private String dbTable1 = null;

  private transient java.sql.Connection con1 = null;

  private transient Statement stmt;

  // TSEIS
  private TSEISDataSource ds2;

  private transient TSConnection con2 = null;

  public Ejb2TestEJB() {
  }

  public void ejbCreate(Properties props) throws CreateException {
    this.testProps = props;
    String eMsg = "";
    try {
      TestUtil.init(props);
      context = new TSNamingContext();
      TestUtil.logTrace("got the context");

      // get a connection object
      eMsg = "Exception looking up JDBCwhitebox";
      ds1 = (DataSource) context.lookup("java:comp/env/eis/JDBCwhitebox-tx");

      eMsg = "Exception looking up EIS whitebox";
      ds2 = (TSEISDataSource) context.lookup("java:comp/env/eis/whitebox-tx");

      TestUtil.logMsg("ds1 : " + ds1);
      TestUtil.logMsg("ds2 : " + ds2);

    } catch (Exception e) {
      TestUtil.logTrace(eMsg);
      TestUtil.logErr("EJB2: ejbCreate failed", e);
      throw new CreateException(e.getMessage());
    }
  }

  public void setSessionContext(SessionContext sc) {
    TestUtil.logTrace("EJB2 @setSessionContext");
    try {
      this.sctx = sc;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception setting EJB context/DataSources",
          e);
      throw new EJBException(e.getMessage());
    }
  }

  public void ejbRemove() {
    TestUtil.logTrace("@ejbRemove");
  }

  public void ejbActivate() {
    TestUtil.logTrace("@ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("@ejbPassivate");
  }

  // ===========================================================
  public void dbConnect(String tName) {
    TestUtil.logTrace("dbConnect");
    try {
      if (tName.equals(dbTable1)) {
        // Make the dbTable1 connection
        conTable1();
        TestUtil.logMsg("Made the JDBC connection to " + dbTable1 + " DB");
      } else {
        conTable2();
        TestUtil.logMsg("Made the connection to EIS");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception on JDBC connection", e);
      throw new EJBException(e.getMessage());
    }
  }

  public void insert(String tName) {
    String key1, key2 = null;
    TestUtil.logMsg("Insert @Ejb2 ");
    // Insert a row into the specified table
    try {
      if (tName.equals(dbTable1)) {
        // Prepare the new dbTable1 row entry
        TestUtil.logMsg("Insert row in " + dbTable1);
        key1 = new String(testProps.getProperty("Xa_Tab1_insert1"));
        key2 = new String(testProps.getProperty("Xa_Tab1_insert2"));
        stmt = con1.createStatement();
        stmt.executeUpdate(key1);
        stmt.executeUpdate(key2);
        TestUtil.logTrace("Inserted two row in table " + tName);
      } else {
        // Prepare the new data entry in EIS
        TestUtil.logMsg("Insert row in EIS");
        key1 = new String(testProps.getProperty("TSEIS_insert1"));
        key2 = new String(testProps.getProperty("TSEIS_insert2"));
        con2.insert(key1, key1);
        con2.insert(key2, key2);
        TestUtil.logTrace("Inserted two elements in EIS ");
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception inserting a row into table " + tName + ";\n"
          + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    }
  }

  public void dbUnConnect(String tName) {
    TestUtil.logTrace("dbUnConnect");
    // Close the DB connections
    try {
      if (tName.equals(dbTable1)) {
        con1.close();
        con1 = null;
        TestUtil.logTrace("Closed " + dbTable1 + " connection");
      } else {
        con2.close();
        con2 = null;
        TestUtil.logTrace("Closed EIS connection");
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occured trying to close the DB connection", e);
      throw new EJBException(e.getMessage());
    }
  }

  public void initLogging(Properties p) {
    TestUtil.logTrace("initLogging Ejb2");
    // this.testProps = p;
    try {
      // TestUtil.init(p);
      // Get the dbTable1 DataSource
      dbTable1 = TestUtil.getTableName(TestUtil.getProperty("Xa_Tab1_Delete"));
      TestUtil.logMsg(dbTable1 + " Ejb2 initLogging OK!");

    } // catch(RemoteLoggingInitException e) {
    catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Inside Ejb2 initLogging" + e.getMessage());
    }
  }

  // Exception methods
  public void throwEJBException() throws EJBException {
    TestUtil.logTrace("throwEJBException");
    throw new EJBException("EJBException from Ejb1TestEJB");
  }

  // private methods
  private void conTable1() {
    TestUtil.logTrace("conTable1");
    try {
      // Get connection info for dbTable1 DB
      con1 = ds1.getConnection();
      TestUtil.logTrace("con1: " + con1.toString());
    } catch (SQLException e) {
      TestUtil.logErr("SQLException connecting to " + dbTable1 + " DB", e);
      throw new EJBException(e.getMessage());
    } catch (Exception ee) {
      TestUtil.logErr("Exception connecting to " + dbTable1 + " DB", ee);
      throw new EJBException(ee.getMessage());
    }
  }

  private void conTable2() {
    TestUtil.logTrace("conTable2");
    try {
      // Get connection info for dbTable1 DB
      con2 = ds2.getConnection();
      TestUtil.logTrace("con2: " + con2.toString());
    } catch (Exception ee) {
      TestUtil.logErr("Exception connecting to EIS ", ee);
      throw new EJBException(ee.getMessage());
    }
  }

}
