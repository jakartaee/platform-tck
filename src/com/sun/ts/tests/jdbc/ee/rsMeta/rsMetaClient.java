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
 * @(#)rsMetaClient.java	1.25 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.rsMeta;

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
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The rsMetaClient class tests methods of ResultSetMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class rsMetaClient extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.rsMeta";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection coffeeCon = null;

  private DataSource ds1 = null;

  private dbSchema dbSch = null;

  private String drManager = null;

  private String query = null;

  private ResultSet rs = null;

  private ResultSetMetaData rsmd = null;

  private Statement stmt = null;

  private JDBCTestMsg msg = null;
  /* private String sqlStmt=null; */

  /* Run test in standalone mode */
  public static void main(String[] args) {
    rsMetaClient theTests = new rsMetaClient();
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
        /*
         * sqlStmt= p.getProperty("rsQuery",""); InputStream istr= new
         * FileInputStream(sqlStmt); Properties sqlp=new Properties();
         * sqlp.load(istr); query=sqlp.getProperty("selectCoffee","");
         */

        String fTableName = p.getProperty("ftable", "");
        query = "SELECT COF_NAME, PRICE FROM " + fTableName;

        if (query.length() == 0)
          throw new Fault("Invalid SQL Statement ");
        logTrace("SQL Statement : " + query);

        drManager = p.getProperty("DriverManager", "");
        if (drManager.length() == 0)
          throw new Fault("Invalid DriverManager Name");

        if (drManager.equals("yes")) {
          logTrace("Using DriverManager");
          DriverManagerConnection dmCon = new DriverManagerConnection();
          coffeeCon = dmCon.getConnection(p);
          dbSch = new dbSchema();
          dbSch.createData(p, coffeeCon);
          // Since scrollable resultSet is optional, the parameters are
          // commented out.
          stmt = coffeeCon.createStatement(/*
                                            * ResultSet.TYPE_SCROLL_INSENSITIVE,
                                            * ResultSet.CONCUR_READ_ONLY
                                            */);
          rs = stmt.executeQuery(query);
          rsmd = rs.getMetaData();
        } else {
          logTrace("Using DataSource");
          DataSourceConnection dsCon = new DataSourceConnection();
          coffeeCon = dsCon.getConnection(p);
          dbSch = new dbSchema();
          dbSch.createData(p, coffeeCon);
          // Since scrollable resultSet is optional, the parameters are
          // commented out.
          stmt = coffeeCon.createStatement(/*
                                            * ResultSet.TYPE_SCROLL_INSENSITIVE,
                                            * ResultSet.CONCUR_READ_ONLY
                                            */);
          rs = stmt.executeQuery(query);
          rsmd = rs.getMetaData();

        }
        msg = new JDBCTestMsg();

      } catch (SQLException ex) {
        logErr("SQL Exception : " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!");
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: testGetColumnCount
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:314; JDBC:JAVADOC:315;
   * 
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the
   * getColumnCount() method on the ResultSetMetaData object.It should return an
   * integer value greater than or equal to zero.
   */
  public void testGetColumnCount() throws Fault {
    try {
      // invoke on the getColumnCount
      msg.setMsg("Calling getColumnCount on ResultSetMetaData");
      int coloumnCount = rsmd.getColumnCount();
      if (coloumnCount >= 0) {
        msg.setMsg("getColumnCount method returns: " + coloumnCount);
      } else {
        msg.printTestError(" getColumnCount method returns a negative value",
            "Call to getColumnCount is Failed!");

      }
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getColumnCount is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getColumnCount is Failed!");

    }
  }

  /*
   * @testName: testIsAutoIncrement
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:316; JDBC:JAVADOC:317;
   *
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the
   * isAutoIncrement(int column) method.It should return a boolean value.
   */
  public void testIsAutoIncrement() throws Fault {
    try {
      // invoke on the isAutoIncrement(int column)
      msg.setMsg("Calling isAutoIncrement on ResultSetMetaData");
      boolean retValue = rsmd.isAutoIncrement(1);
      if (retValue) {
        msg.setMsg(
            "isAutoIncrement method returns column is automatically numbered");
      } else {
        msg.setMsg(
            "isAutoIncrement method returns column cannot be automatically numbered");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to isAutoIncrement is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to isAutoIncrement is Failed!");

    }
  }

  /*
   * /* @testName: testIsCaseSensitive
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:318; JDBC:JAVADOC:319;
   * 
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the
   * isCaseSensitive(int column) method.It should return a boolean value.
   */
  public void testIsCaseSensitive() throws Fault {
    try {
      // invoke on the isCaseSensitive(int column)
      msg.setMsg("Calling isCaseSensitive on ResultSetMetaData");
      boolean retValue = rsmd.isCaseSensitive(1);
      if (retValue) {
        msg.setMsg(
            "isCaseSensitive method returns column's are case sensitive");
      } else {
        msg.setMsg(
            "isCaseSensitive method returns column's are case insensitive");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to isCaseSensitive is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to isCaseSensitive is Failed!");

    }
  }

  /*
   * @testName: testIsSearchable
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:320; JDBC:JAVADOC:321;
   * 
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the
   * isSearchable(int column) method.It should return a boolean value.
   */

  public void testIsSearchable() throws Fault {
    try {
      // invoke on the isSearchable(int column)
      msg.setMsg("Calling isSearchable on ResultSetMetaData");
      boolean retValue = rsmd.isSearchable(1);
      if (retValue) {
        msg.setMsg(
            "isSearchable method returns column can be used in a where clause");
      } else {
        msg.setMsg(
            "isSearchable method returns column cannot be used in a where clause");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to isSearchable is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to isSearchable is Failed!");

    }
  }

  /*
   * @testName: testIsCurrency
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:322; JDBC:JAVADOC:323;
   *
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the
   * isCurrency(int column) method.It should return a boolean value.
   */
  public void testIsCurrency() throws Fault {
    try {
      // invoke on the isCurrency(int column)
      msg.setMsg("Calling IsCurrency on ResultSetMetaData");
      boolean retValue = rsmd.isCurrency(2);
      if (retValue) {
        msg.setMsg("isCurrency method returns column is a cash value");
      } else {
        msg.setMsg(
            "isCurrency method returns column does not contains a cash value");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to isCurrency is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to isCurrency is Failed!");

    }
  }

  /*
   * @testName: testIsNullable
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:324; JDBC:JAVADOC:325;
   * JDBC:JAVADOC:311; JDBC:JAVADOC:312; JDBC:JAVADOC:313;
   * 
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the
   * isNullable(int column) method.It should return an integer value which is
   * one of the constants columnNoNulls(0),columnNullable(1) and
   * columnNullableUnknown(2).
   */
  public void testIsNullable() throws Fault {
    try {
      // invoke on the isNullable(int column)
      msg.setMsg("Calling isNullable on ResultSetMetaData");
      int coloumnCount = rsmd.isNullable(2);
      if ((coloumnCount == ResultSetMetaData.columnNoNulls)
          || (coloumnCount == ResultSetMetaData.columnNullable)
          || (coloumnCount == ResultSetMetaData.columnNullableUnknown)) {
        msg.setMsg("isNullable method returns: " + coloumnCount);
      } else {
        msg.printTestError(" isNullable method returns a negative value",
            "Call to isNullable is Failed!");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to isNullable is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to isNullable is Failed!");

    }
  }

  /*
   * @testName: testIsSigned
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:326; JDBC:JAVADOC:327;
   * 
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the isSigned(int
   * column) method.It should return a boolean value.
   */
  public void testIsSigned() throws Fault {
    try {
      // invoke on the isSigned(int column)
      msg.setMsg("Calling isSigned on ResultSetMetaData");
      boolean retValue = rsmd.isSigned(2);
      if (retValue) {
        msg.setMsg(
            "isSigned method returns values in the column are signed numbers");
      } else {
        msg.setMsg(
            "isSigned method returns values in the column are unsigned numbers");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to isSigned is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to isSigned is Failed!");

    }
  }

  /*
   * @testName: testGetColumnDisplaySize
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:328; JDBC:JAVADOC:329;
   *
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the
   * getColumnDisplaySize(int colindex) method.It should return an integer
   * representing the normal maximum width in characters for column colindex.
   */
  public void testGetColumnDisplaySize() throws Fault {
    try {
      // invoke on the getColumnDisplaySize(int column)
      msg.setMsg("Calling getColumnDisplaySize on ResultSetMetaData");
      int colDispSize = rsmd.getColumnDisplaySize(2);
      if (colDispSize >= 0) {
        msg.setMsg("getColumnDisplaySize method returns: " + colDispSize);
      } else {
        msg.printTestError(
            " getColumnDisplaySize method returns a negative value",
            "Call to getColumnDisplaySize is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getColumnDisplaySize is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getColumnDisplaySize is Failed!");

    }
  }

  /*
   * @testName: testGetColumnLabel
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:330; JDBC:JAVADOC:331;
   *
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the
   * getColumnLabel(int colindex) method.It should return a String object.
   */
  public void testGetColumnLabel() throws Fault {
    try {
      // invoke on the getColumnLabel(int column)
      msg.setMsg("Calling getColumnLabel on ResultSetMetadata");
      String sRetValue = rsmd.getColumnLabel(2);
      if (sRetValue == null) {
        msg.setMsg(
            "getColumnLabel method does not returns the suggested column title");
      } else {
        msg.setMsg("getColumnLabel method returns:  " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getColumnLabel is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getColumnLabel is Failed!");

    }
  }

  /*
   * @testName: testGetColumnName
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:332; JDBC:JAVADOC:333;
   *
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the
   * getColumnName(int colindex) method.It should return a String object.
   */
  public void testGetColumnName() throws Fault {
    try {
      // invoke on the getColumnName(int column)
      msg.setMsg("Calling getColumnName on ResultSetMetadata");
      String sRetValue = rsmd.getColumnName(2);
      if (sRetValue == null) {
        msg.setMsg("getColumnName method does not returns the column name");
      } else {
        msg.setMsg("getColumnName method returns:  " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getColumnName is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getColumnName is Failed!");

    }
  }

  /*
   * @testName: testGetSchemaName
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:334; JDBC:JAVADOC:335;
   *
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the
   * getSchemaName(int colindex) method.It should return a String object.
   */
  public void testGetSchemaName() throws Fault {
    try {
      // invoke on the getSchemaName(int column)
      msg.setMsg("Calling getSchemaName on ResultSetMetadata");
      String sRetValue = rsmd.getSchemaName(2);
      if (sRetValue == null) {
        msg.setMsg("getSchemaName method does not returns the schema name ");
      } else {
        msg.setMsg("getSchemaName returns:  " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getColumnName is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getColumnName is Failed!");

    }
  }

  /*
   * @testName: testGetPrecision
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:336; JDBC:JAVADOC:337;
   *
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the
   * getPrecision(int colindex) method.It should return an integer greater than
   * or equal to zero.
   */
  public void testGetPrecision() throws Fault {
    try {
      // invoke on the getPrecision(int column)
      msg.setMsg("Calling getPrecision on ResultSetMetaData");
      int precisionSize = rsmd.getPrecision(1);
      if (precisionSize >= 0) {
        msg.setMsg("getPrecision method returns: " + precisionSize);
      } else {
        msg.printTestError(" getPrecision method returns a negative value",
            "Call to getPrecision is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getPrecision is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getPrecision is Failed!");

    }
  }

  /*
   * @testName: testGetScale
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:338; JDBC:JAVADOC:339;
   *
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the getScale(int
   * colindex) method.It should return an integer greater than or equal to zero.
   */
  public void testGetScale() throws Fault {
    try {
      // invoke on the getScale(int column)
      msg.setMsg("Calling getScale on ResultSetMetaData");
      int scaleSize = rsmd.getScale(2);
      if (scaleSize >= 0) {
        msg.setMsg("getScale method returns: " + scaleSize);
      } else {
        msg.printTestError(" getScale method returns a negative value",
            "Call to getScale is Failed!");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getScale is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getScale is Failed!");

    }
  }

  /*
   * @testName: testGetTableName
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:340; JDBC:JAVADOC:341;
   * 
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the
   * getTableName(int colindex) method.It should return a String object.
   */
  public void testGetTableName() throws Fault {
    try {
      // invoke on the getTableName(int column)
      msg.setMsg("Calling getTableName on ResultSetMetadata");
      String sRetValue = rsmd.getTableName(1);
      if (sRetValue == null) {
        msg.setMsg(
            "getTableName method does not returns the column's table name");
      } else {
        msg.setMsg("getTableName method returns:  " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getTableName is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getTableName is Failed!");

    }
  }

  /*
   * @testName: testGetCatalogName
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:342; JDBC:JAVADOC:343;
   * 
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the
   * getCatalogName(int colindex) method.It should return a String object.
   */
  public void testGetCatalogName() throws Fault {
    try {
      // invoke on the getCatalogName(int column)
      msg.setMsg("Calling getCatalogName on ResultSetMetadata");
      String sRetValue = rsmd.getCatalogName(1);
      if (sRetValue == null) {
        msg.setMsg(
            "getCatalogName method does not returns the column's table's catalog name");
      } else {
        msg.setMsg("getCatalogName method returns:  " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getCatalogName is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getCatalogName is Failed!");

    }
  }

  /*
   * @testName: testGetColumnType
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:344; JDBC:JAVADOC:345;
   *
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the
   * getColumnType(int colindex) method.Check if an integer value is returned.
   */
  public void testGetColumnType() throws Fault {
    try {
      // invoke on the getColumnType(int column)
      msg.setMsg("Calling getColumnType on ResultSetMetaData");
      int colType = rsmd.getColumnType(1);

      switch (colType) {
      case Types.BIT:
      case Types.TINYINT:
      case Types.SMALLINT:
      case Types.INTEGER:
      case Types.BIGINT:
      case Types.FLOAT:
      case Types.REAL:
      case Types.DOUBLE:
      case Types.NUMERIC:
      case Types.DECIMAL:
      case Types.CHAR:
      case Types.VARCHAR:
      case Types.LONGVARCHAR:
      case Types.DATE:
      case Types.TIME:
      case Types.TIMESTAMP:
      case Types.BINARY:
      case Types.VARBINARY:
      case Types.LONGVARBINARY:
      case Types.NULL:
      case Types.OTHER:
      case Types.JAVA_OBJECT:
      case Types.DISTINCT:
      case Types.STRUCT:
      case Types.ARRAY:
      case Types.BLOB:
      case Types.CLOB:
      case Types.REF:
        msg.setMsg("getColumnType method returns: " + colType);
        break;
      default:
        msg.printTestError(" getColumnType method returns a illegal value",
            "Call to getColumnTypeName failed !");

      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getColumnTypeName failed !");

    } catch (Exception e) {
      msg.printError(e, "Call to getColumnTypeName failed !");

    }
  }

  /*
   * @testName: testGetColumnTypeName
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:346; JDBC:JAVADOC:347;
   *
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the
   * getColumnTypeName(int colindex) method.It should return a String object.
   */
  public void testGetColumnTypeName() throws Fault {
    try {
      // invoke on the getColumnTypeName(int column)
      msg.setMsg("Calling getColumnTypeName on ResultSetMetadata");
      String sRetValue = rsmd.getColumnTypeName(1);
      if (sRetValue == null) {
        msg.setMsg(
            "getColumnTypeName method does not returns the type name used by the database ");
      } else {
        msg.setMsg("getColumnTypeName method returns:  " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getColumnTypeName is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getColumnTypeName is Failed!");

    }
  }

  /*
   * /*
   * 
   * @testName: testIsReadOnly
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:348; JDBC:JAVADOC:349;
   * 
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the
   * isReadOnly(int column) method.It should return a boolean value.
   */
  public void testIsReadOnly() throws Fault {
    try {
      // invoke on the isReadOnly(int column)
      msg.setMsg("Calling isReadOnly on ResultSetMetaData");
      boolean retValue = rsmd.isReadOnly(1);
      if (retValue) {
        msg.setMsg("isReadOnly method returns column cannot be writable");
      } else {
        msg.setMsg("isReadOnly method returns column can be writable");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to isReadOnly is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to isReadOnly is Failed!");

    }
  }

  /*
   * @testName: testIsWritable
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:350; JDBC:JAVADOC:351;
   * 
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the
   * isWritable(int column) method.It should return a boolean value.
   */
  public void testIsWritable() throws Fault {
    try {
      // invoke on the isWritable(int column)
      msg.setMsg("Calling isWritable on ResultSetMetaData");
      boolean retValue = rsmd.isWritable(1);
      if (retValue) {
        msg.setMsg("isWritable method returns column is writable");
      } else {
        msg.setMsg("isWritable method returns column cannot be writable");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to isWritable is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to isWritable is Failed!");

    }
  }

  /*
   * /*
   * 
   * @testName: testIsDefinitelyWritable
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:352; JDBC:JAVADOC:353;
   * 
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the
   * isDefinitelyWritable(int column) method.It should return a boolean value.
   */
  public void testIsDefinitelyWritable() throws Fault {
    try {
      // invoke on the isDefinitelyWritable(int column)
      msg.setMsg("Calling isDefinitelyWritable on ResultSetMetaData");
      boolean retValue = rsmd.isDefinitelyWritable(1);
      if (retValue) {
        msg.setMsg(
            "isDefinitelyWritable method returns write on the column is definitely succeed");
      } else {
        msg.setMsg(
            "isDefinitelyWritable method returns write on the column is definitely failed");
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to isDefinitelyWritable is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to isDefinitelyWritable is Failed!");

    }
  }

  /*
   * @testName: testGetColumnClassName
   * 
   * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:354; JDBC:JAVADOC:355;
   *
   * @test_Strategy: Get the ResultSetMetaData object from the corresponding
   * ResultSet by using the ResultSet's getMetaData method.Call the
   * getColumnClassName(int colindex) method.It should return a String object.
   */
  public void testGetColumnClassName() throws Fault {
    try {
      // invoke on the getColumnClassName(int column)
      msg.setMsg("Calling getColumnClassName on ResultSetMetadata");
      String sRetValue = rsmd.getColumnClassName(1);
      if (sRetValue == null) {
        msg.setMsg(
            "getColumnClassName method does not returns the fully-qualified name of the class");
      } else {
        msg.setMsg("getColumnClassName method returns:  " + sRetValue);
      }
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to getColumnClassName is Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to getColumnClassName is Failed!");

    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      stmt.close();
      // Close the database
      dbSch.destroyData(coffeeCon);
      dbSch.dbUnConnect(coffeeCon);

      /*
       * if(coffeeCon == null) msg.setMsg("coffeeCon returns null value"); else
       * coffeeCon.close();
       */

      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }
}
