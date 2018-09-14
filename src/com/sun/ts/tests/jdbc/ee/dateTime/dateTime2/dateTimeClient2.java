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
 * @(#)dateTimeClient2.java	1.18 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dateTime.dateTime2;

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
 * The dateTimeClient2 class tests methods of Time class using Sun's J2EE
 * Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class dateTimeClient2 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.dateTime.dateTime2";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements
  private String sqlStmt = null;

  private Properties sqlp = null;

  private TimeZone tz = null;

  private int valToGeneralize = 0;

  private Calendar cal = null;

  private String sInTimeVal = null;

  private Timestamp inTimeVal = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    dateTimeClient2 theTests = new dateTimeClient2();
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
   * @testName: testTime01
   * 
   * @assertion_ids: JDBC:JAVADOC:46;
   * 
   * @test_Strategy: Create a Time Object with a long value as an argument. Then
   * get the String representation of that Time object. Check whether it is same
   * as equivalent String Value in property file.
   */
  public void testTime01() throws Fault {
    String sTimeLongVal = null;
    String sTimeVal = null;
    String sTimeObjVal = null;
    java.sql.Time timeObj = null;
    boolean booRetVal;

    try {
      sTimeLongVal = sqlp.getProperty("DateTime_Long_Val1", "");
      msg.setMsg("Time Long Value " + sTimeLongVal);

      long timeLongVal = Long.parseLong(sTimeLongVal);
      msg.setMsg("Long Value " + timeLongVal);

      long generalisedVal = timeLongVal - valToGeneralize;
      sTimeVal = sqlp.getProperty("DateTime_Str_Val1", "");

      sTimeVal = sTimeVal.substring(sTimeVal.indexOf(' '),
          sTimeVal.indexOf('.'));
      msg.setMsg("The time string got from the properties file is" + sTimeVal);

      timeObj = new Time(generalisedVal);
      sTimeObjVal = timeObj.toString().trim();
      msg.setMsg("The time object's value is : " + sTimeObjVal);

      booRetVal = sTimeVal.trim().equals(sTimeObjVal);

      if (booRetVal) {
        msg.setMsg("Time Constructor constructs a Time object");
      } else {
        msg.printTestError("Time Constructor does not construct a Time object",
            "Call to Time Constructor is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to Time Constructor is Failed!");

    }
  }

  /*
   * @testName: testTime02
   * 
   * @assertion_ids: JDBC:JAVADOC:46;
   * 
   * @test_Strategy: Create a Time Object with a long value as an argument. Then
   * get the String representation of that Time object. Check whether it is same
   * as equivalent String Value in property file.
   */
  public void testTime02() throws Fault {
    String sTimeLongVal = null;
    String sTimeVal = null;
    String sTimeObjVal = null;
    java.sql.Time timeObj = null;
    boolean booRetVal;

    try {
      sTimeLongVal = sqlp.getProperty("DateTime_Long_Val2", "");
      msg.setMsg("Time Long Value " + sTimeLongVal);

      long timeLongVal = Long.parseLong(sTimeLongVal);
      msg.setMsg("Long Value " + timeLongVal);

      long generalisedVal = timeLongVal - valToGeneralize;
      sTimeVal = sqlp.getProperty("DateTime_Str_Val2", "");

      sTimeVal = sTimeVal.substring(sTimeVal.indexOf(' '),
          sTimeVal.indexOf('.'));
      msg.setMsg("String value of Time " + sTimeVal);

      timeObj = new Time(generalisedVal);
      sTimeObjVal = timeObj.toString().trim();
      msg.setMsg("The time object's value is : " + sTimeObjVal);

      booRetVal = sTimeVal.trim().equals(sTimeObjVal);
      if (booRetVal) {
        msg.setMsg("Time Constructor constructs a Time object");
      } else {
        msg.printTestError(
            "Time Constructor does not construct a expected Time object",
            "Call to Time Constructor is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to Time Constructor is Failed!");

    }
  }

  /*
   * @testName: testToString01
   * 
   * @assertion_ids: JDBC:JAVADOC:49; JDBC:JAVADOC:46;
   * 
   * @test_Strategy: Create a Time Object with a long value as an argument. Then
   * get the String representation of that Time object. using the toString()
   * method.Check whether it is same as equivalent String Value in property
   * file.
   */
  public void testToString01() throws Fault {
    String sTimeLongVal = null;
    String sTimeVal = null;
    String sTimeObjVal = null;
    java.sql.Time timeObj = null;
    boolean booRetVal;

    try {
      sTimeLongVal = sqlp.getProperty("DateTime_Long_Val1", "");
      msg.setMsg("Time Long Value " + sTimeLongVal);

      long timeLongVal = Long.parseLong(sTimeLongVal);
      msg.setMsg("Long Value " + timeLongVal);

      long generalisedVal = timeLongVal - valToGeneralize;
      sTimeVal = sqlp.getProperty("DateTime_Str_Val1", "");
      sTimeVal = sTimeVal.substring(sTimeVal.indexOf(' '),
          sTimeVal.indexOf('.'));
      msg.setMsg("String value of Time  " + sTimeVal);

      timeObj = new Time(generalisedVal);
      sTimeObjVal = timeObj.toString().trim();
      msg.setMsg("The time object's value is : " + sTimeObjVal);

      booRetVal = sTimeVal.trim().equals(sTimeObjVal);
      if (booRetVal) {
        msg.setMsg("toString method returns equivalent String object");
      } else {
        msg.printTestError(
            "toString method does not return equivalent String object",
            "Call to Time Constructor is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to Time Constructor is Failed!");

    }
  }

  /*
   * @testName: testToString02
   * 
   * @assertion_ids: JDBC:JAVADOC:49; JDBC:JAVADOC:46;
   * 
   * @test_Strategy: Create a Time Object with a long value as an argument. Then
   * get the String representation of that Time object. using the toString()
   * method.Check whether it is same as equivalent String Value in property
   * file.
   */
  public void testToString02() throws Fault {
    String sTimeLongVal = null;
    String sTimeVal = null;
    String sTimeObjVal = null;
    java.sql.Time timeObj = null;
    boolean booRetVal;

    try {
      sTimeLongVal = sqlp.getProperty("DateTime_Long_Val2", "");
      msg.setMsg("Time Long Value " + sTimeLongVal);

      long timeLongVal = Long.parseLong(sTimeLongVal);
      msg.setMsg("Long Value " + timeLongVal);

      long generalisedVal = timeLongVal - valToGeneralize;

      sTimeVal = sqlp.getProperty("DateTime_Str_Val2", "");

      sTimeVal = sTimeVal.substring(sTimeVal.indexOf(' '),
          sTimeVal.indexOf('.'));
      msg.setMsg("String value of Time  " + sTimeVal);

      timeObj = new Time(generalisedVal);
      sTimeObjVal = timeObj.toString().trim();
      msg.setMsg("The time object's value is : " + sTimeObjVal);

      booRetVal = sTimeVal.trim().equals(sTimeObjVal);
      if (booRetVal) {
        msg.setMsg("toString method returns equivalent String object");
      } else {
        msg.printTestError(
            "toString method does not return equivalent String object",
            "Call to toString is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to toString is Failed!");

    }
  }

  /*
   * @testName: testValueOf01
   * 
   * @assertion_ids: JDBC:JAVADOC:48; JDBC:JAVADOC:46;
   * 
   * @test_Strategy: Call valueof(String ts) static method in java.sql.Time
   * class with a String argument to get a Time object Check whether it is same
   * as Time object obtained from equivalent long value .
   * 
   */
  public void testValueOf01() throws Fault {
    String sTimeLongVal = null;
    String sTimeVal = null;
    java.sql.Time timeObj = null;
    java.sql.Time retTimeObj = null;
    String sTimeObjVal = null;
    String sRetTimeObjVal = null;
    boolean booRetVal;

    try {
      sTimeLongVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long timeLongVal = Long.parseLong(sTimeLongVal);
      long generalisedVal = timeLongVal - valToGeneralize;

      sTimeVal = sqlp.getProperty("DateTime_Str_Val1", "");
      sTimeVal = sTimeVal.substring(sTimeVal.indexOf(' '),
          sTimeVal.indexOf('.'));
      msg.setMsg("String value of Time" + sTimeVal);

      timeObj = new Time(generalisedVal);
      sTimeObjVal = timeObj.toString().trim();
      msg.setMsg("The timeObj object's value is :" + sTimeObjVal);

      retTimeObj = java.sql.Time.valueOf(sTimeObjVal);
      sRetTimeObjVal = retTimeObj.toString();
      msg.setMsg("The retTimeObj object's value is :" + sRetTimeObjVal);

      booRetVal = sTimeObjVal.equals(sRetTimeObjVal);
      if (booRetVal) {
        msg.setMsg("valueOf method returns the equivalent Time object");
      } else {
        msg.printTestError(
            "valueOf method does not return the equivalent Time object",
            "Call to valueOf is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to valueOf is Failed!");

    }
  }

  /*
   * @testName: testValueOf02
   * 
   * @assertion_ids: JDBC:JAVADOC:48; JDBC:JAVADOC:46;
   * 
   * @test_Strategy: Call valueof(String ts) static method in java.sql.Time
   * class with a String argument to get a Time object Check whether it is same
   * as Time object obtained from equivalent long value .
   */
  public void testValueOf02() throws Fault {
    String sTimeLongVal = null;
    String sTimeVal = null;
    java.sql.Time timeObj = null;
    java.sql.Time retTimeObj = null;
    String sTimeObjVal = null;
    String sRetTimeObjVal = null;
    boolean booRetVal;

    try {
      sTimeLongVal = sqlp.getProperty("DateTime_Long_Val2", "");
      long timeLongVal = Long.parseLong(sTimeLongVal);
      long generalisedVal = timeLongVal - valToGeneralize;

      sTimeVal = sqlp.getProperty("DateTime_Str_Val2", "");
      sTimeVal = sTimeVal.substring(sTimeVal.indexOf(' '),
          sTimeVal.indexOf('.'));
      msg.setMsg("String value of Time  " + sTimeVal);

      timeObj = new Time(generalisedVal);
      sTimeObjVal = timeObj.toString().trim();
      msg.setMsg("The timeObj object's value is :" + sTimeObjVal);

      retTimeObj = java.sql.Time.valueOf(sTimeObjVal);
      sRetTimeObjVal = retTimeObj.toString();
      msg.setMsg("The retTimeObj object's value is :" + sRetTimeObjVal);

      booRetVal = sTimeObjVal.equals(sRetTimeObjVal);
      if (booRetVal) {
        msg.setMsg("valueOf method returns the equivalent Time object");
      } else {
        msg.printTestError(
            "valueOf method does not return the equivalent Time object",
            "Call to valueOf is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to valueOf is Failed!");

    }
  }

  /*
   * @testName: testSetTime01
   * 
   * @assertion_ids: JDBC:JAVADOC:47; JDBC:JAVADOC:46;
   * 
   * @test_Strategy: Create two Time objects with two different long values. Set
   * the same long value in the second object as used in the first object using
   * setTime(long) method Check whether both the Time objects are equal using
   * equals method
   */
  public void testSetTime01() throws Fault {
    String sTimeLongVal1 = null;
    String sTimeLongVal2 = null;
    String sTimeVal1 = null;
    String sTimeVal2 = null;
    java.sql.Time timeObj1 = null;
    java.sql.Time timeObj2 = null;
    String sTimeObjVal1 = null;
    String sTimeObjVal2 = null;
    boolean booRetVal;

    try {
      sTimeLongVal1 = sqlp.getProperty("DateTime_Long_Val1", "");
      long timeLongVal1 = Long.parseLong(sTimeLongVal1);
      long generalisedVal1 = timeLongVal1 - valToGeneralize;

      sTimeVal1 = sqlp.getProperty("DateTime_Str_Val1", "");
      sTimeVal1 = sTimeVal1.substring(sTimeVal1.indexOf(' '),
          sTimeVal1.indexOf('.'));
      msg.setMsg("String Value of Time1  " + sTimeVal1);

      sTimeLongVal2 = sqlp.getProperty("DateTime_Long_Val2", "");
      long timeLongVal2 = Long.parseLong(sTimeLongVal2);
      long generalisedVal2 = timeLongVal2 - valToGeneralize;

      sTimeVal2 = sqlp.getProperty("DateTime_Str_Val2", "");
      sTimeVal2 = sTimeVal2.substring(sTimeVal2.indexOf(' '),
          sTimeVal2.indexOf('.'));
      msg.setMsg("String Value of Time2" + sTimeVal2);

      timeObj1 = new Time(generalisedVal1);
      sTimeObjVal1 = timeObj1.toString().trim();
      msg.setMsg("The timeObj1 object's value is :" + sTimeObjVal1);

      timeObj2 = new Time(generalisedVal2);
      sTimeObjVal2 = timeObj2.toString().trim();
      msg.setMsg(
          "The timeObj2 object's value before setting is :" + sTimeObjVal2);

      timeObj2.setTime(generalisedVal1);
      sTimeObjVal2 = timeObj2.toString().trim();
      msg.setMsg("The retTimeObj object's value is after  setting time:"
          + sTimeObjVal2);

      booRetVal = sTimeObjVal1.equals(sTimeObjVal2);
      if (booRetVal) {
        msg.setMsg("setTime() method sets the Time value");
      } else {
        msg.printTestError("setTime method does not set the Time value",
            "call to setTime is Failed");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "call to setTime is Failed");

    }
  }

  /*
   * @testName: testSetTime02
   * 
   * @assertion_ids: JDBC:JAVADOC:47; JDBC:JAVADOC:46;
   * 
   * @test_Strategy: Create two Time objects with two different long values. Set
   * the same long value in the second object as used in the first object using
   * setTime(long) method Check whether both the Time objects are equal using
   * equals method
   */
  public void testSetTime02() throws Fault {
    String sTimeLongVal1 = null;
    String sTimeLongVal2 = null;
    String sTimeVal1 = null;
    String sTimeVal2 = null;
    java.sql.Time timeObj1 = null;
    java.sql.Time timeObj2 = null;
    String sTimeObjVal1 = null;
    String sTimeObjVal2 = null;
    String sTestTimeObjectVal2 = null;
    boolean booRetVal;
    try {
      sTimeLongVal1 = sqlp.getProperty("DateTime_Long_Val1", "");
      long timeLongVal1 = Long.parseLong(sTimeLongVal1);
      long generalisedVal1 = timeLongVal1 - valToGeneralize;

      sTimeVal1 = sqlp.getProperty("DateTime_Str_Val1", "");
      sTimeVal1 = sTimeVal1.substring(sTimeVal1.indexOf(' '),
          sTimeVal1.indexOf('.'));
      msg.setMsg("The time string got from the properties file is" + sTimeVal1);

      sTimeLongVal2 = sqlp.getProperty("DateTime_Long_Val2", "");
      long timeLongVal2 = Long.parseLong(sTimeLongVal2);
      long generalisedVal2 = timeLongVal2 - valToGeneralize;

      sTimeVal2 = sqlp.getProperty("DateTime_Str_Val2", "");
      sTimeVal2 = sTimeVal2.substring(sTimeVal2.indexOf(' '),
          sTimeVal2.indexOf('.'));
      msg.setMsg("String value of Time1 " + sTimeVal2);

      timeObj1 = new Time(generalisedVal2);
      sTimeObjVal1 = timeObj1.toString().trim();
      msg.setMsg("The timeObj1 object's value is :" + sTimeObjVal1);

      timeObj2 = new Time(generalisedVal1);
      sTimeObjVal2 = timeObj2.toString().trim();
      msg.setMsg(
          "The timeObj2 object's value before setting is :" + sTimeObjVal2);

      timeObj2.setTime(generalisedVal2);
      sTestTimeObjectVal2 = timeObj2.toString().trim();
      msg.setMsg("The retTimeObj object's value is after  setting time:"
          + sTestTimeObjectVal2);

      booRetVal = sTimeObjVal1.equals(sTestTimeObjectVal2);
      if (booRetVal)
        msg.setMsg("setTime method sets the Time value ");
      else {
        msg.printTestError("setTime method does not set the Time value ",
            "call to setTime is Failed");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "call to setTime is Failed");

    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    try {
      logMsg("Cleanup ok;");
    } catch (Exception e) {
      logErr("An error occurred in Cleanup method", e);
    }
  }
}
