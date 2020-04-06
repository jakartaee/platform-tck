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
 * @(#)TxBeanEJB.java	1.24 03/05/16
 */

package com.sun.ts.tests.jta.ee.txpropagationtest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import java.sql.*;
import javax.sql.*;
import jakarta.ejb.*;

public class TxBeanEJB implements SessionBean {
  // testProps represent the test specific properties passed in
  // from the test harness.
  private Properties testProps = null;

  private SessionContext sctx = null;

  // con1 will be used for the dbTable1 connection
  // con2 will be used for the dbTable2 connection
  private transient Connection con1, con2;

  private transient Statement stmt;

  private transient PreparedStatement pStmt;

  // dbUser1 and dbPassword1 are used for con1 - dbTable1
  // dbUser2 and dbPassword2 are used for con2 - dbTable2
  private String dbUser1, dbPassword1, dbTable1, dbTable2;

  // TableSizes
  // dbSize1 is the size of dbTable1
  // dbSize1 is the size of dbTable2
  private Integer dbSize1;

  private Integer dbSize2;

  // DataSources
  // ds1 is associated with dbTable1
  // ds2 is associated with dbTable2
  private DataSource ds1, ds2;

  private TSNamingContext context = null;

  // Required EJB methods
  public void ejbCreate(Properties p) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    testProps = p;
    try {
      TestUtil.init(p);
      context = new TSNamingContext();
      // Get the dbTable1 DataSource
      dbTable1 = TestUtil.getTableName(p.getProperty("JTA_Tab1_Delete"));
      dbSize1 = (Integer) context.lookup("java:comp/env/size");
      ds1 = (DataSource) context.lookup("java:comp/env/jdbc/DB1");
      TestUtil.logTrace("ds1: " + ds1);
      TestUtil.logMsg(dbTable1 + " DataSource lookup OK!");

      // Get the dbTable2 DataSource
      dbTable2 = TestUtil.getTableName(p.getProperty("JTA_Tab2_Delete"));
      dbSize2 = (Integer) context.lookup("java:comp/env/size");
      ds2 = (DataSource) context.lookup("java:comp/env/jdbc/DB1");
      TestUtil.logTrace("ds2: " + ds2);
      TestUtil.logMsg(dbTable2 + " DataSource lookup OK!");

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception getting the DB DataSource", e);
      throw new CreateException(e.getMessage());
    }
  }

  public void setSessionContext(SessionContext sc) {
    TestUtil.logTrace("setSessionContext");
    this.sctx = sc;
  }

  public void ejbRemove() {
    TestUtil.logTrace("ejbRemove");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  // ===========================================================
  // The TxBean interface implementation

  // Database methods
  public void dbConnect(String tName) {
    TestUtil.logTrace("dbConnect");
    try {
      if (tName.equals(dbTable1)) {
        // Make the dbTable1 connection
        conTable1();
        TestUtil.logMsg("Made the JDBC connection to " + dbTable1 + " DB");
      } else {
        // Make the dbTable2 connection
        conTable2();
        TestUtil.logMsg("Made the JDBC connection to " + dbTable2 + " DB");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception on JDBC connection", e);
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
        // Create the dbTable2 table
        createTable2();
        TestUtil.logMsg("Created the table " + dbTable2 + " ");
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception creating table", e);
      throw new EJBException(e.getMessage());
    }
  }

  public boolean insert(String tName, int key) {
    TestUtil.logTrace("insert");
    // Insert a row into the specified table
    int newKey = key;
    String newName = null;
    float newPrice = (float) .00 + newKey;

    try {
      if (tName.equals(dbTable1)) {
        // Prepare the new dbTable1 row entry
        newName = dbTable1 + "-" + newKey;

        String updateString = TestUtil.getProperty("JTA_Tab1_Insert");
        pStmt = con1.prepareStatement(updateString);
      } else {
        // Prepare the new dbTable2 row entry
        newName = dbTable2 + "-" + newKey;

        String updateString = TestUtil.getProperty("JTA_Tab2_Insert");
        pStmt = con2.prepareStatement(updateString);
      }

      // Perform the insert(s)
      pStmt.setInt(1, newKey);
      pStmt.setString(2, newName);
      pStmt.setFloat(3, newPrice);
      pStmt.executeUpdate();
      pStmt.close();

      TestUtil.logTrace("Inserted a row into the table " + tName);
      return true;

    } catch (Exception e) {
      TestUtil.logErr("Exception inserting a row into table " + tName + ";\n"
          + e.getMessage(), e);
      return false;
    }
  }

  public void delete(String tName, int fromKey, int toKey) {
    TestUtil.logTrace("delete");
    // Delete row(s) from the specified table
    try {
      if (tName.equals(dbTable1)) {
        String updateString = TestUtil.getProperty("JTA_Delete1");
        pStmt = con1.prepareStatement(updateString);
      } else {
        String updateString = TestUtil.getProperty("JTA_Delete2");
        pStmt = con2.prepareStatement(updateString);
      }

      // Perform the delete(s)
      for (int i = fromKey; i <= toKey; i++) {
        pStmt.setInt(1, i);
        pStmt.executeUpdate();
      }
      pStmt.close();

      TestUtil.logTrace("Deleted row(s) " + fromKey + " thru " + toKey
          + " from the table " + tName);

    } catch (Exception e) {
      TestUtil.logErr("Exception deleting row(s) " + fromKey + " thru " + toKey
          + " from the table " + tName, e);
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
        TestUtil.logMsg("Deleted all rows from table " + dbTable2);
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
        TestUtil.logTrace("Closed " + dbTable2 + " connection");
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occured trying to close the DB connection", e);
      throw new EJBException(e.getMessage());
    }
  }

  // Test Results methods
  public Vector getResults(String tName) {
    TestUtil.logTrace("getResults");
    ResultSet rs;
    Vector queryResults = new Vector();
    int i;
    String query, s, name;
    float f;

    try {
      if (tName.equals(dbTable1)) {
        query = TestUtil.getProperty("JTA_Tab1_Select");
        stmt = con1.createStatement();
        rs = stmt.executeQuery(query);
        name = "COF_NAME";
      } else {
        query = TestUtil.getProperty("JTA_Tab2_Select");
        stmt = con2.createStatement();
        rs = stmt.executeQuery(query);
        name = "CHOC_NAME";
      }

      while (rs.next()) {
        i = rs.getInt("KEY_ID");
        s = rs.getString(name);
        f = rs.getFloat("PRICE");
        queryResults.addElement(new Integer(i));
        queryResults.addElement(s);
        queryResults.addElement(new Float(f));
      }

      stmt.close();
      TestUtil.logMsg("Obtained " + tName + " table ResultSet");

    } catch (Exception e) {
      TestUtil.logErr("Exception obtaining " + tName + " table ResultSet", e);
      throw new EJBException(e.getMessage());
    }
    return queryResults;
  }

  public void initLogging(Properties p) {
    TestUtil.logTrace("initLogging");
    this.testProps = p;
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

  // private methods
  private void conTable1() {
    TestUtil.logTrace("conTable1");
    try {
      // Get connection info for dbTable1 DB
      // dbUser1 = testProps.getProperty("user1");
      // dbPassword1 = testProps.getProperty("password1");
      // TestUtil.logTrace("DB user1: " + dbUser1);
      // TestUtil.logTrace("DB password1: " + dbPassword1);

      // Make the JDBC DB connection to dbTable1 DB
      // con1 = ds1.getConnection(dbUser1, dbPassword1);
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
      // Get connection info for dbTable2 DB
      // dbUser2 = testProps.getProperty("user2");
      // dbPassword2 = testProps.getProperty("password2");
      // TestUtil.logTrace("DB user2: " + dbUser2);
      // TestUtil.logTrace("DB password2: " + dbPassword2);

      // Make the JDBC DB connection to dbTable2 DB
      // con2 = ds2.getConnection(dbUser1, dbPassword1);
      con2 = ds2.getConnection();
      TestUtil.logTrace("con2: " + con2.toString());
    } catch (SQLException e) {
      TestUtil.logErr("SQLException connecting to " + dbTable2 + " DB", e);
      throw new EJBException(e.getMessage());
    } catch (Exception ee) {
      TestUtil.logErr("Exception connecting to " + dbTable2 + " DB", ee);
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
      String updateString = TestUtil.getProperty("JTA_Tab1_Insert");
      pStmt = con1.prepareStatement(updateString);

      for (int i = 1; i <= dbSize1.intValue(); i++) {
        // Perform the insert(s)
        int newKey = i;
        String newName = dbTable1 + "-" + i;
        float newPrice = i + (float) .00;

        pStmt.setInt(1, newKey);
        pStmt.setString(2, newName);
        pStmt.setFloat(3, newPrice);

        pStmt.executeUpdate();
      }

      pStmt.close();
    } catch (SQLException e) {
      TestUtil.logErr("SQLException creating " + dbTable1 + " table", e);
      throw new EJBException(e.getMessage());
    }
  }

  private void createTable2() {
    TestUtil.logTrace("createTable2");
    try {
      dropTable2();
      TestUtil.logTrace("All rows deleted from table " + dbTable2);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg(
          "SQLException encountered in createTable2: " + e.getMessage());
    }

    try {

      // Add the prescribed table rows
      TestUtil.logMsg("Adding the " + dbTable2 + " table rows");
      String updateString = TestUtil.getProperty("JTA_Tab2_Insert");

      pStmt = con2.prepareStatement(updateString);

      for (int i = 1; i <= dbSize2.intValue(); i++) {
        // Perform the insert(s)
        int newKey = i;
        String newName = dbTable2 + "-" + i;
        float newPrice = i + (float) .00;

        pStmt.setInt(1, newKey);
        pStmt.setString(2, newName);
        pStmt.setFloat(3, newPrice);

        pStmt.executeUpdate();
      }

      pStmt.close();
    } catch (SQLException e) {
      TestUtil.logErr("SQLException creating " + dbTable2 + " table", e);
      throw new EJBException(e.getMessage());
    }
  }

  private void dropTable1() {
    TestUtil.logTrace("dropTable1");
    // Delete the dbTable1 table
    String removeString = TestUtil.getProperty("JTA_Tab1_Delete");
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
    // Delete the dbTable2 table
    String removeString = TestUtil.getProperty("JTA_Tab2_Delete");
    try {
      stmt = con2.createStatement();
      stmt.executeUpdate(removeString);
      stmt.close();
    } catch (SQLException e) {
      TestUtil.logErr("SQLException dropping " + dbTable2 + " table", e);
      throw new EJBException(e.getMessage());
    }
  }
}
