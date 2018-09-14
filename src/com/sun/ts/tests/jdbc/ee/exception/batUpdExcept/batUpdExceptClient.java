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
 * @(#)batUpdExceptClient.java	1.19 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.exception.batUpdExcept;

import java.io.*;
import java.util.*;

import java.sql.*;
import javax.sql.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.rmi.RemoteException;

import com.sun.javatest.Status;
import com.sun.ts.tests.jdbc.ee.common.*;

// Merant DataSource class
// import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The batUpdExceptClient class tests methods of BatchUpdateException using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class batUpdExceptClient extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.exception.batUpdExcept";

  /*
   * used by testGetUpdateCounts to represent: 1. number of entries in the
   * attempted batch 2. updateCount array offset that will fail 3. Status to be
   * returned in the updatecount array offset if the driver continues after the
   * failure.
   */
  private static final int MAXUPDATECOUNT_ENTRIES = 4;

  private static final int UPDATECOUNTERROR_LOCATION = 3;

  private static final int DRIVERCONTINUES_ERRORSTATE = -3;

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private ResultSet rs = null;

  private Statement stmt = null;

  private DataSource ds1 = null;

  private dbSchema dbSch = null;

  private String drManager = null;

  private transient DatabaseMetaData dbmd = null;

  private Properties sqlp = null;

  private boolean supbatupdflag;

  private boolean isThrown = false;

  private int[] intialVal = new int[MAXUPDATECOUNT_ENTRIES];

  private String sReason = null;

  private String sSqlState = null;

  private String sVendorCode = null;

  private String sIntialVal = null;

  private String sUsr, sPass, sUrl;

  private int vendorCode = 0, maxVal = 0, minVal = 0;

  private int[] updateCount = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    batUpdExceptClient theTests = new batUpdExceptClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: Driver, the Driver name; db1, the database name with
   * url; user1, the database user name; password1, the database password; db2,
   * the database name with url; user2, the database user name; password2, the
   * database password; DriverManager, flag for DriverManager; ptable, the
   * primary table; ftable, the foreign table; cofSize, the initial size of the
   * ptable; cofTypeSize, the initial size of the ftable; binarySize, size of
   * binary data type; varbinarySize, size of varbinary data type;
   * longvarbinarySize, size of longvarbinary data type;
   * 
   * @class.testArgs: -ap tssql.stmt
   */
  public void setup(String[] args, Properties p) throws Fault {
    try {
      try {
        drManager = p.getProperty("DriverManager", "");
        if (drManager.length() == 0)
          throw new Fault("Invalid DriverManager Name");
        sUrl = p.getProperty("db1", "");
        sUsr = p.getProperty("user1", "");
        sPass = p.getProperty("password1", "");
        sqlp = p;

        if (drManager.equals("yes")) {
          logTrace("Using DriverManager");
          DriverManagerConnection dmCon = new DriverManagerConnection();
          conn = dmCon.getConnection(p);
        } else {
          logTrace("Using DataSource");
          DataSourceConnection dsCon = new DataSourceConnection();
          conn = dsCon.getConnection(p);
        }
        dbSch = new dbSchema();
        dbSch.createData(p, conn);
        dbmd = conn.getMetaData();
        msg = new JDBCTestMsg();
        supbatupdflag = dbmd.supportsBatchUpdates();
        logTrace("Driver Supports BatchUpdates  : " + supbatupdflag);
        if (!supbatupdflag) {
          logTrace("Driver does not support Batch Updates ");
          throw new Fault("Driver does not support Batch Updates ");
        }

        stmt = conn.createStatement();
        StringTokenizer sToken = null;
        sReason = sqlp.getProperty("Reason_BatUpdExec");
        logTrace("Reason : " + sReason);
        sSqlState = sqlp.getProperty("SQLState_BatUpdExec");
        logTrace("SQLState : " + sSqlState);
        sVendorCode = sqlp.getProperty("VendorCode_BatUpdExec");
        logTrace("VendorCode : " + sVendorCode);
        sIntialVal = sqlp.getProperty("IntialValue_BatUpdExec");
        logTrace("IntialVal : " + sIntialVal);
        sVendorCode = sVendorCode.trim();
        vendorCode = Integer.valueOf(sVendorCode).intValue();
        sIntialVal = sIntialVal.substring(sIntialVal.indexOf('{') + 1,
            sIntialVal.lastIndexOf('}'));
        sToken = new StringTokenizer(sIntialVal, ",");
        updateCount = new int[sToken.countTokens()];
        int i = 0;
        while (sToken.hasMoreTokens()) {
          updateCount[i++] = Integer.parseInt(sToken.nextToken());
        }
      } catch (SQLException ex) {
        logErr("SQL Exception : " + ex.getMessage());
        throw new Fault("Set Up Failed", ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
      throw new Fault("Setup Failed");
    }
  }

  /*
   * @testName: testGetUpdateCounts
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:121;
   * 
   * @test_Strategy: Get a Statement object and call the addBatch() method with
   * 4 SQL statements ,out of which first 3 statements are insert,update and
   * delete statements followed by a select statement and call the
   * executeBatch() method. This causes a BatchUpdateException being thrown and
   * getUpdateCounts() method on this BatchUpdateException object should return
   * an integer array of length 3 with each value indicating the number of
   * corressponding rows affected by execution of 3 SQL statements in the same
   * order as added to the batch.
   *
   */

  public void testGetUpdateCounts() throws Fault {
    try {
      isThrown = false;
      loading(stmt);
    } catch (BatchUpdateException b) {
      isThrown = true;
      boolean isFailure = false;
      int retVal[] = b.getUpdateCounts();
      msg.setMsg("The Length of return array " + retVal.length);
      msg.setMsg("The Length of orginal array " + intialVal.length);
      if (retVal.length < MAXUPDATECOUNT_ENTRIES) {
        for (int i = 0; i < retVal.length; i++) {
          msg.addOutputMsg(" " + intialVal[i], " " + retVal[i]);

          if (intialVal[i] != retVal[i]) {
            isFailure = true;
            msg.printTestError(
                " getUpdateCount doesnot return the correct number of effected rows in the update count offset. ",
                "call to testGetUpdateCount Failed!");
            break;
          }
        }
      } else if (retVal.length == MAXUPDATECOUNT_ENTRIES) {
        msg.addOutputMsg("" + DRIVERCONTINUES_ERRORSTATE,
            "" + retVal[UPDATECOUNTERROR_LOCATION]);

        if (retVal[UPDATECOUNTERROR_LOCATION] != DRIVERCONTINUES_ERRORSTATE) {
          isFailure = true;
          msg.printTestError(
              "Driver Continues after error but does not return an error state of "
                  + DRIVERCONTINUES_ERRORSTATE
                  + " in the correct update count offset. ",
              "call to testGetUpdateCount Failed!");
        }
      } else if (retVal.length > MAXUPDATECOUNT_ENTRIES) {
        isFailure = true;
        msg.printTestError(
            "More entries returned in update count than queries executed. ",
            "call to testGetUpdateCount Failed!");
      }

      if (isFailure) {
        throw new Fault("Call to getUpdateCounts is Failed!", b);
      } else {
        msg.setMsg(" getUpdateCounts returns the number of rows affected ");
      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getUpdateCounts Failed!");

    } catch (Exception ex) {
      msg.printError(ex, "Call to getUpdateCount Failed!");

    }
    if (!isThrown) {
      msg.printTestError("BatchUpdateException not thrown",
          "Call to getUpdateCount Fails");
    }
  }

  /*
   * The methods adds the SQL statements to Statement object using addBatch()
   * method and sets the initialisation counter to the number of statements
   * added to the batch.
   */
  public void loading(Statement stmt) throws java.sql.BatchUpdateException,
      java.sql.SQLException, java.lang.Exception {

    Statement state = conn.createStatement();
    int i = 0;
    ResultSet result = null;
    boolean isReturn = false;
    String sInsCount = null;
    try {

      String sUpdCoffee = sqlp.getProperty("Coffee_UpdTab");
      String sDelCoffee = sqlp.getProperty("Coffee_DelTab");
      String sInsCoffee = sqlp.getProperty("Coffee_InsTab");
      String sSelCoffee = sqlp.getProperty("Coffee_SelTab");

      String sUpdCount = sqlp.getProperty("Coffee_Updcount_Query");
      String sDelCount = sqlp.getProperty("Coffee_Delcount_Query");
      sInsCount = sqlp.getProperty("Coffee_Inscount_Query");

      result = state.executeQuery(sUpdCount);
      isReturn = result.next();
      if (isReturn) {
        intialVal[i++] = result.getInt(1);
      }

      result = state.executeQuery(sDelCount);
      isReturn = result.next();
      if (isReturn) {
        intialVal[i++] = result.getInt(1);
      }

      msg.setMsg("calling addBatch method");

      stmt.addBatch(sUpdCoffee);
      stmt.addBatch(sDelCoffee);
      stmt.addBatch(sInsCoffee);
      stmt.addBatch(sSelCoffee);

      msg.setMsg("calling executeBatch method");
      stmt.executeBatch();
    } catch (BatchUpdateException e) {
      msg.setMsg(e.toString());
      result = state.executeQuery(sInsCount);
      isReturn = result.next();
      if (isReturn) {
        intialVal[i++] = result.getInt(1);
      }
      throw e;
    }
  }

  /*
   * @testName: testBatchUpdateException01
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:120;
   * 
   * @test_Strategy: This method constructs a BatchUpdateException Object with
   * no arguments and for that object the reason, SQLState,ErrorCode and
   * updateCounts are checked for default values.
   */

  public void testBatchUpdateException01() throws Fault {
    try {
      isThrown = false;
      throw new BatchUpdateException();
    } catch (BatchUpdateException b) {
      isThrown = true;
      if ((b.getMessage() != null) || (b.getSQLState() != null)
          || (b.getErrorCode() != 0) || (b.getUpdateCounts() != null)) {
        msg.printSQLError(b, "BatchUpdateException() Constructor Fails");

      } else {
        msg.setMsg("BatchUpdateException() constructor is implemented");
      }
      msg.printTestMsg();
    } catch (Exception ex) {
      msg.printError(ex, "BatchUpdateException() Constructor Fails");

    }

    if (!isThrown) {

      msg.printTestError("BatchUpdateException not thrown",
          "Call to BatchUpdateException() constructor Fails");

    }
  }

  /*
   * @testName: testBatchUpdateException02
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:119;
   * 
   * @test_Strategy: This method constructs a BatchUpdateException Object with
   * one argument and for that object the reason, SQLState,ErrorCode are checked
   * for default values.The updateCount array is checked for whatever is been
   * assigned while creating the new instance.
   */

  public void testBatchUpdateException02() throws Fault {
    try {
      isThrown = false;
      throw new BatchUpdateException(updateCount);
    } catch (BatchUpdateException b) {
      isThrown = true;
      if ((b.getMessage() != null) || (b.getSQLState() != null)
          || (b.getErrorCode() != 0)) {
        msg.printSQLError(b, "BatchUpdateException() Constructor Fails");

      } else {
        if (!checkForUpdateCount(b.getUpdateCounts())) {
          msg.printTestError("BatchUpdateException(int []) Constructor Fails",
              "Call to BatchUpdateException(int []) Constructor Fails");

        } else {
          msg.setMsg("BatchUpdateException(int []) Constructor is implemented");
        }
      }
      msg.printTestMsg();
    } catch (Exception ex) {
      msg.printError(ex, "BatchUpdateException() Constructor Fails");

    }

    if (!isThrown) {
      msg.printTestError("BatchUpdateException(int []) not thrown",
          "BatchUpdateException(int []) Constructor Fails");

    }
  }

  /*
   * @testName: testBatchUpdateException03
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:118;
   * 
   * @test_Strategy: This method constructs a BatchUpdateException Object with
   * two arguments and for that object. SQLState,ErrorCode are checked for
   * default values.The reason and updateCount array is checked for whatever is
   * been assigned while creating the new instance.
   */

  public void testBatchUpdateException03() throws Fault {
    try {
      isThrown = false;
      throw new BatchUpdateException(sReason, updateCount);
    } catch (BatchUpdateException b) {
      TestUtil.printStackTrace(b);

      isThrown = true;
      if ((b.getSQLState() != null) || (b.getErrorCode() != 0)) {
        msg.printTestError(
            "BatchUpdateException(String,int []) Constructor Fails",
            "Call to BatchUpdateException(String,int []) constructor Fails");

      } else {
        if ((!checkForUpdateCount(b.getUpdateCounts()))
            || (!sReason.equals(b.getMessage()))) {
          msg.printTestError(
              "BatchUpdateException(String,int []) Constructor Fails",
              "Call to BatchUpdateException(String,int []) constructor Fails");

        } else {
          msg.setMsg(
              "Call to BatchUpdateException(String,int []) constructor Passes");
        }
      }
      msg.printTestMsg();
    } catch (Exception ex) {
      msg.printError(ex,
          "Call to BatchUpdateException(String,int []) constructor Fails");

    }

    if (!isThrown) {
      msg.printTestError(
          "Call to BatchUpdateException(String,int []) constructor Fails",
          "Call to BatchUpdateException(String,int []) constructor Fails");

    }
  }

  /*
   * @testName: testBatchUpdateException04
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:117;
   * 
   * @test_Strategy: This method constructs a BatchUpdateException Object with
   * three arguments and for that object. ErrorCode is checked for default
   * values.The reason,SQLState and updateCount array is checked for whatever is
   * been assigned while creating the new instance.
   */

  public void testBatchUpdateException04() throws Fault {
    try {
      isThrown = false;
      throw new BatchUpdateException(sReason, sSqlState, updateCount);
    } catch (BatchUpdateException b) {
      TestUtil.printStackTrace(b);

      isThrown = true;
      if ((b.getErrorCode() != 0)) {
        msg.printTestError(
            "BatchUpdateException(string,string,int[]) Constructor Fails",
            "Call to BatchUpdateException(string,string,int[]) Constructor Fails");

      } else {
        if ((!checkForUpdateCount(b.getUpdateCounts()))
            || (!sReason.equals(b.getMessage()))
            || (!sSqlState.equals(b.getSQLState()))) {
          msg.printTestError(
              "BatchUpdateException(string,string,int[]) Constructor Fails",
              "Call to BatchUpdateException(string,string,int[]) Constructor Fails");

        } else {
          msg.setMsg(
              "BatchUpdateException(string,string,int[]) Constructor is implemented");
        }
      }
      msg.printTestMsg();
    } catch (Exception ex) {
      msg.printError(ex,
          "Call to BatchUpdateException(string,string,int[]) Constructor Fails");

    }

    if (!isThrown) {
      msg.printTestError(
          "BatchUpdateException(string,string,int[]) Constructor not thrown",
          "Call to BatchUpdateException(string,string,int[]) Constructor Fails");

    }
  }

  /*
   * @testName: testBatchUpdateException05
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:116;
   * 
   * @test_Strategy: This method constructs a BatchUpdateException Object with
   * four arguments The reason,SQLState ,ErrorCode and updateCount array is
   * checked for whatever is been assigned while creating the new instance.
   */

  public void testBatchUpdateException05() throws Fault {
    try {
      isThrown = false;
      throw new BatchUpdateException(sReason, sSqlState, vendorCode,
          updateCount);
    } catch (BatchUpdateException b) {
      TestUtil.printStackTrace(b);

      isThrown = true;
      if ((!checkForUpdateCount(b.getUpdateCounts()))
          || (!sReason.equals(b.getMessage()))
          || (!sSqlState.equals(b.getSQLState()))
          || (!(vendorCode == b.getErrorCode()))) {
        msg.printTestError(
            "BatchUpdateException(string,string,int,int []) Constructor Fails",
            "Call to BatchUpdateException(string,string,int,int[]) Constructor Fails");

      } else {
        msg.setMsg(
            "BatchUpdateException(string,string,int,int []) Constructor is implemented");
      }
      msg.printTestMsg();
    } catch (Exception ex) {
      msg.printError(ex,
          "Call to BatchUpdateException(string,string,int,int[]) Constructor Fails");

    }

    if (!isThrown) {
      msg.printTestError(
          "BatchUpdateException(string,string,int,int[]) Constructor Fails",
          "Call to BatchUpdateException(string,string,int,int[]) Constructor Fails");

    }
  }

  /*
   * This method is used for checking each element of the updateCount array and
   * the Return array.It returns true if all elements matches or else it returns
   * false .
   */
  public boolean checkForUpdateCount(int[] retVal) {
    for (int i = 0; i < retVal.length; i++) {
      msg.setMsg("IntialVal : " + updateCount[i] + " ReturnVal :" + retVal[i]);
      if (updateCount[i] != retVal[i])
        return false;
    }

    return true;
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      stmt.close();
      dbSch.destroyData(conn);
      // Close the database
      dbSch.dbUnConnect(conn);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }
}
