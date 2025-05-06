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
 * @(#)TxBeanEJB.java	1.4 03/05/16
 */

/*
 * @(#)TxBeanEJB.java   1.0 02/07/31
 */

package com.sun.ts.tests.xa.ee.resXcomp1;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;
import com.sun.ts.tests.common.connector.whitebox.TSEISDataSource;

import jakarta.ejb.EJBException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Vector;

import javax.sql.DataSource;

public class TxBeanEJB {

    // con1 will be used for the dbTable1 connection
    private transient Connection connection1;
    private transient Statement statement;
    private transient PreparedStatement preparedStatement;

    private String dbUser1, dbPassword1, dbTable1;

    // TableSizes
    // dbSize1 is the size of dbTable1
    private Integer dbSize1;

    // DataSources
    // ds1 is associated with dbTable1
    private DataSource ds1;

    private TSNamingContext context;

    // TSEIS
    private TSEISDataSource ds2;

    private transient TSConnection connection2;

    public void initialize() {
        String eMsg = "";
        TestUtil.logTrace("initialize");
        try {

            context = new TSNamingContext();
            dbSize1 = (Integer) context.lookup("java:comp/env/size");

            eMsg = "Exception looking up JDBCwhitebox";
            ds1 = (DataSource) context.lookup("java:comp/env/eis/JDBCwhitebox-tx");
            TestUtil.logTrace("ds1: " + ds1);

            eMsg = "Exception looking up EIS whitebox";
            ds2 = (TSEISDataSource) context.lookup("java:comp/env/eis/whitebox-tx");
            TestUtil.logTrace("ds2: " + ds2);
        } catch (Exception e) {
            e.printStackTrace();
            TestUtil.logTrace(eMsg);
            TestUtil.logErr("Unexpected exception getting the DB DataSource", e);
            throw new EJBException(e.getMessage());
        }
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
                conTable2();
                TestUtil.logMsg("Made the connection to EIS");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                createTable2();
                TestUtil.logMsg("Created the EIS data");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TestUtil.logErr("Exception creating table", e);
            throw new EJBException(e.getMessage());
        }
    }

    public boolean insert(String tName, int key) {
        TestUtil.logTrace("insert");
        // Insert a row into the specified table
        int newKey = key;
        String newName = null;

        try {
            if (tName.equals(dbTable1)) {
                // Prepare the new dbTable1 row entry
                newName = dbTable1 + "-" + newKey;

                String updateString = TestUtil.getProperty("Xa_Tab1_Insert");
                preparedStatement = connection1.prepareStatement(updateString);
                // Perform the insert(s)
                preparedStatement.setInt(1, newKey);
                preparedStatement.setString(2, newName);
                preparedStatement.setString(3, newName);
                preparedStatement.executeUpdate();
                preparedStatement.close();

                TestUtil.logTrace("Inserted a row into the table " + tName);
            } else {
                // Prepare the new data entry in EIS
                connection2.insert(Integer.toString(key), Integer.toString(key));
                TestUtil.logTrace("Inserted a row into the EIS ");
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            TestUtil.logErr("Exception inserting a row into table " + tName + ";\n" + e.getMessage(), e);
            return false;
        }
    }

    public void delete(String tName, int fromKey, int toKey) {
        TestUtil.logTrace("delete");
        try {
            if (tName.equals(dbTable1)) {
                // Delete row(s) from the specified table
                String updateString = TestUtil.getProperty("Xa_Tab1_Delete1");
                preparedStatement = connection1.prepareStatement(updateString);
                for (int i = fromKey; i <= toKey; i++) {
                    preparedStatement.setInt(1, i);
                    preparedStatement.executeUpdate();
                }
                preparedStatement.close();
                TestUtil.logTrace("Deleted row(s) " + fromKey + " thru " + toKey + " from the table " + tName);
            } else {
                // Delete rows from EIS
                for (int i = fromKey; i <= toKey; i++) {
                    connection2.delete(Integer.toString(i));
                }
                TestUtil.logTrace("Deleted row(s) from EIS " + fromKey + " thru " + toKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
            TestUtil.logErr("Exception deleting row(s) " + fromKey + " thru " + toKey + " from the table " + tName, e);
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
                TestUtil.logMsg("Deleted all rows from EIS");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TestUtil.logErr("Exception occured trying to drop table", e);
            throw new EJBException(e.getMessage());
        }
    }

    public void dbUnConnect(String tName) {
        TestUtil.logTrace("dbUnConnect");
        // Close the DB connections
        try {
            if (tName.equals(dbTable1)) {
                connection1.close();
                connection1 = null;
                TestUtil.logTrace("Closed " + dbTable1 + " connection");
            } else {
                connection2.close();
                connection2 = null;
                TestUtil.logTrace("Closed EIS connection");
            }
        } catch (Exception e) {
            e.printStackTrace();
            TestUtil.logErr("Exception occured trying to close the DB connection", e);
            throw new EJBException(e.getMessage());
        }
    }

    // Test Results methods
    public Vector getResults(String tName) {
        TestUtil.logTrace("getResults");
        ResultSet rs = null;
        Vector queryResults = new Vector();
        int i;
        String query, s, s1, name = null;

        try {
            if (tName.equals(dbTable1)) {
                query = TestUtil.getProperty("Xa_Tab1_Select");
                statement = connection1.createStatement();
                rs = statement.executeQuery(query);
                name = "COF_NAME";
                if (rs != null) {
                    while (rs.next()) {
                        i = rs.getInt(1);
                        s = rs.getString(2);
                        s1 = rs.getString(3);
                        queryResults.addElement(i);
                        queryResults.addElement(s);
                        queryResults.addElement(s1);
                    }
                }
                statement.close();
                TestUtil.logMsg("Obtained " + tName + " table ResultSet");
            } else {
                queryResults = connection2.readData();
            }

        } catch (Exception e) {
            e.printStackTrace();
            TestUtil.logErr("Exception obtaining " + tName + " table ResultSet", e);
            throw new EJBException(e.getMessage());
        }
        return queryResults;
    }

    public void initLogging(Properties p) {
        TestUtil.logTrace("initLogging");
        try {
            TestUtil.init(p);
            // Get the dbTable1 DataSource
            dbTable1 = TestUtil.getTableName(TestUtil.getProperty("Xa_Tab1_Delete"));
            TestUtil.logMsg(dbTable1 + " initLogging OK!");

        } catch (RemoteLoggingInitException e) {
            e.printStackTrace();
            TestUtil.printStackTrace(e);
            throw new EJBException(e.getMessage());
        }
    }

    // Exception methods
    public void throwEJBException() throws EJBException {
        TestUtil.logTrace("throwEJBException");
        throw new EJBException("EJBException from TxBean");
    }

    public void listTableData(Vector dbResults) {
        TestUtil.logTrace("listTableData");
        try {
            if (dbResults.isEmpty())
                TestUtil.logTrace("Empty vector!!!");
            else {
                for (int j = 0; j < dbResults.size(); j++)
                    TestUtil.logTrace(dbResults.elementAt(j).toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            TestUtil.logErr("Exception occured trying to list table data", e);
            throw new EJBException(e.getMessage());
        }
    }

    // private methods
    private void conTable1() {
        TestUtil.logTrace("conTable1");
        try {
            // Get connection info for dbTable1 DB
            connection1 = ds1.getConnection();
            TestUtil.logTrace("con1: " + connection1.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            TestUtil.logErr("SQLException connecting to " + dbTable1 + " DB", e);
            throw new EJBException(e.getMessage());
        } catch (Exception ee) {
            ee.printStackTrace();
            TestUtil.logErr("Exception connecting to " + dbTable1 + " DB", ee);
            throw new EJBException(ee.getMessage());
        }
    }

    private void conTable2() {
        TestUtil.logTrace("conTable2");
        try {
            // Get connection info for dbTable1 DB
            connection2 = ds2.getConnection();
            TestUtil.logTrace("con2: " + connection2.toString());
        } catch (Exception ee) {
            ee.printStackTrace();
            TestUtil.logErr("Exception connecting to EIS ", ee);
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
            e.printStackTrace();
            TestUtil.printStackTrace(e);
            TestUtil.logMsg("SQLException encountered in createTable1: " + e.getMessage());
        }

        try {
            // Add the prescribed table rows
            TestUtil.logMsg("Adding the " + dbTable1 + " table rows");
            String updateString = TestUtil.getProperty("Xa_Tab1_Insert");
            preparedStatement = connection1.prepareStatement(updateString);

            for (int i = 1; i <= dbSize1.intValue(); i++) {
                // Perform the insert(s)
                int newKey = i;
                String newName = dbTable1 + "-" + i;

                preparedStatement.setInt(1, newKey);
                preparedStatement.setString(2, newName);
                preparedStatement.setString(3, newName);
                preparedStatement.executeUpdate();
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
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
            TestUtil.logMsg("SQLException encountered in createTable2: " + e.getMessage());
        }
        try {
            TestUtil.logMsg("Adding the EIS rows");
            for (int i = 1; i <= dbSize1.intValue(); i++) {
                connection2.insert(Integer.toString(i), Integer.toString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
            TestUtil.logErr("SQLException creating " + dbTable1 + " table", e);
            throw new EJBException(e.getMessage());
        }
    }

    private void dropTable1() {
        TestUtil.logTrace("dropTable1");
        // Delete the data in dbTable1 table
        String removeString = TestUtil.getProperty("Xa_Tab1_Delete");
        try {
            statement = connection1.createStatement();
            statement.executeUpdate(removeString);
            statement.close();
        } catch (SQLException e) {
            TestUtil.logErr("SQLException dropping " + dbTable1 + " table", e);
            throw new EJBException(e.getMessage());
        }
    }

    private void dropTable2() {
        TestUtil.logTrace("dropTable2");
        // Delete the data from EIS table
        try {
            connection2.dropTable();
        } catch (Exception e) {
            e.printStackTrace();
            throw new EJBException(e.getMessage());
        }
    }

}
