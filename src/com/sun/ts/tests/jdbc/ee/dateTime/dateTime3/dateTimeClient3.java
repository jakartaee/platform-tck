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
 * @(#)dateTimeClient3.java	1.17 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dateTime.dateTime3;

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

/*
 * The dateTimeClient3 class tests methods of Timestamp class using 
 * Sun's J2EE Reference Implementation.
 * @author  
 * @version 1.7, 06/16/99
 */

public class dateTimeClient3 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.dateTime.dateTime3";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements
  private String drManager = null;

  private String sqlStmt = null;

  private Properties sqlp = null;

  private TimeZone tz = null;

  private int valToGeneralize = 0;

  private Calendar cal = null;

  private String sInTimeVal = null;

  private Timestamp inTimeVal = null;

  private String Reference_Value = null;

  private Timestamp inTimeVal1 = null;

  private Timestamp inTimeVal2 = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    dateTimeClient3 theTests = new dateTimeClient3();
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
      tz = TimeZone.getDefault();
      boolean b1 = tz.useDaylightTime();

      if (!b1) {
        cal = Calendar.getInstance(tz);
        valToGeneralize = tz.getOffset(cal.get(Calendar.ERA),
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.DAY_OF_WEEK),
            cal.get(Calendar.MILLISECOND));
      } else {
        valToGeneralize = tz.getRawOffset();
      }

      drManager = p.getProperty("DriverManager", "");
      if (drManager.length() == 0) {
        throw new Fault("Invalid DriverManager Name");
      }
      /*
       * sqlp=new Properties(); sqlStmt= p.getProperty("rsQuery","");
       * InputStream istr= new FileInputStream(sqlStmt); sqlp.load(istr);
       */
      sqlp = p;
      msg = new JDBCTestMsg();
    } catch (Exception e) {
      logErr("Setup Failed!", e);
    }
  }

  /*
   * @testName: testDate01
   * 
   * @assertion_ids: JDBC:JAVADOC:100
   * 
   * @test_Strategy: Create a Date Object with a long value as an argument. Then
   * get the String representation of that Date object. Check whether it is same
   * as equivalent String Value in property file.
   */

  public void testDate01() throws Fault {
    String sTimeLongVal = null;
    String sDateVal = null;
    String sDateObjVal = null;
    java.sql.Date dateObj = null;
    boolean booRetVal;
    try {
      sTimeLongVal = sqlp.getProperty("DateTime_Long_Val1", "");

      long timeLongVal = Long.parseLong(sTimeLongVal);
      msg.setMsg("Time Long Value " + timeLongVal);

      long generalisedVal = timeLongVal - valToGeneralize;
      sDateVal = sqlp.getProperty("DateTime_Str_Val1", "");

      sDateVal = sDateVal.substring(0, sDateVal.indexOf(' '));
      msg.setMsg("The date string got from the properties file is" + sDateVal);

      dateObj = new java.sql.Date(generalisedVal);
      sDateObjVal = dateObj.toString().trim();
      msg.setMsg("The date object's value is : " + sDateObjVal);

      booRetVal = sDateVal.trim().equals(sDateObjVal);
      if (booRetVal) {
        msg.setMsg("Date object created as expected");
      } else {
        msg.printTestError("Date object not created as expected",
            "Date object construction failed");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Date object construction failed");

    }
  }

  /*
   * @testName: testDate02
   * 
   * @assertion_ids: JDBC:JAVADOC:100
   * 
   * @test_Strategy: Create a Date Object with a long value as an argument. Then
   * get the String representation of that Date object. Check whether it is same
   * as equivalent String Value in property file.
   */
  public void testDate02() throws Fault {
    String sDateLongVal = null;
    String sDateVal = null;
    String sDateObjVal = null;
    java.sql.Date dateObj = null;
    boolean booRetVal;

    try {
      sDateLongVal = sqlp.getProperty("DateTime_Long_Val2", "");

      long dateLongVal = Long.parseLong(sDateLongVal);
      msg.setMsg("Date Long Value " + dateLongVal);

      long generalisedVal = dateLongVal - valToGeneralize;

      sDateVal = sqlp.getProperty("DateTime_Str_Val2", "");

      sDateVal = sDateVal.substring(0, sDateVal.indexOf(' '));
      msg.setMsg("The date string got from the properties file is" + sDateVal);

      dateObj = new java.sql.Date(generalisedVal);
      sDateObjVal = dateObj.toString().trim();
      msg.setMsg("The date object's value is : " + sDateObjVal);
      booRetVal = sDateVal.trim().equals(sDateObjVal);

      if (booRetVal) {
        msg.setMsg("Date object created as expected");
      } else {
        msg.printTestError("Date object not created as expected",
            "Date object construction failed");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Date object construction failed");

    }
  }

  /*
   * @testName: testToString01
   * 
   * @assertion_ids: JDBC:JAVADOC:103
   * 
   * @test_Strategy: Create a Date Object with a long value as an argument. Then
   * get the String representation of that Date object. using the toString()
   * method.Check whether it is same as equivalent String Value in property
   * file.
   */
  public void testToString01() throws Fault {
    String sDateLongVal = null;
    String sDate = null;
    String sDateVal = null;
    String sDateObjVal = null;
    java.sql.Date dateObj = null;
    boolean booRetVal;
    try {
      sDateLongVal = sqlp.getProperty("DateTime_Long_Val1", "");

      long dateLongVal = Long.parseLong(sDateLongVal);
      msg.setMsg("Date Long Value " + dateLongVal);

      long generalisedVal = dateLongVal - valToGeneralize;

      sDateVal = sqlp.getProperty("DateTime_Str_Val1", "");
      sDateVal = sDateVal.substring(0, sDateVal.indexOf(' '));
      msg.setMsg("The date string got from the properties file is" + sDateVal);

      dateObj = new java.sql.Date(generalisedVal);
      sDateObjVal = dateObj.toString().trim();
      msg.setMsg("The date object's value is : " + sDateObjVal);
      booRetVal = sDateVal.trim().equals(sDateObjVal);

      if (booRetVal) {
        msg.setMsg(
            "Date object created as expected and call to toString method passes");
      } else {
        msg.printTestError(
            "call to toString method doesnt returns the expected value",
            "call to toString() nmethod failed");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "call to toString() nmethod failed");

    }
  }

  /*
   * @testName: testToString02
   * 
   * @assertion_ids: JDBC:JAVADOC:103
   * 
   * @test_Strategy: Create a Date Object with a long value as an argument. Then
   * get the String representation of that Date object. using the toString()
   * method.Check whether it is same as equivalent String Value in property
   * file.
   */
  public void testToString02() throws Fault {
    String sDateLongVal = null;
    String sDateVal = null;
    String sDateObjVal = null;
    java.sql.Date dateObj = null;
    boolean booRetVal;
    try {
      sDateLongVal = sqlp.getProperty("DateTime_Long_Val2", "");

      long dateLongVal = Long.parseLong(sDateLongVal);
      msg.setMsg("Date Long Value " + dateLongVal);

      long generalisedVal = dateLongVal - valToGeneralize;
      sDateVal = sqlp.getProperty("DateTime_Str_Val2", "");

      sDateVal = sDateVal.substring(0, sDateVal.indexOf(' '));
      msg.setMsg("The date string got from the properties file is" + sDateVal);

      dateObj = new java.sql.Date(generalisedVal);
      sDateObjVal = dateObj.toString().trim();
      msg.setMsg("The date object's value is : " + sDateObjVal);

      booRetVal = sDateVal.trim().equals(sDateObjVal);
      if (booRetVal) {
        msg.setMsg(
            "Date object created as expected and call to toString method passes");
      } else {
        msg.printTestError(
            "call to toString method doesnt returns the expected value",
            "call to toString() nmethod failed");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "call to toString() nmethod failed");

    }
  }

  /*
   * @testName: testValueOf01
   * 
   * @assertion_ids: JDBC:JAVADOC:102
   * 
   * @test_Strategy: Call valueof(String ts) static method in java.sql.Date
   * class with a String argument to get a Date object Check whether it is same
   * as Date object obtained from equivalent long value .
   */
  public void testValueOf01() throws Fault {
    String sDateLongVal = null;
    String sDateVal = null;
    java.sql.Date dateObj = null;
    java.sql.Date retDateObj = null;
    String sDateObjectVal = null;
    String sRetDateObjectVal = null;
    boolean booRetVal;
    try {
      sDateLongVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long dateLongVal = Long.parseLong(sDateLongVal);
      long generalisedVal = dateLongVal - valToGeneralize;

      sDateVal = sqlp.getProperty("DateTime_Str_Val1", "");
      sDateVal = sDateVal.substring(0, sDateVal.indexOf('.'));
      msg.setMsg("The date string got from the properties file is" + sDateVal);

      dateObj = new java.sql.Date(generalisedVal);
      sDateObjectVal = dateObj.toString().trim();
      msg.setMsg("The dateObj object's value is :" + sDateObjectVal);

      retDateObj = java.sql.Date.valueOf(sDateObjectVal);
      sRetDateObjectVal = retDateObj.toString();
      msg.setMsg("The retDateObj object's value is :" + sRetDateObjectVal);
      booRetVal = sDateObjectVal.equals(sRetDateObjectVal);
      if (booRetVal) {
        msg.setMsg(
            "Date object created as expected cross checking with valuOf() method ");
      } else {
        msg.printTestError(
            "date object not created as expected cross checking with valuOf() method ",
            "call to static method valueOf failed");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "call to static method valueOf failed");

    }
  }

  /*
   * @testName: testValueOf02
   * 
   * @assertion_ids: JDBC:JAVADOC:102
   * 
   * @test_Strategy: Call valueof(String ts) static method in java.sql.Date
   * class with a String argument to get a Date object Check whether it is same
   * as Date object obtained from equivalent long value .
   */
  public void testValueOf02() throws Fault {
    String sDateLongVal = null;
    String sDateVal = null;
    java.sql.Date dateObj = null;
    java.sql.Date retDateObj = null;
    String sDateObjectVal = null;
    String sRetDateObjectVal = null;
    boolean booRetVal;

    try {
      sDateLongVal = sqlp.getProperty("DateTime_Long_Val2", "");
      long dateLongVal = Long.parseLong(sDateLongVal);
      long generalisedVal = dateLongVal - valToGeneralize;

      sDateVal = sqlp.getProperty("DateTime_Str_Val2", "");
      sDateVal = sDateVal.substring(sDateVal.indexOf(' '),
          sDateVal.indexOf('.'));
      msg.setMsg("The date string got from the properties file is" + sDateVal);

      dateObj = new java.sql.Date(generalisedVal);
      sDateObjectVal = dateObj.toString().trim();
      msg.setMsg("The dateObj object's value is :" + sDateObjectVal);

      retDateObj = java.sql.Date.valueOf(sDateObjectVal);
      sRetDateObjectVal = retDateObj.toString();
      msg.setMsg("The retDateObj object's value is :" + sRetDateObjectVal);

      booRetVal = sDateObjectVal.equals(sRetDateObjectVal);
      if (booRetVal) {
        msg.setMsg(
            "Date object created as expected cross checking with valuOf() method ");
      } else {
        msg.printTestError(
            "Date object not created as expected cross checking with valuOf() method ",
            "call to static method valueOf failed");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "call to static method valueOf failed");

    }
  }

  /*
   * @testName: testSetTime01
   * 
   * @assertion_ids: JDBC:JAVADOC:101
   * 
   * @test_Strategy: Create two Date objects with two different long values. Set
   * the same long value in the second object as used in the first object using
   * setTime(long) method Check whether both the Date objects are equal using
   * equals method
   */
  public void testSetTime01() throws Fault {
    String sDateLongVal1 = null;
    String sDateLongVal2 = null;
    String sDateVal1 = null;
    String sDateVal2 = null;
    java.sql.Date dateObj1 = null;
    java.sql.Date dateObj2 = null;
    String sDateObjVal1 = null;
    String sDateObjVal2 = null;
    String sTestDateObjectVal2 = null;
    boolean booRetVal;
    try {
      sDateLongVal1 = sqlp.getProperty("DateTime_Long_Val1", "");
      long dateLongVal1 = Long.parseLong(sDateLongVal1);
      long generalisedVal1 = dateLongVal1 - valToGeneralize;

      sDateVal1 = sqlp.getProperty("DateTime_Str_Val1", "");
      sDateVal1 = sDateVal1.substring(0, sDateVal1.indexOf(' '));
      msg.setMsg("The date string got from the properties file is" + sDateVal1);

      sDateLongVal2 = sqlp.getProperty("DateTime_Long_Val2", "");
      long dateLongVal2 = Long.parseLong(sDateLongVal2);
      long generalisedVal2 = dateLongVal2 - valToGeneralize;

      sDateVal2 = sqlp.getProperty("DateTime_Str_Val2", "");
      sDateVal2 = sDateVal1.substring(0, sDateVal2.indexOf(' '));
      msg.setMsg("The date string got from the properties file is" + sDateVal2);

      dateObj1 = new java.sql.Date(generalisedVal1);
      sDateObjVal1 = dateObj1.toString().trim();
      msg.setMsg("The dateObj1 object's value is :" + sDateObjVal1);

      dateObj2 = new java.sql.Date(generalisedVal2);
      sDateObjVal2 = dateObj2.toString().trim();
      msg.setMsg(
          "The dateObj2 object's value before setting is :" + sDateObjVal2);

      dateObj2.setTime(generalisedVal1);
      sTestDateObjectVal2 = dateObj2.toString().trim();
      msg.setMsg("The test date object's value after setting time:"
          + sTestDateObjectVal2);
      booRetVal = sDateObjVal1.equals(sTestDateObjectVal2);

      if (booRetVal) {
        msg.setMsg(
            "Date object created as expected cross checking with setTime() method ");
      } else {
        msg.printTestError(
            "Date object not created as expected cross checking with setTime() method ",
            "call to  method setTime() failed");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "call to  method setTime() failed");

    }
  }

  /*
   * @testName: testSetTime02
   * 
   * @assertion_ids: JDBC:JAVADOC:101
   * 
   * @test_Strategy: Create two Date objects with two different long values. Set
   * the same long value in the second object as used in the first object using
   * setTime(long) method Check whether both the Date objects are equal using
   * equals method
   */

  public void testSetTime02() throws Fault {
    String sDateLongVal1 = null;
    String sDateLongVal2 = null;
    String sDateVal1 = null;
    String sDateVal2 = null;
    java.sql.Date dateObj1 = null;
    java.sql.Date dateObj2 = null;
    String sDateObjVal1 = null;
    String sDateObjVal2 = null;
    String sTestDateObjectVal2 = null;
    boolean booRetVal;

    try {
      sDateLongVal1 = sqlp.getProperty("DateTime_Long_Val1", "");
      long dateLongVal1 = Long.parseLong(sDateLongVal1);
      long generalisedVal1 = dateLongVal1 - valToGeneralize;

      sDateVal1 = sqlp.getProperty("DateTime_Str_Val1", "");
      sDateVal1 = sDateVal1.substring(0, sDateVal1.indexOf(' '));
      msg.setMsg("The date string got from the properties file is" + sDateVal1);

      sDateLongVal2 = sqlp.getProperty("DateTime_Long_Val2", "");
      long dateLongVal2 = Long.parseLong(sDateLongVal2);
      long generalisedVal2 = dateLongVal2 - valToGeneralize;

      sDateVal2 = sqlp.getProperty("DateTime_Str_Val2", "");
      sDateVal2 = sDateVal1.substring(0, sDateVal2.indexOf(' '));
      msg.setMsg("The date string got from the properties file is" + sDateVal2);

      dateObj1 = new java.sql.Date(generalisedVal2);
      sDateObjVal1 = dateObj1.toString().trim();
      msg.setMsg("The dateObj1 object's value is :" + sDateObjVal1);

      dateObj2 = new java.sql.Date(generalisedVal1);
      sDateObjVal2 = dateObj2.toString().trim();
      msg.setMsg(
          "The dateObj2 object's value before setting is :" + sDateObjVal2);

      dateObj2.setTime(generalisedVal2);
      sTestDateObjectVal2 = dateObj2.toString().trim();
      msg.setMsg("The test date object's value after  setting time is:"
          + sTestDateObjectVal2);
      booRetVal = sDateObjVal1.equals(sTestDateObjectVal2);
      if (booRetVal) {
        msg.setMsg(
            "Date object created as expected cross checking with setTime() method ");
      } else {
        msg.printTestError(
            "Date object not created as expected cross checking with setTime() method ",
            "call to  method setTime() failed");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "call to  method setTime() failed");

    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred while closing the database connection", e);
    }
  }
}
