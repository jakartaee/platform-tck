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
 * $Id$
 */

/*
 * @(#)prepStmtClient16.java	1.16 02/04/23
 */

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt16;

import java.io.*;
import java.util.*;
import java.math.*;
import java.sql.*;
import javax.sql.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.rmi.RemoteException;
import com.sun.javatest.Status;
import com.sun.ts.tests.jdbc.ee.common.*;

/**
 * The prepStmtClient16 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.0, 10/09/2002
 */

public class prepStmtClient16 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.prepStmt.prepStmt16";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private ResultSet rs = null;

  private Statement stmt = null;

  private PreparedStatement pstmt = null;

  private transient DatabaseMetaData dbmd = null;

  private DataSource ds1 = null;

  private dbSchema dbSch = null;

  private rsSchema rsSch = null;

  private csSchema csSch = null;

  private String drManager = null;

  private Properties sqlp = null;

  private Properties props = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    prepStmtClient16 theTests = new prepStmtClient16();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup: */
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
        sqlp = p;
        props = p;

        if (drManager.equals("yes")) {
          logTrace("Using DriverManager");
          DriverManagerConnection dmCon = new DriverManagerConnection();
          conn = dmCon.getConnection(p);
        } else {
          logTrace("Using DataSource");
          DataSourceConnection dsCon = new DataSourceConnection();
          conn = dsCon.getConnection(p);
        }
        stmt = conn.createStatement();
        dbmd = conn.getMetaData();
        rsSch = new rsSchema();
        csSch = new csSchema();
        msg = new JDBCTestMsg();

      } catch (SQLException ex) {
        logErr("SQL Exception: " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testGetParameterMetaData
   * 
   * @assertion_ids: JavaEE:SPEC:186.3; JDBC:JAVADOC:724; JDBC:JAVADOC:725;
   * JDBC:SPEC:9; JDBC:SPEC:26;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Execute the method getParameterMetaData on the preparedStatement
   * object. Get the information about the number of parameters by executing the
   * method getParameterCount(). It should return the number of parameters.
   * 
   */

  public void testGetParameterMetaData() throws Fault {
    ParameterMetaData pmd = null;
    ResultSetMetaData rsmd = null;

    try {
      String sPrepStmt = sqlp.getProperty("CoffeeTab_Query", "");
      msg.setMsg("Prepared Statement String :" + sPrepStmt);

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);
      pmd = pstmt.getParameterMetaData();
      int numberOfParams = pmd.getParameterCount();
      msg.setMsg("numberOfParams" + numberOfParams);

      if (numberOfParams != 0) {
        msg.setMsg(" getParameterCount returns the number of parameters as"
            + numberOfParams);

      } else {
        msg.printTestError(
            "getParameterCount does not return the number of parameters",
            "Test GetParameterMetaData Failed");
      }

      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getMetaData is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getMetaData is Failed!");
    }

    finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }

      } catch (Exception e) {
      }
    }

  }

  /*
   * @testName: testSetAsciiStream
   * 
   * @assertion_ids: JavaEE:SPEC:186; JDBC:JAVADOC:684; JDBC:JAVADOC:685;
   * JDBC:SPEC:9; JDBC:SPEC:26;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Get the InputStream object. Excecute the method
   * preparedStatement.setAsciiStream to Update the Longvarchar_Tab_Name with
   * the value extracted from the Char_Tab. Query the Longvarchar_Tab in the
   * database to retrieve the value that is been set. Compare the value that is
   * inserted with the value retrieved. These values should be equal.
   * 
   */

  public void testSetAsciiStream() throws Fault {
    String sPrepStatement = null;
    String sCoffeeName = null;
    ResultSet rs = null;
    try {
      rsSch.createTab("Longvarchar_Tab", sqlp, conn);
      sCoffeeName = rsSch.extractVal("Char_Tab", 1, sqlp, conn);
      sCoffeeName = sCoffeeName.substring(1, sCoffeeName.length() - 1);
      sPrepStatement = sqlp.getProperty("Longvarchar_Tab_Name_Update", null);
      pstmt = conn.prepareStatement(sPrepStatement);
      msg.setMsg("After creating pstmt...");

      byte[] buf = sCoffeeName.getBytes();
      int len = buf.length;
      InputStream istr = new ByteArrayInputStream(buf);
      msg.setMsg("Execute pstmt.setAsciiStream");
      pstmt.setAsciiStream(1, istr, len);
      pstmt.executeUpdate();

      String longvarchar_query = sqlp.getProperty("Longvarchar_Query_Name", "");
      rs = stmt.executeQuery(longvarchar_query);
      rs.next();

      String retVal = (String) rs.getObject(1);
      msg.addOutputMsg(sCoffeeName, retVal);

      if (sCoffeeName.equalsIgnoreCase(retVal)) {
        msg.setMsg(
            "setObject Method sets the designated parameter with the desired value");

      } else {
        msg.printTestError(
            "setAsciiStream method does not set the designated parameter with the desired value",
            "Call to setAsciiStream failed");
      }
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle)

    {
      msg.printSQLError(sqle, "Call to SetAsciiStream is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to SetAsciiStream is Failed!");
    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }

      } catch (Exception e) {
      }
    }

  }

  /*
   * @testName: testSetBinaryStream
   * 
   * @assertion_ids: JavaEE:SPEC:186; JDBC:JAVADOC:688; JDBC:JAVADOC:689;
   * JDBC:SPEC:9; JDBC:SPEC:26;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Get the InputStream object. Excecute the method
   * preparedStatement.setBinaryStream to Update the Longvarbinary_Tab_Name with
   * some byte array value. Query the Longvarbinary_Tab in the database to
   * retrieve the value that is been set. Compare the byte array value that is
   * inserted with the value retrieved. These cvalues should be equal.
   * 
   */

  public void testSetBinaryStream() throws Fault {
    String sPrepStatement = null;
    String sCoffeeName = null;
    ResultSet rs = null;
    byte retByteArr[] = null;
    try {
      rsSch.createTab("Longvarbinary_Tab", sqlp, conn);
      String sPrepStmt = sqlp.getProperty("Longvarbinary_Tab_Val_Update", "");
      msg.setMsg("Prepared Statement String: " + sPrepStmt);

      String binsize = props.getProperty("longvarbinarySize");
      int bytearrsize = Integer.parseInt(binsize);
      msg.setMsg("Longvarbinary Size: " + bytearrsize);

      byte[] bytearr = new byte[bytearrsize];
      String sbyteval = null;
      // to get the bytearray value
      for (int count = 0; count < bytearrsize; count++) {
        sbyteval = Integer.toString(count % 255);
        bytearr[count] = Byte.parseByte(sbyteval);
      }

      msg.setMsg("get the PreparedStatement object");
      pstmt = conn.prepareStatement(sPrepStmt);

      int len = bytearr.length;
      InputStream istr = new ByteArrayInputStream(bytearr);

      pstmt.setBinaryStream(1, istr, len);
      pstmt.executeUpdate();

      String longvarbinary_val_query = sqlp
          .getProperty("Longvarbinary_Query_Val", "");
      msg.setMsg(longvarbinary_val_query);
      rs = stmt.executeQuery(longvarbinary_val_query);
      rs.next();

      retByteArr = (byte[]) rs.getObject(1);

      for (int i = 0; i < bytearrsize; i++) {
        msg.addOutputMsg(Byte.toString(bytearr[i]),
            Byte.toString(retByteArr[i]));
        if (retByteArr[i] != bytearr[i]) {
          msg.printTestError(
              "setBinaryStream Method does not set the designated parameter with the inputStream",
              "test setBinaryStream Failed");

        }
      }
      msg.setMsg(
          "setBinaryStream sets the designated parameter with the InputStream");
      msg.printTestMsg();
      msg.printOutputMsg();

    } catch (SQLException sqle)

    {
      msg.printSQLError(sqle, "Call to SetBinaryStream is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to SetBinaryStream is Failed!");
    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (pstmt != null) {
          pstmt.close();
          pstmt = null;
        }
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
        rsSch.dropTab("Longvarbinary_Tab", conn);

      } catch (Exception e) {
      }
    }

  }

  /*
   * @testName: testSetCharacterStream
   * 
   * @assertion_ids: JavaEE:SPEC:186; JDBC:JAVADOC:702; JDBC:JAVADOC:703;
   * JDBC:SPEC:9; JDBC:SPEC:26;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Get the InputStream object. Get a Reader object from this
   * InputStream. Excecute the method preparedStatement.setCharacterStream to
   * Update the Longvarchar_Tab_Name with the value extracted from Char_tab.
   * Query the Longvarchar_Tab in the database to retrieve the value that is
   * been set. Compare the byte array value that is inserted with the value
   * retrieved. These values should be equal.
   * 
   */

  public void testSetCharacterStream() throws Fault {
    {
      String sPrepStatement = null;
      String sCoffeeName = null;
      ResultSet rs = null;
      try {
        rsSch.createTab("Longvarchar_Tab", sqlp, conn);
        sCoffeeName = rsSch.extractVal("Char_Tab", 1, sqlp, conn);
        sCoffeeName = sCoffeeName.substring(1, sCoffeeName.length() - 1);
        sPrepStatement = sqlp.getProperty("Longvarchar_Tab_Name_Update", null);
        pstmt = conn.prepareStatement(sPrepStatement);
        msg.setMsg("After creating pstmt...");

        byte[] buf = sCoffeeName.getBytes();
        int len = buf.length;
        InputStream chstr = new ByteArrayInputStream(buf);
        Reader istr = new InputStreamReader(chstr);
        msg.setMsg("Execute pstmt.setCharacterStream");
        pstmt.setCharacterStream(1, istr, len);
        pstmt.executeUpdate();

        String longvarchar_query = sqlp.getProperty("Longvarchar_Query_Name",
            "");
        rs = stmt.executeQuery(longvarchar_query);
        rs.next();

        String retVal = (String) rs.getObject(1);
        msg.addOutputMsg(sCoffeeName, retVal);

        if (sCoffeeName.equalsIgnoreCase(retVal)) {
          msg.setMsg(
              "setObject Method sets the designated parameter with the desired value");

        } else {
          msg.printTestError(
              "setCharacterStream method does not set the designated parameter with the desired value",
              "Call to setCharacterStream failed");
        }
        msg.printTestMsg();
        msg.printOutputMsg();

      } catch (SQLException sqle)

      {
        msg.printSQLError(sqle, "Call to setCharacterStream is Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to setCharacterStream is Failed!");
      }

      finally {
        try {
          if (rs != null) {
            rs.close();
            rs = null;
          }
          if (pstmt != null) {
            pstmt.close();
            pstmt = null;
          }
          if (stmt != null) {
            stmt.close();
            stmt = null;
          }
          rsSch.dropTab("Longvarchar_Tab", conn);
        } catch (Exception e) {
        }
      }

    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      // Close the database
      rsSch.dbUnConnect(conn);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }
}
