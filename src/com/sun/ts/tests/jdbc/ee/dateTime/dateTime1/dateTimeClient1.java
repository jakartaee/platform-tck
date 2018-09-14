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
 * @(#)dateTimeClient1.java	1.18 03/05/16
 */

/*
 * @(#)dateTimeClient1.java	1.16 02/08/27
 */

package com.sun.ts.tests.jdbc.ee.dateTime.dateTime1;

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
 * The dateTimeClient1 class tests methods of Timestamp class using Sun's J2EE
 * Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class dateTimeClient1 extends ServiceEETest implements Serializable {
  private static final String testName = "jdbc.ee.dateTime.dateTime1";

  // Naming specific member variables
  private TSNamingContextInterface jc = null;

  // Harness requirements
  private String sqlStmt = null;

  private Properties sqlp = null;

  private int valToGeneralize = 0;

  private Calendar cal = null;

  private java.sql.Timestamp inTimeVal = null;

  private java.sql.Timestamp inTimeVal1 = null;

  private JDBCTestMsg msg = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    dateTimeClient1 theTests = new dateTimeClient1();
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
      TimeZone tz = null;
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
   * @testName: testTimestamp01
   * 
   * @assertion_ids: JDBC:JAVADOC:32;
   * 
   * @test_Strategy: Create a Timestamp Object with a long value as an argument.
   * Then get the String representation of that Timestamp object. Check whether
   * it is same as equivalent String Value in the property file.
   */
  public void testTimestamp01() throws Fault {
    String sTimestampVal = null;
    String sLongTstampVal = null;
    String sInTimeVal = null;
    boolean booRetVal;
    try {
      sTimestampVal = sqlp.getProperty("DateTime_Str_Val1", "");
      msg.setMsg("Timestamp String Value :  " + sTimestampVal);

      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long tstampVal = Long.parseLong(sLongTstampVal);
      tstampVal = tstampVal - valToGeneralize;

      inTimeVal = new Timestamp(tstampVal);
      msg.setMsg("Timestamp Value  " + inTimeVal);

      sInTimeVal = inTimeVal.toString().trim();
      msg.setMsg("the string after timestamp creation is " + sInTimeVal);

      booRetVal = sTimestampVal.equals(sInTimeVal);
      if (booRetVal) {
        msg.setMsg("Timestamp Constructor constructs Timestamp object");
      } else {
        msg.printTestError(
            "Timestamp Constructor does not construct the expected Timestamp object",
            "Call to Timestamp Constructor is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to Timestamp Constructor is Failed!");

    }
  }

  /*
   * @testName: testTimestamp02
   * 
   * @assertion_ids: JDBC:JAVADOC:32;
   * 
   * @test_Strategy: Create a Timestamp Object with a long value as an argument.
   * Then get the String representation of that Timestamp object. Check whether
   * it is same as equivalent String Value in the property file.
   */

  public void testTimestamp02() throws Fault {
    String sTimestampVal = null;
    String sInTimeVal = null;
    String sLongTstampVal = null;
    boolean booRetVal;
    try {

      sTimestampVal = sqlp.getProperty("DateTime_Str_Val2", "");
      msg.setMsg("Timestamp String Value :  " + sTimestampVal);

      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val2", "");
      long tstampVal = Long.parseLong(sLongTstampVal);
      tstampVal = tstampVal - valToGeneralize;

      inTimeVal = new Timestamp(tstampVal);
      sInTimeVal = inTimeVal.toString().trim();
      msg.setMsg("the string after timestamp creation is" + sInTimeVal);

      booRetVal = sTimestampVal.equals(sInTimeVal);
      if (booRetVal) {
        msg.setMsg("Timestamp Constructor constructs the Timestamp object");
      } else {
        msg.printTestError(
            "Timestamp Constructor does not construct the expected Timestamp object",
            "Call to Timestamp Constructor is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to Timestamp Constructor is Failed!");

    }
  }

  /*
   * @testName: testSetNanos01
   * 
   * @assertion_ids: JDBC:JAVADOC:38;
   * 
   * @test_Strategy: Get a Timestamp object and call the setNanos(int n) method
   * and call getNanos() to check and it should return an Integer value that is
   * been set
   */
  public void testSetNanos01() throws Fault {
    String sLongTstampVal = null;
    int nanoVal = 0;
    try {
      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long tstampVal = Long.parseLong(sLongTstampVal);
      inTimeVal = new Timestamp(tstampVal - valToGeneralize);
      inTimeVal.setNanos(0);

      nanoVal = inTimeVal.getNanos();
      if (nanoVal == 0) {
        msg.setMsg("setNanos method sets the Nano seconds to Timestamp object");
      } else {
        msg.printTestError(
            "setNanos method does not set the Nano seconds to Timestamp object",
            "Call to setNanos is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to setNanos is Failed!");

    }
  }

  /*
   * @testName: testSetNanos02
   * 
   * @assertion_ids: JDBC:JAVADOC:38; JDBC:JAVADOC:37;
   * 
   * 
   * @test_Strategy: Get a Timestamp object and call the setNanos(int n) method
   * and call getNanos() to check and it should return an Integer value that is
   * been set
   */

  public void testSetNanos02() throws Fault {
    String sLongTstampVal = null;
    int nanoVal = 0;

    try {
      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long tstampVal = Long.parseLong(sLongTstampVal);
      tstampVal = tstampVal - valToGeneralize;

      inTimeVal = new Timestamp(tstampVal);
      inTimeVal.setNanos(999999999);

      nanoVal = inTimeVal.getNanos();
      if (nanoVal == 999999999) {
        msg.setMsg(
            "setNanos method sets the Nano seconds for Timestamp object");
      } else {
        msg.printTestError(
            "setNanos method does not set the Nano seconds for Timestamp object",
            "Call to setNanos is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to setNanos is Failed!");

    }
  }

  /*
   * @testName: testSetNanos03
   * 
   * @assertion_ids: JDBC:JAVADOC:38;
   * 
   * @test_Strategy: Get a Timestamp object and call the setNanos(int n) method
   * with the invalid value of argument and it should throw
   * IllegalArgumentException
   */

  public void testSetNanos03() throws Fault {
    boolean illArgExceptFlag = false;
    String sLongTstampVal = null;
    try {
      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long tstampVal = Long.parseLong(sLongTstampVal);
      inTimeVal = new Timestamp(tstampVal - valToGeneralize);

      try {
        inTimeVal.setNanos(-1);
      } catch (IllegalArgumentException e) {
        TestUtil.printStackTrace(e);

        illArgExceptFlag = true;
      }

      if (illArgExceptFlag) {
        msg.setMsg("setNanos Method does not set the out of range value");
      } else {
        msg.printTestError(
            "setNanos Method sets the value which is exceeding limit",
            "Call to setNanos is Failed");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to setNanos is Failed");

    }
  }

  /*
   * @testName: testSetNanos04
   * 
   * @assertion_ids: JDBC:JAVADOC:38;
   * 
   * @test_Strategy: Get a Timestamp object and call the setNanos(int n) method
   * with an invalid value as argument and it should throw
   * IllegalArgumentException
   */
  public void testSetNanos04() throws Fault {
    boolean illArgExceptFlag = false;
    String sLongTstampVal = null;
    try {
      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long tstampVal = Long.parseLong(sLongTstampVal);
      inTimeVal = new Timestamp(tstampVal - valToGeneralize);

      try {
        inTimeVal.setNanos(1000000000);
      } catch (IllegalArgumentException e) {
        TestUtil.printStackTrace(e);

        illArgExceptFlag = true;
      }

      if (illArgExceptFlag) {
        msg.setMsg("setNanos Method does not set the exceeding limit");
      } else {
        msg.printTestError(
            "setNanos Method sets the value which is exceeding limit",
            "Call to setNanos is Failed");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to setNanos is Failed");

    }
  }

  /*
   * @testName: testGetNanos
   * 
   * @assertion_ids: JDBC:JAVADOC:37;
   * 
   * @test_Strategy: Get a Timestamp object and call the getNanos() method. It
   * should return an Integer value.
   */

  public void testGetNanos() throws Fault {
    String sLongTstampVal = null;
    int nanoVal = 0;

    try {
      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long tstampVal = Long.parseLong(sLongTstampVal);
      inTimeVal = new Timestamp(tstampVal - valToGeneralize);

      nanoVal = inTimeVal.getNanos();
      if (nanoVal >= 0 || nanoVal <= 999999999) {
        msg.setMsg(
            "getNanos method returns the Nano seconds of Timestamp object"
                + nanoVal);
      } else {
        msg.printTestError(
            "getNanos() does not return the Nano seconds of Timestamp object",
            "Call to getNanos is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to getNanos is Failed!");

    }
  }

  /*
   * @testName: testToString01
   * 
   * @assertion_ids: JDBC:JAVADOC:36;
   * 
   * @test_Strategy: Create a Timestamp Object with a long value as an argument.
   * Then get the String representation of that Timestamp object. using the
   * toString() method.Check whether it is same as equivalent String Value in
   * property file.
   */
  public void testToString01() throws Fault {
    String sTimestampVal = null;
    String sLongTstampVal = null;
    String sInTimeVal = null;
    try {
      sTimestampVal = sqlp.getProperty("DateTime_Str_Val1", "");
      msg.setMsg("Timestamp String Value :  " + sTimestampVal);

      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long tstampVal = Long.parseLong(sLongTstampVal);
      tstampVal = tstampVal - valToGeneralize;

      inTimeVal = new Timestamp(tstampVal);
      sInTimeVal = inTimeVal.toString();
      msg.setMsg("sInTimeVal = " + sInTimeVal);

      if (sInTimeVal.equals(sTimestampVal))
        msg.setMsg("toString method returns a String object");
      else {
        msg.printTestError("toString method does not return the String object",
            "Call to toString is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to toString is Failed!");

    }
  }

  /*
   * @testName: testToString02
   * 
   * @assertion_ids: JDBC:JAVADOC:36;
   * 
   * @test_Strategy: Create a Timestamp Object with a long value as an argument.
   * Then get the String representation of that Timestamp object. using the
   * toString() method.Check whether it is same as equivalent String Value in
   * property file.
   * 
   */
  public void testToString02() throws Fault {
    String sLongTstampVal = null;
    String sTimestampVal = null;
    String retTstampVal = null;
    try {
      sTimestampVal = sqlp.getProperty("DateTime_Str_Val2", "");
      msg.setMsg("Timestamp String Value :  " + sTimestampVal);

      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val2", "");
      long tstampVal = Long.parseLong(sLongTstampVal);
      tstampVal = tstampVal - valToGeneralize;

      inTimeVal = new Timestamp(tstampVal);
      retTstampVal = inTimeVal.toString().trim();

      if (retTstampVal.equals(sTimestampVal))
        msg.setMsg("toString method returns a String object");
      else {
        msg.printTestError("toString method does not return a String object",
            "Call to toString is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to toString is Failed!");

    }
  }

  /*
   * @testName: testAfter01
   * 
   * @assertion_ids: JDBC:JAVADOC:42;
   * 
   * @test_Strategy: Get a Timestamp object and call the after(Timestamp ts)
   * method with the value of ts is after than the Timestamp It should return a
   * boolean value and the value should be equal to true
   */
  public void testAfter01() throws Fault {
    String sLongTstampVal = null;
    String sRefVal = null;
    boolean booRetVal;
    java.sql.Timestamp inTimeVal2 = null;

    try {
      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long tstampVal1 = Long.parseLong(sLongTstampVal);
      tstampVal1 = tstampVal1 - valToGeneralize;

      sRefVal = sqlp.getProperty("Ref_Milli_Val", "");
      long tstampVal2 = Long.parseLong(sRefVal);
      tstampVal2 = tstampVal1 - valToGeneralize - tstampVal2;

      inTimeVal1 = new Timestamp(tstampVal1);
      msg.setMsg("Timestamp Value # 1 : " + inTimeVal1);

      inTimeVal2 = new Timestamp(tstampVal2);
      msg.setMsg("Timestamp Value # 2 : " + inTimeVal2);

      booRetVal = inTimeVal1.after(inTimeVal2);
      if (booRetVal) {
        msg.setMsg("after method returns  " + booRetVal);
      } else {
        msg.printTestError("after method does not return expected Value ",
            "Call to after is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to after is Failed!");

    }
  }

  /*
   * @testName: testAfter02
   * 
   * @assertion_ids: JDBC:JAVADOC:42;
   * 
   * @test_Strategy: Get a Timestamp object and call the after(Timestamp ts)
   * method with the value of ts is not after than the Timestamp It should
   * return a boolean value and the value should be equal to false
   */
  public void testAfter02() throws Fault {
    String sLongTstampVal = null;
    String sRefVal = null;
    java.sql.Timestamp inTimeVal2 = null;
    boolean booRetVal;
    try {
      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long tstampVal1 = Long.parseLong(sLongTstampVal);
      tstampVal1 = tstampVal1 - valToGeneralize;

      sRefVal = sqlp.getProperty("Ref_Milli_Val", "");
      long tstampVal2 = Long.parseLong(sRefVal);
      tstampVal2 = tstampVal1 - valToGeneralize + tstampVal2;

      inTimeVal1 = new Timestamp(tstampVal1);
      msg.setMsg("Timestamp Value # 1 : " + inTimeVal1);

      inTimeVal2 = new Timestamp(tstampVal2);
      msg.setMsg("Timestamp Value # 2 : " + inTimeVal2);

      booRetVal = inTimeVal1.after(inTimeVal2);
      if (!booRetVal)
        msg.setMsg("after method returns : " + booRetVal);
      else {
        msg.printTestError("after method does not return the expected Value ",
            "Call to after is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to after is Failed!");

    }
  }

  /*
   * @testName: testAfter03
   * 
   * @assertion_ids: JDBC:JAVADOC:42;
   * 
   * @test_Strategy: Get a Timestamp object and call the after(Timestamp ts)
   * method with the value of ts is after than the Timestamp in Nano second
   * level It should return a boolean value and the value should be equal to
   * true
   */
  public void testAfter03() throws Fault {
    Timestamp inTimeVal2 = null;
    String sLongTstampVal = null;
    String sNanosVal = null;
    boolean booRetVal;
    try {
      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long tstampVal1 = Long.parseLong(sLongTstampVal);
      tstampVal1 = tstampVal1 - valToGeneralize;

      sNanosVal = sqlp.getProperty("Ref_Nano_Val", "");
      int nanosSet = Integer.parseInt(sNanosVal);

      inTimeVal1 = new Timestamp(tstampVal1);
      inTimeVal1.setNanos(nanosSet);
      msg.setMsg("Timestamp Value # 1  :  " + inTimeVal1);

      inTimeVal2 = new Timestamp(tstampVal1);
      msg.setMsg("Timestamp Value # 2  :  " + inTimeVal2);

      booRetVal = inTimeVal1.after(inTimeVal2);
      if (booRetVal)
        msg.setMsg("after method returns" + booRetVal);
      else {
        msg.printTestError("after method does not return the expected Value ",
            "Call to after is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to after is Failed!");

    }
  }

  /*
   * @testName: testAfter04
   * 
   * @assertion_ids: JDBC:JAVADOC:42;
   * 
   * @test_Strategy: Get a Timestamp object and call the after(Timestamp ts)
   * method with the value of ts is not after than the Timestamp with Nano
   * seconds level It should return a boolean value and the value should be
   * equal to false
   */
  public void testAfter04() throws Fault {
    String sLongTstampVal = null;
    boolean booRetVal;
    try {
      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long tstampVal = Long.parseLong(sLongTstampVal);
      tstampVal = tstampVal - valToGeneralize;

      inTimeVal = new Timestamp(tstampVal);
      msg.setMsg("TimeStamp Value # 1 :  " + inTimeVal);

      inTimeVal1 = new Timestamp(tstampVal);

      String sNanosVal = sqlp.getProperty("Ref_Nano_Val", "");
      int nanoVal = Integer.parseInt(sNanosVal);

      inTimeVal1.setNanos(nanoVal);
      msg.setMsg("TimeStamp Value # 2 :  " + inTimeVal1);

      booRetVal = inTimeVal.after(inTimeVal1);
      if (!booRetVal)
        msg.setMsg("after method returns " + booRetVal);
      else {
        msg.printTestError("after method does not return the expected value",
            "Call to after is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to after is Failed!");

    }
  }

  /*
   * @testName: testBefore01
   * 
   * @assertion_ids: JDBC:JAVADOC:41;
   * 
   * @test_Strategy: Get a Timestamp object and call the before(Timestamp ts)
   * method with the value of ts is before than the Timestamp It should return a
   * boolean value and the value should be equal to true
   */
  public void testBefore01() throws Fault {
    String sTimestampVal = null;
    String sLongTstampVal = null;
    String sRefVal = null;
    boolean booRetVal;
    try {
      sTimestampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      msg.setMsg("Timestamp String Value :  " + sTimestampVal);

      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long tstampVal1 = Long.parseLong(sLongTstampVal);
      tstampVal1 = tstampVal1 - valToGeneralize;

      sRefVal = sqlp.getProperty("Ref_Milli_Val", "");
      long tstampVal2 = Long.parseLong(sRefVal);
      tstampVal2 = tstampVal1 - valToGeneralize + tstampVal2;

      inTimeVal = new Timestamp(tstampVal1);
      msg.setMsg("Timestamp Value1 : " + inTimeVal);

      inTimeVal1 = new Timestamp(tstampVal2);
      msg.setMsg("Timestamp Value2 : " + inTimeVal1);

      booRetVal = inTimeVal.before(inTimeVal1);
      if (booRetVal)
        msg.setMsg("before method returns : " + booRetVal);
      else {
        msg.printTestError("before method does not return the expected value ",
            "Call to before is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to before is Failed!");

    }
  }

  /*
   * @testName: testBefore02
   * 
   * @assertion_ids: JDBC:JAVADOC:41;
   * 
   * @test_Strategy: Get a Timestamp object and call the before(Timestamp ts)
   * method with the value of ts is not before than the Timestamp It should
   * return a boolean value and the value should be equal to false
   *
   */
  public void testBefore02() throws Fault {
    String sTimestampVal = null;
    String sLongTstampVal = null;
    String sRefVal = null;
    boolean booRetVal;
    try {
      sTimestampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      msg.setMsg("Timestamp String Value :  " + sTimestampVal);

      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long tstampVal1 = Long.parseLong(sLongTstampVal);
      tstampVal1 = tstampVal1 - valToGeneralize;

      sRefVal = sqlp.getProperty("Ref_Milli_Val", "");
      long tstampVal2 = Long.parseLong(sRefVal);
      tstampVal2 = tstampVal1 - valToGeneralize - tstampVal2;

      inTimeVal = new Timestamp(tstampVal1);
      msg.setMsg("Timestamp Value1 :  " + inTimeVal);

      inTimeVal1 = new Timestamp(tstampVal2);
      msg.setMsg("Timestamp Value2 :  " + inTimeVal1);

      booRetVal = inTimeVal.before(inTimeVal1);
      if (!booRetVal)
        msg.setMsg("before method returns : " + booRetVal);
      else {
        msg.printTestError("before method does not return the expected Value ",
            "Call to before is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to before is Failed!");

    }
  }

  /*
   * @testName: testBefore03
   * 
   * @assertion_ids: JDBC:JAVADOC:41;
   * 
   * @test_Strategy: Get a Timestamp object and call the before(Timestamp ts)
   * method with the value of ts is before than the Timestamp in Nano second
   * level It should return a boolean value and the value should be equal to
   * true.
   * 
   */
  public void testBefore03() throws Fault {
    String sLongTstampVal = null;
    Timestamp inTimeVal1 = null;
    boolean booRetVal;

    try {
      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long tstampVal = Long.parseLong(sLongTstampVal);
      tstampVal = tstampVal - valToGeneralize;

      inTimeVal = new Timestamp(tstampVal);
      msg.setMsg("Timestamp Value # 1 : " + inTimeVal);

      inTimeVal1 = new Timestamp(tstampVal);
      String sNanosVal = sqlp.getProperty("Ref_Nano_Val", "");
      int nanosSet = Integer.parseInt(sNanosVal);
      inTimeVal1.setNanos(nanosSet);
      msg.setMsg("Timestamp Value # 2 : " + inTimeVal1);

      booRetVal = inTimeVal.before(inTimeVal1);
      if (booRetVal) {
        msg.setMsg("before method returns " + booRetVal);
      } else {
        msg.printTestError("before method does not return the expected value ",
            "Call to before is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to before is Failed!");

    }
  }

  /*
   * @testName: testBefore04
   * 
   * @assertion_ids: JDBC:JAVADOC:41;
   * 
   * @test_Strategy: Get a Timestamp object and call the before(Timestamp ts)
   * method with the value of ts is not before than the Timestamp with Nano
   * seconds level It should return a boolean value and the value should be
   * equal to false
   */
  public void testBefore04() throws Fault {
    String sLongTstampVal = null;
    String sNanosVal = null;
    boolean booRetVal;

    try {
      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long tstampVal = Long.parseLong(sLongTstampVal);
      tstampVal = tstampVal - valToGeneralize;

      inTimeVal = new Timestamp(tstampVal);
      msg.setMsg("Timestamp Value 1" + inTimeVal);

      inTimeVal1 = new Timestamp(tstampVal);
      sNanosVal = sqlp.getProperty("Ref_Nano_Val", "");
      int nanosSet = Integer.parseInt(sNanosVal);
      inTimeVal1.setNanos(nanosSet);
      msg.setMsg("Timestamp Value 2" + inTimeVal1);

      booRetVal = inTimeVal.before(inTimeVal1);
      if (booRetVal) {
        msg.setMsg("before method returns " + booRetVal);
      } else {
        msg.printTestError("before method does not return the expected Value",
            "Call to before is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to before is Failed!");

    }
  }

  /*
   * @testName: testEqualsTimestamp01
   * 
   * @assertion_ids: JDBC:JAVADOC:39;
   * 
   * @test_Strategy: Get a Timestamp object and call the equals(Timestamp ts)
   * method with equal value of Timestamp It should return a boolean value and
   * the value should be equal to true
   */
  public void testEqualsTimestamp01() throws Fault {
    String sLongTstampVal = null;
    boolean booRetVal;
    try {
      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long tstampVal1 = Long.parseLong(sLongTstampVal);
      tstampVal1 = tstampVal1 - valToGeneralize;

      String sRefMilliVal = sqlp.getProperty("Ref_Milli_Val", "");
      long tstampVal2 = Long.parseLong(sRefMilliVal);
      tstampVal2 = tstampVal2 - valToGeneralize;

      String sRefNanoVal = sqlp.getProperty("Ref_Nano_Val2", "");
      int setNanosVal = Integer.parseInt(sRefNanoVal);

      inTimeVal = new Timestamp(tstampVal1);
      msg.setMsg("Timestamp Value1 : " + inTimeVal);

      inTimeVal1 = new Timestamp(tstampVal2);
      inTimeVal1.setNanos(setNanosVal);
      msg.setMsg("Timestamp Value2 : " + inTimeVal1);

      booRetVal = inTimeVal.equals(inTimeVal1);
      if (booRetVal) {
        msg.setMsg("The method equals() returns the expected value");
      } else {
        msg.printTestError("The method equals() doesnt work as expected",
            "call to equals() failed");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "call to equals() failed");

    }
  }

  /*
   * @testName: testEqualsObject01
   * 
   * @assertion_ids: JDBC:JAVADOC:40;
   * 
   * @test_Strategy: Get a Timestamp object and call the equals(Object obj)
   * method with equal value of Timestamp It should return a boolean value and
   * the value should be equal to true
   */
  public void testEqualsObject01() throws Fault {
    String sLongTstampVal = null;
    boolean booRetVal;
    try {
      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long tstampVal1 = Long.parseLong(sLongTstampVal);
      tstampVal1 = tstampVal1 - valToGeneralize;

      inTimeVal = new Timestamp(tstampVal1);
      msg.setMsg("Timestamp Value1:  " + inTimeVal);

      Object tsObj = new Timestamp(tstampVal1);
      msg.setMsg("Timestamp Object Value1:  " + tsObj);

      booRetVal = inTimeVal.equals(tsObj);
      if (booRetVal) {
        msg.setMsg("The method equals() returns the expected value");
      } else {
        msg.printTestError("The method equals() doesnt work as expected",
            "call to equals() failed");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to equals is Failed!");

    }
  }

  /*
   * @testName: testEqualsObject02
   * 
   * @assertion_ids: JDBC:JAVADOC:40;
   * 
   * @test_Strategy: Get a Timestamp object and call the equals(Object obj)
   * method with equal value of Timestamp in Nano seconds level It should return
   * a boolean value and the value should be equal to true
   */
  public void testEqualsObject02() throws Fault {
    String sTimestampVal = null;
    String sLongTstampVal = null;
    boolean booRetVal;
    try {
      sTimestampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      msg.setMsg("Timestamp String Value :  " + sTimestampVal);

      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long tstampVal1 = Long.parseLong(sLongTstampVal);
      tstampVal1 = tstampVal1 - valToGeneralize;

      String sRefMilliVal = sqlp.getProperty("Ref_Milli_Val", "");
      long tstampVal2 = Long.parseLong(sRefMilliVal);
      tstampVal2 = tstampVal2 - valToGeneralize;

      String sRefNanoVal = sqlp.getProperty("Ref_Nano_Val2", "");
      int setNanosVal = Integer.parseInt(sRefNanoVal);

      inTimeVal = new Timestamp(tstampVal2);
      inTimeVal.setNanos(setNanosVal);
      msg.setMsg("TimeStamp Value    " + inTimeVal);

      Object tsObj = new Timestamp(tstampVal1);
      msg.setMsg("Object Value " + tsObj);

      booRetVal = inTimeVal.equals(tsObj);
      if (booRetVal) {
        msg.setMsg("The method equals() returns the expected value");
      } else {
        msg.printTestError("The method equals() doesnt work as expected",
            "Call to equals is Failed!");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "Call to equals is Failed!");

    }
  }

  /*
   * @testName: testValueOf01
   * 
   * @assertion_ids: JDBC:JAVADOC:35;
   * 
   * @test_Strategy: Call valueof(String ts) static method in java.sql.Timestamp
   * class with a String argument to get a Timestamp object Check whether it is
   * same as Timestamp object obtained from equivalent long value .
   */

  public void testValueOf01() throws Fault {
    String sTimestampVal = null;
    String sLongTstampVal = null;
    String sTestTimestampVal = null;
    boolean booRetVal;
    String sInTimeVal = null;

    try {
      sTimestampVal = sqlp.getProperty("DateTime_Str_Val1", "");
      msg.setMsg("Timestamp String Value :  " + sTimestampVal);

      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val1", "");
      long tstampVal1 = Long.parseLong(sLongTstampVal);
      tstampVal1 = tstampVal1 - valToGeneralize;

      inTimeVal = new Timestamp(tstampVal1);
      sInTimeVal = inTimeVal.toString();
      msg.setMsg("TimeStamp Value    " + inTimeVal);

      inTimeVal1 = Timestamp.valueOf(sTimestampVal);
      sTestTimestampVal = inTimeVal1.toString().trim();
      msg.setMsg("Test Timestamp object's Value " + sTestTimestampVal);

      booRetVal = sInTimeVal.equals(sTestTimestampVal);
      if (booRetVal) {
        msg.setMsg("The method valueOf() returns the correct object");
      } else {
        msg.printTestError("The method valueOf() doesnt work as expected",
            "call to valueOf() failed");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "call to valueOf() failed");

    }
  }

  /*
   * @testName: testValueOf02
   * 
   * @assertion_ids: JDBC:JAVADOC:35;
   * 
   * @test_Strategy: Call valueof(String ts) static method in java.sql.Timestamp
   * class with a String argument to get a Timestamp object Check whether it is
   * same as Timestamp object obtained from equivalent long value .
   */
  public void testValueOf02() throws Fault {
    String sTimestampVal = null;
    String sLongTstampVal = null;
    String sTestTimestampVal = null;
    boolean booRetVal;
    String sInTimeVal = null;

    try {
      sTimestampVal = sqlp.getProperty("DateTime_Str_Val2", "");
      msg.setMsg("Timestamp String Value :  " + sTimestampVal);

      sLongTstampVal = sqlp.getProperty("DateTime_Long_Val2", "");
      long tstampVal1 = Long.parseLong(sLongTstampVal);
      tstampVal1 = tstampVal1 - valToGeneralize;

      inTimeVal = new Timestamp(tstampVal1);
      sInTimeVal = inTimeVal.toString();
      msg.setMsg("TimeStamp Value    " + inTimeVal);

      inTimeVal1 = Timestamp.valueOf(sTimestampVal);
      sTestTimestampVal = inTimeVal1.toString().trim();
      msg.setMsg("Test Timestamp object's Value " + sTestTimestampVal);

      booRetVal = sInTimeVal.equals(sTestTimestampVal);
      if (booRetVal) {
        msg.setMsg("The method valueOf() returns the correct object");
      } else {
        msg.printTestError("The method valueOf() doesnt work as expected",
            "call to valueOf() failed");

      }
      msg.printTestMsg();
    } catch (Exception e) {
      msg.printError(e, "call to valueOf() failed");

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
