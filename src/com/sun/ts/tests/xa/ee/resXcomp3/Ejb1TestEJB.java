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
 * @(#)Ejb1TestEJB.java	1.3 03/05/16
 */

/*
 * @(#)Ejb1TestEJB.java	1.17 02/07/19
 */
package com.sun.ts.tests.xa.ee.resXcomp3;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.porting.*;

import javax.transaction.xa.XAException;
import jakarta.transaction.*;
import java.sql.*;
import javax.sql.*;
import jakarta.ejb.*;
import java.util.*;

import com.sun.ts.tests.common.connector.whitebox.TSEISDataSource;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;

public class Ejb1TestEJB implements SessionBean {
  // testProps represent the test specific properties passed in
  // from the test harness.
  private java.util.Properties p = null;

  private SessionContext sctx = null;

  private Properties testProps = null;

  private Ejb2Test ref = null;

  private Ejb2TestHome home = null;

  private TSNamingContext context = null;

  // DataSources
  // ds1 is associated with dbTable1; con1 will be used for the dbTable1
  // connection
  private DataSource ds1, ds4 = null;

  private String dbTable1 = null;

  private transient java.sql.Connection con1 = null;

  private transient java.sql.Connection con4 = null;

  // TSEIS
  private TSEISDataSource ds2, ds3;

  private transient TSConnection con2 = null;

  private transient TSConnection con3 = null;

  private transient Statement stmt = null;

  public Ejb1TestEJB() {
  }

  public void ejbCreate(java.util.Properties props) throws CreateException {
    this.testProps = props;
    String eMsg = "";
    try {
      TestUtil.init(props);
      context = new TSNamingContext();

      // get reference to ejb
      eMsg = "Exception doing a lookup for Ejb2Test ";
      home = (Ejb2TestHome) context.lookup("java:comp/env/ejb/Ejb2Test",
          Ejb2TestHome.class);
      ref = home.create(props);

      TestUtil.logMsg("Initialize logging data from server in Ejb1");
      eMsg = "Exception doing a initLogging for Ejb2Test ";
      ref.initLogging(p);

      eMsg = "Exception looking up JDBCwhitebox";
      ds1 = (DataSource) context.lookup("java:comp/env/eis/JDBCwhitebox-xa");

      eMsg = "Exception looking up EIS whitebox-xa";
      ds2 = (TSEISDataSource) context.lookup("java:comp/env/eis/whitebox-xa");

      eMsg = "Exception looking up EIS whitebox-notx";
      ds3 = (TSEISDataSource) context.lookup("java:comp/env/eis/whitebox-notx");

      eMsg = "Exception looking up EIS JDBCwhitebox-notx";
      ds4 = (DataSource) context.lookup("java:comp/env/eis/JDBCwhitebox-notx");

      TestUtil.logMsg("JDBCwhitebox-xa ds1 : " + ds1);
      TestUtil.logMsg("whitebox-xa ds2 : " + ds2);
      TestUtil.logMsg("whitebox-notx ds3 : " + ds3);
      TestUtil.logMsg("JDBCwhitebox-notx ds4 : " + ds4);

    } catch (Exception e) {
      TestUtil.logMsg(eMsg);
      TestUtil.logErr("Ejb1: ejbCreate failed", e);
      throw new CreateException(e.getMessage());
    }
  }

  public void setSessionContext(SessionContext sc) {
    try {
      TestUtil.logTrace("Ejb1 @setSessionContext");
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
    TestUtil.logMsg("dbConnect");
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

  public void insertDup(String tName) {
    String key, eMsg = null;
    TestUtil.logTrace("insertDup");
    try {
      if (tName.equals(dbTable1)) {
        TestUtil.logMsg("Getting a connection con4");
        eMsg = "Exception doing a getConnection from ds4";
        con4 = ds4.getConnection();
        TestUtil.logTrace("con4: " + con4.toString());

        // Prepare the new data entry in dbTable
        TestUtil.logMsg("Insert row in " + dbTable1);
        key = new String(testProps.getProperty("Xa_Tab1_insert_init"));
        stmt = con4.createStatement();
        stmt.executeUpdate(key);
        TestUtil.logTrace(
            "Inserted a row into the table using notx - key " + tName + key);
        TestUtil.logMsg("nsertDup key in " + dbTable1);
      } else { // EIS
        TestUtil.logMsg("Getting a connection con3");
        eMsg = "Exception doing a getConnection from ds3";
        con3 = ds3.getConnection();
        TestUtil.logTrace("con3: " + con3.toString());

        // Prepare the new data entry in EIS
        TestUtil.logMsg("Insert row in EIS");
        eMsg = "Exception doing an insert in EIS con3 ds3";
        key = new String(testProps.getProperty("TSEIS_insert_init"));
        con3.insert(key, key);
        TestUtil.logTrace("Inserted a row in EIS using notx - key " + key);
        TestUtil.logMsg("nsertDup key in EIS");
      }
    } catch (Exception e) {
      TestUtil.logMsg(eMsg);
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Captured Exception in insertDup");
      throw new EJBException(e.getMessage());
    } finally {
      try {
        eMsg = "Exception in closing stmt";
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        eMsg = "Exception in closing con3";
        if (con3 != null) {
          con3.close();
          con3 = null;
        }
        eMsg = "Exception in closing con4";
        if (con4 != null) {
          con4.close();
          con4 = null;
        }
      } catch (Exception e) {
        TestUtil.logMsg(eMsg);
        TestUtil.printStackTrace(e);
        TestUtil.logMsg("Captured Exception in insertDup");
        throw new EJBException(e.getMessage());
      }
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
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
      } catch (Exception e) {
        TestUtil.logErr(
            "Exception in insert@Ejb1 closing stmt" + e.getMessage(), e);
        throw new EJBException(e.getMessage());
      }
    }
  }

  public void destroyData(String tName) {
    String eMsg = null;
    String removeString = TestUtil.getProperty("Xa_Tab1_Delete");
    TestUtil.logMsg("destroyData : " + tName);
    try {
      if (tName.equals(dbTable1)) {
        eMsg = "Exception doing a getConnection from ds4";
        TestUtil.logMsg("Getting a connection con4");
        con4 = ds4.getConnection();
        TestUtil.logTrace("con4: " + con4.toString());
        eMsg = "Exception doing a dropTable on con4";
        stmt = con4.createStatement();
        stmt.executeUpdate(removeString);
        // stmt.close();
        TestUtil.logMsg("Deleted all rows from table " + dbTable1);
      } else { // EIS
        eMsg = "Exception doing a getConnection from ds3";
        TestUtil.logMsg("Getting a connection con3");
        con3 = ds3.getConnection();
        TestUtil.logTrace("con3: " + con3.toString());
        eMsg = "Exception doing a dropTable on con3";
        con3.dropTable();
        TestUtil.logMsg("Deleted all rows from EIS");
      }
    } catch (Exception e) {
      TestUtil.logMsg(eMsg);
      TestUtil.logErr("Exception occured trying to drop table", e);
      throw new EJBException(e.getMessage());
    } finally {
      try {
        eMsg = "Exception in closing stmt";
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        eMsg = "Exception in closing con3";
        if (con3 != null) {
          con3.close();
          con3 = null;
        }
        eMsg = "Exception in closing con4";
        if (con4 != null) {
          con4.close();
          con4 = null;
        }
      } catch (Exception e) {
        TestUtil.logMsg(eMsg);
        TestUtil.logErr("Exception occured trying to drop table", e);
        throw new EJBException(e.getMessage());
      }
    }
  }

  public void dbUnConnect(String tName) {
    TestUtil.logMsg("dbUnConnect");
    Vector queryResults = new Vector();
    // Close the DB connections
    try {
      if (tName.equals(dbTable1)) {
        con1.close();
        con1 = null;
        TestUtil.logMsg("Closed " + dbTable1 + " connection");
      } else {
        con2.close();
        con2 = null;
        TestUtil.logMsg("Closed EIS connection");
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
              TestUtil.logMsg("verifyData OK, Status is : " + status);
            } else {
              status = false;
              TestUtil.logMsg("verifyData ERROR, Status is : " + status);
              break;
            }
          } // while
          if (expResults.length != linenum) {
            TestUtil.logTrace(
                "Error - expected row count does not match table in verifyData!!");
            status = false;
          }
        } else { // EIS
          TestUtil.logMsg("Getting a connection con3");
          con3 = ds3.getConnection();
          TestUtil.logMsg("con3: " + con3.toString());
          queryResults = con3.readData();
          for (int i = 0; i < expResults.length; i++) {
            TestUtil.logMsg("Expected results: " + expResults.length);
            if (queryResults
                .contains((new Integer(expResults[i])).toString())) {
              status = true;
              TestUtil.logMsg("VerifyData OK, Status is : " + status);
            } else {
              status = false;
              TestUtil.logMsg("VerifyData Error, Status is : " + status);
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
          TestUtil.logMsg("Getting a connection con3");
          con3 = ds3.getConnection();
          TestUtil.logMsg("con3: " + con3.toString());
          queryResults = con3.readData();
          for (int i = 0; i < expResults.length; i++) {
            TestUtil.logMsg("Not Expected results: " + expResults[i]);
            if (queryResults
                .contains((new Integer(expResults[i])).toString())) {
              status = false;
              TestUtil.logMsg("VerifyData Error, Status is : " + status);
              break;
            } else {
              status = true;
              TestUtil.logMsg("VerifyData OK, Status is : " + status);
            }
          }
          for (int i = 0; i < queryResults.size(); i++) {
            TestUtil.logMsg(
                "Query REsults contains : " + queryResults.elementAt(i));
          }
        }
      }
    } catch (Exception e) {
      TestUtil.logTrace("Error verifyData database inserts Ejb1 ");
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
        if (con3 != null) {
          con3.close();
          con3 = null;
        }
      } catch (Exception ee) {
      }
    }
    TestUtil.logMsg("Verify Data Status is : " + status);
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
      TestUtil.logErr("Exception connecting to " + dbTable1 + " DB con1 ", ee);
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
      TestUtil.logErr("Exception connecting to EIS con2 ", ee);
      throw new EJBException(ee.getMessage());
    }
  }

  /*
   * private void dropTable1() { TestUtil.logTrace("dropTable1"); // Delete the
   * data in dbTable1 table String removeString =
   * TestUtil.getProperty("Xa_Tab1_Delete"); try{ stmt = con1.createStatement();
   * stmt.executeUpdate(removeString); stmt.close(); } catch (SQLException e) {
   * throw new EJBException( e.getMessage() ); } }
   * 
   * private void dropTable2() { TestUtil.logTrace("dropTable2"); // Delete the
   * data from EIS table try{ con2.dropTable(); } catch (Exception e) { throw
   * new EJBException( e.getMessage() ); } }
   */

}
