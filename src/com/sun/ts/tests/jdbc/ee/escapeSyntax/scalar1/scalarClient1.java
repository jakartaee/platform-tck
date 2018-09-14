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
 * @(#)scalarClient1.java	1.20 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.escapeSyntax.scalar1;

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
 * The scalarClient1 class tests methods of scalar Functions interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class scalarClient1 extends ServiceEETest {
  private static final String testName = "jdbc.ee.escapeSyntax";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements

  private transient Connection conn = null;

  private Statement stmt = null;

  private ResultSet rs = null;

  private String drManager = null;

  private fnSchema fnSch = null;

  private Properties props = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    scalarClient1 theTests = new scalarClient1();
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
        props = p;
        drManager = p.getProperty("DriverManager", "");
        if (drManager.length() == 0)
          throw new Fault("Invalid DriverManager Name");

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
        fnSch = new fnSchema();
        fnSch.createTable(p, conn);
        msg = new JDBCTestMsg();

      } catch (SQLException ex) {
        logErr("SQL Exception : " + ex.getMessage(), ex);
      }
    } catch (Exception e) {
      logErr("Setup Failed!", e);
    }
  }

  /*
   * @testName: testConcat
   * 
   * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4; JDBC:SPEC:27;
   * 
   * @test_Strategy: Get a Statement object and call executeQuery(String)
   * method. The query contains the concat scalar function. It should return the
   * concatenated string.
   *
   */
  public void testConcat() throws Fault {
    try {

      String queryString = null;
      msg.setMsg("Query that contains a call to the function concat.");
      queryString = props.getProperty("Concat_Fn_Query", "");
      msg.setMsg(queryString);
      rs = stmt.executeQuery(queryString);
      rs.next();
      String retString = rs.getString(1);
      msg.setMsg("Concatenated String: " + retString);
      rs.close();
      msg.printTestMsg();
    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to testConcat Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to testConcat Failed!");

    }

  }

  /*
   * @testName: testAscii
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a statement object and call executeQuery method. The
   * query contains the ascii function call. It should return an integer.
   *
   *
   */
  public void testAscii() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isStringFuncFound("ASCII", conn) == true) {
        isFuncFound = true;
        msg.setMsg("String function ASCII is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("String function ASCII is not supported by this DBMS");
      }

    } catch (Exception e) {
      msg.printError(e, "Call to testAscii Failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function ascii
        queryString = props.getProperty("Ascii_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("The ASCII value of the leftmost character=" + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testAscii Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testAscii Failed!");

      }
    }
    msg.printTestMsg();

  }

  /*
   * @testName: testInsert
   * 
   * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function insert. It should return a
   * string.
   *
   */
  public void testInsert() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isStringFuncFound("INSERT", conn) == true) {
        isFuncFound = true;
        msg.setMsg("String function INSERT is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("String function INSERT is not supported by this DBMS");
      }

    } catch (Exception e) {
      msg.printError(e, "Call to testInsert Failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function insert
        queryString = props.getProperty("Insert_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("The string after insertion: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testInsert Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testInsert Failed!");

      }
    }
    msg.printTestMsg();

  }

  /*
   * @testName: testLcase
   * 
   * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
   * 
   * @test_Strategy: Get a Statment object and call the method executeQuery. The
   * query contains a call to the function lcase. It should return a string.
   *
   *
   */
  public void testLcase() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isStringFuncFound("LCASE", conn) == true) {
        isFuncFound = true;
        msg.setMsg("String function LCASE is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("String function LCASE is not supported by this DBMS");
      }

    } catch (Exception e) {
      msg.printError(e, "Call to testLcase Failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function lcase
        queryString = props.getProperty("Lcase_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Lowercase String: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testLcase Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testLcase Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testLeft
   * 
   * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function left. It should return a string.
   *
   *
   */
  public void testLeft() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isStringFuncFound("LEFT", conn) == true) {
        isFuncFound = true;
        msg.setMsg("String function LEFT is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("String function LEFT is not supported by this DBMS");
      }

    } catch (Exception e) {
      msg.printError(e, "Call to testLeft Failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function left
        queryString = props.getProperty("Left_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("The first 2 chars: <" + retString + ">");
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testLeft Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testLeft Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testLength
   *
   * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4; JDBC:SPEC:27;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function length. It should return a
   * number.
   *
   *
   */
  public void testLength() throws Fault {

    try {
      String queryString = null;
      msg.setMsg("Query that contains a call to the function length");
      queryString = props.getProperty("Length_Fn_Query", "");
      msg.setMsg(queryString);
      rs = stmt.executeQuery(queryString);
      rs.next();
      String retString = rs.getString(1);
      msg.setMsg("String length=" + retString);
      rs.close();
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to testLength Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to testLength Failed!");

    }
  }

  /*
   * @testName: testLocate01
   * 
   * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function locate. It should return an
   * integer.
   *
   */
  public void testLocate01() throws Fault {

    try {
      String queryString = null;
      // Query that contains a call to the function locate
      queryString = props.getProperty("Locate_Fn_Query", "");
      msg.setMsg(queryString);
      rs = stmt.executeQuery(queryString);
      rs.next();
      String retString = rs.getString(1);
      msg.setMsg("str1 starts at " + retString + " of str2");
      rs.close();
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to testLocate01 Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to testLocate01 Failed!");

    }

  }

  /*
   * @testName: testLtrim
   * 
   * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function ltrim. It should return a string.
   *
   */
  public void testLtrim() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isStringFuncFound("LTRIM", conn) == true) {

        isFuncFound = true;
        msg.setMsg("String function LTRIM is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("String function LTRIM is not supported by this DBMS");

      }

    } catch (Exception e) {
      msg.printError(e, "Call to testLtrim Failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function ltrim
        queryString = props.getProperty("Ltrim_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("String after Left Trim: <" + retString + ">");
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testLtrim Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testLtrim Failed!");

      }

    }
    msg.printTestMsg();
  }

  /*
   * @testName: testRepeat
   * 
   * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function repeat. It should return a
   * string.
   *
   */
  public void testRepeat() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isStringFuncFound("REPEAT", conn) == true) {
        isFuncFound = true;
        msg.setMsg("String function REPEAT is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("String function REPEAT is not supported by this DBMS");

      }

    } catch (Exception e) {
      msg.printError(e, "Call to testRepeat Failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function repeat
        queryString = props.getProperty("Repeat_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("The repeated string: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testRepeat Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testRepeat Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testRight
   * 
   * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function right. It should return a string.
   *
   */
  public void testRight() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isStringFuncFound("RIGHT", conn) == true) {
        isFuncFound = true;
        msg.setMsg("String function RIGHT is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("String function RIGHT is not supported by this DBMS");

      }
    } catch (Exception e) {
      msg.printError(e, "Call to testRight Failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function right
        queryString = props.getProperty("Right_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("The last 3 chars: <" + retString + ">");
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testRight Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testRight Failed!");

      }
    }
    msg.printTestMsg();

  }

  /*
   * @testName: testRtrim
   * 
   * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function rtrim. It should return a string.
   *
   *
   */
  public void testRtrim() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isStringFuncFound("RTRIM", conn) == true) {

        isFuncFound = true;
        msg.setMsg("String function RTRIM is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("String function RTRIM is not supported by this DBMS");

      }
    } catch (Exception e) {
      msg.printError(e, "Call to testRtrim Failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function rtrim
        queryString = props.getProperty("Rtrim_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("String after Right Trim: <" + retString + ">");
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testRtrim Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testRtrim Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testSoundex
   * 
   * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function soundex. It should return a
   * string.
   *
   */
  public void testSoundex() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isStringFuncFound("SOUNDEX", conn) == true) {
        isFuncFound = true;
        msg.setMsg("String function SOUNDEX is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("String function SOUNDEX is not supported by this DBMS");

      }
    } catch (Exception e) {
      msg.printError(e, "Call to testSoundex Failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function soundex
        queryString = props.getProperty("Soundex_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Sound of the word: <" + retString + ">");
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testSoundex Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testSoundex Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testSpace
   * 
   * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function space. It should return a string.
   *
   */
  public void testSpace() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isStringFuncFound("SPACE", conn) == true) {
        isFuncFound = true;
        msg.setMsg("String function SPACE is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("String function SPACE is not supported by this DBMS");

      }
    } catch (Exception e) {
      msg.printError(e, "Call to testSpace Failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function space
        queryString = props.getProperty("Space_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Space string: <" + retString + ">");
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testSpace Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testSpace Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testSubstring
   * 
   * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4; JDBC:SPEC:27;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function substring. It should return a
   * string.
   *
   */
  public void testSubstring() throws Fault {

    try {
      String queryString = null;
      // Query that contains a call to the function substring
      queryString = props.getProperty("Substring_Fn_Query", "");

      rs = stmt.executeQuery(queryString);
      rs.next();
      String retString = rs.getString(1);
      msg.setMsg("Sub string: <" + retString + ">");
      rs.close();
      msg.printTestMsg();

    } catch (SQLException sqle) {
      msg.printSQLError(sqle, "Call to testSubstring Failed!");

    } catch (Exception e) {
      msg.printError(e, "Call to testSubstring Failed!");

    }
  }

  /*
   * @testName: testUcase
   * 
   * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function ucase. It should return a string.
   *
   */
  public void testUcase() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isStringFuncFound("UCASE", conn) == true) {

        isFuncFound = true;
        msg.setMsg("String function UCASE is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("String function UCASE is not supported by this DBMS");

      }
    } catch (Exception e) {
      msg.printError(e, "Call to testUcase Failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function ucase
        queryString = props.getProperty("Ucase_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Uppercase String: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testUcase Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testUcase Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testChar
   * 
   * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function char. It should return a
   * character.
   *
   */
  public void testChar() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isStringFuncFound("CHAR", conn) == true) {

        isFuncFound = true;
        msg.setMsg("String function CHAR is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("String function CHAR is not supported by this DBMS");

      }
    } catch (Exception e) {
      msg.printError(e, "Call to testChar Failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function char
        queryString = props.getProperty("Char_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("Resultant Char is <" + retString + ">");
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testChar Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testChar Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testReplace
   * 
   * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function replace. It should return a
   * string.
   *
   */
  public void testReplace() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isStringFuncFound("REPLACE", conn) == true) {

        isFuncFound = true;
        msg.setMsg("String function REPLACE is supported by this DBMS");
      } else {

        isFuncFound = false;
        msg.setMsg("String function REPLACE is not supported by this DBMS");

      }
    } catch (Exception e) {
      msg.printError(e, "Call to testReplace Failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function replace
        queryString = props.getProperty("Replace_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("String after replacement: <" + retString + ">");
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testReplace Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testReplace Failed!");

      }
    }
    msg.printTestMsg();
  }

  /*
   * @testName: testUser
   * 
   * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function user. It should return a string.
   *
   */
  public void testUser() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isSystemFuncFound("USER", conn) == true) {
        isFuncFound = true;
        msg.setMsg("System function USER is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("System function USER is not supported by this DBMS");

      }
    } catch (Exception e) {
      msg.printError(e, "Call to testUser Failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function user
        queryString = props.getProperty("User_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("User Name: " + retString);
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testUser Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testUser Failed!");

      }
    }
    msg.printTestMsg();

  }

  /*
   * @testName: testIfNull
   * 
   * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function ifnull.
   *
   */
  public void testIfNull() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isSystemFuncFound("IFNULL", conn) == true) {

        isFuncFound = true;
        msg.setMsg("System function IFNULL is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("System function IFNULL is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testIfNull Failed!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function ifnull
        queryString = props.getProperty("Ifnull_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("<" + retString + ">");
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testIfNull Failed!");

      } catch (Exception e) {
        msg.printError(e, "Call to testIfNull Failed!");

      }

    }
    msg.printTestMsg();
  }

  /*
   * @testName: testSin
   * 
   * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function sin. It should return a numerical
   * value.
   */
  public void testSin() throws Fault {
    boolean isFuncFound = false;
    try {
      // Check if the function is supported by the DBMS
      if (fnSch.isNumericFuncFound("SIN", conn) == true) {
        isFuncFound = true;
        msg.setMsg("Numeric function SIN  is supported by this DBMS");
      } else {
        isFuncFound = false;
        msg.setMsg("Numeric function SIN is not supported by this DBMS");
      }
    } catch (Exception e) {
      msg.printError(e, "Call to testSin!");

    }
    if (isFuncFound == true) {
      try {
        String queryString = null;
        // Query that contains a call to the function sin
        queryString = props.getProperty("Sin_Fn_Query", "");

        rs = stmt.executeQuery(queryString);
        rs.next();
        String retString = rs.getString(1);
        msg.setMsg("<" + retString + ">");
        rs.close();

      } catch (SQLException sqle) {
        msg.printSQLError(sqle, "Call to testSin!");

      } catch (Exception e) {
        msg.printError(e, "Call to testSin!");

      }
    }
    msg.printTestMsg();
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      stmt.close();
      // Close the database
      fnSch.dropTable(props, conn);
      fnSch.dbUnConnect(conn);
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }

}
