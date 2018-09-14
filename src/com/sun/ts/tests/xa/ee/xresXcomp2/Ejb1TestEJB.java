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
 * @(#)Ejb1TestEJB.java	1.2 03/05/16
 */

/*
 * @(#)Ejb1TestEJB.java	1.17 02/07/19
 */
package com.sun.ts.tests.xa.ee.xresXcomp2;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.porting.*;

import javax.transaction.xa.XAException;
import javax.transaction.*;
import java.sql.*;
import javax.sql.*;
import javax.ejb.*;
import java.util.*;

import com.sun.ts.tests.common.connector.whitebox.TSEISDataSource;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;

public class Ejb1TestEJB implements SessionBean {
  // testProps represent the test specific properties passed in
  // from the test harness.
  private java.util.Properties p = null;

  private SessionContext sctx = null;

  private Properties testProps = null;

  // TableSizes
  // dbSize1 is the size of dbTable1
  private Integer dbSize1;

  private String dbTable1 = null;

  private Ejb2Test ref = null;

  private Ejb2TestHome home = null;

  private TSNamingContext context = null;

  private transient PreparedStatement pStmt;

  // DataSources
  // ds1 is associated with dbTable1; con1 will be used for the dbTable1
  // connection
  private DataSource ds1, ds4 = null;

  private transient java.sql.Connection con1 = null;

  private transient java.sql.Connection con4 = null;

  // TSEIS
  private TSEISDataSource ds2, ds3, ds5;

  private transient TSConnection con2 = null;

  private transient TSConnection con3 = null;

  private transient TSConnection con5 = null;

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

      eMsg = "Exception looking up size";
      dbSize1 = (Integer) context.lookup("java:comp/env/size");

      eMsg = "Exception looking up JDBCwhitebox";
      ds1 = (DataSource) context.lookup("java:comp/env/eis/JDBCwhitebox-xa");

      eMsg = "Exception looking up EIS whitebox-xa";
      ds2 = (TSEISDataSource) context.lookup("java:comp/env/eis/whitebox-xa");

      eMsg = "Exception looking up EIS whitebox-tx";
      ds3 = (TSEISDataSource) context.lookup("java:comp/env/eis/whitebox-tx");

      eMsg = "Exception looking up JDBCwhitebox-tx";
      ds4 = (DataSource) context.lookup("java:comp/env/eis/JDBCwhitebox-tx");

      eMsg = "Exception looking up whitebox-notx";
      ds5 = (TSEISDataSource) context.lookup("java:comp/env/eis/whitebox-notx");

      TestUtil.logMsg("JDBCwhitebox-xa ds1 : " + ds1);
      TestUtil.logMsg("whitebox-xa ds2 : " + ds2);
      TestUtil.logMsg("whitebox-tx ds3 : " + ds3);
      TestUtil.logMsg("JDBCwhitebox-tx ds4 : " + ds4);
      TestUtil.logMsg("whitebox-notx ds5 : " + ds5);

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
      TestUtil.logErr("Unexpected exception on connection", e);
      throw new EJBException(e.getMessage());
    }
  }

  public void txDbConnect(String tName) {
    TestUtil.logMsg("txDbConnect");
    try {
      if (tName.equals(dbTable1)) {
        // Make the dbTable1 connection
        conTable4();
        TestUtil.logMsg("Made the JDBC TX connection to " + dbTable1 + " DB");
      } else {
        conTable3();
        TestUtil.logMsg("Made the EIS TX connection to EIS");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception on connection", e);
      throw new EJBException(e.getMessage());
    }
  }

  public void createData(String tName) {
    TestUtil.logTrace("createData");
    try {
      if (tName.equals(dbTable1)) {
        // Create the dbTable1 table
        createTable1();
        TestUtil.logMsg("Created the table " + dbTable1 + " ");
      } else {
        createTable2();
        TestUtil.logMsg("Created the EIS data");
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception creating table", e);
      throw new EJBException(e.getMessage());
    }
  }

  public void insertDup(String tName, String tSize) {
    // public void insertDup(String tName){
    String eMsg = null;
    TestUtil.logTrace("insertDup");
    String key = tSize;
    try {
      if (tName.equals(dbTable1)) {
        // JDBC
      } else { // EIS
        TestUtil.logMsg("Getting a connection con5");
        eMsg = "Exception doing a getConnection from ds5";
        con5 = ds5.getConnection();
        TestUtil.logTrace("con5: " + con5.toString());

        // Prepare the new data entry in EIS
        TestUtil.logMsg("Insert row in EIS");
        eMsg = "Exception doing an insert in EIS con5 ds5";
        con5.insert(key, key);
        // for duplicate
        TestUtil.logTrace("Inserted a row in EIS using notx - key ");
        TestUtil.logMsg("nsertDup key in EIS");
      }
    } catch (Exception e) {
      TestUtil.logMsg(eMsg);
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Captured Exception in insertDup");
      throw new EJBException(e.getMessage());
    } finally {
      try {
        // eMsg = "Exception in closing stmt";
        // if (stmt!=null){
        // stmt.close();
        // stmt = null;
        // }
        eMsg = "Exception in closing con5";
        if (con5 != null) {
          con5.close();
          con5 = null;
        }
        // eMsg = "Exception in closing con4";
        // if (con4 != null){
        // con4.close();
        // con4 = null;
        // }
      } catch (Exception e) {
        TestUtil.logMsg(eMsg);
        TestUtil.printStackTrace(e);
        TestUtil.logMsg("Captured Exception in insertDup");
        throw new EJBException(e.getMessage());
      }
    }
  }

  public boolean insert(String tName, int key) {
    TestUtil.logTrace("insert");
    // int newKey = key;
    String newName = null;

    try {
      if (tName.equals(dbTable1)) {
        // Prepare the new dbTable1 row entry
        newName = dbTable1 + "-" + key;
        String updateString = TestUtil.getProperty("Xa_Tab1_Insert");
        pStmt = con1.prepareStatement(updateString);
        // Perform the insert(s)
        pStmt.setInt(1, key);
        pStmt.setString(2, newName);
        pStmt.setString(3, newName);
        TestUtil
            .logMsg("Insert a row into the table " + tName + " valus : " + key);
        pStmt.executeUpdate();
        pStmt.close();
        TestUtil.logTrace("Inserting a row into the EIS ");
        ref.dbConnect("EIS");
        ref.insert("EIS", key);
        ref.dbUnConnect("EIS");

      } else {
        // Prepare the new data entry in EIS
        con2.insert((new Integer(key)).toString(),
            (new Integer(key)).toString());
        TestUtil.logMsg("Inserted a row into the EIS ");
        TestUtil.logMsg("Inserting a row into the table " + dbTable1);
        ref.dbConnect(dbTable1);
        ref.insert(dbTable1, key);
        ref.dbUnConnect(dbTable1);
      }
      return true;

    } catch (Exception e) {
      TestUtil.logErr("Exception inserting a row into table " + tName + ";\n"
          + e.getMessage(), e);
      return false;
    }
  }

  public void delete(String tName, int fromKey, int toKey) {
    TestUtil.logTrace("delete");
    try {
      if (tName.equals(dbTable1)) {
        // Delete row(s) from the specified table
        String updateString = TestUtil.getProperty("Xa_Tab1_Delete1");
        pStmt = con1.prepareStatement(updateString);
        for (int i = fromKey; i <= toKey; i++) {
          pStmt.setInt(1, i);
          pStmt.executeUpdate();
        }
        pStmt.close();
        TestUtil.logMsg("Deleted row(s) " + fromKey + " thru " + toKey
            + " from the table " + tName);
      } else {
        // Delete rows from EIS
        for (int i = fromKey; i <= toKey; i++) {
          con2.delete((new Integer(i)).toString());
        }
        TestUtil
            .logMsg("Deleted row(s) from EIS " + fromKey + " thru " + toKey);
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception deleting row(s) " + fromKey + " thru " + toKey
          + " from the table " + tName, e);
      throw new EJBException(e.getMessage());
    }
  }

  public Vector getResults(String tName) {
    TestUtil.logTrace("getResults");
    ResultSet rs = null;
    Vector queryResults = new Vector();
    int i;
    String query, s, name = null;
    float f;

    try {
      if (tName.equals(dbTable1)) {
        query = TestUtil.getProperty("Xa_Tab1_query");
        stmt = con4.createStatement();
        rs = stmt.executeQuery(query);
        // name = "COF_NAME";
        if (rs != null) {
          while (rs.next()) {
            i = rs.getInt(1);
            // s = rs.getString(name);
            // f = rs.getFloat("PRICE");
            queryResults.addElement(new Integer(i));
            // queryResults.addElement(s);
            // queryResults.addElement( new Float(f) );
          }
        }
        stmt.close();
        TestUtil.logMsg("Obtained " + tName + " table ResultSet");
      } else {
        queryResults = con3.readData();
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception obtaining " + tName + " table ResultSet", e);
      throw new EJBException(e.getMessage());
    }
    return queryResults;
  }

  public void destroyData(String tName) {
    String eMsg = null;
    String removeString = TestUtil.getProperty("Xa_Tab1_Delete");
    TestUtil.logMsg("destroyData : " + tName);
    try {
      if (tName.equals(dbTable1)) {
        // eMsg = "Exception doing a getConnection from ds4";
        // TestUtil.logMsg("Getting a connection con4");
        // con4 = ds4.getConnection();
        // TestUtil.logTrace("con4: " + con4.toString() );
        eMsg = "Exception doing a dropTable on con4";
        stmt = con4.createStatement();
        stmt.executeUpdate(removeString);
        // stmt.close();
        TestUtil.logMsg("Deleted all rows from table " + dbTable1);
      } else { // EIS
        // eMsg = "Exception doing a getConnection from ds3";
        // TestUtil.logMsg("Getting a connection con3");
        // con3 = ds3.getConnection();
        // TestUtil.logTrace("con3: " + con3.toString() );
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
        // eMsg = "Exception in closing con3";
        // if (con3 != null){
        // con3.close();
        // con3 = null;
        // }
        // eMsg = "Exception in closing con4";
        // if (con4 != null){
        // con4.close();
        // con4 = null;
        // }
      } catch (Exception e) {
        TestUtil.logMsg(eMsg);
        TestUtil.logErr("Exception occured trying to drop table", e);
        throw new EJBException(e.getMessage());
      }
    }
  }

  public void dbUnConnect(String tName) {
    TestUtil.logMsg("dbUnConnect");
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

  public void txDbUnConnect(String tName) {
    TestUtil.logMsg("txDbUnConnect");
    // Close the DB connections
    try {
      if (tName.equals(dbTable1)) {
        con4.close();
        con4 = null;
        TestUtil.logMsg("Closed " + dbTable1 + " connection");
      } else {
        con3.close();
        con3 = null;
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
      TestUtil.logErr("SQLException connecting to " + dbTable1 + " DB con1 ",
          e);
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
      TestUtil.logMsg("con2: " + con2.toString());
    } catch (Exception ee) {
      TestUtil.logErr("Exception connecting to EIS con2 ", ee);
      throw new EJBException(ee.getMessage());
    }
  }

  private void conTable3() {
    TestUtil.logTrace("conTable3");
    try {
      // Get connection info for dbTable1 DB
      con3 = ds3.getConnection();
      TestUtil.logTrace("con3: " + con3.toString());
    } catch (Exception ee) {
      TestUtil.logErr("Exception connecting to EIS con3 ", ee);
      throw new EJBException(ee.getMessage());
    }
  }

  private void conTable4() {
    TestUtil.logTrace("conTable4");
    try {
      // Get connection info for dbTable1 DB
      con4 = ds4.getConnection();
      TestUtil.logTrace("con4: " + con4.toString());
    } catch (SQLException e) {
      TestUtil.logErr("SQLException connecting to " + dbTable1 + " DB con4 ",
          e);
      throw new EJBException(e.getMessage());
    } catch (Exception ee) {
      TestUtil.logErr("Exception connecting to " + dbTable1 + " DB con4 ", ee);
      throw new EJBException(ee.getMessage());
    }
  }

  private void createTable1() {
    TestUtil.logTrace("createTable1");
    // drop dbTable1 table if it exists
    try {
      dropTable1();
      TestUtil.logTrace("All rows deleted from table " + dbTable1);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg(
          "SQLException encountered in createTable1: " + e.getMessage());
    }

    try {
      // Add the prescribed table rows
      TestUtil.logMsg("Adding the " + dbTable1 + " table rows");
      String updateString = TestUtil.getProperty("Xa_Tab1_Insert");
      pStmt = con4.prepareStatement(updateString);

      for (int i = 1; i <= dbSize1.intValue(); i++) {
        // Perform the insert(s)
        int newKey = i;
        String newName = dbTable1 + "-" + i;
        // float newPrice = i + (float).00;

        pStmt.setInt(1, newKey);
        pStmt.setString(2, newName);
        pStmt.setString(3, newName);
        // pStmt.setFloat(3, newPrice);

        pStmt.executeUpdate();
      }

      pStmt.close();
    } catch (SQLException e) {
      TestUtil.logErr("SQLException creating " + dbTable1 + " table", e);
      throw new EJBException(e.getMessage());
    }
  }

  private void createTable2() {
    try {
      dropTable2();
      TestUtil.logTrace("All rows deleted from EIS ");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg(
          "SQLException encountered in createTable2: " + e.getMessage());
    }
    try {
      TestUtil.logMsg("Adding the EIS rows");
      for (int i = 1; i <= dbSize1.intValue(); i++) {
        con3.insert((new Integer(i)).toString(), (new Integer(i)).toString());
      }
    } catch (Exception e) {
      TestUtil.logErr("SQLException creating " + dbTable1 + " table", e);
      throw new EJBException(e.getMessage());
    }
  }

  private void dropTable1() {
    TestUtil.logTrace("dropTable1");
    // Delete the data in dbTable1 table
    String removeString = TestUtil.getProperty("Xa_Tab1_Delete");
    try {
      stmt = con4.createStatement();
      stmt.executeUpdate(removeString);
      stmt.close();
    } catch (SQLException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

  private void dropTable2() {
    TestUtil.logTrace("dropTable2");
    // Delete the data from EIS table
    try {
      con3.dropTable();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

}
