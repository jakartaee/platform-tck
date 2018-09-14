/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsonp.api.jsonnumbertests;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import javax.json.*;
import javax.json.stream.*;
import java.io.*;

import com.sun.javatest.Status;
import java.util.Properties;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.sun.ts.tests.jsonp.common.*;

public class Client extends ServiceEETest {
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props:
   */

  public void setup(String[] args, Properties p) throws Fault {
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /* Tests */

  /*
   * @testName: jsonNumberEqualsTest
   * 
   * @assertion_ids: JSONP:JAVADOC:250;
   * 
   * @test_Strategy: Tests JsonNumber equals method. Create 2 equal JsonNumbers
   * and compare them for equality and expect true. Create 2 non-equal
   * JsonNumbers and compare them for equality and expect false.
   */
  public void jsonNumberEqualsTest() throws Fault {
    boolean pass = true;
    try {
      logMsg("Create sample JsonNumber 1 for testing");
      JsonNumber number1 = JSONP_Util.createJsonNumber(10);
      logMsg("number1=" + JSONP_Util.toStringJsonNumber(number1));

      logMsg("Create sample JsonNumber 2 for testing");
      JsonNumber number2 = JSONP_Util.createJsonNumber(10);
      logMsg("number2=" + JSONP_Util.toStringJsonNumber(number2));

      logMsg(
          "Call JsonNumber.equals() to compare 2 equal JsonNumbers and expect true");
      if (number1.equals(number2)) {
        logMsg("JsonNumbers are equal - expected.");
      } else {
        pass = false;
        logErr("JsonNumbers are not equal - unexpected.");
      }

      logMsg("Create sample JsonNumber 1 for testing");
      number1 = JSONP_Util.createJsonNumber(10);
      logMsg("number1=" + JSONP_Util.toStringJsonNumber(number1));

      logMsg("Create sample JsonNumber 2 for testing");
      number2 = JSONP_Util.createJsonNumber((double) 10.25);
      logMsg("number2=" + JSONP_Util.toStringJsonNumber(number2));

      logMsg(
          "Call JsonNumber.equals() to compare 2 equal JsonNumbers and expect false");
      if (!number1.equals(number2)) {
        logMsg("JsonNumbers are not equal - expected.");
      } else {
        pass = false;
        logErr("JsonNumbers are equal - unexpected.");
      }
    } catch (Exception e) {
      throw new Fault("jsonNumberEqualsTest Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonNumberEqualsTest Failed");
  }

  /*
   * @testName: jsonNumberHashCodeTest
   * 
   * @assertion_ids: JSONP:JAVADOC:251;
   * 
   * @test_Strategy: Tests JsonNumber equals method. Create 2 equal JsonNumbers
   * and compare them for hashcode and expect true. Create 2 non-equal
   * JsonNumbers and compare them for hashcode and expect false.
   */
  public void jsonNumberHashCodeTest() throws Fault {
    boolean pass = true;
    try {
      logMsg("Create sample JsonNumber 1 for testing");
      JsonNumber number1 = JSONP_Util.createJsonNumber(10);
      logMsg("number1=" + JSONP_Util.toStringJsonNumber(number1));
      logMsg("number1.hashCode()=" + number1.hashCode());

      logMsg("Create sample JsonNumber 2 for testing");
      JsonNumber number2 = JSONP_Util.createJsonNumber(10);
      logMsg("number2=" + JSONP_Util.toStringJsonNumber(number2));
      logMsg("number2.hashCode()=" + number2.hashCode());

      logMsg(
          "Call JsonNumber.hashCode() to compare 2 equal JsonNumbers and expect true");
      if (number1.hashCode() == number2.hashCode()) {
        logMsg("JsonNumbers hashCode are equal - expected.");
      } else {
        pass = false;
        logErr("JsonNumbers hashCode are not equal - unexpected.");
      }

      logMsg("Create sample JsonNumber 1 for testing");
      number1 = JSONP_Util.createJsonNumber(10);
      logMsg("number1=" + JSONP_Util.toStringJsonNumber(number1));
      logMsg("number1.hashCode()=" + number1.hashCode());

      logMsg("Create sample JsonNumber 2 for testing");
      number2 = JSONP_Util.createJsonNumber((double) 10.25);
      logMsg("number2=" + JSONP_Util.toStringJsonNumber(number2));
      logMsg("number2.hashCode()=" + number2.hashCode());

      logMsg(
          "Call JsonNumber.hashCode() to compare 2 equal JsonNumbers and expect false");
      if (number1.hashCode() != number2.hashCode()) {
        logMsg("JsonNumbers hashCode are not equal - expected.");
      } else {
        pass = false;
        logErr("JsonNumbers hashCode are equal - unexpected.");
      }
    } catch (Exception e) {
      throw new Fault("jsonNumberHashCodeTest Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonNumberHashCodeTest Failed");
  }

  /*
   * @testName: jsonNumberIsIntegralTest
   * 
   * @assertion_ids: JSONP:JAVADOC:51;
   * 
   * @test_Strategy: Test JsonNumber.isIntegral() method.
   */
  public void jsonNumberIsIntegralTest() throws Fault {
    boolean pass = true;
    JsonNumber jsonNumber = null;
    try {
      // INTEGRAL NUMBER TEST
      JsonNumber number1 = JSONP_Util.createJsonNumber(123);
      if (!JSONP_Util.assertEqualsJsonNumberType(number1.isIntegral(),
          JSONP_Util.INTEGRAL))
        pass = false;
      else {
        if (!JSONP_Util.assertEquals(123, number1.intValue()))
          pass = false;
      }
      // NON_INTEGRAL NUMBER TEST
      JsonNumber number2 = JSONP_Util.createJsonNumber(12345.45);
      if (!JSONP_Util.assertEqualsJsonNumberType(number2.isIntegral(),
          JSONP_Util.NON_INTEGRAL))
        pass = false;
      else {
        if (!JSONP_Util.assertEquals(12345.45, number2.doubleValue()))
          pass = false;
      }

    } catch (Exception e) {
      throw new Fault("jsonNumberIsIntegralTest Failed: ", e);
    }

    if (!pass)
      throw new Fault("jsonNumberIsIntegralTest Failed");
  }
}
