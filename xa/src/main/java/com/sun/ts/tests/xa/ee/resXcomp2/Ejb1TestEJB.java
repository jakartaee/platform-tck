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
 * @(#)Ejb1TestEJB.java	1.3 03/05/16
 */

/*
 * @(#)Ejb1TestEJB.java	1.17 02/07/19
 */
package com.sun.ts.tests.xa.ee.resXcomp2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;

import javax.sql.DataSource;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;
import com.sun.ts.tests.common.connector.whitebox.TSEISDataSource;

import jakarta.ejb.EJBException;

public class Ejb1TestEJB {
  // testProps represent the test specific properties passed in
  // from the test harness.
  private Properties p = null;

  private Properties testProps = null;

  private Ejb2Test ref = null;

  private TSNamingContext context = null;

  // DataSources
  // ds1 is associated with dbTable1; con1 will be used for the dbTable1
  // connection
  private DataSource ds1 = null;

  private String dbTable1 = null;

  private transient Connection con1 = null;

  private transient Statement stmt;

  // TSEIS
  private TSEISDataSource ds2;

  private transient TSConnection con2 = null;

  public Ejb1TestEJB() {
  }

  public void initialize(Properties props) {
    this.testProps = props;
    String eMsg = "";
    try {
      TestUtil.init(props);
      context = new TSNamingContext();

      // get reference to ejb
      eMsg = "Exception doing a lookup for Ejb2Test ";
      ref = (Ejb2Test) context.lookup("java:comp/env/ejb/Ejb2Test", Ejb2Test.class);
      ref.initialize(props);

      TestUtil.logMsg("Initialize logging data from server in Ejb1");
      eMsg = "Exception doing a initLogging for Ejb2Test ";
      ref.initLogging(p);

      eMsg = "Exception looking up JDBCwhitebox";
      ds1 = (DataSource) context.lookup("java:comp/env/eis/JDBCwhitebox-tx");

      eMsg = "Exception looking up EIS whitebox";
      ds2 = (TSEISDataSource) context.lookup("java:comp/env/eis/whitebox-tx");

      TestUtil.logMsg("ds1 : " + ds1);
      TestUtil.logMsg("ds2 : " + ds2);

    } catch (Exception e) {
      TestUtil.logTrace(eMsg);
      TestUtil.logErr("Ejb1: ejbCreate failed", e);
      throw new EJBException(e.getMessage());
    }
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
    String key = null;
    TestUtil.logTrace("insert");
    // Insert a row into the specified table
    try {
      if (tName.equals(dbTable1)) {
        // Prepare the new dbTable1 row entry
        TestUtil.logMsg("Insert row in " + dbTable1);
        key = new String(testProps.getProperty("Xa_Tab1_insert_init"));
        stmt = con1.createStatement();
        stmt.executeUpdate(key);
        TestUtil.logTrace("Inserted a row into the table " + tName);
        TestUtil.logMsg("Calling insert in Ejb2 ");
        ref.dbConnect(tName);
        ref.insert(tName);
        ref.dbUnConnect(tName);
      } else {
        // Prepare the new data entry in EIS
        TestUtil.logMsg("Insert row in EIS");
        key = new String(testProps.getProperty("TSEIS_insert_init"));
        con2.insert(key, key);
        TestUtil.logTrace("Inserted a row into the EIS ");
        TestUtil.logMsg("Calling insert in Ejb2 ");
        ref.dbConnect("EIS");
        ref.insert("EIS");
        ref.dbUnConnect("EIS");
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception inserting a row into table " + tName + ";\n"
          + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    }
  }

  public void destroyData(String tName) {
    TestUtil.logTrace("destroyData");
    try {
      if (tName.equals(dbTable1)) {
        dropTable1();
        TestUtil.logMsg("Deleted all rows from table " + dbTable1);
      } else {
        dropTable2();
        TestUtil.logMsg("Deleted all rows from EIS ");
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occured trying to drop table", e);
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
    TestUtil.logTrace("initLogging Ejb1");
    this.testProps = p;
    try {
      TestUtil.init(p);
      // Get the dbTable1 DataSource
      dbTable1 = TestUtil.getTableName(TestUtil.getProperty("Xa_Tab1_Delete"));
      TestUtil.logMsg(dbTable1 + " Ejb1 initLogging OK!");

    } // catch(RemoteLoggingInitException e) {
    catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Inside Ejb1 initLogging" + e.getMessage());
    }
  }

  public boolean verifyData(String operation, String tName, int[] expResults) {
    boolean status = false;
    int linenum = 0;
    PreparedStatement queryStatement = null;
    ResultSet theResults = null;
    Vector queryResults = new Vector();
    try {
      if (operation.equals("commit")) { // operation is commit
        TestUtil.logMsg("Expected number of rows is: " + expResults.length);
        if (tName.equals(dbTable1)) {
          String query = testProps.getProperty("Xa_Tab1_query");
          TestUtil.logMsg("query is " + query);
          queryStatement = con1.prepareStatement(query);
          theResults = queryStatement.executeQuery();
          ResultSetMetaData rsmeta = theResults.getMetaData();
          int numColumns = rsmeta.getColumnCount();
          TestUtil.logTrace("Number of columns from rsmeta " + numColumns);
          while (theResults.next()) {
            linenum++;
            TestUtil.logMsg(
                "Line No: " + linenum + " results: " + theResults.getInt(1)
                    + " expResults: " + expResults[linenum - 1]);
            if (theResults.getInt(1) == expResults[linenum - 1]) {
              status = true;
              TestUtil.logTrace("verifyData OK, Status is : " + status);
            } else {
              status = false;
              TestUtil.logTrace("verifyData ERROR, Status is : " + status);
              break;
            }
          } // while
          if (expResults.length != linenum) {
            TestUtil.logTrace(
                "Error - expected row count does not match table in verifyData!!");
            status = false;
          }
        } else { // EIS
          queryResults = con2.readData();
          TestUtil.logMsg(
              "Expected number of rows in result: " + expResults.length);
          for (int i = 0; i < expResults.length; i++) {
            if (queryResults
                .contains((new Integer(expResults[i])).toString())) {
              status = true;
              TestUtil.logTrace("VerifyData OK, Status is : " + status);
            } else {
              status = false;
              TestUtil.logTrace("VerifyData Error, Status is : " + status);
              break;
            }
          }
          for (int i = 0; i < queryResults.size(); i++) {
            TestUtil.logMsg(
                "Query Results contains : " + queryResults.elementAt(i));
          }
        }

      } else { // operation is rollback
        if (tName.equals(dbTable1)) {
          String query = testProps.getProperty("Xa_Tab1_query");
          TestUtil.logMsg("query is " + query);
          queryStatement = con1.prepareStatement(query);
          theResults = queryStatement.executeQuery();
          TestUtil.logTrace("status is " + status);
          status = !theResults.next();
          TestUtil.logMsg("Resultset has no data? " + status);

        } else { // EIS

          queryResults = con2.readData();
          for (int i = 0; i < expResults.length; i++) {
            TestUtil.logMsg("Not Expected results: " + expResults[i]);
            if (queryResults
                .contains((new Integer(expResults[i])).toString())) {
              status = false;
              TestUtil.logTrace("VerifyData Error, Status is : " + status);
              break;
            } else {
              status = true;
              TestUtil.logTrace("VerifyData OK, Status is : " + status);
            }
          }
          for (int i = 0; i < queryResults.size(); i++) {
            TestUtil.logMsg(
                "Query Results contains : " + queryResults.elementAt(i));
          }
        }

      }
    } catch (Exception e) {
      TestUtil.logMsg("Error verifyData database inserts Ejb1 ");
      TestUtil.printStackTrace(e);
      status = false;
      throw new EJBException(e.getMessage());
    } finally {
      try {
        if (theResults != null) {
          theResults.close();
        }
        if (queryStatement != null) {
          queryStatement.close();
        }
      } catch (Exception ee) {
      }
    }
    TestUtil.logTrace("Verify Data Status is : " + status);
    return status;
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

  private void dropTable1() {
    TestUtil.logTrace("dropTable1");
    // Delete the data in dbTable1 table
    String removeString = TestUtil.getProperty("Xa_Tab1_Delete");
    try {
      stmt = con1.createStatement();
      stmt.executeUpdate(removeString);
      stmt.close();
    } catch (SQLException e) {
      // TestUtil.logErr("SQLException dropping "+dbTable1+" table", e);
      throw new EJBException(e.getMessage());
    }
  }

  private void dropTable2() {
    TestUtil.logTrace("dropTable2");
    // Delete the data from EIS table
    try {
      con2.dropTable();
    } catch (Exception e) {
      // TestUtil.logErr("SQLException dropping "+dbTable1+" table", e);
      throw new EJBException(e.getMessage());
    }
  }

}
